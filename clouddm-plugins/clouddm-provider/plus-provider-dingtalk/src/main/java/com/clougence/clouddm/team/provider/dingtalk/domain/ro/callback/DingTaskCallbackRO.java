package com.clougence.clouddm.team.provider.dingtalk.domain.ro.callback;

import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingActivityResult;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingActivityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class DingTaskCallbackRO {

    private Long               taskId;
    private Long               finishTime;
    private String             remark;
    private String             staffId;
    private DingActivityResult result;
    private DingActivityType   type;
    private String             activityId;
    private String             processInstanceId;
    private Long               createTime;
}
