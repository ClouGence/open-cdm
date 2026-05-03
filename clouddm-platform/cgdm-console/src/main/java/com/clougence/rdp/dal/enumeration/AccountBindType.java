package com.clougence.rdp.dal.enumeration;

import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.rdp.dal.handler.EnumOfCode;
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
    OIDC(LoginProvider.OIDC),
    MANAGED(LoginProvider.MANAGED);

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
        throw new EnumConstantNotPresentException(RdpApprovalType.class, codeString);
    }
}
