package com.clougence.clouddm.team.provider.wechat.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.sdk.security.login.*;
import com.clougence.clouddm.team.provider.wechat.client.WechatApi;
import com.clougence.clouddm.team.provider.wechat.client.WechatClient;
import com.clougence.clouddm.team.provider.wechat.constants.WechatConfigKey;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
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
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class WechatLoginProviderSpi implements LoginProviderSpi {

    private final ConsoleConfigService      configService;
    private final Map<String, WechatClient> clientMap = new ConcurrentHashMap<>();

    public WechatLoginProviderSpi(ConsoleConfigService configService){
        this.configService = configService;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                WechatConfigKey.LoginEnable.getConfigKey(),//
                WechatConfigKey.LoginCorpId.getConfigKey(),//
                WechatConfigKey.LoginAgentId.getConfigKey(),//
                WechatConfigKey.LoginSecret.getConfigKey(),//
                WechatConfigKey.LoginRoleMap.getConfigKey()//
        ));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        // enable is false.
        String enableCfg = configMap.get(WechatConfigKey.LoginEnable.getConfigKey());
        if (!StringUtils.equalsIgnoreCase(enableCfg, LoginProvider.Wechat.name())) {
            log.info("ignoreLogin[Wechat] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.clientMap.containsKey(ownerUid)) {
                log.info("ignoreLogin[Wechat] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            String corpId = configMap.get(WechatConfigKey.LoginCorpId.getConfigKey());
            String agentId = configMap.get(WechatConfigKey.LoginAgentId.getConfigKey());
            String secret = configMap.get(WechatConfigKey.LoginSecret.getConfigKey());
            String roleMapping = configMap.get(WechatConfigKey.LoginRoleMap.getConfigKey());

            // start api client
            WechatClient dingClient = new WechatClient(log, corpId, agentId, secret, roleMapping);
            this.clientMap.put(ownerUid, dingClient);

            // finish
            log.info("startLogin[Wechat] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        // remove api client
        IOUtils.closeQuietly(this.clientMap.remove(ownerUid));

        log.info("stopLogin[Wechat] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.clientMap.containsKey(ownerUid));
        dto.setNameKey(WechatI18nKey2.WECHAT_LOGIN_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    protected WechatApi loginApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("wechatLoginService is was closed");
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
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_LOGIN_FAIL_PRIMARY_MISSING_ARGS);
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
        WechatApi wechatApi = this.loginApi(primaryUID);
        String userTicket;
        if (StringUtils.isBlank(request.getAccessToken())) {
            userTicket = wechatApi.getUserTicket(request.getAuthCode());
        } else {
            userTicket = request.getAccessToken();
        }

        // map user
        UserData wechatUser = loginApi(primaryUID).getUserInfo(userTicket);
        UserData primaryUser = this.configService.findUserByUID(primaryUID);
        wechatUser.setSubAccount(wechatUser.getBindAccount() + "@" + primaryUser.getUserDomain());
        wechatUser.setUserDomain(primaryUser.getUserDomain());
        wechatUser.setAccessToken(userTicket);

        // mapping role
        String roleName = wechatApi.getClient().getRoleMapping();
        roleName = StringUtils.isEmpty(roleName) ? SecSysRole.DEV_ROLE_NAME : roleName;
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        RoleData role = CollectionUtils.isEmpty(roles) ? null : roles.get(0);
        if (role == null) {
            log.info("Wechat: user(" + wechatUser.getSubAccount() + ") not found any role, memberOf=" + roleName);
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_ROLE_MAPPING_FAILED);
        }
        wechatUser.setRoleId(role.getRoleId());

        return new LoginResponse(wechatUser, true, null);
    }
}
