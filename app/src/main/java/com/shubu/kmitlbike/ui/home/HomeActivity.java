package com.shubu.kmitlbike.ui.home;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.ui.home.fragment.BikeInfoFragment;
import com.shubu.kmitlbike.ui.home.fragment.BorrowListener;
import com.shubu.kmitlbike.ui.home.fragment.BottomSheetFragment;
import com.shubu.kmitlbike.ui.home.fragment.ScannerFragment;
import com.shubu.kmitlbike.ui.home.fragment.ScannerListener;
import com.shubu.kmitlbike.ui.home.fragment.StatusFragment;
import com.shubu.kmitlbike.util.HomeBottomSheetBehavior;
import com.shubu.kmitlbike.ui.home.fragment.HomeFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements
        HomeMVPView,
        ScannerListener,
        LocationListener,
        BorrowListener {

    @Inject HomePresenter presenter;

    @BindView(R.id.home_frame) FrameLayout layout;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.home_scanner) FrameLayout scanner;
    @BindView(R.id.sheet_content_layout) FrameLayout bottomSheetLayout;
    @BindView(R.id.HomeRideButton) FloatingActionButton fab;

    private Fragment scannerFragment;
    private BikeInfoFragment bikeInfoFragment;
    private StatusFragment bikeStatusFragment;
    private LocationManager locationManager;
    protected BottomSheetBehavior sheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        presenter.attachView(this);
        presenter.getBikeList();
        presenter.getUsagePlan();
        sheetBehavior = HomeBottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setBottomSheetCallback(
            new BottomSheetBehavior.BottomSheetCallback() {
                 @Override
                 public void onStateChanged(@NonNull View bottomSheet, int newState) { }

                 @Override
                 public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                     fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                 }
             }
        );

        //subscribe scanner eventbus
        eventBus.getScannerCode().subscribe( code -> {
            presenter.onScanComplete(code);
        });

        eventBus.getBikeState().subscribe(new Subscriber<BikeState>() {
            @Override
            public void onCompleted() { // TODO: 4/3/2018 hide bottomsheet
                Timber.e("borrow confirmed");
                //start tracking
            }
            @Override public void onError(Throwable e) {}
            @Override public void onNext(BikeState bikeState) {}
        });

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (savedInstanceState == null)
            this.constructFragment();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void constructFragment(){
        Fragment homeFragment = new HomeFragment();
        Fragment bottomSheetFragment = new BottomSheetFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(layout.getId(), homeFragment);
        ft.add(bottomSheetLayout.getId(), bottomSheetFragment).commit();
    }

    @OnClick(R.id.HomeRideButton)
    public void toggleBottomSheet(){
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onScannerStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 2);


        scannerFragment = new ScannerFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(scanner.getId(), scannerFragment).addToBackStack("scanner").commit();
        // TODO: 4/1/2018 raise error, you don't have access to camera!!
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
    public void onScannerBikeUpdate(Bike bike) {
        bikeInfoFragment = BikeInfoFragment.newInstance(bike.getBikeName(), bike.getBikeModel());
        FragmentManager manager =  getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.hide(scannerFragment);
        manager.popBackStack();
        ft.replace(bottomSheetLayout.getId(), bikeInfoFragment).addToBackStack("bike-ready").commit();

    }

    //BIKE BORROW/RETURN EVENTBUS

    @Override
    public void onBorrow() {
        try {
            FragmentManager manager =  getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.hide(bikeInfoFragment);
            bikeStatusFragment = StatusFragment.newInstance(null);
            ft.replace(bottomSheetLayout.getId(), bikeStatusFragment).commit();
            Location lastestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            presenter.onBorrowStart(lastestLocation);


        } catch (SecurityException e){
            Timber.e(e.toString());
        }
    }

    @Override
    public void onBorrowStatusUpdate(BikeState status) {
        this.eventBus.getBikeState().onNext(status);
    }

    @Override
    public void onBorrowCompleted(Bike bike) {
        Timber.e("on borrow completed : " + bike);
        switch (bike.getBikeModel()){
            case CONSTANTS.GIANT_ESCAPE:
                eventBus.getBikeState().onCompleted();
                //collapse bottomsheet
                break;
            case CONSTANTS.LA_GREEN:
                Timber.e("borrow completed");
                eventBus.getBikePassword().onNext(bike.getMacAddress());
        }
    }

    //LOCATION SERVICE

    @Override
    public void onLocationUpdate(Location location) {
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
