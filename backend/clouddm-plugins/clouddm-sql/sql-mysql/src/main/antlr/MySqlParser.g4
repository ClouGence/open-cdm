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

parser grammar MySqlParser;

options { tokenVocab=MySqlLexer; superClass=MySqlParserBase; }

// Top Level Description

root
    : sqlStatements? MINUSMINUS? EOF
    ;

sqlStatements
    : (sqlStatement MINUSMINUS? | emptyStatement)
     ( SEMI+ (sqlStatement MINUSMINUS?  | emptyStatement))* SEMI?
    ;

sqlStatement
    : ddlStatement | dmlStatement | transactionStatement
    | replicationStatement | preparedStatement
    | administrationStatement | utilityStatement
    ;

emptyStatement
    : SEMI
    ;

ddlStatement
    : createDatabase | createEvent | createIndex
    | createLogfileGroup | createProcedure | createFunction | createLibrary
    | createMaskingPolicy | createServer | createSpatialReferenceSystem | createTable | createTablespaceInnodb
    | createTablespaceNdb | createUndoTablespace | createTrigger | createView
    | alterDatabase | alterEvent | alterFunction
    | alterInstance | alterLibrary | alterLogfileGroup | alterProcedure
    | alterServer | alterTable | alterTablespace | alterUndoTablespace | alterView
    | dropDatabase | dropEvent | dropIndex
    | dropLibrary | dropLogfileGroup | dropMaskingPolicy | dropProcedure | dropFunction
    | dropServer | dropSpatialReferenceSystem | dropTable | dropTablespace | dropUndoTablespace
    | dropTrigger | dropView
    | renameTable | truncateTable
    ;

dmlStatement
    : selectStatement | insertStatement | updateStatement
    | deleteStatement | replaceStatement | callStatement
    | importTableStatement | loadDataStatement | loadXmlStatement | doStatement
    | handlerStatement | withSelectStatement
    | tableStatement | valuesStatement
    ;

transactionStatement
    : startTransaction
    | beginWork | commitWork | rollbackWork
    | savepointStatement | rollbackStatement
    | releaseStatement | lockTables | unlockTables
    | lockInstance | unlockInstance
    ;

replicationStatement
    : changeMaster | changeReplicationSource | changeReplicationFilter | purgeBinaryLogs
    | resetMaster | resetSlave | resetReplica | resetBinaryLogsAndGtids
    | startSlave | startReplica | stopSlave | stopReplica
    | startGroupReplication | stopGroupReplication
    | xaStartTransaction | xaEndTransaction | xaPrepareStatement
    | xaCommitWork | xaRollbackWork | xaRecoverWork
    ;

preparedStatement
    : prepareStatement | executeStatement | deallocatePrepare
    ;

// remark: NOT INCLUDED IN sqlStatement, but include in body
//  of routine's statements
compoundStatement
    : blockStatement
    | caseStatement | ifStatement | leaveStatement
    | loopStatement | repeatStatement | whileStatement
    | iterateStatement | returnStatement | cursorStatement
    ;

administrationStatement
    : alterUser | createUser | dropUser | grantStatement
    | createRole
    | grantProxy | renameUser | revokeStatement
    | revokeProxy | analyzeTable | checkTable
    | checksumTable | optimizeTable | repairTable
    | cloneStatement
    | createUdfFunction | installPlugin | uninstallPlugin
    | installComponent | uninstallComponent
    | createResourceGroup | alterResourceGroup
    | dropResourceGroup | setResourceGroup
    | setStatement | showStatement | binlogStatement
    | cacheIndexStatement | flushStatement | killStatement
    | loadIndexIntoCache | resetStatement
    | restartStatement | shutdownStatement | dropRole
    ;

utilityStatement
    : simpleDescribeStatement | fullDescribeStatement
    | helpStatement | useStatement | signalStatement
    | resignalStatement | diagnosticsStatement
    ;


// Data Definition Language

//    Create statements

createDatabase
    : CREATE dbFormat=(DATABASE | SCHEMA)
      ifNotExists? databaseName createDatabaseOption*
    ;

databaseName
    : uid
    ;

createEvent
    : CREATE ownerStatement? EVENT ifNotExists? fullId
      ON SCHEDULE scheduleExpression
      (ON COMPLETION NOT? PRESERVE)? enableType?
      (COMMENT textLiteralToken)?
      DO routineBody
    ;

createIndex
    : CREATE
      indexCategory=UNIQUE?
      INDEX indexName indexType?
      ON tableName indexColumnNames
      normalIndexOption* indexLockAlgorithmOption*
    | CREATE indexCategory=FULLTEXT INDEX indexName
      ON tableName indexColumnNames
      fulltextIndexOption* indexLockAlgorithmOption*
    | CREATE indexCategory=SPATIAL INDEX indexName
      ON tableName indexColumnNames
      commonIndexOption* indexLockAlgorithmOption*
    ;

indexName
    : uid
    ;

createLogfileGroup
    : CREATE LOGFILE GROUP uid
      ADD UNDOFILE undoFile=textLiteralToken
      logfileGroupOption (','? logfileGroupOption)*
    ;

logfileGroupOption
    : INITIAL_SIZE '='? fileSizeLiteral
    | UNDO_BUFFER_SIZE '='? fileSizeLiteral
    | REDO_BUFFER_SIZE '='? fileSizeLiteral
    | NODEGROUP '='? decimalLiteral
    | WAIT
    | COMMENT '='? textLiteralToken
    | STORAGE? ENGINE '='? engineName
    ;

createProcedure
    : CREATE ownerStatement?
    PROCEDURE ({atLeastExact(80029)}? ifNotExists)? fullId
      '(' procedureParameter? (',' procedureParameter)* ')'
      routineOption* routineUsingClause? routineOption*
    (routineBody | {atLeast(8, 4)}? AS libraryCode)
    ;

createFunction
    : CREATE ownerStatement?
    FUNCTION ({atLeastExact(80029)}? ifNotExists)? fullId
      '(' functionParameter? (',' functionParameter)* ')'
      RETURNS dataType
      routineOption* routineUsingClause? routineOption*
    (routineBody | returnStatement | {atLeast(8, 4)}? AS libraryCode)
    ;

createLibrary
    : {atLeast(9, 7)}? CREATE LIBRARY ifNotExists? fullId
      libraryCharacteristic+
      AS libraryCode
    ;

libraryCharacteristic
    : LANGUAGE libraryLanguage
    | COMMENT textLiteralToken
    ;

createMaskingPolicy
    : {atLeast(9, 7)}? CREATE MASKING POLICY ifNotExists?
      policyName=uid '(' columnName=uid ')' expression
    ;

libraryLanguage
    : uid
    ;

libraryCode
    : textLiteralToken
    | DOLLAR_QUOTED_STRING
    | hexadecimalLiteral
    | bitStringLiteral
    ;

createRole
    : {atLeast(8, 0)}? CREATE ROLE (IF NOT EXISTS)? roleName (',' roleName)*
    ;

createServer
    : CREATE SERVER serverObjectName
    FOREIGN DATA WRAPPER wrapperName=serverWrapperName
    OPTIONS '(' serverOption (',' serverOption)* ')'
    ;

serverObjectName
    : uid
    | textLiteralToken
    ;

serverWrapperName
    : MYSQL | textLiteralToken
    ;

createSpatialReferenceSystem
    : {atLeast(8, 0)}? CREATE OR REPLACE SPATIAL REFERENCE SYSTEM decimalLiteral srsAttribute*
    | {atLeast(8, 0)}? CREATE SPATIAL REFERENCE SYSTEM ifNotExists? decimalLiteral srsAttribute*
    ;

srsAttribute
    : NAME textLiteralToken
    | DEFINITION textLiteralToken
    | ORGANIZATION textLiteralToken IDENTIFIED BY decimalLiteral
    | DESCRIPTION textLiteralToken
    ;

createTable
    : CREATE createTableModifier? TABLE ifNotExists?
       tableName
       (
         LIKE tableName
         | '(' LIKE parenthesisTable=tableName ')'
       )                                                            #copyCreateTable
    | CREATE createTableModifier? TABLE ifNotExists?
       tableName createDefinitions?
       ( tableOption (','? tableOption)* )?
       partitionDefinitions? keyViolate=(IGNORE | REPLACE)?
       AS? createTableQueryExpression                               #queryCreateTable
    | CREATE createTableModifier? TABLE ifNotExists?
       tableName createDefinitions
       ( tableOption (','? tableOption)* )?
       partitionDefinitions?                                        #columnCreateTable
    ;

createTableModifier
    : temporary_
    | {atLeast(9, 7)}? EXTERNAL
    ;

temporary_
    : TEMPORARY
    ;

createTableQueryExpression
    : selectStatement
    | {atLeast(8, 0)}? withSelectStatement
    | {atLeast(8, 0)}? tableStatement
    | {atLeast(8, 0)}? valuesStatement
    ;

createTablespaceInnodb
    : CREATE TABLESPACE uid
      (
        ADD DATAFILE datafile=textLiteralToken (','? tablespaceOption)*
        | {atLeast(8, 0)}? tablespaceOption (','? tablespaceOption)*
      )
    ;

createTablespaceNdb
    : CREATE TABLESPACE uid
      ADD DATAFILE datafile=textLiteralToken
      USE LOGFILE GROUP uid
      tablespaceOption (','? tablespaceOption)*
    ;

tablespaceOption
    : INITIAL_SIZE '='? fileSizeLiteral
    | AUTOEXTEND_SIZE '='? fileSizeLiteral
    | MAX_SIZE '='? fileSizeLiteral
    | EXTENT_SIZE '='? fileSizeLiteral
    | NODEGROUP '='? decimalLiteral
    | STORAGE? ENGINE '='? engineName
    | WAIT
    | NO_WAIT
    | COMMENT '='? textLiteralToken
    | {atLeast(5, 7)}? FILE_BLOCK_SIZE '='? fileSizeLiteral
    | {atLeast(8, 0)}? ENCRYPTION '='? textLiteralToken
    | {atLeast(8, 0)}? ENGINE_ATTRIBUTE '='? textLiteralToken
    ;

createUndoTablespace
    : {atLeast(8, 0)}? CREATE UNDO TABLESPACE uid
      ADD DATAFILE textLiteralToken
      (STORAGE? ENGINE '='? engineName)?
    ;

createTrigger
    : CREATE ownerStatement?
      TRIGGER ({atLeastExact(80029)}? ifNotExists)? thisTrigger=fullId
      triggerTime=(BEFORE | AFTER)
      triggerEvent=(INSERT | UPDATE | DELETE)
      ON tableName FOR EACH ROW
      triggerOrderClause?
      routineBody
    ;

triggerOrderClause
    : {atLeast(5, 7)}? triggerPlace=(FOLLOWS | PRECEDES) otherTrigger=fullId
    ;

createView
    : CREATE (OR REPLACE)?
      (
        ALGORITHM '=' algType=(UNDEFINED | MERGE | TEMPTABLE)
      )?
      ({atLeast(9, 7)}? MATERIALIZED)?
      ownerStatement?
      (SQL SECURITY secContext=(DEFINER | INVOKER))?
      (
        {atLeast(9, 7)}? jsonDualityView VIEW ({isCreateViewIfNotExistsAllowed()}? ifNotExists)? fullId ('(' uidList ')')?
        | VIEW ({isCreateViewIfNotExistsAllowed()}? ifNotExists)? fullId ('(' uidList ')')?
      )
      AS viewQueryStatement
      (WITH checkOption=(CASCADED | LOCAL)? CHECK OPTION)?
    ;

viewQueryStatement
    : selectStatement
    | withSelectStatement
    | tableStatement
    | valuesStatement
    ;

jsonDualityView
    : JSON RELATIONAL? DUALITY
    ;

// details

createDatabaseOption
    : DEFAULT? (CHARACTER SET | CHARSET) '='? (charsetName | DEFAULT)
    | DEFAULT? COLLATE '='? collationName
    | {atLeast(8, 0)}? DEFAULT? ENCRYPTION '='? textLiteralToken
    ;

alterDatabaseOption
    : createDatabaseOption
    | {atLeast(8, 0)}? READ ONLY '='? (DEFAULT | decimalLiteral)
    ;

ownerStatement
    : DEFINER '=' (userName | CURRENT_USER ( '(' ')')?)
    ;

scheduleExpression
    : AT timestampValue intervalExpr*                               #preciseSchedule
    | EVERY (decimalLiteral | expression) intervalType
        (
          STARTS startTimestamp=timestampValue
          (startIntervals+=intervalExpr)*
        )?
        (
          ENDS endTimestamp=timestampValue
          (endIntervals+=intervalExpr)*
        )?                                                          #intervalSchedule
    ;

timestampValue
    : CURRENT_TIMESTAMP
    | stringLiteral
    | decimalLiteral
    | expression
    ;

intervalExpr
    : '+' INTERVAL (decimalLiteral | expression) intervalType
    ;

intervalType
    : intervalTypeBase
    | YEAR | YEAR_MONTH | DAY_HOUR | DAY_MINUTE
    | DAY_SECOND | HOUR_MINUTE | HOUR_SECOND | MINUTE_SECOND
    | SECOND_MICROSECOND | MINUTE_MICROSECOND
    | HOUR_MICROSECOND | DAY_MICROSECOND
    ;

enableType
    : ENABLE
    | DISABLE
    | {atLeast(8, 4)}? DISABLE ON REPLICA
    | DISABLE ON SLAVE
    ;

indexType
    : (USING | TYPE) (BTREE | HASH | RTREE)
    ;

normalIndexOption
    : commonIndexOption
    | indexType
    ;

fulltextIndexOption
    : commonIndexOption
    | WITH PARSER uid
    ;

indexOption
    : normalIndexOption
    | WITH PARSER uid
    ;

commonIndexOption
    : KEY_BLOCK_SIZE '='? fileSizeLiteral
    | COMMENT textLiteralToken
    | {atLeast(8, 0)}? ENGINE_ATTRIBUTE '='? textLiteralToken
    | {atLeast(8, 0)}? SECONDARY_ENGINE_ATTRIBUTE '='? textLiteralToken
    | {atLeast(8, 0)}? (INVISIBLE | VISIBLE)
    ;

indexLockAlgorithmOption
    : ALGORITHM '='? (DEFAULT | INPLACE | COPY)
    | LOCK '='? (DEFAULT | NONE | SHARED | EXCLUSIVE)
    ;

procedureParameter
    : direction=(IN | OUT | INOUT)? uid dataType
    ;

functionParameter
    : uid dataType
    ;

routineOption
    : COMMENT textLiteralToken                                        #routineComment
    | LANGUAGE SQL                                                  #routineLanguage
    | {atLeast(8, 4)}? LANGUAGE uid                                 #routineExternalLanguage
    | NOT? DETERMINISTIC                                            #routineBehavior
    | (
        CONTAINS SQL | NO SQL | READS SQL DATA
        | MODIFIES SQL DATA
      )                                                             #routineData
    | SQL SECURITY context=(DEFINER | INVOKER)                      #routineSecurity
    ;

routineUsingClause
    : {atLeast(9, 7)}? USING '(' routineLibrary (',' routineLibrary)* ')'
    ;

routineLibrary
    : fullId ((AS? uid))?
    ;

alterRoutineOption
    : COMMENT textLiteralToken
    | LANGUAGE SQL
    | {atLeast(8, 4)}? LANGUAGE uid
    | (
        CONTAINS SQL | NO SQL | READS SQL DATA
        | MODIFIES SQL DATA
      )
    | SQL SECURITY context=(DEFINER | INVOKER)
    ;

alterRoutineUsingClause
    : {atLeast(9, 7)}? USING
      '(' (routineLibrary (',' routineLibrary)*)? ')'
    ;

serverOption
    : HOST textLiteralToken
    | DATABASE textLiteralToken
    | USER textLiteralToken
    | PASSWORD textLiteralToken
    | SOCKET textLiteralToken
    | OWNER textLiteralToken
    | PORT decimalLiteral
    ;

createDefinitions
    : '(' createDefinition (',' createDefinition)* ')'
    ;

createDefinition
    : columnDefinition                                          #columnDeclaration
    | tableConstraint                                               #constraintDeclaration
    | indexColumnDefinition                                         #indexDeclaration
    ;

columnDefinition
    : uid dataType constraints+=columnConstraint*
      {isColumnConstraintSequenceAllowed($constraints)}?
    | {atMost(5, 7)}? CUBE dataType constraints+=columnConstraint*
      {isColumnConstraintSequenceAllowed($constraints)}?
    | {atMost(5, 7)}? '.' uid dataType constraints+=columnConstraint*
      {isColumnConstraintSequenceAllowed($constraints)}?
    ;

columnConstraint
    : nullNotnull                                                   #nullColumnConstraint
    | {isDefaultColumnConstraintAllowed()}? DEFAULT defaultValue     #defaultColumnConstraint
    | {atLeast(8, 0)}? (VISIBLE | INVISIBLE)                        #invisibleColumnConstraint
    | (AUTO_INCREMENT | ON UPDATE currentTimestamp)                 #autoIncrementColumnConstraint
    | PRIMARY? KEY                                                  #primaryKeyColumnConstraint
    | UNIQUE KEY?                                                   #uniqueKeyColumnConstraint
    | COMMENT textLiteralToken                                        #commentColumnConstraint
    | COLUMN_FORMAT colformat=(FIXED | DYNAMIC | DEFAULT)           #formatColumnConstraint
    | STORAGE storageval=(DISK | MEMORY | DEFAULT)                  #storageColumnConstraint
    | referenceDefinition                                           #referenceColumnConstraint
    | COLLATE collationName                                         #collateColumnConstraint
    | {atLeast(8, 0)}? SRID decimalLiteral                          #sridColumnConstraint
    | {atLeast(8, 0)}? ENGINE_ATTRIBUTE '='? textLiteralToken         #engineAttributeColumnConstraint
    | {atLeast(8, 0)}? SECONDARY_ENGINE_ATTRIBUTE '='? textLiteralToken #secondaryEngineAttributeColumnConstraint
    | {atLeast(9, 7)}? EXTERNAL_FORMAT textLiteralToken             #externalFormatColumnConstraint
    | {atLeast(9, 7)}? MASKING POLICY uid                           #maskingPolicyColumnConstraint
    | {isGeneratedColumnConstraintAllowed()}? (GENERATED ALWAYS)? AS '(' expression ')' (VIRTUAL | STORED)? #generatedColumnConstraint
    | SERIAL DEFAULT VALUE                                          #serialDefaultColumnConstraint
    | ({atLeast(8, 0)}? CONSTRAINT name=uid?)?
      CHECK '(' expression ')' constraintEnforcement?               #checkColumnConstraint
    ;

tableConstraint
    : (CONSTRAINT name=uid?)?
      PRIMARY KEY index=uid? indexType?
      indexColumnNames indexOption*                                 #primaryKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      UNIQUE indexFormat=(INDEX | KEY)? index=uid?
      indexType? indexColumnNames indexOption*                      #uniqueKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      FOREIGN KEY index=uid? indexColumnNames
      referenceDefinition                                           #foreignKeyTableConstraint
    | (CONSTRAINT name=uid?)?
      CHECK '(' expression ')' constraintEnforcement?               #checkTableConstraint
    ;

constraintEnforcement
    : {atLeast(8, 0)}? NOT? ENFORCED
    ;

referenceDefinition
    : REFERENCES tableName indexColumnNames?
      (MATCH matchType=(FULL | PARTIAL | SIMPLE))?
      referenceAction?
    ;

referenceAction
    : ON DELETE onDelete=referenceControlType
      (
        ON UPDATE onUpdate=referenceControlType
      )?
    | ON UPDATE onUpdate=referenceControlType
      (
        ON DELETE onDelete=referenceControlType
      )?
    ;

referenceControlType
    : RESTRICT | CASCADE | SET (NULL_LITERAL | DEFAULT) | NO ACTION
    ;

indexColumnDefinition
    : indexFormat=(INDEX | KEY) uid? indexType?
      indexColumnNames indexOption*                                 #simpleIndexDeclaration
    | (FULLTEXT | SPATIAL)
      indexFormat=(INDEX | KEY)? uid?
      indexColumnNames indexOption*                                 #specialIndexDeclaration
    ;

tableOption
    : ENGINE '='? engineName                                        #tableOptionEngine
    | {atLeast(8, 0)}? SECONDARY_ENGINE '='?
      (NULL_LITERAL | engineName | textLiteralToken)                  #tableOptionSecondaryEngine
    | AUTO_INCREMENT '='? decimalLiteral                            #tableOptionAutoIncrement
    | {atLeast(8, 0)}? AUTOEXTEND_SIZE '='? fileSizeLiteral         #tableOptionAutoextendSize
    | AVG_ROW_LENGTH '='? decimalLiteral                            #tableOptionAverage
    | DEFAULT? (CHARACTER SET | CHARSET) '='? (charsetName|DEFAULT) #tableOptionCharset
    | CHECKSUM '='? decimalLiteral                                  #tableOptionChecksum
    | DEFAULT? COLLATE '='? collationName                           #tableOptionCollate
    | COMMENT '='? textLiteralToken                                   #tableOptionComment
    | {atLeast(5, 7)}? COMPRESSION '='? (textLiteralToken | ID)       #tableOptionCompression
    | CONNECTION '='? textLiteralToken                                #tableOptionConnection
    | DATA DIRECTORY '='? textLiteralToken                            #tableOptionDataDirectory
    | DELAY_KEY_WRITE '='? decimalLiteral                           #tableOptionDelay
    | {atLeast(5, 7)}? ENCRYPTION '='? textLiteralToken               #tableOptionEncryption
    | {atLeast(8, 0)}? ENGINE_ATTRIBUTE '='? textLiteralToken         #tableOptionEngineAttribute
    | {atLeast(8, 0)}? SECONDARY_ENGINE_ATTRIBUTE '='? textLiteralToken #tableOptionSecondaryEngineAttribute
    | {atLeast(9, 7)}? externalFileFormat                         #tableOptionExternalFileFormat
    | {atLeast(9, 7)}? FILES '='? '(' externalFiles ')'          #tableOptionExternalFiles
    | {atLeast(9, 7)}? ALLOW_MISSING_FILES '='? ternaryOption    #tableOptionAllowMissingFiles
    | {atLeast(9, 7)}? VERIFY_KEY_CONSTRAINTS '='? ternaryOption #tableOptionVerifyKeyConstraints
    | {atLeast(9, 7)}? STRICT_LOAD '='? ternaryOption            #tableOptionStrictLoad
    | {atLeast(9, 7)}? AUTO_REFRESH '='? ternaryOption           #tableOptionAutoRefresh
    | {atLeast(9, 7)}? AUTO_REFRESH_SOURCE '='?
      (NONE | textLiteralToken)                                    #tableOptionAutoRefreshSource
    | {atLeast(8, 0)}? START TRANSACTION                            #tableOptionStartTransaction
    | INDEX DIRECTORY '='? textLiteralToken                           #tableOptionIndexDirectory
    | INSERT_METHOD '='? insertMethod=(NO | FIRST | LAST)           #tableOptionInsertMethod
    | KEY_BLOCK_SIZE '='? fileSizeLiteral                           #tableOptionKeyBlockSize
    | MAX_ROWS '='? decimalLiteral                                  #tableOptionMaxRows
    | MIN_ROWS '='? decimalLiteral                                  #tableOptionMinRows
    | PACK_KEYS '='? extBoolValue=('0' | '1' | DEFAULT)             #tableOptionPackKeys
    | PASSWORD '='? textLiteralToken                                  #tableOptionPassword
    | ROW_FORMAT '='?
        rowFormat=(
          DEFAULT | DYNAMIC | FIXED | COMPRESSED
          | REDUNDANT | COMPACT
        )                                                           #tableOptionRowFormat
    | STATS_AUTO_RECALC '='? extBoolValue=(DEFAULT | '0' | '1')     #tableOptionRecalculation
    | STATS_PERSISTENT '='? extBoolValue=(DEFAULT | '0' | '1')      #tableOptionPersistent
    | STATS_SAMPLE_PAGES '='? (decimalLiteral | DEFAULT)            #tableOptionSamplePage
    | TABLESPACE ({atLeast(5, 7)}? '=')? uid tablespaceStorage?      #tableOptionTablespace
    | tablespaceStorage                                             #tableOptionTablespace
    | UNION '='? '(' tables? ')'                                    #tableOptionUnion
    ;

externalFileFormat
    : FILE_FORMAT '='? '('
      remoteOutfileInfo*
      ((FIELDS | COLUMNS) externalFieldTerm*)?
      (LINES selectLinesInto*)?
      (IGNORE decimalLiteral (LINES | ROWS))?
      ')'
    ;

externalFieldTerm
    : TERMINATED BY textStringLiteral
    | OPTIONALLY? ENCLOSED BY textStringLiteral
    | NOT ENCLOSED
    | ESCAPED BY textStringLiteral
    | (DATE | TIME | DATETIME) FORMAT textStringLiteral
    | NULL_LITERAL AS textStringLiteral
    | EMPTY VALUE textStringLiteral
    ;

externalFiles
    : externalFileAttributes (',' externalFileAttributes)*
    ;

externalFileAttributes
    : externalFileAttribute+
    ;

externalFileAttribute
    : (URL | URI | FILE_NAME | FILE_PATTERN | FILE_PREFIX) '='? textLiteralToken
    | (ALLOW_MISSING_FILES | STRICT_LOAD) '='? ternaryOption
    ;

ternaryOption
    : decimalLiteral
    | DEFAULT
    ;

tablespaceStorage
    : STORAGE (DISK | MEMORY)
    ;

partitionDefinitions
    : PARTITION BY partitionFunctionDefinition
      (PARTITIONS count=decimalLiteral)?
      (
        SUBPARTITION BY subpartitionFunctionDefinition
        (SUBPARTITIONS subCount=decimalLiteral)?
      )?
    ('(' partitionDefinition (',' partitionDefinition)* ')')?
    ;

partitionFunctionDefinition
    : LINEAR? HASH '(' expression ')'                               #partitionFunctionHash
    | LINEAR? KEY (ALGORITHM '=' algType=('1' | '2'))?
      '(' uidList? ')'                                              #partitionFunctionKey
    | RANGE ( '(' expression ')' | COLUMNS '(' uidList ')' )        #partitionFunctionRange
    | LIST ( '(' expression ')' | COLUMNS '(' uidList ')' )         #partitionFunctionList
    ;

subpartitionFunctionDefinition
    : LINEAR? HASH '(' expression ')'                               #subPartitionFunctionHash
    | LINEAR? KEY (ALGORITHM '=' algType=('1' | '2'))?
      '(' uidList ')'                                               #subPartitionFunctionKey
    ;

partitionDefinition
    : PARTITION uid VALUES LESS THAN
      '('
          partitionDefinerAtom (',' partitionDefinerAtom)*
      ')'
      partitionOption*
      ( '(' subpartitionDefinition (',' subpartitionDefinition)* ')' )?       #partitionComparision
    | PARTITION uid VALUES LESS THAN
      partitionDefinerAtom partitionOption*
      ( '(' subpartitionDefinition (',' subpartitionDefinition)* ')' )?       #partitionComparision
    | PARTITION uid VALUES IN
      '('
          partitionDefinerAtom (',' partitionDefinerAtom)*
      ')'
      partitionOption*
      ( '(' subpartitionDefinition (',' subpartitionDefinition)* ')' )?       #partitionListAtom
    | PARTITION uid VALUES IN
      '('
          partitionDefinerVector (',' partitionDefinerVector)*
      ')'
      partitionOption*
      ( '(' subpartitionDefinition (',' subpartitionDefinition)* ')' )?       #partitionListVector
    | PARTITION uid partitionOption*
      ( '(' subpartitionDefinition (',' subpartitionDefinition)* ')' )?       #partitionSimple
    ;

partitionDefinerAtom
    : constant | expression | MAXVALUE
    ;

partitionDefinerVector
    : '(' partitionDefinerAtom (',' partitionDefinerAtom)+ ')'
    ;

subpartitionDefinition
    : SUBPARTITION uid partitionOption*
    ;

partitionOption
    : STORAGE? ENGINE '='? engineName                               #partitionOptionEngine
    | COMMENT '='? comment=textLiteralToken                           #partitionOptionComment
    | DATA DIRECTORY '='? dataDirectory=textLiteralToken              #partitionOptionDataDirectory
    | INDEX DIRECTORY '='? indexDirectory=textLiteralToken            #partitionOptionIndexDirectory
    | MAX_ROWS '='? maxRows=decimalLiteral                          #partitionOptionMaxRows
    | MIN_ROWS '='? minRows=decimalLiteral                          #partitionOptionMinRows
    | TABLESPACE '='? tablespace=uid                                #partitionOptionTablespace
    | NODEGROUP '='? nodegroup=decimalLiteral                       #partitionOptionNodeGroup
    ;

//    Alter statements

alterDatabase
    : ALTER dbFormat=(DATABASE | SCHEMA) databaseName?
      alterDatabaseOption+                                          #alterSimpleDatabase
    | {atMost(5, 7)}? ALTER dbFormat=(DATABASE | SCHEMA) uid
      UPGRADE DATA DIRECTORY NAME                                   #alterUpgradeName
    ;

alterEvent
    : ALTER ownerStatement?
      EVENT fullId
      (ON SCHEDULE scheduleExpression)?
      (ON COMPLETION NOT? PRESERVE)?
      (RENAME TO fullId)? enableType?
      (COMMENT textLiteralToken)?
      (DO routineBody)?
    ;

alterFunction
    : ALTER FUNCTION fullId
      alterRoutineOption* alterRoutineUsingClause? alterRoutineOption*
    ;

alterLibrary
    : {atLeast(9, 7)}? ALTER LIBRARY fullId COMMENT textLiteralToken
    ;

alterInstance
    : {atLeast(5, 7)}? ALTER INSTANCE alterInstanceAction
    ;

alterInstanceAction
    : ROTATE INNODB MASTER KEY
    | {atLeast(8, 0)}? (ENABLE | DISABLE) INNODB REDO_LOG
    | {atLeast(8, 0)}? ROTATE BINLOG MASTER KEY
    | {atLeast(8, 0)}? RELOAD TLS alterInstanceTlsChannel? alterInstanceNoRollback?
    | {atLeast(8, 0)}? RELOAD KEYRING
    ;

alterInstanceTlsChannel
    : FOR CHANNEL uid
    ;

alterInstanceNoRollback
    : NO ROLLBACK ON ERROR
    ;

alterLogfileGroup
    : ALTER LOGFILE GROUP uid
      ADD (UNDOFILE | {atMost(5, 7)}? REDOFILE) textLiteralToken
      alterLogfileGroupOption (','? alterLogfileGroupOption)*
    ;

alterLogfileGroupOption
    : INITIAL_SIZE '='? fileSizeLiteral
    | WAIT
    | STORAGE? ENGINE '='? engineName
    ;

alterProcedure
    : ALTER PROCEDURE fullId
      alterRoutineOption* alterRoutineUsingClause? alterRoutineOption*
    ;

alterServer
    : ALTER SERVER serverObjectName OPTIONS
      '(' serverOption (',' serverOption)* ')'
    ;

alterTable
    : ALTER ({atMost(5, 6)}? IGNORE)? TABLE tableName
      (alterSpecification (',' alterSpecification)*)?
      partitionDefinitions?
    ;

alterTablespace
    : ALTER TABLESPACE uid
      objectAction=(ADD | DROP) DATAFILE textLiteralToken
      (','? alterTablespaceOption)*
    | {atMost(5, 7)}? ALTER TABLESPACE uid
      CHANGE DATAFILE textLiteralToken
      (','? legacyChangeTablespaceOption)+
    | {atMost(5, 7)}? ALTER TABLESPACE uid
      (READ_ONLY | READ_WRITE | NOT ACCESSIBLE)
    | {atLeast(8, 0)}? ALTER TABLESPACE uid
      alterTablespaceOption (','? alterTablespaceOption)*
    | {atLeast(8, 0)}? ALTER TABLESPACE oldName=uid RENAME TO newName=uid
    ;

alterTablespaceOption
    : INITIAL_SIZE '='? fileSizeLiteral
    | AUTOEXTEND_SIZE '='? fileSizeLiteral
    | MAX_SIZE '='? fileSizeLiteral
    | STORAGE? ENGINE '='? engineName
    | WAIT
    | NO_WAIT
    | {atLeast(8, 0)}? ENCRYPTION '='? textLiteralToken
    | {atLeast(8, 0)}? ENGINE_ATTRIBUTE '='? textLiteralToken
    ;

legacyChangeTablespaceOption
    : INITIAL_SIZE '='? fileSizeLiteral
    | AUTOEXTEND_SIZE '='? fileSizeLiteral
    | MAX_SIZE '='? fileSizeLiteral
    ;

alterUndoTablespace
    : {atLeast(8, 0)}? ALTER UNDO TABLESPACE uid
      SET (ACTIVE | INACTIVE)
      (STORAGE? ENGINE '='? engineName)?
    ;

alterView
    : ALTER
      (
        ALGORITHM '=' algType=(UNDEFINED | MERGE | TEMPTABLE)
      )?
      ({atLeast(9, 7)}? MATERIALIZED)?
      ownerStatement?
      (SQL SECURITY secContext=(DEFINER | INVOKER))?
      (
        {atLeast(9, 7)}? jsonDualityView VIEW fullId ('(' uidList ')')?
        | VIEW fullId ('(' uidList ')')?
      )
      AS viewQueryStatement
      (WITH checkOpt=(CASCADED | LOCAL)? CHECK OPTION)?
    ;

// details

alterSpecification
    : tableOption (','? tableOption)*                               #alterByTableOption
    | ADD COLUMN? columnDefinition (FIRST | AFTER uid)?              #alterByAddColumn
    | ADD COLUMN?
        '('
           columnDefinition ( ','  columnDefinition)*
        ')'                                                         #alterByAddColumns
    | ADD indexFormat=(INDEX | KEY) indexName? indexType?
      indexColumnNames normalIndexOption*                           #alterByAddIndex
    | ADD (CONSTRAINT name=uid?)? PRIMARY KEY index=uid?
      indexType? indexColumnNames normalIndexOption*                #alterByAddPrimaryKey
    | ADD (CONSTRAINT name=uid?)? UNIQUE
      indexFormat=(INDEX | KEY)? indexName?
      indexType? indexColumnNames normalIndexOption*                #alterByAddUniqueKey
    | ADD keyType=FULLTEXT
      indexFormat=(INDEX | KEY)? indexName?
      indexColumnNames fulltextIndexOption*                         #alterByAddSpecialIndex
    | ADD keyType=SPATIAL
      indexFormat=(INDEX | KEY)? indexName?
      indexColumnNames commonIndexOption*                           #alterByAddSpecialIndex
    | ADD (CONSTRAINT name=uid?)? FOREIGN KEY
      indexName? indexColumnNames referenceDefinition               #alterByAddForeignKey
    | ADD (CONSTRAINT name=uid?)? CHECK '(' expression ')' constraintEnforcement? #alterByAddCheckTableConstraint
    | ALGORITHM '='? algType=(DEFAULT | INSTANT | INPLACE | COPY)   #alterBySetAlgorithm
    | {atLeast(9, 7)}? ALTER COLUMN? uid SET MASKING POLICY uid     #alterBySetMaskingPolicy
    | {atLeast(9, 7)}? ALTER COLUMN? uid DROP MASKING POLICY        #alterByDropMaskingPolicy
    | ALTER COLUMN? uid
      (
        SET DEFAULT defaultValue
        | DROP DEFAULT
        | {atLeast(8, 0)}? SET (VISIBLE | INVISIBLE)
      )                                                             #alterByChangeDefault
    | {atLeast(8, 0)}? ALTER (CHECK | CONSTRAINT) uid
      constraintEnforcement                                        #alterByAlterConstraintEnforcement
    | CHANGE COLUMN? oldColumn=uid
      columnDefinition
      (FIRST | AFTER afterColumn=uid)?                              #alterByChangeColumn // ifExists is MariaDB-specific
    | {atLeast(8, 0)}? RENAME COLUMN oldColumn=uid TO newColumn=uid #alterByRenameColumn
    | LOCK '='? lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)      #alterByLock
    | MODIFY COLUMN?
      columnDefinition (FIRST | AFTER uid)?                         #alterByModifyColumn
    | DROP COLUMN? uid RESTRICT?                                    #alterByDropColumn
    | {atLeast(8, 0)}? DROP (CONSTRAINT | CHECK) uid                #alterByDropConstraintCheck
    | DROP PRIMARY KEY                                              #alterByDropPrimaryKey
    | DROP indexFormat=(INDEX | KEY) indexName                      #alterByDropIndex
    | {atLeast(5, 7)}? RENAME indexFormat=(INDEX | KEY) uid TO uid  #alterByRenameIndex
    | {atLeast(8, 0)}? ALTER INDEX uid visivility=(VISIBLE | INVISIBLE) #alterByAlterIndexVisibility
    | DROP FOREIGN KEY uid                                         #alterByDropForeignKey
    | DISABLE KEYS                                                  #alterByDisableKeys
    | ENABLE KEYS                                                   #alterByEnableKeys
    | RENAME renameFormat=(TO | AS)? (tableName)                 #alterByRename
    | ORDER BY alterTableOrderList                                  #alterByOrder
    | CONVERT TO CHARACTER SET (charsetName | DEFAULT)
      (COLLATE collationName)?                                      #alterByConvertCharset
    | DEFAULT? CHARACTER SET '=' charsetName
      (COLLATE '=' collationName)?                                  #alterByDefaultCharset
    | DISCARD TABLESPACE                                            #alterByDiscardTablespace
    | IMPORT TABLESPACE                                             #alterByImportTablespace
    | FORCE                                                         #alterByForce
    | {atLeast(5, 7)}? validationFormat=(WITHOUT | WITH) VALIDATION #alterByValidate
    | ADD PARTITION (NO_WRITE_TO_BINLOG | LOCAL)?
        (
          '(' partitionDefinition (',' partitionDefinition)* ')'
          | PARTITIONS decimalLiteral
        )?                                                          #alterByAddPartition
    | DROP PARTITION uidList                                        #alterByDropPartition
    | {atLeast(5, 7)}? DISCARD PARTITION (uidList | ALL) TABLESPACE #alterByDiscardPartition
    | {atLeast(5, 7)}? IMPORT PARTITION (uidList | ALL) TABLESPACE  #alterByImportPartition
    | TRUNCATE PARTITION (uidList | ALL)                            #alterByTruncatePartition
    | COALESCE PARTITION (NO_WRITE_TO_BINLOG | LOCAL)? decimalLiteral #alterByCoalescePartition
    | REORGANIZE PARTITION (NO_WRITE_TO_BINLOG | LOCAL)?
        (
          uidList INTO '('
            partitionDefinition (',' partitionDefinition)*
          ')'
        )?                                                          #alterByReorganizePartition
    | EXCHANGE PARTITION uid WITH TABLE tableName
      ({atLeast(5, 7)}? validationFormat=(WITH | WITHOUT) VALIDATION)? #alterByExchangePartition
    | ANALYZE PARTITION (NO_WRITE_TO_BINLOG | LOCAL)? (uidList | ALL) #alterByAnalyzePartition
    | CHECK PARTITION (uidList | ALL) checkTableOption*             #alterByCheckPartition
    | OPTIMIZE PARTITION (NO_WRITE_TO_BINLOG | LOCAL)? (uidList | ALL)
      ({atMost(5, 7)}? (NO_WRITE_TO_BINLOG | LOCAL))?                #alterByOptimizePartition
    | REBUILD PARTITION (NO_WRITE_TO_BINLOG | LOCAL)? (uidList | ALL) #alterByRebuildPartition
    | REPAIR PARTITION (NO_WRITE_TO_BINLOG | LOCAL)? (uidList | ALL)
      (QUICK | EXTENDED | USE_FRM)*                                 #alterByRepairPartition
    | REMOVE PARTITIONING                                           #alterByRemovePartitioning
    | {between(5, 7, 5, 7)}? UPGRADE PARTITIONING                   #alterByUpgradePartitioning
    | {atLeast(8, 0)}? SECONDARY_LOAD
      ({atLeast(8, 4)}? PARTITION '(' uidList ')')?
      ({atLeast(9, 7)}? VALIDATE ((decimalLiteral | ALL) ROWS)? ONLY)?
      ({atLeast(9, 7)}? GUIDED (ON | OFF))?                         #alterBySecondaryLoad
    | {atLeast(8, 0)}? SECONDARY_UNLOAD
      ({atLeast(8, 4)}? PARTITION '(' uidList ')')?                 #alterBySecondaryUnload
    ;

alterTableOrderList
    : uid (ASC | DESC)? (',' uid (ASC | DESC)?)*
    ;


//    Drop statements

dropDatabase
    : DROP dbFormat=(DATABASE | SCHEMA) ifExists? databaseName
    ;

dropEvent
    : DROP EVENT ifExists? fullId
    ;

dropIndex
    : DROP INDEX indexName ON tableName
      (
        ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
        | LOCK '='?
          lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
      )*
    ;

dropLogfileGroup
    : DROP LOGFILE GROUP uid ENGINE '='? engineName
    ;

dropProcedure
    : DROP PROCEDURE ifExists? fullId
    ;

dropFunction
    : DROP FUNCTION ifExists? fullId
    ;

dropLibrary
    : {atLeast(9, 7)}? DROP LIBRARY ifExists? fullId
    ;

dropMaskingPolicy
    : {atLeast(9, 7)}? DROP MASKING POLICY ifExists? uid
    ;

dropServer
    : DROP SERVER ifExists? serverObjectName
    ;

dropSpatialReferenceSystem
    : {atLeast(8, 0)}? DROP SPATIAL REFERENCE SYSTEM ifExists? decimalLiteral
    ;

dropTable
    : DROP TEMPORARY? (TABLE | TABLES) ifExists?
      tables dropType=(RESTRICT | CASCADE)?
    ;

dropTablespace
    : DROP TABLESPACE uid (STORAGE? ENGINE '='? engineName)?
    ;

dropUndoTablespace
    : {atLeast(8, 0)}? DROP UNDO TABLESPACE uid (STORAGE? ENGINE '='? engineName)?
    ;

dropTrigger
    : DROP TRIGGER ifExists? fullId
    ;

dropView
    : DROP VIEW ifExists?
      fullId (',' fullId)* dropType=(RESTRICT | CASCADE)?
    ;


//    Other DDL statements

renameTable
    : RENAME (TABLE | TABLES)
      renameTableClause (',' renameTableClause)*
    ;

renameTableClause
    : tableName TO tableName
    ;

truncateTable
    : TRUNCATE TABLE? tableName
    ;


// Data Manipulation Language

//    Primary DML Statements


callStatement
    : CALL procName
      (
        '(' procedureArgs? ')'
      )?
    ;

procedureArgs
    : (constant  | functionCall | expression)
    (
      ','
      (constant  | functionCall | expression)
    )*
    ;

deleteStatement
    : singleDeleteStatement | multipleDeleteStatement
    ;

doStatement
    : DO doSelectItem (',' doSelectItem)*
    ;

doSelectItem
    : expression
      ({atLeast(5, 7)}? AS? (uid | textLiteralToken))?
    ;

handlerStatement
    : handlerOpenStatement
    | handlerReadIndexStatement
    | handlerReadStatement
    | handlerCloseStatement
    ;

importTableStatement
    : {atLeast(8, 0)}? IMPORT TABLE FROM stringLiteral (',' stringLiteral)*
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
            insertAlias?
      )
      (
        ON DUPLICATE KEY UPDATE
        duplicatedFirst=updatedElement
        (',' duplicatedElements+=updatedElement)*
      )?
    ;

loadDataStatement
    : LOAD DATA
      priority=(LOW_PRIORITY | CONCURRENT)?
      loadFrom? LOCAL? loadSource
      loadSourceCount? loadSourceOrder?
      violation=(REPLACE | IGNORE)?
      INTO TABLE tableName
      (PARTITION '(' uidList ')' )?
      (CHARACTER SET charset=charsetName)?
      loadCompression?
      (
        fieldsFormat=(FIELDS | COLUMNS)
        loadFieldsInto+
      )?
      (
        LINES
          selectLinesInto+
      )?
      (
        IGNORE decimalLiteral linesFormat=(LINES | ROWS)
      )?
      ( '(' (assignmentField (',' assignmentField)*)? ')' )?
      (SET updatedElement (',' updatedElement)*)?
      loadBulkOptions?
    ;

loadXmlStatement
    : LOAD XML
      priority=(LOW_PRIORITY | CONCURRENT)?
      loadFrom? LOCAL? loadSource
      loadSourceCount? loadSourceOrder?
      violation=(REPLACE | IGNORE)?
      INTO TABLE tableName
      (PARTITION '(' uidList ')' )?
      (CHARACTER SET charset=charsetName)?
      loadCompression?
      (ROWS IDENTIFIED BY tag=textLiteralToken)?
      (
        fieldsFormat=(FIELDS | COLUMNS)
        loadFieldsInto+
      )?
      (
        LINES
          selectLinesInto+
      )?
      ( IGNORE decimalLiteral linesFormat=(LINES | ROWS) )?
      ( '(' (assignmentField (',' assignmentField)*)? ')' )?
      (SET updatedElement (',' updatedElement)*)?
      loadBulkOptions?
    ;

loadFrom
    : {atLeast(8, 0)}? FROM
    ;

loadSource
    : INFILE textLiteralToken
    | {atLeast(8, 0)}? URL textLiteralToken
    | {atLeast(8, 4)}? S3 textLiteralToken
    | {atLeast(9, 7)}? URI textLiteralToken
    ;

loadSourceCount
    : {atLeast(8, 0)}? COUNT decimalLiteral
    ;

loadSourceOrder
    : {atLeast(8, 0)}? IN PRIMARY KEY ORDER
    ;

loadCompression
    : {atLeast(8, 4)}? COMPRESSION '=' (textLiteralToken | ID)
    ;

loadFieldsInto
    : selectFieldsInto
    | {atLeast(9, 7)}? NOT ENCLOSED
    | {atLeast(9, 7)}? DATE FORMAT textStringLiteral
    | {atLeast(9, 7)}? TIME FORMAT textStringLiteral
    | {atLeast(9, 7)}? DATETIME FORMAT textStringLiteral
    | {atLeast(9, 7)}? NULL_LITERAL AS textStringLiteral
    | {atLeast(9, 7)}? EMPTY VALUE textStringLiteral
    ;

loadBulkOptions
    : {atLeast(8, 4)}? PARALLEL '=' decimalLiteral
      (MEMORY '=' fileSizeLiteral)? loadAlgorithmBulk?
    | {atLeast(8, 4)}? MEMORY '=' fileSizeLiteral loadAlgorithmBulk?
    | loadAlgorithmBulk
    ;

loadAlgorithmBulk
    : {atLeast(8, 0)}? ALGORITHM '=' BULK
    ;

replaceStatement
    : REPLACE priority=(LOW_PRIORITY | DELAYED)?
      INTO? tableName
      (PARTITION '(' partitions=uidList ')' )?
      (
        ('(' columns=uidList? ')')? replaceStatementValue
        | SET
          setFirst=updatedElement
          (',' setElements+=updatedElement)*
      )
    ;

selectStatement
    : query=querySpecification querySpecificationSelectTail[$query.ctx] #querySpecificationSelect
    | query=parenthesizedSelect queryExpressionSelectTail[$query.ctx] #queryExpressionSelect
    | {atLeast(8, 0)}? (tableStatement | valuesStatement) unionStatement+
        orderByClause? limitClause? selectIntoExpression?           #unionTableValueSelect
    ;

querySpecificationSelectTail[QuerySpecificationContext query]
    : (
        unionStatement+
        {isUnionAfterQuerySpecificationAllowed($query)}?
        orderByClause? limitClause? lockClauses?
        ({isSelectTailIntoAllowed($query)}? selectIntoExpression)?
        | {atMost(5, 7)}? lockClauses unionStatement+
          orderByClause? limitClause?
        | lockClauses ({isSelectTailIntoAllowed($query)}? selectIntoExpression)?
      )?
    ;

queryExpressionSelectTail[ParserRuleContext parenthesizedQuery]
    : ({isUnionAfterParenthesizedQueryAllowed($parenthesizedQuery)}? unionStatement)*
      orderByClause? limitClause? lockClauses?
      ({atLeast(8, 0)}? selectIntoExpression)?
    ;




updateStatement
    :
      singleUpdateStatement | multipleUpdateStatement
    ;

withSelectStatement
    : withClause (selectStatement | tableStatement | valuesStatement)
    ;

withClause
    : {atLeast(8, 0)}? WITH RECURSIVE? withSelectExpr (COMMA withSelectExpr)*
    ;

withSelectExpr
    : uid ('(' uidList ')')? AS LR_BRACKET
      (withSelectStatement | selectStatement | tableStatement | valuesStatement)
      RR_BRACKET
    ;

// details

insertStatementValue
    : insertFormat=(VALUES | VALUE)
      valuesRow (',' valuesRow)* insertAlias?                         #commentInsertValue
    | insertQuerySource                                                #selectInsertValue
    ;

replaceStatementValue
    : insertFormat=(VALUES | VALUE) valuesRow (',' valuesRow)*
    | insertQuerySource
    ;

insertQuerySource
    : insertQueryStatement
    | '(' insertQueryStatement ')'
    ;

insertQueryStatement
    : selectStatement
    | withSelectStatement
    | {atLeast(8, 0)}? tableStatement
    | {atLeast(8, 0)}? valuesStatement
    ;

insertAlias
    : {atLeastExact(80019)}? AS uid ('(' uidList ')')?
    ;

updatedElement
    : fullColumnName ('=' | ':=') (expression | DEFAULT)
    ;

assignmentField
    : uid | LOCAL_ID
    ;

tableStatement
    : {atLeast(8, 0)}? TABLE tableName orderByClause? limitClause?
    ;

valuesStatement
    : {atLeastExact(80019)}? VALUES explicitValuesRow (',' explicitValuesRow)*
      orderByClause? limitClause?
    ;

valuesRow
    : '(' expressionsWithDefaults? ')'
    ;

explicitValuesRow
    : ROW '(' expressionsWithDefaults? ')'
    ;

lockClause
    : FOR lockFormat=UPDATE lockModernOptions?
    | {atLeast(8, 0)}? FOR lockFormat=SHARE lockModernOptions?
    | LOCK IN SHARE MODE
    ;

lockClauses
    : lockClause ({atLeast(8, 0)}? lockClause)*
    ;

lockModernOptions
    : {atLeast(8, 0)}?
      (
        OF tableName (',' tableName)* (NOWAIT | SKIP_SYMBOL LOCKED)?
        | NOWAIT
        | SKIP_SYMBOL LOCKED
      )
    ;

//    Detailed DML Statements

singleDeleteStatement
    : withClause? DELETE deleteOption*
    FROM tableName deleteTableAlias?
      (PARTITION '(' uidList ')' )?
      whereClause?
      orderByClause? (LIMIT limit=limitClauseAtom)?
    ;

multipleDeleteStatement
    : withClause? DELETE deleteOption*
      (
        tableName ('.' '*')? ( ',' tableName ('.' '*')? )*
            FROM tableSources
        | FROM
            tableName ('.' '*')? ( ',' tableName ('.' '*')? )*
            USING tableSources
      )
      (WHERE expression)?
    ;

deleteOption
    : LOW_PRIORITY
    | QUICK
    | IGNORE
    ;

deleteTableAlias
    : {atLeastExact(80016)}? AS? uid
    ;

handlerOpenStatement
    : HANDLER tableName OPEN (AS? uid)?
    ;

handlerReadIndexStatement
    : HANDLER tableName READ index=uid
      (
        comparisonOperator '(' constants ')'
        | moveOrder=(FIRST | NEXT | PREV | LAST)
      )
      (WHERE expression)? limitClause?
    ;

handlerReadStatement
    : HANDLER tableName READ moveOrder=(FIRST | NEXT)
      (WHERE expression)? limitClause?
    ;

handlerCloseStatement
    : HANDLER tableName CLOSE
    ;

singleUpdateStatement
    :  withClause? UPDATE priority=LOW_PRIORITY? ignore_? tableName
      (PARTITION '(' uidList ')' )? (AS? uid)?
      indexHint*
      SET updatedElement (',' updatedElement)*
      whereClause? orderByClause? limitClause?
    ;

ignore_
    : IGNORE
    ;

multipleUpdateStatement
    : withClause? UPDATE priority=LOW_PRIORITY? IGNORE? tableSources
      SET updatedElement (',' updatedElement)*
      whereClause?
    ;

// details

orderByClause
    : ORDER BY orderByExpression (',' orderByExpression)*
    ;

legacyOrderByClause[boolean followsLegacyTail]
    : ORDER {$followsLegacyTail && atMost(5, 6)}? BY orderByExpression (',' orderByExpression)*
    ;

orderByExpression
    : expression order=(ASC | DESC)?
    ;

tableSources
    : tableSource (',' tableSource)*
    ;

tableSource
    : tableSourceItem joinPart*                                     #tableSourceBase
    | LCURLY_BRACKET uid tableSource RCURLY_BRACKET                 #tableSourceOdbc
    ;

tableSourceItem
    : jsonTableFunction (AS? aliasName)?                               #jsonTableItem
    | tableName
      (PARTITION '(' uidList ')' )?
      (AS? aliasName | {atMost(5, 7)}? EQUAL_SYMBOL aliasName)?
      indexHint* tableSampleClause?                                 #atomTableItem
    | queryExpression
      (AS? aliasName ({isDerivedColumnAliasListAllowed()}? '(' uidList ')')?)? #subqueryTableItem
    | {atLeast(8, 0)}? LATERAL '(' subqueryStatement ')'
      (AS? aliasName)? ('(' uidList ')')?                            #lateralTableItem
    | '(' tableSources ')'                                          #tableSourcesItem
    ;

tableSampleClause
    : {atLeast(8, 4)}? TABLESAMPLE (SYSTEM | BERNOULLI)
      '(' (decimalLiteral | LOCAL_ID | PARAM_MARKER) ')'
    ;

jsonTableFunction
    : {atLeast(8, 0)}? JSON_TABLE '(' expression ',' stringLiteral COLUMNS '(' jsonTableColumn (',' jsonTableColumn)* ')' ')'
    ;

jsonTableColumn
    : uid FOR ORDINALITY                                               #jsonTableOrdinalityColumn
    | uid dataType PATH stringLiteral jsonTableResponseClauses?        #jsonTablePathColumn
    | uid dataType EXISTS PATH stringLiteral jsonTableResponseClauses? #jsonTableExistsColumn
    | NESTED PATH stringLiteral COLUMNS '(' jsonTableColumn (',' jsonTableColumn)* ')' #jsonTableNestedColumn
    ;

jsonTableResponseClauses
    : jsonTableOnEmpty jsonTableOnError?
    | jsonTableOnError jsonTableOnEmpty?
    ;

jsonTableOnEmpty
    : (NULL_LITERAL | ERROR | DEFAULT jsonValueDefaultValue) ON EMPTY
    ;

jsonTableOnError
    : (NULL_LITERAL | ERROR | DEFAULT jsonValueDefaultValue) ON ERROR
    ;

indexHint
    : indexHintAction=USE keyFormat=(INDEX | KEY) (FOR indexHintType)?
      '(' indexHintNameList? ')'
    | indexHintAction=(IGNORE | FORCE) keyFormat=(INDEX | KEY) (FOR indexHintType)?
      '(' indexHintNameList ')'
    ;

indexHintNameList
    : (uid | PRIMARY) (',' (uid | PRIMARY))*
    ;

indexHintType
    : JOIN | ORDER BY | GROUP BY
    ;

joinPart
    : {atLeast(8, 0) && hasJoinConditionAhead()}? innerJoinType tableSource
      (
        ON expression
        | USING '(' uidList ')'
      )?                                                            #rightDeepInnerJoin
    | innerJoinType tableSourceItem
      (
        ON expression
        | USING '(' uidList ')'
      )?                                                            #innerJoin
    | {hasJoinConditionAhead()}? STRAIGHT_JOIN tableSource
      (ON expression)?                                              #rightDeepStraightJoin
    | STRAIGHT_JOIN tableSourceItem (ON expression)?                #straightJoin
    | outerJoinType  tableSource
        (
          ON expression
          | USING '(' uidList ')'
        )                                                           #outerJoin
    | naturalJoinType  tableSourceItem         #naturalJoin
    ;

innerJoinType
    : (INNER | CROSS)? JOIN
    ;

outerJoinType
    : (LEFT | RIGHT) OUTER? JOIN
    ;

naturalJoinType
    : NATURAL
      (
        {atLeast(8, 0)}? INNER
        | (LEFT | RIGHT) OUTER?
      )?
      JOIN
    ;

//    Select Statement's Details

queryExpression
    : '('
      (
        selectStatement
        | {atLeast(8, 0)}? withSelectStatement
        | {atLeast(8, 0)}? tableStatement
        | {atLeast(8, 0)}? valuesStatement
      )
      ')'
    ;

parenthesizedSelect
    : {atLeast(8, 0)}? queryExpression
    | {atMost(5, 7)}? legacyQueryExpression
    ;

legacyQueryExpression
    : '(' (querySpecification lockClauses? | legacyQueryExpression) ')'
    ;

subqueryStatement
    : withSelectStatement
    | selectStatement
    | {atLeast(8, 0)}? tableStatement
    | {atLeast(8, 0)}? valuesStatement
    ;

querySpecification
    : SELECT selectSpec* selectElements
      ({isTrailingSelectIntoAllowed()}? leadingInto=selectIntoExpression)?
      from1=fromClause?
      ({isQueryWhereAllowed($from1.ctx)}? whereClause)?
      ({isQueryGroupOrHavingAllowed($from1.ctx)}? groupClause)?
      ({isQueryGroupOrHavingAllowed($from1.ctx)}? havingClause)?
      windowClause? qualifyClause?
      ({isQueryOrderByAllowed($from1.ctx)}? order=orderByClause)?
      legacyOrderByClause[$order.ctx != null]?
      limit=limitClause?
      legacyOrderByClause[$limit.ctx != null]?
      ({isQueryProcedureAllowed($from1.ctx)}? procedureAnalyseClause)?
      ({isFinalSelectIntoAllowed($leadingInto.ctx)}? selectIntoExpression)?
    ;

querySpecificationUnionOperand
    : SELECT selectSpec* selectElements
      leadingInto=selectIntoExpression?
      from4=fromClause?
      ({isQueryWhereAllowed($from4.ctx)}? whereClause)?
      ({isQueryGroupOrHavingAllowed($from4.ctx)}? groupClause)?
      ({isQueryGroupOrHavingAllowed($from4.ctx)}? havingClause)?
      windowClause? qualifyClause?
      (
        {isUnionOperandTailAllowed($leadingInto.ctx)}?
        (procedureAnalyseClause | selectIntoExpression)
      )?
    ;

unionStatement
    : setOperator unionType=(ALL | DISTINCT)?
      (
        {atLeast(8, 0)}? querySpecificationUnionOperand
        | {atMost(5, 7)}? querySpecification
        | {atLeast(8, 0)}? queryExpression
        | {atMost(5, 7)}? legacyQueryExpression
        | tableStatement
        | valuesStatement
      )
    ;

setOperator
    : UNION | {atLeastExact(80031)}? (INTERSECT | EXCEPT)
    ;

// details

selectSpec
    : (ALL | DISTINCT | DISTINCTROW)
    | HIGH_PRIORITY | STRAIGHT_JOIN | SQL_SMALL_RESULT
    | SQL_BIG_RESULT | SQL_BUFFER_RESULT
    | (SQL_CACHE | SQL_NO_CACHE)
    | SQL_CALC_FOUND_ROWS
    ;

selectElements
    : (star='*' | selectElement ) (',' selectElement)*
    ;

selectElement
    : (schema=uid '.')? table=uid '.' '*'                         #selectStarElement
    | (LOCAL_ID VAR_ASSIGN)? expression (AS? selectAlias)?        #selectExpressionElement
    ;

selectAlias
    : uid
    | PERSIST
    | textLiteralToken
    ;

aliasName
    : uid
    ;

selectIntoExpression
    : INTO assignmentField (',' assignmentField )*                  #selectIntoVariables
    | INTO DUMPFILE textLiteralToken                                  #selectIntoDumpFile
    | {atLeast(9, 7)}? INTO OUTFILE (URL | URI) textLiteralToken
      remoteOutfileInfo* remoteOutfileFields? remoteOutfileLines?     #selectIntoRemoteFile
    | {atLeast(9, 7)}? INTO OUTFILE WITH PARAMETERS textLiteralToken  #selectIntoRemoteParameters
    | (
        INTO OUTFILE filename=textLiteralToken
        ({atLeast(9, 7)}? remoteOutfileInfo)*
        (CHARACTER SET charset=charsetName)?
        (
          fieldsFormat=(FIELDS | COLUMNS)
          selectFieldsInto+
        )?
        (
          LINES selectLinesInto+
        )?
      )                                                             #selectIntoTextFile
    ;

remoteOutfileInfo
    : (FORMAT | COMPRESSION) (uid | textLiteralToken)
    | HEADER (ON | OFF)
    | CHARACTER SET charsetName
    ;

remoteOutfileFields
    : (FIELDS | COLUMNS) externalFieldTerm*
    ;

remoteOutfileLines
    : LINES selectLinesInto*
    ;

selectFieldsInto
    : TERMINATED BY terminationField=textStringLiteral
    | OPTIONALLY? ENCLOSED BY enclosion=textStringLiteral
    | ESCAPED BY escaping=textStringLiteral
    ;

selectLinesInto
    : STARTING BY starting=textStringLiteral
    | TERMINATED BY terminationLine=textStringLiteral
    ;

fromClause
    : FROM tableSources
    ;

whereClause
    : WHERE expression
    ;

groupClause
    : GROUP BY
         groupByItem (',' groupByItem)*
         (WITH ROLLUP | {atMost(5, 7)}? WITH CUBE)?
    | {atLeast(8, 4)}? GROUP BY (ROLLUP | CUBE)
      '(' groupByItem (',' groupByItem)* ')'
    | {atLeast(9, 7)}? GROUP BY GROUPING SETS
      '(' groupingSet (',' groupingSet)* ')'
    ;

groupingSet
    : '(' (groupByItem (',' groupByItem)*)? ')'
    ;

havingClause
    : HAVING havingExpr=expression
    ;

windowClause
    : {atLeast(8, 0)}? WINDOW windowDefinition (',' windowDefinition)*
    ;

qualifyClause
    : {atLeast(8, 4)}? QUALIFY expression
    ;

procedureAnalyseClause
    : {atMost(5, 7)}? PROCEDURE ANALYSE
      '('
        analyseParameter? (',' analyseParameter)?
      ')'
    ;

analyseParameter
    : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
    ;

windowDefinition
    : uid AS window_specification
    ;

groupByItem
    : expression ({atMost(5, 7)}? order=(ASC | DESC))?
    ;

limitClause
    : LIMIT
    (
      (offset=limitClauseAtom ',')? limit=limitClauseAtom
      | limit=limitClauseAtom OFFSET offset=limitClauseAtom
    )
    ;

limitClauseAtom
	: unsignedDecimalIntegerLiteral | simpleId | PARAM_MARKER
	;


// Transaction's Statements

startTransaction
    : START TRANSACTION {isStartTransactionModeListAllowed()}?
      (transactionMode (',' transactionMode)*)?
    ;

beginWork
    : BEGIN WORK?
    ;

commitWork
    : COMMIT {isCompletionTypeAllowed()}? WORK?
      (AND (NO CHAIN (NO? RELEASE)? | CHAIN (NO RELEASE)?) | NO? RELEASE)?
    ;

rollbackWork
    : ROLLBACK {isCompletionTypeAllowed()}? WORK?
      (AND (NO CHAIN (NO? RELEASE)? | CHAIN (NO RELEASE)?) | NO? RELEASE)?
    ;

savepointStatement
    : SAVEPOINT uid
    ;

rollbackStatement
    : ROLLBACK WORK? TO SAVEPOINT? uid
    ;

releaseStatement
    : RELEASE SAVEPOINT uid
    ;

lockTables
    : LOCK (TABLE | TABLES) lockTableElement (',' lockTableElement)*
    ;

unlockTables
    : UNLOCK (TABLES | TABLE)
    ;

lockInstance
    : {atLeast(8, 0)}? LOCK INSTANCE FOR BACKUP
    ;

unlockInstance
    : {atLeast(8, 0)}? UNLOCK INSTANCE
    ;


// details

setAutocommitStatement
    : SET AUTOCOMMIT '=' autocommitValue=('0' | '1')
    ;

setTransactionStatement
    : SET transactionContext=(GLOBAL | SESSION)? TRANSACTION {isSetTransactionOptionListAllowed()}?
      (
        ISOLATION LEVEL transactionLevel (',' transactionAccessMode)?
        | transactionAccessMode (',' ISOLATION LEVEL transactionLevel)?
      )
    ;

transactionMode
    : WITH CONSISTENT SNAPSHOT
    | READ WRITE
    | READ ONLY
    ;

transactionAccessMode
    : READ WRITE
    | READ ONLY
    ;

lockTableElement
    : tableName (AS? uid | {atMost(5, 7)}? '=' uid)? lockAction
    ;

lockAction
    : READ LOCAL? | ({atMost(8, 0)}? LOW_PRIORITY)? WRITE
    ;

transactionLevel
    : REPEATABLE READ
    | READ COMMITTED
    | READ UNCOMMITTED
    | SERIALIZABLE
    ;


// Replication's Statements

//    Base Replication

changeMaster
    : {atMost(8, 0)}? CHANGE MASTER TO
      masterOption (',' masterOption)* ({atLeast(5, 7)}? channelOption)?
    ;

changeReplicationSource
    : {atLeast(8, 0)}? CHANGE REPLICATION SOURCE TO
      sourceOption (',' sourceOption)* channelOption?
    ;

changeReplicationFilter
    : {atLeast(5, 7)}? CHANGE REPLICATION FILTER
      replicationFilter (',' replicationFilter)*
      ({atLeast(8, 0)}? channelOption)?
    ;

purgeBinaryLogs
    : PURGE (purgeFormat=BINARY | {atMost(8, 0)}? purgeFormat=MASTER) LOGS
       (
           TO fileName=textLiteralToken
           | BEFORE (timeValue=textLiteralToken | expression)
       )
    ;

resetMaster
    : {atMost(8, 0)}? RESET MASTER
      ({between(8, 0, 8, 0)}? TO unsignedIntegerLiteral)?
    ;

resetSlave
    : {atMost(8, 0)}? RESET SLAVE ALL? ({atLeast(5, 7)}? channelOption)?
    ;
resetReplica
    : {atLeast(8, 0)}? RESET REPLICA ALL? channelOption?
    ;

resetBinaryLogsAndGtids
    : {atLeast(8, 4)}? RESET BINARY LOGS AND GTIDS
      (TO unsignedIntegerLiteral)?
    ;

startSlave
    : {atMost(8, 0)}? START SLAVE (threadType (',' threadType)*)?
      (UNTIL untilOption)?
      connectionOption* ({atLeast(5, 7)}? channelOption)?
    ;

startReplica
    : {atLeast(8, 0)}? START REPLICA (threadType (',' threadType)*)?
      (UNTIL untilOption)?
      connectionOption* channelOption?
    ;

stopSlave
    : {atMost(8, 0)}? STOP SLAVE (threadType (',' threadType)*)?
      ({atLeast(5, 7)}? channelOption)?
    ;

stopReplica
    : {atLeast(8, 0)}? STOP REPLICA (threadType (',' threadType)*)?
      channelOption?
    ;

startGroupReplication
    : {atLeastExact(50706)}? START GROUP_REPLICATION
      ({atLeastExact(80021)}? groupReplicationStartOption (',' groupReplicationStartOption)*)?
    ;

stopGroupReplication
    : {atLeastExact(50706)}? STOP GROUP_REPLICATION
    ;

groupReplicationStartOption
    : USER '=' textLiteralToken
    | PASSWORD '=' textLiteralToken
    | DEFAULT_AUTH '=' textLiteralToken
    ;

// details

masterOption
    : stringMasterOption '=' textLiteralToken                         #masterStringOption
    | decimalMasterOption '=' decimalLiteral                        #masterDecimalOption
    | boolMasterOption '=' boolVal=('0' | '1')                      #masterBoolOption
    | MASTER_HEARTBEAT_PERIOD '=' REAL_LITERAL                      #masterRealOption
    | IGNORE_SERVER_IDS '='
      '(' (unsignedIntegerLiteral (',' unsignedIntegerLiteral)*)? ')' #masterUidListOption
    ;

stringMasterOption
    : MASTER_BIND | MASTER_HOST | MASTER_USER | MASTER_PASSWORD
    | MASTER_LOG_FILE | RELAY_LOG_FILE | MASTER_SSL_CA
    | MASTER_SSL_CAPATH | MASTER_SSL_CERT | MASTER_SSL_CRL
    | MASTER_SSL_CRLPATH | MASTER_SSL_KEY | MASTER_SSL_CIPHER
    | {atLeast(5, 7)}? MASTER_TLS_VERSION
    ;
decimalMasterOption
    : MASTER_PORT | MASTER_CONNECT_RETRY | MASTER_RETRY_COUNT
    | MASTER_DELAY | MASTER_LOG_POS | RELAY_LOG_POS
    ;

boolMasterOption
    : MASTER_AUTO_POSITION | MASTER_SSL
    | MASTER_SSL_VERIFY_SERVER_CERT
    ;

sourceOption
    : {isReplicationSourceOption()}? sourceOptionName '=' sourceOptionValue
    ;

sourceOptionName
    : uid
    | MASTER_BIND
    | MASTER_SSL_VERIFY_SERVER_CERT
    ;

sourceOptionValue
    : sourceOptionValueAtom
    | '(' (sourceOptionValueAtom (',' sourceOptionValueAtom)*)? ')'
    ;

sourceOptionValueAtom
    : stringLiteral
    | decimalLiteral
    | userName
    | NULL_LITERAL
    | LOCAL
    | ON
    | OFF
    | GENERATE
    | STREAM
    ;

channelOption
    : FOR CHANNEL textLiteralToken
    ;

replicationFilter
    : REPLICATE_DO_DB '=' '(' uidList? ')'                          #doDbReplication
    | REPLICATE_IGNORE_DB '=' '(' uidList? ')'                      #ignoreDbReplication
    | REPLICATE_DO_TABLE '=' '(' tables? ')'                        #doTableReplication
    | REPLICATE_IGNORE_TABLE '=' '(' tables? ')'                    #ignoreTableReplication
    | REPLICATE_WILD_DO_TABLE '=' '(' simpleStrings? ')'            #wildDoTableReplication
    | REPLICATE_WILD_IGNORE_TABLE
       '=' '(' simpleStrings? ')'                                   #wildIgnoreTableReplication
    | REPLICATE_REWRITE_DB '='
      '(' (tablePair (',' tablePair)*)? ')'                         #rewriteDbReplication
    ;

tablePair
    : '(' firstTable=tableName ',' secondTable=tableName ')'
    ;

threadType
    : IO_THREAD | SQL_THREAD
    ;

untilOption
    : gtids=(SQL_BEFORE_GTIDS | SQL_AFTER_GTIDS)
      '=' gtuidSet                                                  #gtidsUntilOption
    | MASTER_LOG_FILE '=' textLiteralToken
      ',' MASTER_LOG_POS '=' decimalLiteral                         #masterLogUntilOption
    | SOURCE_LOG_FILE '=' textLiteralToken
      ',' SOURCE_LOG_POS '=' decimalLiteral                         #sourceLogUntilOption
    | RELAY_LOG_FILE '=' textLiteralToken
      ',' RELAY_LOG_POS '=' decimalLiteral                          #relayLogUntilOption
    | SQL_AFTER_MTS_GAPS                                            #sqlGapsUntilOption
    ;

connectionOption
    : USER '=' conOptUser=textLiteralToken                            #userConnectionOption
    | PASSWORD '=' conOptPassword=textLiteralToken                    #passwordConnectionOption
    | DEFAULT_AUTH '=' conOptDefAuth=textLiteralToken                 #defaultAuthConnectionOption
    | PLUGIN_DIR '=' conOptPluginDir=textLiteralToken                 #pluginDirConnectionOption
    ;

gtuidSet
    : uuidSet (',' uuidSet)*
    | textLiteralToken
    ;


//    XA Transactions

xaStartTransaction
    : XA xaStart=(START | BEGIN) xid xaAction=(JOIN | RESUME)?
    ;

xaEndTransaction
    : XA END xid (SUSPEND (FOR MIGRATE)?)?
    ;

xaPrepareStatement
    : XA PREPARE xid
    ;

xaCommitWork
    : XA COMMIT xid (ONE PHASE)?
    ;

xaRollbackWork
    : XA ROLLBACK xid
    ;

xaRecoverWork
    : XA RECOVER {isXaRecoverClauseAllowed()}?
      ({atLeast(5, 7)}? CONVERT {isXidToken()}? uid)?
    ;


// Prepared Statements

prepareStatement
    : PREPARE uid FROM
      (query=textLiteralToken | variable=LOCAL_ID)
    ;

executeStatement
    : EXECUTE uid (USING userVariables)?
    ;

deallocatePrepare
    : dropFormat=(DEALLOCATE | DROP) PREPARE uid
    ;


// Compound Statements

routineBody
    : blockStatement
    | caseStatement
    | ifStatement
    | loopStatement
    | repeatStatement
    | whileStatement
    | sqlStatement
    ;

// details

blockStatement
    : (labelUid ':')? BEGIN
      (
        ((declareVariable | declareCondition | declareCursor | declareHandler) SEMI)*
        procedureSqlStatement*
      )
      END labelUid?
    ;

caseStatement
    : CASE (uid | expression)? caseAlternative+
      (ELSE procedureSqlStatement+)?
      END CASE
    ;

ifStatement
    : IF expression
      THEN thenStatements+=procedureSqlStatement+
      elifAlternative*
      (ELSE elseStatements+=procedureSqlStatement+ )?
      END IF
    ;

iterateStatement
    : ITERATE labelUid
    ;

leaveStatement
    : LEAVE labelUid
    ;

loopStatement
    : (labelUid ':')?
      LOOP procedureSqlStatement+
      END LOOP labelUid?
    ;

repeatStatement
    : (labelUid ':')?
      REPEAT procedureSqlStatement+
      UNTIL expression
      END REPEAT labelUid?
    ;

returnStatement
    : RETURN expression
    ;

whileStatement
    : (labelUid ':')?
      WHILE expression
      DO procedureSqlStatement+
      END WHILE labelUid?
    ;

cursorStatement
    : CLOSE uid                                                     #CloseCursor
    | FETCH (NEXT? FROM)? uid INTO uidList                          #FetchCursor
    | OPEN uid                                                      #OpenCursor
    ;

// details

declareVariable
    : DECLARE uidList dataType (DEFAULT expression)?
    ;

declareCondition
    : DECLARE uid CONDITION FOR
      ( decimalLiteral | SQLSTATE VALUE? textLiteralToken)
    ;

declareCursor
    : DECLARE uid CURSOR FOR (withSelectStatement | selectStatement)
    ;

declareHandler
    : DECLARE handlerAction=(CONTINUE | EXIT)
      HANDLER FOR
      handlerConditionValue (',' handlerConditionValue)*
      (compoundStatement | sqlStatement)
    ;

handlerConditionValue
    : decimalLiteral                                                #handlerConditionCode
    | SQLSTATE VALUE? textLiteralToken                                #handlerConditionState
    | uid                                                           #handlerConditionName
    | SQLWARNING                                                    #handlerConditionWarning
    | NOT FOUND                                                     #handlerConditionNotfound
    | SQLEXCEPTION                                                  #handlerConditionException
    ;

procedureSqlStatement
    : (compoundStatement | sqlStatement) SEMI
    ;

caseAlternative
    : WHEN (constant | expression)
      THEN procedureSqlStatement+
    ;

elifAlternative
    : ELSEIF expression
      THEN procedureSqlStatement+
    ;

// Administration Statements

//    Account management statements

alterUser
    : ALTER USER
      userSpecification (',' userSpecification)*                    #alterUserMysqlV56
    | {atLeast(5, 7)}? ALTER USER ifExists? (USER '(' ')' | CURRENT_USER ('(' ')')?)
        IDENTIFIED BY (textLiteralToken | {atLeastExact(80018)}? RANDOM PASSWORD)
        (REPLACE textLiteralToken)?
        (RETAIN CURRENT PASSWORD)?                                  #alterUserCurrentUser
    | {atLeast(8, 0)}? ALTER USER ifExists? (USER '(' ')' | CURRENT_USER ('(' ')')?)
        DISCARD OLD PASSWORD                                        #alterUserCurrentUserDiscard
    | {atLeast(8, 0)}? ALTER USER ifExists? userName
        alterUserDefaultRoleClause                                  #alterUserDefaultRole
    | {atLeast(8, 0)}? ALTER USER ifExists? userName
        DISCARD OLD PASSWORD                                        #alterUserDiscardOldPassword
    | {atLeastExact(80027)}? ALTER USER ifExists?
        (userName | USER '(' ')' | CURRENT_USER ('(' ')')?)
        alterUserMfaAction                                          #alterUserMfa
    | {atLeast(5, 7)}? ALTER USER ifExists?
        alterUserAuthOption (',' alterUserAuthOption)*
        (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
        (WITH userResourceOption+)?
        (userPasswordOption | userLockOption)*
        accountAttributeOption?                                     #alterUserMysqlV57
    ;

alterUserMfaAction
    : ADD authFactor mfaIdentification
      (ADD authFactor mfaIdentification)?
    | MODIFY authFactor mfaIdentification
      (MODIFY authFactor mfaIdentification)?
    | DROP authFactor (DROP authFactor)?
    | authFactor INITIATE REGISTRATION
    | authFactor UNREGISTER
    | authFactor FINISH REGISTRATION SET CHALLENGE_RESPONSE AS
      (textLiteralToken | hexadecimalLiteral)
    ;

authFactor
    : decimalLiteral FACTOR
    ;

mfaIdentification
    : IDENTIFIED BY (textLiteralToken | RANDOM PASSWORD)
    | IDENTIFIED WITH authenticationRule
    ;

createUser
    : {atMost(5, 6)}? CREATE USER userAuthOption (',' userAuthOption)*
    | {atLeast(5, 7)}? CREATE USER ifNotExists?
        createUserAuthOption (',' createUserAuthOption)*
        defaultRoleClause?
        (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
        (WITH userResourceOption+)?
        (userPasswordOption | userLockOption)*
        accountAttributeOption?                                    // #createUserMysqlV57
    ;

createUserAuthOption
    : userAuthOption
      ({atLeastExact(80027)}? AND mfaIdentification (AND mfaIdentification)?)?
    ;

dropUser
    : DROP USER ({atLeast(5, 7)}? ifExists)? accountTarget (',' accountTarget)*
    ;

dropRole
    : {atLeast(8, 0)}? DROP ROLE ifExists? roleName(',' roleName)*
    ;

grantStatement
    : GRANT privelegeClause (',' privelegeClause)*
      ON
      privilegeObject=privilegeObjectType?
      privilegeLevel
      TO grantUser (',' grantUser)*
      (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
      (WITH (GRANT OPTION | userResourceOption)* )?
      ({atLeast(8, 0)}? AS grantAsUser (WITH ROLE roleOption)?)?
    | {atLeast(8, 0)}? GRANT roleName (',' roleName)*
      TO (accountTarget | uid) (',' (accountTarget | uid))*
      (WITH ADMIN OPTION)?
    ;

roleOption
    : DEFAULT
    | NONE
    | ALL (EXCEPT userName (',' userName)*)?
    | userName (',' userName)*
    ;

grantProxy
    : GRANT PROXY ON fromFirst=userName
      TO toFirst=userName (',' toOther+=userName)*
      (WITH GRANT OPTION)?
    ;

renameUser
    : RENAME USER
      renameUserClause (',' renameUserClause)*
    ;

revokeStatement
    : REVOKE ({atLeast(8, 0)}? ifExists)? privelegeClause (',' privelegeClause)*
      ON
      privilegeObject=privilegeObjectType?
      privilegeLevel
      FROM accountTarget (',' accountTarget)*
      ignoreUnknownUser?                                            //#detailRevoke
    | REVOKE ({atLeast(8, 0)}? ifExists)? ALL PRIVILEGES? ',' GRANT OPTION
      FROM accountTarget (',' accountTarget)*
      ignoreUnknownUser?                                            //#shortRevoke
    | {atLeast(8, 0)}? REVOKE ({atLeast(8, 0)}? ifExists)? roleName (',' roleName)*
      FROM (accountTarget | uid) (',' (accountTarget | uid))*
      ignoreUnknownUser?                                           // #roleRevoke
    ;

revokeProxy
    : REVOKE ({atLeast(8, 0)}? ifExists)? PROXY ON onUser=userName
      FROM fromFirst=userName (',' fromOther+=userName)*
      ignoreUnknownUser?
    ;

privilegeObjectType
    : TABLE
    | FUNCTION
    | PROCEDURE
    | {atLeast(9, 7)}? LIBRARY
    ;

grantAsUser
    : userName
    | CURRENT_USER ('(' ')')?
    ;

grantUser
    : {atMost(5, 7)}? userAuthOption
    | {atMost(5, 7)}? currentUserGrantAuthOption
    | accountTarget
    ;

currentUserGrantAuthOption
    : CURRENT_USER ('(' ')')? IDENTIFIED BY textLiteralToken
    ;

accountTarget
    : userName
    | CURRENT_USER ('(' ')')?
    ;

ignoreUnknownUser
    : {atLeast(8, 0)}? IGNORE UNKNOWN USER
    ;

setPasswordStatement
    : SET PASSWORD
      (
        '=' (passwordFunctionClause | textLiteralToken)
        ({atLeast(8, 0)}? REPLACE textLiteralToken)?
      | {atMost(5, 6) || atLeast(8, 0)}? TO RANDOM
        ({atMost(5, 6) || atLeast(8, 0)}? REPLACE textLiteralToken)?
      | FOR accountTarget
        (
          '=' (passwordFunctionClause | textLiteralToken)
          ({atLeast(8, 0)}? REPLACE textLiteralToken)?
        | {atLeast(8, 0)}? TO RANDOM
          (REPLACE textLiteralToken)?
        )
      )
      (RETAIN CURRENT PASSWORD)?
    ;

// details

userSpecification
    : userName userPasswordOption
    ;

userAuthOption
    : {atMost(5, 7)}? userName IDENTIFIED BY PASSWORD hashed=textLiteralToken #hashAuthOption
    | userName
      IDENTIFIED BY (password=textLiteralToken | {atLeast(8, 0)}? RANDOM PASSWORD) #stringAuthOption
    | userName
      IDENTIFIED (WITH | VIA)                                       // VIA and OR are MariaDB only
      authenticationRule (OR authenticationRule)*
      initialAuthentication?                                        #moduleAuthOption
    | userName                                                      #simpleAuthOption
    ;

alterUserAuthOption
    : userAuthOption
      ({atLeast(8, 0)}? REPLACE textLiteralToken)?
      ({atLeastExact(80014)}? RETAIN CURRENT PASSWORD)?
    | {atLeast(8, 0)}? userName DISCARD OLD PASSWORD
    ;

authenticationRule
    : authPlugin
      (
        BY (textLiteralToken | {atLeast(8, 0)}? RANDOM PASSWORD)
        | USING textLiteralToken
        | AS (textLiteralToken | hexadecimalLiteral)
      )?                                                            #module
    | authPlugin
      (USING | AS) passwordFunctionClause                           #passwordModuleOption // MariaDB
    ;

initialAuthentication
    : {atLeastExact(80027)}? INITIAL AUTHENTICATION IDENTIFIED
      (
        BY (RANDOM PASSWORD | textLiteralToken)
        | WITH authPlugin AS (textLiteralToken | hexadecimalLiteral)
      )
    ;

defaultRoleClause
    : {atLeast(8, 0)}? DEFAULT ROLE roleName (',' roleName)*
    ;

alterUserDefaultRoleClause
    : DEFAULT ROLE (ALL | NONE | roleName (',' roleName)*)
    ;

tlsOption
    : SSL
    | X509
    | CIPHER textLiteralToken
    | ISSUER textLiteralToken
    | SUBJECT textLiteralToken
    ;

userResourceOption
    : MAX_QUERIES_PER_HOUR decimalLiteral
    | MAX_UPDATES_PER_HOUR decimalLiteral
    | MAX_CONNECTIONS_PER_HOUR decimalLiteral
    | MAX_USER_CONNECTIONS decimalLiteral
    ;

userPasswordOption
    : PASSWORD EXPIRE
      ({atLeast(5, 7)}? (expireType=DEFAULT
        | expireType=NEVER
        | expireType=INTERVAL decimalLiteral DAY
      ))?
    | {atLeast(8, 0)}? PASSWORD HISTORY (DEFAULT | decimalLiteral)
    | {atLeast(8, 0)}? PASSWORD REUSE INTERVAL (DEFAULT | decimalLiteral DAY)
    | {atLeast(8, 0)}? PASSWORD REQUIRE CURRENT (OPTIONAL | DEFAULT)?
    | {atLeast(8, 0)}? FAILED_LOGIN_ATTEMPTS decimalLiteral
    | {atLeast(8, 0)}? PASSWORD_LOCK_TIME (decimalLiteral | UNBOUNDED)
    ;

userLockOption
    : {atLeast(5, 7)}? ACCOUNT lockType=(LOCK | UNLOCK)
    ;

accountAttributeOption
    : {atLeast(8, 0)}? (COMMENT | ATTRIBUTE) textLiteralToken
    ;

privelegeClause
    : {isPrivilegeClauseAllowed()}? privilege ( '(' uidList ')' )?
    ;

privilege
    : ALL PRIVILEGES?
    | ALTER ROUTINE?
    | CREATE
      (TEMPORARY TABLES | ROUTINE | VIEW | USER | TABLESPACE)?
    | {atLeast(8, 0)}? CREATE ROLE
    | DELETE | DROP | {atLeast(8, 0)}? DROP ROLE | EVENT | EXECUTE | FILE | GRANT OPTION
    | INDEX | INSERT | LOCK TABLES | PROCESS | PROXY
    | REFERENCES | RELOAD
    | REPLICATION (CLIENT | SLAVE)
    | SELECT
    | SHOW (VIEW | DATABASES)
    | SHUTDOWN | SUPER | TRIGGER | UPDATE | USAGE
    | dynamicPrivilege
    ;

dynamicPrivilege
    : {isDynamicPrivilege()}? uid
    ;

privilegeLevel
    : '*'                                                           #currentSchemaPriviLevel
    | '*' '.' '*'                                                   #globalPrivLevel
    | uid '.' '*'                                                   #definiteSchemaPrivLevel
    | uid '.' uid                                                   #definiteFullTablePrivLevel
    | uid dottedId                                                  #definiteFullTablePrivLevel2
    | uid                                                           #definiteTablePrivLevel
    ;

renameUserClause
    : fromFirst=accountTarget TO toFirst=userName
    ;

//    Table maintenance statements

analyzeTable
    : ANALYZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      (TABLE | TABLES) tableName analyzeHistogramClause
    | ANALYZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      (TABLE | TABLES) tables
    ;

analyzeHistogramClause
    : {atLeast(8, 0)}? UPDATE HISTOGRAM ON uidList
      (
        {atLeastExact(80031)}? USING DATA textLiteralToken
        | (WITH decimalLiteral BUCKETS)?
          ({atLeast(8, 4)}? (MANUAL | AUTO) UPDATE)?
      )
    | {atLeast(8, 0)}? DROP HISTOGRAM ON uidList
    ;

checkTable
    : CHECK (TABLE | TABLES) tables checkTableOption*
    ;

checksumTable
    : CHECKSUM (TABLE | TABLES) tables actionOption=(QUICK | EXTENDED)?
    ;

optimizeTable
    : OPTIMIZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      (TABLE | TABLES) tables
    ;

repairTable
    : REPAIR actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      (TABLE | TABLES) tables
      (QUICK | EXTENDED | USE_FRM)*
    ;

cloneStatement
    : {atLeast(8, 0)}? CLONE LOCAL DATA DIRECTORY '='? stringLiteral
    | {atLeast(8, 0)}? CLONE INSTANCE FROM userName ':' decimalLiteral
      IDENTIFIED BY stringLiteral cloneDataDirectory? cloneSslOption?
    ;

cloneDataDirectory
    : DATA DIRECTORY '='? stringLiteral
    ;

cloneSslOption
    : REQUIRE NO? SSL
    ;

// details

checkTableOption
    : FOR UPGRADE | QUICK | FAST | MEDIUM | EXTENDED | CHANGED
    ;


//    Plugin and udf statements

createUdfFunction
    : CREATE AGGREGATE? FUNCTION ({atLeastExact(80029)}? ifNotExists)? uid
      RETURNS returnType=(STRING | INTEGER | REAL | DECIMAL)
      SONAME textLiteralToken
    ;

installPlugin
    : INSTALL PLUGIN uid SONAME textLiteralToken
    ;

uninstallPlugin
    : UNINSTALL PLUGIN uid
    ;

installComponent
    : {atLeast(8, 0)}? INSTALL COMPONENT componentNameList installComponentSetClause?
    ;

uninstallComponent
    : {atLeast(8, 0)}? UNINSTALL COMPONENT componentNameList
    ;

componentNameList
    : textLiteralToken (',' textLiteralToken)*
    ;

installComponentSetClause
    : {atLeastExact(80033)}? SET installComponentSetValue (',' installComponentSetValue)*
    ;

installComponentSetValue
    : installComponentSetScope? fullColumnName '=' installComponentSetRvalue
    ;

installComponentSetScope
    : GLOBAL
    | PERSIST
    ;

installComponentSetRvalue
    : expression
    | ON
    ;

//    Resource group statements

createResourceGroup
    : {atLeast(8, 0)}? CREATE RESOURCE GROUP uid TYPE '='? resourceGroupType
      resourceGroupVcpuOption?
      resourceGroupThreadPriorityOption?
      resourceGroupState?
    ;

alterResourceGroup
    : {atLeast(8, 0)}? ALTER RESOURCE GROUP uid
      resourceGroupVcpuOption?
      resourceGroupThreadPriorityOption?
      resourceGroupAlterState?
      FORCE?
    ;

dropResourceGroup
    : {atLeast(8, 0)}? DROP RESOURCE GROUP uid FORCE?
    ;

setResourceGroup
    : {atLeast(8, 0) || atMost(5, 6)}? SET RESOURCE GROUP uid
      (FOR decimalLiteral (',' decimalLiteral)*)?
    ;

resourceGroupType
    : SYSTEM | USER
    ;

resourceGroupVcpuOption
    : VCPU '='? resourceGroupVcpuSpec (',' resourceGroupVcpuSpec)*
    ;

resourceGroupVcpuSpec
    : decimalLiteral (MINUS decimalLiteral)?
    ;

resourceGroupThreadPriorityOption
    : THREAD_PRIORITY '='? MINUS? decimalLiteral
    ;

resourceGroupState
    : ENABLE | DISABLE
    ;

resourceGroupAlterState
    : ENABLE | DISABLE
    ;


//    Set and show statements

setStatement
    : setPasswordStatement                                          #setPassword
    | SET setVariableAssignment (',' setVariableAssignment)*        #setVariable
    | SET (CHARACTER SET | CHARSET) (charsetName | DEFAULT)         #setCharset
    | SET NAMES
        (charsetName (COLLATE collationName)? | DEFAULT)            #setNames
    | setTransactionStatement                                       #setTransaction
    | setAutocommitStatement                                        #setAutocommit
    | {atLeast(8, 0) || isLegacySetRoleAssignment()}?
      SET ROLE roleOption                                           #setRole
    | {atLeast(8, 0)}? SET DEFAULT ROLE roleOption TO userName (',' userName)* #setDefaultRole
    | SET fullId ('=' | ':=') expression
      (',' fullId ('=' | ':=') expression)*                         #setNewValueInsideTrigger
    ;

setVariableAssignment
    : variableClause {isSetVariableAssignmentAllowed($variableClause.ctx)}?
      ('=' | ':=') (expression | DEFAULT | ON)
    ;

showStatement
    : SHOW (logFormat=BINARY | {atMost(8, 0)}? logFormat=MASTER) LOGS #showMasterLogs
    | {atLeast(8, 4)}? SHOW BINARY LOG STATUS                        #showBinaryLogStatus
    | SHOW CHARSET showFilter?                                       #showCharset
    | SHOW BINLOG EVENTS showLogEventOptions                        #showBinlogEvents
    | SHOW RELAYLOG EVENTS showLogEventOptions
      ({atLeast(5, 7)}? channelOption)?                              #showRelayLogEvents
    | SHOW showCommonEntity showFilter?                             #showObjectFilter
    | SHOW ({atLeast(8, 0)}? EXTENDED)? FULL?
      columnsFormat=(COLUMNS | FIELDS)
      tableFormat=(FROM | IN) tableName
        (schemaFormat=(FROM | IN) uid)? showFilter?                 #showColumns
    | SHOW FULL TABLES
      schemaFormat=(FROM | IN) uid
        (WHERE TABLE_TYPE comparisonOperator (uid | textLiteralToken) )?                                         #showTables
    | SHOW CREATE schemaFormat=(DATABASE | SCHEMA)
      ifNotExists? uid                                              #showCreateDb
    | SHOW CREATE
        (namedEntity=(EVENT | FUNCTION | PROCEDURE | TABLE | TRIGGER | VIEW)
          | {atLeast(9, 7)}? namedEntity=LIBRARY)
        fullId                                                      #showCreateFullIdObject
    | {atLeast(9, 7)}? SHOW CREATE MASKING POLICY uid                #showCreateMaskingPolicy
    | {atLeast(5, 7)}? SHOW CREATE USER (userName | CURRENT_USER ('(' ')')?) #showCreateUser
    | SHOW ENGINE
      (engineName | ALL) engineOption=(STATUS | MUTEX | LOGS)       #showEngine
    | SHOW STORAGE? ENGINES                                         #showEngines
    | {atMost(8, 0)}? SHOW MASTER STATUS                             #showStatus
    | SHOW PLUGINS                                                  #showPlugins
    | SHOW PRIVILEGES                                               #showPrivileges
    | SHOW FULL? PROCESSLIST                                        #showProcessList
    | SHOW PROFILES                                                 #showProfiles
    | {atMost(8, 0)}? SHOW SLAVE HOSTS                               #showSlaveHosts
    | SHOW errorFormat=(ERRORS | WARNINGS)
        limitClause?                                                #showErrors
    | SHOW COUNT '(' '*' ')' errorFormat=(ERRORS | WARNINGS)        #showCountErrors
    | SHOW showSchemaEntity
        (schemaFormat=(FROM | IN) uid)? showFilter?                 #showSchemaFilter
    | SHOW routine=(FUNCTION | PROCEDURE) CODE fullId               #showRoutine
    | {atLeast(9, 7)}? SHOW LIBRARY STATUS showFilter?               #showLibraryStatus
    | SHOW GRANTS
      (
        FOR (userName | CURRENT_USER ('(' ')')?)
        ({atLeast(8, 0)}? USING roleName (',' roleName)*)?
      )?                                                            #showGrants
    | SHOW ({atLeast(8, 0)}? EXTENDED)?
      indexFormat=(INDEX | INDEXES | KEYS)
      tableFormat=(FROM | IN) tableName
        (schemaFormat=(FROM | IN) uid)? (WHERE expression)?         #showIndexes
    | SHOW OPEN TABLES ( schemaFormat=(FROM | IN) uid)?
      showFilter?                                                   #showOpenTables
    | SHOW PROFILE (showProfileType (',' showProfileType)*)?
        (FOR QUERY queryCount=profileQueryIdLiteral)?
        limitClause?                                                #showProfile
    | {atMost(8, 0)}? SHOW SLAVE STATUS
      ({atLeast(5, 7)}? channelOption)?                              #showSlaveStatus
    | {atLeast(8, 4)}? SHOW PARSE_TREE sqlStatement                  #showParseTree
    | {atLeast(8, 0)}? SHOW REPLICA STATUS (FOR CHANNEL textLiteralToken)? #showReplicaStatus
    | {atLeast(8, 0)}? SHOW REPLICAS                                 #showReplicas
    ;

// details

showLogEventOptions
    : (IN filename=textLiteralToken)?
      (FROM fromPosition=decimalLiteral)?
      limitClause?
    ;

variableClause
    : LOCAL_ID
    | GLOBAL_ID
    | (('@' '@')? (GLOBAL | SESSION | LOCAL))? uid
    | {atMost(5, 7)}? (GLOBAL | SESSION | LOCAL | persistScope)
    | {atMost(5, 7)}? CUBE
    | {isBarePersistScopeAllowed()}? persistScope uid
    | '@' '@' persistScope '.' uid
    ;

persistScope
    : PERSIST
    | {isPersistOnlyToken()}? ID
    ;

showCommonEntity
    : CHARACTER SET | COLLATION | DATABASES | SCHEMAS
    | FUNCTION STATUS | PROCEDURE STATUS
    | (GLOBAL | SESSION | LOCAL)? (STATUS | VARIABLES)
    ;

showFilter
    : LIKE textLiteralToken
    | WHERE expression
    ;

showGlobalInfoClause
    : STORAGE? ENGINES | MASTER STATUS | PLUGINS
    | PRIVILEGES | FULL? PROCESSLIST | PROFILES
    | SLAVE HOSTS
    ;

showSchemaEntity
    : EVENTS | TABLE STATUS | FULL? (TABLES | TRIGGERS)
    ;

showProfileType
    : ALL | BLOCK IO | CONTEXT SWITCHES | CPU | IPC | MEMORY
    | PAGE FAULTS | SOURCE | SWAPS
    ;


//    Other administrative statements

binlogStatement
    : BINLOG textLiteralToken
    ;

cacheIndexStatement
    : CACHE INDEX
      (tableName adminPartition cacheKeyList? | tableIndexes (',' tableIndexes)*)
      IN keyCacheName
    ;

flushStatement
    : FLUSH flushFormat=(NO_WRITE_TO_BINLOG | LOCAL)?
      (flushOption (',' flushOption)* | flushTablesOption)
    ;

killStatement
    : KILL connectionFormat=(CONNECTION | QUERY)?
      expression
    ;

loadIndexIntoCache
    : LOAD INDEX INTO CACHE
      (
        tableName adminPartition cacheKeyList? (IGNORE LEAVES)?
        | loadedTableIndexes (',' loadedTableIndexes)*
      )
    ;

// remark reset (maser | slave) describe in replication's
//  statements section
resetStatement
    : {atMost(5, 7)}? RESET QUERY CACHE                             #resetQueryCache
    | {atLeast(8, 0)}? RESET PERSIST
      (IF EXISTS resetPersistVariable | resetPersistVariable)?      #resetPersist
    | RESET resetOption (',' resetOption)+                          #resetOptions
    ;

resetOption
    : {atMost(8, 0)}? MASTER
      ({between(8, 0, 8, 0)}? TO unsignedIntegerLiteral)?
    | {atMost(8, 0)}? SLAVE ALL?
      ({atLeast(5, 7)}? channelOption)?
    | {atLeast(8, 0)}? REPLICA ALL? channelOption?
    | {atLeast(8, 4)}? BINARY LOGS AND GTIDS
      (TO unsignedIntegerLiteral)?
    | {atMost(5, 7)}? QUERY CACHE
    ;

resetPersistVariable
    : fullId
    | DEFAULT '.' uid
    ;

restartStatement
    : {atLeast(8, 0)}? RESTART
    ;

shutdownStatement
    : {atLeast(5, 7)}? SHUTDOWN
    ;

// details

tableIndexes
    : tableName cacheKeyList?
    ;

flushOption
    : {atMost(5, 7)}? DES_KEY_FILE
    | {atMost(8, 0)}? HOSTS
    | (BINARY | ENGINE | ERROR | GENERAL | SLOW) LOGS
    | LOGS
    | {atLeast(5, 7)}? OPTIMIZER_COSTS
    | PRIVILEGES
    | {atMost(5, 7)}? QUERY CACHE
    | RELAY LOGS ({atLeast(5, 7)}? channelOption)?
    | STATUS
    | USER_RESOURCES
    ;

flushTablesOption
    : (TABLE | TABLES)
      (WITH READ LOCK | tables flushTableOption?)?
    ;

flushTableOption
    : WITH READ LOCK
    | FOR EXPORT
    ;

loadedTableIndexes
    : tableName
      cacheKeyList?
      (IGNORE LEAVES)?
    ;

adminPartition
    : PARTITION '(' (ALL | uid (',' uid)*) ')'
    ;

cacheKeyList
    : (INDEX | KEY) '(' cacheKeyNameList? ')'
    ;

cacheKeyNameList
    : cacheKeyName (',' cacheKeyName)*
    ;

cacheKeyName
    : uid
    | PRIMARY
    ;

keyCacheName
    : uid | DEFAULT
    ;


// Utility Statements


simpleDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC) tableName
      (column=uid | pattern=textLiteralToken)?
    ;

fullDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC)
      (
        {atMost(5, 7)}? legacyType=(EXTENDED | PARTITIONS)
        | {atLeast(8, 0)}? analyze=ANALYZE
      )?
      (FORMAT '=' formatValue=(TRADITIONAL | JSON | TREE))?
      ({atLeast(8, 4) && ($analyze == null || atLeast(9, 7))}? INTO LOCAL_ID)?
      ({atLeast(8, 4)}? FOR (DATABASE | SCHEMA) uid)?
      describeObjectClause
    ;

helpStatement
    : HELP (uid | textLiteralToken)
    ;

useStatement
    : USE uid
    ;

signalStatement
    : SIGNAL ( ( SQLSTATE VALUE? stringLiteral ) | ID | REVERSE_QUOTE_ID )
        ( SET signalConditionInformation ( ',' signalConditionInformation)* )?
    ;

resignalStatement
    : RESIGNAL ( ( SQLSTATE VALUE? stringLiteral ) | ID | REVERSE_QUOTE_ID )?
        ( SET signalConditionInformation ( ',' signalConditionInformation)* )?
    ;

signalConditionInformation
    : ( CLASS_ORIGIN
          | SUBCLASS_ORIGIN
          | MESSAGE_TEXT
          | MYSQL_ERRNO
          | CONSTRAINT_CATALOG
          | CONSTRAINT_SCHEMA
          | CONSTRAINT_NAME
          | CATALOG_NAME
          | SCHEMA_NAME
          | TABLE_NAME
          | COLUMN_NAME
          | CURSOR_NAME
        ) '=' signalAllowedExpression
    ;

signalAllowedExpression
    : stringLiteral
    | decimalLiteral
    | hexadecimalLiteral
    | bitStringLiteral
    | booleanLiteral
    | (DATE | TIME | TIMESTAMP) stringLiteral
    | NULL_LITERAL
    | mysqlVariable
    | fullColumnName
    ;

diagnosticsStatement
    : GET ( CURRENT | {atLeast(5, 7)}? STACKED )? DIAGNOSTICS (
          ( variableClause '=' ( NUMBER | ROW_COUNT ) ( ',' variableClause '=' ( NUMBER | ROW_COUNT ) )* )
        | ( CONDITION  ( decimalLiteral | variableClause ) variableClause '=' diagnosticsConditionInformationName ( ',' variableClause '=' diagnosticsConditionInformationName )* )
      )
    ;

diagnosticsConditionInformationName
    : CLASS_ORIGIN
    | SUBCLASS_ORIGIN
    | RETURNED_SQLSTATE
    | MESSAGE_TEXT
    | MYSQL_ERRNO
    | CONSTRAINT_CATALOG
    | CONSTRAINT_SCHEMA
    | CONSTRAINT_NAME
    | CATALOG_NAME
    | SCHEMA_NAME
    | TABLE_NAME
    | COLUMN_NAME
    | CURSOR_NAME
    ;

// details

describeObjectClause
    : (
        selectStatement | {atLeast(8, 0)}? withSelectStatement
        | deleteStatement | insertStatement
        | replaceStatement | updateStatement
      )                                                             #describeStatements
    | {atLeast(5, 7)}? FOR CONNECTION decimalLiteral                #describeConnection
    | {atLeast(8, 0)}? TABLE tableName                              #describeTable
    ;


// Common Clauses

//    DB Objects

fullId
    : uid
      ({isIdentifierAfterDotAhead()}? '.' identifierAfterDot=. | '.' uid)?
    ;

tableName
    : fullId
    | {atMost(5, 7)}? '.' delphiName=uid
    ;

procName
    : fullId
    ;

customFunctionName
    : fullId
    ;


roleName
    : userName
    | {atLeast(8, 0)}? (COMMIT | BINLOG) LOCAL_ID?
    | {atLeast(9, 7)}? (SETS | FILES | VECTOR) LOCAL_ID?
    ;

fullColumnName
    : uid (dottedId dottedId? )?
    | {isIdentifierBeforeDot()}? identifierBeforeDot=. dottedId dottedId?
    | {atMost(5, 7)}? '.' uid dottedId
    ;

indexColumnName
    : uid ('(' decimalLiteral ')')? sortType=(ASC | DESC)?
    | {atLeast(8, 0)}? '(' CAST '(' expression AS convertedDataType ARRAY ')' ')' sortType=(ASC | DESC)?
    | {atLeast(8, 0)}? '(' expression ')' sortType=(ASC | DESC)?
    ;

userName
    : user=userNameToken (host= LOCAL_ID)?;

userNameToken
    : textLiteralToken | ID | REVERSE_QUOTE_ID
    ;

mysqlVariable
    : LOCAL_ID
    | GLOBAL_ID
    ;

charsetName
    : BINARY
    | uid
    | textLiteralToken
    ;

collationName
    : uid | textLiteralToken;

engineName
    : ARCHIVE | BLACKHOLE | CSV | FEDERATED | INNODB | MEMORY
    | MERGE | MRG_MYISAM | MYISAM | NDB | NDBCLUSTER | PERFORMANCE_SCHEMA
    | TOKUDB
    | ID
    | REVERSE_QUOTE_ID
    | CONNECT
    ;

uuidSet
    : decimalLiteral '-' decimalLiteral '-' decimalLiteral
      '-' decimalLiteral '-' decimalLiteral
      (':' decimalLiteral '-' decimalLiteral)+
    ;

xid
    : globalTableUid=xuidStringId
      (
        ',' qualifier=xuidStringId
        (',' (decimalLiteral | hexadecimalLiteral))?
      )?
    ;

xuidStringId
    : textLiteralToken
    | BIT_STRING
    | HEXADECIMAL_LITERAL+
    ;

authPlugin
    : uid | textLiteralToken
    ;

uid
    : simpleId
    | {isBareCharsetIntroducerIdentifier()}? STRING_CHARSET_NAME
    | BINLOG
    | DOUBLE_QUOTE_ID
    | DOUBLE_QUOTE_AMBIGUOUS
    | REVERSE_QUOTE_ID
    | CHARSET_REVERSE_QOUTE_STRING
    ;

labelUid
    : {isLabelAllowed()}?
      (simpleId | ALWAYS | SECONDARY_LOAD | SECONDARY_UNLOAD)
    | {atMost(5, 7)}? CUBE
    | {atMost(8, 4)}? BINLOG
    | DOUBLE_QUOTE_ID
    | DOUBLE_QUOTE_AMBIGUOUS
    | REVERSE_QUOTE_ID
    | CHARSET_REVERSE_QOUTE_STRING
    ;

simpleId
    : {isSimpleIdentifierAllowed()}?
      (
        ID
        | charsetNameBase
        | transactionLevelBase
        | engineName
        | privilegesBase
        | intervalTypeBase
        | dataTypeBase
        | versionedKeywordCanBeId
        | keywordsCanBeId
        | functionNameBase
      )
    ;

versionedKeywordCanBeId
    : {atMost(5, 6)}? (GENERATED | OPTIMIZER_COSTS | STORED | VIRTUAL)
    | {atMost(5, 7)}? (INTERSECT | FUNCTION | ROW | ROWS)
    | {atMost(5, 7)}?
      (CUME_DIST | DENSE_RANK | EMPTY | EXCEPT | FIRST_VALUE | GROUPING | GROUPS
      | JSON_TABLE | LAG | LAST_VALUE | LATERAL | LEAD | NTH_VALUE | NTILE | OF
      | OVER | PERCENT_RANK | RANK | RECURSIVE | ROW_NUMBER | SYSTEM | WINDOW)
    | {atMost(8, 0)}? (MANUAL | PARALLEL | QUALIFY | TABLESAMPLE)
    | {atMost(8, 4)}? (EXTERNAL | LIBRARY)
    ;

dottedId
    : '.' (uid | {isIdentifierAfterDot()}? .)
    ;


//    Literals

decimalLiteral
    : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL | REAL_LITERAL
    ;

unsignedIntegerLiteral
    : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
    | HEXADECIMAL_LITERAL
    ;

unsignedDecimalIntegerLiteral
    : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
    ;

profileQueryIdLiteral
    : serverNumLiteral
    ;

temporalPrecisionLiteral
    : serverNumLiteral
    ;

serverNumLiteral
    : {isServerNum()}? unsignedDecimalIntegerLiteral
    ;

fileSizeLiteral
    : FILESIZE_LITERAL | decimalLiteral;

textLiteralToken
    : STRING_LITERAL | DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_QUOTE_AMBIGUOUS
    ;

textStringLiteral
    : textLiteralToken | hexadecimalLiteral | bitStringLiteral
    ;

stringLiteral
    : (
        STRING_CHARSET_NAME? textLiteralToken
        | START_NATIONAL_STRING_LITERAL
      ) textLiteralToken+
    | (
        STRING_CHARSET_NAME? textLiteralToken
        | START_NATIONAL_STRING_LITERAL
      ) (COLLATE collationName)?
    ;

booleanLiteral
    : TRUE | FALSE;

hexadecimalLiteral
    : STRING_CHARSET_NAME? HEXADECIMAL_LITERAL;

bitStringLiteral
    : STRING_CHARSET_NAME? BIT_STRING;

nullNotnull
    : NOT? (NULL_LITERAL | NULL_SPEC_LITERAL)
    ;

constant
    : stringLiteral | decimalLiteral
    | '-' decimalLiteral
    | hexadecimalLiteral | bitStringLiteral | booleanLiteral
    | REAL_LITERAL
    | NOT? nullLiteral=(NULL_LITERAL | NULL_SPEC_LITERAL)
    ;


//    Data Types

dataType
    : typeName=CHAR lengthOneDimension? BYTE                       #stringDataType
    | typeName=(CHAR | CHARACTER | NCHAR)
      VARYING lengthOneDimension?
      BINARY?
      stringCharsetAttribute?
      (COLLATE collationName | BINARY)?                             #stringDataType
    | typeName=(VARCHAR | NVARCHAR)
      lengthOneDimension?
      BINARY?
      stringCharsetAttribute?
      (COLLATE collationName | BINARY)?                             #stringDataType
    | typeName=(CHAR | CHARACTER | TEXT | NCHAR)
      lengthOneDimension? BINARY?
      stringCharsetAttribute?
      (COLLATE collationName | BINARY)?                             #stringDataType
    | typeName=(TINYTEXT | MEDIUMTEXT | LONGTEXT)
      BINARY?
      stringCharsetAttribute?
      (COLLATE collationName | BINARY)?                             #stringDataType
    | NATIONAL typeName=(VARCHAR | CHARACTER)
      lengthOneDimension? BINARY? (COLLATE collationName)?          #nationalStringDataType
    | NATIONAL typeName=CHAR
      lengthOneDimension? BINARY? (COLLATE collationName)?          #nationalStringDataType
    | NCHAR typeName=VARCHAR
      lengthOneDimension? BINARY? (COLLATE collationName)?          #nationalStringDataType
    | NATIONAL typeName=(CHAR | CHARACTER) VARYING
      lengthOneDimension? BINARY? (COLLATE collationName)?          #nationalVaryingStringDataType
    | typeName=(
        TINYINT | SMALLINT | MEDIUMINT | INT | INTEGER | BIGINT
        | MIDDLEINT | INT1 | INT2 | INT3 | INT4 | INT8
      )
      lengthOneDimension? numericFieldOption*                       #dimensionDataType
    | typeName=REAL
      lengthTwoDimension? numericFieldOption*                       #dimensionDataType
    | typeName=DOUBLE PRECISION?
      lengthTwoDimension? numericFieldOption*                       #dimensionDataType
    | typeName=(DECIMAL | DEC | FIXED | NUMERIC | FLOAT | FLOAT4 | FLOAT8)
      lengthTwoOptionalDimension? numericFieldOption*               #dimensionDataType
    | typeName=(
        DATE | TINYBLOB |  MEDIUMBLOB | LONGBLOB
        | BOOL | BOOLEAN | SERIAL
      )                                                             #simpleDataType
    | typeName=YEAR
      lengthOneDimension? numericFieldOption*                       #dimensionDataType
    | (typeName=(BIT | TIME | TIMESTAMP | DATETIME | BINARY | BLOB)
        | {atLeast(9, 7)}? typeName=VECTOR)
      lengthOneDimension?                                           #dimensionDataType
    | typeName=VARBINARY lengthOneDimension                         #dimensionDataType
    | typeName=(ENUM | SET)
      collectionOptions (BINARY | BYTE)?
      stringCharsetAttribute?
      (COLLATE collationName | BINARY | BYTE)?                      #collectionDataType
    | typeName=(
        GEOMETRYCOLLECTION | LINESTRING | MULTILINESTRING
        | MULTIPOINT | MULTIPOLYGON | POINT | POLYGON | GEOMETRY
      )
      ({atLeast(8, 0)}? SRID decimalLiteral)?                       #spatialDataType
    | {atLeast(8, 0)}? typeName=GEOMCOLLECTION
      (SRID decimalLiteral)?                                        #spatialDataType
    | {atLeast(5, 7)}? typeName=JSON                                #simpleDataType
    | typeName=LONG VARCHAR?
      BINARY?
      stringCharsetAttribute?
      (COLLATE collationName)?                                      #longVarcharDataType    // LONG VARCHAR is the same as LONG
    | LONG VARBINARY                                                #longVarbinaryDataType
    ;

numericFieldOption
    : SIGNED | UNSIGNED | ZEROFILL
    ;

collectionOptions
    : '(' collectionOption (',' collectionOption)* ')'
    ;

stringCharsetAttribute
    : (CHARACTER SET | CHARSET) charsetName
    | ASCII
    | UNICODE
    ;

collectionOption
    : textLiteralToken
    | hexadecimalLiteral
    | bitStringLiteral
    ;

convertedDataType
    : typeName=(BINARY| NCHAR) lengthOneDimension?
    | {atLeast(8, 0)}? NATIONAL (CHAR | CHARACTER) lengthOneDimension?
    | typeName=CHAR lengthOneDimension? convertedCharacterModifier?
    | typeName=DATE
    | typeName=(DATETIME | TIME) lengthOneDimension?
    | {atLeast(5, 7)}? typeName=JSON
    | {atLeast(8, 0)}? typeName=FLOAT lengthOneDimension?
    | {atLeast(8, 0)}? typeName=DOUBLE PRECISION?
    | {atLeast(8, 0)}? typeName=(REAL | YEAR)
    | {atLeast(8, 0)}? typeName=(
        POINT | LINESTRING | POLYGON | MULTIPOINT | MULTILINESTRING
        | MULTIPOLYGON | GEOMETRYCOLLECTION | GEOMCOLLECTION
      )
    | typeName=DECIMAL lengthTwoOptionalDimension?
    | (SIGNED | UNSIGNED) (INT | INTEGER)?
    ;

convertedCharacterModifier
    : ASCII
    | UNICODE
    | BYTE
    | BINARY ((CHARACTER SET | CHARSET) charsetName)?
    | (CHARACTER SET | CHARSET) charsetName BINARY?
    ;

lengthOneDimension
    : '(' decimalLiteral ')'
    ;

lengthTwoDimension
    : '(' decimalLiteral ',' decimalLiteral ')'
    ;

lengthTwoOptionalDimension
    : '(' decimalLiteral (',' decimalLiteral)? ')'
    ;


//    Common Lists

uidList
    : uid (',' uid)*
    ;

tables
    : tableName (',' tableName)*
    ;

indexColumnNames
    : '(' indexColumnName (',' indexColumnName)* ')'
    ;

expressions
    : expression (',' expression)*
    ;

expressionsWithDefaults
    : expressionOrDefault (',' expressionOrDefault)*
    ;

constants
    : constant (',' constant)*
    ;

simpleStrings
    : textLiteralToken (',' textLiteralToken)*
    ;

userVariables
    : LOCAL_ID (',' LOCAL_ID)*
    ;


//    Common Expressons

defaultValue
    : NULL_LITERAL
    | unaryOperator? constant
    | currentTimestamp (ON UPDATE currentTimestamp)?
    | {atLeast(8, 0)}? '(' expression ')'
    ;

currentTimestamp
    : (CURRENT_TIMESTAMP | LOCALTIME | LOCALTIMESTAMP)
      ('(' temporalPrecisionLiteral? ')')?
    | NOW '(' temporalPrecisionLiteral? ')'
    ;

expressionOrDefault
    : expression | DEFAULT
    ;

ifExists
    : IF EXISTS;

ifNotExists
    : IF NOT EXISTS;


//    Functions

functionCall
    : (ADDDATE | SUBDATE) '(' expression ',' expression ')'         #nonKeywordFunctionCall
    | (DATE_ADD | DATE_SUB)
      '(' expression ',' INTERVAL expression intervalType ')'       #nonKeywordFunctionCall
    | (TIMESTAMPADD | TIMESTAMPDIFF)
      '(' intervalTypeBase ',' expression ',' expression ')'        #nonKeywordFunctionCall
    | specificFunction                                              #specificFunctionCall
    | aggregateFunction overClause?                                 #aggregateFunctionCall
    | {atLeastExact(80024) && isStCollectToken()}? customFunctionName
      '(' aggregator=DISTINCT? functionArg ')' overClause?         #spatialAggregateFunctionCall
    | jsonDualityObjectFunction                                     #jsonDualityObjectFunctionCall
    | keywordFunction                                               #keywordFunctionCall
    | passwordFunctionClause                                        #passwordFunctionCall
    | {isGenericFunctionSyntaxAhead()}? genericFunction             #genericFunctionCall
    | nonAggregateFunction    overClause                            #nonAggregateFunctionCall
    ;

genericFunction
    : name=genericFunctionName '(' args=functionArgs? ')'
      {isGenericFunctionCallAllowed($name.ctx, $args.ctx)}?
    ;

genericFunctionName
    : {isScalarFunctionNameAhead()}? scalarFunctionName             #scalarGenericFunctionName
    | {!isScalarFunctionNameAhead()}? function=customFunctionName
      {isGenericFunctionCallAllowed($function.ctx)}?                #customGenericFunctionName
    ;

jsonDualityObjectFunction
    : {atLeast(9, 7)}? JSON_DUALITY_OBJECT '(' jsonDualityTableTags? jsonDualityKeyValueList ')'
    ;

keywordFunction
    : (DATE | DAY | HOUR | MINUTE | MONTH | SECOND | TIME | YEAR)
      '(' functionArg ')'
    | TIMESTAMP '(' functionArg (',' functionArg)? ')'
    | (LEFT | RIGHT) '(' functionArg ',' functionArg ')'
    | INSERT '(' functionArg ',' functionArg ',' functionArg ',' functionArg ')'
    | INTERVAL '(' functionArg ',' functionArg (',' functionArg)* ')'
    | USER '(' ')'
    ;

jsonDualityTableTags
    : WITH jsonDualityTableTag
    | WITH '(' jsonDualityTableTag (',' jsonDualityTableTag)* ')'
    ;

jsonDualityTableTag
    : INSERT
    | UPDATE
    | DELETE
    | NO INSERT
    | NO UPDATE
    | NO DELETE
    ;

jsonDualityKeyValueList
    : jsonDualityKeyValue (',' jsonDualityKeyValue)*
    ;

jsonDualityKeyValue
    : stringLiteral ':' functionArg
    ;

nonAggregateFunction
    : (CUME_DIST | DENSE_RANK | PERCENT_RANK | RANK | ROW_NUMBER) '(' ')'
    | NTILE '(' stableInteger ')'
    | (FIRST_VALUE | LAST_VALUE) '(' expression ')' windowNullTreatment?
    | (LAG | LEAD)
      '(' expression (',' stableInteger (',' expression)?)? ')' windowNullTreatment?
    | NTH_VALUE '(' expression ',' expression ')' windowFrom? windowNullTreatment?
    ;

stableInteger
    : unsignedDecimalIntegerLiteral
    | PARAM_MARKER
    | LOCAL_ID
    | simpleId
    ;

windowFrom
    : FROM (FIRST | LAST)
    ;

windowNullTreatment
    : (RESPECT | IGNORE) NULLS
    ;

overClause
    : {atLeast(8, 0)}? OVER (window_specification | uid)
    ;

window_specification
    : LR_BRACKET uid?
      (PARTITION BY expression (',' expression)*)?
      orderByClause? frame_clause? RR_BRACKET
    ;

frame_clause
    : (ROWS | RANGE | GROUPS) frame_extent frameExclusion?
    ;

frameExclusion
    : EXCLUDE (CURRENT ROW | GROUP | TIES | NO OTHERS)
    ;

frame_extent
    : BETWEEN startBound=frame_start_or_end AND endBound=frame_start_or_end
    | frame_start
    ;

frame_start
    : UNBOUNDED PRECEDING
    | frameOffset PRECEDING
    | CURRENT ROW
    ;

frame_start_or_end
    : frame_start
    | frameOffset FOLLOWING
    | UNBOUNDED FOLLOWING
    ;

frameOffset
    : decimalLiteral
    | PARAM_MARKER
    | INTERVAL (PARAM_MARKER | expression) intervalType
    ;

specificFunction
    : (CURRENT_DATE | UTC_DATE | CURRENT_USER | SCHEMA) ('(' ')')? #simpleFunctionCall
    | CURDATE '(' ')'                                               #simpleFunctionCall
    | (CURRENT_TIME | CURRENT_TIMESTAMP | LOCALTIME | LOCALTIMESTAMP
      | UTC_TIME | UTC_TIMESTAMP)
      ('(' temporalPrecisionLiteral? ')')?                          #simpleFunctionCall
    | (CURTIME | NOW | SYSDATE)
      '(' temporalPrecisionLiteral? ')'                             #simpleFunctionCall
    | DEFAULT '(' fullColumnName ')'                                #defaultFunctionCall
    | CONVERT '(' expression separator=',' convertedDataType ')'    #dataTypeFunctionCall
    | CONVERT '(' expression USING charsetName ')'                  #dataTypeFunctionCall
    | CAST '(' expression AS convertedDataType ({atLeast(8, 0)}? ARRAY)? ')'
                                                                    #dataTypeFunctionCall
    | {atLeastExact(80022)}? CAST '(' expression AT LOCAL AS convertedDataType ARRAY? ')'
                                                                    #dataTypeFunctionCall
    | {atLeastExact(80022)}? CAST '(' expression AT TIME ZONE INTERVAL? textLiteralToken
      AS DATETIME lengthOneDimension? ')'                           #dataTypeFunctionCall
    | VALUES '(' fullColumnName ')'                                 #valuesFunctionCall
    | CASE expression caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CASE caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CHAR '(' functionArgs  (USING charsetName)? ')'               #charFunctionCall
    | POSITION
      '('
          (
            positionString=stringLiteral
            | positionExpression=expression
          )
          IN
          (
            inString=stringLiteral
            | inExpression=expression
          )
      ')'                                                           #positionFunctionCall
    | (SUBSTR | SUBSTRING)
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        ) FROM
        (
          fromDecimal=decimalLiteral
          | fromExpression=expression
        )
        (
          FOR
          (
            forDecimal=decimalLiteral
            | forExpression=expression
          )
        )?
      ')'                                                           #substrFunctionCall
    | TRIM
      '('
        positioinForm=(BOTH | LEADING | TRAILING)
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )?
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | TRIM
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | WEIGHT_STRING
      '('
        (stringLiteral | expression)
        (AS stringFormat=(CHAR | BINARY)
        '(' {isPositiveIntegerAhead()}? unsignedDecimalIntegerLiteral ')' )? levelsInWeightString?
      ')'                                                           #weightFunctionCall
    | EXTRACT
      '('
        intervalType
        FROM
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
      ')'                                                           #extractFunctionCall
    | GET_FORMAT
      '('
        datetimeFormat=(DATE | TIME | TIMESTAMP | DATETIME)
        ',' expression
      ')'                                                           #getFormatFunctionCall
    | {atLeastExact(80021)}? JSON_VALUE
      '(' expression
       ',' expression
         (RETURNING convertedDataType)?
         ((NULL_LITERAL | ERROR | (DEFAULT jsonValueDefaultValue)) ON EMPTY)?
         ((NULL_LITERAL | ERROR | (DEFAULT jsonValueDefaultValue)) ON ERROR)?
       ')'                                                          #jsonValueFunctionCall
    ;

jsonValueDefaultValue
    : stringLiteral
    | ('+' | '-')? (decimalLiteral | REAL_LITERAL)
    | hexadecimalLiteral
    | bitStringLiteral
    | booleanLiteral
    | (DATE | TIME | TIMESTAMP) stringLiteral
    ;

caseFuncAlternative
    : WHEN condition=functionArg
      THEN consequent=functionArg
    ;

levelsInWeightString
    : {atMost(5, 7)}? LEVEL levelInWeightListElement
      (',' levelInWeightListElement)*                               #levelWeightList
    | {atMost(5, 7)}? LEVEL
      firstLevel=decimalLiteral '-' lastLevel=decimalLiteral        #levelWeightRange
    ;

levelInWeightListElement
    : decimalLiteral orderType=(ASC | DESC)? REVERSE?
    ;

aggregateFunction
    : (AVG | MAX | MIN | SUM)
      '(' aggregator=(ALL | DISTINCT)? functionArg ')'
    | COUNT '(' aggregator=ALL? (starArg='*' | functionArg) ')'
    | COUNT '(' aggregator=DISTINCT functionArgs ')'
    | (
        BIT_AND | BIT_OR | BIT_XOR | STD | STDDEV | STDDEV_POP
        | STDDEV_SAMP | VAR_POP | VAR_SAMP | VARIANCE
      ) '(' aggregator=ALL? functionArg ')'
    | GROUP_CONCAT '('
        aggregator=DISTINCT? functionArgs
        (ORDER BY
          orderByExpression (',' orderByExpression)*
        )? (SEPARATOR separator=textLiteralToken)?
      ')'
    | {atLeast(5, 7)}? JSON_ARRAYAGG '(' functionArg jsonConstructorNullClause? ')'
    | {atLeast(5, 7)}? JSON_OBJECTAGG '(' functionArg ',' functionArg ')'
    ;

jsonConstructorNullClause
    : {atLeast(9, 7)}? (NULL_LITERAL | ABSENT) ON NULL_LITERAL
    ;

scalarFunctionName
    : functionNameBase
    | ASCII | COALESCE | CONTAINS
    | GROUPING
    | IF | MID | REPLACE | SUBSTR | SUBSTRING | TRIM | TRUNCATE
    ;

passwordFunctionClause
    : ({atMost(5, 7)}? functionName=PASSWORD | {atMost(5, 6)}? functionName=OLD_PASSWORD)
      '(' functionArg ')'
    ;

functionArgs
    : functionArgWithAlias
    (
      ','
      functionArgWithAlias
    )*
    ;

functionArgWithAlias
    : functionArg functionArgAlias?
    ;

functionArgAlias
    : AS? (uid | textLiteralToken)
    ;

functionArg
    : expression
    ;


//    Expressions, predicates

expression
    : assignmentExpression
    ;

assignmentExpression
    : LOCAL_ID VAR_ASSIGN assignmentExpression                       #variableAssignmentExpression
    | orExpression                                                   #logicalAssignmentExpression
    ;

orExpression
    : xorExpression ((OR | PIPES_LOGICAL_OR | PIPES_AMBIGUOUS) xorExpression)* #logicalExpression
    ;

xorExpression
    : andExpression (XOR andExpression)*                            #logicalXorExpression
    ;

andExpression
    : logicalNotExpression ((AND | '&' '&') logicalNotExpression)*  #logicalAndExpression
    ;

logicalNotExpression
    : {!isHighNotPrecedence()}? NOT logicalNotExpression            #notExpression
    | predicate                                                     #predicateExpression
    ;

predicate
    : comparisonExpression NOT? BETWEEN comparisonExpression AND predicate #betweenPredicate
    | comparisonExpression                                         #comparisonPredicate
    ;

comparisonExpression
    : comparisonOperand comparisonPredicateSuffix*
    ;

comparisonOperand
    : bitOrExpression                                               #expressionAtomPredicate
    | MATCH
      (
        '(' fullColumnName (COMMA fullColumnName)* ')'
        | fullColumnName (COMMA fullColumnName)*
      )
      AGAINST '(' expression search_modifier? ')'                   #fullSearchPredicate
    ;

comparisonPredicateSuffix
    : NOT? IN '(' (subqueryStatement | expressions) ')'             #inPredicate
    | IS nullNotnull                                                #isNullPredicate
    | {isTruthPredicateAllowed($ctx)}?
      IS NOT? testValue=(TRUE | FALSE | UNKNOWN)                    #truthPredicate
    | comparisonOperator bitOrExpression                            #binaryComparasionPredicate
    | comparisonOperator
      quantifier=(ALL | ANY | SOME) '(' subqueryStatement ')'       #subqueryComparasionPredicate
    | SOUNDS LIKE bitOrExpression                                   #soundsLikePredicate
    | NOT? LIKE {isPipesConcatLikeOperandAllowed()}? bitOrExpression
      (ESCAPE {isPipesConcatLikeOperandAllowed()}? bitOrExpression)? #likePredicate
    | NOT? regex=(REGEXP | RLIKE) bitOrExpression                   #regexpPredicate
    | {atLeastExact(80017)}? MEMBER OF? '(' bitOrExpression ')'     #jsonMemberOfPredicate
    ;

search_modifier:
    IN NATURAL LANGUAGE MODE | IN BOOLEAN MODE |  WITH QUERY EXPANSION | IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION
    ;

bitOrExpression
    : bitAndExpression ('|' bitAndExpression)*                      #bitExpressionAtom
    ;

bitAndExpression
    : shiftExpression ('&' shiftExpression)*                        #bitAndExpressionAtom
    ;

shiftExpression
    : additiveExpression (('<' '<' | '>' '>') additiveExpression)*  #shiftExpressionAtom
    ;

additiveExpression
    : multiplicativeExpression (('+' | '-' | '--') multiplicativeExpression)* #additiveExpressionAtom
    ;

multiplicativeExpression
    : bitXorExpression (('*' | '/' | '%' | DIV | MOD) bitXorExpression)* #mathExpressionAtom
    ;

bitXorExpression
    : pipesConcatExpression ('^' pipesConcatExpression)*            #bitXorExpressionAtom
    ;

pipesConcatExpression
    : unaryExpression ((PIPES_CONCAT | PIPES_AMBIGUOUS) unaryExpression)* #pipesConcatExpressionAtom
    ;

unaryExpression
    : {isHighNotPrecedence()}? NOT unaryExpression                  #highNotExpression
    | unaryOperator unaryExpression                                 #unaryExpressionAtom
    | LOCAL_ID VAR_ASSIGN assignmentExpression                      #nestedVariableAssignmentExpression
    | expressionAtom                                                #primaryExpressionAtom
    ;


// Add in ASTVisitor nullNotnull in constant
expressionAtom
    : constant                                                      #constantExpressionAtom
    | PARAM_MARKER                                                  #parameterMarkerExpressionAtom
    | {isTypedTemporalLiteralAhead()}? typedTemporalLiteral         #typedTemporalLiteralExpressionAtom
    | fullColumnName                                                #fullColumnNameExpressionAtom
    | {isFunctionSyntaxAllowedAhead()}? functionCall                #functionCallExpressionAtom
    | expressionAtom COLLATE collationName                          #collateExpressionAtom
    | mysqlVariable                                                 #mysqlVariableExpressionAtom
    | BINARY expressionAtom                                         #binaryExpressionAtom
    | '(' expression (',' expression)* ')'                          #nestedExpressionAtom
    | ROW '(' expression (',' expression)+ ')'                      #nestedRowExpressionAtom
    | EXISTS '(' subqueryStatement ')'                              #existsExpessionAtom
    | '(' subqueryStatement ')'                                     #subqueryExpessionAtom
    | LCURLY_BRACKET uid expression RCURLY_BRACKET                  #odbcExpressionAtom
    | INTERVAL expression intervalType                              #intervalExpressionAtom
    | left=fullColumnName jsonOperator right=stringLiteral          #jsonExpressionAtom
    ;

typedTemporalLiteral
    : (DATE | TIME | TIMESTAMP) stringLiteral
    ;

unaryOperator
    : '!' | '~' | '+' | '-' | '--'
    ;

comparisonOperator
    : '=' | '>' | '<' | '<' '=' | '>' '='
    | '<' '>' | '!' '=' | '<' '=' '>'
    ;

bitOperator
    : '<' '<' | '>' '>' | '&' | '^' | '|'
    ;

mathOperator
    : '*' | '/' | '%' | DIV | MOD | '+' | '-' | '--'
    ;

jsonOperator
    : {atLeast(5, 7)}? ('-' '>' | '-' '>' '>')
    ;

//    Simple id sets
//     (that keyword, which can be id)

charsetNameBase
    : ARMSCII8 | ASCII | BIG5 | CP1250 | CP1251 | CP1256 | CP1257
    | CP850 | CP852 | CP866 | CP932 | DEC8 | EUCJPMS | EUCKR
    | GB18030 | GB2312 | GBK | GEOSTD8 | GREEK | HEBREW | HP8 | KEYBCS2
    | KOI8R | KOI8U | LATIN1 | LATIN2 | LATIN5 | LATIN7 | MACCE
    | MACROMAN | SJIS | SWE7 | TIS620 | UCS2 | UJIS | UTF16
    | UTF16LE | UTF32 | UTF8 | UTF8MB3 | UTF8MB4
    ;

transactionLevelBase
    : REPEATABLE | COMMITTED | UNCOMMITTED | SERIALIZABLE
    ;

privilegesBase
    : TABLES | ROUTINE | EXECUTE | FILE | PROCESS
    | RELOAD | SHUTDOWN | SUPER | PRIVILEGES
    ;

intervalTypeBase
    : QUARTER | MONTH | DAY | HOUR
    | MINUTE | WEEK | SECOND | MICROSECOND
    ;

dataTypeBase
    : DATE | TIME | TIMESTAMP | DATETIME | YEAR | ENUM | TEXT | VECTOR
    ;

keywordsCanBeId
    : ACCESSIBLE | ACCOUNT | ACTION | ACTIVE | ADMIN | AFTER | AGGREGATE | ALGORITHM | ANY | APPLICATION_PASSWORD_ADMIN | ARRAY
    | AT | AUDIT_ADMIN | AUTHORS | AUTHENTICATION | AUTO | AUTOCOMMIT | AUTOEXTEND_SIZE
    | ABSENT | ALLOW_MISSING_FILES | AUTO_INCREMENT | AUTO_REFRESH | AUTO_REFRESH_SOURCE
    | AVG | AVG_ROW_LENGTH | BACKUP | BACKUP_ADMIN | BEGIN | BINLOG_ADMIN | BINLOG_ENCRYPTION_ADMIN | BIT | BIT_AND | BIT_OR | BIT_XOR
    | BERNOULLI | BLOCK | BOOL | BOOLEAN | BTREE | BUCKETS | BYTE | CACHE | CASCADED | CAST | CHAIN | CHANGED
    | CHANNEL | CHECKSUM | PAGE_CHECKSUM | CATALOG_NAME | CIPHER
    | CLASS_ORIGIN | CLIENT | CLONE | CLONE_ADMIN | CLOSE | COALESCE | CODE
    | COLUMNS | COLUMN_FORMAT | COLUMN_NAME | COMMENT | COMMIT | COMPACT | COMPONENT
    | COMPLETION | COMPRESSED | COMPRESSION | CONCURRENT | CONNECT
    | CONNECTION | CONNECTION_ADMIN | CONSISTENT | CONSTRAINT_CATALOG | CONSTRAINT_NAME
    | CONSTRAINT_SCHEMA | CONTAINS | CONTEXT
    | CONTRIBUTORS | COPY | COUNT | CPU | CURDATE | CURRENT | CURSOR_NAME | CURTIME
    | DATA | DATAFILE | DATE_ADD | DATE_SUB | DEALLOCATE
    | DEFAULT_AUTH | DEFINER | DEFINITION | DELAY_KEY_WRITE | DES_KEY_FILE | DESCRIPTION | DIAGNOSTICS | DIRECTORY
    | DISABLE | DISCARD | DISK | DO | DUALITY | DUMPFILE | DUPLICATE
    | DYNAMIC | ENABLE | ENCRYPTION | ENCRYPTION_KEY_ADMIN | ENFORCED | END | ENDS | ENGINE | ENGINE_ATTRIBUTE | ENGINES
    | ERROR | ERRORS | ESCAPE | EVEN | EVENT | EVENTS | EVERY | EXTERNAL_FORMAT | EXTRACT
    | EXCHANGE | EXCLUSIVE | EXPIRE | EXPORT | EXTENDED | EXTENT_SIZE | FAILED_LOGIN_ATTEMPTS | FAST | FAULTS
    | FIELDS | FILE_BLOCK_SIZE | FILE_FORMAT | FILE_NAME | FILE_PATTERN | FILE_PREFIX | FILES
    | FILTER | FIREWALL_ADMIN | FIREWALL_USER | FIRST | FIXED | FLUSH
    | FLUSH_OPTIMIZER_COSTS | FLUSH_STATUS | FLUSH_TABLES | FLUSH_USER_RESOURCES
    | FOLLOWS | FOUND | FULL | GENERAL | GENERATE | GLOBAL | GRANTS | GROUP | GROUP_CONCAT
    | GROUP_REPLICATION | GROUP_REPLICATION_ADMIN | GUIDED | HANDLER | HASH | HEADER | HELP | HISTOGRAM | HISTORY | HOST | HOSTS | IDENTIFIED
    | IGNORE_SERVER_IDS | IMPORT | INACTIVE | INDEXES | INITIAL | INITIAL_SIZE | INNODB_REDO_LOG_ARCHIVE | INNODB_REDO_LOG_ENABLE
    | INPLACE | INSERT_METHOD | INSTALL | INSTANCE | INTERNAL | INVOKER | IO
    | IO_THREAD | IPC | ISOLATION | ISSUER | JSON | KEY_BLOCK_SIZE | KEYRING
    | LANGUAGE | LAST | LEAVES | LESS | LEVEL | LIST | LOCAL
    | LOGFILE | LOGS | MASTER | MASTER_AUTO_POSITION | MATERIALIZED
    | MASTER_CONNECT_RETRY | MASTER_DELAY
    | MASTER_HEARTBEAT_PERIOD | MASTER_HOST | MASTER_LOG_FILE
    | MASTER_LOG_POS | MASTER_PASSWORD | MASTER_PORT
    | MASTER_RETRY_COUNT | MASTER_SSL | MASTER_SSL_CA
    | MASTER_SSL_CAPATH | MASTER_SSL_CERT | MASTER_SSL_CIPHER
    | MASTER_SSL_CRL | MASTER_SSL_CRLPATH | MASTER_SSL_KEY
    | MASTER_TLS_VERSION | MASTER_USER | MASKING
    | MAX_CONNECTIONS_PER_HOUR | MAX_QUERIES_PER_HOUR
    | MAX | MAX_ROWS | MAX_SIZE | MAX_UPDATES_PER_HOUR
    | MAX_USER_CONNECTIONS | MEDIUM | MEMBER | MEMORY | MERGE | MESSAGE_TEXT
    | MID | MIGRATE
    | MIN | MIN_ROWS | MODE | MODIFY | MUTEX | MYSQL | MYSQL_ERRNO | NAME | NAMES | NESTED
    | LOCKED | NCHAR | NDB_STORED_USER | NEVER | NEXT | NO | NODEGROUP | NONE | NOW | NOWAIT | NUMBER | ODBC | OFF | OFFLINE | OFFSET
    | OJ | OLD | OLD_PASSWORD | ONE | ONLINE | ONLY | OPEN
    | OPTIONAL | OPTIONS | ORDER | ORDINALITY | ORGANIZATION | OWNER | PACK_KEYS | PAGE | PARAMETERS | PARSER | PARSE_TREE | PARTIAL | PATH
    | PARTITIONING | PARTITIONS | PASSWORD | PASSWORD_LOCK_TIME | PERSIST_RO_VARIABLES_ADMIN | PHASE | PLUGINS
    | PLUGIN_DIR | PLUGIN | POLICY | PORT | POSITION | PRECEDES | PREPARE | PRESERVE | PREV
    | PRIORITY | PROCESSLIST | PROFILE | PROFILES | PROXY | QUERY | QUICK
    | READ_ONLY | READ_WRITE | REBUILD | RECOVER | REDO_BUFFER_SIZE | REDO_LOG | REDOFILE | REDUNDANT | REFERENCE
    | RELATIONAL | RELAY | RELAYLOG | RELAY_LOG_FILE | RELAY_LOG_POS | REMOVE
    | REORGANIZE | REPAIR | REPLICATE_DO_DB | REPLICATE_DO_TABLE
    | REPLICAS | REPLICATE_IGNORE_DB | REPLICATE_IGNORE_TABLE
    | REPLICATE_REWRITE_DB | REPLICATE_WILD_DO_TABLE
    | REPLICATE_WILD_IGNORE_TABLE | REPLICATION | REPLICATION_APPLIER | REPLICATION_SLAVE_ADMIN | RESET | RESTART
    | RESOURCE | RESOURCE_GROUP_ADMIN | RESOURCE_GROUP_USER | RESUME
    | RETURNED_SQLSTATE | RETURNS | RETAIN | REUSE | ROLE | ROLE_ADMIN | ROLLBACK | ROTATE | RTREE
    | ROW_FORMAT | SAVEPOINT | SCHEDULE | SCHEMA_NAME | SECURITY | SERIAL | SERVER
    | SECONDARY_ENGINE | SECONDARY_ENGINE_ATTRIBUTE | SERVICE_CONNECTION_ADMIN | SESSION | SESSION_VARIABLES_ADMIN | SET_USER_ID | SHARE | SHARED | SHOW_ROUTINE | SIGNED | SIMPLE | SLAVE
    | SLOW | SNAPSHOT | SOCKET | SOME | SONAME | SOUNDS | SOURCE | SOURCE_LOG_FILE | SOURCE_LOG_POS
    | SQL_AFTER_GTIDS | SQL_AFTER_MTS_GAPS | SQL_BEFORE_GTIDS
    | SQL_BUFFER_RESULT | SQL_CACHE | SQL_NO_CACHE | SQL_THREAD
    | SETS | SKIP_SYMBOL | SRID | STACKED | START | STARTS | STATS_AUTO_RECALC | STATS_PERSISTENT | STRICT_LOAD
    | STATS_SAMPLE_PAGES | STATUS | STD | STDDEV | STDDEV_POP | STDDEV_SAMP | STOP | STORAGE | STREAM | STRING
    | SUBCLASS_ORIGIN | SUBJECT | SUBPARTITION | SUBPARTITIONS | SUBSTR | SUBSTRING | SUM | SUSPEND | SWAPS
    | SWITCHES | SYSDATE | SYSTEM_USER | SYSTEM_VARIABLES_ADMIN | TABLE_NAME | TABLESPACE | TABLE_ENCRYPTION_ADMIN
    | TEMPORARY | TEMPTABLE | THAN | THREAD_PRIORITY | TLS | TRADITIONAL | TREE | TRIM
    | TRANSACTION | TRANSACTIONAL | TRIGGERS | TRUNCATE | TYPE | UNBOUNDED | UNDEFINED | UNDOFILE
    | UNDO_BUFFER_SIZE | UNINSTALL | UNKNOWN | UNTIL | UPGRADE | USER | USE_FRM | USER_RESOURCES
    | UNICODE | VALIDATE | VALIDATION | VALUE | VAR_POP | VAR_SAMP | VARIABLES | VARIANCE | VCPU | VECTOR | VERIFY_KEY_CONSTRAINTS | VERSION_TOKEN_ADMIN | VIEW | WAIT | WARNINGS | WITHOUT
    | URL | URI | S3 | BULK
    | WORK | WRAPPER | X509 | XA | XA_RECOVER_ADMIN | XML | ZONE
    // MariaDB
    | VIA | LASTVAL | NEXTVAL | SETVAL | PREVIOUS | PERSISTENT | REPLICATION_MASTER_ADMIN | REPLICA | READ_ONLY_ADMIN | FEDERATED_ADMIN | BINLOG_MONITOR | BINLOG_REPLAY | GTIDS
    ;

functionNameBase
    : ID
    ;
