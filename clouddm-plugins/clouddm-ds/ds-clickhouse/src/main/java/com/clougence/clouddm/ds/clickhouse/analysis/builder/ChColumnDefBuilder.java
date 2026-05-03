package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;

public class ChColumnDefBuilder extends ColumnDefBuilder<ChColumnDomain> {

    @Override
    protected ChColumnDomain getColumnDomain() {
        ChColumnDomain drColumnDomain = new ChColumnDomain();
        return drColumnDomain;
    }

    //    @Override
    //    public void addAttr(Attribute attr, Object value) {
    //        if (attr == DrAttribute.AGG_TYPE) {
    //            columnDomain.setAggrType((String) value);
    //        } else if (attr == CommonAttribute.COMMENT) {
    //            columnDomain.setComment((String) value);
    //        } else if (attr == MyAttribute.AUTO_INCREMENT) {
    //            columnDomain.setAuto(true);
    //        } else {
    //            super.addAttr(attr, value);
    //        }
    //    }
}
