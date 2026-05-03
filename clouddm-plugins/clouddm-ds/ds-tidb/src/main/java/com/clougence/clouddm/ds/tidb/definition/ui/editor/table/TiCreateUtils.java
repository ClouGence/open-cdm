package com.clougence.clouddm.ds.tidb.definition.ui.editor.table;

import static com.clougence.adapter.tidb.TiDBAttributeNames.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.mysql.MySQLIndexType;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.clouddm.ds.tidb.dialect.TiDBDialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/8/10 11:39
*/
@Slf4j
public class TiCreateUtils extends AbstractSqlBuilder {

    public Dialect getDialect() { return TiDBDialect.INSTANCE; }

    public List<String> buildCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> tableAttr = eTable.getAttribute();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("CREATE ");
        String temporary = TEMPORARY.getValue(tableAttr);
        if (StringUtils.isNotBlank(temporary) && temporary.equalsIgnoreCase("true")) {
            sqlBuild.append("TEMPORARY ");
        }
        sqlBuild.append("TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, eTable.getSchema(), eTable.getName()));
        sqlBuild.append("(\n");

        // columns
        List<EColumn> columnList = eTable.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            if (i != 0) {
                sqlBuild.append(",\n");
            }
            EColumn eColumn = columnList.get(i).clone();
            buildColumn(sqlBuild, buildContext, eColumn, eTable.getPrimaryKey());
        }
        // pk
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && !primaryKey.getColumnList().isEmpty()) {
            sqlBuild.append(",\n");
            buildPrimaryKey(sqlBuild, buildContext, primaryKey.clone());
        }
        // idx
        List<EIndex> indices = eTable.getIndices();
        if (indices != null && !indices.isEmpty()) {
            for (EIndex index : indices) {
                sqlBuild.append(",\n");
                buildIndex(sqlBuild, buildContext, index.clone(), eTable);
            }
        }
        //
        // eTable.getForeignKeys();
        // private List<EForeignKey> foreignKeys = new ArrayList<>();
        //
        sqlBuild.append("\n )");
        buildTableCreateOption(sqlBuild, eTable, tableAttr);
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    private boolean isAllowIndexType(String indexType) {
        return "BTREE".equalsIgnoreCase(indexType) || "HASH".equalsIgnoreCase(indexType);
    }

    private void buildTableCreateOption(StringBuilder sqlBuild, ETable eTable, Map<String, String> tableAttr) {
        if (StringUtils.isNotBlank(eTable.getComment())) {
            String comment = eTable.getComment();
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("COMMENT = '").append(comment).append("'");
        }

        if (StringUtils.isNotBlank(ENGINE.getValue(tableAttr))) {
            String engine = ENGINE.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("ENGINE = ").append(engine);
            if (engine.equalsIgnoreCase("MyISAM") && StringUtils.isNotBlank(KEY_BLOCK_SIZE.getValue(tableAttr))) {
                String keyBlockSize = KEY_BLOCK_SIZE.getValue(tableAttr);
                sqlBuild.append(" ");
                sqlBuild.append("KEY_BLOCK_SIZE = ").append(keyBlockSize);
            }
        }

        if (StringUtils.isNotBlank(CHARACTER_SET.getValue(tableAttr))) {
            String charSet = CHARACTER_SET.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("DEFAULT CHARACTER SET = '").append(charSet).append("'");
            if (StringUtils.isNotBlank(COLLATION.getValue(tableAttr))) {
                String coll = COLLATION.getValue(tableAttr);
                sqlBuild.append(" ");
                sqlBuild.append("DEFAULT COLLATE = '").append(coll).append("'");
            }
        }

        if (StringUtils.isNotBlank(AUTO_INCREMENT_STAR.getValue(tableAttr))) {
            String autoIncrementStar = AUTO_INCREMENT_STAR.getValue(tableAttr);
            sqlBuild.append("\n");
            sqlBuild.append("  ");
            sqlBuild.append("AUTO_INCREMENT = ").append(autoIncrementStar);
        }

        //if (StringUtils.isNotBlank(ROW_FORMAT.getValue(tableAttr))) {
        //    String rowFormat = ROW_FORMAT.getValue(tableAttr);
        //    sqlBuild.append("\n");
        //    sqlBuild.append("  ");
        //    sqlBuild.append("ROW_FORMAT = ").append(rowFormat);
        //}

        //if (StringUtils.isNotBlank(COMPRESSION.getValue(tableAttr))) {
        //    String compression = COMPRESSION.getValue(tableAttr);
        //    sqlBuild.append("\n");
        //    sqlBuild.append("  ");
        //    sqlBuild.append("COMPRESSION = '").append(compression).append("'");
        //}

        //if (StringUtils.isNotBlank(AVG_ROW_LENGTH.getValue(tableAttr))) {
        //    String avgRowLength = AVG_ROW_LENGTH.getValue(tableAttr);
        //    sqlBuild.append("\n");
        //    sqlBuild.append("  ");
        //    sqlBuild.append("AVG_ROW_LENGTH = ").append(avgRowLength);
        //}

        //if (StringUtils.isNotBlank(MIN_ROWS.getValue(tableAttr))) {
        //    String minRows = MIN_ROWS.getValue(tableAttr);
        //    sqlBuild.append("\n");
        //    sqlBuild.append("  ");
        //    sqlBuild.append("MIN_ROWS = ").append(minRows);
        //}

        //if (StringUtils.isNotBlank(MAX_ROWS.getValue(tableAttr))) {
        //    String maxRows = MAX_ROWS.getValue(tableAttr);
        //    sqlBuild.append("\n");
        //    sqlBuild.append("  ");
        //    sqlBuild.append("MAX_ROWS = ").append(maxRows);
        //}

    }

    private void buildColumn(StringBuilder sqlBuild, TriggerContext buildContext, EColumn eColumn, EPrimaryKey ePrimaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(eColumn.getDbType());
        Map<String, String> columnAttr = eColumn.getAttribute();

        sqlBuild.append("  ");
        sqlBuild.append(getDialect().fmtName(useDelimited, eColumn.getName()));
        String columnType = TiTypeUtils.buildColumnType(mainVersion, eColumn);
        sqlBuild.append("  ");
        sqlBuild.append(columnType.toUpperCase());

        if (sqlTypes.isString() && StringUtils.isNotBlank(DEFAULT_CHARACTER_SET_NAME.getValue(columnAttr))) {
            String collation = DEFAULT_CHARACTER_SET_NAME.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("CHARACTER SET ").append(collation);
        }

        if (sqlTypes.isString() && StringUtils.isNotBlank(DEFAULT_COLLATION_NAME.getValue(columnAttr))) {
            String collation = DEFAULT_COLLATION_NAME.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("COLLATE ").append(collation);
        }

        if (ePrimaryKey != null && ePrimaryKey.getColumnList().contains(eColumn.getName())) {
            sqlBuild.append(" ");
            sqlBuild.append("NOT NULL");
        } else if (!Boolean.TRUE.equals(eColumn.getNullable())) {
            sqlBuild.append(" ");
            sqlBuild.append("NOT NULL");
        } else {
            sqlBuild.append(" ");
            sqlBuild.append("NULL");
        }

        if (sqlTypes.isNumber() && StringUtils.isNotBlank(UNSIGNED.getValue(columnAttr))) {
            String unsigned = UNSIGNED.getValue(columnAttr);
            if (unsigned.equalsIgnoreCase("true")) {
                sqlBuild.append(" ");
                sqlBuild.append("UNSIGNED");
            }
        }

        // if (StringUtils.isNotBlank(ZEROFILL.getValue(columnAttr))) {
        //     String zerofill = ZEROFILL.getValue(columnAttr);
        //     if (zerofill.equalsIgnoreCase("true")) {
        //         sqlBuild.append(" ");
        //         sqlBuild.append("ZEROFILL");
        //     }
        // }

        String defaultValue = TiTypeUtils.buildDefault(sqlTypes, mainVersion, eColumn, buildContext);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuild.append(" ");
            sqlBuild.append(defaultValue);
            sqlBuild.append(" ");
        }

        //        if (!typeUseDefaultSet.contains(eColumn.getDbType().toUpperCase())) {
        //            String defaultValue = eColumn.getDefaultValue();
        //            sqlBuild.append(" ");
        //            if (defaultValue == null) {
        //                sqlBuild.append("DEFAULT ").append("NULL");
        //            } else {
        //                sqlBuild.append("DEFAULT '").append(eColumn.getDefaultValue()).append("'");
        //            }
        //        }

        if (sqlTypes.isDataOrTime() && StringUtils.isNotBlank(CURRENT_UPDATE_TYPE.getValue(columnAttr))) {
            String currentUpdateType = CURRENT_UPDATE_TYPE.getValue(columnAttr);
            sqlBuild.append(" ");
            sqlBuild.append("ON UPDATE ").append(currentUpdateType);
        }

        if (sqlTypes.isNumber() && eColumn.isAutoGenerate()) {
            sqlBuild.append(" ");
            sqlBuild.append("AUTO_INCREMENT");
        }

        if (StringUtils.isNotBlank(eColumn.getComment())) {
            String comment = getDialect().fmtComment(eColumn.getComment());
            sqlBuild.append(" COMMENT '").append(comment).append("'");
        }

    }

    private void buildPrimaryKey(StringBuilder sqlBuild, TriggerContext buildContext, EPrimaryKey primaryKey) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> pkAttr = primaryKey.getAttribute();

        String partJson = SUB_PART.getValue(pkAttr);
        String orderJson = SUB_ORDER.getValue(pkAttr);
        Map<String, String> subPartMap = StringUtils.isBlank(partJson) ? new HashMap<>() : JsonUtils.toMap(partJson);
        Map<String, String> subOrderMap = StringUtils.isBlank(orderJson) ? new HashMap<>() : JsonUtils.toMap(orderJson);

        sqlBuild.append("  PRIMARY KEY");

        String indexType = INDEX_TYPE.getValue(pkAttr);
        if (StringUtils.isNotBlank(indexType) && isAllowIndexType(indexType)) {
            sqlBuild.append(" ");
            sqlBuild.append("USING").append(" ").append(indexType.toUpperCase()).append(" ");
        }
        sqlBuild.append("(");
        List<String> pkColumns = primaryKey.getColumnList();
        for (int i = 0; i < pkColumns.size(); i++) {
            String column = pkColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));

            if (CollectionUtils.isNotEmpty(subPartMap) && subPartMap.containsKey(column)) {
                String subPart = subPartMap.get(column);
                if (StringUtils.isNotBlank(subPart)) {
                    sqlBuild.append("(").append(Integer.valueOf(subPart)).append(")");
                }
            }

            if (CollectionUtils.isNotEmpty(subOrderMap) && subOrderMap.containsKey(column)) {
                String subOrder = subOrderMap.get(column);
                if (StringUtils.isNotBlank(subOrder)) {
                    sqlBuild.append(" ");
                    if (subOrder.equalsIgnoreCase("ASC")) {
                        sqlBuild.append("ASC");
                    } else if (subOrder.equalsIgnoreCase("DESC")) {
                        sqlBuild.append("DESC");
                    }
                }
            }

        }
        sqlBuild.append(")");

        if (StringUtils.isNotBlank(KEY_BLOCK_SIZE.getValue(pkAttr))) {
            String value = KEY_BLOCK_SIZE.getValue(pkAttr);
            sqlBuild.append(" ");
            sqlBuild.append("KEY_BLOCK_SIZE = ").append(Integer.valueOf(value));
        }
    }

    @SneakyThrows
    private void buildIndex(StringBuilder sqlBuild, TriggerContext buildContext, EIndex eIndex, ETable eTable) {
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> indexAttr = eIndex.getAttribute();

        String subPartJson = SUB_PART.getValue(indexAttr);
        Map<String, String> subPart = new HashMap<>();
        if (StringUtils.isNotEmpty(subPartJson)) {
            subPart = JsonUtils.toMap(subPartJson);
        }

        String subOrderJson = SUB_ORDER.getValue(indexAttr);
        Map<String, String> subOrder = new HashMap<>();
        if (StringUtils.isNotEmpty(subOrderJson)) {
            subOrder = JsonUtils.toMap(subOrderJson);
        }

        String typeValue = INDEX_TYPE.getValue(indexAttr);
        String wayValue = INDEX_WAY.getValue(indexAttr);
        String indexName = eIndex.getName();
        String append = "";

        if (StringUtils.isBlank(wayValue)) {
            if (eIndex.getType() != null) {
                if (eIndex.getType().getTypeName().equals(MySQLIndexType.Normal.name())) {
                    wayValue = MySQLIndexType.Normal.name();
                } else if (eIndex.getType().getTypeName().equals(MySQLIndexType.Unique.name())) {
                    wayValue = MySQLIndexType.Unique.name();
                } else if (eIndex.getType().getTypeName().equals(MySQLIndexType.FullText.name())) {
                    wayValue = MySQLIndexType.FullText.name();
                } else if (eIndex.getType().getTypeName().equals(MySQLIndexType.SPATIAL.name())) {
                    wayValue = MySQLIndexType.SPATIAL.name();
                } else {
                    throw new UnsupportedOperationException("index way must have a value");
                }
            } else {
                throw new UnsupportedOperationException("index way must have a value");
            }
        }

        if (wayValue.equalsIgnoreCase(MySQLIndexType.Normal.name())) {
            sqlBuild.append("  INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            }
            //if (StringUtils.isNotBlank(typeValue) && isAllowIndexType(typeValue)) {
            //    sqlBuild.append(" ");
            //    sqlBuild.append("USING ").append(typeValue.toUpperCase());
            //}
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.Unique.name())) {
            sqlBuild.append(" UNIQUE INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            }
            //if (StringUtils.isNotBlank(typeValue) && isAllowIndexType(typeValue)) {
            //    sqlBuild.append(" ");
            //    sqlBuild.append("USING ").append(typeValue.toUpperCase());
            //}
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.FullText.name())) {
            sqlBuild.append(" FULLTEXT INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
                //                if (mainVersion != null && mainVersion.isGe(MySQLMainVersion.MySQL_5_7)) {
                //                    append = " WITH PARSER NGRAM ";
                //                }
            }
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.SPATIAL.name())) {
            sqlBuild.append(" SPATIAL INDEX ");
            if (StringUtils.isNotBlank(indexName)) {
                sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
            }
        }

        List<String> idxColumns = eIndex.getColumnList();
        sqlBuild.append(" ");
        sqlBuild.append("(");
        for (int i = 0; i < idxColumns.size(); i++) {
            String column = idxColumns.get(i);
            if (i > 0) {
                sqlBuild.append(", ");
            }
            sqlBuild.append(getDialect().fmtName(useDelimited, column));
            if (CollectionUtils.isNotEmpty(subPart) && subPart.containsKey(column)) {
                String value = subPart.get(column);
                if (StringUtils.isNotBlank(value)) {
                    sqlBuild.append("(").append(Integer.valueOf(value)).append(")");
                }
            }

            if (CollectionUtils.isNotEmpty(subOrder) && subPart.containsKey(column)) {
                String value = subOrder.get(column);
                if (StringUtils.isNotBlank(value)) {
                    sqlBuild.append(" ");
                    if (value.equalsIgnoreCase("ASC")) {
                        sqlBuild.append("ASC");
                    } else if (value.equalsIgnoreCase("DESC")) {
                        sqlBuild.append("DESC");
                    }
                }
            }

        }
        sqlBuild.append(")");

        if (StringUtils.isNotBlank(KEY_BLOCK_SIZE.getValue(indexAttr))) {
            String value = KEY_BLOCK_SIZE.getValue(indexAttr);
            sqlBuild.append(" ");
            sqlBuild.append("KEY_BLOCK_SIZE = ").append(Integer.valueOf(value));
        }

        if (StringUtils.isNotBlank(eIndex.getComment())) {
            sqlBuild.append(" ");
            sqlBuild.append("COMMENT '").append(eIndex.getComment()).append("'");
        }

        sqlBuild.append(append);
    }
}
