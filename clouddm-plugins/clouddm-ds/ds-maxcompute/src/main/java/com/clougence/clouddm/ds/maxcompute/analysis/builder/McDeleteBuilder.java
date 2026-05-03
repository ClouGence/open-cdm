package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class McDeleteBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new McDeleteDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.TABLE) {

        } else {
            super.handleSubDomain(list, type);
        }
    }
}
