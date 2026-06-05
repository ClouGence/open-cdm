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
