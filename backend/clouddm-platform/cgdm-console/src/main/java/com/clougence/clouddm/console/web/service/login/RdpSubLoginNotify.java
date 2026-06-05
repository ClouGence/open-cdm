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
package com.clougence.clouddm.console.web.service.login;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpSubLoginNotify implements RdpNotifyService {

    @Resource
    private SystemDal          systemDal;
    @Resource
    private RdpSubLoginService loginService;

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
        DmSysUserConfDO currentConfig = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.subAccountAuthType);
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
