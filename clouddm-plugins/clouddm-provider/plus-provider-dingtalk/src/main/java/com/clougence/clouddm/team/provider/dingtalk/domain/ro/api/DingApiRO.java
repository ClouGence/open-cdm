package com.clougence.clouddm.team.provider.dingtalk.domain.ro.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 15:56
*/
@Getter
@Setter
@Builder
@Slf4j
public class DingApiRO {

    @Tolerate
    public DingApiRO(){
    }

    @JsonProperty("errcode")
    private Long   errCode;

    @JsonProperty("errmsg")
    private String errMsg;

    public static void checkSuccess(DingApiRO dingApiRO) {

        if (dingApiRO.errCode != 0) {
            throw new RuntimeException(dingApiRO.errMsg);

        }
    }
}
