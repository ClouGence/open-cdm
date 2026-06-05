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
package com.clougence.clouddm.team.provider.dingtalk.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.team.provider.dingtalk.client.DingApi;
import com.clougence.clouddm.team.provider.dingtalk.client.DingClient;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingConfigKey;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingI18nKeys;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
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
 * @author pudding
 * @version 2020-01-17 15:29
 */
@Slf4j
public class DingLoginProviderSpi implements LoginProviderSpi {

    private final ConsoleConfigService    configService;
    private final Map<String, DingClient> clientMap = new ConcurrentHashMap<>();

    public DingLoginProviderSpi(ConsoleConfigService configService){
        this.configService = configService;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                DingConfigKey.LoginEnable.getConfigKey(),//
                DingConfigKey.LoginClientId.getConfigKey(),//
                DingConfigKey.LoginClientSecret.getConfigKey(),//
                DingConfigKey.LoginRoleMap.getConfigKey()));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        // enable is false.
        String enableCfg = configMap.get(DingConfigKey.LoginEnable.getConfigKey());
        if (!StringUtils.equalsIgnoreCase(enableCfg, LoginProvider.DingTalk.name())) {
            log.info("ignoreLogin[Ding] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.clientMap.containsKey(ownerUid)) {
                log.info("ignoreLogin[Ding] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            String appKey = configMap.get(DingConfigKey.LoginClientId.getConfigKey());
            String appSecret = configMap.get(DingConfigKey.LoginClientSecret.getConfigKey());
            String roleMapping = configMap.get(DingConfigKey.LoginRoleMap.getConfigKey());

            // start api client
            DingClient dingClient = new DingClient(log, appKey, appSecret, roleMapping);
            this.clientMap.put(ownerUid, dingClient);

            // finish
            log.info("startLogin[Ding] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        // remove auth client
        IOUtils.closeQuietly(this.clientMap.remove(ownerUid));

        log.info("stopLogin[Ding] primaryUid: " + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.clientMap.containsKey(ownerUid));
        dto.setNameKey(DingI18nKeys.DINGTALK_LOGIN_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    public DingApi loginApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("dingLoginService is was closed");
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
            throw ThirdPartyApiException.as().with(DingI18nKeys.DINGTALK_LOGIN_FAIL_PRIMARY_MISSING_ARGS);
        }

        String userAccount = fullLoginName.substring(0, splitIdx);
        String userDomain = fullLoginName.substring(splitIdx + 1);
        return new String[] { userAccount, userDomain };
    }

    @Override
    public String loginJumpUrl(String primaryUID, String status, String jumpUrl) throws Exception {
        return loginApi(primaryUID).getLoginUrl(status, jumpUrl);
    }

    @Override
    public String logoutJumpUrl(String primaryUID, String status, String jumpUrl, String accessToken) {
        return "";
    }

    @Override
    public LoginResponse authLogin(String primaryUID, LoginRequest request) {
        DingApi dingApi = this.loginApi(primaryUID);
        String accessToken;
        if (StringUtils.isBlank(request.getAccessToken())) {
            accessToken = dingApi.getUserAccessToken(request.getAuthCode());
        } else {
            accessToken = request.getAccessToken();
        }

        // map user
        UserData dingUser = loginApi(primaryUID).getUserInfo(accessToken);
        UserData primaryUser = this.configService.findUserByUID(primaryUID);
        dingUser.setSubAccount(dingUser.getBindAccount() + "@" + primaryUser.getUserDomain());
        dingUser.setUserDomain(primaryUser.getUserDomain());
        dingUser.setAccessToken(accessToken);

        // mapping role
        String roleName = dingApi.getClient().getRoleMapping();
        roleName = StringUtils.isEmpty(roleName) ? SecSysRole.DEV_ROLE_NAME : roleName;
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        RoleData role = CollectionUtils.isEmpty(roles) ? null : roles.get(0);
        if (role == null) {
            log.info("Ding: user(" + dingUser.getSubAccount() + ") not found any role, memberOf=" + roleName);
            throw ThirdPartyApiException.as().with(DingI18nKeys.DINGTALK_ROLE_MAPPING_FAILED);
        }
        dingUser.setRoleId(role.getRoleId());

        return new LoginResponse(dingUser, true, null);
    }
}
