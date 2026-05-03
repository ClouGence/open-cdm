package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MyBuilderFactory extends RdbBuilderFactory {

    public MyBuilderFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected CreateUserBuilder getCreateUserBuilder() { return new MyCreateUserBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new MyUpdateBuilder(selectStack);
    }

    @Override
    protected DropUserBuilder getDropUserBuilder() { return new MyDropUserBuilder(); }

    @Override
    protected GrantBuilder getGrantBuilder() { return new MyGrantBuilder(); }

    @Override
    protected RevokeBuilder getRevokeBuilder() { return new MyRevokeBuilder(); }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new MySelectDomainBuilder(selectStack);
    }

    @Override
    protected TableJoinBuilder getJoinBuilder(String text) {
        return new MyTableJoinBuilder(text);
    }

    @Override
    protected InsertBuilder getInsertBuilder() { return new MyInsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new MyDeleteBuilder(); }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return null;
    }

    protected DomainBuilder getAlterSchemaBuilder() { return new MyAlterSchemaBuilder(); }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<MyTableDomain> getCreateTableBuilder() { return null; }

    @Override
    protected DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder() { return new MyDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<MySchemaDomain> getCreateSchemaBuilder() { return new MyCreateSchemaBuilder(); }

    protected DropSchemaBuilder<MySchemaDomain> getDropSchemaBuilder() { return new MyDropSchemaBuilder(); }

    protected MyColumnDefBuilder getColumnDefBuilder() { return new MyColumnDefBuilder(); }

    public void addDomain(RuleDomain domain) {
        this.ruleDomains.add(domain);
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

    public void enterRename(TargetType targetType) {
        this.domainStack.add(new MyRenameBuilder(targetType));
    }

    public void enterCreateTable(SecQueryType type) {
        this.domainStack.add(new MyCreateTableBuilder(type));
    }

    @Override
    public void enterAlterTable() {
        this.domainStack.add(new MyAlterTableBuilder());
    }

    public void enterAlterTableItem(AlterTableType type) {
        if (type == AlterTableType.ALTER_COLUMN) {
            this.domainStack.add(new MyColumnAlterTableItemBuilder(type));
        } else {
            this.domainStack.add(new AlterTableItemBuilder(type));
        }

    }

    public void handleReplace(Handler handler) {
        this.domainStack.add(new MyReplaceBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateIndex() {
        this.domainStack.add(new MyCreateIndexBuilder());
    }
}
