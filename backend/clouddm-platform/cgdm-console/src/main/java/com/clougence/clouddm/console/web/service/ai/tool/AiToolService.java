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
package com.clougence.clouddm.console.web.service.ai.tool;

import java.util.*;
import java.util.concurrent.CancellationException;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.service.ai.AiConversation;
import com.clougence.clouddm.console.web.service.ai.AiMetadataExecutor;
import com.clougence.clouddm.console.web.service.ai.AiPermit;
import com.clougence.clouddm.console.web.service.ai.AiRunHandle;
import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.ds.ai.ChatToolArguments;
import com.clougence.clouddm.ds.ai.ChatToolCall;
import com.clougence.clouddm.ds.ai.tool.ChatToolConstants;
import com.clougence.clouddm.ds.ai.tool.ChatToolExecutor;
import com.clougence.clouddm.ds.ai.tool.ChatToolOutcome;
import com.clougence.clouddm.ds.ai.tool.ChatToolRegistry;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AiToolService {
    @Resource(name = "getValidator")
    private Validator                 validator;
    @Resource
    private AiMetadataTools           metadata;
    @Resource
    private AiMetadataExecutor        executor;
    private List<Map<String, Object>> definitions;
    private List<Map<String, Object>> localDefinitions;

    @PostConstruct
    public void initialize() {
        List<Map<String, Object>> local = new ArrayList<>(ChatToolRegistry.definitionsFrom(AiGenerateSqlTool.class));
        local.addAll(ChatToolRegistry.definitionsFrom(AiAskUserQuestionsTool.class));
        localDefinitions = List.copyOf(local);
        List<Map<String, Object>> all = new ArrayList<>(localDefinitions);
        all.addAll(ChatToolRegistry.definitionsFrom(AiSqlTools.class));
        definitions = List.copyOf(all);
    }

    public ChatToolRegistry createContext(AiMetadataContext context, AiRunHandle handle) {
        ChatToolRegistry registry = new ChatToolRegistry(validator);
        registry.register(new AiGenerateSqlTool());
        registry.register(new AiAskUserQuestionsTool());
        if (context.getLevels() != null) {
            registry.register(new AiSqlTools(metadata, context, handle));
        }
        return registry;
    }

    public List<Map<String, Object>> definitions(AiMetadataContext context) {
        if (context.getLevels() == null) {
            return localDefinitions;
        }
        return definitions;
    }

    public boolean validateBatch(List<ChatToolCall> calls, AiConversation conversation) {
        Set<String> ids = new LinkedHashSet<>();
        for (ChatToolCall call : calls) {
            if (!ids.add(call.getId())) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
        }
        if (calls.size() > 1 && calls.stream().anyMatch(call -> AiSqlConstants.TOOL_ASK_USER_QUESTIONS.equals(call.getName()))) {
            for (ChatToolCall call : calls) {
                conversation.addToolResult(call, ChatToolConstants.ERROR_PREFIX + AiSqlConstants.TOOL_ASK_USER_QUESTIONS
                                                 + " must be the only tool call in its assistant turn. No tool in this batch was executed; plan a new batch.");
            }
            return false;
        }
        return true;
    }

    public ChatToolOutcome execute(ChatToolRegistry registry, ChatToolCall call, AiRunHandle handle, AiPermit permit) {
        if (AiSqlConstants.TOOL_GENERATE_SQL.equals(call.getName()) || AiSqlConstants.TOOL_ASK_USER_QUESTIONS.equals(call.getName())) {
            return execute(registry, call);
        }
        return executor.submit(handle, permit, () -> execute(registry, call)).join();
    }

    private ChatToolOutcome execute(ChatToolRegistry registry, ChatToolCall call) {
        ChatToolExecutor executor = registry.executor(call.getName());
        if (executor == null) {
            return ChatToolOutcome.error("Tool not found: " + call.getName() + ". Use a registered tool name.");
        }
        try {
            Object value = executor.execute(ChatToolArguments.parse(call.getArguments()));
            String text = registry.resultText(value);
            return new ChatToolOutcome(!text.startsWith(ChatToolConstants.ERROR_PREFIX), text, value);
        } catch (CancellationException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("AI tool arguments rejected: toolCallId={}, toolName={}", call.getId(), call.getName(), e);
            return ChatToolOutcome.error(e.getMessage());
        } catch (ErrorMessageException e) {
            // Authorization and lifecycle failures remain terminal; ordinary tool failures are returned to the model.
            if (!AiError.METADATA_FAILED.name().equals(e.getErrorCode())) {
                throw e;
            }
            log.error("AI tool failed: {}", call.getName(), e);
            return ChatToolOutcome.error(AiError.METADATA_FAILED.message());
        } catch (Exception e) {
            log.error("AI tool failed: {}", call.getName(), e);
            return ChatToolOutcome.error(AiError.METADATA_FAILED.message());
        }
    }
}
