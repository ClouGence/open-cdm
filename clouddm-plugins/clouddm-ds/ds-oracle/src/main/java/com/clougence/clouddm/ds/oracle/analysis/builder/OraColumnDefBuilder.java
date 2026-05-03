package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.builder.enums.OraAttribute;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;

public class OraColumnDefBuilder extends ColumnDefBuilder<OraColumnDomain> {

    @Override
    protected OraColumnDomain getColumnDomain() {
        OraColumnDomain oraColumnDomain = new OraColumnDomain();
        oraColumnDomain.setNullable(true);
        return oraColumnDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == OraAttribute.AUTO) {
            columnDomain.setAuto(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
