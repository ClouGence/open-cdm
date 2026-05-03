package com.clougence.clouddm.ds.dameng.definition.ui.editor.table;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface DmTableEditorFields extends DsFamilyTableEditorFields {

    String FIELD_TABLE_TABLE_TYPE                              = DmAttributeNames.TABLE_TYPE.getCodeKey();
    String FIELD_COLUMN_VIRTUAL_EXPR                           = DmAttributeNames.VIRTUAL_EXPR.getCodeKey();
    String FIELD_COLUMN_IDENTITY                               = DmAttributeNames.IDENTITY.getCodeKey();
    String FIELD_COLUMN_IDENTITY_SEED                          = DmAttributeNames.IDENTITY_SEED.getCodeKey();
    String FIELD_COLUMN_IDENTITY_INCREMENT                     = DmAttributeNames.IDENTITY_INCREMENT.getCodeKey();
    String FIELD_INDEXES_INDEX_WAY                             = DmAttributeNames.INDEX_WAY.getCodeKey();

    //partition
    String FIELD_PARTITION_ITEM_NAME                           = DmAttributeNames.PARTITION_ITEM_NAME.getCodeKey();
    String FIELD_PARTITION_DESCRIPTION                         = DmAttributeNames.PARTITION_ITEM_DESCRIPTION.getCodeKey();
    String FIELD_PARTITION_ITEM_BOUNDARY_VALUE                 = DmAttributeNames.PARTITION_ITEM_BOUNDARY_VALUE.getCodeKey();
    String FIELD_PARTITION_ITEM_BOUNDARY_TYPE                  = DmAttributeNames.PARTITION_ITEM_BOUNDARY_TYPE.getCodeKey();
    String FIELD_PARTITION_ITEM_TABLE_SPACE                    = DmAttributeNames.PARTITION_ITEM_TABLESPACE.getCodeKey();
    String FIELD_PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS = DmAttributeNames.PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS.getCodeKey();
    String FIELD_PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS    = DmAttributeNames.PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS.getCodeKey();
    String FIELD_PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS     = DmAttributeNames.PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS.getCodeKey();
    String FIELD_PARTITION_ITEM_FILL_RATIO                     = DmAttributeNames.PARTITION_ITEM_FILL_RATIO.getCodeKey();
    String FIELD_PARTITION_ITEM_RANGE_INTERVAL                 = DmAttributeNames.PARTITION_ITEM_RANGE_INTERVAL.getCodeKey();
}
