package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

/**
 * @author wanshao create time is 2021/2/1
 **/
public enum DingTaskStatus {

    NEW,

    RUNNING,

    PAUSED,

    CANCELED,

    COMPLETED,

    TERMINATED;

    public static DingTaskStatus getByName(String status) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(status)) {
            return null;
        }
        status = status.toUpperCase();

        for (DingTaskStatus dingTaskStatus : DingTaskStatus.values()) {
            if (dingTaskStatus.name().equals(status)) {
                return dingTaskStatus;
            }
        }
        return null;
    }
}
