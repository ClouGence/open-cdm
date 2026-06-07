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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginStarter implements UnifiedPostConstruct, RdpNotifyService {
    @Resource
    private AuthDal         authDal;
    @Resource
    private LoginDefService loginDefService;

    @Override
    public void init() throws Exception {
        ThreadUtils.runDaemonThread(this::initLogin);
    }

    @Override
    public void stop() {

    }

    private void initLogin() {
        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        if (rootUser == null) {
            log.error("[LoginStarter] root user not found.");
            return;
        }

        UserConfigMO authTypeConfig = new UserConfigMO();
        authTypeConfig.setConfig(UserDefinedConfig.Fields.accountAuthType);
        authTypeConfig.setNewValue(StringUtils.join(this.loginDefService.listConfLoginTypes(rootUser.getUid()).stream().map(Enum::name).toArray(), ","));
        notifyUserConfig(rootUser.getUid(), List.of(authTypeConfig));
    }

    @SneakyThrows
    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        if (!this.authDal.isRootUser(ownerUid)) {
            return;
        }
        if (configList == null || configList.isEmpty()) {
            return;
        }

        UserConfigMO authTypeConf = configList.stream()
            .filter(c -> StringUtils.equalsIgnoreCase(c.getConfig(), UserDefinedConfig.Fields.accountAuthType))
            .findFirst()
            .orElse(null);

        if (authTypeConf == null) {
            this.restartModifiedProviders(ownerUid, configList);
        } else {
            this.refreshConfiguredProviders(ownerUid, authTypeConf);
        }
    }

    private void restartModifiedProviders(String ownerUid, List<UserConfigMO> configList) throws Exception {
        List<LoginAuthType> availableTypes = this.loginDefService.listConfLoginTypes(ownerUid);
        if (availableTypes.isEmpty()) {
            return;
        }

        Set<UserConfigTagType> filtered = configList.stream()
            .filter(config -> config != null && config.getTagType() != null && (config.isInsert() || config.isUpdate() || config.isDelete()))
            .map(UserConfigMO::getTagType)
            .collect(Collectors.toSet());
        for (LoginAuthType loginType : availableTypes) {
            LoginProvider provider = loginType.getBindType().getProvider();
            if (provider == null || !filtered.contains(loginType.getConfigGroup())) {
                continue;
            }

            LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, provider.name());
            if (service != null) {
                restartProvider(ownerUid, provider);
            }
        }
    }

    private void refreshConfiguredProviders(String ownerUid, UserConfigMO authTypeConfig) throws Exception {
        Set<LoginProvider> oldProviders = LoginDefService.parseProviders(authTypeConfig.getOldValue());
        Set<LoginProvider> newProviders = LoginDefService.parseProviders(authTypeConfig.getNewValue());

        Set<LoginProvider> removedProviders = new HashSet<>(oldProviders);
        removedProviders.removeAll(newProviders);
        for (LoginProvider oldProvider : removedProviders) {
            stopProvider(ownerUid, oldProvider);
        }

        for (LoginProvider newProvider : newProviders) {
            if (oldProviders.contains(newProvider)) {
                restartProvider(ownerUid, newProvider);
            } else {
                startProviderIfEnabled(ownerUid, newProvider);
            }
        }
    }

    private void restartProvider(String ownerUid, LoginProvider provider) throws Exception {
        stopProvider(ownerUid, provider);
        startProviderIfEnabled(ownerUid, provider);
    }

    private void stopProvider(String ownerUid, LoginProvider provider) throws Exception {
        LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, provider.name());
        if (service != null) {
            service.stop(ownerUid, new LifeSpiRequest());
        }
    }

    private void startProviderIfEnabled(String ownerUid, LoginProvider provider) throws Exception {
        LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, provider.name());
        if (service != null && this.loginDefService.checkLoginEnable(ownerUid, provider)) {
            service.start(ownerUid, new LifeSpiRequest());
        }
    }
}
