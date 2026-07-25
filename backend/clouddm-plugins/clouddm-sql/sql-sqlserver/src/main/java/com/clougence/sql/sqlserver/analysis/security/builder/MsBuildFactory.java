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

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
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
    protected CatalogDomainBuilder<MsCatalogDomain> getCatalogDomainBuilder(SecQueryType type) {
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

    public MsCatalogDomain newCatalogDomain(SecQueryType type) {
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public MsSchemaDomain newSchemaDomain(SecQueryType type) {
        MsSchemaDomain domain = new MsSchemaDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public MsTableDomain newTableDomain(SecQueryType type, SecQueryKind kind) {
        MsTableDomain domain = new MsTableDomain();
        domain.setSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public MsColumnDomain newColumnDomain(SecQueryType type, SecQueryKind kind) {
        MsColumnDomain domain = new MsColumnDomain();
        domain.setSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public MsSelectDomain newSelectDomain() {
        MsSelectDomain domain = new MsSelectDomain();
        domain.setSqlType(SecQueryType.SELECT);
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
        domain.setSqlType(SecQueryType.INSERT);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public MsUpdateDomain newUpdateDomain() {
        MsUpdateDomain domain = new MsUpdateDomain();
        domain.setSqlType(SecQueryType.UPDATE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public MsDeleteDomain newDeleteDomain() {
        MsDeleteDomain domain = new MsDeleteDomain();
        domain.setSqlType(SecQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public RdbCallDomain newCallDomain() {
        RdbCallDomain domain = new RdbCallDomain();
        domain.setSqlType(SecQueryType.CALL_PROG_OBJ);
        domain.setAuditKind(SecQueryKind.CALL);
        return domain;
    }

    public RdbViewDomain newViewDomain(SecQueryType type) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbIndexDomain newIndexDomain(SecQueryType type) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbConstraintDomain newConstraintDomain(SecQueryType type) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbSequenceDomain newSequenceDomain(SecQueryType type) {
        RdbSequenceDomain domain = new RdbSequenceDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbResourceDomain newResourceDomain(SecQueryType type, SecQueryKind kind) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public RdbUserDomain newUserDomain(SecQueryType type) {
        RdbUserDomain domain = new RdbUserDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbFunctionDomain newFunctionDomain(SecQueryType type) {
        RdbFunctionDomain domain = new RdbFunctionDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbProcedureDomain newProcedureDomain(SecQueryType type) {
        RdbProcedureDomain domain = new RdbProcedureDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbTriggerDomain newTriggerDomain(SecQueryType type) {
        RdbTriggerDomain domain = new RdbTriggerDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }
}
