package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class DropIndexBuilder extends AbstractDomainBuilder {

    RdbIndexDomain indexDomain = new RdbIndexDomain();

    @Override
    public List<Domain> build() {
        indexDomain.setSqlType(SecQueryType.DROP_INDEX);
        indexDomain.setAuditKind(SecQueryKind.DROP);
        return Collections.singletonList(indexDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            if (domain.getType() == NameType.INDEX) {
                indexDomain.setName(domain.getName());
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(domain.getNameList());
                indexDomain.setTableSchema(map.get(UmiTypes.Schema));
                indexDomain.setTableName(map.get(UmiTypes.Table));
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
