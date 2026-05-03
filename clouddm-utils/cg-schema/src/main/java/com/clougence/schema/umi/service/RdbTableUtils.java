package com.clougence.schema.umi.service;

import java.util.*;

import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.schema.umi.struts.constraint.Unique;
import com.clougence.utils.CollectionUtils;

/**
 * AbstractRdbUmiServiceDm for CloudDM
 * 
 * @author mode 2021/1/8 19:56
 */
public final class RdbTableUtils {

    public static List<RdbTable> fillRdbTable(List<RdbTable> rdbTables, RdbTableUtilsInfo batchInfo) {
        if (CollectionUtils.isEmpty(rdbTables)) {
            return Collections.emptyList();
        }

        Map<String, Set<String>> pkCols = new HashMap<>();
        Map<String, Set<String>> ukCols = new HashMap<>();
        fillPkUkCons(pkCols, ukCols, batchInfo.getPkUkMap());

        List<RdbTable> resultList = new ArrayList<>();
        for (RdbTable umiTable : rdbTables) {
            String tableName = umiTable.getName();

            // pk & uk & fk & index
            fillConsAndIndex(umiTable, tableName, batchInfo);

            // columns
            List<RdbColumn> columns = batchInfo.getAllColumns().get(tableName);
            if (columns != null) {
                Set<String> pks = pkCols.get(tableName);
                Set<String> uks = ukCols.get(tableName);

                for (RdbColumn col : columns) {
                    if (pks != null && pks.contains(col.getName())) {
                        col.addConstraint(new Primary());
                    }

                    if (uks != null && uks.contains(col.getName())) {
                        col.addConstraint(new Unique());
                    }

                    umiTable.addColumn(col);
                }
            }

            //Partition
            if (batchInfo.getPartitionMap() != null) {
                umiTable.setPartition(batchInfo.getPartitionMap().get(tableName));
            }

            // CONSTRAINT
            if (batchInfo.getConstraintMap() != null && batchInfo.getConstraintMap().containsKey(tableName)) {
                List<ConstraintObject> constraintObjects = batchInfo.getConstraintMap().get(tableName);
                for (ConstraintObject constraintObject : constraintObjects) {
                    if (constraintObject instanceof RdbUniqueKey) {
                        umiTable.addUniqueConstraint((RdbUniqueKey) constraintObject);
                    } else if (constraintObject instanceof RdbCheckConstraint) {
                        umiTable.addCheckConstraint((RdbCheckConstraint) constraintObject);
                    }
                }
            }

            resultList.add(umiTable);

        }
        return resultList;
    }

    private static void fillPkUkCons(Map<String, Set<String>> pkCols, Map<String, Set<String>> ukCols, Map<String, Map<String, UmiConstraint>> pkUkMap) {
        for (Map.Entry<String, Map<String, UmiConstraint>> entry : pkUkMap.entrySet()) {
            Set<String> tabPkCols = pkCols.computeIfAbsent(entry.getKey(), k -> new HashSet<>());
            Set<String> tabUkCols = ukCols.computeIfAbsent(entry.getKey(), k -> new HashSet<>());
            for (UmiConstraint cons : entry.getValue().values()) {
                if (cons instanceof RdbPrimaryKey) {
                    RdbPrimaryKey pk = (RdbPrimaryKey) cons;
                    tabPkCols.addAll(pk.getColumnList());
                } else if (cons instanceof RdbUniqueKey) {
                    RdbUniqueKey uk = (RdbUniqueKey) cons;
                    tabUkCols.addAll(uk.getColumnList());
                }
            }
        }
    }

    private static void fillConsAndIndex(RdbTable rdbTable, String tableName, RdbTableUtilsInfo batchInfo) {
        // pk & uk
        Map<String, UmiConstraint> tabPkUkMap = batchInfo.getPkUkMap().get(tableName);
        if (tabPkUkMap != null) {
            List<RdbUniqueKey> uks = new ArrayList<>();
            for (Map.Entry<String, UmiConstraint> entry : tabPkUkMap.entrySet()) {
                if (entry.getValue() instanceof RdbPrimaryKey) {
                    RdbPrimaryKey primaryKey = (RdbPrimaryKey) (entry.getValue());
                    rdbTable.setPrimaryKey(primaryKey);
                } else if (entry.getValue() instanceof RdbUniqueKey) {
                    uks.add((RdbUniqueKey) (entry.getValue()));
                }
            }
            if (!uks.isEmpty()) {
                rdbTable.setUniqueKeys(uks);
            }
        }

        // fks
        List<RdbForeignKey> foreignKeyList = batchInfo.getFkMap().get(tableName);
        if (foreignKeyList != null) {
            rdbTable.setForeignKeys(foreignKeyList);
        }

        // indexs
        List<RdbIndex> indexesList = batchInfo.getIndexMap().get(tableName);
        if (indexesList != null) {
            indexesList.forEach(rdbTable::addIndex);
        }
    }
}
