package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropRoleBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgRoleDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgDropRoleBuilder extends DropRoleBuilder {

    @Override
    protected RdbRoleDomain getRoleDomain() { return new PgRoleDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            roles.add(domain.getVal());
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
