// Generated from MongoParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.mongodb.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MongoParser}.
 */
public interface MongoParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MongoParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(MongoParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(MongoParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(MongoParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(MongoParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#functionCommand}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCommand(MongoParser.FunctionCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#functionCommand}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCommand(MongoParser.FunctionCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUse(MongoParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUse(MongoParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbDropDatabase}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbDropDatabase(MongoParser.DbDropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbDropDatabase}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbCreateCollection}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbCreateCollection(MongoParser.DbCreateCollectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbCreateCollection}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbCreateView}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbCreateView(MongoParser.DbCreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbCreateView}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbCreateView(MongoParser.DbCreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbStats}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbStats(MongoParser.DbStatsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbStats}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbStats(MongoParser.DbStatsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setProfilingLevel}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setProfilingLevel}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getCollectionNames}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetCollectionNames(MongoParser.GetCollectionNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getCollectionNames}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getCollectionInfos}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getCollectionInfos}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getLogComponents}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetLogComponents(MongoParser.GetLogComponentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getLogComponents}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetLogComponents(MongoParser.GetLogComponentsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getProfilingStatus}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getProfilingStatus}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hostInfo}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterHostInfo(MongoParser.HostInfoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hostInfo}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitHostInfo(MongoParser.HostInfoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code adminCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterAdminCommand(MongoParser.AdminCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code adminCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitAdminCommand(MongoParser.AdminCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code runCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterRunCommand(MongoParser.RunCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code runCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitRunCommand(MongoParser.RunCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbAggregate}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbAggregate(MongoParser.DbAggregateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbAggregate}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbAggregate(MongoParser.DbAggregateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterCurrentOp(MongoParser.CurrentOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitCurrentOp(MongoParser.CurrentOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code killOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterKillOp(MongoParser.KillOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code killOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitKillOp(MongoParser.KillOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fsyncLock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterFsyncLock(MongoParser.FsyncLockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fsyncLock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitFsyncLock(MongoParser.FsyncLockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fsyncUnlock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void enterFsyncUnlock(MongoParser.FsyncUnlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fsyncUnlock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 */
	void exitFsyncUnlock(MongoParser.FsyncUnlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 */
	void enterShowDatabases(MongoParser.ShowDatabasesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 */
	void exitShowDatabases(MongoParser.ShowDatabasesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCollections}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 */
	void enterShowCollections(MongoParser.ShowCollectionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCollections}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 */
	void exitShowCollections(MongoParser.ShowCollectionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbServerBuildInfo}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbServerBuildInfo}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbHello}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbHello(MongoParser.DbHelloContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbHello}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbHello(MongoParser.DbHelloContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbServerStatus}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void enterDbServerStatus(MongoParser.DbServerStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbServerStatus}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 */
	void exitDbServerStatus(MongoParser.DbServerStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#collectionFunction}.
	 * @param ctx the parse tree
	 */
	void enterCollectionFunction(MongoParser.CollectionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#collectionFunction}.
	 * @param ctx the parse tree
	 */
	void exitCollectionFunction(MongoParser.CollectionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#collectionMethod}.
	 * @param ctx the parse tree
	 */
	void enterCollectionMethod(MongoParser.CollectionMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#collectionMethod}.
	 * @param ctx the parse tree
	 */
	void exitCollectionMethod(MongoParser.CollectionMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#dataSize}.
	 * @param ctx the parse tree
	 */
	void enterDataSize(MongoParser.DataSizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#dataSize}.
	 * @param ctx the parse tree
	 */
	void exitDataSize(MongoParser.DataSizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#explain}.
	 * @param ctx the parse tree
	 */
	void enterExplain(MongoParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#explain}.
	 * @param ctx the parse tree
	 */
	void exitExplain(MongoParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#explainMethod}.
	 * @param ctx the parse tree
	 */
	void enterExplainMethod(MongoParser.ExplainMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#explainMethod}.
	 * @param ctx the parse tree
	 */
	void exitExplainMethod(MongoParser.ExplainMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#validate}.
	 * @param ctx the parse tree
	 */
	void enterValidate(MongoParser.ValidateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#validate}.
	 * @param ctx the parse tree
	 */
	void exitValidate(MongoParser.ValidateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#replaceOne}.
	 * @param ctx the parse tree
	 */
	void enterReplaceOne(MongoParser.ReplaceOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#replaceOne}.
	 * @param ctx the parse tree
	 */
	void exitReplaceOne(MongoParser.ReplaceOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#renameCollection}.
	 * @param ctx the parse tree
	 */
	void enterRenameCollection(MongoParser.RenameCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#renameCollection}.
	 * @param ctx the parse tree
	 */
	void exitRenameCollection(MongoParser.RenameCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#latencyStats}.
	 * @param ctx the parse tree
	 */
	void enterLatencyStats(MongoParser.LatencyStatsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#latencyStats}.
	 * @param ctx the parse tree
	 */
	void exitLatencyStats(MongoParser.LatencyStatsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#getIndexes}.
	 * @param ctx the parse tree
	 */
	void enterGetIndexes(MongoParser.GetIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#getIndexes}.
	 * @param ctx the parse tree
	 */
	void exitGetIndexes(MongoParser.GetIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#hideIndex}.
	 * @param ctx the parse tree
	 */
	void enterHideIndex(MongoParser.HideIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#hideIndex}.
	 * @param ctx the parse tree
	 */
	void exitHideIndex(MongoParser.HideIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#estimatedDocumentCount}.
	 * @param ctx the parse tree
	 */
	void enterEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#estimatedDocumentCount}.
	 * @param ctx the parse tree
	 */
	void exitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(MongoParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(MongoParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(MongoParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(MongoParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#dropIndexes}.
	 * @param ctx the parse tree
	 */
	void enterDropIndexes(MongoParser.DropIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#dropIndexes}.
	 * @param ctx the parse tree
	 */
	void exitDropIndexes(MongoParser.DropIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#distinct}.
	 * @param ctx the parse tree
	 */
	void enterDistinct(MongoParser.DistinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#distinct}.
	 * @param ctx the parse tree
	 */
	void exitDistinct(MongoParser.DistinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(MongoParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(MongoParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#createIndexes}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndexes(MongoParser.CreateIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#createIndexes}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndexes(MongoParser.CreateIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#aggregate}.
	 * @param ctx the parse tree
	 */
	void enterAggregate(MongoParser.AggregateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#aggregate}.
	 * @param ctx the parse tree
	 */
	void exitAggregate(MongoParser.AggregateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#countDocuments}.
	 * @param ctx the parse tree
	 */
	void enterCountDocuments(MongoParser.CountDocumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#countDocuments}.
	 * @param ctx the parse tree
	 */
	void exitCountDocuments(MongoParser.CountDocumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#find}.
	 * @param ctx the parse tree
	 */
	void enterFind(MongoParser.FindContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#find}.
	 * @param ctx the parse tree
	 */
	void exitFind(MongoParser.FindContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findSkipConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindSkipConstraint(MongoParser.FindSkipConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findSkipConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindSkipConstraint(MongoParser.FindSkipConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findLimitConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindLimitConstraint(MongoParser.FindLimitConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findLimitConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindLimitConstraint(MongoParser.FindLimitConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findSortConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindSortConstraint(MongoParser.FindSortConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findSortConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindSortConstraint(MongoParser.FindSortConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findCommentConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindCommentConstraint(MongoParser.FindCommentConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findCommentConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindCommentConstraint(MongoParser.FindCommentConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findMaxTimeMSConstriant}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindMaxTimeMSConstriant(MongoParser.FindMaxTimeMSConstriantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findMaxTimeMSConstriant}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindMaxTimeMSConstriant(MongoParser.FindMaxTimeMSConstriantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findCollationConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindCollationConstraint(MongoParser.FindCollationConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findCollationConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindCollationConstraint(MongoParser.FindCollationConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findAllowDiskUseConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindAllowDiskUseConstraint(MongoParser.FindAllowDiskUseConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findAllowDiskUseConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindAllowDiskUseConstraint(MongoParser.FindAllowDiskUseConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findMaxConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindMaxConstraint(MongoParser.FindMaxConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findMaxConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindMaxConstraint(MongoParser.FindMaxConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findMinConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindMinConstraint(MongoParser.FindMinConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findMinConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindMinConstraint(MongoParser.FindMinConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findReturnKeyConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindReturnKeyConstraint(MongoParser.FindReturnKeyConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findReturnKeyConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindReturnKeyConstraint(MongoParser.FindReturnKeyConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findShowRecordIdConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindShowRecordIdConstraint(MongoParser.FindShowRecordIdConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findShowRecordIdConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindShowRecordIdConstraint(MongoParser.FindShowRecordIdConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findLetConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterFindLetConstraint(MongoParser.FindLetConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findLetConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitFindLetConstraint(MongoParser.FindLetConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void enterExplainConstraint(MongoParser.ExplainConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 */
	void exitExplainConstraint(MongoParser.ExplainConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#findOne}.
	 * @param ctx the parse tree
	 */
	void enterFindOne(MongoParser.FindOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#findOne}.
	 * @param ctx the parse tree
	 */
	void exitFindOne(MongoParser.FindOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#findOneAndDelete}.
	 * @param ctx the parse tree
	 */
	void enterFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#findOneAndDelete}.
	 * @param ctx the parse tree
	 */
	void exitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#findOneAndReplace}.
	 * @param ctx the parse tree
	 */
	void enterFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#findOneAndReplace}.
	 * @param ctx the parse tree
	 */
	void exitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#findOneAndUpdate}.
	 * @param ctx the parse tree
	 */
	void enterFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#findOneAndUpdate}.
	 * @param ctx the parse tree
	 */
	void exitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#insert}.
	 * @param ctx the parse tree
	 */
	void enterInsert(MongoParser.InsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#insert}.
	 * @param ctx the parse tree
	 */
	void exitInsert(MongoParser.InsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#insertOne}.
	 * @param ctx the parse tree
	 */
	void enterInsertOne(MongoParser.InsertOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#insertOne}.
	 * @param ctx the parse tree
	 */
	void exitInsertOne(MongoParser.InsertOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#insertMany}.
	 * @param ctx the parse tree
	 */
	void enterInsertMany(MongoParser.InsertManyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#insertMany}.
	 * @param ctx the parse tree
	 */
	void exitInsertMany(MongoParser.InsertManyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#deleteOne}.
	 * @param ctx the parse tree
	 */
	void enterDeleteOne(MongoParser.DeleteOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#deleteOne}.
	 * @param ctx the parse tree
	 */
	void exitDeleteOne(MongoParser.DeleteOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#deleteMany}.
	 * @param ctx the parse tree
	 */
	void enterDeleteMany(MongoParser.DeleteManyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#deleteMany}.
	 * @param ctx the parse tree
	 */
	void exitDeleteMany(MongoParser.DeleteManyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(MongoParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(MongoParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#drop}.
	 * @param ctx the parse tree
	 */
	void enterDrop(MongoParser.DropContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#drop}.
	 * @param ctx the parse tree
	 */
	void exitDrop(MongoParser.DropContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#collectionName}.
	 * @param ctx the parse tree
	 */
	void enterCollectionName(MongoParser.CollectionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#collectionName}.
	 * @param ctx the parse tree
	 */
	void exitCollectionName(MongoParser.CollectionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#jsonString}.
	 * @param ctx the parse tree
	 */
	void enterJsonString(MongoParser.JsonStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#jsonString}.
	 * @param ctx the parse tree
	 */
	void exitJsonString(MongoParser.JsonStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#obj}.
	 * @param ctx the parse tree
	 */
	void enterObj(MongoParser.ObjContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#obj}.
	 * @param ctx the parse tree
	 */
	void exitObj(MongoParser.ObjContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(MongoParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(MongoParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(MongoParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(MongoParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#arr}.
	 * @param ctx the parse tree
	 */
	void enterArr(MongoParser.ArrContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#arr}.
	 * @param ctx the parse tree
	 */
	void exitArr(MongoParser.ArrContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(MongoParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(MongoParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MongoParser#keyWordId}.
	 * @param ctx the parse tree
	 */
	void enterKeyWordId(MongoParser.KeyWordIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MongoParser#keyWordId}.
	 * @param ctx the parse tree
	 */
	void exitKeyWordId(MongoParser.KeyWordIdContext ctx);
}