/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

parser grammar SqlServerParser;

options { tokenVocab=SqlServerLexer; }

tsql_file
    : (sql_clauses (SEMI | go_statement)*)+ EOF
    ;


batch_level_statement
    : create_or_alter_function
    | create_or_alter_procedure
    | create_or_alter_trigger
    | create_view
    ;
sql_clauses
    : dml_clause
    | cfl_statement
    | another_statement
    | ddl_clause
    | dbcc_clause
    | backup_statement
    | restore_statement
//    | go_statement
    | batch_level_statement
    | execute_body_batch
    ;

// Data Manipulation Language: https://msdn.microsoft.com/en-us/library/ff848766(v=sql.120).aspx
dml_clause
    : merge_statement
    | delete_statement
    | insert_statement
    | select_statement_standalone
    | update_statement
    ;

// Data Definition Language: https://msdn.microsoft.com/en-us/library/ff848799.aspx)
ddl_clause
    : alter_security_policy
    | add_signature
    | alter_database_scoped_credential
    | create_database_scoped_credential
    | alter_database_encryption_key
    | create_database_encryption_key
    | alter_application_role
    | alter_assembly
    | alter_asymmetric_key
    | alter_authorization
    | alter_authorization_for_azure_dw
    | alter_authorization_for_parallel_dw
    | alter_authorization_for_sql_database
    | alter_availability_group
    | alter_certificate
    | alter_column_encryption_key
    | alter_credential
    | alter_cryptographic_provider
    | alter_database
    | alter_database_scoped_configuration
    | alter_database_audit_specification
    | alter_db_role
    | alter_endpoint
    | alter_external_data_source
    | alter_external_library
    | alter_external_resource_pool
    | alter_fulltext_catalog
    | alter_fulltext_stoplist
    | alter_index
    | alter_login_sql_server
    | alter_master_key_sql_server
    | alter_message_type
    | alter_partition_function
    | alter_partition_scheme
    | alter_remote_service_binding
    | alter_resource_governor
    | alter_resource_pool
    | alter_route
    | alter_schema_azure_sql_dw_and_pdw
    | alter_schema_sql
    | alter_sequence
    | alter_server_audit
    | alter_server_audit_specification
    | alter_server_configuration
    | alter_server_role
    | alter_service
    | alter_service_master_key
    | alter_symmetric_key
    | alter_table
    | alter_user
    | alter_workload_group
    | alter_xml_schema_collection
    | create_application_role
    | create_assembly
    | create_availability_group
    | create_asymmetric_key
    | create_column_encryption_key
    | create_column_master_key
    | create_columnstore_index
    | create_credential
    | create_cryptographic_provider
    | create_database_audit_specification
    | create_database
    | create_db_role
    | create_endpoint
    | create_event_notification
    | create_external_library
    | create_external_resource_pool
    | create_fulltext_catalog
    | create_fulltext_stoplist
    | create_index
    | create_login_sql_server
    | create_master_key_sql_server
    | create_nonclustered_columnstore_index
    | create_or_alter_broker_priority
    | create_or_alter_event_session
    | create_partition_function
    | create_partition_scheme
    | create_remote_service_binding
    | create_resource_pool
    | create_route
    | create_rule
    | create_schema
    | create_schema_azure_sql_dw_and_pdw
    | create_search_property_list
    | create_security_policy
    | create_sequence
    | create_server_audit
    | create_server_audit_specification
    | create_server_role
    | create_service
    | create_statistics
    | create_synonym
    | create_table
    | create_type
    | create_user
    | create_workload_group
    | create_xml_index
    | create_xml_schema_collection
    | disable_trigger
    | drop_aggregate
    | drop_application_role
    | drop_assembly
    | drop_asymmetric_key
    | drop_availability_group
    | drop_broker_priority
    | drop_certificate
    | drop_column_encryption_key
    | drop_column_master_key
    | drop_contract
    | drop_credential
    | drop_cryptograhic_provider
    | drop_database_audit_specification
    | drop_database_encryption_key
    | drop_database_scoped_credential
    | drop_database
    | drop_db_role
    | drop_default
    | drop_endpoint
    | drop_event_notifications
    | drop_event_session
    | drop_external_data_source
    | drop_external_file_format
    | drop_external_library
    | drop_external_resource_pool
    | drop_external_table
    | drop_fulltext_catalog
    | drop_fulltext_index
    | drop_fulltext_stoplist
    | drop_function
    | drop_index
    | drop_login
    | drop_master_key
    | drop_message_type
    | drop_partition_function
    | drop_partition_scheme
    | drop_procedure
    | drop_queue
    | drop_remote_service_binding
    | drop_resource_pool
    | drop_route
    | drop_rule
    | drop_schema
    | drop_search_property_list
    | drop_security_policy
    | drop_sequence
    | drop_server_audit_specification
    | drop_server_audit
    | drop_server_role
    | drop_service
    | drop_signature
    | drop_statistics
    | drop_statistics_name_azure_dw_and_pdw
    | drop_symmetric_key
    | drop_synonym
    | drop_table
    | drop_trigger
    | drop_type
    | drop_user
    | drop_view
    | drop_workload_group
    | drop_xml_schema_collection
    | enable_trigger
    | lock_table
    | truncate_table
    | update_statistics
    ;

backup_statement
    : backup_database
    | backup_log
    | backup_snapshot
    | backup_certificate
    | backup_master_key
    | backup_service_master_key
    ;

// Control-of-Flow Language: https://docs.microsoft.com/en-us/sql/t-sql/language-elements/control-of-flow
cfl_statement
    : block_statement
    | break_statement
    | continue_statement
    | goto_statement
    | if_statement
    | print_statement
    | raiseerror_statement
    | return_statement
    | throw_statement
    | try_catch_statement
    | waitfor_statement
    | while_statement
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/begin-end-transact-sql
block_statement
    : BEGIN ';'? sql_clauses? (SEMI* sql_clauses)* SEMI* END
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/break-transact-sql
break_statement
    : BREAK ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/continue-transact-sql
continue_statement
    : CONTINUE ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/goto-transact-sql
goto_statement
    : GOTO id_ ';'?
    | id_ ':' ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/return-transact-sql
return_statement
    : RETURN expression? ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/if-else-transact-sql
if_statement
    : IF search_condition sql_clauses (ELSE sql_clauses)? ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/throw-transact-sql
throw_statement
    : THROW (throw_error_number ',' throw_message ',' throw_state)? ';'?
    ;

throw_error_number
    : DECIMAL | LOCAL_ID
    ;

throw_message
    : STRING | LOCAL_ID
    ;

throw_state
    : DECIMAL | LOCAL_ID
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/try-catch-transact-sql
try_catch_statement
    : BEGIN TRY ';'? try_clauses=sql_clauses+ END TRY ';'? BEGIN CATCH ';'? catch_clauses=sql_clauses* END CATCH ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/waitfor-transact-sql
waitfor_statement
    : WAITFOR receive_statement? ','? ((DELAY | TIME | TIMEOUT) time)?  expression? ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/while-transact-sql
while_statement
    : WHILE search_condition (sql_clauses | BREAK ';'? | CONTINUE ';'?)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/print-transact-sql
print_statement
    : PRINT (expression | DOUBLE_QUOTE_ID) (',' LOCAL_ID)* ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/raiserror-transact-sql
raiseerror_statement
    : RAISERROR '(' msgContent=(DECIMAL | STRING | LOCAL_ID) ',' severity=constant_LOCAL_ID ','
    state=constant_LOCAL_ID (',' (constant_LOCAL_ID | NULL_))* ')' (WITH (LOG | SETERROR | NOWAIT))? ';'?
    | RAISERROR DECIMAL formatstring=(STRING | LOCAL_ID | DOUBLE_QUOTE_ID) (',' argument=(DECIMAL | STRING | LOCAL_ID))*
    ;

empty_statement
    : ';'
    ;

another_statement
    : alter_queue
    | checkpoint_statement
    | conversation_statement
    | create_contract
    | create_queue
    | cursor_statement
    | declare_statement
    | execute_statement
    | kill_statement
    | message_statement
    | reconfigure_statement
    | security_statement
    | set_statement
    | setuser_statement
    | shutdown_statement
    | transaction_statement
    | use_statement
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-application-role-transact-sql
alter_application_role
    : ALTER APPLICATION ROLE application_role=id_ WITH application_role_option (',' application_role_option)*
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/statements/alter-xml-schema-collection-transact-sql?view=sql-server-ver16
alter_xml_schema_collection
    : ALTER XML SCHEMA COLLECTION (id_ '.')? id_ ADD STRING
    ;

create_application_role
    : CREATE APPLICATION ROLE application_role=id_ WITH PASSWORD '=' STRING
      (',' DEFAULT_SCHEMA '=' id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-aggregate-transact-sql

drop_aggregate
    : DROP AGGREGATE ( IF EXISTS )? ( schema_name=id_ DOT )? aggregate_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-application-role-transact-sql
drop_application_role
    : DROP APPLICATION ROLE rolename=id_
    ;

alter_assembly
    : alter_assembly_start assembly_name=id_ alter_assembly_clause
    ;

alter_assembly_start
    :  ALTER ASSEMBLY
    ;

alter_assembly_clause
    : alter_assembly_from_clause? alter_assembly_with_clause? alter_assembly_drop_clause? alter_assembly_add_clause?
    ;

alter_assembly_from_clause
    : alter_assembly_from_clause_start (client_assembly_specifier | alter_assembly_file_bits )
    ;

alter_assembly_from_clause_start
    : FROM
    ;

alter_assembly_drop_clause
    : alter_assembly_drop alter_assembly_drop_multiple_files
    ;

alter_assembly_drop_multiple_files
    : ALL
    | multiple_local_files
    ;

alter_assembly_drop
    : DROP
    ;

alter_assembly_add_clause
    : alter_asssembly_add_clause_start alter_assembly_client_file_clause
    ;

alter_asssembly_add_clause_start
    : ADD FILE FROM
    ;

// need to implement
alter_assembly_client_file_clause
    :  alter_assembly_file_name (alter_assembly_as id_)?
    ;

alter_assembly_file_name
    : STRING
    ;

//need to implement
alter_assembly_file_bits
    : alter_assembly_as id_
    ;

alter_assembly_as
    : AS
    ;

alter_assembly_with_clause
    : alter_assembly_with assembly_option
    ;

alter_assembly_with
    : WITH
    ;

client_assembly_specifier
    : network_file_share
    | local_file
    | STRING
    ;

assembly_option
    : PERMISSION_SET EQUAL (SAFE|EXTERNAL_ACCESS|UNSAFE)
    | VISIBILITY EQUAL on_off
    | UNCHECKED DATA
    | assembly_option COMMA
    ;

network_file_share
    : network_file_start network_computer file_path
    ;

network_computer
    : computer_name=id_
    ;

network_file_start
    : DOUBLE_BACK_SLASH
    ;

file_path
    : file_directory_path_separator file_path
    | id_
    ;

file_directory_path_separator
    : '\\'
    ;

local_file
    : local_drive file_path
    ;

local_drive
    :
    DISK_DRIVE
    ;
multiple_local_files
    :
    multiple_local_file_start local_file SINGLE_QUOTE COMMA
    | local_file
    ;

multiple_local_file_start
    : SINGLE_QUOTE
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-assembly-transact-sql
create_assembly
    : CREATE ASSEMBLY assembly_name=id_ (AUTHORIZATION owner_name=id_)?
       FROM (COMMA? (STRING|BINARY) )+
       (WITH PERMISSION_SET EQUAL (SAFE|EXTERNAL_ACCESS|UNSAFE) )?

    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-assembly-transact-sql
drop_assembly
    : DROP ASSEMBLY ( IF EXISTS )? (COMMA? assembly_name=id_)+
       ( WITH NO DEPENDENTS )?
    ;
// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-asymmetric-key-transact-sql

alter_asymmetric_key
    : alter_asymmetric_key_start Asym_Key_Name=id_ (asymmetric_key_option | REMOVE PRIVATE KEY )
    ;

alter_asymmetric_key_start
    : ALTER ASYMMETRIC KEY
    ;

asymmetric_key_option
    : asymmetric_key_option_start asymmetric_key_password_change_option ( COMMA asymmetric_key_password_change_option)? RR_BRACKET
    ;

asymmetric_key_option_start
    : WITH PRIVATE KEY LR_BRACKET
    ;

asymmetric_key_password_change_option
    : DECRYPTION BY PASSWORD EQUAL STRING
    | ENCRYPTION BY PASSWORD EQUAL STRING
    ;


//https://docs.microsoft.com/en-us/sql/t-sql/statements/create-asymmetric-key-transact-sql

create_asymmetric_key
    : CREATE ASYMMETRIC KEY Asym_Key_Nam=id_
      (AUTHORIZATION database_principal_name=id_)?
      (FROM (FILE '=' STRING | EXECUTABLE FILE '=' STRING | ASSEMBLY id_ | PROVIDER id_))?
      (WITH asymmetric_key_create_option (',' asymmetric_key_create_option)*)?
      (ENCRYPTION BY PASSWORD '=' STRING)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-asymmetric-key-transact-sql
drop_asymmetric_key
    : DROP ASYMMETRIC KEY key_name=id_ ( REMOVE PROVIDER KEY )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-authorization-transact-sql

alter_authorization
    : alter_authorization_start (class_type colon_colon)? entity=entity_name entity_to authorization_grantee
    ;

authorization_grantee
    : principal_name=id_
    | SCHEMA OWNER
    ;

entity_to
    : TO
    ;

colon_colon
    : DOUBLE_COLON
    ;

alter_authorization_start
    : ALTER AUTHORIZATION ON
    ;

alter_authorization_for_sql_database
    : alter_authorization_start (class_type_for_sql_database colon_colon)? entity=entity_name entity_to authorization_grantee
    ;

alter_authorization_for_azure_dw
    : alter_authorization_start (class_type_for_azure_dw colon_colon)? entity=entity_name_for_azure_dw entity_to authorization_grantee
    ;

alter_authorization_for_parallel_dw
    : alter_authorization_start (class_type_for_parallel_dw colon_colon)? entity=entity_name_for_parallel_dw entity_to authorization_grantee
    ;


class_type
    : OBJECT
    | ASSEMBLY
    | ASYMMETRIC KEY
    | AVAILABILITY GROUP
    | CERTIFICATE
    | CONTRACT
    | TYPE
    | DATABASE
    | ENDPOINT
    | FULLTEXT CATALOG
    | FULLTEXT STOPLIST
    | MESSAGE TYPE
    | REMOTE SERVICE BINDING
    | ROLE
    | ROUTE
    | SCHEMA
    | SEARCH PROPERTY LIST
    | SERVER ROLE
    | SERVICE
    | SYMMETRIC KEY
    | XML SCHEMA COLLECTION
    ;

class_type_for_sql_database
    :  OBJECT
    | ASSEMBLY
    | ASYMMETRIC KEY
    | CERTIFICATE
    | TYPE
    | DATABASE
    | FULLTEXT CATALOG
    | FULLTEXT STOPLIST
    | ROLE
    | SCHEMA
    | SEARCH PROPERTY LIST
    | SYMMETRIC KEY
    | XML SCHEMA COLLECTION
    ;

class_type_for_azure_dw
    : SCHEMA
    | OBJECT
    ;

class_type_for_parallel_dw
    : DATABASE
    | SCHEMA
    | OBJECT
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/grant-transact-sql?view=sql-server-ver15
// SELECT DISTINCT '| ' + CLASS_DESC
// FROM sys.dm_audit_actions
// ORDER BY 1
class_type_for_grant
    : APPLICATION ROLE
    | ASSEMBLY
    | ASYMMETRIC KEY
    | AUDIT
    | AVAILABILITY GROUP
    | BROKER PRIORITY
    | CERTIFICATE
    | COLUMN ( ENCRYPTION | MASTER ) KEY
    | CONTRACT
    | CREDENTIAL
    | CRYPTOGRAPHIC PROVIDER
    | DATABASE ( AUDIT SPECIFICATION
               | ENCRYPTION KEY
               | EVENT SESSION
               | SCOPED ( CONFIGURATION
                        | CREDENTIAL
                        | RESOURCE GOVERNOR )
               )?
    | ENDPOINT
    | EVENT SESSION
    | NOTIFICATION (DATABASE | OBJECT | SERVER)
    | EXTERNAL ( DATA SOURCE
               | FILE FORMAT
               | LIBRARY
               | RESOURCE POOL
               | TABLE
               | CATALOG
               | STOPLIST
               )
    | LOGIN
    | MASTER KEY
    | MESSAGE TYPE
    | OBJECT
    | PARTITION ( FUNCTION | SCHEME)
    | REMOTE SERVICE BINDING
    | RESOURCE GOVERNOR
    | ROLE
    | ROUTE
    | SCHEMA
    | SEARCH PROPERTY LIST
    | SERVER ( ( AUDIT SPECIFICATION? ) | ROLE )?
    | SERVICE
    | SQL LOGIN
    | SYMMETRIC KEY
    | TRIGGER ( DATABASE | SERVER)
    | TYPE
    | USER
    | XML SCHEMA COLLECTION
    ;



// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-availability-group-transact-sql
drop_availability_group
    : DROP AVAILABILITY GROUP group_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-availability-group-transact-sql
alter_availability_group
    : alter_availability_group_start alter_availability_group_options
    ;

alter_availability_group_start
    : ALTER AVAILABILITY GROUP group_name=id_
    ;

alter_availability_group_options
    : SET '(' availability_group_set_option ')'
    | (ADD | REMOVE) DATABASE id_
    | ADD REPLICA ON availability_replica
    | MODIFY REPLICA ON availability_replica
    | REMOVE REPLICA ON STRING
    | JOIN (AVAILABILITY GROUP ON distributed_availability_group (',' distributed_availability_group)?)?
    | MODIFY AVAILABILITY GROUP ON distributed_availability_group (',' distributed_availability_group)?
    | (GRANT | DENY) CREATE ANY DATABASE
    | FAILOVER | FORCE_FAILOVER_ALLOW_DATA_LOSS | OFFLINE
    | ADD LISTENER STRING '(' availability_listener_options ')'
    | MODIFY LISTENER STRING '(' (ADD IP '(' availability_listener_ip ')' | PORT '=' DECIMAL) ')'
    | (RESTART | REMOVE) LISTENER STRING
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-broker-priority-transact-sql
// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-broker-priority-transact-sql
create_or_alter_broker_priority
    : (CREATE | ALTER) BROKER PRIORITY ConversationPriorityName=id_ FOR CONVERSATION
      SET LR_BRACKET
     ( CONTRACT_NAME EQUAL ( ( id_) | ANY )  COMMA?  )?
     ( LOCAL_SERVICE_NAME EQUAL (DOUBLE_FORWARD_SLASH? id_ | ANY ) COMMA? )?
     ( REMOTE_SERVICE_NAME  EQUAL (RemoteServiceName=STRING | ANY ) COMMA? )?
     ( PRIORITY_LEVEL EQUAL ( PriorityValue=DECIMAL | DEFAULT ) ) ?
     RR_BRACKET
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-broker-priority-transact-sql
drop_broker_priority
    : DROP BROKER PRIORITY ConversationPriorityName=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-certificate-transact-sql
alter_certificate
    : ALTER CERTIFICATE certificate_name=id_
      (REMOVE PRIVATE KEY
      | WITH PRIVATE KEY '(' private_key_options ')'
      | WITH ACTIVE FOR BEGIN_DIALOG '=' on_off)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-column-encryption-key-transact-sql
alter_column_encryption_key
    : ALTER COLUMN ENCRYPTION KEY column_encryption_key=id_
      (ADD VALUE column_encryption_key_value | DROP VALUE '(' COLUMN_MASTER_KEY '=' id_ ')')
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-column-encryption-key-transact-sql
create_column_encryption_key
    : CREATE COLUMN ENCRYPTION KEY column_encryption_key=id_
      WITH VALUES column_encryption_key_value (',' column_encryption_key_value)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-certificate-transact-sql
drop_certificate
    : DROP CERTIFICATE certificate_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-column-encryption-key-transact-sql
drop_column_encryption_key
    : DROP COLUMN ENCRYPTION KEY key_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-column-master-key-transact-sql
drop_column_master_key
    : DROP COLUMN MASTER KEY key_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-contract-transact-sql
drop_contract
    : DROP CONTRACT dropped_contract_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-credential-transact-sql
drop_credential
    : DROP CREDENTIAL credential_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-cryptographic-provider-transact-sql
drop_cryptograhic_provider
    : DROP CRYPTOGRAPHIC PROVIDER provider_name=id_
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-database-transact-sql
drop_database
    : DROP DATABASE (IF EXISTS)? database_name_or_database_snapshot_name+=id_
      (',' database_name_or_database_snapshot_name+=id_)*
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-database-audit-specification-transact-sql
drop_database_audit_specification
    : DROP DATABASE AUDIT SPECIFICATION audit_specification_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-database-encryption-key-transact-sql?view=sql-server-ver15
drop_database_encryption_key
    : DROP DATABASE ENCRYPTION KEY
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-database-scoped-credential-transact-sql
drop_database_scoped_credential
   : DROP DATABASE SCOPED CREDENTIAL credential_name=id_
   ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-default-transact-sql
drop_default
    : DROP DEFAULT ( IF EXISTS )? (COMMA? (schema_name=id_ DOT)? default_name=id_)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-endpoint-transact-sql
drop_endpoint
    : DROP ENDPOINT endPointName=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-external-data-source-transact-sql
drop_external_data_source
    : DROP EXTERNAL DATA SOURCE external_data_source_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-external-file-format-transact-sql
drop_external_file_format
    : DROP EXTERNAL FILE FORMAT external_file_format_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-external-library-transact-sql
drop_external_library
    : DROP EXTERNAL LIBRARY library_name=id_
( AUTHORIZATION owner_name=id_ )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-external-resource-pool-transact-sql
drop_external_resource_pool
    : DROP EXTERNAL RESOURCE POOL pool_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-external-table-transact-sql
drop_external_table
    : DROP EXTERNAL TABLE (database_name=id_ DOT)? (schema_name=id_ DOT)? table=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-event-notification-transact-sql
drop_event_notifications
    : DROP EVENT NOTIFICATION id_ (',' id_)* ON (SERVER | DATABASE | QUEUE full_table_name)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-event-session-transact-sql
drop_event_session
    : DROP EVENT SESSION event_session_name=id_
        ON SERVER
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-fulltext-catalog-transact-sql
drop_fulltext_catalog
    : DROP FULLTEXT CATALOG catalog_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-fulltext-index-transact-sql
drop_fulltext_index
    : DROP FULLTEXT INDEX ON (schema=id_ DOT)? table=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-fulltext-stoplist-transact-sql
drop_fulltext_stoplist
    : DROP FULLTEXT STOPLIST stoplist_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-login-transact-sql
drop_login
    : DROP LOGIN login_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-master-key-transact-sql
drop_master_key
    : DROP MASTER KEY
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-message-type-transact-sql
drop_message_type
    : DROP MESSAGE TYPE message_type_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-partition-function-transact-sql
drop_partition_function
    : DROP PARTITION FUNCTION partition_function_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-partition-scheme-transact-sql
drop_partition_scheme
    : DROP PARTITION SCHEME partition_scheme_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-queue-transact-sql
drop_queue
    : DROP QUEUE (database_name=id_ DOT)? (schema_name=id_ DOT)? queue_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-remote-service-binding-transact-sql
drop_remote_service_binding
    : DROP REMOTE SERVICE BINDING binding_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-resource-pool-transact-sql
drop_resource_pool
    : DROP RESOURCE POOL pool_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-role-transact-sql
drop_db_role
    : DROP ROLE ( IF EXISTS )? role_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-route-transact-sql
drop_route
    : DROP ROUTE route_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-rule-transact-sql
drop_rule
    : DROP RULE ( IF EXISTS )? (COMMA? (schema_name=id_ DOT)? rule_name=id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-schema-transact-sql
drop_schema
    :  DROP SCHEMA ( IF EXISTS )? schema_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-search-property-list-transact-sql
drop_search_property_list
    : DROP SEARCH PROPERTY LIST property_list_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-security-policy-transact-sql
drop_security_policy
    : DROP SECURITY POLICY ( IF EXISTS )? (schema_name=id_ DOT )? security_policy_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-sequence-transact-sql
drop_sequence
    : DROP SEQUENCE ( IF EXISTS )? ( COMMA? (database_name=id_ DOT)? (schema_name=id_ DOT)?          sequence_name=id_ )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-server-audit-transact-sql
drop_server_audit
    : DROP SERVER AUDIT audit_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-server-audit-specification-transact-sql
drop_server_audit_specification
    : DROP SERVER AUDIT SPECIFICATION audit_specification_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-server-role-transact-sql
drop_server_role
    : DROP SERVER ROLE role_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-service-transact-sql
drop_service
    : DROP SERVICE dropped_service_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-signature-transact-sql
drop_signature
    : DROP COUNTER? SIGNATURE FROM (OBJECT '::')? (schema_name=id_ '.')? module_name=id_
      BY (CERTIFICATE | ASYMMETRIC KEY) id_ (',' (CERTIFICATE | ASYMMETRIC KEY) id_)*
    ;


drop_statistics_name_azure_dw_and_pdw
    :  DROP STATISTICS  (schema_name=id_ DOT)? object_name=id_ DOT statistics_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-symmetric-key-transact-sql
drop_symmetric_key
    : DROP SYMMETRIC KEY symmetric_key_name=id_ (REMOVE PROVIDER KEY)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-synonym-transact-sql
drop_synonym
    : DROP SYNONYM ( IF EXISTS )? ( schema=id_ DOT )? synonym_name=id_
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-user-transact-sql
drop_user
    : DROP USER ( IF EXISTS )? user_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-workload-group-transact-sql
drop_workload_group
    : DROP WORKLOAD GROUP group_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-xml-schema-collection-transact-sql
drop_xml_schema_collection
    : DROP XML SCHEMA COLLECTION ( relational_schema=id_ DOT )?  sql_identifier=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/disable-trigger-transact-sql
disable_trigger
    : DISABLE TRIGGER ( ( COMMA? (schema_name=id_ DOT)? trigger_name=id_ )+ | ALL)         ON ((schema_id=id_ DOT)? object_name=id_|DATABASE|ALL SERVER)
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/enable-trigger-transact-sql
enable_trigger
    : ENABLE TRIGGER ( ( COMMA? (schema_name=id_ DOT)? trigger_name=id_ )+ | ALL)         ON ( (schema_id=id_ DOT)? object_name=id_|DATABASE|ALL SERVER)
    ;

lock_table
    : LOCK TABLE table_name IN (SHARE | EXCLUSIVE) MODE (WAIT seconds=DECIMAL | NOWAIT)? ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/truncate-table-transact-sql
truncate_table
    : TRUNCATE TABLE table_name
          ( WITH LR_BRACKET
              PARTITIONS LR_BRACKET
                                (COMMA? (DECIMAL|DECIMAL TO DECIMAL) )+
                         RR_BRACKET

                 RR_BRACKET
          )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-column-master-key-transact-sql
create_column_master_key
    : CREATE COLUMN MASTER KEY key_name=id_ WITH '('
      KEY_STORE_PROVIDER_NAME '=' STRING ',' KEY_PATH '=' STRING
      (',' ENCLAVE_COMPUTATIONS '(' SIGNATURE '=' BINARY ')')? ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-credential-transact-sql
alter_credential
    : ALTER CREDENTIAL credential_name=id_
        WITH IDENTITY EQUAL identity_name=STRING
         ( COMMA SECRET EQUAL secret=STRING )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-credential-transact-sql
create_credential
    : CREATE CREDENTIAL credential_name=id_
        WITH IDENTITY EQUAL identity_name=STRING
         ( COMMA SECRET EQUAL secret=STRING )?
         (  FOR CRYPTOGRAPHIC PROVIDER cryptographic_provider_name=id_ )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-cryptographic-provider-transact-sql
alter_cryptographic_provider
    : ALTER CRYPTOGRAPHIC PROVIDER provider_name=id_ (FROM FILE EQUAL crypto_provider_ddl_file=STRING)? (ENABLE | DISABLE)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-cryptographic-provider-transact-sql
create_cryptographic_provider
    : CREATE CRYPTOGRAPHIC PROVIDER provider_name=id_
      FROM FILE EQUAL path_of_DLL=STRING
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/statements/create-endpoint-transact-sql?view=sql-server-ver16
create_endpoint
    : CREATE ENDPOINT endpointname=id_ (AUTHORIZATION login=id_)?
      (STATE '=' (STARTED | STOPPED | DISABLED))?
      AS TCP '(' endpoint_listener_clause ')' endpoint_protocol
    ;

endpoint_encryption_alogorithm_clause
    : ENCRYPTION EQUAL (DISABLED | SUPPORTED | REQUIRED) (ALGORITHM (AES RC4? | RC4 AES?))?
    ;

endpoint_authentication_clause
    : AUTHENTICATION EQUAL
        ( WINDOWS (NTLM | KERBEROS | NEGOTIATE)? (CERTIFICATE cert_name=id_)?
        | CERTIFICATE cert_name=id_ WINDOWS? (NTLM | KERBEROS | NEGOTIATE)?
        )
    ;

endpoint_listener_clause
    : LISTENER_PORT EQUAL port=DECIMAL
        (COMMA LISTENER_IP EQUAL (ALL | '(' (ipv4=IPV4_ADDR | ipv6=STRING) ')'))?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-event-notification-transact-sql
create_event_notification
    : CREATE EVENT NOTIFICATION event_notification_name=id_
      ON (SERVER | DATABASE | QUEUE full_table_name) (WITH FAN_IN)?
      FOR id_ (',' id_)* TO SERVICE STRING ',' STRING
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-event-session-transact-sql
// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-event-session-transact-sql
create_or_alter_event_session
    : CREATE EVENT SESSION event_session_name=id_ ON SERVER
      xe_add_event (',' xe_add_event)* (','? xe_add_target (',' xe_add_target)*)? xe_session_options?
    | ALTER EVENT SESSION event_session_name=id_ ON SERVER
      (STATE '=' (START | STOP)
      | xe_event_change (',' xe_event_change)* (','? xe_target_change (',' xe_target_change)*)? xe_session_options?
      | xe_target_change (',' xe_target_change)* xe_session_options?
      | xe_session_options)
    ;

event_session_predicate_expression
    : NOT? event_session_predicate_factor ((AND | OR) NOT? event_session_predicate_factor)*
    ;

event_session_predicate_factor
    : event_session_predicate_leaf
    | LR_BRACKET event_session_predicate_expression RR_BRACKET
    ;

event_session_predicate_leaf
    : (id_ | xe_object_name) comparison_operator xe_literal
    | xe_object_name '(' (id_ | xe_object_name) ',' xe_literal ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-external-data-source-transact-sql
alter_external_data_source
    : ALTER EXTERNAL DATA SOURCE data_source_name=id_  SET
    ( LOCATION EQUAL location=STRING COMMA? |  RESOURCE_MANAGER_LOCATION EQUAL resource_manager_location=STRING COMMA? |  CREDENTIAL EQUAL credential_name=id_ )+
    | ALTER EXTERNAL DATA SOURCE data_source_name=id_ WITH LR_BRACKET TYPE EQUAL BLOB_STORAGE COMMA LOCATION EQUAL location=STRING (COMMA CREDENTIAL EQUAL credential_name=id_ )? RR_BRACKET
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-external-library-transact-sql
alter_external_library
    : ALTER EXTERNAL LIBRARY library_name=id_ (AUTHORIZATION owner_name=id_)?
       (SET|ADD) ( LR_BRACKET CONTENT EQUAL (client_library=STRING | BINARY | NONE) (COMMA PLATFORM EQUAL (WINDOWS|LINUX)? RR_BRACKET) WITH (COMMA? LANGUAGE EQUAL (R_1|PYTHON) | DATA_SOURCE EQUAL external_data_source_name=id_ )+ RR_BRACKET )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-external-library-transact-sql
create_external_library
    : CREATE EXTERNAL LIBRARY library_name=id_ (AUTHORIZATION owner_name=id_)?
       FROM (COMMA? LR_BRACKET?  (CONTENT EQUAL)? (client_library=STRING | BINARY | NONE) (COMMA PLATFORM EQUAL (WINDOWS|LINUX)? RR_BRACKET)? ) ( WITH (COMMA? LANGUAGE EQUAL (R_1|PYTHON) | DATA_SOURCE EQUAL external_data_source_name=id_ )+ RR_BRACKET  )?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-external-resource-pool-transact-sql
alter_external_resource_pool
    : ALTER EXTERNAL RESOURCE POOL (pool_name=id_ | DEFAULT_DOUBLE_QUOTE)
      (WITH '(' external_resource_pool_option (','? external_resource_pool_option)* ')')?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-external-resource-pool-transact-sql
create_external_resource_pool
    : CREATE EXTERNAL RESOURCE POOL pool_name=id_
      (WITH '(' external_resource_pool_option (','? external_resource_pool_option)* ')')?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-fulltext-catalog-transact-sql
alter_fulltext_catalog
    : ALTER FULLTEXT CATALOG catalog_name=id_ (REBUILD (WITH ACCENT_SENSITIVITY EQUAL (ON|OFF) )? | REORGANIZE | AS DEFAULT )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-fulltext-catalog-transact-sql
create_fulltext_catalog
    : CREATE FULLTEXT CATALOG catalog_name=id_
        (ON FILEGROUP filegroup=id_)?
        (IN PATH rootpath=STRING)?
        (WITH ACCENT_SENSITIVITY EQUAL (ON|OFF) )?
        (AS DEFAULT)?
        (AUTHORIZATION owner_name=id_)?
    ;



// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-fulltext-stoplist-transact-sql
alter_fulltext_stoplist
    : ALTER FULLTEXT STOPLIST stoplist_name=id_ (ADD stopword=STRING LANGUAGE (STRING|DECIMAL|BINARY) | DROP ( stopword=STRING LANGUAGE (STRING|DECIMAL|BINARY) |ALL (STRING|DECIMAL|BINARY) | ALL ) )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-fulltext-stoplist-transact-sql
create_fulltext_stoplist
    :   CREATE FULLTEXT STOPLIST stoplist_name=id_
          (FROM ( (database_name=id_ DOT)? source_stoplist_name=id_ |SYSTEM STOPLIST ) )?
          (AUTHORIZATION owner_name=id_)?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-login-transact-sql
alter_login_sql_server
    : ALTER LOGIN login_name=id_
      (ENABLE | DISABLE
      | WITH alter_login_option (',' alter_login_option)*
      | (ADD | DROP) CREDENTIAL credential_name=id_)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-login-transact-sql
create_login_sql_server
    : CREATE LOGIN login_name=id_
      (WITH PASSWORD '=' (password=STRING MUST_CHANGE? | password_hash=BINARY HASHED)
        (',' create_login_sql_server_with)*
      | FROM WINDOWS (WITH windows_login_option (',' windows_login_option)*)?
      | FROM CERTIFICATE certname=id_
      | FROM ASYMMETRIC KEY asym_key_name=id_
      | FROM EXTERNAL PROVIDER)
    ;

create_login_sql_server_with
    : SID '=' sid=BINARY
    | windows_login_option
    | CHECK_EXPIRATION '=' on_off
    | CHECK_POLICY '=' on_off
    | CREDENTIAL '=' credential_name=id_
    ;





// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-master-key-transact-sql
alter_master_key_sql_server
    : ALTER MASTER KEY ( (FORCE)? REGENERATE WITH ENCRYPTION BY PASSWORD EQUAL password=STRING |(ADD|DROP) ENCRYPTION BY (SERVICE MASTER KEY | PASSWORD EQUAL encryption_password=STRING) )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-master-key-transact-sql
create_master_key_sql_server
    : CREATE MASTER KEY ENCRYPTION BY PASSWORD EQUAL password=STRING
    ;




// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-message-type-transact-sql
alter_message_type
    : ALTER MESSAGE TYPE message_type_name=id_ VALIDATION EQUAL (NONE | EMPTY | WELL_FORMED_XML | VALID_XML WITH SCHEMA COLLECTION schema_collection_name=id_)
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-partition-function-transact-sql
alter_partition_function
    : ALTER PARTITION FUNCTION partition_function_name=id_ LR_BRACKET RR_BRACKET        (SPLIT|MERGE) RANGE LR_BRACKET DECIMAL RR_BRACKET
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-partition-scheme-transact-sql
alter_partition_scheme
    : ALTER PARTITION SCHEME partition_scheme_name=id_ NEXT USED (file_group_name=id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-remote-service-binding-transact-sql
alter_remote_service_binding
    : ALTER REMOTE SERVICE BINDING binding_name=id_
        WITH (USER EQUAL user_name=id_)?
             (COMMA ANONYMOUS EQUAL (ON|OFF) )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-remote-service-binding-transact-sql
create_remote_service_binding
    : CREATE REMOTE SERVICE BINDING binding_name=id_
         (AUTHORIZATION owner_name=id_)?
         TO SERVICE remote_service_name=STRING
         WITH (USER EQUAL user_name=id_)?
              (COMMA ANONYMOUS EQUAL (ON|OFF) )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-resource-pool-transact-sql
create_resource_pool
    : CREATE RESOURCE POOL pool_name=id_
      (WITH '(' resource_pool_option (','? resource_pool_option)* ')')?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-resource-governor-transact-sql
alter_resource_governor
    : ALTER RESOURCE GOVERNOR ( (DISABLE | RECONFIGURE) | WITH LR_BRACKET CLASSIFIER_FUNCTION EQUAL ( schema_name=id_ DOT function_name=id_ | NULL_ ) RR_BRACKET | RESET STATISTICS | WITH LR_BRACKET MAX_OUTSTANDING_IO_PER_VOLUME EQUAL max_outstanding_io_per_volume=DECIMAL RR_BRACKET )
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/statements/alter-database-audit-specification-transact-sql?view=sql-server-ver16
alter_database_audit_specification
    : ALTER DATABASE AUDIT SPECIFICATION audit_specification_name=id_
        (FOR SERVER AUDIT audit_name=id_)?
        (audit_action_spec_group (',' audit_action_spec_group)*)?
        (WITH '(' STATE '=' (ON|OFF) ')')?
    ;

audit_action_spec_group
    : (ADD | DROP) '(' database_audit_item (',' database_audit_item)* ')'
    ;

audit_action_specification
    : action_specification (',' action_specification)* ON (audit_class_name '::')? audit_securable BY principal_id (',' principal_id)*
    ;

action_specification
    : SELECT
    | INSERT
    | UPDATE
    | DELETE
    | EXECUTE
    | RECEIVE
    | REFERENCES
    ;

audit_class_name
    : OBJECT
    | SCHEMA
    | TABLE
    ;

audit_securable
    : ((id_ '.')? id_ '.')? id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-role-transact-sql
alter_db_role
    : ALTER ROLE role_name=id_
        ( (ADD|DROP) MEMBER database_principal=id_
        | WITH NAME EQUAL new_role_name=id_ )
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/statements/create-database-audit-specification-transact-sql?view=sql-server-ver16
create_database_audit_specification
    : CREATE DATABASE AUDIT SPECIFICATION audit_specification_name=id_
      FOR SERVER AUDIT audit_name=id_
      (ADD '(' database_audit_item (',' database_audit_item)* ')' (',' ADD '(' database_audit_item (',' database_audit_item)* ')')*)?
      (WITH '(' STATE '=' on_off ')')?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-role-transact-sql
create_db_role
    : CREATE ROLE role_name=id_ (AUTHORIZATION owner_name = id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-route-transact-sql
create_route
    : CREATE ROUTE route_name=id_
        (AUTHORIZATION owner_name=id_)?
        WITH
          (COMMA? SERVICE_NAME EQUAL route_service_name=STRING)?
          (COMMA? BROKER_INSTANCE EQUAL broker_instance_identifier=STRING)?
          (COMMA? LIFETIME EQUAL DECIMAL)?
          COMMA? ADDRESS EQUAL STRING
          (COMMA MIRROR_ADDRESS EQUAL STRING )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-rule-transact-sql
create_rule
    : CREATE RULE (schema_name=id_ DOT)? rule_name=id_
        AS search_condition
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-schema-transact-sql
alter_schema_sql
    : ALTER SCHEMA schema_name=id_ TRANSFER ((OBJECT|TYPE|XML SCHEMA COLLECTION) DOUBLE_COLON )? id_ (DOT id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-schema-transact-sql
create_schema
    : CREATE SCHEMA
    (schema_name=id_
        |AUTHORIZATION owner_name=id_
        | schema_name=id_ AUTHORIZATION owner_name=id_
        )
        (create_table
         |create_view
         | (GRANT|DENY) (SELECT|INSERT|DELETE|UPDATE) ON (SCHEMA DOUBLE_COLON)? object_name=id_ TO owner_name=id_
         | REVOKE (SELECT|INSERT|DELETE|UPDATE) ON (SCHEMA DOUBLE_COLON)? object_name=id_ FROM owner_name=id_
        )*
    ;

create_schema_azure_sql_dw_and_pdw
    :
CREATE SCHEMA schema_name=id_ (AUTHORIZATION owner_name=id_ )?
    ;

alter_schema_azure_sql_dw_and_pdw
    : ALTER SCHEMA schema_name=id_ TRANSFER (OBJECT DOUBLE_COLON )? id_ (DOT ID)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-search-property-list-transact-sql
create_search_property_list
    : CREATE SEARCH PROPERTY LIST new_list_name=id_
        (FROM (database_name=id_ DOT)? source_list_name=id_ )?
        (AUTHORIZATION owner_name=id_)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-security-policy-transact-sql
create_security_policy
    : CREATE SECURITY POLICY (schema_name=id_ '.')? security_policy_name=id_
      ADD security_predicate (',' ADD security_predicate)*
      (WITH '(' security_policy_option (',' security_policy_option)* ')')?
      (NOT FOR REPLICATION)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-sequence-transact-sql
alter_sequence
    : ALTER SEQUENCE (schema_name=id_ DOT)? sequence_name=id_ ( RESTART (WITH DECIMAL)? )? (INCREMENT BY sequnce_increment=DECIMAL )? ( MINVALUE DECIMAL| NO MINVALUE)? (MAXVALUE DECIMAL| NO MAXVALUE)? (CYCLE|NO CYCLE)? (CACHE DECIMAL | NO CACHE)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-sequence-transact-sql
create_sequence
    : CREATE SEQUENCE (schema_name=id_ DOT)? sequence_name=id_
        (AS data_type  )?
        (START WITH DECIMAL)?
        (INCREMENT BY MINUS? DECIMAL)?
        (MINVALUE (MINUS? DECIMAL)? | NO MINVALUE)?
        (MAXVALUE (MINUS? DECIMAL)? | NO MAXVALUE)?
        (CYCLE|NO CYCLE)?
        (CACHE DECIMAL? | NO CACHE)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-server-audit-transact-sql
alter_server_audit
    : ALTER SERVER AUDIT audit_name=id_
      (TO audit_target (WITH '(' audit_alter_option (',' audit_alter_option)* ')')? (WHERE audit_predicate)?
      | WITH '(' audit_alter_option (',' audit_alter_option)* ')' (WHERE audit_predicate)?
      | WHERE audit_predicate
      | REMOVE WHERE
      | MODIFY NAME '=' new_audit_name=id_)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-server-audit-transact-sql
create_server_audit
    : CREATE SERVER AUDIT audit_name=id_ TO
      (FILE '(' FILEPATH '=' STRING (',' audit_file_option)* ')' | APPLICATION_LOG | SECURITY_LOG)
      (WITH '(' audit_create_option (',' audit_create_option)* ')')?
      (WHERE audit_predicate)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-server-audit-specification-transact-sql

alter_server_audit_specification
    : ALTER SERVER AUDIT SPECIFICATION audit_specification_name=id_
      (FOR SERVER AUDIT audit_name=id_ (server_audit_spec_change (',' server_audit_spec_change)*)? (WITH '(' STATE '=' on_off ')')?
      | server_audit_spec_change (',' server_audit_spec_change)* (WITH '(' STATE '=' on_off ')')?
      | WITH '(' STATE '=' on_off ')')
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-server-audit-specification-transact-sql
create_server_audit_specification
    : CREATE SERVER AUDIT SPECIFICATION audit_specification_name=id_
      FOR SERVER AUDIT audit_name=id_ ADD '(' id_ ')' (',' ADD '(' id_ ')')*
      (WITH '(' STATE '=' on_off ')')?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-server-configuration-transact-sql

alter_server_configuration
    : ALTER SERVER CONFIGURATION SET
      ( PROCESS AFFINITY
        ( CPU '=' (AUTO | resource_affinity_range (',' resource_affinity_range)*)
        | NUMANODE '=' resource_affinity_range (',' resource_affinity_range)*
        )
      | DIAGNOSTICS LOG
        (on_off | PATH '=' (STRING | DEFAULT) | MAX_SIZE '=' (DECIMAL MB | DEFAULT) | MAX_FILES '=' (DECIMAL | DEFAULT))
      | FAILOVER CLUSTER PROPERTY
        ( VERBOSELOGGING '=' (STRING | DEFAULT)
        | SQLDUMPERFLAGS '=' (STRING | DEFAULT)
        | SQLDUMPERPATH '=' (STRING | DEFAULT)
        | SQLDUMPERTIMEOUT (STRING | DEFAULT)
        | FAILURECONDITIONLEVEL '=' (STRING | DEFAULT)
        | HEALTHCHECKTIMEOUT '=' (DECIMAL | DEFAULT)
        )
      | HADR CLUSTER CONTEXT '=' (STRING | LOCAL)
      | BUFFER POOL EXTENSION (ON '(' FILENAME '=' STRING ',' SIZE '=' DECIMAL (KB | MB | GB) ')' | OFF)
      | SOFTNUMA on_off
      | MEMORY_OPTIMIZED (TEMPDB_METADATA | HYBRID_BUFFER_POOL) '=' on_off
      | HARDWARE_OFFLOAD on_off
      | SUSPEND_FOR_SNAPSHOT_BACKUP '=' on_off
        ('(' (GROUP '=' '(' id_ (',' id_)* ')' (',' MODE '=' COPY_ONLY)? | MODE '=' COPY_ONLY) ')')?
      )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-server-role-transact-sql
alter_server_role
    : ALTER SERVER ROLE server_role_name=id_
      ( (ADD|DROP) MEMBER server_principal=id_
      | WITH NAME EQUAL new_server_role_name=id_
      )
    ;
// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-server-role-transact-sql
create_server_role
    : CREATE SERVER ROLE server_role=id_ (AUTHORIZATION server_principal=id_)?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-service-transact-sql
alter_service
    : ALTER SERVICE modified_service_name=id_ (ON QUEUE (schema_name=id_ DOT)? queue_name=id_)?  ('(' opt_arg_clause (COMMA opt_arg_clause)* ')')?
    ;

opt_arg_clause
    : (ADD|DROP) CONTRACT modified_contract_name=id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-service-transact-sql
create_service
    : CREATE SERVICE create_service_name=id_
        (AUTHORIZATION owner_name=id_)?
        ON QUEUE (schema_name=id_ DOT)? queue_name=id_
          ( LR_BRACKET (COMMA? (id_|DEFAULT) )+ RR_BRACKET )?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-service-master-key-transact-sql

alter_service_master_key
    : ALTER SERVICE MASTER KEY ( FORCE? REGENERATE | (WITH (OLD_ACCOUNT EQUAL acold_account_name=STRING COMMA OLD_PASSWORD EQUAL old_password=STRING | NEW_ACCOUNT EQUAL new_account_name=STRING COMMA NEW_PASSWORD EQUAL new_password=STRING)?  ) )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-symmetric-key-transact-sql

alter_symmetric_key
    : ALTER SYMMETRIC KEY key_name=id_ (ADD | DROP) ENCRYPTION BY
      encryption_mechanism (',' encryption_mechanism)*
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-synonym-transact-sql
create_synonym
    : CREATE SYNONYM (schema_name_1=id_ DOT )? synonym_name=id_
        FOR ( (server_name=id_ DOT )? (database_name=id_ DOT)? (schema_name_2=id_ DOT)? object_name=id_
            | (database_or_schema2=id_ DOT)? (schema_id_2_or_object_name=id_ DOT)?
            )
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-user-transact-sql
alter_user
    : ALTER USER username=id_ WITH alter_user_option (',' alter_user_option)*
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-user-transact-sql
create_user
    : CREATE USER user_name=id_
      (((FOR | FROM) LOGIN login_name=id_ | WITHOUT LOGIN)
        (WITH mapped_user_option (',' mapped_user_option)*)?
      | (FOR | FROM) CERTIFICATE cert_name=id_
      | (FOR | FROM) ASYMMETRIC KEY asym_key_name=id_
      | FROM EXTERNAL PROVIDER (WITH DEFAULT_SCHEMA '=' id_)?
      | WITH PASSWORD '=' password=STRING (',' contained_user_option)*
      | WITH contained_user_option (',' contained_user_option)*)?
    ;




// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-workload-group-transact-sql

alter_workload_group
    : ALTER WORKLOAD GROUP (workload_group_group_name=id_ | DEFAULT_DOUBLE_QUOTE)
      (WITH '(' workload_group_option (','? workload_group_option)* ')')?
      (USING (workload_group_pool_name=id_ | DEFAULT_DOUBLE_QUOTE))?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-workload-group-transact-sql
create_workload_group
    : CREATE WORKLOAD GROUP workload_group_group_name=id_
      (WITH '(' workload_group_option (','? workload_group_option)* ')')?
      (USING ((workload_group_pool_name=id_ | DEFAULT_DOUBLE_QUOTE)
              (','? EXTERNAL (external_pool_name=id_ | DEFAULT_DOUBLE_QUOTE))?
             | EXTERNAL (external_pool_name=id_ | DEFAULT_DOUBLE_QUOTE)))?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-xml-schema-collection-transact-sql
create_xml_schema_collection
    : CREATE XML SCHEMA COLLECTION (relational_schema=id_ DOT)? sql_identifier=id_ AS  (STRING|id_|LOCAL_ID)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-partition-function-transact-sql?view=sql-server-ver15
create_partition_function
    : CREATE PARTITION FUNCTION partition_function_name=id_ '(' input_parameter_type=data_type ')'
      AS RANGE ( LEFT | RIGHT )?
      FOR VALUES '(' boundary_values=expression_list_ ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-partition-scheme-transact-sql?view=sql-server-ver15
create_partition_scheme
    : CREATE PARTITION SCHEME partition_scheme_name=id_
      AS PARTITION partition_function_name=id_
      ALL? TO '(' file_group_names+=id_ (',' file_group_names+=id_)* ')'
    ;

create_queue
    : CREATE QUEUE (full_table_name | queue_name=id_) queue_settings?
      (ON filegroup=id_ | DEFAULT)?
    ;


queue_settings
    : WITH
       (STATUS EQUAL on_off COMMA?)?
       (RETENTION EQUAL on_off COMMA?)?
       (ACTIVATION
         LR_BRACKET
           (
             (
              (STATUS EQUAL on_off COMMA? )?
              (PROCEDURE_NAME EQUAL func_proc_name_database_schema COMMA?)?
              (MAX_QUEUE_READERS EQUAL max_readers=DECIMAL COMMA?)?
              (EXECUTE AS (SELF | user_name=STRING | OWNER) COMMA?)?
             )
             | DROP
           )
         RR_BRACKET COMMA?
       )?
       (POISON_MESSAGE_HANDLING
         LR_BRACKET
           (STATUS EQUAL on_off)
         RR_BRACKET
       )?
    ;

alter_queue
    : ALTER QUEUE (full_table_name | queue_name=id_)
      (queue_settings | queue_action)
    ;

queue_action
    : REBUILD (WITH '(' queue_rebuild_options ')')?
    | REORGANIZE (WITH '(' LOB_COMPACTION '=' on_off ')')?
    | MOVE TO (id_ | DEFAULT)
    ;
queue_rebuild_options
    : MAXDOP EQUAL DECIMAL
    ;

create_contract
    : CREATE CONTRACT contract_name
      (AUTHORIZATION owner_name=id_)?
      LR_BRACKET ((message_type_name=id_ | DEFAULT)
          SENT BY (INITIATOR | TARGET | ANY ) COMMA?)+
      RR_BRACKET
    ;

conversation_statement
    : begin_conversation_timer
    | begin_conversation_dialog
    | end_conversation
    | move_conversation
    | get_conversation
    | send_conversation
    | waitfor_conversation
    ;

message_statement
    : CREATE MESSAGE TYPE message_type_name=id_ (AUTHORIZATION owner_name=id_)?
      (VALIDATION '=' (NONE | EMPTY | WELL_FORMED_XML | VALID_XML WITH SCHEMA COLLECTION (id_ '.')? id_))?
    ;

// DML

// https://docs.microsoft.com/en-us/sql/t-sql/statements/merge-transact-sql
// note that there's a limit on number of when_matches but it has to be done runtime due to different ordering of statements allowed
merge_statement
    : with_expression?
      MERGE (TOP '(' expression ')' PERCENT?)?
      INTO? ddl_object with_table_hints? as_table_alias?
      USING table_sources
      ON search_condition
      when_matches+
      output_clause?
      option_clause? ';'
    ;

when_matches
    : (WHEN MATCHED (AND search_condition)?
          THEN merge_matched)+
    | (WHEN NOT MATCHED (BY TARGET)? (AND search_condition)?
          THEN merge_not_matched)
    | (WHEN NOT MATCHED BY SOURCE (AND search_condition)?
          THEN merge_matched)+
    ;

merge_matched
    : UPDATE SET update_elem_merge (',' update_elem_merge)*
    | DELETE
    ;

merge_not_matched
    : INSERT ('(' column_name_list ')')?
      (table_value_constructor | DEFAULT VALUES)
    ;

// https://msdn.microsoft.com/en-us/library/ms189835.aspx
delete_statement
    : with_expression?
      DELETE (TOP '(' expression ')' PERCENT? | TOP DECIMAL)?
      FROM? delete_statement_from
      with_table_hints?
      output_clause?
      (FROM table_sources)?
      (WHERE (search_condition | CURRENT OF (GLOBAL? cursor_name | cursor_var=LOCAL_ID)))?
      for_clause? option_clause?
    ;

delete_statement_from
    : ddl_object
    | rowset_function_limited
    | table_var=LOCAL_ID
    ;

// https://msdn.microsoft.com/en-us/library/ms174335.aspx
insert_statement
    : with_expression?
      INSERT (TOP '(' expression ')' PERCENT?)?
      INTO? (ddl_object | rowset_function_limited)
      with_table_hints?
      ('(' insert_column_name_list ')')?
      output_clause?
      insert_statement_value
      for_clause? option_clause?
    ;

insert_statement_value
    : table_value_constructor
    | derived_table
    | execute_statement
    | DEFAULT VALUES
    ;


receive_statement
    : '('? RECEIVE (ALL | DISTINCT | top_clause | '*')
      (LOCAL_ID '=' expression ','?)* FROM full_table_name
      (INTO table_variable=id_ (WHERE where=search_condition))? ')'?
    ;

// https://msdn.microsoft.com/en-us/library/ms189499.aspx
select_statement_standalone
    : with_expression? select_statement
    ;

select_statement
    : query_expression select_order_by_clause? for_clause? option_clause?
    ;

time
    : (LOCAL_ID | constant)
    ;

// https://msdn.microsoft.com/en-us/library/ms177523.aspx
update_statement
    : with_expression?
      UPDATE (TOP '(' expression ')' PERCENT?)?
      (ddl_object | rowset_function_limited)
      with_table_hints?
      SET update_elem (',' update_elem)*
      output_clause?
      (FROM table_sources)?
      (WHERE (search_condition | CURRENT OF (GLOBAL? cursor_name | cursor_var=LOCAL_ID)))?
      for_clause? option_clause?
    ;

// https://msdn.microsoft.com/en-us/library/ms177564.aspx
output_clause
    : OUTPUT output_dml_list_elem (',' output_dml_list_elem)*
      (INTO (LOCAL_ID | table_name) ('(' column_name_list ')')? )?
    ;

output_dml_list_elem
    : (expression | asterisk) as_column_alias?
    ;

// DDL

// https://msdn.microsoft.com/en-ie/library/ms176061.aspx
create_database
    : CREATE DATABASE database=id_
      ( ON database_attach_file (',' database_attach_file)*
        FOR (ATTACH (WITH (ENABLE_BROKER | NEW_BROKER | ERROR_BROKER_CONVERSATIONS | RESTRICTED_USER))? | ATTACH_REBUILD_LOG)
      | ON database_snapshot_file (',' database_snapshot_file)* AS SNAPSHOT OF source_database=id_
      | (CONTAINMENT '=' (NONE | PARTIAL))?
        (ON PRIMARY? database_file_spec (',' database_file_spec)*)?
        (LOG ON database_file_spec (',' database_file_spec)*)?
        (COLLATE collation_name=id_)?
        (WITH create_database_option (',' create_database_option)*)?
      )
    ;

// https://msdn.microsoft.com/en-us/library/ms188783.aspx
create_index
    : CREATE UNIQUE? clustered? INDEX id_ ON table_name '(' column_name_list_with_order ')'
    (INCLUDE '(' column_name_list ')' )?
    (WHERE where=search_condition)?
    (create_index_options)?
    (ON id_)?
    ;

create_index_options
    : WITH '(' relational_index_option (',' relational_index_option)* ')'
    ;

relational_index_option
    : rebuild_index_option
    | DROP_EXISTING '=' on_off
    | OPTIMIZE_FOR_SEQUENTIAL_KEY '=' on_off
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-index-transact-sql
alter_index
    : ALTER INDEX (id_ | ALL) ON table_name (DISABLE | PAUSE | ABORT | RESUME resumable_index_options? | reorganize_partition | set_index_options | rebuild_partition)
    ;

resumable_index_options
    : WITH '(' (resumable_index_option (',' resumable_index_option)*) ')'
    ;

resumable_index_option
    : MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | MAX_DURATION '=' max_duration=DECIMAL MINUTES?
    | low_priority_lock_wait
    ;

reorganize_partition
    : REORGANIZE (PARTITION '=' DECIMAL)? reorganize_options?
    ;

reorganize_options
    : WITH '(' (reorganize_option (',' reorganize_option)*) ')'
    ;

reorganize_option
    : LOB_COMPACTION '=' on_off
    | COMPRESS_ALL_ROW_GROUPS '=' on_off
    ;

set_index_options
    : SET '(' set_index_option (',' set_index_option)* ')'
    ;

set_index_option
    : ALLOW_ROW_LOCKS '=' on_off
    | ALLOW_PAGE_LOCKS '=' on_off
    | OPTIMIZE_FOR_SEQUENTIAL_KEY '=' on_off
    | IGNORE_DUP_KEY '=' on_off
    | STATISTICS_NORECOMPUTE '=' on_off
    | COMPRESSION_DELAY '=' delay=DECIMAL MINUTES?
    ;

rebuild_partition
    : REBUILD (PARTITION '=' ALL)? rebuild_index_options?
    | REBUILD PARTITION '=' DECIMAL single_partition_rebuild_index_options?
    ;

rebuild_index_options
    : WITH '(' rebuild_index_option (',' rebuild_index_option)* ')'
    ;

rebuild_index_option
    : PAD_INDEX '=' on_off
    | FILLFACTOR '=' DECIMAL
    | SORT_IN_TEMPDB '=' on_off
    | IGNORE_DUP_KEY '=' on_off
    | STATISTICS_NORECOMPUTE '=' on_off
    | STATISTICS_INCREMENTAL '=' on_off
    | ONLINE '=' (ON ('(' low_priority_lock_wait ')')? | OFF)
    | RESUMABLE '=' on_off
    | MAX_DURATION '=' times=DECIMAL MINUTES?
    | ALLOW_ROW_LOCKS '=' on_off
    | ALLOW_PAGE_LOCKS '=' on_off
    | MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | DATA_COMPRESSION '=' (NONE | ROW | PAGE | COLUMNSTORE | COLUMNSTORE_ARCHIVE)
        on_partitions?
    | XML_COMPRESSION '=' on_off
        on_partitions?
    ;

single_partition_rebuild_index_options
    : WITH '(' single_partition_rebuild_index_option (',' single_partition_rebuild_index_option)* ')'
    ;

single_partition_rebuild_index_option
    : SORT_IN_TEMPDB '=' on_off
    | MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | RESUMABLE '=' on_off
    | DATA_COMPRESSION '=' (NONE | ROW | PAGE | COLUMNSTORE | COLUMNSTORE_ARCHIVE)
        on_partitions?
    | XML_COMPRESSION '=' on_off
        on_partitions?
    | ONLINE '=' (ON ('(' low_priority_lock_wait ')')? | OFF)
    ;

on_partitions
    : ON PARTITIONS '('
        partition_number=DECIMAL ( TO to_partition_number=DECIMAL )?
        ( ',' partition_number=DECIMAL ( TO to_partition_number=DECIMAL )? )*
    ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-columnstore-index-transact-sql?view=sql-server-ver15
create_columnstore_index
    : CREATE CLUSTERED COLUMNSTORE INDEX id_ ON table_name
    create_columnstore_index_options?
    (ON id_)?
    ;

create_columnstore_index_options
    : WITH '(' columnstore_index_option (',' columnstore_index_option)* ')'
    ;

columnstore_index_option
    :
      DROP_EXISTING '=' on_off
    | MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | ONLINE '=' on_off
    | COMPRESSION_DELAY '=' delay=DECIMAL MINUTES?
    | DATA_COMPRESSION '=' (COLUMNSTORE | COLUMNSTORE_ARCHIVE)
        on_partitions?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-columnstore-index-transact-sql?view=sql-server-ver15
create_nonclustered_columnstore_index
    : CREATE NONCLUSTERED? COLUMNSTORE INDEX id_ ON table_name '(' column_name_list_with_order ')'
    (WHERE search_condition)?
    create_columnstore_index_options?
    (ON id_)?
    ;

create_xml_index
    : CREATE PRIMARY? XML INDEX id_ ON table_name '(' id_ ')'
    (USING XML INDEX id_ (FOR (VALUE | PATH | PROPERTY)?)?)?
    xml_index_options?
    ;

xml_index_options
    : WITH '(' xml_index_option (',' xml_index_option)* ')'
    ;

xml_index_option
    : PAD_INDEX '=' on_off
    | FILLFACTOR '=' DECIMAL
    | SORT_IN_TEMPDB '=' on_off
    | IGNORE_DUP_KEY '=' on_off
    | DROP_EXISTING '=' on_off
    | ONLINE '=' (ON ('(' low_priority_lock_wait ')')? | OFF)
    | ALLOW_ROW_LOCKS '=' on_off
    | ALLOW_PAGE_LOCKS '=' on_off
    | MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | XML_COMPRESSION '=' on_off
    ;


// https://msdn.microsoft.com/en-us/library/ms187926(v=sql.120).aspx
create_or_alter_procedure
    : ((CREATE (OR (ALTER | REPLACE))?) | ALTER) proc=(PROC | PROCEDURE) procName=func_proc_name_schema (';' DECIMAL)?
      ('('? procedure_param (',' procedure_param)* ')'?)?
      (WITH procedure_option (',' procedure_option)*)?
      (FOR REPLICATION)? AS (as_external_name | sql_clauses*)
    ;

as_external_name
    : EXTERNAL NAME assembly_name = id_ '.' class_name = id_ '.' method_name = id_
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-trigger-transact-sql
create_or_alter_trigger
    : create_or_alter_dml_trigger
    | create_or_alter_ddl_trigger
    ;

create_or_alter_dml_trigger
    : (CREATE (OR (ALTER | REPLACE))? | ALTER) TRIGGER simple_name
      ON table_name
      (WITH dml_trigger_option (',' dml_trigger_option)* )?
      (FOR | AFTER | INSTEAD OF)
      dml_trigger_operation (',' dml_trigger_operation)*
      (WITH APPEND)?
      (NOT FOR REPLICATION)?
      AS sql_clauses
    ;

dml_trigger_option
    : ENCRYPTION
    | execute_clause
    ;

dml_trigger_operation
    : (INSERT | UPDATE | DELETE)
    ;

create_or_alter_ddl_trigger
    : (CREATE (OR (ALTER | REPLACE))? | ALTER) TRIGGER simple_name
      ON (ALL SERVER | DATABASE)
      (WITH dml_trigger_option (',' dml_trigger_option)* )?
      (FOR | AFTER) ddl_trigger_operation (',' ddl_trigger_operation)*
      AS sql_clauses+
    ;

ddl_trigger_operation
    : simple_id
    ;

// https://msdn.microsoft.com/en-us/library/ms186755.aspx
create_or_alter_function
    : ((CREATE (OR ALTER)?) | ALTER) FUNCTION funcName=func_proc_name_schema
        (('(' procedure_param (',' procedure_param)* ')') | '(' ')') //must have (), but can be empty
        (func_body_returns_select | func_body_returns_table | func_body_returns_scalar)
    ;

func_body_returns_select
    : RETURNS TABLE
        (WITH function_option (',' function_option)*)?
        AS? (as_external_name | RETURN ('(' select_statement_standalone ')' | select_statement_standalone))
    ;

func_body_returns_table
    : RETURNS LOCAL_ID table_type_definition
        (WITH function_option (',' function_option)*)?
        AS? (as_external_name |
        BEGIN
           sql_clauses*
           RETURN ';'?
        END ';'?)
    ;

func_body_returns_scalar
    : RETURNS data_type
        (WITH function_option (',' function_option)*)?
        AS? (as_external_name |
        BEGIN
           sql_clauses? (';'* sql_clauses)* ';'*
           RETURN ret=expression ';'?
       END)
    ;

procedure_param_default_value
    : NULL_
    | DEFAULT
    | constant
    | LOCAL_ID
    ;

procedure_param
    : LOCAL_ID AS? (type_schema=id_ '.')? data_type VARYING? ('=' default_val=procedure_param_default_value)? (OUT | OUTPUT | READONLY)?
    ;

procedure_option
    : ENCRYPTION
    | RECOMPILE
    | execute_clause
    ;

function_option
    : ENCRYPTION
    | SCHEMABINDING
    | RETURNS NULL_ ON NULL_ INPUT
    | CALLED ON NULL_ INPUT
    | execute_clause
    ;

// https://msdn.microsoft.com/en-us/library/ms188038.aspx
create_statistics
    : CREATE STATISTICS id_ ON table_name '(' column_name_list ')'
      (WHERE search_condition)?
      (WITH create_statistics_option (',' create_statistics_option)*)? ';'?
    ;

update_statistics
    : UPDATE STATISTICS full_table_name
        ( id_ | '(' id_ ( ',' id_ )* ')' )?
        update_statistics_options?
    ;

update_statistics_options
    : WITH update_statistics_option (',' update_statistics_option)*
    ;

update_statistics_option
    : ( FULLSCAN (','? PERSIST_SAMPLE_PERCENT '=' on_off )? )
    | ( SAMPLE number=DECIMAL (PERCENT | ROWS)
        (','? PERSIST_SAMPLE_PERCENT '=' on_off )? )
    | RESAMPLE on_partitions?
    | STATS_STREAM '=' stats_stream_=expression
    | ROWCOUNT '=' DECIMAL
    | PAGECOUNT '=' DECIMAL
    | ALL
    | COLUMNS
    | INDEX
    | NORECOMPUTE
    | INCREMENTAL '=' on_off
    | MAXDOP '=' max_dregree_of_parallelism=DECIMAL
    | AUTO_DROP '=' on_off
    ;

// https://msdn.microsoft.com/en-us/library/ms174979.aspx
create_table
    : CREATE TABLE table_name '(' column_def_table_constraints  (','? table_indices)*  ','? ')' (LOCK simple_id)? table_options* (ON id_ | DEFAULT | on_partition_or_filegroup)? (TEXTIMAGE_ON id_ | DEFAULT)?
    ;

table_indices
    : INDEX id_  UNIQUE? clustered? '(' column_name_list_with_order ')'
    | INDEX id_ CLUSTERED COLUMNSTORE
    | INDEX id_ NONCLUSTERED? COLUMNSTORE '(' column_name_list ')'
    create_table_index_options?
    (ON id_)?
    ;

table_options
    : WITH ('(' table_option (',' table_option)* ')' | table_option (',' table_option)*)
    ;

table_option
    : (simple_id | keyword) '=' (simple_id | keyword | on_off | DECIMAL)
    | CLUSTERED COLUMNSTORE INDEX | HEAP
    | FILLFACTOR '=' DECIMAL
    | DISTRIBUTION '=' HASH '(' id_ ')' | CLUSTERED INDEX '(' id_ (ASC | DESC)? (',' id_ (ASC | DESC)?)* ')'
    | DATA_COMPRESSION '=' (NONE | ROW | PAGE)
        on_partitions?
    | XML_COMPRESSION '=' on_off
        on_partitions?
    ;

create_table_index_options
    : WITH '(' create_table_index_option ( ',' create_table_index_option)* ')'
    ;

create_table_index_option
    : PAD_INDEX '=' on_off
    | FILLFACTOR '=' DECIMAL
    | IGNORE_DUP_KEY '=' on_off
    | STATISTICS_NORECOMPUTE '=' on_off
    | STATISTICS_INCREMENTAL '=' on_off
    | ALLOW_ROW_LOCKS '=' on_off
    | ALLOW_PAGE_LOCKS '=' on_off
    | OPTIMIZE_FOR_SEQUENTIAL_KEY '=' on_off
    | DATA_COMPRESSION '=' (NONE | ROW | PAGE | COLUMNSTORE | COLUMNSTORE_ARCHIVE)
        on_partitions?
    | XML_COMPRESSION '=' on_off
        on_partitions?
    ;

// https://msdn.microsoft.com/en-us/library/ms187956.aspx
create_view
    : (CREATE (OR (ALTER | REPLACE))? | ALTER) VIEW simple_name ('(' column_name_list ')')?
      (WITH view_attribute (',' view_attribute)*)?
      AS select_statement_standalone (WITH CHECK OPTION)?
    ;

view_attribute
    : ENCRYPTION | SCHEMABINDING | VIEW_METADATA
    ;

// https://msdn.microsoft.com/en-us/library/ms190273.aspx
alter_table
    : ALTER TABLE table_name (SET '(' LOCK_ESCALATION '=' (AUTO | TABLE | DISABLE) ')'
                             | ADD column_def_table_constraints
                             | ALTER COLUMN (column_definition | column_modifier)
                             | DROP COLUMN id_ (',' id_)*
                             | DROP CONSTRAINT constraint=id_
                             | WITH (CHECK | NOCHECK) ADD (CONSTRAINT constraint=id_)?
                                ( FOREIGN KEY '(' fk=column_name_list ')' REFERENCES table_name ('(' pk=column_name_list')')? (on_delete | on_update)*
                                | CHECK '(' search_condition ')' )
                             | (NOCHECK | CHECK) CONSTRAINT constraint=id_
                             | ENABLE CHANGE_TRACKING (WITH '(' TRACK_COLUMNS_UPDATED '=' on_off ')')?
                             | DISABLE CHANGE_TRACKING
                             | (ENABLE | DISABLE) TRIGGER id_?
                             | REBUILD table_options
                             | SWITCH switch_partition)

    ;

switch_partition
    : (PARTITION? source_partition_number_expression=expression)?
      TO target_table=table_name
      (PARTITION target_partition_number_expression=expression)?
      (WITH low_priority_lock_wait)?
    ;

low_priority_lock_wait
    : WAIT_AT_LOW_PRIORITY '('
      MAX_DURATION '=' max_duration=time MINUTES? ','
      ABORT_AFTER_WAIT '=' abort_after_wait=(NONE | SELF | BLOCKERS) ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms174269.aspx
alter_database
    : ALTER DATABASE (database=id_ | CURRENT)
      (MODIFY NAME '=' new_name=id_
      | COLLATE collation=id_
      | SET database_optionspec (',' database_optionspec)* (WITH termination)?
      | add_or_modify_files
      | add_or_modify_filegroups
      )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-database-transact-sql-file-and-filegroup-options?view=sql-server-ver15
add_or_modify_files
    : ADD FILE filespec (',' filespec)* (TO FILEGROUP filegroup_name=id_)?
    | ADD LOG FILE filespec (',' filespec)*
    | REMOVE FILE logical_file_name=id_
    | MODIFY FILE filespec
    ;

filespec
    : '('      NAME       '=' name=id_or_string
          (',' NEWNAME    '=' new_name=id_or_string )?
          (',' FILENAME   '=' file_name=STRING )?
          (',' SIZE       '=' size=file_size )?
          (',' MAXSIZE    '=' (max_size=file_size | UNLIMITED) )?
          (',' FILEGROWTH '=' growth_increment=file_size )?
          (',' OFFLINE )?
      ')'
    ;

add_or_modify_filegroups
    : ADD FILEGROUP filegroup_name=id_ (CONTAINS FILESTREAM | CONTAINS MEMORY_OPTIMIZED_DATA)?
    | REMOVE FILEGROUP filegrou_name=id_
    | MODIFY FILEGROUP filegrou_name=id_ (
          filegroup_updatability_option
        | DEFAULT
        | NAME '=' new_filegroup_name=id_
        | AUTOGROW_SINGLE_FILE
        | AUTOGROW_ALL_FILES
      )
    ;

filegroup_updatability_option
    : READONLY
    | READWRITE
    | READ_ONLY
    | READ_WRITE
    ;

// https://msdn.microsoft.com/en-us/library/bb522682.aspx
// Runtime check.
database_optionspec
    : auto_option
    | change_tracking_option
    | containment_option
    | cursor_option
    | database_mirroring_option
    | date_correlation_optimization_option
    | db_encryption_option
    | db_state_option
    | db_update_option
    | db_user_access_option
    | delayed_durability_option
    | external_access_option
    | FILESTREAM database_filestream_option
    | hadr_options
    | mixed_page_allocation_option
    | parameterization_option
    | query_store_options
    | automatic_tuning_options
    | recovery_option
//  | remote_data_archive_option
    | service_broker_option
    | snapshot_option
    | sql_option
    | target_recovery_time_option
    | TEMPORAL_HISTORY_RETENTION on_off
    | SUSPEND_FOR_SNAPSHOT_BACKUP '=' on_off ('(' MODE '=' COPY_ONLY ')')?
    | termination
    ;

// SQL Server 2022 Query Store configuration.
query_store_options
    : QUERY_STORE ('=' (OFF | ON ('(' query_store_option (',' query_store_option)* ')')?)
                  | '(' query_store_option (',' query_store_option)* ')'
                  | CLEAR ALL?)
    ;

query_store_option
    : OPERATION_MODE '=' (READ_ONLY | READ_WRITE)
    | CLEANUP_POLICY '=' '(' STALE_QUERY_THRESHOLD_DAYS '=' DECIMAL ')'
    | (DATA_FLUSH_INTERVAL_SECONDS | INTERVAL_LENGTH_MINUTES | MAX_STORAGE_SIZE_MB | MAX_PLANS_PER_QUERY) '=' DECIMAL
    | SIZE_BASED_CLEANUP_MODE '=' (AUTO | OFF)
    | QUERY_CAPTURE_MODE '=' (ALL | AUTO | NONE | CUSTOM)
    | WAIT_STATS_CAPTURE_MODE '=' on_off
    | QUERY_CAPTURE_POLICY '=' '(' query_capture_policy_option (',' query_capture_policy_option)* ')'
    ;

query_capture_policy_option
    : STALE_CAPTURE_POLICY_THRESHOLD '=' DECIMAL (HOURS | DAYS)
    | (EXECUTION_COUNT | TOTAL_COMPILE_CPU_TIME_MS | TOTAL_EXECUTION_CPU_TIME_MS) '=' DECIMAL
    ;

automatic_tuning_options
    : AUTOMATIC_TUNING '(' FORCE_LAST_GOOD_PLAN '=' (ON | OFF | DEFAULT) ')'
    ;

auto_option
    : AUTO_CLOSE on_off
    | AUTO_CREATE_STATISTICS (OFF | ON ('(' INCREMENTAL '=' on_off ')')?)
    | AUTO_SHRINK  on_off
    | AUTO_UPDATE_STATISTICS on_off
    | AUTO_UPDATE_STATISTICS_ASYNC  (ON | OFF )
    ;

change_tracking_option
    : CHANGE_TRACKING '=' (OFF | ON ('(' change_tracking_option_list (',' change_tracking_option_list)* ')')?)
    ;

change_tracking_option_list
    : AUTO_CLEANUP EQUAL on_off
    | CHANGE_RETENTION EQUAL DECIMAL ( DAYS | HOURS | MINUTES )
    ;

containment_option
    : CONTAINMENT EQUAL ( NONE | PARTIAL )
    ;

cursor_option
    : CURSOR_CLOSE_ON_COMMIT on_off
    | CURSOR_DEFAULT ( LOCAL | GLOBAL )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-endpoint-transact-sql
alter_endpoint
    : ALTER ENDPOINT endpointname=id_
      (AUTHORIZATION login=id_ (STATE '=' (STARTED | STOPPED | DISABLED))?
        (AS TCP '(' endpoint_listener_clause ')')? endpoint_protocol?
      | STATE '=' (STARTED | STOPPED | DISABLED) (AS TCP '(' endpoint_listener_clause ')')? endpoint_protocol?
      | AS TCP '(' endpoint_listener_clause ')' endpoint_protocol?
      | endpoint_protocol)
    ;

/* Will visit later
*/
database_mirroring_option
    : mirroring_set_option
    ;

mirroring_set_option
    : mirroring_partner  partner_option
    | mirroring_witness  witness_option
    ;
mirroring_partner
    : PARTNER
    ;

mirroring_witness
    : WITNESS
    ;

witness_partner_equal
    : EQUAL
    ;


partner_option
    : witness_partner_equal partner_server
    | FAILOVER
    | FORCE_SERVICE_ALLOW_DATA_LOSS
    | OFF
    | RESUME
    | SAFETY (FULL | OFF )
    | SUSPEND
    | TIMEOUT DECIMAL
    ;

witness_option
    : witness_partner_equal witness_server
    | OFF
    ;

witness_server
    : partner_server
    ;

partner_server
    : STRING
    ;

date_correlation_optimization_option
    : DATE_CORRELATION_OPTIMIZATION on_off
    ;

db_encryption_option
    : ENCRYPTION (ON | OFF | SUSPEND | RESUME)
    ;
db_state_option
    : ( ONLINE | OFFLINE | EMERGENCY )
    ;

db_update_option
    : READ_ONLY | READ_WRITE
    ;

db_user_access_option
    : SINGLE_USER | RESTRICTED_USER | MULTI_USER
    ;
delayed_durability_option
    : DELAYED_DURABILITY EQUAL ( DISABLED | ALLOWED | FORCED )
    ;

external_access_option
    : DB_CHAINING on_off
    | TRUSTWORTHY on_off
    | DEFAULT_LANGUAGE EQUAL ( id_ | STRING )
    | DEFAULT_FULLTEXT_LANGUAGE EQUAL ( id_ | STRING )
    | NESTED_TRIGGERS EQUAL ( OFF | ON )
    | TRANSFORM_NOISE_WORDS EQUAL ( OFF | ON )
    | TWO_DIGIT_YEAR_CUTOFF EQUAL DECIMAL
    ;

hadr_options
    : HADR
      ( ( AVAILABILITY GROUP EQUAL availability_group_name=id_ | OFF ) |(SUSPEND|RESUME) )
    ;

mixed_page_allocation_option
    : MIXED_PAGE_ALLOCATION ( OFF | ON )
    ;

parameterization_option
    : PARAMETERIZATION ( SIMPLE | FORCED )
    ;

recovery_option
    : RECOVERY ( FULL | BULK_LOGGED | SIMPLE )
    | TORN_PAGE_DETECTION on_off
    | ACCELERATED_DATABASE_RECOVERY '=' on_off
    | PAGE_VERIFY ( CHECKSUM | TORN_PAGE_DETECTION | NONE )
    ;

service_broker_option:
    ENABLE_BROKER
    | DISABLE_BROKER
    | NEW_BROKER
    | ERROR_BROKER_CONVERSATIONS
    | HONOR_BROKER_PRIORITY on_off
    ;
snapshot_option
    : ALLOW_SNAPSHOT_ISOLATION on_off
    | READ_COMMITTED_SNAPSHOT (ON | OFF )
    | MEMORY_OPTIMIZED_ELEVATE_TO_SNAPSHOT '=' on_off
    ;

sql_option
    : ANSI_NULL_DEFAULT on_off
    | ANSI_NULLS on_off
    | ANSI_PADDING on_off
    | ANSI_WARNINGS on_off
    | ARITHABORT on_off
    | COMPATIBILITY_LEVEL EQUAL DECIMAL
    | CONCAT_NULL_YIELDS_NULL on_off
    | NUMERIC_ROUNDABORT on_off
    | QUOTED_IDENTIFIER on_off
    | RECURSIVE_TRIGGERS on_off
    ;

target_recovery_time_option
    : TARGET_RECOVERY_TIME EQUAL DECIMAL ( SECONDS | MINUTES )
    ;

termination
    : ROLLBACK AFTER seconds = DECIMAL SECONDS?
    | ROLLBACK IMMEDIATE
    | NO_WAIT
    ;

// https://msdn.microsoft.com/en-us/library/ms176118.aspx
drop_index
    : DROP INDEX (IF EXISTS)?
    ( drop_relational_or_xml_or_spatial_index (',' drop_relational_or_xml_or_spatial_index)*
    | drop_backward_compatible_index (',' drop_backward_compatible_index)*
    )
    ;

drop_relational_or_xml_or_spatial_index
    : index_name=id_ ON full_table_name
    ;

drop_backward_compatible_index
    : (owner_name=id_ '.')? table_or_view_name=id_ '.' index_name=id_
    ;

// https://msdn.microsoft.com/en-us/library/ms174969.aspx
drop_procedure
    : DROP proc=(PROC | PROCEDURE) (IF EXISTS)? func_proc_name_schema (',' func_proc_name_schema)*
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-trigger-transact-sql
drop_trigger
    : drop_dml_trigger
    | drop_ddl_trigger
    ;

drop_dml_trigger
    : DROP TRIGGER (IF EXISTS)? simple_name (',' simple_name)*
    ;

drop_ddl_trigger
    : DROP TRIGGER (IF EXISTS)? simple_name (',' simple_name)*
    ON (DATABASE | ALL SERVER)
    ;

// https://msdn.microsoft.com/en-us/library/ms190290.aspx
drop_function
    : DROP FUNCTION (IF EXISTS)? func_proc_name_schema (',' func_proc_name_schema)*
    ;

// https://msdn.microsoft.com/en-us/library/ms175075.aspx
drop_statistics
    : DROP STATISTICS (COMMA? (table_name '.')? name=id_)+
    ;

// https://msdn.microsoft.com/en-us/library/ms173790.aspx
drop_table
    : DROP TABLE (IF EXISTS)? table_name (',' table_name)*
    ;

// https://msdn.microsoft.com/en-us/library/ms173492.aspx
drop_view
    : DROP VIEW (IF EXISTS)? simple_name (',' simple_name)*
    ;

create_type
    : CREATE TYPE name = simple_name
      (FROM data_type null_notnull?)?
      (AS TABLE LR_BRACKET column_def_table_constraints RR_BRACKET)?
    ;

drop_type:
    DROP TYPE ( IF EXISTS )? name = simple_name
    ;

rowset_function_limited
    : openquery
    | opendatasource
    ;

// https://msdn.microsoft.com/en-us/library/ms188427(v=sql.120).aspx
openquery
    : OPENQUERY '(' linked_server=id_ ',' query=STRING ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms179856.aspx
opendatasource
    : OPENDATASOURCE '(' provider=STRING ',' init=STRING ')'
     '.' (database=id_)? '.' (scheme=id_)? '.' (table=id_)
    ;

// Other statements.

// https://msdn.microsoft.com/en-us/library/ms188927.aspx
declare_statement
    : DECLARE LOCAL_ID AS? (data_type | table_type_definition | table_name)
    | DECLARE loc+=declare_local (',' loc+=declare_local)*
    | DECLARE LOCAL_ID AS? xml_type_definition
    | WITH XMLNAMESPACES '(' xml_dec+=xml_declaration (',' xml_dec+=xml_declaration)* ')'
    ;

xml_declaration
    : xml_namespace_uri=STRING AS id_
    | DEFAULT STRING
    ;

// https://msdn.microsoft.com/en-us/library/ms181441(v=sql.120).aspx
cursor_statement
    // https://msdn.microsoft.com/en-us/library/ms175035(v=sql.120).aspx
    : CLOSE GLOBAL? cursor_name
    // https://msdn.microsoft.com/en-us/library/ms188782(v=sql.120).aspx
    | DEALLOCATE GLOBAL? CURSOR? cursor_name
    // https://msdn.microsoft.com/en-us/library/ms180169(v=sql.120).aspx
    | declare_cursor
    // https://msdn.microsoft.com/en-us/library/ms180152(v=sql.120).aspx
    | fetch_cursor
    // https://msdn.microsoft.com/en-us/library/ms190500(v=sql.120).aspx
    | OPEN GLOBAL? cursor_name
    ;
// https://docs.microsoft.com/en-us/sql/t-sql/statements/backup-transact-sql
backup_database
    : BACKUP DATABASE (database_name=id_ | database_variable=LOCAL_ID)
      (READ_WRITE_FILEGROUPS (',' backup_file_item)* | backup_file_item (',' backup_file_item)*)?
      TO backup_device (',' backup_device)* backup_mirror*
      (WITH backup_database_option (',' backup_database_option)*)?
    ;

backup_log
    : BACKUP LOG (database_name=id_ | database_variable=LOCAL_ID)
      TO backup_device (',' backup_device)* backup_mirror*
      (WITH backup_log_option (',' backup_log_option)*)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/backup-certificate-transact-sql
backup_certificate
    : BACKUP CERTIFICATE certname=id_ TO FILE '=' cert_file=STRING
      (WITH (FORMAT '=' STRING ',')? PRIVATE KEY '(' certificate_backup_option (',' certificate_backup_option)* ')')?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/backup-master-key-transact-sql
backup_master_key
    : BACKUP MASTER KEY TO (FILE | URL) '=' master_key_backup_file=STRING
      ENCRYPTION BY PASSWORD '=' encryption_password=STRING
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/backup-service-master-key-transact-sql
backup_service_master_key
    : BACKUP SERVICE MASTER KEY TO FILE EQUAL service_master_key_backup_file=STRING
         ENCRYPTION BY PASSWORD EQUAL encryption_password=STRING
    ;

kill_statement
    : KILL (kill_process | kill_query_notification | kill_stats_job)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/kill-transact-sql
kill_process
    : session_id=DECIMAL (WITH STATUSONLY)?
    | transaction_uow=STRING (WITH (STATUSONLY | COMMIT | ROLLBACK))?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/kill-query-notification-subscription-transact-sql
kill_query_notification
    : QUERY NOTIFICATION SUBSCRIPTION (ALL | subscription_id=DECIMAL)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/kill-stats-job-transact-sql
kill_stats_job
    : STATS JOB job_id=DECIMAL
    ;

// https://msdn.microsoft.com/en-us/library/ms188332.aspx
execute_statement
    : EXECUTE execute_body
    ;

execute_body_batch
    : func_proc_name_server_database_schema (execute_statement_arg (',' execute_statement_arg)*)?
    ;

//https://docs.microsoft.com/it-it/sql/t-sql/language-elements/execute-transact-sql?view=sql-server-ver15
execute_body
    : (return_status=LOCAL_ID '=')? (func_proc_name_server_database_schema | execute_var_string)  execute_statement_arg? (WITH RECOMPILE)?
    | '(' execute_var_string (',' execute_var_string)* ')' (AS (LOGIN | USER) '=' STRING)? (AT_KEYWORD linkedServer=id_)?
    | execute_as_context
    ;

// Standalone identity switches differ from module EXECUTE AS clauses.
execute_as_context
    : AS ((LOGIN | USER) '=' (STRING | LOCAL_ID) (WITH (NO REVERT | COOKIE INTO LOCAL_ID))?
          | CALLER)
    ;

execute_statement_arg
    :
    execute_statement_arg_unnamed (',' execute_statement_arg) *    //Unnamed params can continue unnamed
    |
    execute_statement_arg_named (',' execute_statement_arg_named)* //Named can only be continued by unnamed
    ;

execute_statement_arg_named
    : name=LOCAL_ID '=' value=execute_parameter
    ;

execute_statement_arg_unnamed
    : value=execute_parameter
    ;

execute_parameter
    : (constant | LOCAL_ID (OUTPUT | OUT)? | id_ | DEFAULT | NULL_)
    ;

execute_var_string
    : LOCAL_ID (OUTPUT | OUT)? ('+' LOCAL_ID ('+' execute_var_string)?)?
    | STRING ('+' LOCAL_ID ('+' execute_var_string)?)?
    ;

// https://msdn.microsoft.com/en-us/library/ff848791.aspx
security_statement
    : grant_statement
    | deny_statement
    | revoke_statement
    | revert_statement
    | open_key
    | close_key
    | create_key
    | create_certificate
    ;

revert_statement
    : REVERT (WITH COOKIE '=' LOCAL_ID)?
    ;

principal_id
    : id_
    | PUBLIC
    ;

create_certificate
    : CREATE CERTIFICATE certificate_name=id_ (AUTHORIZATION user_name=id_)?
      (FROM existing_keys | generate_new_keys)
      (ACTIVE FOR BEGIN_DIALOG '=' on_off)?
    ;

existing_keys
    : ASSEMBLY assembly_name=id_
    | EXECUTABLE? FILE '=' path_to_file=STRING
      (WITH (FORMAT '=' STRING ',')? PRIVATE KEY '(' private_key_options ')')?
    | BINARY_KEYWORD '=' BINARY (WITH PRIVATE KEY '(' private_key_options ')')?
    ;

private_key_options
    : private_key_option (',' private_key_option)*
    ;

generate_new_keys
    : (ENCRYPTION BY PASSWORD '=' password=STRING)?
      WITH SUBJECT EQUAL certificate_subject_name=STRING (',' date_options)*
    ;

date_options
    : (START_DATE | EXPIRY_DATE) EQUAL STRING
    ;

open_key
    : OPEN SYMMETRIC KEY key_name=id_ DECRYPTION BY decryption_mechanism
    | OPEN MASTER KEY DECRYPTION BY PASSWORD '=' password=STRING
    ;

close_key
    : CLOSE SYMMETRIC KEY key_name=id_
    | CLOSE ALL SYMMETRIC KEYS
    | CLOSE MASTER KEY
    ;

create_key
    : CREATE SYMMETRIC KEY key_name=id_ (AUTHORIZATION user_name=id_)?
      (WITH key_options (',' key_options)* ENCRYPTION BY encryption_mechanism (',' encryption_mechanism)*
      | FROM PROVIDER provider_name=id_ WITH key_options (',' key_options)*)
    ;

key_options
    : KEY_SOURCE EQUAL pass_phrase=STRING
    | ALGORITHM EQUAL algorithm
    | IDENTITY_VALUE EQUAL identity_phrase=STRING
    | PROVIDER_KEY_NAME EQUAL key_name_in_provider=STRING
    | CREATION_DISPOSITION EQUAL (CREATE_NEW | OPEN_EXISTING)
    ;

algorithm
    : DES
    | TRIPLE_DES
    | TRIPLE_DES_3KEY
    | RC2
    | RC4
    | RC4_128
    | DESX
    | AES_128
    | AES_192
    | AES_256
    ;

encryption_mechanism
    : CERTIFICATE certificate_name=id_
    | ASYMMETRIC KEY asym_key_name=id_
    | SYMMETRIC KEY decrypting_Key_name=id_
    | PASSWORD '=' STRING
    ;

decryption_mechanism
    : CERTIFICATE certificate_name=id_ (WITH PASSWORD EQUAL STRING)?
    | ASYMMETRIC KEY asym_key_name=id_ (WITH PASSWORD EQUAL STRING)?
    | SYMMETRIC KEY decrypting_Key_name=id_
    | PASSWORD EQUAL STRING
    ;

// https://docs.microsoft.com/en-us/sql/relational-databases/system-functions/sys-fn-builtin-permissions-transact-sql?view=sql-server-ver15
// SELECT DISTINCT '| ' + permission_name
// FROM sys.fn_builtin_permissions (DEFAULT)
// ORDER BY 1
grant_permission
    : ADMINISTER ( BULK OPERATIONS | DATABASE BULK OPERATIONS)
    | ALTER ( ANY ( APPLICATION ROLE
                  | ASSEMBLY
                  | ASYMMETRIC KEY
                  | AVAILABILITY GROUP
                  | CERTIFICATE
                  | COLUMN ( ENCRYPTION KEY | MASTER KEY )
                  | CONNECTION
                  | CONTRACT
                  | CREDENTIAL
                  | DATABASE ( AUDIT
                             | DDL TRIGGER
                             | EVENT ( NOTIFICATION | SESSION )
                             | SCOPED CONFIGURATION
                             )?
                  | DATASPACE
                  | ENDPOINT
                  | EVENT ( NOTIFICATION | SESSION )
                  | EXTERNAL ( DATA SOURCE | FILE FORMAT | LIBRARY)
                  | FULLTEXT CATALOG
                  | LINKED SERVER
                  | LOGIN
                  | MASK
                  | MESSAGE TYPE
                  | REMOTE SERVICE BINDING
                  | ROLE
                  | ROUTE
                  | SCHEMA
                  | SECURITY POLICY
                  | SERVER ( AUDIT | ROLE )
                  | SERVICE
                  | SYMMETRIC KEY
                  | USER
                  )
            | RESOURCES
            | SERVER STATE
            | SETTINGS
            | TRACE
            )?
    | AUTHENTICATE SERVER?
    | BACKUP ( DATABASE | LOG )
    | CHECKPOINT
    | CONNECT ( ANY DATABASE | REPLICATION | SQL )?
    | CONTROL SERVER?
    | CREATE ( AGGREGATE
             | ANY DATABASE
             | ASSEMBLY
             | ASYMMETRIC KEY
             | AVAILABILITY GROUP
             | CERTIFICATE
             | CONTRACT
             | DATABASE (DDL EVENT NOTIFICATION)?
             | DDL EVENT NOTIFICATION
             | DEFAULT
             | ENDPOINT
             | EXTERNAL LIBRARY
             | FULLTEXT CATALOG
             | FUNCTION
             | MESSAGE TYPE
             | PROCEDURE
             | QUEUE
             | REMOTE SERVICE BINDING
             | ROLE
             | ROUTE
             | RULE
             | SCHEMA
             | SEQUENCE
             | SERVER ROLE
             | SERVICE
             | SYMMETRIC KEY
             | SYNONYM
             | TABLE
             | TRACE EVENT NOTIFICATION
             | TYPE
             | VIEW
             | XML SCHEMA COLLECTION
             )
    | DELETE
    | EXECUTE ( ANY EXTERNAL SCRIPT )?
    | EXTERNAL ACCESS ASSEMBLY
    | IMPERSONATE ( ANY LOGIN )?
    | INSERT
    | KILL DATABASE CONNECTION
    | RECEIVE
    | REFERENCES
    | SELECT ( ALL USER SECURABLES )?
    | SEND
    | SHOWPLAN
    | SHUTDOWN
    | SUBSCRIBE QUERY NOTIFICATIONS
    | TAKE OWNERSHIP
    | UNMASK
    | UNSAFE ASSEMBLY
    | UPDATE
    | VIEW ( ANY ( DATABASE | security_definition | COLUMN ( ENCRYPTION | MASTER ) KEY DEFINITION )
           | CHANGE TRACKING
           | DATABASE (SECURITY | PERFORMANCE)? STATE
           | SERVER (SECURITY | PERFORMANCE)? STATE
           | security_definition
           )
    ;

// https://msdn.microsoft.com/en-us/library/ms190356.aspx
// https://msdn.microsoft.com/en-us/library/ms189484.aspx
set_statement
    : SET LOCAL_ID ('.' member_name=id_)? '=' expression
    | SET LOCAL_ID assignment_operator expression
    | SET LOCAL_ID '='
      CURSOR declare_set_cursor_common (FOR (READ ONLY | UPDATE (OF column_name_list)?))?
    // https://msdn.microsoft.com/en-us/library/ms189837.aspx
    | set_special
    ;

// https://msdn.microsoft.com/en-us/library/ms174377.aspx
transaction_statement
    : BEGIN DISTRIBUTED (TRAN | TRANSACTION) (id_ | LOCAL_ID)?
    | BEGIN (TRAN | TRANSACTION) ((id_ | LOCAL_ID) (WITH MARK STRING?)?)?
    | COMMIT (WORK | ((TRAN | TRANSACTION) (id_ | LOCAL_ID)?)?
                     (WITH '(' DELAYED_DURABILITY EQUAL (OFF | ON) ')')?)
    | ROLLBACK ((TRAN | TRANSACTION) (id_ | LOCAL_ID)? | WORK)?
    | SAVE (TRAN | TRANSACTION) (id_ | LOCAL_ID)
    ;

// https://msdn.microsoft.com/en-us/library/ms188037.aspx
go_statement
    : GO  //(count=DECIMAL)?
    ;

// https://msdn.microsoft.com/en-us/library/ms188366.aspx
use_statement
    : USE database=id_
    ;

setuser_statement
    : SETUSER (user=STRING (WITH NORESET)?)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/reconfigure-transact-sql
reconfigure_statement
    : RECONFIGURE (WITH OVERRIDE)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/shutdown-transact-sql
shutdown_statement
    : SHUTDOWN (WITH NOWAIT)?
    ;

checkpoint_statement
    : CHECKPOINT (checkPointDuration=DECIMAL)?
    ;

dbcc_checkalloc_option
    : ALL_ERRORMSGS
    | NO_INFOMSGS
    | TABLOCK
    | ESTIMATEONLY
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checkalloc-transact-sql?view=sql-server-ver16
dbcc_checkalloc
    : name=CHECKALLOC
        (
            '('
                ( database=id_ | databaseid=STRING | DECIMAL )
                ( ',' NOINDEX | ',' ( REPAIR_ALLOW_DATA_LOSS | REPAIR_FAST | REPAIR_REBUILD ) )?
            ')'
            (
                WITH dbcc_option=dbcc_checkalloc_option ( ',' dbcc_option=dbcc_checkalloc_option )*
            )?
        )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checkcatalog-transact-sql?view=sql-server-ver16
dbcc_checkcatalog
    : name=CHECKCATALOG
        (
            '('
                ( database=id_ | databasename=STRING | DECIMAL )
            ')'
        )?
        (
            WITH dbcc_option=NO_INFOMSGS
        )?
    ;

dbcc_checkconstraints_option
    : ALL_CONSTRAINTS
    | ALL_ERRORMSGS
    | NO_INFOMSGS
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checkconstraints-transact-sql?view=sql-server-ver16
dbcc_checkconstraints
    : name=CHECKCONSTRAINTS
        (
            '('
                ( table_or_constraint=id_ | table_or_constraint_name=STRING )
            ')'
        )?
        (
            WITH dbcc_option=dbcc_checkconstraints_option (',' dbcc_option=dbcc_checkconstraints_option)*
        )?
    ;

dbcc_checkdb_table_option
    : ALL_ERRORMSGS
    | EXTENDED_LOGICAL_CHECKS
    | NO_INFOMSGS
    | TABLOCK
    | ESTIMATEONLY
    | PHYSICAL_ONLY
    | DATA_PURITY
    | MAXDOP '=' max_dregree_of_parallelism=DECIMAL
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checkdb-transact-sql?view=sql-server-ver16
dbcc_checkdb
    : name=CHECKDB
        (
            '('
                ( database=id_ | databasename=STRING | DECIMAL )
                (
                    ',' (
                            NOINDEX
                            | REPAIR_ALLOW_DATA_LOSS
                            | REPAIR_FAST
                            | REPAIR_REBUILD
                    )
                )?
            ')'
        )?
        (
            WITH dbcc_option=dbcc_checkdb_table_option (',' dbcc_option=dbcc_checkdb_table_option)*
        )?
    ;

dbcc_checkfilegroup_option
    : ALL_ERRORMSGS
    | NO_INFOMSGS
    | TABLOCK
    | ESTIMATEONLY
    | PHYSICAL_ONLY
    | MAXDOP '=' max_dregree_of_parallelism=DECIMAL
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checkfilegroup-transact-sql?view=sql-server-ver16
// Additional parameters: https://dbtut.com/index.php/2019/01/01/dbcc-checkfilegroup-command-on-sql-server/
dbcc_checkfilegroup
    : name=CHECKFILEGROUP
        (
            '('
                ( filegroup_id=DECIMAL | filegroup_name=STRING )
                (
                    ',' (
                            NOINDEX
                            | REPAIR_ALLOW_DATA_LOSS
                            | REPAIR_FAST
                            | REPAIR_REBUILD
                    )
                )?
            ')'
        )?
        (
            WITH dbcc_option=dbcc_checkfilegroup_option (',' dbcc_option=dbcc_checkfilegroup_option)*
        )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-checktable-transact-sql?view=sql-server-ver16
dbcc_checktable
    : name=CHECKTABLE
        '('
            table_or_view_name=STRING
            ( ',' (
                    NOINDEX
                    | index_id=expression
                    | REPAIR_ALLOW_DATA_LOSS
                    | REPAIR_FAST
                    | REPAIR_REBUILD
                )
            )?
        ')'
        (
            WITH dbcc_option=dbcc_checkdb_table_option (',' dbcc_option=dbcc_checkdb_table_option)*
        )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-cleantable-transact-sql?view=sql-server-ver16
dbcc_cleantable
    : name=CLEANTABLE
        '('
            ( database=id_ | databasename=STRING | DECIMAL )
            ',' ( table_or_view=id_ | table_or_view_name=STRING )
            ( ',' batch_size=DECIMAL )?
        ')'
        ( WITH dbcc_option=NO_INFOMSGS )?
    ;

dbcc_clonedatabase_option
    : NO_STATISTICS
    | NO_QUERYSTORE
    | SERVICEBROKER
    | VERIFY_CLONEDB
    | BACKUP_CLONEDB
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-clonedatabase-transact-sql?view=sql-server-ver16
dbcc_clonedatabase
    : name=CLONEDATABASE
        '('
            source_database=id_
            ',' target_database=id_
        ')'
        ( WITH dbcc_option=dbcc_clonedatabase_option (',' dbcc_option=dbcc_clonedatabase_option)* )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-pdw-showspaceused-transact-sql?view=aps-pdw-2016-au7
dbcc_pdw_showspaceused
    : name=PDW_SHOWSPACEUSED ( '(' tablename=id_ ')' ) ? ( WITH dbcc_option=IGNORE_REPLICATED_TABLE_CACHE )? ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-proccache-transact-sql?view=sql-server-ver16
dbcc_proccache
    : name=PROCCACHE ( WITH dbcc_option=NO_INFOMSGS )? ;

dbcc_showcontig_option
    : ALL_INDEXES
    | TABLERESULTS
    | FAST
    | ALL_LEVELS
    | NO_INFOMSGS
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-showcontig-transact-sql?view=sql-server-ver16
dbcc_showcontig
    : name=SHOWCONTIG
        (
            '('
                table_or_view=expression
                (
                    ',' index=expression
                )?
            ')'
        )?
        ( WITH dbcc_option=dbcc_showcontig_option (',' dbcc_showcontig_option)* )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-shrinklog-azure-sql-data-warehouse?view=aps-pdw-2016-au7
dbcc_shrinklog
    : name=SHRINKLOG
        ('(' SIZE '=' ( (DECIMAL ( MB | GB | TB ) ) | DEFAULT ) ')')?
        ( WITH dbcc_option=NO_INFOMSGS )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-dbreindex-transact-sql?view=sql-server-ver16
dbcc_dbreindex
    : name=DBREINDEX
        '('
            table=id_or_string
            ( ',' index_name=id_or_string ( ',' fillfactor=expression)? )?
        ')'
        (
            WITH dbcc_option=NO_INFOMSGS
        )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-dllname-free-transact-sql?view=sql-server-ver16
dbcc_dll_free
    : dllname=id_
        '(' name=FREE ')'
        (
            WITH dbcc_option=NO_INFOMSGS
        )?
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/database-console-commands/dbcc-dropcleanbuffers-transact-sql?view=sql-server-ver16
dbcc_dropcleanbuffers
    : name=DROPCLEANBUFFERS
        (
            '('
                COMPUTE | ALL
            ')'
        )?
        (
            WITH dbcc_option=NO_INFOMSGS
        )?
    ;

// SQL Server diagnostic and cache-management commands.
dbcc_freeproccache
    : FREEPROCCACHE ('(' (BINARY | LOCAL_ID | id_or_string) ')')? (WITH NO_INFOMSGS)?
    ;

dbcc_freesystemcache
    : FREESYSTEMCACHE '(' STRING (',' id_or_string)? ')'
      (WITH (MARK_IN_USE_FOR_REMOVAL | NO_INFOMSGS) (',' (MARK_IN_USE_FOR_REMOVAL | NO_INFOMSGS))*)?
    ;

dbcc_freesessioncache
    : FREESESSIONCACHE (WITH NO_INFOMSGS)?
    ;

dbcc_inputbuffer
    : INPUTBUFFER '(' (DECIMAL | LOCAL_ID) (',' (DECIMAL | LOCAL_ID))? ')' (WITH NO_INFOMSGS)?
    ;

dbcc_outputbuffer
    : OUTPUTBUFFER '(' (DECIMAL | LOCAL_ID) ')' (WITH NO_INFOMSGS)?
    ;

dbcc_opentran
    : OPENTRAN ('(' (DECIMAL | id_or_string) ')')?
      (WITH (TABLERESULTS | NO_INFOMSGS) (',' (TABLERESULTS | NO_INFOMSGS))*)?
    ;

dbcc_sqlperf
    : SQLPERF '(' (LOGSPACE | id_or_string ',' CLEAR) ')' (WITH NO_INFOMSGS)?
    ;

dbcc_memorystatus
    : MEMORYSTATUS
    ;

dbcc_useroptions
    : USEROPTIONS (WITH NO_INFOMSGS)?
    ;

dbcc_show_statistics
    : SHOW_STATISTICS '(' id_or_string ',' id_or_string ')'
      (WITH (STAT_HEADER | DENSITY_VECTOR | HISTOGRAM) (',' (STAT_HEADER | DENSITY_VECTOR | HISTOGRAM))*)?
    ;

dbcc_tracestatus
    : TRACESTATUS ('(' (dbcc_trace_flag (',' dbcc_trace_flag)*)? ')')? (WITH NO_INFOMSGS)?
    ;

dbcc_trace_control
    : (TRACEON | TRACEOFF) '(' dbcc_trace_flag (',' dbcc_trace_flag)* ')' (WITH NO_INFOMSGS)?
    ;

dbcc_trace_flag
    : '-'? DECIMAL
    ;

dbcc_clause
    : DBCC (
        dbcc_freeproccache
        | dbcc_freesystemcache
        | dbcc_freesessioncache
        | dbcc_inputbuffer
        | dbcc_outputbuffer
        | dbcc_opentran
        | dbcc_sqlperf
        | dbcc_memorystatus
        | dbcc_useroptions
        | dbcc_show_statistics
        | dbcc_tracestatus
        | dbcc_trace_control
        | dbcc_checkalloc
        | dbcc_checkcatalog
        | dbcc_checkconstraints
        | dbcc_checkdb
        | dbcc_checkfilegroup
        | dbcc_checktable
        | dbcc_checkident
        | dbcc_indexdefrag
        | dbcc_shrinkdatabase
        | dbcc_shrinkfile
        | dbcc_updateusage
        | dbcc_cleantable
        | dbcc_clonedatabase
        | dbcc_dbreindex
        | dbcc_dll_free
        | dbcc_dropcleanbuffers
        | dbcc_pdw_showspaceused
        | dbcc_proccache
        | dbcc_showcontig
        | dbcc_shrinklog
    )
    ;

execute_clause
    : EXECUTE AS clause=(CALLER | SELF | OWNER | STRING)
    ;

declare_local
    : LOCAL_ID AS? data_type ('=' expression)?
    ;

table_type_definition
    : TABLE '(' column_def_table_constraints (','? table_type_indices)*  ')'
    ;

table_type_indices
    :  (((PRIMARY KEY | INDEX id_) (CLUSTERED | NONCLUSTERED)?) | UNIQUE) '(' column_name_list_with_order ')'
    | CHECK '(' search_condition ')'
    ;

xml_type_definition
    : XML '(' ( CONTENT | DOCUMENT )? xml_schema_collection ')'
    ;

xml_schema_collection
    : ID '.' ID
    ;

column_def_table_constraints
    : column_def_table_constraint (','? column_def_table_constraint)*
    ;

column_def_table_constraint
    : column_definition
    | materialized_column_definition
    | table_constraint
    ;

// https://msdn.microsoft.com/en-us/library/ms187742.aspx
// There is a documentation error: column definition elements can be given in
// any order
column_definition
    : id_ (data_type | AS expression PERSISTED? )
      column_definition_element*
      column_index?
    ;

column_definition_element
    : FILESTREAM
    | COLLATE collation_name=id_
    | SPARSE
    | MASKED WITH '(' FUNCTION '=' mask_function=STRING ')'
    | (CONSTRAINT constraint=id_)? DEFAULT  constant_expr=expression
    | IDENTITY ('(' seed=DECIMAL ',' increment=DECIMAL ')')?
    | NOT FOR REPLICATION
    | GENERATED ALWAYS AS ( ROW | TRANSACTION_ID | SEQUENCE_NUMBER ) ( START | END ) HIDDEN_KEYWORD?
    // NULL / NOT NULL is a constraint
    | ROWGUIDCOL
    | ENCRYPTED WITH
        '(' COLUMN_ENCRYPTION_KEY '=' key_name=STRING ','
            ENCRYPTION_TYPE '=' ( DETERMINISTIC | RANDOMIZED ) ','
            ALGORITHM '=' algo=STRING
        ')'
    | column_constraint
    ;

column_modifier
    : id_ (ADD | DROP) (
      ROWGUIDCOL
      | PERSISTED
      | NOT FOR REPLICATION
      | SPARSE
      | HIDDEN_KEYWORD
      | MASKED (WITH (FUNCTION EQUAL STRING | LR_BRACKET FUNCTION EQUAL STRING RR_BRACKET))?)
    ;

materialized_column_definition
    : id_ (COMPUTE | AS) expression (MATERIALIZED | NOT MATERIALIZED)?
    ;

// https://msdn.microsoft.com/en-us/library/ms186712.aspx
// There is a documentation error: NOT NULL is a constraint
// and therefore can be given a name.
column_constraint
    : (CONSTRAINT constraint=id_)?
      (
        null_notnull
      | (
            (PRIMARY KEY | UNIQUE)
            clustered?
            primary_key_options
        )
      | (
            (FOREIGN KEY)?
            foreign_key_options
        )
      | check_constraint
      )
    ;

column_index
    :
        INDEX index_name=id_ clustered?
        create_table_index_options?
        on_partition_or_filegroup?
        ( FILESTREAM_ON ( filestream_filegroup_or_partition_schema_name=id_ | NULL_DOUBLE_QUOTE ) )?
    ;

on_partition_or_filegroup
    :
        ON (
            (partition_scheme_name=id_ '(' partition_column_name=id_ ')')
            | filegroup=id_
            | DEFAULT_DOUBLE_QUOTE
        )
    ;

// https://msdn.microsoft.com/en-us/library/ms188066.aspx
table_constraint
    : (CONSTRAINT constraint=id_)?
        (
            (
                (PRIMARY KEY | UNIQUE)
                clustered?
                '(' column_name_list_with_order ')'
                primary_key_options
            )
            |
            (
                FOREIGN KEY
                '(' fk = column_name_list ')'
                foreign_key_options
            )
            |
            (
                CONNECTION
                '(' connection_node ( ',' connection_node )* ')'
            )
            |
            (
                DEFAULT constant_expr=expression FOR column=id_ (WITH VALUES)?
            )
            | check_constraint
        )
    ;

connection_node
    :
        from_node_table=id_ TO to_node_table=id_
    ;

primary_key_options
    :
        (WITH FILLFACTOR '=' DECIMAL)?
        alter_table_index_options?
        on_partition_or_filegroup?
    ;

foreign_key_options
    :
        REFERENCES table_name '(' pk = column_name_list')'
        (on_delete | on_update)*
        (NOT FOR REPLICATION)?
    ;

check_constraint
    :
    CHECK (NOT FOR REPLICATION)? '(' search_condition ')'
    ;

on_delete
    : ON DELETE (NO ACTION | CASCADE | SET NULL_ | SET DEFAULT)
    ;

on_update
    : ON UPDATE (NO ACTION | CASCADE | SET NULL_ | SET DEFAULT)
    ;

alter_table_index_options
    : WITH '(' alter_table_index_option (',' alter_table_index_option)* ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms186869.aspx
alter_table_index_option
    : PAD_INDEX '=' on_off
    | FILLFACTOR '=' DECIMAL
    | IGNORE_DUP_KEY '=' on_off
    | STATISTICS_NORECOMPUTE '=' on_off
    | ALLOW_ROW_LOCKS '=' on_off
    | ALLOW_PAGE_LOCKS '=' on_off
    | OPTIMIZE_FOR_SEQUENTIAL_KEY '=' on_off
    | SORT_IN_TEMPDB '=' on_off
    | MAXDOP '=' max_degree_of_parallelism=DECIMAL
    | DATA_COMPRESSION '=' (NONE | ROW | PAGE | COLUMNSTORE | COLUMNSTORE_ARCHIVE)
        on_partitions?
    | XML_COMPRESSION '=' on_off
        on_partitions?
    | DISTRIBUTION '=' HASH '(' id_ ')' | CLUSTERED INDEX '(' id_ (ASC | DESC)? (',' id_ (ASC | DESC)?)* ')'
    | ONLINE '=' (ON ('(' low_priority_lock_wait ')')? | OFF)
    | RESUMABLE '=' on_off
    | MAX_DURATION '=' times=DECIMAL MINUTES?
    ;

// https://msdn.microsoft.com/en-us/library/ms180169.aspx
declare_cursor
    : DECLARE cursor_name
      (CURSOR (declare_set_cursor_common (FOR UPDATE (OF column_name_list)?)?)?
      | (SEMI_SENSITIVE | INSENSITIVE)? SCROLL? CURSOR FOR select_statement_standalone (FOR (READ ONLY | UPDATE | (OF column_name_list)))?
      )
    ;

declare_set_cursor_common
    : declare_set_cursor_common_partial*
      FOR select_statement_standalone
    ;

declare_set_cursor_common_partial
    : (LOCAL | GLOBAL)
    | (FORWARD_ONLY | SCROLL)
    | (STATIC | KEYSET | DYNAMIC | FAST_FORWARD)
    | (READ_ONLY | SCROLL_LOCKS | OPTIMISTIC)
    | TYPE_WARNING
    ;

fetch_cursor
    : FETCH ((NEXT | PRIOR | FIRST | LAST | (ABSOLUTE | RELATIVE) expression)? FROM)?
      GLOBAL? cursor_name (INTO LOCAL_ID (',' LOCAL_ID)*)?
    ;

// https://msdn.microsoft.com/en-us/library/ms190356.aspx
// Runtime check.
set_special
    : SET id_ (id_ | constant_LOCAL_ID | on_off)
    | SET STATISTICS (IO | TIME | XML | PROFILE) (',' (IO | TIME | XML | PROFILE))* on_off
    | SET ROWCOUNT (LOCAL_ID | DECIMAL)
    | SET TEXTSIZE '-'? DECIMAL
    | SET OFFSETS offsets_option (',' offsets_option)* on_off
    // https://msdn.microsoft.com/en-us/library/ms173763.aspx
    | SET TRANSACTION ISOLATION LEVEL
      (READ UNCOMMITTED | READ COMMITTED | REPEATABLE READ | SNAPSHOT | SERIALIZABLE | DECIMAL)
    // https://msdn.microsoft.com/en-us/library/ms188059.aspx
    | SET IDENTITY_INSERT table_name on_off
    | SET special_list (',' special_list)* on_off
    | SET modify_method
    ;

offsets_option
    : SELECT | FROM | ORDER | TABLE | PROCEDURE | STATEMENT | PARAM | EXECUTE
    ;

special_list
    : ANSI_NULLS
    | QUOTED_IDENTIFIER
    | ANSI_PADDING
    | ANSI_WARNINGS
    | ANSI_DEFAULTS
    | ANSI_NULL_DFLT_OFF
    | ANSI_NULL_DFLT_ON
    | ARITHABORT
    | ARITHIGNORE
    | CONCAT_NULL_YIELDS_NULL
    | CURSOR_CLOSE_ON_COMMIT
    | FMTONLY
    | FORCEPLAN
    | IMPLICIT_TRANSACTIONS
    | NOCOUNT
    | NOEXEC
    | NUMERIC_ROUNDABORT
    | PARSEONLY
    | REMOTE_PROC_TRANSACTIONS
    | SHOWPLAN_ALL
    | SHOWPLAN_TEXT
    | SHOWPLAN_XML
    | XACT_ABORT
    ;

constant_LOCAL_ID
    : constant
    | LOCAL_ID
    ;

// Expression.

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/expressions-transact-sql
// Operator precendence: https://docs.microsoft.com/en-us/sql/t-sql/language-elements/operator-precedence-transact-sql
expression
    : primitive_expression
    | function_call
    | expression '.' (value_call | query_call | exist_call | modify_call)
    | expression '.' hierarchyid_call
    | expression COLLATE id_
    | case_expression
    | full_column_name
    | bracket_expression
    | unary_operator_expression
    | expression op=('*' | '/' | '%') expression
    | expression op=('+' | '-' | '&' | '^' | '|' | '||') expression
    | expression time_zone
    | over_clause
    | DOLLAR_ACTION
    ;

parameter
    : PLACEHOLDER;

time_zone
    : AT_KEYWORD TIME ZONE expression
    ;

primitive_expression
    : DEFAULT | NULL_ | LOCAL_ID | primitive_constant
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/case-transact-sql
case_expression
    : CASE caseExpr=expression switch_section+ (ELSE elseExpr=expression)? END
    | CASE switch_search_condition_section+ (ELSE elseExpr=expression)? END
    ;

unary_operator_expression
    : '~' expression
    | op=('+' | '-') expression
    ;

bracket_expression
    : '(' expression ')' | '(' subquery ')'
    ;

subquery
    : select_statement
    ;

// https://msdn.microsoft.com/en-us/library/ms175972.aspx
with_expression
    : WITH ctes+=common_table_expression (',' ctes+=common_table_expression)*
    ;

common_table_expression
    : expression_name=id_ ('(' columns=column_name_list ')')? AS '(' cte_query=select_statement ')'
    ;

update_elem
    : LOCAL_ID '=' full_column_name ('=' | assignment_operator) expression //Combined variable and column update
    | (full_column_name | LOCAL_ID) ('=' | assignment_operator) expression
    | udt_column_name=id_ '.' method_name=id_ '(' expression_list_ ')'
    //| full_column_name '.' WRITE (expression, )
    ;

update_elem_merge
    : (full_column_name | LOCAL_ID) ('=' | assignment_operator) expression
    | udt_column_name=id_ '.' method_name=id_ '(' expression_list_ ')'
    //| full_column_name '.' WRITE (expression, )
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/search-condition-transact-sql
search_condition
    : NOT* (predicate | '(' search_condition ')')
    | search_condition AND search_condition // AND takes precedence over OR
    | search_condition OR search_condition
    ;

predicate
    : EXISTS '(' subquery ')'
    | freetext_predicate
    | expression comparison_operator expression
    | expression MULT_ASSIGN expression ////SQL-82 syntax for left outer joins; '*='. See https://stackoverflow.com/questions/40665/in-sybase-sql
    | expression comparison_operator (ALL | SOME | ANY) '(' subquery ')'
    | expression NOT* BETWEEN expression AND expression
    | expression NOT* IN '(' (subquery | expression_list_) ')'
    | expression NOT* LIKE expression (ESCAPE expression)?
    | expression IS null_notnull
    ;

// Changed union rule to sql_union to avoid union construct with C++ target.  Issue reported by person who generates into C++.  This individual reports change causes generated code to work

query_expression
    : query_specification select_order_by_clause? unions+=sql_union* //if using top, order by can be on the "top" side of union :/
    | '(' query_expression ')' (UNION ALL? query_expression)?
    ;

sql_union
    : (UNION ALL? | EXCEPT | INTERSECT) (spec=query_specification | ('(' op=query_expression ')'))
    ;

// https://msdn.microsoft.com/en-us/library/ms176104.aspx
query_specification
    : SELECT allOrDistinct=(ALL | DISTINCT)? top=top_clause?
      columns=select_list
      // https://msdn.microsoft.com/en-us/library/ms188029.aspx
      (INTO into=table_name)?
      (FROM from=table_sources)?
      (WHERE where=search_condition)?
      // https://msdn.microsoft.com/en-us/library/ms177673.aspx
      (GROUP BY ((groupByAll=ALL? groupBys+=group_by_item (',' groupBys+=group_by_item)*) | GROUPING SETS '(' groupSets+=grouping_sets_item (',' groupSets+=grouping_sets_item)* ')'))?
      (HAVING having=search_condition)?
    ;

// https://msdn.microsoft.com/en-us/library/ms189463.aspx
top_clause
    : TOP (top_percent | top_count) (WITH TIES)?
    ;

top_percent
    : percent_constant=(REAL | FLOAT | DECIMAL) PERCENT
    | '(' topper_expression=expression ')' PERCENT
    ;

top_count
    : count_constant=DECIMAL
    | '(' topcount_expression=expression ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/select-over-clause-transact-sql?view=sql-server-ver16
order_by_clause
    : ORDER BY order_bys+=order_by_expression (',' order_bys+=order_by_expression)*
    ;

// https://msdn.microsoft.com/en-us/library/ms188385.aspx
select_order_by_clause
    : order_by_clause
      (OFFSET offset_exp=expression offset_rows=(ROW | ROWS) (FETCH fetch_offset=(FIRST | NEXT) fetch_exp=expression fetch_rows=(ROW | ROWS) ONLY)?)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/select-for-clause-transact-sql
for_clause
    : FOR BROWSE
    | FOR XML (RAW ('(' STRING ')')? | AUTO) xml_common_directives*
      (COMMA (XMLDATA | XMLSCHEMA ('(' STRING ')')?))?
      (COMMA ELEMENTS (XSINIL | ABSENT)?)?
    | FOR XML EXPLICIT xml_common_directives*
      (COMMA XMLDATA)?
    | FOR XML PATH ('(' STRING ')')? xml_common_directives*
      (COMMA ELEMENTS (XSINIL | ABSENT)?)?
    | FOR JSON (AUTO | PATH)
      ( COMMA
        ( ROOT ('(' STRING ')')
        | INCLUDE_NULL_VALUES
        | WITHOUT_ARRAY_WRAPPER
        )
      )*
    ;

xml_common_directives
    : ',' (BINARY_KEYWORD BASE64 | TYPE | ROOT ('(' STRING ')')?)
    ;

order_by_expression
    : order_by=expression (ascending=ASC | descending=DESC)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/select-group-by-transact-sql?view=sql-server-ver15
grouping_sets_item
    : '('? groupSetItems+=group_by_item (',' groupSetItems+=group_by_item)* ')'?
    | '(' ')'
    ;

group_by_item
    : expression
    /*| rollup_spec
    | cube_spec
    | grouping_sets_spec
    | grand_total*/
    ;

option_clause
    // https://msdn.microsoft.com/en-us/library/ms181714.aspx
    : OPTION '(' options_ += option (',' options_ += option)* ')'
    ;

option
    : FAST number_rows=DECIMAL
    | (HASH | ORDER) GROUP
    | (MERGE | HASH | CONCAT) UNION
    | (LOOP | MERGE | HASH) JOIN
    | EXPAND VIEWS
    | FORCE ORDER
    | IGNORE_NONCLUSTERED_COLUMNSTORE_INDEX
    | KEEP PLAN
    | KEEPFIXED PLAN
    | MAXDOP number_of_processors=DECIMAL
    | MAXRECURSION number_recursion=DECIMAL
    | OPTIMIZE FOR '(' optimize_for_arg (',' optimize_for_arg)* ')'
    | OPTIMIZE FOR UNKNOWN
    | PARAMETERIZATION (SIMPLE | FORCED)
    | RECOMPILE
    | ROBUST PLAN
    | USE PLAN STRING
    | USE HINT '(' STRING (',' STRING)* ')'
    ;

optimize_for_arg
    : LOCAL_ID (UNKNOWN | '=' (constant | NULL_))
    ;

// https://msdn.microsoft.com/en-us/library/ms176104.aspx
select_list
    : selectElement+=select_list_elem (',' selectElement+=select_list_elem)*
    ;

udt_method_arguments
    : '(' argument+=execute_var_string (',' argument+=execute_var_string)* ')'
    ;

// https://docs.microsoft.com/ru-ru/sql/t-sql/queries/select-clause-transact-sql
asterisk
    : (table_name '.')? '*'
    | (INSERTED | DELETED) '.' '*'
    ;

udt_elem
    : udt_column_name=id_ '.' non_static_attr=id_ udt_method_arguments as_column_alias?
    | udt_column_name=id_ DOUBLE_COLON static_attr=id_ udt_method_arguments? as_column_alias?
    ;

expression_elem
    : leftAlias=column_alias eq='=' leftAssignment=expression
    | expressionAs=expression as_column_alias?
    ;

select_list_elem
    : asterisk
    | LOCAL_ID (assignment_operator | '=') expression
    // Prefer scalar calls for schema.function(args); the UDT syntax also accepts this spelling.
    | expression_elem
    | udt_elem
    ;

table_sources
    : non_ansi_join
    | source+=table_source (',' source+=table_source)*
    ;

// https://sqlenlight.com/support/help/sa0006/
non_ansi_join
    : source+=table_source (',' source+=table_source)+
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/from-transact-sql
table_source
    : table_source_item joins+=join_part*
    ;

table_source_item
    : full_table_name             deprecated_table_hint as_table_alias // this is currently allowed
    | full_table_name             as_table_alias? (with_table_hints | deprecated_table_hint | sybase_legacy_hints)?
    | rowset_function             as_table_alias?
    | '(' derived_table ')'       (as_table_alias column_alias_list?)?
    | change_table                as_table_alias?
    | nodes_method                (as_table_alias column_alias_list?)?
    | function_call               (as_table_alias column_alias_list?)?
    | loc_id=LOCAL_ID             as_table_alias?
    | loc_id_call=LOCAL_ID '.' loc_fcall=function_call (as_table_alias column_alias_list?)?
    | open_xml
    | open_json
    | DOUBLE_COLON oldstyle_fcall=function_call       as_table_alias? // Build-in function (old syntax)
    | '(' table_source ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/functions/openxml-transact-sql
open_xml
    : OPENXML '(' expression ',' expression (',' expression)? ')'
    (WITH '(' schema_declaration ')' )? as_table_alias?
    ;

open_json
    : OPENJSON '(' expression (',' expression)? ')'
    (WITH '(' json_declaration ')' )? as_table_alias?
    ;

json_declaration
    : json_col+=json_column_declaration (',' json_col+=json_column_declaration)*
    ;

json_column_declaration
    : column_declaration (AS JSON)?
    ;

schema_declaration
    : xml_col+=column_declaration (',' xml_col+=column_declaration)*
    ;

column_declaration
    : id_ data_type STRING?
    ;

change_table
    : change_table_changes
    | change_table_version
    ;

change_table_changes
    : CHANGETABLE '(' CHANGES changetable=table_name ',' changesid=(NULL_ | DECIMAL | LOCAL_ID) ')'
    ;
change_table_version
    : CHANGETABLE '(' VERSION versiontable=table_name ',' pk_columns=full_column_name_list ',' pk_values=select_list  ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms191472.aspx
join_part
    // https://msdn.microsoft.com/en-us/library/ms173815(v=sql.120).aspx
    : join_on
    | cross_join
    | apply_
    | pivot
    | unpivot
    ;
join_on
    : (inner=INNER? | join_type=(LEFT | RIGHT | FULL) outer=OUTER?) (join_hint=(LOOP | HASH | MERGE | REMOTE))?
       JOIN source=table_source ON cond=search_condition
    ;

cross_join
    : CROSS JOIN table_source_item
    ;

apply_
    : apply_style=(CROSS | OUTER) APPLY source=table_source_item
    ;

pivot
    : PIVOT pivot_clause as_table_alias
    ;

unpivot
    : UNPIVOT unpivot_clause as_table_alias
    ;

pivot_clause
    : '(' aggregate_windowed_function FOR full_column_name IN column_alias_list ')'
    ;

unpivot_clause
    : '(' unpivot_exp=expression FOR full_column_name IN '(' full_column_name_list ')' ')'
    ;

full_column_name_list
    : column+=full_column_name (',' column+=full_column_name)*
    ;

// https://msdn.microsoft.com/en-us/library/ms190312.aspx
rowset_function
    :  (
        OPENROWSET LR_BRACKET provider_name = STRING COMMA connectionString = STRING COMMA sql = STRING RR_BRACKET
     )
     | ( OPENROWSET '(' BULK data_file=STRING ',' (bulk_option (',' bulk_option)* | id_)')' )
    ;

// runtime check.
bulk_option
    : id_ '=' bulk_option_value=(DECIMAL | STRING)
    ;

derived_table
    : subquery
    | '(' subquery (UNION ALL subquery)* ')'
    | table_value_constructor
    | '(' table_value_constructor ')'
    ;

function_call
    : ranking_windowed_function                         #RANKING_WINDOWED_FUNC
    | aggregate_windowed_function                       #AGGREGATE_WINDOWED_FUNC
    | analytic_windowed_function                        #ANALYTIC_WINDOWED_FUNC
    | built_in_functions                                #BUILT_IN_FUNC
    | scalar_function_name '(' expression_list_? ')'     #SCALAR_FUNCTION
    | freetext_function                                 #FREE_TEXT
    | partition_function                                #PARTITION_FUNC
    | hierarchyid_static_method                         #HIERARCHYID_METHOD
    ;

partition_function
    : (database=id_ '.')? DOLLAR_PARTITION '.' func_name=id_ '(' expression ')'
    ;

freetext_function
    : (CONTAINSTABLE | FREETEXTTABLE) '(' table_name ',' (full_column_name | '(' full_column_name (',' full_column_name)* ')' | '*' ) ',' expression  (',' LANGUAGE expression)? (',' expression)? ')'
    | (SEMANTICSIMILARITYTABLE | SEMANTICKEYPHRASETABLE) '(' table_name ',' (full_column_name | '(' full_column_name (',' full_column_name)* ')' | '*' ) ',' expression ')'
    | SEMANTICSIMILARITYDETAILSTABLE '(' table_name ',' full_column_name ',' expression ',' full_column_name ',' expression ')'
    ;

freetext_predicate
    : CONTAINS '(' (full_column_name | '(' full_column_name (',' full_column_name)* ')' | '*' | PROPERTY '(' full_column_name ',' expression ')') ',' expression ')'
    | FREETEXT '(' table_name ',' (full_column_name | '(' full_column_name (',' full_column_name)* ')' | '*' ) ',' expression  (',' LANGUAGE expression)? ')'
    ;

json_key_value
    : json_key_name=expression ':' value_expression=expression
    ;

json_null_clause
    : (ABSENT | NULL_) ON NULL_
    ;

built_in_functions
    // Metadata functions
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/app-name-transact-sql?view=sql-server-ver16
    : APP_NAME '(' ')'                                                      #APP_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/applock-mode-transact-sql?view=sql-server-ver16
    | APPLOCK_MODE '(' database_principal=expression ',' resource_name=expression ',' lock_owner=expression ')' #APPLOCK_MODE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/applock-test-transact-sql?view=sql-server-ver16
    | APPLOCK_TEST '(' database_principal=expression ',' resource_name=expression ',' lock_mode=expression ',' lock_owner=expression ')' #APPLOCK_TEST
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/assemblyproperty-transact-sql?view=sql-server-ver16
    | ASSEMBLYPROPERTY '(' assembly_name=expression ',' property_name=expression ')' #ASSEMBLYPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/col-length-transact-sql?view=sql-server-ver16
    | COL_LENGTH '(' table=expression ',' column=expression ')'             #COL_LENGTH
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/col-name-transact-sql?view=sql-server-ver16
    | COL_NAME '(' table_id=expression ',' column_id=expression ')'         #COL_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/columnproperty-transact-sql?view=sql-server-ver16
    | COLUMNPROPERTY '(' id=expression ',' column=expression ',' property=expression ')' #COLUMNPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/databasepropertyex-transact-sql?view=sql-server-ver16
    | DATABASEPROPERTYEX '(' database=expression ',' property=expression ')' #DATABASEPROPERTYEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/db-id-transact-sql?view=sql-server-ver16
    | DB_ID '(' database_name=expression? ')'                               #DB_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/db-name-transact-sql?view=sql-server-ver16
    | DB_NAME '(' database_id=expression? ')'                               #DB_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/file-id-transact-sql?view=sql-server-ver16
    | FILE_ID '(' file_name=expression ')'                                  #FILE_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/file-idex-transact-sql?view=sql-server-ver16
    | FILE_IDEX '(' file_name=expression ')'                                #FILE_IDEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/file-name-transact-sql?view=sql-server-ver16
    | FILE_NAME '(' file_id=expression ')'                                  #FILE_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/filegroup-id-transact-sql?view=sql-server-ver16
    | FILEGROUP_ID '(' filegroup_name=expression ')'                        #FILEGROUP_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/filegroup-name-transact-sql?view=sql-server-ver16
    | FILEGROUP_NAME '(' filegroup_id=expression ')'                        #FILEGROUP_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/filegroupproperty-transact-sql?view=sql-server-ver16
    | FILEGROUPPROPERTY '(' filegroup_name=expression ',' property=expression ')' #FILEGROUPPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/fileproperty-transact-sql?view=sql-server-ver16
    | FILEPROPERTY '(' file_name=expression ',' property=expression ')'     #FILEPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/filepropertyex-transact-sql?view=sql-server-ver16
    | FILEPROPERTYEX '(' name=expression ',' property=expression ')'        #FILEPROPERTYEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/fulltextcatalogproperty-transact-sql?view=sql-server-ver16
    | FULLTEXTCATALOGPROPERTY '(' catalog_name=expression ',' property=expression ')' #FULLTEXTCATALOGPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/fulltextserviceproperty-transact-sql?view=sql-server-ver16
    | FULLTEXTSERVICEPROPERTY '(' property=expression ')'                   #FULLTEXTSERVICEPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/index-col-transact-sql?view=sql-server-ver16
    | INDEX_COL '(' table_or_view_name=expression ',' index_id=expression ',' key_id=expression ')' #INDEX_COL
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/indexkey-property-transact-sql?view=sql-server-ver16
    | INDEXKEY_PROPERTY '(' object_id=expression ',' index_id=expression ',' key_id=expression ',' property=expression ')' #INDEXKEY_PROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/indexproperty-transact-sql?view=sql-server-ver16
    | INDEXPROPERTY '(' object_id=expression ',' index_or_statistics_name=expression ',' property=expression ')' #INDEXPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/next-value-for-transact-sql?view=sql-server-ver16
    | NEXT VALUE FOR sequence_name=table_name ( OVER '(' order_by_clause ')' )? #NEXT_VALUE_FOR
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/object-definition-transact-sql?view=sql-server-ver16
    | OBJECT_DEFINITION '(' object_id=expression ')'                        #OBJECT_DEFINITION
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/object-id-transact-sql?view=sql-server-ver16
    | OBJECT_ID '(' object_name=expression ( ',' object_type=expression )? ')'      #OBJECT_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/object-name-transact-sql?view=sql-server-ver16
    | OBJECT_NAME '(' object_id=expression ( ',' database_id=expression )? ')' #OBJECT_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/object-schema-name-transact-sql?view=sql-server-ver16
    | OBJECT_SCHEMA_NAME '(' object_id=expression ( ',' database_id=expression )? ')' #OBJECT_SCHEMA_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/objectproperty-transact-sql?view=sql-server-ver16
    | OBJECTPROPERTY '(' id=expression ',' property=expression ')'          #OBJECTPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/objectpropertyex-transact-sql?view=sql-server-ver16
    | OBJECTPROPERTYEX '(' id=expression ',' property=expression ')'        #OBJECTPROPERTYEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/original-db-name-transact-sql?view=sql-server-ver16
    | ORIGINAL_DB_NAME '(' ')'                                              #ORIGINAL_DB_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/parsename-transact-sql?view=sql-server-ver16
    | PARSENAME '(' object_name=expression ',' object_piece=expression ')'  #PARSENAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/schema-id-transact-sql?view=sql-server-ver16
    | SCHEMA_ID '(' schema_name=expression? ')'                             #SCHEMA_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/schema-name-transact-sql?view=sql-server-ver16
    | SCHEMA_NAME '(' schema_id=expression? ')'                             #SCHEMA_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/scope-identity-transact-sql?view=sql-server-ver16
    | SCOPE_IDENTITY '(' ')'                                                #SCOPE_IDENTITY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/serverproperty-transact-sql?view=sql-server-ver16
    | SERVERPROPERTY '(' property=expression ')'                            #SERVERPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/stats-date-transact-sql?view=sql-server-ver16
    | STATS_DATE '(' object_id=expression ',' stats_id=expression ')'       #STATS_DATE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/type-id-transact-sql?view=sql-server-ver16
    | TYPE_ID '(' type_name=expression ')'                                  #TYPE_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/type-name-transact-sql?view=sql-server-ver16
    | TYPE_NAME '(' type_id=expression ')'                                  #TYPE_NAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/typeproperty-transact-sql?view=sql-server-ver16
    | TYPEPROPERTY '(' type=expression ',' property=expression ')'          #TYPEPROPERTY
    // String functions
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/ascii-transact-sql?view=sql-server-ver16
    | ASCII '(' character_expression=expression ')'                         #ASCII
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/char-transact-sql?view=sql-server-ver16
    | CHAR '(' integer_expression=expression ')'                            #CHAR
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/charindex-transact-sql?view=sql-server-ver16
    | CHARINDEX '(' expressionToFind=expression ',' expressionToSearch=expression ( ',' start_location=expression )? ')' #CHARINDEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/concat-transact-sql?view=sql-server-ver16
    | CONCAT '(' string_value_1=expression ',' string_value_2=expression ( ',' string_value_n+=expression )* ')' #CONCAT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/concat-ws-transact-sql?view=sql-server-ver16
    | CONCAT_WS '(' separator=expression ',' argument_1=expression ',' argument_2=expression ( ',' argument_n+=expression )* ')' #CONCAT_WS
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/difference-transact-sql?view=sql-server-ver16
    | DIFFERENCE '(' character_expression_1=expression ',' character_expression_2=expression ')' #DIFFERENCE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/format-transact-sql?view=sql-server-ver16
    | FORMAT '(' value=expression ',' format=expression ( ',' culture=expression )? ')' #FORMAT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/left-transact-sql?view=sql-server-ver16
    | LEFT '(' character_expression=expression ',' integer_expression=expression ')' #LEFT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/len-transact-sql?view=sql-server-ver16
    | LEN '(' string_expression=expression ')'                              #LEN
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/lower-transact-sql?view=sql-server-ver16
    | LOWER '(' character_expression=expression ')'                         #LOWER
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/ltrim-transact-sql?view=sql-server-ver16
    | LTRIM '(' character_expression=expression ')'                         #LTRIM
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/nchar-transact-sql?view=sql-server-ver16
    | NCHAR '(' integer_expression=expression ')'                           #NCHAR
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/patindex-transact-sql?view=sql-server-ver16
    | PATINDEX '(' pattern=expression ',' string_expression=expression ')'  #PATINDEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/quotename-transact-sql?view=sql-server-ver16
    | QUOTENAME '(' character_string=expression ( ',' quote_character=expression )? ')' #QUOTENAME
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/replace-transact-sql?view=sql-server-ver16
    | REPLACE '(' input=expression ',' replacing=expression ',' with=expression ')'   #REPLACE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/replicate-transact-sql?view=sql-server-ver16
    | REPLICATE '(' string_expression=expression ',' integer_expression=expression ')' #REPLICATE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/reverse-transact-sql?view=sql-server-ver16
    | REVERSE '(' string_expression=expression ')'                          #REVERSE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/right-transact-sql?view=sql-server-ver16
    | RIGHT '(' character_expression=expression ',' integer_expression=expression ')' #RIGHT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/rtrim-transact-sql?view=sql-server-ver16
    | RTRIM '(' character_expression=expression ')'                         #RTRIM
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/soundex-transact-sql?view=sql-server-ver16
    | SOUNDEX '(' character_expression=expression ')'                       #SOUNDEX
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/space-transact-sql?view=sql-server-ver16
    | SPACE_KEYWORD '(' integer_expression=expression ')'                   #SPACE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/str-transact-sql?view=sql-server-ver16
    | STR '(' float_expression=expression ( ',' length_expression=expression ( ',' decimal=expression )? )? ')' #STR
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/string-agg-transact-sql?view=sql-server-ver16
    | STRING_AGG '(' expr=expression ',' separator=expression ')' (WITHIN GROUP '(' order_by_clause ')')?  #STRINGAGG
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/string-escape-transact-sql?view=sql-server-ver16
    | STRING_ESCAPE '(' text_=expression ',' type_=expression ')'           #STRING_ESCAPE
    // https://msdn.microsoft.com/fr-fr/library/ms188043.aspx
    | STUFF '(' str=expression ',' from=expression ',' to=expression ',' str_with=expression ')' #STUFF
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/substring-transact-sql?view=sql-server-ver16
    | SUBSTRING '(' string_expression=expression ',' start_=expression ',' length=expression ')' #SUBSTRING
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/translate-transact-sql?view=sql-server-ver16
    | TRANSLATE '(' inputString=expression ',' characters=expression ',' translations=expression ')' #TRANSLATE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/trim-transact-sql?view=sql-server-ver16
    | TRIM '(' ( characters=expression FROM )? string_=expression ')'       #TRIM
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/unicode-transact-sql?view=sql-server-ver16
    | UNICODE '(' ncharacter_expression=expression ')'                      #UNICODE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/upper-transact-sql?view=sql-server-ver16
    | UPPER '(' character_expression=expression ')'                         #UPPER
    // System functions
    // https://msdn.microsoft.com/en-us/library/ms173784.aspx
    | BINARY_CHECKSUM '(' ( star='*' | expression (',' expression)* ) ')'   #BINARY_CHECKSUM
    // https://msdn.microsoft.com/en-us/library/ms189788.aspx
    | CHECKSUM '(' ( star='*' | expression (',' expression)* ) ')'          #CHECKSUM
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/compress-transact-sql?view=sql-server-ver16
    | COMPRESS '(' expr=expression ')'                                      #COMPRESS
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/connectionproperty-transact-sql?view=sql-server-ver16
    | CONNECTIONPROPERTY '(' property=STRING ')'                            #CONNECTIONPROPERTY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/context-info-transact-sql?view=sql-server-ver16
    | CONTEXT_INFO '(' ')'                                                  #CONTEXT_INFO
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/current-request-id-transact-sql?view=sql-server-ver16
    | CURRENT_REQUEST_ID '(' ')'                                            #CURRENT_REQUEST_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/current-transaction-id-transact-sql?view=sql-server-ver16
    | CURRENT_TRANSACTION_ID '(' ')'                                        #CURRENT_TRANSACTION_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/decompress-transact-sql?view=sql-server-ver16
    | DECOMPRESS '(' expr=expression ')'                                    #DECOMPRESS
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-line-transact-sql?view=sql-server-ver16
    | ERROR_LINE '(' ')'                                                    #ERROR_LINE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-message-transact-sql?view=sql-server-ver16
    | ERROR_MESSAGE '(' ')'                                                 #ERROR_MESSAGE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-number-transact-sql?view=sql-server-ver16
    | ERROR_NUMBER '(' ')'                                                  #ERROR_NUMBER
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-procedure-transact-sql?view=sql-server-ver16
    | ERROR_PROCEDURE '(' ')'                                               #ERROR_PROCEDURE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-severity-transact-sql?view=sql-server-ver16
    | ERROR_SEVERITY '(' ')'                                                #ERROR_SEVERITY
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/error-state-transact-sql?view=sql-server-ver16
    | ERROR_STATE '(' ')'                                                   #ERROR_STATE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/formatmessage-transact-sql?view=sql-server-ver16
    | FORMATMESSAGE '(' (msg_number=DECIMAL | msg_string=STRING | msg_variable=LOCAL_ID) ',' expression (',' expression)* ')' #FORMATMESSAGE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/get-filestream-transaction-context-transact-sql?view=sql-server-ver16
    | GET_FILESTREAM_TRANSACTION_CONTEXT '(' ')'                            #GET_FILESTREAM_TRANSACTION_CONTEXT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/getansinull-transact-sql?view=sql-server-ver16
    | GETANSINULL '(' (database=STRING)? ')'                                #GETANSINULL
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/host-id-transact-sql?view=sql-server-ver16
    | HOST_ID '(' ')'                                                       #HOST_ID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/host-name-transact-sql?view=sql-server-ver16
    | HOST_NAME '(' ')'                                                     #HOST_NAME
    // https://msdn.microsoft.com/en-us/library/ms184325.aspx
    | ISNULL '(' left=expression ',' right=expression ')'                   #ISNULL
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/isnumeric-transact-sql?view=sql-server-ver16
    | ISNUMERIC '(' expression ')'                                          #ISNUMERIC
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/min-active-rowversion-transact-sql?view=sql-server-ver16
    | MIN_ACTIVE_ROWVERSION '(' ')'                                         #MIN_ACTIVE_ROWVERSION
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/newid-transact-sql?view=sql-server-ver16
    | NEWID '(' ')'                                                         #NEWID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/newsequentialid-transact-sql?view=sql-server-ver16
    | NEWSEQUENTIALID '(' ')'                                               #NEWSEQUENTIALID
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/rowcount-big-transact-sql?view=sql-server-ver16
    | ROWCOUNT_BIG '(' ')'                                                  #ROWCOUNT_BIG
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/session-context-transact-sql?view=sql-server-ver16
    | SESSION_CONTEXT '(' key=STRING ')'                                    #SESSION_CONTEXT
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/xact-state-transact-sql?view=sql-server-ver16
    | XACT_STATE '(' ')'                                                    #XACT_STATE
    // https://msdn.microsoft.com/en-us/library/hh231076.aspx
    // https://msdn.microsoft.com/en-us/library/ms187928.aspx
    | CAST '(' expression AS data_type ')'              #CAST
    | TRY_CAST '(' expression AS data_type ')'          #TRY_CAST
    | CONVERT '(' convert_data_type=data_type ','convert_expression=expression (',' style=expression)? ')'                              #CONVERT
    // https://msdn.microsoft.com/en-us/library/ms190349.aspx
    | COALESCE '(' expression_list_ ')'                  #COALESCE
    // Cursor functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cursor-rows-transact-sql?view=sql-server-ver16
    | CURSOR_ROWS                                       #CURSOR_ROWS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cursor-rows-transact-sql?view=sql-server-ver16
    | FETCH_STATUS                                      #FETCH_STATUS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cursor-status-transact-sql?view=sql-server-ver16
    | CURSOR_STATUS '(' scope=STRING ',' cursor=expression ')' #CURSOR_STATUS
    // Cryptographic functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cert-id-transact-sql?view=sql-server-ver16
    | CERT_ID '(' cert_name=expression ')'              #CERT_ID
    // Data type functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datalength-transact-sql?view=sql-server-ver16
    | DATALENGTH '(' expression ')'                     #DATALENGTH
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ident-current-transact-sql?view=sql-server-ver16
    | IDENT_CURRENT '(' table_or_view=expression ')'    # IDENT_CURRENT
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ident-incr-transact-sql?view=sql-server-ver16
    | IDENT_INCR '(' table_or_view=expression ')'       # IDENT_INCR
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ident-seed-transact-sql?view=sql-server-ver16
    | IDENT_SEED '(' table_or_view=expression ')'       # IDENT_SEED
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ident-seed-transact-sql?view=sql-server-ver16
    | IDENTITY '(' datatype=data_type (',' seed=DECIMAL ',' increment=DECIMAL)? ')' #IDENTITY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ident-seed-transact-sql?view=sql-server-ver16
    | SQL_VARIANT_PROPERTY '(' expr=expression ',' property=STRING ')' #SQL_VARIANT_PROPERTY
    // Date functions
    //https://infocenter.sybase.com/help/index.jsp?topic=/com.sybase.infocenter.dc36271.1572/html/blocks/CJADIDHD.htm
    | CURRENT_DATE '(' ')'                              #CURRENT_DATE
    // https://msdn.microsoft.com/en-us/library/ms188751.aspx
    | CURRENT_TIMESTAMP                                 #CURRENT_TIMESTAMP
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/current-timezone-transact-sql?view=sql-server-ver16
    | CURRENT_TIMEZONE '(' ')'                          #CURRENT_TIMEZONE
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/current-timezone-id-transact-sql?view=sql-server-ver16
    | CURRENT_TIMEZONE_ID '(' ')'                       #CURRENT_TIMEZONE_ID
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/date-bucket-transact-sql?view=sql-server-ver16
    | DATE_BUCKET '(' datepart=dateparts_9 ',' number=expression ',' date=expression (',' origin=expression)? ')' #DATE_BUCKET
    // https://msdn.microsoft.com/en-us/library/ms186819.aspx
    | DATEADD '(' datepart=dateparts_12 ',' number=expression ',' date=expression ')'  #DATEADD
    // https://msdn.microsoft.com/en-us/library/ms189794.aspx
    | DATEDIFF '(' datepart=dateparts_12 ',' date_first=expression ',' date_second=expression ')' #DATEDIFF
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datediff-big-transact-sql?view=sql-server-ver16
    | DATEDIFF_BIG '(' datepart=dateparts_12 ',' startdate=expression ',' enddate=expression ')' #DATEDIFF_BIG
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datefromparts-transact-sql?view=sql-server-ver16
    | DATEFROMPARTS '(' year=expression ',' month=expression ',' day=expression ')'#DATEFROMPARTS
    // https://msdn.microsoft.com/en-us/library/ms174395.aspx
    | DATENAME '(' datepart=dateparts_15 ',' date=expression ')'                #DATENAME
    // https://msdn.microsoft.com/en-us/library/ms174420.aspx
    | DATEPART '(' datepart=dateparts_15 ',' date=expression ')'                #DATEPART
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datetime2fromparts-transact-sql?view=sql-server-ver16
    | DATETIME2FROMPARTS '(' year=expression ',' month=expression ',' day=expression ',' hour=expression ',' minute=expression ',' seconds=expression ',' fractions=expression ',' precision=expression ')' #DATETIME2FROMPARTS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datetimefromparts-transact-sql?view=sql-server-ver16
    | DATETIMEFROMPARTS '(' year=expression ',' month=expression ',' day=expression ',' hour=expression ',' minute=expression ',' seconds=expression ',' milliseconds=expression ')' #DATETIMEFROMPARTS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datetimeoffsetfromparts-transact-sql?view=sql-server-ver16
    | DATETIMEOFFSETFROMPARTS '(' year=expression ',' month=expression ',' day=expression ',' hour=expression ',' minute=expression ',' seconds=expression ',' fractions=expression ',' hour_offset=expression ',' minute_offset=expression ',' precision=DECIMAL ')' #DATETIMEOFFSETFROMPARTS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/datetrunc-transact-sql?view=sql-server-ver16
    | DATETRUNC '(' datepart=dateparts_datetrunc ',' date=expression ')' #DATETRUNC
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/day-transact-sql?view=sql-server-ver16
    | DAY '(' date=expression ')' #DAY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/eomonth-transact-sql?view=sql-server-ver16
    | EOMONTH '(' start_date=expression (',' month_to_add=expression)? ')'#EOMONTH
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/getdate-transact-sql
    | GETDATE '(' ')'                                   #GETDATE
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/getdate-transact-sql
    | GETUTCDATE '(' ')'                                #GETUTCDATE
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/isdate-transact-sql?view=sql-server-ver16
    | ISDATE '(' expression ')' #ISDATE
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/month-transact-sql?view=sql-server-ver16
    | MONTH '(' date=expression ')' #MONTH
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/smalldatetimefromparts-transact-sql?view=sql-server-ver16
    | SMALLDATETIMEFROMPARTS '(' year=expression ',' month=expression ',' day=expression ',' hour=expression ',' minute=expression ')' #SMALLDATETIMEFROMPARTS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/switchoffset-transact-sql?view=sql-server-ver16
    | SWITCHOFFSET '(' datetimeoffset_expression=expression ',' timezoneoffset_expression=expression ')' #SWITCHOFFSET
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sysdatetime-transact-sql?view=sql-server-ver16
    | SYSDATETIME '(' ')' #SYSDATETIME
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sysdatetimeoffset-transact-sql?view=sql-server-ver16
    | SYSDATETIMEOFFSET '(' ')' #SYSDATETIMEOFFSET
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sysutcdatetime-transact-sql?view=sql-server-ver16
    | SYSUTCDATETIME '(' ')' #SYSUTCDATETIME
    //https://learn.microsoft.com/en-us/sql/t-sql/functions/timefromparts-transact-sql?view=sql-server-ver16
    | TIMEFROMPARTS '(' hour=expression ',' minute=expression ',' seconds=expression ',' fractions=expression ',' precision=DECIMAL ')' #TIMEFROMPARTS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/todatetimeoffset-transact-sql?view=sql-server-ver16
    | TODATETIMEOFFSET '(' datetime_expression=expression ',' timezoneoffset_expression=expression ')' #TODATETIMEOFFSET
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/year-transact-sql?view=sql-server-ver16
    | YEAR '(' date=expression ')' #YEAR
    // https://msdn.microsoft.com/en-us/library/ms189838.aspx
    | IDENTITY '(' data_type (',' seed=DECIMAL)? (',' increment=DECIMAL)? ')'                                                           #IDENTITY
    // https://msdn.microsoft.com/en-us/library/bb839514.aspx
    | MIN_ACTIVE_ROWVERSION '(' ')'                     #MIN_ACTIVE_ROWVERSION
    // https://msdn.microsoft.com/en-us/library/ms177562.aspx
    | NULLIF '(' left=expression ',' right=expression ')'          #NULLIF
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/parse-transact-sql
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/try-parse-transact-sql
    | PARSE '(' str=expression AS data_type ( USING culture=expression )? ')'          #PARSE
    // https://docs.microsoft.com/en-us/sql/t-sql/xml/xml-data-type-methods
    | xml_data_type_methods                             #XML_DATA_TYPE_FUNC
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/logical-functions-iif-transact-sql
    | IIF '(' cond=search_condition ',' left=expression ',' right=expression ')'   #IIF
    // JSON functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/isjson-transact-sql?view=azure-sqldw-latest
    | ISJSON '(' json_expr=expression (',' json_type_constraint=expression)? ')' #ISJSON
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-object-transact-sql?view=azure-sqldw-latest
    | JSON_OBJECT '(' (key_value=json_key_value (',' key_value=json_key_value)*)? json_null_clause? ')' #JSON_OBJECT
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-array-transact-sql?view=azure-sqldw-latest
    | JSON_ARRAY '(' expression_list_? json_null_clause? ')' #JSON_ARRAY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-value-transact-sql?view=azure-sqldw-latest
    | JSON_VALUE '(' expr=expression ',' path=expression ')' #JSON_VALUE
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-query-transact-sql?view=azure-sqldw-latest
    | JSON_QUERY '(' expr=expression (',' path=expression)? ')' #JSON_QUERY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-modify-transact-sql?view=azure-sqldw-latest
    | JSON_MODIFY '(' expr=expression ',' path=expression ',' new_value=expression ')' #JSON_MODIFY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/json-path-exists-transact-sql?view=azure-sqldw-latest
    | JSON_PATH_EXISTS '(' value_expression=expression ',' sql_json_path=expression ')' #JSON_PATH_EXISTS
    // Math functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/abs-transact-sql?view=sql-server-ver16
    | ABS '(' numeric_expression=expression ')' #ABS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/acos-transact-sql?view=sql-server-ver16
    | ACOS '(' float_expression=expression ')' #ACOS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/asin-transact-sql?view=sql-server-ver16
    | ASIN '(' float_expression=expression ')' #ASIN
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/atan-transact-sql?view=sql-server-ver16
    | ATAN '(' float_expression=expression ')' #ATAN
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/atn2-transact-sql?view=sql-server-ver16
    | ATN2 '(' float_expression=expression ',' float_expression=expression ')' #ATN2
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/ceiling-transact-sql?view=sql-server-ver16
    | CEILING '(' numeric_expression=expression ')' #CEILING
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cos-transact-sql?view=sql-server-ver16
    | COS '(' float_expression=expression ')' #COS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/cot-transact-sql?view=sql-server-ver16
    | COT '(' float_expression=expression ')' #COT
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/degrees-transact-sql?view=sql-server-ver16
    | DEGREES '(' numeric_expression=expression ')' #DEGREES
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/exp-transact-sql?view=sql-server-ver16
    | EXP '(' float_expression=expression ')' #EXP
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/floor-transact-sql?view=sql-server-ver16
    | FLOOR '(' numeric_expression=expression ')' #FLOOR
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/log-transact-sql?view=sql-server-ver16
    | LOG '(' float_expression=expression (',' base=expression)? ')' #LOG
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/log10-transact-sql?view=sql-server-ver16
    | LOG10 '(' float_expression=expression ')' #LOG10
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/pi-transact-sql?view=sql-server-ver16
    | PI '(' ')' #PI
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/power-transact-sql?view=sql-server-ver16
    | POWER '(' float_expression=expression ',' y=expression ')' #POWER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/radians-transact-sql?view=sql-server-ver16
    | RADIANS '(' numeric_expression=expression ')' #RADIANS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/rand-transact-sql?view=sql-server-ver16
    | RAND '(' (seed=expression)? ')' #RAND
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/round-transact-sql?view=sql-server-ver16
    | ROUND '(' numeric_expression=expression ',' length=expression (',' function=expression)? ')' #ROUND
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sign-transact-sql?view=sql-server-ver16
    | SIGN '(' numeric_expression=expression ')' #MATH_SIGN
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sin-transact-sql?view=sql-server-ver16
    | SIN '(' float_expression=expression ')' #SIN
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sqrt-transact-sql?view=sql-server-ver16
    | SQRT '(' float_expression=expression ')' #SQRT
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/square-transact-sql?view=sql-server-ver16
    | SQUARE '(' float_expression=expression ')' #SQUARE
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/tan-transact-sql?view=sql-server-ver16
    | TAN '(' float_expression=expression ')' #TAN
    // Logical functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/logical-functions-greatest-transact-sql?view=azure-sqldw-latest
    | GREATEST '(' expression_list_ ')' #GREATEST
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/logical-functions-least-transact-sql?view=azure-sqldw-latest
    | LEAST '(' expression_list_ ')' #LEAST
    // Security functions
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/certencoded-transact-sql?view=sql-server-ver16
    | CERTENCODED '(' certid=expression ')'             #CERTENCODED
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/certprivatekey-transact-sql?view=sql-server-ver16
    | CERTPRIVATEKEY '(' certid=expression ',' encryption_password=expression (',' decryption_pasword=expression)? ')' #CERTPRIVATEKEY
    // https://msdn.microsoft.com/en-us/library/ms176050.aspx
    | CURRENT_USER                                      #CURRENT_USER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/database-principal-id-transact-sql?view=sql-server-ver16
    | DATABASE_PRINCIPAL_ID '(' (principal_name=expression)? ')' #DATABASE_PRINCIPAL_ID
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/has-dbaccess-transact-sql?view=sql-server-ver16
    | HAS_DBACCESS '(' database_name=expression ')'     #HAS_DBACCESS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/has-perms-by-name-transact-sql?view=sql-server-ver16
    | HAS_PERMS_BY_NAME '(' securable=expression ',' securable_class=expression ',' permission=expression ( ',' sub_securable=expression (',' sub_securable_class=expression )? )? ')' #HAS_PERMS_BY_NAME
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/is-member-transact-sql?view=sql-server-ver16
    | IS_MEMBER '(' group_or_role=expression ')'        #IS_MEMBER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/is-rolemember-transact-sql?view=sql-server-ver16
    | IS_ROLEMEMBER '(' role=expression ( ',' database_principal=expression )? ')' #IS_ROLEMEMBER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/is-srvrolemember-transact-sql?view=sql-server-ver16
    | IS_SRVROLEMEMBER '(' role=expression ( ',' login=expression )? ')' #IS_SRVROLEMEMBER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/loginproperty-transact-sql?view=sql-server-ver16
    | LOGINPROPERTY '(' login_name=expression ',' property_name=expression ')' #LOGINPROPERTY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/original-login-transact-sql?view=sql-server-ver16
    | ORIGINAL_LOGIN '(' ')'                            #ORIGINAL_LOGIN
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/permissions-transact-sql?view=sql-server-ver16
    | PERMISSIONS '(' ( object_id=expression (',' column=expression)? )? ')' #PERMISSIONS
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/pwdencrypt-transact-sql?view=sql-server-ver16
    | PWDENCRYPT '(' password=expression ')'            #PWDENCRYPT
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/pwdcompare-transact-sql?view=sql-server-ver16
    | PWDCOMPARE '(' clear_text_password=expression ',' password_hash=expression (',' version=expression )?')' #PWDCOMPARE
    // https://msdn.microsoft.com/en-us/library/ms177587.aspx
    | SESSION_USER                                      #SESSION_USER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/sessionproperty-transact-sql?view=sql-server-ver16
    | SESSIONPROPERTY '(' option_name=expression ')'    #SESSIONPROPERTY
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/suser-id-transact-sql?view=sql-server-ver16
    | SUSER_ID '(' (login=expression)? ')'              #SUSER_ID
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/suser-name-transact-sql?view=sql-server-ver16
    | SUSER_NAME '(' (server_user_sid=expression)? ')'  #SUSER_SNAME
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/suser-sid-transact-sql?view=sql-server-ver16
    | SUSER_SID '(' (login=expression (',' param2=expression)?)? ')' #SUSER_SID
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/suser-sname-transact-sql?view=sql-server-ver16
    | SUSER_SNAME '(' (server_user_sid=expression)? ')' #SUSER_SNAME
    // https://msdn.microsoft.com/en-us/library/ms179930.aspx
    | SYSTEM_USER                                       #SYSTEM_USER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/user-transact-sql?view=sql-server-ver16
    | USER                                              #USER
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/user-id-transact-sql?view=sql-server-ver16
    | USER_ID '(' (user=expression)? ')'                #USER_ID
    // https://learn.microsoft.com/en-us/sql/t-sql/functions/user-name-transact-sql?view=sql-server-ver16
    | USER_NAME '(' (id=expression)? ')' #USER_NAME
    ;

xml_data_type_methods
    : value_method
    | query_method
    | exist_method
    | modify_method
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/functions/date-bucket-transact-sql?view=sql-server-ver16
dateparts_9
    : YEAR | YEAR_ABBR
    | QUARTER | QUARTER_ABBR
    | MONTH | MONTH_ABBR
    | DAY | DAY_ABBR
    | WEEK | WEEK_ABBR
    | HOUR | HOUR_ABBR
    | MINUTE | MINUTE_ABBR
    | SECOND | SECOND_ABBR
    | MILLISECOND | MILLISECOND_ABBR
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/functions/dateadd-transact-sql?view=sql-server-ver16
dateparts_12
    : dateparts_9
    | DAYOFYEAR | DAYOFYEAR_ABBR
    | MICROSECOND | MICROSECOND_ABBR
    | NANOSECOND | NANOSECOND_ABBR
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/functions/datename-transact-sql?view=sql-server-ver16
dateparts_15
    : dateparts_12
    | WEEKDAY | WEEKDAY_ABBR
    | TZOFFSET | TZOFFSET_ABBR
    | ISO_WEEK | ISO_WEEK_ABBR
    ;

// https://learn.microsoft.com/en-us/sql/t-sql/functions/datetrunc-transact-sql?view=sql-server-ver16
dateparts_datetrunc
    : dateparts_9
    | DAYOFYEAR | DAYOFYEAR_ABBR
    | MICROSECOND | MICROSECOND_ABBR
    | ISO_WEEK | ISO_WEEK_ABBR
    ;

value_method
    : (loc_id=LOCAL_ID | value_id=full_column_name | eventdata=EVENTDATA '(' ')'  | query=query_method | '(' subquery ')') '.' call=value_call
    ;

value_call
    :  (VALUE | VALUE_SQUARE_BRACKET) '(' xquery=STRING ',' sqltype=STRING ')'
    ;

query_method
    : (loc_id=LOCAL_ID | value_id=full_column_name | '(' subquery ')' ) '.' call=query_call
    ;

query_call
    : (QUERY | QUERY_SQUARE_BRACKET) '(' xquery=STRING ')'
    ;

exist_method
    : (loc_id=LOCAL_ID | value_id=full_column_name | '(' subquery ')') '.' call=exist_call
    ;

exist_call
    : (EXIST | EXIST_SQUARE_BRACKET) '(' xquery=STRING ')'
    ;

modify_method
    : (loc_id=LOCAL_ID | value_id=full_column_name | '(' subquery ')') '.' call=modify_call
    ;

modify_call
    : (MODIFY | MODIFY_SQUARE_BRACKET) '(' xml_dml=STRING ')'
    ;

hierarchyid_call
    : GETANCESTOR '(' n=expression ')'
    | GETDESCENDANT '(' child1=expression ',' child2=expression ')'
    | GETLEVEL '(' ')'
    | ISDESCENDANTOF '(' parent_=expression ')'
    | GETREPARENTEDVALUE '(' oldroot=expression ',' newroot=expression ')'
    | TOSTRING '(' ')'
    ;

hierarchyid_static_method
    : HIERARCHYID DOUBLE_COLON (GETROOT '(' ')' | PARSE '(' input=expression ')')
    ;

nodes_method
    : (loc_id=LOCAL_ID | value_id=full_column_name | '(' subquery ')') '.' NODES '(' xquery=STRING ')'
    ;


switch_section
    : WHEN expression THEN expression
    ;

switch_search_condition_section
    : WHEN search_condition THEN expression
    ;

as_column_alias
    : AS? column_alias
    ;

as_table_alias
    : AS? table_alias
    ;

table_alias
    : id_
    ;

// https://msdn.microsoft.com/en-us/library/ms187373.aspx
with_table_hints
    : WITH '(' hint+=table_hint (','? hint+=table_hint)* ')'
    ;

deprecated_table_hint
    : '(' table_hint ')'
    ;

// https://infocenter-archive.sybase.com/help/index.jsp?topic=/com.sybase.infocenter.dc00938.1502/html/locking/locking103.htm
// https://infocenter-archive.sybase.com/help/index.jsp?topic=/com.sybase.dc32300_1250/html/sqlug/sqlug792.htm
// https://infocenter-archive.sybase.com/help/index.jsp?topic=/com.sybase.dc36271_36272_36273_36274_1250/html/refman/X35229.htm
// Legacy hint with no parenthesis and no WITH keyword. Actually conflicts with table alias name except for holdlock which is
// a reserved keyword in this grammar. We might want a separate sybase grammar variant.
sybase_legacy_hints
    : sybase_legacy_hint+
    ;

sybase_legacy_hint
    : HOLDLOCK
    | NOHOLDLOCK
    | READPAST
    | SHARED
    ;

// For simplicity, we don't build subsets for INSERT/UPDATE/DELETE/SELECT/MERGE
// which means the grammar accept slightly more than the what the specification (documentation) says.
table_hint
    : NOEXPAND
    | INDEX (
            '(' index_value (',' index_value)* ')'
            | '=' '(' index_value ')'
            | '=' index_value // examples in the doc include this syntax
            )
    | FORCESEEK ( '(' index_value '(' column_name_list ')' ')' )?
    | FORCESCAN
    | HOLDLOCK
    | NOLOCK
    | NOWAIT
    | PAGLOCK
    | READCOMMITTED
    | READCOMMITTEDLOCK
    | READPAST
    | READUNCOMMITTED
    | REPEATABLEREAD
    | ROWLOCK
    | SERIALIZABLE
    | SNAPSHOT
    | SPATIAL_WINDOW_MAX_CELLS '=' DECIMAL
    | TABLOCK
    | TABLOCKX
    | UPDLOCK
    | XLOCK
    | KEEPIDENTITY
    | KEEPDEFAULTS
    | IGNORE_CONSTRAINTS
    | IGNORE_TRIGGERS
    ;

index_value
    : id_ | DECIMAL
    ;

column_alias_list
    : '(' alias+=column_alias (',' alias+=column_alias)* ')'
    ;

column_alias
    : id_
    | STRING
    ;

table_value_constructor
    : VALUES '(' exps+=expression_list_ ')' (',' '(' exps+=expression_list_ ')')*
    ;

expression_list_
    : exp+=expression (',' exp+=expression)*
    ;

// https://msdn.microsoft.com/en-us/library/ms189798.aspx
ranking_windowed_function
    : (RANK | DENSE_RANK | ROW_NUMBER) '(' ')' over_clause
    | NTILE '(' expression ')' over_clause
    ;

// https://msdn.microsoft.com/en-us/library/ms173454.aspx
aggregate_windowed_function
    : agg_func=(AVG | MAX | MIN | SUM | STDEV | STDEVP | VAR | VARP)
      '(' all_distinct_expression ')' over_clause?
    | cnt=(COUNT | COUNT_BIG)
      '(' ('*' | all_distinct_expression) ')' over_clause?
    | CHECKSUM_AGG '(' all_distinct_expression ')'
    | GROUPING '(' expression ')'
    | GROUPING_ID '(' expression_list_ ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/functions/analytic-functions-transact-sql
analytic_windowed_function
    : (FIRST_VALUE | LAST_VALUE) '(' expression ')' over_clause
    | (LAG | LEAD) '(' expression  (',' expression (',' expression)? )? ')' over_clause
    | (CUME_DIST | PERCENT_RANK) '(' ')' OVER '(' (PARTITION BY expression_list_)? order_by_clause ')'
    | (PERCENTILE_CONT | PERCENTILE_DISC) '(' expression ')' WITHIN GROUP '(' order_by_clause ')' OVER '(' (PARTITION BY expression_list_)? ')'
    ;

all_distinct_expression
    : (ALL | DISTINCT)? expression
    ;

// https://msdn.microsoft.com/en-us/library/ms189461.aspx
over_clause
    : OVER '(' (PARTITION BY expression_list_)? order_by_clause? row_or_range_clause? ')'
    ;

row_or_range_clause
    : (ROWS | RANGE) window_frame_extent
    ;

window_frame_extent
    : window_frame_preceding
    | BETWEEN window_frame_bound AND window_frame_bound
    ;

window_frame_bound
    : window_frame_preceding
    | window_frame_following
    ;

window_frame_preceding
    : UNBOUNDED PRECEDING
    | DECIMAL PRECEDING
    | CURRENT ROW
    ;

window_frame_following
    : UNBOUNDED FOLLOWING
    | DECIMAL FOLLOWING
    ;

create_database_option
    : FILESTREAM ( database_filestream_option (',' database_filestream_option)* )
    | DEFAULT_LANGUAGE EQUAL ( id_ | STRING )
    | DEFAULT_FULLTEXT_LANGUAGE EQUAL ( id_ | STRING )
    | NESTED_TRIGGERS EQUAL ( OFF | ON )
    | TRANSFORM_NOISE_WORDS EQUAL ( OFF | ON )
    | TWO_DIGIT_YEAR_CUTOFF EQUAL DECIMAL
    | DB_CHAINING ( OFF | ON )
    | TRUSTWORTHY ( OFF | ON )
    | LEDGER '=' on_off
    ;

database_filestream_option
    : LR_BRACKET
     (
         ( NON_TRANSACTED_ACCESS EQUAL ( OFF | READ_ONLY | FULL ) )
         |
         ( DIRECTORY_NAME EQUAL STRING )
     )
     RR_BRACKET
    ;

database_file_spec
    : file_group | file_spec
    ;

file_group
    : FILEGROUP id_
     ( CONTAINS FILESTREAM )?
     ( DEFAULT )?
     ( CONTAINS MEMORY_OPTIMIZED_DATA )?
     file_spec ( ',' file_spec )*
    ;
file_spec
    : LR_BRACKET
      NAME EQUAL ( id_ | STRING ) ','?
      FILENAME EQUAL file = STRING ','?
      ( SIZE EQUAL file_size ','? )?
      ( MAXSIZE EQUAL (file_size | UNLIMITED )','? )?
      ( FILEGROWTH EQUAL file_size ','? )?
      RR_BRACKET
    ;


// Primitive.
entity_name
    : (server=id_ '.' database=id_ '.'  schema=id_   '.'
    |                database=id_ '.' (schema=id_)? '.'
    |                                 schema=id_   '.')? table=id_
    ;


entity_name_for_azure_dw
    : schema=id_
    | schema=id_ '.' object_name=id_
    ;

entity_name_for_parallel_dw
    : schema_database=id_
    | schema=id_ '.' object_name=id_
    ;

full_table_name
    : (linkedServer=id_ '.' '.' schema=id_   '.'
    |                       server=id_    '.' database=id_ '.'  schema=id_   '.'
    |                                         database=id_ '.'  schema=id_? '.'
    |                                                           schema=id_    '.')? table=id_
    ;

table_name
    : (database=id_ '.' schema=id_? '.' | schema=id_ '.')? (table=id_ | blocking_hierarchy=BLOCKING_HIERARCHY)
    ;

simple_name
    : (schema=id_ '.')? name=id_
    ;

func_proc_name_schema
    : ((schema=id_) '.')? procedure=id_
    ;

func_proc_name_database_schema
    : database=id_? '.' schema=id_? '.' procedure=id_
    | func_proc_name_schema
    ;

func_proc_name_server_database_schema
    : server=id_? '.' database=id_? '.' schema=id_? '.' procedure=id_
    | func_proc_name_database_schema
    ;

ddl_object
    : full_table_name
    | LOCAL_ID
    ;

full_column_name
    : ((DELETED | INSERTED | full_table_name) '.')? (column_name=id_ | ('$' (IDENTITY | ROWGUID)))
    ;

column_name_list_with_order
    : id_ (ASC | DESC)? (',' id_ (ASC | DESC)?)*
    ;

//For some reason, sql server allows any number of prefixes:  Here, h is the column: a.b.c.d.e.f.g.h
insert_column_name_list
    : col+=insert_column_id (',' col+=insert_column_id)*
    ;

insert_column_id
    : (ignore+=id_? '.' )* id_
    ;

column_name_list
    : col+=id_ (',' col+=id_)*
    ;

cursor_name
    : id_
    | LOCAL_ID
    ;

on_off
    : ON
    | OFF
    ;

clustered
    : CLUSTERED
    | NONCLUSTERED
    ;

null_notnull
    : NOT? NULL_
    ;

scalar_function_name
    : func_proc_name_server_database_schema
    | RIGHT
    | LEFT
    | BINARY_CHECKSUM
    | CHECKSUM
    ;

begin_conversation_timer
    : BEGIN CONVERSATION TIMER '(' LOCAL_ID ')' TIMEOUT '=' time
    ;

begin_conversation_dialog
    : BEGIN DIALOG (CONVERSATION)? dialog_handle=LOCAL_ID
      FROM SERVICE initiator_service_name=service_name
      TO SERVICE target_service_name=service_name (',' service_broker_guid=STRING)?
      ON CONTRACT contract_name
      (WITH
        ((RELATED_CONVERSATION | RELATED_CONVERSATION_GROUP) '=' LOCAL_ID ','?)?
        (LIFETIME '=' (DECIMAL | LOCAL_ID) ','?)?
        (ENCRYPTION '=' on_off)? )?
    ;

contract_name
    : (id_ | expression)
    ;

service_name
    : (id_ | expression)
    ;

end_conversation
    : END CONVERSATION conversation_handle=LOCAL_ID
      (WITH (ERROR '=' (DECIMAL | LOCAL_ID) DESCRIPTION '=' (STRING | LOCAL_ID) | CLEANUP))?
    ;

waitfor_conversation
    : WAITFOR? '(' get_conversation ')' (','? TIMEOUT timeout=time)?
    ;

get_conversation
    :GET CONVERSATION GROUP conversation_group_id=(STRING | LOCAL_ID) FROM queue=queue_id
    ;

queue_id
    : (database_name=id_ '.' schema_name=id_ '.' name=id_)
    | id_
    ;

send_conversation
    : SEND ON CONVERSATION conversation_handle=(STRING | LOCAL_ID)
      MESSAGE TYPE message_type_name=expression
      ('(' message_body_expression=(STRING | LOCAL_ID) ')' )?
    ;

// https://msdn.microsoft.com/en-us/library/ms187752.aspx
// TODO: implement runtime check or add new tokens.

data_type
    : scaled=(VARCHAR | NVARCHAR | BINARY_KEYWORD | VARBINARY_KEYWORD | SQUARE_BRACKET_ID) '(' MAX ')'
    | ext_type=id_ '(' scale=DECIMAL ',' prec=DECIMAL ')'
    | ext_type=id_ '(' scale=DECIMAL ')'
    | ext_type=id_ IDENTITY ('(' seed=DECIMAL ',' inc=DECIMAL ')')?
    | double_prec=DOUBLE PRECISION?
    | unscaled_type=id_
    ;

// https://msdn.microsoft.com/en-us/library/ms179899.aspx
constant
    : STRING // string, datetime or uniqueidentifier
    | BINARY
    | '-'? (DECIMAL | REAL | FLOAT)     // float or decimal
    | '-'? dollar='$' ('-'|'+')? (DECIMAL | FLOAT) // money
    | parameter
    ;

// To reduce ambiguity, -X is considered as an application of unary operator
primitive_constant
    : STRING // string, datetime or uniqueidentifier
    | BINARY
    | (DECIMAL | REAL | FLOAT)          // float or decimal
    | dollar='$' ('-'|'+')? (DECIMAL | FLOAT) // money
    | parameter
    ;

keyword
    : NORESET
    | PARAM
    | STATEMENT
    | AUTOMATIC_TUNING
    | CE_FEEDBACK
    | CLEANUP_POLICY
    | CLEAR
    | CUSTOM
    | DATA_FLUSH_INTERVAL_SECONDS
    | DENSITY_VECTOR
    | DOP_FEEDBACK
    | EXECUTION_COUNT
    | FORCE_LAST_GOOD_PLAN
    | FREEPROCCACHE
    | FREESESSIONCACHE
    | FREESYSTEMCACHE
    | HINT
    | HISTOGRAM
    | INPUTBUFFER
    | INTERVAL_LENGTH_MINUTES
    | LAST_QUERY_PLAN_STATS
    | LIGHTWEIGHT_QUERY_PROFILING
    | LOGSPACE
    | MARK_IN_USE_FOR_REMOVAL
    | MAX_PLANS_PER_QUERY
    | MAX_STORAGE_SIZE_MB
    | MEMORYSTATUS
    | MEMORY_GRANT_FEEDBACK_PERCENTILE_GRANT
    | MEMORY_GRANT_FEEDBACK_PERSISTENCE
    | OPENTRAN
    | OPERATION_MODE
    | OPTIMIZED_PLAN_FORCING
    | OUTPUTBUFFER
    | PARAMETER_SENSITIVE_PLAN_OPTIMIZATION
    | PROCEDURE_CACHE
    | QUERY_CAPTURE_MODE
    | QUERY_CAPTURE_POLICY
    | QUERY_STORE
    | SHOW_STATISTICS
    | SIZE_BASED_CLEANUP_MODE
    | SQLPERF
    | STALE_CAPTURE_POLICY_THRESHOLD
    | STALE_QUERY_THRESHOLD_DAYS
    | STAT_HEADER
    | TOTAL_COMPILE_CPU_TIME_MS
    | TOTAL_EXECUTION_CPU_TIME_MS
    | TRACEOFF
    | TRACEON
    | TRACESTATUS
    | USEROPTIONS
    | WAIT_STATS_CAPTURE_MODE
    | TRACK_COLUMNS_UPDATED
    | READ_WRITE_ROUTING_URL
    | READ_ONLY_ROUTING_URL
    | CONFIGURATION_ONLY
    | WSFC
    | CLUSTER_TYPE
    | REUSE_SYSTEM_DATABASES
    | CONTAINED
    | BASIC
    | SECURED
    | CRYPTOGRAPHICALLY
    | PERFORMANCE
    | ENCLAVE_COMPUTATIONS
    | WHEN_SUPPORTED
    | VERIFYONLY
    | VERBOSE_TRUNCATION_WARNINGS
    | UPDATEUSAGE
    | UNLOAD
    | TRUNCATEONLY
    | TEMPORAL_HISTORY_RETENTION
    | TEMPDB_METADATA
    | SUSPEND_FOR_SNAPSHOT_BACKUP
    | STOPBEFOREMARK
    | STOPATMARK
    | STOPAT
    | SHRINKFILE
    | SHRINKDATABASE
    | RESEED
    | QUERY_OPTIMIZER_HOTFIXES
    | PARAMETER_SNIFFING
    | NOTRUNCATE
    | NORESEED
    | METADATA_ONLY
    | MEMORY_OPTIMIZED_ELEVATE_TO_SNAPSHOT
    | MEMORY_OPTIMIZED
    | MEDIAPASSWORD
    | MAXTRANSFERSIZE
    | LOADHISTORY
    | LEGACY_CARDINALITY_ESTIMATION
    | LEDGER
    | LABELONLY
    | INDEXDEFRAG
    | IDENTITY_CACHE
    | HYBRID_BUFFER_POOL
    | HEADERONLY
    | HARDWARE_OFFLOAD
    | FILELISTONLY
    | FAIL_UNSUPPORTED
    | EMPTYFILE
    | ELEVATE_RESUMABLE
    | ELEVATE_ONLINE
    | DBNAME
    | DATABASE_SNAPSHOT
    | COUNT_ROWS
    | CHECKIDENT
    | ATTACH_REBUILD_LOG
    | ATTACH
    | ASYNC_STATS_UPDATE_WAIT_AT_LOW_PRIORITY
    | ABORT
    | ABSOLUTE
    | ACCENT_SENSITIVITY
    | ACCESS
    | ACTION
    | ACTIVATION
    | ACTIVE
    | ADD   // ?
    | ADDRESS
    | AES_128
    | AES_192
    | AES_256
    | AFFINITY
    | AFTER
    | AGGREGATE
    | ALGORITHM
    | ALL_CONSTRAINTS
    | ALL_ERRORMSGS
    | ALL_INDEXES
    | ALL_LEVELS
    | ALLOW_ENCRYPTED_VALUE_MODIFICATIONS
    | ALLOW_PAGE_LOCKS
    | ALLOW_ROW_LOCKS
    | ALLOW_SNAPSHOT_ISOLATION
    | ALLOWED
    | ALWAYS
    | ANSI_DEFAULTS
    | ANSI_NULL_DEFAULT
    | ANSI_NULL_DFLT_OFF
    | ANSI_NULL_DFLT_ON
    | ANSI_NULLS
    | ANSI_PADDING
    | ANSI_WARNINGS
    | APP_NAME
    | APPLICATION_LOG
    | APPLOCK_MODE
    | APPLOCK_TEST
    | APPLY
    | ARITHABORT
    | ARITHIGNORE
    | ASCII
    | ASSEMBLY
    | ASSEMBLYPROPERTY
    | AT_KEYWORD
    | AUDIT
    | AUDIT_GUID
    | AUTO
    | AUTO_CLEANUP
    | AUTO_CLOSE
    | AUTO_CREATE_STATISTICS
    | AUTO_DROP
    | AUTO_SHRINK
    | AUTO_UPDATE_STATISTICS
    | AUTO_UPDATE_STATISTICS_ASYNC
    | AUTOGROW_ALL_FILES
    | AUTOGROW_SINGLE_FILE
    | AVAILABILITY
    | AVG
    | BACKUP_CLONEDB
    | BACKUP_PRIORITY
    | BASE64
    | BEGIN_DIALOG
    | BIGINT
    | BINARY_KEYWORD
    | BINARY_CHECKSUM
    | BINDING
    | BLOB_STORAGE
    | BROKER
    | BROKER_INSTANCE
    | BULK_LOGGED
    | CALLER
    | CAP_CPU_PERCENT
    | CAST
    | TRY_CAST
    | CATALOG
    | CATCH
    | CERT_ID
    | CERTENCODED
    | CERTPRIVATEKEY
    | CHANGE
    | CHANGE_RETENTION
    | CHANGE_TRACKING
    | CHAR
    | CHARINDEX
    | CHECKALLOC
    | CHECKCATALOG
    | CHECKCONSTRAINTS
    | CHECKDB
    | CHECKFILEGROUP
    | CHECKSUM
    | CHECKSUM_AGG
    | CHECKTABLE
    | CLEANTABLE
    | CLEANUP
    | CLONEDATABASE
    | COL_LENGTH
    | COL_NAME
    | COLLECTION
    | COLUMN_ENCRYPTION_KEY
    | COLUMN_MASTER_KEY
    | COLUMNPROPERTY
    | COLUMNS
    | COLUMNSTORE
    | COLUMNSTORE_ARCHIVE
    | COMMITTED
    | COMPATIBILITY_LEVEL
    | COMPRESS_ALL_ROW_GROUPS
    | COMPRESSION_DELAY
    | CONCAT
    | CONCAT_WS
    | CONCAT_NULL_YIELDS_NULL
    | CONTENT
    | CONTROL
    | COOKIE
    | COUNT
    | COUNT_BIG
    | COUNTER
    | CPU
    | CREATE_NEW
    | CREATION_DISPOSITION
    | CREDENTIAL
    | CRYPTOGRAPHIC
    | CUME_DIST
    | CURSOR_CLOSE_ON_COMMIT
    | CURSOR_DEFAULT
    | CURSOR_STATUS
    | DATA
    | DATA_PURITY
    | DATABASE_PRINCIPAL_ID
    | DATABASEPROPERTYEX
    | DATALENGTH
    | DATE_CORRELATION_OPTIMIZATION
    | DATEADD
    | DATEDIFF
    | DATENAME
    | DATEPART
    | DAYS
    | DB_CHAINING
    | DB_FAILOVER
    | DB_ID
    | DB_NAME
    | DBCC
    | DBREINDEX
    | DECRYPTION
    | DEFAULT_DOUBLE_QUOTE
    | DEFAULT_FULLTEXT_LANGUAGE
    | DEFAULT_LANGUAGE
    | DEFINITION
    | DELAY
    | DELAYED_DURABILITY
    | DELETED
    | DENSE_RANK
    | DEPENDENTS
    | DES
    | DESCRIPTION
    | DESX
    | DETERMINISTIC
    | DHCP
    | DIALOG
    | DIFFERENCE
    | DIRECTORY_NAME
    | DISABLE
    | DISABLE_BROKER
    | DISABLED
    | DOCUMENT
    | DROP_EXISTING
    | DROPCLEANBUFFERS
    | DYNAMIC
    | ELEMENTS
    | EMERGENCY
    | EMPTY
    | ENABLE
    | ENABLE_BROKER
    | ENCRYPTED
    | ENCRYPTED_VALUE
    | ENCRYPTION
    | ENCRYPTION_TYPE
    | ENDPOINT_URL
    | ERROR_BROKER_CONVERSATIONS
    | ESTIMATEONLY
    | EXCLUSIVE
    | EXECUTABLE
    | EXIST
    | EXIST_SQUARE_BRACKET
    | EXPAND
    | EXPIRY_DATE
    | EXPLICIT
    | EXTENDED_LOGICAL_CHECKS
    | FAIL_OPERATION
    | FAILOVER_MODE
    | FAILURE
    | FAILURE_CONDITION_LEVEL
    | FAST
    | FAST_FORWARD
    | FILE_ID
    | FILE_IDEX
    | FILE_NAME
    | FILEGROUP
    | FILEGROUP_ID
    | FILEGROUP_NAME
    | FILEGROUPPROPERTY
    | FILEGROWTH
    | FILENAME
    | FILEPATH
    | FILEPROPERTY
    | FILEPROPERTYEX
    | FILESTREAM
    | FILTER
    | FIRST
    | FIRST_VALUE
    | FMTONLY
    | FOLLOWING
    | FORCE
    | FORCE_FAILOVER_ALLOW_DATA_LOSS
    | FORCED
    | FORCEPLAN
    | FORCESCAN
    | FORMAT
    | FORWARD_ONLY
    | FREE
    | FULLSCAN
    | FULLTEXT
    | FULLTEXTCATALOGPROPERTY
    | FULLTEXTSERVICEPROPERTY
    | GB
    | GENERATED
    | GETDATE
    | GETUTCDATE
    | GLOBAL
//    | GO
    | GREATEST
    | GROUP_MAX_REQUESTS
    | GROUPING
    | GROUPING_ID
    | HADR
    | HAS_DBACCESS
    | HAS_PERMS_BY_NAME
    | HASH
    | HEALTH_CHECK_TIMEOUT
    | HIDDEN_KEYWORD
    | HIGH
    | HONOR_BROKER_PRIORITY
    | HOURS
    | IDENT_CURRENT
    | IDENT_INCR
    | IDENT_SEED
    | IDENTITY_VALUE
    | IGNORE_CONSTRAINTS
    | IGNORE_DUP_KEY
    | IGNORE_NONCLUSTERED_COLUMNSTORE_INDEX
    | IGNORE_REPLICATED_TABLE_CACHE
    | IGNORE_TRIGGERS
    | IMMEDIATE
    | IMPERSONATE
    | IMPLICIT_TRANSACTIONS
    | IMPORTANCE
    | INCLUDE_NULL_VALUES
    | INCREMENTAL
    | INDEX_COL
    | INDEXKEY_PROPERTY
    | INDEXPROPERTY
    | INITIATOR
    | INPUT
    | INSENSITIVE
    | INSERTED
    | INT
    | IP
    | IS_MEMBER
    | IS_ROLEMEMBER
    | IS_SRVROLEMEMBER
    | ISJSON
    | ISOLATION
    | JOB
    | JSON
    | JSON_OBJECT
    | JSON_ARRAY
    | JSON_VALUE
    | JSON_QUERY
    | JSON_MODIFY
    | JSON_PATH_EXISTS
    | KB
    | KEEP
    | KEEPDEFAULTS
    | KEEPFIXED
    | KEEPIDENTITY
    | KEY_SOURCE
    | KEYS
    | KEYSET
    | LAG
    | LAST
    | LAST_VALUE
    | LEAD
    | LEAST
    | LEN
    | LEVEL
    | LIST
    | LISTENER
    | LISTENER_URL
    | LOB_COMPACTION
    | LOCAL
    | LOCATION
    | LOCK
    | LOCK_ESCALATION
    | LOGIN
    | LOGINPROPERTY
    | LOOP
    | LOW
    | LOWER
    | LTRIM
    | MANUAL
    | MARK
    | MASKED
    | MATERIALIZED
    | MAX
    | MAX_CPU_PERCENT
    | MAX_DOP
    | MAX_FILES
    | MAX_IOPS_PER_VOLUME
    | MAX_MEMORY_PERCENT
    | MAX_PROCESSES
    | MAX_QUEUE_READERS
    | MAX_ROLLOVER_FILES
    | MAXDOP
    | MAXRECURSION
    | MAXSIZE
    | MB
    | MEDIUM
    | MEMORY_OPTIMIZED_DATA
    | MESSAGE
    | MIN
    | MIN_ACTIVE_ROWVERSION
    | MIN_CPU_PERCENT
    | MIN_IOPS_PER_VOLUME
    | MIN_MEMORY_PERCENT
    | MINUTES
    | MIRROR_ADDRESS
    | MIXED_PAGE_ALLOCATION
    | MODE
    | MODIFY
    | MODIFY_SQUARE_BRACKET
    | MOVE
    | MULTI_USER
    | NAME
    | NCHAR
    | NESTED_TRIGGERS
    | NEW_ACCOUNT
    | NEW_BROKER
    | NEW_PASSWORD
    | NEWNAME
    | NEXT
    | NO
    | NO_INFOMSGS
    | NO_QUERYSTORE
    | NO_STATISTICS
    | NO_TRUNCATE
    | NO_WAIT
    | NOCOUNT
    | NODES
    | NOEXEC
    | NOEXPAND
    | NOINDEX
    | NOLOCK
    | NON_TRANSACTED_ACCESS
    | NORECOMPUTE
    | NORECOVERY
    | NOTIFICATIONS
    | NOWAIT
    | NTILE
    | NULL_DOUBLE_QUOTE
    | NUMANODE
    | NUMBER
    | NUMERIC_ROUNDABORT
    | OBJECT
    | OBJECT_DEFINITION
    | OBJECT_ID
    | OBJECT_NAME
    | OBJECT_SCHEMA_NAME
    | OBJECTPROPERTY
    | OBJECTPROPERTYEX
    | OFFLINE
    | OFFSET
    | OLD_ACCOUNT
    | ONLINE
    | ONLY
    | OPEN_EXISTING
    | OPENJSON
    | OPTIMISTIC
    | OPTIMIZE
    | OPTIMIZE_FOR_SEQUENTIAL_KEY
    | ORIGINAL_DB_NAME
    | ORIGINAL_LOGIN
    | OUT
    | OUTPUT
    | OVERRIDE
    | OWNER
    | OWNERSHIP
    | PAD_INDEX
    | PAGE_VERIFY
    | PAGECOUNT
    | PAGLOCK
    | PARAMETERIZATION
    | PARSENAME
    | PARSEONLY
    | PARTITION
    | PARTITIONS
    | PARTNER
    | PATH
    | PATINDEX
    | PAUSE
    | PDW_SHOWSPACEUSED
    | PERCENT_RANK
    | PERCENTILE_CONT
    | PERCENTILE_DISC
    | PERMISSIONS
    | PERSIST_SAMPLE_PERCENT
    | PHYSICAL_ONLY
    | POISON_MESSAGE_HANDLING
    | POOL
    | PORT
    | PRECEDING
    | PRIMARY_ROLE
    | PRIOR
    | PRIORITY
    | PRIORITY_LEVEL
    | PRIVATE
    | PRIVATE_KEY
    | PRIVILEGES
    | PROCCACHE
    | PROCEDURE_NAME
    | PROPERTY
    | PROVIDER
    | PROVIDER_KEY_NAME
    | PWDCOMPARE
    | PWDENCRYPT
    | QUERY
    | QUERY_SQUARE_BRACKET
    | QUEUE
    | QUEUE_DELAY
    | QUOTED_IDENTIFIER
    | QUOTENAME
    | RANDOMIZED
    | RANGE
    | RANK
    | RC2
    | RC4
    | RC4_128
    | READ_COMMITTED_SNAPSHOT
    | READ_ONLY
    | READ_ONLY_ROUTING_LIST
    | READ_WRITE
    | READCOMMITTED
    | READCOMMITTEDLOCK
    | READONLY
    | READPAST
    | READUNCOMMITTED
    | READWRITE
    | REBUILD
    | RECEIVE
    | RECOMPILE
    | RECOVERY
    | RECURSIVE_TRIGGERS
    | RELATIVE
    | REMOTE
    | REMOTE_PROC_TRANSACTIONS
    | REMOTE_SERVICE_NAME
    | REMOVE
    | REORGANIZE
    | REPAIR_ALLOW_DATA_LOSS
    | REPAIR_FAST
    | REPAIR_REBUILD
    | REPEATABLE
    | REPEATABLEREAD
    | REPLACE
    | REPLICA
    | REPLICATE
    | REQUEST_MAX_CPU_TIME_SEC
    | REQUEST_MAX_MEMORY_GRANT_PERCENT
    | REQUEST_MEMORY_GRANT_TIMEOUT_SEC
    | REQUIRED_SYNCHRONIZED_SECONDARIES_TO_COMMIT
    | RESAMPLE
    | RESERVE_DISK_SPACE
    | RESOURCE
    | RESOURCE_MANAGER_LOCATION
    | RESTRICTED_USER
    | RESUMABLE
    | RETENTION
    | REVERSE
    | ROBUST
    | ROOT
    | ROUTE
    | ROW
    | ROW_NUMBER
    | ROWGUID
    | ROWLOCK
    | ROWS
    | RTRIM
    | SAMPLE
    | SCHEMA_ID
    | SCHEMA_NAME
    | SCHEMABINDING
    | SCOPE_IDENTITY
    | SCOPED
    | SCROLL
    | SCROLL_LOCKS
    | SEARCH
    | SECONDARY
    | SECONDARY_ONLY
    | SECONDARY_ROLE
    | SECONDS
    | SECRET
    | SECURABLES
    | SECURITY
    | SECURITY_LOG
    | SEEDING_MODE
    | SELF
    | SEMI_SENSITIVE
    | SEND
    | SENT
    | SEQUENCE
    | SEQUENCE_NUMBER
    | SERIALIZABLE
    | SERVERPROPERTY
    | SERVICEBROKER
    | SESSIONPROPERTY
    | SESSION_TIMEOUT
    | SETERROR
    | SHARE
    | SHARED
    | SHOWCONTIG
    | SHOWPLAN
    | SHOWPLAN_ALL
    | SHOWPLAN_TEXT
    | SHOWPLAN_XML
    | SIGNATURE
    | SIMPLE
    | SINGLE_USER
    | SIZE
    | SMALLINT
    | SNAPSHOT
    | SORT_IN_TEMPDB
    | SOUNDEX
    | SPACE_KEYWORD
    | SPARSE
    | SPATIAL_WINDOW_MAX_CELLS
    | SQL_VARIANT_PROPERTY
    | STANDBY
    | START_DATE
    | STATIC
    | STATISTICS_INCREMENTAL
    | STATISTICS_NORECOMPUTE
    | STATS_DATE
    | STATS_STREAM
    | STATUS
    | STATUSONLY
    | STDEV
    | STDEVP
    | STOPLIST
    | STR
    | STRING_AGG
    | STRING_ESCAPE
    | STUFF
    | SUBJECT
    | SUBSCRIBE
    | SUBSCRIPTION
    | SUBSTRING
    | SUM
    | SUSER_ID
    | SUSER_NAME
    | SUSER_SID
    | SUSER_SNAME
    | SUSPEND
    | SYMMETRIC
    | SYNCHRONOUS_COMMIT
    | SYNONYM
    | SYSTEM
    | TABLERESULTS
    | TABLOCK
    | TABLOCKX
    | TAKE
    | TARGET_RECOVERY_TIME
    | TB
    | TEXTIMAGE_ON
    | THROW
    | TIES
    | TIME
    | TIMEOUT
    | TIMER
    | TINYINT
    | TORN_PAGE_DETECTION
    | TRACKING
    | TRANSACTION_ID
    | TRANSFORM_NOISE_WORDS
    | TRANSLATE
    | TRIM
    | TRIPLE_DES
    | TRIPLE_DES_3KEY
    | TRUSTWORTHY
    | TRY
    | TSQL
    | TWO_DIGIT_YEAR_CUTOFF
    | TYPE
    | TYPE_ID
    | TYPE_NAME
    | TYPE_WARNING
    | TYPEPROPERTY
    | UNBOUNDED
    | UNCOMMITTED
    | UNICODE
    | UNKNOWN
    | UNLIMITED
    | UNMASK
    | UOW
    | UPDLOCK
    | UPPER
    | USER_ID
    | USER_NAME
    | USING
    | VALID_XML
    | VALIDATION
    | VALUE
    | VALUE_SQUARE_BRACKET
    | VAR
    | VARBINARY_KEYWORD
    | VARP
    | VERIFY_CLONEDB
    | VERSION
    | VIEW_METADATA
    | VIEWS
    | WAIT
    | WELL_FORMED_XML
    | WITHOUT_ARRAY_WRAPPER
    | WORK
    | WORKLOAD
    | XLOCK
    | XML
    | XML_COMPRESSION
    | XMLDATA
    | XMLNAMESPACES
    | XMLSCHEMA
    | XSINIL
    | ZONE
//More keywords that can also be used as IDs
    | ABORT_AFTER_WAIT
    | ABSENT
    | ADMINISTER
    | AES
    | ALLOW_CONNECTIONS
    | ALLOW_MULTIPLE_EVENT_LOSS
    | ALLOW_SINGLE_EVENT_LOSS
    | ANONYMOUS
    | APPEND
    | APPLICATION
    | ASYMMETRIC
    | ASYNCHRONOUS_COMMIT
    | AUTHENTICATE
    | AUTHENTICATION
    | AUTOMATED_BACKUP_PREFERENCE
    | AUTOMATIC
    | AVAILABILITY_MODE
    | BEFORE
    | BLOCK
    | BLOCKERS
    | BLOCKSIZE
    | BLOCKING_HIERARCHY
    | BUFFER
    | BUFFERCOUNT
    | CACHE
    | CALLED
    | CERTIFICATE
    | CHANGETABLE
    | CHANGES
    | CHECK_POLICY
    | CHECK_EXPIRATION
    | CLASSIFIER_FUNCTION
    | CLUSTER
    | COMPRESS
    | COMPRESSION
    | CONNECT
    | CONNECTION
    | CONFIGURATION
    | CONNECTIONPROPERTY
    | CONTAINMENT
    | CONTEXT
    | CONTEXT_INFO
    | CONTINUE_AFTER_ERROR
    | CONTRACT
    | CONTRACT_NAME
    | CONVERSATION
    | COPY_ONLY
    | CURRENT_REQUEST_ID
    | CURRENT_TRANSACTION_ID
    | CYCLE
    | DATA_COMPRESSION
    | DATA_SOURCE
    | DATABASE_MIRRORING
    | DATASPACE
    | DDL
    | DECOMPRESS
    | DEFAULT_DATABASE
    | DEFAULT_SCHEMA
    | DIAGNOSTICS
    | DIFFERENTIAL
    | DISTRIBUTION
    | DTC_SUPPORT
    | ENABLED
    | ENDPOINT
    | ERROR
    | ERROR_LINE
    | ERROR_MESSAGE
    | ERROR_NUMBER
    | ERROR_PROCEDURE
    | ERROR_SEVERITY
    | ERROR_STATE
    | EVENT
    | EVENTDATA
    | EVENT_RETENTION_MODE
    | EXECUTABLE_FILE
    | EXPIREDATE
    | EXTENSION
    | EXTERNAL_ACCESS
    | FAILOVER
    | FAILURECONDITIONLEVEL
    | FAN_IN
    | FILE_SNAPSHOT
    | FORCESEEK
    | FORCE_SERVICE_ALLOW_DATA_LOSS
    | FORMATMESSAGE
    | GET
    | GET_FILESTREAM_TRANSACTION_CONTEXT
    | GETANCESTOR
    | GETANSINULL
    | GETDESCENDANT
    | GETLEVEL
    | GETREPARENTEDVALUE
    | GETROOT
    | GOVERNOR
    | HASHED
    | HEALTHCHECKTIMEOUT
    | HEAP
    | HIERARCHYID
    | HOST_ID
    | HOST_NAME
    | IIF
    | IO
    | INCLUDE
    | INCREMENT
    | INFINITE
    | INIT
    | INSTEAD
    | ISDESCENDANTOF
    | ISNULL
    | ISNUMERIC
    | KERBEROS
    | KEY_PATH
    | KEY_STORE_PROVIDER_NAME
    | LANGUAGE
    | LIBRARY
    | LIFETIME
    | LINKED
    | LINUX
    | LISTENER_IP
    | LISTENER_PORT
    | LOCAL_SERVICE_NAME
    | LOG
    | MASK
    | MATCHED
    | MASTER
    | MAX_MEMORY
    | MAXTRANSFER
    | MAXVALUE
    | MAX_DISPATCH_LATENCY
    | MAX_DURATION
    | MAX_EVENT_SIZE
    | MAX_SIZE
    | MAX_OUTSTANDING_IO_PER_VOLUME
    | MEDIADESCRIPTION
    | MEDIANAME
    | MEMBER
    | MEMORY_PARTITION_MODE
    | MESSAGE_FORWARDING
    | MESSAGE_FORWARD_SIZE
    | MINVALUE
    | MIRROR
    | MUST_CHANGE
    | NEWID
    | NEWSEQUENTIALID
    | NOFORMAT
    | NOINIT
    | NONE
    | NOREWIND
    | NOSKIP
    | NOUNLOAD
    | NO_CHECKSUM
    | NO_COMPRESSION
    | NO_EVENT_LOSS
    | NOTIFICATION
    | NTLM
    | OLD_PASSWORD
    | ON_FAILURE
    | OPERATIONS
    | PAGE
    | PARAM_NODE
    | PARTIAL
    | PASSWORD
    | PERMISSION_SET
    | PER_CPU
    | PER_DB
    | PER_NODE
    | PERSISTED
    | PLATFORM
    | POLICY
    | PREDICATE
    | PROCESS
    | PROFILE
    | PYTHON
    | R_1
    | READ_WRITE_FILEGROUPS
    | REGENERATE
    | RELATED_CONVERSATION
    | RELATED_CONVERSATION_GROUP
    | REQUIRED
    | RESET
    | RESOURCES
    | RESTART
    | RESUME
    | RETAINDAYS
    | RETURNS
    | REWIND
    | ROLE
    | ROUND_ROBIN
    | ROWCOUNT_BIG
    | RSA_512
    | RSA_1024
    | RSA_2048
    | RSA_3072
    | RSA_4096
    | SAFETY
    | SAFE
    | SCHEDULER
    | SCHEME
    | SCRIPT
    | SERVER
    | SERVICE
    | SERVICE_BROKER
    | SERVICE_NAME
    | SESSION
    | SESSION_CONTEXT
    | SETTINGS
    | SHRINKLOG
    | SID
    | SKIP_KEYWORD
    | SOFTNUMA
    | SOURCE
    | SPECIFICATION
    | SPLIT
    | SQL
    | SQLDUMPERFLAGS
    | SQLDUMPERPATH
    | SQLDUMPERTIMEOUT
    | STATE
    | STATS
    | START
    | STARTED
    | STARTUP_STATE
    | STOP
    | STOPPED
    | STOP_ON_ERROR
    | SUPPORTED
    | SWITCH
    | TAPE
    | TARGET
    | TCP
    | TOSTRING
    | TRACE
    | TRACK_CAUSALITY
    | TRANSFER
    | UNCHECKED
    | UNLOCK
    | UNSAFE
    | URL
    | USED
    | VERBOSELOGGING
    | VISIBILITY
    | WAIT_AT_LOW_PRIORITY
    | WINDOWS
    | WITHOUT
    | WITNESS
    | XACT_ABORT
    | XACT_STATE
    //
    | ABS
    | ACOS
    | ASIN
    | ATAN
    | ATN2
    | CEILING
    | COS
    | COT
    | DEGREES
    | EXP
    | FLOOR
    | LOG10
    | PI
    | POWER
    | RADIANS
    | RAND
    | ROUND
    | SIGN
    | SIN
    | SQRT
    | SQUARE
    | TAN
    //
    | CURRENT_TIMEZONE
    | CURRENT_TIMEZONE_ID
    | DATE_BUCKET
    | DATEDIFF_BIG
    | DATEFROMPARTS
    | DATETIME2FROMPARTS
    | DATETIMEFROMPARTS
    | DATETIMEOFFSETFROMPARTS
    | DATETRUNC
    | DAY
    | EOMONTH
    | ISDATE
    | MONTH
    | SMALLDATETIMEFROMPARTS
    | SWITCHOFFSET
    | SYSDATETIME
    | SYSDATETIMEOFFSET
    | SYSUTCDATETIME
    | TIMEFROMPARTS
    | TODATETIMEOFFSET
    | YEAR
    //
    | QUARTER
    | DAYOFYEAR
    | WEEK
    | HOUR
    | MINUTE
    | SECOND
    | MILLISECOND
    | MICROSECOND
    | NANOSECOND
    | TZOFFSET
    | ISO_WEEK
    | WEEKDAY
    //
    | YEAR_ABBR
    | QUARTER_ABBR
    | MONTH_ABBR
    | DAYOFYEAR_ABBR
    | DAY_ABBR
    | WEEK_ABBR
    | HOUR_ABBR
    | MINUTE_ABBR
    | SECOND_ABBR
    | MILLISECOND_ABBR
    | MICROSECOND_ABBR
    | NANOSECOND_ABBR
    | TZOFFSET_ABBR
    | ISO_WEEK_ABBR
    | WEEKDAY_ABBR
    //
    | SP_EXECUTESQL
    //Build-ins:
    | VARCHAR
    | NVARCHAR
    | PRECISION //For some reason this is possible to use as ID
    | FILESTREAM_ON
    ;

// https://msdn.microsoft.com/en-us/library/ms175874.aspx
id_
    : ID
    | TEMP_ID
    | DOUBLE_QUOTE_ID
    | DOUBLE_QUOTE_BLANK
    | SQUARE_BRACKET_ID
    | keyword
    | RAW
    ;

simple_id
    : ID
    ;

id_or_string
    : id_
    | STRING
    ;

// https://msdn.microsoft.com/en-us/library/ms188074.aspx
// Spaces are allowed for comparison operators.
comparison_operator
    : '=' | '>' | '<' | '<' '=' | '>' '=' | '<' '>' | '!' '=' | '!' '>' | '!' '<'
    ;

assignment_operator
    : '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '^=' | '|='
    ;

file_size
    : DECIMAL( KB | MB | GB | TB | '%' )?
    ;

// SQL Server database and storage administration (2022 baseline).
database_attach_file
    : '(' FILENAME '=' STRING ')'
    ;

database_snapshot_file
    : '(' NAME '=' id_or_string ',' FILENAME '=' STRING ')'
    ;

alter_database_scoped_configuration
    : ALTER DATABASE SCOPED CONFIGURATION ((FOR SECONDARY)? SET database_scoped_option
                                           | CLEAR PROCEDURE_CACHE BINARY?)
    ;

database_scoped_option
    : MAXDOP '=' (DECIMAL | PRIMARY)
    | (LEGACY_CARDINALITY_ESTIMATION | PARAMETER_SNIFFING | QUERY_OPTIMIZER_HOTFIXES) '=' (on_off | PRIMARY)
    | (IDENTITY_CACHE | VERBOSE_TRUNCATION_WARNINGS | ASYNC_STATS_UPDATE_WAIT_AT_LOW_PRIORITY
       | LIGHTWEIGHT_QUERY_PROFILING | LAST_QUERY_PLAN_STATS | PARAMETER_SENSITIVE_PLAN_OPTIMIZATION
       | CE_FEEDBACK | DOP_FEEDBACK | MEMORY_GRANT_FEEDBACK_PERSISTENCE
       | MEMORY_GRANT_FEEDBACK_PERCENTILE_GRANT | OPTIMIZED_PLAN_FORCING) '=' on_off
    | (ELEVATE_ONLINE | ELEVATE_RESUMABLE) '=' (OFF | WHEN_SUPPORTED | FAIL_UNSUPPORTED)
    ;

create_statistics_option
    : FULLSCAN (','? PERSIST_SAMPLE_PERCENT '=' on_off)?
    | SAMPLE DECIMAL (PERCENT | ROWS) (','? PERSIST_SAMPLE_PERCENT '=' on_off)?
    | STATS_STREAM '=' BINARY
    | (ROWCOUNT | PAGECOUNT) '=' DECIMAL
    | NORECOMPUTE
    | INCREMENTAL '=' on_off
    | MAXDOP '=' DECIMAL
    | AUTO_DROP '=' on_off
    ;

alter_resource_pool
    : ALTER RESOURCE POOL (pool_name=id_ | DEFAULT_DOUBLE_QUOTE)
      (WITH '(' resource_pool_option (','? resource_pool_option)* ')')?
    ;

resource_pool_option
    : (MIN_CPU_PERCENT | MAX_CPU_PERCENT | CAP_CPU_PERCENT | MIN_MEMORY_PERCENT | MAX_MEMORY_PERCENT
       | MIN_IOPS_PER_VOLUME | MAX_IOPS_PER_VOLUME) '=' DECIMAL
    | AFFINITY (SCHEDULER '=' (AUTO | '(' resource_affinity_range (',' resource_affinity_range)* ')')
               | NUMANODE '=' '(' resource_affinity_range (',' resource_affinity_range)* ')')
    ;

resource_affinity_range
    : DECIMAL (TO DECIMAL)?
    ;

external_resource_pool_option
    : (MAX_CPU_PERCENT | MAX_MEMORY_PERCENT | MAX_PROCESSES) '=' DECIMAL
    | AFFINITY (CPU '=' (AUTO | '(' resource_affinity_range (',' resource_affinity_range)* ')')
               | NUMANODE '=' '(' resource_affinity_range (',' resource_affinity_range)* ')')
    ;

workload_group_option
    : IMPORTANCE '=' (LOW | MEDIUM | HIGH)
    | REQUEST_MAX_MEMORY_GRANT_PERCENT '=' (DECIMAL | FLOAT)
    | (REQUEST_MAX_CPU_TIME_SEC | REQUEST_MEMORY_GRANT_TIMEOUT_SEC | MAX_DOP | GROUP_MAX_REQUESTS) '=' DECIMAL
    ;

// https://learn.microsoft.com/sql/t-sql/statements/backup-transact-sql
backup_file_item
    : (FILE | FILEGROUP) '=' (STRING | LOCAL_ID)
    ;

backup_device
    : id_
    | LOCAL_ID
    | (DISK | TAPE | URL) '=' (STRING | LOCAL_ID)
    ;

backup_mirror
    : MIRROR TO backup_device (',' backup_device)*
    ;

backup_database_option
    : backup_common_option
    | DIFFERENTIAL
    | FILE_SNAPSHOT
    | METADATA_ONLY
    | SNAPSHOT
    ;

backup_log_option
    : backup_common_option
    | NORECOVERY
    | STANDBY '=' (STRING | LOCAL_ID)
    | NO_TRUNCATE
    ;

backup_common_option
    : COPY_ONLY
    | COMPRESSION
    | NO_COMPRESSION
    | (DESCRIPTION | NAME | CREDENTIAL | EXPIREDATE | MEDIADESCRIPTION | MEDIANAME) '=' (STRING | LOCAL_ID)
    | (RETAINDAYS | BLOCKSIZE | BUFFERCOUNT | MAXTRANSFERSIZE) '=' (DECIMAL | LOCAL_ID)
    | INIT | NOINIT | SKIP_KEYWORD | NOSKIP | FORMAT | NOFORMAT
    | CHECKSUM | NO_CHECKSUM | STOP_ON_ERROR | CONTINUE_AFTER_ERROR
    | RESTART
    | STATS ('=' DECIMAL)?
    | REWIND | NOREWIND | UNLOAD | NOUNLOAD
    | ENCRYPTION '(' ALGORITHM '=' (AES_128 | AES_192 | AES_256 | TRIPLE_DES_3KEY)
      ',' SERVER (CERTIFICATE | ASYMMETRIC KEY) '=' id_ ')'
    ;

backup_snapshot
    : BACKUP (SERVER | GROUP id_ (',' id_)*)
      TO backup_device (',' backup_device)* backup_mirror*
      WITH backup_database_option (',' backup_database_option)*
    ;

// https://learn.microsoft.com/sql/t-sql/statements/restore-statements-transact-sql
restore_statement
    : restore_database
    | restore_log
    | restore_metadata
    | restore_verify
    | restore_master_key
    | restore_service_master_key
    ;

restore_database
    : RESTORE DATABASE (database_name=id_ | database_variable=LOCAL_ID)
      (FROM DATABASE_SNAPSHOT '=' (STRING | LOCAL_ID)
      | restore_file_selection?
        (FROM backup_device (',' backup_device)*)?
        (WITH restore_database_option (',' restore_database_option)*)?
      )
    ;

restore_log
    : RESTORE LOG (database_name=id_ | database_variable=LOCAL_ID)
      restore_file_selection?
      (FROM backup_device (',' backup_device)*)?
      (WITH restore_log_option (',' restore_log_option)*)?
    ;

restore_file_selection
    : (backup_file_item | READ_WRITE_FILEGROUPS) (',' backup_file_item)*
    | PAGE '=' (STRING | LOCAL_ID)
    ;

restore_database_option
    : restore_common_option
    | PARTIAL
    | METADATA_ONLY
    | SNAPSHOT
    | DBNAME '=' (STRING | LOCAL_ID)
    ;

restore_log_option
    : restore_common_option
    | STOPAT '=' (STRING | LOCAL_ID)
    | (STOPATMARK | STOPBEFOREMARK) '=' (STRING | LOCAL_ID) (AFTER (STRING | LOCAL_ID))?
    ;

restore_common_option
    : RECOVERY | NORECOVERY
    | STANDBY '=' (STRING | LOCAL_ID)
    | MOVE (STRING | LOCAL_ID) TO (STRING | LOCAL_ID)
    | REPLACE | RESTART | RESTRICTED_USER
    | FILE '=' (DECIMAL | LOCAL_ID)
    | (PASSWORD | MEDIANAME | MEDIAPASSWORD) '=' (STRING | LOCAL_ID)
    | (BLOCKSIZE | BUFFERCOUNT | MAXTRANSFERSIZE) '=' (DECIMAL | LOCAL_ID)
    | CHECKSUM | NO_CHECKSUM | STOP_ON_ERROR | CONTINUE_AFTER_ERROR
    | STATS ('=' DECIMAL)?
    | REWIND | NOREWIND | UNLOAD | NOUNLOAD
    ;

restore_metadata
    : RESTORE (HEADERONLY | FILELISTONLY) FROM backup_device
      (WITH restore_metadata_option (',' restore_metadata_option)*)?
    | RESTORE LABELONLY FROM backup_device
      (WITH restore_media_option (',' restore_media_option)*)?
    ;

restore_metadata_option
    : restore_media_option
    | FILE '=' (DECIMAL | LOCAL_ID)
    | PASSWORD '=' (STRING | LOCAL_ID)
    | (METADATA_ONLY | SNAPSHOT) (DBNAME '=' (STRING | LOCAL_ID))?
    ;

restore_media_option
    : (MEDIANAME | MEDIAPASSWORD) '=' (STRING | LOCAL_ID)
    | CHECKSUM | NO_CHECKSUM | STOP_ON_ERROR | CONTINUE_AFTER_ERROR
    | REWIND | NOREWIND | UNLOAD | NOUNLOAD
    ;

restore_verify
    : RESTORE VERIFYONLY FROM backup_device (',' backup_device)*
      (WITH restore_verify_option (',' restore_verify_option)*)?
    ;

restore_verify_option
    : restore_media_option
    | FILE '=' (DECIMAL | LOCAL_ID)
    | PASSWORD '=' (STRING | LOCAL_ID)
    | LOADHISTORY
    | MOVE (STRING | LOCAL_ID) TO (STRING | LOCAL_ID)
    | STATS ('=' DECIMAL)?
    ;

// https://learn.microsoft.com/sql/t-sql/database-console-commands/dbcc-checkident-transact-sql
dbcc_checkident
    : CHECKIDENT '(' id_or_string (',' (NORESEED | RESEED (',' ('-' | '+')? DECIMAL)?))? ')'
      (WITH NO_INFOMSGS)?
    ;

dbcc_indexdefrag
    : INDEXDEFRAG '(' dbcc_object ',' dbcc_object (',' dbcc_object (',' DECIMAL)?)? ')'
      (WITH NO_INFOMSGS)?
    ;

dbcc_shrinkdatabase
    : SHRINKDATABASE '(' dbcc_object (',' DECIMAL)? (',' (NOTRUNCATE | TRUNCATEONLY))? ')'
      dbcc_shrink_options?
    ;

dbcc_shrinkfile
    : SHRINKFILE '(' dbcc_object (',' EMPTYFILE | (',' DECIMAL)? (',' (NOTRUNCATE | TRUNCATEONLY))?) ')'
      dbcc_shrink_options?
    ;

dbcc_shrink_options
    : WITH (WAIT_AT_LOW_PRIORITY ('(' ABORT_AFTER_WAIT '=' (SELF | BLOCKERS) ')')?
            (',' NO_INFOMSGS)? | NO_INFOMSGS)
    ;

dbcc_updateusage
    : UPDATEUSAGE '(' database=dbcc_object (',' table_or_view=dbcc_object (',' index=dbcc_object)?)? ')'
      (WITH (NO_INFOMSGS (','? COUNT_ROWS)? | COUNT_ROWS))?
    ;

dbcc_object
    : id_or_string
    | DECIMAL
    ;

windows_login_option
    : DEFAULT_DATABASE '=' default_database=id_
    | DEFAULT_LANGUAGE '=' (id_ | STRING | DECIMAL)
    ;

alter_login_option
    : PASSWORD '=' (STRING (OLD_PASSWORD '=' STRING | MUST_CHANGE UNLOCK? | UNLOCK MUST_CHANGE?)? | BINARY HASHED)
    | windows_login_option
    | NAME '=' new_name=id_
    | CHECK_POLICY '=' on_off
    | CHECK_EXPIRATION '=' on_off
    | CREDENTIAL '=' id_
    | NO CREDENTIAL
    ;

mapped_user_option
    : DEFAULT_SCHEMA '=' schema_name=id_
    | ALLOW_ENCRYPTED_VALUE_MODIFICATIONS '=' on_off
    ;

contained_user_option
    : mapped_user_option
    | DEFAULT_LANGUAGE '=' (NONE | DECIMAL | id_)
    | SID '=' BINARY
    ;

alter_user_option
    : NAME '=' new_name=id_
    | DEFAULT_SCHEMA '=' (id_ | NULL_)
    | LOGIN '=' id_
    | PASSWORD '=' STRING (OLD_PASSWORD '=' STRING)?
    | DEFAULT_LANGUAGE '=' (NONE | DECIMAL | id_)
    | ALLOW_ENCRYPTED_VALUE_MODIFICATIONS '=' on_off
    ;

application_role_option
    : NAME '=' new_name=id_
    | PASSWORD '=' STRING
    | DEFAULT_SCHEMA '=' id_
    ;

grant_statement
    : GRANT permission_list permission_target? TO principal_id (',' principal_id)*
      (WITH GRANT OPTION)? (AS principal_id)?
    ;

deny_statement
    : DENY permission_list permission_target? TO principal_id (',' principal_id)*
      CASCADE? (AS principal_id)?
    ;

revoke_statement
    : REVOKE (GRANT OPTION FOR)? permission_list permission_target?
      (FROM | TO) principal_id (',' principal_id)* CASCADE? (AS principal_id)?
    ;

permission_list
    : ALL PRIVILEGES?
    | permission_item (',' permission_item)*
    ;

permission_item
    : grant_permission ('(' column_name_list ')')?
    ;

permission_target
    : ON (class_type_for_grant '::')? table_name ('(' column_name_list ')')?
    ;

security_definition
    : (SECURITY | PERFORMANCE | CRYPTOGRAPHICALLY SECURED)? DEFINITION
    ;

private_key_option
    : FILE '=' STRING
    | BINARY_KEYWORD '=' BINARY
    | (DECRYPTION | ENCRYPTION) BY PASSWORD '=' STRING
    ;

certificate_backup_option
    : FILE '=' STRING
    | (ENCRYPTION | DECRYPTION) BY PASSWORD '=' STRING
    | ALGORITHM '=' STRING
    ;

asymmetric_key_create_option
    : ALGORITHM '=' (RSA_4096 | RSA_3072 | RSA_2048 | RSA_1024 | RSA_512)
    | PROVIDER_KEY_NAME '=' STRING
    | CREATION_DISPOSITION '=' (CREATE_NEW | OPEN_EXISTING)
    ;

restore_master_key
    : RESTORE MASTER KEY FROM (FILE | URL) '=' STRING
      DECRYPTION BY PASSWORD '=' STRING ENCRYPTION BY PASSWORD '=' STRING FORCE?
    ;

restore_service_master_key
    : RESTORE SERVICE MASTER KEY FROM FILE '=' STRING
      DECRYPTION BY PASSWORD '=' STRING FORCE?
    ;

column_encryption_key_value
    : '(' COLUMN_MASTER_KEY '=' id_ ',' ALGORITHM '=' STRING ',' ENCRYPTED_VALUE '=' BINARY ')'
    ;

create_database_encryption_key
    : CREATE DATABASE ENCRYPTION KEY WITH ALGORITHM '=' database_key_algorithm
      ENCRYPTION BY SERVER (CERTIFICATE | ASYMMETRIC KEY) id_
    ;

alter_database_encryption_key
    : ALTER DATABASE ENCRYPTION KEY
      (REGENERATE WITH ALGORITHM '=' database_key_algorithm
      | ENCRYPTION BY SERVER (CERTIFICATE | ASYMMETRIC KEY) id_)
    ;

database_key_algorithm
    : AES_128 | AES_192 | AES_256 | TRIPLE_DES_3KEY
    ;

create_database_scoped_credential
    : CREATE DATABASE SCOPED CREDENTIAL credential_name=id_
      WITH IDENTITY '=' STRING (',' SECRET '=' STRING)?
    ;

alter_database_scoped_credential
    : ALTER DATABASE SCOPED CREDENTIAL credential_name=id_
      WITH IDENTITY '=' STRING (',' SECRET '=' STRING)?
    ;

add_signature
    : ADD COUNTER? SIGNATURE TO (OBJECT '::')? (schema_name=id_ '.')? module_name=id_
      BY signature_source (',' signature_source)*
    ;

signature_source
    : (CERTIFICATE | ASYMMETRIC KEY) id_ (WITH (PASSWORD '=' STRING | SIGNATURE '=' BINARY))?
    ;

security_predicate
    : FILTER? PREDICATE security_predicate_function ON table_name
    | BLOCK PREDICATE security_predicate_function ON table_name block_predicate_operation?
    ;

security_predicate_function
    : id_ '.' id_ '(' expression (',' expression)* ')'
    ;

block_predicate_operation
    : AFTER (INSERT | UPDATE) | BEFORE (UPDATE | DELETE)
    ;

security_policy_option
    : (STATE | SCHEMABINDING) '=' on_off
    ;

alter_security_policy
    : ALTER SECURITY POLICY (schema_name=id_ '.')? security_policy_name=id_
      (security_predicate_change (',' security_predicate_change)* (WITH '(' STATE '=' on_off ')')? (NOT FOR REPLICATION)?
      | WITH '(' STATE '=' on_off ')' (NOT FOR REPLICATION)?
      | NOT FOR REPLICATION)
    ;

security_predicate_change
    : (ADD | ALTER) security_predicate
    | DROP FILTER PREDICATE ON table_name
    | DROP BLOCK PREDICATE ON table_name block_predicate_operation?
    ;

audit_target
    : FILE '(' audit_file_option (',' audit_file_option)* ')' | APPLICATION_LOG | SECURITY_LOG
    ;

audit_file_option
    : FILEPATH '=' STRING
    | MAXSIZE '=' (DECIMAL (MB | GB | TB) | UNLIMITED)
    | MAX_ROLLOVER_FILES '=' (DECIMAL | UNLIMITED)
    | MAX_FILES '=' DECIMAL
    | RESERVE_DISK_SPACE '=' on_off
    ;

audit_common_option
    : QUEUE_DELAY '=' DECIMAL
    | ON_FAILURE '=' (CONTINUE | SHUTDOWN | FAIL_OPERATION)
    ;

audit_create_option
    : audit_common_option | AUDIT_GUID '=' STRING
    ;

audit_alter_option
    : audit_common_option | STATE '=' on_off
    ;

audit_predicate
    : NOT? audit_predicate_factor ((AND | OR) NOT? audit_predicate_factor)*
    ;

audit_predicate_factor
    : '(' audit_predicate ')'
    | id_ (comparison_operator (DECIMAL | STRING) | LIKE STRING)
    ;

server_audit_spec_change
    : (ADD | DROP) '(' id_ ')'
    ;

database_audit_item
    : audit_action_specification | audit_action_group_name=id_
    ;

xe_object_name
    : (id_ '.')? id_ '.' id_
    ;

xe_add_event
    : ADD EVENT xe_object_name
      ('(' (SET xe_parameter (',' xe_parameter)*)?
      (ACTION '(' xe_object_name (',' xe_object_name)* ')')?
      (WHERE event_session_predicate_expression)? ')')?
    ;

xe_parameter
    : id_ '=' xe_literal
    ;

xe_literal
    : STRING | MINUS? DECIMAL | '(' xe_literal ')'
    ;

xe_add_target
    : ADD TARGET xe_object_name ('(' SET xe_parameter (',' xe_parameter)* ')')?
    ;

xe_event_change
    : xe_add_event | DROP EVENT xe_object_name
    ;

xe_target_change
    : xe_add_target | DROP TARGET xe_object_name
    ;

xe_session_options
    : WITH '(' xe_session_option (',' xe_session_option)* ')'
    ;

xe_session_option
    : (MAX_MEMORY | MAX_EVENT_SIZE) '=' DECIMAL (KB | MB)
    | EVENT_RETENTION_MODE '=' (ALLOW_SINGLE_EVENT_LOSS | ALLOW_MULTIPLE_EVENT_LOSS | NO_EVENT_LOSS)
    | MAX_DISPATCH_LATENCY '=' (DECIMAL SECONDS | INFINITE)
    | MEMORY_PARTITION_MODE '=' (NONE | PER_NODE | PER_CPU)
    | (TRACK_CAUSALITY | STARTUP_STATE) '=' on_off
    ;

create_availability_group
    : CREATE AVAILABILITY GROUP group_name=id_
      (WITH '(' availability_group_option (',' availability_group_option)* ')')?
      (FOR (DATABASE id_ (',' id_)*)? REPLICA ON availability_replica (',' availability_replica)*
      | AVAILABILITY GROUP ON distributed_availability_group (',' distributed_availability_group))
      (LISTENER STRING '(' availability_listener_options ')')?
    ;

availability_group_option
    : availability_group_common_option
    | BASIC | DISTRIBUTED | CONTAINED REUSE_SYSTEM_DATABASES?
    | CLUSTER_TYPE '=' (WSFC | EXTERNAL | NONE)
    ;

availability_group_set_option
    : availability_group_common_option
    | REQUIRED_SYNCHRONIZED_SECONDARIES_TO_COMMIT '=' DECIMAL
    | ROLE '=' SECONDARY
    ;

availability_group_common_option
    : AUTOMATED_BACKUP_PREFERENCE '=' (PRIMARY | SECONDARY_ONLY | SECONDARY | NONE)
    | FAILURE_CONDITION_LEVEL '=' DECIMAL
    | HEALTH_CHECK_TIMEOUT '=' DECIMAL
    | DB_FAILOVER '=' on_off
    | DTC_SUPPORT '=' (PER_DB | NONE)
    ;

availability_replica
    : STRING WITH '(' availability_replica_option (',' availability_replica_option)* ')'
    ;

availability_replica_option
    : ENDPOINT_URL '=' STRING
    | AVAILABILITY_MODE '=' (SYNCHRONOUS_COMMIT | ASYNCHRONOUS_COMMIT | CONFIGURATION_ONLY)
    | FAILOVER_MODE '=' (AUTOMATIC | MANUAL | EXTERNAL)
    | SEEDING_MODE '=' (AUTOMATIC | MANUAL)
    | BACKUP_PRIORITY '=' DECIMAL
    | SESSION_TIMEOUT '=' DECIMAL
    | SECONDARY_ROLE '(' availability_secondary_option (',' availability_secondary_option)* ')'
    | PRIMARY_ROLE '(' availability_primary_option (',' availability_primary_option)* ')'
    ;

availability_secondary_option
    : ALLOW_CONNECTIONS '=' (NO | READ_ONLY | ALL)
    | READ_ONLY_ROUTING_URL '=' (STRING | NONE)
    ;

availability_primary_option
    : ALLOW_CONNECTIONS '=' (READ_WRITE | ALL)
    | READ_ONLY_ROUTING_LIST '=' (NONE | '(' availability_routing_entry (',' availability_routing_entry)* ')')
    | READ_WRITE_ROUTING_URL '=' (STRING | NONE)
    ;

availability_routing_entry
    : STRING | '(' STRING (',' STRING)* ')'
    ;

distributed_availability_group
    : STRING WITH '(' distributed_availability_option (',' distributed_availability_option)* ')'
    ;

distributed_availability_option
    : LISTENER_URL '=' STRING
    | AVAILABILITY_MODE '=' (SYNCHRONOUS_COMMIT | ASYNCHRONOUS_COMMIT)
    | FAILOVER_MODE '=' MANUAL
    | SEEDING_MODE '=' (AUTOMATIC | MANUAL)
    ;

availability_listener_options
    : WITH DHCP (ON '(' STRING ',' STRING ')')?
    | WITH IP '(' '(' availability_listener_ip ')' (',' '(' availability_listener_ip ')')* ')' (',' PORT '=' DECIMAL)?
    ;

availability_listener_ip
    : STRING (',' STRING)?
    ;

endpoint_protocol
    : FOR TSQL '(' ')'
    | FOR SERVICE_BROKER '(' (endpoint_broker_option (','? endpoint_broker_option)*)? ')'
    | FOR DATABASE_MIRRORING '(' endpoint_mirroring_option (','? endpoint_mirroring_option)* ')'
    ;

endpoint_broker_option
    : endpoint_authentication_clause
    | endpoint_encryption_alogorithm_clause
    | MESSAGE_FORWARDING '=' (ENABLED | DISABLED)
    | MESSAGE_FORWARD_SIZE '=' DECIMAL
    ;

endpoint_mirroring_option
    : endpoint_authentication_clause
    | endpoint_encryption_alogorithm_clause
    | ROLE '=' (WITNESS | PARTNER | ALL)
    ;

alter_route
    : ALTER ROUTE route_name=id_ WITH route_option (',' route_option)*
    ;

route_option
    : (SERVICE_NAME | BROKER_INSTANCE | ADDRESS | MIRROR_ADDRESS) '=' STRING
    | LIFETIME '=' DECIMAL
    ;

move_conversation
    : MOVE CONVERSATION LOCAL_ID TO LOCAL_ID
    ;
