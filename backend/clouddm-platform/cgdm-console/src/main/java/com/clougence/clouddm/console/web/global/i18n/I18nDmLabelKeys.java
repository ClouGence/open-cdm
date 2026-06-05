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
package com.clougence.clouddm.console.web.global.i18n;

import com.clougence.utils.i18n.I18nResource;

/**
 * @author wanshao create time is 2020/9/14
 **/
@I18nResource("/i18n/label")
public enum I18nDmLabelKeys {
    SELF_MAINTENANCE,
    ALIBABA_CLOUD_HOSTED,
    AWS_CLOUD_HOSTED,
    MICROSOFT_AZURE_CLOUD_HOSTED,
    ALIBABA_CLOUD,
    HUAWEI_CLOUD_HOSTED,

    LABEL_ALL,
    LABEL_EMPTY,
    LABEL_WORKER_ALIVE_ALERT,
    LABEL_TIME_UNIT_MS,
    LABEL_UNKNOWN,

    RDB_ISOLATION_DEFAULT,
    RDB_ISOLATION_READ_UNCOMMITTED,
    RDB_ISOLATION_READ_COMMITTED,
    RDB_ISOLATION_REPEATABLE_READ,
    RDB_ISOLATION_SERIALIZABLE,

    RDB_ENV,
    RDB_INSTANCE,
    RDB_CATALOG,
    RDB_SCHEMA,
    RDB_TABLE_OR_VIEW,
    RDB_TABLE,
    RDB_VIEW,
    RDB_MATERIALIZED,
    RDB_COLUMN,
    RDB_INDEX,
    RDB_CONSTRAINT,
    RDB_SEQUENCE,
    RDB_FUNCTION,
    RDB_PROCEDURE,
    RDB_TRIGGER,
    RDB_SYNONYM,
    RDB_QUERY,
    RDB_UPDATE,
    RDB_INSERT,
    RDB_DELETE,

    CACHE_KEY,

    SEC_LEVEL_SUGGEST,
    SEC_LEVEL_CONFIRM,
    SEC_LEVEL_TICKET,
    SEC_LEVEL_FAILURE,
    SEC_LEVEL_UNKNOWN,
    SEC_SEN_MODE_ROW,
    SEC_SEN_MODE_VALUE,
    SEC_SEN_MATCH_EXACT,
    SEC_SEN_MATCH_PREFIX,
    SEC_SEN_MATCH_SUFFIX,
    SEC_SEN_MATCH_INCLUDE,
    SEC_SEN_KIND_QUERY,
    SEC_SEN_KIND_SENSITIVE,

    DM_DS_STATUS_NORMAL,
    DM_DS_STATUS_DELETED,
    DM_DS_STATUS_NO_AUTHORITY,
    DM_DS_STATUS_QUERY_NOT_ENABLED,
    DM_DS_STATUS_NOT_WORKER,
    DM_DS_STATUS_CONNECTION_FAILED,
    DM_DS_STATUS_NO_AUTHENTICATION,
    DM_DS_STATUS_UNSUPPORTED,
    DM_DS_STATUS_UNKNOWN,

    DEVOPS_PROVIDER_GITEE,
    DEVOPS_PROVIDER_GITHUB,
    DEVOPS_PROVIDER_DINGTALK,
    DEVOPS_PROVIDER_WECHAT,
    DEVOPS_PROVIDER_FEISHU,
}
