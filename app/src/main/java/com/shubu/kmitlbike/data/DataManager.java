package com.shubu.kmitlbike.data;

import android.bluetooth.BluetoothClass;
import android.location.Location;

import com.annimon.stream.Stream;
import com.google.zxing.Result;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.adapter.LocationAdapter;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowRequest;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowResponse;
import com.shubu.kmitlbike.data.model.bike.BikeReturnForm;
import com.shubu.kmitlbike.data.model.bike.BikeReturnResponse;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.util.BluetoothUtil;
import com.shubu.kmitlbike.util.UUIDHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.Single;
import rx.subjects.PublishSubject;
import timber.log.Timber;

@Singleton
public class DataManager {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private final Router mRouter;
    private List<Bike> bikeList;
    private Bike usingBike;
    private List<UsagePlan> usagePlans;
    private PublishSubject<BikeState> usageStatus;
    private Location currentLocation = null;
    private List<Disposable> bluetoothTasks;
    private PublishSubject<String> commandNotification;
    private String nonce = null;

    @Inject
    public DataManager(Router router) {
        mRouter = router;
    }

    public Single<LoginResponse> login(String username, String password) {
        Timber.i("gonna login");
        return mRouter.login(new LoginForm(username, password));

    }

    //BIKE MANAGER

    public Single<List<UsagePlan>> getUsagePlan() {
        return mRouter.getUsagePlan();
    }

    public Single<List<Bike>> getBikeList() {
        return mRouter.getBikeList();
    }

    public Single<BikeReturnResponse> performReturn(Bike bike, Location location) {
        return mRouter.returnBike(bike.getId(), new BikeReturnForm(LocationAdapter.makeLocationForm(location), false));
    }

    public Bike getBikeFromScannerCode(Result code) {
        List<Bike> result = Stream.of(this.bikeList).filter(bike -> bike.getBarcode().equals(code.getText())).toList();
        if (result.size() <= 0) { /* TODO: 4/2/2018 raise GUI error : case -> barcode not found!!! */ }
        return result.get(0);
    }


    public void setBikeList(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }

    public void setUsagePlans(List<UsagePlan> usagePlans) {
        this.usagePlans = usagePlans;
    }

    public PublishSubject<BikeState> initializeBorrowService(Bike bike) {
        this.usingBike = bike;
        this.usageStatus = PublishSubject.create();
        return this.usageStatus;
    }

    public void performBorrow(Bike bike, Location location) {

        if (bike.getBikeModel().equals(CONSTANTS.GIANT_ESCAPE)) {
            BluetoothUtil bluetoothUtil = new BluetoothUtil(bike);
            bluetoothUtil.setEventbus(usageStatus);
            Disposable shit = bluetoothUtil.getOnceSubscriber()
                .subscribe( item -> {nonce = item; borrowRequest(bike,location, bluetoothUtil);}, throwable -> {});
            bluetoothUtil.initBluetoothService();
        } else {
            borrowRequest(bike,location, null);
        }

    }

    private void borrowRequest(Bike bike, Location location, BluetoothUtil bluetoothUtil){
        usageStatus.onNext(BikeState.BORROW_START);
        BikeBorrowRequest request = new BikeBorrowRequest();
        request.setLocation(LocationAdapter.makeLocationForm(location));

        if (this.nonce == null)
            request.setNonce(Math.round(System.nanoTime() / 1000));
        else
            request.setNonce(Integer.parseInt(this.nonce));
        Timber.e(request.toString());
        request.setSelectedPlan(CONSTANTS.SELECTED_PLAN);
        Disposable borrow = mRouter.borrowBike(bike.getId(), request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(bikeBorrowResponse  -> {
                        switch (bike.getBikeModel()) {
                            case CONSTANTS.GIANT_ESCAPE:
                                bluetoothUtil.borrow(bikeBorrowResponse.getMessage());

                                break;
                            case CONSTANTS.LA_GREEN:
                                usageStatus.onCompleted();
                        }
                    },

                    throwable -> {
                        Timber.tag("on borrow : ").e(throwable);
                    }
                );
    }



    public Bike getUsingBike() {
        return usingBike;
    }

    public void setUsingBike(Bike usingBike) {
        this.usingBike = usingBike;
    }


    //LOCATION MANAGER

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public Single<Object> updateLocation(Location location) {
        if (this.isBetterLocation(location, this.currentLocation)) {
            return mRouter.updateTrackingLocation(LocationAdapter.makeLocationForm(location));
        }
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    //BLUETOOTH MANAGER

}