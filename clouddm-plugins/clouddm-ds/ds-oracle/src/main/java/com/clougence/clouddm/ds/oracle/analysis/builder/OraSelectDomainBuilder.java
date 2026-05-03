package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;

public class OraSelectDomainBuilder extends SelectDomainBuilder<OraSelectDomain> {

    public OraSelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected OraSelectDomain getSelectDomain() { return new OraSelectDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            selectDomain.setHasLimit((boolean) value);
        } else {
            super.addAttr(attr, value);
        }
    }
}
