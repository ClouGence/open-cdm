package com.clougence.clouddm.team.provider.feishu.constants.approval;

import com.clougence.clouddm.sdk.approval.ApprovalInstanceStatus;
import com.clougence.utils.StringUtils;

public enum FeishuInstanceStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    DELETED;

    public static FeishuInstanceStatus getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (FeishuInstanceStatus status : FeishuInstanceStatus.values()) {
            if (status.name().equals(result)) {
                return status;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }

    public ApprovalInstanceStatus convertStatus() {
        switch (this) {
            case PENDING: {
                return ApprovalInstanceStatus.RUNNING;
            }
            case APPROVED: {
                return ApprovalInstanceStatus.COMPLETED;
            }
            case REJECTED: {
                return ApprovalInstanceStatus.REFUSE;
            }
            case CANCELED: {
                return ApprovalInstanceStatus.CANCELED;
            }
            case DELETED: {
                return ApprovalInstanceStatus.TERMINATED;
            }
        }
        return null;
    }
}
