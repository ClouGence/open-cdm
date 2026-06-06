/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.sdk.security.login;

import com.clougence.clouddm.sdk.LifeSpi;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
public interface LoginProviderSpi extends LifeSpi {

    int DEFAULT_ORDER = 100;

    default int order() {
        return DEFAULT_ORDER;
    }

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
