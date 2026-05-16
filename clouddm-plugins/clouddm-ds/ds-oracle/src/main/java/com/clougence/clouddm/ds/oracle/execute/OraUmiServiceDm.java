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
package com.clougence.clouddm.ds.oracle.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public class OraUmiServiceDm extends AbstractRdbUmiService<OraMetaProviderDm> implements RdbUmiServiceDm {

    public OraUmiServiceDm(Connection con){
        this(con, true);
    }

    public OraUmiServiceDm(Connection con, boolean excludeOraMaintainedSchemas){
        super(() -> new OraMetaProviderDm(con, excludeOraMaintainedSchemas));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.metadataSupplier.eGet().selectSchemas();
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return this.metadataSupplier.eGet().loadSelectObject(null, schema, leafName);
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case View:
                return this.metadataSupplier.eGet().selectViews(schema);
            case Table:
                return this.metadataSupplier.eGet().selectTables(schema);
            case Materialized:
                return this.metadataSupplier.eGet().selectMaterializedView(schema);
            case Procedure:
                return this.metadataSupplier.eGet().selectProcedures(schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunctions(schema);
            case Sequence:
                return this.metadataSupplier.eGet().selectSequences(schema);
            case Trigger:
                return this.metadataSupplier.eGet().selectTrigger(schema);
            case Synonym:
                return this.metadataSupplier.eGet().selectSynonym(schema);
            case DBLink:
                return this.metadataSupplier.eGet().selectDbLinks(schema);
            case ROLE:
                return this.metadataSupplier.eGet().selectRoles(schema);
            case USER:
                return this.metadataSupplier.eGet().selectUsers(schema);
            case Job:
                return this.metadataSupplier.eGet().selectJobs(schema);
            case ScheduleJob:
                return this.metadataSupplier.eGet().selectScheduleJobs(schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Catalog:
            case Schema:
                return this.metadataSupplier.eGet().selectSchema(leafName);
            case View:
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.metadataSupplier.eGet().loadViews(null, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            case Table:
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(null, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            case Function:
                List<String> functionNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbFunction> functions = this.metadataSupplier.eGet().loadFunctions(null, schema, functionNames);
                return CollectionUtils.isEmpty(functions) ? null : functions.get(0);
            case Procedure:
                List<String> procedureNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbProcedure> procedures = this.metadataSupplier.eGet().loadProcedures(null, schema, procedureNames);
                return CollectionUtils.isEmpty(procedures) ? null : procedures.get(0);
            case Trigger:
                return this.metadataSupplier.eGet().loadTrigger(schema, leafName);
            case Job:
                return this.metadataSupplier.eGet().loadJob(schema, leafName);
            case ScheduleJob:
                return this.metadataSupplier.eGet().loadScheduleJob(schema, leafName);
            case DBLink:
                return this.metadataSupplier.eGet().loadDbLink(schema, leafName);
            case Materialized:
                return this.metadataSupplier.eGet().loadMaterialized(schema, leafName);
            case Sequence:
                return this.metadataSupplier.eGet().loadSequence(schema, leafName);
            case USER:
                return this.metadataSupplier.eGet().loadUser(schema, leafName);
            case ROLE:
                return this.metadataSupplier.eGet().loadRole(schema, leafName);
            case Synonym:
                return this.metadataSupplier.eGet().loadSynonym(schema, leafName);
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
                Map<String, List<RdbColumn>> result = this.metadataSupplier.eGet().loadColumns(null, schema, leafNames);
                return (result != null) ? result : Collections.emptyMap();
            default:
                throw new UnsupportedOperationException("loadColumns of " + leafType + " Unsupported.");
        }
    }
}
