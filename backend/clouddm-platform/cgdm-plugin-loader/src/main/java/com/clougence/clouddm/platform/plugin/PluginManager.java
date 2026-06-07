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
package com.clougence.clouddm.platform.plugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.info.DsMeta;
import com.clougence.clouddm.platform.plugin.info.GlobalMeta;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.execute.tools.ToolFactory;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.reload.DataEditorReloadSpi;
import com.clougence.clouddm.sdk.ui.editor.dblink.DbLinkUiDefService;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.job.JobUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.role.RoleUiDefService;
import com.clougence.clouddm.sdk.ui.editor.sequence.SequenceUiDefService;
import com.clougence.clouddm.sdk.ui.editor.synonym.SynonymUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.tablespace.TablespaceUiDefService;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.user.UserUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;
import com.clougence.clouddm.sdk.ui.exception.DetermineExceptionSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.drivers.DriverLoader;
import com.clougence.drivers.factory.DefaultDriverLoader;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Load external datasource service jar for clouddm
 *
 * @author mode 2021/01/11
 */
@Slf4j
public class PluginManager {

    private static final DriverLoader                    driverLoader;
    private static final GlobalMeta                      globalMeta;
    private static final Map<String, DsMeta>             dsMetaMap;
    private static final Map<String, Map<Class<?>, Spi>> dsSpiCache;
    private static final AtomicBoolean                   ready = new AtomicBoolean(false);

    private PluginManager(){
    }

    static {
        // ---- Part 1: resolve driverDirectory to an absolute path ----
        String localDirStr = SystemUtils.getSystemProperty("driverDirectory");
        String appDataHome = GlobalConfUtils.getAppDataHome();

        File driverDir;
        if (StringUtils.isBlank(localDirStr)) {
            // blank → default "drivers" sub-directory under appDataHome
            driverDir = new File(appDataHome, "drivers");
        } else {
            File candidate = new File(localDirStr);
            if (candidate.isAbsolute()) {
                // absolute path → use as-is
                driverDir = candidate;
            } else {
                // relative path → resolve against appDataHome
                driverDir = new File(appDataHome, localDirStr);
            }
        }

        // ---- Part 2: validate / create the directory ----
        if (driverDir.exists()) {
            if (driverDir.isFile()) {
                log.error("[PluginManager] Driver directory path '{}' already exists as a file, application will exit.", driverDir.getAbsolutePath());
                System.exit(1);
            }
            if (!driverDir.canRead() || !driverDir.canWrite()) {
                log.error("[PluginManager] Driver directory '{}' is not readable/writable, application will exit.", driverDir.getAbsolutePath());
                System.exit(1);
            }
        } else {
            if (!driverDir.mkdirs()) {
                log.error("[PluginManager] Failed to create driver directory '{}', application will exit.", driverDir.getAbsolutePath());
                System.exit(1);
            }
            if (!driverDir.canRead() || !driverDir.canWrite()) {
                log.error("[PluginManager] Driver directory '{}' is not readable/writable after creation, application will exit.", driverDir.getAbsolutePath());
                System.exit(1);
            }
        }

        // ---- Part 3: build DefaultDriverLoader ----
        log.info("[PluginManager] Using driver directory: {}", driverDir.getAbsolutePath());
        driverLoader = new DefaultDriverLoader(driverDir, System.getProperties());
        globalMeta = new GlobalMeta();
        dsMetaMap = new ConcurrentHashMap<>();
        dsSpiCache = new ConcurrentHashMap<>();
    }

    // ---------------------------------------------
    //                                        Plugin
    // ---------------------------------------------

    static void addPlugin(DsMeta dsMeta) {
        if (dsMeta != null) {
            dsMetaMap.put(dsMeta.getDsType().getTypeName(), dsMeta);
        }
    }

    static void markStarting() {
        ready.set(false);
    }

    static void markReady() {
        ready.set(true);
    }

    public static boolean isReady() { return ready.get(); }

    public static DsPluginInfo findDsPlugin(DataSourceType dsProduct) {
        return dsProduct == null ? null : dsMetaMap.get(dsProduct.getTypeName());
    }

    static GlobalMeta globalMeta() {
        return globalMeta;
    }

    public static DriverLoader driverLoader() {
        if (driverLoader == null) {
            throw new IllegalStateException("DriverLoader not initialized.");
        }
        return driverLoader;
    }

    // ---------------------------------------------
    //                                Global Service
    // ---------------------------------------------

    public static I18nUtils globalI18nUtils() {
        return globalMeta.getI18nUtils();
    }

    public static boolean hasFeature(String featureKey) {
        return globalMeta.hasFeature(featureKey);
    }

    public static ToolFactory findTools(String toolName) {
        return globalMeta.findTools(toolName);
    }

    public static Map<String, Object> getFeatures() { return globalMeta.getGlobalFeatures(); }

    public static <T> void putService(Class<T> serviceType, Service service) {
        globalMeta.putService(serviceType, service);
    }

    public static <T extends Service> T findService(Class<T> serviceType) {
        return globalMeta.findService(serviceType);
    }

    // ---------------------------------------------
    //                                    Global SPI
    // ---------------------------------------------

    public static <T extends Spi> List<T> findSpi(final Class<T> spiType) {
        return globalMeta.findSpi(spiType);
    }

    public static <T extends Spi> T findSpi(final Class<T> spiType, String named) {
        return globalMeta.findSpi(spiType, named);
    }

    public static <T extends Spi> T findDsSpi(final Class<T> spiType, String named) {
        if (spiType == null || StringUtils.isBlank(named)) {
            return null;
        }

        for (DsMeta dsMeta : dsMetaMap.values()) {
            T spi = dsMeta.findSpi(spiType, named);
            if (spi != null) {
                return spi;
            }
        }

        return null;
    }

    public static List<String> getSpiNamesByType(Class<? extends Spi> spiType) {
        return globalMeta.getSpiNamesByType(spiType);
    }

    // ---------------------------------------------
    //                            DataSource Service
    // ---------------------------------------------

    public static Map<String, Object> hasFeature(DataSourceType dsProduct) {
        DsPluginInfo dsInfo = findDsPlugin(dsProduct);
        return dsInfo == null ? globalMeta.getGlobalFeatures() : dsInfo.getPlusFeatures();
    }

    public static I18nUtils findDsI18nUtil(DataSourceType dsProduct) {
        DsPluginInfo dsInfo = findDsPlugin(dsProduct);
        return dsInfo == null ? globalMeta.getI18nUtils() : dsInfo.getPlusI18nUtil();
    }

    public static SqlBuilder findDsSqlBuilder(DataSourceType dsProduct) {
        DsPluginInfo pluginInfo = findDsPlugin(dsProduct);
        if (pluginInfo == null) {
            throw new UnsupportedOperationException("Unsupported " + dsProduct + " SqlDialect.");
        }
        return pluginInfo.getDsSqlBuilder();
    }

    public static Dialect findDsDialect(DataSourceType dsProduct) {
        DsPluginInfo pluginInfo = findDsPlugin(dsProduct);
        if (pluginInfo == null) {
            throw new UnsupportedOperationException("Unsupported " + dsProduct + " SqlDialect.");
        }
        return pluginInfo.getDsDialect();
    }

    // ---------------------------------------------
    //                                DataSource SPI
    // ---------------------------------------------

    private static Spi findOneSpi(final DataSourceType dsProduct, final Class<? extends Spi> spiType) {
        Map<Class<?>, Spi> spiMap = dsSpiCache.computeIfAbsent(dsProduct.getTypeName(), dsType -> new ConcurrentHashMap<>());
        return spiMap.computeIfAbsent(spiType, dsType -> {
            DsPluginInfo pluginMeta = findDsPlugin(dsProduct);
            if (pluginMeta == null) {
                throw new UnsupportedOperationException("Unsupported " + dsProduct);
            }

            List<? extends Spi> list = pluginMeta.findSpi(spiType);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            } else {
                return list.get(0);
            }
        });
    }

    public static SessionSpi findSessionSpi(DataSourceType dsProduct) {
        return (SessionSpi) findOneSpi(dsProduct, SessionSpi.class);
    }

    public static RdbSupportSpi findRdbSupportSpi(DataSourceType dsProduct) {
        return (RdbSupportSpi) findOneSpi(dsProduct, RdbSupportSpi.class);
    }

    public static SecRulesSupportSpi findSecRulesSupportSpi(DataSourceType dsProduct) {
        return (SecRulesSupportSpi) findOneSpi(dsProduct, SecRulesSupportSpi.class);
    }

    public static TableEditorUiDefService findUiDefService(DataSourceType dsProduct) {
        return (TableEditorUiDefService) findOneSpi(dsProduct, TableEditorUiDefService.class);
    }

    public static CmdTemplateSpi findCmdTemplateSpi(DataSourceType dsProduct) {
        return (CmdTemplateSpi) findOneSpi(dsProduct, CmdTemplateSpi.class);
    }

    public static SelectColumnAnalysisSpi findSelectColumnSpi(DataSourceType dsProduct) {
        return (SelectColumnAnalysisSpi) findOneSpi(dsProduct, SelectColumnAnalysisSpi.class);
    }

    public static DetermineExceptionSpi findDetermineExceptionSpi(DataSourceType dsProduct) {
        return (DetermineExceptionSpi) findOneSpi(dsProduct, DetermineExceptionSpi.class);
    }

    public static TableEditorUiDataSpi findTableEditorSpi(DataSourceType dsProduct) {
        return (TableEditorUiDataSpi) findOneSpi(dsProduct, TableEditorUiDataSpi.class);
    }

    public static DataEditorSpi findDataEditorSpi(DataSourceType dsProduct) {
        return (DataEditorSpi) findOneSpi(dsProduct, DataEditorSpi.class);
    }

    public static DataEditorReloadSpi findDataEditorExtSpi(DataSourceType dsProduct) {
        return (DataEditorReloadSpi) findOneSpi(dsProduct, DataEditorReloadSpi.class);
    }

    public static DsBrowseSpi findDsBrowseSpi(DataSourceType dsProduct) {
        return (DsBrowseSpi) findOneSpi(dsProduct, DsBrowseSpi.class);
    }

    public static ResAnalysisSpi findResourceAnalysisSpi(DataSourceType dsProduct) {
        return (ResAnalysisSpi) findOneSpi(dsProduct, ResAnalysisSpi.class);
    }

    public static SplitAnalysisSpi findSplitAnalysisSpi(DataSourceType dsProduct) {
        return (SplitAnalysisSpi) findOneSpi(dsProduct, SplitAnalysisSpi.class);
    }

    public static ConvertTableDDLSpi findConvertDDLSpi(DataSourceType dsProduct) {
        return (ConvertTableDDLSpi) findOneSpi(dsProduct, ConvertTableDDLSpi.class);
    }

    public static SecDomainResolveSpi findSecDomainResolveSpi(DataSourceType dsProduct) {
        return (SecDomainResolveSpi) findOneSpi(dsProduct, SecDomainResolveSpi.class);
    }

    public static DsConfigSpi findDsConfigSpi(DataSourceType dsProduct) {
        return (DsConfigSpi) findOneSpi(dsProduct, DsConfigSpi.class);
    }

    public static RewriteSpi findRewriteSpi(DataSourceType dsProduct) {
        return (RewriteSpi) findOneSpi(dsProduct, RewriteSpi.class);
    }

    public static FunctionUiDefService findFunctionUiDefService(DataSourceType dsProduct) {
        return (FunctionUiDefService) findOneSpi(dsProduct, FunctionUiDefService.class);
    }

    public static ProcedureUiDefService findProcedureUiDefService(DataSourceType dsProduct) {
        return (ProcedureUiDefService) findOneSpi(dsProduct, ProcedureUiDefService.class);
    }

    public static ViewUiDefService findViewUiDefService(DataSourceType dsProduct) {
        return (ViewUiDefService) findOneSpi(dsProduct, ViewUiDefService.class);
    }

    public static RoleUiDefService findRoleUiDefService(DataSourceType dsProduct) {
        return (RoleUiDefService) findOneSpi(dsProduct, RoleUiDefService.class);
    }

    public static TriggerUiDefService findTriggerUiDefService(DataSourceType dsProduct) {
        return (TriggerUiDefService) findOneSpi(dsProduct, TriggerUiDefService.class);
    }

    public static TablespaceUiDefService findTablespaceUiDefService(DataSourceType dsProduct) {
        return (TablespaceUiDefService) findOneSpi(dsProduct, TablespaceUiDefService.class);
    }

    public static DbLinkUiDefService findDbLinkUiDefService(DataSourceType dsProduct) {
        return (DbLinkUiDefService) findOneSpi(dsProduct, DbLinkUiDefService.class);
    }

    public static JobUiDefService findJobUiDefService(DataSourceType dsProduct) {
        return (JobUiDefService) findOneSpi(dsProduct, JobUiDefService.class);
    }

    public static UserUiDefService findUserUiDefService(DataSourceType dsProduct) {
        return (UserUiDefService) findOneSpi(dsProduct, UserUiDefService.class);
    }

    public static SequenceUiDefService findSequenceUiDefService(DataSourceType dsProduct) {
        return (SequenceUiDefService) findOneSpi(dsProduct, SequenceUiDefService.class);
    }

    public static SynonymUiDefService findSynonymUiDefService(DataSourceType dsProduct) {
        return (SynonymUiDefService) findOneSpi(dsProduct, SynonymUiDefService.class);
    }
}
