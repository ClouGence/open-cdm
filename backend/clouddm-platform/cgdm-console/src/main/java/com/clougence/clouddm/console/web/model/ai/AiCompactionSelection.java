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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiCompactionSelection {
    private final List<AiCompactionBlock> blocks;

    private final int                     historyCount;

    private final int                     currentMessageCount;

    private final Long                    anchorId;

    @JsonCreator
    public AiCompactionSelection(@JsonProperty("blocks") List<AiCompactionBlock> blocks, @JsonProperty("historyCount") int historyCount,
                                 @JsonProperty("currentMessageCount") int currentMessageCount, @JsonProperty("anchorId") Long anchorId){
        this.blocks = blocks;
        this.historyCount = historyCount;
        this.currentMessageCount = currentMessageCount;
        this.anchorId = anchorId;
    }
}
