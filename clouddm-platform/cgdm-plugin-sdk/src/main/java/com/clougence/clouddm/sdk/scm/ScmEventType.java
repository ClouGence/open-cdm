package com.clougence.clouddm.sdk.scm;

import com.clougence.utils.StringUtils;

public enum ScmEventType {

    Push,
    Tag,
    PullRequest,
    Issue,
    Note;

    public static ScmEventType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ScmEventType t : ScmEventType.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
