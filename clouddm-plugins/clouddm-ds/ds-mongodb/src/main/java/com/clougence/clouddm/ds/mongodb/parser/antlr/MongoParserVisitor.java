// Generated from MongoParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.mongodb.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MongoParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MongoParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MongoParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(MongoParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(MongoParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#functionCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCommand(MongoParser.FunctionCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse(MongoParser.UseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbDropDatabase}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbCreateCollection}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbCreateView}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbCreateView(MongoParser.DbCreateViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbStats}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbStats(MongoParser.DbStatsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setProfilingLevel}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code getCollectionNames}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code getCollectionInfos}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx);
	/**
	 * Visit a parse tree produced by the {@code getLogComponents}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetLogComponents(MongoParser.GetLogComponentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code getProfilingStatus}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code hostInfo}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHostInfo(MongoParser.HostInfoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adminCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminCommand(MongoParser.AdminCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code runCommand}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRunCommand(MongoParser.RunCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbAggregate}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbAggregate(MongoParser.DbAggregateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code currentOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentOp(MongoParser.CurrentOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code killOp}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillOp(MongoParser.KillOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fsyncLock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsyncLock(MongoParser.FsyncLockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fsyncUnlock}
	 * labeled alternative in {@link MongoParser#databaseFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsyncUnlock(MongoParser.FsyncUnlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabases(MongoParser.ShowDatabasesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCollections}
	 * labeled alternative in {@link MongoParser#showFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCollections(MongoParser.ShowCollectionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbServerBuildInfo}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbHello}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbHello(MongoParser.DbHelloContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbServerStatus}
	 * labeled alternative in {@link MongoParser#environmentFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbServerStatus(MongoParser.DbServerStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#collectionFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionFunction(MongoParser.CollectionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#collectionMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionMethod(MongoParser.CollectionMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#dataSize}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataSize(MongoParser.DataSizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#explain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain(MongoParser.ExplainContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#explainMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainMethod(MongoParser.ExplainMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#validate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidate(MongoParser.ValidateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#replaceOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceOne(MongoParser.ReplaceOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#renameCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameCollection(MongoParser.RenameCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#latencyStats}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLatencyStats(MongoParser.LatencyStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#getIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetIndexes(MongoParser.GetIndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#hideIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHideIndex(MongoParser.HideIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#estimatedDocumentCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(MongoParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#dropIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndex(MongoParser.DropIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#dropIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndexes(MongoParser.DropIndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#distinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistinct(MongoParser.DistinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#createIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndex(MongoParser.CreateIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#createIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndexes(MongoParser.CreateIndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#aggregate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate(MongoParser.AggregateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#countDocuments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountDocuments(MongoParser.CountDocumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#find}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFind(MongoParser.FindContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findSkipConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindSkipConstraint(MongoParser.FindSkipConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findLimitConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindLimitConstraint(MongoParser.FindLimitConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findSortConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindSortConstraint(MongoParser.FindSortConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findCommentConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindCommentConstraint(MongoParser.FindCommentConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findMaxTimeMSConstriant}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindMaxTimeMSConstriant(MongoParser.FindMaxTimeMSConstriantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findCollationConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindCollationConstraint(MongoParser.FindCollationConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findAllowDiskUseConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindAllowDiskUseConstraint(MongoParser.FindAllowDiskUseConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findMaxConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindMaxConstraint(MongoParser.FindMaxConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findMinConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindMinConstraint(MongoParser.FindMinConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findReturnKeyConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindReturnKeyConstraint(MongoParser.FindReturnKeyConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findShowRecordIdConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindShowRecordIdConstraint(MongoParser.FindShowRecordIdConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code findLetConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindLetConstraint(MongoParser.FindLetConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code explainConstraint}
	 * labeled alternative in {@link MongoParser#findConstarint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainConstraint(MongoParser.ExplainConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#findOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindOne(MongoParser.FindOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#findOneAndDelete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#findOneAndReplace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#findOneAndUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(MongoParser.InsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#insertOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertOne(MongoParser.InsertOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#insertMany}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertMany(MongoParser.InsertManyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#deleteOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteOne(MongoParser.DeleteOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#deleteMany}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteMany(MongoParser.DeleteManyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(MongoParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop(MongoParser.DropContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#collectionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionName(MongoParser.CollectionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#jsonString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonString(MongoParser.JsonStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#obj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObj(MongoParser.ObjContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(MongoParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(MongoParser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#arr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArr(MongoParser.ArrContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(MongoParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MongoParser#keyWordId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyWordId(MongoParser.KeyWordIdContext ctx);
}