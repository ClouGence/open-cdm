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
import java.util.stream.Collectors;

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
import com.clougence.clouddm.console.web.util.DsResPath;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultLimit;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.column.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectItem;
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
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private SystemDal           systemDal;
    @Resource
    private AuthDal             authDal;
    @Resource
    private DmAuthServiceForBiz authService;
    @Resource
    private DmResAuthService    resAuthService;
    @Resource
    private SecRulesService     rulesService;

    @Override
    public List<SplitScript> analysisSplit(DataSourceConfig dsConfig, String queryString, List<QueryArg> queryArgs,//
                                           int baseCodeLine, int baseCodeColumn) {
        SqlEngineSpi engine = this.dmDsConfigService.fetchSqlEngineSpi(dsConfig);
        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(dsConfig, Collections.emptyMap());

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
        SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(dsConfig);
        if (sqlEngine == null) {
            throw new IllegalStateException(dsConfig.getDataSourceType() + " has no SqlEngineSpi.");
        }

        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(dsConfig, safeLevels);
        List<SplitScript> scripts = splitRequests(sqlEngine, parameters, queryString, queryArgs, baseCodeLine, baseCodeColumn);
        List<QueryRequest> requests = new ArrayList<>(scripts.size());
        for (SplitScript script : scripts) {
            QueryRequest request = new QueryRequest();
            request.setQueryBody(script.getScript());
            request.setQueryArgs(script.getScriptArgs());
            request.setQueryTypes(script.getType());
            request.setQueryDsType(dsConfig.getDataSourceType());
            requests.add(request);
        }

        if (options.isEnabled(QueryAnalysisFeature.SQL_REWRITE)) {
            rewriteRequests(sqlEngine, parameters, requests);
        }
        analysisResources(sqlEngine, parameters, safeLevels, scripts, requests);
        if (options.isEnabled(QueryAnalysisFeature.COLUMN_ANALYSIS)) {
            analysisColumns(sqlEngine, parameters, dsConfig, options, requests);
        }
        if (options.isEnabled(QueryAnalysisFeature.DESENSITIZATION)) {
            analysisDesensitization(options, requests);
        }
        return requests;
    }

    private List<SplitScript> splitRequests(SqlEngineSpi sqlEngine, SqlParserParameters parameters, String queryString,//
                                            List<QueryArg> queryArgs, int baseCodeLine, int baseCodeColumn) {
        SplitAnalysisSpi splitSpi = sqlEngine.splitAnalysisSpi(parameters);
        if (splitSpi == null) {
            throw new IllegalStateException(sqlEngine + " does not support SplitAnalysisSpi.");
        }
        List<SplitScript> scripts = splitSpi.splitScript(queryString, queryArgs, baseCodeLine, baseCodeColumn);
        if (CollectionUtils.isEmpty(scripts)) {
            throw new IllegalStateException("invoker SplitAnalysisSpi failed, result is empty.");
        }
        return scripts;
    }

    private void rewriteRequests(SqlEngineSpi sqlEngine, SqlParserParameters parameters, List<QueryRequest> requests) {
        RewriteSpi rewriteSpi = sqlEngine.rewriteSpi(parameters);
        Boolean rewriteDisabled = this.systemDal.fetchSystemConf(RootUserConfig.Fields.onlineSelectRewriteDisable, Boolean.class);
        if (rewriteSpi == null || Boolean.TRUE.equals(rewriteDisabled)) {
            return;
        }

        Map<String, String> configMap = this.dmDsConfigService.fetchSettingsMap(Arrays.asList(//
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
            List<StatementBehavior> behaviors = behaviorSpi.analysisBehavior(request.getQueryBody(),//
                    levels, script.getBodyStartCodeLine(), script.getBodyStartCodeColumn());
            request.setResourceActions(this.converter.convert(behaviors, currentResourcePath, instanceResourcePath));
        }
    }

    private void analysisColumns(SqlEngineSpi sqlEngine, SqlParserParameters parameters, DataSourceConfig dsConfig, QueryAnalysisOptions options,//
                                 List<QueryRequest> requests) {
        SelectColumnAnalysisSpi selectColumnSpi = sqlEngine.selectColumnAnalysisSpi(parameters);
        if (selectColumnSpi == null) {
            return;
        }

        ContextInfo contextInfo = ContextInfo.builder()
            .puid(options.getPrimaryUid())
            .cuid(options.getCurrentUid())
            .dsId(options.getDataSourceId())
            .levelsParam(options.getLevels())
            .deepParser(options.isDeepParser())
            .dataSourceConfig(dsConfig)
            .build();
        for (QueryRequest request : requests) {
            if (request.hasQueryType(SplitQueryType.SELECT)) {
                List<SelectItem> selectItems = selectColumnSpi.parseSelectColumn(request.getQueryBody(), contextInfo);
                Set<String> aliases = new HashSet<>();
                if (selectItems.stream().anyMatch(item -> !aliases.add(item.getItemAlias()))) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(//
                            I18nDmMsgKeys.CONSOLE_QUERY_FORBID_SELECT_COLUMN_SAME_NAME.name()));
                }
                request.setColumnList(selectItems.stream().collect(Collectors.toMap(SelectItem::getItemAlias, SelectItem::getColumns)));
            }
        }
    }

    private void analysisDesensitization(ContextInfo contextInfo, String queryString, SqlEngineSpi sqlEngine,//
                                         SqlParserParameters parameters, List<QueryRequest> requests) {
        requests.forEach(request -> request.setUsingValueProcess(true));
        String curUserUid = contextInfo.getCuid();
        String curOwnerUid = contextInfo.getPuid();
        long dsId = contextInfo.getDsId();
        if (StringUtils.isBlank(curUserUid) || StringUtils.isBlank(curOwnerUid) || dsId <= 0) {
            requests.forEach(request -> request.setUsingValueProcess(false));
            return;
        }

        DmAuthUserDO user = this.authDal.userMapper().queryByUid(curUserUid);
        Map<UmiTypes, Object> levels = contextInfo.getLevelsParam() == null ? Collections.emptyMap() : contextInfo.getLevelsParam();
        DsResPath currentPath = () -> currentResourcePath(levels);
        if (user.getAccountType() == AccountType.PRIMARY_ACCOUNT || this.authCheckService.checkResPathWithoutError(curOwnerUid,//
                curUserUid, dsId, AuthKind.DataSource, currentPath, SecDataAuthLabel.DM_DAUTH_SENSITIVE)) {
            requests.forEach(request -> request.setUsingValueProcess(false));
            return;
        }

        List<QueryRequest> selectRequests = requests.stream().filter(request -> request.getQueryType() == SplitQueryType.SELECT).toList();
        boolean hasColumnAnalysis = selectRequests.stream().allMatch(request -> request.getColumnList() != null);
        if (!hasColumnAnalysis) {
            List<ResourceAction> sqlResources = requests.stream()
                .filter(request -> CollectionUtils.isNotEmpty(request.getResourceActions()))
                .flatMap(request -> request.getResourceActions().stream())
                .toList();
            if (CollectionUtils.isNotEmpty(sqlResources)) {
                List<ResourceAction> readRequests = sqlResources.stream()
                    .filter(resourceAction -> !resourceAction.isSkipPermission())
                    .filter(resourceAction -> resourceAction.getAuthKind() == SecDataAuthKind.READ)
                    .toList();
                if (CollectionUtils.isNotEmpty(readRequests)) {
                    boolean viewOriginData = readRequests.stream().allMatch(resourceAction -> this.authCheckService.checkResPathWithoutError(curOwnerUid, curUserUid, dsId,//
                            AuthKind.DataSource, resourceAction.toDsResPath(), SecDataAuthLabel.DM_DAUTH_SENSITIVE));
                    if (viewOriginData) {
                        requests.forEach(request -> request.setUsingValueProcess(false));
                        return;
                    }
                }
            }
        }

        SecCheckerRules rules = this.rulesService.fetchCheckerRulesByDsId(dsId);
        if (!rules.isValid() || CollectionUtils.isEmpty(rules.getSenRuleList())) {
            return;
        }

        if (hasColumnAnalysis) {
            for (QueryRequest request : selectRequests) {
                boolean hasEmptyColumnName = request.getColumnList().keySet().stream().anyMatch(StringUtils::isEmpty);
                if (hasEmptyColumnName) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(//
                            I18nDmMsgKeys.CONSOLE_QUERY_NOT_SUPPORT_SPECIAL_FIELD_NOT_ALIAS.name()));
                }
            }

            List<RealColumn> columnList = selectRequests.stream()
                .map(QueryRequest::getColumnList)
                .filter(CollectionUtils::isNotEmpty)
                .map(Map::values)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .toList();
            List<String> pathList = columnList.stream().map(RealColumn::toDsResPath).distinct().toList();
            List<String> skipPaths = this.resAuthService.listAuthByUser(dsId, curUserUid, AuthKind.DataSource, pathList).stream().map(DmAuthResDO::getResPath).toList();
            for (RealColumn realColumn : columnList) {
                if (skipPaths.stream().anyMatch(path -> realColumn.toDsResPath().startsWith(path))) {
                    realColumn.setSkipDesensitization(true);
                }
            }
            return;
        }

    }
}
