package com.clougence.clouddm.sdk.approval;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalCreateInstanceResult {

    private String                 approvalIdentity;
    private ApprovalUrl            approvalUrl;
    private List<ApprovalActivity> activityList;
}
