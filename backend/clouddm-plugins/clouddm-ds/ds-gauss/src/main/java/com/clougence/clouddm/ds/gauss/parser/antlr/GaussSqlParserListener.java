// Generated from GaussSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.gauss.parser.antlr;

    import com.clougence.clouddm.ds.gauss.parser.base.GaussSqlParserBase;


import com.clougence.clouddm.dsfamily.postgres.parser.base.PgSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GaussSqlParser}.
 */
public interface GaussSqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunc_opt_item(GaussSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunc_opt_item(GaussSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionspec(GaussSqlParser.PartitionspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionspec(GaussSqlParser.PartitionspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void enterUnion_context(GaussSqlParser.Union_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void exitUnion_context(GaussSqlParser.Union_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(GaussSqlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(GaussSqlParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 */
	void enterStmtblock(GaussSqlParser.StmtblockContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 */
	void exitStmtblock(GaussSqlParser.StmtblockContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 */
	void enterStmtmulti(GaussSqlParser.StmtmultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 */
	void exitStmtmulti(GaussSqlParser.StmtmultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(GaussSqlParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(GaussSqlParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void enterCallstmt(GaussSqlParser.CallstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void exitCallstmt(GaussSqlParser.CallstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreaterolestmt(GaussSqlParser.CreaterolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreaterolestmt(GaussSqlParser.CreaterolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#with_}.
	 * @param ctx the parse tree
	 */
	void enterWith_(GaussSqlParser.With_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#with_}.
	 * @param ctx the parse tree
	 */
	void exitWith_(GaussSqlParser.With_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 */
	void enterOptrolelist(GaussSqlParser.OptrolelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 */
	void exitOptrolelist(GaussSqlParser.OptrolelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 */
	void enterAlteroptrolelist(GaussSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 */
	void exitAlteroptrolelist(GaussSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 */
	void enterAlteroptroleelem(GaussSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 */
	void exitAlteroptroleelem(GaussSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 */
	void enterCreateoptroleelem(GaussSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 */
	void exitCreateoptroleelem(GaussSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateuserstmt(GaussSqlParser.CreateuserstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateuserstmt(GaussSqlParser.CreateuserstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterrolestmt(GaussSqlParser.AlterrolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterrolestmt(GaussSqlParser.AlterrolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#in_database_}.
	 * @param ctx the parse tree
	 */
	void enterIn_database_(GaussSqlParser.In_database_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#in_database_}.
	 * @param ctx the parse tree
	 */
	void exitIn_database_(GaussSqlParser.In_database_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterrolesetstmt(GaussSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterrolesetstmt(GaussSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroprolestmt(GaussSqlParser.DroprolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroprolestmt(GaussSqlParser.DroprolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropuserstmt(GaussSqlParser.DropuserstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropuserstmt(GaussSqlParser.DropuserstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreategroupstmt(GaussSqlParser.CreategroupstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreategroupstmt(GaussSqlParser.CreategroupstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltergroupstmt(GaussSqlParser.AltergroupstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltergroupstmt(GaussSqlParser.AltergroupstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#add_drop}.
	 * @param ctx the parse tree
	 */
	void enterAdd_drop(GaussSqlParser.Add_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#add_drop}.
	 * @param ctx the parse tree
	 */
	void exitAdd_drop(GaussSqlParser.Add_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateschemastmt(GaussSqlParser.CreateschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateschemastmt(GaussSqlParser.CreateschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 */
	void enterOptschemaname(GaussSqlParser.OptschemanameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 */
	void exitOptschemaname(GaussSqlParser.OptschemanameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 */
	void enterOptschemaeltlist(GaussSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 */
	void exitOptschemaeltlist(GaussSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSchema_stmt(GaussSqlParser.Schema_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSchema_stmt(GaussSqlParser.Schema_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariablesetstmt(GaussSqlParser.VariablesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariablesetstmt(GaussSqlParser.VariablesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_rest}.
	 * @param ctx the parse tree
	 */
	void enterSet_rest(GaussSqlParser.Set_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_rest}.
	 * @param ctx the parse tree
	 */
	void exitSet_rest(GaussSqlParser.Set_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_set}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_set(GaussSqlParser.Generic_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_set}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_set(GaussSqlParser.Generic_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 */
	void enterSet_rest_more(GaussSqlParser.Set_rest_moreContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 */
	void exitSet_rest_more(GaussSqlParser.Set_rest_moreContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#var_name}.
	 * @param ctx the parse tree
	 */
	void enterVar_name(GaussSqlParser.Var_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#var_name}.
	 * @param ctx the parse tree
	 */
	void exitVar_name(GaussSqlParser.Var_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#var_list}.
	 * @param ctx the parse tree
	 */
	void enterVar_list(GaussSqlParser.Var_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#var_list}.
	 * @param ctx the parse tree
	 */
	void exitVar_list(GaussSqlParser.Var_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#var_value}.
	 * @param ctx the parse tree
	 */
	void enterVar_value(GaussSqlParser.Var_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#var_value}.
	 * @param ctx the parse tree
	 */
	void exitVar_value(GaussSqlParser.Var_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#iso_level}.
	 * @param ctx the parse tree
	 */
	void enterIso_level(GaussSqlParser.Iso_levelContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#iso_level}.
	 * @param ctx the parse tree
	 */
	void exitIso_level(GaussSqlParser.Iso_levelContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_or_string_(GaussSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_or_string_(GaussSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#zone_value}.
	 * @param ctx the parse tree
	 */
	void enterZone_value(GaussSqlParser.Zone_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#zone_value}.
	 * @param ctx the parse tree
	 */
	void exitZone_value(GaussSqlParser.Zone_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#encoding_}.
	 * @param ctx the parse tree
	 */
	void enterEncoding_(GaussSqlParser.Encoding_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#encoding_}.
	 * @param ctx the parse tree
	 */
	void exitEncoding_(GaussSqlParser.Encoding_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 */
	void enterNonreservedword_or_sconst(GaussSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 */
	void exitNonreservedword_or_sconst(GaussSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariableresetstmt(GaussSqlParser.VariableresetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariableresetstmt(GaussSqlParser.VariableresetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 */
	void enterReset_rest(GaussSqlParser.Reset_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 */
	void exitReset_rest(GaussSqlParser.Reset_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_reset(GaussSqlParser.Generic_resetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_reset(GaussSqlParser.Generic_resetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 */
	void enterSetresetclause(GaussSqlParser.SetresetclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 */
	void exitSetresetclause(GaussSqlParser.SetresetclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 */
	void enterFunctionsetresetclause(GaussSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 */
	void exitFunctionsetresetclause(GaussSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 */
	void enterVariableshowstmt(GaussSqlParser.VariableshowstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 */
	void exitVariableshowstmt(GaussSqlParser.VariableshowstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 */
	void enterConstraintssetstmt(GaussSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 */
	void exitConstraintssetstmt(GaussSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 */
	void enterConstraints_set_list(GaussSqlParser.Constraints_set_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 */
	void exitConstraints_set_list(GaussSqlParser.Constraints_set_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 */
	void enterConstraints_set_mode(GaussSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 */
	void exitConstraints_set_mode(GaussSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 */
	void enterCheckpointstmt(GaussSqlParser.CheckpointstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 */
	void exitCheckpointstmt(GaussSqlParser.CheckpointstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 */
	void enterDiscardstmt(GaussSqlParser.DiscardstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 */
	void exitDiscardstmt(GaussSqlParser.DiscardstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertablestmt(GaussSqlParser.AltertablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertablestmt(GaussSqlParser.AltertablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_cmds(GaussSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_cmds(GaussSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 */
	void enterPartition_cmd(GaussSqlParser.Partition_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 */
	void exitPartition_cmd(GaussSqlParser.Partition_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partition_cmd(GaussSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partition_cmd(GaussSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAddColumn(GaussSqlParser.AddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAddColumn(GaussSqlParser.AddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlterColumn(GaussSqlParser.AlterColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlterColumn(GaussSqlParser.AlterColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterDropColumn(GaussSqlParser.DropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitDropColumn(GaussSqlParser.DropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAddConstraint(GaussSqlParser.AddConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAddConstraint(GaussSqlParser.AddConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlterConstaint(GaussSqlParser.AlterConstaintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlterConstaint(GaussSqlParser.AlterConstaintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterValidateConstraint(GaussSqlParser.ValidateConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitValidateConstraint(GaussSqlParser.ValidateConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterDropConstraint(GaussSqlParser.DropConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitDropConstraint(GaussSqlParser.DropConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterSetWithoutOids(GaussSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitSetWithoutOids(GaussSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterClusterOnName(GaussSqlParser.ClusterOnNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitClusterOnName(GaussSqlParser.ClusterOnNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterSetWithOut(GaussSqlParser.SetWithOutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitSetWithOut(GaussSqlParser.SetWithOutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void enterUnsupportAlterTableStatement(GaussSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 */
	void exitUnsupportAlterTableStatement(GaussSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 */
	void enterAlter_column_default(GaussSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 */
	void exitAlter_column_default(GaussSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 */
	void enterDrop_behavior_(GaussSqlParser.Drop_behavior_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 */
	void exitDrop_behavior_(GaussSqlParser.Drop_behavior_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 */
	void enterCollate_clause_(GaussSqlParser.Collate_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 */
	void exitCollate_clause_(GaussSqlParser.Collate_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_using}.
	 * @param ctx the parse tree
	 */
	void enterAlter_using(GaussSqlParser.Alter_usingContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_using}.
	 * @param ctx the parse tree
	 */
	void exitAlter_using(GaussSqlParser.Alter_usingContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 */
	void enterReplica_identity(GaussSqlParser.Replica_identityContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 */
	void exitReplica_identity(GaussSqlParser.Replica_identityContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reloptions}.
	 * @param ctx the parse tree
	 */
	void enterReloptions(GaussSqlParser.ReloptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reloptions}.
	 * @param ctx the parse tree
	 */
	void exitReloptions(GaussSqlParser.ReloptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 */
	void enterReloptions_(GaussSqlParser.Reloptions_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 */
	void exitReloptions_(GaussSqlParser.Reloptions_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 */
	void enterReloption_list(GaussSqlParser.Reloption_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 */
	void exitReloption_list(GaussSqlParser.Reloption_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 */
	void enterReloption_elem(GaussSqlParser.Reloption_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 */
	void exitReloption_elem(GaussSqlParser.Reloption_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identity_column_option_list(GaussSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identity_column_option_list(GaussSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identity_column_option(GaussSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identity_column_option(GaussSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionboundspec(GaussSqlParser.PartitionboundspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionboundspec(GaussSqlParser.PartitionboundspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 */
	void enterHash_partbound_elem(GaussSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 */
	void exitHash_partbound_elem(GaussSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 */
	void enterHash_partbound(GaussSqlParser.Hash_partboundContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 */
	void exitHash_partbound(GaussSqlParser.Hash_partboundContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltercompositetypestmt(GaussSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltercompositetypestmt(GaussSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type_cmds(GaussSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type_cmds(GaussSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type_cmd(GaussSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type_cmd(GaussSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 */
	void enterCloseportalstmt(GaussSqlParser.CloseportalstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 */
	void exitCloseportalstmt(GaussSqlParser.CloseportalstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copystmt}.
	 * @param ctx the parse tree
	 */
	void enterCopystmt(GaussSqlParser.CopystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copystmt}.
	 * @param ctx the parse tree
	 */
	void exitCopystmt(GaussSqlParser.CopystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void enterCopy_from(GaussSqlParser.Copy_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void exitCopy_from(GaussSqlParser.Copy_fromContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#program_}.
	 * @param ctx the parse tree
	 */
	void enterProgram_(GaussSqlParser.Program_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#program_}.
	 * @param ctx the parse tree
	 */
	void exitProgram_(GaussSqlParser.Program_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 */
	void enterCopy_file_name(GaussSqlParser.Copy_file_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 */
	void exitCopy_file_name(GaussSqlParser.Copy_file_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_options}.
	 * @param ctx the parse tree
	 */
	void enterCopy_options(GaussSqlParser.Copy_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_options}.
	 * @param ctx the parse tree
	 */
	void exitCopy_options(GaussSqlParser.Copy_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_opt_list(GaussSqlParser.Copy_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_opt_list(GaussSqlParser.Copy_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCopy_opt_item(GaussSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCopy_opt_item(GaussSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#binary_}.
	 * @param ctx the parse tree
	 */
	void enterBinary_(GaussSqlParser.Binary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#binary_}.
	 * @param ctx the parse tree
	 */
	void exitBinary_(GaussSqlParser.Binary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 */
	void enterCopy_delimiter(GaussSqlParser.Copy_delimiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 */
	void exitCopy_delimiter(GaussSqlParser.Copy_delimiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#using_}.
	 * @param ctx the parse tree
	 */
	void enterUsing_(GaussSqlParser.Using_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#using_}.
	 * @param ctx the parse tree
	 */
	void exitUsing_(GaussSqlParser.Using_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_list(GaussSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_list(GaussSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_elem(GaussSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_elem(GaussSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg(GaussSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg(GaussSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg_list(GaussSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg_list(GaussSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 */
	void enterCopy_generic_opt_arg_list_item(GaussSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 */
	void exitCopy_generic_opt_arg_list_item(GaussSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatestmt(GaussSqlParser.CreatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatestmt(GaussSqlParser.CreatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttemp}.
	 * @param ctx the parse tree
	 */
	void enterOpttemp(GaussSqlParser.OpttempContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttemp}.
	 * @param ctx the parse tree
	 */
	void exitOpttemp(GaussSqlParser.OpttempContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttableelementlist(GaussSqlParser.OpttableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttableelementlist(GaussSqlParser.OpttableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttypedtableelementlist(GaussSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttypedtableelementlist(GaussSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTableelementlist(GaussSqlParser.TableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTableelementlist(GaussSqlParser.TableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTypedtableelementlist(GaussSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTypedtableelementlist(GaussSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tableelement}.
	 * @param ctx the parse tree
	 */
	void enterTableelement(GaussSqlParser.TableelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tableelement}.
	 * @param ctx the parse tree
	 */
	void exitTableelement(GaussSqlParser.TableelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 */
	void enterTypedtableelement(GaussSqlParser.TypedtableelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 */
	void exitTypedtableelement(GaussSqlParser.TypedtableelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void enterColumnDef(GaussSqlParser.ColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void exitColumnDef(GaussSqlParser.ColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 */
	void enterColumnOptions(GaussSqlParser.ColumnOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 */
	void exitColumnOptions(GaussSqlParser.ColumnOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#colquallist}.
	 * @param ctx the parse tree
	 */
	void enterColquallist(GaussSqlParser.ColquallistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#colquallist}.
	 * @param ctx the parse tree
	 */
	void exitColquallist(GaussSqlParser.ColquallistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 */
	void enterColconstraint(GaussSqlParser.ColconstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 */
	void exitColconstraint(GaussSqlParser.ColconstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintNotNull(GaussSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintNotNull(GaussSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintNull(GaussSqlParser.ConstraintNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintNull(GaussSqlParser.ConstraintNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintUnique(GaussSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintUnique(GaussSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintPrimary(GaussSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintPrimary(GaussSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintCheck(GaussSqlParser.ConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintCheck(GaussSqlParser.ConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDefault(GaussSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDefault(GaussSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintGenerated(GaussSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintGenerated(GaussSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintReference(GaussSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintReference(GaussSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generated_when}.
	 * @param ctx the parse tree
	 */
	void enterGenerated_when(GaussSqlParser.Generated_whenContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generated_when}.
	 * @param ctx the parse tree
	 */
	void exitGenerated_when(GaussSqlParser.Generated_whenContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattr(GaussSqlParser.ConstraintattrContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattr(GaussSqlParser.ConstraintattrContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeclause(GaussSqlParser.TablelikeclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeclause(GaussSqlParser.TablelikeclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeoptionlist(GaussSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeoptionlist(GaussSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 */
	void enterTablelikeoption(GaussSqlParser.TablelikeoptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 */
	void exitTablelikeoption(GaussSqlParser.TablelikeoptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 */
	void enterTableconstraint(GaussSqlParser.TableconstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 */
	void exitTableconstraint(GaussSqlParser.TableconstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintelem(GaussSqlParser.ConstraintelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintelem(GaussSqlParser.ConstraintelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 */
	void enterNo_inherit_(GaussSqlParser.No_inherit_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 */
	void exitNo_inherit_(GaussSqlParser.No_inherit_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#column_list_}.
	 * @param ctx the parse tree
	 */
	void enterColumn_list_(GaussSqlParser.Column_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#column_list_}.
	 * @param ctx the parse tree
	 */
	void exitColumn_list_(GaussSqlParser.Column_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#columnlist}.
	 * @param ctx the parse tree
	 */
	void enterColumnlist(GaussSqlParser.ColumnlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#columnlist}.
	 * @param ctx the parse tree
	 */
	void exitColumnlist(GaussSqlParser.ColumnlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#columnElem}.
	 * @param ctx the parse tree
	 */
	void enterColumnElem(GaussSqlParser.ColumnElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#columnElem}.
	 * @param ctx the parse tree
	 */
	void exitColumnElem(GaussSqlParser.ColumnElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#c_include_}.
	 * @param ctx the parse tree
	 */
	void enterC_include_(GaussSqlParser.C_include_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#c_include_}.
	 * @param ctx the parse tree
	 */
	void exitC_include_(GaussSqlParser.C_include_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#key_match}.
	 * @param ctx the parse tree
	 */
	void enterKey_match(GaussSqlParser.Key_matchContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#key_match}.
	 * @param ctx the parse tree
	 */
	void exitKey_match(GaussSqlParser.Key_matchContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 */
	void enterExclusionconstraintlist(GaussSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 */
	void exitExclusionconstraintlist(GaussSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 */
	void enterExclusionconstraintelem(GaussSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 */
	void exitExclusionconstraintelem(GaussSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 */
	void enterExclusionwhereclause(GaussSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 */
	void exitExclusionwhereclause(GaussSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#key_actions}.
	 * @param ctx the parse tree
	 */
	void enterKey_actions(GaussSqlParser.Key_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#key_actions}.
	 * @param ctx the parse tree
	 */
	void exitKey_actions(GaussSqlParser.Key_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#key_update}.
	 * @param ctx the parse tree
	 */
	void enterKey_update(GaussSqlParser.Key_updateContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#key_update}.
	 * @param ctx the parse tree
	 */
	void exitKey_update(GaussSqlParser.Key_updateContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#key_delete}.
	 * @param ctx the parse tree
	 */
	void enterKey_delete(GaussSqlParser.Key_deleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#key_delete}.
	 * @param ctx the parse tree
	 */
	void exitKey_delete(GaussSqlParser.Key_deleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#key_action}.
	 * @param ctx the parse tree
	 */
	void enterKey_action(GaussSqlParser.Key_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#key_action}.
	 * @param ctx the parse tree
	 */
	void exitKey_action(GaussSqlParser.Key_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optinherit}.
	 * @param ctx the parse tree
	 */
	void enterOptinherit(GaussSqlParser.OptinheritContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optinherit}.
	 * @param ctx the parse tree
	 */
	void exitOptinherit(GaussSqlParser.OptinheritContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 */
	void enterOptpartitionspec(GaussSqlParser.OptpartitionspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 */
	void exitOptpartitionspec(GaussSqlParser.OptpartitionspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#partition_item}.
	 * @param ctx the parse tree
	 */
	void enterPartition_item(GaussSqlParser.Partition_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#partition_item}.
	 * @param ctx the parse tree
	 */
	void exitPartition_item(GaussSqlParser.Partition_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#part_params}.
	 * @param ctx the parse tree
	 */
	void enterPart_params(GaussSqlParser.Part_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#part_params}.
	 * @param ctx the parse tree
	 */
	void exitPart_params(GaussSqlParser.Part_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#part_elem}.
	 * @param ctx the parse tree
	 */
	void enterPart_elem(GaussSqlParser.Part_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#part_elem}.
	 * @param ctx the parse tree
	 */
	void exitPart_elem(GaussSqlParser.Part_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 */
	void enterTable_access_method_clause(GaussSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 */
	void exitTable_access_method_clause(GaussSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optwith}.
	 * @param ctx the parse tree
	 */
	void enterOptwith(GaussSqlParser.OptwithContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optwith}.
	 * @param ctx the parse tree
	 */
	void exitOptwith(GaussSqlParser.OptwithContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 */
	void enterOncommitoption(GaussSqlParser.OncommitoptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 */
	void exitOncommitoption(GaussSqlParser.OncommitoptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 */
	void enterOpttablespace(GaussSqlParser.OpttablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 */
	void exitOpttablespace(GaussSqlParser.OpttablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 */
	void enterOptconstablespace(GaussSqlParser.OptconstablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 */
	void exitOptconstablespace(GaussSqlParser.OptconstablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#existingindex}.
	 * @param ctx the parse tree
	 */
	void enterExistingindex(GaussSqlParser.ExistingindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#existingindex}.
	 * @param ctx the parse tree
	 */
	void exitExistingindex(GaussSqlParser.ExistingindexContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatestatsstmt(GaussSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatestatsstmt(GaussSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterstatsstmt(GaussSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterstatsstmt(GaussSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateasstmt(GaussSqlParser.CreateasstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateasstmt(GaussSqlParser.CreateasstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 */
	void enterCreate_as_target(GaussSqlParser.Create_as_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 */
	void exitCreate_as_target(GaussSqlParser.Create_as_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#with_data_}.
	 * @param ctx the parse tree
	 */
	void enterWith_data_(GaussSqlParser.With_data_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#with_data_}.
	 * @param ctx the parse tree
	 */
	void exitWith_data_(GaussSqlParser.With_data_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatematviewstmt(GaussSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatematviewstmt(GaussSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 */
	void enterCreate_mv_target(GaussSqlParser.Create_mv_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 */
	void exitCreate_mv_target(GaussSqlParser.Create_mv_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optnolog}.
	 * @param ctx the parse tree
	 */
	void enterOptnolog(GaussSqlParser.OptnologContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optnolog}.
	 * @param ctx the parse tree
	 */
	void exitOptnolog(GaussSqlParser.OptnologContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 */
	void enterRefreshmatviewstmt(GaussSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 */
	void exitRefreshmatviewstmt(GaussSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateseqstmt(GaussSqlParser.CreateseqstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateseqstmt(GaussSqlParser.CreateseqstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterseqstmt(GaussSqlParser.AlterseqstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterseqstmt(GaussSqlParser.AlterseqstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterOptseqoptlist(GaussSqlParser.OptseqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitOptseqoptlist(GaussSqlParser.OptseqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterOptparenthesizedseqoptlist(GaussSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitOptparenthesizedseqoptlist(GaussSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 */
	void enterSeqoptlist(GaussSqlParser.SeqoptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 */
	void exitSeqoptlist(GaussSqlParser.SeqoptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 */
	void enterSeqoptelem(GaussSqlParser.SeqoptelemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 */
	void exitSeqoptelem(GaussSqlParser.SeqoptelemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#by_}.
	 * @param ctx the parse tree
	 */
	void enterBy_(GaussSqlParser.By_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#by_}.
	 * @param ctx the parse tree
	 */
	void exitBy_(GaussSqlParser.By_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#numericonly}.
	 * @param ctx the parse tree
	 */
	void enterNumericonly(GaussSqlParser.NumericonlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#numericonly}.
	 * @param ctx the parse tree
	 */
	void exitNumericonly(GaussSqlParser.NumericonlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 */
	void enterNumericonly_list(GaussSqlParser.Numericonly_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 */
	void exitNumericonly_list(GaussSqlParser.Numericonly_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateplangstmt(GaussSqlParser.CreateplangstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateplangstmt(GaussSqlParser.CreateplangstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#trusted_}.
	 * @param ctx the parse tree
	 */
	void enterTrusted_(GaussSqlParser.Trusted_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#trusted_}.
	 * @param ctx the parse tree
	 */
	void exitTrusted_(GaussSqlParser.Trusted_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#handler_name}.
	 * @param ctx the parse tree
	 */
	void enterHandler_name(GaussSqlParser.Handler_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#handler_name}.
	 * @param ctx the parse tree
	 */
	void exitHandler_name(GaussSqlParser.Handler_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 */
	void enterInline_handler_(GaussSqlParser.Inline_handler_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 */
	void exitInline_handler_(GaussSqlParser.Inline_handler_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 */
	void enterValidator_clause(GaussSqlParser.Validator_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 */
	void exitValidator_clause(GaussSqlParser.Validator_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#validator_}.
	 * @param ctx the parse tree
	 */
	void enterValidator_(GaussSqlParser.Validator_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#validator_}.
	 * @param ctx the parse tree
	 */
	void exitValidator_(GaussSqlParser.Validator_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#procedural_}.
	 * @param ctx the parse tree
	 */
	void enterProcedural_(GaussSqlParser.Procedural_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#procedural_}.
	 * @param ctx the parse tree
	 */
	void exitProcedural_(GaussSqlParser.Procedural_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetablespacestmt(GaussSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetablespacestmt(GaussSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 */
	void enterOpttablespaceowner(GaussSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 */
	void exitOpttablespaceowner(GaussSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptablespacestmt(GaussSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptablespacestmt(GaussSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateextensionstmt(GaussSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateextensionstmt(GaussSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreate_extension_opt_list(GaussSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreate_extension_opt_list(GaussSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreate_extension_opt_item(GaussSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreate_extension_opt_item(GaussSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterextensionstmt(GaussSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterextensionstmt(GaussSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_extension_opt_list(GaussSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_extension_opt_list(GaussSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterAlter_extension_opt_item(GaussSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitAlter_extension_opt_item(GaussSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterextensioncontentsstmt(GaussSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterextensioncontentsstmt(GaussSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatefdwstmt(GaussSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatefdwstmt(GaussSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 */
	void enterFdw_option(GaussSqlParser.Fdw_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 */
	void exitFdw_option(GaussSqlParser.Fdw_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 */
	void enterFdw_options(GaussSqlParser.Fdw_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 */
	void exitFdw_options(GaussSqlParser.Fdw_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 */
	void enterFdw_options_(GaussSqlParser.Fdw_options_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 */
	void exitFdw_options_(GaussSqlParser.Fdw_options_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterfdwstmt(GaussSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterfdwstmt(GaussSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 */
	void enterCreate_generic_options(GaussSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 */
	void exitCreate_generic_options(GaussSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_list(GaussSqlParser.Generic_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_list(GaussSqlParser.Generic_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_options(GaussSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_options(GaussSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_option_list(GaussSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_option_list(GaussSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterAlter_generic_option_elem(GaussSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitAlter_generic_option_elem(GaussSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_elem(GaussSqlParser.Generic_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_elem(GaussSqlParser.Generic_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_name(GaussSqlParser.Generic_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_name(GaussSqlParser.Generic_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterGeneric_option_arg(GaussSqlParser.Generic_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitGeneric_option_arg(GaussSqlParser.Generic_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateforeignserverstmt(GaussSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateforeignserverstmt(GaussSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(GaussSqlParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(GaussSqlParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 */
	void enterForeign_server_version(GaussSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 */
	void exitForeign_server_version(GaussSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 */
	void enterForeign_server_version_(GaussSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 */
	void exitForeign_server_version_(GaussSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterforeignserverstmt(GaussSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterforeignserverstmt(GaussSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateforeigntablestmt(GaussSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateforeigntablestmt(GaussSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterImportforeignschemastmt(GaussSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitImportforeignschemastmt(GaussSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 */
	void enterImport_qualification_type(GaussSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 */
	void exitImport_qualification_type(GaussSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 */
	void enterImport_qualification(GaussSqlParser.Import_qualificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 */
	void exitImport_qualification(GaussSqlParser.Import_qualificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateusermappingstmt(GaussSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateusermappingstmt(GaussSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 */
	void enterAuth_ident(GaussSqlParser.Auth_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 */
	void exitAuth_ident(GaussSqlParser.Auth_identContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropusermappingstmt(GaussSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropusermappingstmt(GaussSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterusermappingstmt(GaussSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterusermappingstmt(GaussSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatepolicystmt(GaussSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatepolicystmt(GaussSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterpolicystmt(GaussSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterpolicystmt(GaussSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionalexpr(GaussSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionalexpr(GaussSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionalwithcheck(GaussSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionalwithcheck(GaussSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaulttorole(GaussSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaulttorole(GaussSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 */
	void enterRowsecurityoptionaltorole(GaussSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 */
	void exitRowsecurityoptionaltorole(GaussSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaultpermissive(GaussSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaultpermissive(GaussSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 */
	void enterRowsecuritydefaultforcmd(GaussSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 */
	void exitRowsecuritydefaultforcmd(GaussSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 */
	void enterRow_security_cmd(GaussSqlParser.Row_security_cmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 */
	void exitRow_security_cmd(GaussSqlParser.Row_security_cmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateamstmt(GaussSqlParser.CreateamstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateamstmt(GaussSqlParser.CreateamstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#am_type}.
	 * @param ctx the parse tree
	 */
	void enterAm_type(GaussSqlParser.Am_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#am_type}.
	 * @param ctx the parse tree
	 */
	void exitAm_type(GaussSqlParser.Am_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetrigstmt(GaussSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetrigstmt(GaussSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 */
	void enterTriggeractiontime(GaussSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 */
	void exitTriggeractiontime(GaussSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 */
	void enterTriggerevents(GaussSqlParser.TriggereventsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 */
	void exitTriggerevents(GaussSqlParser.TriggereventsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 */
	void enterTriggeroneevent(GaussSqlParser.TriggeroneeventContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 */
	void exitTriggeroneevent(GaussSqlParser.TriggeroneeventContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 */
	void enterTriggerreferencing(GaussSqlParser.TriggerreferencingContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 */
	void exitTriggerreferencing(GaussSqlParser.TriggerreferencingContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 */
	void enterTriggertransitions(GaussSqlParser.TriggertransitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 */
	void exitTriggertransitions(GaussSqlParser.TriggertransitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 */
	void enterTriggertransition(GaussSqlParser.TriggertransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 */
	void exitTriggertransition(GaussSqlParser.TriggertransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 */
	void enterTransitionoldornew(GaussSqlParser.TransitionoldornewContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 */
	void exitTransitionoldornew(GaussSqlParser.TransitionoldornewContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 */
	void enterTransitionrowortable(GaussSqlParser.TransitionrowortableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 */
	void exitTransitionrowortable(GaussSqlParser.TransitionrowortableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 */
	void enterTransitionrelname(GaussSqlParser.TransitionrelnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 */
	void exitTransitionrelname(GaussSqlParser.TransitionrelnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 */
	void enterTriggerforspec(GaussSqlParser.TriggerforspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 */
	void exitTriggerforspec(GaussSqlParser.TriggerforspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 */
	void enterTriggerforopteach(GaussSqlParser.TriggerforopteachContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 */
	void exitTriggerforopteach(GaussSqlParser.TriggerforopteachContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfortype(GaussSqlParser.TriggerfortypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfortype(GaussSqlParser.TriggerfortypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 */
	void enterTriggerwhen(GaussSqlParser.TriggerwhenContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 */
	void exitTriggerwhen(GaussSqlParser.TriggerwhenContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 */
	void enterFunction_or_procedure(GaussSqlParser.Function_or_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 */
	void exitFunction_or_procedure(GaussSqlParser.Function_or_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfuncargs(GaussSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfuncargs(GaussSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 */
	void enterTriggerfuncarg(GaussSqlParser.TriggerfuncargContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 */
	void exitTriggerfuncarg(GaussSqlParser.TriggerfuncargContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 */
	void enterOptconstrfromtable(GaussSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 */
	void exitOptconstrfromtable(GaussSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattributespec(GaussSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattributespec(GaussSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 */
	void enterConstraintattributeElem(GaussSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 */
	void exitConstraintattributeElem(GaussSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateeventtrigstmt(GaussSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateeventtrigstmt(GaussSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_when_list(GaussSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_when_list(GaussSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_when_item(GaussSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_when_item(GaussSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger_value_list(GaussSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger_value_list(GaussSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltereventtrigstmt(GaussSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltereventtrigstmt(GaussSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 */
	void enterEnable_trigger(GaussSqlParser.Enable_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 */
	void exitEnable_trigger(GaussSqlParser.Enable_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateassertionstmt(GaussSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateassertionstmt(GaussSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#definestmt}.
	 * @param ctx the parse tree
	 */
	void enterDefinestmt(GaussSqlParser.DefinestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#definestmt}.
	 * @param ctx the parse tree
	 */
	void exitDefinestmt(GaussSqlParser.DefinestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(GaussSqlParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(GaussSqlParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#def_list}.
	 * @param ctx the parse tree
	 */
	void enterDef_list(GaussSqlParser.Def_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#def_list}.
	 * @param ctx the parse tree
	 */
	void exitDef_list(GaussSqlParser.Def_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#def_elem}.
	 * @param ctx the parse tree
	 */
	void enterDef_elem(GaussSqlParser.Def_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#def_elem}.
	 * @param ctx the parse tree
	 */
	void exitDef_elem(GaussSqlParser.Def_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#def_arg}.
	 * @param ctx the parse tree
	 */
	void enterDef_arg(GaussSqlParser.Def_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#def_arg}.
	 * @param ctx the parse tree
	 */
	void exitDef_arg(GaussSqlParser.Def_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_definition(GaussSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_definition(GaussSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_list(GaussSqlParser.Old_aggr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_list(GaussSqlParser.Old_aggr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 */
	void enterOld_aggr_elem(GaussSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 */
	void exitOld_aggr_elem(GaussSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 */
	void enterEnum_val_list_(GaussSqlParser.Enum_val_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 */
	void exitEnum_val_list_(GaussSqlParser.Enum_val_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 */
	void enterEnum_val_list(GaussSqlParser.Enum_val_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 */
	void exitEnum_val_list(GaussSqlParser.Enum_val_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterenumstmt(GaussSqlParser.AlterenumstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterenumstmt(GaussSqlParser.AlterenumstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 */
	void enterIf_not_exists_(GaussSqlParser.If_not_exists_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 */
	void exitIf_not_exists_(GaussSqlParser.If_not_exists_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateopclassstmt(GaussSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateopclassstmt(GaussSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_item_list(GaussSqlParser.Opclass_item_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_item_list(GaussSqlParser.Opclass_item_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_item(GaussSqlParser.Opclass_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_item(GaussSqlParser.Opclass_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#default_}.
	 * @param ctx the parse tree
	 */
	void enterDefault_(GaussSqlParser.Default_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#default_}.
	 * @param ctx the parse tree
	 */
	void exitDefault_(GaussSqlParser.Default_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 */
	void enterOpfamily_(GaussSqlParser.Opfamily_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 */
	void exitOpfamily_(GaussSqlParser.Opfamily_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_purpose(GaussSqlParser.Opclass_purposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_purpose(GaussSqlParser.Opclass_purposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#recheck_}.
	 * @param ctx the parse tree
	 */
	void enterRecheck_(GaussSqlParser.Recheck_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#recheck_}.
	 * @param ctx the parse tree
	 */
	void exitRecheck_(GaussSqlParser.Recheck_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateopfamilystmt(GaussSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateopfamilystmt(GaussSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterAlteropfamilystmt(GaussSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitAlteropfamilystmt(GaussSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_drop_list(GaussSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_drop_list(GaussSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 */
	void enterOpclass_drop(GaussSqlParser.Opclass_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 */
	void exitOpclass_drop(GaussSqlParser.Opclass_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropopclassstmt(GaussSqlParser.DropopclassstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropopclassstmt(GaussSqlParser.DropopclassstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void enterDropopfamilystmt(GaussSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 */
	void exitDropopfamilystmt(GaussSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropownedstmt(GaussSqlParser.DropownedstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropownedstmt(GaussSqlParser.DropownedstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 */
	void enterReassignownedstmt(GaussSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 */
	void exitReassignownedstmt(GaussSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropstmt(GaussSqlParser.DropstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropstmt(GaussSqlParser.DropstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptablestmt(GaussSqlParser.DroptablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptablestmt(GaussSqlParser.DroptablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_any_name(GaussSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_any_name(GaussSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_name(GaussSqlParser.Object_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_name(GaussSqlParser.Object_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 */
	void enterDrop_type_name(GaussSqlParser.Drop_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 */
	void exitDrop_type_name(GaussSqlParser.Drop_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_name_on_any_name(GaussSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_name_on_any_name(GaussSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 */
	void enterAny_name_list_(GaussSqlParser.Any_name_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 */
	void exitAny_name_list_(GaussSqlParser.Any_name_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#any_name}.
	 * @param ctx the parse tree
	 */
	void enterAny_name(GaussSqlParser.Any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#any_name}.
	 * @param ctx the parse tree
	 */
	void exitAny_name(GaussSqlParser.Any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#attrs}.
	 * @param ctx the parse tree
	 */
	void enterAttrs(GaussSqlParser.AttrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#attrs}.
	 * @param ctx the parse tree
	 */
	void exitAttrs(GaussSqlParser.AttrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 */
	void enterType_name_list(GaussSqlParser.Type_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 */
	void exitType_name_list(GaussSqlParser.Type_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 */
	void enterTruncatestmt(GaussSqlParser.TruncatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 */
	void exitTruncatestmt(GaussSqlParser.TruncatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 */
	void enterRestart_seqs_(GaussSqlParser.Restart_seqs_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 */
	void exitRestart_seqs_(GaussSqlParser.Restart_seqs_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 */
	void enterCommentstmt(GaussSqlParser.CommentstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 */
	void exitCommentstmt(GaussSqlParser.CommentstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterComment_table_stmt(GaussSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitComment_table_stmt(GaussSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 */
	void enterComment_column_stmt(GaussSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 */
	void exitComment_column_stmt(GaussSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#comment_text}.
	 * @param ctx the parse tree
	 */
	void enterComment_text(GaussSqlParser.Comment_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#comment_text}.
	 * @param ctx the parse tree
	 */
	void exitComment_text(GaussSqlParser.Comment_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 */
	void enterSeclabelstmt(GaussSqlParser.SeclabelstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 */
	void exitSeclabelstmt(GaussSqlParser.SeclabelstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#provider_}.
	 * @param ctx the parse tree
	 */
	void enterProvider_(GaussSqlParser.Provider_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#provider_}.
	 * @param ctx the parse tree
	 */
	void exitProvider_(GaussSqlParser.Provider_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#security_label}.
	 * @param ctx the parse tree
	 */
	void enterSecurity_label(GaussSqlParser.Security_labelContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#security_label}.
	 * @param ctx the parse tree
	 */
	void exitSecurity_label(GaussSqlParser.Security_labelContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 */
	void enterFetchstmt(GaussSqlParser.FetchstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 */
	void exitFetchstmt(GaussSqlParser.FetchstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 */
	void enterFetch_args(GaussSqlParser.Fetch_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 */
	void exitFetch_args(GaussSqlParser.Fetch_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#from_in}.
	 * @param ctx the parse tree
	 */
	void enterFrom_in(GaussSqlParser.From_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#from_in}.
	 * @param ctx the parse tree
	 */
	void exitFrom_in(GaussSqlParser.From_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#from_in_}.
	 * @param ctx the parse tree
	 */
	void enterFrom_in_(GaussSqlParser.From_in_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#from_in_}.
	 * @param ctx the parse tree
	 */
	void exitFrom_in_(GaussSqlParser.From_in_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 */
	void enterGrantstmt(GaussSqlParser.GrantstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 */
	void exitGrantstmt(GaussSqlParser.GrantstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 */
	void enterRevokestmt(GaussSqlParser.RevokestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 */
	void exitRevokestmt(GaussSqlParser.RevokestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#privileges}.
	 * @param ctx the parse tree
	 */
	void enterPrivileges(GaussSqlParser.PrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#privileges}.
	 * @param ctx the parse tree
	 */
	void exitPrivileges(GaussSqlParser.PrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_list(GaussSqlParser.Privilege_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_list(GaussSqlParser.Privilege_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(GaussSqlParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(GaussSqlParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_target(GaussSqlParser.Privilege_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_target(GaussSqlParser.Privilege_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 */
	void enterGrantee_list(GaussSqlParser.Grantee_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 */
	void exitGrantee_list(GaussSqlParser.Grantee_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grantee}.
	 * @param ctx the parse tree
	 */
	void enterGrantee(GaussSqlParser.GranteeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grantee}.
	 * @param ctx the parse tree
	 */
	void exitGrantee(GaussSqlParser.GranteeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 */
	void enterGrant_grant_option_(GaussSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 */
	void exitGrant_grant_option_(GaussSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 */
	void enterGrantrolestmt(GaussSqlParser.GrantrolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 */
	void exitGrantrolestmt(GaussSqlParser.GrantrolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 */
	void enterRevokerolestmt(GaussSqlParser.RevokerolestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 */
	void exitRevokerolestmt(GaussSqlParser.RevokerolestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 */
	void enterGrant_admin_option_(GaussSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 */
	void exitGrant_admin_option_(GaussSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 */
	void enterGranted_by_(GaussSqlParser.Granted_by_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 */
	void exitGranted_by_(GaussSqlParser.Granted_by_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdefaultprivilegesstmt(GaussSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdefaultprivilegesstmt(GaussSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 */
	void enterDefacloptionlist(GaussSqlParser.DefacloptionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 */
	void exitDefacloptionlist(GaussSqlParser.DefacloptionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#defacloption}.
	 * @param ctx the parse tree
	 */
	void enterDefacloption(GaussSqlParser.DefacloptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#defacloption}.
	 * @param ctx the parse tree
	 */
	void exitDefacloption(GaussSqlParser.DefacloptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 */
	void enterDefaclaction(GaussSqlParser.DefaclactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 */
	void exitDefaclaction(GaussSqlParser.DefaclactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 */
	void enterDefacl_privilege_target(GaussSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 */
	void exitDefacl_privilege_target(GaussSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 */
	void enterIndexstmt(GaussSqlParser.IndexstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 */
	void exitIndexstmt(GaussSqlParser.IndexstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void enterUnique_(GaussSqlParser.Unique_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void exitUnique_(GaussSqlParser.Unique_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#single_name_}.
	 * @param ctx the parse tree
	 */
	void enterSingle_name_(GaussSqlParser.Single_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#single_name_}.
	 * @param ctx the parse tree
	 */
	void exitSingle_name_(GaussSqlParser.Single_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 */
	void enterConcurrently_(GaussSqlParser.Concurrently_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 */
	void exitConcurrently_(GaussSqlParser.Concurrently_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_name_}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name_(GaussSqlParser.Index_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_name_}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name_(GaussSqlParser.Index_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 */
	void enterAccess_method_clause(GaussSqlParser.Access_method_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 */
	void exitAccess_method_clause(GaussSqlParser.Access_method_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_params}.
	 * @param ctx the parse tree
	 */
	void enterIndex_params(GaussSqlParser.Index_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_params}.
	 * @param ctx the parse tree
	 */
	void exitIndex_params(GaussSqlParser.Index_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 */
	void enterIndex_elem_options(GaussSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 */
	void exitIndex_elem_options(GaussSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_elem}.
	 * @param ctx the parse tree
	 */
	void enterIndex_elem(GaussSqlParser.Index_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_elem}.
	 * @param ctx the parse tree
	 */
	void exitIndex_elem(GaussSqlParser.Index_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#include_}.
	 * @param ctx the parse tree
	 */
	void enterInclude_(GaussSqlParser.Include_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#include_}.
	 * @param ctx the parse tree
	 */
	void exitInclude_(GaussSqlParser.Include_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 */
	void enterIndex_including_params(GaussSqlParser.Index_including_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 */
	void exitIndex_including_params(GaussSqlParser.Index_including_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#collate_}.
	 * @param ctx the parse tree
	 */
	void enterCollate_(GaussSqlParser.Collate_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#collate_}.
	 * @param ctx the parse tree
	 */
	void exitCollate_(GaussSqlParser.Collate_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#class_}.
	 * @param ctx the parse tree
	 */
	void enterClass_(GaussSqlParser.Class_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#class_}.
	 * @param ctx the parse tree
	 */
	void exitClass_(GaussSqlParser.Class_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 */
	void enterAsc_desc_(GaussSqlParser.Asc_desc_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 */
	void exitAsc_desc_(GaussSqlParser.Asc_desc_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 */
	void enterNulls_order_(GaussSqlParser.Nulls_order_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 */
	void exitNulls_order_(GaussSqlParser.Nulls_order_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunctionstmt(GaussSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunctionstmt(GaussSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 */
	void enterOr_replace_(GaussSqlParser.Or_replace_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 */
	void exitOr_replace_(GaussSqlParser.Or_replace_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_args}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args(GaussSqlParser.Func_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_args}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args(GaussSqlParser.Func_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_list(GaussSqlParser.Func_args_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_list(GaussSqlParser.Func_args_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterFunction_with_argtypes_list(GaussSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitFunction_with_argtypes_list(GaussSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterFunction_with_argtypes(GaussSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitFunction_with_argtypes(GaussSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_with_defaults(GaussSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_with_defaults(GaussSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args_with_defaults_list(GaussSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args_with_defaults_list(GaussSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg(GaussSqlParser.Func_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg(GaussSqlParser.Func_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#arg_class}.
	 * @param ctx the parse tree
	 */
	void enterArg_class(GaussSqlParser.Arg_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#arg_class}.
	 * @param ctx the parse tree
	 */
	void exitArg_class(GaussSqlParser.Arg_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#param_name}.
	 * @param ctx the parse tree
	 */
	void enterParam_name(GaussSqlParser.Param_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#param_name}.
	 * @param ctx the parse tree
	 */
	void exitParam_name(GaussSqlParser.Param_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_return}.
	 * @param ctx the parse tree
	 */
	void enterFunc_return(GaussSqlParser.Func_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_return}.
	 * @param ctx the parse tree
	 */
	void exitFunc_return(GaussSqlParser.Func_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_type}.
	 * @param ctx the parse tree
	 */
	void enterFunc_type(GaussSqlParser.Func_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_type}.
	 * @param ctx the parse tree
	 */
	void exitFunc_type(GaussSqlParser.Func_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_with_default(GaussSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_with_default(GaussSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 */
	void enterAggr_arg(GaussSqlParser.Aggr_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 */
	void exitAggr_arg(GaussSqlParser.Aggr_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 */
	void enterAggr_args(GaussSqlParser.Aggr_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 */
	void exitAggr_args(GaussSqlParser.Aggr_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 */
	void enterAggr_args_list(GaussSqlParser.Aggr_args_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 */
	void exitAggr_args_list(GaussSqlParser.Aggr_args_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_with_argtypes(GaussSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_with_argtypes(GaussSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_with_argtypes_list(GaussSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_with_argtypes_list(GaussSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreatefunc_opt_list(GaussSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreatefunc_opt_list(GaussSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCommon_func_opt_item(GaussSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCommon_func_opt_item(GaussSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_body}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body(GaussSqlParser.Func_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_body}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body(GaussSqlParser.Func_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body_statement(GaussSqlParser.Func_body_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body_statement(GaussSqlParser.Func_body_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(GaussSqlParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(GaussSqlParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_as}.
	 * @param ctx the parse tree
	 */
	void enterFunc_as(GaussSqlParser.Func_asContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_as}.
	 * @param ctx the parse tree
	 */
	void exitFunc_as(GaussSqlParser.Func_asContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 */
	void enterTransform_type_list(GaussSqlParser.Transform_type_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 */
	void exitTransform_type_list(GaussSqlParser.Transform_type_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#definition_}.
	 * @param ctx the parse tree
	 */
	void enterDefinition_(GaussSqlParser.Definition_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#definition_}.
	 * @param ctx the parse tree
	 */
	void exitDefinition_(GaussSqlParser.Definition_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 */
	void enterTable_func_column(GaussSqlParser.Table_func_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 */
	void exitTable_func_column(GaussSqlParser.Table_func_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 */
	void enterTable_func_column_list(GaussSqlParser.Table_func_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 */
	void exitTable_func_column_list(GaussSqlParser.Table_func_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterfunctionstmt(GaussSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterfunctionstmt(GaussSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterAlterfunc_opt_list(GaussSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitAlterfunc_opt_list(GaussSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#restrict_}.
	 * @param ctx the parse tree
	 */
	void enterRestrict_(GaussSqlParser.Restrict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#restrict_}.
	 * @param ctx the parse tree
	 */
	void exitRestrict_(GaussSqlParser.Restrict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemovefuncstmt(GaussSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemovefuncstmt(GaussSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemoveaggrstmt(GaussSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemoveaggrstmt(GaussSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 */
	void enterRemoveoperstmt(GaussSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 */
	void exitRemoveoperstmt(GaussSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterOper_argtypes(GaussSqlParser.Oper_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitOper_argtypes(GaussSqlParser.Oper_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#any_operator}.
	 * @param ctx the parse tree
	 */
	void enterAny_operator(GaussSqlParser.Any_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#any_operator}.
	 * @param ctx the parse tree
	 */
	void exitAny_operator(GaussSqlParser.Any_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void enterOperator_with_argtypes_list(GaussSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 */
	void exitOperator_with_argtypes_list(GaussSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void enterOperator_with_argtypes(GaussSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 */
	void exitOperator_with_argtypes(GaussSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dostmt}.
	 * @param ctx the parse tree
	 */
	void enterDostmt(GaussSqlParser.DostmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dostmt}.
	 * @param ctx the parse tree
	 */
	void exitDostmt(GaussSqlParser.DostmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterDostmt_opt_list(GaussSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitDostmt_opt_list(GaussSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterDostmt_opt_item(GaussSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitDostmt_opt_item(GaussSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatecaststmt(GaussSqlParser.CreatecaststmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatecaststmt(GaussSqlParser.CreatecaststmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cast_context}.
	 * @param ctx the parse tree
	 */
	void enterCast_context(GaussSqlParser.Cast_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cast_context}.
	 * @param ctx the parse tree
	 */
	void exitCast_context(GaussSqlParser.Cast_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 */
	void enterDropcaststmt(GaussSqlParser.DropcaststmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 */
	void exitDropcaststmt(GaussSqlParser.DropcaststmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 */
	void enterIf_exists_(GaussSqlParser.If_exists_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 */
	void exitIf_exists_(GaussSqlParser.If_exists_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatetransformstmt(GaussSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatetransformstmt(GaussSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 */
	void enterTransform_element_list(GaussSqlParser.Transform_element_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 */
	void exitTransform_element_list(GaussSqlParser.Transform_element_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 */
	void enterDroptransformstmt(GaussSqlParser.DroptransformstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 */
	void exitDroptransformstmt(GaussSqlParser.DroptransformstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 */
	void enterReindexstmt(GaussSqlParser.ReindexstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 */
	void exitReindexstmt(GaussSqlParser.ReindexstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 */
	void enterReindex_target_relation(GaussSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 */
	void exitReindex_target_relation(GaussSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 */
	void enterReindex_target_all(GaussSqlParser.Reindex_target_allContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 */
	void exitReindex_target_all(GaussSqlParser.Reindex_target_allContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 */
	void enterReindex_option_list(GaussSqlParser.Reindex_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 */
	void exitReindex_option_list(GaussSqlParser.Reindex_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertblspcstmt(GaussSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertblspcstmt(GaussSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 */
	void enterRenamestmt(GaussSqlParser.RenamestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 */
	void exitRenamestmt(GaussSqlParser.RenamestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_table_stmt(GaussSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_table_stmt(GaussSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_schema_stmt(GaussSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_schema_stmt(GaussSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_database_stmt(GaussSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_database_stmt(GaussSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRename_column_stmt(GaussSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRename_column_stmt(GaussSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#column_}.
	 * @param ctx the parse tree
	 */
	void enterColumn_(GaussSqlParser.Column_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#column_}.
	 * @param ctx the parse tree
	 */
	void exitColumn_(GaussSqlParser.Column_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_data_}.
	 * @param ctx the parse tree
	 */
	void enterSet_data_(GaussSqlParser.Set_data_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_data_}.
	 * @param ctx the parse tree
	 */
	void exitSet_data_(GaussSqlParser.Set_data_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterobjectdependsstmt(GaussSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterobjectdependsstmt(GaussSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#no_}.
	 * @param ctx the parse tree
	 */
	void enterNo_(GaussSqlParser.No_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#no_}.
	 * @param ctx the parse tree
	 */
	void exitNo_(GaussSqlParser.No_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterobjectschemastmt(GaussSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterobjectschemastmt(GaussSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlteroperatorstmt(GaussSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlteroperatorstmt(GaussSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_list(GaussSqlParser.Operator_def_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_list(GaussSqlParser.Operator_def_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_elem(GaussSqlParser.Operator_def_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_elem(GaussSqlParser.Operator_def_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 */
	void enterOperator_def_arg(GaussSqlParser.Operator_def_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 */
	void exitOperator_def_arg(GaussSqlParser.Operator_def_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertypestmt(GaussSqlParser.AltertypestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertypestmt(GaussSqlParser.AltertypestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterownerstmt(GaussSqlParser.AlterownerstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterownerstmt(GaussSqlParser.AlterownerstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatepublicationstmt(GaussSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatepublicationstmt(GaussSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 */
	void enterPublication_for_tables_(GaussSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 */
	void exitPublication_for_tables_(GaussSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 */
	void enterPublication_for_tables(GaussSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 */
	void exitPublication_for_tables(GaussSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterpublicationstmt(GaussSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterpublicationstmt(GaussSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatesubscriptionstmt(GaussSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatesubscriptionstmt(GaussSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 */
	void enterPublication_name_list(GaussSqlParser.Publication_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 */
	void exitPublication_name_list(GaussSqlParser.Publication_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 */
	void enterPublication_name_item(GaussSqlParser.Publication_name_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 */
	void exitPublication_name_item(GaussSqlParser.Publication_name_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltersubscriptionstmt(GaussSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltersubscriptionstmt(GaussSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropsubscriptionstmt(GaussSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropsubscriptionstmt(GaussSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 */
	void enterRulestmt(GaussSqlParser.RulestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 */
	void exitRulestmt(GaussSqlParser.RulestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionlist(GaussSqlParser.RuleactionlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionlist(GaussSqlParser.RuleactionlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionmulti(GaussSqlParser.RuleactionmultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionmulti(GaussSqlParser.RuleactionmultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionstmt(GaussSqlParser.RuleactionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionstmt(GaussSqlParser.RuleactionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 */
	void enterRuleactionstmtOrEmpty(GaussSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 */
	void exitRuleactionstmtOrEmpty(GaussSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(GaussSqlParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(GaussSqlParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#instead_}.
	 * @param ctx the parse tree
	 */
	void enterInstead_(GaussSqlParser.Instead_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#instead_}.
	 * @param ctx the parse tree
	 */
	void exitInstead_(GaussSqlParser.Instead_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 */
	void enterNotifystmt(GaussSqlParser.NotifystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 */
	void exitNotifystmt(GaussSqlParser.NotifystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 */
	void enterNotify_payload(GaussSqlParser.Notify_payloadContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 */
	void exitNotify_payload(GaussSqlParser.Notify_payloadContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 */
	void enterListenstmt(GaussSqlParser.ListenstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 */
	void exitListenstmt(GaussSqlParser.ListenstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 */
	void enterUnlistenstmt(GaussSqlParser.UnlistenstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 */
	void exitUnlistenstmt(GaussSqlParser.UnlistenstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 */
	void enterTransactionstmt(GaussSqlParser.TransactionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 */
	void exitTransactionstmt(GaussSqlParser.TransactionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transaction_}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_(GaussSqlParser.Transaction_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transaction_}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_(GaussSqlParser.Transaction_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_item(GaussSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_item(GaussSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_list(GaussSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_list(GaussSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_mode_list_or_empty(GaussSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_mode_list_or_empty(GaussSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_chain_(GaussSqlParser.Transaction_chain_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_chain_(GaussSqlParser.Transaction_chain_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 */
	void enterViewstmt(GaussSqlParser.ViewstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 */
	void exitViewstmt(GaussSqlParser.ViewstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#check_option_}.
	 * @param ctx the parse tree
	 */
	void enterCheck_option_(GaussSqlParser.Check_option_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#check_option_}.
	 * @param ctx the parse tree
	 */
	void exitCheck_option_(GaussSqlParser.Check_option_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 */
	void enterLoadstmt(GaussSqlParser.LoadstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 */
	void exitLoadstmt(GaussSqlParser.LoadstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatedbstmt(GaussSqlParser.CreatedbstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatedbstmt(GaussSqlParser.CreatedbstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_list(GaussSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_list(GaussSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_items(GaussSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_items(GaussSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_item(GaussSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_item(GaussSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 */
	void enterCreatedb_opt_name(GaussSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 */
	void exitCreatedb_opt_name(GaussSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#equal_}.
	 * @param ctx the parse tree
	 */
	void enterEqual_(GaussSqlParser.Equal_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#equal_}.
	 * @param ctx the parse tree
	 */
	void exitEqual_(GaussSqlParser.Equal_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdatabasestmt(GaussSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdatabasestmt(GaussSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 */
	void enterSetTablespaceName(GaussSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 */
	void exitSetTablespaceName(GaussSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 */
	void enterRefresh_collation_version(GaussSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 */
	void exitRefresh_collation_version(GaussSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdatabasesetstmt(GaussSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdatabasesetstmt(GaussSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 */
	void enterDropdbstmt(GaussSqlParser.DropdbstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 */
	void exitDropdbstmt(GaussSqlParser.DropdbstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 */
	void enterDropschemastmt(GaussSqlParser.DropschemastmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 */
	void exitDropschemastmt(GaussSqlParser.DropschemastmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 */
	void enterDrop_option_list(GaussSqlParser.Drop_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 */
	void exitDrop_option_list(GaussSqlParser.Drop_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#drop_option}.
	 * @param ctx the parse tree
	 */
	void enterDrop_option(GaussSqlParser.Drop_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#drop_option}.
	 * @param ctx the parse tree
	 */
	void exitDrop_option(GaussSqlParser.Drop_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltercollationstmt(GaussSqlParser.AltercollationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltercollationstmt(GaussSqlParser.AltercollationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltersystemstmt(GaussSqlParser.AltersystemstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltersystemstmt(GaussSqlParser.AltersystemstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatedomainstmt(GaussSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatedomainstmt(GaussSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 */
	void enterAlterdomainstmt(GaussSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 */
	void exitAlterdomainstmt(GaussSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#as_}.
	 * @param ctx the parse tree
	 */
	void enterAs_(GaussSqlParser.As_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#as_}.
	 * @param ctx the parse tree
	 */
	void exitAs_(GaussSqlParser.As_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertsdictionarystmt(GaussSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertsdictionarystmt(GaussSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 */
	void enterAltertsconfigurationstmt(GaussSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 */
	void exitAltertsconfigurationstmt(GaussSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#any_with}.
	 * @param ctx the parse tree
	 */
	void enterAny_with(GaussSqlParser.Any_withContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#any_with}.
	 * @param ctx the parse tree
	 */
	void exitAny_with(GaussSqlParser.Any_withContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateconversionstmt(GaussSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateconversionstmt(GaussSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 */
	void enterClusterstmt(GaussSqlParser.ClusterstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 */
	void exitClusterstmt(GaussSqlParser.ClusterstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 */
	void enterCluster_index_specification(GaussSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 */
	void exitCluster_index_specification(GaussSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 */
	void enterVacuumstmt(GaussSqlParser.VacuumstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 */
	void exitVacuumstmt(GaussSqlParser.VacuumstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzestmt(GaussSqlParser.AnalyzestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzestmt(GaussSqlParser.AnalyzestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_list(GaussSqlParser.Utility_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_list(GaussSqlParser.Utility_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_list(GaussSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_list(GaussSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_keyword(GaussSqlParser.Analyze_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_keyword(GaussSqlParser.Analyze_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_elem(GaussSqlParser.Utility_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_elem(GaussSqlParser.Utility_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_name(GaussSqlParser.Utility_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_name(GaussSqlParser.Utility_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterUtility_option_arg(GaussSqlParser.Utility_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitUtility_option_arg(GaussSqlParser.Utility_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_elem(GaussSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_elem(GaussSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_name(GaussSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_name(GaussSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterVac_analyze_option_arg(GaussSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitVac_analyze_option_arg(GaussSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#analyze_}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_(GaussSqlParser.Analyze_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#analyze_}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_(GaussSqlParser.Analyze_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#verbose_}.
	 * @param ctx the parse tree
	 */
	void enterVerbose_(GaussSqlParser.Verbose_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#verbose_}.
	 * @param ctx the parse tree
	 */
	void exitVerbose_(GaussSqlParser.Verbose_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#full_}.
	 * @param ctx the parse tree
	 */
	void enterFull_(GaussSqlParser.Full_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#full_}.
	 * @param ctx the parse tree
	 */
	void exitFull_(GaussSqlParser.Full_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#freeze_}.
	 * @param ctx the parse tree
	 */
	void enterFreeze_(GaussSqlParser.Freeze_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#freeze_}.
	 * @param ctx the parse tree
	 */
	void exitFreeze_(GaussSqlParser.Freeze_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#name_list_}.
	 * @param ctx the parse tree
	 */
	void enterName_list_(GaussSqlParser.Name_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#name_list_}.
	 * @param ctx the parse tree
	 */
	void exitName_list_(GaussSqlParser.Name_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation(GaussSqlParser.Vacuum_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation(GaussSqlParser.Vacuum_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation_list(GaussSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation_list(GaussSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_relation_list_(GaussSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_relation_list_(GaussSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 */
	void enterExplainstmt(GaussSqlParser.ExplainstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 */
	void exitExplainstmt(GaussSqlParser.ExplainstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 */
	void enterExplainablestmt(GaussSqlParser.ExplainablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 */
	void exitExplainablestmt(GaussSqlParser.ExplainablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_list(GaussSqlParser.Explain_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_list(GaussSqlParser.Explain_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_elem(GaussSqlParser.Explain_option_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_elem(GaussSqlParser.Explain_option_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_name(GaussSqlParser.Explain_option_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_name(GaussSqlParser.Explain_option_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 */
	void enterExplain_option_arg(GaussSqlParser.Explain_option_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 */
	void exitExplain_option_arg(GaussSqlParser.Explain_option_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 */
	void enterPreparestmt(GaussSqlParser.PreparestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 */
	void exitPreparestmt(GaussSqlParser.PreparestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrep_type_clause(GaussSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrep_type_clause(GaussSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 */
	void enterPreparablestmt(GaussSqlParser.PreparablestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 */
	void exitPreparablestmt(GaussSqlParser.PreparablestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#executestmt}.
	 * @param ctx the parse tree
	 */
	void enterExecutestmt(GaussSqlParser.ExecutestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#executestmt}.
	 * @param ctx the parse tree
	 */
	void exitExecutestmt(GaussSqlParser.ExecutestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 */
	void enterExecute_param_clause(GaussSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 */
	void exitExecute_param_clause(GaussSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatestmt(GaussSqlParser.DeallocatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatestmt(GaussSqlParser.DeallocatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 */
	void enterInsertstmt(GaussSqlParser.InsertstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 */
	void exitInsertstmt(GaussSqlParser.InsertstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#insert_target}.
	 * @param ctx the parse tree
	 */
	void enterInsert_target(GaussSqlParser.Insert_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#insert_target}.
	 * @param ctx the parse tree
	 */
	void exitInsert_target(GaussSqlParser.Insert_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 */
	void enterInsert_rest(GaussSqlParser.Insert_restContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 */
	void exitInsert_rest(GaussSqlParser.Insert_restContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#override_kind}.
	 * @param ctx the parse tree
	 */
	void enterOverride_kind(GaussSqlParser.Override_kindContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#override_kind}.
	 * @param ctx the parse tree
	 */
	void exitOverride_kind(GaussSqlParser.Override_kindContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_list(GaussSqlParser.Insert_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_list(GaussSqlParser.Insert_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_item(GaussSqlParser.Insert_column_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_item(GaussSqlParser.Insert_column_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 */
	void enterOn_conflict_(GaussSqlParser.On_conflict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 */
	void exitOn_conflict_(GaussSqlParser.On_conflict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 */
	void enterConf_expr_(GaussSqlParser.Conf_expr_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 */
	void exitConf_expr_(GaussSqlParser.Conf_expr_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterReturning_clause(GaussSqlParser.Returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitReturning_clause(GaussSqlParser.Returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 */
	void enterMergestmt(GaussSqlParser.MergestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 */
	void exitMergestmt(GaussSqlParser.MergestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_insert_clause(GaussSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_insert_clause(GaussSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_clause(GaussSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_clause(GaussSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_delete_clause(GaussSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_delete_clause(GaussSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 */
	void enterDeletestmt(GaussSqlParser.DeletestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 */
	void exitDeletestmt(GaussSqlParser.DeletestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_clause(GaussSqlParser.Using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_clause(GaussSqlParser.Using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 */
	void enterLockstmt(GaussSqlParser.LockstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 */
	void exitLockstmt(GaussSqlParser.LockstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#lock_}.
	 * @param ctx the parse tree
	 */
	void enterLock_(GaussSqlParser.Lock_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#lock_}.
	 * @param ctx the parse tree
	 */
	void exitLock_(GaussSqlParser.Lock_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#lock_type}.
	 * @param ctx the parse tree
	 */
	void enterLock_type(GaussSqlParser.Lock_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#lock_type}.
	 * @param ctx the parse tree
	 */
	void exitLock_type(GaussSqlParser.Lock_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#nowait_}.
	 * @param ctx the parse tree
	 */
	void enterNowait_(GaussSqlParser.Nowait_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#nowait_}.
	 * @param ctx the parse tree
	 */
	void exitNowait_(GaussSqlParser.Nowait_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 */
	void enterNowait_or_skip_(GaussSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 */
	void exitNowait_or_skip_(GaussSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdatestmt(GaussSqlParser.UpdatestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdatestmt(GaussSqlParser.UpdatestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 */
	void enterSet_clause_list(GaussSqlParser.Set_clause_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 */
	void exitSet_clause_list(GaussSqlParser.Set_clause_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void enterSet_clause(GaussSqlParser.Set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void exitSet_clause(GaussSqlParser.Set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_target}.
	 * @param ctx the parse tree
	 */
	void enterSet_target(GaussSqlParser.Set_targetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_target}.
	 * @param ctx the parse tree
	 */
	void exitSet_target(GaussSqlParser.Set_targetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 */
	void enterSet_target_list(GaussSqlParser.Set_target_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 */
	void exitSet_target_list(GaussSqlParser.Set_target_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 */
	void enterDeclarecursorstmt(GaussSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 */
	void exitDeclarecursorstmt(GaussSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void enterCursor_name(GaussSqlParser.Cursor_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void exitCursor_name(GaussSqlParser.Cursor_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 */
	void enterCursor_options(GaussSqlParser.Cursor_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 */
	void exitCursor_options(GaussSqlParser.Cursor_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#hold_}.
	 * @param ctx the parse tree
	 */
	void enterHold_(GaussSqlParser.Hold_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#hold_}.
	 * @param ctx the parse tree
	 */
	void exitHold_(GaussSqlParser.Hold_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 */
	void enterSelectstmt(GaussSqlParser.SelectstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 */
	void exitSelectstmt(GaussSqlParser.SelectstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 */
	void enterSelect_with_parens(GaussSqlParser.Select_with_parensContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 */
	void exitSelect_with_parens(GaussSqlParser.Select_with_parensContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 */
	void enterSelect_no_parens(GaussSqlParser.Select_no_parensContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 */
	void exitSelect_no_parens(GaussSqlParser.Select_no_parensContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void enterSelect_clause(GaussSqlParser.Select_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void exitSelect_clause(GaussSqlParser.Select_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 */
	void enterSimple_select_pramary(GaussSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 */
	void exitSimple_select_pramary(GaussSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(GaussSqlParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(GaussSqlParser.With_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cte_list}.
	 * @param ctx the parse tree
	 */
	void enterCte_list(GaussSqlParser.Cte_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cte_list}.
	 * @param ctx the parse tree
	 */
	void exitCte_list(GaussSqlParser.Cte_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 */
	void enterCommon_table_expr(GaussSqlParser.Common_table_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 */
	void exitCommon_table_expr(GaussSqlParser.Common_table_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#materialized_}.
	 * @param ctx the parse tree
	 */
	void enterMaterialized_(GaussSqlParser.Materialized_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#materialized_}.
	 * @param ctx the parse tree
	 */
	void exitMaterialized_(GaussSqlParser.Materialized_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause_(GaussSqlParser.With_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause_(GaussSqlParser.With_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause(GaussSqlParser.Into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause(GaussSqlParser.Into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#strict_}.
	 * @param ctx the parse tree
	 */
	void enterStrict_(GaussSqlParser.Strict_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#strict_}.
	 * @param ctx the parse tree
	 */
	void exitStrict_(GaussSqlParser.Strict_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 */
	void enterOpttempTableName(GaussSqlParser.OpttempTableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 */
	void exitOpttempTableName(GaussSqlParser.OpttempTableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#table_}.
	 * @param ctx the parse tree
	 */
	void enterTable_(GaussSqlParser.Table_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#table_}.
	 * @param ctx the parse tree
	 */
	void exitTable_(GaussSqlParser.Table_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 */
	void enterAll_or_distinct(GaussSqlParser.All_or_distinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 */
	void exitAll_or_distinct(GaussSqlParser.All_or_distinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 */
	void enterDistinct_clause(GaussSqlParser.Distinct_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 */
	void exitDistinct_clause(GaussSqlParser.Distinct_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 */
	void enterAll_clause_(GaussSqlParser.All_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 */
	void exitAll_clause_(GaussSqlParser.All_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 */
	void enterSort_clause_(GaussSqlParser.Sort_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 */
	void exitSort_clause_(GaussSqlParser.Sort_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void enterSort_clause(GaussSqlParser.Sort_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 */
	void exitSort_clause(GaussSqlParser.Sort_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 */
	void enterSortby_list(GaussSqlParser.Sortby_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 */
	void exitSortby_list(GaussSqlParser.Sortby_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sortby}.
	 * @param ctx the parse tree
	 */
	void enterSortby(GaussSqlParser.SortbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sortby}.
	 * @param ctx the parse tree
	 */
	void exitSortby(GaussSqlParser.SortbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_limit}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit(GaussSqlParser.Select_limitContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_limit}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit(GaussSqlParser.Select_limitContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit_(GaussSqlParser.Select_limit_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit_(GaussSqlParser.Select_limit_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void enterLimit_clause(GaussSqlParser.Limit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void exitLimit_clause(GaussSqlParser.Limit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void enterOffset_clause(GaussSqlParser.Offset_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void exitOffset_clause(GaussSqlParser.Offset_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_limit_value(GaussSqlParser.Select_limit_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_limit_value(GaussSqlParser.Select_limit_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_offset_value(GaussSqlParser.Select_offset_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_offset_value(GaussSqlParser.Select_offset_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_fetch_first_value(GaussSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_fetch_first_value(GaussSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 */
	void enterI_or_f_const(GaussSqlParser.I_or_f_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 */
	void exitI_or_f_const(GaussSqlParser.I_or_f_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 */
	void enterRow_or_rows(GaussSqlParser.Row_or_rowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 */
	void exitRow_or_rows(GaussSqlParser.Row_or_rowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 */
	void enterFirst_or_next(GaussSqlParser.First_or_nextContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 */
	void exitFirst_or_next(GaussSqlParser.First_or_nextContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#group_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_clause(GaussSqlParser.Group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#group_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_clause(GaussSqlParser.Group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_list(GaussSqlParser.Group_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_list(GaussSqlParser.Group_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_item(GaussSqlParser.Group_by_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_item(GaussSqlParser.Group_by_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 */
	void enterEmpty_grouping_set(GaussSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 */
	void exitEmpty_grouping_set(GaussSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 */
	void enterRollup_clause(GaussSqlParser.Rollup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 */
	void exitRollup_clause(GaussSqlParser.Rollup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 */
	void enterCube_clause(GaussSqlParser.Cube_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 */
	void exitCube_clause(GaussSqlParser.Cube_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_clause(GaussSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_clause(GaussSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(GaussSqlParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(GaussSqlParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_clause(GaussSqlParser.For_locking_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_clause(GaussSqlParser.For_locking_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_clause_(GaussSqlParser.For_locking_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_clause_(GaussSqlParser.For_locking_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_items(GaussSqlParser.For_locking_itemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_items(GaussSqlParser.For_locking_itemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_item(GaussSqlParser.For_locking_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_item(GaussSqlParser.For_locking_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 */
	void enterFor_locking_strength(GaussSqlParser.For_locking_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 */
	void exitFor_locking_strength(GaussSqlParser.For_locking_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 */
	void enterLocked_rels_list(GaussSqlParser.Locked_rels_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 */
	void exitLocked_rels_list(GaussSqlParser.Locked_rels_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void enterValues_clause(GaussSqlParser.Values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void exitValues_clause(GaussSqlParser.Values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_clause(GaussSqlParser.From_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_clause(GaussSqlParser.From_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#from_list}.
	 * @param ctx the parse tree
	 */
	void enterFrom_list(GaussSqlParser.From_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#from_list}.
	 * @param ctx the parse tree
	 */
	void exitFrom_list(GaussSqlParser.From_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref(GaussSqlParser.Table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref(GaussSqlParser.Table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#cross_join}.
	 * @param ctx the parse tree
	 */
	void enterCross_join(GaussSqlParser.Cross_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#cross_join}.
	 * @param ctx the parse tree
	 */
	void exitCross_join(GaussSqlParser.Cross_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#normal_join}.
	 * @param ctx the parse tree
	 */
	void enterNormal_join(GaussSqlParser.Normal_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#normal_join}.
	 * @param ctx the parse tree
	 */
	void exitNormal_join(GaussSqlParser.Normal_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#natural_join}.
	 * @param ctx the parse tree
	 */
	void enterNatural_join(GaussSqlParser.Natural_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#natural_join}.
	 * @param ctx the parse tree
	 */
	void exitNatural_join(GaussSqlParser.Natural_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlias_clause(GaussSqlParser.Alias_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlias_clause(GaussSqlParser.Alias_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 */
	void enterFunc_alias_clause(GaussSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 */
	void exitFunc_alias_clause(GaussSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void enterJoin_type(GaussSqlParser.Join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void exitJoin_type(GaussSqlParser.Join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#join_qual}.
	 * @param ctx the parse tree
	 */
	void enterJoin_qual(GaussSqlParser.Join_qualContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#join_qual}.
	 * @param ctx the parse tree
	 */
	void exitJoin_qual(GaussSqlParser.Join_qualContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr(GaussSqlParser.Relation_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr(GaussSqlParser.Relation_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr_list(GaussSqlParser.Relation_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr_list(GaussSqlParser.Relation_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 */
	void enterRelation_expr_opt_alias(GaussSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 */
	void exitRelation_expr_opt_alias(GaussSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablesample_clause(GaussSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablesample_clause(GaussSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 */
	void enterRepeatable_clause_(GaussSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 */
	void exitRepeatable_clause_(GaussSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_table}.
	 * @param ctx the parse tree
	 */
	void enterFunc_table(GaussSqlParser.Func_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_table}.
	 * @param ctx the parse tree
	 */
	void exitFunc_table(GaussSqlParser.Func_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 */
	void enterRowsfrom_item(GaussSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 */
	void exitRowsfrom_item(GaussSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 */
	void enterRowsfrom_list(GaussSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 */
	void exitRowsfrom_list(GaussSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 */
	void enterCol_def_list_(GaussSqlParser.Col_def_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 */
	void exitCol_def_list_(GaussSqlParser.Col_def_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 */
	void enterOrdinality_(GaussSqlParser.Ordinality_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 */
	void exitOrdinality_(GaussSqlParser.Ordinality_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(GaussSqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(GaussSqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_or_current_clause(GaussSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_or_current_clause(GaussSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void enterOpttablefuncelementlist(GaussSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void exitOpttablefuncelementlist(GaussSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void enterTablefuncelementlist(GaussSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 */
	void exitTablefuncelementlist(GaussSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 */
	void enterTablefuncelement(GaussSqlParser.TablefuncelementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 */
	void exitTablefuncelement(GaussSqlParser.TablefuncelementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void enterXmltable(GaussSqlParser.XmltableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void exitXmltable(GaussSqlParser.XmltableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_list(GaussSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_list(GaussSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_el(GaussSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_el(GaussSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_option_list(GaussSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_option_list(GaussSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 */
	void enterXmltable_column_option_el(GaussSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 */
	void exitXmltable_column_option_el(GaussSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespace_list(GaussSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespace_list(GaussSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespace_el(GaussSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespace_el(GaussSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#typename}.
	 * @param ctx the parse tree
	 */
	void enterTypename(GaussSqlParser.TypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#typename}.
	 * @param ctx the parse tree
	 */
	void exitTypename(GaussSqlParser.TypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 */
	void enterOpt_array_bounds(GaussSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 */
	void exitOpt_array_bounds(GaussSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 */
	void enterSimpletypename(GaussSqlParser.SimpletypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 */
	void exitSimpletypename(GaussSqlParser.SimpletypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#consttypename}.
	 * @param ctx the parse tree
	 */
	void enterConsttypename(GaussSqlParser.ConsttypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#consttypename}.
	 * @param ctx the parse tree
	 */
	void exitConsttypename(GaussSqlParser.ConsttypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#generictype}.
	 * @param ctx the parse tree
	 */
	void enterGenerictype(GaussSqlParser.GenerictypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#generictype}.
	 * @param ctx the parse tree
	 */
	void exitGenerictype(GaussSqlParser.GenerictypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 */
	void enterType_modifiers_(GaussSqlParser.Type_modifiers_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 */
	void exitType_modifiers_(GaussSqlParser.Type_modifiers_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void enterNumeric(GaussSqlParser.NumericContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void exitNumeric(GaussSqlParser.NumericContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#float_}.
	 * @param ctx the parse tree
	 */
	void enterFloat_(GaussSqlParser.Float_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#float_}.
	 * @param ctx the parse tree
	 */
	void exitFloat_(GaussSqlParser.Float_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bit}.
	 * @param ctx the parse tree
	 */
	void enterBit(GaussSqlParser.BitContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bit}.
	 * @param ctx the parse tree
	 */
	void exitBit(GaussSqlParser.BitContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constbit}.
	 * @param ctx the parse tree
	 */
	void enterConstbit(GaussSqlParser.ConstbitContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constbit}.
	 * @param ctx the parse tree
	 */
	void exitConstbit(GaussSqlParser.ConstbitContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 */
	void enterBitwithlength(GaussSqlParser.BitwithlengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 */
	void exitBitwithlength(GaussSqlParser.BitwithlengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 */
	void enterBitwithoutlength(GaussSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 */
	void exitBitwithoutlength(GaussSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(GaussSqlParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(GaussSqlParser.CharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 */
	void enterConstcharacter(GaussSqlParser.ConstcharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 */
	void exitConstcharacter(GaussSqlParser.ConstcharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#character_c}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_c(GaussSqlParser.Character_cContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#character_c}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_c(GaussSqlParser.Character_cContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#varying_}.
	 * @param ctx the parse tree
	 */
	void enterVarying_(GaussSqlParser.Varying_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#varying_}.
	 * @param ctx the parse tree
	 */
	void exitVarying_(GaussSqlParser.Varying_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 */
	void enterConstdatetime(GaussSqlParser.ConstdatetimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 */
	void exitConstdatetime(GaussSqlParser.ConstdatetimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#constinterval}.
	 * @param ctx the parse tree
	 */
	void enterConstinterval(GaussSqlParser.ConstintervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#constinterval}.
	 * @param ctx the parse tree
	 */
	void exitConstinterval(GaussSqlParser.ConstintervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#timezone_}.
	 * @param ctx the parse tree
	 */
	void enterTimezone_(GaussSqlParser.Timezone_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#timezone_}.
	 * @param ctx the parse tree
	 */
	void exitTimezone_(GaussSqlParser.Timezone_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#interval_}.
	 * @param ctx the parse tree
	 */
	void enterInterval_(GaussSqlParser.Interval_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#interval_}.
	 * @param ctx the parse tree
	 */
	void exitInterval_(GaussSqlParser.Interval_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#interval_second}.
	 * @param ctx the parse tree
	 */
	void enterInterval_second(GaussSqlParser.Interval_secondContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#interval_second}.
	 * @param ctx the parse tree
	 */
	void exitInterval_second(GaussSqlParser.Interval_secondContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#jsonType}.
	 * @param ctx the parse tree
	 */
	void enterJsonType(GaussSqlParser.JsonTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#jsonType}.
	 * @param ctx the parse tree
	 */
	void exitJsonType(GaussSqlParser.JsonTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#escape_}.
	 * @param ctx the parse tree
	 */
	void enterEscape_(GaussSqlParser.Escape_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#escape_}.
	 * @param ctx the parse tree
	 */
	void exitEscape_(GaussSqlParser.Escape_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr}.
	 * @param ctx the parse tree
	 */
	void enterA_expr(GaussSqlParser.A_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr}.
	 * @param ctx the parse tree
	 */
	void exitA_expr(GaussSqlParser.A_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_qual(GaussSqlParser.A_expr_qualContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_qual(GaussSqlParser.A_expr_qualContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_lessless(GaussSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_lessless(GaussSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_or(GaussSqlParser.A_expr_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_or(GaussSqlParser.A_expr_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_and(GaussSqlParser.A_expr_andContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_and(GaussSqlParser.A_expr_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_between(GaussSqlParser.A_expr_betweenContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_between(GaussSqlParser.A_expr_betweenContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_in(GaussSqlParser.A_expr_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_in(GaussSqlParser.A_expr_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_not(GaussSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_not(GaussSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_isnull(GaussSqlParser.A_expr_isnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_isnull(GaussSqlParser.A_expr_isnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_is_not(GaussSqlParser.A_expr_is_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_is_not(GaussSqlParser.A_expr_is_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_compare(GaussSqlParser.A_expr_compareContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_compare(GaussSqlParser.A_expr_compareContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_like(GaussSqlParser.A_expr_likeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_like(GaussSqlParser.A_expr_likeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_qual_op(GaussSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_qual_op(GaussSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_qualop(GaussSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_qualop(GaussSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_add(GaussSqlParser.A_expr_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_add(GaussSqlParser.A_expr_addContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_mul(GaussSqlParser.A_expr_mulContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_mul(GaussSqlParser.A_expr_mulContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_caret(GaussSqlParser.A_expr_caretContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_caret(GaussSqlParser.A_expr_caretContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_unary_sign(GaussSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_unary_sign(GaussSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_at_time_zone(GaussSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_at_time_zone(GaussSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_collate(GaussSqlParser.A_expr_collateContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_collate(GaussSqlParser.A_expr_collateContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 */
	void enterA_expr_typecast(GaussSqlParser.A_expr_typecastContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 */
	void exitA_expr_typecast(GaussSqlParser.A_expr_typecastContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#b_expr}.
	 * @param ctx the parse tree
	 */
	void enterB_expr(GaussSqlParser.B_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#b_expr}.
	 * @param ctx the parse tree
	 */
	void exitB_expr(GaussSqlParser.B_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#c_expr}.
	 * @param ctx the parse tree
	 */
	void enterC_expr(GaussSqlParser.C_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#c_expr}.
	 * @param ctx the parse tree
	 */
	void exitC_expr(GaussSqlParser.C_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 */
	void enterPlsqlvariablename(GaussSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 */
	void exitPlsqlvariablename(GaussSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_application}.
	 * @param ctx the parse tree
	 */
	void enterFunc_application(GaussSqlParser.Func_applicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_application}.
	 * @param ctx the parse tree
	 */
	void exitFunc_application(GaussSqlParser.Func_applicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#star_context}.
	 * @param ctx the parse tree
	 */
	void enterStar_context(GaussSqlParser.Star_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#star_context}.
	 * @param ctx the parse tree
	 */
	void exitStar_context(GaussSqlParser.Star_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_expr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr(GaussSqlParser.Func_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_expr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr(GaussSqlParser.Func_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr_windowless(GaussSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr_windowless(GaussSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_expr_common_subexpr(GaussSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_expr_common_subexpr(GaussSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 */
	void enterXml_root_version(GaussSqlParser.Xml_root_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 */
	void exitXml_root_version(GaussSqlParser.Xml_root_versionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 */
	void enterXml_root_standalone_(GaussSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 */
	void exitXml_root_standalone_(GaussSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 */
	void enterXml_attributes(GaussSqlParser.Xml_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 */
	void exitXml_attributes(GaussSqlParser.Xml_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 */
	void enterXml_attribute_list(GaussSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 */
	void exitXml_attribute_list(GaussSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 */
	void enterXml_attribute_el(GaussSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 */
	void exitXml_attribute_el(GaussSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 */
	void enterDocument_or_content(GaussSqlParser.Document_or_contentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 */
	void exitDocument_or_content(GaussSqlParser.Document_or_contentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 */
	void enterXml_whitespace_option(GaussSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 */
	void exitXml_whitespace_option(GaussSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 */
	void enterXmlexists_argument(GaussSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 */
	void exitXmlexists_argument(GaussSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 */
	void enterXml_passing_mech(GaussSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 */
	void exitXml_passing_mech(GaussSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 */
	void enterWithin_group_clause(GaussSqlParser.Within_group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 */
	void exitWithin_group_clause(GaussSqlParser.Within_group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clause(GaussSqlParser.Filter_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clause(GaussSqlParser.Filter_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#window_clause}.
	 * @param ctx the parse tree
	 */
	void enterWindow_clause(GaussSqlParser.Window_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#window_clause}.
	 * @param ctx the parse tree
	 */
	void exitWindow_clause(GaussSqlParser.Window_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 */
	void enterWindow_definition_list(GaussSqlParser.Window_definition_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 */
	void exitWindow_definition_list(GaussSqlParser.Window_definition_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#window_definition}.
	 * @param ctx the parse tree
	 */
	void enterWindow_definition(GaussSqlParser.Window_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#window_definition}.
	 * @param ctx the parse tree
	 */
	void exitWindow_definition(GaussSqlParser.Window_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause(GaussSqlParser.Over_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause(GaussSqlParser.Over_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void enterWindow_specification(GaussSqlParser.Window_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void exitWindow_specification(GaussSqlParser.Window_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 */
	void enterExisting_window_name_(GaussSqlParser.Existing_window_name_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 */
	void exitExisting_window_name_(GaussSqlParser.Existing_window_name_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 */
	void enterPartition_clause_(GaussSqlParser.Partition_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 */
	void exitPartition_clause_(GaussSqlParser.Partition_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause_(GaussSqlParser.Frame_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause_(GaussSqlParser.Frame_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void enterFrame_extent(GaussSqlParser.Frame_extentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void exitFrame_extent(GaussSqlParser.Frame_extentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 */
	void enterFrame_bound(GaussSqlParser.Frame_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 */
	void exitFrame_bound(GaussSqlParser.Frame_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 */
	void enterWindow_exclusion_clause_(GaussSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 */
	void exitWindow_exclusion_clause_(GaussSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(GaussSqlParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(GaussSqlParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 */
	void enterExplicit_row(GaussSqlParser.Explicit_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 */
	void exitExplicit_row(GaussSqlParser.Explicit_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 */
	void enterImplicit_row(GaussSqlParser.Implicit_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 */
	void exitImplicit_row(GaussSqlParser.Implicit_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sub_type}.
	 * @param ctx the parse tree
	 */
	void enterSub_type(GaussSqlParser.Sub_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sub_type}.
	 * @param ctx the parse tree
	 */
	void exitSub_type(GaussSqlParser.Sub_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#all_op}.
	 * @param ctx the parse tree
	 */
	void enterAll_op(GaussSqlParser.All_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#all_op}.
	 * @param ctx the parse tree
	 */
	void exitAll_op(GaussSqlParser.All_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#mathop}.
	 * @param ctx the parse tree
	 */
	void enterMathop(GaussSqlParser.MathopContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#mathop}.
	 * @param ctx the parse tree
	 */
	void exitMathop(GaussSqlParser.MathopContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#qual_op}.
	 * @param ctx the parse tree
	 */
	void enterQual_op(GaussSqlParser.Qual_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#qual_op}.
	 * @param ctx the parse tree
	 */
	void exitQual_op(GaussSqlParser.Qual_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 */
	void enterQual_all_op(GaussSqlParser.Qual_all_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 */
	void exitQual_all_op(GaussSqlParser.Qual_all_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_Op(GaussSqlParser.Subquery_OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_Op(GaussSqlParser.Subquery_OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list(GaussSqlParser.Expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list(GaussSqlParser.Expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_list(GaussSqlParser.Func_arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_list(GaussSqlParser.Func_arg_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg_expr(GaussSqlParser.Func_arg_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg_expr(GaussSqlParser.Func_arg_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_list}.
	 * @param ctx the parse tree
	 */
	void enterType_list(GaussSqlParser.Type_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_list}.
	 * @param ctx the parse tree
	 */
	void exitType_list(GaussSqlParser.Type_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#array_expr}.
	 * @param ctx the parse tree
	 */
	void enterArray_expr(GaussSqlParser.Array_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#array_expr}.
	 * @param ctx the parse tree
	 */
	void exitArray_expr(GaussSqlParser.Array_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterArray_expr_list(GaussSqlParser.Array_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitArray_expr_list(GaussSqlParser.Array_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#extract_list}.
	 * @param ctx the parse tree
	 */
	void enterExtract_list(GaussSqlParser.Extract_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#extract_list}.
	 * @param ctx the parse tree
	 */
	void exitExtract_list(GaussSqlParser.Extract_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 */
	void enterExtract_arg(GaussSqlParser.Extract_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 */
	void exitExtract_arg(GaussSqlParser.Extract_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 */
	void enterUnicode_normal_form(GaussSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 */
	void exitUnicode_normal_form(GaussSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 */
	void enterOverlay_list(GaussSqlParser.Overlay_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 */
	void exitOverlay_list(GaussSqlParser.Overlay_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#position_list}.
	 * @param ctx the parse tree
	 */
	void enterPosition_list(GaussSqlParser.Position_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#position_list}.
	 * @param ctx the parse tree
	 */
	void exitPosition_list(GaussSqlParser.Position_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#substr_list}.
	 * @param ctx the parse tree
	 */
	void enterSubstr_list(GaussSqlParser.Substr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#substr_list}.
	 * @param ctx the parse tree
	 */
	void exitSubstr_list(GaussSqlParser.Substr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#trim_list}.
	 * @param ctx the parse tree
	 */
	void enterTrim_list(GaussSqlParser.Trim_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#trim_list}.
	 * @param ctx the parse tree
	 */
	void exitTrim_list(GaussSqlParser.Trim_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void enterIn_expr_select(GaussSqlParser.In_expr_selectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void exitIn_expr_select(GaussSqlParser.In_expr_selectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void enterIn_expr_list(GaussSqlParser.In_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 */
	void exitIn_expr_list(GaussSqlParser.In_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#case_expr}.
	 * @param ctx the parse tree
	 */
	void enterCase_expr(GaussSqlParser.Case_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#case_expr}.
	 * @param ctx the parse tree
	 */
	void exitCase_expr(GaussSqlParser.Case_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 */
	void enterWhen_clause_list(GaussSqlParser.When_clause_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 */
	void exitWhen_clause_list(GaussSqlParser.When_clause_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#when_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhen_clause(GaussSqlParser.When_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#when_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhen_clause(GaussSqlParser.When_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#case_default}.
	 * @param ctx the parse tree
	 */
	void enterCase_default(GaussSqlParser.Case_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#case_default}.
	 * @param ctx the parse tree
	 */
	void exitCase_default(GaussSqlParser.Case_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#case_arg}.
	 * @param ctx the parse tree
	 */
	void enterCase_arg(GaussSqlParser.Case_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#case_arg}.
	 * @param ctx the parse tree
	 */
	void exitCase_arg(GaussSqlParser.Case_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void enterColumnref(GaussSqlParser.ColumnrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void exitColumnref(GaussSqlParser.ColumnrefContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 */
	void enterIndirection_el(GaussSqlParser.Indirection_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 */
	void exitIndirection_el(GaussSqlParser.Indirection_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 */
	void enterSlice_bound_(GaussSqlParser.Slice_bound_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 */
	void exitSlice_bound_(GaussSqlParser.Slice_bound_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#indirection}.
	 * @param ctx the parse tree
	 */
	void enterIndirection(GaussSqlParser.IndirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#indirection}.
	 * @param ctx the parse tree
	 */
	void exitIndirection(GaussSqlParser.IndirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 */
	void enterOpt_indirection(GaussSqlParser.Opt_indirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 */
	void exitOpt_indirection(GaussSqlParser.Opt_indirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_passing_clause(GaussSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_passing_clause(GaussSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJson_arguments(GaussSqlParser.Json_argumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJson_arguments(GaussSqlParser.Json_argumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_argument}.
	 * @param ctx the parse tree
	 */
	void enterJson_argument(GaussSqlParser.Json_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_argument}.
	 * @param ctx the parse tree
	 */
	void exitJson_argument(GaussSqlParser.Json_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 */
	void enterJson_wrapper_behavior(GaussSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 */
	void exitJson_wrapper_behavior(GaussSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior(GaussSqlParser.Json_behaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior(GaussSqlParser.Json_behaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior_type(GaussSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior_type(GaussSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_behavior_clause(GaussSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_behavior_clause(GaussSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_on_error_clause(GaussSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_on_error_clause(GaussSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_expr(GaussSqlParser.Json_value_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_expr(GaussSqlParser.Json_value_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_format_clause(GaussSqlParser.Json_format_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_format_clause(GaussSqlParser.Json_format_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_quotes_clause(GaussSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_quotes_clause(GaussSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_returning_clause(GaussSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_returning_clause(GaussSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJson_predicate_type_constraint(GaussSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJson_predicate_type_constraint(GaussSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJson_key_uniqueness_constraint(GaussSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJson_key_uniqueness_constraint(GaussSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 */
	void enterJson_name_and_value_list(GaussSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 */
	void exitJson_name_and_value_list(GaussSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 */
	void enterJson_name_and_value(GaussSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 */
	void exitJson_name_and_value(GaussSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_constructor_null_clause(GaussSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_constructor_null_clause(GaussSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_constructor_null_clause(GaussSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_constructor_null_clause(GaussSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_expr_list(GaussSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_expr_list(GaussSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 */
	void enterJson_aggregate_func(GaussSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 */
	void exitJson_aggregate_func(GaussSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_aggregate_order_by_clause(GaussSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_aggregate_order_by_clause(GaussSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#target_list_}.
	 * @param ctx the parse tree
	 */
	void enterTarget_list_(GaussSqlParser.Target_list_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#target_list_}.
	 * @param ctx the parse tree
	 */
	void exitTarget_list_(GaussSqlParser.Target_list_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#target_list}.
	 * @param ctx the parse tree
	 */
	void enterTarget_list(GaussSqlParser.Target_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#target_list}.
	 * @param ctx the parse tree
	 */
	void exitTarget_list(GaussSqlParser.Target_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#target_el}.
	 * @param ctx the parse tree
	 */
	void enterTarget_el(GaussSqlParser.Target_elContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#target_el}.
	 * @param ctx the parse tree
	 */
	void exitTarget_el(GaussSqlParser.Target_elContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#target_alias}.
	 * @param ctx the parse tree
	 */
	void enterTarget_alias(GaussSqlParser.Target_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#target_alias}.
	 * @param ctx the parse tree
	 */
	void exitTarget_alias(GaussSqlParser.Target_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 */
	void enterQualified_name_list(GaussSqlParser.Qualified_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 */
	void exitQualified_name_list(GaussSqlParser.Qualified_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_name(GaussSqlParser.Qualified_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_name(GaussSqlParser.Qualified_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#name_list}.
	 * @param ctx the parse tree
	 */
	void enterName_list(GaussSqlParser.Name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#name_list}.
	 * @param ctx the parse tree
	 */
	void exitName_list(GaussSqlParser.Name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(GaussSqlParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(GaussSqlParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void enterAttr_name(GaussSqlParser.Attr_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void exitAttr_name(GaussSqlParser.Attr_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#file_name}.
	 * @param ctx the parse tree
	 */
	void enterFile_name(GaussSqlParser.File_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#file_name}.
	 * @param ctx the parse tree
	 */
	void exitFile_name(GaussSqlParser.File_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#func_name}.
	 * @param ctx the parse tree
	 */
	void enterFunc_name(GaussSqlParser.Func_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#func_name}.
	 * @param ctx the parse tree
	 */
	void exitFunc_name(GaussSqlParser.Func_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 */
	void enterAexprconst(GaussSqlParser.AexprconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 */
	void exitAexprconst(GaussSqlParser.AexprconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#xconst}.
	 * @param ctx the parse tree
	 */
	void enterXconst(GaussSqlParser.XconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#xconst}.
	 * @param ctx the parse tree
	 */
	void exitXconst(GaussSqlParser.XconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bconst}.
	 * @param ctx the parse tree
	 */
	void enterBconst(GaussSqlParser.BconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bconst}.
	 * @param ctx the parse tree
	 */
	void exitBconst(GaussSqlParser.BconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#fconst}.
	 * @param ctx the parse tree
	 */
	void enterFconst(GaussSqlParser.FconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#fconst}.
	 * @param ctx the parse tree
	 */
	void exitFconst(GaussSqlParser.FconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#iconst}.
	 * @param ctx the parse tree
	 */
	void enterIconst(GaussSqlParser.IconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#iconst}.
	 * @param ctx the parse tree
	 */
	void exitIconst(GaussSqlParser.IconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#sconst}.
	 * @param ctx the parse tree
	 */
	void enterSconst(GaussSqlParser.SconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#sconst}.
	 * @param ctx the parse tree
	 */
	void exitSconst(GaussSqlParser.SconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#anysconst}.
	 * @param ctx the parse tree
	 */
	void enterAnysconst(GaussSqlParser.AnysconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#anysconst}.
	 * @param ctx the parse tree
	 */
	void exitAnysconst(GaussSqlParser.AnysconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#uescape_}.
	 * @param ctx the parse tree
	 */
	void enterUescape_(GaussSqlParser.Uescape_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#uescape_}.
	 * @param ctx the parse tree
	 */
	void exitUescape_(GaussSqlParser.Uescape_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#signediconst}.
	 * @param ctx the parse tree
	 */
	void enterSignediconst(GaussSqlParser.SignediconstContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#signediconst}.
	 * @param ctx the parse tree
	 */
	void exitSignediconst(GaussSqlParser.SignediconstContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#roleid}.
	 * @param ctx the parse tree
	 */
	void enterRoleid(GaussSqlParser.RoleidContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#roleid}.
	 * @param ctx the parse tree
	 */
	void exitRoleid(GaussSqlParser.RoleidContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#rolespec}.
	 * @param ctx the parse tree
	 */
	void enterRolespec(GaussSqlParser.RolespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#rolespec}.
	 * @param ctx the parse tree
	 */
	void exitRolespec(GaussSqlParser.RolespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#role_list}.
	 * @param ctx the parse tree
	 */
	void enterRole_list(GaussSqlParser.Role_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#role_list}.
	 * @param ctx the parse tree
	 */
	void exitRole_list(GaussSqlParser.Role_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#colid}.
	 * @param ctx the parse tree
	 */
	void enterColid(GaussSqlParser.ColidContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#colid}.
	 * @param ctx the parse tree
	 */
	void exitColid(GaussSqlParser.ColidContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 */
	void enterType_function_name(GaussSqlParser.Type_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 */
	void exitType_function_name(GaussSqlParser.Type_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 */
	void enterNonreservedword(GaussSqlParser.NonreservedwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 */
	void exitNonreservedword(GaussSqlParser.NonreservedwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#colLabel}.
	 * @param ctx the parse tree
	 */
	void enterColLabel(GaussSqlParser.ColLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#colLabel}.
	 * @param ctx the parse tree
	 */
	void exitColLabel(GaussSqlParser.ColLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 */
	void enterBareColLabel(GaussSqlParser.BareColLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 */
	void exitBareColLabel(GaussSqlParser.BareColLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 */
	void enterUnreserved_keyword(GaussSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 */
	void exitUnreserved_keyword(GaussSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 */
	void enterCol_name_keyword(GaussSqlParser.Col_name_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 */
	void exitCol_name_keyword(GaussSqlParser.Col_name_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 */
	void enterType_func_name_keyword(GaussSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 */
	void exitType_func_name_keyword(GaussSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 */
	void enterReserved_keyword(GaussSqlParser.Reserved_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 */
	void exitReserved_keyword(GaussSqlParser.Reserved_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 */
	void enterBare_label_keyword(GaussSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 */
	void exitBare_label_keyword(GaussSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAny_identifier(GaussSqlParser.Any_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAny_identifier(GaussSqlParser.Any_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GaussSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(GaussSqlParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GaussSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(GaussSqlParser.IdentifierContext ctx);
}