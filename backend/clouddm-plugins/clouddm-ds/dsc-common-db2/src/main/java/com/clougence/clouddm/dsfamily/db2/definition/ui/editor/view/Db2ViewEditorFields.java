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
package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.view;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface Db2ViewEditorFields extends ViewEditorFields {

    String VIEW_CHECK_OPTION = Db2AttributeNames.VIEW_CHECK_OPTION.getCodeKey();

    String CREATE_TIME       = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME       = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String COLUMN_COUNT      = Db2AttributeNames.COLUMN_COUNT.getCodeKey();
    String VIEW_READ_ONLY    = Db2AttributeNames.VIEW_READ_ONLY.getCodeKey();
    String INSERTABLE        = Db2AttributeNames.INSERTABLE.getCodeKey();
    String DELETABLE         = Db2AttributeNames.DELETABLE.getCodeKey();
    String UPDATABLE         = Db2AttributeNames.UPDATABLE.getCodeKey();
}
