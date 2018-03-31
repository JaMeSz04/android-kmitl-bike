package com.shubu.kmitlbike.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.injection.ApplicationContext;
import com.shubu.kmitlbike.injection.ConfigPersistent;
import com.shubu.kmitlbike.ui.base.BasePresenter;

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
                        // TODO: 3/31/18 : move sharepref token to datamanager
                        Hawk.put("token", value.getToken());
                        getMvpView().showSuccess(value.toString());
                        getMvpView().redirect("main");
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.toString());
                    }
                }));
    }

    public boolean validateToken(){
        return !Hawk.get("token","").isEmpty();

    }

}
