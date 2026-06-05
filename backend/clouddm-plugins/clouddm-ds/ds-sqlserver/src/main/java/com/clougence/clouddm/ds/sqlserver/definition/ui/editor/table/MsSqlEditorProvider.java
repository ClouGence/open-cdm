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
package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/9/12 10:58
*/
public class MsSqlEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new MsSqlEditorProvider();

    public DsType getDataSourceType() { return DsType.SqlServer; }

    public Dialect getDialect() { return SqlServerDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, catalog, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new MsSqlCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (eTable.getComment() != null) {
            StringBuilder sqlBuild = new StringBuilder();

            sqlBuild.append("exec sp_updateextendedproperty ");
            sqlBuild.append(" @name = N'MS_Description', " + "\n");
            sqlBuild.append(" @value = '" + eTable.getComment() + "'" + "\n");
            sqlBuild.append(" @level0type = N'SCHEMA', " + "\n");
            sqlBuild.append(" @level0name = 'dbo', " + "\n");
            sqlBuild.append(" @level1type = N'TABLE', " + "\n");
            sqlBuild.append(" @level1name = 'table_name5_w'");
            sqlBuild.append(";");

            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "drop table " + getDialect().fmtTableName(useDelimited, catalog, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        if (StringUtils.isNotBlank(newName)) {
            StringBuilder sqlBuild = new StringBuilder();
            sqlBuild.append(" exec sp_rename ");
            sqlBuild.append("'").append(table).append("'");
            sqlBuild.append(", ");
            sqlBuild.append("'").append(newName).append("'");
            sqlBuild.append(";");
            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "comment on table " + getDialect().fmtTableName(useDelimited, catalog, schema, table) + " is '" + this.getDialect().fmtComment(comment) + "';";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append(" exec sp_rename ");
        sqlBuild.append("'").append(table).append(".").append(columnInfo.getName()).append("'");
        sqlBuild.append(", ");
        sqlBuild.append("'").append(newColumnName).append("'");
        sqlBuild.append(", ");
        sqlBuild.append("'").append("column").append("'");
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" ADD ").append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" ").append(MsSqlTypeUtils.buildColumnType(columnInfo, buildContext));
        sqlBuild.append(";");

        ArrayList<String> columnScripts = new ArrayList<>();
        columnScripts.add(sqlBuild.toString());
        columnScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        return columnScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop column ").append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        if (buildContext.isCascade()) {
            sqlBuild.append(" cascade");
        }

        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table,//
                                     EColumn columnInfo, EColumn newInfo, List<String> diffChange, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        EColumn oldCInfo = columnInfo.clone();
        EColumn newCInfo = newInfo.clone();
        oldCInfo.setComment("");
        newCInfo.setComment("");
        List<String> sqlList = new ArrayList<>();
        if (oldCInfo.testChanged(newCInfo)) {
            String columnType = MsSqlTypeUtils.buildColumnType(newInfo, buildContext);
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" alter column ").append(getDialect().fmtName(useDelimited, columnInfo.getName()));
            sqlBuild.append(columnType);
            sqlBuild.append(";");
            sqlList.add(sqlBuild.toString());
        }
        if (newInfo.getComment() != null) {
            StringBuilder sqlBuild = new StringBuilder();
            sqlBuild.append("\n");
            sqlBuild.append("exec sp_updateextendedproperty, " + "\n");
            sqlBuild.append(" @name = N'MS_Description', " + "\n");
            sqlBuild.append(" @value = '").append(newInfo.getComment()).append("', ").append("\n");
            sqlBuild.append(" @level0type = N'SCHEMA', " + "\n");
            sqlBuild.append(" @level0name = 'dbo', " + "\n");
            sqlBuild.append(" @level1type = N'TABLE', " + "\n");
            sqlBuild.append(" @level1name = '").append(table).append("', ").append("\n");
            sqlBuild.append(" @level2type = N'COLUMN', " + "\n");
            sqlBuild.append(" @level2name = '").append(newInfo.getName()).append("'");
            sqlBuild.append(";");
            sqlList.add(sqlBuild.toString());
        }
        return sqlList;
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        if (comment != null) {
            schema = StringUtils.isBlank(schema) ? "dbo" : schema;
            boolean useDelimited = buildContext.isUseDelimited();
            String column = getDialect().fmtName(useDelimited, columnInfo.getName());

            StringBuilder sqlBuild = new StringBuilder();
            sqlBuild.append("exec " + catalog + ".sys.sp_addextendedproperty ");
            sqlBuild.append("'MS_Description', N'" + comment + "', ");
            sqlBuild.append("'SCHEMA', " + schema + ", ");
            sqlBuild.append("'TABLE', " + table + ", ");
            sqlBuild.append("'COLUMN', " + column);
            sqlBuild.append(";");
            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index ").append(getDialect().fmtName(useDelimited, indexInfo.getName()));
        } else {
            sqlBuild.append("create index ").append(getDialect().fmtName(useDelimited, indexInfo.getName()));
        }

        sqlBuild.append(" on ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, catalog, schema, table));
        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();

        for (int i = 0; i < columnList.size(); ++i) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }

            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }

        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "drop index " + getDialect().fmtName(useDelimited, indexInfo.getName())//
                          + " on " + getDialect().fmtTableName(useDelimited, catalog, schema, table)//
                          + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "alter index " + getDialect().fmtName(useDelimited, indexInfo.getName()) + " to " + getDialect().fmtName(useDelimited, newIndexName) + ";";
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
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" add constraint ");
        sqlBuild.append(getDialect().fmtName(useDelimited, primaryInfo.getPrimaryKeyName()));
        sqlBuild.append(" primary key ");
        sqlBuild.append("(");
        List<String> columnList = primaryInfo.getColumnList();

        for (int i = 0; i < columnList.size(); ++i) {
            String column = (String) columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }

            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }

        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop constraint ");
        sqlBuild.append(getDialect().fmtName(useDelimited, primaryInfo.getPrimaryKeyName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needAddColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        return indexScripts;
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
