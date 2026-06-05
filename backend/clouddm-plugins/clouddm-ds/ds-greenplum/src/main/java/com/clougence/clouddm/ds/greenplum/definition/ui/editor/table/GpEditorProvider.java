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
package com.clougence.clouddm.ds.greenplum.definition.ui.editor.table;

import static com.clougence.adapter.greenplum.GreenplumAttributeNames.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTypeUtils;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HashUtils;
import com.clougence.utils.StringUtils;

/**
 * @author caishan
 * @date 2023/8/14 14:42
 */
public class GpEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new GpEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.PostgreSQL; }

    public Dialect getDialect() { return PostgreDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("ALTER TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" RENAME TO ");
        //modifying  name cannot specify schema
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("COMMENT ON TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(" IS '");
        sqlBuild.append(getDialect().fmtComment(comment));
        sqlBuild.append("';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new GpCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DROP TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder seqBuild = new StringBuilder();
        List<String> seqNames = new ArrayList<>();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" ADD " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + PgTypeUtils.buildColumnMsg(columnInfo, buildContext, table, seqNames, eTable.getPrimaryKey()));
        sqlBuild.append(";");
        ArrayList<String> columnScripts = new ArrayList<>();
        columnScripts.add(sqlBuild.toString());
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            columnScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        }
        // create sequence
        if (!seqNames.isEmpty()) {
            for (String seqName : seqNames) {
                seqBuild.append("CREATE SEQUENCE ").append(seqName).append(";\n");
            }
            columnScripts.add(0, seqBuild.toString());
        }
        return columnScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" DROP COLUMN ");
        sqlBuild.append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        if (buildContext.isCascade()) {
            sqlBuild.append(" CASCADE");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" RENAME COLUMN ");
        sqlBuild.append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" TO ");
        sqlBuild.append(getDialect().fmtName(useDelimited, newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        //PostgreSQL only one set
        List<String> result = new ArrayList<>();
        Map<String, String> tableAttr = eTable.getAttribute();
        String sqlPrefix = this.buildAlterTable(buildContext, catalog, schema, table).toString();
        StringBuilder sqlBuilder = new StringBuilder();
        //comment
        String comment = eTable.getComment();
        if (comment != null) {
            List<String> commentList = tableComment(buildContext, catalog, schema, table, comment);
            result.addAll(commentList);
        }
        //tablespace
        if (tableAttr.containsKey(TABLESPACE.getCodeKey())) {
            String tablespace = tableAttr.get(TABLESPACE.getCodeKey());
            if (StringUtils.isNotBlank(tablespace)) {
                sqlBuilder.append(sqlPrefix);
                sqlBuilder.append(" SET TABLESPACE ").append(tablespace).append(";");
                result.add(sqlBuilder.toString());
                sqlBuilder.setLength(0);
            }
        }
        //logged/un
        if (tableAttr.containsKey(TABLE_TYPE.getCodeKey())) {
            String tableType = tableAttr.get(TABLE_TYPE.getCodeKey());
            if (StringUtils.isNotBlank(tableType)) {
                sqlBuilder.append(sqlPrefix).append(" SET ");
                switch (tableType) {
                    case "p":
                        sqlBuilder.append("LOGGED;");
                        break;
                    case "u":
                        sqlBuilder.append("UNLOGGED;");
                        break;
                }
                result.add(sqlBuilder.toString());
                sqlBuilder.setLength(0);
            }
        }
        //fillfactor
        if (tableAttr.containsKey(FILL_FACTOR.getCodeKey())) {
            String factor = tableAttr.get(FILL_FACTOR.getCodeKey());
            sqlBuilder.append(sqlPrefix);
            if (StringUtils.isBlank(factor)) {
                sqlBuilder.append(" RESET (FILLFACTOR);");
            } else {
                sqlBuilder.append(" SET (FILLFACTOR=").append(factor).append(");");
            }
            result.add(sqlBuilder.toString());
            sqlBuilder.setLength(0);
        }
        //with oid
        if (tableAttr.containsKey(WITH_OIDS.getCodeKey())) {
            String withOid = tableAttr.get(WITH_OIDS.getCodeKey());
            sqlBuilder.append(sqlPrefix);
            if ("true".equals(withOid)) {
                sqlBuilder.append(" SET WITH OIDS;");
            } else {
                sqlBuilder.append(" SET WITHOUT OIDS;");
            }
            result.add(sqlBuilder.toString());
            sqlBuilder.setLength(0);
        }
        //inherit
        if (tableAttr.containsKey(INHERITED_FROM.getCodeKey())) {
            //source rdbpg_info -> abc, my_table2   null
            //change rdbpg_info -> [abc,  my_table2, aa, bb]   rdbpg_info -> []
            String inherit = tableAttr.get(INHERITED_FROM.getCodeKey());
            inherit = inherit.replace("[", "").replace("]", "");
            String sourceInherit = sourceAttr.get(INHERITED_FROM.getCodeKey());
            if (sourceInherit == null) {
                sourceInherit = "";
            }
            List<String> collect = Arrays.stream(inherit.split(",")).collect(Collectors.toList());
            List<String> sourceCollect = Arrays.stream(sourceInherit.split(",")).collect(Collectors.toList());
            Iterator<String> iterator = collect.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                String trim = item.trim();
                if (sourceCollect.contains(trim)) {
                    iterator.remove();
                    sourceCollect.remove(trim);
                }
            }
            if (CollectionUtils.isNotEmpty(sourceCollect) || CollectionUtils.isNotEmpty(collect)) {
                sqlBuilder.append(sqlPrefix);
                for (int i = 0; i < sourceCollect.size(); i++) {
                    String s = sourceCollect.get(i);
                    if (!"".equals(s)) {
                        if (i != 0) {
                            sqlBuilder.append(",");
                        }
                        sqlBuilder.append(" NO INHERIT ");
                        sqlBuilder.append("\"");
                        sqlBuilder.append(s);
                        sqlBuilder.append("\"");
                    }
                }
                for (int i = 0; i < collect.size(); i++) {
                    String s = collect.get(i);
                    if (!"".equals(s)) {
                        if (i != 0) {
                            sqlBuilder.append(",");
                        }
                        sqlBuilder.append(" INHERIT ");
                        sqlBuilder.append("\"");
                        sqlBuilder.append(s);
                        sqlBuilder.append("\"");
                    }
                }
                sqlBuilder.append(";");
                result.add(sqlBuilder.toString());
            }
        }
        return result;
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        //one column one ddl
        boolean useDelimited = buildContext.isUseDelimited();
        List<String> sqlSplit = new ArrayList<>();
        StringBuilder seqBuild = new StringBuilder();
        String tableMsg = buildAlterTable(buildContext, catalog, schema, table).toString();
        String newType = PgTypeUtils.buildColumnType(newInfo, buildContext);
        String oldType = PgTypeUtils.buildColumnType(columnInfo, buildContext);
        String sqlPrefix = " ALTER COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName());
        //data type
        if (!newType.equals(oldType)) {
            StringBuilder sqlBuild = new StringBuilder();
            sqlBuild.append(sqlPrefix);
            sqlBuild.append(" TYPE ");
            sqlBuild.append(newType);
            sqlBuild.append(" USING ");
            sqlBuild.append(getDialect().fmtName(useDelimited, columnInfo.getName()));
            sqlBuild.append("::");
            sqlBuild.append(findDataType(newInfo));
            sqlSplit.add(sqlBuild.toString());
        }
        // null/not null
        if (Boolean.TRUE.equals(columnInfo.getNullable()) != Boolean.TRUE.equals(newInfo.getNullable())) {
            StringBuilder sqlBuild = new StringBuilder();
            if (Boolean.TRUE.equals(newInfo.getNullable())) {
                sqlBuild.append(sqlPrefix).append(" DROP NOT NULL");
            } else {
                sqlBuild.append(sqlPrefix).append(" SET NOT NULL");
            }
            sqlSplit.add(sqlBuild.toString());
        }
        // default value
        List<String> seqNames = new ArrayList<>();
        String newDb = newInfo.getDbType();
        String oldDefault = columnInfo.getDefaultValue();//this.buildDefault(oldSqlType, columnInfo, table, "update", new ArrayList<>(), buildContext);
        String newDefault = newInfo.getDefaultValue();   // this.buildDefault(newSqlType, newInfo, table, "update", seqNames, buildContext);
        PostgresTypes newSqlType = PostgresTypes.valueOfCode(newDb);
        if (!StringUtils.equals(oldDefault, newDefault)) {
            StringBuilder sqlBuild = new StringBuilder();
            if (oldDefault != null && newDefault == null) {
                sqlBuild.append(sqlPrefix).append(" DROP DEFAULT");
            } else {
                String buildDefault = PgTypeUtils.buildDefault2Create(newSqlType, newInfo, table, seqNames, buildContext, eTable.getPrimaryKey());
                sqlBuild.append(sqlPrefix).append(" SET ").append(buildDefault);
            }
            sqlSplit.add(sqlBuild.toString());
        }
        // create sequence
        if (!seqNames.isEmpty()) {
            for (String seqName : seqNames) {
                seqBuild.append("CREATE SEQUENCE IF NOT EXISTS ").append(seqName).append(";\n");
            }
        }
        // identity
        Map<String, String> oldAttrMap = columnInfo.getAttribute();
        String oldVirtual = oldAttrMap.get(VIRTUAL_TYPE.getCodeKey());
        String identity = buildIdentity(newInfo, oldVirtual, sqlPrefix);
        if (StringUtils.isNotBlank(identity)) {
            sqlSplit.add(identity);
        }
        //alter column comment
        StringBuilder comment = new StringBuilder();
        if (!Objects.equals(newInfo.getComment(), columnInfo.getComment())) {
            buildColumnComment(newInfo, comment, schema, table);
        }
        String sql = "";
        if (CollectionUtils.isNotEmpty(sqlSplit)) {
            sql = tableMsg + String.join(",\n", sqlSplit) + ";";
        }
        return Stream.of(seqBuild.toString(), sql, comment.toString()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private static void buildColumnComment(EColumn newInfo, StringBuilder sqlBuild, String schema, String table) {
        sqlBuild.append("COMMENT ON COLUMN ");
        sqlBuild.append(schema);
        sqlBuild.append(".\"");
        sqlBuild.append(table);
        sqlBuild.append("\".\"");
        sqlBuild.append(newInfo.getName());
        sqlBuild.append("\" IS '");
        sqlBuild.append(newInfo.getComment());
        sqlBuild.append("';\n");
    }

    private static String buildIdentity(EColumn newInfo, String oldVirtual, String sqlPrefix) {
        StringBuilder sqlBuild = new StringBuilder();
        Map<String, String> newAttrMap = newInfo.getAttribute();
        String option = newAttrMap.get(COLUMN_DEFAULT_OPTION.getCodeKey());
        String newVirtual = newAttrMap.get(VIRTUAL_TYPE.getCodeKey());
        String flag = "";
        if (!"Virtual Type".equals(option)) {
            newVirtual = null;
        }
        if (Objects.equals(newVirtual, oldVirtual)) {
            return sqlBuild.toString();
        } else if (StringUtils.isNotBlank(oldVirtual)) {
            flag = "update";
        }
        String incremental = newAttrMap.get(VIRTUAL_TYPE_INCREMENTAL.getCodeKey());
        String min = newAttrMap.get(VIRTUAL_TYPE_MIN.getCodeKey());
        String max = newAttrMap.get(VIRTUAL_TYPE_MAX.getCodeKey());
        String start = newAttrMap.get(VIRTUAL_TYPE_START.getCodeKey());
        String cache = newAttrMap.get(VIRTUAL_TYPE_CACHE.getCodeKey());
        String loop = newAttrMap.get(VIRTUAL_TYPE_LOOP.getCodeKey());
        if ("update".equals(flag)) {
            sqlBuild.append(sqlPrefix);
            if (!"STORED".equals(oldVirtual)) {
                if (StringUtils.isBlank(newVirtual) || "STORED".equals(newVirtual)) {
                    sqlBuild.append(" DROP IDENTITY ");
                    return sqlBuild.toString();
                }
            }
            if ("ALWAYS".equals(newVirtual)) {
                sqlBuild.append(" SET GENERATED ALWAYS\n");
            } else if ("BY DEFAULT".equals(newVirtual)) {
                sqlBuild.append(" SET GENERATED BY DEFAULT\n");
            }
            if (StringUtils.isNotBlank(incremental)) {
                sqlBuild.append(" SET INCREMENT ").append(incremental).append("\n");
            }
            if (StringUtils.isNotBlank(min)) {
                sqlBuild.append(" SET MINVALUE ").append(min).append("\n");
            }
            if (StringUtils.isNotBlank(max)) {
                sqlBuild.append(" SET MAXVALUE ").append(max).append("\n");
            }
            if (StringUtils.isNotBlank(start)) {
                sqlBuild.append(" SET START ").append(start).append("\n");
            }
            if (StringUtils.isNotBlank(cache)) {
                sqlBuild.append(" SET CACHE ").append(cache).append("\n");
            }
            if ("true".equals(loop)) {
                sqlBuild.append(" SET CYCLE");
            } else {
                sqlBuild.append(" SET NO CYCLE");
            }
        } else {
            if (!"STORED".equals(newVirtual)) {
                sqlBuild.append(sqlPrefix);
                if ("ALWAYS".equals(newVirtual)) {
                    sqlBuild.append(" ADD GENERATED ALWAYS AS IDENTITY ");
                } else if ("BY DEFAULT".equals(newVirtual)) {
                    sqlBuild.append(" ADD GENERATED BY DEFAULT AS IDENTITY ");
                }
                if (StringUtils.isNotBlank(incremental) || StringUtils.isNotBlank(min) || StringUtils.isNotBlank(max) || StringUtils.isNotBlank(start)
                    || StringUtils.isNotBlank(cache)) {
                    sqlBuild.append("(");
                    if (StringUtils.isNotBlank(incremental)) {
                        sqlBuild.append(" INCREMENT ").append(incremental).append("\n");
                    }
                    if (StringUtils.isNotBlank(min)) {
                        sqlBuild.append(" MINVALUE ").append(min).append("\n");
                    }
                    if (StringUtils.isNotBlank(max)) {
                        sqlBuild.append(" MAXVALUE ").append(max).append("\n");
                    }
                    if (StringUtils.isNotBlank(start)) {
                        sqlBuild.append(" START ").append(start).append("\n");
                    }
                    if (StringUtils.isNotBlank(cache)) {
                        sqlBuild.append(" CACHE ").append(cache).append("\n");
                    }
                    if ("true".equals(loop)) {
                        sqlBuild.append(" CYCLE ").append("\n");
                    }
                    sqlBuild.append(")");
                }
            }
        }
        return sqlBuild.toString();
    }

    private String findDataType(EColumn newInfo) {
        String dbType = newInfo.getDbType();
        if (dbType.endsWith("[]")) {
            return dbType;
        }
        if (PgTypeUtils.isDate(PostgresTypes.valueOfCode(dbType))) {
            Integer datetime = newInfo.getDatetimePrecision();
            if (datetime != null) {
                return dbType + "(" + datetime + ")";
            }
        } else if (PgTypeUtils.isLength(PostgresTypes.valueOfCode(dbType))) {
            Long length = newInfo.getLength();
            if (length != null) {
                return dbType + "(" + length + ")";
            }
        } else if (PgTypeUtils.isScale(PostgresTypes.valueOfCode(dbType))) {
            Integer precision = newInfo.getNumericPrecision();
            Integer scale = newInfo.getNumericScale();
            if (precision != null) {
                if (scale == null) {
                    scale = 0;
                }
                return dbType + "(" + precision + "," + scale + ")";
            }
        }
        return dbType;
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("COMMENT ON COLUMN ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(".");
        sqlBuild.append(getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" IS '" + getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        GpCreateUtils createUtils = new GpCreateUtils();
        StringBuilder sqlBuild = new StringBuilder();
        createUtils.buildIndex(sqlBuild, buildContext, schema, table, indexInfo, null);
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder("DROP INDEX ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("ALTER INDEX ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, indexInfo.getName()));
        sqlBuild.append(" to ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, newIndexName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        return indexScripts;
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName())) {
            return new ArrayList<>();
        }
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" ADD CONSTRAINT ");
        String pkName;
        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName())) {
            pkName = ("pk_" + Long.toString(HashUtils.fnvHash(UUID.randomUUID().toString()), 16)).toUpperCase();
        } else {
            pkName = primaryInfo.getPrimaryKeyName();
        }

        sqlBuild.append(getDialect().fmtName(useDelimited, pkName));
        sqlBuild.append(" PRIMARY KEY ");
        sqlBuild.append("(");
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(") ");
        createPKOption(sqlBuild, primaryInfo.getAttribute());
        return Collections.singletonList(sqlBuild.toString());
    }

    private void createPKOption(StringBuilder sqlBuild, Map<String, String> attribute) {
        String delay = attribute.get(PK_CONSTRAINT_DELAY.getCodeKey());
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

        //String initial = attribute.get(PK_CONSTRAINT_INITIAL.getCodeKey());
        //if (StringUtils.isNotBlank(initial)) {
        //    sqlBuild.append(" " + initial);
        //}
        sqlBuild.append(";");
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName())) {
            return new ArrayList<>();
        }
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" DROP CONSTRAINT \"");
        sqlBuild.append(primaryInfo.getPrimaryKeyName());
        sqlBuild.append("\";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needAddColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        return indexScripts;
    }

    @Override
    public List<String> createForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return Collections.emptyList();// TODO
    }

    @Override
    public List<String> dropForeignKey(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo) {
        return Collections.emptyList();// TODO
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext buildContext, String catalog, String schema, String table, EForeignKey foreignKeyInfo, String newForeignKeyName) {
        return Collections.emptyList();// TODO
    }

}
