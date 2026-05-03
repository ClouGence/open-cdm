package com.clougence.clouddm.ds.doris.analysis.builder;

import com.clougence.clouddm.ds.doris.analysis.enums.DrAttribute;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;

public class DrColumnDefBuilder extends ColumnDefBuilder<DrColumnDomain> {

    @Override
    protected DrColumnDomain getColumnDomain() {
        DrColumnDomain drColumnDomain = new DrColumnDomain();
        drColumnDomain.setNullable(true);
        return drColumnDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == DrAttribute.AGG_TYPE) {
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
