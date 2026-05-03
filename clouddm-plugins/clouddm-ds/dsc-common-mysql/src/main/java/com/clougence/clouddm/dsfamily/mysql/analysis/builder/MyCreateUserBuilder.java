package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateUserBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyUserDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class MyCreateUserBuilder extends CreateUserBuilder<MyUserDomain> {

    @Override
    protected MyUserDomain getRdbUserDomain() { return new MyUserDomain(); }

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

}
