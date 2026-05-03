package com.clougence.clouddm.sdk.security.login;

import lombok.Getter;

@Getter
public class LoginExtract {

    private final String extractAccount;
    private final String extractDomain;

    public LoginExtract(String extractAccount, String extractDomain){
        this.extractAccount = extractAccount;
        this.extractDomain = extractDomain;
    }
}
