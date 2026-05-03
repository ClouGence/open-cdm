package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyDeleteDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class MyDeleteBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new MyDeleteDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            ((MyDeleteDomain) rdbDeleteDomain).setHasLimit(true);
        } else if (attr == CommonAttribute.IGNORE) {
            ((MyDeleteDomain) rdbDeleteDomain).setHasIgnore(true);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.TABLE) {

        } else {
            super.handleSubDomain(list, type);
        }
    }
}
