package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/9/14 10:50
*/
@Slf4j
public class ChEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new ChEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.ClickHouse; }

    @Override
    public Dialect getDialect() { return ClickHouseDialect.INSTANCE; }

    private StringBuilder buildAlterTable(TriggerContext buildContext, String catalog, String schema, String table) {
        StringBuilder sqlBuild = new StringBuilder();
        boolean useDelimited = buildContext.isUseDelimited();
        String fmtTable = fmtTable(useDelimited, catalog, schema, table);

        sqlBuild.append("ALTER TABLE ");
        sqlBuild.append(fmtTable);
        return sqlBuild;
    }

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        StringBuilder sqlBuild = new StringBuilder();
        boolean useDelimited = buildContext.isUseDelimited();

        String fmtTableOld = fmtTable(useDelimited, catalog, schema, table);
        String fmtTableNew = fmtTable(useDelimited, catalog, schema, newName);

        sqlBuild.append("RENAME TABLE ");
        sqlBuild.append(fmtTableOld);
        sqlBuild.append(" TO ").append(fmtTableNew);

        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new ChCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" ADD COLUMN ").append(fmtName(buildContext.isUseDelimited(), columnInfo.getName()));
        String columnType = ChTypeUtils.buildColumnType(columnInfo, buildContext);
        sqlBuild.append("  ");
        sqlBuild.append(columnType);
        if (StringUtils.isNotBlank(columnInfo.getComment())) {
            String comment = getDialect().fmtComment(columnInfo.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" DROP COLUMN ").append(fmtName(buildContext.isUseDelimited(), columnInfo.getName()));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        sqlBuild.append(" RENAME COLUMN ").append(fmtName(buildContext.isUseDelimited(), columnInfo.getName()));
        sqlBuild.append(" TO ");
        sqlBuild.append(fmtName(buildContext.isUseDelimited(), newColumnName));
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        EColumn oldCInfo = columnInfo.clone();
        EColumn newCInfo = newInfo.clone();
        oldCInfo.setComment("");
        newCInfo.setComment("");
        List<String> sqlList = new ArrayList<>();
        if (oldCInfo.testChanged(newCInfo)) {
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" MODIFY COLUMN ").append(fmtName(buildContext.isUseDelimited(), columnInfo.getName()));
            String columnType = ChTypeUtils.buildColumnType(newInfo, buildContext);
            sqlBuild.append("  ");
            sqlBuild.append(columnType);
            sqlBuild.append(";");
            sqlList.add(sqlBuild.toString());
        }
        if (newInfo.getComment() != null) {
            StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
            sqlBuild.append(" COMMENT COLUMN ").append(fmtName(buildContext.isUseDelimited(), columnInfo.getName()));
            sqlBuild.append("  ");
            sqlBuild.append("'").append(newInfo.getComment()).append("'");
            sqlBuild.append(";");
            sqlList.add(sqlBuild.toString());
        }

        return sqlList;
    }
}
