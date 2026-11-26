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
package com.clougence.clouddm.ds.hana.definition.ui.editor.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiStyle;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public class HanaDataEditorSpi extends DsFamilyDataEditorSpi {
    @Override
    protected Dialect getDialect() { return HanaDialect.INSTANCE; }

    @Override
    public boolean requiresServerMetadata() {
        return true;
    }

    @Override
    public boolean isReadOnly(RdbTable tableMeta) {
        return whereColumns(tableMeta).isEmpty();
    }

    @Override
    public void configTableHeader(RdbTable table, List<DataEditorColumn> headers, Map<String, RdbColumn> columns, I18nUtils i18nUtils) {
        List<String> whereColumns = whereColumns(table);
        boolean tableReadOnly = whereColumns.isEmpty();
        for (DataEditorColumn header : headers) {
            RdbColumn column = columns.get(header.getColumn());
            boolean readOnly = tableReadOnly || HanaDataEditorUtils.generated(column) || !HanaDataEditorUtils.editable(column);
            header.setWhereKey(whereColumns.contains(column.getName()));
            header.setSpareWhere(false);
            header.setInsertReadOnly(readOnly);
            header.setUpdateReadOnly(readOnly);
            // Date pickers round fractional seconds; keep the original textual precision.
            if (column.getSqlType().hasDate() || column.getSqlType().hasTime()) {
                header.setUiType(DataEditorUiStyle.INPUT);
            }
        }
    }

    @Override
    public String buildSelect(RdbTable table, String condition, String orderBy, Integer offset, Integer limit) {
        String keys = HanaDataEditorUtils.keyColumns(table).stream().map(name -> getDialect().fmtName(true, name)).collect(Collectors.joining(", "));
        if (!keys.isEmpty()) {
            if (StringUtils.isBlank(orderBy)) {
                orderBy = keys;
            } else {
                orderBy += ", " + keys;
            }
        }
        String sql = super.buildSelect(table, condition, orderBy, offset, limit);
        if (limit != null) {
            if (limit < 0 || offset != null && offset < 0) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_PAGE_RANGE_INVALID);
            }
            sql += " LIMIT " + limit;
            if (offset != null) {
                sql += " OFFSET " + offset;
            }
        }
        return sql;
    }

    @Override
    public String buildInsert(RdbTable table, Map<String, String> data) {
        requireEditableTable(table);
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");
        for (String name : data.keySet()) {
            RdbColumn column = table.getColumns().get(name);
            if (column == null) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_COLUMN_UNKNOWN, name);
            }
            if (HanaDataEditorUtils.generated(column) && data.get(name) == null) {
                continue;
            }
            if (HanaDataEditorUtils.generated(column) || !HanaDataEditorUtils.editable(column)) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_COLUMN_READ_ONLY, name);
            }
            columns.add(getDialect().fmtName(true, name));
            values.add(HanaDataEditorUtils.literal(column, data.get(name)));
        }
        String tableName = getDialect().fmtTableName(true, table.getCatalog(), table.getSchema(), table.getName());
        if (columns.length() == 0) {
            for (RdbColumn column : table.getColumns().values()) {
                if (Boolean.parseBoolean(column.getAttribute(HanaAttributeNames.AUTO_INCREMENT))) {
                    return "INSERT INTO " + tableName + " (" + getDialect().fmtName(true, column.getName()) + ") OVERRIDING USER VALUE SELECT NULL FROM SYS.DUMMY";
                }
            }
            RdbColumn column = table.getColumns()
                .values()
                .stream()
                .filter(c -> !HanaDataEditorUtils.generated(c))
                .findFirst()
                .orElseThrow(() -> ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_NO_INSERT_COLUMN));
            columns.add(getDialect().fmtName(true, column.getName()));
            String defaultValue = column.getDefaultValue();
            if (defaultValue == null) {
                defaultValue = "NULL";
            }
            values.add(defaultValue);
        }
        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
    }

    @Override
    public String buildUpdate(RdbTable table, Map<String, String> whereData, Map<String, String> setData) {
        if (!HanaDataEditorUtils.keyColumns(table).isEmpty()) {
            return super.buildUpdate(table, whereData, setData);
        }

        Dialect dialect = getDialect();
        StringBuilder where = buildWhere(dialect, table, whereData);
        StringBuilder set = buildSet(dialect, table, setData);
        String tableName = dialect.fmtTableName(true, table.getCatalog(), table.getSchema(), table.getName());
        return "UPDATE TOP 1 " + tableName + " SET " + set + " WHERE " + where;
    }

    @Override
    public String buildDelete(RdbTable table, Map<String, String> whereData) {
        if (!HanaDataEditorUtils.keyColumns(table).isEmpty()) {
            return super.buildDelete(table, whereData);
        }

        Dialect dialect = getDialect();
        StringBuilder where = buildWhere(dialect, table, whereData);
        String tableName = dialect.fmtTableName(true, table.getCatalog(), table.getSchema(), table.getName());
        return "DELETE FROM " + tableName + " WHERE " + where + " AND (SELECT COUNT(*) FROM " + tableName + " WHERE " + where + ") = 1";
    }

    @Override
    protected StringBuilder buildSet(Dialect dialect, RdbTable table, Map<String, String> data) {
        StringJoiner assignments = new StringJoiner(", ");
        for (String name : data.keySet()) {
            RdbColumn column = table.getColumns().get(name);
            if (column == null || HanaDataEditorUtils.generated(column) || !HanaDataEditorUtils.editable(column)) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_COLUMN_NOT_EDITABLE, name);
            }
            assignments.add(dialect.fmtName(true, name) + " = " + HanaDataEditorUtils.literal(column, data.get(name)));
        }
        if (assignments.length() == 0) {
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_NO_UPDATE_COLUMN);
        }
        return new StringBuilder(assignments.toString());
    }

    @Override
    protected StringBuilder buildWhere(Dialect dialect, RdbTable table, Map<String, String> data) {
        List<String> columns = whereColumns(table);
        if (columns.isEmpty()) {
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_ROW_KEY_REQUIRED);
        }
        boolean keyless = HanaDataEditorUtils.keyColumns(table).isEmpty();

        StringJoiner predicates = new StringJoiner(" AND ");
        for (String column : columns) {
            String value = data.get(column);
            if (!data.containsKey(column) || !keyless && value == null) {
                throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_ROW_KEY_MISSING, column);
            }

            String name = dialect.fmtName(true, column);
            if (value == null) {
                predicates.add(name + " IS NULL");
            } else {
                predicates.add(name + " = " + HanaDataEditorUtils.literal(table.getColumns().get(column), value));
            }
        }
        return new StringBuilder(predicates.toString());
    }

    private List<String> whereColumns(RdbTable table) {
        if (table.getUmiType() != UmiTypes.Table) {
            return List.of();
        }

        List<String> keys = HanaDataEditorUtils.keyColumns(table);
        if (keys.isEmpty()) {
            return HanaDataEditorUtils.fallbackWhereColumns(table);
        }
        return keys;
    }

    private void requireEditableTable(RdbTable table) {
        if (isReadOnly(table)) {
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_ROW_KEY_REQUIRED);
        }
    }

    @Override
    public void beforeExecute(RdbTable table, DataEditorSqlType type, QueryRequest request) {
        if (type == DataEditorSqlType.INSERT
            && table.getColumns().values().stream().anyMatch(column -> Boolean.parseBoolean(column.getAttribute(HanaAttributeNames.AUTO_INCREMENT)))) {
            request.getResultConf().setReturnAutoIncrKey(true);
        }
    }

    @Override
    public void validateUpdateCount(long updateCount) {
        if (updateCount != 1) {
            throw ThirdPartyApiException.as().with(HanaDsI18nKeys.HANA_AFFECTED_ROWS_UNEXPECTED, updateCount);
        }
    }

    @Override
    public Reload afterExecute(RdbTable table, QueryRequest request, EditorResultSet result, SqlData data) {
        List<String> keys = HanaDataEditorUtils.keyColumns(table);
        if (keys.isEmpty()) {
            return Reload.success(result);
        }

        Map<String, String> row = new HashMap<>(data.getWhereData());
        row.putAll(data.getUpdateData());
        if (data.getDmlType() == DataEditorSqlType.INSERT && CollectionUtils.isNotEmpty(result.getGeneratedKeys())) {
            Map<String, String> generated = result.getGeneratedKeys().get(0);
            if (generated.size() == 1) {
                for (RdbColumn column : table.getColumns().values()) {
                    if (Boolean.parseBoolean(column.getAttribute(HanaAttributeNames.AUTO_INCREMENT))) {
                        row.put(column.getName(), generated.values().iterator().next());
                    }
                }
            }
        }
        // Missing generated/default keys affect reloading, not the already committed write.
        if (keys.stream().anyMatch(key -> row.get(key) == null)) {
            return Reload.success(result);
        }
        String sql = "SELECT " + buildSelect(getDialect(), table) + " FROM " + getDialect().fmtTableName(true, table.getCatalog(), table.getSchema(), table.getName()) + " WHERE "
                     + buildWhere(getDialect(), table, row);
        return Reload.reload(sql);
    }
}
