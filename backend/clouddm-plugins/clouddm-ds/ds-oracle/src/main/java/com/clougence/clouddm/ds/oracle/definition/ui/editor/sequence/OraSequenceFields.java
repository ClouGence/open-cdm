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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.sequence;

import com.clougence.adapter.oracle.OracleAttributeNames;

public interface OraSequenceFields {

    String STATUS       = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String CREATE_TIME  = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String DDL_TIME     = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();

    String CYCLE_FLAG   = OracleAttributeNames.SEQUENCE_CYCLE_FLAG.getCodeKey();
    String CACHE_SIZE   = OracleAttributeNames.SEQUENCE_CACHE_SIZE.getCodeKey();
    String LAST_NUMBER  = OracleAttributeNames.SEQUENCE_LAST_NUMBER.getCodeKey();
    String SESSION_FLAG = OracleAttributeNames.SEQUENCE_SESSION_FLAG.getCodeKey();
    String KEEP_VALUE   = OracleAttributeNames.SEQUENCE_KEEP_VALUE.getCodeKey();
}
