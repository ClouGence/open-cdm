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

package com.clougence.clouddm.ds.greptimedb.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/greptimedb-config")
public interface GreptimeConfigI18nKeys {

    String GREPTIMEDB_CONFIG_DECODE_ERROR      = "GREPTIMEDB_CONFIG_DECODE_ERROR";
    String GREPTIMEDB_SOCKET_TIMEOUT_DESC      = "GREPTIMEDB_SOCKET_TIMEOUT_DESC";
    String GREPTIMEDB_CONNECT_TIMEOUT_INVALID  = "GREPTIMEDB_CONNECT_TIMEOUT_INVALID";
    String GREPTIMEDB_SOCKET_TIMEOUT_INVALID   = "GREPTIMEDB_SOCKET_TIMEOUT_INVALID";
    String GREPTIMEDB_TRANSACTIONS_UNSUPPORTED = "GREPTIMEDB_TRANSACTIONS_UNSUPPORTED";
    String GREPTIMEDB_QUERY_CANCELLED          = "GREPTIMEDB_QUERY_CANCELLED";
    String GREPTIMEDB_QUERY_TIMEOUT            = "GREPTIMEDB_QUERY_TIMEOUT";
    String GREPTIMEDB_READ_TIMEOUT             = "GREPTIMEDB_READ_TIMEOUT";
    String GREPTIMEDB_CONNECTION_LOST          = "GREPTIMEDB_CONNECTION_LOST";
    String GREPTIMEDB_SESSION_DISCONNECTED     = "GREPTIMEDB_SESSION_DISCONNECTED";
    String GREPTIMEDB_DUPLICATE_COLUMN         = "GREPTIMEDB_DUPLICATE_COLUMN";
    String GREPTIMEDB_QUERY_SCOPE_MISMATCH     = "GREPTIMEDB_QUERY_SCOPE_MISMATCH";
}
