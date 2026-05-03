// Generated from GaussSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.gauss.parser.antlr;

    import com.clougence.clouddm.ds.gauss.parser.base.GaussSqlParserBase;


import com.clougence.clouddm.dsfamily.postgres.parser.base.PgSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GaussSqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GaussSqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunc_opt_item(GaussSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionspec(GaussSqlParser.PartitionspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#union_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion_context(GaussSqlParser.Union_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(GaussSqlParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtblock(GaussSqlParser.StmtblockContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtmulti(GaussSqlParser.StmtmultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(GaussSqlParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#callstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallstmt(GaussSqlParser.CallstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreaterolestmt(GaussSqlParser.CreaterolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#with_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_(GaussSqlParser.With_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptrolelist(GaussSqlParser.OptrolelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroptrolelist(GaussSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroptroleelem(GaussSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateoptroleelem(GaussSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateuserstmt(GaussSqlParser.CreateuserstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterrolestmt(GaussSqlParser.AlterrolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#in_database_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_database_(GaussSqlParser.In_database_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterrolesetstmt(GaussSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroprolestmt(GaussSqlParser.DroprolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropuserstmt(GaussSqlParser.DropuserstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreategroupstmt(GaussSqlParser.CreategroupstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltergroupstmt(GaussSqlParser.AltergroupstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#add_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_drop(GaussSqlParser.Add_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateschemastmt(GaussSqlParser.CreateschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptschemaname(GaussSqlParser.OptschemanameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptschemaeltlist(GaussSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_stmt(GaussSqlParser.Schema_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariablesetstmt(GaussSqlParser.VariablesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_rest(GaussSqlParser.Set_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_set(GaussSqlParser.Generic_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_rest_more(GaussSqlParser.Set_rest_moreContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#var_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_name(GaussSqlParser.Var_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#var_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_list(GaussSqlParser.Var_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#var_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_value(GaussSqlParser.Var_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#iso_level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIso_level(GaussSqlParser.Iso_levelContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_or_string_(GaussSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#zone_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZone_value(GaussSqlParser.Zone_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#encoding_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncoding_(GaussSqlParser.Encoding_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonreservedword_or_sconst(GaussSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableresetstmt(GaussSqlParser.VariableresetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReset_rest(GaussSqlParser.Reset_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_reset(GaussSqlParser.Generic_resetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetresetclause(GaussSqlParser.SetresetclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionsetresetclause(GaussSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableshowstmt(GaussSqlParser.VariableshowstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintssetstmt(GaussSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints_set_list(GaussSqlParser.Constraints_set_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints_set_mode(GaussSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckpointstmt(GaussSqlParser.CheckpointstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscardstmt(GaussSqlParser.DiscardstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertablestmt(GaussSqlParser.AltertablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_cmds(GaussSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_cmd(GaussSqlParser.Partition_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partition_cmd(GaussSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumn(GaussSqlParser.AddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterColumn(GaussSqlParser.AlterColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropColumn(GaussSqlParser.DropColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddConstraint(GaussSqlParser.AddConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterConstaint(GaussSqlParser.AlterConstaintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidateConstraint(GaussSqlParser.ValidateConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropConstraint(GaussSqlParser.DropConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetWithoutOids(GaussSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterOnName(GaussSqlParser.ClusterOnNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetWithOut(GaussSqlParser.SetWithOutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link GaussSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsupportAlterTableStatement(GaussSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_column_default(GaussSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_behavior_(GaussSqlParser.Drop_behavior_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollate_clause_(GaussSqlParser.Collate_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_using}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_using(GaussSqlParser.Alter_usingContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplica_identity(GaussSqlParser.Replica_identityContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reloptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloptions(GaussSqlParser.ReloptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloptions_(GaussSqlParser.Reloptions_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloption_list(GaussSqlParser.Reloption_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloption_elem(GaussSqlParser.Reloption_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identity_column_option_list(GaussSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identity_column_option(GaussSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionboundspec(GaussSqlParser.PartitionboundspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partbound_elem(GaussSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partbound(GaussSqlParser.Hash_partboundContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltercompositetypestmt(GaussSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type_cmds(GaussSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type_cmd(GaussSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloseportalstmt(GaussSqlParser.CloseportalstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopystmt(GaussSqlParser.CopystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_from(GaussSqlParser.Copy_fromContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#program_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram_(GaussSqlParser.Program_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_file_name(GaussSqlParser.Copy_file_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_options(GaussSqlParser.Copy_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_opt_list(GaussSqlParser.Copy_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_opt_item(GaussSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#binary_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_(GaussSqlParser.Binary_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_delimiter(GaussSqlParser.Copy_delimiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#using_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_(GaussSqlParser.Using_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_list(GaussSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_elem(GaussSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg(GaussSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg_list(GaussSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg_list_item(GaussSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatestmt(GaussSqlParser.CreatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttemp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttemp(GaussSqlParser.OpttempContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttableelementlist(GaussSqlParser.OpttableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttypedtableelementlist(GaussSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableelementlist(GaussSqlParser.TableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedtableelementlist(GaussSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tableelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableelement(GaussSqlParser.TableelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedtableelement(GaussSqlParser.TypedtableelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#columnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDef(GaussSqlParser.ColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnOptions(GaussSqlParser.ColumnOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#colquallist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColquallist(GaussSqlParser.ColquallistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColconstraint(GaussSqlParser.ColconstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintNotNull(GaussSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintNull(GaussSqlParser.ConstraintNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintUnique(GaussSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintPrimary(GaussSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintCheck(GaussSqlParser.ConstraintCheckContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintDefault(GaussSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintGenerated(GaussSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link GaussSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintReference(GaussSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generated_when}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenerated_when(GaussSqlParser.Generated_whenContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattr(GaussSqlParser.ConstraintattrContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeclause(GaussSqlParser.TablelikeclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeoptionlist(GaussSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeoption(GaussSqlParser.TablelikeoptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableconstraint(GaussSqlParser.TableconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintelem(GaussSqlParser.ConstraintelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNo_inherit_(GaussSqlParser.No_inherit_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#column_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_list_(GaussSqlParser.Column_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#columnlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnlist(GaussSqlParser.ColumnlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#columnElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnElem(GaussSqlParser.ColumnElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#c_include_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_include_(GaussSqlParser.C_include_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#key_match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_match(GaussSqlParser.Key_matchContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionconstraintlist(GaussSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionconstraintelem(GaussSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionwhereclause(GaussSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#key_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_actions(GaussSqlParser.Key_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#key_update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_update(GaussSqlParser.Key_updateContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#key_delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_delete(GaussSqlParser.Key_deleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#key_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_action(GaussSqlParser.Key_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optinherit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptinherit(GaussSqlParser.OptinheritContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptpartitionspec(GaussSqlParser.OptpartitionspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#partition_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_item(GaussSqlParser.Partition_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#part_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPart_params(GaussSqlParser.Part_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#part_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPart_elem(GaussSqlParser.Part_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_access_method_clause(GaussSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optwith}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptwith(GaussSqlParser.OptwithContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOncommitoption(GaussSqlParser.OncommitoptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablespace(GaussSqlParser.OpttablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptconstablespace(GaussSqlParser.OptconstablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#existingindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistingindex(GaussSqlParser.ExistingindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatestatsstmt(GaussSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterstatsstmt(GaussSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateasstmt(GaussSqlParser.CreateasstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_as_target(GaussSqlParser.Create_as_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#with_data_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_data_(GaussSqlParser.With_data_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatematviewstmt(GaussSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_mv_target(GaussSqlParser.Create_mv_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optnolog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptnolog(GaussSqlParser.OptnologContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshmatviewstmt(GaussSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateseqstmt(GaussSqlParser.CreateseqstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterseqstmt(GaussSqlParser.AlterseqstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptseqoptlist(GaussSqlParser.OptseqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptparenthesizedseqoptlist(GaussSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqoptlist(GaussSqlParser.SeqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqoptelem(GaussSqlParser.SeqoptelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#by_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBy_(GaussSqlParser.By_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#numericonly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericonly(GaussSqlParser.NumericonlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericonly_list(GaussSqlParser.Numericonly_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateplangstmt(GaussSqlParser.CreateplangstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#trusted_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrusted_(GaussSqlParser.Trusted_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#handler_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandler_name(GaussSqlParser.Handler_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_handler_(GaussSqlParser.Inline_handler_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidator_clause(GaussSqlParser.Validator_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#validator_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidator_(GaussSqlParser.Validator_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#procedural_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedural_(GaussSqlParser.Procedural_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetablespacestmt(GaussSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablespaceowner(GaussSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptablespacestmt(GaussSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateextensionstmt(GaussSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_extension_opt_list(GaussSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_extension_opt_item(GaussSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterextensionstmt(GaussSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_extension_opt_list(GaussSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_extension_opt_item(GaussSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterextensioncontentsstmt(GaussSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefdwstmt(GaussSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_option(GaussSqlParser.Fdw_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_options(GaussSqlParser.Fdw_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_options_(GaussSqlParser.Fdw_options_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfdwstmt(GaussSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_generic_options(GaussSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_list(GaussSqlParser.Generic_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_options(GaussSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_option_list(GaussSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_option_elem(GaussSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_elem(GaussSqlParser.Generic_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_name(GaussSqlParser.Generic_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_arg(GaussSqlParser.Generic_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateforeignserverstmt(GaussSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(GaussSqlParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_server_version(GaussSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_server_version_(GaussSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterforeignserverstmt(GaussSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateforeigntablestmt(GaussSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportforeignschemastmt(GaussSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_qualification_type(GaussSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_qualification(GaussSqlParser.Import_qualificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateusermappingstmt(GaussSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuth_ident(GaussSqlParser.Auth_identContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropusermappingstmt(GaussSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterusermappingstmt(GaussSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatepolicystmt(GaussSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterpolicystmt(GaussSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionalexpr(GaussSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionalwithcheck(GaussSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaulttorole(GaussSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionaltorole(GaussSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaultpermissive(GaussSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaultforcmd(GaussSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_security_cmd(GaussSqlParser.Row_security_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateamstmt(GaussSqlParser.CreateamstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#am_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAm_type(GaussSqlParser.Am_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetrigstmt(GaussSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggeractiontime(GaussSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerevents(GaussSqlParser.TriggereventsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggeroneevent(GaussSqlParser.TriggeroneeventContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerreferencing(GaussSqlParser.TriggerreferencingContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggertransitions(GaussSqlParser.TriggertransitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggertransition(GaussSqlParser.TriggertransitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionoldornew(GaussSqlParser.TransitionoldornewContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionrowortable(GaussSqlParser.TransitionrowortableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionrelname(GaussSqlParser.TransitionrelnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerforspec(GaussSqlParser.TriggerforspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerforopteach(GaussSqlParser.TriggerforopteachContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfortype(GaussSqlParser.TriggerfortypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerwhen(GaussSqlParser.TriggerwhenContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_or_procedure(GaussSqlParser.Function_or_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfuncargs(GaussSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfuncarg(GaussSqlParser.TriggerfuncargContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptconstrfromtable(GaussSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattributespec(GaussSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattributeElem(GaussSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateeventtrigstmt(GaussSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_when_list(GaussSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_when_item(GaussSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_value_list(GaussSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltereventtrigstmt(GaussSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_trigger(GaussSqlParser.Enable_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateassertionstmt(GaussSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#definestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinestmt(GaussSqlParser.DefinestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(GaussSqlParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#def_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_list(GaussSqlParser.Def_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#def_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_elem(GaussSqlParser.Def_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#def_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_arg(GaussSqlParser.Def_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_definition(GaussSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_list(GaussSqlParser.Old_aggr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_elem(GaussSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnum_val_list_(GaussSqlParser.Enum_val_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnum_val_list(GaussSqlParser.Enum_val_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterenumstmt(GaussSqlParser.AlterenumstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_not_exists_(GaussSqlParser.If_not_exists_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateopclassstmt(GaussSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_item_list(GaussSqlParser.Opclass_item_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_item(GaussSqlParser.Opclass_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#default_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_(GaussSqlParser.Default_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpfamily_(GaussSqlParser.Opfamily_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_purpose(GaussSqlParser.Opclass_purposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#recheck_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecheck_(GaussSqlParser.Recheck_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateopfamilystmt(GaussSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteropfamilystmt(GaussSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_drop_list(GaussSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_drop(GaussSqlParser.Opclass_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropopclassstmt(GaussSqlParser.DropopclassstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropopfamilystmt(GaussSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropownedstmt(GaussSqlParser.DropownedstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReassignownedstmt(GaussSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropstmt(GaussSqlParser.DropstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptablestmt(GaussSqlParser.DroptablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_any_name(GaussSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_name(GaussSqlParser.Object_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_type_name(GaussSqlParser.Drop_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_name_on_any_name(GaussSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name_list_(GaussSqlParser.Any_name_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(GaussSqlParser.Any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrs(GaussSqlParser.AttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name_list(GaussSqlParser.Type_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncatestmt(GaussSqlParser.TruncatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestart_seqs_(GaussSqlParser.Restart_seqs_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentstmt(GaussSqlParser.CommentstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_table_stmt(GaussSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_column_stmt(GaussSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#comment_text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_text(GaussSqlParser.Comment_textContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeclabelstmt(GaussSqlParser.SeclabelstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#provider_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvider_(GaussSqlParser.Provider_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#security_label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity_label(GaussSqlParser.Security_labelContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetchstmt(GaussSqlParser.FetchstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_args(GaussSqlParser.Fetch_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#from_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_in(GaussSqlParser.From_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#from_in_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_in_(GaussSqlParser.From_in_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantstmt(GaussSqlParser.GrantstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokestmt(GaussSqlParser.RevokestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#privileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivileges(GaussSqlParser.PrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_list(GaussSqlParser.Privilege_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(GaussSqlParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_target(GaussSqlParser.Privilege_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee_list(GaussSqlParser.Grantee_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grantee}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee(GaussSqlParser.GranteeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_grant_option_(GaussSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantrolestmt(GaussSqlParser.GrantrolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokerolestmt(GaussSqlParser.RevokerolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_admin_option_(GaussSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGranted_by_(GaussSqlParser.Granted_by_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdefaultprivilegesstmt(GaussSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacloptionlist(GaussSqlParser.DefacloptionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#defacloption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacloption(GaussSqlParser.DefacloptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaclaction(GaussSqlParser.DefaclactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacl_privilege_target(GaussSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexstmt(GaussSqlParser.IndexstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#unique_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_(GaussSqlParser.Unique_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#single_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_name_(GaussSqlParser.Single_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcurrently_(GaussSqlParser.Concurrently_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_name_(GaussSqlParser.Index_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess_method_clause(GaussSqlParser.Access_method_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_params(GaussSqlParser.Index_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_elem_options(GaussSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_elem(GaussSqlParser.Index_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#include_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_(GaussSqlParser.Include_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_including_params(GaussSqlParser.Index_including_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#collate_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollate_(GaussSqlParser.Collate_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#class_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_(GaussSqlParser.Class_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsc_desc_(GaussSqlParser.Asc_desc_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNulls_order_(GaussSqlParser.Nulls_order_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunctionstmt(GaussSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_replace_(GaussSqlParser.Or_replace_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args(GaussSqlParser.Func_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_list(GaussSqlParser.Func_args_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_with_argtypes_list(GaussSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_with_argtypes(GaussSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_with_defaults(GaussSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_with_defaults_list(GaussSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg(GaussSqlParser.Func_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#arg_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_class(GaussSqlParser.Arg_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#param_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_name(GaussSqlParser.Param_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_return(GaussSqlParser.Func_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_type(GaussSqlParser.Func_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_with_default(GaussSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_arg(GaussSqlParser.Aggr_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_args(GaussSqlParser.Aggr_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_args_list(GaussSqlParser.Aggr_args_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_with_argtypes(GaussSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_with_argtypes_list(GaussSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunc_opt_list(GaussSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_func_opt_item(GaussSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body(GaussSqlParser.Func_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body_statement(GaussSqlParser.Func_body_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(GaussSqlParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_as}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_as(GaussSqlParser.Func_asContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransform_type_list(GaussSqlParser.Transform_type_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#definition_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition_(GaussSqlParser.Definition_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_func_column(GaussSqlParser.Table_func_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_func_column_list(GaussSqlParser.Table_func_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfunctionstmt(GaussSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfunc_opt_list(GaussSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#restrict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestrict_(GaussSqlParser.Restrict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemovefuncstmt(GaussSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveaggrstmt(GaussSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveoperstmt(GaussSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper_argtypes(GaussSqlParser.Oper_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#any_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_operator(GaussSqlParser.Any_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_with_argtypes_list(GaussSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_with_argtypes(GaussSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dostmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt(GaussSqlParser.DostmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt_opt_list(GaussSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt_opt_item(GaussSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatecaststmt(GaussSqlParser.CreatecaststmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cast_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast_context(GaussSqlParser.Cast_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropcaststmt(GaussSqlParser.DropcaststmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_exists_(GaussSqlParser.If_exists_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetransformstmt(GaussSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransform_element_list(GaussSqlParser.Transform_element_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptransformstmt(GaussSqlParser.DroptransformstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindexstmt(GaussSqlParser.ReindexstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_target_relation(GaussSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_target_all(GaussSqlParser.Reindex_target_allContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_option_list(GaussSqlParser.Reindex_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertblspcstmt(GaussSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenamestmt(GaussSqlParser.RenamestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_table_stmt(GaussSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_schema_stmt(GaussSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_database_stmt(GaussSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_column_stmt(GaussSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#column_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_(GaussSqlParser.Column_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_data_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_data_(GaussSqlParser.Set_data_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterobjectdependsstmt(GaussSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#no_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNo_(GaussSqlParser.No_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterobjectschemastmt(GaussSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroperatorstmt(GaussSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_list(GaussSqlParser.Operator_def_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_elem(GaussSqlParser.Operator_def_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_arg(GaussSqlParser.Operator_def_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertypestmt(GaussSqlParser.AltertypestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterownerstmt(GaussSqlParser.AlterownerstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatepublicationstmt(GaussSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_for_tables_(GaussSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_for_tables(GaussSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterpublicationstmt(GaussSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatesubscriptionstmt(GaussSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_name_list(GaussSqlParser.Publication_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_name_item(GaussSqlParser.Publication_name_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltersubscriptionstmt(GaussSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropsubscriptionstmt(GaussSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRulestmt(GaussSqlParser.RulestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionlist(GaussSqlParser.RuleactionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionmulti(GaussSqlParser.RuleactionmultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionstmt(GaussSqlParser.RuleactionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionstmtOrEmpty(GaussSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(GaussSqlParser.EventContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#instead_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstead_(GaussSqlParser.Instead_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotifystmt(GaussSqlParser.NotifystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotify_payload(GaussSqlParser.Notify_payloadContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListenstmt(GaussSqlParser.ListenstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlistenstmt(GaussSqlParser.UnlistenstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionstmt(GaussSqlParser.TransactionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transaction_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_(GaussSqlParser.Transaction_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_item(GaussSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_list(GaussSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_list_or_empty(GaussSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_chain_(GaussSqlParser.Transaction_chain_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewstmt(GaussSqlParser.ViewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#check_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_option_(GaussSqlParser.Check_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadstmt(GaussSqlParser.LoadstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedbstmt(GaussSqlParser.CreatedbstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_list(GaussSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_items(GaussSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_item(GaussSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_name(GaussSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#equal_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual_(GaussSqlParser.Equal_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdatabasestmt(GaussSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTablespaceName(GaussSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefresh_collation_version(GaussSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdatabasesetstmt(GaussSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropdbstmt(GaussSqlParser.DropdbstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropschemastmt(GaussSqlParser.DropschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_option_list(GaussSqlParser.Drop_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#drop_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_option(GaussSqlParser.Drop_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltercollationstmt(GaussSqlParser.AltercollationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltersystemstmt(GaussSqlParser.AltersystemstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedomainstmt(GaussSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdomainstmt(GaussSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#as_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAs_(GaussSqlParser.As_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertsdictionarystmt(GaussSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertsconfigurationstmt(GaussSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#any_with}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_with(GaussSqlParser.Any_withContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateconversionstmt(GaussSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterstmt(GaussSqlParser.ClusterstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_index_specification(GaussSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuumstmt(GaussSqlParser.VacuumstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzestmt(GaussSqlParser.AnalyzestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_list(GaussSqlParser.Utility_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_list(GaussSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_keyword(GaussSqlParser.Analyze_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_elem(GaussSqlParser.Utility_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_name(GaussSqlParser.Utility_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_arg(GaussSqlParser.Utility_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_elem(GaussSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_name(GaussSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_arg(GaussSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#analyze_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_(GaussSqlParser.Analyze_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#verbose_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerbose_(GaussSqlParser.Verbose_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#full_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_(GaussSqlParser.Full_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#freeze_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFreeze_(GaussSqlParser.Freeze_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#name_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_list_(GaussSqlParser.Name_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation(GaussSqlParser.Vacuum_relationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation_list(GaussSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation_list_(GaussSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainstmt(GaussSqlParser.ExplainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainablestmt(GaussSqlParser.ExplainablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_list(GaussSqlParser.Explain_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_elem(GaussSqlParser.Explain_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_name(GaussSqlParser.Explain_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_arg(GaussSqlParser.Explain_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreparestmt(GaussSqlParser.PreparestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrep_type_clause(GaussSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreparablestmt(GaussSqlParser.PreparablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#executestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecutestmt(GaussSqlParser.ExecutestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_param_clause(GaussSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocatestmt(GaussSqlParser.DeallocatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertstmt(GaussSqlParser.InsertstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#insert_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_target(GaussSqlParser.Insert_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_rest(GaussSqlParser.Insert_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#override_kind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverride_kind(GaussSqlParser.Override_kindContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_list(GaussSqlParser.Insert_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_item(GaussSqlParser.Insert_column_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_conflict_(GaussSqlParser.On_conflict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConf_expr_(GaussSqlParser.Conf_expr_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturning_clause(GaussSqlParser.Returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergestmt(GaussSqlParser.MergestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_insert_clause(GaussSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_clause(GaussSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_delete_clause(GaussSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeletestmt(GaussSqlParser.DeletestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_clause(GaussSqlParser.Using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockstmt(GaussSqlParser.LockstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#lock_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_(GaussSqlParser.Lock_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#lock_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_type(GaussSqlParser.Lock_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#nowait_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowait_(GaussSqlParser.Nowait_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowait_or_skip_(GaussSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdatestmt(GaussSqlParser.UpdatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_clause_list(GaussSqlParser.Set_clause_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_clause(GaussSqlParser.Set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_target(GaussSqlParser.Set_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_target_list(GaussSqlParser.Set_target_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarecursorstmt(GaussSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_name(GaussSqlParser.Cursor_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_options(GaussSqlParser.Cursor_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#hold_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHold_(GaussSqlParser.Hold_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectstmt(GaussSqlParser.SelectstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_with_parens(GaussSqlParser.Select_with_parensContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_no_parens(GaussSqlParser.Select_no_parensContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_clause(GaussSqlParser.Select_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_select_pramary(GaussSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#with_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause(GaussSqlParser.With_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cte_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_list(GaussSqlParser.Cte_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_table_expr(GaussSqlParser.Common_table_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#materialized_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaterialized_(GaussSqlParser.Materialized_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause_(GaussSqlParser.With_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause(GaussSqlParser.Into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#strict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrict_(GaussSqlParser.Strict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttempTableName(GaussSqlParser.OpttempTableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#table_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_(GaussSqlParser.Table_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_or_distinct(GaussSqlParser.All_or_distinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistinct_clause(GaussSqlParser.Distinct_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_clause_(GaussSqlParser.All_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_clause_(GaussSqlParser.Sort_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_clause(GaussSqlParser.Sort_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortby_list(GaussSqlParser.Sortby_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sortby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortby(GaussSqlParser.SortbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_limit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit(GaussSqlParser.Select_limitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit_(GaussSqlParser.Select_limit_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimit_clause(GaussSqlParser.Limit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffset_clause(GaussSqlParser.Offset_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit_value(GaussSqlParser.Select_limit_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_offset_value(GaussSqlParser.Select_offset_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_fetch_first_value(GaussSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitI_or_f_const(GaussSqlParser.I_or_f_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_or_rows(GaussSqlParser.Row_or_rowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFirst_or_next(GaussSqlParser.First_or_nextContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_clause(GaussSqlParser.Group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_list(GaussSqlParser.Group_by_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_item(GaussSqlParser.Group_by_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_grouping_set(GaussSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollup_clause(GaussSqlParser.Rollup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCube_clause(GaussSqlParser.Cube_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_clause(GaussSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(GaussSqlParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_clause(GaussSqlParser.For_locking_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_clause_(GaussSqlParser.For_locking_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_items(GaussSqlParser.For_locking_itemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_item(GaussSqlParser.For_locking_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_strength(GaussSqlParser.For_locking_strengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocked_rels_list(GaussSqlParser.Locked_rels_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_clause(GaussSqlParser.Values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_clause(GaussSqlParser.From_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#from_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_list(GaussSqlParser.From_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref(GaussSqlParser.Table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#cross_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCross_join(GaussSqlParser.Cross_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#normal_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormal_join(GaussSqlParser.Normal_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#natural_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNatural_join(GaussSqlParser.Natural_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias_clause(GaussSqlParser.Alias_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_alias_clause(GaussSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_type(GaussSqlParser.Join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#join_qual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_qual(GaussSqlParser.Join_qualContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr(GaussSqlParser.Relation_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr_list(GaussSqlParser.Relation_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr_opt_alias(GaussSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablesample_clause(GaussSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatable_clause_(GaussSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_table(GaussSqlParser.Func_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsfrom_item(GaussSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsfrom_list(GaussSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_def_list_(GaussSqlParser.Col_def_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdinality_(GaussSqlParser.Ordinality_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(GaussSqlParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_or_current_clause(GaussSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablefuncelementlist(GaussSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablefuncelementlist(GaussSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablefuncelement(GaussSqlParser.TablefuncelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmltable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable(GaussSqlParser.XmltableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_list(GaussSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_el(GaussSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_option_list(GaussSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_option_el(GaussSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespace_list(GaussSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespace_el(GaussSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#typename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypename(GaussSqlParser.TypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpt_array_bounds(GaussSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpletypename(GaussSqlParser.SimpletypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#consttypename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConsttypename(GaussSqlParser.ConsttypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#generictype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenerictype(GaussSqlParser.GenerictypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_modifiers_(GaussSqlParser.Type_modifiers_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(GaussSqlParser.NumericContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#float_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_(GaussSqlParser.Float_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBit(GaussSqlParser.BitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constbit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstbit(GaussSqlParser.ConstbitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwithlength(GaussSqlParser.BitwithlengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwithoutlength(GaussSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(GaussSqlParser.CharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstcharacter(GaussSqlParser.ConstcharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#character_c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter_c(GaussSqlParser.Character_cContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#varying_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarying_(GaussSqlParser.Varying_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstdatetime(GaussSqlParser.ConstdatetimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#constinterval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstinterval(GaussSqlParser.ConstintervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#timezone_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimezone_(GaussSqlParser.Timezone_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#interval_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_(GaussSqlParser.Interval_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#interval_second}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_second(GaussSqlParser.Interval_secondContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#jsonType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonType(GaussSqlParser.JsonTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#escape_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscape_(GaussSqlParser.Escape_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr(GaussSqlParser.A_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_qual(GaussSqlParser.A_expr_qualContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_lessless(GaussSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_or(GaussSqlParser.A_expr_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_and(GaussSqlParser.A_expr_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_between(GaussSqlParser.A_expr_betweenContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_in(GaussSqlParser.A_expr_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_not(GaussSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_isnull(GaussSqlParser.A_expr_isnullContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_is_not(GaussSqlParser.A_expr_is_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_compare(GaussSqlParser.A_expr_compareContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_like(GaussSqlParser.A_expr_likeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_qual_op(GaussSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_qualop(GaussSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_add(GaussSqlParser.A_expr_addContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_mul(GaussSqlParser.A_expr_mulContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_caret(GaussSqlParser.A_expr_caretContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_sign(GaussSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_at_time_zone(GaussSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_collate(GaussSqlParser.A_expr_collateContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_typecast(GaussSqlParser.A_expr_typecastContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#b_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitB_expr(GaussSqlParser.B_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#c_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_expr(GaussSqlParser.C_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlsqlvariablename(GaussSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_application}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_application(GaussSqlParser.Func_applicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#star_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar_context(GaussSqlParser.Star_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr(GaussSqlParser.Func_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr_windowless(GaussSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr_common_subexpr(GaussSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_root_version(GaussSqlParser.Xml_root_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_root_standalone_(GaussSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attributes(GaussSqlParser.Xml_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attribute_list(GaussSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attribute_el(GaussSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument_or_content(GaussSqlParser.Document_or_contentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_whitespace_option(GaussSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlexists_argument(GaussSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_passing_mech(GaussSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_group_clause(GaussSqlParser.Within_group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clause(GaussSqlParser.Filter_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#window_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_clause(GaussSqlParser.Window_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_definition_list(GaussSqlParser.Window_definition_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#window_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_definition(GaussSqlParser.Window_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause(GaussSqlParser.Over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#window_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_specification(GaussSqlParser.Window_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExisting_window_name_(GaussSqlParser.Existing_window_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_clause_(GaussSqlParser.Partition_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_clause_(GaussSqlParser.Frame_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_extent(GaussSqlParser.Frame_extentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_bound(GaussSqlParser.Frame_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_exclusion_clause_(GaussSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow(GaussSqlParser.RowContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicit_row(GaussSqlParser.Explicit_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicit_row(GaussSqlParser.Implicit_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sub_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_type(GaussSqlParser.Sub_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#all_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_op(GaussSqlParser.All_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#mathop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathop(GaussSqlParser.MathopContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#qual_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQual_op(GaussSqlParser.Qual_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQual_all_op(GaussSqlParser.Qual_all_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_Op(GaussSqlParser.Subquery_OpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(GaussSqlParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_list(GaussSqlParser.Func_arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_expr(GaussSqlParser.Func_arg_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_list(GaussSqlParser.Type_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#array_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_expr(GaussSqlParser.Array_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_expr_list(GaussSqlParser.Array_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#extract_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtract_list(GaussSqlParser.Extract_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtract_arg(GaussSqlParser.Extract_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnicode_normal_form(GaussSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverlay_list(GaussSqlParser.Overlay_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#position_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPosition_list(GaussSqlParser.Position_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#substr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstr_list(GaussSqlParser.Substr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#trim_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrim_list(GaussSqlParser.Trim_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_expr_select(GaussSqlParser.In_expr_selectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link GaussSqlParser#in_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_expr_list(GaussSqlParser.In_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#case_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expr(GaussSqlParser.Case_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen_clause_list(GaussSqlParser.When_clause_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#when_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen_clause(GaussSqlParser.When_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#case_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_default(GaussSqlParser.Case_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#case_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_arg(GaussSqlParser.Case_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#columnref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnref(GaussSqlParser.ColumnrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirection_el(GaussSqlParser.Indirection_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlice_bound_(GaussSqlParser.Slice_bound_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#indirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirection(GaussSqlParser.IndirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpt_indirection(GaussSqlParser.Opt_indirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_passing_clause(GaussSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_arguments(GaussSqlParser.Json_argumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_argument(GaussSqlParser.Json_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_wrapper_behavior(GaussSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior(GaussSqlParser.Json_behaviorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior_type(GaussSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior_clause(GaussSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_on_error_clause(GaussSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_expr(GaussSqlParser.Json_value_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_format_clause(GaussSqlParser.Json_format_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_quotes_clause(GaussSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_returning_clause(GaussSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_predicate_type_constraint(GaussSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_key_uniqueness_constraint(GaussSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_name_and_value_list(GaussSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_name_and_value(GaussSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_constructor_null_clause(GaussSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_constructor_null_clause(GaussSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_expr_list(GaussSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_aggregate_func(GaussSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_aggregate_order_by_clause(GaussSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#target_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_list_(GaussSqlParser.Target_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#target_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_list(GaussSqlParser.Target_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#target_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_el(GaussSqlParser.Target_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#target_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_alias(GaussSqlParser.Target_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name_list(GaussSqlParser.Qualified_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name(GaussSqlParser.Qualified_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_list(GaussSqlParser.Name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(GaussSqlParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#attr_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttr_name(GaussSqlParser.Attr_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#file_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_name(GaussSqlParser.File_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#func_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_name(GaussSqlParser.Func_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAexprconst(GaussSqlParser.AexprconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#xconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXconst(GaussSqlParser.XconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBconst(GaussSqlParser.BconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#fconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFconst(GaussSqlParser.FconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#iconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIconst(GaussSqlParser.IconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#sconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSconst(GaussSqlParser.SconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#anysconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnysconst(GaussSqlParser.AnysconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#uescape_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUescape_(GaussSqlParser.Uescape_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#signediconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignediconst(GaussSqlParser.SignediconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#roleid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleid(GaussSqlParser.RoleidContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#rolespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRolespec(GaussSqlParser.RolespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#role_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_list(GaussSqlParser.Role_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#colid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColid(GaussSqlParser.ColidContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_function_name(GaussSqlParser.Type_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonreservedword(GaussSqlParser.NonreservedwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#colLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColLabel(GaussSqlParser.ColLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBareColLabel(GaussSqlParser.BareColLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnreserved_keyword(GaussSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_name_keyword(GaussSqlParser.Col_name_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_func_name_keyword(GaussSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReserved_keyword(GaussSqlParser.Reserved_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBare_label_keyword(GaussSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_identifier(GaussSqlParser.Any_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GaussSqlParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(GaussSqlParser.IdentifierContext ctx);
}