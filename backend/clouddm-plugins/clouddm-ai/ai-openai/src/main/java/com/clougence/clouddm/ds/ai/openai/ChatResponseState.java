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
package com.clougence.clouddm.ds.ai.openai;

import java.util.Map;
import java.util.TreeMap;

import com.clougence.clouddm.ds.ai.ChatTextAccumulator;
import com.clougence.clouddm.ds.ai.ChatUsage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ChatResponseState {
    private final Map<Integer, ChatToolCallBuffer> tools            = new TreeMap<>();
    private final StringBuilder                    text             = new StringBuilder();
    private final StringBuilder                    reasoningContent = new StringBuilder();
    private final ChatTextAccumulator              content          = new ChatTextAccumulator(text, reasoningContent);
    private int                                    fallbackToolCallIndex;
    private String                                 finishReason;
    private boolean                                contentFiltered;
    private ChatUsage                              usage;
}
