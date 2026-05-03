package com.clougence.clouddm.dsfamily.definition.ui.editor.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorAttributeKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.reload.DataEditorReloadSpi;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.schema.umi.struts.constraint.Unique;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public abstract class DsFamilyDataEditorSpi implements DataEditorSpi, DataEditorReloadSpi {

    protected abstract Dialect getDialect();

    @Override
    public String buildInsert(RdbTable tableMeta, Map<String, String> recordData) {
        Dialect dialect = this.getDialect();

        StringBuilder insertCols = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();
        int index = 0;
        for (RdbColumn colDef : tableMeta.getColumns().values()) {
            String colValue = recordData.get(colDef.getName());

            if (insertIgnore(colDef, colValue)) {
                continue;
            }

            if (index > 0) {
                insertCols.append(", ");
                insertValues.append(", ");
            }

            index++;
            insertCols.append(dialect.fmtName(true, colDef.getName()));

            if (colValue == null) {
                colValue = colDef.getDefaultValue() != null ? "default" : "null";
                insertValues.append(colValue);
            } else {
                insertValues.append(this.templateOfInsert(dialect, colDef, colValue));
            }
        }

        String tabName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("insert into %s (%s) values (%s)", tabName, insertCols, insertValues);
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        StringBuilder whereSql = this.buildWhere(dialect, tableMeta, whereData);

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("update %s set %s where %s", tableName, setSql, whereSql);
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();

        StringBuilder whereSql = this.buildWhere(dialect, tableMeta, whereData);

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("delete from %s where %s", tableName, whereSql);
    }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        Dialect dialect = this.getDialect();

        String tableName = getDialect().fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        StringBuilder selectSql = this.buildSelect(dialect, tableMeta);

        String sql = String.format("select %s from %s", selectSql, tableName);
        if (StringUtils.isNotBlank(condition)) {
            sql = sql + " where " + condition;
        }
        if (StringUtils.isNotBlank(orderBy)) {
            sql = sql + " order by " + orderBy;
        }
        return sql;
    }

    @Override
    public String buildSelectCount(RdbTable tableMeta, String condition) {
        String tableName = getDialect().fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());

        if (StringUtils.isNotBlank(condition)) {
            return String.format("select count(*) from %s where %s", tableName, condition);
        } else {
            return String.format("select count(*) from %s", tableName);
        }
    }

    @Override
    public void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request) {
        // find auto
        List<RdbColumn> autoCols = tableMeta.getColumns().values().stream().filter(item -> {
            String auto = item.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT);
            return !StringUtils.isBlank(auto) && Boolean.parseBoolean(auto);
        }).collect(Collectors.toList());

        if (dmlType == DataEditorSqlType.INSERT && CollectionUtils.isNotEmpty(autoCols)) {
            request.getResultConf().setReturnAutoIncrKey(true);
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        Dialect dialect = getDialect();
        List<Map<String, String>> generatedKeys = result.getGeneratedKeys();
        if (sqlData.getDmlType() == DataEditorSqlType.INSERT && CollectionUtils.isNotEmpty(generatedKeys)) {
            fillAutoCols(tableMeta, sqlData, generatedKeys);
        }

        Map<String, String> whereData = convertWhereData(sqlData.getWhereData(), sqlData.getUpdateData());

        StringBuilder where = this.buildWhere(dialect, tableMeta, whereData);
        // reload sql
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(buildSelect(dialect, tableMeta));
        sb.append(" from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" where ");
        sb.append(where);
        sb.append(" limit 1");
        return Reload.reload(sb.toString());
    }

    protected Map<String, String> convertWhereData(Map<String, String> whereData, Map<String, String> updateData) {
        if (whereData.isEmpty()) {
            return updateData;
        }
        for (String whereKey : whereData.keySet()) {
            if (updateData.containsKey(whereKey)) {
                whereData.put(whereKey, updateData.get(whereKey));
            }
        }
        return whereData;
    }

    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (RdbColumn colDef : tableMeta.getColumns().values()) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(templateOfSelectCol(dialect, colDef));
            i++;
        }
        // if table has no fields，pg
        // base table of the view has been delete ,execute sql and fetch error message
        if (i == 0) {
            sb.append("*");
        }

        return sb;
    }

    protected StringBuilder buildSet(Dialect dialect, RdbTable tableMeta, Map<String, String> data) {
        int i = 0;
        StringBuilder updateSet = new StringBuilder();
        for (RdbColumn colDef : tableMeta.getColumns().values()) {
            if (!data.containsKey(colDef.getName()) || Boolean.parseBoolean(colDef.getAttribute(DataEditorAttributeKeys.UPDATE_READ_ONLY))) {
                continue;
            }

            if (i > 0) {
                updateSet.append(", ");
            }

            String colValue = data.get(colDef.getName());
            updateSet.append(templateOfSet(dialect, colDef, colValue));
            i++;
        }
        return updateSet;
    }

    protected StringBuilder buildWhere(Dialect dialect, RdbTable tableMeta, Map<String, String> data) {
        List<RdbColumn> pkColumn = new ArrayList<>();
        List<RdbColumn> uKColumn = new ArrayList<>();
        for (RdbColumn colMeta : tableMeta.getColumns().values()) {
            List<UmiConstraint> constraints = colMeta.getConstraints();
            if (CollectionUtils.isEmpty(constraints)) {
                continue;
            }
            for (UmiConstraint constraint : constraints) {
                if (constraint instanceof Primary) {
                    pkColumn.add(colMeta);
                } else if (constraint instanceof Unique) {
                    uKColumn.add(colMeta);
                }
            }
        }
        Collection<RdbColumn> whereMetas = CollectionUtils.isNotEmpty(pkColumn) ? pkColumn : CollectionUtils.isNotEmpty(uKColumn) ? uKColumn : tableMeta.getColumns().values();

        int i = 0;
        StringBuilder updateSet = new StringBuilder();
        for (RdbColumn colDef : whereMetas) {
            if (!data.containsKey(colDef.getName()) || !Boolean.parseBoolean(colDef.getAttribute(DataEditorAttributeKeys.DO_WHERE))) {
                continue;
            }

            if (i > 0) {
                updateSet.append(" and ");
            }

            String colValue = data.get(colDef.getName());
            updateSet.append(templateOfWhere(dialect, colDef, colValue));
            i++;
        }
        return updateSet;
    }

    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return dialect.fmtName(true, col.getName());
    }

    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        } else {
            return fmtDataValue(col.getSqlType(), value);
        }
    }

    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        } else {
            return setCol + " = " + fmtDataValue(col.getSqlType(), value);
        }
    }

    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        } else {
            return whereCol + " = " + fmtDataValue(col.getSqlType(), value);
        }
    }

    private static String fmtDataValue(FieldType sqlType, String value) {
        if (sqlType.isString() || sqlType.hasDate() || sqlType.hasTime()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }

    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        return Boolean.parseBoolean(readOnly);
    }

    protected void fillAutoCols(RdbTable tableMeta, SqlData sqlData, List<Map<String, String>> generatedKeys) {
        //autoCols only one
        List<RdbColumn> autoCols = tableMeta.getColumns().values().stream().filter(item -> {
            String auto = item.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT);
            return !StringUtils.isBlank(auto) && Boolean.parseBoolean(auto);
        }).collect(Collectors.toList());
        // fill auto key
        for (RdbColumn autoCol : autoCols) {
            String name = autoCol.getName();
            if (sqlData.getUpdateData().containsKey(name)) {
                String value = sqlData.getUpdateData().get(name);
                String autoValue = null;
                for (String key : generatedKeys.get(0).keySet()) {//auto list only one?
                    autoValue = generatedKeys.get(0).get(key);
                }
                sqlData.getUpdateData().put(name, StringUtils.isBlank(value) ? autoValue : value);
            }
        }
    }
}
