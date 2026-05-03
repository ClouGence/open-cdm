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
