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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.user;

import com.clougence.adapter.oracle.OracleAttributeNames;

public interface OraUserFields {

    String USER_ID                = OracleAttributeNames.USER_ID.getCodeKey();
    String CREATE_TIME            = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String USER_COMMON            = OracleAttributeNames.USER_OR_ROLE_COMMON.getCodeKey();
    String USER_ORACLE_MAINTAINED = OracleAttributeNames.USER_OR_ROLE_ORACLE_MAINTAINED.getCodeKey();
}
