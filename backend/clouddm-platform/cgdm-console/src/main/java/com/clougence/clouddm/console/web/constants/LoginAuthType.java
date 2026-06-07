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
package com.clougence.clouddm.console.web.constants;

import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author pudding
 * @date 2020-02-29 14:43
 */
@Getter
public enum LoginAuthType {

    PASSWORD(AccountBindType.INTERNAL, UserConfigTagType.SECURITY, I18nRdpLabelKeys.LOGIN_PASSWORD.name(), I18nRdpLabelKeys.LOGIN_TAB_PASSWORD.name()),
    LDAP(AccountBindType.LDAP, UserConfigTagType.LDAP_CONFIG, I18nRdpLabelKeys.LOGIN_LDAP.name(), I18nRdpLabelKeys.LOGIN_TAB_LDAP.name()),
    AD(AccountBindType.AD, UserConfigTagType.LDAP_CONFIG, I18nRdpLabelKeys.LOGIN_AD.name(), I18nRdpLabelKeys.LOGIN_TAB_AD.name()),
    DingTalk(AccountBindType.DingTalk, UserConfigTagType.DINGTALK, I18nRdpLabelKeys.LOGIN_DINGTALK.name(), I18nRdpLabelKeys.LOGIN_TAB_DINGTALK.name()),
    Feishu(AccountBindType.Feishu, UserConfigTagType.FEISHU, I18nRdpLabelKeys.LOGIN_FEISHU.name(), I18nRdpLabelKeys.LOGIN_TAB_FEISHU.name()),
    Wechat(AccountBindType.Wechat, UserConfigTagType.WECHAT, I18nRdpLabelKeys.LOGIN_WECHAT.name(), I18nRdpLabelKeys.LOGIN_TAB_WECHAT.name()),
    OIDC(AccountBindType.OIDC, UserConfigTagType.OIDC, I18nRdpLabelKeys.LOGIN_OIDC.name(), I18nRdpLabelKeys.LOGIN_TAB_OIDC.name()),;

    private final AccountBindType   bindType;
    private final UserConfigTagType configGroup;
    private final String            i18nKey;
    private final String            tabTitleKey;

    LoginAuthType(AccountBindType bindType, UserConfigTagType configGroup, String i18nKey, String tabTitleKey){
        this.bindType = bindType;
        this.configGroup = configGroup;
        this.i18nKey = i18nKey;
        this.tabTitleKey = tabTitleKey;
    }

    public static LoginAuthType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (LoginAuthType t : LoginAuthType.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }

    public static LoginAuthType valueOfProvider(LoginProvider code) {
        if (code == null) {
            return null;
        }
        for (LoginAuthType t : LoginAuthType.values()) {
            if (t.getBindType().getProvider() == code) {
                return t;
            }
        }
        return null;
    }
}
