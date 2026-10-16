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
import java.util.List;
import java.util.Map;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaIndexType;
import com.clougence.adapter.hana.HanaTableType;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class HanaCreateUtils extends AbstractSqlBuilder {

    @Override
    public Dialect getDialect() { return HanaDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext context, ETable table) {
        StringBuilder sql = new StringBuilder("CREATE ");
        String tableType = HanaAttributeNames.TABLE_TYPE.getValue(table.getAttribute());
        if (StringUtils.isNotBlank(tableType)) {
            if (HanaTableType.valueOfCode(tableType) == null) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_TABLE_TYPE_UNSUPPORTED, tableType);
            }
            sql.append(tableType).append(" ");
        }

        sql.append("TABLE ").append(fmtTable(context.isUseDelimited(), null, table.getSchema(), table.getName())).append(" (\n");
        // Physical order also determines which base columns a generated column can reference.
        for (int i = 0; i < table.getColumnList().size(); i++) {
            if (i > 0) {
                sql.append(",\n");
            }
            EColumn column = table.getColumnList().get(i);
            sql.append("  ").append(fmtName(context.isUseDelimited(), column.getName()));
            sql.append(" ").append(HanaTypeUtils.buildColumnType(column, context));
            if (StringUtils.isNotEmpty(column.getComment())) {
                sql.append(" COMMENT '").append(getDialect().fmtComment(column.getComment())).append("'");
            }
        }

        EPrimaryKey primaryKey = table.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sql.append(",\n  ").append(buildPrimaryKey(context, primaryKey));
        }
        sql.append("\n)");
        if (StringUtils.isNotEmpty(table.getComment())) {
            sql.append(" COMMENT '").append(getDialect().fmtComment(table.getComment())).append("'");
        }
        sql.append(";");

        List<String> result = new ArrayList<>();
        result.add(sql.toString());
        for (EIndex index : table.getIndices()) {
            result.add(buildIndex(context, index, table.getSchema(), table.getName()));
        }
        for (EForeignKey foreignKey : table.getForeignKeys()) {
            result.addAll(HanaEditorProvider.INSTANCE.createForeignKey(context, null, table.getSchema(), table.getName(), foreignKey));
        }
        return result;
    }

    String buildPrimaryKey(TriggerContext context, EPrimaryKey primaryKey) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotBlank(primaryKey.getPrimaryKeyName())) {
            sql.append("CONSTRAINT ").append(fmtName(context.isUseDelimited(), primaryKey.getPrimaryKeyName())).append(" ");
        }
        sql.append("PRIMARY KEY ").append(indexType(primaryKey.getAttribute())).append(" (");
        buildColumns(sql, primaryKey.getColumnList(), context, primaryKey.getAttribute());
        return sql.append(")").toString();
    }

    String buildIndex(TriggerContext context, EIndex index, String schema, String tableName) {
        String type = indexType(index.getAttribute());
        StringBuilder sql = new StringBuilder();
        // Unique constraints must be dropped through ALTER TABLE, not DROP INDEX.
        if (index.getType() == EIndexType.Unique && index.getAttribute().containsKey("INDEX_NAME")) {
            sql.append("ALTER TABLE ").append(fmtTable(context.isUseDelimited(), null, schema, tableName));
            sql.append(" ADD CONSTRAINT ").append(fmtName(context.isUseDelimited(), index.getName()));
            sql.append(" UNIQUE ").append(type).append(" (");
        } else {
            sql.append("CREATE ");
            if (index.getType() == EIndexType.Unique) {
                sql.append("UNIQUE ");
            }
            if (!type.isEmpty()) {
                sql.append(type).append(" ");
            }
            sql.append("INDEX ").append(fmtTable(context.isUseDelimited(), null, schema, index.getName()));
            sql.append(" ON ").append(fmtTable(context.isUseDelimited(), null, schema, tableName)).append(" (");
        }
        buildColumns(sql, index.getColumnList(), context, index.getAttribute());
        return sql.append(");").toString();
    }

    private String indexType(Map<String, String> attributes) {
        String value = HanaAttributeNames.INDEX_TYPE.getValue(attributes);
        if (StringUtils.isBlank(value) || "Normal".equals(value)) {
            return "";
        }

        HanaIndexType type = HanaIndexType.valueOfCode(value);
        if (type == null || type == HanaIndexType.GEOCODE || type == HanaIndexType.FULLTEXT) {
            // Fulltext/spatial definitions have additional options not represented by this editor.
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_INDEX_NATIVE_DDL, value);
        }
        return type.getCode();
    }

    private void buildColumns(StringBuilder sql, List<String> columns, TriggerContext context, Map<String, String> attributes) {
        String orderJson = HanaAttributeNames.ORDER_TYPE.getValue(attributes);
        Map<?, ?> orders = Map.of();
        if (StringUtils.isNotBlank(orderJson)) {
            orders = JsonUtils.toObj(orderJson, Map.class);
        }

        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            String column = columns.get(i);
            sql.append(fmtName(context.isUseDelimited(), column));
            if ("DESC".equalsIgnoreCase(String.valueOf(orders.get(column)))) {
                sql.append(" DESC");
            }
        }
    }
}
