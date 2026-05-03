package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

public class CreateUserBuilder<T extends RdbUserDomain> extends AbstractDomainBuilder {

    protected T rdbUserDomain = getRdbUserDomain();

    protected T getRdbUserDomain() { return (T) new RdbUserDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            this.rdbUserDomain.setUser(domain.getNameList().get(0));
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute type, Object value) {
        if (type == CommonAttribute.PASSWORD) {
            this.rdbUserDomain.setPassword(value.toString());
        }
    }

    @Override
    public List<Domain> build() {
        rdbUserDomain.setAuditKind(SecQueryKind.CREATE);
        rdbUserDomain.setSqlType(SecQueryType.CREATE_USER);
        return Collections.singletonList(rdbUserDomain);
    }
}
