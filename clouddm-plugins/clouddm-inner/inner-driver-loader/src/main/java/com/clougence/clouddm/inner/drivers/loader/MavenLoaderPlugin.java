package com.clougence.clouddm.inner.drivers.loader;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Plugin()
public class MavenLoaderPlugin implements DsPlugin, DsFeatureIDs {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.getDriverLoader().registerPreparer("maven", MavenResourcePreparer::new);
    }
}
