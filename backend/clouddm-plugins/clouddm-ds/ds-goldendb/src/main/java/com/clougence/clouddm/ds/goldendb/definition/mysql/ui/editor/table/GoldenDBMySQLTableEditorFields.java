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
package com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table;

import com.clougence.adapter.goldendb.GoldenDBAttributeNames;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorFields;

public interface GoldenDBMySQLTableEditorFields extends MyTableEditorFields {

    String FIELD_TABLE_DISTRIBUTION_TYPE       = GoldenDBAttributeNames.DISTRIBUTION_TYPE.getCodeKey();
    String FIELD_TABLE_DISTRIBUTION_COLUMNS    = GoldenDBAttributeNames.DISTRIBUTION_COLUMNS.getCodeKey();
    String FIELD_TABLE_DISTRIBUTION_EXPRESSION = GoldenDBAttributeNames.DISTRIBUTION_EXPRESSION.getCodeKey();
    String FIELD_TABLE_DISTRIBUTION_GROUPS     = GoldenDBAttributeNames.DISTRIBUTION_GROUPS.getCodeKey();
    String FIELD_TABLE_DISTRIBUTION_COLUMN     = "name";
}
