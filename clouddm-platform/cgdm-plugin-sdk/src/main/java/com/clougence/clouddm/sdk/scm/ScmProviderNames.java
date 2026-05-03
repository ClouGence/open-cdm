package com.clougence.clouddm.sdk.scm;

import com.clougence.utils.StringUtils;

public enum ScmProviderNames {

    Gitee,
    Github,;

    public static ScmProviderNames valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ScmProviderNames t : ScmProviderNames.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
