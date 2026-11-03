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
package com.clougence.clouddm.ds.etcd.definition.auth;

import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthKindCondition;
import com.clougence.clouddm.sdk.security.auth.AuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;

public interface EtcdDataAuthLabel {

    @AuthLabel(order = 0, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = EtcdDsI18nKeys.ETCD_AUTH_READ)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String DM_DAUTH_ETCD_READ   = SecDataAuthLabel.DM_DAUTH_QUERY;

    @AuthLabel(order = 1, category = SecAuthCategory.CAT_DM_FOR_DAUTH_STATEMENTS, usedOfRole = false, kind = { AuthKind.DataSource }, i18nKey = EtcdDsI18nKeys.ETCD_AUTH_MANAGE)
    @AuthKindCondition(kind = AuthKind.DataSource, condition = { AuthElementType.Instance })
    String DM_DAUTH_ETCD_MANAGE = SecDataAuthLabel.DM_DAUTH_MANAGE;
}
