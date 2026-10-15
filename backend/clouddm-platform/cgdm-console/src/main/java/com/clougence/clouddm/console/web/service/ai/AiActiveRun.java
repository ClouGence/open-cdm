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
package com.clougence.clouddm.console.web.service.ai;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.console.web.model.ai.AiContextReference;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.platform.dal.model.ai.DmAiSessionDO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class AiActiveRun {
    private final String                      puid;
    private final String                      uid;
    private final DmAiSessionDO               session;
    private final AiTurnRecordVO              turn;
    private final AiRunHandle                 handle;
    private final AiPermit                    permit;
    private final AiRunEvents                 events;
    private final Locale                      locale;
    private final AtomicBoolean               published      = new AtomicBoolean();
    private final AtomicBoolean               workerFinished = new AtomicBoolean();
    @Setter
    private volatile List<AiContextReference> references     = List.of();
}
