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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lark.oapi.core.enums.BaseUrlEnum;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.google.protobuf.ByteString;
import com.lark.oapi.okhttp.*;
import com.lark.oapi.ws.enums.FrameType;
import com.lark.oapi.ws.enums.MessageType;
import com.lark.oapi.ws.exception.ClientException;
import com.lark.oapi.ws.exception.HeaderNotFoundException;
import com.lark.oapi.ws.exception.ServerException;
import com.lark.oapi.ws.exception.ServerUnreachableException;
import com.lark.oapi.ws.model.ClientConfig;
import com.lark.oapi.ws.model.Endpoint;
import com.lark.oapi.ws.model.EndpointResp;
import com.lark.oapi.ws.pb.Pbbp2;

import lombok.Getter;

// 1. 代码 100% 拷贝自 com.lark.oapi.ws.Client
// 2. 改动点 增加 inited 字段并在 start 时做初始化 pingLoop 的循环判断依靠它

@Getter
public class WsClient {

    private final Logger           log;
    public AtomicBoolean           inited   = new AtomicBoolean(false);
    public final ExecutorService   executor = Executors.newFixedThreadPool(//
            Runtime.getRuntime().availableProcessors() * 2 + 1);
    public Boolean                 autoReconnect;
    protected WebSocket            conn;
    protected String               connUrl;
    protected volatile Boolean     isReconnecting;
    private String                 appId;
    private String                 appSecret;
    private EventDispatcher        eventHandler;
    private ClassLoader            classLoader;
    private String                 domain;
    private String                 serviceId;
    private String                 connId;
    private Integer                reconnectNonce;
    private Integer                reconnectCount;
    private Integer                reconnectInterval;
    private Integer                pingInterval;
    public OkHttpClient            httpClient;
    public Cache<String, byte[][]> cache;

    private WsClient(Logger log, WsClient.Builder builder){
        this.log = log;
        this.appId = builder.appId;
        this.appSecret = builder.appSecret;
        this.eventHandler = builder.eventHandler;
        this.autoReconnect = builder.autoReconnect != null ? builder.autoReconnect : true;
        this.domain = builder.domain != null ? builder.domain : BaseUrlEnum.FeiShu.getUrl();
        this.reconnectNonce = 30;
        this.reconnectCount = -1;
        this.reconnectInterval = 120;
        this.pingInterval = 120;
        this.httpClient = new OkHttpClient();
        this.isReconnecting = false;
        this.classLoader = builder.classLoader;
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    }

    public void start() {
        if (this.inited.compareAndSet(false, true)) {
            try {
                this.connect();
            } catch (ClientException e) {
                log.error(e.toString());
                throw e;
            } catch (Throwable t) {
                log.error(t.toString());
                this.disconnect();
                if (this.autoReconnect) {
                    this.reconnect();
                }
            }
            this.executor.execute(this::pingLoop);
        }
    }

    private void pingLoop() {
        if (this.classLoader != null) {
            Thread.currentThread().setContextClassLoader(this.classLoader);
        }
        while (this.inited.get()) {
            try {
                if (this.conn != null) {
                    Pbbp2.Frame frame = newPingFrame(Integer.parseInt(this.serviceId));
                    this.conn.send(com.lark.oapi.okio.ByteString.of(frame.toByteArray()));
                    log.debug(fmtLog("ping success"));
                }
            } catch (Throwable t) {
                log.warn(fmtLog("ping failed"), t);
            } finally {
                this.sleep(this.pingInterval * 1000);
            }
        }
    }

    public synchronized void disconnect() {
        try {
            if (this.conn == null) {
                return;
            }
            this.conn.close(1000, "client closed");
            log.info(fmtLog("disconnected to %s", this.connUrl));
        } finally {
            this.conn = null;
            this.connUrl = null;
            this.connId = null;
            this.serviceId = null;
        }
    }

    protected void reconnect() {
        this.isReconnecting = true;

        try {
            log.info("start reconnecting...");
            // 首次重连随机抖动
            if (this.reconnectNonce > 0) {
                Random rand = new Random();
                int nonce = rand.nextInt(this.reconnectNonce * 1000);
                this.sleep(nonce);
            }

            // 重连
            if (this.reconnectCount >= 0) {
                for (int i = 0; i < this.reconnectCount; i++) {
                    if (this.conn != null) {
                        return;
                    }
                    if (tryConnect(i)) {
                        return;
                    }
                    this.sleep(this.reconnectInterval * 1000);
                }
                throw new ServerUnreachableException(String.format("unable to connect to the server after trying %d times", this.reconnectCount));
            } else {
                int i = 0;
                while (true) {
                    if (this.conn != null) {
                        return;
                    }
                    if (tryConnect(i)) {
                        return;
                    }
                    this.sleep(this.reconnectInterval * 1000);
                    i++;
                }
            }
        } finally {
            this.isReconnecting = false;
        }
    }

    private boolean tryConnect(int cnt) {
        cnt++;
        String time;
        switch (cnt) {
            case 1:
                time = cnt + "st";
                break;
            case 2:
                time = cnt + "nd";
                break;
            case 3:
                time = cnt + "rd";
                break;
            default:
                time = cnt + "th";
                break;
        }

        log.info(fmtLog("trying to reconnect for the %s time", time));
        try {
            connect();
            return true;
        } catch (ClientException e) {
            log.error(e.toString());
            throw e;
        } catch (Throwable t) {
            log.error(t.toString());
            return false;
        }
    }

    private String getConnUrl() throws IOException {
        String body = String.format("{\"AppID\": \"%s\", \"AppSecret\": \"%s\"}", this.appId, this.appSecret);
        Request request = new Request.Builder().url(this.domain + GEN_ENDPOINT_URI)
            .addHeader("locale", "zh")
            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
            .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.code() != 200 || response.body() == null) {
                throw new ServerException(response.code(), "system busy");
            }

            EndpointResp resp = Jsons.DEFAULT.fromJson(response.body().string(), EndpointResp.class);
            if (resp.getCode() == OK) {
                // do nothing
            } else if (resp.getCode() == SYSTEM_BUSY) {
                throw new ServerException(resp.getCode(), "system busy");
            } else if (resp.getCode() == INTERNAL_ERROR) {
                throw new ServerException(resp.getCode(), resp.getMsg());
            } else {
                throw new ClientException(resp.getCode(), resp.getMsg());
            }

            Endpoint data = resp.getData();
            if (data.getClientConfig() != null) {
                this.configure(data.getClientConfig());
            }

            return data.getUrl();
        }
    }

    private synchronized void connect() throws IOException {
        if (this.conn != null) {
            return;
        }
        this.connUrl = this.getConnUrl();
        HttpUrl u = parseUrl(this.connUrl);
        this.connId = u.queryParameter(DEVICE_ID);
        this.serviceId = u.queryParameter(SERVICE_ID);

        Request request = new Request.Builder().url(connUrl).build();
        this.httpClient.newWebSocket(request, new WsListener(log, this));
    }

    protected void handleMessage(byte[] msg) {
        try {
            Pbbp2.Frame frame = Pbbp2.Frame.parseFrom(msg);
            FrameType ft = FrameType.of(frame.getMethod());

            switch (ft) {
                case CONTROL:
                    handleControlFrame(frame);
                    break;
                case DATA:
                    handleDataFrame(frame);
                    break;
            }
        } catch (Exception e) {
            log.error(fmtLog("handle message failed"), e);
        }
    }

    private void handleControlFrame(Pbbp2.Frame frame) {
        List<Pbbp2.Header> hs = frame.getHeadersList();
        MessageType mt = MessageType.of(getString(hs, HEADER_TYPE));

        switch (mt) {
            case PING:
                return;
            case PONG:
                log.debug(fmtLog("receive pong"));
                if (!frame.hasPayload()) {
                    return;
                }
                ClientConfig conf = Jsons.DEFAULT.fromJson(frame.getPayload().toStringUtf8(), ClientConfig.class);
                configure(conf);
                break;
        }
    }

    private void handleDataFrame(Pbbp2.Frame frame) {
        List<Pbbp2.Header> hs = frame.getHeadersList();
        String msgId = getString(hs, HEADER_MESSAGE_ID);
        String traceId = getString(hs, HEADER_TRACE_ID);
        Integer sum = getInteger(hs, HEADER_SUM);
        Integer seq = getInteger(hs, HEADER_SEQ);
        String type = getString(hs, HEADER_TYPE);

        byte[] pl = frame.getPayload().toByteArray();
        if (sum > 1) {
            // 合包
            pl = combine(msgId, sum, seq, pl);
            if (pl == null) {
                return;
            }
        }

        MessageType mt = MessageType.of(type);
        log.debug(fmtLog("receive message, message_type: %s, message_id: %s, trace_id: %s, payload: %s", mt.getName(), msgId, traceId, new String(pl, StandardCharsets.UTF_8)));

        com.lark.oapi.ws.model.Response response = new com.lark.oapi.ws.model.Response(200);
        long start = System.currentTimeMillis();
        try {
            switch (mt) {
                case EVENT:
                    Object r = this.eventHandler.doWithoutValidation(pl);
                    if (r != null) {
                        response.setData(Jsons.DEFAULT.toJson(r).getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                case CARD:
                    return;
            }
        } catch (Throwable e) {
            log.error(fmtLog("handle message failed, message_type: %s, message_id: %s, trace_id: %s,", mt.getName(), msgId, traceId), e);
            response = new com.lark.oapi.ws.model.Response(500);
        }
        long end = System.currentTimeMillis();

        byte[] resp = Jsons.DEFAULT.toJson(response).getBytes(StandardCharsets.UTF_8);
        byte[] bs = frame.toBuilder()
            .setPayload(ByteString.copyFrom(resp))
            .addHeaders(Pbbp2.Header.newBuilder().setKey(HEADER_BIZ_RT).setValue(String.valueOf(end - start)).build())
            .build()
            .toByteArray();
        this.conn.send(com.lark.oapi.okio.ByteString.of(bs));
    }

    private byte[] combine(String msgId, int sum, int seq, byte[] bs) {
        byte[][] val = this.cache.getIfPresent(msgId);
        if (val == null) {
            byte[][] buf = new byte[sum][];
            buf[seq] = bs;
            this.cache.put(msgId, buf);
            return null;
        }

        val[seq] = bs;
        ByteArrayOutputStream pl = new ByteArrayOutputStream();
        for (byte[] v : val) {
            if (v == null) {
                this.cache.put(msgId, val);
                return null;
            }
            try {
                pl.write(v);
            } catch (IOException ignored) {
            }
        }

        return pl.toByteArray();
    }

    private void configure(ClientConfig conf) {
        this.reconnectCount = conf.getReconnectCount();
        this.reconnectInterval = conf.getReconnectInterval();
        this.reconnectNonce = conf.getReconnectNonce();
        this.pingInterval = conf.getPingInterval();
    }

    private HttpUrl parseUrl(String url) {
        String httpUrl = url.replace("wss://", "https://").replace("ws://", "https://");
        HttpUrl u = HttpUrl.parse(httpUrl);
        if (u == null) {
            throw new ServerException(500, "connect url is invalid");
        }
        return u;
    }

    private String getString(List<Pbbp2.Header> headers, String key) {
        return headers.stream().filter(o -> o.getKey().equals(key)).findFirst().map(Pbbp2.Header::getValue).orElseThrow(() -> new HeaderNotFoundException(key));
    }

    private Integer getInteger(List<Pbbp2.Header> headers, String key) {
        return headers.stream()
            .filter(o -> o.getKey().equals(key))
            .findFirst()
            .map(Pbbp2.Header::getValue)
            .map(Integer::parseInt)
            .orElseThrow(() -> new HeaderNotFoundException(key));
    }

    protected String fmtLog(String format, Object... args) {
        String log = String.format(format, args);

        if (this.connId != null) {
            log += String.format(" [conn_id=%s]", this.connId);
        }

        return log;
    }

    private Pbbp2.Frame newPingFrame(int serviceId) {
        return Pbbp2.Frame.newBuilder()
            .setService(serviceId)
            .setMethod(FrameType.CONTROL.getCode())
            .addHeaders(Pbbp2.Header.newBuilder().setKey(HEADER_TYPE).setValue(MessageType.PING.getName()).build())
            .setSeqID(0)
            .setLogID(0)
            .build();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static class Builder {

        private String          appId;
        private String          appSecret;
        private EventDispatcher eventHandler;
        private Boolean         autoReconnect;
        private String          domain;
        private ClassLoader     classLoader;

        public Builder(String appId, String appSecret){
            this.appId = appId;
            this.appSecret = appSecret;
        }

        public WsClient.Builder eventHandler(EventDispatcher eventHandler) {
            this.eventHandler = eventHandler;
            return this;
        }

        public WsClient.Builder autoReconnect(Boolean autoReconnect) {
            this.autoReconnect = autoReconnect;
            return this;
        }

        public WsClient.Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public WsClient.Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public WsClient build(Logger log) {
            return new WsClient(log, this);
        }
    }
}
