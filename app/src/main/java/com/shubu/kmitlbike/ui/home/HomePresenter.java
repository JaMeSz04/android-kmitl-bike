package com.shubu.kmitlbike.ui.home;

import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.SingleSubscriber;
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

    public void getBikeList() {
        mSubscriptions.add(mDataManager.getBikeList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new SingleSubscriber<List<Bike>>() {
                @Override
                public void onSuccess(List<Bike> value) {
                    getMvpView().onBikeListUpdate(value);
                }

                @Override
                public void onError(Throwable error) {
                    Timber.i("error: " + error.toString());
                }
            }));
    }

}
