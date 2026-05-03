package com.clougence.clouddm.sdk.execute.meta;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public interface DsMetaService {

    void testConnect();

    String getVersion();

    /** If not supported, null should be returned */
    String getCurrentCatalog();

    /** If not supported, null should be returned */
    String getCurrentSchema();

    List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern);

    Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    default Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) {
        throw new UnsupportedOperationException();
    }

    Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames);

    String loadTableEditor(Map<UmiTypes, Object> levelsParam, String table);

    List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);
}
