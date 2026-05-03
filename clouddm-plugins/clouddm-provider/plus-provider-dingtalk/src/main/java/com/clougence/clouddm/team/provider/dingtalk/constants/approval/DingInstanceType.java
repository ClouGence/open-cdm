package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingInstanceType {

    START,
    FINISH,
    TERMINATE;

    public static DingInstanceType getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (DingInstanceType dingInstanceType : DingInstanceType.values()) {
            if (dingInstanceType.name().equals(result)) {
                return dingInstanceType;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }
}
