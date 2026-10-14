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
package com.clougence.clouddm.ds.yashandb.execute.mysql;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.struts.Value;

/**
 * Metadata reading for the YashanDB MySQL compatibility mode.
 * <p>
 * The {@code information_schema} coverage of the YashanDB MySQL compatibility mode is very high; tables,
 * columns, indexes, constraints and views are fully aligned with MySQL (measured on 23.4.1.109:
 * {@code SCHEMATA/TABLES/COLUMNS/STATISTICS/KEY_COLUMN_USAGE/TABLE_CONSTRAINTS/REFERENTIAL_CONSTRAINTS/
 * VIEWS/TRIGGERS/ENGINES/PARTITIONS/PROCESSLIST/PARAMETERS} all exist), so {@link MyMetaProviderDm} is reused
 * directly.
 * </p>
 * <p>
 * The only gap is that there is <b>no {@code ROUTINES} or {@code EVENTS} table</b>: the parent class queries
 * procedures/functions from {@code information_schema.ROUTINES}, which raises
 * {@code YAS-02012 table or view does not exist} on YashanDB. Here procedures/functions are <b>downgraded</b>
 * to an empty list instead of letting "browse procedures/functions" fail.
 * </p>
 * <p>
 * The downgrade returns an empty result rather than falling back to the MySQL built-in function list: not
 * every function in that list is implemented by YashanDB, so listing them would be misleading. Once YashanDB
 * ships {@code information_schema.ROUTINES}, removing the overrides in this class restores full capability
 * automatically.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLMetaProviderDm extends MyMetaProviderDm {

    public YashanDBMySQLMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public List<Value> selectProcedures(String schema) {
        return Collections.emptyList();
    }

    @Override
    public List<Value> selectFunctions(String schema) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funcs) {
        return Collections.emptyList();
    }
}
