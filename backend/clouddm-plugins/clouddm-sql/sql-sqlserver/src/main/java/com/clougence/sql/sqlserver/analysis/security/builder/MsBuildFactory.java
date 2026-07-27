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
package com.clougence.sql.sqlserver.analysis.security.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.analysis.secrules.builder.*;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.sql.sqlserver.analysis.security.domain.*;

public class MsBuildFactory extends RdbBuilderFactory {

    public MsBuildFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new MsSelectDomainBuilder(selectStack);
    }

    @Override
    protected ColumnDefBuilder<MsColumnDomain> getColumnDefBuilder() { return new MsColumnDefBuilder(); }

    @Override
    protected InsertBuilder getInsertBuilder() { return new MsInsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new MsDeleteBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new MsUpdateBuilder(selectStack);
    }

    @Override
    protected CatalogDomainBuilder<MsCatalogDomain> getCatalogDomainBuilder(SplitQueryType type) {
        return new MsCatalogDomainBuilder(type);
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return new MsCommentBuilder(targetType);
    }

    @Override
    protected CreateTableBuilder<MsTableDomain> getCreateTableBuilder() { return new MsCreateTableBuilder(); }

    @Override
    protected DropTableBuilder<MsTableDomain> getDropTableBuilder() { return new MsDropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<MsSchemaDomain> getCreateSchemaBuilder() { return new MsCreateSchemaBuilder(); }

    @Override
    protected DropSchemaBuilder<MsSchemaDomain> getDropSchemaBuilder() { return new MsDropSchemaBuilder(); }

    @Override
    public void addAttr(Attribute attribute, Object value) {
        if (attribute == CommonAttribute.VALUE && value instanceof String text) {
            value = stripIdentifier(text);
        }
        getCurrentBuilder().addAttr(attribute, value);
    }

    private String stripIdentifier(String text) {
        if ((text.startsWith("[") && text.endsWith("]")) || (text.startsWith("\"") && text.endsWith("\""))) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    public MsCatalogDomain newCatalogDomain(SplitQueryType type) {
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public MsSchemaDomain newSchemaDomain(SplitQueryType type) {
        MsSchemaDomain domain = new MsSchemaDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public MsTableDomain newTableDomain(SplitQueryType type, SecQueryKind kind) {
        MsTableDomain domain = new MsTableDomain();
        domain.addSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public MsColumnDomain newColumnDomain(SplitQueryType type, SecQueryKind kind) {
        MsColumnDomain domain = new MsColumnDomain();
        domain.addSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public MsSelectDomain newSelectDomain() {
        MsSelectDomain domain = new MsSelectDomain();
        domain.addSqlType(SplitQueryType.SELECT);
        domain.setAuditKind(SecQueryKind.QUERY);
        domain.setMode(RdbQueryMode.NORMAL);
        domain.setSelectColumns(new ArrayList<>());
        domain.setSelectVariables(new ArrayList<>());
        domain.setSelectFunc(new ArrayList<>());
        domain.setSelectValue(new ArrayList<>());
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinTypes(new ArrayList<>());
        domain.setColumns(new ArrayList<>());
        domain.setWhereDomains(new ArrayList<>());
        domain.setEmptyFrom(true);
        domain.setSimpleSelect(true);
        return domain;
    }

    public QueryItem newQueryItem() {
        return new QueryItem();
    }

    public MsInsertDomain newInsertDomain() {
        MsInsertDomain domain = new MsInsertDomain();
        domain.addSqlType(SplitQueryType.INSERT);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public MsUpdateDomain newUpdateDomain() {
        MsUpdateDomain domain = new MsUpdateDomain();
        domain.addSqlType(SplitQueryType.UPDATE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public MsDeleteDomain newDeleteDomain() {
        MsDeleteDomain domain = new MsDeleteDomain();
        domain.addSqlType(SplitQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public RdbCallDomain newCallDomain() {
        RdbCallDomain domain = new RdbCallDomain();
        domain.addSqlType(SplitQueryType.CALL_PROG_OBJ);
        domain.setAuditKind(SecQueryKind.CALL);
        return domain;
    }

    public RdbViewDomain newViewDomain(SplitQueryType type) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbIndexDomain newIndexDomain(SplitQueryType type) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbConstraintDomain newConstraintDomain(SplitQueryType type) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbSequenceDomain newSequenceDomain(SplitQueryType type) {
        RdbSequenceDomain domain = new RdbSequenceDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbResourceDomain newResourceDomain(SplitQueryType type, SecQueryKind kind) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.addSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public RdbUserDomain newUserDomain(SplitQueryType type) {
        RdbUserDomain domain = new RdbUserDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbFunctionDomain newFunctionDomain(SplitQueryType type) {
        RdbFunctionDomain domain = new RdbFunctionDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbProcedureDomain newProcedureDomain(SplitQueryType type) {
        RdbProcedureDomain domain = new RdbProcedureDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbTriggerDomain newTriggerDomain(SplitQueryType type) {
        RdbTriggerDomain domain = new RdbTriggerDomain();
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }
}
