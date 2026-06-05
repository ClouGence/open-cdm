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
package com.clougence.clouddm.sdk.security.auth.def;

import com.clougence.clouddm.sdk.model.feature.AuthOwnerProduct;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.AuthCategory;

/**
 * @author bucketli 2021/1/6 19:00
 */
@AuthOwnerProduct(RdpFeatureIDs.PRODUCT_CLOUD_DM)
public interface SecAuthCategory {

    // ----------------------------------------------------
    // Workflow
    // ----------------------------------------------------
    @AuthCategory(order = 3, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_WORKER_ORDER, hidden = true)
    String CAT_RDP_WORKER_ORDER         = "CAT_RDP_WORKER_ORDER";

    // ----------------------------------------------------
    // Data Source
    // ----------------------------------------------------

    @AuthCategory(order = 10, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_DS)
    String CAT_RDP_DS                   = "CAT_RDP_DS";

    // ----------------------------------------------------
    // General
    // ----------------------------------------------------

    @AuthCategory(order = 5, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP)
    String CAT_RDP                      = "CAT_RDP";

    @AuthCategory(order = 3, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_ENV)
    String CAT_RDP_ENV                  = "CAT_RDP_ENV";

    // ----------------------------------------------------
    // Security
    // ----------------------------------------------------

    @AuthCategory(order = 0, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_USER)
    String CAT_RDP_USER                 = "CAT_RDP_USER";

    @AuthCategory(order = 1, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_ROLE)
    String CAT_RDP_ROLE                 = "CAT_RDP_ROLE";

    @AuthCategory(order = 4, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_OP_AUDIT)
    String CAT_RDP_OP_AUDIT             = "CAT_RDP_OP_AUDIT";

    @AuthCategory(order = 5, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_PREFERENCE_CONF)
    String CAT_RDP_PRI_PREFERENCE_CONF  = "CAT_RDP_PRI_PREFERENCE_CONF";

    @AuthCategory(order = 6, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_RDP_THIRD_PARTY_CONF)
    String CAT_RDP_PRI_THIRD_PARTY_CONF = "CAT_RDP_PRI_THIRD_PARTY_CONF";

    // ----------------------------------------------------
    // CloudDM
    // ----------------------------------------------------
    @AuthCategory(order = 0, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_FOR_DAUTH_S)
    String CAT_DM_FOR_DAUTH_STATEMENTS  = "CAT_DM_FOR_DAUTH_STATEMENTS";

    @AuthCategory(order = 1, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_FOR_DAUTH_F)
    String CAT_DM_FOR_DAUTH_FUNCTION    = "CAT_DM_FOR_DAUTH_FUNCTION";

    @AuthCategory(order = 0, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_CONSOLE)
    String CAT_DM_CONSOLE               = "CAT_DM_CONSOLE";

    @AuthCategory(order = 1, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_PROJECT)
    String CAT_DM_PROJECT               = "CAT_DM_PROJECT";

    @AuthCategory(order = 2, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_SYS)
    String CAT_DM_SYS                   = "CAT_DM_SYS";

    //@AuthCategory(order = 1, parent = CAT_DM_SYS, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_DATA)
    //String CAT_DM_DATA      = "CAT_DM_DATA";

    @AuthCategory(order = 2, parent = CAT_DM_SYS, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_DS)
    String CAT_DM_DS                    = "CAT_DM_DS";

    @AuthCategory(order = 3, parent = CAT_DM_SYS, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_WORKER)
    String CAT_DM_WORKER                = "CAT_DM_WORKER";

    @AuthCategory(order = 4, parent = CAT_DM_SYS, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_SECRULES)
    String CAT_DM_SECRULES              = "CAT_DM_SECRULES";

    @AuthCategory(order = 5, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_IM)
    String CAT_DM_IM                    = "CAT_DM_IM";

    @AuthCategory(order = 6, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_KEY_DM_CICD)
    String CAT_DM_CICD                  = "CAT_DM_CICD";

    @AuthCategory(order = 7, parent = CAT_RDP, i18nKey = SecAuthI18nKeys.CAT_DM_SQL_AUDIT)
    String CAT_DM_SQL_AUDIT             = "CAT_DM_SQL_AUDIT";
}
