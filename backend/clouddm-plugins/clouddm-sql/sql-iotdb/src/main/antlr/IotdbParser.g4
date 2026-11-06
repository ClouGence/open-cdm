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
parser grammar IotdbParser;

options { tokenVocab = IotdbLexer; }

treeScript: SEMI* (treeStatement (SEMI+ treeStatement)* SEMI*)? EOF;
tableScript: SEMI* (tableStatement (SEMI+ tableStatement)* SEMI*)? EOF;

treeStatement
    : treeQuery                                                                              # treeSelectStatement
    | INSERT INTO treeAbsolutePath LPAREN treeIdList RPAREN ALIGNED? VALUES valueRows        # treeInsertStatement
    | DELETE FROM treePathList (WHERE treeDeletePredicate)?                                  # treeDeleteStatement
    | (DROP | DELETE) TIMESERIES treePathList                                                # treeDropSeriesStatement
    | CREATE TIMESERIES treeAbsolutePath treeAlias? (WITH treeProperties)? treeTags? treeAttributes?
                                                                                            # treeCreateSeriesStatement
    | CREATE ALIGNED TIMESERIES treeAbsolutePath LPAREN treeColumn (COMMA treeColumn)* RPAREN # treeCreateAlignedStatement
    | (CREATE | SET) DATABASE treeAbsolutePath                                              # treeCreateDatabaseStatement
    | (DROP | DELETE) DATABASE treePathList                                                  # treeDropDatabaseStatement
    | SHOW (VERSION | DATABASES | CURRENT_SQL_DIALECT)                                      # treeShowScopeStatement
    | SHOW (DEVICES | TIMESERIES) treeAbsolutePath? (WITH DATABASE)? rowLimit*                # treeShowPathStatement
    ;

treeQuery
    : SELECT LAST? treeSelectItem (COMMA treeSelectItem)* FROM treePathList
      (WHERE treeExpression)? (treeOrder | treeLimit | ALIGN BY DEVICE)*
    ;
treeSelectItem: treeExpression (AS treeId)?;
treeOrder: ORDER BY treeOrderItem (COMMA treeOrderItem)*;
treeOrderItem: (TIME | DEVICE | TIMESERIES) (ASC | DESC)?;
treeLimit: rowLimit | (SLIMIT | SOFFSET) INTEGER;
treePathList: treeAbsolutePath (COMMA treeAbsolutePath)*;
treeAbsolutePath: ROOT (DOT treeNode)*;
treeRelativePath: treeNode (DOT treeNode)*;
treeNode: treeWildcard | treeWildcard treeNodeSlice treeWildcard? | treeNodeSlice treeWildcard | treeId;
treeNodeSlice: treeId | INTEGER;
treeWildcard: STAR | DOUBLE_STAR;
treeId: IDENTIFIER | TREE_IDENTIFIER | BACKTICK_IDENTIFIER | TIME | TIMESTAMP | DATE | DEVICE | TIMESERIES
    | ALIGNED | ALTER | ADD | COLUMN | COMMENT | TAGS | ATTRIBUTES;
treeIdList: treeId (COMMA treeId)*;
treeProperties: treeProperty (COMMA treeProperty)*;
treeProperty: treeId EQ (treeId | treeLiteral);
treeAlias: LPAREN treeId RPAREN;
treeColumn: treeId treeAlias? treeId treeColumnProperties? treeTags? treeAttributes?;
treeColumnProperties: treeProperty (COMMA? treeProperty)*;
treeTags: TAGS LPAREN treeStringProperties RPAREN;
treeAttributes: ATTRIBUTES LPAREN treeStringProperties RPAREN;
treeStringProperties: treeStringProperty (COMMA treeStringProperty)*;
treeStringProperty: (treeId | treeString) EQ (treeId | treeLiteral);
treeDeletePredicate
    : (TIME | TIMESTAMP) comparison treeTimeValue
    | LPAREN treeDeletePredicate RPAREN
    | treeDeletePredicate AND treeDeletePredicate
    | treeDeletePredicate OR treeDeletePredicate
    ;
treeTimeValue: MINUS? INTEGER | NOW LPAREN RPAREN | treeString;

treeExpression
    : treeAtom
    | (PLUS | MINUS | NOT) treeExpression
    | treeExpression (STAR | SLASH | PERCENT) treeExpression
    | treeExpression (PLUS | MINUS) treeExpression
    | treeExpression comparison treeExpression
    | treeExpression IS NOT? NULL
    | treeExpression NOT? (LIKE | REGEXP) treeString
    | treeExpression AND treeExpression
    | treeExpression OR treeExpression
    ;
treeAtom
    : treeLiteral
    | (TIME | TIMESTAMP)
    | (treeId | NOW) LPAREN (treeExpression (COMMA treeExpression)*)? RPAREN
    | treeSeriesReference
    | LPAREN treeExpression RPAREN
    ;
treeSeriesReference: treeAbsolutePath | treeRelativePath;
treeString: TREE_STRING | TREE_DOUBLE_STRING;
treeLiteral: treeString | number | NULL | TRUE | FALSE;

tableStatement
    : tableQuery                                                             # tableSelectStatement
    | INSERT INTO tableName (LPAREN tableIdList RPAREN)? (VALUES valueRows | tableQuery)
                                                                             # tableInsertStatement
    | UPDATE tableName SET tableAssignment (COMMA tableAssignment)* (WHERE tableExpression)?
                                                                             # tableUpdateStatement
    | DELETE FROM tableName (WHERE tableExpression)?                          # tableDeleteStatement
    | CREATE DATABASE (IF NOT EXISTS)? tableId                                # tableCreateDatabaseStatement
    | DROP DATABASE (IF EXISTS)? tableId                                      # tableDropDatabaseStatement
    | CREATE TABLE (IF NOT EXISTS)? tableName LPAREN tableColumn (COMMA tableColumn)* RPAREN
      (COMMENT TABLE_STRING)? (WITH LPAREN tableProperties RPAREN)?
                                                                             # tableCreateStatement
    | ALTER TABLE (IF EXISTS)? tableName ADD COLUMN (IF NOT EXISTS)? tableColumn # tableAddColumnStatement
    | ALTER TABLE (IF EXISTS)? tableName DROP COLUMN (IF EXISTS)? tableId       # tableDropColumnStatement
    | DROP TABLE (IF EXISTS)? tableName                                       # tableDropStatement
    | DROP VIEW (IF EXISTS)? tableName                                        # tableDropViewStatement
    | SHOW (VERSION | DATABASES | CURRENT_SQL_DIALECT | CURRENT_DATABASE)      # tableShowScopeStatement
    | SHOW TABLES DETAILS? ((FROM | IN) tableId)?                              # tableShowTablesStatement
    | (DESCRIBE | DESC) tableName DETAILS?                                    # tableDescribeStatement
    | SHOW CREATE (TABLE | VIEW) tableName                                    # tableShowCreateStatement
    ;

tableQuery
    : tableSelect (UNION (ALL | DISTINCT)? tableSelect)*
      (ORDER BY tableSortItem (COMMA tableSortItem)*)? rowLimit*
    ;
tableSelect
    : SELECT (ALL | DISTINCT)? tableSelectItem (COMMA tableSelectItem)*
      (FROM tableRelation (COMMA tableRelation | tableJoin)*)?
      (WHERE tableExpression)?
      (GROUP BY tableExpression (COMMA tableExpression)*)?
      (HAVING tableExpression)?
    ;
tableSelectItem: tableId DOT STAR | STAR | tableExpression (AS? tableId)?;
tableRelation: (tableName | LPAREN tableQuery RPAREN) (AS? tableId)?;
tableJoin: ((INNER | (LEFT | RIGHT | FULL) OUTER?)? JOIN tableRelation ON tableExpression) | CROSS JOIN tableRelation;
tableName: tableId (DOT tableId)?;
tableId: IDENTIFIER | QUOTED_IDENTIFIER | TIME | TIMESTAMP | DATE | DEVICE | TAG | ATTRIBUTE | FIELD
    | ALTER | ADD | COLUMN | COMMENT | TAGS | ATTRIBUTES;
tableIdList: tableId (COMMA tableId)*;
tableSortItem: tableExpression (ASC | DESC)? (NULLS (FIRST | LAST))?;
tableAssignment: tableId EQ tableExpression;
tableColumn: tableId tableId? (TIME | TAG | ATTRIBUTE | FIELD)? (COMMENT TABLE_STRING)?;
tableProperties: tableProperty (COMMA tableProperty)*;
tableProperty: tableId EQ (tableLiteral | tableId);

tableExpression
    : tableAtom
    | (PLUS | MINUS | NOT) tableExpression
    | tableExpression (STAR | SLASH | PERCENT) tableExpression
    | tableExpression (PLUS | MINUS) tableExpression
    | tableExpression comparison tableExpression
    | tableExpression IS NOT? NULL
    | tableExpression NOT? IN LPAREN (tableQuery | tableExpression (COMMA tableExpression)*) RPAREN
    | tableExpression NOT? LIKE TABLE_STRING
    | tableExpression AND tableExpression
    | tableExpression OR tableExpression
    ;
tableAtom
    : tableLiteral
    | (tableId | NOW) LPAREN (STAR | DISTINCT? tableExpression (COMMA tableExpression)*)? RPAREN
    | tableId (DOT tableId)*
    | EXISTS LPAREN tableQuery RPAREN
    | LPAREN (tableQuery | tableExpression) RPAREN
    ;
tableLiteral: (DATE | TIMESTAMP)? TABLE_STRING | number | NULL | TRUE | FALSE;

valueRows: valueRow (COMMA valueRow)*;
valueRow: LPAREN value (COMMA value)* RPAREN;
value: treeLiteral | tableLiteral | (PLUS | MINUS) number | NOW LPAREN RPAREN;
rowLimit: (LIMIT | OFFSET) INTEGER;
comparison: EQ | NE | LE | GE | LT | GT;
number: INTEGER | DECIMAL;
