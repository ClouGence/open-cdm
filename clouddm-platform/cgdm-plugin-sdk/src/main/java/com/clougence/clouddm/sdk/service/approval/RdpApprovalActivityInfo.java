package com.clougence.clouddm.sdk.service.approval;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RdpApprovalActivityInfo {

    private RdpApprovalActivityStatus status;
    private String                    userName;
    private String                    remark;
    private Date                      finishTime;
    private Date                      startTime;
    private String                    userId;
    private String                    activityId;
    private String                    taskId;

    // find ticket
    private String                    puid;
    private String                    approvalIdentity;
    private String                    platform;

}
