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
package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames;
import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class AdbMyCreateUtils extends AbstractSqlBuilder {

    public Dialect getDialect() { return AdbMySqlDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        //
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("create table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");
        //
        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i);
            buildColumn(sqlBuild, buildContext, eColumn);
        }
        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, primaryKey);
        }
        // idx
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                sqlBuild.append(",\n");
                buildIndex(sqlBuild, buildContext, index.clone(), eTable);
            }
        }
        //
        sqlBuild.append(")");
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            String comment = getDialect().fmtComment(eTable.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        String columnType = AdbMyTypeUtils.buildColumnType(eColumn, buildContext);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("primary key(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    public void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, EIndex eIndex, ETable eTable) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> indexAttr = eIndex.getAttribute();

        String typeValue = AdsMyUmiAttributeNames.INDEX_TYPE.getValue(indexAttr);
        String wayValue = AdsMyUmiAttributeNames.INDEX_WAY.getValue(indexAttr);
        String indexName = eIndex.getName();
        String append = "";

        if (wayValue.equalsIgnoreCase("FullText")) {
            sqlBuild.append(" FULLTEXT INDEX ");
        } else if (wayValue.equalsIgnoreCase("ANN")) {
            sqlBuild.append(" ANN INDEX ");
        }

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append(" ");
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");

        if (wayValue.equalsIgnoreCase("ANN")) {
            String annAlgorithm = AdsMyUmiAttributeNames.ANN_ALGORITHM.getValue(indexAttr);
            String annDisFunction = AdsMyUmiAttributeNames.ANN_DISFUNCTION.getValue(indexAttr);
            if (StringUtils.isNotBlank(annAlgorithm)) {
                sqlBuild.append(" algorithm=").append(annAlgorithm);
            }
            if (StringUtils.isNotBlank(annAlgorithm)) {
                sqlBuild.append(" distancemeasure=").append(annDisFunction);
            }
        }
        sqlBuild.append(append);
    }
}
