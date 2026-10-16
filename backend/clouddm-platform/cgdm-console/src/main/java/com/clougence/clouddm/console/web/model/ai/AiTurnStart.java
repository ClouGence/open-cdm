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

import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.platform.dal.model.ai.DmAiSessionDO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiTurnStart {
    private final DmAiSessionDO  session;

    private final AiTurnRecordVO turn;

    private final boolean        existing;

    @JsonCreator
    public AiTurnStart(@JsonProperty("session") DmAiSessionDO session, @JsonProperty("turn") AiTurnRecordVO turn, @JsonProperty("existing") boolean existing){
        this.session = session;
        this.turn = turn;
        this.existing = existing;
    }
}
