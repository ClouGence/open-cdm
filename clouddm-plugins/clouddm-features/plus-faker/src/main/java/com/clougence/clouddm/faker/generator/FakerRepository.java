/*
 * Copyright 2015-2022 the original author or authors.
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

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import com.clougence.utils.setting.DefaultSettings;
import com.clougence.utils.setting.SettingNode;
import com.clougence.utils.setting.provider.StreamType;

/**
 * FakerGenerator
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
@Getter
public class FakerRepository {

    private static final Logger     logger = ToolUtils.getLoggerAppender();
    private final String            generatorID;
    private final FakerEngineConfig engineConfig;
    private final FakerFactory      fakerFactory;
    private final List<FakerTable>  generatorTables;

    public FakerRepository(FakerFactory fakerFactory){
        this.generatorID = UUID.randomUUID().toString().replace("-", "");
        this.engineConfig = fakerFactory.getEngineConfig();
        this.fakerFactory = fakerFactory;
        this.generatorTables = new CopyOnWriteArrayList<>();
    }

    public void removeTable(String catalog, String schema, String table) {
        FakerTable removeObj = null;
        for (FakerTable tableObj : this.generatorTables) {
            boolean matchCatalog = StringUtils.equals(tableObj.getCatalog(), catalog);
            boolean matchSchema = StringUtils.equals(tableObj.getSchema(), schema);
            boolean matchTable = StringUtils.equals(tableObj.getTable(), table);
            if (matchCatalog && matchSchema && matchTable) {
                removeObj = tableObj;
                break;
            }
        }

        if (removeObj != null) {
            this.generatorTables.remove(removeObj);
        }
    }

    /** 从生成器中随机选择一张 fakerTable 表，并为这张表生成一个事务的语句。语句类型随机 */
    public List<BoundQuery> generator(GenCondition condition, Session session) throws Exception {
        return this.generator(condition, session, this.engineConfig.randomOps());
    }

    /** 从生成器中随机选择一张 fakerTable 表，并为这张表生成一个事务的语句。语句类型由 opsType 决定 */
    public List<BoundQuery> generator(GenCondition condition, Session session, OpsType opsType) throws Exception {
        FakerTable table = randomTable(condition);
        if (table == null) {
            return Collections.emptyList();
        }

        List<BoundQuery> events = new LinkedList<>();
        int opsCountPerTransaction = this.engineConfig.randomOpsCountPerTrans();
        for (int i = 0; i < opsCountPerTransaction; i++) {
            List<BoundQuery> dataSet = this.generatorOps(condition, session, table, opsType);
            events.addAll(dataSet);
        }
        return events;
    }

    /** 为 fakerTable 生成一批 opsType 类型 DML 语句 */
    protected List<BoundQuery> generatorOps(GenCondition condition, Session session, FakerTable fakerTable, OpsType opsType) throws Exception {
        Objects.requireNonNull(fakerTable, "fakerTable is null.");
        Objects.requireNonNull(opsType, "opsType is null.");

        int batchSize = this.engineConfig.randomBatchSizePerOps();
        switch (opsType) {
            case Insert:
                return fakerTable.buildInsert(session, batchSize);
            case Update:
                return fakerTable.buildUpdate(session, batchSize);
            case Delete:
                return fakerTable.buildDelete(session, batchSize);
            default:
                return Collections.emptyList();
        }
    }

    /** 从生成器中随机选择一张 fakerTable 表 */
    protected FakerTable randomTable(GenCondition condition) {
        if (!CollectionUtils.isEmpty(this.generatorTables)) {
            FakerTable fakerTable;
            if (this.generatorTables.size() == 1) {
                fakerTable = this.generatorTables.get(0);
            } else {
                fakerTable = this.generatorTables.get(RandomUtils.nextInt(0, this.generatorTables.size()));
            }
            return fakerTable;
        }
        return null;
    }

    /** 添加一个表到生成器中，表的列信息通过元信息服务来补全。 */
    public FakerTable addTable(String catalog, String schema, String table) throws Exception {
        FakerTable fetchTable = this.fakerFactory.fetchTable(catalog, schema, table);
        this.addTable(fetchTable);
        return fetchTable;
    }

    /** 添加一个表到生成器中 */
    protected FakerTable addTable(FakerTable table) {
        this.generatorTables.add(table);
        return table;
    }

    /** 从生成器中查找某个表 */
    public FakerTable findTable(String catalog, String schema, String table) {
        return this.generatorTables.stream().filter(fakerTable -> {
            return StringUtils.equals(fakerTable.getCatalog(), catalog) && //
                   StringUtils.equals(fakerTable.getSchema(), schema) && //
                   StringUtils.equals(fakerTable.getTable(), table);
        }).findFirst().orElse(null);
    }

    public void loadConfig(String config, StreamType streamType, Map<String, Map<OpsType, Long>> tableTotal) throws Exception {
        DefaultSettings settings = new DefaultSettings();
        settings.loadStringBody(config, streamType);
        SettingNode[] tables = settings.getNodeArray("config.table");
        if (tables != null) {
            List<FakerTable> fakerTables = this.fakerFactory.fetchTable(tables, tableTotal);
            for (FakerTable fakerTable : fakerTables) {
                if (fakerTable != null) {
                    String tableName = DefaultDialect.DEFAULT.fmtTableName(true, fakerTable.getCatalog(), fakerTable.getSchema(), fakerTable.getTable());
                    this.addTable(fakerTable);
                    logger.info("found table '" + tableName + "'");
                }
            }
        }
    }

}
