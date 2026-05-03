package com.clougence.clouddm.ds.dameng.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2023-10-08 15:57
 */
@Slf4j
public class DmEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new DmEditorProvider();

    public DmEditorProvider(){
    }

    @Override
    public DsType getDataSourceType() { return DsType.Dameng; }

    @Override
    public Dialect getDialect() { return DmDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(this.fmtTable(useDelimited, catalog, schema, table));
        return sqlBuild;
    }

    private String getAlterSql(String prefix, EColumn info, String fmtColumn, EColumn column) {
        DmSqlTypes sqlTypes = DmSqlTypes.valueOfCode(info.getDbType());
        StringBuilder sqlBuild = new StringBuilder(prefix);
        sqlBuild.append(" modify ").append(fmtColumn);
        sqlBuild.append(" ");
        sqlBuild.append(com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTypeUtils.buildColumnType(sqlTypes, info, null));
        Map<String, String> attr = column.getAttribute();
        if ("virtual".equals(attr.get(DmAttributeNames.DEFAULT_OPTION.getCodeKey()))) {
            sqlBuild.append(com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTypeUtils.buildVirtual(attr));
        }

        if (Boolean.TRUE.equals(info.getNullable())) {
            sqlBuild.append(" null ");
        } else {
            sqlBuild.append(" not null ");
        }

        String defaultValue = com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTypeUtils.buildDefault(sqlTypes, info, null);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuild.append(" ").append(defaultValue).append(" ");
        }
        sqlBuild.append(";");
        return sqlBuild.toString();
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        StringBuilder alterTable = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(alterTable);

        sqlBuild.append(" rename ");
        sqlBuild.append(" to ");
        sqlBuild.append(getDialect().fmtName(useDelimited, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on table ");
        sqlBuild.append(this.fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(" is '" + this.getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        if (eTable.getComment() != null) {
            String sqlBuild = "comment on table " + fmtTable(true, null, schema, table) + " is '" + getDialect().fmtComment(eTable.getComment()) + "';";
            return Collections.singletonList(sqlBuild);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new DmCreateUtils().buildCreate(buildContext, schema, table, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop table ");
        sqlBuild.append(this.fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        String fmtColumn = fmtName(useDelimited, columnInfo.getName());
        sqlBuild.append(" add " + fmtColumn);
        sqlBuild.append(" " + DmTypeUtils.buildColumnType(columnInfo, buildContext));
        sqlBuild.append(";");

        List<String> ddlScripts = new ArrayList<>();
        ddlScripts.add(sqlBuild.toString());
        com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTypeUtils
            .buildIdentityForAdd(columnInfo.getAttribute(), ddlScripts, buildAlterTable(buildContext, catalog, schema, table).toString(), fmtColumn);
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            ddlScripts.addAll(columnComment(buildContext, catalog, schema, table, columnInfo, columnInfo.getComment(), eTable));
        }

        return ddlScripts;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop column " + this.fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" rename column " + this.fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" to " + this.fmtName(useDelimited, newColumnName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        List<String> ddlScripts = new ArrayList<>();

        EColumn oldColumn = columnInfo.clone();
        EColumn newColumn = newInfo.clone();
        oldColumn.setComment("");
        newColumn.setComment("");
        if (oldColumn.testChanged(newColumn)) {
            String fmtColumn = fmtName(useDelimited, newInfo.getName());
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

            String oldSql = getAlterSql(sqlBuild.toString(), columnInfo, fmtColumn, oldColumn);
            String newSql = getAlterSql(sqlBuild.toString(), newInfo, fmtColumn, newColumn);
            if (!newSql.equals(oldSql)) {
                ddlScripts.add(newSql);
            }

            com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTypeUtils
                .buildIdentityForAlter(oldColumn.getAttribute(), newColumn.getAttribute(), ddlScripts, sqlBuild.toString(), fmtColumn);
        }

        if (newInfo.getComment() != null) {
            ddlScripts.addAll(columnComment(buildContext, catalog, schema, table, newInfo, newInfo.getComment(), eTable));
        }

        return ddlScripts;
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("comment on column ");
        sqlBuild.append(this.fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(".");
        sqlBuild.append(this.fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" is '" + this.getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        new DmCreateUtils().buildIndex(sqlBuilder, buildContext, schema, table, indexInfo);
        return Collections.singletonList(sqlBuilder.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop index ");
        sqlBuild.append(this.fmtTable(useDelimited, null, schema, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter index ");
        sqlBuild.append(this.fmtTable(useDelimited, null, schema, indexInfo.getName()));
        sqlBuild.append(" rename to ");
        sqlBuild.append(this.fmtTable(useDelimited, null, null, newIndexName));
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
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        if (StringUtils.isBlank(primaryInfo.getPrimaryKeyName()) || primaryInfo.getPrimaryKeyName().startsWith("SYS_")) {
            sqlBuild.append(" add primary key (");
        } else {
            sqlBuild.append(" add constraint \"" + "PK" + "\" primary key(");
        }
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(");");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        StringBuilder sqlBuild = this.buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" drop primary key;");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needAddColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().addAll(needAddColumns);
        ArrayList<String> indexScripts = new ArrayList();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);
        ArrayList<String> indexScripts = new ArrayList();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> createForeignKey(TriggerContext triggerContext, String catalog, String schema, String table, EForeignKey foreignKey) {
        StringBuilder sqlBuild = buildAlterTable(triggerContext, catalog, schema, table);
        boolean useDelimited = triggerContext.isUseDelimited();
        sqlBuild.append(" add constraint ");
        sqlBuild.append(fmtTable(useDelimited, null, null, foreignKey.getName())).append(" ");

        List<String> tableColumn = new ArrayList<>();
        List<String> referenceTableColumn = new ArrayList<>();
        foreignKey.getReferenceMapping().forEach((k, v) -> {
            tableColumn.add(k);
            referenceTableColumn.add(v);
        });
        sqlBuild.append("foreign key(");
        for (int i = 0; i < tableColumn.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, tableColumn.get(i)));
        }
        sqlBuild.append(") references ");

        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, foreignKey.getReferenceSchema(), foreignKey.getReferenceTable()));
        sqlBuild.append("(");
        for (int i = 0; i < referenceTableColumn.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, referenceTableColumn.get(i)));
        }
        sqlBuild.append(")");

        if (foreignKey.getUpdateRule() != null && foreignKey.getUpdateRule() != RdbForeignKeyRule.NoAction) {
            sqlBuild.append(" on update ").append(foreignKey.getUpdateRule().getTypeName());
        }
        if (foreignKey.getDeleteRule() != null && foreignKey.getDeleteRule() != RdbForeignKeyRule.NoAction) {
            sqlBuild.append(" on delete ").append(foreignKey.getUpdateRule().getTypeName());
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropForeignKey(TriggerContext triggerContext, String catalog, String schema, String table, EForeignKey eForeignKey) {
        StringBuilder sb = buildAlterTable(triggerContext, catalog, schema, table);
        sb.append(" drop constraint ").append(fmtTable(triggerContext.isUseDelimited(), null, null, eForeignKey.getName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey, String s3) {
        return null;
    }

    @Override
    public List<String> createConstraint(TriggerContext buildContext, String catalog, String schema, String table, EConstraint constraint) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        boolean useDelimited = buildContext.isUseDelimited();
        sqlBuild.append(" add constraint ");
        sqlBuild.append(getDialect().fmtName(useDelimited, constraint.getName())).append(" ");

        EConstraintType type = constraint.getType();

        if (type == EConstraintType.Check) {
            sqlBuild.append("check(");
            sqlBuild.append(constraint.getExpression());
            sqlBuild.append(")");
        } else if (type == EConstraintType.Unique) {
            sqlBuild.append("unique(");
            for (int i = 0; i < constraint.getColumnList().size(); i++) {
                if (i != 0) {
                    sqlBuild.append(",");
                }
                sqlBuild.append(getDialect().fmtName(useDelimited, constraint.getColumnList().get(i)));
            }
            sqlBuild.append(")");
        } else {
            throw new UnsupportedOperationException();
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropConstraint(TriggerContext buildContext, String catalog, String schema, String table, EConstraint constraint) {
        StringBuilder sb = buildAlterTable(buildContext, catalog, schema, table);
        sb.append(" drop constraint ").append(fmtTable(buildContext.isUseDelimited(), null, null, constraint.getName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }
}
