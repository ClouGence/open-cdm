// Generated from /Users/zyc/Documents/dm-project/clougence-dm/clouddm-plugins/clouddm-ds/ds-oceanbase/src/main/antlr/ObForOracleParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oceanbase.parser.ob4ora.antlr;

    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;


    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ObForOracleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ObForOracleParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhysical_attributes_clause(ObForOracleParser.Physical_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_script(ObForOracleParser.Sql_scriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_statement(ObForOracleParser.Unit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_diskgroup(ObForOracleParser.Alter_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_disk_clause(ObForOracleParser.Add_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_disk_clause(ObForOracleParser.Drop_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResize_disk_clause(ObForOracleParser.Resize_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace_disk_clause(ObForOracleParser.Replace_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#wait_nowait}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait_nowait(ObForOracleParser.Wait_nowaitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_disk_clause(ObForOracleParser.Rename_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disk_online_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_online_clause(ObForOracleParser.Disk_online_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_offline_clause(ObForOracleParser.Disk_offline_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#timeout_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeout_clause(ObForOracleParser.Timeout_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRebalance_diskgroup_clause(ObForOracleParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#phase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhase(ObForOracleParser.PhaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_diskgroup_clause(ObForOracleParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_template_clauses(ObForOracleParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_template_clause(ObForOracleParser.Qualified_template_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#redundancy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedundancy_clause(ObForOracleParser.Redundancy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#striping_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStriping_clause(ObForOracleParser.Striping_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#force_noforce}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForce_noforce(ObForOracleParser.Force_noforceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_directory_clauses(ObForOracleParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dir_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDir_name(ObForOracleParser.Dir_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_alias_clauses(ObForOracleParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_volume_clauses(ObForOracleParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_volume_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_volume_clause(ObForOracleParser.Add_volume_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_volume_clause(ObForOracleParser.Modify_volume_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_attributes(ObForOracleParser.Diskgroup_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_diskgroup_file(ObForOracleParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disk_region_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisk_region_clause(ObForOracleParser.Disk_region_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_diskgroup_file_clause(ObForOracleParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert_redundancy_clause(ObForOracleParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsergroup_clauses(ObForOracleParser.Usergroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_clauses(ObForOracleParser.User_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_permissions_clause(ObForOracleParser.File_permissions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#file_owner_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_owner_clause(ObForOracleParser.File_owner_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#scrub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScrub_clause(ObForOracleParser.Scrub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotagroup_clauses(ObForOracleParser.Quotagroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#property_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_name(ObForOracleParser.Property_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#property_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_value(ObForOracleParser.Property_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilegroup_clauses(ObForOracleParser.Filegroup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_filegroup_clause(ObForOracleParser.Add_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_filegroup_clause(ObForOracleParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_to_filegroup_clause(ObForOracleParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_filegroup_clause(ObForOracleParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#quorum_regular}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuorum_regular(ObForOracleParser.Quorum_regularContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndrop_disk_clause(ObForOracleParser.Undrop_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiskgroup_availability(ObForOracleParser.Diskgroup_availabilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_disable_volume(ObForOracleParser.Enable_disable_volumeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_function(ObForOracleParser.Drop_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_flashback_archive(ObForOracleParser.Alter_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_hierarchy(ObForOracleParser.Alter_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_function(ObForOracleParser.Alter_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_java(ObForOracleParser.Alter_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#match_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch_string(ObForOracleParser.Match_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_function_body(ObForOracleParser.Create_function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_macro_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_macro_body(ObForOracleParser.Sql_macro_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParallel_enable_clause(ObForOracleParser.Parallel_enable_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_by_clause(ObForOracleParser.Partition_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#result_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_cache_clause(ObForOracleParser.Result_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessible_by_clause(ObForOracleParser.Accessible_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_collation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_collation_clause(ObForOracleParser.Default_collation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_clause(ObForOracleParser.Aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipelined_using_clause(ObForOracleParser.Pipelined_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#accessor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessor(ObForOracleParser.AccessorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#relies_on_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelies_on_part(ObForOracleParser.Relies_on_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#streaming_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreaming_clause(ObForOracleParser.Streaming_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_outline(ObForOracleParser.Alter_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#outline_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutline_options(ObForOracleParser.Outline_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_lockdown_profile(ObForOracleParser.Alter_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lockdown_feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_feature(ObForOracleParser.Lockdown_featureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lockdown_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_options(ObForOracleParser.Lockdown_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lockdown_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockdown_statements(ObForOracleParser.Lockdown_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#statement_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_clauses(ObForOracleParser.Statement_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#clause_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClause_options(ObForOracleParser.Clause_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#option_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption_values(ObForOracleParser.Option_valuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#string_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_list(ObForOracleParser.String_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disable_enable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisable_enable(ObForOracleParser.Disable_enableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_lockdown_profile(ObForOracleParser.Drop_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_package(ObForOracleParser.Drop_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_package(ObForOracleParser.Alter_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package(ObForOracleParser.Create_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_package_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_package_body(ObForOracleParser.Create_package_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#package_obj_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_obj_spec(ObForOracleParser.Package_obj_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#procedure_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_spec(ObForOracleParser.Procedure_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_spec(ObForOracleParser.Function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#package_obj_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_obj_body(ObForOracleParser.Package_obj_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_pmem_filestore(ObForOracleParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_pmem_filestore(ObForOracleParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_procedure(ObForOracleParser.Drop_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_procedure(ObForOracleParser.Alter_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_body(ObForOracleParser.Function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#procedure_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_body(ObForOracleParser.Procedure_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_procedure_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_procedure_body(ObForOracleParser.Create_procedure_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_resource_cost(ObForOracleParser.Alter_resource_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_outline(ObForOracleParser.Drop_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_rollback_segment(ObForOracleParser.Alter_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_restore_point(ObForOracleParser.Drop_restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_rollback_segment(ObForOracleParser.Drop_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_role(ObForOracleParser.Drop_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_pmem_filestore(ObForOracleParser.Create_pmem_filestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPmem_filestore_options(ObForOracleParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#file_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_path(ObForOracleParser.File_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_rollback_segment(ObForOracleParser.Create_rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_trigger(ObForOracleParser.Drop_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_trigger(ObForOracleParser.Alter_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_trigger(ObForOracleParser.Create_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_follows_clause(ObForOracleParser.Trigger_follows_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_when_clause(ObForOracleParser.Trigger_when_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_dml_trigger(ObForOracleParser.Simple_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#for_each_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_each_row(ObForOracleParser.For_each_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_dml_trigger(ObForOracleParser.Compound_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_dml_trigger(ObForOracleParser.Non_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trigger_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_body(ObForOracleParser.Trigger_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#routine_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_clause(ObForOracleParser.Routine_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_trigger_block(ObForOracleParser.Compound_trigger_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#timing_point_section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTiming_point_section(ObForOracleParser.Timing_point_sectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#non_dml_event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_dml_event(ObForOracleParser.Non_dml_eventContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dml_event_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_clause(ObForOracleParser.Dml_event_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dml_event_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_element(ObForOracleParser.Dml_event_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_event_nested_clause(ObForOracleParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#referencing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencing_clause(ObForOracleParser.Referencing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#referencing_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencing_element(ObForOracleParser.Referencing_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_type(ObForOracleParser.Drop_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_type(ObForOracleParser.Alter_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compile_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompile_type_clause(ObForOracleParser.Compile_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#replace_type_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace_type_clause(ObForOracleParser.Replace_type_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_method_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_method_spec(ObForOracleParser.Alter_method_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_method_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_method_element(ObForOracleParser.Alter_method_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_attribute_definition(ObForOracleParser.Alter_attribute_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#attribute_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_definition(ObForOracleParser.Attribute_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_collection_clauses(ObForOracleParser.Alter_collection_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependent_handling_clause(ObForOracleParser.Dependent_handling_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependent_exceptions_part(ObForOracleParser.Dependent_exceptions_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_type(ObForOracleParser.Create_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_definition(ObForOracleParser.Type_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_def(ObForOracleParser.Object_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_as_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_as_part(ObForOracleParser.Object_as_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_under_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_under_part(ObForOracleParser.Object_under_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_table_type_def(ObForOracleParser.Nested_table_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlj_object_type(ObForOracleParser.Sqlj_object_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_body(ObForOracleParser.Type_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_body_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_body_elements(ObForOracleParser.Type_body_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_order_func_declaration(ObForOracleParser.Map_order_func_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubprog_decl_in_type(ObForOracleParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProc_decl_in_type(ObForOracleParser.Proc_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_decl_in_type(ObForOracleParser.Func_decl_in_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constructor_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_declaration(ObForOracleParser.Constructor_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modifier_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier_clause(ObForOracleParser.Modifier_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_member_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_member_spec(ObForOracleParser.Object_member_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlj_object_type_attr(ObForOracleParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#element_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement_spec(ObForOracleParser.Element_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#element_spec_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement_spec_options(ObForOracleParser.Element_spec_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subprogram_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubprogram_spec(ObForOracleParser.Subprogram_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverriding_subprogram_spec(ObForOracleParser.Overriding_subprogram_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverriding_function_spec(ObForOracleParser.Overriding_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_procedure_spec(ObForOracleParser.Type_procedure_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_function_spec(ObForOracleParser.Type_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constructor_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_spec(ObForOracleParser.Constructor_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_order_function_spec(ObForOracleParser.Map_order_function_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pragma_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_clause(ObForOracleParser.Pragma_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pragma_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_elements(ObForOracleParser.Pragma_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_elements_parameter(ObForOracleParser.Type_elements_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_sequence(ObForOracleParser.Drop_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_sequence(ObForOracleParser.Alter_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_session}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_session(ObForOracleParser.Alter_sessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_session_set_clause(ObForOracleParser.Alter_session_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_system}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_system(ObForOracleParser.Alter_systemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_sequence(ObForOracleParser.Create_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sequence_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_spec(ObForOracleParser.Sequence_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_start_clause(ObForOracleParser.Sequence_start_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_analytic_view(ObForOracleParser.Create_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#classification_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassification_clause(ObForOracleParser.Classification_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#caption_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaption_clause(ObForOracleParser.Caption_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#description_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription_clause(ObForOracleParser.Description_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#classification_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassification_item(ObForOracleParser.Classification_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#language}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguage(ObForOracleParser.LanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cav_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCav_using_clause(ObForOracleParser.Cav_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dim_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_by_clause(ObForOracleParser.Dim_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dim_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_key(ObForOracleParser.Dim_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dim_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_ref(ObForOracleParser.Dim_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hier_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_ref(ObForOracleParser.Hier_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#measures_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeasures_clause(ObForOracleParser.Measures_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#av_measure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAv_measure(ObForOracleParser.Av_measureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#base_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase_meas_clause(ObForOracleParser.Base_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeas_aggregate_clause(ObForOracleParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalc_meas_clause(ObForOracleParser.Calc_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_measure_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_measure_clause(ObForOracleParser.Default_measure_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_aggregate_clause(ObForOracleParser.Default_aggregate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_clause(ObForOracleParser.Cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cache_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_specification(ObForOracleParser.Cache_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#levels_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevels_clause(ObForOracleParser.Levels_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#level_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_specification(ObForOracleParser.Level_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#level_group_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_group_type(ObForOracleParser.Level_group_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFact_columns_clause(ObForOracleParser.Fact_columns_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQry_transform_clause(ObForOracleParser.Qry_transform_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_attribute_dimension(ObForOracleParser.Create_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ad_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_using_clause(ObForOracleParser.Ad_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#source_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource_clause(ObForOracleParser.Source_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_path_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_path_clause(ObForOracleParser.Join_path_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_condition(ObForOracleParser.Join_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_condition_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_condition_item(ObForOracleParser.Join_condition_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributes_clause(ObForOracleParser.Attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_attributes_clause(ObForOracleParser.Ad_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ad_level_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAd_level_clause(ObForOracleParser.Ad_level_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_clause(ObForOracleParser.Key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternate_key_clause(ObForOracleParser.Alternate_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dim_order_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_order_clause(ObForOracleParser.Dim_order_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#all_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_clause(ObForOracleParser.All_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_audit_policy(ObForOracleParser.Create_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege_audit_clause(ObForOracleParser.Privilege_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#action_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_audit_clause(ObForOracleParser.Action_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#system_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_actions(ObForOracleParser.System_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#standard_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_actions(ObForOracleParser.Standard_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#actions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions_clause(ObForOracleParser.Actions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_action(ObForOracleParser.Object_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#system_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_action(ObForOracleParser.System_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#component_actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent_actions(ObForOracleParser.Component_actionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#component_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent_action(ObForOracleParser.Component_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#role_audit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_audit_clause(ObForOracleParser.Role_audit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_controlfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_controlfile(ObForOracleParser.Create_controlfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#controlfile_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlfile_options(ObForOracleParser.Controlfile_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_clause(ObForOracleParser.Logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#character_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter_set_clause(ObForOracleParser.Character_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#file_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_specification(ObForOracleParser.File_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_diskgroup(ObForOracleParser.Create_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_disk_clause(ObForOracleParser.Qualified_disk_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_edition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_edition(ObForOracleParser.Create_editionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_flashback_archive(ObForOracleParser.Create_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_quota(ObForOracleParser.Flashback_archive_quotaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_retention(ObForOracleParser.Flashback_archive_retentionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_hierarchy(ObForOracleParser.Create_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hier_using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_using_clause(ObForOracleParser.Hier_using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#level_hier_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_hier_clause(ObForOracleParser.Level_hier_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attrs_clause(ObForOracleParser.Hier_attrs_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attr_clause(ObForOracleParser.Hier_attr_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hier_attr_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHier_attr_name(ObForOracleParser.Hier_attr_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_index(ObForOracleParser.Create_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_index_clause(ObForOracleParser.Cluster_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cluster_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_name(ObForOracleParser.Cluster_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_index_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_index_columns(ObForOracleParser.Table_index_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitmap_join_index_clause(ObForOracleParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_expr(ObForOracleParser.Index_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_properties(ObForOracleParser.Index_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#domain_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain_index_clause(ObForOracleParser.Domain_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_domain_index_clause(ObForOracleParser.Local_domain_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlindex_clause(ObForOracleParser.Xmlindex_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_xmlindex_clause(ObForOracleParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_partitioned_index(ObForOracleParser.Global_partitioned_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partitioning_clause(ObForOracleParser.Index_partitioning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partitioning_values_list(ObForOracleParser.Index_partitioning_values_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_partitioned_index(ObForOracleParser.Local_partitioned_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_range_partitioned_table(ObForOracleParser.On_range_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_list_partitioned_table(ObForOracleParser.On_list_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitioned_table(ObForOracleParser.Partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_hash_partitioned_table(ObForOracleParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_hash_partitioned_clause(ObForOracleParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_comp_partitioned_table(ObForOracleParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_comp_partitioned_clause(ObForOracleParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_subpartition_clause(ObForOracleParser.Index_subpartition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_subpartition_subclause(ObForOracleParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#odci_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOdci_parameters(ObForOracleParser.Odci_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#indextype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndextype(ObForOracleParser.IndextypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index(ObForOracleParser.Alter_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_ops_set1(ObForOracleParser.Alter_index_ops_set1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_ops_set2(ObForOracleParser.Alter_index_ops_set2Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisible_or_invisible(ObForOracleParser.Visible_or_invisibleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonitoring_nomonitoring(ObForOracleParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rebuild_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRebuild_clause(ObForOracleParser.Rebuild_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_index_partitioning(ObForOracleParser.Alter_index_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_default_attrs(ObForOracleParser.Modify_index_default_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_hash_index_partition(ObForOracleParser.Add_hash_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesce_index_partition(ObForOracleParser.Coalesce_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_partition(ObForOracleParser.Modify_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_partitions_ops(ObForOracleParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rename_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_index_partition(ObForOracleParser.Rename_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index_partition(ObForOracleParser.Drop_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#split_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSplit_index_partition(ObForOracleParser.Split_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_partition_description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_partition_description(ObForOracleParser.Index_partition_descriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_index_subpartition(ObForOracleParser.Modify_index_subpartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_name_old}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_name_old(ObForOracleParser.Partition_name_oldContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_partition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_partition_name(ObForOracleParser.New_partition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_index_name(ObForOracleParser.New_index_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_inmemory_join_group(ObForOracleParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_user(ObForOracleParser.Create_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_user(ObForOracleParser.Alter_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_user(ObForOracleParser.Drop_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_identified_by}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_identified_by(ObForOracleParser.Alter_identified_byContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identified_by}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by(ObForOracleParser.Identified_byContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identified_other_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_other_clause(ObForOracleParser.Identified_other_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_tablespace_clause(ObForOracleParser.User_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#quota_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuota_clause(ObForOracleParser.Quota_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#profile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProfile_clause(ObForOracleParser.Profile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#role_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_clause(ObForOracleParser.Role_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_default_role_clause(ObForOracleParser.User_default_role_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#password_expire_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_expire_clause(ObForOracleParser.Password_expire_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_lock_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_lock_clause(ObForOracleParser.User_lock_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_editions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_editions_clause(ObForOracleParser.User_editions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_user_editions_clause(ObForOracleParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#proxy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProxy_clause(ObForOracleParser.Proxy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#container_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_names(ObForOracleParser.Container_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_container_data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_container_data(ObForOracleParser.Set_container_dataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_rem_container_data(ObForOracleParser.Add_rem_container_dataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#container_data_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_data_clause(ObForOracleParser.Container_data_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#administer_key_management}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminister_key_management(ObForOracleParser.Administer_key_managementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeystore_management_clauses(ObForOracleParser.Keystore_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_keystore(ObForOracleParser.Create_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#open_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_keystore(ObForOracleParser.Open_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#force_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForce_keystore(ObForOracleParser.Force_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#close_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_keystore(ObForOracleParser.Close_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#backup_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBackup_keystore(ObForOracleParser.Backup_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_keystore_password(ObForOracleParser.Alter_keystore_passwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_into_new_keystore(ObForOracleParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_into_existing_keystore(ObForOracleParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#isolate_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsolate_keystore(ObForOracleParser.Isolate_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unite_keystore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnite_keystore(ObForOracleParser.Unite_keystoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#key_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_management_clauses(ObForOracleParser.Key_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_key(ObForOracleParser.Set_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_key(ObForOracleParser.Create_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mkid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMkid(ObForOracleParser.MkidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMk(ObForOracleParser.MkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#use_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_key(ObForOracleParser.Use_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_key_tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_key_tag(ObForOracleParser.Set_key_tagContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#export_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExport_keys(ObForOracleParser.Export_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#import_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_keys(ObForOracleParser.Import_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#migrate_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMigrate_keys(ObForOracleParser.Migrate_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverse_migrate_keys(ObForOracleParser.Reverse_migrate_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#move_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_keys(ObForOracleParser.Move_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identified_by_store}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by_store(ObForOracleParser.Identified_by_storeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_algorithm_clause(ObForOracleParser.Using_algorithm_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_tag_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_tag_clause(ObForOracleParser.Using_tag_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecret_management_clauses(ObForOracleParser.Secret_management_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_update_secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_update_secret(ObForOracleParser.Add_update_secretContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#delete_secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_secret(ObForOracleParser.Delete_secretContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_update_secret_seps(ObForOracleParser.Add_update_secret_sepsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_secret_seps(ObForOracleParser.Delete_secret_sepsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZero_downtime_software_patching_clauses(ObForOracleParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#with_backup_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_backup_clause(ObForOracleParser.With_backup_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentified_by_password_clause(ObForOracleParser.Identified_by_password_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#keystore_password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeystore_password(ObForOracleParser.Keystore_passwordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(ObForOracleParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#secret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecret(ObForOracleParser.SecretContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#analyze}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze(ObForOracleParser.AnalyzeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extention_clause(ObForOracleParser.Partition_extention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#validation_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidation_clauses(ObForOracleParser.Validation_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compute_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompute_clauses(ObForOracleParser.Compute_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#for_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_clause(ObForOracleParser.For_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#online_or_offline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnline_or_offline(ObForOracleParser.Online_or_offlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#into_clause1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause1(ObForOracleParser.Into_clause1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_key_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_key_value(ObForOracleParser.Partition_key_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_key_value(ObForOracleParser.Subpartition_key_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#associate_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociate_statistics(ObForOracleParser.Associate_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_association}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_association(ObForOracleParser.Column_associationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_association}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_association(ObForOracleParser.Function_associationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#indextype_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndextype_name(ObForOracleParser.Indextype_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_statistics_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_statistics_type(ObForOracleParser.Using_statistics_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#statistics_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatistics_type_name(ObForOracleParser.Statistics_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_cost_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_cost_clause(ObForOracleParser.Default_cost_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cpu_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpu_cost(ObForOracleParser.Cpu_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#io_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIo_cost(ObForOracleParser.Io_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#network_cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNetwork_cost(ObForOracleParser.Network_costContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_selectivity_clause(ObForOracleParser.Default_selectivity_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_selectivity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_selectivity(ObForOracleParser.Default_selectivityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#storage_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorage_table_clause(ObForOracleParser.Storage_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unified_auditing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnified_auditing(ObForOracleParser.Unified_auditingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#policy_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolicy_name(ObForOracleParser.Policy_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_traditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_traditional(ObForOracleParser.Audit_traditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_direct_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_direct_path(ObForOracleParser.Audit_direct_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_container_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_container_clause(ObForOracleParser.Audit_container_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_operation_clause(ObForOracleParser.Audit_operation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuditing_by_clause(ObForOracleParser.Auditing_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_user}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_user(ObForOracleParser.Audit_userContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAudit_schema_object_clause(ObForOracleParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_operation(ObForOracleParser.Sql_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuditing_on_clause(ObForOracleParser.Auditing_on_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_name(ObForOracleParser.Model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_name(ObForOracleParser.Object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#profile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProfile_name(ObForOracleParser.Profile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_statement_shortcut(ObForOracleParser.Sql_statement_shortcutContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index(ObForOracleParser.Drop_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisassociate_statistics(ObForOracleParser.Disassociate_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_indextype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_indextype(ObForOracleParser.Drop_indextypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_inmemory_join_group(ObForOracleParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_table(ObForOracleParser.Flashback_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestore_point(ObForOracleParser.Restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#purge_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPurge_statement(ObForOracleParser.Purge_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#noaudit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoaudit_statement(ObForOracleParser.Noaudit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rename_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_object(ObForOracleParser.Rename_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#grant_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_statement(ObForOracleParser.Grant_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#container_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_clause(ObForOracleParser.Container_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#revoke_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_statement(ObForOracleParser.Revoke_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_system_privilege(ObForOracleParser.Revoke_system_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#revokee_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokee_clause(ObForOracleParser.Revokee_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_object_privileges(ObForOracleParser.Revoke_object_privilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_object_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_object_clause(ObForOracleParser.On_object_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_roles_from_programs(ObForOracleParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#program_unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram_unit(ObForOracleParser.Program_unitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_dimension(ObForOracleParser.Create_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_directory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_directory(ObForOracleParser.Create_directoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#directory_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectory_name(ObForOracleParser.Directory_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#directory_path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectory_path(ObForOracleParser.Directory_pathContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_inmemory_join_group(ObForOracleParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_hierarchy(ObForOracleParser.Drop_hierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_library(ObForOracleParser.Alter_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_java(ObForOracleParser.Drop_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_library(ObForOracleParser.Drop_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_java}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_java(ObForOracleParser.Create_javaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_library(ObForOracleParser.Create_libraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#plsql_library_source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlsql_library_source(ObForOracleParser.Plsql_library_sourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#credential_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCredential_name(ObForOracleParser.Credential_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#library_editionable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_editionable(ObForOracleParser.Library_editionableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#library_debug}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_debug(ObForOracleParser.Library_debugContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompiler_parameters_clause(ObForOracleParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parameter_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_value(ObForOracleParser.Parameter_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#library_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary_name(ObForOracleParser.Library_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_dimension(ObForOracleParser.Alter_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#level_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel_clause(ObForOracleParser.Level_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchy_clause(ObForOracleParser.Hierarchy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimension_join_clause(ObForOracleParser.Dimension_join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#attribute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_clause(ObForOracleParser.Attribute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtended_attribute_clause(ObForOracleParser.Extended_attribute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_one_or_more_sub_clause(ObForOracleParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_view(ObForOracleParser.Alter_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_view_editionable(ObForOracleParser.Alter_view_editionableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view(ObForOracleParser.Create_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#editioning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEditioning_clause(ObForOracleParser.Editioning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#view_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_options(ObForOracleParser.View_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_alias_constraint(ObForOracleParser.View_alias_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_view_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_view_clause(ObForOracleParser.Object_view_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inline_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_constraint(ObForOracleParser.Inline_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unique_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_(ObForOracleParser.Unique_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#primary_key_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_key_(ObForOracleParser.Primary_key_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#null_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_(ObForOracleParser.Null_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#not_null_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_null_(ObForOracleParser.Not_null_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_ref_constraint(ObForOracleParser.Inline_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_ref_constraint(ObForOracleParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_constraint(ObForOracleParser.Out_of_line_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constraint_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_column_list(ObForOracleParser.Constraint_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constraint_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_state(ObForOracleParser.Constraint_stateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_view_clause(ObForOracleParser.Xmltype_view_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_schema_spec(ObForOracleParser.Xml_schema_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_schema_url}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_schema_url(ObForOracleParser.Xml_schema_urlContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(ObForOracleParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace(ObForOracleParser.Alter_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_tempfile_clauses(ObForOracleParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_logging_clauses(ObForOracleParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_group_clause(ObForOracleParser.Tablespace_group_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_group_name(ObForOracleParser.Tablespace_group_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_state_clauses(ObForOracleParser.Tablespace_state_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_mode_clause(ObForOracleParser.Flashback_mode_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_tablespace_name(ObForOracleParser.New_tablespace_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_tablespace(ObForOracleParser.Create_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPermanent_tablespace_clause(ObForOracleParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_encryption_spec(ObForOracleParser.Tablespace_encryption_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logging_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogging_clause(ObForOracleParser.Logging_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#extent_management_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtent_management_clause(ObForOracleParser.Extent_management_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#segment_management_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_management_clause(ObForOracleParser.Segment_management_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporary_tablespace_clause(ObForOracleParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_tablespace_clause(ObForOracleParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_retention_clause(ObForOracleParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_tablespace_set(ObForOracleParser.Create_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPermanent_tablespace_attrs(ObForOracleParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_encryption_clause(ObForOracleParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_tablespace_params(ObForOracleParser.Default_tablespace_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_table_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_table_compression(ObForOracleParser.Default_table_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#low_high}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLow_high(ObForOracleParser.Low_highContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_index_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_index_compression(ObForOracleParser.Default_index_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmmemory_clause(ObForOracleParser.Inmmemory_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datafile_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_specification(ObForOracleParser.Datafile_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tempfile_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTempfile_specification(ObForOracleParser.Tempfile_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatafile_tempfile_spec(ObForOracleParser.Datafile_tempfile_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedo_log_file_spec(ObForOracleParser.Redo_log_file_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#autoextend_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutoextend_clause(ObForOracleParser.Autoextend_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#maxsize_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxsize_clause(ObForOracleParser.Maxsize_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#build_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuild_clause(ObForOracleParser.Build_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parallel_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParallel_clause(ObForOracleParser.Parallel_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view(ObForOracleParser.Alter_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mv_option1(ObForOracleParser.Alter_mv_option1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mv_refresh(ObForOracleParser.Alter_mv_refreshContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rollback_segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_segment(ObForOracleParser.Rollback_segmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_mv_column_clause(ObForOracleParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_view_log(ObForOracleParser.Alter_materialized_view_logContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_mv_log_column_clause(ObForOracleParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_mv_log_clause(ObForOracleParser.Move_mv_log_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_log_augmentation(ObForOracleParser.Mv_log_augmentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datetime_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetime_expr(ObForOracleParser.Datetime_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#interval_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_expr(ObForOracleParser.Interval_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynchronous_or_asynchronous(ObForOracleParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#including_or_excluding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncluding_or_excluding(ObForOracleParser.Including_or_excludingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_view_log(ObForOracleParser.Create_materialized_view_logContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_values_clause(ObForOracleParser.New_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_log_purge_clause(ObForOracleParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_zonemap(ObForOracleParser.Create_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_materialized_zonemap(ObForOracleParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_materialized_zonemap(ObForOracleParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_refresh_clause(ObForOracleParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_attributes(ObForOracleParser.Zonemap_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#zonemap_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_name(ObForOracleParser.Zonemap_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#operator_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_name(ObForOracleParser.Operator_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#operator_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_function_name(ObForOracleParser.Operator_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_zonemap_on_table(ObForOracleParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_zonemap_as_subquery(ObForOracleParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_operator(ObForOracleParser.Alter_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_operator(ObForOracleParser.Drop_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_operator(ObForOracleParser.Create_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding_clause(ObForOracleParser.Binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_binding_clause(ObForOracleParser.Add_binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#implementation_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementation_clause(ObForOracleParser.Implementation_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#primary_operator_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_operator_list(ObForOracleParser.Primary_operator_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#primary_operator_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_operator_item(ObForOracleParser.Primary_operator_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#operator_context_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator_context_clause(ObForOracleParser.Operator_context_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_function_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_function_clause(ObForOracleParser.Using_function_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_binding_clause(ObForOracleParser.Drop_binding_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_materialized_view(ObForOracleParser.Create_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScoped_table_ref_constraint(ObForOracleParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mv_column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMv_column_alias(ObForOracleParser.Mv_column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_mv_refresh(ObForOracleParser.Create_mv_refreshContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_materialized_view(ObForOracleParser.Drop_materialized_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_context(ObForOracleParser.Create_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#oracle_namespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOracle_namespace(ObForOracleParser.Oracle_namespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_cluster(ObForOracleParser.Create_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_profile(ObForOracleParser.Create_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#resource_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource_parameters(ObForOracleParser.Resource_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#password_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_parameters(ObForOracleParser.Password_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_lockdown_profile(ObForOracleParser.Create_lockdown_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#static_base_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatic_base_profile(ObForOracleParser.Static_base_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamic_base_profile(ObForOracleParser.Dynamic_base_profileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_outline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_outline(ObForOracleParser.Create_outlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_restore_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_restore_point(ObForOracleParser.Create_restore_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_role(ObForOracleParser.Create_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table(ObForOracleParser.Create_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#temporary_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporary_(ObForOracleParser.Temporary_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltype_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_table(ObForOracleParser.Xmltype_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_virtual_columns(ObForOracleParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_column_properties(ObForOracleParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltype_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltype_storage(ObForOracleParser.Xmltype_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlschema_spec(ObForOracleParser.Xmlschema_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_table(ObForOracleParser.Object_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type(ObForOracleParser.Object_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#oid_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOid_index_clause(ObForOracleParser.Oid_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#oid_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOid_clause(ObForOracleParser.Oid_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_properties(ObForOracleParser.Object_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_table_substitution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_table_substitution(ObForOracleParser.Object_table_substitutionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#relational_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_table(ObForOracleParser.Relational_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_clauses(ObForOracleParser.Immutable_table_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_no_drop_clause(ObForOracleParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutable_table_no_delete_clause(ObForOracleParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_table_clauses(ObForOracleParser.Blockchain_table_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_drop_table_clause(ObForOracleParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_row_retention_clause(ObForOracleParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockchain_hash_and_data_format_clause(ObForOracleParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollation_name(ObForOracleParser.Collation_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_properties(ObForOracleParser.Table_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#read_only_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead_only_clause(ObForOracleParser.Read_only_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#indexing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexing_clause(ObForOracleParser.Indexing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_clustering_clause(ObForOracleParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#clustering_join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_join(ObForOracleParser.Clustering_joinContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#clustering_join_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_join_item(ObForOracleParser.Clustering_join_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#equijoin_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquijoin_condition(ObForOracleParser.Equijoin_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cluster_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCluster_clause(ObForOracleParser.Cluster_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#clustering_columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_columns(ObForOracleParser.Clustering_columnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#clustering_column_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustering_column_group(ObForOracleParser.Clustering_column_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#yes_no}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYes_no(ObForOracleParser.Yes_noContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#zonemap_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZonemap_clause(ObForOracleParser.Zonemap_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_replication_clause(ObForOracleParser.Logical_replication_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(ObForOracleParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#relational_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_property(ObForOracleParser.Relational_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_partitioning_clauses(ObForOracleParser.Table_partitioning_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#range_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_partitions(ObForOracleParser.Range_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#list_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_partitions(ObForOracleParser.List_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partitions(ObForOracleParser.Hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividual_hash_partitions(ObForOracleParser.Individual_hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partitions_by_quantity(ObForOracleParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_partition_quantity(ObForOracleParser.Hash_partition_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_range_partitions(ObForOracleParser.Composite_range_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_list_partitions(ObForOracleParser.Composite_list_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposite_hash_partitions(ObForOracleParser.Composite_hash_partitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#reference_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_partitioning(ObForOracleParser.Reference_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_partition_desc(ObForOracleParser.Reference_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#system_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_partitioning(ObForOracleParser.System_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#range_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_partition_desc(ObForOracleParser.Range_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#list_partition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_partition_desc(ObForOracleParser.List_partition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_template(ObForOracleParser.Subpartition_templateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_subpartition_quantity(ObForOracleParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_range(ObForOracleParser.Subpartition_by_rangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_list(ObForOracleParser.Subpartition_by_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_by_hash(ObForOracleParser.Subpartition_by_hashContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_name(ObForOracleParser.Subpartition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_subpartition_desc(ObForOracleParser.Range_subpartition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_subpartition_desc(ObForOracleParser.List_subpartition_descContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividual_hash_subparts(ObForOracleParser.Individual_hash_subpartsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_subparts_by_quantity(ObForOracleParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#range_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_values_clause(ObForOracleParser.Range_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#range_values_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_values_list(ObForOracleParser.Range_values_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#list_values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_values_clause(ObForOracleParser.List_values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_partition_description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_partition_description(ObForOracleParser.Table_partition_descriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitioning_storage_clause(ObForOracleParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_partitioning_storage(ObForOracleParser.Lob_partitioning_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype_null_enable(ObForOracleParser.Datatype_null_enableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#size_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSize_clause(ObForOracleParser.Size_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_compression(ObForOracleParser.Table_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_table_clause(ObForOracleParser.Inmemory_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_attributes(ObForOracleParser.Inmemory_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_memcompress(ObForOracleParser.Inmemory_memcompressContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_priority}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_priority(ObForOracleParser.Inmemory_priorityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_distribute(ObForOracleParser.Inmemory_distributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_duplicate(ObForOracleParser.Inmemory_duplicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInmemory_column_clause(ObForOracleParser.Inmemory_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorage_clause(ObForOracleParser.Storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeferred_segment_creation(ObForOracleParser.Deferred_segment_creationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_attributes_clause(ObForOracleParser.Segment_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#physical_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPhysical_properties(ObForOracleParser.Physical_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_clause(ObForOracleParser.Ilm_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_policy_clause(ObForOracleParser.Ilm_policy_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_compression_policy(ObForOracleParser.Ilm_compression_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_tiering_policy(ObForOracleParser.Ilm_tiering_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_after_on}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_after_on(ObForOracleParser.Ilm_after_onContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#segment_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_group(ObForOracleParser.Segment_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_inmemory_policy(ObForOracleParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ilm_time_period}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlm_time_period(ObForOracleParser.Ilm_time_periodContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeap_org_table_clause(ObForOracleParser.Heap_org_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#external_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_table_clause(ObForOracleParser.External_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#access_driver_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess_driver_type(ObForOracleParser.Access_driver_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#external_table_data_props}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_table_data_props(ObForOracleParser.External_table_data_propsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpaque_format_spec(ObForOracleParser.Opaque_format_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#row_movement_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_movement_clause(ObForOracleParser.Row_movement_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_archive_clause(ObForOracleParser.Flashback_archive_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#log_grp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog_grp(ObForOracleParser.Log_grpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_table_logging(ObForOracleParser.Supplemental_table_loggingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_log_grp_clause(ObForOracleParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_id_key_clause(ObForOracleParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocate_extent_clause(ObForOracleParser.Allocate_extent_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocate_unused_clause(ObForOracleParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#shrink_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShrink_clause(ObForOracleParser.Shrink_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecords_per_block_clause(ObForOracleParser.Records_per_block_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpgrade_table_clause(ObForOracleParser.Upgrade_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#truncate_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_table(ObForOracleParser.Truncate_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table(ObForOracleParser.Drop_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_tablespace(ObForOracleParser.Drop_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_tablespace_set(ObForOracleParser.Drop_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#including_contents_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncluding_contents_clause(ObForOracleParser.Including_contents_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_view(ObForOracleParser.Drop_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#comment_on_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_column(ObForOracleParser.Comment_on_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#enable_or_disable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_or_disable(ObForOracleParser.Enable_or_disableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllow_or_disallow(ObForOracleParser.Allow_or_disallowContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_synonym(ObForOracleParser.Alter_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_synonym(ObForOracleParser.Create_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_synonym(ObForOracleParser.Drop_synonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_spfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_spfile(ObForOracleParser.Create_spfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#spfile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpfile_name(ObForOracleParser.Spfile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pfile_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPfile_name(ObForOracleParser.Pfile_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#comment_on_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_table(ObForOracleParser.Comment_on_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#comment_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_context(ObForOracleParser.Comment_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment_on_materialized(ObForOracleParser.Comment_on_materializedContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_analytic_view(ObForOracleParser.Alter_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_add_cache_clause(ObForOracleParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#levels_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevels_item(ObForOracleParser.Levels_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#measure_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeasure_list(ObForOracleParser.Measure_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_drop_cache_clause(ObForOracleParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_attribute_dimension(ObForOracleParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_audit_policy(ObForOracleParser.Alter_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_cluster(ObForOracleParser.Alter_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_analytic_view(ObForOracleParser.Drop_analytic_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_attribute_dimension(ObForOracleParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_audit_policy(ObForOracleParser.Drop_audit_policyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_flashback_archive(ObForOracleParser.Drop_flashback_archiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_cluster(ObForOracleParser.Drop_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_context(ObForOracleParser.Drop_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_directory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_directory(ObForOracleParser.Drop_directoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_diskgroup(ObForOracleParser.Drop_diskgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_edition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_edition(ObForOracleParser.Drop_editionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#truncate_cluster}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_cluster(ObForOracleParser.Truncate_clusterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCache_or_nocache(ObForOracleParser.Cache_or_nocacheContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_name(ObForOracleParser.Database_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database(ObForOracleParser.Alter_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_clause(ObForOracleParser.Database_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#startup_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartup_clauses(ObForOracleParser.Startup_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetlogs_or_noresetlogs(ObForOracleParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpgrade_or_downgrade(ObForOracleParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#recovery_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecovery_clauses(ObForOracleParser.Recovery_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#begin_or_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_or_end(ObForOracleParser.Begin_or_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#general_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_recovery(ObForOracleParser.General_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#full_database_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_database_recovery(ObForOracleParser.Full_database_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_database_recovery(ObForOracleParser.Partial_database_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_database_recovery_10g(ObForOracleParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitManaged_standby_recovery(ObForOracleParser.Managed_standby_recoveryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#db_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_name(ObForOracleParser.Db_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database_file_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_file_clauses(ObForOracleParser.Database_file_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_datafile_clause(ObForOracleParser.Create_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_datafile_clause(ObForOracleParser.Alter_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tempfile_clause(ObForOracleParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_datafile_clause(ObForOracleParser.Move_datafile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_clauses(ObForOracleParser.Logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_logfile_clauses(ObForOracleParser.Add_logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_redo_logfile(ObForOracleParser.Group_redo_logfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_logfile_clauses(ObForOracleParser.Drop_logfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_logfile_clause(ObForOracleParser.Switch_logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_db_logging(ObForOracleParser.Supplemental_db_loggingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_or_drop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_or_drop(ObForOracleParser.Add_or_dropContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_plsql_clause(ObForOracleParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogfile_descriptor(ObForOracleParser.Logfile_descriptorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlfile_clauses(ObForOracleParser.Controlfile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trace_file_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrace_file_clause(ObForOracleParser.Trace_file_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandby_database_clauses(ObForOracleParser.Standby_database_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivate_standby_db_clause(ObForOracleParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaximize_standby_db_clause(ObForOracleParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegister_logfile_clause(ObForOracleParser.Register_logfile_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_switchover_clause(ObForOracleParser.Commit_switchover_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#start_standby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_standby_clause(ObForOracleParser.Start_standby_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStop_standby_clause(ObForOracleParser.Stop_standby_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#convert_database_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert_database_clause(ObForOracleParser.Convert_database_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_settings_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_settings_clause(ObForOracleParser.Default_settings_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_time_zone_clause(ObForOracleParser.Set_time_zone_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#instance_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_clauses(ObForOracleParser.Instance_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#security_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity_clause(ObForOracleParser.Security_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#domain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain(ObForOracleParser.DomainContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase(ObForOracleParser.DatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#edition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdition_name(ObForOracleParser.Edition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filenumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilenumber(ObForOracleParser.FilenumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilename(ObForOracleParser.FilenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#prepare_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrepare_clause(ObForOracleParser.Prepare_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_mirror_clause(ObForOracleParser.Drop_mirror_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lost_write_protection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLost_write_protection(ObForOracleParser.Lost_write_protectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCdb_fleet_clauses(ObForOracleParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLead_cdb_clause(ObForOracleParser.Lead_cdb_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLead_cdb_uri_clause(ObForOracleParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#property_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_clauses(ObForOracleParser.Property_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplay_upgrade_clauses(ObForOracleParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database_link(ObForOracleParser.Alter_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#password_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword_value(ObForOracleParser.Password_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#link_authentication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLink_authentication(ObForOracleParser.Link_authenticationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_schema(ObForOracleParser.Create_schemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database(ObForOracleParser.Create_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_logging_clauses(ObForOracleParser.Database_logging_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_logging_sub_clause(ObForOracleParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_clauses(ObForOracleParser.Tablespace_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_pluggable_database(ObForOracleParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#file_name_convert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_name_convert(ObForOracleParser.File_name_convertContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilename_convert_sub_clause(ObForOracleParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace_datafile_clauses(ObForOracleParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_mode_clause(ObForOracleParser.Undo_mode_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_tablespace(ObForOracleParser.Default_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_temp_tablespace(ObForOracleParser.Default_temp_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#undo_tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndo_tablespace(ObForOracleParser.Undo_tablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_database(ObForOracleParser.Drop_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#create_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database_link(ObForOracleParser.Create_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_database_link}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_database_link(ObForOracleParser.Drop_database_linkContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_set(ObForOracleParser.Alter_tablespace_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_attrs(ObForOracleParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_tablespace_encryption(ObForOracleParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTs_file_name_convert(ObForOracleParser.Ts_file_name_convertContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_role(ObForOracleParser.Alter_roleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#role_identified_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_identified_clause(ObForOracleParser.Role_identified_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table(ObForOracleParser.Alter_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoptimize_read_write_clause(ObForOracleParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_table_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_properties(ObForOracleParser.Alter_table_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_partitioning(ObForOracleParser.Alter_table_partitioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_table_partition(ObForOracleParser.Add_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_partition(ObForOracleParser.Drop_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_table_partition(ObForOracleParser.Merge_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_table_partition(ObForOracleParser.Modify_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#split_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSplit_table_partition(ObForOracleParser.Split_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncate_table_partition(ObForOracleParser.Truncate_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExchange_table_partition(ObForOracleParser.Exchange_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesce_table_partition(ObForOracleParser.Coalesce_table_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_interval_partition(ObForOracleParser.Alter_interval_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_extended_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extended_names(ObForOracleParser.Partition_extended_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartition_extended_names(ObForOracleParser.Subpartition_extended_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_properties_1(ObForOracleParser.Alter_table_properties_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_iot_clauses(ObForOracleParser.Alter_iot_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_mapping_table_clause(ObForOracleParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_overflow_clause(ObForOracleParser.Alter_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_overflow_clause(ObForOracleParser.Add_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_index_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_clauses(ObForOracleParser.Update_index_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_global_index_clause(ObForOracleParser.Update_global_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_all_indexes_clause(ObForOracleParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_all_indexes_index_clause(ObForOracleParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_index_partition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_partition(ObForOracleParser.Update_index_partitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_index_subpartition(ObForOracleParser.Update_index_subpartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_disable_clause(ObForOracleParser.Enable_disable_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_index_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_index_clause(ObForOracleParser.Using_index_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_attributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_attributes(ObForOracleParser.Index_attributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_or_nosort(ObForOracleParser.Sort_or_nosortContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exceptions_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptions_clause(ObForOracleParser.Exceptions_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#move_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMove_table_clause(ObForOracleParser.Move_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_org_table_clause(ObForOracleParser.Index_org_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapping_table_clause(ObForOracleParser.Mapping_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#key_compression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_compression(ObForOracleParser.Key_compressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_org_overflow_clause(ObForOracleParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_clauses(ObForOracleParser.Column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_collection_retrieval(ObForOracleParser.Modify_collection_retrievalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collection_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_item(ObForOracleParser.Collection_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rename_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_column_clause(ObForOracleParser.Rename_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#old_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_column_name(ObForOracleParser.Old_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_column_name(ObForOracleParser.New_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_modify_drop_column_clauses(ObForOracleParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_column_clause(ObForOracleParser.Drop_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_column_clauses(ObForOracleParser.Modify_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_properties(ObForOracleParser.Modify_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#col_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol_default(ObForOracleParser.Col_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_visibility(ObForOracleParser.Modify_col_visibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_col_substitutable(ObForOracleParser.Modify_col_substitutableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_column_clause(ObForOracleParser.Add_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_varray_col_properties(ObForOracleParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#varray_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_col_properties(ObForOracleParser.Varray_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_storage_clause(ObForOracleParser.Varray_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_segname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_segname(ObForOracleParser.Lob_segnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_item(ObForOracleParser.Lob_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_storage_parameters(ObForOracleParser.Lob_storage_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_storage_clause(ObForOracleParser.Lob_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_lob_storage_clause(ObForOracleParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_lob_parameters(ObForOracleParser.Modify_lob_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_parameters(ObForOracleParser.Lob_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_deduplicate_clause(ObForOracleParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_compression_clause(ObForOracleParser.Lob_compression_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_retention_clause(ObForOracleParser.Lob_retention_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#encryption_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncryption_spec(ObForOracleParser.Encryption_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablespace(ObForOracleParser.TablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#varray_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_item(ObForOracleParser.Varray_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_properties(ObForOracleParser.Column_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLob_partition_storage(ObForOracleParser.Lob_partition_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#period_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPeriod_definition(ObForOracleParser.Period_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#start_time_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_time_column(ObForOracleParser.Start_time_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#end_time_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd_time_column(ObForOracleParser.End_time_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_definition(ObForOracleParser.Column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_collation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_collation_name(ObForOracleParser.Column_collation_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identity_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_clause(ObForOracleParser.Identity_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_options_parentheses(ObForOracleParser.Identity_options_parenthesesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identity_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity_options(ObForOracleParser.Identity_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtual_column_definition(ObForOracleParser.Virtual_column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutogenerated_sequence_definition(ObForOracleParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluation_edition_clause(ObForOracleParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_line_part_storage(ObForOracleParser.Out_of_line_part_storageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_table_col_properties(ObForOracleParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#nested_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested_item(ObForOracleParser.Nested_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstitutable_column_clause(ObForOracleParser.Substitutable_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_name(ObForOracleParser.Partition_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupplemental_logging_props(ObForOracleParser.Supplemental_logging_propsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_or_attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_or_attribute(ObForOracleParser.Column_or_attributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type_col_properties(ObForOracleParser.Object_type_col_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_table_constarint(ObForOracleParser.Add_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_table_constarint(ObForOracleParser.Modify_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_table_constarint(ObForOracleParser.Rename_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_constarint(ObForOracleParser.Drop_table_constarintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#old_constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld_constraint_name(ObForOracleParser.Old_constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#new_constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_constraint_name(ObForOracleParser.New_constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_constraint_clause(ObForOracleParser.Drop_constraint_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_constraint(ObForOracleParser.Add_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_constraint_clause(ObForOracleParser.Add_constraint_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#check_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheck_constraint(ObForOracleParser.Check_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#drop_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_constraint(ObForOracleParser.Drop_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#enable_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnable_constraint(ObForOracleParser.Enable_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#disable_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisable_constraint(ObForOracleParser.Disable_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_key_clause(ObForOracleParser.Foreign_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#references_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences_clause(ObForOracleParser.References_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#on_delete_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_delete_clause(ObForOracleParser.On_delete_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unique_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnique_key_clause(ObForOracleParser.Unique_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#primary_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_key_clause(ObForOracleParser.Primary_key_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#anonymous_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonymous_block(ObForOracleParser.Anonymous_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvoker_rights_clause(ObForOracleParser.Invoker_rights_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#call_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_spec(ObForOracleParser.Call_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#java_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJava_spec(ObForOracleParser.Java_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#c_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_spec(ObForOracleParser.C_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_agent_in_clause(ObForOracleParser.C_agent_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_parameters_clause(ObForOracleParser.C_parameters_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#c_external_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_external_parameter(ObForOracleParser.C_external_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#c_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_property(ObForOracleParser.C_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(ObForOracleParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#default_value_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_value_part(ObForOracleParser.Default_value_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq_of_declare_specs(ObForOracleParser.Seq_of_declare_specsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#declare_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_spec(ObForOracleParser.Declare_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration(ObForOracleParser.Variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subtype_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtype_declaration(ObForOracleParser.Subtype_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cursor_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_declaration(ObForOracleParser.Cursor_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parameter_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_spec(ObForOracleParser.Parameter_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exception_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_declaration(ObForOracleParser.Exception_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pragma_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_declaration(ObForOracleParser.Pragma_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#record_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_type_def(ObForOracleParser.Record_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#field_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_spec(ObForOracleParser.Field_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef_cursor_type_def(ObForOracleParser.Ref_cursor_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_declaration(ObForOracleParser.Type_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_type_def(ObForOracleParser.Table_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_indexed_by_part(ObForOracleParser.Table_indexed_by_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#varray_type_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarray_type_def(ObForOracleParser.Varray_type_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#seq_of_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq_of_statements(ObForOracleParser.Seq_of_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#label_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel_declaration(ObForOracleParser.Label_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ObForOracleParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwallow_to_semi(ObForOracleParser.Swallow_to_semiContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(ObForOracleParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#continue_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_statement(ObForOracleParser.Continue_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit_statement(ObForOracleParser.Exit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#goto_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoto_statement(ObForOracleParser.Goto_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(ObForOracleParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#elsif_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElsif_part(ObForOracleParser.Elsif_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_part(ObForOracleParser.Else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#loop_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop_statement(ObForOracleParser.Loop_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_loop_param(ObForOracleParser.Cursor_loop_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#forall_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForall_statement(ObForOracleParser.Forall_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#bounds_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBounds_clause(ObForOracleParser.Bounds_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#between_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetween_bound(ObForOracleParser.Between_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lower_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLower_bound(ObForOracleParser.Lower_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#upper_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpper_bound(ObForOracleParser.Upper_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#null_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_statement(ObForOracleParser.Null_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#raise_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaise_statement(ObForOracleParser.Raise_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(ObForOracleParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#call_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_statement(ObForOracleParser.Call_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipe_row_statement(ObForOracleParser.Pipe_row_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#selection_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection_directive(ObForOracleParser.Selection_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#error_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_directive(ObForOracleParser.Error_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#selection_directive_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection_directive_body(ObForOracleParser.Selection_directive_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(ObForOracleParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exception_handler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_handler(ObForOracleParser.Exception_handlerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trigger_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_block(ObForOracleParser.Trigger_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tps_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTps_block(ObForOracleParser.Tps_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ObForOracleParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_statement(ObForOracleParser.Sql_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#execute_immediate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_immediate(ObForOracleParser.Execute_immediateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamic_returning_clause(ObForOracleParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData_manipulation_language_statements(ObForOracleParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_manipulation_statements(ObForOracleParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#close_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_statement(ObForOracleParser.Close_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#open_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_statement(ObForOracleParser.Open_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#fetch_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_statement(ObForOracleParser.Fetch_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#variable_or_collection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_or_collection(ObForOracleParser.Variable_or_collectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#open_for_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_for_statement(ObForOracleParser.Open_for_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_control_statements(ObForOracleParser.Transaction_control_statementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_transaction_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_transaction_command(ObForOracleParser.Set_transaction_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_constraint_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_constraint_command(ObForOracleParser.Set_constraint_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#commit_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_statement(ObForOracleParser.Commit_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#write_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite_clause(ObForOracleParser.Write_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rollback_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_statement(ObForOracleParser.Rollback_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#savepoint_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_statement(ObForOracleParser.Savepoint_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collection_method_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_method_call(ObForOracleParser.Collection_method_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#explain_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain_statement(ObForOracleParser.Explain_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#select_only_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_only_statement(ObForOracleParser.Select_only_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#select_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_statement(ObForOracleParser.Select_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#with_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause(ObForOracleParser.With_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_factoring_clause(ObForOracleParser.With_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_factoring_clause(ObForOracleParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#search_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_clause(ObForOracleParser.Search_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cycle_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCycle_clause(ObForOracleParser.Cycle_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubav_factoring_clause(ObForOracleParser.Subav_factoring_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subav_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubav_clause(ObForOracleParser.Subav_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchies_clause(ObForOracleParser.Hierarchies_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filter_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clauses(ObForOracleParser.Filter_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#filter_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_clause(ObForOracleParser.Filter_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_calcs_clause(ObForOracleParser.Add_calcs_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_calc_meas_clause(ObForOracleParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(ObForOracleParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_basic_elements(ObForOracleParser.Subquery_basic_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_operation_part(ObForOracleParser.Subquery_operation_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#union_context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion_context(ObForOracleParser.Union_contextContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#query_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_block(ObForOracleParser.Query_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#selected_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelected_list(ObForOracleParser.Selected_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#from_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_clause(ObForOracleParser.From_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#select_list_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_elements(ObForOracleParser.Select_list_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_ref_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_list(ObForOracleParser.Table_ref_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref(ObForOracleParser.Table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_ref_aux}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux(ObForOracleParser.Table_ref_auxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_one(ObForOracleParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_two(ObForOracleParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_ref_aux_internal_thre(ObForOracleParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_clause(ObForOracleParser.Join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_type(ObForOracleParser.Join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_on_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_on_part(ObForOracleParser.Join_on_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#join_using_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_using_part(ObForOracleParser.Join_using_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#outer_join_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuter_join_type(ObForOracleParser.Outer_join_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#query_partition_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_partition_clause(ObForOracleParser.Query_partition_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlashback_query_clause(ObForOracleParser.Flashback_query_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_clause(ObForOracleParser.Pivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_element(ObForOracleParser.Pivot_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_for_clause(ObForOracleParser.Pivot_for_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause(ObForOracleParser.Pivot_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause_element(ObForOracleParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_in_clause_elements(ObForOracleParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unpivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_clause(ObForOracleParser.Unpivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_in_clause(ObForOracleParser.Unpivot_in_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_in_elements(ObForOracleParser.Unpivot_in_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchical_query_clause(ObForOracleParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#start_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_part(ObForOracleParser.Start_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#group_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_clause(ObForOracleParser.Group_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#group_by_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_elements(ObForOracleParser.Group_by_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollup_cube_clause(ObForOracleParser.Rollup_cube_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_clause(ObForOracleParser.Grouping_sets_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping_sets_elements(ObForOracleParser.Grouping_sets_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(ObForOracleParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_clause(ObForOracleParser.Model_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cell_reference_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell_reference_options(ObForOracleParser.Cell_reference_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#return_rows_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_rows_clause(ObForOracleParser.Return_rows_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#reference_model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_model(ObForOracleParser.Reference_modelContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#main_model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain_model(ObForOracleParser.Main_modelContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_column_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_clauses(ObForOracleParser.Model_column_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_partition_part(ObForOracleParser.Model_column_partition_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column_list(ObForOracleParser.Model_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_column(ObForOracleParser.Model_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_rules_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_clause(ObForOracleParser.Model_rules_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_rules_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_part(ObForOracleParser.Model_rules_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_rules_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_rules_element(ObForOracleParser.Model_rules_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cell_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell_assignment(ObForOracleParser.Cell_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_iterate_clause(ObForOracleParser.Model_iterate_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#until_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil_part(ObForOracleParser.Until_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_clause(ObForOracleParser.Order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#order_by_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_elements(ObForOracleParser.Order_by_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#offset_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffset_clause(ObForOracleParser.Offset_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#fetch_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_clause(ObForOracleParser.Fetch_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#for_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_clause(ObForOracleParser.For_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#for_update_of_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_of_part(ObForOracleParser.For_update_of_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#for_update_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update_options(ObForOracleParser.For_update_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_statement(ObForOracleParser.Update_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#update_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_set_clause(ObForOracleParser.Update_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_based_update_set_clause(ObForOracleParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#delete_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_statement(ObForOracleParser.Delete_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#insert_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_statement(ObForOracleParser.Insert_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#single_table_insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_table_insert(ObForOracleParser.Single_table_insertContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#multi_table_insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_table_insert(ObForOracleParser.Multi_table_insertContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#multi_table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_table_element(ObForOracleParser.Multi_table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_clause(ObForOracleParser.Conditional_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_when_part(ObForOracleParser.Conditional_insert_when_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_insert_else_part(ObForOracleParser.Conditional_insert_else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#insert_into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_into_clause(ObForOracleParser.Insert_into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#values_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_clause(ObForOracleParser.Values_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_statement(ObForOracleParser.Merge_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_update_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_clause(ObForOracleParser.Merge_update_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_element(ObForOracleParser.Merge_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_update_delete_part(ObForOracleParser.Merge_update_delete_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_insert_clause(ObForOracleParser.Merge_insert_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#selected_tableview}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelected_tableview(ObForOracleParser.Selected_tableviewContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lock_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_table_statement(ObForOracleParser.Lock_table_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait_nowait_part(ObForOracleParser.Wait_nowait_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lock_table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_table_element(ObForOracleParser.Lock_table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#lock_mode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock_mode(ObForOracleParser.Lock_modeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#general_table_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_table_ref(ObForOracleParser.General_table_refContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#static_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatic_returning_clause(ObForOracleParser.Static_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#error_logging_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_clause(ObForOracleParser.Error_logging_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_into_part(ObForOracleParser.Error_logging_into_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_logging_reject_part(ObForOracleParser.Error_logging_reject_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_table_expression_clause(ObForOracleParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_collection_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_collection_expression(ObForOracleParser.Table_collection_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_restriction_clause(ObForOracleParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sample_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSample_clause(ObForOracleParser.Sample_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#seed_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeed_part(ObForOracleParser.Seed_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(ObForOracleParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#expressions_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressions_(ObForOracleParser.Expressions_Context ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ObForOracleParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cursor_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_expression(ObForOracleParser.Cursor_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_expression(ObForOracleParser.Logical_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_logical_expression(ObForOracleParser.Unary_logical_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_logical_operation(ObForOracleParser.Unary_logical_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#logical_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_operation(ObForOracleParser.Logical_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#multiset_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiset_expression(ObForOracleParser.Multiset_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#relational_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_expression(ObForOracleParser.Relational_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#compound_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_expression(ObForOracleParser.Compound_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#relational_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_operator(ObForOracleParser.Relational_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#in_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_elements(ObForOracleParser.In_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#between_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetween_elements(ObForOracleParser.Between_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#concatenation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatenation(ObForOracleParser.ConcatenationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#interval_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_expression(ObForOracleParser.Interval_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_expression(ObForOracleParser.Model_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#model_expression_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_expression_element(ObForOracleParser.Model_expression_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_column_for_loop(ObForOracleParser.Single_column_for_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_column_for_loop(ObForOracleParser.Multi_column_for_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#unary_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expression(ObForOracleParser.Unary_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicit_cursor_expression(ObForOracleParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collection_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_expression(ObForOracleParser.Collection_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_statement(ObForOracleParser.Case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#simple_case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_case_statement(ObForOracleParser.Simple_case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#searched_case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearched_case_statement(ObForOracleParser.Searched_case_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_when_part_statement(ObForOracleParser.Case_when_part_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_else_part_statement(ObForOracleParser.Case_else_part_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expression(ObForOracleParser.Case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#simple_case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_case_expression(ObForOracleParser.Simple_case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#searched_case_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearched_case_expression(ObForOracleParser.Searched_case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_when_part_expression(ObForOracleParser.Case_when_part_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_else_part_expression(ObForOracleParser.Case_else_part_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(ObForOracleParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#quantified_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantified_expression(ObForOracleParser.Quantified_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#string_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_function(ObForOracleParser.String_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#standard_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_function(ObForOracleParser.Standard_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_function(ObForOracleParser.Json_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_object_content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_content(ObForOracleParser.Json_object_contentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_object_entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object_entry(ObForOracleParser.Json_object_entryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_table_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_table_clause(ObForOracleParser.Json_table_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_array_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array_element(ObForOracleParser.Json_array_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_on_null_clause(ObForOracleParser.Json_on_null_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_return_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_return_clause(ObForOracleParser.Json_return_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_transform_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_transform_op(ObForOracleParser.Json_transform_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_column_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_column_clause(ObForOracleParser.Json_column_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_column_definition(ObForOracleParser.Json_column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_returning_clause(ObForOracleParser.Json_query_returning_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_query_return_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_return_type(ObForOracleParser.Json_query_return_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_wrapper_clause(ObForOracleParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_on_error_clause(ObForOracleParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_query_on_empty_clause(ObForOracleParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_return_clause(ObForOracleParser.Json_value_return_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_value_return_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_return_type(ObForOracleParser.Json_value_return_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value_on_mismatch_clause(ObForOracleParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ObForOracleParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function_wrapper(ObForOracleParser.Numeric_function_wrapperContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#numeric_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function(ObForOracleParser.Numeric_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListagg_overflow_clause(ObForOracleParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#other_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOther_function(ObForOracleParser.Other_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause_keyword(ObForOracleParser.Over_clause_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_or_over_clause_keyword(ObForOracleParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandard_prediction_function_keyword(ObForOracleParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause(ObForOracleParser.Over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#windowing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_clause(ObForOracleParser.Windowing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#windowing_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_type(ObForOracleParser.Windowing_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#windowing_elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowing_elements(ObForOracleParser.Windowing_elementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_clause(ObForOracleParser.Using_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#using_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing_element(ObForOracleParser.Using_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollect_order_by_part(ObForOracleParser.Collect_order_by_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#within_or_over_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithin_or_over_part(ObForOracleParser.Within_or_over_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#string_delimiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_delimiter(ObForOracleParser.String_delimiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCost_matrix_clause(ObForOracleParser.Cost_matrix_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_passing_clause(ObForOracleParser.Xml_passing_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_attributes_clause(ObForOracleParser.Xml_attributes_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_namespaces_clause(ObForOracleParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_table_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_table_column(ObForOracleParser.Xml_table_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_general_default_part(ObForOracleParser.Xml_general_default_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_multiuse_expression_element(ObForOracleParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlroot_param_version_part(ObForOracleParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlroot_param_standalone_part(ObForOracleParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_enconding_part(ObForOracleParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_version_part(ObForOracleParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlserialize_param_ident_part(ObForOracleParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_plus_command_no_semicolon(ObForOracleParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sql_plus_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_plus_command(ObForOracleParser.Sql_plus_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#start_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_command(ObForOracleParser.Start_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#whenever_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenever_command(ObForOracleParser.Whenever_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#set_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_command(ObForOracleParser.Set_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#timing_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTiming_command(ObForOracleParser.Timing_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_extension_clause(ObForOracleParser.Partition_extension_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias(ObForOracleParser.Column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(ObForOracleParser.Table_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(ObForOracleParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#into_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInto_clause(ObForOracleParser.Into_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xml_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_column_name(ObForOracleParser.Xml_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cost_class_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCost_class_name(ObForOracleParser.Cost_class_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#attribute_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_name(ObForOracleParser.Attribute_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#savepoint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_name(ObForOracleParser.Savepoint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_segment_name(ObForOracleParser.Rollback_segment_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_var_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_var_name(ObForOracleParser.Table_var_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#schema_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_name(ObForOracleParser.Schema_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#routine_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_name(ObForOracleParser.Routine_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#package_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_name(ObForOracleParser.Package_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#implementation_type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementation_type_name(ObForOracleParser.Implementation_type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#parameter_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_name(ObForOracleParser.Parameter_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#reference_model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_model_name(ObForOracleParser.Reference_model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#main_model_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain_model_name(ObForOracleParser.Main_model_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#container_tableview_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainer_tableview_name(ObForOracleParser.Container_tableview_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_function_name(ObForOracleParser.Aggregate_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#query_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_name(ObForOracleParser.Query_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#grantee_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantee_name(ObForOracleParser.Grantee_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#role_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole_name(ObForOracleParser.Role_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constraint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint_name(ObForOracleParser.Constraint_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#label_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel_name(ObForOracleParser.Label_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name(ObForOracleParser.Type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#sequence_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence_name(ObForOracleParser.Sequence_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#exception_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitException_name(ObForOracleParser.Exception_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name(ObForOracleParser.Function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#procedure_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_name(ObForOracleParser.Procedure_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#trigger_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_name(ObForOracleParser.Trigger_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#variable_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_name(ObForOracleParser.Variable_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_name(ObForOracleParser.Index_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#cursor_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_name(ObForOracleParser.Cursor_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#record_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_name(ObForOracleParser.Record_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#collection_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection_name(ObForOracleParser.Collection_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#link_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLink_name(ObForOracleParser.Link_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#local_link_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_link_name(ObForOracleParser.Local_link_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#connection_qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnection_qualifier(ObForOracleParser.Connection_qualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(ObForOracleParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#tableview_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableview_name(ObForOracleParser.Tableview_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#xmltable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmltable(ObForOracleParser.XmltableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#char_set_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar_set_name(ObForOracleParser.Char_set_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#synonym_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynonym_name(ObForOracleParser.Synonym_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#schema_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_object_name(ObForOracleParser.Schema_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#dir_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDir_object_name(ObForOracleParser.Dir_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#user_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_object_name(ObForOracleParser.User_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#grant_object_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_object_name(ObForOracleParser.Grant_object_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_list(ObForOracleParser.Column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#insert_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_column_list(ObForOracleParser.Insert_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#paren_column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParen_column_list(ObForOracleParser.Paren_column_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#keep_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeep_clause(ObForOracleParser.Keep_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument(ObForOracleParser.Function_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument_analytic(ObForOracleParser.Function_argument_analyticContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_argument_modeling(ObForOracleParser.Function_argument_modelingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRespect_or_ignore_nulls(ObForOracleParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(ObForOracleParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#type_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_spec(ObForOracleParser.Type_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#custom_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustom_type(ObForOracleParser.Custom_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(ObForOracleParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#interval_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_type(ObForOracleParser.Interval_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#precision_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecision_part(ObForOracleParser.Precision_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#native_datatype_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNative_datatype_element(ObForOracleParser.Native_datatype_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#bind_variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBind_variable(ObForOracleParser.Bind_variableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#columnref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnref(ObForOracleParser.ColumnrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#general_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_element(ObForOracleParser.General_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#general_element_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneral_element_part(ObForOracleParser.General_element_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(ObForOracleParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#keyword_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword_function(ObForOracleParser.Keyword_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#table_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_element(ObForOracleParser.Table_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#object_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_privilege(ObForOracleParser.Object_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#system_privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystem_privilege(ObForOracleParser.System_privilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(ObForOracleParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(ObForOracleParser.NumericContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#numeric_negative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_negative(ObForOracleParser.Numeric_negativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#quoted_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuoted_string(ObForOracleParser.Quoted_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(ObForOracleParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#id_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_expression(ObForOracleParser.Id_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#inquiry_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInquiry_directive(ObForOracleParser.Inquiry_directiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#outer_join_sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuter_join_sign(ObForOracleParser.Outer_join_signContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#regular_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegular_id(ObForOracleParser.Regular_idContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_in_18c(ObForOracleParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_in_12c(ObForOracleParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_reserved_keywords_pre12c(ObForOracleParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#string_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_function_name(ObForOracleParser.String_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObForOracleParser#numeric_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_function_name(ObForOracleParser.Numeric_function_nameContext ctx);
}