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
 * UPDATE 生成器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class UpdateAction extends AbstractAction {

    private final List<FakerColumn> updateSetColumns;
    private final List<FakerColumn> whereFullColumns;
    private final List<FakerColumn> whereKeyColumns;

    public UpdateAction(FakerTable tableInfo, Dialect dialect, DataLoader dataLoader, List<FakerColumn> updateSetColumns, List<FakerColumn> whereColumns){
        super(tableInfo, dialect, dataLoader);
        this.updateSetColumns = updateSetColumns;
        this.whereFullColumns = whereColumns;
        this.whereKeyColumns = whereColumns.stream().filter(FakerColumn::isKey).collect(Collectors.toList());
    }

    public List<FakerColumn> getSetColumns() {
        List<FakerColumn> setColumns = null;
        switch (this.tableInfo.getUpdateSetPolitic()) {
            case RandomKeyCol:
            case RandomCol: {
                setColumns = new ArrayList<>(this.updateSetColumns);
                if (!this.updateSetColumns.isEmpty()) {
                    List<FakerColumn> cutColumns = randomCol(this.updateSetColumns);
                    setColumns.removeAll(cutColumns);
                }

                // maker sure is not empty set.
                if (setColumns.isEmpty() && !this.updateSetColumns.isEmpty()) {
                    if (this.updateSetColumns.size() == 1) {
                        setColumns.add(this.updateSetColumns.get(0));
                    } else {
                        setColumns.add(this.updateSetColumns.get(RandomUtils.nextInt(0, this.updateSetColumns.size() - 1)));
                    }
                }
                break;
            }
            case KeyCol:
            case FullCol:
                setColumns = this.updateSetColumns;
                break;
            default:
                throw new UnsupportedOperationException("updateSetPolitic '" + this.tableInfo.getInsertPolitic() + "' Unsupported.");
        }
        return setColumns;
    }

    public List<FakerColumn> getWhereColumns() {
        List<FakerColumn> whereColumns = null;
        switch (this.tableInfo.getWherePolitic()) {
            case RandomKeyCol:
                if (!this.whereKeyColumns.isEmpty()) {
                    whereColumns = randomCol(whereKeyColumns);
                    break;
                }
            case RandomCol:
                whereColumns = randomCol(whereFullColumns);
                break;
            case KeyCol:
                if (!this.whereKeyColumns.isEmpty()) {
                    whereColumns = whereKeyColumns;
                    break;
                }
            case FullCol:
                whereColumns = whereFullColumns;
                break;
            default:
                throw new UnsupportedOperationException("updateWherePolitic '" + this.tableInfo.getWherePolitic() + "' Unsupported.");
        }
        return whereColumns;
    }

    @Override
    public List<BoundQuery> generatorAction(Session session, int batchSize) throws Exception {
        List<FakerColumn> setColumns = getSetColumns();
        List<FakerColumn> whereColumns = getWhereColumns();

        return buildAction(session, batchSize, setColumns, whereColumns);
    }

    private List<FakerColumn> randomCol(List<FakerColumn> useCols) {
        List<FakerColumn> useColumns = new ArrayList<>(useCols);

        int maxCut = RandomUtils.nextInt(0, useColumns.size());
        for (int i = 0; i < maxCut; i++) {
            if (useColumns.size() == 1) {
                useColumns.remove(0);
            } else {
                useColumns.remove(RandomUtils.nextInt(0, useColumns.size() - 1));
            }
        }

        // maker sure is not empty delete.
        if (useColumns.isEmpty() && !useCols.isEmpty()) {
            if (useCols.size() == 1) {
                useColumns.add(useCols.get(0));
            } else {
                useColumns.add(useCols.get(RandomUtils.nextInt(0, useCols.size() - 1)));
            }
        }

        return useColumns;
    }

    private List<BoundQuery> buildAction(Session session, int batchSize, List<FakerColumn> setColumns, List<FakerColumn> whereColumns) throws Exception {

        // build delete sql
        String catalog = this.tableInfo.getCatalog();
        String schema = this.tableInfo.getSchema();
        String table = this.tableInfo.getTable();
        String tableName = this.dialect.fmtTableName(this.useQualifier, catalog, schema, table);

        // build set
        StringBuilder set = new StringBuilder();
        for (FakerColumn colInfo : setColumns) {
            if (set.length() > 0) {
                set.append(", ");
            }
            set.append(this.dialect.fmtName(this.useQualifier, colInfo.getColumn()));
            set.append(" = ");
            set.append(colInfo.getSetValueTemplate());
        }

        // build where
        StringBuilder where = new StringBuilder();
        for (FakerColumn colInfo : whereColumns) {
            if (where.length() > 0) {
                where.append(" and ");
            }
            where.append(colInfo.getWhereColTemplate());
            where.append(" = ");
            where.append(colInfo.getWhereValueTemplate());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("update " + tableName);
        builder.append(" set " + set);
        builder.append(" where " + where);

        List<BoundQuery> boundQueries = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            SqlArg[] args = new SqlArg[setColumns.size() + whereColumns.size()];
            int index = 0;
            for (FakerColumn colInfo : setColumns) {
                args[index++] = colInfo.generatorData();
            }
            for (FakerColumn colInfo : whereColumns) {
                args[index++] = colInfo.generatorData();
            }

            boundQueries.add(new BoundQuery(this.tableInfo, OpsType.Update, builder, args, 1));
        }
        return boundQueries;
    }

    @Override
    public String toString() {
        SqlPolitic updatePolitic = this.tableInfo.getUpdateSetPolitic();
        String setCols = "'" + StringUtils.join(logCols(this.updateSetColumns), "','") + "'";
        switch (updatePolitic) {
            case KeyCol:
            case RandomKeyCol:
                String whereKCols = "'" + StringUtils.join(logCols(this.whereKeyColumns), "','") + "'";
                return "UpdateAct{politic='" + updatePolitic + "'," + "setCols= [" + setCols + "], whereCols= [" + whereKCols + "]}";
            case FullCol:
            case RandomCol:
                String whereFCols = "'" + StringUtils.join(logCols(this.whereFullColumns), "','") + "'";
                return "UpdateAct{politic='" + updatePolitic + "'," + "setCols= [" + setCols + "], whereCols= [" + whereFCols + "]}";
            default:
                return "UpdateAct{politic='" + updatePolitic + "'," + "setCols= [" + setCols + "], whereCols= 'unknown'}";
        }
    }
}
