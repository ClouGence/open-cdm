package com.clougence.clouddm.onlineddl;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Slf4j
@Plugin()
public class GhostPlugin implements DsPlugin, DsFeatureIDs {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        this.configExecute(dsPlugin);
        this.configFeatures(dsPlugin);
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        String ghostClazzName = GhostPlugin.class.getName();
        //dsPlugin.bindToolService("GHOST", (classLoader, driver) -> ClassUtils.newInstance(classLoader, ghostClazzName));
        dsPlugin.addSpi(new MySessionSpi());
    }

    private void configFeatures(DsPluginBinder dsPlugin) {
        dsPlugin.addFeature(FUN_SUPPORT_RDB_TX, SUPPORT_FAKER);
    }
}
