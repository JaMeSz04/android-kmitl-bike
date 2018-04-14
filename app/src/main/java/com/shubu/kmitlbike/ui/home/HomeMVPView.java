package com.shubu.kmitlbike.ui.home;

import android.location.Location;

import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.Session;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.base.MvpView;

import java.util.List;

public interface HomeMVPView extends MvpView {
    void onBikeListUpdate(List<Bike> bikes);
    void onUserSessionUpdate();
    void onUsagePlanUpdate(List<UsagePlan> plans);
    void onScannerBikeUpdate(Bike bike);
    void onLocationUpdate(Location location);
    void onBorrowStatusUpdate(BikeState status);
    void onBorrowCompleted(Bike bike);
    void onReturnCompleted();
}


