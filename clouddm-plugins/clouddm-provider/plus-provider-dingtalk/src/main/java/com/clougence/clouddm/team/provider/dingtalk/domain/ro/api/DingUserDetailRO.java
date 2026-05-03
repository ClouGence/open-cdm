package com.clougence.clouddm.team.provider.dingtalk.domain.ro.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Only save needed fields
 *
 * @author wanshao create time is 2021/2/1
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingUserDetailRO extends DingApiRO {

    @JsonProperty("request_id")
    private String     requestId;

    @JsonProperty("result")
    private ResultInfo resultInfo;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultInfo {

        @JsonProperty("dept_id_list")
        private List<String> deptIdList;

        @JsonProperty("name")
        private String       userName;
    }

}
