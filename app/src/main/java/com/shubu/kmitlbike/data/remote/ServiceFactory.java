package com.shubu.kmitlbike.data.remote;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shubu.kmitlbike.BuildConfig;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.injection.ApplicationContext;
import com.shubu.kmitlbike.injection.component.DaggerConfigPersistentComponent;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Provide "make" methods to create instances of {@link Router}
 * and its related dependencies, such as OkHttpClient, Gson, etc.
 */
public class ServiceFactory {

    public static Router createClient() {
        OkHttpClient okHttpClient = makeOkHttpClient(makeLoggingInterceptor());
        return createService(okHttpClient, makeGson());
    }

    public static Router createService(OkHttpClient okHttpClient,
                                       Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Router.class);
    }

    public static OkHttpClient makeOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthIntercepter())
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ResponseIntercepter())
                .build();
    }

    public static Gson makeGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static HttpLoggingInterceptor makeLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return logging;
    }


}
