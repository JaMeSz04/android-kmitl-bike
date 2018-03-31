package com.shubu.kmitlbike.data.remote;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.KMITLBikeApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AuthIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = Hawk.get("token", "");

        if (token.isEmpty())
            return chain.proceed(request);

        Request authRequest = request.newBuilder()
                .addHeader("Authorization", token)
                .build();

        return chain.proceed(authRequest);
    }
}
