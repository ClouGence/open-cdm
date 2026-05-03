package com.clougence.clouddm.ds.gauss.definition.gs.ui.editor.table;

import com.clougence.adapter.gauss.GaussDBAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface GsTableEditorFields extends PgTableEditorFields {

    String FIELD_TABLE_ORIENTATION  = GaussDBAttributeNames.ORIENTATION.getCodeKey();
    String FIELD_TABLE_STORAGE_TYPE = GaussDBAttributeNames.STORAGE_TYPE.getCodeKey();
}
