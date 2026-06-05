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
package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table;

import com.clougence.adapter.mc.McAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author mode
 * @date 2023/9/27 10:24
*/
public interface McTableEditorFields extends DsFamilyTableEditorFields {

    // property
    String OWNER                  = McAttributeNames.OWNER.getCodeKey();
    String CREATED_TIME           = McAttributeNames.CREATED_TIME.getCodeKey();
    String LAST_META_MODIFIED     = McAttributeNames.LAST_META_MODIFIED.getCodeKey();
    String LAST_DATA_MODIFIED     = McAttributeNames.LAST_DATA_MODIFIED.getCodeKey();
    String LAST_ACCESS_MODIFIED   = McAttributeNames.LAST_ACCESS_MODIFIED.getCodeKey();
    String DATA_BYTES             = McAttributeNames.DATA_BYTES.getCodeKey();
    String PHYSICAL_BYTES         = McAttributeNames.PHYSICAL_BYTES.getCodeKey();
    String FILE_NUMBER            = McAttributeNames.FILE_NUMBER.getCodeKey();
    String RECORD_NUM             = McAttributeNames.RECORD_NUM.getCodeKey();
    String LIFE_DAY               = McAttributeNames.LIFE_DAY.getCodeKey();
    String ARCHIVED               = McAttributeNames.ARCHIVED.getCodeKey();

    // table
    String TABLE_ID               = McAttributeNames.TABLE_ID.getCodeKey();
    String TABLE_CRYPTO_ALGO_NAME = McAttributeNames.TABLE_CRYPTO_ALGO_NAME.getCodeKey();
    String TABLE_MAX_LABEL        = McAttributeNames.TABLE_MAX_LABEL.getCodeKey();
    String TABLE_LABEL            = McAttributeNames.TABLE_LABEL.getCodeKey();

    // column
    String COLUMN_TYPE            = McAttributeNames.COLUMN_TYPE.getCodeKey();

    // func
    String CLASS_PATH             = McAttributeNames.CLASS_PATH.getCodeKey();

    // role
    String ROLE_POLICY            = McAttributeNames.ROLE_POLICY.getCodeKey();
    String ROLE_TYPE              = McAttributeNames.ROLE_TYPE.getCodeKey();
    String ROLE_ACL               = McAttributeNames.ROLE_ACL.getCodeKey();

}
