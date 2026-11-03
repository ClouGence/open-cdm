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
package com.clougence.clouddm.ds.etcd.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/etcd-plugin")
public interface EtcdDsI18nKeys {

    String PLUGIN_NAME_ETCD          = "PLUGIN_NAME_ETCD";
    String ETCD_AUTH_READ            = "ETCD_AUTH_READ";
    String ETCD_AUTH_MANAGE          = "ETCD_AUTH_MANAGE";
    String ETCD_SESSION_BUSY         = "ETCD_SESSION_BUSY";
    String ETCD_SESSION_CLOSED       = "ETCD_SESSION_CLOSED";
    String ETCD_SESSION_READ_ONLY    = "ETCD_SESSION_READ_ONLY";
    String ETCD_SESSION_UNSUPPORTED  = "ETCD_SESSION_UNSUPPORTED";
    String ETCD_METADATA_UNSUPPORTED = "ETCD_METADATA_UNSUPPORTED";
    String ETCD_QUERY_TIMEOUT        = "ETCD_QUERY_TIMEOUT";
    String ETCD_ENDPOINTS_ERROR      = "ETCD_ENDPOINTS_ERROR";
    String ETCD_TIMEOUT_ERROR        = "ETCD_TIMEOUT_ERROR";
    String ETCD_CLIENTS_CLOSED       = "ETCD_CLIENTS_CLOSED";
    String ETCD_COMMAND_UNSUPPORTED  = "ETCD_COMMAND_UNSUPPORTED";
    String ETCD_COMMAND_KEY_REQUIRED = "ETCD_COMMAND_KEY_REQUIRED";
    String ETCD_SSL_CA_REQUIRED      = "ETCD_SSL_CA_REQUIRED";
}
