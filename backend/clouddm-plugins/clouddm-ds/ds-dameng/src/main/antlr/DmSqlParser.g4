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

parser grammar DmSqlParser;

@header {
    import com.clougence.clouddm.ds.dameng.sql.parser.base.DmSqlParserBase;
}

options {
    tokenVocab = DmSqlLexer;
    superClass = DmSqlParserBase;
}

sqlScript
    : statementBlock* EOF
    ;

statementBlock
    : SEMI+
    | SLASH SEMI*
    | statement statementTerminator+
    | statement {isEndOfInputAhead()}?
    ;

statementTerminator
    : SEMI
    | SLASH
    ;

statement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | mergeStatement
    | flashbackStatement
    | refreshMaterializedViewStatement
    | createStatement
    | adminStatement
    | statStatement
    | statProcedureStatement
    | configWriteStatement
    | auditAdminStatement
    | securityAdminStatement
    | alterStatement
    | dropStatement
    | truncateStatement
    | commentStatement
    | grantStatement
    | revokeStatement
    | lockTableStatement
    | transactionStatement
    | callStatement
    | procedureCallStatement
    | alterSessionParallelDmlStatement
    | setSchemaStatement
    | setTimeZoneStatement
    | setIdentityInsertStatement
    | explainStatement
    | sqlBlockStatement
    | cStyleBlockStatement
    ;

selectStatement
@init { pushSiblingsOrderScope(); }
@after { popSiblingsOrderScope(); }
    : withClause? selectOperand queryRemainder* selectStatementTail?
    ;

selectOperand
    : selectQuery
    | LPAREN selectStatement RPAREN
    ;

selectStatementTail
    : selectTailCore forXmlPathClause?
    | forXmlPathClause
    ;

selectTailCore
    : selectOrderByClause orderFirstTail? withUrClause?
    | limitCondition limitFirstTail? withUrClause?
    | forUpdateClause lockFirstTail? withUrClause?
    | withUrClause
    ;

orderFirstTail
    : limitCondition forUpdateClause?
    | forUpdateClause limitCondition?
    ;

limitFirstTail
    : selectOrderByClause forUpdateClause?
    | forUpdateClause
    ;

lockFirstTail
    : selectOrderByClause limitCondition?
    | limitCondition
    ;

withClause
    : WITH RECURSIVE? (withFunctionDefinition+ cteDefinitionList? | cteDefinitionList)
    ;

withFunctionDefinition
    : FUNCTION qualifiedName functionRoutineSignature routineDefinition SEMI
    ;

cteDefinitionList
    : cteDefinition (COMMA cteDefinition)*
    ;

cteDefinition
    : identifier columnNameList? AS LPAREN selectStatement RPAREN cteSearchClause? cteCycleClause?
    ;

cteSearchClause
    : SEARCH (DEPTH | BREADTH) FIRST BY orderByItem (COMMA orderByItem)* SET identifier
    ;

cteCycleClause
    : CYCLE identifier (COMMA identifier)* SET identifier TO expression DEFAULT expression
    ;

selectQuery
    : SELECT selectModifier* topClause? selectList selectIntoClause? fromClause? whereClause? groupHierarchyClause? modelClause? windowDefinitionClause?
    ;

selectModifier
    : ALL
    | DISTINCT
    | UNIQUE
    | SQL_CALC_FOUND_ROWS
    ;

topClause
    : TOP expression (COMMA expression)? PERCENT_KEYWORD? (WITH TIES)?
    ;

selectList
    : selectItem (COMMA selectItem)*
    ;

selectIntoClause
    : INTO returnTargetList
    | BULK COLLECT INTO returnTargetList
    ;

selectItem
    : STAR
    | qualifiedName DOT STAR
    | expression (AS? aliasIdentifier)?
    ;

fromClause
    : FROM tableSource (COMMA tableSource)*
    ;

tableSource
    : tablePrimary joinClause*
    ;

tablePrimary
    : jsonTableExpression tableAlias? derivedColumnList?
    | xmlTableExpression tableAlias? derivedColumnList?
    | jsonCollectionTableExpression tableAlias? derivedColumnList?
    | tableCollectionExpression tableAlias?
    | arrayTableExpression tableAlias?
    | {isTokenNotAhead(ARRAY)}?
      qualifiedName tableIndexClause? tableColumnProjection? partitionExtensionClause* sampleClause? tablePivotClause* (flashbackQueryClause | flashbackVersionQueryClause)? tableAlias? derivedColumnList?
    | LPAREN selectStatement RPAREN tablePivotClause* (flashbackQueryClause | flashbackVersionQueryClause)? tableAlias? derivedColumnList?
    | LPAREN tableSource (COMMA tableSource)* RPAREN tableAlias?
    ;

jsonTableExpression
    : JSON_TABLE LPAREN expression COMMA expression jsonValueEmptyClause? jsonValueErrorClause? jsonColumnsClause RPAREN
    ;

jsonColumnsClause
    : COLUMNS LPAREN jsonTableColumn (COMMA jsonTableColumn)* RPAREN
    ;

jsonTableColumn
    : identifier FOR ORDINALITY
    | NESTED PATH expression jsonColumnsClause
    | identifier dataType PATH expression jsonValueEmptyClause? jsonValueErrorClause?
    | identifier dataType EXISTS PATH expression jsonExistsEmptyClause? jsonExistsErrorClause?
    | identifier dataType FORMAT JSON PATH expression jsonQueryEmptyClause? jsonQueryErrorClause?
    ;

xmlTableExpression
    : XMLTABLE LPAREN xmlNamespacesClause? expression xmlPassingClause? xmlTableColumnsClause? RPAREN
    ;

xmlNamespacesClause
    : (XMLNAMESPACES | XMLNAMESPACE) LPAREN xmlNamespaceItem (COMMA xmlNamespaceItem)* RPAREN COMMA
    ;

xmlNamespaceItem
    : STRING AS identifier
    | DEFAULT STRING
    ;

xmlPassingClause
    : PASSING xmlPassingArgument (COMMA xmlPassingArgument)*
    ;

xmlPassingArgument
    : expression (AS? identifier)?
    ;

xmlTableColumnsClause
    : COLUMNS xmlTableColumn (COMMA xmlTableColumn)*
    ;

xmlTableColumn
    : identifier FOR ORDINALITY
    | identifier dataType (PATH expression)? xmlTableColumnDefaultClause? xmlTableColumnNullClause?
    ;

xmlTableColumnDefaultClause
    : DEFAULT expression
    ;

xmlTableColumnNullClause
    : NOT? NULL_LITERAL
    ;

jsonCollectionTableExpression
    : jsonCollectionTableFunction LPAREN expression RPAREN
    ;

jsonCollectionTableFunction
    : JSON_OBJECT_KEYS
    | JSONB_OBJECT_KEYS
    | JSONB_EACH
    | JSONB_EACH_TEXT
    | JSONB_ARRAY_ELEMENTS
    | JSONB_ARRAY_ELEMENTS_TEXT
    ;

tableCollectionExpression
    : TABLE LPAREN (selectStatement | expression) RPAREN
    ;

arrayTableExpression
    : ARRAY (newArrayExpression | {isTokenNotAhead(NEW)}? expression)
    ;

tableIndexClause
    : INDEX identifier
    ;

tableColumnProjection
    : columnNameList
    ;

partitionExtensionClause
    : (PARTITION | SUBPARTITION) (LPAREN identifier RPAREN | FOR LPAREN expressionList RPAREN)
    ;

flashbackQueryClause
    : WHEN TIMESTAMP expression
    | AS OF TIMESTAMP expression
    | AS OF (SCN | LSN) expression
    ;

flashbackVersionQueryClause
    : VERSIONS BETWEEN TIMESTAMP concatenation AND concatenation
    | VERSIONS BETWEEN (SCN | LSN) concatenation AND concatenation
    ;

sampleClause
    : SAMPLE BLOCK? LPAREN expression RPAREN (SEED LPAREN expression RPAREN)?
    ;

pivotClause
    : PIVOT XML LPAREN pivotExpressionList FOR pivotForClause IN LPAREN pivotXmlInClause RPAREN RPAREN
    | PIVOT LPAREN pivotExpressionList FOR pivotForClause IN LPAREN pivotInClauseList RPAREN RPAREN
    | UNPIVOT includeNullClause? LPAREN unpivotValueClause FOR unpivotForClause IN LPAREN unpivotInClauseList RPAREN RPAREN
    ;

tablePivotClause
    : tableAlias? pivotClause
    ;

pivotExpressionList
    : pivotExpression (COMMA pivotExpression)*
    ;

pivotExpression
    : functionCall (AS? identifier)?
    ;

pivotForClause
    : qualifiedName
    | LPAREN qualifiedNameList RPAREN
    ;

pivotInClauseList
    : pivotInClause (COMMA pivotInClause)*
    ;

pivotXmlInClause
    : ANY
    | selectStatement
    | pivotInClauseList
    ;

pivotInClause
    : expression (AS? identifier)?
    | LPAREN expressionList RPAREN (AS? identifier)?
    ;

includeNullClause
    : (INCLUDE | EXCLUDE) NULLS
    ;

unpivotValueClause
    : identifier
    | LPAREN identifierList RPAREN
    ;

unpivotForClause
    : identifier
    | LPAREN identifierList RPAREN
    ;

unpivotInClauseList
    : unpivotInClause (COMMA unpivotInClause)*
    ;

unpivotInClause
    : qualifiedName (AS? unpivotAlias)?
    | LPAREN qualifiedNameList RPAREN (AS? unpivotAlias)?
    ;

unpivotAlias
    : expression
    | LPAREN expressionList RPAREN
    ;

tableAlias
    : AS {isTokenNotAhead(OF)}? aliasIdentifier
    | {isBareTableAliasAhead()}? aliasIdentifier
    ;

aliasIdentifier
    : {isTokenNotAhead(BINARY)}? identifier
    ;

derivedColumnList
    : columnNameList
    ;

joinClause
    : partitionJoinClause? naturalJoinType JOIN tablePrimary partitionJoinClause?
    | partitionJoinClause? nonNaturalJoinType? JOIN tablePrimary partitionJoinClause? joinCondition?
    | applyJoinClause
    ;

partitionJoinClause
    : PARTITION BY LPAREN expressionList RPAREN
    ;

applyJoinClause
    : (CROSS | OUTER) APPLY tablePrimary
    ;

naturalJoinType
    : NATURAL (INNER | (LEFT | RIGHT | FULL) OUTER?)?
    ;

nonNaturalJoinType
    : INNER
    | CROSS
    | LEFT OUTER?
    | RIGHT OUTER?
    | FULL OUTER?
    ;

joinCondition
    : ON expression
    | USING LPAREN identifierList RPAREN
    ;

whereClause
    : WHERE (currentOfClause | expression)
    ;

currentOfClause
    : CURRENT OF qualifiedName
    ;

groupHierarchyClause
    : hierarchicalClause groupByClause? havingClause?
    | hierarchicalClause havingClause groupByClause?
    | groupByClause havingClause? hierarchicalClause?
    | groupByClause hierarchicalClause havingClause?
    | havingClause groupByClause? hierarchicalClause?
    | havingClause hierarchicalClause groupByClause?
    ;

modelClause
    : MODEL modelReturnRowsClause? modelReferenceClause* modelMainClause? modelPartitionClause? modelDimensionClause modelMeasuresClause modelNavClause? modelRulesClause?
    ;

modelReturnRowsClause
    : RETURN (UPDATED | ALL) ROWS
    ;

modelReferenceClause
    : REFERENCE identifier ON LPAREN selectStatement RPAREN modelDimensionClause modelMeasuresClause
    ;

modelMainClause
    : MAIN identifier
    ;

modelPartitionClause
    : PARTITION BY LPAREN modelColumnList? RPAREN
    ;

modelDimensionClause
    : DIMENSION BY LPAREN modelColumnList? RPAREN
    ;

modelMeasuresClause
    : MEASURES LPAREN modelColumnList? RPAREN
    ;

modelColumnList
    : modelColumn (COMMA modelColumn)*
    ;

modelColumn
    : expression (AS? identifier)?
    ;

modelNavClause
    : (IGNORE | KEEP) NAV
    ;

modelRulesClause
    : RULES modelRulesOption* LPAREN modelRule (COMMA modelRule)* RPAREN
    ;

modelRulesOption
    : modelRuleUpdateAction
    | (AUTOMATIC | SEQUENTIAL) ORDER
    | ITERATE LPAREN expression RPAREN (UNTIL LPAREN expression RPAREN)?
    ;

modelRule
    : modelRuleUpdateAction? modelRuleCellReference orderByClause? EQ expression
    ;

modelRuleUpdateAction
    : UPDATE
    | UPSERT ALL?
    ;

modelRuleCellReference
    : qualifiedName modelCellReference?
    ;

modelCellReference
    : BRACKET_QUOTED_ID
    | LPAREN expressionList? RPAREN
    ;

groupByClause
    : GROUP BY groupByItem (COMMA groupByItem)*
    ;

groupByItem
    : ROLLUP LPAREN expressionList? RPAREN
    | CUBE LPAREN expressionList? RPAREN
    | GROUPING SETS LPAREN groupingSetItem (COMMA groupingSetItem)* RPAREN
    | groupByItemSet
    ;

groupingSetItem
    : groupingSetExpression
    | LPAREN groupingSetExpressionList? RPAREN
    ;

groupingSetExpressionList
    : groupingSetExpression (COMMA groupingSetExpression)*
    ;

groupingSetExpression
    : {isTokenNotAhead(ROLLUP, CUBE)}? expression
    ;

groupByItemSet
    : expression
    | LPAREN expressionList? RPAREN
    ;

havingClause
    : HAVING expression
    ;

selectOrderByClause
    : orderByClause
    | {isSiblingsOrderAllowed()}? orderSiblingsByClause
    ;

orderByClause
    : ORDER BY orderByItem (COMMA orderByItem)*
    ;

orderSiblingsByClause
    : ORDER SIBLINGS BY orderByItem (COMMA orderByItem)*
    ;

orderByItem
    : expression (COLLATE qualifiedName)? (ASC | DESC)? (NULLS (FIRST | LAST))?
    ;

limitCondition
    : limitClause
    | rowLimitClause
    ;

limitClause
    : LIMIT expression (COMMA expression | OFFSET expression)?
    | OFFSET expression LIMIT expression
    ;

rowLimitClause
    : offsetRowsClause fetchClause?
    | fetchClause
    ;

offsetRowsClause
    : OFFSET expression (ROW | ROWS)
    ;

fetchClause
    : FETCH (FIRST | NEXT) fetchCountClause? (ROW | ROWS) (ONLY | WITH TIES)
    ;

fetchCountClause
    : {isTokenNotAhead(PERCENT_KEYWORD)}? expression PERCENT_KEYWORD?
    ;

forUpdateClause
    : FOR UPDATE (OF forUpdateColumnList)? forUpdateLockAction?
    | FOR READ ONLY
    ;

forUpdateColumnList
    : qualifiedName (COMMA qualifiedName)*
    ;

forUpdateLockAction
    : NOWAIT
    | WAIT unsignedIntegerNumber
    | unsignedIntegerNumber? SKIP_KEYWORD LOCKED
    ;

withUrClause
    : WITH UR
    ;

forXmlPathClause
    : FOR XML PATH LPAREN expression RPAREN
    ;

queryRemainder
    : setOperator setQuantifier? setCorrespondingClause? selectOperand {disallowSiblingsOrder();}
    ;

setOperator
    : UNION
    | INTERSECT
    | MINUS_SET
    | EXCEPT
    ;

setQuantifier
    : ALL
    | DISTINCT
    | UNIQUE
    ;

setCorrespondingClause
    : CORRESPONDING (BY columnNameList)?
    ;

hierarchicalClause
    : startWithClause connectByClause+ {allowSiblingsOrder();}
    | connectByClause+ startWithClause? {allowSiblingsOrder();}
    ;

startWithClause
    : START WITH expression
    ;

connectByClause
    : CONNECT BY NOCYCLE? expression
    ;

insertStatement
    : withClause? INSERT (singleInsertStatement | multiInsertStatement)
    ;

singleInsertStatement
    : INTO? insertTarget tableAlias? singleInsertBody returningClause? dmlErrorLoggingClause?
    ;

insertTarget
    : qualifiedName tableIndexClause? partitionExtensionClause*
    | LPAREN selectStatement RPAREN tableAlias?
    ;

multiInsertStatement
    : ALL multiInsertIntoList selectStatement
    | (ALL | FIRST)? multiInsertConditionList multiInsertElseClause? selectStatement
    ;

multiInsertIntoList
    : multiInsertInto+
    ;

multiInsertInto
    : INTO insertTarget tableAlias? columnNameList? (VALUES valueRows)?
    ;

multiInsertConditionList
    : multiInsertCondition (COMMA? multiInsertCondition)*
    ;

multiInsertCondition
    : WHEN expression THEN multiInsertIntoList
    ;

multiInsertElseClause
    : ELSE multiInsertIntoList
    ;

singleInsertBody
    : columnNameList? singleInsertDataBody
    | DEFAULT VALUES
    ;

singleInsertDataBody
    : VALUES valueRows
    | selectStatement
    | LPAREN selectStatement RPAREN
    | TABLE insertTableSource
    ;

insertTableSource
    : qualifiedName tableIndexClause? partitionExtensionClause*
    ;

valueRows
    : LPAREN insertValueList? RPAREN (COMMA LPAREN insertValueList? RPAREN)*
    ;

insertValueList
    : insertValue (COMMA insertValue)*
    ;

insertValue
    : DEFAULT
    | expression
    ;

updateStatement
    : withClause? UPDATE updateTargetList SET assignmentList fromClause? whereClause? returningClause? limitCondition? dmlErrorLoggingClause?
    ;

updateTargetList
    : tableSource (COMMA tableSource)*
    ;

assignmentList
    : multiColumnAssignment
    | assignment (COMMA assignment)*
    ;

assignment
    : qualifiedName EQ (expression | DEFAULT)
    ;

multiColumnAssignment
    : columnNameList EQ LPAREN selectStatement RPAREN tableAlias?
    ;

deleteStatement
    : withClause? DELETE topClause FROM? deleteTarget deleteMultiTableClause? whereClause? returningClause? dmlErrorLoggingClause?
    | withClause? DELETE FROM? deleteTarget deleteMultiTableClause? whereClause? returningClause? limitCondition? dmlErrorLoggingClause?
    ;

deleteTarget
    : tablePrimary
    ;

deleteMultiTableClause
    : (FROM | USING) deleteTableList
    ;

deleteTableList
    : tableSource (COMMA tableSource)*
    ;

mergeStatement
    : MERGE INTO mergeIntoTarget tableAlias? USING mergeSource ON mergeOnCondition mergeClause+ dmlErrorLoggingClause?
    ;

mergeIntoTarget
    : qualifiedName tableIndexClause? partitionExtensionClause*
    | LPAREN selectStatement RPAREN
    ;

mergeSource
    : tableSource
    ;

mergeOnCondition
    : LPAREN expression RPAREN
    | expression
    ;

mergeClause
    : WHEN MATCHED THEN UPDATE SET assignmentList whereClause? (DELETE whereClause?)?
    | WHEN NOT MATCHED THEN INSERT columnNameList? VALUES LPAREN insertValueList? RPAREN whereClause?
    ;

dmlErrorLoggingClause
    : LOG ERRORS (INTO qualifiedName)? (LPAREN expression RPAREN)? REJECT LIMIT (UNLIMITED | expression)
    ;

returningClause
    : (RETURN | RETURNING) expressionList (BULK COLLECT)? INTO returnTargetList
    ;

returnTargetList
    : returnTarget (COMMA returnTarget)*
    ;

returnTarget
    : bindValue
    | qualifiedName
    ;

flashbackStatement
    : FLASHBACK TABLE qualifiedName (COMMA qualifiedName)* TO (SCN | LSN | TIMESTAMP) expression ((ENABLE | DISABLE) TRIGGERS)?
    ;

refreshMaterializedViewStatement
    : REFRESH MATERIALIZED VIEW qualifiedName materializedViewRefreshAction?
    ;

createStatement
    : CREATE OR REPLACE createReplaceTarget
    | CREATE createTarget
    ;

createReplaceTarget
    : viewCreate
    | indexCreate
    | procedureCreate
    | functionCreate
    | triggerCreate
    | synonymCreate
    | replaceableObjectCreate
    ;

createTarget
    : tableCreate
    | materializedViewLogCreate
    | viewCreate
    | indexCreate
    | schemaCreate
    | sequenceCreate
    | userCreate
    | roleCreate
    | procedureCreate
    | functionCreate
    | triggerCreate
    | synonymCreate
    | objectCreate
    ;

tableCreate
    : ((GLOBAL | LOCAL)? TEMPORARY)? TABLE ifNotExists? targetTable=qualifiedName tableCreateBody
    | HUGE TABLE ifNotExists? targetTable=qualifiedName hugeTableCreateBody
    | ((GLOBAL | LOCAL)? TEMPORARY)? TABLE ifNotExists? targetTable=qualifiedName LIKE likeSourceTable=qualifiedName tableCreateClauseSequence
    | EXTERNAL TABLE ifNotExists? targetTable=qualifiedName externalTableCreateBody
    ;

tableCreateBody
    : LPAREN ctasTableElementList RPAREN tableCtasPrefixClause* AS selectStatement tableCtasTailClause*
    | tableCtasPrefixClause+ AS selectStatement tableCtasTailClause*
    | objectTableCreateBody
    | LPAREN tableElementList RPAREN tableCreateClauseSequence
    | AS selectStatement tableCtasTailClause*
    ;

hugeTableCreateBody
    : LPAREN hugeTableElementList RPAREN hugeTableCreateClauseSequence
    | AS selectStatement tableDistributedClause? tablespaceAsmClause?
    ;

hugeTableElementList
    : hugeTableElement (COMMA hugeTableElement)*
    ;

hugeTableElement
    : hugeColumnDefinition
    | hugeTableConstraint
    ;

hugeColumnDefinition
    : identifier dataType hugeColumnAttribute*
    ;

hugeColumnAttribute
    : DEFAULT (ON NULL_LITERAL)? expression
    | CONSTRAINT identifier hugeColumnConstraintAction constraintState?
    | hugeColumnConstraintAction constraintState?
    | hugeColumnStorageClause
    | columnEncryptClause
    | COMMENT STRING
    ;

hugeColumnConstraintAction
    : NOT? NULL_LITERAL
    | uniqueSpec usingIndexTablespaceClause?
    ;

hugeColumnStorageClause
    : STORAGE LPAREN STAT NONE RPAREN
    ;

hugeTableConstraint
    : CONSTRAINT identifier hugeTableConstraintAction constraintState? validateOption?
    | hugeTableConstraintAction constraintState? validateOption?
    ;

hugeTableConstraintAction
    : uniqueSpec columnNameList usingIndexTablespaceClause?
    | CHECK LPAREN expression RPAREN
    ;

hugeTableCreateClauseSequence
    : (tablePartitionClause | partitionGroupTableClause)?
      tablespaceClause?
      hugeTableStorageClause?
      hugeCompressClause?
      hugeLogClause?
      tableDistributedClause?
      tablespaceAsmClause?
    | hugeTableStorageClause
      tablePartitionClause
      tablespaceClause?
      hugeCompressClause?
      hugeLogClause?
      tableDistributedClause?
      tablespaceAsmClause?
    ;

hugeTableStorageClause
    : STORAGE LPAREN hugeTableStorageItem (COMMA hugeTableStorageItem)* RPAREN
    ;

hugeTableStorageItem
    : SECTION LPAREN {isUnsignedIntegerInRangeAhead(1024, 1048576)}? unsignedIntegerNumber RPAREN
    | INITIAL unsignedIntegerNumber
    | FILESIZE LPAREN {isUnsignedIntegerInRangeAhead(16, 1048576)}? unsignedIntegerNumber RPAREN
    | STAT hugeStatMode? hugeStatColumnClause?
    | (WITH | WITHOUT) DELTA
    | ON identifier
    ;

hugeCompressClause
    : COMPRESS hugeCompressionSpec?
      (hugeCompressionColumnList | EXCEPT columnNameList)?
    ;

hugeCompressionColumnList
    : LPAREN hugeCompressionColumn (COMMA hugeCompressionColumn)* RPAREN
    ;

hugeCompressionColumn
    : identifier hugeCompressionSpec?
    ;

hugeCompressionSpec
    : LEVEL {isUnsignedIntegerInRangeAhead(0, 10)}? unsignedIntegerNumber
      (FOR hugeCompressionType)?
    | FOR hugeCompressionType
    ;

hugeCompressionType
    : {isStringLiteralAhead("QUERY", "QUERY LOW", "QUERY HIGH")}? STRING
    ;

hugeLogClause
    : LOG (NONE | LAST | ALL)
    ;

objectTableCreateBody
    : OF qualifiedName (LPAREN ctasTableElementList? RPAREN)? tableCreateClauseSequence
    ;

tableCreateClauseSequence
    : tableCreateNonDistributedClause* tableDistributedClause?
    ;

tableCreateNonDistributedClause
    : tablePartitionClause
    | partitionTableLockClause
    | temporaryTableCommitClause
    | tablespaceClause
    | diskspaceClause
    | segmentCreationClause
    | storageClause
    | compressClause
    | parallelClause
    | rowMovementClause
    | advancedLogClause
    | addLogicLogClause
    | tableAutoIncrementClause
    | partitionGroupTableClause
    | tableOption
    ;

tableCtasPrefixClause
    : tablePartitionClause
    | temporaryTableCommitClause
    | tablespaceClause
    | diskspaceClause
    | storageClause
    | compressClause
    | parallelClause
    | addLogicLogClause
    ;

tableCtasTailClause
    : tableDistributedClause
    | tableAutoIncrementClause
    ;

temporaryTableCommitClause
    : ON COMMIT (DELETE | PRESERVE) ROWS
    ;

tableElementList
    : tableElement (COMMA tableElement)*
    ;

tableElement
    : columnDefinition
    | tableConstraint
    ;

ctasTableElementList
    : ctasTableElement (COMMA ctasTableElement)*
    ;

ctasTableElement
    : ctasColumnDefinition
    | ctasTableConstraint
    ;

ctasColumnDefinition
    : identifier ctasColumnAttribute*
    ;

ctasColumnAttribute
    : CONSTRAINT identifier ctasColumnConstraintAction constraintState?
    | ctasColumnConstraintAction constraintState?
    | DEFAULT expression
    | COMMENT STRING
    ;

ctasColumnConstraintAction
    : NOT? NULL_LITERAL
    | UNIQUE
    | PRIMARY KEY
    | CHECK LPAREN expression RPAREN
    | NOT VISIBLE
    ;

ctasTableConstraint
    : CONSTRAINT identifier ctasTableConstraintAction constraintState?
    | ctasTableConstraintAction constraintState?
    ;

ctasTableConstraintAction
    : UNIQUE columnNameList
    | PRIMARY KEY columnNameList
    | CHECK LPAREN expression RPAREN
    ;

columnDefinition
    : identifier dataType virtualColumnClause columnAttribute* columnTailClause*
    | identifier virtualColumnClause columnAttribute* columnTailClause*
    | identifier dataType columnAttribute* columnTailClause*
    ;

dataType
    : vectorDataType
    | intervalDataType
    | doublePrecisionDataType
    | characterVaryingDataType
    | largeObjectDataType
    | nationalCharacterDataType
    | timeZoneDataType
    | cursorDataType
    | jsonDataType dataTypeArrayBound*
    | {!isKeywordAhead("json") && !isKeywordAhead("jsonb")}?
      qualifiedName dataTypeArgs? dataTypeArrayBound*
    ;

jsonDataType
    : JSON
    | {isKeywordAhead("jsonb")}? identifier
    ;

vectorDataType
    : {isKeywordAhead("vector")}? identifier vectorDataTypeArgs? dataTypeArrayBound*
    ;

vectorDataTypeArgs
    : LPAREN (NUMBER | STAR) (COMMA identifier (COMMA identifier)?)? RPAREN
    ;

cursorDataType
    : CURSOR
    | SYS_REFCURSOR
    ;

dataTypeArrayBound
    : BRACKET_QUOTED_ID
    ;

dataTypeArgs
    : LPAREN NUMBER dataTypeLengthSemantics? RPAREN
    | LPAREN NUMBER COMMA NUMBER RPAREN
    | LPAREN STAR COMMA NUMBER RPAREN
    ;

dataTypeLengthSemantics
    : CHAR
    | BYTE
    ;

typePrecision
    : LPAREN NUMBER RPAREN
    ;

doublePrecisionDataType
    : DOUBLE PRECISION typePrecision?
    ;

characterVaryingDataType
    : characterTypeName VARYING dataTypeArgs?
    ;

nationalCharacterDataType
    : (NCHAR | NATIONAL CHAR | NATIONAL CHARACTER) dataTypeArgs?
    ;

largeObjectDataType
    : characterTypeName LARGE OBJECT
    | BINARY LARGE OBJECT
    ;

characterTypeName
    : CHAR
    | CHARACTER
    | NCHAR
    | NATIONAL CHAR
    | NATIONAL CHARACTER
    ;

timeZoneDataType
    : TIME dataTypeArgs? ((WITH | WITHOUT) TIME ZONE)
    | TIMESTAMP dataTypeArgs? (((WITH LOCAL?) | WITHOUT) TIME ZONE)
    | DATETIME dataTypeArgs? WITH TIME ZONE
    ;

intervalDataType
    : INTERVAL intervalTypeField (TO intervalTypeField)?
    ;

intervalTypeField
    : YEAR typePrecision?
    | MONTH typePrecision?
    | DAY typePrecision?
    | HOUR typePrecision?
    | MINUTE typePrecision?
    | SECOND secondTypePrecision?
    ;

secondTypePrecision
    : LPAREN NUMBER (COMMA NUMBER)? RPAREN
    ;

columnAttribute
    : CONSTRAINT identifier columnConstraintAction constraintState?
    | columnConstraintAction constraintState?
    | DEFAULT (ON NULL_LITERAL)? expression
    | IDENTITY identityArgs?
    | AUTO_INCREMENT
    | COMMENT STRING
    | columnEncryptClause
    ;

identityArgs
    : LPAREN signedIntegerNumber COMMA signedIntegerNumber RPAREN
    ;

signedIntegerNumber
    : (PLUS | MINUS)? unsignedIntegerNumber
    ;

unsignedIntegerNumber
    : {isUnsignedIntegerNumberAhead()}? NUMBER
    ;

columnTailClause
    : storageClause
    | columnEncryptClause
    | COMMENT STRING
    ;

virtualColumnClause
    : (GENERATED ALWAYS?)? AS LPAREN expression RPAREN VIRTUAL? VISIBLE?
    ;

columnConstraintAction
    : NOT? NULL_LITERAL
    | uniqueSpec usingIndexTablespaceClause?
    | referenceConstraint
    | CHECK LPAREN expression RPAREN
    | NOT VISIBLE
    ;

uniqueSpec
    : PRIMARY KEY
    | NOT? CLUSTER PRIMARY KEY
    | CLUSTER UNIQUE? KEY
    | UNIQUE
    ;

usingIndexTablespaceClause
    : USING INDEX TABLESPACE (identifier | DEFAULT)
    ;

referenceConstraint
    : (FOREIGN KEY)? REFERENCES PENDANT? qualifiedName columnNameList? referenceMatchClause? referenceActionClause* (WITH INDEX)?
    ;

referenceMatchClause
    : MATCH (FULL | PARTIAL | SIMPLE)
    ;

referenceActionClause
    : ON (UPDATE | DELETE) referenceAction
    ;

referenceAction
    : CASCADE
    | SET NULL_LITERAL
    | SET DEFAULT
    | NO ACTION
    ;

constraintState
    : ENABLE deferrableConstraintClause?
    | DISABLE
    | deferrableConstraintClause
    ;

deferrableConstraintClause
    : DEFERRABLE (INITIALLY (IMMEDIATE | DEFERRED))?
    ;

columnEncryptClause
    : ENCRYPT (
        columnEncryptAlgorithmClause (columnEncryptUsageClause? hashOption?)
        | columnEncryptUsageClause hashOption?
    )?
    ;

columnEncryptAlgorithmClause
    : WITH identifier
    ;

columnEncryptUsageClause
    : AUTO columnEncryptKeyClause?
    | MANUAL columnEncryptKeyClause? columnEncryptUserClause?
    ;

columnEncryptKeyClause
    : BY (WRAPPED columnEncryptWrappedKey | columnEncryptPlainKey)
    ;

columnEncryptPlainKey
    : DOUBLE_QUOTED_ID
    | ID
    | NUMBER
    ;

columnEncryptWrappedKey
    : STRING
    ;

columnEncryptUserClause
    : USER LPAREN identifierList? RPAREN
    ;

externalTableCreateBody
    : LPAREN externalTableElementList RPAREN externalTableFromClause
    ;

externalTableElementList
    : externalColumnDefinition (COMMA externalColumnDefinition)*
    ;

externalColumnDefinition
    : identifier dataType externalColumnAttribute*
    ;

externalColumnAttribute
    : COMMENT STRING
    ;

externalTableFromClause
    : FROM (DATAFILE externalTableDirectoryOption externalTableParmsClause? | externalTableDirectoryOption)
    ;

externalTableDirectoryOption
    : DEFAULT DIRECTORY identifier LOCATION LPAREN STRING RPAREN
    ;

externalTableParmsClause
    : PARMS LPAREN externalTableParm (COMMA externalTableParm)* RPAREN
    ;

externalTableParm
    : (FIELDS | RECORDS) DELIMITED BY externalTableDelimiter
    | ERRORS unsignedIntegerNumber
    | {isKeywordAhead("badfile")}? identifier STRING
    | LOG STRING
    | {isKeywordAhead("null_str")}? identifier STRING
    | SKIP_KEYWORD unsignedIntegerNumber
    | {isKeywordAhead("character_code")}? identifier STRING
    ;

externalTableDelimiter
    : STRING
    | HEX_LITERAL
    ;

tableConstraint
    : CONSTRAINT identifier constraintBody constraintState? validateOption?
    | constraintBody constraintState? validateOption?
    ;

constraintBody
    : uniqueSpec columnNameList usingIndexTablespaceClause? GLOBAL?
    | FOREIGN KEY columnNameList referenceConstraint
    | CHECK LPAREN expression RPAREN
    ;

validateOption
    : VALIDATE
    | NOVALIDATE
    ;

tableOption
    : {!isKeywordAhead("TABLESPACE") && !isKeywordAhead("DISKSPACE")
        && !isKeywordAhead("DISTRIBUTED") && !isKeywordAhead("ADVANCED")
        && !isKeywordAhead("LOCK") && !isKeywordAhead("PARTITIONS")}? identifier (EQ? literalValue)?
    ;

partitionTableLockClause
    : LOCK partitionLockMode
    ;

partitionGroupTableClause
    : USING PARTITION GROUP qualifiedName BY columnNameList
      (SUBPARTITION BY columnNameList)?
    ;

diskspaceClause
    : DISKSPACE (LIMIT unsignedIntegerNumber | UNLIMITED)
    ;

segmentCreationClause
    : SEGMENT CREATION (IMMEDIATE | DEFERRED)
    ;

tablePartitionClause
    : PARTITION BY (rangeTablePartitionClause | hashTablePartitionClause | listTablePartitionClause)
    ;

rangeTablePartitionClause
    : RANGE LPAREN expressionList RPAREN partitionIntervalClause? tableSubpartitionClauses? rangePartitionDefinitionList?
    ;

hashTablePartitionClause
    : HASH LPAREN expressionList RPAREN tableSubpartitionClauses? (partitionQuantity | hashPartitionDefinitionList)?
    ;

listTablePartitionClause
    : LIST LPAREN expressionList RPAREN tableSubpartitionClauses? listPartitionDefinitionList?
    ;

partitionIntervalClause
    : INTERVAL LPAREN expression RPAREN
    ;

partitionMethod
    : HASH
    | RANGE
    | LIST
    ;

partitionQuantity
    : PARTITIONS expression storeInClause?
    ;

storeInClause
    : STORE IN LPAREN qualifiedName (COMMA qualifiedName)* RPAREN
    ;

tableSubpartitionClauses
    : tableSubpartitionClause (COMMA tableSubpartitionClause)*
    ;

tableSubpartitionClause
    : rangeSubpartitionClause
    | hashSubpartitionClause
    | listSubpartitionClause
    ;

rangeSubpartitionClause
    : SUBPARTITION BY RANGE LPAREN expressionList RPAREN rangeSubpartitionTemplate?
    ;

hashSubpartitionClause
    : SUBPARTITION BY HASH LPAREN expressionList RPAREN hashSubpartitionTemplate?
    ;

listSubpartitionClause
    : SUBPARTITION BY LIST LPAREN expressionList RPAREN listSubpartitionTemplate?
    ;

subpartitionQuantity
    : SUBPARTITIONS expression storeInClause?
    ;

rangeSubpartitionTemplate
    : SUBPARTITION TEMPLATE LPAREN rangeSubpartitionDefinition (COMMA rangeSubpartitionDefinition)* RPAREN
    ;

hashSubpartitionTemplate
    : SUBPARTITION TEMPLATE (LPAREN hashSubpartitionDefinition (COMMA hashSubpartitionDefinition)* RPAREN | subpartitionQuantity)
    ;

listSubpartitionTemplate
    : SUBPARTITION TEMPLATE LPAREN listSubpartitionDefinition (COMMA listSubpartitionDefinition)* RPAREN
    ;

rangePartitionDefinitionList
    : LPAREN rangePartitionDefinition (COMMA rangePartitionDefinition)* RPAREN
    ;

rangePartitionDefinition
    : PARTITION identifier rangePartitionDefinitionItem*
    ;

rangePartitionDefinitionItem
    : VALUES rangePartitionValuesClause
    | partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

hashPartitionDefinitionList
    : LPAREN hashPartitionDefinition (COMMA hashPartitionDefinition)* RPAREN
    ;

hashPartitionDefinition
    : PARTITION identifier hashPartitionDefinitionItem*
    ;

hashPartitionDefinitionItem
    : partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

listPartitionDefinitionList
    : LPAREN listPartitionDefinition (COMMA listPartitionDefinition)* RPAREN
    ;

listPartitionDefinition
    : PARTITION identifier listPartitionDefinitionItem*
    ;

listPartitionDefinitionItem
    : VALUES listPartitionValuesClause
    | partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

subpartitionDefinition
    : rangeSubpartitionDefinition
    | hashSubpartitionDefinition
    | listSubpartitionDefinition
    ;

rangeSubpartitionDefinition
    : SUBPARTITION identifier rangeSubpartitionDefinitionItem*
    ;

rangeSubpartitionDefinitionItem
    : VALUES rangePartitionValuesClause
    | partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

hashSubpartitionDefinition
    : SUBPARTITION identifier hashSubpartitionDefinitionItem*
    ;

hashSubpartitionDefinitionItem
    : partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

listSubpartitionDefinition
    : SUBPARTITION identifier listSubpartitionDefinitionItem*
    ;

listSubpartitionDefinitionItem
    : VALUES listPartitionValuesClause
    | partitionSubpartitionDefinitionList
    | subpartitionQuantity
    | tablespaceClause
    | storageClause
    ;

partitionSubpartitionDefinitionList
    : LPAREN subpartitionDefinition (COMMA subpartitionDefinition)* RPAREN
    ;

rangePartitionValuesClause
    : (EQU OR)? LESS THAN LPAREN partitionValueList RPAREN
    ;

listPartitionValuesClause
    : LPAREN partitionValueList RPAREN
    ;

partitionValuesClause
    : rangePartitionValuesClause
    | IN? listPartitionValuesClause
    ;

partitionValueList
    : partitionValue (COMMA partitionValue)*
    ;

partitionValue
    : DEFAULT
    | expression
    ;

tableDistributedClause
    : DISTRIBUTED ((RANDOMLY | FULLY) | BY distributedMethod? LPAREN expressionList RPAREN distributedDefinitionList?)
    ;

distributedMethod
    : RANGE
    | LIST
    | HASH
    ;

distributedDefinitionList
    : LPAREN distributedDefinition (COMMA distributedDefinition)* RPAREN
    ;

distributedDefinition
    : VALUES partitionValuesClause ON identifier
    ;

storageClause
    : STORAGE LPAREN storageItem (COMMA storageItem)* RPAREN
    | STORAGE ON identifier
    ;

storageItem
    : ON (qualifiedName | LPAREN qualifiedName (COMMA qualifiedName)* RPAREN)
    | HASHPARTMAP LPAREN expression RPAREN
    | (WITH | WITHOUT) identifier
    | DISABLE USING LONG ROW
    | USING LONG ROW
    | BRANCH (expression | LPAREN expression COMMA expression RPAREN)?
    | NOBRANCH
    | CLUSTERBTR
    | identifier (EQ? expression)?
    ;

compressClause
    : COMPRESS (LEVEL expression (FOR expression)? columnNameList? | EXCEPT columnNameList | columnNameList)?
    ;

rowMovementClause
    : (ENABLE | DISABLE) ROW MOVEMENT
    ;

advancedLogClause
    : WITH ADVANCED LOG
    ;

addLogicLogClause
    : ADD LOGIC LOG
    ;

tableAutoIncrementClause
    : AUTO_INCREMENT (EQ? unsignedIntegerNumber)?
    ;

viewCreate
    : FORCE? VIEW ifNotExists? qualifiedName columnNameList? AS selectStatement viewCreateClause?
    | MATERIALIZED VIEW qualifiedName columnNameList? materializedViewCreateStorage? materializedViewRefreshClause? materializedViewQueryRewriteOption? AS selectStatement
    ;

viewCreateClause
    : WITH (LOCAL | CASCADED)? CHECK OPTION
    | WITH READ ONLY
    ;

materializedViewCreateStorage
    : materializedViewPrebuiltClause
    | BUILD (IMMEDIATE | DEFERRED) tablePartitionClause? tablespaceClause? storageClause?
    | tablePartitionClause tablespaceClause? storageClause?
    | tablespaceClause storageClause?
    | storageClause
    ;

materializedViewPrebuiltClause
    : FOR prebuiltTable=qualifiedName ON PREBUILT TABLE ((WITH | WITHOUT) REDUCED PRECISION)?
    ;

materializedViewQueryRewriteOption
    : (ENABLE | DISABLE) QUERY REWRITE
    ;

materializedViewRefreshClause
    : REFRESH materializedRefreshOption+
    | NEVER REFRESH
    ;

materializedRefreshOption
    : materializedViewRefreshAction
    | ON (DEMAND | COMMIT)
    | START WITH expression
    | NEXT expression
    | WITH PRIMARY KEY
    | WITH ROWID
    | materializedViewFullRefreshMethod
    ;

materializedViewRefreshAction
    : FAST
    | COMPLETE materializedViewFullRefreshMethod?
    | FORCE materializedViewFullRefreshMethod?
    | materializedViewFullRefreshMethod
    ;

materializedViewFullRefreshMethod
    : USING (DEFAULT | TRUNCATE | DELETE)
    ;

materializedViewLogCreate
    : MATERIALIZED VIEW LOG ON qualifiedName tablespaceClause? storageClause? materializedViewLogWithClause? materializedViewLogPurgeClause?
    ;

materializedViewLogWithClause
    : WITH materializedViewLogItem (COMMA materializedViewLogItem)*
    ;

materializedViewLogItem
    : PRIMARY KEY
    | ROWID columnNameList?
    | SEQUENCE
    | columnNameList
    ;

materializedViewLogPurgeClause
    : PURGE (IMMEDIATE (SYNCHRONOUS | ASYNCHRONOUS)? | NEXT expression | REPEAT expression | START WITH expression ((NEXT | REPEAT) expression)?)
    ;

indexCreate
    : (OR REPLACE)? (CLUSTER | NOT PARTIAL)? (UNIQUE | BITMAP | SPATIAL)? INDEX ifNotExists? qualifiedName ON qualifiedName indexColumnList bitmapJoinClause? indexCreateClause*
    | ARRAY INDEX ifNotExists? qualifiedName ON qualifiedName indexColumnList
    | CONTEXT INDEX ifNotExists? qualifiedName ON qualifiedName indexColumnList contextIndexClause*
    ;

indexColumnList
    : LPAREN indexColumnDefinition (COMMA indexColumnDefinition)* RPAREN
    ;

indexColumnDefinition
    : expression (ASC | DESC)?
    ;

bitmapJoinClause
    : fromClause whereClause
    ;

indexCreateClause
    : LOCAL
    | GLOBAL
    | tablePartitionClause
    | tablespaceClause
    | storageClause
    | NOSORT
    | ONLINE
    | REVERSE
    | UNUSABLE
    | parallelClause
    ;

contextIndexClause
    : tablespaceClause
    | storageClause
    | LEXER expression
    | SYNC TRANSACTION?
    ;

tablespaceClause
    : TABLESPACE identifier
    ;

parallelClause
    : PARALLEL NUMBER?
    | NOPARALLEL
    ;

schemaCreate
    : SCHEMA schemaAuthorizationOnly (schemaDefinitionItem SEMI?)*
    | SCHEMA {!isKeywordAhead("AUTHORIZATION")}? schemaName=qualifiedName schemaAuthorizationClause? (schemaDefinitionItem SEMI?)*
    ;

schemaAuthorizationOnly
    : AUTHORIZATION schemaOwner=identifier
    ;

schemaAuthorizationClause
    : AUTHORIZATION schemaOwner=identifier
    ;

schemaDefinitionItem
    : {isSchemaDefinitionCreateAhead()}? createStatement
    | alterStatement
    | grantStatement
    | commentStatement
    ;

sequenceCreate
    : SEQUENCE ifNotExists? qualifiedName sequenceOption*
    ;

sequenceOption
    : START WITH expression
    | INCREMENT BY expression
    | MAXVALUE expression
    | NOMAXVALUE
    | MINVALUE expression
    | NOMINVALUE
    | CYCLE
    | NOCYCLE
    | CACHE expression
    | NOCACHE
    | ORDER
    | NOORDER
    | GLOBAL
    | LOCAL
    ;

userCreate
    : USER ifNotExists? identifier IDENTIFIED userAuthMode userPropertyClause*
    ;

roleCreate
    : ROLE ifNotExists? identifier
    ;

userPropertyClause
    : PASSWORD_POLICY userParameterValue
    | ACCOUNT (LOCK | UNLOCK)
    | ENCRYPT BY userPassword
    | DISKSPACE (LIMIT quotaValue | UNLIMITED)
    | NOT? READ ONLY
    | DROP PROFILE
    | PROFILE identifier
    | resourceLimitClause
    | PASSWORD EXPIRE
    | userAccessClause
    | DEFAULT TABLESPACE GROUP identifier
    | DEFAULT TABLESPACE identifier
    | DEFAULT INDEX TABLESPACE identifier
    | quotaClause+
    ;

userAuthMode
    : BY userPassword hashOption?
    | EXTERNALLY (AS userPassword | RADIUS)?
    ;

alterUserAuthMode
    : BY userPassword (RETAIN CURRENT PASSWORD)? hashOption?
    | EXTERNALLY (AS userPassword | RADIUS)?
    ;

hashOption
    : HASH WITH qualifiedName (NO? SALT)?
    ;

userPassword
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    | NUMBER
    ;

userResourceItem
    : userResourceName userParameterValue
    ;

resourceLimitClause
    : LIMIT userResourceItem (COMMA? userResourceItem)*
    ;

userResourceName
    : SESSION_PER_USER
    | CONNECT_IDLE_TIME
    | CONNECT_TIME
    | CPU_PER_CALL
    | CPU_PER_SESSION
    | MEM_SPACE
    | READ_PER_CALL
    | READ_PER_SESSION
    | GLOBAL_SESSION_PER_USER
    | FAILED_LOGIN_ATTEMPTS
    | PASSWORD_LIFE_TIME
    | PASSWORD_REUSE_TIME
    | PASSWORD_REUSE_MAX
    | PASSWORD_LOCK_TIME
    | PASSWORD_GRACE_TIME
    | INACTIVE_ACCOUNT_TIME
    ;

userParameterValue
    : DEFAULT
    | UNLIMITED
    | unsignedIntegerNumber
    ;

quotaClause
    : QUOTA quotaValue (ON identifier)?
    ;

quotaValue
    : UNLIMITED
    | NUMBER identifier?
    ;

userAccessClause
    : userIpClause
    | userDateTimeClause
    ;

userIpClause
    : (ALLOW_IP | NOT_ALLOW_IP) (NULL_LITERAL | userAccessAction? userIpItem (COMMA userIpItem)*)
    ;

userAccessAction
    : ADD
    | DROP
    ;

userIpItem
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    ;

userDateTimeClause
    : (ALLOW_DATETIME | NOT_ALLOW_DATETIME) userDateTimeItem (COMMA userDateTimeItem)*
    ;

userDateTimeItem
    : userDateTimeValue+
    ;

userDateTimeValue
    : STRING
    | DOUBLE_QUOTED_ID
    | dayOfWeek
    | NUMBER
    | COLON
    | MINUS
    | TO
    ;

dayOfWeek
    : MON
    | TUE
    | WED
    | THU
    | THUR
    | THURS
    | FRI
    | SAT
    | SUN
    ;

procedureCreate
    : PROCEDURE ifNotExists? qualifiedName routineEncryptionClause? routineParameterList? routineAuthidClause? routineDefinition
    ;

functionCreate
    : FUNCTION ifNotExists? qualifiedName aggregateFunctionCreateTail
    | FUNCTION ifNotExists? qualifiedName externalFunctionCreateTail
    | FUNCTION ifNotExists? qualifiedName routineEncryptionClause? functionCalculateClause? standaloneFunctionRoutineSignature routineDefinition
    ;

functionCalculateClause
    : FOR CALCULATE
    ;

aggregateFunctionCreateTail
    : aggregateFunctionParameterList? RETURN dataType aggregateFunctionOption* AGGREGATE USING qualifiedName
    ;

aggregateFunctionParameterList
    : LPAREN (aggregateFunctionParameter (COMMA aggregateFunctionParameter)*)? RPAREN
    ;

aggregateFunctionParameter
    : aggregateFunctionParameterName aggregateFunctionParameterMode? dataType routineParameterDefault?
    ;

aggregateFunctionParameterName
    : identifier
    | INPUT
    ;

aggregateFunctionParameterMode
    : IN OUT
    | OUT IN
    | IN
    | OUT
    ;

aggregateFunctionOption
    : PARALLEL_ENABLE
    | DETERMINISTIC
    ;

externalFunctionCreateTail
    : externalFunctionParameterList? RETURN dataType externalFunctionBody
    ;

externalFunctionParameterList
    : LPAREN (externalFunctionParameter (COMMA externalFunctionParameter)*)? RPAREN
    ;

externalFunctionParameter
    : externalFunctionParameterName externalFunctionParameterMode? dataType
    ;

externalFunctionParameterName
    : identifier
    | INPUT
    ;

externalFunctionParameterMode
    : IN OUT
    | OUT IN
    | IN
    | OUT
    ;

externalFunctionBody
    : EXTERNAL STRING (externalFunctionDirectReference | AND STRING)? USING externalFunctionLanguage
    | AS LANGUAGE externalFunctionLanguage LIBRARY qualifiedName NAME externalFunctionReference
    ;

externalFunctionDirectReference
    : qualifiedName externalJavaSignature?
    | DOUBLE_QUOTED_ID
    ;

externalFunctionReference
    : qualifiedName externalJavaSignature?
    | DOUBLE_QUOTED_ID
    | STRING
    ;

externalJavaSignature
    : LPAREN dataType (COMMA dataType)* RPAREN
    ;

externalFunctionLanguage
    : C_LANGUAGE
    | CS_LANGUAGE
    | JAVA_LANGUAGE
    ;

triggerCreate
    : TRIGGER qualifiedName triggerCreateTail
    ;

functionRoutineSignature
    : routineParameterList? RETURN declarationDataType routineFunctionOption*
    ;

standaloneFunctionRoutineSignature
    : routineParameterList? RETURN declarationDataType standaloneRoutineFunctionOption*
    ;

standaloneRoutineFunctionOption
    : routineFunctionOption
    | routineAuthidClause
    ;

routineParameterList
    : LPAREN (routineParameter (COMMA routineParameter)*)? RPAREN
    ;

routineParameter
    : identifier routineParameterMode? declarationDataType routineParameterDefault?
    ;

routineParameterMode
    : IN OUT
    | OUT IN
    | IN
    | OUT
    ;

routineParameterDefault
    : (DEFAULT | ASSIGN | EQ) expression
    ;

routineFunctionOption
    : RESULT_CACHE
    | DETERMINISTIC
    | PIPELINED
    | PARALLEL_ENABLE
    ;

routineEncryptionClause
    : WITH ENCRYPTION
    ;

routineAuthidClause
    : AUTHID (DEFINER | CURRENT_USER)
    ;

routineDefinition
    : (AS | IS) blockDeclaration* sqlBlockStatement
    ;

triggerCreateTail
    : tableTriggerCreateTail
    | eventTriggerCreateTail
    | timerTriggerCreateTail
    ;

tableTriggerCreateTail
    : (WITH ENCRYPTION)? tableTriggerTiming tableTriggerEventList LOCAL? ON qualifiedName triggerReferencingClause?
      triggerForEachClause? triggerOrderClause? triggerWhenClause? sqlBlockStatement qualifiedName?
    ;

tableTriggerTiming
    : BEFORE
    | AFTER
    | INSTEAD OF
    ;

tableTriggerEventList
    : tableTriggerEvent (OR tableTriggerEvent)*
    ;

tableTriggerEvent
    : INSERT
    | DELETE
    | UPDATE (OF identifierList)?
    ;

triggerReferencingClause
    : REFERENCING triggerReferencingItem+
    ;

triggerReferencingItem
    : OLD ROW? AS? identifier
    | NEW ROW? AS? identifier
    ;

triggerForEachClause
    : FOR EACH (ROW | STATEMENT)
    ;

triggerOrderClause
    : (FOLLOWS | PRECEDES) qualifiedName (COMMA qualifiedName)*
    ;

triggerWhenClause
    : WHEN LPAREN expression RPAREN
    | WHEN expression
    ;

eventTriggerCreateTail
    : (WITH ENCRYPTION)? eventTriggerTiming eventTriggerEventList ON eventTriggerTarget triggerExecuteAtClause?
      triggerWhenClause? sqlBlockStatement qualifiedName?
    ;

eventTriggerTiming
    : BEFORE
    | AFTER
    ;

eventTriggerEventList
    : eventTriggerEvent (OR eventTriggerEvent)*
    ;

eventTriggerEvent
    : DDL
    | CREATE
    | ALTER
    | DROP
    | GRANT
    | REVOKE
    | TRUNCATE
    | COMMENT
    | AUDIT
    | NOAUDIT
    | LOGIN
    | LOGON
    | LOGOUT
    | LOGOFF
    | SERERR
    | BACKUP DATABASE
    | RESTORE DATABASE
    | STARTUP
    | SHUTDOWN
    | CHECKPOINT
    ;

eventTriggerTarget
    : DATABASE
    | SCHEMA
    | identifier DOT SCHEMA
    ;

triggerExecuteAtClause
    : EXECUTE AT_KEYWORD triggerExecuteAtTarget
    ;

triggerExecuteAtTarget
    : qualifiedName unsignedIntegerNumber?
    | unsignedIntegerNumber
    ;

timerTriggerCreateTail
    : (WITH ENCRYPTION)? AFTER TIMER ON DATABASE triggerExecuteAtClause? timerTriggerSchedule triggerWhenClause?
      sqlBlockStatement qualifiedName?
    ;

timerTriggerSchedule
    : FOR ONCE AT_KEYWORD DATETIME expression? triggerExecuteAtClause?
    | timerRate timerDailySchedule? timerDuringDate? triggerExecuteAtClause?
    | timerDuringDate triggerExecuteAtClause?
    | expression timerDuringDate? triggerExecuteAtClause?
    | triggerExecuteAtClause
    ;

timerRate
    : FOR EACH expression MONTH timerMonthDay?
    | FOR EACH expression WEEK expressionList?
    | FOR EACH expression DAY
    ;

timerMonthDay
    : DAY expression (OF WEEK (expression | LAST))?
    ;

timerDailySchedule
    : AT_KEYWORD TIME expression
    | timerDuringTime? FOR EACH expression (MINUTE | SECOND)
    ;

timerDuringTime
    : FROM TIME expression (TO TIME expression)?
    ;

timerDuringDate
    : FROM DATETIME expression (TO DATETIME expression)?
    ;

synonymCreate
    : PUBLIC? SYNONYM ifNotExists? synonymName=qualifiedName FOR synonymTarget=qualifiedName
    ;

objectCreate
    : replaceableObjectCreate
    | typeBodyCreate
    | typeCreate
    | classBodyCreate
    | javaClassCreate
    | classCreate
    | TABLESPACE ifNotExists? qualifiedName tablespaceCreateTail
    | DOMAIN ifNotExists? qualifiedName domainCreateTail
    | operatorCreate
    | PROFILE ifNotExists? identifier profileCreateTail?
    | partitionGroupCreate
    ;

partitionGroupCreate
    : PARTITION GROUP qualifiedName partitionGroupPartitionClause storageClause?
    ;

partitionGroupPartitionClause
    : PARTITION BY (
        RANGE LPAREN dataType (COMMA dataType)* RPAREN
          partitionGroupIntervalClause? tableSubpartitionClauses? rangePartitionDefinitionList
        | HASH LPAREN dataType (COMMA dataType)* RPAREN
          tableSubpartitionClauses?
          (PARTITIONS expression (storeInClause | storageClause)? | hashPartitionDefinitionList)
        | LIST LPAREN dataType RPAREN tableSubpartitionClauses? listPartitionDefinitionList
    )
    ;

partitionGroupIntervalClause
    : INTERVAL (LPAREN expression RPAREN | expression)
    ;

replaceableObjectCreate
    : PUBLIC? LINK ifNotExists? qualifiedName linkCreateTail
    | PACKAGE BODY qualifiedName packageBodyCreateTail
    | PACKAGE ifNotExists? qualifiedName packageSpecCreateTail
    | DIRECTORY ifNotExists? identifier AS STRING
    | CONTEXT ifNotExists? identifier USING qualifiedName
    | LIBRARY ifNotExists? qualifiedName AS STRING
    | typeBodyCreate
    | typeCreate
    | classBodyCreate
    | javaClassCreate
    | classCreate
    ;

typeCreate
    : TYPE qualifiedName typeCreateTail
    ;

typeCreateTail
    : (WITH ENCRYPTION)? (AUTHID (DEFINER | CURRENT_USER))? typeDefinition
    ;

typeDefinition
    : objectTypeDefinition typeCreateOption*
    | recordTypeDefinition
    | collectionTypeDefinition
    ;

objectTypeDefinition
    : (AS | IS) OBJECT LPAREN typeObjectMemberList RPAREN
    | UNDER qualifiedName LPAREN typeObjectMemberList RPAREN
    ;

dmsqlTypeDefinition
    : recordTypeDefinition
    | collectionTypeDefinition
    | refCursorTypeDefinition
    ;

recordTypeDefinition
    : (AS | IS) RECORD LPAREN recordTypeMemberList RPAREN
    ;

collectionTypeDefinition
    : (AS | IS) VARRAY LPAREN varrayCapacity RPAREN
      OF declarationDataType collectionNullClause?
    | (AS | IS) TABLE OF declarationDataType collectionNullClause? (INDEX BY declarationDataType)?
    | (AS | IS) ARRAY arrayType=declarationDataType {isValidArrayDeclaration($arrayType.text)}?
    ;

varrayCapacity
    : capacity=NUMBER {isUnsignedIntegerInRange($capacity.text, 0, 2147483647L)}?
    | expression
    ;

refCursorTypeDefinition
    : (AS | IS) REF CURSOR (RETURN declarationDataType)?
    ;

collectionNullClause
    : NOT NULL_LITERAL
    ;

typeObjectMemberList
    : typeObjectMember (COMMA typeObjectMember)*
    ;

typeObjectMember
    : typeMethodSpec
    | identifier declarationDataType
    ;

recordTypeMemberList
    : recordTypeMember (COMMA recordTypeMember)*
    ;

recordTypeMember
    : identifier declarationDataType routineParameterDefault?
    ;

typeMethodSpec
    : CONSTRUCTOR FUNCTION identifier routineParameterList? constructorReturnClause routineFunctionOption*
    | typeMethodInheritancePrefix* (MAP | ORDER)? (STATIC | MEMBER)? FUNCTION identifier
      routineParameterList? typeMethodReturnClause routineFunctionOption*
    | typeMethodInheritancePrefix* (STATIC | MEMBER)? PROCEDURE identifier routineParameterList?
    ;

typeMethodInheritancePrefix
    : NOT? OVERRIDING
    | NOT? FINAL
    | NOT? INSTANTIABLE
    ;

typeMethodReturnClause
    : RETURN declarationDataType (AS identifier)?
    ;

constructorReturnClause
    : RETURN {isKeywordAhead("self")}? identifier
      AS {isKeywordAhead("result")}? identifier
    ;

typeCreateOption
    : NOT? FINAL
    | NOT? INSTANTIABLE
    | NOT? PERSISTABLE
    ;

typeBodyCreate
    : TYPE BODY qualifiedName typeBodyCreateTail
    ;

typeBodyCreateTail
    : (WITH ENCRYPTION)? (AS | IS) typeBodyItem+ END
    ;

typeBodyItem
    : typeMethodBody SEMI*
    ;

typeMethodBody
    : typeMethodHeader routineDefinition
    ;

typeMethodHeader
    : CONSTRUCTOR FUNCTION identifier routineParameterList? constructorReturnClause routineFunctionOption*
    | typeMethodInheritancePrefix* (MAP | ORDER)? (STATIC | MEMBER)? FUNCTION identifier
      routineParameterList? typeMethodReturnClause routineFunctionOption*
    | typeMethodInheritancePrefix* (STATIC | MEMBER)? PROCEDURE identifier routineParameterList?
    ;

classCreate
    : CLASS ifNotExists? qualifiedName classCreateTail
    ;

javaClassCreate
    : JAVA_LANGUAGE javaClassModifier* CLASS identifier javaClassExtendsClause? LBRACE javaClassMember+ RBRACE
    ;

javaClassModifier
    : PUBLIC
    | ABSTRACT
    | FINAL
    ;

javaClassExtendsClause
    : EXTENDS qualifiedName
    ;

javaClassMember
    : javaVisibility? javaMemberModifier* javaClassMemberCore
    ;

javaVisibility
    : PUBLIC
    | PRIVATE
    ;

javaMemberModifier
    : STATIC
    | FINAL
    | ABSTRACT
    | OVERRIDE
    ;

javaClassMemberCore
    : dataType identifier javaClassParameterList (javaClassBlock SEMI* | SEMI+)
    | identifier javaClassParameterList javaClassBlock SEMI*
    | dataType identifierList routineParameterDefault? SEMI+
    ;

javaClassParameterList
    : LPAREN javaClassParameter? (COMMA javaClassParameter)* RPAREN
    ;

javaClassParameter
    : dataType identifier?
    ;

javaClassBlock
    : cStyleBlockStatement
    ;

classCreateTail
    : (WITH ENCRYPTION)? (UNDER qualifiedName)? (NOT? FINAL)? (NOT? INSTANTIABLE)?
      (NOT? PERSISTABLE)? (AUTHID (DEFINER | CURRENT_USER))?
      (AS | IS) classMember+ END qualifiedName?
    ;

classBodyCreate
    : CLASS BODY qualifiedName classBodyCreateTail
    ;

classBodyCreateTail
    : (WITH ENCRYPTION)? (AS | IS) classBodyItem* END qualifiedName?
    ;

classBodyItem
    : typeMethodBody SEMI*
    | classBodyInitializer SEMI*
    ;

classBodyInitializer
    : initializerDeclarationSection? BEGIN blockItem* exceptionSection?
    ;

classMember
    : blockTypeDeclaration SEMI+
    | packageSubtypeDeclaration SEMI+
    | classVariableDeclaration SEMI+
    | packageCursorDeclaration SEMI+
    | packageExceptionDeclaration SEMI+
    | typeMethodSpec SEMI+
    ;

classVariableDeclaration
    : identifierList declarationDataType collectionNullClause? routineParameterDefault?
    ;

linkCreateTail
    : CONNECT linkConnectType? WITH identifier IDENTIFIED BY userPassword USING STRING linkOptionClause?
    ;

linkConnectType
    : STRING
    ;

linkOptionClause
    : OPTION LPAREN linkOptionItem (COMMA linkOptionItem)* RPAREN
    ;

linkOptionItem
    : identifier EQ expression
    ;

packageSpecCreateTail
    : (WITH ENCRYPTION)? (AUTHID (DEFINER | CURRENT_USER))? (AS | IS) packageSpecItem+ END qualifiedName?
    ;

packageBodyCreateTail
    : (WITH ENCRYPTION)? (AS | IS) (packageBodyItem+ packageBodyInitializer? | packageBodyInitializer) END qualifiedName?
    ;

packageSpecItem
    : packageVariableDeclaration SEMI+
    | packageCursorDeclaration SEMI+
    | packageExceptionDeclaration SEMI+
    | packageSubtypeDeclaration SEMI+
    | blockTypeDeclaration SEMI+
    | blockPragmaDeclaration SEMI+
    | packageProcedureDeclaration SEMI+
    | packageFunctionDeclaration SEMI+
    ;

packageBodyItem
    : packageVariableDeclaration SEMI+
    | packageCursorDeclaration SEMI+
    | packageExceptionDeclaration SEMI+
    | packageSubtypeDeclaration SEMI+
    | blockTypeDeclaration SEMI+
    | blockPragmaDeclaration SEMI+
    | packageProcedureDeclaration SEMI+
    | packageFunctionDeclaration SEMI+
    | packageProcedureImplementation SEMI*
    | packageFunctionImplementation SEMI*
    ;

packageVariableDeclaration
    : identifier CONSTANT? declarationDataType collectionNullClause? routineParameterDefault?
    ;

packageCursorDeclaration
    : CURSOR identifier cursorDefinitionClause?
    | identifier CURSOR cursorDefinitionClause? cursorAssignmentClause?
    ;

cursorDefinitionClause
    : cursorFastClause? cursorDefinition
    ;

cursorFastClause
    : FAST
    | NO FAST
    ;

cursorDefinition
    : (IS | FOR) cursorSource
    | cursorParameterList IS selectStatement
    | cursorParameterList? RETURN declarationDataType IS selectStatement
    ;

cursorSource
    : selectStatement
    | TABLE qualifiedName
    ;

cursorParameterList
    : LPAREN cursorParameter? (COMMA cursorParameter)* RPAREN
    ;

cursorParameter
    : identifier IN? declarationDataType routineParameterDefault?
    ;

cursorAssignmentClause
    : (ASSIGN | EQ) cursorSourceExpression
    ;

cursorSourceExpression
    : qualifiedName
    | cursorExpression
    ;

packageExceptionDeclaration
    : identifier EXCEPTION (FOR expression (COMMA expression)?)?
    ;

packageSubtypeDeclaration
    : SUBTYPE identifier IS declarationDataType collectionNullClause?
    ;

packageProcedureDeclaration
    : PROCEDURE identifier routineParameterList?
    ;

packageFunctionDeclaration
    : FUNCTION identifier functionRoutineSignature
    ;

packageProcedureImplementation
    : PROCEDURE identifier routineParameterList? routineDefinition
    ;

packageFunctionImplementation
    : FUNCTION identifier functionRoutineSignature routineDefinition
    ;

packageBodyInitializer
    : initializerDeclarationSection? BEGIN blockItem* exceptionSection?
    ;

initializerDeclarationSection
    : DECLARE? blockDeclaration+
    ;

domainCreateTail
    : AS? dataType domainDefaultClause? domainConstraint*
    ;

operatorCreate
    : OPERATOR operatorQualifiedName LPAREN FUNCTION qualifiedName COMMA operatorArgument (COMMA operatorArgument)? RPAREN
    ;

operatorArgument
    : (LEFTARG | RIGHTARG) operatorArgumentType
    ;

operatorArgumentType
    : operatorDataType
    | NULL_LITERAL
    ;

operatorDataType
    : DOUBLE PRECISION
    | characterTypeName VARYING
    | NCHAR
    | NATIONAL CHAR
    | NATIONAL CHARACTER
    | characterTypeName LARGE OBJECT
    | BINARY LARGE OBJECT
    | TIME ((WITH | WITHOUT) TIME ZONE)?
    | TIMESTAMP (((WITH LOCAL?) | WITHOUT) TIME ZONE)?
    | DATETIME (WITH TIME ZONE)?
    | qualifiedName
    ;

domainDefaultClause
    : DEFAULT expression
    ;

domainConstraint
    : (CONSTRAINT identifier)? CHECK LPAREN expression RPAREN
    ;

profileCreateTail
    : resourceLimitClause
    ;

tablespaceCreateTail
    : tablespaceDatafileClause cacheClause? tablespaceEncryptClause? tablespaceGeneratedCopyClause?
      tablespaceAsmClause? tablespaceHugePathClause? storageClause?
    ;

tablespaceDatafileClause
    : DATAFILE tablespaceFileItem (COMMA tablespaceFileItem)*
    ;

tablespaceFileItem
    : tablespaceFilePath (MIRROR tablespaceFilePath)? SIZE tablespaceSizeValue tablespaceAutoextendClause?
    ;

tablespaceFilePath
    : STRING
    | qualifiedName
    ;

tablespaceSizeValue
    : NUMBER ID?
    | expression
    ;

tablespaceAutoextendClause
    : AUTOEXTEND (ON (NEXT tablespaceSizeValue)? (MAXSIZE (tablespaceSizeValue | UNLIMITED))? | OFF)
    ;

tablespaceGeneratedCopyClause
    : COPY expression MICRO?
    ;

tablespaceAsmClause
    : STRIPING NUMBER (HIGH | NORMAL | EXTERNAL)?
    | HIGH
    | NORMAL
    | EXTERNAL
    ;

cacheClause
    : CACHE EQ? identifier
    ;

tablespaceEncryptClause
    : ENCRYPT WITH identifier (BY (WRAPPED tablespacePassword | {isTokenNotAhead(WRAPPED)}? tablespacePassword))?
    ;

tablespacePassword
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    | NUMBER
    ;

tablespaceHugePathClause
    : WITH HUGE PATH tablespaceFilePath MICRO?
    ;

adminStatement
    : ALTER DATABASE alterDatabaseAction
    | ALTER SYSTEM alterSystemAction
    | BACKUP backupStatementTail
    | RESTORE restoreStatementTail
    | RECOVER recoverStatementTail
    | SHOW showBackupsetTail
    | CHECK checkStatementTail
    | CHECKPOINT LPAREN expression RPAREN
    | DUMP dumpStatementTail
    | REMOVE removeStatementTail
    | REPAIR repairStatementTail
    | LOAD loadBackupsetsTail
    | CONFIGURE configureStatementTail?
    | MERGE DATABASE mergeDatabaseTail
    | dataWatcherAdminProcedure LPAREN routineArgumentList? RPAREN
    ;

dataWatcherAdminProcedure
    : SP_SET_OGUID
    | SP_APPLY_KEEP_PKG
    | SP_CLEAR_ARCH_SEND_INFO
    | SP_CLEAR_RAPPLY_STAT
    ;

alterDatabaseAction
    : RESIZE LOGFILE backupFilePath TO expression
    | ADD LOGFILE alterDatabaseFileItem (COMMA alterDatabaseFileItem)*
    | DROP LOGFILE backupFilePath
    | ADD NODE LOGFILE alterDatabaseFileItem COMMA alterDatabaseFileItem (COMMA alterDatabaseFileItem)*
    | RENAME LOGFILE backupFilePath (COMMA backupFilePath)* TO backupFilePath (COMMA backupFilePath)*
    | OPEN FORCE?
    | MOUNT
    | SUSPEND
    | NORMAL FORCE?
    | PRIMARY FORCE?
    | STANDBY FORCE?
    | ARCHIVELOG CURRENT?
    | NOARCHIVELOG
    | (ADD | MODIFY | DELETE) ARCHIVELOG backupFilePath
    ;

alterSystemAction
    : SWITCH LOGFILE
    | ARCHIVE LOG CURRENT
    ;

alterDatabaseFileItem
    : backupFilePath SIZE expression
    ;

backupStatementTail
    : DATABASE backupFilePath? backupType? backupAdminOption*
    | TABLESPACE qualifiedName backupType? backupAdminOption*
    | TABLE qualifiedName backupAdminOption*
    | backupArchiveLogTail
    ;

backupArchiveLogTail
    : archiveLogKeyword backupArchiveRange? backupArchiveFilter? (DELETE INPUT)?
        (DATABASE backupFilePath)? backupAdminOption*
    ;

backupArchiveRange
    : ALL
    | FROM LSN expression
    | UNTIL LSN expression
    | LSN BETWEEN expression AND expression
    | FROM TIME backupFilePath
    | UNTIL TIME backupFilePath
    | TIME BETWEEN backupFilePath AND backupFilePath
    ;

backupArchiveFilter
    : NOT BACKED UP (expression TIMES | SINCE TIME backupFilePath)?
    ;

backupType
    : FULL (DDL_CLONE | SHADOW)?
    | DDL_CLONE
    | SHADOW
    | INCREMENT CUMULATIVE?
    ;

backupAdminOption
    : BACKUPSET backupFilePath?
    | FORMAT backupFilePath
    | (TO | BACKUPNAME) backupName
    | WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*
    | BASE ON BACKUPSET backupFilePath
    | FROM LSN expression
    | DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | BACKUPINFO backupFilePath
    | MAXPIECESIZE expression
    | LIMIT backupLimit
    | IDENTIFIED BY backupPassword backupEncryptionOption*
    | COMPRESSED (LEVEL expression)?
    | WITHOUT LOG
    | WITHOUT MIRROR
    | TRACE (FILE backupFilePath | LEVEL expression)?
    | TASK THREAD expression
    | PARALLEL expression? (READ SIZE expression)?
    | USE BAK_MAGIC expression
    ;

backupLimit
    : READ SPEED expression (WRITE SPEED expression)?
    | WRITE SPEED expression
    | NUMBER
    ;

backupEncryptionOption
    : WITH ENCRYPTION expression
    | ENCRYPT WITH backupName
    ;

restoreStatementTail
    : DATABASE restoreDatabaseTarget restoreFromClause restoreOption*
    | DATABASE backupFilePath restoreTablespaceTail
    | TABLE ({!isKeywordAhead("STRUCT") && !isKeywordAhead("KEEP") && !isKeywordAhead("WITHOUT")
        && !isKeywordAhead("FROM")}? qualifiedName)? restoreTableModifier* restoreFromClause restoreTableOption*
    | archiveLogKeyword restoreArchiveTail
    ;

restoreTablespaceTail
    : TABLESPACE qualifiedName (WITH CHECK)? restoreDatafileClause? restoreFromClause restoreOption*
    ;

restoreDatafileClause
    : DATAFILE restoreDatafileItem (COMMA restoreDatafileItem)*
    ;

restoreDatafileItem
    : expression
    ;

restoreTableModifier
    : STRUCT
    | KEEP TRXID
    | WITHOUT (INDEX | CONSTRAINT)
    ;

restoreTableOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | IDENTIFIED BY backupPassword (ENCRYPT WITH backupName)?
    | TRACE (FILE backupFilePath | LEVEL expression)?
    ;

restoreArchiveTail
    : (WITH CHECK)? restoreFromClause restoreArchiveOption* backupArchiveRange?
        TO (ARCHIVEDIR | DATABASE) backupFilePath (OVERWRITE expression)?
    ;

restoreArchiveOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | IDENTIFIED BY backupPassword (ENCRYPT WITH backupName)?
    | TASK THREAD expression
    | NOT PARALLEL
    ;

restoreDatabaseTarget
    : backupFilePath restoreDatabaseTargetOption*
    | TO backupFilePath restoreDirectoryTargetOption*
    ;

restoreDirectoryTargetOption
    : TO SHADOW
    | WITH CHECK
    | OVERWRITE
    | WITHOUT MIRROR
    ;

restoreDatabaseTargetOption
    : TO SHADOW
    | WITH CHECK
    | REUSE DMINI
    | REUSE_DMINI
    | WITHOUT SPACE
    | WITHOUT MIRROR
    | AUTO EXTEND
    | OVERWRITE
    ;

restoreFromClause
    : FROM (BACKUPSET backupFilePath | BACKUPNAME backupName)
    ;

restoreOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | IDENTIFIED BY backupPassword backupEncryptionOption*
    | WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*
    | MAPPED FILE backupFilePath
    | TASK THREAD expression
    | RENAME TO backupName
    | USE BAK_MAGIC expression
    | IGNORE CORRUPT
    | AUTO CLEAR
    | AUTO_CLEAR
    ;

recoverStatementTail
    : DATABASE backupFilePath UPDATE DB_MAGIC
    | DATABASE backupFilePath TABLESPACE qualifiedName recoverTablespaceOption*
    | DATABASE backupFilePath (FOR STANDBY)? (recoverArchiveClause | restoreFromClause recoverBackupOption*)
    | DATABASE backupFilePath
    ;

recoverTablespaceOption
    : WITH ARCHIVEDIR backupFilePath (COMMA backupFilePath)*
    | USE DB_MAGIC expression
    | UNTIL TIME backupFilePath
    | UNTIL LSN expression
    ;

recoverArchiveClause
    : WITH ARCHIVEDIR backupFilePath (COMMA backupFilePath)* recoverArchiveOption*
    ;

recoverArchiveOption
    : USE DB_MAGIC expression
    | UNTIL TIME backupFilePath
    | UNTIL LSN expression
    ;

recoverBackupOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | IDENTIFIED BY backupPassword backupEncryptionOption*
    | USE BAK_MAGIC expression
    | UNTIL END_LSN
    ;

showBackupsetTail
    : BACKUPSET backupFilePath showDeviceClause? RECURSIVE? showDatabaseBackupDirectoryClause?
        showBackupsetInfoClause? showBackupsetOutputClause?
    | BACKUPSETS showDeviceClause? showDatabaseBackupDirectoryClause? showBackupsetInfoClause?
        (USE DB_MAGIC expression)? showBackupsetOutputClause?
    ;

showDeviceClause
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    ;

showDatabaseBackupDirectoryClause
    : DATABASE backupFilePath (WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*)?
    | WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*
    ;

showBackupsetInfoClause
    : INFO showBackupsetInfoType (COMMA showBackupsetInfoType)*
    ;

showBackupsetInfoType
    : DB
    | META
    | FILE
    | TABLESPACE
    | TABLE
    ;

showBackupsetOutputClause
    : TO backupFilePath (FORMAT (TXT | XML))?
    ;

checkStatementTail
    : BACKUPSET backupFilePath checkBackupsetOption*
    ;

checkBackupsetOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | DATABASE backupFilePath
    ;

dumpStatementTail
    : BACKUPSET backupFilePath (DEVICE TYPE backupMediaType (PARMS backupFilePath)?)?
        ((DATABASE | TO) backupFilePath)? MAPPED FILE backupFilePath
    ;

removeStatementTail
    : BACKUPSET backupFilePath removeBackupsetOption*
    | backupsetType? BACKUPSETS removeBackupsetsOption*
    ;

removeBackupsetOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | DATABASE backupFilePath
    | CASCADE
    ;

removeBackupsetsOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | DATABASE backupFilePath
    | WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*
    | UNTIL TIME backupFilePath
    | BEFORE expression
    | CASCADE
    ;

backupsetType
    : DATABASE
    | TABLESPACE qualifiedName?
    | TABLE qualifiedName?
    | archiveLogKeyword
    ;

repairStatementTail
    : archiveLogKeyword DATABASE backupFilePath
    ;

loadBackupsetsTail
    : BACKUPSETS FROM DEVICE TYPE backupMediaType (PARMS backupFilePath)?
        (WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*)?
        TO BACKUPDIR backupFilePath
    ;

configureStatementTail
    : CLEAR
    | DEFAULT configureDefaultClause?
    ;

configureDefaultClause
    : DEVICE (TYPE backupMediaType (PARMS backupFilePath)? | CLEAR)?
    | TRACE configureTraceDefault?
    | BACKUPDIR (((ADD | DELETE)? backupFilePath (COMMA backupFilePath)*) | CLEAR)?
    | ARCHIVEDIR (((ADD | DELETE)? backupFilePath (COMMA backupFilePath)*) | CLEAR)?
    | OPEN FILES (expression | CLEAR)?
    ;

configureTraceDefault
    : FILE backupFilePath (TRACE LEVEL expression)?
    | LEVEL expression
    | CLEAR
    ;

mergeDatabaseTail
    : backupFilePath restoreDatabaseTargetOption* FROM BACKUPSET backupFilePath mergeDatabaseOption*
    ;

mergeDatabaseOption
    : DEVICE TYPE backupMediaType (PARMS backupFilePath)?
    | IDENTIFIED BY backupPassword backupEncryptionOption*
    | WITH BACKUPDIR backupFilePath (COMMA backupFilePath)*
    | TASK THREAD expression
    | RENAME TO backupName
    | USE BAK_MAGIC expression
    | IGNORE CORRUPT
    | AUTO CLEAR
    | AUTO_CLEAR
    ;

archiveLogKeyword
    : ARCHIVELOG
    | ARCHIVE LOG
    ;

backupMediaType
    : DISK
    | TAPE
    | identifier
    ;

backupFilePath
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    ;

backupName
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    | NUMBER
    ;

backupPassword
    : STRING
    | DOUBLE_QUOTED_ID
    | identifier
    | NUMBER
    ;

statStatement
    : STAT statSampleClause? ON statTarget GLOBAL?
    ;

statSampleClause
    : expression statSizeClause?
    ;

statSizeClause
    : SIZE expression
    ;

statTarget
    : INDEX qualifiedName
    | qualifiedName statColumnList?
    ;

statColumnList
    : LPAREN identifierList RPAREN
    ;

statProcedureStatement
    : CALL? statProcedureName LPAREN expressionList? RPAREN
    ;

statProcedureName
    : SP_TAB_INDEX_STAT_INIT
    | SP_DB_STAT_INIT
    | SP_INDEX_STAT_INIT
    | SP_COL_STAT_INIT
    | SP_TAB_COL_STAT_INIT
    | SP_STAT_ON_TABLE_COLS
    | SP_TAB_STAT_INIT
    | SP_SQL_STAT_INIT
    | SP_INDEX_STAT_DEINIT
    | SP_COL_STAT_DEINIT
    | SP_TAB_COL_STAT_DEINIT
    | SP_TAB_STAT_DEINIT
    | SP_UPDATE_SYSSTATS
    | SP_TAB_MSTAT_DEINIT
    | SP_CREATE_AUTO_STAT_TRIGGER
    | SP_FLUSH_MODIFICATIONS_INFO
    | SP_CLEAN_MODIFICATIONS
    ;

alterStatement
    : ALTER alterTarget
    ;

alterTarget
    : TABLE qualifiedName alterTableAction
    | INDEX qualifiedName alterIndexAction
    | CONTEXT INDEX contextIndexName=qualifiedName ON contextTableName=qualifiedName alterContextIndexAction
    | VIEW qualifiedName alterViewAction
    | MATERIALIZED VIEW qualifiedName alterMaterializedViewAction?
    | SEQUENCE qualifiedName alterSequenceAction
    | USER identifier alterUserAction?
    | PROCEDURE qualifiedName alterRoutineAction
    | FUNCTION qualifiedName alterRoutineAction
    | TRIGGER qualifiedName alterTriggerAction
    | PACKAGE qualifiedName alterObjectCompileAction
    | TABLESPACE qualifiedName tablespaceAlterAction
    | PROFILE profileName profileAlterAction
    | TYPE qualifiedName alterObjectCompileAction
    | JAVA_LANGUAGE CLASS qualifiedName alterObjectCompileAction
    | CLASS qualifiedName alterObjectCompileAction
    | OPERATOR operatorQualifiedName alterObjectCompileAction
    ;

alterIndexAction
    : REBUILD NOSORT? ONLINE? alterIndexRebuildMode?
    | RENAME TO qualifiedName
    | INVISIBLE
    | VISIBLE
    | UNUSABLE
    | (MONITORING | NOMONITORING) USAGE
    ;

alterIndexRebuildMode
    : SHARE (ASYNCHRONOUS NUMBER?)?
    | EXCLUSIVE
    | PARALLEL NUMBER?
    | NOPARALLEL
    ;

alterContextIndexAction
    : REBUILD ONLINE? (LEXER expression)?
    | (INCREMENT | OPTIMIZE) ONLINE?
    ;

alterViewAction
    : COMPILE
    ;

alterObjectCompileAction
    : COMPILE CASCADE? DEBUG?
    ;

alterMaterializedViewAction
    : materializedViewAlterRefresh materializedViewQueryRewriteOption?
    | materializedViewQueryRewriteOption
    | COMPILE
    ;

materializedViewAlterRefresh
    : REFRESH materializedRefreshOption*
    | NEVER REFRESH
    ;

alterRoutineAction
    : COMPILE CASCADE? DEBUG?
    ;

alterTriggerAction
    : COMPILE DEBUG?
    | ENABLE
    | DISABLE
    ;

alterSequenceAction
    : sequenceAlterOption+
    | RENAME TO identifier
    ;

sequenceAlterOption
    : INCREMENT BY expression
    | MAXVALUE expression
    | NOMAXVALUE
    | NO MAXVALUE
    | MINVALUE expression
    | NOMINVALUE
    | NO MINVALUE
    | CYCLE
    | NOCYCLE
    | NO CYCLE
    | CACHE expression
    | NOCACHE
    | NO CACHE
    | ORDER
    | NOORDER
    | NO ORDER
    | CURRENT VALUE expression
    | GLOBAL
    | LOCAL
    ;

alterUserAction
    : (GRANT | REVOKE) CONNECT THROUGH identifier
    | alterUserClause+
    ;

alterUserClause
    : IDENTIFIED alterUserAuthMode
    | REPLACE userPassword
    | DISCARD OLD PASSWORD
    | ON SCHEMA identifier
    | userPropertyClause
    ;

profileAlterAction
    : resourceLimitClause
    ;

profileName
    : identifier
    | DEFAULT
    ;

tablespaceAlterAction
    : ONLINE
    | OFFLINE
    | CORRUPT
    | RENAME TO qualifiedName
    | RENAME DATAFILE tablespaceFilePath (COMMA tablespaceFilePath)* TO tablespaceFilePath (COMMA tablespaceFilePath)*
    | ADD tablespaceDatafileClause
    | RESIZE DATAFILE tablespaceFilePath TO tablespaceSizeValue (ON identifier)?
    | DATAFILE tablespaceFilePath (COMMA tablespaceFilePath)* tablespaceAutoextendClause?
    | cacheClause
    | OPTIMIZE expression
    | ADD HUGE PATH tablespaceFilePath
    | DROP DATAFILE tablespaceFilePath
    ;

alterTableAction
    : MODIFY LPAREN modifyColumnDefinitionList RPAREN
    | MODIFY partitionModifyAction
    | MODIFY CONSTRAINT identifier TO constraintBody validateOption? dropBehavior?
    | MODIFY CONSTRAINT identifier ENABLE validateOption?
    | MODIFY CONSTRAINT identifier DISABLE validateOption? dropBehavior?
    | MODIFY diskspaceClause
    | MODIFY {!isKeywordAhead("DISKSPACE")}? modifyColumnDefinition
    | MODIFY PATH expression
    | ADD COLUMN? ifNotExists? LPAREN tableElementList RPAREN
    | ADD partitionAddAction
    | ADD COLUMN? identifier (IDENTITY identityArgs? | AUTO_INCREMENT)
    | ADD COLUMN? identifier compressClause
    | ADD COLUMN? ifNotExists? columnDefinition hugeCompressClause?
    | ADD tableConstraint
    | SPLIT PARTITION identifier splitPartitionClause partitionAlterGlobalIndexClause?
    | MERGE PARTITIONS identifier COMMA identifier INTO PARTITION identifier partitionAlterDefinitionItem* partitionAlterGlobalIndexClause?
    | EXCHANGE (PARTITION | SUBPARTITION) identifier WITH TABLE qualifiedName partitionAlterGlobalIndexClause?
    | SET SUBPARTITION TEMPLATE subpartitionTemplateSpec
    | ALTER COLUMN? identifier SET STAT NONE
    | ALTER COLUMN? columnNameList SET STAT NONE?
    | ALTER COLUMN? identifier alterColumnAction
    | DROP CONSTRAINT identifier
      (RESTRICT | CASCADE ((DROP | KEEP) INDEX)? | (DROP | KEEP) INDEX)?
    | DROP PRIMARY KEY dropBehavior?
    | DROP IDENTITY
    | DROP AUTO_INCREMENT
    | DROP LOGIC LOG
    | DROP partitionDropAction partitionAlterGlobalIndexClause?
    | DROP COLUMN ifExists? dropColumnTarget dropBehavior?
    | DROP {!isKeywordAhead("LOGIC")}? ifExists? dropColumnTarget dropBehavior?
    | ENABLE CONSTRAINT identifier validateOption?
    | DISABLE CONSTRAINT identifier validateOption? dropBehavior?
    | rowMovementClause
    | ENABLE ALL TRIGGERS
    | DISABLE ALL TRIGGERS
    | REBUILD COLUMNS
    | RENAME TO qualifiedName
    | RENAME (PARTITION | SUBPARTITION) identifier TO identifier
    | RENAME CONSTRAINT identifier TO identifier
    | RENAME COLUMN identifier TO identifier
    | LOCK partitionLockMode
    | (WITH | WITHOUT) COUNTER
    | DEFAULT DIRECTORY identifier
    | LOCATION LPAREN STRING RPAREN
    | ENABLE USING LONG ROW
    | DISABLE USING LONG ROW
    | ADD LOGIC LOG
    | WITH ADVANCED LOG
    | WITHOUT ADVANCED LOG
    | TRUNCATE ADVANCED LOG
    | TRUNCATE (PARTITION | SUBPARTITION) alterPartitionTruncateTarget truncateStorageOption?
    | MOVE (PARTITION | SUBPARTITION) identifier tablespaceClause? storageClause? partitionAlterGlobalIndexClause?
    | MOVE TABLESPACE identifier
    | WITH DELTA
    | SET STAT hugeStatMode? hugeStatColumnClause?
    | REFRESH STAT
    | FORCE COLUMN STORAGE
    | REBUILD SECTION
    | parallelClause
    | READ (WRITE | ONLY)
    | tableAutoIncrementClause
    ;

partitionModifyAction
    : (PARTITION | SUBPARTITION) partitionSelector ADD SUBPARTITION ifNotExists? identifier partitionAlterDefinitionItem*
    | (PARTITION | SUBPARTITION) partitionSelector ADD VALUES partitionValuesClause
    | (PARTITION | SUBPARTITION) partitionSelector DROP VALUES partitionValuesClause
    ;

partitionSelector
    : identifier
    | FOR LPAREN expressionList RPAREN
    ;

partitionAddAction
    : PARTITION ifNotExists? identifier partitionAlterDefinitionItem* partitionAlterGlobalIndexClause?
    ;

partitionDropAction
    : (PARTITION | SUBPARTITION) partitionDropSelector
    ;

partitionDropSelector
    : ifExists? identifier
    | FOR LPAREN expressionList RPAREN
    ;

splitPartitionClause
    : AT_KEYWORD LPAREN expressionList RPAREN INTO LPAREN splitPartitionItem COMMA splitPartitionItem RPAREN
    | VALUES LPAREN expressionList RPAREN INTO LPAREN splitPartitionItem COMMA splitPartitionItem RPAREN
    | INTO LPAREN splitPartitionItem (COMMA splitPartitionItem)+ RPAREN
    ;

splitPartitionItem
    : PARTITION identifier partitionAlterDefinitionItem*
    ;

partitionAlterDefinitionItem
    : VALUES partitionValuesClause
    | LPAREN subpartitionDefinition (COMMA subpartitionDefinition)* RPAREN
    | tablespaceClause
    | storageClause
    ;

subpartitionTemplateSpec
    : LPAREN subpartitionDefinition (COMMA subpartitionDefinition)* RPAREN
    | SUBPARTITIONS expression storageClause?
    ;

partitionAlterGlobalIndexClause
    : (UPDATE | IGNORE) GLOBAL INDEXES
    ;

partitionLockMode
    : PARTITIONS
    | {isKeywordAhead("root")}? identifier
    ;

alterColumnAction
    : SET DEFAULT (ON NULL_LITERAL)? expression
    | DROP DEFAULT
    | RENAME TO identifier
    | SET NOT? NULL_LITERAL
    | SET NOT? VISIBLE
    | ADD USER LPAREN identifierList RPAREN
    | DROP USER LPAREN identifierList RPAREN
    ;

dropColumnTarget
    : identifier
    | LPAREN identifierList RPAREN
    ;

dropBehavior
    : RESTRICT
    | CASCADE
    ;

partitionTruncateTarget
    : identifier
    | LPAREN identifier RPAREN
    ;

alterPartitionTruncateTarget
    : partitionTruncateTarget
    | FOR LPAREN expressionList RPAREN
    ;

truncateStorageOption
    : (DROP | REUSE) STORAGE
    ;

hugeStatMode
    : NONE
    | SYNCHRONOUS
    | ASYNCHRONOUS
    ;

hugeStatColumnClause
    : (ON | EXCEPT) columnNameList
    ;

columnDefinitionList
    : columnDefinition (COMMA columnDefinition)*
    ;

modifyColumnDefinitionList
    : modifyColumnDefinition (COMMA modifyColumnDefinition)*
    ;

modifyColumnDefinition
    : columnDefinition
    | identifier columnAttribute+ columnTailClause*
    ;

dropStatement
    : DROP dropTarget
    ;

dropTarget
    : TABLE ifExists? qualifiedName (RESTRICT | CASCADE | PURGE)?
    | VIEW ifExists? qualifiedName (RESTRICT | CASCADE)?
    | MATERIALIZED VIEW LOG ON qualifiedName
    | MATERIALIZED VIEW ifExists? qualifiedName CASCADE?
    | CONTEXT INDEX ifExists? contextIndexName=qualifiedName ON contextTableName=qualifiedName
    | INDEX ifExists? qualifiedName (RESTRICT | CASCADE)?
    | SCHEMA ifExists? qualifiedName (RESTRICT | CASCADE)?
    | SEQUENCE ifExists? qualifiedName (RESTRICT | CASCADE)?
    | USER ifExists? identifier (RESTRICT | CASCADE)?
    | ROLE ifExists? identifier (RESTRICT | CASCADE)?
    | PROCEDURE ifExists? qualifiedName (RESTRICT | CASCADE)?
    | FUNCTION ifExists? qualifiedName (RESTRICT | CASCADE)?
    | TRIGGER ifExists? qualifiedName dropOption*
    | PUBLIC? SYNONYM ifExists? synonymName=qualifiedName dropOption*
    | PACKAGE BODY ifExists? qualifiedName (RESTRICT | CASCADE)?
    | PACKAGE {isTokenNotAhead(BODY)}? ifExists? qualifiedName (RESTRICT | CASCADE)?
    | TABLESPACE ifExists? qualifiedName
    | PUBLIC? LINK ifExists? qualifiedName (RESTRICT | CASCADE)?
    | DIRECTORY ifExists? qualifiedName (RESTRICT | CASCADE)?
    | CONTEXT ifExists? qualifiedName
    | DOMAIN ifExists? qualifiedName (RESTRICT | CASCADE)?
    | OPERATOR ifExists? operatorQualifiedName LPAREN operatorArgumentType COMMA operatorArgumentType RPAREN
    | PROFILE ifExists? identifier (RESTRICT | CASCADE)?
    | LIBRARY ifExists? qualifiedName dropOption*
    | TYPE BODY? ifExists? qualifiedName (RESTRICT | CASCADE)?
    | CLASS BODY? ifExists? qualifiedName (RESTRICT | CASCADE)?
    | PARTITION GROUP ifExists? qualifiedName FORCE?
    ;

dropOption
    : CASCADE
    | RESTRICT
    | identifier
    ;

truncateStatement
    : TRUNCATE TABLE qualifiedName truncatePartitionClause? truncateStorageOption? CASCADE? partitionAlterGlobalIndexClause?
    ;

truncatePartitionClause
    : PARTITION partitionTruncateTarget
    ;

commentStatement
    : COMMENT ON commentTarget IS STRING
    ;

commentTarget
    : TABLE qualifiedName
    | VIEW qualifiedName
    | COLUMN qualifiedName
    | MATERIALIZED VIEW qualifiedName
    | SCHEMA qualifiedName
    | TABLESPACE qualifiedName
    | ROLE qualifiedName
    | SEQUENCE qualifiedName
    | INDEX qualifiedName
    | TRIGGER qualifiedName
    | TYPE qualifiedName
    | CONTEXT qualifiedName
    | DOMAIN qualifiedName
    | DIRECTORY qualifiedName
    | PROFILE qualifiedName
    | LINK qualifiedName
    | CLASS qualifiedName
    | FUNCTION qualifiedName
    | PACKAGE qualifiedName
    | PROCEDURE qualifiedName
    | DATABASE
    ;

grantStatement
    : GRANT (grantPrivilegeStatement | grantRoleStatement)
    ;

revokeStatement
    : REVOKE (revokePrivilegeStatement | revokeRoleStatement)
    ;

grantPrivilegeStatement
    : privilegeList privilegeObjectClause? TO granteeList (WITH (GRANT | ADMIN) OPTION)?
    ;

grantRoleStatement
    : granteeList TO granteeList (WITH ADMIN OPTION)?
    ;

revokePrivilegeStatement
    : revokeOption? privilegeList privilegeObjectClause? FROM granteeList revokeRestrictCascade?
    ;

revokeRoleStatement
    : revokeOption? granteeList FROM granteeList revokeRestrictCascade?
    ;

revokeOption
    : GRANT OPTION FOR
    | ADMIN OPTION FOR
    | GRANT ADMIN FOR
    ;

revokeRestrictCascade
    : RESTRICT
    | CASCADE
    ;

privilegeList
    : ALL PRIVILEGES?
    | privilegeItem (COMMA privilegeItem)*
    ;

privilegeItem
    : privilegeAction columnNameList? (ANY privilegeObjectType)?
    | privilegeAction ANY privilegeObjectType
    | privilegeAction privilegeObjectType
    | SELECT FOR DUMP
    | identifier
    ;

privilegeAction
    : SELECT
    | INSERT
    | UPDATE
    | DELETE
    | REFERENCES
    | EXECUTE
    | CREATE
    | ALTER
    | DROP
    | COMMENT
    | GRANT
    | READ
    | WRITE
    | USAGE
    | INDEX
    | DUMP
    | FLASHBACK
    ;

privilegeObjectClause
    : OFF? ON privilegeObject
    ;

privilegeObject
    : SCHEMA identifier
    | privilegeObjectType? qualifiedName
    ;

privilegeObjectType
    : SCHEMA
    | DATABASE
    | USER
    | ROLE
    | TABLESPACE
    | TABLE
    | VIEW
    | MATERIALIZED VIEW
    | INDEX
    | CONTEXT INDEX
    | SEQUENCE
    | PROCEDURE
    | FUNCTION
    | PACKAGE
    | CLASS
    | TYPE
    | TRIGGER
    | PUBLIC SYNONYM
    | SYNONYM
    | DOMAIN
    | DIRECTORY
    | CONTEXT
    | PROFILE
    | LINK
    | LIBRARY
    ;

granteeList
    : grantee (COMMA grantee)*
    ;

grantee
    : PUBLIC
    | identifier
    ;

callStatement
    : CALL qualifiedName (LPAREN routineArgumentList? RPAREN)?
    | EXEC qualifiedName routineArgumentList?
    | EXECUTE {isTokenNotAhead(IMMEDIATE)}? qualifiedName routineArgumentList?
    ;

lockTableStatement
    : LOCK TABLE qualifiedName IN lockMode MODE NOWAIT?
    ;

lockMode
    : INTENT (SHARE | EXCLUSIVE)
    | ROW (SHARE | EXCLUSIVE)
    | SHARE INTENT EXCLUSIVE
    | SHARE ROW EXCLUSIVE
    | SHARE UPDATE
    | SHARE
    | EXCLUSIVE
    ;

alterSessionParallelDmlStatement
    : ALTER SESSION (ENABLE | DISABLE) PARALLEL (DML | DDL | QUERY)
    ;

setSchemaStatement
    : SET SCHEMA qualifiedName
    ;

setTimeZoneStatement
    : SET TIME ZONE timeZoneValue
    ;

timeZoneValue
    : LOCAL
    | STRING
    | INTERVAL STRING intervalQualifier?
    ;

setIdentityInsertStatement
    : SET IDENTITY_INSERT qualifiedName (ON (WITH REPLACE NULL_LITERAL)? | OFF)
    ;

configWriteStatement
    : ALTER SYSTEM SET configAssignment systemConfigDeferredClause? systemConfigScope?
    | ALTER SESSION SET sessionConfigAssignment sessionConfigScope?
    | CALL? configWriteProcedure LPAREN expressionList? RPAREN
    ;

configAssignment
    : configKey EQ expression
    ;

sessionConfigAssignment
    : {isKeywordAhead("CASE_SENSITIVE")}? configKey EQ DEFAULT
    | {isKeywordAhead("NLS_SORT")}? configKey EQ BINARY
    | {isTokenAhead(STRING)}? configKey EQ expression
    | {
        isKeywordAhead("ALTER_TABLE_OPT")
        || isKeywordAhead("CASE_SENSITIVE")
        || isKeywordAhead("NLS_DATE_FORMAT")
        || isKeywordAhead("NLS_DATE_LANGUAGE")
        || isKeywordAhead("NLS_SORT")
        || isKeywordAhead("NLS_TIMESTAMP_FORMAT")
        || isKeywordAhead("NLS_TIMESTAMP_TZ_FORMAT")
        || isKeywordAhead("NLS_TIME_FORMAT")
        || isKeywordAhead("NLS_TIME_TZ_FORMAT")
        || isKeywordAhead("QUERY_REWRITE_INTEGRITY")
      }? configKey EQ expression
    ;

configKey
    : STRING
    | qualifiedName
    ;

systemConfigDeferredClause
    : DEFERRED
    ;

systemConfigScope
    : MEMORY
    | BOTH
    | SPFILE
    ;

sessionConfigScope
    : PURGE
    ;

configWriteProcedure
    : SP_SET_PARA_VALUE
    | SP_SET_PARA_DOUBLE_VALUE
    | SP_SET_PARA_STRING_VALUE
    | SF_SET_SESSION_PARA_VALUE
    | SP_RESET_SESSION_PARA_VALUE
    | SP_SET_PARAM_IN_SESSION
    | SF_SET_SYSTEM_PARA_VALUE
    | SP_SET_INI_PARA_VALUE
    | SP_SET_SESSION_READONLY
    ;

auditAdminStatement
    : auditAdminProcedure LPAREN expressionList? RPAREN
    ;

auditAdminProcedure
    : SP_SET_ENABLE_AUDIT
    | SP_AUDIT_STMT
    | SP_NOAUDIT_STMT
    | SP_AUDIT_OBJECT
    | SP_NOAUDIT_OBJECT
    | SP_AUDIT_SQLSEQ_START
    | SP_AUDIT_SQLSEQ_ADD
    | SP_AUDIT_SQLSEQ_END
    | SP_AUDIT_SQLSEQ_DEL
    | SP_AUDIT_SET_ENC
    | SP_DROP_AUDIT_FILE
    | SP_SWITCH_AUDIT_FILE
    | SP_CREATE_AUDIT_RULE
    | SP_DROP_AUDIT_RULE
    ;

securityAdminStatement
    : securityAdminProcedure LPAREN expressionList? RPAREN
    ;

securityAdminProcedure
    : SP_SET_ROLE
    | SP_INIT_SVI_SYS
    | SP_SWITCH_SVI
    | SP_RESTRICT_DBA
    ;

procedureCallStatement
    : qualifiedName LPAREN routineArgumentList? RPAREN
    | bareRoutineName
    ;

transactionStatement
    : START TRANSACTION transactionModeList?
    | COMMIT WORK? commitWriteOption?
    | ROLLBACK WORK? TO SAVEPOINT? identifier
    | ROLLBACK WORK?
    | SAVEPOINT identifier
    | RELEASE SAVEPOINT identifier
    | SET TRANSACTION transactionModeList
    ;

commitWriteOption
    : IMMEDIATE (WAIT | NOWAIT)?
    | BATCH (WAIT | NOWAIT)
    | WAIT
    | NOWAIT
    ;

transactionModeList
    : transactionMode (COMMA transactionMode)*
    ;

transactionMode
    : ISOLATION LEVEL isolationLevel
    | READ (ONLY | WRITE)
    ;

isolationLevel
    : READ (COMMITTED | UNCOMMITTED)
    | SERIALIZABLE
    ;

explainStatement
    : EXPLAIN (explainAsClause? FOR explainForTargetStatement | explainDirectTargetStatement)
    ;

explainAsClause
    : AS identifier
    ;

explainForTargetStatement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | mergeStatement
    ;

explainDirectTargetStatement
    : explainForTargetStatement
    | createStatement
    | dropStatement
    | callStatement
    ;

sqlBlockStatement
    : (DECLARE blockDeclaration*)? BEGIN blockItem* exceptionSection? END qualifiedName?
    ;

blockItem
    : statementLabel SEMI*
    | cStyleBlockStatement SEMI*
    | ifStatement SEMI*
    | loopStatement SEMI*
    | repeatStatement SEMI*
    | caseControlStatement SEMI*
    | gotoStatement SEMI*
    | exitStatement SEMI*
    | continueStatement SEMI*
    | nullStatement SEMI*
    | assignmentStatement SEMI*
    | executeImmediateStatement SEMI*
    | openCursorStatement SEMI*
    | fetchCursorStatement SEMI*
    | closeCursorStatement SEMI*
    | forallStatement SEMI*
    | raiseStatement SEMI*
    | returnStatement SEMI*
    | printStatement SEMI*
    | pipeRowStatement SEMI*
    | sqlBlockStatement SEMI*
    | blockSqlStatement SEMI*
    ;

statementLabel
    : SHIFT_LEFT identifier SHIFT_RIGHT
    ;

blockSqlStatement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | mergeStatement
    | flashbackStatement
    | createStatement
    | adminStatement
    | statStatement
    | statProcedureStatement
    | configWriteStatement
    | auditAdminStatement
    | securityAdminStatement
    | alterStatement
    | dropStatement
    | truncateStatement
    | commentStatement
    | grantStatement
    | revokeStatement
    | callStatement
    | procedureCallStatement
    | lockTableStatement
    | alterSessionParallelDmlStatement
    | setSchemaStatement
    | setTimeZoneStatement
    | setIdentityInsertStatement
    | transactionStatement
    | explainStatement
    ;

blockDeclaration
    : packageVariableDeclaration SEMI+
    | packageCursorDeclaration SEMI+
    | packageExceptionDeclaration SEMI+
    | packageSubtypeDeclaration SEMI+
    | blockTypeDeclaration SEMI+
    | blockPragmaDeclaration SEMI+
    | packageProcedureDeclaration SEMI+
    | packageFunctionDeclaration SEMI+
    | packageProcedureImplementation SEMI*
    | packageFunctionImplementation SEMI*
    ;

cStyleBlockStatement
    : LBRACE cBlockItem* RBRACE
    ;

cBlockItem
    : cStatement
    ;

cStatement
    : cStyleBlockStatement SEMI*
    | cIfStatement SEMI*
    | cSwitchStatement SEMI*
    | cWhileStatement SEMI*
    | cForStatement SEMI*
    | cTryCatchStatement SEMI*
    | cSimpleStatement SEMI+
    ;

cSimpleStatement
    : cCursorDeclaration
    | cVariableDeclaration
    | cThrowStatement
    | cIncrementStatement
    | cAssignmentStatement
    | returnStatement
    | printStatement
    | openCursorStatement
    | fetchCursorStatement
    | closeCursorStatement
    | cBreakStatement
    | procedureCallStatement
    | blockSqlStatement
    ;

cVariableDeclaration
    : dataType cVariableDeclarator (COMMA cVariableDeclarator)*
    ;

cVariableDeclarator
    : identifier routineParameterDefault?
    ;

cCursorDeclaration
    : CURSOR identifier (IS | FOR) selectStatement
    ;

cIfStatement
    : IF LPAREN expression RPAREN cStatement (ELSE cStatement)?
    ;

cSwitchStatement
    : SWITCH LPAREN expression RPAREN LBRACE cSwitchCaseClause* cSwitchDefaultClause? RBRACE
    ;

cSwitchCaseClause
    : CASE expression COLON cStatement* cBreakStatement? SEMI*
    ;

cSwitchDefaultClause
    : DEFAULT COLON cStatement* cBreakStatement? SEMI*
    ;

cBreakStatement
    : BREAK
    ;

cWhileStatement
    : WHILE LPAREN expression RPAREN cStatement
    ;

cForStatement
    : FOR LPAREN cForInit? SEMI expression? SEMI cForUpdate? RPAREN cStatement
    ;

cForInit
    : cVariableDeclaration
    | cAssignmentStatement
    ;

cForUpdate
    : cIncrementStatement
    | cAssignmentStatement
    | expression
    ;

cIncrementStatement
    : assignmentTarget PLUS_PLUS
    | PLUS_PLUS assignmentTarget
    ;

cAssignmentStatement
    : assignmentTarget (ASSIGN | EQ) expression
    ;

cThrowStatement
    : THROW NEW EXCEPTION LPAREN expressionList? RPAREN
    | THROW qualifiedName?
    ;

cTryCatchStatement
    : TRY cStyleBlockStatement CATCH LPAREN EXCEPTION identifier? RPAREN cStyleBlockStatement
    ;

blockTypeDeclaration
    : TYPE identifier dmsqlTypeDefinition
    ;

blockPragmaDeclaration
    : PRAGMA qualifiedName (LPAREN expressionList? RPAREN)?
    ;

declarationDataType
    : dataType
    | qualifiedName PERCENT (TYPE | ROWTYPE) dataTypeArrayBound*
    ;

exceptionSection
    : EXCEPTION exceptionHandler*
    ;

exceptionHandler
    : WHEN exceptionCondition THEN blockItem*
    ;

exceptionCondition
    : qualifiedName (OR qualifiedName)*
    ;

ifStatement
    : IF controlHeader THEN blockItem* elsifClause* elseClause? END IF
    | IF controlHeader sqlBlockStatement elsifBlockClause* elseBlockClause?
    ;

elsifClause
    : (ELSIF | ELSEIF) controlHeader THEN blockItem*
    ;

elseClause
    : ELSE blockItem*
    ;

elsifBlockClause
    : (ELSIF | ELSEIF) controlHeader sqlBlockStatement
    ;

elseBlockClause
    : ELSE sqlBlockStatement
    ;

loopStatement
    : LOOP blockItem* END LOOP identifier?
    | WHILE controlHeader LOOP blockItem* END LOOP identifier?
    | FOR loopHeader LOOP blockItem* END LOOP identifier?
    ;

repeatStatement
    : REPEAT blockItem* UNTIL controlHeader
    ;

caseControlStatement
    : CASE caseHeader? caseBranch+ elseClause? END CASE?
    ;

caseHeader
    : expression
    ;

caseBranch
    : WHEN expression THEN blockItem*
    ;

gotoStatement
    : GOTO identifier
    ;

exitStatement
    : EXIT identifier? (WHEN controlHeader)?
    ;

continueStatement
    : CONTINUE identifier? (WHEN controlHeader)?
    ;

nullStatement
    : NULL_LITERAL
    ;

assignmentStatement
    : SET? assignmentTarget (ASSIGN | EQ) expression
    ;

assignmentTarget
    : qualifiedName assignmentTargetSuffix*
    | triggerPseudoRecordTarget
    ;

triggerPseudoRecordTarget
    : BIND_VARIABLE (DOT identifier)+ assignmentTargetSuffix*
    ;

assignmentTargetSuffix
    : BRACKET_QUOTED_ID
    | LPAREN expressionList? RPAREN
    | DOT identifier
    ;

executeImmediateStatement
    : EXECUTE IMMEDIATE {isStatementContentAhead()}? expression
      dynamicIntoClause? dynamicUsingClause? dynamicReturningClause?
    ;

dynamicIntoClause
    : (BULK COLLECT)? INTO returnTargetList
    ;

dynamicUsingClause
    : USING dynamicUsingArgument (COMMA dynamicUsingArgument)*
    ;

dynamicUsingArgument
    : (IN | OUT | IN OUT)? expression
    ;

dynamicReturningClause
    : (RETURN | RETURNING) (BULK COLLECT)? INTO returnTargetList
    ;

openCursorStatement
    : OPEN openCursorFastClause? CURSOR? qualifiedName FOR (selectStatement | expression) dynamicUsingClause?
    | OPEN CURSOR? qualifiedName (LPAREN expressionList? RPAREN)?
    ;

openCursorFastClause
    : WITH FAST
    ;

fetchCursorStatement
    : FETCH fetchCursorDirection? CURSOR? qualifiedName fetchIntoClause?
    ;

fetchIntoClause
    : (BULK COLLECT)? INTO returnTargetList limitClause?
    ;

fetchCursorDirection
    : fetchCursorPosition FROM?
    ;

fetchCursorPosition
    : FIRST
    | NEXT
    | PRIOR
    | LAST
    | (ABSOLUTE | RELATIVE) expression
    ;

closeCursorStatement
    : CLOSE CURSOR? qualifiedName
    ;

forallStatement
    : FORALL identifier IN forallBounds (SAVE EXCEPTIONS)? blockSqlStatement
    ;

forallBounds
    : expression DOT DOT expression
    | INDICES OF qualifiedName (BETWEEN expression AND expression)?
    | VALUES OF qualifiedName
    | expressionList
    ;

raiseStatement
    : RAISE qualifiedName?
    ;

returnStatement
    : RETURN expression?
    ;

printStatement
    : PRINT expression
    ;

pipeRowStatement
    : PIPE ROW LPAREN expression RPAREN
    ;

controlHeader
    : expression
    ;

loopHeader
    : numericLoopHeader
    | cursorLoopHeader
    ;

numericLoopHeader
    : identifier IN REVERSE? expression DOT DOT expression
    ;

cursorLoopHeader
    : identifier IN cursorLoopSource
    ;

cursorLoopSource
    : qualifiedName (LPAREN expressionList? RPAREN)?
    | LPAREN selectStatement RPAREN
    ;

ifNotExists
    : IF NOT EXISTS
    ;

ifExists
    : IF EXISTS
    ;

columnNameList
    : LPAREN identifierList RPAREN
    ;

identifierList
    : identifier (COMMA identifier)*
    ;

qualifiedNameList
    : qualifiedName (COMMA qualifiedName)*
    ;

expressionList
    : expression (COMMA expression)*
    ;

expression
    : booleanExpression
    ;

booleanExpression
    : predicate ((AND | AND_OP | OR) predicate)*
    ;

predicate
    : NOT? EXISTS LPAREN selectStatement RPAREN
    | NOT? containsPredicate
    | rowValueExpression (NOT? MEMBER OF collectionExpression)?
      (OVERLAPS rowValueExpression | comparisonOperator (quantifiedSubquery | concatenation))?
      (IS NOT? NULL_LITERAL | jsonPredicateClause | isOfTypePredicateClause)?
      (NOT? LIKE concatenation (ESCAPE concatenation)?)?
      (NOT? BETWEEN concatenation AND concatenation)?
      (NOT? IN inPredicateValue)?
    ;

containsPredicate
    : CONTAINS LPAREN qualifiedName COMMA containsArguments RPAREN
    ;

containsArguments
    : containsSearchCondition (COMMA expression)*
    | expression (COMMA expression)*
    ;

containsSearchCondition
    : containsSearchTerm ((AND NOT? | OR) containsSearchTerm)*
    ;

containsSearchTerm
    : STRING
    ;

jsonPredicateClause
    : IS NOT? JSON jsonFormatClause? jsonUniqueKeysClause?
    ;

isOfTypePredicateClause
    : IS NOT? OF TYPE? LPAREN dataType (COMMA dataType)* RPAREN
    ;

jsonFormatClause
    : STRICT
    | LAX
    | LPAREN (STRICT | LAX) RPAREN
    ;

jsonUniqueKeysClause
    : (WITH | WITHOUT) UNIQUE KEYS
    ;

rowValueExpression
    : collectionExpression
    | LPAREN expressionList RPAREN
    ;

collectionExpression
    : userDefinedOperatorExpression (MULTISET multisetOperator multisetQualifier? userDefinedOperatorExpression)*
    ;

multisetOperator
    : UNION
    | INTERSECT
    | EXCEPT
    ;

multisetQualifier
    : ALL
    | DISTINCT
    | UNIQUE
    ;

userDefinedOperatorExpression
    : userDefinedOperator? concatenation (userDefinedOperator concatenation)* userDefinedOperator?
    ;

userDefinedOperator
    : operatorFunctionClause
    | symbolicOperatorName
    ;

operatorFunctionClause
    : OPERATOR LPAREN operatorQualifiedName RPAREN
    ;

operatorQualifiedName
    : identifier DOT operatorName
    | operatorName
    ;

operatorName
    : symbolicOperatorName
    | DOUBLE_QUOTED_ID
    ;

symbolicOperatorName
    : operatorNameStart operatorNamePart* operatorNameEnd
    ;

operatorNameStart
    : PLUS
    | MINUS
    | STAR
    | SLASH
    | LT
    | GT
    | EQ
    | BIT_NOT
    | AT
    | PERCENT
    | BIT_XOR
    | BIT_AND
    | BIT_OR
    | BACKTICK
    ;

operatorNameEnd
    : STAR
    | SLASH
    | LT
    | GT
    | EQ
    | NOT_OP
    | PERCENT
    | BIT_XOR
    | BIT_AND
    | BIT_OR
    | BACKTICK
    ;

operatorNamePart
    : PLUS
    | MINUS
    | STAR
    | SLASH
    | LT
    | GT
    | EQ
    | BIT_NOT
    | NOT_OP
    | AT
    | PERCENT
    | BIT_XOR
    | BIT_AND
    | BIT_OR
    | BACKTICK
    ;

comparisonOperator
    : EQ
    | NEQ
    | LT
    | LTE
    | GT
    | GTE
    | JSON_CONTAINS
    ;

quantifiedSubquery
    : (ALL | ANY | SOME) LPAREN (selectStatement | expression) RPAREN
    ;

inPredicateValue
    : LPAREN selectStatement RPAREN
    | LPAREN inPredicateElementList? RPAREN
    ;

inPredicateElementList
    : inPredicateElement (COMMA inPredicateElement)*
    ;

inPredicateElement
    : expression
    | LPAREN expressionList? RPAREN
    ;

concatenation
    : bitwiseOr (CONCAT bitwiseOr)*
    ;

bitwiseOr
    : bitwiseXor (BIT_OR bitwiseXor)*
    ;

bitwiseXor
    : bitwiseAnd (BIT_XOR bitwiseAnd)*
    ;

bitwiseAnd
    : shiftExpression (BIT_AND shiftExpression)*
    ;

shiftExpression
    : additive ((SHIFT_LEFT | SHIFT_RIGHT) additive)*
    ;

additive
    : multiplicative ((PLUS | MINUS) multiplicative)*
    ;

multiplicative
    : unary ((STAR | SLASH | PERCENT) unary)*
    ;

unary
    : (PRIOR | CONNECT_BY_ROOT | BINARY) unary
    | (PLUS | MINUS | NOT | NOT_OP | BIT_NOT)? postfixExpression
    ;

postfixExpression
    : primaryExpression postfixOperator*
    ;

postfixOperator
    : DOUBLE_COLON dataType
    | (JSON_ARROW | JSON_TEXT_ARROW) unary
    | cursorAttributeSuffix
    | atTimeZoneClause
    | LPAREN PLUS RPAREN
    | DOT methodName=methodIdentifier (LPAREN functionArguments[$methodName.text]? RPAREN)?
    | BRACKET_QUOTED_ID
    ;

methodIdentifier
    : identifier
    | PRIOR
    | CONNECT_BY_ROOT
    ;

cursorAttributeSuffix
    : PERCENT cursorAttribute
    | PERCENT bulkCursorAttribute LPAREN expression RPAREN
    ;

cursorAttribute
    : FOUND
    | NOTFOUND
    | ISOPEN
    | ROWCOUNT
    ;

bulkCursorAttribute
    : BULK_ROWCOUNT
    | BULK_EXCEPTIONS
    ;

atTimeZoneClause
    : AT_KEYWORD (LOCAL | TIME ZONE unary)
    ;

primaryExpression
    : literalValue
    | bindValue
    | ROWNUM
    | newArrayExpression
    | cursorExpression
    | multisetSubqueryExpression
    | specialFunctionCall
    | functionCall
    | CURRENT_USER
    | {isTokenNotAhead(BINARY)}? qualifiedName
    | CASE expression? caseWhen+ caseElse? END
    | LPAREN selectStatement RPAREN
    | LPAREN expression RPAREN intervalQualifier
    | LPAREN expression RPAREN
    ;

multisetSubqueryExpression
    : MULTISET LPAREN selectStatement RPAREN
    ;

caseWhen
    : WHEN expression THEN expression
    ;

caseElse
    : ELSE expression
    ;

functionCall
    : {isTokenNotAhead(XMLPARSE, BINARY)}?
      name=functionName LPAREN functionArguments[$name.text]? RPAREN
      ({isKeepFunction($name.text)}? keepClause)?
      ({isWithinGroupFunction($name.text)}? withinGroupClause)?
      fromFirstLastClause?
      ({isNullTreatmentFunction($name.text)}? nullTreatment)?
      overClause?
    ;

functionName
    : qualifiedName
    | keywordFunctionName
    ;

keywordFunctionName
    : INSERT
    | LEFT
    | REPLACE
    | RIGHT
    | TRUNCATE
    ;

newArrayExpression
    : NEW dataType LPAREN expressionList? RPAREN
    | NEW arrayType=dataType {isValidArrayAllocation($arrayType.text)}? arrayInitializer?
    ;

cursorExpression
    : CURSOR FAST? LPAREN selectStatement RPAREN
    ;

arrayInitializer
    : LBRACE expressionList? RBRACE
    ;

specialFunctionCall
    : CAST LPAREN expression AS dataType RPAREN
    | TREAT LPAREN expression AS dataType RPAREN
    | convertFunction
    | IF LPAREN expression COMMA expression COMMA expression RPAREN
    | SET LPAREN expression RPAREN
    | EXTRACT LPAREN extractField FROM expression RPAREN
    | EXTRACT LPAREN expression COMMA expression (COMMA expression)? RPAREN
    | TRIM LPAREN trimArguments RPAREN
    | SUBSTRING LPAREN substringArguments RPAREN
    | POSITION LPAREN expression IN expression RPAREN
    | POSITION LPAREN expression COMMA expression RPAREN
    | TRANSLATE LPAREN expression USING translateUsingCharset RPAREN
    | OVERLAY LPAREN expression PLACING expression FROM expression (FOR expression)? RPAREN
    | xmlElementFunction
    | xmlForestFunction
    | xmlCastFunction
    | xmlParseFunction
    | xmlQueryFunction
    | xmlSerializeFunction
    | jsonValueFunction
    | jsonQueryFunction
    | jsonArrayFunction
    | jsonObjectFunction
    ;

convertFunction
    : {isKeywordAhead("convert")}? identifier LPAREN dataType COMMA expression (COMMA expression)? RPAREN
    ;

xmlElementFunction
    : XMLELEMENT LPAREN xmlElementName (COMMA xmlElementArgument)* RPAREN
    ;

xmlElementName
    : EVALNAME expression
    | NAME identifier
    | identifier
    ;

xmlElementArgument
    : xmlAttributesFunction
    | expression
    ;

xmlAttributesFunction
    : XMLATTRIBUTES LPAREN xmlAttribute (COMMA xmlAttribute)* RPAREN
    ;

xmlAttribute
    : expression (AS? xmlAlias)?
    ;

xmlAlias
    : EVALNAME expression
    | identifier
    ;

xmlForestFunction
    : XMLFOREST LPAREN xmlForestItem (COMMA xmlForestItem)* RPAREN
    ;

xmlForestItem
    : expression (AS identifier)?
    ;

xmlCastFunction
    : XMLCAST LPAREN expression AS dataType RPAREN
    ;

xmlParseFunction
    : XMLPARSE LPAREN xmlDocumentKind expression WELLFORMED? RPAREN
    ;

xmlQueryFunction
    : XMLQUERY LPAREN expression xmlPassingClause? (RETURNING CONTENT)? RPAREN
    ;

xmlSerializeFunction
    : XMLSERIALIZE LPAREN xmlDocumentKind expression xmlSerializeDataTypeClause? xmlSerializeEncodingClause? xmlSerializeVersionClause? xmlSerializeIndentClause? xmlSerializeDefaultsClause? RPAREN
    ;

xmlSerializeDataTypeClause
    : AS dataType
    ;

xmlSerializeEncodingClause
    : ENCODING expression
    ;

xmlSerializeVersionClause
    : VERSION expression
    ;

xmlSerializeIndentClause
    : NO INDENT
    | INDENT (SIZE EQ expression)?
    ;

xmlSerializeDefaultsClause
    : (HIDE | SHOW) DEFAULTS
    ;

xmlDocumentKind
    : DOCUMENT
    | CONTENT
    ;

jsonValueFunction
    : JSON_VALUE LPAREN expression COMMA expression jsonReturningClause? ASCII? jsonValueEmptyClause? jsonValueErrorClause? RPAREN
    ;

jsonQueryFunction
    : JSON_QUERY LPAREN expression COMMA expression jsonReturningClause? PRETTY? ASCII? jsonWrapperClause? jsonQueryFunctionErrorClause? RPAREN
    ;

jsonArrayFunction
    : JSON_ARRAY LPAREN jsonConstructorItemList? jsonReturningClause? RPAREN
    ;

jsonObjectFunction
    : JSON_OBJECT LPAREN jsonConstructorItemList? jsonReturningClause? RPAREN
    ;

jsonConstructorItemList
    : jsonConstructorItem (COMMA jsonConstructorItem)*
    ;

jsonConstructorItem
    : expression (FORMAT JSON)?
    ;

jsonReturningClause
    : RETURNING dataType
    ;

jsonWrapperClause
    : WITH (CONDITIONAL | UNCONDITIONAL)? ARRAY? WRAPPER
    | WITHOUT ARRAY? WRAPPER
    ;

jsonValueEmptyClause
    : jsonValueResultClause ON EMPTY
    ;

jsonValueErrorClause
    : jsonValueResultClause ON ERROR
    ;

jsonValueResultClause
    : NULL_LITERAL
    | ERROR
    | DEFAULT literalValue
    ;

jsonExistsEmptyClause
    : (ERROR | TRUE | FALSE) ON EMPTY
    ;

jsonExistsErrorClause
    : (ERROR | TRUE | FALSE) ON ERROR
    ;

jsonQueryFunctionErrorClause
    : jsonQueryFunctionResultClause ON ERROR
    ;

jsonQueryFunctionResultClause
    : NULL_LITERAL
    | ERROR
    | EMPTY
    ;

jsonQueryEmptyClause
    : jsonQueryResultClause ON EMPTY
    ;

jsonQueryErrorClause
    : jsonQueryResultClause ON ERROR
    ;

jsonQueryResultClause
    : NULL_LITERAL
    | ERROR
    | EMPTY ARRAY?
    | EMPTY OBJECT
    ;

extractField
    : YEAR
    | MONTH
    | DAY
    | HOUR
    | MINUTE
    | SECOND
    | TIMEZONE_HOUR
    | TIMEZONE_MINUTE
    | WEEK
    | identifier
    ;

trimArguments
    : ((LEADING | TRAILING | BOTH) expression? | expression)? FROM expression
    | expression
    ;

substringArguments
    : expression FROM expression (FOR expression)?
    | expression COMMA expression (COMMA expression)?
    ;

translateUsingCharset
    : identifier
    ;

functionArguments[String fn]
    : STAR
    | functionArgumentQualifier? functionArgument[$fn] (COMMA functionArgument[$fn])*
      listaggOverflowClause?
      functionArgumentOrderByClause?
    ;

functionArgumentQualifier
    : ALL
    | DISTINCT
    | UNIQUE
    ;

functionArgumentOrderByClause
    : orderByClause
    ;

listaggOverflowClause
    : ON OVERFLOW (ERROR | TRUNCATE STRING? listaggOverflowCountClause?)
    ;

listaggOverflowCountClause
    : (WITH | WITHOUT) COUNT
    ;

functionArgument[String fn]
    : namedArgument
    | expression ({isNullTreatmentFunction($fn)}? nullTreatment)?
    ;

routineArgumentList
    : routineArgument (COMMA routineArgument)*
    ;

routineArgument
    : namedArgument
    | expression
    ;

namedArgument
    : dottedNamePart NAMED_ARGUMENT_ARROW expression
    ;

keepClause
    : KEEP LPAREN DENSE_RANK (FIRST | LAST) orderByClause RPAREN
    ;

withinGroupClause
    : WITHIN GROUP LPAREN orderByClause RPAREN
    ;

fromFirstLastClause
    : FROM (FIRST | LAST)
    ;

nullTreatment
    : (RESPECT | IGNORE) NULLS
    ;

overClause
    : OVER (identifier | LPAREN analyticClause? RPAREN)
    ;

analyticClause
    : partitionByClause orderByClause? windowClause?
    | identifier orderByClause windowClause?
    | orderByClause windowClause?
    | windowClause
    ;

partitionByClause
    : PARTITION BY expressionList
    ;

windowClause
    : (ROWS | RANGE) (BETWEEN windowBound AND windowBound | windowBound)
    ;

windowDefinitionClause
    : WINDOW windowDefinition (COMMA windowDefinition)*
    ;

windowDefinition
    : identifier AS LPAREN windowSpecification? RPAREN
    ;

windowSpecification
    : identifier partitionByClause? orderByClause? windowClause?
    | partitionByClause orderByClause? windowClause?
    | orderByClause windowClause?
    | windowClause
    ;

windowBound
    : UNBOUNDED (PRECEDING | FOLLOWING)
    | CURRENT ROW
    | expression (PRECEDING | FOLLOWING)
    ;

literalValue
    : NUMBER
    | HEX_LITERAL
    | HEX_STRING_LITERAL
    | STRING
    | TRUE
    | FALSE
    | NULL_LITERAL
    | datetimeLiteral
    | intervalLiteral
    | jdbcEscapeExpression
    ;

datetimeLiteral
    : (DATE | TIME | DATETIME | TIMESTAMP) STRING
    ;

intervalLiteral
    : INTERVAL STRING intervalQualifier?
    ;

jdbcEscapeExpression
    : LBRACE jdbcEscapeTag expression RBRACE
    ;

jdbcEscapeTag
    : {isKeywordAhead("fn")}? identifier
    | {isKeywordAhead("d")}? identifier
    | {isKeywordAhead("t")}? identifier
    | {isKeywordAhead("ts")}? identifier
    ;

intervalQualifier
    : intervalTypeField (TO intervalTypeField)?
    ;

bindValue
    : QUESTION
    | BIND_VARIABLE
    ;

qualifiedName
    : dottedName (AT linkName)?
    ;

dottedName
    : identifier (DOT dottedNamePart)*
    ;

bareRoutineName
    : regularIdentifier (DOT dottedNamePart)* (AT linkName)?
    ;

linkName
    : identifier (DOT dottedNamePart)?
    ;

regularIdentifier
    : ID
    | LOCAL_TEMP_ID
    | DOUBLE_QUOTED_ID
    ;

dottedNamePart
    : identifier
    | dottedNameKeyword
    ;

dottedNameKeyword
    : ADD
    | AGGREGATE
    | ALL
    | ALTER
    | AND
    | AS
    | ASC
    | AUTHID
    | AUTHORIZATION
    | AUTO_INCREMENT
    | BEGIN
    | BETWEEN
    | BY
    | CALL
    | CASCADE
    | CASCADED
    | CASE
    | COMMIT
    | CONSTANT
    | CREATE
    | CROSS
    | CURRENT_USER
    | CURSOR
    | SYS_REFCURSOR
    | FOUND
    | NOTFOUND
    | ISOPEN
    | ROWCOUNT
    | BULK_ROWCOUNT
    | BULK_EXCEPTIONS
    | SUBTYPE
    | DECLARE
    | DEFINER
    | DELETE
    | DESC
    | DETERMINISTIC
    | DISTINCT
    | DROP
    | ELSE
    | ENCRYPTION
    | END
    | ESCAPE
    | EXCEPT
    | EXCEPTION
    | EXEC
    | EXECUTE
    | EXISTS
    | ERRORS
    | EXPLAIN
    | FETCH
    | FIRST
    | FLASHBACK
    | FOR
    | FROM
    | REF
    | FULL
    | GLOBAL
    | GRANT
    | HAVING
    | IDENTIFIED
    | IDENTITY
    | IDENTITY_INSERT
    | IF
    | IN
    | INCREMENT
    | INNER
    | INSERT
    | INTERSECT
    | INTO
    | IS
    | JOIN
    | LANGUAGE
    | LEFTARG
    | LEFT
    | LIKE
    | LIMIT
    | LOCAL
    | MATCHED
    | MATERIALIZED
    | MERGE
    | MINUS_SET
    | MODIFY
    | NATURAL
    | NAME
    | NOT
    | NULL_LITERAL
    | OFFSET
    | ON
    | ONLY
    | OR
    | OPERATOR
    | OUTER
    | OVERLAPS
    | PARALLEL_ENABLE
    | PIPELINED
    | PLAN
    | RENAME
    | REPLACE
    | RECURSIVE
    | REJECT
    | ROWTYPE
    | RESTRICT
    | RESULT_CACHE
    | REVOKE
    | RIGHTARG
    | RIGHT
    | ROLLBACK
    | ROWNUM
    | ROWS
    | EACH
    | STATEMENT
    | FOLLOWS
    | PRECEDES
    | SELECT
    | SET
    | START
    | TABLE
    | VIEW
    | INDEX
    | SCHEMA
    | DATABASE
    | SEQUENCE
    | PROCEDURE
    | FUNCTION
    | TRIGGER
    | BEFORE
    | AFTER
    | INSTEAD
    | REFERENCING
    | SYNONYM
    | COLUMN
    | DEFAULT
    | PRIMARY
    | FOREIGN
    | PRAGMA
    | SUBTYPE
    | REFERENCES
    | CONSTRAINT
    | CHECK
    | CONTAINS
    | ORDER
    | GROUP
    | TEMPORARY
    | THEN
    | TO
    | TOP
    | TRUNCATE
    | UNION
    | UPDATE
    | USING
    | VALUES
    | WHEN
    | WHERE
    | WITH
    | TRY
    | CATCH
    | THROW
    | SWITCH
    | BREAK
    ;

identifier
    : ID
    | LOCAL_TEMP_ID
    | DOUBLE_QUOTED_ID
    | nonReservedKeyword
    ;

nonReservedKeyword
    : USER
    | AGGREGATE
    | ROLE
    | CALCULATE
    | COMMENT
    | EXEC
    | SQL_CALC_FOUND_ROWS
    | AUTHORIZATION
    | CASCADED
    | VALUE
    | DDL
    | LIMIT
    | AUDIT
    | NOAUDIT
    | LOGIN
    | LOGON
    | LOGOUT
    | LOGOFF
    | SERERR
    | TIMER
    | ONCE
    | STARTUP
    | SHUTDOWN
    | WINDOW
    | JSON_VALUE
    | JSON_QUERY
    | JSON_TABLE
    | JSON_ARRAY
    | JSON_OBJECT
    | JSON_OVERLAPS
    | JSON_SET
    | JSON_REPLACE
    | JSON_INSERT
    | JSON_EXTRACT
    | JSON_UNQUOTE
    | JSON_KEYS
    | JSON_CONTAINS_PATH
    | JSON_BUILD_ARRAY
    | JSON_BUILD_OBJECT
    | JSON_TYPE
    | JSON_TYPEOF
    | JSON_CONCAT
    | JSON_VALID
    | JSON_OBJECT_KEYS
    | ROW_TO_JSON
    | TO_JSONB
    | TO_JSON
    | JSONB_OBJECT_KEYS
    | JSONB_EACH
    | JSONB_EACH_TEXT
    | JSONB_ARRAY_ELEMENTS
    | JSONB_ARRAY_ELEMENTS_TEXT
    | JSONB_STRIP_NULLS
    | JSONB_SET
    | JSONB_OBJECT_AGG
    | JSONB_CONCAT
    | JSONB_BUILD_OBJECT
    | JSONB_AGG
    | JSONB_BUILD_ARRAY
    | JSONB_TYPEOF
    | JSON
    | XMLCAST
    | XMLPARSE
    | XMLQUERY
    | XMLSERIALIZE
    | XMLTYPE
    | SF_XMLQUERY
    | EXISTSNODE
    | EXTRACTVALUE
    | XMLSEQUENCE
    | DELETEXML
    | APPENDCHILDXML
    | INSERTCHILDXML
    | INSERTCHILDXMLAFTER
    | INSERTCHILDXMLBEFORE
    | INSERTXMLBEFORE
    | INSERTXMLAFTER
    | UPDATEXML
    | XMLTRANSFORM
    | CONTENT
    | DOCUMENT
    | WELLFORMED
    | ENCODING
    | VERSION
    | INDENT
    | HIDE
    | SHOW
    | DEFAULTS
    | STRICT
    | LAX
    | TYPE
    | UNDER
    | FINAL
    | ABSTRACT
    | EXTENDS
    | OVERRIDE
    | VARRAY
    | NEW
    | MEMBER
    | MULTISET
    | STATIC
    | CONSTRUCTOR
    | INSTANTIABLE
    | PERSISTABLE
    | OVERRIDING
    | MAP
    | UNIQUE
    | KEY
    | CLUSTER
    | PARTIAL
    | BITMAP
    | ARRAY
    | FORCE
    | FOUND
    | PACKAGE
    | BODY
    | DIRECTORY
    | LIBRARY
    | CONTEXT
    | DOMAIN
    | PROFILE
    | LINK
    | PUBLIC
    | PRIVATE
    | BEFORE
    | AFTER
    | INSTEAD
    | REFERENCING
    | EACH
    | STATEMENT
    | FOLLOWS
    | PRECEDES
    | PASSWORD_POLICY
    | PASSWORD
    | ACCOUNT
    | UNLOCK
    | IDENTITY_INSERT
    | AUTO
    | ENCRYPT
    | MANUAL
    | INITIALIZED
    | EXTERNALLY
    | ACCESSED
    | GLOBALLY
    | RADIUS
    | NO
    | SALT
    | SESSION_PER_USER
    | CONNECT_IDLE_TIME
    | CONNECT_TIME
    | CPU_PER_CALL
    | CPU_PER_SESSION
    | MEM_SPACE
    | READ_PER_CALL
    | READ_PER_SESSION
    | GLOBAL_SESSION_PER_USER
    | FAILED_LOGIN_ATTEMPTS
    | PASSWORD_LIFE_TIME
    | PASSWORD_REUSE_TIME
    | PASSWORD_REUSE_MAX
    | PASSWORD_LOCK_TIME
    | PASSWORD_GRACE_TIME
    | INACTIVE_ACCOUNT_TIME
    | ALLOW_IP
    | NOT_ALLOW_IP
    | ALLOW_DATETIME
    | NOT_ALLOW_DATETIME
    | RETAIN
    | DISCARD
    | OLD
    | MON
    | TUE
    | WED
    | THU
    | THUR
    | THURS
    | FRI
    | SAT
    | SUN
    | EXPIRE
    | QUOTA
    | UNLIMITED
    | TABLESPACE
    | DATAFILE
    | LOCATION
    | PARMS
    | FIELDS
    | RECORD
    | RECORDS
    | DELIMITED
    | REF
    | SIZE
    | MIRROR
    | AUTOEXTEND
    | MAXSIZE
    | WRAPPED
    | STRIPING
    | HIGH
    | NORMAL
    | EXTERNAL
    | HUGE
    | COMPRESS
    | PATH
    | FORMAT
    | NESTED
    | ORDINALITY
    | OPTIMIZE
    | COPY
    | LANGUAGE
    | NAME
    | C_LANGUAGE
    | CS_LANGUAGE
    | JAVA_LANGUAGE
    | MICRO
    | ONLINE
    | OFFLINE
    | OFF
    | BUILD
    | DEFERRED
    | MEMORY
    | SPFILE
    | PREBUILT
    | REDUCED
    | PRECISION
    | DOUBLE
    | CHAR
    | CHARACTER
    | NCHAR
    | NATIONAL
    | BYTE
    | VARYING
    | LARGE
    | NEVER
    | REFRESH
    | FAST
    | COMPLETE
    | DEMAND
    | QUERY
    | REWRITE
    | REBUILD
    | MOVE
    | REUSE
    | SPLIT
    | EXCHANGE
    | INDEXES
    | AT_KEYWORD
    | SECTION
    | FILESIZE
    | DELTA
    | SYNCHRONOUS
    | ASYNCHRONOUS
    | NONE
    | ROWID
    | WITHOUT
    | KEYS
    | NOSORT
    | UNUSABLE
    | PARALLEL
    | PARALLEL_ENABLE
    | NOPARALLEL
    | LEFTARG
    | RIGHTARG
    | RANDOMLY
    | PENDANT
    | MATCH
    | MATCHED
    | MODEL
    | DIMENSION
    | MEASURES
    | RULES
    | UPSERT
    | NAV
    | UPDATED
    | ITERATE
    | ISOPEN
    | AUTOMATIC
    | SEQUENTIAL
    | REFERENCE
    | SIMPLE
    | ACTION
    | VISIBLE
    | VIRTUAL
    | GENERATED
    | ALWAYS
    | VALIDATE
    | NOVALIDATE
    | BRANCH
    | NOBRANCH
    | CLUSTERBTR
    | COUNTER
    | COUNT
    | LONG
    | FILLFACTOR
    | MINEXTENTS
    | INITIAL
    | DISKSPACE
    | SEGMENT
    | CREATION
    | ADVANCED
    | LOGIC
    | LOG
    | PURGE
    | OPERATOR
    | REJECT
    | CORRUPT
    | RESIZE
    | COMPILE
    | DEBUG
    | ELSIF
    | ELSEIF
    | LOOP
    | WHILE
    | TRY
    | CATCH
    | THROW
    | SWITCH
    | BREAK
    | SYS_REFCURSOR
    | SUBTYPE
    | NOTFOUND
    | ROWCOUNT
    | BULK_ROWCOUNT
    | BULK_EXCEPTIONS
    | REPEAT
    | UNTIL
    | FORALL
    | INDICES
    | GOTO
    | EXIT
    | CONTINUE
    | REVERSE
    | RAISE
    | OPEN
    | CLOSE
    | IMMEDIATE
    | DEFERRABLE
    | INITIALLY
    | SAVE
    | EXCEPTIONS
    | PRINT
    | OUT
    | APPLY
    | CONNECT
    | ABSOLUTE
    | RELATIVE
    | CONNECT_BY_ISLEAF
    | CONNECT_BY_ISCYCLE
    | SYS_CONNECT_BY_PATH
    | WITHIN
    | OVER
    | OVERLAPS
    | OVERFLOW
    | PARTITION
    | SUBPARTITION
    | PARTITIONS
    | SUBPARTITIONS
    | HASH
    | LIST
    | EQU
    | LESS
    | THAN
    | NOMAXVALUE
    | NOMINVALUE
    | MAXVALUE
    | MINVALUE
    | CYCLE
    | SEARCH
    | BREADTH
    | DEPTH
    | TEMPLATE
    | STORAGE
    | STORE
    | HASHPARTMAP
    | DISTRIBUTED
    | FULLY
    | MAIN
    | PIVOT
    | UNPIVOT
    | INCLUDE
    | EXCLUDE
    | XMLTABLE
    | XMLNAMESPACES
    | XMLNAMESPACE
    | XMLAGG
    | XMLELEMENT
    | XMLATTRIBUTES
    | XMLFOREST
    | XML
    | EVALNAME
    | PASSING
    | SAMPLE
    | SEED
    | BLOCK
    | CORRESPONDING
    | ROLLUP
    | CUBE
    | GROUPING
    | SETS
    | ANY
    | SOME
    | RANGE
    | BINARY
    | UNBOUNDED
    | PRECEDING
    | FOLLOWING
    | CURRENT
    | ROW
    | MOVEMENT
    | NEXT
    | LAST
    | OF
    | COLUMNS
    | TIES
    | PERCENT_KEYWORD
    | RETURN
    | RETURNING
    | BACKUP
    | BACKUPSET
    | BACKUPSETS
    | BACKUPNAME
    | BACKUPINFO
    | INFO
    | META
    | TXT
    | DB
    | BACKUPDIR
    | BACKED
    | UP
    | SINCE
    | BASE
    | ARCHIVEDIR
    | ARCHIVELOG
    | NOARCHIVELOG
    | ARCHIVE
    | RESTORE
    | RECOVER
    | REMOVE
    | REPAIR
    | CONFIGURE
    | CHECKPOINT
    | LOGFILE
    | MOUNT
    | SUSPEND
    | STANDBY
    | DEVICE
    | DISK
    | TAPE
    | MAXPIECESIZE
    | COMPRESSED
    | TRACE
    | FILE
    | FILES
    | TASK
    | THREAD
    | MAPPED
    | USE
    | DB_MAGIC
    | BAK_MAGIC
    | END_LSN
    | REUSE_DMINI
    | DMINI
    | SHADOW
    | DDL_CLONE
    | CUMULATIVE
    | STRUCT
    | TRXID
    | TIMES
    | INPUT
    | SPACE
    | OVERWRITE
    | EXTEND
    | CLEAR
    | AUTO_CLEAR
    | SPEED
    | NODE
    | STAT
    | BULK
    | COLLECT
    | SP_SET_PARA_VALUE
    | SP_SET_PARA_DOUBLE_VALUE
    | SP_SET_PARA_STRING_VALUE
    | SF_SET_SESSION_PARA_VALUE
    | SP_RESET_SESSION_PARA_VALUE
    | SP_SET_PARAM_IN_SESSION
    | SF_SET_SYSTEM_PARA_VALUE
    | SP_SET_INI_PARA_VALUE
    | SP_SET_SESSION_READONLY
    | SP_SET_OGUID
    | SP_APPLY_KEEP_PKG
    | SP_CLEAR_ARCH_SEND_INFO
    | SP_CLEAR_RAPPLY_STAT
    | SP_TAB_INDEX_STAT_INIT
    | SP_DB_STAT_INIT
    | SP_INDEX_STAT_INIT
    | SP_COL_STAT_INIT
    | SP_TAB_COL_STAT_INIT
    | SP_STAT_ON_TABLE_COLS
    | SP_TAB_STAT_INIT
    | SP_SQL_STAT_INIT
    | SP_INDEX_STAT_DEINIT
    | SP_COL_STAT_DEINIT
    | SP_TAB_COL_STAT_DEINIT
    | SP_TAB_STAT_DEINIT
    | SP_UPDATE_SYSSTATS
    | SP_TAB_MSTAT_DEINIT
    | SP_CREATE_AUTO_STAT_TRIGGER
    | SP_FLUSH_MODIFICATIONS_INFO
    | SP_CLEAN_MODIFICATIONS
    | SP_SET_ENABLE_AUDIT
    | SP_AUDIT_STMT
    | SP_NOAUDIT_STMT
    | SP_AUDIT_OBJECT
    | SP_NOAUDIT_OBJECT
    | SP_AUDIT_SQLSEQ_START
    | SP_AUDIT_SQLSEQ_ADD
    | SP_AUDIT_SQLSEQ_END
    | SP_AUDIT_SQLSEQ_DEL
    | SP_AUDIT_SET_ENC
    | SP_DROP_AUDIT_FILE
    | SP_SWITCH_AUDIT_FILE
    | SP_CREATE_AUDIT_RULE
    | SP_DROP_AUDIT_RULE
    | SP_SET_ROLE
    | SP_INIT_SVI_SYS
    | SP_SWITCH_SVI
    | SP_RESTRICT_DBA
    | CAST
    | EXTRACT
    | TRIM
    | SUBSTRING
    | TREAT
    | TRANSLATE
    | ASCII
    | PRETTY
    | WRAPPER
    | CONDITIONAL
    | UNCONDITIONAL
    | EMPTY
    | ERROR
    | ERRORS
    | OBJECT
    | TRUE
    | FALSE
    | LEADING
    | TRAILING
    | BOTH
    | POSITION
    | OVERLAY
    | PLACING
    | DATE
    | TIME
    | DATETIME
    | ZONE
    | TIMESTAMP
    | INTERVAL
    | YEAR
    | MONTH
    | DAY
    | HOUR
    | MINUTE
    | SECOND
    | TIMEZONE_HOUR
    | TIMEZONE_MINUTE
    | WEEK
    | SCN
    | LSN
    | VERSIONS
    | ENABLE
    | DISABLE
    | TRIGGERS
    | LEXER
    | SYNC
    | TRANSACTION
    | TRX
    | SAVEPOINT
    | RELEASE
    | WORK
    | ISOLATION
    | LEVEL
    | COMMITTED
    | UNCOMMITTED
    | SERIALIZABLE
    | AUTOCOMMIT
    | SESSION
    | SYSTEM
    | UR
    | WAIT
    | NOWAIT
    | READ
    | WRITE
    | LOCK
    | MODE
    | INTENT
    | PRESERVE
    | SHARE
    | EXCLUSIVE
    | DUMP
    | DML
    | MONITORING
    | NOMONITORING
    | USAGE
    | PRIVILEGES
    | ADMIN
    | OPTION
    | CLASS
    | SKIP_KEYWORD
    | LOCKED
    | SIBLINGS
    | COLLATE
    | NOCACHE
    | NOORDER
    | CACHE
    | KEEP
    | DENSE_RANK
    | NULLS
    | RESPECT
    | IGNORE
    | PIPE
    | THROUGH
    ;
