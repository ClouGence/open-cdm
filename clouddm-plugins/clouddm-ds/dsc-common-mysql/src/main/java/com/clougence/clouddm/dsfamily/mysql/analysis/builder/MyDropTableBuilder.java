package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class MyDropTableBuilder extends DropTableBuilder<MyTableDomain> {

    private boolean ifExists;

    @Override
    protected MyTableDomain getTableDomain() { return new MyTableDomain(); }

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
            MyTableDomain tableDomain1 = (MyTableDomain) tableDomain;
            tableDomain1.setIfExists(this.ifExists);
        }
        return this.tableDomains;
    }
}
