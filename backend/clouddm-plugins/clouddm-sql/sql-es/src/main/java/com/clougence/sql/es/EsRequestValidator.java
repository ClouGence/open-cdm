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

import java.util.List;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.es.antlr.EsParser;
import com.clougence.sql.es.antlr.EsParserBaseListener;

final class EsRequestValidator extends EsParserBaseListener {
    private final EsServerVersion version;

    EsRequestValidator(SqlParserParameters parameters){
        String number = SqlParserParameters.nullToEmpty(parameters).version();
        if (number == null || number.isBlank()) {
            version = null;
        } else {
            version = EsServerVersion.parse(number);
        }
    }

    @Override
    public void exitHeader(EsParser.HeaderContext context) {
        // Parse listeners also run while a syntax exception unwinds an incomplete header.
        if (context.target() == null || context.target().path() == null || context.target().path().getStop() == null) {
            return;
        }
        List<EsPathSegment> segments = EsAstBuilder.segments(context.target().path());
        // ES 7 also exposes typed bulk/msearch routes. _doc/{id} remains a document route.
        if (segments.size() >= 3 && !segments.get(0).value().startsWith("_") && !segments.get(1).value().startsWith("_")) {
            EsPathSegment operation = segments.get(2);
            if (Set.of("_bulk", "_msearch").contains(operation.value())) {
                throw new EsParseException(operation.range(), EsSqlI18nKeys.ES_NDJSON_UNSUPPORTED, operation.value());
            }
        }
        for (int i = 0; i < Math.min(segments.size(), 2); i++) {
            EsPathSegment segment = segments.get(i);
            if (i == 1 && segments.get(0).value().startsWith("_")) {
                break;
            }
            if (Set.of("_bulk", "_msearch").contains(segment.value())) {
                throw new EsParseException(segment.range(), EsSqlI18nKeys.ES_NDJSON_UNSUPPORTED, segment.value());
            }
        }
    }

    void validate(EsRequest request) {
        List<EsPathSegment> path = request.segments();
        for (EsPathSegment segment : path) {
            String value = segment.value();
            if (value.equals(".") || value.equals("..") || value.codePoints().anyMatch(c -> c < 32 || c == 127 || c == '/' || c == '\\')) {
                throw new EsParseException(segment.range(), EsSqlI18nKeys.ES_URL_INVALID);
            }
        }
        if (!path.isEmpty()) {
            EsPathSegment first = path.get(0);
            String index = first.value();
            if (index.equals("_all") || index.contains("*") || index.contains(",") || index.contains(":") || index.contains("?")) {
                throw new EsParseException(first.range(), EsSqlI18nKeys.ES_EXACT_INDEX_REQUIRED);
            }
            if (index.equals("_data_stream")) {
                throw new EsParseException(first.range(), EsSqlI18nKeys.ES_DATA_STREAM_UNSUPPORTED);
            }
            if (Set.of("_search", "_count", "_knn_search", "_update_by_query", "_delete_by_query").contains(index)) {
                throw new EsParseException(first.range(), EsSqlI18nKeys.ES_EXACT_INDEX_REQUIRED);
            }
        }

        if (version == null) {
            return;
        }

        EsVersionCapabilities capabilities = version.capabilities();
        boolean indexed = !path.isEmpty() && !path.get(0).value().startsWith("_");
        if (!capabilities.mappingTypes() && indexed && path.size() >= 2) {
            EsPathSegment second = path.get(1);
            if (!second.value().startsWith("_") || (path.size() >= 3 && second.value().equals("_mapping") && !path.get(2).value().equals("field"))) {
                unsupported(second.range(), "mapping types");
            }
        }

        for (EsQueryParameter parameter : request.parameters()) {
            if (!capabilities.mappingTypes() && parameter.name().equals("include_type_name")) {
                unsupported(parameter.range(), "include_type_name");
            }
        }

        if (indexed && path.size() == 2 && path.get(1).value().equals("_knn_search") && !capabilities.legacyKnnSearch()) {
            unsupported(path.get(1).range(), "_knn_search");
        }

        // Only search request options are versioned here; document fields with these names are ordinary data.
        boolean search = indexed && path.size() == 2 && path.get(1).value().equals("_search");
        boolean typedSearch = indexed && path.size() == 3 && !path.get(1).value().startsWith("_") && path.get(2).value().equals("_search");
        if ((search || typedSearch) && request.body() != null) {
            for (EsJsonMember member : request.body().members()) {
                String name = member.name().value();
                if ((name.equals("knn") && !capabilities.searchKnn()) || (name.equals("retriever") && !capabilities.retriever())) {
                    unsupported(member.name().range(), name);
                }
            }
        }
    }

    private void unsupported(EsSourceRange range, String feature) {
        throw new EsParseException(range, EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED, feature, version.number());
    }
}
