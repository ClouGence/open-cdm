package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.builder.enums.OraAttribute;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

public class OraCreateTableBuilder extends CreateTableBuilder<OraTableDomain> {

    @Override
    protected OraTableDomain getTableDomain() {
        OraTableDomain oraTableDomain = new OraTableDomain();
        oraTableDomain.setAuditKind(SecQueryKind.CREATE);
        oraTableDomain.setSqlType(SecQueryType.CREATE_TABLE);
        return oraTableDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == OraAttribute.TEMPORARY) {
            rdbTableDomain.setTemporary(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
