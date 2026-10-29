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
package com.clougence.sql.es;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/sql-es")
public interface EsSqlI18nKeys {
    String ES_SYNTAX_ERROR                = "ES_SYNTAX_ERROR";
    String ES_URL_INVALID                 = "ES_URL_INVALID";
    String ES_NDJSON_UNSUPPORTED          = "ES_NDJSON_UNSUPPORTED";
    String ES_EXACT_INDEX_REQUIRED        = "ES_EXACT_INDEX_REQUIRED";
    String ES_DATA_STREAM_UNSUPPORTED     = "ES_DATA_STREAM_UNSUPPORTED";
    String ES_SERVER_VERSION_INVALID      = "ES_SERVER_VERSION_INVALID";
    String ES_VERSION_UNSUPPORTED         = "ES_VERSION_UNSUPPORTED";
    String ES_VERSION_FEATURE_UNSUPPORTED = "ES_VERSION_FEATURE_UNSUPPORTED";
    String ES_ARGS_UNSUPPORTED            = "ES_ARGS_UNSUPPORTED";
    String ES_API_OUT_OF_SCOPE            = "ES_API_OUT_OF_SCOPE";
    String ES_BODY_OUT_OF_SCOPE           = "ES_BODY_OUT_OF_SCOPE";
    String ES_BODY_REQUIRED               = "ES_BODY_REQUIRED";
    String ES_PARAMETER_OUT_OF_SCOPE      = "ES_PARAMETER_OUT_OF_SCOPE";
    String ES_AMBIGUOUS_REQUEST           = "ES_AMBIGUOUS_REQUEST";
    String ES_RESOURCE_SCOPE_MISMATCH     = "ES_RESOURCE_SCOPE_MISMATCH";
    String ES_INDEX_NOT_CONCRETE          = "ES_INDEX_NOT_CONCRETE";
}
