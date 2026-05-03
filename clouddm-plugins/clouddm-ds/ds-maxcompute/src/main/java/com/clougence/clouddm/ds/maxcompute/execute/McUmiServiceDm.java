package com.clougence.clouddm.ds.maxcompute.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/11/16 12:38:59
 */
public class McUmiServiceDm extends AbstractRdbUmiService<McMetaProviderDm> implements RdbUmiServiceDm {

    public McUmiServiceDm(Connection con, McConfig dsConfig){
        super(() -> new McMetaProviderDm(con, dsConfig));
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
        switch (leafType) {
            case Table:
                return this.metadataSupplier.eGet().selectTables(catalog, schema);
            case ExternalTable:
                return this.metadataSupplier.eGet().selectExternalTable(catalog, schema);
            case View:
                return this.metadataSupplier.eGet().selectViews(catalog, schema);
            case Materialized:
                return this.metadataSupplier.eGet().selectMaterializedView(catalog, schema);
            case Function:
                return this.metadataSupplier.eGet().selectFunctions(catalog, schema);
            case ROLE:
                return this.metadataSupplier.eGet().selectRoles(catalog, schema);
            case USER:
                return this.metadataSupplier.eGet().selectUsers(catalog, schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        switch (leafType) {
            case Catalog: {
                return this.metadataSupplier.eGet().selectCatalog(leafName);
            }
            case Schema: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                return this.metadataSupplier.eGet().selectSchema(catalog, leafName);
            }
            case Table: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(catalog, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            }
            case View: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.metadataSupplier.eGet().loadViews(catalog, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            }
            case Materialized: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> viewNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> views = this.metadataSupplier.eGet().loadMaterialized(catalog, schema, viewNames);
                return CollectionUtils.isEmpty(views) ? null : views.get(0);
            }
            case ExternalTable: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> tableNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbTable> tables = this.metadataSupplier.eGet().loadExternalTable(catalog, schema, tableNames);
                return CollectionUtils.isEmpty(tables) ? null : tables.get(0);
            }
            case Function: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> functionNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbFunction> functions = this.metadataSupplier.eGet().loadFunctions(catalog, schema, functionNames);
                return CollectionUtils.isEmpty(functions) ? null : functions.get(0);
            }
            case ROLE: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> roleNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbRole> roles = this.metadataSupplier.eGet().loadRoles(catalog, schema, roleNames);
                return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
            }
            case USER: {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                List<String> userNames = StringUtils.isNotBlank(leafName) ? Collections.singletonList(leafName) : new ArrayList<>();
                List<RdbUser> users = this.metadataSupplier.eGet().loadUsers(catalog, schema, userNames);
                return CollectionUtils.isEmpty(users) ? null : users.get(0);
            }
            default:
                throw new UnsupportedOperationException("detailLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        switch (leafType) {
            case Table:
            case View:
            case Materialized:
                List<RdbTable> tables = this.metadataSupplier.eGet().loadTables(catalog, schema, leafNames);
                Map<String, List<RdbColumn>> result = new HashMap<>();
                for (RdbTable table : tables) {
                    result.put(table.getName(), new ArrayList<>(table.getColumns().values()));
                }
                return result;
            default:
                throw new UnsupportedOperationException("loadColumns of " + leafType + " Unsupported.");
        }
    }
}
