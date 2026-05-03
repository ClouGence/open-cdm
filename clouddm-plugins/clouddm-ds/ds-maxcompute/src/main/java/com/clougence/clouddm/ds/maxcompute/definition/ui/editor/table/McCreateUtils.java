//package com.clougence.clouddm.ds.dameng.definition.ui.editor.table;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.clougence.adapter.dm.dialect.DmDialect;
//import com.clougence.adapter.dm.domain.DmPartitionType;
//import com.clougence.adapter.dm.umi.DmAttributeNames;
//import com.clougence.schema.DsType;
//import com.clougence.schema.dialect.Dialect;
//import com.clougence.schema.editor.domain.*;
//import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
//import com.clougence.schema.editor.triggers.TriggerContext;
//import com.clougence.schema.metadata.CaseSensitivityType;
//import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
//import com.clougence.utils.CollectionUtils;
//import com.clougence.utils.StringUtils;
//
///**
// * @Author: Ekko
// * @Date: 2023-10-08 16:46
// */
//public class McCreateUtils extends AbstractSqlBuilder {
//
//    public McCreateUtils(){
//    }
//
//    @Override
//    public DsType getDataSourceType() { return DsType.Dameng; }
//
//    @Override
//    public Dialect getDialect() { return DmDialect.INSTANCE; }
//
//    public List<String> buildCreate(TriggerContext buildContext, String schema, String table, ETable eTable) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        CaseSensitivityType caseSensitivity = buildContext.getCaseSensitivityType();
//        List<String> ddlScripts = new ArrayList<>();
//
//        StringBuilder sqlBuild = new StringBuilder();
//        Map<String, String> tableAttr = eTable.getAttribute();
//        String tableType = tableAttr.get(DmAttributeNames.TABLE_TYPE.getCodeKey());
//        sqlBuild.append("create ");
//        if (StringUtils.isNotBlank(tableType) && !"normal".equalsIgnoreCase(tableType)) {
//            sqlBuild.append(tableType.toLowerCase());
//        }
//        sqlBuild.append(" table ");
//        sqlBuild.append(fmtTable(useDelimited, caseSensitivity, null, eTable.getSchema(), eTable.getName()));
//        sqlBuild.append("(\n");
//
//        // columns
//        List<EColumn> columnList = eTable.getColumnList();
//        for (int i = 0; i < columnList.size(); i++) {
//            if (i != 0) {
//                sqlBuild.append(",\n");
//            }
//            EColumn eColumn = columnList.get(i);
//            buildColumn(sqlBuild, buildContext, eColumn);
//        }
//        // pk
//        EPrimaryKey primaryKey = eTable.getPrimaryKey();
//        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
//            sqlBuild.append(",\n");
//            buildPrimaryKey(sqlBuild, buildContext, primaryKey);
//        }
//        // foreign key
//        List<EForeignKey> foreignKeys = eTable.getForeignKeys();
//        if (foreignKeys != null && !foreignKeys.isEmpty()) {
//            for (EForeignKey foreignKey : foreignKeys) {
//                sqlBuild.append(",\n");
//                buildForeignKey(sqlBuild, buildContext, schema, table, foreignKey);
//            }
//        }
//        // constraints
//        List<EConstraint> constraints = eTable.getConstraints();
//        if (constraints != null && !constraints.isEmpty()) {
//            for (EConstraint constraint : constraints) {
//                sqlBuild.append(",\n");
//                buildConstraint(sqlBuild, buildContext, schema, table, constraint);
//            }
//        }
//
//        sqlBuild.append(")");
//        //partition
//        EPartition partition = eTable.getPartition();
//        if (partition != null) {
//            buildPartition(sqlBuild, buildContext, schema, table, partition);
//        }
//        sqlBuild.append(";");
//        ddlScripts.add(sqlBuild.toString());
//
//        // idx
//        List<EIndex> indices = eTable.getIndices();
//        if (indices != null && !indices.isEmpty()) {
//            for (EIndex index : indices) {
//                StringBuilder indexBuild = new StringBuilder();
//                buildIndex(indexBuild, buildContext, schema, table, index);
//                ddlScripts.add(indexBuild.toString());
//            }
//        }
//
//        // private List<EForeignKey> foreignKeys = new ArrayList<>();
//
//        // table comment
//        if (StringUtils.isNotBlank(eTable.getComment())) {
//            StringBuilder commentSql = new StringBuilder();
//            this.buildTableComment(commentSql, buildContext, eTable);
//            ddlScripts.add(commentSql.toString());
//        }
//
//        // columns comment
//        for (EColumn eColumn : columnList) {
//            if (StringUtils.isBlank(eColumn.getComment())) {
//                continue;
//            }
//            StringBuilder columnCommentSql = new StringBuilder();
//            this.buildColumnComment(columnCommentSql, buildContext, eTable, eColumn);
//            ddlScripts.add(columnCommentSql.toString());
//        }
//
//        return ddlScripts;
//    }
//
//    private void buildPartition(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EPartition partition) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        for (int i = 0; i < partition.getDefinitions().size(); i++) {
//            String p;
//            if (i == 0) {
//                p = "\npartition";
//            } else if (i > 1) {
//                p = ",\nsubpartition";
//            } else {
//                p = "\nsubpartition";
//            }
//            EPartitionDefinition definition = partition.getDefinitions().get(i);
//            sqlBuild.append(p).append(" by ").append(definition.getType());
//            sqlBuild.append(" (").append(definition.getExpression()).append(")");
//            if(StringUtils.isNotEmpty(definition.getAttribute().get(DmAttributeNames.PARTITION_ITEM_RANGE_INTERVAL.getCodeKey()))){
//                sqlBuild.append("interval (").append(definition.getAttribute().get(DmAttributeNames.PARTITION_ITEM_RANGE_INTERVAL.getCodeKey())).append(") ");
//            }
//            if (i != 0 && CollectionUtils.isNotEmpty(definition.getPartitionTemplate())) {
//                sqlBuild.append(" ").append("subpartition template(\n");
//                for (int j = 0; j < definition.getPartitionTemplate().size(); j++) {
//                    if (j != 0) {
//                        sqlBuild.append(",\n");
//                    }
//                    EPartitionItem ePartitionItem = definition.getPartitionTemplate().get(j);
//                    sqlBuild.append("subpartition ").append(getDialect().fmtName(useDelimited, ePartitionItem.getName())).append(" ");
//                    buildPartitionItemValues(sqlBuild, ePartitionItem, definition.getType());
//                    buildPartitionItemConfig(sqlBuild, ePartitionItem);
//                }
//                sqlBuild.append("\n)");
//            }
//        }
//
//        sqlBuild.append("\n(");
//        for (int i = 0; i < partition.getItems().size(); i++) {
//            if (i != 0) {
//                sqlBuild.append(",\n");
//            }
//            EPartitionDefinition definition = partition.getDefinitions().get(0);
//            EPartitionItem ePartitionItem = partition.getItems().get(i);
//            sqlBuild.append("partition ");
//            sqlBuild.append(getDialect().fmtName(useDelimited, ePartitionItem.getName())).append(" ");
//            buildPartitionItemValues(sqlBuild, ePartitionItem, definition.getType());
//            buildPartitionItemConfig(sqlBuild, ePartitionItem);
//            if (CollectionUtils.isNotEmpty(ePartitionItem.getSubPartitionItems())) {
//                buildSubPartitionItems(sqlBuild, ePartitionItem.getSubPartitionItems(), partition.getDefinitions(), 1);
//            }
//        }
//        sqlBuild.append("\n)");
//    }
//
//    private void buildSubPartitionItems(StringBuilder sqlBuild, List<EPartitionItem> subPartitionItems, List<EPartitionDefinition> definitions, int count) {
//        sqlBuild.append("\n(");
//        for (int i = 0; i < subPartitionItems.size(); i++) {
//            if (i != 0) {
//                sqlBuild.append(",\n");
//            }
//            EPartitionDefinition definition = definitions.get(count);
//            EPartitionItem ePartitionItem = subPartitionItems.get(i);
//            sqlBuild.append("subpartition ");
//            sqlBuild.append(getDialect().fmtName(true, ePartitionItem.getName())).append(" ");
//            buildPartitionItemValues(sqlBuild, ePartitionItem, definition.getType());
//            buildPartitionItemConfig(sqlBuild, ePartitionItem);
//            if (CollectionUtils.isNotEmpty(ePartitionItem.getSubPartitionItems())) {
//                buildSubPartitionItems(sqlBuild, ePartitionItem.getSubPartitionItems(), definitions, count + 1);
//            }
//        }
//        sqlBuild.append("\n)");
//    }
//
//    private void buildPartitionItemConfig(StringBuilder sqlBuild, EPartitionItem ePartitionItem) {
//        Map<String, String> attribute = ePartitionItem.getAttribute();
//        String initial = attribute.get(DmAttributeNames.PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS.getCodeKey());
//        String next = attribute.get(DmAttributeNames.PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS.getCodeKey());
//        String min = attribute.get(DmAttributeNames.PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS.getCodeKey());
//        String fill = attribute.get(DmAttributeNames.PARTITION_ITEM_FILL_RATIO.getCodeKey());
//
//        if (StringUtils.isNotEmpty(initial) || StringUtils.isNotEmpty(next) || StringUtils.isNotEmpty(min) || StringUtils.isNotEmpty(fill)
//            || StringUtils.isNotEmpty(ePartitionItem.getTablespace())) {
//            boolean needComma = false;
//            sqlBuild.append(" storage(");
//            if (StringUtils.isNotEmpty(initial)) {
//                sqlBuild.append("initial ").append(initial);
//                needComma = true;
//            }
//            if (StringUtils.isNotEmpty(next)) {
//                if (needComma) {
//                    sqlBuild.append(",");
//                }
//                sqlBuild.append("next ").append(next);
//                needComma = true;
//            }
//            if (StringUtils.isNotEmpty(min)) {
//                if (needComma) {
//                    sqlBuild.append(",");
//                }
//                sqlBuild.append("minextents ").append(min);
//                needComma = true;
//            }
//            if (StringUtils.isNotEmpty(fill)) {
//                if (needComma) {
//                    sqlBuild.append(",");
//                }
//                sqlBuild.append("fillfactor ").append(fill);
//                needComma = true;
//            }
//            if (StringUtils.isNotEmpty(ePartitionItem.getTablespace())) {
//                if (needComma) {
//                    sqlBuild.append(",");
//                }
//                sqlBuild.append("on ").append(getDialect().fmtName(true, ePartitionItem.getTablespace()));
//            }
//            sqlBuild.append(")");
//        }
//    }
//
//    private void buildPartitionItemValues(StringBuilder sqlBuild, EPartitionItem ePartitionItem, String type) {
//        DmPartitionType dmPartitionType = DmPartitionType.valueOfCode(type);
//        if (dmPartitionType == DmPartitionType.RANGE) {
//            String s = safeToString(ePartitionItem.getAttribute().get(DmAttributeNames.PARTITION_ITEM_BOUNDARY_TYPE.getCodeKey()));
//            sqlBuild.append("values ").append(s.toLowerCase()).append(" than ").append("(").append(ePartitionItem.getDescription()).append(")");
//        } else if (dmPartitionType == DmPartitionType.LIST) {
//            sqlBuild.append("values (").append(ePartitionItem.getDescription()).append(")");
//        } else if (dmPartitionType == DmPartitionType.HASH) {
//            return;
//        } else {
//            throw new UnsupportedOperationException("unsupported partition type: " + type);
//        }
//    }
//
//    private void buildForeignKey(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EForeignKey foreignKey) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        sqlBuild.append("constraint ");
//        sqlBuild.append(getDialect().fmtName(useDelimited, foreignKey.getName())).append(" ");
//
//        List<String> tableColumn = new ArrayList<>();
//        List<String> referenceTableColumn = new ArrayList<>();
//        foreignKey.getReferenceMapping().forEach((k, v) -> {
//            tableColumn.add(k);
//            referenceTableColumn.add(v);
//        });
//        sqlBuild.append("foreign key(");
//        for (int i = 0; i < tableColumn.size(); i++) {
//            if (i != 0) {
//                sqlBuild.append(",");
//            }
//            sqlBuild.append(getDialect().fmtName(useDelimited, tableColumn.get(i)));
//        }
//
//        sqlBuild.append(")references ");
//
//        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, foreignKey.getReferenceSchema(), foreignKey.getReferenceTable()));
//        sqlBuild.append("(");
//        for (int i = 0; i < referenceTableColumn.size(); i++) {
//            if (i != 0) {
//                sqlBuild.append(",");
//            }
//            sqlBuild.append(getDialect().fmtName(useDelimited, referenceTableColumn.get(i)));
//        }
//
//        sqlBuild.append(")");
//
//        if (foreignKey.getUpdateRule() != null && foreignKey.getUpdateRule() != RdbForeignKeyRule.NoAction) {
//            sqlBuild.append(" on update ").append(foreignKey.getUpdateRule());
//        }
//        if (foreignKey.getDeleteRule() != null && foreignKey.getDeleteRule() != RdbForeignKeyRule.NoAction) {
//            sqlBuild.append(" on delete ").append(foreignKey.getUpdateRule());
//        }
//    }
//
//    private void buildConstraint(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EConstraint constraint) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        sqlBuild.append("constraint ");
//        sqlBuild.append(getDialect().fmtName(useDelimited, constraint.getName())).append(" ");
//
//        EConstraintType type = constraint.getType();
//
//        if (type == EConstraintType.Check) {
//            sqlBuild.append("check(");
//            sqlBuild.append(constraint.getExpression());
//            sqlBuild.append(")");
//        } else if (type == EConstraintType.Unique) {
//            sqlBuild.append("unique(");
//            for (int i = 0; i < constraint.getColumnList().size(); i++) {
//                if (i != 0) {
//                    sqlBuild.append(",");
//                }
//                sqlBuild.append(getDialect().fmtName(useDelimited, constraint.getColumnList().get(i)));
//            }
//            sqlBuild.append(")");
//        } else {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    protected void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EIndex eIndex) {
//        boolean useDelimited = buildContext.isUseDelimited();
//
//        Map<String, String> attr = eIndex.getAttribute();
//        String indexType = attr.get(DmAttributeNames.INDEX_WAY.getCodeKey());
//        sqlBuild.append(" CREATE ");
//        if (StringUtils.isNotBlank(indexType) && !"NORMAL".equals(indexType)) {
//            sqlBuild.append(indexType);
//        }
//        sqlBuild.append(" INDEX ");
//        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, eIndex.getName()));
//        sqlBuild.append(" ON ");
//        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
//        sqlBuild.append("(");
//        List<String> columnList = eIndex.getColumnList();
//        for (int i = 0; i < columnList.size(); i++) {
//            String column = columnList.get(i);
//            if (i > 0) {
//                sqlBuild.append(", ");
//            }
//            sqlBuild.append(getDialect().fmtName(useDelimited, column));
//        }
//        sqlBuild.append(");");
//    }
//
//    protected void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn) {
//        boolean useDelimited = buildContext.isUseDelimited();
//
//        sqlBuild.append("  ");
//        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
//        String columnType = DmTypeUtils.buildColumnType(eColumn, buildContext);
//        sqlBuild.append("  ");
//        sqlBuild.append(columnType);
//    }
//
//    protected void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        sqlBuild.append("primary key(");
//        List<String> pkColumns = primaryKey.getColumnList();
//
//        for (int i = 0; i < pkColumns.size(); ++i) {
//            String column = pkColumns.get(i);
//            if (i > 0) {
//                sqlBuild.append(", ");
//            }
//
//            sqlBuild.append(this.getDialect().fmtName(useDelimited, column));
//        }
//
//        sqlBuild.append(")");
//    }
//
//    protected void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
//            sqlBuild.append("comment on table ");
//            sqlBuild.append(this.getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
//            sqlBuild.append(" is '").append(this.getDialect().fmtComment(eTable.getComment())).append("';");
//        }
//    }
//
//    protected void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
//        boolean useDelimited = buildContext.isUseDelimited();
//        sqlBuild.append("comment on column ");
//        sqlBuild.append(this.getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
//        sqlBuild.append(".");
//        sqlBuild.append(this.getDialect().fmtName(useDelimited, eColumn.getName()));
//        sqlBuild.append(" is '").append(this.getDialect().fmtComment(eColumn.getComment())).append("';");
//    }
//}
