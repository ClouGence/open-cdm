package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingTaskResult {

    AGREE,

    REFUSE,
    NONE,

    REDIRECTED;

    public static DingTaskResult getByName(String status) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(status)) {
            return null;
        }
        status = status.toUpperCase();

        for (DingTaskResult dingTaskStatus : DingTaskResult.values()) {
            if (dingTaskStatus.name().equals(status)) {
                return dingTaskStatus;
            }
        }
        return null;
    }
}
