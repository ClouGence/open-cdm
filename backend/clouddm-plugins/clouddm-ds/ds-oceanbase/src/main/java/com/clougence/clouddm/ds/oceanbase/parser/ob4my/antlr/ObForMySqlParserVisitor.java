// Generated from ObForMySqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ObForMySqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ObForMySqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#selectSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectSpec(ObForMySqlParser.SelectSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#insertStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStatement(ObForMySqlParser.InsertStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(ObForMySqlParser.DropTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOption(ObForMySqlParser.IndexOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabaseOption(ObForMySqlParser.CreateDatabaseOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionEngine(ObForMySqlParser.TableOptionEngineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionAutoIncrement(ObForMySqlParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionAverage(ObForMySqlParser.TableOptionAverageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionCharset(ObForMySqlParser.TableOptionCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionChecksum(ObForMySqlParser.TableOptionChecksumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionCollate(ObForMySqlParser.TableOptionCollateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionComment(ObForMySqlParser.TableOptionCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionCompression(ObForMySqlParser.TableOptionCompressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionConnection(ObForMySqlParser.TableOptionConnectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionDataDirectory(ObForMySqlParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionDelay(ObForMySqlParser.TableOptionDelayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionEncryption(ObForMySqlParser.TableOptionEncryptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionIndexDirectory(ObForMySqlParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionInsertMethod(ObForMySqlParser.TableOptionInsertMethodContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionKeyBlockSize(ObForMySqlParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionMaxRows(ObForMySqlParser.TableOptionMaxRowsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionMinRows(ObForMySqlParser.TableOptionMinRowsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionPackKeys(ObForMySqlParser.TableOptionPackKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionPassword(ObForMySqlParser.TableOptionPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionRowFormat(ObForMySqlParser.TableOptionRowFormatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionRecalculation(ObForMySqlParser.TableOptionRecalculationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionPersistent(ObForMySqlParser.TableOptionPersistentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionSamplePage(ObForMySqlParser.TableOptionSamplePageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionTablespace(ObForMySqlParser.TableOptionTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionTableType(ObForMySqlParser.TableOptionTableTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionTransactional(ObForMySqlParser.TableOptionTransactionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptionUnion(ObForMySqlParser.TableOptionUnionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code obMyTableOption}
	 * labeled alternative in {@link ObForMySqlParser#tableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObMyTableOption(ObForMySqlParser.ObMyTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(ObForMySqlParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#sqlStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlStatements(ObForMySqlParser.SqlStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlStatement(ObForMySqlParser.SqlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(ObForMySqlParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ddlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdlStatement(ObForMySqlParser.DdlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDmlStatement(ObForMySqlParser.DmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#transactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionStatement(ObForMySqlParser.TransactionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#replicationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplicationStatement(ObForMySqlParser.ReplicationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#preparedStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreparedStatement(ObForMySqlParser.PreparedStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(ObForMySqlParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#administrationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdministrationStatement(ObForMySqlParser.AdministrationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#utilityStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtilityStatement(ObForMySqlParser.UtilityStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(ObForMySqlParser.CreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#databaseName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabaseName(ObForMySqlParser.DatabaseNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateEvent(ObForMySqlParser.CreateEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndex(ObForMySqlParser.CreateIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexName(ObForMySqlParser.IndexNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateLogfileGroup(ObForMySqlParser.CreateLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcedure(ObForMySqlParser.CreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateFunction(ObForMySqlParser.CreateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRole(ObForMySqlParser.CreateRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateServer(ObForMySqlParser.CreateServerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopyCreateTable(ObForMySqlParser.CopyCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryCreateTable(ObForMySqlParser.QueryCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link ObForMySqlParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnCreateTable(ObForMySqlParser.ColumnCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#temporary_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporary_(ObForMySqlParser.Temporary_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTablespaceInnodb(ObForMySqlParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTablespaceNdb(ObForMySqlParser.CreateTablespaceNdbContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTrigger(ObForMySqlParser.CreateTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateView(ObForMySqlParser.CreateViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ownerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnerStatement(ObForMySqlParser.OwnerStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreciseSchedule(ObForMySqlParser.PreciseScheduleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link ObForMySqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalSchedule(ObForMySqlParser.IntervalScheduleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#timestampValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestampValue(ObForMySqlParser.TimestampValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#intervalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalExpr(ObForMySqlParser.IntervalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#intervalType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalType(ObForMySqlParser.IntervalTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#enableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnableType(ObForMySqlParser.EnableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexType(ObForMySqlParser.IndexTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#procedureParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureParameter(ObForMySqlParser.ProcedureParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#functionParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParameter(ObForMySqlParser.FunctionParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineComment(ObForMySqlParser.RoutineCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineLanguage(ObForMySqlParser.RoutineLanguageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineBehavior(ObForMySqlParser.RoutineBehaviorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineData(ObForMySqlParser.RoutineDataContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link ObForMySqlParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineSecurity(ObForMySqlParser.RoutineSecurityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#serverOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitServerOption(ObForMySqlParser.ServerOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createDefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDefinitions(ObForMySqlParser.CreateDefinitionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDeclaration(ObForMySqlParser.ColumnDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintDeclaration(ObForMySqlParser.ConstraintDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#createDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexDeclaration(ObForMySqlParser.IndexDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDefinition(ObForMySqlParser.ColumnDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullColumnConstraint(ObForMySqlParser.NullColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultColumnConstraint(ObForMySqlParser.DefaultColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvisibleColumnConstraint(ObForMySqlParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutoIncrementColumnConstraint(ObForMySqlParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryKeyColumnConstraint(ObForMySqlParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniqueKeyColumnConstraint(ObForMySqlParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentColumnConstraint(ObForMySqlParser.CommentColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatColumnConstraint(ObForMySqlParser.FormatColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageColumnConstraint(ObForMySqlParser.StorageColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceColumnConstraint(ObForMySqlParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollateColumnConstraint(ObForMySqlParser.CollateColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneratedColumnConstraint(ObForMySqlParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSerialDefaultColumnConstraint(ObForMySqlParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link ObForMySqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckColumnConstraint(ObForMySqlParser.CheckColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryKeyTableConstraint(ObForMySqlParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniqueKeyTableConstraint(ObForMySqlParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeignKeyTableConstraint(ObForMySqlParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckTableConstraint(ObForMySqlParser.CheckTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#referenceDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceDefinition(ObForMySqlParser.ReferenceDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#referenceAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceAction(ObForMySqlParser.ReferenceActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#referenceControlType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceControlType(ObForMySqlParser.ReferenceControlTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleIndexDeclaration(ObForMySqlParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link ObForMySqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialIndexDeclaration(ObForMySqlParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableType(ObForMySqlParser.TableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespaceStorage(ObForMySqlParser.TablespaceStorageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinitions(ObForMySqlParser.PartitionDefinitionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionFunctionHash(ObForMySqlParser.PartitionFunctionHashContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionFunctionKey(ObForMySqlParser.PartitionFunctionKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionFunctionRange(ObForMySqlParser.PartitionFunctionRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link ObForMySqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionFunctionList(ObForMySqlParser.PartitionFunctionListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubPartitionFunctionHash(ObForMySqlParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link ObForMySqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubPartitionFunctionKey(ObForMySqlParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionComparision(ObForMySqlParser.PartitionComparisionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionListAtom(ObForMySqlParser.PartitionListAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionListVector(ObForMySqlParser.PartitionListVectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link ObForMySqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionSimple(ObForMySqlParser.PartitionSimpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinerAtom(ObForMySqlParser.PartitionDefinerAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinerVector(ObForMySqlParser.PartitionDefinerVectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartitionDefinition(ObForMySqlParser.SubpartitionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionEngine(ObForMySqlParser.PartitionOptionEngineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionComment(ObForMySqlParser.PartitionOptionCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionDataDirectory(ObForMySqlParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionIndexDirectory(ObForMySqlParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionMaxRows(ObForMySqlParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionMinRows(ObForMySqlParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionTablespace(ObForMySqlParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link ObForMySqlParser#partitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionOptionNodeGroup(ObForMySqlParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterSimpleDatabase(ObForMySqlParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link ObForMySqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUpgradeName(ObForMySqlParser.AlterUpgradeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterEvent(ObForMySqlParser.AlterEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterFunction(ObForMySqlParser.AlterFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterInstance(ObForMySqlParser.AlterInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterLogfileGroup(ObForMySqlParser.AlterLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterProcedure(ObForMySqlParser.AlterProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterServer(ObForMySqlParser.AlterServerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(ObForMySqlParser.AlterTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterTablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablespace(ObForMySqlParser.AlterTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#alterView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterView(ObForMySqlParser.AlterViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByTableOption(ObForMySqlParser.AlterByTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddColumn(ObForMySqlParser.AlterByAddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddColumns(ObForMySqlParser.AlterByAddColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddIndex(ObForMySqlParser.AlterByAddIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddPrimaryKey(ObForMySqlParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddUniqueKey(ObForMySqlParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddSpecialIndex(ObForMySqlParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddForeignKey(ObForMySqlParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddCheckTableConstraint(ObForMySqlParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterBySetAlgorithm(ObForMySqlParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByChangeDefault(ObForMySqlParser.AlterByChangeDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByChangeColumn(ObForMySqlParser.AlterByChangeColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRenameColumn(ObForMySqlParser.AlterByRenameColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByLock(ObForMySqlParser.AlterByLockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByModifyColumn(ObForMySqlParser.AlterByModifyColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropColumn(ObForMySqlParser.AlterByDropColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropConstraintCheck(ObForMySqlParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropPrimaryKey(ObForMySqlParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropIndex(ObForMySqlParser.AlterByDropIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRenameIndex(ObForMySqlParser.AlterByRenameIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAlterIndexVisibility(ObForMySqlParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropForeignKey(ObForMySqlParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDisableKeys(ObForMySqlParser.AlterByDisableKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByEnableKeys(ObForMySqlParser.AlterByEnableKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRename(ObForMySqlParser.AlterByRenameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByOrder(ObForMySqlParser.AlterByOrderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByConvertCharset(ObForMySqlParser.AlterByConvertCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDefaultCharset(ObForMySqlParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDiscardTablespace(ObForMySqlParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByImportTablespace(ObForMySqlParser.AlterByImportTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByForce(ObForMySqlParser.AlterByForceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByValidate(ObForMySqlParser.AlterByValidateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAddPartition(ObForMySqlParser.AlterByAddPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDropPartition(ObForMySqlParser.AlterByDropPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByDiscardPartition(ObForMySqlParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByImportPartition(ObForMySqlParser.AlterByImportPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByTruncatePartition(ObForMySqlParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByCoalescePartition(ObForMySqlParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByReorganizePartition(ObForMySqlParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByExchangePartition(ObForMySqlParser.AlterByExchangePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByAnalyzePartition(ObForMySqlParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByCheckPartition(ObForMySqlParser.AlterByCheckPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByOptimizePartition(ObForMySqlParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRebuildPartition(ObForMySqlParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRepairPartition(ObForMySqlParser.AlterByRepairPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByRemovePartitioning(ObForMySqlParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link ObForMySqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterByUpgradePartitioning(ObForMySqlParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(ObForMySqlParser.DropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropEvent(ObForMySqlParser.DropEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndex(ObForMySqlParser.DropIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropLogfileGroup(ObForMySqlParser.DropLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropProcedure(ObForMySqlParser.DropProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunction(ObForMySqlParser.DropFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropServer(ObForMySqlParser.DropServerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropTablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTablespace(ObForMySqlParser.DropTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTrigger(ObForMySqlParser.DropTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(ObForMySqlParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#renameTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameTable(ObForMySqlParser.RenameTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#renameTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameTableClause(ObForMySqlParser.RenameTableClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#truncateTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncateTable(ObForMySqlParser.TruncateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#callStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallStatement(ObForMySqlParser.CallStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#procedureArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureArgs(ObForMySqlParser.ProcedureArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#deleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement(ObForMySqlParser.DeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#doStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoStatement(ObForMySqlParser.DoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#handlerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerStatement(ObForMySqlParser.HandlerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#loadDataStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadDataStatement(ObForMySqlParser.LoadDataStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadXmlStatement(ObForMySqlParser.LoadXmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#replaceStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceStatement(ObForMySqlParser.ReplaceStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSelect(ObForMySqlParser.SimpleSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisSelect(ObForMySqlParser.ParenthesisSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionSelect(ObForMySqlParser.UnionSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link ObForMySqlParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionParenthesisSelect(ObForMySqlParser.UnionParenthesisSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#updateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateStatement(ObForMySqlParser.UpdateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#withSelectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithSelectStatement(ObForMySqlParser.WithSelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#withClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithClause(ObForMySqlParser.WithClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#withSelectExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithSelectExpr(ObForMySqlParser.WithSelectExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectInsertValue(ObForMySqlParser.SelectInsertValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link ObForMySqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentInsertValue(ObForMySqlParser.CommentInsertValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#updatedElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdatedElement(ObForMySqlParser.UpdatedElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#assignmentField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentField(ObForMySqlParser.AssignmentFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lockClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockClause(ObForMySqlParser.LockClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleDeleteStatement(ObForMySqlParser.SingleDeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipleDeleteStatement(ObForMySqlParser.MultipleDeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerOpenStatement(ObForMySqlParser.HandlerOpenStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerReadIndexStatement(ObForMySqlParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerReadStatement(ObForMySqlParser.HandlerReadStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerCloseStatement(ObForMySqlParser.HandlerCloseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleUpdateStatement(ObForMySqlParser.SingleUpdateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ignore_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnore_(ObForMySqlParser.Ignore_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipleUpdateStatement(ObForMySqlParser.MultipleUpdateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(ObForMySqlParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#orderByExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByExpression(ObForMySqlParser.OrderByExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tableSources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSources(ObForMySqlParser.TableSourcesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceBase(ObForMySqlParser.TableSourceBaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link ObForMySqlParser#tableSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceNested(ObForMySqlParser.TableSourceNestedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomTableItem(ObForMySqlParser.AtomTableItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryTableItem(ObForMySqlParser.SubqueryTableItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourcesItem(ObForMySqlParser.TableSourcesItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link ObForMySqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceWith(ObForMySqlParser.TableSourceWithContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexHint(ObForMySqlParser.IndexHintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexHintType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexHintType(ObForMySqlParser.IndexHintTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerJoin(ObForMySqlParser.InnerJoinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStraightJoin(ObForMySqlParser.StraightJoinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuterJoin(ObForMySqlParser.OuterJoinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link ObForMySqlParser#joinPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaturalJoin(ObForMySqlParser.NaturalJoinContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#innerJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerJoinType(ObForMySqlParser.InnerJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#outerJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuterJoinType(ObForMySqlParser.OuterJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#naturalJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaturalJoinType(ObForMySqlParser.NaturalJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#queryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpression(ObForMySqlParser.QueryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpressionNointo(ObForMySqlParser.QueryExpressionNointoContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#querySpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuerySpecification(ObForMySqlParser.QuerySpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuerySpecificationNointo(ObForMySqlParser.QuerySpecificationNointoContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#unionParenthesis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionParenthesis(ObForMySqlParser.UnionParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#unionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionStatement(ObForMySqlParser.UnionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#selectElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectElements(ObForMySqlParser.SelectElementsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStarElement(ObForMySqlParser.SelectStarElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectColumnElement(ObForMySqlParser.SelectColumnElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectFunctionElement(ObForMySqlParser.SelectFunctionElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link ObForMySqlParser#selectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpressionElement(ObForMySqlParser.SelectExpressionElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#aliasName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasName(ObForMySqlParser.AliasNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectIntoVariables(ObForMySqlParser.SelectIntoVariablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectIntoDumpFile(ObForMySqlParser.SelectIntoDumpFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link ObForMySqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectIntoTextFile(ObForMySqlParser.SelectIntoTextFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectFieldsInto(ObForMySqlParser.SelectFieldsIntoContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#selectLinesInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectLinesInto(ObForMySqlParser.SelectLinesIntoContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(ObForMySqlParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(ObForMySqlParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#groupClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupClause(ObForMySqlParser.GroupClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(ObForMySqlParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#groupByItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByItem(ObForMySqlParser.GroupByItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(ObForMySqlParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClauseAtom(ObForMySqlParser.LimitClauseAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#startTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartTransaction(ObForMySqlParser.StartTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#beginWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginWork(ObForMySqlParser.BeginWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#commitWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommitWork(ObForMySqlParser.CommitWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#rollbackWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollbackWork(ObForMySqlParser.RollbackWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#savepointStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepointStatement(ObForMySqlParser.SavepointStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#rollbackStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollbackStatement(ObForMySqlParser.RollbackStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#releaseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReleaseStatement(ObForMySqlParser.ReleaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lockTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockTables(ObForMySqlParser.LockTablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#unlockTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlockTables(ObForMySqlParser.UnlockTablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAutocommitStatement(ObForMySqlParser.SetAutocommitStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTransactionStatement(ObForMySqlParser.SetTransactionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#transactionMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionMode(ObForMySqlParser.TransactionModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lockTableElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockTableElement(ObForMySqlParser.LockTableElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lockAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockAction(ObForMySqlParser.LockActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#transactionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionOption(ObForMySqlParser.TransactionOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#transactionLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionLevel(ObForMySqlParser.TransactionLevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#changeMaster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeMaster(ObForMySqlParser.ChangeMasterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeReplicationFilter(ObForMySqlParser.ChangeReplicationFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPurgeBinaryLogs(ObForMySqlParser.PurgeBinaryLogsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#resetMaster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetMaster(ObForMySqlParser.ResetMasterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#resetSlave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetSlave(ObForMySqlParser.ResetSlaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#resetReplica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetReplica(ObForMySqlParser.ResetReplicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#startSlave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartSlave(ObForMySqlParser.StartSlaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#stopSlave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStopSlave(ObForMySqlParser.StopSlaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#startGroupReplication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartGroupReplication(ObForMySqlParser.StartGroupReplicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStopGroupReplication(ObForMySqlParser.StopGroupReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterStringOption(ObForMySqlParser.MasterStringOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterDecimalOption(ObForMySqlParser.MasterDecimalOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterBoolOption(ObForMySqlParser.MasterBoolOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterRealOption(ObForMySqlParser.MasterRealOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link ObForMySqlParser#masterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterUidListOption(ObForMySqlParser.MasterUidListOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#stringMasterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringMasterOption(ObForMySqlParser.StringMasterOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalMasterOption(ObForMySqlParser.DecimalMasterOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#boolMasterOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolMasterOption(ObForMySqlParser.BoolMasterOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#channelOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannelOption(ObForMySqlParser.ChannelOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoDbReplication(ObForMySqlParser.DoDbReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnoreDbReplication(ObForMySqlParser.IgnoreDbReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoTableReplication(ObForMySqlParser.DoTableReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnoreTableReplication(ObForMySqlParser.IgnoreTableReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildDoTableReplication(ObForMySqlParser.WildDoTableReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildIgnoreTableReplication(ObForMySqlParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link ObForMySqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRewriteDbReplication(ObForMySqlParser.RewriteDbReplicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tablePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablePair(ObForMySqlParser.TablePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#threadType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThreadType(ObForMySqlParser.ThreadTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtidsUntilOption(ObForMySqlParser.GtidsUntilOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasterLogUntilOption(ObForMySqlParser.MasterLogUntilOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelayLogUntilOption(ObForMySqlParser.RelayLogUntilOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link ObForMySqlParser#untilOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlGapsUntilOption(ObForMySqlParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserConnectionOption(ObForMySqlParser.UserConnectionOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPasswordConnectionOption(ObForMySqlParser.PasswordConnectionOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultAuthConnectionOption(ObForMySqlParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link ObForMySqlParser#connectionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPluginDirConnectionOption(ObForMySqlParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#gtuidSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtuidSet(ObForMySqlParser.GtuidSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaStartTransaction(ObForMySqlParser.XaStartTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaEndTransaction(ObForMySqlParser.XaEndTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaPrepareStatement(ObForMySqlParser.XaPrepareStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaCommitWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaCommitWork(ObForMySqlParser.XaCommitWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaRollbackWork(ObForMySqlParser.XaRollbackWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXaRecoverWork(ObForMySqlParser.XaRecoverWorkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#prepareStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrepareStatement(ObForMySqlParser.PrepareStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#executeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStatement(ObForMySqlParser.ExecuteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocatePrepare(ObForMySqlParser.DeallocatePrepareContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#routineBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineBody(ObForMySqlParser.RoutineBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(ObForMySqlParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(ObForMySqlParser.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(ObForMySqlParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#iterateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateStatement(ObForMySqlParser.IterateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#leaveStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeaveStatement(ObForMySqlParser.LeaveStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#loopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStatement(ObForMySqlParser.LoopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#repeatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(ObForMySqlParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(ObForMySqlParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(ObForMySqlParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloseCursor(ObForMySqlParser.CloseCursorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetchCursor(ObForMySqlParser.FetchCursorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link ObForMySqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpenCursor(ObForMySqlParser.OpenCursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#declareVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareVariable(ObForMySqlParser.DeclareVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#declareCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareCondition(ObForMySqlParser.DeclareConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#declareCursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareCursor(ObForMySqlParser.DeclareCursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#declareHandler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareHandler(ObForMySqlParser.DeclareHandlerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionCode(ObForMySqlParser.HandlerConditionCodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionState(ObForMySqlParser.HandlerConditionStateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionName(ObForMySqlParser.HandlerConditionNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionWarning(ObForMySqlParser.HandlerConditionWarningContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionNotfound(ObForMySqlParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link ObForMySqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerConditionException(ObForMySqlParser.HandlerConditionExceptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureSqlStatement(ObForMySqlParser.ProcedureSqlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#caseAlternative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseAlternative(ObForMySqlParser.CaseAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#elifAlternative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElifAlternative(ObForMySqlParser.ElifAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUserMysqlV56(ObForMySqlParser.AlterUserMysqlV56Context ctx);
	/**
	 * Visit a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link ObForMySqlParser#alterUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUserMysqlV57(ObForMySqlParser.AlterUserMysqlV57Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUser(ObForMySqlParser.CreateUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropUser(ObForMySqlParser.DropUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dropRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRole(ObForMySqlParser.DropRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#grantStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantStatement(ObForMySqlParser.GrantStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#roleOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleOption(ObForMySqlParser.RoleOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#grantProxy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantProxy(ObForMySqlParser.GrantProxyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#renameUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameUser(ObForMySqlParser.RenameUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeStatement(ObForMySqlParser.RevokeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#revokeProxy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeProxy(ObForMySqlParser.RevokeProxyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetPasswordStatement(ObForMySqlParser.SetPasswordStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserSpecification(ObForMySqlParser.UserSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHashAuthOption(ObForMySqlParser.HashAuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringAuthOption(ObForMySqlParser.StringAuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleAuthOption(ObForMySqlParser.ModuleAuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link ObForMySqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleAuthOption(ObForMySqlParser.SimpleAuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code module}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(ObForMySqlParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link ObForMySqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPasswordModuleOption(ObForMySqlParser.PasswordModuleOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tlsOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTlsOption(ObForMySqlParser.TlsOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userResourceOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserResourceOption(ObForMySqlParser.UserResourceOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userPasswordOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserPasswordOption(ObForMySqlParser.UserPasswordOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userLockOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserLockOption(ObForMySqlParser.UserLockOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#privelegeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivelegeClause(ObForMySqlParser.PrivelegeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(ObForMySqlParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentSchemaPriviLevel(ObForMySqlParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalPrivLevel(ObForMySqlParser.GlobalPrivLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiniteSchemaPrivLevel(ObForMySqlParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiniteFullTablePrivLevel(ObForMySqlParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiniteFullTablePrivLevel2(ObForMySqlParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link ObForMySqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiniteTablePrivLevel(ObForMySqlParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#renameUserClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameUserClause(ObForMySqlParser.RenameUserClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#analyzeTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzeTable(ObForMySqlParser.AnalyzeTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#checkTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckTable(ObForMySqlParser.CheckTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#checksumTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChecksumTable(ObForMySqlParser.ChecksumTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#optimizeTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptimizeTable(ObForMySqlParser.OptimizeTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#repairTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepairTable(ObForMySqlParser.RepairTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#checkTableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckTableOption(ObForMySqlParser.CheckTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#createUdfFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUdfFunction(ObForMySqlParser.CreateUdfFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#installPlugin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstallPlugin(ObForMySqlParser.InstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninstallPlugin(ObForMySqlParser.UninstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetVariable(ObForMySqlParser.SetVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCharset(ObForMySqlParser.SetCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNames(ObForMySqlParser.SetNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetPassword(ObForMySqlParser.SetPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTransaction(ObForMySqlParser.SetTransactionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAutocommit(ObForMySqlParser.SetAutocommitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link ObForMySqlParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNewValueInsideTrigger(ObForMySqlParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowMasterLogs(ObForMySqlParser.ShowMasterLogsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCharset(ObForMySqlParser.ShowCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLogEvents(ObForMySqlParser.ShowLogEventsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowObjectFilter(ObForMySqlParser.ShowObjectFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumns(ObForMySqlParser.ShowColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(ObForMySqlParser.ShowTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateDb(ObForMySqlParser.ShowCreateDbContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateFullIdObject(ObForMySqlParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateUser(ObForMySqlParser.ShowCreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEngine(ObForMySqlParser.ShowEngineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEngines(ObForMySqlParser.ShowEnginesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStatus(ObForMySqlParser.ShowStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPlugins(ObForMySqlParser.ShowPluginsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPrivileges(ObForMySqlParser.ShowPrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcessList(ObForMySqlParser.ShowProcessListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfiles(ObForMySqlParser.ShowProfilesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSlaveHosts(ObForMySqlParser.ShowSlaveHostsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAuthros(ObForMySqlParser.ShowAuthrosContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowContributors(ObForMySqlParser.ShowContributorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowErrors(ObForMySqlParser.ShowErrorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCountErrors(ObForMySqlParser.ShowCountErrorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSchemaFilter(ObForMySqlParser.ShowSchemaFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRoutine(ObForMySqlParser.ShowRoutineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrants(ObForMySqlParser.ShowGrantsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexes(ObForMySqlParser.ShowIndexesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowOpenTables(ObForMySqlParser.ShowOpenTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfile(ObForMySqlParser.ShowProfileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSlaveStatus(ObForMySqlParser.ShowSlaveStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link ObForMySqlParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowReplicaStatus(ObForMySqlParser.ShowReplicaStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#variableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableClause(ObForMySqlParser.VariableClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#showCommonEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCommonEntity(ObForMySqlParser.ShowCommonEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#showFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFilter(ObForMySqlParser.ShowFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGlobalInfoClause(ObForMySqlParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSchemaEntity(ObForMySqlParser.ShowSchemaEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#showProfileType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfileType(ObForMySqlParser.ShowProfileTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#binlogStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinlogStatement(ObForMySqlParser.BinlogStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCacheIndexStatement(ObForMySqlParser.CacheIndexStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#flushStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlushStatement(ObForMySqlParser.FlushStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#killStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillStatement(ObForMySqlParser.KillStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadIndexIntoCache(ObForMySqlParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#resetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetStatement(ObForMySqlParser.ResetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#shutdownStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShutdownStatement(ObForMySqlParser.ShutdownStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tableIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIndexes(ObForMySqlParser.TableIndexesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleFlushOption(ObForMySqlParser.SimpleFlushOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannelFlushOption(ObForMySqlParser.ChannelFlushOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link ObForMySqlParser#flushOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableFlushOption(ObForMySqlParser.TableFlushOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#flushTableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlushTableOption(ObForMySqlParser.FlushTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadedTableIndexes(ObForMySqlParser.LoadedTableIndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleDescribeStatement(ObForMySqlParser.SimpleDescribeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullDescribeStatement(ObForMySqlParser.FullDescribeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#helpStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHelpStatement(ObForMySqlParser.HelpStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#useStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseStatement(ObForMySqlParser.UseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#signalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalStatement(ObForMySqlParser.SignalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#resignalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResignalStatement(ObForMySqlParser.ResignalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalConditionInformation(ObForMySqlParser.SignalConditionInformationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiagnosticsStatement(ObForMySqlParser.DiagnosticsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiagnosticsConditionInformationName(ObForMySqlParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeStatements(ObForMySqlParser.DescribeStatementsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link ObForMySqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeConnection(ObForMySqlParser.DescribeConnectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#fullId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullId(ObForMySqlParser.FullIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(ObForMySqlParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#procName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcName(ObForMySqlParser.ProcNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#customFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomFunctionName(ObForMySqlParser.CustomFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#roleName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleName(ObForMySqlParser.RoleNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#fullColumnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnName(ObForMySqlParser.FullColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexColumnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexColumnName(ObForMySqlParser.IndexColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserName(ObForMySqlParser.UserNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#mysqlVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMysqlVariable(ObForMySqlParser.MysqlVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#charsetName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharsetName(ObForMySqlParser.CharsetNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#collationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollationName(ObForMySqlParser.CollationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#engineName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEngineName(ObForMySqlParser.EngineNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#uuidSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUuidSet(ObForMySqlParser.UuidSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXid(ObForMySqlParser.XidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#xuidStringId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXuidStringId(ObForMySqlParser.XuidStringIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#authPlugin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuthPlugin(ObForMySqlParser.AuthPluginContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#uid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUid(ObForMySqlParser.UidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#simpleId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleId(ObForMySqlParser.SimpleIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dottedId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDottedId(ObForMySqlParser.DottedIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#decimalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(ObForMySqlParser.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileSizeLiteral(ObForMySqlParser.FileSizeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(ObForMySqlParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(ObForMySqlParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexadecimalLiteral(ObForMySqlParser.HexadecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#nullNotnull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullNotnull(ObForMySqlParser.NullNotnullContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(ObForMySqlParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringDataType(ObForMySqlParser.StringDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNationalStringDataType(ObForMySqlParser.NationalStringDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNationalVaryingStringDataType(ObForMySqlParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensionDataType(ObForMySqlParser.DimensionDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleDataType(ObForMySqlParser.SimpleDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionDataType(ObForMySqlParser.CollectionDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatialDataType(ObForMySqlParser.SpatialDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongVarcharDataType(ObForMySqlParser.LongVarcharDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link ObForMySqlParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongVarbinaryDataType(ObForMySqlParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#collectionOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOptions(ObForMySqlParser.CollectionOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#collectionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOption(ObForMySqlParser.CollectionOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#convertedDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvertedDataType(ObForMySqlParser.ConvertedDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthOneDimension(ObForMySqlParser.LengthOneDimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthTwoDimension(ObForMySqlParser.LengthTwoDimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthTwoOptionalDimension(ObForMySqlParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#uidList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUidList(ObForMySqlParser.UidListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#tables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTables(ObForMySqlParser.TablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#indexColumnNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexColumnNames(ObForMySqlParser.IndexColumnNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressions(ObForMySqlParser.ExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionsWithDefaults(ObForMySqlParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#constants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstants(ObForMySqlParser.ConstantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#simpleStrings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStrings(ObForMySqlParser.SimpleStringsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#userVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserVariables(ObForMySqlParser.UserVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#defaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultValue(ObForMySqlParser.DefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#currentTimestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentTimestamp(ObForMySqlParser.CurrentTimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionOrDefault(ObForMySqlParser.ExpressionOrDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ifExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExists(ObForMySqlParser.IfExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#ifNotExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfNotExists(ObForMySqlParser.IfNotExistsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecificFunctionCall(ObForMySqlParser.SpecificFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateFunctionCall(ObForMySqlParser.AggregateFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFunctionCall(ObForMySqlParser.ScalarFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdfFunctionCall(ObForMySqlParser.UdfFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPasswordFunctionCall(ObForMySqlParser.PasswordFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonAggregateFunctionCall(ObForMySqlParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonAggregateFunction(ObForMySqlParser.NonAggregateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#overClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverClause(ObForMySqlParser.OverClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#window_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_specification(ObForMySqlParser.Window_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#frame_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_clause(ObForMySqlParser.Frame_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#frame_extent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_extent(ObForMySqlParser.Frame_extentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_start_or_end(ObForMySqlParser.Frame_start_or_endContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleFunctionCall(ObForMySqlParser.SimpleFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialTimeCall(ObForMySqlParser.SpecialTimeCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeFunctionCall(ObForMySqlParser.DataTypeFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesFunctionCall(ObForMySqlParser.ValuesFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseFunctionCall(ObForMySqlParser.CaseFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharFunctionCall(ObForMySqlParser.CharFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionFunctionCall(ObForMySqlParser.PositionFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstrFunctionCall(ObForMySqlParser.SubstrFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrimFunctionCall(ObForMySqlParser.TrimFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWeightFunctionCall(ObForMySqlParser.WeightFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractFunctionCall(ObForMySqlParser.ExtractFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetFormatFunctionCall(ObForMySqlParser.GetFormatFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link ObForMySqlParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonValueFunctionCall(ObForMySqlParser.JsonValueFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseFuncAlternative(ObForMySqlParser.CaseFuncAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevelWeightList(ObForMySqlParser.LevelWeightListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link ObForMySqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevelWeightRange(ObForMySqlParser.LevelWeightRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevelInWeightListElement(ObForMySqlParser.LevelInWeightListElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#aggregateFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateFunction(ObForMySqlParser.AggregateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFunctionName(ObForMySqlParser.ScalarFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPasswordFunctionClause(ObForMySqlParser.PasswordFunctionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#functionArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgs(ObForMySqlParser.FunctionArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(ObForMySqlParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsExpression(ObForMySqlParser.IsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(ObForMySqlParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(ObForMySqlParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link ObForMySqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateExpression(ObForMySqlParser.PredicateExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSoundsLikePredicate(ObForMySqlParser.SoundsLikePredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAtomPredicate(ObForMySqlParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonMemberOfPredicate(ObForMySqlParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPredicate(ObForMySqlParser.InPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryComparasionPredicate(ObForMySqlParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullSearchPredicate(ObForMySqlParser.FullSearchPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenPredicate(ObForMySqlParser.BetweenPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryComparasionPredicate(ObForMySqlParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullPredicate(ObForMySqlParser.IsNullPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLikePredicate(ObForMySqlParser.LikePredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link ObForMySqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexpPredicate(ObForMySqlParser.RegexpPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#search_modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_modifier(ObForMySqlParser.Search_modifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpressionAtom(ObForMySqlParser.UnaryExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollateExpressionAtom(ObForMySqlParser.CollateExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryExpessionAtom(ObForMySqlParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMysqlVariableExpressionAtom(ObForMySqlParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpressionAtom(ObForMySqlParser.NestedExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedRowExpressionAtom(ObForMySqlParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExpressionAtom(ObForMySqlParser.MathExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalExpressionAtom(ObForMySqlParser.IntervalExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonExpressionAtom(ObForMySqlParser.JsonExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsExpessionAtom(ObForMySqlParser.ExistsExpessionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpressionAtom(ObForMySqlParser.ConstantExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpressionAtom(ObForMySqlParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpressionAtom(ObForMySqlParser.BinaryExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnNameExpressionAtom(ObForMySqlParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link ObForMySqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitExpressionAtom(ObForMySqlParser.BitExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(ObForMySqlParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(ObForMySqlParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperator(ObForMySqlParser.LogicalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#bitOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOperator(ObForMySqlParser.BitOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#mathOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathOperator(ObForMySqlParser.MathOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#jsonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonOperator(ObForMySqlParser.JsonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#charsetNameBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharsetNameBase(ObForMySqlParser.CharsetNameBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionLevelBase(ObForMySqlParser.TransactionLevelBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#privilegesBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilegesBase(ObForMySqlParser.PrivilegesBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalTypeBase(ObForMySqlParser.IntervalTypeBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#dataTypeBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeBase(ObForMySqlParser.DataTypeBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordsCanBeId(ObForMySqlParser.KeywordsCanBeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForMySqlParser#functionNameBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionNameBase(ObForMySqlParser.FunctionNameBaseContext ctx);
}