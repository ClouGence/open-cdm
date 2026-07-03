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

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.*;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.UiMenus18nKey;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigField;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.resource.ResourceCategory;
import com.clougence.clouddm.sdk.resource.ResourceSpi;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.drivers.DriverLoader;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 14:27
 */
@Slf4j
@Service
public class DmDsConfigServiceImpl implements DmDsConfigService, UnifiedPostConstruct {

    @Resource
    private DataSourceDal                       dsDal;
    @Resource
    private ObjectCacheDao                      cacheDao;
    @Resource
    private ConsoleConfigService                configService;
    @Resource
    private WhiteListService                    whiteListService;

    private final Map<DataSourceType, DsConfig> dsSettingsCache = new HashMap<>();
    private final Map<String, DsLevelLeaf>      dsLeafCache     = new HashMap<>();
    private final AtomicBoolean                 inited          = new AtomicBoolean();

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            ((UnifiedPostConstruct) this.whiteListService).init();
            for (DataSourceType dsType : DataSourceType.values()) {
                this.dsConstantSettings(dsType);
            }

            log.info("DataSource config operate instance inited.");
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Map<DataSourceType, DsConfig> dsConstantSettings() {
        Map<DataSourceType, DsConfig> data = new HashMap<>();
        for (DataSourceType dsType : DataSourceType.values()) {
            DsConfig dsConfig = this.dsConstantSettings(dsType);
            if (dsConfig != null) {
                data.put(dsType, dsConfig.clone());
            }
        }
        return data;
    }

    @Override
    public DsConfig dsConstantSettings(DataSourceType dsType) {
        if (PluginManager.findDsPlugin(dsType) == null || !this.whiteListService.checkDs(dsType)) {
            return null;
        }
        if (this.dsSettingsCache.containsKey(dsType)) {
            return this.dsSettingsCache.get(dsType);
        }

        synchronized (this) {
            if (this.dsSettingsCache.containsKey(dsType)) {
                return this.dsSettingsCache.get(dsType);
            }

            DsConfig config = new DsConfig();
            config.setFeatures(PluginManager.hasFeature(dsType));
            config.setConstant(new DsConstantConfig());
            config.setCategories(new DsCategories());
            config.setMenus(new HashMap<>());

            // drivers
            DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
            DriverLoader driverLoader = PluginManager.driverLoader();
            List<String> familyNames = dsPlugin.getBindDrivers();
            config.setDriverFamilies(familyNames.stream().map(s -> {
                return DmConvertUtils.convertToDsDriverFamily(driverLoader.findDriver(s));
            }).filter(Objects::nonNull).collect(Collectors.toList()));
            config.setLanguage(loadDsLanguage(dsPlugin));

            //
            RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsType);
            if (supportSpi != null) {
                config.setIsolations(supportSpi.supportIsolation().stream().map(isolation -> {
                    I18nDmLabelKeys i18nKey = I18nDmLabelKeys.valueOf("RDB_ISOLATION_" + isolation.getName());
                    return new DsIsolation(isolation.getName(), DmI18nUtils.getMessage(i18nKey.name()));
                }).collect(Collectors.toList()));
            } else {
                config.setIsolations(Collections.emptyList());
            }

            CmdTemplateSpi cmdTemplate = PluginManager.findCmdTemplateSpi(dsType);
            DsConstantConfig constant = config.getConstant();
            if (cmdTemplate != null) {
                CmdTemplateOption option = new CmdTemplateOption();
                option.setDelimited(true);
                option.setDefaultLimit(20);
                constant.setQuickQueryMap(loadQuickQueryMap(cmdTemplate, option));
            }
            DsBrowseSpi browseSpi = PluginManager.findDsBrowseSpi(dsType);
            if (browseSpi != null) {
                // Levels and Leaf
                List<String> levels = browseSpi.getLevels().stream().map(UmiTypes::getTypeName).collect(Collectors.toList());
                levels.add(0, DsMenuType.Instance.getTypeName());
                levels.add(0, DsMenuType.Env.getTypeName());
                config.getCategories().setLevels(levels);
                config.getCategories().setLeafExpand(browseSpi.getLeafExpand().stream().map(UmiTypes::getTypeName).collect(Collectors.toList()));
                config.getCategories().setLeafGroup(loadLeaf(browseSpi.getLeafGroupMap()));

                constant.setLeftQualifier(browseSpi.getLeftQualifier());
                constant.setRightQualifier(browseSpi.getRightQualifier());
                constant.setCaseType(browseSpi.getCaseType());

                // menus
                for (DsMenuType menuType : DsMenuType.values()) {
                    List<String> umiMenuTemp = browseSpi.getMenus(menuType);
                    List<DsMenu> menuInfoList = DsMenuUtils.generationDsMenus(umiMenuTemp);
                    config.getMenus().put(menuType.getTypeName(), menuInfoList);
                }
            }

            // target ds
            ConvertTableDDLSpi convertDDLSpi = PluginManager.findConvertDDLSpi(dsType);
            if (convertDDLSpi != null) {
                List<DataSourceType> dataSourceTypes = convertDDLSpi.convertDDLTargetList();
                List<String> result = new ArrayList<>();
                dataSourceTypes.forEach(ds -> {
                    result.add(ds.getTypeName());
                });
                config.setTargetDsList(result);

                List<DDLType> ddlTypes = convertDDLSpi.ddlTypeList();
                List<String> ddlResult = new ArrayList<>();
                ddlTypes.forEach(ds -> {
                    ddlResult.add(ds.getTypeName());
                });
                config.setDdlList(ddlResult);
            } else {
                config.setTargetDsList(Collections.emptyList());
                config.setDdlList(Collections.emptyList());
            }

            this.dsSettingsCache.put(dsType, config);
        }
        return this.dsSettingsCache.get(dsType);
    }

    protected Map<String, String> loadQuickQueryMap(CmdTemplateSpi cmdTemplateSpi, CmdTemplateOption option) {
        Map<String, String> quickQueryMap = new HashMap<>();
        String sql;
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByTable(option))) {
            quickQueryMap.put(UmiTypes.Table.getTypeName(), sql);
            quickQueryMap.put(UmiTypes.ExternalTable.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByView(option))) {
            quickQueryMap.put(UmiTypes.View.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByColumn(option))) {
            quickQueryMap.put(UmiTypes.Column.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByMaterialized(option))) {
            quickQueryMap.put(UmiTypes.Materialized.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByProcedure(option))) {
            quickQueryMap.put(UmiTypes.Procedure.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByFunction(option))) {
            quickQueryMap.put(UmiTypes.Function.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByTrigger(option))) {
            quickQueryMap.put(UmiTypes.Trigger.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryBySequence(option))) {
            quickQueryMap.put(UmiTypes.Sequence.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryBySynonym(option))) {
            quickQueryMap.put(UmiTypes.Synonym.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByScheduleJob(option))) {
            quickQueryMap.put(UmiTypes.ScheduleJob.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryByJob(option))) {
            quickQueryMap.put(UmiTypes.Job.getTypeName(), sql);
        }
        if (StringUtils.isNotBlank(sql = cmdTemplateSpi.getQuickQueryKey(option))) {
            quickQueryMap.put(UmiTypes.Key.getTypeName(), sql);
        }
        return quickQueryMap;
    }

    private List<DsLevelLeaf> loadLeaf(List<UmiTypes> umiTypes) {
        List<DsLevelLeaf> result = new ArrayList<>(umiTypes.size());
        for (UmiTypes umiType : umiTypes) {
            String typeName = umiType.getTypeName();
            if (this.dsLeafCache.containsKey(typeName)) {
                result.add(this.dsLeafCache.get(typeName));
            } else {
                DsLevelLeaf dsLeaf = new DsLevelLeaf();
                dsLeaf.setType(typeName);
                dsLeaf.setI18n(DmI18nUtils.getMessage(UiMenus18nKey.findI18nKey(umiType)));
                this.dsLeafCache.put(typeName, dsLeaf);
                result.add(dsLeaf);
            }
        }
        return result;
    }

    private Map<String, List<DsLevelLeaf>> loadLeaf(Map<UmiTypes, List<UmiTypes>> map) {
        Map<String, List<DsLevelLeaf>> result = new HashMap<>(map.size());
        for (UmiTypes umiTypes : map.keySet()) {
            result.put(umiTypes.getTypeName(), loadLeaf(map.get(umiTypes)));
        }

        return result;
    }

    @Override
    public DsLevels parseLevels(List<String> levels) {
        if (levels.size() < 2) {
            throw new IllegalArgumentException("levels format error.");
        }

        String envId = levels.get(0);
        String dsId = levels.get(1);

        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        if (dsDO == null || dsDO.getLifeCycleState() == LifeCycleState.DELETED || dsDO.getLifeCycleState() == LifeCycleState.DELETING) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        DsConfig dsConfig = this.dsConstantSettings(dsDO.getDataSourceType());
        if (dsConfig == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DATA_PLUGIN_NOT_EXIST_ERROR.name()));
        }

        List<String> levelsDef = dsConfig.getCategories().getLevels();

        List<UmiTypes> curLevelsDef = new ArrayList<>();
        Map<UmiTypes, Object> curLevelsParam = new HashMap<>();
        for (int i = 2; i < levels.size(); i++) {
            UmiTypes umiType = UmiTypes.valueOfCode(levelsDef.get(i));
            curLevelsParam.put(umiType, levels.get(i));
            curLevelsDef.add(umiType);
        }

        List<String> dbLevels = new ArrayList<>(levels.subList(2, levels.size()));
        return new DsLevels(envId, dsDO, levels, dbLevels, curLevelsDef, curLevelsParam);
    }

    @Override
    public DsLevels parseLevels(String levels) {
        if (levels == null || levels.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        levels = StringUtils.trimEnd(levels, '/');
        return this.parseLevels(Arrays.asList(StringUtils.split(levels, "/")));
    }

    @Override
    public DataSourceConfig fetchDsConfigFromExists(long dsId) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        List<DmDsConfigKv4DmDO> configs = this.dsDal.configKv4DmMapper().listByDsIdExcludeConfigNames(dsId, lazyConfigNames(dsDO.getDataSourceType()));
        return fetchDsConfigFromExists(dsDO, configs, Collections.emptyMap());
    }

    @Override
    public DataSourceConfig fetchDsConfigFromExists(long dsId, Map<DsConfigField, String> configOverrides) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        List<DmDsConfigKv4DmDO> configs = this.dsDal.configKv4DmMapper().listByDsIdExcludeConfigNames(dsId, lazyConfigNames(dsDO.getDataSourceType()));
        return fetchDsConfigFromExists(dsDO, configs, configOverrides);
    }

    @Override
    public DataSourceConfig fetchFullDsConfigFromExists(long dsId) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        List<DmDsConfigKv4DmDO> configs = this.dsDal.configKv4DmMapper().listByDsId(dsId);
        return fetchDsConfigFromExists(dsDO, configs, Collections.emptyMap());
    }

    private DataSourceConfig fetchDsConfigFromExists(DmDsDO dsDO, List<DmDsConfigKv4DmDO> configs, Map<DsConfigField, String> configOverrides) {
        Map<String, String> configMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(configs)) {
            configs.forEach(c -> {
                configMap.put(c.getConfigName(), c.getConfigValue());
            });
        }
        fillConfigOverrides(configMap, configOverrides);

        DataSourceConfig dsConfig = this.generateDsConfig(dsDO, configMap);
        tryClearSshConfig(dsConfig);

        decryptValue(dsConfig, DataSourceConfig.class);
        decryptValue(dsConfig, dsConfig.getClass());
        log.info("fetch datasource config from dm, dsId={}, dsType={}, host={}, sshProxyEnabled={}, sshConfigId={}, rawSshProxyEnabled={}, rawSshConfigId={}",//
                dsDO.getId(), dsConfig.getDataSourceType(), dsConfig.getHost(), dsConfig.getSshProxyEnabled(), dsConfig.getSshConfigId(),//
                configMap.get("sshProxyEnabled"), configMap.get("sshConfigId"));
        return dsConfig;
    }

    private void fillConfigOverrides(Map<String, String> configMap, Map<DsConfigField, String> configOverrides) {
        if (configOverrides == null || configOverrides.isEmpty()) {
            return;
        }
        configOverrides.forEach((configField, configValue) -> {
            if (configField == null || StringUtils.isBlank(configValue)) {
                return;
            }
            configMap.put(configField.getConfigName(), configValue);
        });
    }

    private List<String> lazyConfigNames(DataSourceType dsType) {
        return this.fetchDsConfigDef(dsType).stream().filter(DsConfigKvDef::isLazy).map(DsConfigKvDef::getConfigName).collect(Collectors.toList());
    }

    private void decryptValue(DataSourceConfig dsConfig, Class<?> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                ConfigDef configDef = field.getAnnotation(ConfigDef.class);
                if (configDef == null) {
                    continue;
                }

                if (configDef.isSecret()) {
                    String value = (String) field.get(dsConfig);
                    if (StringUtils.isNotBlank(value)) {
                        field.set(dsConfig, CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(value));
                    }
                }
            }
        } catch (Exception e) {
            String msg = "collect field value failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private DsLanguage loadDsLanguage(DsPluginInfo dsPlugin) {
        List<DsLanguageSpi> languageSpis = dsPlugin.findSpi(DsLanguageSpi.class);
        if (CollectionUtils.isEmpty(languageSpis)) {
            return null;
        }

        DsLanguageSpi languageSpi = languageSpis.get(0);
        Set<DsLanguageSupport> supports = Optional.ofNullable(languageSpi.supports()).orElseGet(Collections::emptySet);
        DsLanguage language = new DsLanguage();
        language.setSupported(CollectionUtils.isNotEmpty(supports));
        language.setSupports(Set.copyOf(supports));
        language.setCompletion(supports.contains(DsLanguageSupport.COMPLETE));
        language.setValidate(supports.contains(DsLanguageSupport.VALIDATE));
        language.setSplit(supports.contains(DsLanguageSupport.SPLIT));
        String keywordResourceModule = findEditorKeywordResourceModule(dsPlugin);
        if (!StringUtils.isBlank(keywordResourceModule)) {
            language.setKeywordResource(ResourceCategory.EDITOR.getCode() + "/" + keywordResourceModule + "@keywords");
        }
        return language;
    }

    private String findEditorKeywordResourceModule(DsPluginInfo dsPlugin) {
        List<ResourceSpi> resourceSpis = dsPlugin.findSpi(ResourceSpi.class);
        if (CollectionUtils.isEmpty(resourceSpis)) {
            return null;
        }

        return resourceSpis.stream().filter(spi -> spi.findResource(ResourceCategory.EDITOR.getCode(), "keywords", null) != null).map(ResourceSpi::name).findFirst().orElse(null);
    }

    @Override
    public DataSourceConfig fetchDsConfigFromNotExist(DmDsDO dsDO, Map<String, String> configMap) {
        Map<String, String> resolvedConfigMap = configMap == null ? Collections.emptyMap() : configMap;
        return this.genDsConfig(dsDO, resolvedConfigMap, dsDO.getVersion(), dsDO.getDriver());
    }

    @Override
    public DataSourceConfig fetchDsConfigFromNotExist(DataSourceType dsType, Map<String, String> configMap) {
        Map<String, String> currentConfigMap = configMap == null ? Collections.emptyMap() : configMap;

        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        DataSourceConfig config = ClassUtils.newInstance(configSpi.newConfig());
        config = DmDsConfigHelper.initBaseFieldDefaultValue(config);

        DmDsConfigHelper.fillBaseFieldValue(config, currentConfigMap);
        configSpi.fillConfig(config, currentConfigMap);
        tryClearSshConfig(config);
        return config;
    }

    @Override
    public String fetchDsConfig(long dsId, String configKey) {
        DmDsConfigKv4DmDO configs = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(dsId, configKey);
        return configs == null ? null : configs.getConfigValue();
    }

    @Override
    public void upsertDsConfig(long dsId, String configKey, String configValue) {
        if (StringUtils.isBlank(configKey)) {
            return;
        }
        DsCacheEntry dmDsDO = this.cacheDao.queryByDsId(dsId);
        if (dmDsDO == null || dmDsDO.getDsType() == null) {
            return;
        }

        if (StringUtils.equals(configKey, DataSourceConfig.Fields.version)) {
            this.dsDal.dsMapper().updateVersionByInstanceId(dsId, configValue);
            return;
        }

        DsConfigKvDef configDef = this.fetchDsConfigDef(dmDsDO.getDsType()).stream().filter(config -> {
            return StringUtils.equals(config.getConfigName(), configKey);
        }).findFirst().orElse(null);
        if (configDef == null) {
            return;
        }

        if (StringUtils.isBlank(configValue)) {
            this.dsDal.configKv4DmMapper().deleteDsConfig(dsId, configKey);
            return;
        }

        String value = configValue;
        if (configDef.isSecret()) {
            value = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(value);
        }

        DmDsConfigKv4DmDO configDO = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(dsId, configKey);
        if (configDO == null) {
            DmDsConfigKv4DmDO newConfig = new DmDsConfigKv4DmDO();
            newConfig.setDataSourceId(dsId);
            newConfig.setConfigName(configKey);
            newConfig.setConfigValue(value);
            this.dsDal.configKv4DmMapper().insert(newConfig);
        } else if (configDef.isReadOnly()) {
            return;
        } else {
            this.dsDal.configKv4DmMapper().updateDsConfig(dsId, configKey, value);
        }
    }

    @Override
    public void upsertDsConfigs(long dsId, Map<String, String> configMap) {
        if (configMap == null || configMap.isEmpty()) {
            return;
        }

        configMap.forEach((configKey, configValue) -> this.upsertDsConfig(dsId, configKey, configValue));
    }

    private DataSourceConfig generateDsConfig(DmDsDO dsDO, Map<String, String> configMap) {
        return this.genDsConfig(dsDO, configMap, dsDO.getVersion(), dsDO.getDriver());
    }

    @Override
    public void cleanDsConfig(long dsId) {
        this.dsDal.configKv4DmMapper().deleteDsConfigs(dsId);
    }

    @Override
    public Map<String, String> fetchSettingsMap(List<String> names) {
        return this.configService.fetchSettingsMap(names);
    }

    @Override
    public List<DsConfigKvDef> fetchDsConfigDef(DataSourceType dsType) {
        return this.fetchDsConfigDef(dsType, Collections.emptyMap());
    }

    @Override
    public List<UiPanel> fetchDsConfigPanels(DataSourceType dsType, Map<String, String> defaultConfig) {
        Map<String, String> uiDefaultConfig = new LinkedHashMap<>();
        if (defaultConfig != null) {
            uiDefaultConfig.putAll(defaultConfig);
        }

        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        if (configSpi != null) {
            configSpi.customizeUiMap(uiDefaultConfig, new LinkedHashMap<>(uiDefaultConfig));
        }

        Map<DsConfigGroup, Map<String, DsConfigKvDef>> fieldsByGroup = new EnumMap<>(DsConfigGroup.class);
        for (DsConfigKvDef configDef : this.fetchDsConfigDef(dsType, uiDefaultConfig)) {
            if (!DataSourceConfig.Fields.host.equals(configDef.getConfigName()) && uiDefaultConfig.containsKey(configDef.getConfigName())) {
                configDef.setConfigValue(uiDefaultConfig.get(configDef.getConfigName()));
            }
            if (isCertificateConfigured(configDef.getConfigName(), uiDefaultConfig)) {
                configDef.setConfigValue(DmDsConfigHelper.CERTIFICATE_CONFIGURED_VALUE);
            }
            fieldsByGroup.computeIfAbsent(configDef.getConfigGroup(), key -> new LinkedHashMap<>()).put(configDef.getConfigName(), configDef);
        }
        return new DmDsConfigUiPanelFactory().create(dsType, fieldsByGroup);
    }

    private boolean isCertificateConfigured(String configName, Map<String, String> configMap) {
        if (!StringUtils.equals(configName, DataSourceConfig.Fields.sslCaData) && !StringUtils.equals(configName, DataSourceConfig.Fields.sslClientCertData)
            && !StringUtils.equals(configName, DataSourceConfig.Fields.sslClientKeyData)) {
            return false;
        }
        String configValue = configMap == null ? null : configMap.get(configName);
        return StringUtils.isNotBlank(configValue);
    }

    @Override
    public List<DsConfigKvDef> fetchDsConfigDef(DataSourceType dsType, Map<String, String> defaultConfig) {
        Map<String, String> configMap = new HashMap<>();
        if (defaultConfig != null) {
            configMap.putAll(defaultConfig);
        }

        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        DataSourceConfig dsConfig = ClassUtils.newInstance(configSpi.newConfig());

        dsConfig = DmDsConfigHelper.initBaseFieldDefaultValue(dsConfig);
        DmDsConfigHelper.fillBaseFieldValue(dsConfig, configMap);

        configSpi.fillConfig(dsConfig, configMap);
        return DmDsConfigHelper.collectConfigs(dsConfig);
    }

    private DataSourceConfig genDsConfig(DmDsDO dsDO, Map<String, String> currentConfigMap, String version, String driver) {
        Map<String, String> configMap = new HashMap<>();
        if (currentConfigMap != null) {
            configMap.putAll(currentConfigMap);
        }
        configMap.putAll(this.collectBaseConfigMap(dsDO, version, driver));

        // special apply for xxDs
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsDO.getDataSourceType());
        DataSourceConfig config = ClassUtils.newInstance(configSpi.newConfig());
        config = DmDsConfigHelper.initBaseFieldDefaultValue(config);
        DmDsConfigHelper.fillBaseFieldValue(config, configMap);
        configSpi.fillConfig(config, configMap);
        tryClearSshConfig(config);
        return config;
    }

    private void tryClearSshConfig(DataSourceConfig dsConfig) {
        if (!Boolean.TRUE.equals(dsConfig.getSshProxyEnabled())) {
            dsConfig.setSshConfigId(null);
        }
    }

    private Map<String, String> collectBaseConfigMap(DmDsDO dsDO, String version, String driver) {
        Map<String, String> configMap = new HashMap<>();
        putIfNotBlank(configMap, DataSourceConfig.Fields.instanceId, dsDO.getInstanceId());
        putIfNotBlank(configMap, DataSourceConfig.Fields.dataSourceType, dsDO.getDataSourceType() == null ? null : dsDO.getDataSourceType().name());
        putIfNotBlank(configMap, DataSourceConfig.Fields.version, version);
        putIfNotBlank(configMap, DataSourceConfig.Fields.driverVersion, driver);
        putIfNotBlank(configMap, DataSourceConfig.Fields.securityType, dsDO.getSecurityType() == null ? null : dsDO.getSecurityType().name());
        putIfNotBlank(configMap, DataSourceConfig.Fields.userName, dsDO.getAccessKey());
        putIfNotBlank(configMap, DataSourceConfig.Fields.password, dsDO.getSecretKey());
        putIfNotBlank(configMap, DataSourceConfig.Fields.host, dsDO.getHost());
        return configMap;
    }

    private void putIfNotBlank(Map<String, String> configMap, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            configMap.putIfAbsent(key, value);
        }
    }
}
