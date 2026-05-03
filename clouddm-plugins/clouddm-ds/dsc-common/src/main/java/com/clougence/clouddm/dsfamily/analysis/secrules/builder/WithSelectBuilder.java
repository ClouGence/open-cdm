package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class WithSelectBuilder implements DomainBuilder {

    private WithSelectDomain domain = new WithSelectDomain();

    @Override
    public List<Domain> build() {
        domain.getSelectDomain().setMode(RdbQueryMode.WITH_BODY);
        domain.getSelectDomain().setSimpleSelect(false);
        return Collections.singletonList(domain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source != DomainSource.SELECT) {
            throw new UnsupportedOperationException();
        }
        this.domain.setSelectDomain((RdbSelectDomain) list.get(0));
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.domain.setName((String) value);
        }
    }

}
