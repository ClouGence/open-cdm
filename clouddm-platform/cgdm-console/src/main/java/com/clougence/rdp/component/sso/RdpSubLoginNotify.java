package com.clougence.rdp.component.sso;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.enumeration.LoginAuthType;
import com.clougence.rdp.dal.mapper.RdpUserKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpSubLoginNotify implements RdpNotifyService {

    @Resource
    private RdpSubLoginService        loginService;
    @Resource
    private RdpUserKvBaseConfigMapper userConfigMapper;

    @SneakyThrows
    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        // switchProvider
        UserConfigMO authTypeConf = configList.stream().filter(c -> {
            return StringUtils.equalsIgnoreCase(c.getConfig(), UserDefinedConfig.Fields.subAccountAuthType);
        }).findFirst().orElse(null);
        if (authTypeConf != null) {
            this.switchProvider(ownerUid, authTypeConf);
            return;
        }

        // modifyProvider
        RdpUserKvBaseConfigDO currentConfig = this.userConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.subAccountAuthType);
        if (currentConfig != null) {
            LoginAuthType authType = LoginAuthType.valueOfProvider(LoginProvider.valueOfCode(currentConfig.getConfigValue()));
            if (authType == null || authType.getBindType().getProvider() == null) {
                // no Provider need to restart
                return;
            }

            List<UserConfigTagType> tags = configList.stream().map(UserConfigMO::getTagType).distinct().collect(Collectors.toList());
            if (!tags.contains(authType.getConfigGroup())) {
                return;
            }

            LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, authType.getBindType().getProvider().name());
            if (service != null) {
                service.stop(ownerUid, new LifeSpiRequest());
                service.start(ownerUid, new LifeSpiRequest());
            }
        }
    }

    private void switchProvider(String ownerUid, UserConfigMO authTypeConfig) throws Exception {
        LoginProvider oldProvider = LoginProvider.valueOfCode(authTypeConfig.getOldValue());
        LoginProvider newProvider = LoginProvider.valueOfCode(authTypeConfig.getNewValue());

        if (oldProvider != null) {
            LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, oldProvider.name());
            if (service != null) {
                service.stop(ownerUid, new LifeSpiRequest());
            }
        }

        if (newProvider != null) {
            LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, newProvider.name());
            if (service != null && this.loginService.checkLoginEnable(ownerUid, newProvider)) {
                service.start(ownerUid, new LifeSpiRequest());
            }
        }
    }
}
