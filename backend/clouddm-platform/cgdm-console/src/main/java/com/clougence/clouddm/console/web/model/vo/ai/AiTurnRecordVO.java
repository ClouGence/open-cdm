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

import java.util.Date;

import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiTurnRecordVO {

    private Long         historyId;
    private Long         sessionDbId;
    private String       runId;
    private long         eventSeq;
    private String       id;
    private String       sessionId;
    private String       requestId;
    private AiTurnStatus status;
    private String       prompt;
    private String       contextJson;
    private String       engineId;
    private int          engineVersion;
    private String       engineName;
    private String       resultJson;
    private String       userInputAnswerJson;
    private String       referencedObjectsJson;
    private String       usageJson;
    private String       errorCode;
    private String       errorMessage;
    private Date         deadline;
    private Date         gmtCreate;
    private Date         gmtModified;
}
