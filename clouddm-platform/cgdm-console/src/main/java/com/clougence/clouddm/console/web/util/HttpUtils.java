package com.clougence.clouddm.console.web.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author bucketli 2019-12-30 22:46
 * @since 1.1.3
 */
@Slf4j
public class HttpUtils {

    private static final OkHttpClient client = new OkHttpClient.Builder().readTimeout(6, TimeUnit.SECONDS).build();

    public static Response get(String callbackUrl) throws IOException {
        Request request = new Request.Builder().url(callbackUrl).build();
        return client.newCall(request).execute();
    }

    public static Response post(String callbackUrl, Map<String, String> formBody) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (formBody != null) {
            formBody.forEach((key, value) -> builder.addEncoded("client_id", URLEncoder.encode(value)));
        }

        Request request = new Request.Builder().url(callbackUrl) //
            .header("content-type", "application/x-www-form-urlencoded")
            .post(builder.build())
            .build();
        return client.newCall(request).execute();
    }
}
