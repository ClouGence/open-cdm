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

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.generator.FakerColumn;
import com.clougence.clouddm.faker.generator.FakerTable;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.faker.generator.loader.DataLoader;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;

/**
 * 公共
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractAction implements Action {

    protected final FakerTable tableInfo;
    protected final boolean    useQualifier;
    protected final Dialect    dialect;
    protected final DataLoader dataLoader;

    public AbstractAction(FakerTable tableInfo, Dialect dialect, DataLoader dataLoader){
        this.tableInfo = tableInfo;
        this.useQualifier = tableInfo.isUseQualifier();
        this.dialect = dialect;
        this.dataLoader = dataLoader;
    }

    protected final List<Map<String, SqlArg>> retryLoad(Session session, UseFor useFor, FakerTable fakerTable, int batchSize) throws Exception {
        int tryTimes = 0;
        while (true) {
            tryTimes++;
            try {
                List<Map<String, SqlArg>> fetchDataList = this.dataLoader.loadSomeData(session, useFor, fakerTable, batchSize);
                if (CollectionUtils.isEmpty(fetchDataList)) {
                    return Collections.emptyList();
                } else {
                    return fetchDataList;
                }
            } catch (SQLException e) {
                if (tryTimes >= 3) {
                    throw e;
                }
            }
        }
    }

    protected static String[] logCols(List<FakerColumn> cols) {
        return cols.stream().map(c -> c.getColumn().replace("'", "\\'")).toArray(String[]::new);
    }
}
