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

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaIndexType;
import com.clougence.adapter.hana.HanaTableType;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
        String value = "NULL";
        if (StringUtils.isNotEmpty(comment)) {
            value = "'" + getDialect().fmtComment(comment) + "'";
        }
        return List.of("COMMENT ON TABLE " + fmtTable(buildContext.isUseDelimited(), null, schema, table) + " IS " + value + ";");
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        List<String> result = new ArrayList<>();
        if (eTable.getComment() != null) {
            result.addAll(tableComment(buildContext, catalog, schema, table, eTable.getComment()));
        }
        String tableType = HanaAttributeNames.TABLE_TYPE.getValue(eTable.getAttribute());
        if (tableType != null) {
            if (HanaTableType.valueOfCode(tableType) == null) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("Unsupported HANA table type: " + tableType));
            }
            result.add(buildAlterTable(buildContext, catalog, schema, table) + " " + tableType + ";");
        }
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
        if (columnInfo.isAutoGenerate() != newInfo.isAutoGenerate()
            || !Objects.equals(HanaAttributeNames.GENERATION_TYPE.getValue(columnInfo.getAttribute()), HanaAttributeNames.GENERATION_TYPE.getValue(newInfo.getAttribute()))
            || !Objects
                .equals(HanaAttributeNames.GENERATION_ALWAYS_AS.getValue(columnInfo.getAttribute()), HanaAttributeNames.GENERATION_ALWAYS_AS.getValue(newInfo.getAttribute()))) {
            throw ThirdPartyApiException.as().with(new IllegalArgumentException("Changing HANA column generation requires native DDL"));
        }

        List<String> result = new ArrayList<>();
        StringBuilder definition = new StringBuilder();
        String oldType = HanaTypeUtils.buildDataType(columnInfo);
        String newType = HanaTypeUtils.buildDataType(newInfo);
        if (!oldType.equals(newType)) {
            definition.append(" ").append(newType);
        }

        if (!Objects.equals(columnInfo.getDefaultValue(), newInfo.getDefaultValue()) || columnInfo.isDefaultValueIsFunc() != newInfo.isDefaultValueIsFunc()) {
            if (newInfo.getDefaultValue() == null) {
                definition.append(" DEFAULT NULL");
            } else {
                definition.append(HanaTypeUtils.buildDefault(newInfo));
            }
        }

        if (!Objects.equals(columnInfo.getNullable(), newInfo.getNullable())) {
            if (Boolean.TRUE.equals(newInfo.getNullable())) {
                definition.append(" NULL");
            } else {
                definition.append(" NOT NULL");
            }
        }

        if (!definition.isEmpty()) {
            if (columnInfo.isAutoGenerate() || StringUtils.isNotBlank(HanaAttributeNames.GENERATION_TYPE.getValue(columnInfo.getAttribute()))) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("Changing HANA generated column properties requires native DDL"));
            }
            result.add(buildAlterTable(context, catalog, schema, table) + " ALTER (" + fmtName(context.isUseDelimited(), newInfo.getName()) + definition + ");");
        }

        if (!Objects.equals(StringUtils.defaultString(columnInfo.getComment()), StringUtils.defaultString(newInfo.getComment()))) {
            result.addAll(columnComment(context, catalog, schema, table, newInfo, newInfo.getComment(), eTable));
        }

        return result;
    }

    @Override
    public List<String> columnComment(TriggerContext context, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        String value = "NULL";
        if (StringUtils.isNotEmpty(comment)) {
            value = "'" + getDialect().fmtComment(comment) + "'";
        }
        return List.of("COMMENT ON COLUMN " + fmtTable(context.isUseDelimited(), null, schema, table) + "." + fmtName(context.isUseDelimited(), columnInfo.getName()) + " IS "
                       + value + ";");
    }

    @Override
    public List<String> createIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        return Collections.singletonList(new HanaCreateUtils().buildIndex(context, indexInfo, schema, table));
    }

    @Override
    public List<String> dropIndex(TriggerContext context, String catalog, String schema, String table, EIndex indexInfo) {
        if (indexInfo.getType() == EIndexType.Unique && indexInfo.getAttribute().containsKey("INDEX_NAME")) {
            return List.of(buildAlterTable(context, catalog, schema, table) + " DROP CONSTRAINT " + fmtName(context.isUseDelimited(), indexInfo.getName()) + ";");
        }
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DROP ");
        if (HanaIndexType.valueOfCode(HanaAttributeNames.INDEX_TYPE.getValue(indexInfo.getAttribute())) == HanaIndexType.FULLTEXT) {
            sqlBuild.append("FULLTEXT ");
        }
        sqlBuild.append("INDEX ");
        sqlBuild.append(fmtTable(context.isUseDelimited(), null, schema, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("RENAME ");
        if (HanaIndexType.valueOfCode(HanaAttributeNames.INDEX_TYPE.getValue(indexInfo.getAttribute())) == HanaIndexType.FULLTEXT) {
            sqlBuild.append("FULLTEXT ");
        }
        sqlBuild.append("INDEX ");
        sqlBuild.append(fmtTable(buildContext.isUseDelimited(), null, schema, indexInfo.getName()));
        sqlBuild.append(" TO ");
        sqlBuild.append(getDialect().fmtName(buildContext.isUseDelimited(), newIndexName)).append(";");
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
        return new ArrayList<>(this.dropIndex(buildContext, null, schema, table, indexInfo));
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        return List.of(buildAlterTable(buildContext, catalog, schema, table) + " ADD " + new HanaCreateUtils().buildPrimaryKey(buildContext, primaryInfo) + ";");
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
        StringBuilder sql = buildAlterTable(buildContext, catalog, schema, table);
        sql.append(" ADD CONSTRAINT ").append(fmtName(buildContext.isUseDelimited(), foreignKeyInfo.getName()));
        sql.append(" FOREIGN KEY (");
        List<String> columns = foreignKeyInfo.getColumnList();
        List<String> referenced = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            String column = columns.get(i);
            sql.append(fmtName(buildContext.isUseDelimited(), column));
            referenced.add(fmtName(buildContext.isUseDelimited(), foreignKeyInfo.getReferenceMapping().get(column)));
        }
        sql.append(") REFERENCES ").append(fmtTable(buildContext.isUseDelimited(), null, foreignKeyInfo.getReferenceSchema(), foreignKeyInfo.getReferenceTable()));
        sql.append(" (").append(String.join(", ", referenced)).append(")");
        if (foreignKeyInfo.getUpdateRule() != null) {
            sql.append(" ON UPDATE ").append(foreignKeyInfo.getUpdateRule().getTypeName());
        }
        if (foreignKeyInfo.getDeleteRule() != null) {
            sql.append(" ON DELETE ").append(foreignKeyInfo.getDeleteRule().getTypeName());
        }
        return List.of(sql.append(";").toString());
    }

    @Override
    public List<String> dropForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return List.of(buildAlterTable(buildContext, catalog, schema, table) + " DROP CONSTRAINT " + fmtName(buildContext.isUseDelimited(), foreignKeyInfo.getName()) + ";");
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo, String newForeignKeyName) {
        EForeignKey renamed = foreignKeyInfo.clone();
        renamed.setName(newForeignKeyName);
        List<String> result = new ArrayList<>(dropForeignKey(buildContext, catalog, schema, table, foreignKeyInfo));
        result.addAll(createForeignKey(buildContext, catalog, schema, table, renamed));
        return result;
    }
}
