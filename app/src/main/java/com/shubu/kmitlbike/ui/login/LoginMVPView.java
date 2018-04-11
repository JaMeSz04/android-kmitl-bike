package com.shubu.kmitlbike.ui.login;

import android.content.Context;

import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.ui.base.MvpView;

/**
 * Created by patip on 3/30/2018.
 */

public interface LoginMVPView extends MvpView {
    void login(String email, String password);
    void showSuccess(String msg);
    void showError(String error);
    void validateToken();
    void redirect(String where);
}
