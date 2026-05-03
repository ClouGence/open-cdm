package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2021/11/18 17:11:03
 */
public class Db2CreateUtils extends AbstractSqlBuilder {

    public Db2Dialect getDialect() { return Db2Dialect.INSTANCE; }

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
            buildColumn(sqlBuild, buildContext, eColumn, eTable.getPrimaryKey());
        }

        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, eTable, primaryKey);
        }

        sqlBuild.append(");");
        ddlScripts.add(sqlBuild.toString());

        // idx and uk
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                String script = buildIndex(buildContext, eTable.getCatalog(), eTable.getSchema(), eTable.getName(), index);
                ddlScripts.add(script);
            }
        }

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
        return ddlScripts;
    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn, EPrimaryKey ePrimaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();

        sqlBuild.append(" ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        String columnType = Db2TypeUtils.buildColumnType(eColumn, buildContext, ePrimaryKey);
        sqlBuild.append(" ");
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

            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")");
    }

    private String buildIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex eIndex) {
        boolean useDelimited = buildContext.isUseDelimited();
        String indexName = eIndex.getName();
        StringBuilder sqlBuild = new StringBuilder();

        if (eIndex.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexName));
        } else {
            sqlBuild.append("create index ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexName));
        }

        sqlBuild.append(" on ").append(getDialect().fmtTableName(useDelimited, null, schema, table)).append(" ");

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(");");
        return sqlBuild.toString();
    }

    private void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        if (StringUtils.isNotBlank(eTable.getComment())) {
            boolean useDelimited = buildContext.isUseDelimited();
            String schema = getDialect().fmtName(useDelimited, eTable.getSchema());
            String table = getDialect().fmtName(useDelimited, eTable.getName());
            String comment = getDialect().fmtComment(eTable.getComment());

            sqlBuild.append("comment on table ");
            sqlBuild.append(schema).append(".").append(table);
            sqlBuild.append(" is '").append(comment).append("'");
        }
    }

    private void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            boolean useDelimited = buildContext.isUseDelimited();
            String schema = getDialect().fmtName(useDelimited, eTable.getSchema());
            String table = getDialect().fmtName(useDelimited, eTable.getName());
            String column = getDialect().fmtName(useDelimited, eColumn.getName());
            String comment = getDialect().fmtComment(eColumn.getComment());

            sqlBuild.append("comment on column ");
            sqlBuild.append(schema).append(".").append(table).append(".").append(column);
            sqlBuild.append(" is '").append(comment).append("'");
        }
    }
}
