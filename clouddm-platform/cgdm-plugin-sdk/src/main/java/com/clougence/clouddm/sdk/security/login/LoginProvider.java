package com.clougence.clouddm.sdk.security.login;

import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author mode
 * @date 2020-02-29 14:43
 */
@Getter
@Deprecated
public enum LoginProvider {

    LDAP(false, false),
    AD(false, false),
    DingTalk(true, false),
    Feishu(true, false),
    Wechat(true, false),
    OIDC(true, true),
    MANAGED(false, false);

    private final boolean jumpIn;
    private final boolean jumpOut;

    LoginProvider(boolean jumpIn, boolean jumpOut){
        this.jumpIn = jumpIn;
        this.jumpOut = jumpOut;
    }

    public static LoginProvider valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (LoginProvider t : LoginProvider.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
