/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.sql.common.analysis.secrules.builder.*;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.factory.RdbBuilderFactory;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.sql.db2.analysis.security.domain.*;

public class Db2BuildFactory extends RdbBuilderFactory {

    public Db2BuildFactory(MetaService metaService){
        super(metaService);
    }

    @Override
    protected SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder() {
        return new Db2SelectDomainBuilder(selectStack);
    }

    @Override
    protected ColumnDefBuilder<Db2ColumnDomain> getColumnDefBuilder() { return new Db2ColumnDefBuilder(); }

    @Override
    protected InsertBuilder getInsertBuilder() { return new Db2InsertBuilder(); }

    @Override
    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new Db2DeleteBuilder(); }

    @Override
    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new Db2UpdateBuilder(selectStack);
    }

    @Override
    protected CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(RuleQueryType type) {
        return null;
    }

    @Override
    protected CommentBuilder getCommentBuilder(TargetType targetType) {
        return null;
    }

    @Override
    protected CreateTableBuilder<Db2TableDomain> getCreateTableBuilder() { return new Db2CreateTableBuilder(); }

    @Override
    protected DropTableBuilder<Db2TableDomain> getDropTableBuilder() { return new Db2DropTableBuilder(); }

    @Override
    protected CreateSchemaBuilder<Db2SchemaDomain> getCreateSchemaBuilder() { return new Db2CreateSchemaBuilder(); }

    @Override
    protected DropSchemaBuilder<Db2SchemaDomain> getDropSchemaBuilder() { return new Db2DropSchemaBuilder(); }

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

    public Db2SchemaDomain newSchemaDomain(RuleQueryType type) {
        Db2SchemaDomain domain = new Db2SchemaDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public Db2TableDomain newTableDomain(RuleQueryType type, SecQueryKind kind) {
        Db2TableDomain domain = new Db2TableDomain();
        domain.setSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public Db2ColumnDomain newColumnDomain(RuleQueryType type, SecQueryKind kind) {
        Db2ColumnDomain domain = new Db2ColumnDomain();
        domain.setSqlType(type);
        domain.setAuditKind(kind);
        return domain;
    }

    public Db2SelectDomain newSelectDomain(RdbQueryMode mode) {
        Db2SelectDomain domain = new Db2SelectDomain();
        domain.setSqlType(RuleQueryType.SELECT);
        domain.setAuditKind(SecQueryKind.QUERY);
        domain.setMode(mode);
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

    public Db2InsertDomain newInsertDomain() {
        Db2InsertDomain domain = new Db2InsertDomain();
        domain.setSqlType(RuleQueryType.INSERT);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public Db2UpdateDomain newUpdateDomain() {
        Db2UpdateDomain domain = new Db2UpdateDomain();
        domain.setSqlType(RuleQueryType.UPDATE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public Db2DeleteDomain newDeleteDomain() {
        Db2DeleteDomain domain = new Db2DeleteDomain();
        domain.setSqlType(RuleQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        return domain;
    }

    public RdbCallDomain newCallDomain() {
        RdbCallDomain domain = new RdbCallDomain();
        domain.setSqlType(RuleQueryType.CALL_PROG_OBJ);
        domain.setAuditKind(SecQueryKind.CALL);
        return domain;
    }

    public RdbViewDomain newViewDomain(RuleQueryType type) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbIndexDomain newIndexDomain(RuleQueryType type) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }

    public RdbConstraintDomain newConstraintDomain(RuleQueryType type) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        return domain;
    }
}
