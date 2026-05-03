package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

import com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorFields;

public interface AdsMyTableEditorFields extends MyTableEditorFields {

    String FIELD_TABLE_STORAGEPOLICY        = AdsMyUmiAttributeNames.STORAGE_POLICY.getCodeKey();
    String FIELD_TABLE_STORAGEPOLICY_PT_CNT = AdsMyUmiAttributeNames.STORAGE_POLICY_HOT_PT_CNT.getCodeKey();
    String FIELD_TABLE_BLOCKSIZE            = AdsMyUmiAttributeNames.BLOCK_SIZE.getCodeKey();
    String FIELD_TABLE_RTENGINE             = AdsMyUmiAttributeNames.RT_ENGINE.getCodeKey();
    String FIELD_TABLE_PROPERTIES           = AdsMyUmiAttributeNames.TAB_PROPERTIES.getCodeKey();

    String FIELD_COLUMN_STRUCTURE           = AdsMyUmiAttributeNames.TYPE_STRUCTURE.getCodeKey();

    String FIELD_INDEXES_ANNALGORITHM       = AdsMyUmiAttributeNames.ANN_ALGORITHM.getCodeKey();
    String FIELD_INDEXES_ANNDISFUNCTION     = AdsMyUmiAttributeNames.ANN_DISFUNCTION.getCodeKey();
}
