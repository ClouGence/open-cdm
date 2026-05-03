package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class McDropTableBuilder extends DropTableBuilder<McTableDomain> {

    private boolean ifExists;

    @Override
    protected McTableDomain getTableDomain() { return new McTableDomain(); }

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
            McTableDomain tableDomain1 = (McTableDomain) tableDomain;
            tableDomain1.setIfExists(this.ifExists);
        }
        return this.tableDomains;
    }
}
