package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class MyCreateTableBuilder extends CreateTableBuilder<MyTableDomain> {

    public MyCreateTableBuilder(SecQueryType type){
        rdbTableDomain.setSqlType(type);
    }

    @Override
    protected MyTableDomain getTableDomain() {
        MyTableDomain myTableDomain = new MyTableDomain();
        myTableDomain.setAuditKind(SecQueryKind.CREATE);
        return myTableDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            rdbTableDomain.setIfNotExists(true);
        } else if (attr == MyAttribute.ENGINE) {
            rdbTableDomain.setEngine((String) value);
        } else if (attr == MyAttribute.AUTO_INCREMENT) {
            rdbTableDomain.setAutoIncrement((String) value);
        } else if (attr == MyAttribute.CHARACTER_SET) {
            rdbTableDomain.setCharacterSet((String) value);
        } else if (attr == MyAttribute.COLLATE) {
            rdbTableDomain.setCollate((String) value);
        } else if (attr == MyAttribute.TEMPORARY) {
            rdbTableDomain.setTemporary(true);
        } else {
            super.addAttr(attr, value);
        }
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

        } else {
            super.handleSubDomain(list, source);
        }
    }
}
