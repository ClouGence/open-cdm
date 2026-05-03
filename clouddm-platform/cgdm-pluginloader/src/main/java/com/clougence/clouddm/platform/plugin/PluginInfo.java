package com.clougence.clouddm.platform.plugin;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.utils.i18n.I18nUtils;

public interface PluginInfo {

    boolean isDsPlugin();

    ClassLoader getPlusClassLoader();

    Map<String, Object> getPlusFeatures();

    I18nUtils getPlusI18nUtil();

    //

    <T extends Spi> List<T> findSpi(Class<T> spiType);

    <T extends Spi> T findSpi(Class<T> spiType, String named);
}
