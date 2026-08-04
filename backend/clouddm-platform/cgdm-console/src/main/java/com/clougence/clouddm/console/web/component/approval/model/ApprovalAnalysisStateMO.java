package com.clougence.clouddm.console.web.component.approval.model;

import java.util.List;

import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalAnalysisStateMO {
    public static final String          TYPE_SQL_RECOGNITION   = "SQL_RECOGNITION";
    public static final String          TYPE_BEHAVIOR_ANALYSIS = "BEHAVIOR_ANALYSIS";
    public static final String          TYPE_SECURITY_RULE     = "SECURITY_RULE";
    public static final String          STATUS_INIT            = "INIT";
    public static final String          STATUS_RUNNING         = "RUNNING";
    public static final String          STATUS_FINISHED        = "FINISHED";
    public static final String          STATUS_FAILED          = "FAILED";

    private String                      analysisType;
    private String                      analysisStatus;
    private Long                        startTimeUtc;
    private Long                        finishTimeUtc;
    private Long                        processedCount;
    private String                      errorMessage;
    private Long                        totalCount;
    private List<ApprovalBehavior>      behaviors;
    private List<TicketRuleCheckResult> checkedInfo;

    public ApprovalAnalysisStateMO(String analysisType){
        this.analysisType = analysisType;
        this.analysisStatus = STATUS_INIT;
    }

    public static List<ApprovalAnalysisStateMO> initialStates() {
        return List.of(new ApprovalAnalysisStateMO(TYPE_SQL_RECOGNITION), new ApprovalAnalysisStateMO(TYPE_BEHAVIOR_ANALYSIS), new ApprovalAnalysisStateMO(TYPE_SECURITY_RULE));
    }
}
