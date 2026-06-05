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

parser grammar ObForMySqlParser;

import MySqlParser;

options {
    tokenVocab=ObForMySqlLexer;
}


selectSpec
    : (ALL | DISTINCT | DISTINCTROW)
    | HIGH_PRIORITY | STRAIGHT_JOIN | SQL_SMALL_RESULT
    | SQL_BIG_RESULT | SQL_BUFFER_RESULT
    | (SQL_CACHE | SQL_NO_CACHE)
    | SQL_CALC_FOUND_ROWS
    // ob
    | UNIQUE
    ;

insertStatement
    : INSERT
      priority=(LOW_PRIORITY | DELAYED | HIGH_PRIORITY)?
      ignore_? INTO? tableName
      (PARTITION '(' partitions=uidList? ')' )?
      (
        ('(' columns=uidList? ')')? insertStatementValue
        | SET
            setFirst=updatedElement
            (',' setElements+=updatedElement)*
      )
      (
        ON DUPLICATE KEY UPDATE
        duplicatedFirst=updatedElement
        (',' duplicatedElements+=updatedElement)*
      )?
    ;

dropTable
    : DROP TEMPORARY? (TABLE | TABLES) ifExists?
      tables dropType=(RESTRICT | CASCADE)?
    ;

//
//selectElement
//    : fullId '.' '*'                                                                              #selectStarElement
//    | fullColumnName  (AS? uid)?                                    #selectColumnElement
//    | functionCall (OVER '(' orderByClause ')')?  (AS? uid)?                                      #selectFunctionElement
//    | (LOCAL_ID VAR_ASSIGN)? expression  (AS? uid)?               #selectExpressionElement
//    ;

indexOption
    : KEY_BLOCK_SIZE '='? fileSizeLiteral
    | indexType
    | WITH PARSER uid
    | COMMENT STRING_LITERAL
    | INVISIBLE
    | VISIBLE

    //
    | VISIBLE
    | GLOBAL
    | LOCAL
    | BLOCK_SIZE decimalLiteral
    ;

createDatabaseOption
    : DEFAULT? (CHARACTER SET | CHARSET) '='? (charsetName | DEFAULT)
    | DEFAULT? COLLATE '='? collationName
    | READ WRITE
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

    // obMy
    | REPLICA_NUM '='? decimalLiteral                   #obMyTableOption
    | BLOCK_SIZE '='? decimalLiteral                   #obMyTableOption
    | USE_BLOOM_FILTER '='? ( TRUE | FALSE)                   #obMyTableOption
    | TABLET_SIZE '='? decimalLiteral                   #obMyTableOption
    | PCTFREE '='? decimalLiteral                   #obMyTableOption
    ;
