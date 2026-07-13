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

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import org.bson.Document;

import com.mongodb.MongoNamespace;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CursorResult implements Iterable<Document>, AutoCloseable {

    private final Queue<Document> documents = new LinkedBlockingQueue<>();
    private Long                  id;

    private final MongoDatabase   database;
    private final ClientSession   clientSession;
    private final String          collectionName;
    private boolean               closed;

    public CursorResult(Document firstResult, MongoDatabase database, ClientSession clientSession){
        this.database = database;
        this.clientSession = clientSession;
        this.closed = false;

        Document cursor = (Document) firstResult.get("cursor");
        this.collectionName = new MongoNamespace(cursor.getString("ns")).getCollectionName();
        this.id = cursor.getLong("id");
        List<Document> list = cursor.getList("firstBatch", Document.class);
        this.documents.addAll(list);
    }

    @Override
    public Iterator<Document> iterator() {
        return new FindResultIterator();
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;

        Long cursorId = this.id;
        this.id = 0L;
        this.documents.clear();
        if (cursorId == null || cursorId == 0) {
            return;
        }

        Document command = new Document("killCursors", this.collectionName).append("cursors", Collections.singletonList(cursorId));
        try {
            MongoUtils.runCommand(this.database, this.clientSession, command);
        } catch (RuntimeException e) {
            String msg = "kill MongoDB cursor failed, but ignore, database=" + this.database.getName() + ", collection=" + this.collectionName + ", cursorId=" + cursorId;
            log.error(msg, e);
        }
    }

    public class FindResultIterator implements Iterator<Document> {

        @Override
        public boolean hasNext() {
            if (closed) {
                return false;
            }
            while (documents.isEmpty()) {
                if (id == null || id == 0) {
                    break;
                }

                Document command = new Document("getMore", id).append("collection", collectionName).append("batchSize", 100);
                Document result;
                if (clientSession == null) {
                    result = database.runCommand(command);
                } else {
                    result = database.runCommand(clientSession, command);
                }

                Document cursor = (Document) result.get("cursor");
                id = cursor.getLong("id");
                documents.addAll(cursor.getList("nextBatch", Document.class));
            }
            return !documents.isEmpty();
        }

        @Override
        public Document next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return documents.remove();
        }
    }
}
