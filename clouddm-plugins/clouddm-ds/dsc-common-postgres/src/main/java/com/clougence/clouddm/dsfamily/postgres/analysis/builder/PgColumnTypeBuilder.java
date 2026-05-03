package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnTypeBuilder;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.enums.PgAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.PgColumnTypeDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgColumnTypeBuilder extends ColumnTypeBuilder {

    int array = 0;

    public PgColumnTypeBuilder(String fullName){
        super(fullName);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == PgAttribute.ARRAY_TYPE) {
            array++;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new PgColumnTypeDomain(typeName, fullName, params.isEmpty() ? null : params.get(0), array != 0));
    }
}
