package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;

public class MySelectDomainBuilder extends SelectDomainBuilder<MySelectDomain> {

    public MySelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
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
    protected MySelectDomain getSelectDomain() { return new MySelectDomain(); }
}
