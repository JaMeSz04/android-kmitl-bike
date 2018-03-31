package com.shubu.kmitlbike.ui.home;

import android.os.Bundle;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeMVPView {

    @Inject HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getBikeList();
    }

    @Override
    public void onBikeListUpdate(List<Bike> bikes) {
        Timber.i("bike list: " + bikes.toString());
    }
}
