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
package com.clougence.clouddm.console.web.util;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.OBJ_UI_TIPS;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.execute.ResultPageDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.component.detectrule.SecHintInfo;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRange;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRangeItem;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsDriverFamily;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckItemMO;
import com.clougence.clouddm.console.web.global.i18n.*;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseActionFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseConvertDDLFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseGenerateFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseRequestFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsListFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsQueryFO;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.audit.OperateUserVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.browse.cache.BrowseKeyVO;
import com.clougence.clouddm.console.web.model.vo.browse.rdb.*;
import com.clougence.clouddm.console.web.model.vo.checkrules.*;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DmSimpleDsVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsRuleEntity;
import com.clougence.clouddm.console.web.model.vo.faker.DmAsyncTaskVO;
import com.clougence.clouddm.console.web.model.vo.openapi.DmApiDataSourceVO;
import com.clougence.clouddm.console.web.model.vo.project.*;
import com.clougence.clouddm.console.web.model.vo.system.CloudOrIdcNameVO;
import com.clougence.clouddm.console.web.service.browse.model.ActionInfo;
import com.clougence.clouddm.console.web.service.browse.model.ActionTargetMO;
import com.clougence.clouddm.console.web.service.browse.model.GenerateSqlDataAuthEnum;
import com.clougence.clouddm.console.web.service.browse.model.rdb.*;
import com.clougence.clouddm.console.web.service.cluster.WorkerDetector;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.console.web.service.project.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleMO;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.secrule.*;
import com.clougence.clouddm.platform.dal.model.system.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.CheckerRange;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.dblink.DbLinkEditorFields;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;
import com.clougence.clouddm.sdk.ui.editor.job.JobEditorFields;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.role.RoleFields;
import com.clougence.clouddm.sdk.ui.editor.schedule.ScheduleJobFields;
import com.clougence.clouddm.sdk.ui.editor.sequence.SequenceFields;
import com.clougence.clouddm.sdk.ui.editor.synonym.SynonymFields;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorFields;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.editor.user.UserFields;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.drivers.DriverFamily;
import com.clougence.rdp.service.openapi.model.ApiDataSourceVO;
import com.clougence.rdp.service.openapi.model.ApiListDsFO;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.token.GenericTokenParser;
import com.clougence.utils.token.TokenHandlerHelper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author mode create time is 2021/1/30
 **/
public class DmConvertUtils {

    public static Map<TargetType, String> convertToResource(DsLevels dsLevels, String tableOrView) {
        Map<TargetType, String> result = new HashMap<>();

        result.put(TargetType.Environment, dsLevels.envId());
        result.put(TargetType.Instance, String.valueOf(dsLevels.dsDO().getId()));

        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();
        for (UmiTypes umiType : dsLevels.levelsDef()) {
            switch (umiType) {
                case Catalog:
                    result.put(TargetType.Catalog, (String) levelsParam.get(umiType));
                    break;
                case Schema:
                    result.put(TargetType.Schema, (String) levelsParam.get(umiType));
                    break;
            }
        }
        result.put(TargetType.Table, tableOrView);

        return result;
    }

    public static BrowseLevelsVO convertToBrowseLevelsVO(DmSysEnvDO dsEnvDO) {
        BrowseLevelsVO vo = new BrowseLevelsVO();
        vo.setObjId(String.valueOf(dsEnvDO.getId()));
        vo.setObjName(dsEnvDO.getEnvName());
        vo.setObjType(DsMenuType.Env.getTypeName());
        return vo;
    }

    public static BrowseLevelsVO convertToBrowseLevelsVO(DmDsDO dsDO, DataSourceConfig dsConfig, DmDsConfigDO dmDsConfig, RdbSupportSpi supportSpi, String dsHost) {
        BrowseLevelsVO vo = new BrowseLevelsVO();
        vo.setObjId(String.valueOf(dsDO.getId()));
        if (RdpConvertUtils.removeNoDescription(dsDO.getInstanceDesc()) == null) {
            vo.setObjName(dsDO.getInstanceId());
        } else {
            vo.setObjName(dsDO.getInstanceDesc());
        }
        vo.setObjType(DsMenuType.valueOfCode(UmiTypes.Instance.getTypeName()).getTypeName());
        vo.setObjAttr(new HashMap<>());
        vo.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
        vo.getObjAttr().put("dsName", dsDO.getDataSourceType().getTypeName());
        vo.getObjAttr().put("dsVersion", dsDO.getVersion());
        vo.getObjAttr().put("dsHost", dsHost);
        vo.getObjAttr().put("dsDeployType", dsDO.getDeployType().name());
        vo.getObjAttr().put("dsInstance", dsDO.getInstanceId());
        vo.getObjAttr().put("dsInstanceDesc", dsDO.getInstanceDesc());
        vo.getObjAttr().put("dsEnvId", dsDO.getDsEnvId());
        vo.getObjAttr().put("status", dmDsConfig.getStatus());
        vo.getObjAttr().put("msg", convertToDataSourceStatusI18n(dmDsConfig.getStatus(), dmDsConfig.getDataSourceType()));

        if (dsConfig != null) {
            I18nUtils dsI18n = PluginManager.findDsI18nUtil(dsConfig.getDataSourceType());
            SecurityType securityType = dsConfig.getSecurityType();
            securityType = (securityType == null) ? SecurityType.NONE : securityType;
            vo.getObjAttr().put("dsDriver", dsConfig.getDriverVersion());
            vo.getObjAttr().put("dsSecurityType", DmI18nUtils.getMessage(securityType.getI18nKey()));

            // support
            //            if (supportSpi != null) {
            //                Map<String, Object> supported = new LinkedHashMap<>();
            //                supported.put("changeCatalog", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_CATALOG, supportSpi.supportChangeCatalog(dsConfig), dsI18n));
            //                supported.put("changeSchema", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_SCHEMA, supportSpi.supportChangeSchema(dsConfig), dsI18n));
            //                supported.put("changeIsolation", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_ISOLATION, supportSpi.supportChangeIsolation(dsConfig), dsI18n));
            //                supported.put("changeAutoCommit", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_AUTO_COMMIT, supportSpi.supportChangeAutoCommit(dsConfig), dsI18n));
            //                supported.put("changeReadOnly", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_READONLY, supportSpi.supportChangeReadOnly(dsConfig), dsI18n));
            //                supported.put("cancelQuery", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CANCEL_QUERY, supportSpi.supportCancelQuery(dsConfig), dsI18n));
            //                vo.getObjAttr().put("support", supported);
            //            } else {
            //                Map<String, Object> supported = new LinkedHashMap<>();
            //                supported.put("changeCatalog", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_CATALOG, RdbSupportLevel.No, dsI18n));
            //                supported.put("changeSchema", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_SCHEMA, RdbSupportLevel.No, dsI18n));
            //                supported.put("changeIsolation", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_ISOLATION, RdbSupportLevel.No, dsI18n));
            //                supported.put("changeAutoCommit", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_AUTO_COMMIT, RdbSupportLevel.No, dsI18n));
            //                supported.put("changeReadOnly", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_READONLY, RdbSupportLevel.No, dsI18n));
            //                supported.put("cancelQuery", convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CANCEL_QUERY, RdbSupportLevel.No, dsI18n));
            //                vo.getObjAttr().put("support", supported);
            //            }

            //            // support default
            //            if (dsConfig instanceof RdbConfig) {
            //                RdbConfig rdbConfig = (RdbConfig) dsConfig;
            //                vo.getObjAttr().put("defaultCatalog", StringUtils.isBlank(rdbConfig.getDefaultDataBase()) ? "" : rdbConfig.getDefaultDataBase());
            //                vo.getObjAttr().put("defaultSchema", StringUtils.isBlank(rdbConfig.getDefaultSchema()) ? "" : rdbConfig.getDefaultSchema());
            //                vo.getObjAttr().put("defaultIsolation", RdbIsolation.valueOfCode(rdbConfig.getIsolation()).getName());
            //                vo.getObjAttr().put("defaultAutoCommit", rdbConfig.getAutoCommit() == null || rdbConfig.getAutoCommit());
            //                vo.getObjAttr().put("defaultReadOnly", Boolean.TRUE.equals(dsConfig.getReadOnly()));
            //            } else {
            //                vo.getObjAttr().put("defaultCatalog", "");
            //                vo.getObjAttr().put("defaultSchema", "");
            //                vo.getObjAttr().put("defaultIsolation", RdbIsolation.DEFAULT.getName());
            //                vo.getObjAttr().put("defaultAutoCommit", true);
            //                vo.getObjAttr().put("defaultReadOnly", Boolean.TRUE.equals(dsConfig.getReadOnly()));
            //            }
        }
        return vo;
    }

    public static BrowseLevelsVO convertToBrowseLevelsVO(DmDsDO dsDO) {
        BrowseLevelsVO vo = new BrowseLevelsVO();
        vo.setObjId(String.valueOf(dsDO.getId()));
        if (RdpConvertUtils.removeNoDescription(dsDO.getInstanceDesc()) == null) {
            vo.setObjName(dsDO.getInstanceId());
        } else {
            vo.setObjName(dsDO.getInstanceDesc());
        }
        vo.setObjType(DsMenuType.valueOfCode(UmiTypes.Instance.getTypeName()).getTypeName());
        vo.setObjAttr(new HashMap<>());
        vo.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
        vo.getObjAttr().put("dsName", dsDO.getDataSourceType().getTypeName());
        vo.getObjAttr().put("dsInstance", dsDO.getInstanceId());
        vo.getObjAttr().put("dsInstanceDesc", dsDO.getInstanceDesc());
        vo.getObjAttr().put("dsEnvId", dsDO.getDsEnvId());
        return vo;
    }

    public static String findObjId(DsElement dsObject) {
        if (dsObject.getObjId() != -1) {
            return String.valueOf(dsObject.getObjId());
        } else if (dsObject.getObjAttr().get(RdbAttributeNames.RDB_OBJ_ID.getCodeKey()) != null) {
            return (String) dsObject.getObjAttr().get(RdbAttributeNames.RDB_OBJ_ID.getCodeKey());
        } else {
            return "-1";
        }
    }

    public static BrowseLevelsVO convertToBrowseLevelsVO(DsElement dsObject) {
        if (dsObject == null) {
            return null;
        }
        BrowseLevelsVO vo = new BrowseLevelsVO();
        vo.setObjId(findObjId(dsObject));
        vo.setObjName(dsObject.getObjName());
        vo.setObjType(DsMenuType.valueOfCode(dsObject.getObjType().getTypeName()).getTypeName());
        vo.setObjAttr(dsObject.getObjAttr());
        return vo;
    }

    public static ActionInfo convertToActionInfo(DsLevels levels) {
        ActionInfo info = new ActionInfo();
        info.setEnvId(levels.envId());
        info.setOriLevels(levels.levels());
        info.setDbLevels(levels.dbLevels());
        info.setDsDO(levels.dsDO());
        info.setLevelsDef(levels.levelsDef());
        info.setLevelsParam(levels.levelsParam());
        return info;
    }

    private static <T> List<T> notNullList(List<T> list) {
        return (list == null || list.isEmpty()) ? Collections.emptyList() : list;
    }

    public static BrowseKeyMO convertToBrowseKeyMO(String key, String value) {
        BrowseKeyMO mo = new BrowseKeyMO();
        mo.setName(key);
        mo.setType(DsMenuType.Key.getTypeName());
        mo.setTips(key);
        mo.setValue(value);
        return mo;
    }

    public static BrowseTableMO convertToBrowseTableMO(RdbTable rdbTable) {
        RdbPrimaryKey primaryKey = rdbTable.getPrimaryKey();
        List<String> keyCols = (primaryKey == null) ? Collections.emptyList() : primaryKey.getColumnList();

        List<RdbUniqueKey> uniqueKeys = notNullList(rdbTable.getUniqueKeys());
        List<String> ukCols = uniqueKeys.stream().flatMap((Function<RdbUniqueKey, Stream<String>>) idx -> {
            return idx.getColumnList().stream();
        }).collect(Collectors.toList());

        List<RdbIndex> indices = notNullList(rdbTable.getIndices());
        List<String> idxCols = indices.stream().flatMap((Function<RdbIndex, Stream<String>>) idx -> {
            return idx.getColumnList().stream();
        }).collect(Collectors.toList());

        List<RdbForeignKey> foreignKeys = notNullList(rdbTable.getForeignKeys());
        List<String> fkCols = foreignKeys.stream().flatMap((Function<RdbForeignKey, Stream<String>>) idx -> {
            return idx.getColumnList().stream();
        }).collect(Collectors.toList());

        BrowseTableMO mo = new BrowseTableMO();
        mo.setObjId(rdbTable.getAttribute(RdbAttributeNames.RDB_OBJ_ID));
        mo.setName(rdbTable.getName());
        mo.setType(rdbTable.getTableType());
        mo.setTips(rdbTable.getComment());

        if (CollectionUtils.isEmpty(rdbTable.getColumns())) {
            mo.setColumns(Collections.emptyList());
        } else {
            mo.setColumns(rdbTable.getColumns().values().stream().sorted(Comparator.comparingInt(RdbColumn::getIndex)).map(col -> {
                return DmConvertUtils.convertToBrowseColumnMOTipsType(col, keyCols, idxCols, ukCols, fkCols);
            }).collect(Collectors.toList()));
        }

        if (primaryKey != null) {
            mo.setKeys(Collections.singletonList(DmConvertUtils.convertToBrowseKeyMO(primaryKey)));
        } else {
            mo.setKeys(Collections.emptyList());
        }

        List<BrowseIndexMO> indexes = new ArrayList<>();
        indexes.addAll(uniqueKeys.stream().map(DmConvertUtils::convertToBrowseIndexMO).collect(Collectors.toList()));
        indexes.addAll(indices.stream().map(DmConvertUtils::convertToBrowseIndexMO).collect(Collectors.toList()));
        mo.setIndexes(indexes);

        mo.setPartitions(Collections.emptyList());

        List<BrowseConstraintMO> constraints = new ArrayList<>();
        constraints
            .addAll(notNullList(rdbTable.getCheckConstraints()).stream().map(DmConvertUtils::convertToBrowseConstraintMO).filter(Objects::nonNull).collect(Collectors.toList()));
        constraints
            .addAll(notNullList(rdbTable.getUniqueConstraints()).stream().map(DmConvertUtils::convertToBrowseConstraintMO).filter(Objects::nonNull).collect(Collectors.toList()));
        mo.setConstraints(constraints);

        List<BrowseForeignKeyMO> fks = new ArrayList<>();
        fks.addAll(foreignKeys.stream().map(fk -> convertToBrowseForeignMO(fk, rdbTable.getName())).collect(Collectors.toList()));
        mo.setForeignKeys(fks);

        return mo;
    }

    private static BrowseColumnMO convertToBrowseColumnMOTipsType(RdbColumn rdbColumn, List<String> keyCols, List<String> idxCols, List<String> ukCols, List<String> fkCols) {
        String colName = rdbColumn.getName();
        FieldType sqlType = rdbColumn.getSqlType();

        BrowseColumnMO mo = new BrowseColumnMO();
        mo.setName(colName);
        mo.setDbType(sqlType == null ? "" : sqlType.getCodeKey());
        mo.setDataType(BrowseColumnType.DEFAULT);

        mo.setDbKey(keyCols.contains(colName));
        mo.setDbUnique(ukCols.contains(colName));
        mo.setDbForeign(fkCols.contains(colName));
        mo.setDbIndex(idxCols.contains(colName));

        if (sqlType != null) {
            if (sqlType.isArray()) {
                mo.setDataType(BrowseColumnType.ARRAY);
            } else if (sqlType.isStruct()) {
                mo.setDataType(BrowseColumnType.OBJECT);
            } else if (sqlType.isString()) {
                mo.setDataType(BrowseColumnType.TEXT);
            } else if (sqlType.isDataOrTime()) {
                mo.setDataType(BrowseColumnType.DATETIME);
            } else if (sqlType.isGeometry()) {
                mo.setDataType(BrowseColumnType.GEO);
            } else if (sqlType.isBinary()) {
                mo.setDataType(BrowseColumnType.BIN);
            } else if (sqlType.isBoolean()) {
                mo.setDataType(BrowseColumnType.BOOL);
            } else if (sqlType.isNumber()) {
                mo.setDataType(BrowseColumnType.NUM);
            }
        }

        // TODO the user's personalized configuration, currently fixed as columnType
        if (StringUtils.isNotBlank(mo.getDbType())) {
            mo.setTips("(" + mo.getDbType() + ")");
        }
        return mo;
    }

    public static BrowseProcedureMO convertToBrowseProcedureMo(RdbProcedure rdbProcedure) {
        BrowseProcedureMO mo = new BrowseProcedureMO();
        mo.setObjId(rdbProcedure.getAttribute(RdbAttributeNames.RDB_OBJ_ID));
        mo.setName(rdbProcedure.getName());
        mo.setType(rdbProcedure.getUmiType().getTypeName());
        mo.setParams(rdbProcedure.getRdbParams()
            .stream()
            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
            .map(DmConvertUtils::convertToBrowseParamMo)
            .collect(Collectors.toList()));

        return mo;
    }

    public static BrowseFunctionMO convertToBrowseFunctionMo(RdbFunction rdbFunction) {
        BrowseFunctionMO mo = new BrowseFunctionMO();
        mo.setObjId(rdbFunction.getAttribute(RdbAttributeNames.RDB_OBJ_ID));
        mo.setName(rdbFunction.getName());
        mo.setType(rdbFunction.getUmiType().getTypeName());
        mo.setParams(rdbFunction.getRdbParams()
            .stream()
            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
            .map(DmConvertUtils::convertToBrowseParamMo)
            .collect(Collectors.toList()));
        mo.setReturns(convertToBrowseParamMo(rdbFunction.getReturns()));
        return mo;
    }

    public static BrowseParamMO convertToBrowseParamMo(RdbParam rdbParam) {
        BrowseParamMO mo = new BrowseParamMO();
        mo.setName(rdbParam.getName());
        String dataType;
        if (!StringUtils.isNotBlank(rdbParam.getCharacterMaximumLength())) {
            dataType = rdbParam.getType();
        } else {
            dataType = rdbParam.getType() + "(" + rdbParam.getCharacterMaximumLength() + ")";
        }
        mo.setDataType(dataType);
        mo.setTips(rdbParam.getAttribute(OBJ_UI_TIPS));
        return mo;
    }

    public static BrowseColumnMO convertToBrowseColumnMOTipsType(RdbColumn rdbColumn) {
        String colName = rdbColumn.getName();

        BrowseColumnMO mo = new BrowseColumnMO();
        mo.setName(colName);
        mo.setDbType(rdbColumn.getSqlType().getCodeKey());

        mo.setDbKey(rdbColumn.hasConstraint(GeneralConstraintType.Primary));
        mo.setDbUnique(rdbColumn.hasConstraint(GeneralConstraintType.Unique));
        mo.setDbForeign(false);   // TODO use metadata
        mo.setDbIndex(false);     // TODO use metadata

        // TODO the user's personalized configuration, currently fixed as columnType
        mo.setTips(mo.getDbType());
        return mo;
    }

    public static BrowsePrimaryMO convertToBrowseKeyMO(RdbPrimaryKey primaryKey) {
        BrowsePrimaryMO mo = new BrowsePrimaryMO();
        mo.setName(primaryKey.getName());
        mo.setType(BrowseIndexType.Primary.name());
        mo.setTips("(" + StringUtils.join(primaryKey.getColumnList().toArray(), ",") + ")");

        mo.setColumns(primaryKey.getColumnList().stream().map(DmConvertUtils::convertToBrowseTermMO).collect(Collectors.toList()));
        return mo;
    }

    public static BrowseIndexMO convertToBrowseIndexMO(RdbUniqueKey uniqueKey) {
        BrowseIndexMO mo = new BrowseIndexMO();
        mo.setName(uniqueKey.getName());
        mo.setType(BrowseIndexType.Unique.name());
        if (CollectionUtils.isNotEmpty(uniqueKey.getColumnList())) {
            mo.setTips("(" + StringUtils.join(uniqueKey.getColumnList().toArray(), ",") + ") UNIQUE");
        }
        mo.setUnique(true);

        mo.setColumns(uniqueKey.getColumnList().stream().map(DmConvertUtils::convertToBrowseTermMO).collect(Collectors.toList()));
        return mo;
    }

    public static BrowseIndexMO convertToBrowseIndexMO(RdbIndex rdbIndex) {
        BrowseIndexMO mo = new BrowseIndexMO();
        mo.setName(rdbIndex.getName());

        if (rdbIndex.getType() == RdbIndexType.Unique) {
            mo.setType(BrowseIndexType.Unique.name());
            mo.setTips("(" + StringUtils.join(rdbIndex.getColumnList().toArray(), ",") + ") UNIQUE");
            mo.setUnique(true);
        } else if (rdbIndex.getType() == RdbIndexType.Normal) {
            mo.setType(BrowseIndexType.Normal.name());
            mo.setTips("(" + StringUtils.join(rdbIndex.getColumnList().toArray(), ",") + ")");
        } else {
            mo.setType(BrowseIndexType.Other.name());
            mo.setTips("(" + StringUtils.join(rdbIndex.getColumnList().toArray(), ",") + ")");
        }

        mo.setColumns(rdbIndex.getColumnList().stream().map(DmConvertUtils::convertToBrowseTermMO).collect(Collectors.toList()));
        return mo;
    }

    public static BrowseConstraintMO convertToBrowseConstraintMO(ConstraintObject constraintObject) {
        BrowseConstraintMO mo = new BrowseConstraintMO();
        mo.setName(constraintObject.getName());
        if (!constraintObject.getEnabled()) {
            mo.setEnabled(false);
        }
        if (constraintObject.getConstraintType() == GeneralConstraintType.Check) {
            mo.setType(BrowseConstraintType.CHECK);
        } else if (constraintObject.getConstraintType() == GeneralConstraintType.Unique) {
            mo.setType(BrowseConstraintType.UNIQUE);
        } else {
            return null;
        }

        return mo;
    }

    public static BrowseForeignKeyMO convertToBrowseForeignMO(RdbForeignKey rdbForeignKey, String tableName) {
        BrowseForeignKeyMO mo = new BrowseForeignKeyMO();
        mo.setName(rdbForeignKey.getName());
        //        mo.setForeign(true);

        Map<String, String> refMapping = rdbForeignKey.getReferenceMapping();
        //        List<String> leftColList = rdbForeignKey.getColumnList();
        //        List<String> rightColList = leftColList.stream().map(refMapping::get).collect(Collectors.toList());

        String leftPart = "(" + StringUtils.join(refMapping.keySet().toArray(), ",") + ")";
        String rightPart = "(" + StringUtils.join(refMapping.values().toArray(), ",") + ")";
        mo.setTips(leftPart + " → " + tableName + rightPart);

        List<BrowseTermMO> termMOS = new ArrayList<>();
        for (String col : rdbForeignKey.getColumnList()) {
            BrowseTermMO termMO = new BrowseTermMO();
            termMO.setName(col);
            termMO.setTips(col + " → " + refMapping.get(col));
            termMOS.add(termMO);
        }
        mo.setColumns(termMOS);
        return mo;
    }

    public static RdbTriggerMO convertToBrowseTriggerMo(RdbTrigger rdbTrigger) {
        RdbTriggerMO mo = new RdbTriggerMO();
        mo.setObjId(rdbTrigger.getAttribute(RdbAttributeNames.RDB_OBJ_ID));
        mo.setTriggerBody(rdbTrigger.getSql());
        // mo.setTriggerEvent(rdbTrigger.getTriggerEvent());
        mo.setName(rdbTrigger.getName());
        mo.setTriggerTable(rdbTrigger.getTriggerTableName());
        mo.setTriggerTime(rdbTrigger.getTriggerTime());
        return mo;
    }

    public static BrowseKeyMO convertToBrowseKeyMo(RdbValue value) {
        BrowseKeyMO mo = new BrowseKeyMO();
        mo.setObjId(value.getAttribute(RdbAttributeNames.RDB_OBJ_ID));
        mo.setName(value.getValue());
        mo.setTips(RdbAttributeNames.OBJ_UI_TIPS.getValue(value.getAttributes()));
        return mo;
    }

    public static BrowseTermMO convertToBrowseTermMO(String term) {
        BrowseTermMO mo = new BrowseTermMO();
        mo.setName(term);
        mo.setTips(term);
        return mo;
    }

    public static BrowseKeyVO convertToBrowseKeyVO(BrowseKeyMO mo) {
        if (mo == null) {
            return null;
        }

        BrowseKeyVO vo = new BrowseKeyVO();
        vo.setName(mo.getName());
        vo.setType(mo.getType());
        vo.setTips(mo.getTips());
        vo.setValue(mo.getValue());
        return vo;
    }

    public static BrowseObjectVO convertToBrowseObjectVO(BrowseObjectMO mo) {
        if (mo instanceof BrowseTableMO) {
            return convertToBrowseObjectVO((BrowseTableMO) mo);
        } else if (mo instanceof BrowseFunctionMO) {
            return convertToBrowseObjectVO((BrowseFunctionMO) mo);
        } else if (mo instanceof BrowseProcedureMO) {
            return convertToBrowseObjectVO((BrowseProcedureMO) mo);
        } else if (mo instanceof RdbTriggerMO) {
            return convertToBrowseObjectVO((RdbTriggerMO) mo);
        } else {
            return null;
        }
    }

    private static BrowseObjectVO convertToBrowseObjectVO(BrowseTableMO mo) {
        if (mo == null) {
            return null;
        }

        List<String> allIdx = new ArrayList<>();
        allIdx.addAll(mo.getKeys().stream().flatMap((Function<BrowsePrimaryMO, Stream<String>>) m -> {
            return m.getColumns().stream().map(BrowseTermMO::getName);
        }).collect(Collectors.toList()));
        allIdx.addAll(mo.getIndexes().stream().flatMap((Function<BrowseIndexMO, Stream<String>>) m -> {
            return m.getColumns().stream().map(BrowseTermMO::getName);
        }).collect(Collectors.toList()));

        BrowseObjectVO vo = new BrowseObjectVO();
        vo.setObjId(StringUtils.isBlank(mo.getObjId()) ? mo.getName() : mo.getObjId());
        vo.setName(mo.getName());
        vo.setType(mo.getType());
        vo.setTips(mo.getTips());
        vo.setGroup(new ArrayList<>());

        // Browse TableInfo Column Group
        if (CollectionUtils.isNotEmpty(mo.getColumns())) {
            BrowseGroupVO columnGroup = new BrowseGroupVO();
            columnGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_COLUMN_GROUP));
            columnGroup.setType(DsMenuType.RdbColumnGroup.getTypeName());
            columnGroup.setItems(new ArrayList<>());
            for (BrowseColumnMO columnMO : mo.getColumns()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(columnMO.getName());
                itemVO.setType(DsMenuType.RdbColumn.getTypeName());
                itemVO.setDbType(columnMO.getDbType());
                itemVO.setTips(columnMO.getTips());

                itemVO.setAttrs(new HashMap<>());
                itemVO.getAttrs().put("isKey", columnMO.isDbKey());
                itemVO.getAttrs().put("isUnique", columnMO.isDbUnique());
                itemVO.getAttrs().put("isIndex", columnMO.isDbIndex());
                itemVO.getAttrs().put("isForeign", columnMO.isDbForeign());
                if (columnMO.isDbKey()) {
                    itemVO.setIcon("COLUMN-PK");
                } else if (columnMO.isDbUnique()) {
                    itemVO.setIcon("COLUMN-UK");
                } else if (columnMO.isDbForeign()) {
                    itemVO.setIcon("COLUMN-FK");
                } else {
                    String endTag = columnMO.isDbIndex() ? "-IDX" : "";
                    switch (columnMO.getDataType()) {
                        case DATETIME:
                            itemVO.setIcon("COLUMN-DAT" + endTag);
                            break;
                        case TEXT:
                            itemVO.setIcon("COLUMN-TXT" + endTag);
                            break;
                        case BIN:
                            itemVO.setIcon("COLUMN-BIN" + endTag);
                            break;
                        case GEO:
                            itemVO.setIcon("COLUMN-GEO" + endTag);
                            break;
                        case NUM:
                            itemVO.setIcon("COLUMN-NUM" + endTag);
                            break;
                        case BOOL:
                            itemVO.setIcon("COLUMN-BOOL" + endTag);
                            break;
                        case ARRAY:
                            itemVO.setIcon("COLUMN-ARRAY" + endTag);
                            break;
                        case OBJECT:
                            itemVO.setIcon("COLUMN-OBJECT" + endTag);
                            break;
                        case DEFAULT:
                        default:
                            itemVO.setIcon("COLUMN-DEFAULT" + endTag);
                            break;
                    }
                }

                columnGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(columnGroup);
        }

        // Browse TableInfo Key Group
        if (CollectionUtils.isNotEmpty(mo.getKeys())) {
            BrowseGroupVO keyGroup = new BrowseGroupVO();
            keyGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_PRIMARY_GROUP));
            keyGroup.setType(DsMenuType.RdbPrimaryGroup.getTypeName());
            keyGroup.setItems(new ArrayList<>());
            for (BrowsePrimaryMO keyMO : mo.getKeys()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(keyMO.getName());
                itemVO.setType(DsMenuType.RdbPrimary.getTypeName());
                itemVO.setDbType(keyMO.getType());
                itemVO.setTips(keyMO.getTips());
                keyGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(keyGroup);
        }

        // Browse TableInfo Indexes Group
        if (CollectionUtils.isNotEmpty(mo.getIndexes())) {
            BrowseGroupVO indexesGroup = new BrowseGroupVO();
            indexesGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_INDEX_GROUP));
            indexesGroup.setType(DsMenuType.RdbIndexGroup.getTypeName());
            indexesGroup.setItems(new ArrayList<>());
            for (BrowseIndexMO indexMO : mo.getIndexes()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(indexMO.getName());
                itemVO.setType(DsMenuType.RdbIndex.getTypeName());
                if (indexMO.isUnique()) {
                    itemVO.setIcon(DsMenuType.RdbIndex.getTypeName() + "-UK2");
                } else if (indexMO.isForeign()) {
                    itemVO.setIcon(DsMenuType.RdbIndex.getTypeName() + "-FK2");
                } else {
                    itemVO.setIcon(DsMenuType.RdbIndex.getTypeName() + "2");
                }
                itemVO.setDbType(indexMO.getType());
                itemVO.setTips(indexMO.getTips());
                indexesGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(indexesGroup);
        }

        // Browse TableInfo Partition Group
        if (CollectionUtils.isNotEmpty(mo.getPartitions())) {
            BrowseGroupVO partitionGroup = new BrowseGroupVO();
            partitionGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_PARTITION_GROUP));
            partitionGroup.setType(DsMenuType.RdbPartitionGroup.getTypeName());
            partitionGroup.setItems(new ArrayList<>());
            for (BrowsePartitionMO partitionMO : mo.getPartitions()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(partitionMO.getName());
                itemVO.setType(DsMenuType.RdbPartition.getTypeName());
                itemVO.setDbType(partitionMO.getType());
                itemVO.setTips(partitionMO.getTips());
                partitionGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(partitionGroup);
        }

        // foreign
        if (CollectionUtils.isNotEmpty(mo.getForeignKeys())) {
            BrowseGroupVO foreignKeyGroup = new BrowseGroupVO();
            foreignKeyGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_FOREIGN_KEY_GROUP));
            foreignKeyGroup.setType(DsMenuType.RdbForeignGroup.getTypeName());
            foreignKeyGroup.setItems(new ArrayList<>());
            for (BrowseForeignKeyMO foreignKey : mo.getForeignKeys()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(foreignKey.getName());
                itemVO.setType(DsMenuType.RdbForeign.getTypeName());
                itemVO.setTips(foreignKey.getTips());
                StringBuilder icon = new StringBuilder(DsMenuType.RdbForeign.getTypeName());
                icon.append("-FOREIGN");

                if (!foreignKey.isEnabled()) {
                    icon.append("-DISABLE");
                }
                itemVO.setIcon(icon.toString());
                foreignKeyGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(foreignKeyGroup);
        }

        // constraints
        if (CollectionUtils.isNotEmpty(mo.getConstraints())) {
            BrowseGroupVO constraintGroup = new BrowseGroupVO();
            constraintGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_CONSTRAINT_GROUP));
            constraintGroup.setType(DsMenuType.RdbConstraintGroup.getTypeName());
            constraintGroup.setItems(new ArrayList<>());
            for (BrowseConstraintMO constraint : mo.getConstraints()) {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(constraint.getName());
                itemVO.setType(DsMenuType.RdbConstraint.getTypeName());
                itemVO.setDbType(constraint.getType().name());
                itemVO.setTips(constraint.getTips());
                StringBuilder icon = new StringBuilder(DsMenuType.RdbConstraint.getTypeName());
                switch (constraint.getType()) {
                    case CHECK: {
                        icon.append("-CHECK");
                    }
                    case UNIQUE: {
                        icon.append("-UNIQUE");
                    }
                }
                if (!constraint.isEnabled()) {
                    icon.append("-DISABLE");
                }
                itemVO.setIcon(icon.toString());
                constraintGroup.getItems().add(itemVO);
            }
            vo.getGroup().add(constraintGroup);
        }

        return vo;
    }

    private static BrowseObjectVO convertToBrowseObjectVO(BrowseProcedureMO mo) {
        BrowseObjectVO vo = new BrowseObjectVO();
        vo.setObjId(StringUtils.isBlank(mo.getObjId()) ? mo.getName() : mo.getObjId());
        vo.setName(mo.getName());
        vo.setType(mo.getType());
        vo.setTips(mo.getTips());
        BrowseGroupVO browseGroupVO = new BrowseGroupVO();
        browseGroupVO.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_PARAM_GROUP));
        browseGroupVO.setType(DsMenuType.RdbParamGroup.getTypeName());

        if (CollectionUtils.isNotEmpty(mo.getParams())) {
            List<BrowseItemVO> items = mo.getParams().stream().map(param -> {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(param.getName());
                itemVO.setDbType(param.getDataType());
                itemVO.setType(DsMenuType.RdbParam.getTypeName());
                if (StringUtils.isNotBlank(param.getTips())) {
                    itemVO.setTips(param.getTips());
                } else {
                    itemVO.setTips("(" + param.getDataType() + ")");
                }
                itemVO.setIcon("PARAM");
                return itemVO;
            }).collect(Collectors.toList());
            browseGroupVO.setItems(items);
            vo.setGroup(Collections.singletonList(browseGroupVO));
        }

        return vo;
    }

    private static BrowseObjectVO convertToBrowseObjectVO(BrowseFunctionMO mo) {
        BrowseObjectVO vo = new BrowseObjectVO();
        vo.setObjId(StringUtils.isBlank(mo.getObjId()) ? mo.getName() : mo.getObjId());
        vo.setName(mo.getName());
        vo.setType(mo.getType());
        vo.setTips(mo.getTips());
        vo.setGroup(new ArrayList<>());

        if (CollectionUtils.isNotEmpty(mo.getParams())) {
            BrowseGroupVO paramGroup = new BrowseGroupVO();
            paramGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_PARAM_GROUP));
            paramGroup.setType(DsMenuType.RdbParamGroup.getTypeName());
            List<BrowseItemVO> paramItem = mo.getParams().stream().map(param -> {
                BrowseItemVO itemVO = new BrowseItemVO();
                itemVO.setName(param.getName());
                itemVO.setDbType(param.getDataType());
                itemVO.setType(DsMenuType.RdbParam.getTypeName());
                if (StringUtils.isNotBlank(param.getTips())) {
                    itemVO.setTips(param.getTips());
                } else {
                    itemVO.setTips("(" + param.getDataType() + ")");
                }
                itemVO.setIcon("PARAM");
                return itemVO;
            }).collect(Collectors.toList());
            paramGroup.setItems(paramItem);
            vo.getGroup().add(paramGroup);
        }

        if (mo.getReturns() != null) {
            BrowseGroupVO returnGroup = new BrowseGroupVO();
            returnGroup.setName(DmI18nUtils.getMessage(UiMenus18nKey.UI_LEAF_TITLE_RDB_RETURNS));
            returnGroup.setType(DsMenuType.RdbReturns.getTypeName());
            BrowseItemVO returnItem = new BrowseItemVO();
            returnItem.setName("return");
            returnItem.setIcon("RETURNS");
            returnItem.setDbType(mo.getReturns().getDataType());
            if (StringUtils.isNotBlank(mo.getReturns().getTips())) {
                returnItem.setTips(mo.getReturns().getTips());
            } else {
                returnItem.setTips("(" + mo.getReturns().getDataType() + ")");
            }
            returnGroup.setItems(Collections.singletonList(returnItem));
            vo.getGroup().add(returnGroup);
        }

        return vo;
    }

    private static BrowseObjectVO convertToBrowseObjectVO(RdbTriggerMO mo) {
        BrowseTriggerVO vo = new BrowseTriggerVO();
        vo.setObjId(StringUtils.isBlank(mo.getObjId()) ? mo.getName() : mo.getObjId());
        vo.setSql(mo.getTriggerBody());
        vo.setTriggerEvent(mo.getTriggerEvent());
        vo.setName(mo.getTriggerName());
        vo.setTriggerTable(mo.getTriggerTable());
        vo.setTriggerTime(mo.getTriggerTime());
        vo.setTips(mo.getTips());
        return vo;
    }

    public static ClusterVO convertToClusterVO(DmSysClusterDO clusterDO) {
        ClusterVO vo = new ClusterVO();
        vo.setId(clusterDO.getId());
        vo.setGmtCreate(clusterDO.getGmtCreate());
        vo.setGmtModified(clusterDO.getGmtModified());
        vo.setClusterName(clusterDO.getClusterName());
        vo.setRegion(clusterDO.getRegion());
        vo.setCloudOrIdcName(clusterDO.getCloudOrIdcName());
        vo.setClusterDesc(clusterDO.getClusterDesc());
        return vo;
    }

    public static WorkerVO convertToWorkerVO(DmSysWorkerDO workerDO, WorkerDetector workerDetector) {
        WorkerVO vo = new WorkerVO();
        vo.setCloudOrIdcName(workerDO.getCloudOrIdcName());
        vo.setClusterId(workerDO.getClusterId());
        vo.setCpuUseRatio(workerDO.getCpuUseRatio());
        vo.setGmtCreate(workerDO.getGmtCreate());
        vo.setGmtModified(workerDO.getGmtModified());
        vo.setId(workerDO.getId());
        vo.setMemUseRatio(workerDO.getMemUseRatio());
        vo.setRegion(workerDO.getRegion());
        vo.setPrivateIp(workerDO.getWorkerIp());
        vo.setWorkerLoad(workerDO.getWorkerLoad());
        vo.setWorkerState(workerDO.getWorkerState());
        vo.setWorkerName(workerDO.getWorkerName());
        vo.setWorkerSeqNumber(workerDO.getWorkerSeqNumber());
        vo.setSessionPoolUse(workerDO.getSessionPoolUse());
        vo.setSessionPoolMax(workerDO.getSessionPoolMax());

        vo.setWorkerDesc(workerDO.getWorkerDesc());
        vo.setDeployStatus(workerDO.getDeployStatus());
        vo.setPublicIp(workerDO.getExternalIp());
        vo.setInstallOrUpgradeDate(workerDO.getInstallOrUpgradeDate());
        vo.setInstallOrUpgradeVersion(workerDO.getInstallOrUpgradeVersion());

        vo.setHealthLevel(workerDetector.getHealthLevel(workerDO));
        return vo;
    }

    public static CloudOrIdcNameVO convertToCloudOrIdcNameVO(CloudOrIdcName cloudOrIdcName, CloudOrIdcName defaultOption) {
        CloudOrIdcNameVO vo = new CloudOrIdcNameVO();
        vo.setCloudOrIdcName(cloudOrIdcName);
        vo.setDefaultCheck(cloudOrIdcName == defaultOption);
        vo.setI18nName(DmI18nUtils.getMessage(cloudOrIdcName.name()));
        return vo;
    }

    public static DmSimpleDsVO convertToDmSimpleDsVO(DmDsDO dsDO, Map<Long, DmDsConfigDO> confMap) {
        DmSimpleDsVO vo = new DmSimpleDsVO();
        vo.setId(dsDO.getId());
        vo.setGmtCreate(dsDO.getGmtCreate());
        vo.setHost(dsDO.getHost());
        vo.setPrivateHost(dsDO.getPrivateHost());
        vo.setPublicHost(dsDO.getPublicHost());
        vo.setHostType(dsDO.getHostType());
        vo.setAccountName(dsDO.getAccount());
        vo.setLifeCycleState(dsDO.getLifeCycleState());
        vo.setSecurityType(dsDO.getSecurityType());
        vo.setDsEnvId(dsDO.getDsEnvId());
        if (dsDO.getDsEnvDO() != null) {
            vo.setDsEnvName(dsDO.getDsEnvDO().getEnvName());
        }
        vo.setInstanceId(dsDO.getInstanceId());
        vo.setInstanceDesc(dsDO.getInstanceDesc());
        vo.setDataSourceType(dsDO.getDataSourceType());

        if (dsDO.getDeployType() != null) {
            vo.setDeployType(dsDO.getDeployType());
            vo.setDeployTypeI18n(DmI18nUtils.getMessage(dsDO.getDeployType().name()));
        }

        if (confMap.containsKey(dsDO.getId())) {
            vo.setEnableQuery(true);
            vo.setEnableDevops(confMap.get(dsDO.getId()).isEnableDevops());
        } else {
            vo.setEnableQuery(false);
            vo.setEnableDevops(false);
        }

        vo.setVersion(dsDO.getVersion());
        return vo;
    }

    public static DmDsConfigKv4DmDO convertToDmDsKvBaseConfigDOForInsert(DmDsConfigKv4RdpDO config) {
        DmDsConfigKv4DmDO conf = new DmDsConfigKv4DmDO();
        conf.setDataSourceId(config.getDataSourceId());
        conf.setConfigName(config.getConfigName());
        conf.setConfigGroup(config.getConfigGroup());
        conf.setDisplay(config.isDisplay());
        conf.setDescKey(config.getDescKey());
        conf.setValueRequire(config.isValueRequire());
        conf.setValueValidRegex(config.getValueValidRegex());
        conf.setConfigValue(config.getConfigValue());
        conf.setDefaultValue(config.getDefaultValue());
        conf.setValueAdvance(config.getValueAdvance());
        conf.setReadOnly(config.isReadOnly());
        conf.setSecret(config.isSecret());
        return conf;
    }

    public static DsKvConfigVO convertToDsKvConfigVO(DmDsConfigKv4RdpDO config) {
        DsKvConfigVO vo = new DsKvConfigVO();

        vo.setId(config.getId());
        vo.setConfigName(config.getConfigName());
        if (!config.isSecret()) {
            vo.setConfigValue(config.getConfigValue());
        }
        vo.setConfigGroup(config.getConfigGroup());
        vo.setSecret(config.isSecret());
        vo.setDescription(DmI18nUtils.getMessage(config.getDescKey()));
        vo.setValueRequire(config.isValueRequire());
        vo.setValueValidRegex(config.getValueValidRegex());
        vo.setDefaultValue(config.getDefaultValue());
        vo.setValueAdvance(config.getValueAdvance());
        vo.setConfValType(config.getConfValType());
        vo.setReadOnly(config.isReadOnly());
        return vo;
    }

    public static DsKvConfigVO convertToDsKvConfigVO(DmDsConfigKv4DmDO config) {
        DsKvConfigVO vo = new DsKvConfigVO();

        vo.setId(config.getId());
        vo.setConfigName(config.getConfigName());
        if (!config.isSecret()) {
            vo.setConfigValue(config.getConfigValue());
        }
        vo.setConfigGroup(config.getConfigGroup());
        vo.setSecret(config.isSecret());
        vo.setDescription(DmI18nUtils.getMessage(config.getDescKey()));
        vo.setValueRequire(config.isValueRequire());
        vo.setValueValidRegex(config.getValueValidRegex());
        vo.setDefaultValue(config.getDefaultValue());
        vo.setValueAdvance(config.getValueAdvance());
        vo.setReadOnly(config.isReadOnly());
        return vo;
    }

    public static ActionTargetMO convertToActionTargetMO(BrowseActionFO fo) {
        ActionTargetMO mo = new ActionTargetMO();

        mo.setActionType(GenerateSqlDataAuthEnum.valueOfCode(fo.getActionType()));
        mo.setTargetName(fo.getTargetName());
        mo.setTargetType(fo.getTargetType());
        mo.setTargetNewName(fo.getTargetNewName());
        mo.setOptions(fo.getOptions());
        mo.setTargetExactName(fo.getTargetExactName());
        return mo;
    }

    public static ActionTargetMO convertToActionTargetMO(BrowseRequestFO fo) {
        ActionTargetMO mo = new ActionTargetMO();

        mo.setTargetName(fo.getTargetName());
        mo.setTargetType(fo.getTargetType());

        return mo;
    }

    public static ActionTargetMO convertToActionTargetMO(BrowseGenerateFO fo) {
        ActionTargetMO mo = new ActionTargetMO();

        mo.setTargetName(fo.getTargetName());
        mo.setTargetType(fo.getTargetType());
        mo.setOptions(fo.getOptions());

        return mo;
    }

    public static ActionTargetMO convertToActionTargetMO(BrowseConvertDDLFO fo) {
        ActionTargetMO mo = new ActionTargetMO();

        mo.setTargetName(fo.getSourceTableName());
        mo.setTargetType(fo.getLeafType());
        mo.setOptions(fo.getOptions());

        return mo;
    }

    public static Map<String, Object> convertToBrowseTriggerVO(RdbTrigger value) {
        Map<String, Object> map = new HashMap<>();

        map.put(TriggerEditorFields.TRIGGER_BODY, value.getSql());
        map.put(TriggerEditorFields.TRIGGER_EVENT, value.getTriggerEvent());
        map.put(TriggerEditorFields.TRIGGER_NAME, value.getName());
        map.put(TriggerEditorFields.TRIGGER_TABLE, value.getTriggerTableName());
        map.put(TriggerEditorFields.TRIGGER_TIME, value.getTriggerTime().toLowerCase());
        map.put(TriggerEditorFields.TRIGGER_COLUMNS, value.getTriggerTableColumns());
        if (value.getFeatures() != null) {
            map.putAll(value.getFeatures());
        }
        return map;
    }

    public static Map<String, Object> convertToBrowseViewVO(RdbView value) {
        Map<String, Object> map = new HashMap<>();
        map.put(ViewEditorFields.VIEW_NAME, value.getName());
        map.put(ViewEditorFields.SQL, value.getSql());
        map.put(ViewEditorFields.COMMENT, value.getComment());
        if (value.getFeatures() != null) {
            map.putAll(value.getFeatures());
        }
        return map;
    }

    public static Map<String, Object> convertToBrowseFunctionVO(RdbFunction value) {
        Map<String, Object> map = new HashMap<>();
        map.put(FunctionEditorFields.FUNCTION_NAME, value.getName());
        map.put(FunctionEditorFields.SQL, value.getSql());

        if (value.getFeatures() != null) {
            map.putAll(value.getFeatures());
        }

        RdbParam returnValue = value.getReturns();
        map.put(FunctionEditorFields.RETURN_TYPE, returnValue.getType().toUpperCase());
        map.put(FunctionEditorFields.PARAM_NUM_PRECISION, returnValue.getNumericPrecision());
        map.put(FunctionEditorFields.PARAM_DATE_PRECISION, returnValue.getDatetimePrecision());
        map.put(FunctionEditorFields.PARAM_NUM_SCALE, returnValue.getNumericScale());
        map.put(FunctionEditorFields.PARAM_LENGTH, returnValue.getLength());

        if (returnValue.getMode() != null) {
            map.put(FunctionEditorFields.PARAM_MODE, returnValue.getMode().getModeName());
        }

        List<BrowseParamVO> params = new ArrayList<>();
        for (RdbParam rdbParam : value.getRdbParams()) {
            BrowseParamVO param = new BrowseParamVO();
            param.setParamType(rdbParam.getType().toUpperCase());
            param.setLength(rdbParam.getLength());
            if (rdbParam.getMode() != null) {
                param.setMode(rdbParam.getMode().toString());
            }
            param.setNumericPrecision(rdbParam.getNumericPrecision());
            param.setDatetimePrecision(rdbParam.getDatetimePrecision());
            param.setNumericScale(rdbParam.getNumericScale());
            param.setName(rdbParam.getName());
            params.add(param);
        }

        map.put(FunctionEditorFields.FUNCTION_PARAMS, params);
        return map;
    }

    public static Map<String, Object> convertToBrowseProcedureVO(RdbProcedure value) {
        Map<String, Object> map = new HashMap<>();
        map.put(ProcedureEditorFields.PROCEDURE_NAME, value.getName());
        map.put(ProcedureEditorFields.SQL, value.getSql());

        if (value.getFeatures() != null) {
            map.putAll(value.getFeatures());
        }
        List<BrowseParamVO> params = new ArrayList<>();

        for (RdbParam rdbParam : value.getRdbParams()) {
            BrowseParamVO param = new BrowseParamVO();
            param.setParamType(rdbParam.getType().toUpperCase());
            param.setLength(rdbParam.getLength());
            param.setMode(rdbParam.getMode().toString());
            param.setNumericScale(rdbParam.getNumericScale());
            param.setDatetimePrecision(rdbParam.getDatetimePrecision());
            param.setNumericPrecision(rdbParam.getNumericPrecision());
            param.setLength(rdbParam.getLength());
            param.setName(rdbParam.getName());
            params.add(param);
        }
        map.put(ProcedureEditorFields.PROCEDURE_PARAMS, params);
        return map;
    }

    public static DmAsyncTaskVO convertToDmAsyncTaskVO(DmExecAsyncTaskDO taskDO) {
        DmAsyncTaskVO vo = new DmAsyncTaskVO();
        vo.setId(taskDO.getId());
        vo.setTitle(taskDO.getTitle());
        vo.setDescription(taskDO.getDescription());
        vo.setBizId(taskDO.getBizId());
        vo.setBizType(taskDO.getBizType());
        vo.setProcessType(taskDO.getProcessType());
        vo.setProcessValue(taskDO.getProcessValue());
        vo.setStatus(taskDO.getStatus());
        vo.setStatusMsg(taskDO.getStatusMsg());
        vo.setTimeOfStart(taskDO.getTimeOfStart());
        vo.setTimeOfLast(taskDO.getTimeOfLast());
        vo.setTimeOfFinish(taskDO.getTimeOfFinish());
        return vo;
    }

    public static SpecVO convertToDmSecSpecVO(DmSecSpecDO specDO) {
        SpecVO vo = new SpecVO();
        vo.setSpecId(specDO.getId());
        vo.setLastModified(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(specDO.getGmtModified()));
        vo.setName(specDO.getName());
        vo.setDescription(specDO.getDescription());
        vo.setEnable(specDO.isEnable());
        return vo;
    }

    public static String tryRuleI18nMessage(String key) {
        if (key != null && key.startsWith("i18n::")) {
            return DmI18nUtils.getMessage(key.substring("i18n::".length()));
        } else {
            return key;
        }
    }

    public static String resolveMessageArgs(String msg, Map<String, String> varMap) {
        return new GenericTokenParser(new TokenHandlerHelper("#{", "}", content -> {
            String varKey = content;
            String varDefault = "";
            int defaultIndexOf = content.indexOf(":");
            if (defaultIndexOf != -1) {
                varDefault = content.substring(defaultIndexOf + 1);
                varKey = content.substring(0, defaultIndexOf);
            }

            String var = varMap.get(varKey);
            if (StringUtils.isBlank(var) && StringUtils.isNotBlank(varDefault)) {
                var = varDefault;
            }

            if (varKey.equalsIgnoreCase(var)) {
                return varKey;
            } else {
                return var;
            }
        })).parse(msg);
    }

    public static SpecRulesVO convertToDmSecRulesVO(DmSecRuleMO defDO, Map<Long, DmSecRefererDO> refererMap, DmSecSpecDO specDO) {
        // basic
        SpecRulesVO vo = new SpecRulesVO();
        vo.setRuleId(defDO.getId());
        vo.setRuleType(defDO.getScriptType());
        vo.setRuleKind(defDO.getRuleKind());
        vo.setRuleContent(defDO.getScriptContent());
        vo.setDeprecated(defDO.isDeprecated());

        vo.setParameterDef(jsonConvertToSecRuleParamVO(defDO.getScriptDef()));
        vo.setParameterValue(new HashMap<>());
        vo.setLock((specDO != null && !specDO.isEnable()) || defDO.isInnerShare());

        // warnLevel, parameterValue, enable
        DmSecRefererDO refererDO = refererMap.get(defDO.getId());
        if (refererDO != null) {
            vo.setRefId(refererDO.getId());
            Map<String, String> curParams = refererDO.getRuleParam();
            for (SecParam param : vo.getParameterDef()) {
                String key = param.getName();
                vo.getParameterValue().put(key, curParams.getOrDefault(key, param.getDefaultValue()));
            }
            vo.setRuleChange(!StringUtils.equalsIgnoreCase(refererDO.getRefMD5(), defDO.getScriptMD5()));
            vo.setEnable(refererDO.isEnable());
        } else {
            for (SecParam param : vo.getParameterDef()) {
                vo.getParameterValue().put(param.getName(), param.getDefaultValue());
            }
            vo.setRuleChange(false);
            vo.setEnable(false);
        }

        // 
        vo.setRuleName(tryRuleI18nMessage(defDO.getName()));
        vo.setRuleDesc(resolveMessageArgs(tryRuleI18nMessage(defDO.getDescription()), vo.getParameterValue()));

        // for QUERY or SENSITIVE
        if (defDO.getRuleKind() == RuleKind.QUERY) {
            vo.setDsRange(defDO.getRuleDO().getRuleDsRange());
            vo.setTargetType(defDO.getRuleDO().getRuleTarget());
            if (vo.getTargetType() != null) {
                vo.setTargetTypeI18n(DmI18nUtils.getMessage(vo.getTargetType().getI18nKey()));
            } else {
                vo.setTargetTypeI18n(DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_ALL.name()));
            }

            if (refererDO != null) {
                vo.setWarnLevel(refererDO.getWarnLevel());
            } else {
                vo.setWarnLevel(WarnLevel.SUGGEST);
            }
        } else if (defDO.getRuleKind() == RuleKind.SENSITIVE) {
            if (refererDO != null) {
                vo.setSenMode(refererDO.getSenMode());
            } else {
                vo.setSenMode(defDO.getSenDO().getSenMode());
            }

            vo.setSenModeI18n(DmI18nUtils.getMessage(vo.getSenMode().getI18nKey()));
        }

        return vo;
    }

    public static List<SecParam> jsonConvertToSecRuleParamVO(String jsonStr) {
        return JsonUtils.toList(jsonStr, new TypeReference<List<SecParam>>() {});
    }

    public static RuleVO convertToDmSecRulesVO(DmSecRuleMO defDO, boolean skipContent) {
        RuleVO vo = new RuleVO();

        vo.setRuleId(defDO.getId());
        //vo.setRuleName(tryRuleI18nMessage(defDO.getName()));
        //vo.setRuleDesc(tryRuleI18nMessage(defDO.getDescription()));
        vo.setRuleType(defDO.getScriptType());
        vo.setRuleKind(defDO.getRuleKind());
        vo.setRuleParameter(jsonConvertToSecRuleParamVO(defDO.getScriptDef()));
        vo.setRuleContent(skipContent ? null : defDO.getScriptContent());
        vo.setInner(defDO.isInnerShare());
        vo.setDeprecated(defDO.isDeprecated());

        //
        Map<String, String> params = new HashMap<>();
        for (SecParam param : vo.getRuleParameter()) {
            String key = param.getName();
            params.put(key, param.getDefaultValue());
        }
        vo.setRuleName(tryRuleI18nMessage(defDO.getName()));
        vo.setRuleDesc(resolveMessageArgs(tryRuleI18nMessage(defDO.getDescription()), params));

        //
        if (defDO.getRuleKind() == RuleKind.QUERY) {
            vo.setDsRange(dsSort(defDO.getRuleDO().getRuleDsRange()));
            vo.setTargetType(defDO.getRuleDO().getRuleTarget());
            if (vo.getTargetType() != null) {
                vo.setTargetTypeI18n(DmI18nUtils.getMessage(vo.getTargetType().getI18nKey()));
            } else {
                vo.setTargetTypeI18n(DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_ALL.name()));
            }
        } else if (defDO.getRuleKind() == RuleKind.SENSITIVE) {
            vo.setSenMode(defDO.getSenDO().getSenMode());
            vo.setSenModeI18n(DmI18nUtils.getMessage(vo.getSenMode().getI18nKey()));
        }
        return vo;
    }

    public static List<DataSourceType> dsSort(List<DataSourceType> dsList) {
        dsList.sort(Comparator.comparing(Enum::name));
        return dsList;
    }

    public static RefEnvVO convertToRefEnvVO(DmSysEnvDO envDO) {
        RefEnvVO vo = new RefEnvVO();
        vo.setEnvId(envDO.getId());
        vo.setEnvName(envDO.getEnvName());
        vo.setEnvDesc(envDO.getDescription());
        return vo;
    }

    public static RefSpecVO convertToRefSpecVO(DmSecSpecDO specDO) {
        RefSpecVO vo = new RefSpecVO();
        vo.setSpecName(specDO.getName());
        vo.setSpecDesc(specDO.getDescription());
        return vo;
    }

    public static WsRuleEntity convertToWsRuleEntity(SecHintInfo info) {
        WsRuleEntity vo = new WsRuleEntity();
        vo.setLines(info.getLines());
        vo.setSpecName(info.getSpecName());
        vo.setRuleName(info.getRuleName());
        vo.setRuleDesc(info.getMessage());
        vo.setLevel(WarnLevel.valueOfCode(info.getLevel()));
        //vo.setResult(info.getResult());
        return vo;
    }

    public static String convertToDataSourceStatusI18n(DataSourceStatus status, DataSourceType dsType) {
        switch (status) {
            case Normal:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_NORMAL.name());
            case Deleted:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_DELETED.name());
            case NoAuthority:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_NO_AUTHORITY.name());
            case QueryNotEnabled:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_QUERY_NOT_ENABLED.name());
            case NotWorker:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_NOT_WORKER.name());
            case ConnectionFailed:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_CONNECTION_FAILED.name());
            case NoAuthentication:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_NO_AUTHENTICATION.name());
            case Unsupported:
                if (dsType == null) {
                    String unknown = DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_UNKNOWN.name());
                    return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_UNSUPPORTED.name(), unknown);
                } else {
                    return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_UNSUPPORTED.name(), dsType.name());
                }
            default:
                return DmI18nUtils.getMessage(I18nDmLabelKeys.DM_DS_STATUS_UNKNOWN.name());
        }
    }

    public static RangeVO convertToRangeVO(SecRange secRange) {
        RangeVO vo = new RangeVO();
        vo.setRangeId(secRange.getRangeId());
        vo.setRefId(secRange.getRefId());
        vo.setMatchMode(secRange.getMatchMode());
        vo.setRangeType(secRange.getRangeType());
        vo.setVerify(secRange.getVerify());
        vo.setVerifyMessage(secRange.getVerifyMessage());

        vo.setEnv(convertToRangeItemVO(secRange.getEnvironment()));
        vo.setDs(convertToRangeItemVO(secRange.getInstance()));
        vo.setCatalog(convertToRangeItemVO(secRange.getCatalog()));
        vo.setSchema(convertToRangeItemVO(secRange.getSchema()));
        vo.setTable(convertToRangeItemVO(secRange.getTable()));
        vo.setDsType(secRange.getDsType());
        vo.setTableLevelType(secRange.getTableLevelType());

        StringBuilder descStr = new StringBuilder();
        if (secRange.getEnvironment() != null) {
            descStr.append("/").append(secRange.getEnvironment().name());
        }
        if (secRange.getInstance() != null) {
            descStr.append("/").append(secRange.getInstance().name());
        }
        if (secRange.getCatalog() != null) {
            descStr.append("/").append(secRange.getCatalog().name());
        }
        if (secRange.getSchema() != null) {
            descStr.append("/").append(secRange.getSchema().name());
        }
        if (secRange.getTable() != null) {
            descStr.append("/").append(secRange.getTable().name());
        }
        List<SecRangeItem> nodes = secRange.getNodes();
        if (secRange.isChooseAll()) {
            descStr.append(" (" + DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_ALL.name()) + ")");
        } else if (nodes.isEmpty()) {
            descStr.append(" (" + DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_EMPTY.name()) + ")");
        } else {
            descStr.append(" (");
            for (int i = 0; i < nodes.size(); i++) {
                if (i > 0) {
                    descStr.append(", ");
                }
                descStr.append(nodes.get(i).name());
            }
            descStr.append(")");
        }

        vo.setDesc(descStr.toString());

        vo.setNodes(nodes.stream().map(DmConvertUtils::convertToRangeItemVO).collect(Collectors.toList()));
        vo.setChooseAll(secRange.isChooseAll());
        return vo;
    }

    public static RangeItemVO convertToRangeItemVO(SecRangeItem item) {
        if (item == null) {
            return null;
        }

        RangeItemVO vo = new RangeItemVO();
        vo.setName(item.name());
        vo.setDesc(item.desc());
        vo.setValue(item.value());
        return vo;
    }

    public static RangeObjectVO buildRangeObjectVO(String objId, String objName, String objDesc, SecRangeType objType, Map<String, Object> objAttr) {
        RangeObjectVO vo = new RangeObjectVO();
        vo.setObjId(objId);
        vo.setObjName(objName);
        vo.setObjDesc(objDesc);
        vo.setObjType(objType);
        if (objAttr == null) {
            vo.setObjAttr(Collections.emptyMap());
        } else {
            vo.setObjAttr(objAttr);
        }
        return vo;
    }

    public static CheckerRange convertToCheckerRange(DmSecRangeDO rangeDO) {
        CheckerRange range = new CheckerRange();
        range.setScope(convertToScope(rangeDO));
        range.setMatchMode(rangeDO.getMatchMode().getMatchMode());
        range.setLevelPrefix(rangeDO.getLevelPrefix());
        range.setLevelNodes(rangeDO.getLevelNodes());
        range.setChooseAll(rangeDO.isChooseAll());
        return range;
    }

    public static TargetType convertToScope(DmSecRangeDO rangeDO) {
        switch (rangeDO.getRangeType()) {
            case Environment:
                return TargetType.Environment;
            case Instance:
                return TargetType.Instance;
            case Catalog:
                return TargetType.Catalog;
            case Schema:
                return TargetType.Schema;
            case TableOrView:
                if (rangeDO.getTableLevelType() != null) {
                    return rangeDO.getTableLevelType();
                } else {
                    throw new UnsupportedOperationException("TableLevelType is null.");
                }
            case Column:
                return TargetType.Column;
            default:
                throw new UnsupportedOperationException(rangeDO.getRangeType() + " Unsupported.");
        }
    }

    public static SecQueryType convertToSecQueryType(DataEditorSqlType sqlType) {
        switch (sqlType) {
            case INSERT:
                return SecQueryType.INSERT;
            case UPDATE:
                return SecQueryType.UPDATE;
            case DELETE:
                return SecQueryType.DELETE;
            case SELECT:
                return SecQueryType.SELECT;
            default:
                return SecQueryType.UNKNOWN;
        }
    }

    public static DevopsScmVO convertToDevopsScmVO(DmProjectScmDO scmDO, Map<ScmType, DmScmDef> defMap) {
        DmScmDef scmDef = defMap.get(scmDO.getScmType());

        DevopsScmVO scmVO = new DevopsScmVO();
        scmVO.setScmId(scmDO.getId());
        scmVO.setScmType(scmDO.getScmType());
        scmVO.setScmTypeI18n(DmI18nUtils.getMessage(scmDO.getScmType().getI18nKey()));
        scmVO.setDisplay(scmDO.getScmDisplay());
        scmVO.setServiceUrl(scmDO.getScmServiceUrl());
        if (scmDef != null) {
            scmVO.setEnable(true);
            scmVO.setEnableMessage("");
        } else {
            scmVO.setEnable(false);
            scmVO.setEnableMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_UNAVAILABLE_TYPE.name()));
        }
        return scmVO;
    }

    public static ProjectVO convertToProjectVO(DmProjectDO projectDO, ObjectCacheDao ownerCacheService) {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setProjectId(projectDO.getId());
        projectVO.setProjectCode(projectDO.getProjectCode());
        projectVO.setMark(projectDO.getProjectMark());
        projectVO.setStatus(projectDO.getProjectStatus());
        projectVO.setName(projectDO.getProjectName());
        projectVO.setDesc(projectDO.getProjectDesc());
        projectVO.setFlowCheck(projectDO.getFlowCheck());
        projectVO.setFlowApprove(projectDO.getFlowApprove());
        projectVO.setFlowExecute(projectDO.getFlowExecute());
        projectVO.setOptions(projectDO.getOptions());
        projectVO.setCreateTime(WellKnowFormat.WKF_DATE10.format(projectDO.getGmtCreate()));

        String projectUid = projectDO.getProjectUid();
        UserCacheEntry projectUser = ownerCacheService.queryByUid(projectUid);
        if (projectUser != null) {
            projectVO.setProjectOwnerName(projectUser.getUserName());
        } else {
            projectVO.setProjectOwnerName("UID:" + projectUid);
        }
        projectVO.setProjectOwnerUID(projectUid);
        return projectVO;
    }

    public static ProjectUserVO convertToProjectUserVO(RsAuthPersonObj infoDO) {
        ProjectUserVO vo = new ProjectUserVO();
        vo.setUserUid(infoDO.getUid());
        vo.setUserName(infoDO.getUsername());
        return vo;
    }

    public static OperateUserVO convertToOperateUserVO(RsAuthPersonObj infoDO) {
        OperateUserVO vo = new OperateUserVO();
        vo.setUserUid(infoDO.getUid());
        vo.setUserName(infoDO.getUsername());
        return vo;
    }

    public static ProjectScmVO convertToProjectScmVO(DmProjectScmDO scmDO, Map<ScmType, DmScmDef> defMap) {
        DmScmDef scmDef = defMap.get(scmDO.getScmType());

        ProjectScmVO scmVO = new ProjectScmVO();
        scmVO.setScmId(scmDO.getId());
        scmVO.setScmType(scmDO.getScmType());
        scmVO.setScmTypeI18n(DmI18nUtils.getMessage(scmDO.getScmType().getI18nKey()));
        scmVO.setScmDisplay(scmDO.getScmDisplay());
        if (scmDef != null) {
            scmVO.setEvents(scmDef.getEvents());
            scmVO.setEnable(true);
            scmVO.setEnableMessage("");
        } else {
            scmVO.setEvents(Collections.emptyList());
            scmVO.setEnable(false);
            scmVO.setEnableMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_UNAVAILABLE_TYPE.name()));
        }

        return scmVO;
    }

    public static DevopsScmRepoVO convertToDevopsScmRepoVO(DmRepoDef repo) {
        DevopsScmRepoVO vo = new DevopsScmRepoVO();
        vo.setScmId(repo.getScmId());
        vo.setRepoSpace(repo.getRepoSpace());
        vo.setRepoName(repo.getRepoName());
        vo.setRepoUrl(repo.getRepoUrl());
        vo.setRepoHome(repo.getRepoHome());
        vo.setRepoBranch(repo.getBranch());
        return vo;
    }

    public static GuideCheckFlowRefProjectVO convertToDevopsRefProjectVO(ProjectVO projectVO) {
        GuideCheckFlowRefProjectVO vo = new GuideCheckFlowRefProjectVO();
        vo.setProjectId(projectVO.getProjectId());
        vo.setProjectName(projectVO.getName());
        return vo;
    }

    public static ProjectImVO convertToProjectImVO(DmSysMessengerDO messengerDO, Map<ImType, DmImDef> imDefMap) {
        DmImDef imDef = imDefMap.get(messengerDO.getImType());

        ProjectImVO msgVO = new ProjectImVO();
        msgVO.setImId(messengerDO.getId());
        msgVO.setImDisplay(messengerDO.getImDisplay());
        msgVO.setImType(messengerDO.getImType());
        msgVO.setImTypeI18n(DmI18nUtils.getMessage(messengerDO.getImType().getI18nKey()));
        if (imDef != null) {
            msgVO.setEnable(true);
            msgVO.setEnableMessage("");
        } else {
            msgVO.setEnable(false);
            msgVO.setEnableMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_UNAVAILABLE_TYPE.name()));
        }

        return msgVO;
    }

    public static ProjectImConfigVO convertToProjectImConfigVO(DmProjectMsgDO data, DmSysMessengerDO messengerDO) {
        if (data == null) {
            return null;
        }
        ProjectImConfigVO msgVO = new ProjectImConfigVO();
        msgVO.setImConfigId(data.getId());
        msgVO.setImId(data.getRefMsgId());
        msgVO.setImType(data.getRefMsgType());
        msgVO.setImTypeI18n(DmI18nUtils.getMessage(data.getRefMsgType().getI18nKey()));
        msgVO.setEnable(data.isEnable());
        msgVO.setName(messengerDO != null ? messengerDO.getImDisplay() : "");
        msgVO.setLanguage(data.getLanguage());

        msgVO.setEventProjectStatus(data.isEventProjectStatus());
        msgVO.setEventProjectConfig(data.isEventProjectConfig());
        msgVO.setEventChangeLife(data.isEventChangeLife());
        msgVO.setEventChangeNotice(data.isEventChangeNotice());
        return msgVO;
    }

    public static DevopsImVO convertToDevopsImVO(DmSysMessengerDO scmDO, Map<ImType, DmImDef> defMap) {
        DmImDef imDef = defMap.get(scmDO.getImType());

        DevopsImVO msgVO = new DevopsImVO();
        msgVO.setImId(scmDO.getId());
        msgVO.setDisplay(scmDO.getImDisplay());
        msgVO.setImType(scmDO.getImType());
        msgVO.setImTypeI18n(DmI18nUtils.getMessage(scmDO.getImType().getI18nKey()));
        msgVO.setWebhookUrl(scmDO.getWebhook());
        if (imDef != null) {
            msgVO.setEnable(scmDO.isEnable());
            msgVO.setEnableMessage("");
        } else {
            msgVO.setEnable(false);
            msgVO.setEnableMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_UNAVAILABLE_TYPE.name()));
        }

        return msgVO;
    }

    public static ProjectDevopsVO convertToProjectDevopsVO(DmProjectDevopsDO devopsDO, Map<Long, DmProjectScmDO> scmMap, Map<Long, DmDsDO> dsMap, DmScmService dmScmService) {
        DmProjectScmDO scmDO = scmMap.get(devopsDO.getRefScmId());
        DmDsDO dsDO = dsMap.get(devopsDO.getDsId());

        ProjectDevopsVO vo = new ProjectDevopsVO();
        vo.setDevopsId(devopsDO.getId());
        vo.setScmId(devopsDO.getRefScmId());
        vo.setScmType(devopsDO.getRefScmType());
        vo.setScmTypeI18n(DmI18nUtils.getMessage(devopsDO.getRefScmType().getI18nKey()));
        if (scmDO != null) {
            vo.setScmDisplay(scmDO.getScmDisplay());
        } else {
            vo.setScmDisplay(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_SCM_ERROR.name()));
        }
        vo.setRepoUrl(devopsDO.getScmRepoUrl());
        vo.setRepoName(devopsDO.getScmRepoName());
        vo.setRepoBranch(devopsDO.getScmRepoBranch());
        vo.setRepoScriptPath(devopsDO.getScmRepoScript());

        if (dsDO != null) {
            vo.setDsId(dsDO.getId());
            vo.setDsType(dsDO.getDataSourceType());
            vo.setDsInstance(dsDO.getInstanceId());
            vo.setDsDesc(dsDO.getInstanceDesc());
            vo.setDsHost(dsDO.getHostType() == HostType.PUBLIC ? dsDO.getPublicHost() : dsDO.getPrivateHost());
        } else {
            vo.setDsId(devopsDO.getDsId());
            vo.setDsType(devopsDO.getDsType());
            vo.setDsInstance(devopsDO.getDsInstance());
            vo.setDsDesc(devopsDO.getDsDesc());
            vo.setDsHost(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_DS_ERROR.name()));
        }

        vo.setDsLevels(StringUtils.stringToList(devopsDO.getDsPath().substring(1), "/"));
        vo.setWebHookUrl(generateDevOpsWebHookCallBackUrl(devopsDO));
        vo.setWebHookPwd(devopsDO.getScmBindWebhookPwd());
        DmScmDef defByType = dmScmService.getScmDefByType(devopsDO.getRefScmType());
        if (defByType != null) {
            vo.setWebHookHelpUrl(defByType.getHelpUrl());
        }
        vo.setWebHookEnable(devopsDO.isEnable() && devopsDO.isEnableWebhook());

        vo.setCallbackUrl(devopsDO.getCallbackUrl());
        vo.setCallbackMethod(devopsDO.getCallbackMethod());
        vo.setCallbackEnable(devopsDO.isEnable() && devopsDO.isEnableCallback());

        vo.setTriggerUrl(generateDevOpsTriggerUrl(devopsDO));
        vo.setTriggerEnable(devopsDO.isEnableTrigger());
        vo.setTriggerToken(devopsDO.getTriggerToken());

        vo.setEnable(devopsDO.isEnable());
        return vo;
    }

    private static <T> T afterReadyReturnLast(ProjectChangeStatus status, T one, T two) {
        switch (status) {
            case OPEN:
            case READY:
                return one;
            case WAIT:
            case FINISH:
            case CLOSED:
            case FAILED:
            default:
                return two;
        }
    }

    public static ProjectChangeVO convertToProjectChangeVO(DmProjectDO projectDO, DmProjectChangeDO obj, Map<Long, DmProjectDevopsDO> devopsMap, Map<Long, DmDsDO> dsMap,
                                                           Map<Long, DmProjectScmDO> scmMap) {
        DmProjectDevopsDO devopsDO = devopsMap.get(obj.getRefDevopsId());
        DmDsDO dsDO = dsMap.get(devopsDO.getDsId());
        DmProjectScmDO scmDO = scmMap.get(devopsDO.getRefScmId());

        ProjectChangeVO vo = new ProjectChangeVO();
        vo.setChangeId(obj.getId());
        vo.setProjectId(obj.getRefProjectId());
        vo.setDevopsId(obj.getRefDevopsId());
        vo.setChangeName(obj.getChangeName());
        vo.setChangeTime(WellKnowFormat.WKF_DATE_TIME24.format(obj.getChangeTime()));
        vo.setCurrentStatus(obj.getCurrentStatus());
        vo.setCurrentStep(obj.getCurrentStep());
        vo.setRemark(obj.getRemark());
        vo.setProjectName(projectDO.getProjectName());
        vo.setProjectStatus(projectDO.getProjectStatus());

        // init
        vo.setFlowCheck(projectDO.getFlowCheck());
        vo.setFlowApprove(projectDO.getFlowApprove());
        vo.setFlowExecute(projectDO.getFlowExecute());
        switch (obj.getCurrentStep()) {
            case INIT_SNAPSHOT:
            case INIT:
                break;
            case CHECK:
                vo.setFlowCheck(afterReadyReturnLast(obj.getCurrentStatus(), projectDO.getFlowCheck(), obj.getFlowWalked().getFlowCheck()));
                break;
            case APPROVAL:
                vo.setFlowCheck(obj.getFlowWalked().getFlowCheck());
                vo.setFlowApprove(afterReadyReturnLast(obj.getCurrentStatus(), projectDO.getFlowApprove(), obj.getFlowWalked().getFlowApprove()));
                break;
            case EXECUTE:
                vo.setFlowCheck(obj.getFlowWalked().getFlowCheck());
                vo.setFlowApprove(obj.getFlowWalked().getFlowApprove());
                vo.setFlowExecute(afterReadyReturnLast(obj.getCurrentStatus(), projectDO.getFlowExecute(), obj.getFlowWalked().getFlowExecute()));
            case FINISH:
                vo.setFlowCheck(obj.getFlowWalked().getFlowCheck());
                vo.setFlowApprove(obj.getFlowWalked().getFlowApprove());
                vo.setFlowExecute(obj.getFlowWalked().getFlowExecute());
                break;
            default:
                break;
        }

        vo.setLocked(obj.isLockStatus());

        vo.setScmId(devopsDO.getRefScmId());
        vo.setScmDisplay(scmDO == null ? "(Deleted)" : scmDO.getScmDisplay());
        vo.setScmType(devopsDO.getRefScmType());
        vo.setScmTypeI18n(DmI18nUtils.getMessage(devopsDO.getRefScmType().getI18nKey()));
        vo.setRepoUrl(devopsDO.getScmRepoUrl());
        vo.setRepoName(devopsDO.getScmRepoName());
        vo.setRepoBranch(devopsDO.getScmRepoBranch());
        vo.setRepoScriptPath(devopsDO.getScmRepoScript());

        vo.setDsId(devopsDO.getDsId());
        vo.setDsType(devopsDO.getDsType());
        vo.setDsInstance(devopsDO.getDsInstance());
        vo.setDsDesc(devopsDO.getDsDesc());
        if (RdpConvertUtils.removeNoDescription(dsDO.getInstanceDesc()) == null) {
            vo.setDsDisplay(dsDO.getInstanceId());
        } else {
            vo.setDsDisplay(dsDO.getInstanceDesc());
        }

        if (dsDO != null) {
            vo.setDsHost(dsDO.getHostType() == HostType.PUBLIC ? dsDO.getPublicHost() : dsDO.getPrivateHost());
        } else {
            vo.setDsHost("Unknown");
        }
        vo.setDsLevels(Collections.emptyList());
        return vo;
    }

    public static ChangeCheckItemMO convertToChangeCheckItemMO(SecHintInfo info) {
        ChangeCheckItemMO vo = new ChangeCheckItemMO();
        vo.setSpecName(info.getSpecName());
        vo.setRuleName(info.getRuleName());
        vo.setRuleDesc(info.getMessage());
        vo.setLevel(WarnLevel.valueOfCode(info.getLevel()));
        //vo.setResult(info.getResult());
        return vo;
    }

    public static String generateDevOpsWebHookCallBackUrl(DmProjectDevopsDO devopsDO) {
        return RdpWebUtils.getContextPath() + ("project/webhook/event?" +//
                                               "owner=" + devopsDO.getOwnerUid() + "&" +//
                                               "config=" + devopsDO.getId() + "&" +//
                                               "provider=" + devopsDO.getRefScmType().getProviderType().name());
    }

    public static String generateDevOpsTriggerUrl(DmProjectDevopsDO devopsDO) {
        try {
            return RdpWebUtils.getContextPath() + ("project/webhook/trigger?" +//
                                                   "owner=" + devopsDO.getOwnerUid() + "&" +//
                                                   "config=" + devopsDO.getId() + "&" +//
                                                   "token=" + URLEncoder.encode(devopsDO.getTriggerToken(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            return "Error";
        }
    }

    public static PropertyEditorUiData convertToJobUiData(RdbJob value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(JobEditorFields.NAME, value.getName());
        baseInfo.put(JobEditorFields.EXEC_SQL, value.getExecSql());
        baseInfo.put(JobEditorFields.RUNNING, value.getRunning().toString());
        baseInfo.put(JobEditorFields.CREATOR, value.getCreator());
        baseInfo.put(JobEditorFields.SCHEMA, value.getSchema());
        baseInfo.put(JobEditorFields.INTERVAL, value.getInterval());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToViewUiData(RdbView value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(ViewEditorFields.VIEW_NAME, value.getName());
        baseInfo.put(ViewEditorFields.SQL, value.getSql());
        baseInfo.put(ViewEditorFields.COMMENT, value.getComment());
        baseInfo.put(ViewEditorFields.SCHEMA, value.getSchema());
        baseInfo.putAll(value.getAttributes());
        if (value.getFeatures() != null) {
            value.getFeatures().forEach((k, v) -> {
                if (v != null) {
                    baseInfo.put(k, v.toString());
                }
            });
        }
        return vo;
    }

    public static Map<String, Object> convertToBrowseJobVO(RdbJob value) {
        Map<String, Object> map = new HashMap<>();
        map.put(JobEditorFields.NAME, value.getName());
        map.put(JobEditorFields.EXEC_SQL, value.getExecSql());
        map.put(JobEditorFields.INTERVAL, value.getInterval());
        map.putAll(value.getAttributes());
        return map;
    }

    public static PropertyEditorUiData convertToScheduleJobUiData(RdbScheduleJob value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(ScheduleJobFields.NAME, value.getName());
        baseInfo.put(ScheduleJobFields.JOB_ACTION, value.getExecSql());
        baseInfo.put(ScheduleJobFields.ENABLED, value.getEnabled());
        baseInfo.put(ScheduleJobFields.COMMENTS, value.getComment());
        baseInfo.put(ScheduleJobFields.CREATOR, value.getCreator());
        baseInfo.put(ScheduleJobFields.STATUS, value.getStatus());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToDbLinkUiData(RdbDbLink value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(DbLinkEditorFields.DBLINK_NAME, value.getName());
        baseInfo.put(DbLinkEditorFields.LINK_USERNAME, value.getUsername());
        baseInfo.put(DbLinkEditorFields.LINK_URL, value.getHost());
        baseInfo.put(DbLinkEditorFields.SCHEMA, value.getSchema());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToProcedureUiData(RdbProcedure value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(ProcedureEditorFields.PROCEDURE_NAME, value.getName());
        baseInfo.put(ProcedureEditorFields.SCHEMA, value.getSchema());
        baseInfo.putAll(value.getAttributes());
        if (value.getFeatures() != null) {
            value.getFeatures().forEach((k, v) -> {
                if (v != null) {
                    baseInfo.put(k, v.toString());
                }
            });
        }
        return vo;
    }

    public static PropertyEditorUiData convertToFunctionUiData(RdbFunction value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(FunctionEditorFields.FUNCTION_NAME, value.getName());
        baseInfo.put(FunctionEditorFields.SCHEMA, value.getSchema());
        baseInfo.putAll(value.getAttributes());

        if (value.getFeatures() != null) {
            value.getFeatures().forEach((k, v) -> {
                if (v != null) {
                    baseInfo.put(k, v.toString());
                }
            });
        }
        return vo;
    }

    public static PropertyEditorUiData convertToTriggerUiData(RdbTrigger value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(TriggerEditorFields.TRIGGER_NAME, value.getName());
        baseInfo.put(TriggerEditorFields.TRIGGER_TIME, value.getTriggerTime());
        baseInfo.put(TriggerEditorFields.TRIGGER_EVENT, String.join(",", value.getTriggerEvent()));
        baseInfo.put(TriggerEditorFields.TRIGGER_TABLE, value.getTriggerTableName());
        if (value.getTriggerTableColumns() != null) {
            baseInfo.put(TriggerEditorFields.TRIGGER_COLUMNS, String.join(",", value.getTriggerTableColumns()));
        }
        baseInfo.putAll(value.getAttributes());
        if (value.getFeatures() != null) {
            value.getFeatures().forEach((k, v) -> {
                if (v != null) {
                    baseInfo.put(k, v.toString());
                }
            });
        }
        return vo;
    }

    public static PropertyEditorUiData convertToTableUiData(RdbTable value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(TableEditorFields.MODE_TABLE_NAME, value.getName());
        baseInfo.put(TableEditorFields.MODE_TABLE_SCHEMA, value.getSchema());
        baseInfo.put(TableEditorFields.MODE_TABLE_COMMENT, value.getComment());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToSequence(RdbSequence value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(SequenceFields.SEQUENCE_NAME, value.getName());
        baseInfo.put(SequenceFields.SCHEMA, value.getSchema());
        baseInfo.put(SequenceFields.MIN_VALUE, value.getMinValue());
        baseInfo.put(SequenceFields.MAX_VALUE, value.getMaxValue());
        baseInfo.put(SequenceFields.INCREMENT, value.getIncrementBy());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToUser(RdbUser value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(UserFields.USERNAME, value.getUsername());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToRole(RdbRole value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(RoleFields.ROLE_NAME, value.getRoleName());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static PropertyEditorUiData convertToSynonym(RdbSynonym value) {
        PropertyEditorUiData vo = new PropertyEditorUiData();
        Map<String, String> baseInfo = vo.getBaseInfo();
        baseInfo.put(SynonymFields.NAME, value.getName());
        baseInfo.put(SynonymFields.SCHEMA, value.getSchema());
        baseInfo.put(SynonymFields.TABLE, value.getTable());
        baseInfo.put(SynonymFields.TABLE_SCHEMA, value.getTableSchema());
        baseInfo.putAll(value.getAttributes());
        return vo;
    }

    public static URI createFileUri(String fileUriStr) {
        try {
            return new URI(fileUriStr);
        } catch (Exception e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
    }

    public static DataResultPageVO convertToDataResultPageVO(ResultPageDTO page) {
        DataResultPageVO vo = new DataResultPageVO();

        if (page.getRowSet() == null) {
            vo.setRowSet(Collections.emptyList());
        } else {
            vo.setRowSet(page.getRowSet());
        }

        return vo;
    }

    public static DmApiDataSourceVO convertToDmApiDataSourceVO(ApiDataSourceVO vo, Map<Long, DmSysEnvDO> dsEnvMapping) {
        DmApiDataSourceVO copy = new DmApiDataSourceVO();

        if (dsEnvMapping.containsKey(vo.getId())) {
            copy.setEnvId(dsEnvMapping.get(vo.getId()).getId());
            copy.setEnvName(dsEnvMapping.get(vo.getId()).getEnvName());
        }

        copy.setDataSourceId(vo.getId());
        copy.setGmtCreate(vo.getGmtCreate());
        copy.setGmtModified(vo.getGmtModified());
        //copy.setDeployType(vo.getDeployType());
        copy.setDataSourceType(vo.getDataSourceType());

        switch (vo.getHostType()) {
            case PUBLIC:
                copy.setHost(vo.getPublicHost());
                break;
            case PRIVATE:
                copy.setHost(vo.getPrivateHost());
                break;
            default:
                copy.setHost("hostType Unknown.");
                break;
        }

        copy.setInstanceId(vo.getInstanceId());
        copy.setInstanceDesc(vo.getInstanceDesc());
        copy.setVersion(vo.getVersion());
        return copy;
    }

    public static ApiListDsFO convertToApiListDsFO(DmApiDsListFO vo) {
        ApiListDsFO copy = new ApiListDsFO();

        copy.setDataSourceId(vo.getDataSourceId());
        copy.setType(vo.getDataSourceType());

        copy.setInstanceIdLike(vo.getInstanceIdLike());
        copy.setDataSourceDescLike(vo.getInstanceDescLike());

        copy.setDsHostLike(vo.getHostLike());
        return copy;
    }

    public static WsQueryFO convertToWsQueryFO(DmApiDsQueryFO fo, String clientIp) {
        WsQueryFO q = new WsQueryFO();
        q.setQueryType(fo.getQueryType());
        q.setRequestTime(System.currentTimeMillis());
        q.setClientIp(clientIp);
        q.setLevels(fo.getLevels());
        q.setQueryString(fo.getQueryString());
        q.setQueryArgs(new ArrayList<>());
        q.setForce(fo.isQueryForce());
        q.setReceiveMode(ReceiveMode.PAGE_FULL);

        q.setRdbAutoCommit(true);
        q.setRdbReadOnly(false);
        q.setRdbIsolation(RdbIsolation.DEFAULT);
        return q;
    }

    public static DsDriverFamily convertToDsDriverFamily(DriverFamily df) {
        if (df == null) {
            return null;
        }

        DsDriverFamily r = new DsDriverFamily();
        r.setName(df.getFamilyName());
        r.setVersions(df.getVersions());
        return r;
    }
}
