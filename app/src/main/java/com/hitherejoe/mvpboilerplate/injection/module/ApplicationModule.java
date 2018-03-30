package com.hitherejoe.mvpboilerplate.injection.module;

import android.app.Application;
import android.content.Context;

import com.hitherejoe.mvpboilerplate.data.remote.Router;
import com.hitherejoe.mvpboilerplate.data.remote.ServiceFactory;
import com.hitherejoe.mvpboilerplate.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Router provideMvpBoilerplateService() {
        return ServiceFactory.createClient();
    }
}
