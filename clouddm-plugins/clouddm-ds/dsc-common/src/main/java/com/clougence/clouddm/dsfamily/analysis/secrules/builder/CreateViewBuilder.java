package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class CreateViewBuilder extends AbstractDomainBuilder {

    public SecQueryType   type;

    private RdbViewDomain domain = new RdbViewDomain();

    public CreateViewBuilder(SecQueryType type){
        this.type = type;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {

        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseViewName(objNameDomain.getNameList());
            domain.setCatalog(map.get(UmiTypes.Catalog));
            domain.setSchema(map.get(UmiTypes.Schema));
            domain.setView(map.get(UmiTypes.View));
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.MATERIALIZED) {
            domain.setMaterialized(true);
        }
    }

    @Override
    public List<Domain> build() {
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return Collections.singletonList(domain);
    }
}
