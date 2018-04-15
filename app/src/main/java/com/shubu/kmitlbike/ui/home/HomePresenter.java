package com.shubu.kmitlbike.ui.home;

import android.location.Location;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.zxing.Result;
import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowResponse;
import com.shubu.kmitlbike.data.model.bike.BikeReturnResponse;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.base.BasePresenter;
import com.shubu.kmitlbike.ui.base.MvpView;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.ui.common.ErrorFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import io.reactivex.schedulers.Schedulers;

import rx.Subscriber;
import timber.log.Timber;

public class HomePresenter extends BasePresenter<HomeMVPView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscriptions;


    @Inject
    public HomePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }
    @Override
    public void attachView(HomeMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeDisposable();
    }

    public void subscribeError() {
        mSubscriptions.add(mDataManager.getErrorSubject().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(error -> getMvpView().onError(error)));
    }


    public LoginResponse getUser(){
        return mDataManager.getCurrentUser();
    }

    public void onDestroy(){
        mSubscriptions.dispose();
    }

    public Bike getSession(){
        return mDataManager.getUsingBike();
    }

    public void getBikeList() {
        mSubscriptions.add(mDataManager.getBikeList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(listBike -> {
                    Timber.e("pass presenter");
                    mDataManager.setBikeList(listBike);
                    getMvpView().onBikeListUpdate(listBike);
                },
                error -> mDataManager.setError("Unable to connect to the server...")

            ));
    }

    public void onLockout(){
        Hawk.deleteAll();
    }

    public void getUsagePlan(){
        mSubscriptions.add(mDataManager.getUsagePlan()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(usagePlans ->  {
                    mDataManager.setUsagePlans(usagePlans);
                    getMvpView().onUsagePlanUpdate(usagePlans);
                },
                throwable -> mDataManager.setError("Unable to connect to the server")
            ));
    }

    public void onScanComplete(Result code){
        Timber.i("HomePresenter on receive : " + code.getText());
        Bike bike = mDataManager.getBikeFromScannerCode(code);
        if (bike == null)
            return;
        if (mDataManager.getUsingBike() == null) { // borrow case
            mDataManager.setUsingBike(bike);
            getMvpView().onScannerBikeUpdate(bike);
        } else {
            if (!mDataManager.validateBikeReturn(bike)) {
                mDataManager.setError("Please return the bike you've borrowed");
                return;
            }
            mDataManager.setUsingBike(null);
            getMvpView().onScannerReturnUpdate(bike);
        }
    }


    public void onBorrowStart(Location location){
        Bike bike = mDataManager.getUsingBike();
        Timber.e("currentbike : " + bike.toString());
        mDataManager.initializeBorrowReturnService(bike, true).subscribe(new Observer<BikeState>() {
            @Override
            public void onComplete() { getMvpView().onBorrowCompleted(bike); }

            @Override
            public void onError(Throwable e) {
                mDataManager.setError("");
                onReturnStart(bike, location);
            }

            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(BikeState s) { getMvpView().onStatusUpdate(s); }
        });
        mDataManager.performBorrow(bike,location);
    }

    public void onReturnStart(Bike bike, Location location){
        Timber.i("current bike : " + bike.toString());
        mDataManager.initializeBorrowReturnService(bike, false).subscribe(new Observer<BikeState>() {

            @Override
            public void onError(Throwable e) {
                mDataManager.setError("");
            }

            @Override
            public void onComplete() { getMvpView().onReturnCompleted(); }

            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(BikeState s) {
                getMvpView().onStatusUpdate(s);
            }
        });
        mDataManager.performReturn(bike, location);

    }

    public void updateLocation(Location location){
        Single<Object> response = mDataManager.updateLocation(location);
        if (response == null || location.equals(mDataManager.getCurrentLocation())){
            return; //hypothesis : case F | T is not possible
        }
        Disposable dResponse = response.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    object -> getMvpView().onLocationUpdate(location),
                    throwable -> mDataManager.setError(""));
    }

    public void getUserSession(){
        mSubscriptions.add(mDataManager.getUserSession().observeOn( AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
            userSession -> {
                if(userSession.isResume())
                    getMvpView().onUserSessionUpdate();
            },
            throwable -> mDataManager.setError("")));
    }







}
