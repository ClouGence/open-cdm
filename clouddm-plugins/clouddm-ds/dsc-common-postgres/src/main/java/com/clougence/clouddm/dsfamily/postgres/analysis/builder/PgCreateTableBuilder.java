package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.InheritDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class PgCreateTableBuilder extends CreateTableBuilder<PgTableDomain> {

    @Override
    protected PgTableDomain getTableDomain() {
        PgTableDomain pgTableDomain = new PgTableDomain();
        pgTableDomain.setAuditKind(SecQueryKind.CREATE);
        pgTableDomain.setSqlType(SecQueryType.CREATE_TABLE);
        return pgTableDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.INHERIT) {
            InheritDomain domain = (InheritDomain) list.get(0);
            rdbTableDomain.setInheritTables(domain.getTableNames());
            rdbTableDomain.setInherits(true);
        } else if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            if (rdbTableDomain.getTable() == null) {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                rdbTableDomain.setCatalog(map.get(UmiTypes.Catalog));
                rdbTableDomain.setSchema(map.get(UmiTypes.Schema));
                rdbTableDomain.setTable(map.get(UmiTypes.Table));
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                rdbTableDomain.setSourceSchema(map.get(UmiTypes.Schema));
                rdbTableDomain.setSourceTable(map.get(UmiTypes.Table));
                rdbTableDomain.setSqlType(SecQueryType.CREATE_TABLE_LIKE);
            }
        } else {
            super.handleSubDomain(list, source);
        }

    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            rdbTableDomain.setIfNotExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
