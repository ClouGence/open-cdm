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
package com.clougence.clouddm.console.web.model.ai;

import com.clougence.clouddm.console.web.util.SseResponseUtils;

public final class AiSqlConstants {
    public static final String UUID_PATTERN                             = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
    public static final String CONFIG_FINAL_ANSWER_DIRECTIVE_PROMPT     = "STREAMING_FINAL_ANSWER_DIRECTIVE_PROMPT";
    public static final String CONFIG_THINK_ONLY_RETRY_PROMPT           = "STREAMING_THINK_ONLY_RETRY_PROMPT";
    public static final String CONFIG_TOOL_LIMIT_REACHED_PROMPT         = "STREAMING_TOOL_LIMIT_REACHED_PROMPT";
    public static final String CONFIG_TOOL_LIMIT_FAILURE_MESSAGE_PROMPT = "STREAMING_TOOL_LIMIT_FAILURE_MESSAGE_PROMPT";
    public static final String CONFIG_TOOL_WARNING_PROMPT               = "STREAMING_TOOL_WARNING_PROMPT";
    public static final String HISTORY_EVENT_RESULT                     = "RESPONSE_GENERATED";
    public static final String HISTORY_EVENT_USER_INPUT_ANSWERED        = "USER_INPUT_ANSWERED";

    public static final String TOOL_LIST_TABLES                         = "listTables";
    public static final String TOOL_DESCRIBE_TABLES                     = "describeTables";
    public static final String TOOL_GENERATE_SQL                        = "generateSql";
    public static final String TOOL_ASK_USER_QUESTIONS                  = "ask_user_questions";
    public static final String TOOL_PROBE                               = "clouddmProbe";

    public static final String RESULT_GENERATED                         = "GENERATED";
    public static final String RESULT_NEEDS_INPUT                       = "NEEDS_INPUT";

    public static final String PHASE_GENERATING                         = "GENERATING";
    public static final String PHASE_SEARCHING                          = "SEARCHING";
    public static final String PHASE_READING                            = "READING";
    public static final String PHASE_COMPACTING                         = "COMPACTING";

    public static final String TOOL_STATUS_OK                           = "OK";
    public static final String TOOL_STATUS_ERROR                        = "ERROR";

    public static final String SOURCE_CACHED_OR_LIVE                    = "CACHED_OR_LIVE";

    public static final String EVENT_STARTED                            = "started";
    public static final String EVENT_PHASE                              = "phase";
    public static final String EVENT_TOOL_STARTED                       = "toolStarted";
    public static final String EVENT_TOOL_COMPLETED                     = "toolCompleted";
    public static final String EVENT_TEXT_DELTA                         = "textDelta";
    public static final String EVENT_RESULT                             = "result";
    public static final String EVENT_ERROR                              = SseResponseUtils.EVENT_ERROR;
    public static final String EVENT_DONE                               = "done";
    public static final String EVENT_HEARTBEAT                          = "heartbeat";

    // JSON field names shared by stream events, tools and session responses.
    public static final String FIELD_ACCESSIBLE                         = "accessible";
    public static final String FIELD_ACTIVE_TURN                        = "activeTurn";
    public static final String FIELD_ADDITIONAL_PROPERTIES              = "additionalProperties";
    public static final String FIELD_ARGUMENTS                          = "arguments";
    public static final String FIELD_CAN_GENERATE                       = "canGenerate";
    public static final String FIELD_CAN_MANAGE_ENGINES                 = "canManageEngines";
    public static final String FIELD_CODE                               = "code";
    public static final String FIELD_COLUMNS                            = "columns";
    public static final String FIELD_COLUMNS_COMPLETE                   = "columnsComplete";
    public static final String FIELD_COMMENT                            = "comment";
    public static final String FIELD_CONST                              = "const";
    public static final String FIELD_CONTENT                            = "content";
    public static final String FIELD_CONTEXT                            = "context";
    public static final String FIELD_CONTEXT_SUMMARY                    = "contextSummary";
    public static final String FIELD_CONTEXT_TOKENS                     = "contextTokens";
    public static final String FIELD_CONTEXT_USAGE                      = "contextUsage";
    public static final String FIELD_CREATED_AT                         = "createdAt";
    public static final String FIELD_CURSOR                             = "cursor";
    public static final String FIELD_DATA                               = "data";
    public static final String FIELD_DEADLINE                           = "deadline";
    public static final String FIELD_DELETED                            = "deleted";
    public static final String FIELD_DESCRIPTION                        = "description";
    public static final String FIELD_DIALECT                            = "dialect";
    public static final String FIELD_ENGINE_ID                          = "engineId";
    public static final String FIELD_ENGINE_NAME                        = "engineName";
    public static final String FIELD_ENGINE_VERSION                     = "engineVersion";
    public static final String FIELD_ERROR                              = "error";
    public static final String FIELD_ERROR_MESSAGE                      = "errorMessage";
    public static final String FIELD_ERROR_CODE                         = "errorCode";
    public static final String FIELD_FOREIGN_KEYS                       = "foreignKeys";
    public static final String FIELD_FUNCTION                           = "function";
    public static final String FIELD_GENERATION_AVAILABLE               = "generationAvailable";
    public static final String FIELD_HAS_MORE                           = "hasMore";
    public static final String FIELD_HISTORY_COMPACTED                  = "historyCompacted";
    public static final String FIELD_ID                                 = "id";
    public static final String FIELD_ITEMS                              = "items";
    public static final String FIELD_KEYS_KNOWN                         = "keysKnown";
    public static final String FIELD_KEYWORD                            = "keyword";
    public static final String FIELD_LIMITS                             = "limits";
    public static final String FIELD_MAX_TOKENS                         = "maxTokens";
    public static final String FIELD_MESSAGE                            = "message";
    public static final String FIELD_MODIFIED_AT                        = "modifiedAt";
    public static final String FIELD_NAME                               = "name";
    public static final String FIELD_NAMESPACE                          = "namespace";
    public static final String FIELD_NEXT_CURSOR                        = "nextCursor";
    public static final String FIELD_NONCE                              = "nonce";
    public static final String FIELD_OBJECT                             = "object";
    public static final String FIELD_OBJECTS                            = "objects";
    public static final String FIELD_OBJECT_REF                         = "objectRef";
    public static final String FIELD_OBJECT_REFS                        = "objectRefs";
    public static final String FIELD_PARAMETERS                         = "parameters";
    public static final String FIELD_PHASE                              = "phase";
    public static final String FIELD_PRIMARY_KEY                        = "primaryKey";
    public static final String FIELD_PROMPT                             = "prompt";
    public static final String FIELD_PROMPT_CHARACTERS                  = "promptCharacters";
    public static final String FIELD_PROPERTIES                         = "properties";
    public static final String FIELD_REQUEST_ID                         = "requestId";
    public static final String FIELD_REQUIRED                           = "required";
    public static final String FIELD_REPLAY_MESSAGES                    = "replayMessages";
    public static final String FIELD_RESULT                             = "result";
    public static final String FIELD_ROLE                               = "role";
    public static final String FIELD_SCHEMA_READ_AT                     = "schemaReadAt";
    public static final String FIELD_SEQ                                = "seq";
    public static final String FIELD_SESSION_ID                         = "sessionId";
    public static final String FIELD_SOURCE_FRESHNESS                   = "sourceFreshness";
    public static final String FIELD_SQL                                = "sql";
    public static final String FIELD_STATUS                             = "status";
    public static final String FIELD_SUMMARY                            = "summary";
    public static final String FIELD_TABLES                             = "tables";
    public static final String FIELD_TEXT                               = "text";
    public static final String FIELD_TOOL_CALLS                         = "tool_calls";
    public static final String FIELD_TOOL_CALL_ID                       = "toolCallId";
    public static final String FIELD_TOOL_NAME                          = "toolName";
    public static final String FIELD_TURNS                              = "turns";
    public static final String FIELD_TURN_ID                            = "turnId";
    public static final String FIELD_TURN_SECONDS                       = "turnSeconds";
    public static final String FIELD_TYPE                               = "type";
    public static final String FIELD_UNAVAILABLE_CODE                   = "unavailableCode";
    public static final String FIELD_USAGE                              = "usage";
    public static final String FIELD_USED_TOKENS                        = "usedTokens";

    public static final String ROLE_ASSISTANT                           = "assistant";
    public static final String ROLE_SYSTEM                              = "system";
    public static final String ROLE_TOOL                                = "tool";
    public static final String ROLE_USER                                = "user";
    public static final String SCHEMA_TYPE_OBJECT                       = "object";
    public static final String SCHEMA_TYPE_STRING                       = "string";
    public static final String TYPE_FUNCTION                            = "function";

    public static final String FIELD_MODEL_TOOL_CALL_ID                 = "tool_call_id";
    public static final String FIELD_MODELS                             = "models";

    public static final String EVENT_SNAPSHOT                           = "snapshot";
    public static final String FIELD_DRAFT                              = "draft";
    public static final String FIELD_ACTIVITIES                         = "activities";

    public static final String FIELD_USER_INPUT_REQUEST                 = "userInputRequest";
    public static final String FIELD_USER_INPUT_ANSWER                  = "userInputAnswer";
    public static final String FIELD_USER_INPUT_HISTORY_ID              = "request_history_id";
    public static final String FIELD_ANSWER_TEXT                        = "answer_text";
    public static final String FIELD_QUESTION_ID                        = "question_id";
    public static final String FIELD_TITLE                              = "title";
    public static final String FIELD_QUESTIONS                          = "questions";
    public static final String FIELD_QUESTION                           = "question";
    public static final String FIELD_OPTIONS                            = "options";
    public static final String FIELD_LABEL                              = "label";
    public static final String FIELD_ALLOW_CUSTOM                       = "allow_custom";
    public static final String FIELD_SUBMIT_LABEL                       = "submit_label";
    public static final String FIELD_SCHEMA_VERSION                     = "schema_version";
    public static final String FIELD_USER_INPUT_REQUEST_ID              = "request_id";

    private AiSqlConstants(){
    }
}
