package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class OraBuilderFactory extends RdbBuilderFactory {

    public OraBuilderFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new OraSelectDomainBuilder(selectStack);
    }

    @Override
    protected ColumnDefBuilder<? extends RdbColumnDomain> getColumnDefBuilder() { return new OraColumnDefBuilder(); }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return null;
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return new OraCommentBuilder(targetType);
    }

    @Override
    protected InsertBuilder getInsertBuilder() { return new OraInsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new OraDeleteDomainBuilder(); }

    @Override
    protected CreateTableBuilder<OraTableDomain> getCreateTableBuilder() { return new OraCreateTableBuilder(); }

    @Override
    protected RenameBuilder getRenameBuilder(TargetType targetType) {
        return new OraRenameBuilder(targetType);
    }

    @Override
    protected DropTableBuilder<OraTableDomain> getDropTableBuilder() { return new OraDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<? extends RdbSchemaDomain> getCreateSchemaBuilder() { return null; }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new OraUpdateBuilder(selectStack);
    }

    protected FromDomainBuilder newFromDomainBuilder() {
        return new OraFromBuilder();
    }

    @Deprecated
    public void enterValuesBuilder() {
        this.domainStack.add(new OraValuesBuilder());
    }

    public void addAttr(Attribute attribute, Object value) {
        if (attribute == CommonAttribute.VALUE) {
            String text = (String) value;
            if (text.startsWith("\"")) {
                value = text.substring(1, text.length() - 1);
            }
        }
        getCurrentBuilder().addAttr(attribute, value);
    }

    protected DomainBuilder getTruncateBuilder() { return new OraTruncateBuilder(); }

}
