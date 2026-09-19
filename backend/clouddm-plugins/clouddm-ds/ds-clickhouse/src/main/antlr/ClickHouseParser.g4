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

parser grammar ClickHouseParser;

options {
    tokenVocab = ClickHouseLexer;
}

@members {
    private boolean isQueryParameterName(String name) {
        if (name.startsWith("`") || name.startsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        return name.startsWith("param_");
    }

    private boolean isColumnAliasAllowed() {
        for (ParserRuleContext context = _ctx; context != null; context = context.getParent()) {
            if (context instanceof SelectStmtContext) {
                return true;
            }
            if (context instanceof KillWhereClauseContext) {
                return false;
            }
        }
        return true;
    }
}

// Top-level statements

root
    : SEMICOLON* queryStmt? (SEMICOLON+ queryStmt)* SEMICOLON* EOF
    ;

queryStmt
    : query (INTO OUTFILE stringLiteral)?
      ((FORMAT identifierOrNull) settingsClause? | settingsClause (FORMAT identifierOrNull)?)?               # QueryStmtQuery
    | insertStmt                                                                   # QueryStmtInsert
    | deleteStmt                                                                   # QueryStmtDelete
    | updateStmt                                                                   # QueryStmtUpdate
    | executeAsStmt                                                                # QueryStmtExecuteAs
    | systemDefinitionStmt                                                         # QueryStmtSystemDefinition
    | accessStmt                                                                   # QueryStmtAccess
    ;

query
    : alterStmt     // DDL
    | attachStmt    // DDL
    | checkStmt
    | createStmt    // DDL
    | describeCacheStmt
    | describeStmt
    | dropStmt      // DDL
    | existsStmt
    | explainStmt
    | killStmt      // DDL
    | optimizeStmt  // DDL
    | renameStmt    // DDL
    | selectUnionStmt
    | setStmt
    | setRoleStmt
    | setTimeZoneStmt
    | showStmt
    | systemStmt
    | truncateStmt  // DDL
    | useStmt
    | watchStmt
    ;

// ALTER statement

alterStmt
    : ALTER TABLE tableIdentifier clusterClause? alterTableClause (COMMA alterTableClause)*  # AlterTableStmt
    ;

alterTableClause
    : ADD COLUMN (IF NOT EXISTS)? tableColumnDfnt (AFTER nestedIdentifier | FIRST)?           # AlterTableClauseAddColumn
    | ADD INDEX (IF NOT EXISTS)? tableIndexDfnt (AFTER nestedIdentifier)?             # AlterTableClauseAddIndex
    | ADD PROJECTION (IF NOT EXISTS)? tableProjectionDfnt (AFTER nestedIdentifier)?   # AlterTableClauseAddProjection
    | ATTACH partitionClause (FROM tableIdentifier)?                                  # AlterTableClauseAttach
    | CLEAR COLUMN (IF EXISTS)? nestedIdentifier (IN partitionClause)?                # AlterTableClauseClearColumn
    | CLEAR INDEX (IF EXISTS)? nestedIdentifier (IN partitionClause)?                 # AlterTableClauseClearIndex
    | CLEAR PROJECTION (IF EXISTS)? nestedIdentifier (IN partitionClause)?            # AlterTableClauseClearProjection
    | COMMENT COLUMN (IF EXISTS)? identifier stringLiteral                            # AlterTableClauseComment
    | DELETE WHERE columnExpr                                                         # AlterTableClauseDelete
    | DETACH partitionClause                                                          # AlterTableClauseDetach
    | DROP COLUMN (IF EXISTS)? identifier                                             # AlterTableClauseDropColumn
    | DROP INDEX (IF EXISTS)? nestedIdentifier                                        # AlterTableClauseDropIndex
    | DROP PROJECTION (IF EXISTS)? nestedIdentifier                                   # AlterTableClauseDropProjection
    | DROP partitionClause                                                            # AlterTableClauseDropPartition
    | FREEZE partitionClause?                                                         # AlterTableClauseFreezePartition
    | MATERIALIZE INDEX (IF EXISTS)? nestedIdentifier (IN partitionClause)?           # AlterTableClauseMaterializeIndex
    | MATERIALIZE PROJECTION (IF EXISTS)? nestedIdentifier (IN partitionClause)?      # AlterTableClauseMaterializeProjection
//    | MODIFY COLUMN (IF EXISTS)? nestedIdentifier codecExpr                           # AlterTableClauseModifyCodec
//    | MODIFY COLUMN (IF EXISTS)? nestedIdentifier COMMENT stringLiteral               # AlterTableClauseModifyComment
//    | MODIFY COLUMN (IF EXISTS)? nestedIdentifier REMOVE tableColumnPropertyType      # AlterTableClauseModifyRemove
//    | MODIFY COLUMN (IF EXISTS)? tableColumnDfnt                                      # AlterTableClauseModify
    | MODIFY ORDER BY columnExpr                                                      # AlterTableClauseModifyOrderBy
    | MODIFY ttlClause                                                                # AlterTableClauseModifyTTL
    | MOVE partitionClause ( TO DISK stringLiteral
                           | TO VOLUME stringLiteral
                           | TO TABLE tableIdentifier
                           )                                                          # AlterTableClauseMovePartition
    | REMOVE TTL                                                                      # AlterTableClauseRemoveTTL
    | RENAME COLUMN (IF EXISTS)? identifier TO identifier                             # AlterTableClauseRenameColumn
    | REPLACE partitionClause FROM tableIdentifier                                    # AlterTableClauseReplace
    | UPDATE assignmentExprList whereClause                                           # AlterTableClauseUpdate
    | MODIFY commentClause                                                            # AlterTableModifyComment
    | MODIFY COLUMN identifier DEFAULT literal                                        # AlterTableModifyColumnDefault
    | ALTER COLUMN (IF EXISTS)? identifier TYPE columnTypeExpr? tableColumnPropertyExpr? commentClause?
     codecExpr? ttlClause? (AFTER nestedIdentifier | FIRST)?  settingExprList?        #AlterTableAlterColumn
    | MODIFY COLUMN (IF EXISTS)? identifier columnTypeExpr? tableColumnPropertyExpr? commentClause?
         codecExpr? ttlClause? (AFTER nestedIdentifier | FIRST)?  settingExprList?    #AlterTableModifyColumn
    ;

assignmentExprList: assignmentExpr (COMMA assignmentExpr)*;
assignmentExpr: nestedIdentifier EQ_SINGLE columnExpr;

tableColumnPropertyType: ALIAS | CODEC | COMMENT | DEFAULT | MATERIALIZED | TTL;

partitionClause
    : PARTITION columnExpr         // actually we expect here any form of tuple of literals
    | PARTITION ID stringLiteral
    ;

// ATTACH statement
attachStmt
    : ATTACH DICTIONARY tableIdentifier clusterClause?  # AttachDictionaryStmt
    ;

// CHECK statement

checkStmt: CHECK TABLE tableIdentifier partitionClause?;

// CREATE statement

createStmt
    : (ATTACH | CREATE) DATABASE (IF NOT EXISTS)? databaseIdentifier clusterClause? engineExpr? commentClause?                                                                                      # CreateDatabaseStmt
    | (ATTACH | CREATE (OR REPLACE)? | REPLACE) DICTIONARY (IF NOT EXISTS)? tableIdentifier uuidClause? clusterClause? dictionarySchemaClause dictionaryEngineClause                                          # CreateDictionaryStmt
    | (ATTACH | CREATE) MATERIALIZED VIEW (IF NOT EXISTS)? tableIdentifier uuidClause? clusterClause? tableSchemaClause? (destinationClause | engineClause POPULATE?) subqueryClause  # CreateMaterializedViewStmt
    | (ATTACH | CREATE (OR REPLACE)? | REPLACE) TEMPORARY? TABLE (IF NOT EXISTS)? tableIdentifier uuidClause? clusterClause? tableSchemaClause? engineClause? subqueryClause?                                 # CreateTableStmt
    | (ATTACH | CREATE) (OR REPLACE)? VIEW (IF NOT EXISTS)? tableIdentifier uuidClause? clusterClause? tableSchemaClause? subqueryClause                                              # CreateViewStmt
    ;

dictionarySchemaClause: LPAREN dictionaryAttrDfnt (COMMA dictionaryAttrDfnt)* RPAREN;
dictionaryAttrDfnt
locals [java.util.Set<String> attrs = new java.util.HashSet<String>(); ]:
    identifier columnTypeExpr
    ( {!$attrs.contains("default")}?      DEFAULT literal       {$attrs.add("default");}
    | {!$attrs.contains("expression")}?   EXPRESSION columnExpr {$attrs.add("expression");}
    | {!$attrs.contains("hierarchical")}? HIERARCHICAL          {$attrs.add("hierarchical");}
    | {!$attrs.contains("injective")}?    INJECTIVE             {$attrs.add("injective");}
    | {!$attrs.contains("is_object_id")}? IS_OBJECT_ID          {$attrs.add("is_object_id");}
    )*
    ;
dictionaryEngineClause
locals [java.util.Set<String> clauses = new java.util.HashSet<String>();]:
    dictionaryPrimaryKeyClause?
    ( {!$clauses.contains("source")}? sourceClause {$clauses.add("source");}
    | {!$clauses.contains("lifetime")}? lifetimeClause {$clauses.add("lifetime");}
    | {!$clauses.contains("layout")}? layoutClause {$clauses.add("layout");}
    | {!$clauses.contains("range")}? rangeClause {$clauses.add("range");}
    | {!$clauses.contains("settings")}? dictionarySettingsClause {$clauses.add("settings");}
    )*
    ;
dictionaryPrimaryKeyClause: PRIMARY KEY columnExprList;
dictionaryArgExpr: identifier (identifier (LPAREN RPAREN)? | literal);
sourceClause: SOURCE LPAREN identifier LPAREN dictionaryArgExpr* RPAREN RPAREN;
lifetimeClause: LIFETIME LPAREN ( DECIMAL_LITERAL
                                | MIN DECIMAL_LITERAL MAX DECIMAL_LITERAL
                                | MAX DECIMAL_LITERAL MIN DECIMAL_LITERAL
                                ) RPAREN;
layoutClause: LAYOUT LPAREN identifier LPAREN dictionaryArgExpr* RPAREN RPAREN;
rangeClause: RANGE LPAREN (MIN identifier MAX identifier | MAX identifier MIN identifier) RPAREN;
dictionarySettingsClause: SETTINGS LPAREN settingExprList RPAREN;

clusterClause: ON CLUSTER (identifier | stringLiteral);
uuidClause: UUID stringLiteral;
destinationClause: TO tableIdentifier;
subqueryClause: AS selectUnionStmt;
tableSchemaClause
    : LPAREN tableElementExpr (COMMA tableElementExpr)* RPAREN  # SchemaDescriptionClause
    | AS tableIdentifier                                        # SchemaAsTableClause
    | AS tableFunctionExpr                                      # SchemaAsFunctionClause
    ;
engineClause
locals [java.util.Set<String> clauses = new java.util.HashSet<String>();]:
    engineExpr
    ( {!$clauses.contains("orderByClause")}?     orderByClause     {$clauses.add("orderByClause");}
    | {!$clauses.contains("partitionByClause")}? partitionByClause {$clauses.add("partitionByClause");}
    | {!$clauses.contains("primaryKeyClause")}?  primaryKeyClause  {$clauses.add("primaryKeyClause");}
    | {!$clauses.contains("sampleByClause")}?    sampleByClause    {$clauses.add("sampleByClause");}
    | {!$clauses.contains("ttlClause")}?         ttlClause         {$clauses.add("ttlClause");}
    | {!$clauses.contains("settingsClause")}?    settingsClause    {$clauses.add("settingsClause");}
    | {!$clauses.contains("comment")}?           commentClause    {$clauses.add("comment");}
    )*
    ;
partitionByClause: PARTITION BY columnExpr;
primaryKeyClause: PRIMARY KEY columnExpr;
sampleByClause: SAMPLE BY columnExpr;
ttlClause: TTL ttlExpr (COMMA ttlExpr)*;
commentClause: COMMENT STRING_LITERAL ;

engineExpr: ENGINE EQ_SINGLE? identifierOrNull (LPAREN columnExprList? RPAREN)?;
tableElementExpr
    : PRIMARY KEY columnExpr                                                       # TableElementExprPri
    | tableColumnDfnt                                                              # TableElementExprColumn
    | CONSTRAINT identifier CHECK columnExpr                                       # TableElementExprConstraint
    | INDEX tableIndexDfnt                                                         # TableElementExprIndex
    | PROJECTION tableProjectionDfnt                                               # TableElementExprProjection
    ;
tableColumnDfnt
    : nestedIdentifier ( NULLABLE LPAREN columnTypeExpr RPAREN | columnTypeExpr) tableColumnPropertyExpr? (COMMENT stringLiteral)? codecExpr? (TTL columnExpr)?
//    | nestedIdentifier columnTypeExpr? tableColumnPropertyExpr (COMMENT stringLiteral)? codecExpr? (TTL columnExpr)?
    ;
tableColumnPropertyExpr: (DEFAULT | MATERIALIZED | ALIAS) columnExpr;
tableIndexDfnt: nestedIdentifier columnExpr TYPE columnTypeExpr GRANULARITY DECIMAL_LITERAL;
tableProjectionDfnt: nestedIdentifier projectionSelectStmt;
codecExpr: CODEC LPAREN codecArgExpr (COMMA codecArgExpr)* RPAREN;
codecArgExpr: identifier (LPAREN columnExprList? RPAREN)?;
ttlExpr
    : columnExpr (DELETE whereClause? | TO DISK stringLiteral | TO VOLUME stringLiteral)?
    | columnExpr groupByClause SET ttlSetExpr (COMMA ttlSetExpr)*;
ttlSetExpr: columnExpr EQ_SINGLE columnExpr;

// DESCRIBE statement

describeStmt: (DESCRIBE | DESC) TABLE? tableExpr;
describeCacheStmt: (DESCRIBE | DESC) FILESYSTEM CACHE stringLiteral;

// DROP statement

dropStmt
    : (DETACH | DROP) DATABASE (IF EXISTS)? databaseIdentifier clusterClause?                                  # DropDatabaseStmt
    | (DETACH | DROP) (DICTIONARY | TEMPORARY? TABLE | VIEW) (IF EXISTS)? tableIdentifier clusterClause? (NO DELAY)?  # DropTableStmt
    ;

// EXISTS statement

existsStmt
    : EXISTS DATABASE databaseIdentifier                             # ExistsDatabaseStmt
    | EXISTS (DICTIONARY | TEMPORARY? TABLE | VIEW)? tableIdentifier # ExistsTableStmt
    ;

// EXPLAIN statement

explainStmt
    : EXPLAIN (AST | SYNTAX | QUERY TREE | PLAN | PIPELINE | ESTIMATE | TABLE OVERRIDE)? settingExprList? selectUnionStmt
    ;

// INSERT statement

insertStmt: INSERT INTO TABLE? (tableIdentifier | FUNCTION tableFunctionExpr) columnsClause? dataClause;

columnsClause: LPAREN nestedIdentifier (COMMA nestedIdentifier)* RPAREN;
dataClause
    : FORMAT identifier                                                         # DataClauseFormat
    | VALUES assignmentValues (COMMA assignmentValues)*                         # DataClauseValues
    | selectUnionStmt                                         # DataClauseSelect
    ;

assignmentValues
    : LPAREN assignmentValue (COMMA assignmentValue)* RPAREN
    | LPAREN RPAREN
    ;
assignmentValue
    : literal
    ;

// DELETE statement

deleteStmt
    : DELETE FROM nestedIdentifier clusterClause? inPartitionClause? whereClause?
    ;

inPartitionClause
    : IN PARTITION columnExpr
    ;

// UPDATE statement

updateStmt
    : UPDATE nestedIdentifier SET assignmentExprList clusterClause? inPartitionClause? whereClause
    ;

// KILL statement

killStmt
    : KILL MUTATION clusterClause? whereClause (SYNC | ASYNC | TEST)?  # KillMutationStmt
    | KILL QUERY_SQL clusterClause? killWhereClause (SYNC | ASYNC | TEST)? # KillQueryStmt
    ;

// KILL predicates do not accept aliases; SYNC/ASYNC/TEST are trailing command modes.
killWhereClause: WHERE columnExpr;

// OPTIMIZE statement

optimizeStmt: OPTIMIZE TABLE tableIdentifier clusterClause? partitionClause? FINAL? DEDUPLICATE?;

// RENAME statement

renameStmt: RENAME renameEntityClause clusterClause?;

renameEntityClause
    : TABLE tableIdentifier TO tableIdentifier (COMMA tableIdentifier TO tableIdentifier)*
    | DATABASE databaseIdentifier TO databaseIdentifier (COMMA databaseIdentifier TO databaseIdentifier)*
    | DICTIONARY dictionaryIdentifier TO dictionaryIdentifier (COMMA dictionaryIdentifier TO dictionaryIdentifier)*
    ;

// PROJECTION SELECT statement

projectionSelectStmt:
    LPAREN
    withClause?
    SELECT columnExprList
    groupByClause?
    projectionOrderByClause?
    RPAREN
    ;

// SELECT statement

selectUnionStmt: withClause? selectStmtWithParens ((UNION (ALL | DISTINCT)? | EXCEPT | INTERSECT) selectStmtWithParens)*;
selectStmtWithParens: selectStmt | LPAREN selectUnionStmt RPAREN;
selectStmt:
    SELECT DISTINCT? topClause? columnExprList
    fromClause?
    arrayJoinClause?
    windowClause?
    prewhereClause?
    whereClause?
    qualifyClause?
    groupByClause? (WITH (CUBE | ROLLUP))? (WITH TOTALS)?
    havingClause?
    orderByClause?
    interpolateClause?
    limitByClause?
    limitClause?
    settingsClause?
    ;

withClause: WITH withExprList;
withExprList: withExpr (COMMA withExpr)*;
withExpr
    : RECURSIVE? identifier AS LPAREN selectUnionStmt RPAREN  # WithExprSubquery
    | columnExpr AS identifier                                # WithExprExpression
    ;
topClause: TOP DECIMAL_LITERAL (WITH TIES)?;
fromClause: FROM joinExpr;
arrayJoinClause: (LEFT | INNER)? ARRAY JOIN columnExprList;
windowClause: WINDOW identifier AS LPAREN windowExpr RPAREN;
prewhereClause: PREWHERE columnExpr;
qualifyClause: QUALIFY columnExpr;
whereClause: WHERE columnExpr;
groupByClause
  : GROUP BY ALL                                           # GroupByClauseAll
  | GROUP BY (CUBE | ROLLUP) LPAREN columnExprList RPAREN  # GroupByClauseCubeOrRollup
  | GROUP BY GROUPING SETS LPAREN columnExprList RPAREN    # GroupByClauseGroupingSets
  | GROUP BY columnExprList                                # GroupByClauseSimple
  ;
havingClause: HAVING columnExpr;
orderByClause: ORDER BY orderExprList;
interpolateClause: INTERPOLATE LPAREN columnExprList RPAREN;
projectionOrderByClause: ORDER BY columnExprList;
limitByClause: LIMIT limitExpr BY columnExprList;
limitClause: LIMIT limitExpr (WITH TIES)?;
settingsClause: SETTINGS settingExprList;

joinExpr
    : joinExpr (GLOBAL | LOCAL)? joinOp? JOIN joinExpr joinConstraintClause  # JoinExprOp
    | joinExpr joinOpCross joinExpr                                          # JoinExprCrossOp
    | tableExpr FINAL? sampleClause?                                         # JoinExprTable
    | LPAREN joinExpr RPAREN                                                 # JoinExprParens
    ;
joinOp
    : ((ALL | ANY | ASOF)? INNER | INNER (ALL | ANY | ASOF)? | (ALL | ANY | ASOF))  # JoinOpInner
    | ( (SEMI | ALL | ANTI | ANY | ASOF)? (LEFT | RIGHT) OUTER?
      | (LEFT | RIGHT) OUTER? (SEMI | ALL | ANTI | ANY | ASOF)?
      )                                                                             # JoinOpLeftRight
    | ((ALL | ANY)? FULL OUTER? | FULL OUTER? (ALL | ANY)?)                         # JoinOpFull
    ;
joinOpCross
    : (GLOBAL|LOCAL)? CROSS JOIN
    | COMMA
    ;
joinConstraintClause
    : ON columnExprList
    | USING LPAREN columnExprList RPAREN
    | USING columnExprList
    ;

sampleClause: SAMPLE ratioExpr (OFFSET ratioExpr)?;
limitExpr: columnExpr ((COMMA | OFFSET) columnExpr)?;
orderExprList: orderExpr (COMMA orderExpr)*;
orderExpr: columnExpr (ASCENDING | DESCENDING | DESC)? (NULLS (FIRST | LAST))? (COLLATE stringLiteral)? (WITH FILL (FROM columnExpr)? (TO columnExpr)? (STEP columnExpr)?)?;
ratioExpr: numberLiteral (SLASH numberLiteral)?;
settingExprList: settingExpr (COMMA settingExpr)*;
settingExpr
    : identifier (EQ_SINGLE (literal | DEFAULT | queryParameter | settingMap
                             | {isQueryParameterName($identifier.text)}? parameterSettingValue)
                  | {!isQueryParameterName($identifier.text)}?)
    ;
// Query parameters also accept identifier values and nested collections of literals.
parameterSettingValue: nestedIdentifier | settingCollection;
settingMap: LBRACE (stringLiteral COLON stringLiteral (COMMA stringLiteral COLON stringLiteral)*)? RBRACE;
settingCollection
    : LBRACKET (settingCollectionValue (COMMA settingCollectionValue)*)? RBRACKET
    | LPAREN settingCollectionValue (COMMA settingCollectionValue)+ RPAREN
    | LBRACE (settingCollectionValue COLON settingCollectionValue
              (COMMA settingCollectionValue COLON settingCollectionValue)*)? RBRACE
    ;
settingCollectionValue: literal | settingCollection;

windowExpr: winPartitionByClause? winOrderByClause? winFrameClause?;
winPartitionByClause: PARTITION BY columnExprList;
winOrderByClause: ORDER BY orderExprList;
winFrameClause: (ROWS | RANGE) winFrameExtend;
winFrameExtend
    : winFrameBound                             # frameStart
    | BETWEEN winFrameBound AND winFrameBound   # frameBetween
    ;
winFrameBound: (CURRENT ROW | UNBOUNDED PRECEDING | UNBOUNDED FOLLOWING | numberLiteral PRECEDING | numberLiteral FOLLOWING);
//rangeClause: RANGE LPAREN (MIN identifier MAX identifier | MAX identifier MIN identifier) RPAREN;


// SET statement

setStmt: SET settingExprList;
setTimeZoneStmt: SET TIME ZONE EQ_SINGLE? literal;
setRoleStmt: SET ROLE (DEFAULT | NONE | ALL (EXCEPT roleNameList)? | roleNameList);
roleNameList: roleName (COMMA roleName)*;
roleName: identifier | stringLiteral;

executeAsStmt: EXECUTE AS (identifier | stringLiteral) executeAsBody?;
// An impersonated statement is a child; it cannot recursively impersonate again.
executeAsBody
    : query (INTO OUTFILE stringLiteral)?
      ((FORMAT identifierOrNull) settingsClause? | settingsClause (FORMAT identifierOrNull)?)? # ExecuteAsBodyQuery
    | insertStmt                                                   # ExecuteAsBodyInsert
    | deleteStmt                                                   # ExecuteAsBodyDelete
    | updateStmt                                                   # ExecuteAsBodyUpdate
    ;

// Access-control statements use structured clauses, not an opaque SQL tail.
accessStmt
    : createUserStmt | alterUserStmt | dropUserStmt
    | createRoleStmt | alterRoleStmt | dropRoleStmt
    | setDefaultRoleStmt | grantStmt | revokeStmt | checkGrantStmt
    | createRowPolicyStmt | alterRowPolicyStmt | dropRowPolicyStmt
    | createSettingsProfileStmt | alterSettingsProfileStmt | dropSettingsProfileStmt
    | createQuotaStmt | alterQuotaStmt | dropQuotaStmt
    ;
accessName: identifier | stringLiteral;
accessNameList: accessName (COMMA accessName)*;
accessUserName: accessName (AT accessName)?;
accessUserNames: accessUserName (COMMA accessUserName)*;
accessRoleSet: NONE | ALL (EXCEPT excluded=accessUserNames)? | members=accessUserNames (EXCEPT excluded=accessUserNames)?;
accessGrantees: ANY (EXCEPT accessUserNames)? | NONE | accessUserNames (EXCEPT accessUserNames)?;
accessCreateGuard: IF NOT EXISTS | OR REPLACE;
accessStorage: IN accessName;
accessDropStorage: FROM accessName;
accessRename: RENAME TO accessName;

createUserStmt: CREATE USER accessCreateGuard? accessUserNames createUserClause*;
createUserClause
    : userAuthentication | userValidity | userHosts | userDefaultDatabase
    | userRoles | userDefaultRoles | userGrantees | accessSettings
    | clusterClause | accessStorage
    ;
alterUserStmt: ALTER USER (IF EXISTS)? accessUserNames alterUserClause+;
alterUserClause
    : userAuthentication | ADD userAuthentication | RESET AUTHENTICATION METHODS TO NEW
    | userValidity | userHosts | (ADD | DROP) userHosts
    | userDefaultDatabase | userDefaultRoles | userGrantees | alterAccessSettings
    | accessRename | clusterClause | accessStorage
    ;
dropUserStmt: DROP USER (IF EXISTS)? accessUserNames accessDropStorage? clusterClause?;
userRoles: ROLE accessRoleSet;
userDefaultRoles: DEFAULT ROLE accessRoleSet;
userDefaultDatabase: DEFAULT DATABASE (NONE | accessName);
userGrantees: GRANTEES accessGrantees;
userValidity: VALID (UNTIL stringLiteral | FOR INTERVAL numberLiteral interval);
userAuthentication
    : NOT IDENTIFIED
    | IDENTIFIED (WITH authenticationMethod | BY authenticationString userValidity?)
      (COMMA authenticationMethod)*
    ;
authenticationString: stringLiteral | queryParameter;
authenticationMethod
    : (PLAINTEXT_PASSWORD | SHA256_PASSWORD | DOUBLE_SHA1_PASSWORD | BCRYPT_PASSWORD | SCRAM_SHA256_PASSWORD)
      BY authenticationString userValidity?
    | (SHA256_HASH | SCRAM_SHA256_HASH) BY authenticationString (SALT authenticationString)? userValidity?
    | (DOUBLE_SHA1_HASH | BCRYPT_HASH) BY authenticationString userValidity?
    | NO_PASSWORD userValidity?
    | LDAP SERVER authenticationString userValidity?
    | KERBEROS (REALM authenticationString)? userValidity?
    | SSL_CERTIFICATE (CN | SAN) authenticationString (COMMA authenticationString)* userValidity?
    | SSH_KEY BY publicSshKey (COMMA publicSshKey)* userValidity?
    | HTTP SERVER authenticationString (SCHEME authenticationString)? userValidity?
    ;
publicSshKey: KEY authenticationString TYPE authenticationString;
userHosts: HOST (ANY | NONE | hostEntry (COMMA hostEntry)*);
hostEntry: LOCAL | (NAME | REGEXP | LIKE | IP) stringLiteral (COMMA stringLiteral)*;

createRoleStmt: CREATE ROLE accessCreateGuard? accessUserNames (accessSettings | clusterClause | accessStorage)*;
alterRoleStmt: ALTER ROLE (IF EXISTS)? accessUserNames alterRoleClause+;
alterRoleClause: accessRename | alterAccessSettings | clusterClause | accessStorage;
dropRoleStmt: DROP ROLE (IF EXISTS)? accessUserNames accessDropStorage? clusterClause?;
setDefaultRoleStmt: SET DEFAULT ROLE accessRoleSet TO accessUserNames;

// Persistent settings have constraints/inheritance and a separate ALTER syntax.
// They must not be mistaken for standalone SET or its session-variable semantics.
accessSettings
    : (SETTING | SETTINGS) (NONE | accessSetting (COMMA accessSetting)*)
    | (PROFILE | PROFILES) accessNameList
    | INHERIT (PROFILE | PROFILES)? accessNameList
    ;
accessSetting
    : (PROFILE | INHERIT (PROFILE | PROFILES)?) accessName
    | nestedIdentifier (EQ_SINGLE literal)? settingConstraint*
    ;
settingConstraint: (MIN | MAX) EQ_SINGLE? literal | READONLY | CONST | WRITABLE | CHANGEABLE_IN_READONLY;
alterAccessSettings: accessSettings | alterAccessSetting (COMMA alterAccessSetting)*;
alterAccessSetting
    : (ADD | MODIFY) (SETTING | SETTINGS) accessSetting
    | SET nestedIdentifier (EQ_SINGLE literal)? settingConstraint*
    | ADD (PROFILE | PROFILES) accessNameList
    | DROP (SETTING | SETTINGS) nestedIdentifier (COMMA nestedIdentifier)*
    | DROP (PROFILE | PROFILES) accessNameList
    | DROP ALL (SETTINGS | PROFILES)
    ;

// A privilege is a named capability. Its object/column list is never a query.
grantStmt
    : GRANT clusterClause? (accessRights | currentGrants | accessUserNames)
      clusterClause? TO accessUserNames clusterClause? grantOption? replaceGrantOption? clusterClause?
    ;
grantOption: WITH (GRANT | ADMIN) OPTION;
replaceGrantOption: WITH REPLACE OPTION;
currentGrants: CURRENT GRANTS (ON accessScope | LPAREN accessRights RPAREN);
revokeStmt
    : REVOKE clusterClause? ((GRANT | ADMIN) OPTION FOR)? (accessRights | accessRoleSet)
      clusterClause? FROM accessRoleSet clusterClause?
    ;
checkGrantStmt: CHECK GRANT accessRights;
accessRights: accessRightGroup (COMMA accessRightGroup)*;
accessRightGroup: accessPrivilegeColumns (COMMA accessPrivilegeColumns)* ON accessScope;
accessPrivilegeColumns: accessPrivilege (LPAREN identifier (COMMA identifier)* RPAREN)?;
accessScope
    : ASTERISK (DOT ASTERISK)?
    | identifier ASTERISK? (DOT (ASTERISK | identifier ASTERISK?))?
      (LPAREN stringLiteral RPAREN)?
    ;
accessPrivilege
    : ALL PRIVILEGES? | NONE | SELECT | INSERT | UPDATE | DELETE | ALTER | CREATE | DROP | SHOW
    | CREATE (USER | ROLE | ROW? POLICY | QUOTA | SETTINGS? PROFILE | TABLE | VIEW | DATABASE
              | DICTIONARY | FUNCTION | NAMED COLLECTION)
    | ALTER (USER | ROLE | ROW? POLICY | QUOTA | SETTINGS? PROFILE | TABLE | VIEW | DATABASE
             | NAMED COLLECTION | UPDATE | DELETE | ADD COLUMN | DROP COLUMN | MODIFY COLUMN | RENAME COLUMN)
    | DROP (USER | ROLE | ROW? POLICY | QUOTA | SETTINGS? PROFILE | TABLE | VIEW | DATABASE
            | DICTIONARY | FUNCTION | NAMED COLLECTION)
    | SHOW (USERS | ROLES | ROW? POLICIES | QUOTAS | SETTINGS? PROFILES | ACCESS
            | DATABASES | TABLES | COLUMNS | DICTIONARIES | NAMED COLLECTIONS)
    | ACCESS MANAGEMENT | ROLE ADMIN | TRUNCATE | OPTIMIZE | BACKUP | DICTGET
    | SYSTEM (RELOAD (DICTIONARY | DICTIONARIES) | FLUSH LOGS | SHUTDOWN | SYNC REPLICA
              | (START | STOP) (MERGES | TTL MERGES | FETCHES | DISTRIBUTED SENDS | REPLICATED SENDS))
    | KILL QUERY_SQL | TABLE ENGINE | NAMED COLLECTION | IMPERSONATE | DISPLAY_SECRETS
    | READ | WRITE | SOURCES
    ;

rowPolicyNames: accessNameList clusterClause? ON accessScope (COMMA accessScope)* (COMMA accessNameList ON accessScope)*;
createRowPolicyStmt
    : CREATE ROW? POLICY accessCreateGuard? rowPolicyNames rowPolicyClause* (TO accessRoleSet)? clusterClause?
    ;
alterRowPolicyStmt
    : ALTER ROW? POLICY (IF EXISTS)? rowPolicyNames (accessRename | rowPolicyClause)* (TO accessRoleSet)? clusterClause?
    ;
rowPolicyClause
    : AS (PERMISSIVE | RESTRICTIVE)
    | (FOR (SELECT | ALL))? USING (NONE | columnExpr)
    | clusterClause | accessStorage
    ;
dropRowPolicyStmt: DROP ROW? POLICY (IF EXISTS)? rowPolicyNames accessDropStorage? clusterClause?;

createSettingsProfileStmt
    : CREATE SETTINGS? PROFILE accessCreateGuard? accessNameList
      (accessSettings | clusterClause | accessStorage)* (TO accessRoleSet)? clusterClause?
    ;
alterSettingsProfileStmt
    : ALTER SETTINGS? PROFILE (IF EXISTS)? accessNameList
      (accessRename | alterAccessSettings | clusterClause | accessStorage)* (TO accessRoleSet)? clusterClause?
    ;
dropSettingsProfileStmt: DROP SETTINGS? PROFILE (IF EXISTS)? accessNameList accessDropStorage? clusterClause?;

createQuotaStmt: CREATE QUOTA accessCreateGuard? accessNameList quotaClause* (TO accessRoleSet)? clusterClause?;
alterQuotaStmt: ALTER QUOTA (IF EXISTS)? accessNameList (accessRename | quotaClause)* (TO accessRoleSet)? clusterClause?;
dropQuotaStmt: DROP QUOTA (IF EXISTS)? accessNameList accessDropStorage? clusterClause?;
quotaClause
    : NOT KEYED | (KEY | KEYED) BY accessNameList
    | (IPV4_PREFIX_BITS | IPV6_PREFIX_BITS) DECIMAL_LITERAL
    | quotaInterval (COMMA quotaInterval)*
    | clusterClause | accessStorage
    ;
quotaInterval: FOR RANDOMIZED? INTERVAL? numberLiteral interval (NO LIMITS | TRACKING ONLY | quotaLimits);
quotaLimits
    : MAX quotaResource EQ_SINGLE? quotaValue (COMMA MAX? quotaResource EQ_SINGLE? quotaValue)*
    | quotaResource MAX quotaValue (COMMA quotaResource MAX quotaValue)*
    ;
quotaValue: numberLiteral | stringLiteral;
quotaResource
    : QUERIES | ERRORS | QUERY_SQL SELECTS | QUERY_SQL INSERTS | RESULT ROWS | RESULT BYTES
    | READ ROWS | READ BYTES | EXECUTION TIME | WRITTEN BYTES
    | QUERY_SELECTS | QUERY_INSERTS | RESULT_ROWS | RESULT_BYTES | READ_ROWS | READ_BYTES | EXECUTION_TIME | WRITTEN_BYTES
    ;

// SHOW statements

showStmt
    : SHOW CREATE DATABASE databaseIdentifier                                                                                                                                                        # showCreateDatabaseStmt
    | SHOW CREATE DICTIONARY tableIdentifier                                                                                                                                                          # showCreateDictionaryStmt
    | SHOW CREATE? VIEW tableIdentifier                                                                                                                                                               # showCreateViewStmt
    | SHOW DATABASES ((NOT? (LIKE | ILIKE) stringLiteral) | WHERE columnExpr)? (LIMIT DECIMAL_LITERAL)?                                                                                               # showDatabasesStmt
    | SHOW DICTIONARIES (FROM databaseIdentifier)? ((NOT? (LIKE | ILIKE) stringLiteral) | WHERE columnExpr)? (LIMIT DECIMAL_LITERAL)?                                                                 # showDictionariesStmt
    | SHOW FULL? TEMPORARY? TABLES ((FROM | IN) databaseIdentifier)? ((NOT? (LIKE | ILIKE) stringLiteral) | WHERE columnExpr)? (LIMIT DECIMAL_LITERAL)?                                               # showTablesStmt
    | SHOW EXTENDED? FULL? (COLUMNS | FIELDS) (FROM | IN) (tableIdentifier | (identifier (FROM | IN) identifier)) ((NOT? (LIKE | ILIKE) stringLiteral) | WHERE columnExpr)? (LIMIT DECIMAL_LITERAL)?  # showColumnsStmt
    | SHOW EXTENDED? (INDEX | INDEXES | INDICES | KEYS) (FROM | IN) (tableIdentifier | (identifier (FROM | IN) identifier)) (WHERE columnExpr)?                                                       # showIndexStmt
    | SHOW PROCESSLIST                                                                                                                                                                                # showProcessListStmt
    | SHOW GRANTS (FOR accessRoleSet)? ((WITH IMPLICIT) FINAL? | FINAL (WITH IMPLICIT)?)? # showGrantsStmt
    | SHOW CREATE (USER accessUserNames? | USERS accessUserNames?) # showCreateUserStmt
    | SHOW CREATE (ROLE accessUserNames | ROLES accessUserNames?) # showCreateRoleStmt
    | SHOW CREATE ROW? (POLICY (rowPolicyNames | accessName | ON accessScope) | POLICIES (rowPolicyNames | accessName | ON accessScope)?) # showCreatePolicyStmt
    | SHOW CREATE (QUOTA (CURRENT | accessNameList)? | QUOTAS accessNameList?) # showCreateQuotaStmt
    | SHOW CREATE SETTINGS? (PROFILE accessNameList | PROFILES accessNameList?) # showCreateProfileStmt
    | SHOW USERS                                                                                                                                                                                      # showUsersStmt
    | SHOW (CURRENT | ENABLED)? ROLES                                                                                                                                                                 # showRolesStmt
    | SHOW SETTINGS? PROFILES                                                                                                                                                                         # showProfilesStmt
    | SHOW ROW? POLICIES (ON accessScope | accessName)? # showPoliciesStmt
    | SHOW QUOTAS                                                                                                                                                                                     # showQuotasStmt
    | SHOW CURRENT? QUOTA                                                                                                                                                                             # showQuotaStmt
    | SHOW ACCESS                                                                                                                                                                                     # showAccessStmt
    | SHOW CLUSTER (identifier | stringLiteral)                                                                                                                                                                      # showClusterStmt
    | SHOW CLUSTERS (NOT? (LIKE | ILIKE) stringLiteral)? (LIMIT DECIMAL_LITERAL)?                                                                                                                     # showClustersStmt
    | SHOW CHANGED? SETTINGS (LIKE | ILIKE) stringLiteral                                                                                                                                             # showSettingsStmt
    | SHOW SETTING (identifier | stringLiteral)                                                                                                                                                                      # showSettingStmt
    | SHOW FILESYSTEM CACHES                                                                                                                                                                          # showFilesystemCaches
    | SHOW ENGINES                                                                                                                                                                                    # showEnginesStmt
    | SHOW FUNCTIONS ((LIKE | ILIKE) stringLiteral)?                                                                                                                                                  # showFunctionsStmt
    | SHOW MERGES (NOT? (LIKE | ILIKE) stringLiteral)? (LIMIT DECIMAL_LITERAL)?                                                                                                                       # showMergesStmt
    | SHOW PRIVILEGES                                                                                                                                                                                 # showPrivilegesStmt
    | SHOW CREATE TEMPORARY? TABLE tableIdentifier                                                                                                                                                    # showCreateTableStmt
    ;

// SYSTEM statements

systemStmt
    : SYSTEM RELOAD (CONFIG | USERS) clusterClause?                                      # SystemConfigurationStmt
    | SYSTEM RELOAD (EMBEDDED DICTIONARIES | DICTIONARIES) clusterClause?                  # SystemReloadDictionariesStmt
    | SYSTEM RELOAD DICTIONARY systemDictionaryTarget                                    # SystemReloadDictionaryStmt
    | SYSTEM UNLOAD DICTIONARIES clusterClause?                                          # SystemUnloadDictionariesStmt
    | SYSTEM UNLOAD DICTIONARY systemDictionaryTarget                                    # SystemUnloadDictionaryStmt
    | SYSTEM RELOAD MODELS clusterClause?                                                # SystemReloadModelsStmt
    | SYSTEM RELOAD MODEL systemNamedTarget                                             # SystemReloadModelStmt
    | SYSTEM RELOAD FUNCTIONS clusterClause?                                             # SystemReloadFunctionsStmt
    | SYSTEM RELOAD FUNCTION systemNamedTarget                                           # SystemReloadFunctionStmt
    | SYSTEM systemCacheCommand                                                         # SystemCacheStmt
    | SYSTEM RELOAD ASYNCHRONOUS METRICS clusterClause?                                  # SystemReloadMetricsStmt
    | SYSTEM JEMALLOC PURGE clusterClause?                                               # SystemJemallocPurgeStmt
    | SYSTEM FLUSH LOGS clusterClause? (tableIdentifier (COMMA tableIdentifier)*)?         # SystemFlushLogsStmt
    | SYSTEM (START | STOP) LISTEN clusterClause? systemListenTarget                      # SystemListenStmt
    | SYSTEM (SHUTDOWN | KILL) clusterClause?                                             # SystemShutdownStmt
    | SYSTEM SUSPEND clusterClause? FOR DECIMAL_LITERAL SECOND                            # SystemSuspendStmt
    | SYSTEM RESTART DISK systemNamedTarget                                              # SystemRestartDiskStmt
    | SYSTEM FLUSH DISTRIBUTED tableIdentifier                                           # SystemFlushDistributedStmt
    | SYSTEM (START | STOP) (DISTRIBUTED SENDS | FETCHES | TTL? MERGES) tableIdentifier     # SystemTableControlStmt
    | SYSTEM (START | STOP) REPLICATED SENDS                                              # SystemReplicatedSendsStmt
    | SYSTEM SYNC REPLICA tableIdentifier                                                # SystemSyncReplicaStmt
    ;

// Target-bearing commands accept ON CLUSTER either before or after the target, once.
systemDictionaryTarget
    : clusterClause (tableIdentifier | stringLiteral)
    | (tableIdentifier | stringLiteral) clusterClause?
    ;
systemNamedTarget
    : clusterClause (identifier | stringLiteral)
    | (identifier | stringLiteral) clusterClause?
    ;

systemCacheCommand
    : (CLEAR | DROP) systemSimpleCache clusterClause?
    | (CLEAR | DROP) QUERY_SQL CACHE (TAG stringLiteral)? clusterClause?
    | (CLEAR | DROP) FILESYSTEM CACHE (stringLiteral (KEY identifier (OFFSET DECIMAL_LITERAL)?)?)? clusterClause?
    | (CLEAR | DROP) DISK METADATA CACHE systemNamedTarget
    | (CLEAR | DROP) SCHEMA CACHE (FOR (FILE | S3 | HDFS | URL | AZURE))?
    | (CLEAR | DROP) FORMAT SCHEMA CACHE (FOR (PROTOBUF | FILES))?
    | SYNC FILESYSTEM CACHE stringLiteral? clusterClause?
    ;
systemSimpleCache
    : (DNS | CONNECTIONS | MARK | PRIMARY INDEX | UNCOMPRESSED | INDEX MARK | INDEX UNCOMPRESSED
      | VECTOR SIMILARITY INDEX | TEXT INDEX (TOKENS | HEADER | POSTINGS) | MMAP | QUERY_SQL CONDITION
      | ENCRYPTION HEADERS | COMPILED EXPRESSION | ICEBERG METADATA | PAIMON METADATA | PARQUET METADATA
      | POINT IN POLYGON | PAGE | AVRO SCHEMA | S3 CLIENT) CACHE
    | TEXT INDEX CACHES
    ;
systemListenTarget
    : systemListenProtocol
    | QUERIES (ALL | DEFAULT | CUSTOM) (EXCEPT systemListenProtocol (COMMA systemListenProtocol)*)?
    ;
systemListenProtocol
    : TCP (SSH | WITH PROXY | SECURE)?
    | HTTP | HTTPS | MYSQL | GRPC | POSTGRESQL | PROMETHEUS | INTERSERVER (HTTP | HTTPS)
    | ARROW_SQL FLIGHT | CUSTOM stringLiteral
    ;

// Persistent definitions use the non-output query entry, like access-control DDL.
systemDefinitionStmt
    : CREATE NAMED COLLECTION (IF NOT EXISTS)? identifier clusterClause? AS namedCollectionSettings          # CreateNamedCollectionStmt
    | ALTER NAMED COLLECTION (IF EXISTS)? identifier clusterClause?
      (SET namedCollectionSettings (DELETE identifier (COMMA identifier)*)? | DELETE identifier (COMMA identifier)*) # AlterNamedCollectionStmt
    | DROP NAMED COLLECTION (IF EXISTS)? identifier clusterClause?                                          # DropNamedCollectionStmt
    | (CREATE RESOURCE (IF NOT EXISTS)? | CREATE OR REPLACE RESOURCE) identifier clusterClause?
      LPAREN resourceOperations RPAREN                                                                       # CreateResourceStmt
    | DROP RESOURCE (IF EXISTS)? identifier clusterClause?                                                   # DropResourceStmt
    | (CREATE WORKLOAD (IF NOT EXISTS)? | CREATE OR REPLACE WORKLOAD) identifier clusterClause?
      (IN identifier)? (SETTINGS workloadSetting (COMMA workloadSetting)*)?                                   # CreateWorkloadStmt
    | DROP WORKLOAD (IF EXISTS)? identifier clusterClause?                                                   # DropWorkloadStmt
    ;
namedCollectionSettings: namedCollectionSetting (COMMA namedCollectionSetting)*;
namedCollectionSetting: identifier EQ_SINGLE literal (NOT? OVERRIDABLE)?;
resourceOperations
    : resourceDiskOperation (COMMA resourceDiskOperation)*
    | resourceThreadOperation (COMMA resourceThreadOperation)*
    | QUERY_SQL
    | MEMORY RESERVATION
    ;
resourceDiskOperation: (READ | WRITE) (ANY DISK | DISK identifier);
resourceThreadOperation: (MASTER | WORKER) THREAD;
workloadSetting: identifier EQ_SINGLE literal (FOR identifier)?;

// TRUNCATE statements

truncateStmt: TRUNCATE TEMPORARY? TABLE? (IF EXISTS)? tableIdentifier clusterClause?;

// USE statement

useStmt: USE DATABASE? databaseIdentifier;

// WATCH statement

watchStmt: WATCH tableIdentifier EVENTS? (LIMIT DECIMAL_LITERAL)?;



// Columns

columnTypeExpr
    : identifier                                                                             # ColumnTypeExprSimple   // UInt64
    | identifier LPAREN identifier columnTypeExpr (COMMA identifier columnTypeExpr)* RPAREN  # ColumnTypeExprNested   // Nested
    | identifier LPAREN enumValue (COMMA enumValue)* RPAREN                                  # ColumnTypeExprEnum     // Enum
    | identifier LPAREN columnTypeExpr (COMMA columnTypeExpr)* RPAREN                        # ColumnTypeExprComplex  // Array, Tuple
    | identifier LPAREN columnExprList? RPAREN                                               # ColumnTypeExprParam    // FixedString(N)
    ;
columnExprList: columnsExpr (COMMA columnsExpr)*;
columnsExpr
    : (tableIdentifier DOT)? ASTERISK columnExceptExpr?                                 # ColumnsExprAsterisk
    | LPAREN selectUnionStmt RPAREN                                                     # ColumnsExprSubquery
    // NOTE: asterisk and subquery goes before |columnExpr| so that we can mark them as multi-column expressions.
    | columnExpr                                                                        # ColumnsExprColumn
    ;
columnExpr
    : CASE columnExpr? (WHEN columnExpr THEN columnExpr)+ (ELSE columnExpr)? END          # ColumnExprCase
    | CAST LPAREN columnExpr AS columnTypeExpr RPAREN                                     # ColumnExprCast
    | columnExpr DOUBLE_COLON columnTypeExpr                                              # ColumnExprCastSymbol
    | DATE stringLiteral                                                                  # ColumnExprDate
    | EXTRACT LPAREN interval FROM columnExpr RPAREN                                      # ColumnExprExtract
    | INTERVAL columnExpr interval                                                        # ColumnExprInterval
    | SUBSTRING LPAREN columnExpr FROM columnExpr (FOR columnExpr)? RPAREN                # ColumnExprSubstring
    | TIMESTAMP stringLiteral                                                             # ColumnExprTimestamp
    | TRIM LPAREN (BOTH | LEADING | TRAILING) stringLiteral  FROM columnExpr RPAREN       # ColumnExprTrim
    // TODO(ilezhankin): `BETWEEN a AND b AND c` is parsed in a wrong way: `BETWEEN (a AND b) AND c`
    | columnExpr NOT? BETWEEN columnExpr AND columnExpr                                   # ColumnExprBetween
    | NOT columnExpr                                                                      # ColumnExprNot
    | EXISTS LPAREN selectUnionStmt RPAREN                                                # ColumnExprExists
    | identifier (LPAREN columnExprList? RPAREN) OVER LPAREN windowExpr RPAREN            # ColumnExprWinFunction
    | identifier (LPAREN columnExprList? RPAREN) OVER identifier                          # ColumnExprWinFunctionTarget
    | identifier (LPAREN columnExprList? RPAREN)? LPAREN DISTINCT? columnArgList? RPAREN  # ColumnExprFunction
    | queryParameter                                                                      # ColumnExprParameter
    | literal                                                                             # ColumnExprLiteral

    | columnExpr LBRACKET columnExpr RBRACKET                                             # ColumnExprArrayAccess
    | columnExpr DOT (DECIMAL_LITERAL | stringLiteral | identifier)                       # ColumnExprTupleAccess
    | DASH columnExpr                                                                     # ColumnExprNegate
    | columnExpr ( ASTERISK                                                               // multiply
                 | SLASH                                                                  // divide
                 | PERCENT                                                                // modulo
                 ) columnExpr                                                             # ColumnExprPrecedence1
    | columnExpr ( PLUS                                                                   // plus
                 | DASH                                                                   // minus
                 | CONCAT                                                                 // concat
                 ) columnExpr                                                             # ColumnExprPrecedence2
    | columnExpr ( EQ_DOUBLE                                                              // equals
                 | EQ_SINGLE                                                              // equals
                 | NOT_EQ                                                                 // notEquals
                 | LE                                                                     // lessOrEquals
                 | GE                                                                     // greaterOrEquals
                 | LT                                                                     // less
                 | GT                                                                     // greater
                 | GLOBAL? NOT? IN                                                        // in, notIn, globalIn, globalNotIn
                 | NOT? (LIKE | ILIKE)                                                    // like, notLike, ilike, notILike
                 ) columnExpr                                                             # ColumnExprPrecedence3
    | columnExpr IS NOT? NULL_SQL                                                         # ColumnExprIsNull
    | columnExpr AND columnExpr                                                           # ColumnExprAnd
    | columnExpr OR columnExpr                                                            # ColumnExprOr
    | <assoc=right> columnExpr QUERY columnExpr COLON columnExpr                          # ColumnExprTernaryOp
    | columnExpr {isColumnAliasAllowed()}? (alias | AS identifier)                                                  # ColumnExprAlias

    | (tableIdentifier DOT)? ASTERISK                                                     # ColumnExprAsterisk  // single-column only
    | LPAREN selectUnionStmt RPAREN                                                       # ColumnExprSubquery  // single-column only
    | LPAREN columnExpr RPAREN                                                            # ColumnExprParens    // single-column only
    | LPAREN columnExprList? RPAREN                                                       # ColumnExprTuple
    | LBRACKET columnExprList? RBRACKET                                                   # ColumnExprArray
    | columnIdentifier                                                                    # ColumnExprIdentifier
    ;
columnArgList: columnArgExpr (COMMA columnArgExpr)*;
columnArgExpr: columnLambdaExpr | columnExpr;
columnLambdaExpr:
    ( LPAREN identifier (COMMA identifier)* RPAREN
    |        identifier (COMMA identifier)*
    )
    ARROW columnExpr
    ;
columnIdentifier: (tableIdentifier DOT)? nestedIdentifier;
nestedIdentifier: identifier (DOT identifier)*;
columnExceptExpr
    : EXCEPT (stringLiteral | (LPAREN stringLiteral RPAREN))                # columnExceptExprRegexp
    | EXCEPT (identifier | (LPAREN identifier (COMMA identifier)* RPAREN))  # columnExceptExprIdentifiers
    ;

// Tables

tableExpr
    : tableIdentifier                    # TableExprIdentifier
    | tableFunctionExpr                  # TableExprFunction
    | LPAREN selectUnionStmt RPAREN      # TableExprSubquery
    | tableExpr (alias | AS identifier)  # TableExprAlias
    ;
tableFunctionExpr: identifier LPAREN tableArgList? RPAREN;
tableIdentifier: (databaseIdentifier DOT)? identifier;
tableArgList: tableArgExpr (COMMA tableArgExpr)*;
tableArgExpr
    : tableFunctionExpr
    | literal
    | nestedIdentifier
    ;

// Databases

databaseIdentifier: identifier;

// Dictionaries

dictionaryIdentifier: (databaseIdentifier DOT)? identifier;

// Basics

floatingLiteral
    : FLOATING_LITERAL
    | DOT (DECIMAL_LITERAL | OCTAL_LITERAL)
    | DECIMAL_LITERAL DOT (DECIMAL_LITERAL | OCTAL_LITERAL)?  // can't move this to the lexer or it will break nested tuple access: t.1.2
    ;
numberLiteral: (PLUS | DASH)? (floatingLiteral | OCTAL_LITERAL | DECIMAL_LITERAL | HEXADECIMAL_NUMERIC_LITERAL | BINARY_NUMERIC_LITERAL | INF | NAN_SQL);
stringLiteral: HEXADECIMAL_STRING_LITERAL | BINARY_STRING_LITERAL | STRING_LITERAL;
literal
    : numberLiteral
    | JSON_FALSE
    | JSON_TRUE
    | stringLiteral
    | NULL_SQL
    ;
interval: NANOSECOND | MICROSECOND | MILLISECOND | SECOND | MINUTE | HOUR | DAY | WEEK | MONTH | QUARTER | YEAR;
keyword
    // except NULL_SQL, INF, NAN_SQL
    : ACCESS | ADD | AFTER | ALIAS | ALL | ALTER | AND | ANTI | ANY | ARRAY | AS | ASCENDING | ASOF | AST | ASYNC | ATTACH | BETWEEN | BOTH | BY | CACHES | CASE
    | CAST | CHECK | CLEAR | CLUSTER | CLUSTERS | CODEC | COLLATE | COLUMN | COLUMNS | COMMENT | CONSTRAINT | CREATE | CROSS | CUBE | CURRENT | CURRENT_USER | CHANGED | DATABASE
    | DATABASES | DATE | DAY | DEDUPLICATE | DEFAULT | DELAY | DELETE | DESC | DESCENDING | DESCRIBE | DETACH | DICTIONARIES | DICTIONARY | DISK
    | DISTINCT | DISTRIBUTED | DROP | ELSE | ENABLED | END | ENGINE | ENGINES | ESTIMATE | EVENTS | EXCEPT | EXISTS | EXPLAIN | EXPRESSION | EXTENDED | EXTRACT | FETCHES | FIELDS | FILESYSTEM | FILL | FINAL | FIRST
    | FLUSH | FOLLOWING | FOR | FORMAT | FREEZE | FROM | FULL | FUNCTION | FUNCTIONS | GLOBAL | GRANULARITY | GRANTS | GROUP | GROUPING | HAVING | HIERARCHICAL | HOUR | ID
    | IF | ILIKE | IMPLICIT | IN | INDEX | INDEXES | INDICES | INJECTIVE | INNER | INSERT | INTERPOLATE | INTERVAL | INTO | IS | IS_OBJECT_ID | JOIN | JSON_FALSE | JSON_TRUE | KEY | KEYS
    | KILL | LAST | LAYOUT | LEADING | LEFT | LIFETIME | LIKE | LIMIT | LIVE | LOCAL | LOGS | MATERIALIZE | MATERIALIZED | MAX | MERGES
    | MICROSECOND | MILLISECOND | MIN | MINUTE | MODIFY | MOVE | MUTATION | NO | NOT | NULLS | OFFSET | ON | OPTIMIZE | OR | ORDER | OUTER | OUTFILE | OVER | OVERRIDE | PARTITION | PIPELINE | PLAN
    | POLICY | POLICIES | POPULATE | PRECEDING | PREWHERE | PRIMARY | PRIVILEGES | PROCESSLIST | PROFILE | PROFILES | PROJECTION | QUARTER | QUOTA | QUOTAS | RANGE | RECURSIVE | RELOAD | REMOVE | RENAME | REPLACE | REPLICA | REPLICATED | RIGHT | ROLE | ROLES | ROLLUP | ROW
    | ROWS | SAMPLE | SECOND | SELECT | SEMI | SENDS | SET | SETTING | SETTINGS | SHOW | SOURCE | START | STOP | SUBSTRING | SYNC | SYNTAX | SYSTEM | STEP | TABLE
    | TABLES | TEMPORARY | TEST | THEN | TIES | TIMEOUT | TIMESTAMP | TO | TOP | TOTALS | TRAILING | TREE | TRIM | TRUNCATE | TTL | TYPE
    | ADMIN | AUTHENTICATION | BACKUP | BCRYPT_HASH | BCRYPT_PASSWORD | BYTES | CHANGEABLE_IN_READONLY | CN | COLLECTION | COLLECTIONS | CONST
    | DICTGET | DISPLAY_SECRETS | DOUBLE_SHA1_HASH | DOUBLE_SHA1_PASSWORD | ERRORS | EXECUTION | EXECUTION_TIME | GRANT | GRANTEES | HOST | HTTP
    | IDENTIFIED | IMPERSONATE | INHERIT | INSERTS | IP | IPV4_PREFIX_BITS | IPV6_PREFIX_BITS | KERBEROS | KEYED | LDAP | LIMITS | MANAGEMENT
    | METHODS | NAME | NAMED | NEW | NO_PASSWORD | ONLY | OPTION | PERMISSIVE | PLAINTEXT_PASSWORD | QUERIES | QUERY_INSERTS | QUERY_SELECTS
    | RANDOMIZED | READ | READONLY | READ_BYTES | READ_ROWS | REALM | REGEXP | RESET | RESTRICTIVE | RESULT | RESULT_BYTES | RESULT_ROWS
    | REVOKE | SALT | SAN | SCHEME | SCRAM_SHA256_HASH | SCRAM_SHA256_PASSWORD | SELECTS | SERVER | SHA256_HASH | SHA256_PASSWORD | SHUTDOWN
    | SOURCES | SSH_KEY | SSL_CERTIFICATE | TRACKING | UNTIL | VALID | WRITABLE | WRITE | WRITTEN | WRITTEN_BYTES | QUERY_SQL
    | EXECUTE | NONE | NULLABLE | TIME | ZONE
    | UNBOUNDED | UNION | UPDATE | USE | USER | USERS | USING | UUID | VALUES | VIEW | VOLUME | WATCH | WEEK | WHEN | WHERE | WINDOW | WITH | YEAR
    | ARROW_SQL | ASYNCHRONOUS | AVRO | AZURE | CACHE | CLIENT | COMPILED | CONDITION | CONFIG | CONNECTIONS
    | CUSTOM | DNS | EMBEDDED | ENCRYPTION | FILE | FILES | FLIGHT | GRPC | HDFS | HEADER
    | HEADERS | HTTPS | ICEBERG | INTERSERVER | JEMALLOC | LISTEN | MARK | MASTER | MEMORY | METADATA
    | METRICS | MMAP | MODEL | MODELS | MYSQL | OVERRIDABLE | PAGE | PAIMON | PARQUET | POINT
    | POLYGON | POSTGRESQL | POSTINGS | PROMETHEUS | PROTOBUF | PROXY | PURGE | RESERVATION | RESOURCE | RESTART
    | S3 | SCHEMA | SECURE | SIMILARITY | SSH | SUSPEND | TAG | TCP | TEXT | THREAD
    | TOKENS | UNCOMPRESSED | UNLOAD | URL | VECTOR | WORKER | WORKLOAD
    ;
keywordForAlias
    : AFTER | ALIAS | ALTER | AST | ASYNC | ATTACH | BOTH | CASE | CAST | CHECK | CLEAR | CLUSTER | CODEC
    | COLUMN | COMMENT | CONSTRAINT | CREATE | CUBE | CURRENT | DATABASE | DATABASES | DATE | DEDUPLICATE | DEFAULT | DELAY
    | DESCRIBE | DETACH | DICTIONARIES | DICTIONARY | DISK | DISTRIBUTED | DROP | ENGINE | EVENTS
    | EXISTS | EXPLAIN | EXPRESSION | EXTRACT | FETCHES | FLUSH | FOLLOWING | FREEZE | FUNCTION | GRANULARITY | HIERARCHICAL
    | ID | IF | INDEX | INJECTIVE | INSERT | IS_OBJECT_ID | KEY | KILL | LAYOUT | LEADING | LIFETIME | LIVE
    | LOGS | MATERIALIZE | MATERIALIZED | MAX | MERGES | MIN | MODIFY | MOVE | MUTATION | NO | OPTIMIZE | OUTFILE | OVER
    | PARTITION | POPULATE | PRECEDING | PRIMARY | RANGE | RELOAD | REMOVE | RENAME | REPLACE | REPLICA | REPLICATED | ROLLUP | ROW
    | SELECT | SENDS | SET | SHOW | SOURCE | START | STOP | SUBSTRING | SYNC | SYNTAX | SYSTEM | TABLE | TABLES | TEMPORARY
    | TEST | TIES | TIMEOUT | TIMESTAMP | TOTALS | TRAILING | TRIM | TRUNCATE | TTL | TYPE | UNBOUNDED | UPDATE
    | ADMIN | AUTHENTICATION | BACKUP | BCRYPT_HASH | BCRYPT_PASSWORD | BYTES | CHANGEABLE_IN_READONLY | CN | COLLECTION | COLLECTIONS | CONST
    | DICTGET | DISPLAY_SECRETS | DOUBLE_SHA1_HASH | DOUBLE_SHA1_PASSWORD | ERRORS | EXECUTION | EXECUTION_TIME | GRANT | GRANTEES | HOST | HTTP
    | IDENTIFIED | IMPERSONATE | INHERIT | INSERTS | IP | IPV4_PREFIX_BITS | IPV6_PREFIX_BITS | KERBEROS | KEYED | LDAP | LIMITS | MANAGEMENT
    | METHODS | NAME | NAMED | NEW | NO_PASSWORD | ONLY | OPTION | PERMISSIVE | PLAINTEXT_PASSWORD | QUERIES | QUERY_INSERTS | QUERY_SELECTS
    | RANDOMIZED | READ | READONLY | READ_BYTES | READ_ROWS | REALM | REGEXP | RESET | RESTRICTIVE | RESULT | RESULT_BYTES | RESULT_ROWS
    | REVOKE | SALT | SAN | SCHEME | SCRAM_SHA256_HASH | SCRAM_SHA256_PASSWORD | SELECTS | SERVER | SHA256_HASH | SHA256_PASSWORD | SHUTDOWN
    | SOURCES | SSH_KEY | SSL_CERTIFICATE | TRACKING | UNTIL | VALID | WRITABLE | WRITE | WRITTEN | WRITTEN_BYTES | QUERY_SQL
    | USE | UUID | VALUES | VIEW | VOLUME | WATCH | EXECUTE | NONE | TIME | ZONE
    | ARROW_SQL | ASYNCHRONOUS | AVRO | AZURE | CACHE | CLIENT | COMPILED | CONDITION | CONFIG | CONNECTIONS
    | CUSTOM | DNS | EMBEDDED | ENCRYPTION | FILE | FILES | FLIGHT | GRPC | HDFS | HEADER
    | HEADERS | HTTPS | ICEBERG | INTERSERVER | JEMALLOC | LISTEN | MARK | MASTER | MEMORY | METADATA
    | METRICS | MMAP | MODEL | MODELS | MYSQL | OVERRIDABLE | PAGE | PAIMON | PARQUET | POINT
    | POLYGON | POSTGRESQL | POSTINGS | PROMETHEUS | PROTOBUF | PROXY | PURGE | RESERVATION | RESOURCE | RESTART
    | S3 | SCHEMA | SECURE | SIMILARITY | SSH | SUSPEND | TAG | TCP | TEXT | THREAD
    | TOKENS | UNCOMPRESSED | UNLOAD | URL | VECTOR | WORKER | WORKLOAD
    ;
alias: IDENTIFIER | keywordForAlias;  // |interval| can't be an alias, otherwise 'INTERVAL 1 SOMETHING' becomes ambiguous.
queryParameter: LBRACE (IDENTIFIER | keyword | interval) COLON columnTypeExpr RBRACE;
identifier: IDENTIFIER | interval | keyword | queryParameter;
identifierOrNull: identifier | NULL_SQL;  // NULL_SQL can be only 'Null' here.
enumValue: stringLiteral EQ_SINGLE numberLiteral;
