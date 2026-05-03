package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbEventDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class DropEventBuilder extends AbstractDomainBuilder {

    private RdbEventDomain domain = new RdbEventDomain();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);

            Map<UmiTypes, String> map = BuilderUtil.parseProcedureName(objNameDomain.getNameList());
            domain.setCatalog(map.get(UmiTypes.Catalog));
            domain.setSchema(map.get(UmiTypes.Schema));
            domain.setName(map.get(UmiTypes.Procedure));
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setSqlType(SecQueryType.DROP_EVENT);
        return Collections.singletonList(domain);
    }
}
