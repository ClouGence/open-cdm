package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

/**
 *  INSERT xx    VALUES()
 */
public class ValuesBuilder extends AbstractDomainBuilder {

    private List<Domain> domains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type != DomainSource.CONSTANT && type != DomainSource.SELECT && type != DomainSource.FUNCTION) {
            super.handleSubDomain(list, type);
        }
        domains.addAll(list);
    }

    @Override
    public List<Domain> build() {
        return domains;
    }
}
