// Generated from DorisParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.doris.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DorisParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DorisParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DorisParser#multiStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiStatements(DorisParser.MultiStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#singleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleStatement(DorisParser.SingleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#expressionWithEof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionWithEof(DorisParser.ExpressionWithEofContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementBaseAlias}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBaseAlias(DorisParser.StatementBaseAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallProcedure(DorisParser.CallProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcedure(DorisParser.CreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropProcedure(DorisParser.DropProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProcedureStatus}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcedureStatus(DorisParser.ShowProcedureStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateProcedure(DorisParser.ShowCreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showConfig}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowConfig(DorisParser.ShowConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefault(DorisParser.StatementDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedDmlStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedDmlStatementAlias(DorisParser.SupportedDmlStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedCreateStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedCreateStatementAlias(DorisParser.SupportedCreateStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedAlterStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedAlterStatementAlias(DorisParser.SupportedAlterStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code materializedViewStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaterializedViewStatementAlias(DorisParser.MaterializedViewStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedJobStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedJobStatementAlias(DorisParser.SupportedJobStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintStatementAlias(DorisParser.ConstraintStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedCleanStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedCleanStatementAlias(DorisParser.SupportedCleanStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedDescribeStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedDescribeStatementAlias(DorisParser.SupportedDescribeStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedDropStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedDropStatementAlias(DorisParser.SupportedDropStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedSetStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedSetStatementAlias(DorisParser.SupportedSetStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedUnsetStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedUnsetStatementAlias(DorisParser.SupportedUnsetStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedRefreshStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedRefreshStatementAlias(DorisParser.SupportedRefreshStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedShowStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedShowStatementAlias(DorisParser.SupportedShowStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedLoadStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedLoadStatementAlias(DorisParser.SupportedLoadStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedCancelStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedCancelStatementAlias(DorisParser.SupportedCancelStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedRecoverStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedRecoverStatementAlias(DorisParser.SupportedRecoverStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedAdminStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedAdminStatementAlias(DorisParser.SupportedAdminStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedUseStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedUseStatementAlias(DorisParser.SupportedUseStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedOtherStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedOtherStatementAlias(DorisParser.SupportedOtherStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedKillStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedKillStatementAlias(DorisParser.SupportedKillStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedStatsStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedStatsStatementAlias(DorisParser.SupportedStatsStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedTransactionStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedTransactionStatementAlias(DorisParser.SupportedTransactionStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code supportedGrantRevokeStatementAlias}
	 * labeled alternative in {@link DorisParser#statementBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedGrantRevokeStatementAlias(DorisParser.SupportedGrantRevokeStatementAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateMTMV(DorisParser.CreateMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshMTMV(DorisParser.RefreshMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterMTMV(DorisParser.AlterMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropMV(DorisParser.DropMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pauseMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPauseMTMV(DorisParser.PauseMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code resumeMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResumeMTMV(DorisParser.ResumeMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelMTMVTask}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelMTMVTask(DorisParser.CancelMTMVTaskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateMTMV}
	 * labeled alternative in {@link DorisParser#materializedViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateMTMV(DorisParser.ShowCreateMTMVContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createScheduledJob}
	 * labeled alternative in {@link DorisParser#supportedJobStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateScheduledJob(DorisParser.CreateScheduledJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pauseJob}
	 * labeled alternative in {@link DorisParser#supportedJobStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPauseJob(DorisParser.PauseJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropJob}
	 * labeled alternative in {@link DorisParser#supportedJobStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropJob(DorisParser.DropJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code resumeJob}
	 * labeled alternative in {@link DorisParser#supportedJobStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResumeJob(DorisParser.ResumeJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelJobTask}
	 * labeled alternative in {@link DorisParser#supportedJobStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelJobTask(DorisParser.CancelJobTaskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link DorisParser#constraintStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddConstraint(DorisParser.AddConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link DorisParser#constraintStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropConstraint(DorisParser.DropConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showConstraint}
	 * labeled alternative in {@link DorisParser#constraintStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowConstraint(DorisParser.ShowConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#optSpecBranch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptSpecBranch(DorisParser.OptSpecBranchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code insertTable}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertTable(DorisParser.InsertTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code update}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(DorisParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code delete}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete(DorisParser.DeleteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code load}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoad(DorisParser.LoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code export}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport(DorisParser.ExportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replay}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplay(DorisParser.ReplayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code copyInto}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopyInto(DorisParser.CopyIntoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code truncateTable}
	 * labeled alternative in {@link DorisParser#supportedDmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncateTable(DorisParser.TruncateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTable(DorisParser.CreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateView(DorisParser.CreateViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createFile}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateFile(DorisParser.CreateFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableLike(DorisParser.CreateTableLikeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createRole}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRole(DorisParser.CreateRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createWorkloadGroup}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateWorkloadGroup(DorisParser.CreateWorkloadGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createCatalog}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateCatalog(DorisParser.CreateCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createRowPolicy}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRowPolicy(DorisParser.CreateRowPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createStoragePolicy}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateStoragePolicy(DorisParser.CreateStoragePolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code buildIndex}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuildIndex(DorisParser.BuildIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createIndex}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndex(DorisParser.CreateIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createWorkloadPolicy}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateWorkloadPolicy(DorisParser.CreateWorkloadPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createSqlBlockRule}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSqlBlockRule(DorisParser.CreateSqlBlockRuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createEncryptkey}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateEncryptkey(DorisParser.CreateEncryptkeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUserDefineFunction}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserDefineFunction(DorisParser.CreateUserDefineFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createAliasFunction}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateAliasFunction(DorisParser.CreateAliasFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUser}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUser(DorisParser.CreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(DorisParser.CreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createRepository}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRepository(DorisParser.CreateRepositoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createResource}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateResource(DorisParser.CreateResourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createDictionary}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDictionary(DorisParser.CreateDictionaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createStage}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateStage(DorisParser.CreateStageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createStorageVault}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateStorageVault(DorisParser.CreateStorageVaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createIndexAnalyzer}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndexAnalyzer(DorisParser.CreateIndexAnalyzerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createIndexTokenizer}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndexTokenizer(DorisParser.CreateIndexTokenizerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createIndexTokenFilter}
	 * labeled alternative in {@link DorisParser#supportedCreateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndexTokenFilter(DorisParser.CreateIndexTokenFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dictionaryColumnDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryColumnDefs(DorisParser.DictionaryColumnDefsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dictionaryColumnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryColumnDef(DorisParser.DictionaryColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterSystem}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterSystem(DorisParser.AlterSystemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterView}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterView(DorisParser.AlterViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterCatalogRename}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCatalogRename(DorisParser.AlterCatalogRenameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterRole}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterRole(DorisParser.AlterRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterStorageVault}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterStorageVault(DorisParser.AlterStorageVaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterWorkloadGroup}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterWorkloadGroup(DorisParser.AlterWorkloadGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterCatalogProperties}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCatalogProperties(DorisParser.AlterCatalogPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterWorkloadPolicy}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterWorkloadPolicy(DorisParser.AlterWorkloadPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterSqlBlockRule}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterSqlBlockRule(DorisParser.AlterSqlBlockRuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterCatalogComment}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCatalogComment(DorisParser.AlterCatalogCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterDatabaseRename}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabaseRename(DorisParser.AlterDatabaseRenameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterStoragePolicy}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterStoragePolicy(DorisParser.AlterStoragePolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTable}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(DorisParser.AlterTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableAddRollup}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableAddRollup(DorisParser.AlterTableAddRollupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableDropRollup}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableDropRollup(DorisParser.AlterTableDropRollupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableProperties}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableProperties(DorisParser.AlterTablePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterDatabaseSetQuota}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabaseSetQuota(DorisParser.AlterDatabaseSetQuotaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterDatabaseProperties}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabaseProperties(DorisParser.AlterDatabasePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterSystemRenameComputeGroup}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterSystemRenameComputeGroup(DorisParser.AlterSystemRenameComputeGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterResource}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterResource(DorisParser.AlterResourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterRepository}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterRepository(DorisParser.AlterRepositoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterRoutineLoad(DorisParser.AlterRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterColocateGroup}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterColocateGroup(DorisParser.AlterColocateGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterUser}
	 * labeled alternative in {@link DorisParser#supportedAlterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUser(DorisParser.AlterUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropCatalogRecycleBin}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropCatalogRecycleBin(DorisParser.DropCatalogRecycleBinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropEncryptkey}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropEncryptkey(DorisParser.DropEncryptkeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropRole}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRole(DorisParser.DropRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropSqlBlockRule}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropSqlBlockRule(DorisParser.DropSqlBlockRuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropUser}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropUser(DorisParser.DropUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropStoragePolicy}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropStoragePolicy(DorisParser.DropStoragePolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropWorkloadGroup}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropWorkloadGroup(DorisParser.DropWorkloadGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropCatalog}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropCatalog(DorisParser.DropCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropFile}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFile(DorisParser.DropFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropWorkloadPolicy}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropWorkloadPolicy(DorisParser.DropWorkloadPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropRepository}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRepository(DorisParser.DropRepositoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(DorisParser.DropTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(DorisParser.DropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunction(DorisParser.DropFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropIndex}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndex(DorisParser.DropIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropResource}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropResource(DorisParser.DropResourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropRowPolicy}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRowPolicy(DorisParser.DropRowPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropDictionary}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDictionary(DorisParser.DropDictionaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropStage}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropStage(DorisParser.DropStageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(DorisParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropIndexAnalyzer}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndexAnalyzer(DorisParser.DropIndexAnalyzerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropIndexTokenizer}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndexTokenizer(DorisParser.DropIndexTokenizerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropIndexTokenFilter}
	 * labeled alternative in {@link DorisParser#supportedDropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndexTokenFilter(DorisParser.DropIndexTokenFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showVariables}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowVariables(DorisParser.ShowVariablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAuthors}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAuthors(DorisParser.ShowAuthorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAlterTable}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAlterTable(DorisParser.ShowAlterTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateDatabase}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateDatabase(DorisParser.ShowCreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showBackup}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBackup(DorisParser.ShowBackupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showBroker}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBroker(DorisParser.ShowBrokerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showBuildIndex}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBuildIndex(DorisParser.ShowBuildIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDynamicPartition}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDynamicPartition(DorisParser.ShowDynamicPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEvents}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEvents(DorisParser.ShowEventsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showExport}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowExport(DorisParser.ShowExportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showLastInsert}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLastInsert(DorisParser.ShowLastInsertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCharset(DorisParser.ShowCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDelete}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDelete(DorisParser.ShowDeleteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateFunction}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateFunction(DorisParser.ShowCreateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctions(DorisParser.ShowFunctionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGlobalFunctions}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGlobalFunctions(DorisParser.ShowGlobalFunctionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrants(DorisParser.ShowGrantsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGrantsForUser}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrantsForUser(DorisParser.ShowGrantsForUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateUser(DorisParser.ShowCreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSnapshot}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSnapshot(DorisParser.ShowSnapshotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showLoadProfile}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLoadProfile(DorisParser.ShowLoadProfileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateRepository}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateRepository(DorisParser.ShowCreateRepositoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showView}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowView(DorisParser.ShowViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPlugins(DorisParser.ShowPluginsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStorageVault}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStorageVault(DorisParser.ShowStorageVaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRepositories}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRepositories(DorisParser.ShowRepositoriesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEncryptKeys}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEncryptKeys(DorisParser.ShowEncryptKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTable(DorisParser.ShowCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcessList(DorisParser.ShowProcessListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPartitions}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPartitions(DorisParser.ShowPartitionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRestore}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRestore(DorisParser.ShowRestoreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRoles(DorisParser.ShowRolesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPartitionId}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPartitionId(DorisParser.ShowPartitionIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPrivileges(DorisParser.ShowPrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProc}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProc(DorisParser.ShowProcContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSmallFiles}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSmallFiles(DorisParser.ShowSmallFilesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStorageEngines}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStorageEngines(DorisParser.ShowStorageEnginesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateCatalog}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateCatalog(DorisParser.ShowCreateCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCatalog}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCatalog(DorisParser.ShowCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCatalogs}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCatalogs(DorisParser.ShowCatalogsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showUserProperties}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowUserProperties(DorisParser.ShowUserPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAllProperties}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAllProperties(DorisParser.ShowAllPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCollation}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCollation(DorisParser.ShowCollationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRowPolicy}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRowPolicy(DorisParser.ShowRowPolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStoragePolicy}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStoragePolicy(DorisParser.ShowStoragePolicyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSqlBlockRule}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSqlBlockRule(DorisParser.ShowSqlBlockRuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateView}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateView(DorisParser.ShowCreateViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateStorageVault}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateStorageVault(DorisParser.ShowCreateStorageVaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDataTypes}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDataTypes(DorisParser.ShowDataTypesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showData}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowData(DorisParser.ShowDataContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateMaterializedView}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateMaterializedView(DorisParser.ShowCreateMaterializedViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showWarningErrors}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWarningErrors(DorisParser.ShowWarningErrorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showWarningErrorCount}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWarningErrorCount(DorisParser.ShowWarningErrorCountContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showBackends}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBackends(DorisParser.ShowBackendsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStages}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStages(DorisParser.ShowStagesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showReplicaDistribution}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowReplicaDistribution(DorisParser.ShowReplicaDistributionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showResources}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowResources(DorisParser.ShowResourcesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showLoad}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLoad(DorisParser.ShowLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showLoadWarings}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLoadWarings(DorisParser.ShowLoadWaringsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTriggers}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTriggers(DorisParser.ShowTriggersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDiagnoseTablet}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDiagnoseTablet(DorisParser.ShowDiagnoseTabletContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowOpenTables(DorisParser.ShowOpenTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showFrontends}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFrontends(DorisParser.ShowFrontendsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatabaseId}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabaseId(DorisParser.ShowDatabaseIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumns(DorisParser.ShowColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTableId}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableId(DorisParser.ShowTableIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTrash}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTrash(DorisParser.ShowTrashContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTypeCast}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTypeCast(DorisParser.ShowTypeCastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showClusters}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowClusters(DorisParser.ShowClustersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStatus(DorisParser.ShowStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showWhitelist}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWhitelist(DorisParser.ShowWhitelistContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTabletsBelong}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTabletsBelong(DorisParser.ShowTabletsBelongContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDataSkew}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDataSkew(DorisParser.ShowDataSkewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTableCreation}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableCreation(DorisParser.ShowTableCreationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTabletStorageFormat}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTabletStorageFormat(DorisParser.ShowTabletStorageFormatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showQueryProfile}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowQueryProfile(DorisParser.ShowQueryProfileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showConvertLsc}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowConvertLsc(DorisParser.ShowConvertLscContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(DorisParser.ShowTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showViews}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowViews(DorisParser.ShowViewsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTableStatus}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableStatus(DorisParser.ShowTableStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabases(DorisParser.ShowDatabasesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTabletsFromTable}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTabletsFromTable(DorisParser.ShowTabletsFromTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCatalogRecycleBin}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCatalogRecycleBin(DorisParser.ShowCatalogRecycleBinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTabletId}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTabletId(DorisParser.ShowTabletIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDictionaries}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDictionaries(DorisParser.ShowDictionariesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTransaction}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTransaction(DorisParser.ShowTransactionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowReplicaStatus(DorisParser.ShowReplicaStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showWorkloadGroups}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWorkloadGroups(DorisParser.ShowWorkloadGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCopy}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCopy(DorisParser.ShowCopyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showQueryStats}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowQueryStats(DorisParser.ShowQueryStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndex}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndex(DorisParser.ShowIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showWarmUpJob}
	 * labeled alternative in {@link DorisParser#supportedShowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWarmUpJob(DorisParser.ShowWarmUpJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sync}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSync(DorisParser.SyncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateLoad(DorisParser.ShowCreateLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createRoutineLoadAlias}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRoutineLoadAlias(DorisParser.CreateRoutineLoadAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mysqlLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMysqlLoad(DorisParser.MysqlLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateRoutineLoad(DorisParser.ShowCreateRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pauseRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPauseRoutineLoad(DorisParser.PauseRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pauseAllRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPauseAllRoutineLoad(DorisParser.PauseAllRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code resumeRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResumeRoutineLoad(DorisParser.ResumeRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code resumeAllRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResumeAllRoutineLoad(DorisParser.ResumeAllRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stopRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStopRoutineLoad(DorisParser.StopRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRoutineLoad}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRoutineLoad(DorisParser.ShowRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRoutineLoadTask}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRoutineLoadTask(DorisParser.ShowRoutineLoadTaskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexAnalyzer}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexAnalyzer(DorisParser.ShowIndexAnalyzerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexTokenizer}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexTokenizer(DorisParser.ShowIndexTokenizerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexTokenFilter}
	 * labeled alternative in {@link DorisParser#supportedLoadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexTokenFilter(DorisParser.ShowIndexTokenFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code killConnection}
	 * labeled alternative in {@link DorisParser#supportedKillStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillConnection(DorisParser.KillConnectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code killQuery}
	 * labeled alternative in {@link DorisParser#supportedKillStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillQuery(DorisParser.KillQueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code help}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHelp(DorisParser.HelpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unlockTables}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlockTables(DorisParser.UnlockTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code installPlugin}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstallPlugin(DorisParser.InstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by the {@code uninstallPlugin}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninstallPlugin(DorisParser.UninstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lockTables}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockTables(DorisParser.LockTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code restore}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestore(DorisParser.RestoreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code warmUpCluster}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWarmUpCluster(DorisParser.WarmUpClusterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code backup}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBackup(DorisParser.BackupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unsupportedStartTransaction}
	 * labeled alternative in {@link DorisParser#supportedOtherStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsupportedStartTransaction(DorisParser.UnsupportedStartTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#warmUpItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWarmUpItem(DorisParser.WarmUpItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#lockTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockTable(DorisParser.LockTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#createRoutineLoad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRoutineLoad(DorisParser.CreateRoutineLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code separator}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeparator(DorisParser.SeparatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importColumns}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportColumns(DorisParser.ImportColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importPrecedingFilter}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPrecedingFilter(DorisParser.ImportPrecedingFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importWhere}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportWhere(DorisParser.ImportWhereContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importDeleteOn}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeleteOn(DorisParser.ImportDeleteOnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importSequence}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportSequence(DorisParser.ImportSequenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importPartitions}
	 * labeled alternative in {@link DorisParser#loadProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPartitions(DorisParser.ImportPartitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importSequenceStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportSequenceStatement(DorisParser.ImportSequenceStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importDeleteOnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeleteOnStatement(DorisParser.ImportDeleteOnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importWhereStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportWhereStatement(DorisParser.ImportWhereStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importPrecedingFilterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPrecedingFilterStatement(DorisParser.ImportPrecedingFilterStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importColumnsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportColumnsStatement(DorisParser.ImportColumnsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#importColumnDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportColumnDesc(DorisParser.ImportColumnDescContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshCatalog}
	 * labeled alternative in {@link DorisParser#supportedRefreshStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshCatalog(DorisParser.RefreshCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshDatabase}
	 * labeled alternative in {@link DorisParser#supportedRefreshStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshDatabase(DorisParser.RefreshDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshTable}
	 * labeled alternative in {@link DorisParser#supportedRefreshStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshTable(DorisParser.RefreshTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshDictionary}
	 * labeled alternative in {@link DorisParser#supportedRefreshStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshDictionary(DorisParser.RefreshDictionaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshLdap}
	 * labeled alternative in {@link DorisParser#supportedRefreshStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshLdap(DorisParser.RefreshLdapContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cleanAllProfile}
	 * labeled alternative in {@link DorisParser#supportedCleanStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanAllProfile(DorisParser.CleanAllProfileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cleanLabel}
	 * labeled alternative in {@link DorisParser#supportedCleanStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanLabel(DorisParser.CleanLabelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cleanQueryStats}
	 * labeled alternative in {@link DorisParser#supportedCleanStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanQueryStats(DorisParser.CleanQueryStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cleanAllQueryStats}
	 * labeled alternative in {@link DorisParser#supportedCleanStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCleanAllQueryStats(DorisParser.CleanAllQueryStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelLoad}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelLoad(DorisParser.CancelLoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelExport}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelExport(DorisParser.CancelExportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelWarmUpJob}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelWarmUpJob(DorisParser.CancelWarmUpJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelDecommisionBackend}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelDecommisionBackend(DorisParser.CancelDecommisionBackendContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelBackup}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelBackup(DorisParser.CancelBackupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelRestore}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelRestore(DorisParser.CancelRestoreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelBuildIndex}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelBuildIndex(DorisParser.CancelBuildIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cancelAlterTable}
	 * labeled alternative in {@link DorisParser#supportedCancelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCancelAlterTable(DorisParser.CancelAlterTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminShowReplicaDistribution}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminShowReplicaDistribution(DorisParser.AdminShowReplicaDistributionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminRebalanceDisk}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminRebalanceDisk(DorisParser.AdminRebalanceDiskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCancelRebalanceDisk}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCancelRebalanceDisk(DorisParser.AdminCancelRebalanceDiskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminDiagnoseTablet}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminDiagnoseTablet(DorisParser.AdminDiagnoseTabletContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminShowReplicaStatus}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminShowReplicaStatus(DorisParser.AdminShowReplicaStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCompactTable}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCompactTable(DorisParser.AdminCompactTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCheckTablets}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCheckTablets(DorisParser.AdminCheckTabletsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminShowTabletStorageFormat}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminShowTabletStorageFormat(DorisParser.AdminShowTabletStorageFormatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetFrontendConfig}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetFrontendConfig(DorisParser.AdminSetFrontendConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCleanTrash}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCleanTrash(DorisParser.AdminCleanTrashContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetReplicaVersion}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetReplicaVersion(DorisParser.AdminSetReplicaVersionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetTableStatus}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetTableStatus(DorisParser.AdminSetTableStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetReplicaStatus}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetReplicaStatus(DorisParser.AdminSetReplicaStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminRepairTable}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminRepairTable(DorisParser.AdminRepairTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCancelRepairTable}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCancelRepairTable(DorisParser.AdminCancelRepairTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCopyTablet}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCopyTablet(DorisParser.AdminCopyTabletContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetEncryptionRootKey}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetEncryptionRootKey(DorisParser.AdminSetEncryptionRootKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminSetPartitionVersion}
	 * labeled alternative in {@link DorisParser#supportedAdminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminSetPartitionVersion(DorisParser.AdminSetPartitionVersionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recoverDatabase}
	 * labeled alternative in {@link DorisParser#supportedRecoverStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecoverDatabase(DorisParser.RecoverDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recoverTable}
	 * labeled alternative in {@link DorisParser#supportedRecoverStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecoverTable(DorisParser.RecoverTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code recoverPartition}
	 * labeled alternative in {@link DorisParser#supportedRecoverStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecoverPartition(DorisParser.RecoverPartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#baseTableRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseTableRef(DorisParser.BaseTableRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#wildWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildWhere(DorisParser.WildWhereContext ctx);
	/**
	 * Visit a parse tree produced by the {@code transactionBegin}
	 * labeled alternative in {@link DorisParser#supportedTransactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionBegin(DorisParser.TransactionBeginContext ctx);
	/**
	 * Visit a parse tree produced by the {@code transcationCommit}
	 * labeled alternative in {@link DorisParser#supportedTransactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranscationCommit(DorisParser.TranscationCommitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code transactionRollback}
	 * labeled alternative in {@link DorisParser#supportedTransactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionRollback(DorisParser.TransactionRollbackContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantTablePrivilege}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantTablePrivilege(DorisParser.GrantTablePrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantResourcePrivilege}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantResourcePrivilege(DorisParser.GrantResourcePrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantRole}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantRole(DorisParser.GrantRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeRole}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeRole(DorisParser.RevokeRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeResourcePrivilege}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeResourcePrivilege(DorisParser.RevokeResourcePrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeTablePrivilege}
	 * labeled alternative in {@link DorisParser#supportedGrantRevokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeTablePrivilege(DorisParser.RevokeTablePrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(DorisParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#privilegeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilegeList(DorisParser.PrivilegeListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addBackendClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddBackendClause(DorisParser.AddBackendClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropBackendClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropBackendClause(DorisParser.DropBackendClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decommissionBackendClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecommissionBackendClause(DorisParser.DecommissionBackendClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addObserverClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddObserverClause(DorisParser.AddObserverClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropObserverClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropObserverClause(DorisParser.DropObserverClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addFollowerClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddFollowerClause(DorisParser.AddFollowerClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropFollowerClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFollowerClause(DorisParser.DropFollowerClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addBrokerClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddBrokerClause(DorisParser.AddBrokerClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropBrokerClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropBrokerClause(DorisParser.DropBrokerClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropAllBrokerClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropAllBrokerClause(DorisParser.DropAllBrokerClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterLoadErrorUrlClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterLoadErrorUrlClause(DorisParser.AlterLoadErrorUrlClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyBackendClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyBackendClause(DorisParser.ModifyBackendClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyFrontendOrBackendHostNameClause}
	 * labeled alternative in {@link DorisParser#alterSystemClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyFrontendOrBackendHostNameClause(DorisParser.ModifyFrontendOrBackendHostNameClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dropRollupClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRollupClause(DorisParser.DropRollupClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#addRollupClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddRollupClause(DorisParser.AddRollupClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumnClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumnClause(DorisParser.AddColumnClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumnsClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumnsClause(DorisParser.AddColumnsClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropColumnClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropColumnClause(DorisParser.DropColumnClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyColumnClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyColumnClause(DorisParser.ModifyColumnClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code reorderColumnsClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReorderColumnsClause(DorisParser.ReorderColumnsClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addPartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddPartitionClause(DorisParser.AddPartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropPartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropPartitionClause(DorisParser.DropPartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyPartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyPartitionClause(DorisParser.ModifyPartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replacePartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplacePartitionClause(DorisParser.ReplacePartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replaceTableClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceTableClause(DorisParser.ReplaceTableClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameClause(DorisParser.RenameClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameRollupClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameRollupClause(DorisParser.RenameRollupClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renamePartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenamePartitionClause(DorisParser.RenamePartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameColumnClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameColumnClause(DorisParser.RenameColumnClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addIndexClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddIndexClause(DorisParser.AddIndexClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropIndexClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndexClause(DorisParser.DropIndexClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code enableFeatureClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnableFeatureClause(DorisParser.EnableFeatureClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyDistributionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyDistributionClause(DorisParser.ModifyDistributionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyTableCommentClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyTableCommentClause(DorisParser.ModifyTableCommentClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyColumnCommentClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyColumnCommentClause(DorisParser.ModifyColumnCommentClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyEngineClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyEngineClause(DorisParser.ModifyEngineClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterMultiPartitionClause}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterMultiPartitionClause(DorisParser.AlterMultiPartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createOrReplaceTagClauses}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrReplaceTagClauses(DorisParser.CreateOrReplaceTagClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createOrReplaceBranchClauses}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrReplaceBranchClauses(DorisParser.CreateOrReplaceBranchClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropBranchClauses}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropBranchClauses(DorisParser.DropBranchClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropTagClauses}
	 * labeled alternative in {@link DorisParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTagClauses(DorisParser.DropTagClausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#createOrReplaceTagClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrReplaceTagClause(DorisParser.CreateOrReplaceTagClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#createOrReplaceBranchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrReplaceBranchClause(DorisParser.CreateOrReplaceBranchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#tagOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagOptions(DorisParser.TagOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#branchOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranchOptions(DorisParser.BranchOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#retainTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetainTime(DorisParser.RetainTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#retentionSnapshot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetentionSnapshot(DorisParser.RetentionSnapshotContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#minSnapshotsToKeep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinSnapshotsToKeep(DorisParser.MinSnapshotsToKeepContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#timeValueWithUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeValueWithUnit(DorisParser.TimeValueWithUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dropBranchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropBranchClause(DorisParser.DropBranchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dropTagClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTagClause(DorisParser.DropTagClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#columnPosition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnPosition(DorisParser.ColumnPositionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#toRollup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToRollup(DorisParser.ToRollupContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#fromRollup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromRollup(DorisParser.FromRollupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAnalyze}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAnalyze(DorisParser.ShowAnalyzeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showQueuedAnalyzeJobs}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowQueuedAnalyzeJobs(DorisParser.ShowQueuedAnalyzeJobsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showColumnHistogramStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumnHistogramStats(DorisParser.ShowColumnHistogramStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showColumnStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumnStats(DorisParser.ShowColumnStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAnalyzeTask}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAnalyzeTask(DorisParser.ShowAnalyzeTaskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code analyzeDatabase}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzeDatabase(DorisParser.AnalyzeDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code analyzeTable}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzeTable(DorisParser.AnalyzeTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableStats(DorisParser.AlterTableStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterColumnStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterColumnStats(DorisParser.AlterColumnStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexStats(DorisParser.ShowIndexStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropStats(DorisParser.DropStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropCachedStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropCachedStats(DorisParser.DropCachedStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropExpiredStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropExpiredStats(DorisParser.DropExpiredStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code killAnalyzeJob}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillAnalyzeJob(DorisParser.KillAnalyzeJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropAnalyzeJob}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropAnalyzeJob(DorisParser.DropAnalyzeJobContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTableStats}
	 * labeled alternative in {@link DorisParser#supportedStatsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableStats(DorisParser.ShowTableStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#analyzeProperties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzeProperties(DorisParser.AnalyzePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#workloadPolicyActions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkloadPolicyActions(DorisParser.WorkloadPolicyActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#workloadPolicyAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkloadPolicyAction(DorisParser.WorkloadPolicyActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#workloadPolicyConditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkloadPolicyConditions(DorisParser.WorkloadPolicyConditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#workloadPolicyCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkloadPolicyCondition(DorisParser.WorkloadPolicyConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#storageBackend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageBackend(DorisParser.StorageBackendContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#passwordOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPasswordOption(DorisParser.PasswordOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#functionArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArguments(DorisParser.FunctionArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dataTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeList(DorisParser.DataTypeListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setOptions}
	 * labeled alternative in {@link DorisParser#supportedSetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOptions(DorisParser.SetOptionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDefaultStorageVault}
	 * labeled alternative in {@link DorisParser#supportedSetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDefaultStorageVault(DorisParser.SetDefaultStorageVaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setUserProperties}
	 * labeled alternative in {@link DorisParser#supportedSetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetUserProperties(DorisParser.SetUserPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link DorisParser#supportedSetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTransaction(DorisParser.SetTransactionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setVariableWithType}
	 * labeled alternative in {@link DorisParser#optionWithType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetVariableWithType(DorisParser.SetVariableWithTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNames(DorisParser.SetNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCharset(DorisParser.SetCharsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setCollate}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCollate(DorisParser.SetCollateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetPassword(DorisParser.SetPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setLdapAdminPassword}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetLdapAdminPassword(DorisParser.SetLdapAdminPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setVariableWithoutType}
	 * labeled alternative in {@link DorisParser#optionWithoutType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetVariableWithoutType(DorisParser.SetVariableWithoutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setSystemVariable}
	 * labeled alternative in {@link DorisParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSystemVariable(DorisParser.SetSystemVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setUserVariable}
	 * labeled alternative in {@link DorisParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetUserVariable(DorisParser.SetUserVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#transactionAccessMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionAccessMode(DorisParser.TransactionAccessModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#isolationLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsolationLevel(DorisParser.IsolationLevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#supportedUnsetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupportedUnsetStatement(DorisParser.SupportedUnsetStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code switchCatalog}
	 * labeled alternative in {@link DorisParser#supportedUseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCatalog(DorisParser.SwitchCatalogContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useDatabase}
	 * labeled alternative in {@link DorisParser#supportedUseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseDatabase(DorisParser.UseDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useCloudCluster}
	 * labeled alternative in {@link DorisParser#supportedUseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseCloudCluster(DorisParser.UseCloudClusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#stageAndPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStageAndPattern(DorisParser.StageAndPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeTableValuedFunction}
	 * labeled alternative in {@link DorisParser#supportedDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeTableValuedFunction(DorisParser.DescribeTableValuedFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeTableAll}
	 * labeled alternative in {@link DorisParser#supportedDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeTableAll(DorisParser.DescribeTableAllContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeTable}
	 * labeled alternative in {@link DorisParser#supportedDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeTable(DorisParser.DescribeTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code describeDictionary}
	 * labeled alternative in {@link DorisParser#supportedDescribeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeDictionary(DorisParser.DescribeDictionaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(DorisParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionSpec(DorisParser.PartitionSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionTable(DorisParser.PartitionTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identityOrFunctionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentityOrFunctionList(DorisParser.IdentityOrFunctionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identityOrFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentityOrFunction(DorisParser.IdentityOrFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dataDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataDesc(DorisParser.DataDescContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#statementScope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementScope(DorisParser.StatementScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#buildMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuildMode(DorisParser.BuildModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#refreshTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshTrigger(DorisParser.RefreshTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#refreshSchedule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshSchedule(DorisParser.RefreshScheduleContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#refreshMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshMethod(DorisParser.RefreshMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#mvPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMvPartition(DorisParser.MvPartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifierOrText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierOrText(DorisParser.IdentifierOrTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifierOrTextOrAsterisk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierOrTextOrAsterisk(DorisParser.IdentifierOrTextOrAsteriskContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#multipartIdentifierOrAsterisk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipartIdentifierOrAsterisk(DorisParser.MultipartIdentifierOrAsteriskContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifierOrAsterisk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierOrAsterisk(DorisParser.IdentifierOrAsteriskContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#userIdentify}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserIdentify(DorisParser.UserIdentifyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#grantUserIdentify}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantUserIdentify(DorisParser.GrantUserIdentifyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#explain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain(DorisParser.ExplainContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#explainCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainCommand(DorisParser.ExplainCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#planType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlanType(DorisParser.PlanTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#replayCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplayCommand(DorisParser.ReplayCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#replayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplayType(DorisParser.ReplayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#mergeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeType(DorisParser.MergeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#preFilterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreFilterClause(DorisParser.PreFilterClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#deleteOnClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteOnClause(DorisParser.DeleteOnClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#sequenceColClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceColClause(DorisParser.SequenceColClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#colFromPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColFromPath(DorisParser.ColFromPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#colMappingList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColMappingList(DorisParser.ColMappingListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#mappingExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingExpr(DorisParser.MappingExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#withRemoteStorageSystem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithRemoteStorageSystem(DorisParser.WithRemoteStorageSystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#resourceDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResourceDesc(DorisParser.ResourceDescContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#mysqlDataDesc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMysqlDataDesc(DorisParser.MysqlDataDescContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#skipLines}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkipLines(DorisParser.SkipLinesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#outFileClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutFileClause(DorisParser.OutFileClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(DorisParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryTermDefault(DorisParser.QueryTermDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOperation(DorisParser.SetOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#setQuantifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetQuantifier(DorisParser.SetQuantifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryPrimaryDefault(DorisParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(DorisParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valuesTable}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesTable(DorisParser.ValuesTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regularQuerySpecification}
	 * labeled alternative in {@link DorisParser#querySpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularQuerySpecification(DorisParser.RegularQuerySpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#cte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte(DorisParser.CteContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#aliasQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasQuery(DorisParser.AliasQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#columnAliases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAliases(DorisParser.ColumnAliasesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#selectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectClause(DorisParser.SelectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#selectColumnClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectColumnClause(DorisParser.SelectColumnClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(DorisParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(DorisParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#intoClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntoClause(DorisParser.IntoClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#bulkCollectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBulkCollectClause(DorisParser.BulkCollectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#tableRow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableRow(DorisParser.TableRowContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#relations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelations(DorisParser.RelationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(DorisParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#joinRelation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinRelation(DorisParser.JoinRelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#distributeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistributeType(DorisParser.DistributeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#skewHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkewHint(DorisParser.SkewHintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#constantList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantList(DorisParser.ConstantListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bracketRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketRelationHint(DorisParser.BracketRelationHintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code commentRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentRelationHint(DorisParser.CommentRelationHintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#expressionWithOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionWithOrder(DorisParser.ExpressionWithOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#aggClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggClause(DorisParser.AggClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#groupingElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingElement(DorisParser.GroupingElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#groupingSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingSet(DorisParser.GroupingSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(DorisParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#qualifyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifyClause(DorisParser.QualifyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#selectHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectHint(DorisParser.SelectHintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#hintStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHintStatement(DorisParser.HintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#hintName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHintName(DorisParser.HintNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#hintAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHintAssignment(DorisParser.HintAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#updateAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateAssignment(DorisParser.UpdateAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#updateAssignmentSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateAssignmentSeq(DorisParser.UpdateAssignmentSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#lateralView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLateralView(DorisParser.LateralViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#queryOrganization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOrganization(DorisParser.QueryOrganizationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#sortClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortClause(DorisParser.SortClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#sortItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortItem(DorisParser.SortItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(DorisParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionClause(DorisParser.PartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#joinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinType(DorisParser.JoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#joinCriteria}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinCriteria(DorisParser.JoinCriteriaContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(DorisParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierSeq(DorisParser.IdentifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#optScanParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptScanParams(DorisParser.OptScanParamsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(DorisParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aliasedQuery}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasedQuery(DorisParser.AliasedQueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableValuedFunction}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableValuedFunction(DorisParser.TableValuedFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relationList}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationList(DorisParser.RelationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#materializedViewName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaterializedViewName(DorisParser.MaterializedViewNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#propertyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyClause(DorisParser.PropertyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#propertyItemList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyItemList(DorisParser.PropertyItemListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#propertyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyItem(DorisParser.PropertyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#propertyKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyKey(DorisParser.PropertyKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#propertyValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyValue(DorisParser.PropertyValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#tableAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableAlias(DorisParser.TableAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipartIdentifier(DorisParser.MultipartIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#simpleColumnDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleColumnDefs(DorisParser.SimpleColumnDefsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#simpleColumnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleColumnDef(DorisParser.SimpleColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#columnDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDefs(DorisParser.ColumnDefsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#columnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDef(DorisParser.ColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#indexDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexDefs(DorisParser.IndexDefsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#indexDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexDef(DorisParser.IndexDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionsDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionsDef(DorisParser.PartitionsDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDef(DorisParser.PartitionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#lessThanPartitionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanPartitionDef(DorisParser.LessThanPartitionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#fixedPartitionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFixedPartitionDef(DorisParser.FixedPartitionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#stepPartitionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepPartitionDef(DorisParser.StepPartitionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#inPartitionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPartitionDef(DorisParser.InPartitionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionValueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionValueList(DorisParser.PartitionValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#partitionValueDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionValueDef(DorisParser.PartitionValueDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#rollupDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollupDefs(DorisParser.RollupDefsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#rollupDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollupDef(DorisParser.RollupDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#aggTypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggTypeDef(DorisParser.AggTypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#tabletList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTabletList(DorisParser.TabletListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#inlineTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineTable(DorisParser.InlineTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#namedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedExpression(DorisParser.NamedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#namedExpressionSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedExpressionSeq(DorisParser.NamedExpressionSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(DorisParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#funcExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncExpression(DorisParser.FuncExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaExpression(DorisParser.LambdaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exist}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExist(DorisParser.ExistContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalNot(DorisParser.LogicalNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicated(DorisParser.PredicatedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isnull}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsnull(DorisParser.IsnullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code is_not_null_pred}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIs_not_null_pred(DorisParser.Is_not_null_predContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalBinary(DorisParser.LogicalBinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doublePipes}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoublePipes(DorisParser.DoublePipesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#rowConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowConstructor(DorisParser.RowConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#rowConstructorItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowConstructorItem(DorisParser.RowConstructorItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(DorisParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpressionDefault(DorisParser.ValueExpressionDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(DorisParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticBinary(DorisParser.ArithmeticBinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticUnary(DorisParser.ArithmeticUnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDereference(DorisParser.DereferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentDate}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentDate(DorisParser.CurrentDateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast(DorisParser.CastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(DorisParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trim}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrim(DorisParser.TrimContext ctx);
	/**
	 * Visit a parse tree produced by the {@code userVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserVariable(DorisParser.UserVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code elementAt}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementAt(DorisParser.ElementAtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code localTimestamp}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalTimestamp(DorisParser.LocalTimestampContext ctx);
	/**
	 * Visit a parse tree produced by the {@code charFunction}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharFunction(DorisParser.CharFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalLiteral(DorisParser.IntervalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code groupConcat}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupConcat(DorisParser.GroupConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleCase(DorisParser.SimpleCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnReference(DorisParser.ColumnReferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code star}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar(DorisParser.StarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sessionUser}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSessionUser(DorisParser.SessionUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code convertType}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvertType(DorisParser.ConvertTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code convertCharSet}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvertCharSet(DorisParser.ConvertCharSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryExpression(DorisParser.SubqueryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code encryptKey}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncryptKey(DorisParser.EncryptKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentTime}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentTime(DorisParser.CurrentTimeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code localTime}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalTime(DorisParser.LocalTimeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code systemVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemVariable(DorisParser.SystemVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code collate}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollate(DorisParser.CollateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentUser}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentUser(DorisParser.CurrentUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constantDefault}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDefault(DorisParser.ConstantDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code extract}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtract(DorisParser.ExtractContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentTimestamp}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentTimestamp(DorisParser.CurrentTimestampContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(DorisParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arraySlice}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraySlice(DorisParser.ArraySliceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchedCase(DorisParser.SearchedCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code except}
	 * labeled alternative in {@link DorisParser#exceptOrReplace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExcept(DorisParser.ExceptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replace}
	 * labeled alternative in {@link DorisParser#exceptOrReplace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace(DorisParser.ReplaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#castDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastDataType(DorisParser.CastDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#functionCallExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpression(DorisParser.FunctionCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#functionIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionIdentifier(DorisParser.FunctionIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#functionNameIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionNameIdentifier(DorisParser.FunctionNameIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#windowSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowSpec(DorisParser.WindowSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#windowFrame}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowFrame(DorisParser.WindowFrameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#frameUnits}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameUnits(DorisParser.FrameUnitsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#frameBoundary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameBoundary(DorisParser.FrameBoundaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(DorisParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#specifiedPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecifiedPartition(DorisParser.SpecifiedPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(DorisParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeConstructor(DorisParser.TypeConstructorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(DorisParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(DorisParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(DorisParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(DorisParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mapLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(DorisParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code structLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructLiteral(DorisParser.StructLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code placeholder}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlaceholder(DorisParser.PlaceholderContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(DorisParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#booleanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(DorisParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#whenClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenClause(DorisParser.WhenClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval(DorisParser.IntervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#unitIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitIdentifier(DorisParser.UnitIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#dataTypeWithNullable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeWithNullable(DorisParser.DataTypeWithNullableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexDataType(DorisParser.ComplexDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variantPredefinedFields}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantPredefinedFields(DorisParser.VariantPredefinedFieldsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggStateDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggStateDataType(DorisParser.AggStateDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveDataType(DorisParser.PrimitiveDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#primitiveColType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveColType(DorisParser.PrimitiveColTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#complexColTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexColTypeList(DorisParser.ComplexColTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#complexColType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexColType(DorisParser.ComplexColTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variant}
	 * labeled alternative in {@link DorisParser#variantTypeDefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariant(DorisParser.VariantContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#variantSubColTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantSubColTypeList(DorisParser.VariantSubColTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#variantSubColType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantSubColType(DorisParser.VariantSubColTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#variantSubColMatchType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantSubColMatchType(DorisParser.VariantSubColMatchTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#commentSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentSpec(DorisParser.CommentSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#sample}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSample(DorisParser.SampleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sampleByPercentile}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSampleByPercentile(DorisParser.SampleByPercentileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sampleByRows}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSampleByRows(DorisParser.SampleByRowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#tableSnapshot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSnapshot(DorisParser.TableSnapshotContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#errorCapturingIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorCapturingIdentifier(DorisParser.ErrorCapturingIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code errorIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorIdent(DorisParser.ErrorIdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code realIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealIdent(DorisParser.RealIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(DorisParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedIdentifier(DorisParser.UnquotedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code quotedIdentifierAlternative}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedIdentifierAlternative(DorisParser.QuotedIdentifierAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedIdentifier(DorisParser.QuotedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(DorisParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(DorisParser.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link DorisParser#nonReserved}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonReserved(DorisParser.NonReservedContext ctx);
}