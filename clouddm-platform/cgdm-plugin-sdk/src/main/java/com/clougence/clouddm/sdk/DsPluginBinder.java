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
