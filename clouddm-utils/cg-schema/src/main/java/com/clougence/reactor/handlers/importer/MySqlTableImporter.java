package com.clougence.reactor.handlers.importer;

import static com.clougence.adapter.mysql.MyUmiAttributeNames.*;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MYSQL_IS_ALI_SQL;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MYSQL_ZERO_TIME_STRATEGY;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.mysql.MySQLIndexType;
import com.clougence.adapter.mysql.MySQLMainVersion;
import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.attributes.TimeDefaultStrategyEnum;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
public class MySqlTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // timestamp
        processTimestampStrategy(tableEditor, triggerContext);

        // process maxRowSize
        processMaxRowSize(tableEditor);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // GEMO has index
        super.processIdxWithGemo(tableEditor);

        // process maxKeySize
        processMaxKeySize(tableEditor, triggerContext, dstMainVersion);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void processTimestampStrategy(TableEditor tableEditor, TriggerContext triggerContext) {
        // find all timestamp column
        List<String> timestampColumns = tableEditor.getSource().getColumnList().stream().filter(eColumn -> {
            MySQLTypes sqlTypes = MySQLTypes.valueOfCode(eColumn.getDbType());
            return sqlTypes == MySQLTypes.TIMESTAMP || sqlTypes == MySQLTypes.DATETIME;
        }).map(EColumn::getName).collect(Collectors.toList());

        TimeDefaultStrategyEnum strategy = TimeDefaultStrategyEnum.valueOfCode(triggerContext.getAttribute(MYSQL_ZERO_TIME_STRATEGY));
        for (int i = 1; i < timestampColumns.size(); i++) {
            String columnName = timestampColumns.get(i);
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            String defaultValue = columnEditor.getSource().getDefaultValue();

            fillPrecision(columnEditor);

            if (StringUtils.startsWith(defaultValue, "0000-00-00 00:00:00")) {
                if (strategy == TimeDefaultStrategyEnum.ZERO) {
                    columnEditor.setNullable(false);
                    fillDefaultZero(columnEditor, false);
                } else if (strategy == TimeDefaultStrategyEnum.CURRENT) {
                    columnEditor.setNullable(false);
                    fillDefaultCurrent(columnEditor);
                } else if (strategy == TimeDefaultStrategyEnum.IS_NULL) {
                    columnEditor.setNullable(true);
                    columnEditor.setDefault("", false);
                } else if (strategy == TimeDefaultStrategyEnum.NOTHING) {
                    //
                }
            } else if (StringUtils.isBlank(defaultValue) && columnEditor.getSource().getNullable() != null && !columnEditor.getSource().getNullable()) {
                if (strategy == TimeDefaultStrategyEnum.ZERO) {
                    columnEditor.setNullable(false);
                    fillDefaultZero(columnEditor, false);
                } else {
                    columnEditor.setNullable(false);
                    fillDefaultCurrent(columnEditor);
                }
            }
        }
    }

    protected void fillPrecision(ColumnEditor columnEditor) {
        Integer precision = columnEditor.getSource().getDatetimePrecision();
        if (precision != null && precision >= 0) {
            int supportedPrecision = (precision > 6 ? 6 : precision);
            columnEditor.setDateTimePrecision(supportedPrecision);
        }
    }

    protected void fillDefaultCurrent(ColumnEditor columnEditor) {
        Integer precision = columnEditor.getSource().getDatetimePrecision();
        if (precision != null && precision >= 0) {
            int supportedPrecision = (precision > 6 ? 6 : precision);
            columnEditor.setDefault("current_timestamp(" + supportedPrecision + ")", true);
        } else {
            columnEditor.setDefault("current_timestamp", true);
        }
    }

    protected void fillDefaultZero(ColumnEditor columnEditor, boolean nullable) {
        Integer precision = columnEditor.getSource().getDatetimePrecision();
        if (precision != null && precision >= 0) {
            //if have precision, mysql not support zero time.
            if (nullable) {
                columnEditor.setDefault("", false);
            } else {
                int supportedPrecision = (precision > 6 ? 6 : precision);
                columnEditor.setDefault("current_timestamp(" + supportedPrecision + ")", true);
            }
        } else {
            columnEditor.setDefault("0000-00-00 00:00:00", false);
        }
    }

    private void processMaxRowSize(TableEditor tableEditor) {
        long rowSize = evalRowSize(tableEditor, tableEditor.getColumnNames());
        if (rowSize >= 65535) {
            processColumns(tableEditor, rowSize - 65535);
        }
    }

    static class MaxAndPartKeySize {

        public int     defaultMaxKeySize;
        public int     partKeySize;
        public boolean supportedVersion;

        MaxAndPartKeySize(int defaultMaxKeySize, int partKeySize, boolean supportedVersion){
            this.defaultMaxKeySize = defaultMaxKeySize;
            this.partKeySize = partKeySize;
            this.supportedVersion = supportedVersion;
        }
    }

    private MaxAndPartKeySize getMaxKeySize(TriggerContext triggerContext, MainVersion dstMainVersion) {
        if (dstMainVersion == null) {
            return new MaxAndPartKeySize(767, 191, false);
        }

        boolean aliSql = isAliSQL(triggerContext);
        // - mysql 5.6, aliyun RDS 5.6/5.7 Specified key was too long; max key length is 767 bytes
        // - mysql 5.7/8.0, aliyun RDS 8.0 Specified key was too long; max key length is 3072 bytes

        if (aliSql) {
            if (dstMainVersion.isEq(MySQLMainVersion.MySQL_5_6) || dstMainVersion.isEq(MySQLMainVersion.MySQL_5_7) || dstMainVersion.isEq(MySQLMainVersion.MariaDB_10_0)
                || dstMainVersion.isEq(MySQLMainVersion.MariaDB_10_1)) {
                return new MaxAndPartKeySize(767, 191, true);
            } else if (dstMainVersion.isEq(MySQLMainVersion.MySQL_8_0) || dstMainVersion.isGe(MySQLMainVersion.MariaDB_10_2)) {
                return new MaxAndPartKeySize(3072, 768, true);
            }
        } else {
            if (dstMainVersion.isEq(MySQLMainVersion.MySQL_5_6) || dstMainVersion.isEq(MySQLMainVersion.MariaDB_10_0) || dstMainVersion.isEq(MySQLMainVersion.MariaDB_10_1)) {
                return new MaxAndPartKeySize(767, 191, true);
            } else if (dstMainVersion.isEq(MySQLMainVersion.MySQL_5_7) || dstMainVersion.isEq(MySQLMainVersion.MySQL_8_0) || dstMainVersion.isGe(MySQLMainVersion.MariaDB_10_2)) {
                return new MaxAndPartKeySize(3072, 768, true);
            }
        }

        return new MaxAndPartKeySize(767, 191, false);
    }

    private void processMaxKeySize(TableEditor tableEditor, TriggerContext triggerContext, MainVersion dstMainVersion) {
        MaxAndPartKeySize maxKeySize = getMaxKeySize(triggerContext, dstMainVersion);
        if (!maxKeySize.supportedVersion) {
            processIdxWithText(tableEditor, maxKeySize.defaultMaxKeySize);
        } else {
            processMaxKeySize(tableEditor, maxKeySize.defaultMaxKeySize, maxKeySize.partKeySize);
            processIdxWithText(tableEditor, maxKeySize.defaultMaxKeySize);
        }
    }

    private void processIdxWithText(TableEditor tableEditor, int defaultMaxKeySize) {
        List<String> indexNames = tableEditor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            String idxName = indexEditor.getSource().getName();
            List<String> columnList = indexEditor.getSource().getColumnList();

            boolean hasLob = false;
            boolean allString = true;
            int lobMaxKeySize = defaultMaxKeySize;
            final Map<String, Integer> stringTypeSubPart = new HashMap<>();
            String jStr = SUB_PART.getValue(indexEditor.getSource().getAttribute());
            if (StringUtils.isNotBlank(jStr)) {
                try {
                    Map<String, Integer> temp = new ObjectMapper().readValue(jStr, new TypeReference<Map<String, Integer>>() {
                    });
                    stringTypeSubPart.putAll(temp);
                } catch (Exception e) {
                    log.warn("Deserialize sub part failed,but ignore.");
                }
            }

            for (String column : columnList) {
                ColumnEditor editorColumn = tableEditor.getColumn(column);
                MySQLTypes type = MySQLTypes.valueOfCode(editorColumn.getSource().getDbType());

                Integer subPartSize = null;
                if (isString(type)) {
                    subPartSize = stringTypeSubPart.putIfAbsent(column, null);
                }

                //if is prefix index, only minus the specified length, or use type default size
                if (subPartSize != null) {
                    lobMaxKeySize -= subPartSize;
                } else {
                    lobMaxKeySize -= typeOfSize(type, editorColumn.getSource());
                }

                allString = allString && isString(type);
                hasLob = hasLob || isLob(type);
            }

            // only have TEXT or LOB need to continue
            if (!hasLob) {
                continue;
            }

            // mysql allow build index for TEXT/VARCHAR with prefix
            if (allString && !stringTypeSubPart.isEmpty()) {
                continue;
            }

            indexEditor.delete();
            IndexEditor editor = tableEditor.addIndexEditor(idxName, EIndexType.Normal, columnList);
            if (allString) {
                // all column is text and without prefix => normal fulltext index
                INDEX_TYPE.setValue(editor.getSource().getAttribute(), MySQLIndexType.FullText.name());
            } else {
                // blob / number / time / geo not support fulltext index => prefix (unique / normal) index
                int maxCollationBytes = 4;
                int avgSize = lobMaxKeySize / (stringTypeSubPart.size() * maxCollationBytes);
                stringTypeSubPart.forEach((k, v) -> stringTypeSubPart.put(k, avgSize));
                SUB_PART.setValue(editor.getSource().getAttribute(), JSON.toString(stringTypeSubPart));
            }
        }
    }

    private boolean isString(MySQLTypes type) {
        return !type.isGeometry() && !type.isNumber() && !type.isDataOrTime() && !type.isBinary();
    }

    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return MySQLTypes.valueOfCode(columnType);
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        MySQLTypes sqlTypes = MySQLTypes.valueOfCode(domain.getDbType());

        // CHAR / VARCHAR default has value but length out of size
        processDefaultValueLengthOutOfColumnLength(sqlTypes, columnEditor);

        processColumnTypeWithDisplayWidth(srcType, sqlTypes, columnEditor);

        if (MySQLMainVersion.MySQL_5_6.isEq(dstMainVersion)) {

            // current_date and current_time process
            processTimesFuncHaveDefaultOrUpdate(sqlTypes, columnEditor);
            // current_date and current_time process
            processNumberDefaultHasFuncIgnore(sqlTypes, columnEditor);

        } else if (MySQLMainVersion.MySQL_5_7.isEq(dstMainVersion)) {

            // current_date and current_time process
            processTimesFuncHaveDefaultOrUpdate(sqlTypes, columnEditor);
            // current_date and current_time process
            processNumberDefaultHasFuncIgnore(sqlTypes, columnEditor);

        } else if (MySQLMainVersion.MySQL_8_0.isEq(dstMainVersion)) {

            // current_date and current_time process
            processTimesFuncHaveDefaultOrUpdate(sqlTypes, columnEditor);
        }

        // auto_increment must be one key
        processAutoIncrementCond(tableEditor, columnEditor, srcType, dstType);
    }

    private void processAutoIncrementCond(TableEditor tab, ColumnEditor column, DsType srcType, DsType dstType) {
        if (DsType.Db2 != srcType) {
            return;
        }
        EColumn col = column.getSource();
        if (!isKey(tab.getSource(), col) && col.isAutoGenerate()) {
            column.getSource().setAutoGenerate(false);
        }
    }

    private static boolean isKey(ETable eTable, EColumn col) {
        EPrimaryKey primaryKey = eTable.getPrimaryKey();
        if (primaryKey != null && primaryKey.getColumnList().size() == 1 && primaryKey.getColumnList().contains(col.getName())) {
            return true;
        }

        List<EIndex> indices = eTable.getIndices().stream().filter(in -> in.getType() == EIndexType.Unique).collect(Collectors.toList());
        for (EIndex index : indices) {
            List<String> idxs = index.getColumnList();
            if (idxs != null && idxs.size() == 1 && index.getColumnList().contains(col.getName())) {
                return true;
            }
        }

        return false;
    }

    private void processDefaultValueLengthOutOfColumnLength(MySQLTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        if (StringUtils.isBlank(defaultValue) || domain.isDefaultValueIsFunc()) {
            return;
        }
        switch (sqlTypes) {
            case CHAR:
            case VARCHAR:
            case BINARY:
            case VARBINARY:
            case BIT:
                break;
            default:
                return;
        }

        int defaultLength = defaultValue.length();
        Long length = domain.getLength();
        if (length != null && length < defaultLength) {
            columnEditor.setCharLength((long) defaultLength);
        }
    }

    // only TIMESTAMP/DATE/TIME/DATETIME with have `default xxx` or `on update xxxx` and this is defaultValueIsFunc
    private void processTimesFuncHaveDefaultOrUpdate(MySQLTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        boolean defaultValueIsFunc = domain.isDefaultValueIsFunc();
        String updateTypeValue = CURRENT_UPDATE_TYPE.getValue(domain.getAttribute());
        boolean isDate = sqlTypes == MySQLTypes.TIMESTAMP || sqlTypes == MySQLTypes.DATE || sqlTypes == MySQLTypes.TIME || sqlTypes == MySQLTypes.DATETIME;

        if (!defaultValueIsFunc || !isDate || StringUtils.isBlank(defaultValue) && StringUtils.isBlank(updateTypeValue)) {
            return;
        }

        switch (sqlTypes) {
            case TIME:
            case DATE: {
                columnEditor.changeType(MySQLTypes.TIMESTAMP.getCodeKey());

                if (StringUtils.isNotBlank(defaultValue)) {
                    if (StringUtils.equalsIgnoreCase(defaultValue, "curdate()") || StringUtils.equalsIgnoreCase(defaultValue, "curtime()")) {
                        columnEditor.setDefault("current_timestamp", true);
                    }
                }

                if (StringUtils.isNotBlank(updateTypeValue)) {
                    CURRENT_UPDATE_TYPE.setValue(domain.getAttribute(), MySQLOnCurrentUpdateType.CurrentTimestamp.getTypeName());
                }
            }
            default:
                break;
        }
    }

    // number default has func ignore
    private void processNumberDefaultHasFuncIgnore(MySQLTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        boolean defaultValueIsFunc = domain.isDefaultValueIsFunc();

        if (defaultValueIsFunc && StringUtils.isBlank(defaultValue)) {
            switch (sqlTypes) {
                case BIT:
                case TINYINT:
                case SMALLINT:
                case MEDIUMINT:
                case INT:
                case BIGINT:
                case FLOAT:
                case DOUBLE:
                case DECIMAL: {
                    columnEditor.setDefault(null, false);
                    break;
                }
            }
        }
    }

    @SneakyThrows
    private void processMaxKeySize(TableEditor tableEditor, int defaultMaxKeySize, int partKeySize) {
        Map<String, List<String>> dispose = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> disposeParts = new LinkedHashMap<>();

        for (String index : tableEditor.getIndexes()) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(index);
            EIndex eIndex = indexEditor.getSource();
            String subPartJson = SUB_PART.getValue(eIndex.getAttribute());
            Map<String, Integer> subPart = StringUtils.isBlank(subPartJson) ? new HashMap<>() : //
                new ObjectMapper().readValue(subPartJson, new TypeReference<Map<String, Integer>>() {
                });

            List<String> columnList = eIndex.getColumnList();
            long indexColumnsTotalSize = 0;
            for (String column : columnList) {
                ColumnEditor columnEditor = tableEditor.getColumn(column);
                MySQLTypes types = MySQLTypes.valueOfCode(columnEditor.getSource().getDbType());
                long columnSize = typeOfSize(types, columnEditor.getSource());
                if (columnSize >= defaultMaxKeySize) {
                    subPart.put(column, partKeySize);
                    indexColumnsTotalSize += defaultMaxKeySize;
                } else {
                    indexColumnsTotalSize += columnSize;
                }
            }

            if (indexColumnsTotalSize > defaultMaxKeySize) {
                tableEditor.getIndexEditor(index).delete();
                continue;
            }

            boolean hasAnyPartColumn = subPart.values().stream().anyMatch(Objects::nonNull);
            if (hasAnyPartColumn) {
                dispose.put(index, columnList);
                disposeParts.put(index, subPart);
            }
        }

        dispose.forEach((index, columns) -> {
            tableEditor.getIndexEditor(index).delete();
            IndexEditor indexEditor = tableEditor.addIndexEditor(index, EIndexType.Normal, columns);
            Map<String, Integer> indexSubParts = disposeParts.get(index);
            if (indexSubParts != null) {
                SUB_PART.setValue(indexEditor.getSource().getAttribute(), JSON.toString(indexSubParts));
            }
        });
    }

    private long evalRowSize(TableEditor tableEditor, List<String> columnNames) {
        long rowSize = 0;
        for (String columnName : columnNames) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            MySQLTypes types = MySQLTypes.valueOfCode(columnEditor.getSource().getDbType());
            rowSize += typeOfSize(types, columnEditor.getSource());
        }
        return rowSize;
    }

    private void processColumns(TableEditor tableEditor, long rowOverFlowSize) {
        Map<String, MySQLTypes> columnTypes = new HashMap<>();
        List<ColumnEditor> needProcess = new ArrayList<>();
        for (String columnName : tableEditor.getColumnNames()) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            EColumn eColumn = columnEditor.getSource();

            MySQLTypes types = MySQLTypes.valueOfCode(eColumn.getDbType());
            if (isChars(types) || isBinary(types)) {
                needProcess.add(columnEditor);
                columnTypes.put(eColumn.getName(), types);
            }
        }
        needProcess.sort((o1, o2) -> {
            MySQLTypes t1 = columnTypes.get(o1.getSource().getName());
            MySQLTypes t2 = columnTypes.get(o2.getSource().getName());
            long typeSize1 = typeOfSize(t1, o1.getSource());
            long typeSize2 = typeOfSize(t2, o2.getSource());
            return -Long.compare(typeSize1, typeSize2);
        });

        for (ColumnEditor column : needProcess) {
            EColumn eColumn = column.getSource();

            MySQLTypes type = columnTypes.get(eColumn.getName());
            long typeSize = typeOfSize(type, eColumn);

            if (typeSize < 20) {
                continue;
            }

            if (isChars(type)) {
                column.changeType(MySQLTypes.TEXT.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }
            if (isBinary(type)) {
                column.changeType(MySQLTypes.BLOB.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }

            if (rowOverFlowSize <= 0) {
                return;
            }
        }
    }

    private boolean isLob(MySQLTypes type) {
        switch (type) {
            case BLOB:
            case TINYBLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TEXT:
            case TINYTEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
                return true;
            default:
                return false;
        }
    }

    private boolean isChars(MySQLTypes type) {
        switch (type) {
            case CHAR:
            case VARCHAR:
                return true;
            default:
                return false;
        }
    }

    private boolean isBinary(MySQLTypes type) {
        switch (type) {
            case BIT:
            case BINARY:
            case VARBINARY:
                return true;
            default:
                return false;
        }
    }

    // https://dev.mysql.com/doc/refman/5.7/en/storage-requirements.html
    private long typeOfSize(MySQLTypes type, EColumn eColumn) {
        Long length = eColumn.getLength();
        if (length == null || length <= 0) {
            length = 0L;
        }

        switch (type) {
            case BIT:
                return (length + 7) / 8; // BIT(M) approximately (M+7)/8 bytes
            case TINYINT:
                return 1;
            case SMALLINT:
                return 2;
            case MEDIUMINT:
                return 3;
            case INT:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
                return 8;
            case DECIMAL:
                return 8; // max is 8 bytes.
            case DATE:
                return 3;
            case DATETIME:
                return 8; // 5 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIMESTAMP:
                return 7; // 4 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIME:
                return 6; // 3 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case YEAR:
                return 1;
            case CHAR:
            case VARCHAR:
                //TODO Character set must be considered, now use utf8
                return length * 3;// use utf8 length
            case BINARY:
            case VARBINARY:
                return Math.max(length, 1);
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case JSON:
                return 20;
            case ENUM:
                return 2; // 1 or 2 bytes, depending on the number of enumeration values (65,535 values maximum)
            case SET:
                return 8; // 1, 2, 3, 4, or 8 bytes, depending on the number of set members (64 members maximum)
            case GEOMETRY:
            case POINT:
            case LINESTRING:
            case POLYGON:
            case MULTIPOINT:
            case GEOMETRY_COLLECTION:
            case GEOM_COLLECTION:
            case MULTILINESTRING:
            case MULTIPOLYGON:
                return 4;
            default:
                break;
        }
        return 0;
    }

    private boolean isAliSQL(TriggerContext triggerContext) {
        return StringUtils.equalsIgnoreCase(triggerContext.getAttribute(MYSQL_IS_ALI_SQL), "true");
    }

    private void processColumnTypeWithDisplayWidth(DsType srcType, MySQLTypes sqlTypes, ColumnEditor columnEditor) {
        if (srcType != DsType.MySQL) {
            return;
        }
        EColumn eColumn = columnEditor.getSource();
        String rdbmyCt = COLUMN_TYPE.getValue(eColumn.getAttribute());
        String rdbmyCtwdw = "";
        if (sqlTypes.equals(MySQLTypes.BIT) || sqlTypes.equals(MySQLTypes.TINYINT) || sqlTypes.equals(MySQLTypes.SMALLINT) || sqlTypes.equals(MySQLTypes.MEDIUMINT)
            || sqlTypes.equals(MySQLTypes.INT) || sqlTypes.equals(MySQLTypes.BIGINT)) {
            rdbmyCtwdw = rdbmyCt;
        }
        if (StringUtils.isNotBlank(rdbmyCtwdw)) {
            columnEditor.setColumnTypeWithDisplayWidth(rdbmyCtwdw);
        }
    }
}
