package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.adapter.starrocks.StarRocksMainVersion;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;

/**
 * @Author: Ekko
 * @Date: 2023-09-04 11:26
 */
public class SrEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    @Override
    public DsType getDataSourceType() { return DsType.StarRocks; }

    @Override
    public Dialect getDialect() { return StarRocksDialect.INSTANCE; }

    public static final SqlBuilder INSTANCE = new SrEditorProvider();

    protected StringBuilder buildAlterTable(TriggerContext context, String catalog, String schema, String table) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("ALTER TABLE ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> columnRename(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = this.buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" rename column ").append(this.fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" to ").append(this.fmtName(context.isUseDelimited(), newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "ALTER TABLE " + fmtTable(useDelimited, null, schema, table) + " RENAME " + fmtName(useDelimited, newName) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new SrCreateUtils().buildCreate(buildContext, eTable);
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        if (eTable.getComment() != null && StarRocksMainVersion.StarRocks_3_0.isLe(mainVersion)) {
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" COMMENT '").append(getDialect().fmtComment(eTable.getComment())).append("';");
            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" comment '").append(getDialect().fmtComment(comment)).append("';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableDrop(TriggerContext context, String catalog, String schema, String table, ETable eTable) {
        String sqlBuild = "drop table " + fmtTable(context.isUseDelimited(), null, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> addColumn(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        // StarRocks add not null column, must have default value
        if (!Boolean.TRUE.equals(columnInfo.getNullable()) && StringUtils.isBlank(columnInfo.getDefaultValue())) {
            if (StarRocksTypes.valueOfCode(columnInfo.getDbType()).isNumber()) {
                columnInfo.setDefaultValue("0");
            } else {
                columnInfo.setDefaultValue("");
            }
        }
        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" ADD COLUMN ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(SrTypeUtils.buildColumnTypeOnly(columnInfo, context));

        String aggType = columnInfo.getAttribute().get(StarRocksAttributeNames.AGG_TYPE.getCodeKey());
        if (StringUtils.isNotBlank(aggType)) {
            sqlBuild.append(" ").append(aggType.toUpperCase()).append(" ");
        }

        // only support NOT NULL -> NULL
        if (Boolean.TRUE.equals(columnInfo.getNullable())) {
            sqlBuild.append(" ").append("NULL").append(" ");
        } else {
            sqlBuild.append(" ").append("NOT NULL").append(" ");
        }

        if (columnInfo.getDefaultValue() != null) {
            sqlBuild.append(" DEFAULT ");
            sqlBuild.append("\"");
            sqlBuild.append(columnInfo.getDefaultValue());
            sqlBuild.append("\"");
        }

        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }

        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropColumn(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo) {
        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" DROP COLUMN ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        String dbColumns = eTable.getAttribute().get(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        Object parse = JSON.parse(dbColumns);
        if (parse instanceof List) {
            List<String> columnList = (List<String>) parse;
            if (columnList.contains(columnInfo.getName())) {
                // distributed columns unsupported modify
                return Collections.emptyList();
            }
        }

        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" MODIFY COLUMN ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(SrTypeUtils.buildColumnTypeOnly(newInfo, context));

        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        String aggType = columnInfo.getAttribute().get(StarRocksAttributeNames.AGG_TYPE.getCodeKey());
        if (StringUtils.isNotBlank(aggType)) {
            sqlBuild.append(" ").append(aggType.toUpperCase()).append(" ");
        } else if (primaryKey != null && primaryKey.getColumnList().contains(columnInfo.getName())) {
            sqlBuild.append(" ").append("KEY").append(" ");
        }

        // only support NOT NULL -> NULL
        if (!Boolean.TRUE.equals(columnInfo.getNullable()) && Boolean.TRUE.equals(newInfo.getNullable())) {
            sqlBuild.append(" ").append("NULL").append(" ");
        } else if (Boolean.TRUE.equals(columnInfo.getNullable())) {
            sqlBuild.append(" ").append("NULL").append(" ");
        } else if (!Boolean.TRUE.equals(columnInfo.getNullable())) {
            sqlBuild.append(" ").append("NOT NULL").append(" ");
        }

        // default can not be modify, but must have old value
        if (columnInfo.getDefaultValue() != null) {
            sqlBuild.append(" DEFAULT ");
            sqlBuild.append("\"");
            sqlBuild.append(columnInfo.getDefaultValue());
            sqlBuild.append("\"");
        }

        if (StringUtils.isNotBlank(newInfo.getComment())) {
            String comment = getDialect().fmtComment(newInfo.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnComment(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        EColumn newInfo = columnInfo.clone();
        newInfo.setComment(comment);
        List<String> diffChange = Collections.singletonList("comment");
        return columnChange(context, catalog, schema, table, columnInfo, newInfo, diffChange, eTable);
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        return new ArrayList<>(this.createIndex(buildContext, catalog, schema, table, copy));
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        return new ArrayList<>(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
    }

    @Override
    public List<String> createIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = context.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();

        sqlBuild.append("CREATE INDEX ").append(fmtIndex(useDelimited, null, indexInfo.getName()));

        sqlBuild.append(" ON ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }

        sqlBuild.append(") USING BITMAP");

        if (StringUtils.isNotBlank(indexInfo.getComment())) {
            sqlBuild.append(" COMMENT ");
            sqlBuild.append("\"").append(indexInfo.getComment()).append("\"");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = context.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        String dropSql = "DROP INDEX " + fmtIndex(context.isUseDelimited(), null, indexInfo.getName());
        sqlBuild.append(dropSql);
        sqlBuild.append(" ON ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

}
