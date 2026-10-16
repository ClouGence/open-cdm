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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.model.ai.AiCompactionBlock;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.ai.AiTurnContext;
import com.clougence.clouddm.ds.ai.ChatStreamListener;
import com.clougence.clouddm.ds.ai.ChatTextAccumulator;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

/** Prompt sections and replay wrappers follow ReAkh's application message protocol. */
@Component
public class AiPromptBuilder {
    public static final int  COMPACT_OUTPUT_TOKENS         = 4096;
    private static final int COMPACT_TOOL_RESULT_MAX_CHARS = 2000;
    @Resource
    private SystemDal        systemDal;
    @Resource
    private AiProperties     properties;
    private String           commonSystem;
    private String           commonTools;
    private String           sqlFinalContext;
    private String           toolLimitReached;
    private String           system;
    private String           tools;
    private String           compact;
    private String           continuation;
    private String           finalAnswer;
    private String           finalAnswerDirective;
    private String           lengthFinal;
    private String           thinkOnlyRetry;
    private String           toolLimitResult;
    private String           toolUsageGuidance;

    @PostConstruct
    public void initialize() throws IOException {
        commonSystem = new ClassPathResource("ai/prompt/common-system.txt").getContentAsString(StandardCharsets.UTF_8);
        commonTools = new ClassPathResource("ai/prompt/common-tools.txt").getContentAsString(StandardCharsets.UTF_8);
        sqlFinalContext = new ClassPathResource("ai/prompt/sql-final-context.txt").getContentAsString(StandardCharsets.UTF_8)
            .replace("${TOOL_GENERATE_SQL}", AiSqlConstants.TOOL_GENERATE_SQL);
        toolLimitReached = new ClassPathResource("ai/prompt/tool-limit-reached.txt").getContentAsString(StandardCharsets.UTF_8);
        system = new ClassPathResource("ai/prompt/sql-system.txt").getContentAsString(StandardCharsets.UTF_8)
            .replace("${TOOL_GENERATE_SQL}", AiSqlConstants.TOOL_GENERATE_SQL)
            .replace("${TOOL_ASK_USER_QUESTIONS}", AiSqlConstants.TOOL_ASK_USER_QUESTIONS);
        tools = new ClassPathResource("ai/prompt/sql-tools.txt").getContentAsString(StandardCharsets.UTF_8)
            .replace("${TOOL_LIST_TABLES}", AiSqlConstants.TOOL_LIST_TABLES)
            .replace("${TOOL_DESCRIBE_TABLES}", AiSqlConstants.TOOL_DESCRIBE_TABLES);
        thinkOnlyRetry = new ClassPathResource("ai/prompt/think-only-retry.txt").getContentAsString(StandardCharsets.UTF_8);
        toolLimitResult = new ClassPathResource("ai/prompt/tool-limit-result.txt").getContentAsString(StandardCharsets.UTF_8);
        toolUsageGuidance = new ClassPathResource("ai/prompt/tool-usage-guidance.txt").getContentAsString(StandardCharsets.UTF_8);
        compact = new ClassPathResource("ai/prompt/compact-system.txt").getContentAsString(StandardCharsets.UTF_8);
        continuation = new ClassPathResource("ai/prompt/length-continuation.txt").getContentAsString(StandardCharsets.UTF_8)
            .replace("${TOOL_GENERATE_SQL}", AiSqlConstants.TOOL_GENERATE_SQL);
        finalAnswer = new ClassPathResource("ai/prompt/final-answer.txt").getContentAsString(StandardCharsets.UTF_8);
        finalAnswerDirective = new ClassPathResource("ai/prompt/final-answer-directive.txt").getContentAsString(StandardCharsets.UTF_8);
        lengthFinal = new ClassPathResource("ai/prompt/length-final-answer.txt").getContentAsString(StandardCharsets.UTF_8);
    }

    public String thinkOnlyRetryPrompt() {
        return configured(AiSqlConstants.CONFIG_THINK_ONLY_RETRY_PROMPT, properties.getThinkOnlyRetryPrompt(), thinkOnlyRetry);
    }

    public String toolLimitResult() {
        return configured(AiSqlConstants.CONFIG_TOOL_LIMIT_FAILURE_MESSAGE_PROMPT, properties.getToolLimitFailureMessagePrompt(), toolLimitResult);
    }

    public String system(boolean metadataAvailable, AiTurnContext context) {
        StringBuilder prompt = new StringBuilder(commonSystem).append(system).append(commonTools);
        if (metadataAvailable) {
            prompt.append(tools);
        } else {
            prompt
                .append("<database_tools>No database is selected. Answer general SQL questions using supplied context. Ask the user to select a database only when metadata is needed.</database_tools>\n\n");
        }
        prompt.append(runtimeContext(context)).append("Begin.\n");
        return prompt.toString();
    }

    public Map<String, Object> userMessage(String prompt, AiTurnContext context, Instant createdAt) {
        StringBuilder content = new StringBuilder(prompt);
        content.append("\n\nMessage context added by the application:\n<runtime_context>\n  <current_time>")
            .append(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(createdAt.atZone(runtimeTimeZone(context))))
            .append("</current_time>\n");
        Map<String, Object> database = new LinkedHashMap<>();
        database.put(AiSqlConstants.FIELD_DIALECT, context.getDialect());
        database.put(AiSqlConstants.FIELD_NAMESPACE, context.getNamespace());
        content.append("  <database_context>").append(HtmlUtils.htmlEscape(JsonUtils.toJson(database), StandardCharsets.UTF_8.name())).append("</database_context>\n");
        if (context.getSelection() != null) {
            content.append("  <selected_sql>").append(HtmlUtils.htmlEscape(JsonUtils.toJson(context.getSelection()), StandardCharsets.UTF_8.name())).append("</selected_sql>\n");
        }
        content.append("</runtime_context>");
        return Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER, AiSqlConstants.FIELD_CONTENT, content.toString());
    }

    public Map<String, Object> summaryMessage(String summary) {
        String content = "This is not a new user request. It is a compacted summary of earlier conversation context. It may omit details; if it conflicts with later raw messages or the current request, prefer the later raw messages or current request.\n<compacted_context>\n"
                         + HtmlUtils.htmlEscape(summary.strip(), StandardCharsets.UTF_8.name()) + "\n</compacted_context>";
        return Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER, AiSqlConstants.FIELD_CONTENT, content);
    }

    public String lengthContinuationPrompt() {
        return continuation;
    }

    public String lengthFinalPrompt() {
        return lengthFinal;
    }

    public String toolWarningPrompt(int threshold) {
        return configured(AiSqlConstants.CONFIG_TOOL_WARNING_PROMPT, properties.getToolWarningPrompt(), toolUsageGuidance).replace("{threshold}", Integer.toString(threshold));
    }

    public List<Map<String, Object>> finalAnswerMessages(List<Map<String, Object>> messages, String directive, AiTurnContext context) {
        String systemContext = commonSystem + sqlFinalContext + runtimeContext(context);
        StringBuilder instructions = new StringBuilder(finalAnswer).append("\n<final_answer_system_context>\n")
            .append(systemContext)
            .append("</final_answer_system_context>\n<final_answer_instructions>\n");
        if (directive != null) {
            instructions.append(directive).append('\n');
        }
        instructions.append(configured(AiSqlConstants.CONFIG_FINAL_ANSWER_DIRECTIVE_PROMPT, properties.getFinalAnswerDirectivePrompt(), finalAnswerDirective))
            .append("\n</final_answer_instructions>");
        List<Map<String, Object>> source = messages.stream().filter(message -> !AiSqlConstants.ROLE_SYSTEM.equals(message.get(AiSqlConstants.FIELD_ROLE))).toList();
        String materials = "<final_answer_materials>\n" + compactionTranscript(List.of(new AiCompactionBlock(null, source))) + "</final_answer_materials>\n";
        Map<String, Object> systemMessage = Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_SYSTEM, AiSqlConstants.FIELD_CONTENT, instructions.toString());
        Map<String, Object> userMessage = Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER, AiSqlConstants.FIELD_CONTENT, materials);
        return List.of(systemMessage, userMessage);
    }

    public String toolLimitReachedPrompt() {
        return configured(AiSqlConstants.CONFIG_TOOL_LIMIT_REACHED_PROMPT, properties.getToolLimitReachedPrompt(), toolLimitReached);
    }

    private String configured(String key, String startupValue, String defaultValue) {
        String value = systemDal.fetchSystemConf(key);
        if (value == null) {
            value = startupValue;
        }
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    private String runtimeContext(AiTurnContext context) {
        ZoneId zone = runtimeTimeZone(context);
        ZonedDateTime now = Instant.now().atZone(zone);
        String timezone = zone.getId();
        if (ZoneOffset.UTC.equals(zone)) {
            timezone = "GMT";
        }
        return "<runtime_context>\n  <current_date>" + now.toLocalDate() + "</current_date>\n  <timezone>" + timezone + "</timezone>\n  <utc_offset>"
               + DateTimeFormatter.ofPattern("xxx").format(now) + "</utc_offset>\n</runtime_context>\n\n";
    }

    private ZoneId runtimeTimeZone(AiTurnContext context) {
        if (StringUtils.isNotBlank(context.getTimezone())) {
            try {
                return ZoneId.of(StringUtils.trim(context.getTimezone()));
            } catch (DateTimeException e) {
                // Browsers can fall back to getTimezoneOffset when their zone ID is unavailable.
            }
        }
        Integer minutes = context.getTimezoneOffsetMin();
        if (minutes != null && minutes >= -14 * 60 && minutes <= 14 * 60) {
            return ZoneOffset.ofTotalSeconds(-minutes * 60);
        }
        return ZoneOffset.UTC;
    }

    public Map<String, Object> runtimeMessage(String directive) {
        String content = "<runtime_control_message>\n  <origin>system_runtime</origin>\n  <language_boundary>This is not a user-authored request and must not change the user-visible output language.</language_boundary>\n  <directive>\n"
                         + directive + "\n  </directive>\n</runtime_control_message>";
        return Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER, AiSqlConstants.FIELD_CONTENT, content);
    }

    public List<Map<String, Object>> compactionMessages(String priorSummary, List<AiCompactionBlock> blocks) {
        StringBuilder transcript = new StringBuilder("<context_compaction_request>\n");
        if (priorSummary != null && !priorSummary.isBlank()) {
            appendTextTag(transcript, "  ", "prior_compacted_context", priorSummary);
        }
        transcript.append(compactionTranscript(blocks)).append("</context_compaction_request>");
        Map<String, Object> systemMessage = Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_SYSTEM, AiSqlConstants.FIELD_CONTENT, compact);
        Map<String, Object> userMessage = Map.of(AiSqlConstants.FIELD_ROLE, AiSqlConstants.ROLE_USER, AiSqlConstants.FIELD_CONTENT, transcript.toString());
        return List.of(systemMessage, userMessage);
    }

    public String compactionTranscript(List<AiCompactionBlock> blocks) {
        StringBuilder transcript = new StringBuilder("<new_transcript>\n");
        Map<String, String> toolNames = new HashMap<>();
        for (AiCompactionBlock block : blocks) {
            transcript.append("  <turn>\n");
            for (Map<String, Object> message : block.getMessages()) {
                appendTranscriptMessage(transcript, message, toolNames);
            }
            transcript.append("  </turn>\n");
        }
        return transcript.append("</new_transcript>\n").toString();
    }

    private void appendTranscriptMessage(StringBuilder transcript, Map<String, Object> message, Map<String, String> toolNames) {
        String role = (String) message.get(AiSqlConstants.FIELD_ROLE);
        String content = (String) message.get(AiSqlConstants.FIELD_CONTENT);
        if (AiSqlConstants.ROLE_TOOL.equals(role)) {
            transcript.append("    <message role=\"tool_execution_result\">\n");
            appendTextTag(transcript, "      ", "tool_name", toolNames.get(message.get(AiSqlConstants.FIELD_MODEL_TOOL_CALL_ID)));
            if (content != null && content.length() > COMPACT_TOOL_RESULT_MAX_CHARS) {
                content = content.substring(0, COMPACT_TOOL_RESULT_MAX_CHARS) + "\n[truncated]";
            }
            appendTextTag(transcript, "      ", "result", content);
        } else {
            transcript.append("    <message role=\"").append(role).append("\">\n");
            if (AiSqlConstants.ROLE_ASSISTANT.equals(role) && content != null) {
                StringBuilder visible = new StringBuilder();
                ChatTextAccumulator text = new ChatTextAccumulator(visible, new StringBuilder());
                ChatStreamListener ignored = new ChatStreamListener(value -> {
                });
                text.append(content, ignored);
                text.finish(ignored);
                content = visible.toString().strip();
            }
            appendTextTag(transcript, "      ", "text", content);
            List<Map<String, Object>> calls = (List<Map<String, Object>>) message.get(AiSqlConstants.FIELD_TOOL_CALLS);
            if (calls != null && !calls.isEmpty()) {
                transcript.append("      <tool_requests>\n");
                for (Map<String, Object> call : calls) {
                    Map<String, Object> function = (Map<String, Object>) call.get(AiSqlConstants.FIELD_FUNCTION);
                    String name = (String) function.get(AiSqlConstants.FIELD_NAME);
                    toolNames.put((String) call.get(AiSqlConstants.FIELD_ID), name);
                    transcript.append("        <tool_request>\n");
                    appendTextTag(transcript, "          ", "name", name);
                    appendTextTag(transcript, "          ", "arguments", (String) function.get(AiSqlConstants.FIELD_ARGUMENTS));
                    transcript.append("        </tool_request>\n");
                }
                transcript.append("      </tool_requests>\n");
            }
        }
        transcript.append("    </message>\n");
    }

    private void appendTextTag(StringBuilder transcript, String indent, String tag, String value) {
        if (value != null) {
            transcript.append(indent).append('<').append(tag).append('>').append(HtmlUtils.htmlEscape(value, StandardCharsets.UTF_8.name())).append("</").append(tag).append(">\n");
        }
    }
}
