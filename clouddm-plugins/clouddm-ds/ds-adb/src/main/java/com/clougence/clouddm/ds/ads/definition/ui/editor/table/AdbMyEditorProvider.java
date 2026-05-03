package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class AdbMyEditorProvider extends MyEditorProvider {

    public static final SqlBuilder INSTANCE = new AdbMyEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.AdbForMySQL; }

    public Dialect getDialect() { return AdbMySqlDialect.INSTANCE; }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new AdbMyCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> indexAttr = indexInfo.getAttribute();
        StringBuilder sqlBuild = new StringBuilder();

        String wayValue = AdsMyUmiAttributeNames.INDEX_WAY.getValue(indexAttr);
        String indexName = indexInfo.getName();
        String append = "";

        if (StringUtils.isBlank(wayValue)) {
            throw new UnsupportedOperationException("index way must have a value");
        }

        sqlBuild.append("ALTER TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(" ADD ");

        if (wayValue.equalsIgnoreCase("FullText")) {
            sqlBuild.append("FULLTEXT");
        } else if (wayValue.equalsIgnoreCase("ANN")) {
            sqlBuild.append("ANN");
        }

        sqlBuild.append(" INDEX\n  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
        }
        sqlBuild.append(")").append(append);

        if (wayValue.equalsIgnoreCase("ANN")) {
            String annAlgorithm = AdsMyUmiAttributeNames.ANN_ALGORITHM.getValue(indexAttr);
            String annDisFunction = AdsMyUmiAttributeNames.ANN_DISFUNCTION.getValue(indexAttr);
            if (StringUtils.isNotBlank(annAlgorithm)) {
                sqlBuild.append(" algorithm=").append(annAlgorithm);
            }
            if (StringUtils.isNotBlank(annAlgorithm)) {
                sqlBuild.append(" distancemeasure=").append(annDisFunction);
            }
        }
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        String wayValue = AdsMyUmiAttributeNames.INDEX_WAY.getValue(indexInfo.getAttribute());

        StringBuilder sqlBuild = new StringBuilder("ALTER TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        if (wayValue.equalsIgnoreCase("FullText")) {
            sqlBuild.append("DROP FULLTEXT INDEX\n  ");
        } else if (wayValue.equalsIgnoreCase("ANN")) {
            sqlBuild.append("DROP ANN INDEX\n  ");
        }

        sqlBuild.append(getDialect().fmtName(useDelimited, indexInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }
}
