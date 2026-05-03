package com.clougence.clouddm.ds.polardb.execute.pormy;

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

public class PorMyUmiServiceDm extends AbstractRdbUmiService<PorMyMetaProviderDm> implements RdbUmiServiceDm {

    public PorMyUmiServiceDm(Connection con){
        super(() -> new PorMyMetaProviderDm(con));
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
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Table:
                return this.metadataSupplier.eGet().selectTables(schema);
            case View:
                return this.metadataSupplier.eGet().selectViews(schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunctions(schema);
            case Procedure:
                return this.metadataSupplier.eGet().selectProcedures(schema);
            case Trigger:
                return this.metadataSupplier.eGet().selectTriggers(schema);
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
            case Table: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> defs = this.metadataSupplier.eGet().loadTables(null, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            case View: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> defs = this.metadataSupplier.eGet().loadViews(null, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            case Function: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbFunction> defs = this.metadataSupplier.eGet().loadFunctions(null, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            case Procedure: {
                List<String> names = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbProcedure> defs = this.metadataSupplier.eGet().loadProcedures(null, schema, names);
                return CollectionUtils.isEmpty(defs) ? null : defs.get(0);
            }
            case Trigger: {
                return this.metadataSupplier.eGet().loadTrigger(null, schema, leafName);
            }
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

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return this.metadataSupplier.eGet().loadSelectObject(null, schema, leafName);
    }
}
