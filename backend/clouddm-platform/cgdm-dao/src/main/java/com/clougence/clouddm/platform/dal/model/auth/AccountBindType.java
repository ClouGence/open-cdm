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
package com.clougence.clouddm.platform.dal.model.auth;

import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.platform.dal.handler.EnumOfCode;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author mode
 * @date 2020-02-29 14:43
 */
@Getter
public enum AccountBindType implements EnumOfCode<AccountBindType> {

    INTERNAL(null),
    LDAP(LoginProvider.LDAP),
    AD(LoginProvider.AD),
    DingTalk(LoginProvider.DingTalk),
    Feishu(LoginProvider.Feishu),
    Wechat(LoginProvider.Wechat),
    OIDC(LoginProvider.OIDC);

    private final LoginProvider provider;

    AccountBindType(LoginProvider provider){
        this.provider = provider;
    }

    public static AccountBindType valueOfProvider(LoginProvider provider) {
        if (provider == null) {
            return null;
        }
        for (AccountBindType t : AccountBindType.values()) {
            if (t.provider == provider) {
                return t;
            }
        }
        return null;
    }

    @Override
    public AccountBindType valueOfCode(String codeString) {
        for (AccountBindType t : AccountBindType.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), codeString)) {
                return t;
            }
        }
        throw new EnumConstantNotPresentException(AccountBindType.class, codeString);
    }
}
