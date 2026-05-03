package com.clougence.clouddm.team.provider.oidc.client;

import okhttp3.OkHttpClient;

public interface OidcApiCaller<T> {

    T call(OidcClient client, OkHttpClient http) throws Exception;
}
