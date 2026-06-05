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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;

public interface MyProcedureEditorFields extends ProcedureEditorFields {

    String DETERMINISTIC   = MyUmiAttributeNames.DETERMINISTIC.getCodeKey();
    String SQL_DATA_ACCESS = MyUmiAttributeNames.SQL_DATA_ACCESS.getCodeKey();
    String DEFINER         = MyUmiAttributeNames.DEFINER.getCodeKey();
    String SECURITY_TYPE   = MyUmiAttributeNames.SECURITY_TYPE.getCodeKey();
    String CREATE_TIME     = MyUmiAttributeNames.CREATE_TIME.getCodeKey();
    String UPDATE_TIME     = MyUmiAttributeNames.UPDATE_TIME.getCodeKey();
}
