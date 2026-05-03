package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class ChDropSchemaBuilder extends DropSchemaBuilder<ChSchemaDomain> {

    private boolean ifExists;

    @Override
    protected ChSchemaDomain getSchemaDomain() { return new ChSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(objNameDomain.getNameList());
            ChSchemaDomain schemaDomain = getSchemaDomain();
            schemaDomain.setSchema(map.get(UmiTypes.Schema));
            schemaDomain.setCatalog(map.get(UmiTypes.Catalog));
            schemaDomain.setAuditKind(SecQueryKind.DROP);
            schemaDomain.setSqlType(SecQueryType.DROP_SCHEMA);
            schemaDomain.setIfExists(ifExists);
            domains.add(schemaDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
