package com.hitherejoe.mvpboilerplate.ui.login;

import com.hitherejoe.mvpboilerplate.data.DataManager;
import com.hitherejoe.mvpboilerplate.data.model.LoginResponse;
import com.hitherejoe.mvpboilerplate.injection.ConfigPersistent;
import com.hitherejoe.mvpboilerplate.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by patip on 3/30/2018.
 */


@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMVPView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }

    public void login(String username, String password){
        checkViewAttached();
        mSubscriptions.add(mDataManager.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<LoginResponse>(){

                    @Override
                    public void onSuccess(LoginResponse value) {
                        getMvpView().showSuccess(value.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.toString());
                    }
                }));
    }

}
