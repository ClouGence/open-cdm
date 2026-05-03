package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.schema.umi.struts.UmiTypes;

public class DrAlterTableBuilder extends AbstractDomainBuilder {

    private String        newTableName;
    private DrTableDomain myTableDomain = new DrTableDomain();
    private List<Domain>  ruleDomains   = new ArrayList<>();

    private List<String>  nameList;

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
            } else if (ruleDomain instanceof RdbIndexDomain) {
                RdbIndexDomain indexDomain = (RdbIndexDomain) ruleDomain;
                indexDomain.setTableName(table);
                indexDomain.setSchema(schema);
                indexDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof DrTableDomain) {
                RdbTableDomain tableDomain = (RdbTableDomain) ruleDomain;
                tableDomain.setTable(table);
                tableDomain.setSchema(schema);
                tableDomain.setCatalog(catalog);
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return ruleDomains;
    }

    //    @Override
    //    public List<Domain> build() {
    //        List<Domain> domains = new ArrayList<>();
    //        if (alterTable) {
    //            domains.add(myTableDomain);
    //        }
    //
    //        if (newTableName != null) {
    //            DrTableDomain myTableDomain1 = new DrTableDomain();
    //            myTableDomain1.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
    //            myTableDomain1.setSqlKind(SqlKind.ALTER);
    //            myTableDomain1.setTable(myTableDomain.getTable());
    //            myTableDomain1.setSchema(myTableDomain.getSchema());
    //            myTableDomain1.setNewName(newTableName);
    //            if (newSchemaName == null) {
    //                newSchemaName = myTableDomain.getSchema();
    //            }
    //            myTableDomain1.setNewSchemaName(newSchemaName);
    //            domains.add(myTableDomain1);
    //        }
    //        myTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
    //        myTableDomain.setSqlKind(SqlKind.ALTER);
    //
    //        for (Domain ruleDomain : ruleDomains) {
    //            if (ruleDomain instanceof RdbColumnDomain) {
    //                RdbColumnDomain columnDomain = (RdbColumnDomain) ruleDomain;
    //                columnDomain.setTable(myTableDomain.getTable());
    //                columnDomain.setSchema(myTableDomain.getSchema());
    //            } else if (ruleDomain instanceof RdbConstraintDomain) {
    //                RdbConstraintDomain constraintDomain = (RdbConstraintDomain) ruleDomain;
    //                constraintDomain.setTableSchema(myTableDomain.getSchema());
    //                constraintDomain.setTableName(myTableDomain.getTable());
    //            } else if (ruleDomain instanceof RdbIndexDomain) {
    //                RdbIndexDomain indexDomain = (RdbIndexDomain) ruleDomain;
    //                indexDomain.setTableName(myTableDomain.getTable());
    //                indexDomain.setTableSchema(myTableDomain.getSchema());
    //            }
    //        }
    //        domains.addAll(ruleDomains);
    //        return domains;
    //    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            myTableDomain.setComment((String) value);

        } else if (attr == MyAttribute.ENGINE) {
            myTableDomain.setEngine((String) value);

        } else {
            super.addAttr(attr, value);
        }
    }
}
