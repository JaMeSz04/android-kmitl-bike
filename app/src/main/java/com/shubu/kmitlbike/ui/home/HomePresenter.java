package com.shubu.kmitlbike.ui.home;

import com.google.zxing.Result;
import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    public void getBikeList() {
        mSubscriptions.add(mDataManager.getBikeList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new SingleSubscriber<List<Bike>>() {
                @Override
                public void onSuccess(List<Bike> value) {
                    Timber.e("pass presenter");
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
    }



}
