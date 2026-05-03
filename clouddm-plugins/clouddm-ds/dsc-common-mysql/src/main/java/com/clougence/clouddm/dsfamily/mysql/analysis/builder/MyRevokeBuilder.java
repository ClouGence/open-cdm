package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RevokeBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyRevokeDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MyRevokeBuilder extends RevokeBuilder {

    private List<MyRevokeDomain> domains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);

            MyRevokeDomain rdbUserDomain = new MyRevokeDomain();
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
