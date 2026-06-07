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
package com.clougence.clouddm.team.provider.oidc.auth;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.team.provider.oidc.client.OidcApi;
import com.clougence.clouddm.team.provider.oidc.client.OidcCfg;
import com.clougence.clouddm.team.provider.oidc.client.OidcClient;
import com.clougence.clouddm.team.provider.oidc.constants.OidcI18nKey;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://keycloak.java.net.cn/securing-apps/oidc-layers
 * https://openid.net/specs/openid-connect-core-1_0.html
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class OidcLoginProviderSpi implements LoginProviderSpi {

    protected final ConsoleConfigService    configService;
    private final   Map<String, OidcClient> clientMap;

    public OidcLoginProviderSpi(ConsoleConfigService configService){
        this.configService = configService;
        this.clientMap = new ConcurrentHashMap<>();
    }

    @Override
    public int order() {
        return 30;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) throws Exception {
        // fetch config
        OidcCfg conf = OidcCfg.fetchConfig(this.configService, ownerUid);

        // enable is false.
        if (!conf.isEnable()) {
            log.info("ignoreLogin[Oidc] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.clientMap.containsKey(ownerUid)) {
                log.info("ignoreLogin[Oidc] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            this.clientMap.put(ownerUid, new OidcClient(log, ownerUid, conf));

            // finish
            log.info("startLogin[Oidc] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        synchronized (this) {
            IOUtils.closeQuietly(this.clientMap.remove(ownerUid));
        }

        log.info("stopLogin[Oidc] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.clientMap.containsKey(ownerUid));
        dto.setNameKey(OidcI18nKey.OIDC_LOGIN_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    protected OidcApi loginApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("oidcLoginService is was closed");
        }
    }

    //

    @Override
    public String loginExtractAccount(String fullLoginName) {
        return this.extractSplit(fullLoginName)[0];
    }

    @Override
    public String loginExtractDomain(String fullLoginName) {
        return this.extractSplit(fullLoginName)[1];
    }

    private String[] extractSplit(String fullLoginName) {
        int splitIdx = fullLoginName.lastIndexOf("@");
        if (splitIdx == -1) {
            throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_LOGIN_FAIL_PRIMARY_MISSING_ARGS);
        }

        String userAccount = fullLoginName.substring(0, splitIdx);
        String domain = fullLoginName.substring(splitIdx + 1);
        return new String[] { userAccount, domain };
    }

    @Override
    public String loginJumpUrl(String primaryUID, String status, String jumpUrl) throws Exception {
        return this.loginApi(primaryUID).getJumpUrl(status, jumpUrl);
    }

    @Override
    public String logoutJumpUrl(String primaryUID, String status, String jumpUrl, String idToken) throws Exception {
        return this.loginApi(primaryUID).getLogoutJumpUrl(status, jumpUrl, idToken);
    }

    @Override
    public LoginResponse authLogin(String primaryUID, LoginRequest request) {
        OidcApi oidcApi = this.loginApi(primaryUID);
        String idToken;
        if (StringUtils.isBlank(request.getAccessToken())) {
            idToken = oidcApi.fetchIdToken(request.getAuthCode(), request.getJumpUrl());
        } else {
            idToken = request.getAccessToken();
        }

        //https://openid.net/specs/openid-connect-core-1_0.html#RFC6819
        //
        // map user
        UserData oidcUser = oidcApi.fetchUserInfo(idToken);
        oidcUser.setAccount(oidcUser.getBindAccount());
        oidcUser.setAccessToken(idToken);

        // mapping role
        String roleName = StringUtils.isEmpty(oidcApi.getConf().getRoleMap()) ? SecSysRole.DEV_ROLE_NAME : oidcApi.getConf().getRoleMap();
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        RoleData role = CollectionUtils.isEmpty(roles) ? null : roles.get(0);
        if (role == null) {
            log.info("OIDC: user(" + oidcUser.getAccount() + ") not found any role, memberOf=" + roleName);
            throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_ROLE_MAPPING_FAILED);
        }
        oidcUser.setRoleId(role.getRoleId());

        return new LoginResponse(oidcUser, true, null);
    }
}
