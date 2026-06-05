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
package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/9/6 14:56
*/
@Slf4j
public class DrEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new DrEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.Doris; }

    @Override
    public Dialect getDialect() { return DorisDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext context, String catalog, String schema, String table) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new DrCreateUtils().buildCreate(buildContext, eTable);
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (eTable.getComment() != null) {
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" MODIFY COMMENT '").append(getDialect().fmtComment(eTable.getComment())).append("';");
            return Collections.singletonList(sqlBuild.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "alter table " + fmtTable(useDelimited, null, schema, table) + " RENAME " + fmtName(useDelimited, newName) + ";";
        return Collections.singletonList(sqlBuild);
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
        // doris add not null column, must have default value
        if (!Boolean.TRUE.equals(columnInfo.getNullable()) && StringUtils.isBlank(columnInfo.getDefaultValue())) {
            if (DorisTypes.valueOfCode(columnInfo.getDbType()).isNumber()) {
                columnInfo.setDefaultValue("0");
            } else {
                columnInfo.setDefaultValue("");
            }
        }
        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        //
        sqlBuild.append(" add column ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(DrTypeUtils.buildColumnType(columnInfo, context));
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropColumn(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo) {
        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" drop column ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        //
        sqlBuild.append(" rename column ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(fmtName(context.isUseDelimited(), newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        DorisTypes sqlTypes = DorisTypes.valueOfCode(newInfo.getDbType());

        StringBuilder sqlBuild = buildAlterTable(context, catalog, schema, table);
        sqlBuild.append(" modify column ").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(DrTypeUtils.buildColumnType(newInfo, context));

        String defaultValue = DrTypeUtils.buildDefault(sqlTypes, newInfo, context.getMappingHandler(), context);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuild.append(" ");
            sqlBuild.append(defaultValue);
            sqlBuild.append(" ");
        }
        if (StringUtils.isNotBlank(newInfo.getComment())) {
            String comment = getDialect().fmtComment(newInfo.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
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
    public List<String> createIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = context.isUseDelimited();
        //
        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index ").append(fmtIndex(useDelimited, null, indexInfo.getName()));
        } else {
            sqlBuild.append("create index ").append(fmtIndex(useDelimited, null, indexInfo.getName()));
        }

        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        //
        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }

        sqlBuild.append(")");
        if (StringUtils.isNotEmpty(indexInfo.getComment())) {
            sqlBuild.append(" comment '").append(indexInfo.getComment()).append("'");
        }
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        String sqlBuild = "drop index " + fmtIndex(context.isUseDelimited(), null, indexInfo.getName()) + " on " + getDialect().fmtName(true, schema) + "."
                          + getDialect().fmtName(true, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        //
        ArrayList<String> indexScripts = new ArrayList<>();
        //        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        //
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        //        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        //
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }
}
