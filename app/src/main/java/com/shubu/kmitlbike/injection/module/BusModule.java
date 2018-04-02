package com.shubu.kmitlbike.injection.module;


import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.google.zxing.Result;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;

@Module
public class BusModule {
    public static final String BIKE_PROVIDER = "BIKE_PROVIDER";
    public static final String PLAN_PROVIDER = "PLAN_PROVIDER";
    public static final String SCANNER_PROVIDER = "SCANNER_PROVIDER";

    @Provides
    @Singleton
    @Named(BIKE_PROVIDER)
    static PublishSubject<List<Bike>> provideBike() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named(PLAN_PROVIDER)
    static PublishSubject<List<UsagePlan>> providePlan() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named(SCANNER_PROVIDER)
    static PublishSubject<Result> provideScannerCode() { return PublishSubject.create(); }

}
