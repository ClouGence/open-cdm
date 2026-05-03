package com.clougence.rdp.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HealthCheckInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request reequest = chain.request();
        String questUrl = reequest.url().toString();
        boolean isUploadVideoApi = questUrl.endsWith("/healthcheck");
        if (isUploadVideoApi) {
            return chain.withConnectTimeout(1000, TimeUnit.MILLISECONDS)
                .withReadTimeout(1000, TimeUnit.MILLISECONDS)
                .withWriteTimeout(1000, TimeUnit.MILLISECONDS)
                .proceed(reequest);
        }
        return chain.proceed(reequest);
    }
}
