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
package com.clougence.clouddm.console.web.global.ws;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.clougence.clouddm.console.web.global.jwtsession.WebSoInterceptor;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.format.WellKnowFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/16 16:36
 */
@Slf4j
public class WsUtils {

    public static String getChannelKey(WebSocketSession ws) {
        return ws.getHandshakeHeaders().get("Sec-Websocket-Key").get(0);
    }

    public static void writeToSocket(WebSocketSession ws, WsType type, Object data) {
        if (ws == null) {
            return;
        }

        String uid = (String) ws.getAttributes().get(WebSoInterceptor.WS_USER_ID);
        String wsMessage = "WS[" + getChannelKey(ws) + "]:SEND from user(" + uid + ") to console, type:" + type.getCode();

        if (ws.isOpen()) {
            try {
                Map<String, Object> wsMsg = new HashMap<>();
                wsMsg.put("time", WellKnowFormat.WKF_DATE_TIME24.now());
                wsMsg.put("object", data);
                wsMsg.put("type", type.name());
                TextMessage textMessage = new TextMessage(JsonUtils.toJson(wsMsg));
                ws.sendMessage(textMessage);

                log.info(wsMessage + " done.");
            } catch (Exception e) {
                if (e.getMessage().contains("The transformer has been closed and may no longer be used")) {
                    log.error(wsMessage + " sendMessage failed " + e.getMessage());
                } else {
                    log.error(wsMessage + " failed " + e.getMessage(), e);
                }
            }
        } else {
            log.info(wsMessage + " ignore(isOpen=false)");
        }
    }
}
