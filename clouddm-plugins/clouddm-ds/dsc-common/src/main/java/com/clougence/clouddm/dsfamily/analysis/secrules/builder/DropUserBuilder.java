package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DropUserBuilder extends AbstractDomainBuilder {

    protected List<String> users = new ArrayList<>();

    protected boolean      isExists;

    @Override
    public void addAttr(Attribute type, Object value) {
        if (type == CommonAttribute.IF_EXISTS) {
            isExists = true;
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            users.add(domain.getVal());
        } else if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            users.add(domain.getNameList().get(0));
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        List<Domain> domains = new ArrayList<>();
        for (String user : users) {
            RdbUserDomain rdbUserDomain = new RdbUserDomain();
            rdbUserDomain.setSqlType(SecQueryType.DROP_USER);
            rdbUserDomain.setAuditKind(SecQueryKind.DROP);
            rdbUserDomain.setUser(user);
            rdbUserDomain.setIfExists(isExists);
            domains.add(rdbUserDomain);
        }
        return domains;
    }
}
