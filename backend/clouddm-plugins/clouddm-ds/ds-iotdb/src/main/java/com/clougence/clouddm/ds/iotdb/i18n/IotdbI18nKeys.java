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

    String PLUGIN_NAME_IOTDB                 = "PLUGIN_NAME_IOTDB";
    String IOTDB_CONFIG_DECODE_ERROR         = "IOTDB_CONFIG_DECODE_ERROR";
    String IOTDB_ENDPOINT_INVALID            = "IOTDB_ENDPOINT_INVALID";
    String IOTDB_CONNECTION_FAILED           = "IOTDB_CONNECTION_FAILED";
    String IOTDB_NETWORK_TIMEOUT_LABEL       = "IOTDB_NETWORK_TIMEOUT_LABEL";
    String IOTDB_NETWORK_TIMEOUT_DESC        = "IOTDB_NETWORK_TIMEOUT_DESC";
    String IOTDB_NETWORK_TIMEOUT_INVALID     = "IOTDB_NETWORK_TIMEOUT_INVALID";
    String IOTDB_DIRECT_CONNECTION_REQUIRED  = "IOTDB_DIRECT_CONNECTION_REQUIRED";
    String IOTDB_DIALECT_INVALID             = "IOTDB_DIALECT_INVALID";
    String IOTDB_TABLE_DATABASE_ONLY         = "IOTDB_TABLE_DATABASE_ONLY";
    String IOTDB_OPERATION_UNSUPPORTED       = "IOTDB_OPERATION_UNSUPPORTED";
    String IOTDB_SESSION_DISCONNECTED        = "IOTDB_SESSION_DISCONNECTED";
    String IOTDB_STATEMENT_RESULT_UNKNOWN    = "IOTDB_STATEMENT_RESULT_UNKNOWN";
    String IOTDB_REQUEST_CANCELLED           = "IOTDB_REQUEST_CANCELLED";
    String IOTDB_QUERY_TIMEOUT               = "IOTDB_QUERY_TIMEOUT";
    String IOTDB_DUPLICATE_COLUMN            = "IOTDB_DUPLICATE_COLUMN";
    String IOTDB_DEVICES                     = "IOTDB_DEVICES";
    String IOTDB_TIMESERIES                  = "IOTDB_TIMESERIES";
    String IOTDB_INVALID_TREE_PATH           = "IOTDB_INVALID_TREE_PATH";
    String IOTDB_METADATA_MODEL_MISMATCH     = "IOTDB_METADATA_MODEL_MISMATCH";
    String IOTDB_QUERY_SCOPE_MISMATCH        = "IOTDB_QUERY_SCOPE_MISMATCH";
    String IOTDB_OBJECT_SCRIPT_UNSUPPORTED   = "IOTDB_OBJECT_SCRIPT_UNSUPPORTED";
    String IOTDB_EXAMPLE_TREE_CREATE         = "IOTDB_EXAMPLE_TREE_CREATE";
    String IOTDB_EXAMPLE_TREE_CREATE_ALIGNED = "IOTDB_EXAMPLE_TREE_CREATE_ALIGNED";
    String IOTDB_EXAMPLE_TREE_INSERT         = "IOTDB_EXAMPLE_TREE_INSERT";
    String IOTDB_EXAMPLE_TREE_INSERT_ALIGNED = "IOTDB_EXAMPLE_TREE_INSERT_ALIGNED";
    String IOTDB_EXAMPLE_TREE_DELETE         = "IOTDB_EXAMPLE_TREE_DELETE";
    String IOTDB_EXAMPLE_TREE_DROP           = "IOTDB_EXAMPLE_TREE_DROP";
    String IOTDB_EXAMPLE_TABLE_CREATE        = "IOTDB_EXAMPLE_TABLE_CREATE";
    String IOTDB_EXAMPLE_TABLE_ALTER         = "IOTDB_EXAMPLE_TABLE_ALTER";
    String IOTDB_EXAMPLE_TABLE_DROP_COLUMN   = "IOTDB_EXAMPLE_TABLE_DROP_COLUMN";
    String IOTDB_EXAMPLE_TABLE_INSERT        = "IOTDB_EXAMPLE_TABLE_INSERT";
    String IOTDB_EXAMPLE_TABLE_UPDATE        = "IOTDB_EXAMPLE_TABLE_UPDATE";
    String IOTDB_EXAMPLE_TABLE_DELETE        = "IOTDB_EXAMPLE_TABLE_DELETE";
}
