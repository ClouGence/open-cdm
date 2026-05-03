package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;

public class PgSelectDomainBuilder extends SelectDomainBuilder<PgSelectDomain> {

    public PgSelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected PgSelectDomain getSelectDomain() { return new PgSelectDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            selectDomain.setHasLimit((boolean) value);
        } else {
            super.addAttr(attr, value);
        }
    }

}
