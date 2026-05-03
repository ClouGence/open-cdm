package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.Stack;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;

public class SrSelectDomainBuilder extends SelectDomainBuilder<SrSelectDomain> {

    public SrSelectDomainBuilder(Stack selectStack){
        super(selectStack);
    }

    @Override
    protected SrSelectDomain getSelectDomain() { return new SrSelectDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            ((SrSelectDomain) domains.get(0)).setHasLimit(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
