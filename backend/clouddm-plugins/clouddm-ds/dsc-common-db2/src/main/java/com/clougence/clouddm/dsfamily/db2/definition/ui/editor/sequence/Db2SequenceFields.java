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
package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.sequence;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.sequence.SequenceFields;

public interface Db2SequenceFields extends SequenceFields {

    String CREATE_TIME = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME = Db2AttributeNames.UPDATE_DATE.getCodeKey();

    String CYCLE_FLAG  = Db2AttributeNames.CYCLE.getCodeKey();
    String CACHE_SIZE  = Db2AttributeNames.CACHE_SIZE.getCodeKey();
    String LAST_NUMBER = Db2AttributeNames.NEXT_CACHE_VALUE.getCodeKey();
    String ORDER       = Db2AttributeNames.ORDER.getCodeKey();
    String START_VALUE = Db2AttributeNames.START_VALUE.getCodeKey();

}
