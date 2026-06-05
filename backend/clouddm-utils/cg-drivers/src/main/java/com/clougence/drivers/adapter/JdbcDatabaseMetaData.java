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
package com.clougence.drivers.adapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.clougence.utils.StringUtils;

class JdbcDatabaseMetaData implements DatabaseMetaData {

    private final JdbcConnection    jdbcConnection;
    private final AdapterConnection adapterConnection;

    JdbcDatabaseMetaData(JdbcConnection jdbcConnection, AdapterConnection adapterConnection){
        this.jdbcConnection = jdbcConnection;
        this.adapterConnection = adapterConnection;
    }

    @Override
    public Connection getConnection() { return this.jdbcConnection; }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        this.jdbcConnection.checkOpen();
        return this.adapterConnection.unwrap(iface, this);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        this.jdbcConnection.checkOpen();
        return this.adapterConnection.unwrap(iface, this) != null;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public String getURL() { return this.adapterConnection.getInfo().getUrl(); }

    @Override
    public String getUserName() { return this.adapterConnection.getInfo().getUserName(); }

    @Override
    public String getDatabaseProductName() { return this.adapterConnection.getInfo().getDbVersion().getName(); }

    @Override
    public String getDatabaseProductVersion() { return this.adapterConnection.getInfo().getDbVersion().getVersion(); }

    @Override
    public int getDatabaseMajorVersion() { return this.adapterConnection.getInfo().getDbVersion().getMajorVersion(); }

    @Override
    public int getDatabaseMinorVersion() { return this.adapterConnection.getInfo().getDbVersion().getMinorVersion(); }

    @Override
    public String getDriverName() { return this.adapterConnection.getInfo().getDriverVersion().getName(); }

    @Override
    public String getDriverVersion() { return this.adapterConnection.getInfo().getDriverVersion().getVersion(); }

    @Override
    public int getDriverMajorVersion() { return this.adapterConnection.getInfo().getDriverVersion().getMajorVersion(); }

    @Override
    public int getDriverMinorVersion() { return this.adapterConnection.getInfo().getDriverVersion().getMinorVersion(); }

    @Override
    public int getJDBCMajorVersion() { return JdbcDriver.VERSION_MAJOR; }

    @Override
    public int getJDBCMinorVersion() { return JdbcDriver.VERSION_MINOR; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public String getSQLKeywords() { return StringUtils.EMPTY; }

    @Override
    public String getNumericFunctions() { return StringUtils.EMPTY; }

    @Override
    public String getStringFunctions() { return StringUtils.EMPTY; }

    @Override
    public String getSystemFunctions() { return StringUtils.EMPTY; }

    @Override
    public String getTimeDateFunctions() { return StringUtils.EMPTY; }

    @Override
    public String getIdentifierQuoteString() { return StringUtils.EMPTY; }

    @Override
    public String getCatalogSeparator() { return "."; }

    @Override
    public boolean usesLocalFiles() {
        return true;
    }

    @Override
    public boolean usesLocalFilePerTable() {
        return true;
    }

    @Override
    public boolean supportsSavepoints() {
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return false;
    }

    @Override
    public boolean generatedKeyAlwaysReturned() {
        return false;
    }

    @Override
    public boolean nullsAreSortedHigh() {
        return false;
    }

    @Override
    public boolean nullsAreSortedLow() {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() {
        return false;
    }

    @Override
    public boolean nullPlusNonNullIsNull() {
        return false;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() {
        return false;
    }

    @Override
    public boolean supportsCoreSQLGrammar() {
        return false;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() {
        return false;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() {
        return false;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() {
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() {
        return false;
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() {
        return false;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() {
        return false;
    }

    @Override
    public boolean supportsColumnAliasing() {
        return false;
    }

    @Override
    public boolean supportsGroupBy() {
        return false;
    }

    @Override
    public boolean supportsGroupByUnrelated() {
        return false;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() {
        return false;
    }

    @Override
    public boolean supportsTableCorrelationNames() {
        return false;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInExists() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInIns() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() {
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() {
        return false;
    }

    @Override
    public boolean supportsOuterJoins() {
        return false;
    }

    @Override
    public boolean supportsLimitedOuterJoins() {
        return false;
    }

    @Override
    public boolean supportsFullOuterJoins() {
        return false;
    }

    @Override
    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() {
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() {
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    @Override
    public boolean supportsUnion() {
        return false;
    }

    @Override
    public boolean supportsUnionAll() {
        return false;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() {
        return false;
    }

    @Override
    public boolean supportsOrderByUnrelated() {
        return false;
    }

    @Override
    public boolean supportsLikeEscapeClause() {
        return false;
    }

    @Override
    public boolean supportsMultipleResultSets() {
        return true;
    }

    @Override
    public boolean supportsMultipleTransactions() {
        return false;
    }

    @Override
    public boolean supportsNonNullableColumns() {
        return false;
    }

    @Override
    public boolean supportsPositionedDelete() {
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() {
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() {
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() {
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() { return 0; }

    @Override
    public int getMaxCharLiteralLength() { return 0; }

    @Override
    public int getMaxColumnNameLength() { return 0; }

    @Override
    public int getMaxColumnsInGroupBy() { return 0; }

    @Override
    public int getMaxColumnsInIndex() { return 0; }

    @Override
    public int getMaxColumnsInOrderBy() { return 0; }

    @Override
    public int getMaxColumnsInSelect() { return 0; }

    @Override
    public int getMaxColumnsInTable() { return 0; }

    @Override
    public int getMaxConnections() { return 0; }

    @Override
    public int getMaxCursorNameLength() { return 0; }

    @Override
    public int getMaxIndexLength() { return 0; }

    @Override
    public int getMaxSchemaNameLength() { return 0; }

    @Override
    public int getMaxProcedureNameLength() { return 0; }

    @Override
    public int getMaxCatalogNameLength() { return 0; }

    @Override
    public int getMaxRowSize() { return 0; }

    @Override
    public int getMaxStatementLength() { return 0; }

    @Override
    public int getMaxStatements() { return 0; }

    @Override
    public int getMaxTableNameLength() { return 0; }

    @Override
    public int getMaxTablesInSelect() { return 0; }

    @Override
    public int getMaxUserNameLength() { return 0; }

    @Override
    public boolean supportsTransactions() {
        return this.adapterConnection instanceof TransactionSupport;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) {
        if (this.adapterConnection instanceof TransactionSupport) {
            return ((TransactionSupport) this.adapterConnection).supportIsolation(level);
        } else {
            return false;
        }
    }

    @Override
    public int getDefaultTransactionIsolation() { return Connection.TRANSACTION_NONE; }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() {
        return false;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() {
        return false;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() {
        return false;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() {
        return false;
    }

    @Override
    public boolean supportsResultSetType(int type) {
        return false;
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) {
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) {
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int type) {
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int type) {
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) {
        return false;
    }

    @Override
    public boolean othersDeletesAreVisible(int type) {
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int type) {
        return false;
    }

    @Override
    public boolean updatesAreDetected(int type) {
        return false;
    }

    @Override
    public boolean deletesAreDetected(int type) {
        return false;
    }

    @Override
    public boolean insertsAreDetected(int type) {
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() {
        return false;
    }

    @Override
    public boolean supportsNamedParameters() {
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() {
        return false;
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) {
        return false;
    }

    @Override
    public int getResultSetHoldability() { return 0; }

    @Override
    public int getSQLStateType() { return 0; }

    @Override
    public boolean locatorsUpdateCopy() {
        return false;
    }

    @Override
    public boolean supportsStatementPooling() {
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() { return RowIdLifetime.ROWID_UNSUPPORTED; }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() {
        return false;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() {
        return false;
    }

    @Override
    public boolean supportsConvert() {
        return false;
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) {
        return false;
    }

    @Override
    public String getSearchStringEscape() { return ""; }

    @Override
    public String getExtraNameCharacters() { return ""; }

    @Override
    public boolean isCatalogAtStart() { return false; }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() {
        return false;
    }

    @Override
    public String getSchemaTerm() { return "schema"; }

    @Override
    public String getProcedureTerm() { return "procedure"; }

    @Override
    public String getCatalogTerm() { return "database"; }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return emptySet("TABLE_CAT"//
        );
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return emptySet("TABLE_SCHEM",//
                "TABLE_CATALOG"       //
        );
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        return emptySet("TABLE_SCHEM",//
                "TABLE_CATALOG"       //
        );
    }

    @Override
    public boolean allProceduresAreCallable() {
        return false;
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        return emptySet("PROCEDURE_CAT",              //
                "PROCEDURE_SCHEM",                    //
                "PROCEDURE_NAME",                     //
                "NUM_INPUT_PARAMS", JDBCType.INTEGER, //
                "NUM_OUTPUT_PARAMS", JDBCType.INTEGER,//
                "NUM_RESULT_SETS", JDBCType.INTEGER,  //
                "REMARKS",                            //
                "PROCEDURE_TYPE", JDBCType.SMALLINT,  //
                "SPECIFIC_NAME"                       //
        );
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        return emptySet("PROCEDURE_CAT",              //
                "PROCEDURE_SCHEM",                    //
                "PROCEDURE_NAME",                     //
                "COLUMN_NAME",                        //
                "COLUMN_TYPE", JDBCType.SMALLINT,     //
                "DATA_TYPE", JDBCType.INTEGER,        //
                "TYPE_NAME",                          //
                "PRECISION", JDBCType.INTEGER,        //
                "LENGTH", JDBCType.INTEGER,           //
                "SCALE", JDBCType.SMALLINT,           //
                "RADIX", JDBCType.SMALLINT,           //
                "NULLABLE", JDBCType.SMALLINT,        //
                "REMARKS",                            //
                "COLUMN_DEF",                         //
                "SQL_DATA_TYPE", JDBCType.INTEGER,    //
                "SQL_DATETIME_SUB", JDBCType.INTEGER, //
                "CHAR_OCTET_LENGTH", JDBCType.INTEGER,//
                "ORDINAL_POSITION", JDBCType.INTEGER, //
                "IS_NULLABLE",                        //
                "SPECIFIC_NAME"                       //
        );
    }

    @Override
    public boolean allTablesAreSelectable() {
        return false;
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return emptySet("TABLE_CAT",        //
                "TABLE_SCHEM",              //
                "TABLE_NAME",               //
                "TABLE_TYPE",               //
                "REMARKS",                  //
                "TYPE_CAT",                 //
                "TYPE_SCHEM",               //
                "TYPE_NAME",                //
                "SELF_REFERENCING_COL_NAME",//
                "REF_GENERATION"            //
        );
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        return emptySet("TABLE_TYPE"           //
        );
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return emptySet("TABLE_CAT",  //
                "TABLE_SCHEM",        //
                "TABLE_NAME",         //
                "GRANTOR",            //
                "GRANTEE",            //
                "PRIVILEGE",          //
                "IS_GRANTABLE"        //
        );
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return emptySet("TABLE_CAT",                  //
                "TABLE_SCHEM",                        //
                "TABLE_NAME",                         //
                "COLUMN_NAME",                        //
                "DATA_TYPE", JDBCType.INTEGER,        //
                "TYPE_NAME",                          //
                "COLUMN_SIZE", JDBCType.INTEGER,      //
                "BUFFER_LENGTH", JDBCType.INTEGER,    //
                "DECIMAL_DIGITS", JDBCType.INTEGER,   //
                "NUM_PREC_RADIX", JDBCType.INTEGER,   //
                "NULLABLE", JDBCType.INTEGER,         //
                "REMARKS",                            //
                "COLUMN_DEF",                         //
                "SQL_DATA_TYPE", JDBCType.INTEGER,    //
                "SQL_DATETIME_SUB", JDBCType.INTEGER, //
                "CHAR_OCTET_LENGTH", JDBCType.INTEGER,//
                "ORDINAL_POSITION", JDBCType.INTEGER, //
                "IS_NULLABLE",                        //
                "SCOPE_CATALOG",                      //
                "SCOPE_SCHEMA",                       //
                "SCOPE_TABLE",                        //
                "SOURCE_DATA_TYPE", JDBCType.SMALLINT,//
                "IS_AUTOINCREMENT",                   //
                "IS_GENERATEDCOLUMN"                  //
        );
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        return emptySet("TABLE_CAT",  //
                "TABLE_SCHEM",        //
                "TABLE_NAME",         //
                "COLUMN_NAME",        //
                "GRANTOR",            //
                "GRANTEE",            //
                "PRIVILEGE",          //
                "IS_GRANTABLE"        //
        );
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        return emptySet("SCOPE", JDBCType.SMALLINT,//
                "COLUMN_NAME",                     //
                "DATA_TYPE", JDBCType.INTEGER,     //
                "TYPE_NAME",                       //
                "COLUMN_SIZE", JDBCType.INTEGER,   //
                "BUFFER_LENGTH", JDBCType.INTEGER, //
                "DECIMAL_DIGITS", JDBCType.INTEGER,//
                "PSEUDO_COLUMN", JDBCType.SMALLINT //
        );
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        return emptySet("FUNCTION_CAT",            //
                "FUNCTION_SCHEM",                  //
                "FUNCTION_NAME",                   //
                "REMARKS",                         //
                "FUNCTION_TYPE", JDBCType.SMALLINT,//
                "SPECIFIC_NAME"                    //
        );
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        return emptySet("FUNCTION_CAT",               //
                "FUNCTION_SCHEM",                     //
                "FUNCTION_NAME",                      //
                "COLUMN_NAME",                        //
                "COLUMN_TYPE", JDBCType.SMALLINT,     //
                "DATA_TYPE", JDBCType.INTEGER,        //
                "TYPE_NAME",                          //
                "PRECISION", JDBCType.INTEGER,        //
                "LENGTH", JDBCType.INTEGER,           //
                "SCALE", JDBCType.INTEGER,            //
                "RADIX", JDBCType.SMALLINT,           //
                "NULLABLE", JDBCType.SMALLINT,        //
                "REMARKS",                            //
                "CHAR_OCTET_LENGTH", JDBCType.INTEGER,//
                "ORDINAL_POSITION", JDBCType.INTEGER, //
                "IS_NULLABLE",                        //
                "SPECIFIC_NAME"                       //
        );
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        return emptySet("SCOPE", JDBCType.SMALLINT,//
                "COLUMN_NAME",                     //
                "DATA_TYPE", JDBCType.INTEGER,     //
                "TYPE_NAME",                       //
                "COLUMN_SIZE", JDBCType.INTEGER,   //
                "BUFFER_LENGTH", JDBCType.INTEGER, //
                "DECIMAL_DIGITS", JDBCType.INTEGER,//
                "PSEUDO_COLUMN", JDBCType.SMALLINT //
        );
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return emptySet("TABLE_CAT",         //
                "TABLE_SCHEM",               //
                "TABLE_NAME",                //
                "COLUMN_NAME",               //
                "KEY_SEQ", JDBCType.SMALLINT,//
                "PK_NAME"                    //
        );
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        return emptySet("PKTABLE_CAT",            //
                "PKTABLE_SCHEM",                  //
                "PKTABLE_NAME",                   //
                "PKCOLUMN_NAME",                  //
                "FKTABLE_CAT",                    //
                "FKTABLE_SCHEM",                  //
                "FKTABLE_NAME",                   //
                "FKCOLUMN_NAME",                  //
                "KEY_SEQ", JDBCType.SMALLINT,     //
                "UPDATE_RULE", JDBCType.SMALLINT, //
                "DELETE_RULE", JDBCType.SMALLINT, //
                "FK_NAME",                        //
                "PKCOLUMN_NAME",                  //
                "PK_NAME",                        //
                "DEFERRABILITY", JDBCType.SMALLINT//
        );
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return emptySet("PKTABLE_CAT",            //
                "PKTABLE_SCHEM",                  //
                "PKTABLE_NAME",                   //
                "PKCOLUMN_NAME",                  //
                "FKTABLE_CAT",                    //
                "FKTABLE_SCHEM",                  //
                "FKTABLE_NAME",                   //
                "FKCOLUMN_NAME",                  //
                "KEY_SEQ", JDBCType.SMALLINT,     //
                "UPDATE_RULE", JDBCType.SMALLINT, //
                "DELETE_RULE", JDBCType.SMALLINT, //
                "FK_NAME",                        //
                "PK_NAME",                        //
                "DEFERRABILITY", JDBCType.SMALLINT//
        );
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema,
                                       String foreignTable) throws SQLException {
        return emptySet("PKTABLE_CAT",            //
                "PKTABLE_SCHEM",                  //
                "PKTABLE_NAME",                   //
                "PKCOLUMN_NAME",                  //
                "FKTABLE_CAT",                    //
                "FKTABLE_SCHEM",                  //
                "FKTABLE_NAME",                   //
                "FKCOLUMN_NAME",                  //
                "KEY_SEQ", JDBCType.SMALLINT,     //
                "UPDATE_RULE", JDBCType.SMALLINT, //
                "DELETE_RULE", JDBCType.SMALLINT, //
                "FK_NAME",                        //
                "PK_NAME",                        //
                "DEFERRABILITY", JDBCType.SMALLINT//
        );
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        return emptySet("TYPE_NAME",                   //
                "DATA_TYPE", JDBCType.INTEGER,         //
                "PRECISION", JDBCType.INTEGER,         //
                "LITERAL_PREFIX",                      //
                "LITERAL_SUFFIX",                      //
                "CREATE_PARAMS",                       //
                "NULLABLE", JDBCType.SMALLINT,         //
                "CASE_SENSITIVE", JDBCType.BOOLEAN,    //
                "SEARCHABLE", JDBCType.SMALLINT,       //
                "UNSIGNED_ATTRIBUTE", JDBCType.BOOLEAN,//
                "FIXED_PREC_SCALE", JDBCType.BOOLEAN,  //
                "AUTO_INCREMENT", JDBCType.BOOLEAN,    //
                "LOCAL_TYPE_NAME",                     //
                "MINIMUM_SCALE", JDBCType.SMALLINT,    //
                "MAXIMUM_SCALE", JDBCType.SMALLINT,    //
                "SQL_DATA_TYPE", JDBCType.INTEGER,     //
                "SQL_DATETIME_SUB", JDBCType.INTEGER,  //
                "NUM_PREC_RADIX", JDBCType.INTEGER     //
        );
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        return emptySet("TABLE_CAT",                  //
                "TABLE_SCHEM",                        //
                "TABLE_NAME",                         //
                "NON_UNIQUE", JDBCType.BOOLEAN,       //
                "INDEX_QUALIFIER",                    //
                "INDEX_NAME", JDBCType.INTEGER,       //
                "TYPE", JDBCType.SMALLINT,            //
                "ORDINAL_POSITION", JDBCType.SMALLINT,//
                "COLUMN_NAME",                        //
                "ASC_OR_DESC",                        //
                "CARDINALITY", JDBCType.BIGINT,       //
                "PAGES", JDBCType.BIGINT,             //
                "FILTER_CONDITION"                    //
        );
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        return emptySet("TYPE_CAT",            //
                "TYPE_SCHEM",                  //
                "TYPE_NAME",                   //
                "CLASS_NAME",                  //
                "DATA_TYPE", JDBCType.INTEGER, //
                "REMARKS",                     //
                "BASE_TYPE", JDBCType.SMALLINT //
        );
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        return emptySet("TYPE_CAT",   //
                "TYPE_SCHEM",         //
                "TYPE_NAME",          //
                "SUPERTYPE_CAT",      //
                "SUPERTYPE_SCHEM",    //
                "SUPERTYPE_NAME"      //
        );
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return emptySet("TABLE_CAT",  //
                "TABLE_SCHEM",        //
                "TABLE_NAME",         //
                "SUPERTABLE_NAME"     //
        );
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        return emptySet("TYPE_CAT",                   //
                "TYPE_SCHEM",                         //
                "TYPE_NAME",                          //
                "ATTR_NAME",                          //
                "DATA_TYPE", JDBCType.INTEGER,        //
                "ATTR_TYPE_NAME",                     //
                "ATTR_SIZE", JDBCType.INTEGER,        //
                "DECIMAL_DIGITS", JDBCType.INTEGER,   //
                "NUM_PREC_RADIX", JDBCType.INTEGER,   //
                "NULLABLE", JDBCType.INTEGER,         //
                "REMARKS",                            //
                "ATTR_DEF",                           //
                "SQL_DATA_TYPE", JDBCType.INTEGER,    //
                "SQL_DATETIME_SUB", JDBCType.INTEGER, //
                "CHAR_OCTET_LENGTH", JDBCType.INTEGER,//
                "ORDINAL_POSITION", JDBCType.INTEGER, //
                "IS_NULLABLE",                        //
                "SCOPE_CATALOG",                      //
                "SCOPE_SCHEMA",                       //
                "SCOPE_TABLE",                        //
                "SOURCE_DATA_TYPE", JDBCType.SMALLINT //
        );
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        return emptySet("NAME",             //
                "MAX_LEN", JDBCType.INTEGER,//
                "DEFAULT_VALUE",            //
                "DESCRIPTION"               //
        );
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return emptySet("TABLE_CAT",                  //
                "TABLE_SCHEM",                        //
                "TABLE_NAME",                         //
                "COLUMN_NAME",                        //
                "DATA_TYPE", JDBCType.INTEGER,        //
                "COLUMN_SIZE", JDBCType.INTEGER,      //
                "DECIMAL_DIGITS", JDBCType.INTEGER,   //
                "NUM_PREC_RADIX", JDBCType.INTEGER,   //
                "COLUMN_USAGE",                       //
                "REMARKS",                            //
                "CHAR_OCTET_LENGTH", JDBCType.INTEGER,//
                "IS_NULLABLE"                         //
        );
    }

    private ResultSet emptySet(Object... cols) throws SQLException {
        this.jdbcConnection.checkOpen();
        JdbcStatement s = new JdbcStatement(this.jdbcConnection);
        return new JdbcResultSet(s, new AdapterMemoryCursor(columnInfo(cols), null));
    }

    private static List<JdbcColumn> columnInfo(Object... cols) {
        List<JdbcColumn> columns = new ArrayList<>();

        for (int i = 0; i < cols.length; i++) {
            Object obj = cols[i];
            if (obj instanceof String) {
                String name = obj.toString();
                String type = JDBCType.VARCHAR.name();
                if (i + 1 < cols.length) {
                    Object next = cols[i + 1];
                    if (next instanceof JDBCType) {
                        type = ((JDBCType) next).name();
                        i++;
                    }
                }

                columns.add(new JdbcColumn(name, type, "ROUTINES", "INFORMATION_SCHEMA", ""));
            }
        }
        return columns;
    }
}
