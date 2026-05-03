package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectItemBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public class PgSelectColumnBuilder extends SelectItemBuilder {

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ARRAY) {
            for (Domain domain : list) {
                this.selectItemDomain.getDomainList().add((RuleDomain) domain);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
