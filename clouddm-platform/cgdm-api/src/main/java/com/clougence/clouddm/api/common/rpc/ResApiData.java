package com.clougence.clouddm.api.common.rpc;

import java.io.Serializable;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResApiData<T> implements Serializable {

    private String code;
    private String msg;
    private T      data;

    /**
     * Rsocket request will carry the requestId to help fetch the async result for sender. Other normal request, the field can be null.//todo need change to use requestId in workerIdentity
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestId;

    /**
     * Rsocket request will carry the wsn to help the rsocket server return async result to related sidecar. Other normal request, the field can be null.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workerSeqNumber;

    public ResApiData(String code, String msg, String requestId, T data){
        this.code = code;
        this.msg = msg;
        this.requestId = requestId;
        this.data = data;
    }

    public ResApiData(){
    }

    public boolean isSuccess() { return ResultEnum.SUCCESS.getCode().equals(code); }

    public boolean isFail() { return !ResultEnum.SUCCESS.getCode().equals(code); }
}
