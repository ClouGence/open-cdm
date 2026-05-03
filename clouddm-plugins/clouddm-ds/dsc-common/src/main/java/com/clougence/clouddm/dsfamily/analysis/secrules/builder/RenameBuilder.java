package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class RenameBuilder extends AbstractDomainBuilder {

    protected TargetType   targetType;

    protected List<String> nameList = new ArrayList<>();

    public RenameBuilder(TargetType targetType){
        this.targetType = targetType;
    }

    @Override
    public List<Domain> build() {
        throw new UnsupportedOperationException();
    }

    protected RdbColumnDomain getColumnDomain() { return new RdbColumnDomain(); }

    protected RdbTableDomain getTableDomain() { return new RdbTableDomain(); }

    protected RdbSchemaDomain getSchemaDomain() { return new RdbSchemaDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList.addAll(objNameDomain.getNameList());
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.nameList.add((String) value);
        }
    }

}
