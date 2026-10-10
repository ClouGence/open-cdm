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
package com.clougence.sql.kafka;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import lombok.Getter;

@Getter
public class KafkaParseException extends ThirdPartyApiException {
    private final int start;
    private final int end;

    public KafkaParseException(int start, int end, String key, Object... args){
        super(null, key, ThirdPartyApiErrorType.OTHER, key, args);
        this.start = start;
        this.end = end;
    }
}
