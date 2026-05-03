package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingInstanceResult {

    AGREE,
    REFUSE;

    public static DingInstanceResult getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (DingInstanceResult dingInstanceResult : DingInstanceResult.values()) {
            if (dingInstanceResult.name().equals(result)) {
                return dingInstanceResult;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }
}
