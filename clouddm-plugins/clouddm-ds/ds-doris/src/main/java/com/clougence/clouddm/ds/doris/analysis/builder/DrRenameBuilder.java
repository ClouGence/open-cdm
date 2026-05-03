package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrCatalogDomain;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RenameBuilder;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DrRenameBuilder extends RenameBuilder {

    public DrRenameBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Schema) {
            DrSchemaDomain schemaDomain = new DrSchemaDomain();
            schemaDomain.setAuditKind(SecQueryKind.ALTER);
            schemaDomain.setSqlType(SecQueryType.RENAME_SCHEMA);
            int size = nameList.size();
            if (size == 3) {
                schemaDomain.setCatalog(nameList.get(0));
            }
            schemaDomain.setSchema(nameList.get(size - 2));
            schemaDomain.setNewName(nameList.get(size - 1));
            return Collections.singletonList(schemaDomain);
        } else if (targetType == TargetType.Catalog) {
            DrCatalogDomain catalogDomain = new DrCatalogDomain();
            catalogDomain.setAuditKind(SecQueryKind.ALTER);
            catalogDomain.setSqlType(SecQueryType.RENAME_CATALOG);
            catalogDomain.setCatalog(nameList.get(0));
            catalogDomain.setNewName(nameList.get(1));
            catalogDomain.setOptions(new HashMap<>());
            return Collections.singletonList(catalogDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }

}
