package com.shubu.kmitlbike.injection.module;

import android.app.Application;
import android.content.Context;

import com.shubu.kmitlbike.data.remote.MvpBoilerplateService;
import com.shubu.kmitlbike.data.remote.MvpBoilerplateServiceFactory;
import com.shubu.kmitlbike.injection.ApplicationContext;

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
    MvpBoilerplateService provideMvpBoilerplateService() {
        return MvpBoilerplateServiceFactory.makeSecretsService();
    }
}
