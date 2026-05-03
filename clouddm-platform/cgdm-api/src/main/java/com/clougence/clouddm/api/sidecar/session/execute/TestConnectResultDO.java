package com.clougence.clouddm.api.sidecar.session.execute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @version : 2014年10月25日
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestConnectResultDO {

    private String  resultMessage;
    private boolean success;

    public TestConnectResultDO(){
    }

    public TestConnectResultDO(boolean success, String resultMessage){
        this.success = success;
        this.resultMessage = resultMessage;
    }
}
