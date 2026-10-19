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

import org.hibernate.validator.constraints.CodePointLength;

import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class AiObject {
    @NotNull
    private final UmiTypes type;

    @NotNull
    @CodePointLength(min = 1, max = 512)
    private final String   name;

    @JsonCreator
    public AiObject(@JsonProperty("type") UmiTypes type, @JsonProperty("name") String name){
        this.type = type;
        this.name = name;
    }
}
