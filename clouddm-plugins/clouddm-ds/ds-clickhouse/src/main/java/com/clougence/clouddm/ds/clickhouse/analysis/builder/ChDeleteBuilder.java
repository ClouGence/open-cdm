package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

public class ChDeleteBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new ChDeleteDomain(); }

    //    @Override
    //    public void addAttr(Attribute attr, Object value) {
    //        if (attr == CommonAttribute.LIMIT) {
    //            ((ChDeleteDomain) rdbDeleteDomain).setHasLimit(true);
    //        } else if (attr == CommonAttribute.IGNORE) {
    //            ((ChDeleteDomain) rdbDeleteDomain).setHasIgnore(true);
    //        } else {
    //            super.addAttr(attr, value);
    //        }
    //    }

    //    @Override
    //    public void handleSubDomain(List<Domain> list, DomainSource type) {
    //        if (type == DomainSource.TABLE) {
    //
    //        } else {
    //            super.handleSubDomain(list, type);
    //        }
    //    }
}
