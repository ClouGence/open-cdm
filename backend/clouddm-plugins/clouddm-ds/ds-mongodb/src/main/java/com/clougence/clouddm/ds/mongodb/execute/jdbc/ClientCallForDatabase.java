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

import org.bson.Document;

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ClientCallForDatabase extends MongoUtils {

    public static CgFuture<?> listCollections(CgFuture<Object> sync, MongoClient client, ClientSession session, String command, AdapterRequest request, AdapterReceive receive,
                                              String database) throws SQLException, IOException {
        MongoDatabase database1 = client.getDatabase(database);
        ClientSession clientSession = client.startSession();
        Document result = database1.runCommand(session, Document.parse(command));

        return handleResult(sync, request, receive, new MongoResultBuffer(), new CursorResult(result, database1, clientSession).iterator());
    }
}
