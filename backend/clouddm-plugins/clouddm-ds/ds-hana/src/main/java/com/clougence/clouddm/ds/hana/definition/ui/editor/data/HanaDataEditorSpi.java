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

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorAttributeKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nUtils;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return HanaDialect.INSTANCE; }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn idColumn = new DataEditorColumn();
            idColumn.setColumn("$rowid$");
            idColumn.setInsertReadOnly(true);
            idColumn.setUpdateReadOnly(true);
            idColumn.setWhereKey(true);
            idColumn.setHide(true);
            headerCols.add(0, idColumn);
        }
    }

    @Override
    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        String auto = colDef.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT);
        return Boolean.parseBoolean(readOnly) || Boolean.parseBoolean(auto);
    }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        String baseSql = super.buildSelect(tableMeta, condition, orderBy, offset, limit);
        if (offset == null) {
            return baseSql + String.format(" limit %s", limit);
        } else {
            return baseSql + String.format(" limit %s offset %s", limit, offset);
        }
    }

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "\"$rowid$\", ");
        }
        return sb;
    }

    @Override
    public String buildInsert(RdbTable tableMeta, Map<String, String> recordData) {
        return super.buildInsert(tableMeta, recordData);
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("delete from %s where \"$rowid$\" = %s", tableName, whereData.get("$rowid$"));
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("update %s set %s where \"$rowid$\" = %s", tableName, setSql, whereData.get("$rowid$"));
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return super.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return super.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return super.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return super.templateOfWhere(dialect, col, value);
    }
}
