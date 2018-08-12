package com.shubu.kmitlbike.ui.splash;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.home.HomeActivity;
import com.shubu.kmitlbike.ui.login.LoginActivity;
import com.shubu.kmitlbike.ui.splash.fragments.UpdateNotifierFragment;
import com.shubu.kmitlbike.ui.tutorial.TutorialActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SplashActivity extends BaseActivity implements SplashMVPView {


    @Inject SplashPresenter presenter;

    @BindView(R.id.SplashLayout)
    FrameLayout splashLayout;

    @BindView(R.id.SplashLoading)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        presenter.attachView(this);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            Timber.e("validating version");
            presenter.validateVersion(version);
        } catch (Exception e) {
            Timber.e(e);
        }


    }

    @Override
    public void redirect(String where) {
        Timber.e("redirecting");
        Intent intent;
        switch (where){
            case "main" :
                intent = new Intent(this,HomeActivity.class);
                startActivity(intent);
                break;

            case "login" :
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case "tutorial" :
                intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onRequiredupdate(String url) {

        progressBar.setVisibility(View.INVISIBLE);
        Fragment updateFragment = UpdateNotifierFragment.newInstance(url);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(splashLayout.getId(), updateFragment).commit();

    }
}
