package com.clougence.clouddm.platform.plugin.info;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.drivers.DriverBinding;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.DsFactory;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.reflect.Annotation;

public class DsMeta extends BaseMeta implements DsPluginInfo {

    private final Map<Class<?>, Map<String, Spi>> dsSpiMap;
    private final I18nUtils                       dsI18nUtil;
    private final Map<String, Object>             dsFeatures;
    private final DataSourceType                  dsType;
    private String                                dsSessionFactory;
    private SqlBuilder                            dsSqlBuilder;
    private Dialect                               dsDialect;
    private List<String>                          dsDriverFamily;
    //
    private final Map<String, DriverBinding>      driverBindingCache  = new ConcurrentHashMap<>();
    private final Map<String, DsFactory<?>>       driverFactoryCache  = new ConcurrentHashMap<>();
    private final Map<String, SessionFactory<?>>  sessionFactoryCache = new ConcurrentHashMap<>();

    public DsMeta(String pluginClass, Annotation pluginInfo, GlobalMeta globalMeta, LoadDef loadDef) throws IOException{
        super(pluginClass, pluginInfo, globalMeta, loadDef);
        this.dsSpiMap = new ConcurrentHashMap<>();
        this.dsI18nUtil = I18nUtils.initI18n(globalMeta.getI18nUtils());
        this.dsFeatures = new ConcurrentHashMap<>();
        this.dsDriverFamily = new ArrayList<>();

        List<Enum<?>> dsProduct = this.pluginInfo.getEnumArray("dsProduct", DataSourceType.values());
        if (dsProduct.isEmpty()) {
            throw new InvalidClassException(pluginClass, "dsProduct is empty.");
        } else if (dsProduct.size() == 1) {
            this.dsType = (DataSourceType) dsProduct.get(0);
        } else {
            throw new InvalidClassException("Plugin dsProduct not support multi, class=" + pluginClass);
        }
    }

    @Override
    public boolean isDsPlugin() { return true; }

    @Override
    public Map<String, Object> getPlusFeatures() { return this.dsFeatures; }

    @Override
    public I18nUtils getPlusI18nUtil() { return this.dsI18nUtil; }

    @Override
    public DataSourceType getDsType() { return this.dsType; }

    @Override
    public SqlBuilder getDsSqlBuilder() { return this.dsSqlBuilder; }

    @Override
    public Dialect getDsDialect() { return this.dsDialect; }

    @Override
    public List<String> getBindDrivers() { return this.dsDriverFamily; }

    //

    @Override
    public <T extends Spi> List<T> findSpi(Class<T> spiType) {
        Map<String, Spi> spiMap = this.dsSpiMap.get(spiType);
        if (CollectionUtils.isEmpty(spiMap)) {
            return Collections.emptyList();
        } else {
            return spiMap.values().stream().map(spi -> (T) spi).collect(Collectors.toList());
        }
    }

    @Override
    public <T extends Spi> T findSpi(Class<T> spiType, String named) {
        Map<String, Spi> spiMap = this.dsSpiMap.get(spiType);
        if (CollectionUtils.isEmpty(spiMap)) {
            return null;
        } else {
            return (T) spiMap.get(named);
        }
    }

    public void addSpi(Class<?> spiType, String named, Spi spi) {
        if (spiType != null && spi != null) {
            this.dsSpiMap.computeIfAbsent(spiType, key -> {
                return new ConcurrentHashMap<>();
            }).put(named, spi);
        }
    }

    //

    public void bindSessionFactory(Class<? extends SessionFactory<?>> factoryClass) {
        this.dsSessionFactory = factoryClass.getName();
    }

    public void bindSqlBuilder(SqlBuilder dsSqlBuilder) {
        this.dsSqlBuilder = dsSqlBuilder;
    }

    public void bindDsDialect(Dialect dsDialect) {
        this.dsDialect = dsDialect;
    }

    public void bindDriverFamily(String... driverFamily) {
        for (String family : driverFamily) {
            if (!this.dsDriverFamily.contains(family)) {
                this.dsDriverFamily.add(family);
            }
        }
    }

    //

    @Override
    public DsFactory<?> createDriver(String driverFamily, String driverVer) throws Exception {
        String key = buildDriverKey(driverFamily, driverVer);
        ensureDriverCaches(driverFamily, driverVer, key);
        DsFactory<?> cached = this.driverFactoryCache.get(key);
        if (cached != null) {
            return cached;
        }

        synchronized (this.driverFactoryCache) {
            cached = this.driverFactoryCache.get(key);
            if (cached != null) {
                return cached;
            }

            DriverVersion driverVersion = PluginManager.driverLoader().findDriver(driverFamily, driverVer);
            if (driverVersion == null) {
                throw new UnsupportedOperationException("no driver metadata for '" + key + "'.");
            }

            String dsFactoryName = StringUtils.trimToNull(driverVersion.getDsFactory());
            if (dsFactoryName == null) {
                throw new UnsupportedOperationException("no driver dsFactory configured for '" + key + "'.");
            }

            DriverBinding binding = findBinding(driverFamily, driverVer, key);
            Class<?> factoryType = binding.asClassLoader().loadClass(dsFactoryName);
            if (!DsFactory.class.isAssignableFrom(factoryType)) {
                throw new IllegalStateException(dsFactoryName + " is not a DsFactory, driverVersion='" + key + "'.");
            }

            cached = (DsFactory<?>) factoryType.getDeclaredConstructor().newInstance();
            this.driverFactoryCache.put(key, cached);
            return cached;
        }
    }

    @Override
    public SessionFactory<?> createSessionFactory(String driverFamily, String driverVer) throws Exception {
        String key = buildDriverKey(driverFamily, driverVer);
        ensureDriverCaches(driverFamily, driverVer, key);
        SessionFactory<?> cached = this.sessionFactoryCache.get(key);
        if (cached != null) {
            return cached;
        }

        synchronized (this.sessionFactoryCache) {
            cached = this.sessionFactoryCache.get(key);
            if (cached != null) {
                return cached;
            }

            String sessionFactoryName = StringUtils.trimToNull(this.dsSessionFactory);
            if (sessionFactoryName == null) {
                throw new UnsupportedOperationException("no sessionFactory configured for '" + key + "'.");
            }

            DriverBinding binding = findBinding(driverFamily, driverVer, key);
            Class<?> sessionFactoryType = binding.asClassLoader().loadClass(sessionFactoryName);
            if (!SessionFactory.class.isAssignableFrom(sessionFactoryType)) {
                throw new IllegalStateException(sessionFactoryName + " is not a SessionFactory, driverVersion='" + key + "'.");
            }

            cached = (SessionFactory<?>) sessionFactoryType.getDeclaredConstructor().newInstance();
            this.sessionFactoryCache.put(key, cached);
            return cached;
        }
    }

    private DriverBinding findBinding(String driverFamily, String driverVer, String key) {
        ensureDriverCaches(driverFamily, driverVer, key);
        DriverBinding cached = this.driverBindingCache.get(key);
        if (cached != null) {
            return cached;
        }

        synchronized (this.driverBindingCache) {
            cached = this.driverBindingCache.get(key);
            if (cached != null) {
                return cached;
            }

            DriverBinding binding = PluginManager.driverLoader().createBinding(this.pluginClassLoader, driverFamily, driverVer);
            if (binding == null) {
                throw new UnsupportedOperationException("no DriverBinding for driverVersion '" + key + "'.");
            }

            this.configIncludeExclude(binding.asClassLoader());
            this.driverBindingCache.put(key, binding);
            return binding;
        }
    }

    private void ensureDriverCaches(String driverFamily, String driverVer, String key) {
        DriverVersion driverVersion = PluginManager.driverLoader().findDriver(driverFamily, driverVer);
        if (driverVersion == null) {
            clearDriverCaches(key);
            return;
        }

        DriverBinding cachedBinding = this.driverBindingCache.get(key);
        if (cachedBinding == null || !cachedBinding.isExpired()) {
            return;
        }

        synchronized (this.driverBindingCache) {
            DriverBinding bindingInLock = this.driverBindingCache.get(key);
            if (bindingInLock == null || !bindingInLock.isExpired()) {
                return;
            }

            clearDriverCaches(key);
        }
    }

    private void clearDriverCaches(String key) {
        this.driverBindingCache.remove(key);
        this.driverFactoryCache.remove(key);
        this.sessionFactoryCache.remove(key);
    }

    private static String buildDriverKey(String driverFamily, String driverVer) {
        String normalizedFamily = StringUtils.trimToNull(driverFamily);
        String normalizedVersion = StringUtils.trimToNull(driverVer);
        if (normalizedFamily == null || normalizedVersion == null) {
            throw new IllegalArgumentException("driverFamily/driverVer is blank.");
        }
        return normalizedFamily + "/" + normalizedVersion;
    }
}
