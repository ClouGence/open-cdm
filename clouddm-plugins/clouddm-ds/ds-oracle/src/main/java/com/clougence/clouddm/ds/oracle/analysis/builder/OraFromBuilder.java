package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.FromDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class OraFromBuilder extends FromDomainBuilder {

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.JOIN) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }

    }
}
