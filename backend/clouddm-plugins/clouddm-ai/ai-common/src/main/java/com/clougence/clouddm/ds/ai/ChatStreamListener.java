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

import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/** Callbacks run on the stream worker; interrupted snapshots are provisional and never executable. */
@Getter
public final class ChatStreamListener {
    private final Consumer<String>                 text;

    private final Consumer<String>                 reasoning;

    private final Consumer<ChatStreamInterruption> interrupted;

    @JsonCreator
    public ChatStreamListener(@JsonProperty("text") Consumer<String> text, @JsonProperty("reasoning") Consumer<String> reasoning,
                              @JsonProperty("interrupted") Consumer<ChatStreamInterruption> interrupted){
        this.text = text;
        this.reasoning = reasoning;
        this.interrupted = interrupted;
    }

    public ChatStreamListener(Consumer<String> text){
        this(text, value -> {
        }, value -> {
        });
    }
}
