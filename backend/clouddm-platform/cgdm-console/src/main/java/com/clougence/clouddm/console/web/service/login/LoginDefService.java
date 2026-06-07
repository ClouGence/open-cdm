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

import java.util.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.LoginDefVO;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginDefService {

    @Resource
    private AuthDal   authDal;
    @Resource
    private SystemDal systemDal;

    public List<LoginDefVO> listLoginDef() {
        String ownerUid = rootUid();
        List<LoginAuthType> configuredTypes = listConfLoginTypes(ownerUid);
        List<LoginAuthType> visibleTypes = new ArrayList<>();
        visibleTypes.add(LoginAuthType.PASSWORD);
        configuredTypes.stream()
            .filter(loginType -> loginType != LoginAuthType.PASSWORD)
            .sorted(Comparator.comparingInt(this::loginOrder).thenComparing(Enum::name))
            .forEach(loginType -> {
                if (!visibleTypes.contains(loginType)) {
                    visibleTypes.add(loginType);
                }
            });

        return visibleTypes.stream().map(loginType -> buildLoginDef(ownerUid, loginType)).toList();
    }

    public LoginAuthType resolveLoginDefault() {
        String ownerUid = rootUid();
        List<LoginDefVO> defs = listLoginDef();
        List<LoginAuthType> configuredTypes = listConfLoginTypes(ownerUid);
        for (LoginAuthType configuredType : configuredTypes) {
            LoginDefVO def = defs.stream().filter(item -> item.getLoginType() == configuredType).findFirst().orElse(null);
            if (def != null && def.isAvailable()) {
                return configuredType;
            }
        }
        return LoginAuthType.PASSWORD;
    }

    public boolean checkLoginEnable(String ownerUid, LoginProvider type) {
        return StringUtils.isBlank(getLoginUnavailableReason(ownerUid, type));
    }

    private LoginDefVO buildLoginDef(String ownerUid, LoginAuthType loginType) {
        LoginDefVO def = new LoginDefVO();
        def.setLoginType(loginType);
        def.setTabTitle(DmI18nUtils.getMessage(loginType.getTabTitleKey()));
        def.setIconTitle(DmI18nUtils.getMessage(iconTitleKey(loginType)));
        def.setIcon(iconResource(loginType));

        LoginProvider provider = loginType.getBindType().getProvider();
        def.setJump(provider != null && provider.isJumpIn());
        if (provider == null) {
            def.setAvailable(true);
            return def;
        }

        if (PluginManager.findSpi(LoginProviderSpi.class, provider.name()) == null) {
            def.setAvailable(false);
            def.setErrorInfo(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
            return def;
        }

        String unavailableReason = getLoginUnavailableReason(ownerUid, provider);
        def.setAvailable(StringUtils.isBlank(unavailableReason));
        def.setErrorInfo(unavailableReason);
        return def;
    }

    private String rootUid() {
        var rootUser = this.authDal.queryRootUser();
        return rootUser == null ? AuthDal.ROOT_USER_UID : rootUser.getUid();
    }

    private String iconResource(LoginAuthType loginType) {
        return "webside/" + loginType.name() + "@login-icon";
    }

    private String getLoginUnavailableReason(String ownerUid, LoginProvider type) {
        String configValue = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.accountAuthType);
        if (StringUtils.isBlank(configValue)) {
            return "login provider is not configured.";
        }

        if (parseProviders(configValue).contains(type)) {
            return null;
        }
        return "login provider is not configured.";
    }

    public List<LoginAuthType> listConfLoginTypes(String ownerUid) {
        String configValue = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.accountAuthType);
        List<LoginAuthType> loginTypes = new ArrayList<>();
        if (StringUtils.isBlank(configValue)) {
            loginTypes.add(LoginAuthType.PASSWORD);
            return loginTypes;
        }

        for (LoginAuthType loginType : parseLoginTypes(configValue)) {
            if (loginType == null) {
                continue;
            }
            if (!loginTypes.contains(loginType)) {
                loginTypes.add(loginType);
            }
        }

        if (loginTypes.isEmpty()) {
            loginTypes.add(LoginAuthType.PASSWORD);
        }
        return loginTypes;
    }

    public static List<LoginAuthType> parseLoginTypes(String configValue) {
        List<LoginAuthType> loginTypes = new ArrayList<>();
        for (String configured : splitLoginTypes(configValue)) {
            if (StringUtils.isBlank(configured)) {
                continue;
            }
            LoginAuthType loginType = LoginAuthType.valueOfCode(configured);
            if (loginType == null) {
                LoginProvider provider = LoginProvider.valueOfCode(configured);
                loginType = LoginAuthType.valueOfProvider(provider);
            }
            if (loginType == null) {
                log.warn("[LoginDefService] unknown login type config: {}", configured);
                continue;
            }
            loginTypes.add(loginType);
        }
        return loginTypes;
    }

    public static Set<LoginProvider> parseProviders(String configValue) {
        Set<LoginProvider> providers = new LinkedHashSet<>();
        for (LoginAuthType loginType : parseLoginTypes(configValue)) {
            if (loginType.getBindType().getProvider() != null) {
                providers.add(loginType.getBindType().getProvider());
            }
        }
        return providers;
    }

    private static String[] splitLoginTypes(String configValue) {
        return StringUtils.defaultString(configValue).split("[,，;；]");
    }

    private int loginOrder(LoginAuthType loginType) {
        if (loginType == LoginAuthType.PASSWORD) {
            return -1;
        }

        LoginProvider provider = loginType.getBindType().getProvider();
        if (provider == null) {
            return LoginProviderSpi.DEFAULT_ORDER;
        }

        LoginProviderSpi spi = PluginManager.findSpi(LoginProviderSpi.class, provider.name());
        return spi == null ? LoginProviderSpi.DEFAULT_ORDER : spi.order();
    }

    private String iconTitleKey(LoginAuthType authType) {
        if (authType == LoginAuthType.PASSWORD) {
            return I18nRdpLabelKeys.LOGIN_ACCOUNT.name();
        } else {
            return authType.getI18nKey();
        }
    }
}
