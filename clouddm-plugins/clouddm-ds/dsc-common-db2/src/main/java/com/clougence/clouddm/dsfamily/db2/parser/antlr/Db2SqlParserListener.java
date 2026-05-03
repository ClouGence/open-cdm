// Generated from Db2SqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.dsfamily.db2.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Db2SqlParser}.
 */
public interface Db2SqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(Db2SqlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(Db2SqlParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatements(Db2SqlParser.SqlStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatements(Db2SqlParser.SqlStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(Db2SqlParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(Db2SqlParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(Db2SqlParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(Db2SqlParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdlStatement(Db2SqlParser.DdlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdlStatement(Db2SqlParser.DdlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(Db2SqlParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(Db2SqlParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(Db2SqlParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(Db2SqlParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void enterAdministrationStatement(Db2SqlParser.AdministrationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void exitAdministrationStatement(Db2SqlParser.AdministrationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(Db2SqlParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(Db2SqlParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(Db2SqlParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(Db2SqlParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(Db2SqlParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(Db2SqlParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateLogfileGroup(Db2SqlParser.CreateLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateLogfileGroup(Db2SqlParser.CreateLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(Db2SqlParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(Db2SqlParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(Db2SqlParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(Db2SqlParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(Db2SqlParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(Db2SqlParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createServer}.
	 * @param ctx the parse tree
	 */
	void enterCreateServer(Db2SqlParser.CreateServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createServer}.
	 * @param ctx the parse tree
	 */
	void exitCreateServer(Db2SqlParser.CreateServerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCopyCreateTable(Db2SqlParser.CopyCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCopyCreateTable(Db2SqlParser.CopyCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterQueryCreateTable(Db2SqlParser.QueryCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitQueryCreateTable(Db2SqlParser.QueryCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterColumnCreateTable(Db2SqlParser.ColumnCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitColumnCreateTable(Db2SqlParser.ColumnCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryAsCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterQueryAsCreateTable(Db2SqlParser.QueryAsCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryAsCreateTable}
	 * labeled alternative in {@link Db2SqlParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitQueryAsCreateTable(Db2SqlParser.QueryAsCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceInnodb(Db2SqlParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceInnodb(Db2SqlParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceNdb(Db2SqlParser.CreateTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceNdb(Db2SqlParser.CreateTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateTrigger(Db2SqlParser.CreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateTrigger(Db2SqlParser.CreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createView}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(Db2SqlParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createView}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(Db2SqlParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabaseOption(Db2SqlParser.CreateDatabaseOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabaseOption(Db2SqlParser.CreateDatabaseOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void enterOwnerStatement(Db2SqlParser.OwnerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void exitOwnerStatement(Db2SqlParser.OwnerStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link Db2SqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreciseSchedule(Db2SqlParser.PreciseScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link Db2SqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreciseSchedule(Db2SqlParser.PreciseScheduleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link Db2SqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalSchedule(Db2SqlParser.IntervalScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link Db2SqlParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalSchedule(Db2SqlParser.IntervalScheduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(Db2SqlParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(Db2SqlParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpr(Db2SqlParser.IntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpr(Db2SqlParser.IntervalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void enterIntervalType(Db2SqlParser.IntervalTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void exitIntervalType(Db2SqlParser.IntervalTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#enableType}.
	 * @param ctx the parse tree
	 */
	void enterEnableType(Db2SqlParser.EnableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#enableType}.
	 * @param ctx the parse tree
	 */
	void exitEnableType(Db2SqlParser.EnableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexType}.
	 * @param ctx the parse tree
	 */
	void enterIndexType(Db2SqlParser.IndexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexType}.
	 * @param ctx the parse tree
	 */
	void exitIndexType(Db2SqlParser.IndexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(Db2SqlParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(Db2SqlParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParameter(Db2SqlParser.ProcedureParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParameter(Db2SqlParser.ProcedureParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParameter(Db2SqlParser.FunctionParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParameter(Db2SqlParser.FunctionParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineComment(Db2SqlParser.RoutineCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineComment(Db2SqlParser.RoutineCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineLanguage(Db2SqlParser.RoutineLanguageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineLanguage(Db2SqlParser.RoutineLanguageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBehavior(Db2SqlParser.RoutineBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBehavior(Db2SqlParser.RoutineBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineData(Db2SqlParser.RoutineDataContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineData(Db2SqlParser.RoutineDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineSecurity(Db2SqlParser.RoutineSecurityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link Db2SqlParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineSecurity(Db2SqlParser.RoutineSecurityContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void enterServerOption(Db2SqlParser.ServerOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void exitServerOption(Db2SqlParser.ServerOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterCreateDefinitions(Db2SqlParser.CreateDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitCreateDefinitions(Db2SqlParser.CreateDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDeclaration(Db2SqlParser.ColumnDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDeclaration(Db2SqlParser.ColumnDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDeclaration(Db2SqlParser.ConstraintDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDeclaration(Db2SqlParser.ConstraintDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterIndexDeclaration(Db2SqlParser.IndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitIndexDeclaration(Db2SqlParser.IndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(Db2SqlParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(Db2SqlParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterColumnProperties}.
	 * @param ctx the parse tree
	 */
	void enterAlterColumnProperties(Db2SqlParser.AlterColumnPropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterColumnProperties}.
	 * @param ctx the parse tree
	 */
	void exitAlterColumnProperties(Db2SqlParser.AlterColumnPropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterNullColumnConstraint(Db2SqlParser.NullColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitNullColumnConstraint(Db2SqlParser.NullColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterDefaultColumnConstraint(Db2SqlParser.DefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitDefaultColumnConstraint(Db2SqlParser.DefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterInvisibleColumnConstraint(Db2SqlParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitInvisibleColumnConstraint(Db2SqlParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterAutoIncrementColumnConstraint(Db2SqlParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitAutoIncrementColumnConstraint(Db2SqlParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyColumnConstraint(Db2SqlParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyColumnConstraint(Db2SqlParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyColumnConstraint(Db2SqlParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyColumnConstraint(Db2SqlParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumnConstraint(Db2SqlParser.CommentColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumnConstraint(Db2SqlParser.CommentColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterFormatColumnConstraint(Db2SqlParser.FormatColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitFormatColumnConstraint(Db2SqlParser.FormatColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterStorageColumnConstraint(Db2SqlParser.StorageColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitStorageColumnConstraint(Db2SqlParser.StorageColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterReferenceColumnConstraint(Db2SqlParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitReferenceColumnConstraint(Db2SqlParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCollateColumnConstraint(Db2SqlParser.CollateColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCollateColumnConstraint(Db2SqlParser.CollateColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterGeneratedColumnConstraint(Db2SqlParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitGeneratedColumnConstraint(Db2SqlParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterSerialDefaultColumnConstraint(Db2SqlParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitSerialDefaultColumnConstraint(Db2SqlParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckColumnConstraint(Db2SqlParser.CheckColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link Db2SqlParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckColumnConstraint(Db2SqlParser.CheckColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyTableConstraint(Db2SqlParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyTableConstraint(Db2SqlParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyTableConstraint(Db2SqlParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyTableConstraint(Db2SqlParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyTableConstraint(Db2SqlParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyTableConstraint(Db2SqlParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableConstraint(Db2SqlParser.CheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableConstraint(Db2SqlParser.CheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDefinition(Db2SqlParser.ReferenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDefinition(Db2SqlParser.ReferenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAction(Db2SqlParser.ReferenceActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAction(Db2SqlParser.ReferenceActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceControlType(Db2SqlParser.ReferenceControlTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceControlType(Db2SqlParser.ReferenceControlTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIndexDeclaration(Db2SqlParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIndexDeclaration(Db2SqlParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSpecialIndexDeclaration(Db2SqlParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link Db2SqlParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSpecialIndexDeclaration(Db2SqlParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEngine(Db2SqlParser.TableOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEngine(Db2SqlParser.TableOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAutoIncrement(Db2SqlParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAutoIncrement(Db2SqlParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAverage(Db2SqlParser.TableOptionAverageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAverage(Db2SqlParser.TableOptionAverageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCharset(Db2SqlParser.TableOptionCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCharset(Db2SqlParser.TableOptionCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionChecksum(Db2SqlParser.TableOptionChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionChecksum(Db2SqlParser.TableOptionChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCollate(Db2SqlParser.TableOptionCollateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCollate(Db2SqlParser.TableOptionCollateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionComment(Db2SqlParser.TableOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionComment(Db2SqlParser.TableOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCompression(Db2SqlParser.TableOptionCompressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCompression(Db2SqlParser.TableOptionCompressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionConnection(Db2SqlParser.TableOptionConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionConnection(Db2SqlParser.TableOptionConnectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDataDirectory(Db2SqlParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDataDirectory(Db2SqlParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDelay(Db2SqlParser.TableOptionDelayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDelay(Db2SqlParser.TableOptionDelayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEncryption(Db2SqlParser.TableOptionEncryptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEncryption(Db2SqlParser.TableOptionEncryptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionIndexDirectory(Db2SqlParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionIndexDirectory(Db2SqlParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionInsertMethod(Db2SqlParser.TableOptionInsertMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionInsertMethod(Db2SqlParser.TableOptionInsertMethodContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionKeyBlockSize(Db2SqlParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionKeyBlockSize(Db2SqlParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMaxRows(Db2SqlParser.TableOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMaxRows(Db2SqlParser.TableOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMinRows(Db2SqlParser.TableOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMinRows(Db2SqlParser.TableOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPackKeys(Db2SqlParser.TableOptionPackKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPackKeys(Db2SqlParser.TableOptionPackKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPassword(Db2SqlParser.TableOptionPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPassword(Db2SqlParser.TableOptionPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRowFormat(Db2SqlParser.TableOptionRowFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRowFormat(Db2SqlParser.TableOptionRowFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRecalculation(Db2SqlParser.TableOptionRecalculationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRecalculation(Db2SqlParser.TableOptionRecalculationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPersistent(Db2SqlParser.TableOptionPersistentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPersistent(Db2SqlParser.TableOptionPersistentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionSamplePage(Db2SqlParser.TableOptionSamplePageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionSamplePage(Db2SqlParser.TableOptionSamplePageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTablespace(Db2SqlParser.TableOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTablespace(Db2SqlParser.TableOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTableType(Db2SqlParser.TableOptionTableTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTableType(Db2SqlParser.TableOptionTableTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTransactional(Db2SqlParser.TableOptionTransactionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTransactional(Db2SqlParser.TableOptionTransactionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionUnion(Db2SqlParser.TableOptionUnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link Db2SqlParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionUnion(Db2SqlParser.TableOptionUnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tableType}.
	 * @param ctx the parse tree
	 */
	void enterTableType(Db2SqlParser.TableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tableType}.
	 * @param ctx the parse tree
	 */
	void exitTableType(Db2SqlParser.TableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void enterTablespaceStorage(Db2SqlParser.TablespaceStorageContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void exitTablespaceStorage(Db2SqlParser.TablespaceStorageContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitions(Db2SqlParser.PartitionDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitions(Db2SqlParser.PartitionDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionHash(Db2SqlParser.PartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionHash(Db2SqlParser.PartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionKey(Db2SqlParser.PartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionKey(Db2SqlParser.PartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionRange(Db2SqlParser.PartitionFunctionRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionRange(Db2SqlParser.PartitionFunctionRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionList(Db2SqlParser.PartitionFunctionListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link Db2SqlParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionList(Db2SqlParser.PartitionFunctionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link Db2SqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionHash(Db2SqlParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link Db2SqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionHash(Db2SqlParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link Db2SqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionKey(Db2SqlParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link Db2SqlParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionKey(Db2SqlParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionComparision(Db2SqlParser.PartitionComparisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionComparision(Db2SqlParser.PartitionComparisionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListAtom(Db2SqlParser.PartitionListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListAtom(Db2SqlParser.PartitionListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListVector(Db2SqlParser.PartitionListVectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListVector(Db2SqlParser.PartitionListVectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSimple(Db2SqlParser.PartitionSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link Db2SqlParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSimple(Db2SqlParser.PartitionSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerAtom(Db2SqlParser.PartitionDefinerAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerAtom(Db2SqlParser.PartitionDefinerAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerVector(Db2SqlParser.PartitionDefinerVectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerVector(Db2SqlParser.PartitionDefinerVectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubpartitionDefinition(Db2SqlParser.SubpartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubpartitionDefinition(Db2SqlParser.SubpartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionEngine(Db2SqlParser.PartitionOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionEngine(Db2SqlParser.PartitionOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionComment(Db2SqlParser.PartitionOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionComment(Db2SqlParser.PartitionOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionDataDirectory(Db2SqlParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionDataDirectory(Db2SqlParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionIndexDirectory(Db2SqlParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionIndexDirectory(Db2SqlParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMaxRows(Db2SqlParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMaxRows(Db2SqlParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMinRows(Db2SqlParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMinRows(Db2SqlParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionTablespace(Db2SqlParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionTablespace(Db2SqlParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionNodeGroup(Db2SqlParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link Db2SqlParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionNodeGroup(Db2SqlParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link Db2SqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterSimpleDatabase(Db2SqlParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link Db2SqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterSimpleDatabase(Db2SqlParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link Db2SqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterUpgradeName(Db2SqlParser.AlterUpgradeNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link Db2SqlParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterUpgradeName(Db2SqlParser.AlterUpgradeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void enterAlterEvent(Db2SqlParser.AlterEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void exitAlterEvent(Db2SqlParser.AlterEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterFunction(Db2SqlParser.AlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterFunction(Db2SqlParser.AlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void enterAlterInstance(Db2SqlParser.AlterInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void exitAlterInstance(Db2SqlParser.AlterInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterLogfileGroup(Db2SqlParser.AlterLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterLogfileGroup(Db2SqlParser.AlterLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterAlterProcedure(Db2SqlParser.AlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitAlterProcedure(Db2SqlParser.AlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void enterAlterServer(Db2SqlParser.AlterServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void exitAlterServer(Db2SqlParser.AlterServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(Db2SqlParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(Db2SqlParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespace(Db2SqlParser.AlterTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespace(Db2SqlParser.AlterTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#alterView}.
	 * @param ctx the parse tree
	 */
	void enterAlterView(Db2SqlParser.AlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#alterView}.
	 * @param ctx the parse tree
	 */
	void exitAlterView(Db2SqlParser.AlterViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTableOption(Db2SqlParser.AlterByTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTableOption(Db2SqlParser.AlterByTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumn(Db2SqlParser.AlterByAddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumn(Db2SqlParser.AlterByAddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumns(Db2SqlParser.AlterByAddColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumns(Db2SqlParser.AlterByAddColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddIndex(Db2SqlParser.AlterByAddIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddIndex(Db2SqlParser.AlterByAddIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPrimaryKey(Db2SqlParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPrimaryKey(Db2SqlParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddUniqueKey(Db2SqlParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddUniqueKey(Db2SqlParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddSpecialIndex(Db2SqlParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddSpecialIndex(Db2SqlParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddForeignKey(Db2SqlParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddForeignKey(Db2SqlParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddCheckTableConstraint(Db2SqlParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddCheckTableConstraint(Db2SqlParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterBySetAlgorithm(Db2SqlParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterBySetAlgorithm(Db2SqlParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeDefault(Db2SqlParser.AlterByChangeDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeDefault(Db2SqlParser.AlterByChangeDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeColumn(Db2SqlParser.AlterByChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeColumn(Db2SqlParser.AlterByChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameColumn(Db2SqlParser.AlterByRenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameColumn(Db2SqlParser.AlterByRenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByLock(Db2SqlParser.AlterByLockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByLock(Db2SqlParser.AlterByLockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByModifyColumn(Db2SqlParser.AlterByModifyColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByModifyColumn(Db2SqlParser.AlterByModifyColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropColumn(Db2SqlParser.AlterByDropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropColumn(Db2SqlParser.AlterByDropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropConstraintCheck(Db2SqlParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropConstraintCheck(Db2SqlParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPrimaryKey(Db2SqlParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPrimaryKey(Db2SqlParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropIndex(Db2SqlParser.AlterByDropIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropIndex(Db2SqlParser.AlterByDropIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameIndex(Db2SqlParser.AlterByRenameIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameIndex(Db2SqlParser.AlterByRenameIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAlterIndexVisibility(Db2SqlParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAlterIndexVisibility(Db2SqlParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropForeignKey(Db2SqlParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropForeignKey(Db2SqlParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDisableKeys(Db2SqlParser.AlterByDisableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDisableKeys(Db2SqlParser.AlterByDisableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByEnableKeys(Db2SqlParser.AlterByEnableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByEnableKeys(Db2SqlParser.AlterByEnableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRename(Db2SqlParser.AlterByRenameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRename(Db2SqlParser.AlterByRenameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOrder(Db2SqlParser.AlterByOrderContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOrder(Db2SqlParser.AlterByOrderContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByConvertCharset(Db2SqlParser.AlterByConvertCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByConvertCharset(Db2SqlParser.AlterByConvertCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDefaultCharset(Db2SqlParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDefaultCharset(Db2SqlParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardTablespace(Db2SqlParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardTablespace(Db2SqlParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportTablespace(Db2SqlParser.AlterByImportTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportTablespace(Db2SqlParser.AlterByImportTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByForce(Db2SqlParser.AlterByForceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByForce(Db2SqlParser.AlterByForceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByValidate(Db2SqlParser.AlterByValidateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByValidate(Db2SqlParser.AlterByValidateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPartition(Db2SqlParser.AlterByAddPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPartition(Db2SqlParser.AlterByAddPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPartition(Db2SqlParser.AlterByDropPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPartition(Db2SqlParser.AlterByDropPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardPartition(Db2SqlParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardPartition(Db2SqlParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportPartition(Db2SqlParser.AlterByImportPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportPartition(Db2SqlParser.AlterByImportPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTruncatePartition(Db2SqlParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTruncatePartition(Db2SqlParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCoalescePartition(Db2SqlParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCoalescePartition(Db2SqlParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByReorganizePartition(Db2SqlParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByReorganizePartition(Db2SqlParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByExchangePartition(Db2SqlParser.AlterByExchangePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByExchangePartition(Db2SqlParser.AlterByExchangePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAnalyzePartition(Db2SqlParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAnalyzePartition(Db2SqlParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCheckPartition(Db2SqlParser.AlterByCheckPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCheckPartition(Db2SqlParser.AlterByCheckPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOptimizePartition(Db2SqlParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOptimizePartition(Db2SqlParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRebuildPartition(Db2SqlParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRebuildPartition(Db2SqlParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRepairPartition(Db2SqlParser.AlterByRepairPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRepairPartition(Db2SqlParser.AlterByRepairPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRemovePartitioning(Db2SqlParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRemovePartitioning(Db2SqlParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByUpgradePartitioning(Db2SqlParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByUpgradePartitioning(Db2SqlParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterDropIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterDropIndex(Db2SqlParser.AlterDropIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterDropIndex}
	 * labeled alternative in {@link Db2SqlParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterDropIndex(Db2SqlParser.AlterDropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(Db2SqlParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(Db2SqlParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(Db2SqlParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(Db2SqlParser.DropEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(Db2SqlParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(Db2SqlParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropLogfileGroup(Db2SqlParser.DropLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropLogfileGroup(Db2SqlParser.DropLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(Db2SqlParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(Db2SqlParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(Db2SqlParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(Db2SqlParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void enterDropServer(Db2SqlParser.DropServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void exitDropServer(Db2SqlParser.DropServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(Db2SqlParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(Db2SqlParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void enterDropTablespace(Db2SqlParser.DropTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void exitDropTablespace(Db2SqlParser.DropTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(Db2SqlParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(Db2SqlParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(Db2SqlParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(Db2SqlParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(Db2SqlParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(Db2SqlParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameTableClause(Db2SqlParser.RenameTableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameTableClause(Db2SqlParser.RenameTableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(Db2SqlParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(Db2SqlParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallStatement(Db2SqlParser.CallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallStatement(Db2SqlParser.CallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(Db2SqlParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(Db2SqlParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(Db2SqlParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(Db2SqlParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(Db2SqlParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(Db2SqlParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(Db2SqlParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(Db2SqlParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadDataStatement(Db2SqlParser.LoadDataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadDataStatement(Db2SqlParser.LoadDataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadXmlStatement(Db2SqlParser.LoadXmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadXmlStatement(Db2SqlParser.LoadXmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplaceStatement(Db2SqlParser.ReplaceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplaceStatement(Db2SqlParser.ReplaceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelect(Db2SqlParser.SimpleSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelect(Db2SqlParser.SimpleSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisSelect(Db2SqlParser.ParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisSelect(Db2SqlParser.ParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelect(Db2SqlParser.UnionSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelect(Db2SqlParser.UnionSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesisSelect(Db2SqlParser.UnionParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesisSelect(Db2SqlParser.UnionParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code version}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterVersion(Db2SqlParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code version}
	 * labeled alternative in {@link Db2SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitVersion(Db2SqlParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(Db2SqlParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(Db2SqlParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectStatement(Db2SqlParser.WithSelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectStatement(Db2SqlParser.WithSelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatementValue(Db2SqlParser.InsertStatementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatementValue(Db2SqlParser.InsertStatementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void enterUpdatedElement(Db2SqlParser.UpdatedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void exitUpdatedElement(Db2SqlParser.UpdatedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentField(Db2SqlParser.AssignmentFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentField(Db2SqlParser.AssignmentFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void enterLockClause(Db2SqlParser.LockClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void exitLockClause(Db2SqlParser.LockClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleDeleteStatement(Db2SqlParser.SingleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleDeleteStatement(Db2SqlParser.SingleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleDeleteStatement(Db2SqlParser.MultipleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleDeleteStatement(Db2SqlParser.MultipleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerOpenStatement(Db2SqlParser.HandlerOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerOpenStatement(Db2SqlParser.HandlerOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadIndexStatement(Db2SqlParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadIndexStatement(Db2SqlParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadStatement(Db2SqlParser.HandlerReadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadStatement(Db2SqlParser.HandlerReadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerCloseStatement(Db2SqlParser.HandlerCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerCloseStatement(Db2SqlParser.HandlerCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleUpdateStatement(Db2SqlParser.SingleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleUpdateStatement(Db2SqlParser.SingleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleUpdateStatement(Db2SqlParser.MultipleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleUpdateStatement(Db2SqlParser.MultipleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(Db2SqlParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(Db2SqlParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(Db2SqlParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(Db2SqlParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void enterTableSources(Db2SqlParser.TableSourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void exitTableSources(Db2SqlParser.TableSourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link Db2SqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceBase(Db2SqlParser.TableSourceBaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link Db2SqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceBase(Db2SqlParser.TableSourceBaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link Db2SqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceNested(Db2SqlParser.TableSourceNestedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link Db2SqlParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceNested(Db2SqlParser.TableSourceNestedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterAtomTableItem(Db2SqlParser.AtomTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitAtomTableItem(Db2SqlParser.AtomTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryTableItem(Db2SqlParser.SubqueryTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryTableItem(Db2SqlParser.SubqueryTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourcesItem(Db2SqlParser.TableSourcesItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourcesItem(Db2SqlParser.TableSourcesItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceWith(Db2SqlParser.TableSourceWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link Db2SqlParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceWith(Db2SqlParser.TableSourceWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void enterIndexHint(Db2SqlParser.IndexHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void exitIndexHint(Db2SqlParser.IndexHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void enterIndexHintType(Db2SqlParser.IndexHintTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void exitIndexHintType(Db2SqlParser.IndexHintTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoin(Db2SqlParser.InnerJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoin(Db2SqlParser.InnerJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterStraightJoin(Db2SqlParser.StraightJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitStraightJoin(Db2SqlParser.StraightJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoin(Db2SqlParser.OuterJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoin(Db2SqlParser.OuterJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoin(Db2SqlParser.NaturalJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link Db2SqlParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoin(Db2SqlParser.NaturalJoinContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpression(Db2SqlParser.QueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpression(Db2SqlParser.QueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionNointo(Db2SqlParser.QueryExpressionNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionNointo(Db2SqlParser.QueryExpressionNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(Db2SqlParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(Db2SqlParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecificationNointo(Db2SqlParser.QuerySpecificationNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecificationNointo(Db2SqlParser.QuerySpecificationNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesis(Db2SqlParser.UnionParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesis(Db2SqlParser.UnionParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionStatement(Db2SqlParser.UnionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionStatement(Db2SqlParser.UnionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(Db2SqlParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(Db2SqlParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void enterSelectElements(Db2SqlParser.SelectElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void exitSelectElements(Db2SqlParser.SelectElementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStarElement(Db2SqlParser.SelectStarElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStarElement(Db2SqlParser.SelectStarElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnElement(Db2SqlParser.SelectColumnElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnElement(Db2SqlParser.SelectColumnElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectFunctionElement(Db2SqlParser.SelectFunctionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectFunctionElement(Db2SqlParser.SelectFunctionElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpressionElement(Db2SqlParser.SelectExpressionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link Db2SqlParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpressionElement(Db2SqlParser.SelectExpressionElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoVariables(Db2SqlParser.SelectIntoVariablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoVariables(Db2SqlParser.SelectIntoVariablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoDumpFile(Db2SqlParser.SelectIntoDumpFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoDumpFile(Db2SqlParser.SelectIntoDumpFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoTextFile(Db2SqlParser.SelectIntoTextFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link Db2SqlParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoTextFile(Db2SqlParser.SelectIntoTextFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectFieldsInto(Db2SqlParser.SelectFieldsIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectFieldsInto(Db2SqlParser.SelectFieldsIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectLinesInto(Db2SqlParser.SelectLinesIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectLinesInto(Db2SqlParser.SelectLinesIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(Db2SqlParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(Db2SqlParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(Db2SqlParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(Db2SqlParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(Db2SqlParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(Db2SqlParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void enterLimitClauseAtom(Db2SqlParser.LimitClauseAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void exitLimitClauseAtom(Db2SqlParser.LimitClauseAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(Db2SqlParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(Db2SqlParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void enterBeginWork(Db2SqlParser.BeginWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void exitBeginWork(Db2SqlParser.BeginWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void enterCommitWork(Db2SqlParser.CommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void exitCommitWork(Db2SqlParser.CommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterRollbackWork(Db2SqlParser.RollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitRollbackWork(Db2SqlParser.RollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void enterSavepointStatement(Db2SqlParser.SavepointStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void exitSavepointStatement(Db2SqlParser.SavepointStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void enterRollbackStatement(Db2SqlParser.RollbackStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void exitRollbackStatement(Db2SqlParser.RollbackStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterReleaseStatement(Db2SqlParser.ReleaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitReleaseStatement(Db2SqlParser.ReleaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void enterLockTables(Db2SqlParser.LockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void exitLockTables(Db2SqlParser.LockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void enterUnlockTables(Db2SqlParser.UnlockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void exitUnlockTables(Db2SqlParser.UnlockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommitStatement(Db2SqlParser.SetAutocommitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommitStatement(Db2SqlParser.SetAutocommitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransactionStatement(Db2SqlParser.SetTransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransactionStatement(Db2SqlParser.SetTransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionMode(Db2SqlParser.TransactionModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionMode(Db2SqlParser.TransactionModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void enterLockTableElement(Db2SqlParser.LockTableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void exitLockTableElement(Db2SqlParser.LockTableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void enterLockAction(Db2SqlParser.LockActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void exitLockAction(Db2SqlParser.LockActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void enterTransactionOption(Db2SqlParser.TransactionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void exitTransactionOption(Db2SqlParser.TransactionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevel(Db2SqlParser.TransactionLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevel(Db2SqlParser.TransactionLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void enterChangeMaster(Db2SqlParser.ChangeMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void exitChangeMaster(Db2SqlParser.ChangeMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterChangeReplicationFilter(Db2SqlParser.ChangeReplicationFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitChangeReplicationFilter(Db2SqlParser.ChangeReplicationFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void enterPurgeBinaryLogs(Db2SqlParser.PurgeBinaryLogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void exitPurgeBinaryLogs(Db2SqlParser.PurgeBinaryLogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void enterResetMaster(Db2SqlParser.ResetMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void exitResetMaster(Db2SqlParser.ResetMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void enterResetSlave(Db2SqlParser.ResetSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void exitResetSlave(Db2SqlParser.ResetSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void enterStartSlave(Db2SqlParser.StartSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void exitStartSlave(Db2SqlParser.StartSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void enterStopSlave(Db2SqlParser.StopSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void exitStopSlave(Db2SqlParser.StopSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStartGroupReplication(Db2SqlParser.StartGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStartGroupReplication(Db2SqlParser.StartGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStopGroupReplication(Db2SqlParser.StopGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStopGroupReplication(Db2SqlParser.StopGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterStringOption(Db2SqlParser.MasterStringOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterStringOption(Db2SqlParser.MasterStringOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterDecimalOption(Db2SqlParser.MasterDecimalOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterDecimalOption(Db2SqlParser.MasterDecimalOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterBoolOption(Db2SqlParser.MasterBoolOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterBoolOption(Db2SqlParser.MasterBoolOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterRealOption(Db2SqlParser.MasterRealOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterRealOption(Db2SqlParser.MasterRealOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterUidListOption(Db2SqlParser.MasterUidListOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link Db2SqlParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterUidListOption(Db2SqlParser.MasterUidListOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterStringMasterOption(Db2SqlParser.StringMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitStringMasterOption(Db2SqlParser.StringMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterDecimalMasterOption(Db2SqlParser.DecimalMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitDecimalMasterOption(Db2SqlParser.DecimalMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterBoolMasterOption(Db2SqlParser.BoolMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitBoolMasterOption(Db2SqlParser.BoolMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelOption(Db2SqlParser.ChannelOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelOption(Db2SqlParser.ChannelOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoDbReplication(Db2SqlParser.DoDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoDbReplication(Db2SqlParser.DoDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreDbReplication(Db2SqlParser.IgnoreDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreDbReplication(Db2SqlParser.IgnoreDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoTableReplication(Db2SqlParser.DoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoTableReplication(Db2SqlParser.DoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreTableReplication(Db2SqlParser.IgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreTableReplication(Db2SqlParser.IgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildDoTableReplication(Db2SqlParser.WildDoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildDoTableReplication(Db2SqlParser.WildDoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildIgnoreTableReplication(Db2SqlParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildIgnoreTableReplication(Db2SqlParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterRewriteDbReplication(Db2SqlParser.RewriteDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link Db2SqlParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitRewriteDbReplication(Db2SqlParser.RewriteDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void enterTablePair(Db2SqlParser.TablePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void exitTablePair(Db2SqlParser.TablePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#threadType}.
	 * @param ctx the parse tree
	 */
	void enterThreadType(Db2SqlParser.ThreadTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#threadType}.
	 * @param ctx the parse tree
	 */
	void exitThreadType(Db2SqlParser.ThreadTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterGtidsUntilOption(Db2SqlParser.GtidsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitGtidsUntilOption(Db2SqlParser.GtidsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterLogUntilOption(Db2SqlParser.MasterLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterLogUntilOption(Db2SqlParser.MasterLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterRelayLogUntilOption(Db2SqlParser.RelayLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitRelayLogUntilOption(Db2SqlParser.RelayLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterSqlGapsUntilOption(Db2SqlParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link Db2SqlParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitSqlGapsUntilOption(Db2SqlParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterUserConnectionOption(Db2SqlParser.UserConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitUserConnectionOption(Db2SqlParser.UserConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPasswordConnectionOption(Db2SqlParser.PasswordConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPasswordConnectionOption(Db2SqlParser.PasswordConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterDefaultAuthConnectionOption(Db2SqlParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitDefaultAuthConnectionOption(Db2SqlParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPluginDirConnectionOption(Db2SqlParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link Db2SqlParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPluginDirConnectionOption(Db2SqlParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void enterGtuidSet(Db2SqlParser.GtuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void exitGtuidSet(Db2SqlParser.GtuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaStartTransaction(Db2SqlParser.XaStartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaStartTransaction(Db2SqlParser.XaStartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaEndTransaction(Db2SqlParser.XaEndTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaEndTransaction(Db2SqlParser.XaEndTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterXaPrepareStatement(Db2SqlParser.XaPrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitXaPrepareStatement(Db2SqlParser.XaPrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void enterXaCommitWork(Db2SqlParser.XaCommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void exitXaCommitWork(Db2SqlParser.XaCommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRollbackWork(Db2SqlParser.XaRollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRollbackWork(Db2SqlParser.XaRollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRecoverWork(Db2SqlParser.XaRecoverWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRecoverWork(Db2SqlParser.XaRecoverWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrepareStatement(Db2SqlParser.PrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrepareStatement(Db2SqlParser.PrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatement(Db2SqlParser.ExecuteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatement(Db2SqlParser.ExecuteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatePrepare(Db2SqlParser.DeallocatePrepareContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatePrepare(Db2SqlParser.DeallocatePrepareContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBody(Db2SqlParser.RoutineBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBody(Db2SqlParser.RoutineBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(Db2SqlParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(Db2SqlParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(Db2SqlParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(Db2SqlParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(Db2SqlParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(Db2SqlParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterateStatement(Db2SqlParser.IterateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterateStatement(Db2SqlParser.IterateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(Db2SqlParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(Db2SqlParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(Db2SqlParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(Db2SqlParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(Db2SqlParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(Db2SqlParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(Db2SqlParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(Db2SqlParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(Db2SqlParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(Db2SqlParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCloseCursor(Db2SqlParser.CloseCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCloseCursor(Db2SqlParser.CloseCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterFetchCursor(Db2SqlParser.FetchCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitFetchCursor(Db2SqlParser.FetchCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterOpenCursor(Db2SqlParser.OpenCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link Db2SqlParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitOpenCursor(Db2SqlParser.OpenCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVariable(Db2SqlParser.DeclareVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVariable(Db2SqlParser.DeclareVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCondition(Db2SqlParser.DeclareConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCondition(Db2SqlParser.DeclareConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCursor(Db2SqlParser.DeclareCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCursor(Db2SqlParser.DeclareCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void enterDeclareHandler(Db2SqlParser.DeclareHandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void exitDeclareHandler(Db2SqlParser.DeclareHandlerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionCode(Db2SqlParser.HandlerConditionCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionCode(Db2SqlParser.HandlerConditionCodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionState(Db2SqlParser.HandlerConditionStateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionState(Db2SqlParser.HandlerConditionStateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionName(Db2SqlParser.HandlerConditionNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionName(Db2SqlParser.HandlerConditionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionWarning(Db2SqlParser.HandlerConditionWarningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionWarning(Db2SqlParser.HandlerConditionWarningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionNotfound(Db2SqlParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionNotfound(Db2SqlParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionException(Db2SqlParser.HandlerConditionExceptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link Db2SqlParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionException(Db2SqlParser.HandlerConditionExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterProcedureSqlStatement(Db2SqlParser.ProcedureSqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitProcedureSqlStatement(Db2SqlParser.ProcedureSqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseAlternative(Db2SqlParser.CaseAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseAlternative(Db2SqlParser.CaseAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void enterElifAlternative(Db2SqlParser.ElifAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void exitElifAlternative(Db2SqlParser.ElifAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link Db2SqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV56(Db2SqlParser.AlterUserMysqlV56Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link Db2SqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV56(Db2SqlParser.AlterUserMysqlV56Context ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link Db2SqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV57(Db2SqlParser.AlterUserMysqlV57Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link Db2SqlParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV57(Db2SqlParser.AlterUserMysqlV57Context ctx);
	/**
	 * Enter a parse tree produced by the {@code createUserMysqlV56}
	 * labeled alternative in {@link Db2SqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserMysqlV56(Db2SqlParser.CreateUserMysqlV56Context ctx);
	/**
	 * Exit a parse tree produced by the {@code createUserMysqlV56}
	 * labeled alternative in {@link Db2SqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserMysqlV56(Db2SqlParser.CreateUserMysqlV56Context ctx);
	/**
	 * Enter a parse tree produced by the {@code createUserMysqlV57}
	 * labeled alternative in {@link Db2SqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserMysqlV57(Db2SqlParser.CreateUserMysqlV57Context ctx);
	/**
	 * Exit a parse tree produced by the {@code createUserMysqlV57}
	 * labeled alternative in {@link Db2SqlParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserMysqlV57(Db2SqlParser.CreateUserMysqlV57Context ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(Db2SqlParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(Db2SqlParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(Db2SqlParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(Db2SqlParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void enterGrantStatement(Db2SqlParser.GrantStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void exitGrantStatement(Db2SqlParser.GrantStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void enterRoleOption(Db2SqlParser.RoleOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void exitRoleOption(Db2SqlParser.RoleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void enterGrantProxy(Db2SqlParser.GrantProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void exitGrantProxy(Db2SqlParser.GrantProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(Db2SqlParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(Db2SqlParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code detailRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterDetailRevoke(Db2SqlParser.DetailRevokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code detailRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitDetailRevoke(Db2SqlParser.DetailRevokeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code shortRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterShortRevoke(Db2SqlParser.ShortRevokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code shortRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitShortRevoke(Db2SqlParser.ShortRevokeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code roleRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterRoleRevoke(Db2SqlParser.RoleRevokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code roleRevoke}
	 * labeled alternative in {@link Db2SqlParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitRoleRevoke(Db2SqlParser.RoleRevokeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void enterRevokeProxy(Db2SqlParser.RevokeProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void exitRevokeProxy(Db2SqlParser.RevokeProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPasswordStatement(Db2SqlParser.SetPasswordStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPasswordStatement(Db2SqlParser.SetPasswordStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void enterUserSpecification(Db2SqlParser.UserSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void exitUserSpecification(Db2SqlParser.UserSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterHashAuthOption(Db2SqlParser.HashAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitHashAuthOption(Db2SqlParser.HashAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterStringAuthOption(Db2SqlParser.StringAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitStringAuthOption(Db2SqlParser.StringAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterModuleAuthOption(Db2SqlParser.ModuleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitModuleAuthOption(Db2SqlParser.ModuleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAuthOption(Db2SqlParser.SimpleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link Db2SqlParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAuthOption(Db2SqlParser.SimpleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code module}
	 * labeled alternative in {@link Db2SqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterModule(Db2SqlParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code module}
	 * labeled alternative in {@link Db2SqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitModule(Db2SqlParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link Db2SqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterPasswordModuleOption(Db2SqlParser.PasswordModuleOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link Db2SqlParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitPasswordModuleOption(Db2SqlParser.PasswordModuleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void enterTlsOption(Db2SqlParser.TlsOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void exitTlsOption(Db2SqlParser.TlsOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void enterUserResourceOption(Db2SqlParser.UserResourceOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void exitUserResourceOption(Db2SqlParser.UserResourceOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void enterUserPasswordOption(Db2SqlParser.UserPasswordOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void exitUserPasswordOption(Db2SqlParser.UserPasswordOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void enterUserLockOption(Db2SqlParser.UserLockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void exitUserLockOption(Db2SqlParser.UserLockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void enterPrivelegeClause(Db2SqlParser.PrivelegeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void exitPrivelegeClause(Db2SqlParser.PrivelegeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(Db2SqlParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(Db2SqlParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterCurrentSchemaPriviLevel(Db2SqlParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitCurrentSchemaPriviLevel(Db2SqlParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterGlobalPrivLevel(Db2SqlParser.GlobalPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitGlobalPrivLevel(Db2SqlParser.GlobalPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteSchemaPrivLevel(Db2SqlParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteSchemaPrivLevel(Db2SqlParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel(Db2SqlParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel(Db2SqlParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel2(Db2SqlParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel2(Db2SqlParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteTablePrivLevel(Db2SqlParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link Db2SqlParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteTablePrivLevel(Db2SqlParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameUserClause(Db2SqlParser.RenameUserClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameUserClause(Db2SqlParser.RenameUserClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzeTable(Db2SqlParser.AnalyzeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzeTable(Db2SqlParser.AnalyzeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void enterCheckTable(Db2SqlParser.CheckTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void exitCheckTable(Db2SqlParser.CheckTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void enterChecksumTable(Db2SqlParser.ChecksumTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void exitChecksumTable(Db2SqlParser.ChecksumTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void enterOptimizeTable(Db2SqlParser.OptimizeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void exitOptimizeTable(Db2SqlParser.OptimizeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(Db2SqlParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(Db2SqlParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableOption(Db2SqlParser.CheckTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableOption(Db2SqlParser.CheckTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#createUdfunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateUdfunction(Db2SqlParser.CreateUdfunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#createUdfunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateUdfunction(Db2SqlParser.CreateUdfunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void enterInstallPlugin(Db2SqlParser.InstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void exitInstallPlugin(Db2SqlParser.InstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void enterUninstallPlugin(Db2SqlParser.UninstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void exitUninstallPlugin(Db2SqlParser.UninstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetVariable(Db2SqlParser.SetVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetVariable(Db2SqlParser.SetVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetCharset(Db2SqlParser.SetCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetCharset(Db2SqlParser.SetCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNames(Db2SqlParser.SetNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNames(Db2SqlParser.SetNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPassword(Db2SqlParser.SetPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPassword(Db2SqlParser.SetPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransaction(Db2SqlParser.SetTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransaction(Db2SqlParser.SetTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommit(Db2SqlParser.SetAutocommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommit(Db2SqlParser.SetAutocommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNewValueInsideTrigger(Db2SqlParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNewValueInsideTrigger(Db2SqlParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSchema}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetSchema(Db2SqlParser.SetSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSchema}
	 * labeled alternative in {@link Db2SqlParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetSchema(Db2SqlParser.SetSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowMasterLogs(Db2SqlParser.ShowMasterLogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowMasterLogs(Db2SqlParser.ShowMasterLogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCharset(Db2SqlParser.ShowCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCharset(Db2SqlParser.ShowCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowLogEvents(Db2SqlParser.ShowLogEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowLogEvents(Db2SqlParser.ShowLogEventsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowObjectFilter(Db2SqlParser.ShowObjectFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowObjectFilter(Db2SqlParser.ShowObjectFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(Db2SqlParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(Db2SqlParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(Db2SqlParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(Db2SqlParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateDb(Db2SqlParser.ShowCreateDbContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateDb(Db2SqlParser.ShowCreateDbContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateFullIdObject(Db2SqlParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateFullIdObject(Db2SqlParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateUser(Db2SqlParser.ShowCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateUser(Db2SqlParser.ShowCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngine(Db2SqlParser.ShowEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngine(Db2SqlParser.ShowEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGlobalInfo}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowGlobalInfo(Db2SqlParser.ShowGlobalInfoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGlobalInfo}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowGlobalInfo(Db2SqlParser.ShowGlobalInfoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowErrors(Db2SqlParser.ShowErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowErrors(Db2SqlParser.ShowErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCountErrors(Db2SqlParser.ShowCountErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCountErrors(Db2SqlParser.ShowCountErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaFilter(Db2SqlParser.ShowSchemaFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaFilter(Db2SqlParser.ShowSchemaFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoutine(Db2SqlParser.ShowRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoutine(Db2SqlParser.ShowRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(Db2SqlParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(Db2SqlParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowIndexes(Db2SqlParser.ShowIndexesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowIndexes(Db2SqlParser.ShowIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowOpenTables(Db2SqlParser.ShowOpenTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowOpenTables(Db2SqlParser.ShowOpenTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfile(Db2SqlParser.ShowProfileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfile(Db2SqlParser.ShowProfileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveStatus(Db2SqlParser.ShowSlaveStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link Db2SqlParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveStatus(Db2SqlParser.ShowSlaveStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void enterVariableClause(Db2SqlParser.VariableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void exitVariableClause(Db2SqlParser.VariableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowCommonEntity(Db2SqlParser.ShowCommonEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowCommonEntity(Db2SqlParser.ShowCommonEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void enterShowFilter(Db2SqlParser.ShowFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void exitShowFilter(Db2SqlParser.ShowFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void enterShowGlobalInfoClause(Db2SqlParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void exitShowGlobalInfoClause(Db2SqlParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaEntity(Db2SqlParser.ShowSchemaEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaEntity(Db2SqlParser.ShowSchemaEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void enterShowProfileType(Db2SqlParser.ShowProfileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void exitShowProfileType(Db2SqlParser.ShowProfileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void enterBinlogStatement(Db2SqlParser.BinlogStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void exitBinlogStatement(Db2SqlParser.BinlogStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterCacheIndexStatement(Db2SqlParser.CacheIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitCacheIndexStatement(Db2SqlParser.CacheIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(Db2SqlParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(Db2SqlParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void enterKillStatement(Db2SqlParser.KillStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void exitKillStatement(Db2SqlParser.KillStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void enterLoadIndexIntoCache(Db2SqlParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void exitLoadIndexIntoCache(Db2SqlParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void enterResetStatement(Db2SqlParser.ResetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void exitResetStatement(Db2SqlParser.ResetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void enterShutdownStatement(Db2SqlParser.ShutdownStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void exitShutdownStatement(Db2SqlParser.ShutdownStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterTableIndexes(Db2SqlParser.TableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitTableIndexes(Db2SqlParser.TableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFlushOption(Db2SqlParser.SimpleFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFlushOption(Db2SqlParser.SimpleFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelFlushOption(Db2SqlParser.ChannelFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelFlushOption(Db2SqlParser.ChannelFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterTableFlushOption(Db2SqlParser.TableFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link Db2SqlParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitTableFlushOption(Db2SqlParser.TableFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void enterFlushTableOption(Db2SqlParser.FlushTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void exitFlushTableOption(Db2SqlParser.FlushTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterLoadedTableIndexes(Db2SqlParser.LoadedTableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitLoadedTableIndexes(Db2SqlParser.LoadedTableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDescribeStatement(Db2SqlParser.SimpleDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDescribeStatement(Db2SqlParser.SimpleDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterFullDescribeStatement(Db2SqlParser.FullDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitFullDescribeStatement(Db2SqlParser.FullDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void enterHelpStatement(Db2SqlParser.HelpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void exitHelpStatement(Db2SqlParser.HelpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void enterUseStatement(Db2SqlParser.UseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void exitUseStatement(Db2SqlParser.UseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void enterSignalStatement(Db2SqlParser.SignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void exitSignalStatement(Db2SqlParser.SignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void enterResignalStatement(Db2SqlParser.ResignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void exitResignalStatement(Db2SqlParser.ResignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void enterSignalConditionInformation(Db2SqlParser.SignalConditionInformationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void exitSignalConditionInformation(Db2SqlParser.SignalConditionInformationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsStatement(Db2SqlParser.DiagnosticsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsStatement(Db2SqlParser.DiagnosticsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#commentTableStatement}.
	 * @param ctx the parse tree
	 */
	void enterCommentTableStatement(Db2SqlParser.CommentTableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#commentTableStatement}.
	 * @param ctx the parse tree
	 */
	void exitCommentTableStatement(Db2SqlParser.CommentTableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#commentColumnStatement}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumnStatement(Db2SqlParser.CommentColumnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#commentColumnStatement}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumnStatement(Db2SqlParser.CommentColumnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsConditionInformationName(Db2SqlParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsConditionInformationName(Db2SqlParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link Db2SqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeStatements(Db2SqlParser.DescribeStatementsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link Db2SqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeStatements(Db2SqlParser.DescribeStatementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link Db2SqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeConnection(Db2SqlParser.DescribeConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link Db2SqlParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeConnection(Db2SqlParser.DescribeConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#fullId}.
	 * @param ctx the parse tree
	 */
	void enterFullId(Db2SqlParser.FullIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#fullId}.
	 * @param ctx the parse tree
	 */
	void exitFullId(Db2SqlParser.FullIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(Db2SqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(Db2SqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#roleName}.
	 * @param ctx the parse tree
	 */
	void enterRoleName(Db2SqlParser.RoleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#roleName}.
	 * @param ctx the parse tree
	 */
	void exitRoleName(Db2SqlParser.RoleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(Db2SqlParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(Db2SqlParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnName(Db2SqlParser.IndexColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnName(Db2SqlParser.IndexColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userName}.
	 * @param ctx the parse tree
	 */
	void enterUserName(Db2SqlParser.UserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userName}.
	 * @param ctx the parse tree
	 */
	void exitUserName(Db2SqlParser.UserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariable(Db2SqlParser.MysqlVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariable(Db2SqlParser.MysqlVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void enterCharsetName(Db2SqlParser.CharsetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void exitCharsetName(Db2SqlParser.CharsetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#collationName}.
	 * @param ctx the parse tree
	 */
	void enterCollationName(Db2SqlParser.CollationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#collationName}.
	 * @param ctx the parse tree
	 */
	void exitCollationName(Db2SqlParser.CollationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#engineName}.
	 * @param ctx the parse tree
	 */
	void enterEngineName(Db2SqlParser.EngineNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#engineName}.
	 * @param ctx the parse tree
	 */
	void exitEngineName(Db2SqlParser.EngineNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void enterUuidSet(Db2SqlParser.UuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void exitUuidSet(Db2SqlParser.UuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xid}.
	 * @param ctx the parse tree
	 */
	void enterXid(Db2SqlParser.XidContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xid}.
	 * @param ctx the parse tree
	 */
	void exitXid(Db2SqlParser.XidContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void enterXuidStringId(Db2SqlParser.XuidStringIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void exitXuidStringId(Db2SqlParser.XuidStringIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void enterAuthPlugin(Db2SqlParser.AuthPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void exitAuthPlugin(Db2SqlParser.AuthPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#uid}.
	 * @param ctx the parse tree
	 */
	void enterUid(Db2SqlParser.UidContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#uid}.
	 * @param ctx the parse tree
	 */
	void exitUid(Db2SqlParser.UidContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleId(Db2SqlParser.SimpleIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleId(Db2SqlParser.SimpleIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void enterDottedId(Db2SqlParser.DottedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void exitDottedId(Db2SqlParser.DottedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(Db2SqlParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(Db2SqlParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFileSizeLiteral(Db2SqlParser.FileSizeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFileSizeLiteral(Db2SqlParser.FileSizeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(Db2SqlParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(Db2SqlParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(Db2SqlParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(Db2SqlParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(Db2SqlParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(Db2SqlParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void enterNullNotnull(Db2SqlParser.NullNotnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void exitNullNotnull(Db2SqlParser.NullNotnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(Db2SqlParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(Db2SqlParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterStringDataType(Db2SqlParser.StringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitStringDataType(Db2SqlParser.StringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalStringDataType(Db2SqlParser.NationalStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalStringDataType(Db2SqlParser.NationalStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalVaryingStringDataType(Db2SqlParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalVaryingStringDataType(Db2SqlParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDimensionDataType(Db2SqlParser.DimensionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDimensionDataType(Db2SqlParser.DimensionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDataType(Db2SqlParser.SimpleDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDataType(Db2SqlParser.SimpleDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterCollectionDataType(Db2SqlParser.CollectionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitCollectionDataType(Db2SqlParser.CollectionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSpatialDataType(Db2SqlParser.SpatialDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSpatialDataType(Db2SqlParser.SpatialDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarcharDataType(Db2SqlParser.LongVarcharDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarcharDataType(Db2SqlParser.LongVarcharDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarbinaryDataType(Db2SqlParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link Db2SqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarbinaryDataType(Db2SqlParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOptions(Db2SqlParser.CollectionOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOptions(Db2SqlParser.CollectionOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOption(Db2SqlParser.CollectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOption(Db2SqlParser.CollectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void enterConvertedDataType(Db2SqlParser.ConvertedDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void exitConvertedDataType(Db2SqlParser.ConvertedDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthOneDimension(Db2SqlParser.LengthOneDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthOneDimension(Db2SqlParser.LengthOneDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoDimension(Db2SqlParser.LengthTwoDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoDimension(Db2SqlParser.LengthTwoDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoOptionalDimension(Db2SqlParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoOptionalDimension(Db2SqlParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#uidList}.
	 * @param ctx the parse tree
	 */
	void enterUidList(Db2SqlParser.UidListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#uidList}.
	 * @param ctx the parse tree
	 */
	void exitUidList(Db2SqlParser.UidListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void enterTables(Db2SqlParser.TablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void exitTables(Db2SqlParser.TablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnNames(Db2SqlParser.IndexColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnNames(Db2SqlParser.IndexColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(Db2SqlParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(Db2SqlParser.ExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void enterExpressionsWithDefaults(Db2SqlParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void exitExpressionsWithDefaults(Db2SqlParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#constants}.
	 * @param ctx the parse tree
	 */
	void enterConstants(Db2SqlParser.ConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#constants}.
	 * @param ctx the parse tree
	 */
	void exitConstants(Db2SqlParser.ConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStrings(Db2SqlParser.SimpleStringsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStrings(Db2SqlParser.SimpleStringsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void enterUserVariables(Db2SqlParser.UserVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void exitUserVariables(Db2SqlParser.UserVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(Db2SqlParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(Db2SqlParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestamp(Db2SqlParser.CurrentTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestamp(Db2SqlParser.CurrentTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterExpressionOrDefault(Db2SqlParser.ExpressionOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitExpressionOrDefault(Db2SqlParser.ExpressionOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void enterIfExists(Db2SqlParser.IfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void exitIfExists(Db2SqlParser.IfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void enterIfNotExists(Db2SqlParser.IfNotExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void exitIfNotExists(Db2SqlParser.IfNotExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterSpecificFunctionCall(Db2SqlParser.SpecificFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitSpecificFunctionCall(Db2SqlParser.SpecificFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunctionCall(Db2SqlParser.AggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunctionCall(Db2SqlParser.AggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionCall(Db2SqlParser.ScalarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionCall(Db2SqlParser.ScalarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterUdfFunctionCall(Db2SqlParser.UdfFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitUdfFunctionCall(Db2SqlParser.UdfFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionCall(Db2SqlParser.PasswordFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionCall(Db2SqlParser.PasswordFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFunctionCall(Db2SqlParser.SimpleFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFunctionCall(Db2SqlParser.SimpleFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeFunctionCall(Db2SqlParser.DataTypeFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeFunctionCall(Db2SqlParser.DataTypeFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterValuesFunctionCall(Db2SqlParser.ValuesFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitValuesFunctionCall(Db2SqlParser.ValuesFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCaseFunctionCall(Db2SqlParser.CaseFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCaseFunctionCall(Db2SqlParser.CaseFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCharFunctionCall(Db2SqlParser.CharFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCharFunctionCall(Db2SqlParser.CharFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterPositionFunctionCall(Db2SqlParser.PositionFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitPositionFunctionCall(Db2SqlParser.PositionFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSubstrFunctionCall(Db2SqlParser.SubstrFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSubstrFunctionCall(Db2SqlParser.SubstrFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterTrimFunctionCall(Db2SqlParser.TrimFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitTrimFunctionCall(Db2SqlParser.TrimFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterWeightFunctionCall(Db2SqlParser.WeightFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitWeightFunctionCall(Db2SqlParser.WeightFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterExtractFunctionCall(Db2SqlParser.ExtractFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitExtractFunctionCall(Db2SqlParser.ExtractFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetFormatFunctionCall(Db2SqlParser.GetFormatFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetFormatFunctionCall(Db2SqlParser.GetFormatFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueFunctionCall(Db2SqlParser.JsonValueFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link Db2SqlParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueFunctionCall(Db2SqlParser.JsonValueFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseFuncAlternative(Db2SqlParser.CaseFuncAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseFuncAlternative(Db2SqlParser.CaseFuncAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link Db2SqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightList(Db2SqlParser.LevelWeightListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link Db2SqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightList(Db2SqlParser.LevelWeightListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link Db2SqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightRange(Db2SqlParser.LevelWeightRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link Db2SqlParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightRange(Db2SqlParser.LevelWeightRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void enterLevelInWeightListElement(Db2SqlParser.LevelInWeightListElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void exitLevelInWeightListElement(Db2SqlParser.LevelInWeightListElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#aggregateWindowedFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregateWindowedFunction(Db2SqlParser.AggregateWindowedFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#aggregateWindowedFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregateWindowedFunction(Db2SqlParser.AggregateWindowedFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionName(Db2SqlParser.ScalarFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionName(Db2SqlParser.ScalarFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionClause(Db2SqlParser.PasswordFunctionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionClause(Db2SqlParser.PasswordFunctionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgs(Db2SqlParser.FunctionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgs(Db2SqlParser.FunctionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(Db2SqlParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(Db2SqlParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(Db2SqlParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(Db2SqlParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(Db2SqlParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(Db2SqlParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(Db2SqlParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(Db2SqlParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(Db2SqlParser.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link Db2SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(Db2SqlParser.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSoundsLikePredicate(Db2SqlParser.SoundsLikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSoundsLikePredicate(Db2SqlParser.SoundsLikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAtomPredicate(Db2SqlParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAtomPredicate(Db2SqlParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterJsonMemberOfPredicate(Db2SqlParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitJsonMemberOfPredicate(Db2SqlParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(Db2SqlParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(Db2SqlParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryComparasionPredicate(Db2SqlParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryComparasionPredicate(Db2SqlParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetweenPredicate(Db2SqlParser.BetweenPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetweenPredicate(Db2SqlParser.BetweenPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBinaryComparasionPredicate(Db2SqlParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBinaryComparasionPredicate(Db2SqlParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(Db2SqlParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(Db2SqlParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(Db2SqlParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(Db2SqlParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterRegexpPredicate(Db2SqlParser.RegexpPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link Db2SqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitRegexpPredicate(Db2SqlParser.RegexpPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionAtom(Db2SqlParser.UnaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionAtom(Db2SqlParser.UnaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterCollateExpressionAtom(Db2SqlParser.CollateExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitCollateExpressionAtom(Db2SqlParser.CollateExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpessionAtom(Db2SqlParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpessionAtom(Db2SqlParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariableExpressionAtom(Db2SqlParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariableExpressionAtom(Db2SqlParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpressionAtom(Db2SqlParser.NestedExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpressionAtom(Db2SqlParser.NestedExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedRowExpressionAtom(Db2SqlParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedRowExpressionAtom(Db2SqlParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMathExpressionAtom(Db2SqlParser.MathExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMathExpressionAtom(Db2SqlParser.MathExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpressionAtom(Db2SqlParser.IntervalExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpressionAtom(Db2SqlParser.IntervalExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterJsonExpressionAtom(Db2SqlParser.JsonExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitJsonExpressionAtom(Db2SqlParser.JsonExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterExistsExpessionAtom(Db2SqlParser.ExistsExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitExistsExpessionAtom(Db2SqlParser.ExistsExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(Db2SqlParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(Db2SqlParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(Db2SqlParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(Db2SqlParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpressionAtom(Db2SqlParser.BinaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpressionAtom(Db2SqlParser.BinaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(Db2SqlParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(Db2SqlParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBitExpressionAtom(Db2SqlParser.BitExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link Db2SqlParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBitExpressionAtom(Db2SqlParser.BitExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(Db2SqlParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(Db2SqlParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(Db2SqlParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(Db2SqlParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(Db2SqlParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(Db2SqlParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitOperator(Db2SqlParser.BitOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitOperator(Db2SqlParser.BitOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void enterMathOperator(Db2SqlParser.MathOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void exitMathOperator(Db2SqlParser.MathOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void enterJsonOperator(Db2SqlParser.JsonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void exitJsonOperator(Db2SqlParser.JsonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void enterCharsetNameBase(Db2SqlParser.CharsetNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void exitCharsetNameBase(Db2SqlParser.CharsetNameBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevelBase(Db2SqlParser.TransactionLevelBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevelBase(Db2SqlParser.TransactionLevelBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void enterPrivilegesBase(Db2SqlParser.PrivilegesBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void exitPrivilegesBase(Db2SqlParser.PrivilegesBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterIntervalTypeBase(Db2SqlParser.IntervalTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitIntervalTypeBase(Db2SqlParser.IntervalTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeBase(Db2SqlParser.DataTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeBase(Db2SqlParser.DataTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(Db2SqlParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(Db2SqlParser.KeywordsCanBeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Db2SqlParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNameBase(Db2SqlParser.FunctionNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Db2SqlParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNameBase(Db2SqlParser.FunctionNameBaseContext ctx);
}