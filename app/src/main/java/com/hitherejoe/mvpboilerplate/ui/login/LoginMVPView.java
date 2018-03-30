package com.hitherejoe.mvpboilerplate.ui.login;

import com.hitherejoe.mvpboilerplate.ui.base.MvpView;

/**
 * Created by patip on 3/30/2018.
 */

public interface LoginMVPView extends MvpView {
    void login(String email, String password);
    void showSuccess(String msg);
    void showError(String error);
}
