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

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.util.ClientDisconnectUtils;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AiEventSink {
    public static final String             REQUEST_ATTRIBUTE = AiEventSink.class.getName();
    private final String                   sessionId;
    private final String                   turnId;
    private final String                   requestId;
    private final AiRunHandle              handle;
    private final SseEmitter               emitter;
    private final ArrayDeque<AiEventFrame> queue             = new ArrayDeque<>();
    private final CountDownLatch           ready             = new CountDownLatch(1);
    private final AtomicBoolean            disconnected      = new AtomicBoolean();
    private final AtomicBoolean            writerFinished    = new AtomicBoolean();
    private volatile Runnable              abortTransport    = () -> {
                                                             };
    private volatile long                  writeStarted;
    private long                           lastHeartbeat     = System.nanoTime();
    private long                           sequence;
    private boolean                        finishing;
    private boolean                        closed;

    public AiEventSink(String sessionId, String turnId, String requestId, AiRunHandle handle){
        this.sessionId = sessionId;
        this.turnId = turnId;
        this.requestId = requestId;
        this.handle = handle;
        emitter = new SseEmitter(handle.remainingMillis());
        emitter.onTimeout(() -> disconnect());
        emitter.onError(error -> disconnect());
        emitter.onCompletion(() -> {
            synchronized (this) {
                if (finishing) {
                    return;
                }
            }
            disconnect();
        });
    }

    public SseEmitter emitter() {
        return emitter;
    }

    public void ready(Runnable transportAbort) {
        abortTransport = transportAbort;
        ready.countDown();
        if (disconnected.get()) {
            try {
                transportAbort.run();
            } catch (IllegalStateException ignored) {
                // Async completion may have won the race with MVC initialization.
            }
        }
    }

    public synchronized void emit(String type, Object data) {
        if (closed || finishing) {
            return;
        }
        long seq = ++sequence;
        Map<String, Object> event = new LinkedHashMap<>();
        event.put(AiSqlConstants.FIELD_SESSION_ID, sessionId);
        event.put(AiSqlConstants.FIELD_TURN_ID, turnId);
        event.put(AiSqlConstants.FIELD_REQUEST_ID, requestId);
        event.put(AiSqlConstants.FIELD_SEQ, seq);
        event.put(AiSqlConstants.FIELD_TYPE, type);
        event.put(AiSqlConstants.FIELD_DATA, data);
        String value = JsonUtils.toJson(event);
        queue.add(new AiEventFrame(turnId + ":" + seq, type, value));
        if (AiSqlConstants.EVENT_DONE.equals(type)) {
            finishing = true;
        }
        notifyAll();
    }

    public void write() {
        try {
            if (!ready.await(handle.remainingMillis(), TimeUnit.MILLISECONDS)) {
                disconnect();
                return;
            }
            while (true) {
                AiEventFrame frame;
                synchronized (this) {
                    while (queue.isEmpty() && !closed && !finishing) {
                        wait(1000);
                    }
                    if (closed || (finishing && queue.isEmpty())) {
                        break;
                    }
                    frame = queue.removeFirst();
                }
                writeStarted = System.nanoTime();
                try {
                    if (AiSqlConstants.EVENT_HEARTBEAT.equals(frame.getType())) {
                        emitter.send(SseEmitter.event().comment(AiSqlConstants.EVENT_HEARTBEAT));
                    } else {
                        emitter.send(SseEmitter.event().id(frame.getId()).name(frame.getType()).data(frame.getJson(), MediaType.APPLICATION_JSON));
                    }
                } finally {
                    writeStarted = 0;
                }
            }
            // Only the writer calls complete: never wait for its send lock on a timer/control thread.
            emitter.complete();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            disconnect();
        } catch (Exception e) {
            if (ClientDisconnectUtils.isClientDisconnect(e)) {
                log.info("AI stream client disconnected: sessionId={}, turnId={}, requestId={}", sessionId, turnId, requestId);
            } else {
                log.warn("AI stream write failed: sessionId={}, turnId={}, requestId={}", sessionId, turnId, requestId, e);
            }
            disconnect();
        } finally {
            writerFinished.set(true);
            synchronized (this) {
                closed = true;
                queue.clear();
            }
        }
    }

    public void tick() {
        // Cancellation must remain available when the database control worker is blocked.
        if (handle.expireIfDue()) {
            disconnect();
            return;
        }
        long now = System.nanoTime();
        if (writeStarted != 0 && now - writeStarted >= TimeUnit.SECONDS.toNanos(10)) {
            disconnect();
            return;
        }
        synchronized (this) {
            if (!closed && !finishing && now - lastHeartbeat >= TimeUnit.SECONDS.toNanos(10)) {
                queue.add(new AiEventFrame(null, AiSqlConstants.EVENT_HEARTBEAT, null));
                lastHeartbeat = now;
                notifyAll();
            }
        }
    }

    public void disconnect() {
        ready.countDown();
        synchronized (this) {
            closed = true;
            queue.clear();
            notifyAll();
        }
        if (disconnected.compareAndSet(false, true)) {
            try {
                abortTransport.run();
            } catch (IllegalStateException ignored) {
                // The container may already have completed the async request.
            }
        }
    }

    public boolean disconnected() {
        return disconnected.get();
    }

    public boolean writerFinished() {
        return writerFinished.get();
    }
}
