/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
