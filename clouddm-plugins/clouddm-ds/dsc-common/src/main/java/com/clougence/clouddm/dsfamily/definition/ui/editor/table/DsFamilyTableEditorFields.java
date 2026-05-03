package com.clougence.clouddm.dsfamily.definition.ui.editor.table;

import com.clougence.clouddm.sdk.ui.editor.table.TableEditorFields;

/**
 * @Author: Ekko
 * @Date: 2023-08-29 16:06
 */
public interface DsFamilyTableEditorFields extends TableEditorFields {

    @Deprecated
    String SPI_COLUMNS_CUSTOM                      = "customDefault";

    String FIELD_TABLE_CATALOG                     = MODE_TABLE_CATALOG;
    String FIELD_TABLE_SCHEMA                      = MODE_TABLE_SCHEMA;
    String FIELD_TABLE_NAME                        = MODE_TABLE_NAME;
    String FIELD_TABLE_COMMENT                     = MODE_TABLE_COMMENT;

    String FIELD_COLUMN_SOURCE                     = MODE_COLUMN_SOURCE;
    String FIELD_COLUMN_NAME                       = MODE_COLUMN_NAME;
    String FIELD_COLUMN_COMMENT                    = MODE_COLUMN_COMMENT;
    String FIELD_COLUMN_DATATYPE                   = MODE_COLUMN_TYPE;
    String FIELD_COLUMN_NOTNULL                    = MODE_COLUMN_NOT_NULL;
    String FIELD_COLUMN_DEFAULT                    = MODE_COLUMN_DEFAULT;
    String FIELD_COLUMN_DEFAULT_OPTION             = MODE_COLUMN_DEFAULT_OPTION;
    String FIELD_COLUMN_NUM_PRECISION              = MODE_COLUMN_NUM_P;
    String FIELD_COLUMN_NUM_SCALE                  = MODE_COLUMN_NUM_S;
    String FIELD_COLUMN_DATE_PRECISION             = MODE_COLUMN_DATE_P;
    String FIELD_COLUMN_LENGTH                     = MODE_COLUMN_LENGTH;
    @Deprecated
    String FIELD_COLUMN_DEFAULT_CUSTOM             = SPI_COLUMNS_CUSTOM;

    String FIELD_PRIMARY_NAME                      = MODE_KEY_NAME;
    String FIELD_PRIMARY_COLUMNS                   = MODE_KEY_COLUMNS;
    String FIELD_PRIMARY_COLUMNS_NAME              = MODE_KEY_COLUMNS_NAME;

    String FIELD_INDEXES_NAME                      = MODE_INDEX_NAME;
    String FIELD_INDEXES_COMMENT                   = MODE_INDEX_COMMENT;
    String FIELD_INDEXES_TYPE                      = MODE_INDEX_TYPE;
    String FIELD_INDEXES_COLUMNS                   = MODE_INDEX_COLUMNS;
    String FIELD_INDEXES_COLUMNS_NAME              = MODE_INDEX_COLUMNS_NAME;

    //
    String FIELD_CONSTRAINT_NAME                   = MODE_CONSTRAINT_NAME;
    String FIELD_CONSTRAINT_TYPE                   = MODE_CONSTRAINT_TYPE;
    String FIELD_CONSTRAINT_EXPRESSION             = MODE_CONSTRAINT_EXPRESSION;
    String FIELD_CONSTRAINT_COLUMNS                = MODE_CONSTRAINT_COLUMNS;
    String FIELD_CONSTRAINT_COLUMN_NAME            = MODE_CONSTRAINT_COLUMN_NAME;

    // foreign key
    String FIELD_FOREIGN_KEY_NAME                  = MODE_FOREIGN_KEY_NAME;
    String FIELD_CONSTRAINT_REFERENCED_SCHEMA      = MODE_FOREIGN_KEY_REFERENCED_SCHEMA;
    String FIELD_CONSTRAINT_REFERENCED_TABLE       = MODE_FOREIGN_KEY_REFERENCED_TABLE;
    String FIELD_CONSTRAINT_REFERENCED_RELATION    = MODE_FOREIGN_KEY_REFERENCED_RELATION;
    String FIELD_CONSTRAINT_REFERENCED_COLUMN_NAME = MODE_FOREIGN_KEY_REFERENCED_COLUMN_NAME;
    String FIELD_CONSTRAINT_UPDATE_RULE            = MODE_FOREIGN_KEY_UPDATE_RULE;
    String FIELD_CONSTRAINT_DELETE_RULE            = MODE_FOREIGN_KEY_DELETE_RULE;
}
