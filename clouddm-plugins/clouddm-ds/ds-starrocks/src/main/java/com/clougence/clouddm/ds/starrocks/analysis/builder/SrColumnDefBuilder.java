package com.clougence.clouddm.ds.starrocks.analysis.builder;

import com.clougence.clouddm.ds.starrocks.analysis.enums.SrAttribute;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;

public class SrColumnDefBuilder extends ColumnDefBuilder<SrColumnDomain> {

    @Override
    protected SrColumnDomain getColumnDomain() {
        SrColumnDomain drColumnDomain = new SrColumnDomain();
        drColumnDomain.setNullable(true);
        return drColumnDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == SrAttribute.AGG_TYPE) {
            columnDomain.setAggrType((String) value);
        } else if (attr == CommonAttribute.COMMENT) {
            columnDomain.setComment((String) value);
        } else if (attr == MyAttribute.AUTO_INCREMENT) {
            columnDomain.setAuto(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
