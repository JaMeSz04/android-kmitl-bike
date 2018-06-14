package com.shubu.kmitlbike.ui.splash;

import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.ui.base.BasePresenter;
import com.shubu.kmitlbike.ui.login.LoginMVPView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashPresenter extends BasePresenter<SplashMVPView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscriptions;

    @Inject
    public SplashPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SplashMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.dispose();
        mSubscriptions = null;
    }

    public void validateVersion(String version){
        mSubscriptions.add(mDataManager.validateVersion(version).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe( versionResponse -> {
                if (versionResponse.isRequiredUpdate())
                    getMvpView().onRequiredupdate("\"https://play.google.com/store/apps/details?id=com.bike.kmitl.kmitlbike\"");
                else
                    this.validateToken();
            }, throwable -> {
                Timber.e("can not connect to the server : " + throwable.getMessage());
            }));
    }

    private void validateToken(){
        if (Hawk.get("token","").isEmpty()) {
            getMvpView().redirect("login");
            return;
        }
        Disposable validateTask = mDataManager.validateToken(Hawk.get("token", "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe( loginResponse -> {
                mDataManager.setCurrentUser(loginResponse);
                getMvpView().redirect("main");
            }, throwable -> {
                getMvpView().redirect("login");
            });
    }
}
