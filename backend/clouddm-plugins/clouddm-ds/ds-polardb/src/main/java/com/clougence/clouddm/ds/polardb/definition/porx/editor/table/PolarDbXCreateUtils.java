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
package com.clougence.clouddm.ds.polardb.definition.porx.editor.table;

import static com.clougence.adapter.polar.porx.PolarDbXAttributeNames.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.polardb.dialect.porx.PolarDbXDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.builder.CharsetsAndCollations;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class PolarDbXCreateUtils extends AbstractSqlBuilder {

    public Dialect getDialect() { return PolarDbXDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("create table if not exists ");
        sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i).clone();
            buildColumn(sqlBuild, buildContext, eColumn);
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

        sqlBuild.append(")\n");

        String shardDefJsons = SHARD_DEF.getValue(eTable.getAttribute());
        if (StringUtils.isNotBlank(shardDefJsons)) {
            printSharding(sqlBuild, shardDefJsons);
        }

        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            String comment = getDialect().fmtComment(eTable.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }

        CharsetsAndCollations charCollMap = buildContext.getTarDsInfo().getCharsetAndCollation();
        String characterSet = CHARACTER_SET.getValue(eTable.getAttribute());
        String collation = COLLATION.getValue(eTable.getAttribute());

        if (charCollMap.containsCollation(collation)) {
            sqlBuild.append(" default charset = ").append(charCollMap.getData().get(collation));
            sqlBuild.append(" collate = ").append(collation);
        } else if (charCollMap.containsCharset(characterSet)) {
            sqlBuild.append(" default charset = ").append(characterSet);
        }

        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    private void printSharding(StringBuilder sqlBuild, String shardDefJson) {
        try {
            PorXShardDef def = new ObjectMapper().readValue(shardDefJson, new TypeReference<PorXShardDef>() {
            });
            if (def.getDbShardingPolicy() != null) {
                sqlBuild.append("dbpartition by ").append(def.getDbShardingPolicy()).append("(");
                if (CollectionUtils.isNotEmpty(def.getDbShardingCols())) {
                    sqlBuild.append("`");
                    String dbCols = StringUtils.join(def.getDbShardingCols().toArray(new String[0]), "`,`");
                    sqlBuild.append(dbCols).append("`");
                }
                sqlBuild.append(") ");
            }

            if (def.getTbShardingPolicy() != null) {
                sqlBuild.append("tbpartition by ").append(def.getTbShardingPolicy()).append("(");
                if (CollectionUtils.isNotEmpty(def.getTbShardingCols())) {
                    sqlBuild.append("`");
                    String tbCols = StringUtils.join(def.getTbShardingCols().toArray(new String[0]), "`,`");
                    sqlBuild.append(tbCols).append("`");
                }

                sqlBuild.append(") ");
            }

            if (def.getTbShardingNum() != null && def.getTbShardingNum() > 0) {
                sqlBuild.append("tbpartitions ").append(def.getTbShardingNum());
            }
        } catch (Exception e) {
            log.error("read from PolarDbXShardDef json failed.but ignore.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("  ");
        sqlBuild.append(fmtName(useDelimited, eColumn.getName()));
        String columnType = PorXTypeUtils.buildColumnType(eColumn, buildContext);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("primary key(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    @SneakyThrows
    private void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, EIndex eIndex, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        String subPartJson = SUB_PART.getValue(eIndex.getAttribute());
        Map<String, Integer> subPart = StringUtils.isBlank(subPartJson) ? null : new ObjectMapper().readValue(subPartJson, new TypeReference<Map<String, Integer>>() {
        });

        String append = "";

        if (eIndex.getType() == EIndexType.Unique) {
            sqlBuild.append(" unique key ");
            String indexName = safeIdxName("uk", eTable.getName(), eIndex.getName(), false);
            sqlBuild.append(fmtIndex(useDelimited, null, indexName));
        } else {
            sqlBuild.append(" key ");
            String indexName = safeIdxName("idx", eTable.getName(), eIndex.getName(), false);
            sqlBuild.append(fmtIndex(useDelimited, null, indexName));
        }

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
            if (subPart != null && subPart.containsKey(column)) {
                Integer value = subPart.get(column);
                if (value != null) {
                    sqlBuild.append("(").append(value).append(")");
                }
            }
        }

        sqlBuild.append(")");
        sqlBuild.append(append);
    }
}
