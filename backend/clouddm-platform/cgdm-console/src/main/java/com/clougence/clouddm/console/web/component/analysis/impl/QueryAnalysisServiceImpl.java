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

import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.analysis.*;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.detectrule.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.constants.RewriteTags;
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
    @Resource
    private SecRulesEngine      rulesEngine;

    @Override
    public Stream<SplitScript> analysisSplitStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn) {
        SqlEngineSpi engine = this.configService.fetchSqlEngineSpi(dsConfig);
        SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dsConfig, Collections.emptyMap());
        SplitAnalysisSpi analysisSpi = engine.splitAnalysisSpi(parameters);
        return analysisSpi.splitScriptStream(reader, queryArgs, baseCodeLine, baseCodeColumn);
    }

    @Override
    public Stream<SecRulesCheckResult> analysisRulesStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn,
                                                           AnalysisRuleOptions options) {
        if (options == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_RULE_OPTIONS_REQUIRED_ERROR.name()));
        }
        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            return Stream.empty();
        }

        Map<UmiTypes, Object> levels = options.getLevels() == null ? Collections.emptyMap() : options.getLevels();
        SqlEngineSpi engine = this.configService.fetchSqlEngineSpi(dsConfig);
        SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dsConfig, Collections.emptyMap());
        SplitAnalysisSpi analysisSpi = engine.splitAnalysisSpi(parameters);
        SecRulesCheckContext context = SecRulesCheckContext.builder()
            .dsId(options.getDsId())
            .currentUID(options.getCurrentUid())
            .currentCatalog((String) levels.get(UmiTypes.Catalog))
            .currentSchema((String) levels.get(UmiTypes.Schema))
            .requester(options.getRequester())
            .sqlParameters(parameters)
            .unsupportedLevel(options.getUnsupportedLevel())
            .build();

        SecRulesCheckSession session = this.rulesEngine.openQueryCheck(options.getCurrentUid(), dsConfig, context);
        if (!session.isEnabled()) {
            return Stream.empty();
        }
        return analysisSpi.splitScriptStream(reader, queryArgs, baseCodeLine, baseCodeColumn)
            .map(s -> session.applyCheck(s.getScript(), s.getBodyStartCodeLine(), s.getBodyStartCodeColumn()))
            .filter(r -> !r.isAllSuccess());
    }

    @Override
    public Stream<QueryRequest> analysisRequestsStream(DataSourceConfig dsConfig, Reader reader, List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn,
                                                       AnalysisQueryOptions options) {
        if (options == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_QUERY_OPTIONS_REQUIRED_ERROR.name()));
        }
        if (dsConfig == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_DS_CONFIG_REQUIRED_ERROR.name()));
        }

        //
        Map<UmiTypes, Object> levels = options.getLevels();
        Map<UmiTypes, Object> safeLevels = levels == null ? Collections.emptyMap() : levels;
        SqlEngineSpi sqlEngine = this.configService.fetchSqlEngineSpi(dsConfig);
        if (sqlEngine == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_SQL_ENGINE_NOT_FOUND_ERROR.name(), dsConfig.getDataSourceType()));
        }

        SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dsConfig, safeLevels);
        parameters = parameters.putAll(options.getParameters().values());
        SplitAnalysisSpi splitSpi = sqlEngine.splitAnalysisSpi(parameters);
        if (splitSpi == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_SPI_NOT_SUPPORTED_ERROR.name(), sqlEngine.name(), "SplitAnalysisSpi"));
        }

        RequestAnalysisPlan analysisPlan = new RequestAnalysisPlan(sqlEngine, parameters, dsConfig, safeLevels, options);
        Stream<SplitScript> scripts = splitSpi.splitScriptStream(reader, queryArgs, baseCodeLine, baseCodeColumn);
        Iterator<SplitScript> iterator = scripts.iterator();
        if (!iterator.hasNext()) {
            scripts.close();
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_SPLIT_RESULT_EMPTY_ERROR.name()));
        }
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.NONNULL), false)
            .map(analysisPlan::analyze)
            .onClose(scripts::close);
    }

    private final class RequestAnalysisPlan {

        private final DataSourceConfig      dsConfig;
        private final AnalysisQueryOptions  options;
        private final SqlParserParameters   parameters;
        private final Map<UmiTypes, Object> levels;
        private final BehaviorAnalysisSpi   behaviorSpi;
        private final RewriteSpi            rewriteSpi;
        private final long                  rewriteFetchLimit;
        private final LineageAnalysisSpi    lineageSpi;
        private final LineageContext        lineageContext;
        private final boolean               maskingEnabled;
        private final boolean               rootUser;
        private final boolean               hasSensitiveRules;
        private final SysObjectRegistrySpi  registry;
        private final String                currentResourcePath;
        private final String                instanceResourcePath;

        private RequestAnalysisPlan(SqlEngineSpi sqlEngine, SqlParserParameters parameters, DataSourceConfig dsConfig, Map<UmiTypes, Object> levels, AnalysisQueryOptions options){
            this.dsConfig = dsConfig;
            this.options = options;
            this.parameters = parameters;
            this.levels = levels;
            this.behaviorSpi = sqlEngine.behaviorAnalysisSpi(parameters);
            if (this.behaviorSpi == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.QUERY_ANALYSIS_SPI_NOT_SUPPORTED_ERROR.name(), sqlEngine.name(), "BehaviorAnalysisSpi"));
            }

            RewriteSpi preparedRewriteSpi = null;
            long preparedRewriteFetchLimit = 0;
            if (options.isEnabled(QueryAnalysisFeature.REWRITE)) {
                Boolean rewriteDisabled = QueryAnalysisServiceImpl.this.systemDal.fetchSystemConf(RootUserConfig.Fields.onlineSelectRewriteDisable, Boolean.class);
                if (!Boolean.TRUE.equals(rewriteDisabled)) {
                    preparedRewriteSpi = sqlEngine.rewriteSpi(parameters);
                    if (preparedRewriteSpi != null) {
                        Map<String, String> configMap = QueryAnalysisServiceImpl.this.configService.fetchSettingsMap(Arrays.asList(//
                                RootUserConfig.Fields.defaultColumnDisplayChars, //
                                RootUserConfig.Fields.onlineMaxRecordCount,      //
                                RootUserConfig.Fields.onlineMaxResultSetMegaByte,//
                                RootUserConfig.Fields.onlineMaxColumnMegaByte,   //
                                RootUserConfig.Fields.onlineMaxElementMegaByte)  //
                        );
                        ResultLimit limit = DmDsUtils.fetchResultLimit(configMap, Requester.CONSOLE);
                        preparedRewriteFetchLimit = limit.getFetchRecordCountLimit();
                    }
                }
            }
            this.rewriteSpi = preparedRewriteSpi;
            this.rewriteFetchLimit = preparedRewriteFetchLimit;

            this.lineageSpi = options.isEnabled(QueryAnalysisFeature.LINEAGE) ? sqlEngine.lineageAnalysisSpi(parameters) : null;
            this.lineageContext = this.lineageSpi == null ? null : LineageContext.builder()
                .userUID(options.getCurrentUid())
                .dsId(options.getDsId())
                .levelsParam(options.getLevels())
                .dsConfig(dsConfig)
                .build();

            this.maskingEnabled = options.isEnabled(QueryAnalysisFeature.MASKING);
            this.rootUser = this.maskingEnabled && AuthDal.ROOT_USER_UID.equals(options.getCurrentUid());
            SecCheckerRules rules = this.maskingEnabled && !this.rootUser ? QueryAnalysisServiceImpl.this.rulesService.fetchCheckerRulesByDsId(options.getDsId()) : null;
            this.hasSensitiveRules = rules != null && rules.isValid() && CollectionUtils.isNotEmpty(rules.getSenRuleList());
            this.registry = this.hasSensitiveRules ? PluginManager.findSpi(SysObjectRegistrySpi.class, sqlEngine.name()) : null;
            this.currentResourcePath = this.hasSensitiveRules ? DmDsUtils.currentResourcePath(options.getLevels()) : null;
            this.instanceResourcePath = this.hasSensitiveRules ? DmDsUtils.instanceResourcePath(options.getLevels()) : null;
        }

        private QueryRequest analyze(SplitScript script) {
            QueryRequest request = new QueryRequest();
            request.setIndex(script.getIndex());
            request.setQueryBody(script.getScript());
            request.setQueryArgs(script.getScriptArgs());
            request.setBodyStartCodeLine(script.getBodyStartCodeLine());
            request.setQueryTypes(script.getType());
            request.setDsType(this.dsConfig.getDataSourceType());

            this.rewrite(request);
            this.analysisResources(script, request);
            this.lineageColumns(request);
            this.configMasking(request);
            return request;
        }

        private void rewrite(QueryRequest request) {
            if (this.rewriteSpi == null) {
                return;
            }
            if (!request.hasQueryType(SplitQueryType.SELECT)) {
                return;
            }
            String beforeRewrite = request.getQueryBody();
            RewriteContext rewriteCtx = new RewriteContext();
            rewriteCtx.setFetchLimit(this.rewriteFetchLimit);
            rewriteCtx.setParameters(this.parameters);
            String afterRewrite = this.rewriteSpi.rewriteLimit(request.getQueryId(), beforeRewrite, rewriteCtx);

            request.setOriginalBody(beforeRewrite);
            if (StringUtils.equals(beforeRewrite, afterRewrite)) {
                request.setHasRewrite(false);
                request.setQueryBody(beforeRewrite);
            } else {
                request.setHasRewrite(true);
                request.addRewriteTag(RewriteTags.LIMIT);
                request.setQueryBody(afterRewrite);
            }
        }

        private void analysisResources(SplitScript script, QueryRequest request) {
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            List<StatementBehavior> behaviors;
            try (StringReader reader = new StringReader(request.getQueryBody());
                    Stream<StatementBehavior> stream = this.behaviorSpi.analysisBehaviorStream(reader, this.levels, codeLine, codeColumn)) {
                behaviors = stream.toList();
            }

            List<BehaviorRelation> relations = new ArrayList<>();
            for (StatementBehavior behavior : behaviors) {
                if (behavior == null || behavior.getRelations() == null) {
                    continue;
                }
                relations.addAll(behavior.getRelations().stream().filter(Objects::nonNull).toList());
            }
            request.setRelations(relations);
        }

        private void lineageColumns(QueryRequest request) {
            if (this.lineageSpi == null) {
                return;
            }

            if (request.hasQueryType(SplitQueryType.SELECT)) {
                List<LineageColumn> lineageCols = this.lineageSpi.analyze(request.getQueryBody(), this.lineageContext);

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

        private void configMasking(QueryRequest request) {
            if (!this.maskingEnabled) {
                return;
            }
            request.setUsingValueProcess(true);
            if (this.rootUser) {
                request.setUsingValueProcess(false);
                return;
            }
            if (!this.hasSensitiveRules) {
                return;
            }

            if (CollectionUtils.isEmpty(request.getColumnList())) {
                QueryAnalysisServiceImpl.this.configMaskingWithoutProvenance(request, this.registry, this.parameters.version(), this.options.getCurrentUid(), this.options
                    .getDsId(), this.currentResourcePath, this.instanceResourcePath);
            } else {
                QueryAnalysisServiceImpl.this.configMaskingWithProvenance(request, this.options.getCurrentUid(), this.options.getDsId());
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
