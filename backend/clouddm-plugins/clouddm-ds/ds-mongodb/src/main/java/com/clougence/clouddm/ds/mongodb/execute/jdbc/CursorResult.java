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

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.bson.Document;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;

public class CursorResult implements Iterable<Document> {

    private final Queue<Document> documents = new LinkedBlockingQueue<>();
    private Long                  id;

    private final MongoDatabase   database;
    private final ClientSession   clientSession;
    private final String          collectionName;

    public CursorResult(Document document, MongoDatabase database, ClientSession clientSession){
        this(document, database, clientSession, null);
    }

    public CursorResult(Document firstResult, MongoDatabase database, ClientSession clientSession, String collectionName){
        this.collectionName = collectionName;
        this.clientSession = clientSession;
        this.database = database;

        Document cursor = (Document) firstResult.get("cursor");
        this.id = cursor.getLong("id");
        List<Document> list = cursor.getList("firstBatch", Document.class);
        this.documents.addAll(list);
    }

    @Override
    public Iterator<Document> iterator() {
        return new FindResultIterator();
    }

    public class FindResultIterator implements Iterator<Document> {

        @Override
        public boolean hasNext() {
            if (documents.isEmpty()) {
                if (id == null || id == 0) {
                    return false;
                } else {
                    Document parse = Document.parse("{getMore:" + id + ",\"collection\":\"" + collectionName + "\",batchSize:100}");
                    Document document1 = database.runCommand(clientSession, parse);
                    Document cursor = (Document) document1.get("cursor");
                    id = cursor.getLong("id");
                    documents.addAll(cursor.getList("nextBatch", Document.class));
                }
            }
            return true;
        }

        @Override
        public Document next() {
            return documents.poll();
        }
    }
}
