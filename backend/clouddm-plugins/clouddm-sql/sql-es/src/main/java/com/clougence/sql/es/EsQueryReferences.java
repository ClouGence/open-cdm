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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Traverses DSL positions only. Document values and query literals are never interpreted as requests. */
final class EsQueryReferences {
    private static final Set<String>     LEAF_QUERIES  = Set
        .of("match_all", "match_none", "term", "range", "exists", "ids", "prefix", "wildcard", "regexp", "fuzzy", "match", "match_bool_prefix", "match_phrase", "match_phrase_prefix", "multi_match", "combined_fields", "query_string", "simple_query_string", "distance_feature", "rank_feature", "geo_distance", "geo_bounding_box", "geo_polygon", "parent_id", "span_term");
    private static final Set<String>     SEARCH_FIELDS = Set
        .of("query", "post_filter", "aggs", "aggregations", "sort", "from", "size", "_source", "fields", "docvalue_fields", "stored_fields", "track_total_hits", "track_scores", "timeout", "terminate_after", "version", "seq_no_primary_term", "explain", "min_score", "search_after", "stats", "profile", "highlight", "collapse", "rescore", "knn", "script_fields");
    private static final Set<String>     AGGREGATIONS  = Set
        .of("avg", "sum", "min", "max", "value_count", "cardinality", "stats", "extended_stats", "percentiles", "percentile_ranks", "median_absolute_deviation", "weighted_avg", "boxplot", "string_stats", "geo_bounds", "geo_centroid", "terms", "multi_terms", "rare_terms", "significant_terms", "significant_text", "histogram", "date_histogram", "auto_date_histogram", "range", "date_range", "ip_range", "missing", "nested", "reverse_nested", "global", "sampler", "diversified_sampler", "geohash_grid", "geotile_grid", "geo_distance", "composite", "filter", "filters", "adjacency_matrix", "top_hits", "top_metrics", "bucket_sort", "bucket_selector", "bucket_script", "avg_bucket", "sum_bucket", "min_bucket", "max_bucket", "stats_bucket", "extended_stats_bucket", "percentiles_bucket", "derivative", "cumulative_sum", "serial_diff", "moving_fn");
    private final String                 index;
    private final List<EsIndexReference> reads         = new ArrayList<>();

    private boolean                      pageable      = true;

    EsQueryReferences(String index){
        this.index = index;
    }

    boolean pageable() {
        return pageable;
    }

    List<EsIndexReference> reads() {
        return List.copyOf(reads);
    }

    void search(EsJsonValue value) {
        Map<String, EsJsonValue> fields = EsJsonAccess.fields(value, SEARCH_FIELDS);
        for (var entry : fields.entrySet()) {
            switch (entry.getKey()) {
                case "query", "post_filter" -> query(entry.getValue());
                case "aggs", "aggregations" -> aggregations(entry.getValue());
                case "knn" -> knn(entry.getValue());
                case "highlight" -> highlight(entry.getValue());
                case "sort" -> sort(entry.getValue());
                case "rescore" -> rescore(entry.getValue());
                case "collapse" -> {
                    var collapse = EsJsonAccess.fields(entry.getValue(), Set.of("field", "inner_hits", "max_concurrent_group_searches"));
                    if (collapse.containsKey("inner_hits")) {
                        innerHits(collapse.get("inner_hits"));
                    }
                }
                case "script_fields" -> {
                    for (EsJsonValue field : EsJsonAccess.object(entry.getValue()).values()) {
                        var options = EsJsonAccess.fields(field, Set.of("script", "ignore_failure"));
                        if (options.containsKey("script")) {
                            script(options.get("script"));
                        }
                    }
                }
                default -> {
                }
            }
        }
    }

    void query(EsJsonValue value) {
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::query);
            return;
        }
        Map<String, EsJsonValue> clauses = EsJsonAccess.object(value);
        if (clauses.size() != 1) {
            throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
        }
        var clause = clauses.entrySet().iterator().next();
        String type = clause.getKey();
        if (Set.of("script", "script_score", "function_score", "knn").contains(type)) {
            pageable = false;
        }
        if (Set.of("range", "query_string", "simple_query_string", "distance_feature").contains(type)) {
            // The PIT freezes index contents, not the evaluation time of a relative date query.
            if (EsSearchPagination.hasRelativeTime(clause.getValue())) {
                pageable = false;
            }
        }
        if (LEAF_QUERIES.contains(type)) {
            return;
        }
        switch (type) {
            case "bool" -> compound(clause.getValue(), Set.of("must", "filter", "should", "must_not", "minimum_should_match", "boost", "_name"), Set
                .of("must", "filter", "should", "must_not"));
            case "constant_score" -> compound(clause.getValue(), Set.of("filter", "boost", "_name"), Set.of("filter"));
            case "dis_max" -> compound(clause.getValue(), Set.of("queries", "tie_breaker", "boost", "_name"), Set.of("queries"));
            case "boosting" -> compound(clause.getValue(), Set.of("positive", "negative", "negative_boost", "boost", "_name"), Set.of("positive", "negative"));
            case "nested", "has_child", "has_parent" -> {
                var options = EsJsonAccess.fields(clause.getValue(), Set
                    .of("query", "path", "type", "parent_type", "score_mode", "score", "min_children", "max_children", "ignore_unmapped", "inner_hits", "boost", "_name"));
                if (options.containsKey("query")) {
                    query(options.get("query"));
                }
                if (options.containsKey("inner_hits")) {
                    innerHits(options.get("inner_hits"));
                }
            }
            case "terms" -> terms(clause.getValue());
            case "geo_shape", "shape" -> shape(clause.getValue());
            case "more_like_this" -> moreLikeThis(clause.getValue());
            case "script" -> {
                var options = EsJsonAccess.fields(clause.getValue(), Set.of("script", "boost", "_name"));
                if (options.containsKey("script")) {
                    script(options.get("script"));
                }
            }
            case "script_score" -> {
                var options = EsJsonAccess.fields(clause.getValue(), Set.of("query", "script", "min_score", "boost", "_name"));
                if (options.containsKey("query")) {
                    query(options.get("query"));
                }
                if (options.containsKey("script")) {
                    script(options.get("script"));
                }
            }
            case "function_score" -> functionScore(clause.getValue());
            case "knn" -> knn(clause.getValue());
            // Encoded/stored queries, percolation and plugin query types cannot expose their complete resource scope here.
            default -> throw new EsParseException(clause.getValue().range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
        }
    }

    private void compound(EsJsonValue value, Set<String> allowed, Set<String> children) {
        EsJsonAccess.fields(value, allowed).forEach((name, child) -> {
            if (children.contains(name)) {
                query(child);
            }
        });
    }

    private void terms(EsJsonValue value) {
        for (var field : EsJsonAccess.object(value).entrySet()) {
            if (field.getKey().equals("boost") || field.getKey().equals("_name")) {
                continue;
            }
            if (field.getValue() instanceof EsJsonArray) {
                continue;
            }
            var lookup = EsJsonAccess.fields(field.getValue(), Set.of("index", "id", "path", "routing"));
            if (!lookup.containsKey("index") || !lookup.containsKey("id") || !lookup.containsKey("path")) {
                throw new EsParseException(field.getValue().range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
            EsJsonValue target = lookup.get("index");
            reads.add(new EsIndexReference(EsJsonAccess.string(target), target.range()));
        }
    }

    private void shape(EsJsonValue value) {
        for (var field : EsJsonAccess.object(value).entrySet()) {
            if (Set.of("boost", "_name", "ignore_unmapped").contains(field.getKey())) {
                continue;
            }
            var options = EsJsonAccess.fields(field.getValue(), Set.of("shape", "indexed_shape", "relation", "strategy", "ignore_unmapped"));
            if (!options.containsKey("indexed_shape")) {
                continue;
            }
            EsJsonValue lookupValue = options.get("indexed_shape");
            var lookup = EsJsonAccess.fields(lookupValue, Set.of("index", "id", "path", "routing"));
            if (!lookup.containsKey("id")) {
                throw new EsParseException(lookupValue.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
            if (lookup.containsKey("index")) {
                EsJsonValue target = lookup.get("index");
                reads.add(new EsIndexReference(EsJsonAccess.string(target), target.range()));
            } else {
                // Elasticsearch's documented default for pre-indexed shapes.
                reads.add(new EsIndexReference("shapes", lookupValue.range()));
            }
        }
    }

    private void moreLikeThis(EsJsonValue value) {
        var options = EsJsonAccess.fields(value, Set
            .of("fields", "like", "unlike", "max_query_terms", "min_term_freq", "min_doc_freq", "max_doc_freq", "min_word_length", "max_word_length", "stop_words", "analyzer", "minimum_should_match", "fail_on_unsupported_field", "boost_terms", "include", "boost", "_name"));
        for (String name : List.of("like", "unlike")) {
            if (options.containsKey(name)) {
                similarDocuments(options.get(name));
            }
        }
    }

    private void similarDocuments(EsJsonValue value) {
        if (value instanceof EsJsonString) {
            return;
        }
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::similarDocuments);
            return;
        }
        var fields = EsJsonAccess.fields(value, Set.of("_index", "_id", "doc", "routing", "fields", "per_field_analyzer", "version", "version_type"));
        if (fields.containsKey("_index")) {
            EsJsonValue target = fields.get("_index");
            reads.add(new EsIndexReference(EsJsonAccess.string(target), target.range()));
        } else if (fields.containsKey("_id")) {
            reads.add(new EsIndexReference(index, value.range()));
        }
    }

    void knn(EsJsonValue value) {
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::knn);
            return;
        }
        var options = EsJsonAccess.fields(value, Set.of("field", "query_vector", "k", "num_candidates", "filter", "boost", "similarity", "inner_hits", "_name"));
        if (options.containsKey("filter")) {
            query(options.get("filter"));
        }
        if (options.containsKey("inner_hits")) {
            innerHits(options.get("inner_hits"));
        }
    }

    static void script(EsJsonValue value) {
        if (value instanceof EsJsonString) {
            return;
        }
        var options = EsJsonAccess.fields(value, Set.of("source", "lang", "params", "options"));
        if (!options.containsKey("source")) {
            throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
        }
        EsJsonAccess.string(options.get("source"));
    }

    private void functionScore(EsJsonValue value) {
        var options = EsJsonAccess.fields(value, Set
            .of("query", "functions", "script_score", "random_score", "field_value_factor", "gauss", "linear", "exp", "weight", "boost", "boost_mode", "score_mode", "max_boost", "min_score", "_name"));
        if (options.containsKey("query")) {
            query(options.get("query"));
        }
        if (options.containsKey("functions")) {
            if (!(options.get("functions") instanceof EsJsonArray functions)) {
                throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
            }
            for (EsJsonValue function : functions.values()) {
                var fields = EsJsonAccess.fields(function, Set.of("filter", "script_score", "random_score", "field_value_factor", "gauss", "linear", "exp", "weight"));
                if (fields.containsKey("filter")) {
                    query(fields.get("filter"));
                }
                if (fields.containsKey("script_score")) {
                    scoreScript(fields.get("script_score"));
                }
            }
        }
        if (options.containsKey("script_score")) {
            scoreScript(options.get("script_score"));
        }
    }

    private void scoreScript(EsJsonValue value) {
        var options = EsJsonAccess.fields(value, Set.of("script"));
        if (options.containsKey("script")) {
            script(options.get("script"));
        }
    }

    private void innerHits(EsJsonValue value) {
        pageable = false;
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::innerHits);
            return;
        }
        var options = EsJsonAccess.fields(value, Set
            .of("name", "from", "size", "sort", "_source", "fields", "docvalue_fields", "stored_fields", "highlight", "explain", "version", "seq_no_primary_term", "track_scores", "ignore_unmapped"));
        if (options.containsKey("sort")) {
            sort(options.get("sort"));
        }
        if (options.containsKey("highlight")) {
            highlight(options.get("highlight"));
        }
    }

    private void highlight(EsJsonValue value) {
        var options = EsJsonAccess.object(value);
        if (options.containsKey("highlight_query")) {
            query(options.get("highlight_query"));
        }
        if (options.containsKey("fields")) {
            for (EsJsonValue field : EsJsonAccess.object(options.get("fields")).values()) {
                var fields = EsJsonAccess.object(field);
                if (fields.containsKey("highlight_query")) {
                    query(fields.get("highlight_query"));
                }
            }
        }
    }

    private void sort(EsJsonValue value) {
        if (value instanceof EsJsonString string) {
            if (string.value().equals("_doc")) {
                pageable = false;
            }
            return;
        }
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::sort);
            return;
        }
        for (var entry : EsJsonAccess.object(value).entrySet()) {
            if (entry.getKey().equals("_doc")) {
                pageable = false;
            }
            if (!(entry.getValue() instanceof EsJsonObject)) {
                continue;
            }
            var options = EsJsonAccess.object(entry.getValue());
            if (entry.getKey().equals("_script") && options.containsKey("script")) {
                pageable = false;
                script(options.get("script"));
            }
            if (options.containsKey("nested")) {
                nestedSort(options.get("nested"));
            }
        }
    }

    private void nestedSort(EsJsonValue value) {
        var options = EsJsonAccess.fields(value, Set.of("path", "filter", "max_children", "nested"));
        if (options.containsKey("filter")) {
            query(options.get("filter"));
        }
        if (options.containsKey("nested")) {
            nestedSort(options.get("nested"));
        }
    }

    private void rescore(EsJsonValue value) {
        if (value instanceof EsJsonArray array) {
            array.values().forEach(this::rescore);
            return;
        }
        var options = EsJsonAccess.fields(value, Set.of("window_size", "query"));
        if (options.containsKey("query")) {
            var query = EsJsonAccess.fields(options.get("query"), Set.of("rescore_query", "query_weight", "rescore_query_weight", "score_mode"));
            if (query.containsKey("rescore_query")) {
                query(query.get("rescore_query"));
            }
        }
    }

    private void aggregations(EsJsonValue value) {
        for (EsJsonValue aggregation : EsJsonAccess.object(value).values()) {
            for (var entry : EsJsonAccess.object(aggregation).entrySet()) {
                String type = entry.getKey();
                EsJsonValue config = entry.getValue();
                if (type.equals("meta")) {
                    continue;
                }
                if (type.equals("aggs") || type.equals("aggregations")) {
                    aggregations(config);
                    continue;
                }
                if (!AGGREGATIONS.contains(type)) {
                    throw new EsParseException(config.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
                }
                if (type.equals("filter")) {
                    query(config);
                    continue;
                }
                if (type.equals("top_hits")) {
                    search(config);
                    continue;
                }
                var options = EsJsonAccess.object(config);
                if (options.containsKey("background_filter")) {
                    query(options.get("background_filter"));
                }
                if (options.containsKey("filters")) {
                    EsJsonValue filters = options.get("filters");
                    if (filters instanceof EsJsonArray array) {
                        array.values().forEach(this::query);
                    } else {
                        EsJsonAccess.object(filters).values().forEach(this::query);
                    }
                }
                if (options.containsKey("sort")) {
                    sort(options.get("sort"));
                }
                aggregationScripts(config);
            }
        }
    }

    private void aggregationScripts(EsJsonValue value) {
        var options = EsJsonAccess.object(value);
        if (options.containsKey("script")) {
            script(options.get("script"));
        }
        for (String name : List.of("value", "weight")) {
            if (options.get(name) instanceof EsJsonObject source) {
                var fields = EsJsonAccess.object(source);
                if (fields.containsKey("script")) {
                    script(fields.get("script"));
                }
            }
        }
        if (options.get("terms") instanceof EsJsonArray terms) {
            terms.values().forEach(this::aggregationScripts);
        }
        if (options.get("sources") instanceof EsJsonArray sources) {
            for (EsJsonValue source : sources.values()) {
                for (EsJsonValue named : EsJsonAccess.object(source).values()) {
                    EsJsonAccess.fields(named, Set.of("terms", "histogram", "date_histogram", "geotile_grid")).values().forEach(this::aggregationScripts);
                }
            }
        }
    }
}
