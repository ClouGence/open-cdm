package com.clougence.clouddm.team.provider.feishu.domain.ro.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCallback {

    private String uuid;
    private Event  event;
    private String token;
    private String ts;
    private String type;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {

        @JsonProperty("app_id")
        private String appId;
        @JsonProperty("approval_code")
        private String approvalCode;
        @JsonProperty("custom_key")

        private String customKey;
        @JsonProperty("def_key")
        private String defKey;
        @JsonProperty("generateType")
        private String generateType;
        @JsonProperty("instance_code")
        private String instanceCode;
        @JsonProperty("open_id")
        private String openId;
        @JsonProperty("operate_time")
        private String operateTime;
        private String status;
        @JsonProperty("task_id")
        private String taskId;
        @JsonProperty("tenant_key")
        private String tenantKey;
        private String type;
        @JsonProperty("user_id")
        private String userId;
    }
}
