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

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.es.antlr.EsParser;
import com.clougence.sql.es.antlr.EsParserBaseVisitor;

final class EsSplitVisitor extends EsParserBaseVisitor<SplitQueryType> {
    @Override
    public SplitQueryType visitRequest(EsParser.RequestContext context) {
        String method = context.header().METHOD().getText();
        List<EsPathSegment> path = EsAstBuilder.segments(context.header().target().path());
        if (path.isEmpty() || path.get(0).value().startsWith("_")) {
            return SplitQueryType.UNKNOWN;
        }
        boolean read = method.equals("GET") || method.equals("POST");
        if (path.size() == 1) {
            return switch (method) {
                case "PUT" -> SplitQueryType.CREATE_TABLE;
                case "DELETE" -> SplitQueryType.DROP_TABLE;
                case "GET", "HEAD" -> SplitQueryType.METADATA;
                default -> SplitQueryType.UNKNOWN;
            };
        }
        String operation = path.get(1).value();
        if (path.size() == 2) {
            if (read && Set.of("_search", "_count", "_knn_search").contains(operation)) {
                return SplitQueryType.SELECT;
            }
            if (method.equals("POST")) {
                return switch (operation) {
                    case "_doc" -> SplitQueryType.INSERT;
                    case "_update_by_query" -> SplitQueryType.UPDATE;
                    case "_delete_by_query" -> SplitQueryType.DELETE;
                    default -> SplitQueryType.UNKNOWN;
                };
            }
            if (Set.of("_mapping", "_settings").contains(operation)) {
                if (method.equals("GET")) {
                    return SplitQueryType.METADATA;
                }
                if (method.equals("PUT")) {
                    return SplitQueryType.ALTER_TABLE;
                }
            }
        }
        if (path.size() == 3) {
            if (!operation.startsWith("_") && read && Set.of("_search", "_count").contains(path.get(2).value())) {
                return SplitQueryType.SELECT;
            }
            if (operation.equals("_doc")) {
                return switch (method) {
                    case "GET", "HEAD" -> SplitQueryType.SELECT;
                    case "PUT", "POST" -> SplitQueryType.INSERT;
                    case "DELETE" -> SplitQueryType.DELETE;
                    default -> SplitQueryType.UNKNOWN;
                };
            }
            if (operation.equals("_create") && (method.equals("PUT") || method.equals("POST"))) {
                return SplitQueryType.INSERT;
            }
            if (operation.equals("_update") && method.equals("POST")) {
                return SplitQueryType.UPDATE;
            }
            if (operation.equals("_source") && (method.equals("GET") || method.equals("HEAD"))) {
                return SplitQueryType.SELECT;
            }
        }
        // A parsed API is not automatically executable or authorized, especially for unknown POST routes.
        return SplitQueryType.UNKNOWN;
    }
}
