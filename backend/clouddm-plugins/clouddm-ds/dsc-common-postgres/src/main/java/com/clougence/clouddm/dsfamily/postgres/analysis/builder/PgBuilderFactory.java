/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgCatalogDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PgBuilderFactory extends RdbBuilderFactory {

    public PgBuilderFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<PgSelectDomain> newSelectDomainBuilder() {
        return new PgSelectDomainBuilder(selectStack);
    }

    protected CreateUserBuilder<RdbUserDomain> getCreateUserBuilder() { return new PgCreateUserBuilder(); }

    @Override
    protected SelectItemBuilder newSelectItemBuilder() {
        return new PgSelectColumnBuilder();
    }

    @Override
    protected TableJoinBuilder getJoinBuilder(String text) {
        return new PgTableJoinBuilder(text);
    }

    @Override
    protected ColumnDefBuilder<PgColumnDomain> getColumnDefBuilder() { return new PgColumnDefBuilder(); }

    @Override
    protected InsertBuilder getInsertBuilder() { return new PgInsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new PgDeleteDomainBuilder(); }

    @Override
    protected CatalogDomainBuilder<PgCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return new PgCatalogDomainBuilder(type);
    }

    @Override
    protected CreateTableBuilder<PgTableDomain> getCreateTableBuilder() { return new PgCreateTableBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new PgUpdateBuilder(selectStack);
    }

    @Override
    protected RenameBuilder getRenameBuilder(TargetType targetType) {
        return new PgRenameBuilder(targetType);
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return new PgCommentBuilder(targetType);
    }

    @Override
    protected PgDropTableBuilder getDropTableBuilder() { return new PgDropTableBuilder(); }

    @Override
    protected PgCreateSchemaBuilder getCreateSchemaBuilder() { return new PgCreateSchemaBuilder(); }

    @Override
    protected PgDropSchemaBuilder getDropSchemaBuilder() { return new PgDropSchemaBuilder(); }

    @Override
    protected DropRoleBuilder getDropRoleBuilder() { return new PgDropRoleBuilder(); }

    @Override
    protected PgCreateRoleBuilder getCreateRoleBuilder() { return new PgCreateRoleBuilder(); }

    public void enterInherit() {
        this.domainStack.add(new PgInheritBuilder());
    }

    public void exitInherit() {
        DomainBuilder pop = this.domainStack.pop();
        getCurrentBuilder().handleSubDomain(pop.build(), DomainSource.INHERIT);
    }

    public void enterAlterOwner(TargetType targetType) {
        this.domainStack.add(new PgAlterOwnerBuilder(targetType));
    }

    public void exitAlterOwner() {
        DomainBuilder pop = this.domainStack.pop();
        for (Domain domain : pop.build()) {
            this.ruleDomains.add((RuleDomain) domain);
        }
    }

    public void enterAlterDatabase() {
        this.domainStack.add(new PgAlterDatabaseBuilder());
    }

    public void exitAlterDatabase() {
        DomainBuilder pop = this.domainStack.pop();
        for (Domain domain : pop.build()) {
            this.ruleDomains.add((RuleDomain) domain);
        }
    }

    @Override
    public void enterSetColumnValue() {
        this.domainStack.add(new PgSetValueBuilder());
    }

    public void enterSetInfo() {
        this.domainStack.add(new PgSetBuilder());
    }

    public void exitSetInfo() {
        DomainBuilder pop = this.domainStack.pop();
        getCurrentBuilder().handleSubDomain(pop.build(), DomainSource.SET_OPT);
    }

    public void enterRoleSpec(String text) {
        this.domainStack.add(new StringDomainBuilder(text));
    }

    public void exitRoleSpec() {
        DomainBuilder pop = this.domainStack.pop();
        getCurrentBuilder().handleSubDomain(pop.build(), DomainSource.ROLE_SPEC);
    }

    public void enterArray() {
        this.domainStack.add(new PgArrayBuilder());
    }

    public void exitArray() {
        exitBuilder(DomainSource.ARRAY);
    }

    public void enterColumnType(String fullName) {
        this.domainStack.add(new PgColumnTypeBuilder(fullName));
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

    protected DomainBuilder getTruncateBuilder() { return new PgTruncateBuilder(); }

}
