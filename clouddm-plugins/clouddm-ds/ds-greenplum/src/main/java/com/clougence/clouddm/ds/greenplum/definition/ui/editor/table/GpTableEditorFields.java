package com.clougence.clouddm.ds.greenplum.definition.ui.editor.table;

import com.clougence.adapter.greenplum.GreenplumAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface GpTableEditorFields extends PgTableEditorFields {

    String FIELD_TABLE_DISTRIBUTED_TYPE   = GreenplumAttributeNames.DISTRIBUTED_TYPE.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_COLUMN = GreenplumAttributeNames.DISTRIBUTED_COLUMN.getCodeKey();
    String FIELD_TABLE_APPEND_OPTIMIZED   = GreenplumAttributeNames.APPEND_OPTIMIZED.getCodeKey();
    String FIELD_TABLE_BLOCK_SIZE         = GreenplumAttributeNames.BLOCK_SIZE.getCodeKey();
    String FIELD_TABLE_ORIENTATION        = GreenplumAttributeNames.ORIENTATION.getCodeKey();
    String FIELD_TABLE_CHECK_SUM          = GreenplumAttributeNames.CHECK_SUM.getCodeKey();
    String FIELD_TABLE_COMPRESS_TYPE      = GreenplumAttributeNames.COMPRESS_TYPE.getCodeKey();
    String FIELD_TABLE_COMPRESS_LEVEL     = GreenplumAttributeNames.COMPRESS_LEVEL.getCodeKey();
}
