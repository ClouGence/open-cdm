package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WhereDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class WhereDomainBuilder extends AbstractDomainBuilder {

    private WhereDomain whereDomain = new WhereDomain();

    @Override
    public List<Domain> build() {
        // such as 2>1 ...
        if (allConstant()) {
            whereDomain.setValidWhere(false);
        }
        return Collections.singletonList(whereDomain);
    }

    private boolean allConstant() {
        for (Domain domain : whereDomain.getDomains()) {
            if (domain instanceof RdbConstantDomain) {
                continue;
            } else {
                return false;
            }
        }
        return !whereDomain.getDomains().isEmpty();
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        // for in
        if (status == DomainSource.VALUES) {
            return;
        }
        if (status != DomainSource.COLUMN && status != DomainSource.CONSTANT && status != DomainSource.FUNCTION && status != DomainSource.SELECT) {
            super.handleSubDomain(list, status);
        }

        this.whereDomain.getDomains().addAll(list);
    }

    public void setValid(boolean valid) {
        this.whereDomain.setValidWhere(valid);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALID_WHERE) {
            this.whereDomain.setValidWhere((Boolean) value);
        }
    }
}
