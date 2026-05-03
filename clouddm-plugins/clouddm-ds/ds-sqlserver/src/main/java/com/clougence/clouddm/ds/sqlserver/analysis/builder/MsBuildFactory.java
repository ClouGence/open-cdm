package com.clougence.clouddm.ds.sqlserver.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MsBuildFactory extends RdbBuilderFactory {

    public MsBuildFactory(MetaService metaService) {
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return null;
    }

    @Override
    protected ColumnDefBuilder<? extends RdbColumnDomain> getColumnDefBuilder() { return null; }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return null;
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<? extends RdbTableDomain> getCreateTableBuilder() { return null; }

    @Override
    protected DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder() { return null; }

    @Override
    protected CreateSchemaBuilder<? extends RdbSchemaDomain> getCreateSchemaBuilder() { return null; }
}
