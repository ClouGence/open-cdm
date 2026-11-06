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
package com.clougence.clouddm.ds.iotdb.metadata;

import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthKindCondition;
import com.clougence.clouddm.sdk.security.auth.AuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;

public interface IotdbDataAuthLabel {

    @AuthLabel(order = 0, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Timeseries })
    String DM_DAUTH_IOTDB_QUERY = SecDataAuthLabel.DM_DAUTH_QUERY;

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_DML, include = DM_DAUTH_IOTDB_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Timeseries })
    String DM_DAUTH_IOTDB_DML   = SecDataAuthLabel.DM_DAUTH_DML;

    @AuthLabel(order = 3, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_DDL, include = DM_DAUTH_IOTDB_QUERY)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema, AuthElementType.Table,
                                                                 AuthElementType.Timeseries })
    String DM_DAUTH_IOTDB_DDL   = SecDataAuthLabel.DM_DAUTH_DDL;

    @AuthLabel(order = 5, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = SecAuthI18nKeys.AUTH_DATA_DM_SPACE)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance, AuthElementType.Catalog, AuthElementType.Schema })
    String DM_DAUTH_IOTDB_SPACE = SecDataAuthLabel.DM_DAUTH_SPACE;
}
