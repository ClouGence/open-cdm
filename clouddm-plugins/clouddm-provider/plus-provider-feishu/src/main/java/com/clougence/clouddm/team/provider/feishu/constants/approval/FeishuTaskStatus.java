package com.clougence.clouddm.team.provider.feishu.constants.approval;

import com.clougence.clouddm.sdk.service.approval.RdpApprovalActivityStatus;
import com.clougence.utils.StringUtils;

public enum FeishuTaskStatus {

    PENDING,
    APPROVED,
    REJECTED,
    TRANSFERRED,
    DONE;

    public static FeishuTaskStatus getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (FeishuTaskStatus status : FeishuTaskStatus.values()) {
            if (status.name().equals(result)) {
                return status;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }

    public RdpApprovalActivityStatus convertStatus() {
        switch (this) {
            case PENDING: {
                return RdpApprovalActivityStatus.RUNNING;
            }
            case APPROVED: {
                return RdpApprovalActivityStatus.COMPLETED;
            }
            case REJECTED: {
                return RdpApprovalActivityStatus.REFUSE;
            }
            case TRANSFERRED: {
                return RdpApprovalActivityStatus.CLOSE;
            }
            case DONE: {
                return RdpApprovalActivityStatus.CANCELED;
            }

        }
        return null;
    }
}
