package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class SrDropTableBuilder extends DropTableBuilder<SrTableDomain> {

    private boolean ifExists;

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
            SrTableDomain tableDomain1 = (SrTableDomain) tableDomain;
            tableDomain1.setIfExists(this.ifExists);
        }
        return this.tableDomains;
    }

    @Override
    protected SrTableDomain getTableDomain() { return new SrTableDomain(); }
}
