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
package com.clougence.sql.mongodb.analysis.security;

import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.sql.mongodb.parser.ast.MongoFuncType;

public class MongoAnalysisHelper {

    public static RuleQueryType convert(MongoFuncType type) {

        switch (type) {
            case FIND:
            case AGGREGATE: {
                return RuleQueryType.SELECT;
            }
            case FIND_ONE:
            case COUNT:
            case DISTINCT:
            case COUNT_DOCUMENTS: {
                return RuleQueryType.READ;
            }
            case DATA_SIZE:
            case HELLO: {
                return RuleQueryType.PERFORMANCE;
            }

            case LIST_COLLECTIONS:
            case LIST_INDEXES:
            case SHOW_DATABASES:
            case SHOW_COLLECTIONS: {
                return RuleQueryType.UNKNOWN;
            }
            case VALIDATE: {
                return RuleQueryType.ADMIN_TABLE;
            }
            case CREATE_INDEX:
            case CREATE_INDEXES: {
                return RuleQueryType.ADD_INDEX;
            }
            case CREATE_VIEW: {
                return RuleQueryType.CREATE_VIEW;
            }
            case CREATE_COLLECTION: {
                return RuleQueryType.CREATE_TABLE;
            }
            case INSERT:
            case INSERT_ONE:
            case INSERT_MANY: {
                return RuleQueryType.INSERT;
            }
            case UPDATE:
            case UPDATE_MANY:
            case UPDATE_ONE:
            case REPLACE_ONE:
            case FIND_ONE_AND_REPLACE:
            case FIND_ONE_AND_UPDATE: {
                return RuleQueryType.UPDATE;
            }
            case FIND_ONE_AND_DELETE:
            case DELETE_ONE:
            case DELETE_MANY: {
                return RuleQueryType.DELETE;
            }
            case DROP: {
                return RuleQueryType.DROP_TABLE;
            }
            case DROP_DATABASE: {
                return RuleQueryType.DROP_SCHEMA;
            }
            case RENAME_COLLECTION: {
                return RuleQueryType.RENAME_TABLE;
            }
            case ALTER_INDEX: {
                return RuleQueryType.ALTER_INDEX;
            }
            case DROP_INDEXES:
            case DROP_INDEX: {
                return RuleQueryType.DROP_INDEX;
            }
            case EXPLAIN: {
                return RuleQueryType.PERFORMANCE;
            }
            case USE: {
                return RuleQueryType.SWITCH_SCHEMA;
            }
            case HOST_INFO:
            case FSYNC_LOCK:
            case CURRENT_OP:
            case KILL_OP:
            case SERVER_STATUS:
            case BUILD_INFO:
            case GET_LOG_COMPONENTS:
            case PROFILE:
            case FSYNC_UNLOCK:
            case DB_STATS:
            case LATENCY_STATS: {
                return RuleQueryType.ADMIN;
            }
        }
        return RuleQueryType.UNKNOWN;
    }
}
