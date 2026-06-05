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
package com.clougence.clouddm.dsfamily.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-08-29 16:06
 */
public class DsFamilyTableEditorUiDataSpi implements TableEditorUiDataSpi, DsFamilyTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                if (idxMap.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            // name
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            newColumnList.add(name);
                        }
                    }
                    index.setColumnList(newColumnList);
                }

                if (idxMap.containsKey(MODE_INDEX_TYPE)) {
                    String idxWay = safeToString(idxMap.get(MODE_INDEX_TYPE));
                    if (StringUtils.equalsIgnoreCase(idxWay, "UNIQUE")) {
                        index.setType(EIndexType.Unique);
                    } else {
                        index.setType(EIndexType.Normal);
                    }
                }
            }

        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
    }

    public static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }
}
