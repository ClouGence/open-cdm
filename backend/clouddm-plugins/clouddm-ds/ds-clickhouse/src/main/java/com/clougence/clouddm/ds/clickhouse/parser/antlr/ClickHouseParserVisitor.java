// Generated from ClickHouseParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.clickhouse.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ClickHouseParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ClickHouseParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(ClickHouseParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryStmtQuery}
	 * labeled alternative in {@link ClickHouseParser#queryStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryStmtQuery(ClickHouseParser.QueryStmtQueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryStmtInsert}
	 * labeled alternative in {@link ClickHouseParser#queryStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryStmtInsert(ClickHouseParser.QueryStmtInsertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryStmtDelete}
	 * labeled alternative in {@link ClickHouseParser#queryStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryStmtDelete(ClickHouseParser.QueryStmtDeleteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryStmtUpdate}
	 * labeled alternative in {@link ClickHouseParser#queryStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryStmtUpdate(ClickHouseParser.QueryStmtUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(ClickHouseParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableStmt}
	 * labeled alternative in {@link ClickHouseParser#alterStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableStmt(ClickHouseParser.AlterTableStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseAddColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseAddColumn(ClickHouseParser.AlterTableClauseAddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseAddIndex}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseAddIndex(ClickHouseParser.AlterTableClauseAddIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseAddProjection}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseAddProjection(ClickHouseParser.AlterTableClauseAddProjectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseAttach}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseAttach(ClickHouseParser.AlterTableClauseAttachContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseClearColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseClearColumn(ClickHouseParser.AlterTableClauseClearColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseClearIndex}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseClearIndex(ClickHouseParser.AlterTableClauseClearIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseClearProjection}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseClearProjection(ClickHouseParser.AlterTableClauseClearProjectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseComment}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseComment(ClickHouseParser.AlterTableClauseCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDelete}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDelete(ClickHouseParser.AlterTableClauseDeleteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDetach}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDetach(ClickHouseParser.AlterTableClauseDetachContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDropColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDropColumn(ClickHouseParser.AlterTableClauseDropColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDropIndex}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDropIndex(ClickHouseParser.AlterTableClauseDropIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDropProjection}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDropProjection(ClickHouseParser.AlterTableClauseDropProjectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseDropPartition}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseDropPartition(ClickHouseParser.AlterTableClauseDropPartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseFreezePartition}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseFreezePartition(ClickHouseParser.AlterTableClauseFreezePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseMaterializeIndex}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseMaterializeIndex(ClickHouseParser.AlterTableClauseMaterializeIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseMaterializeProjection}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseMaterializeProjection(ClickHouseParser.AlterTableClauseMaterializeProjectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseModifyOrderBy}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseModifyOrderBy(ClickHouseParser.AlterTableClauseModifyOrderByContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseModifyTTL}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseModifyTTL(ClickHouseParser.AlterTableClauseModifyTTLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseMovePartition}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseMovePartition(ClickHouseParser.AlterTableClauseMovePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseRemoveTTL}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseRemoveTTL(ClickHouseParser.AlterTableClauseRemoveTTLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseRenameColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseRenameColumn(ClickHouseParser.AlterTableClauseRenameColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseReplace}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseReplace(ClickHouseParser.AlterTableClauseReplaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableClauseUpdate}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClauseUpdate(ClickHouseParser.AlterTableClauseUpdateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableModifyComment}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableModifyComment(ClickHouseParser.AlterTableModifyCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableModifyColumnDefault}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableModifyColumnDefault(ClickHouseParser.AlterTableModifyColumnDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableAlterColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableAlterColumn(ClickHouseParser.AlterTableAlterColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlterTableModifyColumn}
	 * labeled alternative in {@link ClickHouseParser#alterTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableModifyColumn(ClickHouseParser.AlterTableModifyColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#assignmentExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExprList(ClickHouseParser.AssignmentExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#assignmentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpr(ClickHouseParser.AssignmentExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableColumnPropertyType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableColumnPropertyType(ClickHouseParser.TableColumnPropertyTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#partitionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionClause(ClickHouseParser.PartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AttachDictionaryStmt}
	 * labeled alternative in {@link ClickHouseParser#attachStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttachDictionaryStmt(ClickHouseParser.AttachDictionaryStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#checkStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckStmt(ClickHouseParser.CheckStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CreateDatabaseStmt}
	 * labeled alternative in {@link ClickHouseParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabaseStmt(ClickHouseParser.CreateDatabaseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CreateDictionaryStmt}
	 * labeled alternative in {@link ClickHouseParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDictionaryStmt(ClickHouseParser.CreateDictionaryStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CreateMaterializedViewStmt}
	 * labeled alternative in {@link ClickHouseParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateMaterializedViewStmt(ClickHouseParser.CreateMaterializedViewStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CreateTableStmt}
	 * labeled alternative in {@link ClickHouseParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableStmt(ClickHouseParser.CreateTableStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CreateViewStmt}
	 * labeled alternative in {@link ClickHouseParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateViewStmt(ClickHouseParser.CreateViewStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionarySchemaClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionarySchemaClause(ClickHouseParser.DictionarySchemaClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionaryAttrDfnt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryAttrDfnt(ClickHouseParser.DictionaryAttrDfntContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionaryEngineClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryEngineClause(ClickHouseParser.DictionaryEngineClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionaryPrimaryKeyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryPrimaryKeyClause(ClickHouseParser.DictionaryPrimaryKeyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionaryArgExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryArgExpr(ClickHouseParser.DictionaryArgExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#sourceClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceClause(ClickHouseParser.SourceClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#lifetimeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLifetimeClause(ClickHouseParser.LifetimeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#layoutClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLayoutClause(ClickHouseParser.LayoutClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#rangeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeClause(ClickHouseParser.RangeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionarySettingsClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionarySettingsClause(ClickHouseParser.DictionarySettingsClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#clusterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterClause(ClickHouseParser.ClusterClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#uuidClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUuidClause(ClickHouseParser.UuidClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#destinationClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDestinationClause(ClickHouseParser.DestinationClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#subqueryClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryClause(ClickHouseParser.SubqueryClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SchemaDescriptionClause}
	 * labeled alternative in {@link ClickHouseParser#tableSchemaClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaDescriptionClause(ClickHouseParser.SchemaDescriptionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SchemaAsTableClause}
	 * labeled alternative in {@link ClickHouseParser#tableSchemaClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaAsTableClause(ClickHouseParser.SchemaAsTableClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SchemaAsFunctionClause}
	 * labeled alternative in {@link ClickHouseParser#tableSchemaClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaAsFunctionClause(ClickHouseParser.SchemaAsFunctionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#engineClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEngineClause(ClickHouseParser.EngineClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#partitionByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionByClause(ClickHouseParser.PartitionByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#primaryKeyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryKeyClause(ClickHouseParser.PrimaryKeyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#sampleByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSampleByClause(ClickHouseParser.SampleByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#ttlClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTtlClause(ClickHouseParser.TtlClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#commentClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentClause(ClickHouseParser.CommentClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#engineExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEngineExpr(ClickHouseParser.EngineExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableElementExprPri}
	 * labeled alternative in {@link ClickHouseParser#tableElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementExprPri(ClickHouseParser.TableElementExprPriContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableElementExprColumn}
	 * labeled alternative in {@link ClickHouseParser#tableElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementExprColumn(ClickHouseParser.TableElementExprColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableElementExprConstraint}
	 * labeled alternative in {@link ClickHouseParser#tableElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementExprConstraint(ClickHouseParser.TableElementExprConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableElementExprIndex}
	 * labeled alternative in {@link ClickHouseParser#tableElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementExprIndex(ClickHouseParser.TableElementExprIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableElementExprProjection}
	 * labeled alternative in {@link ClickHouseParser#tableElementExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementExprProjection(ClickHouseParser.TableElementExprProjectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableColumnDfnt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableColumnDfnt(ClickHouseParser.TableColumnDfntContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableColumnPropertyExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableColumnPropertyExpr(ClickHouseParser.TableColumnPropertyExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableIndexDfnt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIndexDfnt(ClickHouseParser.TableIndexDfntContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableProjectionDfnt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableProjectionDfnt(ClickHouseParser.TableProjectionDfntContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#codecExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodecExpr(ClickHouseParser.CodecExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#codecArgExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodecArgExpr(ClickHouseParser.CodecArgExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#ttlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTtlExpr(ClickHouseParser.TtlExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#ttlSetExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTtlSetExpr(ClickHouseParser.TtlSetExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#describeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeStmt(ClickHouseParser.DescribeStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DropDatabaseStmt}
	 * labeled alternative in {@link ClickHouseParser#dropStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabaseStmt(ClickHouseParser.DropDatabaseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DropTableStmt}
	 * labeled alternative in {@link ClickHouseParser#dropStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTableStmt(ClickHouseParser.DropTableStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistsDatabaseStmt}
	 * labeled alternative in {@link ClickHouseParser#existsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsDatabaseStmt(ClickHouseParser.ExistsDatabaseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistsTableStmt}
	 * labeled alternative in {@link ClickHouseParser#existsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsTableStmt(ClickHouseParser.ExistsTableStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#explainStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainStmt(ClickHouseParser.ExplainStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#insertStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStmt(ClickHouseParser.InsertStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnsClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnsClause(ClickHouseParser.ColumnsClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DataClauseFormat}
	 * labeled alternative in {@link ClickHouseParser#dataClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataClauseFormat(ClickHouseParser.DataClauseFormatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DataClauseValues}
	 * labeled alternative in {@link ClickHouseParser#dataClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataClauseValues(ClickHouseParser.DataClauseValuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DataClauseSelect}
	 * labeled alternative in {@link ClickHouseParser#dataClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataClauseSelect(ClickHouseParser.DataClauseSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#assignmentValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentValues(ClickHouseParser.AssignmentValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#assignmentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentValue(ClickHouseParser.AssignmentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#deleteStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStmt(ClickHouseParser.DeleteStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#inPartitionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPartitionClause(ClickHouseParser.InPartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#updateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateStmt(ClickHouseParser.UpdateStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code KillMutationStmt}
	 * labeled alternative in {@link ClickHouseParser#killStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillMutationStmt(ClickHouseParser.KillMutationStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#optimizeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptimizeStmt(ClickHouseParser.OptimizeStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#renameStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameStmt(ClickHouseParser.RenameStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#renameEntityClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameEntityClause(ClickHouseParser.RenameEntityClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#projectionSelectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjectionSelectStmt(ClickHouseParser.ProjectionSelectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#selectUnionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectUnionStmt(ClickHouseParser.SelectUnionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#selectStmtWithParens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStmtWithParens(ClickHouseParser.SelectStmtWithParensContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#selectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStmt(ClickHouseParser.SelectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#withClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithClause(ClickHouseParser.WithClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#withExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithExprList(ClickHouseParser.WithExprListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WithExprSubquery}
	 * labeled alternative in {@link ClickHouseParser#withExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithExprSubquery(ClickHouseParser.WithExprSubqueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WithExprExpression}
	 * labeled alternative in {@link ClickHouseParser#withExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithExprExpression(ClickHouseParser.WithExprExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#topClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopClause(ClickHouseParser.TopClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(ClickHouseParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#arrayJoinClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayJoinClause(ClickHouseParser.ArrayJoinClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#windowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowClause(ClickHouseParser.WindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#prewhereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrewhereClause(ClickHouseParser.PrewhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#qualifyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifyClause(ClickHouseParser.QualifyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(ClickHouseParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GroupByClauseAll}
	 * labeled alternative in {@link ClickHouseParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClauseAll(ClickHouseParser.GroupByClauseAllContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GroupByClauseCubeOrRollup}
	 * labeled alternative in {@link ClickHouseParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClauseCubeOrRollup(ClickHouseParser.GroupByClauseCubeOrRollupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GroupByClauseGroupingSets}
	 * labeled alternative in {@link ClickHouseParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClauseGroupingSets(ClickHouseParser.GroupByClauseGroupingSetsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GroupByClauseSimple}
	 * labeled alternative in {@link ClickHouseParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClauseSimple(ClickHouseParser.GroupByClauseSimpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(ClickHouseParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(ClickHouseParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#interpolateClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterpolateClause(ClickHouseParser.InterpolateClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#projectionOrderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjectionOrderByClause(ClickHouseParser.ProjectionOrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#limitByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitByClause(ClickHouseParser.LimitByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(ClickHouseParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#settingsClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSettingsClause(ClickHouseParser.SettingsClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinExprOp}
	 * labeled alternative in {@link ClickHouseParser#joinExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinExprOp(ClickHouseParser.JoinExprOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinExprTable}
	 * labeled alternative in {@link ClickHouseParser#joinExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinExprTable(ClickHouseParser.JoinExprTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinExprParens}
	 * labeled alternative in {@link ClickHouseParser#joinExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinExprParens(ClickHouseParser.JoinExprParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinExprCrossOp}
	 * labeled alternative in {@link ClickHouseParser#joinExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinExprCrossOp(ClickHouseParser.JoinExprCrossOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinOpInner}
	 * labeled alternative in {@link ClickHouseParser#joinOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinOpInner(ClickHouseParser.JoinOpInnerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinOpLeftRight}
	 * labeled alternative in {@link ClickHouseParser#joinOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinOpLeftRight(ClickHouseParser.JoinOpLeftRightContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JoinOpFull}
	 * labeled alternative in {@link ClickHouseParser#joinOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinOpFull(ClickHouseParser.JoinOpFullContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#joinOpCross}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinOpCross(ClickHouseParser.JoinOpCrossContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#joinConstraintClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinConstraintClause(ClickHouseParser.JoinConstraintClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#sampleClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSampleClause(ClickHouseParser.SampleClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#limitExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitExpr(ClickHouseParser.LimitExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#orderExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderExprList(ClickHouseParser.OrderExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#orderExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderExpr(ClickHouseParser.OrderExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#ratioExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRatioExpr(ClickHouseParser.RatioExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#settingExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSettingExprList(ClickHouseParser.SettingExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#settingExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSettingExpr(ClickHouseParser.SettingExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#windowExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowExpr(ClickHouseParser.WindowExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#winPartitionByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWinPartitionByClause(ClickHouseParser.WinPartitionByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#winOrderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWinOrderByClause(ClickHouseParser.WinOrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#winFrameClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWinFrameClause(ClickHouseParser.WinFrameClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code frameStart}
	 * labeled alternative in {@link ClickHouseParser#winFrameExtend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameStart(ClickHouseParser.FrameStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code frameBetween}
	 * labeled alternative in {@link ClickHouseParser#winFrameExtend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameBetween(ClickHouseParser.FrameBetweenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#winFrameBound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWinFrameBound(ClickHouseParser.WinFrameBoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#setStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStmt(ClickHouseParser.SetStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateDatabaseStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateDatabaseStmt(ClickHouseParser.ShowCreateDatabaseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateDictionaryStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateDictionaryStmt(ClickHouseParser.ShowCreateDictionaryStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateViewStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateViewStmt(ClickHouseParser.ShowCreateViewStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatabasesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabasesStmt(ClickHouseParser.ShowDatabasesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDictionariesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDictionariesStmt(ClickHouseParser.ShowDictionariesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTablesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTablesStmt(ClickHouseParser.ShowTablesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showColumnsStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumnsStmt(ClickHouseParser.ShowColumnsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showIndexStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndexStmt(ClickHouseParser.ShowIndexStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProcessListStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcessListStmt(ClickHouseParser.ShowProcessListStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGrantsStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrantsStmt(ClickHouseParser.ShowGrantsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateUserStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateUserStmt(ClickHouseParser.ShowCreateUserStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateRoleStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateRoleStmt(ClickHouseParser.ShowCreateRoleStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreatePolicyStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreatePolicyStmt(ClickHouseParser.ShowCreatePolicyStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateQuotaStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateQuotaStmt(ClickHouseParser.ShowCreateQuotaStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateProfileStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateProfileStmt(ClickHouseParser.ShowCreateProfileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showUsersStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowUsersStmt(ClickHouseParser.ShowUsersStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRolesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRolesStmt(ClickHouseParser.ShowRolesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProfilesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfilesStmt(ClickHouseParser.ShowProfilesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPoliciesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPoliciesStmt(ClickHouseParser.ShowPoliciesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showQuotasStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowQuotasStmt(ClickHouseParser.ShowQuotasStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showQuotaStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowQuotaStmt(ClickHouseParser.ShowQuotaStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showAccessStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowAccessStmt(ClickHouseParser.ShowAccessStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showClusterStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowClusterStmt(ClickHouseParser.ShowClusterStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showClustersStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowClustersStmt(ClickHouseParser.ShowClustersStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSettingsStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSettingsStmt(ClickHouseParser.ShowSettingsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSettingStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSettingStmt(ClickHouseParser.ShowSettingStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showFilesystemCaches}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFilesystemCaches(ClickHouseParser.ShowFilesystemCachesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEnginesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEnginesStmt(ClickHouseParser.ShowEnginesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showFunctionsStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctionsStmt(ClickHouseParser.ShowFunctionsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showMergesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowMergesStmt(ClickHouseParser.ShowMergesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showPrivilegesStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPrivilegesStmt(ClickHouseParser.ShowPrivilegesStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateTableStmt}
	 * labeled alternative in {@link ClickHouseParser#showStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTableStmt(ClickHouseParser.ShowCreateTableStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#systemStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemStmt(ClickHouseParser.SystemStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#truncateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncateStmt(ClickHouseParser.TruncateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#useStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseStmt(ClickHouseParser.UseStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#watchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWatchStmt(ClickHouseParser.WatchStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnTypeExprSimple}
	 * labeled alternative in {@link ClickHouseParser#columnTypeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnTypeExprSimple(ClickHouseParser.ColumnTypeExprSimpleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnTypeExprNested}
	 * labeled alternative in {@link ClickHouseParser#columnTypeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnTypeExprNested(ClickHouseParser.ColumnTypeExprNestedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnTypeExprEnum}
	 * labeled alternative in {@link ClickHouseParser#columnTypeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnTypeExprEnum(ClickHouseParser.ColumnTypeExprEnumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnTypeExprComplex}
	 * labeled alternative in {@link ClickHouseParser#columnTypeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnTypeExprComplex(ClickHouseParser.ColumnTypeExprComplexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnTypeExprParam}
	 * labeled alternative in {@link ClickHouseParser#columnTypeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnTypeExprParam(ClickHouseParser.ColumnTypeExprParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprList(ClickHouseParser.ColumnExprListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnsExprAsterisk}
	 * labeled alternative in {@link ClickHouseParser#columnsExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnsExprAsterisk(ClickHouseParser.ColumnsExprAsteriskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnsExprSubquery}
	 * labeled alternative in {@link ClickHouseParser#columnsExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnsExprSubquery(ClickHouseParser.ColumnsExprSubqueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnsExprColumn}
	 * labeled alternative in {@link ClickHouseParser#columnsExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnsExprColumn(ClickHouseParser.ColumnsExprColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprTernaryOp}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprTernaryOp(ClickHouseParser.ColumnExprTernaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprAlias}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprAlias(ClickHouseParser.ColumnExprAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprExtract}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprExtract(ClickHouseParser.ColumnExprExtractContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprNegate}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprNegate(ClickHouseParser.ColumnExprNegateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprSubquery}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprSubquery(ClickHouseParser.ColumnExprSubqueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprLiteral}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprLiteral(ClickHouseParser.ColumnExprLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprArray}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprArray(ClickHouseParser.ColumnExprArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprSubstring}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprSubstring(ClickHouseParser.ColumnExprSubstringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprCast}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprCast(ClickHouseParser.ColumnExprCastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprOr}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprOr(ClickHouseParser.ColumnExprOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprCastSymbol}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprCastSymbol(ClickHouseParser.ColumnExprCastSymbolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprPrecedence1}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprPrecedence1(ClickHouseParser.ColumnExprPrecedence1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprPrecedence2}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprPrecedence2(ClickHouseParser.ColumnExprPrecedence2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprPrecedence3}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprPrecedence3(ClickHouseParser.ColumnExprPrecedence3Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprInterval}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprInterval(ClickHouseParser.ColumnExprIntervalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprIsNull}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprIsNull(ClickHouseParser.ColumnExprIsNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprWinFunctionTarget}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprWinFunctionTarget(ClickHouseParser.ColumnExprWinFunctionTargetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprTrim}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprTrim(ClickHouseParser.ColumnExprTrimContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprTuple}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprTuple(ClickHouseParser.ColumnExprTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprArrayAccess}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprArrayAccess(ClickHouseParser.ColumnExprArrayAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprBetween}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprBetween(ClickHouseParser.ColumnExprBetweenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprParens}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprParens(ClickHouseParser.ColumnExprParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprTimestamp}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprTimestamp(ClickHouseParser.ColumnExprTimestampContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprAnd}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprAnd(ClickHouseParser.ColumnExprAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprTupleAccess}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprTupleAccess(ClickHouseParser.ColumnExprTupleAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprCase}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprCase(ClickHouseParser.ColumnExprCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprDate}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprDate(ClickHouseParser.ColumnExprDateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprNot}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprNot(ClickHouseParser.ColumnExprNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprWinFunction}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprWinFunction(ClickHouseParser.ColumnExprWinFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprIdentifier}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprIdentifier(ClickHouseParser.ColumnExprIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprFunction}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprFunction(ClickHouseParser.ColumnExprFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnExprAsterisk}
	 * labeled alternative in {@link ClickHouseParser#columnExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExprAsterisk(ClickHouseParser.ColumnExprAsteriskContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnArgList(ClickHouseParser.ColumnArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnArgExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnArgExpr(ClickHouseParser.ColumnArgExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnLambdaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnLambdaExpr(ClickHouseParser.ColumnLambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#columnIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnIdentifier(ClickHouseParser.ColumnIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#nestedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedIdentifier(ClickHouseParser.NestedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code columnExceptExprRegexp}
	 * labeled alternative in {@link ClickHouseParser#columnExceptExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExceptExprRegexp(ClickHouseParser.ColumnExceptExprRegexpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code columnExceptExprIdentifiers}
	 * labeled alternative in {@link ClickHouseParser#columnExceptExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnExceptExprIdentifiers(ClickHouseParser.ColumnExceptExprIdentifiersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableExprIdentifier}
	 * labeled alternative in {@link ClickHouseParser#tableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableExprIdentifier(ClickHouseParser.TableExprIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableExprSubquery}
	 * labeled alternative in {@link ClickHouseParser#tableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableExprSubquery(ClickHouseParser.TableExprSubqueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableExprAlias}
	 * labeled alternative in {@link ClickHouseParser#tableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableExprAlias(ClickHouseParser.TableExprAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TableExprFunction}
	 * labeled alternative in {@link ClickHouseParser#tableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableExprFunction(ClickHouseParser.TableExprFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableFunctionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableFunctionExpr(ClickHouseParser.TableFunctionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIdentifier(ClickHouseParser.TableIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableArgList(ClickHouseParser.TableArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#tableArgExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableArgExpr(ClickHouseParser.TableArgExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#databaseIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabaseIdentifier(ClickHouseParser.DatabaseIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#dictionaryIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryIdentifier(ClickHouseParser.DictionaryIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#floatingLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatingLiteral(ClickHouseParser.FloatingLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#numberLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiteral(ClickHouseParser.NumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(ClickHouseParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ClickHouseParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval(ClickHouseParser.IntervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(ClickHouseParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#keywordForAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordForAlias(ClickHouseParser.KeywordForAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(ClickHouseParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(ClickHouseParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#identifierOrNull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierOrNull(ClickHouseParser.IdentifierOrNullContext ctx);
	/**
	 * Visit a parse tree produced by {@link ClickHouseParser#enumValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValue(ClickHouseParser.EnumValueContext ctx);
}