package com.clougence.rdp.dal.enumeration;

import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;

import lombok.Getter;

/**
 * @author bucketli 2023/11/23 15:01:50
 */
public enum RdpProduct {

    CloudCanal(RdpFeatureIDs.PRODUCT_CLOUD_CANAL),
    CloudDM(RdpFeatureIDs.PRODUCT_CLOUD_DM);

    @Getter
    private final String featureID;

    RdpProduct(String featureID){
        this.featureID = featureID;
    }
}
