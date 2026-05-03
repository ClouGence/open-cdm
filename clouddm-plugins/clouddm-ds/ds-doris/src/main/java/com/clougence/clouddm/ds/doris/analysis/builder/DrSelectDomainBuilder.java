package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.Stack;

import com.clougence.clouddm.ds.doris.analysis.enums.DrAttribute;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;

public class DrSelectDomainBuilder extends SelectDomainBuilder<DrSelectDomain> {

    public DrSelectDomainBuilder(Stack selectStack){
        super(selectStack);
    }

    @Override
    protected DrSelectDomain getSelectDomain() { return new DrSelectDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            selectDomain.setHasLimit(true);
        } else if (attr == DrAttribute.VALUES_SELECT) {
            selectDomain.setValuesSelect(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
