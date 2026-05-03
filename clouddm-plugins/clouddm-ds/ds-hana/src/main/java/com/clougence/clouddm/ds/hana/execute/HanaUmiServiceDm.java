package com.clougence.clouddm.ds.hana.execute;

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

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaUmiServiceDm extends AbstractRdbUmiService<HanaMetaProviderDm> implements RdbUmiServiceDm {

    public HanaUmiServiceDm(Connection con){
        super(() -> new HanaMetaProviderDm(con));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            // catalogs
            return this.metadataSupplier.eGet().selectCatalogs();
        } else if (levels.size() == 1) {
            // schemas
            String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
            return this.metadataSupplier.eGet().selectSchemas(catalog);
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    /**
     * Leaf node queries, such as tables, views, procedures, functions, triggers, sequences
     * 
     * @param levelsParam Parent node parameters, such as database:testdb, schema:testschema
     * @param leafType Leaf node type
     * @param pattern Query pattern
     * @return
     * @throws SQLException
     */
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
                return this.metadataSupplier.eGet().selectProcedures(catalog, schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunctions(catalog, schema);
            case Trigger:
                return this.metadataSupplier.eGet().selectTriggers(catalog, schema);
            case Sequence:
                return this.metadataSupplier.eGet().selectSequence(catalog, schema);
            case Synonym:
                return this.metadataSupplier.eGet().selectSynonyms(catalog, schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    /**
     * Leaf node details
     * 
     * @param levelsParam Parent node parameters
     * @param leafType Leaf node type
     * @param leafName Leaf node name
     * @return
     * @throws SQLException
     */
    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Catalog:
                return this.metadataSupplier.eGet().selectCatalog(leafName);
            case Schema:
                return this.metadataSupplier.eGet().selectSchema(catalog, leafName);
            case View:
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.metadataSupplier.eGet().loadViews(catalog, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            case Table:
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(catalog, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            case Procedure:
                List<String> procedureNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbProcedure> procedures = this.metadataSupplier.eGet().loadProcedures(null, schema, procedureNames);
                return CollectionUtils.isEmpty(procedures) ? null : procedures.get(0);
            case Function:
                List<String> functionNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbFunction> functions = this.metadataSupplier.eGet().loadFunctions(null, schema, functionNames);
                return CollectionUtils.isEmpty(functions) ? null : functions.get(0);
            case Trigger:
                return this.metadataSupplier.eGet().loadTrigger(schema, leafName);
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
                Map<String, List<RdbColumn>> result = this.metadataSupplier.eGet().loadColumns(null, schema, leafNames);
                return (result != null) ? result : Collections.emptyMap();
            default:
                throw new UnsupportedOperationException("loadColumns of " + leafType + " Unsupported.");
        }
    }
}
