package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CatalogDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgCatalogDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class PgCatalogDomainBuilder extends CatalogDomainBuilder<PgCatalogDomain> {

    public PgCatalogDomainBuilder(SecQueryType secQueryType){
        super(secQueryType);
    }

    @Override
    protected PgCatalogDomain getCatalogDomain() { return new PgCatalogDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            rdbCatalogDomain.setIfExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
