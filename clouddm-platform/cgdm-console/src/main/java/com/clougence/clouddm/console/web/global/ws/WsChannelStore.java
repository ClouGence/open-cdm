package com.clougence.clouddm.console.web.global.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator.OverflowStrategy;

import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.session.WebSoInterceptor;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsResMsg;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryApi;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class WsChannelStore {

    private final String                        uid;
    private final ConsoleQueryApi               queryServiceApi;
    private final Map<String, WebSocketSession> channelMap = new ConcurrentHashMap<>();

    public WsChannelStore(String uid, ConsoleQueryApi queryServiceApi){
        this.uid = uid;
        this.queryServiceApi = queryServiceApi;
    }

    public boolean containsChannel(String channelKey) {
        return this.channelMap.containsKey(channelKey);
    }

    public void acceptChannel(String channelKey, WebSocketSession ws) {
        OverflowStrategy strategy = OverflowStrategy.DROP;
        int sendTimeLimit = 10000;
        int bufferSizeLimit = 4 * 1024 * 1024;

        log.info("WS[" + channelKey + "]:CONNECTED user(" + uid + ")");
        this.channelMap.put(channelKey, new ConcurrentWebSocketSessionDecorator(ws, sendTimeLimit, bufferSizeLimit, strategy));
    }

    public void closeChannel(String channelKey) {
        log.info("WS[" + channelKey + "]:REMOVE user(" + uid + ")");
        this.channelMap.remove(channelKey);
    }

    public void handleMessage(WebSocketSession ws, WsMsg reqMsg) {
        String channelKey = reqMsg.getChannelKey();

        //Objects.requireNonNull(body, "missing request info.");
        log.info("WS[" + channelKey + "]:RECEIVE user(" + uid + "), type:" + reqMsg.getType().getCode());

        switch (reqMsg.getType()) {
            case WS_REQ_ECHO:
                WsUtils.writeToSocket(ws, WsType.WS_RES_ECHO, reqMsg.getObject());
                return;
            case WS_REQ_QUERY:
                WsQueryFO queryFO = JSONObject.parseObject(reqMsg.getObject(), WsQueryFO.class);
                queryFO.setChannelKey(channelKey);
                queryFO.setPrimaryUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_PUSER_ID));
                queryFO.setCurrentUserId((String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID));
                queryFO.setRequestTime(System.currentTimeMillis());
                queryFO.setClientIp(getHost(ws));
                this.queryServiceApi.offerQueryRequest(queryFO, DmGlobalEventBus::triggerQueryResultEvent);
                return;
            default:
                throw new UnsupportedOperationException("Request WsType '" + reqMsg.getType() + "' Unsupported.");
        }
    }

    private String getHost(WebSocketSession ws) {
        //aliyun  slb
        String host = ws.getHandshakeHeaders().getFirst("X-Forwarded-For");
        if (!StringUtils.isEmpty(host)) {
            return host;
        }

        return ws.getRemoteAddress().getHostString();
    }

    public void broadcastWrite(WsType type, Object data) {
        channelMap.forEach((channelKey, ws) -> WsUtils.writeToSocket(ws, type, data));
        log.info("WS[Broadcast]:SEND user(" + uid + "), type:" + type);
    }

    public void directWrite(String channelKey, WsType type, WsResMsg data) {
        WebSocketSession ws = channelMap.get(channelKey);
        if (ws != null) {
            WsUtils.writeToSocket(ws, type, data);
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type);
        } else {
            log.info("WS[" + channelKey + "]:SEND user(" + uid + "), type:" + type + ", Drop.");
        }
    }
}
