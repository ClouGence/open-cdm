package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SetValueBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgSetValueBuilder extends SetValueBuilder {

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.VALUES || source == DomainSource.ARRAY) {
            for (Domain domain : list) {
                if (domain instanceof RdbConstantDomain) {

                } else {
                    values.add(domain);
                }
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
