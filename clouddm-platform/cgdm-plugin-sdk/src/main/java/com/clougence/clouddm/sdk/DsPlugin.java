package com.clougence.clouddm.sdk;

/**
 *
 * @author mode 2021/01/11
 */
public interface DsPlugin {

    void loadPlugin(DsPluginBinder dsPlugin) throws InstantiationException, IllegalAccessException;
}
