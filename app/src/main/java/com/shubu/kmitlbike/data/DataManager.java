package com.shubu.kmitlbike.data;

import android.bluetooth.BluetoothClass;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.provider.ContactsContract;

import com.annimon.stream.Stream;
import com.google.zxing.Result;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.adapter.LocationAdapter;
import com.shubu.kmitlbike.data.model.History;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.data.model.Token;
import com.shubu.kmitlbike.data.model.UserSession;
import com.shubu.kmitlbike.data.model.VersionForm;
import com.shubu.kmitlbike.data.model.VersionResponse;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowRequest;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowResponse;
import com.shubu.kmitlbike.data.model.bike.BikeReturnForm;
import com.shubu.kmitlbike.data.model.bike.BikeReturnResponse;
import com.shubu.kmitlbike.data.model.bike.Session;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.ui.common.ErrorFactory;
import com.shubu.kmitlbike.util.BluetoothUtil;
import com.shubu.kmitlbike.util.ChineseBikeUtil;
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
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@Singleton
public class DataManager {

    private PublishSubject<BikeState> usageStatus;
    private PublishSubject<String> errorStatus;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private final Router mRouter;
    private List<Bike> bikeList;
    private Bike usingBike;
    private LoginResponse currentUser;
    private List<UsagePlan> usagePlans;
    private Location currentLocation = null;
    private BluetoothUtil util;
    private PublishSubject<String> commandNotification;
    private String nonce = null;


    @Inject
    public DataManager(Router router) {
        mRouter = router;
    }

    public PublishSubject<String> getErrorSubject() {
        if (errorStatus == null)
            errorStatus = PublishSubject.create();
        return errorStatus;
    }

    public Single<LoginResponse> login(String username, String password) {
        Timber.i("gonna login");
        return mRouter.login(new LoginForm(username, password));

    }

    public Single<LoginResponse> validateToken(String token){
        return mRouter.tokenLogin( new Token(token) );
    }

    public Single<VersionResponse> validateVersion(String version){
        return mRouter.getVersion( new VersionForm("android", version) );
    }

    public boolean validateBikeReturn(Bike bike){
        return bike.getBikeName().equals(this.usingBike.getBikeName());
    }

    //BIKE MANAGER

    public Single<List<UsagePlan>> getUsagePlan() {
        return mRouter.getUsagePlan();
    }

    public Single<List<Bike>> getBikeList() {
        return mRouter.getBikeList();
    }

    public void performReturn(Bike bike, Location location) {
        usageStatus.onNext(BikeState.RETURN_SCAN_START);
        if (bike.getBikeModel().equals(CONSTANTS.LA_GREEN)) {
            usageStatus.onNext(BikeState.RETURN_START);
        } else {
            usageStatus.onNext(BikeState.RETURN_START);
            if (this.util == null)
                this.util = new BluetoothUtil(bike);
            this.util.initBluetoothService();
        }
        Disposable task = mRouter.returnBike(bike.getId(), new BikeReturnForm(LocationAdapter.makeLocationForm(location), false)).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                    bikeReturnResponse -> {
                        usageStatus.onComplete();
                        this.util.dispose();
                    },
                    throwable -> errorStatus.onNext("Unable to return bike... Try again later")
            );

    }

    public Single<List<ProfileHistory>> getHistoryList(){
        return mRouter.getHistoriesList(this.currentUser.getId());
    }
    public Single<History> getFullHistory(ProfileHistory history){
        return mRouter.getHistory(this.currentUser.getId(), history.getId());
    }

    public Bike getBikeFromScannerCode(Result code) {
        Timber.e(this.bikeList.toString());
        try {
            List<Bike> result = Stream.of(this.bikeList).filter(bike -> bike.getBarcode().equals(code.getText())).toList();
            return this.usingBike == null? result.get(0) : ( code.getText().equals(this.usingBike.getBarcode())? usingBike: result.get(0));
        } catch (Exception e){
            errorStatus.onNext("");
        }
        return null;
    }


    public void setBikeList(List<Bike> bikeList) {
        Timber.e(bikeList.toString());
        this.bikeList = bikeList;
    }

    public void setUsagePlans(List<UsagePlan> usagePlans) {
        this.usagePlans = usagePlans;
    }

    public Single<UserSession> getUserSession(){
        return mRouter.getUserSession(this.currentUser.getId());
    }

    public PublishSubject<BikeState> initializeBorrowReturnService(Bike bike, boolean borrowFlag) {
        if (borrowFlag)
            this.usingBike = bike;
        else
            this.usingBike = null;
        this.usageStatus = PublishSubject.create();
        return this.usageStatus;
    }

    public LoginResponse getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginResponse currentUser) {
        this.currentUser = currentUser;
    }

    public void performBorrow(Bike bike, Location location) {
//        if (bike.getBikeModel().equals(CONSTANTS.GIANT_ESCAPE)) {
//            if (this.util == null)
//                this.util = new BluetoothUtil(bike);
//            this.util.setEventbus(usageStatus);
//            Disposable bluetooth = this.util.getOnceSubscriber().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .subscribe( item -> {nonce = item; borrowRequest(bike,location, this.util);}, throwable -> errorStatus.onNext("Bluetooth initialization error"));
//            this.util.initBluetoothService();
//        } else {
//            borrowRequest(bike,location, null);
//        }
        if (bike.getBikeModel().equals(CONSTANTS.GIANT_ESCAPE)){
            ChineseBikeUtil util = new ChineseBikeUtil(bike);
            Timber.e("starto");

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

        request.setSelectedPlan(2);
        Disposable borrow = mRouter.borrowBike(bike.getId(), request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(bikeBorrowResponse  -> {
                        Timber.e("borrow requested");
                        switch (bike.getBikeModel()) {
                            case CONSTANTS.GIANT_ESCAPE:
                                Timber.e("message is : " + bikeBorrowResponse.getMessage());
                                bluetoothUtil.borrow(bikeBorrowResponse.getMessage());
                                break;
                            case CONSTANTS.LA_GREEN:
                                usageStatus.onComplete();
                        }
                    },

                    throwable -> {
                        errorStatus.onNext("Unable to connect to the server... error incompleted");
                    }
                );
    }


    public Bike getUsingBike() {
        return usingBike;
    }

    public void setUsingBike(Bike usingBike) {
        this.usingBike = usingBike;
    }

    public void setError(String message){
        errorStatus.onNext(message);
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
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

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
}