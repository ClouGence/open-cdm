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
package com.clougence.clouddm.ds.cloudberry.definition.ui.editor.table;

import static com.clougence.adapter.cloudberry.CloudberryAttributeNames.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgCreateUtils;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class CbCreateUtils extends PgCreateUtils {

    private static void buildTableWithOption(StringBuilder sqlBuild, Map<String, String> attrMap) {
        String fill = attrMap.get(FILL_FACTOR.getCodeKey());
        String append = attrMap.get(APPEND_OPTIMIZED.getCodeKey());
        String block = attrMap.get(BLOCK_SIZE.getCodeKey());
        String orientation = attrMap.get(ORIENTATION.getCodeKey());
        String check = attrMap.get(CHECK_SUM.getCodeKey());
        String type = attrMap.get(COMPRESS_TYPE.getCodeKey());
        String level = attrMap.get(COMPRESS_LEVEL.getCodeKey());

        List<String> option = new ArrayList<>();

        if (StringUtils.isNotBlank(fill)) {
            option.add("FILLFACTOR=" + fill);
        }
        if (Boolean.parseBoolean(append)) {
            option.add("APPENDOPTIMIZED=true");
            if (StringUtils.isNotBlank(block)) {
                option.add("BLOCKSIZE=" + block);
            }
            if (StringUtils.isNotBlank(orientation)) {
                option.add("ORIENTATION=" + orientation);
            }
            if (StringUtils.isNotBlank(check)) {
                option.add("CHECKSUM=" + check);
            }
            if (StringUtils.isNotBlank(type)) {
                option.add("COMPRESSTYPE=" + type);
            }
            if (StringUtils.isNotBlank(level)) {
                option.add("COMPRESSLEVEL=" + level);
            }
        }
        if (!option.isEmpty()) {
            sqlBuild.append(" WITH (");
            sqlBuild.append(String.join(",", option));
            sqlBuild.append(")\n");
        }
    }

    @Override
    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> ddlScripts = new ArrayList<>();
        List<String> seqNames = new ArrayList<>();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE ");
        String tableType = eTable.getAttribute().get(TABLE_TYPE.getCodeKey());
        if ("t".equals(tableType)) {
            sqlBuild.append("TEMPORARY TABLE ");
        } else if ("u".equals(tableType)) {
            sqlBuild.append("UNLOGGED TABLE ");
        } else {
            sqlBuild.append("TABLE ");
        }
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, "t".equals(tableType) ? null : eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n ");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n ");
            }
            EColumn eColumn = columnList.get(i);
            buildColumn(sqlBuild, buildContext, eColumn, eTable.getName(), seqNames, eTable.getPrimaryKey());
        }
        //create sequence
        if (seqNames.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String seqName : seqNames) {
                builder.append("CREATE SEQUENCE IF NOT EXISTS ").append(seqName).append(";");
                ddlScripts.add(0, builder.toString());
                builder.setLength(0);
            }
        }
        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n ");
            buildPrimaryKey(sqlBuild, buildContext, eTable, primaryKey);
        }
        sqlBuild.append(")");
        //createOption
        buildTableCreateOption(sqlBuild, eTable, buildContext);
        ddlScripts.add(sqlBuild.toString());

        //table comment
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            StringBuilder commentSql = new StringBuilder();
            this.buildTableComment(commentSql, buildContext, eTable);
            ddlScripts.add(commentSql.toString());
        }

        // columns comment
        StringBuilder columnCommentSql = new StringBuilder();
        for (EColumn eColumn : columnList) {
            if (StringUtils.isBlank(eColumn.getComment())) {
                continue;
            }
            this.buildColumnComment(columnCommentSql, buildContext, eTable, eColumn);
            ddlScripts.add(columnCommentSql.toString());
            columnCommentSql.setLength(0);
        }

        //build column->type mapping
        Map<String, PostgresTypes> columnTypeMap = new HashMap<>();
        columnList.forEach(column -> {
            String dbType = column.getDbType();
            PostgresTypes postgresTypes = PostgresTypes.valueOfCode(dbType);
            columnTypeMap.put(column.getName(), postgresTypes);
        });

        // idx and idx comment
        List<EIndex> idxIndices = eTable.getIndices();
        StringBuilder indexBuild = new StringBuilder();
        StringBuilder commentBuild = new StringBuilder();
        for (EIndex index : idxIndices) {
            buildIndex(indexBuild, buildContext, schema, table, index, columnTypeMap);
            buildIndexComment(commentBuild, schema, index);
            ddlScripts.add(indexBuild.toString());
            ddlScripts.add(commentBuild.toString());
            indexBuild.setLength(0);
            commentBuild.setLength(0);
        }
        return ddlScripts;
    }

    protected String buildTableCreateOption(StringBuilder sqlBuild, ETable eTable, TriggerContext buildContext) {
        Map<String, String> attrMap = eTable.getAttribute();
        String fatherArr = attrMap.get(INHERITED_FROM.getCodeKey());
        if (StringUtils.isNotBlank(fatherArr)) {
            if (fatherArr.startsWith("[") && fatherArr.endsWith("]")) {
                fatherArr = fatherArr.replace("[", "");
                fatherArr = fatherArr.replace("]", "");
                if (StringUtils.isNotBlank(fatherArr)) {
                    String[] split = fatherArr.split(",");
                    sqlBuild.append(" INHERITS (");
                    for (int i = 0; i < split.length; i++) {
                        if (i != 0) {
                            sqlBuild.append(",");
                        }
                        sqlBuild.append(split[i]);
                    }
                    sqlBuild.append(") ");
                }
            }
        }
        //build with
        buildTableWithOption(sqlBuild, attrMap);

        String tablespace = attrMap.get(TABLESPACE.getCodeKey());
        if (StringUtils.isNotBlank(tablespace)) {
            sqlBuild.append(" TABLESPACE ");
            sqlBuild.append(tablespace);
        }

        String distType = attrMap.get(DISTRIBUTED_TYPE.getCodeKey());
        if (StringUtils.isNotBlank(distType)) {
            switch (distType) {
                case "p":
                    sqlBuild.append(" DISTRIBUTED BY (");
                    String distributedColumnJson = attrMap.get(DISTRIBUTED_COLUMN.getCodeKey());
                    List<String> distributedColumns = JsonUtils.toListUseType(distributedColumnJson, String.class);
                    for (int i = 0; i < distributedColumns.size(); i++) {
                        if (i > 0) {
                            sqlBuild.append(",");
                        }
                        sqlBuild.append(getDialect().fmtName(buildContext.isUseDelimited(), distributedColumns.get(i)));
                    }
                    sqlBuild.append(")");
                    break;
                case "r":
                    sqlBuild.append(" DISTRIBUTED RANDOMLY");
                    break;
                case "p_no_column":
                    sqlBuild.append(" DISTRIBUTED REPLICATED");
                    break;
                default:
                    break;
            }
        }
        sqlBuild.append(";");
        return sqlBuild.toString();
    }
}
