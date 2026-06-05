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
package com.clougence.clouddm.ds.sqlserver.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public class MsSqlUmiServiceDm extends AbstractRdbUmiService<MsSqlMetaProviderDm> implements RdbUmiServiceDm {

    public MsSqlUmiServiceDm(Connection con){
        super(() -> new MsSqlMetaProviderDm(con));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.metadataSupplier.eGet().selectCatalogs();
        } else if (levels.size() == 1) {
            String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
            return this.metadataSupplier.eGet().selectSchemas(catalog);
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case View:
                return this.metadataSupplier.eGet().selectViews(catalog, schema);
            case Table:
                return this.metadataSupplier.eGet().selectTables(catalog, schema);
            case Procedure:
                return this.metadataSupplier.eGet().selectProcedure(catalog, schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunction(catalog, schema);
            case Trigger:
                return this.metadataSupplier.eGet().selectTrigger(catalog, schema);
            case Sequence:
                return this.metadataSupplier.eGet().selectSequence(catalog, schema);
            case Synonym:
                return this.metadataSupplier.eGet().selectSynonym(catalog, schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Catalog:
                return this.metadataSupplier.eGet().selectCatalog(leafName);
            case Schema:
                return this.metadataSupplier.eGet().selectSchema(catalog, leafName);
            case Table: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> defs = this.metadataSupplier.eGet().loadTables(catalog, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            case View: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> defs = this.metadataSupplier.eGet().loadViews(catalog, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            //case Procedure: {
            //    List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
            //    List<RdbTable> defs = this.metadataSupplier.eGet().loadSelectObject(catalog, schema, names);
            //    return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            //}
            //case Function: {
            //    List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
            //    List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(catalog, schema, tableNames);
            //    return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            //}
            default:
                throw new UnsupportedOperationException("detailLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException {
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Table:
            case View:
            case Materialized:
                Map<String, List<RdbColumn>> result = this.metadataSupplier.eGet().loadColumns(catalog, schema, leafNames);
                return (result != null) ? result : Collections.emptyMap();
            default:
                throw new UnsupportedOperationException("loadColumns of " + leafType + " Unsupported.");
        }
    }
}
