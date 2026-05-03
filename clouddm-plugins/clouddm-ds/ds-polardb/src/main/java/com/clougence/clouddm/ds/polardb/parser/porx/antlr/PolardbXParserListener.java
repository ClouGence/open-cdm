// Generated from PolardbXParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.polardb.parser.porx.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PolardbXParser}.
 */
public interface PolardbXParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(PolardbXParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(PolardbXParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(PolardbXParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(PolardbXParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitions(PolardbXParser.PartitionDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitions(PolardbXParser.PartitionDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#polardbXAlterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterPolardbXAlterSpecification(PolardbXParser.PolardbXAlterSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#polardbXAlterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitPolardbXAlterSpecification(PolardbXParser.PolardbXAlterSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#polardbxPartitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPolardbxPartitionOption(PolardbXParser.PolardbxPartitionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#polardbxPartitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPolardbxPartitionOption(PolardbXParser.PolardbxPartitionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dbShardingAlgorithm}.
	 * @param ctx the parse tree
	 */
	void enterDbShardingAlgorithm(PolardbXParser.DbShardingAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dbShardingAlgorithm}.
	 * @param ctx the parse tree
	 */
	void exitDbShardingAlgorithm(PolardbXParser.DbShardingAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEngine(PolardbXParser.TableOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEngine(PolardbXParser.TableOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAutoIncrement(PolardbXParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAutoIncrement(PolardbXParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAverage(PolardbXParser.TableOptionAverageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAverage(PolardbXParser.TableOptionAverageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCharset(PolardbXParser.TableOptionCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCharset(PolardbXParser.TableOptionCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionChecksum(PolardbXParser.TableOptionChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionChecksum(PolardbXParser.TableOptionChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCollate(PolardbXParser.TableOptionCollateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCollate(PolardbXParser.TableOptionCollateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionComment(PolardbXParser.TableOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionComment(PolardbXParser.TableOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCompression(PolardbXParser.TableOptionCompressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCompression(PolardbXParser.TableOptionCompressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionConnection(PolardbXParser.TableOptionConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionConnection(PolardbXParser.TableOptionConnectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDataDirectory(PolardbXParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDataDirectory(PolardbXParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDelay(PolardbXParser.TableOptionDelayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDelay(PolardbXParser.TableOptionDelayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEncryption(PolardbXParser.TableOptionEncryptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEncryption(PolardbXParser.TableOptionEncryptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionIndexDirectory(PolardbXParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionIndexDirectory(PolardbXParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionInsertMethod(PolardbXParser.TableOptionInsertMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionInsertMethod(PolardbXParser.TableOptionInsertMethodContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionKeyBlockSize(PolardbXParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionKeyBlockSize(PolardbXParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMaxRows(PolardbXParser.TableOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMaxRows(PolardbXParser.TableOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMinRows(PolardbXParser.TableOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMinRows(PolardbXParser.TableOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPackKeys(PolardbXParser.TableOptionPackKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPackKeys(PolardbXParser.TableOptionPackKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPassword(PolardbXParser.TableOptionPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPassword(PolardbXParser.TableOptionPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRowFormat(PolardbXParser.TableOptionRowFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRowFormat(PolardbXParser.TableOptionRowFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRecalculation(PolardbXParser.TableOptionRecalculationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRecalculation(PolardbXParser.TableOptionRecalculationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPersistent(PolardbXParser.TableOptionPersistentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPersistent(PolardbXParser.TableOptionPersistentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionSamplePage(PolardbXParser.TableOptionSamplePageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionSamplePage(PolardbXParser.TableOptionSamplePageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTablespace(PolardbXParser.TableOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTablespace(PolardbXParser.TableOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTableType(PolardbXParser.TableOptionTableTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTableType(PolardbXParser.TableOptionTableTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTransactional(PolardbXParser.TableOptionTransactionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTransactional(PolardbXParser.TableOptionTransactionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionUnion(PolardbXParser.TableOptionUnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionUnion(PolardbXParser.TableOptionUnionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableBroadcastOption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableBroadcastOption(PolardbXParser.TableBroadcastOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableBroadcastOption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableBroadcastOption(PolardbXParser.TableBroadcastOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableLocalityOption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableLocalityOption(PolardbXParser.TableLocalityOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableLocalityOption}
	 * labeled alternative in {@link PolardbXParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableLocalityOption(PolardbXParser.TableLocalityOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIndexDeclaration(PolardbXParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIndexDeclaration(PolardbXParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSpecialIndexDeclaration(PolardbXParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSpecialIndexDeclaration(PolardbXParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code polardbXPartitionGlobalIndex}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPolardbXPartitionGlobalIndex(PolardbXParser.PolardbXPartitionGlobalIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code polardbXPartitionGlobalIndex}
	 * labeled alternative in {@link PolardbXParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPolardbXPartitionGlobalIndex(PolardbXParser.PolardbXPartitionGlobalIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabaseOption(PolardbXParser.CreateDatabaseOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabaseOption(PolardbXParser.CreateDatabaseOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(PolardbXParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(PolardbXParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatements(PolardbXParser.SqlStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatements(PolardbXParser.SqlStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(PolardbXParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(PolardbXParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(PolardbXParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(PolardbXParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdlStatement(PolardbXParser.DdlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdlStatement(PolardbXParser.DdlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(PolardbXParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(PolardbXParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransactionStatement(PolardbXParser.TransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransactionStatement(PolardbXParser.TransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplicationStatement(PolardbXParser.ReplicationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplicationStatement(PolardbXParser.ReplicationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void enterPreparedStatement(PolardbXParser.PreparedStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void exitPreparedStatement(PolardbXParser.PreparedStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(PolardbXParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(PolardbXParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void enterAdministrationStatement(PolardbXParser.AdministrationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void exitAdministrationStatement(PolardbXParser.AdministrationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void enterUtilityStatement(PolardbXParser.UtilityStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void exitUtilityStatement(PolardbXParser.UtilityStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(PolardbXParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(PolardbXParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void enterDatabaseName(PolardbXParser.DatabaseNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void exitDatabaseName(PolardbXParser.DatabaseNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(PolardbXParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(PolardbXParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexName}.
	 * @param ctx the parse tree
	 */
	void enterIndexName(PolardbXParser.IndexNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexName}.
	 * @param ctx the parse tree
	 */
	void exitIndexName(PolardbXParser.IndexNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateLogfileGroup(PolardbXParser.CreateLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateLogfileGroup(PolardbXParser.CreateLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(PolardbXParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(PolardbXParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(PolardbXParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(PolardbXParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(PolardbXParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(PolardbXParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createServer}.
	 * @param ctx the parse tree
	 */
	void enterCreateServer(PolardbXParser.CreateServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createServer}.
	 * @param ctx the parse tree
	 */
	void exitCreateServer(PolardbXParser.CreateServerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCopyCreateTable(PolardbXParser.CopyCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCopyCreateTable(PolardbXParser.CopyCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterQueryCreateTable(PolardbXParser.QueryCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitQueryCreateTable(PolardbXParser.QueryCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterColumnCreateTable(PolardbXParser.ColumnCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link PolardbXParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitColumnCreateTable(PolardbXParser.ColumnCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_(PolardbXParser.Temporary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_(PolardbXParser.Temporary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceInnodb(PolardbXParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceInnodb(PolardbXParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceNdb(PolardbXParser.CreateTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceNdb(PolardbXParser.CreateTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateTrigger(PolardbXParser.CreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateTrigger(PolardbXParser.CreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createView}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(PolardbXParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createView}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(PolardbXParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void enterOwnerStatement(PolardbXParser.OwnerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void exitOwnerStatement(PolardbXParser.OwnerStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link PolardbXParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreciseSchedule(PolardbXParser.PreciseScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link PolardbXParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreciseSchedule(PolardbXParser.PreciseScheduleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link PolardbXParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalSchedule(PolardbXParser.IntervalScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link PolardbXParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalSchedule(PolardbXParser.IntervalScheduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(PolardbXParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(PolardbXParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpr(PolardbXParser.IntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpr(PolardbXParser.IntervalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void enterIntervalType(PolardbXParser.IntervalTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void exitIntervalType(PolardbXParser.IntervalTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#enableType}.
	 * @param ctx the parse tree
	 */
	void enterEnableType(PolardbXParser.EnableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#enableType}.
	 * @param ctx the parse tree
	 */
	void exitEnableType(PolardbXParser.EnableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexType}.
	 * @param ctx the parse tree
	 */
	void enterIndexType(PolardbXParser.IndexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexType}.
	 * @param ctx the parse tree
	 */
	void exitIndexType(PolardbXParser.IndexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(PolardbXParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(PolardbXParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParameter(PolardbXParser.ProcedureParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParameter(PolardbXParser.ProcedureParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParameter(PolardbXParser.FunctionParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParameter(PolardbXParser.FunctionParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineComment(PolardbXParser.RoutineCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineComment(PolardbXParser.RoutineCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineLanguage(PolardbXParser.RoutineLanguageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineLanguage(PolardbXParser.RoutineLanguageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBehavior(PolardbXParser.RoutineBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBehavior(PolardbXParser.RoutineBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineData(PolardbXParser.RoutineDataContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineData(PolardbXParser.RoutineDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineSecurity(PolardbXParser.RoutineSecurityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link PolardbXParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineSecurity(PolardbXParser.RoutineSecurityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void enterServerOption(PolardbXParser.ServerOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void exitServerOption(PolardbXParser.ServerOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterCreateDefinitions(PolardbXParser.CreateDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitCreateDefinitions(PolardbXParser.CreateDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDeclaration(PolardbXParser.ColumnDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDeclaration(PolardbXParser.ColumnDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDeclaration(PolardbXParser.ConstraintDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDeclaration(PolardbXParser.ConstraintDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterIndexDeclaration(PolardbXParser.IndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link PolardbXParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitIndexDeclaration(PolardbXParser.IndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(PolardbXParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(PolardbXParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterNullColumnConstraint(PolardbXParser.NullColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitNullColumnConstraint(PolardbXParser.NullColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterDefaultColumnConstraint(PolardbXParser.DefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitDefaultColumnConstraint(PolardbXParser.DefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterInvisibleColumnConstraint(PolardbXParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitInvisibleColumnConstraint(PolardbXParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterAutoIncrementColumnConstraint(PolardbXParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitAutoIncrementColumnConstraint(PolardbXParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyColumnConstraint(PolardbXParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyColumnConstraint(PolardbXParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyColumnConstraint(PolardbXParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyColumnConstraint(PolardbXParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumnConstraint(PolardbXParser.CommentColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumnConstraint(PolardbXParser.CommentColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterFormatColumnConstraint(PolardbXParser.FormatColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitFormatColumnConstraint(PolardbXParser.FormatColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterStorageColumnConstraint(PolardbXParser.StorageColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitStorageColumnConstraint(PolardbXParser.StorageColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterReferenceColumnConstraint(PolardbXParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitReferenceColumnConstraint(PolardbXParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCollateColumnConstraint(PolardbXParser.CollateColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCollateColumnConstraint(PolardbXParser.CollateColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterGeneratedColumnConstraint(PolardbXParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitGeneratedColumnConstraint(PolardbXParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterSerialDefaultColumnConstraint(PolardbXParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitSerialDefaultColumnConstraint(PolardbXParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckColumnConstraint(PolardbXParser.CheckColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link PolardbXParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckColumnConstraint(PolardbXParser.CheckColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyTableConstraint(PolardbXParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyTableConstraint(PolardbXParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyTableConstraint(PolardbXParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyTableConstraint(PolardbXParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyTableConstraint(PolardbXParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyTableConstraint(PolardbXParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableConstraint(PolardbXParser.CheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link PolardbXParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableConstraint(PolardbXParser.CheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDefinition(PolardbXParser.ReferenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDefinition(PolardbXParser.ReferenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAction(PolardbXParser.ReferenceActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAction(PolardbXParser.ReferenceActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceControlType(PolardbXParser.ReferenceControlTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceControlType(PolardbXParser.ReferenceControlTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tableType}.
	 * @param ctx the parse tree
	 */
	void enterTableType(PolardbXParser.TableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tableType}.
	 * @param ctx the parse tree
	 */
	void exitTableType(PolardbXParser.TableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void enterTablespaceStorage(PolardbXParser.TablespaceStorageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void exitTablespaceStorage(PolardbXParser.TablespaceStorageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionHash(PolardbXParser.PartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionHash(PolardbXParser.PartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionKey(PolardbXParser.PartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionKey(PolardbXParser.PartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionRange(PolardbXParser.PartitionFunctionRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionRange(PolardbXParser.PartitionFunctionRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionList(PolardbXParser.PartitionFunctionListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link PolardbXParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionList(PolardbXParser.PartitionFunctionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link PolardbXParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionHash(PolardbXParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link PolardbXParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionHash(PolardbXParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link PolardbXParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionKey(PolardbXParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link PolardbXParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionKey(PolardbXParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionComparision(PolardbXParser.PartitionComparisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionComparision(PolardbXParser.PartitionComparisionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListAtom(PolardbXParser.PartitionListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListAtom(PolardbXParser.PartitionListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListVector(PolardbXParser.PartitionListVectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListVector(PolardbXParser.PartitionListVectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSimple(PolardbXParser.PartitionSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link PolardbXParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSimple(PolardbXParser.PartitionSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerAtom(PolardbXParser.PartitionDefinerAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerAtom(PolardbXParser.PartitionDefinerAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerVector(PolardbXParser.PartitionDefinerVectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerVector(PolardbXParser.PartitionDefinerVectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubpartitionDefinition(PolardbXParser.SubpartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubpartitionDefinition(PolardbXParser.SubpartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionEngine(PolardbXParser.PartitionOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionEngine(PolardbXParser.PartitionOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionComment(PolardbXParser.PartitionOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionComment(PolardbXParser.PartitionOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionDataDirectory(PolardbXParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionDataDirectory(PolardbXParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionIndexDirectory(PolardbXParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionIndexDirectory(PolardbXParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMaxRows(PolardbXParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMaxRows(PolardbXParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMinRows(PolardbXParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMinRows(PolardbXParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionTablespace(PolardbXParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionTablespace(PolardbXParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionNodeGroup(PolardbXParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link PolardbXParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionNodeGroup(PolardbXParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link PolardbXParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterSimpleDatabase(PolardbXParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link PolardbXParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterSimpleDatabase(PolardbXParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link PolardbXParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterUpgradeName(PolardbXParser.AlterUpgradeNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link PolardbXParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterUpgradeName(PolardbXParser.AlterUpgradeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void enterAlterEvent(PolardbXParser.AlterEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void exitAlterEvent(PolardbXParser.AlterEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterFunction(PolardbXParser.AlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterFunction(PolardbXParser.AlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void enterAlterInstance(PolardbXParser.AlterInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void exitAlterInstance(PolardbXParser.AlterInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterLogfileGroup(PolardbXParser.AlterLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterLogfileGroup(PolardbXParser.AlterLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterAlterProcedure(PolardbXParser.AlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitAlterProcedure(PolardbXParser.AlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void enterAlterServer(PolardbXParser.AlterServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void exitAlterServer(PolardbXParser.AlterServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespace(PolardbXParser.AlterTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespace(PolardbXParser.AlterTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#alterView}.
	 * @param ctx the parse tree
	 */
	void enterAlterView(PolardbXParser.AlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#alterView}.
	 * @param ctx the parse tree
	 */
	void exitAlterView(PolardbXParser.AlterViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTableOption(PolardbXParser.AlterByTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTableOption(PolardbXParser.AlterByTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumn(PolardbXParser.AlterByAddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumn(PolardbXParser.AlterByAddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumns(PolardbXParser.AlterByAddColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumns(PolardbXParser.AlterByAddColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddIndex(PolardbXParser.AlterByAddIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddIndex(PolardbXParser.AlterByAddIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPrimaryKey(PolardbXParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPrimaryKey(PolardbXParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddUniqueKey(PolardbXParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddUniqueKey(PolardbXParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddSpecialIndex(PolardbXParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddSpecialIndex(PolardbXParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddForeignKey(PolardbXParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddForeignKey(PolardbXParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddCheckTableConstraint(PolardbXParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddCheckTableConstraint(PolardbXParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterBySetAlgorithm(PolardbXParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterBySetAlgorithm(PolardbXParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeDefault(PolardbXParser.AlterByChangeDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeDefault(PolardbXParser.AlterByChangeDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeColumn(PolardbXParser.AlterByChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeColumn(PolardbXParser.AlterByChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameColumn(PolardbXParser.AlterByRenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameColumn(PolardbXParser.AlterByRenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByLock(PolardbXParser.AlterByLockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByLock(PolardbXParser.AlterByLockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByModifyColumn(PolardbXParser.AlterByModifyColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByModifyColumn(PolardbXParser.AlterByModifyColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropColumn(PolardbXParser.AlterByDropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropColumn(PolardbXParser.AlterByDropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropConstraintCheck(PolardbXParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropConstraintCheck(PolardbXParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPrimaryKey(PolardbXParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPrimaryKey(PolardbXParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropIndex(PolardbXParser.AlterByDropIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropIndex(PolardbXParser.AlterByDropIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameIndex(PolardbXParser.AlterByRenameIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameIndex(PolardbXParser.AlterByRenameIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAlterIndexVisibility(PolardbXParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAlterIndexVisibility(PolardbXParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropForeignKey(PolardbXParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropForeignKey(PolardbXParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDisableKeys(PolardbXParser.AlterByDisableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDisableKeys(PolardbXParser.AlterByDisableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByEnableKeys(PolardbXParser.AlterByEnableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByEnableKeys(PolardbXParser.AlterByEnableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRename(PolardbXParser.AlterByRenameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRename(PolardbXParser.AlterByRenameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOrder(PolardbXParser.AlterByOrderContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOrder(PolardbXParser.AlterByOrderContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByConvertCharset(PolardbXParser.AlterByConvertCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByConvertCharset(PolardbXParser.AlterByConvertCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDefaultCharset(PolardbXParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDefaultCharset(PolardbXParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardTablespace(PolardbXParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardTablespace(PolardbXParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportTablespace(PolardbXParser.AlterByImportTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportTablespace(PolardbXParser.AlterByImportTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByForce(PolardbXParser.AlterByForceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByForce(PolardbXParser.AlterByForceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByValidate(PolardbXParser.AlterByValidateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByValidate(PolardbXParser.AlterByValidateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPartition(PolardbXParser.AlterByAddPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPartition(PolardbXParser.AlterByAddPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPartition(PolardbXParser.AlterByDropPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPartition(PolardbXParser.AlterByDropPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardPartition(PolardbXParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardPartition(PolardbXParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportPartition(PolardbXParser.AlterByImportPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportPartition(PolardbXParser.AlterByImportPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTruncatePartition(PolardbXParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTruncatePartition(PolardbXParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCoalescePartition(PolardbXParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCoalescePartition(PolardbXParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByReorganizePartition(PolardbXParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByReorganizePartition(PolardbXParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByExchangePartition(PolardbXParser.AlterByExchangePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByExchangePartition(PolardbXParser.AlterByExchangePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAnalyzePartition(PolardbXParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAnalyzePartition(PolardbXParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCheckPartition(PolardbXParser.AlterByCheckPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCheckPartition(PolardbXParser.AlterByCheckPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOptimizePartition(PolardbXParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOptimizePartition(PolardbXParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRebuildPartition(PolardbXParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRebuildPartition(PolardbXParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRepairPartition(PolardbXParser.AlterByRepairPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRepairPartition(PolardbXParser.AlterByRepairPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRemovePartitioning(PolardbXParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRemovePartitioning(PolardbXParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByUpgradePartitioning(PolardbXParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link PolardbXParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByUpgradePartitioning(PolardbXParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(PolardbXParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(PolardbXParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(PolardbXParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(PolardbXParser.DropEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(PolardbXParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(PolardbXParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropLogfileGroup(PolardbXParser.DropLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropLogfileGroup(PolardbXParser.DropLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(PolardbXParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(PolardbXParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(PolardbXParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(PolardbXParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void enterDropServer(PolardbXParser.DropServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void exitDropServer(PolardbXParser.DropServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(PolardbXParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(PolardbXParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void enterDropTablespace(PolardbXParser.DropTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void exitDropTablespace(PolardbXParser.DropTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(PolardbXParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(PolardbXParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(PolardbXParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(PolardbXParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(PolardbXParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(PolardbXParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameTableClause(PolardbXParser.RenameTableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameTableClause(PolardbXParser.RenameTableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(PolardbXParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(PolardbXParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallStatement(PolardbXParser.CallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallStatement(PolardbXParser.CallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void enterProcedureArgs(PolardbXParser.ProcedureArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void exitProcedureArgs(PolardbXParser.ProcedureArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(PolardbXParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(PolardbXParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(PolardbXParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(PolardbXParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(PolardbXParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(PolardbXParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(PolardbXParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(PolardbXParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadDataStatement(PolardbXParser.LoadDataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadDataStatement(PolardbXParser.LoadDataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadXmlStatement(PolardbXParser.LoadXmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadXmlStatement(PolardbXParser.LoadXmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplaceStatement(PolardbXParser.ReplaceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplaceStatement(PolardbXParser.ReplaceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelect(PolardbXParser.SimpleSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelect(PolardbXParser.SimpleSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisSelect(PolardbXParser.ParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisSelect(PolardbXParser.ParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelect(PolardbXParser.UnionSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelect(PolardbXParser.UnionSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesisSelect(PolardbXParser.UnionParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link PolardbXParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesisSelect(PolardbXParser.UnionParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(PolardbXParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(PolardbXParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectStatement(PolardbXParser.WithSelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectStatement(PolardbXParser.WithSelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#withClause}.
	 * @param ctx the parse tree
	 */
	void enterWithClause(PolardbXParser.WithClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#withClause}.
	 * @param ctx the parse tree
	 */
	void exitWithClause(PolardbXParser.WithClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectExpr(PolardbXParser.WithSelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectExpr(PolardbXParser.WithSelectExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link PolardbXParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterSelectInsertValue(PolardbXParser.SelectInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link PolardbXParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitSelectInsertValue(PolardbXParser.SelectInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link PolardbXParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterCommentInsertValue(PolardbXParser.CommentInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link PolardbXParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitCommentInsertValue(PolardbXParser.CommentInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void enterUpdatedElement(PolardbXParser.UpdatedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void exitUpdatedElement(PolardbXParser.UpdatedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentField(PolardbXParser.AssignmentFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentField(PolardbXParser.AssignmentFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void enterLockClause(PolardbXParser.LockClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void exitLockClause(PolardbXParser.LockClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleDeleteStatement(PolardbXParser.SingleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleDeleteStatement(PolardbXParser.SingleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleDeleteStatement(PolardbXParser.MultipleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleDeleteStatement(PolardbXParser.MultipleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerOpenStatement(PolardbXParser.HandlerOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerOpenStatement(PolardbXParser.HandlerOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadIndexStatement(PolardbXParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadIndexStatement(PolardbXParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadStatement(PolardbXParser.HandlerReadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadStatement(PolardbXParser.HandlerReadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerCloseStatement(PolardbXParser.HandlerCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerCloseStatement(PolardbXParser.HandlerCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleUpdateStatement(PolardbXParser.SingleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleUpdateStatement(PolardbXParser.SingleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void enterIgnore_(PolardbXParser.Ignore_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void exitIgnore_(PolardbXParser.Ignore_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleUpdateStatement(PolardbXParser.MultipleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleUpdateStatement(PolardbXParser.MultipleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(PolardbXParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(PolardbXParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(PolardbXParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(PolardbXParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void enterTableSources(PolardbXParser.TableSourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void exitTableSources(PolardbXParser.TableSourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link PolardbXParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceBase(PolardbXParser.TableSourceBaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link PolardbXParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceBase(PolardbXParser.TableSourceBaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link PolardbXParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceNested(PolardbXParser.TableSourceNestedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link PolardbXParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceNested(PolardbXParser.TableSourceNestedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterAtomTableItem(PolardbXParser.AtomTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitAtomTableItem(PolardbXParser.AtomTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryTableItem(PolardbXParser.SubqueryTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryTableItem(PolardbXParser.SubqueryTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourcesItem(PolardbXParser.TableSourcesItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourcesItem(PolardbXParser.TableSourcesItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceWith(PolardbXParser.TableSourceWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link PolardbXParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceWith(PolardbXParser.TableSourceWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void enterIndexHint(PolardbXParser.IndexHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void exitIndexHint(PolardbXParser.IndexHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void enterIndexHintType(PolardbXParser.IndexHintTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void exitIndexHintType(PolardbXParser.IndexHintTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoin(PolardbXParser.InnerJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoin(PolardbXParser.InnerJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterStraightJoin(PolardbXParser.StraightJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitStraightJoin(PolardbXParser.StraightJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoin(PolardbXParser.OuterJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoin(PolardbXParser.OuterJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoin(PolardbXParser.NaturalJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link PolardbXParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoin(PolardbXParser.NaturalJoinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoinType(PolardbXParser.InnerJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoinType(PolardbXParser.InnerJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoinType(PolardbXParser.OuterJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoinType(PolardbXParser.OuterJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoinType(PolardbXParser.NaturalJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoinType(PolardbXParser.NaturalJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpression(PolardbXParser.QueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpression(PolardbXParser.QueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionNointo(PolardbXParser.QueryExpressionNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionNointo(PolardbXParser.QueryExpressionNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(PolardbXParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(PolardbXParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecificationNointo(PolardbXParser.QuerySpecificationNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecificationNointo(PolardbXParser.QuerySpecificationNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesis(PolardbXParser.UnionParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesis(PolardbXParser.UnionParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionStatement(PolardbXParser.UnionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionStatement(PolardbXParser.UnionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(PolardbXParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(PolardbXParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void enterSelectElements(PolardbXParser.SelectElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void exitSelectElements(PolardbXParser.SelectElementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStarElement(PolardbXParser.SelectStarElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStarElement(PolardbXParser.SelectStarElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnElement(PolardbXParser.SelectColumnElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnElement(PolardbXParser.SelectColumnElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectFunctionElement(PolardbXParser.SelectFunctionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectFunctionElement(PolardbXParser.SelectFunctionElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpressionElement(PolardbXParser.SelectExpressionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link PolardbXParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpressionElement(PolardbXParser.SelectExpressionElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void enterAliasName(PolardbXParser.AliasNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void exitAliasName(PolardbXParser.AliasNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoVariables(PolardbXParser.SelectIntoVariablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoVariables(PolardbXParser.SelectIntoVariablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoDumpFile(PolardbXParser.SelectIntoDumpFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoDumpFile(PolardbXParser.SelectIntoDumpFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoTextFile(PolardbXParser.SelectIntoTextFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link PolardbXParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoTextFile(PolardbXParser.SelectIntoTextFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectFieldsInto(PolardbXParser.SelectFieldsIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectFieldsInto(PolardbXParser.SelectFieldsIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectLinesInto(PolardbXParser.SelectLinesIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectLinesInto(PolardbXParser.SelectLinesIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(PolardbXParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(PolardbXParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(PolardbXParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(PolardbXParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupClause(PolardbXParser.GroupClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupClause(PolardbXParser.GroupClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(PolardbXParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(PolardbXParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(PolardbXParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(PolardbXParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(PolardbXParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(PolardbXParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void enterLimitClauseAtom(PolardbXParser.LimitClauseAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void exitLimitClauseAtom(PolardbXParser.LimitClauseAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(PolardbXParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(PolardbXParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void enterBeginWork(PolardbXParser.BeginWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void exitBeginWork(PolardbXParser.BeginWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void enterCommitWork(PolardbXParser.CommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void exitCommitWork(PolardbXParser.CommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterRollbackWork(PolardbXParser.RollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitRollbackWork(PolardbXParser.RollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void enterSavepointStatement(PolardbXParser.SavepointStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void exitSavepointStatement(PolardbXParser.SavepointStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void enterRollbackStatement(PolardbXParser.RollbackStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void exitRollbackStatement(PolardbXParser.RollbackStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterReleaseStatement(PolardbXParser.ReleaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitReleaseStatement(PolardbXParser.ReleaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void enterLockTables(PolardbXParser.LockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void exitLockTables(PolardbXParser.LockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void enterUnlockTables(PolardbXParser.UnlockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void exitUnlockTables(PolardbXParser.UnlockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommitStatement(PolardbXParser.SetAutocommitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommitStatement(PolardbXParser.SetAutocommitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransactionStatement(PolardbXParser.SetTransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransactionStatement(PolardbXParser.SetTransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionMode(PolardbXParser.TransactionModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionMode(PolardbXParser.TransactionModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void enterLockTableElement(PolardbXParser.LockTableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void exitLockTableElement(PolardbXParser.LockTableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void enterLockAction(PolardbXParser.LockActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void exitLockAction(PolardbXParser.LockActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void enterTransactionOption(PolardbXParser.TransactionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void exitTransactionOption(PolardbXParser.TransactionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevel(PolardbXParser.TransactionLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevel(PolardbXParser.TransactionLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void enterChangeMaster(PolardbXParser.ChangeMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void exitChangeMaster(PolardbXParser.ChangeMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterChangeReplicationFilter(PolardbXParser.ChangeReplicationFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitChangeReplicationFilter(PolardbXParser.ChangeReplicationFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void enterPurgeBinaryLogs(PolardbXParser.PurgeBinaryLogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void exitPurgeBinaryLogs(PolardbXParser.PurgeBinaryLogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void enterResetMaster(PolardbXParser.ResetMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void exitResetMaster(PolardbXParser.ResetMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void enterResetSlave(PolardbXParser.ResetSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void exitResetSlave(PolardbXParser.ResetSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void enterResetReplica(PolardbXParser.ResetReplicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void exitResetReplica(PolardbXParser.ResetReplicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void enterStartSlave(PolardbXParser.StartSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void exitStartSlave(PolardbXParser.StartSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void enterStopSlave(PolardbXParser.StopSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void exitStopSlave(PolardbXParser.StopSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStartGroupReplication(PolardbXParser.StartGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStartGroupReplication(PolardbXParser.StartGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStopGroupReplication(PolardbXParser.StopGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStopGroupReplication(PolardbXParser.StopGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterStringOption(PolardbXParser.MasterStringOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterStringOption(PolardbXParser.MasterStringOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterDecimalOption(PolardbXParser.MasterDecimalOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterDecimalOption(PolardbXParser.MasterDecimalOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterBoolOption(PolardbXParser.MasterBoolOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterBoolOption(PolardbXParser.MasterBoolOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterRealOption(PolardbXParser.MasterRealOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterRealOption(PolardbXParser.MasterRealOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterUidListOption(PolardbXParser.MasterUidListOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link PolardbXParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterUidListOption(PolardbXParser.MasterUidListOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterStringMasterOption(PolardbXParser.StringMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitStringMasterOption(PolardbXParser.StringMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterDecimalMasterOption(PolardbXParser.DecimalMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitDecimalMasterOption(PolardbXParser.DecimalMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterBoolMasterOption(PolardbXParser.BoolMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitBoolMasterOption(PolardbXParser.BoolMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelOption(PolardbXParser.ChannelOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelOption(PolardbXParser.ChannelOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoDbReplication(PolardbXParser.DoDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoDbReplication(PolardbXParser.DoDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreDbReplication(PolardbXParser.IgnoreDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreDbReplication(PolardbXParser.IgnoreDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoTableReplication(PolardbXParser.DoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoTableReplication(PolardbXParser.DoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreTableReplication(PolardbXParser.IgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreTableReplication(PolardbXParser.IgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildDoTableReplication(PolardbXParser.WildDoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildDoTableReplication(PolardbXParser.WildDoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildIgnoreTableReplication(PolardbXParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildIgnoreTableReplication(PolardbXParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterRewriteDbReplication(PolardbXParser.RewriteDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link PolardbXParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitRewriteDbReplication(PolardbXParser.RewriteDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void enterTablePair(PolardbXParser.TablePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void exitTablePair(PolardbXParser.TablePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#threadType}.
	 * @param ctx the parse tree
	 */
	void enterThreadType(PolardbXParser.ThreadTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#threadType}.
	 * @param ctx the parse tree
	 */
	void exitThreadType(PolardbXParser.ThreadTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterGtidsUntilOption(PolardbXParser.GtidsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitGtidsUntilOption(PolardbXParser.GtidsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterLogUntilOption(PolardbXParser.MasterLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterLogUntilOption(PolardbXParser.MasterLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterRelayLogUntilOption(PolardbXParser.RelayLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitRelayLogUntilOption(PolardbXParser.RelayLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterSqlGapsUntilOption(PolardbXParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link PolardbXParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitSqlGapsUntilOption(PolardbXParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterUserConnectionOption(PolardbXParser.UserConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitUserConnectionOption(PolardbXParser.UserConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPasswordConnectionOption(PolardbXParser.PasswordConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPasswordConnectionOption(PolardbXParser.PasswordConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterDefaultAuthConnectionOption(PolardbXParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitDefaultAuthConnectionOption(PolardbXParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPluginDirConnectionOption(PolardbXParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link PolardbXParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPluginDirConnectionOption(PolardbXParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void enterGtuidSet(PolardbXParser.GtuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void exitGtuidSet(PolardbXParser.GtuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaStartTransaction(PolardbXParser.XaStartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaStartTransaction(PolardbXParser.XaStartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaEndTransaction(PolardbXParser.XaEndTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaEndTransaction(PolardbXParser.XaEndTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterXaPrepareStatement(PolardbXParser.XaPrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitXaPrepareStatement(PolardbXParser.XaPrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void enterXaCommitWork(PolardbXParser.XaCommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void exitXaCommitWork(PolardbXParser.XaCommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRollbackWork(PolardbXParser.XaRollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRollbackWork(PolardbXParser.XaRollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRecoverWork(PolardbXParser.XaRecoverWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRecoverWork(PolardbXParser.XaRecoverWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrepareStatement(PolardbXParser.PrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrepareStatement(PolardbXParser.PrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatement(PolardbXParser.ExecuteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatement(PolardbXParser.ExecuteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatePrepare(PolardbXParser.DeallocatePrepareContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatePrepare(PolardbXParser.DeallocatePrepareContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBody(PolardbXParser.RoutineBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBody(PolardbXParser.RoutineBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(PolardbXParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(PolardbXParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(PolardbXParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(PolardbXParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PolardbXParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PolardbXParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterateStatement(PolardbXParser.IterateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterateStatement(PolardbXParser.IterateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(PolardbXParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(PolardbXParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(PolardbXParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(PolardbXParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(PolardbXParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(PolardbXParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PolardbXParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PolardbXParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(PolardbXParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(PolardbXParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCloseCursor(PolardbXParser.CloseCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCloseCursor(PolardbXParser.CloseCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterFetchCursor(PolardbXParser.FetchCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitFetchCursor(PolardbXParser.FetchCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterOpenCursor(PolardbXParser.OpenCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link PolardbXParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitOpenCursor(PolardbXParser.OpenCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVariable(PolardbXParser.DeclareVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVariable(PolardbXParser.DeclareVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCondition(PolardbXParser.DeclareConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCondition(PolardbXParser.DeclareConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCursor(PolardbXParser.DeclareCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCursor(PolardbXParser.DeclareCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void enterDeclareHandler(PolardbXParser.DeclareHandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void exitDeclareHandler(PolardbXParser.DeclareHandlerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionCode(PolardbXParser.HandlerConditionCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionCode(PolardbXParser.HandlerConditionCodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionState(PolardbXParser.HandlerConditionStateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionState(PolardbXParser.HandlerConditionStateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionName(PolardbXParser.HandlerConditionNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionName(PolardbXParser.HandlerConditionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionWarning(PolardbXParser.HandlerConditionWarningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionWarning(PolardbXParser.HandlerConditionWarningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionNotfound(PolardbXParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionNotfound(PolardbXParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionException(PolardbXParser.HandlerConditionExceptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link PolardbXParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionException(PolardbXParser.HandlerConditionExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterProcedureSqlStatement(PolardbXParser.ProcedureSqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitProcedureSqlStatement(PolardbXParser.ProcedureSqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseAlternative(PolardbXParser.CaseAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseAlternative(PolardbXParser.CaseAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void enterElifAlternative(PolardbXParser.ElifAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void exitElifAlternative(PolardbXParser.ElifAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link PolardbXParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV56(PolardbXParser.AlterUserMysqlV56Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link PolardbXParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV56(PolardbXParser.AlterUserMysqlV56Context ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link PolardbXParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV57(PolardbXParser.AlterUserMysqlV57Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link PolardbXParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV57(PolardbXParser.AlterUserMysqlV57Context ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUser(PolardbXParser.CreateUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUser(PolardbXParser.CreateUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(PolardbXParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(PolardbXParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(PolardbXParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(PolardbXParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void enterGrantStatement(PolardbXParser.GrantStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void exitGrantStatement(PolardbXParser.GrantStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void enterRoleOption(PolardbXParser.RoleOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void exitRoleOption(PolardbXParser.RoleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void enterGrantProxy(PolardbXParser.GrantProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void exitGrantProxy(PolardbXParser.GrantProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(PolardbXParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(PolardbXParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterRevokeStatement(PolardbXParser.RevokeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitRevokeStatement(PolardbXParser.RevokeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void enterRevokeProxy(PolardbXParser.RevokeProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void exitRevokeProxy(PolardbXParser.RevokeProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPasswordStatement(PolardbXParser.SetPasswordStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPasswordStatement(PolardbXParser.SetPasswordStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void enterUserSpecification(PolardbXParser.UserSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void exitUserSpecification(PolardbXParser.UserSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterHashAuthOption(PolardbXParser.HashAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitHashAuthOption(PolardbXParser.HashAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterStringAuthOption(PolardbXParser.StringAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitStringAuthOption(PolardbXParser.StringAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterModuleAuthOption(PolardbXParser.ModuleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitModuleAuthOption(PolardbXParser.ModuleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAuthOption(PolardbXParser.SimpleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link PolardbXParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAuthOption(PolardbXParser.SimpleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code module}
	 * labeled alternative in {@link PolardbXParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterModule(PolardbXParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code module}
	 * labeled alternative in {@link PolardbXParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitModule(PolardbXParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link PolardbXParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterPasswordModuleOption(PolardbXParser.PasswordModuleOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link PolardbXParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitPasswordModuleOption(PolardbXParser.PasswordModuleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void enterTlsOption(PolardbXParser.TlsOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void exitTlsOption(PolardbXParser.TlsOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void enterUserResourceOption(PolardbXParser.UserResourceOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void exitUserResourceOption(PolardbXParser.UserResourceOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void enterUserPasswordOption(PolardbXParser.UserPasswordOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void exitUserPasswordOption(PolardbXParser.UserPasswordOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void enterUserLockOption(PolardbXParser.UserLockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void exitUserLockOption(PolardbXParser.UserLockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void enterPrivelegeClause(PolardbXParser.PrivelegeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void exitPrivelegeClause(PolardbXParser.PrivelegeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(PolardbXParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(PolardbXParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterCurrentSchemaPriviLevel(PolardbXParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitCurrentSchemaPriviLevel(PolardbXParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterGlobalPrivLevel(PolardbXParser.GlobalPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitGlobalPrivLevel(PolardbXParser.GlobalPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteSchemaPrivLevel(PolardbXParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteSchemaPrivLevel(PolardbXParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel(PolardbXParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel(PolardbXParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel2(PolardbXParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel2(PolardbXParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteTablePrivLevel(PolardbXParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link PolardbXParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteTablePrivLevel(PolardbXParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameUserClause(PolardbXParser.RenameUserClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameUserClause(PolardbXParser.RenameUserClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzeTable(PolardbXParser.AnalyzeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzeTable(PolardbXParser.AnalyzeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void enterCheckTable(PolardbXParser.CheckTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void exitCheckTable(PolardbXParser.CheckTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void enterChecksumTable(PolardbXParser.ChecksumTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void exitChecksumTable(PolardbXParser.ChecksumTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void enterOptimizeTable(PolardbXParser.OptimizeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void exitOptimizeTable(PolardbXParser.OptimizeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(PolardbXParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(PolardbXParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableOption(PolardbXParser.CheckTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableOption(PolardbXParser.CheckTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateUdfFunction(PolardbXParser.CreateUdfFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateUdfFunction(PolardbXParser.CreateUdfFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void enterInstallPlugin(PolardbXParser.InstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void exitInstallPlugin(PolardbXParser.InstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void enterUninstallPlugin(PolardbXParser.UninstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void exitUninstallPlugin(PolardbXParser.UninstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetVariable(PolardbXParser.SetVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetVariable(PolardbXParser.SetVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetCharset(PolardbXParser.SetCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetCharset(PolardbXParser.SetCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNames(PolardbXParser.SetNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNames(PolardbXParser.SetNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPassword(PolardbXParser.SetPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPassword(PolardbXParser.SetPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransaction(PolardbXParser.SetTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransaction(PolardbXParser.SetTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommit(PolardbXParser.SetAutocommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommit(PolardbXParser.SetAutocommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNewValueInsideTrigger(PolardbXParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link PolardbXParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNewValueInsideTrigger(PolardbXParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowMasterLogs(PolardbXParser.ShowMasterLogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowMasterLogs(PolardbXParser.ShowMasterLogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCharset(PolardbXParser.ShowCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCharset(PolardbXParser.ShowCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowLogEvents(PolardbXParser.ShowLogEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowLogEvents(PolardbXParser.ShowLogEventsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowObjectFilter(PolardbXParser.ShowObjectFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowObjectFilter(PolardbXParser.ShowObjectFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(PolardbXParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(PolardbXParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(PolardbXParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(PolardbXParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateDb(PolardbXParser.ShowCreateDbContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateDb(PolardbXParser.ShowCreateDbContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateFullIdObject(PolardbXParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateFullIdObject(PolardbXParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateUser(PolardbXParser.ShowCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateUser(PolardbXParser.ShowCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngine(PolardbXParser.ShowEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngine(PolardbXParser.ShowEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngines(PolardbXParser.ShowEnginesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngines(PolardbXParser.ShowEnginesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatus(PolardbXParser.ShowStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatus(PolardbXParser.ShowStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPlugins(PolardbXParser.ShowPluginsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPlugins(PolardbXParser.ShowPluginsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPrivileges(PolardbXParser.ShowPrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPrivileges(PolardbXParser.ShowPrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProcessList(PolardbXParser.ShowProcessListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProcessList(PolardbXParser.ShowProcessListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfiles(PolardbXParser.ShowProfilesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfiles(PolardbXParser.ShowProfilesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveHosts(PolardbXParser.ShowSlaveHostsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveHosts(PolardbXParser.ShowSlaveHostsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowAuthros(PolardbXParser.ShowAuthrosContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowAuthros(PolardbXParser.ShowAuthrosContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowContributors(PolardbXParser.ShowContributorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowContributors(PolardbXParser.ShowContributorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowErrors(PolardbXParser.ShowErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowErrors(PolardbXParser.ShowErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCountErrors(PolardbXParser.ShowCountErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCountErrors(PolardbXParser.ShowCountErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaFilter(PolardbXParser.ShowSchemaFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaFilter(PolardbXParser.ShowSchemaFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoutine(PolardbXParser.ShowRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoutine(PolardbXParser.ShowRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(PolardbXParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(PolardbXParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowIndexes(PolardbXParser.ShowIndexesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowIndexes(PolardbXParser.ShowIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowOpenTables(PolardbXParser.ShowOpenTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowOpenTables(PolardbXParser.ShowOpenTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfile(PolardbXParser.ShowProfileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfile(PolardbXParser.ShowProfileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveStatus(PolardbXParser.ShowSlaveStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveStatus(PolardbXParser.ShowSlaveStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowReplicaStatus(PolardbXParser.ShowReplicaStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link PolardbXParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowReplicaStatus(PolardbXParser.ShowReplicaStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void enterVariableClause(PolardbXParser.VariableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void exitVariableClause(PolardbXParser.VariableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowCommonEntity(PolardbXParser.ShowCommonEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowCommonEntity(PolardbXParser.ShowCommonEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void enterShowFilter(PolardbXParser.ShowFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void exitShowFilter(PolardbXParser.ShowFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void enterShowGlobalInfoClause(PolardbXParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void exitShowGlobalInfoClause(PolardbXParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaEntity(PolardbXParser.ShowSchemaEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaEntity(PolardbXParser.ShowSchemaEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void enterShowProfileType(PolardbXParser.ShowProfileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void exitShowProfileType(PolardbXParser.ShowProfileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void enterBinlogStatement(PolardbXParser.BinlogStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void exitBinlogStatement(PolardbXParser.BinlogStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterCacheIndexStatement(PolardbXParser.CacheIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitCacheIndexStatement(PolardbXParser.CacheIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(PolardbXParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(PolardbXParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void enterKillStatement(PolardbXParser.KillStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void exitKillStatement(PolardbXParser.KillStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void enterLoadIndexIntoCache(PolardbXParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void exitLoadIndexIntoCache(PolardbXParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void enterResetStatement(PolardbXParser.ResetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void exitResetStatement(PolardbXParser.ResetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void enterShutdownStatement(PolardbXParser.ShutdownStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void exitShutdownStatement(PolardbXParser.ShutdownStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterTableIndexes(PolardbXParser.TableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitTableIndexes(PolardbXParser.TableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFlushOption(PolardbXParser.SimpleFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFlushOption(PolardbXParser.SimpleFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelFlushOption(PolardbXParser.ChannelFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelFlushOption(PolardbXParser.ChannelFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterTableFlushOption(PolardbXParser.TableFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link PolardbXParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitTableFlushOption(PolardbXParser.TableFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void enterFlushTableOption(PolardbXParser.FlushTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void exitFlushTableOption(PolardbXParser.FlushTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterLoadedTableIndexes(PolardbXParser.LoadedTableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitLoadedTableIndexes(PolardbXParser.LoadedTableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDescribeStatement(PolardbXParser.SimpleDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDescribeStatement(PolardbXParser.SimpleDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterFullDescribeStatement(PolardbXParser.FullDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitFullDescribeStatement(PolardbXParser.FullDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void enterHelpStatement(PolardbXParser.HelpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void exitHelpStatement(PolardbXParser.HelpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void enterUseStatement(PolardbXParser.UseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void exitUseStatement(PolardbXParser.UseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void enterSignalStatement(PolardbXParser.SignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void exitSignalStatement(PolardbXParser.SignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void enterResignalStatement(PolardbXParser.ResignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void exitResignalStatement(PolardbXParser.ResignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void enterSignalConditionInformation(PolardbXParser.SignalConditionInformationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void exitSignalConditionInformation(PolardbXParser.SignalConditionInformationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsStatement(PolardbXParser.DiagnosticsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsStatement(PolardbXParser.DiagnosticsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsConditionInformationName(PolardbXParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsConditionInformationName(PolardbXParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link PolardbXParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeStatements(PolardbXParser.DescribeStatementsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link PolardbXParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeStatements(PolardbXParser.DescribeStatementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link PolardbXParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeConnection(PolardbXParser.DescribeConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link PolardbXParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeConnection(PolardbXParser.DescribeConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#fullId}.
	 * @param ctx the parse tree
	 */
	void enterFullId(PolardbXParser.FullIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#fullId}.
	 * @param ctx the parse tree
	 */
	void exitFullId(PolardbXParser.FullIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(PolardbXParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(PolardbXParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#procName}.
	 * @param ctx the parse tree
	 */
	void enterProcName(PolardbXParser.ProcNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#procName}.
	 * @param ctx the parse tree
	 */
	void exitProcName(PolardbXParser.ProcNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterCustomFunctionName(PolardbXParser.CustomFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitCustomFunctionName(PolardbXParser.CustomFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#roleName}.
	 * @param ctx the parse tree
	 */
	void enterRoleName(PolardbXParser.RoleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#roleName}.
	 * @param ctx the parse tree
	 */
	void exitRoleName(PolardbXParser.RoleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(PolardbXParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(PolardbXParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnName(PolardbXParser.IndexColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnName(PolardbXParser.IndexColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userName}.
	 * @param ctx the parse tree
	 */
	void enterUserName(PolardbXParser.UserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userName}.
	 * @param ctx the parse tree
	 */
	void exitUserName(PolardbXParser.UserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariable(PolardbXParser.MysqlVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariable(PolardbXParser.MysqlVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void enterCharsetName(PolardbXParser.CharsetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void exitCharsetName(PolardbXParser.CharsetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#collationName}.
	 * @param ctx the parse tree
	 */
	void enterCollationName(PolardbXParser.CollationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#collationName}.
	 * @param ctx the parse tree
	 */
	void exitCollationName(PolardbXParser.CollationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#engineName}.
	 * @param ctx the parse tree
	 */
	void enterEngineName(PolardbXParser.EngineNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#engineName}.
	 * @param ctx the parse tree
	 */
	void exitEngineName(PolardbXParser.EngineNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void enterUuidSet(PolardbXParser.UuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void exitUuidSet(PolardbXParser.UuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xid}.
	 * @param ctx the parse tree
	 */
	void enterXid(PolardbXParser.XidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xid}.
	 * @param ctx the parse tree
	 */
	void exitXid(PolardbXParser.XidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void enterXuidStringId(PolardbXParser.XuidStringIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void exitXuidStringId(PolardbXParser.XuidStringIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void enterAuthPlugin(PolardbXParser.AuthPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void exitAuthPlugin(PolardbXParser.AuthPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#uid}.
	 * @param ctx the parse tree
	 */
	void enterUid(PolardbXParser.UidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#uid}.
	 * @param ctx the parse tree
	 */
	void exitUid(PolardbXParser.UidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleId(PolardbXParser.SimpleIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleId(PolardbXParser.SimpleIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void enterDottedId(PolardbXParser.DottedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void exitDottedId(PolardbXParser.DottedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(PolardbXParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(PolardbXParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFileSizeLiteral(PolardbXParser.FileSizeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFileSizeLiteral(PolardbXParser.FileSizeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(PolardbXParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(PolardbXParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(PolardbXParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(PolardbXParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(PolardbXParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(PolardbXParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void enterNullNotnull(PolardbXParser.NullNotnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void exitNullNotnull(PolardbXParser.NullNotnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(PolardbXParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(PolardbXParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterStringDataType(PolardbXParser.StringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitStringDataType(PolardbXParser.StringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalStringDataType(PolardbXParser.NationalStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalStringDataType(PolardbXParser.NationalStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalVaryingStringDataType(PolardbXParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalVaryingStringDataType(PolardbXParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDimensionDataType(PolardbXParser.DimensionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDimensionDataType(PolardbXParser.DimensionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDataType(PolardbXParser.SimpleDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDataType(PolardbXParser.SimpleDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterCollectionDataType(PolardbXParser.CollectionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitCollectionDataType(PolardbXParser.CollectionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSpatialDataType(PolardbXParser.SpatialDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSpatialDataType(PolardbXParser.SpatialDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarcharDataType(PolardbXParser.LongVarcharDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarcharDataType(PolardbXParser.LongVarcharDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarbinaryDataType(PolardbXParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link PolardbXParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarbinaryDataType(PolardbXParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOptions(PolardbXParser.CollectionOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOptions(PolardbXParser.CollectionOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOption(PolardbXParser.CollectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOption(PolardbXParser.CollectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void enterConvertedDataType(PolardbXParser.ConvertedDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void exitConvertedDataType(PolardbXParser.ConvertedDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthOneDimension(PolardbXParser.LengthOneDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthOneDimension(PolardbXParser.LengthOneDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoDimension(PolardbXParser.LengthTwoDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoDimension(PolardbXParser.LengthTwoDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoOptionalDimension(PolardbXParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoOptionalDimension(PolardbXParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#uidList}.
	 * @param ctx the parse tree
	 */
	void enterUidList(PolardbXParser.UidListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#uidList}.
	 * @param ctx the parse tree
	 */
	void exitUidList(PolardbXParser.UidListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#tables}.
	 * @param ctx the parse tree
	 */
	void enterTables(PolardbXParser.TablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#tables}.
	 * @param ctx the parse tree
	 */
	void exitTables(PolardbXParser.TablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnNames(PolardbXParser.IndexColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnNames(PolardbXParser.IndexColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(PolardbXParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(PolardbXParser.ExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void enterExpressionsWithDefaults(PolardbXParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void exitExpressionsWithDefaults(PolardbXParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#constants}.
	 * @param ctx the parse tree
	 */
	void enterConstants(PolardbXParser.ConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#constants}.
	 * @param ctx the parse tree
	 */
	void exitConstants(PolardbXParser.ConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStrings(PolardbXParser.SimpleStringsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStrings(PolardbXParser.SimpleStringsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void enterUserVariables(PolardbXParser.UserVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void exitUserVariables(PolardbXParser.UserVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(PolardbXParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(PolardbXParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestamp(PolardbXParser.CurrentTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestamp(PolardbXParser.CurrentTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterExpressionOrDefault(PolardbXParser.ExpressionOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitExpressionOrDefault(PolardbXParser.ExpressionOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void enterIfExists(PolardbXParser.IfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void exitIfExists(PolardbXParser.IfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void enterIfNotExists(PolardbXParser.IfNotExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void exitIfNotExists(PolardbXParser.IfNotExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterSpecificFunctionCall(PolardbXParser.SpecificFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitSpecificFunctionCall(PolardbXParser.SpecificFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunctionCall(PolardbXParser.AggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunctionCall(PolardbXParser.AggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionCall(PolardbXParser.ScalarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionCall(PolardbXParser.ScalarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterUdfFunctionCall(PolardbXParser.UdfFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitUdfFunctionCall(PolardbXParser.UdfFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionCall(PolardbXParser.PasswordFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionCall(PolardbXParser.PasswordFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunctionCall(PolardbXParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link PolardbXParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunctionCall(PolardbXParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunction(PolardbXParser.NonAggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunction(PolardbXParser.NonAggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(PolardbXParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(PolardbXParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void enterWindow_specification(PolardbXParser.Window_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void exitWindow_specification(PolardbXParser.Window_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause(PolardbXParser.Frame_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause(PolardbXParser.Frame_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void enterFrame_extent(PolardbXParser.Frame_extentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void exitFrame_extent(PolardbXParser.Frame_extentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void enterFrame_start_or_end(PolardbXParser.Frame_start_or_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void exitFrame_start_or_end(PolardbXParser.Frame_start_or_endContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFunctionCall(PolardbXParser.SimpleFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFunctionCall(PolardbXParser.SimpleFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSpecialTimeCall(PolardbXParser.SpecialTimeCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSpecialTimeCall(PolardbXParser.SpecialTimeCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeFunctionCall(PolardbXParser.DataTypeFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeFunctionCall(PolardbXParser.DataTypeFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterValuesFunctionCall(PolardbXParser.ValuesFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitValuesFunctionCall(PolardbXParser.ValuesFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCaseFunctionCall(PolardbXParser.CaseFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCaseFunctionCall(PolardbXParser.CaseFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCharFunctionCall(PolardbXParser.CharFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCharFunctionCall(PolardbXParser.CharFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterPositionFunctionCall(PolardbXParser.PositionFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitPositionFunctionCall(PolardbXParser.PositionFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSubstrFunctionCall(PolardbXParser.SubstrFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSubstrFunctionCall(PolardbXParser.SubstrFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterTrimFunctionCall(PolardbXParser.TrimFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitTrimFunctionCall(PolardbXParser.TrimFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterWeightFunctionCall(PolardbXParser.WeightFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitWeightFunctionCall(PolardbXParser.WeightFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterExtractFunctionCall(PolardbXParser.ExtractFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitExtractFunctionCall(PolardbXParser.ExtractFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetFormatFunctionCall(PolardbXParser.GetFormatFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetFormatFunctionCall(PolardbXParser.GetFormatFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueFunctionCall(PolardbXParser.JsonValueFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link PolardbXParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueFunctionCall(PolardbXParser.JsonValueFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseFuncAlternative(PolardbXParser.CaseFuncAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseFuncAlternative(PolardbXParser.CaseFuncAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link PolardbXParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightList(PolardbXParser.LevelWeightListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link PolardbXParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightList(PolardbXParser.LevelWeightListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link PolardbXParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightRange(PolardbXParser.LevelWeightRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link PolardbXParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightRange(PolardbXParser.LevelWeightRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void enterLevelInWeightListElement(PolardbXParser.LevelInWeightListElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void exitLevelInWeightListElement(PolardbXParser.LevelInWeightListElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunction(PolardbXParser.AggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunction(PolardbXParser.AggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionName(PolardbXParser.ScalarFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionName(PolardbXParser.ScalarFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionClause(PolardbXParser.PasswordFunctionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionClause(PolardbXParser.PasswordFunctionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgs(PolardbXParser.FunctionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgs(PolardbXParser.FunctionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(PolardbXParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(PolardbXParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(PolardbXParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(PolardbXParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(PolardbXParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(PolardbXParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(PolardbXParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(PolardbXParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(PolardbXParser.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link PolardbXParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(PolardbXParser.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSoundsLikePredicate(PolardbXParser.SoundsLikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSoundsLikePredicate(PolardbXParser.SoundsLikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAtomPredicate(PolardbXParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAtomPredicate(PolardbXParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterJsonMemberOfPredicate(PolardbXParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitJsonMemberOfPredicate(PolardbXParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(PolardbXParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(PolardbXParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryComparasionPredicate(PolardbXParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryComparasionPredicate(PolardbXParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterFullSearchPredicate(PolardbXParser.FullSearchPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitFullSearchPredicate(PolardbXParser.FullSearchPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetweenPredicate(PolardbXParser.BetweenPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetweenPredicate(PolardbXParser.BetweenPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBinaryComparasionPredicate(PolardbXParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBinaryComparasionPredicate(PolardbXParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(PolardbXParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(PolardbXParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(PolardbXParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(PolardbXParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterRegexpPredicate(PolardbXParser.RegexpPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link PolardbXParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitRegexpPredicate(PolardbXParser.RegexpPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void enterSearch_modifier(PolardbXParser.Search_modifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void exitSearch_modifier(PolardbXParser.Search_modifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionAtom(PolardbXParser.UnaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionAtom(PolardbXParser.UnaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterCollateExpressionAtom(PolardbXParser.CollateExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitCollateExpressionAtom(PolardbXParser.CollateExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpessionAtom(PolardbXParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpessionAtom(PolardbXParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariableExpressionAtom(PolardbXParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariableExpressionAtom(PolardbXParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpressionAtom(PolardbXParser.NestedExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpressionAtom(PolardbXParser.NestedExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedRowExpressionAtom(PolardbXParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedRowExpressionAtom(PolardbXParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMathExpressionAtom(PolardbXParser.MathExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMathExpressionAtom(PolardbXParser.MathExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpressionAtom(PolardbXParser.IntervalExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpressionAtom(PolardbXParser.IntervalExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterJsonExpressionAtom(PolardbXParser.JsonExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitJsonExpressionAtom(PolardbXParser.JsonExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterExistsExpessionAtom(PolardbXParser.ExistsExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitExistsExpessionAtom(PolardbXParser.ExistsExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(PolardbXParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(PolardbXParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(PolardbXParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(PolardbXParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpressionAtom(PolardbXParser.BinaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpressionAtom(PolardbXParser.BinaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(PolardbXParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(PolardbXParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBitExpressionAtom(PolardbXParser.BitExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link PolardbXParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBitExpressionAtom(PolardbXParser.BitExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(PolardbXParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(PolardbXParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(PolardbXParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(PolardbXParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(PolardbXParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(PolardbXParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitOperator(PolardbXParser.BitOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitOperator(PolardbXParser.BitOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void enterMathOperator(PolardbXParser.MathOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void exitMathOperator(PolardbXParser.MathOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void enterJsonOperator(PolardbXParser.JsonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void exitJsonOperator(PolardbXParser.JsonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void enterCharsetNameBase(PolardbXParser.CharsetNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void exitCharsetNameBase(PolardbXParser.CharsetNameBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevelBase(PolardbXParser.TransactionLevelBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevelBase(PolardbXParser.TransactionLevelBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void enterPrivilegesBase(PolardbXParser.PrivilegesBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void exitPrivilegesBase(PolardbXParser.PrivilegesBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterIntervalTypeBase(PolardbXParser.IntervalTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitIntervalTypeBase(PolardbXParser.IntervalTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeBase(PolardbXParser.DataTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeBase(PolardbXParser.DataTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(PolardbXParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(PolardbXParser.KeywordsCanBeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PolardbXParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNameBase(PolardbXParser.FunctionNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PolardbXParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNameBase(PolardbXParser.FunctionNameBaseContext ctx);
}