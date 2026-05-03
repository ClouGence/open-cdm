package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class CreateRoleBuilder extends AbstractDomainBuilder {

    protected RdbRoleDomain rdbRoleDomain = getRoleDomain();

    protected RdbRoleDomain getRoleDomain() { return new RdbRoleDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            this.rdbRoleDomain.setRole(domain.getName());
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        rdbRoleDomain.setAuditKind(SecQueryKind.CREATE);
        rdbRoleDomain.setSqlType(SecQueryType.CREATE_ROLE);
        return Collections.singletonList(rdbRoleDomain);
    }
}
