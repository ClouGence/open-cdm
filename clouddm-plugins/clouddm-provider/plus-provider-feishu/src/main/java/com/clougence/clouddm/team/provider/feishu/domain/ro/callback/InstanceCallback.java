package com.clougence.clouddm.team.provider.feishu.domain.ro.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstanceCallback {

    private String ts;
    private String uuid;
    private String token;
    private String type;
    private Event  event;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {

        @JsonProperty("app_id")
        private String app_id;
        @JsonProperty("tenant_key")
        private String tenantKey;
        private String type;
        @JsonProperty("approval_code")
        private String approvalCode;
        @JsonProperty("instance_code")
        private String instanceCode;
        private String status;
        @JsonProperty("operate_time")
        private String operateTime;
        @JsonProperty("instance_operate_time")
        private String instanceOperateTime;
        private String uuid;
    }
}
