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
package com.clougence.clouddm.ds.cloudberry.definition.ui.editor.table;

import com.clougence.adapter.cloudberry.CloudberryAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorFields;

public interface CbTableEditorFields extends PgTableEditorFields {

    String FIELD_TABLE_DISTRIBUTED_TYPE        = CloudberryAttributeNames.DISTRIBUTED_TYPE.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_COLUMN      = CloudberryAttributeNames.DISTRIBUTED_COLUMN.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_COLUMN_NAME = "name";
    String FIELD_TABLE_APPEND_OPTIMIZED        = CloudberryAttributeNames.APPEND_OPTIMIZED.getCodeKey();
    String FIELD_TABLE_BLOCK_SIZE              = CloudberryAttributeNames.BLOCK_SIZE.getCodeKey();
    String FIELD_TABLE_ORIENTATION             = CloudberryAttributeNames.ORIENTATION.getCodeKey();
    String FIELD_TABLE_CHECK_SUM               = CloudberryAttributeNames.CHECK_SUM.getCodeKey();
    String FIELD_TABLE_COMPRESS_TYPE           = CloudberryAttributeNames.COMPRESS_TYPE.getCodeKey();
    String FIELD_TABLE_COMPRESS_LEVEL          = CloudberryAttributeNames.COMPRESS_LEVEL.getCodeKey();
}
