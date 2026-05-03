package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyInsertDomain;

public class MyInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        MyInsertDomain myInsertDomain = new MyInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
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
