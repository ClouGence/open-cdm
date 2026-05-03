package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.schema.umi.struts.UmiTypes;

public class AlterTableBuilder extends AbstractDomainBuilder {

    protected List<String> nameList;

    protected List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        String catalog = map.get(UmiTypes.Catalog);
        String schema = map.get(UmiTypes.Schema);
        String table = map.get(UmiTypes.Table);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) ruleDomain;
                columnDomain.setTable(table);
                columnDomain.setSchema(schema);
                columnDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbConstraintDomain) {
                RdbConstraintDomain constraintDomain = (RdbConstraintDomain) ruleDomain;
                constraintDomain.setTableCatalog(catalog);
                constraintDomain.setTableSchema(schema);
                constraintDomain.setTableName(table);
            } else if(ruleDomain instanceof RdbTableDomain){
                RdbTableDomain tableDomain = (RdbTableDomain) ruleDomain;
                tableDomain.setTable(table);
                tableDomain.setSchema(schema);
                tableDomain.setCatalog(catalog);
            }
        }
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.RENAME) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
