package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-10-07 14:46
 */
public class Db2EditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new Db2EditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.Db2; }

    public Dialect getDialect() { return Db2Dialect.INSTANCE; }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" ADD ").append(fmtName(true, columnInfo.getName()));
        sqlBuild.append(" ").append(Db2TypeUtils.buildColumnType(columnInfo, buildContext, eTable.getPrimaryKey()));
        sqlBuild.append(";");

        ArrayList<String> columnScripts = new ArrayList<>();
        columnScripts.add(sqlBuild.toString());
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            columnScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        }
        return columnScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" drop column ").append(fmtName(true, columnInfo.getName()));

        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" rename column ");
        sqlBuild.append(fmtName(true, columnInfo.getName()));
        sqlBuild.append(" to ").append(fmtName(true, newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        Db2Types sqlTypes = Db2Types.valueOfCode(newInfo.getDbType());
        Db2Types oldSqlTypes = Db2Types.valueOfCode(columnInfo.getDbType());
        List<String> result = new ArrayList<>();

        String columnType = Db2TypeUtils.buildColumnType(sqlTypes, newInfo, buildContext);
        String oldColumnType = Db2TypeUtils.buildColumnType(oldSqlTypes, columnInfo, buildContext);
        if (!columnType.equals(oldColumnType)) {
            StringBuilder sqlBuild = buildAlterTable(schema, table);
            sqlBuild.append(buildAlterColumn(newInfo));
            sqlBuild.append(" set data type ").append(columnType);
            sqlBuild.append(";");
            result.add(sqlBuild.toString());
        }

        if (Boolean.TRUE.equals(columnInfo.getNullable()) != Boolean.TRUE.equals(newInfo.getNullable())) {
            StringBuilder sqlBuild = buildAlterTable(schema, table);
            sqlBuild.append(buildAlterColumn(columnInfo));
            sqlBuild.append(" set ");
            if (Boolean.TRUE.equals(newInfo.getNullable())) {
                sqlBuild.append(" null ");
            } else {
                sqlBuild.append(" not null ");
            }
            sqlBuild.append(";");
            result.add(sqlBuild.toString());

        }

        String defaultValue = Db2TypeUtils.buildDefault(sqlTypes, newInfo, buildContext);
        String oldDefaultValue = Db2TypeUtils.buildDefault(sqlTypes, columnInfo, buildContext);
        if (!defaultValue.equals(oldDefaultValue)) {
            StringBuilder sqlBuild = buildAlterTable(schema, table);
            sqlBuild.append(buildAlterColumn(columnInfo));
            sqlBuild.append(" set default").append(defaultValue);
            sqlBuild.append(";");
            result.add(sqlBuild.toString());

        }

        if (columnInfo.getComment() == null) {
            columnInfo.setComment("");
        }
        if (newInfo.getComment() == null) {
            newInfo.setComment("");
        }
        if (!columnInfo.getComment().equals(newInfo.getComment())) {
            String sqlBuild = "comment on column " + fmtTable(true, null, schema, table) + "."
                              + fmtName(true, newInfo.getName()) + " is '" + getDialect().fmtComment(newInfo.getComment()) + "';";
            result.add(sqlBuild);
        }

        return result;
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        String sqlBuild = "comment on column " + fmtTable(true, null, schema, table) + "."
                          + fmtName(true, columnInfo.getName()) + " is '" + getDialect().fmtComment(comment) + "';";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index ").append(fmtIndex(true, null, indexInfo.getName()));
        } else {
            sqlBuild.append("create index ").append(fmtIndex(true, null, indexInfo.getName()));
        }
        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(true, null, schema, table));
        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(true, column));
        }
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        String sqlBuild = "drop index " + fmtIndex(true, null, indexInfo.getName()) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        String sqlBuild = "rename index " + //
                          fmtIndex(true, null, indexInfo.getName())//
                          + " to " + //
                          fmtIndex(true, null, newIndexName) //
                          + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        return indexScripts;
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" add constraint ");
        String pkName = "";
        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName())) {
            pkName = table + "_pkey";
        } else {
            pkName = "PK";
        }

        sqlBuild.append(fmtIndex(true, null, pkName));
        sqlBuild.append(" primary key ");
        sqlBuild.append("(");
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(true, column));
        }
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" drop primary key;");
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
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        StringBuilder sqlBuild = buildAlterTable(schema, table);
        sqlBuild.append(" rename table to ");
        sqlBuild.append(fmtTable(true, null, schema, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        String sqlBuild = "comment on table " + fmtTable(true, null, schema, table) + " is '" + getDialect().fmtComment(comment) + "';";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new Db2CreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (eTable.getComment() != null) {
            String sqlBuild = "comment on table " + fmtTable(true, null, schema, table) + " is '" + getDialect().fmtComment(eTable.getComment()) + "';";
            return Collections.singletonList(sqlBuild);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        String sqlBuild = "drop table " + fmtTable(true, null, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    protected StringBuilder buildAlterTable(String schema, String table) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(fmtTable(true, null, schema, table));
        return sqlBuild;
    }

    protected StringBuilder buildAlterColumn(EColumn columnInfo) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append(" alter column ").append(fmtName(true, columnInfo.getName()));
        return sqlBuild;
    }

    @Override
    public List<String> createForeignKey(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey) {
        return null;
    }

    @Override
    public List<String> dropForeignKey(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey) {
        return null;
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey, String s3) {
        return null;
    }
}
