package com.clougence.clouddm.sdk.scm;

import com.clougence.utils.StringUtils;

public enum ScmEventTarget {

    Repository,
    Issue,
    PullRequest;

    public static ScmEventTarget valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ScmEventTarget t : ScmEventTarget.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
