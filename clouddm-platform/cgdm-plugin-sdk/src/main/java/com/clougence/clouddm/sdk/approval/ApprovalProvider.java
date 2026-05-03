package com.clougence.clouddm.sdk.approval;

import com.clougence.utils.StringUtils;

public enum ApprovalProvider {

    Internal,
    DingTalk,
    Wechat,
    Feishu,
    Custom,;

    public static ApprovalProvider valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ApprovalProvider t : ApprovalProvider.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }

}
