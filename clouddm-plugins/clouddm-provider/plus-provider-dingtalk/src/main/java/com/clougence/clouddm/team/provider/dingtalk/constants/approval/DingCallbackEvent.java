package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingCallbackEvent {

    BPMS_TASK_CHANGE("bpms_task_change"),
    BPMS_INSTANCE_CHANGE("bpms_instance_change");

    String eventType;

    DingCallbackEvent(String eventType){
        this.eventType = eventType;
    }

    public static DingCallbackEvent getByName(String eventType) {
        if (StringUtils.isBlank(eventType)) {
            return null;
        }

        for (DingCallbackEvent dingApproResult : DingCallbackEvent.values()) {
            if (dingApproResult.eventType.equals(eventType)) {
                return dingApproResult;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + eventType);
    }
}
