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
package com.clougence.clouddm.ds.opensearch.execute;

import java.util.Set;

import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsOperation;
import com.clougence.sql.es.EsRequest;

final class OpenSearchRequestSecurity {
    private static final Set<String> UNSUPPORTED_OUTPUTS = Set
        .of("aggs", "aggregations", "fields", "docvalue_fields", "stored_fields", "script_fields", "highlight", "collapse", "explain", "profile");

    private OpenSearchRequestSecurity(){
    }

    static void checkMasking(EsRequest request, EsOperation operation, boolean masking) {
        if (!masking || request.method().equals("HEAD")) {
            return;
        }
        // Mapping/settings contain arbitrary metadata, not document columns with reliable provenance.
        if (operation == EsOperation.READ_INDEX) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
        }
        if (operation != EsOperation.SEARCH && operation != EsOperation.READ_DOCUMENT) {
            return;
        }
        for (var parameter : request.parameters()) {
            if (UNSUPPORTED_OUTPUTS.contains(parameter.name())) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
            }
        }
        if (request.body() != null) {
            for (var member : request.body().members()) {
                if (UNSUPPORTED_OUTPUTS.contains(member.name().value())) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
                }
            }
        }
    }
}
