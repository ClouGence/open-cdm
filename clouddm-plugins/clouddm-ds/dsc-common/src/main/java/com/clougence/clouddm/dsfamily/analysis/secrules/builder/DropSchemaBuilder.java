package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public abstract class DropSchemaBuilder<T extends RdbSchemaDomain> extends AbstractDomainBuilder {

    protected List<T> domains = new ArrayList<>();;

    protected abstract T getSchemaDomain();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(objNameDomain.getNameList());
            T schemaDomain = getSchemaDomain();
            schemaDomain.setSchema(map.get(UmiTypes.Schema));
            schemaDomain.setCatalog(map.get(UmiTypes.Catalog));
            schemaDomain.setAuditKind(SecQueryKind.DROP);
            schemaDomain.setSqlType(SecQueryType.DROP_SCHEMA);
            domains.add(schemaDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        List<Domain> result = new ArrayList<>();
        result.addAll(domains);
        return result;
    }
}
