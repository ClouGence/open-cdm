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
package com.clougence.clouddm.console.web.util;

import java.util.*;

import com.clougence.clouddm.sdk.ui.editor.table.TableEditorFields;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-07-12 14:45
 */
@Getter
@Setter
public class TableEditorUiDataUtils implements TableEditorFields {

    static {
        // table
        TABLE_BLACK_LIST_ATTRS.add(MODE_TABLE_CATALOG);
        TABLE_BLACK_LIST_ATTRS.add(MODE_TABLE_SCHEMA);
        TABLE_BLACK_LIST_ATTRS.add(MODE_TABLE_NAME);
        TABLE_BLACK_LIST_ATTRS.add(MODE_TABLE_COMMENT);

        // column
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_SOURCE);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_NAME);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_COMMENT);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_NOT_NULL);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_AUTOINCREMENT);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_DEFAULT);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_TYPE);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_LENGTH);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_NUM_P);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_NUM_S);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_UNSIGNED);
        COL_BLACK_LIST_ATTRS.add(MODE_COLUMN_DATE_P);

        // pk
        PK_BLACK_LIST_ATTRS.add(MODE_KEY_NAME);
        PK_BLACK_LIST_ATTRS.add(MODE_KEY_COLUMNS);

        // index
        INDEX_BLACK_LIST_ATTRS.add(MODE_INDEX_NAME);
        INDEX_BLACK_LIST_ATTRS.add(MODE_INDEX_TYPE);
        INDEX_BLACK_LIST_ATTRS.add(MODE_INDEX_COMMENT);
        INDEX_BLACK_LIST_ATTRS.add(MODE_INDEX_COLUMNS);

        // partition
        PARTITION_BLACK_LIST_ATTRS.add(MODE_DEFINITION_PARTITION_TYPE);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_DEFINITION_PARTITION_EXPRESSION);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_TEMPLATE);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_ITEM_NAME);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_ITEM_DESCRIPTION);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_ITEM_TABLESPACE);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_ITEM_COMMENT);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_TYPE);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_SUB_PARTITION_TYPE);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_COUNT);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_SUB_PARTITION_COUNT);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_SUB_PARTITION_EXPRESSION);
        PARTITION_BLACK_LIST_ATTRS.add(MODE_PARTITION_EXPRESSION);

        // constraint
        CONSTRAINT_BLACK_LIST_ATTRS.add(MODE_CONSTRAINT_NAME);
        CONSTRAINT_BLACK_LIST_ATTRS.add(MODE_CONSTRAINT_TYPE);
        CONSTRAINT_BLACK_LIST_ATTRS.add(MODE_CONSTRAINT_EXPRESSION);
        CONSTRAINT_BLACK_LIST_ATTRS.add(MODE_CONSTRAINT_COLUMNS);

        //fk
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_REFERENCED_SCHEMA);
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_REFERENCED_TABLE);
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_REFERENCED_RELATION);
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_REFERENCED_COLUMN_NAME);
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_UPDATE_RULE);
        FK_BLACK_LIST_ATTRS.add(MODE_FOREIGN_KEY_DELETE_RULE);

    }

    public static ETable formUiData(TableEditorUiData uiData, ETable targetTable, String schema) {
        // convert basic
        targetTable.setSchema(schema);

        // convert tableInfo
        Map<String, Object> tableInfo = uiData.getTableInfo();
        if (CollectionUtils.isNotEmpty(tableInfo)) {
            if (tableInfo.containsKey(MODE_TABLE_NAME)) {
                targetTable.setName(safeToString(tableInfo.get(MODE_TABLE_NAME)));
            }
            if (tableInfo.containsKey(MODE_TABLE_COMMENT)) {
                targetTable.setComment(safeToString(tableInfo.get(MODE_TABLE_COMMENT)));
            }
            for (String key : tableInfo.keySet()) {
                if (!TABLE_BLACK_LIST_ATTRS.contains(key)) {
                    targetTable.getAttribute().put(key, safeToString(tableInfo.get(key)));
                }
            }
        }

        // convert columns
        List<Map<String, Object>> columnsInfo = uiData.getColumns();
        List<EColumn> eColumnList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columnsInfo)) {
            for (int i = 0; i < columnsInfo.size(); i++) {
                Map<String, Object> curColumn = columnsInfo.get(i);
                EColumn targetEColumn = findOrCreateColumn(targetTable, safeToString(curColumn.get(MODE_COLUMN_SOURCE)));

                if (curColumn.containsKey(MODE_COLUMN_NAME)) {
                    targetEColumn.setName(safeToString(curColumn.get(MODE_COLUMN_NAME)));
                }
                if (curColumn.containsKey(MODE_COLUMN_TYPE)) {
                    targetEColumn.setDbType(safeToString(curColumn.get(MODE_COLUMN_TYPE)));
                }
                if (curColumn.containsKey(MODE_COLUMN_NOT_NULL)) {
                    targetEColumn.setNullable(!safeToBoolean(curColumn.get(MODE_COLUMN_NOT_NULL) == null || safeToBoolean(curColumn.get(MODE_COLUMN_NOT_NULL))));
                }
                if (curColumn.containsKey(MODE_COLUMN_LENGTH)) {
                    targetEColumn.setLength(safeToLong(curColumn.get(MODE_COLUMN_LENGTH)));
                }
                if (curColumn.containsKey(MODE_COLUMN_NUM_P)) {
                    targetEColumn.setNumericPrecision(safeToInteger(curColumn.get(MODE_COLUMN_NUM_P)));
                }
                if (curColumn.containsKey(MODE_COLUMN_NUM_S)) {
                    targetEColumn.setNumericScale(safeToInteger(curColumn.get(MODE_COLUMN_NUM_S)));
                }
                if (curColumn.containsKey(MODE_COLUMN_UNSIGNED)) {
                    targetEColumn.setNumericUnsigned(safeToBoolean(curColumn.get(MODE_COLUMN_UNSIGNED)));
                }
                if (curColumn.containsKey(MODE_COLUMN_DATE_P)) {
                    targetEColumn.setDatetimePrecision(safeToInteger(curColumn.get(MODE_COLUMN_DATE_P)));
                }

                String defaultOpt = safeToString(curColumn.get(MODE_COLUMN_DEFAULT_OPTION));
                curColumn.remove(MODE_COLUMN_DEFAULT_OPTION);
                if (defaultOpt != null) {
                    if (StringUtils.equalsIgnoreCase(defaultOpt, "NULL")) {
                        targetEColumn.setDefaultValue(null);
                    } else if (StringUtils.equalsIgnoreCase(defaultOpt, "EMPTY")) {
                        targetEColumn.setDefaultValue("");
                    } else if (StringUtils.equalsIgnoreCase(defaultOpt, "CUSTOM")) {
                        targetEColumn.setDefaultValue(safeToString(curColumn.get(MODE_COLUMN_DEFAULT)));
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }

                if (curColumn.containsKey(MODE_COLUMN_AUTOINCREMENT)) {
                    targetEColumn.setAutoGenerate(safeToBoolean(curColumn.get(MODE_COLUMN_AUTOINCREMENT)));
                }
                if (curColumn.containsKey(MODE_COLUMN_COMMENT)) {
                    targetEColumn.setComment(safeToString(curColumn.get(MODE_COLUMN_COMMENT)));
                }
                for (String key : curColumn.keySet()) {
                    if (!COL_BLACK_LIST_ATTRS.contains(key)) {
                        targetEColumn.getAttribute().put(key, safeToString(curColumn.get(key)));
                    }
                }

                // TODO maybe delete
                // SpiltTypeInfo spiltTypeInfo = new SpiltTypeInfo();
                // spiltTypeInfo.setDataType(safeToString(curColumn.get(MODE_COLUMN_TYPE)));
                // spiltTypeInfo.setEColumn(targetEColumn);
                // targetEColumn = SchemaFramework.getSpiltType(dsType, spiltTypeInfo);

                eColumnList.add(targetEColumn);
                // find Before and ADD
                //                if (StringUtils.isBlank(targetEColumn.getSource())) {
                //                    String beforeColumn = null;
                //                    if ((i + 1) < columnsInfo.size()) {
                //                        beforeColumn = safeToString(columnsInfo.get(i + 1).get(MODE_COLUMN_NAME));
                //                    }
                //
                //                    EColumn beforeCol = findColumn(targetTable, beforeColumn);
                //                    if (beforeCol != null) {
                //                        int index = targetTable.getColumnList().indexOf(beforeCol);
                //                        targetTable.getColumnList().add(index, targetEColumn);
                //                    } else {
                //                        targetTable.getColumnList().add(targetEColumn);
                //                    }
                //                }
            }
        }
        targetTable.setColumnList(eColumnList);

        // convert primaryKey
        Map<String, Object> primaryKey = uiData.getKeys();
        if (CollectionUtils.isNotEmpty(primaryKey)) {
            EPrimaryKey curKey = findOrCreatePrimaryKey(targetTable);

            if (primaryKey.containsKey(MODE_KEY_NAME)) {
                curKey.setPrimaryKeyName(safeToString(primaryKey.get(MODE_KEY_NAME)));
            }
            if (primaryKey.containsKey(MODE_KEY_COLUMNS)) {
                Object obj = primaryKey.get(MODE_KEY_COLUMNS);
                List<String> keyColumnList = new ArrayList<>();
                if (obj instanceof List) {
                    List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> map : columnListMap) {
                        String name = safeToString(map.get(MODE_KEY_COLUMNS_NAME));
                        keyColumnList.add(name);
                    }
                }
                curKey.setColumnList(keyColumnList);
            }

            for (String key : primaryKey.keySet()) {
                if (!PK_BLACK_LIST_ATTRS.contains(key)) {
                    curKey.getAttribute().put(key, safeToString(primaryKey.get(key)));
                }
            }

            if (targetTable.getPrimaryKey() == null) {
                targetTable.setPrimaryKey(curKey);
            }
        } else {
            targetTable.setPrimaryKey(null);
        }

        // convert Indexes
        List<Map<String, Object>> indexes = uiData.getIndexes();
        List<EIndex> indexList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(indexes)) {
            for (int i = 0; i < indexes.size(); i++) {
                Map<String, Object> curIndex = indexes.get(i);
                EIndex targetEIndex = findOrCreateIndex(targetTable, safeToString(curIndex.get(MODE_INDEX_NAME)));

                if (curIndex.containsKey(MODE_INDEX_NAME)) {
                    targetEIndex.setName(safeToString(curIndex.get(MODE_INDEX_NAME)));
                }
                if (curIndex.containsKey(MODE_INDEX_COMMENT)) {
                    targetEIndex.setComment(safeToString(curIndex.get(MODE_INDEX_COMMENT)));
                }
                // remove to spi
                //                if (curIndex.containsKey(MODE_INDEX_TYPE) && curIndex.containsKey(MySQLAttributeNames.INDEX_WAY.getCodeKey())) {
                //                    String idxType = safeToString(curIndex.get(MODE_INDEX_TYPE));
                //                    String idxWay = safeToString(curIndex.get(MySQLAttributeNames.INDEX_WAY.getCodeKey()));
                //                    if (StringUtils.equalsIgnoreCase(idxType, "BTREE") && StringUtils.equalsIgnoreCase(idxWay, "UNIQUE")) {
                //                        targetEIndex.setType(EIndexType.Unique);
                //                    } else {
                //                        targetEIndex.setType(EIndexType.Normal);
                //                    }
                //                }
                if (curIndex.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = curIndex.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            newColumnList.add(name);
                        }
                    }
                    targetEIndex.setColumnList(newColumnList);
                }
                for (String key : curIndex.keySet()) {
                    if (!INDEX_BLACK_LIST_ATTRS.contains(key)) {
                        targetEIndex.getAttribute().put(key, safeToString(curIndex.get(key)));
                    }
                }
                indexList.add(targetEIndex);
            }
        }
        targetTable.setIndices(indexList);

        // partitions
        Map<String, Object> partitions = uiData.getPartitions();
        if (CollectionUtils.isNotEmpty(partitions)) {
            EPartition ePartition = new EPartition();
            ePartition.setPartitionExpression(safeToString(partitions.get(MODE_PARTITION_EXPRESSION)));
            ePartition.setPartitionMethod(safeToString(partitions.get(MODE_PARTITION_TYPE)));
            ePartition.setPartitionCount(safeToLong(partitions.get(MODE_PARTITION_COUNT)));
            ePartition.setSubPartitionExpression(safeToString(partitions.get(MODE_SUB_PARTITION_EXPRESSION)));
            ePartition.setSubPartitionMethod(safeToString(partitions.get(MODE_SUB_PARTITION_TYPE)));
            ePartition.setSubPartitionCount(safeToLong(partitions.get(MODE_SUB_PARTITION_COUNT)));

            // definitions
            List<Map<String, Object>> definitions = (List<Map<String, Object>>) partitions.get(MODE_PARTITION_DEFINITION);
            if (CollectionUtils.isNotEmpty(definitions)) {
                for (Map<String, Object> definition : definitions) {
                    EPartitionDefinition partitionDefinition = new EPartitionDefinition();
                    partitionDefinition.setType((String) definition.get(MODE_DEFINITION_PARTITION_TYPE));
                    partitionDefinition.setExpression((String) definition.get(MODE_DEFINITION_PARTITION_EXPRESSION));

                    List<Map<String, Object>> templates = (List<Map<String, Object>>) definition.get(MODE_PARTITION_TEMPLATE);
                    if (CollectionUtils.isNotEmpty(templates)) {
                        for (Map<String, Object> template : templates) {
                            EPartitionItem ePartitionItem = new EPartitionItem();
                            ePartitionItem.setDescription(safeToString(template.get(MODE_PARTITION_ITEM_DESCRIPTION)));
                            ePartitionItem.setName(safeToString(template.get(MODE_PARTITION_ITEM_NAME)));
                            ePartitionItem.setTablespace(safeToString(template.get(MODE_PARTITION_ITEM_TABLESPACE)));

                            for (String key : template.keySet()) {
                                if (!PK_BLACK_LIST_ATTRS.contains(key)) {
                                    ePartitionItem.getAttribute().put(key, safeToString(template.get(key)));
                                }
                            }
                            partitionDefinition.getPartitionTemplate().add(ePartitionItem);
                        }
                    }
                    for (String key : definition.keySet()) {
                        if (!PARTITION_BLACK_LIST_ATTRS.contains(key)) {
                            partitionDefinition.getAttribute().put(key, safeToString(definition.get(key)));
                        }
                    }
                    ePartition.getDefinitions().add(partitionDefinition);
                }
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) partitions.get(MODE_PARTITION_ITEMS);
            if (CollectionUtils.isNotEmpty(items)) {
                for (Map<String, Object> item : items) {
                    EPartitionItem ePartitionItem = new EPartitionItem();
                    ePartitionItem.setDescription(safeToString(item.get(MODE_PARTITION_ITEM_DESCRIPTION)));
                    ePartitionItem.setName(safeToString(item.get(MODE_PARTITION_ITEM_NAME)));
                    if (item.get("subItems") != null) {
                        ePartitionItem.setSubPartitionItems(parseSubItems((List<Map<String, Object>>) item.get("subItems")));
                    }

                    for (String key : item.keySet()) {
                        if (!PARTITION_BLACK_LIST_ATTRS.contains(key)) {
                            ePartitionItem.getAttribute().put(key, safeToString(item.get(key)));
                        }
                    }
                    ePartition.getItems().add(ePartitionItem);
                }
            }

            for (String key : partitions.keySet()) {
                if (!PK_BLACK_LIST_ATTRS.contains(key)) {
                    ePartition.getAttribute().put(key, safeToString(partitions.get(key)));
                }
            }
            targetTable.setPartition(ePartition);
        } else {
            targetTable.setPartition(null);
        }

        // constraints
        List<Map<String, Object>> constraints = uiData.getConstraints();
        List<EConstraint> constraintList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(constraints)) {
            for (Map<String, Object> constraint : constraints) {
                EConstraint eConstraint = new EConstraint();

                eConstraint.setName(safeToString(constraint.get(MODE_CONSTRAINT_NAME)));
                eConstraint.setExpression(safeToString(constraint.get(MODE_CONSTRAINT_EXPRESSION)));
                eConstraint.setType(EConstraintType.valueOfCode(safeToString(constraint.get(MODE_CONSTRAINT_TYPE))));

                if (constraint.containsKey(MODE_CONSTRAINT_COLUMNS)) {
                    Object obj = constraint.get(MODE_CONSTRAINT_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            String name = safeToString(map.get(MODE_CONSTRAINT_COLUMN_NAME));
                            newColumnList.add(name);
                        }

                    }
                    eConstraint.setColumnList(newColumnList);
                }

                for (String key : constraint.keySet()) {
                    if (!CONSTRAINT_BLACK_LIST_ATTRS.contains(key)) {
                        eConstraint.getAttribute().put(key, safeToString(constraint.get(key)));
                    }
                }
                constraintList.add(eConstraint);
            }
        }
        targetTable.setConstraints(constraintList);

        // foreign keys
        List<Map<String, Object>> foreignKeys = uiData.getForeignKeys();
        List<EForeignKey> eForeignKeys = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(foreignKeys)) {
            for (Map<String, Object> foreignKey : foreignKeys) {
                EForeignKey eForeignKey = new EForeignKey();
                eForeignKey.setName(safeToString(foreignKey.get(MODE_FOREIGN_KEY_NAME)));
                eForeignKey.setReferenceTable(safeToString(foreignKey.get(MODE_FOREIGN_KEY_REFERENCED_TABLE)));
                eForeignKey.setReferenceSchema(safeToString(foreignKey.get(MODE_FOREIGN_KEY_REFERENCED_SCHEMA)));
                eForeignKey.setUpdateRule(RdbForeignKeyRule.valueOfCode(safeToString(foreignKey.get(MODE_FOREIGN_KEY_UPDATE_RULE))));
                eForeignKey.setDeleteRule(RdbForeignKeyRule.valueOfCode(safeToString(foreignKey.get(MODE_FOREIGN_KEY_DELETE_RULE))));

                Object obj = foreignKey.get(MODE_FOREIGN_KEY_REFERENCED_RELATION);
                Map<String, String> map = new LinkedHashMap<>();
                if (obj instanceof List) {
                    List<Map<String, String>> referenceMap = (List<Map<String, String>>) obj;
                    for (Map<String, String> map1 : referenceMap) {
                        String referencedColumn = map1.get(MODE_FOREIGN_KEY_REFERENCED_COLUMN_NAME);
                        String column = map1.get(MODE_CONSTRAINT_COLUMN_NAME);
                        eForeignKey.getColumnList().add(column);
                        map.put(column, referencedColumn);
                    }
                }
                eForeignKey.setReferenceMapping(map);
                for (String key : foreignKey.keySet()) {
                    if (!FK_BLACK_LIST_ATTRS.contains(key)) {
                        eForeignKey.getAttribute().put(key, safeToString(foreignKey.get(key)));
                    }
                }
                eForeignKeys.add(eForeignKey);
            }
        }
        targetTable.setForeignKeys(eForeignKeys);

        return targetTable;
    }

    private static List<EPartitionItem> parseSubItems(List<Map<String, Object>> subItems) {
        List<EPartitionItem> ePartitionItems = new ArrayList<>();
        for (Map<String, Object> subItem : subItems) {
            EPartitionItem item = new EPartitionItem();
            item.setDescription(safeToString(subItem.get(MODE_PARTITION_ITEM_DESCRIPTION)));
            item.setName(safeToString(subItem.get(MODE_PARTITION_ITEM_NAME)));

            if (subItem.get("subItems") != null) {
                item.setSubPartitionItems(parseSubItems((List<Map<String, Object>>) subItem.get("subItems")));
            }

            for (String key : subItem.keySet()) {
                item.getAttribute().put(key, safeToString(subItem.get(key)));
            }
            ePartitionItems.add(item);
        }
        return ePartitionItems;
    }

    public static TableEditorUiData toUiData(ETable eTable, String version) {
        TableEditorUiData uiData = new TableEditorUiData();
        Map<String, Object> tableInfo = uiData.getTableInfo();

        // convert table
        tableInfo.putAll(eTable.getAttribute());
        tableInfo.put(MODE_TABLE_CATALOG, eTable.getCatalog());
        tableInfo.put(MODE_TABLE_SCHEMA, eTable.getSchema());
        tableInfo.put(MODE_TABLE_NAME, eTable.getName());
        tableInfo.put(MODE_TABLE_COMMENT, eTable.getComment());

        // convert columns
        for (EColumn eColumn : eTable.getColumnList()) {
            Map<String, Object> column = new LinkedHashMap<>();

            column.putAll(eColumn.getAttribute());
            column.put(MODE_COLUMN_SOURCE, eColumn.getSource());
            column.put(MODE_COLUMN_NAME, eColumn.getName());
            column.put(MODE_COLUMN_COMMENT, eColumn.getComment());
            column.put(MODE_COLUMN_NOT_NULL, !Boolean.TRUE.equals(eColumn.getNullable()));
            column.put(MODE_COLUMN_AUTOINCREMENT, eColumn.isAutoGenerate());
            column.put(MODE_COLUMN_DEFAULT, eColumn.getDefaultValue());
            column.put(MODE_COLUMN_TYPE, eColumn.getDbType());
            column.put(MODE_COLUMN_LENGTH, eColumn.getLength());
            column.put(MODE_COLUMN_NUM_P, eColumn.getNumericPrecision());
            column.put(MODE_COLUMN_NUM_S, eColumn.getNumericScale());
            column.put(MODE_COLUMN_UNSIGNED, eColumn.isNumericUnsigned());
            column.put(MODE_COLUMN_DATE_P, eColumn.getDatetimePrecision());

            if (eColumn.getDefaultValue() == null) {
                column.put(MODE_COLUMN_DEFAULT_OPTION, "NULL");
            } else if ("".equals(eColumn.getDefaultValue())) {
                column.put(MODE_COLUMN_DEFAULT_OPTION, "EMPTY");
            } else {
                column.put(MODE_COLUMN_DEFAULT_OPTION, "CUSTOM");
            }

            uiData.getColumns().add(column);
            // TODO maybe delete
            //if (StringUtils.isNotEmpty(eColumn.getDbType())) {
            //    ParseTypeInfo parseTypeInfo = new ParseTypeInfo();
            //    MainVersion mainVersion = SchemaFramework.getMainVersion(dsType, version);
            //    parseTypeInfo.setEColumn(eColumn);
            //    parseTypeInfo.setMainVersion(mainVersion);
            //
            //    String parseType = SchemaFramework.getParseType(dsType, parseTypeInfo);
            //    column.put(MODE_COLUMN_TYPE, parseType);
            //}
        }

        // convert pk
        if (eTable.getPrimaryKey() != null) {
            EPrimaryKey primaryKey = eTable.getPrimaryKey();
            Map<String, Object> primaryKeyInfo = new HashMap<>();

            primaryKeyInfo.putAll(primaryKey.getAttribute());
            primaryKeyInfo.put(MODE_KEY_NAME, primaryKey.getPrimaryKeyName());

            List<Map<String, Object>> columns = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(primaryKey.getColumnList())) {
                for (String columnName : primaryKey.getColumnList()) {
                    Map<String, Object> columnMap = new HashMap<>();
                    columnMap.put(MODE_KEY_COLUMNS_NAME, columnName);
                    columns.add(columnMap);
                }
            }
            primaryKeyInfo.put(MODE_KEY_COLUMNS, columns);

            uiData.setKeys(primaryKeyInfo);
        }

        // convert indexes
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> indexMap = new LinkedHashMap<>();

            indexMap.putAll(index.getAttribute());
            indexMap.put(MODE_INDEX_NAME, index.getName());
            indexMap.put(MODE_INDEX_COMMENT, index.getComment());
            indexMap.put(MODE_INDEX_TYPE, index.getType());

            List<Map<String, Object>> columns = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(index.getColumnList())) {
                for (String columnName : index.getColumnList()) {
                    Map<String, Object> columnMap = new HashMap<>();
                    columnMap.put(MODE_INDEX_NAME, columnName);
                    columns.add(columnMap);
                }
            }
            indexMap.put(MODE_INDEX_COLUMNS, columns);

            uiData.getIndexes().add(indexMap);
        }

        // constraints
        for (EConstraint constraint : eTable.getConstraints()) {
            Map<String, Object> constraintMap = new LinkedHashMap<>();
            constraintMap.putAll(constraint.getAttribute());
            constraintMap.put(MODE_CONSTRAINT_NAME, constraint.getName());
            constraintMap.put(MODE_CONSTRAINT_EXPRESSION, constraint.getExpression());
            constraintMap.put(MODE_CONSTRAINT_TYPE, constraint.getType());
            List<Map<String, Object>> columns = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(constraint.getColumnList())) {
                for (String columnName : constraint.getColumnList()) {
                    Map<String, Object> columnMap = new HashMap<>();
                    columnMap.put(MODE_INDEX_NAME, columnName);
                    columns.add(columnMap);
                }
            }
            constraintMap.put(MODE_INDEX_COLUMNS, columns);

            uiData.getConstraints().add(constraintMap);
        }

        // foreign key
        for (EForeignKey foreignKey : eTable.getForeignKeys()) {
            Map<String, Object> foreignKeyMap = new LinkedHashMap<>();
            foreignKeyMap.putAll(foreignKey.getAttribute());
            foreignKeyMap.put(MODE_FOREIGN_KEY_NAME, foreignKey.getName());
            foreignKeyMap.put(MODE_FOREIGN_KEY_DELETE_RULE, foreignKey.getDeleteRule().getTypeName());
            foreignKeyMap.put(MODE_FOREIGN_KEY_UPDATE_RULE, foreignKey.getUpdateRule().getTypeName());
            foreignKeyMap.put(MODE_FOREIGN_KEY_REFERENCED_TABLE, foreignKey.getReferenceTable());
            foreignKeyMap.put(MODE_FOREIGN_KEY_REFERENCED_SCHEMA, foreignKey.getReferenceSchema());

            List<Map<String, String>> list = new ArrayList<>();
            foreignKey.getReferenceMapping().forEach((k, v) -> {
                LinkedHashMap<String, String> referenceMap = new LinkedHashMap<>();
                referenceMap.put(MODE_CONSTRAINT_COLUMN_NAME, k);
                referenceMap.put(MODE_FOREIGN_KEY_REFERENCED_COLUMN_NAME, v);
                list.add(referenceMap);
            });
            foreignKeyMap.put(MODE_FOREIGN_KEY_REFERENCED_RELATION, list);

            uiData.getForeignKeys().add(foreignKeyMap);
        }

        return uiData;
    }

    //
    // ------------------------------------------------------------------------------------------------------
    //

    private static EPrimaryKey findOrCreatePrimaryKey(ETable targetTable) {
        if (targetTable.getPrimaryKey() != null) {
            return targetTable.getPrimaryKey();
        } else {
            return new EPrimaryKey();
        }
    }

    private static EColumn findOrCreateColumn(ETable targetTable, String sourceCol) {
        return targetTable.getColumnList().stream().filter(eColumn -> {
            return StringUtils.equals(eColumn.getName(), sourceCol);
        }).findFirst().orElse(new EColumn());
    }

    private static EColumn findColumn(ETable targetTable, String sourceCol) {
        return targetTable.getColumnList().stream().filter(eColumn -> {
            return StringUtils.equals(eColumn.getName(), sourceCol);
        }).findFirst().orElse(null);
    }

    private static EIndex findOrCreateConstraint(ETable targetTable, String sourceConstraint) {
        return targetTable.getIndices().stream().filter(eIndex -> {
            return StringUtils.equals(eIndex.getName(), sourceConstraint);
        }).findFirst().orElse(new EIndex());
    }

    private static EIndex findOrCreateIndex(ETable targetTable, String sourceIndex) {
        return targetTable.getIndices().stream().filter(eIndex -> {
            return StringUtils.equals(eIndex.getName(), sourceIndex);
        }).findFirst().orElse(new EIndex());
    }

    private static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }

    private static Boolean safeToBoolean(Object obj) {
        return (obj == null) ? null : (Boolean) ConverterUtils.convert(Boolean.class, obj);
    }

    private static Integer safeToInteger(Object obj) {
        return (obj == null) ? null : (Integer) ConverterUtils.convert(Integer.class, obj);
    }

    private static Long safeToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return (Long) ConverterUtils.convert(Long.class, obj);
    }
}
