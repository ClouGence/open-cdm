package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class InsertBuilder extends AbstractDomainBuilder {

    private List<String>      nameList     = new ArrayList<>();
    protected RdbInsertDomain insertDomain = getInsertDomain();

    private int               values       = 0;

    protected RdbInsertDomain getInsertDomain() { return new RdbInsertDomain(); }

    @Override
    public List<Domain> build() {
        if (values > 1) {
            this.insertDomain.setMultipleValues(true);
        }
        insertDomain.setAuditKind(SecQueryKind.DML);
        insertDomain.setSqlType(SecQueryType.INSERT);

        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        insertDomain.setCatalog(map.get(UmiTypes.Catalog));
        insertDomain.setSchema(map.get(UmiTypes.Schema));
        insertDomain.setTable(map.get(UmiTypes.Table));

        if (insertDomain.getColumns().isEmpty()) {
            insertDomain.setOnlyValues(true);
        }

        return Collections.singletonList(this.insertDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (type == DomainSource.SELECT) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) ruleDomain;
                    insertDomain.addChild(rdbSelectDomain);
                    RdbSelectDomain selectDomain = (RdbSelectDomain) ruleDomain;
                    selectDomain.setMode(RdbQueryMode.INSERT);
                    insertDomain.setFromSelect(true);
                }
            }

        } else if (type == DomainSource.VALUES) {
            values++;
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    RdbSelectDomain selectDomain = (RdbSelectDomain) ruleDomain;
                    selectDomain.setMode(RdbQueryMode.INSERT);
                    insertDomain.addChild(selectDomain);
                    insertDomain.setHasSubQuery(true);
                } else if (ruleDomain instanceof RdbConstantDomain) {
                    RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) ruleDomain;
                    if (rdbConstantDomain.getConstantValue().equals("null")) {
                        insertDomain.setHasNullValue(true);
                    }
                } else if (ruleDomain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) ruleDomain;
                    insertDomain.addChild(rdbCallDomain);
                }
            }
        } else if (type == DomainSource.INSERT_COLUMN) {
            for (Domain ruleDomain : list) {
                RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) ruleDomain;
                insertDomain.getColumns().add(rdbConstantDomain.getConstantValue());
            }
        } else if (type == DomainSource.SET_VALUE) {
            for (Domain domain : list) {
                if (domain instanceof RdbColumnDomain) {
                    // todo need modify
                    RdbColumnDomain domainColumn = (RdbColumnDomain) domain;
                    insertDomain.getColumns().add(domainColumn.getColumn());
                } else if (domain instanceof RdbCallDomain) {
                    insertDomain.addChild((RdbCallDomain) domain);
                } else {
                    super.handleSubDomain(list, type);
                }
            }
        } else if (type == DomainSource.UPDATE_COLUMN) {
            for (Domain domain : list) {
                insertDomain.getColumns().add(((RdbColumnDomain) domain).getColumn());
            }
        } else {
            super.handleSubDomain(list, type);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.INSERT_CONFLICT) {
            insertDomain.setConflict((RdbInsertConflictStrategy) value);
        } else if (attr == CommonAttribute.MULTI_VALUE) {
            insertDomain.setMultipleValues(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
