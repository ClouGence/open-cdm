package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class DefaultBuilder extends AbstractDomainBuilder {

    private List<Domain> domains;

    @Override
    public List<Domain> build() {
        return domains == null ? Collections.emptyList() : domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.CONSTANT) {
            this.domains = list;
        } else {
            super.handleSubDomain(list, type);
        }
    }
}
