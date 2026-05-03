package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgArrayBuilder extends AbstractDomainBuilder {

    private List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.COLUMN_TYPE) {
            return;
        }
        if (source != DomainSource.CONSTANT && source != DomainSource.SELECT && source != DomainSource.FUNCTION && source != DomainSource.ARRAY && source != DomainSource.COLUMN) {
            super.handleSubDomain(list, source);
        }
        ruleDomains.addAll(list);
    }

    @Override
    public List<Domain> build() {
        return this.ruleDomains;
    }
}
