package com.clougence.clouddm.team.provider.gitee;

import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.team.provider.gitee.constants.GiteeI18nKeys;
import com.clougence.clouddm.team.provider.gitee.devops.GiteeDevopsScmProviderSpi;

import okhttp3.OkHttpClient;

@Plugin
public class GiteePlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // initI18n
        dsPlugin.bindGlobalI18n(GiteeI18nKeys.class);

        // spi
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(6, TimeUnit.SECONDS).build();
        dsPlugin.addGlobalSpi(new GiteeDevopsScmProviderSpi(client));
    }
}
