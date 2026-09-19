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

/** System commands manage runtime resources; definition values are not executed SQL. */
class ChSystemBehaviorVisitor extends ChStatementBehaviorVisitor {
    ChSystemBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
    }

    @Override
    public Void visitSystemConfigurationStmt(SystemConfigurationStmtContext ctx) {
        Token key = ctx.CONFIG() == null ? ctx.USERS().getSymbol() : ctx.CONFIG().getSymbol();
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, instance(TargetType.ConfigKey, key, key, ""));
        return null;
    }

    @Override
    public Void visitSystemReloadDictionariesStmt(SystemReloadDictionariesStmtContext ctx) {
        Token start = ctx.DICTIONARIES().getSymbol();
        if (ctx.EMBEDDED() != null) {
            start = ctx.EMBEDDED().getSymbol();
        }
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, instance(TargetType.Dictionary, start, ctx.DICTIONARIES().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemUnloadDictionariesStmt(SystemUnloadDictionariesStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.PURGE, instance(TargetType.Dictionary, ctx.DICTIONARIES().getSymbol(), ctx.DICTIONARIES().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemReloadDictionaryStmt(SystemReloadDictionaryStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.REFRESH, dictionary(ctx.systemDictionaryTarget()));
        return null;
    }

    @Override
    public Void visitSystemUnloadDictionaryStmt(SystemUnloadDictionaryStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.PURGE, dictionary(ctx.systemDictionaryTarget()));
        return null;
    }

    private BehaviorObject dictionary(SystemDictionaryTargetContext ctx) {
        if (ctx.tableIdentifier() != null) {
            TableIdentifierContext table = ctx.tableIdentifier();
            List<String> names = new ArrayList<>();
            if (table.databaseIdentifier() != null) {
                names.add(name(table.databaseIdentifier().identifier()));
            }
            names.add(name(table.identifier()));
            return objects.object(TargetType.Dictionary, table, names);
        }
        // The string form is a qualified dictionary name, including quoted components.
        String value = name(ctx.stringLiteral());
        List<String> names = new ArrayList<>();
        StringBuilder part = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (quote != 0) {
                if (ch == '\\' && i + 1 < value.length()) {
                    part.append(value.charAt(++i));
                } else if (ch == quote) {
                    if (i + 1 < value.length() && value.charAt(i + 1) == quote) {
                        part.append(ch);
                        i++;
                    } else {
                        quote = 0;
                    }
                } else {
                    part.append(ch);
                }
            } else if (ch == '`' || ch == '"') {
                quote = ch;
            } else if (ch == '.') {
                names.add(part.toString());
                part.setLength(0);
            } else {
                part.append(ch);
            }
        }
        names.add(part.toString());
        return objects.object(TargetType.Dictionary, ctx.stringLiteral(), names);
    }

    @Override
    public Void visitSystemReloadModelsStmt(SystemReloadModelsStmtContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, instance(TargetType.Cache, ctx.MODELS().getSymbol(), ctx.MODELS().getSymbol(), "model"));
        return null;
    }

    @Override
    public Void visitSystemReloadModelStmt(SystemReloadModelStmtContext ctx) {
        ParserRuleContext target = namedTarget(ctx.systemNamedTarget());
        String model = name(target);
        while (model.startsWith("/")) {
            model = model.substring(1);
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, instance(TargetType.Cache, target, "model/" + model));
        return null;
    }

    @Override
    public Void visitSystemReloadFunctionsStmt(SystemReloadFunctionsStmtContext ctx) {
        reloadFunction(instance(TargetType.Function, ctx.FUNCTIONS().getSymbol(), ctx.FUNCTIONS().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemReloadFunctionStmt(SystemReloadFunctionStmtContext ctx) {
        ParserRuleContext target = namedTarget(ctx.systemNamedTarget());
        reloadFunction(instance(TargetType.Function, target, name(target)));
        return null;
    }

    private void reloadFunction(BehaviorObject function) {
        add(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.REFRESH, function);
        // Executable function definitions load external command/shell configuration.
        add(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.UNSAFE, function);
    }

    @Override
    public Void visitSystemCacheStmt(SystemCacheStmtContext statement) {
        SystemCacheCommandContext ctx = statement.systemCacheCommand();
        BehaviorObject cache;
        if (ctx.systemSimpleCache() != null) {
            SystemSimpleCacheContext kind = ctx.systemSimpleCache();
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < kind.getChildCount() - 1; i++) {
                if (!key.isEmpty()) {
                    key.append('_');
                }
                key.append(kind.getChild(i).getText().toLowerCase(Locale.ROOT));
            }
            cache = instance(TargetType.Cache, kind, key.toString());
        } else if (ctx.FILESYSTEM() != null) {
            if (ctx.stringLiteral() != null) {
                cache = instance(TargetType.Cache, ctx.stringLiteral(), "filesystem/" + name(ctx.stringLiteral()));
            } else {
                cache = instance(TargetType.Cache, ctx.FILESYSTEM().getSymbol(), ctx.CACHE().getSymbol(), "filesystem");
            }
        } else if (ctx.DISK() != null) {
            ParserRuleContext target = namedTarget(ctx.systemNamedTarget());
            cache = instance(TargetType.Cache, target, "disk_metadata/" + name(target));
        } else if (ctx.SCHEMA() != null) {
            String key = "schema";
            Token start = ctx.SCHEMA().getSymbol();
            Token stop = ctx.CACHE().getSymbol();
            if (ctx.FORMAT() != null) {
                key = "format_schema";
                start = ctx.FORMAT().getSymbol();
            }
            if (ctx.FOR() != null) {
                stop = ctx.getStop();
                key += "/" + stop.getText().toLowerCase(Locale.ROOT);
            }
            cache = instance(TargetType.Cache, start, stop, key);
        } else {
            cache = instance(TargetType.Cache, ctx.QUERY_SQL().getSymbol(), ctx.CACHE().getSymbol(), "query");
        }
        BehaviorAction action = BehaviorAction.PURGE;
        if (ctx.SYNC() != null) {
            action = BehaviorAction.REPAIR;
        }
        add(SplitQueryType.ADMIN_PERFORMANCE, action, cache);
        return null;
    }

    @Override
    public Void visitSystemReloadMetricsStmt(SystemReloadMetricsStmtContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.REFRESH, instance(TargetType.Instance, ctx.ASYNCHRONOUS().getSymbol(), ctx.METRICS().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemJemallocPurgeStmt(SystemJemallocPurgeStmtContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE, instance(TargetType.Instance, ctx.JEMALLOC().getSymbol(), ctx.JEMALLOC().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemFlushLogsStmt(SystemFlushLogsStmtContext ctx) {
        if (ctx.tableIdentifier().isEmpty()) {
            BehaviorObject log = objects.object(TargetType.Schema, ctx.LOGS().getSymbol(), List.of("system"));
            log.setObjectType(TargetType.Log);
            add(SplitQueryType.MAINTAIN_LOG, BehaviorAction.FLUSH, log);
        } else {
            for (TableIdentifierContext target : ctx.tableIdentifier()) {
                add(SplitQueryType.MAINTAIN_LOG, BehaviorAction.FLUSH, objects.object(TargetType.Log, target, List.of("system", name(target.identifier()))));
            }
        }
        return null;
    }

    @Override
    public Void visitSystemListenStmt(SystemListenStmtContext ctx) {
        BehaviorAction action = BehaviorAction.START;
        if (ctx.STOP() != null) {
            action = BehaviorAction.STOP;
        }
        SystemListenTargetContext target = ctx.systemListenTarget();
        if (target.QUERIES() == null) {
            add(SplitQueryType.SYSTEM_SETTING_WRITE, action, endpoint(target.systemListenProtocol(0)));
        } else {
            Token stop = ((TerminalNode) target.getChild(1)).getSymbol();
            add(SplitQueryType.SYSTEM_SETTING_WRITE, action, instance(TargetType.Endpoint, target.QUERIES().getSymbol(), stop, ""));
            for (SystemListenProtocolContext excluded : target.systemListenProtocol()) {
                add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.READ, endpoint(excluded));
            }
        }
        return null;
    }

    private BehaviorObject endpoint(SystemListenProtocolContext ctx) {
        if (ctx.CUSTOM() != null) {
            return instance(TargetType.Endpoint, ctx.stringLiteral(), "protocols/" + name(ctx.stringLiteral()));
        }
        String key = switch (ctx.getText().toUpperCase(Locale.ROOT)) {
            case "TCP" -> "tcp_port";
            case "TCPSSH" -> "tcp_ssh_port";
            case "TCPWITHPROXY" -> "tcp_with_proxy_port";
            case "TCPSECURE" -> "tcp_port_secure";
            case "HTTP" -> "http_port";
            case "HTTPS" -> "https_port";
            case "MYSQL" -> "mysql_port";
            case "GRPC" -> "grpc_port";
            case "POSTGRESQL" -> "postgresql_port";
            case "PROMETHEUS" -> "prometheus.port";
            case "INTERSERVERHTTP" -> "interserver_http_port";
            case "INTERSERVERHTTPS" -> "interserver_https_port";
            case "ARROWFLIGHT" -> "arrowflight_port";
            default -> throw new IllegalArgumentException("Unsupported listener protocol: " + ctx.getText());
        };
        return instance(TargetType.Endpoint, ctx, key);
    }

    @Override
    public Void visitSystemShutdownStmt(SystemShutdownStmtContext ctx) {
        Token operation = ctx.SHUTDOWN() == null ? ctx.KILL().getSymbol() : ctx.SHUTDOWN().getSymbol();
        BehaviorAction action = ctx.SHUTDOWN() == null ? BehaviorAction.TERMINATE : BehaviorAction.STOP;
        add(SplitQueryType.ADMIN, action, instance(TargetType.Instance, operation, operation, ""));
        return null;
    }

    @Override
    public Void visitSystemSuspendStmt(SystemSuspendStmtContext ctx) {
        add(SplitQueryType.ADMIN, BehaviorAction.STOP, instance(TargetType.Instance, ctx.SUSPEND().getSymbol(), ctx.SUSPEND().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitSystemRestartDiskStmt(SystemRestartDiskStmtContext ctx) {
        ParserRuleContext target = namedTarget(ctx.systemNamedTarget());
        add(SplitQueryType.ADMIN, BehaviorAction.RESET, instance(TargetType.Disk, target, name(target)));
        return null;
    }

    @Override
    public Void visitKillQueryStmt(KillQueryStmtContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN;
        BehaviorAction action = BehaviorAction.TERMINATE;
        if (ctx.TEST() != null) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        }
        List<StringLiteralContext> ids = queryIds(ctx.killWhereClause().columnExpr());
        if (ids.isEmpty()) {
            add(type, action, instance(TargetType.Query, ctx.QUERY_SQL().getSymbol(), ctx.QUERY_SQL().getSymbol(), ""));
        } else {
            Map<String, StringLiteralContext> unique = new LinkedHashMap<>();
            for (StringLiteralContext id : ids) {
                unique.putIfAbsent(name(id), id);
            }
            for (var entry : unique.entrySet()) {
                add(type, action, instance(TargetType.Query, entry.getValue(), entry.getKey()));
            }
        }
        // Set the owning type before visiting real calls, parameters or subqueries in WHERE.
        visit(ctx.killWhereClause());
        return null;
    }

    private List<StringLiteralContext> queryIds(ColumnExprContext expression) {
        while (expression instanceof ColumnExprParensContext parens) {
            expression = parens.columnExpr();
        }
        if (!(expression instanceof ColumnExprPrecedence3Context comparison) || comparison.NOT() != null || comparison.GLOBAL() != null) {
            return List.of();
        }
        ColumnExprContext left = comparison.columnExpr(0);
        if (!(left instanceof ColumnExprIdentifierContext column) || column.columnIdentifier().tableIdentifier() != null) {
            return List.of();
        }
        NestedIdentifierContext identifier = column.columnIdentifier().nestedIdentifier();
        if (identifier.identifier().size() != 1 || !name(identifier.identifier(0)).equals("query_id")) {
            return List.of();
        }
        ColumnExprContext right = comparison.columnExpr(1);
        if (comparison.EQ_SINGLE() != null || comparison.EQ_DOUBLE() != null) {
            if (right instanceof ColumnExprLiteralContext literal && literal.literal().stringLiteral() != null) {
                return List.of(literal.literal().stringLiteral());
            }
        } else if (comparison.IN() != null) {
            List<ColumnExprContext> values = new ArrayList<>();
            if (right instanceof ColumnExprTupleContext tuple && tuple.columnExprList() != null) {
                for (ColumnsExprContext item : tuple.columnExprList().columnsExpr()) {
                    if (!(item instanceof ColumnsExprColumnContext columnItem)) {
                        return List.of();
                    }
                    values.add(columnItem.columnExpr());
                }
            } else if (right instanceof ColumnExprParensContext parens) {
                values.add(parens.columnExpr());
            }
            List<StringLiteralContext> result = new ArrayList<>();
            for (ColumnExprContext value : values) {
                if (!(value instanceof ColumnExprLiteralContext literal) || literal.literal().stringLiteral() == null) {
                    return List.of();
                }
                result.add(literal.literal().stringLiteral());
            }
            return result;
        }
        return List.of();
    }

    @Override
    public Void visitShowProcessListStmt(ShowProcessListStmtContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, instance(TargetType.Query, ctx.PROCESSLIST().getSymbol(), ctx.PROCESSLIST().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitDescribeCacheStmt(DescribeCacheStmtContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, instance(TargetType.Cache, ctx.stringLiteral(), "filesystem/" + name(ctx.stringLiteral())));
        return null;
    }

    @Override
    public Void visitShowFilesystemCaches(ShowFilesystemCachesContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, instance(TargetType.Cache, ctx.FILESYSTEM().getSymbol(), ctx.CACHES().getSymbol(), "filesystem"));
        return null;
    }

    @Override
    public Void visitShowClusterStmt(ShowClusterStmtContext ctx) {
        ParserRuleContext target = ctx.identifier();
        if (target == null) {
            target = ctx.stringLiteral();
        }
        String cluster = name(target);
        if (cluster.contains("{")) {
            cluster = "";
        }
        add(SplitQueryType.METADATA, BehaviorAction.READ, instance(TargetType.Cluster, target, cluster));
        return null;
    }

    @Override
    public Void visitShowClustersStmt(ShowClustersStmtContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, instance(TargetType.Cluster, ctx.CLUSTERS().getSymbol(), ctx.CLUSTERS().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitShowEnginesStmt(ShowEnginesStmtContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, instance(TargetType.TableEngine, ctx.ENGINES().getSymbol(), ctx.ENGINES().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitShowFunctionsStmt(ShowFunctionsStmtContext ctx) {
        add(SplitQueryType.METADATA, BehaviorAction.READ, instance(TargetType.Function, ctx.FUNCTIONS().getSymbol(), ctx.FUNCTIONS().getSymbol(), ""));
        return null;
    }

    @Override
    public Void visitCreateNamedCollectionStmt(CreateNamedCollectionStmtContext ctx) {
        BehaviorObject collection = instance(TargetType.NamedCollection, ctx.identifier(), name(ctx.identifier()));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CREATE, collection);
        collectionSettings(collection, ctx.namedCollectionSettings());
        return null;
    }

    @Override
    public Void visitAlterNamedCollectionStmt(AlterNamedCollectionStmtContext ctx) {
        BehaviorObject collection = instance(TargetType.NamedCollection, ctx.identifier(0), name(ctx.identifier(0)));
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.ALTER, collection);
        collectionSettings(collection, ctx.namedCollectionSettings());
        for (int i = 1; i < ctx.identifier().size(); i++) {
            configure(SplitQueryType.SYSTEM_SETTING_WRITE, collection, ctx.identifier(i), "");
        }
        return null;
    }

    private void collectionSettings(BehaviorObject collection, NamedCollectionSettingsContext settings) {
        if (settings != null) {
            for (NamedCollectionSettingContext setting : settings.namedCollectionSetting()) {
                configure(SplitQueryType.SYSTEM_SETTING_WRITE, collection, setting.identifier(), "");
            }
        }
    }

    @Override
    public Void visitDropNamedCollectionStmt(DropNamedCollectionStmtContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instance(TargetType.NamedCollection, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitCreateResourceStmt(CreateResourceStmtContext ctx) {
        Map<String, BehaviorObject> disks = new LinkedHashMap<>();
        for (ResourceDiskOperationContext operation : ctx.resourceOperations().resourceDiskOperation()) {
            BehaviorObject disk;
            if (operation.ANY() != null) {
                disk = instance(TargetType.Disk, operation.ANY().getSymbol(), operation.DISK().getSymbol(), "");
            } else {
                disk = instance(TargetType.Disk, operation.identifier(), name(operation.identifier()));
            }
            disks.putIfAbsent(disk.getObjectPath(), disk);
        }
        BehaviorAction action = ctx.REPLACE() == null ? BehaviorAction.CREATE : BehaviorAction.REPLACE;
        add(SplitQueryType.SYSTEM_SETTING_WRITE, action, instance(TargetType.Resource, ctx.identifier(), name(ctx.identifier())), new ArrayList<>(disks.values()));
        return null;
    }

    @Override
    public Void visitDropResourceStmt(DropResourceStmtContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.DROP, instance(TargetType.Resource, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    @Override
    public Void visitCreateWorkloadStmt(CreateWorkloadStmtContext ctx) {
        BehaviorObject workload = instance(TargetType.ResourceGroup, ctx.identifier(0), name(ctx.identifier(0)));
        Map<String, BehaviorObject> dependencies = new LinkedHashMap<>();
        if (ctx.IN() != null) {
            BehaviorObject parent = instance(TargetType.ResourceGroup, ctx.identifier(1), name(ctx.identifier(1)));
            dependencies.put("parent", parent);
        }
        for (WorkloadSettingContext setting : ctx.workloadSetting()) {
            if (setting.FOR() != null) {
                BehaviorObject resource = instance(TargetType.Resource, setting.identifier(1), name(setting.identifier(1)));
                dependencies.putIfAbsent(resource.getObjectPath(), resource);
            }
        }
        BehaviorAction action = ctx.REPLACE() == null ? BehaviorAction.CREATE : BehaviorAction.REPLACE;
        add(SplitQueryType.CREATE_RESOURCE_GROUP, action, workload, new ArrayList<>(dependencies.values()));
        for (WorkloadSettingContext setting : ctx.workloadSetting()) {
            String scope = "";
            if (setting.FOR() != null) {
                scope = name(setting.identifier(1)) + "/";
            }
            configure(SplitQueryType.CREATE_RESOURCE_GROUP, workload, setting.identifier(0), scope);
        }
        return null;
    }

    @Override
    public Void visitDropWorkloadStmt(DropWorkloadStmtContext ctx) {
        add(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP, instance(TargetType.ResourceGroup, ctx.identifier(), name(ctx.identifier())));
        return null;
    }

    private void configure(SplitQueryType type, BehaviorObject owner, IdentifierContext context, String scope) {
        String keyName = name(context);
        BehaviorObject key = objects.childObject(TargetType.ConfigKey, context, owner, scope + keyName);
        String ownerName = instanceRelativeName(owner);
        if (!scope.isEmpty()) {
            ownerName += "/" + scope.substring(0, scope.length() - 1);
        }
        key.setObjectName(new ObjectName(ownerName, null, keyName));
        add(type, BehaviorAction.CONFIGURE, key);
    }

    private ParserRuleContext namedTarget(SystemNamedTargetContext ctx) {
        if (ctx.identifier() != null) {
            return ctx.identifier();
        }
        return ctx.stringLiteral();
    }

    private BehaviorObject instance(TargetType type, ParserRuleContext context, String value) {
        return instance(type, context.getStart(), context.getStop(), value);
    }

    private BehaviorObject instance(TargetType type, Token start, Token stop, String value) {
        BehaviorObject object = objects.instanceObject(type, start, stop, value);
        if (value.isEmpty()) {
            object.setObjectPath(object.getObjectPath().substring(0, object.getObjectPath().length() - 1));
        }
        // Cache families and listener configuration paths are not database ObjectName identities.
        if (type != TargetType.Cache && type != TargetType.Endpoint) {
            String objectName = value.isEmpty() ? null : value;
            object.setObjectName(new ObjectName(null, null, objectName));
        }
        return object;
    }
}
