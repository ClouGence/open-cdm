package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ValuesBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class OraValuesBuilder extends ValuesBuilder {

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.COLUMN) {

        } else {
            super.handleSubDomain(list, type);
        }
    }
}
