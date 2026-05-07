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

import com.clougence.clouddm.init.component.log.InstallUpgradeLogBus;
import com.clougence.clouddm.init.component.log.InstallUpgradeLogEvent;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("init")
public class InitInstallLogWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession>                 sessions  = new ConcurrentHashMap<>();
    private final Map<String, Consumer<InstallUpgradeLogEvent>> listeners = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        WebSocketSession decorated = new ConcurrentWebSocketSessionDecorator(session, 10000, 4 * 1024 * 1024, OverflowStrategy.DROP);
        Consumer<InstallUpgradeLogEvent> listener = event -> writeEvent(decorated, event);
        sessions.put(sessionId, decorated);
        listeners.put(sessionId, listener);
        InstallUpgradeLogBus.addListener(listener);
        writeEvent(decorated, InstallUpgradeLogBus.snapshotEvent());
        writeEvent(decorated, InstallUpgradeLogBus.scriptSnapshotEvent());
        log.info("[InitInstallLogWebSocketHandler] WebSocket connected, sessionId={}", sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        cleanup(session.getId());
        log.info("[InitInstallLogWebSocketHandler] WebSocket closed, sessionId={}, status={}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.warn("[InitInstallLogWebSocketHandler] WebSocket transport error, sessionId={}, msg={}", session.getId(), exception.getMessage());
        cleanup(session.getId());
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private void cleanup(String sessionId) {
        Consumer<InstallUpgradeLogEvent> listener = listeners.remove(sessionId);
        if (listener != null) {
            InstallUpgradeLogBus.removeListener(listener);
        }
        sessions.remove(sessionId);
    }

    private void writeEvent(WebSocketSession session, InstallUpgradeLogEvent event) {
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            session.sendMessage(new TextMessage(JsonUtils.toJson(event)));
        } catch (Exception e) {
            log.warn("[InitInstallLogWebSocketHandler] Failed to send log event, sessionId={}, msg={}", session.getId(), e.getMessage());
        }
    }
}
