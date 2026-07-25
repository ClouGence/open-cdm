/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

final class MyBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final MyDslProvider           provider;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final MySqlResourceRegistry   resources;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    MyBehaviorParserVisitor(Parser parser, MyDslProvider provider, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, MySqlResourceRegistry resources){
        this.parser = parser;
        this.provider = provider;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
        this.resources = resources;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        ParserRuleContext context = (ParserRuleContext) tree;
        MyBehaviorObjectReferenceVisitor visitor = new MyBehaviorObjectReferenceVisitor(parser,
            levels,
            baseLine,
            baseColumn,
            provider.version(),
            provider.exactVersion(),
            resources);
        visitor.prepareStatement(context);
        visitor.scan(context);
        visitor.scanOptimizerHints(context);

        String sql = parser.getTokenStream().getText(context.getStart(), context.getStop());
        SecQueryType statementType = MyBehaviorStatementTypeResolver.resolve(sql, visitor.references());
        boolean libraryLifecycle = statementType == SecQueryType.CREATE_LIBRARY
            || statementType == SecQueryType.ALTER_LIBRARY
            || statementType == SecQueryType.DROP_LIBRARY
            || statementType == SecQueryType.COMMENT_LIBRARY;
        if (libraryLifecycle) {
            visitor.references().removeIf(reference -> reference.targetType() != TargetType.Library);
        }
        if (visitor.references().isEmpty()
            || visitor.references().stream().allMatch(reference -> reference.targetType() == TargetType.Function && reference.sqlType() == SecQueryType.CALL_PROG_OBJ)) {
            TargetType fallback = fallbackType(statementType);
            if (fallback != null) {
                int fallbackIndex = visitor.references().size();
                visitor.addUnnamedFallback(statementType, fallback, context);
                visitor.references().add(0, visitor.references().remove(fallbackIndex));
            }
        }

        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        behavior.setRelations(new MyBehaviorRelationAssembler(sql, visitor.references(), levels).assemble());
        behaviors.add(behavior);
        return null;
    }

    private TargetType fallbackType(SecQueryType type) {
        return switch (type) {
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, SESSION_VARIABLE_RW -> TargetType.ConfigKey;
            case CREATE_REPLICATION, ALTER_REPLICATION, DROP_REPLICATION, ADMIN_REPLICATION -> TargetType.Replication;
            case CREATE_LOG, ALTER_LOG, DROP_LOG, LOG_READ, ADMIN_LOG, MAINTAIN_LOG -> TargetType.Log;
            case CREATE_LIBRARY, ALTER_LIBRARY, DROP_LIBRARY, COMMENT_LIBRARY -> TargetType.Library;
            case CREATE_USER, ALTER_USER, DROP_USER, RENAME_USER, SWITCH_USER -> TargetType.User;
            case CREATE_ROLE, ALTER_ROLE, DROP_ROLE, RENAME_ROLE, SWITCH_ROLE -> TargetType.Role;
            case DATA_IMPORT, DATA_EXPORT -> TargetType.File;
            case ADMIN_TABLE -> TargetType.Table;
            case ADMIN, ADMIN_PERFORMANCE, PERFORMANCE, METADATA, SESSION_LOCK -> TargetType.Instance;
            default -> null;
        };
    }
}
