package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.InheritDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgInheritBuilder implements DomainBuilder {

    private InheritDomain inheritDomain = new InheritDomain();

    @Override
    public List<Domain> build() {
        return Collections.singletonList(inheritDomain);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            inheritDomain.getTableNames().add((String) value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.inheritDomain.getTableNames().add(objNameDomain.getNameList().get(objNameDomain.getNameList().size() - 1));
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
