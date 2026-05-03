package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/8/10 10:27
*/
public class MyTableEditorUiDataSpi implements TableEditorUiDataSpi, MyTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // column
        List<EColumn> columnList = eTable.getColumnList();
        for (EColumn eColumn : columnList) {
            if (StringUtils.isNotEmpty(eColumn.getDefaultValue())) {
                if (new MyMetaProviderUtils().isFunc(eColumn.getDefaultValue())) {
                    eColumn.setDefaultValueIsFunc(true);
                }
            }
        }

        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                if (idxMap.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    Map<String, String> subPartMap = new LinkedHashMap<>();
                    Map<String, String> subOrderMap = new LinkedHashMap<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            // name
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            newColumnList.add(name);

                            // length
                            String length = safeToString(map.get(SPI_INDEX_COLUMNS_LENGTH));
                            subPartMap.put(name, length);

                            // order
                            String order = safeToString(map.get(SPI_INDEX_COLUMNS_ORDER));
                            subOrderMap.put(name, order);
                        }
                    }
                    index.setColumnList(newColumnList);
                    index.getAttribute().put(MyUmiAttributeNames.SUB_PART.getCodeKey(), JsonUtils.toJson(subPartMap));
                    //index.getAttribute().put(MySQLAttributeNames.SUB_ORDER.getCodeKey(), JsonUtils.toJson(subOrderMap));
                }

                if (idxMap.containsKey(MyUmiAttributeNames.INDEX_WAY.getCodeKey())) {
                    String idxWay = safeToString(idxMap.get(MyUmiAttributeNames.INDEX_WAY.getCodeKey()));
                    if (StringUtils.equalsIgnoreCase(idxWay, "UNIQUE")) {
                        index.setType(EIndexType.Unique);
                    } else {
                        index.setType(EIndexType.Normal);
                    }
                }

            }

        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                List<String> newKeyColumns = new ArrayList<>();
                Map<String, String> subPartMap = new LinkedHashMap<>();
                Map<String, String> subOrderMap = new LinkedHashMap<>();

                for (Map<String, Object> map : columnListMap) {
                    //name
                    String name = safeToString(map.get(MODE_KEY_COLUMNS_NAME));
                    newKeyColumns.add(name);

                    // length
                    String length = safeToString(map.get(SPI_PK_COLUMNS_LENGTH));
                    subPartMap.put(name, length);

                    // order
                    String order = safeToString(map.get(SPI_PK_COLUMNS_ORDER));
                    subOrderMap.put(name, order);
                }
                eTable.getPrimaryKey().setColumnList(newKeyColumns);
                eTable.getPrimaryKey().getAttribute().put(MyUmiAttributeNames.SUB_PART.getCodeKey(), JsonUtils.toJson(subPartMap));
                //eTable.getPrimaryKey().getAttribute().put(MySQLAttributeNames.SUB_ORDER.getCodeKey(), JsonUtils.toJson(subOrderMap));
            }
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                if (obj instanceof List) {
                    List<Map<String, Object>> mapList = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> map : mapList) {

                        String partMapValue = MyUmiAttributeNames.SUB_PART.getValue(index.getAttribute());
                        Map<String, String> subPartMap = JsonUtils.toMap(partMapValue);
                        String subPartValue = subPartMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        map.put(SPI_INDEX_COLUMNS_LENGTH, subPartValue);

                        //String orderMapValue = MyUmiAttributeNames.SUB_ORDER.getValue(index.getAttribute());
                        //Map<String, String> subOrderMap = JsonUtils.toMap(orderMapValue);
                        //String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        //map.put(SPI_INDEX_COLUMNS_ORDER, subOrderValue);
                    }
                }
            }

        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                for (Map<String, Object> map : columnListMap) {

                    String partMapValue = MyUmiAttributeNames.SUB_PART.getValue(eTable.getPrimaryKey().getAttribute());
                    Map<String, String> subPartMap = JsonUtils.toObj(partMapValue, Map.class);
                    String subPartValue = subPartMap.get(safeToString(map.get(MODE_KEY_COLUMNS_NAME)));
                    map.put(SPI_PK_COLUMNS_LENGTH, subPartValue);

                    //String orderMapValue = MyUmiAttributeNames.SUB_ORDER.getValue(eTable.getPrimaryKey().getAttribute());
                    //Map<String, String> subOrderMap = JsonUtils.toObj(orderMapValue, Map.class);
                    //String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_KEY_COLUMNS_NAME)));
                    //map.put(SPI_PK_COLUMNS_ORDER, subOrderValue);
                }
            }
        }
    }

    public static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }
}
