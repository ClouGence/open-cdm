package com.clougence.clouddm.sdk.security.login;

import com.clougence.clouddm.sdk.LifeSpi;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
public interface LoginProviderSpi extends LifeSpi {

    String loginExtractDomain(String fullLoginName);

    // for use account and password login, and use api fetch userinfo.
    //    -- like them: LDAP,AD,...
    String loginExtractAccount(String fullLoginName);

    // for login to jump outside website, and use callBack fetch userinfo.
    //    -- like them: Auth2.0,DingTalk,Google,Wechat,...
    String loginJumpUrl(String primaryUID, String status, String jumpUrl) throws Exception;

    // for logout to jump outside website, and use callBack close self session.
    //    -- like them: OIDC,Keycloak,...
    String logoutJumpUrl(String primaryUID, String status, String jumpUrl, String accessToken) throws Exception;

    // login and fetch userinfo.
    LoginResponse authLogin(String primaryUID, LoginRequest request);
}
