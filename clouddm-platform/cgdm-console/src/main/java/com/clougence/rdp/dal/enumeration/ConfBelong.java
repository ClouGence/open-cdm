package com.clougence.rdp.dal.enumeration;

import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;

/**
 * @author bucketli 2023/11/23 15:01:50
 */
public enum ConfBelong {

    Common,
    CloudCanal,
    CloudDM;

    public String getCloudAliasName() {
        if (this == CloudCanal) {
            return GlobalDeploySite.ccProductName();
        }

        return this.name();
    }
}
