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
package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.sql.mongodb.parser.ast.MongoFilterSubquery;
import com.clougence.sql.mongodb.parser.ast.MongoFilterSubqueryHost;
import com.clougence.sql.mongodb.parser.ast.commands.collection.FindFunc;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

final class MongoFilterSubqueryResolver {

    private static final JsonWriterSettings EXTENDED_JSON                = JsonWriterSettings.builder().outputMode(JsonMode.EXTENDED).build();
    /** Fallback when AdapterRequest has no maxRows (same as onlineMaxRecordCount default). */
    private static final int                DEFAULT_MAX_SUBQUERY_RESULTS = 1000;
    /** Fallback when AdapterRequest has no timeoutSec (same as onlineMaxQueryTimeoutSec default). */
    private static final int                DEFAULT_TIMEOUT_SEC          = 30;

    private MongoFilterSubqueryResolver(){
    }

    static void resolve(MongoClient client, String database, MongoFilterSubqueryHost host, AdapterRequest request, MongoSessionProvider sessionProvider) {
        if (!host.hasFilterSubqueries()) {
            return;
        }
        int maxResults = resolveMaxResults(request);
        int timeoutMs = resolveTimeoutMs(request);

        String filter = host.getFilter();
        MongoDatabase mongoDatabase = client.getDatabase(database);
        try (ClientSession session = sessionProvider.startSession()) {
            List<MongoFilterSubquery> subqueries = host.getFilterSubqueries();
            for (int i = 0; i < subqueries.size(); i++) {
                String placeholder = MongoFilterSubquery.placeholder(i);
                String jsonArray = evaluateSubquery(mongoDatabase, session, subqueries.get(i), maxResults, timeoutMs);
                filter = filter.replace(placeholder, jsonArray);
            }
        }
        host.setFilter(filter);
    }

    private static String evaluateSubquery(MongoDatabase database, ClientSession session, MongoFilterSubquery subquery, int maxResults, int timeoutMs) {
        FindFunc findFunc = subquery.toFindFunc();
        findFunc.setMaxTimeMS(String.valueOf(timeoutMs));
        findFunc.setLimit(String.valueOf(resolveFetchLimit(subquery.getLimit(), maxResults)));

        Document result = MongoUtils.runCommand(database, session, Document.parse(findFunc.toBson()));
        List<Object> mapped = new ArrayList<>();
        try (CursorResult cursorResult = new CursorResult(result, database, session)) {
            for (Document document : cursorResult) {
                Object value = extractMappedValue(document, subquery.getMapField());
                // Skip missing mapped fields: $in:[null] also matches docs without that field.
                if (value == null) {
                    continue;
                }
                if (mapped.size() >= maxResults) {
                    throw new UnsupportedOperationException("Filter subquery result exceeds max of " + maxResults + " values");
                }
                mapped.add(value);
            }
        }
        return toJsonArray(mapped);
    }

    private static int resolveMaxResults(AdapterRequest request) {
        if (request != null && request.getMaxRows() > 0) {
            return (int) Math.min(request.getMaxRows(), Integer.MAX_VALUE);
        }
        return DEFAULT_MAX_SUBQUERY_RESULTS;
    }

    private static int resolveTimeoutMs(AdapterRequest request) {
        if (request != null && request.getTimeoutSec() > 0) {
            return request.getTimeoutSec() * 1000;
        }
        return DEFAULT_TIMEOUT_SEC * 1000;
    }

    /** Fetch at most maxResults+1 docs so an unbounded nested find can still be detected and rejected. */
    private static int resolveFetchLimit(String userLimit, int maxResults) {
        int fetchLimit = maxResults + 1;
        if (userLimit == null || userLimit.isEmpty()) {
            return fetchLimit;
        }
        int parsed = Integer.parseInt(userLimit);
        if (parsed < fetchLimit) {
            return parsed;
        }
        return fetchLimit;
    }

    private static Object extractMappedValue(Document document, String mapField) {
        if (mapField == null || mapField.isEmpty()) {
            return document;
        }
        Object current = document;
        String[] parts = mapField.split("\\.");
        for (String part : parts) {
            if (!(current instanceof Document doc)) {
                return null;
            }
            current = doc.get(part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private static String toJsonArray(List<Object> values) {
        // Wrap as {"v":[...]} then strip the outer object so Extended JSON values stay parseable.
        String wrapped = new Document("v", values).toJson(EXTENDED_JSON);
        return wrapped.substring(wrapped.indexOf('['), wrapped.lastIndexOf(']') + 1);
    }
}
