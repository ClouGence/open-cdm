package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ChDropTableBuilder extends DropTableBuilder<ChTableDomain> {

    @Override
    protected ChTableDomain getTableDomain() { return new ChTableDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            for (Domain tableDomain : tableDomains) {
                ((ChTableDomain) tableDomain).setIfExists((Boolean) value);
            }
        } else {
            super.addAttr(attr, value);
        }
    }

}
