package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class SrBuilderFactory extends RdbBuilderFactory {

    public SrBuilderFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new SrSelectDomainBuilder(selectStack);
    }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new SrDeleteBuilder(); }

    @Override
    protected InsertBuilder getInsertBuilder() { return new SrInsertBuilder(); }

    @Override
    protected ColumnDefBuilder<? extends RdbColumnDomain> getColumnDefBuilder() { return new SrColumnDefBuilder(); }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return new SrCatalogBuilder(type);
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<? extends RdbTableDomain> getCreateTableBuilder() { return new SrCreateTableBuilder(SecQueryType.CREATE_TABLE); }

    @Override
    protected DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder() { return new SrDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<? extends RdbSchemaDomain> getCreateSchemaBuilder() { return new SrCreateSchemaBuilder(); }

    @Override
    protected DropSchemaBuilder<? extends RdbSchemaDomain> getDropSchemaBuilder() { return new SrDropSchemaBuilder(); }

    @Override
    protected DomainBuilder getTruncateBuilder() { return new SrTruncateBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new SrUpdateBuilder(selectStack);
    }

    @Override
    public void addAttr(Attribute attribute, Object value) {
        if (attribute == CommonAttribute.VALUE) {
            String text = (String) value;
            if (text.startsWith("`")) {
                value = text.substring(1, text.length() - 1);
            }
        }
        getCurrentBuilder().addAttr(attribute, value);
    }

    public void enterAlterCatalog() {
        this.domainStack.add(getCatalogDomainBuilder(SecQueryType.ALTER_CATALOG));
    }

    public void exitAlterCatalog() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateTable(SecQueryType type) {
        this.domainStack.add(new SrCreateTableBuilder(type));
    }

    public void enterAlterTable() {
        this.domainStack.add(new SrAlterTableBuilder());
    }

    public void exitAlterTable() {
        exitBuilder(DomainSource.NONE);
    }

    public void addDomain(RuleDomain domain) {
        this.ruleDomains.add(domain);
    }
}
