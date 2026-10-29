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

/** Eligibility for internal PIT pagination; unsupported shapes retain native execution. */
public final class EsSearchPagination {
    public static final int          PAGE_SIZE     = 1000;
    private static final Pattern     RELATIVE_TIME = Pattern.compile("\\bnow\\b");
    private static final Set<String> PARAMETERS    = Set
        .of("size", "q", "df", "analyzer", "analyze_wildcard", "default_operator", "lenient", "_source", "_source_includes", "_source_excludes", "routing", "preference", "version", "seq_no_primary_term", "track_total_hits", "track_scores", "pretty", "human");
    private static final Set<String> FIELDS        = Set
        .of("query", "post_filter", "_source", "sort", "size", "min_score", "track_total_hits", "track_scores", "version", "seq_no_primary_term");

    private EsSearchPagination(){
    }

    public static boolean supports(EsRequest request) {
        if (request.segments().size() != 2 || !request.segments().get(1).value().equals("_search")) {
            return false;
        }
        for (EsQueryParameter parameter : request.parameters()) {
            if ((parameter.name().equals("size") && parameter.value().equals("-1")) || !PARAMETERS.contains(parameter.name())
                || (parameter.name().equals("q") && RELATIVE_TIME.matcher(parameter.value()).find())) {
                return false;
            }
        }
        if (request.body() == null) {
            return true;
        }
        for (EsJsonMember member : request.body().members()) {
            if (!FIELDS.contains(member.name().value()) || hasNamedQuery(member.value())) {
                return false;
            }
        }
        EsQueryReferences references = new EsQueryReferences(request.segments().get(0).value());
        references.search(request.body());
        // Lookup documents are outside the PIT and can change between pages, including same-index lookups.
        return references.pageable() && references.reads().isEmpty();
    }

    private static boolean hasNamedQuery(EsJsonValue value) {
        if (value instanceof EsJsonObject object) {
            for (EsJsonMember member : object.members()) {
                if (member.name().value().equals("_name") || hasNamedQuery(member.value())) {
                    return true;
                }
            }
        } else if (value instanceof EsJsonArray array) {
            for (EsJsonValue item : array.values()) {
                if (hasNamedQuery(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean hasRelativeTime(EsJsonValue value) {
        if (value instanceof EsJsonString string) {
            return RELATIVE_TIME.matcher(string.value()).find();
        }
        if (value instanceof EsJsonObject object) {
            for (EsJsonMember member : object.members()) {
                if (hasRelativeTime(member.value())) {
                    return true;
                }
            }
        } else if (value instanceof EsJsonArray array) {
            for (EsJsonValue item : array.values()) {
                if (hasRelativeTime(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
