package com.shubu.kmitlbike.ui.home;

import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.ui.base.MvpView;

import java.util.List;

public interface HomeMVPView extends MvpView {
    void onBikeListUpdate(List<Bike> bikes);
    void onUsagePlanUpdate(List<UsagePlan> plans);
}


