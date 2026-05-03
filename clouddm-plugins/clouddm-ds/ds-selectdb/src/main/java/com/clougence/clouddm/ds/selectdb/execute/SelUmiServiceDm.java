package com.clougence.clouddm.ds.selectdb.execute;

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

/**
 * @author mode 2021/11/16 12:38:59
 */
public class SelUmiServiceDm extends AbstractRdbUmiService<SelMetaProviderDm> implements RdbUmiServiceDm {

    public SelUmiServiceDm(Connection con){
        super(() -> new SelMetaProviderDm(con));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.metadataSupplier.eGet().selectCatalogs();
        } else if (levels.size() == 1) {
            return this.metadataSupplier.eGet().selectSchemas((String) levelsParam.get(UmiTypes.Catalog));
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        if (!catalog.equals(SelMetaProviderUtils.INTERNAL_CATALOG) && leafType != UmiTypes.ExternalTable) {
            return Collections.emptyList();
        }
        switch (leafType) {
            case View:
                return this.metadataSupplier.eGet().selectViews(schema);
            case Table:
                return this.metadataSupplier.eGet().selectTables(catalog, schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunctions(schema);
            case Materialized:
                return this.metadataSupplier.eGet().selectMaterializeds(schema);
            case ExternalTable:
                return this.metadataSupplier.eGet().selectExternalTables(catalog, schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Schema:
                return this.metadataSupplier.eGet().selectSchema(leafName);
            case Catalog:
                return this.metadataSupplier.eGet().selectCatalog(leafName);
            case View:
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.metadataSupplier.eGet().loadViews(null, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            case Table:
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(null, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
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
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        return this.metadataSupplier.eGet().loadSelectObject(catalog, schema, leafName);
    }
}
