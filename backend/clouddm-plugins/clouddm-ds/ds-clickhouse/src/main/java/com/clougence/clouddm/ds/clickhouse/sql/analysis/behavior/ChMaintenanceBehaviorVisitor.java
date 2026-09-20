/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

/** Maintenance objects describe storage and running work, not table-definition changes. */
class ChMaintenanceBehaviorVisitor extends ChStatementBehaviorVisitor {
    private final String instanceScope;

    ChMaintenanceBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
        instanceScope = objects.instanceObject(TargetType.Instance, parser.getTokenStream().get(0)).getObjectPath();
    }

    private BehaviorObject scoped(TargetType type, Token start, Token stop, String path) {
        BehaviorObject result = objects.instanceObject(type, start, stop, "");
        result.setObjectPath(path);
        if (type == TargetType.Log || type == TargetType.File) {
            result.setObjectName(new ObjectName(null, null, instanceRelativeName(result)));
        }
        return result;
    }

    private BehaviorObject child(TargetType type, ParserRuleContext context, BehaviorObject parent, String suffix) {
        String path = instanceScope;
        if (parent != null) {
            path = parent.getObjectPath();
        }
        if (!suffix.isEmpty()) {
            path += suffix + "/";
        }
        return scoped(type, context.getStart(), context.getStop(), path);
    }

    private BehaviorObject runtime(TargetType type, TableIdentifierContext table, Token start, Token stop, String family) {
        if (table != null) {
            return child(type, table, object(TargetType.Table, table), family);
        }
        String path = instanceScope;
        if (!family.isEmpty()) {
            path += family + "/";
        }
        return scoped(type, start, stop, path);
    }

    private BehaviorObject partition(BehaviorObject table, PartitionClauseContext ctx, boolean detached) {
        String prefix = detached ? "detached/" : "";
        if (ctx.ID() != null) {
            return child(TargetType.Partition, ctx.stringLiteral(), table, prefix + name(ctx.stringLiteral()));
        }
        // Partition values require metadata and evaluation; a value is not a partition ID.
        return scoped(TargetType.Partition, ctx.getStart(), ctx.getStop(), table.getObjectPath() + prefix);
    }

    private BehaviorObject part(BehaviorObject table, StringLiteralContext ctx, boolean detached) {
        String prefix = detached ? "detached/parts/" : "parts/";
        return child(TargetType.DataPart, ctx, table, prefix + name(ctx));
    }

    @Override
    public Void visitOptimizeStmt(OptimizeStmtContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableIdentifier());
        if (ctx.DRY() != null) {
            for (StringLiteralContext part : ctx.stringLiteral()) {
                add(SplitQueryType.PERFORMANCE, BehaviorAction.VALIDATE, part(table, part, false));
            }
        } else if (ctx.partitionClause() != null) {
            add(SplitQueryType.ADMIN_PARTITION, BehaviorAction.OPTIMIZE, partition(table, ctx.partitionClause(), false));
        } else {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, table);
        }
        if (ctx.partitionClause() != null) {
            visit(ctx.partitionClause());
        }
        return null;
    }

    @Override
    public Void visitCheckStmt(CheckStmtContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN_TABLE;
        BehaviorObject subject;
        if (ctx.databaseIdentifier() != null) {
            type = SplitQueryType.ADMIN;
            subject = object(TargetType.Schema, ctx.databaseIdentifier());
        } else if (ctx.ALL() != null) {
            subject = objects.unnamedObject(TargetType.Table, ctx, UmiTypes.Catalog);
            subject = scoped(TargetType.Table, ctx.ALL().getSymbol(), ctx.TABLES().getSymbol(), subject.getObjectPath());
        } else {
            subject = object(TargetType.Table, ctx.tableIdentifier());
            if (ctx.partitionClause() != null) {
                type = SplitQueryType.ADMIN_PARTITION;
                subject = partition(subject, ctx.partitionClause(), false);
            } else if (ctx.PART() != null) {
                subject = part(subject, ctx.stringLiteral(), false);
            }
        }
        add(type, BehaviorAction.VALIDATE, subject);
        if (ctx.partitionClause() != null) {
            visit(ctx.partitionClause());
        }
        return null;
    }

    @Override
    protected boolean maintenanceClause(AlterTableClauseContext clause, BehaviorObject table) {
        SplitQueryType type = SplitQueryType.ALTER_TABLE;
        if (clause instanceof AlterTableClauseAttachContext ctx) {
            if (ctx.FROM() != null) {
                BehaviorObject source = partition(object(TargetType.Table, ctx.tableIdentifier()), ctx.partitionClause(), false);
                add(type, BehaviorAction.COPY, source, List.of(partition(table, ctx.partitionClause(), false)));
            } else if (ctx.PART() != null) {
                add(type, BehaviorAction.CONFIGURE, part(table, ctx.stringLiteral(), false));
            } else {
                add(type, BehaviorAction.CONFIGURE, partition(table, ctx.partitionClause(), false));
            }
        } else if (clause instanceof AlterTableClauseDetachContext ctx) {
            BehaviorObject target;
            if (ctx.PART() != null) {
                target = part(table, ctx.stringLiteral(), false);
            } else {
                target = partition(table, ctx.partitionClause(), false);
            }
            add(type, BehaviorAction.CONFIGURE, target);
        } else if (clause instanceof AlterTableClauseDropPartitionContext ctx) {
            add(type, BehaviorAction.DROP, partition(table, ctx.partitionClause(), false));
        } else if (clause instanceof AlterTableClauseDropPartContext ctx) {
            add(type, BehaviorAction.DROP, part(table, ctx.stringLiteral(), false));
        } else if (clause instanceof AlterTableClauseDropDetachedContext ctx) {
            BehaviorObject target;
            if (ctx.PART() != null) {
                target = part(table, ctx.stringLiteral(), true);
            } else {
                target = partition(table, ctx.partitionClause(), true);
            }
            add(type, BehaviorAction.DROP, target);
        } else if (clause instanceof AlterTableClauseReplaceContext ctx) {
            add(type, BehaviorAction.REPLACE, partition(table, ctx.partitionClause(), false),
                List.of(partition(object(TargetType.Table, ctx.tableIdentifier()), ctx.partitionClause(), false)));
        } else if (clause instanceof AlterTableClauseMovePartitionContext ctx) {
            BehaviorObject target;
            if (ctx.tableIdentifier() != null) {
                target = partition(object(TargetType.Table, ctx.tableIdentifier()), ctx.partitionClause(), false);
            } else {
                target = placement(ctx.stringLiteral(), ctx.DISK() != null);
            }
            add(type, BehaviorAction.MOVE, partition(table, ctx.partitionClause(), false), List.of(target));
        } else if (clause instanceof AlterTableClauseMovePartContext ctx) {
            add(type, BehaviorAction.MOVE, part(table, ctx.stringLiteral(0), false), List.of(placement(ctx.stringLiteral(1), ctx.DISK() != null)));
        } else if (clause instanceof AlterTableClauseFetchContext wrapper) {
            FetchPartitionClauseContext ctx = wrapper.fetchPartitionClause();
            BehaviorObject target;
            int sourceIndex = 0;
            if (ctx.PART() != null) {
                target = part(table, ctx.stringLiteral(0), true);
                sourceIndex = 1;
            } else {
                target = partition(table, ctx.partitionClause(), true);
            }
            StringLiteralContext source = ctx.stringLiteral(sourceIndex);
            add(type, BehaviorAction.IMPORT, target, List.of(child(TargetType.Replication, source, null, keeperPath(source))));
        } else if (clause instanceof AlterTableClauseFreezePartitionContext ctx) {
            BehaviorObject source = table;
            if (ctx.partitionClause() != null) {
                source = partition(table, ctx.partitionClause(), false);
            }
            BehaviorObject backup = frozen(source, ctx.stringLiteral(), ctx.FREEZE().getSymbol());
            add(type, BehaviorAction.COPY, source, List.of(backup));
        } else if (clause instanceof AlterTableClauseUnfreezeContext ctx) {
            BehaviorObject scope = table;
            if (ctx.partitionClause() != null) {
                scope = partition(table, ctx.partitionClause(), false);
            }
            add(type, BehaviorAction.PURGE, frozen(scope, ctx.stringLiteral(), ctx.UNFREEZE().getSymbol()));
        } else if (clause instanceof AlterTableClauseMaterializeTTLContext ctx) {
            BehaviorObject scope = table;
            if (ctx.partitionClause() != null) {
                scope = partition(table, ctx.partitionClause(), false);
            }
            add(type, BehaviorAction.OPTIMIZE, scope);
        } else if (clause instanceof AlterTableClauseRewritePartsContext ctx) {
            BehaviorObject scope = table;
            if (ctx.partitionClause() != null) {
                scope = partition(table, ctx.partitionClause(), false);
            }
            add(type, BehaviorAction.OPTIMIZE, scope);
        } else if (clause instanceof AlterTableClauseStatisticsContext ctx) {
            BehaviorObject statistics;
            if (ctx.ALL() != null) {
                statistics = scoped(TargetType.Statistics, ctx.ALL().getSymbol(), ctx.ALL().getSymbol(), table.getObjectPath() + "statistics/");
            } else {
                String column = String.join(".", ctx.nestedIdentifier().identifier().stream().map(this::name).toList());
                statistics = child(TargetType.Statistics, ctx.nestedIdentifier(), table, "statistics/" + column);
            }
            BehaviorAction action = ctx.CLEAR() == null ? BehaviorAction.ANALYZE : BehaviorAction.PURGE;
            add(type, action, statistics);
        } else {
            return false;
        }
        // Keep executable partition expressions and parameters without treating stored strings as SQL.
        visitChildren(clause);
        return true;
    }

    private BehaviorObject placement(StringLiteralContext name, boolean disk) {
        if (disk) {
            return child(TargetType.Disk, name, null, name(name));
        }
        // The storage policy owning this volume is only available from the table's metadata.
        return child(TargetType.StorageVolume, name, null, "");
    }

    private BehaviorObject frozen(BehaviorObject scope, StringLiteralContext name, Token keyword) {
        if (name != null) {
            return child(TargetType.Backup, name, scope, "frozen/" + name(name));
        }
        return scoped(TargetType.Backup, keyword, keyword, scope.getObjectPath() + "frozen/");
    }

    private String keeperPath(StringLiteralContext ctx) {
        String path = name(ctx);
        String connection = "default";
        if (!path.startsWith("/")) {
            int colon = path.indexOf(':');
            if (colon >= 0) {
                connection = path.substring(0, colon);
                path = path.substring(colon + 1);
            }
        }
        return "keeper/" + connection + "/" + trimSlashes(path);
    }

    private String trimSlashes(String value) {
        int start = 0;
        int end = value.length();
        while (start < end && value.charAt(start) == '/') {
            start++;
        }
        while (end > start && value.charAt(end - 1) == '/') {
            end--;
        }
        return value.substring(start, end);
    }

    @Override
    public Void visitSystemTableControlStmt(SystemTableControlStmtContext ctx) {
        String family = "merges";
        Token start = ctx.MERGES() == null ? null : ctx.MERGES().getSymbol();
        Token stop = start;
        if (ctx.TTL() != null) {
            family = "ttl_merges";
            start = ctx.TTL().getSymbol();
        } else if (ctx.MOVES() != null) {
            family = "moves";
            start = stop = ctx.MOVES().getSymbol();
        } else if (ctx.CLEANUP() != null) {
            family = "cleanup";
            start = stop = ctx.CLEANUP().getSymbol();
        }
        BehaviorObject job;
        if (ctx.VOLUME() != null) {
            String path = instanceScope + name(ctx.identifier(0)) + "/" + name(ctx.identifier(1)) + "/merges/";
            job = scoped(TargetType.Job, ctx.identifier(0).getStart(), ctx.identifier(1).getStop(), path);
        } else {
            job = runtime(TargetType.Job, ctx.tableIdentifier(), start, stop, family);
        }
        add(SplitQueryType.ADMIN_TABLE, controlAction((TerminalNode) ctx.getChild(1)), job);
        return null;
    }

    @Override
    public Void visitSystemScheduleMergeStmt(SystemScheduleMergeStmtContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableIdentifier());
        for (StringLiteralContext value : ctx.stringLiteral()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, part(table, value, false));
        }
        return null;
    }

    @Override
    public Void visitSystemSyncMergesStmt(SystemSyncMergesStmtContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.READ,
            runtime(TargetType.Job, ctx.tableIdentifier(), ctx.MERGES().getSymbol(), ctx.MERGES().getSymbol(), "merges"));
        return null;
    }

    @Override
    public Void visitSystemWaitLoadingPartsStmt(SystemWaitLoadingPartsStmtContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.READ, child(TargetType.DataPart, ctx.tableIdentifier(), object(TargetType.Table, ctx.tableIdentifier()), "parts"));
        return null;
    }

    @Override
    public Void visitSystemPrewarmStmt(SystemPrewarmStmtContext ctx) {
        String family = ctx.MARK() == null ? "primary_index" : "mark";
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.LOAD, child(TargetType.Cache, ctx.tableIdentifier(), object(TargetType.Table, ctx.tableIdentifier()), family));
        return null;
    }

    @Override
    public Void visitSystemPrimaryKeyStmt(SystemPrimaryKeyStmtContext ctx) {
        TableIdentifierContext table = null;
        if (ctx.systemOptionalTableTarget() != null) {
            table = ctx.systemOptionalTableTarget().tableIdentifier();
        }
        BehaviorAction action = BehaviorAction.LOAD;
        if (ctx.UNLOAD() != null) {
            action = BehaviorAction.PURGE;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, action,
            runtime(TargetType.Cache, table, ctx.PRIMARY().getSymbol(), ctx.KEY().getSymbol(), "primary_key"));
        return null;
    }

    @Override
    public Void visitSystemReplicatedSendsStmt(SystemReplicatedSendsStmtContext ctx) {
        add(SplitQueryType.ALTER_REPLICATION, controlAction((TerminalNode) ctx.getChild(1)),
            runtime(TargetType.Replication, ctx.tableIdentifier(), ctx.REPLICATED().getSymbol(), ctx.SENDS().getSymbol(), "replication/replicated_sends"));
        return null;
    }

    @Override
    public Void visitSystemReplicationControlStmt(SystemReplicationControlStmtContext ctx) {
        Token start;
        Token stop;
        String family;
        if (ctx.FETCHES() != null) {
            start = stop = ctx.FETCHES().getSymbol();
            family = "fetches";
        } else if (ctx.PULLING() != null) {
            start = ctx.PULLING().getSymbol();
            stop = ctx.LOG().getSymbol();
            family = "pulling_replication_log";
        } else {
            start = ctx.REPLICATION().getSymbol();
            stop = ctx.QUEUES().getSymbol();
            family = "replication_queues";
        }
        add(SplitQueryType.ALTER_REPLICATION, controlAction((TerminalNode) ctx.getChild(1)),
            runtime(TargetType.Replication, ctx.tableIdentifier(), start, stop, "replication/" + family));
        return null;
    }

    @Override
    public Void visitSystemSyncReplicaStmt(SystemSyncReplicaStmtContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.tableIdentifier());
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.REFRESH, child(TargetType.Replication, ctx.tableIdentifier(), table, "replication"));
        Map<String, StringLiteralContext> sources = new LinkedHashMap<>();
        for (StringLiteralContext replica : ctx.stringLiteral()) {
            sources.putIfAbsent(name(replica), replica);
        }
        sources.forEach((value, replica) -> add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.READ, child(TargetType.Replica, replica, table, "replicas/" + value)));
        return null;
    }

    @Override
    public Void visitSystemSyncDatabaseReplicaStmt(SystemSyncDatabaseReplicaStmtContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.READ,
            child(TargetType.Replication, ctx.databaseIdentifier(), object(TargetType.Schema, ctx.databaseIdentifier()), "replication"));
        return null;
    }

    @Override
    public Void visitSystemRestartReplicaStmt(SystemRestartReplicaStmtContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.RESET,
            child(TargetType.Replica, ctx.tableIdentifier(), object(TargetType.Table, ctx.tableIdentifier()), "replicas"));
        return null;
    }

    @Override
    public Void visitSystemRestartReplicasStmt(SystemRestartReplicasStmtContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.RESET, scoped(TargetType.Replica, ctx.REPLICAS().getSymbol(), ctx.REPLICAS().getSymbol(), instanceScope));
        return null;
    }

    @Override
    public Void visitSystemRestoreReplicaStmt(SystemRestoreReplicaStmtContext ctx) {
        TableIdentifierContext table = ctx.systemTableTarget().tableIdentifier();
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.RECOVER, child(TargetType.Replica, table, object(TargetType.Table, table), "replicas"));
        return null;
    }

    @Override
    public Void visitSystemRestoreDatabaseReplicaStmt(SystemRestoreDatabaseReplicaStmtContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.RECOVER,
            child(TargetType.Replica, ctx.databaseIdentifier(), object(TargetType.Schema, ctx.databaseIdentifier()), "replicas"));
        return null;
    }

    @Override
    public Void visitSystemReconnectZooKeeperStmt(SystemReconnectZooKeeperStmtContext ctx) {
        add(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.RESET,
            scoped(TargetType.Replication, ctx.ZOOKEEPER().getSymbol(), ctx.ZOOKEEPER().getSymbol(), instanceScope + "keeper/default/"));
        return null;
    }

    @Override
    public Void visitSystemDropReplicaStmt(SystemDropReplicaStmtContext ctx) {
        StringLiteralContext replica = ctx.stringLiteral(0);
        String path = instanceScope;
        if (ctx.tableIdentifier() != null) {
            path = object(TargetType.Table, ctx.tableIdentifier()).getObjectPath() + "replicas/" + name(replica) + "/";
        } else if (ctx.databaseIdentifier() != null) {
            path = object(TargetType.Schema, ctx.databaseIdentifier()).getObjectPath();
        } else if (ctx.ZKPATH() != null) {
            path += keeperPath(ctx.stringLiteral(ctx.stringLiteral().size() - 1)) + "/replicas/" + name(replica) + "/";
        }
        add(SplitQueryType.DROP_REPLICATION, BehaviorAction.DROP, scoped(TargetType.Replica, replica.getStart(), replica.getStop(), path));
        return null;
    }

    @Override
    public Void visitSystemDropDatabaseReplicaStmt(SystemDropDatabaseReplicaStmtContext ctx) {
        StringLiteralContext replica = ctx.stringLiteral(0);
        String fullName = name(replica);
        if (ctx.SHARD() != null) {
            fullName = name(ctx.stringLiteral(1)) + "|" + fullName;
        }
        String scope = instanceScope;
        String path = scope;
        if (ctx.databaseIdentifier() != null) {
            scope = object(TargetType.Schema, ctx.databaseIdentifier()).getObjectPath();
            path = scope + "replicas/" + fullName + "/";
        } else if (ctx.ZKPATH() != null) {
            scope += keeperPath(ctx.stringLiteral(ctx.stringLiteral().size() - 1)) + "/";
            path = scope + "replicas/" + fullName + "/";
        }
        add(SplitQueryType.DROP_REPLICATION, BehaviorAction.DROP, scoped(TargetType.Replica, replica.getStart(), replica.getStop(), path));
        if (ctx.TABLES() != null) {
            add(SplitQueryType.DROP_REPLICATION, BehaviorAction.DROP, scoped(TargetType.Replica, ctx.TABLES().getSymbol(), ctx.TABLES().getSymbol(), scope));
        }
        return null;
    }

    @Override
    public Void visitSystemUnfreezeStmt(SystemUnfreezeStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.PURGE, child(TargetType.Backup, ctx.stringLiteral(), null, "frozen/" + name(ctx.stringLiteral())));
        return null;
    }

    @Override
    public Void visitSystemDistributedControlStmt(SystemDistributedControlStmtContext ctx) {
        TableIdentifierContext table = null;
        if (ctx.systemOptionalTableTarget() != null) {
            table = ctx.systemOptionalTableTarget().tableIdentifier();
        }
        add(SplitQueryType.ADMIN_TABLE, controlAction((TerminalNode) ctx.getChild(1)),
            runtime(TargetType.Queue, table, ctx.DISTRIBUTED().getSymbol(), ctx.SENDS().getSymbol(), "distributed"));
        return null;
    }

    @Override
    public Void visitSystemFlushDistributedStmt(SystemFlushDistributedStmtContext ctx) {
        TableIdentifierContext table = ctx.systemTableTarget().tableIdentifier();
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.FLUSH, child(TargetType.Queue, table, object(TargetType.Table, table), "distributed"));
        return null;
    }

    @Override
    public Void visitSystemFlushAsyncInsertStmt(SystemFlushAsyncInsertStmtContext ctx) {
        if (ctx.tableIdentifier().isEmpty()) {
            add(SplitQueryType.ADMIN_TABLE, BehaviorAction.FLUSH,
                scoped(TargetType.Queue, ctx.ASYNC().getSymbol(), ctx.QUEUE().getSymbol(), instanceScope + "async_insert/"));
        } else {
            for (TableIdentifierContext table : ctx.tableIdentifier()) {
                add(SplitQueryType.ADMIN_TABLE, BehaviorAction.FLUSH, child(TargetType.Queue, table, object(TargetType.Table, table), "async_insert"));
            }
        }
        return null;
    }

    @Override
    public Void visitSystemFlushObjectStorageQueueStmt(SystemFlushObjectStorageQueueStmtContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.systemTableTarget().tableIdentifier());
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.READ, child(TargetType.Job, ctx.stringLiteral(), table, "object_storage/" + name(ctx.stringLiteral())));
        return null;
    }

    @Override
    public Void visitSystemViewStmt(SystemViewStmtContext ctx) {
        if (ctx.REFRESH() != null) {
            add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Materialized, ctx.tableIdentifier()));
        } else {
            BehaviorObject view = object(TargetType.Materialized, ctx.tableIdentifier());
            add(SplitQueryType.ADMIN, controlAction((TerminalNode) ctx.getChild(1)), child(TargetType.Job, ctx.tableIdentifier(), view, "refresh"));
        }
        return null;
    }

    @Override
    public Void visitSystemViewsStmt(SystemViewsStmtContext ctx) {
        add(SplitQueryType.ADMIN, controlAction((TerminalNode) ctx.getChild(1)),
            scoped(TargetType.Job, ctx.VIEWS().getSymbol(), ctx.VIEWS().getSymbol(), instanceScope + "refresh/"));
        return null;
    }

    @Override
    public Void visitSystemReplicatedViewStmt(SystemReplicatedViewStmtContext ctx) {
        add(SplitQueryType.ADMIN, controlAction((TerminalNode) ctx.getChild(1)),
            child(TargetType.Replication, ctx.tableIdentifier(), object(TargetType.Materialized, ctx.tableIdentifier()), "replication/refresh"));
        return null;
    }

    @Override
    public Void visitSystemBackgroundStmt(SystemBackgroundStmtContext ctx) {
        Token start = ctx.getStart();
        Token stop = start;
        if (ctx.ALL() != null) {
            start = ctx.ALL().getSymbol();
            stop = ctx.BACKGROUND().getSymbol();
        }
        add(SplitQueryType.ADMIN, controlAction((TerminalNode) ctx.getChild(1)), runtime(TargetType.Job, ctx.tableIdentifier(), start, stop, "background"));
        return null;
    }

    private BehaviorAction controlAction(TerminalNode operation) {
        return switch (operation.getText().toUpperCase(Locale.ROOT)) {
            case "START" -> BehaviorAction.START;
            case "STOP", "PAUSE" -> BehaviorAction.STOP;
            case "WAIT" -> BehaviorAction.READ;
            case "CANCEL" -> BehaviorAction.TERMINATE;
            case "REFRESH" -> BehaviorAction.REFRESH;
            default -> throw new IllegalArgumentException("Unsupported background action: " + operation.getText());
        };
    }

    @Override
    public Void visitSystemSyncTransactionLogStmt(SystemSyncTransactionLogStmtContext ctx) {
        add(SplitQueryType.TRANSACTION, BehaviorAction.READ,
            scoped(TargetType.Log, ctx.TRANSACTION().getSymbol(), ctx.LOG().getSymbol(), instanceScope + "transaction/"));
        return null;
    }

    @Override
    public Void visitSystemSyncFileCacheStmt(SystemSyncFileCacheStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.FLUSH, scoped(TargetType.Instance, ctx.FILE().getSymbol(), ctx.CACHE().getSymbol(), instanceScope));
        return null;
    }

    @Override
    public Void visitSystemWaitBlobsCleanupStmt(SystemWaitBlobsCleanupStmtContext ctx) {
        SystemNamedTargetContext target = ctx.systemNamedTarget();
        ParserRuleContext value = target.identifier();
        if (value == null) {
            value = target.stringLiteral();
        }
        add(SplitQueryType.ADMIN, BehaviorAction.READ, child(TargetType.Disk, value, null, name(value)));
        return null;
    }

    @Override
    public Void visitUndropStmt(UndropStmtContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, BehaviorAction.RECOVER, object(TargetType.Table, ctx.tableIdentifier()));
        return null;
    }

    @Override
    public Void visitShowMergesStmt(ShowMergesStmtContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, scoped(TargetType.Job, ctx.MERGES().getSymbol(), ctx.MERGES().getSymbol(), instanceScope + "merges/"));
        return null;
    }

    @Override
    public Void visitKillMutationStmt(KillMutationStmtContext ctx) {
        killJob(ctx.killWhereClause(), ctx.MUTATION().getSymbol(), ctx.TEST() != null, "mutation", "mutation_id");
        return null;
    }

    @Override
    public Void visitKillPartMoveStmt(KillPartMoveStmtContext ctx) {
        killJob(ctx.killWhereClause(), ctx.PART_MOVE_TO_SHARD().getSymbol(), ctx.TEST() != null, "part_move", "task_uuid");
        return null;
    }

    private void killJob(KillWhereClauseContext where, Token keyword, boolean test, String family, String idKey) {
        Map<String, LiteralContext> values = new LinkedHashMap<>();
        equalities(where.columnExpr(), values);
        LiteralContext database = values.get("database");
        LiteralContext table = values.get("table");
        LiteralContext id = values.get(idKey);
        String path = instanceScope;
        Token start = keyword;
        Token stop = keyword;
        if (database != null && database.stringLiteral() != null) {
            path = objects.object(TargetType.Schema, database, List.of(name(database.stringLiteral()))).getObjectPath();
            if (table != null && table.stringLiteral() != null) {
                path += name(table.stringLiteral()) + "/" + family + "/";
                if (id != null && id.stringLiteral() != null) {
                    path += name(id.stringLiteral()) + "/";
                    start = id.getStart();
                    stop = id.getStop();
                }
            }
        }
        SplitQueryType type = test ? SplitQueryType.PERFORMANCE : SplitQueryType.ADMIN;
        BehaviorAction action = test ? BehaviorAction.READ : BehaviorAction.TERMINATE;
        add(type, action, scoped(TargetType.Job, start, stop, path));
        visit(where);
    }

    private void equalities(ColumnExprContext expression, Map<String, LiteralContext> values) {
        while (expression instanceof ColumnExprParensContext parens) {
            expression = parens.columnExpr();
        }
        if (expression instanceof ColumnExprAndContext and) {
            for (ColumnExprContext term : and.columnExpr()) {
                equalities(term, values);
            }
        } else if (expression instanceof ColumnExprPrecedence3Context comparison
                   && (comparison.EQ_SINGLE() != null || comparison.EQ_DOUBLE() != null)) {
            ColumnExprContext left = comparison.columnExpr(0);
            ColumnExprContext right = comparison.columnExpr(1);
            if (left instanceof ColumnExprLiteralContext && right instanceof ColumnExprIdentifierContext) {
                ColumnExprContext swapped = left;
                left = right;
                right = swapped;
            }
            if (left instanceof ColumnExprIdentifierContext field && field.columnIdentifier().tableIdentifier() == null
                && field.columnIdentifier().nestedIdentifier().identifier().size() == 1 && right instanceof ColumnExprLiteralContext literal) {
                String key = name(field.columnIdentifier().nestedIdentifier().identifier(0));
                LiteralContext value = literal.literal();
                // Contradictory equalities must not fabricate a precise identity.
                if (values.containsKey(key) && (values.get(key) == null || !name(values.get(key)).equals(name(value)))) {
                    values.put(key, null);
                } else if (!values.containsKey(key)) {
                    values.put(key, value);
                }
            }
        }
    }

    @Override
    public Void visitKillTransactionStmt(KillTransactionStmtContext ctx) {
        Map<String, LiteralContext> values = new LinkedHashMap<>();
        equalities(ctx.killWhereClause().columnExpr(), values);
        LiteralContext hash = values.get("tid_hash");
        BehaviorObject transaction;
        if (hash != null && hash.numberLiteral() != null) {
            transaction = child(TargetType.Transaction, hash, null, "hash/" + name(hash));
        } else {
            transaction = scoped(TargetType.Transaction, ctx.TRANSACTION().getSymbol(), ctx.TRANSACTION().getSymbol(), instanceScope);
        }
        BehaviorAction action = BehaviorAction.TERMINATE;
        if (ctx.TEST() != null) {
            action = BehaviorAction.READ;
        }
        add(SplitQueryType.TRANSACTION, action, transaction);
        visit(ctx.killWhereClause());
        return null;
    }

    @Override
    public Void visitBackupQuery(BackupQueryContext ctx) {
        backup(ctx.backupElements(), ctx.backupDestination(), ctx.backupSettings(), false);
        return null;
    }

    @Override
    public Void visitRestoreQuery(RestoreQueryContext ctx) {
        backup(ctx.backupElements(), ctx.backupDestination(), ctx.backupSettings(), true);
        return null;
    }

    private void backup(BackupElementsContext elements, BackupDestinationContext destination, BackupSettingsContext settings, boolean restore) {
        SplitQueryType type = restore ? SplitQueryType.DATA_IMPORT : SplitQueryType.DATA_EXPORT;
        setType(type);
        Map<String, BehaviorObject> disks = new LinkedHashMap<>();
        BehaviorObject file = backupFile(destination, disks);
        List<BehaviorObject> selected = new ArrayList<>();
        for (BackupElementContext element : elements.backupElement()) {
            BehaviorObject owner;
            int index = restore && element.AS() != null ? 1 : 0;
            if (element.ALL() != null) {
                owner = objects.unnamedObject(TargetType.Schema, element.ALL().getSymbol(), UmiTypes.Catalog);
            } else if (element.DATABASE() != null) {
                IdentifierContext database = element.identifier(index);
                owner = objects.object(TargetType.Schema, database, List.of(name(database)));
            } else if (element.TEMPORARY() != null) {
                IdentifierContext table = element.identifier(index);
                owner = objects.object(TargetType.Table, table, List.of(name(table)));
            } else {
                TargetType target = TargetType.Table;
                if (element.DICTIONARY() != null) {
                    target = TargetType.Dictionary;
                } else if (element.VIEW() != null) {
                    target = TargetType.View;
                }
                owner = object(target, element.tableIdentifier(index));
            }
            if (element.backupPartition().isEmpty()) {
                selected.add(owner);
            } else {
                backupPartitions(element, owner, selected);
            }
        }
        if (!restore && settings != null) {
            for (BackupSettingContext setting : settings.backupSetting()) {
                if (setting.BASE_BACKUP() != null) {
                    selected.add(backupFile(setting.backupDestination(), disks));
                }
            }
        }
        if (restore) {
            for (BehaviorObject target : selected) {
                add(type, BehaviorAction.IMPORT, target, List.of(file));
            }
        } else {
            add(type, BehaviorAction.EXPORT, file, selected);
        }
        for (BehaviorObject disk : disks.values()) {
            add(type, BehaviorAction.READ, disk);
        }
        // Descriptors and backup options are stored values; partition expressions can execute calls.
        for (BackupElementContext element : elements.backupElement()) {
            for (BackupPartitionContext partition : element.backupPartition()) {
                if (partition.function != null) {
                    add(type, BehaviorAction.CALL, objects.object(TargetType.Function, partition.function, List.of(name(partition.function))));
                }
                visitChildren(partition);
            }
        }
    }

    private void backupPartitions(BackupElementContext element, BehaviorObject owner, List<BehaviorObject> result) {
        List<BackupPartitionContext> parts = element.backupPartition();
        boolean valuesOnly = parts.stream().noneMatch(part -> part.ID() != null);
        if (valuesOnly) {
            Token start;
            if (element.PARTITION() != null) {
                start = element.PARTITION().getSymbol();
            } else {
                start = element.PARTITIONS().getSymbol();
            }
            result.add(scoped(TargetType.Partition, start, parts.get(parts.size() - 1).getStop(), owner.getObjectPath()));
            return;
        }
        for (BackupPartitionContext part : parts) {
            if (part.ID() != null && part.stringLiteral() != null) {
                result.add(child(TargetType.Partition, part.stringLiteral(), owner, name(part.stringLiteral())));
            } else {
                result.add(child(TargetType.Partition, part, owner, ""));
            }
        }
    }

    private BehaviorObject backupFile(BackupDestinationContext ctx, Map<String, BehaviorObject> disks) {
        String engine = name(ctx.identifier());
        List<LiteralContext> args = ctx.literal();
        int fileIndex = 0;
        String prefix = "";
        if (engine.equalsIgnoreCase("Disk") && args.size() >= 2 && args.get(0).stringLiteral() != null) {
            StringLiteralContext diskName = args.get(0).stringLiteral();
            BehaviorObject disk = child(TargetType.Disk, diskName, null, name(diskName));
            disks.putIfAbsent(disk.getObjectPath(), disk);
            prefix = "disks/" + name(diskName) + "/";
            fileIndex = 1;
        }
        if (args.size() > fileIndex && args.get(fileIndex).stringLiteral() != null) {
            StringLiteralContext path = args.get(fileIndex).stringLiteral();
            return child(TargetType.File, path, null, prefix + trimSlashes(name(path)));
        }
        return child(TargetType.File, ctx, null, "");
    }
}
