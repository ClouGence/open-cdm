package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;

public class McCreateSchemaBuilder extends CreateSchemaBuilder<McSchemaDomain> {

    @Override
    protected McSchemaDomain getSchemaDomain() { return new McSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            schemaDomain.setIfNotExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
