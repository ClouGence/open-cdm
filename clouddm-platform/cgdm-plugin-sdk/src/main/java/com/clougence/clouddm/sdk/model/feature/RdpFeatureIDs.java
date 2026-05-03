package com.clougence.clouddm.sdk.model.feature;

/**
 * @author mode 2020/11/6 18:52
 */
public interface RdpFeatureIDs {

    // PRODUCT
    @Deprecated
    String PRODUCT_CLOUD_RDP             = "PRODUCT_CLOUD_RDP";
    @Deprecated
    String PRODUCT_CLOUD_CANAL           = "PRODUCT_CLOUD_CANAL";
    @Deprecated
    String PRODUCT_CLOUD_DM              = "PRODUCT_CLOUD_DM";

    String ENABLE_REGISTER               = "ENABLE_REGISTER";
    String ENABLE_SMS_LOGIN              = "ENABLE_PHONE_LOGIN";
    String ENABLE_EMAIL_LOGIN            = "ENABLE_EMAIL_LOGIN";
    String ENABLE_SSO_LOGIN              = "ENABLE_SSO_LOGIN";

    String ENABLE_VALIDATE_DS_EXTRA_CONF = "ENABLE_VALIDATE_DS_EXTRA_CONF";
}
