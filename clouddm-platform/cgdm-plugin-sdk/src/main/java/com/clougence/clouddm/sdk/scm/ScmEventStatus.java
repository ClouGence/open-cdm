package com.clougence.clouddm.sdk.scm;

import com.clougence.utils.StringUtils;

public enum ScmEventStatus {

    Create,
    Update,
    Delete,

    Closed,
    Merged;

    public static ScmEventStatus valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ScmEventStatus t : ScmEventStatus.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
