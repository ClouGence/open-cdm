package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

import com.clougence.utils.StringUtils;

public enum DingActivityType {

    START,
    FINISH,
    CANCEL;

    public static DingActivityType getByName(String result) {
        // can receive "" result if result is not generated
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.toUpperCase();

        for (DingActivityType dingActivityType : DingActivityType.values()) {
            if (dingActivityType.name().equals(result)) {
                return dingActivityType;
            }
        }
        throw new UnsupportedOperationException("Can't find enum obj for node result " + result);
    }

}
