import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
import { RANKING_WINDOWED_FUNCContext } from "./TSqlParser";
import { AGGREGATE_WINDOWED_FUNCContext } from "./TSqlParser";
import { ANALYTIC_WINDOWED_FUNCContext } from "./TSqlParser";
import { BUILT_IN_FUNCContext } from "./TSqlParser";
import { SCALAR_FUNCTIONContext } from "./TSqlParser";
import { FREE_TEXTContext } from "./TSqlParser";
import { PARTITION_FUNCContext } from "./TSqlParser";
import { HIERARCHYID_METHODContext } from "./TSqlParser";
import { APP_NAMEContext } from "./TSqlParser";
import { APPLOCK_MODEContext } from "./TSqlParser";
import { APPLOCK_TESTContext } from "./TSqlParser";
import { ASSEMBLYPROPERTYContext } from "./TSqlParser";
import { COL_LENGTHContext } from "./TSqlParser";
import { COL_NAMEContext } from "./TSqlParser";
import { COLUMNPROPERTYContext } from "./TSqlParser";
import { DATABASEPROPERTYEXContext } from "./TSqlParser";
import { DB_IDContext } from "./TSqlParser";
import { DB_NAMEContext } from "./TSqlParser";
import { FILE_IDContext } from "./TSqlParser";
import { FILE_IDEXContext } from "./TSqlParser";
import { FILE_NAMEContext } from "./TSqlParser";
import { FILEGROUP_IDContext } from "./TSqlParser";
import { FILEGROUP_NAMEContext } from "./TSqlParser";
import { FILEGROUPPROPERTYContext } from "./TSqlParser";
import { FILEPROPERTYContext } from "./TSqlParser";
import { FILEPROPERTYEXContext } from "./TSqlParser";
import { FULLTEXTCATALOGPROPERTYContext } from "./TSqlParser";
import { FULLTEXTSERVICEPROPERTYContext } from "./TSqlParser";
import { INDEX_COLContext } from "./TSqlParser";
import { INDEXKEY_PROPERTYContext } from "./TSqlParser";
import { INDEXPROPERTYContext } from "./TSqlParser";
import { NEXT_VALUE_FORContext } from "./TSqlParser";
import { OBJECT_DEFINITIONContext } from "./TSqlParser";
import { OBJECT_IDContext } from "./TSqlParser";
import { OBJECT_NAMEContext } from "./TSqlParser";
import { OBJECT_SCHEMA_NAMEContext } from "./TSqlParser";
import { OBJECTPROPERTYContext } from "./TSqlParser";
import { OBJECTPROPERTYEXContext } from "./TSqlParser";
import { ORIGINAL_DB_NAMEContext } from "./TSqlParser";
import { PARSENAMEContext } from "./TSqlParser";
import { SCHEMA_IDContext } from "./TSqlParser";
import { SCHEMA_NAMEContext } from "./TSqlParser";
import { SCOPE_IDENTITYContext } from "./TSqlParser";
import { SERVERPROPERTYContext } from "./TSqlParser";
import { STATS_DATEContext } from "./TSqlParser";
import { TYPE_IDContext } from "./TSqlParser";
import { TYPE_NAMEContext } from "./TSqlParser";
import { TYPEPROPERTYContext } from "./TSqlParser";
import { ASCIIContext } from "./TSqlParser";
import { CHARContext } from "./TSqlParser";
import { CHARINDEXContext } from "./TSqlParser";
import { CONCATContext } from "./TSqlParser";
import { CONCAT_WSContext } from "./TSqlParser";
import { DIFFERENCEContext } from "./TSqlParser";
import { FORMATContext } from "./TSqlParser";
import { LEFTContext } from "./TSqlParser";
import { LENContext } from "./TSqlParser";
import { LOWERContext } from "./TSqlParser";
import { LTRIMContext } from "./TSqlParser";
import { NCHARContext } from "./TSqlParser";
import { PATINDEXContext } from "./TSqlParser";
import { QUOTENAMEContext } from "./TSqlParser";
import { REPLACEContext } from "./TSqlParser";
import { REPLICATEContext } from "./TSqlParser";
import { REVERSEContext } from "./TSqlParser";
import { RIGHTContext } from "./TSqlParser";
import { RTRIMContext } from "./TSqlParser";
import { SOUNDEXContext } from "./TSqlParser";
import { SPACEContext } from "./TSqlParser";
import { STRContext } from "./TSqlParser";
import { STRINGAGGContext } from "./TSqlParser";
import { STRING_ESCAPEContext } from "./TSqlParser";
import { STUFFContext } from "./TSqlParser";
import { SUBSTRINGContext } from "./TSqlParser";
import { TRANSLATEContext } from "./TSqlParser";
import { TRIMContext } from "./TSqlParser";
import { UNICODEContext } from "./TSqlParser";
import { UPPERContext } from "./TSqlParser";
import { BINARY_CHECKSUMContext } from "./TSqlParser";
import { CHECKSUMContext } from "./TSqlParser";
import { COMPRESSContext } from "./TSqlParser";
import { CONNECTIONPROPERTYContext } from "./TSqlParser";
import { CONTEXT_INFOContext } from "./TSqlParser";
import { CURRENT_REQUEST_IDContext } from "./TSqlParser";
import { CURRENT_TRANSACTION_IDContext } from "./TSqlParser";
import { DECOMPRESSContext } from "./TSqlParser";
import { ERROR_LINEContext } from "./TSqlParser";
import { ERROR_MESSAGEContext } from "./TSqlParser";
import { ERROR_NUMBERContext } from "./TSqlParser";
import { ERROR_PROCEDUREContext } from "./TSqlParser";
import { ERROR_SEVERITYContext } from "./TSqlParser";
import { ERROR_STATEContext } from "./TSqlParser";
import { FORMATMESSAGEContext } from "./TSqlParser";
import { GET_FILESTREAM_TRANSACTION_CONTEXTContext } from "./TSqlParser";
import { GETANSINULLContext } from "./TSqlParser";
import { HOST_IDContext } from "./TSqlParser";
import { HOST_NAMEContext } from "./TSqlParser";
import { ISNULLContext } from "./TSqlParser";
import { ISNUMERICContext } from "./TSqlParser";
import { MIN_ACTIVE_ROWVERSIONContext } from "./TSqlParser";
import { NEWIDContext } from "./TSqlParser";
import { NEWSEQUENTIALIDContext } from "./TSqlParser";
import { ROWCOUNT_BIGContext } from "./TSqlParser";
import { SESSION_CONTEXTContext } from "./TSqlParser";
import { XACT_STATEContext } from "./TSqlParser";
import { CASTContext } from "./TSqlParser";
import { TRY_CASTContext } from "./TSqlParser";
import { CONVERTContext } from "./TSqlParser";
import { COALESCEContext } from "./TSqlParser";
import { CURSOR_ROWSContext } from "./TSqlParser";
import { FETCH_STATUSContext } from "./TSqlParser";
import { CURSOR_STATUSContext } from "./TSqlParser";
import { CERT_IDContext } from "./TSqlParser";
import { DATALENGTHContext } from "./TSqlParser";
import { IDENT_CURRENTContext } from "./TSqlParser";
import { IDENT_INCRContext } from "./TSqlParser";
import { IDENT_SEEDContext } from "./TSqlParser";
import { IDENTITYContext } from "./TSqlParser";
import { SQL_VARIANT_PROPERTYContext } from "./TSqlParser";
import { CURRENT_DATEContext } from "./TSqlParser";
import { CURRENT_TIMESTAMPContext } from "./TSqlParser";
import { CURRENT_TIMEZONEContext } from "./TSqlParser";
import { CURRENT_TIMEZONE_IDContext } from "./TSqlParser";
import { DATE_BUCKETContext } from "./TSqlParser";
import { DATEADDContext } from "./TSqlParser";
import { DATEDIFFContext } from "./TSqlParser";
import { DATEDIFF_BIGContext } from "./TSqlParser";
import { DATEFROMPARTSContext } from "./TSqlParser";
import { DATENAMEContext } from "./TSqlParser";
import { DATEPARTContext } from "./TSqlParser";
import { DATETIME2FROMPARTSContext } from "./TSqlParser";
import { DATETIMEFROMPARTSContext } from "./TSqlParser";
import { DATETIMEOFFSETFROMPARTSContext } from "./TSqlParser";
import { DATETRUNCContext } from "./TSqlParser";
import { DAYContext } from "./TSqlParser";
import { EOMONTHContext } from "./TSqlParser";
import { GETDATEContext } from "./TSqlParser";
import { GETUTCDATEContext } from "./TSqlParser";
import { ISDATEContext } from "./TSqlParser";
import { MONTHContext } from "./TSqlParser";
import { SMALLDATETIMEFROMPARTSContext } from "./TSqlParser";
import { SWITCHOFFSETContext } from "./TSqlParser";
import { SYSDATETIMEContext } from "./TSqlParser";
import { SYSDATETIMEOFFSETContext } from "./TSqlParser";
import { SYSUTCDATETIMEContext } from "./TSqlParser";
import { TIMEFROMPARTSContext } from "./TSqlParser";
import { TODATETIMEOFFSETContext } from "./TSqlParser";
import { YEARContext } from "./TSqlParser";
import { NULLIFContext } from "./TSqlParser";
import { PARSEContext } from "./TSqlParser";
import { XML_DATA_TYPE_FUNCContext } from "./TSqlParser";
import { IIFContext } from "./TSqlParser";
import { ISJSONContext } from "./TSqlParser";
import { JSON_OBJECTContext } from "./TSqlParser";
import { JSON_ARRAYContext } from "./TSqlParser";
import { JSON_VALUEContext } from "./TSqlParser";
import { JSON_QUERYContext } from "./TSqlParser";
import { JSON_MODIFYContext } from "./TSqlParser";
import { JSON_PATH_EXISTSContext } from "./TSqlParser";
import { ABSContext } from "./TSqlParser";
import { ACOSContext } from "./TSqlParser";
import { ASINContext } from "./TSqlParser";
import { ATANContext } from "./TSqlParser";
import { ATN2Context } from "./TSqlParser";
import { CEILINGContext } from "./TSqlParser";
import { COSContext } from "./TSqlParser";
import { COTContext } from "./TSqlParser";
import { DEGREESContext } from "./TSqlParser";
import { EXPContext } from "./TSqlParser";
import { FLOORContext } from "./TSqlParser";
import { LOGContext } from "./TSqlParser";
import { LOG10Context } from "./TSqlParser";
import { PIContext } from "./TSqlParser";
import { POWERContext } from "./TSqlParser";
import { RADIANSContext } from "./TSqlParser";
import { RANDContext } from "./TSqlParser";
import { ROUNDContext } from "./TSqlParser";
import { MATH_SIGNContext } from "./TSqlParser";
import { SINContext } from "./TSqlParser";
import { SQRTContext } from "./TSqlParser";
import { SQUAREContext } from "./TSqlParser";
import { TANContext } from "./TSqlParser";
import { GREATESTContext } from "./TSqlParser";
import { LEASTContext } from "./TSqlParser";
import { CERTENCODEDContext } from "./TSqlParser";
import { CERTPRIVATEKEYContext } from "./TSqlParser";
import { CURRENT_USERContext } from "./TSqlParser";
import { DATABASE_PRINCIPAL_IDContext } from "./TSqlParser";
import { HAS_DBACCESSContext } from "./TSqlParser";
import { HAS_PERMS_BY_NAMEContext } from "./TSqlParser";
import { IS_MEMBERContext } from "./TSqlParser";
import { IS_ROLEMEMBERContext } from "./TSqlParser";
import { IS_SRVROLEMEMBERContext } from "./TSqlParser";
import { LOGINPROPERTYContext } from "./TSqlParser";
import { ORIGINAL_LOGINContext } from "./TSqlParser";
import { PERMISSIONSContext } from "./TSqlParser";
import { PWDENCRYPTContext } from "./TSqlParser";
import { PWDCOMPAREContext } from "./TSqlParser";
import { SESSION_USERContext } from "./TSqlParser";
import { SESSIONPROPERTYContext } from "./TSqlParser";
import { SUSER_IDContext } from "./TSqlParser";
import { SUSER_SNAMEContext } from "./TSqlParser";
import { SUSER_SIDContext } from "./TSqlParser";
import { SYSTEM_USERContext } from "./TSqlParser";
import { USERContext } from "./TSqlParser";
import { USER_IDContext } from "./TSqlParser";
import { USER_NAMEContext } from "./TSqlParser";
import { ProgramContext } from "./TSqlParser";
import { Tsql_fileContext } from "./TSqlParser";
import { BatchContext } from "./TSqlParser";
import { Batch_level_statementContext } from "./TSqlParser";
import { Sql_clausesContext } from "./TSqlParser";
import { Dml_clauseContext } from "./TSqlParser";
import { Ddl_clauseContext } from "./TSqlParser";
import { Backup_statementContext } from "./TSqlParser";
import { Cfl_statementContext } from "./TSqlParser";
import { Block_statementContext } from "./TSqlParser";
import { Break_statementContext } from "./TSqlParser";
import { Continue_statementContext } from "./TSqlParser";
import { Goto_statementContext } from "./TSqlParser";
import { Return_statementContext } from "./TSqlParser";
import { If_statementContext } from "./TSqlParser";
import { Throw_statementContext } from "./TSqlParser";
import { Throw_error_numberContext } from "./TSqlParser";
import { Throw_messageContext } from "./TSqlParser";
import { Throw_stateContext } from "./TSqlParser";
import { Try_catch_statementContext } from "./TSqlParser";
import { Waitfor_statementContext } from "./TSqlParser";
import { While_statementContext } from "./TSqlParser";
import { Print_statementContext } from "./TSqlParser";
import { Raiseerror_statementContext } from "./TSqlParser";
import { Empty_statementContext } from "./TSqlParser";
import { Another_statementContext } from "./TSqlParser";
import { Alter_application_roleContext } from "./TSqlParser";
import { Alter_xml_schema_collectionContext } from "./TSqlParser";
import { Create_application_roleContext } from "./TSqlParser";
import { Drop_aggregateContext } from "./TSqlParser";
import { Drop_application_roleContext } from "./TSqlParser";
import { Alter_assemblyContext } from "./TSqlParser";
import { Alter_assembly_startContext } from "./TSqlParser";
import { Alter_assembly_clauseContext } from "./TSqlParser";
import { Alter_assembly_from_clauseContext } from "./TSqlParser";
import { Alter_assembly_from_clause_startContext } from "./TSqlParser";
import { Alter_assembly_drop_clauseContext } from "./TSqlParser";
import { Alter_assembly_drop_multiple_filesContext } from "./TSqlParser";
import { Alter_assembly_dropContext } from "./TSqlParser";
import { Alter_assembly_add_clauseContext } from "./TSqlParser";
import { Alter_asssembly_add_clause_startContext } from "./TSqlParser";
import { Alter_assembly_client_file_clauseContext } from "./TSqlParser";
import { Alter_assembly_file_nameContext } from "./TSqlParser";
import { Alter_assembly_file_bitsContext } from "./TSqlParser";
import { Alter_assembly_asContext } from "./TSqlParser";
import { Alter_assembly_with_clauseContext } from "./TSqlParser";
import { Alter_assembly_withContext } from "./TSqlParser";
import { Client_assembly_specifierContext } from "./TSqlParser";
import { Assembly_optionContext } from "./TSqlParser";
import { Network_file_shareContext } from "./TSqlParser";
import { Network_computerContext } from "./TSqlParser";
import { Network_file_startContext } from "./TSqlParser";
import { File_pathContext } from "./TSqlParser";
import { File_directory_path_separatorContext } from "./TSqlParser";
import { Local_fileContext } from "./TSqlParser";
import { Local_driveContext } from "./TSqlParser";
import { Multiple_local_filesContext } from "./TSqlParser";
import { Multiple_local_file_startContext } from "./TSqlParser";
import { Create_assemblyContext } from "./TSqlParser";
import { Drop_assemblyContext } from "./TSqlParser";
import { Alter_asymmetric_keyContext } from "./TSqlParser";
import { Alter_asymmetric_key_startContext } from "./TSqlParser";
import { Asymmetric_key_optionContext } from "./TSqlParser";
import { Asymmetric_key_option_startContext } from "./TSqlParser";
import { Asymmetric_key_password_change_optionContext } from "./TSqlParser";
import { Create_asymmetric_keyContext } from "./TSqlParser";
import { Drop_asymmetric_keyContext } from "./TSqlParser";
import { Alter_authorizationContext } from "./TSqlParser";
import { Authorization_granteeContext } from "./TSqlParser";
import { Entity_toContext } from "./TSqlParser";
import { Colon_colonContext } from "./TSqlParser";
import { Alter_authorization_startContext } from "./TSqlParser";
import { Alter_authorization_for_sql_databaseContext } from "./TSqlParser";
import { Alter_authorization_for_azure_dwContext } from "./TSqlParser";
import { Alter_authorization_for_parallel_dwContext } from "./TSqlParser";
import { Class_typeContext } from "./TSqlParser";
import { Class_type_for_sql_databaseContext } from "./TSqlParser";
import { Class_type_for_azure_dwContext } from "./TSqlParser";
import { Class_type_for_parallel_dwContext } from "./TSqlParser";
import { Class_type_for_grantContext } from "./TSqlParser";
import { Drop_availability_groupContext } from "./TSqlParser";
import { Alter_availability_groupContext } from "./TSqlParser";
import { Alter_availability_group_startContext } from "./TSqlParser";
import { Alter_availability_group_optionsContext } from "./TSqlParser";
import { Ip_v4_failoverContext } from "./TSqlParser";
import { Ip_v6_failoverContext } from "./TSqlParser";
import { Create_or_alter_broker_priorityContext } from "./TSqlParser";
import { Drop_broker_priorityContext } from "./TSqlParser";
import { Alter_certificateContext } from "./TSqlParser";
import { Alter_column_encryption_keyContext } from "./TSqlParser";
import { Create_column_encryption_keyContext } from "./TSqlParser";
import { Drop_certificateContext } from "./TSqlParser";
import { Drop_column_encryption_keyContext } from "./TSqlParser";
import { Drop_column_master_keyContext } from "./TSqlParser";
import { Drop_contractContext } from "./TSqlParser";
import { Drop_credentialContext } from "./TSqlParser";
import { Drop_cryptograhic_providerContext } from "./TSqlParser";
import { Drop_databaseContext } from "./TSqlParser";
import { Drop_database_audit_specificationContext } from "./TSqlParser";
import { Drop_database_encryption_keyContext } from "./TSqlParser";
import { Drop_database_scoped_credentialContext } from "./TSqlParser";
import { Drop_defaultContext } from "./TSqlParser";
import { Drop_endpointContext } from "./TSqlParser";
import { Drop_external_data_sourceContext } from "./TSqlParser";
import { Drop_external_file_formatContext } from "./TSqlParser";
import { Drop_external_libraryContext } from "./TSqlParser";
import { Drop_external_resource_poolContext } from "./TSqlParser";
import { Drop_external_tableContext } from "./TSqlParser";
import { Drop_event_notificationsContext } from "./TSqlParser";
import { Drop_event_sessionContext } from "./TSqlParser";
import { Drop_fulltext_catalogContext } from "./TSqlParser";
import { Drop_fulltext_indexContext } from "./TSqlParser";
import { Drop_fulltext_stoplistContext } from "./TSqlParser";
import { Drop_loginContext } from "./TSqlParser";
import { Drop_master_keyContext } from "./TSqlParser";
import { Drop_message_typeContext } from "./TSqlParser";
import { Drop_partition_functionContext } from "./TSqlParser";
import { Drop_partition_schemeContext } from "./TSqlParser";
import { Drop_queueContext } from "./TSqlParser";
import { Drop_remote_service_bindingContext } from "./TSqlParser";
import { Drop_resource_poolContext } from "./TSqlParser";
import { Drop_db_roleContext } from "./TSqlParser";
import { Drop_routeContext } from "./TSqlParser";
import { Drop_ruleContext } from "./TSqlParser";
import { Drop_schemaContext } from "./TSqlParser";
import { Drop_search_property_listContext } from "./TSqlParser";
import { Drop_security_policyContext } from "./TSqlParser";
import { Drop_sequenceContext } from "./TSqlParser";
import { Drop_server_auditContext } from "./TSqlParser";
import { Drop_server_audit_specificationContext } from "./TSqlParser";
import { Drop_server_roleContext } from "./TSqlParser";
import { Drop_serviceContext } from "./TSqlParser";
import { Drop_signatureContext } from "./TSqlParser";
import { Drop_statistics_name_azure_dw_and_pdwContext } from "./TSqlParser";
import { Drop_symmetric_keyContext } from "./TSqlParser";
import { Drop_synonymContext } from "./TSqlParser";
import { Drop_userContext } from "./TSqlParser";
import { Drop_workload_groupContext } from "./TSqlParser";
import { Drop_xml_schema_collectionContext } from "./TSqlParser";
import { Disable_triggerContext } from "./TSqlParser";
import { Enable_triggerContext } from "./TSqlParser";
import { Lock_tableContext } from "./TSqlParser";
import { Truncate_tableContext } from "./TSqlParser";
import { Create_column_master_keyContext } from "./TSqlParser";
import { Alter_credentialContext } from "./TSqlParser";
import { Create_credentialContext } from "./TSqlParser";
import { Alter_cryptographic_providerContext } from "./TSqlParser";
import { Create_cryptographic_providerContext } from "./TSqlParser";
import { Create_endpointContext } from "./TSqlParser";
import { Endpoint_encryption_alogorithm_clauseContext } from "./TSqlParser";
import { Endpoint_authentication_clauseContext } from "./TSqlParser";
import { Endpoint_listener_clauseContext } from "./TSqlParser";
import { Create_event_notificationContext } from "./TSqlParser";
import { Create_or_alter_event_sessionContext } from "./TSqlParser";
import { Event_session_predicate_expressionContext } from "./TSqlParser";
import { Event_session_predicate_factorContext } from "./TSqlParser";
import { Event_session_predicate_leafContext } from "./TSqlParser";
import { Alter_external_data_sourceContext } from "./TSqlParser";
import { Alter_external_libraryContext } from "./TSqlParser";
import { Create_external_libraryContext } from "./TSqlParser";
import { Alter_external_resource_poolContext } from "./TSqlParser";
import { Create_external_resource_poolContext } from "./TSqlParser";
import { Alter_fulltext_catalogContext } from "./TSqlParser";
import { Create_fulltext_catalogContext } from "./TSqlParser";
import { Alter_fulltext_stoplistContext } from "./TSqlParser";
import { Create_fulltext_stoplistContext } from "./TSqlParser";
import { Alter_login_sql_serverContext } from "./TSqlParser";
import { Create_login_sql_serverContext } from "./TSqlParser";
import { Alter_login_azure_sqlContext } from "./TSqlParser";
import { Create_login_azure_sqlContext } from "./TSqlParser";
import { Alter_login_azure_sql_dw_and_pdwContext } from "./TSqlParser";
import { Create_login_pdwContext } from "./TSqlParser";
import { Alter_master_key_sql_serverContext } from "./TSqlParser";
import { Create_master_key_sql_serverContext } from "./TSqlParser";
import { Alter_master_key_azure_sqlContext } from "./TSqlParser";
import { Create_master_key_azure_sqlContext } from "./TSqlParser";
import { Alter_message_typeContext } from "./TSqlParser";
import { Alter_partition_functionContext } from "./TSqlParser";
import { Alter_partition_schemeContext } from "./TSqlParser";
import { Alter_remote_service_bindingContext } from "./TSqlParser";
import { Create_remote_service_bindingContext } from "./TSqlParser";
import { Create_resource_poolContext } from "./TSqlParser";
import { Alter_resource_governorContext } from "./TSqlParser";
import { Alter_database_audit_specificationContext } from "./TSqlParser";
import { Audit_action_spec_groupContext } from "./TSqlParser";
import { Audit_action_specificationContext } from "./TSqlParser";
import { Action_specificationContext } from "./TSqlParser";
import { Audit_class_nameContext } from "./TSqlParser";
import { Audit_securableContext } from "./TSqlParser";
import { Alter_db_roleContext } from "./TSqlParser";
import { Create_database_audit_specificationContext } from "./TSqlParser";
import { Create_db_roleContext } from "./TSqlParser";
import { Create_routeContext } from "./TSqlParser";
import { Create_ruleContext } from "./TSqlParser";
import { Alter_schema_sqlContext } from "./TSqlParser";
import { Create_schemaContext } from "./TSqlParser";
import { Create_schema_azure_sql_dw_and_pdwContext } from "./TSqlParser";
import { Alter_schema_azure_sql_dw_and_pdwContext } from "./TSqlParser";
import { Create_search_property_listContext } from "./TSqlParser";
import { Create_security_policyContext } from "./TSqlParser";
import { Alter_sequenceContext } from "./TSqlParser";
import { Create_sequenceContext } from "./TSqlParser";
import { Alter_server_auditContext } from "./TSqlParser";
import { Create_server_auditContext } from "./TSqlParser";
import { Alter_server_audit_specificationContext } from "./TSqlParser";
import { Create_server_audit_specificationContext } from "./TSqlParser";
import { Alter_server_configurationContext } from "./TSqlParser";
import { Alter_server_roleContext } from "./TSqlParser";
import { Create_server_roleContext } from "./TSqlParser";
import { Alter_server_role_pdwContext } from "./TSqlParser";
import { Alter_serviceContext } from "./TSqlParser";
import { Opt_arg_clauseContext } from "./TSqlParser";
import { Create_serviceContext } from "./TSqlParser";
import { Alter_service_master_keyContext } from "./TSqlParser";
import { Alter_symmetric_keyContext } from "./TSqlParser";
import { Create_synonymContext } from "./TSqlParser";
import { Alter_userContext } from "./TSqlParser";
import { Create_userContext } from "./TSqlParser";
import { Create_user_azure_sql_dwContext } from "./TSqlParser";
import { Alter_user_azure_sqlContext } from "./TSqlParser";
import { Alter_workload_groupContext } from "./TSqlParser";
import { Create_workload_groupContext } from "./TSqlParser";
import { Create_xml_schema_collectionContext } from "./TSqlParser";
import { Create_partition_functionContext } from "./TSqlParser";
import { Create_partition_schemeContext } from "./TSqlParser";
import { Create_queueContext } from "./TSqlParser";
import { Queue_settingsContext } from "./TSqlParser";
import { Alter_queueContext } from "./TSqlParser";
import { Queue_actionContext } from "./TSqlParser";
import { Queue_rebuild_optionsContext } from "./TSqlParser";
import { Create_contractContext } from "./TSqlParser";
import { Conversation_statementContext } from "./TSqlParser";
import { Message_statementContext } from "./TSqlParser";
import { Merge_statementContext } from "./TSqlParser";
import { When_matchesContext } from "./TSqlParser";
import { Merge_matchedContext } from "./TSqlParser";
import { Merge_not_matchedContext } from "./TSqlParser";
import { Delete_statementContext } from "./TSqlParser";
import { Delete_statement_fromContext } from "./TSqlParser";
import { Insert_statementContext } from "./TSqlParser";
import { Insert_statement_valueContext } from "./TSqlParser";
import { Receive_statementContext } from "./TSqlParser";
import { Select_statement_standaloneContext } from "./TSqlParser";
import { Select_statementContext } from "./TSqlParser";
import { TimeContext } from "./TSqlParser";
import { Update_statementContext } from "./TSqlParser";
import { Output_clauseContext } from "./TSqlParser";
import { Output_dml_list_elemContext } from "./TSqlParser";
import { Create_databaseContext } from "./TSqlParser";
import { Create_indexContext } from "./TSqlParser";
import { Create_index_optionsContext } from "./TSqlParser";
import { Relational_index_optionContext } from "./TSqlParser";
import { Alter_indexContext } from "./TSqlParser";
import { Resumable_index_optionsContext } from "./TSqlParser";
import { Resumable_index_optionContext } from "./TSqlParser";
import { Reorganize_partitionContext } from "./TSqlParser";
import { Reorganize_optionsContext } from "./TSqlParser";
import { Reorganize_optionContext } from "./TSqlParser";
import { Set_index_optionsContext } from "./TSqlParser";
import { Set_index_optionContext } from "./TSqlParser";
import { Rebuild_partitionContext } from "./TSqlParser";
import { Rebuild_index_optionsContext } from "./TSqlParser";
import { Rebuild_index_optionContext } from "./TSqlParser";
import { Single_partition_rebuild_index_optionsContext } from "./TSqlParser";
import { Single_partition_rebuild_index_optionContext } from "./TSqlParser";
import { On_partitionsContext } from "./TSqlParser";
import { Create_columnstore_indexContext } from "./TSqlParser";
import { Create_columnstore_index_optionsContext } from "./TSqlParser";
import { Columnstore_index_optionContext } from "./TSqlParser";
import { Create_nonclustered_columnstore_indexContext } from "./TSqlParser";
import { Create_xml_indexContext } from "./TSqlParser";
import { Xml_index_optionsContext } from "./TSqlParser";
import { Xml_index_optionContext } from "./TSqlParser";
import { Create_or_alter_procedureContext } from "./TSqlParser";
import { As_external_nameContext } from "./TSqlParser";
import { Create_or_alter_triggerContext } from "./TSqlParser";
import { Create_or_alter_dml_triggerContext } from "./TSqlParser";
import { Dml_trigger_optionContext } from "./TSqlParser";
import { Dml_trigger_operationContext } from "./TSqlParser";
import { Create_or_alter_ddl_triggerContext } from "./TSqlParser";
import { Ddl_trigger_operationContext } from "./TSqlParser";
import { Create_or_alter_functionContext } from "./TSqlParser";
import { Func_body_returns_selectContext } from "./TSqlParser";
import { Func_body_returns_tableContext } from "./TSqlParser";
import { Func_body_returns_scalarContext } from "./TSqlParser";
import { Procedure_param_default_valueContext } from "./TSqlParser";
import { Procedure_paramContext } from "./TSqlParser";
import { Procedure_optionContext } from "./TSqlParser";
import { Function_optionContext } from "./TSqlParser";
import { Create_statisticsContext } from "./TSqlParser";
import { Update_statisticsContext } from "./TSqlParser";
import { Update_statistics_optionsContext } from "./TSqlParser";
import { Update_statistics_optionContext } from "./TSqlParser";
import { Create_tableContext } from "./TSqlParser";
import { Table_indicesContext } from "./TSqlParser";
import { Table_optionsContext } from "./TSqlParser";
import { Table_optionContext } from "./TSqlParser";
import { Create_table_index_optionsContext } from "./TSqlParser";
import { Create_table_index_optionContext } from "./TSqlParser";
import { Create_viewContext } from "./TSqlParser";
import { View_attributeContext } from "./TSqlParser";
import { Alter_tableContext } from "./TSqlParser";
import { Switch_partitionContext } from "./TSqlParser";
import { Low_priority_lock_waitContext } from "./TSqlParser";
import { Alter_databaseContext } from "./TSqlParser";
import { Add_or_modify_filesContext } from "./TSqlParser";
import { FilespecContext } from "./TSqlParser";
import { Add_or_modify_filegroupsContext } from "./TSqlParser";
import { Filegroup_updatability_optionContext } from "./TSqlParser";
import { Database_optionspecContext } from "./TSqlParser";
import { Auto_optionContext } from "./TSqlParser";
import { Change_tracking_optionContext } from "./TSqlParser";
import { Change_tracking_option_listContext } from "./TSqlParser";
import { Containment_optionContext } from "./TSqlParser";
import { Cursor_optionContext } from "./TSqlParser";
import { Alter_endpointContext } from "./TSqlParser";
import { Database_mirroring_optionContext } from "./TSqlParser";
import { Mirroring_set_optionContext } from "./TSqlParser";
import { Mirroring_partnerContext } from "./TSqlParser";
import { Mirroring_witnessContext } from "./TSqlParser";
import { Witness_partner_equalContext } from "./TSqlParser";
import { Partner_optionContext } from "./TSqlParser";
import { Witness_optionContext } from "./TSqlParser";
import { Witness_serverContext } from "./TSqlParser";
import { Partner_serverContext } from "./TSqlParser";
import { Mirroring_host_port_seperatorContext } from "./TSqlParser";
import { Partner_server_tcp_prefixContext } from "./TSqlParser";
import { Port_numberContext } from "./TSqlParser";
import { HostContext } from "./TSqlParser";
import { Date_correlation_optimization_optionContext } from "./TSqlParser";
import { Db_encryption_optionContext } from "./TSqlParser";
import { Db_state_optionContext } from "./TSqlParser";
import { Db_update_optionContext } from "./TSqlParser";
import { Db_user_access_optionContext } from "./TSqlParser";
import { Delayed_durability_optionContext } from "./TSqlParser";
import { External_access_optionContext } from "./TSqlParser";
import { Hadr_optionsContext } from "./TSqlParser";
import { Mixed_page_allocation_optionContext } from "./TSqlParser";
import { Parameterization_optionContext } from "./TSqlParser";
import { Recovery_optionContext } from "./TSqlParser";
import { Service_broker_optionContext } from "./TSqlParser";
import { Snapshot_optionContext } from "./TSqlParser";
import { Sql_optionContext } from "./TSqlParser";
import { Target_recovery_time_optionContext } from "./TSqlParser";
import { TerminationContext } from "./TSqlParser";
import { Drop_indexContext } from "./TSqlParser";
import { Drop_relational_or_xml_or_spatial_indexContext } from "./TSqlParser";
import { Drop_backward_compatible_indexContext } from "./TSqlParser";
import { Drop_procedureContext } from "./TSqlParser";
import { Drop_triggerContext } from "./TSqlParser";
import { Drop_dml_triggerContext } from "./TSqlParser";
import { Drop_ddl_triggerContext } from "./TSqlParser";
import { Drop_functionContext } from "./TSqlParser";
import { Drop_statisticsContext } from "./TSqlParser";
import { Drop_tableContext } from "./TSqlParser";
import { Drop_viewContext } from "./TSqlParser";
import { Create_typeContext } from "./TSqlParser";
import { Drop_typeContext } from "./TSqlParser";
import { Rowset_function_limitedContext } from "./TSqlParser";
import { OpenqueryContext } from "./TSqlParser";
import { OpendatasourceContext } from "./TSqlParser";
import { Declare_statementContext } from "./TSqlParser";
import { Xml_declarationContext } from "./TSqlParser";
import { Cursor_statementContext } from "./TSqlParser";
import { Backup_databaseContext } from "./TSqlParser";
import { Backup_logContext } from "./TSqlParser";
import { Backup_certificateContext } from "./TSqlParser";
import { Backup_master_keyContext } from "./TSqlParser";
import { Backup_service_master_keyContext } from "./TSqlParser";
import { Kill_statementContext } from "./TSqlParser";
import { Kill_processContext } from "./TSqlParser";
import { Kill_query_notificationContext } from "./TSqlParser";
import { Kill_stats_jobContext } from "./TSqlParser";
import { Execute_statementContext } from "./TSqlParser";
import { Execute_body_batchContext } from "./TSqlParser";
import { Execute_bodyContext } from "./TSqlParser";
import { Execute_statement_argContext } from "./TSqlParser";
import { Execute_statement_arg_namedContext } from "./TSqlParser";
import { Execute_statement_arg_unnamedContext } from "./TSqlParser";
import { Execute_parameterContext } from "./TSqlParser";
import { Execute_var_stringContext } from "./TSqlParser";
import { Security_statementContext } from "./TSqlParser";
import { Principal_idContext } from "./TSqlParser";
import { Create_certificateContext } from "./TSqlParser";
import { Existing_keysContext } from "./TSqlParser";
import { Private_key_optionsContext } from "./TSqlParser";
import { Generate_new_keysContext } from "./TSqlParser";
import { Date_optionsContext } from "./TSqlParser";
import { Open_keyContext } from "./TSqlParser";
import { Close_keyContext } from "./TSqlParser";
import { Create_keyContext } from "./TSqlParser";
import { Key_optionsContext } from "./TSqlParser";
import { AlgorithmContext } from "./TSqlParser";
import { Encryption_mechanismContext } from "./TSqlParser";
import { Decryption_mechanismContext } from "./TSqlParser";
import { Grant_permissionContext } from "./TSqlParser";
import { Set_statementContext } from "./TSqlParser";
import { Transaction_statementContext } from "./TSqlParser";
import { Go_statementContext } from "./TSqlParser";
import { Use_statementContext } from "./TSqlParser";
import { Setuser_statementContext } from "./TSqlParser";
import { Reconfigure_statementContext } from "./TSqlParser";
import { Shutdown_statementContext } from "./TSqlParser";
import { Checkpoint_statementContext } from "./TSqlParser";
import { Dbcc_checkalloc_optionContext } from "./TSqlParser";
import { Dbcc_checkallocContext } from "./TSqlParser";
import { Dbcc_checkcatalogContext } from "./TSqlParser";
import { Dbcc_checkconstraints_optionContext } from "./TSqlParser";
import { Dbcc_checkconstraintsContext } from "./TSqlParser";
import { Dbcc_checkdb_table_optionContext } from "./TSqlParser";
import { Dbcc_checkdbContext } from "./TSqlParser";
import { Dbcc_checkfilegroup_optionContext } from "./TSqlParser";
import { Dbcc_checkfilegroupContext } from "./TSqlParser";
import { Dbcc_checktableContext } from "./TSqlParser";
import { Dbcc_cleantableContext } from "./TSqlParser";
import { Dbcc_clonedatabase_optionContext } from "./TSqlParser";
import { Dbcc_clonedatabaseContext } from "./TSqlParser";
import { Dbcc_pdw_showspaceusedContext } from "./TSqlParser";
import { Dbcc_proccacheContext } from "./TSqlParser";
import { Dbcc_showcontig_optionContext } from "./TSqlParser";
import { Dbcc_showcontigContext } from "./TSqlParser";
import { Dbcc_shrinklogContext } from "./TSqlParser";
import { Dbcc_dbreindexContext } from "./TSqlParser";
import { Dbcc_dll_freeContext } from "./TSqlParser";
import { Dbcc_dropcleanbuffersContext } from "./TSqlParser";
import { Dbcc_clauseContext } from "./TSqlParser";
import { Execute_clauseContext } from "./TSqlParser";
import { Declare_localContext } from "./TSqlParser";
import { Table_type_definitionContext } from "./TSqlParser";
import { Table_type_indicesContext } from "./TSqlParser";
import { Xml_type_definitionContext } from "./TSqlParser";
import { Xml_schema_collectionContext } from "./TSqlParser";
import { Column_def_table_constraintsContext } from "./TSqlParser";
import { Column_def_table_constraintContext } from "./TSqlParser";
import { Column_definitionContext } from "./TSqlParser";
import { Column_definition_elementContext } from "./TSqlParser";
import { Column_modifierContext } from "./TSqlParser";
import { Materialized_column_definitionContext } from "./TSqlParser";
import { Column_constraintContext } from "./TSqlParser";
import { Column_indexContext } from "./TSqlParser";
import { On_partition_or_filegroupContext } from "./TSqlParser";
import { Table_constraintContext } from "./TSqlParser";
import { Connection_nodeContext } from "./TSqlParser";
import { Primary_key_optionsContext } from "./TSqlParser";
import { Foreign_key_optionsContext } from "./TSqlParser";
import { Check_constraintContext } from "./TSqlParser";
import { On_deleteContext } from "./TSqlParser";
import { On_updateContext } from "./TSqlParser";
import { Alter_table_index_optionsContext } from "./TSqlParser";
import { Alter_table_index_optionContext } from "./TSqlParser";
import { Declare_cursorContext } from "./TSqlParser";
import { Declare_set_cursor_commonContext } from "./TSqlParser";
import { Declare_set_cursor_common_partialContext } from "./TSqlParser";
import { Fetch_cursorContext } from "./TSqlParser";
import { Set_specialContext } from "./TSqlParser";
import { Special_listContext } from "./TSqlParser";
import { Constant_LOCAL_IDContext } from "./TSqlParser";
import { ExpressionContext } from "./TSqlParser";
import { ParameterContext } from "./TSqlParser";
import { Time_zoneContext } from "./TSqlParser";
import { Primitive_expressionContext } from "./TSqlParser";
import { Case_expressionContext } from "./TSqlParser";
import { Unary_operator_expressionContext } from "./TSqlParser";
import { Bracket_expressionContext } from "./TSqlParser";
import { SubqueryContext } from "./TSqlParser";
import { With_expressionContext } from "./TSqlParser";
import { Common_table_expressionContext } from "./TSqlParser";
import { Update_elemContext } from "./TSqlParser";
import { Update_elem_mergeContext } from "./TSqlParser";
import { Search_conditionContext } from "./TSqlParser";
import { PredicateContext } from "./TSqlParser";
import { Query_expressionContext } from "./TSqlParser";
import { Sql_unionContext } from "./TSqlParser";
import { Query_specificationContext } from "./TSqlParser";
import { Top_clauseContext } from "./TSqlParser";
import { Top_percentContext } from "./TSqlParser";
import { Top_countContext } from "./TSqlParser";
import { Order_by_clauseContext } from "./TSqlParser";
import { Select_order_by_clauseContext } from "./TSqlParser";
import { For_clauseContext } from "./TSqlParser";
import { Xml_common_directivesContext } from "./TSqlParser";
import { Order_by_expressionContext } from "./TSqlParser";
import { Grouping_sets_itemContext } from "./TSqlParser";
import { Group_by_itemContext } from "./TSqlParser";
import { Option_clauseContext } from "./TSqlParser";
import { OptionContext } from "./TSqlParser";
import { Optimize_for_argContext } from "./TSqlParser";
import { Select_listContext } from "./TSqlParser";
import { Udt_method_argumentsContext } from "./TSqlParser";
import { AsteriskContext } from "./TSqlParser";
import { Udt_elemContext } from "./TSqlParser";
import { Expression_elemContext } from "./TSqlParser";
import { Select_list_elemContext } from "./TSqlParser";
import { Table_sourcesContext } from "./TSqlParser";
import { Non_ansi_joinContext } from "./TSqlParser";
import { Table_sourceContext } from "./TSqlParser";
import { Table_source_itemContext } from "./TSqlParser";
import { Open_xmlContext } from "./TSqlParser";
import { Open_jsonContext } from "./TSqlParser";
import { Json_declarationContext } from "./TSqlParser";
import { Json_column_declarationContext } from "./TSqlParser";
import { Schema_declarationContext } from "./TSqlParser";
import { Column_declarationContext } from "./TSqlParser";
import { Change_tableContext } from "./TSqlParser";
import { Change_table_changesContext } from "./TSqlParser";
import { Change_table_versionContext } from "./TSqlParser";
import { Join_partContext } from "./TSqlParser";
import { Join_onContext } from "./TSqlParser";
import { Cross_joinContext } from "./TSqlParser";
import { Apply_Context } from "./TSqlParser";
import { PivotContext } from "./TSqlParser";
import { UnpivotContext } from "./TSqlParser";
import { Pivot_clauseContext } from "./TSqlParser";
import { Unpivot_clauseContext } from "./TSqlParser";
import { Full_column_name_listContext } from "./TSqlParser";
import { Rowset_functionContext } from "./TSqlParser";
import { Bulk_optionContext } from "./TSqlParser";
import { Derived_tableContext } from "./TSqlParser";
import { Function_callContext } from "./TSqlParser";
import { Partition_functionContext } from "./TSqlParser";
import { Freetext_functionContext } from "./TSqlParser";
import { Freetext_predicateContext } from "./TSqlParser";
import { Json_key_valueContext } from "./TSqlParser";
import { Json_null_clauseContext } from "./TSqlParser";
import { Built_in_functionsContext } from "./TSqlParser";
import { Xml_data_type_methodsContext } from "./TSqlParser";
import { Dateparts_9Context } from "./TSqlParser";
import { Dateparts_12Context } from "./TSqlParser";
import { Dateparts_15Context } from "./TSqlParser";
import { Dateparts_datetruncContext } from "./TSqlParser";
import { Value_methodContext } from "./TSqlParser";
import { Value_callContext } from "./TSqlParser";
import { Query_methodContext } from "./TSqlParser";
import { Query_callContext } from "./TSqlParser";
import { Exist_methodContext } from "./TSqlParser";
import { Exist_callContext } from "./TSqlParser";
import { Modify_methodContext } from "./TSqlParser";
import { Modify_callContext } from "./TSqlParser";
import { Hierarchyid_callContext } from "./TSqlParser";
import { Hierarchyid_static_methodContext } from "./TSqlParser";
import { Nodes_methodContext } from "./TSqlParser";
import { Switch_sectionContext } from "./TSqlParser";
import { Switch_search_condition_sectionContext } from "./TSqlParser";
import { As_column_aliasContext } from "./TSqlParser";
import { As_table_aliasContext } from "./TSqlParser";
import { Table_aliasContext } from "./TSqlParser";
import { With_table_hintsContext } from "./TSqlParser";
import { Deprecated_table_hintContext } from "./TSqlParser";
import { Sybase_legacy_hintsContext } from "./TSqlParser";
import { Sybase_legacy_hintContext } from "./TSqlParser";
import { Table_hintContext } from "./TSqlParser";
import { Index_valueContext } from "./TSqlParser";
import { Column_alias_listContext } from "./TSqlParser";
import { Column_aliasContext } from "./TSqlParser";
import { Table_value_constructorContext } from "./TSqlParser";
import { Expression_list_Context } from "./TSqlParser";
import { Ranking_windowed_functionContext } from "./TSqlParser";
import { Aggregate_windowed_functionContext } from "./TSqlParser";
import { Analytic_windowed_functionContext } from "./TSqlParser";
import { All_distinct_expressionContext } from "./TSqlParser";
import { Over_clauseContext } from "./TSqlParser";
import { Row_or_range_clauseContext } from "./TSqlParser";
import { Window_frame_extentContext } from "./TSqlParser";
import { Window_frame_boundContext } from "./TSqlParser";
import { Window_frame_precedingContext } from "./TSqlParser";
import { Window_frame_followingContext } from "./TSqlParser";
import { Create_database_optionContext } from "./TSqlParser";
import { Database_filestream_optionContext } from "./TSqlParser";
import { Database_file_specContext } from "./TSqlParser";
import { File_groupContext } from "./TSqlParser";
import { File_specContext } from "./TSqlParser";
import { Entity_nameContext } from "./TSqlParser";
import { Entity_name_for_azure_dwContext } from "./TSqlParser";
import { Entity_name_for_parallel_dwContext } from "./TSqlParser";
import { Full_table_nameContext } from "./TSqlParser";
import { Table_nameContext } from "./TSqlParser";
import { Simple_nameContext } from "./TSqlParser";
import { Func_proc_name_schemaContext } from "./TSqlParser";
import { Func_proc_name_database_schemaContext } from "./TSqlParser";
import { Func_proc_name_server_database_schemaContext } from "./TSqlParser";
import { Ddl_objectContext } from "./TSqlParser";
import { Full_column_nameContext } from "./TSqlParser";
import { Column_name_list_with_orderContext } from "./TSqlParser";
import { Insert_column_name_listContext } from "./TSqlParser";
import { Insert_column_idContext } from "./TSqlParser";
import { Column_name_listContext } from "./TSqlParser";
import { Cursor_nameContext } from "./TSqlParser";
import { On_offContext } from "./TSqlParser";
import { ClusteredContext } from "./TSqlParser";
import { Null_notnullContext } from "./TSqlParser";
import { Scalar_function_nameContext } from "./TSqlParser";
import { Begin_conversation_timerContext } from "./TSqlParser";
import { Begin_conversation_dialogContext } from "./TSqlParser";
import { Contract_nameContext } from "./TSqlParser";
import { Service_nameContext } from "./TSqlParser";
import { End_conversationContext } from "./TSqlParser";
import { Waitfor_conversationContext } from "./TSqlParser";
import { Get_conversationContext } from "./TSqlParser";
import { Queue_idContext } from "./TSqlParser";
import { Send_conversationContext } from "./TSqlParser";
import { Data_typeContext } from "./TSqlParser";
import { ConstantContext } from "./TSqlParser";
import { Primitive_constantContext } from "./TSqlParser";
import { KeywordContext } from "./TSqlParser";
import { Id_Context } from "./TSqlParser";
import { Simple_idContext } from "./TSqlParser";
import { Id_or_stringContext } from "./TSqlParser";
import { Comparison_operatorContext } from "./TSqlParser";
import { Assignment_operatorContext } from "./TSqlParser";
import { File_sizeContext } from "./TSqlParser";
/**
 * This interface defines a complete listener for a parse tree produced by
 * `TSqlParser`.
 */
export interface TSqlParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the `RANKING_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterRANKING_WINDOWED_FUNC?: (ctx: RANKING_WINDOWED_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `RANKING_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitRANKING_WINDOWED_FUNC?: (ctx: RANKING_WINDOWED_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `AGGREGATE_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterAGGREGATE_WINDOWED_FUNC?: (ctx: AGGREGATE_WINDOWED_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `AGGREGATE_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitAGGREGATE_WINDOWED_FUNC?: (ctx: AGGREGATE_WINDOWED_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `ANALYTIC_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterANALYTIC_WINDOWED_FUNC?: (ctx: ANALYTIC_WINDOWED_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `ANALYTIC_WINDOWED_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitANALYTIC_WINDOWED_FUNC?: (ctx: ANALYTIC_WINDOWED_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `BUILT_IN_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterBUILT_IN_FUNC?: (ctx: BUILT_IN_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `BUILT_IN_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitBUILT_IN_FUNC?: (ctx: BUILT_IN_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `SCALAR_FUNCTION`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterSCALAR_FUNCTION?: (ctx: SCALAR_FUNCTIONContext) => void;
    /**
     * Exit a parse tree produced by the `SCALAR_FUNCTION`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitSCALAR_FUNCTION?: (ctx: SCALAR_FUNCTIONContext) => void;
    /**
     * Enter a parse tree produced by the `FREE_TEXT`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterFREE_TEXT?: (ctx: FREE_TEXTContext) => void;
    /**
     * Exit a parse tree produced by the `FREE_TEXT`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitFREE_TEXT?: (ctx: FREE_TEXTContext) => void;
    /**
     * Enter a parse tree produced by the `PARTITION_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterPARTITION_FUNC?: (ctx: PARTITION_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `PARTITION_FUNC`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitPARTITION_FUNC?: (ctx: PARTITION_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `HIERARCHYID_METHOD`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterHIERARCHYID_METHOD?: (ctx: HIERARCHYID_METHODContext) => void;
    /**
     * Exit a parse tree produced by the `HIERARCHYID_METHOD`
     * labeled alternative in `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitHIERARCHYID_METHOD?: (ctx: HIERARCHYID_METHODContext) => void;
    /**
     * Enter a parse tree produced by the `APP_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterAPP_NAME?: (ctx: APP_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `APP_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitAPP_NAME?: (ctx: APP_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `APPLOCK_MODE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterAPPLOCK_MODE?: (ctx: APPLOCK_MODEContext) => void;
    /**
     * Exit a parse tree produced by the `APPLOCK_MODE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitAPPLOCK_MODE?: (ctx: APPLOCK_MODEContext) => void;
    /**
     * Enter a parse tree produced by the `APPLOCK_TEST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterAPPLOCK_TEST?: (ctx: APPLOCK_TESTContext) => void;
    /**
     * Exit a parse tree produced by the `APPLOCK_TEST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitAPPLOCK_TEST?: (ctx: APPLOCK_TESTContext) => void;
    /**
     * Enter a parse tree produced by the `ASSEMBLYPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterASSEMBLYPROPERTY?: (ctx: ASSEMBLYPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `ASSEMBLYPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitASSEMBLYPROPERTY?: (ctx: ASSEMBLYPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `COL_LENGTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOL_LENGTH?: (ctx: COL_LENGTHContext) => void;
    /**
     * Exit a parse tree produced by the `COL_LENGTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOL_LENGTH?: (ctx: COL_LENGTHContext) => void;
    /**
     * Enter a parse tree produced by the `COL_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOL_NAME?: (ctx: COL_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `COL_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOL_NAME?: (ctx: COL_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `COLUMNPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOLUMNPROPERTY?: (ctx: COLUMNPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `COLUMNPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOLUMNPROPERTY?: (ctx: COLUMNPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `DATABASEPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATABASEPROPERTYEX?: (ctx: DATABASEPROPERTYEXContext) => void;
    /**
     * Exit a parse tree produced by the `DATABASEPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATABASEPROPERTYEX?: (ctx: DATABASEPROPERTYEXContext) => void;
    /**
     * Enter a parse tree produced by the `DB_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDB_ID?: (ctx: DB_IDContext) => void;
    /**
     * Exit a parse tree produced by the `DB_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDB_ID?: (ctx: DB_IDContext) => void;
    /**
     * Enter a parse tree produced by the `DB_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDB_NAME?: (ctx: DB_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `DB_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDB_NAME?: (ctx: DB_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `FILE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILE_ID?: (ctx: FILE_IDContext) => void;
    /**
     * Exit a parse tree produced by the `FILE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILE_ID?: (ctx: FILE_IDContext) => void;
    /**
     * Enter a parse tree produced by the `FILE_IDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILE_IDEX?: (ctx: FILE_IDEXContext) => void;
    /**
     * Exit a parse tree produced by the `FILE_IDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILE_IDEX?: (ctx: FILE_IDEXContext) => void;
    /**
     * Enter a parse tree produced by the `FILE_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILE_NAME?: (ctx: FILE_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `FILE_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILE_NAME?: (ctx: FILE_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `FILEGROUP_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILEGROUP_ID?: (ctx: FILEGROUP_IDContext) => void;
    /**
     * Exit a parse tree produced by the `FILEGROUP_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILEGROUP_ID?: (ctx: FILEGROUP_IDContext) => void;
    /**
     * Enter a parse tree produced by the `FILEGROUP_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILEGROUP_NAME?: (ctx: FILEGROUP_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `FILEGROUP_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILEGROUP_NAME?: (ctx: FILEGROUP_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `FILEGROUPPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILEGROUPPROPERTY?: (ctx: FILEGROUPPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `FILEGROUPPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILEGROUPPROPERTY?: (ctx: FILEGROUPPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `FILEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILEPROPERTY?: (ctx: FILEPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `FILEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILEPROPERTY?: (ctx: FILEPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `FILEPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFILEPROPERTYEX?: (ctx: FILEPROPERTYEXContext) => void;
    /**
     * Exit a parse tree produced by the `FILEPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFILEPROPERTYEX?: (ctx: FILEPROPERTYEXContext) => void;
    /**
     * Enter a parse tree produced by the `FULLTEXTCATALOGPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFULLTEXTCATALOGPROPERTY?: (ctx: FULLTEXTCATALOGPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `FULLTEXTCATALOGPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFULLTEXTCATALOGPROPERTY?: (ctx: FULLTEXTCATALOGPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `FULLTEXTSERVICEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFULLTEXTSERVICEPROPERTY?: (ctx: FULLTEXTSERVICEPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `FULLTEXTSERVICEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFULLTEXTSERVICEPROPERTY?: (ctx: FULLTEXTSERVICEPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `INDEX_COL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterINDEX_COL?: (ctx: INDEX_COLContext) => void;
    /**
     * Exit a parse tree produced by the `INDEX_COL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitINDEX_COL?: (ctx: INDEX_COLContext) => void;
    /**
     * Enter a parse tree produced by the `INDEXKEY_PROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterINDEXKEY_PROPERTY?: (ctx: INDEXKEY_PROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `INDEXKEY_PROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitINDEXKEY_PROPERTY?: (ctx: INDEXKEY_PROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `INDEXPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterINDEXPROPERTY?: (ctx: INDEXPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `INDEXPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitINDEXPROPERTY?: (ctx: INDEXPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `NEXT_VALUE_FOR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterNEXT_VALUE_FOR?: (ctx: NEXT_VALUE_FORContext) => void;
    /**
     * Exit a parse tree produced by the `NEXT_VALUE_FOR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitNEXT_VALUE_FOR?: (ctx: NEXT_VALUE_FORContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECT_DEFINITION`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECT_DEFINITION?: (ctx: OBJECT_DEFINITIONContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECT_DEFINITION`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECT_DEFINITION?: (ctx: OBJECT_DEFINITIONContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECT_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECT_ID?: (ctx: OBJECT_IDContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECT_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECT_ID?: (ctx: OBJECT_IDContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECT_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECT_NAME?: (ctx: OBJECT_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECT_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECT_NAME?: (ctx: OBJECT_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECT_SCHEMA_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECT_SCHEMA_NAME?: (ctx: OBJECT_SCHEMA_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECT_SCHEMA_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECT_SCHEMA_NAME?: (ctx: OBJECT_SCHEMA_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECTPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECTPROPERTY?: (ctx: OBJECTPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECTPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECTPROPERTY?: (ctx: OBJECTPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `OBJECTPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterOBJECTPROPERTYEX?: (ctx: OBJECTPROPERTYEXContext) => void;
    /**
     * Exit a parse tree produced by the `OBJECTPROPERTYEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitOBJECTPROPERTYEX?: (ctx: OBJECTPROPERTYEXContext) => void;
    /**
     * Enter a parse tree produced by the `ORIGINAL_DB_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterORIGINAL_DB_NAME?: (ctx: ORIGINAL_DB_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `ORIGINAL_DB_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitORIGINAL_DB_NAME?: (ctx: ORIGINAL_DB_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `PARSENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPARSENAME?: (ctx: PARSENAMEContext) => void;
    /**
     * Exit a parse tree produced by the `PARSENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPARSENAME?: (ctx: PARSENAMEContext) => void;
    /**
     * Enter a parse tree produced by the `SCHEMA_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSCHEMA_ID?: (ctx: SCHEMA_IDContext) => void;
    /**
     * Exit a parse tree produced by the `SCHEMA_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSCHEMA_ID?: (ctx: SCHEMA_IDContext) => void;
    /**
     * Enter a parse tree produced by the `SCHEMA_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSCHEMA_NAME?: (ctx: SCHEMA_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `SCHEMA_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSCHEMA_NAME?: (ctx: SCHEMA_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `SCOPE_IDENTITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSCOPE_IDENTITY?: (ctx: SCOPE_IDENTITYContext) => void;
    /**
     * Exit a parse tree produced by the `SCOPE_IDENTITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSCOPE_IDENTITY?: (ctx: SCOPE_IDENTITYContext) => void;
    /**
     * Enter a parse tree produced by the `SERVERPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSERVERPROPERTY?: (ctx: SERVERPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `SERVERPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSERVERPROPERTY?: (ctx: SERVERPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `STATS_DATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSTATS_DATE?: (ctx: STATS_DATEContext) => void;
    /**
     * Exit a parse tree produced by the `STATS_DATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSTATS_DATE?: (ctx: STATS_DATEContext) => void;
    /**
     * Enter a parse tree produced by the `TYPE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTYPE_ID?: (ctx: TYPE_IDContext) => void;
    /**
     * Exit a parse tree produced by the `TYPE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTYPE_ID?: (ctx: TYPE_IDContext) => void;
    /**
     * Enter a parse tree produced by the `TYPE_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTYPE_NAME?: (ctx: TYPE_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `TYPE_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTYPE_NAME?: (ctx: TYPE_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `TYPEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTYPEPROPERTY?: (ctx: TYPEPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `TYPEPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTYPEPROPERTY?: (ctx: TYPEPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `ASCII`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterASCII?: (ctx: ASCIIContext) => void;
    /**
     * Exit a parse tree produced by the `ASCII`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitASCII?: (ctx: ASCIIContext) => void;
    /**
     * Enter a parse tree produced by the `CHAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCHAR?: (ctx: CHARContext) => void;
    /**
     * Exit a parse tree produced by the `CHAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCHAR?: (ctx: CHARContext) => void;
    /**
     * Enter a parse tree produced by the `CHARINDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCHARINDEX?: (ctx: CHARINDEXContext) => void;
    /**
     * Exit a parse tree produced by the `CHARINDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCHARINDEX?: (ctx: CHARINDEXContext) => void;
    /**
     * Enter a parse tree produced by the `CONCAT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCONCAT?: (ctx: CONCATContext) => void;
    /**
     * Exit a parse tree produced by the `CONCAT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCONCAT?: (ctx: CONCATContext) => void;
    /**
     * Enter a parse tree produced by the `CONCAT_WS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCONCAT_WS?: (ctx: CONCAT_WSContext) => void;
    /**
     * Exit a parse tree produced by the `CONCAT_WS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCONCAT_WS?: (ctx: CONCAT_WSContext) => void;
    /**
     * Enter a parse tree produced by the `DIFFERENCE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDIFFERENCE?: (ctx: DIFFERENCEContext) => void;
    /**
     * Exit a parse tree produced by the `DIFFERENCE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDIFFERENCE?: (ctx: DIFFERENCEContext) => void;
    /**
     * Enter a parse tree produced by the `FORMAT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFORMAT?: (ctx: FORMATContext) => void;
    /**
     * Exit a parse tree produced by the `FORMAT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFORMAT?: (ctx: FORMATContext) => void;
    /**
     * Enter a parse tree produced by the `LEFT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLEFT?: (ctx: LEFTContext) => void;
    /**
     * Exit a parse tree produced by the `LEFT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLEFT?: (ctx: LEFTContext) => void;
    /**
     * Enter a parse tree produced by the `LEN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLEN?: (ctx: LENContext) => void;
    /**
     * Exit a parse tree produced by the `LEN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLEN?: (ctx: LENContext) => void;
    /**
     * Enter a parse tree produced by the `LOWER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLOWER?: (ctx: LOWERContext) => void;
    /**
     * Exit a parse tree produced by the `LOWER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLOWER?: (ctx: LOWERContext) => void;
    /**
     * Enter a parse tree produced by the `LTRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLTRIM?: (ctx: LTRIMContext) => void;
    /**
     * Exit a parse tree produced by the `LTRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLTRIM?: (ctx: LTRIMContext) => void;
    /**
     * Enter a parse tree produced by the `NCHAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterNCHAR?: (ctx: NCHARContext) => void;
    /**
     * Exit a parse tree produced by the `NCHAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitNCHAR?: (ctx: NCHARContext) => void;
    /**
     * Enter a parse tree produced by the `PATINDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPATINDEX?: (ctx: PATINDEXContext) => void;
    /**
     * Exit a parse tree produced by the `PATINDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPATINDEX?: (ctx: PATINDEXContext) => void;
    /**
     * Enter a parse tree produced by the `QUOTENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterQUOTENAME?: (ctx: QUOTENAMEContext) => void;
    /**
     * Exit a parse tree produced by the `QUOTENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitQUOTENAME?: (ctx: QUOTENAMEContext) => void;
    /**
     * Enter a parse tree produced by the `REPLACE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterREPLACE?: (ctx: REPLACEContext) => void;
    /**
     * Exit a parse tree produced by the `REPLACE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitREPLACE?: (ctx: REPLACEContext) => void;
    /**
     * Enter a parse tree produced by the `REPLICATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterREPLICATE?: (ctx: REPLICATEContext) => void;
    /**
     * Exit a parse tree produced by the `REPLICATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitREPLICATE?: (ctx: REPLICATEContext) => void;
    /**
     * Enter a parse tree produced by the `REVERSE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterREVERSE?: (ctx: REVERSEContext) => void;
    /**
     * Exit a parse tree produced by the `REVERSE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitREVERSE?: (ctx: REVERSEContext) => void;
    /**
     * Enter a parse tree produced by the `RIGHT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterRIGHT?: (ctx: RIGHTContext) => void;
    /**
     * Exit a parse tree produced by the `RIGHT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitRIGHT?: (ctx: RIGHTContext) => void;
    /**
     * Enter a parse tree produced by the `RTRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterRTRIM?: (ctx: RTRIMContext) => void;
    /**
     * Exit a parse tree produced by the `RTRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitRTRIM?: (ctx: RTRIMContext) => void;
    /**
     * Enter a parse tree produced by the `SOUNDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSOUNDEX?: (ctx: SOUNDEXContext) => void;
    /**
     * Exit a parse tree produced by the `SOUNDEX`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSOUNDEX?: (ctx: SOUNDEXContext) => void;
    /**
     * Enter a parse tree produced by the `SPACE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSPACE?: (ctx: SPACEContext) => void;
    /**
     * Exit a parse tree produced by the `SPACE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSPACE?: (ctx: SPACEContext) => void;
    /**
     * Enter a parse tree produced by the `STR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSTR?: (ctx: STRContext) => void;
    /**
     * Exit a parse tree produced by the `STR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSTR?: (ctx: STRContext) => void;
    /**
     * Enter a parse tree produced by the `STRINGAGG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSTRINGAGG?: (ctx: STRINGAGGContext) => void;
    /**
     * Exit a parse tree produced by the `STRINGAGG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSTRINGAGG?: (ctx: STRINGAGGContext) => void;
    /**
     * Enter a parse tree produced by the `STRING_ESCAPE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSTRING_ESCAPE?: (ctx: STRING_ESCAPEContext) => void;
    /**
     * Exit a parse tree produced by the `STRING_ESCAPE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSTRING_ESCAPE?: (ctx: STRING_ESCAPEContext) => void;
    /**
     * Enter a parse tree produced by the `STUFF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSTUFF?: (ctx: STUFFContext) => void;
    /**
     * Exit a parse tree produced by the `STUFF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSTUFF?: (ctx: STUFFContext) => void;
    /**
     * Enter a parse tree produced by the `SUBSTRING`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSUBSTRING?: (ctx: SUBSTRINGContext) => void;
    /**
     * Exit a parse tree produced by the `SUBSTRING`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSUBSTRING?: (ctx: SUBSTRINGContext) => void;
    /**
     * Enter a parse tree produced by the `TRANSLATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTRANSLATE?: (ctx: TRANSLATEContext) => void;
    /**
     * Exit a parse tree produced by the `TRANSLATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTRANSLATE?: (ctx: TRANSLATEContext) => void;
    /**
     * Enter a parse tree produced by the `TRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTRIM?: (ctx: TRIMContext) => void;
    /**
     * Exit a parse tree produced by the `TRIM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTRIM?: (ctx: TRIMContext) => void;
    /**
     * Enter a parse tree produced by the `UNICODE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterUNICODE?: (ctx: UNICODEContext) => void;
    /**
     * Exit a parse tree produced by the `UNICODE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitUNICODE?: (ctx: UNICODEContext) => void;
    /**
     * Enter a parse tree produced by the `UPPER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterUPPER?: (ctx: UPPERContext) => void;
    /**
     * Exit a parse tree produced by the `UPPER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitUPPER?: (ctx: UPPERContext) => void;
    /**
     * Enter a parse tree produced by the `BINARY_CHECKSUM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterBINARY_CHECKSUM?: (ctx: BINARY_CHECKSUMContext) => void;
    /**
     * Exit a parse tree produced by the `BINARY_CHECKSUM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitBINARY_CHECKSUM?: (ctx: BINARY_CHECKSUMContext) => void;
    /**
     * Enter a parse tree produced by the `CHECKSUM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCHECKSUM?: (ctx: CHECKSUMContext) => void;
    /**
     * Exit a parse tree produced by the `CHECKSUM`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCHECKSUM?: (ctx: CHECKSUMContext) => void;
    /**
     * Enter a parse tree produced by the `COMPRESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOMPRESS?: (ctx: COMPRESSContext) => void;
    /**
     * Exit a parse tree produced by the `COMPRESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOMPRESS?: (ctx: COMPRESSContext) => void;
    /**
     * Enter a parse tree produced by the `CONNECTIONPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCONNECTIONPROPERTY?: (ctx: CONNECTIONPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `CONNECTIONPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCONNECTIONPROPERTY?: (ctx: CONNECTIONPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `CONTEXT_INFO`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCONTEXT_INFO?: (ctx: CONTEXT_INFOContext) => void;
    /**
     * Exit a parse tree produced by the `CONTEXT_INFO`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCONTEXT_INFO?: (ctx: CONTEXT_INFOContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_REQUEST_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_REQUEST_ID?: (ctx: CURRENT_REQUEST_IDContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_REQUEST_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_REQUEST_ID?: (ctx: CURRENT_REQUEST_IDContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_TRANSACTION_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_TRANSACTION_ID?: (ctx: CURRENT_TRANSACTION_IDContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_TRANSACTION_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_TRANSACTION_ID?: (ctx: CURRENT_TRANSACTION_IDContext) => void;
    /**
     * Enter a parse tree produced by the `DECOMPRESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDECOMPRESS?: (ctx: DECOMPRESSContext) => void;
    /**
     * Exit a parse tree produced by the `DECOMPRESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDECOMPRESS?: (ctx: DECOMPRESSContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_LINE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_LINE?: (ctx: ERROR_LINEContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_LINE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_LINE?: (ctx: ERROR_LINEContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_MESSAGE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_MESSAGE?: (ctx: ERROR_MESSAGEContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_MESSAGE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_MESSAGE?: (ctx: ERROR_MESSAGEContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_NUMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_NUMBER?: (ctx: ERROR_NUMBERContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_NUMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_NUMBER?: (ctx: ERROR_NUMBERContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_PROCEDURE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_PROCEDURE?: (ctx: ERROR_PROCEDUREContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_PROCEDURE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_PROCEDURE?: (ctx: ERROR_PROCEDUREContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_SEVERITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_SEVERITY?: (ctx: ERROR_SEVERITYContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_SEVERITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_SEVERITY?: (ctx: ERROR_SEVERITYContext) => void;
    /**
     * Enter a parse tree produced by the `ERROR_STATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterERROR_STATE?: (ctx: ERROR_STATEContext) => void;
    /**
     * Exit a parse tree produced by the `ERROR_STATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitERROR_STATE?: (ctx: ERROR_STATEContext) => void;
    /**
     * Enter a parse tree produced by the `FORMATMESSAGE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFORMATMESSAGE?: (ctx: FORMATMESSAGEContext) => void;
    /**
     * Exit a parse tree produced by the `FORMATMESSAGE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFORMATMESSAGE?: (ctx: FORMATMESSAGEContext) => void;
    /**
     * Enter a parse tree produced by the `GET_FILESTREAM_TRANSACTION_CONTEXT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterGET_FILESTREAM_TRANSACTION_CONTEXT?: (ctx: GET_FILESTREAM_TRANSACTION_CONTEXTContext) => void;
    /**
     * Exit a parse tree produced by the `GET_FILESTREAM_TRANSACTION_CONTEXT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitGET_FILESTREAM_TRANSACTION_CONTEXT?: (ctx: GET_FILESTREAM_TRANSACTION_CONTEXTContext) => void;
    /**
     * Enter a parse tree produced by the `GETANSINULL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterGETANSINULL?: (ctx: GETANSINULLContext) => void;
    /**
     * Exit a parse tree produced by the `GETANSINULL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitGETANSINULL?: (ctx: GETANSINULLContext) => void;
    /**
     * Enter a parse tree produced by the `HOST_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterHOST_ID?: (ctx: HOST_IDContext) => void;
    /**
     * Exit a parse tree produced by the `HOST_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitHOST_ID?: (ctx: HOST_IDContext) => void;
    /**
     * Enter a parse tree produced by the `HOST_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterHOST_NAME?: (ctx: HOST_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `HOST_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitHOST_NAME?: (ctx: HOST_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `ISNULL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterISNULL?: (ctx: ISNULLContext) => void;
    /**
     * Exit a parse tree produced by the `ISNULL`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitISNULL?: (ctx: ISNULLContext) => void;
    /**
     * Enter a parse tree produced by the `ISNUMERIC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterISNUMERIC?: (ctx: ISNUMERICContext) => void;
    /**
     * Exit a parse tree produced by the `ISNUMERIC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitISNUMERIC?: (ctx: ISNUMERICContext) => void;
    /**
     * Enter a parse tree produced by the `MIN_ACTIVE_ROWVERSION`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterMIN_ACTIVE_ROWVERSION?: (ctx: MIN_ACTIVE_ROWVERSIONContext) => void;
    /**
     * Exit a parse tree produced by the `MIN_ACTIVE_ROWVERSION`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitMIN_ACTIVE_ROWVERSION?: (ctx: MIN_ACTIVE_ROWVERSIONContext) => void;
    /**
     * Enter a parse tree produced by the `NEWID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterNEWID?: (ctx: NEWIDContext) => void;
    /**
     * Exit a parse tree produced by the `NEWID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitNEWID?: (ctx: NEWIDContext) => void;
    /**
     * Enter a parse tree produced by the `NEWSEQUENTIALID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterNEWSEQUENTIALID?: (ctx: NEWSEQUENTIALIDContext) => void;
    /**
     * Exit a parse tree produced by the `NEWSEQUENTIALID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitNEWSEQUENTIALID?: (ctx: NEWSEQUENTIALIDContext) => void;
    /**
     * Enter a parse tree produced by the `ROWCOUNT_BIG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterROWCOUNT_BIG?: (ctx: ROWCOUNT_BIGContext) => void;
    /**
     * Exit a parse tree produced by the `ROWCOUNT_BIG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitROWCOUNT_BIG?: (ctx: ROWCOUNT_BIGContext) => void;
    /**
     * Enter a parse tree produced by the `SESSION_CONTEXT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSESSION_CONTEXT?: (ctx: SESSION_CONTEXTContext) => void;
    /**
     * Exit a parse tree produced by the `SESSION_CONTEXT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSESSION_CONTEXT?: (ctx: SESSION_CONTEXTContext) => void;
    /**
     * Enter a parse tree produced by the `XACT_STATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterXACT_STATE?: (ctx: XACT_STATEContext) => void;
    /**
     * Exit a parse tree produced by the `XACT_STATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitXACT_STATE?: (ctx: XACT_STATEContext) => void;
    /**
     * Enter a parse tree produced by the `CAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCAST?: (ctx: CASTContext) => void;
    /**
     * Exit a parse tree produced by the `CAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCAST?: (ctx: CASTContext) => void;
    /**
     * Enter a parse tree produced by the `TRY_CAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTRY_CAST?: (ctx: TRY_CASTContext) => void;
    /**
     * Exit a parse tree produced by the `TRY_CAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTRY_CAST?: (ctx: TRY_CASTContext) => void;
    /**
     * Enter a parse tree produced by the `CONVERT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCONVERT?: (ctx: CONVERTContext) => void;
    /**
     * Exit a parse tree produced by the `CONVERT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCONVERT?: (ctx: CONVERTContext) => void;
    /**
     * Enter a parse tree produced by the `COALESCE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOALESCE?: (ctx: COALESCEContext) => void;
    /**
     * Exit a parse tree produced by the `COALESCE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOALESCE?: (ctx: COALESCEContext) => void;
    /**
     * Enter a parse tree produced by the `CURSOR_ROWS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURSOR_ROWS?: (ctx: CURSOR_ROWSContext) => void;
    /**
     * Exit a parse tree produced by the `CURSOR_ROWS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURSOR_ROWS?: (ctx: CURSOR_ROWSContext) => void;
    /**
     * Enter a parse tree produced by the `FETCH_STATUS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFETCH_STATUS?: (ctx: FETCH_STATUSContext) => void;
    /**
     * Exit a parse tree produced by the `FETCH_STATUS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFETCH_STATUS?: (ctx: FETCH_STATUSContext) => void;
    /**
     * Enter a parse tree produced by the `CURSOR_STATUS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURSOR_STATUS?: (ctx: CURSOR_STATUSContext) => void;
    /**
     * Exit a parse tree produced by the `CURSOR_STATUS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURSOR_STATUS?: (ctx: CURSOR_STATUSContext) => void;
    /**
     * Enter a parse tree produced by the `CERT_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCERT_ID?: (ctx: CERT_IDContext) => void;
    /**
     * Exit a parse tree produced by the `CERT_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCERT_ID?: (ctx: CERT_IDContext) => void;
    /**
     * Enter a parse tree produced by the `DATALENGTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATALENGTH?: (ctx: DATALENGTHContext) => void;
    /**
     * Exit a parse tree produced by the `DATALENGTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATALENGTH?: (ctx: DATALENGTHContext) => void;
    /**
     * Enter a parse tree produced by the `IDENT_CURRENT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIDENT_CURRENT?: (ctx: IDENT_CURRENTContext) => void;
    /**
     * Exit a parse tree produced by the `IDENT_CURRENT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIDENT_CURRENT?: (ctx: IDENT_CURRENTContext) => void;
    /**
     * Enter a parse tree produced by the `IDENT_INCR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIDENT_INCR?: (ctx: IDENT_INCRContext) => void;
    /**
     * Exit a parse tree produced by the `IDENT_INCR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIDENT_INCR?: (ctx: IDENT_INCRContext) => void;
    /**
     * Enter a parse tree produced by the `IDENT_SEED`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIDENT_SEED?: (ctx: IDENT_SEEDContext) => void;
    /**
     * Exit a parse tree produced by the `IDENT_SEED`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIDENT_SEED?: (ctx: IDENT_SEEDContext) => void;
    /**
     * Enter a parse tree produced by the `IDENTITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIDENTITY?: (ctx: IDENTITYContext) => void;
    /**
     * Exit a parse tree produced by the `IDENTITY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIDENTITY?: (ctx: IDENTITYContext) => void;
    /**
     * Enter a parse tree produced by the `SQL_VARIANT_PROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSQL_VARIANT_PROPERTY?: (ctx: SQL_VARIANT_PROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `SQL_VARIANT_PROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSQL_VARIANT_PROPERTY?: (ctx: SQL_VARIANT_PROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_DATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_DATE?: (ctx: CURRENT_DATEContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_DATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_DATE?: (ctx: CURRENT_DATEContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_TIMESTAMP`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_TIMESTAMP?: (ctx: CURRENT_TIMESTAMPContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_TIMESTAMP`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_TIMESTAMP?: (ctx: CURRENT_TIMESTAMPContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_TIMEZONE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_TIMEZONE?: (ctx: CURRENT_TIMEZONEContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_TIMEZONE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_TIMEZONE?: (ctx: CURRENT_TIMEZONEContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_TIMEZONE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_TIMEZONE_ID?: (ctx: CURRENT_TIMEZONE_IDContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_TIMEZONE_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_TIMEZONE_ID?: (ctx: CURRENT_TIMEZONE_IDContext) => void;
    /**
     * Enter a parse tree produced by the `DATE_BUCKET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATE_BUCKET?: (ctx: DATE_BUCKETContext) => void;
    /**
     * Exit a parse tree produced by the `DATE_BUCKET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATE_BUCKET?: (ctx: DATE_BUCKETContext) => void;
    /**
     * Enter a parse tree produced by the `DATEADD`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATEADD?: (ctx: DATEADDContext) => void;
    /**
     * Exit a parse tree produced by the `DATEADD`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATEADD?: (ctx: DATEADDContext) => void;
    /**
     * Enter a parse tree produced by the `DATEDIFF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATEDIFF?: (ctx: DATEDIFFContext) => void;
    /**
     * Exit a parse tree produced by the `DATEDIFF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATEDIFF?: (ctx: DATEDIFFContext) => void;
    /**
     * Enter a parse tree produced by the `DATEDIFF_BIG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATEDIFF_BIG?: (ctx: DATEDIFF_BIGContext) => void;
    /**
     * Exit a parse tree produced by the `DATEDIFF_BIG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATEDIFF_BIG?: (ctx: DATEDIFF_BIGContext) => void;
    /**
     * Enter a parse tree produced by the `DATEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATEFROMPARTS?: (ctx: DATEFROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `DATEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATEFROMPARTS?: (ctx: DATEFROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `DATENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATENAME?: (ctx: DATENAMEContext) => void;
    /**
     * Exit a parse tree produced by the `DATENAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATENAME?: (ctx: DATENAMEContext) => void;
    /**
     * Enter a parse tree produced by the `DATEPART`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATEPART?: (ctx: DATEPARTContext) => void;
    /**
     * Exit a parse tree produced by the `DATEPART`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATEPART?: (ctx: DATEPARTContext) => void;
    /**
     * Enter a parse tree produced by the `DATETIME2FROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATETIME2FROMPARTS?: (ctx: DATETIME2FROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `DATETIME2FROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATETIME2FROMPARTS?: (ctx: DATETIME2FROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `DATETIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATETIMEFROMPARTS?: (ctx: DATETIMEFROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `DATETIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATETIMEFROMPARTS?: (ctx: DATETIMEFROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `DATETIMEOFFSETFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATETIMEOFFSETFROMPARTS?: (ctx: DATETIMEOFFSETFROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `DATETIMEOFFSETFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATETIMEOFFSETFROMPARTS?: (ctx: DATETIMEOFFSETFROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `DATETRUNC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATETRUNC?: (ctx: DATETRUNCContext) => void;
    /**
     * Exit a parse tree produced by the `DATETRUNC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATETRUNC?: (ctx: DATETRUNCContext) => void;
    /**
     * Enter a parse tree produced by the `DAY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDAY?: (ctx: DAYContext) => void;
    /**
     * Exit a parse tree produced by the `DAY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDAY?: (ctx: DAYContext) => void;
    /**
     * Enter a parse tree produced by the `EOMONTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterEOMONTH?: (ctx: EOMONTHContext) => void;
    /**
     * Exit a parse tree produced by the `EOMONTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitEOMONTH?: (ctx: EOMONTHContext) => void;
    /**
     * Enter a parse tree produced by the `GETDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterGETDATE?: (ctx: GETDATEContext) => void;
    /**
     * Exit a parse tree produced by the `GETDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitGETDATE?: (ctx: GETDATEContext) => void;
    /**
     * Enter a parse tree produced by the `GETUTCDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterGETUTCDATE?: (ctx: GETUTCDATEContext) => void;
    /**
     * Exit a parse tree produced by the `GETUTCDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitGETUTCDATE?: (ctx: GETUTCDATEContext) => void;
    /**
     * Enter a parse tree produced by the `ISDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterISDATE?: (ctx: ISDATEContext) => void;
    /**
     * Exit a parse tree produced by the `ISDATE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitISDATE?: (ctx: ISDATEContext) => void;
    /**
     * Enter a parse tree produced by the `MONTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterMONTH?: (ctx: MONTHContext) => void;
    /**
     * Exit a parse tree produced by the `MONTH`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitMONTH?: (ctx: MONTHContext) => void;
    /**
     * Enter a parse tree produced by the `SMALLDATETIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSMALLDATETIMEFROMPARTS?: (ctx: SMALLDATETIMEFROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `SMALLDATETIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSMALLDATETIMEFROMPARTS?: (ctx: SMALLDATETIMEFROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `SWITCHOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSWITCHOFFSET?: (ctx: SWITCHOFFSETContext) => void;
    /**
     * Exit a parse tree produced by the `SWITCHOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSWITCHOFFSET?: (ctx: SWITCHOFFSETContext) => void;
    /**
     * Enter a parse tree produced by the `SYSDATETIME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSYSDATETIME?: (ctx: SYSDATETIMEContext) => void;
    /**
     * Exit a parse tree produced by the `SYSDATETIME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSYSDATETIME?: (ctx: SYSDATETIMEContext) => void;
    /**
     * Enter a parse tree produced by the `SYSDATETIMEOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSYSDATETIMEOFFSET?: (ctx: SYSDATETIMEOFFSETContext) => void;
    /**
     * Exit a parse tree produced by the `SYSDATETIMEOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSYSDATETIMEOFFSET?: (ctx: SYSDATETIMEOFFSETContext) => void;
    /**
     * Enter a parse tree produced by the `SYSUTCDATETIME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSYSUTCDATETIME?: (ctx: SYSUTCDATETIMEContext) => void;
    /**
     * Exit a parse tree produced by the `SYSUTCDATETIME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSYSUTCDATETIME?: (ctx: SYSUTCDATETIMEContext) => void;
    /**
     * Enter a parse tree produced by the `TIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTIMEFROMPARTS?: (ctx: TIMEFROMPARTSContext) => void;
    /**
     * Exit a parse tree produced by the `TIMEFROMPARTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTIMEFROMPARTS?: (ctx: TIMEFROMPARTSContext) => void;
    /**
     * Enter a parse tree produced by the `TODATETIMEOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTODATETIMEOFFSET?: (ctx: TODATETIMEOFFSETContext) => void;
    /**
     * Exit a parse tree produced by the `TODATETIMEOFFSET`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTODATETIMEOFFSET?: (ctx: TODATETIMEOFFSETContext) => void;
    /**
     * Enter a parse tree produced by the `YEAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterYEAR?: (ctx: YEARContext) => void;
    /**
     * Exit a parse tree produced by the `YEAR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitYEAR?: (ctx: YEARContext) => void;
    /**
     * Enter a parse tree produced by the `NULLIF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterNULLIF?: (ctx: NULLIFContext) => void;
    /**
     * Exit a parse tree produced by the `NULLIF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitNULLIF?: (ctx: NULLIFContext) => void;
    /**
     * Enter a parse tree produced by the `PARSE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPARSE?: (ctx: PARSEContext) => void;
    /**
     * Exit a parse tree produced by the `PARSE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPARSE?: (ctx: PARSEContext) => void;
    /**
     * Enter a parse tree produced by the `XML_DATA_TYPE_FUNC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterXML_DATA_TYPE_FUNC?: (ctx: XML_DATA_TYPE_FUNCContext) => void;
    /**
     * Exit a parse tree produced by the `XML_DATA_TYPE_FUNC`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitXML_DATA_TYPE_FUNC?: (ctx: XML_DATA_TYPE_FUNCContext) => void;
    /**
     * Enter a parse tree produced by the `IIF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIIF?: (ctx: IIFContext) => void;
    /**
     * Exit a parse tree produced by the `IIF`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIIF?: (ctx: IIFContext) => void;
    /**
     * Enter a parse tree produced by the `ISJSON`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterISJSON?: (ctx: ISJSONContext) => void;
    /**
     * Exit a parse tree produced by the `ISJSON`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitISJSON?: (ctx: ISJSONContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_OBJECT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_OBJECT?: (ctx: JSON_OBJECTContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_OBJECT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_OBJECT?: (ctx: JSON_OBJECTContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_ARRAY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_ARRAY?: (ctx: JSON_ARRAYContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_ARRAY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_ARRAY?: (ctx: JSON_ARRAYContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_VALUE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_VALUE?: (ctx: JSON_VALUEContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_VALUE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_VALUE?: (ctx: JSON_VALUEContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_QUERY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_QUERY?: (ctx: JSON_QUERYContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_QUERY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_QUERY?: (ctx: JSON_QUERYContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_MODIFY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_MODIFY?: (ctx: JSON_MODIFYContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_MODIFY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_MODIFY?: (ctx: JSON_MODIFYContext) => void;
    /**
     * Enter a parse tree produced by the `JSON_PATH_EXISTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterJSON_PATH_EXISTS?: (ctx: JSON_PATH_EXISTSContext) => void;
    /**
     * Exit a parse tree produced by the `JSON_PATH_EXISTS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitJSON_PATH_EXISTS?: (ctx: JSON_PATH_EXISTSContext) => void;
    /**
     * Enter a parse tree produced by the `ABS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterABS?: (ctx: ABSContext) => void;
    /**
     * Exit a parse tree produced by the `ABS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitABS?: (ctx: ABSContext) => void;
    /**
     * Enter a parse tree produced by the `ACOS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterACOS?: (ctx: ACOSContext) => void;
    /**
     * Exit a parse tree produced by the `ACOS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitACOS?: (ctx: ACOSContext) => void;
    /**
     * Enter a parse tree produced by the `ASIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterASIN?: (ctx: ASINContext) => void;
    /**
     * Exit a parse tree produced by the `ASIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitASIN?: (ctx: ASINContext) => void;
    /**
     * Enter a parse tree produced by the `ATAN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterATAN?: (ctx: ATANContext) => void;
    /**
     * Exit a parse tree produced by the `ATAN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitATAN?: (ctx: ATANContext) => void;
    /**
     * Enter a parse tree produced by the `ATN2`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterATN2?: (ctx: ATN2Context) => void;
    /**
     * Exit a parse tree produced by the `ATN2`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitATN2?: (ctx: ATN2Context) => void;
    /**
     * Enter a parse tree produced by the `CEILING`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCEILING?: (ctx: CEILINGContext) => void;
    /**
     * Exit a parse tree produced by the `CEILING`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCEILING?: (ctx: CEILINGContext) => void;
    /**
     * Enter a parse tree produced by the `COS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOS?: (ctx: COSContext) => void;
    /**
     * Exit a parse tree produced by the `COS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOS?: (ctx: COSContext) => void;
    /**
     * Enter a parse tree produced by the `COT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCOT?: (ctx: COTContext) => void;
    /**
     * Exit a parse tree produced by the `COT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCOT?: (ctx: COTContext) => void;
    /**
     * Enter a parse tree produced by the `DEGREES`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDEGREES?: (ctx: DEGREESContext) => void;
    /**
     * Exit a parse tree produced by the `DEGREES`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDEGREES?: (ctx: DEGREESContext) => void;
    /**
     * Enter a parse tree produced by the `EXP`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterEXP?: (ctx: EXPContext) => void;
    /**
     * Exit a parse tree produced by the `EXP`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitEXP?: (ctx: EXPContext) => void;
    /**
     * Enter a parse tree produced by the `FLOOR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterFLOOR?: (ctx: FLOORContext) => void;
    /**
     * Exit a parse tree produced by the `FLOOR`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitFLOOR?: (ctx: FLOORContext) => void;
    /**
     * Enter a parse tree produced by the `LOG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLOG?: (ctx: LOGContext) => void;
    /**
     * Exit a parse tree produced by the `LOG`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLOG?: (ctx: LOGContext) => void;
    /**
     * Enter a parse tree produced by the `LOG10`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLOG10?: (ctx: LOG10Context) => void;
    /**
     * Exit a parse tree produced by the `LOG10`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLOG10?: (ctx: LOG10Context) => void;
    /**
     * Enter a parse tree produced by the `PI`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPI?: (ctx: PIContext) => void;
    /**
     * Exit a parse tree produced by the `PI`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPI?: (ctx: PIContext) => void;
    /**
     * Enter a parse tree produced by the `POWER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPOWER?: (ctx: POWERContext) => void;
    /**
     * Exit a parse tree produced by the `POWER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPOWER?: (ctx: POWERContext) => void;
    /**
     * Enter a parse tree produced by the `RADIANS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterRADIANS?: (ctx: RADIANSContext) => void;
    /**
     * Exit a parse tree produced by the `RADIANS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitRADIANS?: (ctx: RADIANSContext) => void;
    /**
     * Enter a parse tree produced by the `RAND`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterRAND?: (ctx: RANDContext) => void;
    /**
     * Exit a parse tree produced by the `RAND`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitRAND?: (ctx: RANDContext) => void;
    /**
     * Enter a parse tree produced by the `ROUND`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterROUND?: (ctx: ROUNDContext) => void;
    /**
     * Exit a parse tree produced by the `ROUND`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitROUND?: (ctx: ROUNDContext) => void;
    /**
     * Enter a parse tree produced by the `MATH_SIGN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterMATH_SIGN?: (ctx: MATH_SIGNContext) => void;
    /**
     * Exit a parse tree produced by the `MATH_SIGN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitMATH_SIGN?: (ctx: MATH_SIGNContext) => void;
    /**
     * Enter a parse tree produced by the `SIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSIN?: (ctx: SINContext) => void;
    /**
     * Exit a parse tree produced by the `SIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSIN?: (ctx: SINContext) => void;
    /**
     * Enter a parse tree produced by the `SQRT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSQRT?: (ctx: SQRTContext) => void;
    /**
     * Exit a parse tree produced by the `SQRT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSQRT?: (ctx: SQRTContext) => void;
    /**
     * Enter a parse tree produced by the `SQUARE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSQUARE?: (ctx: SQUAREContext) => void;
    /**
     * Exit a parse tree produced by the `SQUARE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSQUARE?: (ctx: SQUAREContext) => void;
    /**
     * Enter a parse tree produced by the `TAN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterTAN?: (ctx: TANContext) => void;
    /**
     * Exit a parse tree produced by the `TAN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitTAN?: (ctx: TANContext) => void;
    /**
     * Enter a parse tree produced by the `GREATEST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterGREATEST?: (ctx: GREATESTContext) => void;
    /**
     * Exit a parse tree produced by the `GREATEST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitGREATEST?: (ctx: GREATESTContext) => void;
    /**
     * Enter a parse tree produced by the `LEAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLEAST?: (ctx: LEASTContext) => void;
    /**
     * Exit a parse tree produced by the `LEAST`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLEAST?: (ctx: LEASTContext) => void;
    /**
     * Enter a parse tree produced by the `CERTENCODED`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCERTENCODED?: (ctx: CERTENCODEDContext) => void;
    /**
     * Exit a parse tree produced by the `CERTENCODED`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCERTENCODED?: (ctx: CERTENCODEDContext) => void;
    /**
     * Enter a parse tree produced by the `CERTPRIVATEKEY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCERTPRIVATEKEY?: (ctx: CERTPRIVATEKEYContext) => void;
    /**
     * Exit a parse tree produced by the `CERTPRIVATEKEY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCERTPRIVATEKEY?: (ctx: CERTPRIVATEKEYContext) => void;
    /**
     * Enter a parse tree produced by the `CURRENT_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterCURRENT_USER?: (ctx: CURRENT_USERContext) => void;
    /**
     * Exit a parse tree produced by the `CURRENT_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitCURRENT_USER?: (ctx: CURRENT_USERContext) => void;
    /**
     * Enter a parse tree produced by the `DATABASE_PRINCIPAL_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterDATABASE_PRINCIPAL_ID?: (ctx: DATABASE_PRINCIPAL_IDContext) => void;
    /**
     * Exit a parse tree produced by the `DATABASE_PRINCIPAL_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitDATABASE_PRINCIPAL_ID?: (ctx: DATABASE_PRINCIPAL_IDContext) => void;
    /**
     * Enter a parse tree produced by the `HAS_DBACCESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterHAS_DBACCESS?: (ctx: HAS_DBACCESSContext) => void;
    /**
     * Exit a parse tree produced by the `HAS_DBACCESS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitHAS_DBACCESS?: (ctx: HAS_DBACCESSContext) => void;
    /**
     * Enter a parse tree produced by the `HAS_PERMS_BY_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterHAS_PERMS_BY_NAME?: (ctx: HAS_PERMS_BY_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `HAS_PERMS_BY_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitHAS_PERMS_BY_NAME?: (ctx: HAS_PERMS_BY_NAMEContext) => void;
    /**
     * Enter a parse tree produced by the `IS_MEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIS_MEMBER?: (ctx: IS_MEMBERContext) => void;
    /**
     * Exit a parse tree produced by the `IS_MEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIS_MEMBER?: (ctx: IS_MEMBERContext) => void;
    /**
     * Enter a parse tree produced by the `IS_ROLEMEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIS_ROLEMEMBER?: (ctx: IS_ROLEMEMBERContext) => void;
    /**
     * Exit a parse tree produced by the `IS_ROLEMEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIS_ROLEMEMBER?: (ctx: IS_ROLEMEMBERContext) => void;
    /**
     * Enter a parse tree produced by the `IS_SRVROLEMEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterIS_SRVROLEMEMBER?: (ctx: IS_SRVROLEMEMBERContext) => void;
    /**
     * Exit a parse tree produced by the `IS_SRVROLEMEMBER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitIS_SRVROLEMEMBER?: (ctx: IS_SRVROLEMEMBERContext) => void;
    /**
     * Enter a parse tree produced by the `LOGINPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterLOGINPROPERTY?: (ctx: LOGINPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `LOGINPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitLOGINPROPERTY?: (ctx: LOGINPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `ORIGINAL_LOGIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterORIGINAL_LOGIN?: (ctx: ORIGINAL_LOGINContext) => void;
    /**
     * Exit a parse tree produced by the `ORIGINAL_LOGIN`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitORIGINAL_LOGIN?: (ctx: ORIGINAL_LOGINContext) => void;
    /**
     * Enter a parse tree produced by the `PERMISSIONS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPERMISSIONS?: (ctx: PERMISSIONSContext) => void;
    /**
     * Exit a parse tree produced by the `PERMISSIONS`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPERMISSIONS?: (ctx: PERMISSIONSContext) => void;
    /**
     * Enter a parse tree produced by the `PWDENCRYPT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPWDENCRYPT?: (ctx: PWDENCRYPTContext) => void;
    /**
     * Exit a parse tree produced by the `PWDENCRYPT`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPWDENCRYPT?: (ctx: PWDENCRYPTContext) => void;
    /**
     * Enter a parse tree produced by the `PWDCOMPARE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterPWDCOMPARE?: (ctx: PWDCOMPAREContext) => void;
    /**
     * Exit a parse tree produced by the `PWDCOMPARE`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitPWDCOMPARE?: (ctx: PWDCOMPAREContext) => void;
    /**
     * Enter a parse tree produced by the `SESSION_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSESSION_USER?: (ctx: SESSION_USERContext) => void;
    /**
     * Exit a parse tree produced by the `SESSION_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSESSION_USER?: (ctx: SESSION_USERContext) => void;
    /**
     * Enter a parse tree produced by the `SESSIONPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSESSIONPROPERTY?: (ctx: SESSIONPROPERTYContext) => void;
    /**
     * Exit a parse tree produced by the `SESSIONPROPERTY`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSESSIONPROPERTY?: (ctx: SESSIONPROPERTYContext) => void;
    /**
     * Enter a parse tree produced by the `SUSER_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSUSER_ID?: (ctx: SUSER_IDContext) => void;
    /**
     * Exit a parse tree produced by the `SUSER_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSUSER_ID?: (ctx: SUSER_IDContext) => void;
    /**
     * Enter a parse tree produced by the `SUSER_SNAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSUSER_SNAME?: (ctx: SUSER_SNAMEContext) => void;
    /**
     * Exit a parse tree produced by the `SUSER_SNAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSUSER_SNAME?: (ctx: SUSER_SNAMEContext) => void;
    /**
     * Enter a parse tree produced by the `SUSER_SID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSUSER_SID?: (ctx: SUSER_SIDContext) => void;
    /**
     * Exit a parse tree produced by the `SUSER_SID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSUSER_SID?: (ctx: SUSER_SIDContext) => void;
    /**
     * Enter a parse tree produced by the `SYSTEM_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterSYSTEM_USER?: (ctx: SYSTEM_USERContext) => void;
    /**
     * Exit a parse tree produced by the `SYSTEM_USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitSYSTEM_USER?: (ctx: SYSTEM_USERContext) => void;
    /**
     * Enter a parse tree produced by the `USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterUSER?: (ctx: USERContext) => void;
    /**
     * Exit a parse tree produced by the `USER`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitUSER?: (ctx: USERContext) => void;
    /**
     * Enter a parse tree produced by the `USER_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterUSER_ID?: (ctx: USER_IDContext) => void;
    /**
     * Exit a parse tree produced by the `USER_ID`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitUSER_ID?: (ctx: USER_IDContext) => void;
    /**
     * Enter a parse tree produced by the `USER_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterUSER_NAME?: (ctx: USER_NAMEContext) => void;
    /**
     * Exit a parse tree produced by the `USER_NAME`
     * labeled alternative in `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitUSER_NAME?: (ctx: USER_NAMEContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.program`.
     * @param ctx the parse tree
     */
    enterProgram?: (ctx: ProgramContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.program`.
     * @param ctx the parse tree
     */
    exitProgram?: (ctx: ProgramContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.tsql_file`.
     * @param ctx the parse tree
     */
    enterTsql_file?: (ctx: Tsql_fileContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.tsql_file`.
     * @param ctx the parse tree
     */
    exitTsql_file?: (ctx: Tsql_fileContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.batch`.
     * @param ctx the parse tree
     */
    enterBatch?: (ctx: BatchContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.batch`.
     * @param ctx the parse tree
     */
    exitBatch?: (ctx: BatchContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.batch_level_statement`.
     * @param ctx the parse tree
     */
    enterBatch_level_statement?: (ctx: Batch_level_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.batch_level_statement`.
     * @param ctx the parse tree
     */
    exitBatch_level_statement?: (ctx: Batch_level_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.sql_clauses`.
     * @param ctx the parse tree
     */
    enterSql_clauses?: (ctx: Sql_clausesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.sql_clauses`.
     * @param ctx the parse tree
     */
    exitSql_clauses?: (ctx: Sql_clausesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dml_clause`.
     * @param ctx the parse tree
     */
    enterDml_clause?: (ctx: Dml_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dml_clause`.
     * @param ctx the parse tree
     */
    exitDml_clause?: (ctx: Dml_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ddl_clause`.
     * @param ctx the parse tree
     */
    enterDdl_clause?: (ctx: Ddl_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ddl_clause`.
     * @param ctx the parse tree
     */
    exitDdl_clause?: (ctx: Ddl_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_statement`.
     * @param ctx the parse tree
     */
    enterBackup_statement?: (ctx: Backup_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_statement`.
     * @param ctx the parse tree
     */
    exitBackup_statement?: (ctx: Backup_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.cfl_statement`.
     * @param ctx the parse tree
     */
    enterCfl_statement?: (ctx: Cfl_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.cfl_statement`.
     * @param ctx the parse tree
     */
    exitCfl_statement?: (ctx: Cfl_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.block_statement`.
     * @param ctx the parse tree
     */
    enterBlock_statement?: (ctx: Block_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.block_statement`.
     * @param ctx the parse tree
     */
    exitBlock_statement?: (ctx: Block_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.break_statement`.
     * @param ctx the parse tree
     */
    enterBreak_statement?: (ctx: Break_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.break_statement`.
     * @param ctx the parse tree
     */
    exitBreak_statement?: (ctx: Break_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.continue_statement`.
     * @param ctx the parse tree
     */
    enterContinue_statement?: (ctx: Continue_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.continue_statement`.
     * @param ctx the parse tree
     */
    exitContinue_statement?: (ctx: Continue_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.goto_statement`.
     * @param ctx the parse tree
     */
    enterGoto_statement?: (ctx: Goto_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.goto_statement`.
     * @param ctx the parse tree
     */
    exitGoto_statement?: (ctx: Goto_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.return_statement`.
     * @param ctx the parse tree
     */
    enterReturn_statement?: (ctx: Return_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.return_statement`.
     * @param ctx the parse tree
     */
    exitReturn_statement?: (ctx: Return_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.if_statement`.
     * @param ctx the parse tree
     */
    enterIf_statement?: (ctx: If_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.if_statement`.
     * @param ctx the parse tree
     */
    exitIf_statement?: (ctx: If_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.throw_statement`.
     * @param ctx the parse tree
     */
    enterThrow_statement?: (ctx: Throw_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.throw_statement`.
     * @param ctx the parse tree
     */
    exitThrow_statement?: (ctx: Throw_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.throw_error_number`.
     * @param ctx the parse tree
     */
    enterThrow_error_number?: (ctx: Throw_error_numberContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.throw_error_number`.
     * @param ctx the parse tree
     */
    exitThrow_error_number?: (ctx: Throw_error_numberContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.throw_message`.
     * @param ctx the parse tree
     */
    enterThrow_message?: (ctx: Throw_messageContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.throw_message`.
     * @param ctx the parse tree
     */
    exitThrow_message?: (ctx: Throw_messageContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.throw_state`.
     * @param ctx the parse tree
     */
    enterThrow_state?: (ctx: Throw_stateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.throw_state`.
     * @param ctx the parse tree
     */
    exitThrow_state?: (ctx: Throw_stateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.try_catch_statement`.
     * @param ctx the parse tree
     */
    enterTry_catch_statement?: (ctx: Try_catch_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.try_catch_statement`.
     * @param ctx the parse tree
     */
    exitTry_catch_statement?: (ctx: Try_catch_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.waitfor_statement`.
     * @param ctx the parse tree
     */
    enterWaitfor_statement?: (ctx: Waitfor_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.waitfor_statement`.
     * @param ctx the parse tree
     */
    exitWaitfor_statement?: (ctx: Waitfor_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.while_statement`.
     * @param ctx the parse tree
     */
    enterWhile_statement?: (ctx: While_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.while_statement`.
     * @param ctx the parse tree
     */
    exitWhile_statement?: (ctx: While_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.print_statement`.
     * @param ctx the parse tree
     */
    enterPrint_statement?: (ctx: Print_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.print_statement`.
     * @param ctx the parse tree
     */
    exitPrint_statement?: (ctx: Print_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.raiseerror_statement`.
     * @param ctx the parse tree
     */
    enterRaiseerror_statement?: (ctx: Raiseerror_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.raiseerror_statement`.
     * @param ctx the parse tree
     */
    exitRaiseerror_statement?: (ctx: Raiseerror_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.empty_statement`.
     * @param ctx the parse tree
     */
    enterEmpty_statement?: (ctx: Empty_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.empty_statement`.
     * @param ctx the parse tree
     */
    exitEmpty_statement?: (ctx: Empty_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.another_statement`.
     * @param ctx the parse tree
     */
    enterAnother_statement?: (ctx: Another_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.another_statement`.
     * @param ctx the parse tree
     */
    exitAnother_statement?: (ctx: Another_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_application_role`.
     * @param ctx the parse tree
     */
    enterAlter_application_role?: (ctx: Alter_application_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_application_role`.
     * @param ctx the parse tree
     */
    exitAlter_application_role?: (ctx: Alter_application_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_xml_schema_collection`.
     * @param ctx the parse tree
     */
    enterAlter_xml_schema_collection?: (ctx: Alter_xml_schema_collectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_xml_schema_collection`.
     * @param ctx the parse tree
     */
    exitAlter_xml_schema_collection?: (ctx: Alter_xml_schema_collectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_application_role`.
     * @param ctx the parse tree
     */
    enterCreate_application_role?: (ctx: Create_application_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_application_role`.
     * @param ctx the parse tree
     */
    exitCreate_application_role?: (ctx: Create_application_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_aggregate`.
     * @param ctx the parse tree
     */
    enterDrop_aggregate?: (ctx: Drop_aggregateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_aggregate`.
     * @param ctx the parse tree
     */
    exitDrop_aggregate?: (ctx: Drop_aggregateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_application_role`.
     * @param ctx the parse tree
     */
    enterDrop_application_role?: (ctx: Drop_application_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_application_role`.
     * @param ctx the parse tree
     */
    exitDrop_application_role?: (ctx: Drop_application_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly`.
     * @param ctx the parse tree
     */
    enterAlter_assembly?: (ctx: Alter_assemblyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly`.
     * @param ctx the parse tree
     */
    exitAlter_assembly?: (ctx: Alter_assemblyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_start`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_start?: (ctx: Alter_assembly_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_start`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_start?: (ctx: Alter_assembly_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_clause?: (ctx: Alter_assembly_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_clause?: (ctx: Alter_assembly_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_from_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_from_clause?: (ctx: Alter_assembly_from_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_from_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_from_clause?: (ctx: Alter_assembly_from_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_from_clause_start`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_from_clause_start?: (ctx: Alter_assembly_from_clause_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_from_clause_start`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_from_clause_start?: (ctx: Alter_assembly_from_clause_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_drop_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_drop_clause?: (ctx: Alter_assembly_drop_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_drop_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_drop_clause?: (ctx: Alter_assembly_drop_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_drop_multiple_files`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_drop_multiple_files?: (ctx: Alter_assembly_drop_multiple_filesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_drop_multiple_files`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_drop_multiple_files?: (ctx: Alter_assembly_drop_multiple_filesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_drop`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_drop?: (ctx: Alter_assembly_dropContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_drop`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_drop?: (ctx: Alter_assembly_dropContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_add_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_add_clause?: (ctx: Alter_assembly_add_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_add_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_add_clause?: (ctx: Alter_assembly_add_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_asssembly_add_clause_start`.
     * @param ctx the parse tree
     */
    enterAlter_asssembly_add_clause_start?: (ctx: Alter_asssembly_add_clause_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_asssembly_add_clause_start`.
     * @param ctx the parse tree
     */
    exitAlter_asssembly_add_clause_start?: (ctx: Alter_asssembly_add_clause_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_client_file_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_client_file_clause?: (ctx: Alter_assembly_client_file_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_client_file_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_client_file_clause?: (ctx: Alter_assembly_client_file_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_file_name`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_file_name?: (ctx: Alter_assembly_file_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_file_name`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_file_name?: (ctx: Alter_assembly_file_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_file_bits`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_file_bits?: (ctx: Alter_assembly_file_bitsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_file_bits`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_file_bits?: (ctx: Alter_assembly_file_bitsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_as`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_as?: (ctx: Alter_assembly_asContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_as`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_as?: (ctx: Alter_assembly_asContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_with_clause`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_with_clause?: (ctx: Alter_assembly_with_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_with_clause`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_with_clause?: (ctx: Alter_assembly_with_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_assembly_with`.
     * @param ctx the parse tree
     */
    enterAlter_assembly_with?: (ctx: Alter_assembly_withContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_assembly_with`.
     * @param ctx the parse tree
     */
    exitAlter_assembly_with?: (ctx: Alter_assembly_withContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.client_assembly_specifier`.
     * @param ctx the parse tree
     */
    enterClient_assembly_specifier?: (ctx: Client_assembly_specifierContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.client_assembly_specifier`.
     * @param ctx the parse tree
     */
    exitClient_assembly_specifier?: (ctx: Client_assembly_specifierContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.assembly_option`.
     * @param ctx the parse tree
     */
    enterAssembly_option?: (ctx: Assembly_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.assembly_option`.
     * @param ctx the parse tree
     */
    exitAssembly_option?: (ctx: Assembly_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.network_file_share`.
     * @param ctx the parse tree
     */
    enterNetwork_file_share?: (ctx: Network_file_shareContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.network_file_share`.
     * @param ctx the parse tree
     */
    exitNetwork_file_share?: (ctx: Network_file_shareContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.network_computer`.
     * @param ctx the parse tree
     */
    enterNetwork_computer?: (ctx: Network_computerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.network_computer`.
     * @param ctx the parse tree
     */
    exitNetwork_computer?: (ctx: Network_computerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.network_file_start`.
     * @param ctx the parse tree
     */
    enterNetwork_file_start?: (ctx: Network_file_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.network_file_start`.
     * @param ctx the parse tree
     */
    exitNetwork_file_start?: (ctx: Network_file_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.file_path`.
     * @param ctx the parse tree
     */
    enterFile_path?: (ctx: File_pathContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.file_path`.
     * @param ctx the parse tree
     */
    exitFile_path?: (ctx: File_pathContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.file_directory_path_separator`.
     * @param ctx the parse tree
     */
    enterFile_directory_path_separator?: (ctx: File_directory_path_separatorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.file_directory_path_separator`.
     * @param ctx the parse tree
     */
    exitFile_directory_path_separator?: (ctx: File_directory_path_separatorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.local_file`.
     * @param ctx the parse tree
     */
    enterLocal_file?: (ctx: Local_fileContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.local_file`.
     * @param ctx the parse tree
     */
    exitLocal_file?: (ctx: Local_fileContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.local_drive`.
     * @param ctx the parse tree
     */
    enterLocal_drive?: (ctx: Local_driveContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.local_drive`.
     * @param ctx the parse tree
     */
    exitLocal_drive?: (ctx: Local_driveContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.multiple_local_files`.
     * @param ctx the parse tree
     */
    enterMultiple_local_files?: (ctx: Multiple_local_filesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.multiple_local_files`.
     * @param ctx the parse tree
     */
    exitMultiple_local_files?: (ctx: Multiple_local_filesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.multiple_local_file_start`.
     * @param ctx the parse tree
     */
    enterMultiple_local_file_start?: (ctx: Multiple_local_file_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.multiple_local_file_start`.
     * @param ctx the parse tree
     */
    exitMultiple_local_file_start?: (ctx: Multiple_local_file_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_assembly`.
     * @param ctx the parse tree
     */
    enterCreate_assembly?: (ctx: Create_assemblyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_assembly`.
     * @param ctx the parse tree
     */
    exitCreate_assembly?: (ctx: Create_assemblyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_assembly`.
     * @param ctx the parse tree
     */
    enterDrop_assembly?: (ctx: Drop_assemblyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_assembly`.
     * @param ctx the parse tree
     */
    exitDrop_assembly?: (ctx: Drop_assemblyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_asymmetric_key`.
     * @param ctx the parse tree
     */
    enterAlter_asymmetric_key?: (ctx: Alter_asymmetric_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_asymmetric_key`.
     * @param ctx the parse tree
     */
    exitAlter_asymmetric_key?: (ctx: Alter_asymmetric_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_asymmetric_key_start`.
     * @param ctx the parse tree
     */
    enterAlter_asymmetric_key_start?: (ctx: Alter_asymmetric_key_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_asymmetric_key_start`.
     * @param ctx the parse tree
     */
    exitAlter_asymmetric_key_start?: (ctx: Alter_asymmetric_key_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.asymmetric_key_option`.
     * @param ctx the parse tree
     */
    enterAsymmetric_key_option?: (ctx: Asymmetric_key_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.asymmetric_key_option`.
     * @param ctx the parse tree
     */
    exitAsymmetric_key_option?: (ctx: Asymmetric_key_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.asymmetric_key_option_start`.
     * @param ctx the parse tree
     */
    enterAsymmetric_key_option_start?: (ctx: Asymmetric_key_option_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.asymmetric_key_option_start`.
     * @param ctx the parse tree
     */
    exitAsymmetric_key_option_start?: (ctx: Asymmetric_key_option_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.asymmetric_key_password_change_option`.
     * @param ctx the parse tree
     */
    enterAsymmetric_key_password_change_option?: (ctx: Asymmetric_key_password_change_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.asymmetric_key_password_change_option`.
     * @param ctx the parse tree
     */
    exitAsymmetric_key_password_change_option?: (ctx: Asymmetric_key_password_change_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_asymmetric_key`.
     * @param ctx the parse tree
     */
    enterCreate_asymmetric_key?: (ctx: Create_asymmetric_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_asymmetric_key`.
     * @param ctx the parse tree
     */
    exitCreate_asymmetric_key?: (ctx: Create_asymmetric_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_asymmetric_key`.
     * @param ctx the parse tree
     */
    enterDrop_asymmetric_key?: (ctx: Drop_asymmetric_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_asymmetric_key`.
     * @param ctx the parse tree
     */
    exitDrop_asymmetric_key?: (ctx: Drop_asymmetric_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_authorization`.
     * @param ctx the parse tree
     */
    enterAlter_authorization?: (ctx: Alter_authorizationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_authorization`.
     * @param ctx the parse tree
     */
    exitAlter_authorization?: (ctx: Alter_authorizationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.authorization_grantee`.
     * @param ctx the parse tree
     */
    enterAuthorization_grantee?: (ctx: Authorization_granteeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.authorization_grantee`.
     * @param ctx the parse tree
     */
    exitAuthorization_grantee?: (ctx: Authorization_granteeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.entity_to`.
     * @param ctx the parse tree
     */
    enterEntity_to?: (ctx: Entity_toContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.entity_to`.
     * @param ctx the parse tree
     */
    exitEntity_to?: (ctx: Entity_toContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.colon_colon`.
     * @param ctx the parse tree
     */
    enterColon_colon?: (ctx: Colon_colonContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.colon_colon`.
     * @param ctx the parse tree
     */
    exitColon_colon?: (ctx: Colon_colonContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_authorization_start`.
     * @param ctx the parse tree
     */
    enterAlter_authorization_start?: (ctx: Alter_authorization_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_authorization_start`.
     * @param ctx the parse tree
     */
    exitAlter_authorization_start?: (ctx: Alter_authorization_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_authorization_for_sql_database`.
     * @param ctx the parse tree
     */
    enterAlter_authorization_for_sql_database?: (ctx: Alter_authorization_for_sql_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_authorization_for_sql_database`.
     * @param ctx the parse tree
     */
    exitAlter_authorization_for_sql_database?: (ctx: Alter_authorization_for_sql_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_authorization_for_azure_dw`.
     * @param ctx the parse tree
     */
    enterAlter_authorization_for_azure_dw?: (ctx: Alter_authorization_for_azure_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_authorization_for_azure_dw`.
     * @param ctx the parse tree
     */
    exitAlter_authorization_for_azure_dw?: (ctx: Alter_authorization_for_azure_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_authorization_for_parallel_dw`.
     * @param ctx the parse tree
     */
    enterAlter_authorization_for_parallel_dw?: (ctx: Alter_authorization_for_parallel_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_authorization_for_parallel_dw`.
     * @param ctx the parse tree
     */
    exitAlter_authorization_for_parallel_dw?: (ctx: Alter_authorization_for_parallel_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.class_type`.
     * @param ctx the parse tree
     */
    enterClass_type?: (ctx: Class_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.class_type`.
     * @param ctx the parse tree
     */
    exitClass_type?: (ctx: Class_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.class_type_for_sql_database`.
     * @param ctx the parse tree
     */
    enterClass_type_for_sql_database?: (ctx: Class_type_for_sql_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.class_type_for_sql_database`.
     * @param ctx the parse tree
     */
    exitClass_type_for_sql_database?: (ctx: Class_type_for_sql_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.class_type_for_azure_dw`.
     * @param ctx the parse tree
     */
    enterClass_type_for_azure_dw?: (ctx: Class_type_for_azure_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.class_type_for_azure_dw`.
     * @param ctx the parse tree
     */
    exitClass_type_for_azure_dw?: (ctx: Class_type_for_azure_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.class_type_for_parallel_dw`.
     * @param ctx the parse tree
     */
    enterClass_type_for_parallel_dw?: (ctx: Class_type_for_parallel_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.class_type_for_parallel_dw`.
     * @param ctx the parse tree
     */
    exitClass_type_for_parallel_dw?: (ctx: Class_type_for_parallel_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.class_type_for_grant`.
     * @param ctx the parse tree
     */
    enterClass_type_for_grant?: (ctx: Class_type_for_grantContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.class_type_for_grant`.
     * @param ctx the parse tree
     */
    exitClass_type_for_grant?: (ctx: Class_type_for_grantContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_availability_group`.
     * @param ctx the parse tree
     */
    enterDrop_availability_group?: (ctx: Drop_availability_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_availability_group`.
     * @param ctx the parse tree
     */
    exitDrop_availability_group?: (ctx: Drop_availability_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_availability_group`.
     * @param ctx the parse tree
     */
    enterAlter_availability_group?: (ctx: Alter_availability_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_availability_group`.
     * @param ctx the parse tree
     */
    exitAlter_availability_group?: (ctx: Alter_availability_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_availability_group_start`.
     * @param ctx the parse tree
     */
    enterAlter_availability_group_start?: (ctx: Alter_availability_group_startContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_availability_group_start`.
     * @param ctx the parse tree
     */
    exitAlter_availability_group_start?: (ctx: Alter_availability_group_startContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_availability_group_options`.
     * @param ctx the parse tree
     */
    enterAlter_availability_group_options?: (ctx: Alter_availability_group_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_availability_group_options`.
     * @param ctx the parse tree
     */
    exitAlter_availability_group_options?: (ctx: Alter_availability_group_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ip_v4_failover`.
     * @param ctx the parse tree
     */
    enterIp_v4_failover?: (ctx: Ip_v4_failoverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ip_v4_failover`.
     * @param ctx the parse tree
     */
    exitIp_v4_failover?: (ctx: Ip_v4_failoverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ip_v6_failover`.
     * @param ctx the parse tree
     */
    enterIp_v6_failover?: (ctx: Ip_v6_failoverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ip_v6_failover`.
     * @param ctx the parse tree
     */
    exitIp_v6_failover?: (ctx: Ip_v6_failoverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_broker_priority`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_broker_priority?: (ctx: Create_or_alter_broker_priorityContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_broker_priority`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_broker_priority?: (ctx: Create_or_alter_broker_priorityContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_broker_priority`.
     * @param ctx the parse tree
     */
    enterDrop_broker_priority?: (ctx: Drop_broker_priorityContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_broker_priority`.
     * @param ctx the parse tree
     */
    exitDrop_broker_priority?: (ctx: Drop_broker_priorityContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_certificate`.
     * @param ctx the parse tree
     */
    enterAlter_certificate?: (ctx: Alter_certificateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_certificate`.
     * @param ctx the parse tree
     */
    exitAlter_certificate?: (ctx: Alter_certificateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_column_encryption_key`.
     * @param ctx the parse tree
     */
    enterAlter_column_encryption_key?: (ctx: Alter_column_encryption_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_column_encryption_key`.
     * @param ctx the parse tree
     */
    exitAlter_column_encryption_key?: (ctx: Alter_column_encryption_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_column_encryption_key`.
     * @param ctx the parse tree
     */
    enterCreate_column_encryption_key?: (ctx: Create_column_encryption_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_column_encryption_key`.
     * @param ctx the parse tree
     */
    exitCreate_column_encryption_key?: (ctx: Create_column_encryption_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_certificate`.
     * @param ctx the parse tree
     */
    enterDrop_certificate?: (ctx: Drop_certificateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_certificate`.
     * @param ctx the parse tree
     */
    exitDrop_certificate?: (ctx: Drop_certificateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_column_encryption_key`.
     * @param ctx the parse tree
     */
    enterDrop_column_encryption_key?: (ctx: Drop_column_encryption_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_column_encryption_key`.
     * @param ctx the parse tree
     */
    exitDrop_column_encryption_key?: (ctx: Drop_column_encryption_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_column_master_key`.
     * @param ctx the parse tree
     */
    enterDrop_column_master_key?: (ctx: Drop_column_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_column_master_key`.
     * @param ctx the parse tree
     */
    exitDrop_column_master_key?: (ctx: Drop_column_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_contract`.
     * @param ctx the parse tree
     */
    enterDrop_contract?: (ctx: Drop_contractContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_contract`.
     * @param ctx the parse tree
     */
    exitDrop_contract?: (ctx: Drop_contractContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_credential`.
     * @param ctx the parse tree
     */
    enterDrop_credential?: (ctx: Drop_credentialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_credential`.
     * @param ctx the parse tree
     */
    exitDrop_credential?: (ctx: Drop_credentialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_cryptograhic_provider`.
     * @param ctx the parse tree
     */
    enterDrop_cryptograhic_provider?: (ctx: Drop_cryptograhic_providerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_cryptograhic_provider`.
     * @param ctx the parse tree
     */
    exitDrop_cryptograhic_provider?: (ctx: Drop_cryptograhic_providerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_database`.
     * @param ctx the parse tree
     */
    enterDrop_database?: (ctx: Drop_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_database`.
     * @param ctx the parse tree
     */
    exitDrop_database?: (ctx: Drop_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_database_audit_specification`.
     * @param ctx the parse tree
     */
    enterDrop_database_audit_specification?: (ctx: Drop_database_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_database_audit_specification`.
     * @param ctx the parse tree
     */
    exitDrop_database_audit_specification?: (ctx: Drop_database_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_database_encryption_key`.
     * @param ctx the parse tree
     */
    enterDrop_database_encryption_key?: (ctx: Drop_database_encryption_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_database_encryption_key`.
     * @param ctx the parse tree
     */
    exitDrop_database_encryption_key?: (ctx: Drop_database_encryption_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_database_scoped_credential`.
     * @param ctx the parse tree
     */
    enterDrop_database_scoped_credential?: (ctx: Drop_database_scoped_credentialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_database_scoped_credential`.
     * @param ctx the parse tree
     */
    exitDrop_database_scoped_credential?: (ctx: Drop_database_scoped_credentialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_default`.
     * @param ctx the parse tree
     */
    enterDrop_default?: (ctx: Drop_defaultContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_default`.
     * @param ctx the parse tree
     */
    exitDrop_default?: (ctx: Drop_defaultContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_endpoint`.
     * @param ctx the parse tree
     */
    enterDrop_endpoint?: (ctx: Drop_endpointContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_endpoint`.
     * @param ctx the parse tree
     */
    exitDrop_endpoint?: (ctx: Drop_endpointContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_external_data_source`.
     * @param ctx the parse tree
     */
    enterDrop_external_data_source?: (ctx: Drop_external_data_sourceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_external_data_source`.
     * @param ctx the parse tree
     */
    exitDrop_external_data_source?: (ctx: Drop_external_data_sourceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_external_file_format`.
     * @param ctx the parse tree
     */
    enterDrop_external_file_format?: (ctx: Drop_external_file_formatContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_external_file_format`.
     * @param ctx the parse tree
     */
    exitDrop_external_file_format?: (ctx: Drop_external_file_formatContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_external_library`.
     * @param ctx the parse tree
     */
    enterDrop_external_library?: (ctx: Drop_external_libraryContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_external_library`.
     * @param ctx the parse tree
     */
    exitDrop_external_library?: (ctx: Drop_external_libraryContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_external_resource_pool`.
     * @param ctx the parse tree
     */
    enterDrop_external_resource_pool?: (ctx: Drop_external_resource_poolContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_external_resource_pool`.
     * @param ctx the parse tree
     */
    exitDrop_external_resource_pool?: (ctx: Drop_external_resource_poolContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_external_table`.
     * @param ctx the parse tree
     */
    enterDrop_external_table?: (ctx: Drop_external_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_external_table`.
     * @param ctx the parse tree
     */
    exitDrop_external_table?: (ctx: Drop_external_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_event_notifications`.
     * @param ctx the parse tree
     */
    enterDrop_event_notifications?: (ctx: Drop_event_notificationsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_event_notifications`.
     * @param ctx the parse tree
     */
    exitDrop_event_notifications?: (ctx: Drop_event_notificationsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_event_session`.
     * @param ctx the parse tree
     */
    enterDrop_event_session?: (ctx: Drop_event_sessionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_event_session`.
     * @param ctx the parse tree
     */
    exitDrop_event_session?: (ctx: Drop_event_sessionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_fulltext_catalog`.
     * @param ctx the parse tree
     */
    enterDrop_fulltext_catalog?: (ctx: Drop_fulltext_catalogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_fulltext_catalog`.
     * @param ctx the parse tree
     */
    exitDrop_fulltext_catalog?: (ctx: Drop_fulltext_catalogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_fulltext_index`.
     * @param ctx the parse tree
     */
    enterDrop_fulltext_index?: (ctx: Drop_fulltext_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_fulltext_index`.
     * @param ctx the parse tree
     */
    exitDrop_fulltext_index?: (ctx: Drop_fulltext_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    enterDrop_fulltext_stoplist?: (ctx: Drop_fulltext_stoplistContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    exitDrop_fulltext_stoplist?: (ctx: Drop_fulltext_stoplistContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_login`.
     * @param ctx the parse tree
     */
    enterDrop_login?: (ctx: Drop_loginContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_login`.
     * @param ctx the parse tree
     */
    exitDrop_login?: (ctx: Drop_loginContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_master_key`.
     * @param ctx the parse tree
     */
    enterDrop_master_key?: (ctx: Drop_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_master_key`.
     * @param ctx the parse tree
     */
    exitDrop_master_key?: (ctx: Drop_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_message_type`.
     * @param ctx the parse tree
     */
    enterDrop_message_type?: (ctx: Drop_message_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_message_type`.
     * @param ctx the parse tree
     */
    exitDrop_message_type?: (ctx: Drop_message_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_partition_function`.
     * @param ctx the parse tree
     */
    enterDrop_partition_function?: (ctx: Drop_partition_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_partition_function`.
     * @param ctx the parse tree
     */
    exitDrop_partition_function?: (ctx: Drop_partition_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_partition_scheme`.
     * @param ctx the parse tree
     */
    enterDrop_partition_scheme?: (ctx: Drop_partition_schemeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_partition_scheme`.
     * @param ctx the parse tree
     */
    exitDrop_partition_scheme?: (ctx: Drop_partition_schemeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_queue`.
     * @param ctx the parse tree
     */
    enterDrop_queue?: (ctx: Drop_queueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_queue`.
     * @param ctx the parse tree
     */
    exitDrop_queue?: (ctx: Drop_queueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_remote_service_binding`.
     * @param ctx the parse tree
     */
    enterDrop_remote_service_binding?: (ctx: Drop_remote_service_bindingContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_remote_service_binding`.
     * @param ctx the parse tree
     */
    exitDrop_remote_service_binding?: (ctx: Drop_remote_service_bindingContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_resource_pool`.
     * @param ctx the parse tree
     */
    enterDrop_resource_pool?: (ctx: Drop_resource_poolContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_resource_pool`.
     * @param ctx the parse tree
     */
    exitDrop_resource_pool?: (ctx: Drop_resource_poolContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_db_role`.
     * @param ctx the parse tree
     */
    enterDrop_db_role?: (ctx: Drop_db_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_db_role`.
     * @param ctx the parse tree
     */
    exitDrop_db_role?: (ctx: Drop_db_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_route`.
     * @param ctx the parse tree
     */
    enterDrop_route?: (ctx: Drop_routeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_route`.
     * @param ctx the parse tree
     */
    exitDrop_route?: (ctx: Drop_routeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_rule`.
     * @param ctx the parse tree
     */
    enterDrop_rule?: (ctx: Drop_ruleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_rule`.
     * @param ctx the parse tree
     */
    exitDrop_rule?: (ctx: Drop_ruleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_schema`.
     * @param ctx the parse tree
     */
    enterDrop_schema?: (ctx: Drop_schemaContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_schema`.
     * @param ctx the parse tree
     */
    exitDrop_schema?: (ctx: Drop_schemaContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_search_property_list`.
     * @param ctx the parse tree
     */
    enterDrop_search_property_list?: (ctx: Drop_search_property_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_search_property_list`.
     * @param ctx the parse tree
     */
    exitDrop_search_property_list?: (ctx: Drop_search_property_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_security_policy`.
     * @param ctx the parse tree
     */
    enterDrop_security_policy?: (ctx: Drop_security_policyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_security_policy`.
     * @param ctx the parse tree
     */
    exitDrop_security_policy?: (ctx: Drop_security_policyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_sequence`.
     * @param ctx the parse tree
     */
    enterDrop_sequence?: (ctx: Drop_sequenceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_sequence`.
     * @param ctx the parse tree
     */
    exitDrop_sequence?: (ctx: Drop_sequenceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_server_audit`.
     * @param ctx the parse tree
     */
    enterDrop_server_audit?: (ctx: Drop_server_auditContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_server_audit`.
     * @param ctx the parse tree
     */
    exitDrop_server_audit?: (ctx: Drop_server_auditContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_server_audit_specification`.
     * @param ctx the parse tree
     */
    enterDrop_server_audit_specification?: (ctx: Drop_server_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_server_audit_specification`.
     * @param ctx the parse tree
     */
    exitDrop_server_audit_specification?: (ctx: Drop_server_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_server_role`.
     * @param ctx the parse tree
     */
    enterDrop_server_role?: (ctx: Drop_server_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_server_role`.
     * @param ctx the parse tree
     */
    exitDrop_server_role?: (ctx: Drop_server_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_service`.
     * @param ctx the parse tree
     */
    enterDrop_service?: (ctx: Drop_serviceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_service`.
     * @param ctx the parse tree
     */
    exitDrop_service?: (ctx: Drop_serviceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_signature`.
     * @param ctx the parse tree
     */
    enterDrop_signature?: (ctx: Drop_signatureContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_signature`.
     * @param ctx the parse tree
     */
    exitDrop_signature?: (ctx: Drop_signatureContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_statistics_name_azure_dw_and_pdw`.
     * @param ctx the parse tree
     */
    enterDrop_statistics_name_azure_dw_and_pdw?: (ctx: Drop_statistics_name_azure_dw_and_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_statistics_name_azure_dw_and_pdw`.
     * @param ctx the parse tree
     */
    exitDrop_statistics_name_azure_dw_and_pdw?: (ctx: Drop_statistics_name_azure_dw_and_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_symmetric_key`.
     * @param ctx the parse tree
     */
    enterDrop_symmetric_key?: (ctx: Drop_symmetric_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_symmetric_key`.
     * @param ctx the parse tree
     */
    exitDrop_symmetric_key?: (ctx: Drop_symmetric_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_synonym`.
     * @param ctx the parse tree
     */
    enterDrop_synonym?: (ctx: Drop_synonymContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_synonym`.
     * @param ctx the parse tree
     */
    exitDrop_synonym?: (ctx: Drop_synonymContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_user`.
     * @param ctx the parse tree
     */
    enterDrop_user?: (ctx: Drop_userContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_user`.
     * @param ctx the parse tree
     */
    exitDrop_user?: (ctx: Drop_userContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_workload_group`.
     * @param ctx the parse tree
     */
    enterDrop_workload_group?: (ctx: Drop_workload_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_workload_group`.
     * @param ctx the parse tree
     */
    exitDrop_workload_group?: (ctx: Drop_workload_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_xml_schema_collection`.
     * @param ctx the parse tree
     */
    enterDrop_xml_schema_collection?: (ctx: Drop_xml_schema_collectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_xml_schema_collection`.
     * @param ctx the parse tree
     */
    exitDrop_xml_schema_collection?: (ctx: Drop_xml_schema_collectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.disable_trigger`.
     * @param ctx the parse tree
     */
    enterDisable_trigger?: (ctx: Disable_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.disable_trigger`.
     * @param ctx the parse tree
     */
    exitDisable_trigger?: (ctx: Disable_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.enable_trigger`.
     * @param ctx the parse tree
     */
    enterEnable_trigger?: (ctx: Enable_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.enable_trigger`.
     * @param ctx the parse tree
     */
    exitEnable_trigger?: (ctx: Enable_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.lock_table`.
     * @param ctx the parse tree
     */
    enterLock_table?: (ctx: Lock_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.lock_table`.
     * @param ctx the parse tree
     */
    exitLock_table?: (ctx: Lock_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.truncate_table`.
     * @param ctx the parse tree
     */
    enterTruncate_table?: (ctx: Truncate_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.truncate_table`.
     * @param ctx the parse tree
     */
    exitTruncate_table?: (ctx: Truncate_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_column_master_key`.
     * @param ctx the parse tree
     */
    enterCreate_column_master_key?: (ctx: Create_column_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_column_master_key`.
     * @param ctx the parse tree
     */
    exitCreate_column_master_key?: (ctx: Create_column_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_credential`.
     * @param ctx the parse tree
     */
    enterAlter_credential?: (ctx: Alter_credentialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_credential`.
     * @param ctx the parse tree
     */
    exitAlter_credential?: (ctx: Alter_credentialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_credential`.
     * @param ctx the parse tree
     */
    enterCreate_credential?: (ctx: Create_credentialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_credential`.
     * @param ctx the parse tree
     */
    exitCreate_credential?: (ctx: Create_credentialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_cryptographic_provider`.
     * @param ctx the parse tree
     */
    enterAlter_cryptographic_provider?: (ctx: Alter_cryptographic_providerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_cryptographic_provider`.
     * @param ctx the parse tree
     */
    exitAlter_cryptographic_provider?: (ctx: Alter_cryptographic_providerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_cryptographic_provider`.
     * @param ctx the parse tree
     */
    enterCreate_cryptographic_provider?: (ctx: Create_cryptographic_providerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_cryptographic_provider`.
     * @param ctx the parse tree
     */
    exitCreate_cryptographic_provider?: (ctx: Create_cryptographic_providerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_endpoint`.
     * @param ctx the parse tree
     */
    enterCreate_endpoint?: (ctx: Create_endpointContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_endpoint`.
     * @param ctx the parse tree
     */
    exitCreate_endpoint?: (ctx: Create_endpointContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.endpoint_encryption_alogorithm_clause`.
     * @param ctx the parse tree
     */
    enterEndpoint_encryption_alogorithm_clause?: (ctx: Endpoint_encryption_alogorithm_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.endpoint_encryption_alogorithm_clause`.
     * @param ctx the parse tree
     */
    exitEndpoint_encryption_alogorithm_clause?: (ctx: Endpoint_encryption_alogorithm_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.endpoint_authentication_clause`.
     * @param ctx the parse tree
     */
    enterEndpoint_authentication_clause?: (ctx: Endpoint_authentication_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.endpoint_authentication_clause`.
     * @param ctx the parse tree
     */
    exitEndpoint_authentication_clause?: (ctx: Endpoint_authentication_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.endpoint_listener_clause`.
     * @param ctx the parse tree
     */
    enterEndpoint_listener_clause?: (ctx: Endpoint_listener_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.endpoint_listener_clause`.
     * @param ctx the parse tree
     */
    exitEndpoint_listener_clause?: (ctx: Endpoint_listener_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_event_notification`.
     * @param ctx the parse tree
     */
    enterCreate_event_notification?: (ctx: Create_event_notificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_event_notification`.
     * @param ctx the parse tree
     */
    exitCreate_event_notification?: (ctx: Create_event_notificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_event_session`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_event_session?: (ctx: Create_or_alter_event_sessionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_event_session`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_event_session?: (ctx: Create_or_alter_event_sessionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.event_session_predicate_expression`.
     * @param ctx the parse tree
     */
    enterEvent_session_predicate_expression?: (ctx: Event_session_predicate_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.event_session_predicate_expression`.
     * @param ctx the parse tree
     */
    exitEvent_session_predicate_expression?: (ctx: Event_session_predicate_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.event_session_predicate_factor`.
     * @param ctx the parse tree
     */
    enterEvent_session_predicate_factor?: (ctx: Event_session_predicate_factorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.event_session_predicate_factor`.
     * @param ctx the parse tree
     */
    exitEvent_session_predicate_factor?: (ctx: Event_session_predicate_factorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.event_session_predicate_leaf`.
     * @param ctx the parse tree
     */
    enterEvent_session_predicate_leaf?: (ctx: Event_session_predicate_leafContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.event_session_predicate_leaf`.
     * @param ctx the parse tree
     */
    exitEvent_session_predicate_leaf?: (ctx: Event_session_predicate_leafContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_external_data_source`.
     * @param ctx the parse tree
     */
    enterAlter_external_data_source?: (ctx: Alter_external_data_sourceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_external_data_source`.
     * @param ctx the parse tree
     */
    exitAlter_external_data_source?: (ctx: Alter_external_data_sourceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_external_library`.
     * @param ctx the parse tree
     */
    enterAlter_external_library?: (ctx: Alter_external_libraryContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_external_library`.
     * @param ctx the parse tree
     */
    exitAlter_external_library?: (ctx: Alter_external_libraryContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_external_library`.
     * @param ctx the parse tree
     */
    enterCreate_external_library?: (ctx: Create_external_libraryContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_external_library`.
     * @param ctx the parse tree
     */
    exitCreate_external_library?: (ctx: Create_external_libraryContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_external_resource_pool`.
     * @param ctx the parse tree
     */
    enterAlter_external_resource_pool?: (ctx: Alter_external_resource_poolContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_external_resource_pool`.
     * @param ctx the parse tree
     */
    exitAlter_external_resource_pool?: (ctx: Alter_external_resource_poolContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_external_resource_pool`.
     * @param ctx the parse tree
     */
    enterCreate_external_resource_pool?: (ctx: Create_external_resource_poolContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_external_resource_pool`.
     * @param ctx the parse tree
     */
    exitCreate_external_resource_pool?: (ctx: Create_external_resource_poolContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_fulltext_catalog`.
     * @param ctx the parse tree
     */
    enterAlter_fulltext_catalog?: (ctx: Alter_fulltext_catalogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_fulltext_catalog`.
     * @param ctx the parse tree
     */
    exitAlter_fulltext_catalog?: (ctx: Alter_fulltext_catalogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_fulltext_catalog`.
     * @param ctx the parse tree
     */
    enterCreate_fulltext_catalog?: (ctx: Create_fulltext_catalogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_fulltext_catalog`.
     * @param ctx the parse tree
     */
    exitCreate_fulltext_catalog?: (ctx: Create_fulltext_catalogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    enterAlter_fulltext_stoplist?: (ctx: Alter_fulltext_stoplistContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    exitAlter_fulltext_stoplist?: (ctx: Alter_fulltext_stoplistContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    enterCreate_fulltext_stoplist?: (ctx: Create_fulltext_stoplistContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_fulltext_stoplist`.
     * @param ctx the parse tree
     */
    exitCreate_fulltext_stoplist?: (ctx: Create_fulltext_stoplistContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_login_sql_server`.
     * @param ctx the parse tree
     */
    enterAlter_login_sql_server?: (ctx: Alter_login_sql_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_login_sql_server`.
     * @param ctx the parse tree
     */
    exitAlter_login_sql_server?: (ctx: Alter_login_sql_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_login_sql_server`.
     * @param ctx the parse tree
     */
    enterCreate_login_sql_server?: (ctx: Create_login_sql_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_login_sql_server`.
     * @param ctx the parse tree
     */
    exitCreate_login_sql_server?: (ctx: Create_login_sql_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_login_azure_sql`.
     * @param ctx the parse tree
     */
    enterAlter_login_azure_sql?: (ctx: Alter_login_azure_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_login_azure_sql`.
     * @param ctx the parse tree
     */
    exitAlter_login_azure_sql?: (ctx: Alter_login_azure_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_login_azure_sql`.
     * @param ctx the parse tree
     */
    enterCreate_login_azure_sql?: (ctx: Create_login_azure_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_login_azure_sql`.
     * @param ctx the parse tree
     */
    exitCreate_login_azure_sql?: (ctx: Create_login_azure_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_login_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    enterAlter_login_azure_sql_dw_and_pdw?: (ctx: Alter_login_azure_sql_dw_and_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_login_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    exitAlter_login_azure_sql_dw_and_pdw?: (ctx: Alter_login_azure_sql_dw_and_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_login_pdw`.
     * @param ctx the parse tree
     */
    enterCreate_login_pdw?: (ctx: Create_login_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_login_pdw`.
     * @param ctx the parse tree
     */
    exitCreate_login_pdw?: (ctx: Create_login_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_master_key_sql_server`.
     * @param ctx the parse tree
     */
    enterAlter_master_key_sql_server?: (ctx: Alter_master_key_sql_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_master_key_sql_server`.
     * @param ctx the parse tree
     */
    exitAlter_master_key_sql_server?: (ctx: Alter_master_key_sql_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_master_key_sql_server`.
     * @param ctx the parse tree
     */
    enterCreate_master_key_sql_server?: (ctx: Create_master_key_sql_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_master_key_sql_server`.
     * @param ctx the parse tree
     */
    exitCreate_master_key_sql_server?: (ctx: Create_master_key_sql_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_master_key_azure_sql`.
     * @param ctx the parse tree
     */
    enterAlter_master_key_azure_sql?: (ctx: Alter_master_key_azure_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_master_key_azure_sql`.
     * @param ctx the parse tree
     */
    exitAlter_master_key_azure_sql?: (ctx: Alter_master_key_azure_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_master_key_azure_sql`.
     * @param ctx the parse tree
     */
    enterCreate_master_key_azure_sql?: (ctx: Create_master_key_azure_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_master_key_azure_sql`.
     * @param ctx the parse tree
     */
    exitCreate_master_key_azure_sql?: (ctx: Create_master_key_azure_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_message_type`.
     * @param ctx the parse tree
     */
    enterAlter_message_type?: (ctx: Alter_message_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_message_type`.
     * @param ctx the parse tree
     */
    exitAlter_message_type?: (ctx: Alter_message_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_partition_function`.
     * @param ctx the parse tree
     */
    enterAlter_partition_function?: (ctx: Alter_partition_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_partition_function`.
     * @param ctx the parse tree
     */
    exitAlter_partition_function?: (ctx: Alter_partition_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_partition_scheme`.
     * @param ctx the parse tree
     */
    enterAlter_partition_scheme?: (ctx: Alter_partition_schemeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_partition_scheme`.
     * @param ctx the parse tree
     */
    exitAlter_partition_scheme?: (ctx: Alter_partition_schemeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_remote_service_binding`.
     * @param ctx the parse tree
     */
    enterAlter_remote_service_binding?: (ctx: Alter_remote_service_bindingContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_remote_service_binding`.
     * @param ctx the parse tree
     */
    exitAlter_remote_service_binding?: (ctx: Alter_remote_service_bindingContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_remote_service_binding`.
     * @param ctx the parse tree
     */
    enterCreate_remote_service_binding?: (ctx: Create_remote_service_bindingContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_remote_service_binding`.
     * @param ctx the parse tree
     */
    exitCreate_remote_service_binding?: (ctx: Create_remote_service_bindingContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_resource_pool`.
     * @param ctx the parse tree
     */
    enterCreate_resource_pool?: (ctx: Create_resource_poolContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_resource_pool`.
     * @param ctx the parse tree
     */
    exitCreate_resource_pool?: (ctx: Create_resource_poolContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_resource_governor`.
     * @param ctx the parse tree
     */
    enterAlter_resource_governor?: (ctx: Alter_resource_governorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_resource_governor`.
     * @param ctx the parse tree
     */
    exitAlter_resource_governor?: (ctx: Alter_resource_governorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_database_audit_specification`.
     * @param ctx the parse tree
     */
    enterAlter_database_audit_specification?: (ctx: Alter_database_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_database_audit_specification`.
     * @param ctx the parse tree
     */
    exitAlter_database_audit_specification?: (ctx: Alter_database_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.audit_action_spec_group`.
     * @param ctx the parse tree
     */
    enterAudit_action_spec_group?: (ctx: Audit_action_spec_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.audit_action_spec_group`.
     * @param ctx the parse tree
     */
    exitAudit_action_spec_group?: (ctx: Audit_action_spec_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.audit_action_specification`.
     * @param ctx the parse tree
     */
    enterAudit_action_specification?: (ctx: Audit_action_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.audit_action_specification`.
     * @param ctx the parse tree
     */
    exitAudit_action_specification?: (ctx: Audit_action_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.action_specification`.
     * @param ctx the parse tree
     */
    enterAction_specification?: (ctx: Action_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.action_specification`.
     * @param ctx the parse tree
     */
    exitAction_specification?: (ctx: Action_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.audit_class_name`.
     * @param ctx the parse tree
     */
    enterAudit_class_name?: (ctx: Audit_class_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.audit_class_name`.
     * @param ctx the parse tree
     */
    exitAudit_class_name?: (ctx: Audit_class_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.audit_securable`.
     * @param ctx the parse tree
     */
    enterAudit_securable?: (ctx: Audit_securableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.audit_securable`.
     * @param ctx the parse tree
     */
    exitAudit_securable?: (ctx: Audit_securableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_db_role`.
     * @param ctx the parse tree
     */
    enterAlter_db_role?: (ctx: Alter_db_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_db_role`.
     * @param ctx the parse tree
     */
    exitAlter_db_role?: (ctx: Alter_db_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_database_audit_specification`.
     * @param ctx the parse tree
     */
    enterCreate_database_audit_specification?: (ctx: Create_database_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_database_audit_specification`.
     * @param ctx the parse tree
     */
    exitCreate_database_audit_specification?: (ctx: Create_database_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_db_role`.
     * @param ctx the parse tree
     */
    enterCreate_db_role?: (ctx: Create_db_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_db_role`.
     * @param ctx the parse tree
     */
    exitCreate_db_role?: (ctx: Create_db_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_route`.
     * @param ctx the parse tree
     */
    enterCreate_route?: (ctx: Create_routeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_route`.
     * @param ctx the parse tree
     */
    exitCreate_route?: (ctx: Create_routeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_rule`.
     * @param ctx the parse tree
     */
    enterCreate_rule?: (ctx: Create_ruleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_rule`.
     * @param ctx the parse tree
     */
    exitCreate_rule?: (ctx: Create_ruleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_schema_sql`.
     * @param ctx the parse tree
     */
    enterAlter_schema_sql?: (ctx: Alter_schema_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_schema_sql`.
     * @param ctx the parse tree
     */
    exitAlter_schema_sql?: (ctx: Alter_schema_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_schema`.
     * @param ctx the parse tree
     */
    enterCreate_schema?: (ctx: Create_schemaContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_schema`.
     * @param ctx the parse tree
     */
    exitCreate_schema?: (ctx: Create_schemaContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_schema_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    enterCreate_schema_azure_sql_dw_and_pdw?: (ctx: Create_schema_azure_sql_dw_and_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_schema_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    exitCreate_schema_azure_sql_dw_and_pdw?: (ctx: Create_schema_azure_sql_dw_and_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_schema_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    enterAlter_schema_azure_sql_dw_and_pdw?: (ctx: Alter_schema_azure_sql_dw_and_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_schema_azure_sql_dw_and_pdw`.
     * @param ctx the parse tree
     */
    exitAlter_schema_azure_sql_dw_and_pdw?: (ctx: Alter_schema_azure_sql_dw_and_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_search_property_list`.
     * @param ctx the parse tree
     */
    enterCreate_search_property_list?: (ctx: Create_search_property_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_search_property_list`.
     * @param ctx the parse tree
     */
    exitCreate_search_property_list?: (ctx: Create_search_property_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_security_policy`.
     * @param ctx the parse tree
     */
    enterCreate_security_policy?: (ctx: Create_security_policyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_security_policy`.
     * @param ctx the parse tree
     */
    exitCreate_security_policy?: (ctx: Create_security_policyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_sequence`.
     * @param ctx the parse tree
     */
    enterAlter_sequence?: (ctx: Alter_sequenceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_sequence`.
     * @param ctx the parse tree
     */
    exitAlter_sequence?: (ctx: Alter_sequenceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_sequence`.
     * @param ctx the parse tree
     */
    enterCreate_sequence?: (ctx: Create_sequenceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_sequence`.
     * @param ctx the parse tree
     */
    exitCreate_sequence?: (ctx: Create_sequenceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_server_audit`.
     * @param ctx the parse tree
     */
    enterAlter_server_audit?: (ctx: Alter_server_auditContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_server_audit`.
     * @param ctx the parse tree
     */
    exitAlter_server_audit?: (ctx: Alter_server_auditContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_server_audit`.
     * @param ctx the parse tree
     */
    enterCreate_server_audit?: (ctx: Create_server_auditContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_server_audit`.
     * @param ctx the parse tree
     */
    exitCreate_server_audit?: (ctx: Create_server_auditContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_server_audit_specification`.
     * @param ctx the parse tree
     */
    enterAlter_server_audit_specification?: (ctx: Alter_server_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_server_audit_specification`.
     * @param ctx the parse tree
     */
    exitAlter_server_audit_specification?: (ctx: Alter_server_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_server_audit_specification`.
     * @param ctx the parse tree
     */
    enterCreate_server_audit_specification?: (ctx: Create_server_audit_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_server_audit_specification`.
     * @param ctx the parse tree
     */
    exitCreate_server_audit_specification?: (ctx: Create_server_audit_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_server_configuration`.
     * @param ctx the parse tree
     */
    enterAlter_server_configuration?: (ctx: Alter_server_configurationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_server_configuration`.
     * @param ctx the parse tree
     */
    exitAlter_server_configuration?: (ctx: Alter_server_configurationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_server_role`.
     * @param ctx the parse tree
     */
    enterAlter_server_role?: (ctx: Alter_server_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_server_role`.
     * @param ctx the parse tree
     */
    exitAlter_server_role?: (ctx: Alter_server_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_server_role`.
     * @param ctx the parse tree
     */
    enterCreate_server_role?: (ctx: Create_server_roleContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_server_role`.
     * @param ctx the parse tree
     */
    exitCreate_server_role?: (ctx: Create_server_roleContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_server_role_pdw`.
     * @param ctx the parse tree
     */
    enterAlter_server_role_pdw?: (ctx: Alter_server_role_pdwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_server_role_pdw`.
     * @param ctx the parse tree
     */
    exitAlter_server_role_pdw?: (ctx: Alter_server_role_pdwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_service`.
     * @param ctx the parse tree
     */
    enterAlter_service?: (ctx: Alter_serviceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_service`.
     * @param ctx the parse tree
     */
    exitAlter_service?: (ctx: Alter_serviceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.opt_arg_clause`.
     * @param ctx the parse tree
     */
    enterOpt_arg_clause?: (ctx: Opt_arg_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.opt_arg_clause`.
     * @param ctx the parse tree
     */
    exitOpt_arg_clause?: (ctx: Opt_arg_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_service`.
     * @param ctx the parse tree
     */
    enterCreate_service?: (ctx: Create_serviceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_service`.
     * @param ctx the parse tree
     */
    exitCreate_service?: (ctx: Create_serviceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_service_master_key`.
     * @param ctx the parse tree
     */
    enterAlter_service_master_key?: (ctx: Alter_service_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_service_master_key`.
     * @param ctx the parse tree
     */
    exitAlter_service_master_key?: (ctx: Alter_service_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_symmetric_key`.
     * @param ctx the parse tree
     */
    enterAlter_symmetric_key?: (ctx: Alter_symmetric_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_symmetric_key`.
     * @param ctx the parse tree
     */
    exitAlter_symmetric_key?: (ctx: Alter_symmetric_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_synonym`.
     * @param ctx the parse tree
     */
    enterCreate_synonym?: (ctx: Create_synonymContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_synonym`.
     * @param ctx the parse tree
     */
    exitCreate_synonym?: (ctx: Create_synonymContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_user`.
     * @param ctx the parse tree
     */
    enterAlter_user?: (ctx: Alter_userContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_user`.
     * @param ctx the parse tree
     */
    exitAlter_user?: (ctx: Alter_userContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_user`.
     * @param ctx the parse tree
     */
    enterCreate_user?: (ctx: Create_userContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_user`.
     * @param ctx the parse tree
     */
    exitCreate_user?: (ctx: Create_userContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_user_azure_sql_dw`.
     * @param ctx the parse tree
     */
    enterCreate_user_azure_sql_dw?: (ctx: Create_user_azure_sql_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_user_azure_sql_dw`.
     * @param ctx the parse tree
     */
    exitCreate_user_azure_sql_dw?: (ctx: Create_user_azure_sql_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_user_azure_sql`.
     * @param ctx the parse tree
     */
    enterAlter_user_azure_sql?: (ctx: Alter_user_azure_sqlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_user_azure_sql`.
     * @param ctx the parse tree
     */
    exitAlter_user_azure_sql?: (ctx: Alter_user_azure_sqlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_workload_group`.
     * @param ctx the parse tree
     */
    enterAlter_workload_group?: (ctx: Alter_workload_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_workload_group`.
     * @param ctx the parse tree
     */
    exitAlter_workload_group?: (ctx: Alter_workload_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_workload_group`.
     * @param ctx the parse tree
     */
    enterCreate_workload_group?: (ctx: Create_workload_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_workload_group`.
     * @param ctx the parse tree
     */
    exitCreate_workload_group?: (ctx: Create_workload_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_xml_schema_collection`.
     * @param ctx the parse tree
     */
    enterCreate_xml_schema_collection?: (ctx: Create_xml_schema_collectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_xml_schema_collection`.
     * @param ctx the parse tree
     */
    exitCreate_xml_schema_collection?: (ctx: Create_xml_schema_collectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_partition_function`.
     * @param ctx the parse tree
     */
    enterCreate_partition_function?: (ctx: Create_partition_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_partition_function`.
     * @param ctx the parse tree
     */
    exitCreate_partition_function?: (ctx: Create_partition_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_partition_scheme`.
     * @param ctx the parse tree
     */
    enterCreate_partition_scheme?: (ctx: Create_partition_schemeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_partition_scheme`.
     * @param ctx the parse tree
     */
    exitCreate_partition_scheme?: (ctx: Create_partition_schemeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_queue`.
     * @param ctx the parse tree
     */
    enterCreate_queue?: (ctx: Create_queueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_queue`.
     * @param ctx the parse tree
     */
    exitCreate_queue?: (ctx: Create_queueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.queue_settings`.
     * @param ctx the parse tree
     */
    enterQueue_settings?: (ctx: Queue_settingsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.queue_settings`.
     * @param ctx the parse tree
     */
    exitQueue_settings?: (ctx: Queue_settingsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_queue`.
     * @param ctx the parse tree
     */
    enterAlter_queue?: (ctx: Alter_queueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_queue`.
     * @param ctx the parse tree
     */
    exitAlter_queue?: (ctx: Alter_queueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.queue_action`.
     * @param ctx the parse tree
     */
    enterQueue_action?: (ctx: Queue_actionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.queue_action`.
     * @param ctx the parse tree
     */
    exitQueue_action?: (ctx: Queue_actionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.queue_rebuild_options`.
     * @param ctx the parse tree
     */
    enterQueue_rebuild_options?: (ctx: Queue_rebuild_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.queue_rebuild_options`.
     * @param ctx the parse tree
     */
    exitQueue_rebuild_options?: (ctx: Queue_rebuild_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_contract`.
     * @param ctx the parse tree
     */
    enterCreate_contract?: (ctx: Create_contractContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_contract`.
     * @param ctx the parse tree
     */
    exitCreate_contract?: (ctx: Create_contractContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.conversation_statement`.
     * @param ctx the parse tree
     */
    enterConversation_statement?: (ctx: Conversation_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.conversation_statement`.
     * @param ctx the parse tree
     */
    exitConversation_statement?: (ctx: Conversation_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.message_statement`.
     * @param ctx the parse tree
     */
    enterMessage_statement?: (ctx: Message_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.message_statement`.
     * @param ctx the parse tree
     */
    exitMessage_statement?: (ctx: Message_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.merge_statement`.
     * @param ctx the parse tree
     */
    enterMerge_statement?: (ctx: Merge_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.merge_statement`.
     * @param ctx the parse tree
     */
    exitMerge_statement?: (ctx: Merge_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.when_matches`.
     * @param ctx the parse tree
     */
    enterWhen_matches?: (ctx: When_matchesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.when_matches`.
     * @param ctx the parse tree
     */
    exitWhen_matches?: (ctx: When_matchesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.merge_matched`.
     * @param ctx the parse tree
     */
    enterMerge_matched?: (ctx: Merge_matchedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.merge_matched`.
     * @param ctx the parse tree
     */
    exitMerge_matched?: (ctx: Merge_matchedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.merge_not_matched`.
     * @param ctx the parse tree
     */
    enterMerge_not_matched?: (ctx: Merge_not_matchedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.merge_not_matched`.
     * @param ctx the parse tree
     */
    exitMerge_not_matched?: (ctx: Merge_not_matchedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.delete_statement`.
     * @param ctx the parse tree
     */
    enterDelete_statement?: (ctx: Delete_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.delete_statement`.
     * @param ctx the parse tree
     */
    exitDelete_statement?: (ctx: Delete_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.delete_statement_from`.
     * @param ctx the parse tree
     */
    enterDelete_statement_from?: (ctx: Delete_statement_fromContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.delete_statement_from`.
     * @param ctx the parse tree
     */
    exitDelete_statement_from?: (ctx: Delete_statement_fromContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.insert_statement`.
     * @param ctx the parse tree
     */
    enterInsert_statement?: (ctx: Insert_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.insert_statement`.
     * @param ctx the parse tree
     */
    exitInsert_statement?: (ctx: Insert_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.insert_statement_value`.
     * @param ctx the parse tree
     */
    enterInsert_statement_value?: (ctx: Insert_statement_valueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.insert_statement_value`.
     * @param ctx the parse tree
     */
    exitInsert_statement_value?: (ctx: Insert_statement_valueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.receive_statement`.
     * @param ctx the parse tree
     */
    enterReceive_statement?: (ctx: Receive_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.receive_statement`.
     * @param ctx the parse tree
     */
    exitReceive_statement?: (ctx: Receive_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.select_statement_standalone`.
     * @param ctx the parse tree
     */
    enterSelect_statement_standalone?: (ctx: Select_statement_standaloneContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.select_statement_standalone`.
     * @param ctx the parse tree
     */
    exitSelect_statement_standalone?: (ctx: Select_statement_standaloneContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.select_statement`.
     * @param ctx the parse tree
     */
    enterSelect_statement?: (ctx: Select_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.select_statement`.
     * @param ctx the parse tree
     */
    exitSelect_statement?: (ctx: Select_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.time`.
     * @param ctx the parse tree
     */
    enterTime?: (ctx: TimeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.time`.
     * @param ctx the parse tree
     */
    exitTime?: (ctx: TimeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_statement`.
     * @param ctx the parse tree
     */
    enterUpdate_statement?: (ctx: Update_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_statement`.
     * @param ctx the parse tree
     */
    exitUpdate_statement?: (ctx: Update_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.output_clause`.
     * @param ctx the parse tree
     */
    enterOutput_clause?: (ctx: Output_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.output_clause`.
     * @param ctx the parse tree
     */
    exitOutput_clause?: (ctx: Output_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.output_dml_list_elem`.
     * @param ctx the parse tree
     */
    enterOutput_dml_list_elem?: (ctx: Output_dml_list_elemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.output_dml_list_elem`.
     * @param ctx the parse tree
     */
    exitOutput_dml_list_elem?: (ctx: Output_dml_list_elemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_database`.
     * @param ctx the parse tree
     */
    enterCreate_database?: (ctx: Create_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_database`.
     * @param ctx the parse tree
     */
    exitCreate_database?: (ctx: Create_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_index`.
     * @param ctx the parse tree
     */
    enterCreate_index?: (ctx: Create_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_index`.
     * @param ctx the parse tree
     */
    exitCreate_index?: (ctx: Create_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_index_options`.
     * @param ctx the parse tree
     */
    enterCreate_index_options?: (ctx: Create_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_index_options`.
     * @param ctx the parse tree
     */
    exitCreate_index_options?: (ctx: Create_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.relational_index_option`.
     * @param ctx the parse tree
     */
    enterRelational_index_option?: (ctx: Relational_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.relational_index_option`.
     * @param ctx the parse tree
     */
    exitRelational_index_option?: (ctx: Relational_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_index`.
     * @param ctx the parse tree
     */
    enterAlter_index?: (ctx: Alter_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_index`.
     * @param ctx the parse tree
     */
    exitAlter_index?: (ctx: Alter_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.resumable_index_options`.
     * @param ctx the parse tree
     */
    enterResumable_index_options?: (ctx: Resumable_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.resumable_index_options`.
     * @param ctx the parse tree
     */
    exitResumable_index_options?: (ctx: Resumable_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.resumable_index_option`.
     * @param ctx the parse tree
     */
    enterResumable_index_option?: (ctx: Resumable_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.resumable_index_option`.
     * @param ctx the parse tree
     */
    exitResumable_index_option?: (ctx: Resumable_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.reorganize_partition`.
     * @param ctx the parse tree
     */
    enterReorganize_partition?: (ctx: Reorganize_partitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.reorganize_partition`.
     * @param ctx the parse tree
     */
    exitReorganize_partition?: (ctx: Reorganize_partitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.reorganize_options`.
     * @param ctx the parse tree
     */
    enterReorganize_options?: (ctx: Reorganize_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.reorganize_options`.
     * @param ctx the parse tree
     */
    exitReorganize_options?: (ctx: Reorganize_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.reorganize_option`.
     * @param ctx the parse tree
     */
    enterReorganize_option?: (ctx: Reorganize_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.reorganize_option`.
     * @param ctx the parse tree
     */
    exitReorganize_option?: (ctx: Reorganize_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.set_index_options`.
     * @param ctx the parse tree
     */
    enterSet_index_options?: (ctx: Set_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.set_index_options`.
     * @param ctx the parse tree
     */
    exitSet_index_options?: (ctx: Set_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.set_index_option`.
     * @param ctx the parse tree
     */
    enterSet_index_option?: (ctx: Set_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.set_index_option`.
     * @param ctx the parse tree
     */
    exitSet_index_option?: (ctx: Set_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.rebuild_partition`.
     * @param ctx the parse tree
     */
    enterRebuild_partition?: (ctx: Rebuild_partitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.rebuild_partition`.
     * @param ctx the parse tree
     */
    exitRebuild_partition?: (ctx: Rebuild_partitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.rebuild_index_options`.
     * @param ctx the parse tree
     */
    enterRebuild_index_options?: (ctx: Rebuild_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.rebuild_index_options`.
     * @param ctx the parse tree
     */
    exitRebuild_index_options?: (ctx: Rebuild_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.rebuild_index_option`.
     * @param ctx the parse tree
     */
    enterRebuild_index_option?: (ctx: Rebuild_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.rebuild_index_option`.
     * @param ctx the parse tree
     */
    exitRebuild_index_option?: (ctx: Rebuild_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.single_partition_rebuild_index_options`.
     * @param ctx the parse tree
     */
    enterSingle_partition_rebuild_index_options?: (ctx: Single_partition_rebuild_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.single_partition_rebuild_index_options`.
     * @param ctx the parse tree
     */
    exitSingle_partition_rebuild_index_options?: (ctx: Single_partition_rebuild_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.single_partition_rebuild_index_option`.
     * @param ctx the parse tree
     */
    enterSingle_partition_rebuild_index_option?: (ctx: Single_partition_rebuild_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.single_partition_rebuild_index_option`.
     * @param ctx the parse tree
     */
    exitSingle_partition_rebuild_index_option?: (ctx: Single_partition_rebuild_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.on_partitions`.
     * @param ctx the parse tree
     */
    enterOn_partitions?: (ctx: On_partitionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.on_partitions`.
     * @param ctx the parse tree
     */
    exitOn_partitions?: (ctx: On_partitionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_columnstore_index`.
     * @param ctx the parse tree
     */
    enterCreate_columnstore_index?: (ctx: Create_columnstore_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_columnstore_index`.
     * @param ctx the parse tree
     */
    exitCreate_columnstore_index?: (ctx: Create_columnstore_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_columnstore_index_options`.
     * @param ctx the parse tree
     */
    enterCreate_columnstore_index_options?: (ctx: Create_columnstore_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_columnstore_index_options`.
     * @param ctx the parse tree
     */
    exitCreate_columnstore_index_options?: (ctx: Create_columnstore_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.columnstore_index_option`.
     * @param ctx the parse tree
     */
    enterColumnstore_index_option?: (ctx: Columnstore_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.columnstore_index_option`.
     * @param ctx the parse tree
     */
    exitColumnstore_index_option?: (ctx: Columnstore_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_nonclustered_columnstore_index`.
     * @param ctx the parse tree
     */
    enterCreate_nonclustered_columnstore_index?: (ctx: Create_nonclustered_columnstore_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_nonclustered_columnstore_index`.
     * @param ctx the parse tree
     */
    exitCreate_nonclustered_columnstore_index?: (ctx: Create_nonclustered_columnstore_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_xml_index`.
     * @param ctx the parse tree
     */
    enterCreate_xml_index?: (ctx: Create_xml_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_xml_index`.
     * @param ctx the parse tree
     */
    exitCreate_xml_index?: (ctx: Create_xml_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_index_options`.
     * @param ctx the parse tree
     */
    enterXml_index_options?: (ctx: Xml_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_index_options`.
     * @param ctx the parse tree
     */
    exitXml_index_options?: (ctx: Xml_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_index_option`.
     * @param ctx the parse tree
     */
    enterXml_index_option?: (ctx: Xml_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_index_option`.
     * @param ctx the parse tree
     */
    exitXml_index_option?: (ctx: Xml_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_procedure`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_procedure?: (ctx: Create_or_alter_procedureContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_procedure`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_procedure?: (ctx: Create_or_alter_procedureContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.as_external_name`.
     * @param ctx the parse tree
     */
    enterAs_external_name?: (ctx: As_external_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.as_external_name`.
     * @param ctx the parse tree
     */
    exitAs_external_name?: (ctx: As_external_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_trigger`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_trigger?: (ctx: Create_or_alter_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_trigger`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_trigger?: (ctx: Create_or_alter_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_dml_trigger`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_dml_trigger?: (ctx: Create_or_alter_dml_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_dml_trigger`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_dml_trigger?: (ctx: Create_or_alter_dml_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dml_trigger_option`.
     * @param ctx the parse tree
     */
    enterDml_trigger_option?: (ctx: Dml_trigger_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dml_trigger_option`.
     * @param ctx the parse tree
     */
    exitDml_trigger_option?: (ctx: Dml_trigger_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dml_trigger_operation`.
     * @param ctx the parse tree
     */
    enterDml_trigger_operation?: (ctx: Dml_trigger_operationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dml_trigger_operation`.
     * @param ctx the parse tree
     */
    exitDml_trigger_operation?: (ctx: Dml_trigger_operationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_ddl_trigger`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_ddl_trigger?: (ctx: Create_or_alter_ddl_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_ddl_trigger`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_ddl_trigger?: (ctx: Create_or_alter_ddl_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ddl_trigger_operation`.
     * @param ctx the parse tree
     */
    enterDdl_trigger_operation?: (ctx: Ddl_trigger_operationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ddl_trigger_operation`.
     * @param ctx the parse tree
     */
    exitDdl_trigger_operation?: (ctx: Ddl_trigger_operationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_or_alter_function`.
     * @param ctx the parse tree
     */
    enterCreate_or_alter_function?: (ctx: Create_or_alter_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_or_alter_function`.
     * @param ctx the parse tree
     */
    exitCreate_or_alter_function?: (ctx: Create_or_alter_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_body_returns_select`.
     * @param ctx the parse tree
     */
    enterFunc_body_returns_select?: (ctx: Func_body_returns_selectContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_body_returns_select`.
     * @param ctx the parse tree
     */
    exitFunc_body_returns_select?: (ctx: Func_body_returns_selectContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_body_returns_table`.
     * @param ctx the parse tree
     */
    enterFunc_body_returns_table?: (ctx: Func_body_returns_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_body_returns_table`.
     * @param ctx the parse tree
     */
    exitFunc_body_returns_table?: (ctx: Func_body_returns_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_body_returns_scalar`.
     * @param ctx the parse tree
     */
    enterFunc_body_returns_scalar?: (ctx: Func_body_returns_scalarContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_body_returns_scalar`.
     * @param ctx the parse tree
     */
    exitFunc_body_returns_scalar?: (ctx: Func_body_returns_scalarContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.procedure_param_default_value`.
     * @param ctx the parse tree
     */
    enterProcedure_param_default_value?: (ctx: Procedure_param_default_valueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.procedure_param_default_value`.
     * @param ctx the parse tree
     */
    exitProcedure_param_default_value?: (ctx: Procedure_param_default_valueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.procedure_param`.
     * @param ctx the parse tree
     */
    enterProcedure_param?: (ctx: Procedure_paramContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.procedure_param`.
     * @param ctx the parse tree
     */
    exitProcedure_param?: (ctx: Procedure_paramContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.procedure_option`.
     * @param ctx the parse tree
     */
    enterProcedure_option?: (ctx: Procedure_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.procedure_option`.
     * @param ctx the parse tree
     */
    exitProcedure_option?: (ctx: Procedure_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.function_option`.
     * @param ctx the parse tree
     */
    enterFunction_option?: (ctx: Function_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.function_option`.
     * @param ctx the parse tree
     */
    exitFunction_option?: (ctx: Function_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_statistics`.
     * @param ctx the parse tree
     */
    enterCreate_statistics?: (ctx: Create_statisticsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_statistics`.
     * @param ctx the parse tree
     */
    exitCreate_statistics?: (ctx: Create_statisticsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_statistics`.
     * @param ctx the parse tree
     */
    enterUpdate_statistics?: (ctx: Update_statisticsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_statistics`.
     * @param ctx the parse tree
     */
    exitUpdate_statistics?: (ctx: Update_statisticsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_statistics_options`.
     * @param ctx the parse tree
     */
    enterUpdate_statistics_options?: (ctx: Update_statistics_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_statistics_options`.
     * @param ctx the parse tree
     */
    exitUpdate_statistics_options?: (ctx: Update_statistics_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_statistics_option`.
     * @param ctx the parse tree
     */
    enterUpdate_statistics_option?: (ctx: Update_statistics_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_statistics_option`.
     * @param ctx the parse tree
     */
    exitUpdate_statistics_option?: (ctx: Update_statistics_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_table`.
     * @param ctx the parse tree
     */
    enterCreate_table?: (ctx: Create_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_table`.
     * @param ctx the parse tree
     */
    exitCreate_table?: (ctx: Create_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_indices`.
     * @param ctx the parse tree
     */
    enterTable_indices?: (ctx: Table_indicesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_indices`.
     * @param ctx the parse tree
     */
    exitTable_indices?: (ctx: Table_indicesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_options`.
     * @param ctx the parse tree
     */
    enterTable_options?: (ctx: Table_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_options`.
     * @param ctx the parse tree
     */
    exitTable_options?: (ctx: Table_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_option`.
     * @param ctx the parse tree
     */
    enterTable_option?: (ctx: Table_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_option`.
     * @param ctx the parse tree
     */
    exitTable_option?: (ctx: Table_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_table_index_options`.
     * @param ctx the parse tree
     */
    enterCreate_table_index_options?: (ctx: Create_table_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_table_index_options`.
     * @param ctx the parse tree
     */
    exitCreate_table_index_options?: (ctx: Create_table_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_table_index_option`.
     * @param ctx the parse tree
     */
    enterCreate_table_index_option?: (ctx: Create_table_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_table_index_option`.
     * @param ctx the parse tree
     */
    exitCreate_table_index_option?: (ctx: Create_table_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_view`.
     * @param ctx the parse tree
     */
    enterCreate_view?: (ctx: Create_viewContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_view`.
     * @param ctx the parse tree
     */
    exitCreate_view?: (ctx: Create_viewContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.view_attribute`.
     * @param ctx the parse tree
     */
    enterView_attribute?: (ctx: View_attributeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.view_attribute`.
     * @param ctx the parse tree
     */
    exitView_attribute?: (ctx: View_attributeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_table`.
     * @param ctx the parse tree
     */
    enterAlter_table?: (ctx: Alter_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_table`.
     * @param ctx the parse tree
     */
    exitAlter_table?: (ctx: Alter_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.switch_partition`.
     * @param ctx the parse tree
     */
    enterSwitch_partition?: (ctx: Switch_partitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.switch_partition`.
     * @param ctx the parse tree
     */
    exitSwitch_partition?: (ctx: Switch_partitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.low_priority_lock_wait`.
     * @param ctx the parse tree
     */
    enterLow_priority_lock_wait?: (ctx: Low_priority_lock_waitContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.low_priority_lock_wait`.
     * @param ctx the parse tree
     */
    exitLow_priority_lock_wait?: (ctx: Low_priority_lock_waitContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_database`.
     * @param ctx the parse tree
     */
    enterAlter_database?: (ctx: Alter_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_database`.
     * @param ctx the parse tree
     */
    exitAlter_database?: (ctx: Alter_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.add_or_modify_files`.
     * @param ctx the parse tree
     */
    enterAdd_or_modify_files?: (ctx: Add_or_modify_filesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.add_or_modify_files`.
     * @param ctx the parse tree
     */
    exitAdd_or_modify_files?: (ctx: Add_or_modify_filesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.filespec`.
     * @param ctx the parse tree
     */
    enterFilespec?: (ctx: FilespecContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.filespec`.
     * @param ctx the parse tree
     */
    exitFilespec?: (ctx: FilespecContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.add_or_modify_filegroups`.
     * @param ctx the parse tree
     */
    enterAdd_or_modify_filegroups?: (ctx: Add_or_modify_filegroupsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.add_or_modify_filegroups`.
     * @param ctx the parse tree
     */
    exitAdd_or_modify_filegroups?: (ctx: Add_or_modify_filegroupsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.filegroup_updatability_option`.
     * @param ctx the parse tree
     */
    enterFilegroup_updatability_option?: (ctx: Filegroup_updatability_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.filegroup_updatability_option`.
     * @param ctx the parse tree
     */
    exitFilegroup_updatability_option?: (ctx: Filegroup_updatability_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.database_optionspec`.
     * @param ctx the parse tree
     */
    enterDatabase_optionspec?: (ctx: Database_optionspecContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.database_optionspec`.
     * @param ctx the parse tree
     */
    exitDatabase_optionspec?: (ctx: Database_optionspecContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.auto_option`.
     * @param ctx the parse tree
     */
    enterAuto_option?: (ctx: Auto_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.auto_option`.
     * @param ctx the parse tree
     */
    exitAuto_option?: (ctx: Auto_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.change_tracking_option`.
     * @param ctx the parse tree
     */
    enterChange_tracking_option?: (ctx: Change_tracking_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.change_tracking_option`.
     * @param ctx the parse tree
     */
    exitChange_tracking_option?: (ctx: Change_tracking_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.change_tracking_option_list`.
     * @param ctx the parse tree
     */
    enterChange_tracking_option_list?: (ctx: Change_tracking_option_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.change_tracking_option_list`.
     * @param ctx the parse tree
     */
    exitChange_tracking_option_list?: (ctx: Change_tracking_option_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.containment_option`.
     * @param ctx the parse tree
     */
    enterContainment_option?: (ctx: Containment_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.containment_option`.
     * @param ctx the parse tree
     */
    exitContainment_option?: (ctx: Containment_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.cursor_option`.
     * @param ctx the parse tree
     */
    enterCursor_option?: (ctx: Cursor_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.cursor_option`.
     * @param ctx the parse tree
     */
    exitCursor_option?: (ctx: Cursor_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_endpoint`.
     * @param ctx the parse tree
     */
    enterAlter_endpoint?: (ctx: Alter_endpointContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_endpoint`.
     * @param ctx the parse tree
     */
    exitAlter_endpoint?: (ctx: Alter_endpointContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.database_mirroring_option`.
     * @param ctx the parse tree
     */
    enterDatabase_mirroring_option?: (ctx: Database_mirroring_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.database_mirroring_option`.
     * @param ctx the parse tree
     */
    exitDatabase_mirroring_option?: (ctx: Database_mirroring_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.mirroring_set_option`.
     * @param ctx the parse tree
     */
    enterMirroring_set_option?: (ctx: Mirroring_set_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.mirroring_set_option`.
     * @param ctx the parse tree
     */
    exitMirroring_set_option?: (ctx: Mirroring_set_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.mirroring_partner`.
     * @param ctx the parse tree
     */
    enterMirroring_partner?: (ctx: Mirroring_partnerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.mirroring_partner`.
     * @param ctx the parse tree
     */
    exitMirroring_partner?: (ctx: Mirroring_partnerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.mirroring_witness`.
     * @param ctx the parse tree
     */
    enterMirroring_witness?: (ctx: Mirroring_witnessContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.mirroring_witness`.
     * @param ctx the parse tree
     */
    exitMirroring_witness?: (ctx: Mirroring_witnessContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.witness_partner_equal`.
     * @param ctx the parse tree
     */
    enterWitness_partner_equal?: (ctx: Witness_partner_equalContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.witness_partner_equal`.
     * @param ctx the parse tree
     */
    exitWitness_partner_equal?: (ctx: Witness_partner_equalContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.partner_option`.
     * @param ctx the parse tree
     */
    enterPartner_option?: (ctx: Partner_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.partner_option`.
     * @param ctx the parse tree
     */
    exitPartner_option?: (ctx: Partner_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.witness_option`.
     * @param ctx the parse tree
     */
    enterWitness_option?: (ctx: Witness_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.witness_option`.
     * @param ctx the parse tree
     */
    exitWitness_option?: (ctx: Witness_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.witness_server`.
     * @param ctx the parse tree
     */
    enterWitness_server?: (ctx: Witness_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.witness_server`.
     * @param ctx the parse tree
     */
    exitWitness_server?: (ctx: Witness_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.partner_server`.
     * @param ctx the parse tree
     */
    enterPartner_server?: (ctx: Partner_serverContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.partner_server`.
     * @param ctx the parse tree
     */
    exitPartner_server?: (ctx: Partner_serverContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.mirroring_host_port_seperator`.
     * @param ctx the parse tree
     */
    enterMirroring_host_port_seperator?: (ctx: Mirroring_host_port_seperatorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.mirroring_host_port_seperator`.
     * @param ctx the parse tree
     */
    exitMirroring_host_port_seperator?: (ctx: Mirroring_host_port_seperatorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.partner_server_tcp_prefix`.
     * @param ctx the parse tree
     */
    enterPartner_server_tcp_prefix?: (ctx: Partner_server_tcp_prefixContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.partner_server_tcp_prefix`.
     * @param ctx the parse tree
     */
    exitPartner_server_tcp_prefix?: (ctx: Partner_server_tcp_prefixContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.port_number`.
     * @param ctx the parse tree
     */
    enterPort_number?: (ctx: Port_numberContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.port_number`.
     * @param ctx the parse tree
     */
    exitPort_number?: (ctx: Port_numberContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.host`.
     * @param ctx the parse tree
     */
    enterHost?: (ctx: HostContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.host`.
     * @param ctx the parse tree
     */
    exitHost?: (ctx: HostContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.date_correlation_optimization_option`.
     * @param ctx the parse tree
     */
    enterDate_correlation_optimization_option?: (ctx: Date_correlation_optimization_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.date_correlation_optimization_option`.
     * @param ctx the parse tree
     */
    exitDate_correlation_optimization_option?: (ctx: Date_correlation_optimization_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.db_encryption_option`.
     * @param ctx the parse tree
     */
    enterDb_encryption_option?: (ctx: Db_encryption_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.db_encryption_option`.
     * @param ctx the parse tree
     */
    exitDb_encryption_option?: (ctx: Db_encryption_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.db_state_option`.
     * @param ctx the parse tree
     */
    enterDb_state_option?: (ctx: Db_state_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.db_state_option`.
     * @param ctx the parse tree
     */
    exitDb_state_option?: (ctx: Db_state_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.db_update_option`.
     * @param ctx the parse tree
     */
    enterDb_update_option?: (ctx: Db_update_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.db_update_option`.
     * @param ctx the parse tree
     */
    exitDb_update_option?: (ctx: Db_update_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.db_user_access_option`.
     * @param ctx the parse tree
     */
    enterDb_user_access_option?: (ctx: Db_user_access_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.db_user_access_option`.
     * @param ctx the parse tree
     */
    exitDb_user_access_option?: (ctx: Db_user_access_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.delayed_durability_option`.
     * @param ctx the parse tree
     */
    enterDelayed_durability_option?: (ctx: Delayed_durability_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.delayed_durability_option`.
     * @param ctx the parse tree
     */
    exitDelayed_durability_option?: (ctx: Delayed_durability_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.external_access_option`.
     * @param ctx the parse tree
     */
    enterExternal_access_option?: (ctx: External_access_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.external_access_option`.
     * @param ctx the parse tree
     */
    exitExternal_access_option?: (ctx: External_access_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.hadr_options`.
     * @param ctx the parse tree
     */
    enterHadr_options?: (ctx: Hadr_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.hadr_options`.
     * @param ctx the parse tree
     */
    exitHadr_options?: (ctx: Hadr_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.mixed_page_allocation_option`.
     * @param ctx the parse tree
     */
    enterMixed_page_allocation_option?: (ctx: Mixed_page_allocation_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.mixed_page_allocation_option`.
     * @param ctx the parse tree
     */
    exitMixed_page_allocation_option?: (ctx: Mixed_page_allocation_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.parameterization_option`.
     * @param ctx the parse tree
     */
    enterParameterization_option?: (ctx: Parameterization_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.parameterization_option`.
     * @param ctx the parse tree
     */
    exitParameterization_option?: (ctx: Parameterization_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.recovery_option`.
     * @param ctx the parse tree
     */
    enterRecovery_option?: (ctx: Recovery_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.recovery_option`.
     * @param ctx the parse tree
     */
    exitRecovery_option?: (ctx: Recovery_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.service_broker_option`.
     * @param ctx the parse tree
     */
    enterService_broker_option?: (ctx: Service_broker_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.service_broker_option`.
     * @param ctx the parse tree
     */
    exitService_broker_option?: (ctx: Service_broker_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.snapshot_option`.
     * @param ctx the parse tree
     */
    enterSnapshot_option?: (ctx: Snapshot_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.snapshot_option`.
     * @param ctx the parse tree
     */
    exitSnapshot_option?: (ctx: Snapshot_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.sql_option`.
     * @param ctx the parse tree
     */
    enterSql_option?: (ctx: Sql_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.sql_option`.
     * @param ctx the parse tree
     */
    exitSql_option?: (ctx: Sql_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.target_recovery_time_option`.
     * @param ctx the parse tree
     */
    enterTarget_recovery_time_option?: (ctx: Target_recovery_time_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.target_recovery_time_option`.
     * @param ctx the parse tree
     */
    exitTarget_recovery_time_option?: (ctx: Target_recovery_time_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.termination`.
     * @param ctx the parse tree
     */
    enterTermination?: (ctx: TerminationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.termination`.
     * @param ctx the parse tree
     */
    exitTermination?: (ctx: TerminationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_index`.
     * @param ctx the parse tree
     */
    enterDrop_index?: (ctx: Drop_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_index`.
     * @param ctx the parse tree
     */
    exitDrop_index?: (ctx: Drop_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_relational_or_xml_or_spatial_index`.
     * @param ctx the parse tree
     */
    enterDrop_relational_or_xml_or_spatial_index?: (ctx: Drop_relational_or_xml_or_spatial_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_relational_or_xml_or_spatial_index`.
     * @param ctx the parse tree
     */
    exitDrop_relational_or_xml_or_spatial_index?: (ctx: Drop_relational_or_xml_or_spatial_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_backward_compatible_index`.
     * @param ctx the parse tree
     */
    enterDrop_backward_compatible_index?: (ctx: Drop_backward_compatible_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_backward_compatible_index`.
     * @param ctx the parse tree
     */
    exitDrop_backward_compatible_index?: (ctx: Drop_backward_compatible_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_procedure`.
     * @param ctx the parse tree
     */
    enterDrop_procedure?: (ctx: Drop_procedureContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_procedure`.
     * @param ctx the parse tree
     */
    exitDrop_procedure?: (ctx: Drop_procedureContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_trigger`.
     * @param ctx the parse tree
     */
    enterDrop_trigger?: (ctx: Drop_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_trigger`.
     * @param ctx the parse tree
     */
    exitDrop_trigger?: (ctx: Drop_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_dml_trigger`.
     * @param ctx the parse tree
     */
    enterDrop_dml_trigger?: (ctx: Drop_dml_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_dml_trigger`.
     * @param ctx the parse tree
     */
    exitDrop_dml_trigger?: (ctx: Drop_dml_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_ddl_trigger`.
     * @param ctx the parse tree
     */
    enterDrop_ddl_trigger?: (ctx: Drop_ddl_triggerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_ddl_trigger`.
     * @param ctx the parse tree
     */
    exitDrop_ddl_trigger?: (ctx: Drop_ddl_triggerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_function`.
     * @param ctx the parse tree
     */
    enterDrop_function?: (ctx: Drop_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_function`.
     * @param ctx the parse tree
     */
    exitDrop_function?: (ctx: Drop_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_statistics`.
     * @param ctx the parse tree
     */
    enterDrop_statistics?: (ctx: Drop_statisticsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_statistics`.
     * @param ctx the parse tree
     */
    exitDrop_statistics?: (ctx: Drop_statisticsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_table`.
     * @param ctx the parse tree
     */
    enterDrop_table?: (ctx: Drop_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_table`.
     * @param ctx the parse tree
     */
    exitDrop_table?: (ctx: Drop_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_view`.
     * @param ctx the parse tree
     */
    enterDrop_view?: (ctx: Drop_viewContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_view`.
     * @param ctx the parse tree
     */
    exitDrop_view?: (ctx: Drop_viewContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_type`.
     * @param ctx the parse tree
     */
    enterCreate_type?: (ctx: Create_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_type`.
     * @param ctx the parse tree
     */
    exitCreate_type?: (ctx: Create_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.drop_type`.
     * @param ctx the parse tree
     */
    enterDrop_type?: (ctx: Drop_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.drop_type`.
     * @param ctx the parse tree
     */
    exitDrop_type?: (ctx: Drop_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.rowset_function_limited`.
     * @param ctx the parse tree
     */
    enterRowset_function_limited?: (ctx: Rowset_function_limitedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.rowset_function_limited`.
     * @param ctx the parse tree
     */
    exitRowset_function_limited?: (ctx: Rowset_function_limitedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.openquery`.
     * @param ctx the parse tree
     */
    enterOpenquery?: (ctx: OpenqueryContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.openquery`.
     * @param ctx the parse tree
     */
    exitOpenquery?: (ctx: OpenqueryContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.opendatasource`.
     * @param ctx the parse tree
     */
    enterOpendatasource?: (ctx: OpendatasourceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.opendatasource`.
     * @param ctx the parse tree
     */
    exitOpendatasource?: (ctx: OpendatasourceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.declare_statement`.
     * @param ctx the parse tree
     */
    enterDeclare_statement?: (ctx: Declare_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.declare_statement`.
     * @param ctx the parse tree
     */
    exitDeclare_statement?: (ctx: Declare_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_declaration`.
     * @param ctx the parse tree
     */
    enterXml_declaration?: (ctx: Xml_declarationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_declaration`.
     * @param ctx the parse tree
     */
    exitXml_declaration?: (ctx: Xml_declarationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.cursor_statement`.
     * @param ctx the parse tree
     */
    enterCursor_statement?: (ctx: Cursor_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.cursor_statement`.
     * @param ctx the parse tree
     */
    exitCursor_statement?: (ctx: Cursor_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_database`.
     * @param ctx the parse tree
     */
    enterBackup_database?: (ctx: Backup_databaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_database`.
     * @param ctx the parse tree
     */
    exitBackup_database?: (ctx: Backup_databaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_log`.
     * @param ctx the parse tree
     */
    enterBackup_log?: (ctx: Backup_logContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_log`.
     * @param ctx the parse tree
     */
    exitBackup_log?: (ctx: Backup_logContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_certificate`.
     * @param ctx the parse tree
     */
    enterBackup_certificate?: (ctx: Backup_certificateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_certificate`.
     * @param ctx the parse tree
     */
    exitBackup_certificate?: (ctx: Backup_certificateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_master_key`.
     * @param ctx the parse tree
     */
    enterBackup_master_key?: (ctx: Backup_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_master_key`.
     * @param ctx the parse tree
     */
    exitBackup_master_key?: (ctx: Backup_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.backup_service_master_key`.
     * @param ctx the parse tree
     */
    enterBackup_service_master_key?: (ctx: Backup_service_master_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.backup_service_master_key`.
     * @param ctx the parse tree
     */
    exitBackup_service_master_key?: (ctx: Backup_service_master_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.kill_statement`.
     * @param ctx the parse tree
     */
    enterKill_statement?: (ctx: Kill_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.kill_statement`.
     * @param ctx the parse tree
     */
    exitKill_statement?: (ctx: Kill_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.kill_process`.
     * @param ctx the parse tree
     */
    enterKill_process?: (ctx: Kill_processContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.kill_process`.
     * @param ctx the parse tree
     */
    exitKill_process?: (ctx: Kill_processContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.kill_query_notification`.
     * @param ctx the parse tree
     */
    enterKill_query_notification?: (ctx: Kill_query_notificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.kill_query_notification`.
     * @param ctx the parse tree
     */
    exitKill_query_notification?: (ctx: Kill_query_notificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.kill_stats_job`.
     * @param ctx the parse tree
     */
    enterKill_stats_job?: (ctx: Kill_stats_jobContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.kill_stats_job`.
     * @param ctx the parse tree
     */
    exitKill_stats_job?: (ctx: Kill_stats_jobContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_statement`.
     * @param ctx the parse tree
     */
    enterExecute_statement?: (ctx: Execute_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_statement`.
     * @param ctx the parse tree
     */
    exitExecute_statement?: (ctx: Execute_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_body_batch`.
     * @param ctx the parse tree
     */
    enterExecute_body_batch?: (ctx: Execute_body_batchContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_body_batch`.
     * @param ctx the parse tree
     */
    exitExecute_body_batch?: (ctx: Execute_body_batchContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_body`.
     * @param ctx the parse tree
     */
    enterExecute_body?: (ctx: Execute_bodyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_body`.
     * @param ctx the parse tree
     */
    exitExecute_body?: (ctx: Execute_bodyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_statement_arg`.
     * @param ctx the parse tree
     */
    enterExecute_statement_arg?: (ctx: Execute_statement_argContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_statement_arg`.
     * @param ctx the parse tree
     */
    exitExecute_statement_arg?: (ctx: Execute_statement_argContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_statement_arg_named`.
     * @param ctx the parse tree
     */
    enterExecute_statement_arg_named?: (ctx: Execute_statement_arg_namedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_statement_arg_named`.
     * @param ctx the parse tree
     */
    exitExecute_statement_arg_named?: (ctx: Execute_statement_arg_namedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_statement_arg_unnamed`.
     * @param ctx the parse tree
     */
    enterExecute_statement_arg_unnamed?: (ctx: Execute_statement_arg_unnamedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_statement_arg_unnamed`.
     * @param ctx the parse tree
     */
    exitExecute_statement_arg_unnamed?: (ctx: Execute_statement_arg_unnamedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_parameter`.
     * @param ctx the parse tree
     */
    enterExecute_parameter?: (ctx: Execute_parameterContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_parameter`.
     * @param ctx the parse tree
     */
    exitExecute_parameter?: (ctx: Execute_parameterContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_var_string`.
     * @param ctx the parse tree
     */
    enterExecute_var_string?: (ctx: Execute_var_stringContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_var_string`.
     * @param ctx the parse tree
     */
    exitExecute_var_string?: (ctx: Execute_var_stringContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.security_statement`.
     * @param ctx the parse tree
     */
    enterSecurity_statement?: (ctx: Security_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.security_statement`.
     * @param ctx the parse tree
     */
    exitSecurity_statement?: (ctx: Security_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.principal_id`.
     * @param ctx the parse tree
     */
    enterPrincipal_id?: (ctx: Principal_idContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.principal_id`.
     * @param ctx the parse tree
     */
    exitPrincipal_id?: (ctx: Principal_idContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_certificate`.
     * @param ctx the parse tree
     */
    enterCreate_certificate?: (ctx: Create_certificateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_certificate`.
     * @param ctx the parse tree
     */
    exitCreate_certificate?: (ctx: Create_certificateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.existing_keys`.
     * @param ctx the parse tree
     */
    enterExisting_keys?: (ctx: Existing_keysContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.existing_keys`.
     * @param ctx the parse tree
     */
    exitExisting_keys?: (ctx: Existing_keysContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.private_key_options`.
     * @param ctx the parse tree
     */
    enterPrivate_key_options?: (ctx: Private_key_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.private_key_options`.
     * @param ctx the parse tree
     */
    exitPrivate_key_options?: (ctx: Private_key_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.generate_new_keys`.
     * @param ctx the parse tree
     */
    enterGenerate_new_keys?: (ctx: Generate_new_keysContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.generate_new_keys`.
     * @param ctx the parse tree
     */
    exitGenerate_new_keys?: (ctx: Generate_new_keysContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.date_options`.
     * @param ctx the parse tree
     */
    enterDate_options?: (ctx: Date_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.date_options`.
     * @param ctx the parse tree
     */
    exitDate_options?: (ctx: Date_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.open_key`.
     * @param ctx the parse tree
     */
    enterOpen_key?: (ctx: Open_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.open_key`.
     * @param ctx the parse tree
     */
    exitOpen_key?: (ctx: Open_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.close_key`.
     * @param ctx the parse tree
     */
    enterClose_key?: (ctx: Close_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.close_key`.
     * @param ctx the parse tree
     */
    exitClose_key?: (ctx: Close_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_key`.
     * @param ctx the parse tree
     */
    enterCreate_key?: (ctx: Create_keyContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_key`.
     * @param ctx the parse tree
     */
    exitCreate_key?: (ctx: Create_keyContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.key_options`.
     * @param ctx the parse tree
     */
    enterKey_options?: (ctx: Key_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.key_options`.
     * @param ctx the parse tree
     */
    exitKey_options?: (ctx: Key_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.algorithm`.
     * @param ctx the parse tree
     */
    enterAlgorithm?: (ctx: AlgorithmContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.algorithm`.
     * @param ctx the parse tree
     */
    exitAlgorithm?: (ctx: AlgorithmContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.encryption_mechanism`.
     * @param ctx the parse tree
     */
    enterEncryption_mechanism?: (ctx: Encryption_mechanismContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.encryption_mechanism`.
     * @param ctx the parse tree
     */
    exitEncryption_mechanism?: (ctx: Encryption_mechanismContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.decryption_mechanism`.
     * @param ctx the parse tree
     */
    enterDecryption_mechanism?: (ctx: Decryption_mechanismContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.decryption_mechanism`.
     * @param ctx the parse tree
     */
    exitDecryption_mechanism?: (ctx: Decryption_mechanismContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.grant_permission`.
     * @param ctx the parse tree
     */
    enterGrant_permission?: (ctx: Grant_permissionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.grant_permission`.
     * @param ctx the parse tree
     */
    exitGrant_permission?: (ctx: Grant_permissionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.set_statement`.
     * @param ctx the parse tree
     */
    enterSet_statement?: (ctx: Set_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.set_statement`.
     * @param ctx the parse tree
     */
    exitSet_statement?: (ctx: Set_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.transaction_statement`.
     * @param ctx the parse tree
     */
    enterTransaction_statement?: (ctx: Transaction_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.transaction_statement`.
     * @param ctx the parse tree
     */
    exitTransaction_statement?: (ctx: Transaction_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.go_statement`.
     * @param ctx the parse tree
     */
    enterGo_statement?: (ctx: Go_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.go_statement`.
     * @param ctx the parse tree
     */
    exitGo_statement?: (ctx: Go_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.use_statement`.
     * @param ctx the parse tree
     */
    enterUse_statement?: (ctx: Use_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.use_statement`.
     * @param ctx the parse tree
     */
    exitUse_statement?: (ctx: Use_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.setuser_statement`.
     * @param ctx the parse tree
     */
    enterSetuser_statement?: (ctx: Setuser_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.setuser_statement`.
     * @param ctx the parse tree
     */
    exitSetuser_statement?: (ctx: Setuser_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.reconfigure_statement`.
     * @param ctx the parse tree
     */
    enterReconfigure_statement?: (ctx: Reconfigure_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.reconfigure_statement`.
     * @param ctx the parse tree
     */
    exitReconfigure_statement?: (ctx: Reconfigure_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.shutdown_statement`.
     * @param ctx the parse tree
     */
    enterShutdown_statement?: (ctx: Shutdown_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.shutdown_statement`.
     * @param ctx the parse tree
     */
    exitShutdown_statement?: (ctx: Shutdown_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.checkpoint_statement`.
     * @param ctx the parse tree
     */
    enterCheckpoint_statement?: (ctx: Checkpoint_statementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.checkpoint_statement`.
     * @param ctx the parse tree
     */
    exitCheckpoint_statement?: (ctx: Checkpoint_statementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkalloc_option`.
     * @param ctx the parse tree
     */
    enterDbcc_checkalloc_option?: (ctx: Dbcc_checkalloc_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkalloc_option`.
     * @param ctx the parse tree
     */
    exitDbcc_checkalloc_option?: (ctx: Dbcc_checkalloc_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkalloc`.
     * @param ctx the parse tree
     */
    enterDbcc_checkalloc?: (ctx: Dbcc_checkallocContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkalloc`.
     * @param ctx the parse tree
     */
    exitDbcc_checkalloc?: (ctx: Dbcc_checkallocContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkcatalog`.
     * @param ctx the parse tree
     */
    enterDbcc_checkcatalog?: (ctx: Dbcc_checkcatalogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkcatalog`.
     * @param ctx the parse tree
     */
    exitDbcc_checkcatalog?: (ctx: Dbcc_checkcatalogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkconstraints_option`.
     * @param ctx the parse tree
     */
    enterDbcc_checkconstraints_option?: (ctx: Dbcc_checkconstraints_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkconstraints_option`.
     * @param ctx the parse tree
     */
    exitDbcc_checkconstraints_option?: (ctx: Dbcc_checkconstraints_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkconstraints`.
     * @param ctx the parse tree
     */
    enterDbcc_checkconstraints?: (ctx: Dbcc_checkconstraintsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkconstraints`.
     * @param ctx the parse tree
     */
    exitDbcc_checkconstraints?: (ctx: Dbcc_checkconstraintsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkdb_table_option`.
     * @param ctx the parse tree
     */
    enterDbcc_checkdb_table_option?: (ctx: Dbcc_checkdb_table_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkdb_table_option`.
     * @param ctx the parse tree
     */
    exitDbcc_checkdb_table_option?: (ctx: Dbcc_checkdb_table_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkdb`.
     * @param ctx the parse tree
     */
    enterDbcc_checkdb?: (ctx: Dbcc_checkdbContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkdb`.
     * @param ctx the parse tree
     */
    exitDbcc_checkdb?: (ctx: Dbcc_checkdbContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkfilegroup_option`.
     * @param ctx the parse tree
     */
    enterDbcc_checkfilegroup_option?: (ctx: Dbcc_checkfilegroup_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkfilegroup_option`.
     * @param ctx the parse tree
     */
    exitDbcc_checkfilegroup_option?: (ctx: Dbcc_checkfilegroup_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checkfilegroup`.
     * @param ctx the parse tree
     */
    enterDbcc_checkfilegroup?: (ctx: Dbcc_checkfilegroupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checkfilegroup`.
     * @param ctx the parse tree
     */
    exitDbcc_checkfilegroup?: (ctx: Dbcc_checkfilegroupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_checktable`.
     * @param ctx the parse tree
     */
    enterDbcc_checktable?: (ctx: Dbcc_checktableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_checktable`.
     * @param ctx the parse tree
     */
    exitDbcc_checktable?: (ctx: Dbcc_checktableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_cleantable`.
     * @param ctx the parse tree
     */
    enterDbcc_cleantable?: (ctx: Dbcc_cleantableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_cleantable`.
     * @param ctx the parse tree
     */
    exitDbcc_cleantable?: (ctx: Dbcc_cleantableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_clonedatabase_option`.
     * @param ctx the parse tree
     */
    enterDbcc_clonedatabase_option?: (ctx: Dbcc_clonedatabase_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_clonedatabase_option`.
     * @param ctx the parse tree
     */
    exitDbcc_clonedatabase_option?: (ctx: Dbcc_clonedatabase_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_clonedatabase`.
     * @param ctx the parse tree
     */
    enterDbcc_clonedatabase?: (ctx: Dbcc_clonedatabaseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_clonedatabase`.
     * @param ctx the parse tree
     */
    exitDbcc_clonedatabase?: (ctx: Dbcc_clonedatabaseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_pdw_showspaceused`.
     * @param ctx the parse tree
     */
    enterDbcc_pdw_showspaceused?: (ctx: Dbcc_pdw_showspaceusedContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_pdw_showspaceused`.
     * @param ctx the parse tree
     */
    exitDbcc_pdw_showspaceused?: (ctx: Dbcc_pdw_showspaceusedContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_proccache`.
     * @param ctx the parse tree
     */
    enterDbcc_proccache?: (ctx: Dbcc_proccacheContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_proccache`.
     * @param ctx the parse tree
     */
    exitDbcc_proccache?: (ctx: Dbcc_proccacheContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_showcontig_option`.
     * @param ctx the parse tree
     */
    enterDbcc_showcontig_option?: (ctx: Dbcc_showcontig_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_showcontig_option`.
     * @param ctx the parse tree
     */
    exitDbcc_showcontig_option?: (ctx: Dbcc_showcontig_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_showcontig`.
     * @param ctx the parse tree
     */
    enterDbcc_showcontig?: (ctx: Dbcc_showcontigContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_showcontig`.
     * @param ctx the parse tree
     */
    exitDbcc_showcontig?: (ctx: Dbcc_showcontigContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_shrinklog`.
     * @param ctx the parse tree
     */
    enterDbcc_shrinklog?: (ctx: Dbcc_shrinklogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_shrinklog`.
     * @param ctx the parse tree
     */
    exitDbcc_shrinklog?: (ctx: Dbcc_shrinklogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_dbreindex`.
     * @param ctx the parse tree
     */
    enterDbcc_dbreindex?: (ctx: Dbcc_dbreindexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_dbreindex`.
     * @param ctx the parse tree
     */
    exitDbcc_dbreindex?: (ctx: Dbcc_dbreindexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_dll_free`.
     * @param ctx the parse tree
     */
    enterDbcc_dll_free?: (ctx: Dbcc_dll_freeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_dll_free`.
     * @param ctx the parse tree
     */
    exitDbcc_dll_free?: (ctx: Dbcc_dll_freeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_dropcleanbuffers`.
     * @param ctx the parse tree
     */
    enterDbcc_dropcleanbuffers?: (ctx: Dbcc_dropcleanbuffersContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_dropcleanbuffers`.
     * @param ctx the parse tree
     */
    exitDbcc_dropcleanbuffers?: (ctx: Dbcc_dropcleanbuffersContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dbcc_clause`.
     * @param ctx the parse tree
     */
    enterDbcc_clause?: (ctx: Dbcc_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dbcc_clause`.
     * @param ctx the parse tree
     */
    exitDbcc_clause?: (ctx: Dbcc_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.execute_clause`.
     * @param ctx the parse tree
     */
    enterExecute_clause?: (ctx: Execute_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.execute_clause`.
     * @param ctx the parse tree
     */
    exitExecute_clause?: (ctx: Execute_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.declare_local`.
     * @param ctx the parse tree
     */
    enterDeclare_local?: (ctx: Declare_localContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.declare_local`.
     * @param ctx the parse tree
     */
    exitDeclare_local?: (ctx: Declare_localContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_type_definition`.
     * @param ctx the parse tree
     */
    enterTable_type_definition?: (ctx: Table_type_definitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_type_definition`.
     * @param ctx the parse tree
     */
    exitTable_type_definition?: (ctx: Table_type_definitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_type_indices`.
     * @param ctx the parse tree
     */
    enterTable_type_indices?: (ctx: Table_type_indicesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_type_indices`.
     * @param ctx the parse tree
     */
    exitTable_type_indices?: (ctx: Table_type_indicesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_type_definition`.
     * @param ctx the parse tree
     */
    enterXml_type_definition?: (ctx: Xml_type_definitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_type_definition`.
     * @param ctx the parse tree
     */
    exitXml_type_definition?: (ctx: Xml_type_definitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_schema_collection`.
     * @param ctx the parse tree
     */
    enterXml_schema_collection?: (ctx: Xml_schema_collectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_schema_collection`.
     * @param ctx the parse tree
     */
    exitXml_schema_collection?: (ctx: Xml_schema_collectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_def_table_constraints`.
     * @param ctx the parse tree
     */
    enterColumn_def_table_constraints?: (ctx: Column_def_table_constraintsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_def_table_constraints`.
     * @param ctx the parse tree
     */
    exitColumn_def_table_constraints?: (ctx: Column_def_table_constraintsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_def_table_constraint`.
     * @param ctx the parse tree
     */
    enterColumn_def_table_constraint?: (ctx: Column_def_table_constraintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_def_table_constraint`.
     * @param ctx the parse tree
     */
    exitColumn_def_table_constraint?: (ctx: Column_def_table_constraintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_definition`.
     * @param ctx the parse tree
     */
    enterColumn_definition?: (ctx: Column_definitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_definition`.
     * @param ctx the parse tree
     */
    exitColumn_definition?: (ctx: Column_definitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_definition_element`.
     * @param ctx the parse tree
     */
    enterColumn_definition_element?: (ctx: Column_definition_elementContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_definition_element`.
     * @param ctx the parse tree
     */
    exitColumn_definition_element?: (ctx: Column_definition_elementContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_modifier`.
     * @param ctx the parse tree
     */
    enterColumn_modifier?: (ctx: Column_modifierContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_modifier`.
     * @param ctx the parse tree
     */
    exitColumn_modifier?: (ctx: Column_modifierContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.materialized_column_definition`.
     * @param ctx the parse tree
     */
    enterMaterialized_column_definition?: (ctx: Materialized_column_definitionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.materialized_column_definition`.
     * @param ctx the parse tree
     */
    exitMaterialized_column_definition?: (ctx: Materialized_column_definitionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_constraint`.
     * @param ctx the parse tree
     */
    enterColumn_constraint?: (ctx: Column_constraintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_constraint`.
     * @param ctx the parse tree
     */
    exitColumn_constraint?: (ctx: Column_constraintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_index`.
     * @param ctx the parse tree
     */
    enterColumn_index?: (ctx: Column_indexContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_index`.
     * @param ctx the parse tree
     */
    exitColumn_index?: (ctx: Column_indexContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.on_partition_or_filegroup`.
     * @param ctx the parse tree
     */
    enterOn_partition_or_filegroup?: (ctx: On_partition_or_filegroupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.on_partition_or_filegroup`.
     * @param ctx the parse tree
     */
    exitOn_partition_or_filegroup?: (ctx: On_partition_or_filegroupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_constraint`.
     * @param ctx the parse tree
     */
    enterTable_constraint?: (ctx: Table_constraintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_constraint`.
     * @param ctx the parse tree
     */
    exitTable_constraint?: (ctx: Table_constraintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.connection_node`.
     * @param ctx the parse tree
     */
    enterConnection_node?: (ctx: Connection_nodeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.connection_node`.
     * @param ctx the parse tree
     */
    exitConnection_node?: (ctx: Connection_nodeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.primary_key_options`.
     * @param ctx the parse tree
     */
    enterPrimary_key_options?: (ctx: Primary_key_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.primary_key_options`.
     * @param ctx the parse tree
     */
    exitPrimary_key_options?: (ctx: Primary_key_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.foreign_key_options`.
     * @param ctx the parse tree
     */
    enterForeign_key_options?: (ctx: Foreign_key_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.foreign_key_options`.
     * @param ctx the parse tree
     */
    exitForeign_key_options?: (ctx: Foreign_key_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.check_constraint`.
     * @param ctx the parse tree
     */
    enterCheck_constraint?: (ctx: Check_constraintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.check_constraint`.
     * @param ctx the parse tree
     */
    exitCheck_constraint?: (ctx: Check_constraintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.on_delete`.
     * @param ctx the parse tree
     */
    enterOn_delete?: (ctx: On_deleteContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.on_delete`.
     * @param ctx the parse tree
     */
    exitOn_delete?: (ctx: On_deleteContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.on_update`.
     * @param ctx the parse tree
     */
    enterOn_update?: (ctx: On_updateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.on_update`.
     * @param ctx the parse tree
     */
    exitOn_update?: (ctx: On_updateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_table_index_options`.
     * @param ctx the parse tree
     */
    enterAlter_table_index_options?: (ctx: Alter_table_index_optionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_table_index_options`.
     * @param ctx the parse tree
     */
    exitAlter_table_index_options?: (ctx: Alter_table_index_optionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.alter_table_index_option`.
     * @param ctx the parse tree
     */
    enterAlter_table_index_option?: (ctx: Alter_table_index_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.alter_table_index_option`.
     * @param ctx the parse tree
     */
    exitAlter_table_index_option?: (ctx: Alter_table_index_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.declare_cursor`.
     * @param ctx the parse tree
     */
    enterDeclare_cursor?: (ctx: Declare_cursorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.declare_cursor`.
     * @param ctx the parse tree
     */
    exitDeclare_cursor?: (ctx: Declare_cursorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.declare_set_cursor_common`.
     * @param ctx the parse tree
     */
    enterDeclare_set_cursor_common?: (ctx: Declare_set_cursor_commonContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.declare_set_cursor_common`.
     * @param ctx the parse tree
     */
    exitDeclare_set_cursor_common?: (ctx: Declare_set_cursor_commonContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.declare_set_cursor_common_partial`.
     * @param ctx the parse tree
     */
    enterDeclare_set_cursor_common_partial?: (ctx: Declare_set_cursor_common_partialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.declare_set_cursor_common_partial`.
     * @param ctx the parse tree
     */
    exitDeclare_set_cursor_common_partial?: (ctx: Declare_set_cursor_common_partialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.fetch_cursor`.
     * @param ctx the parse tree
     */
    enterFetch_cursor?: (ctx: Fetch_cursorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.fetch_cursor`.
     * @param ctx the parse tree
     */
    exitFetch_cursor?: (ctx: Fetch_cursorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.set_special`.
     * @param ctx the parse tree
     */
    enterSet_special?: (ctx: Set_specialContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.set_special`.
     * @param ctx the parse tree
     */
    exitSet_special?: (ctx: Set_specialContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.special_list`.
     * @param ctx the parse tree
     */
    enterSpecial_list?: (ctx: Special_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.special_list`.
     * @param ctx the parse tree
     */
    exitSpecial_list?: (ctx: Special_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.constant_LOCAL_ID`.
     * @param ctx the parse tree
     */
    enterConstant_LOCAL_ID?: (ctx: Constant_LOCAL_IDContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.constant_LOCAL_ID`.
     * @param ctx the parse tree
     */
    exitConstant_LOCAL_ID?: (ctx: Constant_LOCAL_IDContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.expression`.
     * @param ctx the parse tree
     */
    enterExpression?: (ctx: ExpressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.expression`.
     * @param ctx the parse tree
     */
    exitExpression?: (ctx: ExpressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.parameter`.
     * @param ctx the parse tree
     */
    enterParameter?: (ctx: ParameterContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.parameter`.
     * @param ctx the parse tree
     */
    exitParameter?: (ctx: ParameterContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.time_zone`.
     * @param ctx the parse tree
     */
    enterTime_zone?: (ctx: Time_zoneContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.time_zone`.
     * @param ctx the parse tree
     */
    exitTime_zone?: (ctx: Time_zoneContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.primitive_expression`.
     * @param ctx the parse tree
     */
    enterPrimitive_expression?: (ctx: Primitive_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.primitive_expression`.
     * @param ctx the parse tree
     */
    exitPrimitive_expression?: (ctx: Primitive_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.case_expression`.
     * @param ctx the parse tree
     */
    enterCase_expression?: (ctx: Case_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.case_expression`.
     * @param ctx the parse tree
     */
    exitCase_expression?: (ctx: Case_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.unary_operator_expression`.
     * @param ctx the parse tree
     */
    enterUnary_operator_expression?: (ctx: Unary_operator_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.unary_operator_expression`.
     * @param ctx the parse tree
     */
    exitUnary_operator_expression?: (ctx: Unary_operator_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.bracket_expression`.
     * @param ctx the parse tree
     */
    enterBracket_expression?: (ctx: Bracket_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.bracket_expression`.
     * @param ctx the parse tree
     */
    exitBracket_expression?: (ctx: Bracket_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.subquery`.
     * @param ctx the parse tree
     */
    enterSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.subquery`.
     * @param ctx the parse tree
     */
    exitSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.with_expression`.
     * @param ctx the parse tree
     */
    enterWith_expression?: (ctx: With_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.with_expression`.
     * @param ctx the parse tree
     */
    exitWith_expression?: (ctx: With_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.common_table_expression`.
     * @param ctx the parse tree
     */
    enterCommon_table_expression?: (ctx: Common_table_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.common_table_expression`.
     * @param ctx the parse tree
     */
    exitCommon_table_expression?: (ctx: Common_table_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_elem`.
     * @param ctx the parse tree
     */
    enterUpdate_elem?: (ctx: Update_elemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_elem`.
     * @param ctx the parse tree
     */
    exitUpdate_elem?: (ctx: Update_elemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.update_elem_merge`.
     * @param ctx the parse tree
     */
    enterUpdate_elem_merge?: (ctx: Update_elem_mergeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.update_elem_merge`.
     * @param ctx the parse tree
     */
    exitUpdate_elem_merge?: (ctx: Update_elem_mergeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.search_condition`.
     * @param ctx the parse tree
     */
    enterSearch_condition?: (ctx: Search_conditionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.search_condition`.
     * @param ctx the parse tree
     */
    exitSearch_condition?: (ctx: Search_conditionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.predicate`.
     * @param ctx the parse tree
     */
    enterPredicate?: (ctx: PredicateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.predicate`.
     * @param ctx the parse tree
     */
    exitPredicate?: (ctx: PredicateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.query_expression`.
     * @param ctx the parse tree
     */
    enterQuery_expression?: (ctx: Query_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.query_expression`.
     * @param ctx the parse tree
     */
    exitQuery_expression?: (ctx: Query_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.sql_union`.
     * @param ctx the parse tree
     */
    enterSql_union?: (ctx: Sql_unionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.sql_union`.
     * @param ctx the parse tree
     */
    exitSql_union?: (ctx: Sql_unionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.query_specification`.
     * @param ctx the parse tree
     */
    enterQuery_specification?: (ctx: Query_specificationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.query_specification`.
     * @param ctx the parse tree
     */
    exitQuery_specification?: (ctx: Query_specificationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.top_clause`.
     * @param ctx the parse tree
     */
    enterTop_clause?: (ctx: Top_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.top_clause`.
     * @param ctx the parse tree
     */
    exitTop_clause?: (ctx: Top_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.top_percent`.
     * @param ctx the parse tree
     */
    enterTop_percent?: (ctx: Top_percentContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.top_percent`.
     * @param ctx the parse tree
     */
    exitTop_percent?: (ctx: Top_percentContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.top_count`.
     * @param ctx the parse tree
     */
    enterTop_count?: (ctx: Top_countContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.top_count`.
     * @param ctx the parse tree
     */
    exitTop_count?: (ctx: Top_countContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.order_by_clause`.
     * @param ctx the parse tree
     */
    enterOrder_by_clause?: (ctx: Order_by_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.order_by_clause`.
     * @param ctx the parse tree
     */
    exitOrder_by_clause?: (ctx: Order_by_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.select_order_by_clause`.
     * @param ctx the parse tree
     */
    enterSelect_order_by_clause?: (ctx: Select_order_by_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.select_order_by_clause`.
     * @param ctx the parse tree
     */
    exitSelect_order_by_clause?: (ctx: Select_order_by_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.for_clause`.
     * @param ctx the parse tree
     */
    enterFor_clause?: (ctx: For_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.for_clause`.
     * @param ctx the parse tree
     */
    exitFor_clause?: (ctx: For_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_common_directives`.
     * @param ctx the parse tree
     */
    enterXml_common_directives?: (ctx: Xml_common_directivesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_common_directives`.
     * @param ctx the parse tree
     */
    exitXml_common_directives?: (ctx: Xml_common_directivesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.order_by_expression`.
     * @param ctx the parse tree
     */
    enterOrder_by_expression?: (ctx: Order_by_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.order_by_expression`.
     * @param ctx the parse tree
     */
    exitOrder_by_expression?: (ctx: Order_by_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.grouping_sets_item`.
     * @param ctx the parse tree
     */
    enterGrouping_sets_item?: (ctx: Grouping_sets_itemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.grouping_sets_item`.
     * @param ctx the parse tree
     */
    exitGrouping_sets_item?: (ctx: Grouping_sets_itemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.group_by_item`.
     * @param ctx the parse tree
     */
    enterGroup_by_item?: (ctx: Group_by_itemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.group_by_item`.
     * @param ctx the parse tree
     */
    exitGroup_by_item?: (ctx: Group_by_itemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.option_clause`.
     * @param ctx the parse tree
     */
    enterOption_clause?: (ctx: Option_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.option_clause`.
     * @param ctx the parse tree
     */
    exitOption_clause?: (ctx: Option_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.option`.
     * @param ctx the parse tree
     */
    enterOption?: (ctx: OptionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.option`.
     * @param ctx the parse tree
     */
    exitOption?: (ctx: OptionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.optimize_for_arg`.
     * @param ctx the parse tree
     */
    enterOptimize_for_arg?: (ctx: Optimize_for_argContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.optimize_for_arg`.
     * @param ctx the parse tree
     */
    exitOptimize_for_arg?: (ctx: Optimize_for_argContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.select_list`.
     * @param ctx the parse tree
     */
    enterSelect_list?: (ctx: Select_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.select_list`.
     * @param ctx the parse tree
     */
    exitSelect_list?: (ctx: Select_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.udt_method_arguments`.
     * @param ctx the parse tree
     */
    enterUdt_method_arguments?: (ctx: Udt_method_argumentsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.udt_method_arguments`.
     * @param ctx the parse tree
     */
    exitUdt_method_arguments?: (ctx: Udt_method_argumentsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.asterisk`.
     * @param ctx the parse tree
     */
    enterAsterisk?: (ctx: AsteriskContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.asterisk`.
     * @param ctx the parse tree
     */
    exitAsterisk?: (ctx: AsteriskContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.udt_elem`.
     * @param ctx the parse tree
     */
    enterUdt_elem?: (ctx: Udt_elemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.udt_elem`.
     * @param ctx the parse tree
     */
    exitUdt_elem?: (ctx: Udt_elemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.expression_elem`.
     * @param ctx the parse tree
     */
    enterExpression_elem?: (ctx: Expression_elemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.expression_elem`.
     * @param ctx the parse tree
     */
    exitExpression_elem?: (ctx: Expression_elemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.select_list_elem`.
     * @param ctx the parse tree
     */
    enterSelect_list_elem?: (ctx: Select_list_elemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.select_list_elem`.
     * @param ctx the parse tree
     */
    exitSelect_list_elem?: (ctx: Select_list_elemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_sources`.
     * @param ctx the parse tree
     */
    enterTable_sources?: (ctx: Table_sourcesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_sources`.
     * @param ctx the parse tree
     */
    exitTable_sources?: (ctx: Table_sourcesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.non_ansi_join`.
     * @param ctx the parse tree
     */
    enterNon_ansi_join?: (ctx: Non_ansi_joinContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.non_ansi_join`.
     * @param ctx the parse tree
     */
    exitNon_ansi_join?: (ctx: Non_ansi_joinContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_source`.
     * @param ctx the parse tree
     */
    enterTable_source?: (ctx: Table_sourceContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_source`.
     * @param ctx the parse tree
     */
    exitTable_source?: (ctx: Table_sourceContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_source_item`.
     * @param ctx the parse tree
     */
    enterTable_source_item?: (ctx: Table_source_itemContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_source_item`.
     * @param ctx the parse tree
     */
    exitTable_source_item?: (ctx: Table_source_itemContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.open_xml`.
     * @param ctx the parse tree
     */
    enterOpen_xml?: (ctx: Open_xmlContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.open_xml`.
     * @param ctx the parse tree
     */
    exitOpen_xml?: (ctx: Open_xmlContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.open_json`.
     * @param ctx the parse tree
     */
    enterOpen_json?: (ctx: Open_jsonContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.open_json`.
     * @param ctx the parse tree
     */
    exitOpen_json?: (ctx: Open_jsonContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.json_declaration`.
     * @param ctx the parse tree
     */
    enterJson_declaration?: (ctx: Json_declarationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.json_declaration`.
     * @param ctx the parse tree
     */
    exitJson_declaration?: (ctx: Json_declarationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.json_column_declaration`.
     * @param ctx the parse tree
     */
    enterJson_column_declaration?: (ctx: Json_column_declarationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.json_column_declaration`.
     * @param ctx the parse tree
     */
    exitJson_column_declaration?: (ctx: Json_column_declarationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.schema_declaration`.
     * @param ctx the parse tree
     */
    enterSchema_declaration?: (ctx: Schema_declarationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.schema_declaration`.
     * @param ctx the parse tree
     */
    exitSchema_declaration?: (ctx: Schema_declarationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_declaration`.
     * @param ctx the parse tree
     */
    enterColumn_declaration?: (ctx: Column_declarationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_declaration`.
     * @param ctx the parse tree
     */
    exitColumn_declaration?: (ctx: Column_declarationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.change_table`.
     * @param ctx the parse tree
     */
    enterChange_table?: (ctx: Change_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.change_table`.
     * @param ctx the parse tree
     */
    exitChange_table?: (ctx: Change_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.change_table_changes`.
     * @param ctx the parse tree
     */
    enterChange_table_changes?: (ctx: Change_table_changesContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.change_table_changes`.
     * @param ctx the parse tree
     */
    exitChange_table_changes?: (ctx: Change_table_changesContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.change_table_version`.
     * @param ctx the parse tree
     */
    enterChange_table_version?: (ctx: Change_table_versionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.change_table_version`.
     * @param ctx the parse tree
     */
    exitChange_table_version?: (ctx: Change_table_versionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.join_part`.
     * @param ctx the parse tree
     */
    enterJoin_part?: (ctx: Join_partContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.join_part`.
     * @param ctx the parse tree
     */
    exitJoin_part?: (ctx: Join_partContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.join_on`.
     * @param ctx the parse tree
     */
    enterJoin_on?: (ctx: Join_onContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.join_on`.
     * @param ctx the parse tree
     */
    exitJoin_on?: (ctx: Join_onContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.cross_join`.
     * @param ctx the parse tree
     */
    enterCross_join?: (ctx: Cross_joinContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.cross_join`.
     * @param ctx the parse tree
     */
    exitCross_join?: (ctx: Cross_joinContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.apply_`.
     * @param ctx the parse tree
     */
    enterApply_?: (ctx: Apply_Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.apply_`.
     * @param ctx the parse tree
     */
    exitApply_?: (ctx: Apply_Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.pivot`.
     * @param ctx the parse tree
     */
    enterPivot?: (ctx: PivotContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.pivot`.
     * @param ctx the parse tree
     */
    exitPivot?: (ctx: PivotContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.unpivot`.
     * @param ctx the parse tree
     */
    enterUnpivot?: (ctx: UnpivotContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.unpivot`.
     * @param ctx the parse tree
     */
    exitUnpivot?: (ctx: UnpivotContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.pivot_clause`.
     * @param ctx the parse tree
     */
    enterPivot_clause?: (ctx: Pivot_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.pivot_clause`.
     * @param ctx the parse tree
     */
    exitPivot_clause?: (ctx: Pivot_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.unpivot_clause`.
     * @param ctx the parse tree
     */
    enterUnpivot_clause?: (ctx: Unpivot_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.unpivot_clause`.
     * @param ctx the parse tree
     */
    exitUnpivot_clause?: (ctx: Unpivot_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.full_column_name_list`.
     * @param ctx the parse tree
     */
    enterFull_column_name_list?: (ctx: Full_column_name_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.full_column_name_list`.
     * @param ctx the parse tree
     */
    exitFull_column_name_list?: (ctx: Full_column_name_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.rowset_function`.
     * @param ctx the parse tree
     */
    enterRowset_function?: (ctx: Rowset_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.rowset_function`.
     * @param ctx the parse tree
     */
    exitRowset_function?: (ctx: Rowset_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.bulk_option`.
     * @param ctx the parse tree
     */
    enterBulk_option?: (ctx: Bulk_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.bulk_option`.
     * @param ctx the parse tree
     */
    exitBulk_option?: (ctx: Bulk_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.derived_table`.
     * @param ctx the parse tree
     */
    enterDerived_table?: (ctx: Derived_tableContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.derived_table`.
     * @param ctx the parse tree
     */
    exitDerived_table?: (ctx: Derived_tableContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    enterFunction_call?: (ctx: Function_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.function_call`.
     * @param ctx the parse tree
     */
    exitFunction_call?: (ctx: Function_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.partition_function`.
     * @param ctx the parse tree
     */
    enterPartition_function?: (ctx: Partition_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.partition_function`.
     * @param ctx the parse tree
     */
    exitPartition_function?: (ctx: Partition_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.freetext_function`.
     * @param ctx the parse tree
     */
    enterFreetext_function?: (ctx: Freetext_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.freetext_function`.
     * @param ctx the parse tree
     */
    exitFreetext_function?: (ctx: Freetext_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.freetext_predicate`.
     * @param ctx the parse tree
     */
    enterFreetext_predicate?: (ctx: Freetext_predicateContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.freetext_predicate`.
     * @param ctx the parse tree
     */
    exitFreetext_predicate?: (ctx: Freetext_predicateContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.json_key_value`.
     * @param ctx the parse tree
     */
    enterJson_key_value?: (ctx: Json_key_valueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.json_key_value`.
     * @param ctx the parse tree
     */
    exitJson_key_value?: (ctx: Json_key_valueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.json_null_clause`.
     * @param ctx the parse tree
     */
    enterJson_null_clause?: (ctx: Json_null_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.json_null_clause`.
     * @param ctx the parse tree
     */
    exitJson_null_clause?: (ctx: Json_null_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    enterBuilt_in_functions?: (ctx: Built_in_functionsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.built_in_functions`.
     * @param ctx the parse tree
     */
    exitBuilt_in_functions?: (ctx: Built_in_functionsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.xml_data_type_methods`.
     * @param ctx the parse tree
     */
    enterXml_data_type_methods?: (ctx: Xml_data_type_methodsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.xml_data_type_methods`.
     * @param ctx the parse tree
     */
    exitXml_data_type_methods?: (ctx: Xml_data_type_methodsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dateparts_9`.
     * @param ctx the parse tree
     */
    enterDateparts_9?: (ctx: Dateparts_9Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dateparts_9`.
     * @param ctx the parse tree
     */
    exitDateparts_9?: (ctx: Dateparts_9Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dateparts_12`.
     * @param ctx the parse tree
     */
    enterDateparts_12?: (ctx: Dateparts_12Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dateparts_12`.
     * @param ctx the parse tree
     */
    exitDateparts_12?: (ctx: Dateparts_12Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dateparts_15`.
     * @param ctx the parse tree
     */
    enterDateparts_15?: (ctx: Dateparts_15Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dateparts_15`.
     * @param ctx the parse tree
     */
    exitDateparts_15?: (ctx: Dateparts_15Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.dateparts_datetrunc`.
     * @param ctx the parse tree
     */
    enterDateparts_datetrunc?: (ctx: Dateparts_datetruncContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.dateparts_datetrunc`.
     * @param ctx the parse tree
     */
    exitDateparts_datetrunc?: (ctx: Dateparts_datetruncContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.value_method`.
     * @param ctx the parse tree
     */
    enterValue_method?: (ctx: Value_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.value_method`.
     * @param ctx the parse tree
     */
    exitValue_method?: (ctx: Value_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.value_call`.
     * @param ctx the parse tree
     */
    enterValue_call?: (ctx: Value_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.value_call`.
     * @param ctx the parse tree
     */
    exitValue_call?: (ctx: Value_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.query_method`.
     * @param ctx the parse tree
     */
    enterQuery_method?: (ctx: Query_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.query_method`.
     * @param ctx the parse tree
     */
    exitQuery_method?: (ctx: Query_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.query_call`.
     * @param ctx the parse tree
     */
    enterQuery_call?: (ctx: Query_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.query_call`.
     * @param ctx the parse tree
     */
    exitQuery_call?: (ctx: Query_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.exist_method`.
     * @param ctx the parse tree
     */
    enterExist_method?: (ctx: Exist_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.exist_method`.
     * @param ctx the parse tree
     */
    exitExist_method?: (ctx: Exist_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.exist_call`.
     * @param ctx the parse tree
     */
    enterExist_call?: (ctx: Exist_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.exist_call`.
     * @param ctx the parse tree
     */
    exitExist_call?: (ctx: Exist_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.modify_method`.
     * @param ctx the parse tree
     */
    enterModify_method?: (ctx: Modify_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.modify_method`.
     * @param ctx the parse tree
     */
    exitModify_method?: (ctx: Modify_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.modify_call`.
     * @param ctx the parse tree
     */
    enterModify_call?: (ctx: Modify_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.modify_call`.
     * @param ctx the parse tree
     */
    exitModify_call?: (ctx: Modify_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.hierarchyid_call`.
     * @param ctx the parse tree
     */
    enterHierarchyid_call?: (ctx: Hierarchyid_callContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.hierarchyid_call`.
     * @param ctx the parse tree
     */
    exitHierarchyid_call?: (ctx: Hierarchyid_callContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.hierarchyid_static_method`.
     * @param ctx the parse tree
     */
    enterHierarchyid_static_method?: (ctx: Hierarchyid_static_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.hierarchyid_static_method`.
     * @param ctx the parse tree
     */
    exitHierarchyid_static_method?: (ctx: Hierarchyid_static_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.nodes_method`.
     * @param ctx the parse tree
     */
    enterNodes_method?: (ctx: Nodes_methodContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.nodes_method`.
     * @param ctx the parse tree
     */
    exitNodes_method?: (ctx: Nodes_methodContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.switch_section`.
     * @param ctx the parse tree
     */
    enterSwitch_section?: (ctx: Switch_sectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.switch_section`.
     * @param ctx the parse tree
     */
    exitSwitch_section?: (ctx: Switch_sectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.switch_search_condition_section`.
     * @param ctx the parse tree
     */
    enterSwitch_search_condition_section?: (ctx: Switch_search_condition_sectionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.switch_search_condition_section`.
     * @param ctx the parse tree
     */
    exitSwitch_search_condition_section?: (ctx: Switch_search_condition_sectionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.as_column_alias`.
     * @param ctx the parse tree
     */
    enterAs_column_alias?: (ctx: As_column_aliasContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.as_column_alias`.
     * @param ctx the parse tree
     */
    exitAs_column_alias?: (ctx: As_column_aliasContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.as_table_alias`.
     * @param ctx the parse tree
     */
    enterAs_table_alias?: (ctx: As_table_aliasContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.as_table_alias`.
     * @param ctx the parse tree
     */
    exitAs_table_alias?: (ctx: As_table_aliasContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_alias`.
     * @param ctx the parse tree
     */
    enterTable_alias?: (ctx: Table_aliasContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_alias`.
     * @param ctx the parse tree
     */
    exitTable_alias?: (ctx: Table_aliasContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.with_table_hints`.
     * @param ctx the parse tree
     */
    enterWith_table_hints?: (ctx: With_table_hintsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.with_table_hints`.
     * @param ctx the parse tree
     */
    exitWith_table_hints?: (ctx: With_table_hintsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.deprecated_table_hint`.
     * @param ctx the parse tree
     */
    enterDeprecated_table_hint?: (ctx: Deprecated_table_hintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.deprecated_table_hint`.
     * @param ctx the parse tree
     */
    exitDeprecated_table_hint?: (ctx: Deprecated_table_hintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.sybase_legacy_hints`.
     * @param ctx the parse tree
     */
    enterSybase_legacy_hints?: (ctx: Sybase_legacy_hintsContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.sybase_legacy_hints`.
     * @param ctx the parse tree
     */
    exitSybase_legacy_hints?: (ctx: Sybase_legacy_hintsContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.sybase_legacy_hint`.
     * @param ctx the parse tree
     */
    enterSybase_legacy_hint?: (ctx: Sybase_legacy_hintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.sybase_legacy_hint`.
     * @param ctx the parse tree
     */
    exitSybase_legacy_hint?: (ctx: Sybase_legacy_hintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_hint`.
     * @param ctx the parse tree
     */
    enterTable_hint?: (ctx: Table_hintContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_hint`.
     * @param ctx the parse tree
     */
    exitTable_hint?: (ctx: Table_hintContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.index_value`.
     * @param ctx the parse tree
     */
    enterIndex_value?: (ctx: Index_valueContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.index_value`.
     * @param ctx the parse tree
     */
    exitIndex_value?: (ctx: Index_valueContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_alias_list`.
     * @param ctx the parse tree
     */
    enterColumn_alias_list?: (ctx: Column_alias_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_alias_list`.
     * @param ctx the parse tree
     */
    exitColumn_alias_list?: (ctx: Column_alias_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_alias`.
     * @param ctx the parse tree
     */
    enterColumn_alias?: (ctx: Column_aliasContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_alias`.
     * @param ctx the parse tree
     */
    exitColumn_alias?: (ctx: Column_aliasContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_value_constructor`.
     * @param ctx the parse tree
     */
    enterTable_value_constructor?: (ctx: Table_value_constructorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_value_constructor`.
     * @param ctx the parse tree
     */
    exitTable_value_constructor?: (ctx: Table_value_constructorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.expression_list_`.
     * @param ctx the parse tree
     */
    enterExpression_list_?: (ctx: Expression_list_Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.expression_list_`.
     * @param ctx the parse tree
     */
    exitExpression_list_?: (ctx: Expression_list_Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ranking_windowed_function`.
     * @param ctx the parse tree
     */
    enterRanking_windowed_function?: (ctx: Ranking_windowed_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ranking_windowed_function`.
     * @param ctx the parse tree
     */
    exitRanking_windowed_function?: (ctx: Ranking_windowed_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.aggregate_windowed_function`.
     * @param ctx the parse tree
     */
    enterAggregate_windowed_function?: (ctx: Aggregate_windowed_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.aggregate_windowed_function`.
     * @param ctx the parse tree
     */
    exitAggregate_windowed_function?: (ctx: Aggregate_windowed_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.analytic_windowed_function`.
     * @param ctx the parse tree
     */
    enterAnalytic_windowed_function?: (ctx: Analytic_windowed_functionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.analytic_windowed_function`.
     * @param ctx the parse tree
     */
    exitAnalytic_windowed_function?: (ctx: Analytic_windowed_functionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.all_distinct_expression`.
     * @param ctx the parse tree
     */
    enterAll_distinct_expression?: (ctx: All_distinct_expressionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.all_distinct_expression`.
     * @param ctx the parse tree
     */
    exitAll_distinct_expression?: (ctx: All_distinct_expressionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.over_clause`.
     * @param ctx the parse tree
     */
    enterOver_clause?: (ctx: Over_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.over_clause`.
     * @param ctx the parse tree
     */
    exitOver_clause?: (ctx: Over_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.row_or_range_clause`.
     * @param ctx the parse tree
     */
    enterRow_or_range_clause?: (ctx: Row_or_range_clauseContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.row_or_range_clause`.
     * @param ctx the parse tree
     */
    exitRow_or_range_clause?: (ctx: Row_or_range_clauseContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.window_frame_extent`.
     * @param ctx the parse tree
     */
    enterWindow_frame_extent?: (ctx: Window_frame_extentContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.window_frame_extent`.
     * @param ctx the parse tree
     */
    exitWindow_frame_extent?: (ctx: Window_frame_extentContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.window_frame_bound`.
     * @param ctx the parse tree
     */
    enterWindow_frame_bound?: (ctx: Window_frame_boundContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.window_frame_bound`.
     * @param ctx the parse tree
     */
    exitWindow_frame_bound?: (ctx: Window_frame_boundContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.window_frame_preceding`.
     * @param ctx the parse tree
     */
    enterWindow_frame_preceding?: (ctx: Window_frame_precedingContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.window_frame_preceding`.
     * @param ctx the parse tree
     */
    exitWindow_frame_preceding?: (ctx: Window_frame_precedingContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.window_frame_following`.
     * @param ctx the parse tree
     */
    enterWindow_frame_following?: (ctx: Window_frame_followingContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.window_frame_following`.
     * @param ctx the parse tree
     */
    exitWindow_frame_following?: (ctx: Window_frame_followingContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.create_database_option`.
     * @param ctx the parse tree
     */
    enterCreate_database_option?: (ctx: Create_database_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.create_database_option`.
     * @param ctx the parse tree
     */
    exitCreate_database_option?: (ctx: Create_database_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.database_filestream_option`.
     * @param ctx the parse tree
     */
    enterDatabase_filestream_option?: (ctx: Database_filestream_optionContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.database_filestream_option`.
     * @param ctx the parse tree
     */
    exitDatabase_filestream_option?: (ctx: Database_filestream_optionContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.database_file_spec`.
     * @param ctx the parse tree
     */
    enterDatabase_file_spec?: (ctx: Database_file_specContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.database_file_spec`.
     * @param ctx the parse tree
     */
    exitDatabase_file_spec?: (ctx: Database_file_specContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.file_group`.
     * @param ctx the parse tree
     */
    enterFile_group?: (ctx: File_groupContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.file_group`.
     * @param ctx the parse tree
     */
    exitFile_group?: (ctx: File_groupContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.file_spec`.
     * @param ctx the parse tree
     */
    enterFile_spec?: (ctx: File_specContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.file_spec`.
     * @param ctx the parse tree
     */
    exitFile_spec?: (ctx: File_specContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.entity_name`.
     * @param ctx the parse tree
     */
    enterEntity_name?: (ctx: Entity_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.entity_name`.
     * @param ctx the parse tree
     */
    exitEntity_name?: (ctx: Entity_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.entity_name_for_azure_dw`.
     * @param ctx the parse tree
     */
    enterEntity_name_for_azure_dw?: (ctx: Entity_name_for_azure_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.entity_name_for_azure_dw`.
     * @param ctx the parse tree
     */
    exitEntity_name_for_azure_dw?: (ctx: Entity_name_for_azure_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.entity_name_for_parallel_dw`.
     * @param ctx the parse tree
     */
    enterEntity_name_for_parallel_dw?: (ctx: Entity_name_for_parallel_dwContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.entity_name_for_parallel_dw`.
     * @param ctx the parse tree
     */
    exitEntity_name_for_parallel_dw?: (ctx: Entity_name_for_parallel_dwContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.full_table_name`.
     * @param ctx the parse tree
     */
    enterFull_table_name?: (ctx: Full_table_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.full_table_name`.
     * @param ctx the parse tree
     */
    exitFull_table_name?: (ctx: Full_table_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.table_name`.
     * @param ctx the parse tree
     */
    enterTable_name?: (ctx: Table_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.table_name`.
     * @param ctx the parse tree
     */
    exitTable_name?: (ctx: Table_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.simple_name`.
     * @param ctx the parse tree
     */
    enterSimple_name?: (ctx: Simple_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.simple_name`.
     * @param ctx the parse tree
     */
    exitSimple_name?: (ctx: Simple_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_proc_name_schema`.
     * @param ctx the parse tree
     */
    enterFunc_proc_name_schema?: (ctx: Func_proc_name_schemaContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_proc_name_schema`.
     * @param ctx the parse tree
     */
    exitFunc_proc_name_schema?: (ctx: Func_proc_name_schemaContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_proc_name_database_schema`.
     * @param ctx the parse tree
     */
    enterFunc_proc_name_database_schema?: (ctx: Func_proc_name_database_schemaContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_proc_name_database_schema`.
     * @param ctx the parse tree
     */
    exitFunc_proc_name_database_schema?: (ctx: Func_proc_name_database_schemaContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.func_proc_name_server_database_schema`.
     * @param ctx the parse tree
     */
    enterFunc_proc_name_server_database_schema?: (ctx: Func_proc_name_server_database_schemaContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.func_proc_name_server_database_schema`.
     * @param ctx the parse tree
     */
    exitFunc_proc_name_server_database_schema?: (ctx: Func_proc_name_server_database_schemaContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.ddl_object`.
     * @param ctx the parse tree
     */
    enterDdl_object?: (ctx: Ddl_objectContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.ddl_object`.
     * @param ctx the parse tree
     */
    exitDdl_object?: (ctx: Ddl_objectContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.full_column_name`.
     * @param ctx the parse tree
     */
    enterFull_column_name?: (ctx: Full_column_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.full_column_name`.
     * @param ctx the parse tree
     */
    exitFull_column_name?: (ctx: Full_column_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_name_list_with_order`.
     * @param ctx the parse tree
     */
    enterColumn_name_list_with_order?: (ctx: Column_name_list_with_orderContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_name_list_with_order`.
     * @param ctx the parse tree
     */
    exitColumn_name_list_with_order?: (ctx: Column_name_list_with_orderContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.insert_column_name_list`.
     * @param ctx the parse tree
     */
    enterInsert_column_name_list?: (ctx: Insert_column_name_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.insert_column_name_list`.
     * @param ctx the parse tree
     */
    exitInsert_column_name_list?: (ctx: Insert_column_name_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.insert_column_id`.
     * @param ctx the parse tree
     */
    enterInsert_column_id?: (ctx: Insert_column_idContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.insert_column_id`.
     * @param ctx the parse tree
     */
    exitInsert_column_id?: (ctx: Insert_column_idContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.column_name_list`.
     * @param ctx the parse tree
     */
    enterColumn_name_list?: (ctx: Column_name_listContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.column_name_list`.
     * @param ctx the parse tree
     */
    exitColumn_name_list?: (ctx: Column_name_listContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.cursor_name`.
     * @param ctx the parse tree
     */
    enterCursor_name?: (ctx: Cursor_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.cursor_name`.
     * @param ctx the parse tree
     */
    exitCursor_name?: (ctx: Cursor_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.on_off`.
     * @param ctx the parse tree
     */
    enterOn_off?: (ctx: On_offContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.on_off`.
     * @param ctx the parse tree
     */
    exitOn_off?: (ctx: On_offContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.clustered`.
     * @param ctx the parse tree
     */
    enterClustered?: (ctx: ClusteredContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.clustered`.
     * @param ctx the parse tree
     */
    exitClustered?: (ctx: ClusteredContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.null_notnull`.
     * @param ctx the parse tree
     */
    enterNull_notnull?: (ctx: Null_notnullContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.null_notnull`.
     * @param ctx the parse tree
     */
    exitNull_notnull?: (ctx: Null_notnullContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.scalar_function_name`.
     * @param ctx the parse tree
     */
    enterScalar_function_name?: (ctx: Scalar_function_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.scalar_function_name`.
     * @param ctx the parse tree
     */
    exitScalar_function_name?: (ctx: Scalar_function_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.begin_conversation_timer`.
     * @param ctx the parse tree
     */
    enterBegin_conversation_timer?: (ctx: Begin_conversation_timerContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.begin_conversation_timer`.
     * @param ctx the parse tree
     */
    exitBegin_conversation_timer?: (ctx: Begin_conversation_timerContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.begin_conversation_dialog`.
     * @param ctx the parse tree
     */
    enterBegin_conversation_dialog?: (ctx: Begin_conversation_dialogContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.begin_conversation_dialog`.
     * @param ctx the parse tree
     */
    exitBegin_conversation_dialog?: (ctx: Begin_conversation_dialogContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.contract_name`.
     * @param ctx the parse tree
     */
    enterContract_name?: (ctx: Contract_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.contract_name`.
     * @param ctx the parse tree
     */
    exitContract_name?: (ctx: Contract_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.service_name`.
     * @param ctx the parse tree
     */
    enterService_name?: (ctx: Service_nameContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.service_name`.
     * @param ctx the parse tree
     */
    exitService_name?: (ctx: Service_nameContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.end_conversation`.
     * @param ctx the parse tree
     */
    enterEnd_conversation?: (ctx: End_conversationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.end_conversation`.
     * @param ctx the parse tree
     */
    exitEnd_conversation?: (ctx: End_conversationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.waitfor_conversation`.
     * @param ctx the parse tree
     */
    enterWaitfor_conversation?: (ctx: Waitfor_conversationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.waitfor_conversation`.
     * @param ctx the parse tree
     */
    exitWaitfor_conversation?: (ctx: Waitfor_conversationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.get_conversation`.
     * @param ctx the parse tree
     */
    enterGet_conversation?: (ctx: Get_conversationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.get_conversation`.
     * @param ctx the parse tree
     */
    exitGet_conversation?: (ctx: Get_conversationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.queue_id`.
     * @param ctx the parse tree
     */
    enterQueue_id?: (ctx: Queue_idContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.queue_id`.
     * @param ctx the parse tree
     */
    exitQueue_id?: (ctx: Queue_idContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.send_conversation`.
     * @param ctx the parse tree
     */
    enterSend_conversation?: (ctx: Send_conversationContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.send_conversation`.
     * @param ctx the parse tree
     */
    exitSend_conversation?: (ctx: Send_conversationContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.data_type`.
     * @param ctx the parse tree
     */
    enterData_type?: (ctx: Data_typeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.data_type`.
     * @param ctx the parse tree
     */
    exitData_type?: (ctx: Data_typeContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.constant`.
     * @param ctx the parse tree
     */
    enterConstant?: (ctx: ConstantContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.constant`.
     * @param ctx the parse tree
     */
    exitConstant?: (ctx: ConstantContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.primitive_constant`.
     * @param ctx the parse tree
     */
    enterPrimitive_constant?: (ctx: Primitive_constantContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.primitive_constant`.
     * @param ctx the parse tree
     */
    exitPrimitive_constant?: (ctx: Primitive_constantContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.keyword`.
     * @param ctx the parse tree
     */
    enterKeyword?: (ctx: KeywordContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.keyword`.
     * @param ctx the parse tree
     */
    exitKeyword?: (ctx: KeywordContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.id_`.
     * @param ctx the parse tree
     */
    enterId_?: (ctx: Id_Context) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.id_`.
     * @param ctx the parse tree
     */
    exitId_?: (ctx: Id_Context) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.simple_id`.
     * @param ctx the parse tree
     */
    enterSimple_id?: (ctx: Simple_idContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.simple_id`.
     * @param ctx the parse tree
     */
    exitSimple_id?: (ctx: Simple_idContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.id_or_string`.
     * @param ctx the parse tree
     */
    enterId_or_string?: (ctx: Id_or_stringContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.id_or_string`.
     * @param ctx the parse tree
     */
    exitId_or_string?: (ctx: Id_or_stringContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.comparison_operator`.
     * @param ctx the parse tree
     */
    enterComparison_operator?: (ctx: Comparison_operatorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.comparison_operator`.
     * @param ctx the parse tree
     */
    exitComparison_operator?: (ctx: Comparison_operatorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.assignment_operator`.
     * @param ctx the parse tree
     */
    enterAssignment_operator?: (ctx: Assignment_operatorContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.assignment_operator`.
     * @param ctx the parse tree
     */
    exitAssignment_operator?: (ctx: Assignment_operatorContext) => void;
    /**
     * Enter a parse tree produced by `TSqlParser.file_size`.
     * @param ctx the parse tree
     */
    enterFile_size?: (ctx: File_sizeContext) => void;
    /**
     * Exit a parse tree produced by `TSqlParser.file_size`.
     * @param ctx the parse tree
     */
    exitFile_size?: (ctx: File_sizeContext) => void;
}
