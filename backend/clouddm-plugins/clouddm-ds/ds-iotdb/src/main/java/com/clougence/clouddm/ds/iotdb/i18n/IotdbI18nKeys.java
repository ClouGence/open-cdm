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

package com.clougence.clouddm.ds.iotdb.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/iotdb-plugin")
public interface IotdbI18nKeys {

    String PLUGIN_NAME_IOTDB                = "PLUGIN_NAME_IOTDB";
    String IOTDB_CONFIG_DECODE_ERROR        = "IOTDB_CONFIG_DECODE_ERROR";
    String IOTDB_ENDPOINT_INVALID           = "IOTDB_ENDPOINT_INVALID";
    String IOTDB_CONNECTION_FAILED          = "IOTDB_CONNECTION_FAILED";
    String IOTDB_NETWORK_TIMEOUT_LABEL      = "IOTDB_NETWORK_TIMEOUT_LABEL";
    String IOTDB_NETWORK_TIMEOUT_DESC       = "IOTDB_NETWORK_TIMEOUT_DESC";
    String IOTDB_NETWORK_TIMEOUT_INVALID    = "IOTDB_NETWORK_TIMEOUT_INVALID";
    String IOTDB_DIRECT_CONNECTION_REQUIRED = "IOTDB_DIRECT_CONNECTION_REQUIRED";
    String IOTDB_DIALECT_INVALID            = "IOTDB_DIALECT_INVALID";
    String IOTDB_TABLE_DATABASE_ONLY        = "IOTDB_TABLE_DATABASE_ONLY";
}
