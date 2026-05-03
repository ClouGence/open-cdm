package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AlterTableItemBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyColumnDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MyColumnAlterTableItemBuilder extends AlterTableItemBuilder {

    private String oldColumnName;

    public MyColumnAlterTableItemBuilder(AlterTableType type){
        super(type);
    }

    @Override
    public List<Domain> build() {
        List<Domain> build = super.build();
        Domain domain = build.get(0);
        if (domain instanceof RdbColumnDomain) {
            RdbColumnDomain rdbColumn = (RdbColumnDomain) domain;
            if (oldColumnName != null && !rdbColumn.getColumn().equals(oldColumnName)) {
                MyColumnDomain rdbColumnDomain = new MyColumnDomain();
                rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
                rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
                rdbColumnDomain.setColumn(oldColumnName);
                rdbColumnDomain.setNewName(rdbColumn.getColumn());
                build.add(rdbColumnDomain);
                rdbColumn.setColumn(oldColumnName);
            }

        }
        return build;

    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.oldColumnName = (String) value;
        } else {
            super.addAttr(attr, value);
        }
    }
}
