package com.shubu.kmitlbike;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.injection.component.ApplicationComponent;
import com.shubu.kmitlbike.injection.component.DaggerApplicationComponent;
import com.shubu.kmitlbike.injection.module.ApplicationModule;

import timber.log.Timber;

public class KMITLBikeApplication extends Application  {

    ApplicationComponent mApplicationComponent;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this.getApplicationContext()).build();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static KMITLBikeApplication get(Context context) {
        return (KMITLBikeApplication) context.getApplicationContext();
    }


    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
