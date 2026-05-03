package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class InsertColumnBuilder extends AbstractDomainBuilder {

    private List<Domain> columns = new ArrayList<>();

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            RdbConstantDomain rdbConstantDomain = new RdbConstantDomain((String) value);
            columns.add(rdbConstantDomain);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            RdbConstantDomain rdbConstantDomain = new RdbConstantDomain(objNameDomain.getName());
            columns.add(rdbConstantDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return columns;
    }

    private String getString(String val) {
        if (val.startsWith("\"")) {
            return val.substring(1, val.length() - 1);
        }
        return val;
    }
}
