// Generated from PgSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.dsfamily.postgres.parser.antlr;

import com.clougence.clouddm.dsfamily.postgres.parser.base.PgSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PgSqlParser}.
 */
public interface PgSqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(PgSqlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(PgSqlParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 */
	void enterStmtblock(PgSqlParser.StmtblockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 */
	void exitStmtblock(PgSqlParser.StmtblockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 */
	void enterStmtmulti(PgSqlParser.StmtmultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 */
	void exitStmtmulti(PgSqlParser.StmtmultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(PgSqlParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(PgSqlParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void enterCallstmt(PgSqlParser.CallstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void exitCallstmt(PgSqlParser.CallstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreaterolestmt(PgSqlParser.CreaterolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreaterolestmt(PgSqlParser.CreaterolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#with_}.
	 * @param ctx the parse tree
	 */
	void enterWith_(PgSqlParser.With_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#with_}.
	 * @param ctx the parse tree
	 */
	void exitWith_(PgSqlParser.With_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 */
	void enterOptrolelist(PgSqlParser.OptrolelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 */
	void exitOptrolelist(PgSqlParser.OptrolelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 */
	void enterAlteroptrolelist(PgSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 */
	void exitAlteroptrolelist(PgSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 */
	void enterAlteroptroleelem(PgSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 */
	void exitAlteroptroleelem(PgSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 */
	void enterCreateoptroleelem(PgSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 */
	void exitCreateoptroleelem(PgSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateuserstmt(PgSqlParser.CreateuserstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateuserstmt(PgSqlParser.CreateuserstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterrolestmt(PgSqlParser.AlterrolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterrolestmt(PgSqlParser.AlterrolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#in_database_}.
	 * @param ctx the parse tree
	 */
	void enterIn_database_(PgSqlParser.In_database_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#in_database_}.
	 * @param ctx the parse tree
	 */
	void exitIn_database_(PgSqlParser.In_database_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterrolesetstmt(PgSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterrolesetstmt(PgSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroprolestmt(PgSqlParser.DroprolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroprolestmt(PgSqlParser.DroprolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropuserstmt(PgSqlParser.DropuserstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropuserstmt(PgSqlParser.DropuserstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreategroupstmt(PgSqlParser.CreategroupstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreategroupstmt(PgSqlParser.CreategroupstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltergroupstmt(PgSqlParser.AltergroupstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltergroupstmt(PgSqlParser.AltergroupstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#add_drop}.
	 * @param ctx the parse tree
	 */
	void enterAdd_drop(PgSqlParser.Add_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#add_drop}.
	 * @param ctx the parse tree
	 */
	void exitAdd_drop(PgSqlParser.Add_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateschemastmt(PgSqlParser.CreateschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateschemastmt(PgSqlParser.CreateschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 */
	void enterOptschemaname(PgSqlParser.OptschemanameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 */
	void exitOptschemaname(PgSqlParser.OptschemanameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 */
	void enterOptschemaeltlist(PgSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 */
	void exitOptschemaeltlist(PgSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSchema_stmt(PgSqlParser.Schema_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSchema_stmt(PgSqlParser.Schema_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariablesetstmt(PgSqlParser.VariablesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariablesetstmt(PgSqlParser.VariablesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_rest}.
	 * @param ctx the parse tree
	 */
	void enterSet_rest(PgSqlParser.Set_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_rest}.
	 * @param ctx the parse tree
	 */
	void exitSet_rest(PgSqlParser.Set_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_set}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_set(PgSqlParser.Generic_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_set}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_set(PgSqlParser.Generic_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 */
	void enterSet_rest_more(PgSqlParser.Set_rest_moreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 */
	void exitSet_rest_more(PgSqlParser.Set_rest_moreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#var_name}.
	 * @param ctx the parse tree
	 */
	void enterVar_name(PgSqlParser.Var_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#var_name}.
	 * @param ctx the parse tree
	 */
	void exitVar_name(PgSqlParser.Var_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#var_list}.
	 * @param ctx the parse tree
	 */
	void enterVar_list(PgSqlParser.Var_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#var_list}.
	 * @param ctx the parse tree
	 */
	void exitVar_list(PgSqlParser.Var_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#var_value}.
	 * @param ctx the parse tree
	 */
	void enterVar_value(PgSqlParser.Var_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#var_value}.
	 * @param ctx the parse tree
	 */
	void exitVar_value(PgSqlParser.Var_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#iso_level}.
	 * @param ctx the parse tree
	 */
	void enterIso_level(PgSqlParser.Iso_levelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#iso_level}.
	 * @param ctx the parse tree
	 */
	void exitIso_level(PgSqlParser.Iso_levelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_or_string_(PgSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_or_string_(PgSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#zone_value}.
	 * @param ctx the parse tree
	 */
	void enterZone_value(PgSqlParser.Zone_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#zone_value}.
	 * @param ctx the parse tree
	 */
	void exitZone_value(PgSqlParser.Zone_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#encoding_}.
	 * @param ctx the parse tree
	 */
	void enterEncoding_(PgSqlParser.Encoding_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#encoding_}.
	 * @param ctx the parse tree
	 */
	void exitEncoding_(PgSqlParser.Encoding_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 */
	void enterNonreservedword_or_sconst(PgSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 */
	void exitNonreservedword_or_sconst(PgSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariableresetstmt(PgSqlParser.VariableresetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariableresetstmt(PgSqlParser.VariableresetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 */
	void enterReset_rest(PgSqlParser.Reset_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 */
	void exitReset_rest(PgSqlParser.Reset_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_reset(PgSqlParser.Generic_resetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_reset(PgSqlParser.Generic_resetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 */
	void enterSetresetclause(PgSqlParser.SetresetclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 */
	void exitSetresetclause(PgSqlParser.SetresetclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 */
	void enterFunctionsetresetclause(PgSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 */
	void exitFunctionsetresetclause(PgSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariableshowstmt(PgSqlParser.VariableshowstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariableshowstmt(PgSqlParser.VariableshowstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 */
	void enterConstraintssetstmt(PgSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 */
	void exitConstraintssetstmt(PgSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 */
	void enterConstraints_set_list(PgSqlParser.Constraints_set_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 */
	void exitConstraints_set_list(PgSqlParser.Constraints_set_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 */
	void enterConstraints_set_mode(PgSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 */
	void exitConstraints_set_mode(PgSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 */
	void enterCheckpointstmt(PgSqlParser.CheckpointstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 */
	void exitCheckpointstmt(PgSqlParser.CheckpointstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 */
	void enterDiscardstmt(PgSqlParser.DiscardstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 */
	void exitDiscardstmt(PgSqlParser.DiscardstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertablestmt(PgSqlParser.AltertablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertablestmt(PgSqlParser.AltertablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_cmds(PgSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_cmds(PgSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 */
	void enterPartition_cmd(PgSqlParser.Partition_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 */
	void exitPartition_cmd(PgSqlParser.Partition_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partition_cmd(PgSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partition_cmd(PgSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAddColumn(PgSqlParser.AddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAddColumn(PgSqlParser.AddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlterColumn(PgSqlParser.AlterColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlterColumn(PgSqlParser.AlterColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterDropColumn(PgSqlParser.DropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitDropColumn(PgSqlParser.DropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAddConstraint(PgSqlParser.AddConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAddConstraint(PgSqlParser.AddConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlterConstaint(PgSqlParser.AlterConstaintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlterConstaint(PgSqlParser.AlterConstaintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterValidateConstraint(PgSqlParser.ValidateConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitValidateConstraint(PgSqlParser.ValidateConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterDropConstraint(PgSqlParser.DropConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitDropConstraint(PgSqlParser.DropConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterSetWithoutOids(PgSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitSetWithoutOids(PgSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterClusterOnName(PgSqlParser.ClusterOnNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitClusterOnName(PgSqlParser.ClusterOnNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterSetWithOut(PgSqlParser.SetWithOutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitSetWithOut(PgSqlParser.SetWithOutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterUnsupportAlterTableStatement(PgSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitUnsupportAlterTableStatement(PgSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 */
	void enterAlter_column_default(PgSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 */
	void exitAlter_column_default(PgSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 */
	void enterDrop_behavior_(PgSqlParser.Drop_behavior_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 */
	void exitDrop_behavior_(PgSqlParser.Drop_behavior_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 */
	void enterCollate_clause_(PgSqlParser.Collate_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 */
	void exitCollate_clause_(PgSqlParser.Collate_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_using}.
	 * @param ctx the parse tree
	 */
	void enterAlter_using(PgSqlParser.Alter_usingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_using}.
	 * @param ctx the parse tree
	 */
	void exitAlter_using(PgSqlParser.Alter_usingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 */
	void enterReplica_identity(PgSqlParser.Replica_identityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 */
	void exitReplica_identity(PgSqlParser.Replica_identityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reloptions}.
	 * @param ctx the parse tree
	 */
	void enterReloptions(PgSqlParser.ReloptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reloptions}.
	 * @param ctx the parse tree
	 */
	void exitReloptions(PgSqlParser.ReloptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 */
	void enterReloptions_(PgSqlParser.Reloptions_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 */
	void exitReloptions_(PgSqlParser.Reloptions_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 */
	void enterReloption_list(PgSqlParser.Reloption_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 */
	void exitReloption_list(PgSqlParser.Reloption_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 */
	void enterReloption_elem(PgSqlParser.Reloption_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 */
	void exitReloption_elem(PgSqlParser.Reloption_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identity_column_option_list(PgSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identity_column_option_list(PgSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identity_column_option(PgSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identity_column_option(PgSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionboundspec(PgSqlParser.PartitionboundspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionboundspec(PgSqlParser.PartitionboundspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 */
	void enterHash_partbound_elem(PgSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 */
	void exitHash_partbound_elem(PgSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 */
	void enterHash_partbound(PgSqlParser.Hash_partboundContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 */
	void exitHash_partbound(PgSqlParser.Hash_partboundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltercompositetypestmt(PgSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltercompositetypestmt(PgSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type_cmds(PgSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type_cmds(PgSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type_cmd(PgSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type_cmd(PgSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 */
	void enterCloseportalstmt(PgSqlParser.CloseportalstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 */
	void exitCloseportalstmt(PgSqlParser.CloseportalstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copystmt}.
	 * @param ctx the parse tree
	 */
	void enterCopystmt(PgSqlParser.CopystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copystmt}.
	 * @param ctx the parse tree
	 */
	void exitCopystmt(PgSqlParser.CopystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void enterCopy_from(PgSqlParser.Copy_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void exitCopy_from(PgSqlParser.Copy_fromContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#program_}.
	 * @param ctx the parse tree
	 */
	void enterProgram_(PgSqlParser.Program_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#program_}.
	 * @param ctx the parse tree
	 */
	void exitProgram_(PgSqlParser.Program_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 */
	void enterCopy_file_name(PgSqlParser.Copy_file_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 */
	void exitCopy_file_name(PgSqlParser.Copy_file_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_options}.
	 * @param ctx the parse tree
	 */
	void enterCopy_options(PgSqlParser.Copy_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_options}.
	 * @param ctx the parse tree
	 */
	void exitCopy_options(PgSqlParser.Copy_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_opt_list(PgSqlParser.Copy_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_opt_list(PgSqlParser.Copy_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCopy_opt_item(PgSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCopy_opt_item(PgSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#binary_}.
	 * @param ctx the parse tree
	 */
	void enterBinary_(PgSqlParser.Binary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#binary_}.
	 * @param ctx the parse tree
	 */
	void exitBinary_(PgSqlParser.Binary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 */
	void enterCopy_delimiter(PgSqlParser.Copy_delimiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 */
	void exitCopy_delimiter(PgSqlParser.Copy_delimiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#using_}.
	 * @param ctx the parse tree
	 */
	void enterUsing_(PgSqlParser.Using_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#using_}.
	 * @param ctx the parse tree
	 */
	void exitUsing_(PgSqlParser.Using_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_list(PgSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_list(PgSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_elem(PgSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_elem(PgSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg(PgSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg(PgSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg_list(PgSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg_list(PgSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg_list_item(PgSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg_list_item(PgSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatestmt(PgSqlParser.CreatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatestmt(PgSqlParser.CreatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttemp}.
	 * @param ctx the parse tree
	 */
	void enterOpttemp(PgSqlParser.OpttempContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttemp}.
	 * @param ctx the parse tree
	 */
	void exitOpttemp(PgSqlParser.OpttempContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttableelementlist(PgSqlParser.OpttableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttableelementlist(PgSqlParser.OpttableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttypedtableelementlist(PgSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttypedtableelementlist(PgSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTableelementlist(PgSqlParser.TableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTableelementlist(PgSqlParser.TableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTypedtableelementlist(PgSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTypedtableelementlist(PgSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tableelement}.
	 * @param ctx the parse tree
	 */
	void enterTableelement(PgSqlParser.TableelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tableelement}.
	 * @param ctx the parse tree
	 */
	void exitTableelement(PgSqlParser.TableelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 */
	void enterTypedtableelement(PgSqlParser.TypedtableelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 */
	void exitTypedtableelement(PgSqlParser.TypedtableelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void enterColumnDef(PgSqlParser.ColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void exitColumnDef(PgSqlParser.ColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 */
	void enterColumnOptions(PgSqlParser.ColumnOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 */
	void exitColumnOptions(PgSqlParser.ColumnOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#colquallist}.
	 * @param ctx the parse tree
	 */
	void enterColquallist(PgSqlParser.ColquallistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#colquallist}.
	 * @param ctx the parse tree
	 */
	void exitColquallist(PgSqlParser.ColquallistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 */
	void enterColconstraint(PgSqlParser.ColconstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 */
	void exitColconstraint(PgSqlParser.ColconstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintNotNull(PgSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintNotNull(PgSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintNull(PgSqlParser.ConstraintNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintNull(PgSqlParser.ConstraintNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintUnique(PgSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintUnique(PgSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintPrimary(PgSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintPrimary(PgSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintCheck(PgSqlParser.ConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintCheck(PgSqlParser.ConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDefault(PgSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDefault(PgSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintGenerated(PgSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintGenerated(PgSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintReference(PgSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintReference(PgSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generated_when}.
	 * @param ctx the parse tree
	 */
	void enterGenerated_when(PgSqlParser.Generated_whenContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generated_when}.
	 * @param ctx the parse tree
	 */
	void exitGenerated_when(PgSqlParser.Generated_whenContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattr(PgSqlParser.ConstraintattrContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattr(PgSqlParser.ConstraintattrContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeclause(PgSqlParser.TablelikeclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeclause(PgSqlParser.TablelikeclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeoptionlist(PgSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeoptionlist(PgSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeoption(PgSqlParser.TablelikeoptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeoption(PgSqlParser.TablelikeoptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 */
	void enterTableconstraint(PgSqlParser.TableconstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 */
	void exitTableconstraint(PgSqlParser.TableconstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintelem(PgSqlParser.ConstraintelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintelem(PgSqlParser.ConstraintelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 */
	void enterNo_inherit_(PgSqlParser.No_inherit_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 */
	void exitNo_inherit_(PgSqlParser.No_inherit_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#column_list_}.
	 * @param ctx the parse tree
	 */
	void enterColumn_list_(PgSqlParser.Column_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#column_list_}.
	 * @param ctx the parse tree
	 */
	void exitColumn_list_(PgSqlParser.Column_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#columnlist}.
	 * @param ctx the parse tree
	 */
	void enterColumnlist(PgSqlParser.ColumnlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#columnlist}.
	 * @param ctx the parse tree
	 */
	void exitColumnlist(PgSqlParser.ColumnlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#columnElem}.
	 * @param ctx the parse tree
	 */
	void enterColumnElem(PgSqlParser.ColumnElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#columnElem}.
	 * @param ctx the parse tree
	 */
	void exitColumnElem(PgSqlParser.ColumnElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#c_include_}.
	 * @param ctx the parse tree
	 */
	void enterC_include_(PgSqlParser.C_include_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#c_include_}.
	 * @param ctx the parse tree
	 */
	void exitC_include_(PgSqlParser.C_include_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#key_match}.
	 * @param ctx the parse tree
	 */
	void enterKey_match(PgSqlParser.Key_matchContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#key_match}.
	 * @param ctx the parse tree
	 */
	void exitKey_match(PgSqlParser.Key_matchContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 */
	void enterExclusionconstraintlist(PgSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 */
	void exitExclusionconstraintlist(PgSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterExclusionconstraintelem(PgSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitExclusionconstraintelem(PgSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 */
	void enterExclusionwhereclause(PgSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 */
	void exitExclusionwhereclause(PgSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#key_actions}.
	 * @param ctx the parse tree
	 */
	void enterKey_actions(PgSqlParser.Key_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#key_actions}.
	 * @param ctx the parse tree
	 */
	void exitKey_actions(PgSqlParser.Key_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#key_update}.
	 * @param ctx the parse tree
	 */
	void enterKey_update(PgSqlParser.Key_updateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#key_update}.
	 * @param ctx the parse tree
	 */
	void exitKey_update(PgSqlParser.Key_updateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#key_delete}.
	 * @param ctx the parse tree
	 */
	void enterKey_delete(PgSqlParser.Key_deleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#key_delete}.
	 * @param ctx the parse tree
	 */
	void exitKey_delete(PgSqlParser.Key_deleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#key_action}.
	 * @param ctx the parse tree
	 */
	void enterKey_action(PgSqlParser.Key_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#key_action}.
	 * @param ctx the parse tree
	 */
	void exitKey_action(PgSqlParser.Key_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optinherit}.
	 * @param ctx the parse tree
	 */
	void enterOptinherit(PgSqlParser.OptinheritContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optinherit}.
	 * @param ctx the parse tree
	 */
	void exitOptinherit(PgSqlParser.OptinheritContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 */
	void enterOptpartitionspec(PgSqlParser.OptpartitionspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 */
	void exitOptpartitionspec(PgSqlParser.OptpartitionspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionspec(PgSqlParser.PartitionspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionspec(PgSqlParser.PartitionspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#partition_item}.
	 * @param ctx the parse tree
	 */
	void enterPartition_item(PgSqlParser.Partition_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#partition_item}.
	 * @param ctx the parse tree
	 */
	void exitPartition_item(PgSqlParser.Partition_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#part_params}.
	 * @param ctx the parse tree
	 */
	void enterPart_params(PgSqlParser.Part_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#part_params}.
	 * @param ctx the parse tree
	 */
	void exitPart_params(PgSqlParser.Part_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#part_elem}.
	 * @param ctx the parse tree
	 */
	void enterPart_elem(PgSqlParser.Part_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#part_elem}.
	 * @param ctx the parse tree
	 */
	void exitPart_elem(PgSqlParser.Part_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 */
	void enterTable_access_method_clause(PgSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 */
	void exitTable_access_method_clause(PgSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optwith}.
	 * @param ctx the parse tree
	 */
	void enterOptwith(PgSqlParser.OptwithContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optwith}.
	 * @param ctx the parse tree
	 */
	void exitOptwith(PgSqlParser.OptwithContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 */
	void enterOncommitoption(PgSqlParser.OncommitoptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 */
	void exitOncommitoption(PgSqlParser.OncommitoptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 */
	void enterOpttablespace(PgSqlParser.OpttablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 */
	void exitOpttablespace(PgSqlParser.OpttablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 */
	void enterOptconstablespace(PgSqlParser.OptconstablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 */
	void exitOptconstablespace(PgSqlParser.OptconstablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#existingindex}.
	 * @param ctx the parse tree
	 */
	void enterExistingindex(PgSqlParser.ExistingindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#existingindex}.
	 * @param ctx the parse tree
	 */
	void exitExistingindex(PgSqlParser.ExistingindexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatestatsstmt(PgSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatestatsstmt(PgSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterstatsstmt(PgSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterstatsstmt(PgSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateasstmt(PgSqlParser.CreateasstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateasstmt(PgSqlParser.CreateasstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 */
	void enterCreate_as_target(PgSqlParser.Create_as_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 */
	void exitCreate_as_target(PgSqlParser.Create_as_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#with_data_}.
	 * @param ctx the parse tree
	 */
	void enterWith_data_(PgSqlParser.With_data_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#with_data_}.
	 * @param ctx the parse tree
	 */
	void exitWith_data_(PgSqlParser.With_data_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatematviewstmt(PgSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatematviewstmt(PgSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 */
	void enterCreate_mv_target(PgSqlParser.Create_mv_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 */
	void exitCreate_mv_target(PgSqlParser.Create_mv_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optnolog}.
	 * @param ctx the parse tree
	 */
	void enterOptnolog(PgSqlParser.OptnologContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optnolog}.
	 * @param ctx the parse tree
	 */
	void exitOptnolog(PgSqlParser.OptnologContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 */
	void enterRefreshmatviewstmt(PgSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 */
	void exitRefreshmatviewstmt(PgSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateseqstmt(PgSqlParser.CreateseqstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateseqstmt(PgSqlParser.CreateseqstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterseqstmt(PgSqlParser.AlterseqstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterseqstmt(PgSqlParser.AlterseqstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterOptseqoptlist(PgSqlParser.OptseqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitOptseqoptlist(PgSqlParser.OptseqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterOptparenthesizedseqoptlist(PgSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitOptparenthesizedseqoptlist(PgSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterSeqoptlist(PgSqlParser.SeqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitSeqoptlist(PgSqlParser.SeqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 */
	void enterSeqoptelem(PgSqlParser.SeqoptelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 */
	void exitSeqoptelem(PgSqlParser.SeqoptelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#by_}.
	 * @param ctx the parse tree
	 */
	void enterBy_(PgSqlParser.By_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#by_}.
	 * @param ctx the parse tree
	 */
	void exitBy_(PgSqlParser.By_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#numericonly}.
	 * @param ctx the parse tree
	 */
	void enterNumericonly(PgSqlParser.NumericonlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#numericonly}.
	 * @param ctx the parse tree
	 */
	void exitNumericonly(PgSqlParser.NumericonlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 */
	void enterNumericonly_list(PgSqlParser.Numericonly_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 */
	void exitNumericonly_list(PgSqlParser.Numericonly_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateplangstmt(PgSqlParser.CreateplangstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateplangstmt(PgSqlParser.CreateplangstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#trusted_}.
	 * @param ctx the parse tree
	 */
	void enterTrusted_(PgSqlParser.Trusted_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#trusted_}.
	 * @param ctx the parse tree
	 */
	void exitTrusted_(PgSqlParser.Trusted_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#handler_name}.
	 * @param ctx the parse tree
	 */
	void enterHandler_name(PgSqlParser.Handler_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#handler_name}.
	 * @param ctx the parse tree
	 */
	void exitHandler_name(PgSqlParser.Handler_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 */
	void enterInline_handler_(PgSqlParser.Inline_handler_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 */
	void exitInline_handler_(PgSqlParser.Inline_handler_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 */
	void enterValidator_clause(PgSqlParser.Validator_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 */
	void exitValidator_clause(PgSqlParser.Validator_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#validator_}.
	 * @param ctx the parse tree
	 */
	void enterValidator_(PgSqlParser.Validator_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#validator_}.
	 * @param ctx the parse tree
	 */
	void exitValidator_(PgSqlParser.Validator_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#procedural_}.
	 * @param ctx the parse tree
	 */
	void enterProcedural_(PgSqlParser.Procedural_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#procedural_}.
	 * @param ctx the parse tree
	 */
	void exitProcedural_(PgSqlParser.Procedural_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetablespacestmt(PgSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetablespacestmt(PgSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 */
	void enterOpttablespaceowner(PgSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 */
	void exitOpttablespaceowner(PgSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptablespacestmt(PgSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptablespacestmt(PgSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateextensionstmt(PgSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateextensionstmt(PgSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreate_extension_opt_list(PgSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreate_extension_opt_list(PgSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_extension_opt_item(PgSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_extension_opt_item(PgSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterextensionstmt(PgSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterextensionstmt(PgSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_extension_opt_list(PgSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_extension_opt_list(PgSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterAlter_extension_opt_item(PgSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitAlter_extension_opt_item(PgSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterextensioncontentsstmt(PgSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterextensioncontentsstmt(PgSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatefdwstmt(PgSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatefdwstmt(PgSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 */
	void enterFdw_option(PgSqlParser.Fdw_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 */
	void exitFdw_option(PgSqlParser.Fdw_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 */
	void enterFdw_options(PgSqlParser.Fdw_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 */
	void exitFdw_options(PgSqlParser.Fdw_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 */
	void enterFdw_options_(PgSqlParser.Fdw_options_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 */
	void exitFdw_options_(PgSqlParser.Fdw_options_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterfdwstmt(PgSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterfdwstmt(PgSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 */
	void enterCreate_generic_options(PgSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 */
	void exitCreate_generic_options(PgSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_list(PgSqlParser.Generic_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_list(PgSqlParser.Generic_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_options(PgSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_options(PgSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_option_list(PgSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_option_list(PgSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_option_elem(PgSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_option_elem(PgSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_elem(PgSqlParser.Generic_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_elem(PgSqlParser.Generic_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_name(PgSqlParser.Generic_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_name(PgSqlParser.Generic_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_arg(PgSqlParser.Generic_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_arg(PgSqlParser.Generic_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateforeignserverstmt(PgSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateforeignserverstmt(PgSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(PgSqlParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(PgSqlParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 */
	void enterForeign_server_version(PgSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 */
	void exitForeign_server_version(PgSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 */
	void enterForeign_server_version_(PgSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 */
	void exitForeign_server_version_(PgSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterforeignserverstmt(PgSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterforeignserverstmt(PgSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateforeigntablestmt(PgSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateforeigntablestmt(PgSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterImportforeignschemastmt(PgSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitImportforeignschemastmt(PgSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 */
	void enterImport_qualification_type(PgSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 */
	void exitImport_qualification_type(PgSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 */
	void enterImport_qualification(PgSqlParser.Import_qualificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 */
	void exitImport_qualification(PgSqlParser.Import_qualificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateusermappingstmt(PgSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateusermappingstmt(PgSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 */
	void enterAuth_ident(PgSqlParser.Auth_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 */
	void exitAuth_ident(PgSqlParser.Auth_identContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropusermappingstmt(PgSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropusermappingstmt(PgSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterusermappingstmt(PgSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterusermappingstmt(PgSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatepolicystmt(PgSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatepolicystmt(PgSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterpolicystmt(PgSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterpolicystmt(PgSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionalexpr(PgSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionalexpr(PgSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionalwithcheck(PgSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionalwithcheck(PgSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaulttorole(PgSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaulttorole(PgSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionaltorole(PgSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionaltorole(PgSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaultpermissive(PgSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaultpermissive(PgSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaultforcmd(PgSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaultforcmd(PgSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 */
	void enterRow_security_cmd(PgSqlParser.Row_security_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 */
	void exitRow_security_cmd(PgSqlParser.Row_security_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateamstmt(PgSqlParser.CreateamstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateamstmt(PgSqlParser.CreateamstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#am_type}.
	 * @param ctx the parse tree
	 */
	void enterAm_type(PgSqlParser.Am_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#am_type}.
	 * @param ctx the parse tree
	 */
	void exitAm_type(PgSqlParser.Am_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetrigstmt(PgSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetrigstmt(PgSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 */
	void enterTriggeractiontime(PgSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 */
	void exitTriggeractiontime(PgSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 */
	void enterTriggerevents(PgSqlParser.TriggereventsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 */
	void exitTriggerevents(PgSqlParser.TriggereventsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 */
	void enterTriggeroneevent(PgSqlParser.TriggeroneeventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 */
	void exitTriggeroneevent(PgSqlParser.TriggeroneeventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 */
	void enterTriggerreferencing(PgSqlParser.TriggerreferencingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 */
	void exitTriggerreferencing(PgSqlParser.TriggerreferencingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 */
	void enterTriggertransitions(PgSqlParser.TriggertransitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 */
	void exitTriggertransitions(PgSqlParser.TriggertransitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 */
	void enterTriggertransition(PgSqlParser.TriggertransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 */
	void exitTriggertransition(PgSqlParser.TriggertransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 */
	void enterTransitionoldornew(PgSqlParser.TransitionoldornewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 */
	void exitTransitionoldornew(PgSqlParser.TransitionoldornewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 */
	void enterTransitionrowortable(PgSqlParser.TransitionrowortableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 */
	void exitTransitionrowortable(PgSqlParser.TransitionrowortableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 */
	void enterTransitionrelname(PgSqlParser.TransitionrelnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 */
	void exitTransitionrelname(PgSqlParser.TransitionrelnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 */
	void enterTriggerforspec(PgSqlParser.TriggerforspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 */
	void exitTriggerforspec(PgSqlParser.TriggerforspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 */
	void enterTriggerforopteach(PgSqlParser.TriggerforopteachContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 */
	void exitTriggerforopteach(PgSqlParser.TriggerforopteachContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfortype(PgSqlParser.TriggerfortypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfortype(PgSqlParser.TriggerfortypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 */
	void enterTriggerwhen(PgSqlParser.TriggerwhenContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 */
	void exitTriggerwhen(PgSqlParser.TriggerwhenContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 */
	void enterFunction_or_procedure(PgSqlParser.Function_or_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 */
	void exitFunction_or_procedure(PgSqlParser.Function_or_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfuncargs(PgSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfuncargs(PgSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfuncarg(PgSqlParser.TriggerfuncargContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfuncarg(PgSqlParser.TriggerfuncargContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 */
	void enterOptconstrfromtable(PgSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 */
	void exitOptconstrfromtable(PgSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattributespec(PgSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattributespec(PgSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattributeElem(PgSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattributeElem(PgSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateeventtrigstmt(PgSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateeventtrigstmt(PgSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_when_list(PgSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_when_list(PgSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_when_item(PgSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_when_item(PgSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_value_list(PgSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_value_list(PgSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltereventtrigstmt(PgSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltereventtrigstmt(PgSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 */
	void enterEnable_trigger(PgSqlParser.Enable_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 */
	void exitEnable_trigger(PgSqlParser.Enable_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateassertionstmt(PgSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateassertionstmt(PgSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#definestmt}.
	 * @param ctx the parse tree
	 */
	void enterDefinestmt(PgSqlParser.DefinestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#definestmt}.
	 * @param ctx the parse tree
	 */
	void exitDefinestmt(PgSqlParser.DefinestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(PgSqlParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(PgSqlParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#def_list}.
	 * @param ctx the parse tree
	 */
	void enterDef_list(PgSqlParser.Def_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#def_list}.
	 * @param ctx the parse tree
	 */
	void exitDef_list(PgSqlParser.Def_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#def_elem}.
	 * @param ctx the parse tree
	 */
	void enterDef_elem(PgSqlParser.Def_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#def_elem}.
	 * @param ctx the parse tree
	 */
	void exitDef_elem(PgSqlParser.Def_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#def_arg}.
	 * @param ctx the parse tree
	 */
	void enterDef_arg(PgSqlParser.Def_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#def_arg}.
	 * @param ctx the parse tree
	 */
	void exitDef_arg(PgSqlParser.Def_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_definition(PgSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_definition(PgSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_list(PgSqlParser.Old_aggr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_list(PgSqlParser.Old_aggr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_elem(PgSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_elem(PgSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 */
	void enterEnum_val_list_(PgSqlParser.Enum_val_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 */
	void exitEnum_val_list_(PgSqlParser.Enum_val_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 */
	void enterEnum_val_list(PgSqlParser.Enum_val_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 */
	void exitEnum_val_list(PgSqlParser.Enum_val_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterenumstmt(PgSqlParser.AlterenumstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterenumstmt(PgSqlParser.AlterenumstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 */
	void enterIf_not_exists_(PgSqlParser.If_not_exists_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 */
	void exitIf_not_exists_(PgSqlParser.If_not_exists_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateopclassstmt(PgSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateopclassstmt(PgSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_item_list(PgSqlParser.Opclass_item_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_item_list(PgSqlParser.Opclass_item_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_item(PgSqlParser.Opclass_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_item(PgSqlParser.Opclass_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#default_}.
	 * @param ctx the parse tree
	 */
	void enterDefault_(PgSqlParser.Default_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#default_}.
	 * @param ctx the parse tree
	 */
	void exitDefault_(PgSqlParser.Default_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 */
	void enterOpfamily_(PgSqlParser.Opfamily_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 */
	void exitOpfamily_(PgSqlParser.Opfamily_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_purpose(PgSqlParser.Opclass_purposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_purpose(PgSqlParser.Opclass_purposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#recheck_}.
	 * @param ctx the parse tree
	 */
	void enterRecheck_(PgSqlParser.Recheck_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#recheck_}.
	 * @param ctx the parse tree
	 */
	void exitRecheck_(PgSqlParser.Recheck_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateopfamilystmt(PgSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateopfamilystmt(PgSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterAlteropfamilystmt(PgSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitAlteropfamilystmt(PgSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_drop_list(PgSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_drop_list(PgSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_drop(PgSqlParser.Opclass_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_drop(PgSqlParser.Opclass_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropopclassstmt(PgSqlParser.DropopclassstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropopclassstmt(PgSqlParser.DropopclassstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterDropopfamilystmt(PgSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitDropopfamilystmt(PgSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropownedstmt(PgSqlParser.DropownedstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropownedstmt(PgSqlParser.DropownedstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 */
	void enterReassignownedstmt(PgSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 */
	void exitReassignownedstmt(PgSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropstmt(PgSqlParser.DropstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropstmt(PgSqlParser.DropstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptablestmt(PgSqlParser.DroptablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptablestmt(PgSqlParser.DroptablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_any_name(PgSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_any_name(PgSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_name(PgSqlParser.Object_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_name(PgSqlParser.Object_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 */
	void enterDrop_type_name(PgSqlParser.Drop_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 */
	void exitDrop_type_name(PgSqlParser.Drop_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_name_on_any_name(PgSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_name_on_any_name(PgSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 */
	void enterAny_name_list_(PgSqlParser.Any_name_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 */
	void exitAny_name_list_(PgSqlParser.Any_name_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#any_name}.
	 * @param ctx the parse tree
	 */
	void enterAny_name(PgSqlParser.Any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#any_name}.
	 * @param ctx the parse tree
	 */
	void exitAny_name(PgSqlParser.Any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#attrs}.
	 * @param ctx the parse tree
	 */
	void enterAttrs(PgSqlParser.AttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#attrs}.
	 * @param ctx the parse tree
	 */
	void exitAttrs(PgSqlParser.AttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 */
	void enterType_name_list(PgSqlParser.Type_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 */
	void exitType_name_list(PgSqlParser.Type_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 */
	void enterTruncatestmt(PgSqlParser.TruncatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 */
	void exitTruncatestmt(PgSqlParser.TruncatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 */
	void enterRestart_seqs_(PgSqlParser.Restart_seqs_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 */
	void exitRestart_seqs_(PgSqlParser.Restart_seqs_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 */
	void enterCommentstmt(PgSqlParser.CommentstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 */
	void exitCommentstmt(PgSqlParser.CommentstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterComment_table_stmt(PgSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitComment_table_stmt(PgSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 */
	void enterComment_column_stmt(PgSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 */
	void exitComment_column_stmt(PgSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#comment_text}.
	 * @param ctx the parse tree
	 */
	void enterComment_text(PgSqlParser.Comment_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#comment_text}.
	 * @param ctx the parse tree
	 */
	void exitComment_text(PgSqlParser.Comment_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 */
	void enterSeclabelstmt(PgSqlParser.SeclabelstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 */
	void exitSeclabelstmt(PgSqlParser.SeclabelstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#provider_}.
	 * @param ctx the parse tree
	 */
	void enterProvider_(PgSqlParser.Provider_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#provider_}.
	 * @param ctx the parse tree
	 */
	void exitProvider_(PgSqlParser.Provider_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#security_label}.
	 * @param ctx the parse tree
	 */
	void enterSecurity_label(PgSqlParser.Security_labelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#security_label}.
	 * @param ctx the parse tree
	 */
	void exitSecurity_label(PgSqlParser.Security_labelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 */
	void enterFetchstmt(PgSqlParser.FetchstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 */
	void exitFetchstmt(PgSqlParser.FetchstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 */
	void enterFetch_args(PgSqlParser.Fetch_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 */
	void exitFetch_args(PgSqlParser.Fetch_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#from_in}.
	 * @param ctx the parse tree
	 */
	void enterFrom_in(PgSqlParser.From_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#from_in}.
	 * @param ctx the parse tree
	 */
	void exitFrom_in(PgSqlParser.From_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#from_in_}.
	 * @param ctx the parse tree
	 */
	void enterFrom_in_(PgSqlParser.From_in_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#from_in_}.
	 * @param ctx the parse tree
	 */
	void exitFrom_in_(PgSqlParser.From_in_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 */
	void enterGrantstmt(PgSqlParser.GrantstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 */
	void exitGrantstmt(PgSqlParser.GrantstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 */
	void enterRevokestmt(PgSqlParser.RevokestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 */
	void exitRevokestmt(PgSqlParser.RevokestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#privileges}.
	 * @param ctx the parse tree
	 */
	void enterPrivileges(PgSqlParser.PrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#privileges}.
	 * @param ctx the parse tree
	 */
	void exitPrivileges(PgSqlParser.PrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_list(PgSqlParser.Privilege_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_list(PgSqlParser.Privilege_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(PgSqlParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(PgSqlParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_target(PgSqlParser.Privilege_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_target(PgSqlParser.Privilege_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 */
	void enterGrantee_list(PgSqlParser.Grantee_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 */
	void exitGrantee_list(PgSqlParser.Grantee_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grantee}.
	 * @param ctx the parse tree
	 */
	void enterGrantee(PgSqlParser.GranteeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grantee}.
	 * @param ctx the parse tree
	 */
	void exitGrantee(PgSqlParser.GranteeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 */
	void enterGrant_grant_option_(PgSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 */
	void exitGrant_grant_option_(PgSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterGrantrolestmt(PgSqlParser.GrantrolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitGrantrolestmt(PgSqlParser.GrantrolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 */
	void enterRevokerolestmt(PgSqlParser.RevokerolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 */
	void exitRevokerolestmt(PgSqlParser.RevokerolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 */
	void enterGrant_admin_option_(PgSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 */
	void exitGrant_admin_option_(PgSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 */
	void enterGranted_by_(PgSqlParser.Granted_by_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 */
	void exitGranted_by_(PgSqlParser.Granted_by_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdefaultprivilegesstmt(PgSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdefaultprivilegesstmt(PgSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 */
	void enterDefacloptionlist(PgSqlParser.DefacloptionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 */
	void exitDefacloptionlist(PgSqlParser.DefacloptionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#defacloption}.
	 * @param ctx the parse tree
	 */
	void enterDefacloption(PgSqlParser.DefacloptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#defacloption}.
	 * @param ctx the parse tree
	 */
	void exitDefacloption(PgSqlParser.DefacloptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 */
	void enterDefaclaction(PgSqlParser.DefaclactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 */
	void exitDefaclaction(PgSqlParser.DefaclactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 */
	void enterDefacl_privilege_target(PgSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 */
	void exitDefacl_privilege_target(PgSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 */
	void enterIndexstmt(PgSqlParser.IndexstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 */
	void exitIndexstmt(PgSqlParser.IndexstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void enterUnique_(PgSqlParser.Unique_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void exitUnique_(PgSqlParser.Unique_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#single_name_}.
	 * @param ctx the parse tree
	 */
	void enterSingle_name_(PgSqlParser.Single_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#single_name_}.
	 * @param ctx the parse tree
	 */
	void exitSingle_name_(PgSqlParser.Single_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 */
	void enterConcurrently_(PgSqlParser.Concurrently_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 */
	void exitConcurrently_(PgSqlParser.Concurrently_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_name_}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name_(PgSqlParser.Index_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_name_}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name_(PgSqlParser.Index_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 */
	void enterAccess_method_clause(PgSqlParser.Access_method_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 */
	void exitAccess_method_clause(PgSqlParser.Access_method_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_params}.
	 * @param ctx the parse tree
	 */
	void enterIndex_params(PgSqlParser.Index_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_params}.
	 * @param ctx the parse tree
	 */
	void exitIndex_params(PgSqlParser.Index_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 */
	void enterIndex_elem_options(PgSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 */
	void exitIndex_elem_options(PgSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_elem}.
	 * @param ctx the parse tree
	 */
	void enterIndex_elem(PgSqlParser.Index_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_elem}.
	 * @param ctx the parse tree
	 */
	void exitIndex_elem(PgSqlParser.Index_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#include_}.
	 * @param ctx the parse tree
	 */
	void enterInclude_(PgSqlParser.Include_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#include_}.
	 * @param ctx the parse tree
	 */
	void exitInclude_(PgSqlParser.Include_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 */
	void enterIndex_including_params(PgSqlParser.Index_including_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 */
	void exitIndex_including_params(PgSqlParser.Index_including_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#collate_}.
	 * @param ctx the parse tree
	 */
	void enterCollate_(PgSqlParser.Collate_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#collate_}.
	 * @param ctx the parse tree
	 */
	void exitCollate_(PgSqlParser.Collate_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#class_}.
	 * @param ctx the parse tree
	 */
	void enterClass_(PgSqlParser.Class_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#class_}.
	 * @param ctx the parse tree
	 */
	void exitClass_(PgSqlParser.Class_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 */
	void enterAsc_desc_(PgSqlParser.Asc_desc_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 */
	void exitAsc_desc_(PgSqlParser.Asc_desc_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 */
	void enterNulls_order_(PgSqlParser.Nulls_order_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 */
	void exitNulls_order_(PgSqlParser.Nulls_order_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunctionstmt(PgSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunctionstmt(PgSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 */
	void enterOr_replace_(PgSqlParser.Or_replace_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 */
	void exitOr_replace_(PgSqlParser.Or_replace_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_args}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args(PgSqlParser.Func_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_args}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args(PgSqlParser.Func_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_list(PgSqlParser.Func_args_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_list(PgSqlParser.Func_args_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterFunction_with_argtypes_list(PgSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitFunction_with_argtypes_list(PgSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterFunction_with_argtypes(PgSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitFunction_with_argtypes(PgSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_with_defaults(PgSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_with_defaults(PgSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_with_defaults_list(PgSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_with_defaults_list(PgSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg(PgSqlParser.Func_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg(PgSqlParser.Func_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#arg_class}.
	 * @param ctx the parse tree
	 */
	void enterArg_class(PgSqlParser.Arg_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#arg_class}.
	 * @param ctx the parse tree
	 */
	void exitArg_class(PgSqlParser.Arg_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#param_name}.
	 * @param ctx the parse tree
	 */
	void enterParam_name(PgSqlParser.Param_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#param_name}.
	 * @param ctx the parse tree
	 */
	void exitParam_name(PgSqlParser.Param_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_return}.
	 * @param ctx the parse tree
	 */
	void enterFunc_return(PgSqlParser.Func_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_return}.
	 * @param ctx the parse tree
	 */
	void exitFunc_return(PgSqlParser.Func_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_type}.
	 * @param ctx the parse tree
	 */
	void enterFunc_type(PgSqlParser.Func_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_type}.
	 * @param ctx the parse tree
	 */
	void exitFunc_type(PgSqlParser.Func_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_with_default(PgSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_with_default(PgSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 */
	void enterAggr_arg(PgSqlParser.Aggr_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 */
	void exitAggr_arg(PgSqlParser.Aggr_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 */
	void enterAggr_args(PgSqlParser.Aggr_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 */
	void exitAggr_args(PgSqlParser.Aggr_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 */
	void enterAggr_args_list(PgSqlParser.Aggr_args_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 */
	void exitAggr_args_list(PgSqlParser.Aggr_args_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_with_argtypes(PgSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_with_argtypes(PgSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_with_argtypes_list(PgSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_with_argtypes_list(PgSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunc_opt_list(PgSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunc_opt_list(PgSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCommon_func_opt_item(PgSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCommon_func_opt_item(PgSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunc_opt_item(PgSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunc_opt_item(PgSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_body}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body(PgSqlParser.Func_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_body}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body(PgSqlParser.Func_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body_statement(PgSqlParser.Func_body_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body_statement(PgSqlParser.Func_body_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(PgSqlParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(PgSqlParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_as}.
	 * @param ctx the parse tree
	 */
	void enterFunc_as(PgSqlParser.Func_asContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_as}.
	 * @param ctx the parse tree
	 */
	void exitFunc_as(PgSqlParser.Func_asContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 */
	void enterTransform_type_list(PgSqlParser.Transform_type_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 */
	void exitTransform_type_list(PgSqlParser.Transform_type_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#definition_}.
	 * @param ctx the parse tree
	 */
	void enterDefinition_(PgSqlParser.Definition_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#definition_}.
	 * @param ctx the parse tree
	 */
	void exitDefinition_(PgSqlParser.Definition_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 */
	void enterTable_func_column(PgSqlParser.Table_func_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 */
	void exitTable_func_column(PgSqlParser.Table_func_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 */
	void enterTable_func_column_list(PgSqlParser.Table_func_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 */
	void exitTable_func_column_list(PgSqlParser.Table_func_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterfunctionstmt(PgSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterfunctionstmt(PgSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterAlterfunc_opt_list(PgSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitAlterfunc_opt_list(PgSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#restrict_}.
	 * @param ctx the parse tree
	 */
	void enterRestrict_(PgSqlParser.Restrict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#restrict_}.
	 * @param ctx the parse tree
	 */
	void exitRestrict_(PgSqlParser.Restrict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemovefuncstmt(PgSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemovefuncstmt(PgSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemoveaggrstmt(PgSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemoveaggrstmt(PgSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemoveoperstmt(PgSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemoveoperstmt(PgSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterOper_argtypes(PgSqlParser.Oper_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitOper_argtypes(PgSqlParser.Oper_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#any_operator}.
	 * @param ctx the parse tree
	 */
	void enterAny_operator(PgSqlParser.Any_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#any_operator}.
	 * @param ctx the parse tree
	 */
	void exitAny_operator(PgSqlParser.Any_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterOperator_with_argtypes_list(PgSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitOperator_with_argtypes_list(PgSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterOperator_with_argtypes(PgSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitOperator_with_argtypes(PgSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dostmt}.
	 * @param ctx the parse tree
	 */
	void enterDostmt(PgSqlParser.DostmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dostmt}.
	 * @param ctx the parse tree
	 */
	void exitDostmt(PgSqlParser.DostmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterDostmt_opt_list(PgSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitDostmt_opt_list(PgSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterDostmt_opt_item(PgSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitDostmt_opt_item(PgSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatecaststmt(PgSqlParser.CreatecaststmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatecaststmt(PgSqlParser.CreatecaststmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cast_context}.
	 * @param ctx the parse tree
	 */
	void enterCast_context(PgSqlParser.Cast_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cast_context}.
	 * @param ctx the parse tree
	 */
	void exitCast_context(PgSqlParser.Cast_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 */
	void enterDropcaststmt(PgSqlParser.DropcaststmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 */
	void exitDropcaststmt(PgSqlParser.DropcaststmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 */
	void enterIf_exists_(PgSqlParser.If_exists_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 */
	void exitIf_exists_(PgSqlParser.If_exists_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetransformstmt(PgSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetransformstmt(PgSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 */
	void enterTransform_element_list(PgSqlParser.Transform_element_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 */
	void exitTransform_element_list(PgSqlParser.Transform_element_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptransformstmt(PgSqlParser.DroptransformstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptransformstmt(PgSqlParser.DroptransformstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 */
	void enterReindexstmt(PgSqlParser.ReindexstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 */
	void exitReindexstmt(PgSqlParser.ReindexstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 */
	void enterReindex_target_relation(PgSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 */
	void exitReindex_target_relation(PgSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 */
	void enterReindex_target_all(PgSqlParser.Reindex_target_allContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 */
	void exitReindex_target_all(PgSqlParser.Reindex_target_allContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 */
	void enterReindex_option_list(PgSqlParser.Reindex_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 */
	void exitReindex_option_list(PgSqlParser.Reindex_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertblspcstmt(PgSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertblspcstmt(PgSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 */
	void enterRenamestmt(PgSqlParser.RenamestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 */
	void exitRenamestmt(PgSqlParser.RenamestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_table_stmt(PgSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_table_stmt(PgSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_schema_stmt(PgSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_schema_stmt(PgSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_database_stmt(PgSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_database_stmt(PgSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_column_stmt(PgSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_column_stmt(PgSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#column_}.
	 * @param ctx the parse tree
	 */
	void enterColumn_(PgSqlParser.Column_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#column_}.
	 * @param ctx the parse tree
	 */
	void exitColumn_(PgSqlParser.Column_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_data_}.
	 * @param ctx the parse tree
	 */
	void enterSet_data_(PgSqlParser.Set_data_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_data_}.
	 * @param ctx the parse tree
	 */
	void exitSet_data_(PgSqlParser.Set_data_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterobjectdependsstmt(PgSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterobjectdependsstmt(PgSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#no_}.
	 * @param ctx the parse tree
	 */
	void enterNo_(PgSqlParser.No_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#no_}.
	 * @param ctx the parse tree
	 */
	void exitNo_(PgSqlParser.No_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterobjectschemastmt(PgSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterobjectschemastmt(PgSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlteroperatorstmt(PgSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlteroperatorstmt(PgSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_list(PgSqlParser.Operator_def_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_list(PgSqlParser.Operator_def_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_elem(PgSqlParser.Operator_def_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_elem(PgSqlParser.Operator_def_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_arg(PgSqlParser.Operator_def_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_arg(PgSqlParser.Operator_def_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertypestmt(PgSqlParser.AltertypestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertypestmt(PgSqlParser.AltertypestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterownerstmt(PgSqlParser.AlterownerstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterownerstmt(PgSqlParser.AlterownerstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatepublicationstmt(PgSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatepublicationstmt(PgSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 */
	void enterPublication_for_tables_(PgSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 */
	void exitPublication_for_tables_(PgSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 */
	void enterPublication_for_tables(PgSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 */
	void exitPublication_for_tables(PgSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterpublicationstmt(PgSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterpublicationstmt(PgSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatesubscriptionstmt(PgSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatesubscriptionstmt(PgSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 */
	void enterPublication_name_list(PgSqlParser.Publication_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 */
	void exitPublication_name_list(PgSqlParser.Publication_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 */
	void enterPublication_name_item(PgSqlParser.Publication_name_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 */
	void exitPublication_name_item(PgSqlParser.Publication_name_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltersubscriptionstmt(PgSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltersubscriptionstmt(PgSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropsubscriptionstmt(PgSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropsubscriptionstmt(PgSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 */
	void enterRulestmt(PgSqlParser.RulestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 */
	void exitRulestmt(PgSqlParser.RulestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionlist(PgSqlParser.RuleactionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionlist(PgSqlParser.RuleactionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionmulti(PgSqlParser.RuleactionmultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionmulti(PgSqlParser.RuleactionmultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionstmt(PgSqlParser.RuleactionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionstmt(PgSqlParser.RuleactionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionstmtOrEmpty(PgSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionstmtOrEmpty(PgSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(PgSqlParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(PgSqlParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#instead_}.
	 * @param ctx the parse tree
	 */
	void enterInstead_(PgSqlParser.Instead_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#instead_}.
	 * @param ctx the parse tree
	 */
	void exitInstead_(PgSqlParser.Instead_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 */
	void enterNotifystmt(PgSqlParser.NotifystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 */
	void exitNotifystmt(PgSqlParser.NotifystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 */
	void enterNotify_payload(PgSqlParser.Notify_payloadContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 */
	void exitNotify_payload(PgSqlParser.Notify_payloadContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 */
	void enterListenstmt(PgSqlParser.ListenstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 */
	void exitListenstmt(PgSqlParser.ListenstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 */
	void enterUnlistenstmt(PgSqlParser.UnlistenstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 */
	void exitUnlistenstmt(PgSqlParser.UnlistenstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 */
	void enterTransactionstmt(PgSqlParser.TransactionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 */
	void exitTransactionstmt(PgSqlParser.TransactionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transaction_}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_(PgSqlParser.Transaction_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transaction_}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_(PgSqlParser.Transaction_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_item(PgSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_item(PgSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_list(PgSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_list(PgSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_list_or_empty(PgSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_list_or_empty(PgSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_chain_(PgSqlParser.Transaction_chain_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_chain_(PgSqlParser.Transaction_chain_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 */
	void enterViewstmt(PgSqlParser.ViewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 */
	void exitViewstmt(PgSqlParser.ViewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#check_option_}.
	 * @param ctx the parse tree
	 */
	void enterCheck_option_(PgSqlParser.Check_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#check_option_}.
	 * @param ctx the parse tree
	 */
	void exitCheck_option_(PgSqlParser.Check_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 */
	void enterLoadstmt(PgSqlParser.LoadstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 */
	void exitLoadstmt(PgSqlParser.LoadstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatedbstmt(PgSqlParser.CreatedbstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatedbstmt(PgSqlParser.CreatedbstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_list(PgSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_list(PgSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_items(PgSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_items(PgSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_item(PgSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_item(PgSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_name(PgSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_name(PgSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#equal_}.
	 * @param ctx the parse tree
	 */
	void enterEqual_(PgSqlParser.Equal_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#equal_}.
	 * @param ctx the parse tree
	 */
	void exitEqual_(PgSqlParser.Equal_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdatabasestmt(PgSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdatabasestmt(PgSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 */
	void enterSetTablespaceName(PgSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 */
	void exitSetTablespaceName(PgSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 */
	void enterRefresh_collation_version(PgSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 */
	void exitRefresh_collation_version(PgSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdatabasesetstmt(PgSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdatabasesetstmt(PgSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropdbstmt(PgSqlParser.DropdbstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropdbstmt(PgSqlParser.DropdbstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterDropschemastmt(PgSqlParser.DropschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitDropschemastmt(PgSqlParser.DropschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 */
	void enterDrop_option_list(PgSqlParser.Drop_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 */
	void exitDrop_option_list(PgSqlParser.Drop_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#drop_option}.
	 * @param ctx the parse tree
	 */
	void enterDrop_option(PgSqlParser.Drop_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#drop_option}.
	 * @param ctx the parse tree
	 */
	void exitDrop_option(PgSqlParser.Drop_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltercollationstmt(PgSqlParser.AltercollationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltercollationstmt(PgSqlParser.AltercollationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltersystemstmt(PgSqlParser.AltersystemstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltersystemstmt(PgSqlParser.AltersystemstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatedomainstmt(PgSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatedomainstmt(PgSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdomainstmt(PgSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdomainstmt(PgSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#as_}.
	 * @param ctx the parse tree
	 */
	void enterAs_(PgSqlParser.As_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#as_}.
	 * @param ctx the parse tree
	 */
	void exitAs_(PgSqlParser.As_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertsdictionarystmt(PgSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertsdictionarystmt(PgSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertsconfigurationstmt(PgSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertsconfigurationstmt(PgSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#any_with}.
	 * @param ctx the parse tree
	 */
	void enterAny_with(PgSqlParser.Any_withContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#any_with}.
	 * @param ctx the parse tree
	 */
	void exitAny_with(PgSqlParser.Any_withContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateconversionstmt(PgSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateconversionstmt(PgSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 */
	void enterClusterstmt(PgSqlParser.ClusterstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 */
	void exitClusterstmt(PgSqlParser.ClusterstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 */
	void enterCluster_index_specification(PgSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 */
	void exitCluster_index_specification(PgSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 */
	void enterVacuumstmt(PgSqlParser.VacuumstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 */
	void exitVacuumstmt(PgSqlParser.VacuumstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzestmt(PgSqlParser.AnalyzestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzestmt(PgSqlParser.AnalyzestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_list(PgSqlParser.Utility_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_list(PgSqlParser.Utility_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_list(PgSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_list(PgSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_keyword(PgSqlParser.Analyze_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_keyword(PgSqlParser.Analyze_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_elem(PgSqlParser.Utility_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_elem(PgSqlParser.Utility_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_name(PgSqlParser.Utility_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_name(PgSqlParser.Utility_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_arg(PgSqlParser.Utility_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_arg(PgSqlParser.Utility_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_elem(PgSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_elem(PgSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_name(PgSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_name(PgSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_arg(PgSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_arg(PgSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#analyze_}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_(PgSqlParser.Analyze_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#analyze_}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_(PgSqlParser.Analyze_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#verbose_}.
	 * @param ctx the parse tree
	 */
	void enterVerbose_(PgSqlParser.Verbose_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#verbose_}.
	 * @param ctx the parse tree
	 */
	void exitVerbose_(PgSqlParser.Verbose_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#full_}.
	 * @param ctx the parse tree
	 */
	void enterFull_(PgSqlParser.Full_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#full_}.
	 * @param ctx the parse tree
	 */
	void exitFull_(PgSqlParser.Full_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#freeze_}.
	 * @param ctx the parse tree
	 */
	void enterFreeze_(PgSqlParser.Freeze_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#freeze_}.
	 * @param ctx the parse tree
	 */
	void exitFreeze_(PgSqlParser.Freeze_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#name_list_}.
	 * @param ctx the parse tree
	 */
	void enterName_list_(PgSqlParser.Name_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#name_list_}.
	 * @param ctx the parse tree
	 */
	void exitName_list_(PgSqlParser.Name_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation(PgSqlParser.Vacuum_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation(PgSqlParser.Vacuum_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation_list(PgSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation_list(PgSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation_list_(PgSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation_list_(PgSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 */
	void enterExplainstmt(PgSqlParser.ExplainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 */
	void exitExplainstmt(PgSqlParser.ExplainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 */
	void enterExplainablestmt(PgSqlParser.ExplainablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 */
	void exitExplainablestmt(PgSqlParser.ExplainablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_list(PgSqlParser.Explain_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_list(PgSqlParser.Explain_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_elem(PgSqlParser.Explain_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_elem(PgSqlParser.Explain_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_name(PgSqlParser.Explain_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_name(PgSqlParser.Explain_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_arg(PgSqlParser.Explain_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_arg(PgSqlParser.Explain_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 */
	void enterPreparestmt(PgSqlParser.PreparestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 */
	void exitPreparestmt(PgSqlParser.PreparestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrep_type_clause(PgSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrep_type_clause(PgSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 */
	void enterPreparablestmt(PgSqlParser.PreparablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 */
	void exitPreparablestmt(PgSqlParser.PreparablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#executestmt}.
	 * @param ctx the parse tree
	 */
	void enterExecutestmt(PgSqlParser.ExecutestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#executestmt}.
	 * @param ctx the parse tree
	 */
	void exitExecutestmt(PgSqlParser.ExecutestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 */
	void enterExecute_param_clause(PgSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 */
	void exitExecute_param_clause(PgSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatestmt(PgSqlParser.DeallocatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatestmt(PgSqlParser.DeallocatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 */
	void enterInsertstmt(PgSqlParser.InsertstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 */
	void exitInsertstmt(PgSqlParser.InsertstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#insert_target}.
	 * @param ctx the parse tree
	 */
	void enterInsert_target(PgSqlParser.Insert_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#insert_target}.
	 * @param ctx the parse tree
	 */
	void exitInsert_target(PgSqlParser.Insert_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 */
	void enterInsert_rest(PgSqlParser.Insert_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 */
	void exitInsert_rest(PgSqlParser.Insert_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#override_kind}.
	 * @param ctx the parse tree
	 */
	void enterOverride_kind(PgSqlParser.Override_kindContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#override_kind}.
	 * @param ctx the parse tree
	 */
	void exitOverride_kind(PgSqlParser.Override_kindContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_list(PgSqlParser.Insert_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_list(PgSqlParser.Insert_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_item(PgSqlParser.Insert_column_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_item(PgSqlParser.Insert_column_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 */
	void enterOn_conflict_(PgSqlParser.On_conflict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 */
	void exitOn_conflict_(PgSqlParser.On_conflict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 */
	void enterConf_expr_(PgSqlParser.Conf_expr_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 */
	void exitConf_expr_(PgSqlParser.Conf_expr_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterReturning_clause(PgSqlParser.Returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitReturning_clause(PgSqlParser.Returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 */
	void enterMergestmt(PgSqlParser.MergestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 */
	void exitMergestmt(PgSqlParser.MergestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_insert_clause(PgSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_insert_clause(PgSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_clause(PgSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_clause(PgSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_delete_clause(PgSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_delete_clause(PgSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 */
	void enterDeletestmt(PgSqlParser.DeletestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 */
	void exitDeletestmt(PgSqlParser.DeletestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_clause(PgSqlParser.Using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_clause(PgSqlParser.Using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 */
	void enterLockstmt(PgSqlParser.LockstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 */
	void exitLockstmt(PgSqlParser.LockstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#lock_}.
	 * @param ctx the parse tree
	 */
	void enterLock_(PgSqlParser.Lock_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#lock_}.
	 * @param ctx the parse tree
	 */
	void exitLock_(PgSqlParser.Lock_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#lock_type}.
	 * @param ctx the parse tree
	 */
	void enterLock_type(PgSqlParser.Lock_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#lock_type}.
	 * @param ctx the parse tree
	 */
	void exitLock_type(PgSqlParser.Lock_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#nowait_}.
	 * @param ctx the parse tree
	 */
	void enterNowait_(PgSqlParser.Nowait_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#nowait_}.
	 * @param ctx the parse tree
	 */
	void exitNowait_(PgSqlParser.Nowait_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 */
	void enterNowait_or_skip_(PgSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 */
	void exitNowait_or_skip_(PgSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdatestmt(PgSqlParser.UpdatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdatestmt(PgSqlParser.UpdatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 */
	void enterSet_clause_list(PgSqlParser.Set_clause_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 */
	void exitSet_clause_list(PgSqlParser.Set_clause_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void enterSet_clause(PgSqlParser.Set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void exitSet_clause(PgSqlParser.Set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_target}.
	 * @param ctx the parse tree
	 */
	void enterSet_target(PgSqlParser.Set_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_target}.
	 * @param ctx the parse tree
	 */
	void exitSet_target(PgSqlParser.Set_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 */
	void enterSet_target_list(PgSqlParser.Set_target_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 */
	void exitSet_target_list(PgSqlParser.Set_target_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 */
	void enterDeclarecursorstmt(PgSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 */
	void exitDeclarecursorstmt(PgSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void enterCursor_name(PgSqlParser.Cursor_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void exitCursor_name(PgSqlParser.Cursor_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 */
	void enterCursor_options(PgSqlParser.Cursor_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 */
	void exitCursor_options(PgSqlParser.Cursor_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#hold_}.
	 * @param ctx the parse tree
	 */
	void enterHold_(PgSqlParser.Hold_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#hold_}.
	 * @param ctx the parse tree
	 */
	void exitHold_(PgSqlParser.Hold_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 */
	void enterSelectstmt(PgSqlParser.SelectstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 */
	void exitSelectstmt(PgSqlParser.SelectstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 */
	void enterSelect_with_parens(PgSqlParser.Select_with_parensContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 */
	void exitSelect_with_parens(PgSqlParser.Select_with_parensContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 */
	void enterSelect_no_parens(PgSqlParser.Select_no_parensContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 */
	void exitSelect_no_parens(PgSqlParser.Select_no_parensContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void enterSelect_clause(PgSqlParser.Select_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void exitSelect_clause(PgSqlParser.Select_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void enterUnion_context(PgSqlParser.Union_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void exitUnion_context(PgSqlParser.Union_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 */
	void enterSimple_select_pramary(PgSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 */
	void exitSimple_select_pramary(PgSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(PgSqlParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(PgSqlParser.With_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cte_list}.
	 * @param ctx the parse tree
	 */
	void enterCte_list(PgSqlParser.Cte_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cte_list}.
	 * @param ctx the parse tree
	 */
	void exitCte_list(PgSqlParser.Cte_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 */
	void enterCommon_table_expr(PgSqlParser.Common_table_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 */
	void exitCommon_table_expr(PgSqlParser.Common_table_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#materialized_}.
	 * @param ctx the parse tree
	 */
	void enterMaterialized_(PgSqlParser.Materialized_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#materialized_}.
	 * @param ctx the parse tree
	 */
	void exitMaterialized_(PgSqlParser.Materialized_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause_(PgSqlParser.With_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause_(PgSqlParser.With_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause(PgSqlParser.Into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause(PgSqlParser.Into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#strict_}.
	 * @param ctx the parse tree
	 */
	void enterStrict_(PgSqlParser.Strict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#strict_}.
	 * @param ctx the parse tree
	 */
	void exitStrict_(PgSqlParser.Strict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 */
	void enterOpttempTableName(PgSqlParser.OpttempTableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 */
	void exitOpttempTableName(PgSqlParser.OpttempTableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#table_}.
	 * @param ctx the parse tree
	 */
	void enterTable_(PgSqlParser.Table_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#table_}.
	 * @param ctx the parse tree
	 */
	void exitTable_(PgSqlParser.Table_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 */
	void enterAll_or_distinct(PgSqlParser.All_or_distinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 */
	void exitAll_or_distinct(PgSqlParser.All_or_distinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 */
	void enterDistinct_clause(PgSqlParser.Distinct_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 */
	void exitDistinct_clause(PgSqlParser.Distinct_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 */
	void enterAll_clause_(PgSqlParser.All_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 */
	void exitAll_clause_(PgSqlParser.All_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 */
	void enterSort_clause_(PgSqlParser.Sort_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 */
	void exitSort_clause_(PgSqlParser.Sort_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void enterSort_clause(PgSqlParser.Sort_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void exitSort_clause(PgSqlParser.Sort_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 */
	void enterSortby_list(PgSqlParser.Sortby_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 */
	void exitSortby_list(PgSqlParser.Sortby_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sortby}.
	 * @param ctx the parse tree
	 */
	void enterSortby(PgSqlParser.SortbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sortby}.
	 * @param ctx the parse tree
	 */
	void exitSortby(PgSqlParser.SortbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_limit}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit(PgSqlParser.Select_limitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_limit}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit(PgSqlParser.Select_limitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit_(PgSqlParser.Select_limit_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit_(PgSqlParser.Select_limit_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void enterLimit_clause(PgSqlParser.Limit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void exitLimit_clause(PgSqlParser.Limit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void enterOffset_clause(PgSqlParser.Offset_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void exitOffset_clause(PgSqlParser.Offset_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit_value(PgSqlParser.Select_limit_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit_value(PgSqlParser.Select_limit_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_offset_value(PgSqlParser.Select_offset_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_offset_value(PgSqlParser.Select_offset_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_fetch_first_value(PgSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_fetch_first_value(PgSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 */
	void enterI_or_f_const(PgSqlParser.I_or_f_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 */
	void exitI_or_f_const(PgSqlParser.I_or_f_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 */
	void enterRow_or_rows(PgSqlParser.Row_or_rowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 */
	void exitRow_or_rows(PgSqlParser.Row_or_rowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 */
	void enterFirst_or_next(PgSqlParser.First_or_nextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 */
	void exitFirst_or_next(PgSqlParser.First_or_nextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#group_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_clause(PgSqlParser.Group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#group_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_clause(PgSqlParser.Group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_list(PgSqlParser.Group_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_list(PgSqlParser.Group_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_item(PgSqlParser.Group_by_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_item(PgSqlParser.Group_by_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 */
	void enterEmpty_grouping_set(PgSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 */
	void exitEmpty_grouping_set(PgSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 */
	void enterRollup_clause(PgSqlParser.Rollup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 */
	void exitRollup_clause(PgSqlParser.Rollup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 */
	void enterCube_clause(PgSqlParser.Cube_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 */
	void exitCube_clause(PgSqlParser.Cube_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_clause(PgSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_clause(PgSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(PgSqlParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(PgSqlParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_clause(PgSqlParser.For_locking_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_clause(PgSqlParser.For_locking_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_clause_(PgSqlParser.For_locking_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_clause_(PgSqlParser.For_locking_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_items(PgSqlParser.For_locking_itemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_items(PgSqlParser.For_locking_itemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_item(PgSqlParser.For_locking_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_item(PgSqlParser.For_locking_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_strength(PgSqlParser.For_locking_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_strength(PgSqlParser.For_locking_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 */
	void enterLocked_rels_list(PgSqlParser.Locked_rels_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 */
	void exitLocked_rels_list(PgSqlParser.Locked_rels_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void enterValues_clause(PgSqlParser.Values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void exitValues_clause(PgSqlParser.Values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_clause(PgSqlParser.From_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_clause(PgSqlParser.From_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#from_list}.
	 * @param ctx the parse tree
	 */
	void enterFrom_list(PgSqlParser.From_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#from_list}.
	 * @param ctx the parse tree
	 */
	void exitFrom_list(PgSqlParser.From_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref(PgSqlParser.Table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref(PgSqlParser.Table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#cross_join}.
	 * @param ctx the parse tree
	 */
	void enterCross_join(PgSqlParser.Cross_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#cross_join}.
	 * @param ctx the parse tree
	 */
	void exitCross_join(PgSqlParser.Cross_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#normal_join}.
	 * @param ctx the parse tree
	 */
	void enterNormal_join(PgSqlParser.Normal_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#normal_join}.
	 * @param ctx the parse tree
	 */
	void exitNormal_join(PgSqlParser.Normal_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#natural_join}.
	 * @param ctx the parse tree
	 */
	void enterNatural_join(PgSqlParser.Natural_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#natural_join}.
	 * @param ctx the parse tree
	 */
	void exitNatural_join(PgSqlParser.Natural_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlias_clause(PgSqlParser.Alias_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlias_clause(PgSqlParser.Alias_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 */
	void enterFunc_alias_clause(PgSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 */
	void exitFunc_alias_clause(PgSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void enterJoin_type(PgSqlParser.Join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void exitJoin_type(PgSqlParser.Join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#join_qual}.
	 * @param ctx the parse tree
	 */
	void enterJoin_qual(PgSqlParser.Join_qualContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#join_qual}.
	 * @param ctx the parse tree
	 */
	void exitJoin_qual(PgSqlParser.Join_qualContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr(PgSqlParser.Relation_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr(PgSqlParser.Relation_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr_list(PgSqlParser.Relation_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr_list(PgSqlParser.Relation_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr_opt_alias(PgSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr_opt_alias(PgSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablesample_clause(PgSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablesample_clause(PgSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 */
	void enterRepeatable_clause_(PgSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 */
	void exitRepeatable_clause_(PgSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_table}.
	 * @param ctx the parse tree
	 */
	void enterFunc_table(PgSqlParser.Func_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_table}.
	 * @param ctx the parse tree
	 */
	void exitFunc_table(PgSqlParser.Func_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 */
	void enterRowsfrom_item(PgSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 */
	void exitRowsfrom_item(PgSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 */
	void enterRowsfrom_list(PgSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 */
	void exitRowsfrom_list(PgSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 */
	void enterCol_def_list_(PgSqlParser.Col_def_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 */
	void exitCol_def_list_(PgSqlParser.Col_def_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 */
	void enterOrdinality_(PgSqlParser.Ordinality_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 */
	void exitOrdinality_(PgSqlParser.Ordinality_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(PgSqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(PgSqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_or_current_clause(PgSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_or_current_clause(PgSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttablefuncelementlist(PgSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttablefuncelementlist(PgSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTablefuncelementlist(PgSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTablefuncelementlist(PgSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 */
	void enterTablefuncelement(PgSqlParser.TablefuncelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 */
	void exitTablefuncelement(PgSqlParser.TablefuncelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void enterXmltable(PgSqlParser.XmltableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void exitXmltable(PgSqlParser.XmltableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_list(PgSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_list(PgSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_el(PgSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_el(PgSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_option_list(PgSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_option_list(PgSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_option_el(PgSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_option_el(PgSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespace_list(PgSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespace_list(PgSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespace_el(PgSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespace_el(PgSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#typename}.
	 * @param ctx the parse tree
	 */
	void enterTypename(PgSqlParser.TypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#typename}.
	 * @param ctx the parse tree
	 */
	void exitTypename(PgSqlParser.TypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 */
	void enterOpt_array_bounds(PgSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 */
	void exitOpt_array_bounds(PgSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 */
	void enterSimpletypename(PgSqlParser.SimpletypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 */
	void exitSimpletypename(PgSqlParser.SimpletypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#consttypename}.
	 * @param ctx the parse tree
	 */
	void enterConsttypename(PgSqlParser.ConsttypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#consttypename}.
	 * @param ctx the parse tree
	 */
	void exitConsttypename(PgSqlParser.ConsttypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#generictype}.
	 * @param ctx the parse tree
	 */
	void enterGenerictype(PgSqlParser.GenerictypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#generictype}.
	 * @param ctx the parse tree
	 */
	void exitGenerictype(PgSqlParser.GenerictypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 */
	void enterType_modifiers_(PgSqlParser.Type_modifiers_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 */
	void exitType_modifiers_(PgSqlParser.Type_modifiers_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void enterNumeric(PgSqlParser.NumericContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void exitNumeric(PgSqlParser.NumericContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#float_}.
	 * @param ctx the parse tree
	 */
	void enterFloat_(PgSqlParser.Float_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#float_}.
	 * @param ctx the parse tree
	 */
	void exitFloat_(PgSqlParser.Float_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bit}.
	 * @param ctx the parse tree
	 */
	void enterBit(PgSqlParser.BitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bit}.
	 * @param ctx the parse tree
	 */
	void exitBit(PgSqlParser.BitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constbit}.
	 * @param ctx the parse tree
	 */
	void enterConstbit(PgSqlParser.ConstbitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constbit}.
	 * @param ctx the parse tree
	 */
	void exitConstbit(PgSqlParser.ConstbitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 */
	void enterBitwithlength(PgSqlParser.BitwithlengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 */
	void exitBitwithlength(PgSqlParser.BitwithlengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 */
	void enterBitwithoutlength(PgSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 */
	void exitBitwithoutlength(PgSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(PgSqlParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(PgSqlParser.CharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 */
	void enterConstcharacter(PgSqlParser.ConstcharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 */
	void exitConstcharacter(PgSqlParser.ConstcharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#character_c}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_c(PgSqlParser.Character_cContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#character_c}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_c(PgSqlParser.Character_cContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#varying_}.
	 * @param ctx the parse tree
	 */
	void enterVarying_(PgSqlParser.Varying_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#varying_}.
	 * @param ctx the parse tree
	 */
	void exitVarying_(PgSqlParser.Varying_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 */
	void enterConstdatetime(PgSqlParser.ConstdatetimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 */
	void exitConstdatetime(PgSqlParser.ConstdatetimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#constinterval}.
	 * @param ctx the parse tree
	 */
	void enterConstinterval(PgSqlParser.ConstintervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#constinterval}.
	 * @param ctx the parse tree
	 */
	void exitConstinterval(PgSqlParser.ConstintervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#timezone_}.
	 * @param ctx the parse tree
	 */
	void enterTimezone_(PgSqlParser.Timezone_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#timezone_}.
	 * @param ctx the parse tree
	 */
	void exitTimezone_(PgSqlParser.Timezone_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#interval_}.
	 * @param ctx the parse tree
	 */
	void enterInterval_(PgSqlParser.Interval_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#interval_}.
	 * @param ctx the parse tree
	 */
	void exitInterval_(PgSqlParser.Interval_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#interval_second}.
	 * @param ctx the parse tree
	 */
	void enterInterval_second(PgSqlParser.Interval_secondContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#interval_second}.
	 * @param ctx the parse tree
	 */
	void exitInterval_second(PgSqlParser.Interval_secondContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#jsonType}.
	 * @param ctx the parse tree
	 */
	void enterJsonType(PgSqlParser.JsonTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#jsonType}.
	 * @param ctx the parse tree
	 */
	void exitJsonType(PgSqlParser.JsonTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#escape_}.
	 * @param ctx the parse tree
	 */
	void enterEscape_(PgSqlParser.Escape_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#escape_}.
	 * @param ctx the parse tree
	 */
	void exitEscape_(PgSqlParser.Escape_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr}.
	 * @param ctx the parse tree
	 */
	void enterA_expr(PgSqlParser.A_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr}.
	 * @param ctx the parse tree
	 */
	void exitA_expr(PgSqlParser.A_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_qual(PgSqlParser.A_expr_qualContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_qual(PgSqlParser.A_expr_qualContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_lessless(PgSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_lessless(PgSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_or(PgSqlParser.A_expr_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_or(PgSqlParser.A_expr_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_and(PgSqlParser.A_expr_andContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_and(PgSqlParser.A_expr_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_between(PgSqlParser.A_expr_betweenContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_between(PgSqlParser.A_expr_betweenContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_in(PgSqlParser.A_expr_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_in(PgSqlParser.A_expr_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_not(PgSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_not(PgSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_isnull(PgSqlParser.A_expr_isnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_isnull(PgSqlParser.A_expr_isnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_is_not(PgSqlParser.A_expr_is_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_is_not(PgSqlParser.A_expr_is_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_compare(PgSqlParser.A_expr_compareContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_compare(PgSqlParser.A_expr_compareContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_like(PgSqlParser.A_expr_likeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_like(PgSqlParser.A_expr_likeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_qual_op(PgSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_qual_op(PgSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_qualop(PgSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_qualop(PgSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_add(PgSqlParser.A_expr_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_add(PgSqlParser.A_expr_addContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_mul(PgSqlParser.A_expr_mulContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_mul(PgSqlParser.A_expr_mulContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_caret(PgSqlParser.A_expr_caretContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_caret(PgSqlParser.A_expr_caretContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_sign(PgSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_sign(PgSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_at_time_zone(PgSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_at_time_zone(PgSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_collate(PgSqlParser.A_expr_collateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_collate(PgSqlParser.A_expr_collateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_typecast(PgSqlParser.A_expr_typecastContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_typecast(PgSqlParser.A_expr_typecastContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#b_expr}.
	 * @param ctx the parse tree
	 */
	void enterB_expr(PgSqlParser.B_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#b_expr}.
	 * @param ctx the parse tree
	 */
	void exitB_expr(PgSqlParser.B_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#c_expr}.
	 * @param ctx the parse tree
	 */
	void enterC_expr(PgSqlParser.C_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#c_expr}.
	 * @param ctx the parse tree
	 */
	void exitC_expr(PgSqlParser.C_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 */
	void enterPlsqlvariablename(PgSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 */
	void exitPlsqlvariablename(PgSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_application}.
	 * @param ctx the parse tree
	 */
	void enterFunc_application(PgSqlParser.Func_applicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_application}.
	 * @param ctx the parse tree
	 */
	void exitFunc_application(PgSqlParser.Func_applicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#star_context}.
	 * @param ctx the parse tree
	 */
	void enterStar_context(PgSqlParser.Star_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#star_context}.
	 * @param ctx the parse tree
	 */
	void exitStar_context(PgSqlParser.Star_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_expr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr(PgSqlParser.Func_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_expr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr(PgSqlParser.Func_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr_windowless(PgSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr_windowless(PgSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr_common_subexpr(PgSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr_common_subexpr(PgSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 */
	void enterXml_root_version(PgSqlParser.Xml_root_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 */
	void exitXml_root_version(PgSqlParser.Xml_root_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 */
	void enterXml_root_standalone_(PgSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 */
	void exitXml_root_standalone_(PgSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 */
	void enterXml_attributes(PgSqlParser.Xml_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 */
	void exitXml_attributes(PgSqlParser.Xml_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 */
	void enterXml_attribute_list(PgSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 */
	void exitXml_attribute_list(PgSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 */
	void enterXml_attribute_el(PgSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 */
	void exitXml_attribute_el(PgSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 */
	void enterDocument_or_content(PgSqlParser.Document_or_contentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 */
	void exitDocument_or_content(PgSqlParser.Document_or_contentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 */
	void enterXml_whitespace_option(PgSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 */
	void exitXml_whitespace_option(PgSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 */
	void enterXmlexists_argument(PgSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 */
	void exitXmlexists_argument(PgSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 */
	void enterXml_passing_mech(PgSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 */
	void exitXml_passing_mech(PgSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 */
	void enterWithin_group_clause(PgSqlParser.Within_group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 */
	void exitWithin_group_clause(PgSqlParser.Within_group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clause(PgSqlParser.Filter_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clause(PgSqlParser.Filter_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#window_clause}.
	 * @param ctx the parse tree
	 */
	void enterWindow_clause(PgSqlParser.Window_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#window_clause}.
	 * @param ctx the parse tree
	 */
	void exitWindow_clause(PgSqlParser.Window_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 */
	void enterWindow_definition_list(PgSqlParser.Window_definition_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 */
	void exitWindow_definition_list(PgSqlParser.Window_definition_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#window_definition}.
	 * @param ctx the parse tree
	 */
	void enterWindow_definition(PgSqlParser.Window_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#window_definition}.
	 * @param ctx the parse tree
	 */
	void exitWindow_definition(PgSqlParser.Window_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause(PgSqlParser.Over_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause(PgSqlParser.Over_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void enterWindow_specification(PgSqlParser.Window_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void exitWindow_specification(PgSqlParser.Window_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 */
	void enterExisting_window_name_(PgSqlParser.Existing_window_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 */
	void exitExisting_window_name_(PgSqlParser.Existing_window_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 */
	void enterPartition_clause_(PgSqlParser.Partition_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 */
	void exitPartition_clause_(PgSqlParser.Partition_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause_(PgSqlParser.Frame_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause_(PgSqlParser.Frame_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void enterFrame_extent(PgSqlParser.Frame_extentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void exitFrame_extent(PgSqlParser.Frame_extentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 */
	void enterFrame_bound(PgSqlParser.Frame_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 */
	void exitFrame_bound(PgSqlParser.Frame_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 */
	void enterWindow_exclusion_clause_(PgSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 */
	void exitWindow_exclusion_clause_(PgSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(PgSqlParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(PgSqlParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 */
	void enterExplicit_row(PgSqlParser.Explicit_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 */
	void exitExplicit_row(PgSqlParser.Explicit_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 */
	void enterImplicit_row(PgSqlParser.Implicit_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 */
	void exitImplicit_row(PgSqlParser.Implicit_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sub_type}.
	 * @param ctx the parse tree
	 */
	void enterSub_type(PgSqlParser.Sub_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sub_type}.
	 * @param ctx the parse tree
	 */
	void exitSub_type(PgSqlParser.Sub_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#all_op}.
	 * @param ctx the parse tree
	 */
	void enterAll_op(PgSqlParser.All_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#all_op}.
	 * @param ctx the parse tree
	 */
	void exitAll_op(PgSqlParser.All_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#mathop}.
	 * @param ctx the parse tree
	 */
	void enterMathop(PgSqlParser.MathopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#mathop}.
	 * @param ctx the parse tree
	 */
	void exitMathop(PgSqlParser.MathopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#qual_op}.
	 * @param ctx the parse tree
	 */
	void enterQual_op(PgSqlParser.Qual_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#qual_op}.
	 * @param ctx the parse tree
	 */
	void exitQual_op(PgSqlParser.Qual_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 */
	void enterQual_all_op(PgSqlParser.Qual_all_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 */
	void exitQual_all_op(PgSqlParser.Qual_all_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_Op(PgSqlParser.Subquery_OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_Op(PgSqlParser.Subquery_OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list(PgSqlParser.Expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list(PgSqlParser.Expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_list(PgSqlParser.Func_arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_list(PgSqlParser.Func_arg_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_expr(PgSqlParser.Func_arg_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_expr(PgSqlParser.Func_arg_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_list}.
	 * @param ctx the parse tree
	 */
	void enterType_list(PgSqlParser.Type_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_list}.
	 * @param ctx the parse tree
	 */
	void exitType_list(PgSqlParser.Type_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#array_expr}.
	 * @param ctx the parse tree
	 */
	void enterArray_expr(PgSqlParser.Array_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#array_expr}.
	 * @param ctx the parse tree
	 */
	void exitArray_expr(PgSqlParser.Array_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterArray_expr_list(PgSqlParser.Array_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitArray_expr_list(PgSqlParser.Array_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#extract_list}.
	 * @param ctx the parse tree
	 */
	void enterExtract_list(PgSqlParser.Extract_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#extract_list}.
	 * @param ctx the parse tree
	 */
	void exitExtract_list(PgSqlParser.Extract_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 */
	void enterExtract_arg(PgSqlParser.Extract_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 */
	void exitExtract_arg(PgSqlParser.Extract_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 */
	void enterUnicode_normal_form(PgSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 */
	void exitUnicode_normal_form(PgSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 */
	void enterOverlay_list(PgSqlParser.Overlay_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 */
	void exitOverlay_list(PgSqlParser.Overlay_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#position_list}.
	 * @param ctx the parse tree
	 */
	void enterPosition_list(PgSqlParser.Position_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#position_list}.
	 * @param ctx the parse tree
	 */
	void exitPosition_list(PgSqlParser.Position_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#substr_list}.
	 * @param ctx the parse tree
	 */
	void enterSubstr_list(PgSqlParser.Substr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#substr_list}.
	 * @param ctx the parse tree
	 */
	void exitSubstr_list(PgSqlParser.Substr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#trim_list}.
	 * @param ctx the parse tree
	 */
	void enterTrim_list(PgSqlParser.Trim_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#trim_list}.
	 * @param ctx the parse tree
	 */
	void exitTrim_list(PgSqlParser.Trim_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void enterIn_expr_select(PgSqlParser.In_expr_selectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void exitIn_expr_select(PgSqlParser.In_expr_selectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void enterIn_expr_list(PgSqlParser.In_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void exitIn_expr_list(PgSqlParser.In_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#case_expr}.
	 * @param ctx the parse tree
	 */
	void enterCase_expr(PgSqlParser.Case_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#case_expr}.
	 * @param ctx the parse tree
	 */
	void exitCase_expr(PgSqlParser.Case_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 */
	void enterWhen_clause_list(PgSqlParser.When_clause_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 */
	void exitWhen_clause_list(PgSqlParser.When_clause_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#when_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhen_clause(PgSqlParser.When_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#when_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhen_clause(PgSqlParser.When_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#case_default}.
	 * @param ctx the parse tree
	 */
	void enterCase_default(PgSqlParser.Case_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#case_default}.
	 * @param ctx the parse tree
	 */
	void exitCase_default(PgSqlParser.Case_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#case_arg}.
	 * @param ctx the parse tree
	 */
	void enterCase_arg(PgSqlParser.Case_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#case_arg}.
	 * @param ctx the parse tree
	 */
	void exitCase_arg(PgSqlParser.Case_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void enterColumnref(PgSqlParser.ColumnrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void exitColumnref(PgSqlParser.ColumnrefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 */
	void enterIndirection_el(PgSqlParser.Indirection_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 */
	void exitIndirection_el(PgSqlParser.Indirection_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 */
	void enterSlice_bound_(PgSqlParser.Slice_bound_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 */
	void exitSlice_bound_(PgSqlParser.Slice_bound_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#indirection}.
	 * @param ctx the parse tree
	 */
	void enterIndirection(PgSqlParser.IndirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#indirection}.
	 * @param ctx the parse tree
	 */
	void exitIndirection(PgSqlParser.IndirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 */
	void enterOpt_indirection(PgSqlParser.Opt_indirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 */
	void exitOpt_indirection(PgSqlParser.Opt_indirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_passing_clause(PgSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_passing_clause(PgSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJson_arguments(PgSqlParser.Json_argumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJson_arguments(PgSqlParser.Json_argumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_argument}.
	 * @param ctx the parse tree
	 */
	void enterJson_argument(PgSqlParser.Json_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_argument}.
	 * @param ctx the parse tree
	 */
	void exitJson_argument(PgSqlParser.Json_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 */
	void enterJson_wrapper_behavior(PgSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 */
	void exitJson_wrapper_behavior(PgSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior(PgSqlParser.Json_behaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior(PgSqlParser.Json_behaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior_type(PgSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior_type(PgSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior_clause(PgSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior_clause(PgSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_on_error_clause(PgSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_on_error_clause(PgSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_expr(PgSqlParser.Json_value_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_expr(PgSqlParser.Json_value_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_format_clause(PgSqlParser.Json_format_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_format_clause(PgSqlParser.Json_format_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_quotes_clause(PgSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_quotes_clause(PgSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_returning_clause(PgSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_returning_clause(PgSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJson_predicate_type_constraint(PgSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJson_predicate_type_constraint(PgSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJson_key_uniqueness_constraint(PgSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJson_key_uniqueness_constraint(PgSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 */
	void enterJson_name_and_value_list(PgSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 */
	void exitJson_name_and_value_list(PgSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 */
	void enterJson_name_and_value(PgSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 */
	void exitJson_name_and_value(PgSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_constructor_null_clause(PgSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_constructor_null_clause(PgSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_constructor_null_clause(PgSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_constructor_null_clause(PgSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_expr_list(PgSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_expr_list(PgSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 */
	void enterJson_aggregate_func(PgSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 */
	void exitJson_aggregate_func(PgSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_aggregate_order_by_clause(PgSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_aggregate_order_by_clause(PgSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#target_list_}.
	 * @param ctx the parse tree
	 */
	void enterTarget_list_(PgSqlParser.Target_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#target_list_}.
	 * @param ctx the parse tree
	 */
	void exitTarget_list_(PgSqlParser.Target_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#target_list}.
	 * @param ctx the parse tree
	 */
	void enterTarget_list(PgSqlParser.Target_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#target_list}.
	 * @param ctx the parse tree
	 */
	void exitTarget_list(PgSqlParser.Target_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#target_el}.
	 * @param ctx the parse tree
	 */
	void enterTarget_el(PgSqlParser.Target_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#target_el}.
	 * @param ctx the parse tree
	 */
	void exitTarget_el(PgSqlParser.Target_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#target_alias}.
	 * @param ctx the parse tree
	 */
	void enterTarget_alias(PgSqlParser.Target_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#target_alias}.
	 * @param ctx the parse tree
	 */
	void exitTarget_alias(PgSqlParser.Target_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 */
	void enterQualified_name_list(PgSqlParser.Qualified_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 */
	void exitQualified_name_list(PgSqlParser.Qualified_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_name(PgSqlParser.Qualified_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_name(PgSqlParser.Qualified_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#name_list}.
	 * @param ctx the parse tree
	 */
	void enterName_list(PgSqlParser.Name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#name_list}.
	 * @param ctx the parse tree
	 */
	void exitName_list(PgSqlParser.Name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(PgSqlParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(PgSqlParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void enterAttr_name(PgSqlParser.Attr_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void exitAttr_name(PgSqlParser.Attr_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#file_name}.
	 * @param ctx the parse tree
	 */
	void enterFile_name(PgSqlParser.File_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#file_name}.
	 * @param ctx the parse tree
	 */
	void exitFile_name(PgSqlParser.File_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#func_name}.
	 * @param ctx the parse tree
	 */
	void enterFunc_name(PgSqlParser.Func_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#func_name}.
	 * @param ctx the parse tree
	 */
	void exitFunc_name(PgSqlParser.Func_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 */
	void enterAexprconst(PgSqlParser.AexprconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 */
	void exitAexprconst(PgSqlParser.AexprconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#xconst}.
	 * @param ctx the parse tree
	 */
	void enterXconst(PgSqlParser.XconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#xconst}.
	 * @param ctx the parse tree
	 */
	void exitXconst(PgSqlParser.XconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bconst}.
	 * @param ctx the parse tree
	 */
	void enterBconst(PgSqlParser.BconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bconst}.
	 * @param ctx the parse tree
	 */
	void exitBconst(PgSqlParser.BconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#fconst}.
	 * @param ctx the parse tree
	 */
	void enterFconst(PgSqlParser.FconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#fconst}.
	 * @param ctx the parse tree
	 */
	void exitFconst(PgSqlParser.FconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#iconst}.
	 * @param ctx the parse tree
	 */
	void enterIconst(PgSqlParser.IconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#iconst}.
	 * @param ctx the parse tree
	 */
	void exitIconst(PgSqlParser.IconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#sconst}.
	 * @param ctx the parse tree
	 */
	void enterSconst(PgSqlParser.SconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#sconst}.
	 * @param ctx the parse tree
	 */
	void exitSconst(PgSqlParser.SconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#anysconst}.
	 * @param ctx the parse tree
	 */
	void enterAnysconst(PgSqlParser.AnysconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#anysconst}.
	 * @param ctx the parse tree
	 */
	void exitAnysconst(PgSqlParser.AnysconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#uescape_}.
	 * @param ctx the parse tree
	 */
	void enterUescape_(PgSqlParser.Uescape_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#uescape_}.
	 * @param ctx the parse tree
	 */
	void exitUescape_(PgSqlParser.Uescape_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#signediconst}.
	 * @param ctx the parse tree
	 */
	void enterSignediconst(PgSqlParser.SignediconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#signediconst}.
	 * @param ctx the parse tree
	 */
	void exitSignediconst(PgSqlParser.SignediconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#roleid}.
	 * @param ctx the parse tree
	 */
	void enterRoleid(PgSqlParser.RoleidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#roleid}.
	 * @param ctx the parse tree
	 */
	void exitRoleid(PgSqlParser.RoleidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#rolespec}.
	 * @param ctx the parse tree
	 */
	void enterRolespec(PgSqlParser.RolespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#rolespec}.
	 * @param ctx the parse tree
	 */
	void exitRolespec(PgSqlParser.RolespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#role_list}.
	 * @param ctx the parse tree
	 */
	void enterRole_list(PgSqlParser.Role_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#role_list}.
	 * @param ctx the parse tree
	 */
	void exitRole_list(PgSqlParser.Role_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#colid}.
	 * @param ctx the parse tree
	 */
	void enterColid(PgSqlParser.ColidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#colid}.
	 * @param ctx the parse tree
	 */
	void exitColid(PgSqlParser.ColidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 */
	void enterType_function_name(PgSqlParser.Type_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 */
	void exitType_function_name(PgSqlParser.Type_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 */
	void enterNonreservedword(PgSqlParser.NonreservedwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 */
	void exitNonreservedword(PgSqlParser.NonreservedwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#colLabel}.
	 * @param ctx the parse tree
	 */
	void enterColLabel(PgSqlParser.ColLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#colLabel}.
	 * @param ctx the parse tree
	 */
	void exitColLabel(PgSqlParser.ColLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 */
	void enterBareColLabel(PgSqlParser.BareColLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 */
	void exitBareColLabel(PgSqlParser.BareColLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 */
	void enterUnreserved_keyword(PgSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 */
	void exitUnreserved_keyword(PgSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 */
	void enterCol_name_keyword(PgSqlParser.Col_name_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 */
	void exitCol_name_keyword(PgSqlParser.Col_name_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 */
	void enterType_func_name_keyword(PgSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 */
	void exitType_func_name_keyword(PgSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 */
	void enterReserved_keyword(PgSqlParser.Reserved_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 */
	void exitReserved_keyword(PgSqlParser.Reserved_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 */
	void enterBare_label_keyword(PgSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 */
	void exitBare_label_keyword(PgSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAny_identifier(PgSqlParser.Any_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAny_identifier(PgSqlParser.Any_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PgSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(PgSqlParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PgSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(PgSqlParser.IdentifierContext ctx);
}