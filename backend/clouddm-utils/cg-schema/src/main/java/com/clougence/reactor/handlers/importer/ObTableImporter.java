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
package com.clougence.reactor.handlers.importer;

import static com.clougence.adapter.ob.obformysql.ObForMySQLAttributeNames.SUB_PART;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.INDEX_TYPE;

import java.util.*;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.ob.obformysql.ObForMySQLIndexType;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

/**
 * @author mode 2021/1/8 19:56
 */
public class ObTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

        // process maxKeySize
        processMaxKeySize(tableEditor, triggerContext, dstMainVersion);
    }

    private void processMaxKeySize(TableEditor tableEditor, TriggerContext triggerContext, MainVersion dstMainVersion) {
        if (dstMainVersion == null) {
            return;
        }

        processIdxWithText(tableEditor, 3072);
    }

    private void processIdxWithText(TableEditor tableEditor, int defaultMaxKeySize) {
        List<String> indexNames = tableEditor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            String idxName = indexEditor.getSource().getName();
            List<String> columnList = indexEditor.getSource().getColumnList();

            boolean hasLob = false;
            boolean allFullText = true;
            int lobMaxKeySize = defaultMaxKeySize;
            Map<String, Integer> blobSubPart = new HashMap<>();

            for (String column : columnList) {
                ColumnEditor editorColumn = tableEditor.getColumn(column);
                MySQLTypes type = MySQLTypes.valueOfCode(editorColumn.getSource().getDbType());

                if (isFullText(type)) {
                    blobSubPart.put(column, null);
                } else {
                    lobMaxKeySize -= typeOfSize(type, editorColumn.getSource());
                }

                allFullText = allFullText && isFullText(type);
                hasLob = hasLob || isLob(type);
            }

            if (!hasLob) {
                continue;
            }

            // has blob / text in index
            indexEditor.delete();
            IndexEditor editor = tableEditor.addIndexEditor(idxName, EIndexType.Normal, columnList);
            if (allFullText) {
                // all column is blob / text => normal fulltext index
                INDEX_TYPE.setValue(editor.getSource().getAttribute(), ObForMySQLIndexType.FullText.name());
                // full index columns in OceanBase can not be null
                for (String col : editor.getSource().getColumnList()) {
                    ColumnEditor eCol = tableEditor.getColumn(col);
                    eCol.setNullable(false);
                }
            } else {
                // blob / number / time / geo not support fulltext index => prefix (unique / normal) index
                int maxCollationBytes = 4;
                int avgSize = lobMaxKeySize / (blobSubPart.size() * maxCollationBytes);
                blobSubPart.forEach((k, v) -> blobSubPart.put(k, avgSize));
                SUB_PART.setValue(editor.getSource().getAttribute(), JSON.toString(blobSubPart));
            }
        }
    }

    private boolean isFullText(MySQLTypes type) {
        return !type.isGeometry() && !type.isNumber() && !type.isDataOrTime() && !type.isBinary();
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
            for (String column : columnList) {
                ColumnEditor columnEditor = tableEditor.getColumn(column);
                MySQLTypes types = MySQLTypes.valueOfCode(columnEditor.getSource().getDbType());
                long columnSize = typeOfSize(types, columnEditor.getSource());
                if (columnSize >= defaultMaxKeySize) {
                    subPart.put(column, partKeySize);
                }
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

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
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
            case YEAR:
                return 1;
            case SMALLINT:
                return 2;
            case MEDIUMINT:
            case DATE:
                return 3;
            case INT:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
                return 8;
            case DECIMAL:
                return 8; // max is 8 bytes.
            case DATETIME:
                return 8; // 5 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIMESTAMP:
                return 7; // 4 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIME:
                return 6; // 3 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case CHAR:
            case VARCHAR:
                return length * 4;// use utf-8 length TODO Character set factors should be considered
            case BINARY:
            case VARBINARY:
                return length;
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
            default:
                break;
        }
        return 0;
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return MySQLTypes.valueOfCode(columnType);
    }
}
