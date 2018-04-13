package com.shubu.kmitlbike.ui.detail;

import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class TermsAndConditionsPresenter extends BasePresenter<TermsAndConditionsMVPView>{
    private final DataManager mDataManager;
    private CompositeDisposable mSubscriptions;

    @Inject
    public TermsAndConditionsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(TermsAndConditionsMVPView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.dispose();
        mSubscriptions = null;
    }

    public void getTermsAndCondition(){

    }

}
