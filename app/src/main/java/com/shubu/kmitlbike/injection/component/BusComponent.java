package com.shubu.kmitlbike.injection.component;


import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.injection.module.BusModule;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import rx.subjects.PublishSubject;
import com.google.zxing.Result;

@Component(modules = BusModule.class)
@Singleton
public interface BusComponent {

    @Named(BusModule.BIKE_PROVIDER)
    PublishSubject<List<Bike>> getBike();

    @Named(BusModule.PLAN_PROVIDER)
    PublishSubject<List<UsagePlan>> getPlan();


    @Named(BusModule.SCANNER_PROVIDER)
    PublishSubject<Result> getScannerCode();
}
