package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class GrantBuilder extends AbstractDomainBuilder {

    private List<String> users = new ArrayList<>();

    @Override
    public List<Domain> build() {
        List<Domain> result = new ArrayList<>();
        for (String user : users) {
            RdbGrantDomain rdbGrantDomain = new RdbGrantDomain();
            rdbGrantDomain.setName(user);
            rdbGrantDomain.setSqlType(SecQueryType.GRANT);
            rdbGrantDomain.setAuditKind(SecQueryKind.ALTER);
            result.add(rdbGrantDomain);
        }
        return result;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            this.users.add(domain.getVal());
        } else if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            this.users.add(domain.getNameList().get(0));
        } else {
            super.handleSubDomain(list, source);
        }

    }
}
