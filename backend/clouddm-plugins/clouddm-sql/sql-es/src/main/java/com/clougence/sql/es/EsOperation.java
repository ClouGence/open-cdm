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

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EsOperation {
    SEARCH(BehaviorAction.READ, SplitQueryType.SELECT),
    COUNT(BehaviorAction.READ, SplitQueryType.SELECT),
    KNN_SEARCH(BehaviorAction.READ, SplitQueryType.SELECT),
    READ_DOCUMENT(BehaviorAction.READ, SplitQueryType.SELECT),
    READ_INDEX(BehaviorAction.READ, SplitQueryType.METADATA),
    INSERT_DOCUMENT(BehaviorAction.INSERT, SplitQueryType.INSERT),
    REPLACE_DOCUMENT(BehaviorAction.REPLACE, SplitQueryType.INSERT),
    UPDATE_DOCUMENT(BehaviorAction.UPDATE, SplitQueryType.UPDATE),
    DELETE_DOCUMENT(BehaviorAction.DELETE, SplitQueryType.DELETE),
    UPDATE_BY_QUERY(BehaviorAction.UPDATE, SplitQueryType.UPDATE),
    DELETE_BY_QUERY(BehaviorAction.DELETE, SplitQueryType.DELETE),
    CREATE_INDEX(BehaviorAction.CREATE, SplitQueryType.CREATE_TABLE),
    DROP_INDEX(BehaviorAction.DROP, SplitQueryType.DROP_TABLE),
    PUT_MAPPING(BehaviorAction.ALTER, SplitQueryType.ALTER_TABLE),
    PUT_SETTINGS(BehaviorAction.ALTER, SplitQueryType.ALTER_TABLE);

    private final BehaviorAction action;
    private final SplitQueryType queryType;

    static EsOperation resolve(EsRequest request) {
        EsOperation operation = find(request.method(), request.segments());
        if (operation == null) {
            throw new EsParseException(request.range(), EsSqlI18nKeys.ES_API_OUT_OF_SCOPE);
        }
        return operation;
    }

    static EsOperation find(String method, List<EsPathSegment> path) {
        if (path.isEmpty() || path.get(0).value().startsWith("_")) {
            return null;
        }
        EsOperation operation = null;
        if (path.size() == 1) {
            operation = switch (method) {
                case "GET", "HEAD" -> READ_INDEX;
                case "PUT" -> CREATE_INDEX;
                case "DELETE" -> DROP_INDEX;
                default -> null;
            };
        } else if (path.size() == 2) {
            String endpoint = path.get(1).value();
            if (method.equals("GET") || method.equals("POST")) {
                operation = switch (endpoint) {
                    case "_search" -> SEARCH;
                    case "_count" -> COUNT;
                    case "_knn_search" -> KNN_SEARCH;
                    default -> null;
                };
            }
            if (method.equals("POST")) {
                operation = switch (endpoint) {
                    case "_doc" -> INSERT_DOCUMENT;
                    case "_update_by_query" -> UPDATE_BY_QUERY;
                    case "_delete_by_query" -> DELETE_BY_QUERY;
                    default -> operation;
                };
            }
            if (method.equals("GET") && (endpoint.equals("_mapping") || endpoint.equals("_settings") || endpoint.equals("_alias"))) {
                operation = READ_INDEX;
            }
            if (method.equals("PUT")) {
                operation = switch (endpoint) {
                    case "_mapping" -> PUT_MAPPING;
                    case "_settings" -> PUT_SETTINGS;
                    default -> operation;
                };
            }
        } else if (path.size() == 3) {
            String endpoint = path.get(1).value();
            if (endpoint.equals("_doc")) {
                operation = switch (method) {
                    case "GET", "HEAD" -> READ_DOCUMENT;
                    case "PUT", "POST" -> REPLACE_DOCUMENT;
                    case "DELETE" -> DELETE_DOCUMENT;
                    default -> null;
                };
            } else if (endpoint.equals("_create") && (method.equals("POST") || method.equals("PUT"))) {
                operation = INSERT_DOCUMENT;
            } else if (endpoint.equals("_update") && method.equals("POST")) {
                operation = UPDATE_DOCUMENT;
            } else if (endpoint.equals("_source") && (method.equals("GET") || method.equals("HEAD"))) {
                operation = READ_DOCUMENT;
            } else if (!endpoint.startsWith("_") && (method.equals("GET") || method.equals("POST"))) {
                operation = switch (path.get(2).value()) {
                    case "_search" -> SEARCH;
                    case "_count" -> COUNT;
                    default -> null;
                };
            }
        }
        return operation;
    }
}
