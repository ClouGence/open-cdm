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
package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSchemaDomain;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
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
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class McBuilderFactory extends RdbBuilderFactory {

    private final boolean schemaEnabled;

    public boolean getSchemaEnabled() { return this.schemaEnabled; }

    public McBuilderFactory(MetaService metaService, Boolean schemaEnabled){
        super(metaService);
        if (schemaEnabled == null) {
            this.schemaEnabled = false;
        } else {
            this.schemaEnabled = schemaEnabled;
        }
    }

    @Override
    protected SelectTableDomainBuilder newTableDomainBuilder() {
        return new McTableDomainBuilder(schemaEnabled, selectStack);
    }

    @Override
    protected CreateUserBuilder getCreateUserBuilder() { return new McCreateUserBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new McUpdateBuilder(selectStack);
    }

    @Override
    protected DropUserBuilder getDropUserBuilder() { return new McDropUserBuilder(); }

    @Override
    protected GrantBuilder getGrantBuilder() { return new McGrantBuilder(); }

    @Override
    protected RevokeBuilder getRevokeBuilder() { return new McRevokeBuilder(); }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new McSelectDomainBuilder(selectStack);
    }

    @Override
    protected TableJoinBuilder getJoinBuilder(String text) {
        return new McTableJoinBuilder(text);
    }

    @Override
    protected InsertBuilder getInsertBuilder() { return new McInsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new McDeleteBuilder(); }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return null;
    }

    protected DomainBuilder getAlterSchemaBuilder() { return new McAlterSchemaBuilder(); }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<McTableDomain> getCreateTableBuilder() { return null; }

    @Override
    protected DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder() { return new McDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<McSchemaDomain> getCreateSchemaBuilder() { return new McCreateSchemaBuilder(); }

    protected DropSchemaBuilder<McSchemaDomain> getDropSchemaBuilder() { return new McDropSchemaBuilder(); }

    protected McColumnDefBuilder getColumnDefBuilder() { return new McColumnDefBuilder(); }

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
        this.domainStack.add(new McRenameBuilder(targetType));
    }

    public void enterCreateTable(SecQueryType type) {
        this.domainStack.add(new McCreateTableBuilder(type, schemaEnabled));
    }

    @Override
    public void enterAlterTable() {
        this.domainStack.add(new McAlterTableBuilder(schemaEnabled));
    }

    public void enterAlterTableItem(AlterTableType type) {
        if (type == AlterTableType.ALTER_COLUMN) {
            this.domainStack.add(new McColumnAlterTableItemBuilder(type));
        } else {
            this.domainStack.add(new AlterTableItemBuilder(type));
        }

    }

    public void enterTruncate() {
        this.domainStack.add(new McTruncateBuilder());
    }

    public void exitTruncate() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateIndex() {
        this.domainStack.add(new McCreateIndexBuilder());
    }
}
