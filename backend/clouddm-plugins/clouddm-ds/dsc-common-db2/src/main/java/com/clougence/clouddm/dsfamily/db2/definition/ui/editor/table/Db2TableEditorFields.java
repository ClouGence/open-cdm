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
