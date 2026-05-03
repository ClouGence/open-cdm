package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public class OtherDomainBuilder extends AbstractDomainBuilder {

    private List<Domain> domains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        for (Domain domain : domains) {
            RuleDomain ruleDomain = (RuleDomain) domain;
            if (ruleDomain.getAuditKind() == null) {
                ruleDomain.setAuditKind(SecQueryKind.OTHER);
            }
            if (ruleDomain.getSqlType() == null) {
                ruleDomain.setSqlType(SecQueryType.UNKNOWN);
            }
        }
        return this.domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        for (Domain domain : list) {
            if (domain instanceof ModeDomain) {
                super.handleSubDomain(list, source);
            }
        }
        if (source == DomainSource.SELECT || source == DomainSource.FUNCTION) {
            this.domains.addAll(list);
        }
    }
}
