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
package com.clougence.clouddm.ds.oceanbase.definition.ob4my.ui.editor.table;

import static com.clougence.adapter.ob.obformysql.ObForMySQLAttributeNames.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.ob.obformysql.ObForMySQLIndexType;
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.clouddm.ds.oceanbase.dialect.ob4my.ObForMySQLDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/8/10 11:39
*/
@Slf4j
public class ObCreateUtils extends AbstractSqlBuilder {

    @Override
    public Dialect getDialect() { return ObForMySQLDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> tableAttr = eTable.getAttribute();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE ");
        String temporary = TEMPORARY.getValue(tableAttr);
        if (StringUtils.isNotBlank(temporary) && temporary.equalsIgnoreCase("true")) {
            sqlBuild.append("TEMPORARY ");
        }
        sqlBuild.append("TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i).clone();
            buildColumn(sqlBuild, buildContext, eColumn, eTable.getPrimaryKey());
        }
        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, primaryKey.clone());
        }
        // idx
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                sqlBuild.append(",\n");
                buildIndex(sqlBuild, buildContext, index.clone(), eTable);
            }
        }
        //
        // eTable.getForeignKeys();
        // private List<EForeignKey> foreignKeys = new ArrayList<>();
        //
        sqlBuild.append("\n )");
        buildTableCreateOption(sqlBuild, eTable, tableAttr);
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    private void buildTableCreateOption(StringBuilder sqlBuild, ETable eTable, Map<String, String> tableAttr) {

        if (StringUtils.isNotBlank(eTable.getComment())) {
            String comment = eTable.getComment();
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("COMMENT = '").append(comment).append("'");
        }

        if (StringUtils.isNotBlank(CHARACTER_SET.getValue(tableAttr))) {
            String charSet = CHARACTER_SET.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("DEFAULT CHARACTER SET = '").append(charSet).append("'");
            if (StringUtils.isNotBlank(COLLATION.getValue(tableAttr))) {
                String coll = COLLATION.getValue(tableAttr);
                sqlBuild.append(" ");
                sqlBuild.append("DEFAULT COLLATE = '").append(coll).append("'");
            }
        }

        if (StringUtils.isNotBlank(AUTO_INCREMENT_STAR.getValue(tableAttr))) {
            String autoIncrementStar = AUTO_INCREMENT_STAR.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("AUTO_INCREMENT = ").append(autoIncrementStar);
        }

        if (StringUtils.isNotBlank(ROW_FORMAT.getValue(tableAttr))) {
            String rowFormat = ROW_FORMAT.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("ROW_FORMAT = ").append(rowFormat);
        }

        if (StringUtils.isNotBlank(COMPRESSION.getValue(tableAttr))) {
            String compression = COMPRESSION.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("COMPRESSION = '").append(compression).append("'");
        }
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn, EPrimaryKey ePrimaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        ObForMySQLTypes sqlTypes = ObForMySQLTypes.valueOfCode(eColumn.getDbType());
        Map<String, String> columnAttr = eColumn.getAttribute();

        sqlBuild.append("  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        String columnType = ObTypeUtils.buildColumnType(eColumn, ePrimaryKey, buildContext);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);

        if (sqlTypes.isString() && StringUtils.isNotBlank(DEFAULT_CHARACTER_SET_NAME.getValue(columnAttr))) {
            String collation = DEFAULT_CHARACTER_SET_NAME.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("CHARACTER SET ").append(collation);
        }

        if (sqlTypes.isString() && StringUtils.isNotBlank(DEFAULT_COLLATION_NAME.getValue(columnAttr))) {
            String collation = DEFAULT_COLLATION_NAME.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("COLLATE ").append(collation);
        }

        if (sqlTypes.isDataOrTime() && StringUtils.isNotBlank(CURRENT_UPDATE_TYPE.getValue(columnAttr))) {
            String currentUpdateType = CURRENT_UPDATE_TYPE.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("ON UPDATE ").append(currentUpdateType);
        }

        if (sqlTypes.isNumber() && eColumn.isAutoGenerate()) {
            sqlBuild.append(" ");
            sqlBuild.append("AUTO_INCREMENT");
        }

        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> pkAttr = primaryKey.getAttribute();

        String partJson = SUB_PART.getValue(pkAttr);
        Map<String, String> subPartMap = new HashMap<>();
        if (StringUtils.isNotBlank(partJson)) {
            subPartMap = JsonUtils.toMap(partJson);
        }

        sqlBuild.append("  PRIMARY KEY");
        //        if (StringUtils.isNotBlank(INDEX_TYPE.getValue(pkAttr))) {
        //            String indexType = INDEX_TYPE.getValue(pkAttr);
        //            sqlBuild.append(" ");
        //            sqlBuild.append("PARTITION BY ").append(" ").append(indexType.toUpperCase()).append(" ");
        //        }
        sqlBuild.append("(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));

            if (CollectionUtils.isNotEmpty(subPartMap) && subPartMap.containsKey(column)) {
                String subPart = subPartMap.get(column);
                if (StringUtils.isNotBlank(subPart)) {
                    sqlBuild.append("(").append(Integer.valueOf(subPart)).append(")");
                }
            }
        }
        sqlBuild.append(")");
    }

    @SneakyThrows
    private void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, EIndex eIndex, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> indexAttr = eIndex.getAttribute();

        String subPartJson = SUB_PART.getValue(indexAttr);
        Map<String, String> subPart = new HashMap<>();
        if (StringUtils.isNotEmpty(subPartJson)) {
            subPart = JsonUtils.toMap(subPartJson);
        }

        String typeValue = INDEX_TYPE.getValue(indexAttr);
        String wayValue = INDEX_WAY.getValue(indexAttr);
        String indexName = eIndex.getName();
        String append = "";

        if (StringUtils.isBlank(wayValue)) {
            if (eIndex.getType() != null) {
                if (eIndex.getType().getTypeName().equals(ObForMySQLIndexType.Normal.name())) {
                    wayValue = ObForMySQLIndexType.Normal.name();
                } else if (eIndex.getType().getTypeName().equals(ObForMySQLIndexType.Unique.name())) {
                    wayValue = ObForMySQLIndexType.Unique.name();
                } else if (eIndex.getType().getTypeName().equals(ObForMySQLIndexType.FullText.name())) {
                    wayValue = ObForMySQLIndexType.FullText.name();
                } else {
                    throw new UnsupportedOperationException("indexType " + wayValue + " not support");
                }
            } else {
                throw new UnsupportedOperationException("index way must have a value");
            }
        }

        if (wayValue.equalsIgnoreCase(ObForMySQLIndexType.Normal.name())) {
            sqlBuild.append(" INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            }
            // if (StringUtils.isNotBlank(typeValue)) {
            //     sqlBuild.append(" ");
            //     sqlBuild.append("USING ").append(typeValue.toUpperCase());
            // }
        } else if (wayValue.equalsIgnoreCase(ObForMySQLIndexType.Unique.name())) {
            sqlBuild.append(" UNIQUE INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            }
            // if (StringUtils.isNotBlank(typeValue)) {
            //     sqlBuild.append(" ");
            //     sqlBuild.append("USING ").append(typeValue.toUpperCase());
            // }
        }

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append(" ");
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
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
        }
        sqlBuild.append(")");

        //if (StringUtils.isNotBlank(KEY_BLOCK_SIZE.getValue(indexAttr))) {
        //    String value = KEY_BLOCK_SIZE.getValue(indexAttr);
        //    sqlBuild.append(" ");
        //    sqlBuild.append("BLOCK_SIZE = ").append(Integer.valueOf(value));
        //}

        if (StringUtils.isNotBlank(eIndex.getComment())) {
            sqlBuild.append(" ");
            sqlBuild.append("COMMENT '").append(eIndex.getComment()).append("'");
        }

        sqlBuild.append(append);
    }
}
