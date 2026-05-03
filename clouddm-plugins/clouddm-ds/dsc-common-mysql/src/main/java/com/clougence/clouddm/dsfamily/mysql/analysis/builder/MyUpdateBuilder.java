package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyUpdateDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class MyUpdateBuilder extends UpdateBuilder {

    public MyUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            ((MyUpdateDomain) updateDomain).setHasLimit(true);
        } else if (attr == CommonAttribute.IGNORE) {
            ((MyUpdateDomain) updateDomain).setHasIgnore(true);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.TABLE) {
            for (Domain domain : list) {
                if (domain instanceof RdbTableDomain) {
                    this.updateDomain.getTables().add((RdbTableDomain) domain);
                } else if (domain instanceof JoinDomain) {
                    this.updateDomain.getJoinTypes().add(((JoinDomain) domain).getJoinType());
                }
            }
        } else if (source == DomainSource.SET_VALUE) {
            for (Domain domain : list) {
                if (domain instanceof RdbColumnDomain) {
                } else if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_SET);
                    updateDomain.addChild(rdbSelectDomain);
                    updateDomain.setSelectInSet(true);
                } else if (domain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) domain;
                    updateDomain.addChild(rdbCallDomain);
                } else {
                    super.handleSubDomain(list, source);
                }
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        MyUpdateDomain pgUpdateDomain = new MyUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }

    @Override
    public List<Domain> build() {
        if (!this.selectStack.peek().isEmpty()) {
            this.updateDomain.setHasWith(true);
        }
        if (updateDomain.getTables().size() > 1) {
            updateDomain.setMultiUpdate(true);
        }
        if (nameList != null) {
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            this.updateDomain.setTable(map.get(UmiTypes.Table));
            this.updateDomain.setSchema(map.get(UmiTypes.Schema));
            this.updateDomain.setCatalog(map.get(UmiTypes.Catalog));
        }
        updateDomain.setAuditKind(SecQueryKind.DML);
        updateDomain.setSqlType(SecQueryType.UPDATE);
        if (updateDomain.getWhereColumns() == null) {
            updateDomain.setWhereColumns(new ArrayList<>());
        }
        return Collections.singletonList(this.updateDomain);
    }
}
