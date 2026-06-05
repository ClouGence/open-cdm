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
package com.clougence.clouddm.dsfamily.definition.ui.browser;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.menus.UiMenuDef;

public class RdbUiMenuDef extends UiMenuDef {
    // create table       < 创建相关
    // create view        <
    // create trigger     <
    // --- separator ---
    // copy name          < 常用操作
    // refresh            <     如：改名/修改/编辑数据/复制/粘贴/编辑/刷新
    // --- separator ---
    // compile            < 其它操作
    // Enable             <
    // Disable            <
    // Properties         <
    // --- separator ---
    // truncate           < 危险操作
    // Drop               <
    // --- separator ---
    // table get ddl      < 获取脚本/导入导出数据/生成数据
    // import data
    // export data
    // generate table data
    // generate table data

    public final static List<String> DEFAULT_RDB_INSTANCE_THREE_LAYERS = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_CATALOG_CREATE,         // create database
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_INSTANCE_RENAME,        // Modify Name
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_COPY_JDBC,              // Copy JdbcUrl
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_INSTANCE_DROP           // delete DataSource
    );

    public final static List<String> DEFAULT_RDB_INSTANCE_TWO_LAYERS   = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_SCHEMA_CREATE,          // create schema
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_INSTANCE_RENAME,        // Modify Name
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_COPY_JDBC,              // Copy JdbcUrl
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_INSTANCE_DROP           // delete DataSource
    );

    public final static List<String> DEFAULT_RDB_CATALOG               = Arrays.asList(//
            MENU_BROWSE_CONSOLE,                // create new console
            MENU_BROWSE_CATALOG_CREATE,         // create database
            MENU_BROWSE_SCHEMA_CREATE,          // create schema
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_CATALOG_RENAME,         // Modify Name
            MENU_BROWSE_COPY_NAME,              // copy name
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_CATALOG_DROP            // drop database
    );

    public final static List<String> DEFAULT_RDB_SCHEMA                = Arrays.asList(//
            MENU_BROWSE_CONSOLE,                // create new console
            MENU_BROWSE_SCHEMA_CREATE,          // create schema
            MENU_BROWSE_TABLE_CREATE,           // create table
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_SCHEMA_RENAME,          // Modify Name
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_SCHEMA_DROP             // drop schema
    );

    public final static List<String> DEFAULT_RDB_EXTERNAL_CATALOG      = Arrays.asList(//
            //MENU_BROWSE_CONSOLE,              // create new console
            //MENU_BROWSE_EXTERNAL_CATALOG_CREATE,// create database
            //MENU_BROWSE_EXTERNAL_SCHEMA_CREATE, // create schema
            //MENU_SEPARATOR,                   // --- separator ---
            //MENU_BROWSE_EXTERNAL_CATALOG_RENAME,// Modify Name
            MENU_BROWSE_COPY_NAME,              // copy name
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR                      // --- separator ---
    //MENU_BROWSE_EXTERNAL_CATALOG_DROP         // drop database
    );
    public final static List<String> DEFAULT_RDB_EXTERNAL_SCHEMA       = Arrays.asList(//
            MENU_BROWSE_CONSOLE,                // create new console
            //MENU_BROWSE_EXTERNAL_SCHEMA_CREATE, // create schema
            MENU_SEPARATOR,                     // --- separator ---
            //MENU_BROWSE_EXTERNAL_SCHEMA_RENAME, // Modify Name
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR                      // --- separator ---
    //MENU_BROWSE_EXTERNAL_SCHEMA_DROP          // drop schema
    );

    public final static List<String> DEFAULT_RDB_TABLE                 = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_TABLE_CREATE,           // create table
            MENU_BROWSE_TRIGGER_CREATE,         // create trigger
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_RENAME,           // Rename
            MENU_BROWSE_TABLE_ALTER,            // Alter Table
            MENU_BROWSE_TABLE_DATA,             // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_TRUNCATE,         // truncate table
            MENU_BROWSE_TABLE_DROP,             // drop table
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_GET_DDL,          // request or generate DDL
            // MENU_BROWSE_TABLE_IMPORT,        // import data
            // MENU_BROWSE_TABLE_EXPORT,        // export data
            MENU_BROWSE_TABLE_FAKER,            // generate table data
            MENU_BROWSE_TABLE_FAKER_INCREMENT   // generate table data
    );

    public final static List<String> DEFAULT_RDB_VIEW                  = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_VIEW_CREATE,            // create view
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_VIEW_RENAME,            // Rename
            MENU_BROWSE_VIEW_ALTER,             // Alter View
            MENU_BROWSE_VIEW_DATA,              // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_VIEW_COMPILE,           // compile view
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_VIEW_DROP,              // drop view
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_VIEW_REQUEST            // request or generate DDL
    //      MENU_BROWSE_VIEW_EXPORT,            // export data
    );

    public final static List<String> DEFAULT_RDB_COLUMN                = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_COLUMN_CREATE,       // create column
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_COLUMN_RENAME,       // Rename
            // MENU_BROWSE_COLUMN_ALTER,        // Alter column (Table Editor)
            MENU_BROWSE_TABLE_DATA,             // Editor Data  (Table Data Editor)
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR                      // --- separator ---
    // MENU_BROWSE_PROPERTY,                    // Properties
    // MENU_BROWSE_COLUMN_DROP,                 // drop column
    );

    public final static List<String> DEFAULT_RDB_MATERIALIZED          = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_MATERIALIZED_CREATE, // create column
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_MATERIALIZED_RENAME  // Rename
            // MENU_BROWSE_MATERIALIZED_ALTER   // Alter View
            // MENU_BROWSE_MATERIALIZED_DATA,   // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_MATERIALIZED_DROP,      // drop view
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_MATERIALIZED_REQUEST    // request or generate DDL
    );

    public final static List<String> DEFAULT_RDB_DBLINK                = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_DBLINK_CREATE,          // create db link
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_DBLINK_TEST,            // test dbLink
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_DBLINK_DROP             // drop
    );

    public final static List<String> DEFAULT_RDB_JOB                   = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_JOB_CREATE,             // create
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_JOB_ALTER,              // alter
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_JOB_ENABLE,             // Enable Join
            MENU_BROWSE_JOB_DISABLE,            // Disable Join
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_JOB_DROP                // drop
    );

    public final static List<String> DEFAULT_RDB_SCHEDULE_JOB          = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_SCHEDULE_CREATE,        // create schedule
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_SCHEDULE_RUN,           // Run
            MENU_BROWSE_SCHEDULE_ENABLE,        // Enable
            MENU_BROWSE_SCHEDULE_DISABLE,       // Disable
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_SCHEDULE_DROP           // drop
    );

    public final static List<String> DEFAULT_RDB_SEQUENCE              = Arrays.asList(//          //
            // MENU_BROWSE_CONSOLE,             // create new console
            //MENU_BROWSE_SEQUENCE_CREATE,      // create schedule
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR                      // --- separator ---
    //      MENU_BROWSE_SEQUENCE_DROP           // drop
    );

    public final static List<String> DEFAULT_RDB_KEY                   = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_PRIMARY_CREATE,      // create primary key
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_PRIMARY_RENAME,      // Rename
            // MENU_BROWSE_PRIMARY_ALTER,       // Alter primary key
            MENU_BROWSE_TABLE_DATA,             // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_TRUNCATE,         // truncate table
            // MENU_BROWSE_PRIMARY_DROP,        // drop primary key
            MENU_SEPARATOR                      // --- separator ---
    );

    public final static List<String> DEFAULT_RDB_INDEX                 = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_INDEX_CREATE,        // create index
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_INDEX_RENAME,        // rename index
            // MENU_BROWSE_INDEX_ALTER,         // alter index
            MENU_BROWSE_TABLE_DATA,             // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_TRUNCATE,         // truncate table
            MENU_BROWSE_INDEX_DROP              // drop index
    );

    public final static List<String> DEFAULT_RDB_PARTITION             = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_PARTITION_CREATE,    // create partition
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_PARTITION_RENAME,    // rename index
            // MENU_BROWSE_PARTITION_ALTER,     // alter index
            MENU_BROWSE_TABLE_DATA,             // Editor Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_TRUNCATE          // truncate table
    //      MENU_BROWSE_PARTITION_DROP,         // drop partition
    );

    public final static List<String> DEFAULT_RDB_TRIGGER               = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_TRIGGER_CREATE,         // create trigger
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TRIGGER_ALTER,          // alter trigger
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TRIGGER_COMPILE,        // compile trigger
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TRIGGER_DROP,           // drop trigger
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TRIGGER_REQUEST         // request or generate DDL
    );

    public final static List<String> DEFAULT_RDB_PROCEDURE             = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_PROCEDURE_CREATE,       // alter procedure
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROCEDURE_ALTER,        // Alter procedure
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROCEDURE_COMPILE,      // compile
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROCEDURE_DROP,         // drop procedure
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROCEDURE_REQUEST       // request or generate DDL
    );

    public final static List<String> DEFAULT_RDB_FUNCTION              = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_FUNCTION_CREATE,        // create function
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_FUNCTION_ALTER,         // alter function
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_FUNCTION_COMPILE,       // Compile
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_FUNCTION_DROP,          // drop trigger
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_FUNCTION_REQUEST        // request or generate DDL
    );

    public final static List<String> DEFAULT_RDB_SYNONYM               = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            // MENU_BROWSE_SYNONYM_CREATE,      // create function
            MENU_SEPARATOR,                     // --- separator ---
            // MENU_BROWSE_SYNONYM_RENAME,      // Rename
            // MENU_BROWSE_SYNONYM_ALTER,       // Alter View
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_SYNONYM_REQUEST         // request or generate DDL
    );

    public final static List<String> DEFAULT_RDB_CONSTRAINT            = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_CONSTRAINT_ENABLE,      // Enable
            MENU_BROWSE_CONSTRAINT_DISABLE      // Disable
    );

    public final static List<String> DEFAULT_RDB_USER                  = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_USER_DROP               // Drop
    );

    public final static List<String> DEFAULT_RDB_ROLE                  = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY                // Properties
    );

    public final static List<String> DEFAULT_RDB_EXTERNAL_TABLE        = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_TABLE_DROP              // drop
    );

    public final static List<String> DEFAULT_CACHE_KEY                 = Arrays.asList(//
            // MENU_BROWSE_CONSOLE,             // create new console
            MENU_BROWSE_KEY_CREATE,             // create Key
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_KEY_RENAME,             // Rename
            MENU_BROWSE_KEY_ALTER,              // Alter Key
            MENU_BROWSE_KEY_DATA,               // Key Data
            MENU_BROWSE_COPY_NAME,              // Copy
            MENU_BROWSE_REFRESH,                // Refresh
            MENU_BROWSE_PERMISSIONS,            // Permissions
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_PROPERTY,               // Properties
            MENU_SEPARATOR,                     // --- separator ---
            MENU_BROWSE_KEY_TRUNCATE,           // truncate table
            MENU_BROWSE_KEY_DROP,               // drop table
            MENU_SEPARATOR                      // --- separator ---
    // MENU_BROWSE_KEY_IMPORT,                  // import data
    // MENU_BROWSE_KEY_EXPORT,                  // export data
    );
}
