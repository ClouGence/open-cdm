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

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.DataSizeFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.RenameCollectionFunc;
import com.clougence.utils.future.CgFuture;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MongoDistributeCall {

    public static CgFuture<?> execFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, AbstractMongoFunc func, AdapterRequest request, AdapterReceive receive,
                                       String database) throws SQLException, IOException {
        switch (func.getFuncType().getCommandType()) {
            /* ------------------------------------------------------------------------------------------------ String Commands */
            case SHOW_DATABASES:
                return ClientCallForShow.showDatabasesFunc(sync, client, session, request, receive);
            case SHOW_COLLECTIONS:
                return ClientCallForShow.showCollectionsFunc(sync, client, session, request, receive, database);
            case DATA_SIZE:
                return ClientCallForCollection.dataSizeFunc(sync, client, session, (DataSizeFunc) func, request, receive, database);
            case FIND:
            case AGGREGATE:
            case LIST_INDEXES:
                return ClientCallForCollection.findFunc(sync, client, session, (CollectionFunc) func, request, receive, database);
            case LIST_COLLECTIONS:
                return ClientCallForDatabase.listCollections(sync, client, session, func.toBson(), request, receive, database);
            case RENAME_COLLECTION:
                return ClientCallForCollection.renameCollectionFunc(sync, client, session, (RenameCollectionFunc) func, request, receive, database);
            case INSERT:
            case DELETE:
            case UPDATE:
            case DROP:
            case CREATE_INDEXES:
            case DISTINCT:
            case DROP_INDEXES:
            case COUNT:
            case FIND_AND_MODIFY:
            case ALTER_INDEX:
            case VALIDATE:
            case DB_STATS:
            case PROFILE:
            case RUN_COMMAND:
            case HELLO:
            case SERVER_STATUS:
            case CREATE:
            case HOST_INFO:
            case DROP_DATABASE:
            case EXPLAIN:
                return ClientCallForCommand.runCommand(sync, client, session, func.toBson(), request, receive, database);
            // adminCommand
            case ADMIN_COMMAND:
            case BUILD_INFO:
            case CURRENT_OP:
            case FSYNC:
            case FSYNC_UNLOCK:
            case GET_PARAMETER:
            case KILL_OP:
                return ClientCallForCommand.runCommand(sync, client, session, func.toBson(), request, receive, "admin");
            default:
                throw new UnsupportedOperationException();
        }
    }
}
