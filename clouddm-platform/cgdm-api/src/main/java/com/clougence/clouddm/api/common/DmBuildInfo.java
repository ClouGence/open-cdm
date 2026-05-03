package com.clougence.clouddm.api.common;

import com.clougence.utils.SystemUtils;

/** @author mode create time is 2021/1/5 **/

public final class DmBuildInfo {

    public static final String BUILD_ID;
    public static final String BUILD_VERSION;
    public static final String PRODUCT_HOME;//todo

    static {
        BUILD_ID = SystemUtils.getSystemProperty("app.buildId", "Unknown");
        BUILD_VERSION = SystemUtils.getSystemProperty("app.buildVersion", "Unknown");
        PRODUCT_HOME = "https://www.clougence.com/clouddm-personal";
    }
}
