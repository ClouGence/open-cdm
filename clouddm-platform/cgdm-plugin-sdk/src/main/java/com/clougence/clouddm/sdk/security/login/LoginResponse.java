package com.clougence.clouddm.sdk.security.login;

import com.clougence.clouddm.sdk.service.config.UserData;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final UserData loginUser;
    private final boolean  success;
    private final String   errMsg;

    public LoginResponse(UserData loginUser, boolean success){
        this.loginUser = loginUser;
        this.success = success;
        this.errMsg = null;
    }

    public LoginResponse(UserData loginUser, boolean success, String errMsg){
        this.loginUser = loginUser;
        this.success = success;
        this.errMsg = errMsg;
    }
}
