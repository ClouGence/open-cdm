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

import com.clougence.utils.i18n.I18nResource;

/**
 * @author bucketli 2021/1/6 19:00
 */
@I18nResource({ "/META-INF/rdp/sdk/i18n/dm-auth-label", "/META-INF/rdp/sdk/i18n/rdp-auth-label" })
public interface SecAuthI18nKeys {

    // for Auth Category
    String CAT_KEY_DM_CONSOLE                       = "CAT_KEY_DM_CONSOLE";
    String CAT_KEY_DM_SYS                           = "CAT_KEY_DM_SYS";
    String CAT_KEY_DM_FOR_DAUTH_S                   = "CAT_KEY_DM_FOR_DAUTH_S";
    String CAT_KEY_DM_FOR_DAUTH_F                   = "CAT_KEY_DM_FOR_DAUTH_F";
    //String CAT_KEY_DM_DATA             = "CAT_KEY_DM_DATA";
    String CAT_KEY_DM_DS                            = "CAT_KEY_DM_DS";
    String CAT_KEY_DM_WORKER                        = "CAT_KEY_DM_WORKER";
    String CAT_KEY_DM_SECRULES                      = "CAT_KEY_DM_SECRULES";
    String CAT_KEY_DM_PROJECT                       = "CAT_KEY_DM_PROJECT";
    String CAT_KEY_DM_IM                            = "CAT_KEY_DM_IM";
    String CAT_KEY_DM_CICD                          = "CAT_KEY_DM_CICD";
    String CAT_DM_SQL_AUDIT                         = "CAT_DM_SQL_AUDIT";
    String CAT_KEY_RDP                              = "CAT_KEY_RDP";
    String CAT_KEY_RDP_SYS                          = "CAT_KEY_RDP_SYS";
    String CAT_KEY_RDP_ENV                          = "CAT_KEY_RDP_ENV";
    //    String CAT_KEY_RDP_PRODUCT_CLUSTER              = "CAT_KEY_RDP_PRODUCT_CLUSTER";
    String CAT_KEY_RDP_ALERT                        = "CAT_KEY_RDP_ALERT";
    String CAT_KEY_RDP_USER                         = "CAT_KEY_RDP_USER";
    String CAT_KEY_RDP_ROLE                         = "CAT_KEY_RDP_ROLE";
    String CAT_KEY_RDP_AUTH                         = "CAT_KEY_RDP_AUTH";
    String CAT_KEY_RDP_OP_AUDIT                     = "CAT_KEY_RDP_OP_AUDIT";
    String CAT_KEY_RDP_DS                           = "CAT_KEY_RDP_DS";
    String CAT_KEY_RDP_WORKER_ORDER                 = "CAT_KEY_RDP_WORKER_ORDER";
    String CAT_KEY_RDP_PREFERENCE_CONF              = "CAT_KEY_RDP_PREFERENCE_CONF";
    String CAT_KEY_RDP_THIRD_PARTY_CONF             = "CAT_KEY_RDP_THIRD_PARTY_CONF";

    // for Role Auth Label
    String AUTH_KEY_DM_QUERY_CONSOLE                = "AUTH_KEY_DM_QUERY_CONSOLE";
    String AUTH_KEY_DM_QUERY_EXPORT                 = "AUTH_KEY_DM_QUERY_EXPORT";
    String AUTH_KEY_DM_OBJECT_MANAGER               = "AUTH_KEY_DM_OBJECT_MANAGER";
    String AUTH_KEY_DM_DS_MAINTENANCE               = "AUTH_KEY_DM_DS_MAINTENANCE";
    String AUTH_KEY_DM_DATA_READ                    = "AUTH_KEY_DM_DATA_READ";
    String AUTH_KEY_DM_DATA_MANAGE                  = "AUTH_KEY_DM_DATA_MANAGE";
    String AUTH_KEY_DM_DS_READ                      = "AUTH_KEY_DM_DS_READ";
    String AUTH_KEY_DM_DS_MANAGE                    = "AUTH_KEY_DM_DS_MANAGE";
    String AUTH_KEY_DM_WORKER_READ                  = "AUTH_KEY_DM_WORKER_READ";
    String AUTH_KEY_DM_WORKER_MANAGE                = "AUTH_KEY_DM_WORKER_MANAGE";
    String AUTH_KEY_DM_SECRULES_READ                = "AUTH_KEY_DM_SECRULES_READ";
    String AUTH_KEY_DM_SECRULES_MANAGE              = "AUTH_KEY_DM_SECRULES_MANAGE";
    String AUTH_KEY_DM_PROJECT_READ                 = "AUTH_KEY_DM_PROJECT_READ";
    String AUTH_KEY_DM_CHANGE_OPERATE               = "AUTH_KEY_DM_CHANGE_OPERATE";
    String AUTH_KEY_DM_PROJECT_MANAGE               = "AUTH_KEY_DM_PROJECT_MANAGE";
    String AUTH_KEY_DM_IM_READ                      = "AUTH_KEY_DM_IM_READ";
    String AUTH_KEY_DM_IM_MANAGE                    = "AUTH_KEY_DM_IM_MANAGE";
    String AUTH_KEY_DM_CICD_READ                    = "AUTH_KEY_DM_CICD_READ";
    String AUTH_KEY_DM_CICD_MANAGE                  = "AUTH_KEY_DM_CICD_MANAGE";
    String AUTH_KEY_DM_SQL_AUDIT_READ               = "AUTH_KEY_DM_SQL_AUDIT_READ";
    String AUTH_KEY_RDP_SYS_READ                    = "AUTH_KEY_RDP_SYS_READ";
    String AUTH_KEY_RDP_SYS_MANAGE                  = "AUTH_KEY_RDP_SYS_MANAGE";
    String AUTH_KEY_RDP_DS_READ                     = "AUTH_KEY_RDP_DS_READ";
    String AUTH_KEY_RDP_DS_MANAGE                   = "AUTH_KEY_RDP_DS_MANAGE";
    String AUTH_KEY_RDP_ENV_READ                    = "AUTH_KEY_RDP_ENV_READ";
    String AUTH_KEY_RDP_ENV_MANAGE                  = "AUTH_KEY_RDP_ENV_MANAGE";
    String AUTH_KEY_RDP_USER_READ                   = "AUTH_KEY_RDP_USER_READ";
    String AUTH_KEY_RDP_USER_MANAGE                 = "AUTH_KEY_RDP_USER_MANAGE";
    String AUTH_KEY_RDP_ROLE_READ                   = "AUTH_KEY_RDP_ROLE_READ";
    String AUTH_KEY_RDP_ROLE_MANAGE                 = "AUTH_KEY_RDP_ROLE_MANAGE";
    String AUTH_KEY_RDP_AUTH_REQUEST                = "AUTH_KEY_RDP_AUTH_REQUEST";
    String AUTH_KEY_RDP_AUTH_READ                   = "AUTH_KEY_RDP_AUTH_READ";
    String AUTH_KEY_RDP_AUTH_MANAGE                 = "AUTH_KEY_RDP_AUTH_MANAGE";
    //    String AUTH_KEY_RDP_PRODUCT_CLUSTER_READ        = "AUTH_KEY_RDP_PRODUCT_CLUSTER_READ";
    //    String AUTH_KEY_RDP_PRODUCT_CLUSTER_MANAGE      = "AUTH_KEY_RDP_PRODUCT_CLUSTER_MANAGE";
    String AUTH_KEY_RDP_OP_AUDIT_READ               = "AUTH_KEY_RDP_OP_AUDIT_READ";
    String AUTH_KEY_RDP_OP_AUDIT_EXPORT             = "AUTH_KEY_RDP_OP_AUDIT_EXPORT";
    String AUTH_KEY_RDP_WORKER_ORDER_READ           = "AUTH_KEY_RDP_WORKER_ORDER_READ";
    String AUTH_KEY_RDP_WORKER_ORDER_REQUEST        = "AUTH_KEY_RDP_WORKER_ORDER_REQUEST";
    String AUTH_KEY_RDP_WORKER_ORDER_APPROVE        = "AUTH_KEY_RDP_WORKER_ORDER_APPROVE";
    String AUTH_KEY_RDP_WORKER_ORDER_EXECUTE        = "AUTH_KEY_RDP_WORKER_ORDER_EXECUTE";
    //String AUTH_KEY_RDP_AUTH_WORKER_ORDER_APPROVE   = "AUTH_KEY_RDP_AUTH_WORKER_ORDER_APPROVE";
    //String AUTH_KEY_RDP_WORKER_ORDER_MANAGE         = "AUTH_KEY_RDP_WORKER_ORDER_MANAGE";
    String AUTH_KEY_RDP_PRI_USER_AK_SK_R            = "AUTH_KEY_RDP_PRI_USER_AK_SK_R";
    String AUTH_KEY_RDP_PRI_USER_AK_SK_W            = "AUTH_KEY_RDP_PRI_USER_AK_SK_W";
    String AUTH_KEY_RDP_PRI_USER_KV_CONF_R          = "AUTH_KEY_RDP_PRI_USER_KV_CONF_R";
    String AUTH_KEY_RDP_PRI_USER_KV_CONF_W          = "AUTH_KEY_RDP_PRI_USER_KV_CONF_W";
    String AUTH_KEY_RDP_PRI_USER_NORMAL_CONF_R      = "AUTH_KEY_RDP_PRI_USER_NORMAL_CONF_R";
    String AUTH_KEY_RDP_PRI_USER_THIRD_PARTY_CONF_W = "AUTH_KEY_RDP_PRI_USER_THIRD_PARTY_CONF_W";

    // for Role and Data
    String RDP_AUTH_DATA_DS_READ                    = "RDP_AUTH_DATA_DS_READ";
    String RDP_AUTH_DATA_DS_MANAGE                  = "RDP_AUTH_DATA_DS_MANAGE";

    // for Data Auth Label
    String AUTH_DATA_DM_QUERY                       = "AUTH_DATA_DM_QUERY";
    String AUTH_DATA_DM_DML                         = "AUTH_DATA_DM_DML";
    String AUTH_DATA_DM_DDL                         = "AUTH_DATA_DM_DDL";
    String AUTH_DATA_DM_OBJ                         = "AUTH_DATA_DM_OBJ";
    String AUTH_DATA_DM_SPACE                       = "AUTH_DATA_DM_SPACE";
    String AUTH_DATA_DM_DCL                         = "AUTH_DATA_DM_DCL";
    String AUTH_DATA_DM_CALL                        = "AUTH_DATA_DM_CALL";
    String AUTH_DATA_DM_OTHER                       = "AUTH_DATA_DM_OTHER";
    String AUTH_DATA_DM_SENSITIVE                   = "AUTH_DATA_DM_SENSITIVE";
    String AUTH_DATA_DM_TICKET                      = "AUTH_DATA_DM_TICKET";

    // for Data Auth Label (Redis)
    String AUTH_DATA_DM_REDIS_READ                  = "AUTH_DATA_DM_REDIS_READ";
    String AUTH_DATA_DM_REDIS_WRITE                 = "AUTH_DATA_DM_REDIS_WRITE";
    String AUTH_DATA_DM_REDIS_ADMIN                 = "AUTH_DATA_DM_REDIS_ADMIN";
    String AUTH_DATA_DM_REDIS_OTHER                 = "AUTH_DATA_DM_REDIS_OTHER";

}
