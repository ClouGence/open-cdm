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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.tools.ToolFactory;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public class GlobalMeta {

    private final Map<Class<?>, Map<String, Spi>> spiMap     = new ConcurrentHashMap<>();
    private final I18nUtils                       i18nMap    = I18nUtils.initI18n();
    private final Map<String, Object>             features   = new ConcurrentHashMap<>();
    private final Map<Class<?>, Service>          serviceMap = new ConcurrentHashMap<>();
    private final Map<String, ToolFactory>        toolsMap   = new ConcurrentHashMap<>();

    public GlobalMeta(){
    }

    // for SPI

    public <T extends Spi> T findSpi(Class<T> spiType, String named) {
        Objects.requireNonNull(spiType, "spiType is null.");
        Map<String, Spi> spiGroup = this.spiMap.get(spiType);
        return spiGroup == null ? null : spiType.cast(spiGroup.get(named));
    }

    public <T extends Spi> List<T> findSpi(Class<T> spiType) {
        Map<String, Spi> spiGroup = spiMap.get(spiType);
        return spiGroup == null ? Collections.emptyList() : spiGroup.values().stream().map(spi -> (T) spi).collect(Collectors.toList());
    }

    public List<String> getSpiNamesByType(Class<? extends Spi> spiType) {
        Map<String, Spi> spiGroup = this.spiMap.get(spiType);
        return spiGroup == null ? Collections.emptyList() : new ArrayList<>(spiGroup.keySet());
    }

    public void addSpi(Class<?> spiType, Spi spi) {
        addSpi(spiType, spi == null ? "" : spi.name(), spi);
    }

    public void addSpi(Class<?> spiType, String named, Spi spi) {
        if (spiType == null || spi == null) {
            return;
        }
        Map<String, Spi> spiGroup = this.spiMap.computeIfAbsent(spiType, key -> {
            return Collections.synchronizedMap(new LinkedHashMap<>());
        });

        named = StringUtils.isBlank(named) ? "" : named;
        spiGroup.put(named, spi);
    }

    // for I18n

    public I18nUtils getI18nUtils() { return this.i18nMap; }

    // for Features

    public boolean hasFeature(String featureKey) {
        return this.features.containsKey(featureKey);
    }

    public Map<String, Object> getGlobalFeatures() { return this.features; }

    // for Service

    public <T extends Service> T findService(Class<T> serviceType) {
        Objects.requireNonNull(serviceType, "serviceType is null.");
        Service service = this.serviceMap.get(serviceType);
        return service == null ? null : serviceType.cast(service);
    }

    public void putService(Service service) {
        if (service == null) {
            return;
        }
        for (Class<?> type : ClassUtils.getAllInterfaces(service.getClass())) {
            if (Service.class.isAssignableFrom(type)) {
                this.serviceMap.put(type, service);
            }
        }
    }

    public <T> void putService(Class<T> serviceType, Service service) {
        Objects.requireNonNull(service, "service is null.");
        this.serviceMap.put(serviceType, service);
    }

    // for Tools 

    public ToolFactory findTools(String toolName) {
        return this.toolsMap.get(toolName);
    }

    public void putToolService(String toolKey, ToolFactory toolsFactory) {
        this.toolsMap.put(toolKey, toolsFactory);
    }
}
