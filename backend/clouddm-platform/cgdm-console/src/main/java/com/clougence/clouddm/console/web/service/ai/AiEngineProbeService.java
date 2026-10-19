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

import java.util.*;
import java.util.concurrent.*;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineVersionFO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineTestVO;
import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.ds.ai.ChatResponse;
import com.clougence.clouddm.ds.ai.ChatToolArguments;
import com.clougence.clouddm.ds.ai.ChatToolCall;
import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.Resource;

@Service
public class AiEngineProbeService {
    @Resource
    private DmAuthServiceForBiz authorization;
    @Resource
    private AiEngineService     engines;
    @Resource
    private AiModelClient       client;
    @Resource
    private AiExecutors         executors;
    @Resource
    private AiCapacity          capacity;

    public CompletableFuture<AiEngineTestVO> test(String puid, String uid, AiEngineVersionFO fo, AiRunHandle handle) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        AiEngineConfigVO engine = engines.find(fo.getId());
        if (engine == null) {
            throw AiError.NOT_FOUND.exception();
        }
        if (engine.getVersion() != fo.getVersion()) {
            throw AiError.ENGINE_VERSION_CONFLICT.exception();
        }
        AiPermit permit = capacity.acquire(uid, engine.getId(), true);
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return CompletableFuture.supplyAsync(() -> {
                ScheduledFuture<?> timeout = null;
                try (permit) {
                    LocaleContextHolder.setLocale(locale);
                    timeout = executors.getTimer().schedule(handle::cancel, handle.remainingMillis(), TimeUnit.MILLISECONDS);
                    return probe(puid, uid, engine, handle);
                } finally {
                    LocaleContextHolder.resetLocaleContext();
                    if (timeout != null) {
                        timeout.cancel(false);
                    }
                }
            }, executors.getProvider());
        } catch (RejectedExecutionException e) {
            permit.close();
            throw AiError.AI_BUSY.exception();
        }
    }

    private AiEngineTestVO probe(String puid, String uid, AiEngineConfigVO engine, AiRunHandle handle) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        String nonce = UUID.randomUUID().toString();
        Map<String, Object> message = new LinkedHashMap<>();
        message.put(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER);
        message.put(AiSqlConstants.FIELD_CONTENT, "Call " + AiSqlConstants.TOOL_PROBE + " once with this nonce: " + nonce + ". Do not produce other output.");
        List<Map<String, Object>> messages = List.of(message);

        Map<String, Object> nonceSchema = new LinkedHashMap<>();
        nonceSchema.put(AiSqlConstants.FIELD_TYPE, AiSqlConstants.SCHEMA_TYPE_STRING);
        nonceSchema.put(AiSqlConstants.FIELD_CONST, nonce);

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(AiSqlConstants.FIELD_TYPE, AiSqlConstants.SCHEMA_TYPE_OBJECT);
        parameters.put(AiSqlConstants.FIELD_ADDITIONAL_PROPERTIES, false);
        parameters.put(AiSqlConstants.FIELD_PROPERTIES, Map.of(AiSqlConstants.FIELD_NONCE, nonceSchema));
        parameters.put(AiSqlConstants.FIELD_REQUIRED, List.of(AiSqlConstants.FIELD_NONCE));

        Map<String, Object> function = new LinkedHashMap<>();
        function.put(AiSqlConstants.FIELD_NAME, AiSqlConstants.TOOL_PROBE);
        function.put(AiSqlConstants.FIELD_DESCRIPTION, "Return the provided nonce.");
        function.put(AiSqlConstants.FIELD_PARAMETERS, parameters);

        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put(AiSqlConstants.FIELD_TYPE, AiSqlConstants.TYPE_FUNCTION);
        tool.put(AiSqlConstants.FIELD_FUNCTION, function);
        List<Map<String, Object>> tools = List.of(tool);
        AiVerificationStatus status = AiVerificationStatus.PASS;
        String errorCode = null;
        try {
            ChatResponse reply = client.stream(engine, messages, tools, null, handle, text -> {
            });
            if (reply.getTools().size() != 1) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            ChatToolCall call = reply.getTools().get(0);
            JsonNode arguments;
            try {
                arguments = ChatToolArguments.parse(call.getArguments());
            } catch (IllegalArgumentException e) {
                throw ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            if (!AiSqlConstants.TOOL_PROBE.equals(call.getName()) || arguments.size() != 1 || !nonce.equals(arguments.path(AiSqlConstants.FIELD_NONCE).asText())) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
        } catch (ThirdPartyApiException e) {
            status = AiVerificationStatus.FAIL;
            errorCode = e.getMessageKey();
        } catch (ErrorMessageException e) {
            status = AiVerificationStatus.FAIL;
            errorCode = e.getErrorCode();
        } catch (CancellationException e) {
            status = AiVerificationStatus.FAIL;
            errorCode = ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT;
        }
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        AiEngineConfigVO current = engines.verify(engine.getId(), engine.getVersion(), status);
        return new AiEngineTestVO(current.getVersion(), current.getVerificationStatus(), current.getVerifiedAt().toInstant(), errorCode);
    }
}
