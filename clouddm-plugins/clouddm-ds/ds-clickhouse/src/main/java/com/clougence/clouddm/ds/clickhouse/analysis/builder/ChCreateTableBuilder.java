package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.analysis.builder.enums.ChAttribute;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class ChCreateTableBuilder extends CreateTableBuilder<ChTableDomain> {

    @Override
    protected ChTableDomain getTableDomain() {
        ChTableDomain oraTableDomain = new ChTableDomain();
        oraTableDomain.setAuditKind(SecQueryKind.CREATE);
        oraTableDomain.setSqlType(SecQueryType.CREATE_TABLE);
        return oraTableDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
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

        } else if (source == DomainSource.CONSTRAINT) {
            for (Domain domain : list) {
                RdbConstraintDomain rdbConstantDomain = (RdbConstraintDomain) domain;
                if (rdbConstantDomain.getColumns().isEmpty()) {
                    return;
                }
                rdbConstantDomain.setAuditKind(SecQueryKind.CREATE);
                rdbConstantDomain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
                rdbTableDomain.getConstraintDomains().add(rdbConstantDomain);
            }
        } else if (source == DomainSource.OPTIONS) {
            OptionsDomain optionsDomain = (OptionsDomain) list.get(0);
            rdbTableDomain.setOptions(optionsDomain.getOptions());
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            rdbTableDomain.setComment((String) value);
        } else if (attr == CommonAttribute.IF_NOT_EXISTS) {
            rdbTableDomain.setIfNotExists(true);
        } else if (attr == ChAttribute.ENGINE) {
            rdbTableDomain.setEngine((String) value);
        } else if (attr == ChAttribute.TEMPORARY) {
            rdbTableDomain.setTemporary(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
