package com.clougence.clouddm.sdk.security.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String loginAccount;
    private String loginPassword;
    private String loginVerifyCode;

    // for OIDC
    private String authCode;
    private String accessToken;
    private String jumpUrl;
}
