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
package com.clougence.clouddm.ai.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.ai.config.AiExecutors;
import com.clougence.clouddm.ai.dao.model.DmAiSessionDO;
import com.clougence.clouddm.ai.model.AiError;
import com.clougence.clouddm.ai.model.AiTurnStart;
import com.clougence.clouddm.ai.model.AiTurnStatus;
import com.clougence.clouddm.ai.model.fo.AiResumeTurnFO;
import com.clougence.clouddm.ai.model.fo.AiTurnFO;
import com.clougence.clouddm.ai.model.vo.AiTurnRecordVO;
import com.clougence.clouddm.ai.tool.AiMetadataContext;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiTurnService {
    @Resource
    private DmAuthServiceForBiz            authorization;
    @Resource
    private AiEngineService                engines;
    @Resource
    private AiSessionService               sessions;
    @Resource
    private AiTurnStore                    store;
    @Resource
    private AiCapacity                     capacity;
    @Resource
    private AiExecutors                    executors;
    @Resource
    private AiGenerationService            generation;
    @Resource
    private AiStreamService                streams;
    private final Map<String, AiActiveRun> active = new ConcurrentHashMap<>();

    public AiEventSink start(String puid, String uid, AiTurnFO fo) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        if (fo.selection() != null && fo.selection().endLine() < fo.selection().startLine()) {
            throw AiError.INVALID_REQUEST.exception();
        }
        AiTurnRecordVO existing = store.duplicate(puid, uid, fo);
        if (existing != null) {
            return resume(puid, uid, new AiResumeTurnFO(fo.sessionId(), fo.requestId(), fo.streamClientId()));
        }
        DmAiSessionDO session = store.requireOwner(puid, uid, fo.sessionId());
        if (!fo.engineId().equals(session.getEngineId()) || fo.engineVersion() != session.getEngineVersion()) {
            throw AiError.SESSION_MODEL_CHANGED.exception();
        }
        engines.requireAvailable(puid, uid, session.getEngineId(), session.getEngineVersion());
        AiMetadataContext context = sessions.context(puid, uid, fo.levels());
        AiPermit permit;
        try {
            permit = capacity.acquire(uid, session.getEngineId(), false);
        } catch (ErrorMessageException e) {
            existing = store.duplicate(puid, uid, fo);
            if (existing != null) {
                return resume(puid, uid, new AiResumeTurnFO(fo.sessionId(), fo.requestId(), fo.streamClientId()));
            }
            throw e;
        }
        AiTurnStart started = null;
        AiActiveRun run = null;
        try {
            started = store.begin(puid, uid, fo, sessions.snapshot(fo.levels(), context, fo.selection(), fo.userInputAnswer(), fo.timezone(), fo.timezoneOffsetMin()));
            if (started.existing()) {
                permit.close();
                return resume(puid, uid, new AiResumeTurnFO(fo.sessionId(), fo.requestId(), fo.streamClientId()));
            }
            AiRunHandle handle = new AiRunHandle(Duration.between(store.now().toInstant(), started.turn().getDeadline().toInstant()));
            String runId = started.turn().getRunId();
            AiRunEvents events = new AiRunEvents(started.turn().getDeadline().toInstant(), (type, data) -> store.appendEvent(runId, type, data));
            run = new AiActiveRun(puid, uid, started.session(), started.turn(), handle, permit, events, LocaleContextHolder.getLocale());
            active.put(run.getTurn().getId(), run);
            AiEventSink sink = streams.subscribe(started.turn(), events, fo.streamClientId());
            AiActiveRun executing = run;
            executors.getProvider().execute(() -> execute(executing));
            return sink;
        } catch (RuntimeException e) {
            if (run != null) {
                active.remove(run.getTurn().getId());
                run.getEvents().disconnect();
            }
            permit.close();
            if (started != null && !started.existing()) {
                store.finish(session.getSessionId(), started.turn().getId(), AiTurnStatus.FAILED, null, null, AiError.AI_BUSY.name(), AiError.AI_BUSY.message(), null);
            }
            if (e instanceof RejectedExecutionException) {
                throw AiError.AI_BUSY.exception();
            }
            throw e;
        }
    }

    public AiTurnRecordVO cancel(String puid, String uid, String sessionId, String requestId) {
        store.requireOwner(puid, uid, sessionId);
        AiTurnRecordVO turn = store.findRequest(sessionId, requestId);
        if (turn == null) {
            throw AiError.NOT_FOUND.exception();
        }
        AiActiveRun run = active.get(turn.getId());
        AiTurnRecordVO terminal = store.cancel(puid, uid, sessionId, requestId);
        if (run != null) {
            run.getHandle().cancel();
            publish(run, terminal);
        }
        return terminal;
    }

    public AiEventSink resume(String puid, String uid, AiResumeTurnFO fo) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        store.requireOwner(puid, uid, fo.sessionId());
        AiTurnRecordVO turn = store.findRequest(fo.sessionId(), fo.requestId());
        if (turn == null) {
            throw AiError.NOT_FOUND.exception();
        }
        sessions.authorize(puid, uid, List.of(turn));
        AiRunEvents events = null;
        AiActiveRun running = active.get(turn.getId());
        if (running != null) {
            events = running.getEvents();
        }
        return streams.resume(puid, uid, turn, events, fo.streamClientId());
    }

    private void execute(AiActiveRun run) {
        long started = System.nanoTime();
        log.info("AI turn started: sessionId={}, turnId={}, requestId={}, engineId={}, promptCharacters={}", run.getSession().getSessionId(), run.getTurn().getId(), run.getTurn()
            .getRequestId(), run.getSession().getEngineId(), run.getTurn().getPrompt().length());
        try {
            LocaleContextHolder.setLocale(run.getLocale());
            publish(run, generation.generate(run));
        } catch (Exception e) {
            terminate(run, e);
        } finally {
            log.info("AI turn worker finished: sessionId={}, turnId={}, requestId={}, elapsedMs={}", run.getSession().getSessionId(), run.getTurn().getId(), run.getTurn()
                .getRequestId(), (System.nanoTime() - started) / 1_000_000);
            run.getPermit().close();
            run.getWorkerFinished().set(true);
            LocaleContextHolder.resetLocaleContext();
        }
    }

    private void terminate(AiActiveRun run, Throwable error) {
        while (error instanceof CompletionException && error.getCause() != null) {
            error = error.getCause();
        }
        AiTurnStatus status = AiTurnStatus.FAILED;
        String errorCode = AiError.INTERNAL_ERROR.name();
        String errorMessage = AiError.INTERNAL_ERROR.message();
        if (error instanceof CancellationException) {
            status = AiTurnStatus.CANCELLED;
            errorCode = null;
            errorMessage = null;
        } else if (error instanceof ErrorMessageException business) {
            errorCode = business.getErrorCode();
            errorMessage = business.getMessage();
        } else if (error instanceof ThirdPartyApiException provider) {
            errorCode = provider.getMessageKey();
            errorMessage = DmI18nUtils.getMessage(provider.getMessageKey(), provider.getMessageArgs());
        }
        run.getHandle().cancel();
        if (AiError.TURN_EXPIRED.name().equals(errorCode)) {
            status = AiTurnStatus.EXPIRED;
        }
        if (status == AiTurnStatus.CANCELLED) {
            log.info("AI turn cancelled: sessionId={}, turnId={}, requestId={}", run.getSession().getSessionId(), run.getTurn().getId(), run.getTurn().getRequestId());
        } else {
            log.error("AI turn failed: sessionId={}, turnId={}, requestId={}, status={}, errorCode={}", run.getSession().getSessionId(), run.getTurn().getId(), run.getTurn()
                .getRequestId(), status, errorCode, error);
        }
        try {
            publish(run, store.finish(run.getSession().getSessionId(), run.getTurn().getId(), status, null, null, errorCode, errorMessage, null));
        } catch (Exception e) {
            log.error("AI terminal persistence failed: turnId={}", run.getTurn().getId(), e);
            run.getEvents().disconnect();
        }
    }

    private void publish(AiActiveRun run, AiTurnRecordVO terminal) {
        if (!run.getPublished().compareAndSet(false, true)) {
            return;
        }
        try {
            streams.finish(run.getPuid(), run.getUid(), run.getEvents(), terminal);
        } catch (Exception e) {
            log.error("AI terminal event publication failed: turnId={}", terminal.getId(), e);
            run.getEvents().disconnect();
        }
    }

    public void checkActiveRuns() {
        active.values().removeIf(run -> run.getWorkerFinished().get());
        List<AiActiveRun> runs = new ArrayList<>(active.values());
        if (runs.isEmpty()) {
            return;
        }
        try {
            Map<String, AiTurnRecordVO> current = new HashMap<>();
            store.active(runs.stream().map(run -> run.getTurn().getId()).toList()).forEach(turn -> current.put(turn.getId(), turn));
            for (AiActiveRun run : runs) {
                LocaleContextHolder.setLocale(run.getLocale());
                AiTurnRecordVO turn = current.get(run.getTurn().getId());
                if (turn == null || turn.getStatus() != AiTurnStatus.RUNNING) {
                    run.getHandle().cancel();
                    if (turn != null) {
                        publish(run, turn);
                    } else {
                        run.getEvents().disconnect();
                    }
                    continue;
                }
                try {
                    run.getHandle().checkActive();
                } catch (RuntimeException e) {
                    terminate(run, e);
                }
            }
        } catch (Exception e) {
            runs.forEach(run -> run.getHandle().fail(AiError.INTERNAL_ERROR));
            log.error("AI running-state check failed", e);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    @PreDestroy
    public void stop() {
        try {
            for (AiActiveRun run : active.values()) {
                LocaleContextHolder.setLocale(run.getLocale());
                terminate(run, new CancellationException("AI service stopped"));
            }
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

}
