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
package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;

import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;

/**
 * @author mode 2021/5/21 19:56
 */
public interface ColumnTriggers extends java.util.EventListener {

    default List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                   EColumn columnInfo, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                    EColumn columnInfo) {
        return Collections.emptyList();
    }

    default boolean supportColumnRename() {
        return true;
    }

    /**
     * colum rename
     * @param columnInfo old column
     * @param newColumnName new column name
     * @return
     */
    default List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, //
                                      EColumn columnInfo, String newColumnName) {
        return Collections.emptyList();
    }

    default List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, //
                                      EColumn columnInfo, EColumn newInfo, List<String> diffChange, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, //
                                       EColumn columnInfo, String comment, ETable eTable) {
        return Collections.emptyList();
    }
}
