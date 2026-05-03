package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface Db2TableEditorFields extends DsFamilyTableEditorFields {

    String CREATE_TIME     = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME     = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String INVALIDATE_TIME = Db2AttributeNames.INVALID_DATE.getCodeKey();
    String STATS_TIME      = Db2AttributeNames.STATS_TIME.getCodeKey();
    String COLUMN_COUNT    = Db2AttributeNames.COLUMN_COUNT.getCodeKey();
    String ROW_COUNT       = Db2AttributeNames.ROW_COUNT.getCodeKey();
    String NPAGES          = Db2AttributeNames.NPAGES.getCodeKey();
    String MPAGES          = Db2AttributeNames.MPAGES.getCodeKey();
    String FPAGES          = Db2AttributeNames.FPAGES.getCodeKey();
    String SYSTEM_TABLE    = Db2AttributeNames.SYSTEM_TABLE.getCodeKey();
    String INSERTABLE      = Db2AttributeNames.INSERTABLE.getCodeKey();
    String DEFINER         = Db2AttributeNames.DEFINER.getCodeKey();
}
