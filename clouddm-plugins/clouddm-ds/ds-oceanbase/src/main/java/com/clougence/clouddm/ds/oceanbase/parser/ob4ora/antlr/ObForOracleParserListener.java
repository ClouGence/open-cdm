// Generated from /home/zyc/project/dm/open-cdm/clouddm-plugins/clouddm-ds/ds-oceanbase/src/main/antlr/ObForOracleParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.oceanbase.parser.ob4ora.antlr;

    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;


    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ObForOracleParser}.
 */
public interface ObForOracleParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterPhysical_attributes_clause(ObForOracleParser.Physical_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#physical_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitPhysical_attributes_clause(ObForOracleParser.Physical_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_script}.
	 * @param ctx the parse tree
	 */
	void enterSql_script(ObForOracleParser.Sql_scriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_script}.
	 * @param ctx the parse tree
	 */
	void exitSql_script(ObForOracleParser.Sql_scriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unit_statement}.
	 * @param ctx the parse tree
	 */
	void enterUnit_statement(ObForOracleParser.Unit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unit_statement}.
	 * @param ctx the parse tree
	 */
	void exitUnit_statement(ObForOracleParser.Unit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterAlter_diskgroup(ObForOracleParser.Alter_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitAlter_diskgroup(ObForOracleParser.Alter_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_disk_clause(ObForOracleParser.Add_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_disk_clause(ObForOracleParser.Add_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_disk_clause(ObForOracleParser.Drop_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_disk_clause(ObForOracleParser.Drop_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterResize_disk_clause(ObForOracleParser.Resize_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#resize_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitResize_disk_clause(ObForOracleParser.Resize_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterReplace_disk_clause(ObForOracleParser.Replace_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#replace_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitReplace_disk_clause(ObForOracleParser.Replace_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#wait_nowait}.
	 * @param ctx the parse tree
	 */
	void enterWait_nowait(ObForOracleParser.Wait_nowaitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#wait_nowait}.
	 * @param ctx the parse tree
	 */
	void exitWait_nowait(ObForOracleParser.Wait_nowaitContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterRename_disk_clause(ObForOracleParser.Rename_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rename_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitRename_disk_clause(ObForOracleParser.Rename_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disk_online_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_online_clause(ObForOracleParser.Disk_online_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disk_online_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_online_clause(ObForOracleParser.Disk_online_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_offline_clause(ObForOracleParser.Disk_offline_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disk_offline_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_offline_clause(ObForOracleParser.Disk_offline_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#timeout_clause}.
	 * @param ctx the parse tree
	 */
	void enterTimeout_clause(ObForOracleParser.Timeout_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#timeout_clause}.
	 * @param ctx the parse tree
	 */
	void exitTimeout_clause(ObForOracleParser.Timeout_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterRebalance_diskgroup_clause(ObForOracleParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rebalance_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitRebalance_diskgroup_clause(ObForOracleParser.Rebalance_diskgroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#phase}.
	 * @param ctx the parse tree
	 */
	void enterPhase(ObForOracleParser.PhaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#phase}.
	 * @param ctx the parse tree
	 */
	void exitPhase(ObForOracleParser.PhaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterCheck_diskgroup_clause(ObForOracleParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#check_diskgroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitCheck_diskgroup_clause(ObForOracleParser.Check_diskgroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_template_clauses(ObForOracleParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_template_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_template_clauses(ObForOracleParser.Diskgroup_template_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 */
	void enterQualified_template_clause(ObForOracleParser.Qualified_template_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#qualified_template_clause}.
	 * @param ctx the parse tree
	 */
	void exitQualified_template_clause(ObForOracleParser.Qualified_template_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void enterRedundancy_clause(ObForOracleParser.Redundancy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void exitRedundancy_clause(ObForOracleParser.Redundancy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#striping_clause}.
	 * @param ctx the parse tree
	 */
	void enterStriping_clause(ObForOracleParser.Striping_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#striping_clause}.
	 * @param ctx the parse tree
	 */
	void exitStriping_clause(ObForOracleParser.Striping_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#force_noforce}.
	 * @param ctx the parse tree
	 */
	void enterForce_noforce(ObForOracleParser.Force_noforceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#force_noforce}.
	 * @param ctx the parse tree
	 */
	void exitForce_noforce(ObForOracleParser.Force_noforceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_directory_clauses(ObForOracleParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_directory_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_directory_clauses(ObForOracleParser.Diskgroup_directory_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dir_name}.
	 * @param ctx the parse tree
	 */
	void enterDir_name(ObForOracleParser.Dir_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dir_name}.
	 * @param ctx the parse tree
	 */
	void exitDir_name(ObForOracleParser.Dir_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_alias_clauses(ObForOracleParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_alias_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_alias_clauses(ObForOracleParser.Diskgroup_alias_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_volume_clauses(ObForOracleParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_volume_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_volume_clauses(ObForOracleParser.Diskgroup_volume_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_volume_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_volume_clause(ObForOracleParser.Add_volume_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_volume_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_volume_clause(ObForOracleParser.Add_volume_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_volume_clause(ObForOracleParser.Modify_volume_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_volume_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_volume_clause(ObForOracleParser.Modify_volume_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_attributes(ObForOracleParser.Diskgroup_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_attributes}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_attributes(ObForOracleParser.Diskgroup_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 */
	void enterModify_diskgroup_file(ObForOracleParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_diskgroup_file}.
	 * @param ctx the parse tree
	 */
	void exitModify_diskgroup_file(ObForOracleParser.Modify_diskgroup_fileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disk_region_clause}.
	 * @param ctx the parse tree
	 */
	void enterDisk_region_clause(ObForOracleParser.Disk_region_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disk_region_clause}.
	 * @param ctx the parse tree
	 */
	void exitDisk_region_clause(ObForOracleParser.Disk_region_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_diskgroup_file_clause(ObForOracleParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_diskgroup_file_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_diskgroup_file_clause(ObForOracleParser.Drop_diskgroup_file_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void enterConvert_redundancy_clause(ObForOracleParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#convert_redundancy_clause}.
	 * @param ctx the parse tree
	 */
	void exitConvert_redundancy_clause(ObForOracleParser.Convert_redundancy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUsergroup_clauses(ObForOracleParser.Usergroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#usergroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUsergroup_clauses(ObForOracleParser.Usergroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUser_clauses(ObForOracleParser.User_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUser_clauses(ObForOracleParser.User_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 */
	void enterFile_permissions_clause(ObForOracleParser.File_permissions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#file_permissions_clause}.
	 * @param ctx the parse tree
	 */
	void exitFile_permissions_clause(ObForOracleParser.File_permissions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#file_owner_clause}.
	 * @param ctx the parse tree
	 */
	void enterFile_owner_clause(ObForOracleParser.File_owner_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#file_owner_clause}.
	 * @param ctx the parse tree
	 */
	void exitFile_owner_clause(ObForOracleParser.File_owner_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#scrub_clause}.
	 * @param ctx the parse tree
	 */
	void enterScrub_clause(ObForOracleParser.Scrub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#scrub_clause}.
	 * @param ctx the parse tree
	 */
	void exitScrub_clause(ObForOracleParser.Scrub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterQuotagroup_clauses(ObForOracleParser.Quotagroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#quotagroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitQuotagroup_clauses(ObForOracleParser.Quotagroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#property_name}.
	 * @param ctx the parse tree
	 */
	void enterProperty_name(ObForOracleParser.Property_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#property_name}.
	 * @param ctx the parse tree
	 */
	void exitProperty_name(ObForOracleParser.Property_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#property_value}.
	 * @param ctx the parse tree
	 */
	void enterProperty_value(ObForOracleParser.Property_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#property_value}.
	 * @param ctx the parse tree
	 */
	void exitProperty_value(ObForOracleParser.Property_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterFilegroup_clauses(ObForOracleParser.Filegroup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filegroup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitFilegroup_clauses(ObForOracleParser.Filegroup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_filegroup_clause(ObForOracleParser.Add_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_filegroup_clause(ObForOracleParser.Add_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_filegroup_clause(ObForOracleParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_filegroup_clause(ObForOracleParser.Modify_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_to_filegroup_clause(ObForOracleParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#move_to_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_to_filegroup_clause(ObForOracleParser.Move_to_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_filegroup_clause(ObForOracleParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_filegroup_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_filegroup_clause(ObForOracleParser.Drop_filegroup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#quorum_regular}.
	 * @param ctx the parse tree
	 */
	void enterQuorum_regular(ObForOracleParser.Quorum_regularContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#quorum_regular}.
	 * @param ctx the parse tree
	 */
	void exitQuorum_regular(ObForOracleParser.Quorum_regularContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndrop_disk_clause(ObForOracleParser.Undrop_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#undrop_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndrop_disk_clause(ObForOracleParser.Undrop_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 */
	void enterDiskgroup_availability(ObForOracleParser.Diskgroup_availabilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#diskgroup_availability}.
	 * @param ctx the parse tree
	 */
	void exitDiskgroup_availability(ObForOracleParser.Diskgroup_availabilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 */
	void enterEnable_disable_volume(ObForOracleParser.Enable_disable_volumeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#enable_disable_volume}.
	 * @param ctx the parse tree
	 */
	void exitEnable_disable_volume(ObForOracleParser.Enable_disable_volumeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_function}.
	 * @param ctx the parse tree
	 */
	void enterDrop_function(ObForOracleParser.Drop_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_function}.
	 * @param ctx the parse tree
	 */
	void exitDrop_function(ObForOracleParser.Drop_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterAlter_flashback_archive(ObForOracleParser.Alter_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitAlter_flashback_archive(ObForOracleParser.Alter_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterAlter_hierarchy(ObForOracleParser.Alter_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitAlter_hierarchy(ObForOracleParser.Alter_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_function}.
	 * @param ctx the parse tree
	 */
	void enterAlter_function(ObForOracleParser.Alter_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_function}.
	 * @param ctx the parse tree
	 */
	void exitAlter_function(ObForOracleParser.Alter_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_java}.
	 * @param ctx the parse tree
	 */
	void enterAlter_java(ObForOracleParser.Alter_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_java}.
	 * @param ctx the parse tree
	 */
	void exitAlter_java(ObForOracleParser.Alter_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#match_string}.
	 * @param ctx the parse tree
	 */
	void enterMatch_string(ObForOracleParser.Match_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#match_string}.
	 * @param ctx the parse tree
	 */
	void exitMatch_string(ObForOracleParser.Match_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_function_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_function_body(ObForOracleParser.Create_function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_function_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_function_body(ObForOracleParser.Create_function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_macro_body}.
	 * @param ctx the parse tree
	 */
	void enterSql_macro_body(ObForOracleParser.Sql_macro_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_macro_body}.
	 * @param ctx the parse tree
	 */
	void exitSql_macro_body(ObForOracleParser.Sql_macro_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 */
	void enterParallel_enable_clause(ObForOracleParser.Parallel_enable_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parallel_enable_clause}.
	 * @param ctx the parse tree
	 */
	void exitParallel_enable_clause(ObForOracleParser.Parallel_enable_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_by_clause(ObForOracleParser.Partition_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_by_clause(ObForOracleParser.Partition_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#result_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterResult_cache_clause(ObForOracleParser.Result_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#result_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitResult_cache_clause(ObForOracleParser.Result_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterAccessible_by_clause(ObForOracleParser.Accessible_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#accessible_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitAccessible_by_clause(ObForOracleParser.Accessible_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_collation_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_collation_clause(ObForOracleParser.Default_collation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_collation_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_collation_clause(ObForOracleParser.Default_collation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_clause(ObForOracleParser.Aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_clause(ObForOracleParser.Aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterPipelined_using_clause(ObForOracleParser.Pipelined_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pipelined_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitPipelined_using_clause(ObForOracleParser.Pipelined_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#accessor}.
	 * @param ctx the parse tree
	 */
	void enterAccessor(ObForOracleParser.AccessorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#accessor}.
	 * @param ctx the parse tree
	 */
	void exitAccessor(ObForOracleParser.AccessorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#relies_on_part}.
	 * @param ctx the parse tree
	 */
	void enterRelies_on_part(ObForOracleParser.Relies_on_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#relies_on_part}.
	 * @param ctx the parse tree
	 */
	void exitRelies_on_part(ObForOracleParser.Relies_on_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#streaming_clause}.
	 * @param ctx the parse tree
	 */
	void enterStreaming_clause(ObForOracleParser.Streaming_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#streaming_clause}.
	 * @param ctx the parse tree
	 */
	void exitStreaming_clause(ObForOracleParser.Streaming_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_outline}.
	 * @param ctx the parse tree
	 */
	void enterAlter_outline(ObForOracleParser.Alter_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_outline}.
	 * @param ctx the parse tree
	 */
	void exitAlter_outline(ObForOracleParser.Alter_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#outline_options}.
	 * @param ctx the parse tree
	 */
	void enterOutline_options(ObForOracleParser.Outline_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#outline_options}.
	 * @param ctx the parse tree
	 */
	void exitOutline_options(ObForOracleParser.Outline_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterAlter_lockdown_profile(ObForOracleParser.Alter_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitAlter_lockdown_profile(ObForOracleParser.Alter_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lockdown_feature}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_feature(ObForOracleParser.Lockdown_featureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lockdown_feature}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_feature(ObForOracleParser.Lockdown_featureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lockdown_options}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_options(ObForOracleParser.Lockdown_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lockdown_options}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_options(ObForOracleParser.Lockdown_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lockdown_statements}.
	 * @param ctx the parse tree
	 */
	void enterLockdown_statements(ObForOracleParser.Lockdown_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lockdown_statements}.
	 * @param ctx the parse tree
	 */
	void exitLockdown_statements(ObForOracleParser.Lockdown_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#statement_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStatement_clauses(ObForOracleParser.Statement_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#statement_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStatement_clauses(ObForOracleParser.Statement_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#clause_options}.
	 * @param ctx the parse tree
	 */
	void enterClause_options(ObForOracleParser.Clause_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#clause_options}.
	 * @param ctx the parse tree
	 */
	void exitClause_options(ObForOracleParser.Clause_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#option_values}.
	 * @param ctx the parse tree
	 */
	void enterOption_values(ObForOracleParser.Option_valuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#option_values}.
	 * @param ctx the parse tree
	 */
	void exitOption_values(ObForOracleParser.Option_valuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#string_list}.
	 * @param ctx the parse tree
	 */
	void enterString_list(ObForOracleParser.String_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#string_list}.
	 * @param ctx the parse tree
	 */
	void exitString_list(ObForOracleParser.String_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disable_enable}.
	 * @param ctx the parse tree
	 */
	void enterDisable_enable(ObForOracleParser.Disable_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disable_enable}.
	 * @param ctx the parse tree
	 */
	void exitDisable_enable(ObForOracleParser.Disable_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterDrop_lockdown_profile(ObForOracleParser.Drop_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitDrop_lockdown_profile(ObForOracleParser.Drop_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_package}.
	 * @param ctx the parse tree
	 */
	void enterDrop_package(ObForOracleParser.Drop_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_package}.
	 * @param ctx the parse tree
	 */
	void exitDrop_package(ObForOracleParser.Drop_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_package}.
	 * @param ctx the parse tree
	 */
	void enterAlter_package(ObForOracleParser.Alter_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_package}.
	 * @param ctx the parse tree
	 */
	void exitAlter_package(ObForOracleParser.Alter_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_package}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package(ObForOracleParser.Create_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_package}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package(ObForOracleParser.Create_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_package_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_package_body(ObForOracleParser.Create_package_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_package_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_package_body(ObForOracleParser.Create_package_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#package_obj_spec}.
	 * @param ctx the parse tree
	 */
	void enterPackage_obj_spec(ObForOracleParser.Package_obj_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#package_obj_spec}.
	 * @param ctx the parse tree
	 */
	void exitPackage_obj_spec(ObForOracleParser.Package_obj_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#procedure_spec}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_spec(ObForOracleParser.Procedure_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#procedure_spec}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_spec(ObForOracleParser.Procedure_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_spec}.
	 * @param ctx the parse tree
	 */
	void enterFunction_spec(ObForOracleParser.Function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_spec}.
	 * @param ctx the parse tree
	 */
	void exitFunction_spec(ObForOracleParser.Function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#package_obj_body}.
	 * @param ctx the parse tree
	 */
	void enterPackage_obj_body(ObForOracleParser.Package_obj_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#package_obj_body}.
	 * @param ctx the parse tree
	 */
	void exitPackage_obj_body(ObForOracleParser.Package_obj_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterAlter_pmem_filestore(ObForOracleParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitAlter_pmem_filestore(ObForOracleParser.Alter_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterDrop_pmem_filestore(ObForOracleParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitDrop_pmem_filestore(ObForOracleParser.Drop_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_procedure}.
	 * @param ctx the parse tree
	 */
	void enterDrop_procedure(ObForOracleParser.Drop_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_procedure}.
	 * @param ctx the parse tree
	 */
	void exitDrop_procedure(ObForOracleParser.Drop_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_procedure}.
	 * @param ctx the parse tree
	 */
	void enterAlter_procedure(ObForOracleParser.Alter_procedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_procedure}.
	 * @param ctx the parse tree
	 */
	void exitAlter_procedure(ObForOracleParser.Alter_procedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_body}.
	 * @param ctx the parse tree
	 */
	void enterFunction_body(ObForOracleParser.Function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_body}.
	 * @param ctx the parse tree
	 */
	void exitFunction_body(ObForOracleParser.Function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#procedure_body}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_body(ObForOracleParser.Procedure_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#procedure_body}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_body(ObForOracleParser.Procedure_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_procedure_body}.
	 * @param ctx the parse tree
	 */
	void enterCreate_procedure_body(ObForOracleParser.Create_procedure_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_procedure_body}.
	 * @param ctx the parse tree
	 */
	void exitCreate_procedure_body(ObForOracleParser.Create_procedure_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 */
	void enterAlter_resource_cost(ObForOracleParser.Alter_resource_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_resource_cost}.
	 * @param ctx the parse tree
	 */
	void exitAlter_resource_cost(ObForOracleParser.Alter_resource_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_outline}.
	 * @param ctx the parse tree
	 */
	void enterDrop_outline(ObForOracleParser.Drop_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_outline}.
	 * @param ctx the parse tree
	 */
	void exitDrop_outline(ObForOracleParser.Drop_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterAlter_rollback_segment(ObForOracleParser.Alter_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitAlter_rollback_segment(ObForOracleParser.Alter_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_restore_point}.
	 * @param ctx the parse tree
	 */
	void enterDrop_restore_point(ObForOracleParser.Drop_restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_restore_point}.
	 * @param ctx the parse tree
	 */
	void exitDrop_restore_point(ObForOracleParser.Drop_restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterDrop_rollback_segment(ObForOracleParser.Drop_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitDrop_rollback_segment(ObForOracleParser.Drop_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_role}.
	 * @param ctx the parse tree
	 */
	void enterDrop_role(ObForOracleParser.Drop_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_role}.
	 * @param ctx the parse tree
	 */
	void exitDrop_role(ObForOracleParser.Drop_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void enterCreate_pmem_filestore(ObForOracleParser.Create_pmem_filestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_pmem_filestore}.
	 * @param ctx the parse tree
	 */
	void exitCreate_pmem_filestore(ObForOracleParser.Create_pmem_filestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 */
	void enterPmem_filestore_options(ObForOracleParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pmem_filestore_options}.
	 * @param ctx the parse tree
	 */
	void exitPmem_filestore_options(ObForOracleParser.Pmem_filestore_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#file_path}.
	 * @param ctx the parse tree
	 */
	void enterFile_path(ObForOracleParser.File_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#file_path}.
	 * @param ctx the parse tree
	 */
	void exitFile_path(ObForOracleParser.File_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterCreate_rollback_segment(ObForOracleParser.Create_rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitCreate_rollback_segment(ObForOracleParser.Create_rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_trigger}.
	 * @param ctx the parse tree
	 */
	void enterDrop_trigger(ObForOracleParser.Drop_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_trigger}.
	 * @param ctx the parse tree
	 */
	void exitDrop_trigger(ObForOracleParser.Drop_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_trigger}.
	 * @param ctx the parse tree
	 */
	void enterAlter_trigger(ObForOracleParser.Alter_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_trigger}.
	 * @param ctx the parse tree
	 */
	void exitAlter_trigger(ObForOracleParser.Alter_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_trigger}.
	 * @param ctx the parse tree
	 */
	void enterCreate_trigger(ObForOracleParser.Create_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_trigger}.
	 * @param ctx the parse tree
	 */
	void exitCreate_trigger(ObForOracleParser.Create_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_follows_clause(ObForOracleParser.Trigger_follows_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trigger_follows_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_follows_clause(ObForOracleParser.Trigger_follows_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_when_clause(ObForOracleParser.Trigger_when_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trigger_when_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_when_clause(ObForOracleParser.Trigger_when_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterSimple_dml_trigger(ObForOracleParser.Simple_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#simple_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitSimple_dml_trigger(ObForOracleParser.Simple_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#for_each_row}.
	 * @param ctx the parse tree
	 */
	void enterFor_each_row(ObForOracleParser.For_each_rowContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#for_each_row}.
	 * @param ctx the parse tree
	 */
	void exitFor_each_row(ObForOracleParser.For_each_rowContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterCompound_dml_trigger(ObForOracleParser.Compound_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compound_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitCompound_dml_trigger(ObForOracleParser.Compound_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void enterNon_dml_trigger(ObForOracleParser.Non_dml_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#non_dml_trigger}.
	 * @param ctx the parse tree
	 */
	void exitNon_dml_trigger(ObForOracleParser.Non_dml_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trigger_body}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_body(ObForOracleParser.Trigger_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trigger_body}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_body(ObForOracleParser.Trigger_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#routine_clause}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_clause(ObForOracleParser.Routine_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#routine_clause}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_clause(ObForOracleParser.Routine_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 */
	void enterCompound_trigger_block(ObForOracleParser.Compound_trigger_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compound_trigger_block}.
	 * @param ctx the parse tree
	 */
	void exitCompound_trigger_block(ObForOracleParser.Compound_trigger_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#timing_point_section}.
	 * @param ctx the parse tree
	 */
	void enterTiming_point_section(ObForOracleParser.Timing_point_sectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#timing_point_section}.
	 * @param ctx the parse tree
	 */
	void exitTiming_point_section(ObForOracleParser.Timing_point_sectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#non_dml_event}.
	 * @param ctx the parse tree
	 */
	void enterNon_dml_event(ObForOracleParser.Non_dml_eventContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#non_dml_event}.
	 * @param ctx the parse tree
	 */
	void exitNon_dml_event(ObForOracleParser.Non_dml_eventContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dml_event_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_clause(ObForOracleParser.Dml_event_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dml_event_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_clause(ObForOracleParser.Dml_event_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dml_event_element}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_element(ObForOracleParser.Dml_event_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dml_event_element}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_element(ObForOracleParser.Dml_event_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_event_nested_clause(ObForOracleParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dml_event_nested_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_event_nested_clause(ObForOracleParser.Dml_event_nested_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#referencing_clause}.
	 * @param ctx the parse tree
	 */
	void enterReferencing_clause(ObForOracleParser.Referencing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#referencing_clause}.
	 * @param ctx the parse tree
	 */
	void exitReferencing_clause(ObForOracleParser.Referencing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#referencing_element}.
	 * @param ctx the parse tree
	 */
	void enterReferencing_element(ObForOracleParser.Referencing_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#referencing_element}.
	 * @param ctx the parse tree
	 */
	void exitReferencing_element(ObForOracleParser.Referencing_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_type}.
	 * @param ctx the parse tree
	 */
	void enterDrop_type(ObForOracleParser.Drop_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_type}.
	 * @param ctx the parse tree
	 */
	void exitDrop_type(ObForOracleParser.Drop_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_type}.
	 * @param ctx the parse tree
	 */
	void enterAlter_type(ObForOracleParser.Alter_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_type}.
	 * @param ctx the parse tree
	 */
	void exitAlter_type(ObForOracleParser.Alter_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compile_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterCompile_type_clause(ObForOracleParser.Compile_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compile_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitCompile_type_clause(ObForOracleParser.Compile_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#replace_type_clause}.
	 * @param ctx the parse tree
	 */
	void enterReplace_type_clause(ObForOracleParser.Replace_type_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#replace_type_clause}.
	 * @param ctx the parse tree
	 */
	void exitReplace_type_clause(ObForOracleParser.Replace_type_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_method_spec}.
	 * @param ctx the parse tree
	 */
	void enterAlter_method_spec(ObForOracleParser.Alter_method_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_method_spec}.
	 * @param ctx the parse tree
	 */
	void exitAlter_method_spec(ObForOracleParser.Alter_method_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_method_element}.
	 * @param ctx the parse tree
	 */
	void enterAlter_method_element(ObForOracleParser.Alter_method_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_method_element}.
	 * @param ctx the parse tree
	 */
	void exitAlter_method_element(ObForOracleParser.Alter_method_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 */
	void enterAlter_attribute_definition(ObForOracleParser.Alter_attribute_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_attribute_definition}.
	 * @param ctx the parse tree
	 */
	void exitAlter_attribute_definition(ObForOracleParser.Alter_attribute_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#attribute_definition}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_definition(ObForOracleParser.Attribute_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#attribute_definition}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_definition(ObForOracleParser.Attribute_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAlter_collection_clauses(ObForOracleParser.Alter_collection_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_collection_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAlter_collection_clauses(ObForOracleParser.Alter_collection_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 */
	void enterDependent_handling_clause(ObForOracleParser.Dependent_handling_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dependent_handling_clause}.
	 * @param ctx the parse tree
	 */
	void exitDependent_handling_clause(ObForOracleParser.Dependent_handling_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 */
	void enterDependent_exceptions_part(ObForOracleParser.Dependent_exceptions_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dependent_exceptions_part}.
	 * @param ctx the parse tree
	 */
	void exitDependent_exceptions_part(ObForOracleParser.Dependent_exceptions_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_type}.
	 * @param ctx the parse tree
	 */
	void enterCreate_type(ObForOracleParser.Create_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_type}.
	 * @param ctx the parse tree
	 */
	void exitCreate_type(ObForOracleParser.Create_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_definition}.
	 * @param ctx the parse tree
	 */
	void enterType_definition(ObForOracleParser.Type_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_definition}.
	 * @param ctx the parse tree
	 */
	void exitType_definition(ObForOracleParser.Type_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_type_def}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_def(ObForOracleParser.Object_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_type_def}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_def(ObForOracleParser.Object_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_as_part}.
	 * @param ctx the parse tree
	 */
	void enterObject_as_part(ObForOracleParser.Object_as_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_as_part}.
	 * @param ctx the parse tree
	 */
	void exitObject_as_part(ObForOracleParser.Object_as_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_under_part}.
	 * @param ctx the parse tree
	 */
	void enterObject_under_part(ObForOracleParser.Object_under_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_under_part}.
	 * @param ctx the parse tree
	 */
	void exitObject_under_part(ObForOracleParser.Object_under_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 */
	void enterNested_table_type_def(ObForOracleParser.Nested_table_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#nested_table_type_def}.
	 * @param ctx the parse tree
	 */
	void exitNested_table_type_def(ObForOracleParser.Nested_table_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 */
	void enterSqlj_object_type(ObForOracleParser.Sqlj_object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sqlj_object_type}.
	 * @param ctx the parse tree
	 */
	void exitSqlj_object_type(ObForOracleParser.Sqlj_object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_body}.
	 * @param ctx the parse tree
	 */
	void enterType_body(ObForOracleParser.Type_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_body}.
	 * @param ctx the parse tree
	 */
	void exitType_body(ObForOracleParser.Type_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_body_elements}.
	 * @param ctx the parse tree
	 */
	void enterType_body_elements(ObForOracleParser.Type_body_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_body_elements}.
	 * @param ctx the parse tree
	 */
	void exitType_body_elements(ObForOracleParser.Type_body_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMap_order_func_declaration(ObForOracleParser.Map_order_func_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#map_order_func_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMap_order_func_declaration(ObForOracleParser.Map_order_func_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterSubprog_decl_in_type(ObForOracleParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subprog_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitSubprog_decl_in_type(ObForOracleParser.Subprog_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterProc_decl_in_type(ObForOracleParser.Proc_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#proc_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitProc_decl_in_type(ObForOracleParser.Proc_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void enterFunc_decl_in_type(ObForOracleParser.Func_decl_in_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#func_decl_in_type}.
	 * @param ctx the parse tree
	 */
	void exitFunc_decl_in_type(ObForOracleParser.Func_decl_in_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constructor_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_declaration(ObForOracleParser.Constructor_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constructor_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_declaration(ObForOracleParser.Constructor_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modifier_clause}.
	 * @param ctx the parse tree
	 */
	void enterModifier_clause(ObForOracleParser.Modifier_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modifier_clause}.
	 * @param ctx the parse tree
	 */
	void exitModifier_clause(ObForOracleParser.Modifier_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_member_spec}.
	 * @param ctx the parse tree
	 */
	void enterObject_member_spec(ObForOracleParser.Object_member_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_member_spec}.
	 * @param ctx the parse tree
	 */
	void exitObject_member_spec(ObForOracleParser.Object_member_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 */
	void enterSqlj_object_type_attr(ObForOracleParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sqlj_object_type_attr}.
	 * @param ctx the parse tree
	 */
	void exitSqlj_object_type_attr(ObForOracleParser.Sqlj_object_type_attrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void enterElement_spec(ObForOracleParser.Element_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void exitElement_spec(ObForOracleParser.Element_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#element_spec_options}.
	 * @param ctx the parse tree
	 */
	void enterElement_spec_options(ObForOracleParser.Element_spec_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#element_spec_options}.
	 * @param ctx the parse tree
	 */
	void exitElement_spec_options(ObForOracleParser.Element_spec_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void enterSubprogram_spec(ObForOracleParser.Subprogram_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void exitSubprogram_spec(ObForOracleParser.Subprogram_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void enterOverriding_subprogram_spec(ObForOracleParser.Overriding_subprogram_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#overriding_subprogram_spec}.
	 * @param ctx the parse tree
	 */
	void exitOverriding_subprogram_spec(ObForOracleParser.Overriding_subprogram_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterOverriding_function_spec(ObForOracleParser.Overriding_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#overriding_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitOverriding_function_spec(ObForOracleParser.Overriding_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_procedure_spec(ObForOracleParser.Type_procedure_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_procedure_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_procedure_spec(ObForOracleParser.Type_procedure_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_function_spec(ObForOracleParser.Type_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_function_spec(ObForOracleParser.Type_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constructor_spec}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_spec(ObForOracleParser.Constructor_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constructor_spec}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_spec(ObForOracleParser.Constructor_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 */
	void enterMap_order_function_spec(ObForOracleParser.Map_order_function_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#map_order_function_spec}.
	 * @param ctx the parse tree
	 */
	void exitMap_order_function_spec(ObForOracleParser.Map_order_function_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pragma_clause}.
	 * @param ctx the parse tree
	 */
	void enterPragma_clause(ObForOracleParser.Pragma_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pragma_clause}.
	 * @param ctx the parse tree
	 */
	void exitPragma_clause(ObForOracleParser.Pragma_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pragma_elements}.
	 * @param ctx the parse tree
	 */
	void enterPragma_elements(ObForOracleParser.Pragma_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pragma_elements}.
	 * @param ctx the parse tree
	 */
	void exitPragma_elements(ObForOracleParser.Pragma_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 */
	void enterType_elements_parameter(ObForOracleParser.Type_elements_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_elements_parameter}.
	 * @param ctx the parse tree
	 */
	void exitType_elements_parameter(ObForOracleParser.Type_elements_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_sequence}.
	 * @param ctx the parse tree
	 */
	void enterDrop_sequence(ObForOracleParser.Drop_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_sequence}.
	 * @param ctx the parse tree
	 */
	void exitDrop_sequence(ObForOracleParser.Drop_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_sequence}.
	 * @param ctx the parse tree
	 */
	void enterAlter_sequence(ObForOracleParser.Alter_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_sequence}.
	 * @param ctx the parse tree
	 */
	void exitAlter_sequence(ObForOracleParser.Alter_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_session}.
	 * @param ctx the parse tree
	 */
	void enterAlter_session(ObForOracleParser.Alter_sessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_session}.
	 * @param ctx the parse tree
	 */
	void exitAlter_session(ObForOracleParser.Alter_sessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_session_set_clause(ObForOracleParser.Alter_session_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_session_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_session_set_clause(ObForOracleParser.Alter_session_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_system}.
	 * @param ctx the parse tree
	 */
	void enterAlter_system(ObForOracleParser.Alter_systemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_system}.
	 * @param ctx the parse tree
	 */
	void exitAlter_system(ObForOracleParser.Alter_systemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_sequence}.
	 * @param ctx the parse tree
	 */
	void enterCreate_sequence(ObForOracleParser.Create_sequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_sequence}.
	 * @param ctx the parse tree
	 */
	void exitCreate_sequence(ObForOracleParser.Create_sequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sequence_spec}.
	 * @param ctx the parse tree
	 */
	void enterSequence_spec(ObForOracleParser.Sequence_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sequence_spec}.
	 * @param ctx the parse tree
	 */
	void exitSequence_spec(ObForOracleParser.Sequence_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 */
	void enterSequence_start_clause(ObForOracleParser.Sequence_start_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sequence_start_clause}.
	 * @param ctx the parse tree
	 */
	void exitSequence_start_clause(ObForOracleParser.Sequence_start_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_analytic_view(ObForOracleParser.Create_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_analytic_view(ObForOracleParser.Create_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#classification_clause}.
	 * @param ctx the parse tree
	 */
	void enterClassification_clause(ObForOracleParser.Classification_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#classification_clause}.
	 * @param ctx the parse tree
	 */
	void exitClassification_clause(ObForOracleParser.Classification_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#caption_clause}.
	 * @param ctx the parse tree
	 */
	void enterCaption_clause(ObForOracleParser.Caption_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#caption_clause}.
	 * @param ctx the parse tree
	 */
	void exitCaption_clause(ObForOracleParser.Caption_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#description_clause}.
	 * @param ctx the parse tree
	 */
	void enterDescription_clause(ObForOracleParser.Description_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#description_clause}.
	 * @param ctx the parse tree
	 */
	void exitDescription_clause(ObForOracleParser.Description_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#classification_item}.
	 * @param ctx the parse tree
	 */
	void enterClassification_item(ObForOracleParser.Classification_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#classification_item}.
	 * @param ctx the parse tree
	 */
	void exitClassification_item(ObForOracleParser.Classification_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#language}.
	 * @param ctx the parse tree
	 */
	void enterLanguage(ObForOracleParser.LanguageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#language}.
	 * @param ctx the parse tree
	 */
	void exitLanguage(ObForOracleParser.LanguageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cav_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterCav_using_clause(ObForOracleParser.Cav_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cav_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitCav_using_clause(ObForOracleParser.Cav_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dim_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterDim_by_clause(ObForOracleParser.Dim_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dim_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitDim_by_clause(ObForOracleParser.Dim_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dim_key}.
	 * @param ctx the parse tree
	 */
	void enterDim_key(ObForOracleParser.Dim_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dim_key}.
	 * @param ctx the parse tree
	 */
	void exitDim_key(ObForOracleParser.Dim_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dim_ref}.
	 * @param ctx the parse tree
	 */
	void enterDim_ref(ObForOracleParser.Dim_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dim_ref}.
	 * @param ctx the parse tree
	 */
	void exitDim_ref(ObForOracleParser.Dim_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hier_ref}.
	 * @param ctx the parse tree
	 */
	void enterHier_ref(ObForOracleParser.Hier_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hier_ref}.
	 * @param ctx the parse tree
	 */
	void exitHier_ref(ObForOracleParser.Hier_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#measures_clause}.
	 * @param ctx the parse tree
	 */
	void enterMeasures_clause(ObForOracleParser.Measures_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#measures_clause}.
	 * @param ctx the parse tree
	 */
	void exitMeasures_clause(ObForOracleParser.Measures_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#av_measure}.
	 * @param ctx the parse tree
	 */
	void enterAv_measure(ObForOracleParser.Av_measureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#av_measure}.
	 * @param ctx the parse tree
	 */
	void exitAv_measure(ObForOracleParser.Av_measureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#base_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterBase_meas_clause(ObForOracleParser.Base_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#base_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitBase_meas_clause(ObForOracleParser.Base_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterMeas_aggregate_clause(ObForOracleParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#meas_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitMeas_aggregate_clause(ObForOracleParser.Meas_aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterCalc_meas_clause(ObForOracleParser.Calc_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitCalc_meas_clause(ObForOracleParser.Calc_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_measure_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_measure_clause(ObForOracleParser.Default_measure_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_measure_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_measure_clause(ObForOracleParser.Default_measure_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_aggregate_clause(ObForOracleParser.Default_aggregate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_aggregate_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_aggregate_clause(ObForOracleParser.Default_aggregate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterCache_clause(ObForOracleParser.Cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitCache_clause(ObForOracleParser.Cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cache_specification}.
	 * @param ctx the parse tree
	 */
	void enterCache_specification(ObForOracleParser.Cache_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cache_specification}.
	 * @param ctx the parse tree
	 */
	void exitCache_specification(ObForOracleParser.Cache_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#levels_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevels_clause(ObForOracleParser.Levels_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#levels_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevels_clause(ObForOracleParser.Levels_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#level_specification}.
	 * @param ctx the parse tree
	 */
	void enterLevel_specification(ObForOracleParser.Level_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#level_specification}.
	 * @param ctx the parse tree
	 */
	void exitLevel_specification(ObForOracleParser.Level_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#level_group_type}.
	 * @param ctx the parse tree
	 */
	void enterLevel_group_type(ObForOracleParser.Level_group_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#level_group_type}.
	 * @param ctx the parse tree
	 */
	void exitLevel_group_type(ObForOracleParser.Level_group_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 */
	void enterFact_columns_clause(ObForOracleParser.Fact_columns_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#fact_columns_clause}.
	 * @param ctx the parse tree
	 */
	void exitFact_columns_clause(ObForOracleParser.Fact_columns_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 */
	void enterQry_transform_clause(ObForOracleParser.Qry_transform_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#qry_transform_clause}.
	 * @param ctx the parse tree
	 */
	void exitQry_transform_clause(ObForOracleParser.Qry_transform_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterCreate_attribute_dimension(ObForOracleParser.Create_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitCreate_attribute_dimension(ObForOracleParser.Create_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ad_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_using_clause(ObForOracleParser.Ad_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ad_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_using_clause(ObForOracleParser.Ad_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#source_clause}.
	 * @param ctx the parse tree
	 */
	void enterSource_clause(ObForOracleParser.Source_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#source_clause}.
	 * @param ctx the parse tree
	 */
	void exitSource_clause(ObForOracleParser.Source_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_path_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_path_clause(ObForOracleParser.Join_path_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_path_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_path_clause(ObForOracleParser.Join_path_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_condition}.
	 * @param ctx the parse tree
	 */
	void enterJoin_condition(ObForOracleParser.Join_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_condition}.
	 * @param ctx the parse tree
	 */
	void exitJoin_condition(ObForOracleParser.Join_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_condition_item}.
	 * @param ctx the parse tree
	 */
	void enterJoin_condition_item(ObForOracleParser.Join_condition_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_condition_item}.
	 * @param ctx the parse tree
	 */
	void exitJoin_condition_item(ObForOracleParser.Join_condition_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttributes_clause(ObForOracleParser.Attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttributes_clause(ObForOracleParser.Attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_attributes_clause(ObForOracleParser.Ad_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ad_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_attributes_clause(ObForOracleParser.Ad_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ad_level_clause}.
	 * @param ctx the parse tree
	 */
	void enterAd_level_clause(ObForOracleParser.Ad_level_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ad_level_clause}.
	 * @param ctx the parse tree
	 */
	void exitAd_level_clause(ObForOracleParser.Ad_level_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#key_clause}.
	 * @param ctx the parse tree
	 */
	void enterKey_clause(ObForOracleParser.Key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#key_clause}.
	 * @param ctx the parse tree
	 */
	void exitKey_clause(ObForOracleParser.Key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlternate_key_clause(ObForOracleParser.Alternate_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alternate_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlternate_key_clause(ObForOracleParser.Alternate_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dim_order_clause}.
	 * @param ctx the parse tree
	 */
	void enterDim_order_clause(ObForOracleParser.Dim_order_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dim_order_clause}.
	 * @param ctx the parse tree
	 */
	void exitDim_order_clause(ObForOracleParser.Dim_order_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#all_clause}.
	 * @param ctx the parse tree
	 */
	void enterAll_clause(ObForOracleParser.All_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#all_clause}.
	 * @param ctx the parse tree
	 */
	void exitAll_clause(ObForOracleParser.All_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterCreate_audit_policy(ObForOracleParser.Create_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitCreate_audit_policy(ObForOracleParser.Create_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege_audit_clause(ObForOracleParser.Privilege_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#privilege_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege_audit_clause(ObForOracleParser.Privilege_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#action_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterAction_audit_clause(ObForOracleParser.Action_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#action_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitAction_audit_clause(ObForOracleParser.Action_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#system_actions}.
	 * @param ctx the parse tree
	 */
	void enterSystem_actions(ObForOracleParser.System_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#system_actions}.
	 * @param ctx the parse tree
	 */
	void exitSystem_actions(ObForOracleParser.System_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#standard_actions}.
	 * @param ctx the parse tree
	 */
	void enterStandard_actions(ObForOracleParser.Standard_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#standard_actions}.
	 * @param ctx the parse tree
	 */
	void exitStandard_actions(ObForOracleParser.Standard_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#actions_clause}.
	 * @param ctx the parse tree
	 */
	void enterActions_clause(ObForOracleParser.Actions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#actions_clause}.
	 * @param ctx the parse tree
	 */
	void exitActions_clause(ObForOracleParser.Actions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_action}.
	 * @param ctx the parse tree
	 */
	void enterObject_action(ObForOracleParser.Object_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_action}.
	 * @param ctx the parse tree
	 */
	void exitObject_action(ObForOracleParser.Object_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#system_action}.
	 * @param ctx the parse tree
	 */
	void enterSystem_action(ObForOracleParser.System_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#system_action}.
	 * @param ctx the parse tree
	 */
	void exitSystem_action(ObForOracleParser.System_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#component_actions}.
	 * @param ctx the parse tree
	 */
	void enterComponent_actions(ObForOracleParser.Component_actionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#component_actions}.
	 * @param ctx the parse tree
	 */
	void exitComponent_actions(ObForOracleParser.Component_actionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#component_action}.
	 * @param ctx the parse tree
	 */
	void enterComponent_action(ObForOracleParser.Component_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#component_action}.
	 * @param ctx the parse tree
	 */
	void exitComponent_action(ObForOracleParser.Component_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#role_audit_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_audit_clause(ObForOracleParser.Role_audit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#role_audit_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_audit_clause(ObForOracleParser.Role_audit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_controlfile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_controlfile(ObForOracleParser.Create_controlfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_controlfile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_controlfile(ObForOracleParser.Create_controlfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#controlfile_options}.
	 * @param ctx the parse tree
	 */
	void enterControlfile_options(ObForOracleParser.Controlfile_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#controlfile_options}.
	 * @param ctx the parse tree
	 */
	void exitControlfile_options(ObForOracleParser.Controlfile_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_clause(ObForOracleParser.Logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_clause(ObForOracleParser.Logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#character_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_set_clause(ObForOracleParser.Character_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#character_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_set_clause(ObForOracleParser.Character_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#file_specification}.
	 * @param ctx the parse tree
	 */
	void enterFile_specification(ObForOracleParser.File_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#file_specification}.
	 * @param ctx the parse tree
	 */
	void exitFile_specification(ObForOracleParser.File_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterCreate_diskgroup(ObForOracleParser.Create_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitCreate_diskgroup(ObForOracleParser.Create_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 */
	void enterQualified_disk_clause(ObForOracleParser.Qualified_disk_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#qualified_disk_clause}.
	 * @param ctx the parse tree
	 */
	void exitQualified_disk_clause(ObForOracleParser.Qualified_disk_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_edition}.
	 * @param ctx the parse tree
	 */
	void enterCreate_edition(ObForOracleParser.Create_editionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_edition}.
	 * @param ctx the parse tree
	 */
	void exitCreate_edition(ObForOracleParser.Create_editionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterCreate_flashback_archive(ObForOracleParser.Create_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitCreate_flashback_archive(ObForOracleParser.Create_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_quota(ObForOracleParser.Flashback_archive_quotaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_archive_quota}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_quota(ObForOracleParser.Flashback_archive_quotaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_retention(ObForOracleParser.Flashback_archive_retentionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_archive_retention}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_retention(ObForOracleParser.Flashback_archive_retentionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterCreate_hierarchy(ObForOracleParser.Create_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitCreate_hierarchy(ObForOracleParser.Create_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hier_using_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_using_clause(ObForOracleParser.Hier_using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hier_using_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_using_clause(ObForOracleParser.Hier_using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#level_hier_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevel_hier_clause(ObForOracleParser.Level_hier_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#level_hier_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevel_hier_clause(ObForOracleParser.Level_hier_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_attrs_clause(ObForOracleParser.Hier_attrs_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hier_attrs_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_attrs_clause(ObForOracleParser.Hier_attrs_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 */
	void enterHier_attr_clause(ObForOracleParser.Hier_attr_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hier_attr_clause}.
	 * @param ctx the parse tree
	 */
	void exitHier_attr_clause(ObForOracleParser.Hier_attr_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hier_attr_name}.
	 * @param ctx the parse tree
	 */
	void enterHier_attr_name(ObForOracleParser.Hier_attr_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hier_attr_name}.
	 * @param ctx the parse tree
	 */
	void exitHier_attr_name(ObForOracleParser.Hier_attr_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_index}.
	 * @param ctx the parse tree
	 */
	void enterCreate_index(ObForOracleParser.Create_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_index}.
	 * @param ctx the parse tree
	 */
	void exitCreate_index(ObForOracleParser.Create_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterCluster_index_clause(ObForOracleParser.Cluster_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cluster_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitCluster_index_clause(ObForOracleParser.Cluster_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cluster_name}.
	 * @param ctx the parse tree
	 */
	void enterCluster_name(ObForOracleParser.Cluster_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cluster_name}.
	 * @param ctx the parse tree
	 */
	void exitCluster_name(ObForOracleParser.Cluster_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_index_columns}.
	 * @param ctx the parse tree
	 */
	void enterTable_index_columns(ObForOracleParser.Table_index_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_index_columns}.
	 * @param ctx the parse tree
	 */
	void exitTable_index_columns(ObForOracleParser.Table_index_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterBitmap_join_index_clause(ObForOracleParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#bitmap_join_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitBitmap_join_index_clause(ObForOracleParser.Bitmap_join_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_expr}.
	 * @param ctx the parse tree
	 */
	void enterIndex_expr(ObForOracleParser.Index_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_expr}.
	 * @param ctx the parse tree
	 */
	void exitIndex_expr(ObForOracleParser.Index_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_properties}.
	 * @param ctx the parse tree
	 */
	void enterIndex_properties(ObForOracleParser.Index_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_properties}.
	 * @param ctx the parse tree
	 */
	void exitIndex_properties(ObForOracleParser.Index_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterDomain_index_clause(ObForOracleParser.Domain_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitDomain_index_clause(ObForOracleParser.Domain_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterLocal_domain_index_clause(ObForOracleParser.Local_domain_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#local_domain_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitLocal_domain_index_clause(ObForOracleParser.Local_domain_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void enterXmlindex_clause(ObForOracleParser.Xmlindex_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void exitXmlindex_clause(ObForOracleParser.Xmlindex_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void enterLocal_xmlindex_clause(ObForOracleParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#local_xmlindex_clause}.
	 * @param ctx the parse tree
	 */
	void exitLocal_xmlindex_clause(ObForOracleParser.Local_xmlindex_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void enterGlobal_partitioned_index(ObForOracleParser.Global_partitioned_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#global_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void exitGlobal_partitioned_index(ObForOracleParser.Global_partitioned_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partitioning_clause(ObForOracleParser.Index_partitioning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_partitioning_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partitioning_clause(ObForOracleParser.Index_partitioning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partitioning_values_list(ObForOracleParser.Index_partitioning_values_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_partitioning_values_list}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partitioning_values_list(ObForOracleParser.Index_partitioning_values_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void enterLocal_partitioned_index(ObForOracleParser.Local_partitioned_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#local_partitioned_index}.
	 * @param ctx the parse tree
	 */
	void exitLocal_partitioned_index(ObForOracleParser.Local_partitioned_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_range_partitioned_table(ObForOracleParser.On_range_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_range_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_range_partitioned_table(ObForOracleParser.On_range_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_list_partitioned_table(ObForOracleParser.On_list_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_list_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_list_partitioned_table(ObForOracleParser.On_list_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterPartitioned_table(ObForOracleParser.Partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitPartitioned_table(ObForOracleParser.Partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_hash_partitioned_table(ObForOracleParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_hash_partitioned_table(ObForOracleParser.On_hash_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_hash_partitioned_clause(ObForOracleParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_hash_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_hash_partitioned_clause(ObForOracleParser.On_hash_partitioned_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void enterOn_comp_partitioned_table(ObForOracleParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_table}.
	 * @param ctx the parse tree
	 */
	void exitOn_comp_partitioned_table(ObForOracleParser.On_comp_partitioned_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_comp_partitioned_clause(ObForOracleParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_comp_partitioned_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_comp_partitioned_clause(ObForOracleParser.On_comp_partitioned_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_subpartition_clause(ObForOracleParser.Index_subpartition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_subpartition_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_subpartition_clause(ObForOracleParser.Index_subpartition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_subpartition_subclause(ObForOracleParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_subpartition_subclause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_subpartition_subclause(ObForOracleParser.Index_subpartition_subclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#odci_parameters}.
	 * @param ctx the parse tree
	 */
	void enterOdci_parameters(ObForOracleParser.Odci_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#odci_parameters}.
	 * @param ctx the parse tree
	 */
	void exitOdci_parameters(ObForOracleParser.Odci_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#indextype}.
	 * @param ctx the parse tree
	 */
	void enterIndextype(ObForOracleParser.IndextypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#indextype}.
	 * @param ctx the parse tree
	 */
	void exitIndextype(ObForOracleParser.IndextypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_index}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index(ObForOracleParser.Alter_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_index}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index(ObForOracleParser.Alter_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_ops_set1(ObForOracleParser.Alter_index_ops_set1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_index_ops_set1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_ops_set1(ObForOracleParser.Alter_index_ops_set1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_ops_set2(ObForOracleParser.Alter_index_ops_set2Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_index_ops_set2}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_ops_set2(ObForOracleParser.Alter_index_ops_set2Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 */
	void enterVisible_or_invisible(ObForOracleParser.Visible_or_invisibleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#visible_or_invisible}.
	 * @param ctx the parse tree
	 */
	void exitVisible_or_invisible(ObForOracleParser.Visible_or_invisibleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 */
	void enterMonitoring_nomonitoring(ObForOracleParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#monitoring_nomonitoring}.
	 * @param ctx the parse tree
	 */
	void exitMonitoring_nomonitoring(ObForOracleParser.Monitoring_nomonitoringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rebuild_clause}.
	 * @param ctx the parse tree
	 */
	void enterRebuild_clause(ObForOracleParser.Rebuild_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rebuild_clause}.
	 * @param ctx the parse tree
	 */
	void exitRebuild_clause(ObForOracleParser.Rebuild_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterAlter_index_partitioning(ObForOracleParser.Alter_index_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_index_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitAlter_index_partitioning(ObForOracleParser.Alter_index_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_default_attrs(ObForOracleParser.Modify_index_default_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_index_default_attrs}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_default_attrs(ObForOracleParser.Modify_index_default_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterAdd_hash_index_partition(ObForOracleParser.Add_hash_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_hash_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitAdd_hash_index_partition(ObForOracleParser.Add_hash_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterCoalesce_index_partition(ObForOracleParser.Coalesce_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#coalesce_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitCoalesce_index_partition(ObForOracleParser.Coalesce_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_partition(ObForOracleParser.Modify_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_partition(ObForOracleParser.Modify_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_partitions_ops(ObForOracleParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_index_partitions_ops}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_partitions_ops(ObForOracleParser.Modify_index_partitions_opsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rename_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterRename_index_partition(ObForOracleParser.Rename_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rename_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitRename_index_partition(ObForOracleParser.Rename_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index_partition(ObForOracleParser.Drop_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index_partition(ObForOracleParser.Drop_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#split_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterSplit_index_partition(ObForOracleParser.Split_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#split_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitSplit_index_partition(ObForOracleParser.Split_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_partition_description}.
	 * @param ctx the parse tree
	 */
	void enterIndex_partition_description(ObForOracleParser.Index_partition_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_partition_description}.
	 * @param ctx the parse tree
	 */
	void exitIndex_partition_description(ObForOracleParser.Index_partition_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void enterModify_index_subpartition(ObForOracleParser.Modify_index_subpartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void exitModify_index_subpartition(ObForOracleParser.Modify_index_subpartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_name_old}.
	 * @param ctx the parse tree
	 */
	void enterPartition_name_old(ObForOracleParser.Partition_name_oldContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_name_old}.
	 * @param ctx the parse tree
	 */
	void exitPartition_name_old(ObForOracleParser.Partition_name_oldContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_partition_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_partition_name(ObForOracleParser.New_partition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_partition_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_partition_name(ObForOracleParser.New_partition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_index_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_index_name(ObForOracleParser.New_index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_index_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_index_name(ObForOracleParser.New_index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterAlter_inmemory_join_group(ObForOracleParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitAlter_inmemory_join_group(ObForOracleParser.Alter_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_user}.
	 * @param ctx the parse tree
	 */
	void enterCreate_user(ObForOracleParser.Create_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_user}.
	 * @param ctx the parse tree
	 */
	void exitCreate_user(ObForOracleParser.Create_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_user}.
	 * @param ctx the parse tree
	 */
	void enterAlter_user(ObForOracleParser.Alter_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_user}.
	 * @param ctx the parse tree
	 */
	void exitAlter_user(ObForOracleParser.Alter_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_user}.
	 * @param ctx the parse tree
	 */
	void enterDrop_user(ObForOracleParser.Drop_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_user}.
	 * @param ctx the parse tree
	 */
	void exitDrop_user(ObForOracleParser.Drop_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_identified_by}.
	 * @param ctx the parse tree
	 */
	void enterAlter_identified_by(ObForOracleParser.Alter_identified_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_identified_by}.
	 * @param ctx the parse tree
	 */
	void exitAlter_identified_by(ObForOracleParser.Alter_identified_byContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identified_by}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by(ObForOracleParser.Identified_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identified_by}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by(ObForOracleParser.Identified_byContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identified_other_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_other_clause(ObForOracleParser.Identified_other_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identified_other_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_other_clause(ObForOracleParser.Identified_other_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_tablespace_clause(ObForOracleParser.User_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_tablespace_clause(ObForOracleParser.User_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#quota_clause}.
	 * @param ctx the parse tree
	 */
	void enterQuota_clause(ObForOracleParser.Quota_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#quota_clause}.
	 * @param ctx the parse tree
	 */
	void exitQuota_clause(ObForOracleParser.Quota_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#profile_clause}.
	 * @param ctx the parse tree
	 */
	void enterProfile_clause(ObForOracleParser.Profile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#profile_clause}.
	 * @param ctx the parse tree
	 */
	void exitProfile_clause(ObForOracleParser.Profile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#role_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_clause(ObForOracleParser.Role_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#role_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_clause(ObForOracleParser.Role_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_default_role_clause(ObForOracleParser.User_default_role_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_default_role_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_default_role_clause(ObForOracleParser.User_default_role_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#password_expire_clause}.
	 * @param ctx the parse tree
	 */
	void enterPassword_expire_clause(ObForOracleParser.Password_expire_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#password_expire_clause}.
	 * @param ctx the parse tree
	 */
	void exitPassword_expire_clause(ObForOracleParser.Password_expire_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_lock_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_lock_clause(ObForOracleParser.User_lock_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_lock_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_lock_clause(ObForOracleParser.User_lock_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void enterUser_editions_clause(ObForOracleParser.User_editions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void exitUser_editions_clause(ObForOracleParser.User_editions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_user_editions_clause(ObForOracleParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_user_editions_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_user_editions_clause(ObForOracleParser.Alter_user_editions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#proxy_clause}.
	 * @param ctx the parse tree
	 */
	void enterProxy_clause(ObForOracleParser.Proxy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#proxy_clause}.
	 * @param ctx the parse tree
	 */
	void exitProxy_clause(ObForOracleParser.Proxy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#container_names}.
	 * @param ctx the parse tree
	 */
	void enterContainer_names(ObForOracleParser.Container_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#container_names}.
	 * @param ctx the parse tree
	 */
	void exitContainer_names(ObForOracleParser.Container_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_container_data}.
	 * @param ctx the parse tree
	 */
	void enterSet_container_data(ObForOracleParser.Set_container_dataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_container_data}.
	 * @param ctx the parse tree
	 */
	void exitSet_container_data(ObForOracleParser.Set_container_dataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 */
	void enterAdd_rem_container_data(ObForOracleParser.Add_rem_container_dataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_rem_container_data}.
	 * @param ctx the parse tree
	 */
	void exitAdd_rem_container_data(ObForOracleParser.Add_rem_container_dataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#container_data_clause}.
	 * @param ctx the parse tree
	 */
	void enterContainer_data_clause(ObForOracleParser.Container_data_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#container_data_clause}.
	 * @param ctx the parse tree
	 */
	void exitContainer_data_clause(ObForOracleParser.Container_data_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#administer_key_management}.
	 * @param ctx the parse tree
	 */
	void enterAdminister_key_management(ObForOracleParser.Administer_key_managementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#administer_key_management}.
	 * @param ctx the parse tree
	 */
	void exitAdminister_key_management(ObForOracleParser.Administer_key_managementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterKeystore_management_clauses(ObForOracleParser.Keystore_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#keystore_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitKeystore_management_clauses(ObForOracleParser.Keystore_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_keystore}.
	 * @param ctx the parse tree
	 */
	void enterCreate_keystore(ObForOracleParser.Create_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_keystore}.
	 * @param ctx the parse tree
	 */
	void exitCreate_keystore(ObForOracleParser.Create_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#open_keystore}.
	 * @param ctx the parse tree
	 */
	void enterOpen_keystore(ObForOracleParser.Open_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#open_keystore}.
	 * @param ctx the parse tree
	 */
	void exitOpen_keystore(ObForOracleParser.Open_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#force_keystore}.
	 * @param ctx the parse tree
	 */
	void enterForce_keystore(ObForOracleParser.Force_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#force_keystore}.
	 * @param ctx the parse tree
	 */
	void exitForce_keystore(ObForOracleParser.Force_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#close_keystore}.
	 * @param ctx the parse tree
	 */
	void enterClose_keystore(ObForOracleParser.Close_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#close_keystore}.
	 * @param ctx the parse tree
	 */
	void exitClose_keystore(ObForOracleParser.Close_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#backup_keystore}.
	 * @param ctx the parse tree
	 */
	void enterBackup_keystore(ObForOracleParser.Backup_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#backup_keystore}.
	 * @param ctx the parse tree
	 */
	void exitBackup_keystore(ObForOracleParser.Backup_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 */
	void enterAlter_keystore_password(ObForOracleParser.Alter_keystore_passwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_keystore_password}.
	 * @param ctx the parse tree
	 */
	void exitAlter_keystore_password(ObForOracleParser.Alter_keystore_passwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 */
	void enterMerge_into_new_keystore(ObForOracleParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_into_new_keystore}.
	 * @param ctx the parse tree
	 */
	void exitMerge_into_new_keystore(ObForOracleParser.Merge_into_new_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 */
	void enterMerge_into_existing_keystore(ObForOracleParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_into_existing_keystore}.
	 * @param ctx the parse tree
	 */
	void exitMerge_into_existing_keystore(ObForOracleParser.Merge_into_existing_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#isolate_keystore}.
	 * @param ctx the parse tree
	 */
	void enterIsolate_keystore(ObForOracleParser.Isolate_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#isolate_keystore}.
	 * @param ctx the parse tree
	 */
	void exitIsolate_keystore(ObForOracleParser.Isolate_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unite_keystore}.
	 * @param ctx the parse tree
	 */
	void enterUnite_keystore(ObForOracleParser.Unite_keystoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unite_keystore}.
	 * @param ctx the parse tree
	 */
	void exitUnite_keystore(ObForOracleParser.Unite_keystoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#key_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterKey_management_clauses(ObForOracleParser.Key_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#key_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitKey_management_clauses(ObForOracleParser.Key_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_key}.
	 * @param ctx the parse tree
	 */
	void enterSet_key(ObForOracleParser.Set_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_key}.
	 * @param ctx the parse tree
	 */
	void exitSet_key(ObForOracleParser.Set_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_key}.
	 * @param ctx the parse tree
	 */
	void enterCreate_key(ObForOracleParser.Create_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_key}.
	 * @param ctx the parse tree
	 */
	void exitCreate_key(ObForOracleParser.Create_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mkid}.
	 * @param ctx the parse tree
	 */
	void enterMkid(ObForOracleParser.MkidContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mkid}.
	 * @param ctx the parse tree
	 */
	void exitMkid(ObForOracleParser.MkidContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mk}.
	 * @param ctx the parse tree
	 */
	void enterMk(ObForOracleParser.MkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mk}.
	 * @param ctx the parse tree
	 */
	void exitMk(ObForOracleParser.MkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#use_key}.
	 * @param ctx the parse tree
	 */
	void enterUse_key(ObForOracleParser.Use_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#use_key}.
	 * @param ctx the parse tree
	 */
	void exitUse_key(ObForOracleParser.Use_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_key_tag}.
	 * @param ctx the parse tree
	 */
	void enterSet_key_tag(ObForOracleParser.Set_key_tagContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_key_tag}.
	 * @param ctx the parse tree
	 */
	void exitSet_key_tag(ObForOracleParser.Set_key_tagContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#export_keys}.
	 * @param ctx the parse tree
	 */
	void enterExport_keys(ObForOracleParser.Export_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#export_keys}.
	 * @param ctx the parse tree
	 */
	void exitExport_keys(ObForOracleParser.Export_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#import_keys}.
	 * @param ctx the parse tree
	 */
	void enterImport_keys(ObForOracleParser.Import_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#import_keys}.
	 * @param ctx the parse tree
	 */
	void exitImport_keys(ObForOracleParser.Import_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#migrate_keys}.
	 * @param ctx the parse tree
	 */
	void enterMigrate_keys(ObForOracleParser.Migrate_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#migrate_keys}.
	 * @param ctx the parse tree
	 */
	void exitMigrate_keys(ObForOracleParser.Migrate_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 */
	void enterReverse_migrate_keys(ObForOracleParser.Reverse_migrate_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#reverse_migrate_keys}.
	 * @param ctx the parse tree
	 */
	void exitReverse_migrate_keys(ObForOracleParser.Reverse_migrate_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#move_keys}.
	 * @param ctx the parse tree
	 */
	void enterMove_keys(ObForOracleParser.Move_keysContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#move_keys}.
	 * @param ctx the parse tree
	 */
	void exitMove_keys(ObForOracleParser.Move_keysContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identified_by_store}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by_store(ObForOracleParser.Identified_by_storeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identified_by_store}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by_store(ObForOracleParser.Identified_by_storeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_algorithm_clause(ObForOracleParser.Using_algorithm_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_algorithm_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_algorithm_clause(ObForOracleParser.Using_algorithm_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_tag_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_tag_clause(ObForOracleParser.Using_tag_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_tag_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_tag_clause(ObForOracleParser.Using_tag_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 */
	void enterSecret_management_clauses(ObForOracleParser.Secret_management_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#secret_management_clauses}.
	 * @param ctx the parse tree
	 */
	void exitSecret_management_clauses(ObForOracleParser.Secret_management_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_update_secret}.
	 * @param ctx the parse tree
	 */
	void enterAdd_update_secret(ObForOracleParser.Add_update_secretContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_update_secret}.
	 * @param ctx the parse tree
	 */
	void exitAdd_update_secret(ObForOracleParser.Add_update_secretContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#delete_secret}.
	 * @param ctx the parse tree
	 */
	void enterDelete_secret(ObForOracleParser.Delete_secretContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#delete_secret}.
	 * @param ctx the parse tree
	 */
	void exitDelete_secret(ObForOracleParser.Delete_secretContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 */
	void enterAdd_update_secret_seps(ObForOracleParser.Add_update_secret_sepsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_update_secret_seps}.
	 * @param ctx the parse tree
	 */
	void exitAdd_update_secret_seps(ObForOracleParser.Add_update_secret_sepsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 */
	void enterDelete_secret_seps(ObForOracleParser.Delete_secret_sepsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#delete_secret_seps}.
	 * @param ctx the parse tree
	 */
	void exitDelete_secret_seps(ObForOracleParser.Delete_secret_sepsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 */
	void enterZero_downtime_software_patching_clauses(ObForOracleParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#zero_downtime_software_patching_clauses}.
	 * @param ctx the parse tree
	 */
	void exitZero_downtime_software_patching_clauses(ObForOracleParser.Zero_downtime_software_patching_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#with_backup_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_backup_clause(ObForOracleParser.With_backup_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#with_backup_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_backup_clause(ObForOracleParser.With_backup_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentified_by_password_clause(ObForOracleParser.Identified_by_password_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identified_by_password_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentified_by_password_clause(ObForOracleParser.Identified_by_password_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#keystore_password}.
	 * @param ctx the parse tree
	 */
	void enterKeystore_password(ObForOracleParser.Keystore_passwordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#keystore_password}.
	 * @param ctx the parse tree
	 */
	void exitKeystore_password(ObForOracleParser.Keystore_passwordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(ObForOracleParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(ObForOracleParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#secret}.
	 * @param ctx the parse tree
	 */
	void enterSecret(ObForOracleParser.SecretContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#secret}.
	 * @param ctx the parse tree
	 */
	void exitSecret(ObForOracleParser.SecretContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#analyze}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze(ObForOracleParser.AnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#analyze}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze(ObForOracleParser.AnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extention_clause(ObForOracleParser.Partition_extention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_extention_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extention_clause(ObForOracleParser.Partition_extention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#validation_clauses}.
	 * @param ctx the parse tree
	 */
	void enterValidation_clauses(ObForOracleParser.Validation_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#validation_clauses}.
	 * @param ctx the parse tree
	 */
	void exitValidation_clauses(ObForOracleParser.Validation_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compute_clauses}.
	 * @param ctx the parse tree
	 */
	void enterCompute_clauses(ObForOracleParser.Compute_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compute_clauses}.
	 * @param ctx the parse tree
	 */
	void exitCompute_clauses(ObForOracleParser.Compute_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#for_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_clause(ObForOracleParser.For_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#for_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_clause(ObForOracleParser.For_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#online_or_offline}.
	 * @param ctx the parse tree
	 */
	void enterOnline_or_offline(ObForOracleParser.Online_or_offlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#online_or_offline}.
	 * @param ctx the parse tree
	 */
	void exitOnline_or_offline(ObForOracleParser.Online_or_offlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#into_clause1}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause1(ObForOracleParser.Into_clause1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#into_clause1}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause1(ObForOracleParser.Into_clause1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_key_value}.
	 * @param ctx the parse tree
	 */
	void enterPartition_key_value(ObForOracleParser.Partition_key_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_key_value}.
	 * @param ctx the parse tree
	 */
	void exitPartition_key_value(ObForOracleParser.Partition_key_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_key_value(ObForOracleParser.Subpartition_key_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_key_value}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_key_value(ObForOracleParser.Subpartition_key_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#associate_statistics}.
	 * @param ctx the parse tree
	 */
	void enterAssociate_statistics(ObForOracleParser.Associate_statisticsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#associate_statistics}.
	 * @param ctx the parse tree
	 */
	void exitAssociate_statistics(ObForOracleParser.Associate_statisticsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_association}.
	 * @param ctx the parse tree
	 */
	void enterColumn_association(ObForOracleParser.Column_associationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_association}.
	 * @param ctx the parse tree
	 */
	void exitColumn_association(ObForOracleParser.Column_associationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_association}.
	 * @param ctx the parse tree
	 */
	void enterFunction_association(ObForOracleParser.Function_associationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_association}.
	 * @param ctx the parse tree
	 */
	void exitFunction_association(ObForOracleParser.Function_associationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#indextype_name}.
	 * @param ctx the parse tree
	 */
	void enterIndextype_name(ObForOracleParser.Indextype_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#indextype_name}.
	 * @param ctx the parse tree
	 */
	void exitIndextype_name(ObForOracleParser.Indextype_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_statistics_type}.
	 * @param ctx the parse tree
	 */
	void enterUsing_statistics_type(ObForOracleParser.Using_statistics_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_statistics_type}.
	 * @param ctx the parse tree
	 */
	void exitUsing_statistics_type(ObForOracleParser.Using_statistics_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#statistics_type_name}.
	 * @param ctx the parse tree
	 */
	void enterStatistics_type_name(ObForOracleParser.Statistics_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#statistics_type_name}.
	 * @param ctx the parse tree
	 */
	void exitStatistics_type_name(ObForOracleParser.Statistics_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_cost_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_cost_clause(ObForOracleParser.Default_cost_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_cost_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_cost_clause(ObForOracleParser.Default_cost_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cpu_cost}.
	 * @param ctx the parse tree
	 */
	void enterCpu_cost(ObForOracleParser.Cpu_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cpu_cost}.
	 * @param ctx the parse tree
	 */
	void exitCpu_cost(ObForOracleParser.Cpu_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#io_cost}.
	 * @param ctx the parse tree
	 */
	void enterIo_cost(ObForOracleParser.Io_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#io_cost}.
	 * @param ctx the parse tree
	 */
	void exitIo_cost(ObForOracleParser.Io_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#network_cost}.
	 * @param ctx the parse tree
	 */
	void enterNetwork_cost(ObForOracleParser.Network_costContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#network_cost}.
	 * @param ctx the parse tree
	 */
	void exitNetwork_cost(ObForOracleParser.Network_costContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_selectivity_clause(ObForOracleParser.Default_selectivity_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_selectivity_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_selectivity_clause(ObForOracleParser.Default_selectivity_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_selectivity}.
	 * @param ctx the parse tree
	 */
	void enterDefault_selectivity(ObForOracleParser.Default_selectivityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_selectivity}.
	 * @param ctx the parse tree
	 */
	void exitDefault_selectivity(ObForOracleParser.Default_selectivityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#storage_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterStorage_table_clause(ObForOracleParser.Storage_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#storage_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitStorage_table_clause(ObForOracleParser.Storage_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unified_auditing}.
	 * @param ctx the parse tree
	 */
	void enterUnified_auditing(ObForOracleParser.Unified_auditingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unified_auditing}.
	 * @param ctx the parse tree
	 */
	void exitUnified_auditing(ObForOracleParser.Unified_auditingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#policy_name}.
	 * @param ctx the parse tree
	 */
	void enterPolicy_name(ObForOracleParser.Policy_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#policy_name}.
	 * @param ctx the parse tree
	 */
	void exitPolicy_name(ObForOracleParser.Policy_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_traditional}.
	 * @param ctx the parse tree
	 */
	void enterAudit_traditional(ObForOracleParser.Audit_traditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_traditional}.
	 * @param ctx the parse tree
	 */
	void exitAudit_traditional(ObForOracleParser.Audit_traditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_direct_path}.
	 * @param ctx the parse tree
	 */
	void enterAudit_direct_path(ObForOracleParser.Audit_direct_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_direct_path}.
	 * @param ctx the parse tree
	 */
	void exitAudit_direct_path(ObForOracleParser.Audit_direct_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_container_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_container_clause(ObForOracleParser.Audit_container_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_container_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_container_clause(ObForOracleParser.Audit_container_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_operation_clause(ObForOracleParser.Audit_operation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_operation_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_operation_clause(ObForOracleParser.Audit_operation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterAuditing_by_clause(ObForOracleParser.Auditing_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#auditing_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitAuditing_by_clause(ObForOracleParser.Auditing_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_user}.
	 * @param ctx the parse tree
	 */
	void enterAudit_user(ObForOracleParser.Audit_userContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_user}.
	 * @param ctx the parse tree
	 */
	void exitAudit_user(ObForOracleParser.Audit_userContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 */
	void enterAudit_schema_object_clause(ObForOracleParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#audit_schema_object_clause}.
	 * @param ctx the parse tree
	 */
	void exitAudit_schema_object_clause(ObForOracleParser.Audit_schema_object_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_operation}.
	 * @param ctx the parse tree
	 */
	void enterSql_operation(ObForOracleParser.Sql_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_operation}.
	 * @param ctx the parse tree
	 */
	void exitSql_operation(ObForOracleParser.Sql_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 */
	void enterAuditing_on_clause(ObForOracleParser.Auditing_on_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#auditing_on_clause}.
	 * @param ctx the parse tree
	 */
	void exitAuditing_on_clause(ObForOracleParser.Auditing_on_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_name}.
	 * @param ctx the parse tree
	 */
	void enterModel_name(ObForOracleParser.Model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_name}.
	 * @param ctx the parse tree
	 */
	void exitModel_name(ObForOracleParser.Model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_name}.
	 * @param ctx the parse tree
	 */
	void enterObject_name(ObForOracleParser.Object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_name}.
	 * @param ctx the parse tree
	 */
	void exitObject_name(ObForOracleParser.Object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#profile_name}.
	 * @param ctx the parse tree
	 */
	void enterProfile_name(ObForOracleParser.Profile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#profile_name}.
	 * @param ctx the parse tree
	 */
	void exitProfile_name(ObForOracleParser.Profile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 */
	void enterSql_statement_shortcut(ObForOracleParser.Sql_statement_shortcutContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_statement_shortcut}.
	 * @param ctx the parse tree
	 */
	void exitSql_statement_shortcut(ObForOracleParser.Sql_statement_shortcutContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_index}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index(ObForOracleParser.Drop_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_index}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index(ObForOracleParser.Drop_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 */
	void enterDisassociate_statistics(ObForOracleParser.Disassociate_statisticsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disassociate_statistics}.
	 * @param ctx the parse tree
	 */
	void exitDisassociate_statistics(ObForOracleParser.Disassociate_statisticsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_indextype}.
	 * @param ctx the parse tree
	 */
	void enterDrop_indextype(ObForOracleParser.Drop_indextypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_indextype}.
	 * @param ctx the parse tree
	 */
	void exitDrop_indextype(ObForOracleParser.Drop_indextypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterDrop_inmemory_join_group(ObForOracleParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitDrop_inmemory_join_group(ObForOracleParser.Drop_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_table}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_table(ObForOracleParser.Flashback_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_table}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_table(ObForOracleParser.Flashback_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#restore_point}.
	 * @param ctx the parse tree
	 */
	void enterRestore_point(ObForOracleParser.Restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#restore_point}.
	 * @param ctx the parse tree
	 */
	void exitRestore_point(ObForOracleParser.Restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#purge_statement}.
	 * @param ctx the parse tree
	 */
	void enterPurge_statement(ObForOracleParser.Purge_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#purge_statement}.
	 * @param ctx the parse tree
	 */
	void exitPurge_statement(ObForOracleParser.Purge_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#noaudit_statement}.
	 * @param ctx the parse tree
	 */
	void enterNoaudit_statement(ObForOracleParser.Noaudit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#noaudit_statement}.
	 * @param ctx the parse tree
	 */
	void exitNoaudit_statement(ObForOracleParser.Noaudit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rename_object}.
	 * @param ctx the parse tree
	 */
	void enterRename_object(ObForOracleParser.Rename_objectContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rename_object}.
	 * @param ctx the parse tree
	 */
	void exitRename_object(ObForOracleParser.Rename_objectContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#grant_statement}.
	 * @param ctx the parse tree
	 */
	void enterGrant_statement(ObForOracleParser.Grant_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#grant_statement}.
	 * @param ctx the parse tree
	 */
	void exitGrant_statement(ObForOracleParser.Grant_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#container_clause}.
	 * @param ctx the parse tree
	 */
	void enterContainer_clause(ObForOracleParser.Container_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#container_clause}.
	 * @param ctx the parse tree
	 */
	void exitContainer_clause(ObForOracleParser.Container_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#revoke_statement}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_statement(ObForOracleParser.Revoke_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#revoke_statement}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_statement(ObForOracleParser.Revoke_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_system_privilege(ObForOracleParser.Revoke_system_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#revoke_system_privilege}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_system_privilege(ObForOracleParser.Revoke_system_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#revokee_clause}.
	 * @param ctx the parse tree
	 */
	void enterRevokee_clause(ObForOracleParser.Revokee_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#revokee_clause}.
	 * @param ctx the parse tree
	 */
	void exitRevokee_clause(ObForOracleParser.Revokee_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_object_privileges(ObForOracleParser.Revoke_object_privilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#revoke_object_privileges}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_object_privileges(ObForOracleParser.Revoke_object_privilegesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_object_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_object_clause(ObForOracleParser.On_object_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_object_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_object_clause(ObForOracleParser.On_object_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_roles_from_programs(ObForOracleParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#revoke_roles_from_programs}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_roles_from_programs(ObForOracleParser.Revoke_roles_from_programsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#program_unit}.
	 * @param ctx the parse tree
	 */
	void enterProgram_unit(ObForOracleParser.Program_unitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#program_unit}.
	 * @param ctx the parse tree
	 */
	void exitProgram_unit(ObForOracleParser.Program_unitContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_dimension}.
	 * @param ctx the parse tree
	 */
	void enterCreate_dimension(ObForOracleParser.Create_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_dimension}.
	 * @param ctx the parse tree
	 */
	void exitCreate_dimension(ObForOracleParser.Create_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_directory}.
	 * @param ctx the parse tree
	 */
	void enterCreate_directory(ObForOracleParser.Create_directoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_directory}.
	 * @param ctx the parse tree
	 */
	void exitCreate_directory(ObForOracleParser.Create_directoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#directory_name}.
	 * @param ctx the parse tree
	 */
	void enterDirectory_name(ObForOracleParser.Directory_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#directory_name}.
	 * @param ctx the parse tree
	 */
	void exitDirectory_name(ObForOracleParser.Directory_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#directory_path}.
	 * @param ctx the parse tree
	 */
	void enterDirectory_path(ObForOracleParser.Directory_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#directory_path}.
	 * @param ctx the parse tree
	 */
	void exitDirectory_path(ObForOracleParser.Directory_pathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void enterCreate_inmemory_join_group(ObForOracleParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_inmemory_join_group}.
	 * @param ctx the parse tree
	 */
	void exitCreate_inmemory_join_group(ObForOracleParser.Create_inmemory_join_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterDrop_hierarchy(ObForOracleParser.Drop_hierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitDrop_hierarchy(ObForOracleParser.Drop_hierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_library}.
	 * @param ctx the parse tree
	 */
	void enterAlter_library(ObForOracleParser.Alter_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_library}.
	 * @param ctx the parse tree
	 */
	void exitAlter_library(ObForOracleParser.Alter_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_java}.
	 * @param ctx the parse tree
	 */
	void enterDrop_java(ObForOracleParser.Drop_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_java}.
	 * @param ctx the parse tree
	 */
	void exitDrop_java(ObForOracleParser.Drop_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_library}.
	 * @param ctx the parse tree
	 */
	void enterDrop_library(ObForOracleParser.Drop_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_library}.
	 * @param ctx the parse tree
	 */
	void exitDrop_library(ObForOracleParser.Drop_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_java}.
	 * @param ctx the parse tree
	 */
	void enterCreate_java(ObForOracleParser.Create_javaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_java}.
	 * @param ctx the parse tree
	 */
	void exitCreate_java(ObForOracleParser.Create_javaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_library}.
	 * @param ctx the parse tree
	 */
	void enterCreate_library(ObForOracleParser.Create_libraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_library}.
	 * @param ctx the parse tree
	 */
	void exitCreate_library(ObForOracleParser.Create_libraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#plsql_library_source}.
	 * @param ctx the parse tree
	 */
	void enterPlsql_library_source(ObForOracleParser.Plsql_library_sourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#plsql_library_source}.
	 * @param ctx the parse tree
	 */
	void exitPlsql_library_source(ObForOracleParser.Plsql_library_sourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#credential_name}.
	 * @param ctx the parse tree
	 */
	void enterCredential_name(ObForOracleParser.Credential_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#credential_name}.
	 * @param ctx the parse tree
	 */
	void exitCredential_name(ObForOracleParser.Credential_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#library_editionable}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_editionable(ObForOracleParser.Library_editionableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#library_editionable}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_editionable(ObForOracleParser.Library_editionableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#library_debug}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_debug(ObForOracleParser.Library_debugContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#library_debug}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_debug(ObForOracleParser.Library_debugContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void enterCompiler_parameters_clause(ObForOracleParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compiler_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void exitCompiler_parameters_clause(ObForOracleParser.Compiler_parameters_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parameter_value}.
	 * @param ctx the parse tree
	 */
	void enterParameter_value(ObForOracleParser.Parameter_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parameter_value}.
	 * @param ctx the parse tree
	 */
	void exitParameter_value(ObForOracleParser.Parameter_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#library_name}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_name(ObForOracleParser.Library_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#library_name}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_name(ObForOracleParser.Library_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_dimension}.
	 * @param ctx the parse tree
	 */
	void enterAlter_dimension(ObForOracleParser.Alter_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_dimension}.
	 * @param ctx the parse tree
	 */
	void exitAlter_dimension(ObForOracleParser.Alter_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#level_clause}.
	 * @param ctx the parse tree
	 */
	void enterLevel_clause(ObForOracleParser.Level_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#level_clause}.
	 * @param ctx the parse tree
	 */
	void exitLevel_clause(ObForOracleParser.Level_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchy_clause(ObForOracleParser.Hierarchy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hierarchy_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchy_clause(ObForOracleParser.Hierarchy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 */
	void enterDimension_join_clause(ObForOracleParser.Dimension_join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dimension_join_clause}.
	 * @param ctx the parse tree
	 */
	void exitDimension_join_clause(ObForOracleParser.Dimension_join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#attribute_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_clause(ObForOracleParser.Attribute_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#attribute_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_clause(ObForOracleParser.Attribute_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 */
	void enterExtended_attribute_clause(ObForOracleParser.Extended_attribute_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#extended_attribute_clause}.
	 * @param ctx the parse tree
	 */
	void exitExtended_attribute_clause(ObForOracleParser.Extended_attribute_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterColumn_one_or_more_sub_clause(ObForOracleParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_one_or_more_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitColumn_one_or_more_sub_clause(ObForOracleParser.Column_one_or_more_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_view(ObForOracleParser.Alter_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_view(ObForOracleParser.Alter_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 */
	void enterAlter_view_editionable(ObForOracleParser.Alter_view_editionableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_view_editionable}.
	 * @param ctx the parse tree
	 */
	void exitAlter_view_editionable(ObForOracleParser.Alter_view_editionableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view(ObForOracleParser.Create_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view(ObForOracleParser.Create_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#editioning_clause}.
	 * @param ctx the parse tree
	 */
	void enterEditioning_clause(ObForOracleParser.Editioning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#editioning_clause}.
	 * @param ctx the parse tree
	 */
	void exitEditioning_clause(ObForOracleParser.Editioning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#view_options}.
	 * @param ctx the parse tree
	 */
	void enterView_options(ObForOracleParser.View_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#view_options}.
	 * @param ctx the parse tree
	 */
	void exitView_options(ObForOracleParser.View_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 */
	void enterView_alias_constraint(ObForOracleParser.View_alias_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#view_alias_constraint}.
	 * @param ctx the parse tree
	 */
	void exitView_alias_constraint(ObForOracleParser.View_alias_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_view_clause}.
	 * @param ctx the parse tree
	 */
	void enterObject_view_clause(ObForOracleParser.Object_view_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_view_clause}.
	 * @param ctx the parse tree
	 */
	void exitObject_view_clause(ObForOracleParser.Object_view_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inline_constraint}.
	 * @param ctx the parse tree
	 */
	void enterInline_constraint(ObForOracleParser.Inline_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inline_constraint}.
	 * @param ctx the parse tree
	 */
	void exitInline_constraint(ObForOracleParser.Inline_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unique_}.
	 * @param ctx the parse tree
	 */
	void enterUnique_(ObForOracleParser.Unique_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unique_}.
	 * @param ctx the parse tree
	 */
	void exitUnique_(ObForOracleParser.Unique_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#primary_key_}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key_(ObForOracleParser.Primary_key_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#primary_key_}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key_(ObForOracleParser.Primary_key_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#null_}.
	 * @param ctx the parse tree
	 */
	void enterNull_(ObForOracleParser.Null_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#null_}.
	 * @param ctx the parse tree
	 */
	void exitNull_(ObForOracleParser.Null_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#not_null_}.
	 * @param ctx the parse tree
	 */
	void enterNot_null_(ObForOracleParser.Not_null_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#not_null_}.
	 * @param ctx the parse tree
	 */
	void exitNot_null_(ObForOracleParser.Not_null_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterInline_ref_constraint(ObForOracleParser.Inline_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inline_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitInline_ref_constraint(ObForOracleParser.Inline_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_ref_constraint(ObForOracleParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#out_of_line_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_ref_constraint(ObForOracleParser.Out_of_line_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_constraint(ObForOracleParser.Out_of_line_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#out_of_line_constraint}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_constraint(ObForOracleParser.Out_of_line_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constraint_column_list}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_column_list(ObForOracleParser.Constraint_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constraint_column_list}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_column_list(ObForOracleParser.Constraint_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constraint_state}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_state(ObForOracleParser.Constraint_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constraint_state}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_state(ObForOracleParser.Constraint_stateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_view_clause(ObForOracleParser.Xmltype_view_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltype_view_clause}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_view_clause(ObForOracleParser.Xmltype_view_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 */
	void enterXml_schema_spec(ObForOracleParser.Xml_schema_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_schema_spec}.
	 * @param ctx the parse tree
	 */
	void exitXml_schema_spec(ObForOracleParser.Xml_schema_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_schema_url}.
	 * @param ctx the parse tree
	 */
	void enterXml_schema_url(ObForOracleParser.Xml_schema_urlContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_schema_url}.
	 * @param ctx the parse tree
	 */
	void exitXml_schema_url(ObForOracleParser.Xml_schema_urlContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(ObForOracleParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(ObForOracleParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace(ObForOracleParser.Alter_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace(ObForOracleParser.Alter_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_tempfile_clauses(ObForOracleParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datafile_tempfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_tempfile_clauses(ObForOracleParser.Datafile_tempfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_logging_clauses(ObForOracleParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_logging_clauses(ObForOracleParser.Tablespace_logging_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_group_clause(ObForOracleParser.Tablespace_group_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_group_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_group_clause(ObForOracleParser.Tablespace_group_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_group_name(ObForOracleParser.Tablespace_group_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_group_name}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_group_name(ObForOracleParser.Tablespace_group_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_state_clauses(ObForOracleParser.Tablespace_state_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_state_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_state_clauses(ObForOracleParser.Tablespace_state_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_mode_clause(ObForOracleParser.Flashback_mode_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_mode_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_mode_clause(ObForOracleParser.Flashback_mode_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_tablespace_name(ObForOracleParser.New_tablespace_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_tablespace_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_tablespace_name(ObForOracleParser.New_tablespace_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterCreate_tablespace(ObForOracleParser.Create_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitCreate_tablespace(ObForOracleParser.Create_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterPermanent_tablespace_clause(ObForOracleParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#permanent_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitPermanent_tablespace_clause(ObForOracleParser.Permanent_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_encryption_spec(ObForOracleParser.Tablespace_encryption_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_encryption_spec}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_encryption_spec(ObForOracleParser.Tablespace_encryption_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logging_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogging_clause(ObForOracleParser.Logging_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logging_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogging_clause(ObForOracleParser.Logging_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#extent_management_clause}.
	 * @param ctx the parse tree
	 */
	void enterExtent_management_clause(ObForOracleParser.Extent_management_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#extent_management_clause}.
	 * @param ctx the parse tree
	 */
	void exitExtent_management_clause(ObForOracleParser.Extent_management_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#segment_management_clause}.
	 * @param ctx the parse tree
	 */
	void enterSegment_management_clause(ObForOracleParser.Segment_management_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#segment_management_clause}.
	 * @param ctx the parse tree
	 */
	void exitSegment_management_clause(ObForOracleParser.Segment_management_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_tablespace_clause(ObForOracleParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#temporary_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_tablespace_clause(ObForOracleParser.Temporary_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndo_tablespace_clause(ObForOracleParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#undo_tablespace_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndo_tablespace_clause(ObForOracleParser.Undo_tablespace_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_retention_clause(ObForOracleParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_retention_clause(ObForOracleParser.Tablespace_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterCreate_tablespace_set(ObForOracleParser.Create_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitCreate_tablespace_set(ObForOracleParser.Create_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void enterPermanent_tablespace_attrs(ObForOracleParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#permanent_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void exitPermanent_tablespace_attrs(ObForOracleParser.Permanent_tablespace_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_encryption_clause(ObForOracleParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_encryption_clause}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_encryption_clause(ObForOracleParser.Tablespace_encryption_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 */
	void enterDefault_tablespace_params(ObForOracleParser.Default_tablespace_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_tablespace_params}.
	 * @param ctx the parse tree
	 */
	void exitDefault_tablespace_params(ObForOracleParser.Default_tablespace_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_table_compression}.
	 * @param ctx the parse tree
	 */
	void enterDefault_table_compression(ObForOracleParser.Default_table_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_table_compression}.
	 * @param ctx the parse tree
	 */
	void exitDefault_table_compression(ObForOracleParser.Default_table_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#low_high}.
	 * @param ctx the parse tree
	 */
	void enterLow_high(ObForOracleParser.Low_highContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#low_high}.
	 * @param ctx the parse tree
	 */
	void exitLow_high(ObForOracleParser.Low_highContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_index_compression}.
	 * @param ctx the parse tree
	 */
	void enterDefault_index_compression(ObForOracleParser.Default_index_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_index_compression}.
	 * @param ctx the parse tree
	 */
	void exitDefault_index_compression(ObForOracleParser.Default_index_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmmemory_clause(ObForOracleParser.Inmmemory_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmmemory_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmmemory_clause(ObForOracleParser.Inmmemory_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datafile_specification}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_specification(ObForOracleParser.Datafile_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datafile_specification}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_specification(ObForOracleParser.Datafile_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tempfile_specification}.
	 * @param ctx the parse tree
	 */
	void enterTempfile_specification(ObForOracleParser.Tempfile_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tempfile_specification}.
	 * @param ctx the parse tree
	 */
	void exitTempfile_specification(ObForOracleParser.Tempfile_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 */
	void enterDatafile_tempfile_spec(ObForOracleParser.Datafile_tempfile_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datafile_tempfile_spec}.
	 * @param ctx the parse tree
	 */
	void exitDatafile_tempfile_spec(ObForOracleParser.Datafile_tempfile_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 */
	void enterRedo_log_file_spec(ObForOracleParser.Redo_log_file_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#redo_log_file_spec}.
	 * @param ctx the parse tree
	 */
	void exitRedo_log_file_spec(ObForOracleParser.Redo_log_file_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#autoextend_clause}.
	 * @param ctx the parse tree
	 */
	void enterAutoextend_clause(ObForOracleParser.Autoextend_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#autoextend_clause}.
	 * @param ctx the parse tree
	 */
	void exitAutoextend_clause(ObForOracleParser.Autoextend_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#maxsize_clause}.
	 * @param ctx the parse tree
	 */
	void enterMaxsize_clause(ObForOracleParser.Maxsize_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#maxsize_clause}.
	 * @param ctx the parse tree
	 */
	void exitMaxsize_clause(ObForOracleParser.Maxsize_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#build_clause}.
	 * @param ctx the parse tree
	 */
	void enterBuild_clause(ObForOracleParser.Build_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#build_clause}.
	 * @param ctx the parse tree
	 */
	void exitBuild_clause(ObForOracleParser.Build_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parallel_clause}.
	 * @param ctx the parse tree
	 */
	void enterParallel_clause(ObForOracleParser.Parallel_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parallel_clause}.
	 * @param ctx the parse tree
	 */
	void exitParallel_clause(ObForOracleParser.Parallel_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view(ObForOracleParser.Alter_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view(ObForOracleParser.Alter_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mv_option1(ObForOracleParser.Alter_mv_option1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_mv_option1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mv_option1(ObForOracleParser.Alter_mv_option1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mv_refresh(ObForOracleParser.Alter_mv_refreshContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mv_refresh(ObForOracleParser.Alter_mv_refreshContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rollback_segment}.
	 * @param ctx the parse tree
	 */
	void enterRollback_segment(ObForOracleParser.Rollback_segmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rollback_segment}.
	 * @param ctx the parse tree
	 */
	void exitRollback_segment(ObForOracleParser.Rollback_segmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_mv_column_clause(ObForOracleParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_mv_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_mv_column_clause(ObForOracleParser.Modify_mv_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_view_log(ObForOracleParser.Alter_materialized_view_logContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_view_log(ObForOracleParser.Alter_materialized_view_logContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_mv_log_column_clause(ObForOracleParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_mv_log_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_mv_log_column_clause(ObForOracleParser.Add_mv_log_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_mv_log_clause(ObForOracleParser.Move_mv_log_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#move_mv_log_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_mv_log_clause(ObForOracleParser.Move_mv_log_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 */
	void enterMv_log_augmentation(ObForOracleParser.Mv_log_augmentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mv_log_augmentation}.
	 * @param ctx the parse tree
	 */
	void exitMv_log_augmentation(ObForOracleParser.Mv_log_augmentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datetime_expr}.
	 * @param ctx the parse tree
	 */
	void enterDatetime_expr(ObForOracleParser.Datetime_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datetime_expr}.
	 * @param ctx the parse tree
	 */
	void exitDatetime_expr(ObForOracleParser.Datetime_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#interval_expr}.
	 * @param ctx the parse tree
	 */
	void enterInterval_expr(ObForOracleParser.Interval_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#interval_expr}.
	 * @param ctx the parse tree
	 */
	void exitInterval_expr(ObForOracleParser.Interval_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 */
	void enterSynchronous_or_asynchronous(ObForOracleParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#synchronous_or_asynchronous}.
	 * @param ctx the parse tree
	 */
	void exitSynchronous_or_asynchronous(ObForOracleParser.Synchronous_or_asynchronousContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#including_or_excluding}.
	 * @param ctx the parse tree
	 */
	void enterIncluding_or_excluding(ObForOracleParser.Including_or_excludingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#including_or_excluding}.
	 * @param ctx the parse tree
	 */
	void exitIncluding_or_excluding(ObForOracleParser.Including_or_excludingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_view_log(ObForOracleParser.Create_materialized_view_logContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_materialized_view_log}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_view_log(ObForOracleParser.Create_materialized_view_logContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterNew_values_clause(ObForOracleParser.New_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitNew_values_clause(ObForOracleParser.New_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 */
	void enterMv_log_purge_clause(ObForOracleParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mv_log_purge_clause}.
	 * @param ctx the parse tree
	 */
	void exitMv_log_purge_clause(ObForOracleParser.Mv_log_purge_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_zonemap(ObForOracleParser.Create_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_zonemap(ObForOracleParser.Create_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterAlter_materialized_zonemap(ObForOracleParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitAlter_materialized_zonemap(ObForOracleParser.Alter_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void enterDrop_materialized_zonemap(ObForOracleParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_materialized_zonemap}.
	 * @param ctx the parse tree
	 */
	void exitDrop_materialized_zonemap(ObForOracleParser.Drop_materialized_zonemapContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_refresh_clause(ObForOracleParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#zonemap_refresh_clause}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_refresh_clause(ObForOracleParser.Zonemap_refresh_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_attributes(ObForOracleParser.Zonemap_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#zonemap_attributes}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_attributes(ObForOracleParser.Zonemap_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#zonemap_name}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_name(ObForOracleParser.Zonemap_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#zonemap_name}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_name(ObForOracleParser.Zonemap_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#operator_name}.
	 * @param ctx the parse tree
	 */
	void enterOperator_name(ObForOracleParser.Operator_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#operator_name}.
	 * @param ctx the parse tree
	 */
	void exitOperator_name(ObForOracleParser.Operator_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#operator_function_name}.
	 * @param ctx the parse tree
	 */
	void enterOperator_function_name(ObForOracleParser.Operator_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#operator_function_name}.
	 * @param ctx the parse tree
	 */
	void exitOperator_function_name(ObForOracleParser.Operator_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 */
	void enterCreate_zonemap_on_table(ObForOracleParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_zonemap_on_table}.
	 * @param ctx the parse tree
	 */
	void exitCreate_zonemap_on_table(ObForOracleParser.Create_zonemap_on_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 */
	void enterCreate_zonemap_as_subquery(ObForOracleParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_zonemap_as_subquery}.
	 * @param ctx the parse tree
	 */
	void exitCreate_zonemap_as_subquery(ObForOracleParser.Create_zonemap_as_subqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_operator}.
	 * @param ctx the parse tree
	 */
	void enterAlter_operator(ObForOracleParser.Alter_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_operator}.
	 * @param ctx the parse tree
	 */
	void exitAlter_operator(ObForOracleParser.Alter_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_operator}.
	 * @param ctx the parse tree
	 */
	void enterDrop_operator(ObForOracleParser.Drop_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_operator}.
	 * @param ctx the parse tree
	 */
	void exitDrop_operator(ObForOracleParser.Drop_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_operator}.
	 * @param ctx the parse tree
	 */
	void enterCreate_operator(ObForOracleParser.Create_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_operator}.
	 * @param ctx the parse tree
	 */
	void exitCreate_operator(ObForOracleParser.Create_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterBinding_clause(ObForOracleParser.Binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitBinding_clause(ObForOracleParser.Binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_binding_clause(ObForOracleParser.Add_binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_binding_clause(ObForOracleParser.Add_binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#implementation_clause}.
	 * @param ctx the parse tree
	 */
	void enterImplementation_clause(ObForOracleParser.Implementation_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#implementation_clause}.
	 * @param ctx the parse tree
	 */
	void exitImplementation_clause(ObForOracleParser.Implementation_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#primary_operator_list}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_operator_list(ObForOracleParser.Primary_operator_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#primary_operator_list}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_operator_list(ObForOracleParser.Primary_operator_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#primary_operator_item}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_operator_item(ObForOracleParser.Primary_operator_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#primary_operator_item}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_operator_item(ObForOracleParser.Primary_operator_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#operator_context_clause}.
	 * @param ctx the parse tree
	 */
	void enterOperator_context_clause(ObForOracleParser.Operator_context_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#operator_context_clause}.
	 * @param ctx the parse tree
	 */
	void exitOperator_context_clause(ObForOracleParser.Operator_context_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_function_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_function_clause(ObForOracleParser.Using_function_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_function_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_function_clause(ObForOracleParser.Using_function_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_binding_clause(ObForOracleParser.Drop_binding_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_binding_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_binding_clause(ObForOracleParser.Drop_binding_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterCreate_materialized_view(ObForOracleParser.Create_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitCreate_materialized_view(ObForOracleParser.Create_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void enterScoped_table_ref_constraint(ObForOracleParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#scoped_table_ref_constraint}.
	 * @param ctx the parse tree
	 */
	void exitScoped_table_ref_constraint(ObForOracleParser.Scoped_table_ref_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mv_column_alias}.
	 * @param ctx the parse tree
	 */
	void enterMv_column_alias(ObForOracleParser.Mv_column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mv_column_alias}.
	 * @param ctx the parse tree
	 */
	void exitMv_column_alias(ObForOracleParser.Mv_column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void enterCreate_mv_refresh(ObForOracleParser.Create_mv_refreshContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_mv_refresh}.
	 * @param ctx the parse tree
	 */
	void exitCreate_mv_refresh(ObForOracleParser.Create_mv_refreshContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_materialized_view(ObForOracleParser.Drop_materialized_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_materialized_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_materialized_view(ObForOracleParser.Drop_materialized_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_context}.
	 * @param ctx the parse tree
	 */
	void enterCreate_context(ObForOracleParser.Create_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_context}.
	 * @param ctx the parse tree
	 */
	void exitCreate_context(ObForOracleParser.Create_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#oracle_namespace}.
	 * @param ctx the parse tree
	 */
	void enterOracle_namespace(ObForOracleParser.Oracle_namespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#oracle_namespace}.
	 * @param ctx the parse tree
	 */
	void exitOracle_namespace(ObForOracleParser.Oracle_namespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_cluster}.
	 * @param ctx the parse tree
	 */
	void enterCreate_cluster(ObForOracleParser.Create_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_cluster}.
	 * @param ctx the parse tree
	 */
	void exitCreate_cluster(ObForOracleParser.Create_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_profile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_profile(ObForOracleParser.Create_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_profile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_profile(ObForOracleParser.Create_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#resource_parameters}.
	 * @param ctx the parse tree
	 */
	void enterResource_parameters(ObForOracleParser.Resource_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#resource_parameters}.
	 * @param ctx the parse tree
	 */
	void exitResource_parameters(ObForOracleParser.Resource_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#password_parameters}.
	 * @param ctx the parse tree
	 */
	void enterPassword_parameters(ObForOracleParser.Password_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#password_parameters}.
	 * @param ctx the parse tree
	 */
	void exitPassword_parameters(ObForOracleParser.Password_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_lockdown_profile(ObForOracleParser.Create_lockdown_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_lockdown_profile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_lockdown_profile(ObForOracleParser.Create_lockdown_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#static_base_profile}.
	 * @param ctx the parse tree
	 */
	void enterStatic_base_profile(ObForOracleParser.Static_base_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#static_base_profile}.
	 * @param ctx the parse tree
	 */
	void exitStatic_base_profile(ObForOracleParser.Static_base_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 */
	void enterDynamic_base_profile(ObForOracleParser.Dynamic_base_profileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dynamic_base_profile}.
	 * @param ctx the parse tree
	 */
	void exitDynamic_base_profile(ObForOracleParser.Dynamic_base_profileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_outline}.
	 * @param ctx the parse tree
	 */
	void enterCreate_outline(ObForOracleParser.Create_outlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_outline}.
	 * @param ctx the parse tree
	 */
	void exitCreate_outline(ObForOracleParser.Create_outlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_restore_point}.
	 * @param ctx the parse tree
	 */
	void enterCreate_restore_point(ObForOracleParser.Create_restore_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_restore_point}.
	 * @param ctx the parse tree
	 */
	void exitCreate_restore_point(ObForOracleParser.Create_restore_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_role}.
	 * @param ctx the parse tree
	 */
	void enterCreate_role(ObForOracleParser.Create_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_role}.
	 * @param ctx the parse tree
	 */
	void exitCreate_role(ObForOracleParser.Create_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_table}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table(ObForOracleParser.Create_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_table}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table(ObForOracleParser.Create_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_(ObForOracleParser.Temporary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_(ObForOracleParser.Temporary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltype_table}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_table(ObForOracleParser.Xmltype_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltype_table}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_table(ObForOracleParser.Xmltype_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_virtual_columns(ObForOracleParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltype_virtual_columns}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_virtual_columns(ObForOracleParser.Xmltype_virtual_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_column_properties(ObForOracleParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltype_column_properties}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_column_properties(ObForOracleParser.Xmltype_column_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltype_storage}.
	 * @param ctx the parse tree
	 */
	void enterXmltype_storage(ObForOracleParser.Xmltype_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltype_storage}.
	 * @param ctx the parse tree
	 */
	void exitXmltype_storage(ObForOracleParser.Xmltype_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 */
	void enterXmlschema_spec(ObForOracleParser.Xmlschema_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlschema_spec}.
	 * @param ctx the parse tree
	 */
	void exitXmlschema_spec(ObForOracleParser.Xmlschema_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_table}.
	 * @param ctx the parse tree
	 */
	void enterObject_table(ObForOracleParser.Object_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_table}.
	 * @param ctx the parse tree
	 */
	void exitObject_table(ObForOracleParser.Object_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_type}.
	 * @param ctx the parse tree
	 */
	void enterObject_type(ObForOracleParser.Object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_type}.
	 * @param ctx the parse tree
	 */
	void exitObject_type(ObForOracleParser.Object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#oid_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterOid_index_clause(ObForOracleParser.Oid_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#oid_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitOid_index_clause(ObForOracleParser.Oid_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#oid_clause}.
	 * @param ctx the parse tree
	 */
	void enterOid_clause(ObForOracleParser.Oid_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#oid_clause}.
	 * @param ctx the parse tree
	 */
	void exitOid_clause(ObForOracleParser.Oid_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_properties}.
	 * @param ctx the parse tree
	 */
	void enterObject_properties(ObForOracleParser.Object_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_properties}.
	 * @param ctx the parse tree
	 */
	void exitObject_properties(ObForOracleParser.Object_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_table_substitution}.
	 * @param ctx the parse tree
	 */
	void enterObject_table_substitution(ObForOracleParser.Object_table_substitutionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_table_substitution}.
	 * @param ctx the parse tree
	 */
	void exitObject_table_substitution(ObForOracleParser.Object_table_substitutionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#relational_table}.
	 * @param ctx the parse tree
	 */
	void enterRelational_table(ObForOracleParser.Relational_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#relational_table}.
	 * @param ctx the parse tree
	 */
	void exitRelational_table(ObForOracleParser.Relational_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_clauses(ObForOracleParser.Immutable_table_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#immutable_table_clauses}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_clauses(ObForOracleParser.Immutable_table_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_no_drop_clause(ObForOracleParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#immutable_table_no_drop_clause}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_no_drop_clause(ObForOracleParser.Immutable_table_no_drop_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterImmutable_table_no_delete_clause(ObForOracleParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#immutable_table_no_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitImmutable_table_no_delete_clause(ObForOracleParser.Immutable_table_no_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_table_clauses(ObForOracleParser.Blockchain_table_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#blockchain_table_clauses}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_table_clauses(ObForOracleParser.Blockchain_table_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_drop_table_clause(ObForOracleParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#blockchain_drop_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_drop_table_clause(ObForOracleParser.Blockchain_drop_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_row_retention_clause(ObForOracleParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#blockchain_row_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_row_retention_clause(ObForOracleParser.Blockchain_row_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 */
	void enterBlockchain_hash_and_data_format_clause(ObForOracleParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#blockchain_hash_and_data_format_clause}.
	 * @param ctx the parse tree
	 */
	void exitBlockchain_hash_and_data_format_clause(ObForOracleParser.Blockchain_hash_and_data_format_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void enterCollation_name(ObForOracleParser.Collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void exitCollation_name(ObForOracleParser.Collation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_properties}.
	 * @param ctx the parse tree
	 */
	void enterTable_properties(ObForOracleParser.Table_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_properties}.
	 * @param ctx the parse tree
	 */
	void exitTable_properties(ObForOracleParser.Table_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#read_only_clause}.
	 * @param ctx the parse tree
	 */
	void enterRead_only_clause(ObForOracleParser.Read_only_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#read_only_clause}.
	 * @param ctx the parse tree
	 */
	void exitRead_only_clause(ObForOracleParser.Read_only_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#indexing_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndexing_clause(ObForOracleParser.Indexing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#indexing_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndexing_clause(ObForOracleParser.Indexing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_clustering_clause(ObForOracleParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#attribute_clustering_clause}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_clustering_clause(ObForOracleParser.Attribute_clustering_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#clustering_join}.
	 * @param ctx the parse tree
	 */
	void enterClustering_join(ObForOracleParser.Clustering_joinContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#clustering_join}.
	 * @param ctx the parse tree
	 */
	void exitClustering_join(ObForOracleParser.Clustering_joinContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#clustering_join_item}.
	 * @param ctx the parse tree
	 */
	void enterClustering_join_item(ObForOracleParser.Clustering_join_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#clustering_join_item}.
	 * @param ctx the parse tree
	 */
	void exitClustering_join_item(ObForOracleParser.Clustering_join_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#equijoin_condition}.
	 * @param ctx the parse tree
	 */
	void enterEquijoin_condition(ObForOracleParser.Equijoin_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#equijoin_condition}.
	 * @param ctx the parse tree
	 */
	void exitEquijoin_condition(ObForOracleParser.Equijoin_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cluster_clause}.
	 * @param ctx the parse tree
	 */
	void enterCluster_clause(ObForOracleParser.Cluster_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cluster_clause}.
	 * @param ctx the parse tree
	 */
	void exitCluster_clause(ObForOracleParser.Cluster_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#clustering_columns}.
	 * @param ctx the parse tree
	 */
	void enterClustering_columns(ObForOracleParser.Clustering_columnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#clustering_columns}.
	 * @param ctx the parse tree
	 */
	void exitClustering_columns(ObForOracleParser.Clustering_columnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#clustering_column_group}.
	 * @param ctx the parse tree
	 */
	void enterClustering_column_group(ObForOracleParser.Clustering_column_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#clustering_column_group}.
	 * @param ctx the parse tree
	 */
	void exitClustering_column_group(ObForOracleParser.Clustering_column_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#yes_no}.
	 * @param ctx the parse tree
	 */
	void enterYes_no(ObForOracleParser.Yes_noContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#yes_no}.
	 * @param ctx the parse tree
	 */
	void exitYes_no(ObForOracleParser.Yes_noContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#zonemap_clause}.
	 * @param ctx the parse tree
	 */
	void enterZonemap_clause(ObForOracleParser.Zonemap_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#zonemap_clause}.
	 * @param ctx the parse tree
	 */
	void exitZonemap_clause(ObForOracleParser.Zonemap_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 */
	void enterLogical_replication_clause(ObForOracleParser.Logical_replication_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logical_replication_clause}.
	 * @param ctx the parse tree
	 */
	void exitLogical_replication_clause(ObForOracleParser.Logical_replication_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(ObForOracleParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(ObForOracleParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#relational_property}.
	 * @param ctx the parse tree
	 */
	void enterRelational_property(ObForOracleParser.Relational_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#relational_property}.
	 * @param ctx the parse tree
	 */
	void exitRelational_property(ObForOracleParser.Relational_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTable_partitioning_clauses(ObForOracleParser.Table_partitioning_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_partitioning_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTable_partitioning_clauses(ObForOracleParser.Table_partitioning_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#range_partitions}.
	 * @param ctx the parse tree
	 */
	void enterRange_partitions(ObForOracleParser.Range_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#range_partitions}.
	 * @param ctx the parse tree
	 */
	void exitRange_partitions(ObForOracleParser.Range_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#list_partitions}.
	 * @param ctx the parse tree
	 */
	void enterList_partitions(ObForOracleParser.List_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#list_partitions}.
	 * @param ctx the parse tree
	 */
	void exitList_partitions(ObForOracleParser.List_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterHash_partitions(ObForOracleParser.Hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitHash_partitions(ObForOracleParser.Hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterIndividual_hash_partitions(ObForOracleParser.Individual_hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#individual_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitIndividual_hash_partitions(ObForOracleParser.Individual_hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_partitions_by_quantity(ObForOracleParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hash_partitions_by_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_partitions_by_quantity(ObForOracleParser.Hash_partitions_by_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_partition_quantity(ObForOracleParser.Hash_partition_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hash_partition_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_partition_quantity(ObForOracleParser.Hash_partition_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_range_partitions(ObForOracleParser.Composite_range_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#composite_range_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_range_partitions(ObForOracleParser.Composite_range_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_list_partitions(ObForOracleParser.Composite_list_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#composite_list_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_list_partitions(ObForOracleParser.Composite_list_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void enterComposite_hash_partitions(ObForOracleParser.Composite_hash_partitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#composite_hash_partitions}.
	 * @param ctx the parse tree
	 */
	void exitComposite_hash_partitions(ObForOracleParser.Composite_hash_partitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#reference_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterReference_partitioning(ObForOracleParser.Reference_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#reference_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitReference_partitioning(ObForOracleParser.Reference_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterReference_partition_desc(ObForOracleParser.Reference_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#reference_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitReference_partition_desc(ObForOracleParser.Reference_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#system_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterSystem_partitioning(ObForOracleParser.System_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#system_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitSystem_partitioning(ObForOracleParser.System_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#range_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterRange_partition_desc(ObForOracleParser.Range_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#range_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitRange_partition_desc(ObForOracleParser.Range_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#list_partition_desc}.
	 * @param ctx the parse tree
	 */
	void enterList_partition_desc(ObForOracleParser.List_partition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#list_partition_desc}.
	 * @param ctx the parse tree
	 */
	void exitList_partition_desc(ObForOracleParser.List_partition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_template}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_template(ObForOracleParser.Subpartition_templateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_template}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_template(ObForOracleParser.Subpartition_templateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_subpartition_quantity(ObForOracleParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hash_subpartition_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_subpartition_quantity(ObForOracleParser.Hash_subpartition_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_range(ObForOracleParser.Subpartition_by_rangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_by_range}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_range(ObForOracleParser.Subpartition_by_rangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_list(ObForOracleParser.Subpartition_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_by_list}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_list(ObForOracleParser.Subpartition_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_by_hash(ObForOracleParser.Subpartition_by_hashContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_by_hash}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_by_hash(ObForOracleParser.Subpartition_by_hashContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_name}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_name(ObForOracleParser.Subpartition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_name}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_name(ObForOracleParser.Subpartition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void enterRange_subpartition_desc(ObForOracleParser.Range_subpartition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#range_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void exitRange_subpartition_desc(ObForOracleParser.Range_subpartition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void enterList_subpartition_desc(ObForOracleParser.List_subpartition_descContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#list_subpartition_desc}.
	 * @param ctx the parse tree
	 */
	void exitList_subpartition_desc(ObForOracleParser.List_subpartition_descContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 */
	void enterIndividual_hash_subparts(ObForOracleParser.Individual_hash_subpartsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#individual_hash_subparts}.
	 * @param ctx the parse tree
	 */
	void exitIndividual_hash_subparts(ObForOracleParser.Individual_hash_subpartsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 */
	void enterHash_subparts_by_quantity(ObForOracleParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hash_subparts_by_quantity}.
	 * @param ctx the parse tree
	 */
	void exitHash_subparts_by_quantity(ObForOracleParser.Hash_subparts_by_quantityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#range_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterRange_values_clause(ObForOracleParser.Range_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#range_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitRange_values_clause(ObForOracleParser.Range_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#range_values_list}.
	 * @param ctx the parse tree
	 */
	void enterRange_values_list(ObForOracleParser.Range_values_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#range_values_list}.
	 * @param ctx the parse tree
	 */
	void exitRange_values_list(ObForOracleParser.Range_values_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#list_values_clause}.
	 * @param ctx the parse tree
	 */
	void enterList_values_clause(ObForOracleParser.List_values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#list_values_clause}.
	 * @param ctx the parse tree
	 */
	void exitList_values_clause(ObForOracleParser.List_values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_partition_description}.
	 * @param ctx the parse tree
	 */
	void enterTable_partition_description(ObForOracleParser.Table_partition_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_partition_description}.
	 * @param ctx the parse tree
	 */
	void exitTable_partition_description(ObForOracleParser.Table_partition_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartitioning_storage_clause(ObForOracleParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partitioning_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartitioning_storage_clause(ObForOracleParser.Partitioning_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 */
	void enterLob_partitioning_storage(ObForOracleParser.Lob_partitioning_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_partitioning_storage}.
	 * @param ctx the parse tree
	 */
	void exitLob_partitioning_storage(ObForOracleParser.Lob_partitioning_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 */
	void enterDatatype_null_enable(ObForOracleParser.Datatype_null_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datatype_null_enable}.
	 * @param ctx the parse tree
	 */
	void exitDatatype_null_enable(ObForOracleParser.Datatype_null_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#size_clause}.
	 * @param ctx the parse tree
	 */
	void enterSize_clause(ObForOracleParser.Size_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#size_clause}.
	 * @param ctx the parse tree
	 */
	void exitSize_clause(ObForOracleParser.Size_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_compression}.
	 * @param ctx the parse tree
	 */
	void enterTable_compression(ObForOracleParser.Table_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_compression}.
	 * @param ctx the parse tree
	 */
	void exitTable_compression(ObForOracleParser.Table_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_table_clause(ObForOracleParser.Inmemory_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_table_clause(ObForOracleParser.Inmemory_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_attributes(ObForOracleParser.Inmemory_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_attributes}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_attributes(ObForOracleParser.Inmemory_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_memcompress(ObForOracleParser.Inmemory_memcompressContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_memcompress}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_memcompress(ObForOracleParser.Inmemory_memcompressContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_priority}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_priority(ObForOracleParser.Inmemory_priorityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_priority}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_priority(ObForOracleParser.Inmemory_priorityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_distribute(ObForOracleParser.Inmemory_distributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_distribute}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_distribute(ObForOracleParser.Inmemory_distributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_duplicate(ObForOracleParser.Inmemory_duplicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_duplicate}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_duplicate(ObForOracleParser.Inmemory_duplicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterInmemory_column_clause(ObForOracleParser.Inmemory_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inmemory_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitInmemory_column_clause(ObForOracleParser.Inmemory_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterStorage_clause(ObForOracleParser.Storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitStorage_clause(ObForOracleParser.Storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 */
	void enterDeferred_segment_creation(ObForOracleParser.Deferred_segment_creationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#deferred_segment_creation}.
	 * @param ctx the parse tree
	 */
	void exitDeferred_segment_creation(ObForOracleParser.Deferred_segment_creationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterSegment_attributes_clause(ObForOracleParser.Segment_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#segment_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitSegment_attributes_clause(ObForOracleParser.Segment_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#physical_properties}.
	 * @param ctx the parse tree
	 */
	void enterPhysical_properties(ObForOracleParser.Physical_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#physical_properties}.
	 * @param ctx the parse tree
	 */
	void exitPhysical_properties(ObForOracleParser.Physical_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_clause}.
	 * @param ctx the parse tree
	 */
	void enterIlm_clause(ObForOracleParser.Ilm_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_clause}.
	 * @param ctx the parse tree
	 */
	void exitIlm_clause(ObForOracleParser.Ilm_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 */
	void enterIlm_policy_clause(ObForOracleParser.Ilm_policy_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_policy_clause}.
	 * @param ctx the parse tree
	 */
	void exitIlm_policy_clause(ObForOracleParser.Ilm_policy_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_compression_policy(ObForOracleParser.Ilm_compression_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_compression_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_compression_policy(ObForOracleParser.Ilm_compression_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_tiering_policy(ObForOracleParser.Ilm_tiering_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_tiering_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_tiering_policy(ObForOracleParser.Ilm_tiering_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_after_on}.
	 * @param ctx the parse tree
	 */
	void enterIlm_after_on(ObForOracleParser.Ilm_after_onContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_after_on}.
	 * @param ctx the parse tree
	 */
	void exitIlm_after_on(ObForOracleParser.Ilm_after_onContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#segment_group}.
	 * @param ctx the parse tree
	 */
	void enterSegment_group(ObForOracleParser.Segment_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#segment_group}.
	 * @param ctx the parse tree
	 */
	void exitSegment_group(ObForOracleParser.Segment_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 */
	void enterIlm_inmemory_policy(ObForOracleParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_inmemory_policy}.
	 * @param ctx the parse tree
	 */
	void exitIlm_inmemory_policy(ObForOracleParser.Ilm_inmemory_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ilm_time_period}.
	 * @param ctx the parse tree
	 */
	void enterIlm_time_period(ObForOracleParser.Ilm_time_periodContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ilm_time_period}.
	 * @param ctx the parse tree
	 */
	void exitIlm_time_period(ObForOracleParser.Ilm_time_periodContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterHeap_org_table_clause(ObForOracleParser.Heap_org_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#heap_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitHeap_org_table_clause(ObForOracleParser.Heap_org_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#external_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterExternal_table_clause(ObForOracleParser.External_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#external_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitExternal_table_clause(ObForOracleParser.External_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#access_driver_type}.
	 * @param ctx the parse tree
	 */
	void enterAccess_driver_type(ObForOracleParser.Access_driver_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#access_driver_type}.
	 * @param ctx the parse tree
	 */
	void exitAccess_driver_type(ObForOracleParser.Access_driver_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#external_table_data_props}.
	 * @param ctx the parse tree
	 */
	void enterExternal_table_data_props(ObForOracleParser.External_table_data_propsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#external_table_data_props}.
	 * @param ctx the parse tree
	 */
	void exitExternal_table_data_props(ObForOracleParser.External_table_data_propsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 */
	void enterOpaque_format_spec(ObForOracleParser.Opaque_format_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#opaque_format_spec}.
	 * @param ctx the parse tree
	 */
	void exitOpaque_format_spec(ObForOracleParser.Opaque_format_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#row_movement_clause}.
	 * @param ctx the parse tree
	 */
	void enterRow_movement_clause(ObForOracleParser.Row_movement_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#row_movement_clause}.
	 * @param ctx the parse tree
	 */
	void exitRow_movement_clause(ObForOracleParser.Row_movement_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_archive_clause(ObForOracleParser.Flashback_archive_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_archive_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_archive_clause(ObForOracleParser.Flashback_archive_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#log_grp}.
	 * @param ctx the parse tree
	 */
	void enterLog_grp(ObForOracleParser.Log_grpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#log_grp}.
	 * @param ctx the parse tree
	 */
	void exitLog_grp(ObForOracleParser.Log_grpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_table_logging(ObForOracleParser.Supplemental_table_loggingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_table_logging}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_table_logging(ObForOracleParser.Supplemental_table_loggingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_log_grp_clause(ObForOracleParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_log_grp_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_log_grp_clause(ObForOracleParser.Supplemental_log_grp_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_id_key_clause(ObForOracleParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_id_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_id_key_clause(ObForOracleParser.Supplemental_id_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 */
	void enterAllocate_extent_clause(ObForOracleParser.Allocate_extent_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#allocate_extent_clause}.
	 * @param ctx the parse tree
	 */
	void exitAllocate_extent_clause(ObForOracleParser.Allocate_extent_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 */
	void enterDeallocate_unused_clause(ObForOracleParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#deallocate_unused_clause}.
	 * @param ctx the parse tree
	 */
	void exitDeallocate_unused_clause(ObForOracleParser.Deallocate_unused_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#shrink_clause}.
	 * @param ctx the parse tree
	 */
	void enterShrink_clause(ObForOracleParser.Shrink_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#shrink_clause}.
	 * @param ctx the parse tree
	 */
	void exitShrink_clause(ObForOracleParser.Shrink_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 */
	void enterRecords_per_block_clause(ObForOracleParser.Records_per_block_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#records_per_block_clause}.
	 * @param ctx the parse tree
	 */
	void exitRecords_per_block_clause(ObForOracleParser.Records_per_block_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpgrade_table_clause(ObForOracleParser.Upgrade_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#upgrade_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpgrade_table_clause(ObForOracleParser.Upgrade_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#truncate_table}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_table(ObForOracleParser.Truncate_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#truncate_table}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_table(ObForOracleParser.Truncate_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_table}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table(ObForOracleParser.Drop_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_table}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table(ObForOracleParser.Drop_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDrop_tablespace(ObForOracleParser.Drop_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDrop_tablespace(ObForOracleParser.Drop_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterDrop_tablespace_set(ObForOracleParser.Drop_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitDrop_tablespace_set(ObForOracleParser.Drop_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#including_contents_clause}.
	 * @param ctx the parse tree
	 */
	void enterIncluding_contents_clause(ObForOracleParser.Including_contents_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#including_contents_clause}.
	 * @param ctx the parse tree
	 */
	void exitIncluding_contents_clause(ObForOracleParser.Including_contents_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_view(ObForOracleParser.Drop_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_view(ObForOracleParser.Drop_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#comment_on_column}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_column(ObForOracleParser.Comment_on_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#comment_on_column}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_column(ObForOracleParser.Comment_on_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#enable_or_disable}.
	 * @param ctx the parse tree
	 */
	void enterEnable_or_disable(ObForOracleParser.Enable_or_disableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#enable_or_disable}.
	 * @param ctx the parse tree
	 */
	void exitEnable_or_disable(ObForOracleParser.Enable_or_disableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 */
	void enterAllow_or_disallow(ObForOracleParser.Allow_or_disallowContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#allow_or_disallow}.
	 * @param ctx the parse tree
	 */
	void exitAllow_or_disallow(ObForOracleParser.Allow_or_disallowContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_synonym}.
	 * @param ctx the parse tree
	 */
	void enterAlter_synonym(ObForOracleParser.Alter_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_synonym}.
	 * @param ctx the parse tree
	 */
	void exitAlter_synonym(ObForOracleParser.Alter_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_synonym}.
	 * @param ctx the parse tree
	 */
	void enterCreate_synonym(ObForOracleParser.Create_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_synonym}.
	 * @param ctx the parse tree
	 */
	void exitCreate_synonym(ObForOracleParser.Create_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_synonym}.
	 * @param ctx the parse tree
	 */
	void enterDrop_synonym(ObForOracleParser.Drop_synonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_synonym}.
	 * @param ctx the parse tree
	 */
	void exitDrop_synonym(ObForOracleParser.Drop_synonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_spfile}.
	 * @param ctx the parse tree
	 */
	void enterCreate_spfile(ObForOracleParser.Create_spfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_spfile}.
	 * @param ctx the parse tree
	 */
	void exitCreate_spfile(ObForOracleParser.Create_spfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#spfile_name}.
	 * @param ctx the parse tree
	 */
	void enterSpfile_name(ObForOracleParser.Spfile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#spfile_name}.
	 * @param ctx the parse tree
	 */
	void exitSpfile_name(ObForOracleParser.Spfile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pfile_name}.
	 * @param ctx the parse tree
	 */
	void enterPfile_name(ObForOracleParser.Pfile_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pfile_name}.
	 * @param ctx the parse tree
	 */
	void exitPfile_name(ObForOracleParser.Pfile_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#comment_on_table}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_table(ObForOracleParser.Comment_on_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#comment_on_table}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_table(ObForOracleParser.Comment_on_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#comment_context}.
	 * @param ctx the parse tree
	 */
	void enterComment_context(ObForOracleParser.Comment_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#comment_context}.
	 * @param ctx the parse tree
	 */
	void exitComment_context(ObForOracleParser.Comment_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 */
	void enterComment_on_materialized(ObForOracleParser.Comment_on_materializedContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#comment_on_materialized}.
	 * @param ctx the parse tree
	 */
	void exitComment_on_materialized(ObForOracleParser.Comment_on_materializedContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterAlter_analytic_view(ObForOracleParser.Alter_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitAlter_analytic_view(ObForOracleParser.Alter_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_add_cache_clause(ObForOracleParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_add_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_add_cache_clause(ObForOracleParser.Alter_add_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#levels_item}.
	 * @param ctx the parse tree
	 */
	void enterLevels_item(ObForOracleParser.Levels_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#levels_item}.
	 * @param ctx the parse tree
	 */
	void exitLevels_item(ObForOracleParser.Levels_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#measure_list}.
	 * @param ctx the parse tree
	 */
	void enterMeasure_list(ObForOracleParser.Measure_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#measure_list}.
	 * @param ctx the parse tree
	 */
	void exitMeasure_list(ObForOracleParser.Measure_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_drop_cache_clause(ObForOracleParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_drop_cache_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_drop_cache_clause(ObForOracleParser.Alter_drop_cache_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterAlter_attribute_dimension(ObForOracleParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitAlter_attribute_dimension(ObForOracleParser.Alter_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterAlter_audit_policy(ObForOracleParser.Alter_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitAlter_audit_policy(ObForOracleParser.Alter_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_cluster}.
	 * @param ctx the parse tree
	 */
	void enterAlter_cluster(ObForOracleParser.Alter_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_cluster}.
	 * @param ctx the parse tree
	 */
	void exitAlter_cluster(ObForOracleParser.Alter_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 */
	void enterDrop_analytic_view(ObForOracleParser.Drop_analytic_viewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_analytic_view}.
	 * @param ctx the parse tree
	 */
	void exitDrop_analytic_view(ObForOracleParser.Drop_analytic_viewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void enterDrop_attribute_dimension(ObForOracleParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_attribute_dimension}.
	 * @param ctx the parse tree
	 */
	void exitDrop_attribute_dimension(ObForOracleParser.Drop_attribute_dimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 */
	void enterDrop_audit_policy(ObForOracleParser.Drop_audit_policyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_audit_policy}.
	 * @param ctx the parse tree
	 */
	void exitDrop_audit_policy(ObForOracleParser.Drop_audit_policyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void enterDrop_flashback_archive(ObForOracleParser.Drop_flashback_archiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_flashback_archive}.
	 * @param ctx the parse tree
	 */
	void exitDrop_flashback_archive(ObForOracleParser.Drop_flashback_archiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_cluster}.
	 * @param ctx the parse tree
	 */
	void enterDrop_cluster(ObForOracleParser.Drop_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_cluster}.
	 * @param ctx the parse tree
	 */
	void exitDrop_cluster(ObForOracleParser.Drop_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_context}.
	 * @param ctx the parse tree
	 */
	void enterDrop_context(ObForOracleParser.Drop_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_context}.
	 * @param ctx the parse tree
	 */
	void exitDrop_context(ObForOracleParser.Drop_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_directory}.
	 * @param ctx the parse tree
	 */
	void enterDrop_directory(ObForOracleParser.Drop_directoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_directory}.
	 * @param ctx the parse tree
	 */
	void exitDrop_directory(ObForOracleParser.Drop_directoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 */
	void enterDrop_diskgroup(ObForOracleParser.Drop_diskgroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_diskgroup}.
	 * @param ctx the parse tree
	 */
	void exitDrop_diskgroup(ObForOracleParser.Drop_diskgroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_edition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_edition(ObForOracleParser.Drop_editionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_edition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_edition(ObForOracleParser.Drop_editionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#truncate_cluster}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_cluster(ObForOracleParser.Truncate_clusterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#truncate_cluster}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_cluster(ObForOracleParser.Truncate_clusterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 */
	void enterCache_or_nocache(ObForOracleParser.Cache_or_nocacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cache_or_nocache}.
	 * @param ctx the parse tree
	 */
	void exitCache_or_nocache(ObForOracleParser.Cache_or_nocacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database_name}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_name(ObForOracleParser.Database_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database_name}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_name(ObForOracleParser.Database_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_database}.
	 * @param ctx the parse tree
	 */
	void enterAlter_database(ObForOracleParser.Alter_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_database}.
	 * @param ctx the parse tree
	 */
	void exitAlter_database(ObForOracleParser.Alter_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database_clause}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_clause(ObForOracleParser.Database_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database_clause}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_clause(ObForOracleParser.Database_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#startup_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStartup_clauses(ObForOracleParser.Startup_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#startup_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStartup_clauses(ObForOracleParser.Startup_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 */
	void enterResetlogs_or_noresetlogs(ObForOracleParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#resetlogs_or_noresetlogs}.
	 * @param ctx the parse tree
	 */
	void exitResetlogs_or_noresetlogs(ObForOracleParser.Resetlogs_or_noresetlogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 */
	void enterUpgrade_or_downgrade(ObForOracleParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#upgrade_or_downgrade}.
	 * @param ctx the parse tree
	 */
	void exitUpgrade_or_downgrade(ObForOracleParser.Upgrade_or_downgradeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#recovery_clauses}.
	 * @param ctx the parse tree
	 */
	void enterRecovery_clauses(ObForOracleParser.Recovery_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#recovery_clauses}.
	 * @param ctx the parse tree
	 */
	void exitRecovery_clauses(ObForOracleParser.Recovery_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#begin_or_end}.
	 * @param ctx the parse tree
	 */
	void enterBegin_or_end(ObForOracleParser.Begin_or_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#begin_or_end}.
	 * @param ctx the parse tree
	 */
	void exitBegin_or_end(ObForOracleParser.Begin_or_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#general_recovery}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_recovery(ObForOracleParser.General_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#general_recovery}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_recovery(ObForOracleParser.General_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#full_database_recovery}.
	 * @param ctx the parse tree
	 */
	void enterFull_database_recovery(ObForOracleParser.Full_database_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#full_database_recovery}.
	 * @param ctx the parse tree
	 */
	void exitFull_database_recovery(ObForOracleParser.Full_database_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 */
	void enterPartial_database_recovery(ObForOracleParser.Partial_database_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partial_database_recovery}.
	 * @param ctx the parse tree
	 */
	void exitPartial_database_recovery(ObForOracleParser.Partial_database_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 */
	void enterPartial_database_recovery_10g(ObForOracleParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partial_database_recovery_10g}.
	 * @param ctx the parse tree
	 */
	void exitPartial_database_recovery_10g(ObForOracleParser.Partial_database_recovery_10gContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 */
	void enterManaged_standby_recovery(ObForOracleParser.Managed_standby_recoveryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#managed_standby_recovery}.
	 * @param ctx the parse tree
	 */
	void exitManaged_standby_recovery(ObForOracleParser.Managed_standby_recoveryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#db_name}.
	 * @param ctx the parse tree
	 */
	void enterDb_name(ObForOracleParser.Db_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#db_name}.
	 * @param ctx the parse tree
	 */
	void exitDb_name(ObForOracleParser.Db_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database_file_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_file_clauses(ObForOracleParser.Database_file_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database_file_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_file_clauses(ObForOracleParser.Database_file_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterCreate_datafile_clause(ObForOracleParser.Create_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitCreate_datafile_clause(ObForOracleParser.Create_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_datafile_clause(ObForOracleParser.Alter_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_datafile_clause(ObForOracleParser.Alter_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tempfile_clause(ObForOracleParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_tempfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tempfile_clause(ObForOracleParser.Alter_tempfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_datafile_clause(ObForOracleParser.Move_datafile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#move_datafile_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_datafile_clause(ObForOracleParser.Move_datafile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_clauses(ObForOracleParser.Logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_clauses(ObForOracleParser.Logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_logfile_clauses(ObForOracleParser.Add_logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_logfile_clauses(ObForOracleParser.Add_logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 */
	void enterGroup_redo_logfile(ObForOracleParser.Group_redo_logfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#group_redo_logfile}.
	 * @param ctx the parse tree
	 */
	void exitGroup_redo_logfile(ObForOracleParser.Group_redo_logfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDrop_logfile_clauses(ObForOracleParser.Drop_logfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_logfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDrop_logfile_clauses(ObForOracleParser.Drop_logfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_logfile_clause(ObForOracleParser.Switch_logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#switch_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_logfile_clause(ObForOracleParser.Switch_logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_db_logging(ObForOracleParser.Supplemental_db_loggingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_db_logging}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_db_logging(ObForOracleParser.Supplemental_db_loggingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_or_drop}.
	 * @param ctx the parse tree
	 */
	void enterAdd_or_drop(ObForOracleParser.Add_or_dropContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_or_drop}.
	 * @param ctx the parse tree
	 */
	void exitAdd_or_drop(ObForOracleParser.Add_or_dropContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_plsql_clause(ObForOracleParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_plsql_clause}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_plsql_clause(ObForOracleParser.Supplemental_plsql_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 */
	void enterLogfile_descriptor(ObForOracleParser.Logfile_descriptorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logfile_descriptor}.
	 * @param ctx the parse tree
	 */
	void exitLogfile_descriptor(ObForOracleParser.Logfile_descriptorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterControlfile_clauses(ObForOracleParser.Controlfile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#controlfile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitControlfile_clauses(ObForOracleParser.Controlfile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trace_file_clause}.
	 * @param ctx the parse tree
	 */
	void enterTrace_file_clause(ObForOracleParser.Trace_file_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trace_file_clause}.
	 * @param ctx the parse tree
	 */
	void exitTrace_file_clause(ObForOracleParser.Trace_file_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 */
	void enterStandby_database_clauses(ObForOracleParser.Standby_database_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#standby_database_clauses}.
	 * @param ctx the parse tree
	 */
	void exitStandby_database_clauses(ObForOracleParser.Standby_database_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void enterActivate_standby_db_clause(ObForOracleParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#activate_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void exitActivate_standby_db_clause(ObForOracleParser.Activate_standby_db_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void enterMaximize_standby_db_clause(ObForOracleParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#maximize_standby_db_clause}.
	 * @param ctx the parse tree
	 */
	void exitMaximize_standby_db_clause(ObForOracleParser.Maximize_standby_db_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void enterRegister_logfile_clause(ObForOracleParser.Register_logfile_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#register_logfile_clause}.
	 * @param ctx the parse tree
	 */
	void exitRegister_logfile_clause(ObForOracleParser.Register_logfile_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 */
	void enterCommit_switchover_clause(ObForOracleParser.Commit_switchover_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#commit_switchover_clause}.
	 * @param ctx the parse tree
	 */
	void exitCommit_switchover_clause(ObForOracleParser.Commit_switchover_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#start_standby_clause}.
	 * @param ctx the parse tree
	 */
	void enterStart_standby_clause(ObForOracleParser.Start_standby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#start_standby_clause}.
	 * @param ctx the parse tree
	 */
	void exitStart_standby_clause(ObForOracleParser.Start_standby_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 */
	void enterStop_standby_clause(ObForOracleParser.Stop_standby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#stop_standby_clause}.
	 * @param ctx the parse tree
	 */
	void exitStop_standby_clause(ObForOracleParser.Stop_standby_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#convert_database_clause}.
	 * @param ctx the parse tree
	 */
	void enterConvert_database_clause(ObForOracleParser.Convert_database_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#convert_database_clause}.
	 * @param ctx the parse tree
	 */
	void exitConvert_database_clause(ObForOracleParser.Convert_database_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_settings_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_settings_clause(ObForOracleParser.Default_settings_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_settings_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_settings_clause(ObForOracleParser.Default_settings_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 */
	void enterSet_time_zone_clause(ObForOracleParser.Set_time_zone_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_time_zone_clause}.
	 * @param ctx the parse tree
	 */
	void exitSet_time_zone_clause(ObForOracleParser.Set_time_zone_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#instance_clauses}.
	 * @param ctx the parse tree
	 */
	void enterInstance_clauses(ObForOracleParser.Instance_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#instance_clauses}.
	 * @param ctx the parse tree
	 */
	void exitInstance_clauses(ObForOracleParser.Instance_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#security_clause}.
	 * @param ctx the parse tree
	 */
	void enterSecurity_clause(ObForOracleParser.Security_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#security_clause}.
	 * @param ctx the parse tree
	 */
	void exitSecurity_clause(ObForOracleParser.Security_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#domain}.
	 * @param ctx the parse tree
	 */
	void enterDomain(ObForOracleParser.DomainContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#domain}.
	 * @param ctx the parse tree
	 */
	void exitDomain(ObForOracleParser.DomainContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database}.
	 * @param ctx the parse tree
	 */
	void enterDatabase(ObForOracleParser.DatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database}.
	 * @param ctx the parse tree
	 */
	void exitDatabase(ObForOracleParser.DatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#edition_name}.
	 * @param ctx the parse tree
	 */
	void enterEdition_name(ObForOracleParser.Edition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#edition_name}.
	 * @param ctx the parse tree
	 */
	void exitEdition_name(ObForOracleParser.Edition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filenumber}.
	 * @param ctx the parse tree
	 */
	void enterFilenumber(ObForOracleParser.FilenumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filenumber}.
	 * @param ctx the parse tree
	 */
	void exitFilenumber(ObForOracleParser.FilenumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filename}.
	 * @param ctx the parse tree
	 */
	void enterFilename(ObForOracleParser.FilenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filename}.
	 * @param ctx the parse tree
	 */
	void exitFilename(ObForOracleParser.FilenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#prepare_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrepare_clause(ObForOracleParser.Prepare_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#prepare_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrepare_clause(ObForOracleParser.Prepare_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_mirror_clause(ObForOracleParser.Drop_mirror_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_mirror_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_mirror_clause(ObForOracleParser.Drop_mirror_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lost_write_protection}.
	 * @param ctx the parse tree
	 */
	void enterLost_write_protection(ObForOracleParser.Lost_write_protectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lost_write_protection}.
	 * @param ctx the parse tree
	 */
	void exitLost_write_protection(ObForOracleParser.Lost_write_protectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 */
	void enterCdb_fleet_clauses(ObForOracleParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cdb_fleet_clauses}.
	 * @param ctx the parse tree
	 */
	void exitCdb_fleet_clauses(ObForOracleParser.Cdb_fleet_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 */
	void enterLead_cdb_clause(ObForOracleParser.Lead_cdb_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lead_cdb_clause}.
	 * @param ctx the parse tree
	 */
	void exitLead_cdb_clause(ObForOracleParser.Lead_cdb_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 */
	void enterLead_cdb_uri_clause(ObForOracleParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lead_cdb_uri_clause}.
	 * @param ctx the parse tree
	 */
	void exitLead_cdb_uri_clause(ObForOracleParser.Lead_cdb_uri_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#property_clauses}.
	 * @param ctx the parse tree
	 */
	void enterProperty_clauses(ObForOracleParser.Property_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#property_clauses}.
	 * @param ctx the parse tree
	 */
	void exitProperty_clauses(ObForOracleParser.Property_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 */
	void enterReplay_upgrade_clauses(ObForOracleParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#replay_upgrade_clauses}.
	 * @param ctx the parse tree
	 */
	void exitReplay_upgrade_clauses(ObForOracleParser.Replay_upgrade_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_database_link}.
	 * @param ctx the parse tree
	 */
	void enterAlter_database_link(ObForOracleParser.Alter_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_database_link}.
	 * @param ctx the parse tree
	 */
	void exitAlter_database_link(ObForOracleParser.Alter_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#password_value}.
	 * @param ctx the parse tree
	 */
	void enterPassword_value(ObForOracleParser.Password_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#password_value}.
	 * @param ctx the parse tree
	 */
	void exitPassword_value(ObForOracleParser.Password_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#link_authentication}.
	 * @param ctx the parse tree
	 */
	void enterLink_authentication(ObForOracleParser.Link_authenticationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#link_authentication}.
	 * @param ctx the parse tree
	 */
	void exitLink_authentication(ObForOracleParser.Link_authenticationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_schema}.
	 * @param ctx the parse tree
	 */
	void enterCreate_schema(ObForOracleParser.Create_schemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_schema}.
	 * @param ctx the parse tree
	 */
	void exitCreate_schema(ObForOracleParser.Create_schemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_database}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database(ObForOracleParser.Create_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_database}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database(ObForOracleParser.Create_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_logging_clauses(ObForOracleParser.Database_logging_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database_logging_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_logging_clauses(ObForOracleParser.Database_logging_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_logging_sub_clause(ObForOracleParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#database_logging_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_logging_sub_clause(ObForOracleParser.Database_logging_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_clauses(ObForOracleParser.Tablespace_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_clauses(ObForOracleParser.Tablespace_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 */
	void enterEnable_pluggable_database(ObForOracleParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#enable_pluggable_database}.
	 * @param ctx the parse tree
	 */
	void exitEnable_pluggable_database(ObForOracleParser.Enable_pluggable_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#file_name_convert}.
	 * @param ctx the parse tree
	 */
	void enterFile_name_convert(ObForOracleParser.File_name_convertContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#file_name_convert}.
	 * @param ctx the parse tree
	 */
	void exitFile_name_convert(ObForOracleParser.File_name_convertContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilename_convert_sub_clause(ObForOracleParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filename_convert_sub_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilename_convert_sub_clause(ObForOracleParser.Filename_convert_sub_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 */
	void enterTablespace_datafile_clauses(ObForOracleParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace_datafile_clauses}.
	 * @param ctx the parse tree
	 */
	void exitTablespace_datafile_clauses(ObForOracleParser.Tablespace_datafile_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 */
	void enterUndo_mode_clause(ObForOracleParser.Undo_mode_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#undo_mode_clause}.
	 * @param ctx the parse tree
	 */
	void exitUndo_mode_clause(ObForOracleParser.Undo_mode_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDefault_tablespace(ObForOracleParser.Default_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDefault_tablespace(ObForOracleParser.Default_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterDefault_temp_tablespace(ObForOracleParser.Default_temp_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_temp_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitDefault_temp_tablespace(ObForOracleParser.Default_temp_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#undo_tablespace}.
	 * @param ctx the parse tree
	 */
	void enterUndo_tablespace(ObForOracleParser.Undo_tablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#undo_tablespace}.
	 * @param ctx the parse tree
	 */
	void exitUndo_tablespace(ObForOracleParser.Undo_tablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_database}.
	 * @param ctx the parse tree
	 */
	void enterDrop_database(ObForOracleParser.Drop_databaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_database}.
	 * @param ctx the parse tree
	 */
	void exitDrop_database(ObForOracleParser.Drop_databaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#create_database_link}.
	 * @param ctx the parse tree
	 */
	void enterCreate_database_link(ObForOracleParser.Create_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#create_database_link}.
	 * @param ctx the parse tree
	 */
	void exitCreate_database_link(ObForOracleParser.Create_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_database_link}.
	 * @param ctx the parse tree
	 */
	void enterDrop_database_link(ObForOracleParser.Drop_database_linkContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_database_link}.
	 * @param ctx the parse tree
	 */
	void exitDrop_database_link(ObForOracleParser.Drop_database_linkContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_set(ObForOracleParser.Alter_tablespace_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_tablespace_set}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_set(ObForOracleParser.Alter_tablespace_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_attrs(ObForOracleParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_tablespace_attrs}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_attrs(ObForOracleParser.Alter_tablespace_attrsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 */
	void enterAlter_tablespace_encryption(ObForOracleParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_tablespace_encryption}.
	 * @param ctx the parse tree
	 */
	void exitAlter_tablespace_encryption(ObForOracleParser.Alter_tablespace_encryptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 */
	void enterTs_file_name_convert(ObForOracleParser.Ts_file_name_convertContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ts_file_name_convert}.
	 * @param ctx the parse tree
	 */
	void exitTs_file_name_convert(ObForOracleParser.Ts_file_name_convertContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_role}.
	 * @param ctx the parse tree
	 */
	void enterAlter_role(ObForOracleParser.Alter_roleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_role}.
	 * @param ctx the parse tree
	 */
	void exitAlter_role(ObForOracleParser.Alter_roleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#role_identified_clause}.
	 * @param ctx the parse tree
	 */
	void enterRole_identified_clause(ObForOracleParser.Role_identified_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#role_identified_clause}.
	 * @param ctx the parse tree
	 */
	void exitRole_identified_clause(ObForOracleParser.Role_identified_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_table}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table(ObForOracleParser.Alter_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_table}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table(ObForOracleParser.Alter_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 */
	void enterMemoptimize_read_write_clause(ObForOracleParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#memoptimize_read_write_clause}.
	 * @param ctx the parse tree
	 */
	void exitMemoptimize_read_write_clause(ObForOracleParser.Memoptimize_read_write_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_table_properties}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_properties(ObForOracleParser.Alter_table_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_table_properties}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_properties(ObForOracleParser.Alter_table_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_partitioning(ObForOracleParser.Alter_table_partitioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_table_partitioning}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_partitioning(ObForOracleParser.Alter_table_partitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterAdd_table_partition(ObForOracleParser.Add_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitAdd_table_partition(ObForOracleParser.Add_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_partition(ObForOracleParser.Drop_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_partition(ObForOracleParser.Drop_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterMerge_table_partition(ObForOracleParser.Merge_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitMerge_table_partition(ObForOracleParser.Merge_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterModify_table_partition(ObForOracleParser.Modify_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitModify_table_partition(ObForOracleParser.Modify_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#split_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterSplit_table_partition(ObForOracleParser.Split_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#split_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitSplit_table_partition(ObForOracleParser.Split_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterTruncate_table_partition(ObForOracleParser.Truncate_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#truncate_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitTruncate_table_partition(ObForOracleParser.Truncate_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterExchange_table_partition(ObForOracleParser.Exchange_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exchange_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitExchange_table_partition(ObForOracleParser.Exchange_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 */
	void enterCoalesce_table_partition(ObForOracleParser.Coalesce_table_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#coalesce_table_partition}.
	 * @param ctx the parse tree
	 */
	void exitCoalesce_table_partition(ObForOracleParser.Coalesce_table_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 */
	void enterAlter_interval_partition(ObForOracleParser.Alter_interval_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_interval_partition}.
	 * @param ctx the parse tree
	 */
	void exitAlter_interval_partition(ObForOracleParser.Alter_interval_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_extended_names}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extended_names(ObForOracleParser.Partition_extended_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_extended_names}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extended_names(ObForOracleParser.Partition_extended_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 */
	void enterSubpartition_extended_names(ObForOracleParser.Subpartition_extended_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subpartition_extended_names}.
	 * @param ctx the parse tree
	 */
	void exitSubpartition_extended_names(ObForOracleParser.Subpartition_extended_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_properties_1(ObForOracleParser.Alter_table_properties_1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_table_properties_1}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_properties_1(ObForOracleParser.Alter_table_properties_1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAlter_iot_clauses(ObForOracleParser.Alter_iot_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_iot_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAlter_iot_clauses(ObForOracleParser.Alter_iot_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_mapping_table_clause(ObForOracleParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_mapping_table_clause(ObForOracleParser.Alter_mapping_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterAlter_overflow_clause(ObForOracleParser.Alter_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitAlter_overflow_clause(ObForOracleParser.Alter_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_overflow_clause(ObForOracleParser.Add_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_overflow_clause(ObForOracleParser.Add_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_index_clauses}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_clauses(ObForOracleParser.Update_index_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_index_clauses}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_clauses(ObForOracleParser.Update_index_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_global_index_clause(ObForOracleParser.Update_global_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_global_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_global_index_clause(ObForOracleParser.Update_global_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_all_indexes_clause(ObForOracleParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_all_indexes_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_all_indexes_clause(ObForOracleParser.Update_all_indexes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_all_indexes_index_clause(ObForOracleParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_all_indexes_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_all_indexes_index_clause(ObForOracleParser.Update_all_indexes_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_index_partition}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_partition(ObForOracleParser.Update_index_partitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_index_partition}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_partition(ObForOracleParser.Update_index_partitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_index_subpartition(ObForOracleParser.Update_index_subpartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_index_subpartition}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_index_subpartition(ObForOracleParser.Update_index_subpartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 */
	void enterEnable_disable_clause(ObForOracleParser.Enable_disable_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#enable_disable_clause}.
	 * @param ctx the parse tree
	 */
	void exitEnable_disable_clause(ObForOracleParser.Enable_disable_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_index_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_index_clause(ObForOracleParser.Using_index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_index_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_index_clause(ObForOracleParser.Using_index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_attributes}.
	 * @param ctx the parse tree
	 */
	void enterIndex_attributes(ObForOracleParser.Index_attributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_attributes}.
	 * @param ctx the parse tree
	 */
	void exitIndex_attributes(ObForOracleParser.Index_attributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 */
	void enterSort_or_nosort(ObForOracleParser.Sort_or_nosortContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sort_or_nosort}.
	 * @param ctx the parse tree
	 */
	void exitSort_or_nosort(ObForOracleParser.Sort_or_nosortContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exceptions_clause}.
	 * @param ctx the parse tree
	 */
	void enterExceptions_clause(ObForOracleParser.Exceptions_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exceptions_clause}.
	 * @param ctx the parse tree
	 */
	void exitExceptions_clause(ObForOracleParser.Exceptions_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#move_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterMove_table_clause(ObForOracleParser.Move_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#move_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitMove_table_clause(ObForOracleParser.Move_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_org_table_clause(ObForOracleParser.Index_org_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_org_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_org_table_clause(ObForOracleParser.Index_org_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterMapping_table_clause(ObForOracleParser.Mapping_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#mapping_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitMapping_table_clause(ObForOracleParser.Mapping_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#key_compression}.
	 * @param ctx the parse tree
	 */
	void enterKey_compression(ObForOracleParser.Key_compressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#key_compression}.
	 * @param ctx the parse tree
	 */
	void exitKey_compression(ObForOracleParser.Key_compressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_org_overflow_clause(ObForOracleParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_org_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_org_overflow_clause(ObForOracleParser.Index_org_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterColumn_clauses(ObForOracleParser.Column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitColumn_clauses(ObForOracleParser.Column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 */
	void enterModify_collection_retrieval(ObForOracleParser.Modify_collection_retrievalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_collection_retrieval}.
	 * @param ctx the parse tree
	 */
	void exitModify_collection_retrieval(ObForOracleParser.Modify_collection_retrievalContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collection_item}.
	 * @param ctx the parse tree
	 */
	void enterCollection_item(ObForOracleParser.Collection_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collection_item}.
	 * @param ctx the parse tree
	 */
	void exitCollection_item(ObForOracleParser.Collection_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rename_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterRename_column_clause(ObForOracleParser.Rename_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rename_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitRename_column_clause(ObForOracleParser.Rename_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#old_column_name}.
	 * @param ctx the parse tree
	 */
	void enterOld_column_name(ObForOracleParser.Old_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#old_column_name}.
	 * @param ctx the parse tree
	 */
	void exitOld_column_name(ObForOracleParser.Old_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_column_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_column_name(ObForOracleParser.New_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_column_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_column_name(ObForOracleParser.New_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_modify_drop_column_clauses(ObForOracleParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_modify_drop_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_modify_drop_column_clauses(ObForOracleParser.Add_modify_drop_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_column_clause(ObForOracleParser.Drop_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_column_clause(ObForOracleParser.Drop_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModify_column_clauses(ObForOracleParser.Modify_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModify_column_clauses(ObForOracleParser.Modify_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_properties(ObForOracleParser.Modify_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_properties(ObForOracleParser.Modify_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#col_default}.
	 * @param ctx the parse tree
	 */
	void enterCol_default(ObForOracleParser.Col_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#col_default}.
	 * @param ctx the parse tree
	 */
	void exitCol_default(ObForOracleParser.Col_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_visibility(ObForOracleParser.Modify_col_visibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_col_visibility}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_visibility(ObForOracleParser.Modify_col_visibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 */
	void enterModify_col_substitutable(ObForOracleParser.Modify_col_substitutableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_col_substitutable}.
	 * @param ctx the parse tree
	 */
	void exitModify_col_substitutable(ObForOracleParser.Modify_col_substitutableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_column_clause(ObForOracleParser.Add_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_column_clause(ObForOracleParser.Add_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterAlter_varray_col_properties(ObForOracleParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#alter_varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitAlter_varray_col_properties(ObForOracleParser.Alter_varray_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterVarray_col_properties(ObForOracleParser.Varray_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#varray_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitVarray_col_properties(ObForOracleParser.Varray_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterVarray_storage_clause(ObForOracleParser.Varray_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#varray_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitVarray_storage_clause(ObForOracleParser.Varray_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_segname}.
	 * @param ctx the parse tree
	 */
	void enterLob_segname(ObForOracleParser.Lob_segnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_segname}.
	 * @param ctx the parse tree
	 */
	void exitLob_segname(ObForOracleParser.Lob_segnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_item}.
	 * @param ctx the parse tree
	 */
	void enterLob_item(ObForOracleParser.Lob_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_item}.
	 * @param ctx the parse tree
	 */
	void exitLob_item(ObForOracleParser.Lob_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 */
	void enterLob_storage_parameters(ObForOracleParser.Lob_storage_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_storage_parameters}.
	 * @param ctx the parse tree
	 */
	void exitLob_storage_parameters(ObForOracleParser.Lob_storage_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_storage_clause(ObForOracleParser.Lob_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_storage_clause(ObForOracleParser.Lob_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void enterModify_lob_storage_clause(ObForOracleParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_lob_storage_clause}.
	 * @param ctx the parse tree
	 */
	void exitModify_lob_storage_clause(ObForOracleParser.Modify_lob_storage_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 */
	void enterModify_lob_parameters(ObForOracleParser.Modify_lob_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#modify_lob_parameters}.
	 * @param ctx the parse tree
	 */
	void exitModify_lob_parameters(ObForOracleParser.Modify_lob_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_parameters}.
	 * @param ctx the parse tree
	 */
	void enterLob_parameters(ObForOracleParser.Lob_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_parameters}.
	 * @param ctx the parse tree
	 */
	void exitLob_parameters(ObForOracleParser.Lob_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_deduplicate_clause(ObForOracleParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_deduplicate_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_deduplicate_clause(ObForOracleParser.Lob_deduplicate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_compression_clause(ObForOracleParser.Lob_compression_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_compression_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_compression_clause(ObForOracleParser.Lob_compression_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 */
	void enterLob_retention_clause(ObForOracleParser.Lob_retention_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_retention_clause}.
	 * @param ctx the parse tree
	 */
	void exitLob_retention_clause(ObForOracleParser.Lob_retention_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#encryption_spec}.
	 * @param ctx the parse tree
	 */
	void enterEncryption_spec(ObForOracleParser.Encryption_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#encryption_spec}.
	 * @param ctx the parse tree
	 */
	void exitEncryption_spec(ObForOracleParser.Encryption_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tablespace}.
	 * @param ctx the parse tree
	 */
	void enterTablespace(ObForOracleParser.TablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tablespace}.
	 * @param ctx the parse tree
	 */
	void exitTablespace(ObForOracleParser.TablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#varray_item}.
	 * @param ctx the parse tree
	 */
	void enterVarray_item(ObForOracleParser.Varray_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#varray_item}.
	 * @param ctx the parse tree
	 */
	void exitVarray_item(ObForOracleParser.Varray_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_properties}.
	 * @param ctx the parse tree
	 */
	void enterColumn_properties(ObForOracleParser.Column_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_properties}.
	 * @param ctx the parse tree
	 */
	void exitColumn_properties(ObForOracleParser.Column_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 */
	void enterLob_partition_storage(ObForOracleParser.Lob_partition_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lob_partition_storage}.
	 * @param ctx the parse tree
	 */
	void exitLob_partition_storage(ObForOracleParser.Lob_partition_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#period_definition}.
	 * @param ctx the parse tree
	 */
	void enterPeriod_definition(ObForOracleParser.Period_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#period_definition}.
	 * @param ctx the parse tree
	 */
	void exitPeriod_definition(ObForOracleParser.Period_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#start_time_column}.
	 * @param ctx the parse tree
	 */
	void enterStart_time_column(ObForOracleParser.Start_time_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#start_time_column}.
	 * @param ctx the parse tree
	 */
	void exitStart_time_column(ObForOracleParser.Start_time_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#end_time_column}.
	 * @param ctx the parse tree
	 */
	void enterEnd_time_column(ObForOracleParser.End_time_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#end_time_column}.
	 * @param ctx the parse tree
	 */
	void exitEnd_time_column(ObForOracleParser.End_time_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_definition}.
	 * @param ctx the parse tree
	 */
	void enterColumn_definition(ObForOracleParser.Column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_definition}.
	 * @param ctx the parse tree
	 */
	void exitColumn_definition(ObForOracleParser.Column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_collation_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_collation_name(ObForOracleParser.Column_collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_collation_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_collation_name(ObForOracleParser.Column_collation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identity_clause}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_clause(ObForOracleParser.Identity_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identity_clause}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_clause(ObForOracleParser.Identity_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_options_parentheses(ObForOracleParser.Identity_options_parenthesesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identity_options_parentheses}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_options_parentheses(ObForOracleParser.Identity_options_parenthesesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identity_options}.
	 * @param ctx the parse tree
	 */
	void enterIdentity_options(ObForOracleParser.Identity_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identity_options}.
	 * @param ctx the parse tree
	 */
	void exitIdentity_options(ObForOracleParser.Identity_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 */
	void enterVirtual_column_definition(ObForOracleParser.Virtual_column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#virtual_column_definition}.
	 * @param ctx the parse tree
	 */
	void exitVirtual_column_definition(ObForOracleParser.Virtual_column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 */
	void enterAutogenerated_sequence_definition(ObForOracleParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#autogenerated_sequence_definition}.
	 * @param ctx the parse tree
	 */
	void exitAutogenerated_sequence_definition(ObForOracleParser.Autogenerated_sequence_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation_edition_clause(ObForOracleParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#evaluation_edition_clause}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation_edition_clause(ObForOracleParser.Evaluation_edition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_line_part_storage(ObForOracleParser.Out_of_line_part_storageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#out_of_line_part_storage}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_line_part_storage(ObForOracleParser.Out_of_line_part_storageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterNested_table_col_properties(ObForOracleParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#nested_table_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitNested_table_col_properties(ObForOracleParser.Nested_table_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#nested_item}.
	 * @param ctx the parse tree
	 */
	void enterNested_item(ObForOracleParser.Nested_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#nested_item}.
	 * @param ctx the parse tree
	 */
	void exitNested_item(ObForOracleParser.Nested_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubstitutable_column_clause(ObForOracleParser.Substitutable_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#substitutable_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubstitutable_column_clause(ObForOracleParser.Substitutable_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_name}.
	 * @param ctx the parse tree
	 */
	void enterPartition_name(ObForOracleParser.Partition_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_name}.
	 * @param ctx the parse tree
	 */
	void exitPartition_name(ObForOracleParser.Partition_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 */
	void enterSupplemental_logging_props(ObForOracleParser.Supplemental_logging_propsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#supplemental_logging_props}.
	 * @param ctx the parse tree
	 */
	void exitSupplemental_logging_props(ObForOracleParser.Supplemental_logging_propsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_or_attribute}.
	 * @param ctx the parse tree
	 */
	void enterColumn_or_attribute(ObForOracleParser.Column_or_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_or_attribute}.
	 * @param ctx the parse tree
	 */
	void exitColumn_or_attribute(ObForOracleParser.Column_or_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 */
	void enterObject_type_col_properties(ObForOracleParser.Object_type_col_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_type_col_properties}.
	 * @param ctx the parse tree
	 */
	void exitObject_type_col_properties(ObForOracleParser.Object_type_col_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterAdd_table_constarint(ObForOracleParser.Add_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code add_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitAdd_table_constarint(ObForOracleParser.Add_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModify_table_constarint(ObForOracleParser.Modify_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code modify_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModify_table_constarint(ObForOracleParser.Modify_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterRename_table_constarint(ObForOracleParser.Rename_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rename_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitRename_table_constarint(ObForOracleParser.Rename_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_constarint(ObForOracleParser.Drop_table_constarintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code drop_table_constarint}
	 * labeled alternative in {@link ObForOracleParser#constraint_clauses}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_constarint(ObForOracleParser.Drop_table_constarintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#old_constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterOld_constraint_name(ObForOracleParser.Old_constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#old_constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitOld_constraint_name(ObForOracleParser.Old_constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#new_constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_constraint_name(ObForOracleParser.New_constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#new_constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_constraint_name(ObForOracleParser.New_constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void enterDrop_constraint_clause(ObForOracleParser.Drop_constraint_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void exitDrop_constraint_clause(ObForOracleParser.Drop_constraint_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_constraint}.
	 * @param ctx the parse tree
	 */
	void enterAdd_constraint(ObForOracleParser.Add_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_constraint}.
	 * @param ctx the parse tree
	 */
	void exitAdd_constraint(ObForOracleParser.Add_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_constraint_clause(ObForOracleParser.Add_constraint_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_constraint_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_constraint_clause(ObForOracleParser.Add_constraint_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#check_constraint}.
	 * @param ctx the parse tree
	 */
	void enterCheck_constraint(ObForOracleParser.Check_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#check_constraint}.
	 * @param ctx the parse tree
	 */
	void exitCheck_constraint(ObForOracleParser.Check_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#drop_constraint}.
	 * @param ctx the parse tree
	 */
	void enterDrop_constraint(ObForOracleParser.Drop_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#drop_constraint}.
	 * @param ctx the parse tree
	 */
	void exitDrop_constraint(ObForOracleParser.Drop_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#enable_constraint}.
	 * @param ctx the parse tree
	 */
	void enterEnable_constraint(ObForOracleParser.Enable_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#enable_constraint}.
	 * @param ctx the parse tree
	 */
	void exitEnable_constraint(ObForOracleParser.Enable_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#disable_constraint}.
	 * @param ctx the parse tree
	 */
	void enterDisable_constraint(ObForOracleParser.Disable_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#disable_constraint}.
	 * @param ctx the parse tree
	 */
	void exitDisable_constraint(ObForOracleParser.Disable_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterForeign_key_clause(ObForOracleParser.Foreign_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitForeign_key_clause(ObForOracleParser.Foreign_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#references_clause}.
	 * @param ctx the parse tree
	 */
	void enterReferences_clause(ObForOracleParser.References_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#references_clause}.
	 * @param ctx the parse tree
	 */
	void exitReferences_clause(ObForOracleParser.References_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#on_delete_clause}.
	 * @param ctx the parse tree
	 */
	void enterOn_delete_clause(ObForOracleParser.On_delete_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#on_delete_clause}.
	 * @param ctx the parse tree
	 */
	void exitOn_delete_clause(ObForOracleParser.On_delete_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unique_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnique_key_clause(ObForOracleParser.Unique_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unique_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnique_key_clause(ObForOracleParser.Unique_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#primary_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key_clause(ObForOracleParser.Primary_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#primary_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key_clause(ObForOracleParser.Primary_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#anonymous_block}.
	 * @param ctx the parse tree
	 */
	void enterAnonymous_block(ObForOracleParser.Anonymous_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#anonymous_block}.
	 * @param ctx the parse tree
	 */
	void exitAnonymous_block(ObForOracleParser.Anonymous_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 */
	void enterInvoker_rights_clause(ObForOracleParser.Invoker_rights_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#invoker_rights_clause}.
	 * @param ctx the parse tree
	 */
	void exitInvoker_rights_clause(ObForOracleParser.Invoker_rights_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#call_spec}.
	 * @param ctx the parse tree
	 */
	void enterCall_spec(ObForOracleParser.Call_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#call_spec}.
	 * @param ctx the parse tree
	 */
	void exitCall_spec(ObForOracleParser.Call_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#java_spec}.
	 * @param ctx the parse tree
	 */
	void enterJava_spec(ObForOracleParser.Java_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#java_spec}.
	 * @param ctx the parse tree
	 */
	void exitJava_spec(ObForOracleParser.Java_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#c_spec}.
	 * @param ctx the parse tree
	 */
	void enterC_spec(ObForOracleParser.C_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#c_spec}.
	 * @param ctx the parse tree
	 */
	void exitC_spec(ObForOracleParser.C_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterC_agent_in_clause(ObForOracleParser.C_agent_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#c_agent_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitC_agent_in_clause(ObForOracleParser.C_agent_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void enterC_parameters_clause(ObForOracleParser.C_parameters_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#c_parameters_clause}.
	 * @param ctx the parse tree
	 */
	void exitC_parameters_clause(ObForOracleParser.C_parameters_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#c_external_parameter}.
	 * @param ctx the parse tree
	 */
	void enterC_external_parameter(ObForOracleParser.C_external_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#c_external_parameter}.
	 * @param ctx the parse tree
	 */
	void exitC_external_parameter(ObForOracleParser.C_external_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#c_property}.
	 * @param ctx the parse tree
	 */
	void enterC_property(ObForOracleParser.C_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#c_property}.
	 * @param ctx the parse tree
	 */
	void exitC_property(ObForOracleParser.C_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(ObForOracleParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(ObForOracleParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#default_value_part}.
	 * @param ctx the parse tree
	 */
	void enterDefault_value_part(ObForOracleParser.Default_value_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#default_value_part}.
	 * @param ctx the parse tree
	 */
	void exitDefault_value_part(ObForOracleParser.Default_value_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 */
	void enterSeq_of_declare_specs(ObForOracleParser.Seq_of_declare_specsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#seq_of_declare_specs}.
	 * @param ctx the parse tree
	 */
	void exitSeq_of_declare_specs(ObForOracleParser.Seq_of_declare_specsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#declare_spec}.
	 * @param ctx the parse tree
	 */
	void enterDeclare_spec(ObForOracleParser.Declare_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#declare_spec}.
	 * @param ctx the parse tree
	 */
	void exitDeclare_spec(ObForOracleParser.Declare_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariable_declaration(ObForOracleParser.Variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariable_declaration(ObForOracleParser.Variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subtype_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSubtype_declaration(ObForOracleParser.Subtype_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subtype_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSubtype_declaration(ObForOracleParser.Subtype_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cursor_declaration}.
	 * @param ctx the parse tree
	 */
	void enterCursor_declaration(ObForOracleParser.Cursor_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cursor_declaration}.
	 * @param ctx the parse tree
	 */
	void exitCursor_declaration(ObForOracleParser.Cursor_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parameter_spec}.
	 * @param ctx the parse tree
	 */
	void enterParameter_spec(ObForOracleParser.Parameter_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parameter_spec}.
	 * @param ctx the parse tree
	 */
	void exitParameter_spec(ObForOracleParser.Parameter_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exception_declaration}.
	 * @param ctx the parse tree
	 */
	void enterException_declaration(ObForOracleParser.Exception_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exception_declaration}.
	 * @param ctx the parse tree
	 */
	void exitException_declaration(ObForOracleParser.Exception_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pragma_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPragma_declaration(ObForOracleParser.Pragma_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pragma_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPragma_declaration(ObForOracleParser.Pragma_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#record_type_def}.
	 * @param ctx the parse tree
	 */
	void enterRecord_type_def(ObForOracleParser.Record_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#record_type_def}.
	 * @param ctx the parse tree
	 */
	void exitRecord_type_def(ObForOracleParser.Record_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void enterField_spec(ObForOracleParser.Field_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void exitField_spec(ObForOracleParser.Field_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 */
	void enterRef_cursor_type_def(ObForOracleParser.Ref_cursor_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#ref_cursor_type_def}.
	 * @param ctx the parse tree
	 */
	void exitRef_cursor_type_def(ObForOracleParser.Ref_cursor_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void enterType_declaration(ObForOracleParser.Type_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void exitType_declaration(ObForOracleParser.Type_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_type_def}.
	 * @param ctx the parse tree
	 */
	void enterTable_type_def(ObForOracleParser.Table_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_type_def}.
	 * @param ctx the parse tree
	 */
	void exitTable_type_def(ObForOracleParser.Table_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 */
	void enterTable_indexed_by_part(ObForOracleParser.Table_indexed_by_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_indexed_by_part}.
	 * @param ctx the parse tree
	 */
	void exitTable_indexed_by_part(ObForOracleParser.Table_indexed_by_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#varray_type_def}.
	 * @param ctx the parse tree
	 */
	void enterVarray_type_def(ObForOracleParser.Varray_type_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#varray_type_def}.
	 * @param ctx the parse tree
	 */
	void exitVarray_type_def(ObForOracleParser.Varray_type_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#seq_of_statements}.
	 * @param ctx the parse tree
	 */
	void enterSeq_of_statements(ObForOracleParser.Seq_of_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#seq_of_statements}.
	 * @param ctx the parse tree
	 */
	void exitSeq_of_statements(ObForOracleParser.Seq_of_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#label_declaration}.
	 * @param ctx the parse tree
	 */
	void enterLabel_declaration(ObForOracleParser.Label_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#label_declaration}.
	 * @param ctx the parse tree
	 */
	void exitLabel_declaration(ObForOracleParser.Label_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ObForOracleParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ObForOracleParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 */
	void enterSwallow_to_semi(ObForOracleParser.Swallow_to_semiContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#swallow_to_semi}.
	 * @param ctx the parse tree
	 */
	void exitSwallow_to_semi(ObForOracleParser.Swallow_to_semiContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(ObForOracleParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(ObForOracleParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue_statement(ObForOracleParser.Continue_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue_statement(ObForOracleParser.Continue_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exit_statement}.
	 * @param ctx the parse tree
	 */
	void enterExit_statement(ObForOracleParser.Exit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exit_statement}.
	 * @param ctx the parse tree
	 */
	void exitExit_statement(ObForOracleParser.Exit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#goto_statement}.
	 * @param ctx the parse tree
	 */
	void enterGoto_statement(ObForOracleParser.Goto_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#goto_statement}.
	 * @param ctx the parse tree
	 */
	void exitGoto_statement(ObForOracleParser.Goto_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(ObForOracleParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(ObForOracleParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void enterElsif_part(ObForOracleParser.Elsif_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#elsif_part}.
	 * @param ctx the parse tree
	 */
	void exitElsif_part(ObForOracleParser.Elsif_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#else_part}.
	 * @param ctx the parse tree
	 */
	void enterElse_part(ObForOracleParser.Else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#else_part}.
	 * @param ctx the parse tree
	 */
	void exitElse_part(ObForOracleParser.Else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void enterLoop_statement(ObForOracleParser.Loop_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void exitLoop_statement(ObForOracleParser.Loop_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 */
	void enterCursor_loop_param(ObForOracleParser.Cursor_loop_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cursor_loop_param}.
	 * @param ctx the parse tree
	 */
	void exitCursor_loop_param(ObForOracleParser.Cursor_loop_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#forall_statement}.
	 * @param ctx the parse tree
	 */
	void enterForall_statement(ObForOracleParser.Forall_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#forall_statement}.
	 * @param ctx the parse tree
	 */
	void exitForall_statement(ObForOracleParser.Forall_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#bounds_clause}.
	 * @param ctx the parse tree
	 */
	void enterBounds_clause(ObForOracleParser.Bounds_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#bounds_clause}.
	 * @param ctx the parse tree
	 */
	void exitBounds_clause(ObForOracleParser.Bounds_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#between_bound}.
	 * @param ctx the parse tree
	 */
	void enterBetween_bound(ObForOracleParser.Between_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#between_bound}.
	 * @param ctx the parse tree
	 */
	void exitBetween_bound(ObForOracleParser.Between_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lower_bound}.
	 * @param ctx the parse tree
	 */
	void enterLower_bound(ObForOracleParser.Lower_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lower_bound}.
	 * @param ctx the parse tree
	 */
	void exitLower_bound(ObForOracleParser.Lower_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#upper_bound}.
	 * @param ctx the parse tree
	 */
	void enterUpper_bound(ObForOracleParser.Upper_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#upper_bound}.
	 * @param ctx the parse tree
	 */
	void exitUpper_bound(ObForOracleParser.Upper_boundContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#null_statement}.
	 * @param ctx the parse tree
	 */
	void enterNull_statement(ObForOracleParser.Null_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#null_statement}.
	 * @param ctx the parse tree
	 */
	void exitNull_statement(ObForOracleParser.Null_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void enterRaise_statement(ObForOracleParser.Raise_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void exitRaise_statement(ObForOracleParser.Raise_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(ObForOracleParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(ObForOracleParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void enterCall_statement(ObForOracleParser.Call_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#call_statement}.
	 * @param ctx the parse tree
	 */
	void exitCall_statement(ObForOracleParser.Call_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 */
	void enterPipe_row_statement(ObForOracleParser.Pipe_row_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pipe_row_statement}.
	 * @param ctx the parse tree
	 */
	void exitPipe_row_statement(ObForOracleParser.Pipe_row_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#selection_directive}.
	 * @param ctx the parse tree
	 */
	void enterSelection_directive(ObForOracleParser.Selection_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#selection_directive}.
	 * @param ctx the parse tree
	 */
	void exitSelection_directive(ObForOracleParser.Selection_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#error_directive}.
	 * @param ctx the parse tree
	 */
	void enterError_directive(ObForOracleParser.Error_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#error_directive}.
	 * @param ctx the parse tree
	 */
	void exitError_directive(ObForOracleParser.Error_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#selection_directive_body}.
	 * @param ctx the parse tree
	 */
	void enterSelection_directive_body(ObForOracleParser.Selection_directive_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#selection_directive_body}.
	 * @param ctx the parse tree
	 */
	void exitSelection_directive_body(ObForOracleParser.Selection_directive_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(ObForOracleParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(ObForOracleParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exception_handler}.
	 * @param ctx the parse tree
	 */
	void enterException_handler(ObForOracleParser.Exception_handlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exception_handler}.
	 * @param ctx the parse tree
	 */
	void exitException_handler(ObForOracleParser.Exception_handlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trigger_block}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_block(ObForOracleParser.Trigger_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trigger_block}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_block(ObForOracleParser.Trigger_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tps_block}.
	 * @param ctx the parse tree
	 */
	void enterTps_block(ObForOracleParser.Tps_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tps_block}.
	 * @param ctx the parse tree
	 */
	void exitTps_block(ObForOracleParser.Tps_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ObForOracleParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ObForOracleParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_statement(ObForOracleParser.Sql_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_statement(ObForOracleParser.Sql_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#execute_immediate}.
	 * @param ctx the parse tree
	 */
	void enterExecute_immediate(ObForOracleParser.Execute_immediateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#execute_immediate}.
	 * @param ctx the parse tree
	 */
	void exitExecute_immediate(ObForOracleParser.Execute_immediateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterDynamic_returning_clause(ObForOracleParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dynamic_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitDynamic_returning_clause(ObForOracleParser.Dynamic_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 */
	void enterData_manipulation_language_statements(ObForOracleParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#data_manipulation_language_statements}.
	 * @param ctx the parse tree
	 */
	void exitData_manipulation_language_statements(ObForOracleParser.Data_manipulation_language_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 */
	void enterCursor_manipulation_statements(ObForOracleParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cursor_manipulation_statements}.
	 * @param ctx the parse tree
	 */
	void exitCursor_manipulation_statements(ObForOracleParser.Cursor_manipulation_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#close_statement}.
	 * @param ctx the parse tree
	 */
	void enterClose_statement(ObForOracleParser.Close_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#close_statement}.
	 * @param ctx the parse tree
	 */
	void exitClose_statement(ObForOracleParser.Close_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#open_statement}.
	 * @param ctx the parse tree
	 */
	void enterOpen_statement(ObForOracleParser.Open_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#open_statement}.
	 * @param ctx the parse tree
	 */
	void exitOpen_statement(ObForOracleParser.Open_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#fetch_statement}.
	 * @param ctx the parse tree
	 */
	void enterFetch_statement(ObForOracleParser.Fetch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#fetch_statement}.
	 * @param ctx the parse tree
	 */
	void exitFetch_statement(ObForOracleParser.Fetch_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#variable_or_collection}.
	 * @param ctx the parse tree
	 */
	void enterVariable_or_collection(ObForOracleParser.Variable_or_collectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#variable_or_collection}.
	 * @param ctx the parse tree
	 */
	void exitVariable_or_collection(ObForOracleParser.Variable_or_collectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#open_for_statement}.
	 * @param ctx the parse tree
	 */
	void enterOpen_for_statement(ObForOracleParser.Open_for_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#open_for_statement}.
	 * @param ctx the parse tree
	 */
	void exitOpen_for_statement(ObForOracleParser.Open_for_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_control_statements(ObForOracleParser.Transaction_control_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#transaction_control_statements}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_control_statements(ObForOracleParser.Transaction_control_statementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_transaction_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_transaction_command(ObForOracleParser.Set_transaction_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_transaction_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_transaction_command(ObForOracleParser.Set_transaction_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_constraint_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_constraint_command(ObForOracleParser.Set_constraint_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_constraint_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_constraint_command(ObForOracleParser.Set_constraint_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#commit_statement}.
	 * @param ctx the parse tree
	 */
	void enterCommit_statement(ObForOracleParser.Commit_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#commit_statement}.
	 * @param ctx the parse tree
	 */
	void exitCommit_statement(ObForOracleParser.Commit_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#write_clause}.
	 * @param ctx the parse tree
	 */
	void enterWrite_clause(ObForOracleParser.Write_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#write_clause}.
	 * @param ctx the parse tree
	 */
	void exitWrite_clause(ObForOracleParser.Write_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rollback_statement}.
	 * @param ctx the parse tree
	 */
	void enterRollback_statement(ObForOracleParser.Rollback_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rollback_statement}.
	 * @param ctx the parse tree
	 */
	void exitRollback_statement(ObForOracleParser.Rollback_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#savepoint_statement}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_statement(ObForOracleParser.Savepoint_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#savepoint_statement}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_statement(ObForOracleParser.Savepoint_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collection_method_call}.
	 * @param ctx the parse tree
	 */
	void enterCollection_method_call(ObForOracleParser.Collection_method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collection_method_call}.
	 * @param ctx the parse tree
	 */
	void exitCollection_method_call(ObForOracleParser.Collection_method_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#explain_statement}.
	 * @param ctx the parse tree
	 */
	void enterExplain_statement(ObForOracleParser.Explain_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#explain_statement}.
	 * @param ctx the parse tree
	 */
	void exitExplain_statement(ObForOracleParser.Explain_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#select_only_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_only_statement(ObForOracleParser.Select_only_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#select_only_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_only_statement(ObForOracleParser.Select_only_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_statement(ObForOracleParser.Select_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_statement(ObForOracleParser.Select_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(ObForOracleParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(ObForOracleParser.With_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_factoring_clause(ObForOracleParser.With_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#with_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_factoring_clause(ObForOracleParser.With_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_factoring_clause(ObForOracleParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subquery_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_factoring_clause(ObForOracleParser.Subquery_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#search_clause}.
	 * @param ctx the parse tree
	 */
	void enterSearch_clause(ObForOracleParser.Search_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#search_clause}.
	 * @param ctx the parse tree
	 */
	void exitSearch_clause(ObForOracleParser.Search_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cycle_clause}.
	 * @param ctx the parse tree
	 */
	void enterCycle_clause(ObForOracleParser.Cycle_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cycle_clause}.
	 * @param ctx the parse tree
	 */
	void exitCycle_clause(ObForOracleParser.Cycle_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubav_factoring_clause(ObForOracleParser.Subav_factoring_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subav_factoring_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubav_factoring_clause(ObForOracleParser.Subav_factoring_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subav_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubav_clause(ObForOracleParser.Subav_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subav_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubav_clause(ObForOracleParser.Subav_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchies_clause(ObForOracleParser.Hierarchies_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hierarchies_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchies_clause(ObForOracleParser.Hierarchies_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filter_clauses}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clauses(ObForOracleParser.Filter_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filter_clauses}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clauses(ObForOracleParser.Filter_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void enterFilter_clause(ObForOracleParser.Filter_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#filter_clause}.
	 * @param ctx the parse tree
	 */
	void exitFilter_clause(ObForOracleParser.Filter_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_calcs_clause(ObForOracleParser.Add_calcs_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_calcs_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_calcs_clause(ObForOracleParser.Add_calcs_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void enterAdd_calc_meas_clause(ObForOracleParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#add_calc_meas_clause}.
	 * @param ctx the parse tree
	 */
	void exitAdd_calc_meas_clause(ObForOracleParser.Add_calc_meas_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(ObForOracleParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(ObForOracleParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_basic_elements(ObForOracleParser.Subquery_basic_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subquery_basic_elements}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_basic_elements(ObForOracleParser.Subquery_basic_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_operation_part(ObForOracleParser.Subquery_operation_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subquery_operation_part}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_operation_part(ObForOracleParser.Subquery_operation_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#union_context}.
	 * @param ctx the parse tree
	 */
	void enterUnion_context(ObForOracleParser.Union_contextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#union_context}.
	 * @param ctx the parse tree
	 */
	void exitUnion_context(ObForOracleParser.Union_contextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#query_block}.
	 * @param ctx the parse tree
	 */
	void enterQuery_block(ObForOracleParser.Query_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#query_block}.
	 * @param ctx the parse tree
	 */
	void exitQuery_block(ObForOracleParser.Query_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#selected_list}.
	 * @param ctx the parse tree
	 */
	void enterSelected_list(ObForOracleParser.Selected_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#selected_list}.
	 * @param ctx the parse tree
	 */
	void exitSelected_list(ObForOracleParser.Selected_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrom_clause(ObForOracleParser.From_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#from_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrom_clause(ObForOracleParser.From_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#select_list_elements}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list_elements(ObForOracleParser.Select_list_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#select_list_elements}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list_elements(ObForOracleParser.Select_list_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_ref_list}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_list(ObForOracleParser.Table_ref_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_ref_list}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_list(ObForOracleParser.Table_ref_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref(ObForOracleParser.Table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_ref}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref(ObForOracleParser.Table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_ref_aux}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux(ObForOracleParser.Table_ref_auxContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_ref_aux}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux(ObForOracleParser.Table_ref_auxContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_one(ObForOracleParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_one}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_one(ObForOracleParser.Table_ref_aux_internal_oneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_two(ObForOracleParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_two}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_two(ObForOracleParser.Table_ref_aux_internal_twoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void enterTable_ref_aux_internal_thre(ObForOracleParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table_ref_aux_internal_thre}
	 * labeled alternative in {@link ObForOracleParser#table_ref_aux_internal}.
	 * @param ctx the parse tree
	 */
	void exitTable_ref_aux_internal_thre(ObForOracleParser.Table_ref_aux_internal_threContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_clause(ObForOracleParser.Join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_clause(ObForOracleParser.Join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_type}.
	 * @param ctx the parse tree
	 */
	void enterJoin_type(ObForOracleParser.Join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_type}.
	 * @param ctx the parse tree
	 */
	void exitJoin_type(ObForOracleParser.Join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_on_part}.
	 * @param ctx the parse tree
	 */
	void enterJoin_on_part(ObForOracleParser.Join_on_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_on_part}.
	 * @param ctx the parse tree
	 */
	void exitJoin_on_part(ObForOracleParser.Join_on_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#join_using_part}.
	 * @param ctx the parse tree
	 */
	void enterJoin_using_part(ObForOracleParser.Join_using_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#join_using_part}.
	 * @param ctx the parse tree
	 */
	void exitJoin_using_part(ObForOracleParser.Join_using_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#outer_join_type}.
	 * @param ctx the parse tree
	 */
	void enterOuter_join_type(ObForOracleParser.Outer_join_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#outer_join_type}.
	 * @param ctx the parse tree
	 */
	void exitOuter_join_type(ObForOracleParser.Outer_join_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#query_partition_clause}.
	 * @param ctx the parse tree
	 */
	void enterQuery_partition_clause(ObForOracleParser.Query_partition_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#query_partition_clause}.
	 * @param ctx the parse tree
	 */
	void exitQuery_partition_clause(ObForOracleParser.Query_partition_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 */
	void enterFlashback_query_clause(ObForOracleParser.Flashback_query_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#flashback_query_clause}.
	 * @param ctx the parse tree
	 */
	void exitFlashback_query_clause(ObForOracleParser.Flashback_query_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_clause(ObForOracleParser.Pivot_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_clause(ObForOracleParser.Pivot_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_element}.
	 * @param ctx the parse tree
	 */
	void enterPivot_element(ObForOracleParser.Pivot_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_element}.
	 * @param ctx the parse tree
	 */
	void exitPivot_element(ObForOracleParser.Pivot_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_for_clause(ObForOracleParser.Pivot_for_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_for_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_for_clause(ObForOracleParser.Pivot_for_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause(ObForOracleParser.Pivot_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause(ObForOracleParser.Pivot_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause_element(ObForOracleParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_in_clause_element}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause_element(ObForOracleParser.Pivot_in_clause_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 */
	void enterPivot_in_clause_elements(ObForOracleParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#pivot_in_clause_elements}.
	 * @param ctx the parse tree
	 */
	void exitPivot_in_clause_elements(ObForOracleParser.Pivot_in_clause_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unpivot_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_clause(ObForOracleParser.Unpivot_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unpivot_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_clause(ObForOracleParser.Unpivot_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_in_clause(ObForOracleParser.Unpivot_in_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unpivot_in_clause}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_in_clause(ObForOracleParser.Unpivot_in_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 */
	void enterUnpivot_in_elements(ObForOracleParser.Unpivot_in_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unpivot_in_elements}.
	 * @param ctx the parse tree
	 */
	void exitUnpivot_in_elements(ObForOracleParser.Unpivot_in_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_query_clause(ObForOracleParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#hierarchical_query_clause}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_query_clause(ObForOracleParser.Hierarchical_query_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#start_part}.
	 * @param ctx the parse tree
	 */
	void enterStart_part(ObForOracleParser.Start_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#start_part}.
	 * @param ctx the parse tree
	 */
	void exitStart_part(ObForOracleParser.Start_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_clause(ObForOracleParser.Group_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_clause(ObForOracleParser.Group_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#group_by_elements}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_elements(ObForOracleParser.Group_by_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#group_by_elements}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_elements(ObForOracleParser.Group_by_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 */
	void enterRollup_cube_clause(ObForOracleParser.Rollup_cube_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rollup_cube_clause}.
	 * @param ctx the parse tree
	 */
	void exitRollup_cube_clause(ObForOracleParser.Rollup_cube_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_clause(ObForOracleParser.Grouping_sets_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#grouping_sets_clause}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_clause(ObForOracleParser.Grouping_sets_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 */
	void enterGrouping_sets_elements(ObForOracleParser.Grouping_sets_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#grouping_sets_elements}.
	 * @param ctx the parse tree
	 */
	void exitGrouping_sets_elements(ObForOracleParser.Grouping_sets_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(ObForOracleParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(ObForOracleParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_clause(ObForOracleParser.Model_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_clause(ObForOracleParser.Model_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cell_reference_options}.
	 * @param ctx the parse tree
	 */
	void enterCell_reference_options(ObForOracleParser.Cell_reference_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cell_reference_options}.
	 * @param ctx the parse tree
	 */
	void exitCell_reference_options(ObForOracleParser.Cell_reference_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#return_rows_clause}.
	 * @param ctx the parse tree
	 */
	void enterReturn_rows_clause(ObForOracleParser.Return_rows_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#return_rows_clause}.
	 * @param ctx the parse tree
	 */
	void exitReturn_rows_clause(ObForOracleParser.Return_rows_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#reference_model}.
	 * @param ctx the parse tree
	 */
	void enterReference_model(ObForOracleParser.Reference_modelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#reference_model}.
	 * @param ctx the parse tree
	 */
	void exitReference_model(ObForOracleParser.Reference_modelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#main_model}.
	 * @param ctx the parse tree
	 */
	void enterMain_model(ObForOracleParser.Main_modelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#main_model}.
	 * @param ctx the parse tree
	 */
	void exitMain_model(ObForOracleParser.Main_modelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_column_clauses}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_clauses(ObForOracleParser.Model_column_clausesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_column_clauses}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_clauses(ObForOracleParser.Model_column_clausesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_partition_part(ObForOracleParser.Model_column_partition_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_column_partition_part}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_partition_part(ObForOracleParser.Model_column_partition_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_column_list}.
	 * @param ctx the parse tree
	 */
	void enterModel_column_list(ObForOracleParser.Model_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_column_list}.
	 * @param ctx the parse tree
	 */
	void exitModel_column_list(ObForOracleParser.Model_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_column}.
	 * @param ctx the parse tree
	 */
	void enterModel_column(ObForOracleParser.Model_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_column}.
	 * @param ctx the parse tree
	 */
	void exitModel_column(ObForOracleParser.Model_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_rules_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_clause(ObForOracleParser.Model_rules_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_rules_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_clause(ObForOracleParser.Model_rules_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_rules_part}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_part(ObForOracleParser.Model_rules_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_rules_part}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_part(ObForOracleParser.Model_rules_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_rules_element}.
	 * @param ctx the parse tree
	 */
	void enterModel_rules_element(ObForOracleParser.Model_rules_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_rules_element}.
	 * @param ctx the parse tree
	 */
	void exitModel_rules_element(ObForOracleParser.Model_rules_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cell_assignment}.
	 * @param ctx the parse tree
	 */
	void enterCell_assignment(ObForOracleParser.Cell_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cell_assignment}.
	 * @param ctx the parse tree
	 */
	void exitCell_assignment(ObForOracleParser.Cell_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 */
	void enterModel_iterate_clause(ObForOracleParser.Model_iterate_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_iterate_clause}.
	 * @param ctx the parse tree
	 */
	void exitModel_iterate_clause(ObForOracleParser.Model_iterate_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#until_part}.
	 * @param ctx the parse tree
	 */
	void enterUntil_part(ObForOracleParser.Until_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#until_part}.
	 * @param ctx the parse tree
	 */
	void exitUntil_part(ObForOracleParser.Until_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_clause(ObForOracleParser.Order_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#order_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_clause(ObForOracleParser.Order_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#order_by_elements}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_elements(ObForOracleParser.Order_by_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#order_by_elements}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_elements(ObForOracleParser.Order_by_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void enterOffset_clause(ObForOracleParser.Offset_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#offset_clause}.
	 * @param ctx the parse tree
	 */
	void exitOffset_clause(ObForOracleParser.Offset_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#fetch_clause}.
	 * @param ctx the parse tree
	 */
	void enterFetch_clause(ObForOracleParser.Fetch_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#fetch_clause}.
	 * @param ctx the parse tree
	 */
	void exitFetch_clause(ObForOracleParser.Fetch_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#for_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_clause(ObForOracleParser.For_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#for_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_clause(ObForOracleParser.For_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#for_update_of_part}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_of_part(ObForOracleParser.For_update_of_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#for_update_of_part}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_of_part(ObForOracleParser.For_update_of_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#for_update_options}.
	 * @param ctx the parse tree
	 */
	void enterFor_update_options(ObForOracleParser.For_update_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#for_update_options}.
	 * @param ctx the parse tree
	 */
	void exitFor_update_options(ObForOracleParser.For_update_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_statement(ObForOracleParser.Update_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_statement(ObForOracleParser.Update_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#update_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_set_clause(ObForOracleParser.Update_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#update_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_set_clause(ObForOracleParser.Update_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 */
	void enterColumn_based_update_set_clause(ObForOracleParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_based_update_set_clause}.
	 * @param ctx the parse tree
	 */
	void exitColumn_based_update_set_clause(ObForOracleParser.Column_based_update_set_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete_statement(ObForOracleParser.Delete_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete_statement(ObForOracleParser.Delete_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void enterInsert_statement(ObForOracleParser.Insert_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void exitInsert_statement(ObForOracleParser.Insert_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#single_table_insert}.
	 * @param ctx the parse tree
	 */
	void enterSingle_table_insert(ObForOracleParser.Single_table_insertContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#single_table_insert}.
	 * @param ctx the parse tree
	 */
	void exitSingle_table_insert(ObForOracleParser.Single_table_insertContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#multi_table_insert}.
	 * @param ctx the parse tree
	 */
	void enterMulti_table_insert(ObForOracleParser.Multi_table_insertContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#multi_table_insert}.
	 * @param ctx the parse tree
	 */
	void exitMulti_table_insert(ObForOracleParser.Multi_table_insertContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#multi_table_element}.
	 * @param ctx the parse tree
	 */
	void enterMulti_table_element(ObForOracleParser.Multi_table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#multi_table_element}.
	 * @param ctx the parse tree
	 */
	void exitMulti_table_element(ObForOracleParser.Multi_table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_clause(ObForOracleParser.Conditional_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#conditional_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_clause(ObForOracleParser.Conditional_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_when_part(ObForOracleParser.Conditional_insert_when_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#conditional_insert_when_part}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_when_part(ObForOracleParser.Conditional_insert_when_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 */
	void enterConditional_insert_else_part(ObForOracleParser.Conditional_insert_else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#conditional_insert_else_part}.
	 * @param ctx the parse tree
	 */
	void exitConditional_insert_else_part(ObForOracleParser.Conditional_insert_else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#insert_into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInsert_into_clause(ObForOracleParser.Insert_into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#insert_into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInsert_into_clause(ObForOracleParser.Insert_into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void enterValues_clause(ObForOracleParser.Values_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#values_clause}.
	 * @param ctx the parse tree
	 */
	void exitValues_clause(ObForOracleParser.Values_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_statement}.
	 * @param ctx the parse tree
	 */
	void enterMerge_statement(ObForOracleParser.Merge_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_statement}.
	 * @param ctx the parse tree
	 */
	void exitMerge_statement(ObForOracleParser.Merge_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_clause(ObForOracleParser.Merge_update_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_update_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_clause(ObForOracleParser.Merge_update_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_element}.
	 * @param ctx the parse tree
	 */
	void enterMerge_element(ObForOracleParser.Merge_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_element}.
	 * @param ctx the parse tree
	 */
	void exitMerge_element(ObForOracleParser.Merge_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 */
	void enterMerge_update_delete_part(ObForOracleParser.Merge_update_delete_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_update_delete_part}.
	 * @param ctx the parse tree
	 */
	void exitMerge_update_delete_part(ObForOracleParser.Merge_update_delete_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void enterMerge_insert_clause(ObForOracleParser.Merge_insert_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#merge_insert_clause}.
	 * @param ctx the parse tree
	 */
	void exitMerge_insert_clause(ObForOracleParser.Merge_insert_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#selected_tableview}.
	 * @param ctx the parse tree
	 */
	void enterSelected_tableview(ObForOracleParser.Selected_tableviewContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#selected_tableview}.
	 * @param ctx the parse tree
	 */
	void exitSelected_tableview(ObForOracleParser.Selected_tableviewContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lock_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterLock_table_statement(ObForOracleParser.Lock_table_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lock_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitLock_table_statement(ObForOracleParser.Lock_table_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 */
	void enterWait_nowait_part(ObForOracleParser.Wait_nowait_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#wait_nowait_part}.
	 * @param ctx the parse tree
	 */
	void exitWait_nowait_part(ObForOracleParser.Wait_nowait_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lock_table_element}.
	 * @param ctx the parse tree
	 */
	void enterLock_table_element(ObForOracleParser.Lock_table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lock_table_element}.
	 * @param ctx the parse tree
	 */
	void exitLock_table_element(ObForOracleParser.Lock_table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#lock_mode}.
	 * @param ctx the parse tree
	 */
	void enterLock_mode(ObForOracleParser.Lock_modeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#lock_mode}.
	 * @param ctx the parse tree
	 */
	void exitLock_mode(ObForOracleParser.Lock_modeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#general_table_ref}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_table_ref(ObForOracleParser.General_table_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#general_table_ref}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_table_ref(ObForOracleParser.General_table_refContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#static_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterStatic_returning_clause(ObForOracleParser.Static_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#static_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitStatic_returning_clause(ObForOracleParser.Static_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#error_logging_clause}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_clause(ObForOracleParser.Error_logging_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#error_logging_clause}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_clause(ObForOracleParser.Error_logging_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_into_part(ObForOracleParser.Error_logging_into_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#error_logging_into_part}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_into_part(ObForOracleParser.Error_logging_into_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 */
	void enterError_logging_reject_part(ObForOracleParser.Error_logging_reject_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#error_logging_reject_part}.
	 * @param ctx the parse tree
	 */
	void exitError_logging_reject_part(ObForOracleParser.Error_logging_reject_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 */
	void enterDml_table_expression_clause(ObForOracleParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dml_table_expression_clause}.
	 * @param ctx the parse tree
	 */
	void exitDml_table_expression_clause(ObForOracleParser.Dml_table_expression_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_collection_expression}.
	 * @param ctx the parse tree
	 */
	void enterTable_collection_expression(ObForOracleParser.Table_collection_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_collection_expression}.
	 * @param ctx the parse tree
	 */
	void exitTable_collection_expression(ObForOracleParser.Table_collection_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_restriction_clause(ObForOracleParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#subquery_restriction_clause}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_restriction_clause(ObForOracleParser.Subquery_restriction_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sample_clause}.
	 * @param ctx the parse tree
	 */
	void enterSample_clause(ObForOracleParser.Sample_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sample_clause}.
	 * @param ctx the parse tree
	 */
	void exitSample_clause(ObForOracleParser.Sample_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#seed_part}.
	 * @param ctx the parse tree
	 */
	void enterSeed_part(ObForOracleParser.Seed_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#seed_part}.
	 * @param ctx the parse tree
	 */
	void exitSeed_part(ObForOracleParser.Seed_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(ObForOracleParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(ObForOracleParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#expressions_}.
	 * @param ctx the parse tree
	 */
	void enterExpressions_(ObForOracleParser.Expressions_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#expressions_}.
	 * @param ctx the parse tree
	 */
	void exitExpressions_(ObForOracleParser.Expressions_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ObForOracleParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ObForOracleParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cursor_expression}.
	 * @param ctx the parse tree
	 */
	void enterCursor_expression(ObForOracleParser.Cursor_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cursor_expression}.
	 * @param ctx the parse tree
	 */
	void exitCursor_expression(ObForOracleParser.Cursor_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogical_expression(ObForOracleParser.Logical_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogical_expression(ObForOracleParser.Logical_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_logical_expression(ObForOracleParser.Unary_logical_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unary_logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_logical_expression(ObForOracleParser.Unary_logical_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 */
	void enterUnary_logical_operation(ObForOracleParser.Unary_logical_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unary_logical_operation}.
	 * @param ctx the parse tree
	 */
	void exitUnary_logical_operation(ObForOracleParser.Unary_logical_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void enterLogical_operation(ObForOracleParser.Logical_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void exitLogical_operation(ObForOracleParser.Logical_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#multiset_expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiset_expression(ObForOracleParser.Multiset_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#multiset_expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiset_expression(ObForOracleParser.Multiset_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#relational_expression}.
	 * @param ctx the parse tree
	 */
	void enterRelational_expression(ObForOracleParser.Relational_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#relational_expression}.
	 * @param ctx the parse tree
	 */
	void exitRelational_expression(ObForOracleParser.Relational_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#compound_expression}.
	 * @param ctx the parse tree
	 */
	void enterCompound_expression(ObForOracleParser.Compound_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#compound_expression}.
	 * @param ctx the parse tree
	 */
	void exitCompound_expression(ObForOracleParser.Compound_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#relational_operator}.
	 * @param ctx the parse tree
	 */
	void enterRelational_operator(ObForOracleParser.Relational_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#relational_operator}.
	 * @param ctx the parse tree
	 */
	void exitRelational_operator(ObForOracleParser.Relational_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#in_elements}.
	 * @param ctx the parse tree
	 */
	void enterIn_elements(ObForOracleParser.In_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#in_elements}.
	 * @param ctx the parse tree
	 */
	void exitIn_elements(ObForOracleParser.In_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#between_elements}.
	 * @param ctx the parse tree
	 */
	void enterBetween_elements(ObForOracleParser.Between_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#between_elements}.
	 * @param ctx the parse tree
	 */
	void exitBetween_elements(ObForOracleParser.Between_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(ObForOracleParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(ObForOracleParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#interval_expression}.
	 * @param ctx the parse tree
	 */
	void enterInterval_expression(ObForOracleParser.Interval_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#interval_expression}.
	 * @param ctx the parse tree
	 */
	void exitInterval_expression(ObForOracleParser.Interval_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_expression}.
	 * @param ctx the parse tree
	 */
	void enterModel_expression(ObForOracleParser.Model_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_expression}.
	 * @param ctx the parse tree
	 */
	void exitModel_expression(ObForOracleParser.Model_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#model_expression_element}.
	 * @param ctx the parse tree
	 */
	void enterModel_expression_element(ObForOracleParser.Model_expression_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#model_expression_element}.
	 * @param ctx the parse tree
	 */
	void exitModel_expression_element(ObForOracleParser.Model_expression_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void enterSingle_column_for_loop(ObForOracleParser.Single_column_for_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#single_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void exitSingle_column_for_loop(ObForOracleParser.Single_column_for_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void enterMulti_column_for_loop(ObForOracleParser.Multi_column_for_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#multi_column_for_loop}.
	 * @param ctx the parse tree
	 */
	void exitMulti_column_for_loop(ObForOracleParser.Multi_column_for_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(ObForOracleParser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(ObForOracleParser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 */
	void enterImplicit_cursor_expression(ObForOracleParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#implicit_cursor_expression}.
	 * @param ctx the parse tree
	 */
	void exitImplicit_cursor_expression(ObForOracleParser.Implicit_cursor_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collection_expression}.
	 * @param ctx the parse tree
	 */
	void enterCollection_expression(ObForOracleParser.Collection_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collection_expression}.
	 * @param ctx the parse tree
	 */
	void exitCollection_expression(ObForOracleParser.Collection_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_statement(ObForOracleParser.Case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_statement(ObForOracleParser.Case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#simple_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterSimple_case_statement(ObForOracleParser.Simple_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#simple_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitSimple_case_statement(ObForOracleParser.Simple_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#searched_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterSearched_case_statement(ObForOracleParser.Searched_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#searched_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitSearched_case_statement(ObForOracleParser.Searched_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_when_part_statement(ObForOracleParser.Case_when_part_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_when_part_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_when_part_statement(ObForOracleParser.Case_when_part_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_else_part_statement(ObForOracleParser.Case_else_part_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_else_part_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_else_part_statement(ObForOracleParser.Case_else_part_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_expression(ObForOracleParser.Case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_expression(ObForOracleParser.Case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#simple_case_expression}.
	 * @param ctx the parse tree
	 */
	void enterSimple_case_expression(ObForOracleParser.Simple_case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#simple_case_expression}.
	 * @param ctx the parse tree
	 */
	void exitSimple_case_expression(ObForOracleParser.Simple_case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#searched_case_expression}.
	 * @param ctx the parse tree
	 */
	void enterSearched_case_expression(ObForOracleParser.Searched_case_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#searched_case_expression}.
	 * @param ctx the parse tree
	 */
	void exitSearched_case_expression(ObForOracleParser.Searched_case_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_when_part_expression(ObForOracleParser.Case_when_part_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_when_part_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_when_part_expression(ObForOracleParser.Case_when_part_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 */
	void enterCase_else_part_expression(ObForOracleParser.Case_else_part_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#case_else_part_expression}.
	 * @param ctx the parse tree
	 */
	void exitCase_else_part_expression(ObForOracleParser.Case_else_part_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(ObForOracleParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(ObForOracleParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#quantified_expression}.
	 * @param ctx the parse tree
	 */
	void enterQuantified_expression(ObForOracleParser.Quantified_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#quantified_expression}.
	 * @param ctx the parse tree
	 */
	void exitQuantified_expression(ObForOracleParser.Quantified_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#string_function}.
	 * @param ctx the parse tree
	 */
	void enterString_function(ObForOracleParser.String_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#string_function}.
	 * @param ctx the parse tree
	 */
	void exitString_function(ObForOracleParser.String_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#standard_function}.
	 * @param ctx the parse tree
	 */
	void enterStandard_function(ObForOracleParser.Standard_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#standard_function}.
	 * @param ctx the parse tree
	 */
	void exitStandard_function(ObForOracleParser.Standard_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_function}.
	 * @param ctx the parse tree
	 */
	void enterJson_function(ObForOracleParser.Json_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_function}.
	 * @param ctx the parse tree
	 */
	void exitJson_function(ObForOracleParser.Json_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_object_content}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_content(ObForOracleParser.Json_object_contentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_object_content}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_content(ObForOracleParser.Json_object_contentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_object_entry}.
	 * @param ctx the parse tree
	 */
	void enterJson_object_entry(ObForOracleParser.Json_object_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_object_entry}.
	 * @param ctx the parse tree
	 */
	void exitJson_object_entry(ObForOracleParser.Json_object_entryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_table_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_table_clause(ObForOracleParser.Json_table_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_table_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_table_clause(ObForOracleParser.Json_table_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_array_element}.
	 * @param ctx the parse tree
	 */
	void enterJson_array_element(ObForOracleParser.Json_array_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_array_element}.
	 * @param ctx the parse tree
	 */
	void exitJson_array_element(ObForOracleParser.Json_array_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_on_null_clause(ObForOracleParser.Json_on_null_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_on_null_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_on_null_clause(ObForOracleParser.Json_on_null_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_return_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_return_clause(ObForOracleParser.Json_return_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_return_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_return_clause(ObForOracleParser.Json_return_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_transform_op}.
	 * @param ctx the parse tree
	 */
	void enterJson_transform_op(ObForOracleParser.Json_transform_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_transform_op}.
	 * @param ctx the parse tree
	 */
	void exitJson_transform_op(ObForOracleParser.Json_transform_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_column_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_column_clause(ObForOracleParser.Json_column_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_column_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_column_clause(ObForOracleParser.Json_column_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_column_definition}.
	 * @param ctx the parse tree
	 */
	void enterJson_column_definition(ObForOracleParser.Json_column_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_column_definition}.
	 * @param ctx the parse tree
	 */
	void exitJson_column_definition(ObForOracleParser.Json_column_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_returning_clause(ObForOracleParser.Json_query_returning_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_query_returning_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_returning_clause(ObForOracleParser.Json_query_returning_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_query_return_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_return_type(ObForOracleParser.Json_query_return_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_query_return_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_return_type(ObForOracleParser.Json_query_return_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_wrapper_clause(ObForOracleParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_query_wrapper_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_wrapper_clause(ObForOracleParser.Json_query_wrapper_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_on_error_clause(ObForOracleParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_query_on_error_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_on_error_clause(ObForOracleParser.Json_query_on_error_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_query_on_empty_clause(ObForOracleParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_query_on_empty_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_query_on_empty_clause(ObForOracleParser.Json_query_on_empty_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_return_clause(ObForOracleParser.Json_value_return_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_value_return_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_return_clause(ObForOracleParser.Json_value_return_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_value_return_type}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_return_type(ObForOracleParser.Json_value_return_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_value_return_type}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_return_type(ObForOracleParser.Json_value_return_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 */
	void enterJson_value_on_mismatch_clause(ObForOracleParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#json_value_on_mismatch_clause}.
	 * @param ctx the parse tree
	 */
	void exitJson_value_on_mismatch_clause(ObForOracleParser.Json_value_on_mismatch_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ObForOracleParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ObForOracleParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function_wrapper(ObForOracleParser.Numeric_function_wrapperContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#numeric_function_wrapper}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function_wrapper(ObForOracleParser.Numeric_function_wrapperContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#numeric_function}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function(ObForOracleParser.Numeric_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#numeric_function}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function(ObForOracleParser.Numeric_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void enterListagg_overflow_clause(ObForOracleParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#listagg_overflow_clause}.
	 * @param ctx the parse tree
	 */
	void exitListagg_overflow_clause(ObForOracleParser.Listagg_overflow_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#other_function}.
	 * @param ctx the parse tree
	 */
	void enterOther_function(ObForOracleParser.Other_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#other_function}.
	 * @param ctx the parse tree
	 */
	void exitOther_function(ObForOracleParser.Other_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause_keyword(ObForOracleParser.Over_clause_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause_keyword(ObForOracleParser.Over_clause_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void enterWithin_or_over_clause_keyword(ObForOracleParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#within_or_over_clause_keyword}.
	 * @param ctx the parse tree
	 */
	void exitWithin_or_over_clause_keyword(ObForOracleParser.Within_or_over_clause_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 */
	void enterStandard_prediction_function_keyword(ObForOracleParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#standard_prediction_function_keyword}.
	 * @param ctx the parse tree
	 */
	void exitStandard_prediction_function_keyword(ObForOracleParser.Standard_prediction_function_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void enterOver_clause(ObForOracleParser.Over_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#over_clause}.
	 * @param ctx the parse tree
	 */
	void exitOver_clause(ObForOracleParser.Over_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#windowing_clause}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_clause(ObForOracleParser.Windowing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#windowing_clause}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_clause(ObForOracleParser.Windowing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#windowing_type}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_type(ObForOracleParser.Windowing_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#windowing_type}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_type(ObForOracleParser.Windowing_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#windowing_elements}.
	 * @param ctx the parse tree
	 */
	void enterWindowing_elements(ObForOracleParser.Windowing_elementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#windowing_elements}.
	 * @param ctx the parse tree
	 */
	void exitWindowing_elements(ObForOracleParser.Windowing_elementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void enterUsing_clause(ObForOracleParser.Using_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_clause}.
	 * @param ctx the parse tree
	 */
	void exitUsing_clause(ObForOracleParser.Using_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#using_element}.
	 * @param ctx the parse tree
	 */
	void enterUsing_element(ObForOracleParser.Using_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#using_element}.
	 * @param ctx the parse tree
	 */
	void exitUsing_element(ObForOracleParser.Using_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 */
	void enterCollect_order_by_part(ObForOracleParser.Collect_order_by_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collect_order_by_part}.
	 * @param ctx the parse tree
	 */
	void exitCollect_order_by_part(ObForOracleParser.Collect_order_by_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#within_or_over_part}.
	 * @param ctx the parse tree
	 */
	void enterWithin_or_over_part(ObForOracleParser.Within_or_over_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#within_or_over_part}.
	 * @param ctx the parse tree
	 */
	void exitWithin_or_over_part(ObForOracleParser.Within_or_over_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#string_delimiter}.
	 * @param ctx the parse tree
	 */
	void enterString_delimiter(ObForOracleParser.String_delimiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#string_delimiter}.
	 * @param ctx the parse tree
	 */
	void exitString_delimiter(ObForOracleParser.String_delimiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 */
	void enterCost_matrix_clause(ObForOracleParser.Cost_matrix_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cost_matrix_clause}.
	 * @param ctx the parse tree
	 */
	void exitCost_matrix_clause(ObForOracleParser.Cost_matrix_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_passing_clause(ObForOracleParser.Xml_passing_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_passing_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_passing_clause(ObForOracleParser.Xml_passing_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_attributes_clause(ObForOracleParser.Xml_attributes_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_attributes_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_attributes_clause(ObForOracleParser.Xml_attributes_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 */
	void enterXml_namespaces_clause(ObForOracleParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_namespaces_clause}.
	 * @param ctx the parse tree
	 */
	void exitXml_namespaces_clause(ObForOracleParser.Xml_namespaces_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_table_column}.
	 * @param ctx the parse tree
	 */
	void enterXml_table_column(ObForOracleParser.Xml_table_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_table_column}.
	 * @param ctx the parse tree
	 */
	void exitXml_table_column(ObForOracleParser.Xml_table_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 */
	void enterXml_general_default_part(ObForOracleParser.Xml_general_default_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_general_default_part}.
	 * @param ctx the parse tree
	 */
	void exitXml_general_default_part(ObForOracleParser.Xml_general_default_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 */
	void enterXml_multiuse_expression_element(ObForOracleParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_multiuse_expression_element}.
	 * @param ctx the parse tree
	 */
	void exitXml_multiuse_expression_element(ObForOracleParser.Xml_multiuse_expression_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlroot_param_version_part(ObForOracleParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlroot_param_version_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlroot_param_version_part(ObForOracleParser.Xmlroot_param_version_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlroot_param_standalone_part(ObForOracleParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlroot_param_standalone_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlroot_param_standalone_part(ObForOracleParser.Xmlroot_param_standalone_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_enconding_part(ObForOracleParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_enconding_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_enconding_part(ObForOracleParser.Xmlserialize_param_enconding_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_version_part(ObForOracleParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_version_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_version_part(ObForOracleParser.Xmlserialize_param_version_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 */
	void enterXmlserialize_param_ident_part(ObForOracleParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmlserialize_param_ident_part}.
	 * @param ctx the parse tree
	 */
	void exitXmlserialize_param_ident_part(ObForOracleParser.Xmlserialize_param_ident_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 */
	void enterSql_plus_command_no_semicolon(ObForOracleParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_plus_command_no_semicolon}.
	 * @param ctx the parse tree
	 */
	void exitSql_plus_command_no_semicolon(ObForOracleParser.Sql_plus_command_no_semicolonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sql_plus_command}.
	 * @param ctx the parse tree
	 */
	void enterSql_plus_command(ObForOracleParser.Sql_plus_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sql_plus_command}.
	 * @param ctx the parse tree
	 */
	void exitSql_plus_command(ObForOracleParser.Sql_plus_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#start_command}.
	 * @param ctx the parse tree
	 */
	void enterStart_command(ObForOracleParser.Start_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#start_command}.
	 * @param ctx the parse tree
	 */
	void exitStart_command(ObForOracleParser.Start_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#whenever_command}.
	 * @param ctx the parse tree
	 */
	void enterWhenever_command(ObForOracleParser.Whenever_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#whenever_command}.
	 * @param ctx the parse tree
	 */
	void exitWhenever_command(ObForOracleParser.Whenever_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#set_command}.
	 * @param ctx the parse tree
	 */
	void enterSet_command(ObForOracleParser.Set_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#set_command}.
	 * @param ctx the parse tree
	 */
	void exitSet_command(ObForOracleParser.Set_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#timing_command}.
	 * @param ctx the parse tree
	 */
	void enterTiming_command(ObForOracleParser.Timing_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#timing_command}.
	 * @param ctx the parse tree
	 */
	void exitTiming_command(ObForOracleParser.Timing_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 */
	void enterPartition_extension_clause(ObForOracleParser.Partition_extension_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#partition_extension_clause}.
	 * @param ctx the parse tree
	 */
	void exitPartition_extension_clause(ObForOracleParser.Partition_extension_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void enterColumn_alias(ObForOracleParser.Column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void exitColumn_alias(ObForOracleParser.Column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(ObForOracleParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(ObForOracleParser.Table_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(ObForOracleParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(ObForOracleParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void enterInto_clause(ObForOracleParser.Into_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#into_clause}.
	 * @param ctx the parse tree
	 */
	void exitInto_clause(ObForOracleParser.Into_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xml_column_name}.
	 * @param ctx the parse tree
	 */
	void enterXml_column_name(ObForOracleParser.Xml_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xml_column_name}.
	 * @param ctx the parse tree
	 */
	void exitXml_column_name(ObForOracleParser.Xml_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cost_class_name}.
	 * @param ctx the parse tree
	 */
	void enterCost_class_name(ObForOracleParser.Cost_class_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cost_class_name}.
	 * @param ctx the parse tree
	 */
	void exitCost_class_name(ObForOracleParser.Cost_class_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#attribute_name}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_name(ObForOracleParser.Attribute_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#attribute_name}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_name(ObForOracleParser.Attribute_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_name(ObForOracleParser.Savepoint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_name(ObForOracleParser.Savepoint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 */
	void enterRollback_segment_name(ObForOracleParser.Rollback_segment_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#rollback_segment_name}.
	 * @param ctx the parse tree
	 */
	void exitRollback_segment_name(ObForOracleParser.Rollback_segment_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_var_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_var_name(ObForOracleParser.Table_var_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_var_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_var_name(ObForOracleParser.Table_var_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void enterSchema_name(ObForOracleParser.Schema_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void exitSchema_name(ObForOracleParser.Schema_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#routine_name}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_name(ObForOracleParser.Routine_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#routine_name}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_name(ObForOracleParser.Routine_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#package_name}.
	 * @param ctx the parse tree
	 */
	void enterPackage_name(ObForOracleParser.Package_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#package_name}.
	 * @param ctx the parse tree
	 */
	void exitPackage_name(ObForOracleParser.Package_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#implementation_type_name}.
	 * @param ctx the parse tree
	 */
	void enterImplementation_type_name(ObForOracleParser.Implementation_type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#implementation_type_name}.
	 * @param ctx the parse tree
	 */
	void exitImplementation_type_name(ObForOracleParser.Implementation_type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#parameter_name}.
	 * @param ctx the parse tree
	 */
	void enterParameter_name(ObForOracleParser.Parameter_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#parameter_name}.
	 * @param ctx the parse tree
	 */
	void exitParameter_name(ObForOracleParser.Parameter_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#reference_model_name}.
	 * @param ctx the parse tree
	 */
	void enterReference_model_name(ObForOracleParser.Reference_model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#reference_model_name}.
	 * @param ctx the parse tree
	 */
	void exitReference_model_name(ObForOracleParser.Reference_model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#main_model_name}.
	 * @param ctx the parse tree
	 */
	void enterMain_model_name(ObForOracleParser.Main_model_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#main_model_name}.
	 * @param ctx the parse tree
	 */
	void exitMain_model_name(ObForOracleParser.Main_model_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#container_tableview_name}.
	 * @param ctx the parse tree
	 */
	void enterContainer_tableview_name(ObForOracleParser.Container_tableview_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#container_tableview_name}.
	 * @param ctx the parse tree
	 */
	void exitContainer_tableview_name(ObForOracleParser.Container_tableview_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_function_name(ObForOracleParser.Aggregate_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#aggregate_function_name}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_function_name(ObForOracleParser.Aggregate_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#query_name}.
	 * @param ctx the parse tree
	 */
	void enterQuery_name(ObForOracleParser.Query_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#query_name}.
	 * @param ctx the parse tree
	 */
	void exitQuery_name(ObForOracleParser.Query_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#grantee_name}.
	 * @param ctx the parse tree
	 */
	void enterGrantee_name(ObForOracleParser.Grantee_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#grantee_name}.
	 * @param ctx the parse tree
	 */
	void exitGrantee_name(ObForOracleParser.Grantee_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#role_name}.
	 * @param ctx the parse tree
	 */
	void enterRole_name(ObForOracleParser.Role_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#role_name}.
	 * @param ctx the parse tree
	 */
	void exitRole_name(ObForOracleParser.Role_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constraint_name}.
	 * @param ctx the parse tree
	 */
	void enterConstraint_name(ObForOracleParser.Constraint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constraint_name}.
	 * @param ctx the parse tree
	 */
	void exitConstraint_name(ObForOracleParser.Constraint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#label_name}.
	 * @param ctx the parse tree
	 */
	void enterLabel_name(ObForOracleParser.Label_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#label_name}.
	 * @param ctx the parse tree
	 */
	void exitLabel_name(ObForOracleParser.Label_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_name}.
	 * @param ctx the parse tree
	 */
	void enterType_name(ObForOracleParser.Type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_name}.
	 * @param ctx the parse tree
	 */
	void exitType_name(ObForOracleParser.Type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void enterSequence_name(ObForOracleParser.Sequence_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void exitSequence_name(ObForOracleParser.Sequence_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#exception_name}.
	 * @param ctx the parse tree
	 */
	void enterException_name(ObForOracleParser.Exception_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#exception_name}.
	 * @param ctx the parse tree
	 */
	void exitException_name(ObForOracleParser.Exception_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_name}.
	 * @param ctx the parse tree
	 */
	void enterFunction_name(ObForOracleParser.Function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_name}.
	 * @param ctx the parse tree
	 */
	void exitFunction_name(ObForOracleParser.Function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#procedure_name}.
	 * @param ctx the parse tree
	 */
	void enterProcedure_name(ObForOracleParser.Procedure_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#procedure_name}.
	 * @param ctx the parse tree
	 */
	void exitProcedure_name(ObForOracleParser.Procedure_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_name(ObForOracleParser.Trigger_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_name(ObForOracleParser.Trigger_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#variable_name}.
	 * @param ctx the parse tree
	 */
	void enterVariable_name(ObForOracleParser.Variable_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#variable_name}.
	 * @param ctx the parse tree
	 */
	void exitVariable_name(ObForOracleParser.Variable_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#index_name}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name(ObForOracleParser.Index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#index_name}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name(ObForOracleParser.Index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void enterCursor_name(ObForOracleParser.Cursor_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#cursor_name}.
	 * @param ctx the parse tree
	 */
	void exitCursor_name(ObForOracleParser.Cursor_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#record_name}.
	 * @param ctx the parse tree
	 */
	void enterRecord_name(ObForOracleParser.Record_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#record_name}.
	 * @param ctx the parse tree
	 */
	void exitRecord_name(ObForOracleParser.Record_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#collection_name}.
	 * @param ctx the parse tree
	 */
	void enterCollection_name(ObForOracleParser.Collection_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#collection_name}.
	 * @param ctx the parse tree
	 */
	void exitCollection_name(ObForOracleParser.Collection_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#link_name}.
	 * @param ctx the parse tree
	 */
	void enterLink_name(ObForOracleParser.Link_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#link_name}.
	 * @param ctx the parse tree
	 */
	void exitLink_name(ObForOracleParser.Link_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#local_link_name}.
	 * @param ctx the parse tree
	 */
	void enterLocal_link_name(ObForOracleParser.Local_link_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#local_link_name}.
	 * @param ctx the parse tree
	 */
	void exitLocal_link_name(ObForOracleParser.Local_link_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#connection_qualifier}.
	 * @param ctx the parse tree
	 */
	void enterConnection_qualifier(ObForOracleParser.Connection_qualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#connection_qualifier}.
	 * @param ctx the parse tree
	 */
	void exitConnection_qualifier(ObForOracleParser.Connection_qualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(ObForOracleParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(ObForOracleParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#tableview_name}.
	 * @param ctx the parse tree
	 */
	void enterTableview_name(ObForOracleParser.Tableview_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#tableview_name}.
	 * @param ctx the parse tree
	 */
	void exitTableview_name(ObForOracleParser.Tableview_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void enterXmltable(ObForOracleParser.XmltableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#xmltable}.
	 * @param ctx the parse tree
	 */
	void exitXmltable(ObForOracleParser.XmltableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#char_set_name}.
	 * @param ctx the parse tree
	 */
	void enterChar_set_name(ObForOracleParser.Char_set_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#char_set_name}.
	 * @param ctx the parse tree
	 */
	void exitChar_set_name(ObForOracleParser.Char_set_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#synonym_name}.
	 * @param ctx the parse tree
	 */
	void enterSynonym_name(ObForOracleParser.Synonym_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#synonym_name}.
	 * @param ctx the parse tree
	 */
	void exitSynonym_name(ObForOracleParser.Synonym_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#schema_object_name}.
	 * @param ctx the parse tree
	 */
	void enterSchema_object_name(ObForOracleParser.Schema_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#schema_object_name}.
	 * @param ctx the parse tree
	 */
	void exitSchema_object_name(ObForOracleParser.Schema_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#dir_object_name}.
	 * @param ctx the parse tree
	 */
	void enterDir_object_name(ObForOracleParser.Dir_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#dir_object_name}.
	 * @param ctx the parse tree
	 */
	void exitDir_object_name(ObForOracleParser.Dir_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#user_object_name}.
	 * @param ctx the parse tree
	 */
	void enterUser_object_name(ObForOracleParser.User_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#user_object_name}.
	 * @param ctx the parse tree
	 */
	void exitUser_object_name(ObForOracleParser.User_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#grant_object_name}.
	 * @param ctx the parse tree
	 */
	void enterGrant_object_name(ObForOracleParser.Grant_object_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#grant_object_name}.
	 * @param ctx the parse tree
	 */
	void exitGrant_object_name(ObForOracleParser.Grant_object_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#column_list}.
	 * @param ctx the parse tree
	 */
	void enterColumn_list(ObForOracleParser.Column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#column_list}.
	 * @param ctx the parse tree
	 */
	void exitColumn_list(ObForOracleParser.Column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void enterInsert_column_list(ObForOracleParser.Insert_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#insert_column_list}.
	 * @param ctx the parse tree
	 */
	void exitInsert_column_list(ObForOracleParser.Insert_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#paren_column_list}.
	 * @param ctx the parse tree
	 */
	void enterParen_column_list(ObForOracleParser.Paren_column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#paren_column_list}.
	 * @param ctx the parse tree
	 */
	void exitParen_column_list(ObForOracleParser.Paren_column_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#keep_clause}.
	 * @param ctx the parse tree
	 */
	void enterKeep_clause(ObForOracleParser.Keep_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#keep_clause}.
	 * @param ctx the parse tree
	 */
	void exitKeep_clause(ObForOracleParser.Keep_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_argument}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument(ObForOracleParser.Function_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_argument}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument(ObForOracleParser.Function_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument_analytic(ObForOracleParser.Function_argument_analyticContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_argument_analytic}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument_analytic(ObForOracleParser.Function_argument_analyticContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 */
	void enterFunction_argument_modeling(ObForOracleParser.Function_argument_modelingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_argument_modeling}.
	 * @param ctx the parse tree
	 */
	void exitFunction_argument_modeling(ObForOracleParser.Function_argument_modelingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 */
	void enterRespect_or_ignore_nulls(ObForOracleParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#respect_or_ignore_nulls}.
	 * @param ctx the parse tree
	 */
	void exitRespect_or_ignore_nulls(ObForOracleParser.Respect_or_ignore_nullsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(ObForOracleParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(ObForOracleParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_spec(ObForOracleParser.Type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_spec(ObForOracleParser.Type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#custom_type}.
	 * @param ctx the parse tree
	 */
	void enterCustom_type(ObForOracleParser.Custom_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#custom_type}.
	 * @param ctx the parse tree
	 */
	void exitCustom_type(ObForOracleParser.Custom_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(ObForOracleParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(ObForOracleParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#interval_type}.
	 * @param ctx the parse tree
	 */
	void enterInterval_type(ObForOracleParser.Interval_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#interval_type}.
	 * @param ctx the parse tree
	 */
	void exitInterval_type(ObForOracleParser.Interval_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#precision_part}.
	 * @param ctx the parse tree
	 */
	void enterPrecision_part(ObForOracleParser.Precision_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#precision_part}.
	 * @param ctx the parse tree
	 */
	void exitPrecision_part(ObForOracleParser.Precision_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#native_datatype_element}.
	 * @param ctx the parse tree
	 */
	void enterNative_datatype_element(ObForOracleParser.Native_datatype_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#native_datatype_element}.
	 * @param ctx the parse tree
	 */
	void exitNative_datatype_element(ObForOracleParser.Native_datatype_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#bind_variable}.
	 * @param ctx the parse tree
	 */
	void enterBind_variable(ObForOracleParser.Bind_variableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#bind_variable}.
	 * @param ctx the parse tree
	 */
	void exitBind_variable(ObForOracleParser.Bind_variableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#columnref}.
	 * @param ctx the parse tree
	 */
	void enterColumnref(ObForOracleParser.ColumnrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#columnref}.
	 * @param ctx the parse tree
	 */
	void exitColumnref(ObForOracleParser.ColumnrefContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#general_element}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_element(ObForOracleParser.General_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#general_element}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_element(ObForOracleParser.General_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#general_element_part}.
	 * @param ctx the parse tree
	 */
	void enterGeneral_element_part(ObForOracleParser.General_element_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#general_element_part}.
	 * @param ctx the parse tree
	 */
	void exitGeneral_element_part(ObForOracleParser.General_element_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(ObForOracleParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(ObForOracleParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#keyword_function}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_function(ObForOracleParser.Keyword_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#keyword_function}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_function(ObForOracleParser.Keyword_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#table_element}.
	 * @param ctx the parse tree
	 */
	void enterTable_element(ObForOracleParser.Table_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#table_element}.
	 * @param ctx the parse tree
	 */
	void exitTable_element(ObForOracleParser.Table_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#object_privilege}.
	 * @param ctx the parse tree
	 */
	void enterObject_privilege(ObForOracleParser.Object_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#object_privilege}.
	 * @param ctx the parse tree
	 */
	void exitObject_privilege(ObForOracleParser.Object_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#system_privilege}.
	 * @param ctx the parse tree
	 */
	void enterSystem_privilege(ObForOracleParser.System_privilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#system_privilege}.
	 * @param ctx the parse tree
	 */
	void exitSystem_privilege(ObForOracleParser.System_privilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(ObForOracleParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(ObForOracleParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#numeric}.
	 * @param ctx the parse tree
	 */
	void enterNumeric(ObForOracleParser.NumericContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#numeric}.
	 * @param ctx the parse tree
	 */
	void exitNumeric(ObForOracleParser.NumericContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#numeric_negative}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_negative(ObForOracleParser.Numeric_negativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#numeric_negative}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_negative(ObForOracleParser.Numeric_negativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#quoted_string}.
	 * @param ctx the parse tree
	 */
	void enterQuoted_string(ObForOracleParser.Quoted_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#quoted_string}.
	 * @param ctx the parse tree
	 */
	void exitQuoted_string(ObForOracleParser.Quoted_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(ObForOracleParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(ObForOracleParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#id_expression}.
	 * @param ctx the parse tree
	 */
	void enterId_expression(ObForOracleParser.Id_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#id_expression}.
	 * @param ctx the parse tree
	 */
	void exitId_expression(ObForOracleParser.Id_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#inquiry_directive}.
	 * @param ctx the parse tree
	 */
	void enterInquiry_directive(ObForOracleParser.Inquiry_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#inquiry_directive}.
	 * @param ctx the parse tree
	 */
	void exitInquiry_directive(ObForOracleParser.Inquiry_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#outer_join_sign}.
	 * @param ctx the parse tree
	 */
	void enterOuter_join_sign(ObForOracleParser.Outer_join_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#outer_join_sign}.
	 * @param ctx the parse tree
	 */
	void exitOuter_join_sign(ObForOracleParser.Outer_join_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void enterRegular_id(ObForOracleParser.Regular_idContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void exitRegular_id(ObForOracleParser.Regular_idContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_in_18c(ObForOracleParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_18c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_in_18c(ObForOracleParser.Non_reserved_keywords_in_18cContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_in_12c(ObForOracleParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_in_12c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_in_12c(ObForOracleParser.Non_reserved_keywords_in_12cContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 */
	void enterNon_reserved_keywords_pre12c(ObForOracleParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#non_reserved_keywords_pre12c}.
	 * @param ctx the parse tree
	 */
	void exitNon_reserved_keywords_pre12c(ObForOracleParser.Non_reserved_keywords_pre12cContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#string_function_name}.
	 * @param ctx the parse tree
	 */
	void enterString_function_name(ObForOracleParser.String_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#string_function_name}.
	 * @param ctx the parse tree
	 */
	void exitString_function_name(ObForOracleParser.String_function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObForOracleParser#numeric_function_name}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_function_name(ObForOracleParser.Numeric_function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObForOracleParser#numeric_function_name}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_function_name(ObForOracleParser.Numeric_function_nameContext ctx);
}