package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class SrCreateSchemaBuilder extends CreateSchemaBuilder<SrSchemaDomain> {

    @Override
    protected SrSchemaDomain getSchemaDomain() { return new SrSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            schemaDomain.setIfNotExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OPTIONS) {
            OptionsDomain optionsDomain = (OptionsDomain) list.get(0);
            schemaDomain.setOptions(optionsDomain.getOptions());
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
