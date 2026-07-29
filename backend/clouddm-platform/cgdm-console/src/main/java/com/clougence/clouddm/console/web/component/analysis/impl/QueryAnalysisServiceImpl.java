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
package com.clougence.clouddm.console.web.component.analysis.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.detectrule.SecCheckerRules;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.DsResPathObj;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultLimit;
import com.clougence.clouddm.sdk.execute.session.result.ColumnConfig;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class QueryAnalysisServiceImpl implements QueryAnalysisService {

    @Resource
    private DmDsConfigService   configService;
    @Resource
    private SystemDal           systemDal;
    @Resource
    private DmAuthServiceForBiz authService;
    @Resource
    private DmResAuthService    resAuthService;
    @Resource
    private SecRulesService     rulesService;

    @Override
    public List<SplitScript> analysisSplit(DataSourceConfig dsConfig, String queryString, List<QueryArg> queryArgs,//
                                           int baseCodeLine, int baseCodeColumn) {
        SqlEngineSpi engine = this.configService.fetchSqlEngineSpi(dsConfig);
        SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dsConfig, Collections.emptyMap());

        SplitAnalysisSpi analysisSpi = engine.splitAnalysisSpi(parameters);
        List<SplitScript> scripts = analysisSpi.splitScript(queryString, queryArgs, baseCodeLine, baseCodeColumn);
        if (CollectionUtils.isEmpty(scripts)) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " invoker SplitAnalysisSpi failed, result is empty.");
        } else {
            return scripts;
        }
    }

    @Override
    public List<QueryRequest> analysisRequests(DataSourceConfig dsConfig, String queryString, List<QueryArg> queryArgs,//
                                               int baseCodeLine, int baseCodeColumn, QueryAnalysisOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("QueryAnalysisOptions is required for query request analysis.");
        }
        if (dsConfig == null) {
            throw new IllegalArgumentException("DataSourceConfig is required for query request analysis.");
        }

        Map<UmiTypes, Object> levels = options.getLevels();
        Map<UmiTypes, Object> safeLevels = levels == null ? Collections.emptyMap() : levels;
        SqlEngineSpi sqlEngine = this.configService.fetchSqlEngineSpi(dsConfig);
        if (sqlEngine == null) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " has no SqlEngineSpi.");
        }

        SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dsConfig, safeLevels);
        SplitAnalysisSpi splitSpi = sqlEngine.splitAnalysisSpi(parameters);
        if (splitSpi == null) {
            throw new IllegalStateException(sqlEngine + " does not support SplitAnalysisSpi.");
        }
        List<SplitScript> scripts = splitSpi.splitScript(queryString, queryArgs, baseCodeLine, baseCodeColumn);
        if (CollectionUtils.isEmpty(scripts)) {
            throw new IllegalStateException("invoker SplitAnalysisSpi failed, result is empty.");
        }

        List<QueryRequest> requests = new ArrayList<>(scripts.size());
        for (SplitScript script : scripts) {
            QueryRequest request = new QueryRequest();
            request.setQueryBody(script.getScript());
            request.setQueryArgs(script.getScriptArgs());
            request.setQueryTypes(script.getType());
            request.setQueryDsType(dsConfig.getDataSourceType());
            requests.add(request);
        }

        if (options.isEnabled(QueryAnalysisFeature.REWRITE)) {
            rewriteRequests(sqlEngine, parameters, requests);
        }
        analysisResources(sqlEngine, parameters, safeLevels, scripts, requests);
        if (options.isEnabled(QueryAnalysisFeature.LINEAGE)) {
            lineageColumns(sqlEngine, parameters, dsConfig, options, requests);
        }
        if (options.isEnabled(QueryAnalysisFeature.MASKING)) {
            configMasking(sqlEngine, parameters, options, requests);
        }
        return requests;
    }

    private void rewriteRequests(SqlEngineSpi sqlEngine, SqlParserParameters parameters, List<QueryRequest> requests) {
        RewriteSpi rewriteSpi = sqlEngine.rewriteSpi(parameters);
        Boolean rewriteDisabled = this.systemDal.fetchSystemConf(RootUserConfig.Fields.onlineSelectRewriteDisable, Boolean.class);
        if (rewriteSpi == null || Boolean.TRUE.equals(rewriteDisabled)) {
            return;
        }

        Map<String, String> configMap = this.configService.fetchSettingsMap(Arrays.asList(//
                RootUserConfig.Fields.defaultColumnDisplayChars, //
                RootUserConfig.Fields.onlineMaxRecordCount,      //
                RootUserConfig.Fields.onlineMaxResultSetMegaByte,//
                RootUserConfig.Fields.onlineMaxColumnMegaByte,   //
                RootUserConfig.Fields.onlineMaxElementMegaByte)  //
        );
        ResultLimit limit = DmDsUtils.fetchResultLimit(configMap, Requester.CONSOLE);
        RewriteContext rewriteCtx = new RewriteContext();
        rewriteCtx.setFetchLimit(limit.getFetchRecordCountLimit());

        for (QueryRequest request : requests) {
            if (!request.hasQueryType(SplitQueryType.SELECT)) {
                continue;
            }
            String beforeRewrite = request.getQueryBody();
            String afterRewrite = rewriteSpi.rewriterQuery(request, rewriteCtx);
            request.setOriginalBody(beforeRewrite);
            if (StringUtils.equals(beforeRewrite, afterRewrite)) {
                request.setHasRewrite(false);
                request.setRewriteTag(Collections.emptyList());
                request.setQueryBody(beforeRewrite);
            } else {
                request.setHasRewrite(true);
                request.setRewriteTag(rewriteCtx.getRewriterTags());
                request.setQueryBody(afterRewrite);
            }
        }
    }

    private void analysisResources(SqlEngineSpi sqlEngine, SqlParserParameters parameters, Map<UmiTypes, Object> levels,//
                                   List<SplitScript> scripts, List<QueryRequest> requests) {
        BehaviorAnalysisSpi behaviorSpi = sqlEngine.behaviorAnalysisSpi(parameters);
        if (behaviorSpi == null) {
            throw new IllegalStateException(sqlEngine + " does not support BehaviorAnalysisSpi.");
        }

        for (int i = 0; i < requests.size(); i++) {
            QueryRequest request = requests.get(i);
            SplitScript script = scripts.get(i);
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            List<StatementBehavior> behaviors = behaviorSpi.analysisBehavior(request.getQueryBody(), levels, codeLine, codeColumn);
            List<BehaviorRelation> relations = new ArrayList<>();
            if (behaviors != null) {
                for (StatementBehavior behavior : behaviors) {
                    if (behavior == null || behavior.getRelations() == null) {
                        continue;
                    }
                    relations.addAll(behavior.getRelations().stream().filter(Objects::nonNull).toList());
                }
            }
            request.setRelations(relations);
        }
    }

    private void lineageColumns(SqlEngineSpi sqlEngine, SqlParserParameters parameters, DataSourceConfig dsConfig,//
                                QueryAnalysisOptions options, List<QueryRequest> requests) {
        LineageAnalysisSpi lineageSpi = sqlEngine.lineageAnalysisSpi(parameters);
        if (lineageSpi == null) {
            return;
        }

        LineageContext lineageContext = LineageContext.builder()
            .userUID(options.getCurrentUid())
            .dsId(options.getDataSourceId())
            .levelsParam(options.getLevels())
            .dsConfig(dsConfig)
            .build();
        for (QueryRequest request : requests) {
            if (request.hasQueryType(SplitQueryType.SELECT)) {
                List<LineageColumn> lineageCols = lineageSpi.analyze(request.getQueryBody(), lineageContext);
                Set<String> columnNames = new HashSet<>();
                if (lineageCols.stream().anyMatch(c -> !columnNames.add(c.column()))) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_FORBID_SELECT_COLUMN_SAME_NAME.name()));
                }

                Map<String, ColumnConfig> columnList = new LinkedHashMap<>();
                for (LineageColumn lineage : lineageCols) {
                    ColumnConfig config = new ColumnConfig();
                    config.setSourceNames(lineage.sources());
                    // TODO  use DmDsMetaConfigDO config column
                    //List<DmDsMetaConfigDO> configs = this.dataSourceDal.metaConfigMapper().selectAllByDsId(dsId, pathList);

                    columnList.put(lineage.column(), config);
                }

                request.setColumnList(columnList);
            }
        }
    }

    private void configMasking(SqlEngineSpi sqlEngine, SqlParserParameters parameters, QueryAnalysisOptions options, List<QueryRequest> requests) {
        requests.forEach(r -> r.setUsingValueProcess(true));
        String userUid = options.getCurrentUid();
        long dsId = options.getDataSourceId();
        if (AuthDal.ROOT_USER_UID.equals(userUid)) {
            requests.forEach(r -> r.setUsingValueProcess(false));
            return;
        }

        // has rule
        SecCheckerRules rules = this.rulesService.fetchCheckerRulesByDsId(dsId);
        if (!rules.isValid() || CollectionUtils.isEmpty(rules.getSenRuleList())) {
            return;
        }

        //
        Map<UmiTypes, Object> levels = options.getLevels();
        SysObjectRegistrySpi registry = PluginManager.findSpi(SysObjectRegistrySpi.class, sqlEngine.name());
        String currentResourcePath = DmDsUtils.currentResourcePath(levels);
        String instanceResourcePath = DmDsUtils.instanceResourcePath(levels);
        for (QueryRequest request : requests) {
            if (CollectionUtils.isEmpty(request.getColumnList())) {
                this.configMaskingWithoutProvenance(request, registry, parameters.version(), userUid, dsId, currentResourcePath, instanceResourcePath);
            } else {
                this.configMaskingWithProvenance(request, userUid, dsId);
            }
        }
    }

    private void configMaskingWithoutProvenance(QueryRequest request, SysObjectRegistrySpi sysObjRegistry, String dbVersion, String userUid, long dsId,//
                                                String currentResourcePath, String instanceResourcePath) {
        List<DsResPathObj> objList = BehaviorRelations.flattenResource(sysObjRegistry, dbVersion, request.getRelations()).stream().filter(b -> {
            return b.authKind() == SecDataAuthKind.READ;
        }).map(b -> {
            return new DsResPathObj(BehaviorRelations.resourcePath(b.resource(), currentResourcePath, instanceResourcePath));
        }).toList();

        //
        boolean allAuthorized = CollectionUtils.isNotEmpty(objList) && objList.stream().allMatch(path -> {
            return this.authService.checkResPathWithoutError(AuthDal.ROOT_USER_UID, userUid, dsId, AuthKind.DataSource, path, SecDataAuthLabel.DM_DAUTH_SENSITIVE);
        });
        if (allAuthorized) {
            request.setUsingValueProcess(false);
        }
    }

    private void configMaskingWithProvenance(QueryRequest request, String userUid, long dsId) {
        boolean hasEmptyColumnName = request.getColumnList().keySet().stream().anyMatch(StringUtils::isEmpty);
        if (hasEmptyColumnName) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NOT_SUPPORT_SPECIAL_FIELD_NOT_ALIAS.name()));
        }

        List<SourceName> sourceNames = request.getColumnList().values().stream().map(ColumnConfig::getSourceNames).filter(Objects::nonNull).flatMap(Collection::stream).toList();
        if (sourceNames.isEmpty()) {
            return;
        }
        List<String> pathList = sourceNames.stream().map(SourceName::toDsResPath).distinct().toList();
        List<String> skipPaths = this.resAuthService.listAuthByUser(dsId, userUid, AuthKind.DataSource, pathList).stream().map(DmAuthResDO::getResPath).toList();

        for (ColumnConfig config : request.getColumnList().values()) {
            List<SourceName> configSources = config.getSourceNames();
            if (CollectionUtils.isNotEmpty(configSources)) {
                List<SourceName> processSources = configSources.stream().filter(source -> {
                    return skipPaths.stream().noneMatch(path -> source.toDsResPath().startsWith(path));
                }).toList();
                config.setSourceNames(processSources);
                config.setUsingValueProcess(CollectionUtils.isNotEmpty(processSources));
            }
        }
        if (sourceNames.stream().allMatch(source -> {
            return skipPaths.stream().anyMatch(path -> source.toDsResPath().startsWith(path));
        })) {
            request.setUsingValueProcess(false);
        }
    }
}
