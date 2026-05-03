package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public abstract class AbstractDomainBuilder implements DomainBuilder {

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        throw new UnsupportedOperationException(this.getClass().getName() + " unsupported :" + source.name());
    }
}
