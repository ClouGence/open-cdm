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
