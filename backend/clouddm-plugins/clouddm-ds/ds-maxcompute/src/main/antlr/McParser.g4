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

// HPL/SQL Procedural SQL Extension Grammar
grammar McParser;

program : block EOF;

//block : ((begin_end_block | stmt) T_GO?)+ | EOF ;               // Multiple consecutive blocks/statements

block : semicolon_stmt* ((begin_end_block | stmt) T_GO?) (semicolon_stmt+ ((begin_end_block | stmt) T_GO?) )*  semicolon_stmt* EOF  ;               // Multiple consecutive blocks/statements

begin_end_block :
       declare_block? T_BEGIN block exception_block? block_end
     ;

single_block_stmt :                                      // Single BEGIN END block (but nested blocks are possible) or single statement
       T_BEGIN block exception_block? block_end
     | stmt T_SEMICOLON?
     ;

block_end :
       {!_input.LT(2).getText().equalsIgnoreCase("TRANSACTION")}? T_END
     ;

proc_block :
       begin_end_block
     | stmt+ T_GO?
     ;

stmt :
       assignment_stmt
     | allocate_cursor_stmt
     | alter_table_stmt
     | associate_locator_stmt
     | begin_transaction_stmt
     | break_stmt
     | call_stmt
     | collect_stats_stmt
     | close_stmt
     | cmp_stmt
     | copy_from_local_stmt
     | copy_stmt
     | commit_stmt
     | create_database_stmt
     | create_function_stmt
     | create_local_temp_table_stmt
     | create_package_stmt
     | create_package_body_stmt
     | create_procedure_stmt
     | create_table_stmt
     | analyze_table_stmt
//     | create_table_type_stmt
     | create_view_stmt
     | create_materialized_view_stmt
     | alter_materialized_view_stmt
     | declare_stmt
     | delete_stmt
     | describe_stmt
     | drop_stmt
     | end_transaction_stmt
     | exec_stmt
     | exit_stmt
     | fetch_stmt
     | for_cursor_stmt
     | for_range_stmt
     | if_stmt
     | include_stmt
     | cte_select_stmt? insert_stmt
     | insert_directory_stmt
     | get_diag_stmt
     | grant_stmt
     | leave_stmt
//     | map_object_stmt
     | merge_stmt
     | open_stmt
     | quit_stmt
     | raise_stmt
     | resignal_stmt
     | return_stmt
     | rollback_stmt
     | select_stmt
     | signal_stmt
     | summary_stmt
     | update_stmt
     | use_stmt
     | truncate_stmt
     | values_into_stmt
     | while_stmt
     | unconditional_loop_stmt
     | label
//     | expr_stmt
     | show_stmt
//     | semicolon_stmt      // Placed here to allow null statements ;;...
     ;
create_view_stmt :
       T_CREATE (T_OR T_REPLACE)? T_VIEW (T_IF T_NOT T_EXISTS)? table_name (T_OPEN_P ident comment_clause? (T_COMMA ident comment_clause?)* T_CLOSE_P)?
       comment_clause? T_AS select_stmt
     ;

show_stmt:
    T_SHOW T_TABLES (T_LIKE L_S_STRING)? #showTables
    | T_SHOW T_CREATE T_TABLE table_name   #showCreateTable
    | T_SHOW T_STATISTIC table_name partition_spec? T_COLUMNS (T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P)? #showTableColumnStatics
    | T_SHOW T_PARTITIONS table_name #showTablePartitions
    | T_SHOW T_HISTORY T_FOR T_TABLES (T_LIKE L_S_STRING)? #showHistoryTables
    | T_SHOW T_HISTORY T_FOR T_TABLE table_name #showHistoryTable
    | (T_SHOW | T_LIST) T_ROLES  #showRoles
    |  T_LIST T_USERS  #showUsers
    |  T_LIST T_TRUSTEDPROJECTS  #showTrustProjects
    ;

create_materialized_view_stmt:
    T_CREATE T_MATERIALIZED T_VIEW (T_IF T_NOT T_EXISTS)? name = ident
    (T_LIFECYCLE int_number)?
    (T_BUILD T_DEFERRED)?
    ( T_OPEN_P ident comment_clause? (T_COMMA ident comment_clause?)* T_CLOSE_P )?
    ( T_DISABLE T_REWRITE)?
    comment_clause?
    (T_PARTITIONED T_BY T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P)?
    create_table_clustere?
    (T_REFRESH T_EVERY int_number (T_MINUTES | T_HOURS | T_DAYS))?
    tbprops?
    T_AS select_stmt
    ;

alter_materialized_view_stmt:
    T_ALTER T_MATERIALIZED T_VIEW name = ident alter_materialized_view_item
    ;

alter_materialized_view_item:
     T_REBUILD  partition_spec_filter?
     | T_SET T_LIFECYCLE int_number
     | partition_spec_filter? (T_ENABLE|T_DISABLE) T_LIFECYCLE
     | T_DROP (T_IF T_EXISTS)? partition_spec_filter
    ;

tbprops:
    T_TBLPROPERTIES T_OPEN_P prop (T_COMMA prop)* T_CLOSE_P;

prop:
    key=string T_EQUAL value=string
    ;

semicolon_stmt :
       T_SEMICOLON
     | '@' | '#' | '/'
     ;

exception_block :       // Exception block
       T_EXCEPTION exception_block_item+
     ;

exception_block_item :
       T_WHEN L_ID T_THEN block ~(T_WHEN | T_END)
     ;


expr_stmt :             // Standalone expression
       {!_input.LT(1).getText().equalsIgnoreCase("GO")}? expr
     ;

assignment_stmt :       // Assignment statement
//       T_SET set_session_option
//     | T_SET assignment_stmt_item (T_COMMA assignment_stmt_item)*
        T_SET key=qident T_EQUAL value=expr
     ;

assignment_stmt_item :
       assignment_stmt_single_item
     | assignment_stmt_multiple_item
     | assignment_stmt_select_item
     | assignment_stmt_collection_item
     ;

assignment_stmt_single_item :
       qident T_COLON? T_EQUAL expr?
     | T_OPEN_P qident T_CLOSE_P T_COLON? T_EQUAL expr
     ;

assignment_stmt_collection_item :
    expr_func T_COLON T_EQUAL expr
    ;

assignment_stmt_multiple_item :
       T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P T_COLON? T_EQUAL T_OPEN_P expr (T_COMMA expr)* T_CLOSE_P
     ;

assignment_stmt_select_item :
       (ident | (T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P)) T_COLON? T_EQUAL T_OPEN_P select_stmt T_CLOSE_P
     ;

allocate_cursor_stmt:
       T_ALLOCATE ident T_CURSOR T_FOR ((T_RESULT T_SET) | T_PROCEDURE) ident
     ;

associate_locator_stmt :
       T_ASSOCIATE (T_RESULT T_SET)? (T_LOCATOR | T_LOCATORS) T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P T_WITH T_PROCEDURE ident
     ;

begin_transaction_stmt :
       T_BEGIN T_TRANSACTION
     ;

break_stmt :
       T_BREAK
     ;

call_stmt :
       T_CALL (expr_dot | expr_func | ident)
     ;

declare_stmt :          // Declaration statement
       T_DECLARE declare_stmt_item (T_COMMA declare_stmt_item)*
     ;

declare_block :         // Declaration block
       T_DECLARE declare_stmt_item T_SEMICOLON (declare_stmt_item T_SEMICOLON)*
     ;

declare_block_inplace :
       declare_stmt_item T_SEMICOLON (declare_stmt_item T_SEMICOLON)*
     ;

declare_stmt_item :
       declare_cursor_item
     | declare_condition_item
     | declare_handler_item
     | declare_var_item
     | declare_temporary_table_item
     ;

declare_var_item :
       ident (T_COMMA ident)* T_AS? dtype dtype_len? dtype_attr dtype_default?
     | ident T_CONSTANT T_AS? dtype dtype_len? dtype_default
     ;

declare_condition_item :    // Condition declaration
       ident T_CONDITION
     ;

declare_cursor_item :      // Cursor declaration
       (T_CURSOR ident | ident T_CURSOR) (cursor_with_return | cursor_without_return)? (T_IS | T_AS | T_FOR) (select_stmt | expr )
     ;

cursor_with_return :
       T_WITH T_RETURN T_ONLY? (T_TO (T_CALLER | T_CLIENT))?
     ;

cursor_without_return :
       T_WITHOUT T_RETURN
     ;

declare_handler_item :     // Condition handler declaration
       (T_CONTINUE | T_EXIT) T_HANDLER T_FOR (T_SQLEXCEPTION | T_SQLWARNING | T_NOT T_FOUND | ident) single_block_stmt
     ;

declare_temporary_table_item :     // DECLARE TEMPORARY TABLE statement
       T_GLOBAL? T_TEMPORARY T_TABLE qident create_table_preoptions? create_table_definition
     ;

create_table_stmt :
       T_CREATE T_TABLE (T_IF T_NOT T_EXISTS)? table_name create_table_definition create_table_partitions? create_table_clustere? store_as? tbprops? (T_LIFECYCLE L_INT)?
     ;

store_as:
    T_STORED T_AS L_ID // todo
    ;

analyze_table_stmt:
        T_ANALYZE T_TABLE tableName=qident partition_spec? T_COMPUTE T_STATISTICS T_FOR T_COLUMNS (T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P)?
    ;

create_table_partitions:
     T_PARTITIONED T_BY T_OPEN_P create_partition_column (T_COMMA create_partition_column)?  T_CLOSE_P
     | T_AUTO T_PARTITIONED T_BY T_OPEN_P expr_func T_AS ident T_CLOSE_P
    ;

create_partition_column:
     ident column_type (T_COMMENT L_S_STRING)?
     ;

create_table_clustere:
    T_RANGE? T_CLUSTERED T_BY T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P
    (T_SORTED T_BY T_OPEN_P ident (T_ASC | T_DESC)? (T_COMMA ident (T_ASC | T_DESC)?)* T_CLOSE_P)?
    (T_INTO int_number T_BUCKETS)?
    ;


create_local_temp_table_stmt :
       T_CREATE (T_LOCAL T_TEMPORARY | (T_SET | T_MULTISET)? T_VOLATILE) T_TABLE qident create_table_preoptions? create_table_definition
     ;

//create_table_definition :
//      (T_AS? T_OPEN_P select_stmt T_CLOSE_P | T_AS? select_stmt | T_OPEN_P create_table_columns T_CLOSE_P  | T_LIKE table_name) create_table_options?
//     ;

create_table_definition :
      T_AS? T_OPEN_P select_stmt T_CLOSE_P   #createTableSelect
      | T_AS? select_stmt                    #createTableSelect
      | T_OPEN_P create_table_columns (T_COMMA create_table_primary)? T_CLOSE_P comment_clause?  #createTableColumn
      | T_LIKE table_name (T_LIFECYCLE L_INT)?                     #createTableLike
     ;

comment_clause :
    T_COMMENT L_S_STRING
    ;

create_table_columns :
       create_table_columns_item (T_COMMA create_table_columns_item)*
     ;

create_table_columns_item :
       column_name column_type (T_NOT? T_NULL)? (T_PRIMARY T_KEY?)? (T_DEFAULT default_value)? (T_COMMENT comment = (L_S_STRING | L_D_STRING))?
     ;

create_table_primary:
       (T_CONSTRAINT name=ident)? T_PRIMARY T_KEY T_CLUSTERED? T_OPEN_P columns+=ident (T_ASC | T_DESC)? (T_COMMA columns+=ident (T_ASC | T_DESC)?)* T_CLOSE_P T_ENABLE? index_storage_clause?
      ;

//create_table_type_stmt :
//      T_TYPE ident T_IS T_TABLE T_OF tbl_type (T_NOT T_NULL)? T_INDEX T_BY dtype
//    ;

//tbl_type :
//    sql_type | dtype
//    ;

default_value:
    L_S_STRING
    | dec_number
    | date_literal
    | timestamp_literal
    | int_number
    | T_TRUE
    | T_FALSE
    ;


sql_type :
    qident '%' (T_TYPE | T_ROWTYPE)
    ;

column_name :
       qident
     ;

create_table_column_inline_cons :
       dtype_default
     | T_NOT? T_NULL
     | T_PRIMARY T_KEY
     | T_UNIQUE
     | T_REFERENCES table_name T_OPEN_P qident T_CLOSE_P create_table_fk_action*
     | T_IDENTITY T_OPEN_P L_INT (T_COMMA L_INT)* T_CLOSE_P
     | T_AUTO_INCREMENT
     | T_ENABLE
     ;

//create_table_column_cons :
//       T_PRIMARY T_KEY T_CLUSTERED? T_OPEN_P qident (T_ASC | T_DESC)? (T_COMMA qident (T_ASC | T_DESC)?)* T_CLOSE_P T_ENABLE? index_storage_clause?
////     | T_FOREIGN T_KEY T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P T_REFERENCES table_name T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P create_table_fk_action*
//    ;

create_table_fk_action :
       T_ON (T_UPDATE | T_DELETE) (T_NO T_ACTION | T_RESTRICT | T_SET T_NULL | T_SET T_DEFAULT | T_CASCADE)
     ;

create_table_preoptions :
      create_table_preoptions_item+
     ;

create_table_preoptions_item :
        T_COMMA create_table_preoptions_td_item
      | create_table_options_hive_item
     ;

create_table_preoptions_td_item :
       T_NO? (T_LOG | T_FALLBACK)
     ;

//create_table_options :
//       create_table_options_item+
//     ;


//
//create_table_options_item :
////       T_ON T_COMMIT (T_DELETE | T_PRESERVE) T_ROWS
////     | create_table_options_ora_item
////     | create_table_options_db2_item
////     | create_table_options_td_item
////     | create_table_options_hive_item
////     | create_table_options_mssql_item
////     | create_table_options_mysql_item
//     | tbprops
//     ;

create_table_options_ora_item :
       T_SEGMENT T_CREATION (T_IMMEDIATE | T_DEFERRED)
     | (T_PCTFREE | T_PCTUSED | T_INITRANS | T_MAXTRANS) L_INT
     | T_NOCOMPRESS
     | (T_LOGGING | T_NOLOGGING)
     | T_STORAGE T_OPEN_P (qident | L_INT)+ T_CLOSE_P
     | T_TABLESPACE qident
     ;

create_table_options_db2_item :
       T_INDEX? T_IN qident
     | T_WITH T_REPLACE
     | T_DISTRIBUTE T_BY T_HASH T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P
     | T_NOT? T_LOGGED
     | T_COMPRESS (T_YES | T_NO)
     | T_DEFINITION T_ONLY
     | T_WITH T_RESTRICT T_ON T_DROP
     ;

create_table_options_td_item :
       T_UNIQUE? T_PRIMARY T_INDEX T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P
     | T_WITH T_DATA
     ;

create_table_options_hive_item :
       create_table_hive_row_format
     | T_STORED T_AS qident
     ;

create_table_hive_row_format :
       T_ROW T_FORMAT T_DELIMITED create_table_hive_row_format_fields*
     ;

create_table_hive_row_format_fields :
       T_FIELDS T_TERMINATED T_BY expr (T_ESCAPED T_BY expr)?
     | T_COLLECTION T_ITEMS T_TERMINATED T_BY expr
     | T_MAP T_KEYS T_TERMINATED T_BY expr
     | T_LINES T_TERMINATED T_BY expr
     | T_NULL T_DEFINED T_AS expr
     ;

create_table_options_mssql_item :
       T_ON qident
     | T_TEXTIMAGE_ON qident
     ;
//
//create_table_options_mysql_item :
//       T_AUTO_INCREMENT T_EQUAL? expr
//     | T_COMMENT T_EQUAL? expr
//     | T_DEFAULT? (T_CHARACTER T_SET | T_CHARSET) T_EQUAL? expr
//     | T_ENGINE T_EQUAL? expr
//     ;

alter_table_stmt :
       T_ALTER T_TABLE table_name alter_table_item
     ;

alter_table_item :
       T_SET T_COMMENT comment = L_S_STRING #alterTableComment
       | T_SET T_LIFECYCLE L_INT #alterTableLifeCycle
       | partition_spec? (T_DISABLE | T_ENABLE) T_LIFECYCLE #alterTableEnableLifeCycle
       | T_TOUCH partition_spec? #alterTableTouch
       | create_table_clustere #alterTableClustered
       | T_NOT T_CLUSTERED    #alterTableClustered
       | T_RENAME T_TO  ident #alterTableReanme
       | T_ADD2 (T_IF T_NOT T_EXISTS)?   partition_spec partition_spec*  #alterTablePartition
       | partition_spec  T_RENAME T_TO   partition_spec  #alterTablePartition
       | T_MERGE (T_IF T_EXISTS)?  partition_spec  (T_COMMA  partition_spec)* T_OVERWRITE   partition_spec T_PURGE? #alterTablePartition
       | T_DROP (T_IF T_EXISTS)?  partition_spec_filter  (T_COMMA  partition_spec_filter)* #alterTablePartition
       | T_SET tbprops   #alterTableProp
       | T_ADD2 T_COLUMNS (T_IF T_NOT T_EXISTS)?  T_OPEN_P create_table_columns T_CLOSE_P   #alterTableAddColumns
       | T_DROP T_COLUMNS ident (T_COMMA ident)*   #alterTableDropColumns
       | T_CHANGE T_COLUMN? oldname=ident newname=ident column_type comment_clause? #alterTableChangeColumn
       | T_CHANGE T_COLUMN? oldname=ident T_RENAME T_TO newname=ident  #alterTableChangeColumnName
       | T_CHANGE T_COLUMN? oldname=ident comment_clause #alterTableChangeColumnComment
       | T_CHANGE T_COLUMN? oldname=ident T_NULL #alterTableChangeColumnNull
       | partition_spec T_COMPACT T_MAJOR #alterTableCompctMajor
     ;

partition_spec:
     T_PARTITION T_OPEN_P ident T_EQUAL (string | int_number) (T_COMMA ident T_EQUAL(string | int_number))* T_CLOSE_P
    ;

partition_spec_filter:
     T_PARTITION T_OPEN_P bool_expr (T_COMMA bool_expr)* T_CLOSE_P
    ;


column_type:
    dtype dtype_len?
    ;

dtype :                  // Data types
       T_CHAR
     | T_CHARACTER
     | T_BIGINT
     | T_BINARY_DOUBLE
     | T_BINARY_FLOAT
     | T_BINARY_INTEGER
     | T_BIT
     | T_DATE
     | T_DATETIME
     | T_DEC
     | T_DECIMAL
     | T_DOUBLE T_PRECISION?
     | T_FLOAT
     | T_INT
     | T_INT2
     | T_INT4
     | T_INT8
     | T_INTEGER
     | T_NCHAR
     | T_NVARCHAR
     | T_NUMBER
     | T_NUMERIC
     | T_PLS_INTEGER
     | T_REAL
     | T_RESULT_SET_LOCATOR T_VARYING
     | T_SIMPLE_FLOAT
     | T_SIMPLE_DOUBLE
     | T_SIMPLE_INTEGER
     | T_SMALLINT
     | T_SMALLDATETIME
     | T_STRING
     | T_SYS_REFCURSOR
     | T_TIMESTAMP
     | T_TINYINT
     | T_VARCHAR
     | T_VARCHAR2
     | T_XML
     | qident ('%' (T_TYPE | T_ROWTYPE))?             // User-defined or derived data type
     ;

dtype_len :             // Data type length or size specification
       T_OPEN_P (L_INT | T_MAX) (T_CHAR | T_BYTE)? (T_COMMA L_INT)? T_CLOSE_P
     ;

dtype_attr :
       T_NOT? T_NULL
     | T_CHARACTER T_SET ident
     | T_NOT? (T_CASESPECIFIC | T_CS)
     ;

dtype_default :
       T_COLON? T_EQUAL expr
     | T_WITH? T_DEFAULT expr?
     ;

create_database_stmt :
      T_CREATE  (T_DATABASE | T_SCHEMA) (T_IF T_NOT T_EXISTS)? qident create_database_option*
    ;

create_database_option :
      T_COMMENT expr
    | T_LOCATION expr
    ;

create_function_stmt :
      (T_ALTER | T_CREATE (T_OR T_REPLACE)? | T_REPLACE)? T_FUNCTION ident create_routine_params? create_function_return (T_AS | T_IS)? declare_block_inplace? single_block_stmt
    ;

create_function_return :
       (T_RETURN | T_RETURNS) dtype dtype_len?
     ;

create_package_stmt :
      (T_ALTER | T_CREATE (T_OR T_REPLACE)? | T_REPLACE)? T_PACKAGE ident (T_AS | T_IS) package_spec T_END (ident T_SEMICOLON)?
    ;

package_spec :
      package_spec_item T_SEMICOLON (package_spec_item T_SEMICOLON)*
    ;

package_spec_item :
      declare_stmt_item
    | T_FUNCTION ident create_routine_params? create_function_return
    | (T_PROCEDURE | T_PROC) ident create_routine_params?
    ;

create_package_body_stmt :
      (T_ALTER | T_CREATE (T_OR T_REPLACE)? | T_REPLACE)? T_PACKAGE T_BODY ident (T_AS | T_IS) package_body T_END (ident T_SEMICOLON)?
    ;

package_body :
      package_body_item T_SEMICOLON (package_body_item T_SEMICOLON)*
    ;

package_body_item :
      declare_stmt_item
    | create_function_stmt
    | create_procedure_stmt
    ;

create_procedure_stmt :
      (T_ALTER | T_CREATE (T_OR T_REPLACE)? | T_REPLACE)? (T_PROCEDURE | T_PROC) ident create_routine_params? create_routine_options? (T_AS | T_IS)? declare_block_inplace? label? proc_block (ident T_SEMICOLON)?
    ;

create_routine_params :
       T_OPEN_P T_CLOSE_P
     | T_OPEN_P create_routine_param_item (T_COMMA create_routine_param_item)* T_CLOSE_P
     | {!_input.LT(1).getText().equalsIgnoreCase("IS") &&
        !_input.LT(1).getText().equalsIgnoreCase("AS") &&
        !(_input.LT(1).getText().equalsIgnoreCase("DYNAMIC") && _input.LT(2).getText().equalsIgnoreCase("RESULT"))
        }?
       create_routine_param_item (T_COMMA create_routine_param_item)*
     ;

create_routine_param_item :
       (T_IN | T_OUT | T_INOUT | T_IN T_OUT)? ident dtype dtype_len? dtype_attr* dtype_default?
     | ident (T_IN | T_OUT | T_INOUT | T_IN T_OUT)? dtype dtype_len? dtype_attr* dtype_default?
     ;

create_routine_options :
       create_routine_option+
     ;
create_routine_option :
       T_LANGUAGE T_SQL
     | T_SQL T_SECURITY (T_CREATOR | T_DEFINER | T_INVOKER | T_OWNER)
     | T_DYNAMIC? T_RESULT T_SETS L_INT
     ;

drop_stmt :             // DROP statement
       T_DROP T_TABLE (T_IF T_EXISTS)? table_name        #dropTable
     | T_DROP T_VIEW (T_IF T_EXISTS)? table_name        #dropView
     | T_DROP T_MATERIALIZED T_VIEW (T_IF T_EXISTS)? ident T_PURGE?        #dropMView
     | T_DROP T_PACKAGE (T_IF T_EXISTS)? ident           #dropPackage
     | T_DROP (T_PROCEDURE | T_FUNCTION) (T_IF T_EXISTS)? ident  #dropFunc
     | T_DROP (T_DATABASE | T_SCHEMA) (T_IF T_EXISTS)? qident      #dropSchema
     ;

end_transaction_stmt :
       T_END T_TRANSACTION
     ;

exec_stmt :             // EXEC, EXECUTE IMMEDIATE statement
       (T_EXEC | T_EXECUTE) T_IMMEDIATE? expr (T_OPEN_P expr_func_params T_CLOSE_P | expr_func_params)? (T_INTO L_ID (T_COMMA L_ID)*)? using_clause?
     ;

if_stmt :               // IF statement
       if_plsql_stmt
     | if_tsql_stmt
     | if_bteq_stmt | if_mc_stmt
     ;
if_mc_stmt :
      T_IF T_OPEN_P bool_expr T_COMMA expr T_COMMA expr T_CLOSE_P
     ;
if_plsql_stmt :
       T_IF bool_expr T_THEN block elseif_block* else_block? T_END T_IF
     ;

if_tsql_stmt :
       T_IF bool_expr single_block_stmt (T_ELSE single_block_stmt)?
     ;

if_bteq_stmt :
       '.' T_IF bool_expr T_THEN single_block_stmt
     ;

elseif_block :
       (T_ELSIF | T_ELSEIF) bool_expr T_THEN block
     ;

else_block :
       T_ELSE block
     ;

include_stmt :          // INCLUDE statement
       T_INCLUDE (file_name | expr)
     ;

insert_stmt :           // INSERT statement
        T_INSERT (T_OVERWRITE T_TABLE | T_INTO T_TABLE?) table_name pt_spec? insert_stmt_cols? (select_stmt | insert_stmt_rows) zorder_stmt?
     ;
pt_spec :
       T_PARTITION T_OPEN_P func_param (T_COMMA func_param)* T_CLOSE_P
     ;
zorder_stmt :
       T_ZORDER T_BY qident (T_COMMA qident)*
     ;
insert_stmt_cols :
       T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P
     ;

insert_stmt_rows :
       T_VALUES insert_stmt_row (T_COMMA insert_stmt_row)*
     ;

insert_stmt_row:
       T_OPEN_P expr (T_COMMA expr)* T_CLOSE_P
     ;

insert_directory_stmt :
       T_INSERT T_OVERWRITE T_LOCAL? T_DIRECTORY expr_file expr_select
     ;

exit_stmt :
       T_EXIT L_ID? (T_WHEN bool_expr)?
     ;

get_diag_stmt :         // GET DIAGNOSTICS statement
       T_GET T_DIAGNOSTICS get_diag_stmt_item
     ;

get_diag_stmt_item :
       get_diag_stmt_exception_item
     | get_diag_stmt_rowcount_item
     ;

get_diag_stmt_exception_item :
       T_EXCEPTION L_INT qident T_EQUAL T_MESSAGE_TEXT
     ;

get_diag_stmt_rowcount_item :
       qident T_EQUAL T_ROW_COUNT
     ;

grant_stmt :
       T_GRANT grant_stmt_item (T_COMMA grant_stmt_item)* T_TO T_ROLE qident
     ;

grant_stmt_item :
       T_EXECUTE T_ON T_PROCEDURE qident
     ;

leave_stmt :
       T_LEAVE L_ID?
     ;

map_object_stmt :
       T_MAP T_OBJECT ident (T_TO ident)? (T_AT ident)?
     ;

open_stmt :             // OPEN cursor statement
         T_OPEN ident (T_FOR (select_stmt | expr))?
     ;

fetch_stmt :            // FETCH cursor statement
       T_FETCH T_FROM? ident bulk_collect_clause? T_INTO ident (T_COMMA ident)* fetch_limit?
     ;

fetch_limit:
      T_LIMIT L_INT
     ;

collect_stats_stmt :
       T_COLLECT (T_STATISTICS | T_STATS) T_ON table_name collect_stats_clause?
     ;

collect_stats_clause :
       T_COLUMN T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P
     ;

close_stmt :            // CLOSE cursor statement
       T_CLOSE L_ID
     ;

cmp_stmt :              // CMP statement
       T_CMP (T_ROW_COUNT | T_SUM) cmp_source T_COMMA cmp_source
     ;

cmp_source :
      (table_name where_clause? | T_OPEN_P select_stmt T_CLOSE_P) (T_AT qident)?
     ;

copy_from_local_stmt :  // COPY FROM LOCAL statement
       T_COPY T_FROM T_LOCAL copy_source (T_COMMA copy_source)* T_TO copy_target copy_file_option*
     ;

copy_stmt :             // COPY statement
       T_COPY (table_name | T_OPEN_P select_stmt T_CLOSE_P) T_TO T_HDFS? copy_target copy_option*
     ;

copy_source :
       (file_name | expr)
     ;

copy_target :
       (file_name | expr)
     ;

copy_option :
       T_AT qident
     | T_BATCHSIZE expr
     | T_DELIMITER expr
     | T_SQLINSERT qident
     ;

copy_file_option :
       T_DELETE
     | T_IGNORE
     | T_OVERWRITE
     ;

commit_stmt :           // COMMIT statement
       T_COMMIT T_WORK?
     ;

create_index_col :
       qident (T_ASC | T_DESC)?
     ;

index_storage_clause :
      index_mssql_storage_clause
    ;

index_mssql_storage_clause :
      T_WITH T_OPEN_P qident T_EQUAL qident (T_COMMA qident T_EQUAL qident)* T_CLOSE_P create_table_options_mssql_item*
    ;


quit_stmt :
       '.'? T_QUIT expr?
     ;

raise_stmt :
       T_RAISE
     ;

resignal_stmt :         // RESIGNAL statement
       T_RESIGNAL (T_SQLSTATE T_VALUE? expr (T_SET T_MESSAGE_TEXT T_EQUAL expr)? )?
     ;

return_stmt :           // RETURN statement
       T_RETURN expr?
     ;

rollback_stmt :         // ROLLBACK statement
       T_ROLLBACK T_WORK?
     ;

set_session_option :
       set_current_schema_option
     | set_mssql_session_option
     | set_teradata_session_option
     ;

set_current_schema_option :
       ((T_CURRENT? T_SCHEMA) | T_CURRENT_SCHEMA) T_EQUAL? expr
     ;

set_mssql_session_option :
     ( T_ANSI_NULLS
     | T_ANSI_PADDING
     | T_NOCOUNT
     | T_QUOTED_IDENTIFIER
     | T_XACT_ABORT )
     (T_ON | T_OFF)
     ;

set_teradata_session_option :
       T_QUERY_BAND T_EQUAL (expr | T_NONE) T_UPDATE? T_FOR (T_TRANSACTION | T_SESSION)
     ;

signal_stmt :          // SIGNAL statement
       T_SIGNAL ident
     ;

summary_stmt :         // SUMMARY statement
       T_SUMMARY (T_TOP expr)? T_FOR (select_stmt | table_name where_clause? (T_LIMIT expr)?)
     ;

truncate_stmt :
       T_TRUNCATE T_TABLE table_name (partition_spec_filter (T_COMMA partition_spec_filter)*)?
     ;

use_stmt :              // USE statement
       T_USE expr
     ;

values_into_stmt :     // VALUES INTO statement
       T_VALUES T_OPEN_P? expr (T_COMMA expr)* T_CLOSE_P? T_INTO T_OPEN_P? ident (T_COMMA ident)* T_CLOSE_P?
     ;

while_stmt :            // WHILE loop statement
       T_WHILE bool_expr (T_DO | T_LOOP | T_THEN | T_BEGIN) block T_END (T_WHILE | T_LOOP)?
     ;

unconditional_loop_stmt : // LOOP .. END LOOP
       T_LOOP block T_END T_LOOP
     ;

for_cursor_stmt :       // FOR (cursor) statement
       T_FOR L_ID T_IN T_OPEN_P? select_stmt T_CLOSE_P? T_LOOP block T_END T_LOOP
     ;

for_range_stmt :        // FOR (Integer range) statement
       T_FOR L_ID T_IN T_REVERSE? expr T_DOT2 expr ((T_BY | T_STEP) expr)? T_LOOP block T_END T_LOOP
     ;

label :
       L_LABEL
     | T_LESS T_LESS L_ID T_GREATER T_GREATER
     ;

using_clause :          // USING var,... clause
       T_USING expr (T_COMMA expr)*
     ;

select_stmt :            // SELECT statement
      cte_select_stmt? fullselect_stmt order_by_clause? distribute_clause? (T_LIMIT L_INT)?
     ;

cte_select_stmt :
       T_WITH cte_select_stmt_item (T_COMMA cte_select_stmt_item)*
     ;

cte_select_stmt_item :
       ident cte_select_cols? T_AS T_OPEN_P select_stmt T_CLOSE_P
     ;

cte_select_cols :
       T_OPEN_P qident (T_COMMA qident)* T_CLOSE_P
     ;

fullselect_stmt :
       fullselect_stmt_item (fullselect_set_clause fullselect_stmt_item)*
     ;

fullselect_stmt_item :
       subselect_stmt
     | T_OPEN_P fullselect_stmt T_CLOSE_P
     ;

fullselect_set_clause :
       (T_UNION | T_EXCEPT | T_INTERSECT | T_MINUTES) (T_DISTINCT |T_ALL)?
     ;

subselect_stmt :
       T_SELECT select_list from_clause? where_clause?  (distribute_clause? sort_clause? | (group_by_clause having_clause?)? order_by_clause?)   (T_LIMIT L_INT)?
     ;

distribute_clause:
     T_DISTRIBUTE T_BY ident
    ;

sort_clause:
     T_SORT T_BY ident T_DESC?
    ;

select_list :
       select_list_set?  select_list_item (T_COMMA select_list_item)*
     ;

select_list_set :
       T_ALL
     | T_DISTINCT
     ;

//select_list_limit :
//       T_TOP expr
//     ;

select_list_item :
       ((expr | bool_expr) select_list_alias? |select_list_asterisk)
     ;

select_list_alias :
       {!_input.LT(1).getText().equalsIgnoreCase("INTO") && !_input.LT(1).getText().equalsIgnoreCase("FROM")}?
//       (T_AS? qident | T_OPEN_P T_TITLE L_S_STRING T_CLOSE_P ) |        (T_AS T_OPEN_P ident (T_COMMA ident)* T_CLOSE_P )
       T_AS? ident
     ;

select_list_asterisk :
       (L_ID '.')? '*'
     ;

table_row :
      ident T_OPEN_P L_INT T_CLOSE_P
    ;


bulk_collect_clause :
       T_BULK T_COLLECT
     ;

from_clause :
       T_FROM from_body_clause
     ;

lateralView :
        T_LATERAL T_VIEW T_OUTER? expr_func from_alias_clause (T_AS ident (T_COMMA ident)*)?
    | T_COMMA? T_LATERAL (
        T_VIEW expr_func from_alias_clause (T_AS ident (T_COMMA ident)*)?

    )
    ;

from_table_clause :
      ( from_table_name_clause
     | from_subselect_clause
     | from_table_values_clause )
     lateralView*
     ;

from_table_name_clause :
       table_name from_alias_clause?
     ;

from_subselect_clause :
       T_OPEN_P select_stmt T_CLOSE_P from_alias_clause?  | nest_from_clause from_alias_clause?
     ;

nest_from_clause :
        T_OPEN_P from_body_clause T_CLOSE_P
     ;

from_body_clause:
    from_table_clause (from_join_clause)*
     ;

from_join_clause :
       T_COMMA from_table_clause
     | from_join_type_clause from_table_clause (T_ON bool_expr)?
     ;

from_join_type_clause :
       T_INNER? T_JOIN
     | (T_LEFT | T_RIGHT | T_FULL) T_OUTER? T_JOIN
     | T_NATURAL T_JOIN
     ;

from_table_values_clause:
       T_TABLE T_OPEN_P T_VALUES from_table_values_row (T_COMMA from_table_values_row)* T_CLOSE_P from_alias_clause?
     ;

from_table_values_row:
       expr
     | T_OPEN_P expr (T_COMMA expr)* T_CLOSE_P
     ;

from_alias_clause :
       {!_input.LT(1).getText().equalsIgnoreCase("EXEC") &&
        !_input.LT(1).getText().equalsIgnoreCase("EXECUTE") &&
        !_input.LT(1).getText().equalsIgnoreCase("INNER") &&
        !_input.LT(1).getText().equalsIgnoreCase("LEFT") &&
        !_input.LT(1).getText().equalsIgnoreCase("GROUP") &&
        !_input.LT(1).getText().equalsIgnoreCase("ORDER") &&
        !_input.LT(1).getText().equalsIgnoreCase("LIMIT") &&
        !_input.LT(1).getText().equalsIgnoreCase("WITH")}?
//       T_AS? qident (T_OPEN_P L_ID (T_COMMA L_ID)* T_CLOSE_P)?
       T_AS? ident
     ;

table_name :
      qident
//      | expr_dot_property_access
     ;

where_clause :
       T_WHERE bool_expr
     ;

group_by_clause : // todo  https://help.aliyun.com/zh/maxcompute/user-guide/grouping-sets?spm=a2c4g.11186623.help-menu-27797.d_2_1_2_2_13.6cda78b88CYHQY#section-y4k-via-8pf
       T_GROUP T_BY ( T_ROLLUP T_OPEN_P  qident (T_COMMA qident)* T_CLOSE_P | qident (T_COMMA qident)*)
     ;

having_clause :
       T_HAVING bool_expr
     ;

order_by_clause :
       T_ORDER T_BY expr (T_ASC | T_DESC)? (T_COMMA expr (T_ASC | T_DESC)?)*
     ;


update_stmt :                              // UPDATE statement
       T_UPDATE update_table T_SET update_assignment where_clause? update_upsert?
     ;

update_assignment :
       assignment_stmt_item (T_COMMA assignment_stmt_item)*
     ;

update_table :
       (table_name from_clause? | T_OPEN_P select_stmt T_CLOSE_P) (T_AS? qident)?
     ;

update_upsert :
       T_ELSE insert_stmt
     ;

merge_stmt :                              // MERGE statement
       T_MERGE T_INTO merge_table T_USING merge_table T_ON bool_expr merge_condition+
     ;

merge_table :
       (table_name | (T_OPEN_P select_stmt T_CLOSE_P)) (T_AS? qident)?
     ;

merge_condition :
       T_WHEN T_NOT? T_MATCHED (T_AND bool_expr)? T_THEN merge_action
     | T_ELSE T_IGNORE
     ;

merge_action :
       T_INSERT insert_stmt_cols? T_VALUES insert_stmt_row
     | T_UPDATE T_SET assignment_stmt_item (T_COMMA assignment_stmt_item)* where_clause?
     | T_DELETE
     ;

delete_stmt :
       T_DELETE T_FROM? table_name delete_alias? pt_spec? (where_clause | T_ALL)?
     ;

delete_alias :
       {!_input.LT(1).getText().equalsIgnoreCase("ALL")}?
       T_AS? qident
     ;

describe_stmt :
       (T_DESCRIBE | T_DESC) table_name
     ;

bool_expr :                               // Boolean condition
       T_NOT? T_OPEN_P bool_expr T_CLOSE_P
     | bool_expr bool_expr_logical_operator bool_expr
     | bool_expr_atom
     ;

bool_expr_atom :
      bool_expr_unary
    | bool_expr_binary
    | expr
    ;

bool_expr_unary :
      expr T_IS T_NOT? T_NULL
    | expr T_BETWEEN expr T_AND expr
    | T_NOT? T_EXISTS T_OPEN_P select_stmt T_CLOSE_P
    | bool_expr_single_in
    | bool_expr_multi_in
    ;

bool_expr_single_in :
      expr T_NOT? T_IN T_OPEN_P ((expr (T_COMMA expr)*) | select_stmt) T_CLOSE_P
    ;

bool_expr_multi_in :
      T_OPEN_P expr (T_COMMA expr)* T_CLOSE_P T_NOT? T_IN T_OPEN_P select_stmt T_CLOSE_P
    ;

bool_expr_binary :
       expr bool_expr_binary_operator expr
     ;

bool_expr_logical_operator :
       T_AND
     | T_OR
     ;

bool_expr_binary_operator :
       T_EQUAL
     | T_EQUAL2
     | T_NOTEQUAL
     | T_NOTEQUAL2
     | T_LESS
     | T_LESSEQUAL
     | T_GREATER
     | T_GREATEREQUAL
     | T_NOT? (T_LIKE | T_RLIKE | T_REGEXP)
     ;

expr :
       expr interval_item
     | expr (T_MUL | T_DIV) expr
     | expr (T_ADD | T_SUB) expr
     | expr T_MOD expr
     | T_OPEN_P select_stmt T_CLOSE_P
     | (T_ADD | T_SUB)?  T_OPEN_P expr T_CLOSE_P
     | expr_interval
     | expr_concat
     | expr_atom
     | expr_dot
     | expr_case
     | expr_cursor_attribute
     | expr_agg_window_func
     | expr_spec_func
     | expr_func
     | expr_cast
     ;
expr_cast :
      T_DATETIME (L_S_STRING)
      ;
expr_atom :
       date_literal
     | timestamp_literal
     | bool_literal
     | dec_number
     | qident
     | string
     | int_number
     | null_const
//     | ident ident  UNKNOWN
     | '-'
     ;

expr_interval :
       T_INTERVAL expr interval_item
     ;
interval_item :
       T_DAY
     | T_DAYS
     | T_MICROSECOND
     | T_MICROSECONDS
     | T_SECOND
     | T_SECONDS
     ;

expr_concat :                  // String concatenation operator
       expr_concat_item (T_PIPE | T_CONCAT) expr_concat_item ((T_PIPE | T_CONCAT) expr_concat_item)*
     ;

expr_concat_item :
       T_OPEN_P expr T_CLOSE_P
     | expr_case
     | expr_agg_window_func
     | expr_spec_func
     | expr_dot
     | expr_func
     | expr_atom
     ;

expr_case :                    // CASE expression
       expr_case_simple
     | expr_case_searched
     ;

expr_case_simple :
       T_CASE expr (T_WHEN expr T_THEN expr)+ (T_ELSE expr)? T_END
     ;

expr_case_searched :
       T_CASE (T_WHEN bool_expr T_THEN bool_expr)+ (T_ELSE bool_expr)? T_END
     ;

expr_cursor_attribute :
      ident '%' (T_ISOPEN | T_FOUND | T_NOTFOUND)
    ;

// todo
expr_agg_window_func :
       (T_ADD | T_SUB)? ( T_AVG T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_COUNT T_OPEN_P ((expr_func_all_distinct? expr) | T_MUL) T_CLOSE_P expr_func_over_clause?
     | T_COUNT_BIG T_OPEN_P ((expr_func_all_distinct? expr) | T_MUL) T_CLOSE_P expr_func_over_clause?
     | T_CUME_DIST T_OPEN_P T_CLOSE_P expr_func_over_clause
     | T_DENSE_RANK T_OPEN_P T_CLOSE_P expr_func_over_clause
     | T_FIRST_VALUE T_OPEN_P expr T_CLOSE_P expr_func_over_clause
     | T_LAG T_OPEN_P expr (T_COMMA expr (T_COMMA expr)?)? T_CLOSE_P expr_func_over_clause
     | T_LAST_VALUE T_OPEN_P expr T_CLOSE_P expr_func_over_clause
     | T_LEAD T_OPEN_P expr (T_COMMA expr (T_COMMA expr)?)? T_CLOSE_P expr_func_over_clause
     | T_MAX T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_MIN T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_RANK T_OPEN_P T_CLOSE_P expr_func_over_clause
     | T_ROW_NUMBER T_OPEN_P T_CLOSE_P expr_func_over_clause
     | T_STDEV T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_SUM T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_VAR T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause?
     | T_VARIANCE T_OPEN_P expr_func_all_distinct? expr T_CLOSE_P expr_func_over_clause? )
     ;

expr_func_all_distinct :
       T_ALL
     | T_DISTINCT
     ;

expr_func_over_clause :
       T_OVER T_OPEN_P expr_func_partition_by_clause? order_by_clause? frame_clause? T_CLOSE_P
     ;

frame_clause :
        (T_ROWS|T_RANGE|T_GROUPS) frame_start frame_exclusion? | (T_ROWS|T_RANGE|T_GROUPS) T_BETWEEN frame_start T_AND frame_end frame_exclusion?
     ;
frame_exclusion :
      T_EXCLUDE (T_NO T_OTHERS | T_CURRENT T_ROW | T_GROUP T_TIES)
     ;
frame_start :
      (T_OFFSET | T_UNBOUNDED ) (T_PRECEDING | T_FOLLOWING) | T_CURRENT T_ROW
      ;
frame_end :
      frame_start
      ;
expr_func_partition_by_clause :
       T_PARTITION T_BY expr (T_COMMA expr)*
     ;

expr_spec_func :
       T_ACTIVITY_COUNT
     | T_CAST T_OPEN_P expr T_AS  dtype dtype_len? T_CLOSE_P
     | T_COUNT T_OPEN_P (expr | '*') T_CLOSE_P
     | T_CURRENT_DATE | T_CURRENT T_DATE
     | (T_CURRENT_TIMESTAMP | T_CURRENT T_TIMESTAMP) (T_OPEN_P expr T_CLOSE_P)?
     | T_CURRENT_USER | T_CURRENT T_USER
     | T_MAX_PART_STRING T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_MIN_PART_STRING T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_MAX_PART_INT T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_MIN_PART_INT T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_MAX_PART_DATE T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_MIN_PART_DATE T_OPEN_P expr (T_COMMA expr (T_COMMA expr T_EQUAL expr)*)? T_CLOSE_P
     | T_PART_COUNT T_OPEN_P expr (T_COMMA expr T_EQUAL expr)* T_CLOSE_P
     | T_PART_LOC T_OPEN_P expr (T_COMMA expr T_EQUAL expr)+ (T_COMMA expr)? T_CLOSE_P
     | T_TRIM T_OPEN_P expr T_CLOSE_P
     | T_SUBSTRING T_OPEN_P expr T_FROM expr (T_FOR expr)? T_CLOSE_P
     | T_SYSDATE
     | T_USER
     ;

expr_func :
       ident T_OPEN_P expr_func_params? T_CLOSE_P  | if_stmt | expr_case_simple

     ;

expr_dot :
       expr_dot_method_call | expr_dot_property_access
      ;

expr_dot_method_call :
       (ident | expr_func | expr_spec_func) T_DOT expr_func
      ;

expr_dot_property_access :
       (ident | expr_func) T_DOT ident
      ;

expr_func_params :
       func_param (T_COMMA func_param)*
     ;

func_param :
       {!_input.LT(1).getText().equalsIgnoreCase("INTO")}? T_DISTINCT? (ident T_EQUAL T_GREATER?)? expr
     ;

expr_select :
       select_stmt
     | expr
     ;

expr_file :
       file_name
     | expr
     ;


file_name :
       L_FILE | ('/' | '.' '/')? qident ('/' qident)*
     ;

date_literal :                             // DATE 'YYYY-MM-DD' literal
       T_DATE string
     ;

timestamp_literal :                       // TIMESTAMP 'YYYY-MM-DD HH:MI:SS.FFF' literal
       T_TIMESTAMP string
     ;

ident :
       '-'? (L_ID | non_reserved_words)
     ;

qident :                                  // qualified identifier e.g: table_name.col_name or db_name._table_name
       ident ('.'ident)*
     ;

string :                                   // String literal (single or double quoted)
       L_S_STRING                          # single_quotedString
     | L_D_STRING                          # double_quotedString
     ;

int_number :                               // Integer (positive or negative)
     ('-' | '+')? (L_INT | L_INT2)
     ;

dec_number :                               // Decimal number (positive or negative)
     ('-' | '+')? L_DEC
     ;

bool_literal :                            // Boolean literal
       T_TRUE
     | T_FALSE
     ;

null_const :                              // NULL constant
       T_NULL
     ;

non_reserved_words :                      // Tokens that are not reserved words and can be used as identifiers
       T_ACTION
     | T_MAJOR
     | T_ACTIVITY_COUNT
     | T_ADD2
     | T_ALL
     | T_ALLOCATE
     | T_ALTER
     | T_AND
     | T_ANSI_NULLS
     | T_ANSI_PADDING
     | T_AS
     | T_ASC
     | T_ASSOCIATE
     | T_AT
     | T_AUTO_INCREMENT
     | T_AVG
     | T_BATCHSIZE
     | T_BEGIN
     | T_BETWEEN
     | T_BIGINT
     | T_BINARY_DOUBLE
     | T_BINARY_FLOAT
     | T_BIT
     | T_BODY
     | T_BREAK
     | T_BULK
     | T_BY
     | T_BYTE
     | T_CALL
     | T_CALLER
     | T_CASCADE
     | T_CASE
     | T_CASESPECIFIC
     | T_CAST
     | T_CHAR
     | T_CHARACTER
     | T_CHARSET
     | T_CLIENT
     | T_CLOSE
     | T_CLUSTERED
     | T_CMP
     | T_COLLECT
     | T_COLLECTION
     | T_COLUMN
     | T_COMMENT
     | T_COMPRESS
     | T_CONSTANT
     | T_COPY
     | T_COMMIT
     | T_CONCAT
     | T_CONDITION
     | T_CONSTRAINT
     | T_CONTINUE
     | T_COUNT
     | T_COUNT_BIG
     | T_CREATE
     | T_CREATION
     | T_CREATOR
     | T_CS
     | T_CUME_DIST
     | T_CURRENT
     | T_CURRENT_DATE
     | T_CURRENT_SCHEMA
     | T_CURRENT_TIME_MILLIS
     | T_CURRENT_TIMESTAMP
     | T_CURRENT_USER
     | T_CURSOR
     | T_DATA
     | T_DATABASE
     | T_DATE
     | T_DATETIME
     | T_DAY
     | T_DAYS
     | T_DEC
     | T_DECIMAL
     | T_DECLARE
     | T_DEFAULT
     | T_DEFERRED
     | T_DEFINED
     | T_DEFINER
     | T_DEFINITION
     | T_DELETE
     | T_DELIMITED
     | T_DELIMITER
     | T_DENSE_RANK
     | T_DESC
     | T_DESCRIBE
     | T_DIAGNOSTICS
     | T_DIR
     | T_DIRECTORY
     | T_DISTINCT
     | T_DISTRIBUTE
     | T_DO
     | T_DOUBLE
     | T_DROP
     | T_DYNAMIC
     // T_ELSE reserved word
     // T_ELSEIF reserved word
     // T_ELSIF reserved word
     // T_END reserved word
     | T_ENABLE
     | T_ENGINE
     | T_ESCAPED
     | T_EXCEPT
     | T_EXEC
     | T_EXECUTE
     | T_EXCEPTION
     | T_EXCLUSIVE
     | T_EXISTS
     | T_EXIT
     | T_FALLBACK
     | T_FALSE
     | T_FETCH
     | T_FIELDS
     | T_FILE
     | T_FILES
     | T_FIRST_VALUE
     | T_FLOAT
     | T_FOR
     | T_FOREIGN
     | T_FORMAT
     | T_FOUND
//     | T_FROM
     | T_FULL
     | T_FUNCTION
     | T_GET
     | T_GLOBAL
     | T_GO
     | T_GRANT
     | T_GROUP
     | T_HANDLER
     | T_HASH
     | T_HAVING
     | T_HDFS
     | T_HIVE
     | T_HOST
     | T_IDENTITY
     | T_IF
     | T_IGNORE
     | T_IMMEDIATE
     | T_IN
     | T_INCLUDE
     | T_INDEX
     | T_INITRANS
     | T_INNER
     | T_INOUT
     | T_INSERT
     | T_INT
     | T_INT2
     | T_INT4
     | T_INT8
     | T_INTEGER
     | T_INTERSECT
     | T_INTERVAL
     | T_INTO
     | T_INVOKER
     | T_ITEMS
     | T_IS
     | T_ISOPEN
     | T_JOIN
     | T_KEEP
     | T_KEY
     | T_KEYS
     | T_LAG
     | T_LANGUAGE
     | T_LAST_VALUE
     | T_LEAD
     | T_LEAVE
     | T_LEFT
     | T_LIKE
     | T_LIMIT
     | T_LINES
     | T_LOCAL
     | T_LOCATION
     | T_LOCATOR
     | T_LOCATORS
     | T_LOCKS
     | T_LOG
     | T_LOGGED
     | T_LOGGING
     | T_LOOP
     | T_MAP
     | T_MATCHED
     | T_MAX
     | T_MAXTRANS
     | T_MERGE
     | T_MESSAGE_TEXT
     | T_MICROSECOND
     | T_MICROSECONDS
     | T_MIN
     | T_MULTISET
     | T_NCHAR
     | T_NEW
     | T_NVARCHAR
     | T_NO
     | T_NOCOMPRESS
     | T_NOCOUNT
     | T_NOLOGGING
     | T_NONE
     | T_NOT
     | T_NOTFOUND
     // T_NULL reserved word
     | T_NUMERIC
     | T_NUMBER
     | T_OBJECT
     | T_OFF
     | T_ON
     | T_ONLY
     | T_OPEN
     | T_OR
     | T_ORDER
     | T_OUT
     | T_OUTER
     | T_OVER
     | T_OVERWRITE
     | T_OWNER
     | T_OTHERS
     | T_PACKAGE
     | T_PART_COUNT
     | T_PART_LOC
     | T_PARTITION
     | T_PCTFREE
     | T_PCTUSED
     | T_PRECISION
     | T_PRESERVE
     | T_PRIMARY
     | T_PRINT
     | T_PROC
     | T_PROCEDURE
     | T_PWD
     | T_QUALIFY
     | T_QUERY_BAND
     | T_QUIT
     | T_QUOTED_IDENTIFIER
     | T_RAISE
     | T_RANK
     | T_REAL
     | T_REFERENCES
     | T_REGEXP
     | T_RR
     | T_REPLACE
     | T_RESIGNAL
     | T_RESTRICT
     | T_RESULT
     | T_RESULT_SET_LOCATOR
     | T_RETURN
     | T_RETURNS
     | T_REVERSE
     | T_RIGHT
     | T_RLIKE
     | T_RS
     | T_ROLE
     | T_ROLLBACK
     | T_ROW
     | T_ROWS
     | T_ROW_COUNT
     | T_ROW_NUMBER
     | T_SCHEMA
     | T_SECOND
     | T_SECONDS
     | T_SECURITY
     | T_SEGMENT
     | T_SELECT
     | T_SESSION
     | T_SESSIONS
     | T_SET
     | T_SETS
     | T_SHARE
     | T_SIGNAL
     | T_SIMPLE_DOUBLE
     | T_SIMPLE_FLOAT
     | T_SMALLDATETIME
     | T_SMALLINT
     | T_SQL
     | T_SQLEXCEPTION
     | T_SQLINSERT
     | T_SQLSTATE
     | T_SQLWARNING
     | T_STATS
     | T_STATISTICS
     | T_STEP
     | T_STDEV
     | T_STORAGE
     | T_STORED
     | T_STRING
     | T_SUBDIR
     | T_SUBSTRING
     | T_SUM
     | T_SUMMARY
     | T_SYSDATE
     | T_SYS_REFCURSOR
     | T_TABLE
     | T_TABLESPACE
     | T_TEMPORARY
     | T_TERMINATED
     | T_TEXTIMAGE_ON
     | T_THEN
     | T_TIMESTAMP
     | T_TITLE
     | T_TO
     | T_TOP
     | T_TRANSACTION
     | T_TRIM
     | T_TRUE
     | T_TRUNCATE
     | T_TYPE
     // T_UNION reserved word
     | T_UNIQUE
     | T_UPDATE
     | T_UR
     | T_USE
     | T_USER
     | T_USING
     | T_VALUE
     | T_VALUES
     | T_VAR
     | T_VARCHAR
     | T_VARCHAR2
     | T_VARYING
     | T_VARIANCE
     | T_VOLATILE
     // T_WHEN reserved word
     // T_WHERE reserved word
     | T_WHILE
     | T_WITH
     | T_WITHOUT
     | T_WORK
     | T_XACT_ABORT
     | T_XML
     | T_YES
     | T_TIME
     | T_LIST
     | T_ROLES
     | T_USERS
     | T_TRUSTEDPROJECTS
     ;

// Lexer rules
T_ACTION          : A C T I O N ;
T_ADD2            : A D D ;
T_ALL             : A L L ;
T_ALLOCATE        : A L L O C A T E ;
T_ALTER           : A L T E R ;
T_AND             : A N D ;
T_ANSI_NULLS      : A N S I '_' N U L L S ;
T_ANSI_PADDING    : A N S I '_' P A D D I N G ;
T_AS              : A S ;
T_ASC             : A S C ;
T_ASSOCIATE       : A S S O C I A T E ;
T_AT              : A T ;
T_AUTO_INCREMENT  : A U T O '_' I N C R E M E N T ;
T_AUTO            : A U T O ;
T_AVG             : A V G ;
T_BATCHSIZE       : B A T C H S I Z E ;
T_BEGIN           : B E G I N ;
T_BETWEEN         : B E T W E E N ;
T_BIGINT          : B I G I N T ;
T_BINARY_DOUBLE   : B I N A R Y '_' D O U B L E ;
T_BINARY_FLOAT    : B I N A R Y '_' F L O A T ;
T_BINARY_INTEGER  : B I N A R Y '_' I N T E G E R ;
T_BIT             : B I T ;
T_BODY            : B O D Y ;
T_BREAK           : B R E A K ;
T_BULK            : B U L K ;
T_BY              : B Y ;
T_BYTE            : B Y T E ;
T_CALL            : C A L L ;
T_CALLER          : C A L L E R ;
T_CASCADE         : C A S C A D E ;
T_CASE            : C A S E ;
T_CASESPECIFIC    : C A S E S P E C I F I C ;
T_CAST            : C A S T ;
T_CHAR            : C H A R ;
T_CHARACTER       : C H A R A C T E R ;
T_CHARSET         : C H A R S E T ;
T_CLIENT          : C L I E N T ;
T_CLOSE           : C L O S E ;
T_CLUSTERED       : C L U S T E R E D;
T_RENAME          : R E N A M E;
T_CMP             : C M P ;
T_COLLECT         : C O L L E C T ;
T_COLLECTION      : C O L L E C T I O N ;
T_COLUMN          : C O L U M N ;
T_COLUMNS          : C O L U M N S;
T_COMMENT         : C O M M E N T;
T_TOUCH           : T O U C H;
T_CONSTANT        : C O N S T A N T ;
T_COMMIT          : C O M M I T ;
T_COMPRESS        : C O M P R E S S ;
T_CONCAT          : C O N C A T;
T_CONDITION       : C O N D I T I O N ;
T_CONSTRAINT      : C O N S T R A I N T ;
T_CONTINUE        : C O N T I N U E ;
T_COPY            : C O P Y ;
T_COUNT           : C O U N T ;
T_COUNT_BIG       : C O U N T '_' B I G;
T_CREATE          : C R E A T E ;
T_ANALYZE         : A N A L Y Z E;
T_COMPUTE         : C O M P U T E;
T_SHOW            : S H O W;
T_HISTORY         : H I S T O R Y;
T_MATERIALIZED    : M A T E R I A L I Z E D;
T_REBUILD         : R E B U I L D;
T_LIFECYCLE       : L I F E C Y C L E;
T_BUILD           : B U I L D;
T_DISABLE         : D I S A B L E;
T_REFRESH         : R E F R E S H;
T_EVERY           : E V E R Y;
T_MINUTES         : M I N U T E S;
T_HOURS           : H O U R S;
T_REWRITE         : R E W R I T E;
T_CREATION        : C R E A T I O N ;
T_CREATOR         : C R E A T O R ;
T_CS              : C S;
T_TBLPROPERTIES    : T B L P R O P E R T I E S;
T_CURRENT         : C U R R E N T ;
T_CURRENT_SCHEMA  : C U R R E N T '_' S C H E M A ;
T_CURSOR          : C U R S O R ;
T_DATABASE        : D A T A B A S E ;
T_DATA            : D A T A ;
T_DATE            : D A T E ;
T_DATETIME        : D A T E T I M E ;
T_DAY             : D A Y ;
T_DAYS            : D A Y S ;
T_DEC             : D E C ;
T_DECIMAL         : D E C I M A L ;
T_DECLARE         : D E C L A R E ;
T_DEFAULT         : D E F A U L T ;
T_DEFERRED        : D E F E R R E D ;
T_DEFINED         : D E F I N E D ;
T_DEFINER         : D E F I N E R ;
T_DEFINITION      : D E F I N I T I O N ;
T_DELETE          : D E L E T E ;
T_DELIMITED       : D E L I M I T E D ;
T_DELIMITER       : D E L I M I T E R ;
T_DESC            : D E S C ;
T_DESCRIBE        : D E S C R I B E ;
T_DIAGNOSTICS     : D I A G N O S T I C S ;
T_DIR             : D I R ;
T_DIRECTORY       : D I R E C T O R Y ;
T_DISTINCT        : D I S T I N C T ;
T_DISTRIBUTE      : D I S T R I B U T E ;
T_DO              : D O ;
T_DOUBLE          : D O U B L E ;
T_DROP            : D R O P ;
T_CHANGE          : C H A N G E;
T_COMPACT         : C O M P A C T;
T_MAJOR           : M A J O R;
T_DYNAMIC         : D Y N A M I C ;
T_ELSE            : E L S E ;
T_ELSEIF          : E L S E I F ;
T_ELSIF           : E L S I F ;
T_ENABLE          : E N A B L E ;
T_END             : E N D ;
T_ENGINE          : E N G I N E ;
T_ESCAPED         : E S C A P E D ;
T_EXCEPT          : E X C E P T ;
T_EXEC            : E X E C ;
T_EXECUTE         : E X E C U T E ;
T_EXCEPTION       : E X C E P T I O N ;
T_EXCLUSIVE       : E X C L U S I V E ;
T_EXISTS          : E X I S T S ;
T_PURGE           : P U R G E;
T_EXIT            : E X I T ;
T_FALLBACK        : F A L L B A C K ;
T_FALSE           : F A L S E ;
T_FETCH           : F E T C H ;
T_FIELDS          : F I E L D S ;
T_FILE            : F I L E ;
T_FILES           : F I L E S ;
T_FLOAT           : F L O A T ;
T_FOR             : F O R ;
T_FOREIGN         : F O R E I G N ;
T_FORMAT          : F O R M A T ;
T_FOUND           : F O U N D ;
T_FROM            : F R O M ;
T_FULL            : F U L L ;
T_FUNCTION        : F U N C T I O N ;
T_GET             : G E T ;
T_GLOBAL          : G L O B A L ;
T_GO              : G O ;
T_GRANT           : G R A N T ;
T_GROUP           : G R O U P ;
T_ROLLUP          : R O L L U P;
T_HANDLER         : H A N D L E R ;
T_HASH            : H A S H ;
T_HAVING          : H A V I N G ;
T_HDFS            : H D F S ;
T_HIVE            : H I V E ;
T_HOST            : H O S T ;
T_IDENTITY        : I D E N T I T Y ;
T_IF              : I F ;
T_IGNORE          : I G N O R E ;
T_IMMEDIATE       : I M M E D I A T E ;
T_IN              : I N ;
T_INCLUDE         : I N C L U D E ;
T_INDEX           : I N D E X ;
T_INITRANS        : I N I T R A N S ;
T_INNER           : I N N E R ;
T_INOUT           : I N O U T;
T_INSERT          : I N S E R T ;
T_INT             : I N T ;
T_INT2            : I N T '2';
T_INT4            : I N T '4';
T_INT8            : I N T '8';
T_INTEGER         : I N T E G E R ;
T_INTERSECT       : I N T E R S E C T ;
T_MINUS           : M I N U S ;
T_INTERVAL        : I N T E R V A L ;
T_INTO            : I N T O ;
T_INVOKER         : I N V O K E R ;
T_IS              : I S ;
T_ISOPEN          : I S O P E N ;
T_ITEMS           : I T E M S ;
T_JOIN            : J O I N ;
T_KEEP            : K E E P;
T_KEY             : K E Y ;
T_KEYS            : K E Y S ;
T_LANGUAGE        : L A N G U A G E ;
T_LEAVE           : L E A V E ;
T_LEFT            : L E F T ;
T_LIKE            : L I K E ;
T_LIMIT           : L I M I T ;
T_LINES           : L I N E S ;
T_LOCAL           : L O C A L ;
T_LOCATION        : L O C A T I O N ;
T_LOCATOR         : L O C A T O R ;
T_LOCATORS        : L O C A T O R S ;
T_LOCKS           : L O C K S ;
T_LOG             : L O G ;
T_LOGGED          : L O G G E D ;
T_LOGGING         : L O G G I N G ;
T_LOOP            : L O O P ;
T_MAP             : M A P ;
T_MATCHED         : M A T C H E D ;
T_MAX             : M A X ;
T_MAXTRANS        : M A X T R A N S ;
T_MERGE           : M E R G E ;
T_MESSAGE_TEXT    : M E S S A G E '_' T E X T ;
T_MICROSECOND     : M I C R O S E C O N D ;
T_MICROSECONDS    : M I C R O S E C O N D S;
T_MILLIS          : M I L L I S ;
T_MIN             : M I N ;
T_MULTISET        : M U L T I S E T ;
T_NCHAR           : N C H A R ;
T_NEW             : N E W ;
T_NVARCHAR        : N V A R C H A R ;
T_NO              : N O ;
T_NOCOUNT         : N O C O U N T ;
T_NOCOMPRESS      : N O C O M P R E S S ;
T_NOLOGGING       : N O L O G G I N G ;
T_NONE            : N O N E ;
T_NOT             : N O T ;
T_NOTFOUND        : N O T F O U N D ;
T_NULL            : N U L L ;
T_NUMERIC         : N U M E R I C ;
T_NUMBER          : N U M B E R ;
T_OBJECT          : O B J E C T ;
T_OFF             : O F F ;
T_OF              : O F ;
T_ON              : O N ;
T_ONLY            : O N L Y ;
T_OPEN            : O P E N ;
T_OR              : O R ;
T_ORDER           : O R D E R;
T_ZORDER          : Z O R D E R;
T_OUT             : O U T ;
T_OUTER           : O U T E R ;
T_NATURAL         : N A T U R A L;
T_OVER            : O V E R ;
T_OVERWRITE       : O V E R W R I T E ;
T_OWNER           : O W N E R ;
T_PACKAGE         : P A C K A G E ;
T_PARTITION       : P A R T I T I O N ;
T_PARTITIONS      : P A R T I T I O N S;
T_PARTITIONED     : P A R T I T I O N E D;
T_PCTFREE         : P C T F R E E ;
T_PCTUSED         : P C T U S E D ;
T_PLS_INTEGER     : P L S '_' I N T E G E R ;
T_PRECISION       : P R E C I S I O N ;
T_PRESERVE        : P R E S E R V E ;
T_PRIMARY         : P R I M A R Y ;
T_PRINT           : P R I N T ;
T_PROC            : P R O C ;
T_PROCEDURE       : P R O C E D U R E ;
T_QUALIFY         : Q U A L I F Y ;
T_QUERY_BAND      : Q U E R Y '_' B A N D ;
T_QUIT            : Q U I T ;
T_QUOTED_IDENTIFIER : Q U O T E D '_' I D E N T I F I E R ;
T_RAISE           : R A I S E ;
T_REAL            : R E A L ;
T_REFERENCES      : R E F E R E N C E S ;
T_REGEXP          : R E G E X P ;
T_REPLACE         : R E P L A C E ;
T_RESIGNAL        : R E S I G N A L ;
T_RESTRICT        : R E S T R I C T ;
T_RESULT          : R E S U L T ;
T_RESULT_SET_LOCATOR : R E S U L T '_' S E T '_' L O C A T O R ;
T_RETURN          : R E T U R N ;
T_RETURNS         : R E T U R N S ;
T_REVERSE         : R E V E R S E ;
T_RIGHT           : R I G H T ;
T_RLIKE           : R L I K E ;
T_ROLE            : R O L E ;
T_ROLES           : R O L E S;
T_LIST            : L I S T;
T_ROLLBACK        : R O L L B A C K ;
T_ROW             : R O W ;
T_ROWS            : R O W S ;
T_ROWTYPE         : R O W T Y P E ;
T_ROW_COUNT       : R O W '_' C O U N T ;
T_RR              : R R;
T_RS              : R S ;
T_PWD             : P W D ;
T_TRIM            : T R I M ;
T_SCHEMA          : S C H E M A ;
T_SECOND          : S E C O N D ;
T_SECONDS         : S E C O N D S;
T_SECURITY        : S E C U R I T Y ;
T_SEGMENT         : S E G M E N T ;
T_SELECT          : S E L E C T ;
T_SET             : S E T ;
T_SESSION         : S E S S I O N ;
T_SESSIONS        : S E S S I O N S ;
T_SETS            : S E T S;
T_SHARE           : S H A R E ;
T_SIGNAL          : S I G N A L ;
T_SIMPLE_DOUBLE   : S I M P L E '_' D O U B L E ;
T_SIMPLE_FLOAT    : S I M P L E '_' F L O A T ;
T_SIMPLE_INTEGER  : S I M P L E '_' I N T E G E R ;
T_SMALLDATETIME   : S M A L L D A T E T I M E ;
T_SMALLINT        : S M A L L I N T ;
T_SQL             : S Q L ;
T_SQLEXCEPTION    : S Q L E X C E P T I O N ;
T_SQLINSERT       : S Q L I N S E R T ;
T_SQLSTATE        : S Q L S T A T E ;
T_SQLWARNING      : S Q L W A R N I N G ;
T_STATS           : S T A T S ;
T_STATISTICS      : S T A T I S T I C S ;
T_STATISTIC      : S T A T I S T I C;
T_STEP            : S T E P ;
T_STORAGE         : S T O R A G E ;
T_STORED          : S T O R E D ;
T_STRING          : S T R I N G ;
T_SUBDIR          : S U B D I R ;
T_SUBSTRING       : S U B S T R I N G ;
T_SUM             : S U M ;
T_SUMMARY         : S U M M A R Y ;
T_SYS_REFCURSOR   : S Y S '_' R E F C U R S O R ;
T_TABLE           : T A B L E ;
T_TABLES          : T A B L E S;
T_TABLESPACE      : T A B L E S P A C E ;
T_TEMPORARY       : T E M P O R A R Y ;
T_TERMINATED      : T E R M I N A T E D ;
T_TEXTIMAGE_ON    : T E X T I M A G E '_' O N ;
T_THEN            : T H E N ;
T_TIME            : T I M E ;
T_TIMESTAMP       : T I M E S T A M P ;
T_TINYINT         : T I N Y I N T ;
T_TITLE           : T I T L E ;
T_TO              : T O ;
T_TOP             : T O P ;
T_TRANSACTION     : T R A N S A C T I O N ;
T_TRUE            : T R U E ;
T_TRUNCATE        : T R U N C A T E;
T_TYPE            : T Y P E ;
T_UNION           : U N I O N ;
T_UNIQUE          : U N I Q U E ;
T_UPDATE          : U P D A T E ;
T_UR              : U R ;
T_USE             : U S E ;
T_USING           : U S I N G ;
T_VALUE           : V A L U E ;
T_VALUES          : V A L U E S ;
T_VAR             : V A R ;
T_VARCHAR         : V A R C H A R ;
T_VARCHAR2        : V A R C H A R '2' ;
T_VARYING         : V A R Y I N G ;
T_VOLATILE        : V O L A T I L E ;
T_WHEN            : W H E N ;
T_WHERE           : W H E R E ;
T_WHILE           : W H I L E ;
T_WITH            : W I T H ;
T_WITHOUT         : W I T H O U T ;
T_WORK            : W O R K ;
T_XACT_ABORT      : X A C T '_' A B O R T ;
T_XML             : X M L ;
T_YES             : Y E S ;

// Functions with specific syntax
T_ACTIVITY_COUNT       : A C T I V I T Y '_' C O U N T ;
T_CUME_DIST            : C U M E '_' D I S T ;
T_CURRENT_DATE         : C U R R E N T '_' D A T E ;
T_CURRENT_TIME_MILLIS  : C U R R E N T '_' T I M E '_' M I L L I S ;
T_CURRENT_TIMESTAMP    : C U R R E N T '_' T I M E S T A M P ;
T_CURRENT_USER         : C U R R E N T '_' U S E R ;
T_DENSE_RANK           : D E N S E '_' R A N K ;
T_FIRST_VALUE          : F I R S T '_' V A L U E;
T_LAG                  : L A G ;
T_LAST_VALUE           : L A S T '_' V A L U E;
T_LEAD                 : L E A D ;
T_MAX_PART_STRING      : M A X '_' P A R T '_' S T R I N G ;
T_MIN_PART_STRING      : M I N '_' P A R T '_' S T R I N G ;
T_MAX_PART_INT         : M A X '_' P A R T '_' I N T ;
T_MIN_PART_INT         : M I N '_' P A R T '_' I N T ;
T_MAX_PART_DATE        : M A X '_' P A R T '_' D A T E ;
T_MIN_PART_DATE        : M I N '_' P A R T '_' D A T E ;
T_PART_COUNT           : P A R T '_' C O U N T ;
T_PART_LOC             : P A R T '_' L O C ;
T_RANK                 : R A N K ;
T_ROW_NUMBER           : R O W '_' N U M B E R;
T_STDEV                : S T D E V ;
T_SYSDATE              : S Y S D A T E ;
T_VARIANCE             : V A R I A N C E ;
T_USER                 : U S E R;
T_USERS                 : U S E R S;
T_TRUSTEDPROJECTS       : T R U S T E D P R O J E C T S;
T_RANGE                 : R A N G E;
T_SORTED                : S O R T E D;
T_SORT                  : S O R T;
T_BUCKETS               : B U C K E T S;
T_GROUPS                 : G R O U P S;
T_EXCLUDE : E X C L U D E;
T_OTHERS : O T H E R S;
T_TIES : T I E S;
T_OFFSET : O F F S E T;
T_UNBOUNDED : U N B O U N D E D;
T_PRECEDING : P R E C E D I N G;
T_FOLLOWING : F O L L O W I N G;
T_LATERAL : L A T E R A L;
T_VIEW : V I E W;

T_ADD          : '+' ;
T_COLON        : ':' ;
T_COMMA        : ',' ;
T_PIPE         : '||' ;
T_DIV          : '/' ;
T_DOT          : '.' ;
T_DOT2         : '..' ;
T_EQUAL        : '=' ;
T_EQUAL2       : '==' ;
T_NOTEQUAL     : '<>' ;
T_NOTEQUAL2    : '!=' ;
T_GREATER      : '>' ;
T_GREATEREQUAL : '>=' ;
T_LESS         : '<' ;
T_LESSEQUAL    : '<=' ;
T_MUL          : '*' ;
T_OPEN_B       : '{' ;
T_OPEN_P       : '(' ;
T_OPEN_SB      : '[' ;
T_CLOSE_B      : '}' ;
T_CLOSE_P      : ')' ;
T_CLOSE_SB     : ']' ;
T_SEMICOLON    : ';' ;
T_SUB          : '-' ;
T_MOD          : '%' ;


L_D_STRING  : '"' (L_STR_ESC_D | .)*? '"'
            ;                             // Double quoted string literal

L_ID        : L_ID_PART                                                // Identifier
            ;
L_S_STRING  : '\'' ('\\' . | '\'\'' | ~('\'' | '\\'))* '\''           // Single quoted string literal
            ;

L_INT       : L_DIGIT+ ;                                               // Integer
L_INT2      : L_DIGIT+ (S|Y|L)?   ;
L_DEC       : L_DIGIT+ '.' ~'.' L_DIGIT*                               // Decimal number

            ;
L_WS        : L_BLANK+ -> channel(HIDDEN) ;                                       // Whitespace
L_M_COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;                       // Multiline comment
L_S_COMMENT : ('--' | '//')  .*? '\r'? '\n' -> channel(HIDDEN) ;       // Single line comment

L_FILE      : ([a-zA-Z\u4e00-\u9fa5] ':' '\\'?)? L_ID ('\\' L_ID)*                  // File path (a/b/c Linux path causes conflicts with division operator and handled at parser level)
            ;

L_LABEL     : ([a-zA-Z\u4e00-\u9fa5] | L_DIGIT | '_')* ':'
            ;

fragment
L_ID_PART  :
             [a-zA-Z\u4e00-\u9fa5] ([a-zA-Z\u4e00-\u9fa5] | L_DIGIT | '_')*                           // Identifier part
            | '$' '{' .*? '}'
            | ('_' | '@' | ':' | '#' | '$') ([a-zA-Z\u4e00-\u9fa5] | L_DIGIT | '_' | '@' | ':' | '#' | '$')+     // (at least one char must follow special char)
            | '`' .*? '`'
            ;
fragment
L_STR_ESC_D :                                                          // Double quoted string escape sequence
              '""' | '\\"'
            ;
fragment
L_DIGIT     : [0-9]                                                    // Digit
            ;
fragment
L_BLANK     : (' ' | '\t' | '\r' | '\n')
            ;

// Support case-insensitive keywords and allowing case-sensitive identifiers
fragment A : ('a'|'A') ;
fragment B : ('b'|'B') ;
fragment C : ('c'|'C') ;
fragment D : ('d'|'D') ;
fragment E : ('e'|'E') ;
fragment F : ('f'|'F') ;
fragment G : ('g'|'G') ;
fragment H : ('h'|'H') ;
fragment I : ('i'|'I') ;
fragment J : ('j'|'J') ;
fragment K : ('k'|'K') ;
fragment L : ('l'|'L') ;
fragment M : ('m'|'M') ;
fragment N : ('n'|'N') ;
fragment O : ('o'|'O') ;
fragment P : ('p'|'P') ;
fragment Q : ('q'|'Q') ;
fragment R : ('r'|'R') ;
fragment S : ('s'|'S') ;
fragment T : ('t'|'T') ;
fragment U : ('u'|'U') ;
fragment V : ('v'|'V') ;
fragment W : ('w'|'W') ;
fragment X : ('x'|'X') ;
fragment Y : ('y'|'Y') ;
fragment Z : ('z'|'Z') ;
