package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class ResourceBuilder extends AbstractDomainBuilder {

    private RdbResourceDomain domain = new RdbResourceDomain();

    public ResourceBuilder(SecQueryType sqlType, SecQueryKind auditKind, boolean needSupply, TargetType targetType){
        domain.setSqlType(sqlType);
        domain.setAuditKind(auditKind);
        domain.setNeedSupply(needSupply);
        domain.setTarget(targetType);
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(domain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            if (objNameDomain.getType() == NameType.SCHEMA) {
                Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(objNameDomain.getNameList());
                domain.setSchema(map.get(UmiTypes.Schema));
                domain.setCatalog(map.get(UmiTypes.Catalog));
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                domain.setCatalog(map.get(UmiTypes.Catalog));
                domain.setSchema(map.get(UmiTypes.Schema));
                domain.setName(map.get(UmiTypes.Table));
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
