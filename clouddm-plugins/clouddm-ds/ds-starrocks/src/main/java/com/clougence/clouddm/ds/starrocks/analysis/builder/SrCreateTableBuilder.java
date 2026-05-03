package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.analysis.enums.SrAttribute;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class SrCreateTableBuilder extends CreateTableBuilder<SrTableDomain> {

    public SrCreateTableBuilder(SecQueryType type){
        rdbTableDomain.setSqlType(type);
    }

    @Override
    protected SrTableDomain getTableDomain() {
        SrTableDomain oraTableDomain = new SrTableDomain();
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
        } else if (attr == MyAttribute.ENGINE) {
            rdbTableDomain.setEngine((String) value);
        } else if (attr == MyAttribute.TEMPORARY) {
            rdbTableDomain.setTemporary(true);
        } else if (attr == SrAttribute.EXTERNAL) {
            rdbTableDomain.setExternal(true);
        } else if (attr == SrAttribute.TABLE_MODEL) {
            rdbTableDomain.setTableModel((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }
}
