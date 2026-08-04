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
package com.clougence.sql.common.analysis.secrules.builder.factory;

import java.util.*;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.*;
import com.clougence.sql.common.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.enums.NameType;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.utils.CollectionUtils;

public abstract class RdbBuilderFactory {

    protected final Stack<DomainBuilder>    domainStack = new Stack<>();
    protected final List<RuleDomain>        ruleDomains = new ArrayList<>();
    protected Stack<List<WithSelectDomain>> selectStack = new Stack<>();

    public RdbBuilderFactory(MetaService metaService){
    }

    protected DomainBuilder getCurrentBuilder() { return domainStack.peek(); }

    // get builder -----------------------------------
    protected SelectItemBuilder newSelectItemBuilder() {
        return new SelectItemBuilder();
    }

    protected FromDomainBuilder newFromDomainBuilder() {
        return new FromDomainBuilder();
    }

    protected SelectTableDomainBuilder newTableDomainBuilder() {
        return new SelectTableDomainBuilder(selectStack);
    }

    protected abstract SelectDomainBuilder<? extends RdbSelectDomain> newSelectDomainBuilder();

    protected CreateUserBuilder<? extends RdbUserDomain> getCreateUserBuilder() { return new CreateUserBuilder(); }

    protected TableJoinBuilder getJoinBuilder(String text) {
        return new TableJoinBuilder(text);
    }

    protected abstract ColumnDefBuilder<? extends RdbColumnDomain> getColumnDefBuilder();

    protected InsertBuilder getInsertBuilder() { return new InsertBuilder(); }

    protected DeleteDomainBuilder getDeleteDomainBuilder() { return new DeleteDomainBuilder(); }

    protected abstract CatalogDomainBuilder<? extends RdbCatalogDomain> getCatalogDomainBuilder(RuleQueryType type);

    protected GrantBuilder getGrantBuilder() { return new GrantBuilder(); }

    protected RenameBuilder getRenameBuilder(TargetType targetType) {
        return new RenameBuilder(targetType);
    }

    protected abstract CommentBuilder getCommentBuilder(TargetType targetType);

    protected CallDomainBuilder getCallDomainBuilder() { return new CallDomainBuilder(); }

    protected abstract CreateTableBuilder<? extends RdbTableDomain> getCreateTableBuilder();

    protected abstract DropTableBuilder<? extends RdbTableDomain> getDropTableBuilder();

    protected abstract CreateSchemaBuilder<? extends RdbSchemaDomain> getCreateSchemaBuilder();

    protected UpdateBuilder getUpdateBuilder(Stack<List<WithSelectDomain>> selectStack) {
        return new UpdateBuilder(selectStack);
    }

    protected DomainBuilder getAlterSchemaBuilder() {
        throw new UnsupportedOperationException("unsupported alter schema");
    }

    protected DropSchemaBuilder<? extends RdbSchemaDomain> getDropSchemaBuilder() {
        throw new UnsupportedOperationException("unsupported drop schema");
    }

    protected DropRoleBuilder getDropRoleBuilder() { return new DropRoleBuilder(); }

    protected CreateRoleBuilder getCreateRoleBuilder() { return new CreateRoleBuilder(); }

    public void addDomain(RuleDomain domain) {
        this.ruleDomains.add(domain);
    }

    // get builder -----------------------------------

    // build -------------------------------
    public List<RuleDomain> build() {
        List<RuleDomain> domains = new ArrayList<>();
        for (RuleDomain ruleDomain : ruleDomains) {
            parseDomain(domains, ruleDomain, true);
        }
        return domains;
    }

    public List<RuleDomain> build(String uid, long dsId, Map<UmiTypes, Object> levelsParam) {
        return this.build();
    }

    public List<RuleDomain> buildKeepOrigin() {
        return ruleDomains;
    }

    protected void parseDomain(List<RuleDomain> domains, RuleDomain ruleDomain, boolean needAdd) {
        if (needAdd && !(ruleDomain instanceof RdbConstantDomain)) {
            domains.add(ruleDomain);
        }

        if (ruleDomain instanceof RdbSelectDomain) {
            parseSelectDomain(domains, (RdbSelectDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbCallDomain) {
            parseCallDomain(domains, (RdbCallDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbTableDomain) {
            parseTableDomain(domains, (RdbTableDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbInsertDomain) {
            parseInsertDomain(domains, (RdbInsertDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbDeleteDomain) {
            parseDeleteDomain(domains, (RdbDeleteDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbUpdateDomain) {
            parseUpdateDomain(domains, (RdbUpdateDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbSchemaDomain) {
            parseSchemaDomain(domains, (RdbSchemaDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbIndexDomain) {
            parseIndexDomain(domains, (RdbIndexDomain) ruleDomain);
        } else if (ruleDomain instanceof RdbConfigDomain) {
            parseConfigDomain(domains, (RdbConfigDomain) ruleDomain);
        }
    }

    private void parseIndexDomain(List<RuleDomain> domains, RdbIndexDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain child : ruleDomain.getChildren()) {
                parseDomain(domains, child, true);
            }
        }
    }

    private void parseConfigDomain(List<RuleDomain> domains, RdbConfigDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain child : ruleDomain.getChildren()) {
                parseDomain(domains, child, true);
            }
        }
    }

    private void parseSchemaDomain(List<RuleDomain> domains, RdbSchemaDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain child : ruleDomain.getChildren()) {
                parseDomain(domains, child, true);
            }
        }
    }

    private void parseUpdateDomain(List<RuleDomain> domains, RdbUpdateDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
                for (RuleDomain childDomain : ruleDomain.getChildren()) {
                    parseDomain(domains, childDomain, true);
                }
            }
        }
    }

    private void parseDeleteDomain(List<RuleDomain> domains, RdbDeleteDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain childDomain : ruleDomain.getChildren()) {
                parseDomain(domains, childDomain, true);
            }
        }
    }

    protected void parseInsertDomain(List<RuleDomain> domains, RdbInsertDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain childDomain : ruleDomain.getChildren()) {
                parseDomain(domains, childDomain, true);
            }
        }
    }

    protected void parseSelectDomain(List<RuleDomain> domains, RdbSelectDomain rdbSelectDomain) {
        List<QueryItem> columns = rdbSelectDomain.getColumns();
        for (QueryItem column : columns) {
            for (RuleDomain columnColumn : column.getColumns()) {
                if (columnColumn instanceof RdbColumnDomain) {
                    continue;
                } else if (columnColumn instanceof RdbVariableDomain) {
                    continue;
                }
                parseDomain(domains, columnColumn, true);
            }
        }

        if (CollectionUtils.isNotEmpty(rdbSelectDomain.getChildren())) {
            for (RuleDomain childDomain : rdbSelectDomain.getChildren()) {
                parseDomain(domains, childDomain, !(childDomain instanceof RdbTableDomain));

            }
        }

        for (RuleDomain whereDomain : rdbSelectDomain.getWhereDomains()) {
            if (whereDomain instanceof RdbCallDomain) {
                parseDomain(domains, whereDomain, true);
            } else if (whereDomain instanceof RdbSelectDomain) {
                parseDomain(domains, whereDomain, true);
            }
        }
    }

    private void parseTableDomain(List<RuleDomain> domains, RdbTableDomain ruleDomain) {
        if (CollectionUtils.isNotEmpty(ruleDomain.getChildren())) {
            for (RuleDomain child : ruleDomain.getChildren()) {
                parseDomain(domains, child, !(child instanceof RdbTableDomain));

            }
        }

        if (CollectionUtils.isNotEmpty(ruleDomain.getColumnDomains())) {
            for (RdbColumnDomain columnDomain : ruleDomain.getColumnDomains()) {
                parseDomain(domains, columnDomain, true);
            }
        }

        if (CollectionUtils.isNotEmpty(ruleDomain.getConstraintDomains())) {
            for (RdbConstraintDomain constraintDomain : ruleDomain.getConstraintDomains()) {
                parseDomain(domains, constraintDomain, true);
            }
        }

        if (CollectionUtils.isNotEmpty(ruleDomain.getIndexDomains())) {
            for (RdbIndexDomain constraintDomain : ruleDomain.getIndexDomains()) {
                parseDomain(domains, constraintDomain, true);
            }
        }

    }

    private void parseCallDomain(List<RuleDomain> domains, RdbCallDomain ruleDomain) {
        if (CollectionUtils.isEmpty(ruleDomain.getChildren())) {
            return;
        }
        for (RuleDomain child : ruleDomain.getChildren()) {
            if (child instanceof RdbColumnDomain) {
                continue;
            } else if (child instanceof RdbVariableDomain) {
                continue;
            }
            parseDomain(domains, child, true);
        }
    }

    // build -------------------------------
    public void handleDomain(Domain rdbColumnDomain, DomainSource type) {
        getCurrentBuilder().handleSubDomain(Collections.singletonList(rdbColumnDomain), type);
    }

    protected void exitBuilder(DomainSource source) {
        DomainBuilder pop = this.domainStack.pop();
        List<Domain> domains = pop.build();
        if (domainStack.isEmpty()) {
            for (Domain domain : domains) {
                this.ruleDomains.add((RuleDomain) domain);
            }
        } else {
            getCurrentBuilder().handleSubDomain(domains, source);
        }
    }

    // select --------------------------------------------------------------------------
    public void enterSelectDomain() {
        domainStack.add(newSelectDomainBuilder());
        selectStack.add(new ArrayList<>());
    }

    public void exitSelectDomain() {
        DomainBuilder pop = domainStack.pop();
        if (domainStack.isEmpty()) {
            List<Domain> build = pop.build();
            for (Domain domain : build) {
                ruleDomains.add((RuleDomain) domain);
            }
        } else {
            getCurrentBuilder().handleSubDomain(pop.build(), DomainSource.SELECT);
        }
        selectStack.pop();
    }

    public void handleSelectDomain(Handler handler) {
        enterSelectDomain();
        handler.handle();
        exitSelectDomain();
    }

    @Deprecated
    public void enterBuildSelectItem() {
        SelectItemBuilder selectColumnDomainBuilder = newSelectItemBuilder();
        this.domainStack.add(selectColumnDomainBuilder);
    }

    @Deprecated
    public void exitBuildSelectItem() {
        exitBuilder(DomainSource.SELECT_ITEM);
    }

    public void handleBuildSelectItem(Handler handler) {
        enterBuildSelectItem();
        handler.handle();
        exitBuildSelectItem();
    }

    @Deprecated
    public void enterSelectFromBuilder() {
        this.domainStack.add(newFromDomainBuilder());
    }

    @Deprecated
    public void exitSelectFromBuilder() {
        exitBuilder(DomainSource.FROM);
    }

    public void handleSelectFrom(Handler handler) {
        enterSelectFromBuilder();
        handler.handle();
        exitSelectFromBuilder();
    }

    @Deprecated
    public void enterSelectTableBuilder() {
        this.domainStack.add(newTableDomainBuilder());
    }

    @Deprecated
    public void exitSelectTableBuilder() {
        exitBuilder(DomainSource.TABLE);
    }

    public void handleSelectTable(Handler handler) {
        enterSelectTableBuilder();
        handler.handle();
        exitSelectTableBuilder();
    }

    @Deprecated
    public void enterWhere() {
        this.domainStack.add(new WhereDomainBuilder());
    }

    @Deprecated
    public void exitWhere() {
        exitBuilder(DomainSource.WHERE);
    }

    public void handleWhere(Handler handler) {
        enterWhere();
        handler.handle();
        exitWhere();
    }

    /**
     * join
     */
    public void enterJoin(String text) {
        domainStack.add(getJoinBuilder(text));
    }

    public void exitJoin() {
        exitBuilder(DomainSource.JOIN);
    }

    @Deprecated
    public void enterWithSelect() {
        this.domainStack.add(new WithSelectBuilder());
    }

    @Deprecated
    public void exitWithSelect() {
        DomainBuilder pop = this.domainStack.pop();
        List<Domain> build = pop.build();
        this.selectStack.peek().add((WithSelectDomain) build.get(0));
    }

    public void handleWithSelect(Handler handler) {
        enterWithSelect();
        handler.handle();
        exitWithSelect();
    }

    // select --------------------------------------------------------------------------

    // common -------------------------------------------------------------------

    // common -------------------------------------------------------------------

    // func   -------------------------------------------------------------------
    @Deprecated
    public void enterCall() {
        this.domainStack.add(getCallDomainBuilder());
    }

    @Deprecated
    public void exitCall() {
        DomainBuilder pop = this.domainStack.pop();
        if (domainStack.isEmpty()) {
            List<Domain> build = pop.build();
            for (Domain ruleDomain : build) {
                RdbCallDomain rdbCallDomain = (RdbCallDomain) ruleDomain;
                rdbCallDomain.setFunc(false);
            }
            for (Domain domain : build) {
                ruleDomains.add((RuleDomain) domain);
            }
        } else {
            getCurrentBuilder().handleSubDomain(pop.build(), DomainSource.FUNCTION);
        }
    }

    public void handleCall(Handler handler) {
        enterCall();
        handler.handle();
        exitCall();
    }

    @Deprecated
    public void enterFunctionArgs() {
        this.domainStack.add(new FunctionArgBuilder());
    }

    @Deprecated
    public void exitFunctionArgs() {
        exitBuilder(DomainSource.FUNCTION_ARGS);
    }

    public void handleFunctionArgs(Handler handler) {
        enterFunctionArgs();
        handler.handle();
        exitFunctionArgs();
    }

    public void handleOtherDomains(Handler handler) {
        this.domainStack.add(new OtherDomainBuilder());
        handler.handle();
        DomainBuilder pop = this.domainStack.pop();
        for (Domain domain : pop.build()) {
            this.ruleDomains.add((RuleDomain) domain);
        }
    }

    // func   -------------------------------------------------------------------

    /**
     * constant
     */
    public void enterConstant() {
        this.domainStack.add(new ConstantDomainBuilder());
    }

    public void exitConstant() {
        exitBuilder(DomainSource.CONSTANT);
    }

    /**
     * column
     */
    @Deprecated
    public void enterSelectColumn() {
        this.domainStack.add(new ColumnDomainBuilder());
    }

    @Deprecated
    public void exitSelectColumn() {
        exitBuilder(DomainSource.COLUMN);
    }

    public void handleSelectColumn(Handler handler) {
        enterSelectColumn();
        handler.handle();
        exitSelectColumn();
    }

    // alter table -----------------------

    public void enterAlterTable() {
        this.domainStack.add(new AlterTableBuilder());
    }

    public void exitAlterTable() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleAlterTable(Handler handler) {
        enterAlterTable();
        handler.handle();
        exitAlterTable();
    }

    @Deprecated
    public void enterAlterTableItem(AlterTableType type) {
        this.domainStack.add(new AlterTableItemBuilder(type));
    }

    @Deprecated
    public void exitAlterTableItem() {
        exitBuilder(DomainSource.ALTER_TABLE_ITEM);
    }

    public void handleAlterTableItem(Handler handler, AlterTableType type) {
        enterAlterTableItem(type);
        handler.handle();
        exitAlterTableItem();
    }

    @Deprecated
    public void enterColumnDef() {
        this.domainStack.add(getColumnDefBuilder());
    }

    @Deprecated
    public void exitColumnDef() {
        exitBuilder(DomainSource.COLUMN_DEF);
    }

    public void handleColumnDef(Handler handler) {
        enterColumnDef();
        handler.handle();
        exitColumnDef();
    }
    // alter table -----------------------

    // insert --------------------------------------------------------------
    @Deprecated
    public void enterInsertBuilder() {
        this.domainStack.add(getInsertBuilder());
    }

    @Deprecated
    public void exitInsertBuilder() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleInsert(Handler handler) {
        enterInsertBuilder();
        handler.handle();
        exitInsertBuilder();
    }

    @Deprecated
    public void enterInsertColumnBuilder() {
        this.domainStack.add(new InsertColumnBuilder());
    }

    @Deprecated
    public void exitInsertColumnBuilder() {
        exitBuilder(DomainSource.INSERT_COLUMN);
    }

    public void handleInsertColumn(Handler handler) {
        enterInsertColumnBuilder();
        handler.handle();
        exitInsertColumnBuilder();
    }

    @Deprecated
    public void enterValuesBuilder() {
        this.domainStack.add(new ValuesBuilder());
    }

    @Deprecated
    public void exitValuesBuilder() {
        exitBuilder(DomainSource.VALUES);
    }

    public void handleValues(Handler handler) {
        enterValuesBuilder();
        handler.handle();
        exitValuesBuilder();
    }

    // insert --------------------------------------------------------------

    // delete --------------------------------------------------------------

    @Deprecated
    public void enterDelete() {
        this.domainStack.add(getDeleteDomainBuilder());
    }

    @Deprecated
    public void exitDelete() {
        exitBuilder(DomainSource.DELETE);
    }

    public void handleDelete(Handler handler) {
        enterDelete();
        handler.handle();
        exitDelete();
    }

    // create catalog ---------------------------------------------------------

    public void enterCreateCatalog() {
        this.domainStack.add(getCatalogDomainBuilder(RuleQueryType.CREATE_CATALOG));
    }

    public void exitCreateCatalog() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropCatalog() {
        this.domainStack.add(getCatalogDomainBuilder(RuleQueryType.DROP_CATALOG));
    }

    public void exitDropCatalog() {
        exitBuilder(DomainSource.NONE);
    }

    // create catalog ---------------------------------------------------------

    // attribute

    public void addAttr(Attribute attribute, Object value) {
        getCurrentBuilder().addAttr(attribute, value);
    }

    @Deprecated
    public void enterRename(TargetType targetType) {
        this.domainStack.add(getRenameBuilder(targetType));
    }

    @Deprecated
    public void exitRename() {
        exitBuilder(DomainSource.RENAME);
    }

    public void handleRename(Handler handler, TargetType targetType) {
        enterRename(targetType);
        handler.handle();
        exitRename();
    }

    public void enterComment(TargetType targetType) {
        this.domainStack.add(getCommentBuilder(targetType));
    }

    public void exitComment() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterColumnDefault() {
        this.domainStack.add(new DefaultBuilder());
    }

    public void exitColumnDefault() {
        exitBuilder(DomainSource.COLUMN_DEFAULT_VALUE);
    }

    @Deprecated
    public void enterColumnList() {
        this.domainStack.add(new ColumnListBuilder());
    }

    @Deprecated
    public void exitColumnList() {
        exitBuilder(DomainSource.COLUMN_LIST);
    }

    public void handleColumnList(Handler handler) {
        enterColumnList();
        handler.handle();
        exitColumnList();
    }

    @Deprecated
    public void enterConstraint() {
        this.domainStack.add(new ConstraintBuilder());
    }

    @Deprecated
    public void exitConstraint() {
        exitBuilder(DomainSource.CONSTRAINT);
    }

    public void handleConstraint(Handler handler) {
        enterConstraint();
        handler.handle();
        exitConstraint();
    }

    @Deprecated
    public void enterCreateTable() {
        this.domainStack.add(getCreateTableBuilder());
    }

    @Deprecated
    public void exitCreateTable() {
        exitBuilder(DomainSource.CREATE_TABLE);
    }

    public void handleCreateTable(Handler handler) {
        enterCreateTable();
        handler.handle();
        exitCreateTable();
    }

    public void enterObjName() {
        this.domainStack.add(new ObjNameBuilder(null));
    }

    public void enterObjName(NameType type) {
        this.domainStack.add(new ObjNameBuilder(type));
    }

    public void exitObjName() {
        exitBuilder(DomainSource.OBJ_NAME);
    }

    public void handleObjName(Handler handler) {
        this.domainStack.add(new ObjNameBuilder(null));
        handler.handle();
        exitBuilder(DomainSource.OBJ_NAME);
    }

    public void handleObjName(Handler handler, NameType type) {
        this.domainStack.add(new ObjNameBuilder(type));
        handler.handle();
        exitBuilder(DomainSource.OBJ_NAME);
    }

    @Deprecated
    public void enterDropTable() {
        this.domainStack.add(getDropTableBuilder());
    }

    @Deprecated
    public void exitDropTable() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleDropTable(Handler handler) {
        enterDropTable();
        handler.handle();
        exitDropTable();
    }

    @Deprecated
    public void enterUpdate() {
        this.domainStack.add(getUpdateBuilder(selectStack));
        selectStack.add(new ArrayList<>());
    }

    @Deprecated
    public void exitUpdate() {
        exitBuilder(DomainSource.UPDATE);
        selectStack.pop();
    }

    public void handleUpdate(Handler handler) {
        enterUpdate();
        handler.handle();
        exitUpdate();
    }

    public void enterSetColumnValue() {
        this.domainStack.add(new SetValueBuilder());
    }

    public void exitSetColumnValue() {
        exitBuilder(DomainSource.SET_VALUE);
    }

    public void handleSetColumnValue(Handler handler) {
        enterSetColumnValue();
        handler.handle();
        exitSetColumnValue();
    }

    public void handleUpdateColumn(Handler handler) {
        this.domainStack.add(new UpdateColumnBuilder());
        handler.handle();
        exitBuilder(DomainSource.UPDATE_COLUMN);
    }

    @Deprecated
    public void enterCreateSchema() {
        this.domainStack.add(getCreateSchemaBuilder());
    }

    @Deprecated
    public void exitCreateSchema() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleCreateSchema(Handler handler) {
        enterCreateSchema();
        handler.handle();
        exitCreateSchema();
    }

    @Deprecated
    public void enterDropSchema() {
        this.domainStack.add(getDropSchemaBuilder());
    }

    @Deprecated
    public void exitDropSchema() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleDropSchema(Handler handler) {
        enterDropSchema();
        handler.handle();
        exitDropSchema();
    }

    public void enterCreateRole() {
        this.domainStack.add(getCreateRoleBuilder());
    }

    public void exitCreateRole() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateUser() {
        this.domainStack.add(getCreateUserBuilder());
    }

    public void exitCreateUser() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropUser() {
        this.domainStack.add(getDropUserBuilder());
    }

    protected DropUserBuilder getDropUserBuilder() { return new DropUserBuilder(); }

    public void exitDropUser() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropRole() {
        this.domainStack.add(getDropRoleBuilder());
    }

    public void exitDropRole() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterGrant() {
        this.domainStack.add(getGrantBuilder());
    }

    public void exitGrant() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterRevoke() {
        this.domainStack.add(getRevokeBuilder());
    }

    protected RevokeBuilder getRevokeBuilder() { return new RevokeBuilder(); }

    public void exitRevoke() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterColumnType(String fullName) {
        this.domainStack.add(new ColumnTypeBuilder(fullName));
    }

    public void exitColumnType() {
        exitBuilder(DomainSource.COLUMN_TYPE);
    }

    public void handleColumnType(Handler handler, String fullName) {
        enterColumnType(fullName);
        handler.handle();
        exitColumnType();
    }

    public void enterCreateFunction() {
        this.domainStack.add(new CreateFunctionBuilder());
    }

    public void exitCreateFunction() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleCreateEvent(Handler handler) {
        this.domainStack.add(new CreateEventBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void handleAnalyzeTable(Handler handler) {
        this.domainStack.add(new AnalyzeTableBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateTrigger() {
        this.domainStack.add(new CreateTriggerBuilder());
    }

    public void exitCreateTrigger() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterAlterTrigger() {
        this.domainStack.add(new AlterTriggerBuilder());
    }

    public void exitAlterTrigger() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropSequence() {
        this.domainStack.add(new DropSequenceBuilder());
    }

    public void exitDropSequence() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropTrigger() {
        this.domainStack.add(new DropTriggerBuilder());
    }

    public void exitDropTrigger() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateProcedure() {
        this.domainStack.add(new CreateProcedureBuilder());
    }

    public void enterExitProcedure() {
        exitBuilder(DomainSource.NONE);
    }

    @Deprecated
    public void enterCreateView() {
        this.enterView(RuleQueryType.CREATE_VIEW);
    }

    public void enterView(RuleQueryType type) {
        this.domainStack.add(new CreateViewBuilder(type));
    }

    public void exitView() {
        exitBuilder(DomainSource.NONE);
    }

    @Deprecated
    public void exitCreateView() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleCreateView(Handler handler) {
        enterCreateView();
        handler.handle();
        exitCreateView();
    }

    @Deprecated
    public void enterCreateIndex() {
        this.domainStack.add(new CreateIndexBuilder());
    }

    @Deprecated
    public void exitCreateIndex() {
        exitBuilder(DomainSource.INDEX);
    }

    public void handleCreateIndex(Handler handler) {
        enterCreateIndex();
        handler.handle();
        exitCreateIndex();
    }

    public void enterDropFunction() {
        this.domainStack.add(new DropFunctionBuilder());
    }

    public void exitDropFunction() {
        exitBuilder(DomainSource.NONE);
    }

    @Deprecated
    public void enterDropView() {
        this.domainStack.add(new DropViewBuilder());
    }

    @Deprecated
    public void exitDropView() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleDropView(Handler handler) {
        enterDropView();
        handler.handle();
        exitDropView();
    }

    public void enterDropProcedure() {
        this.domainStack.add(new DropProcedureBuilder());
    }

    public void exitDropProcedure() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleDropEvent(Handler handler) {
        this.domainStack.add(new DropEventBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void handleOptimizeTable(Handler handler) {
        this.domainStack.add(new OptimizeTableBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void handleResource(Handler handler, RuleQueryType sqlType, SecQueryKind auditKind, boolean needSupply, TargetType targetType) {
        this.domainStack.add(new ResourceBuilder(sqlType, auditKind, needSupply, targetType));
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    public void enterAlterSchema() {
        this.domainStack.add(getAlterSchemaBuilder());
    }

    public void exitAlterSchema() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterDropIndex() {
        this.domainStack.add(new DropIndexBuilder());
    }

    public void exitDropIndex() {
        exitBuilder(DomainSource.INDEX);
    }

    public void enterOptionsBuilder() {
        this.domainStack.add(new OptionsBuilder());
    }

    public void exitOptionsBuilder() {
        exitBuilder(DomainSource.OPTIONS);
    }

    public void enterAlterView() {
        this.domainStack.add(new AlterViewBuilder());
    }

    public void enterAlterView(RuleQueryType type) {
        this.domainStack.add(new AlterViewBuilder(type));
    }

    public void exitAlterView() {
        exitBuilder(DomainSource.NONE);
    }

    protected DomainBuilder getTruncateBuilder() { return new TruncateBuilder(); }

    public void enterTruncateTable() {
        this.domainStack.add(getTruncateBuilder());
    }

    public void exitTruncateTable() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleTruncateTable(Handler handler) {
        enterTruncateTable();
        handler.handle();
        exitTruncateTable();
    }

    public void enterCreateSequence() {
        this.domainStack.add(new CreateSequenceBuilder());
    }

    public void exitCreateSequence() {
        exitBuilder(DomainSource.NONE);
    }

    public void enterCreateSynonym() {
        this.domainStack.add(new CreateSynonymBuilder());
    }

    public void exitCreateSynonym() {
        exitBuilder(DomainSource.NONE);
    }

    public void handleCreateObject(Handler handler) {
        this.domainStack.add(new CreateObjectBuilder());
        handler.handle();
        exitBuilder(DomainSource.NONE);
    }

    @FunctionalInterface
    public interface Handler {

        void handle();
    }
}
