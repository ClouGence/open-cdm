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

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.clougence.clouddm.ai.model.AiSqlConstants;
import com.clougence.clouddm.ai.model.vo.AiTurnRecordVO;
import com.clougence.clouddm.console.web.util.SseResponseUtils;
import com.clougence.clouddm.ds.ai.ChatToolCall;
import com.clougence.clouddm.ds.ai.tool.ChatToolOutcome;
import com.clougence.utils.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/** Generation state belongs to the run; browser connections only subscribe to it. */
public class AiRunEvents {
    private final BiConsumer<String, Object>       recorder;
    private final Instant                          deadline;
    private final Map<String, AiEventSink>         subscribers = new LinkedHashMap<>();
    private final StringBuilder                    draft       = new StringBuilder();
    private final Map<String, Map<String, Object>> activities  = new LinkedHashMap<>();
    @Getter
    private long                                   lastPersistedSeq;
    private String                                 phase       = AiSqlConstants.PHASE_GENERATING;
    private Object                                 contextUsage;
    private Object                                 result;
    private Object                                 error;
    private Object                                 done;

    public AiRunEvents(Instant deadline){
        this(deadline, (type, data) -> {
        });
    }

    public AiRunEvents(Instant deadline, BiConsumer<String, Object> recorder){
        this.deadline = deadline;
        this.recorder = recorder;
    }

    public synchronized void subscribe(String clientId, AiEventSink sink) {
        if (clientId == null || clientId.isBlank()) {
            clientId = UUID.randomUUID().toString();
        }
        AiEventSink previous = subscribers.remove(clientId);
        if (previous != null) {
            previous.disconnect();
        }
        sink.emit(AiSqlConstants.EVENT_STARTED, Map.of(AiSqlConstants.FIELD_DEADLINE, deadline.toString()));
        sink.emit(AiSqlConstants.EVENT_SNAPSHOT, snapshot());
        if (result != null) {
            sink.emit(AiSqlConstants.EVENT_RESULT, result);
        }
        if (error != null) {
            sink.emit(AiSqlConstants.EVENT_ERROR, error);
        }
        if (done != null) {
            sink.emit(AiSqlConstants.EVENT_DONE, done);
        } else {
            subscribers.put(clientId, sink);
        }
    }

    public synchronized void toolStarted(ChatToolCall call) {
        String phase = AiSqlConstants.PHASE_READING;
        if (AiSqlConstants.TOOL_LIST_TABLES.equals(call.name())) {
            phase = AiSqlConstants.PHASE_SEARCHING;
        } else if (AiSqlConstants.TOOL_GENERATE_SQL.equals(call.name())) {
            phase = AiSqlConstants.PHASE_GENERATING;
        }
        emit(AiSqlConstants.EVENT_PHASE, Map.of(AiSqlConstants.FIELD_PHASE, phase));
        emit(AiSqlConstants.EVENT_TOOL_STARTED, Map.of(AiSqlConstants.FIELD_TOOL_CALL_ID, call.id(), AiSqlConstants.FIELD_TOOL_NAME, call.name()));
    }

    public synchronized void toolCompleted(ChatToolCall call, ChatToolOutcome outcome) {
        List<Map<String, String>> observed = new ArrayList<>();
        if (outcome.success() && !AiSqlConstants.TOOL_GENERATE_SQL.equals(call.name()) && outcome.value() instanceof Map<?, ?> value) {
            JsonNode exposed = JsonUtils.defaultObjectMapper().valueToTree(value);
            exposed.path(AiSqlConstants.FIELD_ITEMS) //
                .forEach(item -> observed.add(Map.of( //
                        AiSqlConstants.FIELD_TYPE, item.path(AiSqlConstants.FIELD_TYPE).textValue(), //
                        AiSqlConstants.FIELD_NAME, item.path(AiSqlConstants.FIELD_NAME).textValue())));
            exposed.path(AiSqlConstants.FIELD_TABLES) //
                .forEach(item -> observed.add(Map.of( //
                        AiSqlConstants.FIELD_TYPE, item.path(AiSqlConstants.FIELD_OBJECT).path(AiSqlConstants.FIELD_TYPE).textValue(), //
                        AiSqlConstants.FIELD_NAME, item.path(AiSqlConstants.FIELD_OBJECT).path(AiSqlConstants.FIELD_NAME).textValue())));
        }
        String status = AiSqlConstants.TOOL_STATUS_ERROR;
        if (outcome.success()) {
            status = AiSqlConstants.TOOL_STATUS_OK;
        }
        Map<String, Object> completed = new LinkedHashMap<>();
        completed.put(AiSqlConstants.FIELD_TOOL_CALL_ID, call.id());
        completed.put(AiSqlConstants.FIELD_TOOL_NAME, call.name());
        completed.put(AiSqlConstants.FIELD_OBJECTS, observed);
        completed.put(AiSqlConstants.FIELD_STATUS, status);
        if (outcome.success() && AiSqlConstants.TOOL_GENERATE_SQL.equals(call.name())) {
            completed.put(AiSqlConstants.FIELD_RESULT, outcome.value());
        }
        emit(AiSqlConstants.EVENT_TOOL_COMPLETED, completed);
    }

    public synchronized void finish(AiTurnRecordVO turn, Map<String, Object> result) {
        if (turn.getStatus().isSuccessful() && result != null) {
            emit(AiSqlConstants.EVENT_RESULT, result);
        } else if (turn.getErrorCode() != null) {
            emit(AiSqlConstants.EVENT_ERROR, SseResponseUtils.errorPayload(turn.getErrorCode(), turn.getErrorMessage(), turn.getErrorMessage(), turn.getRequestId()));
        }
        Map<String, Object> terminal = new LinkedHashMap<>();
        terminal.put(AiSqlConstants.FIELD_STATUS, turn.getStatus());
        Object usage = null;
        if (turn.getUsageJson() != null) {
            usage = JsonUtils.toObj(turn.getUsageJson(), JsonNode.class);
        }
        terminal.put(AiSqlConstants.FIELD_USAGE, usage);
        emit(AiSqlConstants.EVENT_DONE, terminal);
    }

    public synchronized void emit(String type, Object data) {
        apply(type, data, true);
    }

    public synchronized void replay(long seq, String type, Object data) {
        if (seq <= lastPersistedSeq) {
            return;
        }
        apply(type, data, false);
        lastPersistedSeq = seq;
    }

    private void apply(String type, Object data, boolean persist) {
        if (done != null) {
            return;
        }
        if (AiSqlConstants.EVENT_TOOL_STARTED.equals(type) || AiSqlConstants.EVENT_TOOL_COMPLETED.equals(type)) {
            Map<String, Object> value = new LinkedHashMap<>();
            ((Map<?, ?>) data).forEach((key, item) -> value.put((String) key, item));
            if (AiSqlConstants.EVENT_TOOL_STARTED.equals(type) && !draft.isEmpty()) {
                value.put(AiSqlConstants.FIELD_TEXT, draft.toString());
            }
            data = value;
        }
        // Cancellation can reject the journal write; rejected events must never enter the replay snapshot.
        if (persist) {
            recorder.accept(type, data);
        }
        if (AiSqlConstants.EVENT_TEXT_DELTA.equals(type)) {
            draft.append(((Map<?, ?>) data).get(AiSqlConstants.FIELD_TEXT));
        } else if (AiSqlConstants.EVENT_PHASE.equals(type)) {
            Map<?, ?> value = (Map<?, ?>) data;
            phase = (String) value.get(AiSqlConstants.FIELD_PHASE);
            if (value.containsKey(AiSqlConstants.FIELD_CONTEXT_USAGE)) {
                contextUsage = value.get(AiSqlConstants.FIELD_CONTEXT_USAGE);
            }
        } else if (AiSqlConstants.EVENT_TOOL_STARTED.equals(type) || AiSqlConstants.EVENT_TOOL_COMPLETED.equals(type)) {
            Map<String, Object> value = (Map<String, Object>) data;
            if (AiSqlConstants.EVENT_TOOL_STARTED.equals(type)) {
                // Text preceding tool calls belongs to the activity, not the final answer.
                draft.setLength(0);
            }
            String id = (String) value.get(AiSqlConstants.FIELD_TOOL_CALL_ID);
            activities.computeIfAbsent(id, ignored -> new LinkedHashMap<>()).putAll(value);
        } else if (AiSqlConstants.EVENT_SNAPSHOT.equals(type)) {
            restore((Map<String, Object>) data);
        } else if (AiSqlConstants.EVENT_RESULT.equals(type)) {
            result = data;
        } else if (AiSqlConstants.EVENT_ERROR.equals(type)) {
            error = data;
        } else if (AiSqlConstants.EVENT_DONE.equals(type)) {
            done = data;
        }
        subscribers.values().removeIf(AiEventSink::disconnected);
        Object eventData = data;
        subscribers.values().forEach(sink -> sink.emit(type, eventData));
        if (done != null) {
            subscribers.clear();
        }
    }

    public synchronized String draft() {
        return draft.toString();
    }

    public synchronized List<String> generatedSql() {
        return activities.values()
            .stream()
            .filter(activity -> AiSqlConstants.TOOL_GENERATE_SQL.equals(activity.get(AiSqlConstants.FIELD_TOOL_NAME))
                                && AiSqlConstants.TOOL_STATUS_OK.equals(activity.get(AiSqlConstants.FIELD_STATUS)))
            .map(activity -> (String) ((Map<?, ?>) activity.get(AiSqlConstants.FIELD_RESULT)).get(AiSqlConstants.FIELD_SQL))
            .toList();
    }

    public synchronized void replaceDraft(String text) {
        if (done != null) {
            return;
        }
        Map<String, Object> state = snapshot();
        state.put(AiSqlConstants.FIELD_DRAFT, text);
        emit(AiSqlConstants.EVENT_SNAPSHOT, state);
    }

    public synchronized Map<String, Object> snapshot() {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(AiSqlConstants.FIELD_DRAFT, draft.toString());
        value.put(AiSqlConstants.FIELD_PHASE, phase);
        value.put(AiSqlConstants.FIELD_ACTIVITIES, activities.values().stream().map(LinkedHashMap::new).toList());
        value.put(AiSqlConstants.FIELD_CONTEXT_USAGE, contextUsage);
        return value;
    }

    public synchronized void restore(Map<String, Object> state) {
        draft.setLength(0);
        draft.append((String) state.get(AiSqlConstants.FIELD_DRAFT));
        phase = (String) state.get(AiSqlConstants.FIELD_PHASE);
        contextUsage = state.get(AiSqlConstants.FIELD_CONTEXT_USAGE);
        activities.clear();
        for (Map<String, Object> activity : (List<Map<String, Object>>) state.get(AiSqlConstants.FIELD_ACTIVITIES)) {
            activities.put((String) activity.get(AiSqlConstants.FIELD_TOOL_CALL_ID), new LinkedHashMap<>(activity));
        }
    }

    public synchronized void disconnect() {
        subscribers.values().forEach(AiEventSink::disconnect);
        subscribers.clear();
    }
}
