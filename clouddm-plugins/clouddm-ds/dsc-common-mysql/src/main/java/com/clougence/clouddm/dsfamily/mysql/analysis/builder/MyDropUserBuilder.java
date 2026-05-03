package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropUserBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyUserDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MyDropUserBuilder extends DropUserBuilder {

    private List<RdbUserDomain> rdbUserDomains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);

            MyUserDomain rdbUserDomain = new MyUserDomain();
            rdbUserDomain.setSqlType(SecQueryType.DROP_USER);
            rdbUserDomain.setAuditKind(SecQueryKind.DROP);
            rdbUserDomain.setUser(domain.getNameList().get(0));
            if (domain.getNameList().size() > 1) {
                rdbUserDomain.setHost(domain.getNameList().get(1));
            }
            rdbUserDomain.setIfExists(isExists);
            rdbUserDomains.add(rdbUserDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return new ArrayList<>(rdbUserDomains);
    }
}
