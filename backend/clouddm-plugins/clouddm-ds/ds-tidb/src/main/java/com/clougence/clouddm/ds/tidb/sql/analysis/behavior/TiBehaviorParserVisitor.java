/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBResourceRegistry;
import com.clougence.utils.StringUtils;

final class TiBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final TiDBDslProvider         provider;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final TiDBResourceRegistry   resources;
    private final List<StatementBehavior> behaviors = new ArrayList<>();
    private ParserRuleContext             statementContext;
    private String                        statementSql;

    TiBehaviorParserVisitor(Parser parser, TiDBDslProvider provider, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, TiDBResourceRegistry resources){
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
        this.statementContext = context;
        TiBehaviorObjectReferenceVisitor visitor = new TiBehaviorObjectReferenceVisitor(parser,
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
        this.statementSql = sql;
        SplitQueryType statementType = TiBehaviorStatementTypeResolver.resolve(sql, visitor.references());
        boolean libraryLifecycle = statementType == SplitQueryType.CREATE_LIBRARY || statementType == SplitQueryType.ALTER_LIBRARY || statementType == SplitQueryType.DROP_LIBRARY
                                   || statementType == SplitQueryType.COMMENT_LIBRARY;
        if (libraryLifecycle) {
            visitor.references().removeIf(reference -> reference.targetType() != TargetType.Library);
        }
        if (visitor.references().isEmpty()
            || hasLegacyFunctionOnlyFallback(statementType)
               && visitor.references().stream().allMatch(reference -> reference.targetType() == TargetType.Function && reference.sqlType() == SplitQueryType.CALL_PROG_OBJ)) {
            TargetType fallback = fallbackType(statementType);
            if (fallback != null) {
                int fallbackIndex = visitor.references().size();
                visitor.addUnnamedFallback(statementType, fallback, context);
                visitor.references().add(0, visitor.references().remove(fallbackIndex));
            }
        }

        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        behavior.setRelations(new TiBehaviorRelationAssembler(sql, statementType, visitor.references(), levels).assemble());
        addTiDBAdministrativeRelations(sql, statementType, behavior);
        addBindingBodyRelations(sql, statementType, behavior);
        addLooseShowWhereRelations(sql, statementType, behavior);
        addTiDBEmbeddedStatementRelations(sql, statementType, behavior);
        addTiDBLooseObjectRelations(sql, behavior);
        removeSupersededUnknown(statementType, behavior);
        behaviors.add(behavior);
        return null;
    }

    private void addTiDBAdministrativeRelations(String sql, SplitQueryType statementType, StatementBehavior behavior) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (statementType == SplitQueryType.CREATE_POLICY || statementType == SplitQueryType.ALTER_POLICY || statementType == SplitQueryType.DROP_POLICY) {
            BehaviorAction action = statementType == SplitQueryType.CREATE_POLICY ? BehaviorAction.CREATE : statementType == SplitQueryType.ALTER_POLICY ? BehaviorAction.ALTER : BehaviorAction.DROP;
            String anchor = normalized.startsWith("ALTER RANGE ") ? "RANGE" : "POLICY";
            addFirstIdentifierAfter(sql, behavior, anchor, TargetType.Policy, action);
            if (normalized.startsWith("ALTER RANGE ")) {
                addLastIdentifierAfter(sql, behavior, "POLICY", TargetType.Policy, BehaviorAction.READ, "DEFAULT");
            }
        } else if (normalized.startsWith("BACKUP ") || normalized.startsWith("RESTORE ")) {
            boolean backup = normalized.startsWith("BACKUP ");
            BehaviorAction dataAction = backup ? BehaviorAction.EXPORT : BehaviorAction.RESTORE;
            TargetType dataType = normalized.startsWith((backup ? "BACKUP" : "RESTORE") + " TABLE") ? TargetType.Table : TargetType.Schema;
            String anchor = dataType == TargetType.Table ? "TABLE" : normalized.contains(" SCHEMA ") ? "SCHEMA" : "DATABASE";
            removeUnnamedFallback(behavior, TargetType.File);
            if (!backup) {
                behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == dataType);
            }
            if (!backup || behavior.getRelations().stream().noneMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == dataType)) {
                addIdentifierListAfter(sql, behavior, anchor, backup ? "TO" : "FROM", dataType, dataAction);
            }
            int objectStart = TiBehaviorText.skipWhitespace(sql, TiBehaviorText.findWord(sql, 0, anchor) + anchor.length());
            if (objectStart >= anchor.length() && objectStart < sql.length() && sql.charAt(objectStart) == '*') {
                addUnnamedRelation(sql, behavior, dataType, dataAction);
            }
            addQuotedPathAfter(sql, behavior, backup ? "TO" : "FROM", TargetType.File, backup ? BehaviorAction.EXPORT : BehaviorAction.READ);
            linkBackupOrRestore(behavior, dataType, backup);
        } else if (normalized.startsWith("FLASHBACK ")) {
            if (normalized.startsWith("FLASHBACK CLUSTER")) {
                addUnnamedRelation(sql, behavior, TargetType.Instance, BehaviorAction.RESTORE);
            } else {
                TargetType type = normalized.startsWith("FLASHBACK TABLE") ? TargetType.Table : TargetType.Schema;
                String anchor = type == TargetType.Table ? "TABLE" : normalized.contains(" SCHEMA ") ? "SCHEMA" : "DATABASE";
                behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == type);
                addIdentifierListAfter(sql, behavior, anchor, "TO", type, BehaviorAction.RESTORE);
            }
        } else if (normalized.startsWith("INDEX ADVISE ")) {
            behavior.getRelations().clear();
            if (statementType != SplitQueryType.UNSAFE) {
                addUnnamedRelation(sql, behavior, TargetType.Index, BehaviorAction.ANALYZE);
            }
            addQuotedPathAfter(sql, behavior, "INFILE", TargetType.File, BehaviorAction.UNSAFE);
        } else if (normalized.startsWith("RECOMMEND INDEX ")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Instance);
            addUnnamedRelation(sql, behavior, TargetType.Index, normalized.startsWith("RECOMMEND INDEX SHOW") ? BehaviorAction.READ : BehaviorAction.ANALYZE);
            if (normalized.startsWith("RECOMMEND INDEX RUN FOR")) {
                addQuotedPathAfter(sql, behavior, "FOR", TargetType.Query, BehaviorAction.UNSAFE);
            }
        } else if (normalized.startsWith("CALIBRATE RESOURCE")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Instance);
            addUnnamedRelation(sql, behavior, TargetType.ResourceGroup, BehaviorAction.ANALYZE);
        } else if (normalized.startsWith("ADMIN SHOW DDL")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
            addUnnamedRelation(sql, behavior, TargetType.Job, BehaviorAction.READ);
        } else if (normalized.startsWith("ADMIN SHOW TELEMETRY")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
            addUnnamedRelation(sql, behavior, TargetType.Instance, BehaviorAction.READ);
        } else if (normalized.startsWith("RECOVER TABLE BY JOB")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
            addUnnamedRelation(sql, behavior, TargetType.Job, BehaviorAction.RECOVER);
        } else if (normalized.startsWith("ADMIN SHOW SLOW")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
            addUnnamedRelation(sql, behavior, TargetType.Query, BehaviorAction.READ);
        } else if (normalized.startsWith("CREATE IMPORT ") || normalized.startsWith("ALTER IMPORT ") || normalized.startsWith("DROP IMPORT ")
                   || normalized.startsWith("RESUME IMPORT ") || normalized.startsWith("STOP IMPORT ") || normalized.startsWith("PURGE IMPORT ")) {
            BehaviorAction action = normalized.startsWith("CREATE ") ? BehaviorAction.CREATE : normalized
                .startsWith("ALTER ") ? BehaviorAction.ALTER : normalized.startsWith("DROP ") ? BehaviorAction.DROP : normalized
                    .startsWith("RESUME ") ? BehaviorAction.START : normalized.startsWith("STOP ") ? BehaviorAction.STOP : BehaviorAction.PURGE;
            addFirstIdentifierAfter(sql, behavior, "IMPORT", TargetType.Job, action);
            if (behavior.getRelations().stream().noneMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Job)) {
                addUnnamedRelation(sql, behavior, TargetType.Job, action);
            }
            addQuotedPathAfter(sql, behavior, "FROM", TargetType.File, BehaviorAction.READ);
        } else if (normalized.startsWith("CANCEL IMPORT JOB") || normalized.startsWith("CANCEL BR JOB") || normalized.startsWith("CANCEL TRAFFIC JOBS")) {
            addUnnamedRelation(sql, behavior, TargetType.Job, BehaviorAction.TERMINATE);
        } else if (normalized.startsWith("SHOW IMPORT") || normalized.startsWith("SHOW CREATE IMPORT") || normalized.startsWith("SHOW TRAFFIC JOBS")) {
            behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Instance);
            addUnnamedRelation(sql, behavior, TargetType.Job, BehaviorAction.READ);
        } else if (normalized.startsWith("ADMIN ALTER DDL JOBS") || normalized.startsWith("ADMIN PAUSE DDL JOBS") || normalized.startsWith("ADMIN RESUME DDL JOBS")) {
            BehaviorAction action = normalized.startsWith("ADMIN ALTER") ? BehaviorAction.ALTER : normalized.startsWith("ADMIN PAUSE") ? BehaviorAction.STOP : BehaviorAction.START;
            addUnnamedRelation(sql, behavior, TargetType.Job, action);
        } else if (normalized.startsWith("TRAFFIC CAPTURE ") || normalized.startsWith("TRAFFIC REPLAY ")) {
            boolean capture = normalized.startsWith("TRAFFIC CAPTURE ");
            addUnnamedRelation(sql, behavior, TargetType.Job, capture ? BehaviorAction.CREATE : BehaviorAction.UNSAFE);
            addQuotedPathAfter(sql, behavior, capture ? "TO" : "FROM", TargetType.File, capture ? BehaviorAction.EXPORT : BehaviorAction.UNSAFE);
        } else if (normalized.startsWith("CHANGE PUMP ") || normalized.startsWith("CHANGE DRAINER ") || normalized.startsWith("ADMIN SET BDR ROLE")
                   || normalized.startsWith("ADMIN UNSET BDR ROLE") || normalized.startsWith("ADMIN SHOW BDR ROLE")) {
            behavior.getRelations()
                .removeIf(relation -> relation.getSubject() != null
                                      && (relation.getSubject().getObjectType() == TargetType.Table || relation.getSubject().getObjectType() == TargetType.Replication));
            BehaviorAction action = normalized
                .startsWith("ADMIN SHOW") ? BehaviorAction.READ : normalized.startsWith("ADMIN UNSET") ? BehaviorAction.RESET : BehaviorAction.CONFIGURE;
            addUnnamedRelation(sql, behavior, TargetType.Replication, action);
        } else if (normalized.startsWith("SET CONFIG ")) {
            addFirstIdentifierAfter(sql, behavior, "CONFIG", TargetType.ConfigKey, BehaviorAction.CONFIGURE);
        } else if (normalized.startsWith("STOP BACKUP LOGS") || normalized.startsWith("PAUSE BACKUP LOGS") || normalized.startsWith("RESUME BACKUP LOGS")
                   || normalized.startsWith("PURGE BACKUP LOGS")) {
            BehaviorAction action = normalized.startsWith("STOP")
                                    || normalized.startsWith("PAUSE") ? BehaviorAction.STOP : normalized.startsWith("RESUME") ? BehaviorAction.START : BehaviorAction.PURGE;
            addUnnamedRelation(sql, behavior, TargetType.Log, action);
            addQuotedPathAfter(sql, behavior, "FROM", TargetType.File, BehaviorAction.READ);
        } else if (normalized.startsWith("CREATE GLOBAL BINDING") || normalized.startsWith("CREATE SESSION BINDING") || normalized.startsWith("CREATE BINDING")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.CREATE);
        } else if (normalized.startsWith("DROP GLOBAL BINDING") || normalized.startsWith("DROP SESSION BINDING") || normalized.startsWith("DROP BINDING")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.DROP);
        } else if (normalized.startsWith("SET BINDING")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.ALTER);
        } else if (normalized.startsWith("PLAN REPLAYER")) {
            addUnnamedRelation(sql, behavior, TargetType.Query, BehaviorAction.ANALYZE);
            addQuotedPathAfter(sql, behavior, "EXPLAIN", TargetType.File, BehaviorAction.EXPORT);
        } else if (normalized.startsWith("CANCEL DISTRIBUTION JOB") || normalized.startsWith("ADMIN CANCEL DDL JOB")) {
            addUnnamedRelation(sql, behavior, TargetType.Job, BehaviorAction.TERMINATE);
        } else if (isLoadDataJobCommand(normalized)) {
            BehaviorAction action = normalized.startsWith("DROP") ? BehaviorAction.DROP : normalized.startsWith("PAUSE") ? BehaviorAction.STOP : BehaviorAction.START;
            addUnnamedRelation(sql, behavior, TargetType.Job, action);
        } else if (normalized.startsWith("ADMIN CREATE WORKLOAD SNAPSHOT")) {
            addUnnamedRelation(sql, behavior, TargetType.Snapshot, BehaviorAction.CREATE);
        } else if (normalized.startsWith("ADMIN CAPTURE BINDINGS")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.CREATE);
        } else if (normalized.startsWith("ADMIN EVOLVE BINDINGS")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.ALTER);
        } else if (normalized.startsWith("ADMIN RELOAD BINDINGS") || normalized.startsWith("ADMIN FLUSH BINDINGS")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.REFRESH);
        } else if (normalized.startsWith("ADMIN FLUSH") && normalized.contains("PLAN_CACHE")) {
            addUnnamedRelation(sql, behavior, TargetType.Query, BehaviorAction.FLUSH);
        } else if (normalized.startsWith("ADMIN RELOAD STATISTICS") || normalized.startsWith("ADMIN RELOAD STATS_EXTENDED")) {
            addUnnamedRelation(sql, behavior, TargetType.Statistics, BehaviorAction.REFRESH);
        } else if (normalized.startsWith("ADMIN PLUGINS ENABLE") || normalized.startsWith("ADMIN PLUGINS DISABLE")) {
            addIdentifierListAfter(sql, behavior, normalized.startsWith("ADMIN PLUGINS ENABLE") ? "ENABLE" : "DISABLE", null, TargetType.Plugin, normalized
                .startsWith("ADMIN PLUGINS ENABLE") ? BehaviorAction.START : BehaviorAction.STOP);
        } else if (normalized.startsWith("ADMIN RELOAD EXPR_PUSHDOWN_BLACKLIST") || normalized.startsWith("ADMIN RELOAD OPT_RULE_BLACKLIST")) {
            addUnnamedRelation(sql, behavior, TargetType.Policy, BehaviorAction.REFRESH);
        } else if (normalized.startsWith("ADMIN RESET TELEMETRY_ID")) {
            addUnnamedRelation(sql, behavior, TargetType.ConfigKey, BehaviorAction.RESET);
        } else if (normalized.startsWith("SET SESSION_STATES")) {
            addUnnamedRelation(sql, behavior, TargetType.Session, BehaviorAction.CONFIGURE);
        } else if (normalized.startsWith("SET RESOURCE GROUP")) {
            addFirstIdentifierAfter(sql, behavior, "GROUP", TargetType.ResourceGroup, BehaviorAction.SWITCH);
            if (behavior.getRelations().stream().noneMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.ResourceGroup)) {
                addUnnamedRelation(sql, behavior, TargetType.ResourceGroup, BehaviorAction.SWITCH);
            }
        } else if (normalized.startsWith("LOAD STATS")) {
            addQuotedPathAfter(sql, behavior, "STATS", TargetType.File, BehaviorAction.IMPORT);
        }
    }

    private void addUnnamedRelation(String sql, StatementBehavior behavior, TargetType type, BehaviorAction action) {
        removeUnknownFallback(behavior);
        if (action != BehaviorAction.UNKNOWN) {
            behavior.getRelations()
                .removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == type && relation.getSubject().getObjectName() == null
                                      && relation.getAction() == BehaviorAction.UNKNOWN);
        }
        RdbBehaviorObjectFactory factory = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        BehaviorObject object = type == TargetType.Instance || type == TargetType.File || type == TargetType.Job || type == TargetType.Replication
                                || type == TargetType.ResourceGroup || type == TargetType.Log ? factory.instanceObject(type, statementContext) : factory
                                    .unnamedObject(type, statementContext, type == TargetType.Schema ? UmiTypes.Catalog : UmiTypes.Schema);
        addRelation(behavior, object, action);
    }

    private void addFirstIdentifierAfter(String sql, StatementBehavior behavior, String anchor, TargetType type, BehaviorAction action) {
        addIdentifierListAfter(sql, behavior, anchor, null, type, action, 1);
    }

    private void addLastIdentifierAfter(String sql, StatementBehavior behavior, String anchor, TargetType type, BehaviorAction action, String ignored) {
        int start = TiBehaviorText.findWord(sql, 0, anchor);
        if (start < 0)
            return;
        TiBehaviorTextSpan identifier = TiBehaviorText.nextIdentifier(sql, TiBehaviorText.skipWhitespace(sql, start + anchor.length()), sql.length(), false);
        if (identifier != null && !identifier.text(sql).equalsIgnoreCase(ignored)) {
            addNamedRelation(sql, behavior, type, action, identifier.text(sql), identifier.start());
        }
    }

    private void addIdentifierListAfter(String sql, StatementBehavior behavior, String anchor, String before, TargetType type, BehaviorAction action) {
        addIdentifierListAfter(sql, behavior, anchor, before, type, action, Integer.MAX_VALUE);
    }

    private void addIdentifierListAfter(String sql, StatementBehavior behavior, String anchor, String before, TargetType type, BehaviorAction action, int limit) {
        int anchorStart = TiBehaviorText.findWord(sql, 0, anchor);
        if (anchorStart < 0)
            return;
        int start = TiBehaviorText.skipWhitespace(sql, anchorStart + anchor.length());
        int end = before == null ? sql.length() : TiBehaviorText.findWord(sql, start, before);
        if (end < 0)
            end = sql.length();
        int count = 0;
        while (start < end && count < limit) {
            TiBehaviorTextSpan identifier = TiBehaviorText.nextIdentifier(sql, start, end, false);
            if (identifier == null) {
                break;
            }
            start = identifier.end();
            String raw = identifier.text(sql);
            String bare = raw.replace("`", "");
            if (bare.equalsIgnoreCase("IF") || bare.equalsIgnoreCase("NOT") || bare.equalsIgnoreCase("EXISTS") || bare.equals("*"))
                continue;
            addNamedRelation(sql, behavior, type, action, raw, identifier.start());
            count++;
        }
    }

    private void addQuotedPathAfter(String sql, StatementBehavior behavior, String anchor, TargetType type, BehaviorAction action) {
        int anchorStart = TiBehaviorText.findWord(sql, 0, anchor);
        if (anchorStart < 0)
            return;
        TiBehaviorTextSpan quoted = TiBehaviorText.nextQuoted(sql, TiBehaviorText.skipWhitespace(sql, anchorStart + anchor.length()));
        if (quoted == null)
            return;
        String raw = quoted.text(sql);
        String value = raw.substring(1, raw.length() - 1).replace("''", "'").replace("\"\"", "\"");
        if (type == TargetType.File)
            value = TiBehaviorText.collapseSlashes(value);
        addNamedRelation(sql, behavior, type, action, value, quoted.start(), raw);
    }

    private void addNamedRelation(String sql, StatementBehavior behavior, TargetType type, BehaviorAction action, String raw, int offset) {
        addNamedRelation(sql, behavior, type, action, TiBehaviorText.normalizeQualifiedName(raw), offset, raw);
    }

    private void addNamedRelation(String sql, StatementBehavior behavior, TargetType type, BehaviorAction action, String value, int offset, String sourceText) {
        removeUnknownFallback(behavior);
        int[] pos = position(sql, offset);
        CommonToken token = new CommonToken(0, sourceText);
        token.setLine(tokenLine(pos));
        token.setCharPositionInLine(tokenColumn(pos));
        RdbBehaviorObjectFactory factory = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        BehaviorObject object;
        if (type == TargetType.File || type == TargetType.Query || type == TargetType.Job || type == TargetType.Replication || type == TargetType.ConfigKey) {
            object = factory.instanceObject(type, token, value);
        } else {
            object = factory.object(type, token, token, TiBehaviorText.qualifiedNameParts(value));
        }
        addRelation(behavior, object, action);
    }

    private void addRelation(StatementBehavior behavior, BehaviorObject object, BehaviorAction action) {
        if (object == null || behavior.getRelations()
            .stream()
            .anyMatch(r -> r.getSubject() != null && r.getSubject().getObjectType() == object.getObjectType()
                           && StringUtils.equalsIgnoreCase(r.getSubject().getObjectPath(), object.getObjectPath()) && r.getAction() == action))
            return;
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(action);
        behavior.getRelations().add(relation);
    }

    private void linkBackupOrRestore(StatementBehavior behavior, TargetType dataType, boolean backup) {
        List<BehaviorRelation> data = behavior.getRelations()
            .stream()
            .filter(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == dataType)
            .toList();
        BehaviorRelation file = behavior.getRelations()
            .stream()
            .filter(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.File)
            .findFirst()
            .orElse(null);
        if (file == null || data.isEmpty())
            return;
        if (backup) {
            BehaviorRelation linked = new BehaviorRelation();
            linked.setSubject(file.getSubject());
            linked.setAction(BehaviorAction.EXPORT);
            data.forEach(relation -> linked.getTarget().add(relation.getSubject()));
            behavior.getRelations().remove(file);
            behavior.getRelations().removeAll(data);
            behavior.getRelations().add(linked);
        } else {
            behavior.getRelations().remove(file);
            data.forEach(relation -> relation.getTarget().add(file.getSubject()));
        }
    }

    private void removeUnknownFallback(StatementBehavior behavior) {
        behavior.getRelations().removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Unknown);
    }

    private void removeSupersededUnknown(SplitQueryType statementType, StatementBehavior behavior) {
        if (statementType == SplitQueryType.UNKNOWN)
            return;
        boolean concrete = behavior.getRelations()
            .stream()
            .anyMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() != TargetType.Unknown && relation.getAction() != BehaviorAction.UNKNOWN);
        if (!concrete)
            return;
        removeUnknownFallback(behavior);
        List<String> knownObjects = behavior.getRelations()
            .stream()
            .filter(relation -> relation.getAction() != BehaviorAction.UNKNOWN && relation.getSubject() != null)
            .map(relation -> relation.getSubject().getObjectType() + "|" + relation.getSubject().getObjectPath())
            .toList();
        behavior.getRelations()
            .removeIf(relation -> relation.getAction() == BehaviorAction.UNKNOWN
                                  && (relation.getSubject() == null || relation.getSubject().getObjectName() == null || knownObjects.stream()
                                      .anyMatch(key -> StringUtils.equalsIgnoreCase(key, relation.getSubject().getObjectType() + "|" + relation.getSubject().getObjectPath()))));
    }

    private void removeUnnamedFallback(StatementBehavior behavior, TargetType type) {
        behavior.getRelations()
            .removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == type && relation.getSubject().getObjectName() == null);
    }

    private boolean hasLegacyFunctionOnlyFallback(SplitQueryType type) {
        return type != SplitQueryType.SELECT && type != SplitQueryType.BLOCK && type != SplitQueryType.PROGRAM_CONTROL && type != SplitQueryType.UNKNOWN
               && type != SplitQueryType.TRANSACTION;
    }

    private void addBindingBodyRelations(String sql, SplitQueryType statementType, StatementBehavior behavior) {
        if (statementType != SplitQueryType.ADMIN_PERFORMANCE || TiBehaviorText.findWord(sql, 0, "BINDING") < 0) {
            return;
        }
        int using = findTopLevelWord(sql, "USING");
        if (using < 0) {
            return;
        }
        int bodyStart = TiBehaviorText.skipWhitespace(sql, using + "USING".length());
        if (TiBehaviorText.startsWithWord(sql, bodyStart, "PLAN")) {
            return;
        }
        int bodyEnd = sql.length();
        while (bodyEnd > bodyStart && (Character.isWhitespace(sql.charAt(bodyEnd - 1)) || sql.charAt(bodyEnd - 1) == ';')) {
            bodyEnd--;
        }
        if (bodyStart >= bodyEnd) {
            return;
        }
        int[] position = position(sql, bodyStart);
        int nestedLine = absoluteLine(position);
        int nestedColumn = absoluteColumn(position);
        List<StatementBehavior> nested = new TiBehaviorAnalysisSpi(provider.config()).analysisBehavior(sql.substring(bodyStart, bodyEnd), levels, nestedLine, nestedColumn);
        nested.forEach(statement -> behavior.getRelations().addAll(statement.getRelations()));
    }

    private void addLooseShowWhereRelations(String sql, SplitQueryType statementType, StatementBehavior behavior) {
        String normalized = sql.stripLeading();
        if (!normalized.regionMatches(true, 0, "SHOW", 0, "SHOW".length()) || statementType != SplitQueryType.ADMIN_PERFORMANCE && statementType != SplitQueryType.METADATA) {
            return;
        }
        int where = findTopLevelWord(sql, "WHERE");
        if (where < 0) {
            return;
        }
        int expressionStart = TiBehaviorText.skipWhitespace(sql, where + "WHERE".length());
        int expressionEnd = sql.length();
        while (expressionEnd > expressionStart && (Character.isWhitespace(sql.charAt(expressionEnd - 1)) || sql.charAt(expressionEnd - 1) == ';')) {
            expressionEnd--;
        }
        if (expressionStart >= expressionEnd) {
            return;
        }
        String prefix = "SELECT ";
        int[] position = position(sql, expressionStart);
        int nestedLine = absoluteLine(position);
        int nestedColumn = absoluteColumn(position) - prefix.length();
        List<StatementBehavior> nested = new TiBehaviorAnalysisSpi(provider.config())
            .analysisBehavior(prefix + sql.substring(expressionStart, expressionEnd), levels, nestedLine, nestedColumn);
        nested.forEach(statement -> statement.getRelations()
            .stream()
            .filter(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Function)
            .forEach(behavior.getRelations()::add));
    }

    private void addTiDBEmbeddedStatementRelations(String sql, SplitQueryType statementType, StatementBehavior behavior) {
        int bodyStart = -1;
        String normalized = sql.stripLeading();
        if (normalized.regionMatches(true, 0, "TRACE", 0, "TRACE".length())) {
            int trace = TiBehaviorText.findWord(sql, 0, "TRACE");
            bodyStart = TiBehaviorText.findWord(sql, trace + "TRACE".length(), "SELECT", "WITH", "UPDATE", "DELETE", "INSERT", "REPLACE");
        } else if (statementType == SplitQueryType.ADMIN_PERFORMANCE && TiBehaviorText.findWord(sql, 0, "BINDING") >= 0) {
            int keyword = findTopLevelWord(sql, "FOR");
            if (keyword >= 0) {
                bodyStart = TiBehaviorText.skipWhitespace(sql, keyword + "FOR".length());
                if (TiBehaviorText.startsWithWord(sql, bodyStart, "SQL")) {
                    return;
                }
            }
        } else if (statementType == SplitQueryType.ADMIN_PERFORMANCE) {
            int explain = TiBehaviorText.findWord(sql, 0, "EXPLAIN");
            int nestedStatement = explain < 0 ? -1 : TiBehaviorText.findWord(sql, explain + "EXPLAIN".length(), "SELECT", "WITH", "UPDATE", "DELETE", "INSERT", "REPLACE");
            if (nestedStatement >= 0) {
                bodyStart = explain;
            }
        }
        if (bodyStart < 0 || bodyStart >= sql.length()) {
            return;
        }
        if (TiBehaviorText.startsWithWord(sql, bodyStart, "SQL")) {
            int digest = TiBehaviorText.skipWhitespace(sql, TiBehaviorText.wordEnd(sql, bodyStart));
            if (TiBehaviorText.startsWithWord(sql, digest, "DIGEST")) {
                return;
            }
        }
        int bodyEnd = sql.length();
        if (statementType == SplitQueryType.ADMIN_PERFORMANCE && TiBehaviorText.findWord(sql, 0, "BINDING") >= 0) {
            int using = findTopLevelWord(sql, "USING");
            if (using > bodyStart) {
                bodyEnd = using;
            }
        }
        while (bodyEnd > bodyStart && (Character.isWhitespace(sql.charAt(bodyEnd - 1)) || sql.charAt(bodyEnd - 1) == ';')) {
            bodyEnd--;
        }
        int[] position = position(sql, bodyStart);
        int nestedLine = absoluteLine(position);
        int nestedColumn = absoluteColumn(position);
        List<StatementBehavior> nested = new TiBehaviorAnalysisSpi(provider.config()).analysisBehavior(sql.substring(bodyStart, bodyEnd), levels, nestedLine, nestedColumn);
        nested.forEach(statement -> behavior.getRelations().addAll(statement.getRelations()));
    }

    private void addTiDBLooseObjectRelations(String sql, StatementBehavior behavior) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("ADMIN CHECKSUM TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", null, BehaviorAction.CHECKSUM);
        } else if (normalized.startsWith("ADMIN CHECK TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", null, BehaviorAction.VALIDATE);
        } else if (normalized.startsWith("ADMIN CLEANUP TABLE LOCK")) {
            addLooseTableList(sql, behavior, "LOCK", null, BehaviorAction.UNLOCK);
        } else if (normalized.startsWith("DROP STATS") || normalized.startsWith("DROP STATISTICS")) {
            addLooseObjectList(sql, behavior, normalized
                .startsWith("DROP STATISTICS") ? "STATISTICS" : "STATS", "PARTITION", BehaviorAction.DROP, TargetType.Statistics, Integer.MAX_VALUE, 0, true);
            behavior.getRelations()
                .removeIf(relation -> relation.getAction() == BehaviorAction.UNKNOWN && relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
        } else if (normalized.startsWith("CREATE STATISTICS")) {
            addLooseObjectList(sql, behavior, "STATISTICS", "ON", BehaviorAction.CREATE, TargetType.Statistics, 1, 0, true);
            behavior.getRelations()
                .stream()
                .filter(relation -> relation.getAction() == BehaviorAction.UNKNOWN && relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table)
                .forEach(relation -> relation.setAction(BehaviorAction.READ));
        } else if (normalized.startsWith("REFRESH STATS")) {
            addStatisticsScopes(sql, behavior, "STATS", BehaviorAction.REFRESH);
        } else if (normalized.startsWith("ANALYZE INCREMENTAL TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", "INDEX", BehaviorAction.ANALYZE, 1);
        } else if (normalized.startsWith("SPLIT ")) {
            addLooseTableList(sql, behavior, "TABLE", "PARTITION", BehaviorAction.ALTER, 1);
        } else if (normalized.startsWith("RECOVER TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", null, BehaviorAction.RECOVER, 1);
        } else if (normalized.startsWith("ADMIN REPAIR TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", "CREATE", BehaviorAction.REPAIR, 1);
        } else if (normalized.startsWith("SHOW TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", "REGIONS", BehaviorAction.READ, 1);
        } else if (normalized.startsWith("ALTER TABLE") && normalized.contains(" COMPACT")) {
            addLooseTableList(sql, behavior, "TABLE", "COMPACT", BehaviorAction.OPTIMIZE, 1);
        } else if (normalized.startsWith("LOCK STATS")) {
            addStatisticsScopes(sql, behavior, "STATS", BehaviorAction.LOCK);
        } else if (normalized.startsWith("UNLOCK STATS")) {
            addStatisticsScopes(sql, behavior, "STATS", BehaviorAction.UNLOCK);
        } else if (normalized.startsWith("FLASHBACK TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", "TO", BehaviorAction.RECOVER);
        } else if (normalized.startsWith("DISTRIBUTE TABLE")) {
            addLooseTableList(sql, behavior, "TABLE", "RULE", BehaviorAction.ALTER, 1);
        } else if (normalized.startsWith("SHOW STATS_")) {
            addLooseObjectList(sql, behavior, "SHOW", "WHERE", BehaviorAction.READ, TargetType.Table, 1, 0, false);
        } else if (normalized.startsWith("SHOW CREATE SEQUENCE")) {
            addLooseObjectList(sql, behavior, "SEQUENCE", null, BehaviorAction.READ, TargetType.Sequence, 1, 0, false);
        } else if (normalized.startsWith("ADMIN CHECK INDEX") || normalized.startsWith("ADMIN CLEANUP INDEX") || normalized.startsWith("ADMIN RECOVER INDEX")) {
            addLooseTableList(sql, behavior, "INDEX", null, BehaviorAction.VALIDATE, 1);
        } else if (normalized.startsWith("ADMIN SHOW")) {
            addLooseTableList(sql, behavior, "SHOW", "NEXT_ROW_ID", BehaviorAction.READ, 1);
        } else if (normalized.startsWith("CREATE TABLE")) {
            addCreateTableFallback(sql, behavior);
        } else if (normalized.startsWith("ALTER TABLE") && normalized.contains("EXCHANGE PARTITION")) {
            int with = TiBehaviorText.findWord(sql, 0, "WITH");
            if (with >= 0) {
                String suffix = sql.substring(with);
                addLooseObjectList(suffix, behavior, "TABLE", null, BehaviorAction.ALTER, TargetType.Table, 1, with, false);
            }
        }
        if (normalized.startsWith("ADMIN REPAIR TABLE") && normalized.contains("CREATE TABLE")) {
            int create = TiBehaviorText.findWord(sql, 0, "CREATE");
            if (create >= 0) {
                String suffix = sql.substring(create);
                addLooseObjectList(suffix, behavior, "TABLE", null, BehaviorAction.CREATE, TargetType.Table, 1, create, false);
            }
        }
        addSequenceFunctionRelations(sql, behavior);
    }

    private void addLooseTableList(String sql, StatementBehavior behavior, String afterWord, String beforeWord, BehaviorAction action) {
        addLooseTableList(sql, behavior, afterWord, beforeWord, action, Integer.MAX_VALUE, 0, true);
    }

    private void addLooseTableList(String sql, StatementBehavior behavior, String afterWord, String beforeWord, BehaviorAction action, int limit) {
        addLooseTableList(sql, behavior, afterWord, beforeWord, action, limit, 0, true);
    }

    private void addLooseTableList(String sql, StatementBehavior behavior, String afterWord, String beforeWord, BehaviorAction action, int limit, int sourceOffset,
                                   boolean removeUnnamed) {
        addLooseObjectList(sql, behavior, afterWord, beforeWord, action, TargetType.Table, limit, sourceOffset, removeUnnamed);
    }

    private void addStatisticsScopes(String sql, StatementBehavior behavior, String afterWord, BehaviorAction action) {
        int anchor = TiBehaviorText.findWord(sql, 0, afterWord);
        if (anchor < 0)
            return;
        int start = TiBehaviorText.skipWhitespace(sql, anchor + afterWord.length());
        int end = TiBehaviorText.findWord(sql, start, "PARTITION");
        if (end < 0)
            end = TiBehaviorText.findWord(sql, start, "GLOBAL");
        if (end < 0)
            end = sql.length();
        while (start < end) {
            TiBehaviorTextSpan identifier = TiBehaviorText.nextIdentifier(sql, start, end, true);
            if (identifier == null) {
                break;
            }
            start = identifier.end();
            addNamedRelation(sql, behavior, TargetType.Statistics, action, identifier.text(sql), identifier.start());
        }
        behavior.getRelations()
            .removeIf(relation -> relation.getAction() == BehaviorAction.UNKNOWN && relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table);
        if (behavior.getRelations()
            .stream()
            .noneMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Statistics && relation.getAction() == action)) {
            addUnnamedRelation(sql, behavior, TargetType.Statistics, action);
        }
    }

    private void addLooseObjectList(String sql, StatementBehavior behavior, String afterWord, String beforeWord, BehaviorAction action, TargetType targetType, int limit,
                                    int sourceOffset, boolean removeUnnamed) {
        int start = TiBehaviorText.findWord(sql, 0, afterWord);
        if (start < 0) {
            return;
        }
        start = TiBehaviorText.skipWhitespace(sql, start + afterWord.length());
        int end = beforeWord == null ? sql.length() : TiBehaviorText.findWord(sql, start, beforeWord);
        if (end < 0) {
            end = sql.length();
        }
        if (removeUnnamed) {
            behavior.getRelations()
                .removeIf(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == targetType && relation.getSubject().getObjectName() == null);
        }
        int count = 0;
        while (start < end && count++ < limit) {
            TiBehaviorTextSpan identifier = TiBehaviorText.nextIdentifier(sql, start, end, false);
            if (identifier == null) {
                break;
            }
            start = identifier.end();
            String raw = identifier.text(sql);
            if (isLooseStopWord(raw)) {
                break;
            }
            int absoluteStart = sourceOffset + identifier.start();
            BehaviorObject object = looseObject(sql, targetType, raw, absoluteStart);
            if (object != null) {
                BehaviorRelation existing = behavior.getRelations()
                    .stream()
                    .filter(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == targetType
                                        && StringUtils.equalsIgnoreCase(relation.getSubject().getObjectPath(), object.getObjectPath()))
                    .findFirst()
                    .orElse(null);
                if (existing != null && existing.getAction() == BehaviorAction.UNKNOWN) {
                    existing.setAction(action);
                } else if (existing == null) {
                    BehaviorRelation relation = new BehaviorRelation();
                    relation.setSubject(object);
                    relation.setAction(action);
                    behavior.getRelations().add(relation);
                }
            }
        }
    }

    private void addCreateTableFallback(String sql, StatementBehavior behavior) {
        if (behavior.getRelations()
            .stream()
            .anyMatch(relation -> relation.getSubject() != null && relation.getSubject().getObjectType() == TargetType.Table && relation.getSubject().getObjectName() != null)) {
            return;
        }
        int table = TiBehaviorText.findWord(sql, 0, "TABLE");
        int nameStart = table < 0 ? -1 : TiBehaviorText.skipWhitespace(sql, table + "TABLE".length());
        for (String optional : List.of("IF", "NOT", "EXISTS")) {
            if (nameStart >= 0 && TiBehaviorText.startsWithWord(sql, nameStart, optional)) {
                nameStart = TiBehaviorText.skipWhitespace(sql, TiBehaviorText.wordEnd(sql, nameStart));
            }
        }
        if (nameStart < 0) {
            return;
        }
        TiBehaviorTextSpan identifier = TiBehaviorText.identifierAt(sql, nameStart, sql.length(), false);
        if (identifier == null) {
            return;
        }
        BehaviorObject object = looseObject(sql, TargetType.Table, identifier.text(sql), identifier.start());
        if (object != null && !containsObject(behavior, TargetType.Table, object)) {
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction(BehaviorAction.CREATE);
            behavior.getRelations().add(relation);
        }
    }

    private void addSequenceFunctionRelations(String sql, StatementBehavior behavior) {
        int searchFrom = 0;
        while (searchFrom < sql.length()) {
            int functionStart = TiBehaviorText.findWord(sql, searchFrom, "NEXTVAL", "LASTVAL", "SETVAL");
            if (functionStart < 0) {
                break;
            }
            int functionEnd = TiBehaviorText.wordEnd(sql, functionStart);
            String function = sql.substring(functionStart, functionEnd);
            int opening = TiBehaviorText.skipWhitespace(sql, functionEnd);
            if (opening >= sql.length() || sql.charAt(opening) != '(') {
                searchFrom = functionEnd;
                continue;
            }
            int nameStart = TiBehaviorText.skipWhitespace(sql, opening + 1);
            TiBehaviorTextSpan identifier = TiBehaviorText.identifierAt(sql, nameStart, sql.length(), false);
            if (identifier == null) {
                searchFrom = opening + 1;
                continue;
            }
            BehaviorObject object = looseObject(sql, TargetType.Sequence, identifier.text(sql), identifier.start());
            if (object == null || containsObject(behavior, TargetType.Sequence, object)) {
                searchFrom = identifier.end();
                continue;
            }
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction("SETVAL".equalsIgnoreCase(function) ? BehaviorAction.UPDATE : BehaviorAction.READ);
            behavior.getRelations().add(relation);
            searchFrom = identifier.end();
        }
    }

    private static boolean containsObject(StatementBehavior behavior, TargetType type, BehaviorObject candidate) {
        return behavior.getRelations().stream().anyMatch(relation -> {
            BehaviorObject existing = relation.getSubject();
            return existing != null && existing.getObjectType() == type && StringUtils.equalsIgnoreCase(existing.getObjectPath(), candidate.getObjectPath());
        });
    }

    private BehaviorObject looseObject(String sql, TargetType type, String rawName, int offset) {
        List<String> names = TiBehaviorText.qualifiedNameParts(rawName);
        int[] position = position(statementSql, offset);
        CommonToken token = new CommonToken(0, rawName);
        token.setLine(tokenLine(position));
        token.setCharPositionInLine(tokenColumn(position));
        return new RdbBehaviorObjectFactory(levels, baseLine, baseColumn).object(type, token, token, names);
    }

    private static boolean isLooseStopWord(String value) {
        String normalized = value.replace("`", "").toUpperCase(Locale.ROOT);
        return normalized.equals("PARTITION") || normalized.equals("REGIONS") || normalized.equals("CREATE") || normalized.equals("FROM") || normalized.equals("TO")
               || normalized.equals("BETWEEN") || normalized.equals("VALUES") || normalized.equals("WHERE");
    }

    private static boolean isLoadDataJobCommand(String sql) {
        return TiBehaviorText.afterStartingWords(sql, "DROP", "LOAD", "DATA", "JOB") >= 0
               || TiBehaviorText.afterStartingWords(sql, "PAUSE", "LOAD", "DATA", "JOB") >= 0
               || TiBehaviorText.afterStartingWords(sql, "RESUME", "LOAD", "DATA", "JOB") >= 0;
    }

    private static int findTopLevelWord(String sql, String word) {
        int depth = 0;
        char quote = 0;
        for (int index = 0; index < sql.length(); index++) {
            char value = sql.charAt(index);
            if (quote != 0) {
                if (value == '\\') {
                    index++;
                } else if (value == quote) {
                    if (index + 1 < sql.length() && sql.charAt(index + 1) == quote) {
                        index++;
                    } else {
                        quote = 0;
                    }
                }
                continue;
            }
            if (value == '\'' || value == '"' || value == '`') {
                quote = value;
            } else if (value == '(') {
                depth++;
            } else if (value == ')') {
                depth = Math.max(0, depth - 1);
            } else if (depth == 0 && TiBehaviorText.startsWithWord(sql, index, word)) {
                return index;
            }
        }
        return -1;
    }

    private static int[] position(String sql, int offset) {
        int line = 0;
        int column = 0;
        for (int index = 0; index < offset;) {
            int codePoint = sql.codePointAt(index);
            if (codePoint == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
            index += Character.charCount(codePoint);
        }
        return new int[] { line, column };
    }

    private int tokenLine(int[] relativePosition) {
        return statementContext.getStart().getLine() + relativePosition[0];
    }

    private int tokenColumn(int[] relativePosition) {
        return relativePosition[0] == 0 ? statementContext.getStart().getCharPositionInLine() + relativePosition[1] : relativePosition[1];
    }

    private int absoluteLine(int[] relativePosition) {
        return baseLine + tokenLine(relativePosition) - 1;
    }

    private int absoluteColumn(int[] relativePosition) {
        int line = tokenLine(relativePosition);
        return (line == 1 ? baseColumn : 0) + tokenColumn(relativePosition);
    }

    private TargetType fallbackType(SplitQueryType type) {
        return switch (type) {
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, SESSION_VARIABLE_RW -> TargetType.ConfigKey;
            case CREATE_REPLICATION, ALTER_REPLICATION, DROP_REPLICATION, ADMIN_REPLICATION -> TargetType.Replication;
            case CREATE_LOG, ALTER_LOG, DROP_LOG, LOG_READ, ADMIN_LOG, MAINTAIN_LOG -> TargetType.Log;
            case CREATE_LIBRARY, ALTER_LIBRARY, DROP_LIBRARY, COMMENT_LIBRARY -> TargetType.Library;
            case CREATE_USER, ALTER_USER, DROP_USER, RENAME_USER, SWITCH_USER -> TargetType.User;
            case CREATE_ROLE, ALTER_ROLE, DROP_ROLE, RENAME_ROLE, SWITCH_ROLE -> TargetType.Role;
            case DATA_IMPORT, DATA_EXPORT -> TargetType.File;
            case ADMIN_TABLE -> TargetType.Table;
            case ADMIN, ADMIN_PERFORMANCE, PERFORMANCE, METADATA, SESSION_LOCK, UNSAFE -> TargetType.Instance;
            case TRANSACTION -> TargetType.Transaction;
            case ALTER_SCHEMA -> TargetType.Schema;
            case PROGRAM_CONTROL -> TargetType.ProgramObject;
            case BLOCK, UNKNOWN -> TargetType.Unknown;
            default -> null;
        };
    }
}
