package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McInsertDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;

public class McInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        McInsertDomain mcInsertDomain = new McInsertDomain();
        mcInsertDomain.setColumns(new ArrayList<>());
        mcInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return mcInsertDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IGNORE) {
            insertDomain.setConflict(RdbInsertConflictStrategy.IGNORE);
        } else {
            super.addAttr(attr, value);
        }
    }
}
