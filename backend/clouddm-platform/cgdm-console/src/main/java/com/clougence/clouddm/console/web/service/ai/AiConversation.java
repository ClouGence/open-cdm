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

import java.time.Instant;
import java.util.*;

import com.clougence.clouddm.console.web.model.ai.*;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.ds.ai.ChatResponse;
import com.clougence.clouddm.ds.ai.ChatToolCall;
import com.clougence.clouddm.platform.dal.model.ai.DmAiSessionDO;
import com.clougence.utils.JsonUtils;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;

import lombok.Getter;

/** Derived model context; original conversation rows are never removed by compaction. */
public class AiConversation {
    private static final Encoding           TOKEN_ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.CL100K_BASE);
    private final AiPromptBuilder           prompts;
    private final String                    system;
    private final int                       inputBudget;
    private final int                       contextTokens;
    private final int                       tailBudget;
    private final List<AiTurnRecordVO>      history;
    private final List<Map<String, Object>> current        = new ArrayList<>();
    private String                          summary;
    private final List<Map<String, Object>> replay         = new ArrayList<>();
    private boolean                         runtimeCompacted;
    @Getter
    private boolean                         historyCompacted;

    public AiConversation(AiPromptBuilder prompts, int contextTokens, String system, DmAiSessionDO session, List<AiTurnRecordVO> latest, AiTurnContext database, String prompt,
                          Instant createdAt){
        this.prompts = prompts;
        this.system = system;
        // Match ReAkh's replay budget: reserve 10% and retain a bounded recent tail.
        this.contextTokens = contextTokens;
        inputBudget = contextTokens - contextTokens / 10;
        tailBudget = Math.min(12000, contextTokens / 10);
        summary = session.getCompactSummary();
        historyCompacted = summary != null;
        history = new ArrayList<>(latest);
        Collections.reverse(history);
        if (session.getCompactAnchorHistoryId() != null) {
            int anchor = -1;
            for (int i = 0; i < history.size(); i++) {
                if (history.get(i).getHistoryId().equals(session.getCompactAnchorHistoryId())) {
                    anchor = i;
                }
            }
            if (anchor < 0) {
                throw AiError.INTERNAL_ERROR.exception();
            }
            history.subList(0, anchor + 1).clear();
        }
        if (prompt != null) {
            current.add(prompts.userMessage(prompt, database, createdAt));
        }
    }

    public int estimatedTokens(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        return messageTokens(messages) + TOKEN_ENCODING.countTokensOrdinary(JsonUtils.toJson(tools));
    }

    public Map<String, Integer> contextUsage(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        return Map.of(AiSqlConstants.FIELD_USED_TOKENS, estimatedTokens(messages, tools), AiSqlConstants.FIELD_MAX_TOKENS, contextTokens);
    }

    public boolean needsCompaction(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        return Math.max(estimatedTokens(messages, tools), estimatedTokens(buildMessages(), tools)) >= inputBudget;
    }

    public List<Map<String, Object>> messages() {
        return buildMessages();
    }

    public AiCompactionSelection compactionSelection() {
        List<AiCompactionBlock> blocks = new ArrayList<>();
        for (AiTurnRecordVO turn : history) {
            blocks.add(new AiCompactionBlock(turn.getHistoryId(), historyMessages(List.of(turn))));
        }
        int index = 1;
        while (index < current.size()) {
            List<Map<String, Object>> batch = new ArrayList<>();
            Map<String, Object> assistant = current.get(index++);
            if (!AiSqlConstants.ROLE_ASSISTANT.equals(assistant.get(AiSqlConstants.FIELD_ROLE))) {
                break;
            }
            batch.add(assistant);
            while (index < current.size() && AiSqlConstants.ROLE_TOOL.equals(current.get(index).get(AiSqlConstants.FIELD_ROLE))) {
                batch.add(current.get(index++));
            }
            List<Map<String, Object>> calls = (List<Map<String, Object>>) assistant.get(AiSqlConstants.FIELD_TOOL_CALLS);
            if (calls != null && calls.stream()
                .anyMatch(call -> batch.stream().noneMatch(message -> call.get(AiSqlConstants.FIELD_ID).equals(message.get(AiSqlConstants.FIELD_MODEL_TOOL_CALL_ID))))) {
                break;
            }
            blocks.add(new AiCompactionBlock(null, List.copyOf(batch)));
        }
        if (blocks.isEmpty()) {
            return null;
        }
        int preservedTokens = 0;
        int preservedTextMessages = 0;
        int preservedStart = blocks.size();
        for (int i = blocks.size() - 1; i >= 0; i--) {
            AiCompactionBlock block = blocks.get(i);
            int count = messageTokens(block.getMessages());
            if (preservedTokens + count > tailBudget) {
                break;
            }
            preservedTokens += count;
            preservedTextMessages += (int) block.getMessages().stream().filter(message -> !AiSqlConstants.ROLE_TOOL.equals(message.get(AiSqlConstants.FIELD_ROLE))).count();
            preservedStart = i;
            if (preservedTextMessages >= 5 && preservedTokens >= Math.min(2048, tailBudget)) {
                break;
            }
        }
        if (preservedStart == 0 || preservedStart == blocks.size()) {
            preservedStart = blocks.size();
        }
        List<AiCompactionBlock> selected = List.copyOf(blocks.subList(0, preservedStart));
        int historyCount = 0;
        int currentMessageCount = 0;
        Long anchorId = null;
        for (AiCompactionBlock block : selected) {
            if (block.getAnchorId() != null) {
                historyCount++;
                anchorId = block.getAnchorId();
            } else {
                currentMessageCount += block.getMessages().size();
            }
        }
        if (currentMessageCount > 0) {
            anchorId = null;
        }
        return new AiCompactionSelection(selected, historyCount, currentMessageCount, anchorId);
    }

    public List<Map<String, Object>> compactionMessages(AiCompactionSelection selection) {
        return prompts.compactionMessages(summary, selection.getBlocks());
    }

    public void acceptSummary(String value, AiCompactionSelection selection) {
        summary = value;
        history.subList(0, selection.getHistoryCount()).clear();
        current.subList(1, 1 + selection.getCurrentMessageCount()).clear();
        runtimeCompacted |= selection.getCurrentMessageCount() > 0;
        historyCompacted = true;
    }

    public void addAssistant(ChatResponse reply) {
        List<Map<String, Object>> encoded = new ArrayList<>();
        for (ChatToolCall call : reply.getTools()) {
            Map<String, Object> function = new LinkedHashMap<>();
            function.put(AiSqlConstants.FIELD_NAME, call.getName());
            function.put(AiSqlConstants.FIELD_ARGUMENTS, call.getArguments());

            Map<String, Object> tool = new LinkedHashMap<>();
            tool.put(AiSqlConstants.FIELD_ID, call.getId());
            tool.put(AiSqlConstants.FIELD_TYPE, AiSqlConstants.TYPE_FUNCTION);
            tool.put(AiSqlConstants.FIELD_FUNCTION, function);
            encoded.add(tool);
        }

        Map<String, Object> message = new LinkedHashMap<>();
        message.put(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_ASSISTANT);
        message.put(AiSqlConstants.FIELD_CONTENT, reply.getText());
        if (!reply.getReasoningContent().isEmpty()) {
            message.put(ChatProtocolConstants.FIELD_REASONING_CONTENT, reply.getReasoningContent());
        }
        if (!encoded.isEmpty()) {
            message.put(AiSqlConstants.FIELD_TOOL_CALLS, encoded);
        }
        add(message);
    }

    public void addToolResult(ChatToolCall call, String text) {
        add(Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_TOOL, AiSqlConstants.FIELD_MODEL_TOOL_CALL_ID, call.getId(), AiSqlConstants.FIELD_CONTENT, text));
    }

    private void add(Map<String, Object> message) {
        current.add(message);
        replay.add(message);
    }

    public List<Map<String, Object>> replayMessages() {
        return List.copyOf(replay);
    }

    public String durableSummary() {
        if (!runtimeCompacted) {
            return null;
        }
        return summary + "\n\n<current_turn_after_compaction>\n" + prompts.compactionTranscript(List.of(new AiCompactionBlock(null, current))) + "</current_turn_after_compaction>";
    }

    private List<Map<String, Object>> buildMessages() {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_SYSTEM, AiSqlConstants.FIELD_CONTENT, system));
        if (summary != null) {
            messages.add(prompts.summaryMessage(summary));
        }
        messages.addAll(historyMessages(history));
        messages.addAll(current);
        return messages;
    }

    private List<Map<String, Object>> historyMessages(List<AiTurnRecordVO> turns) {
        List<Map<String, Object>> messages = new ArrayList<>();
        for (AiTurnRecordVO turn : turns) {
            AiTurnContext context = JsonUtils.toObj(turn.getContextJson(), AiTurnContext.class);
            messages.add(prompts.userMessage(turn.getPrompt(), context, turn.getGmtCreate().toInstant()));
            if (turn.getResultJson() == null) {
                continue;
            }
            Map<String, Object> result = JsonUtils.toObj(turn.getResultJson(), Map.class);
            List<Map<String, Object>> replayed = (List<Map<String, Object>>) result.get(AiSqlConstants.FIELD_REPLAY_MESSAGES);
            for (Map<String, Object> message : userInputReplay(result, replayed, turn.getUserInputAnswerJson())) {
                Object content = message.get(AiSqlConstants.FIELD_CONTENT);
                if (AiSqlConstants.ROLE_ASSISTANT.equals(message.get(AiSqlConstants.FIELD_ROLE)) && !message.containsKey(AiSqlConstants.FIELD_TOOL_CALLS)
                    && (!(content instanceof String text) || text.isBlank())) {
                    continue;
                }
                messages.add(message);
            }
        }
        return messages;
    }

    private List<Map<String, Object>> userInputReplay(Map<String, Object> result, List<Map<String, Object>> replayed, String answerJson) {
        Map<String, Object> request = (Map<String, Object>) result.get(AiSqlConstants.FIELD_USER_INPUT_REQUEST);
        if (request == null) {
            return replayed;
        }
        Object answer = null;
        if (answerJson != null) {
            answer = JsonUtils.toObj(answerJson, Map.class);
        }
        String pendingCallId = null;
        for (Map<String, Object> message : replayed) {
            List<Map<String, Object>> calls = (List<Map<String, Object>>) message.get(AiSqlConstants.FIELD_TOOL_CALLS);
            if (calls == null) {
                continue;
            }
            for (Map<String, Object> call : calls) {
                Map<String, Object> function = (Map<String, Object>) call.get(AiSqlConstants.FIELD_FUNCTION);
                if (AiSqlConstants.TOOL_ASK_USER_QUESTIONS.equals(function.get(AiSqlConstants.FIELD_NAME))) {
                    pendingCallId = (String) call.get(AiSqlConstants.FIELD_ID);
                }
            }
        }
        List<Map<String, Object>> resolved = new ArrayList<>();
        for (Map<String, Object> message : replayed) {
            List<Map<String, Object>> calls = (List<Map<String, Object>>) message.get(AiSqlConstants.FIELD_TOOL_CALLS);
            boolean pending = false;
            if (calls != null) {
                for (Map<String, Object> call : calls) {
                    if (call.get(AiSqlConstants.FIELD_ID).equals(pendingCallId)) {
                        pending = true;
                    }
                }
            }
            if (!pending) {
                resolved.add(message);
                continue;
            }
            Map<String, Object> assistant = new LinkedHashMap<>(message);
            if (answer == null) {
                assistant.remove(AiSqlConstants.FIELD_TOOL_CALLS);
                Object content = assistant.get(AiSqlConstants.FIELD_CONTENT);
                if (content instanceof String text && !text.isBlank()) {
                    resolved.add(assistant);
                }
                continue;
            }
            String requestId = (String) request.get(AiSqlConstants.FIELD_USER_INPUT_REQUEST_ID);
            Map<String, Object> function = new LinkedHashMap<>();
            function.put(AiSqlConstants.FIELD_NAME, AiSqlConstants.TOOL_ASK_USER_QUESTIONS);
            function.put(AiSqlConstants.FIELD_ARGUMENTS, JsonUtils.toJson(request));
            Map<String, Object> tool = new LinkedHashMap<>();
            tool.put(AiSqlConstants.FIELD_ID, requestId);
            tool.put(AiSqlConstants.FIELD_TYPE, AiSqlConstants.TYPE_FUNCTION);
            tool.put(AiSqlConstants.FIELD_FUNCTION, function);
            assistant.put(AiSqlConstants.FIELD_TOOL_CALLS, List.of(tool));
            resolved.add(assistant);

            Map<String, Object> toolResult = new LinkedHashMap<>();
            toolResult.put(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_TOOL);
            toolResult.put(AiSqlConstants.FIELD_MODEL_TOOL_CALL_ID, requestId);
            toolResult.put(AiSqlConstants.FIELD_CONTENT, JsonUtils.toJson(answer));
            resolved.add(toolResult);
        }
        return resolved;
    }

    private int messageTokens(List<Map<String, Object>> messages) {
        int count = 3;
        for (Map<String, Object> message : messages) {
            count += 4;
            String content = (String) message.get(AiSqlConstants.FIELD_CONTENT);
            if (content != null) {
                count += TOKEN_ENCODING.countTokensOrdinary(content);
            }
            List<Map<String, Object>> calls = (List<Map<String, Object>>) message.get(AiSqlConstants.FIELD_TOOL_CALLS);
            if (calls != null) {
                count += 6;
                for (Map<String, Object> call : calls) {
                    Map<String, Object> function = (Map<String, Object>) call.get(AiSqlConstants.FIELD_FUNCTION);
                    count += TOKEN_ENCODING.countTokensOrdinary((String) function.get(AiSqlConstants.FIELD_NAME));
                    count += TOKEN_ENCODING.countTokensOrdinary((String) function.get(AiSqlConstants.FIELD_ARGUMENTS));
                }
            }
        }
        return count;
    }
}
