parser grammar AdsMyParser;

import MySqlParser;

options {
    tokenVocab=AdsMyLexer;
}




createTable
    : CREATE temporary_? TABLE ifNotExists?
       tableName
       (
         LIKE tableName
         | '(' LIKE parenthesisTable=tableName ')'
       )                                                            #copyCreateTable
    | CREATE temporary_? TABLE ifNotExists?
       tableName createDefinitions?
       ( tableOption (','? tableOption)* )?
       partitionDefinitions? keyViolate=(IGNORE | REPLACE)?
       AS? selectStatement                                          #queryCreateTable
    | CREATE temporary_? TABLE ifNotExists?
       tableName createDefinitions
       ( tableOption (','? tableOption)* )?
       partitionDefinitions?                                        #columnCreateTable

     | CREATE EXTERNAL TABLE (IF NOT EXISTS)? tableName
       '(' columnDefinition (',' columnDefinition?)* ')'
       (PARTITIONED BY '(' columnDefinition (',' columnDefinition?)* ')')?
       ROW FORMAT DELIMITED FIELDS TERMINATED BY STRING_LITERAL
       STORED AS (TEXTFILE|ORC|PARQUET|JSON|RCFIL|HUDI|ICEBERG|PAIMON)
       LOCATION STRING_LITERAL                                      #adbExternalTable
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

    // adb
    | (DISTRIBUTED | DISTRIBUTE) BY HASH '(' uid (',' uid)* ')'  #adbTableOption
    | (DISTRIBUTED | DISTRIBUTE) BY BROADCAST  #adbTableOption
    | INDEX_ALL EQUAL_SYMBOL STRING_LITERAL #adbTableOption
    | STORAGE_POLICY EQUAL_SYMBOL STRING_LITERAL #adbTableOption
    | TABLE_PROPERTIES EQUAL_SYMBOL STRING_LITERAL #adbTableOption
    | ENGINE '='? STRING_LITERAL                   #adbTableOption
    | PARTITION BY
             (VALUE '(' uid ')' | VALUE '(' DATE_FORMAT '(' uid ',' STRING_LITERAL ')'')' | VALUE '(' FROM_UNIXTIME '(' uid ',' STRING_LITERAL ')' ')')
       PARTITION NUM decimalLiteral     #adbTableOption
    ;




analyzeTable
    :{false}? ANALYZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
       TABLE tables
    | ANALYZE TABLE tableName (UPDATE (BASIC | HISTOGRAM | GROUP_STATS)?)? (ON uid (COMMA uid)*)? (WITH ENABLE SAMPLING)?
    ;

insertStatement
    : INSERT
      OVERWRITE?
      priority=(LOW_PRIORITY | DELAYED | HIGH_PRIORITY)?
      ignore_? INTO? tableName
      (PARTITION '(' partitions=uidList? ')' )?
      (
        ('(' columns=uidList ')')? insertStatementValue
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


setStatement
    : SET variableClause ('=' | ':=') expression
      (',' variableClause ('=' | ':=') expression)*                 #setVariable
    | SET (CHARACTER SET | CHARSET) (charsetName | DEFAULT)         #setCharset
    | SET NAMES
        (charsetName (COLLATE collationName)? | DEFAULT)            #setNames
    | setPasswordStatement                                          #setPassword
    | setTransactionStatement                                       #setTransaction
    | setAutocommitStatement                                        #setAutocommit
    | SET fullId ('=' | ':=') expression
      (',' fullId ('=' | ':=') expression)*                         #setNewValueInsideTrigger
    | SET ADB_CONFIG variableClause ('=' | ':=') expression
                           (',' variableClause ('=' | ':=') expression)*  #adbSetVariable
    ;

