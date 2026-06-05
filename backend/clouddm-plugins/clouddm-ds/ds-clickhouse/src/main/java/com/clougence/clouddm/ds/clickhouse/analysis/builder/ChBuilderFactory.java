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
package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class ChBuilderFactory extends RdbBuilderFactory {

    public ChBuilderFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new ChSelectDomainBuilder(selectStack);
    }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new ChUpdateBuilder(selectStack);
    }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new ChDeleteBuilder(); }

    @Override
    protected InsertBuilder getInsertBuilder() { return new ChInsertBuilder(); }

    @Override
    protected ColumnDefBuilder<? extends RdbColumnDomain> getColumnDefBuilder() { return new ChColumnDefBuilder(); }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
        return null;
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<? extends RdbTableDomain> getCreateTableBuilder() { return new ChCreateTableBuilder(); }

    @Override
    protected DropSchemaBuilder<? extends RdbSchemaDomain> getDropSchemaBuilder() { return new ChDropSchemaBuilder(); }

    @Override
    protected DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder() { return new ChDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<? extends RdbSchemaDomain> getCreateSchemaBuilder() { return new ChCreateSchemaBuilder(); }

    @Override
    public void enterRename(TargetType targetType) {
        this.domainStack.add(new ChRenameBuilder(targetType));
    }

    public void enterAlterTable() {
        this.domainStack.add(new ChAlterTableBuilder());
    }

}
