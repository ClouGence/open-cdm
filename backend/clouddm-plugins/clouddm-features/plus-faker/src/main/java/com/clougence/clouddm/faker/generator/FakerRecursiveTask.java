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
package com.clougence.clouddm.faker.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.clougence.clouddm.faker.config.FakerConfigEnum;
import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.config.SqlPolitic;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;

import com.clougence.utils.setting.SettingNode;
import com.clougence.utils.setting.data.TreeNode;

/**
 * @author Ekko
 * @date 2024/3/15 11:18
*/
public class FakerRecursiveTask {

    private final String                          catalog;
    private final String                          schema;
    private final List<String>                    tables;
    private final Map<String, SettingNode>        tableColumnsConfigs;
    private final Map<String, SettingNode>        tableInfoConfigs;
    private final Map<String, Map<OpsType, Long>> tableTotal;

    public FakerRecursiveTask(String catalog, String schema, List<String> tables, List<SettingNode> tableConfigs){
        this.catalog = catalog;
        this.schema = schema;
        this.tables = tables;
        this.tableTotal = Collections.emptyMap();
        this.tableColumnsConfigs = new HashMap<>();
        this.tableInfoConfigs = new HashMap<>();
        for (SettingNode tableConfig : tableConfigs) {
            String tableName = tableConfig.getSubValue(FakerConfigEnum.TABLE_TABLE.getConfigKey());
            SettingNode columnNode = tableConfig.findNode(FakerConfigEnum.TABLE_COLUMNS.getConfigKey());
            this.tableColumnsConfigs.put(tableName, columnNode);
            this.tableInfoConfigs.put(tableName, tableConfig);
        }
    }

    public FakerRecursiveTask(String catalog, String schema, List<String> tables, List<SettingNode> tableConfigs, Map<String, Map<OpsType, Long>> tableTotal){
        this.catalog = catalog;
        this.schema = schema;
        this.tables = tables;
        this.tableTotal = tableTotal;
        this.tableColumnsConfigs = new HashMap<>();
        this.tableInfoConfigs = new HashMap<>();
        for (SettingNode tableConfig : tableConfigs) {
            String tableName = tableConfig.getSubValue(FakerConfigEnum.TABLE_TABLE.getConfigKey());
            SettingNode columnNode = tableConfig.findNode(FakerConfigEnum.TABLE_COLUMNS.getConfigKey());
            this.tableColumnsConfigs.put(tableName, columnNode);
            this.tableInfoConfigs.put(tableName, tableConfig);
        }
    }

    public List<FakerTable> compute(FakerFactory factory, Session session) throws Exception {
        List<FakerTable> fakerTables = new CopyOnWriteArrayList<>();
        Map<UmiTypes, Object> map = new HashMap<>();
        map.put(UmiTypes.Catalog, this.catalog);
        map.put(UmiTypes.Schema, this.schema);

        for (String table : this.tables) {
            RdbTable rdbTable = (RdbTable) session.getMetaService().detailLeaf(map, UmiTypes.Table, table);
            FakerTable fakerTable = new FakerTable(this.catalog, this.schema, table, factory);
            FakerEngineConfig fakerConfig = factory.getEngineConfig();
            fakerTable.setUseQualifier(fakerConfig.isUseQualifier());
            fakerTable.setKeyChanges(fakerConfig.isKeyChanges());
            SettingNode columnConfig = this.tableColumnsConfigs.get(table);
            columnConfig = columnConfig == null ? new TreeNode() : columnConfig;
            factory.buildColumns(fakerTable, columnConfig, rdbTable);

            // Phase II to do this
            SettingNode tableSettingNode = this.tableInfoConfigs.get(table);
            String insertPoliticStr = tableSettingNode.getSubValue(FakerConfigEnum.TABLE_ACT_POLITIC_INSERT.getConfigKey());
            String updatePoliticStr = tableSettingNode.getSubValue(FakerConfigEnum.TABLE_ACT_POLITIC_UPDATE.getConfigKey());
            String wherePoliticStr = tableSettingNode.getSubValue(FakerConfigEnum.TABLE_ACT_POLITIC_WHERE.getConfigKey());

            fakerTable.setInsertPolitic(SqlPolitic.valueOfCode(insertPoliticStr, SqlPolitic.FullCol));
            fakerTable.setUpdateSetPolitic(SqlPolitic.valueOfCode(updatePoliticStr, SqlPolitic.FullCol));
            fakerTable.setWherePolitic(SqlPolitic.valueOfCode(wherePoliticStr, SqlPolitic.KeyCol));

            if (this.tableTotal.containsKey(table)) {
                Map<OpsType, Long> acceptTotal = this.tableTotal.get(table);
                acceptTotal.forEach(fakerTable::acceptTotal);
            }

            fakerTable.apply();
            fakerTables.add(fakerTable);
        }

        return fakerTables;
    }
}
