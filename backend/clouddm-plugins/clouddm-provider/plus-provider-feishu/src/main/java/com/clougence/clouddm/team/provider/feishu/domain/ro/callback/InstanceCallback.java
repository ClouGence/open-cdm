/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
