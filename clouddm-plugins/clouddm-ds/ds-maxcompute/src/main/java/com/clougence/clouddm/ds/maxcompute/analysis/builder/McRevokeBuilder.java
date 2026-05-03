package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McRevokeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RevokeBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

public class McRevokeBuilder extends RevokeBuilder {

    private List<McRevokeDomain> domains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);

            McRevokeDomain rdbUserDomain = new McRevokeDomain();
            rdbUserDomain.setSqlType(SecQueryType.REVOKE);
            rdbUserDomain.setAuditKind(SecQueryKind.ALTER);
            rdbUserDomain.setName(domain.getNameList().get(0));
            if (domain.getNameList().size() > 1) {
                rdbUserDomain.setHost(domain.getNameList().get(1));
            }
            domains.add(rdbUserDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return new ArrayList<>(domains);
    }
}
