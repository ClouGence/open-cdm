package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ObjNameBuilder implements DomainBuilder {

    private List<String> nameList = new ArrayList<>();
    private NameType     type;

    public ObjNameBuilder(NameType type){
        this.type = type;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.nameList.add((String) value);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new ObjNameDomain(nameList, type));
    }
}
