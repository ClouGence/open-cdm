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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/** Raw fragments retained for interruption recovery; arguments may be incomplete JSON. */
@Getter
public final class ChatPartialToolCall {
    private final Integer index;

    private final String  id;

    private final String  name;

    private final String  arguments;

    @JsonCreator
    public ChatPartialToolCall(@JsonProperty("index") Integer index, @JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("arguments") String arguments){
        this.index = index;
        this.id = id;
        this.name = name;
        this.arguments = arguments;
    }
}
