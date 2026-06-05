// Generated from PlSqlParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oracle.parser.antlr;

    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PlSqlParser}.
 */
public interface PlSqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_script}.
	 * @param ctx the parse tree
	 */
	void enterSql_script(PlSqlParser.Sql_scriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_script}.
	 * @param ctx the parse tree
	 */
	void exitSql_script(PlSqlParser.Sql_scriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unit_statement}.
	 * @param ctx the parse tree
	 */
	void enterUnit_statement(PlSqlParser.Unit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unit_statement}.
	 * @param ctx the parse tree
	 */
	void exitUnit_statement(PlSqlParser.Unit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterAlter_diskgroup(PlSqlParser.Alter_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitAlter_diskgroup(PlSqlParser.Alter_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_disk_clause(PlSqlParser.Add_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_disk_clause(PlSqlParser.Add_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_disk_clause(PlSqlParser.Drop_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_disk_clause(PlSqlParser.Drop_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterResize_disk_clause(PlSqlParser.Resize_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitResize_disk_clause(PlSqlParser.Resize_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterReplace_disk_clause(PlSqlParser.Replace_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitReplace_disk_clause(PlSqlParser.Replace_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#wait_nowait}.
	 * @param ctx the parse tree
	 */
	void enterWait_nowait(PlSqlParser.Wait_nowaitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#wait_nowait}.
	 * @param ctx the parse tree
	 */
	void exitWait_nowait(PlSqlParser.Wait_nowaitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterRename_disk_clause(PlSqlParser.Rename_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitRename_disk_clause(PlSqlParser.Rename_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disk_online_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_online_clause(PlSqlParser.Disk_online_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disk_online_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_online_clause(PlSqlParser.Disk_online_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_offline_clause(PlSqlParser.Disk_offline_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_offline_clause(PlSqlParser.Disk_offline_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#timeout_clause}.
	 * @param ctx the parse tree
	 */
	void enterTimeout_clause(PlSqlParser.Timeout_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#timeout_clause}.
	 * @param ctx the parse tree
	 */
	void exitTimeout_clause(PlSqlParser.Timeout_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterRebalance_diskgroup_clause(PlSqlParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitRebalance_diskgroup_clause(PlSqlParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#phase}.
	 * @param ctx the parse tree
	 */
	void enterPhase(PlSqlParser.PhaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#phase}.
	 * @param ctx the parse tree
	 */
	void exitPhase(PlSqlParser.PhaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterCheck_diskgroup_clause(PlSqlParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitCheck_diskgroup_clause(PlSqlParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_template_clauses(PlSqlParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_template_clauses(PlSqlParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 */
	void enterQualified_template_clause(PlSqlParser.Qualified_template_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 */
	void exitQualified_template_clause(PlSqlParser.Qualified_template_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void enterRedundancy_clause(PlSqlParser.Redundancy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void exitRedundancy_clause(PlSqlParser.Redundancy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#striping_clause}.
	 * @param ctx the parse tree
	 */
	void enterStriping_clause(PlSqlParser.Striping_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#striping_clause}.
	 * @param ctx the parse tree
	 */
	void exitStriping_clause(PlSqlParser.Striping_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#force_noforce}.
	 * @param ctx the parse tree
	 */
	void enterForce_noforce(PlSqlParser.Force_noforceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#force_noforce}.
	 * @param ctx the parse tree
	 */
	void exitForce_noforce(PlSqlParser.Force_noforceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_directory_clauses(PlSqlParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_directory_clauses(PlSqlParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dir_name}.
	 * @param ctx the parse tree
	 */
	void enterDir_name(PlSqlParser.Dir_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dir_name}.
	 * @param ctx the parse tree
	 */
	void exitDir_name(PlSqlParser.Dir_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_alias_clauses(PlSqlParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_alias_clauses(PlSqlParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_volume_clauses(PlSqlParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_volume_clauses(PlSqlParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_volume_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_volume_clause(PlSqlParser.Add_volume_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_volume_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_volume_clause(PlSqlParser.Add_volume_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_volume_clause(PlSqlParser.Modify_volume_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_volume_clause(PlSqlParser.Modify_volume_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_attributes(PlSqlParser.Diskgroup_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_attributes(PlSqlParser.Diskgroup_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 */
	void enterModify_diskgroup_file(PlSqlParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 */
	void exitModify_diskgroup_file(PlSqlParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disk_region_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_region_clause(PlSqlParser.Disk_region_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disk_region_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_region_clause(PlSqlParser.Disk_region_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_diskgroup_file_clause(PlSqlParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_diskgroup_file_clause(PlSqlParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void enterConvert_redundancy_clause(PlSqlParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void exitConvert_redundancy_clause(PlSqlParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUsergroup_clauses(PlSqlParser.Usergroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUsergroup_clauses(PlSqlParser.Usergroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUser_clauses(PlSqlParser.User_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUser_clauses(PlSqlParser.User_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 */
	void enterFile_permissions_clause(PlSqlParser.File_permissions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 */
	void exitFile_permissions_clause(PlSqlParser.File_permissions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#file_owner_clause}.
	 * @param ctx the parse tree
	 */
	void enterFile_owner_clause(PlSqlParser.File_owner_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#file_owner_clause}.
	 * @param ctx the parse tree
	 */
	void exitFile_owner_clause(PlSqlParser.File_owner_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#scrub_clause}.
	 * @param ctx the parse tree
	 */
	void enterScrub_clause(PlSqlParser.Scrub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#scrub_clause}.
	 * @param ctx the parse tree
	 */
	void exitScrub_clause(PlSqlParser.Scrub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterQuotagroup_clauses(PlSqlParser.Quotagroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitQuotagroup_clauses(PlSqlParser.Quotagroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#property_name}.
	 * @param ctx the parse tree
	 */
	void enterProperty_name(PlSqlParser.Property_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#property_name}.
	 * @param ctx the parse tree
	 */
	void exitProperty_name(PlSqlParser.Property_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#property_value}.
	 * @param ctx the parse tree
	 */
	void enterProperty_value(PlSqlParser.Property_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#property_value}.
	 * @param ctx the parse tree
	 */
	void exitProperty_value(PlSqlParser.Property_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterFilegroup_clauses(PlSqlParser.Filegroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitFilegroup_clauses(PlSqlParser.Filegroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_filegroup_clause(PlSqlParser.Add_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_filegroup_clause(PlSqlParser.Add_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_filegroup_clause(PlSqlParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_filegroup_clause(PlSqlParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_to_filegroup_clause(PlSqlParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_to_filegroup_clause(PlSqlParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_filegroup_clause(PlSqlParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_filegroup_clause(PlSqlParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#quorum_regular}.
	 * @param ctx the parse tree
	 */
	void enterQuorum_regular(PlSqlParser.Quorum_regularContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#quorum_regular}.
	 * @param ctx the parse tree
	 */
	void exitQuorum_regular(PlSqlParser.Quorum_regularContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndrop_disk_clause(PlSqlParser.Undrop_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndrop_disk_clause(PlSqlParser.Undrop_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_availability(PlSqlParser.Diskgroup_availabilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_availability(PlSqlParser.Diskgroup_availabilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 */
	void enterEnable_disable_volume(PlSqlParser.Enable_disable_volumeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 */
	void exitEnable_disable_volume(PlSqlParser.Enable_disable_volumeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_function}.
	 * @param ctx the parse tree
	 */
	void enterDrop_function(PlSqlParser.Drop_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_function}.
	 * @param ctx the parse tree
	 */
	void exitDrop_function(PlSqlParser.Drop_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterAlter_flashback_archive(PlSqlParser.Alter_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitAlter_flashback_archive(PlSqlParser.Alter_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterAlter_hierarchy(PlSqlParser.Alter_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitAlter_hierarchy(PlSqlParser.Alter_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_function}.
	 * @param ctx the parse tree
	 */
	void enterAlter_function(PlSqlParser.Alter_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_function}.
	 * @param ctx the parse tree
	 */
	void exitAlter_function(PlSqlParser.Alter_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_java}.
	 * @param ctx the parse tree
	 */
	void enterAlter_java(PlSqlParser.Alter_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_java}.
	 * @param ctx the parse tree
	 */
	void exitAlter_java(PlSqlParser.Alter_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#match_string}.
	 * @param ctx the parse tree
	 */
	void enterMatch_string(PlSqlParser.Match_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#match_string}.
	 * @param ctx the parse tree
	 */
	void exitMatch_string(PlSqlParser.Match_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_function_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_function_body(PlSqlParser.Create_function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_function_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_function_body(PlSqlParser.Create_function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_macro_body}.
	 * @param ctx the parse tree
	 */
	void enterSql_macro_body(PlSqlParser.Sql_macro_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_macro_body}.
	 * @param ctx the parse tree
	 */
	void exitSql_macro_body(PlSqlParser.Sql_macro_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 */
	void enterParallel_enable_clause(PlSqlParser.Parallel_enable_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 */
	void exitParallel_enable_clause(PlSqlParser.Parallel_enable_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_by_clause(PlSqlParser.Partition_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_by_clause(PlSqlParser.Partition_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#result_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterResult_cache_clause(PlSqlParser.Result_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#result_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitResult_cache_clause(PlSqlParser.Result_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterAccessible_by_clause(PlSqlParser.Accessible_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitAccessible_by_clause(PlSqlParser.Accessible_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_collation_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_collation_clause(PlSqlParser.Default_collation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_collation_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_collation_clause(PlSqlParser.Default_collation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_clause(PlSqlParser.Aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_clause(PlSqlParser.Aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterPipelined_using_clause(PlSqlParser.Pipelined_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitPipelined_using_clause(PlSqlParser.Pipelined_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#accessor}.
	 * @param ctx the parse tree
	 */
	void enterAccessor(PlSqlParser.AccessorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#accessor}.
	 * @param ctx the parse tree
	 */
	void exitAccessor(PlSqlParser.AccessorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#relies_on_part}.
	 * @param ctx the parse tree
	 */
	void enterRelies_on_part(PlSqlParser.Relies_on_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#relies_on_part}.
	 * @param ctx the parse tree
	 */
	void exitRelies_on_part(PlSqlParser.Relies_on_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#streaming_clause}.
	 * @param ctx the parse tree
	 */
	void enterStreaming_clause(PlSqlParser.Streaming_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#streaming_clause}.
	 * @param ctx the parse tree
	 */
	void exitStreaming_clause(PlSqlParser.Streaming_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_outline}.
	 * @param ctx the parse tree
	 */
	void enterAlter_outline(PlSqlParser.Alter_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_outline}.
	 * @param ctx the parse tree
	 */
	void exitAlter_outline(PlSqlParser.Alter_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#outline_options}.
	 * @param ctx the parse tree
	 */
	void enterOutline_options(PlSqlParser.Outline_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#outline_options}.
	 * @param ctx the parse tree
	 */
	void exitOutline_options(PlSqlParser.Outline_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterAlter_lockdown_profile(PlSqlParser.Alter_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitAlter_lockdown_profile(PlSqlParser.Alter_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lockdown_feature}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_feature(PlSqlParser.Lockdown_featureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lockdown_feature}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_feature(PlSqlParser.Lockdown_featureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lockdown_options}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_options(PlSqlParser.Lockdown_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lockdown_options}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_options(PlSqlParser.Lockdown_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lockdown_statements}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_statements(PlSqlParser.Lockdown_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lockdown_statements}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_statements(PlSqlParser.Lockdown_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#statement_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStatement_clauses(PlSqlParser.Statement_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#statement_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStatement_clauses(PlSqlParser.Statement_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#clause_options}.
	 * @param ctx the parse tree
	 */
	void enterClause_options(PlSqlParser.Clause_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#clause_options}.
	 * @param ctx the parse tree
	 */
	void exitClause_options(PlSqlParser.Clause_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#option_values}.
	 * @param ctx the parse tree
	 */
	void enterOption_values(PlSqlParser.Option_valuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#option_values}.
	 * @param ctx the parse tree
	 */
	void exitOption_values(PlSqlParser.Option_valuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#string_list}.
	 * @param ctx the parse tree
	 */
	void enterString_list(PlSqlParser.String_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#string_list}.
	 * @param ctx the parse tree
	 */
	void exitString_list(PlSqlParser.String_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disable_enable}.
	 * @param ctx the parse tree
	 */
	void enterDisable_enable(PlSqlParser.Disable_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disable_enable}.
	 * @param ctx the parse tree
	 */
	void exitDisable_enable(PlSqlParser.Disable_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterDrop_lockdown_profile(PlSqlParser.Drop_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitDrop_lockdown_profile(PlSqlParser.Drop_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_package}.
	 * @param ctx the parse tree
	 */
	void enterDrop_package(PlSqlParser.Drop_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_package}.
	 * @param ctx the parse tree
	 */
	void exitDrop_package(PlSqlParser.Drop_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_package}.
	 * @param ctx the parse tree
	 */
	void enterAlter_package(PlSqlParser.Alter_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_package}.
	 * @param ctx the parse tree
	 */
	void exitAlter_package(PlSqlParser.Alter_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_package}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package(PlSqlParser.Create_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_package}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package(PlSqlParser.Create_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_package_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package_body(PlSqlParser.Create_package_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_package_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package_body(PlSqlParser.Create_package_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#package_obj_spec}.
	 * @param ctx the parse tree
	 */
	void enterPackage_obj_spec(PlSqlParser.Package_obj_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#package_obj_spec}.
	 * @param ctx the parse tree
	 */
	void exitPackage_obj_spec(PlSqlParser.Package_obj_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#procedure_spec}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_spec(PlSqlParser.Procedure_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#procedure_spec}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_spec(PlSqlParser.Procedure_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_spec}.
	 * @param ctx the parse tree
	 */
	void enterFunction_spec(PlSqlParser.Function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_spec}.
	 * @param ctx the parse tree
	 */
	void exitFunction_spec(PlSqlParser.Function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#package_obj_body}.
	 * @param ctx the parse tree
	 */
	void enterPackage_obj_body(PlSqlParser.Package_obj_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#package_obj_body}.
	 * @param ctx the parse tree
	 */
	void exitPackage_obj_body(PlSqlParser.Package_obj_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterAlter_pmem_filestore(PlSqlParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitAlter_pmem_filestore(PlSqlParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterDrop_pmem_filestore(PlSqlParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitDrop_pmem_filestore(PlSqlParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_procedure}.
	 * @param ctx the parse tree
	 */
	void enterDrop_procedure(PlSqlParser.Drop_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_procedure}.
	 * @param ctx the parse tree
	 */
	void exitDrop_procedure(PlSqlParser.Drop_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_procedure}.
	 * @param ctx the parse tree
	 */
	void enterAlter_procedure(PlSqlParser.Alter_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_procedure}.
	 * @param ctx the parse tree
	 */
	void exitAlter_procedure(PlSqlParser.Alter_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_body}.
	 * @param ctx the parse tree
	 */
	void enterFunction_body(PlSqlParser.Function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_body}.
	 * @param ctx the parse tree
	 */
	void exitFunction_body(PlSqlParser.Function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#procedure_body}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_body(PlSqlParser.Procedure_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#procedure_body}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_body(PlSqlParser.Procedure_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_procedure_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_procedure_body(PlSqlParser.Create_procedure_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_procedure_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_procedure_body(PlSqlParser.Create_procedure_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 */
	void enterAlter_resource_cost(PlSqlParser.Alter_resource_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 */
	void exitAlter_resource_cost(PlSqlParser.Alter_resource_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_outline}.
	 * @param ctx the parse tree
	 */
	void enterDrop_outline(PlSqlParser.Drop_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_outline}.
	 * @param ctx the parse tree
	 */
	void exitDrop_outline(PlSqlParser.Drop_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterAlter_rollback_segment(PlSqlParser.Alter_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitAlter_rollback_segment(PlSqlParser.Alter_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_restore_point}.
	 * @param ctx the parse tree
	 */
	void enterDrop_restore_point(PlSqlParser.Drop_restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_restore_point}.
	 * @param ctx the parse tree
	 */
	void exitDrop_restore_point(PlSqlParser.Drop_restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterDrop_rollback_segment(PlSqlParser.Drop_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitDrop_rollback_segment(PlSqlParser.Drop_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_role}.
	 * @param ctx the parse tree
	 */
	void enterDrop_role(PlSqlParser.Drop_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_role}.
	 * @param ctx the parse tree
	 */
	void exitDrop_role(PlSqlParser.Drop_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterCreate_pmem_filestore(PlSqlParser.Create_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitCreate_pmem_filestore(PlSqlParser.Create_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 */
	void enterPmem_filestore_options(PlSqlParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 */
	void exitPmem_filestore_options(PlSqlParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#file_path}.
	 * @param ctx the parse tree
	 */
	void enterFile_path(PlSqlParser.File_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#file_path}.
	 * @param ctx the parse tree
	 */
	void exitFile_path(PlSqlParser.File_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterCreate_rollback_segment(PlSqlParser.Create_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitCreate_rollback_segment(PlSqlParser.Create_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_trigger}.
	 * @param ctx the parse tree
	 */
	void enterDrop_trigger(PlSqlParser.Drop_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_trigger}.
	 * @param ctx the parse tree
	 */
	void exitDrop_trigger(PlSqlParser.Drop_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_trigger}.
	 * @param ctx the parse tree
	 */
	void enterAlter_trigger(PlSqlParser.Alter_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_trigger}.
	 * @param ctx the parse tree
	 */
	void exitAlter_trigger(PlSqlParser.Alter_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_trigger}.
	 * @param ctx the parse tree
	 */
	void enterCreate_trigger(PlSqlParser.Create_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_trigger}.
	 * @param ctx the parse tree
	 */
	void exitCreate_trigger(PlSqlParser.Create_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_follows_clause(PlSqlParser.Trigger_follows_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_follows_clause(PlSqlParser.Trigger_follows_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_when_clause(PlSqlParser.Trigger_when_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_when_clause(PlSqlParser.Trigger_when_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterSimple_dml_trigger(PlSqlParser.Simple_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitSimple_dml_trigger(PlSqlParser.Simple_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#for_each_row}.
	 * @param ctx the parse tree
	 */
	void enterFor_each_row(PlSqlParser.For_each_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#for_each_row}.
	 * @param ctx the parse tree
	 */
	void exitFor_each_row(PlSqlParser.For_each_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterCompound_dml_trigger(PlSqlParser.Compound_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitCompound_dml_trigger(PlSqlParser.Compound_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterNon_dml_trigger(PlSqlParser.Non_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitNon_dml_trigger(PlSqlParser.Non_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trigger_body}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_body(PlSqlParser.Trigger_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trigger_body}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_body(PlSqlParser.Trigger_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#routine_clause}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_clause(PlSqlParser.Routine_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#routine_clause}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_clause(PlSqlParser.Routine_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 */
	void enterCompound_trigger_block(PlSqlParser.Compound_trigger_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 */
	void exitCompound_trigger_block(PlSqlParser.Compound_trigger_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#timing_point_section}.
	 * @param ctx the parse tree
	 */
	void enterTiming_point_section(PlSqlParser.Timing_point_sectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#timing_point_section}.
	 * @param ctx the parse tree
	 */
	void exitTiming_point_section(PlSqlParser.Timing_point_sectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#non_dml_event}.
	 * @param ctx the parse tree
	 */
	void enterNon_dml_event(PlSqlParser.Non_dml_eventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#non_dml_event}.
	 * @param ctx the parse tree
	 */
	void exitNon_dml_event(PlSqlParser.Non_dml_eventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dml_event_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_clause(PlSqlParser.Dml_event_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dml_event_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_clause(PlSqlParser.Dml_event_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dml_event_element}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_element(PlSqlParser.Dml_event_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dml_event_element}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_element(PlSqlParser.Dml_event_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_nested_clause(PlSqlParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_nested_clause(PlSqlParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#referencing_clause}.
	 * @param ctx the parse tree
	 */
	void enterReferencing_clause(PlSqlParser.Referencing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#referencing_clause}.
	 * @param ctx the parse tree
	 */
	void exitReferencing_clause(PlSqlParser.Referencing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#referencing_element}.
	 * @param ctx the parse tree
	 */
	void enterReferencing_element(PlSqlParser.Referencing_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#referencing_element}.
	 * @param ctx the parse tree
	 */
	void exitReferencing_element(PlSqlParser.Referencing_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_type}.
	 * @param ctx the parse tree
	 */
	void enterDrop_type(PlSqlParser.Drop_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_type}.
	 * @param ctx the parse tree
	 */
	void exitDrop_type(PlSqlParser.Drop_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_type}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type(PlSqlParser.Alter_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_type}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type(PlSqlParser.Alter_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compile_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterCompile_type_clause(PlSqlParser.Compile_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compile_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitCompile_type_clause(PlSqlParser.Compile_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#replace_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterReplace_type_clause(PlSqlParser.Replace_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#replace_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitReplace_type_clause(PlSqlParser.Replace_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_method_spec}.
	 * @param ctx the parse tree
	 */
	void enterAlter_method_spec(PlSqlParser.Alter_method_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_method_spec}.
	 * @param ctx the parse tree
	 */
	void exitAlter_method_spec(PlSqlParser.Alter_method_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_method_element}.
	 * @param ctx the parse tree
	 */
	void enterAlter_method_element(PlSqlParser.Alter_method_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_method_element}.
	 * @param ctx the parse tree
	 */
	void exitAlter_method_element(PlSqlParser.Alter_method_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 */
	void enterAlter_attribute_definition(PlSqlParser.Alter_attribute_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 */
	void exitAlter_attribute_definition(PlSqlParser.Alter_attribute_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#attribute_definition}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_definition(PlSqlParser.Attribute_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#attribute_definition}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_definition(PlSqlParser.Attribute_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAlter_collection_clauses(PlSqlParser.Alter_collection_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAlter_collection_clauses(PlSqlParser.Alter_collection_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 */
	void enterDependent_handling_clause(PlSqlParser.Dependent_handling_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 */
	void exitDependent_handling_clause(PlSqlParser.Dependent_handling_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 */
	void enterDependent_exceptions_part(PlSqlParser.Dependent_exceptions_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 */
	void exitDependent_exceptions_part(PlSqlParser.Dependent_exceptions_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_type}.
	 * @param ctx the parse tree
	 */
	void enterCreate_type(PlSqlParser.Create_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_type}.
	 * @param ctx the parse tree
	 */
	void exitCreate_type(PlSqlParser.Create_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_definition}.
	 * @param ctx the parse tree
	 */
	void enterType_definition(PlSqlParser.Type_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_definition}.
	 * @param ctx the parse tree
	 */
	void exitType_definition(PlSqlParser.Type_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_type_def}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_def(PlSqlParser.Object_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_type_def}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_def(PlSqlParser.Object_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_as_part}.
	 * @param ctx the parse tree
	 */
	void enterObject_as_part(PlSqlParser.Object_as_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_as_part}.
	 * @param ctx the parse tree
	 */
	void exitObject_as_part(PlSqlParser.Object_as_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_under_part}.
	 * @param ctx the parse tree
	 */
	void enterObject_under_part(PlSqlParser.Object_under_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_under_part}.
	 * @param ctx the parse tree
	 */
	void exitObject_under_part(PlSqlParser.Object_under_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 */
	void enterNested_table_type_def(PlSqlParser.Nested_table_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 */
	void exitNested_table_type_def(PlSqlParser.Nested_table_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 */
	void enterSqlj_object_type(PlSqlParser.Sqlj_object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 */
	void exitSqlj_object_type(PlSqlParser.Sqlj_object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_body}.
	 * @param ctx the parse tree
	 */
	void enterType_body(PlSqlParser.Type_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_body}.
	 * @param ctx the parse tree
	 */
	void exitType_body(PlSqlParser.Type_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_body_elements}.
	 * @param ctx the parse tree
	 */
	void enterType_body_elements(PlSqlParser.Type_body_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_body_elements}.
	 * @param ctx the parse tree
	 */
	void exitType_body_elements(PlSqlParser.Type_body_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMap_order_func_declaration(PlSqlParser.Map_order_func_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMap_order_func_declaration(PlSqlParser.Map_order_func_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterSubprog_decl_in_type(PlSqlParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitSubprog_decl_in_type(PlSqlParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterProc_decl_in_type(PlSqlParser.Proc_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitProc_decl_in_type(PlSqlParser.Proc_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterFunc_decl_in_type(PlSqlParser.Func_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitFunc_decl_in_type(PlSqlParser.Func_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constructor_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_declaration(PlSqlParser.Constructor_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constructor_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_declaration(PlSqlParser.Constructor_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modifier_clause}.
	 * @param ctx the parse tree
	 */
	void enterModifier_clause(PlSqlParser.Modifier_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modifier_clause}.
	 * @param ctx the parse tree
	 */
	void exitModifier_clause(PlSqlParser.Modifier_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_member_spec}.
	 * @param ctx the parse tree
	 */
	void enterObject_member_spec(PlSqlParser.Object_member_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_member_spec}.
	 * @param ctx the parse tree
	 */
	void exitObject_member_spec(PlSqlParser.Object_member_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 */
	void enterSqlj_object_type_attr(PlSqlParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 */
	void exitSqlj_object_type_attr(PlSqlParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void enterElement_spec(PlSqlParser.Element_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void exitElement_spec(PlSqlParser.Element_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#element_spec_options}.
	 * @param ctx the parse tree
	 */
	void enterElement_spec_options(PlSqlParser.Element_spec_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#element_spec_options}.
	 * @param ctx the parse tree
	 */
	void exitElement_spec_options(PlSqlParser.Element_spec_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void enterSubprogram_spec(PlSqlParser.Subprogram_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void exitSubprogram_spec(PlSqlParser.Subprogram_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void enterOverriding_subprogram_spec(PlSqlParser.Overriding_subprogram_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void exitOverriding_subprogram_spec(PlSqlParser.Overriding_subprogram_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterOverriding_function_spec(PlSqlParser.Overriding_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitOverriding_function_spec(PlSqlParser.Overriding_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_procedure_spec(PlSqlParser.Type_procedure_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_procedure_spec(PlSqlParser.Type_procedure_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_function_spec(PlSqlParser.Type_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_function_spec(PlSqlParser.Type_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constructor_spec}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_spec(PlSqlParser.Constructor_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constructor_spec}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_spec(PlSqlParser.Constructor_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterMap_order_function_spec(PlSqlParser.Map_order_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitMap_order_function_spec(PlSqlParser.Map_order_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pragma_clause}.
	 * @param ctx the parse tree
	 */
	void enterPragma_clause(PlSqlParser.Pragma_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pragma_clause}.
	 * @param ctx the parse tree
	 */
	void exitPragma_clause(PlSqlParser.Pragma_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pragma_elements}.
	 * @param ctx the parse tree
	 */
	void enterPragma_elements(PlSqlParser.Pragma_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pragma_elements}.
	 * @param ctx the parse tree
	 */
	void exitPragma_elements(PlSqlParser.Pragma_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 */
	void enterType_elements_parameter(PlSqlParser.Type_elements_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 */
	void exitType_elements_parameter(PlSqlParser.Type_elements_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_sequence}.
	 * @param ctx the parse tree
	 */
	void enterDrop_sequence(PlSqlParser.Drop_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_sequence}.
	 * @param ctx the parse tree
	 */
	void exitDrop_sequence(PlSqlParser.Drop_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_sequence}.
	 * @param ctx the parse tree
	 */
	void enterAlter_sequence(PlSqlParser.Alter_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_sequence}.
	 * @param ctx the parse tree
	 */
	void exitAlter_sequence(PlSqlParser.Alter_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_session}.
	 * @param ctx the parse tree
	 */
	void enterAlter_session(PlSqlParser.Alter_sessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_session}.
	 * @param ctx the parse tree
	 */
	void exitAlter_session(PlSqlParser.Alter_sessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_session_set_clause(PlSqlParser.Alter_session_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_session_set_clause(PlSqlParser.Alter_session_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_system}.
	 * @param ctx the parse tree
	 */
	void enterAlter_system(PlSqlParser.Alter_systemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_system}.
	 * @param ctx the parse tree
	 */
	void exitAlter_system(PlSqlParser.Alter_systemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_sequence}.
	 * @param ctx the parse tree
	 */
	void enterCreate_sequence(PlSqlParser.Create_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_sequence}.
	 * @param ctx the parse tree
	 */
	void exitCreate_sequence(PlSqlParser.Create_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sequence_spec}.
	 * @param ctx the parse tree
	 */
	void enterSequence_spec(PlSqlParser.Sequence_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sequence_spec}.
	 * @param ctx the parse tree
	 */
	void exitSequence_spec(PlSqlParser.Sequence_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 */
	void enterSequence_start_clause(PlSqlParser.Sequence_start_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 */
	void exitSequence_start_clause(PlSqlParser.Sequence_start_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_analytic_view(PlSqlParser.Create_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_analytic_view(PlSqlParser.Create_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#classification_clause}.
	 * @param ctx the parse tree
	 */
	void enterClassification_clause(PlSqlParser.Classification_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#classification_clause}.
	 * @param ctx the parse tree
	 */
	void exitClassification_clause(PlSqlParser.Classification_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#caption_clause}.
	 * @param ctx the parse tree
	 */
	void enterCaption_clause(PlSqlParser.Caption_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#caption_clause}.
	 * @param ctx the parse tree
	 */
	void exitCaption_clause(PlSqlParser.Caption_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#description_clause}.
	 * @param ctx the parse tree
	 */
	void enterDescription_clause(PlSqlParser.Description_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#description_clause}.
	 * @param ctx the parse tree
	 */
	void exitDescription_clause(PlSqlParser.Description_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#classification_item}.
	 * @param ctx the parse tree
	 */
	void enterClassification_item(PlSqlParser.Classification_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#classification_item}.
	 * @param ctx the parse tree
	 */
	void exitClassification_item(PlSqlParser.Classification_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#language}.
	 * @param ctx the parse tree
	 */
	void enterLanguage(PlSqlParser.LanguageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#language}.
	 * @param ctx the parse tree
	 */
	void exitLanguage(PlSqlParser.LanguageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cav_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterCav_using_clause(PlSqlParser.Cav_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cav_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitCav_using_clause(PlSqlParser.Cav_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dim_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterDim_by_clause(PlSqlParser.Dim_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dim_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitDim_by_clause(PlSqlParser.Dim_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dim_key}.
	 * @param ctx the parse tree
	 */
	void enterDim_key(PlSqlParser.Dim_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dim_key}.
	 * @param ctx the parse tree
	 */
	void exitDim_key(PlSqlParser.Dim_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dim_ref}.
	 * @param ctx the parse tree
	 */
	void enterDim_ref(PlSqlParser.Dim_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dim_ref}.
	 * @param ctx the parse tree
	 */
	void exitDim_ref(PlSqlParser.Dim_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hier_ref}.
	 * @param ctx the parse tree
	 */
	void enterHier_ref(PlSqlParser.Hier_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hier_ref}.
	 * @param ctx the parse tree
	 */
	void exitHier_ref(PlSqlParser.Hier_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#measures_clause}.
	 * @param ctx the parse tree
	 */
	void enterMeasures_clause(PlSqlParser.Measures_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#measures_clause}.
	 * @param ctx the parse tree
	 */
	void exitMeasures_clause(PlSqlParser.Measures_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#av_measure}.
	 * @param ctx the parse tree
	 */
	void enterAv_measure(PlSqlParser.Av_measureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#av_measure}.
	 * @param ctx the parse tree
	 */
	void exitAv_measure(PlSqlParser.Av_measureContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#base_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterBase_meas_clause(PlSqlParser.Base_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#base_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitBase_meas_clause(PlSqlParser.Base_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterMeas_aggregate_clause(PlSqlParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitMeas_aggregate_clause(PlSqlParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterCalc_meas_clause(PlSqlParser.Calc_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitCalc_meas_clause(PlSqlParser.Calc_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_measure_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_measure_clause(PlSqlParser.Default_measure_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_measure_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_measure_clause(PlSqlParser.Default_measure_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_aggregate_clause(PlSqlParser.Default_aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_aggregate_clause(PlSqlParser.Default_aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterCache_clause(PlSqlParser.Cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitCache_clause(PlSqlParser.Cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cache_specification}.
	 * @param ctx the parse tree
	 */
	void enterCache_specification(PlSqlParser.Cache_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cache_specification}.
	 * @param ctx the parse tree
	 */
	void exitCache_specification(PlSqlParser.Cache_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#levels_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevels_clause(PlSqlParser.Levels_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#levels_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevels_clause(PlSqlParser.Levels_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#level_specification}.
	 * @param ctx the parse tree
	 */
	void enterLevel_specification(PlSqlParser.Level_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#level_specification}.
	 * @param ctx the parse tree
	 */
	void exitLevel_specification(PlSqlParser.Level_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#level_group_type}.
	 * @param ctx the parse tree
	 */
	void enterLevel_group_type(PlSqlParser.Level_group_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#level_group_type}.
	 * @param ctx the parse tree
	 */
	void exitLevel_group_type(PlSqlParser.Level_group_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 */
	void enterFact_columns_clause(PlSqlParser.Fact_columns_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 */
	void exitFact_columns_clause(PlSqlParser.Fact_columns_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 */
	void enterQry_transform_clause(PlSqlParser.Qry_transform_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 */
	void exitQry_transform_clause(PlSqlParser.Qry_transform_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterCreate_attribute_dimension(PlSqlParser.Create_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitCreate_attribute_dimension(PlSqlParser.Create_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ad_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_using_clause(PlSqlParser.Ad_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ad_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_using_clause(PlSqlParser.Ad_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#source_clause}.
	 * @param ctx the parse tree
	 */
	void enterSource_clause(PlSqlParser.Source_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#source_clause}.
	 * @param ctx the parse tree
	 */
	void exitSource_clause(PlSqlParser.Source_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_path_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_path_clause(PlSqlParser.Join_path_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_path_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_path_clause(PlSqlParser.Join_path_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_condition}.
	 * @param ctx the parse tree
	 */
	void enterJoin_condition(PlSqlParser.Join_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_condition}.
	 * @param ctx the parse tree
	 */
	void exitJoin_condition(PlSqlParser.Join_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_condition_item}.
	 * @param ctx the parse tree
	 */
	void enterJoin_condition_item(PlSqlParser.Join_condition_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_condition_item}.
	 * @param ctx the parse tree
	 */
	void exitJoin_condition_item(PlSqlParser.Join_condition_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttributes_clause(PlSqlParser.Attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttributes_clause(PlSqlParser.Attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_attributes_clause(PlSqlParser.Ad_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_attributes_clause(PlSqlParser.Ad_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ad_level_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_level_clause(PlSqlParser.Ad_level_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ad_level_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_level_clause(PlSqlParser.Ad_level_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#key_clause}.
	 * @param ctx the parse tree
	 */
	void enterKey_clause(PlSqlParser.Key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#key_clause}.
	 * @param ctx the parse tree
	 */
	void exitKey_clause(PlSqlParser.Key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlternate_key_clause(PlSqlParser.Alternate_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlternate_key_clause(PlSqlParser.Alternate_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dim_order_clause}.
	 * @param ctx the parse tree
	 */
	void enterDim_order_clause(PlSqlParser.Dim_order_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dim_order_clause}.
	 * @param ctx the parse tree
	 */
	void exitDim_order_clause(PlSqlParser.Dim_order_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#all_clause}.
	 * @param ctx the parse tree
	 */
	void enterAll_clause(PlSqlParser.All_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#all_clause}.
	 * @param ctx the parse tree
	 */
	void exitAll_clause(PlSqlParser.All_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterCreate_audit_policy(PlSqlParser.Create_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitCreate_audit_policy(PlSqlParser.Create_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_audit_clause(PlSqlParser.Privilege_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_audit_clause(PlSqlParser.Privilege_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#action_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterAction_audit_clause(PlSqlParser.Action_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#action_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitAction_audit_clause(PlSqlParser.Action_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#system_actions}.
	 * @param ctx the parse tree
	 */
	void enterSystem_actions(PlSqlParser.System_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#system_actions}.
	 * @param ctx the parse tree
	 */
	void exitSystem_actions(PlSqlParser.System_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#standard_actions}.
	 * @param ctx the parse tree
	 */
	void enterStandard_actions(PlSqlParser.Standard_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#standard_actions}.
	 * @param ctx the parse tree
	 */
	void exitStandard_actions(PlSqlParser.Standard_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#actions_clause}.
	 * @param ctx the parse tree
	 */
	void enterActions_clause(PlSqlParser.Actions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#actions_clause}.
	 * @param ctx the parse tree
	 */
	void exitActions_clause(PlSqlParser.Actions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_action}.
	 * @param ctx the parse tree
	 */
	void enterObject_action(PlSqlParser.Object_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_action}.
	 * @param ctx the parse tree
	 */
	void exitObject_action(PlSqlParser.Object_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#system_action}.
	 * @param ctx the parse tree
	 */
	void enterSystem_action(PlSqlParser.System_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#system_action}.
	 * @param ctx the parse tree
	 */
	void exitSystem_action(PlSqlParser.System_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#component_actions}.
	 * @param ctx the parse tree
	 */
	void enterComponent_actions(PlSqlParser.Component_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#component_actions}.
	 * @param ctx the parse tree
	 */
	void exitComponent_actions(PlSqlParser.Component_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#component_action}.
	 * @param ctx the parse tree
	 */
	void enterComponent_action(PlSqlParser.Component_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#component_action}.
	 * @param ctx the parse tree
	 */
	void exitComponent_action(PlSqlParser.Component_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#role_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_audit_clause(PlSqlParser.Role_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#role_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_audit_clause(PlSqlParser.Role_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_controlfile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_controlfile(PlSqlParser.Create_controlfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_controlfile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_controlfile(PlSqlParser.Create_controlfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#controlfile_options}.
	 * @param ctx the parse tree
	 */
	void enterControlfile_options(PlSqlParser.Controlfile_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#controlfile_options}.
	 * @param ctx the parse tree
	 */
	void exitControlfile_options(PlSqlParser.Controlfile_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_clause(PlSqlParser.Logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_clause(PlSqlParser.Logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#character_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_set_clause(PlSqlParser.Character_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#character_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_set_clause(PlSqlParser.Character_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#file_specification}.
	 * @param ctx the parse tree
	 */
	void enterFile_specification(PlSqlParser.File_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#file_specification}.
	 * @param ctx the parse tree
	 */
	void exitFile_specification(PlSqlParser.File_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterCreate_diskgroup(PlSqlParser.Create_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitCreate_diskgroup(PlSqlParser.Create_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterQualified_disk_clause(PlSqlParser.Qualified_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitQualified_disk_clause(PlSqlParser.Qualified_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_edition}.
	 * @param ctx the parse tree
	 */
	void enterCreate_edition(PlSqlParser.Create_editionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_edition}.
	 * @param ctx the parse tree
	 */
	void exitCreate_edition(PlSqlParser.Create_editionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterCreate_flashback_archive(PlSqlParser.Create_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitCreate_flashback_archive(PlSqlParser.Create_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_quota(PlSqlParser.Flashback_archive_quotaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_quota(PlSqlParser.Flashback_archive_quotaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_retention(PlSqlParser.Flashback_archive_retentionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_retention(PlSqlParser.Flashback_archive_retentionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterCreate_hierarchy(PlSqlParser.Create_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitCreate_hierarchy(PlSqlParser.Create_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hier_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_using_clause(PlSqlParser.Hier_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hier_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_using_clause(PlSqlParser.Hier_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#level_hier_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevel_hier_clause(PlSqlParser.Level_hier_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#level_hier_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevel_hier_clause(PlSqlParser.Level_hier_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_attrs_clause(PlSqlParser.Hier_attrs_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_attrs_clause(PlSqlParser.Hier_attrs_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_attr_clause(PlSqlParser.Hier_attr_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_attr_clause(PlSqlParser.Hier_attr_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hier_attr_name}.
	 * @param ctx the parse tree
	 */
	void enterHier_attr_name(PlSqlParser.Hier_attr_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hier_attr_name}.
	 * @param ctx the parse tree
	 */
	void exitHier_attr_name(PlSqlParser.Hier_attr_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_index}.
	 * @param ctx the parse tree
	 */
	void enterCreate_index(PlSqlParser.Create_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_index}.
	 * @param ctx the parse tree
	 */
	void exitCreate_index(PlSqlParser.Create_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterCluster_index_clause(PlSqlParser.Cluster_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitCluster_index_clause(PlSqlParser.Cluster_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cluster_name}.
	 * @param ctx the parse tree
	 */
	void enterCluster_name(PlSqlParser.Cluster_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cluster_name}.
	 * @param ctx the parse tree
	 */
	void exitCluster_name(PlSqlParser.Cluster_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_index_columns}.
	 * @param ctx the parse tree
	 */
	void enterTable_index_columns(PlSqlParser.Table_index_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_index_columns}.
	 * @param ctx the parse tree
	 */
	void exitTable_index_columns(PlSqlParser.Table_index_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterBitmap_join_index_clause(PlSqlParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitBitmap_join_index_clause(PlSqlParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_expr}.
	 * @param ctx the parse tree
	 */
	void enterIndex_expr(PlSqlParser.Index_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_expr}.
	 * @param ctx the parse tree
	 */
	void exitIndex_expr(PlSqlParser.Index_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_properties}.
	 * @param ctx the parse tree
	 */
	void enterIndex_properties(PlSqlParser.Index_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_properties}.
	 * @param ctx the parse tree
	 */
	void exitIndex_properties(PlSqlParser.Index_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterDomain_index_clause(PlSqlParser.Domain_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitDomain_index_clause(PlSqlParser.Domain_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterLocal_domain_index_clause(PlSqlParser.Local_domain_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitLocal_domain_index_clause(PlSqlParser.Local_domain_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void enterXmlindex_clause(PlSqlParser.Xmlindex_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void exitXmlindex_clause(PlSqlParser.Xmlindex_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void enterLocal_xmlindex_clause(PlSqlParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void exitLocal_xmlindex_clause(PlSqlParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void enterGlobal_partitioned_index(PlSqlParser.Global_partitioned_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void exitGlobal_partitioned_index(PlSqlParser.Global_partitioned_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partitioning_clause(PlSqlParser.Index_partitioning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partitioning_clause(PlSqlParser.Index_partitioning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partitioning_values_list(PlSqlParser.Index_partitioning_values_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partitioning_values_list(PlSqlParser.Index_partitioning_values_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void enterLocal_partitioned_index(PlSqlParser.Local_partitioned_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void exitLocal_partitioned_index(PlSqlParser.Local_partitioned_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_range_partitioned_table(PlSqlParser.On_range_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_range_partitioned_table(PlSqlParser.On_range_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_list_partitioned_table(PlSqlParser.On_list_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_list_partitioned_table(PlSqlParser.On_list_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterPartitioned_table(PlSqlParser.Partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitPartitioned_table(PlSqlParser.Partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_hash_partitioned_table(PlSqlParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_hash_partitioned_table(PlSqlParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_hash_partitioned_clause(PlSqlParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_hash_partitioned_clause(PlSqlParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_comp_partitioned_table(PlSqlParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_comp_partitioned_table(PlSqlParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_comp_partitioned_clause(PlSqlParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_comp_partitioned_clause(PlSqlParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_subpartition_clause(PlSqlParser.Index_subpartition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_subpartition_clause(PlSqlParser.Index_subpartition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_subpartition_subclause(PlSqlParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_subpartition_subclause(PlSqlParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#odci_parameters}.
	 * @param ctx the parse tree
	 */
	void enterOdci_parameters(PlSqlParser.Odci_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#odci_parameters}.
	 * @param ctx the parse tree
	 */
	void exitOdci_parameters(PlSqlParser.Odci_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#indextype}.
	 * @param ctx the parse tree
	 */
	void enterIndextype(PlSqlParser.IndextypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#indextype}.
	 * @param ctx the parse tree
	 */
	void exitIndextype(PlSqlParser.IndextypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_index}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index(PlSqlParser.Alter_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_index}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index(PlSqlParser.Alter_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_ops_set1(PlSqlParser.Alter_index_ops_set1Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_ops_set1(PlSqlParser.Alter_index_ops_set1Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_ops_set2(PlSqlParser.Alter_index_ops_set2Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_ops_set2(PlSqlParser.Alter_index_ops_set2Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 */
	void enterVisible_or_invisible(PlSqlParser.Visible_or_invisibleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 */
	void exitVisible_or_invisible(PlSqlParser.Visible_or_invisibleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 */
	void enterMonitoring_nomonitoring(PlSqlParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 */
	void exitMonitoring_nomonitoring(PlSqlParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rebuild_clause}.
	 * @param ctx the parse tree
	 */
	void enterRebuild_clause(PlSqlParser.Rebuild_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rebuild_clause}.
	 * @param ctx the parse tree
	 */
	void exitRebuild_clause(PlSqlParser.Rebuild_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_partitioning(PlSqlParser.Alter_index_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_partitioning(PlSqlParser.Alter_index_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_default_attrs(PlSqlParser.Modify_index_default_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_default_attrs(PlSqlParser.Modify_index_default_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterAdd_hash_index_partition(PlSqlParser.Add_hash_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitAdd_hash_index_partition(PlSqlParser.Add_hash_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterCoalesce_index_partition(PlSqlParser.Coalesce_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitCoalesce_index_partition(PlSqlParser.Coalesce_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_partition(PlSqlParser.Modify_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_partition(PlSqlParser.Modify_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_partitions_ops(PlSqlParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_partitions_ops(PlSqlParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rename_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterRename_index_partition(PlSqlParser.Rename_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rename_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitRename_index_partition(PlSqlParser.Rename_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index_partition(PlSqlParser.Drop_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index_partition(PlSqlParser.Drop_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#split_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterSplit_index_partition(PlSqlParser.Split_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#split_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitSplit_index_partition(PlSqlParser.Split_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_partition_description}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partition_description(PlSqlParser.Index_partition_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_partition_description}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partition_description(PlSqlParser.Index_partition_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_subpartition(PlSqlParser.Modify_index_subpartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_subpartition(PlSqlParser.Modify_index_subpartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_name_old}.
	 * @param ctx the parse tree
	 */
	void enterPartition_name_old(PlSqlParser.Partition_name_oldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_name_old}.
	 * @param ctx the parse tree
	 */
	void exitPartition_name_old(PlSqlParser.Partition_name_oldContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_partition_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_partition_name(PlSqlParser.New_partition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_partition_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_partition_name(PlSqlParser.New_partition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_index_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_index_name(PlSqlParser.New_index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_index_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_index_name(PlSqlParser.New_index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterAlter_inmemory_join_group(PlSqlParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitAlter_inmemory_join_group(PlSqlParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_user}.
	 * @param ctx the parse tree
	 */
	void enterCreate_user(PlSqlParser.Create_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_user}.
	 * @param ctx the parse tree
	 */
	void exitCreate_user(PlSqlParser.Create_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_user}.
	 * @param ctx the parse tree
	 */
	void enterAlter_user(PlSqlParser.Alter_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_user}.
	 * @param ctx the parse tree
	 */
	void exitAlter_user(PlSqlParser.Alter_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_user}.
	 * @param ctx the parse tree
	 */
	void enterDrop_user(PlSqlParser.Drop_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_user}.
	 * @param ctx the parse tree
	 */
	void exitDrop_user(PlSqlParser.Drop_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_identified_by}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identified_by(PlSqlParser.Alter_identified_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_identified_by}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identified_by(PlSqlParser.Alter_identified_byContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identified_by}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by(PlSqlParser.Identified_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identified_by}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by(PlSqlParser.Identified_byContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identified_other_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_other_clause(PlSqlParser.Identified_other_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identified_other_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_other_clause(PlSqlParser.Identified_other_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_tablespace_clause(PlSqlParser.User_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_tablespace_clause(PlSqlParser.User_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#quota_clause}.
	 * @param ctx the parse tree
	 */
	void enterQuota_clause(PlSqlParser.Quota_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#quota_clause}.
	 * @param ctx the parse tree
	 */
	void exitQuota_clause(PlSqlParser.Quota_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#profile_clause}.
	 * @param ctx the parse tree
	 */
	void enterProfile_clause(PlSqlParser.Profile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#profile_clause}.
	 * @param ctx the parse tree
	 */
	void exitProfile_clause(PlSqlParser.Profile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#role_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_clause(PlSqlParser.Role_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#role_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_clause(PlSqlParser.Role_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_default_role_clause(PlSqlParser.User_default_role_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_default_role_clause(PlSqlParser.User_default_role_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#password_expire_clause}.
	 * @param ctx the parse tree
	 */
	void enterPassword_expire_clause(PlSqlParser.Password_expire_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#password_expire_clause}.
	 * @param ctx the parse tree
	 */
	void exitPassword_expire_clause(PlSqlParser.Password_expire_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_lock_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_lock_clause(PlSqlParser.User_lock_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_lock_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_lock_clause(PlSqlParser.User_lock_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_editions_clause(PlSqlParser.User_editions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_editions_clause(PlSqlParser.User_editions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_user_editions_clause(PlSqlParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_user_editions_clause(PlSqlParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#proxy_clause}.
	 * @param ctx the parse tree
	 */
	void enterProxy_clause(PlSqlParser.Proxy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#proxy_clause}.
	 * @param ctx the parse tree
	 */
	void exitProxy_clause(PlSqlParser.Proxy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#container_names}.
	 * @param ctx the parse tree
	 */
	void enterContainer_names(PlSqlParser.Container_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#container_names}.
	 * @param ctx the parse tree
	 */
	void exitContainer_names(PlSqlParser.Container_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_container_data}.
	 * @param ctx the parse tree
	 */
	void enterSet_container_data(PlSqlParser.Set_container_dataContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_container_data}.
	 * @param ctx the parse tree
	 */
	void exitSet_container_data(PlSqlParser.Set_container_dataContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 */
	void enterAdd_rem_container_data(PlSqlParser.Add_rem_container_dataContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 */
	void exitAdd_rem_container_data(PlSqlParser.Add_rem_container_dataContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#container_data_clause}.
	 * @param ctx the parse tree
	 */
	void enterContainer_data_clause(PlSqlParser.Container_data_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#container_data_clause}.
	 * @param ctx the parse tree
	 */
	void exitContainer_data_clause(PlSqlParser.Container_data_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#administer_key_management}.
	 * @param ctx the parse tree
	 */
	void enterAdminister_key_management(PlSqlParser.Administer_key_managementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#administer_key_management}.
	 * @param ctx the parse tree
	 */
	void exitAdminister_key_management(PlSqlParser.Administer_key_managementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterKeystore_management_clauses(PlSqlParser.Keystore_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitKeystore_management_clauses(PlSqlParser.Keystore_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_keystore}.
	 * @param ctx the parse tree
	 */
	void enterCreate_keystore(PlSqlParser.Create_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_keystore}.
	 * @param ctx the parse tree
	 */
	void exitCreate_keystore(PlSqlParser.Create_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#open_keystore}.
	 * @param ctx the parse tree
	 */
	void enterOpen_keystore(PlSqlParser.Open_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#open_keystore}.
	 * @param ctx the parse tree
	 */
	void exitOpen_keystore(PlSqlParser.Open_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#force_keystore}.
	 * @param ctx the parse tree
	 */
	void enterForce_keystore(PlSqlParser.Force_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#force_keystore}.
	 * @param ctx the parse tree
	 */
	void exitForce_keystore(PlSqlParser.Force_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#close_keystore}.
	 * @param ctx the parse tree
	 */
	void enterClose_keystore(PlSqlParser.Close_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#close_keystore}.
	 * @param ctx the parse tree
	 */
	void exitClose_keystore(PlSqlParser.Close_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#backup_keystore}.
	 * @param ctx the parse tree
	 */
	void enterBackup_keystore(PlSqlParser.Backup_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#backup_keystore}.
	 * @param ctx the parse tree
	 */
	void exitBackup_keystore(PlSqlParser.Backup_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 */
	void enterAlter_keystore_password(PlSqlParser.Alter_keystore_passwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 */
	void exitAlter_keystore_password(PlSqlParser.Alter_keystore_passwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 */
	void enterMerge_into_new_keystore(PlSqlParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 */
	void exitMerge_into_new_keystore(PlSqlParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 */
	void enterMerge_into_existing_keystore(PlSqlParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 */
	void exitMerge_into_existing_keystore(PlSqlParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#isolate_keystore}.
	 * @param ctx the parse tree
	 */
	void enterIsolate_keystore(PlSqlParser.Isolate_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#isolate_keystore}.
	 * @param ctx the parse tree
	 */
	void exitIsolate_keystore(PlSqlParser.Isolate_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unite_keystore}.
	 * @param ctx the parse tree
	 */
	void enterUnite_keystore(PlSqlParser.Unite_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unite_keystore}.
	 * @param ctx the parse tree
	 */
	void exitUnite_keystore(PlSqlParser.Unite_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#key_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterKey_management_clauses(PlSqlParser.Key_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#key_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitKey_management_clauses(PlSqlParser.Key_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_key}.
	 * @param ctx the parse tree
	 */
	void enterSet_key(PlSqlParser.Set_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_key}.
	 * @param ctx the parse tree
	 */
	void exitSet_key(PlSqlParser.Set_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_key}.
	 * @param ctx the parse tree
	 */
	void enterCreate_key(PlSqlParser.Create_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_key}.
	 * @param ctx the parse tree
	 */
	void exitCreate_key(PlSqlParser.Create_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mkid}.
	 * @param ctx the parse tree
	 */
	void enterMkid(PlSqlParser.MkidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mkid}.
	 * @param ctx the parse tree
	 */
	void exitMkid(PlSqlParser.MkidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mk}.
	 * @param ctx the parse tree
	 */
	void enterMk(PlSqlParser.MkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mk}.
	 * @param ctx the parse tree
	 */
	void exitMk(PlSqlParser.MkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#use_key}.
	 * @param ctx the parse tree
	 */
	void enterUse_key(PlSqlParser.Use_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#use_key}.
	 * @param ctx the parse tree
	 */
	void exitUse_key(PlSqlParser.Use_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_key_tag}.
	 * @param ctx the parse tree
	 */
	void enterSet_key_tag(PlSqlParser.Set_key_tagContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_key_tag}.
	 * @param ctx the parse tree
	 */
	void exitSet_key_tag(PlSqlParser.Set_key_tagContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#export_keys}.
	 * @param ctx the parse tree
	 */
	void enterExport_keys(PlSqlParser.Export_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#export_keys}.
	 * @param ctx the parse tree
	 */
	void exitExport_keys(PlSqlParser.Export_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#import_keys}.
	 * @param ctx the parse tree
	 */
	void enterImport_keys(PlSqlParser.Import_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#import_keys}.
	 * @param ctx the parse tree
	 */
	void exitImport_keys(PlSqlParser.Import_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#migrate_keys}.
	 * @param ctx the parse tree
	 */
	void enterMigrate_keys(PlSqlParser.Migrate_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#migrate_keys}.
	 * @param ctx the parse tree
	 */
	void exitMigrate_keys(PlSqlParser.Migrate_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 */
	void enterReverse_migrate_keys(PlSqlParser.Reverse_migrate_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 */
	void exitReverse_migrate_keys(PlSqlParser.Reverse_migrate_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#move_keys}.
	 * @param ctx the parse tree
	 */
	void enterMove_keys(PlSqlParser.Move_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#move_keys}.
	 * @param ctx the parse tree
	 */
	void exitMove_keys(PlSqlParser.Move_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identified_by_store}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by_store(PlSqlParser.Identified_by_storeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identified_by_store}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by_store(PlSqlParser.Identified_by_storeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_algorithm_clause(PlSqlParser.Using_algorithm_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_algorithm_clause(PlSqlParser.Using_algorithm_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_tag_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_tag_clause(PlSqlParser.Using_tag_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_tag_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_tag_clause(PlSqlParser.Using_tag_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterSecret_management_clauses(PlSqlParser.Secret_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitSecret_management_clauses(PlSqlParser.Secret_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_update_secret}.
	 * @param ctx the parse tree
	 */
	void enterAdd_update_secret(PlSqlParser.Add_update_secretContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_update_secret}.
	 * @param ctx the parse tree
	 */
	void exitAdd_update_secret(PlSqlParser.Add_update_secretContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#delete_secret}.
	 * @param ctx the parse tree
	 */
	void enterDelete_secret(PlSqlParser.Delete_secretContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#delete_secret}.
	 * @param ctx the parse tree
	 */
	void exitDelete_secret(PlSqlParser.Delete_secretContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 */
	void enterAdd_update_secret_seps(PlSqlParser.Add_update_secret_sepsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 */
	void exitAdd_update_secret_seps(PlSqlParser.Add_update_secret_sepsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 */
	void enterDelete_secret_seps(PlSqlParser.Delete_secret_sepsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 */
	void exitDelete_secret_seps(PlSqlParser.Delete_secret_sepsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 */
	void enterZero_downtime_software_patching_clauses(PlSqlParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 */
	void exitZero_downtime_software_patching_clauses(PlSqlParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#with_backup_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_backup_clause(PlSqlParser.With_backup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#with_backup_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_backup_clause(PlSqlParser.With_backup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by_password_clause(PlSqlParser.Identified_by_password_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by_password_clause(PlSqlParser.Identified_by_password_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#keystore_password}.
	 * @param ctx the parse tree
	 */
	void enterKeystore_password(PlSqlParser.Keystore_passwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#keystore_password}.
	 * @param ctx the parse tree
	 */
	void exitKeystore_password(PlSqlParser.Keystore_passwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(PlSqlParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(PlSqlParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#secret}.
	 * @param ctx the parse tree
	 */
	void enterSecret(PlSqlParser.SecretContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#secret}.
	 * @param ctx the parse tree
	 */
	void exitSecret(PlSqlParser.SecretContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#analyze}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze(PlSqlParser.AnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#analyze}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze(PlSqlParser.AnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extention_clause(PlSqlParser.Partition_extention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extention_clause(PlSqlParser.Partition_extention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#validation_clauses}.
	 * @param ctx the parse tree
	 */
	void enterValidation_clauses(PlSqlParser.Validation_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#validation_clauses}.
	 * @param ctx the parse tree
	 */
	void exitValidation_clauses(PlSqlParser.Validation_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compute_clauses}.
	 * @param ctx the parse tree
	 */
	void enterCompute_clauses(PlSqlParser.Compute_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compute_clauses}.
	 * @param ctx the parse tree
	 */
	void exitCompute_clauses(PlSqlParser.Compute_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#for_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_clause(PlSqlParser.For_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#for_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_clause(PlSqlParser.For_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#online_or_offline}.
	 * @param ctx the parse tree
	 */
	void enterOnline_or_offline(PlSqlParser.Online_or_offlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#online_or_offline}.
	 * @param ctx the parse tree
	 */
	void exitOnline_or_offline(PlSqlParser.Online_or_offlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#into_clause1}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause1(PlSqlParser.Into_clause1Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#into_clause1}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause1(PlSqlParser.Into_clause1Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_key_value}.
	 * @param ctx the parse tree
	 */
	void enterPartition_key_value(PlSqlParser.Partition_key_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_key_value}.
	 * @param ctx the parse tree
	 */
	void exitPartition_key_value(PlSqlParser.Partition_key_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_key_value(PlSqlParser.Subpartition_key_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_key_value(PlSqlParser.Subpartition_key_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#associate_statistics}.
	 * @param ctx the parse tree
	 */
	void enterAssociate_statistics(PlSqlParser.Associate_statisticsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#associate_statistics}.
	 * @param ctx the parse tree
	 */
	void exitAssociate_statistics(PlSqlParser.Associate_statisticsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_association}.
	 * @param ctx the parse tree
	 */
	void enterColumn_association(PlSqlParser.Column_associationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_association}.
	 * @param ctx the parse tree
	 */
	void exitColumn_association(PlSqlParser.Column_associationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_association}.
	 * @param ctx the parse tree
	 */
	void enterFunction_association(PlSqlParser.Function_associationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_association}.
	 * @param ctx the parse tree
	 */
	void exitFunction_association(PlSqlParser.Function_associationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#indextype_name}.
	 * @param ctx the parse tree
	 */
	void enterIndextype_name(PlSqlParser.Indextype_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#indextype_name}.
	 * @param ctx the parse tree
	 */
	void exitIndextype_name(PlSqlParser.Indextype_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_statistics_type}.
	 * @param ctx the parse tree
	 */
	void enterUsing_statistics_type(PlSqlParser.Using_statistics_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_statistics_type}.
	 * @param ctx the parse tree
	 */
	void exitUsing_statistics_type(PlSqlParser.Using_statistics_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#statistics_type_name}.
	 * @param ctx the parse tree
	 */
	void enterStatistics_type_name(PlSqlParser.Statistics_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#statistics_type_name}.
	 * @param ctx the parse tree
	 */
	void exitStatistics_type_name(PlSqlParser.Statistics_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_cost_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_cost_clause(PlSqlParser.Default_cost_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_cost_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_cost_clause(PlSqlParser.Default_cost_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cpu_cost}.
	 * @param ctx the parse tree
	 */
	void enterCpu_cost(PlSqlParser.Cpu_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cpu_cost}.
	 * @param ctx the parse tree
	 */
	void exitCpu_cost(PlSqlParser.Cpu_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#io_cost}.
	 * @param ctx the parse tree
	 */
	void enterIo_cost(PlSqlParser.Io_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#io_cost}.
	 * @param ctx the parse tree
	 */
	void exitIo_cost(PlSqlParser.Io_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#network_cost}.
	 * @param ctx the parse tree
	 */
	void enterNetwork_cost(PlSqlParser.Network_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#network_cost}.
	 * @param ctx the parse tree
	 */
	void exitNetwork_cost(PlSqlParser.Network_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_selectivity_clause(PlSqlParser.Default_selectivity_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_selectivity_clause(PlSqlParser.Default_selectivity_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_selectivity}.
	 * @param ctx the parse tree
	 */
	void enterDefault_selectivity(PlSqlParser.Default_selectivityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_selectivity}.
	 * @param ctx the parse tree
	 */
	void exitDefault_selectivity(PlSqlParser.Default_selectivityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#storage_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterStorage_table_clause(PlSqlParser.Storage_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#storage_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitStorage_table_clause(PlSqlParser.Storage_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unified_auditing}.
	 * @param ctx the parse tree
	 */
	void enterUnified_auditing(PlSqlParser.Unified_auditingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unified_auditing}.
	 * @param ctx the parse tree
	 */
	void exitUnified_auditing(PlSqlParser.Unified_auditingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#policy_name}.
	 * @param ctx the parse tree
	 */
	void enterPolicy_name(PlSqlParser.Policy_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#policy_name}.
	 * @param ctx the parse tree
	 */
	void exitPolicy_name(PlSqlParser.Policy_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_traditional}.
	 * @param ctx the parse tree
	 */
	void enterAudit_traditional(PlSqlParser.Audit_traditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_traditional}.
	 * @param ctx the parse tree
	 */
	void exitAudit_traditional(PlSqlParser.Audit_traditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_direct_path}.
	 * @param ctx the parse tree
	 */
	void enterAudit_direct_path(PlSqlParser.Audit_direct_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_direct_path}.
	 * @param ctx the parse tree
	 */
	void exitAudit_direct_path(PlSqlParser.Audit_direct_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_container_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_container_clause(PlSqlParser.Audit_container_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_container_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_container_clause(PlSqlParser.Audit_container_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_operation_clause(PlSqlParser.Audit_operation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_operation_clause(PlSqlParser.Audit_operation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterAuditing_by_clause(PlSqlParser.Auditing_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitAuditing_by_clause(PlSqlParser.Auditing_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_user}.
	 * @param ctx the parse tree
	 */
	void enterAudit_user(PlSqlParser.Audit_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_user}.
	 * @param ctx the parse tree
	 */
	void exitAudit_user(PlSqlParser.Audit_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_schema_object_clause(PlSqlParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_schema_object_clause(PlSqlParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_operation}.
	 * @param ctx the parse tree
	 */
	void enterSql_operation(PlSqlParser.Sql_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_operation}.
	 * @param ctx the parse tree
	 */
	void exitSql_operation(PlSqlParser.Sql_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 */
	void enterAuditing_on_clause(PlSqlParser.Auditing_on_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 */
	void exitAuditing_on_clause(PlSqlParser.Auditing_on_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_name}.
	 * @param ctx the parse tree
	 */
	void enterModel_name(PlSqlParser.Model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_name}.
	 * @param ctx the parse tree
	 */
	void exitModel_name(PlSqlParser.Model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_name(PlSqlParser.Object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_name(PlSqlParser.Object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#profile_name}.
	 * @param ctx the parse tree
	 */
	void enterProfile_name(PlSqlParser.Profile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#profile_name}.
	 * @param ctx the parse tree
	 */
	void exitProfile_name(PlSqlParser.Profile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 */
	void enterSql_statement_shortcut(PlSqlParser.Sql_statement_shortcutContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 */
	void exitSql_statement_shortcut(PlSqlParser.Sql_statement_shortcutContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_index}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index(PlSqlParser.Drop_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_index}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index(PlSqlParser.Drop_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 */
	void enterDisassociate_statistics(PlSqlParser.Disassociate_statisticsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 */
	void exitDisassociate_statistics(PlSqlParser.Disassociate_statisticsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_indextype}.
	 * @param ctx the parse tree
	 */
	void enterDrop_indextype(PlSqlParser.Drop_indextypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_indextype}.
	 * @param ctx the parse tree
	 */
	void exitDrop_indextype(PlSqlParser.Drop_indextypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterDrop_inmemory_join_group(PlSqlParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitDrop_inmemory_join_group(PlSqlParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_table}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_table(PlSqlParser.Flashback_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_table}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_table(PlSqlParser.Flashback_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#restore_point}.
	 * @param ctx the parse tree
	 */
	void enterRestore_point(PlSqlParser.Restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#restore_point}.
	 * @param ctx the parse tree
	 */
	void exitRestore_point(PlSqlParser.Restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#purge_statement}.
	 * @param ctx the parse tree
	 */
	void enterPurge_statement(PlSqlParser.Purge_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#purge_statement}.
	 * @param ctx the parse tree
	 */
	void exitPurge_statement(PlSqlParser.Purge_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#noaudit_statement}.
	 * @param ctx the parse tree
	 */
	void enterNoaudit_statement(PlSqlParser.Noaudit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#noaudit_statement}.
	 * @param ctx the parse tree
	 */
	void exitNoaudit_statement(PlSqlParser.Noaudit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rename_object}.
	 * @param ctx the parse tree
	 */
	void enterRename_object(PlSqlParser.Rename_objectContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rename_object}.
	 * @param ctx the parse tree
	 */
	void exitRename_object(PlSqlParser.Rename_objectContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#grant_statement}.
	 * @param ctx the parse tree
	 */
	void enterGrant_statement(PlSqlParser.Grant_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#grant_statement}.
	 * @param ctx the parse tree
	 */
	void exitGrant_statement(PlSqlParser.Grant_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#container_clause}.
	 * @param ctx the parse tree
	 */
	void enterContainer_clause(PlSqlParser.Container_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#container_clause}.
	 * @param ctx the parse tree
	 */
	void exitContainer_clause(PlSqlParser.Container_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#revoke_statement}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_statement(PlSqlParser.Revoke_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#revoke_statement}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_statement(PlSqlParser.Revoke_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_system_privilege(PlSqlParser.Revoke_system_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_system_privilege(PlSqlParser.Revoke_system_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#revokee_clause}.
	 * @param ctx the parse tree
	 */
	void enterRevokee_clause(PlSqlParser.Revokee_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#revokee_clause}.
	 * @param ctx the parse tree
	 */
	void exitRevokee_clause(PlSqlParser.Revokee_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_object_privileges(PlSqlParser.Revoke_object_privilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_object_privileges(PlSqlParser.Revoke_object_privilegesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_object_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_object_clause(PlSqlParser.On_object_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_object_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_object_clause(PlSqlParser.On_object_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_roles_from_programs(PlSqlParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_roles_from_programs(PlSqlParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#program_unit}.
	 * @param ctx the parse tree
	 */
	void enterProgram_unit(PlSqlParser.Program_unitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#program_unit}.
	 * @param ctx the parse tree
	 */
	void exitProgram_unit(PlSqlParser.Program_unitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_dimension}.
	 * @param ctx the parse tree
	 */
	void enterCreate_dimension(PlSqlParser.Create_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_dimension}.
	 * @param ctx the parse tree
	 */
	void exitCreate_dimension(PlSqlParser.Create_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_directory}.
	 * @param ctx the parse tree
	 */
	void enterCreate_directory(PlSqlParser.Create_directoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_directory}.
	 * @param ctx the parse tree
	 */
	void exitCreate_directory(PlSqlParser.Create_directoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#directory_name}.
	 * @param ctx the parse tree
	 */
	void enterDirectory_name(PlSqlParser.Directory_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#directory_name}.
	 * @param ctx the parse tree
	 */
	void exitDirectory_name(PlSqlParser.Directory_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#directory_path}.
	 * @param ctx the parse tree
	 */
	void enterDirectory_path(PlSqlParser.Directory_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#directory_path}.
	 * @param ctx the parse tree
	 */
	void exitDirectory_path(PlSqlParser.Directory_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterCreate_inmemory_join_group(PlSqlParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitCreate_inmemory_join_group(PlSqlParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterDrop_hierarchy(PlSqlParser.Drop_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitDrop_hierarchy(PlSqlParser.Drop_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_library}.
	 * @param ctx the parse tree
	 */
	void enterAlter_library(PlSqlParser.Alter_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_library}.
	 * @param ctx the parse tree
	 */
	void exitAlter_library(PlSqlParser.Alter_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_java}.
	 * @param ctx the parse tree
	 */
	void enterDrop_java(PlSqlParser.Drop_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_java}.
	 * @param ctx the parse tree
	 */
	void exitDrop_java(PlSqlParser.Drop_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_library}.
	 * @param ctx the parse tree
	 */
	void enterDrop_library(PlSqlParser.Drop_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_library}.
	 * @param ctx the parse tree
	 */
	void exitDrop_library(PlSqlParser.Drop_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_java}.
	 * @param ctx the parse tree
	 */
	void enterCreate_java(PlSqlParser.Create_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_java}.
	 * @param ctx the parse tree
	 */
	void exitCreate_java(PlSqlParser.Create_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_library}.
	 * @param ctx the parse tree
	 */
	void enterCreate_library(PlSqlParser.Create_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_library}.
	 * @param ctx the parse tree
	 */
	void exitCreate_library(PlSqlParser.Create_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#plsql_library_source}.
	 * @param ctx the parse tree
	 */
	void enterPlsql_library_source(PlSqlParser.Plsql_library_sourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#plsql_library_source}.
	 * @param ctx the parse tree
	 */
	void exitPlsql_library_source(PlSqlParser.Plsql_library_sourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#credential_name}.
	 * @param ctx the parse tree
	 */
	void enterCredential_name(PlSqlParser.Credential_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#credential_name}.
	 * @param ctx the parse tree
	 */
	void exitCredential_name(PlSqlParser.Credential_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#library_editionable}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_editionable(PlSqlParser.Library_editionableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#library_editionable}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_editionable(PlSqlParser.Library_editionableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#library_debug}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_debug(PlSqlParser.Library_debugContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#library_debug}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_debug(PlSqlParser.Library_debugContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void enterCompiler_parameters_clause(PlSqlParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void exitCompiler_parameters_clause(PlSqlParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parameter_value}.
	 * @param ctx the parse tree
	 */
	void enterParameter_value(PlSqlParser.Parameter_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parameter_value}.
	 * @param ctx the parse tree
	 */
	void exitParameter_value(PlSqlParser.Parameter_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#library_name}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_name(PlSqlParser.Library_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#library_name}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_name(PlSqlParser.Library_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_dimension}.
	 * @param ctx the parse tree
	 */
	void enterAlter_dimension(PlSqlParser.Alter_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_dimension}.
	 * @param ctx the parse tree
	 */
	void exitAlter_dimension(PlSqlParser.Alter_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#level_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevel_clause(PlSqlParser.Level_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#level_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevel_clause(PlSqlParser.Level_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchy_clause(PlSqlParser.Hierarchy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchy_clause(PlSqlParser.Hierarchy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 */
	void enterDimension_join_clause(PlSqlParser.Dimension_join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 */
	void exitDimension_join_clause(PlSqlParser.Dimension_join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#attribute_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_clause(PlSqlParser.Attribute_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#attribute_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_clause(PlSqlParser.Attribute_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 */
	void enterExtended_attribute_clause(PlSqlParser.Extended_attribute_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 */
	void exitExtended_attribute_clause(PlSqlParser.Extended_attribute_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterColumn_one_or_more_sub_clause(PlSqlParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitColumn_one_or_more_sub_clause(PlSqlParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_view(PlSqlParser.Alter_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_view(PlSqlParser.Alter_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 */
	void enterAlter_view_editionable(PlSqlParser.Alter_view_editionableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 */
	void exitAlter_view_editionable(PlSqlParser.Alter_view_editionableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view(PlSqlParser.Create_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view(PlSqlParser.Create_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#editioning_clause}.
	 * @param ctx the parse tree
	 */
	void enterEditioning_clause(PlSqlParser.Editioning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#editioning_clause}.
	 * @param ctx the parse tree
	 */
	void exitEditioning_clause(PlSqlParser.Editioning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#view_options}.
	 * @param ctx the parse tree
	 */
	void enterView_options(PlSqlParser.View_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#view_options}.
	 * @param ctx the parse tree
	 */
	void exitView_options(PlSqlParser.View_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 */
	void enterView_alias_constraint(PlSqlParser.View_alias_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 */
	void exitView_alias_constraint(PlSqlParser.View_alias_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_view_clause}.
	 * @param ctx the parse tree
	 */
	void enterObject_view_clause(PlSqlParser.Object_view_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_view_clause}.
	 * @param ctx the parse tree
	 */
	void exitObject_view_clause(PlSqlParser.Object_view_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inline_constraint}.
	 * @param ctx the parse tree
	 */
	void enterInline_constraint(PlSqlParser.Inline_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inline_constraint}.
	 * @param ctx the parse tree
	 */
	void exitInline_constraint(PlSqlParser.Inline_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void enterUnique_(PlSqlParser.Unique_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unique_}.
	 * @param ctx the parse tree
	 */
	void exitUnique_(PlSqlParser.Unique_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#primary_key_}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key_(PlSqlParser.Primary_key_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#primary_key_}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key_(PlSqlParser.Primary_key_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#null_}.
	 * @param ctx the parse tree
	 */
	void enterNull_(PlSqlParser.Null_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#null_}.
	 * @param ctx the parse tree
	 */
	void exitNull_(PlSqlParser.Null_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#not_null_}.
	 * @param ctx the parse tree
	 */
	void enterNot_null_(PlSqlParser.Not_null_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#not_null_}.
	 * @param ctx the parse tree
	 */
	void exitNot_null_(PlSqlParser.Not_null_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterInline_ref_constraint(PlSqlParser.Inline_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitInline_ref_constraint(PlSqlParser.Inline_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_ref_constraint(PlSqlParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_ref_constraint(PlSqlParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_constraint(PlSqlParser.Out_of_line_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_constraint(PlSqlParser.Out_of_line_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constraint_column_list}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_column_list(PlSqlParser.Constraint_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constraint_column_list}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_column_list(PlSqlParser.Constraint_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constraint_state}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_state(PlSqlParser.Constraint_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constraint_state}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_state(PlSqlParser.Constraint_stateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_view_clause(PlSqlParser.Xmltype_view_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_view_clause(PlSqlParser.Xmltype_view_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 */
	void enterXml_schema_spec(PlSqlParser.Xml_schema_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 */
	void exitXml_schema_spec(PlSqlParser.Xml_schema_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_schema_url}.
	 * @param ctx the parse tree
	 */
	void enterXml_schema_url(PlSqlParser.Xml_schema_urlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_schema_url}.
	 * @param ctx the parse tree
	 */
	void exitXml_schema_url(PlSqlParser.Xml_schema_urlContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(PlSqlParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(PlSqlParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace(PlSqlParser.Alter_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace(PlSqlParser.Alter_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_tempfile_clauses(PlSqlParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_tempfile_clauses(PlSqlParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_logging_clauses(PlSqlParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_logging_clauses(PlSqlParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_group_clause(PlSqlParser.Tablespace_group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_group_clause(PlSqlParser.Tablespace_group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_group_name(PlSqlParser.Tablespace_group_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_group_name(PlSqlParser.Tablespace_group_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_state_clauses(PlSqlParser.Tablespace_state_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_state_clauses(PlSqlParser.Tablespace_state_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_mode_clause(PlSqlParser.Flashback_mode_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_mode_clause(PlSqlParser.Flashback_mode_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_tablespace_name(PlSqlParser.New_tablespace_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_tablespace_name(PlSqlParser.New_tablespace_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterCreate_tablespace(PlSqlParser.Create_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitCreate_tablespace(PlSqlParser.Create_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterPermanent_tablespace_clause(PlSqlParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitPermanent_tablespace_clause(PlSqlParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_encryption_spec(PlSqlParser.Tablespace_encryption_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_encryption_spec(PlSqlParser.Tablespace_encryption_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logging_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogging_clause(PlSqlParser.Logging_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logging_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogging_clause(PlSqlParser.Logging_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#extent_management_clause}.
	 * @param ctx the parse tree
	 */
	void enterExtent_management_clause(PlSqlParser.Extent_management_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#extent_management_clause}.
	 * @param ctx the parse tree
	 */
	void exitExtent_management_clause(PlSqlParser.Extent_management_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#segment_management_clause}.
	 * @param ctx the parse tree
	 */
	void enterSegment_management_clause(PlSqlParser.Segment_management_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#segment_management_clause}.
	 * @param ctx the parse tree
	 */
	void exitSegment_management_clause(PlSqlParser.Segment_management_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_tablespace_clause(PlSqlParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_tablespace_clause(PlSqlParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndo_tablespace_clause(PlSqlParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndo_tablespace_clause(PlSqlParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_retention_clause(PlSqlParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_retention_clause(PlSqlParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterCreate_tablespace_set(PlSqlParser.Create_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitCreate_tablespace_set(PlSqlParser.Create_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void enterPermanent_tablespace_attrs(PlSqlParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void exitPermanent_tablespace_attrs(PlSqlParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_encryption_clause(PlSqlParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_encryption_clause(PlSqlParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 */
	void enterDefault_tablespace_params(PlSqlParser.Default_tablespace_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 */
	void exitDefault_tablespace_params(PlSqlParser.Default_tablespace_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_table_compression}.
	 * @param ctx the parse tree
	 */
	void enterDefault_table_compression(PlSqlParser.Default_table_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_table_compression}.
	 * @param ctx the parse tree
	 */
	void exitDefault_table_compression(PlSqlParser.Default_table_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#low_high}.
	 * @param ctx the parse tree
	 */
	void enterLow_high(PlSqlParser.Low_highContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#low_high}.
	 * @param ctx the parse tree
	 */
	void exitLow_high(PlSqlParser.Low_highContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_index_compression}.
	 * @param ctx the parse tree
	 */
	void enterDefault_index_compression(PlSqlParser.Default_index_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_index_compression}.
	 * @param ctx the parse tree
	 */
	void exitDefault_index_compression(PlSqlParser.Default_index_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmmemory_clause(PlSqlParser.Inmmemory_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmmemory_clause(PlSqlParser.Inmmemory_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datafile_specification}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_specification(PlSqlParser.Datafile_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datafile_specification}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_specification(PlSqlParser.Datafile_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tempfile_specification}.
	 * @param ctx the parse tree
	 */
	void enterTempfile_specification(PlSqlParser.Tempfile_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tempfile_specification}.
	 * @param ctx the parse tree
	 */
	void exitTempfile_specification(PlSqlParser.Tempfile_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_tempfile_spec(PlSqlParser.Datafile_tempfile_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_tempfile_spec(PlSqlParser.Datafile_tempfile_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 */
	void enterRedo_log_file_spec(PlSqlParser.Redo_log_file_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 */
	void exitRedo_log_file_spec(PlSqlParser.Redo_log_file_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#autoextend_clause}.
	 * @param ctx the parse tree
	 */
	void enterAutoextend_clause(PlSqlParser.Autoextend_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#autoextend_clause}.
	 * @param ctx the parse tree
	 */
	void exitAutoextend_clause(PlSqlParser.Autoextend_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#maxsize_clause}.
	 * @param ctx the parse tree
	 */
	void enterMaxsize_clause(PlSqlParser.Maxsize_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#maxsize_clause}.
	 * @param ctx the parse tree
	 */
	void exitMaxsize_clause(PlSqlParser.Maxsize_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#build_clause}.
	 * @param ctx the parse tree
	 */
	void enterBuild_clause(PlSqlParser.Build_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#build_clause}.
	 * @param ctx the parse tree
	 */
	void exitBuild_clause(PlSqlParser.Build_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parallel_clause}.
	 * @param ctx the parse tree
	 */
	void enterParallel_clause(PlSqlParser.Parallel_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parallel_clause}.
	 * @param ctx the parse tree
	 */
	void exitParallel_clause(PlSqlParser.Parallel_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view(PlSqlParser.Alter_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view(PlSqlParser.Alter_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mv_option1(PlSqlParser.Alter_mv_option1Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mv_option1(PlSqlParser.Alter_mv_option1Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mv_refresh(PlSqlParser.Alter_mv_refreshContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mv_refresh(PlSqlParser.Alter_mv_refreshContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterRollback_segment(PlSqlParser.Rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitRollback_segment(PlSqlParser.Rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_mv_column_clause(PlSqlParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_mv_column_clause(PlSqlParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view_log(PlSqlParser.Alter_materialized_view_logContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view_log(PlSqlParser.Alter_materialized_view_logContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_mv_log_column_clause(PlSqlParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_mv_log_column_clause(PlSqlParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_mv_log_clause(PlSqlParser.Move_mv_log_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_mv_log_clause(PlSqlParser.Move_mv_log_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 */
	void enterMv_log_augmentation(PlSqlParser.Mv_log_augmentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 */
	void exitMv_log_augmentation(PlSqlParser.Mv_log_augmentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datetime_expr}.
	 * @param ctx the parse tree
	 */
	void enterDatetime_expr(PlSqlParser.Datetime_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datetime_expr}.
	 * @param ctx the parse tree
	 */
	void exitDatetime_expr(PlSqlParser.Datetime_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#interval_expr}.
	 * @param ctx the parse tree
	 */
	void enterInterval_expr(PlSqlParser.Interval_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#interval_expr}.
	 * @param ctx the parse tree
	 */
	void exitInterval_expr(PlSqlParser.Interval_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 */
	void enterSynchronous_or_asynchronous(PlSqlParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 */
	void exitSynchronous_or_asynchronous(PlSqlParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#including_or_excluding}.
	 * @param ctx the parse tree
	 */
	void enterIncluding_or_excluding(PlSqlParser.Including_or_excludingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#including_or_excluding}.
	 * @param ctx the parse tree
	 */
	void exitIncluding_or_excluding(PlSqlParser.Including_or_excludingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_view_log(PlSqlParser.Create_materialized_view_logContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_view_log(PlSqlParser.Create_materialized_view_logContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterNew_values_clause(PlSqlParser.New_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitNew_values_clause(PlSqlParser.New_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 */
	void enterMv_log_purge_clause(PlSqlParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 */
	void exitMv_log_purge_clause(PlSqlParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_zonemap(PlSqlParser.Create_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_zonemap(PlSqlParser.Create_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_zonemap(PlSqlParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_zonemap(PlSqlParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterDrop_materialized_zonemap(PlSqlParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitDrop_materialized_zonemap(PlSqlParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_refresh_clause(PlSqlParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_refresh_clause(PlSqlParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_attributes(PlSqlParser.Zonemap_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_attributes(PlSqlParser.Zonemap_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#zonemap_name}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_name(PlSqlParser.Zonemap_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#zonemap_name}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_name(PlSqlParser.Zonemap_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#operator_name}.
	 * @param ctx the parse tree
	 */
	void enterOperator_name(PlSqlParser.Operator_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#operator_name}.
	 * @param ctx the parse tree
	 */
	void exitOperator_name(PlSqlParser.Operator_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#operator_function_name}.
	 * @param ctx the parse tree
	 */
	void enterOperator_function_name(PlSqlParser.Operator_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#operator_function_name}.
	 * @param ctx the parse tree
	 */
	void exitOperator_function_name(PlSqlParser.Operator_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 */
	void enterCreate_zonemap_on_table(PlSqlParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 */
	void exitCreate_zonemap_on_table(PlSqlParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 */
	void enterCreate_zonemap_as_subquery(PlSqlParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 */
	void exitCreate_zonemap_as_subquery(PlSqlParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_operator}.
	 * @param ctx the parse tree
	 */
	void enterAlter_operator(PlSqlParser.Alter_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_operator}.
	 * @param ctx the parse tree
	 */
	void exitAlter_operator(PlSqlParser.Alter_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_operator}.
	 * @param ctx the parse tree
	 */
	void enterDrop_operator(PlSqlParser.Drop_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_operator}.
	 * @param ctx the parse tree
	 */
	void exitDrop_operator(PlSqlParser.Drop_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_operator}.
	 * @param ctx the parse tree
	 */
	void enterCreate_operator(PlSqlParser.Create_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_operator}.
	 * @param ctx the parse tree
	 */
	void exitCreate_operator(PlSqlParser.Create_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterBinding_clause(PlSqlParser.Binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitBinding_clause(PlSqlParser.Binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_binding_clause(PlSqlParser.Add_binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_binding_clause(PlSqlParser.Add_binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#implementation_clause}.
	 * @param ctx the parse tree
	 */
	void enterImplementation_clause(PlSqlParser.Implementation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#implementation_clause}.
	 * @param ctx the parse tree
	 */
	void exitImplementation_clause(PlSqlParser.Implementation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#primary_operator_list}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_operator_list(PlSqlParser.Primary_operator_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#primary_operator_list}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_operator_list(PlSqlParser.Primary_operator_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#primary_operator_item}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_operator_item(PlSqlParser.Primary_operator_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#primary_operator_item}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_operator_item(PlSqlParser.Primary_operator_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#operator_context_clause}.
	 * @param ctx the parse tree
	 */
	void enterOperator_context_clause(PlSqlParser.Operator_context_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#operator_context_clause}.
	 * @param ctx the parse tree
	 */
	void exitOperator_context_clause(PlSqlParser.Operator_context_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_function_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_function_clause(PlSqlParser.Using_function_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_function_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_function_clause(PlSqlParser.Using_function_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_binding_clause(PlSqlParser.Drop_binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_binding_clause(PlSqlParser.Drop_binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_view(PlSqlParser.Create_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_view(PlSqlParser.Create_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterScoped_table_ref_constraint(PlSqlParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitScoped_table_ref_constraint(PlSqlParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mv_column_alias}.
	 * @param ctx the parse tree
	 */
	void enterMv_column_alias(PlSqlParser.Mv_column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mv_column_alias}.
	 * @param ctx the parse tree
	 */
	void exitMv_column_alias(PlSqlParser.Mv_column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void enterCreate_mv_refresh(PlSqlParser.Create_mv_refreshContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void exitCreate_mv_refresh(PlSqlParser.Create_mv_refreshContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_materialized_view(PlSqlParser.Drop_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_materialized_view(PlSqlParser.Drop_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_context}.
	 * @param ctx the parse tree
	 */
	void enterCreate_context(PlSqlParser.Create_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_context}.
	 * @param ctx the parse tree
	 */
	void exitCreate_context(PlSqlParser.Create_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#oracle_namespace}.
	 * @param ctx the parse tree
	 */
	void enterOracle_namespace(PlSqlParser.Oracle_namespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#oracle_namespace}.
	 * @param ctx the parse tree
	 */
	void exitOracle_namespace(PlSqlParser.Oracle_namespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_cluster}.
	 * @param ctx the parse tree
	 */
	void enterCreate_cluster(PlSqlParser.Create_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_cluster}.
	 * @param ctx the parse tree
	 */
	void exitCreate_cluster(PlSqlParser.Create_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_profile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_profile(PlSqlParser.Create_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_profile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_profile(PlSqlParser.Create_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#resource_parameters}.
	 * @param ctx the parse tree
	 */
	void enterResource_parameters(PlSqlParser.Resource_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#resource_parameters}.
	 * @param ctx the parse tree
	 */
	void exitResource_parameters(PlSqlParser.Resource_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#password_parameters}.
	 * @param ctx the parse tree
	 */
	void enterPassword_parameters(PlSqlParser.Password_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#password_parameters}.
	 * @param ctx the parse tree
	 */
	void exitPassword_parameters(PlSqlParser.Password_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_lockdown_profile(PlSqlParser.Create_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_lockdown_profile(PlSqlParser.Create_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#static_base_profile}.
	 * @param ctx the parse tree
	 */
	void enterStatic_base_profile(PlSqlParser.Static_base_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#static_base_profile}.
	 * @param ctx the parse tree
	 */
	void exitStatic_base_profile(PlSqlParser.Static_base_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 */
	void enterDynamic_base_profile(PlSqlParser.Dynamic_base_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 */
	void exitDynamic_base_profile(PlSqlParser.Dynamic_base_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_outline}.
	 * @param ctx the parse tree
	 */
	void enterCreate_outline(PlSqlParser.Create_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_outline}.
	 * @param ctx the parse tree
	 */
	void exitCreate_outline(PlSqlParser.Create_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_restore_point}.
	 * @param ctx the parse tree
	 */
	void enterCreate_restore_point(PlSqlParser.Create_restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_restore_point}.
	 * @param ctx the parse tree
	 */
	void exitCreate_restore_point(PlSqlParser.Create_restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_role}.
	 * @param ctx the parse tree
	 */
	void enterCreate_role(PlSqlParser.Create_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_role}.
	 * @param ctx the parse tree
	 */
	void exitCreate_role(PlSqlParser.Create_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_table}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table(PlSqlParser.Create_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_table}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table(PlSqlParser.Create_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_(PlSqlParser.Temporary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_(PlSqlParser.Temporary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltype_table}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_table(PlSqlParser.Xmltype_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltype_table}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_table(PlSqlParser.Xmltype_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_virtual_columns(PlSqlParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_virtual_columns(PlSqlParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_column_properties(PlSqlParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_column_properties(PlSqlParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltype_storage}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_storage(PlSqlParser.Xmltype_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltype_storage}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_storage(PlSqlParser.Xmltype_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 */
	void enterXmlschema_spec(PlSqlParser.Xmlschema_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 */
	void exitXmlschema_spec(PlSqlParser.Xmlschema_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_table}.
	 * @param ctx the parse tree
	 */
	void enterObject_table(PlSqlParser.Object_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_table}.
	 * @param ctx the parse tree
	 */
	void exitObject_table(PlSqlParser.Object_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_type}.
	 * @param ctx the parse tree
	 */
	void enterObject_type(PlSqlParser.Object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_type}.
	 * @param ctx the parse tree
	 */
	void exitObject_type(PlSqlParser.Object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#oid_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterOid_index_clause(PlSqlParser.Oid_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#oid_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitOid_index_clause(PlSqlParser.Oid_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#oid_clause}.
	 * @param ctx the parse tree
	 */
	void enterOid_clause(PlSqlParser.Oid_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#oid_clause}.
	 * @param ctx the parse tree
	 */
	void exitOid_clause(PlSqlParser.Oid_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_properties}.
	 * @param ctx the parse tree
	 */
	void enterObject_properties(PlSqlParser.Object_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_properties}.
	 * @param ctx the parse tree
	 */
	void exitObject_properties(PlSqlParser.Object_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_table_substitution}.
	 * @param ctx the parse tree
	 */
	void enterObject_table_substitution(PlSqlParser.Object_table_substitutionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_table_substitution}.
	 * @param ctx the parse tree
	 */
	void exitObject_table_substitution(PlSqlParser.Object_table_substitutionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#relational_table}.
	 * @param ctx the parse tree
	 */
	void enterRelational_table(PlSqlParser.Relational_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#relational_table}.
	 * @param ctx the parse tree
	 */
	void exitRelational_table(PlSqlParser.Relational_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_clauses(PlSqlParser.Immutable_table_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_clauses(PlSqlParser.Immutable_table_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_no_drop_clause(PlSqlParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_no_drop_clause(PlSqlParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_no_delete_clause(PlSqlParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_no_delete_clause(PlSqlParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_table_clauses(PlSqlParser.Blockchain_table_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_table_clauses(PlSqlParser.Blockchain_table_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_drop_table_clause(PlSqlParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_drop_table_clause(PlSqlParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_row_retention_clause(PlSqlParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_row_retention_clause(PlSqlParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_hash_and_data_format_clause(PlSqlParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_hash_and_data_format_clause(PlSqlParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void enterCollation_name(PlSqlParser.Collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void exitCollation_name(PlSqlParser.Collation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_properties}.
	 * @param ctx the parse tree
	 */
	void enterTable_properties(PlSqlParser.Table_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_properties}.
	 * @param ctx the parse tree
	 */
	void exitTable_properties(PlSqlParser.Table_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#read_only_clause}.
	 * @param ctx the parse tree
	 */
	void enterRead_only_clause(PlSqlParser.Read_only_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#read_only_clause}.
	 * @param ctx the parse tree
	 */
	void exitRead_only_clause(PlSqlParser.Read_only_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#indexing_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndexing_clause(PlSqlParser.Indexing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#indexing_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndexing_clause(PlSqlParser.Indexing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_clustering_clause(PlSqlParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_clustering_clause(PlSqlParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#clustering_join}.
	 * @param ctx the parse tree
	 */
	void enterClustering_join(PlSqlParser.Clustering_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#clustering_join}.
	 * @param ctx the parse tree
	 */
	void exitClustering_join(PlSqlParser.Clustering_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#clustering_join_item}.
	 * @param ctx the parse tree
	 */
	void enterClustering_join_item(PlSqlParser.Clustering_join_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#clustering_join_item}.
	 * @param ctx the parse tree
	 */
	void exitClustering_join_item(PlSqlParser.Clustering_join_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#equijoin_condition}.
	 * @param ctx the parse tree
	 */
	void enterEquijoin_condition(PlSqlParser.Equijoin_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#equijoin_condition}.
	 * @param ctx the parse tree
	 */
	void exitEquijoin_condition(PlSqlParser.Equijoin_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cluster_clause}.
	 * @param ctx the parse tree
	 */
	void enterCluster_clause(PlSqlParser.Cluster_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cluster_clause}.
	 * @param ctx the parse tree
	 */
	void exitCluster_clause(PlSqlParser.Cluster_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#clustering_columns}.
	 * @param ctx the parse tree
	 */
	void enterClustering_columns(PlSqlParser.Clustering_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#clustering_columns}.
	 * @param ctx the parse tree
	 */
	void exitClustering_columns(PlSqlParser.Clustering_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#clustering_column_group}.
	 * @param ctx the parse tree
	 */
	void enterClustering_column_group(PlSqlParser.Clustering_column_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#clustering_column_group}.
	 * @param ctx the parse tree
	 */
	void exitClustering_column_group(PlSqlParser.Clustering_column_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#yes_no}.
	 * @param ctx the parse tree
	 */
	void enterYes_no(PlSqlParser.Yes_noContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#yes_no}.
	 * @param ctx the parse tree
	 */
	void exitYes_no(PlSqlParser.Yes_noContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#zonemap_clause}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_clause(PlSqlParser.Zonemap_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#zonemap_clause}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_clause(PlSqlParser.Zonemap_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogical_replication_clause(PlSqlParser.Logical_replication_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogical_replication_clause(PlSqlParser.Logical_replication_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(PlSqlParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(PlSqlParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#relational_property}.
	 * @param ctx the parse tree
	 */
	void enterRelational_property(PlSqlParser.Relational_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#relational_property}.
	 * @param ctx the parse tree
	 */
	void exitRelational_property(PlSqlParser.Relational_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTable_partitioning_clauses(PlSqlParser.Table_partitioning_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTable_partitioning_clauses(PlSqlParser.Table_partitioning_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#range_partitions}.
	 * @param ctx the parse tree
	 */
	void enterRange_partitions(PlSqlParser.Range_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#range_partitions}.
	 * @param ctx the parse tree
	 */
	void exitRange_partitions(PlSqlParser.Range_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#list_partitions}.
	 * @param ctx the parse tree
	 */
	void enterList_partitions(PlSqlParser.List_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#list_partitions}.
	 * @param ctx the parse tree
	 */
	void exitList_partitions(PlSqlParser.List_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterHash_partitions(PlSqlParser.Hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitHash_partitions(PlSqlParser.Hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterIndividual_hash_partitions(PlSqlParser.Individual_hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitIndividual_hash_partitions(PlSqlParser.Individual_hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_partitions_by_quantity(PlSqlParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_partitions_by_quantity(PlSqlParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_partition_quantity(PlSqlParser.Hash_partition_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_partition_quantity(PlSqlParser.Hash_partition_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_range_partitions(PlSqlParser.Composite_range_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_range_partitions(PlSqlParser.Composite_range_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_list_partitions(PlSqlParser.Composite_list_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_list_partitions(PlSqlParser.Composite_list_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_hash_partitions(PlSqlParser.Composite_hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_hash_partitions(PlSqlParser.Composite_hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#reference_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterReference_partitioning(PlSqlParser.Reference_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#reference_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitReference_partitioning(PlSqlParser.Reference_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterReference_partition_desc(PlSqlParser.Reference_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitReference_partition_desc(PlSqlParser.Reference_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#system_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterSystem_partitioning(PlSqlParser.System_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#system_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitSystem_partitioning(PlSqlParser.System_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#range_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterRange_partition_desc(PlSqlParser.Range_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#range_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitRange_partition_desc(PlSqlParser.Range_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#list_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterList_partition_desc(PlSqlParser.List_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#list_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitList_partition_desc(PlSqlParser.List_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_template}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_template(PlSqlParser.Subpartition_templateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_template}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_template(PlSqlParser.Subpartition_templateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_subpartition_quantity(PlSqlParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_subpartition_quantity(PlSqlParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_range(PlSqlParser.Subpartition_by_rangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_range(PlSqlParser.Subpartition_by_rangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_list(PlSqlParser.Subpartition_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_list(PlSqlParser.Subpartition_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_hash(PlSqlParser.Subpartition_by_hashContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_hash(PlSqlParser.Subpartition_by_hashContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_name}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_name(PlSqlParser.Subpartition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_name}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_name(PlSqlParser.Subpartition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void enterRange_subpartition_desc(PlSqlParser.Range_subpartition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void exitRange_subpartition_desc(PlSqlParser.Range_subpartition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void enterList_subpartition_desc(PlSqlParser.List_subpartition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void exitList_subpartition_desc(PlSqlParser.List_subpartition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 */
	void enterIndividual_hash_subparts(PlSqlParser.Individual_hash_subpartsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 */
	void exitIndividual_hash_subparts(PlSqlParser.Individual_hash_subpartsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_subparts_by_quantity(PlSqlParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_subparts_by_quantity(PlSqlParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#range_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterRange_values_clause(PlSqlParser.Range_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#range_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitRange_values_clause(PlSqlParser.Range_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#range_values_list}.
	 * @param ctx the parse tree
	 */
	void enterRange_values_list(PlSqlParser.Range_values_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#range_values_list}.
	 * @param ctx the parse tree
	 */
	void exitRange_values_list(PlSqlParser.Range_values_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#list_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterList_values_clause(PlSqlParser.List_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#list_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitList_values_clause(PlSqlParser.List_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_partition_description}.
	 * @param ctx the parse tree
	 */
	void enterTable_partition_description(PlSqlParser.Table_partition_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_partition_description}.
	 * @param ctx the parse tree
	 */
	void exitTable_partition_description(PlSqlParser.Table_partition_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartitioning_storage_clause(PlSqlParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartitioning_storage_clause(PlSqlParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 */
	void enterLob_partitioning_storage(PlSqlParser.Lob_partitioning_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 */
	void exitLob_partitioning_storage(PlSqlParser.Lob_partitioning_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 */
	void enterDatatype_null_enable(PlSqlParser.Datatype_null_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 */
	void exitDatatype_null_enable(PlSqlParser.Datatype_null_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#size_clause}.
	 * @param ctx the parse tree
	 */
	void enterSize_clause(PlSqlParser.Size_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#size_clause}.
	 * @param ctx the parse tree
	 */
	void exitSize_clause(PlSqlParser.Size_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_compression}.
	 * @param ctx the parse tree
	 */
	void enterTable_compression(PlSqlParser.Table_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_compression}.
	 * @param ctx the parse tree
	 */
	void exitTable_compression(PlSqlParser.Table_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_table_clause(PlSqlParser.Inmemory_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_table_clause(PlSqlParser.Inmemory_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_attributes(PlSqlParser.Inmemory_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_attributes(PlSqlParser.Inmemory_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_memcompress(PlSqlParser.Inmemory_memcompressContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_memcompress(PlSqlParser.Inmemory_memcompressContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_priority}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_priority(PlSqlParser.Inmemory_priorityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_priority}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_priority(PlSqlParser.Inmemory_priorityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_distribute(PlSqlParser.Inmemory_distributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_distribute(PlSqlParser.Inmemory_distributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_duplicate(PlSqlParser.Inmemory_duplicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_duplicate(PlSqlParser.Inmemory_duplicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_column_clause(PlSqlParser.Inmemory_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_column_clause(PlSqlParser.Inmemory_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterPhysical_attributes_clause(PlSqlParser.Physical_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitPhysical_attributes_clause(PlSqlParser.Physical_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterStorage_clause(PlSqlParser.Storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitStorage_clause(PlSqlParser.Storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 */
	void enterDeferred_segment_creation(PlSqlParser.Deferred_segment_creationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 */
	void exitDeferred_segment_creation(PlSqlParser.Deferred_segment_creationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterSegment_attributes_clause(PlSqlParser.Segment_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitSegment_attributes_clause(PlSqlParser.Segment_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#physical_properties}.
	 * @param ctx the parse tree
	 */
	void enterPhysical_properties(PlSqlParser.Physical_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#physical_properties}.
	 * @param ctx the parse tree
	 */
	void exitPhysical_properties(PlSqlParser.Physical_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_clause}.
	 * @param ctx the parse tree
	 */
	void enterIlm_clause(PlSqlParser.Ilm_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_clause}.
	 * @param ctx the parse tree
	 */
	void exitIlm_clause(PlSqlParser.Ilm_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 */
	void enterIlm_policy_clause(PlSqlParser.Ilm_policy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 */
	void exitIlm_policy_clause(PlSqlParser.Ilm_policy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_compression_policy(PlSqlParser.Ilm_compression_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_compression_policy(PlSqlParser.Ilm_compression_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_tiering_policy(PlSqlParser.Ilm_tiering_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_tiering_policy(PlSqlParser.Ilm_tiering_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_after_on}.
	 * @param ctx the parse tree
	 */
	void enterIlm_after_on(PlSqlParser.Ilm_after_onContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_after_on}.
	 * @param ctx the parse tree
	 */
	void exitIlm_after_on(PlSqlParser.Ilm_after_onContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#segment_group}.
	 * @param ctx the parse tree
	 */
	void enterSegment_group(PlSqlParser.Segment_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#segment_group}.
	 * @param ctx the parse tree
	 */
	void exitSegment_group(PlSqlParser.Segment_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_inmemory_policy(PlSqlParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_inmemory_policy(PlSqlParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ilm_time_period}.
	 * @param ctx the parse tree
	 */
	void enterIlm_time_period(PlSqlParser.Ilm_time_periodContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ilm_time_period}.
	 * @param ctx the parse tree
	 */
	void exitIlm_time_period(PlSqlParser.Ilm_time_periodContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterHeap_org_table_clause(PlSqlParser.Heap_org_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitHeap_org_table_clause(PlSqlParser.Heap_org_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#external_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterExternal_table_clause(PlSqlParser.External_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#external_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitExternal_table_clause(PlSqlParser.External_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#access_driver_type}.
	 * @param ctx the parse tree
	 */
	void enterAccess_driver_type(PlSqlParser.Access_driver_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#access_driver_type}.
	 * @param ctx the parse tree
	 */
	void exitAccess_driver_type(PlSqlParser.Access_driver_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#external_table_data_props}.
	 * @param ctx the parse tree
	 */
	void enterExternal_table_data_props(PlSqlParser.External_table_data_propsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#external_table_data_props}.
	 * @param ctx the parse tree
	 */
	void exitExternal_table_data_props(PlSqlParser.External_table_data_propsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 */
	void enterOpaque_format_spec(PlSqlParser.Opaque_format_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 */
	void exitOpaque_format_spec(PlSqlParser.Opaque_format_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#row_movement_clause}.
	 * @param ctx the parse tree
	 */
	void enterRow_movement_clause(PlSqlParser.Row_movement_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#row_movement_clause}.
	 * @param ctx the parse tree
	 */
	void exitRow_movement_clause(PlSqlParser.Row_movement_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_clause(PlSqlParser.Flashback_archive_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_clause(PlSqlParser.Flashback_archive_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#log_grp}.
	 * @param ctx the parse tree
	 */
	void enterLog_grp(PlSqlParser.Log_grpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#log_grp}.
	 * @param ctx the parse tree
	 */
	void exitLog_grp(PlSqlParser.Log_grpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_table_logging(PlSqlParser.Supplemental_table_loggingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_table_logging(PlSqlParser.Supplemental_table_loggingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_log_grp_clause(PlSqlParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_log_grp_clause(PlSqlParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_id_key_clause(PlSqlParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_id_key_clause(PlSqlParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 */
	void enterAllocate_extent_clause(PlSqlParser.Allocate_extent_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 */
	void exitAllocate_extent_clause(PlSqlParser.Allocate_extent_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 */
	void enterDeallocate_unused_clause(PlSqlParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 */
	void exitDeallocate_unused_clause(PlSqlParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#shrink_clause}.
	 * @param ctx the parse tree
	 */
	void enterShrink_clause(PlSqlParser.Shrink_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#shrink_clause}.
	 * @param ctx the parse tree
	 */
	void exitShrink_clause(PlSqlParser.Shrink_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 */
	void enterRecords_per_block_clause(PlSqlParser.Records_per_block_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 */
	void exitRecords_per_block_clause(PlSqlParser.Records_per_block_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpgrade_table_clause(PlSqlParser.Upgrade_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpgrade_table_clause(PlSqlParser.Upgrade_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#truncate_table}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_table(PlSqlParser.Truncate_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#truncate_table}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_table(PlSqlParser.Truncate_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_table}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table(PlSqlParser.Drop_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_table}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table(PlSqlParser.Drop_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDrop_tablespace(PlSqlParser.Drop_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDrop_tablespace(PlSqlParser.Drop_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterDrop_tablespace_set(PlSqlParser.Drop_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitDrop_tablespace_set(PlSqlParser.Drop_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#including_contents_clause}.
	 * @param ctx the parse tree
	 */
	void enterIncluding_contents_clause(PlSqlParser.Including_contents_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#including_contents_clause}.
	 * @param ctx the parse tree
	 */
	void exitIncluding_contents_clause(PlSqlParser.Including_contents_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_view(PlSqlParser.Drop_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_view(PlSqlParser.Drop_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#comment_on_column}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_column(PlSqlParser.Comment_on_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#comment_on_column}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_column(PlSqlParser.Comment_on_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#enable_or_disable}.
	 * @param ctx the parse tree
	 */
	void enterEnable_or_disable(PlSqlParser.Enable_or_disableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#enable_or_disable}.
	 * @param ctx the parse tree
	 */
	void exitEnable_or_disable(PlSqlParser.Enable_or_disableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 */
	void enterAllow_or_disallow(PlSqlParser.Allow_or_disallowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 */
	void exitAllow_or_disallow(PlSqlParser.Allow_or_disallowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_synonym}.
	 * @param ctx the parse tree
	 */
	void enterAlter_synonym(PlSqlParser.Alter_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_synonym}.
	 * @param ctx the parse tree
	 */
	void exitAlter_synonym(PlSqlParser.Alter_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_synonym}.
	 * @param ctx the parse tree
	 */
	void enterCreate_synonym(PlSqlParser.Create_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_synonym}.
	 * @param ctx the parse tree
	 */
	void exitCreate_synonym(PlSqlParser.Create_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_synonym}.
	 * @param ctx the parse tree
	 */
	void enterDrop_synonym(PlSqlParser.Drop_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_synonym}.
	 * @param ctx the parse tree
	 */
	void exitDrop_synonym(PlSqlParser.Drop_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_spfile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_spfile(PlSqlParser.Create_spfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_spfile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_spfile(PlSqlParser.Create_spfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#spfile_name}.
	 * @param ctx the parse tree
	 */
	void enterSpfile_name(PlSqlParser.Spfile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#spfile_name}.
	 * @param ctx the parse tree
	 */
	void exitSpfile_name(PlSqlParser.Spfile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pfile_name}.
	 * @param ctx the parse tree
	 */
	void enterPfile_name(PlSqlParser.Pfile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pfile_name}.
	 * @param ctx the parse tree
	 */
	void exitPfile_name(PlSqlParser.Pfile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#comment_on_table}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_table(PlSqlParser.Comment_on_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#comment_on_table}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_table(PlSqlParser.Comment_on_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#comment_context}.
	 * @param ctx the parse tree
	 */
	void enterComment_context(PlSqlParser.Comment_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#comment_context}.
	 * @param ctx the parse tree
	 */
	void exitComment_context(PlSqlParser.Comment_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_materialized(PlSqlParser.Comment_on_materializedContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_materialized(PlSqlParser.Comment_on_materializedContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_analytic_view(PlSqlParser.Alter_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_analytic_view(PlSqlParser.Alter_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_add_cache_clause(PlSqlParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_add_cache_clause(PlSqlParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#levels_item}.
	 * @param ctx the parse tree
	 */
	void enterLevels_item(PlSqlParser.Levels_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#levels_item}.
	 * @param ctx the parse tree
	 */
	void exitLevels_item(PlSqlParser.Levels_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#measure_list}.
	 * @param ctx the parse tree
	 */
	void enterMeasure_list(PlSqlParser.Measure_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#measure_list}.
	 * @param ctx the parse tree
	 */
	void exitMeasure_list(PlSqlParser.Measure_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_drop_cache_clause(PlSqlParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_drop_cache_clause(PlSqlParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterAlter_attribute_dimension(PlSqlParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitAlter_attribute_dimension(PlSqlParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterAlter_audit_policy(PlSqlParser.Alter_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitAlter_audit_policy(PlSqlParser.Alter_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_cluster}.
	 * @param ctx the parse tree
	 */
	void enterAlter_cluster(PlSqlParser.Alter_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_cluster}.
	 * @param ctx the parse tree
	 */
	void exitAlter_cluster(PlSqlParser.Alter_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_analytic_view(PlSqlParser.Drop_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_analytic_view(PlSqlParser.Drop_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterDrop_attribute_dimension(PlSqlParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitDrop_attribute_dimension(PlSqlParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterDrop_audit_policy(PlSqlParser.Drop_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitDrop_audit_policy(PlSqlParser.Drop_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterDrop_flashback_archive(PlSqlParser.Drop_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitDrop_flashback_archive(PlSqlParser.Drop_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_cluster}.
	 * @param ctx the parse tree
	 */
	void enterDrop_cluster(PlSqlParser.Drop_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_cluster}.
	 * @param ctx the parse tree
	 */
	void exitDrop_cluster(PlSqlParser.Drop_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_context}.
	 * @param ctx the parse tree
	 */
	void enterDrop_context(PlSqlParser.Drop_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_context}.
	 * @param ctx the parse tree
	 */
	void exitDrop_context(PlSqlParser.Drop_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_directory}.
	 * @param ctx the parse tree
	 */
	void enterDrop_directory(PlSqlParser.Drop_directoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_directory}.
	 * @param ctx the parse tree
	 */
	void exitDrop_directory(PlSqlParser.Drop_directoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterDrop_diskgroup(PlSqlParser.Drop_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitDrop_diskgroup(PlSqlParser.Drop_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_edition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_edition(PlSqlParser.Drop_editionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_edition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_edition(PlSqlParser.Drop_editionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#truncate_cluster}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_cluster(PlSqlParser.Truncate_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#truncate_cluster}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_cluster(PlSqlParser.Truncate_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 */
	void enterCache_or_nocache(PlSqlParser.Cache_or_nocacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 */
	void exitCache_or_nocache(PlSqlParser.Cache_or_nocacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database_name}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_name(PlSqlParser.Database_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database_name}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_name(PlSqlParser.Database_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_database}.
	 * @param ctx the parse tree
	 */
	void enterAlter_database(PlSqlParser.Alter_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_database}.
	 * @param ctx the parse tree
	 */
	void exitAlter_database(PlSqlParser.Alter_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database_clause}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_clause(PlSqlParser.Database_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database_clause}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_clause(PlSqlParser.Database_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#startup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStartup_clauses(PlSqlParser.Startup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#startup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStartup_clauses(PlSqlParser.Startup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 */
	void enterResetlogs_or_noresetlogs(PlSqlParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 */
	void exitResetlogs_or_noresetlogs(PlSqlParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 */
	void enterUpgrade_or_downgrade(PlSqlParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 */
	void exitUpgrade_or_downgrade(PlSqlParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#recovery_clauses}.
	 * @param ctx the parse tree
	 */
	void enterRecovery_clauses(PlSqlParser.Recovery_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#recovery_clauses}.
	 * @param ctx the parse tree
	 */
	void exitRecovery_clauses(PlSqlParser.Recovery_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#begin_or_end}.
	 * @param ctx the parse tree
	 */
	void enterBegin_or_end(PlSqlParser.Begin_or_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#begin_or_end}.
	 * @param ctx the parse tree
	 */
	void exitBegin_or_end(PlSqlParser.Begin_or_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#general_recovery}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_recovery(PlSqlParser.General_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#general_recovery}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_recovery(PlSqlParser.General_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#full_database_recovery}.
	 * @param ctx the parse tree
	 */
	void enterFull_database_recovery(PlSqlParser.Full_database_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#full_database_recovery}.
	 * @param ctx the parse tree
	 */
	void exitFull_database_recovery(PlSqlParser.Full_database_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 */
	void enterPartial_database_recovery(PlSqlParser.Partial_database_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 */
	void exitPartial_database_recovery(PlSqlParser.Partial_database_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 */
	void enterPartial_database_recovery_10g(PlSqlParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 */
	void exitPartial_database_recovery_10g(PlSqlParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 */
	void enterManaged_standby_recovery(PlSqlParser.Managed_standby_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 */
	void exitManaged_standby_recovery(PlSqlParser.Managed_standby_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#db_name}.
	 * @param ctx the parse tree
	 */
	void enterDb_name(PlSqlParser.Db_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#db_name}.
	 * @param ctx the parse tree
	 */
	void exitDb_name(PlSqlParser.Db_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database_file_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_file_clauses(PlSqlParser.Database_file_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database_file_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_file_clauses(PlSqlParser.Database_file_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterCreate_datafile_clause(PlSqlParser.Create_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitCreate_datafile_clause(PlSqlParser.Create_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_datafile_clause(PlSqlParser.Alter_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_datafile_clause(PlSqlParser.Alter_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tempfile_clause(PlSqlParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tempfile_clause(PlSqlParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_datafile_clause(PlSqlParser.Move_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_datafile_clause(PlSqlParser.Move_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_clauses(PlSqlParser.Logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_clauses(PlSqlParser.Logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_logfile_clauses(PlSqlParser.Add_logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_logfile_clauses(PlSqlParser.Add_logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 */
	void enterGroup_redo_logfile(PlSqlParser.Group_redo_logfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 */
	void exitGroup_redo_logfile(PlSqlParser.Group_redo_logfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDrop_logfile_clauses(PlSqlParser.Drop_logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDrop_logfile_clauses(PlSqlParser.Drop_logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_logfile_clause(PlSqlParser.Switch_logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_logfile_clause(PlSqlParser.Switch_logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_db_logging(PlSqlParser.Supplemental_db_loggingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_db_logging(PlSqlParser.Supplemental_db_loggingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_or_drop}.
	 * @param ctx the parse tree
	 */
	void enterAdd_or_drop(PlSqlParser.Add_or_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_or_drop}.
	 * @param ctx the parse tree
	 */
	void exitAdd_or_drop(PlSqlParser.Add_or_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_plsql_clause(PlSqlParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_plsql_clause(PlSqlParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_descriptor(PlSqlParser.Logfile_descriptorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_descriptor(PlSqlParser.Logfile_descriptorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterControlfile_clauses(PlSqlParser.Controlfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitControlfile_clauses(PlSqlParser.Controlfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trace_file_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrace_file_clause(PlSqlParser.Trace_file_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trace_file_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrace_file_clause(PlSqlParser.Trace_file_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStandby_database_clauses(PlSqlParser.Standby_database_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStandby_database_clauses(PlSqlParser.Standby_database_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void enterActivate_standby_db_clause(PlSqlParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void exitActivate_standby_db_clause(PlSqlParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void enterMaximize_standby_db_clause(PlSqlParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void exitMaximize_standby_db_clause(PlSqlParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterRegister_logfile_clause(PlSqlParser.Register_logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitRegister_logfile_clause(PlSqlParser.Register_logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 */
	void enterCommit_switchover_clause(PlSqlParser.Commit_switchover_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 */
	void exitCommit_switchover_clause(PlSqlParser.Commit_switchover_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#start_standby_clause}.
	 * @param ctx the parse tree
	 */
	void enterStart_standby_clause(PlSqlParser.Start_standby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#start_standby_clause}.
	 * @param ctx the parse tree
	 */
	void exitStart_standby_clause(PlSqlParser.Start_standby_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 */
	void enterStop_standby_clause(PlSqlParser.Stop_standby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 */
	void exitStop_standby_clause(PlSqlParser.Stop_standby_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#convert_database_clause}.
	 * @param ctx the parse tree
	 */
	void enterConvert_database_clause(PlSqlParser.Convert_database_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#convert_database_clause}.
	 * @param ctx the parse tree
	 */
	void exitConvert_database_clause(PlSqlParser.Convert_database_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_settings_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_settings_clause(PlSqlParser.Default_settings_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_settings_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_settings_clause(PlSqlParser.Default_settings_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 */
	void enterSet_time_zone_clause(PlSqlParser.Set_time_zone_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 */
	void exitSet_time_zone_clause(PlSqlParser.Set_time_zone_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#instance_clauses}.
	 * @param ctx the parse tree
	 */
	void enterInstance_clauses(PlSqlParser.Instance_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#instance_clauses}.
	 * @param ctx the parse tree
	 */
	void exitInstance_clauses(PlSqlParser.Instance_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#security_clause}.
	 * @param ctx the parse tree
	 */
	void enterSecurity_clause(PlSqlParser.Security_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#security_clause}.
	 * @param ctx the parse tree
	 */
	void exitSecurity_clause(PlSqlParser.Security_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#domain}.
	 * @param ctx the parse tree
	 */
	void enterDomain(PlSqlParser.DomainContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#domain}.
	 * @param ctx the parse tree
	 */
	void exitDomain(PlSqlParser.DomainContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database}.
	 * @param ctx the parse tree
	 */
	void enterDatabase(PlSqlParser.DatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database}.
	 * @param ctx the parse tree
	 */
	void exitDatabase(PlSqlParser.DatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#edition_name}.
	 * @param ctx the parse tree
	 */
	void enterEdition_name(PlSqlParser.Edition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#edition_name}.
	 * @param ctx the parse tree
	 */
	void exitEdition_name(PlSqlParser.Edition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filenumber}.
	 * @param ctx the parse tree
	 */
	void enterFilenumber(PlSqlParser.FilenumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filenumber}.
	 * @param ctx the parse tree
	 */
	void exitFilenumber(PlSqlParser.FilenumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filename}.
	 * @param ctx the parse tree
	 */
	void enterFilename(PlSqlParser.FilenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filename}.
	 * @param ctx the parse tree
	 */
	void exitFilename(PlSqlParser.FilenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#prepare_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrepare_clause(PlSqlParser.Prepare_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#prepare_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrepare_clause(PlSqlParser.Prepare_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_mirror_clause(PlSqlParser.Drop_mirror_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_mirror_clause(PlSqlParser.Drop_mirror_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lost_write_protection}.
	 * @param ctx the parse tree
	 */
	void enterLost_write_protection(PlSqlParser.Lost_write_protectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lost_write_protection}.
	 * @param ctx the parse tree
	 */
	void exitLost_write_protection(PlSqlParser.Lost_write_protectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 */
	void enterCdb_fleet_clauses(PlSqlParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 */
	void exitCdb_fleet_clauses(PlSqlParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 */
	void enterLead_cdb_clause(PlSqlParser.Lead_cdb_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 */
	void exitLead_cdb_clause(PlSqlParser.Lead_cdb_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 */
	void enterLead_cdb_uri_clause(PlSqlParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 */
	void exitLead_cdb_uri_clause(PlSqlParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#property_clauses}.
	 * @param ctx the parse tree
	 */
	void enterProperty_clauses(PlSqlParser.Property_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#property_clauses}.
	 * @param ctx the parse tree
	 */
	void exitProperty_clauses(PlSqlParser.Property_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 */
	void enterReplay_upgrade_clauses(PlSqlParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 */
	void exitReplay_upgrade_clauses(PlSqlParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_database_link}.
	 * @param ctx the parse tree
	 */
	void enterAlter_database_link(PlSqlParser.Alter_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_database_link}.
	 * @param ctx the parse tree
	 */
	void exitAlter_database_link(PlSqlParser.Alter_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#password_value}.
	 * @param ctx the parse tree
	 */
	void enterPassword_value(PlSqlParser.Password_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#password_value}.
	 * @param ctx the parse tree
	 */
	void exitPassword_value(PlSqlParser.Password_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#link_authentication}.
	 * @param ctx the parse tree
	 */
	void enterLink_authentication(PlSqlParser.Link_authenticationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#link_authentication}.
	 * @param ctx the parse tree
	 */
	void exitLink_authentication(PlSqlParser.Link_authenticationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_schema}.
	 * @param ctx the parse tree
	 */
	void enterCreate_schema(PlSqlParser.Create_schemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_schema}.
	 * @param ctx the parse tree
	 */
	void exitCreate_schema(PlSqlParser.Create_schemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_database}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database(PlSqlParser.Create_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_database}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database(PlSqlParser.Create_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_logging_clauses(PlSqlParser.Database_logging_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_logging_clauses(PlSqlParser.Database_logging_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_logging_sub_clause(PlSqlParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_logging_sub_clause(PlSqlParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_clauses(PlSqlParser.Tablespace_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_clauses(PlSqlParser.Tablespace_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 */
	void enterEnable_pluggable_database(PlSqlParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 */
	void exitEnable_pluggable_database(PlSqlParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#file_name_convert}.
	 * @param ctx the parse tree
	 */
	void enterFile_name_convert(PlSqlParser.File_name_convertContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#file_name_convert}.
	 * @param ctx the parse tree
	 */
	void exitFile_name_convert(PlSqlParser.File_name_convertContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilename_convert_sub_clause(PlSqlParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilename_convert_sub_clause(PlSqlParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_datafile_clauses(PlSqlParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_datafile_clauses(PlSqlParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndo_mode_clause(PlSqlParser.Undo_mode_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndo_mode_clause(PlSqlParser.Undo_mode_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDefault_tablespace(PlSqlParser.Default_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDefault_tablespace(PlSqlParser.Default_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDefault_temp_tablespace(PlSqlParser.Default_temp_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDefault_temp_tablespace(PlSqlParser.Default_temp_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#undo_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterUndo_tablespace(PlSqlParser.Undo_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#undo_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitUndo_tablespace(PlSqlParser.Undo_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_database}.
	 * @param ctx the parse tree
	 */
	void enterDrop_database(PlSqlParser.Drop_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_database}.
	 * @param ctx the parse tree
	 */
	void exitDrop_database(PlSqlParser.Drop_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#create_database_link}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database_link(PlSqlParser.Create_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#create_database_link}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database_link(PlSqlParser.Create_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_database_link}.
	 * @param ctx the parse tree
	 */
	void enterDrop_database_link(PlSqlParser.Drop_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_database_link}.
	 * @param ctx the parse tree
	 */
	void exitDrop_database_link(PlSqlParser.Drop_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_set(PlSqlParser.Alter_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_set(PlSqlParser.Alter_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_attrs(PlSqlParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_attrs(PlSqlParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_encryption(PlSqlParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_encryption(PlSqlParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 */
	void enterTs_file_name_convert(PlSqlParser.Ts_file_name_convertContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 */
	void exitTs_file_name_convert(PlSqlParser.Ts_file_name_convertContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_role}.
	 * @param ctx the parse tree
	 */
	void enterAlter_role(PlSqlParser.Alter_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_role}.
	 * @param ctx the parse tree
	 */
	void exitAlter_role(PlSqlParser.Alter_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#role_identified_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_identified_clause(PlSqlParser.Role_identified_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#role_identified_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_identified_clause(PlSqlParser.Role_identified_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_table}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table(PlSqlParser.Alter_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_table}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table(PlSqlParser.Alter_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 */
	void enterMemoptimize_read_write_clause(PlSqlParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 */
	void exitMemoptimize_read_write_clause(PlSqlParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_table_properties}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_properties(PlSqlParser.Alter_table_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_table_properties}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_properties(PlSqlParser.Alter_table_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_partitioning(PlSqlParser.Alter_table_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_partitioning(PlSqlParser.Alter_table_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterAdd_table_partition(PlSqlParser.Add_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitAdd_table_partition(PlSqlParser.Add_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_partition(PlSqlParser.Drop_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_partition(PlSqlParser.Drop_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterMerge_table_partition(PlSqlParser.Merge_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitMerge_table_partition(PlSqlParser.Merge_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterModify_table_partition(PlSqlParser.Modify_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitModify_table_partition(PlSqlParser.Modify_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#split_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterSplit_table_partition(PlSqlParser.Split_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#split_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitSplit_table_partition(PlSqlParser.Split_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_table_partition(PlSqlParser.Truncate_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_table_partition(PlSqlParser.Truncate_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterExchange_table_partition(PlSqlParser.Exchange_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitExchange_table_partition(PlSqlParser.Exchange_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterCoalesce_table_partition(PlSqlParser.Coalesce_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitCoalesce_table_partition(PlSqlParser.Coalesce_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 */
	void enterAlter_interval_partition(PlSqlParser.Alter_interval_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 */
	void exitAlter_interval_partition(PlSqlParser.Alter_interval_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_extended_names}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extended_names(PlSqlParser.Partition_extended_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_extended_names}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extended_names(PlSqlParser.Partition_extended_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_extended_names(PlSqlParser.Subpartition_extended_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_extended_names(PlSqlParser.Subpartition_extended_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_properties_1(PlSqlParser.Alter_table_properties_1Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_properties_1(PlSqlParser.Alter_table_properties_1Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAlter_iot_clauses(PlSqlParser.Alter_iot_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAlter_iot_clauses(PlSqlParser.Alter_iot_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mapping_table_clause(PlSqlParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mapping_table_clause(PlSqlParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_overflow_clause(PlSqlParser.Alter_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_overflow_clause(PlSqlParser.Alter_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_overflow_clause(PlSqlParser.Add_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_overflow_clause(PlSqlParser.Add_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_index_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_clauses(PlSqlParser.Update_index_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_index_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_clauses(PlSqlParser.Update_index_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_global_index_clause(PlSqlParser.Update_global_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_global_index_clause(PlSqlParser.Update_global_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_all_indexes_clause(PlSqlParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_all_indexes_clause(PlSqlParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_all_indexes_index_clause(PlSqlParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_all_indexes_index_clause(PlSqlParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_partition(PlSqlParser.Update_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_partition(PlSqlParser.Update_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_subpartition(PlSqlParser.Update_index_subpartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_subpartition(PlSqlParser.Update_index_subpartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 */
	void enterEnable_disable_clause(PlSqlParser.Enable_disable_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 */
	void exitEnable_disable_clause(PlSqlParser.Enable_disable_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_index_clause(PlSqlParser.Using_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_index_clause(PlSqlParser.Using_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_attributes}.
	 * @param ctx the parse tree
	 */
	void enterIndex_attributes(PlSqlParser.Index_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_attributes}.
	 * @param ctx the parse tree
	 */
	void exitIndex_attributes(PlSqlParser.Index_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 */
	void enterSort_or_nosort(PlSqlParser.Sort_or_nosortContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 */
	void exitSort_or_nosort(PlSqlParser.Sort_or_nosortContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exceptions_clause}.
	 * @param ctx the parse tree
	 */
	void enterExceptions_clause(PlSqlParser.Exceptions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exceptions_clause}.
	 * @param ctx the parse tree
	 */
	void exitExceptions_clause(PlSqlParser.Exceptions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#move_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_table_clause(PlSqlParser.Move_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#move_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_table_clause(PlSqlParser.Move_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_org_table_clause(PlSqlParser.Index_org_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_org_table_clause(PlSqlParser.Index_org_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterMapping_table_clause(PlSqlParser.Mapping_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitMapping_table_clause(PlSqlParser.Mapping_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#key_compression}.
	 * @param ctx the parse tree
	 */
	void enterKey_compression(PlSqlParser.Key_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#key_compression}.
	 * @param ctx the parse tree
	 */
	void exitKey_compression(PlSqlParser.Key_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_org_overflow_clause(PlSqlParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_org_overflow_clause(PlSqlParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterColumn_clauses(PlSqlParser.Column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitColumn_clauses(PlSqlParser.Column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 */
	void enterModify_collection_retrieval(PlSqlParser.Modify_collection_retrievalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 */
	void exitModify_collection_retrieval(PlSqlParser.Modify_collection_retrievalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collection_item}.
	 * @param ctx the parse tree
	 */
	void enterCollection_item(PlSqlParser.Collection_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collection_item}.
	 * @param ctx the parse tree
	 */
	void exitCollection_item(PlSqlParser.Collection_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rename_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterRename_column_clause(PlSqlParser.Rename_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rename_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitRename_column_clause(PlSqlParser.Rename_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#old_column_name}.
	 * @param ctx the parse tree
	 */
	void enterOld_column_name(PlSqlParser.Old_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#old_column_name}.
	 * @param ctx the parse tree
	 */
	void exitOld_column_name(PlSqlParser.Old_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_column_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_column_name(PlSqlParser.New_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_column_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_column_name(PlSqlParser.New_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_modify_drop_column_clauses(PlSqlParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_modify_drop_column_clauses(PlSqlParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_column_clause(PlSqlParser.Drop_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_column_clause(PlSqlParser.Drop_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModify_column_clauses(PlSqlParser.Modify_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModify_column_clauses(PlSqlParser.Modify_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_properties(PlSqlParser.Modify_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_properties(PlSqlParser.Modify_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#col_default}.
	 * @param ctx the parse tree
	 */
	void enterCol_default(PlSqlParser.Col_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#col_default}.
	 * @param ctx the parse tree
	 */
	void exitCol_default(PlSqlParser.Col_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_visibility(PlSqlParser.Modify_col_visibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_visibility(PlSqlParser.Modify_col_visibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_substitutable(PlSqlParser.Modify_col_substitutableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_substitutable(PlSqlParser.Modify_col_substitutableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_column_clause(PlSqlParser.Add_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_column_clause(PlSqlParser.Add_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterAlter_varray_col_properties(PlSqlParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitAlter_varray_col_properties(PlSqlParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterVarray_col_properties(PlSqlParser.Varray_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitVarray_col_properties(PlSqlParser.Varray_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterVarray_storage_clause(PlSqlParser.Varray_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitVarray_storage_clause(PlSqlParser.Varray_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_segname}.
	 * @param ctx the parse tree
	 */
	void enterLob_segname(PlSqlParser.Lob_segnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_segname}.
	 * @param ctx the parse tree
	 */
	void exitLob_segname(PlSqlParser.Lob_segnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_item}.
	 * @param ctx the parse tree
	 */
	void enterLob_item(PlSqlParser.Lob_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_item}.
	 * @param ctx the parse tree
	 */
	void exitLob_item(PlSqlParser.Lob_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 */
	void enterLob_storage_parameters(PlSqlParser.Lob_storage_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 */
	void exitLob_storage_parameters(PlSqlParser.Lob_storage_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_storage_clause(PlSqlParser.Lob_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_storage_clause(PlSqlParser.Lob_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_lob_storage_clause(PlSqlParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_lob_storage_clause(PlSqlParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 */
	void enterModify_lob_parameters(PlSqlParser.Modify_lob_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 */
	void exitModify_lob_parameters(PlSqlParser.Modify_lob_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_parameters}.
	 * @param ctx the parse tree
	 */
	void enterLob_parameters(PlSqlParser.Lob_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_parameters}.
	 * @param ctx the parse tree
	 */
	void exitLob_parameters(PlSqlParser.Lob_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_deduplicate_clause(PlSqlParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_deduplicate_clause(PlSqlParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_compression_clause(PlSqlParser.Lob_compression_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_compression_clause(PlSqlParser.Lob_compression_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_retention_clause(PlSqlParser.Lob_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_retention_clause(PlSqlParser.Lob_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#encryption_spec}.
	 * @param ctx the parse tree
	 */
	void enterEncryption_spec(PlSqlParser.Encryption_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#encryption_spec}.
	 * @param ctx the parse tree
	 */
	void exitEncryption_spec(PlSqlParser.Encryption_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tablespace}.
	 * @param ctx the parse tree
	 */
	void enterTablespace(PlSqlParser.TablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tablespace}.
	 * @param ctx the parse tree
	 */
	void exitTablespace(PlSqlParser.TablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#varray_item}.
	 * @param ctx the parse tree
	 */
	void enterVarray_item(PlSqlParser.Varray_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#varray_item}.
	 * @param ctx the parse tree
	 */
	void exitVarray_item(PlSqlParser.Varray_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_properties}.
	 * @param ctx the parse tree
	 */
	void enterColumn_properties(PlSqlParser.Column_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_properties}.
	 * @param ctx the parse tree
	 */
	void exitColumn_properties(PlSqlParser.Column_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 */
	void enterLob_partition_storage(PlSqlParser.Lob_partition_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 */
	void exitLob_partition_storage(PlSqlParser.Lob_partition_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#period_definition}.
	 * @param ctx the parse tree
	 */
	void enterPeriod_definition(PlSqlParser.Period_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#period_definition}.
	 * @param ctx the parse tree
	 */
	void exitPeriod_definition(PlSqlParser.Period_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#start_time_column}.
	 * @param ctx the parse tree
	 */
	void enterStart_time_column(PlSqlParser.Start_time_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#start_time_column}.
	 * @param ctx the parse tree
	 */
	void exitStart_time_column(PlSqlParser.Start_time_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#end_time_column}.
	 * @param ctx the parse tree
	 */
	void enterEnd_time_column(PlSqlParser.End_time_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#end_time_column}.
	 * @param ctx the parse tree
	 */
	void exitEnd_time_column(PlSqlParser.End_time_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_definition}.
	 * @param ctx the parse tree
	 */
	void enterColumn_definition(PlSqlParser.Column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_definition}.
	 * @param ctx the parse tree
	 */
	void exitColumn_definition(PlSqlParser.Column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_collation_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_collation_name(PlSqlParser.Column_collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_collation_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_collation_name(PlSqlParser.Column_collation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identity_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_clause(PlSqlParser.Identity_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identity_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_clause(PlSqlParser.Identity_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_options_parentheses(PlSqlParser.Identity_options_parenthesesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_options_parentheses(PlSqlParser.Identity_options_parenthesesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identity_options}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_options(PlSqlParser.Identity_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identity_options}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_options(PlSqlParser.Identity_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 */
	void enterVirtual_column_definition(PlSqlParser.Virtual_column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 */
	void exitVirtual_column_definition(PlSqlParser.Virtual_column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 */
	void enterAutogenerated_sequence_definition(PlSqlParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 */
	void exitAutogenerated_sequence_definition(PlSqlParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation_edition_clause(PlSqlParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation_edition_clause(PlSqlParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_part_storage(PlSqlParser.Out_of_line_part_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_part_storage(PlSqlParser.Out_of_line_part_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterNested_table_col_properties(PlSqlParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitNested_table_col_properties(PlSqlParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#nested_item}.
	 * @param ctx the parse tree
	 */
	void enterNested_item(PlSqlParser.Nested_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#nested_item}.
	 * @param ctx the parse tree
	 */
	void exitNested_item(PlSqlParser.Nested_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubstitutable_column_clause(PlSqlParser.Substitutable_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubstitutable_column_clause(PlSqlParser.Substitutable_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_name}.
	 * @param ctx the parse tree
	 */
	void enterPartition_name(PlSqlParser.Partition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_name}.
	 * @param ctx the parse tree
	 */
	void exitPartition_name(PlSqlParser.Partition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_logging_props(PlSqlParser.Supplemental_logging_propsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_logging_props(PlSqlParser.Supplemental_logging_propsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_or_attribute}.
	 * @param ctx the parse tree
	 */
	void enterColumn_or_attribute(PlSqlParser.Column_or_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_or_attribute}.
	 * @param ctx the parse tree
	 */
	void exitColumn_or_attribute(PlSqlParser.Column_or_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_col_properties(PlSqlParser.Object_type_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_col_properties(PlSqlParser.Object_type_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_table_constarint(PlSqlParser.Add_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_table_constarint(PlSqlParser.Add_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModify_table_constarint(PlSqlParser.Modify_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModify_table_constarint(PlSqlParser.Modify_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterRename_table_constarint(PlSqlParser.Rename_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitRename_table_constarint(PlSqlParser.Rename_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_constarint(PlSqlParser.Drop_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link PlSqlParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_constarint(PlSqlParser.Drop_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#old_constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterOld_constraint_name(PlSqlParser.Old_constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#old_constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitOld_constraint_name(PlSqlParser.Old_constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#new_constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_constraint_name(PlSqlParser.New_constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#new_constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_constraint_name(PlSqlParser.New_constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_constraint_clause(PlSqlParser.Drop_constraint_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_constraint_clause(PlSqlParser.Drop_constraint_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_constraint}.
	 * @param ctx the parse tree
	 */
	void enterAdd_constraint(PlSqlParser.Add_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_constraint}.
	 * @param ctx the parse tree
	 */
	void exitAdd_constraint(PlSqlParser.Add_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_constraint_clause(PlSqlParser.Add_constraint_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_constraint_clause(PlSqlParser.Add_constraint_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#check_constraint}.
	 * @param ctx the parse tree
	 */
	void enterCheck_constraint(PlSqlParser.Check_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#check_constraint}.
	 * @param ctx the parse tree
	 */
	void exitCheck_constraint(PlSqlParser.Check_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#drop_constraint}.
	 * @param ctx the parse tree
	 */
	void enterDrop_constraint(PlSqlParser.Drop_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#drop_constraint}.
	 * @param ctx the parse tree
	 */
	void exitDrop_constraint(PlSqlParser.Drop_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#enable_constraint}.
	 * @param ctx the parse tree
	 */
	void enterEnable_constraint(PlSqlParser.Enable_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#enable_constraint}.
	 * @param ctx the parse tree
	 */
	void exitEnable_constraint(PlSqlParser.Enable_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#disable_constraint}.
	 * @param ctx the parse tree
	 */
	void enterDisable_constraint(PlSqlParser.Disable_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#disable_constraint}.
	 * @param ctx the parse tree
	 */
	void exitDisable_constraint(PlSqlParser.Disable_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterForeign_key_clause(PlSqlParser.Foreign_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitForeign_key_clause(PlSqlParser.Foreign_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#references_clause}.
	 * @param ctx the parse tree
	 */
	void enterReferences_clause(PlSqlParser.References_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#references_clause}.
	 * @param ctx the parse tree
	 */
	void exitReferences_clause(PlSqlParser.References_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#on_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_delete_clause(PlSqlParser.On_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#on_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_delete_clause(PlSqlParser.On_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unique_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnique_key_clause(PlSqlParser.Unique_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unique_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnique_key_clause(PlSqlParser.Unique_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#primary_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key_clause(PlSqlParser.Primary_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#primary_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key_clause(PlSqlParser.Primary_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#anonymous_block}.
	 * @param ctx the parse tree
	 */
	void enterAnonymous_block(PlSqlParser.Anonymous_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#anonymous_block}.
	 * @param ctx the parse tree
	 */
	void exitAnonymous_block(PlSqlParser.Anonymous_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 */
	void enterInvoker_rights_clause(PlSqlParser.Invoker_rights_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 */
	void exitInvoker_rights_clause(PlSqlParser.Invoker_rights_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#call_spec}.
	 * @param ctx the parse tree
	 */
	void enterCall_spec(PlSqlParser.Call_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#call_spec}.
	 * @param ctx the parse tree
	 */
	void exitCall_spec(PlSqlParser.Call_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#java_spec}.
	 * @param ctx the parse tree
	 */
	void enterJava_spec(PlSqlParser.Java_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#java_spec}.
	 * @param ctx the parse tree
	 */
	void exitJava_spec(PlSqlParser.Java_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#c_spec}.
	 * @param ctx the parse tree
	 */
	void enterC_spec(PlSqlParser.C_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#c_spec}.
	 * @param ctx the parse tree
	 */
	void exitC_spec(PlSqlParser.C_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterC_agent_in_clause(PlSqlParser.C_agent_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitC_agent_in_clause(PlSqlParser.C_agent_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void enterC_parameters_clause(PlSqlParser.C_parameters_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void exitC_parameters_clause(PlSqlParser.C_parameters_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#c_external_parameter}.
	 * @param ctx the parse tree
	 */
	void enterC_external_parameter(PlSqlParser.C_external_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#c_external_parameter}.
	 * @param ctx the parse tree
	 */
	void exitC_external_parameter(PlSqlParser.C_external_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#c_property}.
	 * @param ctx the parse tree
	 */
	void enterC_property(PlSqlParser.C_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#c_property}.
	 * @param ctx the parse tree
	 */
	void exitC_property(PlSqlParser.C_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(PlSqlParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(PlSqlParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#default_value_part}.
	 * @param ctx the parse tree
	 */
	void enterDefault_value_part(PlSqlParser.Default_value_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#default_value_part}.
	 * @param ctx the parse tree
	 */
	void exitDefault_value_part(PlSqlParser.Default_value_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 */
	void enterSeq_of_declare_specs(PlSqlParser.Seq_of_declare_specsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 */
	void exitSeq_of_declare_specs(PlSqlParser.Seq_of_declare_specsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#declare_spec}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_spec(PlSqlParser.Declare_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#declare_spec}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_spec(PlSqlParser.Declare_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariable_declaration(PlSqlParser.Variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariable_declaration(PlSqlParser.Variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subtype_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSubtype_declaration(PlSqlParser.Subtype_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subtype_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSubtype_declaration(PlSqlParser.Subtype_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cursor_declaration}.
	 * @param ctx the parse tree
	 */
	void enterCursor_declaration(PlSqlParser.Cursor_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cursor_declaration}.
	 * @param ctx the parse tree
	 */
	void exitCursor_declaration(PlSqlParser.Cursor_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parameter_spec}.
	 * @param ctx the parse tree
	 */
	void enterParameter_spec(PlSqlParser.Parameter_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parameter_spec}.
	 * @param ctx the parse tree
	 */
	void exitParameter_spec(PlSqlParser.Parameter_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exception_declaration}.
	 * @param ctx the parse tree
	 */
	void enterException_declaration(PlSqlParser.Exception_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exception_declaration}.
	 * @param ctx the parse tree
	 */
	void exitException_declaration(PlSqlParser.Exception_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pragma_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPragma_declaration(PlSqlParser.Pragma_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pragma_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPragma_declaration(PlSqlParser.Pragma_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#record_type_def}.
	 * @param ctx the parse tree
	 */
	void enterRecord_type_def(PlSqlParser.Record_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#record_type_def}.
	 * @param ctx the parse tree
	 */
	void exitRecord_type_def(PlSqlParser.Record_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void enterField_spec(PlSqlParser.Field_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void exitField_spec(PlSqlParser.Field_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 */
	void enterRef_cursor_type_def(PlSqlParser.Ref_cursor_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 */
	void exitRef_cursor_type_def(PlSqlParser.Ref_cursor_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void enterType_declaration(PlSqlParser.Type_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void exitType_declaration(PlSqlParser.Type_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_type_def}.
	 * @param ctx the parse tree
	 */
	void enterTable_type_def(PlSqlParser.Table_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_type_def}.
	 * @param ctx the parse tree
	 */
	void exitTable_type_def(PlSqlParser.Table_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 */
	void enterTable_indexed_by_part(PlSqlParser.Table_indexed_by_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 */
	void exitTable_indexed_by_part(PlSqlParser.Table_indexed_by_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#varray_type_def}.
	 * @param ctx the parse tree
	 */
	void enterVarray_type_def(PlSqlParser.Varray_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#varray_type_def}.
	 * @param ctx the parse tree
	 */
	void exitVarray_type_def(PlSqlParser.Varray_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#seq_of_statements}.
	 * @param ctx the parse tree
	 */
	void enterSeq_of_statements(PlSqlParser.Seq_of_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#seq_of_statements}.
	 * @param ctx the parse tree
	 */
	void exitSeq_of_statements(PlSqlParser.Seq_of_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#label_declaration}.
	 * @param ctx the parse tree
	 */
	void enterLabel_declaration(PlSqlParser.Label_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#label_declaration}.
	 * @param ctx the parse tree
	 */
	void exitLabel_declaration(PlSqlParser.Label_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PlSqlParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PlSqlParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 */
	void enterSwallow_to_semi(PlSqlParser.Swallow_to_semiContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 */
	void exitSwallow_to_semi(PlSqlParser.Swallow_to_semiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(PlSqlParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(PlSqlParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue_statement(PlSqlParser.Continue_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue_statement(PlSqlParser.Continue_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exit_statement}.
	 * @param ctx the parse tree
	 */
	void enterExit_statement(PlSqlParser.Exit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exit_statement}.
	 * @param ctx the parse tree
	 */
	void exitExit_statement(PlSqlParser.Exit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#goto_statement}.
	 * @param ctx the parse tree
	 */
	void enterGoto_statement(PlSqlParser.Goto_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#goto_statement}.
	 * @param ctx the parse tree
	 */
	void exitGoto_statement(PlSqlParser.Goto_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(PlSqlParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(PlSqlParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void enterElsif_part(PlSqlParser.Elsif_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void exitElsif_part(PlSqlParser.Elsif_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#else_part}.
	 * @param ctx the parse tree
	 */
	void enterElse_part(PlSqlParser.Else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#else_part}.
	 * @param ctx the parse tree
	 */
	void exitElse_part(PlSqlParser.Else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void enterLoop_statement(PlSqlParser.Loop_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void exitLoop_statement(PlSqlParser.Loop_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 */
	void enterCursor_loop_param(PlSqlParser.Cursor_loop_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 */
	void exitCursor_loop_param(PlSqlParser.Cursor_loop_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#forall_statement}.
	 * @param ctx the parse tree
	 */
	void enterForall_statement(PlSqlParser.Forall_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#forall_statement}.
	 * @param ctx the parse tree
	 */
	void exitForall_statement(PlSqlParser.Forall_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#bounds_clause}.
	 * @param ctx the parse tree
	 */
	void enterBounds_clause(PlSqlParser.Bounds_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#bounds_clause}.
	 * @param ctx the parse tree
	 */
	void exitBounds_clause(PlSqlParser.Bounds_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#between_bound}.
	 * @param ctx the parse tree
	 */
	void enterBetween_bound(PlSqlParser.Between_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#between_bound}.
	 * @param ctx the parse tree
	 */
	void exitBetween_bound(PlSqlParser.Between_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lower_bound}.
	 * @param ctx the parse tree
	 */
	void enterLower_bound(PlSqlParser.Lower_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lower_bound}.
	 * @param ctx the parse tree
	 */
	void exitLower_bound(PlSqlParser.Lower_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#upper_bound}.
	 * @param ctx the parse tree
	 */
	void enterUpper_bound(PlSqlParser.Upper_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#upper_bound}.
	 * @param ctx the parse tree
	 */
	void exitUpper_bound(PlSqlParser.Upper_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#null_statement}.
	 * @param ctx the parse tree
	 */
	void enterNull_statement(PlSqlParser.Null_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#null_statement}.
	 * @param ctx the parse tree
	 */
	void exitNull_statement(PlSqlParser.Null_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void enterRaise_statement(PlSqlParser.Raise_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void exitRaise_statement(PlSqlParser.Raise_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(PlSqlParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(PlSqlParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void enterCall_statement(PlSqlParser.Call_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void exitCall_statement(PlSqlParser.Call_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 */
	void enterPipe_row_statement(PlSqlParser.Pipe_row_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 */
	void exitPipe_row_statement(PlSqlParser.Pipe_row_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#selection_directive}.
	 * @param ctx the parse tree
	 */
	void enterSelection_directive(PlSqlParser.Selection_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#selection_directive}.
	 * @param ctx the parse tree
	 */
	void exitSelection_directive(PlSqlParser.Selection_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#error_directive}.
	 * @param ctx the parse tree
	 */
	void enterError_directive(PlSqlParser.Error_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#error_directive}.
	 * @param ctx the parse tree
	 */
	void exitError_directive(PlSqlParser.Error_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#selection_directive_body}.
	 * @param ctx the parse tree
	 */
	void enterSelection_directive_body(PlSqlParser.Selection_directive_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#selection_directive_body}.
	 * @param ctx the parse tree
	 */
	void exitSelection_directive_body(PlSqlParser.Selection_directive_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(PlSqlParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(PlSqlParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exception_handler}.
	 * @param ctx the parse tree
	 */
	void enterException_handler(PlSqlParser.Exception_handlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exception_handler}.
	 * @param ctx the parse tree
	 */
	void exitException_handler(PlSqlParser.Exception_handlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trigger_block}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_block(PlSqlParser.Trigger_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trigger_block}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_block(PlSqlParser.Trigger_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tps_block}.
	 * @param ctx the parse tree
	 */
	void enterTps_block(PlSqlParser.Tps_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tps_block}.
	 * @param ctx the parse tree
	 */
	void exitTps_block(PlSqlParser.Tps_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PlSqlParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PlSqlParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_statement(PlSqlParser.Sql_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_statement(PlSqlParser.Sql_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#execute_immediate}.
	 * @param ctx the parse tree
	 */
	void enterExecute_immediate(PlSqlParser.Execute_immediateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#execute_immediate}.
	 * @param ctx the parse tree
	 */
	void exitExecute_immediate(PlSqlParser.Execute_immediateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterDynamic_returning_clause(PlSqlParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitDynamic_returning_clause(PlSqlParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 */
	void enterData_manipulation_language_statements(PlSqlParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 */
	void exitData_manipulation_language_statements(PlSqlParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 */
	void enterCursor_manipulation_statements(PlSqlParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 */
	void exitCursor_manipulation_statements(PlSqlParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#close_statement}.
	 * @param ctx the parse tree
	 */
	void enterClose_statement(PlSqlParser.Close_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#close_statement}.
	 * @param ctx the parse tree
	 */
	void exitClose_statement(PlSqlParser.Close_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#open_statement}.
	 * @param ctx the parse tree
	 */
	void enterOpen_statement(PlSqlParser.Open_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#open_statement}.
	 * @param ctx the parse tree
	 */
	void exitOpen_statement(PlSqlParser.Open_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#fetch_statement}.
	 * @param ctx the parse tree
	 */
	void enterFetch_statement(PlSqlParser.Fetch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#fetch_statement}.
	 * @param ctx the parse tree
	 */
	void exitFetch_statement(PlSqlParser.Fetch_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#variable_or_collection}.
	 * @param ctx the parse tree
	 */
	void enterVariable_or_collection(PlSqlParser.Variable_or_collectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#variable_or_collection}.
	 * @param ctx the parse tree
	 */
	void exitVariable_or_collection(PlSqlParser.Variable_or_collectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#open_for_statement}.
	 * @param ctx the parse tree
	 */
	void enterOpen_for_statement(PlSqlParser.Open_for_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#open_for_statement}.
	 * @param ctx the parse tree
	 */
	void exitOpen_for_statement(PlSqlParser.Open_for_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_control_statements(PlSqlParser.Transaction_control_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_control_statements(PlSqlParser.Transaction_control_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_transaction_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_transaction_command(PlSqlParser.Set_transaction_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_transaction_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_transaction_command(PlSqlParser.Set_transaction_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_constraint_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_constraint_command(PlSqlParser.Set_constraint_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_constraint_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_constraint_command(PlSqlParser.Set_constraint_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#commit_statement}.
	 * @param ctx the parse tree
	 */
	void enterCommit_statement(PlSqlParser.Commit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#commit_statement}.
	 * @param ctx the parse tree
	 */
	void exitCommit_statement(PlSqlParser.Commit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#write_clause}.
	 * @param ctx the parse tree
	 */
	void enterWrite_clause(PlSqlParser.Write_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#write_clause}.
	 * @param ctx the parse tree
	 */
	void exitWrite_clause(PlSqlParser.Write_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rollback_statement}.
	 * @param ctx the parse tree
	 */
	void enterRollback_statement(PlSqlParser.Rollback_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rollback_statement}.
	 * @param ctx the parse tree
	 */
	void exitRollback_statement(PlSqlParser.Rollback_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#savepoint_statement}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_statement(PlSqlParser.Savepoint_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#savepoint_statement}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_statement(PlSqlParser.Savepoint_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collection_method_call}.
	 * @param ctx the parse tree
	 */
	void enterCollection_method_call(PlSqlParser.Collection_method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collection_method_call}.
	 * @param ctx the parse tree
	 */
	void exitCollection_method_call(PlSqlParser.Collection_method_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#explain_statement}.
	 * @param ctx the parse tree
	 */
	void enterExplain_statement(PlSqlParser.Explain_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#explain_statement}.
	 * @param ctx the parse tree
	 */
	void exitExplain_statement(PlSqlParser.Explain_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#select_only_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_only_statement(PlSqlParser.Select_only_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#select_only_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_only_statement(PlSqlParser.Select_only_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_statement(PlSqlParser.Select_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_statement(PlSqlParser.Select_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(PlSqlParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(PlSqlParser.With_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_factoring_clause(PlSqlParser.With_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_factoring_clause(PlSqlParser.With_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_factoring_clause(PlSqlParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_factoring_clause(PlSqlParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#search_clause}.
	 * @param ctx the parse tree
	 */
	void enterSearch_clause(PlSqlParser.Search_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#search_clause}.
	 * @param ctx the parse tree
	 */
	void exitSearch_clause(PlSqlParser.Search_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cycle_clause}.
	 * @param ctx the parse tree
	 */
	void enterCycle_clause(PlSqlParser.Cycle_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cycle_clause}.
	 * @param ctx the parse tree
	 */
	void exitCycle_clause(PlSqlParser.Cycle_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubav_factoring_clause(PlSqlParser.Subav_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubav_factoring_clause(PlSqlParser.Subav_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subav_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubav_clause(PlSqlParser.Subav_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subav_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubav_clause(PlSqlParser.Subav_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchies_clause(PlSqlParser.Hierarchies_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchies_clause(PlSqlParser.Hierarchies_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filter_clauses}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clauses(PlSqlParser.Filter_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filter_clauses}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clauses(PlSqlParser.Filter_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clause(PlSqlParser.Filter_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clause(PlSqlParser.Filter_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_calcs_clause(PlSqlParser.Add_calcs_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_calcs_clause(PlSqlParser.Add_calcs_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_calc_meas_clause(PlSqlParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_calc_meas_clause(PlSqlParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(PlSqlParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(PlSqlParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_basic_elements(PlSqlParser.Subquery_basic_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_basic_elements(PlSqlParser.Subquery_basic_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_operation_part(PlSqlParser.Subquery_operation_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_operation_part(PlSqlParser.Subquery_operation_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void enterUnion_context(PlSqlParser.Union_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#union_context}.
	 * @param ctx the parse tree
	 */
	void exitUnion_context(PlSqlParser.Union_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#query_block}.
	 * @param ctx the parse tree
	 */
	void enterQuery_block(PlSqlParser.Query_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#query_block}.
	 * @param ctx the parse tree
	 */
	void exitQuery_block(PlSqlParser.Query_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#selected_list}.
	 * @param ctx the parse tree
	 */
	void enterSelected_list(PlSqlParser.Selected_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#selected_list}.
	 * @param ctx the parse tree
	 */
	void exitSelected_list(PlSqlParser.Selected_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_clause(PlSqlParser.From_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_clause(PlSqlParser.From_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#select_list_elements}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_elements(PlSqlParser.Select_list_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#select_list_elements}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_elements(PlSqlParser.Select_list_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_ref_list}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_list(PlSqlParser.Table_ref_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_ref_list}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_list(PlSqlParser.Table_ref_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref(PlSqlParser.Table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref(PlSqlParser.Table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_ref_aux}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux(PlSqlParser.Table_ref_auxContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_ref_aux}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux(PlSqlParser.Table_ref_auxContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_one(PlSqlParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_one(PlSqlParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_two(PlSqlParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_two(PlSqlParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_thre(PlSqlParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link PlSqlParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_thre(PlSqlParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_clause(PlSqlParser.Join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_clause(PlSqlParser.Join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void enterJoin_type(PlSqlParser.Join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_type}.
	 * @param ctx the parse tree
	 */
	void exitJoin_type(PlSqlParser.Join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_on_part}.
	 * @param ctx the parse tree
	 */
	void enterJoin_on_part(PlSqlParser.Join_on_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_on_part}.
	 * @param ctx the parse tree
	 */
	void exitJoin_on_part(PlSqlParser.Join_on_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#join_using_part}.
	 * @param ctx the parse tree
	 */
	void enterJoin_using_part(PlSqlParser.Join_using_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#join_using_part}.
	 * @param ctx the parse tree
	 */
	void exitJoin_using_part(PlSqlParser.Join_using_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#outer_join_type}.
	 * @param ctx the parse tree
	 */
	void enterOuter_join_type(PlSqlParser.Outer_join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#outer_join_type}.
	 * @param ctx the parse tree
	 */
	void exitOuter_join_type(PlSqlParser.Outer_join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#query_partition_clause}.
	 * @param ctx the parse tree
	 */
	void enterQuery_partition_clause(PlSqlParser.Query_partition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#query_partition_clause}.
	 * @param ctx the parse tree
	 */
	void exitQuery_partition_clause(PlSqlParser.Query_partition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_query_clause(PlSqlParser.Flashback_query_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_query_clause(PlSqlParser.Flashback_query_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_clause(PlSqlParser.Pivot_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_clause(PlSqlParser.Pivot_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_element}.
	 * @param ctx the parse tree
	 */
	void enterPivot_element(PlSqlParser.Pivot_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_element}.
	 * @param ctx the parse tree
	 */
	void exitPivot_element(PlSqlParser.Pivot_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_for_clause(PlSqlParser.Pivot_for_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_for_clause(PlSqlParser.Pivot_for_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause(PlSqlParser.Pivot_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause(PlSqlParser.Pivot_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause_element(PlSqlParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause_element(PlSqlParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause_elements(PlSqlParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause_elements(PlSqlParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unpivot_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_clause(PlSqlParser.Unpivot_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unpivot_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_clause(PlSqlParser.Unpivot_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_in_clause(PlSqlParser.Unpivot_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_in_clause(PlSqlParser.Unpivot_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_in_elements(PlSqlParser.Unpivot_in_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_in_elements(PlSqlParser.Unpivot_in_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_query_clause(PlSqlParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_query_clause(PlSqlParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#start_part}.
	 * @param ctx the parse tree
	 */
	void enterStart_part(PlSqlParser.Start_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#start_part}.
	 * @param ctx the parse tree
	 */
	void exitStart_part(PlSqlParser.Start_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_clause(PlSqlParser.Group_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_clause(PlSqlParser.Group_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#group_by_elements}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_elements(PlSqlParser.Group_by_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#group_by_elements}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_elements(PlSqlParser.Group_by_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 */
	void enterRollup_cube_clause(PlSqlParser.Rollup_cube_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 */
	void exitRollup_cube_clause(PlSqlParser.Rollup_cube_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_clause(PlSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_clause(PlSqlParser.Grouping_sets_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_elements(PlSqlParser.Grouping_sets_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_elements(PlSqlParser.Grouping_sets_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(PlSqlParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(PlSqlParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_clause(PlSqlParser.Model_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_clause(PlSqlParser.Model_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cell_reference_options}.
	 * @param ctx the parse tree
	 */
	void enterCell_reference_options(PlSqlParser.Cell_reference_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cell_reference_options}.
	 * @param ctx the parse tree
	 */
	void exitCell_reference_options(PlSqlParser.Cell_reference_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#return_rows_clause}.
	 * @param ctx the parse tree
	 */
	void enterReturn_rows_clause(PlSqlParser.Return_rows_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#return_rows_clause}.
	 * @param ctx the parse tree
	 */
	void exitReturn_rows_clause(PlSqlParser.Return_rows_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#reference_model}.
	 * @param ctx the parse tree
	 */
	void enterReference_model(PlSqlParser.Reference_modelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#reference_model}.
	 * @param ctx the parse tree
	 */
	void exitReference_model(PlSqlParser.Reference_modelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#main_model}.
	 * @param ctx the parse tree
	 */
	void enterMain_model(PlSqlParser.Main_modelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#main_model}.
	 * @param ctx the parse tree
	 */
	void exitMain_model(PlSqlParser.Main_modelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_clauses(PlSqlParser.Model_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_clauses(PlSqlParser.Model_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_partition_part(PlSqlParser.Model_column_partition_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_partition_part(PlSqlParser.Model_column_partition_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_column_list}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_list(PlSqlParser.Model_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_column_list}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_list(PlSqlParser.Model_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_column}.
	 * @param ctx the parse tree
	 */
	void enterModel_column(PlSqlParser.Model_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_column}.
	 * @param ctx the parse tree
	 */
	void exitModel_column(PlSqlParser.Model_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_rules_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_clause(PlSqlParser.Model_rules_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_rules_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_clause(PlSqlParser.Model_rules_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_rules_part}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_part(PlSqlParser.Model_rules_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_rules_part}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_part(PlSqlParser.Model_rules_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_rules_element}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_element(PlSqlParser.Model_rules_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_rules_element}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_element(PlSqlParser.Model_rules_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cell_assignment}.
	 * @param ctx the parse tree
	 */
	void enterCell_assignment(PlSqlParser.Cell_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cell_assignment}.
	 * @param ctx the parse tree
	 */
	void exitCell_assignment(PlSqlParser.Cell_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_iterate_clause(PlSqlParser.Model_iterate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_iterate_clause(PlSqlParser.Model_iterate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#until_part}.
	 * @param ctx the parse tree
	 */
	void enterUntil_part(PlSqlParser.Until_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#until_part}.
	 * @param ctx the parse tree
	 */
	void exitUntil_part(PlSqlParser.Until_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_clause(PlSqlParser.Order_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_clause(PlSqlParser.Order_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#order_by_elements}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_elements(PlSqlParser.Order_by_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#order_by_elements}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_elements(PlSqlParser.Order_by_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void enterOffset_clause(PlSqlParser.Offset_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void exitOffset_clause(PlSqlParser.Offset_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#fetch_clause}.
	 * @param ctx the parse tree
	 */
	void enterFetch_clause(PlSqlParser.Fetch_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#fetch_clause}.
	 * @param ctx the parse tree
	 */
	void exitFetch_clause(PlSqlParser.Fetch_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#for_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_clause(PlSqlParser.For_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#for_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_clause(PlSqlParser.For_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#for_update_of_part}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_of_part(PlSqlParser.For_update_of_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#for_update_of_part}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_of_part(PlSqlParser.For_update_of_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#for_update_options}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_options(PlSqlParser.For_update_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#for_update_options}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_options(PlSqlParser.For_update_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_statement(PlSqlParser.Update_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_statement(PlSqlParser.Update_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#update_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_set_clause(PlSqlParser.Update_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#update_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_set_clause(PlSqlParser.Update_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterColumn_based_update_set_clause(PlSqlParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitColumn_based_update_set_clause(PlSqlParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete_statement(PlSqlParser.Delete_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete_statement(PlSqlParser.Delete_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void enterInsert_statement(PlSqlParser.Insert_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void exitInsert_statement(PlSqlParser.Insert_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#single_table_insert}.
	 * @param ctx the parse tree
	 */
	void enterSingle_table_insert(PlSqlParser.Single_table_insertContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#single_table_insert}.
	 * @param ctx the parse tree
	 */
	void exitSingle_table_insert(PlSqlParser.Single_table_insertContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#multi_table_insert}.
	 * @param ctx the parse tree
	 */
	void enterMulti_table_insert(PlSqlParser.Multi_table_insertContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#multi_table_insert}.
	 * @param ctx the parse tree
	 */
	void exitMulti_table_insert(PlSqlParser.Multi_table_insertContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#multi_table_element}.
	 * @param ctx the parse tree
	 */
	void enterMulti_table_element(PlSqlParser.Multi_table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#multi_table_element}.
	 * @param ctx the parse tree
	 */
	void exitMulti_table_element(PlSqlParser.Multi_table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_clause(PlSqlParser.Conditional_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_clause(PlSqlParser.Conditional_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_when_part(PlSqlParser.Conditional_insert_when_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_when_part(PlSqlParser.Conditional_insert_when_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_else_part(PlSqlParser.Conditional_insert_else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_else_part(PlSqlParser.Conditional_insert_else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#insert_into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInsert_into_clause(PlSqlParser.Insert_into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#insert_into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInsert_into_clause(PlSqlParser.Insert_into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void enterValues_clause(PlSqlParser.Values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void exitValues_clause(PlSqlParser.Values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_statement}.
	 * @param ctx the parse tree
	 */
	void enterMerge_statement(PlSqlParser.Merge_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_statement}.
	 * @param ctx the parse tree
	 */
	void exitMerge_statement(PlSqlParser.Merge_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_clause(PlSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_clause(PlSqlParser.Merge_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_element}.
	 * @param ctx the parse tree
	 */
	void enterMerge_element(PlSqlParser.Merge_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_element}.
	 * @param ctx the parse tree
	 */
	void exitMerge_element(PlSqlParser.Merge_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_delete_part(PlSqlParser.Merge_update_delete_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_delete_part(PlSqlParser.Merge_update_delete_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_insert_clause(PlSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_insert_clause(PlSqlParser.Merge_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#selected_tableview}.
	 * @param ctx the parse tree
	 */
	void enterSelected_tableview(PlSqlParser.Selected_tableviewContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#selected_tableview}.
	 * @param ctx the parse tree
	 */
	void exitSelected_tableview(PlSqlParser.Selected_tableviewContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lock_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterLock_table_statement(PlSqlParser.Lock_table_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lock_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitLock_table_statement(PlSqlParser.Lock_table_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 */
	void enterWait_nowait_part(PlSqlParser.Wait_nowait_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 */
	void exitWait_nowait_part(PlSqlParser.Wait_nowait_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lock_table_element}.
	 * @param ctx the parse tree
	 */
	void enterLock_table_element(PlSqlParser.Lock_table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lock_table_element}.
	 * @param ctx the parse tree
	 */
	void exitLock_table_element(PlSqlParser.Lock_table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#lock_mode}.
	 * @param ctx the parse tree
	 */
	void enterLock_mode(PlSqlParser.Lock_modeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#lock_mode}.
	 * @param ctx the parse tree
	 */
	void exitLock_mode(PlSqlParser.Lock_modeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#general_table_ref}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_table_ref(PlSqlParser.General_table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#general_table_ref}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_table_ref(PlSqlParser.General_table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#static_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterStatic_returning_clause(PlSqlParser.Static_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#static_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitStatic_returning_clause(PlSqlParser.Static_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#error_logging_clause}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_clause(PlSqlParser.Error_logging_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#error_logging_clause}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_clause(PlSqlParser.Error_logging_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_into_part(PlSqlParser.Error_logging_into_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_into_part(PlSqlParser.Error_logging_into_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_reject_part(PlSqlParser.Error_logging_reject_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_reject_part(PlSqlParser.Error_logging_reject_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_table_expression_clause(PlSqlParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_table_expression_clause(PlSqlParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_collection_expression}.
	 * @param ctx the parse tree
	 */
	void enterTable_collection_expression(PlSqlParser.Table_collection_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_collection_expression}.
	 * @param ctx the parse tree
	 */
	void exitTable_collection_expression(PlSqlParser.Table_collection_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_restriction_clause(PlSqlParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_restriction_clause(PlSqlParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sample_clause}.
	 * @param ctx the parse tree
	 */
	void enterSample_clause(PlSqlParser.Sample_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sample_clause}.
	 * @param ctx the parse tree
	 */
	void exitSample_clause(PlSqlParser.Sample_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#seed_part}.
	 * @param ctx the parse tree
	 */
	void enterSeed_part(PlSqlParser.Seed_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#seed_part}.
	 * @param ctx the parse tree
	 */
	void exitSeed_part(PlSqlParser.Seed_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(PlSqlParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(PlSqlParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#expressions_}.
	 * @param ctx the parse tree
	 */
	void enterExpressions_(PlSqlParser.Expressions_Context ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#expressions_}.
	 * @param ctx the parse tree
	 */
	void exitExpressions_(PlSqlParser.Expressions_Context ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PlSqlParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PlSqlParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cursor_expression}.
	 * @param ctx the parse tree
	 */
	void enterCursor_expression(PlSqlParser.Cursor_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cursor_expression}.
	 * @param ctx the parse tree
	 */
	void exitCursor_expression(PlSqlParser.Cursor_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogical_expression(PlSqlParser.Logical_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogical_expression(PlSqlParser.Logical_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_logical_expression(PlSqlParser.Unary_logical_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_logical_expression(PlSqlParser.Unary_logical_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 */
	void enterUnary_logical_operation(PlSqlParser.Unary_logical_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 */
	void exitUnary_logical_operation(PlSqlParser.Unary_logical_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void enterLogical_operation(PlSqlParser.Logical_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void exitLogical_operation(PlSqlParser.Logical_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#multiset_expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiset_expression(PlSqlParser.Multiset_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#multiset_expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiset_expression(PlSqlParser.Multiset_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#relational_expression}.
	 * @param ctx the parse tree
	 */
	void enterRelational_expression(PlSqlParser.Relational_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#relational_expression}.
	 * @param ctx the parse tree
	 */
	void exitRelational_expression(PlSqlParser.Relational_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#compound_expression}.
	 * @param ctx the parse tree
	 */
	void enterCompound_expression(PlSqlParser.Compound_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#compound_expression}.
	 * @param ctx the parse tree
	 */
	void exitCompound_expression(PlSqlParser.Compound_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#relational_operator}.
	 * @param ctx the parse tree
	 */
	void enterRelational_operator(PlSqlParser.Relational_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#relational_operator}.
	 * @param ctx the parse tree
	 */
	void exitRelational_operator(PlSqlParser.Relational_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#in_elements}.
	 * @param ctx the parse tree
	 */
	void enterIn_elements(PlSqlParser.In_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#in_elements}.
	 * @param ctx the parse tree
	 */
	void exitIn_elements(PlSqlParser.In_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#between_elements}.
	 * @param ctx the parse tree
	 */
	void enterBetween_elements(PlSqlParser.Between_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#between_elements}.
	 * @param ctx the parse tree
	 */
	void exitBetween_elements(PlSqlParser.Between_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(PlSqlParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(PlSqlParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#interval_expression}.
	 * @param ctx the parse tree
	 */
	void enterInterval_expression(PlSqlParser.Interval_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#interval_expression}.
	 * @param ctx the parse tree
	 */
	void exitInterval_expression(PlSqlParser.Interval_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_expression}.
	 * @param ctx the parse tree
	 */
	void enterModel_expression(PlSqlParser.Model_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_expression}.
	 * @param ctx the parse tree
	 */
	void exitModel_expression(PlSqlParser.Model_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#model_expression_element}.
	 * @param ctx the parse tree
	 */
	void enterModel_expression_element(PlSqlParser.Model_expression_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#model_expression_element}.
	 * @param ctx the parse tree
	 */
	void exitModel_expression_element(PlSqlParser.Model_expression_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void enterSingle_column_for_loop(PlSqlParser.Single_column_for_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void exitSingle_column_for_loop(PlSqlParser.Single_column_for_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void enterMulti_column_for_loop(PlSqlParser.Multi_column_for_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void exitMulti_column_for_loop(PlSqlParser.Multi_column_for_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(PlSqlParser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(PlSqlParser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 */
	void enterImplicit_cursor_expression(PlSqlParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 */
	void exitImplicit_cursor_expression(PlSqlParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collection_expression}.
	 * @param ctx the parse tree
	 */
	void enterCollection_expression(PlSqlParser.Collection_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collection_expression}.
	 * @param ctx the parse tree
	 */
	void exitCollection_expression(PlSqlParser.Collection_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_statement(PlSqlParser.Case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_statement(PlSqlParser.Case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#simple_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterSimple_case_statement(PlSqlParser.Simple_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#simple_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitSimple_case_statement(PlSqlParser.Simple_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#searched_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterSearched_case_statement(PlSqlParser.Searched_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#searched_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitSearched_case_statement(PlSqlParser.Searched_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_when_part_statement(PlSqlParser.Case_when_part_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_when_part_statement(PlSqlParser.Case_when_part_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_else_part_statement(PlSqlParser.Case_else_part_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_else_part_statement(PlSqlParser.Case_else_part_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_expression(PlSqlParser.Case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_expression(PlSqlParser.Case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#simple_case_expression}.
	 * @param ctx the parse tree
	 */
	void enterSimple_case_expression(PlSqlParser.Simple_case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#simple_case_expression}.
	 * @param ctx the parse tree
	 */
	void exitSimple_case_expression(PlSqlParser.Simple_case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#searched_case_expression}.
	 * @param ctx the parse tree
	 */
	void enterSearched_case_expression(PlSqlParser.Searched_case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#searched_case_expression}.
	 * @param ctx the parse tree
	 */
	void exitSearched_case_expression(PlSqlParser.Searched_case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_when_part_expression(PlSqlParser.Case_when_part_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_when_part_expression(PlSqlParser.Case_when_part_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_else_part_expression(PlSqlParser.Case_else_part_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_else_part_expression(PlSqlParser.Case_else_part_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(PlSqlParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(PlSqlParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#quantified_expression}.
	 * @param ctx the parse tree
	 */
	void enterQuantified_expression(PlSqlParser.Quantified_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#quantified_expression}.
	 * @param ctx the parse tree
	 */
	void exitQuantified_expression(PlSqlParser.Quantified_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#string_function}.
	 * @param ctx the parse tree
	 */
	void enterString_function(PlSqlParser.String_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#string_function}.
	 * @param ctx the parse tree
	 */
	void exitString_function(PlSqlParser.String_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#standard_function}.
	 * @param ctx the parse tree
	 */
	void enterStandard_function(PlSqlParser.Standard_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#standard_function}.
	 * @param ctx the parse tree
	 */
	void exitStandard_function(PlSqlParser.Standard_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_function}.
	 * @param ctx the parse tree
	 */
	void enterJson_function(PlSqlParser.Json_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_function}.
	 * @param ctx the parse tree
	 */
	void exitJson_function(PlSqlParser.Json_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_object_content}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_content(PlSqlParser.Json_object_contentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_object_content}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_content(PlSqlParser.Json_object_contentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_object_entry}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_entry(PlSqlParser.Json_object_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_object_entry}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_entry(PlSqlParser.Json_object_entryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_table_clause(PlSqlParser.Json_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_table_clause(PlSqlParser.Json_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_array_element}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_element(PlSqlParser.Json_array_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_array_element}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_element(PlSqlParser.Json_array_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_on_null_clause(PlSqlParser.Json_on_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_on_null_clause(PlSqlParser.Json_on_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_return_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_return_clause(PlSqlParser.Json_return_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_return_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_return_clause(PlSqlParser.Json_return_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_transform_op}.
	 * @param ctx the parse tree
	 */
	void enterJson_transform_op(PlSqlParser.Json_transform_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_transform_op}.
	 * @param ctx the parse tree
	 */
	void exitJson_transform_op(PlSqlParser.Json_transform_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_column_clause(PlSqlParser.Json_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_column_clause(PlSqlParser.Json_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_column_definition}.
	 * @param ctx the parse tree
	 */
	void enterJson_column_definition(PlSqlParser.Json_column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_column_definition}.
	 * @param ctx the parse tree
	 */
	void exitJson_column_definition(PlSqlParser.Json_column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_returning_clause(PlSqlParser.Json_query_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_returning_clause(PlSqlParser.Json_query_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_query_return_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_return_type(PlSqlParser.Json_query_return_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_query_return_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_return_type(PlSqlParser.Json_query_return_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_wrapper_clause(PlSqlParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_wrapper_clause(PlSqlParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_on_error_clause(PlSqlParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_on_error_clause(PlSqlParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_on_empty_clause(PlSqlParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_on_empty_clause(PlSqlParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_return_clause(PlSqlParser.Json_value_return_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_return_clause(PlSqlParser.Json_value_return_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_value_return_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_return_type(PlSqlParser.Json_value_return_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_value_return_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_return_type(PlSqlParser.Json_value_return_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_on_mismatch_clause(PlSqlParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_on_mismatch_clause(PlSqlParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(PlSqlParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(PlSqlParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function_wrapper(PlSqlParser.Numeric_function_wrapperContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function_wrapper(PlSqlParser.Numeric_function_wrapperContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#numeric_function}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function(PlSqlParser.Numeric_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#numeric_function}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function(PlSqlParser.Numeric_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterListagg_overflow_clause(PlSqlParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitListagg_overflow_clause(PlSqlParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#other_function}.
	 * @param ctx the parse tree
	 */
	void enterOther_function(PlSqlParser.Other_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#other_function}.
	 * @param ctx the parse tree
	 */
	void exitOther_function(PlSqlParser.Other_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause_keyword(PlSqlParser.Over_clause_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause_keyword(PlSqlParser.Over_clause_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void enterWithin_or_over_clause_keyword(PlSqlParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void exitWithin_or_over_clause_keyword(PlSqlParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 */
	void enterStandard_prediction_function_keyword(PlSqlParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 */
	void exitStandard_prediction_function_keyword(PlSqlParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause(PlSqlParser.Over_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause(PlSqlParser.Over_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#windowing_clause}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_clause(PlSqlParser.Windowing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#windowing_clause}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_clause(PlSqlParser.Windowing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#windowing_type}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_type(PlSqlParser.Windowing_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#windowing_type}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_type(PlSqlParser.Windowing_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#windowing_elements}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_elements(PlSqlParser.Windowing_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#windowing_elements}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_elements(PlSqlParser.Windowing_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_clause(PlSqlParser.Using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_clause(PlSqlParser.Using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#using_element}.
	 * @param ctx the parse tree
	 */
	void enterUsing_element(PlSqlParser.Using_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#using_element}.
	 * @param ctx the parse tree
	 */
	void exitUsing_element(PlSqlParser.Using_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 */
	void enterCollect_order_by_part(PlSqlParser.Collect_order_by_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 */
	void exitCollect_order_by_part(PlSqlParser.Collect_order_by_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#within_or_over_part}.
	 * @param ctx the parse tree
	 */
	void enterWithin_or_over_part(PlSqlParser.Within_or_over_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#within_or_over_part}.
	 * @param ctx the parse tree
	 */
	void exitWithin_or_over_part(PlSqlParser.Within_or_over_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#string_delimiter}.
	 * @param ctx the parse tree
	 */
	void enterString_delimiter(PlSqlParser.String_delimiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#string_delimiter}.
	 * @param ctx the parse tree
	 */
	void exitString_delimiter(PlSqlParser.String_delimiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 */
	void enterCost_matrix_clause(PlSqlParser.Cost_matrix_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 */
	void exitCost_matrix_clause(PlSqlParser.Cost_matrix_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_passing_clause(PlSqlParser.Xml_passing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_passing_clause(PlSqlParser.Xml_passing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_attributes_clause(PlSqlParser.Xml_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_attributes_clause(PlSqlParser.Xml_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespaces_clause(PlSqlParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespaces_clause(PlSqlParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_table_column}.
	 * @param ctx the parse tree
	 */
	void enterXml_table_column(PlSqlParser.Xml_table_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_table_column}.
	 * @param ctx the parse tree
	 */
	void exitXml_table_column(PlSqlParser.Xml_table_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 */
	void enterXml_general_default_part(PlSqlParser.Xml_general_default_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 */
	void exitXml_general_default_part(PlSqlParser.Xml_general_default_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 */
	void enterXml_multiuse_expression_element(PlSqlParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 */
	void exitXml_multiuse_expression_element(PlSqlParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlroot_param_version_part(PlSqlParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlroot_param_version_part(PlSqlParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlroot_param_standalone_part(PlSqlParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlroot_param_standalone_part(PlSqlParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_enconding_part(PlSqlParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_enconding_part(PlSqlParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_version_part(PlSqlParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_version_part(PlSqlParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_ident_part(PlSqlParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_ident_part(PlSqlParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 */
	void enterSql_plus_command_no_semicolon(PlSqlParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 */
	void exitSql_plus_command_no_semicolon(PlSqlParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sql_plus_command}.
	 * @param ctx the parse tree
	 */
	void enterSql_plus_command(PlSqlParser.Sql_plus_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sql_plus_command}.
	 * @param ctx the parse tree
	 */
	void exitSql_plus_command(PlSqlParser.Sql_plus_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#start_command}.
	 * @param ctx the parse tree
	 */
	void enterStart_command(PlSqlParser.Start_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#start_command}.
	 * @param ctx the parse tree
	 */
	void exitStart_command(PlSqlParser.Start_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#whenever_command}.
	 * @param ctx the parse tree
	 */
	void enterWhenever_command(PlSqlParser.Whenever_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#whenever_command}.
	 * @param ctx the parse tree
	 */
	void exitWhenever_command(PlSqlParser.Whenever_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#set_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_command(PlSqlParser.Set_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#set_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_command(PlSqlParser.Set_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#timing_command}.
	 * @param ctx the parse tree
	 */
	void enterTiming_command(PlSqlParser.Timing_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#timing_command}.
	 * @param ctx the parse tree
	 */
	void exitTiming_command(PlSqlParser.Timing_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extension_clause(PlSqlParser.Partition_extension_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extension_clause(PlSqlParser.Partition_extension_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void enterColumn_alias(PlSqlParser.Column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void exitColumn_alias(PlSqlParser.Column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(PlSqlParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(PlSqlParser.Table_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(PlSqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(PlSqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause(PlSqlParser.Into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause(PlSqlParser.Into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xml_column_name}.
	 * @param ctx the parse tree
	 */
	void enterXml_column_name(PlSqlParser.Xml_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xml_column_name}.
	 * @param ctx the parse tree
	 */
	void exitXml_column_name(PlSqlParser.Xml_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cost_class_name}.
	 * @param ctx the parse tree
	 */
	void enterCost_class_name(PlSqlParser.Cost_class_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cost_class_name}.
	 * @param ctx the parse tree
	 */
	void exitCost_class_name(PlSqlParser.Cost_class_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#attribute_name}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_name(PlSqlParser.Attribute_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#attribute_name}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_name(PlSqlParser.Attribute_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_name(PlSqlParser.Savepoint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_name(PlSqlParser.Savepoint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 */
	void enterRollback_segment_name(PlSqlParser.Rollback_segment_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 */
	void exitRollback_segment_name(PlSqlParser.Rollback_segment_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_var_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_var_name(PlSqlParser.Table_var_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_var_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_var_name(PlSqlParser.Table_var_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void enterSchema_name(PlSqlParser.Schema_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void exitSchema_name(PlSqlParser.Schema_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#routine_name}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_name(PlSqlParser.Routine_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#routine_name}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_name(PlSqlParser.Routine_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#package_name}.
	 * @param ctx the parse tree
	 */
	void enterPackage_name(PlSqlParser.Package_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#package_name}.
	 * @param ctx the parse tree
	 */
	void exitPackage_name(PlSqlParser.Package_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#implementation_type_name}.
	 * @param ctx the parse tree
	 */
	void enterImplementation_type_name(PlSqlParser.Implementation_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#implementation_type_name}.
	 * @param ctx the parse tree
	 */
	void exitImplementation_type_name(PlSqlParser.Implementation_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#parameter_name}.
	 * @param ctx the parse tree
	 */
	void enterParameter_name(PlSqlParser.Parameter_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#parameter_name}.
	 * @param ctx the parse tree
	 */
	void exitParameter_name(PlSqlParser.Parameter_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#reference_model_name}.
	 * @param ctx the parse tree
	 */
	void enterReference_model_name(PlSqlParser.Reference_model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#reference_model_name}.
	 * @param ctx the parse tree
	 */
	void exitReference_model_name(PlSqlParser.Reference_model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#main_model_name}.
	 * @param ctx the parse tree
	 */
	void enterMain_model_name(PlSqlParser.Main_model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#main_model_name}.
	 * @param ctx the parse tree
	 */
	void exitMain_model_name(PlSqlParser.Main_model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#container_tableview_name}.
	 * @param ctx the parse tree
	 */
	void enterContainer_tableview_name(PlSqlParser.Container_tableview_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#container_tableview_name}.
	 * @param ctx the parse tree
	 */
	void exitContainer_tableview_name(PlSqlParser.Container_tableview_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_function_name(PlSqlParser.Aggregate_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_function_name(PlSqlParser.Aggregate_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#query_name}.
	 * @param ctx the parse tree
	 */
	void enterQuery_name(PlSqlParser.Query_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#query_name}.
	 * @param ctx the parse tree
	 */
	void exitQuery_name(PlSqlParser.Query_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#grantee_name}.
	 * @param ctx the parse tree
	 */
	void enterGrantee_name(PlSqlParser.Grantee_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#grantee_name}.
	 * @param ctx the parse tree
	 */
	void exitGrantee_name(PlSqlParser.Grantee_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#role_name}.
	 * @param ctx the parse tree
	 */
	void enterRole_name(PlSqlParser.Role_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#role_name}.
	 * @param ctx the parse tree
	 */
	void exitRole_name(PlSqlParser.Role_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_name(PlSqlParser.Constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_name(PlSqlParser.Constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#label_name}.
	 * @param ctx the parse tree
	 */
	void enterLabel_name(PlSqlParser.Label_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#label_name}.
	 * @param ctx the parse tree
	 */
	void exitLabel_name(PlSqlParser.Label_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_name}.
	 * @param ctx the parse tree
	 */
	void enterType_name(PlSqlParser.Type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_name}.
	 * @param ctx the parse tree
	 */
	void exitType_name(PlSqlParser.Type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void enterSequence_name(PlSqlParser.Sequence_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void exitSequence_name(PlSqlParser.Sequence_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#exception_name}.
	 * @param ctx the parse tree
	 */
	void enterException_name(PlSqlParser.Exception_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#exception_name}.
	 * @param ctx the parse tree
	 */
	void exitException_name(PlSqlParser.Exception_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_name}.
	 * @param ctx the parse tree
	 */
	void enterFunction_name(PlSqlParser.Function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_name}.
	 * @param ctx the parse tree
	 */
	void exitFunction_name(PlSqlParser.Function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#procedure_name}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_name(PlSqlParser.Procedure_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#procedure_name}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_name(PlSqlParser.Procedure_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_name(PlSqlParser.Trigger_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_name(PlSqlParser.Trigger_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#variable_name}.
	 * @param ctx the parse tree
	 */
	void enterVariable_name(PlSqlParser.Variable_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#variable_name}.
	 * @param ctx the parse tree
	 */
	void exitVariable_name(PlSqlParser.Variable_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#index_name}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name(PlSqlParser.Index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#index_name}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name(PlSqlParser.Index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void enterCursor_name(PlSqlParser.Cursor_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void exitCursor_name(PlSqlParser.Cursor_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#record_name}.
	 * @param ctx the parse tree
	 */
	void enterRecord_name(PlSqlParser.Record_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#record_name}.
	 * @param ctx the parse tree
	 */
	void exitRecord_name(PlSqlParser.Record_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#collection_name}.
	 * @param ctx the parse tree
	 */
	void enterCollection_name(PlSqlParser.Collection_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#collection_name}.
	 * @param ctx the parse tree
	 */
	void exitCollection_name(PlSqlParser.Collection_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#link_name}.
	 * @param ctx the parse tree
	 */
	void enterLink_name(PlSqlParser.Link_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#link_name}.
	 * @param ctx the parse tree
	 */
	void exitLink_name(PlSqlParser.Link_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#local_link_name}.
	 * @param ctx the parse tree
	 */
	void enterLocal_link_name(PlSqlParser.Local_link_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#local_link_name}.
	 * @param ctx the parse tree
	 */
	void exitLocal_link_name(PlSqlParser.Local_link_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#connection_qualifier}.
	 * @param ctx the parse tree
	 */
	void enterConnection_qualifier(PlSqlParser.Connection_qualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#connection_qualifier}.
	 * @param ctx the parse tree
	 */
	void exitConnection_qualifier(PlSqlParser.Connection_qualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(PlSqlParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(PlSqlParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#tableview_name}.
	 * @param ctx the parse tree
	 */
	void enterTableview_name(PlSqlParser.Tableview_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#tableview_name}.
	 * @param ctx the parse tree
	 */
	void exitTableview_name(PlSqlParser.Tableview_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void enterXmltable(PlSqlParser.XmltableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void exitXmltable(PlSqlParser.XmltableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#char_set_name}.
	 * @param ctx the parse tree
	 */
	void enterChar_set_name(PlSqlParser.Char_set_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#char_set_name}.
	 * @param ctx the parse tree
	 */
	void exitChar_set_name(PlSqlParser.Char_set_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#synonym_name}.
	 * @param ctx the parse tree
	 */
	void enterSynonym_name(PlSqlParser.Synonym_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#synonym_name}.
	 * @param ctx the parse tree
	 */
	void exitSynonym_name(PlSqlParser.Synonym_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#schema_object_name}.
	 * @param ctx the parse tree
	 */
	void enterSchema_object_name(PlSqlParser.Schema_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#schema_object_name}.
	 * @param ctx the parse tree
	 */
	void exitSchema_object_name(PlSqlParser.Schema_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#dir_object_name}.
	 * @param ctx the parse tree
	 */
	void enterDir_object_name(PlSqlParser.Dir_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#dir_object_name}.
	 * @param ctx the parse tree
	 */
	void exitDir_object_name(PlSqlParser.Dir_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#user_object_name}.
	 * @param ctx the parse tree
	 */
	void enterUser_object_name(PlSqlParser.User_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#user_object_name}.
	 * @param ctx the parse tree
	 */
	void exitUser_object_name(PlSqlParser.User_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#grant_object_name}.
	 * @param ctx the parse tree
	 */
	void enterGrant_object_name(PlSqlParser.Grant_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#grant_object_name}.
	 * @param ctx the parse tree
	 */
	void exitGrant_object_name(PlSqlParser.Grant_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#column_list}.
	 * @param ctx the parse tree
	 */
	void enterColumn_list(PlSqlParser.Column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#column_list}.
	 * @param ctx the parse tree
	 */
	void exitColumn_list(PlSqlParser.Column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_list(PlSqlParser.Insert_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_list(PlSqlParser.Insert_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#paren_column_list}.
	 * @param ctx the parse tree
	 */
	void enterParen_column_list(PlSqlParser.Paren_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#paren_column_list}.
	 * @param ctx the parse tree
	 */
	void exitParen_column_list(PlSqlParser.Paren_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#keep_clause}.
	 * @param ctx the parse tree
	 */
	void enterKeep_clause(PlSqlParser.Keep_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#keep_clause}.
	 * @param ctx the parse tree
	 */
	void exitKeep_clause(PlSqlParser.Keep_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_argument}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument(PlSqlParser.Function_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_argument}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument(PlSqlParser.Function_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument_analytic(PlSqlParser.Function_argument_analyticContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument_analytic(PlSqlParser.Function_argument_analyticContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument_modeling(PlSqlParser.Function_argument_modelingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument_modeling(PlSqlParser.Function_argument_modelingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 */
	void enterRespect_or_ignore_nulls(PlSqlParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 */
	void exitRespect_or_ignore_nulls(PlSqlParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(PlSqlParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(PlSqlParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_spec(PlSqlParser.Type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_spec(PlSqlParser.Type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#custom_type}.
	 * @param ctx the parse tree
	 */
	void enterCustom_type(PlSqlParser.Custom_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#custom_type}.
	 * @param ctx the parse tree
	 */
	void exitCustom_type(PlSqlParser.Custom_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(PlSqlParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(PlSqlParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#interval_type}.
	 * @param ctx the parse tree
	 */
	void enterInterval_type(PlSqlParser.Interval_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#interval_type}.
	 * @param ctx the parse tree
	 */
	void exitInterval_type(PlSqlParser.Interval_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#precision_part}.
	 * @param ctx the parse tree
	 */
	void enterPrecision_part(PlSqlParser.Precision_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#precision_part}.
	 * @param ctx the parse tree
	 */
	void exitPrecision_part(PlSqlParser.Precision_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#native_datatype_element}.
	 * @param ctx the parse tree
	 */
	void enterNative_datatype_element(PlSqlParser.Native_datatype_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#native_datatype_element}.
	 * @param ctx the parse tree
	 */
	void exitNative_datatype_element(PlSqlParser.Native_datatype_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#bind_variable}.
	 * @param ctx the parse tree
	 */
	void enterBind_variable(PlSqlParser.Bind_variableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#bind_variable}.
	 * @param ctx the parse tree
	 */
	void exitBind_variable(PlSqlParser.Bind_variableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void enterColumnref(PlSqlParser.ColumnrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#columnref}.
	 * @param ctx the parse tree
	 */
	void exitColumnref(PlSqlParser.ColumnrefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#general_element}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_element(PlSqlParser.General_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#general_element}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_element(PlSqlParser.General_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#general_element_part}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_element_part(PlSqlParser.General_element_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#general_element_part}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_element_part(PlSqlParser.General_element_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(PlSqlParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(PlSqlParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#keyword_function}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_function(PlSqlParser.Keyword_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#keyword_function}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_function(PlSqlParser.Keyword_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#table_element}.
	 * @param ctx the parse tree
	 */
	void enterTable_element(PlSqlParser.Table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#table_element}.
	 * @param ctx the parse tree
	 */
	void exitTable_element(PlSqlParser.Table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#object_privilege}.
	 * @param ctx the parse tree
	 */
	void enterObject_privilege(PlSqlParser.Object_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#object_privilege}.
	 * @param ctx the parse tree
	 */
	void exitObject_privilege(PlSqlParser.Object_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#system_privilege}.
	 * @param ctx the parse tree
	 */
	void enterSystem_privilege(PlSqlParser.System_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#system_privilege}.
	 * @param ctx the parse tree
	 */
	void exitSystem_privilege(PlSqlParser.System_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(PlSqlParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(PlSqlParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void enterNumeric(PlSqlParser.NumericContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#numeric}.
	 * @param ctx the parse tree
	 */
	void exitNumeric(PlSqlParser.NumericContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#numeric_negative}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_negative(PlSqlParser.Numeric_negativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#numeric_negative}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_negative(PlSqlParser.Numeric_negativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#quoted_string}.
	 * @param ctx the parse tree
	 */
	void enterQuoted_string(PlSqlParser.Quoted_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#quoted_string}.
	 * @param ctx the parse tree
	 */
	void exitQuoted_string(PlSqlParser.Quoted_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(PlSqlParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(PlSqlParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#id_expression}.
	 * @param ctx the parse tree
	 */
	void enterId_expression(PlSqlParser.Id_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#id_expression}.
	 * @param ctx the parse tree
	 */
	void exitId_expression(PlSqlParser.Id_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#inquiry_directive}.
	 * @param ctx the parse tree
	 */
	void enterInquiry_directive(PlSqlParser.Inquiry_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#inquiry_directive}.
	 * @param ctx the parse tree
	 */
	void exitInquiry_directive(PlSqlParser.Inquiry_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#outer_join_sign}.
	 * @param ctx the parse tree
	 */
	void enterOuter_join_sign(PlSqlParser.Outer_join_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#outer_join_sign}.
	 * @param ctx the parse tree
	 */
	void exitOuter_join_sign(PlSqlParser.Outer_join_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void enterRegular_id(PlSqlParser.Regular_idContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void exitRegular_id(PlSqlParser.Regular_idContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_in_18c(PlSqlParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_in_18c(PlSqlParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_in_12c(PlSqlParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_in_12c(PlSqlParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_pre12c(PlSqlParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_pre12c(PlSqlParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#string_function_name}.
	 * @param ctx the parse tree
	 */
	void enterString_function_name(PlSqlParser.String_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#string_function_name}.
	 * @param ctx the parse tree
	 */
	void exitString_function_name(PlSqlParser.String_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlSqlParser#numeric_function_name}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function_name(PlSqlParser.Numeric_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlSqlParser#numeric_function_name}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function_name(PlSqlParser.Numeric_function_nameContext ctx);
}