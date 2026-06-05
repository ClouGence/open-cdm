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
package com.clougence.clouddm.ds.tidb.definition.ui.editor.table;

import static com.clougence.adapter.tidb.TiDBAttributeNames.*;

import java.util.*;

import com.clougence.adapter.mysql.MySQLIndexType;
import com.clougence.adapter.tidb.TiDBAttributeNames;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.clouddm.ds.tidb.dialect.TiDBDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/8/18 10:07
*/
@Slf4j
public class TiEditorProvider extends MyEditorProvider {

    public static final SqlBuilder INSTANCE = new TiEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.TiDB; }

    public Dialect getDialect() { return TiDBDialect.INSTANCE; }

    @Override
    public List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new TiCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(columnInfo.getDbType());
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        List<String> result = new ArrayList<>();

        sqlBuild.append(" ADD " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + TiTypeUtils.buildColumnType(mainVersion, columnInfo).toUpperCase());
        Map<String, String> columnAtr = columnInfo.getAttribute();

        //if (Boolean.TRUE.equals(columnInfo.getNullable())) {
        //    sqlBuild.append(" NULL ");
        //} else {
        //    sqlBuild.append(" NOT  NULL ");
        //}

        if (sqlTypes.isString() && columnAtr.containsKey(TiDBAttributeNames.DEFAULT_CHARACTER_SET_NAME.getCodeKey())) {
            String value = TiDBAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" CHARACTER SET ").append(value).append(" ");
            }
        }

        if (sqlTypes.isString() && columnAtr.containsKey(TiDBAttributeNames.DEFAULT_COLLATION_NAME.getCodeKey())) {
            String value = TiDBAttributeNames.DEFAULT_COLLATION_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" COLLATE ").append(value).append(" ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(TiDBAttributeNames.UNSIGNED.getCodeKey())) {
            if (TiDBAttributeNames.UNSIGNED.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" UNSIGNED ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(TiDBAttributeNames.ZEROFILL.getCodeKey())) {
            if (TiDBAttributeNames.ZEROFILL.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" ZEROFILL ");
            }
        }

        if (columnInfo.getDefaultValue() != null) {
            String defaultValue = TiTypeUtils.buildDefault(sqlTypes, mainVersion, columnInfo, buildContext).toUpperCase();
            sqlBuild.append(" ").append(defaultValue).append(" ");
        }

        if (columnInfo.isAutoGenerate()) {
            sqlBuild.append(" AUTO_INCREMENT ");
        }

        if (sqlTypes.isDataOrTime() && columnAtr.containsKey(TiDBAttributeNames.CURRENT_UPDATE_TYPE.getCodeKey())) {
            String value = TiDBAttributeNames.CURRENT_UPDATE_TYPE.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" ON UPDATE ").append(value).append(" ");
            }
        }

        if (columnInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(columnInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        result.add(sqlBuild.toString());

        return result;
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" CHANGE COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + getDialect().fmtName(useDelimited, newColumnName));
        sqlBuild.append(" " + TiTypeUtils.buildColumnType(columnInfo, buildContext).toUpperCase());
        if (columnInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(columnInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(newInfo.getDbType());
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        List<String> result = new ArrayList<>();

        sqlBuild.append(" MODIFY COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + TiTypeUtils.buildColumnType(mainVersion, newInfo).toUpperCase());
        Map<String, String> columnAtr = newInfo.getAttribute();

        if (sqlTypes.isString() && columnAtr.containsKey(TiDBAttributeNames.DEFAULT_CHARACTER_SET_NAME.getCodeKey())) {
            String value = TiDBAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" CHARACTER SET ").append(value).append(" ");
            } else if (StringUtils.isNotBlank(TiDBAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(newInfo.getAttribute()))) {
                sqlBuild.append(" CHARACTER SET ").append(" DEFAULT ").append(" ");
            }
        }

        if (sqlTypes.isString() && columnAtr.containsKey(TiDBAttributeNames.DEFAULT_COLLATION_NAME.getCodeKey())) {
            String value = TiDBAttributeNames.DEFAULT_COLLATION_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" COLLATE ").append(value).append(" ");
            } else if (StringUtils.isNotBlank(TiDBAttributeNames.DEFAULT_COLLATION_NAME.getValue(newInfo.getAttribute()))) {
                sqlBuild.append(" COLLATE ").append(" DEFAULT ").append(" ");
            }
        }

        // if (Boolean.TRUE.equals(newInfo.getNullable())) {
        //     sqlBuild.append(" NULL ");
        // } else {
        //     sqlBuild.append(" NOT NULL ");
        // }

        if (sqlTypes.isNumber() && columnAtr.containsKey(TiDBAttributeNames.UNSIGNED.getCodeKey())) {
            if (TiDBAttributeNames.UNSIGNED.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" UNSIGNED ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(TiDBAttributeNames.ZEROFILL.getCodeKey())) {
            if (TiDBAttributeNames.ZEROFILL.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" ZEROFILL ");
            }
        }

        String defaultValue = TiTypeUtils.buildDefault(sqlTypes, mainVersion, newInfo, buildContext);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuild.append(" ");
            sqlBuild.append(defaultValue);
            sqlBuild.append(" ");
        }

        if (newInfo.isAutoGenerate()) {
            sqlBuild.append(" AUTO_INCREMENT ");
        }

        if (sqlTypes.isDataOrTime() && columnAtr.containsKey(TiDBAttributeNames.CURRENT_UPDATE_TYPE.getCodeKey())) {
            String value = TiDBAttributeNames.CURRENT_UPDATE_TYPE.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" ON UPDATE ").append(value).append(" ");
            }
        }

        if (newInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(newInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        result.add(sqlBuild.toString());

        return result;
    }

    @SneakyThrows
    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> indexAttr = indexInfo.getAttribute();
        StringBuilder sqlBuild = new StringBuilder();

        String subPartJson = SUB_PART.getValue(indexAttr);
        String subOrderJson = SUB_ORDER.getValue(indexAttr);
        Map<String, String> subPart = StringUtils.isBlank(subPartJson) ? new HashMap<>() : JsonUtils.toMap(subPartJson);
        Map<String, String> subOrder = StringUtils.isBlank(subOrderJson) ? new HashMap<>() : JsonUtils.toMap(subPartJson);

        String wayValue = INDEX_WAY.getValue(indexAttr);
        String indexName = indexInfo.getName();
        String typeValue = INDEX_TYPE.getValue(indexInfo.getAttribute());
        String append = "";

        if (StringUtils.isBlank(wayValue)) {
            throw new UnsupportedOperationException("index way must have a value");
        }

        sqlBuild.append("CREATE ");
        if (wayValue.equalsIgnoreCase(MySQLIndexType.Unique.name())) {
            sqlBuild.append("UNIQUE");
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.FullText.name())) {
            sqlBuild.append("FULLTEXT");
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.SPATIAL.name())) {
            sqlBuild.append("SPATIAL");
        }
        sqlBuild.append(" INDEX ");
        sqlBuild.append(getDialect().fmtName(useDelimited, indexName));

        sqlBuild.append(" ON ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
            if (CollectionUtils.isNotEmpty(subPart) && subPart.containsKey(column)) {
                String value = subPart.get(column);
                if (StringUtils.isNotBlank(value)) {
                    sqlBuild.append("(").append(Integer.valueOf(value)).append(")");
                }
            }

            if (CollectionUtils.isNotEmpty(subOrder) && subPart.containsKey(column)) {
                String value = subOrder.get(column);
                if (StringUtils.isNotBlank(value)) {
                    sqlBuild.append(" ");
                    if (value.equalsIgnoreCase("ASC")) {
                        sqlBuild.append("ASC");
                    } else if (value.equalsIgnoreCase("DESC")) {
                        sqlBuild.append("DESC");
                    }
                }
            }
        }
        sqlBuild.append(")").append(append);

        if (StringUtils.isNotBlank(KEY_BLOCK_SIZE.getValue(indexAttr))) {
            String value = KEY_BLOCK_SIZE.getValue(indexAttr);
            sqlBuild.append(" ");
            sqlBuild.append("KEY_BLOCK_SIZE = ").append(Integer.valueOf(value));
        }

        if (indexInfo.getComment() != null) {
            sqlBuild.append(" ");
            sqlBuild.append("COMMENT '").append(indexInfo.getComment()).append("'");
        }
        return Collections.singletonList(sqlBuild.toString());
    }
}
