package com.clougence.clouddm.base.metadata.rdp.enumeration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/5/18 17:44:27
 */
@Slf4j
public enum GlobalDeploySite {

    china,
    north_america,
    asia_pacific,
    europe;

    public static final GlobalDeploySite currDeploySite;

    static {
        currDeploySite = GlobalDeploySite.china;

        log.info("Global deploy site: " + currDeploySite);
    }

    public static String rdpProductName() {
        if (currDeploySite == GlobalDeploySite.china) {
            //            return "ClouGence RDP";
            return "CloudCanal & CloudDM";
        } else {
            return "BladePipe";
        }
    }

    public static String ccProductName() {
        if (currDeploySite == GlobalDeploySite.china) {
            return "CloudCanal";
        } else {
            return "BladePipe";
        }
    }

    public static String ccUserName() {
        if (currDeploySite == GlobalDeploySite.china) {
            return "clougence";
        } else {
            return "bladepipe";
        }
    }

    public static boolean outChina() {
        return currDeploySite != GlobalDeploySite.china;
    }

    public static boolean saasEnable() {
        //        return currDeploySite != GlobalDeploySite.china;
        return true;
    }

    public static boolean initVoucherEnable() {
        //        return currDeploySite != GlobalDeploySite.china;
        return true;
    }
}
