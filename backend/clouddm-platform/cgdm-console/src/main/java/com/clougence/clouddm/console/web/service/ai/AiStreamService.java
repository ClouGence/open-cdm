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

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.ai.AiRunEventType;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.platform.dal.mapper.ai.DmAiTurnEventMapper;
import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;
import com.clougence.clouddm.platform.dal.model.ai.DmAiTurnEventDO;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiStreamService {
    @Resource
    private AiExecutors                           executors;
    @Resource
    private AiProperties                          limits;
    @Resource
    private AiTurnStore                           store;
    @Resource
    private AiSessionService                      sessions;
    @Resource
    private DmAiTurnEventMapper                   journal;
    private final Set<AiEventSink>                connections = ConcurrentHashMap.newKeySet();
    private final Map<AiEventSink, AiRunFollower> followers   = new ConcurrentHashMap<>();

    public AiEventSink subscribe(AiTurnRecordVO turn, AiRunEvents events, String clientId) {
        AiRunHandle transport = new AiRunHandle(Duration.ofSeconds(limits.getTurnSeconds() + 10L));
        AiEventSink sink = new AiEventSink(turn.getSessionId(), turn.getId(), turn.getRequestId(), transport);
        connections.add(sink);
        try {
            events.subscribe(clientId, sink);
            executors.getEvents().execute(sink::write);
        } catch (RejectedExecutionException e) {
            sink.disconnect();
            connections.remove(sink);
            throw AiError.AI_BUSY.exception();
        }
        return sink;
    }

    public AiEventSink resume(String puid, String uid, AiTurnRecordVO turn, AiRunEvents local, String clientId) {
        if (local != null) {
            return subscribe(turn, local, clientId);
        }
        if (turn.getStatus() == AiTurnStatus.RUNNING) {
            if (turn.getDeadline().after(store.now())) {
                AiRunEvents events = store.replay(turn);
                AiEventSink sink = subscribe(turn, events, clientId);
                followers.put(sink, new AiRunFollower(puid, uid, turn.getId(), events, sink));
                return sink;
            }
            turn = store.finish(turn.getSessionId(), turn.getId(), AiTurnStatus.EXPIRED, null, null, AiError.TURN_EXPIRED.name(), AiError.TURN_EXPIRED.message(), null);
        }
        AiRunEvents events = store.replay(turn);
        finish(puid, uid, events, turn);
        return subscribe(turn, events, clientId);
    }

    public void finish(String puid, String uid, AiRunEvents events, AiTurnRecordVO turn) {
        Map<String, Object> result = null;
        if (turn.getStatus().isSuccessful()) {
            sessions.authorize(puid, uid, List.of(turn));
            result = sessions.turnResult(turn);
            result.remove(AiSqlConstants.FIELD_REPLAY_MESSAGES);
        }
        events.finish(turn, result);
    }

    public void tick() {
        for (AiEventSink sink : connections) {
            sink.tick();
            if (sink.writerFinished()) {
                connections.remove(sink);
            }
        }
    }

    public void followPersistedRuns() {
        for (AiRunFollower follower : followers.values()) {
            if (follower.getSink().disconnected()) {
                followers.remove(follower.getSink());
                continue;
            }
            try {
                AiTurnRecordVO turn = store.find(follower.getTurnId());
                if (turn == null) {
                    follower.getSink().disconnect();
                    followers.remove(follower.getSink());
                    continue;
                }
                sessions.authorize(follower.getPuid(), follower.getUid(), List.of(turn));
                for (DmAiTurnEventDO event : journal.findByRunIdAfterSeq(turn.getRunId(), follower.getEvents().getLastPersistedSeq())) {
                    follower.getEvents().replay(event.getSeq(), AiRunEventType.valueOf(event.getEventType()).getWireType(), JsonUtils.toObj(event.getEventData(), Object.class));
                }
                if (turn.getStatus() != AiTurnStatus.RUNNING) {
                    // The terminal database commit may precede publication of its last event.
                    finish(follower.getPuid(), follower.getUid(), follower.getEvents(), turn);
                    followers.remove(follower.getSink());
                }
            } catch (Exception e) {
                log.error("AI event journal follow failed: turnId={}", follower.getTurnId(), e);
                follower.getSink().disconnect();
                followers.remove(follower.getSink());
            }
        }
    }

    @PreDestroy
    public void stop() {
        connections.forEach(AiEventSink::disconnect);
        followers.clear();
    }
}
