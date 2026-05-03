package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.PgColumnTypeDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgColumnDefBuilder extends ColumnDefBuilder<PgColumnDomain> {

    @Override
    protected PgColumnDomain getColumnDomain() {
        PgColumnDomain pgColumnDomain = new PgColumnDomain();
        pgColumnDomain.setNullable(true);
        return pgColumnDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.COLUMN_TYPE) {
            PgColumnTypeDomain columnTypeDomain = (PgColumnTypeDomain) list.get(0);
            this.columnDomain.setTypeDesc(columnTypeDomain.getFullName());
            this.columnDomain.setTypeName(columnTypeDomain.getType());
            this.columnDomain.setLength(columnTypeDomain.getLength());
            this.columnDomain.setArray(columnTypeDomain.isArray());
        }
        super.handleSubDomain(list, type);
    }
}
