package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class MyAlterTableBuilder extends AbstractDomainBuilder {

    private String        newSchemaName;
    private String        newTableName;
    private MyTableDomain myTableDomain = new MyTableDomain();
    private List<Domain>  ruleDomains   = new ArrayList<>();

    private boolean       alterTable;

    @Override
    public List<Domain> build() {
        List<Domain> domains = new ArrayList<>();
        if (alterTable) {
            domains.add(myTableDomain);
        }

        if (newTableName != null) {
            MyTableDomain myTableDomain1 = new MyTableDomain();
            myTableDomain1.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
            myTableDomain1.setAuditKind(SecQueryKind.ALTER);
            myTableDomain1.setTable(myTableDomain.getTable());
            myTableDomain1.setSchema(myTableDomain.getSchema());
            myTableDomain1.setNewName(newTableName);
            if (newSchemaName == null) {
                newSchemaName = myTableDomain.getSchema();
            }
            myTableDomain1.setNewSchemaName(newSchemaName);
            domains.add(myTableDomain1);
        }
        myTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        myTableDomain.setAuditKind(SecQueryKind.ALTER);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) ruleDomain;
                columnDomain.setTable(myTableDomain.getTable());
                columnDomain.setSchema(myTableDomain.getSchema());
            } else if (ruleDomain instanceof RdbConstraintDomain) {
                RdbConstraintDomain constraintDomain = (RdbConstraintDomain) ruleDomain;
                constraintDomain.setTableSchema(myTableDomain.getSchema());
                constraintDomain.setTableName(myTableDomain.getTable());
            } else if (ruleDomain instanceof RdbIndexDomain) {
                RdbIndexDomain indexDomain = (RdbIndexDomain) ruleDomain;
                indexDomain.setTableName(myTableDomain.getTable());
                indexDomain.setTableSchema(myTableDomain.getSchema());
            } else if (ruleDomain instanceof RdbTableDomain) {
                RdbTableDomain tableDomain = (RdbTableDomain) ruleDomain;
                tableDomain.setTable(myTableDomain.getTable());
                tableDomain.setSchema(myTableDomain.getSchema());
                tableDomain.setAuditKind(SecQueryKind.ALTER);
                tableDomain.setSqlType(SecQueryType.ALTER_TABLE);
            }
        }
        domains.addAll(ruleDomains);
        return domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            if (objNameDomain.getType() == NameType.NEW_TABLE) {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                newSchemaName = map.get(UmiTypes.Schema);
                newTableName = map.get(UmiTypes.Table);
                myTableDomain.setNewName(newTableName);
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                myTableDomain.setSchema(map.get(UmiTypes.Schema));
                myTableDomain.setTable(map.get(UmiTypes.Table));

            }
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
            alterTable = true;
        } else if (attr == MyAttribute.ENGINE) {
            myTableDomain.setEngine((String) value);
            alterTable = true;
        } else if (attr == MyAttribute.CHARACTER_SET) {
            myTableDomain.setCharacterSet((String) value);
            alterTable = true;
        } else if (attr == MyAttribute.COLLATE) {
            myTableDomain.setCollate((String) value);
            alterTable = true;
        } else {
            super.addAttr(attr, value);
        }
    }
}
