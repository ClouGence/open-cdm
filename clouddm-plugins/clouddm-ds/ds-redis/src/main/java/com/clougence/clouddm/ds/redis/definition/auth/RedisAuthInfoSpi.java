package com.clougence.clouddm.ds.redis.definition.auth;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.security.auth.AuthBinder;
import com.clougence.clouddm.sdk.security.auth.AuthHelper;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoSpi;

/**
 * @author mode 2021/1/6 19:00
 */
public class RedisAuthInfoSpi implements AuthInfoSpi {

    @Override
    public String name() {
        return "redis";
    }

    @Override
    public void registryAuthLabel(AuthBinder labelBinder) {
        for (AuthInfo authInfo : AuthHelper.lookUp(RedisDataAuthLabel.class, DataSourceType.Redis)) {
            labelBinder.addAuthInfo(authInfo);
        }
    }
}
