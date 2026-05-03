package com.clougence.clouddm.ds.polardb.definition.porpg.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.polar.porpg.PolarDBPgAttributeNames;
import com.clougence.clouddm.ds.polardb.dialect.porpg.PolarDBPgDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class PorPgEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new PorPgEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.PolarDBPostgre; }

    public Dialect getDialect() { return PolarDBPgDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" rename to ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on table ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(" is '" + getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        return null;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new PorPgCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop table ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" add " + fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + PorPgTypeUtils.buildColumnType(columnInfo, buildContext));
        sqlBuild.append(";");

        ArrayList<String> columnScripts = new ArrayList<>();
        columnScripts.add(sqlBuild.toString());

        columnScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        return columnScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop column " + fmtName(useDelimited, columnInfo.getName()));
        if (buildContext.isCascade()) {
            sqlBuild.append(" cascade");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" rename column ");
        sqlBuild.append(fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" to " + fmtName(useDelimited, newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" alter column " + fmtName(useDelimited, columnInfo.getName()));
        String columnType = PorPgTypeUtils.buildColumnType(newInfo, buildContext);
        sqlBuild.append(" type " + columnType);
        sqlBuild.append(" using " + fmtName(useDelimited, columnInfo.getName()) + "::" + columnType);
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on column ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(".");
        sqlBuild.append(fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" is '" + getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index " + fmtIndex(useDelimited, null, indexInfo.getName()));
        } else {
            sqlBuild.append("create index " + fmtIndex(useDelimited, null, indexInfo.getName()));
        }
        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));

        String typeNameValue = PolarDBPgAttributeNames.INDEX_TYPE_NAME.getValue(indexInfo.getAttribute());
        if (StringUtils.isNotBlank(typeNameValue) && !StringUtils.equalsIgnoreCase(typeNameValue, "btree")) {
            sqlBuild.append(" using " + typeNameValue + " ");
        }

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(");");

        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder("drop index ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter index ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexInfo.getName()));
        sqlBuild.append(" to ");
        sqlBuild.append(fmtIndex(useDelimited, null, newIndexName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" add constraint ");
        String pkName = "";
        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName())) {
            pkName = table + "_pkey";
        } else {
            pkName = primaryInfo.getPrimaryKeyName();
        }

        sqlBuild.append(fmtIndex(useDelimited, null, pkName));
        sqlBuild.append(" primary key ");
        sqlBuild.append("(");
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop constraint ");
        sqlBuild.append(primaryInfo.getPrimaryKeyName());
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needAddColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
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

    @Override
    public List<String> createForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return Collections.emptyList();// TODO
    }

    @Override
    public List<String> dropForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return Collections.emptyList();// TODO
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo, String newForeignKeyName) {
        return Collections.emptyList();// TODO
    }
}
