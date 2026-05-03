package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AlterSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;

public class MyAlterSchemaBuilder extends AlterSchemaBuilder<MySchemaDomain> {

    @Override
    protected MySchemaDomain getSchemaDomain() { return new MySchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == MyAttribute.CHARACTER_SET) {
            schemaDomain.setCharacterSet((String) value);
        } else if (attr == MyAttribute.COLLATE) {
            schemaDomain.setCollate((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }
}
