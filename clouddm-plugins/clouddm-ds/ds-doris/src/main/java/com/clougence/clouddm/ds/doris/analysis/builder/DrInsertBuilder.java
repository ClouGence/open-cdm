package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrInsertDomain;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.column.QueryItem;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public class DrInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        DrInsertDomain myInsertDomain = new DrInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.SELECT) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    DrSelectDomain rdbSelectDomain = (DrSelectDomain) ruleDomain;
                    if (rdbSelectDomain.isValuesSelect()) {
                        for (QueryItem column : rdbSelectDomain.getColumns()) {
                            for (RuleDomain columnColumn : column.getColumns()) {
                                if (columnColumn instanceof RdbConstantDomain) {
                                    String constantValue = ((RdbConstantDomain) columnColumn).getConstantValue();
                                    if (constantValue.equalsIgnoreCase("null")) {
                                        insertDomain.setHasNullValue(true);
                                    }
                                } else if (columnColumn instanceof DrSelectDomain) {
                                    DrSelectDomain drSelectDomain = (DrSelectDomain) columnColumn;
                                    drSelectDomain.setMode(RdbQueryMode.INSERT);
                                    insertDomain.setHasSubQuery(true);
                                    insertDomain.addChild(columnColumn);
                                } else {
                                    insertDomain.addChild(columnColumn);
                                }
                            }
                        }
                    } else {
                        RdbSelectDomain selectDomain = (RdbSelectDomain) ruleDomain;
                        selectDomain.setMode(RdbQueryMode.INSERT);
                        insertDomain.addChild(rdbSelectDomain);
                        insertDomain.setFromSelect(true);
                    }
                }
            }
        } else {
            super.handleSubDomain(list, type);
        }
    }
}
