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
package com.clougence.clouddm.ds.gauss.definition.gsog.ui.editor.table;

import com.clougence.adapter.gauss.GaussDBAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorFields;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public interface GsogTableEditorFields extends PgTableEditorFields {

    String FIELD_TABLE_ORIENTATION  = GaussDBAttributeNames.ORIENTATION.getCodeKey();
    String FIELD_TABLE_STORAGE_TYPE = GaussDBAttributeNames.STORAGE_TYPE.getCodeKey();
}
