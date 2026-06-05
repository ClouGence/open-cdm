/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
