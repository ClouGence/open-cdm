package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;

public class ChCreateSchemaBuilder extends CreateSchemaBuilder<ChSchemaDomain> {

    @Override
    protected ChSchemaDomain getSchemaDomain() { return new ChSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            schemaDomain.setIfNotExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
