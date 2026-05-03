package com.clougence.clouddm.sdk.approval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.approval.RdpApprovalActivityInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalInstanceInfo {

    private Map<String, List<RdpApprovalActivityInfo>> map = new HashMap<>();

    private ApprovalInstanceStatus                     status;

}
