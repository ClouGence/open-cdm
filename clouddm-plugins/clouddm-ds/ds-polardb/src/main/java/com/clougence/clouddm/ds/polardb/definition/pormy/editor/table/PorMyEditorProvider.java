package com.clougence.clouddm.ds.polardb.definition.pormy.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.polar.pormy.PolarDBMyIndexType;
import com.clougence.adapter.polar.pormy.PolarDBMyMainVersion;
import com.clougence.clouddm.ds.polardb.dialect.pormy.PolarDBMyDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class PorMyEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new PorMyEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.PolarDBMySQL; }

    public Dialect getDialect() { return PolarDBMyDialect.INSTANCE; }

    protected StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("alter table ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        return sqlBuild;
    }

    @Override
    public List<String> tableRename(TriggerContext context, String catalog, String schema, String table, String newName) {
        String sqlBuild = "rename table " + fmtTable(context.isUseDelimited(), null, schema, table) + " to " + fmtTable(context.isUseDelimited(), null, schema, newName) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" comment '").append(getDialect().fmtComment(comment)).append("';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        return null;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new PorMyCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext context, String catalog, String schema, String table, ETable eTable) {
        String sqlBuild = "drop table if exists " + fmtTable(context.isUseDelimited(), null, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> tableTruncate(TriggerContext context, String catalog, String schema, String table, ETable eTable) {
        String sqlBuild = "truncate table " + fmtTable(context.isUseDelimited(), null, schema, table) + ";";
        return Collections.singletonList(sqlBuild);
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" add " + fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + PorMyTypeUtils.buildColumnType(columnInfo, buildContext));
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" drop column " + fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" change column " + fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + fmtName(useDelimited, newColumnName));
        sqlBuild.append(" " + PorMyTypeUtils.buildColumnType(columnInfo, buildContext));
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" modify column " + fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + PorMyTypeUtils.buildColumnType(newInfo, buildContext));
        if (StringUtils.isNotBlank(newInfo.getComment())) {
            String comment = getDialect().fmtComment(newInfo.getComment());
            sqlBuild.append(" comment '").append(comment).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String comment, ETable eTable) {
        EColumn newInfo = columnInfo.clone();
        newInfo.setComment(comment);
        List<String> diffChange = Collections.singletonList("comment");
        return columnChange(buildContext, catalog, schema, table, columnInfo, newInfo, diffChange, eTable);
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        boolean useDelimited = buildContext.isUseDelimited();

        String typeValue = RdbAttributeNames.INDEX_TYPE.getValue(indexInfo.getAttribute());
        String append = "";

        StringBuilder sqlBuild = new StringBuilder();
        if (indexInfo.getType() == EIndexType.Unique) {
            sqlBuild.append("create unique index " + fmtIndex(useDelimited, null, indexInfo.getName()));
        } else if (StringUtils.equalsIgnoreCase(PolarDBMyIndexType.FullText.name(), typeValue)) {
            sqlBuild.append("create fulltext index " + fmtIndex(useDelimited, null, indexInfo.getName()));
            if (mainVersion != null && mainVersion.isGe(PolarDBMyMainVersion.PolarDBMy_5_7)) {
                append = " with parser ngram";
            }
        } else {
            sqlBuild.append("create index " + fmtIndex(useDelimited, null, indexInfo.getName()));
        }
        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(fmtName(useDelimited, column));
        }
        sqlBuild.append(")").append(append).append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder("drop index ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexInfo.getName()));
        sqlBuild.append(" on ");
        sqlBuild.append(fmtTable(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" rename index ");
        sqlBuild.append(fmtIndex(useDelimited, null, indexInfo.getName()));
        sqlBuild.append(" to ");
        sqlBuild.append(fmtIndex(useDelimited, null, newIndexName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" add primary key");
        sqlBuild.append("(");
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
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" drop primary key;");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needAddColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        EPrimaryKey copy = primaryInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropPrimaryKey(buildContext, catalog, schema, table, primaryInfo));
        indexScripts.addAll(this.createPrimaryKey(buildContext, catalog, schema, table, copy));
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
