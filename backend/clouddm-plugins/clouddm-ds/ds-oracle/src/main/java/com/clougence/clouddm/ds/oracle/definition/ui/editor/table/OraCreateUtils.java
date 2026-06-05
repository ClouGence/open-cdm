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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class OraCreateUtils extends AbstractSqlBuilder {

    public Dialect getDialect() { return OracleDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> ddlScripts = new ArrayList<>();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE TABLE ");
        sqlBuild.append(fmtTable(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i);
            buildColumn(sqlBuild, buildContext, eTable.getPrimaryKey(), eColumn);
        }
        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, primaryKey);
        }
        sqlBuild.append(")");
        sqlBuild.append(";");
        ddlScripts.add(sqlBuild.toString());
        // idx
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                StringBuilder indexBuild = new StringBuilder();
                buildIndex(indexBuild, buildContext, schema, table, index);
                ddlScripts.add(indexBuild.toString());
            }
        }

        // table comment
        if (StringUtils.isNotBlank(eTable.getComment())) {
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

        return ddlScripts;
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey ePrimaryKey, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();

        String columnType = OraTypeUtils.buildColumnType(eColumn, buildContext, ePrimaryKey);
        sqlBuild.append("  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append("PRIMARY KEY(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    private void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EIndex eIndex) {
        boolean useDelimited = buildContext.isUseDelimited();

        if (eIndex.getType() == EIndexType.Unique) {
            sqlBuild.append(" ALTER TABLE ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
            sqlBuild.append(" ADD CONSTRAINT ");
            String indexName = safeIdxName("uk", table, eIndex.getName(), true);
            sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            sqlBuild.append(" UNIQUE ");
        } else {
            sqlBuild.append(" CREATE INDEX ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, eIndex.getName()));
            sqlBuild.append(" ON ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        }

        sqlBuild.append("(");
        List<String> columnList = eIndex.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
        sqlBuild.append(";");

        //        if (eIndex.getComment() != null) {
        //            sqlBuild.append("\n");
        //            sqlBuild.append(" COMMENT ON INDEX ");
        //            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, eIndex.getName()));
        //            sqlBuild.append(" ON ");
        //            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        //            sqlBuild.append(" IS ");
        //            sqlBuild.append("'");
        //            sqlBuild.append(eIndex.getComment());
        //            sqlBuild.append("'");
        //            sqlBuild.append(";");
        //        }
    }

    private void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            sqlBuild.append("COMMENT ON TABLE ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
            sqlBuild.append(" IS '").append(getDialect().fmtComment(eTable.getComment())).append("'");
            sqlBuild.append(";");
        }
    }

    private void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("COMMENT ON COLUMN ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append(".");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        sqlBuild.append(" IS '").append(getDialect().fmtComment(eColumn.getComment())).append("'");
        sqlBuild.append(";");
    }
}
