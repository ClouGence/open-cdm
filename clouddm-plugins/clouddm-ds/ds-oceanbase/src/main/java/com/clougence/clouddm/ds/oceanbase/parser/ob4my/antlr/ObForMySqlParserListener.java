// Generated from /Users/zyc/Documents/dm-project/clougence-dm/clouddm-plugins/clouddm-ds/ds-oceanbase/src/main/antlr/ObForMySqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ObForMySqlParser}.
 */
public interface ObForMySqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(ObForMySqlParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(ObForMySqlParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(ObForMySqlParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(ObForMySqlParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(ObForMySqlParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(ObForMySqlParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(ObForMySqlParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(ObForMySqlParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabaseOption(ObForMySqlParser.CreateDatabaseOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabaseOption(ObForMySqlParser.CreateDatabaseOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEngine(ObForMySqlParser.TableOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEngine(ObForMySqlParser.TableOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAutoIncrement(ObForMySqlParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAutoIncrement(ObForMySqlParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAverage(ObForMySqlParser.TableOptionAverageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAverage(ObForMySqlParser.TableOptionAverageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCharset(ObForMySqlParser.TableOptionCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCharset(ObForMySqlParser.TableOptionCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionChecksum(ObForMySqlParser.TableOptionChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionChecksum(ObForMySqlParser.TableOptionChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCollate(ObForMySqlParser.TableOptionCollateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCollate(ObForMySqlParser.TableOptionCollateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionComment(ObForMySqlParser.TableOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionComment(ObForMySqlParser.TableOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCompression(ObForMySqlParser.TableOptionCompressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCompression(ObForMySqlParser.TableOptionCompressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionConnection(ObForMySqlParser.TableOptionConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionConnection(ObForMySqlParser.TableOptionConnectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDataDirectory(ObForMySqlParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDataDirectory(ObForMySqlParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDelay(ObForMySqlParser.TableOptionDelayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDelay(ObForMySqlParser.TableOptionDelayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEncryption(ObForMySqlParser.TableOptionEncryptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEncryption(ObForMySqlParser.TableOptionEncryptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionIndexDirectory(ObForMySqlParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionIndexDirectory(ObForMySqlParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionInsertMethod(ObForMySqlParser.TableOptionInsertMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionInsertMethod(ObForMySqlParser.TableOptionInsertMethodContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionKeyBlockSize(ObForMySqlParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionKeyBlockSize(ObForMySqlParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMaxRows(ObForMySqlParser.TableOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMaxRows(ObForMySqlParser.TableOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMinRows(ObForMySqlParser.TableOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMinRows(ObForMySqlParser.TableOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPackKeys(ObForMySqlParser.TableOptionPackKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPackKeys(ObForMySqlParser.TableOptionPackKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPassword(ObForMySqlParser.TableOptionPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPassword(ObForMySqlParser.TableOptionPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRowFormat(ObForMySqlParser.TableOptionRowFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRowFormat(ObForMySqlParser.TableOptionRowFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRecalculation(ObForMySqlParser.TableOptionRecalculationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRecalculation(ObForMySqlParser.TableOptionRecalculationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPersistent(ObForMySqlParser.TableOptionPersistentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPersistent(ObForMySqlParser.TableOptionPersistentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionSamplePage(ObForMySqlParser.TableOptionSamplePageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionSamplePage(ObForMySqlParser.TableOptionSamplePageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTablespace(ObForMySqlParser.TableOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTablespace(ObForMySqlParser.TableOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTableType(ObForMySqlParser.TableOptionTableTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTableType(ObForMySqlParser.TableOptionTableTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTransactional(ObForMySqlParser.TableOptionTransactionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTransactional(ObForMySqlParser.TableOptionTransactionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionUnion(ObForMySqlParser.TableOptionUnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionUnion(ObForMySqlParser.TableOptionUnionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code obMyTableOption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterObMyTableOption(ObForMySqlParser.ObMyTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code obMyTableOption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitObMyTableOption(ObForMySqlParser.ObMyTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ObForMySqlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ObForMySqlParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatements(ObForMySqlParser.SqlStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatements(ObForMySqlParser.SqlStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(ObForMySqlParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(ObForMySqlParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(ObForMySqlParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(ObForMySqlParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdlStatement(ObForMySqlParser.DdlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdlStatement(ObForMySqlParser.DdlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(ObForMySqlParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(ObForMySqlParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransactionStatement(ObForMySqlParser.TransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransactionStatement(ObForMySqlParser.TransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplicationStatement(ObForMySqlParser.ReplicationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplicationStatement(ObForMySqlParser.ReplicationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void enterPreparedStatement(ObForMySqlParser.PreparedStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void exitPreparedStatement(ObForMySqlParser.PreparedStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(ObForMySqlParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(ObForMySqlParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void enterAdministrationStatement(ObForMySqlParser.AdministrationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void exitAdministrationStatement(ObForMySqlParser.AdministrationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void enterUtilityStatement(ObForMySqlParser.UtilityStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void exitUtilityStatement(ObForMySqlParser.UtilityStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(ObForMySqlParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(ObForMySqlParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void enterDatabaseName(ObForMySqlParser.DatabaseNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void exitDatabaseName(ObForMySqlParser.DatabaseNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(ObForMySqlParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(ObForMySqlParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(ObForMySqlParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(ObForMySqlParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexName}.
	 * @param ctx the parse tree
	 */
	void enterIndexName(ObForMySqlParser.IndexNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexName}.
	 * @param ctx the parse tree
	 */
	void exitIndexName(ObForMySqlParser.IndexNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateLogfileGroup(ObForMySqlParser.CreateLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateLogfileGroup(ObForMySqlParser.CreateLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(ObForMySqlParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(ObForMySqlParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(ObForMySqlParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(ObForMySqlParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(ObForMySqlParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(ObForMySqlParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createServer}.
	 * @param ctx the parse tree
	 */
	void enterCreateServer(ObForMySqlParser.CreateServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createServer}.
	 * @param ctx the parse tree
	 */
	void exitCreateServer(ObForMySqlParser.CreateServerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCopyCreateTable(ObForMySqlParser.CopyCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCopyCreateTable(ObForMySqlParser.CopyCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterQueryCreateTable(ObForMySqlParser.QueryCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitQueryCreateTable(ObForMySqlParser.QueryCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterColumnCreateTable(ObForMySqlParser.ColumnCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitColumnCreateTable(ObForMySqlParser.ColumnCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_(ObForMySqlParser.Temporary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_(ObForMySqlParser.Temporary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceInnodb(ObForMySqlParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceInnodb(ObForMySqlParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceNdb(ObForMySqlParser.CreateTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceNdb(ObForMySqlParser.CreateTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateTrigger(ObForMySqlParser.CreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateTrigger(ObForMySqlParser.CreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createView}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(ObForMySqlParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createView}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(ObForMySqlParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void enterOwnerStatement(ObForMySqlParser.OwnerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void exitOwnerStatement(ObForMySqlParser.OwnerStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreciseSchedule(ObForMySqlParser.PreciseScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreciseSchedule(ObForMySqlParser.PreciseScheduleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalSchedule(ObForMySqlParser.IntervalScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalSchedule(ObForMySqlParser.IntervalScheduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(ObForMySqlParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(ObForMySqlParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpr(ObForMySqlParser.IntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpr(ObForMySqlParser.IntervalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void enterIntervalType(ObForMySqlParser.IntervalTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void exitIntervalType(ObForMySqlParser.IntervalTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#enableType}.
	 * @param ctx the parse tree
	 */
	void enterEnableType(ObForMySqlParser.EnableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#enableType}.
	 * @param ctx the parse tree
	 */
	void exitEnableType(ObForMySqlParser.EnableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexType}.
	 * @param ctx the parse tree
	 */
	void enterIndexType(ObForMySqlParser.IndexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexType}.
	 * @param ctx the parse tree
	 */
	void exitIndexType(ObForMySqlParser.IndexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParameter(ObForMySqlParser.ProcedureParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParameter(ObForMySqlParser.ProcedureParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParameter(ObForMySqlParser.FunctionParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParameter(ObForMySqlParser.FunctionParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineComment(ObForMySqlParser.RoutineCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineComment(ObForMySqlParser.RoutineCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineLanguage(ObForMySqlParser.RoutineLanguageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineLanguage(ObForMySqlParser.RoutineLanguageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBehavior(ObForMySqlParser.RoutineBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBehavior(ObForMySqlParser.RoutineBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineData(ObForMySqlParser.RoutineDataContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineData(ObForMySqlParser.RoutineDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineSecurity(ObForMySqlParser.RoutineSecurityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineSecurity(ObForMySqlParser.RoutineSecurityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void enterServerOption(ObForMySqlParser.ServerOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void exitServerOption(ObForMySqlParser.ServerOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterCreateDefinitions(ObForMySqlParser.CreateDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitCreateDefinitions(ObForMySqlParser.CreateDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDeclaration(ObForMySqlParser.ColumnDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDeclaration(ObForMySqlParser.ColumnDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDeclaration(ObForMySqlParser.ConstraintDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDeclaration(ObForMySqlParser.ConstraintDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterIndexDeclaration(ObForMySqlParser.IndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitIndexDeclaration(ObForMySqlParser.IndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(ObForMySqlParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(ObForMySqlParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterNullColumnConstraint(ObForMySqlParser.NullColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitNullColumnConstraint(ObForMySqlParser.NullColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterDefaultColumnConstraint(ObForMySqlParser.DefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitDefaultColumnConstraint(ObForMySqlParser.DefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterInvisibleColumnConstraint(ObForMySqlParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitInvisibleColumnConstraint(ObForMySqlParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterAutoIncrementColumnConstraint(ObForMySqlParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitAutoIncrementColumnConstraint(ObForMySqlParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyColumnConstraint(ObForMySqlParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyColumnConstraint(ObForMySqlParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyColumnConstraint(ObForMySqlParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyColumnConstraint(ObForMySqlParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumnConstraint(ObForMySqlParser.CommentColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumnConstraint(ObForMySqlParser.CommentColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterFormatColumnConstraint(ObForMySqlParser.FormatColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitFormatColumnConstraint(ObForMySqlParser.FormatColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterStorageColumnConstraint(ObForMySqlParser.StorageColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitStorageColumnConstraint(ObForMySqlParser.StorageColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterReferenceColumnConstraint(ObForMySqlParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitReferenceColumnConstraint(ObForMySqlParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCollateColumnConstraint(ObForMySqlParser.CollateColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCollateColumnConstraint(ObForMySqlParser.CollateColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterGeneratedColumnConstraint(ObForMySqlParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitGeneratedColumnConstraint(ObForMySqlParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterSerialDefaultColumnConstraint(ObForMySqlParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitSerialDefaultColumnConstraint(ObForMySqlParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckColumnConstraint(ObForMySqlParser.CheckColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckColumnConstraint(ObForMySqlParser.CheckColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyTableConstraint(ObForMySqlParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyTableConstraint(ObForMySqlParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyTableConstraint(ObForMySqlParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyTableConstraint(ObForMySqlParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyTableConstraint(ObForMySqlParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyTableConstraint(ObForMySqlParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableConstraint(ObForMySqlParser.CheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableConstraint(ObForMySqlParser.CheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDefinition(ObForMySqlParser.ReferenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDefinition(ObForMySqlParser.ReferenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAction(ObForMySqlParser.ReferenceActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAction(ObForMySqlParser.ReferenceActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceControlType(ObForMySqlParser.ReferenceControlTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceControlType(ObForMySqlParser.ReferenceControlTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIndexDeclaration(ObForMySqlParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIndexDeclaration(ObForMySqlParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSpecialIndexDeclaration(ObForMySqlParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSpecialIndexDeclaration(ObForMySqlParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tableType}.
	 * @param ctx the parse tree
	 */
	void enterTableType(ObForMySqlParser.TableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tableType}.
	 * @param ctx the parse tree
	 */
	void exitTableType(ObForMySqlParser.TableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void enterTablespaceStorage(ObForMySqlParser.TablespaceStorageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void exitTablespaceStorage(ObForMySqlParser.TablespaceStorageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitions(ObForMySqlParser.PartitionDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitions(ObForMySqlParser.PartitionDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionHash(ObForMySqlParser.PartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionHash(ObForMySqlParser.PartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionKey(ObForMySqlParser.PartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionKey(ObForMySqlParser.PartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionRange(ObForMySqlParser.PartitionFunctionRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionRange(ObForMySqlParser.PartitionFunctionRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionList(ObForMySqlParser.PartitionFunctionListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionList(ObForMySqlParser.PartitionFunctionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionHash(ObForMySqlParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionHash(ObForMySqlParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionKey(ObForMySqlParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionKey(ObForMySqlParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionComparision(ObForMySqlParser.PartitionComparisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionComparision(ObForMySqlParser.PartitionComparisionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListAtom(ObForMySqlParser.PartitionListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListAtom(ObForMySqlParser.PartitionListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListVector(ObForMySqlParser.PartitionListVectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListVector(ObForMySqlParser.PartitionListVectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSimple(ObForMySqlParser.PartitionSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSimple(ObForMySqlParser.PartitionSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerAtom(ObForMySqlParser.PartitionDefinerAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerAtom(ObForMySqlParser.PartitionDefinerAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerVector(ObForMySqlParser.PartitionDefinerVectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerVector(ObForMySqlParser.PartitionDefinerVectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubpartitionDefinition(ObForMySqlParser.SubpartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubpartitionDefinition(ObForMySqlParser.SubpartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionEngine(ObForMySqlParser.PartitionOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionEngine(ObForMySqlParser.PartitionOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionComment(ObForMySqlParser.PartitionOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionComment(ObForMySqlParser.PartitionOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionDataDirectory(ObForMySqlParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionDataDirectory(ObForMySqlParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionIndexDirectory(ObForMySqlParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionIndexDirectory(ObForMySqlParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMaxRows(ObForMySqlParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMaxRows(ObForMySqlParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMinRows(ObForMySqlParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMinRows(ObForMySqlParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionTablespace(ObForMySqlParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionTablespace(ObForMySqlParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionNodeGroup(ObForMySqlParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionNodeGroup(ObForMySqlParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterSimpleDatabase(ObForMySqlParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterSimpleDatabase(ObForMySqlParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterUpgradeName(ObForMySqlParser.AlterUpgradeNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterUpgradeName(ObForMySqlParser.AlterUpgradeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void enterAlterEvent(ObForMySqlParser.AlterEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void exitAlterEvent(ObForMySqlParser.AlterEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterFunction(ObForMySqlParser.AlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterFunction(ObForMySqlParser.AlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void enterAlterInstance(ObForMySqlParser.AlterInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void exitAlterInstance(ObForMySqlParser.AlterInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterLogfileGroup(ObForMySqlParser.AlterLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterLogfileGroup(ObForMySqlParser.AlterLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterAlterProcedure(ObForMySqlParser.AlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitAlterProcedure(ObForMySqlParser.AlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void enterAlterServer(ObForMySqlParser.AlterServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void exitAlterServer(ObForMySqlParser.AlterServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(ObForMySqlParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(ObForMySqlParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespace(ObForMySqlParser.AlterTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespace(ObForMySqlParser.AlterTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#alterView}.
	 * @param ctx the parse tree
	 */
	void enterAlterView(ObForMySqlParser.AlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#alterView}.
	 * @param ctx the parse tree
	 */
	void exitAlterView(ObForMySqlParser.AlterViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTableOption(ObForMySqlParser.AlterByTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTableOption(ObForMySqlParser.AlterByTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumn(ObForMySqlParser.AlterByAddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumn(ObForMySqlParser.AlterByAddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumns(ObForMySqlParser.AlterByAddColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumns(ObForMySqlParser.AlterByAddColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddIndex(ObForMySqlParser.AlterByAddIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddIndex(ObForMySqlParser.AlterByAddIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPrimaryKey(ObForMySqlParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPrimaryKey(ObForMySqlParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddUniqueKey(ObForMySqlParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddUniqueKey(ObForMySqlParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddSpecialIndex(ObForMySqlParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddSpecialIndex(ObForMySqlParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddForeignKey(ObForMySqlParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddForeignKey(ObForMySqlParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddCheckTableConstraint(ObForMySqlParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddCheckTableConstraint(ObForMySqlParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterBySetAlgorithm(ObForMySqlParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterBySetAlgorithm(ObForMySqlParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeDefault(ObForMySqlParser.AlterByChangeDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeDefault(ObForMySqlParser.AlterByChangeDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeColumn(ObForMySqlParser.AlterByChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeColumn(ObForMySqlParser.AlterByChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameColumn(ObForMySqlParser.AlterByRenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameColumn(ObForMySqlParser.AlterByRenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByLock(ObForMySqlParser.AlterByLockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByLock(ObForMySqlParser.AlterByLockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByModifyColumn(ObForMySqlParser.AlterByModifyColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByModifyColumn(ObForMySqlParser.AlterByModifyColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropColumn(ObForMySqlParser.AlterByDropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropColumn(ObForMySqlParser.AlterByDropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropConstraintCheck(ObForMySqlParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropConstraintCheck(ObForMySqlParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPrimaryKey(ObForMySqlParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPrimaryKey(ObForMySqlParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropIndex(ObForMySqlParser.AlterByDropIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropIndex(ObForMySqlParser.AlterByDropIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameIndex(ObForMySqlParser.AlterByRenameIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameIndex(ObForMySqlParser.AlterByRenameIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAlterIndexVisibility(ObForMySqlParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAlterIndexVisibility(ObForMySqlParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropForeignKey(ObForMySqlParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropForeignKey(ObForMySqlParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDisableKeys(ObForMySqlParser.AlterByDisableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDisableKeys(ObForMySqlParser.AlterByDisableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByEnableKeys(ObForMySqlParser.AlterByEnableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByEnableKeys(ObForMySqlParser.AlterByEnableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRename(ObForMySqlParser.AlterByRenameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRename(ObForMySqlParser.AlterByRenameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOrder(ObForMySqlParser.AlterByOrderContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOrder(ObForMySqlParser.AlterByOrderContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByConvertCharset(ObForMySqlParser.AlterByConvertCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByConvertCharset(ObForMySqlParser.AlterByConvertCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDefaultCharset(ObForMySqlParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDefaultCharset(ObForMySqlParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardTablespace(ObForMySqlParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardTablespace(ObForMySqlParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportTablespace(ObForMySqlParser.AlterByImportTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportTablespace(ObForMySqlParser.AlterByImportTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByForce(ObForMySqlParser.AlterByForceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByForce(ObForMySqlParser.AlterByForceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByValidate(ObForMySqlParser.AlterByValidateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByValidate(ObForMySqlParser.AlterByValidateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPartition(ObForMySqlParser.AlterByAddPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPartition(ObForMySqlParser.AlterByAddPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPartition(ObForMySqlParser.AlterByDropPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPartition(ObForMySqlParser.AlterByDropPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardPartition(ObForMySqlParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardPartition(ObForMySqlParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportPartition(ObForMySqlParser.AlterByImportPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportPartition(ObForMySqlParser.AlterByImportPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTruncatePartition(ObForMySqlParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTruncatePartition(ObForMySqlParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCoalescePartition(ObForMySqlParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCoalescePartition(ObForMySqlParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByReorganizePartition(ObForMySqlParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByReorganizePartition(ObForMySqlParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByExchangePartition(ObForMySqlParser.AlterByExchangePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByExchangePartition(ObForMySqlParser.AlterByExchangePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAnalyzePartition(ObForMySqlParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAnalyzePartition(ObForMySqlParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCheckPartition(ObForMySqlParser.AlterByCheckPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCheckPartition(ObForMySqlParser.AlterByCheckPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOptimizePartition(ObForMySqlParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOptimizePartition(ObForMySqlParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRebuildPartition(ObForMySqlParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRebuildPartition(ObForMySqlParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRepairPartition(ObForMySqlParser.AlterByRepairPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRepairPartition(ObForMySqlParser.AlterByRepairPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRemovePartitioning(ObForMySqlParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRemovePartitioning(ObForMySqlParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByUpgradePartitioning(ObForMySqlParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByUpgradePartitioning(ObForMySqlParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(ObForMySqlParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(ObForMySqlParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(ObForMySqlParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(ObForMySqlParser.DropEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(ObForMySqlParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(ObForMySqlParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropLogfileGroup(ObForMySqlParser.DropLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropLogfileGroup(ObForMySqlParser.DropLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(ObForMySqlParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(ObForMySqlParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(ObForMySqlParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(ObForMySqlParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void enterDropServer(ObForMySqlParser.DropServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void exitDropServer(ObForMySqlParser.DropServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void enterDropTablespace(ObForMySqlParser.DropTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void exitDropTablespace(ObForMySqlParser.DropTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(ObForMySqlParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(ObForMySqlParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(ObForMySqlParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(ObForMySqlParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(ObForMySqlParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(ObForMySqlParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameTableClause(ObForMySqlParser.RenameTableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameTableClause(ObForMySqlParser.RenameTableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(ObForMySqlParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(ObForMySqlParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallStatement(ObForMySqlParser.CallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallStatement(ObForMySqlParser.CallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void enterProcedureArgs(ObForMySqlParser.ProcedureArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void exitProcedureArgs(ObForMySqlParser.ProcedureArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(ObForMySqlParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(ObForMySqlParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(ObForMySqlParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(ObForMySqlParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(ObForMySqlParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(ObForMySqlParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadDataStatement(ObForMySqlParser.LoadDataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadDataStatement(ObForMySqlParser.LoadDataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadXmlStatement(ObForMySqlParser.LoadXmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadXmlStatement(ObForMySqlParser.LoadXmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplaceStatement(ObForMySqlParser.ReplaceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplaceStatement(ObForMySqlParser.ReplaceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelect(ObForMySqlParser.SimpleSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelect(ObForMySqlParser.SimpleSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisSelect(ObForMySqlParser.ParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisSelect(ObForMySqlParser.ParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelect(ObForMySqlParser.UnionSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelect(ObForMySqlParser.UnionSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesisSelect(ObForMySqlParser.UnionParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesisSelect(ObForMySqlParser.UnionParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(ObForMySqlParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(ObForMySqlParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectStatement(ObForMySqlParser.WithSelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectStatement(ObForMySqlParser.WithSelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#withClause}.
	 * @param ctx the parse tree
	 */
	void enterWithClause(ObForMySqlParser.WithClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#withClause}.
	 * @param ctx the parse tree
	 */
	void exitWithClause(ObForMySqlParser.WithClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectExpr(ObForMySqlParser.WithSelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectExpr(ObForMySqlParser.WithSelectExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterSelectInsertValue(ObForMySqlParser.SelectInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitSelectInsertValue(ObForMySqlParser.SelectInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterCommentInsertValue(ObForMySqlParser.CommentInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitCommentInsertValue(ObForMySqlParser.CommentInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void enterUpdatedElement(ObForMySqlParser.UpdatedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void exitUpdatedElement(ObForMySqlParser.UpdatedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentField(ObForMySqlParser.AssignmentFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentField(ObForMySqlParser.AssignmentFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void enterLockClause(ObForMySqlParser.LockClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void exitLockClause(ObForMySqlParser.LockClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleDeleteStatement(ObForMySqlParser.SingleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleDeleteStatement(ObForMySqlParser.SingleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleDeleteStatement(ObForMySqlParser.MultipleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleDeleteStatement(ObForMySqlParser.MultipleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerOpenStatement(ObForMySqlParser.HandlerOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerOpenStatement(ObForMySqlParser.HandlerOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadIndexStatement(ObForMySqlParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadIndexStatement(ObForMySqlParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadStatement(ObForMySqlParser.HandlerReadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadStatement(ObForMySqlParser.HandlerReadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerCloseStatement(ObForMySqlParser.HandlerCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerCloseStatement(ObForMySqlParser.HandlerCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleUpdateStatement(ObForMySqlParser.SingleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleUpdateStatement(ObForMySqlParser.SingleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void enterIgnore_(ObForMySqlParser.Ignore_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void exitIgnore_(ObForMySqlParser.Ignore_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleUpdateStatement(ObForMySqlParser.MultipleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleUpdateStatement(ObForMySqlParser.MultipleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(ObForMySqlParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(ObForMySqlParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(ObForMySqlParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(ObForMySqlParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void enterTableSources(ObForMySqlParser.TableSourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void exitTableSources(ObForMySqlParser.TableSourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceBase(ObForMySqlParser.TableSourceBaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceBase(ObForMySqlParser.TableSourceBaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceNested(ObForMySqlParser.TableSourceNestedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceNested(ObForMySqlParser.TableSourceNestedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterAtomTableItem(ObForMySqlParser.AtomTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitAtomTableItem(ObForMySqlParser.AtomTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryTableItem(ObForMySqlParser.SubqueryTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryTableItem(ObForMySqlParser.SubqueryTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourcesItem(ObForMySqlParser.TableSourcesItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourcesItem(ObForMySqlParser.TableSourcesItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceWith(ObForMySqlParser.TableSourceWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceWith(ObForMySqlParser.TableSourceWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void enterIndexHint(ObForMySqlParser.IndexHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void exitIndexHint(ObForMySqlParser.IndexHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void enterIndexHintType(ObForMySqlParser.IndexHintTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void exitIndexHintType(ObForMySqlParser.IndexHintTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoin(ObForMySqlParser.InnerJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoin(ObForMySqlParser.InnerJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterStraightJoin(ObForMySqlParser.StraightJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitStraightJoin(ObForMySqlParser.StraightJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoin(ObForMySqlParser.OuterJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoin(ObForMySqlParser.OuterJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoin(ObForMySqlParser.NaturalJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoin(ObForMySqlParser.NaturalJoinContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoinType(ObForMySqlParser.InnerJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoinType(ObForMySqlParser.InnerJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoinType(ObForMySqlParser.OuterJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoinType(ObForMySqlParser.OuterJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoinType(ObForMySqlParser.NaturalJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoinType(ObForMySqlParser.NaturalJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpression(ObForMySqlParser.QueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpression(ObForMySqlParser.QueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionNointo(ObForMySqlParser.QueryExpressionNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionNointo(ObForMySqlParser.QueryExpressionNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(ObForMySqlParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(ObForMySqlParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecificationNointo(ObForMySqlParser.QuerySpecificationNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecificationNointo(ObForMySqlParser.QuerySpecificationNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesis(ObForMySqlParser.UnionParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesis(ObForMySqlParser.UnionParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionStatement(ObForMySqlParser.UnionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionStatement(ObForMySqlParser.UnionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void enterSelectElements(ObForMySqlParser.SelectElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void exitSelectElements(ObForMySqlParser.SelectElementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStarElement(ObForMySqlParser.SelectStarElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStarElement(ObForMySqlParser.SelectStarElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnElement(ObForMySqlParser.SelectColumnElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnElement(ObForMySqlParser.SelectColumnElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectFunctionElement(ObForMySqlParser.SelectFunctionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectFunctionElement(ObForMySqlParser.SelectFunctionElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpressionElement(ObForMySqlParser.SelectExpressionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpressionElement(ObForMySqlParser.SelectExpressionElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void enterAliasName(ObForMySqlParser.AliasNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void exitAliasName(ObForMySqlParser.AliasNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoVariables(ObForMySqlParser.SelectIntoVariablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoVariables(ObForMySqlParser.SelectIntoVariablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoDumpFile(ObForMySqlParser.SelectIntoDumpFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoDumpFile(ObForMySqlParser.SelectIntoDumpFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoTextFile(ObForMySqlParser.SelectIntoTextFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoTextFile(ObForMySqlParser.SelectIntoTextFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectFieldsInto(ObForMySqlParser.SelectFieldsIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectFieldsInto(ObForMySqlParser.SelectFieldsIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectLinesInto(ObForMySqlParser.SelectLinesIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectLinesInto(ObForMySqlParser.SelectLinesIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(ObForMySqlParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(ObForMySqlParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(ObForMySqlParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(ObForMySqlParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupClause(ObForMySqlParser.GroupClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupClause(ObForMySqlParser.GroupClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(ObForMySqlParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(ObForMySqlParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(ObForMySqlParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(ObForMySqlParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(ObForMySqlParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(ObForMySqlParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void enterLimitClauseAtom(ObForMySqlParser.LimitClauseAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void exitLimitClauseAtom(ObForMySqlParser.LimitClauseAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(ObForMySqlParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(ObForMySqlParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void enterBeginWork(ObForMySqlParser.BeginWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void exitBeginWork(ObForMySqlParser.BeginWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void enterCommitWork(ObForMySqlParser.CommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void exitCommitWork(ObForMySqlParser.CommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterRollbackWork(ObForMySqlParser.RollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitRollbackWork(ObForMySqlParser.RollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void enterSavepointStatement(ObForMySqlParser.SavepointStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void exitSavepointStatement(ObForMySqlParser.SavepointStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void enterRollbackStatement(ObForMySqlParser.RollbackStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void exitRollbackStatement(ObForMySqlParser.RollbackStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterReleaseStatement(ObForMySqlParser.ReleaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitReleaseStatement(ObForMySqlParser.ReleaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void enterLockTables(ObForMySqlParser.LockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void exitLockTables(ObForMySqlParser.LockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void enterUnlockTables(ObForMySqlParser.UnlockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void exitUnlockTables(ObForMySqlParser.UnlockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommitStatement(ObForMySqlParser.SetAutocommitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommitStatement(ObForMySqlParser.SetAutocommitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransactionStatement(ObForMySqlParser.SetTransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransactionStatement(ObForMySqlParser.SetTransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionMode(ObForMySqlParser.TransactionModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionMode(ObForMySqlParser.TransactionModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void enterLockTableElement(ObForMySqlParser.LockTableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void exitLockTableElement(ObForMySqlParser.LockTableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void enterLockAction(ObForMySqlParser.LockActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void exitLockAction(ObForMySqlParser.LockActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void enterTransactionOption(ObForMySqlParser.TransactionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void exitTransactionOption(ObForMySqlParser.TransactionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevel(ObForMySqlParser.TransactionLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevel(ObForMySqlParser.TransactionLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void enterChangeMaster(ObForMySqlParser.ChangeMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void exitChangeMaster(ObForMySqlParser.ChangeMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterChangeReplicationFilter(ObForMySqlParser.ChangeReplicationFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitChangeReplicationFilter(ObForMySqlParser.ChangeReplicationFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void enterPurgeBinaryLogs(ObForMySqlParser.PurgeBinaryLogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void exitPurgeBinaryLogs(ObForMySqlParser.PurgeBinaryLogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void enterResetMaster(ObForMySqlParser.ResetMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void exitResetMaster(ObForMySqlParser.ResetMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void enterResetSlave(ObForMySqlParser.ResetSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void exitResetSlave(ObForMySqlParser.ResetSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void enterResetReplica(ObForMySqlParser.ResetReplicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void exitResetReplica(ObForMySqlParser.ResetReplicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void enterStartSlave(ObForMySqlParser.StartSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void exitStartSlave(ObForMySqlParser.StartSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void enterStopSlave(ObForMySqlParser.StopSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void exitStopSlave(ObForMySqlParser.StopSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStartGroupReplication(ObForMySqlParser.StartGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStartGroupReplication(ObForMySqlParser.StartGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStopGroupReplication(ObForMySqlParser.StopGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStopGroupReplication(ObForMySqlParser.StopGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterStringOption(ObForMySqlParser.MasterStringOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterStringOption(ObForMySqlParser.MasterStringOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterDecimalOption(ObForMySqlParser.MasterDecimalOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterDecimalOption(ObForMySqlParser.MasterDecimalOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterBoolOption(ObForMySqlParser.MasterBoolOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterBoolOption(ObForMySqlParser.MasterBoolOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterRealOption(ObForMySqlParser.MasterRealOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterRealOption(ObForMySqlParser.MasterRealOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterUidListOption(ObForMySqlParser.MasterUidListOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterUidListOption(ObForMySqlParser.MasterUidListOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterStringMasterOption(ObForMySqlParser.StringMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitStringMasterOption(ObForMySqlParser.StringMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterDecimalMasterOption(ObForMySqlParser.DecimalMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitDecimalMasterOption(ObForMySqlParser.DecimalMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterBoolMasterOption(ObForMySqlParser.BoolMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitBoolMasterOption(ObForMySqlParser.BoolMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelOption(ObForMySqlParser.ChannelOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelOption(ObForMySqlParser.ChannelOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoDbReplication(ObForMySqlParser.DoDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoDbReplication(ObForMySqlParser.DoDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreDbReplication(ObForMySqlParser.IgnoreDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreDbReplication(ObForMySqlParser.IgnoreDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoTableReplication(ObForMySqlParser.DoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoTableReplication(ObForMySqlParser.DoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreTableReplication(ObForMySqlParser.IgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreTableReplication(ObForMySqlParser.IgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildDoTableReplication(ObForMySqlParser.WildDoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildDoTableReplication(ObForMySqlParser.WildDoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildIgnoreTableReplication(ObForMySqlParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildIgnoreTableReplication(ObForMySqlParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterRewriteDbReplication(ObForMySqlParser.RewriteDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitRewriteDbReplication(ObForMySqlParser.RewriteDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void enterTablePair(ObForMySqlParser.TablePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void exitTablePair(ObForMySqlParser.TablePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#threadType}.
	 * @param ctx the parse tree
	 */
	void enterThreadType(ObForMySqlParser.ThreadTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#threadType}.
	 * @param ctx the parse tree
	 */
	void exitThreadType(ObForMySqlParser.ThreadTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterGtidsUntilOption(ObForMySqlParser.GtidsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitGtidsUntilOption(ObForMySqlParser.GtidsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterLogUntilOption(ObForMySqlParser.MasterLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterLogUntilOption(ObForMySqlParser.MasterLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterRelayLogUntilOption(ObForMySqlParser.RelayLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitRelayLogUntilOption(ObForMySqlParser.RelayLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterSqlGapsUntilOption(ObForMySqlParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitSqlGapsUntilOption(ObForMySqlParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterUserConnectionOption(ObForMySqlParser.UserConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitUserConnectionOption(ObForMySqlParser.UserConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPasswordConnectionOption(ObForMySqlParser.PasswordConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPasswordConnectionOption(ObForMySqlParser.PasswordConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterDefaultAuthConnectionOption(ObForMySqlParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitDefaultAuthConnectionOption(ObForMySqlParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPluginDirConnectionOption(ObForMySqlParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPluginDirConnectionOption(ObForMySqlParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void enterGtuidSet(ObForMySqlParser.GtuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void exitGtuidSet(ObForMySqlParser.GtuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaStartTransaction(ObForMySqlParser.XaStartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaStartTransaction(ObForMySqlParser.XaStartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaEndTransaction(ObForMySqlParser.XaEndTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaEndTransaction(ObForMySqlParser.XaEndTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterXaPrepareStatement(ObForMySqlParser.XaPrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitXaPrepareStatement(ObForMySqlParser.XaPrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void enterXaCommitWork(ObForMySqlParser.XaCommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void exitXaCommitWork(ObForMySqlParser.XaCommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRollbackWork(ObForMySqlParser.XaRollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRollbackWork(ObForMySqlParser.XaRollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRecoverWork(ObForMySqlParser.XaRecoverWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRecoverWork(ObForMySqlParser.XaRecoverWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrepareStatement(ObForMySqlParser.PrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrepareStatement(ObForMySqlParser.PrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatement(ObForMySqlParser.ExecuteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatement(ObForMySqlParser.ExecuteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatePrepare(ObForMySqlParser.DeallocatePrepareContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatePrepare(ObForMySqlParser.DeallocatePrepareContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBody(ObForMySqlParser.RoutineBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBody(ObForMySqlParser.RoutineBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(ObForMySqlParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(ObForMySqlParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(ObForMySqlParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(ObForMySqlParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(ObForMySqlParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(ObForMySqlParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterateStatement(ObForMySqlParser.IterateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterateStatement(ObForMySqlParser.IterateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(ObForMySqlParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(ObForMySqlParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(ObForMySqlParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(ObForMySqlParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(ObForMySqlParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(ObForMySqlParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(ObForMySqlParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(ObForMySqlParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(ObForMySqlParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(ObForMySqlParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCloseCursor(ObForMySqlParser.CloseCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCloseCursor(ObForMySqlParser.CloseCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterFetchCursor(ObForMySqlParser.FetchCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitFetchCursor(ObForMySqlParser.FetchCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterOpenCursor(ObForMySqlParser.OpenCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitOpenCursor(ObForMySqlParser.OpenCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVariable(ObForMySqlParser.DeclareVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVariable(ObForMySqlParser.DeclareVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCondition(ObForMySqlParser.DeclareConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCondition(ObForMySqlParser.DeclareConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCursor(ObForMySqlParser.DeclareCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCursor(ObForMySqlParser.DeclareCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void enterDeclareHandler(ObForMySqlParser.DeclareHandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void exitDeclareHandler(ObForMySqlParser.DeclareHandlerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionCode(ObForMySqlParser.HandlerConditionCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionCode(ObForMySqlParser.HandlerConditionCodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionState(ObForMySqlParser.HandlerConditionStateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionState(ObForMySqlParser.HandlerConditionStateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionName(ObForMySqlParser.HandlerConditionNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionName(ObForMySqlParser.HandlerConditionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionWarning(ObForMySqlParser.HandlerConditionWarningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionWarning(ObForMySqlParser.HandlerConditionWarningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionNotfound(ObForMySqlParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionNotfound(ObForMySqlParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionException(ObForMySqlParser.HandlerConditionExceptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionException(ObForMySqlParser.HandlerConditionExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterProcedureSqlStatement(ObForMySqlParser.ProcedureSqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitProcedureSqlStatement(ObForMySqlParser.ProcedureSqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseAlternative(ObForMySqlParser.CaseAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseAlternative(ObForMySqlParser.CaseAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void enterElifAlternative(ObForMySqlParser.ElifAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void exitElifAlternative(ObForMySqlParser.ElifAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV56(ObForMySqlParser.AlterUserMysqlV56Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV56(ObForMySqlParser.AlterUserMysqlV56Context ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV57(ObForMySqlParser.AlterUserMysqlV57Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV57(ObForMySqlParser.AlterUserMysqlV57Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUser(ObForMySqlParser.CreateUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUser(ObForMySqlParser.CreateUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(ObForMySqlParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(ObForMySqlParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(ObForMySqlParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(ObForMySqlParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void enterGrantStatement(ObForMySqlParser.GrantStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void exitGrantStatement(ObForMySqlParser.GrantStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void enterRoleOption(ObForMySqlParser.RoleOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void exitRoleOption(ObForMySqlParser.RoleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void enterGrantProxy(ObForMySqlParser.GrantProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void exitGrantProxy(ObForMySqlParser.GrantProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(ObForMySqlParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(ObForMySqlParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterRevokeStatement(ObForMySqlParser.RevokeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitRevokeStatement(ObForMySqlParser.RevokeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void enterRevokeProxy(ObForMySqlParser.RevokeProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void exitRevokeProxy(ObForMySqlParser.RevokeProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPasswordStatement(ObForMySqlParser.SetPasswordStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPasswordStatement(ObForMySqlParser.SetPasswordStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void enterUserSpecification(ObForMySqlParser.UserSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void exitUserSpecification(ObForMySqlParser.UserSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterHashAuthOption(ObForMySqlParser.HashAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitHashAuthOption(ObForMySqlParser.HashAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterStringAuthOption(ObForMySqlParser.StringAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitStringAuthOption(ObForMySqlParser.StringAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterModuleAuthOption(ObForMySqlParser.ModuleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitModuleAuthOption(ObForMySqlParser.ModuleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAuthOption(ObForMySqlParser.SimpleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAuthOption(ObForMySqlParser.SimpleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code module}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterModule(ObForMySqlParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code module}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitModule(ObForMySqlParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterPasswordModuleOption(ObForMySqlParser.PasswordModuleOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitPasswordModuleOption(ObForMySqlParser.PasswordModuleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void enterTlsOption(ObForMySqlParser.TlsOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void exitTlsOption(ObForMySqlParser.TlsOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void enterUserResourceOption(ObForMySqlParser.UserResourceOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void exitUserResourceOption(ObForMySqlParser.UserResourceOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void enterUserPasswordOption(ObForMySqlParser.UserPasswordOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void exitUserPasswordOption(ObForMySqlParser.UserPasswordOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void enterUserLockOption(ObForMySqlParser.UserLockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void exitUserLockOption(ObForMySqlParser.UserLockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void enterPrivelegeClause(ObForMySqlParser.PrivelegeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void exitPrivelegeClause(ObForMySqlParser.PrivelegeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(ObForMySqlParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(ObForMySqlParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterCurrentSchemaPriviLevel(ObForMySqlParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitCurrentSchemaPriviLevel(ObForMySqlParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterGlobalPrivLevel(ObForMySqlParser.GlobalPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitGlobalPrivLevel(ObForMySqlParser.GlobalPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteSchemaPrivLevel(ObForMySqlParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteSchemaPrivLevel(ObForMySqlParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel(ObForMySqlParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel(ObForMySqlParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel2(ObForMySqlParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel2(ObForMySqlParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteTablePrivLevel(ObForMySqlParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteTablePrivLevel(ObForMySqlParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameUserClause(ObForMySqlParser.RenameUserClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameUserClause(ObForMySqlParser.RenameUserClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzeTable(ObForMySqlParser.AnalyzeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzeTable(ObForMySqlParser.AnalyzeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void enterCheckTable(ObForMySqlParser.CheckTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void exitCheckTable(ObForMySqlParser.CheckTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void enterChecksumTable(ObForMySqlParser.ChecksumTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void exitChecksumTable(ObForMySqlParser.ChecksumTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void enterOptimizeTable(ObForMySqlParser.OptimizeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void exitOptimizeTable(ObForMySqlParser.OptimizeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(ObForMySqlParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(ObForMySqlParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableOption(ObForMySqlParser.CheckTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableOption(ObForMySqlParser.CheckTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateUdfFunction(ObForMySqlParser.CreateUdfFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateUdfFunction(ObForMySqlParser.CreateUdfFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void enterInstallPlugin(ObForMySqlParser.InstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void exitInstallPlugin(ObForMySqlParser.InstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void enterUninstallPlugin(ObForMySqlParser.UninstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void exitUninstallPlugin(ObForMySqlParser.UninstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetVariable(ObForMySqlParser.SetVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetVariable(ObForMySqlParser.SetVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetCharset(ObForMySqlParser.SetCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetCharset(ObForMySqlParser.SetCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNames(ObForMySqlParser.SetNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNames(ObForMySqlParser.SetNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPassword(ObForMySqlParser.SetPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPassword(ObForMySqlParser.SetPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransaction(ObForMySqlParser.SetTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransaction(ObForMySqlParser.SetTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommit(ObForMySqlParser.SetAutocommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommit(ObForMySqlParser.SetAutocommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNewValueInsideTrigger(ObForMySqlParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNewValueInsideTrigger(ObForMySqlParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowMasterLogs(ObForMySqlParser.ShowMasterLogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowMasterLogs(ObForMySqlParser.ShowMasterLogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCharset(ObForMySqlParser.ShowCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCharset(ObForMySqlParser.ShowCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowLogEvents(ObForMySqlParser.ShowLogEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowLogEvents(ObForMySqlParser.ShowLogEventsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowObjectFilter(ObForMySqlParser.ShowObjectFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowObjectFilter(ObForMySqlParser.ShowObjectFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(ObForMySqlParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(ObForMySqlParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(ObForMySqlParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(ObForMySqlParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateDb(ObForMySqlParser.ShowCreateDbContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateDb(ObForMySqlParser.ShowCreateDbContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateFullIdObject(ObForMySqlParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateFullIdObject(ObForMySqlParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateUser(ObForMySqlParser.ShowCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateUser(ObForMySqlParser.ShowCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngine(ObForMySqlParser.ShowEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngine(ObForMySqlParser.ShowEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngines(ObForMySqlParser.ShowEnginesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngines(ObForMySqlParser.ShowEnginesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatus(ObForMySqlParser.ShowStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatus(ObForMySqlParser.ShowStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPlugins(ObForMySqlParser.ShowPluginsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPlugins(ObForMySqlParser.ShowPluginsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPrivileges(ObForMySqlParser.ShowPrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPrivileges(ObForMySqlParser.ShowPrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProcessList(ObForMySqlParser.ShowProcessListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProcessList(ObForMySqlParser.ShowProcessListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfiles(ObForMySqlParser.ShowProfilesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfiles(ObForMySqlParser.ShowProfilesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveHosts(ObForMySqlParser.ShowSlaveHostsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveHosts(ObForMySqlParser.ShowSlaveHostsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowAuthros(ObForMySqlParser.ShowAuthrosContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowAuthros(ObForMySqlParser.ShowAuthrosContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowContributors(ObForMySqlParser.ShowContributorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowContributors(ObForMySqlParser.ShowContributorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowErrors(ObForMySqlParser.ShowErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowErrors(ObForMySqlParser.ShowErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCountErrors(ObForMySqlParser.ShowCountErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCountErrors(ObForMySqlParser.ShowCountErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaFilter(ObForMySqlParser.ShowSchemaFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaFilter(ObForMySqlParser.ShowSchemaFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoutine(ObForMySqlParser.ShowRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoutine(ObForMySqlParser.ShowRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(ObForMySqlParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(ObForMySqlParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowIndexes(ObForMySqlParser.ShowIndexesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowIndexes(ObForMySqlParser.ShowIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowOpenTables(ObForMySqlParser.ShowOpenTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowOpenTables(ObForMySqlParser.ShowOpenTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfile(ObForMySqlParser.ShowProfileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfile(ObForMySqlParser.ShowProfileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveStatus(ObForMySqlParser.ShowSlaveStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveStatus(ObForMySqlParser.ShowSlaveStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowReplicaStatus(ObForMySqlParser.ShowReplicaStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowReplicaStatus(ObForMySqlParser.ShowReplicaStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void enterVariableClause(ObForMySqlParser.VariableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void exitVariableClause(ObForMySqlParser.VariableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowCommonEntity(ObForMySqlParser.ShowCommonEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowCommonEntity(ObForMySqlParser.ShowCommonEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void enterShowFilter(ObForMySqlParser.ShowFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void exitShowFilter(ObForMySqlParser.ShowFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void enterShowGlobalInfoClause(ObForMySqlParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void exitShowGlobalInfoClause(ObForMySqlParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaEntity(ObForMySqlParser.ShowSchemaEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaEntity(ObForMySqlParser.ShowSchemaEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void enterShowProfileType(ObForMySqlParser.ShowProfileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void exitShowProfileType(ObForMySqlParser.ShowProfileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void enterBinlogStatement(ObForMySqlParser.BinlogStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void exitBinlogStatement(ObForMySqlParser.BinlogStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterCacheIndexStatement(ObForMySqlParser.CacheIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitCacheIndexStatement(ObForMySqlParser.CacheIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(ObForMySqlParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(ObForMySqlParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void enterKillStatement(ObForMySqlParser.KillStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void exitKillStatement(ObForMySqlParser.KillStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void enterLoadIndexIntoCache(ObForMySqlParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void exitLoadIndexIntoCache(ObForMySqlParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void enterResetStatement(ObForMySqlParser.ResetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void exitResetStatement(ObForMySqlParser.ResetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void enterShutdownStatement(ObForMySqlParser.ShutdownStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void exitShutdownStatement(ObForMySqlParser.ShutdownStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterTableIndexes(ObForMySqlParser.TableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitTableIndexes(ObForMySqlParser.TableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFlushOption(ObForMySqlParser.SimpleFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFlushOption(ObForMySqlParser.SimpleFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelFlushOption(ObForMySqlParser.ChannelFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelFlushOption(ObForMySqlParser.ChannelFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterTableFlushOption(ObForMySqlParser.TableFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitTableFlushOption(ObForMySqlParser.TableFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void enterFlushTableOption(ObForMySqlParser.FlushTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void exitFlushTableOption(ObForMySqlParser.FlushTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterLoadedTableIndexes(ObForMySqlParser.LoadedTableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitLoadedTableIndexes(ObForMySqlParser.LoadedTableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDescribeStatement(ObForMySqlParser.SimpleDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDescribeStatement(ObForMySqlParser.SimpleDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterFullDescribeStatement(ObForMySqlParser.FullDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitFullDescribeStatement(ObForMySqlParser.FullDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void enterHelpStatement(ObForMySqlParser.HelpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void exitHelpStatement(ObForMySqlParser.HelpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void enterUseStatement(ObForMySqlParser.UseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void exitUseStatement(ObForMySqlParser.UseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void enterSignalStatement(ObForMySqlParser.SignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void exitSignalStatement(ObForMySqlParser.SignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void enterResignalStatement(ObForMySqlParser.ResignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void exitResignalStatement(ObForMySqlParser.ResignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void enterSignalConditionInformation(ObForMySqlParser.SignalConditionInformationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void exitSignalConditionInformation(ObForMySqlParser.SignalConditionInformationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsStatement(ObForMySqlParser.DiagnosticsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsStatement(ObForMySqlParser.DiagnosticsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsConditionInformationName(ObForMySqlParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsConditionInformationName(ObForMySqlParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeStatements(ObForMySqlParser.DescribeStatementsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeStatements(ObForMySqlParser.DescribeStatementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeConnection(ObForMySqlParser.DescribeConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeConnection(ObForMySqlParser.DescribeConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#fullId}.
	 * @param ctx the parse tree
	 */
	void enterFullId(ObForMySqlParser.FullIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#fullId}.
	 * @param ctx the parse tree
	 */
	void exitFullId(ObForMySqlParser.FullIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(ObForMySqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(ObForMySqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#procName}.
	 * @param ctx the parse tree
	 */
	void enterProcName(ObForMySqlParser.ProcNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#procName}.
	 * @param ctx the parse tree
	 */
	void exitProcName(ObForMySqlParser.ProcNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterCustomFunctionName(ObForMySqlParser.CustomFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitCustomFunctionName(ObForMySqlParser.CustomFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#roleName}.
	 * @param ctx the parse tree
	 */
	void enterRoleName(ObForMySqlParser.RoleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#roleName}.
	 * @param ctx the parse tree
	 */
	void exitRoleName(ObForMySqlParser.RoleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(ObForMySqlParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(ObForMySqlParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnName(ObForMySqlParser.IndexColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnName(ObForMySqlParser.IndexColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userName}.
	 * @param ctx the parse tree
	 */
	void enterUserName(ObForMySqlParser.UserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userName}.
	 * @param ctx the parse tree
	 */
	void exitUserName(ObForMySqlParser.UserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariable(ObForMySqlParser.MysqlVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariable(ObForMySqlParser.MysqlVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void enterCharsetName(ObForMySqlParser.CharsetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void exitCharsetName(ObForMySqlParser.CharsetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#collationName}.
	 * @param ctx the parse tree
	 */
	void enterCollationName(ObForMySqlParser.CollationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#collationName}.
	 * @param ctx the parse tree
	 */
	void exitCollationName(ObForMySqlParser.CollationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#engineName}.
	 * @param ctx the parse tree
	 */
	void enterEngineName(ObForMySqlParser.EngineNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#engineName}.
	 * @param ctx the parse tree
	 */
	void exitEngineName(ObForMySqlParser.EngineNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void enterUuidSet(ObForMySqlParser.UuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void exitUuidSet(ObForMySqlParser.UuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xid}.
	 * @param ctx the parse tree
	 */
	void enterXid(ObForMySqlParser.XidContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xid}.
	 * @param ctx the parse tree
	 */
	void exitXid(ObForMySqlParser.XidContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void enterXuidStringId(ObForMySqlParser.XuidStringIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void exitXuidStringId(ObForMySqlParser.XuidStringIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void enterAuthPlugin(ObForMySqlParser.AuthPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void exitAuthPlugin(ObForMySqlParser.AuthPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#uid}.
	 * @param ctx the parse tree
	 */
	void enterUid(ObForMySqlParser.UidContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#uid}.
	 * @param ctx the parse tree
	 */
	void exitUid(ObForMySqlParser.UidContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleId(ObForMySqlParser.SimpleIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleId(ObForMySqlParser.SimpleIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void enterDottedId(ObForMySqlParser.DottedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void exitDottedId(ObForMySqlParser.DottedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(ObForMySqlParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(ObForMySqlParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFileSizeLiteral(ObForMySqlParser.FileSizeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFileSizeLiteral(ObForMySqlParser.FileSizeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(ObForMySqlParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(ObForMySqlParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(ObForMySqlParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(ObForMySqlParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(ObForMySqlParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(ObForMySqlParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void enterNullNotnull(ObForMySqlParser.NullNotnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void exitNullNotnull(ObForMySqlParser.NullNotnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(ObForMySqlParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(ObForMySqlParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterStringDataType(ObForMySqlParser.StringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitStringDataType(ObForMySqlParser.StringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalStringDataType(ObForMySqlParser.NationalStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalStringDataType(ObForMySqlParser.NationalStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalVaryingStringDataType(ObForMySqlParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalVaryingStringDataType(ObForMySqlParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDimensionDataType(ObForMySqlParser.DimensionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDimensionDataType(ObForMySqlParser.DimensionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDataType(ObForMySqlParser.SimpleDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDataType(ObForMySqlParser.SimpleDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterCollectionDataType(ObForMySqlParser.CollectionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitCollectionDataType(ObForMySqlParser.CollectionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSpatialDataType(ObForMySqlParser.SpatialDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSpatialDataType(ObForMySqlParser.SpatialDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarcharDataType(ObForMySqlParser.LongVarcharDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarcharDataType(ObForMySqlParser.LongVarcharDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarbinaryDataType(ObForMySqlParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarbinaryDataType(ObForMySqlParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOptions(ObForMySqlParser.CollectionOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOptions(ObForMySqlParser.CollectionOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOption(ObForMySqlParser.CollectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOption(ObForMySqlParser.CollectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void enterConvertedDataType(ObForMySqlParser.ConvertedDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void exitConvertedDataType(ObForMySqlParser.ConvertedDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthOneDimension(ObForMySqlParser.LengthOneDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthOneDimension(ObForMySqlParser.LengthOneDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoDimension(ObForMySqlParser.LengthTwoDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoDimension(ObForMySqlParser.LengthTwoDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoOptionalDimension(ObForMySqlParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoOptionalDimension(ObForMySqlParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#uidList}.
	 * @param ctx the parse tree
	 */
	void enterUidList(ObForMySqlParser.UidListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#uidList}.
	 * @param ctx the parse tree
	 */
	void exitUidList(ObForMySqlParser.UidListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void enterTables(ObForMySqlParser.TablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void exitTables(ObForMySqlParser.TablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnNames(ObForMySqlParser.IndexColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnNames(ObForMySqlParser.IndexColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(ObForMySqlParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(ObForMySqlParser.ExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void enterExpressionsWithDefaults(ObForMySqlParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void exitExpressionsWithDefaults(ObForMySqlParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#constants}.
	 * @param ctx the parse tree
	 */
	void enterConstants(ObForMySqlParser.ConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#constants}.
	 * @param ctx the parse tree
	 */
	void exitConstants(ObForMySqlParser.ConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStrings(ObForMySqlParser.SimpleStringsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStrings(ObForMySqlParser.SimpleStringsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void enterUserVariables(ObForMySqlParser.UserVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void exitUserVariables(ObForMySqlParser.UserVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(ObForMySqlParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(ObForMySqlParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestamp(ObForMySqlParser.CurrentTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestamp(ObForMySqlParser.CurrentTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterExpressionOrDefault(ObForMySqlParser.ExpressionOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitExpressionOrDefault(ObForMySqlParser.ExpressionOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void enterIfExists(ObForMySqlParser.IfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void exitIfExists(ObForMySqlParser.IfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void enterIfNotExists(ObForMySqlParser.IfNotExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void exitIfNotExists(ObForMySqlParser.IfNotExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterSpecificFunctionCall(ObForMySqlParser.SpecificFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitSpecificFunctionCall(ObForMySqlParser.SpecificFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunctionCall(ObForMySqlParser.AggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunctionCall(ObForMySqlParser.AggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionCall(ObForMySqlParser.ScalarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionCall(ObForMySqlParser.ScalarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterUdfFunctionCall(ObForMySqlParser.UdfFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitUdfFunctionCall(ObForMySqlParser.UdfFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionCall(ObForMySqlParser.PasswordFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionCall(ObForMySqlParser.PasswordFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunctionCall(ObForMySqlParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunctionCall(ObForMySqlParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunction(ObForMySqlParser.NonAggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunction(ObForMySqlParser.NonAggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(ObForMySqlParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(ObForMySqlParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void enterWindow_specification(ObForMySqlParser.Window_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void exitWindow_specification(ObForMySqlParser.Window_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause(ObForMySqlParser.Frame_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause(ObForMySqlParser.Frame_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void enterFrame_extent(ObForMySqlParser.Frame_extentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void exitFrame_extent(ObForMySqlParser.Frame_extentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void enterFrame_start_or_end(ObForMySqlParser.Frame_start_or_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void exitFrame_start_or_end(ObForMySqlParser.Frame_start_or_endContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFunctionCall(ObForMySqlParser.SimpleFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFunctionCall(ObForMySqlParser.SimpleFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSpecialTimeCall(ObForMySqlParser.SpecialTimeCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSpecialTimeCall(ObForMySqlParser.SpecialTimeCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeFunctionCall(ObForMySqlParser.DataTypeFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeFunctionCall(ObForMySqlParser.DataTypeFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterValuesFunctionCall(ObForMySqlParser.ValuesFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitValuesFunctionCall(ObForMySqlParser.ValuesFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCaseFunctionCall(ObForMySqlParser.CaseFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCaseFunctionCall(ObForMySqlParser.CaseFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCharFunctionCall(ObForMySqlParser.CharFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCharFunctionCall(ObForMySqlParser.CharFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterPositionFunctionCall(ObForMySqlParser.PositionFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitPositionFunctionCall(ObForMySqlParser.PositionFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSubstrFunctionCall(ObForMySqlParser.SubstrFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSubstrFunctionCall(ObForMySqlParser.SubstrFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterTrimFunctionCall(ObForMySqlParser.TrimFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitTrimFunctionCall(ObForMySqlParser.TrimFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterWeightFunctionCall(ObForMySqlParser.WeightFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitWeightFunctionCall(ObForMySqlParser.WeightFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterExtractFunctionCall(ObForMySqlParser.ExtractFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitExtractFunctionCall(ObForMySqlParser.ExtractFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetFormatFunctionCall(ObForMySqlParser.GetFormatFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetFormatFunctionCall(ObForMySqlParser.GetFormatFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueFunctionCall(ObForMySqlParser.JsonValueFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueFunctionCall(ObForMySqlParser.JsonValueFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseFuncAlternative(ObForMySqlParser.CaseFuncAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseFuncAlternative(ObForMySqlParser.CaseFuncAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightList(ObForMySqlParser.LevelWeightListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightList(ObForMySqlParser.LevelWeightListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightRange(ObForMySqlParser.LevelWeightRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightRange(ObForMySqlParser.LevelWeightRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void enterLevelInWeightListElement(ObForMySqlParser.LevelInWeightListElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void exitLevelInWeightListElement(ObForMySqlParser.LevelInWeightListElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunction(ObForMySqlParser.AggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunction(ObForMySqlParser.AggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionName(ObForMySqlParser.ScalarFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionName(ObForMySqlParser.ScalarFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionClause(ObForMySqlParser.PasswordFunctionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionClause(ObForMySqlParser.PasswordFunctionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgs(ObForMySqlParser.FunctionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgs(ObForMySqlParser.FunctionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(ObForMySqlParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(ObForMySqlParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(ObForMySqlParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(ObForMySqlParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(ObForMySqlParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(ObForMySqlParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(ObForMySqlParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(ObForMySqlParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(ObForMySqlParser.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(ObForMySqlParser.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSoundsLikePredicate(ObForMySqlParser.SoundsLikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSoundsLikePredicate(ObForMySqlParser.SoundsLikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAtomPredicate(ObForMySqlParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAtomPredicate(ObForMySqlParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterJsonMemberOfPredicate(ObForMySqlParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitJsonMemberOfPredicate(ObForMySqlParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(ObForMySqlParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(ObForMySqlParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryComparasionPredicate(ObForMySqlParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryComparasionPredicate(ObForMySqlParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterFullSearchPredicate(ObForMySqlParser.FullSearchPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitFullSearchPredicate(ObForMySqlParser.FullSearchPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetweenPredicate(ObForMySqlParser.BetweenPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetweenPredicate(ObForMySqlParser.BetweenPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBinaryComparasionPredicate(ObForMySqlParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBinaryComparasionPredicate(ObForMySqlParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(ObForMySqlParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(ObForMySqlParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(ObForMySqlParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(ObForMySqlParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterRegexpPredicate(ObForMySqlParser.RegexpPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitRegexpPredicate(ObForMySqlParser.RegexpPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void enterSearch_modifier(ObForMySqlParser.Search_modifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void exitSearch_modifier(ObForMySqlParser.Search_modifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionAtom(ObForMySqlParser.UnaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionAtom(ObForMySqlParser.UnaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterCollateExpressionAtom(ObForMySqlParser.CollateExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitCollateExpressionAtom(ObForMySqlParser.CollateExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpessionAtom(ObForMySqlParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpessionAtom(ObForMySqlParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariableExpressionAtom(ObForMySqlParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariableExpressionAtom(ObForMySqlParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpressionAtom(ObForMySqlParser.NestedExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpressionAtom(ObForMySqlParser.NestedExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedRowExpressionAtom(ObForMySqlParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedRowExpressionAtom(ObForMySqlParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMathExpressionAtom(ObForMySqlParser.MathExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMathExpressionAtom(ObForMySqlParser.MathExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpressionAtom(ObForMySqlParser.IntervalExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpressionAtom(ObForMySqlParser.IntervalExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterJsonExpressionAtom(ObForMySqlParser.JsonExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitJsonExpressionAtom(ObForMySqlParser.JsonExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterExistsExpessionAtom(ObForMySqlParser.ExistsExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitExistsExpessionAtom(ObForMySqlParser.ExistsExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(ObForMySqlParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(ObForMySqlParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(ObForMySqlParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(ObForMySqlParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpressionAtom(ObForMySqlParser.BinaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpressionAtom(ObForMySqlParser.BinaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(ObForMySqlParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(ObForMySqlParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBitExpressionAtom(ObForMySqlParser.BitExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBitExpressionAtom(ObForMySqlParser.BitExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(ObForMySqlParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(ObForMySqlParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(ObForMySqlParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(ObForMySqlParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(ObForMySqlParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(ObForMySqlParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitOperator(ObForMySqlParser.BitOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitOperator(ObForMySqlParser.BitOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void enterMathOperator(ObForMySqlParser.MathOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void exitMathOperator(ObForMySqlParser.MathOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void enterJsonOperator(ObForMySqlParser.JsonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void exitJsonOperator(ObForMySqlParser.JsonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void enterCharsetNameBase(ObForMySqlParser.CharsetNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void exitCharsetNameBase(ObForMySqlParser.CharsetNameBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevelBase(ObForMySqlParser.TransactionLevelBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevelBase(ObForMySqlParser.TransactionLevelBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void enterPrivilegesBase(ObForMySqlParser.PrivilegesBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void exitPrivilegesBase(ObForMySqlParser.PrivilegesBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterIntervalTypeBase(ObForMySqlParser.IntervalTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitIntervalTypeBase(ObForMySqlParser.IntervalTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeBase(ObForMySqlParser.DataTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeBase(ObForMySqlParser.DataTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(ObForMySqlParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(ObForMySqlParser.KeywordsCanBeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForMySqlParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNameBase(ObForMySqlParser.FunctionNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForMySqlParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNameBase(ObForMySqlParser.FunctionNameBaseContext ctx);
}