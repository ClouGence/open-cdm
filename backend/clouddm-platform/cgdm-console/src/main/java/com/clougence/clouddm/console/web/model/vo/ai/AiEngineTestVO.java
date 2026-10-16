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
package com.clougence.clouddm.console.web.model.vo.ai;

import java.time.Instant;

import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiEngineTestVO {
    private final int                  version;

    private final AiVerificationStatus verificationStatus;

    private final Instant              verifiedAt;

    private final String               errorCode;

    @JsonCreator
    public AiEngineTestVO(@JsonProperty("version") int version, @JsonProperty("verificationStatus") AiVerificationStatus verificationStatus,
                          @JsonProperty("verifiedAt") Instant verifiedAt, @JsonProperty("errorCode") String errorCode){
        this.version = version;
        this.verificationStatus = verificationStatus;
        this.verifiedAt = verifiedAt;
        this.errorCode = errorCode;
    }
}
