package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/6 14:55
*/
public interface DrTableEditorFields extends DsFamilyTableEditorFields {

    String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME = "name";

    String FIELD_TABLE_BUCKET_NUMBER               = DorisAttributeNames.BUCKET_NUMBER.getCodeKey();
    String FIELD_TABLE_COLUMN_KEY_TYPE             = DorisAttributeNames.KEY_TYPE.getCodeKey();

    String FIELD_COLUMN_AGG_TYPE                   = DorisAttributeNames.AGG_TYPE.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_BY              = DorisAttributeNames.DISTRIBUTED_BY_TYPE.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS      = DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey();
}
