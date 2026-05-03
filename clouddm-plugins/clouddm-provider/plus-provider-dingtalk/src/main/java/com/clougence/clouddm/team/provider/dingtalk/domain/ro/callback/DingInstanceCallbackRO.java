package com.clougence.clouddm.team.provider.dingtalk.domain.ro.callback;

import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingInstanceResult;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingInstanceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class DingInstanceCallbackRO {

    private DingInstanceType   type;
    private DingInstanceResult result;
    private Long               finishTime;
    private String             processInstanceId;
}
