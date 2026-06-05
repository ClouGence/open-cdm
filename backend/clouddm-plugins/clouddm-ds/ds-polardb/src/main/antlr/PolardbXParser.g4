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

parser grammar PolardbXParser;

import MySqlParser;

options {
    tokenVocab=PolardbXLexer;
}


createIndex
    :  CREATE (OR REPLACE)?                                        // OR is MariaDB-specific only
          intimeAction=(ONLINE | OFFLINE)?
          indexCategory=(UNIQUE | FULLTEXT | SPATIAL)?
          INDEX indexName indexType?
          ON tableName indexColumnNames
          indexOption*
          (
            ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
            | LOCK '='?
              lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
          )*
    |   CREATE indexCategory=UNIQUE? GLOBAL INDEX indexName ON tableName indexColumnNames (COVERING indexColumnNames)? polardbxPartitionOption
    ;


alterTable
    : ALTER intimeAction=(ONLINE | OFFLINE)?
      IGNORE? TABLE tableName
      ((alterSpecification |polardbXAlterSpecification) (',' ( alterSpecification | polardbXAlterSpecification))*)?
      polardbxPartitionOption?
    ;

partitionDefinitions
    : PARTITION BY partitionFunctionDefinition
      (PARTITIONS count=decimalLiteral)?
      (
        SUBPARTITION BY subpartitionFunctionDefinition
        (SUBPARTITIONS subCount=decimalLiteral)?
      )?
    ('(' partitionDefinition (',' partitionDefinition)* ')')?
    | polardbxPartitionOption
    ;


polardbXAlterSpecification
    :
     ADD (UNIQUE | (CONSTRAINT UNIQUE))? GLOBAL INDEX indexName indexColumnNames (COVERING indexColumnNames)? polardbxPartitionOption (LOCK EQUAL_SYMBOL (DEFAULT | NONE | SHARED | EXCLUSIVE ))?
    ;


polardbxPartitionOption
    : DBPARTITION BY dbShardingAlgorithm (TBPARTITION BY dbShardingAlgorithm)? (TBPARTITIONS decimalLiteral)? (DBPARTITIONS decimalLiteral)?
    ;


dbShardingAlgorithm
    : HASH '(' (indexColumnName (',' indexColumnName)*)? ')'
    | RANGE_HASH '(' indexColumnName ',' indexColumnName ',' decimalLiteral  ')'
    | RIGHT_SHIFT '(' indexColumnName ',' decimalLiteral ')'
    | UNI_HASH '(' indexColumnName ')'
    | WEEK '(' indexColumnName ')'
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

    // polardbx
    | BROADCAST                                                     # tableBroadcastOption
    | LOCALITY '=' STRING_LITERAL                                      # tableLocalityOption
    ;


indexColumnDefinition
    : indexFormat=(INDEX | KEY) uid? indexType?
      indexColumnNames indexOption*                                 #simpleIndexDeclaration
    | (FULLTEXT | SPATIAL)
      indexFormat=(INDEX | KEY)? uid?
      indexColumnNames indexOption*                                 #specialIndexDeclaration


     | UNIQUE? GLOBAL indexFormat=(INDEX | KEY) indexName? indexType?
                     indexColumnNames (COVERING indexColumnNames)? polardbxPartitionOption       #polardbXPartitionGlobalIndex
    ;


createDatabaseOption
    : DEFAULT? (CHARACTER SET | CHARSET) '='? (charsetName | DEFAULT)
    | DEFAULT? COLLATE '='? collationName
    | PARTITION_MODE '=' (PARTITIONING | SHARDING)
    ;

