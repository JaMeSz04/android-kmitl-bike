package com.google.android.apps.secrets.test.common.injection.module;

import android.app.Application;
import android.content.Context;

import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {
    private final Application mApplication;

    public ApplicationTestModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /************* MOCKS *************/

    @Provides
    @Singleton
    DataManager providesDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    Router provideMvpBoilerplateService() {
        return mock(Router.class);
    }

}
