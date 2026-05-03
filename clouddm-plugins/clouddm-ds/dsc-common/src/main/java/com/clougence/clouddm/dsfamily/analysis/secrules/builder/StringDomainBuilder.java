package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class StringDomainBuilder implements DomainBuilder {

    private String value;

    public StringDomainBuilder(String value){
        this.value = value;
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new StringDomain(value));
    }
}
