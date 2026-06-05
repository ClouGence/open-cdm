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
package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table;

import static com.clougence.adapter.clickhouse.ClickHouseAttributeNames.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.adapter.clickhouse.ClickHouseTableEngine;
import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/9/14 10:50
*/
@Slf4j
public class ChCreateUtils extends AbstractSqlBuilder {

    @Override
    public Dialect getDialect() { return ClickHouseDialect.INSTANCE; }

    protected void appendOnCluster(TriggerContext context, StringBuilder sqlBuild, boolean multiReplica) {
        if (multiReplica) {
            String clusterName = CK_CLUSTER.getValue(context.getAttributes());
            if (clusterName != null) {
                sqlBuild.append(" ON CLUSTER ").append(getDialect().fmtName(true, clusterName));
            }
        }
    }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE TABLE ");
        sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));

        String multiReplicaVal = MULTI_REPLICA.getValue(buildContext.getAttributes());
        boolean multiReplica = false;
        if (multiReplicaVal != null) {
            multiReplica = Boolean.parseBoolean(multiReplicaVal);
        }

        //        String shardName = SHARD_NAME.getValue(buildContext.getAttributes());
        //        String replicaName = REPLICA_NAME.getValue(buildContext.getAttributes());

        appendOnCluster(buildContext, sqlBuild, multiReplica);

        sqlBuild.append(" (\n");

        // COLUMNS
        List<EColumn> columnList = eTable.getColumnList();
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i);
            buildColumn(sqlBuild, buildContext, eColumn, primaryKey);
        }
        sqlBuild.append(") ");

        // SortingKey
        List<String> sortingKeys = new ArrayList<>();
        for (EColumn eColumn : columnList) {
            String sortingKeyStr = SORTING_KEY.getValue(eColumn.getAttribute());
            if (!StringUtils.equalsIgnoreCase("true", sortingKeyStr)) {
                continue;
            }

            if (!sortingKeys.contains(eColumn.getName()) && !Boolean.TRUE.equals(eColumn.getNullable())) {
                sortingKeys.add(eColumn.getName());
            }
        }

        sqlBuild.append("\n");

        // ENGINE
        String engineFullValue = ENGINE_FULL.getValue(eTable.getAttribute());
        if (StringUtils.isNotBlank(engineFullValue)) {
            sqlBuild.append(engineFullValue).append(";");
            return Collections.singletonList(sqlBuild.toString());
        } else if (sortingKeys.isEmpty()) {
            // modify by junyu. user mergeTree for
            if (multiReplica) {
                //                String tabPath = genReplicatedTabPath(shardName, eTable.getName());
                sqlBuild.append("engine ").append(ClickHouseTableEngine.ReplicatedMergeTree.getExpr());
                //                sqlBuild.append("('").append(tabPath).append("','").append(replicaName).append("')");
            } else {
                sqlBuild.append("engine ").append(ClickHouseTableEngine.MergeTree.getExpr()).append(" ");
            }
        } else {
            String engineVal = ENGINE.getValue(eTable.getAttribute());
            //            ClickHouseTableEngine engine = ClickHouseTableEngine.valueOf(engineVal);
            //            if (engine == ClickHouseTableEngine.ReplicatedReplacingMergeTree) {
            //                String tabPath = genReplicatedTabPath(shardName, eTable.getName());
            //                sqlBuild.append("engine ").append(engineVal);
            //                sqlBuild.append("('").append(tabPath).append("','").append(replicaName).append("')");
            //            } else {
            sqlBuild.append("engine ").append(engineVal);
            //            }
        }

        // PK
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(" ");
            buildPrimaryKey(sqlBuild, buildContext, primaryKey);
        }

        sqlBuild.append("\n");
        if (primaryKey == null || CollectionUtils.isEmpty(primaryKey.getColumnList())) {
            buildSortingKey(sqlBuild, buildContext, sortingKeys);
        }

        sqlBuild.append("\n");
        // COMMENT
        if (StringUtils.isNotBlank(eTable.getComment())) {
            if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
                String comment = getDialect().fmtComment(eTable.getComment());
                sqlBuild.append(" COMMENT '").append(comment).append("'");
            }
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    protected void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("PRIMARY KEY(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    protected void buildSortingKey(StringBuilder sqlBuild, TriggerContext buildContext, List<String> sortingKeys) {
        if (sortingKeys.isEmpty()) {
            sqlBuild.append("ORDER BY tuple()");
            return;
        }

        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("ORDER BY(");
        for (int i = 0; i < sortingKeys.size(); i++) {
            String column = sortingKeys.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("  ");
        sqlBuild.append(fmtName(useDelimited, eColumn.getName()));

        ClickHouseTypes sqlTypes = ClickHouseTypes.valueOfCode(eColumn.getDbType());

        String columnType = ChTypeUtils.buildColumnType(sqlTypes, eColumn, buildContext);
        columnType = buildNullable(sqlTypes, Boolean.TRUE.equals(eColumn.getNullable()), columnType, eColumn, primaryKey);
        String defaultValue = ChTypeUtils.buildDefault(sqlTypes, eColumn, buildContext);
        columnType = columnType + " " + defaultValue;

        sqlBuild.append("  ");
        sqlBuild.append(columnType);
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }
    }

    private String buildNullable(ClickHouseTypes sqlTypes, boolean nullable, String columnType, EColumn eColumn, EPrimaryKey ePrimaryKey) {
        if (!nullable) {
            return columnType;
        } else if (ePrimaryKey != null && ePrimaryKey.getColumnList().contains(eColumn.getName())) {
            return columnType;
        } else {
            switch (sqlTypes) {
                case Point:
                    return columnType;
                case Polygon:
                case MultiPolygon:
                    return columnType; // ClickHouse exception, code: 43, Nested type Point cannot be inside Nullable type (version 21.8.2.1)
                default:
                    return "Nullable(" + columnType + ")";
            }
        }
    }
}
