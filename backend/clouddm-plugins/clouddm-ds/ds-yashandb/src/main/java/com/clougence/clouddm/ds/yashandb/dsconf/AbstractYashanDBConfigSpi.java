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
package com.clougence.clouddm.ds.yashandb.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.execute.dsconf.capability.ClientTimeZoneExtProperties;
import com.clougence.drivers.adapter.ConvertUtils;

/**
 * 崖山数据库（YashanDB）连接配置 SPI 基类。
 * <p>
 * 两个兼容模式（Oracle / MySQL）的连接能力一致，差别仅在于具体的配置类与默认端口，
 * 因此由子类通过构造器注入自己的 {@link AbstractYashanDBConfig} 实现与 {@code defaultPort}。
 * </p>
 *
 * @author open-cdm
 */
public abstract class AbstractYashanDBConfigSpi extends AbstractDsConfigSpi {

    private final Class<? extends AbstractYashanDBConfig> configClass;
    private final String                                  defaultPort;

    protected AbstractYashanDBConfigSpi(Class<? extends AbstractYashanDBConfig> configClass, String defaultPort){
        this.configClass = configClass;
        this.defaultPort = defaultPort;
    }

    @Override
    public String defaultPort() {
        return this.defaultPort;
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return configClass;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        AbstractYashanDBConfig config = (AbstractYashanDBConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(AbstractYashanDBConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(AbstractYashanDBConfig.Fields.soTimeoutSec), false);
        config.setDefaultCatalog(defaultConfig.get(AbstractYashanDBConfig.Fields.defaultCatalog));
        config.setDefaultSchema(defaultConfig.get(AbstractYashanDBConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(defaultConfig.get(ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD));
        return config;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of();
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public boolean supportTx() {
        return true;
    }
}
