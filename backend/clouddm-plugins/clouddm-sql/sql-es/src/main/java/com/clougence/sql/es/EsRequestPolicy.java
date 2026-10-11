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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** The executable subset is deliberately narrower than the request grammar. */
public final class EsRequestPolicy {
    // filter_path could hide shard failures or timeout/acknowledgement fields needed for execution status.
    private static final Set<String> COMMON_PARAMETERS   = Set.of("pretty", "human", "error_trace");
    private static final Set<String> SEARCH_PARAMETERS   = Set
        .of("q", "df", "analyzer", "analyze_wildcard", "default_operator", "lenient", "size", "from", "sort", "_source", "_source_includes", "_source_excludes", "stored_fields", "docvalue_fields", "track_total_hits", "track_scores", "timeout", "terminate_after", "routing", "preference", "request_cache", "allow_partial_search_results", "search_type", "typed_keys", "rest_total_hits_as_int", "version", "explain", "seq_no_primary_term", "max_concurrent_shard_requests", "batched_reduce_size", "pre_filter_shard_size", "stats");
    private static final Set<String> DOCUMENT_PARAMETERS = Set
        .of("routing", "refresh", "timeout", "wait_for_active_shards", "if_seq_no", "if_primary_term", "version", "version_type", "op_type", "retry_on_conflict", "_source", "_source_includes", "_source_excludes", "stored_fields", "realtime", "preference");
    private static final Set<String> INDEX_PARAMETERS    = Set
        .of("timeout", "master_timeout", "wait_for_active_shards", "include_type_name", "flat_settings", "include_defaults", "local");

    private EsRequestPolicy(){
    }

    public static EsRequestAnalysis analyze(EsRequest request) {
        EsOperation operation = EsOperation.resolve(request);
        EsPathSegment first = request.segments().get(0);
        EsIndexReference index = new EsIndexReference(first.value(), first.range());
        validateParameters(request, operation);
        EsQueryReferences references = new EsQueryReferences(index.name());
        EsJsonObject body = request.body();
        if (body != null) {
            switch (operation) {
                case SEARCH -> references.search(body);
                case COUNT -> {
                    var fields = EsJsonAccess.fields(body, Set.of("query"));
                    if (fields.containsKey("query")) {
                        references.query(fields.get("query"));
                    }
                }
                case KNN_SEARCH -> {
                    var fields = EsJsonAccess.fields(body, Set.of("knn", "_source", "fields", "docvalue_fields", "stored_fields", "filter"));
                    if (fields.containsKey("knn")) {
                        references.knn(fields.get("knn"));
                    }
                    if (fields.containsKey("filter")) {
                        references.query(fields.get("filter"));
                    }
                }
                case INSERT_DOCUMENT, REPLACE_DOCUMENT -> {
                    /* Entire body is opaque document data. */ }
                case UPDATE_DOCUMENT -> update(body);
                case UPDATE_BY_QUERY, DELETE_BY_QUERY -> {
                    var fields = EsJsonAccess.fields(body, Set.of("query", "script", "max_docs", "slice", "conflicts"));
                    if (fields.containsKey("query")) {
                        references.query(fields.get("query"));
                    }
                    if (fields.containsKey("script")) {
                        if (operation == EsOperation.DELETE_BY_QUERY) {
                            throw new EsParseException(body.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
                        }
                        EsQueryReferences.script(fields.get("script"));
                    }
                }
                case CREATE_INDEX -> {
                    var fields = EsJsonAccess.fields(body, Set.of("settings", "mappings"));
                    if (fields.containsKey("settings")) {
                        settings(fields.get("settings"), "");
                    }
                    if (fields.containsKey("mappings")) {
                        mappings(fields.get("mappings"));
                    }
                }
                case PUT_MAPPING -> mappings(body);
                case PUT_SETTINGS -> settings(body, "");
                default -> throw new EsParseException(body.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
        } else if (Set
            .of(EsOperation.INSERT_DOCUMENT, EsOperation.REPLACE_DOCUMENT, EsOperation.UPDATE_DOCUMENT, EsOperation.PUT_MAPPING, EsOperation.PUT_SETTINGS, EsOperation.KNN_SEARCH)
            .contains(operation)) {
            throw new EsParseException(request.range(), EsSqlI18nKeys.ES_BODY_REQUIRED);
        }
        if (operation == EsOperation.REPLACE_DOCUMENT && request.parameters().stream().anyMatch(p -> p.name().equals("op_type") && p.value().equals("create"))) {
            operation = EsOperation.INSERT_DOCUMENT;
        }
        return new EsRequestAnalysis(operation, index, references.reads());
    }

    private static void validateParameters(EsRequest request, EsOperation operation) {
        Set<String> allowed = switch (operation) {
            case SEARCH, COUNT, KNN_SEARCH -> SEARCH_PARAMETERS;
            case READ_DOCUMENT, INSERT_DOCUMENT, REPLACE_DOCUMENT, UPDATE_DOCUMENT, DELETE_DOCUMENT -> DOCUMENT_PARAMETERS;
            case UPDATE_BY_QUERY, DELETE_BY_QUERY -> Set
                .of("q", "routing", "timeout", "refresh", "wait_for_active_shards", "conflicts", "max_docs", "requests_per_second", "scroll_size", "slices", "wait_for_completion");
            default -> INDEX_PARAMETERS;
        };
        Set<String> names = new HashSet<>();
        for (EsQueryParameter parameter : request.parameters()) {
            if (!names.add(parameter.name())) {
                throw new EsParseException(parameter.range(), EsSqlI18nKeys.ES_AMBIGUOUS_REQUEST);
            }
            if (!COMMON_PARAMETERS.contains(parameter.name()) && !allowed.contains(parameter.name())) {
                throw new EsParseException(parameter.range(), EsSqlI18nKeys.ES_PARAMETER_OUT_OF_SCOPE, parameter.name());
            }
            if (parameter.name().equals("wait_for_completion") && !parameter.value().equals("true")) {
                throw new EsParseException(parameter.range(), EsSqlI18nKeys.ES_PARAMETER_OUT_OF_SCOPE, parameter.name());
            }
        }
    }

    private static void update(EsJsonValue value) {
        var fields = EsJsonAccess.fields(value, Set.of("doc", "upsert", "doc_as_upsert", "scripted_upsert", "script", "detect_noop", "_source"));
        if (fields.containsKey("script")) {
            EsQueryReferences.script(fields.get("script"));
        }
        // doc/upsert may contain arbitrary field names, including index, query, script or aliases.
    }

    private static void settings(EsJsonValue value, String prefix) {
        for (var entry : EsJsonAccess.object(value).entrySet()) {
            String name = prefix + entry.getKey();
            if (entry.getValue() instanceof EsJsonObject) {
                settings(entry.getValue(), name + ".");
                continue;
            }
            if (name.startsWith("index.")) {
                name = name.substring("index.".length());
            }
            if (!Set.of("number_of_shards", "number_of_replicas", "refresh_interval", "max_result_window", "blocks.read_only", "blocks.write", "blocks.read_only_allow_delete")
                .contains(name)) {
                throw new EsParseException(entry.getValue().range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
        }
    }

    private static void mappings(EsJsonValue value) {
        var fields = EsJsonAccess.fields(value, Set
            .of("properties", "runtime", "dynamic_templates", "dynamic", "date_detection", "numeric_detection", "dynamic_date_formats", "_source", "_meta", "_routing", "_field_names", "enabled", "subobjects"));
        for (String name : Set.of("properties", "runtime")) {
            if (fields.containsKey(name)) {
                for (EsJsonValue definition : EsJsonAccess.object(fields.get(name)).values()) {
                    mappingField(definition);
                }
            }
        }
        if (fields.containsKey("dynamic_templates")) {
            EsJsonValue templates = fields.get("dynamic_templates");
            if (!(templates instanceof EsJsonArray array)) {
                throw new EsParseException(templates.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
            for (EsJsonValue item : array.values()) {
                for (EsJsonValue template : EsJsonAccess.object(item).values()) {
                    var options = EsJsonAccess.object(template);
                    for (String name : Set.of("mapping", "runtime")) {
                        if (options.containsKey(name)) {
                            mappingField(options.get(name));
                        }
                    }
                }
            }
        }
    }

    private static void mappingField(EsJsonValue value) {
        Map<String, EsJsonValue> fields = EsJsonAccess.object(value);
        if (fields.containsKey("type")) {
            String type = EsJsonAccess.string(fields.get("type"));
            if (Set.of("percolator", "lookup", "semantic_text").contains(type) || type.contains("{")) {
                throw new EsParseException(fields.get("type").range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
        }
        if (fields.containsKey("script")) {
            EsQueryReferences.script(fields.get("script"));
        }
        for (String name : Set.of("properties", "fields")) {
            if (fields.containsKey(name)) {
                for (EsJsonValue child : EsJsonAccess.object(fields.get(name)).values()) {
                    mappingField(child);
                }
            }
        }
    }
}
