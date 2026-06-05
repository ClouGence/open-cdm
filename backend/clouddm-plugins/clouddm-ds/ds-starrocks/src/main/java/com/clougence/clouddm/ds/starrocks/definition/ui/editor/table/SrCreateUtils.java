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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import static com.clougence.adapter.starrocks.StarRocksAttributeNames.*;

import java.text.MessageFormat;
import java.util.*;

import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.adapter.starrocks.StarRocksMainVersion;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

/**
 * @author Ekko
 * @date 2023/12/6 17:38
*/
public class SrCreateUtils extends AbstractSqlBuilder {

    public SrCreateUtils(){
    }

    public DsType getDataSourceType() { return DsType.StarRocks; }

    public Dialect getDialect() { return StarRocksDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext context, ETable eTable) {
        StringBuilder sqlBuild = new StringBuilder();
        Map<String, String> tableAtr = eTable.getAttribute();
        String external = StarRocksAttributeNames.EXTERNAL.getValue(tableAtr);
        if (StringUtils.isNotBlank(external) && external.equalsIgnoreCase("true")) {
            sqlBuild.append("CREATE EXTERNAL TABLE ");
        } else {
            sqlBuild.append("CREATE TABLE ");
        }

        sqlBuild.append(fmtTable(context.isUseDelimited(), null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        buildColumns(sqlBuild, context, eTable);
        buildIndices(sqlBuild, context, eTable);

        sqlBuild.append("\n) \n");

        String engineValue = StarRocksAttributeNames.ENGINE.getValue(tableAtr);
        if (StringUtils.isNotBlank(engineValue)) {
            sqlBuild.append("ENGINE=").append(engineValue);
            sqlBuild.append("\n");
        }

        EPrimaryKey pk = eTable.getPrimaryKey();
        if (Objects.nonNull(pk)) {
            String pkName = pk.getPrimaryKeyName();
            if (pkName.contains("PRIMARY") || pkName.contains("pk")) {
                buildPkClause(sqlBuild, context, pk.clone(), null);
                sqlBuild.append("\n");
                buildTableComment(sqlBuild, context, eTable);
                buildDistributedKey(eTable, sqlBuild, context, pk.getColumnList());
            } else if (pkName.contains("UNIQUE") || pkName.contains("uk")) {
                buildUkClause(sqlBuild, context, pk.clone());
                sqlBuild.append("\n");
                buildTableComment(sqlBuild, context, eTable);
                buildDistributedKey(eTable, sqlBuild, context, pk.getColumnList());
            } else if (pkName.contains("AGGREGATE")) {
                buildAggKey(sqlBuild, context, pk.clone());
                sqlBuild.append("\n");
                buildTableComment(sqlBuild, context, eTable);
                buildDistributedKey(eTable, sqlBuild, context, pk.getColumnList());
            } else if (pkName.contains("DUPLICATE")) {
                buildDupKey(sqlBuild, context, pk.clone());
                sqlBuild.append("\n");
                buildTableComment(sqlBuild, context, eTable);
                buildDistributedKey(eTable, sqlBuild, context, pk.getColumnList());
            }
        } else {
            String firstColName = eTable.getColumnList().get(0).getName();
            buildTableComment(sqlBuild, context, eTable);
            buildDistributedKey(eTable, sqlBuild, context, Collections.singletonList(firstColName));
        }

        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    protected void buildColumns(StringBuilder sqlBuild, TriggerContext context, ETable eTable) {
        List<EColumn> reorderedECols = reorderByPkUkCommonCols(eTable, null);

        for (int i = 0; i < reorderedECols.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = reorderedECols.get(i).clone();
            this.buildColumn(sqlBuild, context, eTable, eColumn);
        }
    }

    protected void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            String comment = this.getDialect().fmtComment(eTable.getComment());
            sqlBuild.append("COMMENT '").append(comment).append("'\n");
        }

    }

    protected void fillDistributed(StringBuilder sqlBuild, ETable table, TriggerContext buildContext, List<String> distCols) {
        String distributedType = StarRocksAttributeNames.DISTRIBUTED_BY_TYPE.getValue(table.getAttribute());
        String distributedColumns = StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getValue(table.getAttribute());
        MainVersion mainVersion = buildContext.getSrcDsInfo().getMainVersion();

        List<String> columnList = new ArrayList<>();
        if (StringUtils.isNotBlank(distributedColumns)) {
            Object[] objList = (Object[]) JSON.parse(distributedColumns);
            for (Object o : objList) {
                columnList.add(o.toString());
            }
        } else if (CollectionUtils.isNotEmpty(table.getColumnList())) {
            String name = table.getColumnList().get(0).getName();
            columnList.add(name);
        }

        if (StringUtils.isNotBlank(distributedType)) {
            if (distributedType.equalsIgnoreCase("RANDOM")) {
                sqlBuild.append("DISTRIBUTED BY RANDOM");
            } else if (distributedType.equalsIgnoreCase("HASH")) {
                sqlBuild.append("DISTRIBUTED BY HASH");
                sqlBuild.append("(");
                buildColStrWithComma(sqlBuild, columnList, buildContext);
                sqlBuild.append(")");
            }
        } else if (!StarRocksMainVersion.StarRocks_3_1.isLt(mainVersion)) {
            sqlBuild.append("DISTRIBUTED BY HASH");
            sqlBuild.append("(");
            buildColStrWithComma(sqlBuild, columnList, buildContext);
            sqlBuild.append(")");
        }

        MainVersion version = buildContext.getSrcDsInfo().getMainVersion();
        String bucketNum = table.getAttribute().get(BUCKET_NUMBER.getCodeKey());
        if (StringUtils.isBlank(bucketNum)) {
            bucketNum = buildContext.getAttribute(BUCKET_NUMBER);
        }
        // sr 2.5.7 support auto buckets, not required buckets
        if ((StringUtils.isBlank(bucketNum) || StringUtils.equalsIgnoreCase("null", bucketNum)) && !StarRocksMainVersion.StarRocks_2_5.isLt(version)) {
            sqlBuild.append(" BUCKETS ").append("1");
        } else if (StringUtils.isNotBlank(bucketNum) && !StringUtils.equalsIgnoreCase("null", bucketNum)) {
            sqlBuild.append(" BUCKETS ").append(bucketNum);
        }
        sqlBuild.append(" \n");
    }

    private void buildColumn(StringBuilder sqlBuilder, TriggerContext context, ETable eTable, EColumn eColumn) {
        StarRocksTypes sqlTypes = StarRocksTypes.valueOfCode(eColumn.getDbType());
        sqlBuilder.append("  ");
        sqlBuilder.append(fmtName(context.isUseDelimited(), eColumn.getName()));
        String columnType = SrTypeUtils.buildColumnTypeOnly(eColumn, context);
        sqlBuilder.append(" ");
        sqlBuilder.append(columnType);
        if (StringUtils.isNotBlank(StarRocksAttributeNames.AGG_TYPE.getValue(eColumn.getAttribute()))) {
            String value = StarRocksAttributeNames.AGG_TYPE.getValue(eColumn.getAttribute());
            sqlBuilder.append(" ").append(value);
        }

        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && primaryKey.getColumnList().contains(eColumn.getName())) {
            sqlBuilder.append(" ").append("NOT NULL");
        } else if (Boolean.TRUE.equals(eColumn.getNullable())) {
            sqlBuilder.append(" ").append("NULL");
        } else {
            sqlBuilder.append(" ").append("NOT NULL");
        }

        String defaultValue = SrTypeUtils.buildDefault(sqlTypes, eColumn, context.getMappingHandler(), context);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuilder.append(" ");
            sqlBuilder.append(defaultValue);
            sqlBuilder.append(" ");
        }

        String autoIncrement = StarRocksAttributeNames.AUTO_INCREMENT.getValue(eColumn.getAttribute());
        if (StringUtils.isNotBlank(autoIncrement) && autoIncrement.equalsIgnoreCase("true")) {
            sqlBuilder.append(" ").append("AUTO_INCREMENT ");
        }

        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuilder.append(" COMMENT '").append(comment).append("'");
        }
    }

    protected void buildUkClause(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("UNIQUE KEY (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildDupKey(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("DUPLICATE KEY (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildAggKey(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("AGGREGATE KEY (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildDistributedKey(ETable table, StringBuilder sqlBuild, TriggerContext buildContext, List<String> distCols) {
        String partitionExpr = fetchPartitionExprFromContext(table);

        fillPartition(sqlBuild, partitionExpr);

        fillDistributed(sqlBuild, table, buildContext, distCols);

        fillOrderBy(sqlBuild, table, buildContext);

        String globalProp = fetchGlobalPropFromContext(table, buildContext);
        fillTableProps(sqlBuild, partitionExpr, globalProp);
    }

    protected String fetchPartitionExprFromContext(ETable table) {
        return table.getAttribute().get(PARTITION_EXPR.getCodeKey());
    }

    protected void fillPartition(StringBuilder sqlBuild, String partitionExpr) {
        String pureParExpr = purePartitionExpr(partitionExpr);
        if (StringUtils.isNotBlank(pureParExpr)) {
            sqlBuild.append("PARTITION BY ").append(pureParExpr).append(" \n");
        }
    }

    protected void fillTableProps(StringBuilder sqlBuild, String partitionExpr, String globalProps) {
        String pureTabProps = pureProps(partitionExpr);

        if (StringUtils.isBlank(globalProps)) {
            sqlBuild.append("PROPERTIES(");
            if (StringUtils.isNotBlank(pureTabProps)) {
                sqlBuild.append(pureTabProps).append(",");
            }

            sqlBuild.append("\"replication_num\" = \"1\",\n");
            sqlBuild.append("\"enable_persistent_index\" = \"true\")");
        } else {
            sqlBuild.append("PROPERTIES(");
            if (StringUtils.isNotBlank(pureTabProps)) {
                sqlBuild.append(pureTabProps).append(",");
            }

            String gProps = pureProps(globalProps);
            if (StringUtils.isNotBlank(gProps)) {
                sqlBuild.append(gProps).append(")");
            }
        }
    }

    protected String purePartitionExpr(String parExpr) {
        if (StringUtils.isBlank(parExpr) || !parExpr.contains("PARTITION BY")) {
            return null;
        }

        String parLeft = parExpr.substring(parExpr.indexOf("PARTITION BY") + "PARTITION BY".length());
        if (StringUtils.isBlank(parLeft)) {
            return null;
        }

        if (parLeft.contains("PROPERTIES")) {
            return parLeft.substring(0, parLeft.indexOf("PROPERTIES"));
        } else {
            return parLeft;
        }
    }

    protected String pureProps(String partitionExpr) {
        if (StringUtils.isBlank(partitionExpr) || !partitionExpr.contains("PROPERTIES")) {
            return null;
        }

        String propLeft = partitionExpr.substring(partitionExpr.indexOf("PROPERTIES"));
        if (StringUtils.isBlank(propLeft)) {
            return null;
        }

        if (propLeft.contains("PARTITION BY")) {
            propLeft = propLeft.substring(propLeft.indexOf("(") + 1, propLeft.indexOf("PARTITION BY"));
        }

        return propLeft.substring(propLeft.indexOf("(") + 1, propLeft.lastIndexOf(")"));
    }

    protected String fetchGlobalPropFromContext(ETable table, TriggerContext buildContext) {
        String globalProps = table.getAttribute().get(PROPERTIES_CONFIG.getCodeKey());
        if (StringUtils.isBlank(globalProps)) {
            globalProps = buildContext.getAttribute(PROPERTIES_CONFIG);
        }

        return globalProps;
    }

    private void fillOrderBy(StringBuilder sqlBuild, ETable table, TriggerContext buildContext) {
        if (table.getPrimaryKey() != null) {
            String value = StarRocksAttributeNames.ORDER_BY.getValue(table.getPrimaryKey().getAttribute());
            if (StringUtils.isNotBlank(value)) {
                Object[] objList = (Object[]) JSON.parse(value);
                List<String> columnList = new ArrayList<>();
                for (Object o : objList) {
                    columnList.add(o.toString());
                }
                if (CollectionUtils.isNotEmpty(columnList)) {
                    sqlBuild.append("ORDER BY (");
                    buildColStrWithComma(sqlBuild, columnList, buildContext);
                    sqlBuild.append(") \n");
                }
            }
        }
    }

    @SneakyThrows
    protected void buildNormalIndex(StringBuilder sqlBuild, TriggerContext context, EIndex eIndex) {
        if (eIndex.getColumnList().size() > 1) {
            return;
        }

        sqlBuild.append(",\n");

        String subPartJson = SUB_PART.getValue(eIndex.getAttribute());
        Map<String, Integer> subPart = StringUtils.isBlank(subPartJson) ? null : new ObjectMapper().readValue(subPartJson, new TypeReference<Map<String, Integer>>() {
        });

        sqlBuild.append(" INDEX ");
        sqlBuild.append(fmtIndex(context.isUseDelimited(), null, eIndex.getName()));
        sqlBuild.append("(");

        for (int i = 0; i < eIndex.getColumnList().size(); i++) {
            String column = eIndex.getColumnList().get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }

            sqlBuild.append(fmtName(context.isUseDelimited(), column));
            if (subPart != null && subPart.containsKey(column)) {
                Integer value = subPart.get(column);
                if (value != null) {
                    sqlBuild.append("(").append(value).append(")");
                }
            }
        }

        sqlBuild.append(") USING BITMAP");
        String comment = COMMENT.getValue(eIndex.getAttribute());
        if (StringUtils.isNotBlank(comment)) {
            sqlBuild.append("COMMENT ").append("\"").append(comment).append("\"");
        }
    }

    protected void buildIndices(StringBuilder sqlBuild, TriggerContext context, ETable eTable) {
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                if (index.getType() == EIndexType.Unique) {
                    continue;
                }

                buildNormalIndex(sqlBuild, context, index.clone());
            }
        }
    }

    protected void buildPkClause(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk, List<String> partitionCols) {
        sqlBuilder.append("PRIMARY KEY (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);

        if (partitionCols != null) {
            List<String> extraCols = new ArrayList<>();
            for (String col : partitionCols) {
                if (pk.getColumnList().contains(col)) {
                    continue;
                }

                extraCols.add(col);
            }

            if (!extraCols.isEmpty()) {
                sqlBuilder.append(",");
                buildColStrWithComma(sqlBuilder, extraCols, context);
            }
        }

        sqlBuilder.append(")");
    }

    protected void buildColStrWithComma(StringBuilder sqlBuilder, List<String> cols, TriggerContext context) {
        boolean first = true;
        for (String col : cols) {
            if (first) {
                first = false;
            } else {
                sqlBuilder.append(", ");
            }

            sqlBuilder.append(fmtName(context.isUseDelimited(), col));
        }
    }

    protected List<EColumn> reorderByPkUkCommonCols(ETable eTable, List<String> partitionCols) {
        List<String> ukColNames = getUkColNames(eTable);
        List<String> pkColNames = getPkColNames(eTable);

        List<EColumn> reorderedECols = new ArrayList<>();
        List<EColumn> pkEColumnList = new ArrayList<>();
        List<EColumn> ukEColumnList = new ArrayList<>();
        List<EColumn> parEColumnList = new ArrayList<>();
        List<EColumn> nonPkEColumnList = new ArrayList<>();

        // modify by @hanlizhi, sort rule: first pkColumns, second partitionCols, third ukColumns, last nonPkColumn
        HashMap<String, EColumn> columnHashMap = buildAllColumnMap(eTable.getColumnList());
        HashSet<String> keyColSet = new HashSet<>();
        for (String pkColName : pkColNames) {
            if (keyColSet.contains(pkColName)) {
                continue;
            }
            pkEColumnList.add(getNotNullColumn(pkColName, columnHashMap));
            keyColSet.add(pkColName);
        }

        for (String ukColName : ukColNames) {
            if (keyColSet.contains(ukColName)) {
                continue;
            }
            ukEColumnList.add(getNotNullColumn(ukColName, columnHashMap));
            keyColSet.add(ukColName);
        }

        if (partitionCols != null) {
            for (String partitionCol : partitionCols) {
                if (keyColSet.contains(partitionCol)) {
                    continue;
                }

                parEColumnList.add(getNotNullColumn(partitionCol, columnHashMap));
                keyColSet.add(partitionCol);
            }
        }

        for (EColumn eCol : eTable.getColumnList()) {
            if (keyColSet.contains(eCol.getName())) {
                continue;
            }
            nonPkEColumnList.add(eCol);
            keyColSet.add(eCol.getName());
        }

        reorderedECols.addAll(pkEColumnList);
        reorderedECols.addAll(parEColumnList);
        reorderedECols.addAll(ukEColumnList);
        reorderedECols.addAll(nonPkEColumnList);
        return reorderedECols;
    }

    protected List<String> getUkColNames(ETable eTable) {
        List<String> ukColNames = new ArrayList<>();
        for (EIndex index : eTable.getIndices()) {
            if (index.getType() == EIndexType.Unique) {
                ukColNames.addAll(index.getColumnList());
            }
        }

        return ukColNames;
    }

    protected List<String> getPkColNames(ETable eTable) {
        if (eTable.getPrimaryKey() != null) {
            return eTable.getPrimaryKey().getColumnList();
        } else {
            return new ArrayList<>();
        }
    }

    private HashMap<String, EColumn> buildAllColumnMap(List<EColumn> eColumns) {
        HashMap<String, EColumn> columnHashMap = new HashMap<>();
        for (EColumn eColumn : eColumns) {
            columnHashMap.put(eColumn.getName(), eColumn);
        }
        return columnHashMap;
    }

    private EColumn getNotNullColumn(String name, HashMap<String, EColumn> allColumnMap) {
        EColumn result = allColumnMap.get(name);
        if (result == null) {
            throw new NoColumnException(MessageFormat.format("[SrCreateUtils] allColumnMap:{0} not exist column:{0}", JSON.toString(allColumnMap), name));
        }
        return result;
    }
}
