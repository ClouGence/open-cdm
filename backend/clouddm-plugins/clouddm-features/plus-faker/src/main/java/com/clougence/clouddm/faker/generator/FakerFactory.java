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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.clouddm.faker.config.FakerConfigEnum;
import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.config.processor.DslTypeProcessorFactory;
import com.clougence.clouddm.faker.config.processor.TypeProcessorFactory;
import com.clougence.clouddm.faker.engine.SessionFactory;
import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.seed.array.ArraySeedFactory;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.BeanUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import com.clougence.utils.setting.SettingNode;
import com.clougence.utils.setting.data.TreeNode;

/**
 * FakerTable 构建器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class FakerFactory {

    private static final Logger        logger = LoggerFactory.getLogger(FakerFactory.class);
    private final FakerEngineConfig    engineConfig;
    private final SessionFactory       sessionFactory;
    @Getter
    private final Dialect              dialect;
    private final TypeProcessorFactory typeProcess;

    public FakerFactory(FakerEngineConfig engineConfig, SessionFactory sessionFactory, Dialect dialect) throws Exception{
        this.engineConfig = engineConfig;
        this.sessionFactory = sessionFactory;
        this.dialect = dialect;
        this.typeProcess = this.initTypeDialect(engineConfig);
    }

    protected Map<String, Object> initVariables(FakerEngineConfig config) throws Exception {
        Map<String, Object> javaVars = new HashMap<>();
        System.getProperties().forEach((k, v) -> {
            javaVars.put(String.valueOf(k), v);
        });

        Map<String, Object> envVars = new HashMap<>();
        System.getenv().forEach((k, v) -> envVars.put(String.valueOf(k), v));

        Map<String, Object> globalVars = new HashMap<>();
        globalVars.put("java", javaVars);
        globalVars.put("env", envVars);
        globalVars.put("dbType", config.getDsType().getTypeName());
        globalVars.put("policy", config.getPolicy());
        try (Session session = this.sessionFactory.createSession()) {
            session.executeQuery(con -> {
                Connection conn = (Connection) con;
                DatabaseMetaData metaData = conn.getMetaData();
                globalVars.put("jdbcUrl", metaData.getURL());
                globalVars.put("driverName", metaData.getDriverName());
                globalVars.put("driverVersion", metaData.getDriverVersion());
                globalVars.put("dbMajorVersion", metaData.getDatabaseMajorVersion());
                globalVars.put("dbMinorVersion", metaData.getDatabaseMinorVersion());
                globalVars.put("dbProductName", metaData.getDatabaseProductName());
                globalVars.put("dbProductVersion", metaData.getDatabaseProductVersion());
                return globalVars;
            });
        }
        return globalVars;
    }

    protected TypeProcessorFactory initTypeDialect(FakerEngineConfig config) throws Exception {
        Map<String, Object> variables = this.initVariables(config);
        if (config.getTypeProcessorFactory() != null) {
            return config.getTypeProcessorFactory();
        }

        return new DslTypeProcessorFactory(config.getDsType(), variables, config);
    }

    protected FakerEngineConfig getEngineConfig() { return this.engineConfig; }

    public List<FakerTable> fetchTable(SettingNode[] tableConfig, Map<String, Map<OpsType, Long>> tableTotal) throws Exception {
        if (tableConfig != null && tableConfig.length > 0) {
            String catalog = tableConfig[0].getSubValue(FakerConfigEnum.TABLE_CATALOG.getConfigKey());
            String schema = tableConfig[0].getSubValue(FakerConfigEnum.TABLE_SCHEMA.getConfigKey());
            List<String> tables = new ArrayList<>();
            for (SettingNode settingNode : tableConfig) {
                String tableName = settingNode.getSubValue(FakerConfigEnum.TABLE_TABLE.getConfigKey());
                tables.add(tableName);
            }
            return this.buildTable(catalog, schema, tables, tableTotal, tableConfig);
        } else {
            throw new IllegalArgumentException("SettingNode must not null.");
        }
    }

    public FakerTable fetchTable(String catalog, String schema, String table) throws Exception {
        return this.buildTable(catalog, schema, Collections.singletonList(table), null).get(0);
    }

    public List<FakerTable> buildTable(String catalog, String schema, List<String> tables, SettingNode[] tableConfigs) throws Exception {
        try (Session session = this.sessionFactory.createSession()) {
            FakerRecursiveTask task = new FakerRecursiveTask(catalog, schema, tables, Arrays.stream(tableConfigs).collect(Collectors.toList()));
            List<FakerTable> tableList = task.compute(this, session);
            if (CollectionUtils.isEmpty(tableList) || tableList.size() != tables.size()) {
                throw new IllegalArgumentException("Get table information field.");
            }
            return tableList;
        }
    }

    public List<FakerTable> buildTable(String catalog, String schema, List<String> tables,//
                                       Map<String, Map<OpsType, Long>> tableTotal, SettingNode[] tableConfigs) throws Exception {
        try (Session session = this.sessionFactory.createSession()) {
            FakerRecursiveTask task = new FakerRecursiveTask(catalog, schema, tables, Arrays.stream(tableConfigs).collect(Collectors.toList()), tableTotal);
            List<FakerTable> tableList = task.compute(this, session);
            if (CollectionUtils.isEmpty(tableList) || tableList.size() != tables.size()) {
                throw new IllegalArgumentException("Get table information field.");
            }
            return tableList;
        }
    }

    protected String[] getIgnoreCols(SettingNode node, String name) {
        List<String> cols = new ArrayList<>();
        for (SettingNode subNode : node.getSubNodes()) {
            String isIgnore = subNode.getSubValue(name);
            if (Boolean.parseBoolean(isIgnore)) {
                cols.add(subNode.getName());
            }
        }
        return cols.isEmpty() ? new String[0] : cols.toArray(new String[0]);
    }

    protected void buildColumns(FakerTable fakerTable, SettingNode columnConfig, RdbTable refTable) throws ReflectiveOperationException {
        Set<String> configColumn = Arrays.stream(columnConfig.getSubKeys()).collect(Collectors.toSet());
        Set<String> allColumn = refTable.getColumns().keySet();
        allColumn.retainAll(configColumn);
        configColumn.removeAll(allColumn);

        String[] ignoreCols = configColumn.toArray(new String[0]);
        String[] ignoreInsertCols = columnConfig.getSubValues(FakerConfigEnum.TABLE_COL_IGNORE_INSERT.getConfigKey());
        String[] ignoreUpdateCols = columnConfig.getSubValues(FakerConfigEnum.TABLE_COL_IGNORE_UPDATE.getConfigKey());
        String[] ignoreDeleteWhereCols = columnConfig.getSubValues(FakerConfigEnum.TABLE_COL_IGNORE_DELETE_WHERE.getConfigKey());
        String[] ignoreUpdateWhereCols = columnConfig.getSubValues(FakerConfigEnum.TABLE_COL_IGNORE_UPDATE_WHERE.getConfigKey());
        Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreCols));
        Set<String> ignoreInsertSet = new HashSet<>(Arrays.asList(ignoreInsertCols));
        Set<String> ignoreUpdateSet = new HashSet<>(Arrays.asList(ignoreUpdateCols));
        Set<String> ignoreDeleteWhereSet = new HashSet<>(Arrays.asList(ignoreDeleteWhereCols));
        Set<String> ignoreUpdateWhereSet = new HashSet<>(Arrays.asList(ignoreUpdateWhereCols));

        Collection<RdbColumn> columns = refTable.getColumns().values();
        if (columns.isEmpty()) {
            throw new UnsupportedOperationException(fakerTable + " no columns were found in the meta information.");
        }

        for (RdbColumn rdbColumn : columns) {
            FakerColumn fakerColumn = createFakerColumn(fakerTable, rdbColumn, columnConfig
                .findNode(rdbColumn.getName()), ignoreSet, ignoreInsertSet, ignoreUpdateSet, ignoreDeleteWhereSet, ignoreUpdateWhereSet);
            if (fakerColumn != null) {
                fakerTable.addColumn(fakerColumn);
            }
        }
    }

    private FakerColumn createFakerColumn(FakerTable fakerTable, RdbColumn rdbColumn, SettingNode columnConfig, //
                                          Set<String> ignoreSet, Set<String> ignoreInsertSet, Set<String> ignoreUpdateSet, Set<String> ignoreDeleteWhereSet,
                                          Set<String> ignoreUpdateWhereSet) throws ReflectiveOperationException {
        columnConfig = (columnConfig == null) ? new TreeNode() : columnConfig;

        // try use setting create it
        SeedFactory seedFactory = this.createSeedFactory(columnConfig);
        SeedConfig seedConfig = null;
        if (seedFactory != null) {
            seedConfig = this.createSeedConfig(seedFactory, columnConfig);
        }

        // use rdbColumn create it
        TypeProcessor typeProcessor = null;
        if (seedConfig == null) {
            try {
                typeProcessor = this.typeProcess.createSeedFactory(rdbColumn, columnConfig);
            } catch (UnsupportedOperationException e) {
                logger.error(e.getMessage());
                return null;
            }
        } else {
            typeProcessor = new TypeProcessor(seedFactory, seedConfig, rdbColumn.getSqlType().getJdbcType());
            this.typeProcess.fillSeedConfig(rdbColumn, columnConfig, typeProcessor);
        }

        // final apply form strategy
        if (!rdbColumn.hasConstraint(GeneralConstraintType.NonNull)) {
            SeedConfig config = typeProcessor.getSeedConfig();
            config.setAllowNullable(true);
            if (config.getNullableRatio() == null) {
                config.setNullableRatio(20f);
            }
        }

        // final apply form config
        Class<?> configClass = typeProcessor.getConfigType();
        List<String> properties = BeanUtils.getProperties(configClass);
        Map<String, Class<?>> propertiesMap = BeanUtils.getPropertyType(configClass);

        String[] configProperties = columnConfig.getSubKeys();
        for (String property : configProperties) {
            Object[] propertyValue = columnConfig.getSubValues(property);
            if (propertyValue == null || propertyValue.length == 0) {
                continue;
            }

            Object writeValue = (propertyValue.length == 1) ? propertyValue[0] : propertyValue;
            typeProcessor.putConfig(property, writeValue);

            Class<?> propertyType = propertiesMap.get(property);
            if (propertyType != null && propertyType.isArray()) {
                writeValue = propertyValue;
            }

            if (properties.contains(property)) {
                typeProcessor.writeProperty(property, writeValue);
            }
        }

        Set<UseFor> ignoreAct = new HashSet<>(typeProcessor.getDefaultIgnoreAct());
        // if all ignore why you choose the field?
        ignoreAct.addAll(ignoreSet.contains(rdbColumn.getName()) ? Arrays.asList(UseFor.values()) : Collections.emptySet());
        ignoreAct.addAll(ignoreInsertSet.contains(rdbColumn.getName()) ? Collections.singletonList(UseFor.Insert) : Collections.emptySet());
        ignoreAct.addAll(ignoreUpdateSet.contains(rdbColumn.getName()) ? Collections.singletonList(UseFor.UpdateSet) : Collections.emptySet());
        ignoreAct.addAll(ignoreDeleteWhereSet.contains(rdbColumn.getName()) ? Collections.singletonList(UseFor.DeleteWhere) : Collections.emptySet());
        ignoreAct.addAll(ignoreUpdateWhereSet.contains(rdbColumn.getName()) ? Collections.singletonList(UseFor.UpdateWhere) : Collections.emptySet());

        return new FakerColumn(fakerTable, rdbColumn, typeProcessor, ignoreAct, this, columnConfig);
    }

    private SeedConfig createSeedConfig(SeedFactory seedFactory, SettingNode columnConfig) {
        SeedConfig seedConfig = seedFactory.newConfig();
        for (String subKey : columnConfig.getSubKeys()) {
            String[] subValue = columnConfig.getSubValues(subKey);
            if (subValue == null || subValue.length == 0) {
                continue;
            }
            if (subValue.length == 1) {
                seedConfig.getConfigMap().put(subKey, subValue[0]);
            } else {
                seedConfig.getConfigMap().put(subKey, Arrays.asList(subValue));
            }
        }
        return seedConfig;
    }

    private SeedFactory createSeedFactory(SettingNode columnConfig) throws ReflectiveOperationException {
        String seedFactoryStr = columnConfig == null ? null : columnConfig.getSubValue(FakerConfigEnum.COLUMN_SEED_FACTORY.getConfigKey());
        if (StringUtils.isNotBlank(seedFactoryStr)) {
            Class<?> seedFactoryType = this.engineConfig.getClassLoader().loadClass(seedFactoryStr);
            return (SeedFactory) seedFactoryType.newInstance();
        }

        String array = columnConfig == null ? null : columnConfig.getSubValue(FakerConfigEnum.COLUMN_ARRAY_TYPE.getConfigKey());
        String seedTypeStr = columnConfig == null ? null : columnConfig.getSubValue(FakerConfigEnum.COLUMN_SEED_TYPE.getConfigKey());
        SeedType seedType = SeedType.valueOfCode(seedTypeStr);
        boolean isArray = StringUtils.isNotBlank(array) && Boolean.parseBoolean(array);

        if (seedType == SeedType.Custom) {
            throw new IllegalArgumentException("custom seedType must config seedFactory.");
        } else if (seedType == SeedType.Array) {
            throw new IllegalArgumentException("arrays are specified by config.");
        }

        SeedFactory<? extends SeedConfig> factory = seedType != null ? seedType.newFactory() : null;
        if (isArray) {
            return factory == null ? null : new ArraySeedFactory(factory);
        } else {
            return factory;
        }
    }
}
