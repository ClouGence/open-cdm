package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgLimitBuilder extends AbstractDomainBuilder {

    private List<Domain> domains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        return domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.SELECT) {
            this.domains.addAll(list);
        } else if (source == DomainSource.CONSTANT) {
            this.domains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
