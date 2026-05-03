package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public interface DomainBuilder {

    List<Domain> build();

    // handle sub builder
    default void handleSubDomain(List<Domain> list, DomainSource source) {
        throw new UnsupportedOperationException();
    }

    default void addAttr(Attribute attr, Object value) {

    }

}
