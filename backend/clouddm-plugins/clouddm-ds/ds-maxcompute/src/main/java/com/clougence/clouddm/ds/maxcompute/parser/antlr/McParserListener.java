// Generated from McParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.maxcompute.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link McParserParser}.
 */
public interface McParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link McParserParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(McParserParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(McParserParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(McParserParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(McParserParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#begin_end_block}.
	 * @param ctx the parse tree
	 */
	void enterBegin_end_block(McParserParser.Begin_end_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#begin_end_block}.
	 * @param ctx the parse tree
	 */
	void exitBegin_end_block(McParserParser.Begin_end_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#single_block_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSingle_block_stmt(McParserParser.Single_block_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#single_block_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSingle_block_stmt(McParserParser.Single_block_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#block_end}.
	 * @param ctx the parse tree
	 */
	void enterBlock_end(McParserParser.Block_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#block_end}.
	 * @param ctx the parse tree
	 */
	void exitBlock_end(McParserParser.Block_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#proc_block}.
	 * @param ctx the parse tree
	 */
	void enterProc_block(McParserParser.Proc_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#proc_block}.
	 * @param ctx the parse tree
	 */
	void exitProc_block(McParserParser.Proc_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(McParserParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(McParserParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view_stmt(McParserParser.Create_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(McParserParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(McParserParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(McParserParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(McParserParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTableColumnStatics}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTableColumnStatics}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTablePartitions}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTablePartitions}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showHistoryTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showHistoryTables}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showHistoryTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowHistoryTable(McParserParser.ShowHistoryTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showHistoryTable}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowRoles(McParserParser.ShowRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowRoles(McParserParser.ShowRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowUsers(McParserParser.ShowUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowUsers(McParserParser.ShowUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTrustProjects}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTrustProjects}
	 * labeled alternative in {@link McParserParser#show_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_materialized_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_materialized_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#alter_materialized_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#alter_materialized_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#alter_materialized_view_item}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view_item(McParserParser.Alter_materialized_view_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#alter_materialized_view_item}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view_item(McParserParser.Alter_materialized_view_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#tbprops}.
	 * @param ctx the parse tree
	 */
	void enterTbprops(McParserParser.TbpropsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#tbprops}.
	 * @param ctx the parse tree
	 */
	void exitTbprops(McParserParser.TbpropsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#prop}.
	 * @param ctx the parse tree
	 */
	void enterProp(McParserParser.PropContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#prop}.
	 * @param ctx the parse tree
	 */
	void exitProp(McParserParser.PropContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#semicolon_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSemicolon_stmt(McParserParser.Semicolon_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#semicolon_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSemicolon_stmt(McParserParser.Semicolon_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#exception_block}.
	 * @param ctx the parse tree
	 */
	void enterException_block(McParserParser.Exception_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#exception_block}.
	 * @param ctx the parse tree
	 */
	void exitException_block(McParserParser.Exception_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#exception_block_item}.
	 * @param ctx the parse tree
	 */
	void enterException_block_item(McParserParser.Exception_block_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#exception_block_item}.
	 * @param ctx the parse tree
	 */
	void exitException_block_item(McParserParser.Exception_block_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(McParserParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(McParserParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt(McParserParser.Assignment_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt(McParserParser.Assignment_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt_item(McParserParser.Assignment_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt_item(McParserParser.Assignment_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt_single_item}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt_single_item(McParserParser.Assignment_stmt_single_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt_single_item}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt_single_item(McParserParser.Assignment_stmt_single_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt_collection_item}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt_collection_item(McParserParser.Assignment_stmt_collection_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt_collection_item}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt_collection_item(McParserParser.Assignment_stmt_collection_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt_multiple_item}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt_multiple_item(McParserParser.Assignment_stmt_multiple_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt_multiple_item}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt_multiple_item(McParserParser.Assignment_stmt_multiple_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#assignment_stmt_select_item}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt_select_item(McParserParser.Assignment_stmt_select_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#assignment_stmt_select_item}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt_select_item(McParserParser.Assignment_stmt_select_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#allocate_cursor_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAllocate_cursor_stmt(McParserParser.Allocate_cursor_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#allocate_cursor_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAllocate_cursor_stmt(McParserParser.Allocate_cursor_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#associate_locator_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssociate_locator_stmt(McParserParser.Associate_locator_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#associate_locator_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssociate_locator_stmt(McParserParser.Associate_locator_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#begin_transaction_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBegin_transaction_stmt(McParserParser.Begin_transaction_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#begin_transaction_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBegin_transaction_stmt(McParserParser.Begin_transaction_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(McParserParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(McParserParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#call_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCall_stmt(McParserParser.Call_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#call_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCall_stmt(McParserParser.Call_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_stmt(McParserParser.Declare_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_stmt(McParserParser.Declare_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_block}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_block(McParserParser.Declare_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_block}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_block(McParserParser.Declare_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_block_inplace}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_block_inplace(McParserParser.Declare_block_inplaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_block_inplace}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_block_inplace(McParserParser.Declare_block_inplaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_stmt_item(McParserParser.Declare_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_stmt_item(McParserParser.Declare_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_var_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_var_item(McParserParser.Declare_var_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_var_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_var_item(McParserParser.Declare_var_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_condition_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_condition_item(McParserParser.Declare_condition_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_condition_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_condition_item(McParserParser.Declare_condition_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_cursor_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_cursor_item(McParserParser.Declare_cursor_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_cursor_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_cursor_item(McParserParser.Declare_cursor_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cursor_with_return}.
	 * @param ctx the parse tree
	 */
	void enterCursor_with_return(McParserParser.Cursor_with_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cursor_with_return}.
	 * @param ctx the parse tree
	 */
	void exitCursor_with_return(McParserParser.Cursor_with_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cursor_without_return}.
	 * @param ctx the parse tree
	 */
	void enterCursor_without_return(McParserParser.Cursor_without_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cursor_without_return}.
	 * @param ctx the parse tree
	 */
	void exitCursor_without_return(McParserParser.Cursor_without_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_handler_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_handler_item(McParserParser.Declare_handler_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_handler_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_handler_item(McParserParser.Declare_handler_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#declare_temporary_table_item}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_temporary_table_item(McParserParser.Declare_temporary_table_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#declare_temporary_table_item}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_temporary_table_item(McParserParser.Declare_temporary_table_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_stmt(McParserParser.Create_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#store_as}.
	 * @param ctx the parse tree
	 */
	void enterStore_as(McParserParser.Store_asContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#store_as}.
	 * @param ctx the parse tree
	 */
	void exitStore_as(McParserParser.Store_asContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#analyze_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#analyze_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_partitions}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_partitions(McParserParser.Create_table_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_partitions}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_partitions(McParserParser.Create_table_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_partition_column}.
	 * @param ctx the parse tree
	 */
	void enterCreate_partition_column(McParserParser.Create_partition_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_partition_column}.
	 * @param ctx the parse tree
	 */
	void exitCreate_partition_column(McParserParser.Create_partition_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_clustere}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_clustere(McParserParser.Create_table_clustereContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_clustere}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_clustere(McParserParser.Create_table_clustereContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_local_temp_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_local_temp_table_stmt(McParserParser.Create_local_temp_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_local_temp_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_local_temp_table_stmt(McParserParser.Create_local_temp_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableSelect}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableSelect(McParserParser.CreateTableSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableSelect}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableSelect(McParserParser.CreateTableSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableColumn}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableColumn(McParserParser.CreateTableColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableColumn}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableColumn(McParserParser.CreateTableColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableLike(McParserParser.CreateTableLikeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link McParserParser#create_table_definition}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableLike(McParserParser.CreateTableLikeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#comment_clause}.
	 * @param ctx the parse tree
	 */
	void enterComment_clause(McParserParser.Comment_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#comment_clause}.
	 * @param ctx the parse tree
	 */
	void exitComment_clause(McParserParser.Comment_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_columns}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_columns(McParserParser.Create_table_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_columns}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_columns(McParserParser.Create_table_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_columns_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_columns_item(McParserParser.Create_table_columns_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_columns_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_columns_item(McParserParser.Create_table_columns_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_primary}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_primary(McParserParser.Create_table_primaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_primary}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_primary(McParserParser.Create_table_primaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#default_value}.
	 * @param ctx the parse tree
	 */
	void enterDefault_value(McParserParser.Default_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#default_value}.
	 * @param ctx the parse tree
	 */
	void exitDefault_value(McParserParser.Default_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#sql_type}.
	 * @param ctx the parse tree
	 */
	void enterSql_type(McParserParser.Sql_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#sql_type}.
	 * @param ctx the parse tree
	 */
	void exitSql_type(McParserParser.Sql_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(McParserParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(McParserParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_column_inline_cons}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_column_inline_cons(McParserParser.Create_table_column_inline_consContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_column_inline_cons}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_column_inline_cons(McParserParser.Create_table_column_inline_consContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_fk_action}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_fk_action(McParserParser.Create_table_fk_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_fk_action}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_fk_action(McParserParser.Create_table_fk_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_preoptions}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_preoptions(McParserParser.Create_table_preoptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_preoptions}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_preoptions(McParserParser.Create_table_preoptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_preoptions_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_preoptions_item(McParserParser.Create_table_preoptions_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_preoptions_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_preoptions_item(McParserParser.Create_table_preoptions_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_preoptions_td_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_preoptions_td_item(McParserParser.Create_table_preoptions_td_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_preoptions_td_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_preoptions_td_item(McParserParser.Create_table_preoptions_td_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_options_ora_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_options_ora_item(McParserParser.Create_table_options_ora_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_options_ora_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_options_ora_item(McParserParser.Create_table_options_ora_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_options_db2_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_options_db2_item(McParserParser.Create_table_options_db2_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_options_db2_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_options_db2_item(McParserParser.Create_table_options_db2_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_options_td_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_options_td_item(McParserParser.Create_table_options_td_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_options_td_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_options_td_item(McParserParser.Create_table_options_td_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_options_hive_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_options_hive_item(McParserParser.Create_table_options_hive_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_options_hive_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_options_hive_item(McParserParser.Create_table_options_hive_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_hive_row_format}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_hive_row_format(McParserParser.Create_table_hive_row_formatContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_hive_row_format}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_hive_row_format(McParserParser.Create_table_hive_row_formatContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_hive_row_format_fields}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_hive_row_format_fields(McParserParser.Create_table_hive_row_format_fieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_hive_row_format_fields}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_hive_row_format_fields(McParserParser.Create_table_hive_row_format_fieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_table_options_mssql_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_options_mssql_item(McParserParser.Create_table_options_mssql_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_table_options_mssql_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_options_mssql_item(McParserParser.Create_table_options_mssql_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableComment(McParserParser.AlterTableCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableComment(McParserParser.AlterTableCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableLifeCycle(McParserParser.AlterTableLifeCycleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableLifeCycle(McParserParser.AlterTableLifeCycleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableEnableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableEnableLifeCycle(McParserParser.AlterTableEnableLifeCycleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableEnableLifeCycle}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableEnableLifeCycle(McParserParser.AlterTableEnableLifeCycleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableTouch}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableTouch(McParserParser.AlterTableTouchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableTouch}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableTouch(McParserParser.AlterTableTouchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableClustered}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableClustered(McParserParser.AlterTableClusteredContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableClustered}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableClustered(McParserParser.AlterTableClusteredContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableReanme}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableReanme(McParserParser.AlterTableReanmeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableReanme}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableReanme(McParserParser.AlterTableReanmeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTablePartition}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablePartition(McParserParser.AlterTablePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTablePartition}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablePartition(McParserParser.AlterTablePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableProp}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableProp(McParserParser.AlterTablePropContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableProp}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableProp(McParserParser.AlterTablePropContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableAddColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableAddColumns(McParserParser.AlterTableAddColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableAddColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableAddColumns(McParserParser.AlterTableAddColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableDropColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableDropColumns(McParserParser.AlterTableDropColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableDropColumns}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableDropColumns(McParserParser.AlterTableDropColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableChangeColumn}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableChangeColumn(McParserParser.AlterTableChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableChangeColumn}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableChangeColumn(McParserParser.AlterTableChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableChangeColumnName}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableChangeColumnName(McParserParser.AlterTableChangeColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableChangeColumnName}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableChangeColumnName(McParserParser.AlterTableChangeColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableChangeColumnComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableChangeColumnComment(McParserParser.AlterTableChangeColumnCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableChangeColumnComment}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableChangeColumnComment(McParserParser.AlterTableChangeColumnCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableChangeColumnNull}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableChangeColumnNull(McParserParser.AlterTableChangeColumnNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableChangeColumnNull}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableChangeColumnNull(McParserParser.AlterTableChangeColumnNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableCompctMajor}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableCompctMajor(McParserParser.AlterTableCompctMajorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableCompctMajor}
	 * labeled alternative in {@link McParserParser#alter_table_item}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableCompctMajor(McParserParser.AlterTableCompctMajorContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#partition_spec}.
	 * @param ctx the parse tree
	 */
	void enterPartition_spec(McParserParser.Partition_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#partition_spec}.
	 * @param ctx the parse tree
	 */
	void exitPartition_spec(McParserParser.Partition_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#partition_spec_filter}.
	 * @param ctx the parse tree
	 */
	void enterPartition_spec_filter(McParserParser.Partition_spec_filterContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#partition_spec_filter}.
	 * @param ctx the parse tree
	 */
	void exitPartition_spec_filter(McParserParser.Partition_spec_filterContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#column_type}.
	 * @param ctx the parse tree
	 */
	void enterColumn_type(McParserParser.Column_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#column_type}.
	 * @param ctx the parse tree
	 */
	void exitColumn_type(McParserParser.Column_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#dtype}.
	 * @param ctx the parse tree
	 */
	void enterDtype(McParserParser.DtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#dtype}.
	 * @param ctx the parse tree
	 */
	void exitDtype(McParserParser.DtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#dtype_len}.
	 * @param ctx the parse tree
	 */
	void enterDtype_len(McParserParser.Dtype_lenContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#dtype_len}.
	 * @param ctx the parse tree
	 */
	void exitDtype_len(McParserParser.Dtype_lenContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#dtype_attr}.
	 * @param ctx the parse tree
	 */
	void enterDtype_attr(McParserParser.Dtype_attrContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#dtype_attr}.
	 * @param ctx the parse tree
	 */
	void exitDtype_attr(McParserParser.Dtype_attrContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#dtype_default}.
	 * @param ctx the parse tree
	 */
	void enterDtype_default(McParserParser.Dtype_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#dtype_default}.
	 * @param ctx the parse tree
	 */
	void exitDtype_default(McParserParser.Dtype_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_database_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database_stmt(McParserParser.Create_database_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_database_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_database_option}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database_option(McParserParser.Create_database_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_database_option}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database_option(McParserParser.Create_database_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_function_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_function_stmt(McParserParser.Create_function_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_function_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_function_stmt(McParserParser.Create_function_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_function_return}.
	 * @param ctx the parse tree
	 */
	void enterCreate_function_return(McParserParser.Create_function_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_function_return}.
	 * @param ctx the parse tree
	 */
	void exitCreate_function_return(McParserParser.Create_function_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_package_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package_stmt(McParserParser.Create_package_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_package_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package_stmt(McParserParser.Create_package_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#package_spec}.
	 * @param ctx the parse tree
	 */
	void enterPackage_spec(McParserParser.Package_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#package_spec}.
	 * @param ctx the parse tree
	 */
	void exitPackage_spec(McParserParser.Package_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#package_spec_item}.
	 * @param ctx the parse tree
	 */
	void enterPackage_spec_item(McParserParser.Package_spec_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#package_spec_item}.
	 * @param ctx the parse tree
	 */
	void exitPackage_spec_item(McParserParser.Package_spec_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_package_body_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package_body_stmt(McParserParser.Create_package_body_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_package_body_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package_body_stmt(McParserParser.Create_package_body_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#package_body}.
	 * @param ctx the parse tree
	 */
	void enterPackage_body(McParserParser.Package_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#package_body}.
	 * @param ctx the parse tree
	 */
	void exitPackage_body(McParserParser.Package_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#package_body_item}.
	 * @param ctx the parse tree
	 */
	void enterPackage_body_item(McParserParser.Package_body_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#package_body_item}.
	 * @param ctx the parse tree
	 */
	void exitPackage_body_item(McParserParser.Package_body_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_procedure_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_procedure_stmt(McParserParser.Create_procedure_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_procedure_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_procedure_stmt(McParserParser.Create_procedure_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_routine_params}.
	 * @param ctx the parse tree
	 */
	void enterCreate_routine_params(McParserParser.Create_routine_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_routine_params}.
	 * @param ctx the parse tree
	 */
	void exitCreate_routine_params(McParserParser.Create_routine_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_routine_param_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_routine_param_item(McParserParser.Create_routine_param_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_routine_param_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_routine_param_item(McParserParser.Create_routine_param_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_routine_options}.
	 * @param ctx the parse tree
	 */
	void enterCreate_routine_options(McParserParser.Create_routine_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_routine_options}.
	 * @param ctx the parse tree
	 */
	void exitCreate_routine_options(McParserParser.Create_routine_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_routine_option}.
	 * @param ctx the parse tree
	 */
	void enterCreate_routine_option(McParserParser.Create_routine_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_routine_option}.
	 * @param ctx the parse tree
	 */
	void exitCreate_routine_option(McParserParser.Create_routine_optionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(McParserParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(McParserParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropView(McParserParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropView(McParserParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropMView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropMView(McParserParser.DropMViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropMView}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropMView(McParserParser.DropMViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropPackage}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropPackage(McParserParser.DropPackageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropPackage}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropPackage(McParserParser.DropPackageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropFunc}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropFunc(McParserParser.DropFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropFunc}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropFunc(McParserParser.DropFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDropSchema(McParserParser.DropSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link McParserParser#drop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDropSchema(McParserParser.DropSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#end_transaction_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEnd_transaction_stmt(McParserParser.End_transaction_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#end_transaction_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEnd_transaction_stmt(McParserParser.End_transaction_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#exec_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExec_stmt(McParserParser.Exec_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#exec_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExec_stmt(McParserParser.Exec_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(McParserParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(McParserParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#if_mc_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_mc_stmt(McParserParser.If_mc_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#if_mc_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_mc_stmt(McParserParser.If_mc_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#if_plsql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_plsql_stmt(McParserParser.If_plsql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#if_plsql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_plsql_stmt(McParserParser.If_plsql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#if_tsql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_tsql_stmt(McParserParser.If_tsql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#if_tsql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_tsql_stmt(McParserParser.If_tsql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#if_bteq_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_bteq_stmt(McParserParser.If_bteq_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#if_bteq_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_bteq_stmt(McParserParser.If_bteq_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#elseif_block}.
	 * @param ctx the parse tree
	 */
	void enterElseif_block(McParserParser.Elseif_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#elseif_block}.
	 * @param ctx the parse tree
	 */
	void exitElseif_block(McParserParser.Elseif_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#else_block}.
	 * @param ctx the parse tree
	 */
	void enterElse_block(McParserParser.Else_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#else_block}.
	 * @param ctx the parse tree
	 */
	void exitElse_block(McParserParser.Else_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInclude_stmt(McParserParser.Include_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInclude_stmt(McParserParser.Include_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt(McParserParser.Insert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt(McParserParser.Insert_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#pt_spec}.
	 * @param ctx the parse tree
	 */
	void enterPt_spec(McParserParser.Pt_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#pt_spec}.
	 * @param ctx the parse tree
	 */
	void exitPt_spec(McParserParser.Pt_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#zorder_stmt}.
	 * @param ctx the parse tree
	 */
	void enterZorder_stmt(McParserParser.Zorder_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#zorder_stmt}.
	 * @param ctx the parse tree
	 */
	void exitZorder_stmt(McParserParser.Zorder_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#insert_stmt_cols}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt_cols(McParserParser.Insert_stmt_colsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#insert_stmt_cols}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt_cols(McParserParser.Insert_stmt_colsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#insert_stmt_rows}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt_rows(McParserParser.Insert_stmt_rowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#insert_stmt_rows}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt_rows(McParserParser.Insert_stmt_rowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#insert_stmt_row}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt_row(McParserParser.Insert_stmt_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#insert_stmt_row}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt_row(McParserParser.Insert_stmt_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#insert_directory_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_directory_stmt(McParserParser.Insert_directory_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#insert_directory_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_directory_stmt(McParserParser.Insert_directory_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#exit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExit_stmt(McParserParser.Exit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#exit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExit_stmt(McParserParser.Exit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#get_diag_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGet_diag_stmt(McParserParser.Get_diag_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#get_diag_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGet_diag_stmt(McParserParser.Get_diag_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#get_diag_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterGet_diag_stmt_item(McParserParser.Get_diag_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#get_diag_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitGet_diag_stmt_item(McParserParser.Get_diag_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#get_diag_stmt_exception_item}.
	 * @param ctx the parse tree
	 */
	void enterGet_diag_stmt_exception_item(McParserParser.Get_diag_stmt_exception_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#get_diag_stmt_exception_item}.
	 * @param ctx the parse tree
	 */
	void exitGet_diag_stmt_exception_item(McParserParser.Get_diag_stmt_exception_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#get_diag_stmt_rowcount_item}.
	 * @param ctx the parse tree
	 */
	void enterGet_diag_stmt_rowcount_item(McParserParser.Get_diag_stmt_rowcount_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#get_diag_stmt_rowcount_item}.
	 * @param ctx the parse tree
	 */
	void exitGet_diag_stmt_rowcount_item(McParserParser.Get_diag_stmt_rowcount_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#grant_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGrant_stmt(McParserParser.Grant_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#grant_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGrant_stmt(McParserParser.Grant_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#grant_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterGrant_stmt_item(McParserParser.Grant_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#grant_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitGrant_stmt_item(McParserParser.Grant_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#leave_stmt}.
	 * @param ctx the parse tree
	 */
	void enterLeave_stmt(McParserParser.Leave_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#leave_stmt}.
	 * @param ctx the parse tree
	 */
	void exitLeave_stmt(McParserParser.Leave_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#map_object_stmt}.
	 * @param ctx the parse tree
	 */
	void enterMap_object_stmt(McParserParser.Map_object_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#map_object_stmt}.
	 * @param ctx the parse tree
	 */
	void exitMap_object_stmt(McParserParser.Map_object_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#open_stmt}.
	 * @param ctx the parse tree
	 */
	void enterOpen_stmt(McParserParser.Open_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#open_stmt}.
	 * @param ctx the parse tree
	 */
	void exitOpen_stmt(McParserParser.Open_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#fetch_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFetch_stmt(McParserParser.Fetch_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#fetch_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFetch_stmt(McParserParser.Fetch_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#fetch_limit}.
	 * @param ctx the parse tree
	 */
	void enterFetch_limit(McParserParser.Fetch_limitContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#fetch_limit}.
	 * @param ctx the parse tree
	 */
	void exitFetch_limit(McParserParser.Fetch_limitContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#collect_stats_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCollect_stats_stmt(McParserParser.Collect_stats_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#collect_stats_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCollect_stats_stmt(McParserParser.Collect_stats_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#collect_stats_clause}.
	 * @param ctx the parse tree
	 */
	void enterCollect_stats_clause(McParserParser.Collect_stats_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#collect_stats_clause}.
	 * @param ctx the parse tree
	 */
	void exitCollect_stats_clause(McParserParser.Collect_stats_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#close_stmt}.
	 * @param ctx the parse tree
	 */
	void enterClose_stmt(McParserParser.Close_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#close_stmt}.
	 * @param ctx the parse tree
	 */
	void exitClose_stmt(McParserParser.Close_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cmp_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCmp_stmt(McParserParser.Cmp_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cmp_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCmp_stmt(McParserParser.Cmp_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cmp_source}.
	 * @param ctx the parse tree
	 */
	void enterCmp_source(McParserParser.Cmp_sourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cmp_source}.
	 * @param ctx the parse tree
	 */
	void exitCmp_source(McParserParser.Cmp_sourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_from_local_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCopy_from_local_stmt(McParserParser.Copy_from_local_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_from_local_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCopy_from_local_stmt(McParserParser.Copy_from_local_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCopy_stmt(McParserParser.Copy_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCopy_stmt(McParserParser.Copy_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_source}.
	 * @param ctx the parse tree
	 */
	void enterCopy_source(McParserParser.Copy_sourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_source}.
	 * @param ctx the parse tree
	 */
	void exitCopy_source(McParserParser.Copy_sourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_target}.
	 * @param ctx the parse tree
	 */
	void enterCopy_target(McParserParser.Copy_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_target}.
	 * @param ctx the parse tree
	 */
	void exitCopy_target(McParserParser.Copy_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_option}.
	 * @param ctx the parse tree
	 */
	void enterCopy_option(McParserParser.Copy_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_option}.
	 * @param ctx the parse tree
	 */
	void exitCopy_option(McParserParser.Copy_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#copy_file_option}.
	 * @param ctx the parse tree
	 */
	void enterCopy_file_option(McParserParser.Copy_file_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#copy_file_option}.
	 * @param ctx the parse tree
	 */
	void exitCopy_file_option(McParserParser.Copy_file_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCommit_stmt(McParserParser.Commit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCommit_stmt(McParserParser.Commit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#create_index_col}.
	 * @param ctx the parse tree
	 */
	void enterCreate_index_col(McParserParser.Create_index_colContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#create_index_col}.
	 * @param ctx the parse tree
	 */
	void exitCreate_index_col(McParserParser.Create_index_colContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#index_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_storage_clause(McParserParser.Index_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#index_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_storage_clause(McParserParser.Index_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#index_mssql_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_mssql_storage_clause(McParserParser.Index_mssql_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#index_mssql_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_mssql_storage_clause(McParserParser.Index_mssql_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#quit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterQuit_stmt(McParserParser.Quit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#quit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitQuit_stmt(McParserParser.Quit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#raise_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRaise_stmt(McParserParser.Raise_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#raise_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRaise_stmt(McParserParser.Raise_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#resignal_stmt}.
	 * @param ctx the parse tree
	 */
	void enterResignal_stmt(McParserParser.Resignal_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#resignal_stmt}.
	 * @param ctx the parse tree
	 */
	void exitResignal_stmt(McParserParser.Resignal_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(McParserParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(McParserParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRollback_stmt(McParserParser.Rollback_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRollback_stmt(McParserParser.Rollback_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#set_session_option}.
	 * @param ctx the parse tree
	 */
	void enterSet_session_option(McParserParser.Set_session_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#set_session_option}.
	 * @param ctx the parse tree
	 */
	void exitSet_session_option(McParserParser.Set_session_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#set_current_schema_option}.
	 * @param ctx the parse tree
	 */
	void enterSet_current_schema_option(McParserParser.Set_current_schema_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#set_current_schema_option}.
	 * @param ctx the parse tree
	 */
	void exitSet_current_schema_option(McParserParser.Set_current_schema_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#set_mssql_session_option}.
	 * @param ctx the parse tree
	 */
	void enterSet_mssql_session_option(McParserParser.Set_mssql_session_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#set_mssql_session_option}.
	 * @param ctx the parse tree
	 */
	void exitSet_mssql_session_option(McParserParser.Set_mssql_session_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#set_teradata_session_option}.
	 * @param ctx the parse tree
	 */
	void enterSet_teradata_session_option(McParserParser.Set_teradata_session_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#set_teradata_session_option}.
	 * @param ctx the parse tree
	 */
	void exitSet_teradata_session_option(McParserParser.Set_teradata_session_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#signal_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSignal_stmt(McParserParser.Signal_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#signal_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSignal_stmt(McParserParser.Signal_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#summary_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSummary_stmt(McParserParser.Summary_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#summary_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSummary_stmt(McParserParser.Summary_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#truncate_stmt}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_stmt(McParserParser.Truncate_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#truncate_stmt}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_stmt(McParserParser.Truncate_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#use_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUse_stmt(McParserParser.Use_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#use_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUse_stmt(McParserParser.Use_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#values_into_stmt}.
	 * @param ctx the parse tree
	 */
	void enterValues_into_stmt(McParserParser.Values_into_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#values_into_stmt}.
	 * @param ctx the parse tree
	 */
	void exitValues_into_stmt(McParserParser.Values_into_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(McParserParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(McParserParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#unconditional_loop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUnconditional_loop_stmt(McParserParser.Unconditional_loop_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#unconditional_loop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUnconditional_loop_stmt(McParserParser.Unconditional_loop_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#for_cursor_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_cursor_stmt(McParserParser.For_cursor_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#for_cursor_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_cursor_stmt(McParserParser.For_cursor_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#for_range_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_range_stmt(McParserParser.For_range_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#for_range_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_range_stmt(McParserParser.For_range_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(McParserParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(McParserParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_clause(McParserParser.Using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_clause(McParserParser.Using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSelect_stmt(McParserParser.Select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSelect_stmt(McParserParser.Select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cte_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCte_select_stmt(McParserParser.Cte_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cte_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCte_select_stmt(McParserParser.Cte_select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cte_select_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterCte_select_stmt_item(McParserParser.Cte_select_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cte_select_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitCte_select_stmt_item(McParserParser.Cte_select_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#cte_select_cols}.
	 * @param ctx the parse tree
	 */
	void enterCte_select_cols(McParserParser.Cte_select_colsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#cte_select_cols}.
	 * @param ctx the parse tree
	 */
	void exitCte_select_cols(McParserParser.Cte_select_colsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#fullselect_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFullselect_stmt(McParserParser.Fullselect_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#fullselect_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFullselect_stmt(McParserParser.Fullselect_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#fullselect_stmt_item}.
	 * @param ctx the parse tree
	 */
	void enterFullselect_stmt_item(McParserParser.Fullselect_stmt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#fullselect_stmt_item}.
	 * @param ctx the parse tree
	 */
	void exitFullselect_stmt_item(McParserParser.Fullselect_stmt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#fullselect_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterFullselect_set_clause(McParserParser.Fullselect_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#fullselect_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitFullselect_set_clause(McParserParser.Fullselect_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#subselect_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSubselect_stmt(McParserParser.Subselect_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#subselect_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSubselect_stmt(McParserParser.Subselect_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#distribute_clause}.
	 * @param ctx the parse tree
	 */
	void enterDistribute_clause(McParserParser.Distribute_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#distribute_clause}.
	 * @param ctx the parse tree
	 */
	void exitDistribute_clause(McParserParser.Distribute_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void enterSort_clause(McParserParser.Sort_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void exitSort_clause(McParserParser.Sort_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_list}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list(McParserParser.Select_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_list}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list(McParserParser.Select_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_list_set}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_set(McParserParser.Select_list_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_list_set}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_set(McParserParser.Select_list_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_list_item}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_item(McParserParser.Select_list_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_list_item}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_item(McParserParser.Select_list_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_list_alias}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_alias(McParserParser.Select_list_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_list_alias}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_alias(McParserParser.Select_list_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#select_list_asterisk}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_asterisk(McParserParser.Select_list_asteriskContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#select_list_asterisk}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_asterisk(McParserParser.Select_list_asteriskContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#table_row}.
	 * @param ctx the parse tree
	 */
	void enterTable_row(McParserParser.Table_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#table_row}.
	 * @param ctx the parse tree
	 */
	void exitTable_row(McParserParser.Table_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bulk_collect_clause}.
	 * @param ctx the parse tree
	 */
	void enterBulk_collect_clause(McParserParser.Bulk_collect_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bulk_collect_clause}.
	 * @param ctx the parse tree
	 */
	void exitBulk_collect_clause(McParserParser.Bulk_collect_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_clause(McParserParser.From_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_clause(McParserParser.From_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void enterLateralView(McParserParser.LateralViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void exitLateralView(McParserParser.LateralViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_table_clause(McParserParser.From_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_table_clause(McParserParser.From_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_table_name_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_table_name_clause(McParserParser.From_table_name_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_table_name_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_table_name_clause(McParserParser.From_table_name_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_subselect_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_subselect_clause(McParserParser.From_subselect_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_subselect_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_subselect_clause(McParserParser.From_subselect_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#nest_from_clause}.
	 * @param ctx the parse tree
	 */
	void enterNest_from_clause(McParserParser.Nest_from_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#nest_from_clause}.
	 * @param ctx the parse tree
	 */
	void exitNest_from_clause(McParserParser.Nest_from_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_body_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_body_clause(McParserParser.From_body_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_body_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_body_clause(McParserParser.From_body_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_join_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_join_clause(McParserParser.From_join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_join_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_join_clause(McParserParser.From_join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_join_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_join_type_clause(McParserParser.From_join_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_join_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_join_type_clause(McParserParser.From_join_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_table_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_table_values_clause(McParserParser.From_table_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_table_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_table_values_clause(McParserParser.From_table_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_table_values_row}.
	 * @param ctx the parse tree
	 */
	void enterFrom_table_values_row(McParserParser.From_table_values_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_table_values_row}.
	 * @param ctx the parse tree
	 */
	void exitFrom_table_values_row(McParserParser.From_table_values_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#from_alias_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_alias_clause(McParserParser.From_alias_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#from_alias_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_alias_clause(McParserParser.From_alias_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(McParserParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(McParserParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(McParserParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(McParserParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_clause(McParserParser.Group_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_clause(McParserParser.Group_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(McParserParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(McParserParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_clause(McParserParser.Order_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_clause(McParserParser.Order_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt(McParserParser.Update_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt(McParserParser.Update_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#update_assignment}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_assignment(McParserParser.Update_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#update_assignment}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_assignment(McParserParser.Update_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#update_table}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_table(McParserParser.Update_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#update_table}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_table(McParserParser.Update_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#update_upsert}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_upsert(McParserParser.Update_upsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#update_upsert}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_upsert(McParserParser.Update_upsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#merge_stmt}.
	 * @param ctx the parse tree
	 */
	void enterMerge_stmt(McParserParser.Merge_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#merge_stmt}.
	 * @param ctx the parse tree
	 */
	void exitMerge_stmt(McParserParser.Merge_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#merge_table}.
	 * @param ctx the parse tree
	 */
	void enterMerge_table(McParserParser.Merge_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#merge_table}.
	 * @param ctx the parse tree
	 */
	void exitMerge_table(McParserParser.Merge_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#merge_condition}.
	 * @param ctx the parse tree
	 */
	void enterMerge_condition(McParserParser.Merge_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#merge_condition}.
	 * @param ctx the parse tree
	 */
	void exitMerge_condition(McParserParser.Merge_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#merge_action}.
	 * @param ctx the parse tree
	 */
	void enterMerge_action(McParserParser.Merge_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#merge_action}.
	 * @param ctx the parse tree
	 */
	void exitMerge_action(McParserParser.Merge_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt(McParserParser.Delete_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt(McParserParser.Delete_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#delete_alias}.
	 * @param ctx the parse tree
	 */
	void enterDelete_alias(McParserParser.Delete_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#delete_alias}.
	 * @param ctx the parse tree
	 */
	void exitDelete_alias(McParserParser.Delete_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#describe_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDescribe_stmt(McParserParser.Describe_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#describe_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDescribe_stmt(McParserParser.Describe_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr(McParserParser.Bool_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr(McParserParser.Bool_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_atom}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_atom(McParserParser.Bool_expr_atomContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_atom}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_atom(McParserParser.Bool_expr_atomContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_unary}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_unary(McParserParser.Bool_expr_unaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_unary}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_unary(McParserParser.Bool_expr_unaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_single_in}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_single_in(McParserParser.Bool_expr_single_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_single_in}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_single_in(McParserParser.Bool_expr_single_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_multi_in}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_multi_in(McParserParser.Bool_expr_multi_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_multi_in}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_multi_in(McParserParser.Bool_expr_multi_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_binary}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_binary(McParserParser.Bool_expr_binaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_binary}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_binary(McParserParser.Bool_expr_binaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_logical_operator}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_logical_operator(McParserParser.Bool_expr_logical_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_logical_operator}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_logical_operator(McParserParser.Bool_expr_logical_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_expr_binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr_binary_operator(McParserParser.Bool_expr_binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_expr_binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr_binary_operator(McParserParser.Bool_expr_binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(McParserParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(McParserParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_cast}.
	 * @param ctx the parse tree
	 */
	void enterExpr_cast(McParserParser.Expr_castContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_cast}.
	 * @param ctx the parse tree
	 */
	void exitExpr_cast(McParserParser.Expr_castContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void enterExpr_atom(McParserParser.Expr_atomContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void exitExpr_atom(McParserParser.Expr_atomContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_interval}.
	 * @param ctx the parse tree
	 */
	void enterExpr_interval(McParserParser.Expr_intervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_interval}.
	 * @param ctx the parse tree
	 */
	void exitExpr_interval(McParserParser.Expr_intervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#interval_item}.
	 * @param ctx the parse tree
	 */
	void enterInterval_item(McParserParser.Interval_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#interval_item}.
	 * @param ctx the parse tree
	 */
	void exitInterval_item(McParserParser.Interval_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_concat}.
	 * @param ctx the parse tree
	 */
	void enterExpr_concat(McParserParser.Expr_concatContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_concat}.
	 * @param ctx the parse tree
	 */
	void exitExpr_concat(McParserParser.Expr_concatContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_concat_item}.
	 * @param ctx the parse tree
	 */
	void enterExpr_concat_item(McParserParser.Expr_concat_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_concat_item}.
	 * @param ctx the parse tree
	 */
	void exitExpr_concat_item(McParserParser.Expr_concat_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_case}.
	 * @param ctx the parse tree
	 */
	void enterExpr_case(McParserParser.Expr_caseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_case}.
	 * @param ctx the parse tree
	 */
	void exitExpr_case(McParserParser.Expr_caseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_case_simple}.
	 * @param ctx the parse tree
	 */
	void enterExpr_case_simple(McParserParser.Expr_case_simpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_case_simple}.
	 * @param ctx the parse tree
	 */
	void exitExpr_case_simple(McParserParser.Expr_case_simpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_case_searched}.
	 * @param ctx the parse tree
	 */
	void enterExpr_case_searched(McParserParser.Expr_case_searchedContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_case_searched}.
	 * @param ctx the parse tree
	 */
	void exitExpr_case_searched(McParserParser.Expr_case_searchedContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_cursor_attribute}.
	 * @param ctx the parse tree
	 */
	void enterExpr_cursor_attribute(McParserParser.Expr_cursor_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_cursor_attribute}.
	 * @param ctx the parse tree
	 */
	void exitExpr_cursor_attribute(McParserParser.Expr_cursor_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_agg_window_func}.
	 * @param ctx the parse tree
	 */
	void enterExpr_agg_window_func(McParserParser.Expr_agg_window_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_agg_window_func}.
	 * @param ctx the parse tree
	 */
	void exitExpr_agg_window_func(McParserParser.Expr_agg_window_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_func_all_distinct}.
	 * @param ctx the parse tree
	 */
	void enterExpr_func_all_distinct(McParserParser.Expr_func_all_distinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_func_all_distinct}.
	 * @param ctx the parse tree
	 */
	void exitExpr_func_all_distinct(McParserParser.Expr_func_all_distinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_func_over_clause}.
	 * @param ctx the parse tree
	 */
	void enterExpr_func_over_clause(McParserParser.Expr_func_over_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_func_over_clause}.
	 * @param ctx the parse tree
	 */
	void exitExpr_func_over_clause(McParserParser.Expr_func_over_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause(McParserParser.Frame_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause(McParserParser.Frame_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#frame_exclusion}.
	 * @param ctx the parse tree
	 */
	void enterFrame_exclusion(McParserParser.Frame_exclusionContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#frame_exclusion}.
	 * @param ctx the parse tree
	 */
	void exitFrame_exclusion(McParserParser.Frame_exclusionContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#frame_start}.
	 * @param ctx the parse tree
	 */
	void enterFrame_start(McParserParser.Frame_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#frame_start}.
	 * @param ctx the parse tree
	 */
	void exitFrame_start(McParserParser.Frame_startContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#frame_end}.
	 * @param ctx the parse tree
	 */
	void enterFrame_end(McParserParser.Frame_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#frame_end}.
	 * @param ctx the parse tree
	 */
	void exitFrame_end(McParserParser.Frame_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_func_partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterExpr_func_partition_by_clause(McParserParser.Expr_func_partition_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_func_partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitExpr_func_partition_by_clause(McParserParser.Expr_func_partition_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_spec_func}.
	 * @param ctx the parse tree
	 */
	void enterExpr_spec_func(McParserParser.Expr_spec_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_spec_func}.
	 * @param ctx the parse tree
	 */
	void exitExpr_spec_func(McParserParser.Expr_spec_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_func}.
	 * @param ctx the parse tree
	 */
	void enterExpr_func(McParserParser.Expr_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_func}.
	 * @param ctx the parse tree
	 */
	void exitExpr_func(McParserParser.Expr_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_dot}.
	 * @param ctx the parse tree
	 */
	void enterExpr_dot(McParserParser.Expr_dotContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_dot}.
	 * @param ctx the parse tree
	 */
	void exitExpr_dot(McParserParser.Expr_dotContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_dot_method_call}.
	 * @param ctx the parse tree
	 */
	void enterExpr_dot_method_call(McParserParser.Expr_dot_method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_dot_method_call}.
	 * @param ctx the parse tree
	 */
	void exitExpr_dot_method_call(McParserParser.Expr_dot_method_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_dot_property_access}.
	 * @param ctx the parse tree
	 */
	void enterExpr_dot_property_access(McParserParser.Expr_dot_property_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_dot_property_access}.
	 * @param ctx the parse tree
	 */
	void exitExpr_dot_property_access(McParserParser.Expr_dot_property_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_func_params}.
	 * @param ctx the parse tree
	 */
	void enterExpr_func_params(McParserParser.Expr_func_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_func_params}.
	 * @param ctx the parse tree
	 */
	void exitExpr_func_params(McParserParser.Expr_func_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#func_param}.
	 * @param ctx the parse tree
	 */
	void enterFunc_param(McParserParser.Func_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#func_param}.
	 * @param ctx the parse tree
	 */
	void exitFunc_param(McParserParser.Func_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void enterExpr_select(McParserParser.Expr_selectContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void exitExpr_select(McParserParser.Expr_selectContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#expr_file}.
	 * @param ctx the parse tree
	 */
	void enterExpr_file(McParserParser.Expr_fileContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#expr_file}.
	 * @param ctx the parse tree
	 */
	void exitExpr_file(McParserParser.Expr_fileContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#file_name}.
	 * @param ctx the parse tree
	 */
	void enterFile_name(McParserParser.File_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#file_name}.
	 * @param ctx the parse tree
	 */
	void exitFile_name(McParserParser.File_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#date_literal}.
	 * @param ctx the parse tree
	 */
	void enterDate_literal(McParserParser.Date_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#date_literal}.
	 * @param ctx the parse tree
	 */
	void exitDate_literal(McParserParser.Date_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#timestamp_literal}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp_literal(McParserParser.Timestamp_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#timestamp_literal}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp_literal(McParserParser.Timestamp_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(McParserParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(McParserParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#qident}.
	 * @param ctx the parse tree
	 */
	void enterQident(McParserParser.QidentContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#qident}.
	 * @param ctx the parse tree
	 */
	void exitQident(McParserParser.QidentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code single_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 */
	void enterSingle_quotedString(McParserParser.Single_quotedStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code single_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 */
	void exitSingle_quotedString(McParserParser.Single_quotedStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code double_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 */
	void enterDouble_quotedString(McParserParser.Double_quotedStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code double_quotedString}
	 * labeled alternative in {@link McParserParser#string}.
	 * @param ctx the parse tree
	 */
	void exitDouble_quotedString(McParserParser.Double_quotedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#int_number}.
	 * @param ctx the parse tree
	 */
	void enterInt_number(McParserParser.Int_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#int_number}.
	 * @param ctx the parse tree
	 */
	void exitInt_number(McParserParser.Int_numberContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#dec_number}.
	 * @param ctx the parse tree
	 */
	void enterDec_number(McParserParser.Dec_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#dec_number}.
	 * @param ctx the parse tree
	 */
	void exitDec_number(McParserParser.Dec_numberContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#bool_literal}.
	 * @param ctx the parse tree
	 */
	void enterBool_literal(McParserParser.Bool_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#bool_literal}.
	 * @param ctx the parse tree
	 */
	void exitBool_literal(McParserParser.Bool_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#null_const}.
	 * @param ctx the parse tree
	 */
	void enterNull_const(McParserParser.Null_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#null_const}.
	 * @param ctx the parse tree
	 */
	void exitNull_const(McParserParser.Null_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link McParserParser#non_reserved_words}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_words(McParserParser.Non_reserved_wordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link McParserParser#non_reserved_words}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_words(McParserParser.Non_reserved_wordsContext ctx);
}