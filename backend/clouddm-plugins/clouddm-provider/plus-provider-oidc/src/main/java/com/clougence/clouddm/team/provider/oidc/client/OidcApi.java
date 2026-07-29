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
package com.clougence.clouddm.team.provider.oidc.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.team.provider.oidc.constants.OidcI18nKey;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * https://keycloak.java.net.cn/securing-apps/oidc-layers
 * https://openid.net/specs/openid-connect-core-1_0.html
 */
@Slf4j
public class OidcApi {

    private final OidcClient          client;
    private final String              primaryUid;
    private final OidcCfg             conf;
    private final OidcIdTokenVerifier verifier;
    private String                    loginEndpoint;
    private String                    tokenEndpoint;
    private String                    logoutEndpoint;

    //

    OidcApi(OidcClient client, String primaryUid, OidcCfg conf){
        this.client = client;
        this.primaryUid = primaryUid;
        this.conf = conf;
        this.verifier = new OidcIdTokenVerifier(conf);
    }

    public OidcCfg getConf() { return conf; }

    void initWellKnown() {
        try {
            // fetch well-known
            JSONObject wellKnownJson = this.client.callApi((client, http) -> {
                Request request = new Request.Builder().url(this.conf.getWellKnownURL()).build();
                Response response = http.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, response.code() + ":" + response.message());
                }

                return JSONObject.parseObject(response.body().string());
            });
            log.info("oidcWellKnownJson primaryUid：" + this.primaryUid + " is " + JsonUtils.toJson(wellKnownJson));

            // authMethod support check
            // JSONArray authMethodType = wellKnownJson.getJSONArray("token_endpoint_auth_methods_supported");
            // List<String> typeSet = authMethodType.toList(String.class);
            // if (!typeSet.contains("client_secret_basic")) {
            //     throw ThirdPartyApiException.asRDP().with(OidcI18nKey.OIDC_VERIFY_AUTH_METHOD_ERROR, "client_secret_basic");
            // }

            // fetch jwks
            String jwksUrl = (String) wellKnownJson.get("jwks_uri");
            if (StringUtils.isBlank(jwksUrl)) {
                throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_WELLKNOWN_MISSING_JWKS_URI_ERROR);
            }
            JSONObject jwksJson = this.client.callApi((client, http) -> {
                Request request = new Request.Builder().url(jwksUrl).build();
                Response response = http.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, response.code() + ":" + response.message());
                }

                return JSONObject.parseObject(response.body().string());
            });

            // 2. fetch token by code
            this.loginEndpoint = (String) wellKnownJson.get("authorization_endpoint");
            this.tokenEndpoint = (String) wellKnownJson.get("token_endpoint");
            this.logoutEndpoint = (String) wellKnownJson.get("end_session_endpoint");
            this.verifier.initialize((String) wellKnownJson.get("issuer"), jwksJson);
            log.info("oidcJwkList primaryUid：" + this.primaryUid + ", sigJwk keys [" + //
                     StringUtils.join(this.verifier.getSigningKeyIds(), ",") + "];");
        } catch (ThirdPartyApiException e) {
            if (StringUtils.equals(e.getMessageKey(), OidcI18nKey.OIDC_UNKNOWN_CALL_API_ERROR)) {
                throw ThirdPartyApiException.as().with(e, OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, e.getMessage());
        }
    }

    public String getJumpUrl(String state, String jumpUrl) throws UnsupportedEncodingException {
        return this.loginEndpoint + "?" +//
               "response_type=code&" +//
               "client_id=" + URLEncoder.encode(this.conf.getClientId(), "UTF-8") + "&" +//
               "scope=" + URLEncoder.encode(this.conf.getScope(), "UTF-8") + "&" +//
               "state=" + URLEncoder.encode(state, "UTF-8") + "&" +//
               "redirect_uri=" + URLEncoder.encode(jumpUrl, "UTF-8");
    }

    public String getLogoutJumpUrl(String status, String jumpUrl, String idToken) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(this.logoutEndpoint)) {
            return jumpUrl + "&state=" + URLEncoder.encode(status, "UTF-8");
        }

        return this.logoutEndpoint + "?" +//
               "id_token_hint=" + URLEncoder.encode(StringUtils.defaultString(idToken, ""), "UTF-8") + "&" +//
               "client_id=" + URLEncoder.encode(this.conf.getClientId(), "UTF-8") + "&" +//
               "state=" + URLEncoder.encode(status, "UTF-8") + "&" +//
               "post_logout_redirect_uri=" + URLEncoder.encode(jumpUrl, "UTF-8");
    }

    public String fetchIdToken(String code, String jumpUrl) {
        if (StringUtils.isBlank(code)) {
            throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_TOKEN_ERROR);
        }

        JSONObject fetchToken = this.client.callApi((client, http) -> {
            Request request = new Request.Builder().url(this.tokenEndpoint) //
                .header("content-type", "application/x-www-form-urlencoded")
                .post(new FormBody.Builder()//
                    .addEncoded("grant_type", "authorization_code")
                    .addEncoded("client_id", this.conf.getClientId())
                    .addEncoded("client_secret", this.conf.getClientSecret())
                    .addEncoded("code", code)
                    .addEncoded("redirect_uri", StringUtils.defaultIfBlank(jumpUrl, ""))//https://www.rfc-editor.org/rfc/rfc6749.txt [Page 33] description of redirect_uri
                    .build())
                .build();

            Response response = http.newCall(request).execute();
            if (!response.isSuccessful()) {
                JSONObject errorData = JSONObject.parseObject(response.body().string());
                String errorCode = errorData.getString("error");
                String errorDesc = errorData.getString("error_description");
                throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_UNKNOWN_CALL_API_ERROR, errorCode + ":" + errorDesc);
            }

            return JSONObject.parseObject(response.body().string());
        });

        String idToken = fetchToken.getString("id_token");

        try {
            this.verifier.verify(idToken);
            return idToken;
        } catch (Exception e) {
            log.error("oidc verify ID Token failed, " + e.getMessage());
            throw ThirdPartyApiException.as().with(e, OidcI18nKey.OIDC_VERIFY_TOKEN_ERROR, e.getMessage());
        }
    }

    private static final String OIDC_UID      = "sub";
    private static final String OIDC_UserName = "name";
    private static final String OIDC_Account  = "preferred_username";

    private static final String OIDC_Mail     = "email";
    private static final String OIDC_Phone    = "phone_number";
    private static final String OIDC_Picture  = "picture";

    public UserData fetchUserInfo(String idToken) {
        DecodedJWT decodedJWT = JWT.decode(idToken);
        String externalUID = decodedJWT.getClaim(OIDC_UID).asString();
        if (StringUtils.isBlank(externalUID)) {
            throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_VERIFY_TOKEN_MISSING_UNIQUE_ID_ERROR);
        }

        UserData userData = new UserData();
        userData.setExternalUID(externalUID);
        userData.setUserName(decodedJWT.getClaim(OIDC_UserName).asString());
        userData.setEmail(decodedJWT.getClaim(OIDC_Mail).asString());
        userData.setPhone(decodedJWT.getClaim(OIDC_Phone).asString());
        userData.setBindAccount(decodedJWT.getClaim(OIDC_Account).asString());
        userData.setAccessToken(idToken);
        return userData;
    }
}
