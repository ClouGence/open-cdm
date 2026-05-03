package com.clougence.rdp.component.request;

import java.util.function.BiConsumer;

import com.clougence.rdp.constant.auth.RequestAuth;

/**
 * @author bucketli 2020-01-31 12:43
 * @since 1.1.3
 */
public interface RdpRequestAuthService {

    void foreach(BiConsumer<String, RequestAuth> biConsumer);

    String getI18N(String resourceValue);

    RequestAuth loadAuthByApi(String resourceValue);
}
