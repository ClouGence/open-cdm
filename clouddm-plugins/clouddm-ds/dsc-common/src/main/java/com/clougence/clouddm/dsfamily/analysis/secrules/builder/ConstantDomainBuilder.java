package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ConstantDomainBuilder extends AbstractDomainBuilder {

    private String value;

    @Override
    public List<Domain> build() {
        RdbConstantDomain rdbConstantDomain = new RdbConstantDomain(value);
        return Collections.singletonList(rdbConstantDomain);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.value = (String) value;
        }
    }

}
