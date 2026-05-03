package com.clougence.clouddm.base.metadata.rdp.enumeration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum GlobalDeployMode {

    CLOUD,

    ON_PREMISE,;

    public static final GlobalDeployMode currDeployMode;

    static {
        currDeployMode = GlobalDeployMode.ON_PREMISE;
        log.info("Global deploy mode: " + currDeployMode);
    }

    public static boolean inCloud() {
        return currDeployMode == GlobalDeployMode.CLOUD;
    }

    public static boolean inPrivate() {
        return currDeployMode == GlobalDeployMode.ON_PREMISE;
    }
}
