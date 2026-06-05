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

parser grammar TiDBParser;

import MySqlParser;

options {
    tokenVocab=TiDBLexer;
}


ddlStatement
    : createDatabase | createEvent | createIndex
    | createLogfileGroup | createProcedure | createFunction
    | createServer | createTable | createTablespaceInnodb
    | createTablespaceNdb | createTrigger | createView
    | alterDatabase | alterEvent | alterFunction
    | alterInstance | alterLogfileGroup | alterProcedure
    | alterServer | alterTable | alterTablespace | alterView
    | dropDatabase | dropEvent | dropIndex
    | dropLogfileGroup | dropProcedure | dropFunction
    | dropServer | dropTable | dropTablespace
    | dropTrigger | dropView
    | renameTable | truncateTable

    // tidb
    |  createSequence | dropSequence
    ;


tableOption
    : ENGINE '='? engineName?                                       #tableOptionEngine
    | AUTO_INCREMENT '='? decimalLiteral                            #tableOptionAutoIncrement
    | AVG_ROW_LENGTH '='? decimalLiteral                            #tableOptionAverage
    | DEFAULT? (CHARACTER SET | CHARSET) '='? (charsetName|DEFAULT) #tableOptionCharset
    | (CHECKSUM | PAGE_CHECKSUM) '='? boolValue=('0' | '1')         #tableOptionChecksum
    | DEFAULT? COLLATE '='? collationName                           #tableOptionCollate
    | COMMENT '='? STRING_LITERAL                                   #tableOptionComment
    | COMPRESSION '='? (STRING_LITERAL | ID)                        #tableOptionCompression
    | CONNECTION '='? STRING_LITERAL                                #tableOptionConnection
    | DATA DIRECTORY '='? STRING_LITERAL                            #tableOptionDataDirectory
    | DELAY_KEY_WRITE '='? boolValue=('0' | '1')                    #tableOptionDelay
    | ENCRYPTION '='? STRING_LITERAL                                #tableOptionEncryption
    | INDEX DIRECTORY '='? STRING_LITERAL                           #tableOptionIndexDirectory
    | INSERT_METHOD '='? insertMethod=(NO | FIRST | LAST)           #tableOptionInsertMethod
    | KEY_BLOCK_SIZE '='? fileSizeLiteral                           #tableOptionKeyBlockSize
    | MAX_ROWS '='? decimalLiteral                                  #tableOptionMaxRows
    | MIN_ROWS '='? decimalLiteral                                  #tableOptionMinRows
    | PACK_KEYS '='? extBoolValue=('0' | '1' | DEFAULT)             #tableOptionPackKeys
    | PASSWORD '='? STRING_LITERAL                                  #tableOptionPassword
    | ROW_FORMAT '='?
        rowFormat=(
          DEFAULT | DYNAMIC | FIXED | COMPRESSED
          | REDUNDANT | COMPACT | ID
        )                                                           #tableOptionRowFormat
    | STATS_AUTO_RECALC '='? extBoolValue=(DEFAULT | '0' | '1')     #tableOptionRecalculation
    | STATS_PERSISTENT '='? extBoolValue=(DEFAULT | '0' | '1')      #tableOptionPersistent
    | STATS_SAMPLE_PAGES '='? decimalLiteral                        #tableOptionSamplePage
    | TABLESPACE uid tablespaceStorage?                             #tableOptionTablespace
    | TABLE_TYPE '=' tableType                                      #tableOptionTableType
    | tablespaceStorage                                             #tableOptionTablespace
    | TRANSACTIONAL '='? ('0' | '1')                                #tableOptionTransactional
    | UNION '='? '(' tables ')'                                     #tableOptionUnion

    // tidb
    | TTL '=' uid intervalExpr (TTL_ENABLE '=' STRING_LITERAL )?             #tidbTableOptionTTL
    ;


columnConstraint
    : nullNotnull                                                   #nullColumnConstraint
    | DEFAULT defaultValue                                          #defaultColumnConstraint
    | (VISIBLE | INVISIBLE)                                         #invisibleColumnConstraint
    | (AUTO_INCREMENT | AUTO_RANDOM | ON UPDATE currentTimestamp)   #autoIncrementColumnConstraint //tidb
    | PRIMARY? KEY (CLUSTERED | NONCLUSTERED)?                      #primaryKeyColumnConstraint // tidb
    | UNIQUE KEY?                                                   #uniqueKeyColumnConstraint
    | COMMENT STRING_LITERAL                                        #commentColumnConstraint
    | COLUMN_FORMAT colformat=(FIXED | DYNAMIC | DEFAULT)           #formatColumnConstraint
    | STORAGE storageval=(DISK | MEMORY | DEFAULT)                  #storageColumnConstraint
    | referenceDefinition                                           #referenceColumnConstraint
    | COLLATE collationName                                         #collateColumnConstraint
    | (GENERATED ALWAYS)? AS '(' expression ')' (VIRTUAL | STORED | PERSISTENT)? #generatedColumnConstraint
    | SERIAL DEFAULT VALUE                                          #serialDefaultColumnConstraint
    | (CONSTRAINT name=uid?)?
      CHECK '(' expression ')'                                      #checkColumnConstraint
    ;

tableConstraint
    : (CONSTRAINT name=uid?)?
      PRIMARY KEY index=uid? indexType?
      indexColumnNames indexOption* (CLUSTERED | NONCLUSTERED)?    #primaryKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      UNIQUE indexFormat=(INDEX | KEY)? index=uid?
      indexType? indexColumnNames indexOption*                      #uniqueKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      FOREIGN KEY index=uid? indexColumnNames
      referenceDefinition                                           #foreignKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      CHECK '(' expression ')'                                      #checkTableConstraint
    ;



createSequence:
    CREATE TEMPORARY? SEQUENCE (IF NOT EXISTS)? sequence_name
       ( ( INCREMENT ( BY | '='? ) secquenceCount ) |
        ( MINVALUE '='? secquenceCount | NO MINVALUE | NOMINVALUE ) |
        ( MAXVALUE '='? secquenceCount | NO MAXVALUE | NOMAXVALUE ) |
        ( START ( WITH | '=' )? secquenceCount ) |
        ( CACHE '='? (DECIMAL_LITERAL| ONE_DECIMAL | TWO_DECIMAL) | NOCACHE | NO CACHE) |
        ( CYCLE | NOCYCLE | NO CYCLE))*
    ;

dropSequence:
    DROP SEQUENCE (IF EXISTS)? sequence_name (',' sequence_name)*
    ;


secquenceCount:
    MINUS? (DECIMAL_LITERAL| ONE_DECIMAL | TWO_DECIMAL )
;

sequence_name
    : fullId
    ;

partitionFunctionDefinition
    : LINEAR? HASH '(' expression ')'                               #partitionFunctionHash
    | LINEAR? KEY (ALGORITHM '=' algType=('1' | '2'))?
      '(' uidList ')'                                               #partitionFunctionKey
    | RANGE ( '(' expression ')' | COLUMNS '(' uidList ')' )        #partitionFunctionRange
    | LIST ( '(' expression ')' | COLUMNS '(' uidList ')' )         #partitionFunctionList
    | RANGE ( '(' expression ')' | COLUMNS '(' uidList ')' )  INTERVAL '(' ((DECIMAL_LITERAL| ONE_DECIMAL | TWO_DECIMAL) intervalType?)  ')'
        FIRST PARTITION LESS THAN '(' expression ')'
        LAST PARTITION LESS THAN '(' expression ')'
        (NULL PARTITION)?
        (MAXVALUE PARTITION)?                                       #tiIntervalPartition
    ;


partitionDefinitions
    : PARTITION BY partitionFunctionDefinition
      (PARTITIONS count=decimalLiteral)?
      (
        SUBPARTITION BY subpartitionFunctionDefinition
        (SUBPARTITIONS subCount=decimalLiteral)?
      )?
    ('(' partitionDefinition (',' partitionDefinition)* ')')?
    | (FIRST | LAST) PARTITION LESS THAN '(' expression ')'
    ;