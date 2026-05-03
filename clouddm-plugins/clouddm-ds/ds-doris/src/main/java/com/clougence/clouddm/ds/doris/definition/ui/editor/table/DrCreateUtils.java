package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import java.text.MessageFormat;
import java.util.*;

import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/9/6 14:55
*/
@Slf4j
public class DrCreateUtils extends AbstractSqlBuilder {

    // tips: doris use mysql dialect
    @Override
    public Dialect getDialect() { return DorisDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext context, ETable eTable) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("create table if not exists ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        EPrimaryKey pk = eTable.getPrimaryKey();
        Map<String, String> tableAtr = eTable.getAttribute();
        boolean model = StringUtils.isNotBlank(DorisAttributeNames.KEY_TYPE.getValue(tableAtr)) && pk != null && CollectionUtils.isNotEmpty(pk.getColumnList());

        buildColumns(sqlBuild, context, eTable);
        buildIndices(sqlBuild, context, eTable);

        sqlBuild.append(") \n");

        if (model) {
            String value = DorisAttributeNames.KEY_TYPE.getValue(tableAtr);
            if (value.equals("PRIMARY KEY")) {
                buildPkClause(sqlBuild, context, pk.clone());
            } else if (value.equals("UNIQUE KEY")) {
                buildUkClause(sqlBuild, context, pk.clone());
            } else if (value.equals("AGGREGATE KEY")) {
                buildAggKey(sqlBuild, context, pk.clone());
            } else if (value.equals("DUPLICATE KEY")) {
                buildDupKey(sqlBuild, context, pk.clone());
            }
            sqlBuild.append("\n");
        }

        buildTableComment(sqlBuild, context, eTable);
        buildDistributedKey(eTable, sqlBuild, context);
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    protected void buildColumns(StringBuilder sqlBuild, TriggerContext context, ETable eTable) {
        List<EColumn> reorderedECols = reorderByPkUkCommonCols(eTable);

        for (int i = 0; i < reorderedECols.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = reorderedECols.get(i).clone();
            this.buildColumn(sqlBuild, context, eTable, eColumn);
        }
    }

    private void buildColumn(StringBuilder sqlBuilder, TriggerContext context, ETable eTable, EColumn eColumn) {
        DorisTypes sqlTypes = DorisTypes.valueOfCode(eColumn.getDbType());
        sqlBuilder.append("  ");
        sqlBuilder.append(fmtName(context.isUseDelimited(), eColumn.getName()));
        String columnType = DrTypeUtils.buildColumnTypeOnly(eColumn, context);
        sqlBuilder.append(" ");
        sqlBuilder.append(columnType);
        if (StringUtils.isNotBlank(DorisAttributeNames.AGG_TYPE.getValue(eColumn.getAttribute()))) {
            String value = DorisAttributeNames.AGG_TYPE.getValue(eColumn.getAttribute());
            sqlBuilder.append(" ").append(value);
        }

        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && primaryKey.getColumnList().contains(eColumn.getName())) {
            sqlBuilder.append(" ").append("not null");
        } else if (Boolean.TRUE.equals(eColumn.getNullable())) {
            sqlBuilder.append(" ").append("null");
        } else {
            sqlBuilder.append(" ").append("not null");
        }

        String defaultValue = DrTypeUtils.buildDefault(sqlTypes, eColumn, context.getMappingHandler(), context);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuilder.append(" ");
            sqlBuilder.append(defaultValue);
            sqlBuilder.append(" ");
        }

        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuilder.append(" comment '").append(comment).append("'");
        }
    }

    protected void buildUkClause(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("UNIQUE key (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildDupKey(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("DUPLICATE key (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildAggKey(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("AGGREGATE key (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected void buildDistributedKey(ETable table, StringBuilder sqlBuild, TriggerContext buildContext) {
        String bucketNumber = table.getAttribute().get(DorisAttributeNames.BUCKET_NUMBER.getCodeKey());
        if (StringUtils.isBlank(bucketNumber)) {
            bucketNumber = buildContext.getAttribute(DorisAttributeNames.BUCKET_NUMBER);
        }
        String distributedColumns = DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getValue(table.getAttribute());
        String distributedType = DorisAttributeNames.DISTRIBUTED_BY_TYPE.getValue(table.getAttribute());

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
                sqlBuild.append("DISTRIBUTED BY HASH(");
                buildColStrWithComma(sqlBuild, columnList, buildContext);
                sqlBuild.append(")");
            }
        } else {
            sqlBuild.append("DISTRIBUTED BY HASH(");
            buildColStrWithComma(sqlBuild, columnList, buildContext);
            sqlBuild.append(")");
        }

        MainVersion version = buildContext.getTarDsInfo().getMainVersion();
        // sr 2.5.7 support auto buckets, not required buckets
        if ((StringUtils.isBlank(bucketNumber) || StringUtils.equalsIgnoreCase("null", bucketNumber)) && version != null) {
            sqlBuild.append(" \n");
        } else if (StringUtils.isNotBlank(bucketNumber)) {
            sqlBuild.append(" BUCKETS ").append(bucketNumber).append(" \n");
        } else {
            sqlBuild.append(" BUCKETS ").append(1).append(" \n");
        }

        String migProps = table.getAttribute().get(DorisAttributeNames.PROPERTIES_CONFIG.getCodeKey());
        if (StringUtils.isBlank(migProps)) {
            migProps = buildContext.getAttribute(DorisAttributeNames.PROPERTIES_CONFIG);
        }
        if (StringUtils.isBlank(migProps)) {
            sqlBuild.append("PROPERTIES(\"replication_num\" = \"1\")");
        } else {
            sqlBuild.append(migProps);
        }
    }

    protected void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            String comment = getDialect().fmtComment(eTable.getComment());
            sqlBuild.append("COMMENT '").append(comment).append("'\n");
        }
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

    protected void buildIndices(StringBuilder sqlBuild, TriggerContext context, ETable eTable) {
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                buildNormalIndex(sqlBuild, context, index.clone());
            }
        }
    }

    @SneakyThrows
    private void buildNormalIndex(StringBuilder sqlBuild, TriggerContext context, EIndex eIndex) {
        if (eIndex.getColumnList().size() > 1) {
            return;
        }

        sqlBuild.append(",\n");
        sqlBuild.append(" index ");
        sqlBuild.append(fmtIndex(context.isUseDelimited(), null, eIndex.getName()));
        sqlBuild.append("(");

        for (int i = 0; i < eIndex.getColumnList().size(); i++) {
            String column = eIndex.getColumnList().get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(context.isUseDelimited(), column));
        }

        sqlBuild.append(")");
        //        sqlBuild.append(" using inverted");
        if (StringUtils.isNotEmpty(eIndex.getComment())) {
            sqlBuild.append(" COMMENT '").append(eIndex.getComment()).append("'");
        }
    }

    protected void buildPkClause(StringBuilder sqlBuilder, TriggerContext context, EPrimaryKey pk) {
        sqlBuilder.append("primary key (");
        buildColStrWithComma(sqlBuilder, pk.getColumnList(), context);
        sqlBuilder.append(")");
    }

    protected List<String> getPkColNames(ETable eTable) {
        if (eTable.getPrimaryKey() != null) {
            return eTable.getPrimaryKey().getColumnList();
        } else {
            return new ArrayList<>();
        }
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
            throw new NoColumnException(MessageFormat.format("[DorisCreateUtils] allColumnMap:{0} not exist column:{0}", JSON.toString(allColumnMap), name));
        }
        return result;
    }
}
