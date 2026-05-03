package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class FromDomainBuilder extends AbstractDomainBuilder {

    protected List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.TABLE) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbTableDomain) {
                    RdbTableDomain rdbTableDomain = (RdbTableDomain) ruleDomain;
                    rdbTableDomain.setAuditKind(SecQueryKind.QUERY);
                    rdbTableDomain.setSqlType(SecQueryType.SELECT);
                }
            }
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.JOIN) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }

    }

}
