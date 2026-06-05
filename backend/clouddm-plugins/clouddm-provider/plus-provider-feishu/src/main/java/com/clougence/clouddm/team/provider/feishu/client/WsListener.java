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
package com.clougence.clouddm.team.provider.feishu.client;

import static com.lark.oapi.ws.Constant.*;

import org.slf4j.Logger;

import com.lark.oapi.core.utils.Strings;
import com.lark.oapi.okhttp.Response;
import com.lark.oapi.okhttp.WebSocket;
import com.lark.oapi.okhttp.WebSocketListener;
import com.lark.oapi.okio.ByteString;
import com.lark.oapi.ws.exception.ClientException;
import com.lark.oapi.ws.exception.ServerException;

import lombok.Getter;

@Getter
public class WsListener extends WebSocketListener {

    private final Logger   log;
    private final WsClient cli;

    public WsListener(Logger log, WsClient cli){
        this.log = log;
        this.cli = cli;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.cli.conn = webSocket;
        log.info(cli.fmtLog("connected to %s", this.cli.connUrl));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        this.cli.executor.execute(() -> cli.handleMessage(bytes.toByteArray()));
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        this.cli.disconnect();
        if (this.cli.autoReconnect) {
            this.cli.reconnect();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        try {
            if (response == null) {
                throw t;
            }
            String code = response.header(HEADER_HANDSHAKE_STATUS);
            String msg = response.header(HEADER_HANDSHAKE_MSG);
            if (Strings.isEmpty(code) || Strings.isEmpty(msg)) {
                throw t;
            }

            int c = Integer.parseInt(code);
            switch (c) {
                case AUTH_FAILED:
                    String authCode = response.header(HEADER_HANDSHAKE_AUTH_ERRCODE);
                    if (String.valueOf(EXCEED_CONN_LIMIT).equals(authCode)) {
                        throw new ClientException(c, msg);
                    } else {
                        throw new ServerException(c, msg);
                    }
                case FORBIDDEN:
                    throw new ClientException(c, msg);
                default:
                    throw new ServerException(c, msg);
            }
        } catch (ClientException e) {
            log.error(e.toString());
        } catch (Throwable e) {
            log.error(e.toString());
            if (this.cli.isReconnecting) {
                return;
            }
            this.cli.disconnect();
            if (this.cli.autoReconnect) {
                this.cli.reconnect();
            }
        }
    }
}
