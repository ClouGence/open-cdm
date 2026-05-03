package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;

public class MyCreateSchemaBuilder extends CreateSchemaBuilder<MySchemaDomain> {

    @Override
    protected MySchemaDomain getSchemaDomain() { return new MySchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            schemaDomain.setIfNotExists(true);
        } else if (attr == MyAttribute.CHARACTER_SET) {
            schemaDomain.setCharacterSet((String) value);
        } else if (attr == MyAttribute.COLLATE) {
            schemaDomain.setCollate((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }
}
