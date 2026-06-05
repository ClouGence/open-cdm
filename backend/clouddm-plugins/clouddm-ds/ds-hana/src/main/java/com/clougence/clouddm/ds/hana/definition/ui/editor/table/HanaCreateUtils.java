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
package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import static com.clougence.adapter.hana.HanaAttributeNames.GEO_TYPE;
import static com.clougence.adapter.hana.HanaAttributeNames.ORDER_TYPE;

import java.text.MessageFormat;
import java.util.*;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/12/3
 **/
@Slf4j
public class HanaCreateUtils extends AbstractSqlBuilder {

    @Override
    public Dialect getDialect() { return HanaDialect.INSTANCE; }

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

    protected List<EColumn> reorderByPkUkCommonCols(ETable eTable) {
        List<String> ukColNames = getUkColNames(eTable);
        List<String> pkColNames = getPkColNames(eTable);

        List<EColumn> reorderedECols = new ArrayList<>();
        List<EColumn> pkEColumnList = new ArrayList<>();
        List<EColumn> ukEColumnList = new ArrayList<>();
        List<EColumn> nonPkEColumnList = new ArrayList<>();

        // modify by @hanlizhi, sort rule: first pkColumns, second ukColumns, last nonPkColumn
        HashMap<String, EColumn> columnHashMap = buildAllColumnMap(eTable.getColumnList());
        HashSet<String> pkAndUkColumnNameSet = new HashSet<>();
        for (String pkColName : pkColNames) {
            if (pkAndUkColumnNameSet.contains(pkColName)) {
                continue;
            }
            pkEColumnList.add(getNotNullColumn(pkColName, columnHashMap));
            pkAndUkColumnNameSet.add(pkColName);
        }
        for (String ukColName : ukColNames) {
            if (pkAndUkColumnNameSet.contains(ukColName)) {
                continue;
            }
            ukEColumnList.add(getNotNullColumn(ukColName, columnHashMap));
            pkAndUkColumnNameSet.add(ukColName);
        }
        for (EColumn eCol : eTable.getColumnList()) {
            if (pkAndUkColumnNameSet.contains(eCol.getName())) {
                continue;
            }
            nonPkEColumnList.add(eCol);
            pkAndUkColumnNameSet.add(eCol.getName());
        }

        reorderedECols.addAll(pkEColumnList);
        reorderedECols.addAll(ukEColumnList);
        reorderedECols.addAll(nonPkEColumnList);
        return reorderedECols;
    }

    private EColumn getNotNullColumn(String name, HashMap<String, EColumn> allColumnMap) {
        EColumn result = allColumnMap.get(name);
        if (result == null) {
            throw new NoColumnException(MessageFormat.format("[HanaCreateUtils] allColumnMap:{0} not exist column:{0}", JSON.toString(allColumnMap), name));
        }
        return result;
    }

    private HashMap<String, EColumn> buildAllColumnMap(List<EColumn> eColumns) {
        HashMap<String, EColumn> columnHashMap = new HashMap<>();
        for (EColumn eColumn : eColumns) {
            columnHashMap.put(eColumn.getName(), eColumn);
        }
        return columnHashMap;
    }

    protected void buildColumns(StringBuilder sqlBuild, TriggerContext context, ETable eTable) {
        List<EColumn> reorderedECols = reorderByPkUkCommonCols(eTable);

        for (int i = 0; i < reorderedECols.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = reorderedECols.get(i).clone();
            buildColumn(sqlBuild, context, eColumn);
        }
    }

    public List<String> buildCreate(TriggerContext context, ETable eTable) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE ");
        String tableType = HanaAttributeNames.TABLE_TYPE.getValue(eTable.getAttribute());
        if (!StringUtils.isBlank(tableType)) {
            sqlBuild.append(tableType);
        }
        sqlBuild.append(" TABLE ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append(" (\n");

        buildColumns(sqlBuild, context, eTable);
        // pk
        buildPkClause(sqlBuild, context, eTable);

        sqlBuild.append(") \n");
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            String comment = getDialect().fmtComment(eTable.getComment());
            sqlBuild.append("COMMENT '").append(comment).append("'\n");
        }

        sqlBuild.append(";");
        List<String> result = new LinkedList<>();
        // table definition
        result.add(sqlBuild.toString());
        // idx definition
        List<EIndex> indices = eTable.getIndices();
        if (CollectionUtils.isNotEmpty(indices)) {
            for (EIndex index : indices) {
                result.add(buildIndex(context, index.clone(), eTable.getSchema(), eTable.getName()));
            }
        }
        return result;
    }

    @SneakyThrows
    protected String buildIndex(TriggerContext buildContext, EIndex eIndex, String schema, String tableName) {
        String orderTypeJson = ORDER_TYPE.getValue(eIndex.getAttribute());
        Map<String, String> orderType = StringUtils.isBlank(orderTypeJson) ? null : new ObjectMapper().readValue(orderTypeJson, new TypeReference<Map<String, String>>() {
        });

        StringBuilder result = new StringBuilder();
        result.append("CREATE ");
        String hanaIndexType = convertToHanaIndexType(eIndex);
        if (StringUtils.isNotEmpty(hanaIndexType)) {
            if (hanaIndexType.equals(EIndexType.Normal.getTypeName())) {
                hanaIndexType = "";
            }
            result.append(hanaIndexType).append(" ");
        }
        result.append("INDEX ");
        if (eIndex.getType() == EIndexType.Unique) {
            result.append(getDialect().fmtName(buildContext.isUseDelimited(), eIndex.getName()));
        } else {
            result.append(getDialect().fmtName(buildContext.isUseDelimited(), eIndex.getName()));
        }
        result.append(" ON ");
        result.append(fmtTable(buildContext.isUseDelimited(), null, schema, tableName));
        result.append(" ( ");
        buildColStrWithComma(result, eIndex.getColumnList(), buildContext, eIndex.getAttribute(), orderType);
        result.append(" ) ;");
        return result.toString();
    }

    private String convertToHanaIndexType(EIndex eIndex) {
        String idxType = HanaAttributeNames.INDEX_TYPE.getValue(eIndex.getAttribute());
        if (StringUtils.isBlank(idxType)) {
            return StringUtils.EMPTY;
        }
        return idxType;
    }

    private void buildColumn(StringBuilder sqlBuilder, TriggerContext context, EColumn eColumn) {
        sqlBuilder.append("  ");
        sqlBuilder.append(fmtName(context.isUseDelimited(), eColumn.getName()));
        String columnType = StringUtils.trim(HanaTypeUtils.buildColumnType(eColumn, context));
        sqlBuilder.append(" ");
        sqlBuilder.append(columnType);
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuilder.append(" COMMENT '").append(comment).append("'");
        }
    }

    private void buildPkClause(StringBuilder sqlBuilder, TriggerContext context, ETable eTable) {
        EPrimaryKey pk = eTable.getPrimaryKey();
        if (pk != null && CollectionUtils.isNotEmpty(pk.getColumnList())) {
            sqlBuilder.append(",\n");
            sqlBuilder.append(" PRIMARY KEY (");
            buildColStrWithComma(sqlBuilder, pk.getColumnList(), context, eTable.getAttribute(), null);
            sqlBuilder.append(")");
        }
    }

    @SneakyThrows
    protected void buildColStrWithComma(StringBuilder sqlBuilder, List<String> cols, TriggerContext context, Map<String, String> attribute, Map<String, String> orderType) {
        String geoTypeJson = GEO_TYPE.getValue(attribute);
        Map<String, String> geoType = StringUtils.isBlank(geoTypeJson) ? null : new ObjectMapper().readValue(geoTypeJson, new TypeReference<Map<String, String>>() {
        });

        boolean first = true;
        for (String col : cols) {
            if (first) {
                first = false;
            } else {
                sqlBuilder.append(", ");
            }

            sqlBuilder.append(fmtName(context.isUseDelimited(), col));

            // geo
            if (geoType != null && geoType.get(col) != null) {
                sqlBuilder.append(" ").append(geoType.get(col));
            }

            // index need order
            if (orderType != null && orderType.containsKey(col)) {
                String order = orderType.get(col);
                if (StringUtils.equalsIgnoreCase(order, "DESC")) {
                    sqlBuilder.append(" ").append(order);
                }
            }
        }
    }
}
