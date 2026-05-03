package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DropRoleBuilder extends AbstractDomainBuilder {

    protected List<String> roles = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            roles.add(domain.getName());
        }
    }

    @Override
    public List<Domain> build() {
        List<Domain> domains = new ArrayList<>();
        for (String user : roles) {
            RdbRoleDomain rdbUserDomain = getRoleDomain();
            rdbUserDomain.setSqlType(SecQueryType.DROP_ROLE);
            rdbUserDomain.setAuditKind(SecQueryKind.DROP);
            rdbUserDomain.setRole(user);
            domains.add(rdbUserDomain);
        }
        return domains;
    }

    protected RdbRoleDomain getRoleDomain() { return new RdbRoleDomain(); }
}
