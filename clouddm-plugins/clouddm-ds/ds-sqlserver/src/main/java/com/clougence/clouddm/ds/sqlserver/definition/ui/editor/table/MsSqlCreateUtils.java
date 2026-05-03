package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

/**
 * @author Ekko
 * @date 2023/9/12 10:58
*/
public class MsSqlCreateUtils extends AbstractSqlBuilder {

    public DsType getDataSourceType() { return DsType.SqlServer; }

    public SqlServerDialect getDialect() { return SqlServerDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> ddlScripts = new ArrayList<>();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("create table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, eTable.getCatalog(), eTable.getSchema(), eTable.getName()));
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
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        SqlServerTypes sqlTypes = SqlServerTypes.valueOfCode(eColumn.getDbType());
        String columnType = MsSqlTypeUtils.buildColumnType(sqlTypes, eColumn, buildContext);
        String defaultValue = MsSqlTypeUtils.buildDefault(sqlTypes, mainVersion, eColumn, buildContext);

        sqlBuild.append(" ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        sqlBuild.append(" ");
        sqlBuild.append(columnType);
        sqlBuild.append(" ");

        if (ePrimaryKey != null && ePrimaryKey.getColumnList().contains(eColumn.getName())) {
            sqlBuild.append("not null");
        } else if (!Boolean.TRUE.equals(eColumn.getNullable())) {
            sqlBuild.append("not null");
        } else {
            sqlBuild.append("null");
        }
        sqlBuild.append(" ");
        sqlBuild.append(defaultValue);
    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("constraint ");
        sqlBuild.append(getDialect().fmtName(useDelimited, primaryKey.getPrimaryKeyName()));
        sqlBuild.append(" primary key(");
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
        StringBuilder sqlBuild = new StringBuilder();

        if (eIndex.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index ");
            sqlBuild.append(getDialect().fmtName(useDelimited, eIndex.getName()));
        } else {
            sqlBuild.append("create index ");
            sqlBuild.append(getDialect().fmtName(useDelimited, eIndex.getName()));
        }

        sqlBuild.append(" on " + getDialect().fmtTableName(useDelimited, catalog, schema, table) + " ");

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
            String catalog = getDialect().fmtName(useDelimited, eTable.getCatalog());
            String schema = getDialect().fmtName(useDelimited, eTable.getSchema());
            String table = getDialect().fmtName(useDelimited, eTable.getName());
            String comment = getDialect().fmtComment(eTable.getComment());

            if (StringUtils.isBlank(catalog)) {
                sqlBuild.append("exec sys.sp_addextendedproperty ");
            } else {
                sqlBuild.append("exec " + catalog + ".sys.sp_addextendedproperty ");
            }
            schema = StringUtils.isBlank(schema) ? "dbo" : schema;

            sqlBuild.append("'MS_Description', N'" + comment + "', ");
            sqlBuild.append("'SCHEMA', " + schema + ", ");
            sqlBuild.append("'TABLE', " + table);
            sqlBuild.append(";");
        }
    }

    private void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
        if (StringUtils.isNotBlank(eColumn.getComment())) {
            boolean useDelimited = buildContext.isUseDelimited();
            String catalog = getDialect().fmtName(useDelimited, eTable.getCatalog());
            String schema = getDialect().fmtName(useDelimited, eTable.getSchema());
            String table = getDialect().fmtName(useDelimited, eTable.getName());
            String column = getDialect().fmtName(useDelimited, eColumn.getName());
            String comment = getDialect().fmtComment(eColumn.getComment());

            if (StringUtils.isBlank(catalog)) {
                sqlBuild.append("exec sys.sp_addextendedproperty ");
            } else {
                sqlBuild.append("exec " + catalog + ".sys.sp_addextendedproperty ");
            }
            schema = StringUtils.isBlank(schema) ? "dbo" : schema;

            sqlBuild.append("'MS_Description', N'" + comment + "', ");
            sqlBuild.append("'SCHEMA', " + schema + ", ");
            sqlBuild.append("'TABLE', " + table + ", ");
            sqlBuild.append("'COLUMN', " + column);
            sqlBuild.append(";");
        }
    }
}
