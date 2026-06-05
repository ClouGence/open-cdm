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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.view;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface OraViewEditorFields extends ViewEditorFields {

    String FORCE_CREATE_VIEW      = OracleAttributeNames.FORCE_CREATE_VIEW.getCodeKey();

    // property
    String TEXT_LENGTH            = OracleAttributeNames.VIEW_TEXT_LENGTH.getCodeKey();
    String STATUS                 = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String CREATE_TIME            = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String DDL_TIME               = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String READ_ONLY              = OracleAttributeNames.VIEW_READ_ONLY.getCodeKey();

    String MVIEW_REFRESH_DATE     = OracleAttributeNames.MVIEW_REFRESH_DATE.getCodeKey();
    String MVIEW_REFRESH_END_TIME = OracleAttributeNames.MVIEW_REFRESH_END_TIME.getCodeKey();
}
