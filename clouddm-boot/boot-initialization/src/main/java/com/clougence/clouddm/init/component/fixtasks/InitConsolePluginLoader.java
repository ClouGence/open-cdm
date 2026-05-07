package com.clougence.clouddm.init.component.fixtasks;

import java.io.File;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;

@Service
public class InitConsolePluginLoader {

    public void loadPlugin(ClassLoader parentClassLoader) throws Exception {
        File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
        File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
        PluginLoadHelper.loadPlugins(parentClassLoader, pluginPath1, pluginPath2);
    }
}
