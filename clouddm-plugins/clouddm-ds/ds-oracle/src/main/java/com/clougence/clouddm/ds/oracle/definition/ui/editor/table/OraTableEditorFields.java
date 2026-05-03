package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface OraTableEditorFields extends DsFamilyTableEditorFields {

    // property
    String STATUS         = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String CREATE_TIME    = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String DDL_TIME       = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();

    String CLUSTER_NAME   = OracleAttributeNames.CLUSTER_NAME.getCodeKey();
    String PCT_FREE       = OracleAttributeNames.PCT_FREE.getCodeKey();
    String PCT_USED       = OracleAttributeNames.PCT_USED.getCodeKey();
    String INI_TRANS      = OracleAttributeNames.INI_TRANS.getCodeKey();
    String MAX_TRANS      = OracleAttributeNames.MAX_TRANS.getCodeKey();
    String INITIAL_EXTENT = OracleAttributeNames.INITIAL_EXTENT.getCodeKey();
    String NEXT_EXTENT    = OracleAttributeNames.NEXT_EXTENT.getCodeKey();
    String MIN_EXTENTS    = OracleAttributeNames.MIN_EXTENTS.getCodeKey();
    String MAX_EXTENTS    = OracleAttributeNames.MAX_EXTENTS.getCodeKey();
    String TABLESPACE     = OracleAttributeNames.TABLESPACE.getCodeKey();
    String TEMPORARY      = OracleAttributeNames.TEMPORARY.getCodeKey();
    String PARTITION      = OracleAttributeNames.PARTITION_TABLE.getCodeKey();
}
