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

import java.io.IOException;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/** Decode provider envelopes before falling back to HTTP status classification. */
public final class ChatHttpErrorDecoder {

    private ChatHttpErrorDecoder(){
    }

    public static ThirdPartyApiException decode(int status, String body) {
        IOException cause = new IOException("Model server HTTP status: " + status);
        try {
            JsonNode error = JsonUtils.defaultObjectMapper().readTree(body);
            if (error != null) {
                String code = error.path(ChatProtocolConstants.FIELD_ERROR_CODE).asText();
                if (ChatProtocolConstants.ERROR_CONTENT_FILTER.equals(error.path(ChatProtocolConstants.FIELD_ERROR).path(ChatProtocolConstants.FIELD_ERROR_TYPE).asText())
                    || ChatProtocolConstants.ERROR_DATA_INSPECTION_FAILED.equals(code) || ChatProtocolConstants.ERROR_CONTENT_FILTER.equals(code)) {
                    return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_MODEL_CONTENT_FILTERED);
                }
            }
        } catch (JsonProcessingException ignored) {
            // Non-JSON HTTP errors still have a meaningful status code.
        }
        if (status == 401 || status == 403) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_AUTH);
        }
        if (status == 404) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_MODEL_NOT_FOUND);
        }
        if (status == 408) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT);
        }
        if (status == 429) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_RATE_LIMIT);
        }
        if (status >= 500 && status < 600) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_UNAVAILABLE);
        }
        if (status >= 400 && status < 500) {
            return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_PROVIDER_INVALID_REQUEST);
        }
        return ThirdPartyApiException.as().with(cause, ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
    }
}
