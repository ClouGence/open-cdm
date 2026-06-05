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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2023-09-08 14:32
 */
@Slf4j
public class OraEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new OraEditorProvider();

    public DsType getDataSourceType() { return DsType.Oracle; }

    public Dialect getDialect() { return OracleDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (eTable.getComment() != null) {
            boolean useDelimited = buildContext.isUseDelimited();

            StringBuilder sqlBuild = new StringBuilder();
            sqlBuild.append("comment on table ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
            sqlBuild.append(" is '" + getDialect().fmtComment(eTable.getComment()) + "'");
            sqlBuild.append(";");
            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new OraCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        StringBuilder alterTable = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(alterTable);

        sqlBuild.append(" rename ");
        sqlBuild.append(" to ");
        sqlBuild.append(getDialect().fmtName(useDelimited, newName));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(" is '" + getDialect().fmtComment(comment) + "'");

        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" add " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + OraTypeUtils.buildColumnType(columnInfo, buildContext, eTable.getPrimaryKey()));
        sqlBuild.append(";");
        List<String> ddlScripts = new ArrayList<>();
        ddlScripts.add(sqlBuild.toString());

        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            ddlScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        }

        return ddlScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" drop column " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" rename column " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" to " + getDialect().fmtName(useDelimited, newColumnName));

        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> ddlScripts = new ArrayList<>();

        EColumn oldColumn = columnInfo.clone();
        EColumn newColumn = newInfo.clone();
        oldColumn.setComment("");
        newColumn.setComment("");
        if (oldColumn.testChanged(newColumn)) {
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" modify " + getDialect().fmtName(useDelimited, newInfo.getName()));
            sqlBuild.append(" ");
            sqlBuild.append(OraTypeUtils.buildColumnType(newInfo, buildContext, eTable.getPrimaryKey()));
            sqlBuild.append(";");
            ddlScripts.add(sqlBuild.toString());
        }

        if (newInfo.getComment() != null) {
            ddlScripts.addAll(columnComment(buildContext, catalog, schema, table, newInfo, newInfo.getComment(), eTable));
        }
        return ddlScripts;
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on column ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(".");
        sqlBuild.append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" is '" + getDialect().fmtComment(comment) + "'");
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" add primary key (");
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop primary key");
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
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index " + getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        } else {
            sqlBuild.append("create index " + getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        }
        sqlBuild.append(" on ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop index ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter index ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        sqlBuild.append(" rename to ");
        sqlBuild.append(getDialect().fmtName(useDelimited, newIndexName));
        return Collections.singletonList(sqlBuild.toString());
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
}
