package com.shubu.kmitlbike.ui.home;

import android.location.Location;

import com.google.zxing.Result;
import com.orhanobut.hawk.Hawk;
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

import java.util.List;

import javax.inject.Inject;

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
                error -> Timber.i("error: " + error.toString())

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
                throwable -> {}
            ));
    }

    public void onScanComplete(Result code){
        Timber.i("HomePresenter on receive : " + code.getText());
        Bike bike = mDataManager.getBikeFromScannerCode(code);
        mDataManager.setUsingBike(bike);
        getMvpView().onScannerBikeUpdate(bike);
    }

    public void onBorrowStart(Location location){
        Bike bike = mDataManager.getUsingBike();
        Timber.e("currentbike : " + bike.toString());
        mDataManager.initializeBorrowService(bike).subscribe(new Subscriber<BikeState>() {
            @Override
            public void onCompleted() {
                getMvpView().onBorrowCompleted(bike);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("borrow error!!! returning...");
                onReturnStart(location);
            }

            @Override
            public void onNext(BikeState s) {
                getMvpView().onBorrowStatusUpdate(s);
            }
        });
        mDataManager.performBorrow(bike,location);
    }

    public void onReturnStart(Location location){
        Bike bike = mDataManager.getUsingBike();
        Timber.i("current bike : " + bike.toString());
        mSubscriptions.add(mDataManager.performReturn(bike, location)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                bikeReturnResponse -> getMvpView().onReturnCompleted(),
                throwable -> Timber.e(throwable)
            ));
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
                    throwable -> Timber.e(throwable));
    }







}
