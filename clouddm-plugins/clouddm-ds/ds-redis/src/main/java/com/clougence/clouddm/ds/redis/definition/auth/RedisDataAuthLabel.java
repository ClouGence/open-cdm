package com.clougence.clouddm.ds.redis.definition.auth;

import com.clougence.clouddm.sdk.model.feature.AuthOwnerProduct;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthKindCondition;
import com.clougence.clouddm.sdk.security.auth.AuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;

/**
 * @author mode 2021/1/6 19:00
 */
@AuthOwnerProduct(RdpFeatureIDs.PRODUCT_CLOUD_DM)
public interface RedisDataAuthLabel {

    @AuthLabel(order = 0, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_REDIS_READ)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Schema })
    String DM_DAUTH_REDIS_READ  = SecDataAuthLabel.DM_DAUTH_QUERY;

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_REDIS_WRITE, include = DM_DAUTH_REDIS_READ)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Schema })
    String DM_DAUTH_REDIS_WRITE = SecDataAuthLabel.DM_DAUTH_DML;

    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_REDIS_ADMIN)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Schema })
    String DM_DAUTH_REDIS_ADMIN = SecDataAuthLabel.DM_DAUTH_DCL;

    @AuthLabel(order = 3, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_REDIS_OTHER)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Schema })
    String DM_DAUTH_REDIS_OTHER = SecDataAuthLabel.DM_DAUTH_OTHER;
}
