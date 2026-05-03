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
package com.clougence.clouddm.faker.generator.action;

import java.util.ArrayList;
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
 * DELETE 生成器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class DeleteAction extends AbstractAction {

    private final List<FakerColumn> whereFullCols;
    private final List<FakerColumn> whereKeyCols;

    public DeleteAction(FakerTable tableInfo, Dialect dialect, DataLoader dataLoader, List<FakerColumn> whereColumns){
        super(tableInfo, dialect, dataLoader);
        this.whereFullCols = whereColumns;
        this.whereKeyCols = whereColumns.stream().filter(FakerColumn::isKey).collect(Collectors.toList());
    }

    @Override
    public List<BoundQuery> generatorAction(Session session, int batchSize) {
        return buildAction(session, batchSize, getWhereCols());
    }

    public List<FakerColumn> getWhereCols() {
        switch (this.tableInfo.getWherePolitic()) {
            case RandomKeyCol: {
                if (!this.whereKeyCols.isEmpty()) {
                    List<FakerColumn> useColumns = new ArrayList<>(this.whereKeyCols);

                    int maxCut = RandomUtils.nextInt(0, useColumns.size());
                    for (int i = 0; i < maxCut; i++) {
                        useColumns.remove(RandomUtils.nextInt(0, useColumns.size() - 1));
                    }
                    // maker sure is not empty delete.
                    if (useColumns.isEmpty() && !this.whereKeyCols.isEmpty()) {
                        if (this.whereKeyCols.size() == 1) {
                            useColumns.add(this.whereKeyCols.get(0));
                        } else {
                            useColumns.add(this.whereKeyCols.get(RandomUtils.nextInt(0, this.whereKeyCols.size() - 1)));
                        }
                    }
                    return useColumns;
                }
            }
            case RandomCol: {
                List<FakerColumn> useColumns = new ArrayList<>(this.whereFullCols);

                int maxCut = RandomUtils.nextInt(0, useColumns.size());
                for (int i = 0; i < maxCut; i++) {
                    useColumns.remove(RandomUtils.nextInt(0, useColumns.size() - 1));
                }

                // maker sure is not empty delete.
                if (useColumns.isEmpty() && !this.whereFullCols.isEmpty()) {
                    if (this.whereFullCols.size() == 1) {
                        useColumns.add(this.whereFullCols.get(0));
                    } else {
                        useColumns.add(this.whereFullCols.get(RandomUtils.nextInt(0, this.whereFullCols.size() - 1)));
                    }
                }
                return useColumns;
            }
            case KeyCol:
                if (!this.whereKeyCols.isEmpty()) {
                    return this.whereKeyCols;
                }
            case FullCol:
                return this.whereFullCols;
            default:
                throw new UnsupportedOperationException("deletePolitic '" + this.tableInfo.getWherePolitic() + "' Unsupported.");
        }
    }

    private List<BoundQuery> generatorByRandomCol(Session session, int batchSize, List<FakerColumn> useCols) {
        List<FakerColumn> useColumns = new ArrayList<>(useCols);

        int maxCut = RandomUtils.nextInt(0, useColumns.size());
        for (int i = 0; i < maxCut; i++) {
            useColumns.remove(RandomUtils.nextInt(0, useColumns.size() - 1));
        }

        // maker sure is not empty delete.
        if (useColumns.isEmpty() && !useCols.isEmpty()) {
            if (useCols.size() == 1) {
                useColumns.add(useCols.get(0));
            } else {
                useColumns.add(useCols.get(RandomUtils.nextInt(0, useCols.size() - 1)));
            }
        }

        return buildAction(session, batchSize, useColumns);
    }

    private List<BoundQuery> buildAction(Session session, int batchSize, List<FakerColumn> useColumns) {

        // build delete sql
        String catalog = this.tableInfo.getCatalog();
        String schema = this.tableInfo.getSchema();
        String table = this.tableInfo.getTable();
        String tableName = this.dialect.fmtTableName(this.useQualifier, catalog, schema, table);

        StringBuilder where = new StringBuilder();
        for (FakerColumn colInfo : useColumns) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            where.append(colInfo.getWhereColTemplate());
            where.append(" = ");
            where.append(colInfo.getWhereValueTemplate());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ");
        builder.append(tableName);
        builder.append(" where " + where);

        // build args
        List<BoundQuery> boundQueries = new ArrayList<>();
        for (int j = 0; j < batchSize; j++) {
            SqlArg[] args = new SqlArg[useColumns.size()];
            for (int i = 0; i < useColumns.size(); i++) {
                FakerColumn colInfo = useColumns.get(i);
                args[i] = colInfo.generatorData();
            }

            boundQueries.add(new BoundQuery(this.tableInfo, OpsType.Delete, builder, args, 1));
        }
        return boundQueries;
    }

    @Override
    public String toString() {
        SqlPolitic wherePolitic = this.tableInfo.getWherePolitic();
        switch (wherePolitic) {
            case KeyCol:
            case RandomKeyCol: {
                String whereCols = "'" + StringUtils.join(logCols(this.whereKeyCols), "','") + "'";
                return "DelAct{politic='" + wherePolitic + "'," + "whereKCols= [" + whereCols + "]}";
            }
            case FullCol:
            case RandomCol: {
                String whereCols = "'" + StringUtils.join(logCols(this.whereFullCols), "','") + "'";
                return "DelAct{politic='" + wherePolitic + "'," + "whereKCols= [" + whereCols + "]}";
            }
            default:
                return "DelAct{politic='" + wherePolitic + "', whereCols= 'unknown'}";
        }
    }
}
