package com.clougence.clouddm.platform.plugin.info;

import java.util.List;

import com.clougence.utils.loader.CgClassLoader;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.reflect.Annotation;

public abstract class BaseMeta {

    protected final String         pluginClass;
    protected final ResourceLoader pluginResource;
    protected final CgClassLoader  pluginClassLoader;
    protected final GlobalMeta     globalMeta;
    //
    protected final Annotation     pluginInfo;

    protected BaseMeta(String pluginClass, Annotation pluginInfo, GlobalMeta globalMeta, LoadDef loadDef){
        this.pluginClass = pluginClass;
        this.pluginResource = loadDef.pluginResource;
        this.pluginClassLoader = loadDef.pluginLoader;
        this.globalMeta = globalMeta;
        this.pluginInfo = pluginInfo;
        this.configIncludeExclude(this.pluginClassLoader);
    }

    public ClassLoader getPlusClassLoader() { return this.pluginClassLoader; }

    public String getPluginClass() { return this.pluginClass; }

    public GlobalMeta getGlobalMeta() { return this.globalMeta; }

    public int getOrder() { return this.pluginInfo.getInt("order", 1); }

    protected List<String> getIncludePackages() {
        return this.pluginInfo.getStringArray("includePackages");
    }

    public void configIncludeExclude(CgClassLoader classLoader) {
        for (String s : this.getIncludePackages()) {
            classLoader.addIncludePackages(s);
        }
        for (String s : this.pluginInfo.getStringArray("excludePackages")) {
            classLoader.addExcludePackages(s);
        }
    }
}
