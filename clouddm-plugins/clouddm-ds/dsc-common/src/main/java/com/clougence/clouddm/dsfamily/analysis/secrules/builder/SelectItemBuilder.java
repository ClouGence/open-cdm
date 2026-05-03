package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.SelectItemDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class SelectItemBuilder extends AbstractDomainBuilder {

    protected SelectItemDomain selectItemDomain = new SelectItemDomain();

    private String             alias;

    @Override
    public void addAttr(Attribute type, Object value) {
        if (type == CommonAttribute.ALIAS) {
            this.alias = value.toString();
        } else if (type == CommonAttribute.VALUE) {
            if (selectItemDomain.getName() == null) {
                this.selectItemDomain.setName((String) value);
            } else {
                alias = (String) value;
            }
        }
    }

    @Override
    public List<Domain> build() {

        selectItemDomain.setAlias(alias);
        return Collections.singletonList(selectItemDomain);

    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        if (status == DomainSource.COLUMN || status == DomainSource.FUNCTION || status == DomainSource.SELECT || status == DomainSource.CONSTANT
            || status == DomainSource.VARIABLE) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) ruleDomain;
                    rdbColumnDomain.setSqlType(SecQueryType.SELECT);
                    rdbColumnDomain.setAuditKind(SecQueryKind.QUERY);
                }
            }
            for (Domain domain : list) {
                this.selectItemDomain.getDomainList().add((RuleDomain) domain);
            }
        } else {
            super.handleSubDomain(list, status);
        }

    }
}
