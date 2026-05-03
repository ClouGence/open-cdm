package com.clougence.clouddm.platform.plugin.info;

import com.clougence.utils.loader.CgClassLoader;
import com.clougence.utils.loader.ResourceLoader;

public class LoadDef {

    public final ResourceLoader pluginResource;
    public final ClassLoader    appClassLoader;
    public final CgClassLoader  pluginLoader;

    public LoadDef(ClassLoader appLoader, ResourceLoader pluginResource){
        this.pluginResource = pluginResource;
        this.appClassLoader = appLoader;
        this.pluginLoader = this.pluginResource.toClassLoader(appLoader);
    }
}
