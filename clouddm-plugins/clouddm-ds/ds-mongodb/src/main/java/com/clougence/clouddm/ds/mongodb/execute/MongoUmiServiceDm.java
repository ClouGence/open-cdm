package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

public class MongoUmiServiceDm extends AbstractRdbUmiService<MongoMetaProviderDm> implements RdbUmiServiceDm {

    public MongoUmiServiceDm(Connection connection){
        super(() -> new MongoMetaProviderDm(connection));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.metadataSupplier.eGet().selectSchemas((String) levelsParam.get(UmiTypes.Schema));
        } else {
            throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
        }
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Table:
                DslProvider instance = MongoDslProvider.INSTANCE;
                return this.metadataSupplier.eGet().selectTables(schema);
            case View:
                return this.metadataSupplier.eGet().selectViews(schema);
            default:
                throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        switch (leafType) {
            case Table: {
                return this.metadataSupplier.eGet().loadTable(schema, leafName);
            }
            default: {
                throw new UnsupportedOperationException("detailLeaf of " + leafType + " Unsupported.");
            }
        }
    }
}
