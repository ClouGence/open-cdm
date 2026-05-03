package com.clougence.clouddm.sdk.security.auth.def;

import static com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory.*;
import static com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys.*;

import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.AuthLabel;
import com.clougence.clouddm.sdk.model.feature.AuthOwnerProduct;

/**
 * @author mode 2021/1/6 19:00
 */
@AuthOwnerProduct(RdpFeatureIDs.PRODUCT_CLOUD_DM)
public interface SecRoleAuthLabel {

    // ================================ CAT_RDP_SYS ===============================================

    //@AuthLabel(order = 0, category = CAT_RDP_SYS, i18nKey = AUTH_KEY_RDP_SYS_READ)
    //String RDP_SYS_READ                    = "RDP_SYS_READ";

    //@AuthLabel(order = 0, category = CAT_RDP_SYS, i18nKey = AUTH_KEY_RDP_SYS_MANAGE)
    //String RDP_SYS_MANAGE                  = "RDP_SYS_MANAGE";

    // ================================ CAT_RDP_PRODUCT ===========================================

    //@AuthLabel(order = 0, category = CAT_RDP_PRODUCT_CLUSTER, i18nKey = AUTH_KEY_RDP_PRODUCT_CLUSTER_READ)
    //String RDP_PRODUCT_CLUSTER_READ        = "RDP_PRODUCT_CLUSTER_READ";

    //@AuthLabel(order = 0, category = CAT_RDP_PRODUCT_CLUSTER, i18nKey = AUTH_KEY_RDP_PRODUCT_CLUSTER_MANAGE, include = { RDP_PRODUCT_CLUSTER_READ })
    //String RDP_PRODUCT_CLUSTER_MANAGE      = "RDP_PRODUCT_CLUSTER_MANAGE";

    // ================================ CAT_RDP_ENV ===============================================

    @AuthLabel(order = 0, category = CAT_RDP_ENV, i18nKey = AUTH_KEY_RDP_ENV_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_ENV_READ                    = "RDP_ENV_READ";

    @AuthLabel(order = 1, category = CAT_RDP_ENV, i18nKey = AUTH_KEY_RDP_ENV_MANAGE, tag = { SecSysRole.DBA_ROLE_NAME }, include = RDP_ENV_READ)
    String RDP_ENV_MANAGE                  = "RDP_ENV_MANAGE";

    // ================================ CAT_RDP_USER ==============================================

    @AuthLabel(order = 0, category = CAT_RDP_USER, i18nKey = AUTH_KEY_RDP_USER_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_USER_READ                   = "RDP_USER_READ";

    @AuthLabel(order = 1, category = CAT_RDP_USER, i18nKey = AUTH_KEY_RDP_USER_MANAGE, include = RDP_USER_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_USER_MANAGE                 = "RDP_USER_MANAGE";

    @AuthLabel(order = 2, category = CAT_RDP_USER, i18nKey = AUTH_KEY_RDP_AUTH_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_AUTH_READ                   = "RDP_AUTH_READ";

    @AuthLabel(order = 3, category = CAT_RDP_USER, i18nKey = AUTH_KEY_RDP_AUTH_MANAGE, tag = { SecSysRole.DBA_ROLE_NAME }, include = RDP_AUTH_READ)
    String RDP_AUTH_MANAGE                 = "RDP_AUTH_MANAGE";

    // ================================ CAT_RDP_ROLE ==============================================

    @AuthLabel(order = 0, category = CAT_RDP_ROLE, i18nKey = AUTH_KEY_RDP_ROLE_READ)
    String RDP_ROLE_READ                   = "RDP_ROLE_READ";

    @AuthLabel(order = 2, category = CAT_RDP_ROLE, i18nKey = AUTH_KEY_RDP_ROLE_MANAGE, include = RDP_ROLE_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_ROLE_MANAGE                 = "RDP_ROLE_MANAGE";

    // ================================ CAT_RDP_AUTH ==============================================

    //@AuthLabel(order = 0, category = CAT_RDP_AUTH, i18nKey = AUTH_KEY_RDP_AUTH_REQUEST, tag = { SecSysRole.DBA_ROLE_NAME, SecSysRole.DEV_ROLE_NAME })
    //String RDP_AUTH_REQUEST                = "RDP_AUTH_REQUEST";

    // ================================ CAT_RDP_OP_AUDITS ============================================

    @AuthLabel(order = 0, category = CAT_RDP_OP_AUDIT, i18nKey = AUTH_KEY_RDP_OP_AUDIT_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_OP_AUDIT_READ               = "RDP_OP_AUDIT_READ";

    @AuthLabel(order = 1, category = CAT_RDP_OP_AUDIT, i18nKey = AUTH_KEY_RDP_OP_AUDIT_EXPORT, include = { RDP_OP_AUDIT_READ }, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_OP_AUDIT_EXPORT             = "RDP_OP_AUDIT_EXPORT";

    // ================================ CAT_RDP_PRIMARY_USER_CONFIG ============================================
    @AuthLabel(order = 2, category = CAT_RDP_PRI_PREFERENCE_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_AK_SK_R)
    String RDP_PRI_USER_AK_SK_R            = "RDP_PRI_USER_AK_SK_R";

    @AuthLabel(order = 2, category = CAT_RDP_PRI_PREFERENCE_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_AK_SK_W)
    String RDP_PRI_USER_AK_SK_W            = "RDP_PRI_USER_AK_SK_W";

    @AuthLabel(order = 0, category = CAT_RDP_PRI_PREFERENCE_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_KV_CONF_R, tag = { SecSysRole.DBA_ROLE_NAME, SecSysRole.DEV_ROLE_NAME })
    String RDP_PRI_USER_KV_CONF_R          = "RDP_PRI_USER_KV_CONF_R";

    @AuthLabel(order = 1, category = CAT_RDP_PRI_PREFERENCE_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_KV_CONF_W, include = { RDP_PRI_USER_KV_CONF_R }, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_PRI_USER_KV_CONF_W          = "RDP_PRI_USER_KV_CONF_W";

    @AuthLabel(order = 3, category = CAT_RDP_PRI_PREFERENCE_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_NORMAL_CONF_R, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_PRI_USER_NORMAL_CONF_R      = "RDP_PRI_USER_NORMAL_CONF_R";

    @AuthLabel(order = 4, category = CAT_RDP_PRI_THIRD_PARTY_CONF, i18nKey = AUTH_KEY_RDP_PRI_USER_THIRD_PARTY_CONF_W, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_PRI_USER_THIRD_PARTY_CONF_W = "RDP_PRI_USER_THIRD_PARTY_CONF_W";

    // ================================ CAT_RDP_DATASOURCE ============================================

    @AuthLabel(order = 0, category = CAT_RDP_DS, i18nKey = AUTH_KEY_RDP_DS_READ, tag = { SecSysRole.DBA_ROLE_NAME, SecSysRole.DEV_ROLE_NAME })
    String RDP_DS_READ                     = "RDP_DS_READ";

    @AuthLabel(order = 1, category = CAT_RDP_DS, i18nKey = AUTH_KEY_RDP_DS_MANAGE, include = { RDP_DS_READ }, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_DS_MANAGE                   = "RDP_DS_MANAGE";

    // ================================ CAT_WORKER_TICKET =========================================

    @AuthLabel(order = 0, category = CAT_RDP_WORKER_ORDER, i18nKey = AUTH_KEY_RDP_WORKER_ORDER_READ, tag = { SecSysRole.DBA_ROLE_NAME, SecSysRole.DEV_ROLE_NAME })
    String RDP_WORKER_ORDER_READ           = "RDP_WORKER_ORDER_READ";

    @AuthLabel(order = 1, category = CAT_RDP_WORKER_ORDER, i18nKey = AUTH_KEY_RDP_WORKER_ORDER_REQUEST, include = RDP_WORKER_ORDER_READ, tag = { SecSysRole.DBA_ROLE_NAME,
                                                                                                                                                 SecSysRole.DEV_ROLE_NAME })
    String RDP_WORKER_ORDER_REQUEST        = "RDP_WORKER_ORDER_REQUEST";

    @AuthLabel(order = 2, category = CAT_RDP_WORKER_ORDER, i18nKey = AUTH_KEY_RDP_WORKER_ORDER_APPROVE, include = RDP_WORKER_ORDER_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_WORKER_ORDER_APPROVE        = "RDP_WORKER_ORDER_APPROVE";

    @AuthLabel(order = 3, category = CAT_RDP_WORKER_ORDER, i18nKey = AUTH_KEY_RDP_WORKER_ORDER_EXECUTE, include = RDP_WORKER_ORDER_READ, tag = { SecSysRole.DBA_ROLE_NAME })
    String RDP_WORKER_ORDER_EXECUTE        = "RDP_WORKER_ORDER_EXECUTE";

    //@AuthLabel(order = 3, category = CAT_RDP_WORKER_ORDER, i18nKey = AUTH_KEY_RDP_WORKER_ORDER_MANAGE, include = RDP_WORKER_ORDER_APPROVE, tag = { SecSysRole.DBA_ROLE_NAME })
    //String RDP_WORKER_ORDER_MANAGER        = "RDP_WORKER_ORDER_MANAGER";

    // ================================ CAT_ENV_PARAM =========================================
    //@AuthLabel(order = 0, category = CAT_ENV_PARAM, i18nKey = RDP_AUTH_ENV_PARAM_READ, tag = { SecSysRole.DBA_ROLE_NAME, SecSysRole.DEV_ROLE_NAME })
    //String RDP_ENV_PARAM_READ              = "RDP_ENV_PARAM_READ";

    //@AuthLabel(order = 1, category = CAT_ENV_PARAM, i18nKey = RDP_AUTH_ENV_PARAM_MANAGE, tag = { SecSysRole.DBA_ROLE_NAME })
    //String RDP_ENV_PARAM_MANAGER           = "RDP_ENV_PARAM_MANAGER";

    // ================================ CAT_DM_QUERY ==============================================

    @AuthLabel(order = 0, category = SecAuthCategory.CAT_DM_CONSOLE, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_QUERY_CONSOLE, tag = { SecSysRole.DBA_ROLE_NAME,
                                                                                                                                  SecSysRole.DEV_ROLE_NAME })
    String DM_QUERY_CONSOLE                = "DM_QUERY_CONSOLE";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_CONSOLE, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_QUERY_EXPORT, tag = { SecSysRole.DBA_ROLE_NAME,
                                                                                                                                 SecSysRole.DEV_ROLE_NAME })
    String DM_QUERY_EXPORT                 = "DM_QUERY_EXPORT";

    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_CONSOLE, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_OBJECT_MANAGER, tag = { SecSysRole.DBA_ROLE_NAME,
                                                                                                                                   SecSysRole.DEV_ROLE_NAME }, include = DM_QUERY_CONSOLE)
    String DM_OBJECT_MANAGER               = "DM_OBJECT_MANAGER";
    @AuthLabel(order = 3, category = SecAuthCategory.CAT_DM_CONSOLE, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_DS_MAINTENANCE, tag = { SecSysRole.DBA_ROLE_NAME }, include = { DM_QUERY_CONSOLE,
                                                                                                                                                                           DM_OBJECT_MANAGER })
    String DM_DS_MAINTENANCE               = "DM_DS_MAINTENANCE";

    // ================================ CAT_DM_SYS ============================================
    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_DS, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_DS_READ, tag = SecSysRole.DBA_ROLE_NAME)
    String DM_DS_READ                      = "DM_DS_READ";
    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_DS, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_DS_MANAGE, tag = SecSysRole.DBA_ROLE_NAME, include = DM_DS_READ)
    String DM_DS_MANAGE                    = "DM_DS_MANAGE";

    //@AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_DATA, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_DATA_READ, tag = SecSysRole.DBA_ROLE_NAME)
    //String DM_DATA_READ       = "DM_DATA_READ";
    //@AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_DATA, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_DATA_MANAGE, tag = SecSysRole.DBA_ROLE_NAME)
    //String DM_DATA_MANAGE     = "DM_DATA_MANAGE";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_WORKER, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_WORKER_READ, tag = SecSysRole.DBA_ROLE_NAME)
    String DM_WORKER_READ                  = "DM_WORKER_READ";
    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_WORKER, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_WORKER_MANAGE, tag = SecSysRole.DBA_ROLE_NAME, include = DM_WORKER_READ)
    String DM_WORKER_MANAGE                = "DM_WORKER_MANAGE";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_SECRULES, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_SECRULES_READ, tag = SecSysRole.DBA_ROLE_NAME)
    String DM_SECRULES_READ                = "DM_SECRULES_READ";
    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_SECRULES, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_SECRULES_MANAGE, tag = SecSysRole.DBA_ROLE_NAME, include = DM_SECRULES_READ)
    String DM_SECRULES_MANAGE              = "DM_SECRULES_MANAGE";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_PROJECT, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_PROJECT_READ, tag = { SecSysRole.DEV_ROLE_NAME,
                                                                                                                                 SecSysRole.PM_ROLE_NAME })
    String DM_PROJECT_READ                 = "DM_PROJECT_READ";
    @AuthLabel(order = 3, category = SecAuthCategory.CAT_DM_PROJECT, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_CHANGE_OPERATE, tag = { SecSysRole.DEV_ROLE_NAME,
                                                                                                                                   SecSysRole.PM_ROLE_NAME }, include = DM_PROJECT_READ)
    String DM_PROJECT_OPERATE              = "DM_PROJECT_OPERATE";
    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_PROJECT, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_PROJECT_MANAGE, tag = SecSysRole.PM_ROLE_NAME, include = { DM_PROJECT_READ,
                                                                                                                                                                      DM_PROJECT_OPERATE })
    String DM_PROJECT_MANAGE               = "DM_PROJECT_MANAGE";

    @AuthLabel(order = 7, category = SecAuthCategory.CAT_DM_IM, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_IM_READ, tag = SecSysRole.PM_ROLE_NAME)
    String DM_IM_READ                      = "DM_IM_READ";
    @AuthLabel(order = 8, category = SecAuthCategory.CAT_DM_IM, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_IM_MANAGE, tag = SecSysRole.PM_ROLE_NAME, include = DM_IM_READ)
    String DM_IM_MANAGE                    = "DM_IM_MANAGE";

    @AuthLabel(order = 9, category = SecAuthCategory.CAT_DM_CICD, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_CICD_READ, tag = SecSysRole.PM_ROLE_NAME)
    String DM_CICD_READ                    = "DM_CICD_READ";
    @AuthLabel(order = 10, category = SecAuthCategory.CAT_DM_CICD, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_CICD_MANAGE, tag = SecSysRole.PM_ROLE_NAME, include = DM_CICD_READ)
    String DM_CICD_MANAGE                  = "DM_CICD_MANAGE";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_SQL_AUDIT, i18nKey = SecAuthI18nKeys.AUTH_KEY_DM_SQL_AUDIT_READ, tag = SecSysRole.DBA_ROLE_NAME)
    String DM_SQL_AUDIT                    = "DM_SQL_AUDIT";
}
