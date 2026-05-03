// Generated from PlSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oracle.parser.antlr;

    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PlSqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PlSqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_script(PlSqlParser.Sql_scriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_statement(PlSqlParser.Unit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_diskgroup(PlSqlParser.Alter_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_disk_clause(PlSqlParser.Add_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_disk_clause(PlSqlParser.Drop_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResize_disk_clause(PlSqlParser.Resize_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace_disk_clause(PlSqlParser.Replace_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#wait_nowait}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait_nowait(PlSqlParser.Wait_nowaitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_disk_clause(PlSqlParser.Rename_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disk_online_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_online_clause(PlSqlParser.Disk_online_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_offline_clause(PlSqlParser.Disk_offline_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#timeout_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeout_clause(PlSqlParser.Timeout_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRebalance_diskgroup_clause(PlSqlParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#phase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhase(PlSqlParser.PhaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_diskgroup_clause(PlSqlParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_template_clauses(PlSqlParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_template_clause(PlSqlParser.Qualified_template_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#redundancy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedundancy_clause(PlSqlParser.Redundancy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#striping_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStriping_clause(PlSqlParser.Striping_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#force_noforce}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForce_noforce(PlSqlParser.Force_noforceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_directory_clauses(PlSqlParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dir_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDir_name(PlSqlParser.Dir_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_alias_clauses(PlSqlParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_volume_clauses(PlSqlParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_volume_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_volume_clause(PlSqlParser.Add_volume_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_volume_clause(PlSqlParser.Modify_volume_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_attributes(PlSqlParser.Diskgroup_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_diskgroup_file(PlSqlParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disk_region_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_region_clause(PlSqlParser.Disk_region_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_diskgroup_file_clause(PlSqlParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert_redundancy_clause(PlSqlParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsergroup_clauses(PlSqlParser.Usergroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_clauses(PlSqlParser.User_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_permissions_clause(PlSqlParser.File_permissions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#file_owner_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_owner_clause(PlSqlParser.File_owner_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#scrub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScrub_clause(PlSqlParser.Scrub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotagroup_clauses(PlSqlParser.Quotagroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#property_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_name(PlSqlParser.Property_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#property_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_value(PlSqlParser.Property_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilegroup_clauses(PlSqlParser.Filegroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_filegroup_clause(PlSqlParser.Add_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_filegroup_clause(PlSqlParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_to_filegroup_clause(PlSqlParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_filegroup_clause(PlSqlParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#quorum_regular}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuorum_regular(PlSqlParser.Quorum_regularContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndrop_disk_clause(PlSqlParser.Undrop_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_availability(PlSqlParser.Diskgroup_availabilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_disable_volume(PlSqlParser.Enable_disable_volumeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_function(PlSqlParser.Drop_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_flashback_archive(PlSqlParser.Alter_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_hierarchy(PlSqlParser.Alter_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_function(PlSqlParser.Alter_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_java(PlSqlParser.Alter_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#match_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch_string(PlSqlParser.Match_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_function_body(PlSqlParser.Create_function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_macro_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_macro_body(PlSqlParser.Sql_macro_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParallel_enable_clause(PlSqlParser.Parallel_enable_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_by_clause(PlSqlParser.Partition_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#result_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_cache_clause(PlSqlParser.Result_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessible_by_clause(PlSqlParser.Accessible_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_collation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_collation_clause(PlSqlParser.Default_collation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_clause(PlSqlParser.Aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipelined_using_clause(PlSqlParser.Pipelined_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#accessor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessor(PlSqlParser.AccessorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#relies_on_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelies_on_part(PlSqlParser.Relies_on_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#streaming_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreaming_clause(PlSqlParser.Streaming_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_outline(PlSqlParser.Alter_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#outline_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutline_options(PlSqlParser.Outline_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_lockdown_profile(PlSqlParser.Alter_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lockdown_feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_feature(PlSqlParser.Lockdown_featureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lockdown_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_options(PlSqlParser.Lockdown_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lockdown_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_statements(PlSqlParser.Lockdown_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#statement_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_clauses(PlSqlParser.Statement_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#clause_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClause_options(PlSqlParser.Clause_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#option_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption_values(PlSqlParser.Option_valuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#string_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_list(PlSqlParser.String_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disable_enable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisable_enable(PlSqlParser.Disable_enableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_lockdown_profile(PlSqlParser.Drop_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_package(PlSqlParser.Drop_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_package(PlSqlParser.Alter_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package(PlSqlParser.Create_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_package_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package_body(PlSqlParser.Create_package_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#package_obj_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_obj_spec(PlSqlParser.Package_obj_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#procedure_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_spec(PlSqlParser.Procedure_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_spec(PlSqlParser.Function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#package_obj_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_obj_body(PlSqlParser.Package_obj_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_pmem_filestore(PlSqlParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_pmem_filestore(PlSqlParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_procedure(PlSqlParser.Drop_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_procedure(PlSqlParser.Alter_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_body(PlSqlParser.Function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#procedure_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_body(PlSqlParser.Procedure_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_procedure_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_procedure_body(PlSqlParser.Create_procedure_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_resource_cost(PlSqlParser.Alter_resource_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_outline(PlSqlParser.Drop_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_rollback_segment(PlSqlParser.Alter_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_restore_point(PlSqlParser.Drop_restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_rollback_segment(PlSqlParser.Drop_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_role(PlSqlParser.Drop_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_pmem_filestore(PlSqlParser.Create_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPmem_filestore_options(PlSqlParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#file_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_path(PlSqlParser.File_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_rollback_segment(PlSqlParser.Create_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_trigger(PlSqlParser.Drop_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_trigger(PlSqlParser.Alter_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_trigger(PlSqlParser.Create_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_follows_clause(PlSqlParser.Trigger_follows_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_when_clause(PlSqlParser.Trigger_when_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_dml_trigger(PlSqlParser.Simple_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#for_each_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_each_row(PlSqlParser.For_each_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_dml_trigger(PlSqlParser.Compound_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_dml_trigger(PlSqlParser.Non_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trigger_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_body(PlSqlParser.Trigger_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#routine_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_clause(PlSqlParser.Routine_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_trigger_block(PlSqlParser.Compound_trigger_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#timing_point_section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTiming_point_section(PlSqlParser.Timing_point_sectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#non_dml_event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_dml_event(PlSqlParser.Non_dml_eventContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dml_event_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_clause(PlSqlParser.Dml_event_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dml_event_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_element(PlSqlParser.Dml_event_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_nested_clause(PlSqlParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#referencing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencing_clause(PlSqlParser.Referencing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#referencing_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencing_element(PlSqlParser.Referencing_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_type(PlSqlParser.Drop_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type(PlSqlParser.Alter_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compile_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompile_type_clause(PlSqlParser.Compile_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#replace_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace_type_clause(PlSqlParser.Replace_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_method_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_method_spec(PlSqlParser.Alter_method_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_method_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_method_element(PlSqlParser.Alter_method_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_attribute_definition(PlSqlParser.Alter_attribute_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#attribute_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_definition(PlSqlParser.Attribute_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_collection_clauses(PlSqlParser.Alter_collection_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependent_handling_clause(PlSqlParser.Dependent_handling_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependent_exceptions_part(PlSqlParser.Dependent_exceptions_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_type(PlSqlParser.Create_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_definition(PlSqlParser.Type_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_def(PlSqlParser.Object_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_as_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_as_part(PlSqlParser.Object_as_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_under_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_under_part(PlSqlParser.Object_under_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_table_type_def(PlSqlParser.Nested_table_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlj_object_type(PlSqlParser.Sqlj_object_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_body(PlSqlParser.Type_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_body_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_body_elements(PlSqlParser.Type_body_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_order_func_declaration(PlSqlParser.Map_order_func_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubprog_decl_in_type(PlSqlParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProc_decl_in_type(PlSqlParser.Proc_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_decl_in_type(PlSqlParser.Func_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constructor_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_declaration(PlSqlParser.Constructor_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modifier_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier_clause(PlSqlParser.Modifier_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_member_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_member_spec(PlSqlParser.Object_member_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlj_object_type_attr(PlSqlParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#element_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement_spec(PlSqlParser.Element_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#element_spec_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement_spec_options(PlSqlParser.Element_spec_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subprogram_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubprogram_spec(PlSqlParser.Subprogram_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverriding_subprogram_spec(PlSqlParser.Overriding_subprogram_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverriding_function_spec(PlSqlParser.Overriding_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_procedure_spec(PlSqlParser.Type_procedure_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_function_spec(PlSqlParser.Type_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constructor_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_spec(PlSqlParser.Constructor_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_order_function_spec(PlSqlParser.Map_order_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pragma_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_clause(PlSqlParser.Pragma_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pragma_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_elements(PlSqlParser.Pragma_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_elements_parameter(PlSqlParser.Type_elements_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_sequence(PlSqlParser.Drop_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_sequence(PlSqlParser.Alter_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_session}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_session(PlSqlParser.Alter_sessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_session_set_clause(PlSqlParser.Alter_session_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_system}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_system(PlSqlParser.Alter_systemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_sequence(PlSqlParser.Create_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sequence_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_spec(PlSqlParser.Sequence_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_start_clause(PlSqlParser.Sequence_start_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_analytic_view(PlSqlParser.Create_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#classification_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassification_clause(PlSqlParser.Classification_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#caption_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaption_clause(PlSqlParser.Caption_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#description_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription_clause(PlSqlParser.Description_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#classification_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassification_item(PlSqlParser.Classification_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#language}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguage(PlSqlParser.LanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cav_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCav_using_clause(PlSqlParser.Cav_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dim_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_by_clause(PlSqlParser.Dim_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dim_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_key(PlSqlParser.Dim_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dim_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_ref(PlSqlParser.Dim_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hier_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_ref(PlSqlParser.Hier_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#measures_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeasures_clause(PlSqlParser.Measures_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#av_measure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAv_measure(PlSqlParser.Av_measureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#base_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase_meas_clause(PlSqlParser.Base_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeas_aggregate_clause(PlSqlParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalc_meas_clause(PlSqlParser.Calc_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_measure_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_measure_clause(PlSqlParser.Default_measure_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_aggregate_clause(PlSqlParser.Default_aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_clause(PlSqlParser.Cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cache_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_specification(PlSqlParser.Cache_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#levels_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevels_clause(PlSqlParser.Levels_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#level_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_specification(PlSqlParser.Level_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#level_group_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_group_type(PlSqlParser.Level_group_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFact_columns_clause(PlSqlParser.Fact_columns_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQry_transform_clause(PlSqlParser.Qry_transform_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_attribute_dimension(PlSqlParser.Create_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ad_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_using_clause(PlSqlParser.Ad_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#source_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource_clause(PlSqlParser.Source_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_path_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_path_clause(PlSqlParser.Join_path_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_condition(PlSqlParser.Join_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_condition_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_condition_item(PlSqlParser.Join_condition_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributes_clause(PlSqlParser.Attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_attributes_clause(PlSqlParser.Ad_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ad_level_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_level_clause(PlSqlParser.Ad_level_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_clause(PlSqlParser.Key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternate_key_clause(PlSqlParser.Alternate_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dim_order_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_order_clause(PlSqlParser.Dim_order_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#all_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_clause(PlSqlParser.All_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_audit_policy(PlSqlParser.Create_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_audit_clause(PlSqlParser.Privilege_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#action_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_audit_clause(PlSqlParser.Action_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#system_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_actions(PlSqlParser.System_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#standard_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_actions(PlSqlParser.Standard_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#actions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions_clause(PlSqlParser.Actions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_action(PlSqlParser.Object_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#system_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_action(PlSqlParser.System_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#component_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent_actions(PlSqlParser.Component_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#component_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent_action(PlSqlParser.Component_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#role_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_audit_clause(PlSqlParser.Role_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_controlfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_controlfile(PlSqlParser.Create_controlfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#controlfile_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlfile_options(PlSqlParser.Controlfile_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_clause(PlSqlParser.Logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#character_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter_set_clause(PlSqlParser.Character_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#file_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_specification(PlSqlParser.File_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_diskgroup(PlSqlParser.Create_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_disk_clause(PlSqlParser.Qualified_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_edition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_edition(PlSqlParser.Create_editionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_flashback_archive(PlSqlParser.Create_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_quota(PlSqlParser.Flashback_archive_quotaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_retention(PlSqlParser.Flashback_archive_retentionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_hierarchy(PlSqlParser.Create_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hier_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_using_clause(PlSqlParser.Hier_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#level_hier_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_hier_clause(PlSqlParser.Level_hier_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attrs_clause(PlSqlParser.Hier_attrs_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attr_clause(PlSqlParser.Hier_attr_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hier_attr_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attr_name(PlSqlParser.Hier_attr_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_index(PlSqlParser.Create_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_index_clause(PlSqlParser.Cluster_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cluster_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_name(PlSqlParser.Cluster_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_index_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_index_columns(PlSqlParser.Table_index_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitmap_join_index_clause(PlSqlParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_expr(PlSqlParser.Index_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_properties(PlSqlParser.Index_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#domain_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain_index_clause(PlSqlParser.Domain_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_domain_index_clause(PlSqlParser.Local_domain_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlindex_clause(PlSqlParser.Xmlindex_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_xmlindex_clause(PlSqlParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_partitioned_index(PlSqlParser.Global_partitioned_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partitioning_clause(PlSqlParser.Index_partitioning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partitioning_values_list(PlSqlParser.Index_partitioning_values_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_partitioned_index(PlSqlParser.Local_partitioned_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_range_partitioned_table(PlSqlParser.On_range_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_list_partitioned_table(PlSqlParser.On_list_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitioned_table(PlSqlParser.Partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_hash_partitioned_table(PlSqlParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_hash_partitioned_clause(PlSqlParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_comp_partitioned_table(PlSqlParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_comp_partitioned_clause(PlSqlParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_subpartition_clause(PlSqlParser.Index_subpartition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_subpartition_subclause(PlSqlParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#odci_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOdci_parameters(PlSqlParser.Odci_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#indextype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndextype(PlSqlParser.IndextypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index(PlSqlParser.Alter_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_ops_set1(PlSqlParser.Alter_index_ops_set1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_ops_set2(PlSqlParser.Alter_index_ops_set2Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisible_or_invisible(PlSqlParser.Visible_or_invisibleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonitoring_nomonitoring(PlSqlParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rebuild_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRebuild_clause(PlSqlParser.Rebuild_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_partitioning(PlSqlParser.Alter_index_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_default_attrs(PlSqlParser.Modify_index_default_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_hash_index_partition(PlSqlParser.Add_hash_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesce_index_partition(PlSqlParser.Coalesce_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_partition(PlSqlParser.Modify_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_partitions_ops(PlSqlParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rename_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_index_partition(PlSqlParser.Rename_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index_partition(PlSqlParser.Drop_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#split_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSplit_index_partition(PlSqlParser.Split_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_partition_description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partition_description(PlSqlParser.Index_partition_descriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_subpartition(PlSqlParser.Modify_index_subpartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_name_old}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_name_old(PlSqlParser.Partition_name_oldContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_partition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_partition_name(PlSqlParser.New_partition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_index_name(PlSqlParser.New_index_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_inmemory_join_group(PlSqlParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_user(PlSqlParser.Create_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_user(PlSqlParser.Alter_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_user(PlSqlParser.Drop_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_identified_by}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identified_by(PlSqlParser.Alter_identified_byContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identified_by}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by(PlSqlParser.Identified_byContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identified_other_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_other_clause(PlSqlParser.Identified_other_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_tablespace_clause(PlSqlParser.User_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#quota_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuota_clause(PlSqlParser.Quota_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#profile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProfile_clause(PlSqlParser.Profile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#role_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_clause(PlSqlParser.Role_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_default_role_clause(PlSqlParser.User_default_role_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#password_expire_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_expire_clause(PlSqlParser.Password_expire_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_lock_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_lock_clause(PlSqlParser.User_lock_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_editions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_editions_clause(PlSqlParser.User_editions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_user_editions_clause(PlSqlParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#proxy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProxy_clause(PlSqlParser.Proxy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#container_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_names(PlSqlParser.Container_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_container_data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_container_data(PlSqlParser.Set_container_dataContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_rem_container_data(PlSqlParser.Add_rem_container_dataContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#container_data_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_data_clause(PlSqlParser.Container_data_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#administer_key_management}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminister_key_management(PlSqlParser.Administer_key_managementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeystore_management_clauses(PlSqlParser.Keystore_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_keystore(PlSqlParser.Create_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#open_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_keystore(PlSqlParser.Open_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#force_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForce_keystore(PlSqlParser.Force_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#close_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_keystore(PlSqlParser.Close_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#backup_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBackup_keystore(PlSqlParser.Backup_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_keystore_password(PlSqlParser.Alter_keystore_passwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_into_new_keystore(PlSqlParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_into_existing_keystore(PlSqlParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#isolate_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsolate_keystore(PlSqlParser.Isolate_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unite_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnite_keystore(PlSqlParser.Unite_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#key_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_management_clauses(PlSqlParser.Key_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_key(PlSqlParser.Set_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_key(PlSqlParser.Create_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mkid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMkid(PlSqlParser.MkidContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMk(PlSqlParser.MkContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#use_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_key(PlSqlParser.Use_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_key_tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_key_tag(PlSqlParser.Set_key_tagContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#export_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport_keys(PlSqlParser.Export_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#import_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_keys(PlSqlParser.Import_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#migrate_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMigrate_keys(PlSqlParser.Migrate_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverse_migrate_keys(PlSqlParser.Reverse_migrate_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#move_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_keys(PlSqlParser.Move_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identified_by_store}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by_store(PlSqlParser.Identified_by_storeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_algorithm_clause(PlSqlParser.Using_algorithm_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_tag_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_tag_clause(PlSqlParser.Using_tag_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecret_management_clauses(PlSqlParser.Secret_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_update_secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_update_secret(PlSqlParser.Add_update_secretContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#delete_secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_secret(PlSqlParser.Delete_secretContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_update_secret_seps(PlSqlParser.Add_update_secret_sepsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_secret_seps(PlSqlParser.Delete_secret_sepsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZero_downtime_software_patching_clauses(PlSqlParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#with_backup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_backup_clause(PlSqlParser.With_backup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by_password_clause(PlSqlParser.Identified_by_password_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#keystore_password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeystore_password(PlSqlParser.Keystore_passwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(PlSqlParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecret(PlSqlParser.SecretContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#analyze}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze(PlSqlParser.AnalyzeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extention_clause(PlSqlParser.Partition_extention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#validation_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidation_clauses(PlSqlParser.Validation_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compute_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompute_clauses(PlSqlParser.Compute_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#for_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_clause(PlSqlParser.For_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#online_or_offline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnline_or_offline(PlSqlParser.Online_or_offlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#into_clause1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause1(PlSqlParser.Into_clause1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_key_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_key_value(PlSqlParser.Partition_key_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_key_value(PlSqlParser.Subpartition_key_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#associate_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociate_statistics(PlSqlParser.Associate_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_association}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_association(PlSqlParser.Column_associationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_association}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_association(PlSqlParser.Function_associationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#indextype_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndextype_name(PlSqlParser.Indextype_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_statistics_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_statistics_type(PlSqlParser.Using_statistics_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#statistics_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatistics_type_name(PlSqlParser.Statistics_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_cost_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_cost_clause(PlSqlParser.Default_cost_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cpu_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpu_cost(PlSqlParser.Cpu_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#io_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIo_cost(PlSqlParser.Io_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#network_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNetwork_cost(PlSqlParser.Network_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_selectivity_clause(PlSqlParser.Default_selectivity_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_selectivity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_selectivity(PlSqlParser.Default_selectivityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#storage_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorage_table_clause(PlSqlParser.Storage_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unified_auditing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnified_auditing(PlSqlParser.Unified_auditingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#policy_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolicy_name(PlSqlParser.Policy_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_traditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_traditional(PlSqlParser.Audit_traditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_direct_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_direct_path(PlSqlParser.Audit_direct_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_container_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_container_clause(PlSqlParser.Audit_container_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_operation_clause(PlSqlParser.Audit_operation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuditing_by_clause(PlSqlParser.Auditing_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_user(PlSqlParser.Audit_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_schema_object_clause(PlSqlParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_operation(PlSqlParser.Sql_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuditing_on_clause(PlSqlParser.Auditing_on_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_name(PlSqlParser.Model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_name(PlSqlParser.Object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#profile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProfile_name(PlSqlParser.Profile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_statement_shortcut(PlSqlParser.Sql_statement_shortcutContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index(PlSqlParser.Drop_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisassociate_statistics(PlSqlParser.Disassociate_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_indextype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_indextype(PlSqlParser.Drop_indextypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_inmemory_join_group(PlSqlParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_table(PlSqlParser.Flashback_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestore_point(PlSqlParser.Restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#purge_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPurge_statement(PlSqlParser.Purge_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#noaudit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoaudit_statement(PlSqlParser.Noaudit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rename_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_object(PlSqlParser.Rename_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#grant_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_statement(PlSqlParser.Grant_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#container_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_clause(PlSqlParser.Container_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#revoke_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_statement(PlSqlParser.Revoke_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_system_privilege(PlSqlParser.Revoke_system_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#revokee_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokee_clause(PlSqlParser.Revokee_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_object_privileges(PlSqlParser.Revoke_object_privilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_object_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_object_clause(PlSqlParser.On_object_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_roles_from_programs(PlSqlParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#program_unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram_unit(PlSqlParser.Program_unitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_dimension(PlSqlParser.Create_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_directory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_directory(PlSqlParser.Create_directoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#directory_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectory_name(PlSqlParser.Directory_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#directory_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectory_path(PlSqlParser.Directory_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_inmemory_join_group(PlSqlParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_hierarchy(PlSqlParser.Drop_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_library(PlSqlParser.Alter_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_java(PlSqlParser.Drop_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_library(PlSqlParser.Drop_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_java(PlSqlParser.Create_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_library(PlSqlParser.Create_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#plsql_library_source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlsql_library_source(PlSqlParser.Plsql_library_sourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#credential_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCredential_name(PlSqlParser.Credential_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#library_editionable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_editionable(PlSqlParser.Library_editionableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#library_debug}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_debug(PlSqlParser.Library_debugContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompiler_parameters_clause(PlSqlParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parameter_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_value(PlSqlParser.Parameter_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#library_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_name(PlSqlParser.Library_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_dimension(PlSqlParser.Alter_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#level_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_clause(PlSqlParser.Level_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchy_clause(PlSqlParser.Hierarchy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimension_join_clause(PlSqlParser.Dimension_join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#attribute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_clause(PlSqlParser.Attribute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtended_attribute_clause(PlSqlParser.Extended_attribute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_one_or_more_sub_clause(PlSqlParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_view(PlSqlParser.Alter_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_view_editionable(PlSqlParser.Alter_view_editionableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view(PlSqlParser.Create_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#editioning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEditioning_clause(PlSqlParser.Editioning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#view_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_options(PlSqlParser.View_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_alias_constraint(PlSqlParser.View_alias_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_view_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_view_clause(PlSqlParser.Object_view_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inline_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_constraint(PlSqlParser.Inline_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unique_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_(PlSqlParser.Unique_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#primary_key_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_key_(PlSqlParser.Primary_key_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#null_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_(PlSqlParser.Null_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#not_null_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_null_(PlSqlParser.Not_null_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_ref_constraint(PlSqlParser.Inline_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_ref_constraint(PlSqlParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_constraint(PlSqlParser.Out_of_line_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constraint_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_column_list(PlSqlParser.Constraint_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constraint_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_state(PlSqlParser.Constraint_stateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_view_clause(PlSqlParser.Xmltype_view_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_schema_spec(PlSqlParser.Xml_schema_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_schema_url}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_schema_url(PlSqlParser.Xml_schema_urlContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(PlSqlParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace(PlSqlParser.Alter_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_tempfile_clauses(PlSqlParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_logging_clauses(PlSqlParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_group_clause(PlSqlParser.Tablespace_group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_group_name(PlSqlParser.Tablespace_group_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_state_clauses(PlSqlParser.Tablespace_state_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_mode_clause(PlSqlParser.Flashback_mode_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_tablespace_name(PlSqlParser.New_tablespace_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_tablespace(PlSqlParser.Create_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPermanent_tablespace_clause(PlSqlParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_encryption_spec(PlSqlParser.Tablespace_encryption_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logging_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogging_clause(PlSqlParser.Logging_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#extent_management_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtent_management_clause(PlSqlParser.Extent_management_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#segment_management_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_management_clause(PlSqlParser.Segment_management_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporary_tablespace_clause(PlSqlParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_tablespace_clause(PlSqlParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_retention_clause(PlSqlParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_tablespace_set(PlSqlParser.Create_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPermanent_tablespace_attrs(PlSqlParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_encryption_clause(PlSqlParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_tablespace_params(PlSqlParser.Default_tablespace_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_table_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_table_compression(PlSqlParser.Default_table_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#low_high}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLow_high(PlSqlParser.Low_highContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_index_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_index_compression(PlSqlParser.Default_index_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmmemory_clause(PlSqlParser.Inmmemory_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datafile_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_specification(PlSqlParser.Datafile_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tempfile_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTempfile_specification(PlSqlParser.Tempfile_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_tempfile_spec(PlSqlParser.Datafile_tempfile_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedo_log_file_spec(PlSqlParser.Redo_log_file_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#autoextend_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutoextend_clause(PlSqlParser.Autoextend_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#maxsize_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxsize_clause(PlSqlParser.Maxsize_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#build_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuild_clause(PlSqlParser.Build_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parallel_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParallel_clause(PlSqlParser.Parallel_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view(PlSqlParser.Alter_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mv_option1(PlSqlParser.Alter_mv_option1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mv_refresh(PlSqlParser.Alter_mv_refreshContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_segment(PlSqlParser.Rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_mv_column_clause(PlSqlParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view_log(PlSqlParser.Alter_materialized_view_logContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_mv_log_column_clause(PlSqlParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_mv_log_clause(PlSqlParser.Move_mv_log_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_log_augmentation(PlSqlParser.Mv_log_augmentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datetime_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetime_expr(PlSqlParser.Datetime_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#interval_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_expr(PlSqlParser.Interval_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynchronous_or_asynchronous(PlSqlParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#including_or_excluding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncluding_or_excluding(PlSqlParser.Including_or_excludingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_view_log(PlSqlParser.Create_materialized_view_logContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_values_clause(PlSqlParser.New_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_log_purge_clause(PlSqlParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_zonemap(PlSqlParser.Create_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_zonemap(PlSqlParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_materialized_zonemap(PlSqlParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_refresh_clause(PlSqlParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_attributes(PlSqlParser.Zonemap_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#zonemap_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_name(PlSqlParser.Zonemap_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#operator_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_name(PlSqlParser.Operator_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#operator_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_function_name(PlSqlParser.Operator_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_zonemap_on_table(PlSqlParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_zonemap_as_subquery(PlSqlParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_operator(PlSqlParser.Alter_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_operator(PlSqlParser.Drop_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_operator(PlSqlParser.Create_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding_clause(PlSqlParser.Binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_binding_clause(PlSqlParser.Add_binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#implementation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementation_clause(PlSqlParser.Implementation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#primary_operator_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_operator_list(PlSqlParser.Primary_operator_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#primary_operator_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_operator_item(PlSqlParser.Primary_operator_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#operator_context_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_context_clause(PlSqlParser.Operator_context_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_function_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_function_clause(PlSqlParser.Using_function_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_binding_clause(PlSqlParser.Drop_binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_view(PlSqlParser.Create_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScoped_table_ref_constraint(PlSqlParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mv_column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_column_alias(PlSqlParser.Mv_column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_mv_refresh(PlSqlParser.Create_mv_refreshContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_materialized_view(PlSqlParser.Drop_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_context(PlSqlParser.Create_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#oracle_namespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOracle_namespace(PlSqlParser.Oracle_namespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_cluster(PlSqlParser.Create_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_profile(PlSqlParser.Create_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#resource_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource_parameters(PlSqlParser.Resource_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#password_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_parameters(PlSqlParser.Password_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_lockdown_profile(PlSqlParser.Create_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#static_base_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatic_base_profile(PlSqlParser.Static_base_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamic_base_profile(PlSqlParser.Dynamic_base_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_outline(PlSqlParser.Create_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_restore_point(PlSqlParser.Create_restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_role(PlSqlParser.Create_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table(PlSqlParser.Create_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#temporary_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporary_(PlSqlParser.Temporary_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltype_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_table(PlSqlParser.Xmltype_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_virtual_columns(PlSqlParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_column_properties(PlSqlParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltype_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_storage(PlSqlParser.Xmltype_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlschema_spec(PlSqlParser.Xmlschema_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_table(PlSqlParser.Object_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type(PlSqlParser.Object_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#oid_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOid_index_clause(PlSqlParser.Oid_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#oid_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOid_clause(PlSqlParser.Oid_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_properties(PlSqlParser.Object_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_table_substitution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_table_substitution(PlSqlParser.Object_table_substitutionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#relational_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_table(PlSqlParser.Relational_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_clauses(PlSqlParser.Immutable_table_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_no_drop_clause(PlSqlParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_no_delete_clause(PlSqlParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_table_clauses(PlSqlParser.Blockchain_table_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_drop_table_clause(PlSqlParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_row_retention_clause(PlSqlParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_hash_and_data_format_clause(PlSqlParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollation_name(PlSqlParser.Collation_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_properties(PlSqlParser.Table_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#read_only_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead_only_clause(PlSqlParser.Read_only_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#indexing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexing_clause(PlSqlParser.Indexing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_clustering_clause(PlSqlParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#clustering_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_join(PlSqlParser.Clustering_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#clustering_join_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_join_item(PlSqlParser.Clustering_join_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#equijoin_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquijoin_condition(PlSqlParser.Equijoin_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cluster_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_clause(PlSqlParser.Cluster_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#clustering_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_columns(PlSqlParser.Clustering_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#clustering_column_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_column_group(PlSqlParser.Clustering_column_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#yes_no}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYes_no(PlSqlParser.Yes_noContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#zonemap_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_clause(PlSqlParser.Zonemap_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_replication_clause(PlSqlParser.Logical_replication_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(PlSqlParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#relational_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_property(PlSqlParser.Relational_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_partitioning_clauses(PlSqlParser.Table_partitioning_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#range_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_partitions(PlSqlParser.Range_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#list_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_partitions(PlSqlParser.List_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partitions(PlSqlParser.Hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividual_hash_partitions(PlSqlParser.Individual_hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partitions_by_quantity(PlSqlParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partition_quantity(PlSqlParser.Hash_partition_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_range_partitions(PlSqlParser.Composite_range_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_list_partitions(PlSqlParser.Composite_list_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_hash_partitions(PlSqlParser.Composite_hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#reference_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_partitioning(PlSqlParser.Reference_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_partition_desc(PlSqlParser.Reference_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#system_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_partitioning(PlSqlParser.System_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#range_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_partition_desc(PlSqlParser.Range_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#list_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_partition_desc(PlSqlParser.List_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_template(PlSqlParser.Subpartition_templateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_subpartition_quantity(PlSqlParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_range(PlSqlParser.Subpartition_by_rangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_list(PlSqlParser.Subpartition_by_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_hash(PlSqlParser.Subpartition_by_hashContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_name(PlSqlParser.Subpartition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_subpartition_desc(PlSqlParser.Range_subpartition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_subpartition_desc(PlSqlParser.List_subpartition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividual_hash_subparts(PlSqlParser.Individual_hash_subpartsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_subparts_by_quantity(PlSqlParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#range_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_values_clause(PlSqlParser.Range_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#range_values_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_values_list(PlSqlParser.Range_values_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#list_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_values_clause(PlSqlParser.List_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_partition_description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_partition_description(PlSqlParser.Table_partition_descriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitioning_storage_clause(PlSqlParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_partitioning_storage(PlSqlParser.Lob_partitioning_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype_null_enable(PlSqlParser.Datatype_null_enableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#size_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSize_clause(PlSqlParser.Size_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_compression(PlSqlParser.Table_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_table_clause(PlSqlParser.Inmemory_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_attributes(PlSqlParser.Inmemory_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_memcompress(PlSqlParser.Inmemory_memcompressContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_priority}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_priority(PlSqlParser.Inmemory_priorityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_distribute(PlSqlParser.Inmemory_distributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_duplicate(PlSqlParser.Inmemory_duplicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_column_clause(PlSqlParser.Inmemory_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhysical_attributes_clause(PlSqlParser.Physical_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorage_clause(PlSqlParser.Storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeferred_segment_creation(PlSqlParser.Deferred_segment_creationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_attributes_clause(PlSqlParser.Segment_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#physical_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhysical_properties(PlSqlParser.Physical_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_clause(PlSqlParser.Ilm_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_policy_clause(PlSqlParser.Ilm_policy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_compression_policy(PlSqlParser.Ilm_compression_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_tiering_policy(PlSqlParser.Ilm_tiering_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_after_on}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_after_on(PlSqlParser.Ilm_after_onContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#segment_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_group(PlSqlParser.Segment_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_inmemory_policy(PlSqlParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ilm_time_period}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_time_period(PlSqlParser.Ilm_time_periodContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeap_org_table_clause(PlSqlParser.Heap_org_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#external_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_table_clause(PlSqlParser.External_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#access_driver_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess_driver_type(PlSqlParser.Access_driver_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#external_table_data_props}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_table_data_props(PlSqlParser.External_table_data_propsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpaque_format_spec(PlSqlParser.Opaque_format_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#row_movement_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_movement_clause(PlSqlParser.Row_movement_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_clause(PlSqlParser.Flashback_archive_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#log_grp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog_grp(PlSqlParser.Log_grpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_table_logging(PlSqlParser.Supplemental_table_loggingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_log_grp_clause(PlSqlParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_id_key_clause(PlSqlParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocate_extent_clause(PlSqlParser.Allocate_extent_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocate_unused_clause(PlSqlParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#shrink_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShrink_clause(PlSqlParser.Shrink_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecords_per_block_clause(PlSqlParser.Records_per_block_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpgrade_table_clause(PlSqlParser.Upgrade_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#truncate_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_table(PlSqlParser.Truncate_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table(PlSqlParser.Drop_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_tablespace(PlSqlParser.Drop_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_tablespace_set(PlSqlParser.Drop_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#including_contents_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncluding_contents_clause(PlSqlParser.Including_contents_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_view(PlSqlParser.Drop_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#comment_on_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_column(PlSqlParser.Comment_on_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#enable_or_disable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_or_disable(PlSqlParser.Enable_or_disableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllow_or_disallow(PlSqlParser.Allow_or_disallowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_synonym(PlSqlParser.Alter_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_synonym(PlSqlParser.Create_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_synonym(PlSqlParser.Drop_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_spfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_spfile(PlSqlParser.Create_spfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#spfile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpfile_name(PlSqlParser.Spfile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pfile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPfile_name(PlSqlParser.Pfile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#comment_on_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_table(PlSqlParser.Comment_on_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#comment_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_context(PlSqlParser.Comment_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_materialized(PlSqlParser.Comment_on_materializedContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_analytic_view(PlSqlParser.Alter_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_add_cache_clause(PlSqlParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#levels_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevels_item(PlSqlParser.Levels_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#measure_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeasure_list(PlSqlParser.Measure_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_drop_cache_clause(PlSqlParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_attribute_dimension(PlSqlParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_audit_policy(PlSqlParser.Alter_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_cluster(PlSqlParser.Alter_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_analytic_view(PlSqlParser.Drop_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_attribute_dimension(PlSqlParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_audit_policy(PlSqlParser.Drop_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_flashback_archive(PlSqlParser.Drop_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_cluster(PlSqlParser.Drop_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_context(PlSqlParser.Drop_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_directory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_directory(PlSqlParser.Drop_directoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_diskgroup(PlSqlParser.Drop_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_edition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_edition(PlSqlParser.Drop_editionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#truncate_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_cluster(PlSqlParser.Truncate_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_or_nocache(PlSqlParser.Cache_or_nocacheContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_name(PlSqlParser.Database_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database(PlSqlParser.Alter_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_clause(PlSqlParser.Database_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#startup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartup_clauses(PlSqlParser.Startup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetlogs_or_noresetlogs(PlSqlParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpgrade_or_downgrade(PlSqlParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#recovery_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecovery_clauses(PlSqlParser.Recovery_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#begin_or_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_or_end(PlSqlParser.Begin_or_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#general_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_recovery(PlSqlParser.General_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#full_database_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_database_recovery(PlSqlParser.Full_database_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_database_recovery(PlSqlParser.Partial_database_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_database_recovery_10g(PlSqlParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitManaged_standby_recovery(PlSqlParser.Managed_standby_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#db_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_name(PlSqlParser.Db_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database_file_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_file_clauses(PlSqlParser.Database_file_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_datafile_clause(PlSqlParser.Create_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_datafile_clause(PlSqlParser.Alter_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tempfile_clause(PlSqlParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_datafile_clause(PlSqlParser.Move_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_clauses(PlSqlParser.Logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_logfile_clauses(PlSqlParser.Add_logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_redo_logfile(PlSqlParser.Group_redo_logfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_logfile_clauses(PlSqlParser.Drop_logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_logfile_clause(PlSqlParser.Switch_logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_db_logging(PlSqlParser.Supplemental_db_loggingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_or_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_or_drop(PlSqlParser.Add_or_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_plsql_clause(PlSqlParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_descriptor(PlSqlParser.Logfile_descriptorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlfile_clauses(PlSqlParser.Controlfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trace_file_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrace_file_clause(PlSqlParser.Trace_file_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandby_database_clauses(PlSqlParser.Standby_database_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivate_standby_db_clause(PlSqlParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaximize_standby_db_clause(PlSqlParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegister_logfile_clause(PlSqlParser.Register_logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_switchover_clause(PlSqlParser.Commit_switchover_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#start_standby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_standby_clause(PlSqlParser.Start_standby_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStop_standby_clause(PlSqlParser.Stop_standby_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#convert_database_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert_database_clause(PlSqlParser.Convert_database_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_settings_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_settings_clause(PlSqlParser.Default_settings_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_time_zone_clause(PlSqlParser.Set_time_zone_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#instance_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_clauses(PlSqlParser.Instance_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#security_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity_clause(PlSqlParser.Security_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#domain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain(PlSqlParser.DomainContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase(PlSqlParser.DatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#edition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdition_name(PlSqlParser.Edition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filenumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilenumber(PlSqlParser.FilenumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilename(PlSqlParser.FilenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#prepare_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrepare_clause(PlSqlParser.Prepare_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_mirror_clause(PlSqlParser.Drop_mirror_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lost_write_protection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLost_write_protection(PlSqlParser.Lost_write_protectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCdb_fleet_clauses(PlSqlParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLead_cdb_clause(PlSqlParser.Lead_cdb_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLead_cdb_uri_clause(PlSqlParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#property_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_clauses(PlSqlParser.Property_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplay_upgrade_clauses(PlSqlParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database_link(PlSqlParser.Alter_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#password_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_value(PlSqlParser.Password_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#link_authentication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLink_authentication(PlSqlParser.Link_authenticationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_schema(PlSqlParser.Create_schemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database(PlSqlParser.Create_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_logging_clauses(PlSqlParser.Database_logging_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_logging_sub_clause(PlSqlParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_clauses(PlSqlParser.Tablespace_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_pluggable_database(PlSqlParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#file_name_convert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_name_convert(PlSqlParser.File_name_convertContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilename_convert_sub_clause(PlSqlParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_datafile_clauses(PlSqlParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_mode_clause(PlSqlParser.Undo_mode_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_tablespace(PlSqlParser.Default_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_temp_tablespace(PlSqlParser.Default_temp_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#undo_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_tablespace(PlSqlParser.Undo_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_database(PlSqlParser.Drop_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#create_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database_link(PlSqlParser.Create_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_database_link(PlSqlParser.Drop_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_set(PlSqlParser.Alter_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_attrs(PlSqlParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_encryption(PlSqlParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTs_file_name_convert(PlSqlParser.Ts_file_name_convertContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_role(PlSqlParser.Alter_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#role_identified_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_identified_clause(PlSqlParser.Role_identified_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table(PlSqlParser.Alter_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoptimize_read_write_clause(PlSqlParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_table_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_properties(PlSqlParser.Alter_table_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_partitioning(PlSqlParser.Alter_table_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_table_partition(PlSqlParser.Add_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_partition(PlSqlParser.Drop_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_table_partition(PlSqlParser.Merge_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_table_partition(PlSqlParser.Modify_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#split_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSplit_table_partition(PlSqlParser.Split_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_table_partition(PlSqlParser.Truncate_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExchange_table_partition(PlSqlParser.Exchange_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesce_table_partition(PlSqlParser.Coalesce_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_interval_partition(PlSqlParser.Alter_interval_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_extended_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extended_names(PlSqlParser.Partition_extended_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_extended_names(PlSqlParser.Subpartition_extended_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_properties_1(PlSqlParser.Alter_table_properties_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_iot_clauses(PlSqlParser.Alter_iot_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mapping_table_clause(PlSqlParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_overflow_clause(PlSqlParser.Alter_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_overflow_clause(PlSqlParser.Add_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_index_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_clauses(PlSqlParser.Update_index_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_global_index_clause(PlSqlParser.Update_global_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_all_indexes_clause(PlSqlParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_all_indexes_index_clause(PlSqlParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_partition(PlSqlParser.Update_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_subpartition(PlSqlParser.Update_index_subpartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_disable_clause(PlSqlParser.Enable_disable_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_index_clause(PlSqlParser.Using_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_attributes(PlSqlParser.Index_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_or_nosort(PlSqlParser.Sort_or_nosortContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exceptions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptions_clause(PlSqlParser.Exceptions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#move_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_table_clause(PlSqlParser.Move_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_org_table_clause(PlSqlParser.Index_org_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapping_table_clause(PlSqlParser.Mapping_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#key_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_compression(PlSqlParser.Key_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_org_overflow_clause(PlSqlParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_clauses(PlSqlParser.Column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_collection_retrieval(PlSqlParser.Modify_collection_retrievalContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collection_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_item(PlSqlParser.Collection_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rename_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_column_clause(PlSqlParser.Rename_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#old_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_column_name(PlSqlParser.Old_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_column_name(PlSqlParser.New_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_modify_drop_column_clauses(PlSqlParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_column_clause(PlSqlParser.Drop_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_column_clauses(PlSqlParser.Modify_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_properties(PlSqlParser.Modify_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#col_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_default(PlSqlParser.Col_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_visibility(PlSqlParser.Modify_col_visibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_substitutable(PlSqlParser.Modify_col_substitutableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_column_clause(PlSqlParser.Add_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_varray_col_properties(PlSqlParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#varray_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_col_properties(PlSqlParser.Varray_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_storage_clause(PlSqlParser.Varray_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_segname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_segname(PlSqlParser.Lob_segnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_item(PlSqlParser.Lob_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_storage_parameters(PlSqlParser.Lob_storage_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_storage_clause(PlSqlParser.Lob_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_lob_storage_clause(PlSqlParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_lob_parameters(PlSqlParser.Modify_lob_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_parameters(PlSqlParser.Lob_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_deduplicate_clause(PlSqlParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_compression_clause(PlSqlParser.Lob_compression_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_retention_clause(PlSqlParser.Lob_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#encryption_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncryption_spec(PlSqlParser.Encryption_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace(PlSqlParser.TablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#varray_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_item(PlSqlParser.Varray_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_properties(PlSqlParser.Column_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_partition_storage(PlSqlParser.Lob_partition_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#period_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPeriod_definition(PlSqlParser.Period_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#start_time_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_time_column(PlSqlParser.Start_time_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#end_time_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd_time_column(PlSqlParser.End_time_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_definition(PlSqlParser.Column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_collation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_collation_name(PlSqlParser.Column_collation_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identity_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_clause(PlSqlParser.Identity_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_options_parentheses(PlSqlParser.Identity_options_parenthesesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identity_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_options(PlSqlParser.Identity_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtual_column_definition(PlSqlParser.Virtual_column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutogenerated_sequence_definition(PlSqlParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluation_edition_clause(PlSqlParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_part_storage(PlSqlParser.Out_of_line_part_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_table_col_properties(PlSqlParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#nested_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_item(PlSqlParser.Nested_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstitutable_column_clause(PlSqlParser.Substitutable_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_name(PlSqlParser.Partition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_logging_props(PlSqlParser.Supplemental_logging_propsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_or_attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_or_attribute(PlSqlParser.Column_or_attributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_col_properties(PlSqlParser.Object_type_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_table_constarint(PlSqlParser.Add_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_table_constarint(PlSqlParser.Modify_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_table_constarint(PlSqlParser.Rename_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_constarint(PlSqlParser.Drop_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#old_constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_constraint_name(PlSqlParser.Old_constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#new_constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_constraint_name(PlSqlParser.New_constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_constraint_clause(PlSqlParser.Drop_constraint_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_constraint(PlSqlParser.Add_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_constraint_clause(PlSqlParser.Add_constraint_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#check_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_constraint(PlSqlParser.Check_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#drop_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_constraint(PlSqlParser.Drop_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#enable_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_constraint(PlSqlParser.Enable_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#disable_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisable_constraint(PlSqlParser.Disable_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_key_clause(PlSqlParser.Foreign_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#references_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences_clause(PlSqlParser.References_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#on_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_delete_clause(PlSqlParser.On_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unique_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_key_clause(PlSqlParser.Unique_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#primary_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_key_clause(PlSqlParser.Primary_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#anonymous_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonymous_block(PlSqlParser.Anonymous_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvoker_rights_clause(PlSqlParser.Invoker_rights_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#call_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_spec(PlSqlParser.Call_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#java_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJava_spec(PlSqlParser.Java_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#c_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_spec(PlSqlParser.C_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_agent_in_clause(PlSqlParser.C_agent_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_parameters_clause(PlSqlParser.C_parameters_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#c_external_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_external_parameter(PlSqlParser.C_external_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#c_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_property(PlSqlParser.C_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(PlSqlParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#default_value_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_value_part(PlSqlParser.Default_value_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq_of_declare_specs(PlSqlParser.Seq_of_declare_specsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#declare_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_spec(PlSqlParser.Declare_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration(PlSqlParser.Variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subtype_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtype_declaration(PlSqlParser.Subtype_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cursor_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_declaration(PlSqlParser.Cursor_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parameter_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_spec(PlSqlParser.Parameter_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exception_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_declaration(PlSqlParser.Exception_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pragma_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_declaration(PlSqlParser.Pragma_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#record_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_type_def(PlSqlParser.Record_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#field_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_spec(PlSqlParser.Field_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef_cursor_type_def(PlSqlParser.Ref_cursor_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_declaration(PlSqlParser.Type_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_type_def(PlSqlParser.Table_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_indexed_by_part(PlSqlParser.Table_indexed_by_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#varray_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_type_def(PlSqlParser.Varray_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#seq_of_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq_of_statements(PlSqlParser.Seq_of_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#label_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel_declaration(PlSqlParser.Label_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PlSqlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwallow_to_semi(PlSqlParser.Swallow_to_semiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(PlSqlParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#continue_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_statement(PlSqlParser.Continue_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit_statement(PlSqlParser.Exit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#goto_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoto_statement(PlSqlParser.Goto_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(PlSqlParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#elsif_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElsif_part(PlSqlParser.Elsif_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_part(PlSqlParser.Else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#loop_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop_statement(PlSqlParser.Loop_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_loop_param(PlSqlParser.Cursor_loop_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#forall_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForall_statement(PlSqlParser.Forall_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#bounds_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBounds_clause(PlSqlParser.Bounds_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#between_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetween_bound(PlSqlParser.Between_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lower_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLower_bound(PlSqlParser.Lower_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#upper_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpper_bound(PlSqlParser.Upper_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#null_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_statement(PlSqlParser.Null_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#raise_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaise_statement(PlSqlParser.Raise_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(PlSqlParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#call_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_statement(PlSqlParser.Call_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipe_row_statement(PlSqlParser.Pipe_row_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#selection_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection_directive(PlSqlParser.Selection_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#error_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_directive(PlSqlParser.Error_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#selection_directive_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection_directive_body(PlSqlParser.Selection_directive_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(PlSqlParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exception_handler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_handler(PlSqlParser.Exception_handlerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trigger_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_block(PlSqlParser.Trigger_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tps_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTps_block(PlSqlParser.Tps_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(PlSqlParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_statement(PlSqlParser.Sql_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#execute_immediate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_immediate(PlSqlParser.Execute_immediateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamic_returning_clause(PlSqlParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData_manipulation_language_statements(PlSqlParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_manipulation_statements(PlSqlParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#close_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_statement(PlSqlParser.Close_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#open_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_statement(PlSqlParser.Open_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#fetch_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_statement(PlSqlParser.Fetch_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#variable_or_collection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_or_collection(PlSqlParser.Variable_or_collectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#open_for_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_for_statement(PlSqlParser.Open_for_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_control_statements(PlSqlParser.Transaction_control_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_transaction_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_transaction_command(PlSqlParser.Set_transaction_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_constraint_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_constraint_command(PlSqlParser.Set_constraint_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#commit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_statement(PlSqlParser.Commit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#write_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite_clause(PlSqlParser.Write_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rollback_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_statement(PlSqlParser.Rollback_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#savepoint_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_statement(PlSqlParser.Savepoint_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collection_method_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_method_call(PlSqlParser.Collection_method_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#explain_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_statement(PlSqlParser.Explain_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#select_only_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_only_statement(PlSqlParser.Select_only_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#select_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_statement(PlSqlParser.Select_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#with_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause(PlSqlParser.With_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_factoring_clause(PlSqlParser.With_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_factoring_clause(PlSqlParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#search_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_clause(PlSqlParser.Search_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cycle_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCycle_clause(PlSqlParser.Cycle_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubav_factoring_clause(PlSqlParser.Subav_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subav_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubav_clause(PlSqlParser.Subav_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchies_clause(PlSqlParser.Hierarchies_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filter_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clauses(PlSqlParser.Filter_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clause(PlSqlParser.Filter_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_calcs_clause(PlSqlParser.Add_calcs_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_calc_meas_clause(PlSqlParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(PlSqlParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_basic_elements(PlSqlParser.Subquery_basic_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_operation_part(PlSqlParser.Subquery_operation_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#union_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion_context(PlSqlParser.Union_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#query_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_block(PlSqlParser.Query_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#selected_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelected_list(PlSqlParser.Selected_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_clause(PlSqlParser.From_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#select_list_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_elements(PlSqlParser.Select_list_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_ref_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_list(PlSqlParser.Table_ref_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref(PlSqlParser.Table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_ref_aux}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux(PlSqlParser.Table_ref_auxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_one(PlSqlParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_two(PlSqlParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_thre(PlSqlParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_clause(PlSqlParser.Join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_type(PlSqlParser.Join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_on_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_on_part(PlSqlParser.Join_on_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#join_using_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_using_part(PlSqlParser.Join_using_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#outer_join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuter_join_type(PlSqlParser.Outer_join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#query_partition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_partition_clause(PlSqlParser.Query_partition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_query_clause(PlSqlParser.Flashback_query_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_clause(PlSqlParser.Pivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_element(PlSqlParser.Pivot_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_for_clause(PlSqlParser.Pivot_for_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause(PlSqlParser.Pivot_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause_element(PlSqlParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause_elements(PlSqlParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unpivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_clause(PlSqlParser.Unpivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_in_clause(PlSqlParser.Unpivot_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_in_elements(PlSqlParser.Unpivot_in_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchical_query_clause(PlSqlParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#start_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_part(PlSqlParser.Start_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#group_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_clause(PlSqlParser.Group_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#group_by_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_elements(PlSqlParser.Group_by_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollup_cube_clause(PlSqlParser.Rollup_cube_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_clause(PlSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_elements(PlSqlParser.Grouping_sets_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(PlSqlParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_clause(PlSqlParser.Model_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cell_reference_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell_reference_options(PlSqlParser.Cell_reference_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#return_rows_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_rows_clause(PlSqlParser.Return_rows_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#reference_model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_model(PlSqlParser.Reference_modelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#main_model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain_model(PlSqlParser.Main_modelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_clauses(PlSqlParser.Model_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_partition_part(PlSqlParser.Model_column_partition_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_list(PlSqlParser.Model_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column(PlSqlParser.Model_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_rules_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_clause(PlSqlParser.Model_rules_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_rules_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_part(PlSqlParser.Model_rules_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_rules_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_element(PlSqlParser.Model_rules_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cell_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell_assignment(PlSqlParser.Cell_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_iterate_clause(PlSqlParser.Model_iterate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#until_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil_part(PlSqlParser.Until_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_clause(PlSqlParser.Order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#order_by_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_elements(PlSqlParser.Order_by_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffset_clause(PlSqlParser.Offset_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#fetch_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_clause(PlSqlParser.Fetch_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#for_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_clause(PlSqlParser.For_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#for_update_of_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_of_part(PlSqlParser.For_update_of_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#for_update_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_options(PlSqlParser.For_update_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_statement(PlSqlParser.Update_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#update_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_set_clause(PlSqlParser.Update_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_based_update_set_clause(PlSqlParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#delete_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_statement(PlSqlParser.Delete_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#insert_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_statement(PlSqlParser.Insert_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#single_table_insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_table_insert(PlSqlParser.Single_table_insertContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#multi_table_insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_table_insert(PlSqlParser.Multi_table_insertContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#multi_table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_table_element(PlSqlParser.Multi_table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_clause(PlSqlParser.Conditional_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_when_part(PlSqlParser.Conditional_insert_when_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_else_part(PlSqlParser.Conditional_insert_else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#insert_into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_into_clause(PlSqlParser.Insert_into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_clause(PlSqlParser.Values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_statement(PlSqlParser.Merge_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_clause(PlSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_element(PlSqlParser.Merge_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_delete_part(PlSqlParser.Merge_update_delete_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_insert_clause(PlSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#selected_tableview}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelected_tableview(PlSqlParser.Selected_tableviewContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lock_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_table_statement(PlSqlParser.Lock_table_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait_nowait_part(PlSqlParser.Wait_nowait_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lock_table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_table_element(PlSqlParser.Lock_table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#lock_mode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_mode(PlSqlParser.Lock_modeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#general_table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_table_ref(PlSqlParser.General_table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#static_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatic_returning_clause(PlSqlParser.Static_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#error_logging_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_clause(PlSqlParser.Error_logging_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_into_part(PlSqlParser.Error_logging_into_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_reject_part(PlSqlParser.Error_logging_reject_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_table_expression_clause(PlSqlParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_collection_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_collection_expression(PlSqlParser.Table_collection_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_restriction_clause(PlSqlParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sample_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSample_clause(PlSqlParser.Sample_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#seed_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeed_part(PlSqlParser.Seed_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(PlSqlParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#expressions_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressions_(PlSqlParser.Expressions_Context ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PlSqlParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cursor_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_expression(PlSqlParser.Cursor_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_expression(PlSqlParser.Logical_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_logical_expression(PlSqlParser.Unary_logical_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_logical_operation(PlSqlParser.Unary_logical_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#logical_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_operation(PlSqlParser.Logical_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#multiset_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiset_expression(PlSqlParser.Multiset_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#relational_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_expression(PlSqlParser.Relational_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#compound_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_expression(PlSqlParser.Compound_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#relational_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_operator(PlSqlParser.Relational_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#in_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_elements(PlSqlParser.In_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#between_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetween_elements(PlSqlParser.Between_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#concatenation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatenation(PlSqlParser.ConcatenationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#interval_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_expression(PlSqlParser.Interval_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_expression(PlSqlParser.Model_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#model_expression_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_expression_element(PlSqlParser.Model_expression_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_column_for_loop(PlSqlParser.Single_column_for_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_column_for_loop(PlSqlParser.Multi_column_for_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#unary_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expression(PlSqlParser.Unary_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicit_cursor_expression(PlSqlParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collection_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_expression(PlSqlParser.Collection_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_statement(PlSqlParser.Case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#simple_case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_case_statement(PlSqlParser.Simple_case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#searched_case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearched_case_statement(PlSqlParser.Searched_case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_when_part_statement(PlSqlParser.Case_when_part_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_else_part_statement(PlSqlParser.Case_else_part_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expression(PlSqlParser.Case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#simple_case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_case_expression(PlSqlParser.Simple_case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#searched_case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearched_case_expression(PlSqlParser.Searched_case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_when_part_expression(PlSqlParser.Case_when_part_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_else_part_expression(PlSqlParser.Case_else_part_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(PlSqlParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#quantified_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantified_expression(PlSqlParser.Quantified_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#string_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_function(PlSqlParser.String_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#standard_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_function(PlSqlParser.Standard_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_function(PlSqlParser.Json_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_object_content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_content(PlSqlParser.Json_object_contentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_object_entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_entry(PlSqlParser.Json_object_entryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_table_clause(PlSqlParser.Json_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_array_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_element(PlSqlParser.Json_array_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_on_null_clause(PlSqlParser.Json_on_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_return_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_return_clause(PlSqlParser.Json_return_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_transform_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_transform_op(PlSqlParser.Json_transform_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_column_clause(PlSqlParser.Json_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_column_definition(PlSqlParser.Json_column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_returning_clause(PlSqlParser.Json_query_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_query_return_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_return_type(PlSqlParser.Json_query_return_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_wrapper_clause(PlSqlParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_on_error_clause(PlSqlParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_on_empty_clause(PlSqlParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_return_clause(PlSqlParser.Json_value_return_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_value_return_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_return_type(PlSqlParser.Json_value_return_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_on_mismatch_clause(PlSqlParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(PlSqlParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function_wrapper(PlSqlParser.Numeric_function_wrapperContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#numeric_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function(PlSqlParser.Numeric_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListagg_overflow_clause(PlSqlParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#other_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOther_function(PlSqlParser.Other_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause_keyword(PlSqlParser.Over_clause_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_or_over_clause_keyword(PlSqlParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_prediction_function_keyword(PlSqlParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause(PlSqlParser.Over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#windowing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_clause(PlSqlParser.Windowing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#windowing_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_type(PlSqlParser.Windowing_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#windowing_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_elements(PlSqlParser.Windowing_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_clause(PlSqlParser.Using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#using_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_element(PlSqlParser.Using_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollect_order_by_part(PlSqlParser.Collect_order_by_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#within_or_over_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_or_over_part(PlSqlParser.Within_or_over_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#string_delimiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_delimiter(PlSqlParser.String_delimiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCost_matrix_clause(PlSqlParser.Cost_matrix_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_passing_clause(PlSqlParser.Xml_passing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attributes_clause(PlSqlParser.Xml_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespaces_clause(PlSqlParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_table_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_table_column(PlSqlParser.Xml_table_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_general_default_part(PlSqlParser.Xml_general_default_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_multiuse_expression_element(PlSqlParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlroot_param_version_part(PlSqlParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlroot_param_standalone_part(PlSqlParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_enconding_part(PlSqlParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_version_part(PlSqlParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_ident_part(PlSqlParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_plus_command_no_semicolon(PlSqlParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sql_plus_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_plus_command(PlSqlParser.Sql_plus_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#start_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_command(PlSqlParser.Start_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#whenever_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenever_command(PlSqlParser.Whenever_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#set_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_command(PlSqlParser.Set_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#timing_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTiming_command(PlSqlParser.Timing_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extension_clause(PlSqlParser.Partition_extension_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias(PlSqlParser.Column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(PlSqlParser.Table_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(PlSqlParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause(PlSqlParser.Into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xml_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_column_name(PlSqlParser.Xml_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cost_class_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCost_class_name(PlSqlParser.Cost_class_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#attribute_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_name(PlSqlParser.Attribute_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#savepoint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_name(PlSqlParser.Savepoint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_segment_name(PlSqlParser.Rollback_segment_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_var_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_var_name(PlSqlParser.Table_var_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#schema_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_name(PlSqlParser.Schema_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#routine_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_name(PlSqlParser.Routine_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#package_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_name(PlSqlParser.Package_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#implementation_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementation_type_name(PlSqlParser.Implementation_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#parameter_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_name(PlSqlParser.Parameter_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#reference_model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_model_name(PlSqlParser.Reference_model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#main_model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain_model_name(PlSqlParser.Main_model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#container_tableview_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_tableview_name(PlSqlParser.Container_tableview_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_function_name(PlSqlParser.Aggregate_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#query_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_name(PlSqlParser.Query_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#grantee_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee_name(PlSqlParser.Grantee_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#role_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_name(PlSqlParser.Role_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_name(PlSqlParser.Constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#label_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel_name(PlSqlParser.Label_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name(PlSqlParser.Type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#sequence_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_name(PlSqlParser.Sequence_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#exception_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_name(PlSqlParser.Exception_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name(PlSqlParser.Function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#procedure_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_name(PlSqlParser.Procedure_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#trigger_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_name(PlSqlParser.Trigger_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#variable_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_name(PlSqlParser.Variable_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_name(PlSqlParser.Index_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_name(PlSqlParser.Cursor_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#record_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_name(PlSqlParser.Record_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#collection_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_name(PlSqlParser.Collection_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#link_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLink_name(PlSqlParser.Link_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#local_link_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_link_name(PlSqlParser.Local_link_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#connection_qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnection_qualifier(PlSqlParser.Connection_qualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(PlSqlParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#tableview_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableview_name(PlSqlParser.Tableview_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#xmltable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable(PlSqlParser.XmltableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#char_set_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar_set_name(PlSqlParser.Char_set_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#synonym_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynonym_name(PlSqlParser.Synonym_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#schema_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_object_name(PlSqlParser.Schema_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#dir_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDir_object_name(PlSqlParser.Dir_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#user_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_object_name(PlSqlParser.User_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#grant_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_object_name(PlSqlParser.Grant_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_list(PlSqlParser.Column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_list(PlSqlParser.Insert_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#paren_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParen_column_list(PlSqlParser.Paren_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#keep_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeep_clause(PlSqlParser.Keep_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument(PlSqlParser.Function_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument_analytic(PlSqlParser.Function_argument_analyticContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument_modeling(PlSqlParser.Function_argument_modelingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRespect_or_ignore_nulls(PlSqlParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(PlSqlParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#type_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_spec(PlSqlParser.Type_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#custom_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustom_type(PlSqlParser.Custom_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(PlSqlParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#interval_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_type(PlSqlParser.Interval_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#precision_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecision_part(PlSqlParser.Precision_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#native_datatype_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNative_datatype_element(PlSqlParser.Native_datatype_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#bind_variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBind_variable(PlSqlParser.Bind_variableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#columnref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnref(PlSqlParser.ColumnrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#general_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_element(PlSqlParser.General_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#general_element_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_element_part(PlSqlParser.General_element_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(PlSqlParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#keyword_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword_function(PlSqlParser.Keyword_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_element(PlSqlParser.Table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#object_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_privilege(PlSqlParser.Object_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#system_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_privilege(PlSqlParser.System_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(PlSqlParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(PlSqlParser.NumericContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#numeric_negative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_negative(PlSqlParser.Numeric_negativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#quoted_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuoted_string(PlSqlParser.Quoted_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(PlSqlParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#id_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_expression(PlSqlParser.Id_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#inquiry_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInquiry_directive(PlSqlParser.Inquiry_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#outer_join_sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuter_join_sign(PlSqlParser.Outer_join_signContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#regular_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegular_id(PlSqlParser.Regular_idContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_in_18c(PlSqlParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_in_12c(PlSqlParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_pre12c(PlSqlParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#string_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_function_name(PlSqlParser.String_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlSqlParser#numeric_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function_name(PlSqlParser.Numeric_function_nameContext ctx);
}