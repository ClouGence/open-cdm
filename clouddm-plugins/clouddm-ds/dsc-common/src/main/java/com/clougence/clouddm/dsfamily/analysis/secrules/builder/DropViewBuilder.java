package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class DropViewBuilder extends AbstractDomainBuilder {

    private RdbViewDomain functionDomain = new RdbViewDomain();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseViewName(objNameDomain.getNameList());
            functionDomain.setCatalog(map.get(UmiTypes.Catalog));
            functionDomain.setSchema(map.get(UmiTypes.Schema));
            functionDomain.setView(map.get(UmiTypes.View));
        }
    }

    @Override
    public List<Domain> build() {
        functionDomain.setAuditKind(SecQueryKind.DROP);
        functionDomain.setSqlType(SecQueryType.DROP_VIEW);
        return Collections.singletonList(functionDomain);
    }
}
