package com.shubu.kmitlbike.ui.splash;

import com.shubu.kmitlbike.ui.base.MvpView;

public interface SplashMVPView extends MvpView {

    void redirect(String where);
    void onRequiredupdate(String url);
}
