package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSchemaDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgCreateSchemaBuilder extends CreateSchemaBuilder<PgSchemaDomain> {

    private boolean ifNotExists;

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            this.schemaDomain.setOwner(domain.getVal());
            if (schemaDomain.getSchema() == null) {
                this.schemaDomain.setSchema(domain.getVal());
            }
        } else if (source == DomainSource.CREATE_TABLE) {
            PgTableDomain domain = (PgTableDomain) list.get(0);
            domain.setSchema(schemaDomain.getSchema());
            for (RdbColumnDomain columnDomain : domain.getColumnDomains()) {
                columnDomain.setSchema(schemaDomain.getSchema());
            }
            for (RdbColumnDomain columnDomain : domain.getColumnDomains()) {
                columnDomain.setSchema(schemaDomain.getSchema());
            }
            for (RdbConstraintDomain constraintDomain : domain.getConstraintDomains()) {
                constraintDomain.setTableSchema(schemaDomain.getSchema());
            }
            schemaDomain.addChild(domain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        schemaDomain.setIfNotExists(ifNotExists);
        return super.build();
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            this.ifNotExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    protected PgSchemaDomain getSchemaDomain() { return new PgSchemaDomain(); }
}
