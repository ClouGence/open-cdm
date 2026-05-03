package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.ds.maxcompute.analysis.builder.utils.McBuilderUtil;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class McCreateTableBuilder extends CreateTableBuilder<McTableDomain> {

    private boolean schemaEnabled;

    public McCreateTableBuilder(SecQueryType type,boolean schemaEnabled){
        rdbTableDomain.setSqlType(type);
        this.schemaEnabled = schemaEnabled;
    }

    @Override
    protected McTableDomain getTableDomain() {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.CREATE);
        return mcTableDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            rdbTableDomain.setIfNotExists(true);
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
                Map<UmiTypes, String> map = McBuilderUtil.parseTableName(objNameDomain.getNameList(), schemaEnabled);
                rdbTableDomain.setCatalog(map.get(UmiTypes.Catalog));
                rdbTableDomain.setSchema(map.get(UmiTypes.Schema));
                rdbTableDomain.setTable(map.get(UmiTypes.Table));
            } else {
                Map<UmiTypes, String> map = McBuilderUtil.parseTableName(objNameDomain.getNameList(), schemaEnabled);
                rdbTableDomain.setSourceSchema(map.get(UmiTypes.Schema));
                rdbTableDomain.setSourceTable(map.get(UmiTypes.Table));
            }

        } else {
            super.handleSubDomain(list, source);
        }
    }
}
