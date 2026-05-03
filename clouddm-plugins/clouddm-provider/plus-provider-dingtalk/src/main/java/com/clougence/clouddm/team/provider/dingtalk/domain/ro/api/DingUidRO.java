package com.clougence.clouddm.team.provider.dingtalk.domain.ro.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/2/1
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingUidRO extends DingApiRO {

    @JsonProperty("request_id")
    private String  requestId;

    @JsonProperty("result")
    private DingUid dingUidInfo;

    @Data
    public class DingUid {

        @JsonProperty("userid")
        private String userId;
    }

}
