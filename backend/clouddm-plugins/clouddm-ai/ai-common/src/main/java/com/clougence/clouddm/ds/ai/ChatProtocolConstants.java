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
package com.clougence.clouddm.ds.ai;

public final class ChatProtocolConstants {
    public static final String FIELD_TOOL_CALLS                 = "tool_calls";
    public static final String FIELD_FUNCTION                   = "function";
    public static final String FIELD_ARGUMENTS                  = "arguments";
    public static final String FIELD_REASONING_CONTENT          = "reasoning_content";
    public static final String PARAMETER_MAX_COMPLETION_TOKENS  = "max_completion_tokens";
    public static final String PARAMETER_TEMPERATURE            = "temperature";
    public static final String PARAMETER_TOP_P                  = "top_p";
    public static final String PARAMETER_REASONING_EFFORT       = "reasoning_effort";
    public static final String PARAMETER_THINKING               = "thinking";

    public static final String FIELD_INPUT_TOKEN_DETAILS        = "input_tokens_details";
    public static final String FIELD_PROMPT_TOKEN_DETAILS       = "prompt_tokens_details";
    public static final String FIELD_OUTPUT_TOKEN_DETAILS       = "output_tokens_details";
    public static final String FIELD_COMPLETION_TOKEN_DETAILS   = "completion_tokens_details";
    public static final String FIELD_CACHED_TOKENS              = "cached_tokens";
    public static final String FIELD_REASONING_TOKENS           = "reasoning_tokens";
    public static final String FIELD_ERROR                      = "error";
    public static final String FIELD_ERROR_TYPE                 = "type";
    public static final String FIELD_ERROR_CODE                 = "code";
    public static final String ERROR_CONTENT_FILTER             = "ContentFilter";
    public static final String ERROR_DATA_INSPECTION_FAILED     = "DataInspectionFailed";

    public static final String ERROR_PROVIDER_AUTH              = "PROVIDER_AUTH";
    public static final String ERROR_PROVIDER_MODEL_NOT_FOUND   = "PROVIDER_MODEL_NOT_FOUND";
    public static final String ERROR_PROVIDER_INVALID_REQUEST   = "PROVIDER_INVALID_REQUEST";
    public static final String ERROR_PROVIDER_UNRESOLVED_SERVER = "PROVIDER_UNRESOLVED_SERVER";
    public static final String ERROR_PROVIDER_RATE_LIMIT        = "PROVIDER_RATE_LIMIT";
    public static final String ERROR_PROVIDER_UNAVAILABLE       = "PROVIDER_UNAVAILABLE";
    public static final String ERROR_PROVIDER_TIMEOUT           = "PROVIDER_TIMEOUT";
    public static final String ERROR_MODEL_PROTOCOL             = "MODEL_PROTOCOL";
    public static final String ERROR_MODEL_CONTENT_FILTERED     = "MODEL_CONTENT_FILTERED";

    private ChatProtocolConstants(){
    }
}
