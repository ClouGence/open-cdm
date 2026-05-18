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
package com.clougence.clouddm.init.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator.OverflowStrategy;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.clougence.clouddm.init.component.log.InitMysqlDriverProgressBus;
import com.clougence.clouddm.init.component.log.InstallUpgradeLogEvent;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("init")
public class InitMysqlDriverWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Consumer<InstallUpgradeLogEvent>> listeners = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        WebSocketSession decorated = new ConcurrentWebSocketSessionDecorator(session, 10000, 4 * 1024 * 1024, OverflowStrategy.DROP);
        Consumer<InstallUpgradeLogEvent> listener = event -> writeEvent(decorated, event);
        listeners.put(sessionId, listener);
        InitMysqlDriverProgressBus.addListener(listener);
        writeEvent(decorated, InitMysqlDriverProgressBus.snapshotEvent());
        log.info("[InitMysqlDriverWebSocketHandler] WebSocket connected, sessionId={}", sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        cleanup(session.getId());
        log.info("[InitMysqlDriverWebSocketHandler] WebSocket closed, sessionId={}, status={}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.warn("[InitMysqlDriverWebSocketHandler] WebSocket transport error, sessionId={}, msg={}", session.getId(), exception.getMessage());
        cleanup(session.getId());
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private void cleanup(String sessionId) {
        Consumer<InstallUpgradeLogEvent> listener = listeners.remove(sessionId);
        if (listener != null) {
            InitMysqlDriverProgressBus.removeListener(listener);
        }
    }

    private void writeEvent(WebSocketSession session, InstallUpgradeLogEvent event) {
        if (event == null || session == null || !session.isOpen()) {
            return;
        }

        try {
            session.sendMessage(new TextMessage(JsonUtils.toJson(event)));
        } catch (Exception e) {
            log.warn("[InitMysqlDriverWebSocketHandler] Failed to send driver event, sessionId={}, msg={}", session.getId(), e.getMessage());
        }
    }
}
