package com.shubu.kmitlbike.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.injection.ConfigPersistent;
import com.shubu.kmitlbike.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by patip on 3/30/2018.
 */


@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMVPView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscriptions;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.dispose();
        mSubscriptions = null;
    }

    public void login(String username, String password){
        checkViewAttached();
        mSubscriptions.add(mDataManager.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(
                        loginResponse -> {
                            Hawk.put("token", loginResponse.getToken());
                            getMvpView().showSuccess(loginResponse.toString());
                            getMvpView().redirect("main");
                        },

                        throwable -> getMvpView().showError(throwable.toString())
                ));
    }

    public boolean validateToken(){
        return !Hawk.get("token","").isEmpty();

    }

}
