package com.shubu.kmitlbike.injection.component;

import android.app.Application;
import android.content.Context;

import com.shubu.kmitlbike.data.DataManager;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.injection.ApplicationContext;
import com.shubu.kmitlbike.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    DataManager dataManager();
    Router mvpBoilerplateService();
}
