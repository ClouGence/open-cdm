package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CatalogDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DrCatalogBuilder extends CatalogDomainBuilder<DrCatalogDomain> {

    public DrCatalogBuilder(SecQueryType secQueryType){
        super(secQueryType);
    }

    @Override
    protected DrCatalogDomain getCatalogDomain() { return new DrCatalogDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            rdbCatalogDomain.setCatalog(domain.getName());
        } else if (source == DomainSource.OPTIONS) {
            OptionsDomain domain = (OptionsDomain) list.get(0);
            rdbCatalogDomain.setOptions(domain.getOptions());
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            rdbCatalogDomain.setComment((String) value);
        } else if (attr == CommonAttribute.IF_EXISTS) {
            rdbCatalogDomain.setIfExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
