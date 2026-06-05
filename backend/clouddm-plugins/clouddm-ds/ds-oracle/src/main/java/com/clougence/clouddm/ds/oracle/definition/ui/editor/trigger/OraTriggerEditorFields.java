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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.trigger;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface OraTriggerEditorFields extends TriggerEditorFields {

    String NEW_ALIAS           = OracleAttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS           = OracleAttributeNames.OLD_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY = OracleAttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String CONDITION           = OracleAttributeNames.CONDITION.getCodeKey();

    String OBJ_ENABLED         = OracleAttributeNames.OBJ_ENABLED.getCodeKey();
    String CREATE_TIME         = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String LAST_DDL_TIME       = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String OBJ_STATUS          = OracleAttributeNames.OBJ_STATUS.getCodeKey();
}
