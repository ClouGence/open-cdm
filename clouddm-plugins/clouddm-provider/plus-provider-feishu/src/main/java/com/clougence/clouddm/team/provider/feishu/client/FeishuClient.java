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

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.lark.oapi.Client;
import com.lark.oapi.core.exception.ObtainAccessTokenException;
import com.lark.oapi.core.request.EventReq;
import com.lark.oapi.event.CustomEventHandler;
import com.lark.oapi.event.EventDispatcher;

import lombok.Getter;

@Getter
public class FeishuClient implements Closeable {

    private final Logger         logger;
    private final String         appId;
    private final String         appSecret;
    private final int            apiTimeoutSec;
    private final FeishuApi      bindApi;

    //private com.lark.oapi.ws.Client wsClient;
    private WsClient             wsClient;
    private com.lark.oapi.Client apiClient;
    private FeishuEventHandler   approvalInstanceEventHandler;
    private FeishuEventHandler   approvalTaskEventHandler;

    public FeishuClient(Logger logger, String appId, String appSecret, int apiTimeoutSec, String roleMap){
        this.logger = (logger != null) ? logger : LoggerUtil.getLoggerAppender();
        this.appId = appId;
        this.appSecret = appSecret;
        this.apiTimeoutSec = apiTimeoutSec;
        this.approvalInstanceEventHandler = FeishuEventHandler.EMPTY;
        this.approvalTaskEventHandler = FeishuEventHandler.EMPTY;
        this.bindApi = new FeishuApi(this, roleMap);
    }

    public Client apiClient() {
        return this.apiClient;
    }

    public void start(boolean includeWS) {
        int timeout = this.apiTimeoutSec <= 0 ? 5 : this.apiTimeoutSec;
        this.apiClient = Client.newBuilder(this.appId, this.appSecret).requestTimeout(timeout, TimeUnit.SECONDS).build();

        if (includeWS) {
            EventDispatcher build = EventDispatcher.newBuilder("", "")//
                .onCustomizedEvent("approval_instance", new CustomEventHandler() {

                    @Override
                    public void handle(EventReq event) {
                        customInstanceHandler(event);
                    }
                })
                .onCustomizedEvent("approval_task", new CustomEventHandler() {

                    @Override
                    public void handle(EventReq event) {
                        customTaskHandler(event);
                    }
                })
                .build();

            //this.wsClient = new com.lark.oapi.ws.Client.Builder(this.appId, this.appSecret)//
            //    .autoReconnect(true)
            //    .eventHandler(build)
            //    .build();
            this.wsClient = new WsClient.Builder(this.appId, this.appSecret)//
                .autoReconnect(true)
                .eventHandler(build)
                .classLoader(this.getClass().getClassLoader())
                .build(logger);
            this.wsClient.start();
        }
    }

    @Override
    public void close() {
        FeishuClientKiller.close(this.logger, this.wsClient);
        FeishuClientKiller.close(this.logger, this.apiClient);
        this.wsClient = null;
        this.apiClient = null;
    }

    private void customTaskHandler(EventReq event) {
        this.approvalTaskEventHandler.onEvent(event);
    }

    private void customInstanceHandler(EventReq event) {
        this.approvalInstanceEventHandler.onEvent(event);
    }

    public void setApprovalInstanceEventHandler(FeishuEventHandler approvalInstanceEventHandler) { this.approvalInstanceEventHandler = approvalInstanceEventHandler; }

    public void setApprovalTaskEventHandler(FeishuEventHandler approvalTaskEventHandler) { this.approvalTaskEventHandler = approvalTaskEventHandler; }

    public <T> T callApi(FeishuApiCaller<T> caller) {
        return callApi(caller, 1);
    }

    private <T> T callApi(FeishuApiCaller<T> caller, int count) {
        try {
            return caller.call(this);
        } catch (IOException e) {
            throw ThirdPartyApiException.as().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, FeishuI18nKeys2.FEISHU_CONNECTION_ERROR, exceptionDescription(e));
        } catch (CallApiException e) {
            if (e.getCode() == 99991400 && count < 3) {
                return callApi(caller, count + 1);
            }
            handleCallApiException(e);
            throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_UNKNOWN_CALL_API_ERROR, e.getMessage());
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (ObtainAccessTokenException e) {
            // appid or secret error
            throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_APP_CONFIG_ERROR);
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_UNKNOWN_CALL_API_ERROR, e.getMessage());
        }
    }

    private void handleCallApiException(CallApiException e) {
        switch (e.getCode()) {
            case 99991400: {
                throw ThirdPartyApiException.as().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, FeishuI18nKeys2.FEISHU_REQUEST_FREQUENCY);
            }
            case 1390002:
            case 1390016:
            case 1390015: {
                throw ThirdPartyApiException.as().with(e, ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS, FeishuI18nKeys2.FEISHU_APPROVAL_TEMPLATE_NOT_EXIST);
            }
            case 1390003: {
                throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_APPROVAL_INSTANCE_NOT_EXIST);
            }
            case 99991672: {
                throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_API_ACCESS_DENY);
            }
            case 99991403: {
                throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_API_CALL_COUNT_LIMIT);
            }
            case 99991401: {
                throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_API_CALL_IP_DENY, e.getMessage().split(" ")[1]);
            }
            default: {
                throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_UNKNOWN_CALL_API_ERROR, e.getMessage());
            }
        }
    }

    private static String exceptionDescription(Throwable e) {
        if (e == null) {
            return "";
        }
        String message = e.getMessage();
        return message == null ? e.getClass().getName() : e.getClass().getName() + ": " + message;
    }
}
