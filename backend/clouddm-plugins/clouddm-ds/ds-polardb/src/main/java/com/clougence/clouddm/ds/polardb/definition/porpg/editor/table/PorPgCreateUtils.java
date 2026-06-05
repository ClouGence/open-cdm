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
package com.clougence.clouddm.ds.polardb.definition.porpg.editor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.adapter.polar.porpg.PolarDBPgAttributeNames;
import com.clougence.clouddm.ds.polardb.dialect.porpg.PolarDBPgDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class PorPgCreateUtils extends AbstractSqlBuilder {

    public Dialect getDialect() { return PolarDBPgDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        List<String> ddlScripts = new ArrayList<>();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("create table ");
        sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i);
            buildColumn(sqlBuild, buildContext, eColumn);
        }

        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, eTable, primaryKey);
        }

        // idx Unique
        List<EIndex> uniqueIndices = eTable.getIndices();
        uniqueIndices = uniqueIndices.stream().filter(eIndex -> eIndex.getType() == EIndexType.Unique).collect(Collectors.toList());
        for (EIndex uniqueIndex : uniqueIndices) {
            sqlBuild.append(",\n");
            buildUniqueIndex(sqlBuild, buildContext, schema, table, uniqueIndex);
        }

        // eTable.getForeignKeys();
        // private List<EForeignKey> foreignKeys = new ArrayList<>();
        sqlBuild.append(");");
        ddlScripts.add(sqlBuild.toString());

        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            StringBuilder commentSql = new StringBuilder();
            this.buildTableComment(commentSql, buildContext, eTable);
            ddlScripts.add(commentSql.toString());
        }

        // columns comment
        for (EColumn eColumn : columnList) {
            if (StringUtils.isBlank(eColumn.getComment())) {
                continue;
            }
            StringBuilder columnCommentSql = new StringBuilder();
            this.buildColumnComment(columnCommentSql, buildContext, eTable, eColumn);
            ddlScripts.add(columnCommentSql.toString());
        }

        // idx
        List<EIndex> idxIndices = eTable.getIndices();
        idxIndices = idxIndices.stream().filter(eIndex -> eIndex.getType() != EIndexType.Unique).collect(Collectors.toList());
        for (int i = 0; i < idxIndices.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EIndex index = idxIndices.get(i);
            StringBuilder indexBuild = new StringBuilder();
            buildIndex(indexBuild, buildContext, schema, table, index);
            ddlScripts.add(indexBuild.toString());
        }

        return ddlScripts;
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("  ");
        sqlBuild.append(fmtName(useDelimited, eColumn.getName()));
        String columnType = PorPgTypeUtils.buildColumnType(eColumn, buildContext);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EPrimaryKey primaryKey) {
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

    private void buildUniqueIndex(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EIndex eIndex) {
        boolean useDelimited = buildContext.isUseDelimited();

        String indexName = safeIdxName("uk", table, eIndex.getName(), true);
        sqlBuild.append("  constraint ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexName));
        sqlBuild.append(" unique ");

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    private void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EIndex eIndex) {
        boolean useDelimited = buildContext.isUseDelimited();

        String indexName = safeIdxName("idx", table, eIndex.getName(), true);
        List<String> columnName = eIndex.getColumnList();

        sqlBuild.append("create index ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexName));
        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));

        String typeNameValue = PolarDBPgAttributeNames.INDEX_TYPE_NAME.getValue(eIndex.getAttribute());
        if (StringUtils.isNotBlank(typeNameValue) && !StringUtils.equalsIgnoreCase(typeNameValue, "btree")) {
            sqlBuild.append(" using " + typeNameValue + " ");
        }

        sqlBuild.append("(");
        for (int i = 0; i < columnName.size(); i++) {
            String column = columnName.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(");");
    }

    private void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            sqlBuild.append("comment on table ");
            sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));
            sqlBuild.append(" is '").append(getDialect().fmtComment(eTable.getComment())).append("';");
        }
    }

    private void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("comment on column ");
        sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append(".");
        sqlBuild.append(fmtName(useDelimited, eColumn.getName()));
        sqlBuild.append(" is '").append(getDialect().fmtComment(eColumn.getComment())).append("';");
    }
}
