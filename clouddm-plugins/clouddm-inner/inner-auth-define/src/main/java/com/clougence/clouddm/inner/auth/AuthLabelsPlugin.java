package com.clougence.clouddm.inner.auth;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;

/**
 * @author bucketli 2021/1/6 19:00
 */
@Plugin
public class AuthLabelsPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder binder) {
        binder.addGlobalSpi(new GlobalAuthInfoSpi());
        binder.bindGlobalI18n(SecSysRole.class);
        binder.bindGlobalI18n(SecAuthI18nKeys.class);
    }
}
