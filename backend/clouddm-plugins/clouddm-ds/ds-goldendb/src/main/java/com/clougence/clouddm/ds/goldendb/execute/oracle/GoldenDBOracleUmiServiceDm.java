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
package com.clougence.clouddm.ds.goldendb.execute.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public class GoldenDBOracleUmiServiceDm extends AbstractRdbUmiService<GoldenDBOracleMetaProvider> implements RdbUmiServiceDm {

    private static final Set<String> UNAVAILABLE_SCHEMAS = Set.of("information_schema", "performance_schema");

    public GoldenDBOracleUmiServiceDm(Connection connection){
        super(() -> new GoldenDBOracleMetaProvider(connection));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            List<Value> values = this.metadataSupplier.eGet().selectSchemas();
            return values.stream().filter(value -> {
                String schema = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
                return !UNAVAILABLE_SCHEMAS.contains(schema);
            }).toList();
        }
        throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return this.metadataSupplier.eGet().loadSelectObject(null, schema, leafName);
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return switch (leafType) {
            case View -> this.metadataSupplier.eGet().selectViews(schema);
            case Table -> this.metadataSupplier.eGet().selectTables(schema);
            case Materialized -> this.metadataSupplier.eGet().selectMaterializedView(schema);
            case Sequence -> this.metadataSupplier.eGet().selectSequences(schema);
            default -> throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        };
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Catalog:
            case Schema:
                return this.metadataSupplier.eGet().selectSchema(leafName);
            case View:
                List<String> viewNames = new ArrayList<>();
                if (StringUtils.isNotBlank(leafName)) {
                    viewNames = Collections.singletonList(leafName);
                }
                List<RdbTable> views = this.metadataSupplier.eGet().loadViews(null, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            case Table:
                List<String> tableNames = new ArrayList<>();
                if (StringUtils.isNotBlank(leafName)) {
                    tableNames = Collections.singletonList(leafName);
                }
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(null, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            case Materialized:
                return this.metadataSupplier.eGet().loadMaterialized(schema, leafName);
            case Sequence:
                return this.metadataSupplier.eGet().loadSequence(schema, leafName);
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
