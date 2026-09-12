package com.clougence.sql.postgres.analysis.behavior;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

record PgFunctionBehavior(BehaviorAction action, TargetType targetType, BehaviorAction targetAction, boolean unsafe) {
    static final PgFunctionBehavior DEFAULT = new PgFunctionBehavior(BehaviorAction.CALL, null, null, false);
}
