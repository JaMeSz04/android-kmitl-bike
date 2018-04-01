package com.shubu.kmitlbike.ui.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapFragment;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.injection.component.BusComponent;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.home.fragment.HomeFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeMVPView, HomeFragment.OnFragmentInteractionListener {

    @Inject HomePresenter presenter;



    @BindView(R.id.home_frame) FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getBikeList();
        if (savedInstanceState == null)
            this.constructFragment();

    }

    private void constructFragment(){
        Fragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(layout.getId(), homeFragment).commit();
    }

    @Override
    public void onBikeListUpdate(List<Bike> bikes) {
        this.eventBus.getBike().onNext(bikes); //publish bikes to subscribers
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
