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
package com.clougence.clouddm.console.web.service.ai;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.ai.*;
import com.clougence.clouddm.console.web.model.fo.ai.AiGetSessionFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiSessionListFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiSwitchModelFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiUserInputAnswerFO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiSessionListVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.console.web.service.ai.tool.AiMetadataContext;
import com.clougence.clouddm.console.web.service.ai.tool.AiMetadataTools;
import com.clougence.clouddm.console.web.service.ai.tool.AiToolService;
import com.clougence.clouddm.ds.ai.ChatUsage;
import com.clougence.clouddm.platform.dal.mapper.ai.DmAiSessionMapper;
import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;
import com.clougence.clouddm.platform.dal.model.ai.DmAiSessionDO;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;

@Service
public class AiSessionService {
    @Resource
    private AiPromptBuilder     prompts;
    @Resource
    private DmAiSessionMapper   sessions;
    @Resource
    private AiTurnStore         store;
    @Resource
    private AiEngineService     engines;
    @Resource
    private AiPlatformAccess    platform;
    @Resource
    private DmAuthServiceForBiz authorization;
    @Resource
    private AiProperties        limits;
    @Resource
    private AiToolService       toolService;
    @Resource
    private AiMetadataTools     metadataTools;

    public Map<String, Object> create(String puid, String uid, AiEngineConfigVO engine) {
        DmAiSessionDO session = new DmAiSessionDO();
        session.setSessionId(UUID.randomUUID().toString());
        session.setPuid(puid);
        session.setUid(uid);
        session.setEngineId(engine.getId());
        session.setEngineVersion(engine.getVersion());
        session.setEngineName(engine.getName());
        session = store.create(session);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_SESSION_ID, session.getSessionId());
        result.put(AiSqlConstants.FIELD_ENGINE_ID, session.getEngineId());
        result.put(AiSqlConstants.FIELD_ENGINE_VERSION, session.getEngineVersion());
        result.put(AiSqlConstants.FIELD_CREATED_AT, session.getGmtCreate().toInstant());
        return result;
    }

    public Map<String, Object> switchModel(String puid, String uid, AiSwitchModelFO fo) {
        AiEngineConfigVO engine = engines.requireAvailable(puid, uid, fo.getEngineId(), fo.getEngineVersion());
        DmAiSessionDO session = store.switchModel(puid, uid, fo, engine);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_ENGINE_ID, session.getEngineId());
        result.put(AiSqlConstants.FIELD_ENGINE_VERSION, session.getEngineVersion());
        result.put(AiSqlConstants.FIELD_ENGINE_NAME, session.getEngineName());
        return result;
    }

    public AiMetadataContext context(String puid, String uid, List<String> names) {
        if (names.isEmpty()) {
            if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
                throw AiError.FORBIDDEN.exception();
            }
            return new AiMetadataContext(puid, uid, null);
        }
        DsLevels levels = platform.namespace(puid, uid, names);
        return new AiMetadataContext(puid, uid, levels);
    }

    public AiMetadataContext context(String puid, String uid, AiTurnRecordVO turn) {
        try {
            return context(puid, uid, JsonUtils.toObj(turn.getContextJson(), AiTurnContext.class).getLevels());
        } catch (ErrorMessageException e) {
            if (AiError.INVALID_REQUEST.name().equals(e.getErrorCode())) {
                throw AiError.CONTEXT_REVOKED.exception();
            }
            throw e;
        }
    }

    public AiTurnContext snapshot(List<String> names, AiMetadataContext context, AiSqlSelection selection, AiUserInputAnswerFO answer, String timezone, Integer timezoneOffsetMin) {
        if (context.getLevels() == null) {
            return new AiTurnContext(List.of(), List.of(), "", selection, answer, timezone, timezoneOffsetMin);
        }
        return new AiTurnContext(List
            .copyOf(names), List.copyOf(context.getLevels().dbLevels()), context.getLevels().dsDO().getDataSourceType().name(), selection, answer, timezone, timezoneOffsetMin);
    }

    public void authorize(String puid, String uid, List<AiTurnRecordVO> values) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        for (AiTurnRecordVO turn : values) {
            AiMetadataContext context = context(puid, uid, turn);
            if (context.getLevels() != null) {
                platform.authorizeReferences(puid, uid, context.getLevels(), JsonUtils.toListUseType(turn.getReferencedObjectsJson(), AiContextReference.class));
            }
        }
    }

    public Map<String, Object> get(String puid, String uid, AiGetSessionFO fo) {
        if (fo.getBeforeTurnId() != null && fo.getRequestId() != null) {
            throw AiError.INVALID_REQUEST.exception();
        }
        DmAiSessionDO session = store.requireOwner(puid, uid, fo.getSessionId());
        AiTurnRecordVO active = null;
        if (session.getActiveTurnId() != null) {
            active = store.find(session.getActiveTurnId());
            if (active != null && active.getStatus() == AiTurnStatus.RUNNING && !active.getDeadline().after(store.now())) {
                active = store.finish(session.getSessionId(), active.getId(), AiTurnStatus.EXPIRED, null, null, AiError.TURN_EXPIRED.name(), AiError.TURN_EXPIRED.message(), null);
            }
        }
        AiTurnRecordVO before = null;
        if (fo.getBeforeTurnId() != null) {
            before = store.find(fo.getBeforeTurnId());
            if (before == null || !session.getSessionId().equals(before.getSessionId())) {
                throw AiError.NOT_FOUND.exception();
            }
        }
        List<AiTurnRecordVO> values;
        boolean hasMore = false;
        if (fo.getRequestId() != null) {
            AiTurnRecordVO value = store.findRequest(session.getSessionId(), fo.getRequestId());
            values = List.of();
            if (value != null) {
                values = List.of(value);
            }
        } else {
            values = store.list(session.getSessionId(), before, fo.getPageSize() + 1);
            hasMore = values.size() > fo.getPageSize();
            if (hasMore) {
                values = values.subList(0, fo.getPageSize());
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_ID, session.getSessionId());
        result.put(AiSqlConstants.FIELD_CREATED_AT, session.getGmtCreate().toInstant());
        result.put(AiSqlConstants.FIELD_MODIFIED_AT, session.getGmtModified().toInstant());
        result.put(AiSqlConstants.FIELD_ACCESSIBLE, false);
        if (active != null) {
            Map<String, Object> activeTurn = new LinkedHashMap<>();
            activeTurn.put(AiSqlConstants.FIELD_ID, active.getId());
            activeTurn.put(AiSqlConstants.FIELD_REQUEST_ID, active.getRequestId());
            activeTurn.put(AiSqlConstants.FIELD_STATUS, active.getStatus());
            result.put(AiSqlConstants.FIELD_ACTIVE_TURN, activeTurn);
        }
        List<AiTurnRecordVO> history = store.history(session.getSessionId());
        try {
            authorize(puid, uid, values);
            authorize(puid, uid, history);
        } catch (ErrorMessageException e) {
            if (AiError.FORBIDDEN.name().equals(e.getErrorCode()) || AiError.CONTEXT_REVOKED.name().equals(e.getErrorCode())) {
                return result;
            }
            throw e;
        }
        String prompt = null;
        if (active != null && active.getStatus() == AiTurnStatus.RUNNING) {
            prompt = active.getPrompt();
        }
        AiTurnContext database = new AiTurnContext(List.of(), List.of(), "", null, null, null, null);
        if (active != null) {
            database = JsonUtils.toObj(active.getContextJson(), AiTurnContext.class);
        } else if (!history.isEmpty()) {
            database = JsonUtils.toObj(history.get(0).getContextJson(), AiTurnContext.class);
        }
        AiMetadataContext current = context(puid, uid, database.getLevels());
        AiConversation conversation = new AiConversation(prompts,
            limits.getContextTokens(),
            prompts.system(current.getLevels() != null, database),
            session,
            history,
            database,
            prompt,
            Instant.now());
        result.put(AiSqlConstants.FIELD_CONTEXT_USAGE, conversation.contextUsage(conversation.messages(), toolService.definitions(current)));
        result.put(AiSqlConstants.FIELD_ACCESSIBLE, true);
        result.put(AiSqlConstants.FIELD_ENGINE_ID, session.getEngineId());
        result.put(AiSqlConstants.FIELD_ENGINE_VERSION, session.getEngineVersion());
        result.put(AiSqlConstants.FIELD_ENGINE_NAME, session.getEngineName());
        result.put(AiSqlConstants.FIELD_HAS_MORE, hasMore);
        result.put(AiSqlConstants.FIELD_TURNS, values.stream().map(this::turnView).toList());
        return result;
    }

    public AiEngineConfigVO authorizeRuntime(AiActiveRun run, AiMetadataContext context) {
        AiEngineConfigVO engine = engines.requireAvailable(run.getPuid(), run.getUid(), run.getSession().getEngineId(), run.getSession().getEngineVersion());
        // Summary replay still contains facts from earlier databases; authorize their original snapshots.
        authorize(run.getPuid(), run.getUid(), store.history(run.getSession().getSessionId()));
        if (context.getLevels() != null) {
            platform.authorizeReferences(run.getPuid(), run.getUid(), context.getLevels(), run.getReferences());
        }
        return engine;
    }

    public void saveReferences(AiActiveRun run, AiMetadataContext context) {
        List<AiContextReference> references = metadataTools.references(context, run.getReferences());
        if (context.getLevels() != null) {
            platform.authorizeReferences(run.getPuid(), run.getUid(), context.getLevels(), references);
        }
        store.references(run.getSession().getSessionId(), run.getTurn().getId(), JsonUtils.toJson(references));
        run.setReferences(references);
    }

    public AiTurnRecordVO saveResult(AiActiveRun run, AiMetadataContext context, AiConversation conversation, AiSqlResult candidate, ChatUsage usage) {
        Map<String, Object> summary = new LinkedHashMap<>();
        if (context.getLevels() != null) {
            summary.put(AiSqlConstants.FIELD_DIALECT, context.getLevels().dsDO().getDataSourceType().name());
            summary.put(AiSqlConstants.FIELD_NAMESPACE, context.getLevels().dbLevels());
        }
        summary.put(AiSqlConstants.FIELD_OBJECTS, context.getSchemas().keySet());
        summary.put(AiSqlConstants.FIELD_SCHEMA_READ_AT, context.getSchemas()
            .values()
            .stream()
            .map(s -> s.getSchemaReadAt())
            .max(Instant::compareTo)
            .map(Instant::toString)
            .orElse(null));
        summary.put(AiSqlConstants.FIELD_SOURCE_FRESHNESS, AiSqlConstants.SOURCE_CACHED_OR_LIVE);
        summary.put(AiSqlConstants.FIELD_HISTORY_COMPACTED, conversation.isHistoryCompacted());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_STATUS, candidate.getStatus());
        result.put(AiSqlConstants.FIELD_SQL, candidate.getSql());
        result.put(AiSqlConstants.FIELD_MESSAGE, candidate.getMessage());
        if (candidate.getUserInputRequest() != null) {
            result.put(AiSqlConstants.FIELD_USER_INPUT_REQUEST, candidate.getUserInputRequest());
        }
        result.put(AiSqlConstants.FIELD_CONTEXT_SUMMARY, summary);
        result.put(AiSqlConstants.FIELD_REPLAY_MESSAGES, conversation.replayMessages());
        String resultJson = JsonUtils.toJson(result);
        AiTurnStatus status = AiTurnStatus.NEEDS_INPUT;
        if (AiSqlConstants.RESULT_GENERATED.equals(candidate.getStatus())) {
            status = AiTurnStatus.COMPLETED;
        }
        String usageJson = null;
        if (usage != null) {
            usageJson = JsonUtils.toJson(usage);
        }
        return store.finish(run.getSession().getSessionId(), run.getTurn().getId(), status, resultJson, usageJson, null, null, conversation.durableSummary());
    }

    public Map<String, Object> turnView(AiTurnRecordVO turn) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(AiSqlConstants.FIELD_ID, turn.getId());
        value.put(AiSqlConstants.FIELD_REQUEST_ID, turn.getRequestId());
        value.put(AiSqlConstants.FIELD_STATUS, turn.getStatus());
        value.put(AiSqlConstants.FIELD_PROMPT, turn.getPrompt());
        value.put(AiSqlConstants.FIELD_CONTEXT, JsonUtils.toObj(turn.getContextJson(), AiTurnContext.class));
        value.put(AiSqlConstants.FIELD_ENGINE_ID, turn.getEngineId());
        value.put(AiSqlConstants.FIELD_ENGINE_VERSION, turn.getEngineVersion());
        value.put(AiSqlConstants.FIELD_ENGINE_NAME, turn.getEngineName());
        value.put(AiSqlConstants.FIELD_CREATED_AT, turn.getGmtCreate().toInstant());
        value.put(AiSqlConstants.FIELD_MODIFIED_AT, turn.getGmtModified().toInstant());
        value.put(AiSqlConstants.FIELD_DEADLINE, turn.getDeadline().toInstant());
        value.put(AiSqlConstants.FIELD_ERROR_CODE, turn.getErrorCode());
        value.put(AiSqlConstants.FIELD_ERROR_MESSAGE, turn.getErrorMessage());
        value.putAll(store.replay(turn).snapshot());
        if (turn.getResultJson() != null) {
            Map<String, Object> result = turnResult(turn);
            result.remove(AiSqlConstants.FIELD_REPLAY_MESSAGES);
            if (turn.getStatus().isSuccessful()) {
                value.put(AiSqlConstants.FIELD_RESULT, result);
            }
        }
        if (turn.getUsageJson() != null) {
            value.put(AiSqlConstants.FIELD_USAGE, JsonUtils.toObj(turn.getUsageJson(), Map.class));
        }
        return value;
    }

    public Map<String, Object> turnResult(AiTurnRecordVO turn) {
        Map<String, Object> result = JsonUtils.toObj(turn.getResultJson(), Map.class);
        if (turn.getUserInputAnswerJson() != null) {
            result.put(AiSqlConstants.FIELD_USER_INPUT_ANSWER, JsonUtils.toObj(turn.getUserInputAnswerJson(), Map.class));
        }
        return result;
    }

    public AiSessionListVO list(String puid, String uid, AiSessionListFO fo) {
        if ((fo.getBeforeId() == null) != (fo.getBeforeModifiedAt() == null)) {
            throw AiError.INVALID_REQUEST.exception();
        }
        String keyword = null;
        if (fo.getKeyword() != null && !fo.getKeyword().isBlank()) {
            keyword = fo.getKeyword().strip();
        }
        Date beforeModifiedAt = null;
        if (fo.getBeforeModifiedAt() != null) {
            beforeModifiedAt = Date.from(fo.getBeforeModifiedAt());
        }
        List<DmAiSessionDO> values = sessions.findPageByUid(puid, uid, beforeModifiedAt, fo.getBeforeId(), keyword, fo.getPageSize() + 1);
        boolean hasMore = values.size() > fo.getPageSize();
        if (hasMore) {
            values = values.subList(0, fo.getPageSize());
        }
        if (values.isEmpty()) {
            return new AiSessionListVO(List.of(), false, null, null);
        }
        Map<String, List<AiTurnRecordVO>> firstTurns = store.firstBySessionIds(values.stream().map(DmAiSessionDO::getId).toList())
            .stream()
            .collect(Collectors.groupingBy(AiTurnRecordVO::getSessionId));
        List<Map<String, Object>> items = new ArrayList<>();
        for (DmAiSessionDO session : values) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put(AiSqlConstants.FIELD_ID, session.getSessionId());
            item.put(AiSqlConstants.FIELD_MODIFIED_AT, session.getGmtModified().toInstant());
            item.put(AiSqlConstants.FIELD_ACCESSIBLE, false);
            List<AiTurnRecordVO> first = firstTurns.getOrDefault(session.getSessionId(), List.of());
            try {
                authorize(puid, uid, first);
                item.put(AiSqlConstants.FIELD_ACCESSIBLE, true);
                item.put(AiSqlConstants.FIELD_ENGINE_NAME, session.getEngineName());
                item.put(AiSqlConstants.FIELD_SUMMARY, session.getTitle());
            } catch (ErrorMessageException e) {
                if (!AiError.FORBIDDEN.name().equals(e.getErrorCode()) && !AiError.CONTEXT_REVOKED.name().equals(e.getErrorCode())) {
                    throw e;
                }
            }
            items.add(item);
        }
        DmAiSessionDO last = values.get(values.size() - 1);
        return new AiSessionListVO(items, hasMore, last.getGmtModified().toInstant(), last.getSessionId());
    }
}
