package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table;

import static com.clougence.adapter.mysql.MyUmiAttributeNames.SUB_ORDER;
import static com.clougence.adapter.mysql.MyUmiAttributeNames.SUB_PART;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.INDEX_TYPE;

import java.util.*;

import com.clougence.adapter.mysql.MySQLIndexType;
import com.clougence.adapter.mysql.MySQLMainVersion;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.*;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/8/18 10:07
*/
@Slf4j
public class MyEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new MyEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.MySQL; }

    public Dialect getDialect() { return MySqlDialect.INSTANCE; }

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

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("RENAME TABLE ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(" TO ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" COMMENT '" + getDialect().fmtComment(comment) + "';");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> tableAlterBeFore(TriggerContext triggerContext, String s, String s1, String s2, ETable eTable) {
        return null;
    }

    @Override
    public List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        int count = countETable(eTable);
        if (count == 0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        String comment = eTable.getComment();
        Map<String, String> tableAttr = eTable.getAttribute();
        StringBuilder sqlBuilder = this.buildAlterTable(buildContext, catalog, schema, table);
        if (comment != null) {
            sqlBuilder.append(" COMMENT '").append(comment).append("'");
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.ENGINE.getCodeKey())) {
            String value = MyUmiAttributeNames.ENGINE.getValue(tableAttr);
            sqlBuilder.append(" ENGINE = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append("DEFAULT");
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.CHARACTER_SET.getCodeKey())) {
            String value = MyUmiAttributeNames.CHARACTER_SET.getValue(tableAttr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(" CHARACTER SET = ").append(value);
            } else {
                sqlBuilder.append(" CONVERT TO CHARACTER SET DEFAULT");
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.COLLATION.getCodeKey())) {
            String value = MyUmiAttributeNames.COLLATION.getValue(tableAttr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(" COLLATE = ").append(value);
                count = appendComma(sqlBuilder, count);
            } else {
                count--;
            }
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.AUTO_INCREMENT_STAR.getCodeKey())) {
            String value = MyUmiAttributeNames.AUTO_INCREMENT_STAR.getValue(tableAttr);
            sqlBuilder.append(" AUTO_INCREMENT = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append(1);
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.ROW_FORMAT.getCodeKey())) {
            String value = MyUmiAttributeNames.ROW_FORMAT.getValue(tableAttr);
            sqlBuilder.append(" ROW_FORMAT = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append("DEFAULT");
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.COMPRESSION.getCodeKey())) {
            String value = MyUmiAttributeNames.COMPRESSION.getValue(tableAttr);
            sqlBuilder.append(" COMPRESSION = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append("''");
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.AVG_ROW_LENGTH.getCodeKey())) {
            String value = MyUmiAttributeNames.AVG_ROW_LENGTH.getValue(tableAttr);
            sqlBuilder.append(" AVG_ROW_LENGTH = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append("NULL");
            }
            count = appendComma(sqlBuilder, count);
        }

        if (tableAttr.containsKey(MyUmiAttributeNames.MAX_ROWS.getCodeKey())) {
            String value = MyUmiAttributeNames.MAX_ROWS.getValue(tableAttr);
            sqlBuilder.append(" MAX_ROWS = ");
            if (StringUtils.isNotBlank(value)) {
                sqlBuilder.append(value);
            } else {
                sqlBuilder.append(0);
            }
            count = appendComma(sqlBuilder, count);
        }

        // Meaningless
        if (StringUtils.isNotBlank(MyUmiAttributeNames.MIN_ROWS.getValue(tableAttr))) {
            sqlBuilder.append(" MIN_ROWS  = ").append(MyUmiAttributeNames.MIN_ROWS.getValue(tableAttr));
            count = appendComma(sqlBuilder, count);
        }

        result.add(sqlBuilder.toString());
        return result;
    }

    private int appendComma(StringBuilder sqlBuilder, int count) {
        count--;
        if (count > 0) {
            sqlBuilder.append(", \n");
        } else {
            sqlBuilder.append(";");
        }
        return count;
    }

    private int countETable(ETable eTable) {
        int count = 0;
        String comment = eTable.getComment();
        Map<String, String> tableAttr = eTable.getAttribute();
        if (comment != null) {
            count++;
        }
        count += tableAttr.size();
        return count;
    }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new MyCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }

    @Override
    public List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("drop table ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        MySQLTypes sqlTypes = MySQLTypes.valueOfCode(columnInfo.getDbType());
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        List<String> result = new ArrayList<>();

        sqlBuild.append(" ADD " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + MyTypeUtils.buildColumnType(mainVersion, columnInfo).toUpperCase());
        Map<String, String> columnAtr = columnInfo.getAttribute();

        // if (Boolean.TRUE.equals(columnInfo.getNullable())) {
        //     sqlBuild.append(" NULL ");
        // } else {
        //     sqlBuild.append(" NOT  NULL ");
        // }

        if (sqlTypes.isString() && columnAtr.containsKey(MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getCodeKey())) {
            String value = MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" CHARACTER SET ").append(value).append(" ");
            }
        }

        if (sqlTypes.isString() && columnAtr.containsKey(MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getCodeKey())) {
            String value = MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" COLLATE ").append(value).append(" ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(MyUmiAttributeNames.UNSIGNED.getCodeKey())) {
            if (MyUmiAttributeNames.UNSIGNED.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" UNSIGNED ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(MyUmiAttributeNames.ZEROFILL.getCodeKey())) {
            if (MyUmiAttributeNames.ZEROFILL.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" ZEROFILL ");
            }
        }

        if (columnInfo.getDefaultValue() != null) {
            String defaultValue = MyTypeUtils.buildDefault(sqlTypes, mainVersion, columnInfo, buildContext).toUpperCase();
            sqlBuild.append(" ").append(defaultValue).append(" ");
        }

        if (columnInfo.isAutoGenerate()) {
            sqlBuild.append(" AUTO_INCREMENT ");
        }

        if (sqlTypes.isDataOrTime() && columnAtr.containsKey(MyUmiAttributeNames.CURRENT_UPDATE_TYPE.getCodeKey())) {
            String value = MyUmiAttributeNames.CURRENT_UPDATE_TYPE.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" ON UPDATE ").append(value).append(" ");
            }
        }

        if (columnInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(columnInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        result.add(sqlBuild.toString());

        return result;
    }

    @Override
    public List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" DROP COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" CHANGE COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + getDialect().fmtName(useDelimited, newColumnName));
        sqlBuild.append(" " + MyTypeUtils.buildColumnType(columnInfo, buildContext).toUpperCase());
        if (columnInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(columnInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, EColumn columnInfo, EColumn newInfo, List<String> diffChange,
                                     ETable eTable) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);
        MySQLTypes sqlTypes = MySQLTypes.valueOfCode(newInfo.getDbType());
        MainVersion mainVersion = buildContext.getTarDsInfo().getMainVersion();
        List<String> result = new ArrayList<>();

        sqlBuild.append(" MODIFY COLUMN " + getDialect().fmtName(useDelimited, columnInfo.getName()));
        sqlBuild.append(" " + MyTypeUtils.buildColumnType(mainVersion, newInfo).toUpperCase());
        Map<String, String> columnAtr = newInfo.getAttribute();

        if (sqlTypes.isString() && columnAtr.containsKey(MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getCodeKey())) {
            String value = MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" CHARACTER SET ").append(value).append(" ");
            } else if (StringUtils.isNotBlank(MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(newInfo.getAttribute()))) {
                sqlBuild.append(" CHARACTER SET ").append(" DEFAULT ").append(" ");
            }
        }

        if (sqlTypes.isString() && columnAtr.containsKey(MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getCodeKey())) {
            String value = MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" COLLATE ").append(value).append(" ");
            } else if (StringUtils.isNotBlank(MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getValue(newInfo.getAttribute()))) {
                sqlBuild.append(" COLLATE ").append(" DEFAULT ").append(" ");
            }
        }

        // if (Boolean.TRUE.equals(newInfo.getNullable())) {
        //     sqlBuild.append(" NULL ");
        // } else {
        //     sqlBuild.append(" NOT  NULL ");
        // }

        if (sqlTypes.isNumber() && columnAtr.containsKey(MyUmiAttributeNames.UNSIGNED.getCodeKey())) {
            if (MyUmiAttributeNames.UNSIGNED.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" UNSIGNED ");
            }
        }

        if (sqlTypes.isNumber() && columnAtr.containsKey(MyUmiAttributeNames.ZEROFILL.getCodeKey())) {
            if (MyUmiAttributeNames.ZEROFILL.getValue(columnAtr).equalsIgnoreCase("true")) {
                sqlBuild.append(" ZEROFILL ");
            }
        }

        String defaultValue = MyTypeUtils.buildDefault(sqlTypes, mainVersion, newInfo, buildContext);
        if (StringUtils.isNotBlank(defaultValue)) {
            sqlBuild.append(" ");
            sqlBuild.append(defaultValue);
            sqlBuild.append(" ");
        }

        if (newInfo.isAutoGenerate()) {
            sqlBuild.append(" AUTO_INCREMENT ");
        }

        if (sqlTypes.isDataOrTime() && columnAtr.containsKey(MyUmiAttributeNames.CURRENT_UPDATE_TYPE.getCodeKey())) {
            String value = MyUmiAttributeNames.CURRENT_UPDATE_TYPE.getValue(columnAtr);
            if (StringUtils.isNotBlank(value)) {
                sqlBuild.append(" ON UPDATE ").append(value).append(" ");
            }
        }

        if (newInfo.getComment() != null) {
            sqlBuild.append(" COMMENT '").append(newInfo.getComment()).append("'");
        }
        sqlBuild.append(";");
        result.add(sqlBuild.toString());

        return result;
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
        Map<String, String> indexAttr = indexInfo.getAttribute();
        StringBuilder sqlBuild = new StringBuilder();

        String partJson = SUB_PART.getValue(indexAttr);
        String orderJson = SUB_ORDER.getValue(indexAttr);
        Map<String, String> subPart = StringUtils.isBlank(partJson) ? new HashMap<>() : JsonUtils.toMap(partJson);
        Map<String, String> subOrder = StringUtils.isBlank(orderJson) ? new HashMap<>() : JsonUtils.toMap(orderJson);

        String wayValue = MyUmiAttributeNames.INDEX_WAY.getValue(indexAttr);
        String indexName = indexInfo.getName();
        String typeValue = MyUmiAttributeNames.INDEX_TYPE.getValue(indexInfo.getAttribute());
        String append = "";

        if (StringUtils.isBlank(wayValue)) {
            throw new UnsupportedOperationException("index way must have a value");
        }

        sqlBuild.append("CREATE ");
        if (wayValue.equalsIgnoreCase(MySQLIndexType.Unique.name())) {
            sqlBuild.append("UNIQUE");
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.FullText.name())) {
            sqlBuild.append("FULLTEXT");
            if (mainVersion != null && MySQLMainVersion.MySQL_5_7.isLt(mainVersion)) {
                append = " WITH PARSER NGRAM ";
            }
        } else if (wayValue.equalsIgnoreCase(MySQLIndexType.SPATIAL.name())) {
            sqlBuild.append("SPATIAL");
        }
        sqlBuild.append(" INDEX ");
        sqlBuild.append(getDialect().fmtName(useDelimited, indexName));
        if (StringUtils.isNotBlank(typeValue)) {
            sqlBuild.append(" ");
            sqlBuild.append("USING ").append(typeValue.toUpperCase());
        }

        sqlBuild.append(" ON ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));

        sqlBuild.append("(");
        List<String> columnList = indexInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
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
        sqlBuild.append(")").append(append);

        if (StringUtils.isNotBlank(MyUmiAttributeNames.KEY_BLOCK_SIZE.getValue(indexAttr))) {
            String value = MyUmiAttributeNames.KEY_BLOCK_SIZE.getValue(indexAttr);
            sqlBuild.append(" ");
            sqlBuild.append("KEY_BLOCK_SIZE = ").append(Integer.valueOf(value));
        }

        if (indexInfo.getComment() != null) {
            sqlBuild.append(" ");
            sqlBuild.append("COMMENT '").append(indexInfo.getComment()).append("'");
        }
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo) {
        boolean useDelimited = buildContext.isUseDelimited();

        StringBuilder sqlBuild = new StringBuilder("DROP INDEX ");
        sqlBuild.append(getDialect().fmtName(useDelimited, indexInfo.getName()));
        sqlBuild.append(" ON ");
        sqlBuild.append(getDialect().fmtTableName(useDelimited, null, schema, table));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, String newIndexName) {
        boolean useDelimited = buildContext.isUseDelimited();
        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" RENAME INDEX ");
        sqlBuild.append(getDialect().fmtName(useDelimited, indexInfo.getName()));
        sqlBuild.append(" TO ");
        sqlBuild.append(getDialect().fmtName(useDelimited, newIndexName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }

    @Override
    public List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needAddColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().addAll(needAddColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        //        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, EIndex indexInfo, List<String> needRemoveColumns) {
        EIndex copy = indexInfo.clone();
        copy.getColumnList().removeAll(needRemoveColumns);

        ArrayList<String> indexScripts = new ArrayList<>();
        indexScripts.addAll(this.dropIndex(buildContext, catalog, schema, table, indexInfo));
        //        indexScripts.addAll(this.createIndex(buildContext, catalog, schema, table, copy));
        return indexScripts;
    }

    @Override
    public List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, EPrimaryKey primaryInfo) {
        boolean useDelimited = buildContext.isUseDelimited();
        Map<String, String> pkAttr = primaryInfo.getAttribute();

        String partJson = SUB_PART.getValue(pkAttr);
        String orderJson = SUB_ORDER.getValue(pkAttr);
        Map<String, String> subPartMap = StringUtils.isBlank(partJson) ? new HashMap<>() : JsonUtils.toMap(partJson);
        Map<String, String> subOrderMap = StringUtils.isBlank(orderJson) ? new HashMap<>() : JsonUtils.toMap(orderJson);

        StringBuilder sqlBuild = buildAlterTable(buildContext, catalog, schema, table);

        sqlBuild.append(" ADD PRIMARY KEY");

        if (StringUtils.isNotBlank(INDEX_TYPE.getValue(pkAttr))) {
            String indexType = INDEX_TYPE.getValue(pkAttr);
            sqlBuild.append(" ");
            sqlBuild.append("USING").append(" ").append(indexType.toUpperCase()).append(" ");
        }

        sqlBuild.append("(");
        List<String> columnList = primaryInfo.getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            String column = columnList.get(i);
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
        if (StringUtils.isNotBlank(MyUmiAttributeNames.KEY_BLOCK_SIZE.getValue(pkAttr))) {
            String value = MyUmiAttributeNames.KEY_BLOCK_SIZE.getValue(pkAttr);
            sqlBuild.append(" ");
            sqlBuild.append("KEY_BLOCK_SIZE = ").append(Integer.valueOf(value));
            sqlBuild.append(";");
        } else {
            sqlBuild.append(";");
        }
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
    public List<String> createForeignKey(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey) {
        return Collections.emptyList();
    }

    @Override
    public List<String> dropForeignKey(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey) {
        return Collections.emptyList();
    }

    @Override
    public List<String> foreignKeyRename(TriggerContext triggerContext, String s, String s1, String s2, EForeignKey eForeignKey, String s3) {
        return Collections.emptyList();
    }
}
