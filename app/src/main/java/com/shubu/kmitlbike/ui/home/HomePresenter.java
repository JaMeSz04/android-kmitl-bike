package com.shubu.kmitlbike.ui.home;

import android.location.Location;

import com.google.zxing.Result;
import com.shubu.kmitlbike.data.DataManager;
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

import retrofit2.Response;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class HomePresenter extends BasePresenter<HomeMVPView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;


    @Inject
    public HomePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }
    @Override
    public void attachView(HomeMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }
    public void onDestroy(){
        mSubscriptions.unsubscribe();
    }


    public void getBikeList() {
        mSubscriptions.add(mDataManager.getBikeList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new SingleSubscriber<List<Bike>>() {
                @Override
                public void onSuccess(List<Bike> value) {
                    Timber.e("pass presenter");
                    mDataManager.setBikeList(value);
                    getMvpView().onBikeListUpdate(value);
                }
                @Override
                public void onError(Throwable error) {
                    Timber.i("error: " + error.toString());
                }
            }));
    }

    public void getUsagePlan(){
        mSubscriptions.add(mDataManager.getUsagePlan()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new SingleSubscriber<List<UsagePlan>>() {
                @Override
                public void onSuccess(List<UsagePlan> value) {
                    mDataManager.setUsagePlans(value);
                    getMvpView().onUsagePlanUpdate(value);
                }
                @Override
                public void onError(Throwable error) {

                }
            }))
        ;
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
            .subscribe(new SingleSubscriber<BikeReturnResponse>() {
                @Override
                public void onSuccess(BikeReturnResponse value) {
                    getMvpView().onReturnCompleted();
                }

                @Override
                public void onError(Throwable error) {
                    Timber.e(error);
                }
            }));

    }

    public void updateLocation(Location location){
        Single<Object> response = mDataManager.updateLocation(location);
        if (response == null || location.equals(mDataManager.getCurrentLocation())){
            return; //hypothesis : case F | T is not possible
        }
        response.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<Object>() {

            @Override
            public void onSuccess(Object value) {
                getMvpView().onLocationUpdate(location);
            }

            @Override
            public void onError(Throwable error) {
                Timber.e(error);
            }
        });
    }







}
