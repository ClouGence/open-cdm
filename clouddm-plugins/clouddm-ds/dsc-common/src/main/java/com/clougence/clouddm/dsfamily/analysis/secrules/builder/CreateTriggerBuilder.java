package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTriggerDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class CreateTriggerBuilder extends AbstractDomainBuilder {

    private RdbTriggerDomain triggerDomain = new RdbTriggerDomain();

    @Override
    public List<Domain> build() {
        triggerDomain.setSqlType(SecQueryType.CREATE_TRIGGER);
        triggerDomain.setAuditKind(SecQueryKind.CREATE);

        return Collections.singletonList(triggerDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseTriggerName(objNameDomain.getNameList());
            triggerDomain.setCatalog(map.get(UmiTypes.Catalog));
            triggerDomain.setSchema(map.get(UmiTypes.Schema));
            triggerDomain.setName(map.get(UmiTypes.Trigger));
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
