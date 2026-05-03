package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;

public class ChSelectDomainBuilder extends SelectDomainBuilder<ChSelectDomain> {

    public ChSelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
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
    protected ChSelectDomain getSelectDomain() { return new ChSelectDomain(); }
}
