/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

/** Access definitions are stored data; their filters, privilege words and values are not executed queries. */
final class ChAccessBehaviorVisitor extends ChStatementBehaviorVisitor {
    ChAccessBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
    }

    @Override
    public Void visitCreateUserStmt(CreateUserStmtContext ctx) {
        List<BehaviorObject> users = identities(ctx.accessUserNames(), TargetType.User, false);
        define(ctx, users, SplitQueryType.CREATE_USER, createAction(ctx.accessCreateGuard()), null);
        List<UserRolesContext> roles = descendants(ctx, UserRolesContext.class);
        List<UserDefaultRolesContext> defaults = descendants(ctx, UserDefaultRolesContext.class);
        AccessRoleSetContext grants = null;
        if (!roles.isEmpty()) {
            grants = roles.get(0).accessRoleSet();
        } else if (!defaults.isEmpty() && defaults.get(0).accessRoleSet().ALL() == null) {
            grants = defaults.get(0).accessRoleSet();
        }
        if (grants != null) {
            for (BehaviorObject role : roleSet(grants, TargetType.Role)) {
                add(SplitQueryType.CREATE_USER, BehaviorAction.GRANT, role, users);
            }
        }
        finishDefinition(ctx, users, false);
        return null;
    }

    @Override
    public Void visitAlterUserStmt(AlterUserStmtContext ctx) {
        List<BehaviorObject> users = identities(ctx.accessUserNames(), TargetType.User, false);
        AccessRenameContext rename = first(ctx, AccessRenameContext.class);
        boolean changes = ctx.alterUserClause().stream().anyMatch(c -> c.accessRename() == null && c.clusterClause() == null && c.accessStorage() == null);
        SplitQueryType type = SplitQueryType.ALTER_USER;
        if (rename != null && !changes) {
            type = SplitQueryType.RENAME_USER;
        }
        define(ctx, users, type, BehaviorAction.ALTER, rename);
        finishDefinition(ctx, users, true);
        return null;
    }

    @Override
    public Void visitDropUserStmt(DropUserStmtContext ctx) {
        remove(identities(ctx.accessUserNames(), TargetType.User, false), SplitQueryType.DROP_USER);
        return null;
    }

    @Override
    public Void visitCreateRoleStmt(CreateRoleStmtContext ctx) {
        List<BehaviorObject> roles = identities(ctx.accessUserNames(), TargetType.Role);
        define(ctx, roles, SplitQueryType.CREATE_ROLE, createAction(ctx.accessCreateGuard()), null);
        finishDefinition(ctx, roles, false);
        return null;
    }

    @Override
    public Void visitAlterRoleStmt(AlterRoleStmtContext ctx) {
        List<BehaviorObject> roles = identities(ctx.accessUserNames(), TargetType.Role);
        AccessRenameContext rename = first(ctx, AccessRenameContext.class);
        SplitQueryType type = SplitQueryType.ALTER_ROLE;
        if (rename != null && descendants(ctx, AlterAccessSettingsContext.class).isEmpty()) {
            type = SplitQueryType.RENAME_ROLE;
        }
        define(ctx, roles, type, BehaviorAction.ALTER, rename);
        finishDefinition(ctx, roles, true);
        return null;
    }

    @Override
    public Void visitDropRoleStmt(DropRoleStmtContext ctx) {
        remove(identities(ctx.accessUserNames(), TargetType.Role), SplitQueryType.DROP_ROLE);
        return null;
    }

    @Override
    public Void visitSetDefaultRoleStmt(SetDefaultRoleStmtContext ctx) {
        for (BehaviorObject user : identities(ctx.accessUserNames(), TargetType.User)) {
            add(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, user, roleSet(ctx.accessRoleSet(), TargetType.Role));
        }
        excluded(ctx.accessRoleSet(), TargetType.Role);
        return null;
    }

    private BehaviorAction createAction(AccessCreateGuardContext guard) {
        if (guard != null && guard.REPLACE() != null) {
            return BehaviorAction.REPLACE;
        }
        return BehaviorAction.CREATE;
    }

    private void define(ParserRuleContext ctx, List<BehaviorObject> owners, SplitQueryType type, BehaviorAction action, AccessRenameContext rename) {
        setType(type);
        List<BehaviorObject> dependencies = new ArrayList<>();
        definitionDependencies(ctx, dependencies);
        for (BehaviorObject owner : owners) {
            if (rename != null) {
                add(type, BehaviorAction.RENAME, owner, List.of(named(owner.getObjectType(), rename.accessName())));
                boolean changes = hasDefinitionChanges(ctx);
                if (!changes) {
                    continue;
                }
            }
            add(type, action, owner, dependencies);
        }
    }

    private boolean hasDefinitionChanges(ParserRuleContext ctx) {
        if (ctx instanceof AlterUserStmtContext user) {
            return user.alterUserClause().stream().anyMatch(c -> c.accessRename() == null && c.clusterClause() == null && c.accessStorage() == null);
        }
        if (ctx instanceof AlterRoleStmtContext role) {
            return role.alterRoleClause().stream().anyMatch(c -> c.alterAccessSettings() != null);
        }
        if (ctx instanceof AlterSettingsProfileStmtContext profile) {
            return !profile.alterAccessSettings().isEmpty() || profile.accessRoleSet() != null;
        }
        if (ctx instanceof AlterQuotaStmtContext quota) {
            return quota.accessRoleSet() != null || quota.quotaClause().stream().anyMatch(c -> c.clusterClause() == null && c.accessStorage() == null);
        }
        return false;
    }

    private void definitionDependencies(ParseTree tree, List<BehaviorObject> result) {
        if (tree instanceof UserRolesContext roles) {
            result.addAll(roleSet(roles.accessRoleSet(), TargetType.Role));
        } else if (tree instanceof UserDefaultRolesContext roles) {
            result.addAll(roleSet(roles.accessRoleSet(), TargetType.Role));
        } else if (tree instanceof UserDefaultDatabaseContext database) {
            if (database.accessName() != null) {
                result.add(objects.object(TargetType.Schema, database.accessName(), List.of(name(database.accessName()))));
            }
        } else if (tree instanceof UserGranteesContext grantees) {
            AccessGranteesContext set = grantees.accessGrantees();
            if (set.ANY() != null) {
                result.add(objects.instanceObject(TargetType.UserOrRole, set.ANY().getSymbol()));
            } else if (set.NONE() == null) {
                result.addAll(identities(set.accessUserNames(0), TargetType.UserOrRole));
            }
        } else if (tree instanceof AccessSettingsContext || tree instanceof AlterAccessSettingsContext) {
            for (AccessNameContext profile : descendants(tree, AccessNameContext.class)) {
                result.add(named(TargetType.Profile, profile));
            }
        } else if (tree instanceof AccessRoleSetContext roles) {
            result.addAll(roleSet(roles, TargetType.UserOrRole));
        } else {
            for (int i = 0; i < tree.getChildCount(); i++) {
                definitionDependencies(tree.getChild(i), result);
            }
        }
    }

    private void finishDefinition(ParserRuleContext ctx, List<BehaviorObject> owners, boolean alter) {
        for (BehaviorObject owner : owners) {
            settings(ctx, owner, alter);
        }
        for (AccessRoleSetContext roles : descendants(ctx, AccessRoleSetContext.class)) {
            TargetType type = TargetType.UserOrRole;
            if (roles.getParent() instanceof UserRolesContext || roles.getParent() instanceof UserDefaultRolesContext) {
                type = TargetType.Role;
            }
            excluded(roles, type);
        }
        for (AccessGranteesContext grantees : descendants(ctx, AccessGranteesContext.class)) {
            if (grantees.EXCEPT() != null) {
                List<AccessUserNamesContext> lists = grantees.accessUserNames();
                read(identities(lists.get(lists.size() - 1), TargetType.UserOrRole));
            }
        }
    }

    private void settings(ParseTree tree, BehaviorObject owner, boolean alter) {
        if (tree instanceof AccessSettingContext setting) {
            if (setting.nestedIdentifier() != null) {
                configure(owner, setting.nestedIdentifier());
            }
            return;
        }
        if (tree instanceof AlterAccessSettingContext setting) {
            if (setting.ALL() != null && setting.SETTINGS() != null) {
                BehaviorObject key = range(TargetType.ConfigKey, setting.ALL().getSymbol(), setting.SETTINGS().getSymbol());
                key.setObjectPath(owner.getObjectPath());
                key.setObjectName(new ObjectName(instanceRelativeName(owner), null, null));
                add(behavior().getStatementType(), BehaviorAction.CONFIGURE, key);
            } else {
                for (NestedIdentifierContext key : setting.nestedIdentifier()) {
                    configure(owner, key);
                }
                for (AccessSettingContext key : descendants(setting, AccessSettingContext.class)) {
                    settings(key, owner, alter);
                }
            }
            return;
        }
        if (alter && tree instanceof AccessSettingsContext setting && setting.NONE() != null) {
            BehaviorObject key = objects.instanceObject(TargetType.ConfigKey, setting.NONE().getSymbol());
            key.setObjectPath(owner.getObjectPath());
            key.setObjectName(new ObjectName(instanceRelativeName(owner), null, null));
            add(behavior().getStatementType(), BehaviorAction.CONFIGURE, key);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            settings(tree.getChild(i), owner, alter);
        }
    }

    private void configure(BehaviorObject owner, NestedIdentifierContext context) {
        List<String> parts = new ArrayList<>();
        for (IdentifierContext part : context.identifier()) {
            parts.add(name(part));
        }
        String keyName = String.join(".", parts);
        BehaviorObject key = objects.childObject(TargetType.ConfigKey, context, owner, keyName);
        key.setObjectName(new ObjectName(instanceRelativeName(owner), null, keyName));
        add(behavior().getStatementType(), BehaviorAction.CONFIGURE, key);
    }

    @Override
    public Void visitGrantStmt(GrantStmtContext ctx) {
        setType(SplitQueryType.GRANT);
        List<AccessUserNamesContext> lists = ctx.accessUserNames();
        List<BehaviorObject> recipients = identities(lists.get(lists.size() - 1), TargetType.UserOrRole);
        boolean membership = ctx.accessRights() == null && ctx.currentGrants() == null;
        if (ctx.replaceGrantOption() != null) {
            TargetType type = TargetType.Instance;
            if (membership) {
                type = TargetType.Role;
            }
            ReplaceGrantOptionContext replace = ctx.replaceGrantOption();
            add(SplitQueryType.GRANT, BehaviorAction.REVOKE, range(type, replace.REPLACE().getSymbol(), replace.OPTION().getSymbol()), recipients);
        }
        List<BehaviorObject> sources;
        if (membership) {
            sources = identities(lists.get(0), TargetType.Role);
        } else if (ctx.accessRights() != null) {
            sources = rights(ctx.accessRights());
        } else if (ctx.currentGrants().accessRights() != null) {
            sources = rights(ctx.currentGrants().accessRights());
        } else {
            AccessScopeContext range = ctx.currentGrants().accessScope();
            sources = List.of(scope(generalScopeType(range), range));
        }
        for (BehaviorObject source : sources) {
            add(SplitQueryType.GRANT, BehaviorAction.GRANT, source, recipients);
        }
        return null;
    }

    @Override
    public Void visitRevokeStmt(RevokeStmtContext ctx) {
        setType(SplitQueryType.REVOKE);
        List<AccessRoleSetContext> sets = ctx.accessRoleSet();
        AccessRoleSetContext recipientSet = sets.get(sets.size() - 1);
        List<BehaviorObject> recipients = roleSet(recipientSet, TargetType.UserOrRole);
        List<BehaviorObject> sources;
        if (ctx.accessRights() != null) {
            sources = rights(ctx.accessRights());
        } else {
            sources = roleSet(sets.get(0), TargetType.Role);
        }
        for (BehaviorObject source : sources) {
            add(SplitQueryType.REVOKE, BehaviorAction.REVOKE, source, recipients);
        }
        if (ctx.accessRights() == null) {
            excluded(sets.get(0), TargetType.Role);
        }
        excluded(recipientSet, TargetType.UserOrRole);
        return null;
    }

    @Override
    public Void visitCheckGrantStmt(CheckGrantStmtContext ctx) {
        setType(SplitQueryType.METADATA);
        read(rights(ctx.accessRights()));
        return null;
    }

    private List<BehaviorObject> rights(AccessRightsContext ctx) {
        List<BehaviorObject> result = new ArrayList<>();
        for (AccessRightGroupContext group : ctx.accessRightGroup()) {
            Set<TargetType> types = new LinkedHashSet<>();
            for (AccessPrivilegeColumnsContext privilege : group.accessPrivilegeColumns()) {
                types.add(privilegeType(privilege.accessPrivilege(), group.accessScope()));
            }
            for (TargetType type : types) {
                result.add(scope(type, group.accessScope()));
            }
        }
        return result;
    }

    private TargetType privilegeType(AccessPrivilegeContext ctx, AccessScopeContext scope) {
        if (ctx.IMPERSONATE() != null || ctx.USER() != null) {
            return TargetType.User;
        }
        if (ctx.ROLE() != null && ctx.ADMIN() == null) {
            return TargetType.Role;
        }
        if (ctx.ENGINE() != null) {
            return TargetType.TableEngine;
        }
        if (ctx.COLLECTION() != null || ctx.COLLECTIONS() != null) {
            return TargetType.NamedCollection;
        }
        if (ctx.DICTIONARY() != null || ctx.DICTIONARIES() != null || ctx.DICTGET() != null) {
            return TargetType.Dictionary;
        }
        if (ctx.VIEW() != null) {
            return TargetType.View;
        }
        if (ctx.READ() != null || ctx.WRITE() != null || ctx.SOURCES() != null) {
            return TargetType.File;
        }
        if (ctx.DATABASE() != null || ctx.DATABASES() != null) {
            return TargetType.Schema;
        }
        if (ctx.ALL() != null || ctx.MANAGEMENT() != null || ctx.ACCESS() != null) {
            return generalScopeType(scope);
        }
        if (ctx.FUNCTION() != null || ctx.QUOTA() != null || ctx.QUOTAS() != null || ctx.PROFILE() != null
            || ctx.PROFILES() != null || ctx.USERS() != null || ctx.ROLES() != null || ctx.ADMIN() != null
            || ctx.DISPLAY_SECRETS() != null || ctx.KILL() != null || ctx.LOGS() != null || ctx.SHUTDOWN() != null) {
            return TargetType.Instance;
        }
        return TargetType.Table;
    }

    private TargetType generalScopeType(AccessScopeContext scope) {
        if (scope.identifier().isEmpty() && scope.DOT() != null) {
            return TargetType.Instance;
        }
        if (scope.identifier().size() == 2 || (scope.DOT() == null && !scope.identifier().isEmpty())) {
            return TargetType.Table;
        }
        return TargetType.Schema;
    }

    private BehaviorObject scope(TargetType type, AccessScopeContext ctx) {
        List<IdentifierContext> names = ctx.identifier();
        if (type == TargetType.Instance || type == TargetType.File) {
            return objects.instanceObject(type, ctx);
        }
        if (type == TargetType.User || type == TargetType.Role || type == TargetType.NamedCollection || type == TargetType.TableEngine) {
            if (names.isEmpty()) {
                return objects.instanceObject(type, ctx);
            }
            if (!ctx.ASTERISK().isEmpty()) {
                return objects.instanceObject(type, ctx);
            }
            return objects.instanceObject(type, ctx, name(names.get(0)));
        }
        boolean databasePrefix = !names.isEmpty() && ctx.DOT() != null
            && ctx.getChild(1).getText().equals("*");
        if (type == TargetType.Schema) {
            if (!names.isEmpty() && !databasePrefix) {
                return objects.object(type, ctx, List.of(name(names.get(0))));
            }
            if (ctx.DOT() == null) {
                return objects.unnamedObject(type, ctx, UmiTypes.Schema);
            }
            return objects.unnamedObject(type, ctx, UmiTypes.Catalog);
        }
        if (ctx.DOT() == null && names.size() == 1 && ctx.ASTERISK().isEmpty()) {
            return objects.object(type, ctx, List.of(name(names.get(0))));
        }
        if (ctx.DOT() != null && names.size() == 2 && ctx.ASTERISK().isEmpty()) {
            return objects.object(type, ctx, List.of(name(names.get(0)), name(names.get(1))));
        }
        if (ctx.DOT() != null && !names.isEmpty() && !databasePrefix) {
            BehaviorObject result = objects.object(TargetType.Schema, ctx, List.of(name(names.get(0))));
            result.setObjectType(type);
            result.setObjectName(null);
            return result;
        }
        UmiTypes ancestor = UmiTypes.Schema;
        if (ctx.DOT() != null) {
            ancestor = UmiTypes.Catalog;
        }
        return objects.unnamedObject(type, ctx, ancestor);
    }

    @Override
    public Void visitCreateRowPolicyStmt(CreateRowPolicyStmtContext ctx) {
        policies(ctx.rowPolicyNames(), SplitQueryType.CREATE_POLICY, createAction(ctx.accessCreateGuard()), ctx.accessRoleSet(), null);
        return null;
    }

    @Override
    public Void visitAlterRowPolicyStmt(AlterRowPolicyStmtContext ctx) {
        AccessRenameContext rename = first(ctx, AccessRenameContext.class);
        BehaviorAction action = BehaviorAction.ALTER;
        if (rename != null) {
            action = BehaviorAction.RENAME;
        }
        AccessRoleSetContext applies = ctx.accessRoleSet();
        if (rename != null) {
            applies = null;
        }
        policies(ctx.rowPolicyNames(), SplitQueryType.ALTER_POLICY, action, applies, rename);
        if (rename != null && (ctx.rowPolicyClause().stream().anyMatch(c -> c.clusterClause() == null && c.accessStorage() == null) || ctx.accessRoleSet() != null)) {
            policies(ctx.rowPolicyNames(), SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, ctx.accessRoleSet(), null);
        }
        return null;
    }

    @Override
    public Void visitDropRowPolicyStmt(DropRowPolicyStmtContext ctx) {
        policies(ctx.rowPolicyNames(), SplitQueryType.DROP_POLICY, BehaviorAction.DROP, null, null);
        return null;
    }

    private void policies(RowPolicyNamesContext ctx, SplitQueryType type, BehaviorAction action, AccessRoleSetContext applies, AccessRenameContext rename) {
        setType(type);
        List<BehaviorObject> recipients = roleSet(applies, TargetType.UserOrRole);
        List<AccessNameContext> names = List.of();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof AccessNameListContext list) {
                names = list.accessName();
            } else if (child instanceof AccessScopeContext scope) {
                BehaviorObject table = scope(TargetType.Table, scope);
                for (AccessNameContext name : names) {
                    BehaviorObject policy = policy(name, table);
                    List<BehaviorObject> dependencies = new ArrayList<>();
                    if (action == BehaviorAction.RENAME) {
                        dependencies.add(policy(rename.accessName(), table));
                    } else if (action != BehaviorAction.DROP && action != BehaviorAction.READ) {
                        dependencies.add(table);
                        dependencies.addAll(recipients);
                    }
                    add(type, action, policy, dependencies);
                }
            }
        }
        excluded(applies, TargetType.UserOrRole);
    }

    private BehaviorObject policy(AccessNameContext name, BehaviorObject table) {
        if (table.getObjectName() != null) {
            return objects.childObject(TargetType.RowAccessPolicy, name, table, name(name));
        }
        BehaviorObject policy = objects.instanceObject(TargetType.RowAccessPolicy, name);
        policy.setObjectPath(table.getObjectPath());
        return policy;
    }

    @Override
    public Void visitCreateSettingsProfileStmt(CreateSettingsProfileStmtContext ctx) {
        List<BehaviorObject> profiles = names(ctx.accessNameList(), TargetType.Profile);
        define(ctx, profiles, SplitQueryType.SYSTEM_SETTING_WRITE, createAction(ctx.accessCreateGuard()), null);
        finishDefinition(ctx, profiles, false);
        return null;
    }

    @Override
    public Void visitAlterSettingsProfileStmt(AlterSettingsProfileStmtContext ctx) {
        List<BehaviorObject> profiles = names(ctx.accessNameList(), TargetType.Profile);
        define(ctx, profiles, SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, first(ctx, AccessRenameContext.class));
        finishDefinition(ctx, profiles, true);
        return null;
    }

    @Override
    public Void visitDropSettingsProfileStmt(DropSettingsProfileStmtContext ctx) {
        remove(names(ctx.accessNameList(), TargetType.Profile), SplitQueryType.SYSTEM_SETTING_WRITE);
        return null;
    }

    @Override
    public Void visitCreateQuotaStmt(CreateQuotaStmtContext ctx) {
        List<BehaviorObject> quotas = names(ctx.accessNameList(), TargetType.Quota);
        define(ctx, quotas, SplitQueryType.SYSTEM_SETTING_WRITE, createAction(ctx.accessCreateGuard()), null);
        finishDefinition(ctx, quotas, false);
        return null;
    }

    @Override
    public Void visitAlterQuotaStmt(AlterQuotaStmtContext ctx) {
        List<BehaviorObject> quotas = names(ctx.accessNameList(), TargetType.Quota);
        define(ctx, quotas, SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, first(ctx, AccessRenameContext.class));
        finishDefinition(ctx, quotas, true);
        return null;
    }

    @Override
    public Void visitDropQuotaStmt(DropQuotaStmtContext ctx) {
        remove(names(ctx.accessNameList(), TargetType.Quota), SplitQueryType.SYSTEM_SETTING_WRITE);
        return null;
    }

    @Override
    public Void visitShowUsersStmt(ShowUsersStmtContext ctx) {
        read(List.of(objects.instanceObject(TargetType.User, ctx.USERS().getSymbol())));
        return null;
    }

    @Override
    public Void visitShowCreateUserStmt(ShowCreateUserStmtContext ctx) {
        if (ctx.accessUserNames() != null) {
            read(identities(ctx.accessUserNames(), TargetType.User));
        } else {
            read(List.of(objects.instanceObject(TargetType.User, ctx.getStop())));
        }
        return null;
    }

    @Override
    public Void visitShowCreateRoleStmt(ShowCreateRoleStmtContext ctx) {
        if (ctx.accessUserNames() != null) {
            read(identities(ctx.accessUserNames(), TargetType.Role));
        } else {
            read(List.of(objects.instanceObject(TargetType.Role, ctx.ROLES().getSymbol())));
        }
        return null;
    }

    @Override
    public Void visitShowGrantsStmt(ShowGrantsStmtContext ctx) {
        setType(SplitQueryType.METADATA);
        if (ctx.accessRoleSet() == null) {
            read(List.of(objects.instanceObject(TargetType.User, ctx.GRANTS().getSymbol())));
        } else {
            read(roleSet(ctx.accessRoleSet(), TargetType.UserOrRole));
            excluded(ctx.accessRoleSet(), TargetType.UserOrRole);
        }
        return null;
    }

    @Override
    public Void visitShowProfilesStmt(ShowProfilesStmtContext ctx) {
        Token start = ctx.getStop();
        if (ctx.SETTINGS() != null) {
            start = ctx.SETTINGS().getSymbol();
        }
        read(List.of(range(TargetType.Profile, start, ctx.getStop())));
        return null;
    }

    @Override
    public Void visitShowCreateProfileStmt(ShowCreateProfileStmtContext ctx) {
        if (ctx.accessNameList() != null) {
            read(names(ctx.accessNameList(), TargetType.Profile));
        } else {
            Token start = ctx.getStop();
            if (ctx.SETTINGS() != null) {
                start = ctx.SETTINGS().getSymbol();
            }
            read(List.of(range(TargetType.Profile, start, ctx.getStop())));
        }
        return null;
    }

    @Override
    public Void visitShowQuotasStmt(ShowQuotasStmtContext ctx) {
        read(List.of(objects.instanceObject(TargetType.Quota, ctx.QUOTAS().getSymbol())));
        return null;
    }

    @Override
    public Void visitShowQuotaStmt(ShowQuotaStmtContext ctx) {
        Token start = ctx.QUOTA().getSymbol();
        if (ctx.CURRENT() != null) {
            start = ctx.CURRENT().getSymbol();
        }
        read(List.of(range(TargetType.Quota, start, ctx.getStop())));
        return null;
    }

    @Override
    public Void visitShowCreateQuotaStmt(ShowCreateQuotaStmtContext ctx) {
        if (ctx.accessNameList() != null) {
            read(names(ctx.accessNameList(), TargetType.Quota));
        } else {
            read(List.of(objects.instanceObject(TargetType.Quota, ctx.getStop())));
        }
        return null;
    }

    @Override
    public Void visitShowPoliciesStmt(ShowPoliciesStmtContext ctx) {
        Token start = ctx.POLICIES().getSymbol();
        if (ctx.ROW() != null) {
            start = ctx.ROW().getSymbol();
        }
        readPolicyRange(ctx.accessScope(), ctx.accessName(), start, ctx.POLICIES().getSymbol());
        return null;
    }

    @Override
    public Void visitShowCreatePolicyStmt(ShowCreatePolicyStmtContext ctx) {
        if (ctx.rowPolicyNames() != null) {
            policies(ctx.rowPolicyNames(), SplitQueryType.METADATA, BehaviorAction.READ, null, null);
        } else {
            Token end = ctx.getStop();
            if (ctx.POLICIES() != null) {
                end = ctx.POLICIES().getSymbol();
            } else if (ctx.POLICY() != null) {
                end = ctx.POLICY().getSymbol();
            }
            Token start = end;
            if (ctx.ROW() != null) {
                start = ctx.ROW().getSymbol();
            }
            readPolicyRange(ctx.accessScope(), ctx.accessName(), start, end);
        }
        return null;
    }

    private void readPolicyRange(AccessScopeContext scope, AccessNameContext name, Token start, Token stop) {
        if (name != null) {
            start = name.getStart();
            stop = name.getStop();
        }
        BehaviorObject policy = range(TargetType.RowAccessPolicy, start, stop);
        BehaviorObject parent = objects.unnamedObject(TargetType.RowAccessPolicy, start, UmiTypes.Catalog);
        if (scope != null) {
            parent = scope(TargetType.Table, scope);
        }
        policy.setObjectPath(parent.getObjectPath());
        read(List.of(policy));
    }

    @Override
    public Void visitShowAccessStmt(ShowAccessStmtContext ctx) {
        List<BehaviorObject> entities = new ArrayList<>();
        for (TargetType type : List.of(TargetType.User, TargetType.Role, TargetType.RowAccessPolicy, TargetType.Profile, TargetType.Quota)) {
            if (type == TargetType.RowAccessPolicy) {
                entities.add(objects.unnamedObject(type, ctx.ACCESS().getSymbol(), UmiTypes.Catalog));
            } else {
                entities.add(objects.instanceObject(type, ctx.ACCESS().getSymbol()));
            }
        }
        read(entities);
        return null;
    }

    private List<BehaviorObject> identities(AccessUserNamesContext ctx, TargetType type) {
        return identities(ctx, type, true);
    }

    private List<BehaviorObject> identities(AccessUserNamesContext ctx, TargetType type, boolean currentUserSelector) {
        List<BehaviorObject> result = new ArrayList<>();
        for (AccessUserNameContext user : ctx.accessUserName()) {
            if (currentUserSelector && type != TargetType.Role && user.getText().equalsIgnoreCase("CURRENT_USER")) {
                result.add(objects.instanceObject(TargetType.User, user));
            } else {
                String value = name(user.accessName(0));
                if (user.AT() != null) {
                    String host = name(user.accessName(1)).trim();
                    if (!host.isEmpty() && !host.equals("%")) {
                        value += "@" + host;
                    }
                }
                result.add(objects.instanceObject(type, user, value));
            }
        }
        return result;
    }

    private List<BehaviorObject> roleSet(AccessRoleSetContext ctx, TargetType type) {
        if (ctx == null || ctx.NONE() != null) {
            return List.of();
        }
        if (ctx.ALL() != null) {
            return List.of(objects.instanceObject(type, ctx.ALL().getSymbol()));
        }
        return identities(ctx.members, type);
    }

    private void excluded(AccessRoleSetContext ctx, TargetType type) {
        if (ctx != null && ctx.excluded != null) {
            read(identities(ctx.excluded, type));
        }
    }

    private List<BehaviorObject> names(AccessNameListContext ctx, TargetType type) {
        List<BehaviorObject> result = new ArrayList<>();
        for (AccessNameContext name : ctx.accessName()) {
            result.add(named(type, name));
        }
        return result;
    }

    private BehaviorObject named(TargetType type, AccessNameContext name) {
        return objects.instanceObject(type, name, name(name));
    }

    private BehaviorObject range(TargetType type, Token start, Token stop) {
        BehaviorObject object = objects.instanceObject(type, start);
        BehaviorObject end = objects.instanceObject(type, stop);
        object.setEndLine(end.getEndLine());
        object.setEndColumn(end.getEndColumn());
        return object;
    }

    private void read(List<BehaviorObject> objects) {
        for (BehaviorObject object : objects) {
            add(SplitQueryType.METADATA, BehaviorAction.READ, object);
        }
    }

    private void remove(List<BehaviorObject> objects, SplitQueryType type) {
        for (BehaviorObject object : objects) {
            add(type, BehaviorAction.DROP, object);
        }
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> nodes = descendants(tree, type);
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.get(0);
    }
}
