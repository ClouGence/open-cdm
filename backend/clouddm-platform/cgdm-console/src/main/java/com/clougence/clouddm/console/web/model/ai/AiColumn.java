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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiColumn {
    private final String  name;

    private final String  type;

    private final Long    charLength;

    private final Integer numericPrecision;

    private final Integer numericScale;

    private final Boolean numericUnsigned;

    private final Integer arrayDimension;

    private final Integer datetimePrecision;

    private final String  comment;

    @JsonCreator
    public AiColumn(@JsonProperty("name") String name, @JsonProperty("type") String type, @JsonProperty("charLength") Long charLength,
                    @JsonProperty("numericPrecision") Integer numericPrecision, @JsonProperty("numericScale") Integer numericScale,
                    @JsonProperty("numericUnsigned") Boolean numericUnsigned, @JsonProperty("arrayDimension") Integer arrayDimension,
                    @JsonProperty("datetimePrecision") Integer datetimePrecision, @JsonProperty("comment") String comment){
        this.name = name;
        this.type = type;
        this.charLength = charLength;
        this.numericPrecision = numericPrecision;
        this.numericScale = numericScale;
        this.numericUnsigned = numericUnsigned;
        this.arrayDimension = arrayDimension;
        this.datetimePrecision = datetimePrecision;
        this.comment = comment;
    }
}
