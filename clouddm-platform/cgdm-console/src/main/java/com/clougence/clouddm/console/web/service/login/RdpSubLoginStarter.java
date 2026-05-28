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

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpSubLoginStarter implements UnifiedPostConstruct {
    @Resource
    private AuthDal            authDal;
    @Resource
    private RdpSubLoginService loginService;

    @Override
    public void init() throws Exception {
        ThreadUtils.runDaemonThread(this::initLogin);
    }

    @Override
    public void stop() {

    }

    private void initLogin() {
        List<String> serviceNames;
        try {
            serviceNames = PluginManager.getSpiNamesByType(LoginProviderSpi.class);
            if (CollectionUtils.isNotEmpty(serviceNames)) {
                log.info("[RdpSubLoginStarter] SsoService found " + serviceNames.size() + " provider is " + StringUtils.join(serviceNames.toArray(), ","));
            } else {
                log.error("[RdpSubLoginStarter] SsoService not found any provider.");
                return;
            }
        } catch (Exception e) {
            log.error("[RdpSubLoginStarter] SsoService found provider error " + e.getMessage(), e);
            return;
        }

        // start plugin for user.
        List<DmAuthUserDO> users = this.authDal.userMapper().listPrimaryAccount();
        for (DmAuthUserDO rdpUserDO : users) {
            for (String serviceName : serviceNames) {
                LoginProvider providerType = LoginProvider.valueOfCode(serviceName);

                startServiceForUser(providerType, rdpUserDO.getUid());
            }
        }
    }

    protected void startServiceForUser(LoginProvider providerType, String primaryUid) {
        if (!this.loginService.checkLoginEnable(primaryUid, providerType)) {
            return;
        }

        LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, providerType.name());
        if (service == null) {
            return;
        }

        try {
            service.start(primaryUid, new LifeSpiRequest());
        } catch (Exception e) {
            log.error("[RdpSubLoginStarter] SsoService switch " + providerType.name() + "client was failed", e);
        }
    }
}
