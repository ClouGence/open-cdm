/*
 * Copyright 2015-2022 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.clougence.clouddm.faker.generator.loader;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.SqlPolitic;
import com.clougence.clouddm.faker.generator.FakerColumn;
import com.clougence.clouddm.faker.generator.FakerTable;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.faker.utils.QueryUtils;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.RowMapper;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;

/**
 * 反查数据加载器
 *
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class DefaultDataLoaderFactory implements DataLoaderFactory {

    @Override
    public DataLoader createDataLoader(FakerEngineConfig fakerConfig) {
        return (session, useFor, fakerTable, batchSize) -> {
            boolean onlyKey = fakerTable.getWherePolitic() == SqlPolitic.KeyCol || fakerTable.getWherePolitic() == SqlPolitic.RandomKeyCol && fakerTable.isHasKey();
            List<String> includeColumns = new ArrayList<>();
            Map<String, String> includeColumnTerms = new HashMap<>();

            for (String col : fakerTable.getColumns()) {
                FakerColumn column = fakerTable.findColumn(col);
                if (onlyKey && !column.isKey()) {
                    continue;
                }

                String template = column.getSelectTemplate();
                includeColumns.add(col);
                if (StringUtils.isNotBlank(template) && !StringUtils.equals(template, col)) {
                    includeColumnTerms.put(column.getColumn(), template);
                }
            }

            DataSourceType dbType = fakerConfig.getDsType();
            switch (fakerConfig.getRandomMode()) {
                case RandomQuery:
                    return loadForRandomQuery(dbType, session, fakerTable, includeColumns, includeColumnTerms, batchSize);
                case RandomData:
                    return loadForRandomData(dbType, session, fakerTable, includeColumns, includeColumnTerms, batchSize);
                default:
                    throw new UnsupportedOperationException("RandomMode '" + fakerConfig.getRandomMode() + "' Unsupported.");
            }
        };
    }

    protected List<Map<String, SqlArg>> loadForRandomQuery(DataSourceType dbType, Session session, FakerTable fakerTable, //
                                                           List<String> includeColumns, Map<String, String> includeColumnTerms, int batchSize) throws Exception {
        return session.executeQuery(con -> {
            boolean useQualifier = fakerTable.isUseQualifier();
            String catalog = fakerTable.getCatalog();
            String schema = fakerTable.getSchema();
            String table = fakerTable.getTable();
            Dialect dialect = fakerTable.getFactory().getDialect();
            String queryString = QueryUtils.buildRandomQuery(dbType, dialect, useQualifier, catalog, schema, table, includeColumns, includeColumnTerms, batchSize);

            try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(queryString)) {
                return new MultipleRowResultSetExtractor<>(convertRow(fakerTable, includeColumns)).extractData(rs);
            }
        });
    }

    protected List<Map<String, SqlArg>> loadForRandomData(DataSourceType dbType, Session session, FakerTable fakerTable, //
                                                          List<String> includeColumns, Map<String, String> includeColumnTerms, int batchSize) throws Exception {
        List<Map<String, SqlArg>> resultData = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            Map<String, SqlArg> record = new LinkedHashMap<>();
            for (String colName : includeColumns) {
                FakerColumn col = fakerTable.findColumn(colName);
                record.put(colName, col.generatorData());
            }
            resultData.add(record);
        }
        return resultData;
    }

    protected RowMapper<Map<String, SqlArg>> convertRow(FakerTable fakerTable, List<String> includeColumns) {
        List<String> selectColumns;
        if (CollectionUtils.isEmpty(includeColumns)) {
            selectColumns = fakerTable.getColumns();
        } else {
            selectColumns = includeColumns;
        }

        return (rs, rowNum) -> {
            Map<String, SqlArg> row = new LinkedHashMap<>();
            for (String column : selectColumns) {
                FakerColumn tableColumn = fakerTable.findColumn(column);
                if (tableColumn == null) {
                    continue;
                }
                SqlArg result = tableColumn.readData(rs);
                row.put(column, result);
            }
            return row;
        };
    }
}
