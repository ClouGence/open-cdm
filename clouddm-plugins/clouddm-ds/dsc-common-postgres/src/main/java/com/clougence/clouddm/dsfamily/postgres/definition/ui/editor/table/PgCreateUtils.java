package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table;

import static com.clougence.adapter.postgre.PostgreAttributeNames.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;

/**
 * @author caishan
 * @date 2023/8/14 14:39
 */
public class PgCreateUtils extends AbstractSqlBuilder {

    @Override
    public Dialect getDialect() { return PostgreDialect.INSTANCE; }

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
        sqlBuild.append("(\n");

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
        if (!seqNames.isEmpty()) {
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
        sqlBuild.append("\n) ");
        //createOption
        buildTableCreateOption(sqlBuild, eTable);
        ddlScripts.add(sqlBuild.toString());

        //table comment
        if (eTable.getComment() != null && !eTable.getComment().isEmpty()) {
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

    protected void buildIndexComment(StringBuilder commentBuild, String schema, EIndex index) {
        //COMMENT ON INDEX "public"."aaa" IS 'zzzx';
        String comment = index.getComment();
        if (StringUtils.isNotBlank(comment)) {
            commentBuild.append("COMMENT ON INDEX ");
            commentBuild.append(schema);
            commentBuild.append(".");
            commentBuild.append(index.getName());
            commentBuild.append(" IS ");
            commentBuild.append("'");
            commentBuild.append(comment);
            commentBuild.append("';");
        }
    }

    protected String buildTableCreateOption(StringBuilder sqlBuild, ETable eTable) {
        Map<String, String> attrMap = eTable.getAttribute();
        String fatherArr = attrMap.get(INHERITED_FROM.getCodeKey());
        if (StringUtils.isNotBlank(fatherArr)) {
            if (fatherArr.startsWith("[") && fatherArr.endsWith("]")) {
                fatherArr = fatherArr.replace("[", "");
                fatherArr = fatherArr.replace("]", "");
                if (StringUtils.isNotBlank(fatherArr)) {
                    String[] split = fatherArr.split(",");
                    sqlBuild.append("INHERITS (");
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
        String withOid = attrMap.get(WITH_OIDS.getCodeKey());
        String fill = attrMap.get(FILL_FACTOR.getCodeKey());
        if (StringUtils.isNotBlank(withOid) || StringUtils.isNotBlank(fill)) {
            List<String> option = new ArrayList<>();
            sqlBuild.append("WITH (");
            if (StringUtils.isNotBlank(withOid)) {
                if ("true".equals(withOid)) {
                    option.add("OIDS=TRUE");
                } else {
                    option.add("OIDS=FALSE");
                }
            }
            if (StringUtils.isNotBlank(fill)) {
                option.add("FILLFACTOR=" + fill);
            }
            sqlBuild.append(String.join(",", option));
            sqlBuild.append(") ");
        }

        String tablespace = attrMap.get(TABLESPACE.getCodeKey());
        if (StringUtils.isNotBlank(tablespace)) {
            sqlBuild.append("TABLESPACE ");
            sqlBuild.append(tablespace);
        }
        sqlBuild.append(";");
        return sqlBuild.toString();
    }

    protected void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn, String tableName, List<String> seqNames, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        String columnType = PgTypeUtils.buildColumnMsg(eColumn, buildContext, tableName, seqNames, primaryKey);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
    }

    protected void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("CONSTRAINT ");
        sqlBuild.append(getDialect().fmtName(useDelimited, primaryKey.getPrimaryKeyName()));
        sqlBuild.append(" PRIMARY KEY(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(") ");
        Map<String, String> pkAttrMap = primaryKey.getAttribute();
        String delay = pkAttrMap.get(PK_CONSTRAINT_DELAY.getCodeKey());
        if (StringUtils.isNotBlank(delay)) {
            switch (delay) {
                case "NOT DEFERRABLE":
                    sqlBuild.append("INITIALLY IMMEDIATE");
                    break;
                case "DEFERRABLE":
                    sqlBuild.append("INITIALLY DEFERRED");
                    break;
                case "":
                    break; // use default
                default:
                    throw new UnsupportedOperationException("Unsupported delay type :" + delay);
            }
            //String initial = pkAttrMap.get(PK_CONSTRAINT_INITIAL.getCodeKey());
            //if (StringUtils.isNotBlank(initial)) {
            //    sqlBuild.append(" " + initial);
            //}
        }
    }

    public void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, String schema, String table, EIndex eIndex, Map<String, PostgresTypes> columnTypeMap) {
        //CREATE UNIQUE INDEX idx_my_table_columns_prefix ON public.my_table USING btree (
        //  SUBSTRING(column1 FROM 1 FOR 10) COLLATE "pg_catalog"."de-IT-x-icu" "pg_catalog"."varchar_ops" ASC NULLS LAST,
        //  SUBSTRING(column2 FROM 1 FOR 20) COLLATE "pg_catalog"."en-US-x-icu" "pg_catalog"."varchar_ops" DESC NULLS FIRST
        //);
        Map<String, String> attrMap = eIndex.getAttribute();
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> columnName = eIndex.getColumnList();

        if (EIndexType.Unique.getTypeName().equals(attrMap.get(INDEX_WAY.getCodeKey()))) {
            sqlBuild.append("CREATE UNIQUE INDEX ");
        } else {
            sqlBuild.append("CREATE INDEX ");
        }

        if ("true".equals(attrMap.get(INDEX_CONCURRENTLY.getCodeKey()))) {
            sqlBuild.append("CONCURRENTLY ");
        }
        String indexName = eIndex.getName();
        sqlBuild.append(getDialect().fmtName(useDelimited, indexName));

        sqlBuild.append(" ON ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        //default index type is btree
        String indexType = attrMap.get(INDEX_TYPE.getCodeKey());
        if (StringUtils.isNotBlank(indexType)) {
            sqlBuild.append(" USING ");
            sqlBuild.append(indexType).append("(");
            fmtColumn(columnName, attrMap, sqlBuild, columnTypeMap, indexType);
            sqlBuild.append(")");
        } else {
            sqlBuild.append("(");
            fmtColumn(columnName, attrMap, sqlBuild, columnTypeMap, indexType);
            sqlBuild.append(")");
        }

        //update index name
        eIndex.setName(indexName);
        //with option
        fillWithOption(attrMap, sqlBuild);
        //tablespace
        String tablespace = attrMap.get(INDEX_TABLESPACE.getCodeKey());
        if (StringUtils.isNotBlank(tablespace)) {
            sqlBuild.append(" TABLESPACE ").append(tablespace);
        }
        //where predicate
        String where = attrMap.get(INDEX_WHERE.getCodeKey());
        if (StringUtils.isNotBlank(where)) {
            sqlBuild.append(" WHERE ").append(where);
        }
        sqlBuild.append(";");
    }

    private void fillWithOption(Map<String, String> attrMap, StringBuilder sqlBuild) {
        List<String> option = new ArrayList<>();
        if (attrMap.containsKey(INDEX_WITH_FILLFACTOR.getCodeKey())) {
            String fill = attrMap.get(INDEX_WITH_FILLFACTOR.getCodeKey());
            if (StringUtils.isNotBlank(fill)) {
                option.add("FILLFACTOR=" + fill);
            }
        }
        if (attrMap.containsKey(INDEX_WITH_BUFFERING.getCodeKey())) {
            String buffer = attrMap.get(INDEX_WITH_BUFFERING.getCodeKey());
            if (StringUtils.isNotBlank(buffer)) {
                option.add("BUFFERING=" + buffer);
            }
        }
        if (attrMap.containsKey(INDEX_WITH_FASTUPDATE.getCodeKey())) {
            String fast = attrMap.get(INDEX_WITH_FASTUPDATE.getCodeKey());
            if (StringUtils.isNotBlank(fast)) {
                if ("true".equals(fast)) {
                    option.add("FASTUPDATE=ON");
                } else {
                    option.add("FASTUPDATE=OFF");
                }
            }
        }
        if (CollectionUtils.isNotEmpty(option)) {
            sqlBuild.append(" WITH (").append(String.join(", ", option)).append(")");
        }
    }

    protected void buildTableComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        if (eTable.getComment() != null && !"".equals(eTable.getComment())) {
            sqlBuild.append("COMMENT ON TABLE ");
            sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
            sqlBuild.append(" IS '").append(getDialect().fmtComment(eTable.getComment())).append("';\n");
        }
    }

    protected void fmtColumn(List<String> columnName, Map<String, String> attrMap, StringBuilder sqlBuild, Map<String, PostgresTypes> columnTypeMap, String indexType) {
        String prefixLenJson = attrMap.get(INDEX_PREFIX_LENGTH.getCodeKey());
        HashMap<String, String> prefixLenMap = new HashMap<>();
        if (StringUtils.isNotBlank(prefixLenJson)) {
            Object orderObj = JSON.parse(prefixLenJson);
            if (orderObj instanceof Map) {
                prefixLenMap = (HashMap<String, String>) orderObj;
            }
        }
        // String sortOrderJson = attrMap.get(INDEX_SORT_ORDER.getCodeKey());
        // HashMap<String, String> sortOrderMap = new HashMap<>();
        // if (StringUtils.isNotBlank(sortOrderJson)) {
        //     Object orderObj = JSON.parse(sortOrderJson);
        //     if (orderObj instanceof Map) {
        //         sortOrderMap = (HashMap<String, String>) orderObj;
        //     }
        // }
        // String nullsSortJson = attrMap.get(INDEX_NULLS_SORT.getCodeKey());
        // HashMap<String, String> nullsSortMap = new HashMap<>();
        // if (StringUtils.isNotBlank(nullsSortJson)) {
        //     Object orderObj = JSON.parse(nullsSortJson);
        //     if (orderObj instanceof Map) {
        //         nullsSortMap = (HashMap<String, String>) orderObj;
        //     }
        // }
        // String operatorTypeJson = attrMap.get(INDEX_OPERATOR_TYPE.getCodeKey());
        // HashMap<String, String> operatorTypeMap = new HashMap<>();
        // if (StringUtils.isNotBlank(operatorTypeJson)) {
        //     Object orderObj = JSON.parse(operatorTypeJson);
        //     if (orderObj instanceof Map) {
        //         operatorTypeMap = (HashMap<String, String>) orderObj;
        //     }
        // }
        // String sortRulesJson = attrMap.get(INDEX_SORT_RULES.getCodeKey());
        // HashMap<String, String> sortRulesMap = new HashMap<>();
        // if (StringUtils.isNotBlank(sortRulesJson)) {
        //     Object orderObj = JSON.parse(sortRulesJson);
        //     if (orderObj instanceof Map) {
        //         sortRulesMap = (HashMap<String, String>) orderObj;
        //     }
        // }
        for (String column : columnName) {
            String prefixLen = prefixLenMap.get(column);
            if (StringUtils.isNotBlank(prefixLen)) {
                //SUBSTRING(column1 FROM 1 FOR 10)
                sqlBuild.append("SUBSTRING( ");
                sqlBuild.append(column);
                sqlBuild.append(" from 1 for ");
                sqlBuild.append(prefixLen);
                sqlBuild.append(" )");
            } else {
                sqlBuild.append(column);
                sqlBuild.append(" ");
            }
            // String sortRoles = sortRulesMap.get(column);
            // String operatorType = operatorTypeMap.get(column);
            // String sortOrder = sortOrderMap.get(column);
            // String nullsSort = nullsSortMap.get(column);
            // if (StringUtils.isNotBlank(sortRoles) || StringUtils.isNotBlank(operatorType) || StringUtils.isNotBlank(sortOrder) || StringUtils.isNotBlank(nullsSort)) {
            //     if (StringUtils.isNotBlank(operatorType)) {
            //         String[] split = operatorType.split("\\.");
            //         sqlBuild.append("\"");
            //         sqlBuild.append(split[0]);
            //         sqlBuild.append("\".\"");
            //         sqlBuild.append(split[1]);
            //         sqlBuild.append("\" ");
            //     }
            //     if ("btree".equals(indexType)) {
            //         if (StringUtils.isNotBlank(sortOrder)) {
            //             sortOrder = "A".equals(sortOrder) ? "asc" : "desc";
            //             sqlBuild.append(sortOrder);
            //             sqlBuild.append(" ");
            //         }
            //         if (StringUtils.isNotBlank(nullsSort)) {
            //             sqlBuild.append(nullsSort);
            //         }
            //     }
            // }
            sqlBuild.append(",");
        }
        int i = sqlBuild.lastIndexOf(",");
        if (i >= 0) {
            sqlBuild.deleteCharAt(i);
        }
    }

    protected void buildColumnComment(StringBuilder sqlBuild, TriggerContext buildContext, ETable eTable, EColumn eColumn) {
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append("COMMENT ON COLUMN ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append(".");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        sqlBuild.append(" IS '").append(getDialect().fmtComment(eColumn.getComment())).append("';");
    }

}
