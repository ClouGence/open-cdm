package com.clougence.clouddm.sdk.messenger;

import com.clougence.utils.StringUtils;

public enum MsgProviderType {

    DingTalk,
    Wechat,
    Feishu,
    //Discord,
    //Slack,
    //Email;
    ;

    public static MsgProviderType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (MsgProviderType t : MsgProviderType.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
