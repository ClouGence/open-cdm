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

import static com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory.CAT_RDP_DS;
import static com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys.RDP_AUTH_DATA_DS_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys.RDP_AUTH_DATA_DS_READ;

import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthKindCondition;
import com.clougence.clouddm.sdk.security.auth.AuthLabel;

/**
 * @author mode 2021/1/6 19:00
 */
public interface SecDataAuthLabel {

    @AuthLabel(order = 0, category = CAT_RDP_DS, usedOfRole = false, i18nKey = RDP_AUTH_DATA_DS_READ, //
            kind = { AuthKind.DataSource }, global = true)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String RDP_DAUTH_DS_READ    = "RDP_DAUTH_DS_READ";

    @AuthLabel(order = 1, category = CAT_RDP_DS, usedOfRole = false, include = { RDP_DAUTH_DS_READ }, i18nKey = RDP_AUTH_DATA_DS_MANAGE, //
            kind = { AuthKind.DataSource }, global = true)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String RDP_DAUTH_DS_MANAGER = "RDP_DATA_DS_MANAGER";

    // ================================ CAT_DM_QUERY ==============================================

    @AuthLabel(order = 0, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_QUERY,//
            kind = { AuthKind.DataSource })
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Column })
    String DM_DAUTH_QUERY       = "DM_QUERY";

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_CALL,//
            kind = { AuthKind.DataSource }, include = DM_DAUTH_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema })
    String DM_DAUTH_CALL        = "DM_CALL";

    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_DML,//
            kind = { AuthKind.DataSource }, include = DM_DAUTH_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Column })
    String DM_DAUTH_DML         = "DM_DML";

    @AuthLabel(order = 3, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_DDL,//
            kind = { AuthKind.DataSource }, include = DM_DAUTH_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Column })
    String DM_DAUTH_DDL         = "DM_DDL";

    @AuthLabel(order = 4, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_OBJ,//
            kind = { AuthKind.DataSource }, include = { DM_DAUTH_QUERY, DM_DAUTH_DDL })
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.View,
                                                                 AuthElementType.Materialized, AuthElementType.Sequence, AuthElementType.Synonym, AuthElementType.Function,
                                                                 AuthElementType.Procedure, AuthElementType.Trigger })
    String DM_DAUTH_OBJ         = "DM_OBJ";

    @AuthLabel(order = 5, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_SPACE,//
            kind = { AuthKind.DataSource })
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema })
    String DM_DAUTH_SPACE       = "DM_SPACE";

    @AuthLabel(order = 6, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_DCL,//
            kind = { AuthKind.DataSource })
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String DM_DAUTH_DCL         = "DM_DCL";

    @AuthLabel(order = 7, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_OTHER,//
            kind = { AuthKind.DataSource })
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema })
    String DM_DAUTH_OTHER       = "DM_OTHER";

    // ================================ CAT_DM_DATA ===============================================

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_FOR_DAUTH_FUNCTION, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_SENSITIVE,//
            kind = { AuthKind.DataSource }, global = true)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table })
    String DM_DAUTH_SENSITIVE   = "DM_SENSITIVE";

    @AuthLabel(order = 2, category = SecAuthCategory.CAT_DM_FOR_DAUTH_FUNCTION, usedOfRole = false, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_TICKET,//
            kind = { AuthKind.DataSource }, global = true)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String DM_DAUTH_TICKET      = "DM_TICKET";
}
