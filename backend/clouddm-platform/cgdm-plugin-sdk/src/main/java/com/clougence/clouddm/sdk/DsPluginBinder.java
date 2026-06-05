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
package com.clougence.clouddm.sdk;

import java.util.List;

import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.clouddm.sdk.execute.tools.ToolFactory;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.drivers.DriverLoader;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.provider.SqlBuilder;

public interface DsPluginBinder {

    // for any plugin

    ClassLoader getPluginClassLoader();

    DriverLoader getDriverLoader();

    void addPluginFeature(String... featureIds);

    void bindPluginI18n(Class<?>... clazz);

    void bindGlobalI18n(Class<?>... clazz);

    // SPI

    void addPluginSpi(Spi spi);

    void addPluginSpi(Class<? extends Spi> spiType, String named, Spi spi);

    void addGlobalSpi(Spi spi);

    void addGlobalSpi(Class<? extends Spi> spiType, String named, Spi spi);

    <T extends Spi> List<T> findGlobalSpi(Class<T> spiType);

    <T extends Spi> T findGlobalSpi(Class<T> spiType, String name);

    // for ds

    default void bindDsSessionFactory(Class<? extends SessionFactory<?>> factoryClass) {
        throw new UnsupportedOperationException();
    }

    default void bindDsSqlBuilder(SqlBuilder builder) {
        throw new UnsupportedOperationException();
    }

    default void bindDsDialect(Dialect dialect) {
        throw new UnsupportedOperationException();
    }

    default void bindDsDriverFamily(String... driverFamily) {
        throw new UnsupportedOperationException();
    }

    // for service

    <T extends Service> T findGlobalService(Class<T> serviceType);

    void addGlobalService(Service service);

    void bindGlobalToolService(String toolKey, ToolFactory toolsFactory);
}
