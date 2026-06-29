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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.config.FoldTypeConfig;
import com.clougence.clouddm.base.metadata.ui.form.value.OptionValueDef;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.model.vo.datasource.DsAddUiPanelVO;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsConfigKv4DmMapper;
import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsMapper;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.JsonUtils;

import lombok.experimental.FieldNameConstants;

public class DmDsConfigServiceImplTest {

    private Object previousPlugin;
    private Object previousSpiCache;

    @After
    public void clearInjectedPlugin() throws Exception {
        restore(dsMetaMap(), DataSourceType.MySQL.getTypeName(), previousPlugin);
        restore(dsSpiCache(), DataSourceType.MySQL.getTypeName(), previousSpiCache);
    }

    @Test
    public void fetchDsConfigFromDMCreatesPluginConfigFromUnifiedKv() throws Exception {
        PluginConfigSpi configSpi = new PluginConfigSpi();
        registerPlugin(configSpi);

        DmDsDO dsDO = new DmDsDO();
        dsDO.setId(10L);
        dsDO.setDataSourceType(DataSourceType.MySQL);
        dsDO.setSecurityType(SecurityType.USER_PASSWD);
        dsDO.setHost("10.0.0.8:3306");
        dsDO.setVersion("8.0");
        dsDO.setDriver("mysql-driver");
        dsDO.setInstanceId("mysql-10");
        dsDO.setAccessKey("root");
        dsDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt("fresh-password"));

        DmDsConfigKv4DmDO historicalExtra = new DmDsConfigKv4DmDO();
        historicalExtra.setDataSourceId(dsDO.getId());
        historicalExtra.setConfigName("pluginOnlyOption");
        historicalExtra.setConfigValue("from-migrated-rdp-kv");

        DmDsConfigKv4DmDO stalePassword = new DmDsConfigKv4DmDO();
        stalePassword.setDataSourceId(dsDO.getId());
        stalePassword.setConfigName(DataSourceConfig.Fields.password);
        stalePassword.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt("stale-password"));

        DmDsConfigKv4DmDO sshProxyEnabled = new DmDsConfigKv4DmDO();
        sshProxyEnabled.setDataSourceId(dsDO.getId());
        sshProxyEnabled.setConfigName("sshProxyEnabled");
        sshProxyEnabled.setConfigValue("true");

        DmDsConfigKv4DmDO sshConfigId = new DmDsConfigKv4DmDO();
        sshConfigId.setDataSourceId(dsDO.getId());
        sshConfigId.setConfigName("sshConfigId");
        sshConfigId.setConfigValue("12");

        DmDsConfigServiceImpl service = new DmDsConfigServiceImpl();
        setField(service, "dsDal", dsDal(dsDO, List.of(historicalExtra, stalePassword, sshProxyEnabled, sshConfigId)));

        DataSourceConfig config = service.fetchDsConfigFromExists(dsDO.getId());

        assertTrue(config instanceof PluginConfig);
        PluginConfig pluginConfig = (PluginConfig) config;
        assertSame(config, configSpi.lastFillConfig);
        assertEquals("from-migrated-rdp-kv", pluginConfig.getPluginOnlyOption());
        assertEquals("mysql-10", pluginConfig.getInstanceId());
        assertEquals(DataSourceType.MySQL, pluginConfig.getDataSourceType());
        assertEquals("8.0", pluginConfig.getVersion());
        assertEquals("mysql-driver", pluginConfig.getDriverVersion());
        assertEquals(SecurityType.USER_PASSWD, pluginConfig.getSecurityType());
        assertEquals("10.0.0.8:3306", pluginConfig.getHost());
        assertEquals("root", pluginConfig.getUserName());
        assertEquals("fresh-password", pluginConfig.getPassword());
        assertEquals(Boolean.TRUE, pluginConfig.getSshProxyEnabled());
        assertEquals(Long.valueOf(12L), pluginConfig.getSshConfigId());
    }

    @Test
    public void collectConfigsUsesConfigDefValTypeBeforeFieldTypeFallback() {
        List<DsConfigKvDef> configs = DmDsConfigHelper.collectConfigs(new PluginConfig());

        DsConfigKvDef textConfig = configs.stream().filter(config -> "pluginOnlyOption".equals(config.getConfigName())).findFirst().orElseThrow();
        DsConfigKvDef jsonConfig = configs.stream().filter(config -> "pluginJsonOption".equals(config.getConfigName())).findFirst().orElseThrow();
        DsConfigKvDef booleanConfig = configs.stream().filter(config -> "pluginBooleanOption".equals(config.getConfigName())).findFirst().orElseThrow();

        assertEquals(ConfigValType.TEXT, textConfig.getConfValType());
        assertEquals(ConfigValType.JSON, jsonConfig.getConfValType());
        assertEquals(ConfigValType.BOOLEAN, booleanConfig.getConfValType());
    }

    @Test
    public void addDsUiPanelVoDoesNotExposeJacksonClassMetadata() {
        UiPanel panel = new UiPanel();
        panel.setKey("advanced");
        panel.addField(UiPanelField.builder()
            .field("connectTimeoutMs")
            .type(UiPanelFieldType.Input)
            .typeConfig(new FoldTypeConfig())
            .defaultValue(OptionValueDef.builder().labelI18N("1000").value("1000").build())
            .build());

        assertTrue(JsonUtils.toJson(panel).contains("@class"));

        List<DsAddUiPanelVO> panels = UiWebUtil.addDsUiPanels2VO(List.of(panel));

        String json = JsonUtils.toJson(panels);
        assertFalse(json, json.contains("@class"));
        assertTrue(json, json.contains("\"defaultValue\""));
        assertTrue(json, json.contains("\"typeConfig\""));
    }

    private void registerPlugin(DsConfigSpi configSpi) throws Exception {
        Map<String, Object> pluginMap = dsMetaMap();
        Map<String, Object> cacheMap = dsSpiCache();
        this.previousPlugin = pluginMap.put(DataSourceType.MySQL.getTypeName(), pluginInfo(configSpi));
        this.previousSpiCache = cacheMap.remove(DataSourceType.MySQL.getTypeName());
    }

    private static DsPluginInfo pluginInfo(DsConfigSpi configSpi) {
        return (DsPluginInfo) Proxy.newProxyInstance(DmDsConfigServiceImplTest.class.getClassLoader(), new Class<?>[] { DsPluginInfo.class }, (proxy, method, args) -> {
            if ("isDsPlugin".equals(method.getName())) {
                return true;
            }
            if ("getDsType".equals(method.getName())) {
                return DataSourceType.MySQL;
            }
            if ("findSpi".equals(method.getName()) && args != null && args.length == 1 && args[0] == DsConfigSpi.class) {
                return List.of(configSpi);
            }
            if ("findSpi".equals(method.getName()) && args != null && args.length == 2 && args[0] == DsConfigSpi.class) {
                return configSpi.name().equals(args[1]) ? configSpi : null;
            }
            if ("getPlusFeatures".equals(method.getName())) {
                return Collections.emptyMap();
            }
            if ("getBindDrivers".equals(method.getName())) {
                return Collections.emptyList();
            }
            if (method.getReturnType() == boolean.class) {
                return false;
            }
            return null;
        });
    }

    private static DataSourceDal dsDal(DmDsDO dsDO, List<DmDsConfigKv4DmDO> configs) {
        DmDsMapper dsMapper = (DmDsMapper) Proxy.newProxyInstance(DmDsConfigServiceImplTest.class
            .getClassLoader(), new Class<?>[] { DmDsMapper.class }, (proxy, method, args) -> "selectById".equals(method.getName()) ? dsDO : null);
        DmDsConfigKv4DmMapper configMapper = (DmDsConfigKv4DmMapper) Proxy.newProxyInstance(DmDsConfigServiceImplTest.class
            .getClassLoader(), new Class<?>[] { DmDsConfigKv4DmMapper.class }, (proxy, method, args) -> {
                if ("listByDsId".equals(method.getName()) || "listByDsIdExcludeConfigNames".equals(method.getName())) {
                    return configs;
                }
                return null;
            });

        return (DataSourceDal) Proxy.newProxyInstance(DmDsConfigServiceImplTest.class.getClassLoader(), new Class<?>[] { DataSourceDal.class }, (proxy, method, args) -> {
            if ("dsMapper".equals(method.getName())) {
                return dsMapper;
            }
            if ("configKv4DmMapper".equals(method.getName())) {
                return configMapper;
            }
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> dsMetaMap() throws Exception {
        Field field = PluginManager.class.getDeclaredField("dsMetaMap");
        field.setAccessible(true);
        return (Map<String, Object>) field.get(null);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> dsSpiCache() throws Exception {
        Field field = PluginManager.class.getDeclaredField("dsSpiCache");
        field.setAccessible(true);
        return (Map<String, Object>) field.get(null);
    }

    private static void restore(Map<String, Object> map, String key, Object value) {
        if (value == null) {
            map.remove(key);
        } else {
            map.put(key, value);
        }
    }

    private static void setField(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static class PluginConfigSpi implements DsConfigSpi {

        private DataSourceConfig lastFillConfig;

        @Override
        public String name() {
            return "test-plugin-config";
        }

        @Override
        public Class<? extends DataSourceConfig> newConfig() {
            return PluginConfig.class;
        }

        @Override
        public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
            this.lastFillConfig = dsConfig;
            PluginConfig config = (PluginConfig) dsConfig;
            config.pluginOnlyOption = defaultConfig.get(PluginConfig.Fields.pluginOnlyOption);
            config.pluginJsonOption = defaultConfig.get(PluginConfig.Fields.pluginJsonOption);
            config.pluginBooleanOption = ConvertUtils.toBoolean(defaultConfig.get(PluginConfig.Fields.pluginBooleanOption), false);
            return dsConfig;
        }

        @Override
        public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        }

        @Override
        public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        }

        @Override
        public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
            return Collections.emptyMap();
        }

        public boolean supportSSL() {
            return false;
        }

        @Override
        public boolean supportSSH() {
            return true;
        }

        @Override
        public boolean supportTx() {
            return false;
        }

        @Override
        public List<SecurityType> securityTypes() {
            return Collections.emptyList();
        }

        @Override
        public List<SslMode> sslModeSet() {
            return Collections.emptyList();
        }

        @Override
        public String defaultPort() {
            return "3306";
        }
    }

    @FieldNameConstants
    public static class PluginConfig extends DataSourceConfig {

        @ConfigDef(name = Fields.pluginOnlyOption, //
                labelKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY, descKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY)
        private String  pluginOnlyOption;

        @ConfigDef(name = Fields.pluginJsonOption, //
                labelKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY, descKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY, valType = ConfigValType.JSON)
        private String  pluginJsonOption;

        @ConfigDef(name = Fields.pluginBooleanOption, //
                labelKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY, descKey = ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY)
        private Boolean pluginBooleanOption;

        public PluginConfig(){
            setDataSourceType(DataSourceType.MySQL);
        }

        public String getPluginOnlyOption() { return pluginOnlyOption; }
    }
}
