// Generated from McParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.maxcompute.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link McParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface McParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link McParserParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(McParserParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(McParserParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#begin_end_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_end_block(McParserParser.Begin_end_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#single_block_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_block_stmt(McParserParser.Single_block_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#block_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_end(McParserParser.Block_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#proc_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProc_block(McParserParser.Proc_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(McParserParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(McParserParser.ShowTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTable(McParserParser.ShowCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTableColumnStatics}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTablePartitions}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showHistoryTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showHistoryTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRoles(McParserParser.ShowRolesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowUsers(McParserParser.ShowUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTrustProjects}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_materialized_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#alter_materialized_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#alter_materialized_view_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view_item(McParserParser.Alter_materialized_view_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#tbprops}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTbprops(McParserParser.TbpropsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#prop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProp(McParserParser.PropContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#semicolon_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemicolon_stmt(McParserParser.Semicolon_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#exception_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_block(McParserParser.Exception_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#exception_block_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_block_item(McParserParser.Exception_block_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(McParserParser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt(McParserParser.Assignment_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt_item(McParserParser.Assignment_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt_single_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt_single_item(McParserParser.Assignment_stmt_single_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt_collection_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt_collection_item(McParserParser.Assignment_stmt_collection_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt_multiple_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt_multiple_item(McParserParser.Assignment_stmt_multiple_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#assignment_stmt_select_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_stmt_select_item(McParserParser.Assignment_stmt_select_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#allocate_cursor_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocate_cursor_stmt(McParserParser.Allocate_cursor_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#associate_locator_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociate_locator_stmt(McParserParser.Associate_locator_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#begin_transaction_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_transaction_stmt(McParserParser.Begin_transaction_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#break_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(McParserParser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#call_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_stmt(McParserParser.Call_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_stmt(McParserParser.Declare_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_block(McParserParser.Declare_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_block_inplace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_block_inplace(McParserParser.Declare_block_inplaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_stmt_item(McParserParser.Declare_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_var_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_var_item(McParserParser.Declare_var_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_condition_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_condition_item(McParserParser.Declare_condition_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_cursor_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_cursor_item(McParserParser.Declare_cursor_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cursor_with_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_with_return(McParserParser.Cursor_with_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cursor_without_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_without_return(McParserParser.Cursor_without_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_handler_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_handler_item(McParserParser.Declare_handler_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#declare_temporary_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_temporary_table_item(McParserParser.Declare_temporary_table_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#store_as}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStore_as(McParserParser.Store_asContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#analyze_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_partitions(McParserParser.Create_table_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_partition_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_partition_column(McParserParser.Create_partition_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_clustere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_clustere(McParserParser.Create_table_clustereContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_local_temp_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_local_temp_table_stmt(McParserParser.Create_local_temp_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTableSelect}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableSelect(McParserParser.CreateTableSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTableColumn}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableColumn(McParserParser.CreateTableColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableLike(McParserParser.CreateTableLikeContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#comment_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_clause(McParserParser.Comment_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_columns(McParserParser.Create_table_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_columns_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_columns_item(McParserParser.Create_table_columns_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_primary(McParserParser.Create_table_primaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#default_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_value(McParserParser.Default_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#sql_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_type(McParserParser.Sql_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(McParserParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_column_inline_cons}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_column_inline_cons(McParserParser.Create_table_column_inline_consContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_fk_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_fk_action(McParserParser.Create_table_fk_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_preoptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_preoptions(McParserParser.Create_table_preoptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_preoptions_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_preoptions_item(McParserParser.Create_table_preoptions_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_preoptions_td_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_preoptions_td_item(McParserParser.Create_table_preoptions_td_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_options_ora_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_options_ora_item(McParserParser.Create_table_options_ora_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_options_db2_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_options_db2_item(McParserParser.Create_table_options_db2_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_options_td_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_options_td_item(McParserParser.Create_table_options_td_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_options_hive_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_options_hive_item(McParserParser.Create_table_options_hive_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_hive_row_format}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_hive_row_format(McParserParser.Create_table_hive_row_formatContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_hive_row_format_fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_hive_row_format_fields(McParserParser.Create_table_hive_row_format_fieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_table_options_mssql_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_options_mssql_item(McParserParser.Create_table_options_mssql_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableComment(McParserParser.AlterTableCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableLifeCycle(McParserParser.AlterTableLifeCycleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableEnableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableEnableLifeCycle(McParserParser.AlterTableEnableLifeCycleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableTouch}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableTouch(McParserParser.AlterTableTouchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableClustered}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableClustered(McParserParser.AlterTableClusteredContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableReanme}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableReanme(McParserParser.AlterTableReanmeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTablePartition}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablePartition(McParserParser.AlterTablePartitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableProp}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableProp(McParserParser.AlterTablePropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableAddColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableAddColumns(McParserParser.AlterTableAddColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableDropColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableDropColumns(McParserParser.AlterTableDropColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableChangeColumn}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableChangeColumn(McParserParser.AlterTableChangeColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableChangeColumnName}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableChangeColumnName(McParserParser.AlterTableChangeColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableChangeColumnComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableChangeColumnComment(McParserParser.AlterTableChangeColumnCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableChangeColumnNull}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableChangeColumnNull(McParserParser.AlterTableChangeColumnNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableCompctMajor}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableCompctMajor(McParserParser.AlterTableCompctMajorContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#partition_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_spec(McParserParser.Partition_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#partition_spec_filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_spec_filter(McParserParser.Partition_spec_filterContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#column_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_type(McParserParser.Column_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#dtype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtype(McParserParser.DtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#dtype_len}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtype_len(McParserParser.Dtype_lenContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#dtype_attr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtype_attr(McParserParser.Dtype_attrContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#dtype_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtype_default(McParserParser.Dtype_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_database_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_database_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database_option(McParserParser.Create_database_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_function_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_function_stmt(McParserParser.Create_function_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_function_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_function_return(McParserParser.Create_function_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_package_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package_stmt(McParserParser.Create_package_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#package_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_spec(McParserParser.Package_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#package_spec_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_spec_item(McParserParser.Package_spec_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_package_body_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package_body_stmt(McParserParser.Create_package_body_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#package_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_body(McParserParser.Package_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#package_body_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_body_item(McParserParser.Package_body_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_procedure_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_procedure_stmt(McParserParser.Create_procedure_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_routine_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_routine_params(McParserParser.Create_routine_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_routine_param_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_routine_param_item(McParserParser.Create_routine_param_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_routine_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_routine_options(McParserParser.Create_routine_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_routine_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_routine_option(McParserParser.Create_routine_optionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(McParserParser.DropTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(McParserParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropMView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropMView(McParserParser.DropMViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropPackage}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropPackage(McParserParser.DropPackageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropFunc}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunc(McParserParser.DropFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropSchema(McParserParser.DropSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#end_transaction_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd_transaction_stmt(McParserParser.End_transaction_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#exec_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExec_stmt(McParserParser.Exec_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(McParserParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#if_mc_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_mc_stmt(McParserParser.If_mc_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#if_plsql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_plsql_stmt(McParserParser.If_plsql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#if_tsql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_tsql_stmt(McParserParser.If_tsql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#if_bteq_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_bteq_stmt(McParserParser.If_bteq_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#elseif_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_block(McParserParser.Elseif_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#else_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_block(McParserParser.Else_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#include_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_stmt(McParserParser.Include_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#insert_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt(McParserParser.Insert_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#pt_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPt_spec(McParserParser.Pt_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#zorder_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZorder_stmt(McParserParser.Zorder_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#insert_stmt_cols}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt_cols(McParserParser.Insert_stmt_colsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#insert_stmt_rows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt_rows(McParserParser.Insert_stmt_rowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#insert_stmt_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt_row(McParserParser.Insert_stmt_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#insert_directory_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_directory_stmt(McParserParser.Insert_directory_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#exit_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit_stmt(McParserParser.Exit_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#get_diag_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet_diag_stmt(McParserParser.Get_diag_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#get_diag_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet_diag_stmt_item(McParserParser.Get_diag_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#get_diag_stmt_exception_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet_diag_stmt_exception_item(McParserParser.Get_diag_stmt_exception_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#get_diag_stmt_rowcount_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet_diag_stmt_rowcount_item(McParserParser.Get_diag_stmt_rowcount_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#grant_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_stmt(McParserParser.Grant_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#grant_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_stmt_item(McParserParser.Grant_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#leave_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeave_stmt(McParserParser.Leave_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#map_object_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_object_stmt(McParserParser.Map_object_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#open_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_stmt(McParserParser.Open_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#fetch_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_stmt(McParserParser.Fetch_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#fetch_limit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_limit(McParserParser.Fetch_limitContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#collect_stats_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollect_stats_stmt(McParserParser.Collect_stats_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#collect_stats_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollect_stats_clause(McParserParser.Collect_stats_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#close_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_stmt(McParserParser.Close_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cmp_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmp_stmt(McParserParser.Cmp_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cmp_source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmp_source(McParserParser.Cmp_sourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_from_local_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_from_local_stmt(McParserParser.Copy_from_local_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_stmt(McParserParser.Copy_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_source(McParserParser.Copy_sourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_target(McParserParser.Copy_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_option(McParserParser.Copy_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#copy_file_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_file_option(McParserParser.Copy_file_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#commit_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_stmt(McParserParser.Commit_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#create_index_col}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_index_col(McParserParser.Create_index_colContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#index_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_storage_clause(McParserParser.Index_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#index_mssql_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_mssql_storage_clause(McParserParser.Index_mssql_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#quit_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuit_stmt(McParserParser.Quit_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#raise_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaise_stmt(McParserParser.Raise_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#resignal_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResignal_stmt(McParserParser.Resignal_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(McParserParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#rollback_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_stmt(McParserParser.Rollback_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#set_session_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_session_option(McParserParser.Set_session_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#set_current_schema_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_current_schema_option(McParserParser.Set_current_schema_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#set_mssql_session_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_mssql_session_option(McParserParser.Set_mssql_session_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#set_teradata_session_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_teradata_session_option(McParserParser.Set_teradata_session_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#signal_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignal_stmt(McParserParser.Signal_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#summary_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSummary_stmt(McParserParser.Summary_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#truncate_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_stmt(McParserParser.Truncate_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#use_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_stmt(McParserParser.Use_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#values_into_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_into_stmt(McParserParser.Values_into_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#while_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(McParserParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#unconditional_loop_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnconditional_loop_stmt(McParserParser.Unconditional_loop_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#for_cursor_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_cursor_stmt(McParserParser.For_cursor_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#for_range_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_range_stmt(McParserParser.For_range_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(McParserParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_clause(McParserParser.Using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_stmt(McParserParser.Select_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cte_select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_select_stmt(McParserParser.Cte_select_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cte_select_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_select_stmt_item(McParserParser.Cte_select_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#cte_select_cols}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_select_cols(McParserParser.Cte_select_colsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#fullselect_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullselect_stmt(McParserParser.Fullselect_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#fullselect_stmt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullselect_stmt_item(McParserParser.Fullselect_stmt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#fullselect_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullselect_set_clause(McParserParser.Fullselect_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#subselect_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubselect_stmt(McParserParser.Subselect_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#distribute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistribute_clause(McParserParser.Distribute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#sort_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_clause(McParserParser.Sort_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list(McParserParser.Select_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_list_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_set(McParserParser.Select_list_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_list_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_item(McParserParser.Select_list_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_list_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_alias(McParserParser.Select_list_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#select_list_asterisk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_asterisk(McParserParser.Select_list_asteriskContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#table_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_row(McParserParser.Table_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bulk_collect_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBulk_collect_clause(McParserParser.Bulk_collect_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_clause(McParserParser.From_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#lateralView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLateralView(McParserParser.LateralViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_table_clause(McParserParser.From_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_table_name_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_table_name_clause(McParserParser.From_table_name_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_subselect_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_subselect_clause(McParserParser.From_subselect_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#nest_from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNest_from_clause(McParserParser.Nest_from_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_body_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_body_clause(McParserParser.From_body_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_join_clause(McParserParser.From_join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_join_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_join_type_clause(McParserParser.From_join_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_table_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_table_values_clause(McParserParser.From_table_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_table_values_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_table_values_row(McParserParser.From_table_values_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#from_alias_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_alias_clause(McParserParser.From_alias_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(McParserParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(McParserParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#group_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_clause(McParserParser.Group_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(McParserParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_clause(McParserParser.Order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#update_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_stmt(McParserParser.Update_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#update_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_assignment(McParserParser.Update_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#update_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_table(McParserParser.Update_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#update_upsert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_upsert(McParserParser.Update_upsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#merge_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_stmt(McParserParser.Merge_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#merge_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_table(McParserParser.Merge_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#merge_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_condition(McParserParser.Merge_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#merge_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_action(McParserParser.Merge_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#delete_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_stmt(McParserParser.Delete_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#delete_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_alias(McParserParser.Delete_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#describe_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribe_stmt(McParserParser.Describe_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr(McParserParser.Bool_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_atom(McParserParser.Bool_expr_atomContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_unary(McParserParser.Bool_expr_unaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_single_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_single_in(McParserParser.Bool_expr_single_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_multi_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_multi_in(McParserParser.Bool_expr_multi_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_binary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_binary(McParserParser.Bool_expr_binaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_logical_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_logical_operator(McParserParser.Bool_expr_logical_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_expr_binary_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr_binary_operator(McParserParser.Bool_expr_binary_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(McParserParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_cast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_cast(McParserParser.Expr_castContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_atom(McParserParser.Expr_atomContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_interval(McParserParser.Expr_intervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#interval_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_item(McParserParser.Interval_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_concat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_concat(McParserParser.Expr_concatContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_concat_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_concat_item(McParserParser.Expr_concat_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_case}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_case(McParserParser.Expr_caseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_case_simple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_case_simple(McParserParser.Expr_case_simpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_case_searched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_case_searched(McParserParser.Expr_case_searchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_cursor_attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_cursor_attribute(McParserParser.Expr_cursor_attributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_agg_window_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_agg_window_func(McParserParser.Expr_agg_window_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_func_all_distinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_func_all_distinct(McParserParser.Expr_func_all_distinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_func_over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_func_over_clause(McParserParser.Expr_func_over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#frame_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_clause(McParserParser.Frame_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#frame_exclusion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_exclusion(McParserParser.Frame_exclusionContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#frame_start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_start(McParserParser.Frame_startContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#frame_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_end(McParserParser.Frame_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_func_partition_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_func_partition_by_clause(McParserParser.Expr_func_partition_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_spec_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_spec_func(McParserParser.Expr_spec_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_func(McParserParser.Expr_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_dot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_dot(McParserParser.Expr_dotContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_dot_method_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_dot_method_call(McParserParser.Expr_dot_method_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_dot_property_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_dot_property_access(McParserParser.Expr_dot_property_accessContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_func_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_func_params(McParserParser.Expr_func_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#func_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_param(McParserParser.Func_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_select(McParserParser.Expr_selectContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#expr_file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_file(McParserParser.Expr_fileContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#file_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_name(McParserParser.File_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#date_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_literal(McParserParser.Date_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#timestamp_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp_literal(McParserParser.Timestamp_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(McParserParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#qident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQident(McParserParser.QidentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code single_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_quotedString(McParserParser.Single_quotedStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code double_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDouble_quotedString(McParserParser.Double_quotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#int_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_number(McParserParser.Int_numberContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#dec_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDec_number(McParserParser.Dec_numberContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#bool_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_literal(McParserParser.Bool_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#null_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_const(McParserParser.Null_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link McParserParser#non_reserved_words}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_words(McParserParser.Non_reserved_wordsContext ctx);
}