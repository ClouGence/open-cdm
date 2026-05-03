package com.clougence.clouddm.sdk.ui.editor.table;

import java.util.HashSet;
import java.util.Set;

public interface TableEditorFields {

    // Attrs Blacklist
    Set<String> TABLE_BLACK_LIST_ATTRS                  = new HashSet<>();
    Set<String> COL_BLACK_LIST_ATTRS                    = new HashSet<>();
    Set<String> PK_BLACK_LIST_ATTRS                     = new HashSet<>();
    Set<String> INDEX_BLACK_LIST_ATTRS                  = new HashSet<>();
    Set<String> PARTITION_BLACK_LIST_ATTRS              = new HashSet<>();
    Set<String> CONSTRAINT_BLACK_LIST_ATTRS             = new HashSet<>();
    Set<String> FK_BLACK_LIST_ATTRS                     = new HashSet<>();

    // Table
    String      MODE_TABLE_CATALOG                      = "catalog";
    String      MODE_TABLE_SCHEMA                       = "schema";
    String      MODE_TABLE_NAME                         = "name";
    String      MODE_TABLE_COMMENT                      = "comment";

    // Column
    String      MODE_COLUMN_SOURCE                      = "source";
    String      MODE_COLUMN_NAME                        = "name";
    String      MODE_COLUMN_COMMENT                     = "comment";
    String      MODE_COLUMN_NOT_NULL                    = "notNull";
    String      MODE_COLUMN_AUTOINCREMENT               = "autoIncrement";
    String      MODE_COLUMN_DEFAULT                     = "default";
    String      MODE_COLUMN_DEFAULT_OPTION              = "defaultOpt";
    String      MODE_COLUMN_TYPE                        = "columnType";
    String      MODE_COLUMN_LENGTH                      = "length";
    String      MODE_COLUMN_NUM_P                       = "numericPrecision";
    String      MODE_COLUMN_NUM_S                       = "numericScale";
    String      MODE_COLUMN_UNSIGNED                    = "numericUnsigned";
    String      MODE_COLUMN_DATE_P                      = "datePrecision";

    // Primary Key
    String      MODE_KEY_NAME                           = "name";
    String      MODE_KEY_COLUMNS                        = "columns";
    String      MODE_KEY_COLUMNS_NAME                   = "name";
    String      MODE_KEY_INDEX_TYPE                     = "indexType";

    // Indexes
    String      MODE_INDEX_NAME                         = "name";
    String      MODE_INDEX_COMMENT                      = "comment";
    String      MODE_INDEX_TYPE                         = "type";
    String      MODE_INDEX_COLUMNS                      = "columns";
    String      MODE_INDEX_COLUMNS_NAME                 = "name";

    //partition
    String      MODE_PARTITION_DEFINITION               = "definition";
    String      MODE_PARTITION_ITEMS                    = "items";

    // partition define
    String      MODE_DEFINITION_PARTITION_TYPE          = "type";
    String      MODE_DEFINITION_PARTITION_EXPRESSION    = "expression";
    String      MODE_PARTITION_TEMPLATE                 = "template";
    // partition item
    String      MODE_PARTITION_ITEM_NAME                = "name";
    String      MODE_PARTITION_ITEM_DESCRIPTION         = "description";
    String      MODE_PARTITION_ITEM_TABLESPACE          = "tablespace";
    String      MODE_PARTITION_ITEM_COMMENT             = "comment";
    String      MODE_PARTITION_ITEM_TYPE                = "type";

    String      MODE_PARTITION_TYPE                     = "partitionType";
    String      MODE_SUB_PARTITION_TYPE                 = "subPartitionType";
    String      MODE_PARTITION_COUNT                    = "partitionCount";
    String      MODE_SUB_PARTITION_COUNT                = "subPartitionCount";
    String      MODE_SUB_PARTITION_EXPRESSION           = "subPartitionExpression";
    String      MODE_PARTITION_EXPRESSION               = "partitionExpression";

    // constraints
    String      MODE_CONSTRAINT_NAME                    = "name";
    String      MODE_CONSTRAINT_TYPE                    = "type";
    String      MODE_CONSTRAINT_EXPRESSION              = "expression";
    String      MODE_CONSTRAINT_COLUMNS                 = "columns";
    String      MODE_CONSTRAINT_COLUMN_NAME             = "name";

    String      MODE_FOREIGN_KEY_NAME                   = "name";
    String      MODE_FOREIGN_KEY_REFERENCED_SCHEMA      = "referencedSchema";
    String      MODE_FOREIGN_KEY_REFERENCED_TABLE       = "referencedTable";
    String      MODE_FOREIGN_KEY_REFERENCED_RELATION    = "relation";
    String      MODE_FOREIGN_KEY_REFERENCED_COLUMN_NAME = "referenceColumnName";
    String      MODE_FOREIGN_KEY_UPDATE_RULE            = "updateRule";
    String      MODE_FOREIGN_KEY_DELETE_RULE            = "deleteRule";

    //property
    String      BASE_INFO                               = "baseInfo";
}
