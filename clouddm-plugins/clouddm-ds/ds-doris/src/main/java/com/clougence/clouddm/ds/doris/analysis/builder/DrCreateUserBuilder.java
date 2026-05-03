package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrUserDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateUserBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DrCreateUserBuilder extends CreateUserBuilder<DrUserDomain> {

    @Override
    protected DrUserDomain getRdbUserDomain() { return new DrUserDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            this.rdbUserDomain.setUser(domain.getNameList().get(0));
            if (domain.getNameList().size() == 2) {
                this.rdbUserDomain.setHost(domain.getNameList().get(1));
            }
        } else {
            super.handleSubDomain(list, source);
        }

    }

    @Override
    public List<Domain> build() {
        rdbUserDomain.setAuditKind(SecQueryKind.CREATE);
        rdbUserDomain.setSqlType(SecQueryType.CREATE_USER);
        if (rdbUserDomain.getHost() == null) {
            rdbUserDomain.setHost("%");
        }
        return Collections.singletonList(rdbUserDomain);
    }
}
