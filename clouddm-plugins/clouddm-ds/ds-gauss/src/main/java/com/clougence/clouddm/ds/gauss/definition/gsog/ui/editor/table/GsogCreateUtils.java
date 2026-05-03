package com.clougence.clouddm.ds.gauss.definition.gsog.ui.editor.table;

import static com.clougence.adapter.gauss.GaussDBAttributeNames.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.gauss.GaussDBAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgCreateUtils;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @author caishan
 * @date 2023/8/14 14:39
 */
public class GsogCreateUtils extends PgCreateUtils {

    @Override
    protected String buildTableCreateOption(StringBuilder sqlBuild, ETable eTable) {
        Map<String, String> attrMap = eTable.getAttribute();
        String fatherArr = attrMap.get(INHERITED_FROM.getCodeKey());
        if (StringUtils.isNotBlank(fatherArr)) {
            if (fatherArr.startsWith("[") && fatherArr.endsWith("]")) {
                fatherArr = fatherArr.replace("[", "");
                fatherArr = fatherArr.replace("]", "");
                if (StringUtils.isNotBlank(fatherArr)) {
                    String[] split = fatherArr.split(",");
                    sqlBuild.append("INHERITS (");
                    for (int i = 0; i < split.length; i++) {
                        if (i != 0) {
                            sqlBuild.append(",");
                        }
                        sqlBuild.append(split[i]);
                    }
                    sqlBuild.append(") ");
                }
            }
        }

        //build with
        buildTableWithOption(sqlBuild, attrMap);

        String tablespace = attrMap.get(TABLESPACE.getCodeKey());
        if (StringUtils.isNotBlank(tablespace)) {
            sqlBuild.append("TABLESPACE ");
            sqlBuild.append(tablespace);
        }

        sqlBuild.append(";");
        return sqlBuild.toString();
    }

    protected void buildTableWithOption(StringBuilder sqlBuild, Map<String, String> attrMap) {
        boolean hasParam = false;
        String orientation = attrMap.get(ORIENTATION.getCodeKey());
        String storageType = attrMap.get(GaussDBAttributeNames.STORAGE_TYPE.getCodeKey());

        List<String> option = new ArrayList<>();
        // if (StringUtils.isNotBlank(orientation)) {
        //     option.add("orientation=" + orientation);
        //     hasParam = true;
        // }
        if (StringUtils.isNotBlank(storageType)) {
            option.add("storage_type=" + storageType);
            hasParam = true;
        }
        if (hasParam) {
            sqlBuild.append("WITH (");
            sqlBuild.append(String.join(",", option));
            sqlBuild.append(")\n");
        }
    }
}
