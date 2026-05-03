package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author caishan
 * @date 2023/8/14 14:42
 */
public interface PgTableEditorFields extends DsFamilyTableEditorFields {

    String SPI_PK_COLUMNS_LENGTH                 = "length";
    String SPI_PK_COLUMNS_ORDER                  = "order";

    String SPI_INDEX_COLUMNS_LENGTH              = "length";
    String SPI_INDEX_COLUMNS_ORDER               = "order";

    String SPI_INDEX_NULLS_SORT                  = "nullsSort";
    String SPI_INDEX_OPERATOR_TYPE               = "operatorType";
    String SPI_INDEX_SORT_RULES                  = "sortRule";

    String FIELD_TABLE_TABLE_TYPE                = PostgreAttributeNames.TABLE_TYPE.getCodeKey();
    String FIELD_TABLE_TABLESPACE                = PostgreAttributeNames.TABLESPACE.getCodeKey();
    String FIELD_TABLE_INHERITED_FROM            = PostgreAttributeNames.INHERITED_FROM.getCodeKey();
    String FIELD_TABLE_FILL_FACTOR               = PostgreAttributeNames.FILL_FACTOR.getCodeKey();
    String FIELD_TABLE_PARTITION_TYPE            = PostgreAttributeNames.PARTITION_TYPE.getCodeKey();
    String FIELD_TABLE_WITH_OIDS                 = PostgreAttributeNames.WITH_OIDS.getCodeKey();

    //
    String FIELD_COLUMN_VIRTUAL_TYPE             = PostgreAttributeNames.VIRTUAL_TYPE.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_INCREMENTAL = PostgreAttributeNames.VIRTUAL_TYPE_INCREMENTAL.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_EXPRESSION  = PostgreAttributeNames.VIRTUAL_TYPE_EXPRESSION.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_MIN         = PostgreAttributeNames.VIRTUAL_TYPE_MIN.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_MAX         = PostgreAttributeNames.VIRTUAL_TYPE_MAX.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_START       = PostgreAttributeNames.VIRTUAL_TYPE_START.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_CACHE       = PostgreAttributeNames.VIRTUAL_TYPE_CACHE.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_TYPE_LOOP        = PostgreAttributeNames.VIRTUAL_TYPE_LOOP.getCodeKey();
    String FIELD_COLUMN_SORT_RULES               = PostgreAttributeNames.COLUMN_SORT_RULES.getCodeKey();
    String FIELD_COLUMN_ARRAY_DIMENSION          = PostgreAttributeNames.ARRAY_DIMENSION.getCodeKey();
    //
    String FIELD_PRIMARY_INDEX_TYPE              = PostgreAttributeNames.INDEX_TYPE.getCodeKey();
    String FIELD_PRIMARY_CONSTRAINT_DELAY        = PostgreAttributeNames.PK_CONSTRAINT_DELAY.getCodeKey();
    String FIELD_PRIMARY_CONSTRAINT_INITIAL      = PostgreAttributeNames.PK_CONSTRAINT_INITIAL.getCodeKey();
    String FIELD_PRIMARY_COLUMNS_PART            = PgTableEditorUiDataSpi.SPI_PK_COLUMNS_LENGTH;
    String FIELD_PRIMARY_COLUMNS_ORDER           = PgTableEditorUiDataSpi.SPI_PK_COLUMNS_ORDER;
    //
    String FIELD_INDEXES_CONCURRENTLY            = PostgreAttributeNames.INDEX_CONCURRENTLY.getCodeKey();
    String FIELD_INDEXES_COLUMNS_LENGTH          = PgTableEditorUiDataSpi.SPI_INDEX_COLUMNS_LENGTH;
    String FIELD_INDEXES_COLUMNS_ORDER           = PgTableEditorUiDataSpi.SPI_INDEX_COLUMNS_ORDER;
    String FIELD_INDEXES_NULLS_SORT              = PgTableEditorUiDataSpi.SPI_INDEX_NULLS_SORT;
    String FIELD_INDEXES_OPERATOR_TYPE           = PgTableEditorUiDataSpi.SPI_INDEX_OPERATOR_TYPE;
    String FIELD_INDEXES_SORT_RULES              = PgTableEditorUiDataSpi.SPI_INDEX_SORT_RULES;
    String FIELD_INDEXES_INDEX_TYPE              = PostgreAttributeNames.INDEX_TYPE.getCodeKey();
    String FIELD_INDEXES_INDEX_WAY               = PostgreAttributeNames.INDEX_WAY.getCodeKey();
    String FIELD_INDEXES_WITH_FILLFACTOR         = PostgreAttributeNames.INDEX_WITH_FILLFACTOR.getCodeKey();
    String FIELD_INDEXES_WITH_BUFFERING          = PostgreAttributeNames.INDEX_WITH_BUFFERING.getCodeKey();
    String FIELD_INDEXES_WITH_FASTUPDATE         = PostgreAttributeNames.INDEX_WITH_FASTUPDATE.getCodeKey();
    String FIELD_INDEXES_INDEX_TABLESPACE        = PostgreAttributeNames.INDEX_TABLESPACE.getCodeKey();
    String FIELD_INDEXES_INDEX_WHERE             = PostgreAttributeNames.INDEX_WHERE.getCodeKey();
}
