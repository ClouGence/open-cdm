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
package com.clougence.sql.mysql.analysis.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySplitVisitor;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;
import com.clougence.sql.mysql.parser.antlr.MySqlParser.CommentInsertValueContext;
import com.clougence.sql.mysql.parser.antlr.MySqlParser.InsertStatementContext;
import com.clougence.sql.mysql.parser.antlr.MySqlParser.ReplaceStatementContext;

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
        ParseTree command = context;
        while (command.getChildCount() == 1 && command.getChild(0) instanceof ParserRuleContext) {
            command = command.getChild(0);
        }
        Map<UmiTypes, Object> statementLevels = resolveStatementLevels(command);
        MyBehaviorObjectReferenceVisitor visitor = new MyBehaviorObjectReferenceVisitor(parser,
            statementLevels,
            baseLine,
            baseColumn,
            provider.version(),
            provider.exactVersion(),
            resources);
        if (command instanceof MySqlParser.MariaSetStatementContext scoped) {
            behaviors.add(analyzeScopedStatement(context, scoped, visitor, statementLevels));
            return null;
        }
        visitor.prepareStatement(context);
        visitor.scan(context);
        visitor.scanOptimizerHints(context);

        String sql = MyBehaviorText.statementText(parser.getTokenStream(), context);
        SplitQueryType statementType = resolveStatementType(context, command, sql, visitor);
        normalizeReferences(context, command, statementType, visitor);

        List<BehaviorRelation> relations = new MyBehaviorRelationAssembler(sql, statementType, visitor.references(), statementLevels, isUnsafeReset(command)).assemble();
        applyCommandActions(command, relations);
        applyInsertRows(context, relations);

        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        behavior.setRelations(relations);
        behaviors.add(behavior);
        return null;
    }

    private Map<UmiTypes, Object> resolveStatementLevels(ParseTree command) {
        if (!(command instanceof MySqlParser.FullDescribeStatementContext describe) || describe.uid() == null) {
            return levels;
        }
        Map<UmiTypes, Object> statementLevels = new HashMap<>();
        if (levels != null) {
            statementLevels.putAll(levels);
        }
        String schema = describe.uid().getText();
        if (schema.startsWith("`") && schema.endsWith("`")) {
            schema = schema.substring(1, schema.length() - 1).replace("``", "`");
        }
        statementLevels.put(UmiTypes.Schema, schema);
        return statementLevels;
    }

    private StatementBehavior analyzeScopedStatement(ParserRuleContext context, MySqlParser.MariaSetStatementContext scoped, MyBehaviorObjectReferenceVisitor visitor,
                                                     Map<UmiTypes, Object> statementLevels) {
        // Preserve the child's actions, including commands with only unnamed targets.
        for (var assignment : scoped.mariaStatementAssignment()) {
            visitor.scan(assignment);
        }
        MyBehaviorParserVisitor childVisitor = new MyBehaviorParserVisitor(parser, provider, statementLevels, baseLine, baseColumn, resources);
        childVisitor.visit(scoped.sqlStatement());
        StatementBehavior child = childVisitor.behaviors().get(0);
        String scopedSql = MyBehaviorText.statementText(parser.getTokenStream(), context);
        List<BehaviorRelation> relations = new MyBehaviorRelationAssembler(scopedSql, SplitQueryType.SESSION_SETTING_WRITE, visitor.references(), statementLevels, false)
            .assemble();
        relations.addAll(child.getRelations());
        child.setRelations(relations);
        child.setStatementType(SplitQueryType.SESSION_SETTING_WRITE);
        return child;
    }

    private SplitQueryType resolveStatementType(ParserRuleContext context, ParseTree command, String sql, MyBehaviorObjectReferenceVisitor visitor) {
        SplitQueryType statementType = MyBehaviorStatementTypeResolver.resolve(sql, visitor.references());
        boolean optionFallbackRequired = command instanceof MySqlParser.ResetOptionsContext || hasMariaFlushOptions(command);
        if (requiresSplitType(command) || optionFallbackRequired) {
            var statementTypes = new MySplitVisitor(provider.version()).collectTypes(command);
            statementType = statementTypes.iterator().next();
            if (optionFallbackRequired) {
                // Each option can affect a different kind of unnamed resource.
                for (SplitQueryType type : statementTypes) {
                    if (type == SplitQueryType.UNSAFE) {
                        continue;
                    }
                    TargetType target = fallbackType(type);
                    if (target != null && visitor.references().stream().noneMatch(reference -> reference.targetType() == target)) {
                        visitor.addUnnamedFallback(type, target, context);
                    }
                }
            }
        }
        if (command instanceof MySqlParser.MariaAnalyzeContext analyze) {
            // ANALYZE executes its child, including UPDATE/DELETE; it is not EXPLAIN.
            for (int i = 0; i < analyze.getChildCount(); i++) {
                if (analyze.getChild(i) instanceof ParserRuleContext child && !(child instanceof MySqlParser.MariaKeywordContext)) {
                    statementType = MyBehaviorStatementTypeResolver.resolve(MyBehaviorText.statementText(parser.getTokenStream(), child), visitor.references());
                    break;
                }
            }
        }
        return statementType;
    }

    private static boolean requiresSplitType(ParseTree command) {
        if (command instanceof MySqlParser.FullDescribeStatementContext describe) {
            return describe.analyze == null || describe.describeObjectClause() instanceof MySqlParser.DescribeConnectionContext;
        }
        return command instanceof MySqlParser.SimpleDescribeStatementContext || command instanceof MySqlParser.SetVariableContext || command instanceof MySqlParser.MariaShowContext
               || command instanceof MySqlParser.MariaBackupContext || command instanceof MySqlParser.MariaAllReplicasContext;
    }

    private static boolean hasMariaFlushOptions(ParseTree command) {
        return command instanceof MySqlParser.FlushStatementContext flush && flush.flushOption().stream().anyMatch(option -> option.mariaFlushOption() != null);
    }

    private static boolean isUnsafeReset(ParseTree command) {
        if (command instanceof MySqlParser.ResetOptionsContext reset) {
            return reset.resetOption().stream().anyMatch(option -> {
                boolean resetsMasterLogs = option.MASTER() != null || option.BINARY() != null && option.LOGS() != null;
                boolean removesReplication = option.ALL() != null && (option.SLAVE() != null || option.REPLICA() != null);
                return resetsMasterLogs || removesReplication;
            });
        }
        if (command instanceof MySqlParser.ResetMasterContext || command instanceof MySqlParser.ResetBinaryLogsAndGtidsContext) {
            return true;
        }
        if (command instanceof MySqlParser.ResetSlaveContext reset) {
            return reset.ALL() != null;
        }
        if (command instanceof MySqlParser.ResetReplicaContext reset) {
            return reset.ALL() != null;
        }
        return false;
    }

    private void normalizeReferences(ParserRuleContext context, ParseTree command, SplitQueryType statementType, MyBehaviorObjectReferenceVisitor visitor) {
        boolean libraryLifecycle = switch (statementType) {
            case CREATE_LIBRARY, ALTER_LIBRARY, DROP_LIBRARY, COMMENT_LIBRARY -> true;
            default -> false;
        };
        if (libraryLifecycle) {
            visitor.references().removeIf(reference -> reference.targetType() != TargetType.Library);
        }
        if (command instanceof MySqlParser.FullDescribeStatementContext && statementType == SplitQueryType.PERFORMANCE) {
            visitor.references().removeIf(reference -> reference.targetType() == TargetType.File);
        }
        boolean missingKillTarget = command instanceof MySqlParser.KillStatementContext
                                    && visitor.references().stream().noneMatch(reference -> reference.targetType() == TargetType.Instance);
        boolean onlyFunctionCalls = statementType != SplitQueryType.SELECT && statementType != SplitQueryType.BLOCK
                                    && visitor.references()
                                        .stream()
                                        .allMatch(reference -> reference.targetType() == TargetType.Function && reference.sqlType() == SplitQueryType.CALL_PROG_OBJ);
        if (missingKillTarget || visitor.references().isEmpty() || onlyFunctionCalls) {
            TargetType fallback = fallbackType(statementType);
            if (fallback != null) {
                int fallbackIndex = visitor.references().size();
                visitor.addUnnamedFallback(statementType, fallback, context);
                visitor.references().add(0, visitor.references().remove(fallbackIndex));
            }
        }
    }

    private static void applyCommandActions(ParseTree command, List<BehaviorRelation> relations) {
        if (command instanceof MySqlParser.MariaBackupContext backup) {
            BehaviorAction action = BehaviorAction.LOCK;
            if (backup.UNLOCK() != null || backup.END() != null) {
                action = BehaviorAction.UNLOCK;
            }
            for (BehaviorRelation relation : relations) {
                relation.setAction(action);
            }
        } else if (command instanceof MySqlParser.MariaAllReplicasContext replicas) {
            BehaviorAction action = BehaviorAction.STOP;
            if (replicas.START() != null) {
                action = BehaviorAction.START;
            }
            for (BehaviorRelation relation : relations) {
                relation.setAction(action);
            }
        } else if (hasMariaFlushOptions(command)) {
            for (BehaviorRelation relation : relations) {
                if (relation.getSubject().getObjectType() != TargetType.Function) {
                    relation.setAction(BehaviorAction.FLUSH);
                }
            }
        }
    }

    private static void applyInsertRows(ParserRuleContext context, List<BehaviorRelation> relations) {
        Long insertRows = insertRows(context);
        if (insertRows != null) {
            relations.stream()
                .filter(relation -> relation.getAction() == BehaviorAction.INSERT || relation.getAction() == BehaviorAction.MERGE || relation.getAction() == BehaviorAction.REPLACE)
                .findFirst()
                .ifPresent(relation -> relation.setInsertRows(insertRows));
        }
    }

    private static Long insertRows(ParseTree tree) {
        if (tree instanceof InsertStatementContext insert) {
            if (insert.setFirst != null) {
                return 1L;
            }
            if (insert.insertStatementValue() instanceof CommentInsertValueContext values) {
                return (long) values.valuesRow().size();
            }
            return null;
        }
        if (tree instanceof ReplaceStatementContext replace) {
            if (replace.setFirst != null) {
                return 1L;
            }
            if (replace.replaceStatementValue() != null && replace.replaceStatementValue().insertFormat != null) {
                return (long) replace.replaceStatementValue().valuesRow().size();
            }
            return null;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            Long rows = insertRows(tree.getChild(i));
            if (rows != null) {
                return rows;
            }
        }
        return null;
    }

    private TargetType fallbackType(SplitQueryType type) {
        return switch (type) {
            case SELECT -> TargetType.Query;
            case TRANSACTION, BLOCK -> TargetType.Unknown;
            case PROGRAM_CONTROL -> TargetType.ProgramObject;
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, SESSION_VARIABLE_RW -> TargetType.ConfigKey;
            case CREATE_REPLICATION, ALTER_REPLICATION, DROP_REPLICATION, ADMIN_REPLICATION -> TargetType.Replication;
            case CREATE_LOG, ALTER_LOG, DROP_LOG, LOG_READ, ADMIN_LOG, MAINTAIN_LOG -> TargetType.Log;
            case CREATE_LIBRARY, ALTER_LIBRARY, DROP_LIBRARY, COMMENT_LIBRARY -> TargetType.Library;
            case CREATE_USER, ALTER_USER, DROP_USER, RENAME_USER, SWITCH_USER -> TargetType.User;
            case CREATE_ROLE, ALTER_ROLE, DROP_ROLE, RENAME_ROLE, SWITCH_ROLE -> TargetType.Role;
            case DATA_IMPORT, DATA_EXPORT -> TargetType.File;
            case ADMIN_TABLE -> TargetType.Table;
            case ADMIN, ADMIN_PERFORMANCE, PERFORMANCE, METADATA, SESSION_LOCK, UNSAFE -> TargetType.Instance;
            default -> null;
        };
    }
}
