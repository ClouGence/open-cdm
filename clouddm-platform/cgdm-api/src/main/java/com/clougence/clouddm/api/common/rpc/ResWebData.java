package com.clougence.clouddm.api.common.rpc;

import java.io.Serializable;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResWebData<T> implements Serializable {

    private String  code;
    private String  msg;
    private T       data;

    private boolean permission;
    private String  permissionI18n;
    private String  needResource;
    private String  needAuthKey;

    private String  requestId;

    public ResWebData(String code, String msg, String requestId, T data){
        this.code = code;
        this.msg = msg;
        this.requestId = requestId;
        this.data = data;
        this.permission = true;
    }

    public ResWebData(){
    }

    public boolean isSuccess() { return ResultEnum.SUCCESS.getCode().equals(code); }

    public boolean isFail() { return !ResultEnum.SUCCESS.getCode().equals(code); }
}
