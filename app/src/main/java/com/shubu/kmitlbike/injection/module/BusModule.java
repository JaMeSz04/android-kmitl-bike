package com.shubu.kmitlbike.injection.module;


import android.location.Location;

import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.google.zxing.Result;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.common.MapEvent;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;
import rx.Single;
import rx.subjects.PublishSubject;

@Module
public class BusModule {
    public static final String BIKE_PROVIDER = "BIKE_PROVIDER";
    public static final String PLAN_PROVIDER = "PLAN_PROVIDER";
    public static final String SCANNER_PROVIDER = "SCANNER_PROVIDER";
    public static final String BIKE_STATE_PROVIDER = "BIKE_STATE_PROVIDER";
    public static final String BIKE_PASSWORD_PROVIDER = "BIKE_PASSWORD_PROVIDER";
    public static final String LOCATION_PROVIDER = "LOCATION_PROVIDER";
    public static final String MAP_EVENT_PROVIDER = "MAP_EVENT_PROVIDER";

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

    @Provides
    @Singleton
    @Named(BIKE_STATE_PROVIDER)
    static PublishSubject<BikeState> provideBikeState() { return PublishSubject.create(); }

    @Provides
    @Singleton
    @Named(BIKE_PASSWORD_PROVIDER)
    static PublishSubject<String> provideBikePassword() { return PublishSubject.create(); }

    @Provides
    @Singleton
    @Named(LOCATION_PROVIDER)
    static PublishSubject<Location> provideLocation() { return PublishSubject.create(); }

    @Provides
    @Singleton
    @Named(MAP_EVENT_PROVIDER)
    static PublishSubject<Location> provideMapEvent() { return PublishSubject.create(); }
}
