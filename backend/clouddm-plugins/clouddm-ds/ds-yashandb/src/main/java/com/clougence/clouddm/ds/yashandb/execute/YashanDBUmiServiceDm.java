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
package com.clougence.clouddm.ds.yashandb.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.oracle.execute.OraUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

/**
 * 崖山数据库 UMI 服务。
 * <p>
 * 层级结构、对象类型分派与 Oracle 完全一致，因此直接复用 {@link OraUmiServiceDm} 的分派实现，
 * 仅把元数据读取替换为崖山的数据字典实现 {@link YashanDBMetaProviderDm}。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBUmiServiceDm extends OraUmiServiceDm {

    private final YashanDBMetaProviderDm provider;

    public YashanDBUmiServiceDm(Connection connection){
        super(connection);
        this.provider = new YashanDBMetaProviderDm(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        return this.provider.getVersion();
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.provider.selectSchemas();
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return this.provider.loadSelectObject(null, schema, leafName);
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return switch (leafType) {
            case View -> this.provider.selectViews(schema);
            case Table -> this.provider.selectTables(schema);
            case Materialized -> this.provider.selectMaterializedView(schema);
            case Procedure -> this.provider.selectProcedures(schema);
            case Function -> this.provider.selectFunctions(schema);
            case Sequence -> this.provider.selectSequences(schema);
            case Trigger -> this.provider.selectTrigger(schema);
            case Synonym -> this.provider.selectSynonym(schema);
            case DBLink -> this.provider.selectDbLinks(schema);
            case ROLE -> this.provider.selectRoles(schema);
            case USER -> this.provider.selectUsers(schema);
            case Job -> this.provider.selectJobs(schema);
            case ScheduleJob -> this.provider.selectScheduleJobs(schema);
            default -> throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        };
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Catalog:
            case Schema:
                return this.provider.selectSchema(leafName);
            case View:
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.provider.loadViews(null, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            case Table:
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.provider.loadTables(null, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            case Function:
                List<String> functionNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbFunction> functions = this.provider.loadFunctions(null, schema, functionNames);
                return CollectionUtils.isEmpty(functions) ? null : functions.get(0);
            case Procedure:
                List<String> procedureNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbProcedure> procedures = this.provider.loadProcedures(null, schema, procedureNames);
                return CollectionUtils.isEmpty(procedures) ? null : procedures.get(0);
            case Trigger:
                return this.provider.loadTrigger(schema, leafName);
            case Job:
                return this.provider.loadJob(schema, leafName);
            case ScheduleJob:
                return this.provider.loadScheduleJob(schema, leafName);
            case DBLink:
                return this.provider.loadDbLink(schema, leafName);
            case Materialized:
                return this.provider.loadMaterialized(schema, leafName);
            case Sequence:
                return this.provider.loadSequence(schema, leafName);
            case USER:
                return this.provider.loadUser(schema, leafName);
            case ROLE:
                return this.provider.loadRole(schema, leafName);
            case Synonym:
                return this.provider.loadSynonym(schema, leafName);
            default:
                throw new UnsupportedOperationException("detailLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Table:
            case View:
            case Materialized:
                Map<String, List<RdbColumn>> result = this.provider.loadColumns(null, schema, leafNames);
                return (result != null) ? result : Collections.emptyMap();
            default:
                throw new UnsupportedOperationException("loadColumns of " + leafType + " Unsupported.");
        }
    }
}
