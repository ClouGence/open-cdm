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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.ai.*;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputRequestVO;
import com.clougence.clouddm.console.web.service.ai.tool.AiMetadataContext;
import com.clougence.clouddm.console.web.service.ai.tool.AiToolService;
import com.clougence.clouddm.ds.ai.*;
import com.clougence.clouddm.ds.ai.tool.ChatToolOutcome;
import com.clougence.clouddm.ds.ai.tool.ChatToolRegistry;
import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiGenerationService {
    private static final int MAX_COMPACTION_ATTEMPTS     = 3;
    private static final int MAX_COMPLETION_GATE_RETRIES = 2;
    private static final int TOOL_WARNING_THRESHOLD      = 30;
    @Resource
    private AiPromptBuilder  prompts;
    @Resource
    private AiSessionService sessions;
    @Resource
    private AiTurnStore      store;
    @Resource
    private AiProperties     limits;
    @Resource
    private AiModelClient    client;
    @Resource
    private AiToolService    toolService;

    public AiTurnRecordVO generate(AiActiveRun run) {
        AiMetadataContext context = sessions.context(run.getPuid(), run.getUid(), run.getTurn());
        AiTurnContext turnContext = JsonUtils.toObj(run.getTurn().getContextJson(), AiTurnContext.class);
        AiConversation conversation = new AiConversation(prompts,
            limits.getContextTokens(),
            prompts.system(context.getLevels() != null, turnContext),
            run.getSession(),
            store.history(run.getSession().getSessionId()),
            turnContext,
            run.getTurn().getPrompt(),
            run.getTurn().getGmtCreate().toInstant());
        ChatToolRegistry registry = toolService.createContext(context, run.getHandle());
        AiGenerationState state = new AiGenerationState();
        for (int request = 0;; request++) {
            if (request > 0 && request >= limits.getMaxRequests() - 1 && !state.isFinalAnswer()) {
                state.setFinalAnswer(true);
                run.getEvents().replaceDraft("");
            }
            List<Map<String, Object>> definitions = List.of();
            if (!state.isFinalAnswer()) {
                definitions = registry.definitions();
            }
            ChatResponse reply = request(run, context, turnContext, conversation, definitions, state, request);
            if (continueAfterResponse(run, conversation, reply, state, request)) {
                continue;
            }
            if (reply.getTools().isEmpty()) {
                conversation.addAssistant(reply);
                return complete(run, context, conversation, AiSqlResult.fromText(run.getEvents().draft(), run.getEvents().generatedSql()), state.usage());
            }
            state.setCompletionGateRetries(0);
            int previousToolCalls = state.getToolCalls();
            conversation.addAssistant(reply);
            if (!toolService.validateBatch(reply.getTools(), conversation)) {
                continue;
            }
            AiSqlResult waiting = executeTools(run, context, conversation, registry, reply, state);
            if (waiting != null) {
                return complete(run, context, conversation, waiting, state.usage());
            }
            if (state.getToolCalls() / TOOL_WARNING_THRESHOLD > previousToolCalls / TOOL_WARNING_THRESHOLD && state.getToolCalls() < limits.getMaxToolCalls()) {
                state.setNextDirective(prompts.toolWarningPrompt(state.getToolCalls() / TOOL_WARNING_THRESHOLD * TOOL_WARNING_THRESHOLD));
            }
            if (state.getToolCalls() >= limits.getMaxToolCalls()) {
                state.setFinalAnswer(true);
                state.setNextDirective(prompts.toolLimitReachedPrompt());
                run.getEvents().replaceDraft("");
                log.warn("AI tool limit reached; composing final answer without tools: sessionId={}, turnId={}, toolCalls={}", run.getSession().getSessionId(), run.getTurn()
                    .getId(), state.getToolCalls());
            }
        }
    }

    private ChatResponse request(AiActiveRun run, AiMetadataContext context, AiTurnContext turnContext, AiConversation conversation, List<Map<String, Object>> definitions,
                                 AiGenerationState state, int request) {
        AiEngineConfigVO engine = check(run, context);
        List<Map<String, Object>> messages;
        boolean compactedRequest = false;
        while (true) {
            messages = conversation.messages();
            if (state.isFinalAnswer()) {
                messages = prompts.finalAnswerMessages(messages, state.getNextDirective(), turnContext);
            } else if (state.getNextDirective() != null) {
                messages = new ArrayList<>(messages);
                messages.add(prompts.runtimeMessage(state.getNextDirective()));
            }
            if (compactedRequest || !conversation.needsCompaction(messages, definitions)) {
                break;
            }
            ChatResponse compacted = compact(run, context, conversation, engine);
            if (compacted == null) {
                break;
            }
            compactedRequest = true;
            state.addUsage(compacted.getUsage());
        }
        state.setNextDirective(null);
        log.info("AI turn model request: sessionId={}, turnId={}, requestId={}, requestNumber={}, engineId={}, messageCount={}, toolCount={}, contextUsage={}", run.getSession()
            .getSessionId(), run.getTurn()
                .getId(), run.getTurn().getRequestId(), request + 1, engine.getId(), messages.size(), definitions.size(), conversation.contextUsage(messages, definitions));
        sessions.saveReferences(run, context);
        Map<String, Object> phase = new LinkedHashMap<>();
        phase.put(AiSqlConstants.FIELD_PHASE, AiSqlConstants.PHASE_GENERATING);
        phase.put(AiSqlConstants.FIELD_CONTEXT_USAGE, conversation.contextUsage(messages, definitions));
        run.getEvents().emit(AiSqlConstants.EVENT_PHASE, phase);
        ChatResponse reply = client
            .stream(engine, messages, definitions, null, run.getHandle(), text -> run.getEvents().emit(AiSqlConstants.EVENT_TEXT_DELTA, Map.of(AiSqlConstants.FIELD_TEXT, text)));
        state.addUsage(reply.getUsage());
        check(run, context);
        if (reply.getFinishReason() == ChatFinishReason.CONTENT_FILTER && reply.getText().isBlank()) {
            String message = DmI18nUtils.getMessage(ChatProtocolConstants.ERROR_MODEL_CONTENT_FILTERED);
            run.getEvents().emit(AiSqlConstants.EVENT_TEXT_DELTA, Map.of(AiSqlConstants.FIELD_TEXT, message));
            reply = new ChatResponse(message, reply.getReasoningContent(), List.of(), reply.getUsage(), ChatFinishReason.CONTENT_FILTER);
        }
        if (reply.getFinishReason() == null || reply.getFinishReason() == ChatFinishReason.OTHER) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        if (state.isFinalAnswer() && reply.getFinishReason() != ChatFinishReason.STOP && reply.getFinishReason() != ChatFinishReason.CONTENT_FILTER) {
            if (reply.getFinishReason() == ChatFinishReason.LENGTH) {
                throw AiError.MODEL_OUTPUT_TRUNCATED.exception();
            }
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        // Terminal and truncated responses never execute tool requests.
        if (state.isFinalAnswer() || reply.getFinishReason() == ChatFinishReason.LENGTH || reply.getFinishReason() == ChatFinishReason.CONTENT_FILTER) {
            return new ChatResponse(reply.getText(), reply.getReasoningContent(), List.of(), reply.getUsage(), reply.getFinishReason());
        }
        return reply;
    }

    private boolean continueAfterResponse(AiActiveRun run, AiConversation conversation, ChatResponse reply, AiGenerationState state, int request) {
        if (reply.getFinishReason() == ChatFinishReason.LENGTH) {
            continueTruncated(run, conversation, reply, state, request);
            return true;
        }
        if (!state.isFinalAnswer() && reply.getFinishReason() == ChatFinishReason.STOP && reply.getTools().isEmpty() && reply.getText().isBlank()) {
            state.setCompletionGateRetries(0);
            conversation.addAssistant(reply);
            if (!state.isEmptyRetried()) {
                state.setEmptyRetried(true);
                state.setNextDirective(prompts.thinkOnlyRetryPrompt());
                log.warn("AI think-only response; retrying once: sessionId={}, turnId={}, requestNumber={}", run.getSession().getSessionId(), run.getTurn().getId(), request + 1);
            } else {
                state.setFinalAnswer(true);
                run.getEvents().replaceDraft("");
            }
            return true;
        }
        if (reply.getTools().isEmpty() && reply.getFinishReason() == ChatFinishReason.TOOL_CALLS) {
            conversation.addAssistant(reply);
            if (state.getCompletionGateRetries() < MAX_COMPLETION_GATE_RETRIES) {
                state.setCompletionGateRetries(state.getCompletionGateRetries() + 1);
                log.warn("AI tool continuation returned no requests; retrying: sessionId={}, turnId={}, retries={}", run.getSession().getSessionId(), run.getTurn().getId(), state
                    .getCompletionGateRetries());
            } else {
                state.setFinalAnswer(true);
                run.getEvents().replaceDraft("");
            }
            return true;
        }
        return false;
    }

    private void continueTruncated(AiActiveRun run, AiConversation conversation, ChatResponse reply, AiGenerationState state, int request) {
        state.setCompletionGateRetries(0);
        conversation.addAssistant(reply);
        if (!state.isLengthRetried()) {
            state.setLengthRetried(true);
            state.setNextDirective(prompts.lengthContinuationPrompt());
            log.warn("AI output truncated; continuing once: sessionId={}, turnId={}, requestNumber={}, textCharacters={}, usage={}", run.getSession().getSessionId(), run.getTurn()
                .getId(), request + 1, reply.getText().length(), reply.getUsage());
        } else {
            state.setFinalAnswer(true);
            state.setNextDirective(prompts.lengthFinalPrompt());
            run.getEvents().replaceDraft("");
            log.warn("AI continuation truncated; composing final answer without tools: sessionId={}, turnId={}, requestNumber={}, usage={}", run.getSession()
                .getSessionId(), run.getTurn().getId(), request + 1, reply.getUsage());
        }
    }

    private AiSqlResult executeTools(AiActiveRun run, AiMetadataContext context, AiConversation conversation, ChatToolRegistry registry, ChatResponse reply,
                                     AiGenerationState state) {
        for (ChatToolCall call : reply.getTools()) {
            check(run, context);
            boolean userInput = AiSqlConstants.TOOL_ASK_USER_QUESTIONS.equals(call.getName());
            if (!userInput && state.getToolCalls() >= limits.getMaxToolCalls()) {
                conversation.addToolResult(call, prompts.toolLimitResult());
                continue;
            }
            if (!userInput) {
                state.setToolCalls(state.getToolCalls() + 1);
            }
            long started = System.nanoTime();
            log.info("AI tool started: sessionId={}, turnId={}, toolCallId={}, toolName={}", run.getSession().getSessionId(), run.getTurn().getId(), call.getId(), call.getName());
            boolean registered = registry.executor(call.getName()) != null;
            if (registered) {
                run.getEvents().toolStarted(call);
            }
            ChatToolOutcome outcome = toolService.execute(registry, call, run.getHandle(), run.getPermit());
            log.info("AI tool completed: sessionId={}, turnId={}, toolCallId={}, toolName={}, success={}, resultCharacters={}, elapsedMs={}", run.getSession()
                .getSessionId(), run.getTurn().getId(), call.getId(), call.getName(), outcome.isSuccess(), outcome.getText().length(), (System.nanoTime() - started) / 1_000_000);
            check(run, context);
            sessions.saveReferences(run, context);
            if (registered) {
                run.getEvents().toolCompleted(call, outcome);
            }
            if (outcome.isSuccess() && userInput) {
                AiSqlResult delivered = AiSqlResult.fromText("", run.getEvents().generatedSql());
                return new AiSqlResult(AiSqlConstants.RESULT_NEEDS_INPUT, delivered.getSql(), reply.getText(), (AiUserInputRequestVO) outcome.getValue());
            }
            conversation.addToolResult(call, outcome.getText());
        }
        return null;
    }

    private ChatResponse compact(AiActiveRun run, AiMetadataContext context, AiConversation conversation, AiEngineConfigVO engine) {
        long started = System.nanoTime();
        AiCompactionSelection selection = conversation.compactionSelection();
        if (selection == null) {
            return null;
        }
        log.info("AI context compaction started: sessionId={}, turnId={}, historyTurns={}, currentMessages={}, maxOutputTokens={}", run.getSession().getSessionId(), run.getTurn()
            .getId(), selection.getHistoryCount(), selection.getCurrentMessageCount(), AiPromptBuilder.COMPACT_OUTPUT_TOKENS);
        run.getEvents().emit(AiSqlConstants.EVENT_PHASE, Map.of(AiSqlConstants.FIELD_PHASE, AiSqlConstants.PHASE_COMPACTING));
        ChatResponse response = null;
        boolean usageComplete = true;
        long inputTokens = 0;
        long outputTokens = 0;
        for (int attempt = 1; attempt <= MAX_COMPACTION_ATTEMPTS; attempt++) {
            check(run, context);
            try {
                response = client.stream(engine, conversation.compactionMessages(selection), List.of(), AiPromptBuilder.COMPACT_OUTPUT_TOKENS, run.getHandle(), text -> {
                });
                if (response.getUsage() == null) {
                    usageComplete = false;
                } else {
                    inputTokens += response.getUsage().getInputTokens();
                    outputTokens += response.getUsage().getOutputTokens();
                }
                if (response.getText().isBlank()) {
                    throw AiError.CONTEXT_COMPACTION_FAILED.exception();
                }
            } catch (RuntimeException e) {
                // Cancellation, expiry and lost access must stop retries and must not write a checkpoint.
                check(run, context);
                usageComplete = false;
                log.warn("AI context compaction attempt failed: sessionId={}, turnId={}, attempt={}", run.getSession().getSessionId(), run.getTurn().getId(), attempt, e);
                if (attempt == MAX_COMPACTION_ATTEMPTS) {
                    throw AiError.CONTEXT_COMPACTION_FAILED.exception();
                }
                continue;
            }
            check(run, context);
            String summary = response.getText().strip();
            if (selection.getCurrentMessageCount() == 0) {
                store.saveCompaction(run.getSession().getSessionId(), run.getTurn().getId(), summary, selection.getAnchorId());
            }
            conversation.acceptSummary(summary, selection);
            ChatUsage usage = null;
            if (usageComplete) {
                usage = new ChatUsage(inputTokens, outputTokens);
            }
            log.info("AI context compaction completed: sessionId={}, turnId={}, historyTurns={}, currentMessages={}, attempt={}, summaryCharacters={}, usage={}, elapsedMs={}", run
                .getSession()
                .getSessionId(), run.getTurn()
                    .getId(), selection.getHistoryCount(), selection.getCurrentMessageCount(), attempt, summary.length(), usage, (System.nanoTime() - started) / 1_000_000);
            return new ChatResponse(summary, response.getReasoningContent(), List.of(), usage, response.getFinishReason());
        }
        throw AiError.CONTEXT_COMPACTION_FAILED.exception();
    }

    private AiEngineConfigVO check(AiActiveRun run, AiMetadataContext context) {
        run.getHandle().checkActive();
        AiTurnRecordVO turn = store.find(run.getTurn().getId());
        if (turn == null || turn.getStatus() != AiTurnStatus.RUNNING) {
            run.getHandle().cancel();
            throw new CancellationException("AI turn ended");
        }
        if (!turn.getDeadline().after(store.now())) {
            throw AiError.TURN_EXPIRED.exception();
        }
        return sessions.authorizeRuntime(run, context);
    }

    private AiTurnRecordVO complete(AiActiveRun run, AiMetadataContext context, AiConversation conversation, AiSqlResult result, ChatUsage usage) {
        check(run, context);
        AiTurnRecordVO terminal = sessions.saveResult(run, context, conversation, result, usage);
        log.info("AI turn saved: sessionId={}, turnId={}, requestId={}, status={}", run.getSession().getSessionId(), run.getTurn().getId(), run.getTurn().getRequestId(), terminal
            .getStatus());
        return terminal;
    }

}
