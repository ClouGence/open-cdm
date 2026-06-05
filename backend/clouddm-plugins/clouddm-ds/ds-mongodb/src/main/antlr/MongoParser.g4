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

parser grammar MongoParser;
options { tokenVocab = MongoLexer; }
/* ----------------------------------------------------------------------------------- basic type */

/* 入口 */
root : command (SEMI+ command)* SEMI* EOF;

command:
    functionCommand
;

functionCommand:
     databaseFunction
    | collectionFunction
    | environmentFunction
    | showFunction
    | use
    ;

use:
    USE ID
    ;

databaseFunction:
    DB DOT DROP_DATABASE  LS_BRACKET RS_BRACKET  #dbDropDatabase
    | DB DOT CREATE_COLLECTION LS_BRACKET STRING (COMMA options=obj)? RS_BRACKET #dbCreateCollection
    | DB DOT CREATE_VIEW LS_BRACKET viewName=STRING COMMA sourceName=STRING COMMA pipeline=arr (COMMA options=obj)? RS_BRACKET #dbCreateView
    | DB DOT STATS LS_BRACKET (obj|NUMBER)? RS_BRACKET #dbStats
    | DB DOT SET_PROFILING_LEVEL LS_BRACKET NUMBER (COMMA options=obj)? RS_BRACKET #setProfilingLevel
    | DB DOT GET_COLLECTION_NAMES LS_BRACKET RS_BRACKET #getCollectionNames
    | DB DOT GET_COLLECTION_INFOS LS_BRACKET (filter=obj (COMMA options=obj)?)? RS_BRACKET #getCollectionInfos
    | DB DOT GET_LOG_COMPONENTS LS_BRACKET  RS_BRACKET #getLogComponents
    | DB DOT GET_PROFILING_STATUS LS_BRACKET  RS_BRACKET #getProfilingStatus
    | DB DOT HOST_INFO LS_BRACKET  RS_BRACKET #hostInfo
    | DB DOT ADMIN_COMMAND LS_BRACKET obj RS_BRACKET #adminCommand
    | DB DOT RUN_COMMAND LS_BRACKET obj RS_BRACKET #runCommand

    | DB DOT AGGREGATE LS_BRACKET pipline=arr (COMMA options=obj)? RS_BRACKET #dbAggregate
    | DB DOT CURRENT_OP LS_BRACKET options=obj? RS_BRACKET #currentOp
    | DB DOT KILL_OP LS_BRACKET NUMBER RS_BRACKET #killOp
    | DB DOT FSYNC_LOCK LS_BRACKET  RS_BRACKET #fsyncLock
    | DB DOT FSYNC_UNLOCK LS_BRACKET  RS_BRACKET #fsyncUnlock

    ;

showFunction:
    SHOW (DATABASES | DBS) #showDatabases
    | SHOW COLLECTIONS #showCollections
    ;

environmentFunction:
    DB DOT SERVER_BUILD_INFO LS_BRACKET RS_BRACKET #dbServerBuildInfo
    | DB DOT HELLO  LS_BRACKET RS_BRACKET          #dbHello
    | DB DOT SERVER_STATUS  LS_BRACKET RS_BRACKET  #dbServerStatus
    ;

collectionFunction:
    collectionName DOT collectionMethod
    ;

collectionMethod:
    insert
    | insertOne
    | insertMany
    | find
    | findOne
    | findOneAndDelete
    | findOneAndReplace
    | findOneAndUpdate
    | deleteOne
    | deleteMany
    | update
    | drop
    | aggregate
    | countDocuments
    | createIndex
    | createIndexes
    | distinct
    | dropIndex
    | dropIndexes
    | count
    | estimatedDocumentCount
    | getIndexes
    | hideIndex
    | latencyStats
    | renameCollection
    | replaceOne
    | validate
    | explain
    | dataSize

    ;

dataSize:
    DATA_SIZE LS_BRACKET RS_BRACKET
    ;

explain:
    EXPLAIN LS_BRACKET verbosity=STRING? RS_BRACKET DOT explainMethod
    ;

explainMethod:
    aggregate
    |find
    |count
    |distinct
    ;

validate:
    VALIDATE LS_BRACKET options=obj? RS_BRACKET
    ;

replaceOne:
    REPLACE_ONE LS_BRACKET filter=obj COMMA replacement=obj (COMMA options=obj)? RS_BRACKET
    ;

renameCollection:
    RENAME_COLLECTION LS_BRACKET STRING (COMMA dropTarget=(TRUE|FALSE))?  RS_BRACKET
    ;

latencyStats:
    LATENCY_STATS LS_BRACKET obj? RS_BRACKET
    ;


getIndexes:
    GET_INDEXES LS_BRACKET RS_BRACKET
    ;

hideIndex:
    (HIDE_INDEX|UNHIDE_INDEX) LS_BRACKET (partten=obj | name = STRING)RS_BRACKET
    ;

estimatedDocumentCount:
    ESTIMATED_DOCUMENT_COUNT LS_BRACKET option=obj? RS_BRACKET
    ;

count:
    COUNT LS_BRACKET (filter=obj (COMMA option=obj)?)? RS_BRACKET
    ;


dropIndex:
    DROP_INDEX LS_BRACKET (obj | STRING) RS_BRACKET
    ;

dropIndexes:
    DROP_INDEXES LS_BRACKET jsonString? RS_BRACKET
    ;

distinct:
    DISTINCT LS_BRACKET field=STRING (COMMA query=obj (COMMA option=obj)?)? RS_BRACKET
    ;

createIndex:
    CREATE_INDEX LS_BRACKET keys=obj (COMMA options=obj (COMMA  commitQuorum=(NUMBER | STRING))?)? RS_BRACKET
    ;

createIndexes:
    CREATE_INDEXES LS_BRACKET keys=arr (COMMA options=obj (COMMA  commitQuorum=(NUMBER | STRING))?)? RS_BRACKET
    ;

aggregate:
    AGGREGATE LS_BRACKET (arr (COMMA options=obj)?)? RS_BRACKET
    ;

countDocuments:
    COUNT_DOCUMENTS LS_BRACKET (filter=obj (COMMA option=obj)?)? RS_BRACKET
    ;

find:
    FIND LS_BRACKET (filter =jsonString (COMMA projection=jsonString (COMMA options=obj)?)?)? RS_BRACKET
    (DOT findConstarint)*
    ;

findConstarint:
     S_SKIP LS_BRACKET NUMBER RS_BRACKET #findSkipConstraint
    | LIMIT LS_BRACKET NUMBER RS_BRACKET #findLimitConstraint
    | SORT LS_BRACKET jsonString RS_BRACKET #findSortConstraint
    | S_COMMENT LS_BRACKET jsonString RS_BRACKET #findCommentConstraint
    | MAX_TIME_MS LS_BRACKET NUMBER RS_BRACKET #findMaxTimeMSConstriant
    | COLLATION LS_BRACKET jsonString RS_BRACKET #findCollationConstraint
    | ALLOW_DISK_USE LS_BRACKET bool= (TRUE|FALSE) RS_BRACKET #findAllowDiskUseConstraint
    | MAX LS_BRACKET jsonString RS_BRACKET #findMaxConstraint
    | MIN LS_BRACKET jsonString RS_BRACKET #findMinConstraint
    | RETURN_KEY LS_BRACKET bool=(TRUE|FALSE) RS_BRACKET #findReturnKeyConstraint
    | SHOW_RECORD_ID LS_BRACKET bool=(TRUE|FALSE) RS_BRACKET #findShowRecordIdConstraint
    | LET LS_BRACKET jsonString RS_BRACKET #findLetConstraint
    | EXPLAIN LS_BRACKET verbosity=STRING? RS_BRACKET #explainConstraint
    ;

findOne:
    FIND_ONE LS_BRACKET (filter =jsonString (COMMA projection=jsonString (COMMA options=obj)?)?)? RS_BRACKET
    ;

findOneAndDelete:
    FIND_ONE_AND_DELETE LS_BRACKET (filter=obj (COMMA options=obj)?)? RS_BRACKET
    ;

findOneAndReplace:
    FIND_ONE_AND_REPLACE LS_BRACKET filter=obj (COMMA replace=obj (COMMA options=obj)?)? RS_BRACKET
    ;

findOneAndUpdate:
    FIND_ONE_AND_UPDATE LS_BRACKET filter=obj COMMA (objUpdate=obj|arrUpdate=arr) (COMMA options=obj)? RS_BRACKET
    ;

insert:
    INSERT LS_BRACKET (document=obj|documents=arr) (COMMA option = obj)? RS_BRACKET
    ;

insertOne:
    INSERT_ONE LS_BRACKET document=obj (COMMA option = obj)? RS_BRACKET
    ;

insertMany:
    INSERT_MANY LS_BRACKET documents=arr (COMMA option = obj)? RS_BRACKET
    ;

deleteOne:
    DELETE_ONE LS_BRACKET filter=obj? (COMMA option = obj)? RS_BRACKET
    ;

deleteMany:
    DELETE_MANY LS_BRACKET filter=obj? (COMMA option = obj)? RS_BRACKET
    ;

update:
    (UPDATE_ONE | UPDATE_MANY | UPDATE) LS_BRACKET filter=obj (COMMA (objUpdate = obj|arrUpdate=arr)) (COMMA option = obj)? RS_BRACKET
    ;

drop:
    DROP LS_BRACKET option = obj? RS_BRACKET
    ;

collectionName:
    DB DOT ID
    | DB DOT GET_COLLECTION LS_BRACKET STRING RS_BRACKET
    | DB '[' STRING ']'
    ;


// json
jsonString:
    value
    ;


obj
    : '{' pair (',' pair)* '}'
    | '{' '}'
    ;

pair
    : key ':' value
    ;

key:
    (ID | STRING | keyWordId)
    ;

arr
    : LM_BRACKET value (',' value)* ']'
    | '[' ']'
    ;

value
    :
     obj
    | STRING
    | NUMBER
    | arr
    | TRUE
    | FALSE
    | NULL
    | ID LS_BRACKET (STRING | NUMBER) RS_BRACKET
    | NEW ID LS_BRACKET (value (COMMA value)*)? RS_BRACKET
    ;


keyWordId:
    MAX_TIME_MS
    | S_COMMENT
    | LET
    | COLLATION
    | S_SKIP
    | LIMIT
    | SORT
    | MAX
    | MIN
    | ALLOW_DISK_USE
    | SHOW_RECORD_ID
    | RETURN_KEY
    | COUNT
    | FIND
    | LATENCY_STATS
    | HELLO
    | SERVER_STATUS
    | SERVER_BUILD_INFO
    | GET_INDEXES
    | HIDE_INDEX
    | UNHIDE_INDEX
    | ESTIMATED_DOCUMENT_COUNT
    | COUNT_DOCUMENTS
    | CREATE_INDEX
    | CREATE_INDEXES
    | DISTINCT
    | DROP_INDEX
    | DROP_INDEXES
    | AGGREGATE
    | FIND_ONE
    | FIND_ONE_AND_DELETE
    | FIND_ONE_AND_REPLACE
    | FIND_ONE_AND_UPDATE
    | INSERT
    | INSERT_ONE
    | INSERT_MANY
    | DELETE_ONE
    | DELETE_MANY
    | UPDATE
    | UPDATE_ONE
    | UPDATE_MANY
    | DROP
    | RENAME_COLLECTION
    | REPLACE_ONE
    | VALIDATE
    | HOST_INFO
    | KILL_OP
    | CURRENT_OP
    | DROP_DATABASE
    | EXPLAIN
    ;




