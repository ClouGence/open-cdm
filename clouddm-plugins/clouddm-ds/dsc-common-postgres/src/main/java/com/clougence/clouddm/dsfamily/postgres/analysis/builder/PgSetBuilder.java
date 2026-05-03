package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.OptDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgSetBuilder extends AbstractDomainBuilder {

    private String key;
    private String value;

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.CONSTANT) {
            value = ((RdbConstantDomain) list.get(0)).getConstantValue();
            if (value.startsWith("'")) {
                value = value.substring(1, value.length() - 1);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new OptDomain(key, value));
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.DEFAULT) {
            this.value = "default";
        } else if (attr == CommonAttribute.VALUE) {
            this.key = (String) value;
        }
    }
}
