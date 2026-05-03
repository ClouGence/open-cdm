package com.clougence.clouddm.team.provider.github;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;

@Plugin
public class GithubPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        //dsPlugin.addService(new GithubDevopsScmProviderSpi());
    }
}
