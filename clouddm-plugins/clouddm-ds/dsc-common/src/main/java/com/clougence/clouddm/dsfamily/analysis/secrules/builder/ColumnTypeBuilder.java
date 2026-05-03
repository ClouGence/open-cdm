package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ColumnTypeBuilder extends AbstractDomainBuilder {

    protected String       typeName;

    protected String       fullName;

    protected List<String> params = new ArrayList<>();

    public ColumnTypeBuilder(String fullName){
        this.fullName = fullName;
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new ColumnTypeDomain(typeName, fullName, params.isEmpty() ? null : params.get(0)));
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.CONSTANT) {
            RdbConstantDomain constant = (RdbConstantDomain) list.get(0);
            this.params.add(constant.getConstantValue());
        } else if (source == DomainSource.VALUES) {
            RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) list.get(0);
            this.params.add(rdbConstantDomain.getConstantValue());
        } else if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            this.typeName = objNameDomain.getName();
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
