package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgDropTableBuilder extends DropTableBuilder<PgTableDomain> {

    private boolean ifExists;

    @Override
    protected PgTableDomain getTableDomain() { return new PgTableDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        for (Domain tableDomain : this.tableDomains) {
            PgTableDomain tableDomain1 = (PgTableDomain) tableDomain;
            tableDomain1.setIfExists(this.ifExists);
        }
        return this.tableDomains;
    }
}
