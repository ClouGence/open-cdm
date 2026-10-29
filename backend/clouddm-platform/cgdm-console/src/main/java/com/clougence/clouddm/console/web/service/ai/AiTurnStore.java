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

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.model.ai.*;
import com.clougence.clouddm.console.web.model.fo.ai.AiSwitchModelFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiTurnFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiUserInputAnswerFO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputRequestVO;
import com.clougence.clouddm.platform.dal.mapper.ai.*;
import com.clougence.clouddm.platform.dal.model.ai.*;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.Resource;

@Service
@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class AiTurnStore {
    @Resource
    private DmAiSessionMapper      sessions;
    @Resource
    private DmAiHistoryEventMapper historyEvents;
    @Resource
    private DmAiHistoryMapper      histories;
    @Resource
    private DmAiTurnRunMapper      runs;
    @Resource
    private DmAiTurnEventMapper    events;
    @Resource
    private AiProperties           limits;

    private DmAiSessionDO session(String sessionId, boolean lock) {
        DmAiSessionDO session;
        if (lock) {
            session = sessions.findBySessionIdForUpdate(sessionId);
        } else {
            session = sessions.findBySessionId(sessionId);
        }
        if (session == null) {
            return null;
        }
        List<DmAiTurnRunDO> active = runs.findRunningBySessionId(session.getId(), lock);
        if (active.isEmpty()) {
            return session;
        }
        Map<Long, DmAiHistoryDO> values = histories.findByIds(active.stream().map(DmAiTurnRunDO::getHistoryId).toList(), lock)
            .stream()
            .collect(Collectors.toMap(DmAiHistoryDO::getId, Function.identity()));
        for (DmAiTurnRunDO run : active) {
            DmAiHistoryDO history = values.get(run.getHistoryId());
            if (history != null) {
                session.setActiveTurnId(history.getUuid());
                break;
            }
        }
        return session;
    }

    public AiTurnRecordVO find(String id) {
        return readTurn(histories.findByUuid(id), false);
    }

    private AiTurnRecordVO lock(String id) {
        return readTurn(histories.findByUuidForUpdate(id), true);
    }

    private AiTurnRecordVO readTurn(DmAiHistoryDO history, boolean lock) {
        if (history == null) {
            return null;
        }
        List<AiTurnRecordVO> values = assemble(List.of(history), lock);
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    public AiTurnRecordVO findRequest(String sessionId, String requestId) {
        return findRequest(sessions.findBySessionId(sessionId), requestId, false);
    }

    private AiTurnRecordVO findRequest(DmAiSessionDO session, String requestId, boolean lock) {
        if (session == null) {
            return null;
        }
        return readTurn(histories.findBySessionIdAndRequestId(session.getId(), requestId, lock), lock);
    }

    public List<AiTurnRecordVO> list(String sessionId, AiTurnRecordVO before, int limit) {
        return list(sessions.findBySessionId(sessionId), before, limit, false);
    }

    private List<AiTurnRecordVO> list(DmAiSessionDO session, AiTurnRecordVO before, int limit, boolean lock) {
        if (session == null) {
            return List.of();
        }
        Long beforeId = null;
        if (before != null) {
            beforeId = before.getHistoryId();
        }
        List<AiTurnRecordVO> values = new ArrayList<>();
        while (values.size() < limit) {
            int remaining = limit - values.size();
            List<DmAiHistoryDO> page = histories.findPageBySessionId(session.getId(), beforeId, remaining, lock);
            values.addAll(assemble(page, lock));
            if (page.size() < remaining) {
                break;
            }
            beforeId = page.get(page.size() - 1).getId();
        }
        return values;
    }

    public List<AiTurnRecordVO> history(String sessionId) {
        DmAiSessionDO session = sessions.findBySessionId(sessionId);
        if (session == null) {
            return List.of();
        }
        return assemble(histories.findBySessionId(session.getId()), false).stream().filter(turn -> turn.getStatus() != AiTurnStatus.RUNNING).toList();
    }

    public List<AiTurnRecordVO> firstBySessionIds(List<Long> sessionIds) {
        if (sessionIds.isEmpty()) {
            return List.of();
        }
        Set<Long> runnable = runs.findBySessionIds(sessionIds).stream().map(DmAiTurnRunDO::getHistoryId).collect(Collectors.toSet());
        Map<Long, Long> first = new LinkedHashMap<>();
        for (DmAiHistoryDO history : histories.findIdsBySessionIds(sessionIds)) {
            if (runnable.contains(history.getId())) {
                first.putIfAbsent(history.getSessionId(), history.getId());
            }
        }
        if (first.isEmpty()) {
            return List.of();
        }
        return assemble(histories.findByIds(new ArrayList<>(first.values()), false), false);
    }

    public List<AiTurnRecordVO> active(List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return assemble(histories.findByUuids(ids), false);
    }

    public List<AiTurnRecordVO> expired(int limit) {
        List<DmAiTurnRunDO> candidates = runs.findRunningByDeadline(now(), limit);
        if (candidates.isEmpty()) {
            return List.of();
        }
        List<Long> ids = candidates.stream().map(DmAiTurnRunDO::getHistoryId).toList();
        Map<Long, AiTurnRecordVO> records = assemble(histories.findByIds(ids, false), false).stream().collect(Collectors.toMap(AiTurnRecordVO::getHistoryId, Function.identity()));
        return candidates.stream().map(run -> records.get(run.getHistoryId())).filter(Objects::nonNull).toList();
    }

    private List<AiTurnRecordVO> assemble(List<DmAiHistoryDO> values, boolean lock) {
        if (values.isEmpty()) {
            return List.of();
        }
        List<Long> ids = values.stream().map(DmAiHistoryDO::getId).toList();
        Map<Long, DmAiTurnRunDO> byHistory = runs.findByHistoryIds(ids, lock).stream().collect(Collectors.toMap(DmAiTurnRunDO::getHistoryId, Function.identity()));
        List<Long> sessionIds = values.stream().map(DmAiHistoryDO::getSessionId).distinct().toList();
        Map<Long, DmAiSessionDO> owners = sessions.findByIds(sessionIds, lock).stream().collect(Collectors.toMap(DmAiSessionDO::getId, Function.identity()));
        Map<Long, List<DmAiHistoryEventDO>> results = historyEvents.findByHistoryIds(ids, lock).stream().collect(Collectors.groupingBy(DmAiHistoryEventDO::getHistoryId));
        List<AiTurnRecordVO> records = new ArrayList<>();
        for (DmAiHistoryDO history : values) {
            DmAiTurnRunDO run = byHistory.get(history.getId());
            DmAiSessionDO owner = owners.get(history.getSessionId());
            if (run != null && owner != null) {
                records.add(turnRecord(history, run, owner, results.getOrDefault(history.getId(), List.of())));
            }
        }
        return records;
    }

    private AiTurnRecordVO turnRecord(DmAiHistoryDO history, DmAiTurnRunDO run, DmAiSessionDO session, List<DmAiHistoryEventDO> results) {
        AiTurnRecordVO turn = new AiTurnRecordVO();
        turn.setId(history.getUuid());
        turn.setHistoryId(history.getId());
        turn.setSessionDbId(history.getSessionId());
        turn.setSessionId(session.getSessionId());
        turn.setRequestId(history.getRequestId());
        turn.setPrompt(history.getMessage());
        turn.setContextJson(history.getContextJson());
        turn.setEngineId(history.getModel());
        turn.setEngineVersion(history.getEngineVersion());
        turn.setEngineName(history.getEngineName());
        turn.setReferencedObjectsJson(history.getReferencedObjectsJson());
        turn.setUsageJson(history.getUsageJson());
        turn.setGmtCreate(history.getGmtCreate());
        turn.setGmtModified(history.getGmtModified());
        turn.setRunId(run.getRunId());
        turn.setStatus(run.getStatus());
        turn.setEventSeq(run.getEventSeq());
        turn.setErrorCode(run.getErrorCode());
        turn.setErrorMessage(run.getErrorMessage());
        turn.setDeadline(run.getDeadline());
        for (DmAiHistoryEventDO event : results) {
            if (AiSqlConstants.HISTORY_EVENT_RESULT.equals(event.getEventType())) {
                turn.setResultJson(event.getEventData());
            } else if (AiSqlConstants.HISTORY_EVENT_USER_INPUT_ANSWERED.equals(event.getEventType())) {
                turn.setUserInputAnswerJson(event.getEventData());
            }
        }
        return turn;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public DmAiSessionDO create(DmAiSessionDO session) {
        Date createdAt = now();
        session.setGmtCreate(createdAt);
        session.setGmtModified(createdAt);
        sessions.insert(session);
        return session(session.getSessionId(), false);
    }

    public DmAiSessionDO requireOwner(String puid, String uid, String sessionId) {
        return requireOwner(puid, uid, session(sessionId, false));
    }

    private DmAiSessionDO requireOwner(String puid, String uid, DmAiSessionDO session) {
        if (session == null || !uid.equals(session.getUid()) || !puid.equals(session.getPuid())) {
            throw AiError.NOT_FOUND.exception();
        }
        return session;
    }

    public AiTurnRecordVO duplicate(String puid, String uid, AiTurnFO fo) {
        DmAiSessionDO session = requireOwner(puid, uid, fo.getSessionId());
        AiTurnRecordVO found = findRequest(session, fo.getRequestId(), false);
        if (found != null) {
            requireSameRequest(found, fo);
        }
        return found;
    }

    private void requireSameRequest(AiTurnRecordVO found, AiTurnFO fo) {
        AiTurnContext context = JsonUtils.toObj(found.getContextJson(), AiTurnContext.class);
        if (!Objects.equals(fo.getPrompt(), found.getPrompt()) || !Objects.equals(fo.getEngineId(), found.getEngineId()) || fo.getEngineVersion() != found.getEngineVersion()
            || !fo.getLevels().equals(context.getLevels()) || !Objects.equals(fo.getSelection(), context.getSelection())
            || !Objects.equals(fo.getUserInputAnswer(), context.getUserInputAnswer())) {
            throw AiError.REQUEST_CONFLICT.exception();
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AiTurnStart begin(String puid, String uid, AiTurnFO fo, AiTurnContext context) {
        String sessionId = fo.getSessionId();
        String requestId = fo.getRequestId();
        String prompt = fo.getPrompt();
        String engineId = fo.getEngineId();
        int engineVersion = fo.getEngineVersion();
        DmAiSessionDO session = requireOwner(puid, uid, session(sessionId, true));
        AiTurnRecordVO found = findRequest(session, requestId, true);
        if (found != null) {
            requireSameRequest(found, fo);
            return new AiTurnStart(session, found, true);
        }
        if (!engineId.equals(session.getEngineId()) || engineVersion != session.getEngineVersion()) {
            throw AiError.SESSION_MODEL_CHANGED.exception();
        }
        if (session.getActiveTurnId() != null) {
            AiTurnRecordVO active = lock(session.getActiveTurnId());
            if (active != null && active.getStatus() == AiTurnStatus.RUNNING && !active.getDeadline().after(now())) {
                terminal(session, active, AiTurnStatus.EXPIRED, null, null, AiError.TURN_EXPIRED.name(), AiError.TURN_EXPIRED.message());
            } else {
                throw AiError.SESSION_BUSY.exception();
            }
        }
        if (fo.getRegenerateTurnId() != null) {
            AiTurnRecordVO target = lock(fo.getRegenerateTurnId());
            if (target == null || !sessionId.equals(target.getSessionId())) {
                throw AiError.NOT_FOUND.exception();
            }
            if (target.getStatus() == AiTurnStatus.RUNNING) {
                throw AiError.INVALID_REQUEST.exception();
            }
            // Edit or re-answer from this question; later answers and any derived summary must not leak into replay.
            AiTurnContext previousContext = JsonUtils.toObj(target.getContextJson(), AiTurnContext.class);
            if (previousContext.getUserInputAnswer() != null) {
                AiTurnRecordVO question = find(previousContext.getUserInputAnswer().getRequestHistoryId());
                if (question != null && sessionId.equals(question.getSessionId())) {
                    historyEvents.deleteByHistoryIdAndType(question.getHistoryId(), AiSqlConstants.HISTORY_EVENT_USER_INPUT_ANSWERED);
                }
            }
            historyEvents.deleteBySessionIdFromHistoryId(session.getId(), target.getHistoryId());
            events.deleteBySessionIdFromHistoryId(session.getId(), target.getHistoryId());
            runs.deleteBySessionIdFromHistoryId(session.getId(), target.getHistoryId());
            histories.deleteBySessionIdFromId(session.getId(), target.getHistoryId());
            session.setCompactSummary(null);
            session.setCompactAnchorHistoryId(null);
            sessions.updateCompactionBySessionId(session.getSessionId(), session.getCompactSummary(), session.getCompactAnchorHistoryId());
        }
        AiTurnRecordVO question = validateUserInputAnswer(session, fo.getUserInputAnswer());
        DmAiHistoryDO history = new DmAiHistoryDO();
        history.setUuid(UUID.randomUUID().toString());
        history.setSessionId(session.getId());
        history.setRequestId(requestId);
        history.setMessage(prompt);
        history.setContextJson(JsonUtils.toJson(context));
        history.setModel(session.getEngineId());
        history.setEngineVersion(session.getEngineVersion());
        history.setEngineName(session.getEngineName());
        history.setReferencedObjectsJson("[]");
        Date now = now();
        history.setGmtCreate(now);
        history.setGmtModified(now);
        histories.insert(history);
        DmAiTurnRunDO run = new DmAiTurnRunDO();
        run.setRunId(UUID.randomUUID().toString());
        run.setUid(uid);
        run.setSessionId(session.getId());
        run.setHistoryId(history.getId());
        run.setStatus(AiTurnStatus.RUNNING);
        run.setDeadline(new Date(now.getTime() + TimeUnit.SECONDS.toMillis(limits.getTurnSeconds())));
        run.setGmtCreate(now);
        run.setGmtModified(now);
        runs.insert(run);
        sessions.updateModifiedBySessionId(sessionId, now);
        int titleLength = Math.min(80, prompt.codePointCount(0, prompt.length()));
        sessions.updateTitleBySessionId(sessionId, prompt.substring(0, prompt.offsetByCodePoints(0, titleLength)));
        if (question != null) {
            DmAiHistoryEventDO answer = new DmAiHistoryEventDO();
            answer.setSessionId(session.getId());
            answer.setHistoryId(question.getHistoryId());
            answer.setEventType(AiSqlConstants.HISTORY_EVENT_USER_INPUT_ANSWERED);
            answer.setEventData(JsonUtils.toJson(fo.getUserInputAnswer()));
            answer.setGmtCreate(now);
            answer.setGmtModified(now);
            historyEvents.insert(answer);
            // The answer changes a question already covered by the checkpoint; rebuild it with the paired tool result.
            if (session.getCompactAnchorHistoryId() != null && question.getHistoryId() <= session.getCompactAnchorHistoryId()) {
                session.setCompactSummary(null);
                session.setCompactAnchorHistoryId(null);
                sessions.updateCompactionBySessionId(session.getSessionId(), session.getCompactSummary(), session.getCompactAnchorHistoryId());
            }
        }
        return new AiTurnStart(session(sessionId, false), find(history.getUuid()), false);
    }

    private AiTurnRecordVO validateUserInputAnswer(DmAiSessionDO session, AiUserInputAnswerFO answer) {
        if (answer == null) {
            return null;
        }
        List<AiTurnRecordVO> latest = list(session, null, 1, true);
        if (latest.isEmpty() || latest.get(0).getStatus() != AiTurnStatus.NEEDS_INPUT || !latest.get(0).getId().equals(answer.getRequestHistoryId())) {
            throw AiError.INVALID_REQUEST.exception();
        }
        AiTurnRecordVO question = latest.get(0);
        var result = JsonUtils.toObj(question.getResultJson(), JsonNode.class);
        AiUserInputRequestVO request = JsonUtils.defaultObjectMapper().convertValue(result.get(AiSqlConstants.FIELD_USER_INPUT_REQUEST), AiUserInputRequestVO.class);
        if (request == null || !request.getRequestId().equals(answer.getRequestId()) || question.getUserInputAnswerJson() != null) {
            throw AiError.INVALID_REQUEST.exception();
        }
        return question;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public DmAiSessionDO switchModel(String puid, String uid, AiSwitchModelFO fo, AiEngineConfigVO engine) {
        DmAiSessionDO session = requireOwner(puid, uid, session(fo.getSessionId(), true));
        if (session.getActiveTurnId() != null) {
            AiTurnRecordVO active = lock(session.getActiveTurnId());
            if (active != null && active.getStatus() == AiTurnStatus.RUNNING && !active.getDeadline().after(now())) {
                terminal(session, active, AiTurnStatus.EXPIRED, null, null, AiError.TURN_EXPIRED.name(), AiError.TURN_EXPIRED.message());
            } else {
                throw AiError.SESSION_BUSY.exception();
            }
        }
        if (!fo.getPreviousEngineId().equals(session.getEngineId()) || fo.getPreviousEngineVersion() != session.getEngineVersion()) {
            throw AiError.SESSION_MODEL_CHANGED.exception();
        }
        session.setEngineId(engine.getId());
        session.setEngineVersion(engine.getVersion());
        session.setEngineName(engine.getName());
        sessions.updateModelBySessionId(session.getSessionId(), engine.getId(), engine.getVersion(), engine.getName(), now());
        return session;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void saveCompaction(String sessionId, String turnId, String summary, Long anchorId) {
        DmAiSessionDO session = session(sessionId, true);
        AiTurnRecordVO turn = lock(turnId);
        if (session == null || turn == null || !turnId.equals(session.getActiveTurnId()) || turn.getStatus() != AiTurnStatus.RUNNING || !turn.getDeadline().after(now())) {
            throw AiError.TURN_EXPIRED.exception();
        }
        sessions.updateCompactionBySessionId(sessionId, summary, anchorId);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void appendEvent(String runId, String type, Object data) {
        DmAiTurnRunDO current = runs.findByRunId(runId);
        if (current == null || sessions.findByIds(List.of(current.getSessionId()), true).isEmpty()) {
            throw new CancellationException("AI run removed");
        }
        DmAiTurnRunDO run = runs.findByRunIdForUpdate(runId);
        if (run == null) {
            throw new CancellationException("AI run removed");
        }
        boolean terminalEvent = AiSqlConstants.EVENT_RESULT.equals(type) || AiSqlConstants.EVENT_ERROR.equals(type) || AiSqlConstants.EVENT_DONE.equals(type);
        if (run.getStatus() != AiTurnStatus.RUNNING && !terminalEvent) {
            throw new CancellationException("AI run ended");
        }
        DmAiTurnEventDO event = new DmAiTurnEventDO();
        event.setRunId(runId);
        event.setUid(run.getUid());
        event.setSessionId(run.getSessionId());
        event.setHistoryId(run.getHistoryId());
        event.setSeq(run.getEventSeq() + 1);
        event.setEventType(AiRunEventType.fromWire(type, data).name());
        event.setEventData(JsonUtils.toJson(data));
        Date now = now();
        event.setCreatedMs(now.getTime());
        event.setGmtCreate(now);
        event.setGmtModified(now);
        events.insert(event);
        runs.updateSeqByRunId(runId, event.getSeq());
    }

    public AiRunEvents replay(AiTurnRecordVO turn) {
        AiRunEvents state = new AiRunEvents(turn.getDeadline().toInstant());
        for (DmAiTurnEventDO event : events.findByRunIdAfterSeq(turn.getRunId(), 0)) {
            state.replay(event.getSeq(), AiRunEventType.valueOf(event.getEventType()).getWireType(), JsonUtils.toObj(event.getEventData(), Object.class));
        }
        return state;
    }

    public Date now() {
        // Read an absolute database clock; its session timezone may differ from the JDBC Date mapping.
        return new Date(histories.findCurrentTimeMillis());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AiTurnRecordVO finish(String sessionId, String turnId, AiTurnStatus status, String result, String usage, String error, String errorMessage, String compactSummary) {
        if (status == AiTurnStatus.RUNNING) {
            throw AiError.INTERNAL_ERROR.exception();
        }
        DmAiSessionDO session = session(sessionId, true);
        AiTurnRecordVO turn = lock(turnId);
        if (session == null || turn == null || !sessionId.equals(turn.getSessionId())) {
            throw AiError.NOT_FOUND.exception();
        }
        boolean running = turn.getStatus() == AiTurnStatus.RUNNING;
        AiTurnRecordVO completed = terminal(session, turn, status, result, usage, error, errorMessage);
        if (running && compactSummary != null && completed.getStatus().isSuccessful()) {
            // The current anchor becomes replayable only with its successful terminal history, in this transaction.
            sessions.updateCompactionBySessionId(sessionId, compactSummary, completed.getHistoryId());
        }
        return completed;
    }

    private AiTurnRecordVO terminal(DmAiSessionDO session, AiTurnRecordVO turn, AiTurnStatus status, String result, String usage, String error, String errorMessage) {
        if (turn.getStatus() != AiTurnStatus.RUNNING) {
            return turn;
        }
        Date now = now();
        if (!turn.getDeadline().after(now)) {
            status = AiTurnStatus.EXPIRED;
            error = AiError.TURN_EXPIRED.name();
            errorMessage = AiError.TURN_EXPIRED.message();
        }
        turn.setStatus(status);
        turn.setResultJson(result);
        turn.setUsageJson(usage);
        turn.setErrorCode(error);
        turn.setErrorMessage(errorMessage);
        runs.updateStatusByRunId(turn.getRunId(), status, error, errorMessage, now);
        if (result != null) {
            DmAiHistoryEventDO response = new DmAiHistoryEventDO();
            response.setSessionId(turn.getSessionDbId());
            response.setHistoryId(turn.getHistoryId());
            response.setEventType(AiSqlConstants.HISTORY_EVENT_RESULT);
            response.setEventData(result);
            response.setGmtCreate(now);
            response.setGmtModified(now);
            historyEvents.insert(response);
        }
        histories.updateUsageById(turn.getHistoryId(), usage, now);
        sessions.updateModifiedBySessionId(session.getSessionId(), now);
        return find(turn.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void references(String sessionId, String turnId, String references) {
        DmAiSessionDO session = session(sessionId, true);
        AiTurnRecordVO turn = lock(turnId);
        if (session == null || turn == null || !sessionId.equals(turn.getSessionId()) || turn.getStatus() != AiTurnStatus.RUNNING || !turn.getDeadline().after(now())) {
            throw AiError.TURN_EXPIRED.exception();
        }
        if (histories.updateReferencesById(turn.getHistoryId(), references) != 1) {
            throw AiError.TURN_EXPIRED.exception();
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AiTurnRecordVO cancel(String puid, String uid, String sessionId, String requestId) {
        DmAiSessionDO session = requireOwner(puid, uid, session(sessionId, true));
        AiTurnRecordVO found = findRequest(session, requestId, true);
        if (found == null) {
            throw AiError.NOT_FOUND.exception();
        }
        return terminal(session, found, AiTurnStatus.CANCELLED, null, null, null, null);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void delete(String puid, String uid, String sessionId) {
        DmAiSessionDO session = requireOwner(puid, uid, session(sessionId, true));
        if (session.getActiveTurnId() != null) {
            throw AiError.SESSION_BUSY.exception();
        }
        historyEvents.deleteBySessionId(session.getId());
        events.deleteBySessionId(session.getId());
        runs.deleteBySessionId(session.getId());
        histories.deleteBySessionId(session.getId());
        sessions.deleteBySessionId(sessionId);
    }
}
