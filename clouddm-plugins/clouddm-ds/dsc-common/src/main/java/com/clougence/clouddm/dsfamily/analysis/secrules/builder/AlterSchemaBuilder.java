package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.HashMap;
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

public abstract class AlterSchemaBuilder<T extends RdbSchemaDomain> extends AbstractDomainBuilder {

    protected T schemaDomain = getSchemaDomain();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(objNameDomain.getNameList());
            schemaDomain.setCatalog(map.get(UmiTypes.Catalog));
            schemaDomain.setSchema(map.get(UmiTypes.Schema));
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        schemaDomain.setAuditKind(SecQueryKind.ALTER);
        schemaDomain.setSqlType(SecQueryType.ALTER_SCHEMA);
        if (schemaDomain.getOptions() == null) {
            schemaDomain.setOptions(new HashMap<>());
        }
        return Collections.singletonList(schemaDomain);
    }

    protected abstract T getSchemaDomain();
}
