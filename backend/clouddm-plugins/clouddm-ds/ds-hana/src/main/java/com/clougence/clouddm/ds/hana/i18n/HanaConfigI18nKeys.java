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
package com.clougence.clouddm.ds.hana.i18n;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/hana-config")
public interface HanaConfigI18nKeys extends ConfigI18nKey {

    String CONFIG_HANA_DATABASE_LABEL        = "CONFIG_HANA_DATABASE_LABEL";
    String CONFIG_HANA_DATABASE_DESC         = "CONFIG_HANA_DATABASE_DESC";
    String CONFIG_HANA_TLS_HOST_LABEL        = "CONFIG_HANA_TLS_HOST_LABEL";
    String CONFIG_HANA_TLS_HOST_DESC         = "CONFIG_HANA_TLS_HOST_DESC";
    String CONFIG_HANA_URL_LABEL             = "CONFIG_HANA_URL_LABEL";
    String CONFIG_HANA_URL_DESC              = "CONFIG_HANA_URL_DESC";
    String CONFIG_HANA_URL_ERROR             = "CONFIG_HANA_URL_ERROR";
    String CONFIG_HANA_URL_SSH_ERROR         = "CONFIG_HANA_URL_SSH_ERROR";
    String CONFIG_HANA_TLS_HOST_REQUIRED     = "CONFIG_HANA_TLS_HOST_REQUIRED";
    String CONFIG_HANA_TLS_HOST_ERROR        = "CONFIG_HANA_TLS_HOST_ERROR";
    String CONFIG_HANA_TLS_FILE_REQUIRED     = "CONFIG_HANA_TLS_FILE_REQUIRED";
    String CONFIG_HANA_TIMEOUT_ERROR         = "CONFIG_HANA_TIMEOUT_ERROR";
    String CONFIG_HANA_CATALOG_MISMATCH      = "CONFIG_HANA_CATALOG_MISMATCH";
    String CONFIG_HANA_CATALOG_UNSUPPORTED   = "CONFIG_HANA_CATALOG_UNSUPPORTED";
    String CONFIG_HANA_ISOLATION_UNSUPPORTED = "CONFIG_HANA_ISOLATION_UNSUPPORTED";
}
