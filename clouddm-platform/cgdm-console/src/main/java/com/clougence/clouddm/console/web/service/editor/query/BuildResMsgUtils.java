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
package com.clougence.clouddm.console.web.service.editor.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.console.web.component.detectrule.SecHintInfo;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryType;
import com.clougence.clouddm.console.web.model.vo.editor.query.*;
import com.clougence.clouddm.console.web.service.editor.DsQueryEditorService;
import com.clougence.clouddm.console.web.service.editor.model.DsAvailableDTO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhase;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetCount;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetMeta;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BuildResMsgUtils {

    public static WsResMsg buildRules(WsQueryFO queryDTO, SecRulesCheckResult checkResult) {
        WsRuleResMsg dto = new WsRuleResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.RuleCheck);

        dto.setQueryBody(queryDTO.getQueryString());
        dto.setWarnLevel(WarnLevel.PASS);
        List<WsRuleEntity> message = new ArrayList<>();
        for (SecHintInfo info : checkResult.toSecHintList()) {
            WsRuleEntity ent = DmConvertUtils.convertToWsRuleEntity(info);
            message.add(ent);

            if (ent.getLevel().getLevel() <= dto.getWarnLevel().getLevel()) {
                dto.setWarnLevel(ent.getLevel());
            }
        }

        dto.setMessage(message);
        return dto;
    }

    public static WsResMsg buildCost(WsQueryFO queryDTO, QueryCtx ctx, boolean isFinish) {
        // step : Prepare -> Query  -> Receive    -> Finish
        // cost : (none)  -> preCost -> queryCost -> rcvCost
        WsCostResMsg dto = new WsCostResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.Cost);

        dto.setStep(ctx.getQueryStatus());
        dto.setStartTime(ctx.getStartTime());
        dto.setPreCost(ctx.getPrepareCost());
        dto.setQueryCost(ctx.getQueryCost());
        dto.setRcvCost(ctx.getReceiveCost());
        return dto;
    }

    public static WsResMsg buildResultMeta(WsQueryFO queryDTO, QueryCtx ctx, ResultSetMeta result) {
        WsResultSetMetaMsg dto = new WsResultSetMetaMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.ResultSetMeta);

        dto.setResultId(result.getResultId());
        //dto.setResource(result.getResource());
        dto.setColumnList(result.getColumnList());
        dto.setColumnType(result.getColumnType());
        dto.setReceiveMode(result.getReceiveMode());
        dto.setReceiveCost(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_RECEIVE_COST_MESSAGE.name(), result.getCostTimeMs()));
        dto.setCacheFile(result.getCacheFileUri());
        dto.setQuerySql(result.getQuerySql());
        dto.setOriginal(result.getOriginalScript());

        if (result.isHasRewrite()) {
            dto.setOriginal(result.getOriginalScript().trim());
            I18nUtils i18n = PluginManager.findDsI18nUtil(ctx.getLevels().dsDO().getDataSourceType());
            dto.setRewriteTags(result.getRewriteTag().stream().map(i18n::getMessage).collect(Collectors.toList()));
        }
        return dto;
    }

    public static WsResMsg buildResult(WsQueryFO queryDTO, QueryCtx ctx, ResultSet result) {
        WsResultSetResMsg dto = new WsResultSetResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.ResultSet);

        dto.setResultId(result.getResultId());
        dto.setFetchCount(result.getFetchCount());
        dto.setReceiveCost(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_RECEIVE_COST_MESSAGE.name(), result.getCostTimeMs()));
        dto.setRowSet(result.getRowSet());
        return dto;
    }

    public static WsResMsg buildResultSetRows(WsQueryFO queryDTO, QueryCtx ctx, ResultSetCount result) {
        WsResultSetRowsMsg dto = new WsResultSetRowsMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.ResultSetRows);

        dto.setResultId(result.getResultId());
        dto.setFetchCount(result.getFetchCount());
        dto.setReceiveCost(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_RECEIVE_COST_MESSAGE.name(), result.getCostTimeMs()));
        return dto;
    }

    public static WsResMsg buildStatus(WsQueryFO queryDTO, QueryCtx ctx, DsQueryEditorService queryService) {
        DsAvailableDTO statusDTO = queryService.availableDataSource(queryDTO.getPrimaryUserId(), queryDTO.getCurrentUserId(), ctx.getLevels().dsDO().getId());

        WsStatusResMsg dto = new WsStatusResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.Status);
        dto.setDsStatus(statusDTO.getDsStatus());
        dto.setDsStatusMessage(statusDTO.getDsStatusMessage());

        dto.setRdbCatalog(ctx.getCtxDTO().getRdbCatalog());
        dto.setRdbSchema(ctx.getCtxDTO().getRdbSchema());
        dto.setRdbAutoCommit(ctx.getCtxDTO().isRdbAutoCommit());
        dto.setRdbTxIsolation(ctx.getCtxDTO().getRdbTxIsolation());
        dto.setRdbReadOnly(ctx.getCtxDTO().isRdbReadOnly());
        return dto;
    }

    public static WsResMsg buildDone(WsQueryFO queryDTO) {
        WsDoneResMsg dto = new WsDoneResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.Done);
        return dto;
    }

    public static WsResMsg buildCancelDone(WsQueryFO queryDTO) {
        WsDoneResMsg dto = new WsDoneResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.CancelDone);
        return dto;
    }

    public static WsResMsg buildQueryMsg(WsQueryFO queryDTO, ResultPhase phase, QueryCtx ctx) {
        WsQueryInfoMsg dto = new WsQueryInfoMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.QueryScript);

        SessionContextDTO ctxDTO = ctx.getCtxDTO();
        List<UmiTypes> levelsDef = ctx.getLevels().levelsDef();

        List<String> lineData = new ArrayList<>();
        if (levelsDef.contains(UmiTypes.Catalog)) {
            lineData.add(ctxDTO.getRdbCatalog());
        }
        if (levelsDef.contains(UmiTypes.Schema)) {
            lineData.add(ctxDTO.getRdbSchema());
        }

        dto.setLine(StringUtils.join(lineData.toArray(), ".") + ">");

        String preStr = "";
        if (queryDTO.getQueryType() == WsQueryType.RequestPlan) {
            preStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_PLAN_TITLE.name()) + " ";
        }

        dto.setScript(preStr + phase.getQuerySql().trim());

        if (phase.isHasRewrite()) {
            dto.setOriginal(phase.getOriginalScript().trim());
            I18nUtils i18n = PluginManager.findDsI18nUtil(ctx.getLevels().dsDO().getDataSourceType());
            dto.setRewriteTags(phase.getRewriteTag().stream().map(i18n::getMessage).collect(Collectors.toList()));
        }

        return dto;
    }

    public static WsResMsg buildConsoleMsg(WsQueryFO queryDTO, String message, MessageLevel level, boolean timestamp) {
        WsInfoResMsg dto = new WsInfoResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.Message);

        WsInfoEntity entity = new WsInfoEntity();
        entity.setTitle("");
        entity.setMessage(message);
        entity.setLevel(level);
        entity.setMode(MessageMode.Console);
        entity.setTimestamp(timestamp);

        dto.setEntities(Collections.singletonList(entity));
        return dto;
    }

    public static WsResMsg buildClearHint(WsQueryFO queryDTO) {
        WsClearHintMsg dto = new WsClearHintMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.ClearHintMessage);
        return dto;
    }

    public static WsResMsg buildHintMsg(WsQueryFO queryDTO, String message, MessageLevel level) {
        WsInfoResMsg dto = new WsInfoResMsg();
        dto.setOriginal(queryDTO.getQueryType().name());
        dto.setChannelKey(queryDTO.getChannelKey());
        dto.setSessionId(queryDTO.getSessionId());
        dto.setCurUserId(queryDTO.getCurrentUserId());
        dto.setResultType(WsResultType.Message);

        WsInfoEntity entity = new WsInfoEntity();
        entity.setTitle("");
        entity.setMessage(message);
        entity.setLevel(level);
        entity.setMode(MessageMode.Hint);
        entity.setTimestamp(false);

        dto.setEntities(Collections.singletonList(entity));
        return dto;
    }
}
