package com.clougence.reactor.mappings.db2_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToTIDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.TiDB));

        binder.bindTableHandler(DsType.Db2, DsType.TiDB).to(new TiDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(Db2Types.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(Db2Types.INTEGER, TiDBTypes.INT);
        typeMapping.mapping(Db2Types.BIGINT, TiDBTypes.BIGINT);

        typeMapping.mapping(Db2Types.REAL, TiDBTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DOUBLE, TiDBTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DECIMAL, TiDBTypes.DECIMAL);

        typeMapping.mapping(Db2Types.CHARACTER, TiDBTypes.CHAR);
        typeMapping.mapping(Db2Types.VARCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(Db2Types.GRAPHIC, TiDBTypes.CHAR);
        typeMapping.mapping(Db2Types.VARGRAPHIC, TiDBTypes.VARCHAR);

        typeMapping.mapping(Db2Types.CHAR_FOR_BIT_DATA, TiDBTypes.BINARY);
        typeMapping.mapping(Db2Types.VARCHAR_FOR_BIT_DATA, TiDBTypes.VARBINARY);

        typeMapping.mapping(Db2Types.DATE, TiDBTypes.DATE);
        typeMapping.mapping(Db2Types.TIME, TiDBTypes.TIME);
        typeMapping.mapping(Db2Types.TIMESTAMP, TiDBTypes.TIMESTAMP);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
