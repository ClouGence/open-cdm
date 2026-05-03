package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingActivityResult {

    AGREE,
    REFUSE,
    REDIRECT;

    public static DingActivityResult getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (DingActivityResult dingActivityResult : DingActivityResult.values()) {
            if (dingActivityResult.name().equals(result)) {
                return dingActivityResult;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }
}
