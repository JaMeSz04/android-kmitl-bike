package com.shubu.kmitlbike.ui.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.MapFragment;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.injection.component.BusComponent;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.home.fragment.BottomSheetFragment;
import com.shubu.kmitlbike.ui.home.fragment.HomeBottomSheetBehavior;
import com.shubu.kmitlbike.ui.home.fragment.HomeFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeMVPView, HomeFragment.OnFragmentInteractionListener, BottomSheetFragment.OnFragmentInteractionListener {

    @Inject HomePresenter presenter;

    @BindView(R.id.home_frame) FrameLayout layout;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.sheet_content_layout) FrameLayout bottomSheetLayout;

    protected BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getBikeList();
        presenter.getUsagePlan();

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        // TODO: 4/1/2018 currently use for dev purpose -> change it to HomeBottomSheetBehavior later


        if (savedInstanceState == null)
            this.constructFragment();


    }

    private void constructFragment(){
        Fragment homeFragment = new HomeFragment();
        Fragment bottomSheetFragment = new BottomSheetFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(layout.getId(), homeFragment);
        ft.add(bottomSheetLayout.getId(), bottomSheetFragment).commit();
    }

    @OnClick(R.id.ride_button)
    public void toggleBottomSheet(){
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onBikeListUpdate(List<Bike> bikes) {
        this.eventBus.getBike().onNext(bikes); //publish bikes to subscribers
    }

    @Override
    public void onUsagePlanUpdate(List<UsagePlan> plans) {
        Timber.i("new plan" + plans.toString());
        this.eventBus.getPlan().onNext(plans);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
