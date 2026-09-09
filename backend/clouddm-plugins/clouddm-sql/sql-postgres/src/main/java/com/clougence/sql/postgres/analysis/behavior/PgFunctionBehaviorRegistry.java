/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.postgres.analysis.behavior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

final class PgFunctionBehaviorRegistry {

    static final PgFunctionBehaviorRegistry       INSTANCE  = new PgFunctionBehaviorRegistry();
    private final Map<String, PgFunctionBehavior> functions = new HashMap<>();

    private PgFunctionBehaviorRegistry(){
        registerFunctionBehaviors();
    }

    private void registerFunctionBehaviors() {
        // Function behavior is shared across PostgreSQL versions.
        // References: PostgreSQL 18 System Administration Functions, verified 2026-09-08.
        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-ADMIN-SET
        register(new PgFunctionBehavior(BehaviorAction.CONFIGURE, TargetType.ConfigKey, BehaviorAction.CONFIGURE, false), "set_config");
        register(new PgFunctionBehavior(BehaviorAction.CONFIGURE, null, null, false), "pg_reload_conf");
        register(new PgFunctionBehavior(BehaviorAction.CALL, TargetType.ConfigKey, BehaviorAction.READ, false), "current_setting");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-ADMIN-SIGNAL
        register(new PgFunctionBehavior(BehaviorAction.TERMINATE, null, null, false), "pg_cancel_backend", "pg_terminate_backend");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-REPLICATION
        register(new PgFunctionBehavior(BehaviorAction.CREATE,
            TargetType.Replication,
            BehaviorAction.CREATE,
            false), "pg_create_physical_replication_slot", "pg_create_logical_replication_slot", "pg_replication_origin_create");
        register(new PgFunctionBehavior(BehaviorAction.DROP, TargetType.Replication, BehaviorAction.DROP, false), "pg_drop_replication_slot", "pg_replication_origin_drop");
        register(new PgFunctionBehavior(BehaviorAction.ALTER,
            TargetType.Replication,
            BehaviorAction.ALTER,
            false), "pg_replication_origin_advance", "pg_replication_origin_session_setup", "pg_replication_slot_advance");
        register(new PgFunctionBehavior(BehaviorAction.RESET, null, null, false), "pg_replication_origin_session_reset");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-RECOVERY-CONTROL
        register(new PgFunctionBehavior(BehaviorAction.SWITCH, null, null, false), "pg_promote");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-BACKUP
        register(new PgFunctionBehavior(BehaviorAction.START, null, null, false), "pg_backup_start");
        register(new PgFunctionBehavior(BehaviorAction.STOP, null, null, false), "pg_backup_stop");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-ADVISORY-LOCKS
        register(new PgFunctionBehavior(BehaviorAction.LOCK,
            null,
            null,
            false), "pg_advisory_lock", "pg_advisory_xact_lock", "pg_advisory_lock_shared", "pg_advisory_xact_lock_shared", "pg_try_advisory_lock", "pg_try_advisory_xact_lock", "pg_try_advisory_lock_shared", "pg_try_advisory_xact_lock_shared");
        register(new PgFunctionBehavior(BehaviorAction.UNLOCK, null, null, false), "pg_advisory_unlock", "pg_advisory_unlock_shared", "pg_advisory_unlock_all");

        // https://www.postgresql.org/docs/18/functions-admin.html#FUNCTIONS-ADMIN-GENFILE
        register(new PgFunctionBehavior(BehaviorAction.CALL, TargetType.File, BehaviorAction.READ, true), "pg_read_file", "pg_read_binary_file", "pg_ls_dir", "pg_stat_file");
    }

    private void register(PgFunctionBehavior behavior, String... names) {
        for (String name : names) {
            functions.put(name, behavior);
        }
    }

    PgFunctionBehavior behavior(List<String> names) {
        if (names.size() > 2 || names.size() == 2 && !"pg_catalog".equals(names.get(0))) {
            return PgFunctionBehavior.DEFAULT;
        }
        String functionName = names.get(names.size() - 1);
        return functions.getOrDefault(functionName, PgFunctionBehavior.DEFAULT);
    }

}
