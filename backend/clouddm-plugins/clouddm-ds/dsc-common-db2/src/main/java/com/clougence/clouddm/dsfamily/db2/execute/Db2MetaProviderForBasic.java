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
package com.clougence.clouddm.dsfamily.db2.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.Value;

/**
 * https://www.ibm.com/docs/zh/i/7.5?topic=views-i-catalog-tables
 * @author mode 2021/11/16 09:55:55
 */
public abstract class Db2MetaProviderForBasic extends AbstractMetadataProvider {

    public Db2MetaProviderForBasic(Connection connection){
        super(connection);
    }

    public abstract List<Value> selectSchemas() throws SQLException;

    public abstract Value selectSchema(String schema) throws SQLException;

    public abstract List<Value> selectTables(String schema) throws SQLException;

    public abstract List<Value> selectViews(String schema) throws SQLException;

    public abstract List<Value> selectMaterialized(String schema) throws SQLException;

    public abstract List<Value> selectProcedures(String schema) throws SQLException;

    public abstract List<Value> selectFunctions(String schema) throws SQLException;

    public abstract List<Value> selectTriggers(String schema) throws SQLException;

    public abstract List<Value> selectSequence(String schema) throws SQLException;

    public abstract Map<String, List<RdbColumn>> loadColumns(String schema, List<String> tables) throws SQLException;

    public abstract List<RdbProcedure> loadProcedure(String schema, List<String> specNames) throws SQLException;

    public abstract List<RdbFunction> loadFunction(String schema, List<String> specNames) throws SQLException;

    public abstract List<RdbTrigger> loadTriggers(String schema, List<String> specNames) throws SQLException;

    public abstract List<RdbTable> loadMaterialized(String schema, List<String> specNames) throws SQLException;

    public abstract List<RdbSequence> loadSequences(String schema, List<String> sequenceNames) throws SQLException;
}
