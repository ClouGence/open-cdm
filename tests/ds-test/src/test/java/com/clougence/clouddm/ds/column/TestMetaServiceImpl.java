package com.clougence.clouddm.ds.column;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

public class TestMetaServiceImpl implements MetaService {

    private final Map<String, String> map                   = new HashMap<>();

    private final String              schema1Table1         = "src/test/resources/column-test/schema1-table1.json";
    private final String              schema1Table2         = "src/test/resources/column-test/schema1-table2.json";
    private final String              schema2Table1         = "src/test/resources/column-test/schema2-table1.json";
    private final String              schema2Table2         = "src/test/resources/column-test/schema2-table2.json";
    private final String              catalog1Schema1Table1 = "src/test/resources/column-test/catalog1-schema1-table1.json";
    private final String              catalog2Schema1Table2 = "src/test/resources/column-test/catalog2-schema1-table2.json";
    private final String              catalog1Schema1Table2 = "src/test/resources/column-test/catalog1-schema1-table2.json";
    private final String              catalog1Schema2Table1 = "src/test/resources/column-test/catalog1-schema2-table1.json";
    private final String              catalog2Schema2Table2 = "src/test/resources/column-test/catalog2-schema2-table2.json";
    private final String              catalog1Schema2Table2 = "src/test/resources/column-test/catalog1-schema2-table2.json";
    private final String              catalog1DefaultTable2 = "src/test/resources/column-test/catalog1-default-table2.json";
    private final String              catalog2DefaultTable2 = "src/test/resources/column-test/catalog2-default-table2.json";

    public TestMetaServiceImpl(){
        map.put("schema1-table1", schema1Table1);
        map.put("schema1-table2", schema1Table2);
        map.put("schema2-table1", schema2Table1);
        map.put("schema2-table2", schema2Table2);
        map.put("catalog1-schema1-table1", catalog1Schema1Table1);
        map.put("catalog2-schema1-table2", catalog2Schema1Table2);
        map.put("catalog1-schema1-table2", catalog1Schema1Table2);
        map.put("catalog1-schema2-table1", catalog1Schema2Table1);
        map.put("catalog2-schema2-table2", catalog2Schema2Table2);
        map.put("catalog1-schema2-table2", catalog1Schema2Table2);
        map.put("catalog1-default-table2", catalog1DefaultTable2);
        map.put("catalog2-default-table2", catalog2DefaultTable2);
    }

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String leafName, int tableId) {
        String schema = levelsParam.get(UmiTypes.Schema).toString();
        String s = map.get(schema + "-" + leafName);
        if (levelsParam.get(UmiTypes.Catalog) != null) {
            s = map.get(levelsParam.get(UmiTypes.Catalog) + "-" + schema + "-" + leafName);
        }

        try {
            String json = getJson(s);
            List<SelectItem> selectItems = JsonUtils.toList(json, new TypeReference<List<SelectItem>>() {
            });
            return selectItems.stream().map(selectItem -> {
                MetaCol metaCol = new MetaCol();
                metaCol.setColumn(selectItem.getColumns().get(0).getColumn());
                metaCol.setTable(selectItem.getColumns().get(0).getTable());
                metaCol.setSchema(selectItem.getColumns().get(0).getSchema());
                metaCol.setCatalog(selectItem.getColumns().get(0).getCatalog());
                return metaCol;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getJson(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
}
