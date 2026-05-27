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
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.*;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.UiMenus18nKey;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.drivers.DriverLoader;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.RdpDsResourceService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.BeanMap;

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
    private RdpDsResourceService                dsResourceService;
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
            config.setClassify(dsType.getDsClassify());
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

        if (this.dsDal.configMapper().queryById(dsDO.getUid(), dsDO.getId()) == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_QUERY_NEED_ENABLE.name()));
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
    public DataSourceConfig fetchDsConfigFromDM(long dsId, DataSourceType dsType) {
        List<DmDsConfigKv4DmDO> configs = this.dsDal.configKv4DmMapper().listByDsId(dsId);
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);

        Map<String, String> configMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(configs)) {
            configs.forEach(c -> {
                configMap.put(c.getConfigName(), c.getConfigValue());
            });
        }

        DataSourceConfig dsConfig = this.generateDsConfig(dsDO, configMap);
        dsConfig.deserialize();

        decryptValue(dsConfig, DataSourceConfig.class);
        decryptValue(dsConfig, dsConfig.getClass());
        return dsConfig;
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

    @Override
    public DataSourceConfig fetchDsConfigFromRDP(long dsId, DataSourceType dsType, HostType hostType) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        HostType ht = hostType == null ? dsDO.getHostType() : hostType;
        List<DmDsConfigKv4RdpDO> configs = this.collectConfigFromRdp(dsDO, ht, dsDO.getVersion());

        Map<String, String> configMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(configs)) {
            configs.forEach(c -> configMap.put(c.getConfigName(), c.getConfigValue()));
        }

        DataSourceConfig dsConfig = this.generateDsConfig(dsDO, configMap);
        DmDsConfigHelper.fillFieldValue(dsConfig, configMap);
        dsConfig.deserialize();
        return dsConfig;
    }

    @Override
    public DataSourceConfig fetchDsConfigFromTemp(DmDsDO dsDO, Map<String, String> configMap, HostType hostType) {
        Map<String, String> resolvedConfigMap = configMap == null ? Collections.emptyMap() : configMap;
        DataSourceConfig dsConfig = this.genDsConfig(dsDO, null, hostType, dsDO.getVersion(), dsDO.getDriver());
        DmDsConfigHelper.fillFieldValue(dsConfig, resolvedConfigMap);
        dsConfig.deserialize();
        return dsConfig;
    }

    @Override
    public String fetchDsConfig(long dsId, String configKey) {
        DmDsConfigKv4DmDO configs = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(dsId, configKey);
        return configs == null ? null : configs.getConfigValue();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void persistDsConfig(DmDsDO dsDO, HostType hostType, String version) {
        List<DmDsConfigKv4RdpDO> configs = this.collectConfigFromRdp(dsDO, hostType, version);
        for (DmDsConfigKv4RdpDO config : configs) {
            if (config.isSecret()) {
                config.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(config.getConfigValue()));
            }
            DmDsConfigKv4DmDO dmConfig = DmConvertUtils.convertToDmDsKvBaseConfigDOForInsert(config);
            this.dsDal.configKv4DmMapper().insert(dmConfig);
        }
    }

    private DataSourceConfig generateDsConfig(DmDsDO dsDO, Map<String, String> configMap) {
        RdpDsExtraConfGen gen = this.dsResourceService.getDsExtraConfGen(dsDO.getDataSourceType());
        DsExtraConfig extraConfig = null;
        if (gen != null) {
            extraConfig = gen.genDsExtraConfigFromExist(dsDO, fetchConfig(dsDO.getId()));
        }

        DataSourceConfig dsConfig = this.genDsConfig(dsDO, extraConfig, dsDO.getHostType(), dsDO.getVersion(), dsDO.getDriver());
        DmDsConfigHelper.fillFieldValue(dsConfig, configMap);
        return dsConfig;
    }

    private List<DmDsConfigKv4RdpDO> collectConfigFromRdp(DmDsDO dsDO, HostType hostType, String version) {
        RdpDsExtraConfGen gen = this.dsResourceService.getDsExtraConfGen(dsDO.getDataSourceType());
        DsExtraConfig extraConfig = null;
        if (gen != null) {
            extraConfig = gen.genDsExtraConfigFromExist(dsDO, fetchConfig(dsDO.getId()));
        }

        DataSourceConfig dsConfig = this.genDsConfig(dsDO, extraConfig, hostType, version, dsDO.getDriver());
        List<DmDsConfigKv4RdpDO> dvConfigs = DmDsConfigHelper.collectConfigs(dsConfig);
        for (DmDsConfigKv4RdpDO config : dvConfigs) {
            config.setDataSourceId(dsDO.getId());

            if (config.isSecret() && StringUtils.isNotBlank(config.getConfigValue())) {
                try {
                    config.setConfigValue(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(config.getConfigValue()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_HOST)) {
                if (hostType == HostType.PUBLIC) {
                    config.setConfigValue(dsDO.getPublicHost());
                } else if (hostType == HostType.PRIVATE) {
                    config.setConfigValue(dsDO.getPrivateHost());
                } else {
                    config.setConfigValue(dsDO.getHost());
                }
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_SEC_TYPE)) {
                config.setConfigValue(dsDO.getSecurityType().name());
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_USERNAME)) {
                config.setConfigValue(dsDO.getAccount());
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_PASSWORD)) {
                config.setConfigValue(config.getConfigValue());
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_VERSION)) {
                config.setConfigValue(version);
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_DRIVER_VERSION)) {
                config.setConfigValue(dsDO.getDriver());
            } else if (config.getConfigName().equals(ConfigKeys.DM_DS_KEY_STORE_PASSWORD)) {
                config.setConfigValue(config.getConfigValue());
            }
        }

        return dvConfigs;
    }

    private List<DmDsConfigKv4RdpDO> fetchConfig(long dsId) {
        List<DmDsConfigKv4RdpDO> confList = this.dsDal.configKv4RdpMapper().listByDsId(dsId);

        for (DmDsConfigKv4RdpDO confDO : confList) {
            if (confDO.isSecret() && StringUtils.isNotBlank(confDO.getConfigValue())) {
                try {
                    confDO.setConfigValue(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(confDO.getConfigValue()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return confList;
    }

    @Override
    public void cleanDsConfig(long dsId) {
        this.dsDal.configKv4DmMapper().deleteDsConfigs(dsId);
    }

    @Override
    public Map<String, String> fetchSettingsMap(String ownerUid, List<String> names) {
        return this.configService.fetchSettingsMap(ownerUid, names);
    }

    public List<DmDsConfigKv4RdpDO> fetchDsConfigDef(DataSourceType dsType) {
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        DataSourceConfig dsConfig = configSpi.newConfig(globalDefault());
        dsConfig = DmDsConfigHelper.initFieldDefaultValue(dsConfig);
        dsConfig.deserialize();
        return DmDsConfigHelper.collectConfigs(dsConfig);
    }

    protected Map<String, String> globalDefault() {
        return Collections.emptyMap();
    }

    private DataSourceConfig genDsConfig(DmDsDO dsDO, DsExtraConfig extraConfig, HostType hostType, String version, String driver) {
        Map<String, String> configMap = new HashMap<>(globalDefault());
        // TODO put configMap from extraConfig

        //
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsDO.getDataSourceType());
        DataSourceConfig config = configSpi.newConfig(configMap);
        config = DmDsConfigHelper.initFieldDefaultValue(config);

        config.setInstanceId(dsDO.getInstanceId());
        config.setSecurityType(dsDO.getSecurityType());
        config.setVersion(version);
        config.setDriverVersion(driver);
        dsDO.setHostType(hostType);

        if (hostType == HostType.PUBLIC && StringUtils.isNotBlank(dsDO.getPublicHost())) {
            config.setHost(dsDO.getPublicHost());
        } else {
            config.setHost(dsDO.getPrivateHost());
        }

        config.setUserName(dsDO.getAccount());
        config.setPassword(dsDO.getPassword());
        config.setStorePassword(dsDO.getClientTrustStorePassword());

        DsConfigMap dsConfigMap = new DsConfigMap();
        dsConfigMap.setDefaultConfig(configMap);
        dsConfigMap.setRdpDsBean(new BeanMap(dsDO));
        dsConfigMap.setRdpExtraBean(new BeanMap(extraConfig));
        configSpi.fillConfig(config, dsConfigMap);
        config.deserialize();
        return config;
    }
}
