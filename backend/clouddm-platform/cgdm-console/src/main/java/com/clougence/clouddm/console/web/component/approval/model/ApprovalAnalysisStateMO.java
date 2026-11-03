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
package com.clougence.clouddm.console.web.component.approval.model;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalAnalysisStateMO {
    public static final String          TYPE_SQL_RECOGNITION   = "SQL_RECOGNITION";
    public static final String          TYPE_BEHAVIOR_ANALYSIS = "BEHAVIOR_ANALYSIS";
    public static final String          TYPE_SECURITY_RULE     = "SECURITY_RULE";
    public static final String          TYPE_DML_EXPLAIN       = "DML_EXPLAIN";
    public static final String          STATUS_INIT            = "INIT";
    public static final String          STATUS_RUNNING         = "RUNNING";
    public static final String          STATUS_FINISHED        = "FINISHED";
    public static final String          STATUS_FAILED          = "FAILED";
    public static final String          PHASE_PREPARING        = "PREPARING";
    public static final String          PHASE_ANALYZING        = "ANALYZING";

    private String                      analysisType;
    private String                      runId;
    private Integer                     displayOrder;
    private String                      analysisStatus;
    private String                      analysisPhase;
    private Long                        startTimeUtc;
    private Long                        finishTimeUtc;
    private Long                        processedCount;
    private Long                        processedBytes;
    private Long                        totalBytes;
    private String                      errorMessage;
    private Long                        totalCount;
    // SQL recognition
    private Map<String, Long>           statementTypeCounts;
    // Behavior analysis
    private Long                        behaviorCount;
    private List<ApprovalBehavior>      behaviors;
    // Security rule
    private List<TicketRuleCheckResult> checkedInfo;
    // DML explain
    private Long                        dmlStatementCount;
    private Long                        executedExplainCount;
    private Long                        skippedBySizeLimit;
    private Long                        skippedByCountLimit;
    private Long                        failedExplainCount;
    private List<DmlExplainResultMO>    explainResults;

    public ApprovalAnalysisStateMO(){
    }

    public ApprovalAnalysisStateMO(String analysisType){
        this.analysisType = analysisType;
        this.analysisStatus = STATUS_INIT;
    }

    public ApprovalAnalysisStateMO(String analysisType, int displayOrder){
        this(analysisType);
        this.displayOrder = displayOrder;
    }
}
