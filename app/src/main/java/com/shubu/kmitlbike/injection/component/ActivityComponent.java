package com.shubu.kmitlbike.injection.component;

import com.shubu.kmitlbike.injection.PerActivity;
import com.shubu.kmitlbike.injection.module.ActivityModule;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.detail.TermsAndConditionsActivity;
import com.shubu.kmitlbike.ui.home.HomeActivity;
import com.shubu.kmitlbike.ui.login.LoginActivity;
import com.shubu.kmitlbike.ui.profile.ProfileActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);
    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
    void inject(ProfileActivity profileActivity);
    void inject(TermsAndConditionsActivity termsAndConditionsActivity);
}
