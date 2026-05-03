package com.clougence.clouddm.console.web.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.EnvCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultLimit;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

/**
 * @author mode create time is 2021/1/30
 **/
public class DmDsUtils {

    private static final Map<DataSourceType, SessionSpi> DS_SESSION_SPI_CACHE = new ConcurrentHashMap<>();
    private static final Map<DataSourceType, Dialect>    DS_DIALECT_CACHE     = new ConcurrentHashMap<>();
    private static       BizResOwnerCacheService ownerCacheService;
    private static       ConsoleConfigService    consoleService;
    private final static long                    MB_SIZE              = 1024L * 1024L;

    public static void initUtils(ApplicationContext spring) {
        ownerCacheService = spring.getBean(BizResOwnerCacheService.class);
        consoleService = spring.getBean(ConsoleConfigService.class);
    }

    public static Dialect getDialect(DataSourceType dsType) {
        if (!DS_DIALECT_CACHE.containsKey(dsType)) {
            Dialect dialect = PluginManager.findDsDialect(dsType);
            DS_DIALECT_CACHE.put(dsType, dialect);
        }
        return DS_DIALECT_CACHE.get(dsType);
    }

    public static DsLevels createLevels(RdpDataSourceDO dsDO, DsConfig dsSetting, String catalog, String schema) {
        if (dsDO == null) {
            return null;
        }
        List<String> levelsDef = dsSetting.getCategories().getLevels();

        // gen levels
        List<String> levels = new ArrayList<>();
        levels.add(String.valueOf(dsDO.getDsEnvId()));
        levels.add(String.valueOf(dsDO.getId()));
        if (levelsDef.contains(DsMenuType.RdbCatalog.getTypeName())) {
            levels.add(catalog);
        }
        if (levelsDef.contains(DsMenuType.RdbSchema.getTypeName())) {
            levels.add(schema);
        }

        //
        List<UmiTypes> curLevelsDef = new ArrayList<>();
        Map<UmiTypes, Object> curLevelsParam = new HashMap<>();
        for (int i = 2; i < levels.size(); i++) {
            UmiTypes umiType = UmiTypes.valueOfCode(levelsDef.get(i));
            curLevelsParam.put(umiType, levels.get(i));
            curLevelsDef.add(umiType);
        }

        List<String> dbLevels = new ArrayList<>(levels.subList(2, levels.size()));
        return new DsLevels(String.valueOf(dsDO.getDsEnvId()), dsDO, levels, dbLevels, curLevelsDef, curLevelsParam);
    }

    public static SessionContextDTO createSessionCtx(DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));

        SessionSpi spi = DS_SESSION_SPI_CACHE.get(dsConfig.getDataSourceType());
        if (spi == null) {
            spi = PluginManager.findSessionSpi(dsConfig.getDataSourceType());
            DS_SESSION_SPI_CACHE.put(dsConfig.getDataSourceType(), spi);
        }

        return spi.createSessionContext(dsConfig, params);
    }

    public static QueryRequest createRequestCtx(DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, SessionContextDTO sessionCtx, String uid, String clientIp,
                                                boolean formConsole) {
        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));

        SessionSpi spi = DS_SESSION_SPI_CACHE.get(dsConfig.getDataSourceType());
        if (spi == null) {
            spi = PluginManager.findSessionSpi(dsConfig.getDataSourceType());
            DS_SESSION_SPI_CACHE.put(dsConfig.getDataSourceType(), spi);
        }

        return spi.createQueryRequest(sessionCtx, dsConfig, params, uid, clientIp, formConsole);
    }

    public static ResultLimit fetchResultLimit(Map<String, String> configMap, Requester requester) {
        ResultLimit limit = new ResultLimit();
        if (requester.isOnline()) {
            limit.setFetchRecordCountLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.onlineMaxRecordCount), 3000));
            limit.setFetchResultSetBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.onlineMaxResultSetMegaByte), 200) * MB_SIZE);
            limit.setFetchColumnBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.onlineMaxColumnMegaByte), 4) * MB_SIZE);
            limit.setFetchElementBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.onlineMaxElementMegaByte), 1) * MB_SIZE);
            limit.setFetchPageSize(30);
            limit.setQueryTimeoutSec(30);// default 30 seconds for console
        } else {
            limit.setFetchRecordCountLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.taskMaxRecordCount), 3000));
            limit.setFetchResultSetBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.taskMaxResultSetMegaByte), 200) * MB_SIZE);
            limit.setFetchColumnBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.taskMaxColumnMegaByte), 4) * MB_SIZE);
            limit.setFetchElementBytesLimit(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.taskMaxElementMegaByte), 1) * MB_SIZE);
            limit.setFetchPageSize(-1);
            limit.setQueryTimeoutSec(300);// default 5 minutes for task
        }
        return limit;
    }

    public static void fillRequestVariables(List<QueryRequest> queryList, long dsId, String curUser) {
        if (CollectionUtils.isEmpty(queryList)) {
            return;
        }

        UserCacheEntry userCache = ownerCacheService.queryByUid(curUser);
        DsCacheEntry dsCache = ownerCacheService.queryByDsId(dsId);
        EnvCacheEntry envCache = ownerCacheService.queryByEnvId(dsCache.getEnvId());
        Map<String, String> configMap = consoleService.fetchSettingsMap(dsCache.getOwnerUid(), Arrays.asList(//
                UserDefinedConfig.Fields.defaultColumnDisplayChars, //
                UserDefinedConfig.Fields.onlineMaxRecordCount,      //
                UserDefinedConfig.Fields.onlineMaxResultSetMegaByte,//
                UserDefinedConfig.Fields.onlineMaxColumnMegaByte,   //
                UserDefinedConfig.Fields.onlineMaxElementMegaByte)  //
        );

        queryList.forEach(query -> {
            if (query.getVariables() == null) {
                query.setVariables(new HashMap<>());
            }

            query.getVariables().put(ExecuteVariables.DS_ID, String.valueOf(dsCache.getDsNumId()));
            query.getVariables().put(ExecuteVariables.DS_NAME, dsCache.getDsInstId());
            query.getVariables().put(ExecuteVariables.ENV_ID, String.valueOf(envCache.getEnvNumId()));
            query.getVariables().put(ExecuteVariables.ENV_NAME, envCache.getEnvName());
            query.getVariables().put(ExecuteVariables.USER_NAME, userCache.getUserName());
            query.getVariables().put(ExecuteVariables.ROLE_NAME, userCache.getRoleName());

            ResultLimit limit = fetchResultLimit(configMap, query.getRequester());
            query.getResultConf().setQueryTimeoutSec(limit.getQueryTimeoutSec());
            query.getResultConf().setFetchRecordCountLimit(limit.getFetchRecordCountLimit());
            query.getResultConf().setFetchResultSetBytesLimit(limit.getFetchResultSetBytesLimit());
            query.getResultConf().setFetchColumnBytesLimit(limit.getFetchColumnBytesLimit());
            query.getResultConf().setFetchElementBytesLimit(limit.getFetchElementBytesLimit());
            query.getResultConf().setFetchPageSize(limit.getFetchPageSize());
            query.getResultConf().setDisplayChars(safeGetConfSize(configMap.get(UserDefinedConfig.Fields.defaultColumnDisplayChars), 256));
            query.getResultConf().setDataFormat(WellKnowFormat.WKF_DATE10);
            query.getResultConf().setTimeFormat(WellKnowFormat.WKF_TIME24_S9);
            query.getResultConf().setDataTimeFormat(WellKnowFormat.WKF_DATE_TIME24_S9);
            query.getResultConf().setTimeWithZoneFormat(WellKnowFormat.WKF_OFFSET_TIME24_S9);
            query.getResultConf().setDataTimeWithZoneFormat(WellKnowFormat.WKF_OFFSET_DATE_TIME24_S9);
        });
    }

    private static int safeGetConfSize(String val, int defaultVal) {
        return StringUtils.isBlank(val) ? defaultVal : Integer.parseInt(val);
    }
}
