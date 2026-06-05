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
package com.clougence.clouddm.faker.generator.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.config.SqlPolitic;
import com.clougence.clouddm.faker.generator.BoundQuery;
import com.clougence.clouddm.faker.generator.FakerColumn;
import com.clougence.clouddm.faker.generator.FakerTable;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.faker.generator.loader.DataLoader;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.StringUtils;

/**
 * INSERT 生成器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class InsertAction extends AbstractAction {

    private final List<FakerColumn> insertColumns;
    private final List<FakerColumn> canCutColumns;

    public InsertAction(FakerTable tableInfo, Dialect dialect, DataLoader dataLoader, List<FakerColumn> insertColumns){
        super(tableInfo, dialect, dataLoader);
        this.insertColumns = insertColumns;
        this.canCutColumns = insertColumns.stream().filter(FakerColumn::isCanBeCut).collect(Collectors.toList());
        //Disrupt the original order
        Collections.shuffle(this.canCutColumns);
    }

    @Override
    public List<BoundQuery> generatorAction(Session session, int batchSize) {
        return this.buildAction(batchSize, getInsertCols());
    }

    public List<FakerColumn> getInsertCols() {
        switch (this.tableInfo.getInsertPolitic()) {
            case KeyCol:
            case RandomKeyCol:
            case RandomCol:
                return generatorByRandom();
            case FullCol:
                return generatorByFull();
            default:
                throw new UnsupportedOperationException("insertPolitic '" + this.tableInfo.getInsertPolitic() + "' Unsupported.");
        }
    }

    private List<FakerColumn> generatorByRandom() {
        // try use cut
        List<FakerColumn> useColumns = new ArrayList<>(this.insertColumns);
        List<FakerColumn> cutColumns = new ArrayList<>();

        int maxCut = RandomUtils.nextInt(0, this.canCutColumns.size() - 1);
        while (cutColumns.size() < maxCut) {
            FakerColumn cutColumn;
            if (this.canCutColumns.size() == 1) {
                cutColumn = this.canCutColumns.get(0);
            } else {
                cutColumn = this.canCutColumns.get(RandomUtils.nextInt(0, maxCut));
            }

            if (!cutColumns.contains(cutColumn)) {
                cutColumns.add(cutColumn);
            }
        }
        useColumns.removeAll(cutColumns);

        // maker sure is not empty insert.
        if (useColumns.isEmpty() && !this.canCutColumns.isEmpty()) {
            if (this.canCutColumns.size() == 1) {
                useColumns.add(this.canCutColumns.get(0));
            } else {
                useColumns.add(this.canCutColumns.get(RandomUtils.nextInt(0, this.canCutColumns.size() - 1)));
            }
        }
        return useColumns;
        //        return buildAction(batchSize, useColumns);
    }

    private List<FakerColumn> generatorByFull() {
        return this.insertColumns;
        //        return buildAction(batchSize, this.insertColumns);
    }

    private List<BoundQuery> buildAction(int batchSize, List<FakerColumn> useColumns) {
        String catalog = this.tableInfo.getCatalog();
        String schema = this.tableInfo.getSchema();
        String table = this.tableInfo.getTable();
        String tableName = this.dialect.fmtTableName(this.useQualifier, catalog, schema, table);

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (FakerColumn colInfo : useColumns) {
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }
            String colName = colInfo.getColumn();
            columns.append(this.dialect.fmtName(this.useQualifier, colName));
            values.append(colInfo.getInsertTemplate());
        }

        StringBuilder builder = new StringBuilder();
        builder.append("insert into " + tableName);
        builder.append("(" + columns + ")");
        builder.append(" values ");
        List<BoundQuery> boundQueries;
        if (this.tableInfo.isBatchInsert()) {
            boundQueries = generatorBatch(batchSize, useColumns, values, builder);
        } else {
            boundQueries = generator(batchSize, useColumns, builder, values);
        }
        return boundQueries;
    }

    private List<BoundQuery> generator(int batchSize, List<FakerColumn> useColumns, StringBuilder builder, StringBuilder values) {
        builder.append("(" + values + ")");

        List<BoundQuery> boundQueries = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            SqlArg[] args = new SqlArg[useColumns.size()];
            for (int argIdx = 0; argIdx < useColumns.size(); argIdx++) {
                FakerColumn colInfo = useColumns.get(argIdx);
                args[argIdx] = colInfo.generatorData();
            }
            boundQueries.add(new BoundQuery(this.tableInfo, OpsType.Insert, builder, args, 1));
        }
        return boundQueries;
    }

    private List<BoundQuery> generatorBatch(int batchSize, List<FakerColumn> useColumns, StringBuilder values, StringBuilder builder) {
        List<String> valueList = new ArrayList<>();

        List<BoundQuery> boundQueries = new ArrayList<>();
        SqlArg[] args = new SqlArg[useColumns.size() * batchSize];
        for (int i = 0; i < batchSize; i++) {
            valueList.add("(" + values + ")");
        }
        for (int argIdx = 0; argIdx < useColumns.size() * batchSize; argIdx++) {
            FakerColumn colInfo = useColumns.get(argIdx % useColumns.size());
            args[argIdx] = colInfo.generatorData();
        }
        builder.append(String.join(",", valueList));
        boundQueries.add(new BoundQuery(this.tableInfo, OpsType.Insert, builder, args, batchSize));
        return boundQueries;
    }

    @Override
    public String toString() {
        SqlPolitic insertPolitic = this.tableInfo.getInsertPolitic();
        String insertCols = "'" + StringUtils.join(logCols(this.insertColumns), "','") + "'";
        return "InsertAct{politic='" + insertPolitic + "'," + "insertCols= [" + insertCols + "]}";
    }
}
