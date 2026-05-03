package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RenameBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSchemaDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class PgRenameBuilder extends RenameBuilder {

    protected boolean ifExists;

    public PgRenameBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    protected RdbColumnDomain getColumnDomain() { return new PgColumnDomain(); }

    @Override
    protected RdbTableDomain getTableDomain() { return new PgTableDomain(); }

    protected RdbSchemaDomain getSchemaDomain() { return new PgSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Column) {
            RdbColumnDomain rdbColumnDomain = getColumnDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 5: {
                    rdbColumnDomain.setCatalog(nameList.get(0));
                }
                case 4: {
                    rdbColumnDomain.setSchema(nameList.get(size - 4));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 3));
                    rdbColumnDomain.setColumn(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Table) {
            PgTableDomain rdbColumnDomain = new PgTableDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 4: {
                    rdbColumnDomain.setCatalog(nameList.get(0));
                }
                case 3: {
                    rdbColumnDomain.setSchema(nameList.get(size - 3));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            if (ifExists) {
                rdbColumnDomain.setIfExists(true);
            }
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Catalog) {
            RdbCatalogDomain catalogDomain = new RdbCatalogDomain();
            catalogDomain.setAuditKind(SecQueryKind.ALTER);
            catalogDomain.setSqlType(SecQueryType.RENAME_CATALOG);
            catalogDomain.setCatalog(nameList.get(0));
            catalogDomain.setNewName(nameList.get(1));
            catalogDomain.setOptions(new HashMap<>());
            return Collections.singletonList(catalogDomain);
        } else if (targetType == TargetType.Schema) {
            RdbSchemaDomain schemaDomain = getSchemaDomain();
            schemaDomain.setAuditKind(SecQueryKind.ALTER);
            schemaDomain.setSqlType(SecQueryType.RENAME_SCHEMA);
            int size = nameList.size();
            if (size == 3) {
                schemaDomain.setCatalog(nameList.get(0));
            }
            schemaDomain.setSchema(nameList.get(size - 2));
            schemaDomain.setNewName(nameList.get(size - 1));
            return Collections.singletonList(schemaDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }
}
