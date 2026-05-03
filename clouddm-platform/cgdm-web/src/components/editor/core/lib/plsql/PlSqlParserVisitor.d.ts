import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
import { Table_ref_aux_internal_oneContext } from "./PlSqlParser";
import { Table_ref_aux_internal_twoContext } from "./PlSqlParser";
import { Table_ref_aux_internal_threeContext } from "./PlSqlParser";
import { ProgramContext } from "./PlSqlParser";
import { Sql_scriptContext } from "./PlSqlParser";
import { Unit_statementContext } from "./PlSqlParser";
import { Alter_diskgroupContext } from "./PlSqlParser";
import { Add_disk_clauseContext } from "./PlSqlParser";
import { Drop_disk_clauseContext } from "./PlSqlParser";
import { Resize_disk_clauseContext } from "./PlSqlParser";
import { Replace_disk_clauseContext } from "./PlSqlParser";
import { Wait_nowaitContext } from "./PlSqlParser";
import { Rename_disk_clauseContext } from "./PlSqlParser";
import { Disk_online_clauseContext } from "./PlSqlParser";
import { Disk_offline_clauseContext } from "./PlSqlParser";
import { Timeout_clauseContext } from "./PlSqlParser";
import { Rebalance_diskgroup_clauseContext } from "./PlSqlParser";
import { PhaseContext } from "./PlSqlParser";
import { Check_diskgroup_clauseContext } from "./PlSqlParser";
import { Diskgroup_template_clausesContext } from "./PlSqlParser";
import { Qualified_template_clauseContext } from "./PlSqlParser";
import { Redundancy_clauseContext } from "./PlSqlParser";
import { Striping_clauseContext } from "./PlSqlParser";
import { Force_noforceContext } from "./PlSqlParser";
import { Diskgroup_directory_clausesContext } from "./PlSqlParser";
import { Dir_nameContext } from "./PlSqlParser";
import { Diskgroup_alias_clausesContext } from "./PlSqlParser";
import { Diskgroup_volume_clausesContext } from "./PlSqlParser";
import { Add_volume_clauseContext } from "./PlSqlParser";
import { Modify_volume_clauseContext } from "./PlSqlParser";
import { Diskgroup_attributesContext } from "./PlSqlParser";
import { Modify_diskgroup_fileContext } from "./PlSqlParser";
import { Disk_region_clauseContext } from "./PlSqlParser";
import { Drop_diskgroup_file_clauseContext } from "./PlSqlParser";
import { Convert_redundancy_clauseContext } from "./PlSqlParser";
import { Usergroup_clausesContext } from "./PlSqlParser";
import { User_clausesContext } from "./PlSqlParser";
import { File_permissions_clauseContext } from "./PlSqlParser";
import { File_owner_clauseContext } from "./PlSqlParser";
import { Scrub_clauseContext } from "./PlSqlParser";
import { Quotagroup_clausesContext } from "./PlSqlParser";
import { Property_nameContext } from "./PlSqlParser";
import { Property_valueContext } from "./PlSqlParser";
import { Filegroup_clausesContext } from "./PlSqlParser";
import { Add_filegroup_clauseContext } from "./PlSqlParser";
import { Modify_filegroup_clauseContext } from "./PlSqlParser";
import { Move_to_filegroup_clauseContext } from "./PlSqlParser";
import { Drop_filegroup_clauseContext } from "./PlSqlParser";
import { Quorum_regularContext } from "./PlSqlParser";
import { Undrop_disk_clauseContext } from "./PlSqlParser";
import { Diskgroup_availabilityContext } from "./PlSqlParser";
import { Enable_disable_volumeContext } from "./PlSqlParser";
import { Drop_functionContext } from "./PlSqlParser";
import { Alter_flashback_archiveContext } from "./PlSqlParser";
import { Alter_hierarchyContext } from "./PlSqlParser";
import { Alter_functionContext } from "./PlSqlParser";
import { Alter_javaContext } from "./PlSqlParser";
import { Match_stringContext } from "./PlSqlParser";
import { Create_function_bodyContext } from "./PlSqlParser";
import { Sql_macro_bodyContext } from "./PlSqlParser";
import { Parallel_enable_clauseContext } from "./PlSqlParser";
import { Partition_by_clauseContext } from "./PlSqlParser";
import { Result_cache_clauseContext } from "./PlSqlParser";
import { Relies_on_partContext } from "./PlSqlParser";
import { Streaming_clauseContext } from "./PlSqlParser";
import { Alter_outlineContext } from "./PlSqlParser";
import { Outline_optionsContext } from "./PlSqlParser";
import { Alter_lockdown_profileContext } from "./PlSqlParser";
import { Lockdown_featureContext } from "./PlSqlParser";
import { Lockdown_optionsContext } from "./PlSqlParser";
import { Lockdown_statementsContext } from "./PlSqlParser";
import { Statement_clausesContext } from "./PlSqlParser";
import { Clause_optionsContext } from "./PlSqlParser";
import { Option_valuesContext } from "./PlSqlParser";
import { String_listContext } from "./PlSqlParser";
import { Disable_enableContext } from "./PlSqlParser";
import { Drop_lockdown_profileContext } from "./PlSqlParser";
import { Drop_packageContext } from "./PlSqlParser";
import { Alter_packageContext } from "./PlSqlParser";
import { Create_packageContext } from "./PlSqlParser";
import { Create_package_bodyContext } from "./PlSqlParser";
import { Package_obj_specContext } from "./PlSqlParser";
import { Procedure_specContext } from "./PlSqlParser";
import { Function_specContext } from "./PlSqlParser";
import { Package_obj_bodyContext } from "./PlSqlParser";
import { Alter_pmem_filestoreContext } from "./PlSqlParser";
import { Drop_pmem_filestoreContext } from "./PlSqlParser";
import { Drop_procedureContext } from "./PlSqlParser";
import { Alter_procedureContext } from "./PlSqlParser";
import { Function_bodyContext } from "./PlSqlParser";
import { Procedure_bodyContext } from "./PlSqlParser";
import { Create_procedure_bodyContext } from "./PlSqlParser";
import { Alter_resource_costContext } from "./PlSqlParser";
import { Drop_outlineContext } from "./PlSqlParser";
import { Alter_rollback_segmentContext } from "./PlSqlParser";
import { Drop_restore_pointContext } from "./PlSqlParser";
import { Drop_rollback_segmentContext } from "./PlSqlParser";
import { Drop_roleContext } from "./PlSqlParser";
import { Create_pmem_filestoreContext } from "./PlSqlParser";
import { Pmem_filestore_optionsContext } from "./PlSqlParser";
import { File_pathContext } from "./PlSqlParser";
import { Create_rollback_segmentContext } from "./PlSqlParser";
import { Drop_triggerContext } from "./PlSqlParser";
import { Alter_triggerContext } from "./PlSqlParser";
import { Create_triggerContext } from "./PlSqlParser";
import { Trigger_follows_clauseContext } from "./PlSqlParser";
import { Trigger_when_clauseContext } from "./PlSqlParser";
import { Simple_dml_triggerContext } from "./PlSqlParser";
import { For_each_rowContext } from "./PlSqlParser";
import { Compound_dml_triggerContext } from "./PlSqlParser";
import { Non_dml_triggerContext } from "./PlSqlParser";
import { Trigger_bodyContext } from "./PlSqlParser";
import { Routine_clauseContext } from "./PlSqlParser";
import { Compound_trigger_blockContext } from "./PlSqlParser";
import { Timing_point_sectionContext } from "./PlSqlParser";
import { Non_dml_eventContext } from "./PlSqlParser";
import { Dml_event_clauseContext } from "./PlSqlParser";
import { Dml_event_elementContext } from "./PlSqlParser";
import { Dml_event_nested_clauseContext } from "./PlSqlParser";
import { Referencing_clauseContext } from "./PlSqlParser";
import { Referencing_elementContext } from "./PlSqlParser";
import { Drop_typeContext } from "./PlSqlParser";
import { Alter_typeContext } from "./PlSqlParser";
import { Compile_type_clauseContext } from "./PlSqlParser";
import { Replace_type_clauseContext } from "./PlSqlParser";
import { Alter_method_specContext } from "./PlSqlParser";
import { Alter_method_elementContext } from "./PlSqlParser";
import { Alter_attribute_definitionContext } from "./PlSqlParser";
import { Attribute_definitionContext } from "./PlSqlParser";
import { Alter_collection_clausesContext } from "./PlSqlParser";
import { Dependent_handling_clauseContext } from "./PlSqlParser";
import { Dependent_exceptions_partContext } from "./PlSqlParser";
import { Create_typeContext } from "./PlSqlParser";
import { Type_definitionContext } from "./PlSqlParser";
import { Object_type_defContext } from "./PlSqlParser";
import { Object_as_partContext } from "./PlSqlParser";
import { Object_under_partContext } from "./PlSqlParser";
import { Nested_table_type_defContext } from "./PlSqlParser";
import { Sqlj_object_typeContext } from "./PlSqlParser";
import { Type_bodyContext } from "./PlSqlParser";
import { Type_body_elementsContext } from "./PlSqlParser";
import { Map_order_func_declarationContext } from "./PlSqlParser";
import { Subprog_decl_in_typeContext } from "./PlSqlParser";
import { Proc_decl_in_typeContext } from "./PlSqlParser";
import { Func_decl_in_typeContext } from "./PlSqlParser";
import { Constructor_declarationContext } from "./PlSqlParser";
import { Modifier_clauseContext } from "./PlSqlParser";
import { Object_member_specContext } from "./PlSqlParser";
import { Sqlj_object_type_attrContext } from "./PlSqlParser";
import { Element_specContext } from "./PlSqlParser";
import { Element_spec_optionsContext } from "./PlSqlParser";
import { Subprogram_specContext } from "./PlSqlParser";
import { Overriding_subprogram_specContext } from "./PlSqlParser";
import { Overriding_function_specContext } from "./PlSqlParser";
import { Type_procedure_specContext } from "./PlSqlParser";
import { Type_function_specContext } from "./PlSqlParser";
import { Constructor_specContext } from "./PlSqlParser";
import { Map_order_function_specContext } from "./PlSqlParser";
import { Pragma_clauseContext } from "./PlSqlParser";
import { Pragma_elementsContext } from "./PlSqlParser";
import { Type_elements_parameterContext } from "./PlSqlParser";
import { Drop_sequenceContext } from "./PlSqlParser";
import { Alter_sequenceContext } from "./PlSqlParser";
import { Alter_sessionContext } from "./PlSqlParser";
import { Alter_session_set_clauseContext } from "./PlSqlParser";
import { Create_sequenceContext } from "./PlSqlParser";
import { Sequence_specContext } from "./PlSqlParser";
import { Sequence_start_clauseContext } from "./PlSqlParser";
import { Create_analytic_viewContext } from "./PlSqlParser";
import { Classification_clauseContext } from "./PlSqlParser";
import { Caption_clauseContext } from "./PlSqlParser";
import { Description_clauseContext } from "./PlSqlParser";
import { Classification_itemContext } from "./PlSqlParser";
import { LanguageContext } from "./PlSqlParser";
import { Cav_using_clauseContext } from "./PlSqlParser";
import { Dim_by_clauseContext } from "./PlSqlParser";
import { Dim_keyContext } from "./PlSqlParser";
import { Dim_refContext } from "./PlSqlParser";
import { Hier_refContext } from "./PlSqlParser";
import { Measures_clauseContext } from "./PlSqlParser";
import { Av_measureContext } from "./PlSqlParser";
import { Base_meas_clauseContext } from "./PlSqlParser";
import { Meas_aggregate_clauseContext } from "./PlSqlParser";
import { Calc_meas_clauseContext } from "./PlSqlParser";
import { Default_measure_clauseContext } from "./PlSqlParser";
import { Default_aggregate_clauseContext } from "./PlSqlParser";
import { Cache_clauseContext } from "./PlSqlParser";
import { Cache_specificationContext } from "./PlSqlParser";
import { Levels_clauseContext } from "./PlSqlParser";
import { Level_specificationContext } from "./PlSqlParser";
import { Level_group_typeContext } from "./PlSqlParser";
import { Fact_columns_clauseContext } from "./PlSqlParser";
import { Qry_transform_clauseContext } from "./PlSqlParser";
import { Create_attribute_dimensionContext } from "./PlSqlParser";
import { Ad_using_clauseContext } from "./PlSqlParser";
import { Source_clauseContext } from "./PlSqlParser";
import { Join_path_clauseContext } from "./PlSqlParser";
import { Join_conditionContext } from "./PlSqlParser";
import { Join_condition_itemContext } from "./PlSqlParser";
import { Attributes_clauseContext } from "./PlSqlParser";
import { Ad_attributes_clauseContext } from "./PlSqlParser";
import { Ad_level_clauseContext } from "./PlSqlParser";
import { Key_clauseContext } from "./PlSqlParser";
import { Alternate_key_clauseContext } from "./PlSqlParser";
import { Dim_order_clauseContext } from "./PlSqlParser";
import { All_clauseContext } from "./PlSqlParser";
import { Create_audit_policyContext } from "./PlSqlParser";
import { Privilege_audit_clauseContext } from "./PlSqlParser";
import { Action_audit_clauseContext } from "./PlSqlParser";
import { System_actionsContext } from "./PlSqlParser";
import { Standard_actionsContext } from "./PlSqlParser";
import { Actions_clauseContext } from "./PlSqlParser";
import { Object_actionContext } from "./PlSqlParser";
import { System_actionContext } from "./PlSqlParser";
import { Component_actionsContext } from "./PlSqlParser";
import { Component_actionContext } from "./PlSqlParser";
import { Role_audit_clauseContext } from "./PlSqlParser";
import { Create_controlfileContext } from "./PlSqlParser";
import { Controlfile_optionsContext } from "./PlSqlParser";
import { Logfile_clauseContext } from "./PlSqlParser";
import { Character_set_clauseContext } from "./PlSqlParser";
import { File_specificationContext } from "./PlSqlParser";
import { Create_diskgroupContext } from "./PlSqlParser";
import { Qualified_disk_clauseContext } from "./PlSqlParser";
import { Create_editionContext } from "./PlSqlParser";
import { Create_flashback_archiveContext } from "./PlSqlParser";
import { Flashback_archive_quotaContext } from "./PlSqlParser";
import { Flashback_archive_retentionContext } from "./PlSqlParser";
import { Create_hierarchyContext } from "./PlSqlParser";
import { Hier_using_clauseContext } from "./PlSqlParser";
import { Level_hier_clauseContext } from "./PlSqlParser";
import { Hier_attrs_clauseContext } from "./PlSqlParser";
import { Hier_attr_clauseContext } from "./PlSqlParser";
import { Hier_attr_nameContext } from "./PlSqlParser";
import { Create_indexContext } from "./PlSqlParser";
import { Cluster_index_clauseContext } from "./PlSqlParser";
import { Cluster_nameContext } from "./PlSqlParser";
import { Table_index_clauseContext } from "./PlSqlParser";
import { Bitmap_join_index_clauseContext } from "./PlSqlParser";
import { Index_exprContext } from "./PlSqlParser";
import { Index_propertiesContext } from "./PlSqlParser";
import { Domain_index_clauseContext } from "./PlSqlParser";
import { Local_domain_index_clauseContext } from "./PlSqlParser";
import { Xmlindex_clauseContext } from "./PlSqlParser";
import { Local_xmlindex_clauseContext } from "./PlSqlParser";
import { Global_partitioned_indexContext } from "./PlSqlParser";
import { Index_partitioning_clauseContext } from "./PlSqlParser";
import { Local_partitioned_indexContext } from "./PlSqlParser";
import { On_range_partitioned_tableContext } from "./PlSqlParser";
import { On_list_partitioned_tableContext } from "./PlSqlParser";
import { Partitioned_tableContext } from "./PlSqlParser";
import { On_hash_partitioned_tableContext } from "./PlSqlParser";
import { On_hash_partitioned_clauseContext } from "./PlSqlParser";
import { On_comp_partitioned_tableContext } from "./PlSqlParser";
import { On_comp_partitioned_clauseContext } from "./PlSqlParser";
import { Index_subpartition_clauseContext } from "./PlSqlParser";
import { Index_subpartition_subclauseContext } from "./PlSqlParser";
import { Odci_parametersContext } from "./PlSqlParser";
import { IndextypeContext } from "./PlSqlParser";
import { Alter_indexContext } from "./PlSqlParser";
import { Alter_index_ops_set1Context } from "./PlSqlParser";
import { Alter_index_ops_set2Context } from "./PlSqlParser";
import { Visible_or_invisibleContext } from "./PlSqlParser";
import { Monitoring_nomonitoringContext } from "./PlSqlParser";
import { Rebuild_clauseContext } from "./PlSqlParser";
import { Alter_index_partitioningContext } from "./PlSqlParser";
import { Modify_index_default_attrsContext } from "./PlSqlParser";
import { Add_hash_index_partitionContext } from "./PlSqlParser";
import { Coalesce_index_partitionContext } from "./PlSqlParser";
import { Modify_index_partitionContext } from "./PlSqlParser";
import { Modify_index_partitions_opsContext } from "./PlSqlParser";
import { Rename_index_partitionContext } from "./PlSqlParser";
import { Drop_index_partitionContext } from "./PlSqlParser";
import { Split_index_partitionContext } from "./PlSqlParser";
import { Index_partition_descriptionContext } from "./PlSqlParser";
import { Modify_index_subpartitionContext } from "./PlSqlParser";
import { Partition_name_oldContext } from "./PlSqlParser";
import { New_partition_nameContext } from "./PlSqlParser";
import { New_index_nameContext } from "./PlSqlParser";
import { Alter_inmemory_join_groupContext } from "./PlSqlParser";
import { Create_userContext } from "./PlSqlParser";
import { Alter_userContext } from "./PlSqlParser";
import { Drop_userContext } from "./PlSqlParser";
import { Alter_identified_byContext } from "./PlSqlParser";
import { Identified_byContext } from "./PlSqlParser";
import { Identified_other_clauseContext } from "./PlSqlParser";
import { User_tablespace_clauseContext } from "./PlSqlParser";
import { Quota_clauseContext } from "./PlSqlParser";
import { Profile_clauseContext } from "./PlSqlParser";
import { Role_clauseContext } from "./PlSqlParser";
import { User_default_role_clauseContext } from "./PlSqlParser";
import { Password_expire_clauseContext } from "./PlSqlParser";
import { User_lock_clauseContext } from "./PlSqlParser";
import { User_editions_clauseContext } from "./PlSqlParser";
import { Alter_user_editions_clauseContext } from "./PlSqlParser";
import { Proxy_clauseContext } from "./PlSqlParser";
import { Container_namesContext } from "./PlSqlParser";
import { Set_container_dataContext } from "./PlSqlParser";
import { Add_rem_container_dataContext } from "./PlSqlParser";
import { Container_data_clauseContext } from "./PlSqlParser";
import { Administer_key_managementContext } from "./PlSqlParser";
import { Keystore_management_clausesContext } from "./PlSqlParser";
import { Create_keystoreContext } from "./PlSqlParser";
import { Open_keystoreContext } from "./PlSqlParser";
import { Force_keystoreContext } from "./PlSqlParser";
import { Close_keystoreContext } from "./PlSqlParser";
import { Backup_keystoreContext } from "./PlSqlParser";
import { Alter_keystore_passwordContext } from "./PlSqlParser";
import { Merge_into_new_keystoreContext } from "./PlSqlParser";
import { Merge_into_existing_keystoreContext } from "./PlSqlParser";
import { Isolate_keystoreContext } from "./PlSqlParser";
import { Unite_keystoreContext } from "./PlSqlParser";
import { Key_management_clausesContext } from "./PlSqlParser";
import { Set_keyContext } from "./PlSqlParser";
import { Create_keyContext } from "./PlSqlParser";
import { MkidContext } from "./PlSqlParser";
import { MkContext } from "./PlSqlParser";
import { Use_keyContext } from "./PlSqlParser";
import { Set_key_tagContext } from "./PlSqlParser";
import { Export_keysContext } from "./PlSqlParser";
import { Import_keysContext } from "./PlSqlParser";
import { Migrate_keysContext } from "./PlSqlParser";
import { Reverse_migrate_keysContext } from "./PlSqlParser";
import { Move_keysContext } from "./PlSqlParser";
import { Identified_by_storeContext } from "./PlSqlParser";
import { Using_algorithm_clauseContext } from "./PlSqlParser";
import { Using_tag_clauseContext } from "./PlSqlParser";
import { Secret_management_clausesContext } from "./PlSqlParser";
import { Add_update_secretContext } from "./PlSqlParser";
import { Delete_secretContext } from "./PlSqlParser";
import { Add_update_secret_sepsContext } from "./PlSqlParser";
import { Delete_secret_sepsContext } from "./PlSqlParser";
import { Zero_downtime_software_patching_clausesContext } from "./PlSqlParser";
import { With_backup_clauseContext } from "./PlSqlParser";
import { Identified_by_password_clauseContext } from "./PlSqlParser";
import { Keystore_passwordContext } from "./PlSqlParser";
import { PathContext } from "./PlSqlParser";
import { SecretContext } from "./PlSqlParser";
import { AnalyzeContext } from "./PlSqlParser";
import { Partition_extention_clauseContext } from "./PlSqlParser";
import { Validation_clausesContext } from "./PlSqlParser";
import { Compute_clausesContext } from "./PlSqlParser";
import { For_clauseContext } from "./PlSqlParser";
import { Online_or_offlineContext } from "./PlSqlParser";
import { Into_clause1Context } from "./PlSqlParser";
import { Partition_key_valueContext } from "./PlSqlParser";
import { Subpartition_key_valueContext } from "./PlSqlParser";
import { Associate_statisticsContext } from "./PlSqlParser";
import { Column_associationContext } from "./PlSqlParser";
import { Function_associationContext } from "./PlSqlParser";
import { Indextype_nameContext } from "./PlSqlParser";
import { Using_statistics_typeContext } from "./PlSqlParser";
import { Statistics_type_nameContext } from "./PlSqlParser";
import { Default_cost_clauseContext } from "./PlSqlParser";
import { Cpu_costContext } from "./PlSqlParser";
import { Io_costContext } from "./PlSqlParser";
import { Network_costContext } from "./PlSqlParser";
import { Default_selectivity_clauseContext } from "./PlSqlParser";
import { Default_selectivityContext } from "./PlSqlParser";
import { Storage_table_clauseContext } from "./PlSqlParser";
import { Unified_auditingContext } from "./PlSqlParser";
import { Policy_nameContext } from "./PlSqlParser";
import { Audit_traditionalContext } from "./PlSqlParser";
import { Audit_direct_pathContext } from "./PlSqlParser";
import { Audit_container_clauseContext } from "./PlSqlParser";
import { Audit_operation_clauseContext } from "./PlSqlParser";
import { Auditing_by_clauseContext } from "./PlSqlParser";
import { Audit_userContext } from "./PlSqlParser";
import { Audit_schema_object_clauseContext } from "./PlSqlParser";
import { Sql_operationContext } from "./PlSqlParser";
import { Auditing_on_clauseContext } from "./PlSqlParser";
import { Model_nameContext } from "./PlSqlParser";
import { Object_nameContext } from "./PlSqlParser";
import { Profile_nameContext } from "./PlSqlParser";
import { Sql_statement_shortcutContext } from "./PlSqlParser";
import { Drop_indexContext } from "./PlSqlParser";
import { Disassociate_statisticsContext } from "./PlSqlParser";
import { Drop_indextypeContext } from "./PlSqlParser";
import { Drop_inmemory_join_groupContext } from "./PlSqlParser";
import { Flashback_tableContext } from "./PlSqlParser";
import { Restore_pointContext } from "./PlSqlParser";
import { Purge_statementContext } from "./PlSqlParser";
import { Noaudit_statementContext } from "./PlSqlParser";
import { Rename_objectContext } from "./PlSqlParser";
import { Grant_statementContext } from "./PlSqlParser";
import { Container_clauseContext } from "./PlSqlParser";
import { Revoke_statementContext } from "./PlSqlParser";
import { Revoke_system_privilegeContext } from "./PlSqlParser";
import { Revokee_clauseContext } from "./PlSqlParser";
import { Revoke_object_privilegesContext } from "./PlSqlParser";
import { On_object_clauseContext } from "./PlSqlParser";
import { Revoke_roles_from_programsContext } from "./PlSqlParser";
import { Program_unitContext } from "./PlSqlParser";
import { Create_dimensionContext } from "./PlSqlParser";
import { Create_directoryContext } from "./PlSqlParser";
import { Directory_nameContext } from "./PlSqlParser";
import { Directory_pathContext } from "./PlSqlParser";
import { Create_inmemory_join_groupContext } from "./PlSqlParser";
import { Drop_hierarchyContext } from "./PlSqlParser";
import { Alter_libraryContext } from "./PlSqlParser";
import { Drop_javaContext } from "./PlSqlParser";
import { Drop_libraryContext } from "./PlSqlParser";
import { Create_javaContext } from "./PlSqlParser";
import { Create_libraryContext } from "./PlSqlParser";
import { Plsql_library_sourceContext } from "./PlSqlParser";
import { Credential_nameContext } from "./PlSqlParser";
import { Library_editionableContext } from "./PlSqlParser";
import { Library_debugContext } from "./PlSqlParser";
import { Compiler_parameters_clauseContext } from "./PlSqlParser";
import { Parameter_valueContext } from "./PlSqlParser";
import { Library_nameContext } from "./PlSqlParser";
import { Alter_dimensionContext } from "./PlSqlParser";
import { Level_clauseContext } from "./PlSqlParser";
import { Hierarchy_clauseContext } from "./PlSqlParser";
import { Dimension_join_clauseContext } from "./PlSqlParser";
import { Attribute_clauseContext } from "./PlSqlParser";
import { Extended_attribute_clauseContext } from "./PlSqlParser";
import { Column_one_or_more_sub_clauseContext } from "./PlSqlParser";
import { Alter_viewContext } from "./PlSqlParser";
import { Alter_view_editionableContext } from "./PlSqlParser";
import { Create_viewContext } from "./PlSqlParser";
import { Editioning_clauseContext } from "./PlSqlParser";
import { View_optionsContext } from "./PlSqlParser";
import { View_alias_constraintContext } from "./PlSqlParser";
import { Object_view_clauseContext } from "./PlSqlParser";
import { Inline_constraintContext } from "./PlSqlParser";
import { Inline_ref_constraintContext } from "./PlSqlParser";
import { Out_of_line_ref_constraintContext } from "./PlSqlParser";
import { Out_of_line_constraintContext } from "./PlSqlParser";
import { Constraint_stateContext } from "./PlSqlParser";
import { Xmltype_view_clauseContext } from "./PlSqlParser";
import { Xml_schema_specContext } from "./PlSqlParser";
import { Xml_schema_urlContext } from "./PlSqlParser";
import { ElementContext } from "./PlSqlParser";
import { Alter_tablespaceContext } from "./PlSqlParser";
import { Datafile_tempfile_clausesContext } from "./PlSqlParser";
import { Tablespace_logging_clausesContext } from "./PlSqlParser";
import { Tablespace_group_clauseContext } from "./PlSqlParser";
import { Tablespace_group_nameContext } from "./PlSqlParser";
import { Tablespace_state_clausesContext } from "./PlSqlParser";
import { Flashback_mode_clauseContext } from "./PlSqlParser";
import { New_tablespace_nameContext } from "./PlSqlParser";
import { Create_tablespaceContext } from "./PlSqlParser";
import { Permanent_tablespace_clauseContext } from "./PlSqlParser";
import { Tablespace_encryption_specContext } from "./PlSqlParser";
import { Logging_clauseContext } from "./PlSqlParser";
import { Extent_management_clauseContext } from "./PlSqlParser";
import { Segment_management_clauseContext } from "./PlSqlParser";
import { Temporary_tablespace_clauseContext } from "./PlSqlParser";
import { Undo_tablespace_clauseContext } from "./PlSqlParser";
import { Tablespace_retention_clauseContext } from "./PlSqlParser";
import { Create_tablespace_setContext } from "./PlSqlParser";
import { Permanent_tablespace_attrsContext } from "./PlSqlParser";
import { Tablespace_encryption_clauseContext } from "./PlSqlParser";
import { Default_tablespace_paramsContext } from "./PlSqlParser";
import { Default_table_compressionContext } from "./PlSqlParser";
import { Low_highContext } from "./PlSqlParser";
import { Default_index_compressionContext } from "./PlSqlParser";
import { Inmmemory_clauseContext } from "./PlSqlParser";
import { Datafile_specificationContext } from "./PlSqlParser";
import { Tempfile_specificationContext } from "./PlSqlParser";
import { Datafile_tempfile_specContext } from "./PlSqlParser";
import { Redo_log_file_specContext } from "./PlSqlParser";
import { Autoextend_clauseContext } from "./PlSqlParser";
import { Maxsize_clauseContext } from "./PlSqlParser";
import { Build_clauseContext } from "./PlSqlParser";
import { Parallel_clauseContext } from "./PlSqlParser";
import { Alter_materialized_viewContext } from "./PlSqlParser";
import { Alter_mv_option1Context } from "./PlSqlParser";
import { Alter_mv_refreshContext } from "./PlSqlParser";
import { Rollback_segmentContext } from "./PlSqlParser";
import { Modify_mv_column_clauseContext } from "./PlSqlParser";
import { Alter_materialized_view_logContext } from "./PlSqlParser";
import { Add_mv_log_column_clauseContext } from "./PlSqlParser";
import { Move_mv_log_clauseContext } from "./PlSqlParser";
import { Mv_log_augmentationContext } from "./PlSqlParser";
import { Datetime_exprContext } from "./PlSqlParser";
import { Interval_exprContext } from "./PlSqlParser";
import { Synchronous_or_asynchronousContext } from "./PlSqlParser";
import { Including_or_excludingContext } from "./PlSqlParser";
import { Create_materialized_view_logContext } from "./PlSqlParser";
import { New_values_clauseContext } from "./PlSqlParser";
import { Mv_log_purge_clauseContext } from "./PlSqlParser";
import { Create_materialized_zonemapContext } from "./PlSqlParser";
import { Alter_materialized_zonemapContext } from "./PlSqlParser";
import { Drop_materialized_zonemapContext } from "./PlSqlParser";
import { Zonemap_refresh_clauseContext } from "./PlSqlParser";
import { Zonemap_attributesContext } from "./PlSqlParser";
import { Zonemap_nameContext } from "./PlSqlParser";
import { Operator_nameContext } from "./PlSqlParser";
import { Operator_function_nameContext } from "./PlSqlParser";
import { Create_zonemap_on_tableContext } from "./PlSqlParser";
import { Create_zonemap_as_subqueryContext } from "./PlSqlParser";
import { Alter_operatorContext } from "./PlSqlParser";
import { Drop_operatorContext } from "./PlSqlParser";
import { Create_operatorContext } from "./PlSqlParser";
import { Binding_clauseContext } from "./PlSqlParser";
import { Add_binding_clauseContext } from "./PlSqlParser";
import { Implementation_clauseContext } from "./PlSqlParser";
import { Primary_operator_listContext } from "./PlSqlParser";
import { Primary_operator_itemContext } from "./PlSqlParser";
import { Operator_context_clauseContext } from "./PlSqlParser";
import { Using_function_clauseContext } from "./PlSqlParser";
import { Drop_binding_clauseContext } from "./PlSqlParser";
import { Create_materialized_viewContext } from "./PlSqlParser";
import { Scoped_table_ref_constraintContext } from "./PlSqlParser";
import { Mv_column_aliasContext } from "./PlSqlParser";
import { Create_mv_refreshContext } from "./PlSqlParser";
import { Drop_materialized_viewContext } from "./PlSqlParser";
import { Create_contextContext } from "./PlSqlParser";
import { Oracle_namespaceContext } from "./PlSqlParser";
import { Create_clusterContext } from "./PlSqlParser";
import { Create_profileContext } from "./PlSqlParser";
import { Resource_parametersContext } from "./PlSqlParser";
import { Password_parametersContext } from "./PlSqlParser";
import { Create_lockdown_profileContext } from "./PlSqlParser";
import { Static_base_profileContext } from "./PlSqlParser";
import { Dynamic_base_profileContext } from "./PlSqlParser";
import { Create_outlineContext } from "./PlSqlParser";
import { Create_restore_pointContext } from "./PlSqlParser";
import { Create_roleContext } from "./PlSqlParser";
import { Create_tableContext } from "./PlSqlParser";
import { Xmltype_tableContext } from "./PlSqlParser";
import { Xmltype_virtual_columnsContext } from "./PlSqlParser";
import { Xmltype_column_propertiesContext } from "./PlSqlParser";
import { Xmltype_storageContext } from "./PlSqlParser";
import { Xmlschema_specContext } from "./PlSqlParser";
import { Object_tableContext } from "./PlSqlParser";
import { Object_typeContext } from "./PlSqlParser";
import { Oid_index_clauseContext } from "./PlSqlParser";
import { Oid_clauseContext } from "./PlSqlParser";
import { Object_propertiesContext } from "./PlSqlParser";
import { Object_table_substitutionContext } from "./PlSqlParser";
import { Relational_tableContext } from "./PlSqlParser";
import { Immutable_table_clausesContext } from "./PlSqlParser";
import { Immutable_table_no_drop_clauseContext } from "./PlSqlParser";
import { Immutable_table_no_delete_clauseContext } from "./PlSqlParser";
import { Blockchain_table_clausesContext } from "./PlSqlParser";
import { Blockchain_drop_table_clauseContext } from "./PlSqlParser";
import { Blockchain_row_retention_clauseContext } from "./PlSqlParser";
import { Blockchain_hash_and_data_format_clauseContext } from "./PlSqlParser";
import { Collation_nameContext } from "./PlSqlParser";
import { Table_propertiesContext } from "./PlSqlParser";
import { Read_only_clauseContext } from "./PlSqlParser";
import { Indexing_clauseContext } from "./PlSqlParser";
import { Attribute_clustering_clauseContext } from "./PlSqlParser";
import { Clustering_joinContext } from "./PlSqlParser";
import { Clustering_join_itemContext } from "./PlSqlParser";
import { Equijoin_conditionContext } from "./PlSqlParser";
import { Cluster_clauseContext } from "./PlSqlParser";
import { Clustering_columnsContext } from "./PlSqlParser";
import { Clustering_column_groupContext } from "./PlSqlParser";
import { Yes_noContext } from "./PlSqlParser";
import { Zonemap_clauseContext } from "./PlSqlParser";
import { Logical_replication_clauseContext } from "./PlSqlParser";
import { Table_nameContext } from "./PlSqlParser";
import { Relational_propertyContext } from "./PlSqlParser";
import { Table_partitioning_clausesContext } from "./PlSqlParser";
import { Range_partitionsContext } from "./PlSqlParser";
import { List_partitionsContext } from "./PlSqlParser";
import { Hash_partitionsContext } from "./PlSqlParser";
import { Individual_hash_partitionsContext } from "./PlSqlParser";
import { Hash_partitions_by_quantityContext } from "./PlSqlParser";
import { Hash_partition_quantityContext } from "./PlSqlParser";
import { Composite_range_partitionsContext } from "./PlSqlParser";
import { Composite_list_partitionsContext } from "./PlSqlParser";
import { Composite_hash_partitionsContext } from "./PlSqlParser";
import { Reference_partitioningContext } from "./PlSqlParser";
import { Reference_partition_descContext } from "./PlSqlParser";
import { System_partitioningContext } from "./PlSqlParser";
import { Range_partition_descContext } from "./PlSqlParser";
import { List_partition_descContext } from "./PlSqlParser";
import { Subpartition_templateContext } from "./PlSqlParser";
import { Hash_subpartition_quantityContext } from "./PlSqlParser";
import { Subpartition_by_rangeContext } from "./PlSqlParser";
import { Subpartition_by_listContext } from "./PlSqlParser";
import { Subpartition_by_hashContext } from "./PlSqlParser";
import { Subpartition_nameContext } from "./PlSqlParser";
import { Range_subpartition_descContext } from "./PlSqlParser";
import { List_subpartition_descContext } from "./PlSqlParser";
import { Individual_hash_subpartsContext } from "./PlSqlParser";
import { Hash_subparts_by_quantityContext } from "./PlSqlParser";
import { Range_values_clauseContext } from "./PlSqlParser";
import { List_values_clauseContext } from "./PlSqlParser";
import { Table_partition_descriptionContext } from "./PlSqlParser";
import { Partitioning_storage_clauseContext } from "./PlSqlParser";
import { Lob_partitioning_storageContext } from "./PlSqlParser";
import { Datatype_null_enableContext } from "./PlSqlParser";
import { Size_clauseContext } from "./PlSqlParser";
import { Table_compressionContext } from "./PlSqlParser";
import { Inmemory_table_clauseContext } from "./PlSqlParser";
import { Inmemory_attributesContext } from "./PlSqlParser";
import { Inmemory_memcompressContext } from "./PlSqlParser";
import { Inmemory_priorityContext } from "./PlSqlParser";
import { Inmemory_distributeContext } from "./PlSqlParser";
import { Inmemory_duplicateContext } from "./PlSqlParser";
import { Inmemory_column_clauseContext } from "./PlSqlParser";
import { Physical_attributes_clauseContext } from "./PlSqlParser";
import { Storage_clauseContext } from "./PlSqlParser";
import { Deferred_segment_creationContext } from "./PlSqlParser";
import { Segment_attributes_clauseContext } from "./PlSqlParser";
import { Physical_propertiesContext } from "./PlSqlParser";
import { Ilm_clauseContext } from "./PlSqlParser";
import { Ilm_policy_clauseContext } from "./PlSqlParser";
import { Ilm_compression_policyContext } from "./PlSqlParser";
import { Ilm_tiering_policyContext } from "./PlSqlParser";
import { Ilm_after_onContext } from "./PlSqlParser";
import { Segment_groupContext } from "./PlSqlParser";
import { Ilm_inmemory_policyContext } from "./PlSqlParser";
import { Ilm_time_periodContext } from "./PlSqlParser";
import { Heap_org_table_clauseContext } from "./PlSqlParser";
import { External_table_clauseContext } from "./PlSqlParser";
import { Access_driver_typeContext } from "./PlSqlParser";
import { External_table_data_propsContext } from "./PlSqlParser";
import { Opaque_format_specContext } from "./PlSqlParser";
import { Row_movement_clauseContext } from "./PlSqlParser";
import { Flashback_archive_clauseContext } from "./PlSqlParser";
import { Log_grpContext } from "./PlSqlParser";
import { Supplemental_table_loggingContext } from "./PlSqlParser";
import { Supplemental_log_grp_clauseContext } from "./PlSqlParser";
import { Supplemental_id_key_clauseContext } from "./PlSqlParser";
import { Allocate_extent_clauseContext } from "./PlSqlParser";
import { Deallocate_unused_clauseContext } from "./PlSqlParser";
import { Shrink_clauseContext } from "./PlSqlParser";
import { Records_per_block_clauseContext } from "./PlSqlParser";
import { Upgrade_table_clauseContext } from "./PlSqlParser";
import { Truncate_tableContext } from "./PlSqlParser";
import { Drop_tableContext } from "./PlSqlParser";
import { Drop_tablespaceContext } from "./PlSqlParser";
import { Drop_tablespace_setContext } from "./PlSqlParser";
import { Including_contents_clauseContext } from "./PlSqlParser";
import { Drop_viewContext } from "./PlSqlParser";
import { Comment_on_columnContext } from "./PlSqlParser";
import { Enable_or_disableContext } from "./PlSqlParser";
import { Allow_or_disallowContext } from "./PlSqlParser";
import { Alter_synonymContext } from "./PlSqlParser";
import { Create_synonymContext } from "./PlSqlParser";
import { Drop_synonymContext } from "./PlSqlParser";
import { Create_spfileContext } from "./PlSqlParser";
import { Spfile_nameContext } from "./PlSqlParser";
import { Pfile_nameContext } from "./PlSqlParser";
import { Comment_on_tableContext } from "./PlSqlParser";
import { Comment_on_materializedContext } from "./PlSqlParser";
import { Alter_analytic_viewContext } from "./PlSqlParser";
import { Alter_add_cache_clauseContext } from "./PlSqlParser";
import { Levels_itemContext } from "./PlSqlParser";
import { Measure_listContext } from "./PlSqlParser";
import { Alter_drop_cache_clauseContext } from "./PlSqlParser";
import { Alter_attribute_dimensionContext } from "./PlSqlParser";
import { Alter_audit_policyContext } from "./PlSqlParser";
import { Alter_clusterContext } from "./PlSqlParser";
import { Drop_analytic_viewContext } from "./PlSqlParser";
import { Drop_attribute_dimensionContext } from "./PlSqlParser";
import { Drop_audit_policyContext } from "./PlSqlParser";
import { Drop_flashback_archiveContext } from "./PlSqlParser";
import { Drop_clusterContext } from "./PlSqlParser";
import { Drop_contextContext } from "./PlSqlParser";
import { Drop_directoryContext } from "./PlSqlParser";
import { Drop_diskgroupContext } from "./PlSqlParser";
import { Drop_editionContext } from "./PlSqlParser";
import { Truncate_clusterContext } from "./PlSqlParser";
import { Cache_or_nocacheContext } from "./PlSqlParser";
import { Database_nameContext } from "./PlSqlParser";
import { Alter_databaseContext } from "./PlSqlParser";
import { Database_clauseContext } from "./PlSqlParser";
import { Startup_clausesContext } from "./PlSqlParser";
import { Resetlogs_or_noresetlogsContext } from "./PlSqlParser";
import { Upgrade_or_downgradeContext } from "./PlSqlParser";
import { Recovery_clausesContext } from "./PlSqlParser";
import { Begin_or_endContext } from "./PlSqlParser";
import { General_recoveryContext } from "./PlSqlParser";
import { Full_database_recoveryContext } from "./PlSqlParser";
import { Partial_database_recoveryContext } from "./PlSqlParser";
import { Partial_database_recovery_10gContext } from "./PlSqlParser";
import { Managed_standby_recoveryContext } from "./PlSqlParser";
import { Db_nameContext } from "./PlSqlParser";
import { Database_file_clausesContext } from "./PlSqlParser";
import { Create_datafile_clauseContext } from "./PlSqlParser";
import { Alter_datafile_clauseContext } from "./PlSqlParser";
import { Alter_tempfile_clauseContext } from "./PlSqlParser";
import { Move_datafile_clauseContext } from "./PlSqlParser";
import { Logfile_clausesContext } from "./PlSqlParser";
import { Add_logfile_clausesContext } from "./PlSqlParser";
import { Group_redo_logfileContext } from "./PlSqlParser";
import { Drop_logfile_clausesContext } from "./PlSqlParser";
import { Switch_logfile_clauseContext } from "./PlSqlParser";
import { Supplemental_db_loggingContext } from "./PlSqlParser";
import { Add_or_dropContext } from "./PlSqlParser";
import { Supplemental_plsql_clauseContext } from "./PlSqlParser";
import { Logfile_descriptorContext } from "./PlSqlParser";
import { Controlfile_clausesContext } from "./PlSqlParser";
import { Trace_file_clauseContext } from "./PlSqlParser";
import { Standby_database_clausesContext } from "./PlSqlParser";
import { Activate_standby_db_clauseContext } from "./PlSqlParser";
import { Maximize_standby_db_clauseContext } from "./PlSqlParser";
import { Register_logfile_clauseContext } from "./PlSqlParser";
import { Commit_switchover_clauseContext } from "./PlSqlParser";
import { Start_standby_clauseContext } from "./PlSqlParser";
import { Stop_standby_clauseContext } from "./PlSqlParser";
import { Convert_database_clauseContext } from "./PlSqlParser";
import { Default_settings_clauseContext } from "./PlSqlParser";
import { Set_time_zone_clauseContext } from "./PlSqlParser";
import { Instance_clausesContext } from "./PlSqlParser";
import { Security_clauseContext } from "./PlSqlParser";
import { DomainContext } from "./PlSqlParser";
import { DatabaseContext } from "./PlSqlParser";
import { Edition_nameContext } from "./PlSqlParser";
import { FilenumberContext } from "./PlSqlParser";
import { FilenameContext } from "./PlSqlParser";
import { Prepare_clauseContext } from "./PlSqlParser";
import { Drop_mirror_clauseContext } from "./PlSqlParser";
import { Lost_write_protectionContext } from "./PlSqlParser";
import { Cdb_fleet_clausesContext } from "./PlSqlParser";
import { Lead_cdb_clauseContext } from "./PlSqlParser";
import { Lead_cdb_uri_clauseContext } from "./PlSqlParser";
import { Property_clausesContext } from "./PlSqlParser";
import { Replay_upgrade_clausesContext } from "./PlSqlParser";
import { Alter_database_linkContext } from "./PlSqlParser";
import { Password_valueContext } from "./PlSqlParser";
import { Link_authenticationContext } from "./PlSqlParser";
import { Create_databaseContext } from "./PlSqlParser";
import { Database_logging_clausesContext } from "./PlSqlParser";
import { Database_logging_sub_clauseContext } from "./PlSqlParser";
import { Tablespace_clausesContext } from "./PlSqlParser";
import { Enable_pluggable_databaseContext } from "./PlSqlParser";
import { File_name_convertContext } from "./PlSqlParser";
import { Filename_convert_sub_clauseContext } from "./PlSqlParser";
import { Tablespace_datafile_clausesContext } from "./PlSqlParser";
import { Undo_mode_clauseContext } from "./PlSqlParser";
import { Default_tablespaceContext } from "./PlSqlParser";
import { Default_temp_tablespaceContext } from "./PlSqlParser";
import { Undo_tablespaceContext } from "./PlSqlParser";
import { Drop_databaseContext } from "./PlSqlParser";
import { Create_database_linkContext } from "./PlSqlParser";
import { DblinkContext } from "./PlSqlParser";
import { Drop_database_linkContext } from "./PlSqlParser";
import { Alter_tablespace_setContext } from "./PlSqlParser";
import { Alter_tablespace_attrsContext } from "./PlSqlParser";
import { Alter_tablespace_encryptionContext } from "./PlSqlParser";
import { Ts_file_name_convertContext } from "./PlSqlParser";
import { Alter_roleContext } from "./PlSqlParser";
import { Role_identified_clauseContext } from "./PlSqlParser";
import { Alter_tableContext } from "./PlSqlParser";
import { Memoptimize_read_write_clauseContext } from "./PlSqlParser";
import { Alter_table_propertiesContext } from "./PlSqlParser";
import { Alter_table_partitioningContext } from "./PlSqlParser";
import { Add_table_partitionContext } from "./PlSqlParser";
import { Drop_table_partitionContext } from "./PlSqlParser";
import { Merge_table_partitionContext } from "./PlSqlParser";
import { Modify_table_partitionContext } from "./PlSqlParser";
import { Split_table_partitionContext } from "./PlSqlParser";
import { Truncate_table_partitionContext } from "./PlSqlParser";
import { Exchange_table_partitionContext } from "./PlSqlParser";
import { Coalesce_table_partitionContext } from "./PlSqlParser";
import { Alter_interval_partitionContext } from "./PlSqlParser";
import { Partition_extended_namesContext } from "./PlSqlParser";
import { Subpartition_extended_namesContext } from "./PlSqlParser";
import { Alter_table_properties_1Context } from "./PlSqlParser";
import { Alter_iot_clausesContext } from "./PlSqlParser";
import { Alter_mapping_table_clauseContext } from "./PlSqlParser";
import { Alter_overflow_clauseContext } from "./PlSqlParser";
import { Add_overflow_clauseContext } from "./PlSqlParser";
import { Update_index_clausesContext } from "./PlSqlParser";
import { Update_global_index_clauseContext } from "./PlSqlParser";
import { Update_all_indexes_clauseContext } from "./PlSqlParser";
import { Update_all_indexes_index_clauseContext } from "./PlSqlParser";
import { Update_index_partitionContext } from "./PlSqlParser";
import { Update_index_subpartitionContext } from "./PlSqlParser";
import { Enable_disable_clauseContext } from "./PlSqlParser";
import { Using_index_clauseContext } from "./PlSqlParser";
import { Index_attributesContext } from "./PlSqlParser";
import { Sort_or_nosortContext } from "./PlSqlParser";
import { Exceptions_clauseContext } from "./PlSqlParser";
import { Move_table_clauseContext } from "./PlSqlParser";
import { Index_org_table_clauseContext } from "./PlSqlParser";
import { Mapping_table_clauseContext } from "./PlSqlParser";
import { Key_compressionContext } from "./PlSqlParser";
import { Index_org_overflow_clauseContext } from "./PlSqlParser";
import { Column_clausesContext } from "./PlSqlParser";
import { Modify_collection_retrievalContext } from "./PlSqlParser";
import { Collection_itemContext } from "./PlSqlParser";
import { Rename_column_clauseContext } from "./PlSqlParser";
import { Old_column_nameContext } from "./PlSqlParser";
import { New_column_nameContext } from "./PlSqlParser";
import { Add_modify_drop_column_clausesContext } from "./PlSqlParser";
import { Drop_column_clauseContext } from "./PlSqlParser";
import { Modify_column_clausesContext } from "./PlSqlParser";
import { Modify_col_propertiesContext } from "./PlSqlParser";
import { Modify_col_visibilityContext } from "./PlSqlParser";
import { Modify_col_substitutableContext } from "./PlSqlParser";
import { Add_column_clauseContext } from "./PlSqlParser";
import { Alter_varray_col_propertiesContext } from "./PlSqlParser";
import { Varray_col_propertiesContext } from "./PlSqlParser";
import { Varray_storage_clauseContext } from "./PlSqlParser";
import { Lob_segnameContext } from "./PlSqlParser";
import { Lob_itemContext } from "./PlSqlParser";
import { Lob_storage_parametersContext } from "./PlSqlParser";
import { Lob_storage_clauseContext } from "./PlSqlParser";
import { Modify_lob_storage_clauseContext } from "./PlSqlParser";
import { Modify_lob_parametersContext } from "./PlSqlParser";
import { Lob_parametersContext } from "./PlSqlParser";
import { Lob_deduplicate_clauseContext } from "./PlSqlParser";
import { Lob_compression_clauseContext } from "./PlSqlParser";
import { Lob_retention_clauseContext } from "./PlSqlParser";
import { Encryption_specContext } from "./PlSqlParser";
import { TablespaceContext } from "./PlSqlParser";
import { Varray_itemContext } from "./PlSqlParser";
import { Column_propertiesContext } from "./PlSqlParser";
import { Lob_partition_storageContext } from "./PlSqlParser";
import { Period_definitionContext } from "./PlSqlParser";
import { Start_time_columnContext } from "./PlSqlParser";
import { End_time_columnContext } from "./PlSqlParser";
import { Column_definitionContext } from "./PlSqlParser";
import { Column_collation_nameContext } from "./PlSqlParser";
import { Identity_clauseContext } from "./PlSqlParser";
import { Identity_options_parenthesesContext } from "./PlSqlParser";
import { Identity_optionsContext } from "./PlSqlParser";
import { Virtual_column_definitionContext } from "./PlSqlParser";
import { Autogenerated_sequence_definitionContext } from "./PlSqlParser";
import { Evaluation_edition_clauseContext } from "./PlSqlParser";
import { Out_of_line_part_storageContext } from "./PlSqlParser";
import { Nested_table_col_propertiesContext } from "./PlSqlParser";
import { Nested_itemContext } from "./PlSqlParser";
import { Substitutable_column_clauseContext } from "./PlSqlParser";
import { Partition_nameContext } from "./PlSqlParser";
import { Supplemental_logging_propsContext } from "./PlSqlParser";
import { Column_or_attributeContext } from "./PlSqlParser";
import { Object_type_col_propertiesContext } from "./PlSqlParser";
import { Constraint_clausesContext } from "./PlSqlParser";
import { Old_constraint_nameContext } from "./PlSqlParser";
import { New_constraint_nameContext } from "./PlSqlParser";
import { Drop_constraint_clauseContext } from "./PlSqlParser";
import { Add_constraintContext } from "./PlSqlParser";
import { Add_constraint_clauseContext } from "./PlSqlParser";
import { Check_constraintContext } from "./PlSqlParser";
import { Drop_constraintContext } from "./PlSqlParser";
import { Enable_constraintContext } from "./PlSqlParser";
import { Disable_constraintContext } from "./PlSqlParser";
import { Foreign_key_clauseContext } from "./PlSqlParser";
import { References_clauseContext } from "./PlSqlParser";
import { On_delete_clauseContext } from "./PlSqlParser";
import { Unique_key_clauseContext } from "./PlSqlParser";
import { Primary_key_clauseContext } from "./PlSqlParser";
import { Anonymous_blockContext } from "./PlSqlParser";
import { Invoker_rights_clauseContext } from "./PlSqlParser";
import { Call_specContext } from "./PlSqlParser";
import { Java_specContext } from "./PlSqlParser";
import { C_specContext } from "./PlSqlParser";
import { C_agent_in_clauseContext } from "./PlSqlParser";
import { C_parameters_clauseContext } from "./PlSqlParser";
import { ParameterContext } from "./PlSqlParser";
import { Default_value_partContext } from "./PlSqlParser";
import { Seq_of_declare_specsContext } from "./PlSqlParser";
import { Declare_specContext } from "./PlSqlParser";
import { Variable_declarationContext } from "./PlSqlParser";
import { Subtype_declarationContext } from "./PlSqlParser";
import { Cursor_declarationContext } from "./PlSqlParser";
import { Parameter_specContext } from "./PlSqlParser";
import { Exception_declarationContext } from "./PlSqlParser";
import { Pragma_declarationContext } from "./PlSqlParser";
import { Record_type_defContext } from "./PlSqlParser";
import { Field_specContext } from "./PlSqlParser";
import { Ref_cursor_type_defContext } from "./PlSqlParser";
import { Type_declarationContext } from "./PlSqlParser";
import { Table_type_defContext } from "./PlSqlParser";
import { Table_indexed_by_partContext } from "./PlSqlParser";
import { Varray_type_defContext } from "./PlSqlParser";
import { Seq_of_statementsContext } from "./PlSqlParser";
import { Label_declarationContext } from "./PlSqlParser";
import { StatementContext } from "./PlSqlParser";
import { Swallow_to_semiContext } from "./PlSqlParser";
import { Assignment_statementContext } from "./PlSqlParser";
import { Continue_statementContext } from "./PlSqlParser";
import { Exit_statementContext } from "./PlSqlParser";
import { Goto_statementContext } from "./PlSqlParser";
import { If_statementContext } from "./PlSqlParser";
import { Elsif_partContext } from "./PlSqlParser";
import { Else_partContext } from "./PlSqlParser";
import { Loop_statementContext } from "./PlSqlParser";
import { Cursor_loop_paramContext } from "./PlSqlParser";
import { Forall_statementContext } from "./PlSqlParser";
import { Bounds_clauseContext } from "./PlSqlParser";
import { Between_boundContext } from "./PlSqlParser";
import { Lower_boundContext } from "./PlSqlParser";
import { Upper_boundContext } from "./PlSqlParser";
import { Null_statementContext } from "./PlSqlParser";
import { Raise_statementContext } from "./PlSqlParser";
import { Return_statementContext } from "./PlSqlParser";
import { Call_statementContext } from "./PlSqlParser";
import { Pipe_row_statementContext } from "./PlSqlParser";
import { BodyContext } from "./PlSqlParser";
import { Exception_handlerContext } from "./PlSqlParser";
import { Trigger_blockContext } from "./PlSqlParser";
import { Tps_bodyContext } from "./PlSqlParser";
import { BlockContext } from "./PlSqlParser";
import { Sql_statementContext } from "./PlSqlParser";
import { Execute_immediateContext } from "./PlSqlParser";
import { Dynamic_returning_clauseContext } from "./PlSqlParser";
import { Data_manipulation_language_statementsContext } from "./PlSqlParser";
import { Cursor_manipulation_statementsContext } from "./PlSqlParser";
import { Close_statementContext } from "./PlSqlParser";
import { Open_statementContext } from "./PlSqlParser";
import { Fetch_statementContext } from "./PlSqlParser";
import { Open_for_statementContext } from "./PlSqlParser";
import { Transaction_control_statementsContext } from "./PlSqlParser";
import { Set_transaction_commandContext } from "./PlSqlParser";
import { Set_constraint_commandContext } from "./PlSqlParser";
import { Commit_statementContext } from "./PlSqlParser";
import { Write_clauseContext } from "./PlSqlParser";
import { Rollback_statementContext } from "./PlSqlParser";
import { Savepoint_statementContext } from "./PlSqlParser";
import { Explain_statementContext } from "./PlSqlParser";
import { Select_only_statementContext } from "./PlSqlParser";
import { Select_statementContext } from "./PlSqlParser";
import { Subquery_factoring_clauseContext } from "./PlSqlParser";
import { Factoring_elementContext } from "./PlSqlParser";
import { Search_clauseContext } from "./PlSqlParser";
import { Cycle_clauseContext } from "./PlSqlParser";
import { SubqueryContext } from "./PlSqlParser";
import { Subquery_basic_elementsContext } from "./PlSqlParser";
import { Subquery_operation_partContext } from "./PlSqlParser";
import { Query_blockContext } from "./PlSqlParser";
import { Selected_listContext } from "./PlSqlParser";
import { From_clauseContext } from "./PlSqlParser";
import { Select_list_elementsContext } from "./PlSqlParser";
import { Table_ref_listContext } from "./PlSqlParser";
import { Table_refContext } from "./PlSqlParser";
import { Table_ref_auxContext } from "./PlSqlParser";
import { Table_ref_aux_internalContext } from "./PlSqlParser";
import { Join_clauseContext } from "./PlSqlParser";
import { Join_on_partContext } from "./PlSqlParser";
import { Join_using_partContext } from "./PlSqlParser";
import { Outer_join_typeContext } from "./PlSqlParser";
import { Query_partition_clauseContext } from "./PlSqlParser";
import { Flashback_query_clauseContext } from "./PlSqlParser";
import { Pivot_clauseContext } from "./PlSqlParser";
import { Pivot_elementContext } from "./PlSqlParser";
import { Pivot_for_clauseContext } from "./PlSqlParser";
import { Pivot_in_clauseContext } from "./PlSqlParser";
import { Pivot_in_clause_elementContext } from "./PlSqlParser";
import { Pivot_in_clause_elementsContext } from "./PlSqlParser";
import { Unpivot_clauseContext } from "./PlSqlParser";
import { Unpivot_in_clauseContext } from "./PlSqlParser";
import { Unpivot_in_elementsContext } from "./PlSqlParser";
import { Hierarchical_query_clauseContext } from "./PlSqlParser";
import { Start_partContext } from "./PlSqlParser";
import { Group_by_clauseContext } from "./PlSqlParser";
import { Group_by_elementsContext } from "./PlSqlParser";
import { Rollup_cube_clauseContext } from "./PlSqlParser";
import { Grouping_sets_clauseContext } from "./PlSqlParser";
import { Grouping_sets_elementsContext } from "./PlSqlParser";
import { Having_clauseContext } from "./PlSqlParser";
import { Model_clauseContext } from "./PlSqlParser";
import { Cell_reference_optionsContext } from "./PlSqlParser";
import { Return_rows_clauseContext } from "./PlSqlParser";
import { Reference_modelContext } from "./PlSqlParser";
import { Main_modelContext } from "./PlSqlParser";
import { Model_column_clausesContext } from "./PlSqlParser";
import { Model_column_partition_partContext } from "./PlSqlParser";
import { Model_column_listContext } from "./PlSqlParser";
import { Model_columnContext } from "./PlSqlParser";
import { Model_rules_clauseContext } from "./PlSqlParser";
import { Model_rules_partContext } from "./PlSqlParser";
import { Model_rules_elementContext } from "./PlSqlParser";
import { Cell_assignmentContext } from "./PlSqlParser";
import { Model_iterate_clauseContext } from "./PlSqlParser";
import { Until_partContext } from "./PlSqlParser";
import { Order_by_clauseContext } from "./PlSqlParser";
import { Order_by_elementsContext } from "./PlSqlParser";
import { Offset_clauseContext } from "./PlSqlParser";
import { Fetch_clauseContext } from "./PlSqlParser";
import { For_update_clauseContext } from "./PlSqlParser";
import { For_update_of_partContext } from "./PlSqlParser";
import { For_update_optionsContext } from "./PlSqlParser";
import { Update_statementContext } from "./PlSqlParser";
import { Update_set_clauseContext } from "./PlSqlParser";
import { Column_based_update_set_clauseContext } from "./PlSqlParser";
import { Delete_statementContext } from "./PlSqlParser";
import { Insert_statementContext } from "./PlSqlParser";
import { Single_table_insertContext } from "./PlSqlParser";
import { Multi_table_insertContext } from "./PlSqlParser";
import { Multi_table_elementContext } from "./PlSqlParser";
import { Conditional_insert_clauseContext } from "./PlSqlParser";
import { Conditional_insert_when_partContext } from "./PlSqlParser";
import { Conditional_insert_else_partContext } from "./PlSqlParser";
import { Insert_into_clauseContext } from "./PlSqlParser";
import { Values_clauseContext } from "./PlSqlParser";
import { Merge_statementContext } from "./PlSqlParser";
import { Merge_update_clauseContext } from "./PlSqlParser";
import { Merge_elementContext } from "./PlSqlParser";
import { Merge_update_delete_partContext } from "./PlSqlParser";
import { Merge_insert_clauseContext } from "./PlSqlParser";
import { Selected_tableviewContext } from "./PlSqlParser";
import { Lock_table_statementContext } from "./PlSqlParser";
import { Wait_nowait_partContext } from "./PlSqlParser";
import { Lock_table_elementContext } from "./PlSqlParser";
import { Lock_modeContext } from "./PlSqlParser";
import { General_table_refContext } from "./PlSqlParser";
import { Static_returning_clauseContext } from "./PlSqlParser";
import { Error_logging_clauseContext } from "./PlSqlParser";
import { Error_logging_into_partContext } from "./PlSqlParser";
import { Error_logging_reject_partContext } from "./PlSqlParser";
import { Dml_table_expression_clauseContext } from "./PlSqlParser";
import { Table_collection_expressionContext } from "./PlSqlParser";
import { Subquery_restriction_clauseContext } from "./PlSqlParser";
import { Sample_clauseContext } from "./PlSqlParser";
import { Seed_partContext } from "./PlSqlParser";
import { ConditionContext } from "./PlSqlParser";
import { Json_conditionContext } from "./PlSqlParser";
import { ExpressionsContext } from "./PlSqlParser";
import { ExpressionContext } from "./PlSqlParser";
import { Cursor_expressionContext } from "./PlSqlParser";
import { Logical_expressionContext } from "./PlSqlParser";
import { Unary_logical_expressionContext } from "./PlSqlParser";
import { Unary_logical_operationContext } from "./PlSqlParser";
import { Logical_operationContext } from "./PlSqlParser";
import { Multiset_expressionContext } from "./PlSqlParser";
import { Relational_expressionContext } from "./PlSqlParser";
import { Compound_expressionContext } from "./PlSqlParser";
import { Relational_operatorContext } from "./PlSqlParser";
import { In_elementsContext } from "./PlSqlParser";
import { Between_elementsContext } from "./PlSqlParser";
import { ConcatenationContext } from "./PlSqlParser";
import { Interval_expressionContext } from "./PlSqlParser";
import { Model_expressionContext } from "./PlSqlParser";
import { Model_expression_elementContext } from "./PlSqlParser";
import { Single_column_for_loopContext } from "./PlSqlParser";
import { Multi_column_for_loopContext } from "./PlSqlParser";
import { Unary_expressionContext } from "./PlSqlParser";
import { Case_statementContext } from "./PlSqlParser";
import { Simple_case_statementContext } from "./PlSqlParser";
import { Simple_case_when_partContext } from "./PlSqlParser";
import { Searched_case_statementContext } from "./PlSqlParser";
import { Searched_case_when_partContext } from "./PlSqlParser";
import { Case_else_partContext } from "./PlSqlParser";
import { AtomContext } from "./PlSqlParser";
import { Quantified_expressionContext } from "./PlSqlParser";
import { String_functionContext } from "./PlSqlParser";
import { Standard_functionContext } from "./PlSqlParser";
import { Json_functionContext } from "./PlSqlParser";
import { Json_object_contentContext } from "./PlSqlParser";
import { Json_object_entryContext } from "./PlSqlParser";
import { Json_table_clauseContext } from "./PlSqlParser";
import { Json_array_elementContext } from "./PlSqlParser";
import { Json_on_null_clauseContext } from "./PlSqlParser";
import { Json_return_clauseContext } from "./PlSqlParser";
import { Json_transform_opContext } from "./PlSqlParser";
import { Json_column_clauseContext } from "./PlSqlParser";
import { Json_column_definitionContext } from "./PlSqlParser";
import { Json_query_returning_clauseContext } from "./PlSqlParser";
import { Json_query_return_typeContext } from "./PlSqlParser";
import { Json_query_wrapper_clauseContext } from "./PlSqlParser";
import { Json_query_on_error_clauseContext } from "./PlSqlParser";
import { Json_query_on_empty_clauseContext } from "./PlSqlParser";
import { Json_value_return_clauseContext } from "./PlSqlParser";
import { Json_value_return_typeContext } from "./PlSqlParser";
import { Json_value_on_mismatch_clauseContext } from "./PlSqlParser";
import { LiteralContext } from "./PlSqlParser";
import { Numeric_function_wrapperContext } from "./PlSqlParser";
import { Numeric_functionContext } from "./PlSqlParser";
import { Listagg_overflow_clauseContext } from "./PlSqlParser";
import { Other_functionContext } from "./PlSqlParser";
import { Over_clause_keywordContext } from "./PlSqlParser";
import { Within_or_over_clause_keywordContext } from "./PlSqlParser";
import { Standard_prediction_function_keywordContext } from "./PlSqlParser";
import { Over_clauseContext } from "./PlSqlParser";
import { Windowing_clauseContext } from "./PlSqlParser";
import { Windowing_typeContext } from "./PlSqlParser";
import { Windowing_elementsContext } from "./PlSqlParser";
import { Using_clauseContext } from "./PlSqlParser";
import { Using_elementContext } from "./PlSqlParser";
import { Collect_order_by_partContext } from "./PlSqlParser";
import { Within_or_over_partContext } from "./PlSqlParser";
import { Cost_matrix_clauseContext } from "./PlSqlParser";
import { Xml_passing_clauseContext } from "./PlSqlParser";
import { Xml_attributes_clauseContext } from "./PlSqlParser";
import { Xml_namespaces_clauseContext } from "./PlSqlParser";
import { Xml_table_columnContext } from "./PlSqlParser";
import { Xml_general_default_partContext } from "./PlSqlParser";
import { Xml_multiuse_expression_elementContext } from "./PlSqlParser";
import { Xmlroot_param_version_partContext } from "./PlSqlParser";
import { Xmlroot_param_standalone_partContext } from "./PlSqlParser";
import { Xmlserialize_param_enconding_partContext } from "./PlSqlParser";
import { Xmlserialize_param_version_partContext } from "./PlSqlParser";
import { Xmlserialize_param_ident_partContext } from "./PlSqlParser";
import { Sql_plus_command_no_semicolonContext } from "./PlSqlParser";
import { Sql_plus_commandContext } from "./PlSqlParser";
import { Start_commandContext } from "./PlSqlParser";
import { Whenever_commandContext } from "./PlSqlParser";
import { Set_commandContext } from "./PlSqlParser";
import { Timing_commandContext } from "./PlSqlParser";
import { Partition_extension_clauseContext } from "./PlSqlParser";
import { Column_aliasContext } from "./PlSqlParser";
import { Table_aliasContext } from "./PlSqlParser";
import { Where_clauseContext } from "./PlSqlParser";
import { Quantitative_where_stmtContext } from "./PlSqlParser";
import { Into_clauseContext } from "./PlSqlParser";
import { Xml_column_nameContext } from "./PlSqlParser";
import { Cost_class_nameContext } from "./PlSqlParser";
import { Attribute_nameContext } from "./PlSqlParser";
import { Savepoint_nameContext } from "./PlSqlParser";
import { Rollback_segment_nameContext } from "./PlSqlParser";
import { Table_var_nameContext } from "./PlSqlParser";
import { Schema_nameContext } from "./PlSqlParser";
import { Routine_nameContext } from "./PlSqlParser";
import { Package_nameContext } from "./PlSqlParser";
import { Implementation_type_nameContext } from "./PlSqlParser";
import { Parameter_nameContext } from "./PlSqlParser";
import { Reference_model_nameContext } from "./PlSqlParser";
import { Main_model_nameContext } from "./PlSqlParser";
import { Container_tableview_nameContext } from "./PlSqlParser";
import { Aggregate_function_nameContext } from "./PlSqlParser";
import { Query_nameContext } from "./PlSqlParser";
import { Grantee_nameContext } from "./PlSqlParser";
import { Role_nameContext } from "./PlSqlParser";
import { Constraint_nameContext } from "./PlSqlParser";
import { Label_nameContext } from "./PlSqlParser";
import { Type_nameContext } from "./PlSqlParser";
import { Sequence_nameContext } from "./PlSqlParser";
import { Exception_nameContext } from "./PlSqlParser";
import { Function_nameContext } from "./PlSqlParser";
import { Procedure_nameContext } from "./PlSqlParser";
import { Trigger_nameContext } from "./PlSqlParser";
import { Variable_nameContext } from "./PlSqlParser";
import { Index_nameContext } from "./PlSqlParser";
import { Cursor_nameContext } from "./PlSqlParser";
import { Record_nameContext } from "./PlSqlParser";
import { Collection_nameContext } from "./PlSqlParser";
import { Link_nameContext } from "./PlSqlParser";
import { Column_nameContext } from "./PlSqlParser";
import { Tableview_nameContext } from "./PlSqlParser";
import { XmltableContext } from "./PlSqlParser";
import { Char_set_nameContext } from "./PlSqlParser";
import { Synonym_nameContext } from "./PlSqlParser";
import { Schema_object_nameContext } from "./PlSqlParser";
import { Dir_object_nameContext } from "./PlSqlParser";
import { User_object_nameContext } from "./PlSqlParser";
import { Grant_object_nameContext } from "./PlSqlParser";
import { Column_listContext } from "./PlSqlParser";
import { Paren_column_listContext } from "./PlSqlParser";
import { Keep_clauseContext } from "./PlSqlParser";
import { Function_argumentContext } from "./PlSqlParser";
import { Function_argument_analyticContext } from "./PlSqlParser";
import { Function_argument_modelingContext } from "./PlSqlParser";
import { Respect_or_ignore_nullsContext } from "./PlSqlParser";
import { ArgumentContext } from "./PlSqlParser";
import { Type_specContext } from "./PlSqlParser";
import { DatatypeContext } from "./PlSqlParser";
import { Precision_partContext } from "./PlSqlParser";
import { Native_datatype_elementContext } from "./PlSqlParser";
import { Bind_variableContext } from "./PlSqlParser";
import { General_elementContext } from "./PlSqlParser";
import { General_element_partContext } from "./PlSqlParser";
import { Table_elementContext } from "./PlSqlParser";
import { Object_privilegeContext } from "./PlSqlParser";
import { System_privilegeContext } from "./PlSqlParser";
import { ConstantContext } from "./PlSqlParser";
import { NumericContext } from "./PlSqlParser";
import { Numeric_negativeContext } from "./PlSqlParser";
import { Quoted_stringContext } from "./PlSqlParser";
import { IdentifierContext } from "./PlSqlParser";
import { Id_expressionContext } from "./PlSqlParser";
import { Outer_join_signContext } from "./PlSqlParser";
import { Regular_idContext } from "./PlSqlParser";
import { Non_reserved_keywords_in_12cContext } from "./PlSqlParser";
import { Non_reserved_keywords_pre12cContext } from "./PlSqlParser";
import { String_function_nameContext } from "./PlSqlParser";
import { Numeric_function_nameContext } from "./PlSqlParser";
/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by `PlSqlParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface PlSqlParserVisitor<Result> extends ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the `table_ref_aux_internal_one`
     * labeled alternative in `PlSqlParser.table_ref_aux_internal`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_aux_internal_one?: (ctx: Table_ref_aux_internal_oneContext) => Result;
    /**
     * Visit a parse tree produced by the `table_ref_aux_internal_two`
     * labeled alternative in `PlSqlParser.table_ref_aux_internal`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_aux_internal_two?: (ctx: Table_ref_aux_internal_twoContext) => Result;
    /**
     * Visit a parse tree produced by the `table_ref_aux_internal_three`
     * labeled alternative in `PlSqlParser.table_ref_aux_internal`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_aux_internal_three?: (ctx: Table_ref_aux_internal_threeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.program`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram?: (ctx: ProgramContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_script`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_script?: (ctx: Sql_scriptContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unit_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnit_statement?: (ctx: Unit_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_diskgroup`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_diskgroup?: (ctx: Alter_diskgroupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_disk_clause?: (ctx: Add_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_disk_clause?: (ctx: Drop_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.resize_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResize_disk_clause?: (ctx: Resize_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.replace_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReplace_disk_clause?: (ctx: Replace_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.wait_nowait`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWait_nowait?: (ctx: Wait_nowaitContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rename_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRename_disk_clause?: (ctx: Rename_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disk_online_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisk_online_clause?: (ctx: Disk_online_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disk_offline_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisk_offline_clause?: (ctx: Disk_offline_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.timeout_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTimeout_clause?: (ctx: Timeout_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rebalance_diskgroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRebalance_diskgroup_clause?: (ctx: Rebalance_diskgroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.phase`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPhase?: (ctx: PhaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.check_diskgroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCheck_diskgroup_clause?: (ctx: Check_diskgroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_template_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_template_clauses?: (ctx: Diskgroup_template_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.qualified_template_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQualified_template_clause?: (ctx: Qualified_template_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.redundancy_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRedundancy_clause?: (ctx: Redundancy_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.striping_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStriping_clause?: (ctx: Striping_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.force_noforce`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitForce_noforce?: (ctx: Force_noforceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_directory_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_directory_clauses?: (ctx: Diskgroup_directory_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dir_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDir_name?: (ctx: Dir_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_alias_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_alias_clauses?: (ctx: Diskgroup_alias_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_volume_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_volume_clauses?: (ctx: Diskgroup_volume_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_volume_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_volume_clause?: (ctx: Add_volume_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_volume_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_volume_clause?: (ctx: Modify_volume_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_attributes`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_attributes?: (ctx: Diskgroup_attributesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_diskgroup_file`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_diskgroup_file?: (ctx: Modify_diskgroup_fileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disk_region_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisk_region_clause?: (ctx: Disk_region_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_diskgroup_file_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_diskgroup_file_clause?: (ctx: Drop_diskgroup_file_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.convert_redundancy_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConvert_redundancy_clause?: (ctx: Convert_redundancy_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.usergroup_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsergroup_clauses?: (ctx: Usergroup_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_clauses?: (ctx: User_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.file_permissions_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFile_permissions_clause?: (ctx: File_permissions_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.file_owner_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFile_owner_clause?: (ctx: File_owner_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.scrub_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitScrub_clause?: (ctx: Scrub_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quotagroup_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuotagroup_clauses?: (ctx: Quotagroup_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.property_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperty_name?: (ctx: Property_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.property_value`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperty_value?: (ctx: Property_valueContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.filegroup_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFilegroup_clauses?: (ctx: Filegroup_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_filegroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_filegroup_clause?: (ctx: Add_filegroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_filegroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_filegroup_clause?: (ctx: Modify_filegroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.move_to_filegroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMove_to_filegroup_clause?: (ctx: Move_to_filegroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_filegroup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_filegroup_clause?: (ctx: Drop_filegroup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quorum_regular`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuorum_regular?: (ctx: Quorum_regularContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.undrop_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUndrop_disk_clause?: (ctx: Undrop_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.diskgroup_availability`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDiskgroup_availability?: (ctx: Diskgroup_availabilityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.enable_disable_volume`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnable_disable_volume?: (ctx: Enable_disable_volumeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_function?: (ctx: Drop_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_flashback_archive`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_flashback_archive?: (ctx: Alter_flashback_archiveContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_hierarchy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_hierarchy?: (ctx: Alter_hierarchyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_function?: (ctx: Alter_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_java`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_java?: (ctx: Alter_javaContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.match_string`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMatch_string?: (ctx: Match_stringContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_function_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_function_body?: (ctx: Create_function_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_macro_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_macro_body?: (ctx: Sql_macro_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parallel_enable_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParallel_enable_clause?: (ctx: Parallel_enable_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_by_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_by_clause?: (ctx: Partition_by_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.result_cache_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResult_cache_clause?: (ctx: Result_cache_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.relies_on_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelies_on_part?: (ctx: Relies_on_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.streaming_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStreaming_clause?: (ctx: Streaming_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_outline`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_outline?: (ctx: Alter_outlineContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.outline_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOutline_options?: (ctx: Outline_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_lockdown_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_lockdown_profile?: (ctx: Alter_lockdown_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lockdown_feature`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLockdown_feature?: (ctx: Lockdown_featureContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lockdown_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLockdown_options?: (ctx: Lockdown_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lockdown_statements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLockdown_statements?: (ctx: Lockdown_statementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.statement_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatement_clauses?: (ctx: Statement_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.clause_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClause_options?: (ctx: Clause_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.option_values`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOption_values?: (ctx: Option_valuesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.string_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitString_list?: (ctx: String_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disable_enable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisable_enable?: (ctx: Disable_enableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_lockdown_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_lockdown_profile?: (ctx: Drop_lockdown_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_package`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_package?: (ctx: Drop_packageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_package`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_package?: (ctx: Alter_packageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_package`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_package?: (ctx: Create_packageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_package_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_package_body?: (ctx: Create_package_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.package_obj_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPackage_obj_spec?: (ctx: Package_obj_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.procedure_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProcedure_spec?: (ctx: Procedure_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_spec?: (ctx: Function_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.package_obj_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPackage_obj_body?: (ctx: Package_obj_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_pmem_filestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_pmem_filestore?: (ctx: Alter_pmem_filestoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_pmem_filestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_pmem_filestore?: (ctx: Drop_pmem_filestoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_procedure`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_procedure?: (ctx: Drop_procedureContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_procedure`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_procedure?: (ctx: Alter_procedureContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_body?: (ctx: Function_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.procedure_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProcedure_body?: (ctx: Procedure_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_procedure_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_procedure_body?: (ctx: Create_procedure_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_resource_cost`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_resource_cost?: (ctx: Alter_resource_costContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_outline`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_outline?: (ctx: Drop_outlineContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_rollback_segment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_rollback_segment?: (ctx: Alter_rollback_segmentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_restore_point`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_restore_point?: (ctx: Drop_restore_pointContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_rollback_segment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_rollback_segment?: (ctx: Drop_rollback_segmentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_role`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_role?: (ctx: Drop_roleContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_pmem_filestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_pmem_filestore?: (ctx: Create_pmem_filestoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pmem_filestore_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPmem_filestore_options?: (ctx: Pmem_filestore_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.file_path`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFile_path?: (ctx: File_pathContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_rollback_segment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_rollback_segment?: (ctx: Create_rollback_segmentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_trigger?: (ctx: Drop_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_trigger?: (ctx: Alter_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_trigger?: (ctx: Create_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trigger_follows_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrigger_follows_clause?: (ctx: Trigger_follows_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trigger_when_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrigger_when_clause?: (ctx: Trigger_when_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.simple_dml_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimple_dml_trigger?: (ctx: Simple_dml_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.for_each_row`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFor_each_row?: (ctx: For_each_rowContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compound_dml_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompound_dml_trigger?: (ctx: Compound_dml_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.non_dml_trigger`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNon_dml_trigger?: (ctx: Non_dml_triggerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trigger_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrigger_body?: (ctx: Trigger_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.routine_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRoutine_clause?: (ctx: Routine_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compound_trigger_block`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompound_trigger_block?: (ctx: Compound_trigger_blockContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.timing_point_section`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTiming_point_section?: (ctx: Timing_point_sectionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.non_dml_event`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNon_dml_event?: (ctx: Non_dml_eventContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dml_event_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDml_event_clause?: (ctx: Dml_event_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dml_event_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDml_event_element?: (ctx: Dml_event_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dml_event_nested_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDml_event_nested_clause?: (ctx: Dml_event_nested_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.referencing_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReferencing_clause?: (ctx: Referencing_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.referencing_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReferencing_element?: (ctx: Referencing_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_type?: (ctx: Drop_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_type?: (ctx: Alter_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compile_type_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompile_type_clause?: (ctx: Compile_type_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.replace_type_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReplace_type_clause?: (ctx: Replace_type_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_method_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_method_spec?: (ctx: Alter_method_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_method_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_method_element?: (ctx: Alter_method_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_attribute_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_attribute_definition?: (ctx: Alter_attribute_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.attribute_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAttribute_definition?: (ctx: Attribute_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_collection_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_collection_clauses?: (ctx: Alter_collection_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dependent_handling_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDependent_handling_clause?: (ctx: Dependent_handling_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dependent_exceptions_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDependent_exceptions_part?: (ctx: Dependent_exceptions_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_type?: (ctx: Create_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_definition?: (ctx: Type_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_type_def?: (ctx: Object_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_as_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_as_part?: (ctx: Object_as_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_under_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_under_part?: (ctx: Object_under_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.nested_table_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNested_table_type_def?: (ctx: Nested_table_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sqlj_object_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlj_object_type?: (ctx: Sqlj_object_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_body?: (ctx: Type_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_body_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_body_elements?: (ctx: Type_body_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.map_order_func_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMap_order_func_declaration?: (ctx: Map_order_func_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subprog_decl_in_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubprog_decl_in_type?: (ctx: Subprog_decl_in_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.proc_decl_in_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProc_decl_in_type?: (ctx: Proc_decl_in_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.func_decl_in_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunc_decl_in_type?: (ctx: Func_decl_in_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constructor_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstructor_declaration?: (ctx: Constructor_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modifier_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifier_clause?: (ctx: Modifier_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_member_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_member_spec?: (ctx: Object_member_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sqlj_object_type_attr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlj_object_type_attr?: (ctx: Sqlj_object_type_attrContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.element_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitElement_spec?: (ctx: Element_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.element_spec_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitElement_spec_options?: (ctx: Element_spec_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subprogram_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubprogram_spec?: (ctx: Subprogram_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.overriding_subprogram_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOverriding_subprogram_spec?: (ctx: Overriding_subprogram_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.overriding_function_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOverriding_function_spec?: (ctx: Overriding_function_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_procedure_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_procedure_spec?: (ctx: Type_procedure_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_function_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_function_spec?: (ctx: Type_function_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constructor_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstructor_spec?: (ctx: Constructor_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.map_order_function_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMap_order_function_spec?: (ctx: Map_order_function_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pragma_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPragma_clause?: (ctx: Pragma_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pragma_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPragma_elements?: (ctx: Pragma_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_elements_parameter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_elements_parameter?: (ctx: Type_elements_parameterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_sequence`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_sequence?: (ctx: Drop_sequenceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_sequence`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_sequence?: (ctx: Alter_sequenceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_session`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_session?: (ctx: Alter_sessionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_session_set_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_session_set_clause?: (ctx: Alter_session_set_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_sequence`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_sequence?: (ctx: Create_sequenceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sequence_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSequence_spec?: (ctx: Sequence_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sequence_start_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSequence_start_clause?: (ctx: Sequence_start_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_analytic_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_analytic_view?: (ctx: Create_analytic_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.classification_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClassification_clause?: (ctx: Classification_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.caption_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCaption_clause?: (ctx: Caption_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.description_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDescription_clause?: (ctx: Description_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.classification_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClassification_item?: (ctx: Classification_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.language`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLanguage?: (ctx: LanguageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cav_using_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCav_using_clause?: (ctx: Cav_using_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dim_by_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDim_by_clause?: (ctx: Dim_by_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dim_key`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDim_key?: (ctx: Dim_keyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dim_ref`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDim_ref?: (ctx: Dim_refContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hier_ref`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHier_ref?: (ctx: Hier_refContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.measures_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMeasures_clause?: (ctx: Measures_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.av_measure`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAv_measure?: (ctx: Av_measureContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.base_meas_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBase_meas_clause?: (ctx: Base_meas_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.meas_aggregate_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMeas_aggregate_clause?: (ctx: Meas_aggregate_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.calc_meas_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCalc_meas_clause?: (ctx: Calc_meas_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_measure_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_measure_clause?: (ctx: Default_measure_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_aggregate_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_aggregate_clause?: (ctx: Default_aggregate_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cache_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCache_clause?: (ctx: Cache_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cache_specification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCache_specification?: (ctx: Cache_specificationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.levels_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevels_clause?: (ctx: Levels_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.level_specification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevel_specification?: (ctx: Level_specificationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.level_group_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevel_group_type?: (ctx: Level_group_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.fact_columns_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFact_columns_clause?: (ctx: Fact_columns_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.qry_transform_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQry_transform_clause?: (ctx: Qry_transform_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_attribute_dimension`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_attribute_dimension?: (ctx: Create_attribute_dimensionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ad_using_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAd_using_clause?: (ctx: Ad_using_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.source_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSource_clause?: (ctx: Source_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_path_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_path_clause?: (ctx: Join_path_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_condition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_condition?: (ctx: Join_conditionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_condition_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_condition_item?: (ctx: Join_condition_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.attributes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAttributes_clause?: (ctx: Attributes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ad_attributes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAd_attributes_clause?: (ctx: Ad_attributes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ad_level_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAd_level_clause?: (ctx: Ad_level_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKey_clause?: (ctx: Key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alternate_key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlternate_key_clause?: (ctx: Alternate_key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dim_order_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDim_order_clause?: (ctx: Dim_order_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.all_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAll_clause?: (ctx: All_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_audit_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_audit_policy?: (ctx: Create_audit_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.privilege_audit_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivilege_audit_clause?: (ctx: Privilege_audit_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.action_audit_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAction_audit_clause?: (ctx: Action_audit_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.system_actions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystem_actions?: (ctx: System_actionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.standard_actions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStandard_actions?: (ctx: Standard_actionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.actions_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitActions_clause?: (ctx: Actions_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_action`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_action?: (ctx: Object_actionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.system_action`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystem_action?: (ctx: System_actionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.component_actions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComponent_actions?: (ctx: Component_actionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.component_action`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComponent_action?: (ctx: Component_actionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.role_audit_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRole_audit_clause?: (ctx: Role_audit_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_controlfile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_controlfile?: (ctx: Create_controlfileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.controlfile_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitControlfile_options?: (ctx: Controlfile_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logfile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogfile_clause?: (ctx: Logfile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.character_set_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCharacter_set_clause?: (ctx: Character_set_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.file_specification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFile_specification?: (ctx: File_specificationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_diskgroup`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_diskgroup?: (ctx: Create_diskgroupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.qualified_disk_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQualified_disk_clause?: (ctx: Qualified_disk_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_edition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_edition?: (ctx: Create_editionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_flashback_archive`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_flashback_archive?: (ctx: Create_flashback_archiveContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_archive_quota`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_archive_quota?: (ctx: Flashback_archive_quotaContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_archive_retention`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_archive_retention?: (ctx: Flashback_archive_retentionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_hierarchy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_hierarchy?: (ctx: Create_hierarchyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hier_using_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHier_using_clause?: (ctx: Hier_using_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.level_hier_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevel_hier_clause?: (ctx: Level_hier_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hier_attrs_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHier_attrs_clause?: (ctx: Hier_attrs_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hier_attr_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHier_attr_clause?: (ctx: Hier_attr_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hier_attr_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHier_attr_name?: (ctx: Hier_attr_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_index`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_index?: (ctx: Create_indexContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cluster_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCluster_index_clause?: (ctx: Cluster_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cluster_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCluster_name?: (ctx: Cluster_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_index_clause?: (ctx: Table_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.bitmap_join_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBitmap_join_index_clause?: (ctx: Bitmap_join_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_expr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_expr?: (ctx: Index_exprContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_properties?: (ctx: Index_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.domain_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDomain_index_clause?: (ctx: Domain_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.local_domain_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLocal_domain_index_clause?: (ctx: Local_domain_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlindex_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlindex_clause?: (ctx: Xmlindex_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.local_xmlindex_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLocal_xmlindex_clause?: (ctx: Local_xmlindex_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.global_partitioned_index`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGlobal_partitioned_index?: (ctx: Global_partitioned_indexContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_partitioning_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_partitioning_clause?: (ctx: Index_partitioning_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.local_partitioned_index`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLocal_partitioned_index?: (ctx: Local_partitioned_indexContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_range_partitioned_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_range_partitioned_table?: (ctx: On_range_partitioned_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_list_partitioned_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_list_partitioned_table?: (ctx: On_list_partitioned_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partitioned_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitioned_table?: (ctx: Partitioned_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_hash_partitioned_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_hash_partitioned_table?: (ctx: On_hash_partitioned_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_hash_partitioned_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_hash_partitioned_clause?: (ctx: On_hash_partitioned_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_comp_partitioned_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_comp_partitioned_table?: (ctx: On_comp_partitioned_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_comp_partitioned_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_comp_partitioned_clause?: (ctx: On_comp_partitioned_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_subpartition_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_subpartition_clause?: (ctx: Index_subpartition_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_subpartition_subclause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_subpartition_subclause?: (ctx: Index_subpartition_subclauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.odci_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOdci_parameters?: (ctx: Odci_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.indextype`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndextype?: (ctx: IndextypeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_index`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_index?: (ctx: Alter_indexContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_index_ops_set1`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_index_ops_set1?: (ctx: Alter_index_ops_set1Context) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_index_ops_set2`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_index_ops_set2?: (ctx: Alter_index_ops_set2Context) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.visible_or_invisible`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVisible_or_invisible?: (ctx: Visible_or_invisibleContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.monitoring_nomonitoring`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMonitoring_nomonitoring?: (ctx: Monitoring_nomonitoringContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rebuild_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRebuild_clause?: (ctx: Rebuild_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_index_partitioning`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_index_partitioning?: (ctx: Alter_index_partitioningContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_index_default_attrs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_index_default_attrs?: (ctx: Modify_index_default_attrsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_hash_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_hash_index_partition?: (ctx: Add_hash_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.coalesce_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCoalesce_index_partition?: (ctx: Coalesce_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_index_partition?: (ctx: Modify_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_index_partitions_ops`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_index_partitions_ops?: (ctx: Modify_index_partitions_opsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rename_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRename_index_partition?: (ctx: Rename_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_index_partition?: (ctx: Drop_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.split_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSplit_index_partition?: (ctx: Split_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_partition_description`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_partition_description?: (ctx: Index_partition_descriptionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_index_subpartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_index_subpartition?: (ctx: Modify_index_subpartitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_name_old`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_name_old?: (ctx: Partition_name_oldContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_partition_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_partition_name?: (ctx: New_partition_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_index_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_index_name?: (ctx: New_index_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_inmemory_join_group`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_inmemory_join_group?: (ctx: Alter_inmemory_join_groupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_user?: (ctx: Create_userContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_user?: (ctx: Alter_userContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_user?: (ctx: Drop_userContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_identified_by`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_identified_by?: (ctx: Alter_identified_byContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identified_by`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentified_by?: (ctx: Identified_byContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identified_other_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentified_other_clause?: (ctx: Identified_other_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_tablespace_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_tablespace_clause?: (ctx: User_tablespace_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quota_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuota_clause?: (ctx: Quota_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.profile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProfile_clause?: (ctx: Profile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.role_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRole_clause?: (ctx: Role_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_default_role_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_default_role_clause?: (ctx: User_default_role_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.password_expire_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPassword_expire_clause?: (ctx: Password_expire_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_lock_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_lock_clause?: (ctx: User_lock_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_editions_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_editions_clause?: (ctx: User_editions_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_user_editions_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_user_editions_clause?: (ctx: Alter_user_editions_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.proxy_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProxy_clause?: (ctx: Proxy_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.container_names`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitContainer_names?: (ctx: Container_namesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_container_data`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_container_data?: (ctx: Set_container_dataContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_rem_container_data`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_rem_container_data?: (ctx: Add_rem_container_dataContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.container_data_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitContainer_data_clause?: (ctx: Container_data_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.administer_key_management`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminister_key_management?: (ctx: Administer_key_managementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.keystore_management_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeystore_management_clauses?: (ctx: Keystore_management_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_keystore?: (ctx: Create_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.open_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOpen_keystore?: (ctx: Open_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.force_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitForce_keystore?: (ctx: Force_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.close_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClose_keystore?: (ctx: Close_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.backup_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBackup_keystore?: (ctx: Backup_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_keystore_password`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_keystore_password?: (ctx: Alter_keystore_passwordContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_into_new_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_into_new_keystore?: (ctx: Merge_into_new_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_into_existing_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_into_existing_keystore?: (ctx: Merge_into_existing_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.isolate_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsolate_keystore?: (ctx: Isolate_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unite_keystore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnite_keystore?: (ctx: Unite_keystoreContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.key_management_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKey_management_clauses?: (ctx: Key_management_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_key`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_key?: (ctx: Set_keyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_key`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_key?: (ctx: Create_keyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mkid`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMkid?: (ctx: MkidContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mk`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMk?: (ctx: MkContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.use_key`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUse_key?: (ctx: Use_keyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_key_tag`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_key_tag?: (ctx: Set_key_tagContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.export_keys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExport_keys?: (ctx: Export_keysContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.import_keys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImport_keys?: (ctx: Import_keysContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.migrate_keys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMigrate_keys?: (ctx: Migrate_keysContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.reverse_migrate_keys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReverse_migrate_keys?: (ctx: Reverse_migrate_keysContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.move_keys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMove_keys?: (ctx: Move_keysContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identified_by_store`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentified_by_store?: (ctx: Identified_by_storeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_algorithm_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_algorithm_clause?: (ctx: Using_algorithm_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_tag_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_tag_clause?: (ctx: Using_tag_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.secret_management_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSecret_management_clauses?: (ctx: Secret_management_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_update_secret`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_update_secret?: (ctx: Add_update_secretContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.delete_secret`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDelete_secret?: (ctx: Delete_secretContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_update_secret_seps`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_update_secret_seps?: (ctx: Add_update_secret_sepsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.delete_secret_seps`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDelete_secret_seps?: (ctx: Delete_secret_sepsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.zero_downtime_software_patching_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitZero_downtime_software_patching_clauses?: (ctx: Zero_downtime_software_patching_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.with_backup_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWith_backup_clause?: (ctx: With_backup_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identified_by_password_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentified_by_password_clause?: (ctx: Identified_by_password_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.keystore_password`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeystore_password?: (ctx: Keystore_passwordContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.path`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPath?: (ctx: PathContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.secret`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSecret?: (ctx: SecretContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.analyze`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyze?: (ctx: AnalyzeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_extention_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_extention_clause?: (ctx: Partition_extention_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.validation_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValidation_clauses?: (ctx: Validation_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compute_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompute_clauses?: (ctx: Compute_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.for_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFor_clause?: (ctx: For_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.online_or_offline`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOnline_or_offline?: (ctx: Online_or_offlineContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.into_clause1`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInto_clause1?: (ctx: Into_clause1Context) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_key_value`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_key_value?: (ctx: Partition_key_valueContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_key_value`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_key_value?: (ctx: Subpartition_key_valueContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.associate_statistics`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssociate_statistics?: (ctx: Associate_statisticsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_association`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_association?: (ctx: Column_associationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_association`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_association?: (ctx: Function_associationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.indextype_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndextype_name?: (ctx: Indextype_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_statistics_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_statistics_type?: (ctx: Using_statistics_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.statistics_type_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatistics_type_name?: (ctx: Statistics_type_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_cost_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_cost_clause?: (ctx: Default_cost_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cpu_cost`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCpu_cost?: (ctx: Cpu_costContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.io_cost`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIo_cost?: (ctx: Io_costContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.network_cost`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNetwork_cost?: (ctx: Network_costContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_selectivity_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_selectivity_clause?: (ctx: Default_selectivity_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_selectivity`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_selectivity?: (ctx: Default_selectivityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.storage_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStorage_table_clause?: (ctx: Storage_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unified_auditing`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnified_auditing?: (ctx: Unified_auditingContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.policy_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPolicy_name?: (ctx: Policy_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_traditional`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_traditional?: (ctx: Audit_traditionalContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_direct_path`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_direct_path?: (ctx: Audit_direct_pathContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_container_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_container_clause?: (ctx: Audit_container_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_operation_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_operation_clause?: (ctx: Audit_operation_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.auditing_by_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuditing_by_clause?: (ctx: Auditing_by_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_user?: (ctx: Audit_userContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.audit_schema_object_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAudit_schema_object_clause?: (ctx: Audit_schema_object_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_operation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_operation?: (ctx: Sql_operationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.auditing_on_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuditing_on_clause?: (ctx: Auditing_on_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_name?: (ctx: Model_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_name?: (ctx: Object_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.profile_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProfile_name?: (ctx: Profile_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_statement_shortcut`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_statement_shortcut?: (ctx: Sql_statement_shortcutContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_index`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_index?: (ctx: Drop_indexContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disassociate_statistics`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisassociate_statistics?: (ctx: Disassociate_statisticsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_indextype`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_indextype?: (ctx: Drop_indextypeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_inmemory_join_group`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_inmemory_join_group?: (ctx: Drop_inmemory_join_groupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_table?: (ctx: Flashback_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.restore_point`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRestore_point?: (ctx: Restore_pointContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.purge_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPurge_statement?: (ctx: Purge_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.noaudit_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNoaudit_statement?: (ctx: Noaudit_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rename_object`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRename_object?: (ctx: Rename_objectContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.grant_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrant_statement?: (ctx: Grant_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.container_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitContainer_clause?: (ctx: Container_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.revoke_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevoke_statement?: (ctx: Revoke_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.revoke_system_privilege`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevoke_system_privilege?: (ctx: Revoke_system_privilegeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.revokee_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokee_clause?: (ctx: Revokee_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.revoke_object_privileges`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevoke_object_privileges?: (ctx: Revoke_object_privilegesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_object_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_object_clause?: (ctx: On_object_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.revoke_roles_from_programs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevoke_roles_from_programs?: (ctx: Revoke_roles_from_programsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.program_unit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram_unit?: (ctx: Program_unitContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_dimension`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_dimension?: (ctx: Create_dimensionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_directory`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_directory?: (ctx: Create_directoryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.directory_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDirectory_name?: (ctx: Directory_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.directory_path`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDirectory_path?: (ctx: Directory_pathContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_inmemory_join_group`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_inmemory_join_group?: (ctx: Create_inmemory_join_groupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_hierarchy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_hierarchy?: (ctx: Drop_hierarchyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_library`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_library?: (ctx: Alter_libraryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_java`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_java?: (ctx: Drop_javaContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_library`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_library?: (ctx: Drop_libraryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_java`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_java?: (ctx: Create_javaContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_library`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_library?: (ctx: Create_libraryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.plsql_library_source`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPlsql_library_source?: (ctx: Plsql_library_sourceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.credential_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCredential_name?: (ctx: Credential_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.library_editionable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLibrary_editionable?: (ctx: Library_editionableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.library_debug`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLibrary_debug?: (ctx: Library_debugContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compiler_parameters_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompiler_parameters_clause?: (ctx: Compiler_parameters_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parameter_value`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParameter_value?: (ctx: Parameter_valueContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.library_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLibrary_name?: (ctx: Library_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_dimension`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_dimension?: (ctx: Alter_dimensionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.level_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevel_clause?: (ctx: Level_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hierarchy_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHierarchy_clause?: (ctx: Hierarchy_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dimension_join_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDimension_join_clause?: (ctx: Dimension_join_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.attribute_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAttribute_clause?: (ctx: Attribute_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.extended_attribute_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtended_attribute_clause?: (ctx: Extended_attribute_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_one_or_more_sub_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_one_or_more_sub_clause?: (ctx: Column_one_or_more_sub_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_view?: (ctx: Alter_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_view_editionable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_view_editionable?: (ctx: Alter_view_editionableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_view?: (ctx: Create_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.editioning_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEditioning_clause?: (ctx: Editioning_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.view_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitView_options?: (ctx: View_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.view_alias_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitView_alias_constraint?: (ctx: View_alias_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_view_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_view_clause?: (ctx: Object_view_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inline_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInline_constraint?: (ctx: Inline_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inline_ref_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInline_ref_constraint?: (ctx: Inline_ref_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.out_of_line_ref_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOut_of_line_ref_constraint?: (ctx: Out_of_line_ref_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.out_of_line_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOut_of_line_constraint?: (ctx: Out_of_line_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constraint_state`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstraint_state?: (ctx: Constraint_stateContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltype_view_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltype_view_clause?: (ctx: Xmltype_view_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_schema_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_schema_spec?: (ctx: Xml_schema_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_schema_url`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_schema_url?: (ctx: Xml_schema_urlContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitElement?: (ctx: ElementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_tablespace?: (ctx: Alter_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datafile_tempfile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatafile_tempfile_clauses?: (ctx: Datafile_tempfile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_logging_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_logging_clauses?: (ctx: Tablespace_logging_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_group_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_group_clause?: (ctx: Tablespace_group_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_group_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_group_name?: (ctx: Tablespace_group_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_state_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_state_clauses?: (ctx: Tablespace_state_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_mode_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_mode_clause?: (ctx: Flashback_mode_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_tablespace_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_tablespace_name?: (ctx: New_tablespace_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_tablespace?: (ctx: Create_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.permanent_tablespace_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPermanent_tablespace_clause?: (ctx: Permanent_tablespace_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_encryption_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_encryption_spec?: (ctx: Tablespace_encryption_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logging_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogging_clause?: (ctx: Logging_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.extent_management_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtent_management_clause?: (ctx: Extent_management_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.segment_management_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSegment_management_clause?: (ctx: Segment_management_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.temporary_tablespace_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTemporary_tablespace_clause?: (ctx: Temporary_tablespace_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.undo_tablespace_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUndo_tablespace_clause?: (ctx: Undo_tablespace_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_retention_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_retention_clause?: (ctx: Tablespace_retention_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_tablespace_set`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_tablespace_set?: (ctx: Create_tablespace_setContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.permanent_tablespace_attrs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPermanent_tablespace_attrs?: (ctx: Permanent_tablespace_attrsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_encryption_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_encryption_clause?: (ctx: Tablespace_encryption_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_tablespace_params`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_tablespace_params?: (ctx: Default_tablespace_paramsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_table_compression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_table_compression?: (ctx: Default_table_compressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.low_high`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLow_high?: (ctx: Low_highContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_index_compression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_index_compression?: (ctx: Default_index_compressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmmemory_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmmemory_clause?: (ctx: Inmmemory_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datafile_specification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatafile_specification?: (ctx: Datafile_specificationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tempfile_specification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTempfile_specification?: (ctx: Tempfile_specificationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datafile_tempfile_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatafile_tempfile_spec?: (ctx: Datafile_tempfile_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.redo_log_file_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRedo_log_file_spec?: (ctx: Redo_log_file_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.autoextend_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAutoextend_clause?: (ctx: Autoextend_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.maxsize_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaxsize_clause?: (ctx: Maxsize_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.build_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBuild_clause?: (ctx: Build_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parallel_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParallel_clause?: (ctx: Parallel_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_materialized_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_materialized_view?: (ctx: Alter_materialized_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_mv_option1`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_mv_option1?: (ctx: Alter_mv_option1Context) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_mv_refresh`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_mv_refresh?: (ctx: Alter_mv_refreshContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rollback_segment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollback_segment?: (ctx: Rollback_segmentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_mv_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_mv_column_clause?: (ctx: Modify_mv_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_materialized_view_log`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_materialized_view_log?: (ctx: Alter_materialized_view_logContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_mv_log_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_mv_log_column_clause?: (ctx: Add_mv_log_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.move_mv_log_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMove_mv_log_clause?: (ctx: Move_mv_log_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mv_log_augmentation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMv_log_augmentation?: (ctx: Mv_log_augmentationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datetime_expr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatetime_expr?: (ctx: Datetime_exprContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.interval_expr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInterval_expr?: (ctx: Interval_exprContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.synchronous_or_asynchronous`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSynchronous_or_asynchronous?: (ctx: Synchronous_or_asynchronousContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.including_or_excluding`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIncluding_or_excluding?: (ctx: Including_or_excludingContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_materialized_view_log`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_materialized_view_log?: (ctx: Create_materialized_view_logContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_values_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_values_clause?: (ctx: New_values_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mv_log_purge_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMv_log_purge_clause?: (ctx: Mv_log_purge_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_materialized_zonemap`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_materialized_zonemap?: (ctx: Create_materialized_zonemapContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_materialized_zonemap`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_materialized_zonemap?: (ctx: Alter_materialized_zonemapContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_materialized_zonemap`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_materialized_zonemap?: (ctx: Drop_materialized_zonemapContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.zonemap_refresh_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitZonemap_refresh_clause?: (ctx: Zonemap_refresh_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.zonemap_attributes`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitZonemap_attributes?: (ctx: Zonemap_attributesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.zonemap_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitZonemap_name?: (ctx: Zonemap_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.operator_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOperator_name?: (ctx: Operator_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.operator_function_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOperator_function_name?: (ctx: Operator_function_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_zonemap_on_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_zonemap_on_table?: (ctx: Create_zonemap_on_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_zonemap_as_subquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_zonemap_as_subquery?: (ctx: Create_zonemap_as_subqueryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_operator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_operator?: (ctx: Alter_operatorContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_operator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_operator?: (ctx: Drop_operatorContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_operator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_operator?: (ctx: Create_operatorContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.binding_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBinding_clause?: (ctx: Binding_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_binding_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_binding_clause?: (ctx: Add_binding_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.implementation_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImplementation_clause?: (ctx: Implementation_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.primary_operator_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrimary_operator_list?: (ctx: Primary_operator_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.primary_operator_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrimary_operator_item?: (ctx: Primary_operator_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.operator_context_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOperator_context_clause?: (ctx: Operator_context_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_function_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_function_clause?: (ctx: Using_function_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_binding_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_binding_clause?: (ctx: Drop_binding_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_materialized_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_materialized_view?: (ctx: Create_materialized_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.scoped_table_ref_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitScoped_table_ref_constraint?: (ctx: Scoped_table_ref_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mv_column_alias`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMv_column_alias?: (ctx: Mv_column_aliasContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_mv_refresh`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_mv_refresh?: (ctx: Create_mv_refreshContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_materialized_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_materialized_view?: (ctx: Drop_materialized_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_context`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_context?: (ctx: Create_contextContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.oracle_namespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOracle_namespace?: (ctx: Oracle_namespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_cluster`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_cluster?: (ctx: Create_clusterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_profile?: (ctx: Create_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.resource_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResource_parameters?: (ctx: Resource_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.password_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPassword_parameters?: (ctx: Password_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_lockdown_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_lockdown_profile?: (ctx: Create_lockdown_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.static_base_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatic_base_profile?: (ctx: Static_base_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dynamic_base_profile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDynamic_base_profile?: (ctx: Dynamic_base_profileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_outline`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_outline?: (ctx: Create_outlineContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_restore_point`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_restore_point?: (ctx: Create_restore_pointContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_role`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_role?: (ctx: Create_roleContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_table?: (ctx: Create_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltype_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltype_table?: (ctx: Xmltype_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltype_virtual_columns`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltype_virtual_columns?: (ctx: Xmltype_virtual_columnsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltype_column_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltype_column_properties?: (ctx: Xmltype_column_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltype_storage`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltype_storage?: (ctx: Xmltype_storageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlschema_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlschema_spec?: (ctx: Xmlschema_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_table?: (ctx: Object_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_type?: (ctx: Object_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.oid_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOid_index_clause?: (ctx: Oid_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.oid_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOid_clause?: (ctx: Oid_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_properties?: (ctx: Object_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_table_substitution`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_table_substitution?: (ctx: Object_table_substitutionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.relational_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelational_table?: (ctx: Relational_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.immutable_table_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImmutable_table_clauses?: (ctx: Immutable_table_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.immutable_table_no_drop_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImmutable_table_no_drop_clause?: (ctx: Immutable_table_no_drop_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.immutable_table_no_delete_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImmutable_table_no_delete_clause?: (ctx: Immutable_table_no_delete_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.blockchain_table_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBlockchain_table_clauses?: (ctx: Blockchain_table_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.blockchain_drop_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBlockchain_drop_table_clause?: (ctx: Blockchain_drop_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.blockchain_row_retention_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBlockchain_row_retention_clause?: (ctx: Blockchain_row_retention_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.blockchain_hash_and_data_format_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBlockchain_hash_and_data_format_clause?: (ctx: Blockchain_hash_and_data_format_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.collation_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollation_name?: (ctx: Collation_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_properties?: (ctx: Table_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.read_only_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRead_only_clause?: (ctx: Read_only_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.indexing_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndexing_clause?: (ctx: Indexing_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.attribute_clustering_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAttribute_clustering_clause?: (ctx: Attribute_clustering_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.clustering_join`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClustering_join?: (ctx: Clustering_joinContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.clustering_join_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClustering_join_item?: (ctx: Clustering_join_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.equijoin_condition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEquijoin_condition?: (ctx: Equijoin_conditionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cluster_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCluster_clause?: (ctx: Cluster_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.clustering_columns`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClustering_columns?: (ctx: Clustering_columnsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.clustering_column_group`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClustering_column_group?: (ctx: Clustering_column_groupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.yes_no`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitYes_no?: (ctx: Yes_noContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.zonemap_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitZonemap_clause?: (ctx: Zonemap_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logical_replication_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogical_replication_clause?: (ctx: Logical_replication_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_name?: (ctx: Table_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.relational_property`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelational_property?: (ctx: Relational_propertyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_partitioning_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_partitioning_clauses?: (ctx: Table_partitioning_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.range_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRange_partitions?: (ctx: Range_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.list_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitList_partitions?: (ctx: List_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hash_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHash_partitions?: (ctx: Hash_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.individual_hash_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndividual_hash_partitions?: (ctx: Individual_hash_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hash_partitions_by_quantity`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHash_partitions_by_quantity?: (ctx: Hash_partitions_by_quantityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hash_partition_quantity`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHash_partition_quantity?: (ctx: Hash_partition_quantityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.composite_range_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComposite_range_partitions?: (ctx: Composite_range_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.composite_list_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComposite_list_partitions?: (ctx: Composite_list_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.composite_hash_partitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComposite_hash_partitions?: (ctx: Composite_hash_partitionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.reference_partitioning`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReference_partitioning?: (ctx: Reference_partitioningContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.reference_partition_desc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReference_partition_desc?: (ctx: Reference_partition_descContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.system_partitioning`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystem_partitioning?: (ctx: System_partitioningContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.range_partition_desc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRange_partition_desc?: (ctx: Range_partition_descContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.list_partition_desc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitList_partition_desc?: (ctx: List_partition_descContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_template`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_template?: (ctx: Subpartition_templateContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hash_subpartition_quantity`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHash_subpartition_quantity?: (ctx: Hash_subpartition_quantityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_by_range`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_by_range?: (ctx: Subpartition_by_rangeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_by_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_by_list?: (ctx: Subpartition_by_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_by_hash`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_by_hash?: (ctx: Subpartition_by_hashContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_name?: (ctx: Subpartition_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.range_subpartition_desc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRange_subpartition_desc?: (ctx: Range_subpartition_descContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.list_subpartition_desc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitList_subpartition_desc?: (ctx: List_subpartition_descContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.individual_hash_subparts`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndividual_hash_subparts?: (ctx: Individual_hash_subpartsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hash_subparts_by_quantity`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHash_subparts_by_quantity?: (ctx: Hash_subparts_by_quantityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.range_values_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRange_values_clause?: (ctx: Range_values_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.list_values_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitList_values_clause?: (ctx: List_values_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_partition_description`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_partition_description?: (ctx: Table_partition_descriptionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partitioning_storage_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitioning_storage_clause?: (ctx: Partitioning_storage_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_partitioning_storage`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_partitioning_storage?: (ctx: Lob_partitioning_storageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datatype_null_enable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatatype_null_enable?: (ctx: Datatype_null_enableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.size_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSize_clause?: (ctx: Size_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_compression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_compression?: (ctx: Table_compressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_table_clause?: (ctx: Inmemory_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_attributes`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_attributes?: (ctx: Inmemory_attributesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_memcompress`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_memcompress?: (ctx: Inmemory_memcompressContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_priority`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_priority?: (ctx: Inmemory_priorityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_distribute`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_distribute?: (ctx: Inmemory_distributeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_duplicate`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_duplicate?: (ctx: Inmemory_duplicateContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.inmemory_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInmemory_column_clause?: (ctx: Inmemory_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.physical_attributes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPhysical_attributes_clause?: (ctx: Physical_attributes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.storage_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStorage_clause?: (ctx: Storage_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.deferred_segment_creation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDeferred_segment_creation?: (ctx: Deferred_segment_creationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.segment_attributes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSegment_attributes_clause?: (ctx: Segment_attributes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.physical_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPhysical_properties?: (ctx: Physical_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_clause?: (ctx: Ilm_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_policy_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_policy_clause?: (ctx: Ilm_policy_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_compression_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_compression_policy?: (ctx: Ilm_compression_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_tiering_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_tiering_policy?: (ctx: Ilm_tiering_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_after_on`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_after_on?: (ctx: Ilm_after_onContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.segment_group`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSegment_group?: (ctx: Segment_groupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_inmemory_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_inmemory_policy?: (ctx: Ilm_inmemory_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ilm_time_period`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIlm_time_period?: (ctx: Ilm_time_periodContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.heap_org_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHeap_org_table_clause?: (ctx: Heap_org_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.external_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExternal_table_clause?: (ctx: External_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.access_driver_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAccess_driver_type?: (ctx: Access_driver_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.external_table_data_props`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExternal_table_data_props?: (ctx: External_table_data_propsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.opaque_format_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOpaque_format_spec?: (ctx: Opaque_format_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.row_movement_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRow_movement_clause?: (ctx: Row_movement_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_archive_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_archive_clause?: (ctx: Flashback_archive_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.log_grp`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLog_grp?: (ctx: Log_grpContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_table_logging`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_table_logging?: (ctx: Supplemental_table_loggingContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_log_grp_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_log_grp_clause?: (ctx: Supplemental_log_grp_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_id_key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_id_key_clause?: (ctx: Supplemental_id_key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.allocate_extent_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAllocate_extent_clause?: (ctx: Allocate_extent_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.deallocate_unused_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDeallocate_unused_clause?: (ctx: Deallocate_unused_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.shrink_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShrink_clause?: (ctx: Shrink_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.records_per_block_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecords_per_block_clause?: (ctx: Records_per_block_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.upgrade_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpgrade_table_clause?: (ctx: Upgrade_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.truncate_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncate_table?: (ctx: Truncate_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_table?: (ctx: Drop_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_tablespace?: (ctx: Drop_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_tablespace_set`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_tablespace_set?: (ctx: Drop_tablespace_setContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.including_contents_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIncluding_contents_clause?: (ctx: Including_contents_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_view?: (ctx: Drop_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.comment_on_column`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComment_on_column?: (ctx: Comment_on_columnContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.enable_or_disable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnable_or_disable?: (ctx: Enable_or_disableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.allow_or_disallow`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAllow_or_disallow?: (ctx: Allow_or_disallowContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_synonym`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_synonym?: (ctx: Alter_synonymContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_synonym`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_synonym?: (ctx: Create_synonymContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_synonym`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_synonym?: (ctx: Drop_synonymContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_spfile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_spfile?: (ctx: Create_spfileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.spfile_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpfile_name?: (ctx: Spfile_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pfile_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPfile_name?: (ctx: Pfile_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.comment_on_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComment_on_table?: (ctx: Comment_on_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.comment_on_materialized`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComment_on_materialized?: (ctx: Comment_on_materializedContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_analytic_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_analytic_view?: (ctx: Alter_analytic_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_add_cache_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_add_cache_clause?: (ctx: Alter_add_cache_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.levels_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLevels_item?: (ctx: Levels_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.measure_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMeasure_list?: (ctx: Measure_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_drop_cache_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_drop_cache_clause?: (ctx: Alter_drop_cache_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_attribute_dimension`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_attribute_dimension?: (ctx: Alter_attribute_dimensionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_audit_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_audit_policy?: (ctx: Alter_audit_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_cluster`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_cluster?: (ctx: Alter_clusterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_analytic_view`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_analytic_view?: (ctx: Drop_analytic_viewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_attribute_dimension`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_attribute_dimension?: (ctx: Drop_attribute_dimensionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_audit_policy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_audit_policy?: (ctx: Drop_audit_policyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_flashback_archive`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_flashback_archive?: (ctx: Drop_flashback_archiveContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_cluster`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_cluster?: (ctx: Drop_clusterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_context`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_context?: (ctx: Drop_contextContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_directory`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_directory?: (ctx: Drop_directoryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_diskgroup`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_diskgroup?: (ctx: Drop_diskgroupContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_edition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_edition?: (ctx: Drop_editionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.truncate_cluster`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncate_cluster?: (ctx: Truncate_clusterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cache_or_nocache`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCache_or_nocache?: (ctx: Cache_or_nocacheContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase_name?: (ctx: Database_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_database`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_database?: (ctx: Alter_databaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase_clause?: (ctx: Database_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.startup_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStartup_clauses?: (ctx: Startup_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.resetlogs_or_noresetlogs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResetlogs_or_noresetlogs?: (ctx: Resetlogs_or_noresetlogsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.upgrade_or_downgrade`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpgrade_or_downgrade?: (ctx: Upgrade_or_downgradeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.recovery_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecovery_clauses?: (ctx: Recovery_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.begin_or_end`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBegin_or_end?: (ctx: Begin_or_endContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.general_recovery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGeneral_recovery?: (ctx: General_recoveryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.full_database_recovery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFull_database_recovery?: (ctx: Full_database_recoveryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partial_database_recovery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartial_database_recovery?: (ctx: Partial_database_recoveryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partial_database_recovery_10g`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartial_database_recovery_10g?: (ctx: Partial_database_recovery_10gContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.managed_standby_recovery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitManaged_standby_recovery?: (ctx: Managed_standby_recoveryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.db_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDb_name?: (ctx: Db_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database_file_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase_file_clauses?: (ctx: Database_file_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_datafile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_datafile_clause?: (ctx: Create_datafile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_datafile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_datafile_clause?: (ctx: Alter_datafile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_tempfile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_tempfile_clause?: (ctx: Alter_tempfile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.move_datafile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMove_datafile_clause?: (ctx: Move_datafile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logfile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogfile_clauses?: (ctx: Logfile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_logfile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_logfile_clauses?: (ctx: Add_logfile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.group_redo_logfile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroup_redo_logfile?: (ctx: Group_redo_logfileContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_logfile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_logfile_clauses?: (ctx: Drop_logfile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.switch_logfile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSwitch_logfile_clause?: (ctx: Switch_logfile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_db_logging`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_db_logging?: (ctx: Supplemental_db_loggingContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_or_drop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_or_drop?: (ctx: Add_or_dropContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_plsql_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_plsql_clause?: (ctx: Supplemental_plsql_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logfile_descriptor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogfile_descriptor?: (ctx: Logfile_descriptorContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.controlfile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitControlfile_clauses?: (ctx: Controlfile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trace_file_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrace_file_clause?: (ctx: Trace_file_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.standby_database_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStandby_database_clauses?: (ctx: Standby_database_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.activate_standby_db_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitActivate_standby_db_clause?: (ctx: Activate_standby_db_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.maximize_standby_db_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaximize_standby_db_clause?: (ctx: Maximize_standby_db_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.register_logfile_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRegister_logfile_clause?: (ctx: Register_logfile_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.commit_switchover_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCommit_switchover_clause?: (ctx: Commit_switchover_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.start_standby_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart_standby_clause?: (ctx: Start_standby_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.stop_standby_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStop_standby_clause?: (ctx: Stop_standby_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.convert_database_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConvert_database_clause?: (ctx: Convert_database_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_settings_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_settings_clause?: (ctx: Default_settings_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_time_zone_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_time_zone_clause?: (ctx: Set_time_zone_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.instance_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInstance_clauses?: (ctx: Instance_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.security_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSecurity_clause?: (ctx: Security_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.domain`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDomain?: (ctx: DomainContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase?: (ctx: DatabaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.edition_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEdition_name?: (ctx: Edition_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.filenumber`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFilenumber?: (ctx: FilenumberContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.filename`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFilename?: (ctx: FilenameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.prepare_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrepare_clause?: (ctx: Prepare_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_mirror_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_mirror_clause?: (ctx: Drop_mirror_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lost_write_protection`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLost_write_protection?: (ctx: Lost_write_protectionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cdb_fleet_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCdb_fleet_clauses?: (ctx: Cdb_fleet_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lead_cdb_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLead_cdb_clause?: (ctx: Lead_cdb_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lead_cdb_uri_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLead_cdb_uri_clause?: (ctx: Lead_cdb_uri_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.property_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperty_clauses?: (ctx: Property_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.replay_upgrade_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReplay_upgrade_clauses?: (ctx: Replay_upgrade_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_database_link`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_database_link?: (ctx: Alter_database_linkContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.password_value`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPassword_value?: (ctx: Password_valueContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.link_authentication`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLink_authentication?: (ctx: Link_authenticationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_database`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_database?: (ctx: Create_databaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database_logging_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase_logging_clauses?: (ctx: Database_logging_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.database_logging_sub_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatabase_logging_sub_clause?: (ctx: Database_logging_sub_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_clauses?: (ctx: Tablespace_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.enable_pluggable_database`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnable_pluggable_database?: (ctx: Enable_pluggable_databaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.file_name_convert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFile_name_convert?: (ctx: File_name_convertContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.filename_convert_sub_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFilename_convert_sub_clause?: (ctx: Filename_convert_sub_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace_datafile_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace_datafile_clauses?: (ctx: Tablespace_datafile_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.undo_mode_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUndo_mode_clause?: (ctx: Undo_mode_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_tablespace?: (ctx: Default_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_temp_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_temp_tablespace?: (ctx: Default_temp_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.undo_tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUndo_tablespace?: (ctx: Undo_tablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_database`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_database?: (ctx: Drop_databaseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.create_database_link`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreate_database_link?: (ctx: Create_database_linkContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dblink`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDblink?: (ctx: DblinkContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_database_link`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_database_link?: (ctx: Drop_database_linkContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_tablespace_set`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_tablespace_set?: (ctx: Alter_tablespace_setContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_tablespace_attrs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_tablespace_attrs?: (ctx: Alter_tablespace_attrsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_tablespace_encryption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_tablespace_encryption?: (ctx: Alter_tablespace_encryptionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ts_file_name_convert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTs_file_name_convert?: (ctx: Ts_file_name_convertContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_role`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_role?: (ctx: Alter_roleContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.role_identified_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRole_identified_clause?: (ctx: Role_identified_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_table`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_table?: (ctx: Alter_tableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.memoptimize_read_write_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMemoptimize_read_write_clause?: (ctx: Memoptimize_read_write_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_table_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_table_properties?: (ctx: Alter_table_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_table_partitioning`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_table_partitioning?: (ctx: Alter_table_partitioningContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_table_partition?: (ctx: Add_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_table_partition?: (ctx: Drop_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_table_partition?: (ctx: Merge_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_table_partition?: (ctx: Modify_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.split_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSplit_table_partition?: (ctx: Split_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.truncate_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncate_table_partition?: (ctx: Truncate_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exchange_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExchange_table_partition?: (ctx: Exchange_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.coalesce_table_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCoalesce_table_partition?: (ctx: Coalesce_table_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_interval_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_interval_partition?: (ctx: Alter_interval_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_extended_names`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_extended_names?: (ctx: Partition_extended_namesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subpartition_extended_names`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubpartition_extended_names?: (ctx: Subpartition_extended_namesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_table_properties_1`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_table_properties_1?: (ctx: Alter_table_properties_1Context) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_iot_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_iot_clauses?: (ctx: Alter_iot_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_mapping_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_mapping_table_clause?: (ctx: Alter_mapping_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_overflow_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_overflow_clause?: (ctx: Alter_overflow_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_overflow_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_overflow_clause?: (ctx: Add_overflow_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_index_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_index_clauses?: (ctx: Update_index_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_global_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_global_index_clause?: (ctx: Update_global_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_all_indexes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_all_indexes_clause?: (ctx: Update_all_indexes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_all_indexes_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_all_indexes_index_clause?: (ctx: Update_all_indexes_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_index_partition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_index_partition?: (ctx: Update_index_partitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_index_subpartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_index_subpartition?: (ctx: Update_index_subpartitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.enable_disable_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnable_disable_clause?: (ctx: Enable_disable_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_index_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_index_clause?: (ctx: Using_index_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_attributes`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_attributes?: (ctx: Index_attributesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sort_or_nosort`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSort_or_nosort?: (ctx: Sort_or_nosortContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exceptions_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExceptions_clause?: (ctx: Exceptions_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.move_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMove_table_clause?: (ctx: Move_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_org_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_org_table_clause?: (ctx: Index_org_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.mapping_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapping_table_clause?: (ctx: Mapping_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.key_compression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKey_compression?: (ctx: Key_compressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_org_overflow_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_org_overflow_clause?: (ctx: Index_org_overflow_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_clauses?: (ctx: Column_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_collection_retrieval`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_collection_retrieval?: (ctx: Modify_collection_retrievalContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.collection_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollection_item?: (ctx: Collection_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rename_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRename_column_clause?: (ctx: Rename_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.old_column_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOld_column_name?: (ctx: Old_column_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_column_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_column_name?: (ctx: New_column_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_modify_drop_column_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_modify_drop_column_clauses?: (ctx: Add_modify_drop_column_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_column_clause?: (ctx: Drop_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_column_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_column_clauses?: (ctx: Modify_column_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_col_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_col_properties?: (ctx: Modify_col_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_col_visibility`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_col_visibility?: (ctx: Modify_col_visibilityContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_col_substitutable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_col_substitutable?: (ctx: Modify_col_substitutableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_column_clause?: (ctx: Add_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.alter_varray_col_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlter_varray_col_properties?: (ctx: Alter_varray_col_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.varray_col_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarray_col_properties?: (ctx: Varray_col_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.varray_storage_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarray_storage_clause?: (ctx: Varray_storage_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_segname`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_segname?: (ctx: Lob_segnameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_item?: (ctx: Lob_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_storage_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_storage_parameters?: (ctx: Lob_storage_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_storage_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_storage_clause?: (ctx: Lob_storage_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_lob_storage_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_lob_storage_clause?: (ctx: Modify_lob_storage_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.modify_lob_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModify_lob_parameters?: (ctx: Modify_lob_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_parameters`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_parameters?: (ctx: Lob_parametersContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_deduplicate_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_deduplicate_clause?: (ctx: Lob_deduplicate_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_compression_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_compression_clause?: (ctx: Lob_compression_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_retention_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_retention_clause?: (ctx: Lob_retention_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.encryption_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEncryption_spec?: (ctx: Encryption_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tablespace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTablespace?: (ctx: TablespaceContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.varray_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarray_item?: (ctx: Varray_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_properties?: (ctx: Column_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lob_partition_storage`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLob_partition_storage?: (ctx: Lob_partition_storageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.period_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPeriod_definition?: (ctx: Period_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.start_time_column`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart_time_column?: (ctx: Start_time_columnContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.end_time_column`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnd_time_column?: (ctx: End_time_columnContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_definition?: (ctx: Column_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_collation_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_collation_name?: (ctx: Column_collation_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identity_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentity_clause?: (ctx: Identity_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identity_options_parentheses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentity_options_parentheses?: (ctx: Identity_options_parenthesesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identity_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentity_options?: (ctx: Identity_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.virtual_column_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVirtual_column_definition?: (ctx: Virtual_column_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.autogenerated_sequence_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAutogenerated_sequence_definition?: (ctx: Autogenerated_sequence_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.evaluation_edition_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEvaluation_edition_clause?: (ctx: Evaluation_edition_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.out_of_line_part_storage`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOut_of_line_part_storage?: (ctx: Out_of_line_part_storageContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.nested_table_col_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNested_table_col_properties?: (ctx: Nested_table_col_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.nested_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNested_item?: (ctx: Nested_itemContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.substitutable_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubstitutable_column_clause?: (ctx: Substitutable_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_name?: (ctx: Partition_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.supplemental_logging_props`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSupplemental_logging_props?: (ctx: Supplemental_logging_propsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_or_attribute`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_or_attribute?: (ctx: Column_or_attributeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_type_col_properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_type_col_properties?: (ctx: Object_type_col_propertiesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constraint_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstraint_clauses?: (ctx: Constraint_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.old_constraint_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOld_constraint_name?: (ctx: Old_constraint_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.new_constraint_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNew_constraint_name?: (ctx: New_constraint_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_constraint_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_constraint_clause?: (ctx: Drop_constraint_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_constraint?: (ctx: Add_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.add_constraint_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdd_constraint_clause?: (ctx: Add_constraint_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.check_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCheck_constraint?: (ctx: Check_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.drop_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDrop_constraint?: (ctx: Drop_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.enable_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnable_constraint?: (ctx: Enable_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.disable_constraint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDisable_constraint?: (ctx: Disable_constraintContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.foreign_key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitForeign_key_clause?: (ctx: Foreign_key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.references_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReferences_clause?: (ctx: References_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.on_delete_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOn_delete_clause?: (ctx: On_delete_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unique_key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnique_key_clause?: (ctx: Unique_key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.primary_key_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrimary_key_clause?: (ctx: Primary_key_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.anonymous_block`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnonymous_block?: (ctx: Anonymous_blockContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.invoker_rights_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInvoker_rights_clause?: (ctx: Invoker_rights_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.call_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCall_spec?: (ctx: Call_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.java_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJava_spec?: (ctx: Java_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.c_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitC_spec?: (ctx: C_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.c_agent_in_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitC_agent_in_clause?: (ctx: C_agent_in_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.c_parameters_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitC_parameters_clause?: (ctx: C_parameters_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parameter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParameter?: (ctx: ParameterContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.default_value_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefault_value_part?: (ctx: Default_value_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.seq_of_declare_specs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSeq_of_declare_specs?: (ctx: Seq_of_declare_specsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.declare_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDeclare_spec?: (ctx: Declare_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.variable_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVariable_declaration?: (ctx: Variable_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subtype_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubtype_declaration?: (ctx: Subtype_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cursor_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor_declaration?: (ctx: Cursor_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parameter_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParameter_spec?: (ctx: Parameter_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exception_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitException_declaration?: (ctx: Exception_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pragma_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPragma_declaration?: (ctx: Pragma_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.record_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecord_type_def?: (ctx: Record_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.field_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitField_spec?: (ctx: Field_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.ref_cursor_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRef_cursor_type_def?: (ctx: Ref_cursor_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_declaration?: (ctx: Type_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_type_def?: (ctx: Table_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_indexed_by_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_indexed_by_part?: (ctx: Table_indexed_by_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.varray_type_def`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarray_type_def?: (ctx: Varray_type_defContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.seq_of_statements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSeq_of_statements?: (ctx: Seq_of_statementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.label_declaration`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLabel_declaration?: (ctx: Label_declarationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatement?: (ctx: StatementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.swallow_to_semi`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSwallow_to_semi?: (ctx: Swallow_to_semiContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.assignment_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssignment_statement?: (ctx: Assignment_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.continue_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitContinue_statement?: (ctx: Continue_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exit_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExit_statement?: (ctx: Exit_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.goto_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGoto_statement?: (ctx: Goto_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.if_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIf_statement?: (ctx: If_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.elsif_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitElsif_part?: (ctx: Elsif_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.else_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitElse_part?: (ctx: Else_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.loop_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoop_statement?: (ctx: Loop_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cursor_loop_param`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor_loop_param?: (ctx: Cursor_loop_paramContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.forall_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitForall_statement?: (ctx: Forall_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.bounds_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBounds_clause?: (ctx: Bounds_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.between_bound`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBetween_bound?: (ctx: Between_boundContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lower_bound`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLower_bound?: (ctx: Lower_boundContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.upper_bound`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpper_bound?: (ctx: Upper_boundContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.null_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNull_statement?: (ctx: Null_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.raise_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRaise_statement?: (ctx: Raise_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.return_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReturn_statement?: (ctx: Return_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.call_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCall_statement?: (ctx: Call_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pipe_row_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPipe_row_statement?: (ctx: Pipe_row_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBody?: (ctx: BodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exception_handler`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitException_handler?: (ctx: Exception_handlerContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trigger_block`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrigger_block?: (ctx: Trigger_blockContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tps_body`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTps_body?: (ctx: Tps_bodyContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.block`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBlock?: (ctx: BlockContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_statement?: (ctx: Sql_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.execute_immediate`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExecute_immediate?: (ctx: Execute_immediateContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dynamic_returning_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDynamic_returning_clause?: (ctx: Dynamic_returning_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.data_manipulation_language_statements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitData_manipulation_language_statements?: (ctx: Data_manipulation_language_statementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cursor_manipulation_statements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor_manipulation_statements?: (ctx: Cursor_manipulation_statementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.close_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClose_statement?: (ctx: Close_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.open_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOpen_statement?: (ctx: Open_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.fetch_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFetch_statement?: (ctx: Fetch_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.open_for_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOpen_for_statement?: (ctx: Open_for_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.transaction_control_statements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTransaction_control_statements?: (ctx: Transaction_control_statementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_transaction_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_transaction_command?: (ctx: Set_transaction_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_constraint_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_constraint_command?: (ctx: Set_constraint_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.commit_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCommit_statement?: (ctx: Commit_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.write_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWrite_clause?: (ctx: Write_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rollback_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollback_statement?: (ctx: Rollback_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.savepoint_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSavepoint_statement?: (ctx: Savepoint_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.explain_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExplain_statement?: (ctx: Explain_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.select_only_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelect_only_statement?: (ctx: Select_only_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.select_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelect_statement?: (ctx: Select_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subquery_factoring_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery_factoring_clause?: (ctx: Subquery_factoring_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.factoring_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFactoring_element?: (ctx: Factoring_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.search_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSearch_clause?: (ctx: Search_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cycle_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCycle_clause?: (ctx: Cycle_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery?: (ctx: SubqueryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subquery_basic_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery_basic_elements?: (ctx: Subquery_basic_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subquery_operation_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery_operation_part?: (ctx: Subquery_operation_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.query_block`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuery_block?: (ctx: Query_blockContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.selected_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelected_list?: (ctx: Selected_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.from_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFrom_clause?: (ctx: From_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.select_list_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelect_list_elements?: (ctx: Select_list_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_ref_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_list?: (ctx: Table_ref_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_ref`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref?: (ctx: Table_refContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_ref_aux`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_aux?: (ctx: Table_ref_auxContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_ref_aux_internal`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_ref_aux_internal?: (ctx: Table_ref_aux_internalContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_clause?: (ctx: Join_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_on_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_on_part?: (ctx: Join_on_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.join_using_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoin_using_part?: (ctx: Join_using_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.outer_join_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOuter_join_type?: (ctx: Outer_join_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.query_partition_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuery_partition_clause?: (ctx: Query_partition_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.flashback_query_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFlashback_query_clause?: (ctx: Flashback_query_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_clause?: (ctx: Pivot_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_element?: (ctx: Pivot_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_for_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_for_clause?: (ctx: Pivot_for_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_in_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_in_clause?: (ctx: Pivot_in_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_in_clause_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_in_clause_element?: (ctx: Pivot_in_clause_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.pivot_in_clause_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPivot_in_clause_elements?: (ctx: Pivot_in_clause_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unpivot_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnpivot_clause?: (ctx: Unpivot_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unpivot_in_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnpivot_in_clause?: (ctx: Unpivot_in_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unpivot_in_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnpivot_in_elements?: (ctx: Unpivot_in_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.hierarchical_query_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHierarchical_query_clause?: (ctx: Hierarchical_query_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.start_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart_part?: (ctx: Start_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.group_by_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroup_by_clause?: (ctx: Group_by_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.group_by_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroup_by_elements?: (ctx: Group_by_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rollup_cube_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollup_cube_clause?: (ctx: Rollup_cube_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.grouping_sets_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrouping_sets_clause?: (ctx: Grouping_sets_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.grouping_sets_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrouping_sets_elements?: (ctx: Grouping_sets_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.having_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHaving_clause?: (ctx: Having_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_clause?: (ctx: Model_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cell_reference_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCell_reference_options?: (ctx: Cell_reference_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.return_rows_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReturn_rows_clause?: (ctx: Return_rows_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.reference_model`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReference_model?: (ctx: Reference_modelContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.main_model`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMain_model?: (ctx: Main_modelContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_column_clauses`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_column_clauses?: (ctx: Model_column_clausesContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_column_partition_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_column_partition_part?: (ctx: Model_column_partition_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_column_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_column_list?: (ctx: Model_column_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_column`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_column?: (ctx: Model_columnContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_rules_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_rules_clause?: (ctx: Model_rules_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_rules_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_rules_part?: (ctx: Model_rules_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_rules_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_rules_element?: (ctx: Model_rules_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cell_assignment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCell_assignment?: (ctx: Cell_assignmentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_iterate_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_iterate_clause?: (ctx: Model_iterate_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.until_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUntil_part?: (ctx: Until_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.order_by_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOrder_by_clause?: (ctx: Order_by_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.order_by_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOrder_by_elements?: (ctx: Order_by_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.offset_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOffset_clause?: (ctx: Offset_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.fetch_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFetch_clause?: (ctx: Fetch_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.for_update_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFor_update_clause?: (ctx: For_update_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.for_update_of_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFor_update_of_part?: (ctx: For_update_of_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.for_update_options`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFor_update_options?: (ctx: For_update_optionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_statement?: (ctx: Update_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.update_set_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdate_set_clause?: (ctx: Update_set_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_based_update_set_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_based_update_set_clause?: (ctx: Column_based_update_set_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.delete_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDelete_statement?: (ctx: Delete_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.insert_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInsert_statement?: (ctx: Insert_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.single_table_insert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingle_table_insert?: (ctx: Single_table_insertContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.multi_table_insert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMulti_table_insert?: (ctx: Multi_table_insertContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.multi_table_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMulti_table_element?: (ctx: Multi_table_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.conditional_insert_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConditional_insert_clause?: (ctx: Conditional_insert_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.conditional_insert_when_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConditional_insert_when_part?: (ctx: Conditional_insert_when_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.conditional_insert_else_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConditional_insert_else_part?: (ctx: Conditional_insert_else_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.insert_into_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInsert_into_clause?: (ctx: Insert_into_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.values_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValues_clause?: (ctx: Values_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_statement?: (ctx: Merge_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_update_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_update_clause?: (ctx: Merge_update_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_element?: (ctx: Merge_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_update_delete_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_update_delete_part?: (ctx: Merge_update_delete_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.merge_insert_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMerge_insert_clause?: (ctx: Merge_insert_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.selected_tableview`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelected_tableview?: (ctx: Selected_tableviewContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lock_table_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_table_statement?: (ctx: Lock_table_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.wait_nowait_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWait_nowait_part?: (ctx: Wait_nowait_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lock_table_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_table_element?: (ctx: Lock_table_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.lock_mode`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_mode?: (ctx: Lock_modeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.general_table_ref`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGeneral_table_ref?: (ctx: General_table_refContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.static_returning_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatic_returning_clause?: (ctx: Static_returning_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.error_logging_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitError_logging_clause?: (ctx: Error_logging_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.error_logging_into_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitError_logging_into_part?: (ctx: Error_logging_into_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.error_logging_reject_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitError_logging_reject_part?: (ctx: Error_logging_reject_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dml_table_expression_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDml_table_expression_clause?: (ctx: Dml_table_expression_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_collection_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_collection_expression?: (ctx: Table_collection_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.subquery_restriction_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery_restriction_clause?: (ctx: Subquery_restriction_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sample_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSample_clause?: (ctx: Sample_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.seed_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSeed_part?: (ctx: Seed_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.condition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCondition?: (ctx: ConditionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_condition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_condition?: (ctx: Json_conditionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.expressions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressions?: (ctx: ExpressionsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpression?: (ctx: ExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cursor_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor_expression?: (ctx: Cursor_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logical_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogical_expression?: (ctx: Logical_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unary_logical_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnary_logical_expression?: (ctx: Unary_logical_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unary_logical_operation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnary_logical_operation?: (ctx: Unary_logical_operationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.logical_operation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogical_operation?: (ctx: Logical_operationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.multiset_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiset_expression?: (ctx: Multiset_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.relational_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelational_expression?: (ctx: Relational_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.compound_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompound_expression?: (ctx: Compound_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.relational_operator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelational_operator?: (ctx: Relational_operatorContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.in_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIn_elements?: (ctx: In_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.between_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBetween_elements?: (ctx: Between_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.concatenation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConcatenation?: (ctx: ConcatenationContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.interval_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInterval_expression?: (ctx: Interval_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_expression?: (ctx: Model_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.model_expression_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModel_expression_element?: (ctx: Model_expression_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.single_column_for_loop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingle_column_for_loop?: (ctx: Single_column_for_loopContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.multi_column_for_loop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMulti_column_for_loop?: (ctx: Multi_column_for_loopContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.unary_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnary_expression?: (ctx: Unary_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.case_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCase_statement?: (ctx: Case_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.simple_case_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimple_case_statement?: (ctx: Simple_case_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.simple_case_when_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimple_case_when_part?: (ctx: Simple_case_when_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.searched_case_statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSearched_case_statement?: (ctx: Searched_case_statementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.searched_case_when_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSearched_case_when_part?: (ctx: Searched_case_when_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.case_else_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCase_else_part?: (ctx: Case_else_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.atom`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAtom?: (ctx: AtomContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quantified_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuantified_expression?: (ctx: Quantified_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.string_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitString_function?: (ctx: String_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.standard_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStandard_function?: (ctx: Standard_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_function?: (ctx: Json_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_object_content`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_object_content?: (ctx: Json_object_contentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_object_entry`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_object_entry?: (ctx: Json_object_entryContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_table_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_table_clause?: (ctx: Json_table_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_array_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_array_element?: (ctx: Json_array_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_on_null_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_on_null_clause?: (ctx: Json_on_null_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_return_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_return_clause?: (ctx: Json_return_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_transform_op`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_transform_op?: (ctx: Json_transform_opContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_column_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_column_clause?: (ctx: Json_column_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_column_definition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_column_definition?: (ctx: Json_column_definitionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_query_returning_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_query_returning_clause?: (ctx: Json_query_returning_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_query_return_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_query_return_type?: (ctx: Json_query_return_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_query_wrapper_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_query_wrapper_clause?: (ctx: Json_query_wrapper_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_query_on_error_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_query_on_error_clause?: (ctx: Json_query_on_error_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_query_on_empty_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_query_on_empty_clause?: (ctx: Json_query_on_empty_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_value_return_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_value_return_clause?: (ctx: Json_value_return_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_value_return_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_value_return_type?: (ctx: Json_value_return_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.json_value_on_mismatch_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJson_value_on_mismatch_clause?: (ctx: Json_value_on_mismatch_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.literal`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLiteral?: (ctx: LiteralContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.numeric_function_wrapper`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumeric_function_wrapper?: (ctx: Numeric_function_wrapperContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.numeric_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumeric_function?: (ctx: Numeric_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.listagg_overflow_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitListagg_overflow_clause?: (ctx: Listagg_overflow_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.other_function`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOther_function?: (ctx: Other_functionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.over_clause_keyword`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOver_clause_keyword?: (ctx: Over_clause_keywordContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.within_or_over_clause_keyword`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithin_or_over_clause_keyword?: (ctx: Within_or_over_clause_keywordContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.standard_prediction_function_keyword`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStandard_prediction_function_keyword?: (ctx: Standard_prediction_function_keywordContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.over_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOver_clause?: (ctx: Over_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.windowing_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowing_clause?: (ctx: Windowing_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.windowing_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowing_type?: (ctx: Windowing_typeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.windowing_elements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowing_elements?: (ctx: Windowing_elementsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_clause?: (ctx: Using_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.using_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUsing_element?: (ctx: Using_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.collect_order_by_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollect_order_by_part?: (ctx: Collect_order_by_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.within_or_over_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithin_or_over_part?: (ctx: Within_or_over_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cost_matrix_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCost_matrix_clause?: (ctx: Cost_matrix_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_passing_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_passing_clause?: (ctx: Xml_passing_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_attributes_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_attributes_clause?: (ctx: Xml_attributes_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_namespaces_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_namespaces_clause?: (ctx: Xml_namespaces_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_table_column`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_table_column?: (ctx: Xml_table_columnContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_general_default_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_general_default_part?: (ctx: Xml_general_default_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_multiuse_expression_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_multiuse_expression_element?: (ctx: Xml_multiuse_expression_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlroot_param_version_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlroot_param_version_part?: (ctx: Xmlroot_param_version_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlroot_param_standalone_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlroot_param_standalone_part?: (ctx: Xmlroot_param_standalone_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlserialize_param_enconding_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlserialize_param_enconding_part?: (ctx: Xmlserialize_param_enconding_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlserialize_param_version_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlserialize_param_version_part?: (ctx: Xmlserialize_param_version_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmlserialize_param_ident_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmlserialize_param_ident_part?: (ctx: Xmlserialize_param_ident_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_plus_command_no_semicolon`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_plus_command_no_semicolon?: (ctx: Sql_plus_command_no_semicolonContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sql_plus_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSql_plus_command?: (ctx: Sql_plus_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.start_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart_command?: (ctx: Start_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.whenever_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWhenever_command?: (ctx: Whenever_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.set_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSet_command?: (ctx: Set_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.timing_command`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTiming_command?: (ctx: Timing_commandContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.partition_extension_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartition_extension_clause?: (ctx: Partition_extension_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_alias`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_alias?: (ctx: Column_aliasContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_alias`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_alias?: (ctx: Table_aliasContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.where_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWhere_clause?: (ctx: Where_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quantitative_where_stmt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuantitative_where_stmt?: (ctx: Quantitative_where_stmtContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.into_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInto_clause?: (ctx: Into_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xml_column_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXml_column_name?: (ctx: Xml_column_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cost_class_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCost_class_name?: (ctx: Cost_class_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.attribute_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAttribute_name?: (ctx: Attribute_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.savepoint_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSavepoint_name?: (ctx: Savepoint_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.rollback_segment_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollback_segment_name?: (ctx: Rollback_segment_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_var_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_var_name?: (ctx: Table_var_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.schema_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSchema_name?: (ctx: Schema_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.routine_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRoutine_name?: (ctx: Routine_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.package_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPackage_name?: (ctx: Package_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.implementation_type_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImplementation_type_name?: (ctx: Implementation_type_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.parameter_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParameter_name?: (ctx: Parameter_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.reference_model_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReference_model_name?: (ctx: Reference_model_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.main_model_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMain_model_name?: (ctx: Main_model_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.container_tableview_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitContainer_tableview_name?: (ctx: Container_tableview_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.aggregate_function_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggregate_function_name?: (ctx: Aggregate_function_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.query_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuery_name?: (ctx: Query_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.grantee_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantee_name?: (ctx: Grantee_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.role_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRole_name?: (ctx: Role_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constraint_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstraint_name?: (ctx: Constraint_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.label_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLabel_name?: (ctx: Label_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_name?: (ctx: Type_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.sequence_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSequence_name?: (ctx: Sequence_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.exception_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitException_name?: (ctx: Exception_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_name?: (ctx: Function_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.procedure_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProcedure_name?: (ctx: Procedure_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.trigger_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTrigger_name?: (ctx: Trigger_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.variable_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVariable_name?: (ctx: Variable_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.index_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndex_name?: (ctx: Index_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.cursor_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor_name?: (ctx: Cursor_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.record_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecord_name?: (ctx: Record_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.collection_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollection_name?: (ctx: Collection_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.link_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLink_name?: (ctx: Link_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_name?: (ctx: Column_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.tableview_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableview_name?: (ctx: Tableview_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.xmltable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitXmltable?: (ctx: XmltableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.char_set_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitChar_set_name?: (ctx: Char_set_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.synonym_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSynonym_name?: (ctx: Synonym_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.schema_object_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSchema_object_name?: (ctx: Schema_object_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.dir_object_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDir_object_name?: (ctx: Dir_object_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.user_object_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser_object_name?: (ctx: User_object_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.grant_object_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrant_object_name?: (ctx: Grant_object_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.column_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumn_list?: (ctx: Column_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.paren_column_list`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParen_column_list?: (ctx: Paren_column_listContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.keep_clause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeep_clause?: (ctx: Keep_clauseContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_argument`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_argument?: (ctx: Function_argumentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_argument_analytic`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_argument_analytic?: (ctx: Function_argument_analyticContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.function_argument_modeling`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunction_argument_modeling?: (ctx: Function_argument_modelingContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.respect_or_ignore_nulls`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRespect_or_ignore_nulls?: (ctx: Respect_or_ignore_nullsContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.argument`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArgument?: (ctx: ArgumentContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.type_spec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType_spec?: (ctx: Type_specContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.datatype`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDatatype?: (ctx: DatatypeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.precision_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrecision_part?: (ctx: Precision_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.native_datatype_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNative_datatype_element?: (ctx: Native_datatype_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.bind_variable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBind_variable?: (ctx: Bind_variableContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.general_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGeneral_element?: (ctx: General_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.general_element_part`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGeneral_element_part?: (ctx: General_element_partContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.table_element`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTable_element?: (ctx: Table_elementContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.object_privilege`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitObject_privilege?: (ctx: Object_privilegeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.system_privilege`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystem_privilege?: (ctx: System_privilegeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.constant`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConstant?: (ctx: ConstantContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.numeric`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumeric?: (ctx: NumericContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.numeric_negative`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumeric_negative?: (ctx: Numeric_negativeContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.quoted_string`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuoted_string?: (ctx: Quoted_stringContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifier?: (ctx: IdentifierContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.id_expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitId_expression?: (ctx: Id_expressionContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.outer_join_sign`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOuter_join_sign?: (ctx: Outer_join_signContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.regular_id`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRegular_id?: (ctx: Regular_idContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.non_reserved_keywords_in_12c`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNon_reserved_keywords_in_12c?: (ctx: Non_reserved_keywords_in_12cContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.non_reserved_keywords_pre12c`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNon_reserved_keywords_pre12c?: (ctx: Non_reserved_keywords_pre12cContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.string_function_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitString_function_name?: (ctx: String_function_nameContext) => Result;
    /**
     * Visit a parse tree produced by `PlSqlParser.numeric_function_name`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumeric_function_name?: (ctx: Numeric_function_nameContext) => Result;
}
