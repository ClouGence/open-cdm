package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;

public class McSelectDomainBuilder extends SelectDomainBuilder<McSelectDomain> {

    public McSelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            selectDomain.setHasLimit(true);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    protected McSelectDomain getSelectDomain() { return new McSelectDomain(); }
}
