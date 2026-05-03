package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainOptionKeys;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgCatalogDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSchemaDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class PgAlterOwnerBuilder implements DomainBuilder {

    private TargetType   targetType;

    private String       owner;

    private List<String> nameList = new ArrayList<>();

    public PgAlterOwnerBuilder(TargetType type){
        this.targetType = type;
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Catalog) {
            RdbCatalogDomain domain = new PgCatalogDomain();
            domain.setSqlType(SecQueryType.ALTER_CATALOG);
            domain.setAuditKind(SecQueryKind.ALTER);
            domain.setCatalog(nameList.get(0));
            domain.setOptions(new HashMap<>());

            if (owner != null) {
                domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_OWNER, owner);
            }

            return Collections.singletonList(domain);
        } else if (targetType == TargetType.Schema) {
            PgSchemaDomain domain = new PgSchemaDomain();
            domain.setSqlType(SecQueryType.ALTER_SCHEMA);
            domain.setAuditKind(SecQueryKind.ALTER);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(nameList);
            domain.setCatalog(map.get(UmiTypes.Catalog));
            domain.setSchema(map.get(UmiTypes.Schema));
            domain.setOptions(new HashMap<>());
            if (owner != null) {
                domain.setOwner(owner);
            }
            return Collections.singletonList(domain);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            this.owner = domain.getVal();
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            if (targetType == TargetType.Catalog && !nameList.isEmpty()) {
                throw new RuntimeException();
            }
            this.nameList.add((String) value);
        }
    }
}
