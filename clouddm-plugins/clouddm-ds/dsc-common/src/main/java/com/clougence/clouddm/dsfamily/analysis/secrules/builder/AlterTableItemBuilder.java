package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class AlterTableItemBuilder extends AbstractDomainBuilder {

    private AlterTableType alterTableType;

    private List<Domain>   domainList = new ArrayList<>();

    public AlterTableItemBuilder(AlterTableType type){
        this.alterTableType = type;
    }

    @Override
    public List<Domain> build() {
        return domainList;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.COLUMN_DEF) {
            for (Domain ruleDomain : list) {
                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) ruleDomain;
                if (alterTableType == AlterTableType.ADD_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.CREATE);
                    rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ADD_COLUMN);
                } else if (alterTableType == AlterTableType.DROP_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.DROP);
                    rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_DROP_COLUMN);
                } else if (alterTableType == AlterTableType.ALTER_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
                    rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
                }
            }
            domainList.addAll(list);
        } else if (source == DomainSource.CONSTRAINT) {
            for (Domain ruleDomain : list) {
                if (alterTableType == AlterTableType.DROP_CONSTRAINT) {
                    RdbConstraintDomain rdbConstraintDomain = (RdbConstraintDomain) ruleDomain;
                    rdbConstraintDomain.setSqlType(SecQueryType.ALTER_TABLE_DROP_CONSTRAINT);
                    rdbConstraintDomain.setAuditKind(SecQueryKind.DROP);
                    if (rdbConstraintDomain.getType() == null) {
                        rdbConstraintDomain.setType(SqlConstraintType.ByName);
                    }
                } else {
                    RdbConstraintDomain rdbConstraintDomain = (RdbConstraintDomain) ruleDomain;
                    rdbConstraintDomain.setSqlType(SecQueryType.ALTER_TABLE_ADD_CONSTRAINT);
                    rdbConstraintDomain.setAuditKind(SecQueryKind.CREATE);
                }
            }
            domainList.addAll(list);
        } else if (source == DomainSource.INDEX) {
            for (Domain domain : list) {
                if (alterTableType == AlterTableType.ADD_INDEX) {
                    RdbIndexDomain domain1 = (RdbIndexDomain) domain;
                    domain1.setAuditKind(SecQueryKind.CREATE);
                    domain1.setSqlType(SecQueryType.ALTER_TABLE_ADD_INDEX);
                } else if (alterTableType == AlterTableType.DROP_INDEX) {
                    RdbIndexDomain domain1 = (RdbIndexDomain) domain;
                    domain1.setAuditKind(SecQueryKind.DROP);
                    domain1.setSqlType(SecQueryType.DROP_INDEX);
                }
            }
            domainList.addAll(list);
        } else {
            super.handleSubDomain(domainList, source);
        }
    }
}
