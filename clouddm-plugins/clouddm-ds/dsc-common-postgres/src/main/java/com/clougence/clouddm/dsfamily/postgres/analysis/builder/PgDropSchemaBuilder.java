package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSchemaDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgDropSchemaBuilder extends DropSchemaBuilder<PgSchemaDomain> {

    private boolean ifExists;

    @Override
    protected PgSchemaDomain getSchemaDomain() { return new PgSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.CASCADE) {
            for (PgSchemaDomain domain : domains) {
                domain.setCascade(true);
            }
        } else if (attr == CommonAttribute.RESTRICT) {
            for (PgSchemaDomain domain : domains) {
                domain.setRestrict(true);
            }
        } else if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        for (PgSchemaDomain domain : domains) {
            domain.setIfExists(ifExists);
        }
        return super.build();
    }
}
