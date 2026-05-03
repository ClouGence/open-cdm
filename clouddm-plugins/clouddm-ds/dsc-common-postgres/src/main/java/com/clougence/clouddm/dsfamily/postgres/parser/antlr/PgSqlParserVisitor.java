// Generated from PgSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.dsfamily.postgres.parser.antlr;

import com.clougence.clouddm.dsfamily.postgres.parser.base.PgSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PgSqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PgSqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(PgSqlParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#stmtblock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtblock(PgSqlParser.StmtblockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#stmtmulti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtmulti(PgSqlParser.StmtmultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(PgSqlParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#callstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallstmt(PgSqlParser.CallstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreaterolestmt(PgSqlParser.CreaterolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#with_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_(PgSqlParser.With_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optrolelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptrolelist(PgSqlParser.OptrolelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alteroptrolelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroptrolelist(PgSqlParser.AlteroptrolelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alteroptroleelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroptroleelem(PgSqlParser.AlteroptroleelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createoptroleelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateoptroleelem(PgSqlParser.CreateoptroleelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createuserstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateuserstmt(PgSqlParser.CreateuserstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterrolestmt(PgSqlParser.AlterrolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#in_database_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_database_(PgSqlParser.In_database_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterrolesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterrolesetstmt(PgSqlParser.AlterrolesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#droprolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroprolestmt(PgSqlParser.DroprolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropuserstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropuserstmt(PgSqlParser.DropuserstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#creategroupstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreategroupstmt(PgSqlParser.CreategroupstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altergroupstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltergroupstmt(PgSqlParser.AltergroupstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#add_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_drop(PgSqlParser.Add_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateschemastmt(PgSqlParser.CreateschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optschemaname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptschemaname(PgSqlParser.OptschemanameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optschemaeltlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptschemaeltlist(PgSqlParser.OptschemaeltlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#schema_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_stmt(PgSqlParser.Schema_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#variablesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariablesetstmt(PgSqlParser.VariablesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_rest(PgSqlParser.Set_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_set(PgSqlParser.Generic_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_rest_more}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_rest_more(PgSqlParser.Set_rest_moreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#var_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_name(PgSqlParser.Var_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#var_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_list(PgSqlParser.Var_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#var_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_value(PgSqlParser.Var_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#iso_level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIso_level(PgSqlParser.Iso_levelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#boolean_or_string_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_or_string_(PgSqlParser.Boolean_or_string_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#zone_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZone_value(PgSqlParser.Zone_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#encoding_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncoding_(PgSqlParser.Encoding_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#nonreservedword_or_sconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonreservedword_or_sconst(PgSqlParser.Nonreservedword_or_sconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#variableresetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableresetstmt(PgSqlParser.VariableresetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reset_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReset_rest(PgSqlParser.Reset_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_reset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_reset(PgSqlParser.Generic_resetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#setresetclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetresetclause(PgSqlParser.SetresetclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#functionsetresetclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionsetresetclause(PgSqlParser.FunctionsetresetclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#variableshowstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableshowstmt(PgSqlParser.VariableshowstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraintssetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintssetstmt(PgSqlParser.ConstraintssetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraints_set_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints_set_list(PgSqlParser.Constraints_set_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraints_set_mode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints_set_mode(PgSqlParser.Constraints_set_modeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#checkpointstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckpointstmt(PgSqlParser.CheckpointstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#discardstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscardstmt(PgSqlParser.DiscardstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altertablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertablestmt(PgSqlParser.AltertablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_table_cmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_cmds(PgSqlParser.Alter_table_cmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#partition_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_cmd(PgSqlParser.Partition_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_partition_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partition_cmd(PgSqlParser.Index_partition_cmdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumn(PgSqlParser.AddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterColumn(PgSqlParser.AlterColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropColumn(PgSqlParser.DropColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddConstraint(PgSqlParser.AddConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterConstaint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterConstaint(PgSqlParser.AlterConstaintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code validateConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidateConstraint(PgSqlParser.ValidateConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropConstraint(PgSqlParser.DropConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setWithoutOids}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetWithoutOids(PgSqlParser.SetWithoutOidsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code clusterOnName}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterOnName(PgSqlParser.ClusterOnNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setWithOut}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetWithOut(PgSqlParser.SetWithOutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unsupportAlterTableStatement}
	 * labeled alternative in {@link PgSqlParser#alter_table_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsupportAlterTableStatement(PgSqlParser.UnsupportAlterTableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_column_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_column_default(PgSqlParser.Alter_column_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#drop_behavior_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_behavior_(PgSqlParser.Drop_behavior_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#collate_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollate_clause_(PgSqlParser.Collate_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_using}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_using(PgSqlParser.Alter_usingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#replica_identity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplica_identity(PgSqlParser.Replica_identityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reloptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloptions(PgSqlParser.ReloptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reloptions_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloptions_(PgSqlParser.Reloptions_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reloption_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloption_list(PgSqlParser.Reloption_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reloption_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReloption_elem(PgSqlParser.Reloption_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_identity_column_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identity_column_option_list(PgSqlParser.Alter_identity_column_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_identity_column_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identity_column_option(PgSqlParser.Alter_identity_column_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#partitionboundspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionboundspec(PgSqlParser.PartitionboundspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#hash_partbound_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partbound_elem(PgSqlParser.Hash_partbound_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#hash_partbound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partbound(PgSqlParser.Hash_partboundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altercompositetypestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltercompositetypestmt(PgSqlParser.AltercompositetypestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_type_cmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type_cmds(PgSqlParser.Alter_type_cmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_type_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type_cmd(PgSqlParser.Alter_type_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#closeportalstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloseportalstmt(PgSqlParser.CloseportalstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopystmt(PgSqlParser.CopystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_from(PgSqlParser.Copy_fromContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#program_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram_(PgSqlParser.Program_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_file_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_file_name(PgSqlParser.Copy_file_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_options(PgSqlParser.Copy_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_opt_list(PgSqlParser.Copy_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_opt_item(PgSqlParser.Copy_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#binary_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_(PgSqlParser.Binary_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_delimiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_delimiter(PgSqlParser.Copy_delimiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#using_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_(PgSqlParser.Using_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_generic_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_list(PgSqlParser.Copy_generic_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_generic_opt_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_elem(PgSqlParser.Copy_generic_opt_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg(PgSqlParser.Copy_generic_opt_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg_list(PgSqlParser.Copy_generic_opt_arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#copy_generic_opt_arg_list_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy_generic_opt_arg_list_item(PgSqlParser.Copy_generic_opt_arg_list_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatestmt(PgSqlParser.CreatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttemp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttemp(PgSqlParser.OpttempContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttableelementlist(PgSqlParser.OpttableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttypedtableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttypedtableelementlist(PgSqlParser.OpttypedtableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableelementlist(PgSqlParser.TableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#typedtableelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedtableelementlist(PgSqlParser.TypedtableelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tableelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableelement(PgSqlParser.TableelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#typedtableelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedtableelement(PgSqlParser.TypedtableelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#columnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDef(PgSqlParser.ColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#columnOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnOptions(PgSqlParser.ColumnOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#colquallist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColquallist(PgSqlParser.ColquallistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#colconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColconstraint(PgSqlParser.ColconstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintNotNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintNotNull(PgSqlParser.ConstraintNotNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintNull}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintNull(PgSqlParser.ConstraintNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintUnique}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintUnique(PgSqlParser.ConstraintUniqueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintPrimary}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintPrimary(PgSqlParser.ConstraintPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintCheck(PgSqlParser.ConstraintCheckContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintDefault}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintDefault(PgSqlParser.ConstraintDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintGenerated}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintGenerated(PgSqlParser.ConstraintGeneratedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintReference}
	 * labeled alternative in {@link PgSqlParser#colconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintReference(PgSqlParser.ConstraintReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generated_when}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenerated_when(PgSqlParser.Generated_whenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraintattr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattr(PgSqlParser.ConstraintattrContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablelikeclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeclause(PgSqlParser.TablelikeclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablelikeoptionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeoptionlist(PgSqlParser.TablelikeoptionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablelikeoption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablelikeoption(PgSqlParser.TablelikeoptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tableconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableconstraint(PgSqlParser.TableconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintelem(PgSqlParser.ConstraintelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#no_inherit_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNo_inherit_(PgSqlParser.No_inherit_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#column_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_list_(PgSqlParser.Column_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#columnlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnlist(PgSqlParser.ColumnlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#columnElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnElem(PgSqlParser.ColumnElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#c_include_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_include_(PgSqlParser.C_include_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#key_match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_match(PgSqlParser.Key_matchContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#exclusionconstraintlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionconstraintlist(PgSqlParser.ExclusionconstraintlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#exclusionconstraintelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionconstraintelem(PgSqlParser.ExclusionconstraintelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#exclusionwhereclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionwhereclause(PgSqlParser.ExclusionwhereclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#key_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_actions(PgSqlParser.Key_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#key_update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_update(PgSqlParser.Key_updateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#key_delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_delete(PgSqlParser.Key_deleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#key_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_action(PgSqlParser.Key_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optinherit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptinherit(PgSqlParser.OptinheritContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optpartitionspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptpartitionspec(PgSqlParser.OptpartitionspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#partitionspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionspec(PgSqlParser.PartitionspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#partition_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_item(PgSqlParser.Partition_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#part_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPart_params(PgSqlParser.Part_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#part_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPart_elem(PgSqlParser.Part_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#table_access_method_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_access_method_clause(PgSqlParser.Table_access_method_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optwith}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptwith(PgSqlParser.OptwithContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#oncommitoption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOncommitoption(PgSqlParser.OncommitoptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablespace(PgSqlParser.OpttablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optconstablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptconstablespace(PgSqlParser.OptconstablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#existingindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistingindex(PgSqlParser.ExistingindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createstatsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatestatsstmt(PgSqlParser.CreatestatsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterstatsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterstatsstmt(PgSqlParser.AlterstatsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createasstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateasstmt(PgSqlParser.CreateasstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#create_as_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_as_target(PgSqlParser.Create_as_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#with_data_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_data_(PgSqlParser.With_data_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#creatematviewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatematviewstmt(PgSqlParser.CreatematviewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#create_mv_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_mv_target(PgSqlParser.Create_mv_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optnolog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptnolog(PgSqlParser.OptnologContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#refreshmatviewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshmatviewstmt(PgSqlParser.RefreshmatviewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createseqstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateseqstmt(PgSqlParser.CreateseqstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterseqstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterseqstmt(PgSqlParser.AlterseqstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optseqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptseqoptlist(PgSqlParser.OptseqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optparenthesizedseqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptparenthesizedseqoptlist(PgSqlParser.OptparenthesizedseqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#seqoptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqoptlist(PgSqlParser.SeqoptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#seqoptelem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqoptelem(PgSqlParser.SeqoptelemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#by_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBy_(PgSqlParser.By_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#numericonly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericonly(PgSqlParser.NumericonlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#numericonly_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericonly_list(PgSqlParser.Numericonly_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createplangstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateplangstmt(PgSqlParser.CreateplangstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#trusted_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrusted_(PgSqlParser.Trusted_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#handler_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandler_name(PgSqlParser.Handler_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#inline_handler_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_handler_(PgSqlParser.Inline_handler_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#validator_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidator_clause(PgSqlParser.Validator_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#validator_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidator_(PgSqlParser.Validator_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#procedural_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedural_(PgSqlParser.Procedural_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createtablespacestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetablespacestmt(PgSqlParser.CreatetablespacestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttablespaceowner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablespaceowner(PgSqlParser.OpttablespaceownerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#droptablespacestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptablespacestmt(PgSqlParser.DroptablespacestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createextensionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateextensionstmt(PgSqlParser.CreateextensionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#create_extension_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_extension_opt_list(PgSqlParser.Create_extension_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#create_extension_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_extension_opt_item(PgSqlParser.Create_extension_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterextensionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterextensionstmt(PgSqlParser.AlterextensionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_extension_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_extension_opt_list(PgSqlParser.Alter_extension_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_extension_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_extension_opt_item(PgSqlParser.Alter_extension_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterextensioncontentsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterextensioncontentsstmt(PgSqlParser.AlterextensioncontentsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createfdwstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefdwstmt(PgSqlParser.CreatefdwstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fdw_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_option(PgSqlParser.Fdw_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fdw_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_options(PgSqlParser.Fdw_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fdw_options_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFdw_options_(PgSqlParser.Fdw_options_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterfdwstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfdwstmt(PgSqlParser.AlterfdwstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#create_generic_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_generic_options(PgSqlParser.Create_generic_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_list(PgSqlParser.Generic_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_generic_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_options(PgSqlParser.Alter_generic_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_generic_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_option_list(PgSqlParser.Alter_generic_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alter_generic_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_generic_option_elem(PgSqlParser.Alter_generic_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_elem(PgSqlParser.Generic_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_name(PgSqlParser.Generic_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generic_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric_option_arg(PgSqlParser.Generic_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createforeignserverstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateforeignserverstmt(PgSqlParser.CreateforeignserverstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(PgSqlParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#foreign_server_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_server_version(PgSqlParser.Foreign_server_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#foreign_server_version_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_server_version_(PgSqlParser.Foreign_server_version_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterforeignserverstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterforeignserverstmt(PgSqlParser.AlterforeignserverstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createforeigntablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateforeigntablestmt(PgSqlParser.CreateforeigntablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#importforeignschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportforeignschemastmt(PgSqlParser.ImportforeignschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#import_qualification_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_qualification_type(PgSqlParser.Import_qualification_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#import_qualification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_qualification(PgSqlParser.Import_qualificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateusermappingstmt(PgSqlParser.CreateusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#auth_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuth_ident(PgSqlParser.Auth_identContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropusermappingstmt(PgSqlParser.DropusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterusermappingstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterusermappingstmt(PgSqlParser.AlterusermappingstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createpolicystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatepolicystmt(PgSqlParser.CreatepolicystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterpolicystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterpolicystmt(PgSqlParser.AlterpolicystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecurityoptionalexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionalexpr(PgSqlParser.RowsecurityoptionalexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecurityoptionalwithcheck}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionalwithcheck(PgSqlParser.RowsecurityoptionalwithcheckContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecuritydefaulttorole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaulttorole(PgSqlParser.RowsecuritydefaulttoroleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecurityoptionaltorole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecurityoptionaltorole(PgSqlParser.RowsecurityoptionaltoroleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecuritydefaultpermissive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaultpermissive(PgSqlParser.RowsecuritydefaultpermissiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsecuritydefaultforcmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsecuritydefaultforcmd(PgSqlParser.RowsecuritydefaultforcmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#row_security_cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_security_cmd(PgSqlParser.Row_security_cmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createamstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateamstmt(PgSqlParser.CreateamstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#am_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAm_type(PgSqlParser.Am_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetrigstmt(PgSqlParser.CreatetrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggeractiontime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggeractiontime(PgSqlParser.TriggeractiontimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerevents}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerevents(PgSqlParser.TriggereventsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggeroneevent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggeroneevent(PgSqlParser.TriggeroneeventContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerreferencing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerreferencing(PgSqlParser.TriggerreferencingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggertransitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggertransitions(PgSqlParser.TriggertransitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggertransition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggertransition(PgSqlParser.TriggertransitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transitionoldornew}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionoldornew(PgSqlParser.TransitionoldornewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transitionrowortable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionrowortable(PgSqlParser.TransitionrowortableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transitionrelname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionrelname(PgSqlParser.TransitionrelnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerforspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerforspec(PgSqlParser.TriggerforspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerforopteach}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerforopteach(PgSqlParser.TriggerforopteachContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerfortype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfortype(PgSqlParser.TriggerfortypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerwhen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerwhen(PgSqlParser.TriggerwhenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#function_or_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_or_procedure(PgSqlParser.Function_or_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerfuncargs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfuncargs(PgSqlParser.TriggerfuncargsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#triggerfuncarg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerfuncarg(PgSqlParser.TriggerfuncargContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#optconstrfromtable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptconstrfromtable(PgSqlParser.OptconstrfromtableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraintattributespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattributespec(PgSqlParser.ConstraintattributespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constraintattributeElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintattributeElem(PgSqlParser.ConstraintattributeElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createeventtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateeventtrigstmt(PgSqlParser.CreateeventtrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#event_trigger_when_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_when_list(PgSqlParser.Event_trigger_when_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#event_trigger_when_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_when_item(PgSqlParser.Event_trigger_when_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#event_trigger_value_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_trigger_value_list(PgSqlParser.Event_trigger_value_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altereventtrigstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltereventtrigstmt(PgSqlParser.AltereventtrigstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#enable_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_trigger(PgSqlParser.Enable_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createassertionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateassertionstmt(PgSqlParser.CreateassertionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#definestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinestmt(PgSqlParser.DefinestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(PgSqlParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#def_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_list(PgSqlParser.Def_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#def_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_elem(PgSqlParser.Def_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#def_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_arg(PgSqlParser.Def_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#old_aggr_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_definition(PgSqlParser.Old_aggr_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#old_aggr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_list(PgSqlParser.Old_aggr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#old_aggr_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_aggr_elem(PgSqlParser.Old_aggr_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#enum_val_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnum_val_list_(PgSqlParser.Enum_val_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#enum_val_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnum_val_list(PgSqlParser.Enum_val_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterenumstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterenumstmt(PgSqlParser.AlterenumstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#if_not_exists_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_not_exists_(PgSqlParser.If_not_exists_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createopclassstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateopclassstmt(PgSqlParser.CreateopclassstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opclass_item_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_item_list(PgSqlParser.Opclass_item_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opclass_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_item(PgSqlParser.Opclass_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#default_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_(PgSqlParser.Default_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opfamily_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpfamily_(PgSqlParser.Opfamily_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opclass_purpose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_purpose(PgSqlParser.Opclass_purposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#recheck_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecheck_(PgSqlParser.Recheck_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createopfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateopfamilystmt(PgSqlParser.CreateopfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alteropfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteropfamilystmt(PgSqlParser.AlteropfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opclass_drop_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_drop_list(PgSqlParser.Opclass_drop_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opclass_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpclass_drop(PgSqlParser.Opclass_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropopclassstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropopclassstmt(PgSqlParser.DropopclassstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropopfamilystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropopfamilystmt(PgSqlParser.DropopfamilystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropownedstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropownedstmt(PgSqlParser.DropownedstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reassignownedstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReassignownedstmt(PgSqlParser.ReassignownedstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropstmt(PgSqlParser.DropstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#droptablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptablestmt(PgSqlParser.DroptablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#object_type_any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_any_name(PgSqlParser.Object_type_any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#object_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_name(PgSqlParser.Object_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#drop_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_type_name(PgSqlParser.Drop_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#object_type_name_on_any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_name_on_any_name(PgSqlParser.Object_type_name_on_any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#any_name_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name_list_(PgSqlParser.Any_name_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(PgSqlParser.Any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrs(PgSqlParser.AttrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name_list(PgSqlParser.Type_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#truncatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncatestmt(PgSqlParser.TruncatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#restart_seqs_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestart_seqs_(PgSqlParser.Restart_seqs_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#commentstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentstmt(PgSqlParser.CommentstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#comment_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_table_stmt(PgSqlParser.Comment_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#comment_column_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_column_stmt(PgSqlParser.Comment_column_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#comment_text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_text(PgSqlParser.Comment_textContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#seclabelstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeclabelstmt(PgSqlParser.SeclabelstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#provider_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvider_(PgSqlParser.Provider_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#security_label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity_label(PgSqlParser.Security_labelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fetchstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetchstmt(PgSqlParser.FetchstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fetch_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_args(PgSqlParser.Fetch_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#from_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_in(PgSqlParser.From_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#from_in_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_in_(PgSqlParser.From_in_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grantstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantstmt(PgSqlParser.GrantstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#revokestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokestmt(PgSqlParser.RevokestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#privileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivileges(PgSqlParser.PrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#privilege_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_list(PgSqlParser.Privilege_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(PgSqlParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#privilege_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_target(PgSqlParser.Privilege_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grantee_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee_list(PgSqlParser.Grantee_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grantee}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee(PgSqlParser.GranteeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grant_grant_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_grant_option_(PgSqlParser.Grant_grant_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grantrolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantrolestmt(PgSqlParser.GrantrolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#revokerolestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokerolestmt(PgSqlParser.RevokerolestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grant_admin_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_admin_option_(PgSqlParser.Grant_admin_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#granted_by_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGranted_by_(PgSqlParser.Granted_by_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterdefaultprivilegesstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdefaultprivilegesstmt(PgSqlParser.AlterdefaultprivilegesstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#defacloptionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacloptionlist(PgSqlParser.DefacloptionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#defacloption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacloption(PgSqlParser.DefacloptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#defaclaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaclaction(PgSqlParser.DefaclactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#defacl_privilege_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefacl_privilege_target(PgSqlParser.Defacl_privilege_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#indexstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexstmt(PgSqlParser.IndexstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#unique_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_(PgSqlParser.Unique_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#single_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_name_(PgSqlParser.Single_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#concurrently_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcurrently_(PgSqlParser.Concurrently_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_name_(PgSqlParser.Index_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#access_method_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess_method_clause(PgSqlParser.Access_method_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_params(PgSqlParser.Index_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_elem_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_elem_options(PgSqlParser.Index_elem_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_elem(PgSqlParser.Index_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#include_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_(PgSqlParser.Include_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#index_including_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_including_params(PgSqlParser.Index_including_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#collate_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollate_(PgSqlParser.Collate_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#class_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_(PgSqlParser.Class_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#asc_desc_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsc_desc_(PgSqlParser.Asc_desc_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#nulls_order_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNulls_order_(PgSqlParser.Nulls_order_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createfunctionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunctionstmt(PgSqlParser.CreatefunctionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#or_replace_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_replace_(PgSqlParser.Or_replace_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args(PgSqlParser.Func_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_args_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_list(PgSqlParser.Func_args_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#function_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_with_argtypes_list(PgSqlParser.Function_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#function_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_with_argtypes(PgSqlParser.Function_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_args_with_defaults}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_with_defaults(PgSqlParser.Func_args_with_defaultsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_args_with_defaults_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args_with_defaults_list(PgSqlParser.Func_args_with_defaults_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg(PgSqlParser.Func_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#arg_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_class(PgSqlParser.Arg_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#param_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_name(PgSqlParser.Param_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_return(PgSqlParser.Func_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_type(PgSqlParser.Func_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_arg_with_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_with_default(PgSqlParser.Func_arg_with_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aggr_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_arg(PgSqlParser.Aggr_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aggr_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_args(PgSqlParser.Aggr_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aggr_args_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggr_args_list(PgSqlParser.Aggr_args_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_with_argtypes(PgSqlParser.Aggregate_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aggregate_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_with_argtypes_list(PgSqlParser.Aggregate_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createfunc_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunc_opt_list(PgSqlParser.Createfunc_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#common_func_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_func_opt_item(PgSqlParser.Common_func_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createfunc_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatefunc_opt_item(PgSqlParser.Createfunc_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body(PgSqlParser.Func_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_body_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body_statement(PgSqlParser.Func_body_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(PgSqlParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_as}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_as(PgSqlParser.Func_asContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transform_type_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransform_type_list(PgSqlParser.Transform_type_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#definition_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition_(PgSqlParser.Definition_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#table_func_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_func_column(PgSqlParser.Table_func_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#table_func_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_func_column_list(PgSqlParser.Table_func_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterfunctionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfunctionstmt(PgSqlParser.AlterfunctionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterfunc_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterfunc_opt_list(PgSqlParser.Alterfunc_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#restrict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestrict_(PgSqlParser.Restrict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#removefuncstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemovefuncstmt(PgSqlParser.RemovefuncstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#removeaggrstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveaggrstmt(PgSqlParser.RemoveaggrstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#removeoperstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveoperstmt(PgSqlParser.RemoveoperstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#oper_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOper_argtypes(PgSqlParser.Oper_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#any_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_operator(PgSqlParser.Any_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#operator_with_argtypes_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_with_argtypes_list(PgSqlParser.Operator_with_argtypes_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#operator_with_argtypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_with_argtypes(PgSqlParser.Operator_with_argtypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dostmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt(PgSqlParser.DostmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dostmt_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt_opt_list(PgSqlParser.Dostmt_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dostmt_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDostmt_opt_item(PgSqlParser.Dostmt_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createcaststmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatecaststmt(PgSqlParser.CreatecaststmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cast_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast_context(PgSqlParser.Cast_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropcaststmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropcaststmt(PgSqlParser.DropcaststmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#if_exists_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_exists_(PgSqlParser.If_exists_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createtransformstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatetransformstmt(PgSqlParser.CreatetransformstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transform_element_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransform_element_list(PgSqlParser.Transform_element_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#droptransformstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDroptransformstmt(PgSqlParser.DroptransformstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reindexstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindexstmt(PgSqlParser.ReindexstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reindex_target_relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_target_relation(PgSqlParser.Reindex_target_relationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reindex_target_all}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_target_all(PgSqlParser.Reindex_target_allContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reindex_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_option_list(PgSqlParser.Reindex_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altertblspcstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertblspcstmt(PgSqlParser.AltertblspcstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#renamestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenamestmt(PgSqlParser.RenamestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rename_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_table_stmt(PgSqlParser.Rename_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rename_schema_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_schema_stmt(PgSqlParser.Rename_schema_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rename_database_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_database_stmt(PgSqlParser.Rename_database_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rename_column_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_column_stmt(PgSqlParser.Rename_column_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#column_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_(PgSqlParser.Column_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_data_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_data_(PgSqlParser.Set_data_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterobjectdependsstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterobjectdependsstmt(PgSqlParser.AlterobjectdependsstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#no_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNo_(PgSqlParser.No_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterobjectschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterobjectschemastmt(PgSqlParser.AlterobjectschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alteroperatorstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlteroperatorstmt(PgSqlParser.AlteroperatorstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#operator_def_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_list(PgSqlParser.Operator_def_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#operator_def_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_elem(PgSqlParser.Operator_def_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#operator_def_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_def_arg(PgSqlParser.Operator_def_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altertypestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertypestmt(PgSqlParser.AltertypestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterownerstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterownerstmt(PgSqlParser.AlterownerstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createpublicationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatepublicationstmt(PgSqlParser.CreatepublicationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#publication_for_tables_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_for_tables_(PgSqlParser.Publication_for_tables_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#publication_for_tables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_for_tables(PgSqlParser.Publication_for_tablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterpublicationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterpublicationstmt(PgSqlParser.AlterpublicationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createsubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatesubscriptionstmt(PgSqlParser.CreatesubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#publication_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_name_list(PgSqlParser.Publication_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#publication_name_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPublication_name_item(PgSqlParser.Publication_name_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altersubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltersubscriptionstmt(PgSqlParser.AltersubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropsubscriptionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropsubscriptionstmt(PgSqlParser.DropsubscriptionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rulestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRulestmt(PgSqlParser.RulestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#ruleactionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionlist(PgSqlParser.RuleactionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#ruleactionmulti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionmulti(PgSqlParser.RuleactionmultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#ruleactionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionstmt(PgSqlParser.RuleactionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#ruleactionstmtOrEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleactionstmtOrEmpty(PgSqlParser.RuleactionstmtOrEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(PgSqlParser.EventContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#instead_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstead_(PgSqlParser.Instead_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#notifystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotifystmt(PgSqlParser.NotifystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#notify_payload}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotify_payload(PgSqlParser.Notify_payloadContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#listenstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListenstmt(PgSqlParser.ListenstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#unlistenstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlistenstmt(PgSqlParser.UnlistenstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transactionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionstmt(PgSqlParser.TransactionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transaction_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_(PgSqlParser.Transaction_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transaction_mode_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_item(PgSqlParser.Transaction_mode_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transaction_mode_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_list(PgSqlParser.Transaction_mode_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transaction_mode_list_or_empty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_mode_list_or_empty(PgSqlParser.Transaction_mode_list_or_emptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#transaction_chain_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_chain_(PgSqlParser.Transaction_chain_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#viewstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewstmt(PgSqlParser.ViewstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#check_option_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_option_(PgSqlParser.Check_option_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#loadstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadstmt(PgSqlParser.LoadstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdbstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedbstmt(PgSqlParser.CreatedbstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdb_opt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_list(PgSqlParser.Createdb_opt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdb_opt_items}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_items(PgSqlParser.Createdb_opt_itemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdb_opt_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_item(PgSqlParser.Createdb_opt_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdb_opt_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedb_opt_name(PgSqlParser.Createdb_opt_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#equal_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual_(PgSqlParser.Equal_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterdatabasestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdatabasestmt(PgSqlParser.AlterdatabasestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#setTablespaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTablespaceName(PgSqlParser.SetTablespaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#refresh_collation_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefresh_collation_version(PgSqlParser.Refresh_collation_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterdatabasesetstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdatabasesetstmt(PgSqlParser.AlterdatabasesetstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropdbstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropdbstmt(PgSqlParser.DropdbstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#dropschemastmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropschemastmt(PgSqlParser.DropschemastmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#drop_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_option_list(PgSqlParser.Drop_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#drop_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_option(PgSqlParser.Drop_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altercollationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltercollationstmt(PgSqlParser.AltercollationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altersystemstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltersystemstmt(PgSqlParser.AltersystemstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createdomainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedomainstmt(PgSqlParser.CreatedomainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alterdomainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterdomainstmt(PgSqlParser.AlterdomainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#as_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAs_(PgSqlParser.As_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altertsdictionarystmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertsdictionarystmt(PgSqlParser.AltertsdictionarystmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#altertsconfigurationstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAltertsconfigurationstmt(PgSqlParser.AltertsconfigurationstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#any_with}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_with(PgSqlParser.Any_withContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#createconversionstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateconversionstmt(PgSqlParser.CreateconversionstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#clusterstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterstmt(PgSqlParser.ClusterstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cluster_index_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_index_specification(PgSqlParser.Cluster_index_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vacuumstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuumstmt(PgSqlParser.VacuumstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#analyzestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzestmt(PgSqlParser.AnalyzestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#utility_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_list(PgSqlParser.Utility_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vac_analyze_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_list(PgSqlParser.Vac_analyze_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#analyze_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_keyword(PgSqlParser.Analyze_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#utility_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_elem(PgSqlParser.Utility_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#utility_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_name(PgSqlParser.Utility_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#utility_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtility_option_arg(PgSqlParser.Utility_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vac_analyze_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_elem(PgSqlParser.Vac_analyze_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vac_analyze_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_name(PgSqlParser.Vac_analyze_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vac_analyze_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVac_analyze_option_arg(PgSqlParser.Vac_analyze_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#analyze_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_(PgSqlParser.Analyze_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#verbose_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerbose_(PgSqlParser.Verbose_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#full_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_(PgSqlParser.Full_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#freeze_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFreeze_(PgSqlParser.Freeze_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#name_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_list_(PgSqlParser.Name_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vacuum_relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation(PgSqlParser.Vacuum_relationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vacuum_relation_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation_list(PgSqlParser.Vacuum_relation_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#vacuum_relation_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_relation_list_(PgSqlParser.Vacuum_relation_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explainstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainstmt(PgSqlParser.ExplainstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explainablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainablestmt(PgSqlParser.ExplainablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explain_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_list(PgSqlParser.Explain_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explain_option_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_elem(PgSqlParser.Explain_option_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explain_option_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_name(PgSqlParser.Explain_option_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explain_option_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_option_arg(PgSqlParser.Explain_option_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#preparestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreparestmt(PgSqlParser.PreparestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#prep_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrep_type_clause(PgSqlParser.Prep_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#preparablestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreparablestmt(PgSqlParser.PreparablestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#executestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecutestmt(PgSqlParser.ExecutestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#execute_param_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_param_clause(PgSqlParser.Execute_param_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#deallocatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocatestmt(PgSqlParser.DeallocatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#insertstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertstmt(PgSqlParser.InsertstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#insert_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_target(PgSqlParser.Insert_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#insert_rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_rest(PgSqlParser.Insert_restContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#override_kind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverride_kind(PgSqlParser.Override_kindContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_list(PgSqlParser.Insert_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#insert_column_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_item(PgSqlParser.Insert_column_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#on_conflict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_conflict_(PgSqlParser.On_conflict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#conf_expr_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConf_expr_(PgSqlParser.Conf_expr_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturning_clause(PgSqlParser.Returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#mergestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergestmt(PgSqlParser.MergestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_insert_clause(PgSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_clause(PgSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#merge_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_delete_clause(PgSqlParser.Merge_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#deletestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeletestmt(PgSqlParser.DeletestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_clause(PgSqlParser.Using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#lockstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockstmt(PgSqlParser.LockstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#lock_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_(PgSqlParser.Lock_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#lock_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_type(PgSqlParser.Lock_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#nowait_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowait_(PgSqlParser.Nowait_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#nowait_or_skip_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowait_or_skip_(PgSqlParser.Nowait_or_skip_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#updatestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdatestmt(PgSqlParser.UpdatestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_clause_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_clause_list(PgSqlParser.Set_clause_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_clause(PgSqlParser.Set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_target(PgSqlParser.Set_targetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#set_target_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_target_list(PgSqlParser.Set_target_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#declarecursorstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarecursorstmt(PgSqlParser.DeclarecursorstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_name(PgSqlParser.Cursor_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cursor_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_options(PgSqlParser.Cursor_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#hold_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHold_(PgSqlParser.Hold_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#selectstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectstmt(PgSqlParser.SelectstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_with_parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_with_parens(PgSqlParser.Select_with_parensContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_no_parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_no_parens(PgSqlParser.Select_no_parensContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_clause(PgSqlParser.Select_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#union_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion_context(PgSqlParser.Union_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#simple_select_pramary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_select_pramary(PgSqlParser.Simple_select_pramaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#with_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause(PgSqlParser.With_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cte_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_list(PgSqlParser.Cte_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#common_table_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_table_expr(PgSqlParser.Common_table_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#materialized_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaterialized_(PgSqlParser.Materialized_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#with_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause_(PgSqlParser.With_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause(PgSqlParser.Into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#strict_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrict_(PgSqlParser.Strict_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttempTableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttempTableName(PgSqlParser.OpttempTableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#table_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_(PgSqlParser.Table_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#all_or_distinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_or_distinct(PgSqlParser.All_or_distinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#distinct_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistinct_clause(PgSqlParser.Distinct_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#all_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_clause_(PgSqlParser.All_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sort_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_clause_(PgSqlParser.Sort_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sort_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_clause(PgSqlParser.Sort_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sortby_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortby_list(PgSqlParser.Sortby_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sortby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortby(PgSqlParser.SortbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_limit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit(PgSqlParser.Select_limitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_limit_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit_(PgSqlParser.Select_limit_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#limit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimit_clause(PgSqlParser.Limit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffset_clause(PgSqlParser.Offset_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_limit_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_limit_value(PgSqlParser.Select_limit_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_offset_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_offset_value(PgSqlParser.Select_offset_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#select_fetch_first_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_fetch_first_value(PgSqlParser.Select_fetch_first_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#i_or_f_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitI_or_f_const(PgSqlParser.I_or_f_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#row_or_rows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_or_rows(PgSqlParser.Row_or_rowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#first_or_next}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFirst_or_next(PgSqlParser.First_or_nextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_clause(PgSqlParser.Group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#group_by_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_list(PgSqlParser.Group_by_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#group_by_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_item(PgSqlParser.Group_by_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#empty_grouping_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_grouping_set(PgSqlParser.Empty_grouping_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rollup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollup_clause(PgSqlParser.Rollup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cube_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCube_clause(PgSqlParser.Cube_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_clause(PgSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(PgSqlParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#for_locking_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_clause(PgSqlParser.For_locking_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#for_locking_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_clause_(PgSqlParser.For_locking_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#for_locking_items}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_items(PgSqlParser.For_locking_itemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#for_locking_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_item(PgSqlParser.For_locking_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#for_locking_strength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_locking_strength(PgSqlParser.For_locking_strengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#locked_rels_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocked_rels_list(PgSqlParser.Locked_rels_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_clause(PgSqlParser.Values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_clause(PgSqlParser.From_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#from_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_list(PgSqlParser.From_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref(PgSqlParser.Table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#cross_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCross_join(PgSqlParser.Cross_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#normal_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormal_join(PgSqlParser.Normal_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#natural_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNatural_join(PgSqlParser.Natural_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#alias_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias_clause(PgSqlParser.Alias_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_alias_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_alias_clause(PgSqlParser.Func_alias_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_type(PgSqlParser.Join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#join_qual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_qual(PgSqlParser.Join_qualContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#relation_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr(PgSqlParser.Relation_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#relation_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr_list(PgSqlParser.Relation_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#relation_expr_opt_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_expr_opt_alias(PgSqlParser.Relation_expr_opt_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablesample_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablesample_clause(PgSqlParser.Tablesample_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#repeatable_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatable_clause_(PgSqlParser.Repeatable_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_table(PgSqlParser.Func_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsfrom_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsfrom_item(PgSqlParser.Rowsfrom_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rowsfrom_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowsfrom_list(PgSqlParser.Rowsfrom_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#col_def_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_def_list_(PgSqlParser.Col_def_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#ordinality_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdinality_(PgSqlParser.Ordinality_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(PgSqlParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#where_or_current_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_or_current_clause(PgSqlParser.Where_or_current_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opttablefuncelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpttablefuncelementlist(PgSqlParser.OpttablefuncelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablefuncelementlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablefuncelementlist(PgSqlParser.TablefuncelementlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#tablefuncelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablefuncelement(PgSqlParser.TablefuncelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmltable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable(PgSqlParser.XmltableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmltable_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_list(PgSqlParser.Xmltable_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmltable_column_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_el(PgSqlParser.Xmltable_column_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmltable_column_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_option_list(PgSqlParser.Xmltable_column_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmltable_column_option_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable_column_option_el(PgSqlParser.Xmltable_column_option_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_namespace_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespace_list(PgSqlParser.Xml_namespace_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_namespace_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespace_el(PgSqlParser.Xml_namespace_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#typename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypename(PgSqlParser.TypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opt_array_bounds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpt_array_bounds(PgSqlParser.Opt_array_boundsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#simpletypename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpletypename(PgSqlParser.SimpletypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#consttypename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConsttypename(PgSqlParser.ConsttypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#generictype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenerictype(PgSqlParser.GenerictypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_modifiers_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_modifiers_(PgSqlParser.Type_modifiers_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(PgSqlParser.NumericContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#float_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_(PgSqlParser.Float_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBit(PgSqlParser.BitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constbit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstbit(PgSqlParser.ConstbitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bitwithlength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwithlength(PgSqlParser.BitwithlengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bitwithoutlength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwithoutlength(PgSqlParser.BitwithoutlengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(PgSqlParser.CharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constcharacter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstcharacter(PgSqlParser.ConstcharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#character_c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter_c(PgSqlParser.Character_cContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#varying_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarying_(PgSqlParser.Varying_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constdatetime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstdatetime(PgSqlParser.ConstdatetimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#constinterval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstinterval(PgSqlParser.ConstintervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#timezone_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimezone_(PgSqlParser.Timezone_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#interval_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_(PgSqlParser.Interval_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#interval_second}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_second(PgSqlParser.Interval_secondContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#jsonType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonType(PgSqlParser.JsonTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#escape_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscape_(PgSqlParser.Escape_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr(PgSqlParser.A_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_qual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_qual(PgSqlParser.A_expr_qualContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_lessless}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_lessless(PgSqlParser.A_expr_lesslessContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_or(PgSqlParser.A_expr_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_and(PgSqlParser.A_expr_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_between}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_between(PgSqlParser.A_expr_betweenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_in(PgSqlParser.A_expr_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_unary_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_not(PgSqlParser.A_expr_unary_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_isnull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_isnull(PgSqlParser.A_expr_isnullContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_is_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_is_not(PgSqlParser.A_expr_is_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_compare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_compare(PgSqlParser.A_expr_compareContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_like}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_like(PgSqlParser.A_expr_likeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_qual_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_qual_op(PgSqlParser.A_expr_qual_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_unary_qualop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_qualop(PgSqlParser.A_expr_unary_qualopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_add(PgSqlParser.A_expr_addContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_mul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_mul(PgSqlParser.A_expr_mulContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_caret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_caret(PgSqlParser.A_expr_caretContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_unary_sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_unary_sign(PgSqlParser.A_expr_unary_signContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_at_time_zone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_at_time_zone(PgSqlParser.A_expr_at_time_zoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_collate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_collate(PgSqlParser.A_expr_collateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#a_expr_typecast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA_expr_typecast(PgSqlParser.A_expr_typecastContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#b_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitB_expr(PgSqlParser.B_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#c_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_expr(PgSqlParser.C_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#plsqlvariablename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlsqlvariablename(PgSqlParser.PlsqlvariablenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_application}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_application(PgSqlParser.Func_applicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#star_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar_context(PgSqlParser.Star_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr(PgSqlParser.Func_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_expr_windowless}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr_windowless(PgSqlParser.Func_expr_windowlessContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_expr_common_subexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_expr_common_subexpr(PgSqlParser.Func_expr_common_subexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_root_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_root_version(PgSqlParser.Xml_root_versionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_root_standalone_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_root_standalone_(PgSqlParser.Xml_root_standalone_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attributes(PgSqlParser.Xml_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_attribute_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attribute_list(PgSqlParser.Xml_attribute_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_attribute_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attribute_el(PgSqlParser.Xml_attribute_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#document_or_content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument_or_content(PgSqlParser.Document_or_contentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_whitespace_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_whitespace_option(PgSqlParser.Xml_whitespace_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xmlexists_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlexists_argument(PgSqlParser.Xmlexists_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xml_passing_mech}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_passing_mech(PgSqlParser.Xml_passing_mechContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#within_group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_group_clause(PgSqlParser.Within_group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clause(PgSqlParser.Filter_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#window_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_clause(PgSqlParser.Window_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#window_definition_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_definition_list(PgSqlParser.Window_definition_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#window_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_definition(PgSqlParser.Window_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause(PgSqlParser.Over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#window_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_specification(PgSqlParser.Window_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#existing_window_name_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExisting_window_name_(PgSqlParser.Existing_window_name_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#partition_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_clause_(PgSqlParser.Partition_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#frame_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_clause_(PgSqlParser.Frame_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#frame_extent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_extent(PgSqlParser.Frame_extentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#frame_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrame_bound(PgSqlParser.Frame_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#window_exclusion_clause_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_exclusion_clause_(PgSqlParser.Window_exclusion_clause_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow(PgSqlParser.RowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#explicit_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicit_row(PgSqlParser.Explicit_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#implicit_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicit_row(PgSqlParser.Implicit_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sub_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_type(PgSqlParser.Sub_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#all_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_op(PgSqlParser.All_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#mathop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathop(PgSqlParser.MathopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#qual_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQual_op(PgSqlParser.Qual_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#qual_all_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQual_all_op(PgSqlParser.Qual_all_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#subquery_Op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_Op(PgSqlParser.Subquery_OpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(PgSqlParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_list(PgSqlParser.Func_arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_arg_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg_expr(PgSqlParser.Func_arg_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_list(PgSqlParser.Type_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#array_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_expr(PgSqlParser.Array_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#array_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_expr_list(PgSqlParser.Array_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#extract_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtract_list(PgSqlParser.Extract_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#extract_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtract_arg(PgSqlParser.Extract_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#unicode_normal_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnicode_normal_form(PgSqlParser.Unicode_normal_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#overlay_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverlay_list(PgSqlParser.Overlay_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#position_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPosition_list(PgSqlParser.Position_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#substr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstr_list(PgSqlParser.Substr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#trim_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrim_list(PgSqlParser.Trim_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code in_expr_select}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_expr_select(PgSqlParser.In_expr_selectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code in_expr_list}
	 * labeled alternative in {@link PgSqlParser#in_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_expr_list(PgSqlParser.In_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#case_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expr(PgSqlParser.Case_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#when_clause_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen_clause_list(PgSqlParser.When_clause_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#when_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen_clause(PgSqlParser.When_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#case_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_default(PgSqlParser.Case_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#case_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_arg(PgSqlParser.Case_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#columnref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnref(PgSqlParser.ColumnrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#indirection_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirection_el(PgSqlParser.Indirection_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#slice_bound_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlice_bound_(PgSqlParser.Slice_bound_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#indirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirection(PgSqlParser.IndirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#opt_indirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpt_indirection(PgSqlParser.Opt_indirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_passing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_passing_clause(PgSqlParser.Json_passing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_arguments(PgSqlParser.Json_argumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_argument(PgSqlParser.Json_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_wrapper_behavior}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_wrapper_behavior(PgSqlParser.Json_wrapper_behaviorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_behavior}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior(PgSqlParser.Json_behaviorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_behavior_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior_type(PgSqlParser.Json_behavior_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_behavior_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_behavior_clause(PgSqlParser.Json_behavior_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_on_error_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_on_error_clause(PgSqlParser.Json_on_error_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_value_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_expr(PgSqlParser.Json_value_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_format_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_format_clause(PgSqlParser.Json_format_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_quotes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_quotes_clause(PgSqlParser.Json_quotes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_returning_clause(PgSqlParser.Json_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_predicate_type_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_predicate_type_constraint(PgSqlParser.Json_predicate_type_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_key_uniqueness_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_key_uniqueness_constraint(PgSqlParser.Json_key_uniqueness_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_name_and_value_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_name_and_value_list(PgSqlParser.Json_name_and_value_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_name_and_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_name_and_value(PgSqlParser.Json_name_and_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_object_constructor_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_constructor_null_clause(PgSqlParser.Json_object_constructor_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_array_constructor_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_constructor_null_clause(PgSqlParser.Json_array_constructor_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_value_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_expr_list(PgSqlParser.Json_value_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_aggregate_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_aggregate_func(PgSqlParser.Json_aggregate_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#json_array_aggregate_order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_aggregate_order_by_clause(PgSqlParser.Json_array_aggregate_order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#target_list_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_list_(PgSqlParser.Target_list_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#target_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_list(PgSqlParser.Target_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#target_el}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_el(PgSqlParser.Target_elContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#target_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_alias(PgSqlParser.Target_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#qualified_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name_list(PgSqlParser.Qualified_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#qualified_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name(PgSqlParser.Qualified_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_list(PgSqlParser.Name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(PgSqlParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#attr_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttr_name(PgSqlParser.Attr_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#file_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_name(PgSqlParser.File_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#func_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_name(PgSqlParser.Func_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#aexprconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAexprconst(PgSqlParser.AexprconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#xconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXconst(PgSqlParser.XconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBconst(PgSqlParser.BconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#fconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFconst(PgSqlParser.FconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#iconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIconst(PgSqlParser.IconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#sconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSconst(PgSqlParser.SconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#anysconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnysconst(PgSqlParser.AnysconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#uescape_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUescape_(PgSqlParser.Uescape_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#signediconst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignediconst(PgSqlParser.SignediconstContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#roleid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleid(PgSqlParser.RoleidContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#rolespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRolespec(PgSqlParser.RolespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#role_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_list(PgSqlParser.Role_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#colid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColid(PgSqlParser.ColidContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_function_name(PgSqlParser.Type_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#nonreservedword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonreservedword(PgSqlParser.NonreservedwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#colLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColLabel(PgSqlParser.ColLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bareColLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBareColLabel(PgSqlParser.BareColLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#unreserved_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnreserved_keyword(PgSqlParser.Unreserved_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#col_name_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_name_keyword(PgSqlParser.Col_name_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#type_func_name_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_func_name_keyword(PgSqlParser.Type_func_name_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#reserved_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReserved_keyword(PgSqlParser.Reserved_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#bare_label_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBare_label_keyword(PgSqlParser.Bare_label_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#any_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_identifier(PgSqlParser.Any_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link PgSqlParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(PgSqlParser.IdentifierContext ctx);
}