package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public abstract class CreateTableBuilder<T extends RdbTableDomain> extends AbstractDomainBuilder {

    protected T rdbTableDomain = getTableDomain();

    protected abstract T getTableDomain();

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            rdbTableDomain.setComment((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        if (rdbTableDomain.getOptions() == null) {
            rdbTableDomain.setOptions(new HashMap<>());
        }

        rdbTableDomain.setColumns(new ArrayList<>());

        for (RdbColumnDomain columnDomain : rdbTableDomain.getColumnDomains()) {
            columnDomain.setCatalog(rdbTableDomain.getCatalog());
            columnDomain.setSchema(rdbTableDomain.getSchema());
            columnDomain.setTable(rdbTableDomain.getTable());
            rdbTableDomain.getColumns().add(columnDomain.getColumn());
            if (columnDomain.isPrimary()) {
                rdbTableDomain.setHasPrimary(true);
            }
            if (columnDomain.isUnique()) {
                rdbTableDomain.setHasUnique(true);
            }
            if (columnDomain.isForeign()) {
                rdbTableDomain.setHasForeignKey(true);
            }
            if (columnDomain.isIndex()) {
                rdbTableDomain.setHasIndex(true);
            }
        }

        for (RdbConstraintDomain constraintDomain : rdbTableDomain.getConstraintDomains()) {
            constraintDomain.setTableCatalog(rdbTableDomain.getCatalog());
            constraintDomain.setTableSchema(rdbTableDomain.getSchema());
            constraintDomain.setTableName(rdbTableDomain.getTable());
            constraintDomain.setCatalog(rdbTableDomain.getCatalog());
            constraintDomain.setSchema(rdbTableDomain.getSchema());
            for (RdbColumnDomain columnDomain : rdbTableDomain.getColumnDomains()) {
                if (constraintDomain.getColumns().contains(columnDomain.getColumn())) {
                    if (constraintDomain.getType() == SqlConstraintType.Primary) {
                        rdbTableDomain.setHasPrimary(true);
                        columnDomain.setPrimary(true);
                    } else if (constraintDomain.getType() == SqlConstraintType.Unique) {
                        rdbTableDomain.setHasUnique(true);
                        columnDomain.setUnique(true);
                    } else if (constraintDomain.getType() == SqlConstraintType.ForeignKey) {
                        rdbTableDomain.setHasForeignKey(true);
                        columnDomain.setForeign(true);
                    }
                }
            }

        }

        return Collections.singletonList(rdbTableDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.COLUMN_DEF) {
            for (Domain domain : list) {
                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                rdbColumnDomain.setAuditKind(SecQueryKind.CREATE);
                rdbColumnDomain.setSqlType(SecQueryType.CREATE_TABLE_ADD_COLUMN);
                rdbTableDomain.getColumnDomains().add(rdbColumnDomain);

                parseConstraint(rdbColumnDomain);
            }
        } else if (source == DomainSource.CONSTRAINT) {
            for (Domain domain : list) {
                RdbConstraintDomain rdbConstantDomain = (RdbConstraintDomain) domain;
                rdbConstantDomain.setAuditKind(SecQueryKind.CREATE);
                rdbConstantDomain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
                rdbTableDomain.getConstraintDomains().add(rdbConstantDomain);
            }
        } else if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
            rdbTableDomain.setCatalog(map.get(UmiTypes.Catalog));
            rdbTableDomain.setSchema(map.get(UmiTypes.Schema));
            rdbTableDomain.setTable(map.get(UmiTypes.Table));
        } else if (source == DomainSource.SELECT) {
            Domain domain = list.get(0);
            rdbTableDomain.setSqlType(SecQueryType.CREATE_TABLE_SELECT);
            RdbSelectDomain selectDomain = (RdbSelectDomain) domain;
            selectDomain.setMode(RdbQueryMode.CREATE);
            rdbTableDomain.addChild(selectDomain);
        } else if (source == DomainSource.INDEX) {
            Domain domain = list.get(0);
            RdbIndexDomain indexDomain = (RdbIndexDomain) domain;
            indexDomain.setSqlType(SecQueryType.CREATE_TABLE_ADD_INDEX);
            indexDomain.setAuditKind(SecQueryKind.CREATE);
            indexDomain.setTableName(rdbTableDomain.getTable());
            indexDomain.setTableSchema(rdbTableDomain.getSchema());
            indexDomain.setTableCatalog(rdbTableDomain.getCatalog());
            rdbTableDomain.getIndexDomains().add(indexDomain);
            rdbTableDomain.setHasIndex(true);
            for (String column : indexDomain.getColumns()) {
                for (RdbColumnDomain columnDomain : rdbTableDomain.getColumnDomains()) {
                    if (columnDomain.getColumn().equals(column)) {
                        columnDomain.setIndex(true);
                        break;
                    }
                }
            }
        } else if (source == DomainSource.OPTIONS) {
            OptionsDomain optionsDomain = (OptionsDomain) list.get(0);
            rdbTableDomain.setOptions(optionsDomain.getOptions());
        } else {
            super.handleSubDomain(list, source);
        }

    }

    private void parseConstraint(RdbColumnDomain rdbColumnDomain) {
        RdbConstraintDomain constraintDomain = new RdbConstraintDomain();
        if (rdbColumnDomain.isPrimary()) {
            constraintDomain.setType(SqlConstraintType.Primary);
        } else if (rdbColumnDomain.isUnique()) {
            constraintDomain.setType(SqlConstraintType.Unique);
        } else if (rdbColumnDomain.isForeign()) {
            constraintDomain.setType(SqlConstraintType.ForeignKey);
        } else {
            return;
        }

        constraintDomain.setAuditKind(SecQueryKind.CREATE);
        constraintDomain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        constraintDomain.setColumns(Arrays.asList(rdbColumnDomain.getColumn()));
        constraintDomain.setSchema(rdbTableDomain.getSchema());
        constraintDomain.setCatalog(rdbTableDomain.getCatalog());
        constraintDomain.setTableName(rdbTableDomain.getTable());
        rdbTableDomain.getConstraintDomains().add(constraintDomain);
    }

}
