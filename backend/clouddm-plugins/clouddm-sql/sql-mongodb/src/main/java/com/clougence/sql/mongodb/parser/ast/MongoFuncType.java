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
package com.clougence.sql.mongodb.parser.ast;

import lombok.Getter;

public enum MongoFuncType {

    // info
    BUILD_INFO("buildInfo", com.clougence.sql.mongodb.parser.ast.CommandType.BUILD_INFO, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    HELLO("hello", com.clougence.sql.mongodb.parser.ast.CommandType.HELLO, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    SERVER_STATUS("serverStatus", com.clougence.sql.mongodb.parser.ast.CommandType.SERVER_STATUS, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    SHOW_DATABASES("show databases", com.clougence.sql.mongodb.parser.ast.CommandType.SHOW_DATABASES, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    SHOW_COLLECTIONS("show collections", com.clougence.sql.mongodb.parser.ast.CommandType.SHOW_COLLECTIONS, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    GET_LOG_COMPONENTS("GetLogComponents", com.clougence.sql.mongodb.parser.ast.CommandType.GET_PARAMETER, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    HOST_INFO("hostInfo", com.clougence.sql.mongodb.parser.ast.CommandType.HOST_INFO, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    LIST_COLLECTIONS("listCollections", com.clougence.sql.mongodb.parser.ast.CommandType.LIST_COLLECTIONS, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    DROP_DATABASE("dropDatabase", com.clougence.sql.mongodb.parser.ast.CommandType.DROP_DATABASE, com.clougence.sql.mongodb.parser.ast.CmdKindType.DROP),
    CREATE_COLLECTION("createCollection", com.clougence.sql.mongodb.parser.ast.CommandType.CREATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.CREATE),
    CREATE_VIEW("createView", com.clougence.sql.mongodb.parser.ast.CommandType.CREATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.CREATE),
    FIND("find", com.clougence.sql.mongodb.parser.ast.CommandType.FIND, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    FIND_ONE("findOne", com.clougence.sql.mongodb.parser.ast.CommandType.FIND, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    DATA_SIZE("dataSize", com.clougence.sql.mongodb.parser.ast.CommandType.DATA_SIZE, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    FIND_ONE_AND_DELETE("findOneAndDelete", com.clougence.sql.mongodb.parser.ast.CommandType.FIND_AND_MODIFY, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    FIND_ONE_AND_UPDATE("findOneAndUpdate", com.clougence.sql.mongodb.parser.ast.CommandType.FIND_AND_MODIFY, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    FIND_ONE_AND_REPLACE("findOneAndReplace", com.clougence.sql.mongodb.parser.ast.CommandType.FIND_AND_MODIFY, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    INSERT("insert", com.clougence.sql.mongodb.parser.ast.CommandType.INSERT, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    INSERT_ONE("insertOne", com.clougence.sql.mongodb.parser.ast.CommandType.INSERT, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    INSERT_MANY("insertMany", com.clougence.sql.mongodb.parser.ast.CommandType.INSERT, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    DELETE_ONE("deleteOne", com.clougence.sql.mongodb.parser.ast.CommandType.DELETE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    DELETE_MANY("deleteMany", com.clougence.sql.mongodb.parser.ast.CommandType.DELETE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    UPDATE("update", com.clougence.sql.mongodb.parser.ast.CommandType.UPDATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    UPDATE_ONE("updateOne", com.clougence.sql.mongodb.parser.ast.CommandType.UPDATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    UPDATE_MANY("updateMany", com.clougence.sql.mongodb.parser.ast.CommandType.UPDATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    REPLACE_ONE("replaceOne", com.clougence.sql.mongodb.parser.ast.CommandType.UPDATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.WRITE),
    DROP("drop", com.clougence.sql.mongodb.parser.ast.CommandType.DROP, com.clougence.sql.mongodb.parser.ast.CmdKindType.DROP),
    USE("use", com.clougence.sql.mongodb.parser.ast.CommandType.USE, com.clougence.sql.mongodb.parser.ast.CmdKindType.OTHER),
    COUNT_DOCUMENTS("countDocuments", com.clougence.sql.mongodb.parser.ast.CommandType.AGGREGATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    COUNT("count", com.clougence.sql.mongodb.parser.ast.CommandType.COUNT, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    AGGREGATE("aggregate", com.clougence.sql.mongodb.parser.ast.CommandType.AGGREGATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    LATENCY_STATS("latencyStats", com.clougence.sql.mongodb.parser.ast.CommandType.AGGREGATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    CREATE_INDEX("createIndex", com.clougence.sql.mongodb.parser.ast.CommandType.CREATE_INDEXES, com.clougence.sql.mongodb.parser.ast.CmdKindType.CREATE),
    CREATE_INDEXES("createIndexes", com.clougence.sql.mongodb.parser.ast.CommandType.CREATE_INDEXES, com.clougence.sql.mongodb.parser.ast.CmdKindType.CREATE),
    DISTINCT("distinct", com.clougence.sql.mongodb.parser.ast.CommandType.DISTINCT, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    DROP_INDEXES("dropIndexes", com.clougence.sql.mongodb.parser.ast.CommandType.DROP_INDEXES, com.clougence.sql.mongodb.parser.ast.CmdKindType.DROP),
    DROP_INDEX("dropIndex", com.clougence.sql.mongodb.parser.ast.CommandType.DROP_INDEXES, com.clougence.sql.mongodb.parser.ast.CmdKindType.DROP),
    LIST_INDEXES("listIndexes", com.clougence.sql.mongodb.parser.ast.CommandType.LIST_INDEXES, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    ALTER_INDEX("alterIndex", com.clougence.sql.mongodb.parser.ast.CommandType.ALTER_INDEX, com.clougence.sql.mongodb.parser.ast.CmdKindType.MODIFY),
    RENAME_COLLECTION("renameCollection", com.clougence.sql.mongodb.parser.ast.CommandType.RENAME_COLLECTION, com.clougence.sql.mongodb.parser.ast.CmdKindType.MODIFY),
    VALIDATE("validate", com.clougence.sql.mongodb.parser.ast.CommandType.VALIDATE, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    DB_STATS("dbStats", com.clougence.sql.mongodb.parser.ast.CommandType.DB_STATS, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ),
    PROFILE("profile", com.clougence.sql.mongodb.parser.ast.CommandType.PROFILE, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    KILL_OP("killOp", com.clougence.sql.mongodb.parser.ast.CommandType.KILL_OP, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    RUN_COMMAND("runCommand", com.clougence.sql.mongodb.parser.ast.CommandType.RUN_COMMAND, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    ADMIN_COMMAND("adminCommand", com.clougence.sql.mongodb.parser.ast.CommandType.ADMIN_COMMAND, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    FSYNC_LOCK("fsyncLock", com.clougence.sql.mongodb.parser.ast.CommandType.FSYNC, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    FSYNC_UNLOCK("fsyncUnlock", com.clougence.sql.mongodb.parser.ast.CommandType.FSYNC_UNLOCK, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    CURRENT_OP("currentOp", com.clougence.sql.mongodb.parser.ast.CommandType.CURRENT_OP, com.clougence.sql.mongodb.parser.ast.CmdKindType.ADMIN),
    EXPLAIN("explain", com.clougence.sql.mongodb.parser.ast.CommandType.EXPLAIN, com.clougence.sql.mongodb.parser.ast.CmdKindType.READ);

    @Getter
    private final String                                           funcStr;
    @Getter
    private final com.clougence.sql.mongodb.parser.ast.CommandType commandType;
    @Getter
    private final com.clougence.sql.mongodb.parser.ast.CmdKindType kindType;

    MongoFuncType(String commandStr, CommandType commandType, CmdKindType kindType){
        this.funcStr = commandStr;
        this.commandType = commandType;
        this.kindType = kindType;
    }

    public static MongoFuncType valueOfCode(String code) {
        for (MongoFuncType tableType : MongoFuncType.values()) {
            if (tableType.funcStr.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported MongoDB func " + code);
    }
}
