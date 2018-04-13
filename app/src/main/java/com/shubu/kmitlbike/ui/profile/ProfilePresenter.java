package com.shubu.kmitlbike.ui.profile;

import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.injection.ConfigPersistent;
import com.shubu.kmitlbike.ui.base.BasePresenter;
import com.shubu.kmitlbike.ui.login.LoginMVPView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class ProfilePresenter extends BasePresenter<ProfileMVPView> {
    private final DataManager mDataManager;
    private CompositeDisposable mSubscriptions;

    @Inject
    public ProfilePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ProfileMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.dispose();
        mSubscriptions = null;
    }

    public void loadUserHistories(){
        mSubscriptions.add( mDataManager.getHistoryList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
            profileHistories -> getMvpView().onHistoryListLoad(profileHistories),
            throwable -> Timber.e(throwable)
        ));
    }

    public void loadHistory(ProfileHistory selectedHistory){
        mSubscriptions.add( mDataManager.getFullHistory(selectedHistory).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
            history -> getMvpView().onHistoryLoad(history),
            throwable -> Timber.e(throwable)
        ));
    }
}
