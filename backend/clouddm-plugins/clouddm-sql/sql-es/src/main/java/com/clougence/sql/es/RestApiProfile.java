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

import java.util.Set;
import java.util.regex.Pattern;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;

/** Product selection is confined here; consumers use capabilities and request-position policies. */
public record RestApiProfile(String version, EsVersionCapabilities capabilities, Set<String> indexParameters, Set<String> excludedSearchFields, Set<String> excludedMappingFields,
                             Set<String> excludedQueryTypes, Set<String> excludedSorts, Set<String> excludedScriptLanguages, String featureErrorKey, String preferredManagerTimeout,
                             String engineName) {

    private static final Pattern OPENSEARCH_VERSION = Pattern.compile("([23])\\.(\\d+)\\.(\\d+)(?:[-+][A-Za-z0-9.-]+)?");
    private static final Set<String> ES_INDEX_PARAMETERS = Set
        .of("timeout", "master_timeout", "wait_for_active_shards", "include_type_name", "flat_settings", "include_defaults", "local");
    private static final Set<String> OPENSEARCH_INDEX_PARAMETERS = Set
        .of("timeout", "master_timeout", "cluster_manager_timeout", "wait_for_active_shards", "flat_settings", "include_defaults", "local");

    public static RestApiProfile from(SqlParserParameters parameters) {
        parameters = SqlParserParameters.nullToEmpty(parameters);
        String product = parameters.get(RestProduct.PARAMETER);
        if (product == null) {
            product = RestProduct.ELASTICSEARCH.name();
        }
        return switch (product) {
            case "ELASTICSEARCH" -> elasticsearch(parameters.version());
            case "OPENSEARCH" -> opensearch(parameters.version());
            default -> throw ThirdPartyApiException.as().with(EsSqlI18nKeys.REST_PRODUCT_INVALID);
        };
    }

    private static RestApiProfile elasticsearch(String number) {
        EsVersionCapabilities capabilities = null;
        if (number != null && !number.isBlank()) {
            capabilities = EsServerVersion.parse(number).capabilities();
        }
        // Missing ES versions retain the existing syntax-only version policy.
        return new RestApiProfile(number,
            capabilities,
            ES_INDEX_PARAMETERS,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED,
            "master_timeout",
            EsSqlEngineSpi.NAME);
    }

    private static RestApiProfile opensearch(String number) {
        int major = 0;
        int minor = 0;
        if (number != null && !number.isBlank()) {
            var match = OPENSEARCH_VERSION.matcher(number);
            if (!match.matches()) {
                throw ThirdPartyApiException.as().with(EsSqlI18nKeys.OPENSEARCH_SERVER_VERSION_INVALID);
            }
            try {
                major = Integer.parseInt(match.group(1));
                minor = Integer.parseInt(match.group(2));
                Integer.parseInt(match.group(3));
            } catch (NumberFormatException e) {
                throw ThirdPartyApiException.as().with(EsSqlI18nKeys.OPENSEARCH_SERVER_VERSION_INVALID);
            }
        }
        boolean pit = major == 3 || (major == 2 && minor >= 4);
        boolean shardDoc = major == 3 && minor >= 3;
        Set<String> excludedSorts = Set.of();
        if (!shardDoc) {
            excludedSorts = Set.of("_shard_doc");
        }
        // PIT availability and stable pagination ordering are distinct capabilities.
        EsVersionCapabilities capabilities = new EsVersionCapabilities(pit, shardDoc, false, null, false, false, false);
        return new RestApiProfile(number,
            capabilities,
            OPENSEARCH_INDEX_PARAMETERS,
            Set.of("knn", "retriever", "runtime_mappings"),
            Set.of("runtime", "subobjects"),
            Set.of("knn"),
            excludedSorts,
            Set.of("knn"),
            EsSqlI18nKeys.OPENSEARCH_FEATURE_UNSUPPORTED,
            "cluster_manager_timeout",
            OpenSearchSqlEngineSpi.NAME);
    }

    void unsupported(EsSourceRange range, String feature) {
        String number = version;
        if (number == null || number.isBlank()) {
            number = "?";
        }
        throw new EsParseException(range, featureErrorKey, feature, number);
    }
}
