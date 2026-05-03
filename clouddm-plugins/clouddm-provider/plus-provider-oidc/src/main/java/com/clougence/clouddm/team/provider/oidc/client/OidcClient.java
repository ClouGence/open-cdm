package com.clougence.clouddm.team.provider.oidc.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.clougence.clouddm.team.provider.oidc.constants.OidcI18nKey;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import okhttp3.OkHttpClient;

public class OidcClient implements Closeable {

    private final Logger       log;
    private final OkHttpClient client;
    private final OidcApi      bindApi;

    public OidcClient(Logger log, String primaryUid, OidcCfg conf) throws Exception{
        this.log = log;
        this.client = new OkHttpClient.Builder().readTimeout(6, TimeUnit.SECONDS).build();
        this.bindApi = new OidcApi(this, primaryUid, conf);
        this.bindApi.initWellKnown();
    }

    public OidcApi getBindApi() { return this.bindApi; }

    public <T> T callApi(OidcApiCaller<T> caller) {
        return callApi(caller, 1);
    }

    private <T> T callApi(OidcApiCaller<T> caller, int count) {
        try {
            return caller.call(this, this.client);
        } catch (Exception e) {
            if (count < 3) {
                return callApi(caller, count + 1);
            }
            handleCallApiException(e);
            throw ThirdPartyApiException.asRDP().with(e, OidcI18nKey.OIDC_UNKNOWN_CALL_API_ERROR, e.getMessage());
        }
    }

    private void handleCallApiException(Exception e) {
        throw ThirdPartyApiException.asRDP().with(e, OidcI18nKey.OIDC_UNKNOWN_CALL_API_ERROR, e.getMessage());
    }

    @Override
    public void close() throws IOException {
        OidcClientKiller.close(this.log, this.client);
    }
}
