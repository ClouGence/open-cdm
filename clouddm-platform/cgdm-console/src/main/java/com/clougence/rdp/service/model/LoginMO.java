package com.clougence.rdp.service.model;

import com.clougence.rdp.controller.model.fo.LoginAutoRegisterFO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMO {

    private boolean             success;

    private boolean             needMore;

    private boolean             needMfa;

    private String              mfaPreActionToken;

    private LoginAutoRegisterFO moreInfo;

    private String              errMsg;

    private String              uid;

    private String              puid;

    private String              username;

    private String              token;

    private String              requestId;

    public LoginMO(){
    }

    public LoginMO(boolean success, String errMsg){
        this.success = success;
        this.errMsg = errMsg;
    }

    public LoginMO(boolean success, String errMsg, String requestId){
        this.success = success;
        this.errMsg = errMsg;
        this.requestId = requestId;
    }
}
