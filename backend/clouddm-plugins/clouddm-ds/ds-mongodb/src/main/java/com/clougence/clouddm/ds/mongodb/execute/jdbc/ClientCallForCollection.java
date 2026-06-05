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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;

import org.bson.Document;

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.DataSizeFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.RenameCollectionFunc;
import com.clougence.utils.future.CgFuture;
import com.mongodb.MongoNamespace;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.RenameCollectionOptions;

public class ClientCallForCollection extends MongoUtils {

    public static CgFuture<?> findFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, CollectionFunc func, AdapterRequest request, AdapterReceive receive,
                                       String database) throws SQLException, IOException {
        MongoDatabase database1 = client.getDatabase(database);
        Document result = database1.runCommand(session, Document.parse(func.toBson()));

        MongoResultBuffer buffer = new MongoResultBuffer();
        Iterator<Document> iterator = new CursorResult(result, database1, session, func.getCollectionName()).iterator();
        return handleResult(sync, request, receive, buffer, iterator);
    }

    public static CgFuture<?> renameCollectionFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, RenameCollectionFunc func, AdapterRequest request,
                                                   AdapterReceive receive, String database) throws SQLException {
        RenameCollectionOptions renameCollectionOptions;
        if (func.getDropTarget() != null) {
            renameCollectionOptions = new RenameCollectionOptions().dropTarget(func.getDropTarget());
        } else {
            renameCollectionOptions = new RenameCollectionOptions();
        }
        client.getDatabase(database).getCollection(func.getCollectionName()).renameCollection(session, new MongoNamespace(database, func.getTo()), renameCollectionOptions);

        receive.responseResult(request, MongoUtils.singleResult(request, MongoUtils.RESULT_COLUMN, "ok"));
        return completed(sync);
    }

    public static CgFuture<?> dataSizeFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, DataSizeFunc func, AdapterRequest request, AdapterReceive receive,
                                           String database) throws SQLException, IOException {
        String command = func.toBson(database);
        Document result = client.getDatabase(database).runCommand(session, Document.parse(command));

        return handleResult(sync, request, receive, new MongoResultBuffer(), Collections.singletonList(result).iterator());
    }
}
