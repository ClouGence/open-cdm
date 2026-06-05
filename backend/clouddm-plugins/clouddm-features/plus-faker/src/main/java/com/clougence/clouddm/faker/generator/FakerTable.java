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

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.config.SqlPolitic;
import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.generator.action.Action;
import com.clougence.clouddm.faker.generator.action.DeleteAction;
import com.clougence.clouddm.faker.generator.action.InsertAction;
import com.clougence.clouddm.faker.generator.action.UpdateAction;
import com.clougence.clouddm.faker.generator.loader.DataLoader;
import com.clougence.clouddm.faker.generator.loader.DataLoaderFactory;
import com.clougence.clouddm.faker.generator.loader.DefaultDataLoaderFactory;
import com.clougence.clouddm.faker.utils.RandomRatio;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.model.faker.FakerLineDTO;
import com.clougence.schema.dialect.Dialect;

import lombok.Getter;
import lombok.Setter;

/**
 * 要生成数据的表基本信息和配置信息
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class FakerTable {

    @Getter
    private final String                   catalog;
    @Getter
    private final String                   schema;
    @Getter
    private final String                   table;
    private final Map<String, FakerColumn> columnMap;
    private final List<FakerColumn>        columnList;
    @Getter
    private final FakerFactory             factory;
    //
    @Setter
    @Getter
    private SqlPolitic                     insertPolitic;
    @Setter
    @Getter
    private SqlPolitic                     updateSetPolitic;
    @Setter
    @Getter
    private SqlPolitic                     wherePolitic;
    private Action                         insertGenerator;
    private Action                         updateGenerator;
    private Action                         deleteGenerator;
    @Getter
    private final Map<OpsType, Long>       acceptTotal = new HashMap<>();
    @Setter
    @Getter
    private boolean                        useQualifier;
    @Setter
    @Getter
    private boolean                        keyChanges;
    @Getter
    private boolean                        hasKey;
    @Getter
    @Setter
    private boolean                        batchInsert;

    FakerTable(String catalog, String schema, String table, FakerFactory factory){
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.columnMap = new LinkedHashMap<>();
        this.columnList = new ArrayList<>();
        this.factory = factory;
        //        this.insertPolitic = SqlPolitic.RandomCol;
        //        this.updateSetPolitic = SqlPolitic.RandomCol;
        //        this.wherePolitic = SqlPolitic.KeyCol;
        for (OpsType opsType : OpsType.values()) {
            this.acceptTotal.put(opsType, Long.MAX_VALUE);
        }
        this.useQualifier = true;
        this.hasKey = false;
    }

    public void acceptTotal(OpsType opsType, long accept) {
        this.acceptTotal.put(opsType, accept);
    }

    /** 添加一个列 */
    public void addColumn(FakerColumn fakerColumn) {
        this.columnMap.put(fakerColumn.getColumn(), fakerColumn);
        this.columnList.add(fakerColumn);
        this.hasKey = this.hasKey | fakerColumn.isKey();
    }

    /** 获取所有列 */
    public List<String> getColumns() { return this.columnList.stream().map(FakerColumn::getColumn).collect(Collectors.toList()); }

    /** 查找某个列 */
    public FakerColumn findColumn(String columnName) {
        return this.columnMap.get(columnName);
    }

    /** 应用最新配置，并且创建 IUD 生成器 */
    public void apply() {
        List<FakerColumn> insertColumns = new ArrayList<>();
        List<FakerColumn> updateSetColumns = new ArrayList<>();
        List<FakerColumn> updateWhereColumns = new ArrayList<>();
        List<FakerColumn> deleteWhereColumns = new ArrayList<>();

        for (FakerColumn fakerColumn : this.columnList) {
            if (fakerColumn.isGenerator(UseFor.Insert)) {
                insertColumns.add(fakerColumn);
            }
            if (fakerColumn.isGenerator(UseFor.UpdateSet)) {
                if (!(fakerColumn.isKey() && !this.keyChanges)) {
                    updateSetColumns.add(fakerColumn);
                }
            }
            if (fakerColumn.isGenerator(UseFor.UpdateWhere)) {
                updateWhereColumns.add(fakerColumn);
            }
            if (fakerColumn.isGenerator(UseFor.DeleteWhere)) {
                deleteWhereColumns.add(fakerColumn);
            }
            fakerColumn.applyConfig();
        }

        Dialect dialect = this.factory.getDialect();
        DataLoader dataLoader = createDataLoader();

        this.insertGenerator = new InsertAction(this, dialect, dataLoader, insertColumns);
        this.updateGenerator = new UpdateAction(this, dialect, dataLoader, updateSetColumns, updateWhereColumns);
        this.deleteGenerator = new DeleteAction(this, dialect, dataLoader, deleteWhereColumns);
    }

    private DataLoader createDataLoader() {
        DataLoaderFactory dataLoaderFactory = this.factory.getEngineConfig().getDataLoaderFactory();
        dataLoaderFactory = dataLoaderFactory == null ? new DefaultDataLoaderFactory() : dataLoaderFactory;
        return dataLoaderFactory.createDataLoader(this.factory.getEngineConfig());
    }

    /** 生成一批 insert，每批语句都是相同的语句模版 */
    public List<BoundQuery> buildInsert(Session session, int batchSize) throws Exception {
        if (this.acceptTotal.get(OpsType.Insert) == 0) {
            return Collections.emptyList();
        } else {
            return this.insertGenerator.generatorAction(session, batchSize);
        }
    }

    /** 生成一批 update，每批语句都是相同的语句模版 */
    protected List<BoundQuery> buildUpdate(Session session, int batchSize) throws Exception {
        if (this.acceptTotal.get(OpsType.Update) == 0) {
            return Collections.emptyList();
        } else {
            return this.updateGenerator.generatorAction(session, batchSize);
        }
    }

    /** 生成一批 delete，每批语句都是相同的语句模版 */
    protected List<BoundQuery> buildDelete(Session session, int batchSize) throws Exception {
        if (this.acceptTotal.get(OpsType.Delete) == 0) {
            return Collections.emptyList();
        } else {
            return this.deleteGenerator.generatorAction(session, batchSize);
        }
    }

    public List<FakerLineDTO> buildPreview(int batchSize) {
        List<FakerLineDTO> preview = new ArrayList<>();

        List<String> deleteWhereCols = this.getDeleteWhereCols();
        List<String> updateSetCols = this.getUpdateSetCols();
        List<String> updateWhereCols = this.getUpdateWhereCols();

        List<FakerColumn> insertCols = this.getInsertCols();

        RandomRatio<OpsType> opsRatio = factory.getEngineConfig().getOpsRatio();
        for (int i = 0; i < batchSize; i++) {
            // Returns the corresponding amount of presentation data based on the set ratio
            switch (opsRatio.getByRandom()) {
                case Update: {
                    Map<String, String> oldValue = this.buildValue(this.columnList);
                    Map<String, String> newValue = this.buildValue(this.columnList);
                    // make sure just setCols modified
                    for (String name : oldValue.keySet()) {
                        if (!updateSetCols.contains(name)) {
                            newValue.put(name, oldValue.get(name));
                        }
                    }
                    FakerLineDTO line = new FakerLineDTO(oldValue, newValue, OpsType.Update.name());
                    line.setUseWhere(updateWhereCols);
                    line.setUseSet(updateSetCols);
                    preview.add(line);
                    break;
                }
                case Insert: {
                    Map<String, String> newValue = this.buildValue(insertCols);
                    FakerLineDTO line = new FakerLineDTO(null, newValue, OpsType.Insert.name());
                    preview.add(line);
                    break;
                }
                case Delete: {
                    Map<String, String> oldValue = this.buildValue(this.columnList);
                    FakerLineDTO line = new FakerLineDTO(oldValue, null, OpsType.Delete.name());
                    line.setUseWhere(deleteWhereCols);
                    preview.add(line);
                    break;
                }
            }
        }
        return preview;
    }

    private List<String> getUpdateWhereCols() {
        UpdateAction updateGenerator = (UpdateAction) this.updateGenerator;
        List<String> cols = new ArrayList<>();
        for (FakerColumn whereColumn : updateGenerator.getWhereColumns()) {
            cols.add(whereColumn.getColumn());
        }
        return cols;
    }

    private List<String> getUpdateSetCols() {
        UpdateAction updateGenerator = (UpdateAction) this.updateGenerator;
        List<String> cols = new ArrayList<>();
        for (FakerColumn whereColumn : updateGenerator.getSetColumns()) {
            cols.add(whereColumn.getColumn());
        }
        return cols;
    }

    private List<String> getDeleteWhereCols() {
        DeleteAction deleteGenerator = (DeleteAction) this.deleteGenerator;
        List<String> cols = new ArrayList<>();
        for (FakerColumn whereColumn : deleteGenerator.getWhereCols()) {
            cols.add(whereColumn.getColumn());
        }
        return cols;
    }

    private List<FakerColumn> getInsertCols() {
        InsertAction insertAction = (InsertAction) this.insertGenerator;
        return insertAction.getInsertCols();
    }

    private Map<String, String> buildValue(List<FakerColumn> cols) {
        Map<String, String> map = new HashMap<>();
        for (FakerColumn fakerColumn : cols) {
            SqlArg sqlArg = fakerColumn.generatorData();
            if (sqlArg.getObject() != null) {
                map.put(fakerColumn.getColumn(), sqlArg.getObject().toString());
            }
        }

        return map;
    }

    @Override
    public String toString() {
        return "{catalog='" + this.catalog + "', schema='" + this.schema + "', table='" + this.table + "'}";
    }
}
