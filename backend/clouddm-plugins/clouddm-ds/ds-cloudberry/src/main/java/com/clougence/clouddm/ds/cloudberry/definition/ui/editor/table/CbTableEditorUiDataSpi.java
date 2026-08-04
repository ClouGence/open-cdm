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

import static com.clougence.adapter.cloudberry.CloudberryAttributeNames.DISTRIBUTED_COLUMN;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiDataSpi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class CbTableEditorUiDataSpi extends PgTableEditorUiDataSpi implements CbTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        super.fillETable(viewMode, uiData, eTable, mainVersion);

        Object value = uiData.getTableInfo().get(FIELD_TABLE_DISTRIBUTED_COLUMN);
        if (value instanceof List<?> selectedColumns) {
            List<String> columns = new ArrayList<>();
            for (Object selectedColumn : selectedColumns) {
                if (selectedColumn instanceof Map<?, ?> column) {
                    Object name = column.get(FIELD_TABLE_DISTRIBUTED_COLUMN_NAME);
                    if (name != null) {
                        columns.add(name.toString());
                    }
                }
            }
            DISTRIBUTED_COLUMN.setValue(eTable.getAttribute(), JsonUtils.toJson(columns));
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        super.fillUiData(viewMode, eTable, uiData, mainVersion);

        String value = DISTRIBUTED_COLUMN.getValue(eTable.getAttribute());
        if (StringUtils.isBlank(value)) {
            return;
        }

        List<String> columns = JsonUtils.toListUseType(value, String.class);
        List<Map<String, Object>> selectedColumns = new ArrayList<>();
        for (String column : columns) {
            Map<String, Object> selectedColumn = new LinkedHashMap<>();
            selectedColumn.put(FIELD_TABLE_DISTRIBUTED_COLUMN_NAME, column);
            selectedColumns.add(selectedColumn);
        }
        uiData.getTableInfo().put(FIELD_TABLE_DISTRIBUTED_COLUMN, selectedColumns);
    }
}
