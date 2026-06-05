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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaIndexType;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/12/3
 **/
@Slf4j
public class HanaEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new HanaEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.Hana; }

    @Override
    public Dialect getDialect() { return HanaDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext context, String catalog, String schema, String table) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("ALTER TABLE ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();
        String sqlBuild = "RENAME TABLE " + fmtTable(useDelimited, null, schema, table) + " TO " + fmtName(useDelimited, newName) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("COMMENT ON TABLE ")
            .append(fmtTable(buildContext.isUseDelimited(), null, schema, table))
            .append(" IS '")
            .append(getDialect().fmtComment(comment))
            .append("';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (StringUtils.isBlank(eTable.getComment())) {
            return null;
        }
        List<String> result = new ArrayList<>();
        String comment = eTable.getComment();
        result.add(tableComment(buildContext, catalog, schema, table, comment).get(0));
        return result;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new HanaCreateUtils().buildCreate(buildContext, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext context, String catalog, String schema, String table, ETable eTable) {
        String sqlBuild = "DROP TABLE " + fmtTable(context.isUseDelimited(), null, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> addColumn(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        StringBuilder sqlBuild = buildAlterTable(context, null, schema, table);
        sqlBuild.append(" ADD ( ");
        buildColumn(context, columnInfo, sqlBuild);
        sqlBuild.append(" );");
        return Collections.singletonList(sqlBuild.toString());
    }

    private void buildColumn(TriggerContext context, EColumn columnInfo, StringBuilder sqlBuild) {
        sqlBuild.append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" ").append(StringUtils.trim(HanaTypeUtils.buildColumnType(columnInfo, context)));
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }
    }

    @Override
    public List<String> dropColumn(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo) {
        StringBuilder sqlBuild = buildAlterTable(context, null, schema, table);
        sqlBuild.append(" DROP (").append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append(" RENAME COLUMN ")
            .append(fmtTable(context.isUseDelimited(), null, schema, table))
            .append(".")
            .append(fmtName(context.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" TO ").append(fmtName(context.isUseDelimited(), newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        StringBuilder sqlBuild = buildAlterTable(context, null, schema, table);
        sqlBuild.append(" ALTER (");
        buildColumn(context, newInfo, sqlBuild);
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnComment(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        EColumn newInfo = columnInfo.clone();
        newInfo.setComment(comment);
        List<String> diffChange = Collections.singletonList("COMMENT");
        return columnChange(context, null, schema, table, columnInfo, newInfo, diffChange, eTable);
    }

    @Override
    public List<String> createIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        return Collections.singletonList(new HanaCreateUtils().buildIndex(context, indexInfo, schema, table));
    }

    @Override
    public List<String> dropIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DROP ");
        if (indexInfo.getAttribute().get(HanaAttributeNames.INDEX_TYPE.getCodeKey()).equals(HanaIndexType.FULLTEXT.getCode())) {
            sqlBuild.append("FULLTEXT ");
        }
        sqlBuild.append("INDEX ");
        sqlBuild.append(fmtIndex(context.isUseDelimited(), null, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("RENAME ");
        if (indexInfo.getAttribute().get(HanaAttributeNames.INDEX_TYPE.getCodeKey()).equals(HanaIndexType.FULLTEXT.getCode())) {
            sqlBuild.append("FULLTEXT ");
        }
        sqlBuild.append("INDEX ");
        sqlBuild.append(getDialect().fmtName(buildContext.isUseDelimited(), indexInfo.getName()));
        sqlBuild.append(" TO ");
        sqlBuild.append(getDialect().fmtName(buildContext.isUseDelimited(), newIndexName));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        return new ArrayList<>(this.createIndex(buildContext, null, schema, table, copy));
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        return new ArrayList<>(this.dropIndex(buildContext, null, schema, table, indexInfo));
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" ADD PRIMARY KEY");
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
        sqlBuild.append(" DROP PRIMARY KEY;");
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
        indexScripts.addAll(this.dropPrimaryKey(buildContext, null, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, null, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> createForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return new ArrayList<>();
    }

    @Override
    public List<String> dropForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return new ArrayList<>();
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo, String newForeignKeyName) {
        return new ArrayList<>();
    }
}
