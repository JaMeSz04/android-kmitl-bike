package com.shubu.kmitlbike.data.remote;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import timber.log.Timber;

public class ResponseIntercepter implements Interceptor {

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);

        // todo deal with the issues the way you need to
        if (response.code() == 500) {
            Timber.e(response.toString());
            Timber.e(response.message());
            return response;
        }

        return response;
    }

}
