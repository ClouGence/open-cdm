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

import java.util.List;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.tools.ToolFactory;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.drivers.DriverLoader;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;

public class FooMetaBinder implements DsPluginBinder {

    private final GlobalMeta globalMeta;
    private final FooMeta    dsMeta;

    public FooMetaBinder(GlobalMeta globalMeta, FooMeta dsMeta){
        this.globalMeta = globalMeta;
        this.dsMeta = dsMeta;
    }

    // for any plugin

    @Override
    public ClassLoader getPluginClassLoader() { return this.dsMeta.getPlusClassLoader(); }

    @Override
    public DriverLoader getDriverLoader() { return PluginManager.driverLoader(); }

    @Override
    public void addPluginFeature(String... featureIds) {
        for (String featureId : featureIds) {
            this.dsMeta.getPlusFeatures().put(featureId, true);
        }
    }

    @Override
    public void bindPluginI18n(Class<?>... clazz) {
        this.dsMeta.getPlusI18nUtil().loadResources(clazz);
    }

    @Override
    public void bindGlobalI18n(Class<?>... clazz) {
        this.globalMeta.getI18nUtils().loadResources(clazz);
    }

    // SPI

    @Override
    public void addPluginSpi(Spi spi) {
        if (spi == null) {
            return;
        }

        for (Class<?> type : ClassUtils.getAllInterfaces(spi.getClass())) {
            if (Spi.class.isAssignableFrom(type)) {
                String named = StringUtils.isBlank(spi.name()) ? type.getName() : spi.name();
                this.dsMeta.addSpi(type, named, spi);
            }
        }
    }

    @Override
    public void addPluginSpi(Class<? extends Spi> spiType, String named, Spi spi) {
        if (Spi.class.isAssignableFrom(spiType)) {
            this.dsMeta.addSpi(spiType, named, spi);
        }
    }

    @Override
    public void addGlobalSpi(Spi spi) {
        if (spi == null) {
            return;
        }

        for (Class<?> type : ClassUtils.getAllInterfaces(spi.getClass())) {
            if (Spi.class.isAssignableFrom(type)) {
                this.globalMeta.addSpi(type, spi);
            }
        }
    }

    @Override
    public void addGlobalSpi(Class<? extends Spi> spiType, String named, Spi spi) {
        if (Spi.class.isAssignableFrom(spiType)) {
            this.globalMeta.addSpi(spiType, named, spi);
        }
    }

    @Override
    public <T extends Spi> List<T> findGlobalSpi(Class<T> spiType) {
        return this.globalMeta.findSpi(spiType);
    }

    @Override
    public <T extends Spi> T findGlobalSpi(Class<T> spiType, String name) {
        return this.globalMeta.findSpi(spiType, name);
    }

    // for service

    @Override
    public <T extends Service> T findGlobalService(Class<T> serviceType) {
        return this.globalMeta.findService(serviceType);
    }

    @Override
    public void addGlobalService(Service service) {
        this.globalMeta.putService(service);
    }

    @Override
    public void bindGlobalToolService(String toolKey, ToolFactory toolsFactory) {
        this.dsMeta.getGlobalMeta().putToolService(toolKey, toolsFactory);
    }
}
