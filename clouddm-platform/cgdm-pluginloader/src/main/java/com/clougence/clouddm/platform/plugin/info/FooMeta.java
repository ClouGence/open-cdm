/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.plugin.info;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.platform.plugin.PluginInfo;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.reflect.Annotation;

public class FooMeta extends BaseMeta implements PluginInfo {

    private final Map<Class<?>, Map<String, Spi>> plusSpiMap;
    private final I18nUtils                       plusI18nUtil;
    private final Map<String, Object>             plusFeatures;

    public FooMeta(String pluginClass, Annotation pluginInfo, GlobalMeta globalMeta, LoadDef loadDef){
        super(pluginClass, pluginInfo, globalMeta, loadDef);
        this.plusSpiMap = new ConcurrentHashMap<>();
        this.plusI18nUtil = I18nUtils.initI18n(globalMeta.getI18nUtils());
        this.plusFeatures = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isDsPlugin() { return false; }

    @Override
    public Map<String, Object> getPlusFeatures() { return this.plusFeatures; }

    @Override
    public I18nUtils getPlusI18nUtil() { return this.plusI18nUtil; }

    //

    @Override
    public <T extends Spi> List<T> findSpi(Class<T> spiType) {
        Map<String, Spi> spiMap = this.plusSpiMap.get(spiType);
        if (CollectionUtils.isEmpty(spiMap)) {
            return Collections.emptyList();
        } else {
            return spiMap.values().stream().map(spi -> (T) spi).collect(Collectors.toList());
        }
    }

    @Override
    public <T extends Spi> T findSpi(Class<T> spiType, String named) {
        Map<String, Spi> spiMap = this.plusSpiMap.get(spiType);
        if (CollectionUtils.isEmpty(spiMap)) {
            return null;
        } else {
            return (T) spiMap.get(named);
        }
    }

    public void addSpi(Class<?> spiType, String named, Spi spi) {
        if (spiType != null && spi != null) {
            this.plusSpiMap.computeIfAbsent(spiType, key -> {
                return new ConcurrentHashMap<>();
            }).put(named, spi);
        }
    }
}
