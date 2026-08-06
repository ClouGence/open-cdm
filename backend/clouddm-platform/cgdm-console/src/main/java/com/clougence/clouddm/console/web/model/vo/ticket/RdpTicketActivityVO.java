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
package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.component.approval.model.TicketRuleCheckResult;
import com.clougence.clouddm.console.web.constants.RdpTicketProcessActivityStatus;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpTicketActivityVO {

    private Long                           processActivityId;
    private Long                           processId;
    private Long                           ticketId;
    private String                         gmtCreate;
    private String                         gmtModified;
    private RdpTicketProcessActivityStatus activityStatus;
    private String                         activityTitle;
    private Integer                        displayOrder;
    private String                         finishTime;
    private String                         remark;
    private List<String>                   approvalUserList;
    private String                         startTime;
    private Long                           startTimeUtc;
    private Long                           finishTimeUtc;
    private Long                           processedCount;
    private Long                           processedBytes;
    private Long                           totalBytes;
    private Long                           statementCount;
    private Long                           objectCount;
    private Long                           behaviorCount;
    private Long                           ruleCount;
    private Map<String, Long>              statementTypeCounts;
    private List<ApprovalBehavior>         behaviors;
    private List<TicketRuleCheckResult>    ruleResults;
}
