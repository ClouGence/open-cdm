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

import com.clougence.sql.mongodb.parser.ast.MongoFilterSubquery;
import com.clougence.sql.mongodb.parser.ast.commands.collection.DeleteManyFunc;
import com.clougence.sql.mongodb.parser.ast.commands.collection.FindFunc;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

final class MongoFilterSubqueryResolver {

    private static final JsonWriterSettings EXTENDED_JSON = JsonWriterSettings.builder().outputMode(JsonMode.EXTENDED).build();

    private MongoFilterSubqueryResolver(){
    }

    static void resolve(MongoClient client, String database, DeleteManyFunc func, MongoSessionProvider sessionProvider) {
        if (func == null || !func.hasFilterSubqueries()) {
            return;
        }
        String filter = func.getFilter();
        if (filter == null) {
            return;
        }
        MongoDatabase mongoDatabase = client.getDatabase(database);
        try (ClientSession session = sessionProvider.startSession()) {
            List<MongoFilterSubquery> subqueries = func.getFilterSubqueries();
            for (int i = 0; i < subqueries.size(); i++) {
                String placeholder = MongoFilterSubquery.placeholder(i);
                String jsonArray = evaluateSubquery(mongoDatabase, session, subqueries.get(i));
                filter = filter.replace(placeholder, jsonArray);
            }
        }
        func.setFilter(filter);
    }

    private static String evaluateSubquery(MongoDatabase database, ClientSession session, MongoFilterSubquery subquery) {
        FindFunc findFunc = subquery.toFindFunc();
        Document result = MongoUtils.runCommand(database, session, Document.parse(findFunc.toBson()));
        List<Object> mapped = new ArrayList<>();
        try (CursorResult cursorResult = new CursorResult(result, database, session)) {
            for (Document document : cursorResult) {
                mapped.add(extractMappedValue(document, subquery.getMapField()));
            }
        }
        return toJsonArray(mapped);
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
        // Wrap as {"v":[...]} then strip the outer object so Document.parse accepts Extended JSON values.
        String wrapped = new Document("v", values).toJson(EXTENDED_JSON);
        int start = wrapped.indexOf('[');
        int end = wrapped.lastIndexOf(']');
        if (start < 0 || end < start) {
            return "[]";
        }
        return wrapped.substring(start, end + 1);
    }
}
