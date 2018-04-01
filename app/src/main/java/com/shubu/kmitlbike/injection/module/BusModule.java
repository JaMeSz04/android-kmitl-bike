package com.shubu.kmitlbike.injection.module;


import com.shubu.kmitlbike.data.model.Bike;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;

@Module
public class BusModule {
    public static final String HOME_PROVIDER = "HOME_PROVIDER";

    @Provides
    @Singleton
    @Named(HOME_PROVIDER)
    static PublishSubject<List<Bike>> provideBike() {
        return PublishSubject.create();
    }
}
