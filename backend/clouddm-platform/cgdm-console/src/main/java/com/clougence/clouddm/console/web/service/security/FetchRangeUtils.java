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
package com.clougence.clouddm.console.web.service.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecRangeVerify;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRange;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRangeItem;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecSaveRangeFO;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.access.entry.EnvCacheEntry;
import com.clougence.clouddm.platform.dal.model.secrule.SecMatchMode;
import com.clougence.clouddm.platform.dal.model.secrule.SecRangeType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public class FetchRangeUtils {

    private static ObjectCacheDao    ownerCacheService;
    private static DmDsConfigService dsConfigService;

    public static void initUtils(ApplicationContext applicationContext) {
        ownerCacheService = applicationContext.getBean(ObjectCacheDao.class);
        dsConfigService = applicationContext.getBean(DmDsConfigService.class);
    }

    private static void envNotExist(SecRangeVerify level, SecRange range, String envId) {
        range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_NOTEXIST_ENV_MESSAGE.name(), envId));
        range.setVerify(level);
    }

    private static void dsNotExist(SecRangeVerify level, SecRange range, String dsId) {
        range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_NOTEXIST_INSTANCE_MESSAGE.name(), dsId));
        range.setVerify(level);
    }

    private static Map<SecRangeType, String> parseLevels(DataSourceType dsType, String[] level) {
        // def
        DsBrowseSpi browseSpi = PluginManager.findDsBrowseSpi(dsType);
        List<SecRangeType> levels = new ArrayList<>();
        levels.add(SecRangeType.Environment);
        levels.add(SecRangeType.Instance);
        for (UmiTypes umiTypes : browseSpi.getLevels()) {
            switch (umiTypes) {
                case Catalog:
                    levels.add(SecRangeType.Catalog);
                    break;
                case Schema:
                    levels.add(SecRangeType.Schema);
                    break;
            }
        }
        levels.add(SecRangeType.TableOrView);
        levels.add(SecRangeType.Column);

        //
        Map<SecRangeType, String> res = new HashMap<>();
        for (int i = 0; i < levels.size(); i++) {
            if (level.length <= i) {
                res.put(levels.get(i), null);
            } else {
                res.put(levels.get(i), level[i]);
            }
        }
        return res;
    }

    private static void fetchRangeFillEnv(SecRange range, String[] levelPrefix) {
        if (levelPrefix.length == 0) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        } else if (!NumberUtils.isNumber(levelPrefix[0])) {
            String envId = levelPrefix[0];
            envNotExist(SecRangeVerify.Broken, range, envId);
            range.setEnvironment(new SecRangeItem(MessageUtils.genEnvNameByEnvId(envId), envId));
        } else {
            String envId = levelPrefix[0];
            EnvCacheEntry envCache = ownerCacheService.queryByEnvId(Long.parseLong(envId));
            if (envCache == null) {
                envNotExist(SecRangeVerify.Broken, range, envId);
                range.setEnvironment(new SecRangeItem(MessageUtils.genEnvNameByEnvId(envId), envId));
            } else {
                range.setEnvironment(new SecRangeItem(envCache.getEnvName(), envId));
            }
        }
    }

    private static void fetchRangeFillDs(SecRange range, String[] levelPrefix) {
        if (levelPrefix.length < 2) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        } else if (!NumberUtils.isNumber(levelPrefix[1])) {
            String dsId = levelPrefix[1];
            dsNotExist(SecRangeVerify.Broken, range, dsId);
            range.setInstance(new SecRangeItem(MessageUtils.genDsNameByDsId(dsId), dsId));
        } else {
            String dsId = levelPrefix[1];
            DsCacheEntry dsCache = ownerCacheService.queryByDsId(Long.parseLong(dsId));
            if (dsCache == null) {
                envNotExist(SecRangeVerify.Broken, range, dsId);
                range.setInstance(new SecRangeItem(MessageUtils.genDsNameByDsId(dsId), dsId));
            } else {
                String dsInstDesc = RdpConvertUtils.removeNoDescription(dsCache.getDsInstDesc());
                range.setInstance(new SecRangeItem(dsCache.getDsInstId(), dsId, dsInstDesc));
                range.setDsType(dsCache.getDsType());
            }
        }
    }

    public static void fetchRangeTypeByEnv(SecRange range, List<String> levelNodes) {
        // levelNodes for env
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
            return;
        }

        boolean hasAnyVerify = false;
        if (range.getMatchMode() == SecMatchMode.EXACT) {
            for (String levelNode : levelNodes) {
                EnvCacheEntry envCache = ownerCacheService.queryByEnvId(Long.parseLong(levelNode));
                if (envCache == null) {
                    envNotExist(SecRangeVerify.Warn, range, levelNode);
                    range.getNodes().add(new SecRangeItem(MessageUtils.genEnvNameByEnvId(levelNode), levelNode));
                } else {
                    hasAnyVerify = true;
                    range.getNodes().add(new SecRangeItem(envCache.getEnvName(), levelNode));
                }
            }
        } else {
            for (String levelNode : levelNodes) {
                hasAnyVerify = true;
                range.getNodes().add(new SecRangeItem(levelNode, levelNode));
            }
        }

        if (!hasAnyVerify) {
            range.setVerify(SecRangeVerify.Broken);
        }
    }

    public static void fetchRangeTypeByInstance(SecRange range, String[] levelPrefix, List<String> levelNodes) {
        // env
        fetchRangeFillEnv(range, levelPrefix);

        // levelNodes for instance
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
            return;
        }

        boolean hasAnyVerify = false;
        if (range.getMatchMode() == SecMatchMode.EXACT) {
            for (String levelNode : levelNodes) {
                DsCacheEntry dsCache = ownerCacheService.queryByDsId(Long.parseLong(levelNode));
                if (dsCache == null) {
                    dsNotExist(SecRangeVerify.Warn, range, levelNode);
                    range.getNodes().add(new SecRangeItem(MessageUtils.genDsNameByDsId(levelNode), levelNode));
                } else {
                    hasAnyVerify = true;
                    range.getNodes().add(new SecRangeItem(dsCache.getDsInstId(), levelNode, dsCache.getDsInstDesc()));
                }
            }
        } else {
            for (String levelNode : levelNodes) {
                hasAnyVerify = true;
                range.getNodes().add(new SecRangeItem(levelNode, levelNode));
            }
        }

        if (!hasAnyVerify) {
            range.setVerify(SecRangeVerify.Broken);
        }
    }

    public static void fetchRangeTypeByCatalog(SecRange range, String[] levelPrefix, List<String> levelNodes) {
        // env
        fetchRangeFillEnv(range, levelPrefix);

        // instance
        fetchRangeFillDs(range, levelPrefix);

        // levelNodes for Catalog
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            return;
        }

        for (String levelNode : levelNodes) {
            range.getNodes().add(new SecRangeItem(levelNode, levelNode));
        }
    }

    public static void fetchRangeTypeBySchema(SecRange range, String[] levelPrefix, List<String> levelNodes) {
        // env
        fetchRangeFillEnv(range, levelPrefix);

        // instance
        fetchRangeFillDs(range, levelPrefix);

        // catalog
        Map<SecRangeType, String> levelMap = parseLevels(range.getDsType(), levelPrefix);
        if (levelMap.containsKey(SecRangeType.Catalog)) {
            String catalog = levelMap.get(SecRangeType.Catalog);
            if (StringUtils.isBlank(catalog)) {
                range.setVerify(SecRangeVerify.Broken);
                range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            } else {
                range.setCatalog(new SecRangeItem(catalog, catalog));
            }
        }

        // levelNodes for Schema
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_SCHEMA_MESSAGE.name()));
            return;
        }

        for (String levelNode : levelNodes) {
            range.getNodes().add(new SecRangeItem(levelNode, levelNode));
        }
    }

    public static void fetchRangeTypeByTable(SecRange range, String[] levelPrefix, List<String> levelNodes) {
        Map<SecRangeType, String> levelMap = parseLevels(range.getDsType(), levelPrefix);

        // env
        fetchRangeFillEnv(range, levelPrefix);

        // instance
        fetchRangeFillDs(range, levelPrefix);

        // catalog
        if (levelMap.containsKey(SecRangeType.Catalog)) {
            String catalog = levelMap.get(SecRangeType.Catalog);
            if (StringUtils.isBlank(catalog)) {
                range.setVerify(SecRangeVerify.Broken);
                range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            } else {
                range.setCatalog(new SecRangeItem(catalog, catalog));
            }
        }

        // schema
        if (levelMap.containsKey(SecRangeType.Schema)) {
            String schema = levelMap.get(SecRangeType.Schema);
            if (StringUtils.isBlank(schema)) {
                range.setVerify(SecRangeVerify.Broken);
                range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_SCHEMA_MESSAGE.name()));
            } else {
                range.setSchema(new SecRangeItem(schema, schema));
            }
        }

        // levelNodes for Table
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_TABLEVIEW_MESSAGE.name()));
            return;
        }

        for (String levelNode : levelNodes) {
            range.getNodes().add(new SecRangeItem(levelNode, levelNode));
        }
    }

    public static void fetchRangeTypeByColumn(SecRange range, String[] levelPrefix, List<String> levelNodes) {
        Map<SecRangeType, String> levelMap = parseLevels(range.getDsType(), levelPrefix);

        // env
        fetchRangeFillEnv(range, levelPrefix);

        // instance
        fetchRangeFillDs(range, levelPrefix);

        // catalog
        if (levelMap.containsKey(SecRangeType.Catalog)) {
            String catalog = levelMap.get(SecRangeType.Catalog);
            if (StringUtils.isBlank(catalog)) {
                range.setVerify(SecRangeVerify.Broken);
                range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            } else {
                range.setCatalog(new SecRangeItem(catalog, catalog));
            }
        }

        // schema
        if (levelMap.containsKey(SecRangeType.Schema)) {
            String schema = levelMap.get(SecRangeType.Schema);
            if (StringUtils.isBlank(schema)) {
                range.setVerify(SecRangeVerify.Broken);
                range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_SCHEMA_MESSAGE.name()));
            } else {
                range.setSchema(new SecRangeItem(schema, schema));
            }
        }

        // table
        String table = levelMap.get(SecRangeType.TableOrView);
        if (StringUtils.isBlank(table)) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_TABLEVIEW_MESSAGE.name()));
        } else {
            range.setTable(new SecRangeItem(table, table));
        }

        // levelNodes for Column
        if (levelNodes.isEmpty() && !range.isChooseAll()) {
            range.setVerify(SecRangeVerify.Broken);
            range.getVerifyMessage().add(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_COLUMN_MESSAGE.name()));
            return;
        }

        for (String levelNode : levelNodes) {
            range.getNodes().add(new SecRangeItem(levelNode, levelNode));
        }
    }

    public static String toLevelPrefixByEnv(SpecSaveRangeFO fo) {
        return "/";
    }

    public static String toLevelPrefixByInstance(SpecSaveRangeFO fo) {
        if (fo.getEnvId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        }

        return "/" + fo.getEnvId();
    }

    public static String toLevelPrefixByCatalog(SpecSaveRangeFO fo) {
        if (fo.getEnvId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        }
        if (fo.getDsId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        }

        DsCacheEntry dsCache = ownerCacheService.queryByDsId(fo.getDsId());
        if (dsCache == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        return "/" + fo.getEnvId() + "/" + fo.getDsId();
    }

    public static String toLevelPrefixBySchema(SpecSaveRangeFO fo) {
        if (fo.getEnvId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        }
        if (fo.getDsId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        }

        DsCacheEntry dsCache = ownerCacheService.queryByDsId(fo.getDsId());
        if (dsCache == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        DsBrowseSpi dsBrowseSpi = PluginManager.findDsBrowseSpi(dsCache.getDsType());
        if (dsBrowseSpi.getLevels().contains(UmiTypes.Catalog)) {
            return "/" + fo.getEnvId() + "/" + fo.getDsId() + "/" + fo.getCatalog();
        } else {
            return "/" + fo.getEnvId() + "/" + fo.getDsId();
        }
    }

    public static String toLevelPrefixByTable(SpecSaveRangeFO fo) {
        if (fo.getEnvId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        }
        if (fo.getDsId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        }

        DsCacheEntry dsCache = ownerCacheService.queryByDsId(fo.getDsId());
        if (dsCache == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        StringBuilder levelBuilder = new StringBuilder("/" + fo.getEnvId() + "/" + fo.getDsId());

        DsBrowseSpi dsBrowseSpi = PluginManager.findDsBrowseSpi(dsCache.getDsType());
        if (dsBrowseSpi.getLevels().contains(UmiTypes.Catalog)) {
            if (StringUtils.isBlank(fo.getCatalog())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            }
            levelBuilder.append("/" + fo.getCatalog());
        }

        if (dsBrowseSpi.getLevels().contains(UmiTypes.Schema)) {
            if (StringUtils.isBlank(fo.getSchema())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_SCHEMA_MESSAGE.name()));
            }
            levelBuilder.append("/" + fo.getSchema());
        }

        return levelBuilder.toString();
    }

    public static String toLevelPrefixByColumn(SpecSaveRangeFO fo) {
        if (fo.getEnvId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_ENV_MESSAGE.name()));
        }
        if (fo.getDsId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        }

        DsCacheEntry dsCache = ownerCacheService.queryByDsId(fo.getDsId());
        if (dsCache == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        StringBuilder levelBuilder = new StringBuilder("/" + fo.getEnvId() + "/" + fo.getDsId());

        DsBrowseSpi dsBrowseSpi = PluginManager.findDsBrowseSpi(dsCache.getDsType());
        if (dsBrowseSpi.getLevels().contains(UmiTypes.Catalog)) {
            if (StringUtils.isBlank(fo.getCatalog())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_CATALOG_MESSAGE.name()));
            }
            levelBuilder.append("/" + fo.getCatalog());
        }

        if (dsBrowseSpi.getLevels().contains(UmiTypes.Schema)) {
            if (StringUtils.isBlank(fo.getSchema())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_SCHEMA_MESSAGE.name()));
            }
            levelBuilder.append("/" + fo.getSchema());
        }

        if (StringUtils.isBlank(fo.getTable())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_TABLEVIEW_MESSAGE.name()));
        }

        levelBuilder.append("/" + fo.getTable());

        return levelBuilder.toString();
    }

    public static DataSourceType toReferDsType(SpecSaveRangeFO fo) {
        if (fo.getDsId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_VERIFY_MISSING_INSTANCE_MESSAGE.name()));
        }

        DsCacheEntry dsCache = ownerCacheService.queryByDsId(fo.getDsId());
        if (dsCache == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        return dsCache.getDsType();
    }
}
