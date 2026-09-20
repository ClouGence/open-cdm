/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

/** Native function properties shared by query execution and EXPLAIN dependency analysis. */
final class ChPlanningFunctions {
    private ChPlanningFunctions() {
    }

    static boolean readsSetting(String name) {
        return name.equals("getSetting") || name.equals("getSettingOrDefault");
    }

    static boolean foldsLambda(String name) {
        // FunctionArrayMapped permits constant folding; a closed lambda over literal arrays is evaluated.
        // Unknown higher-order functions and UDF definitions must not inherit this property.
        return name.equals("arrayMap");
    }
}
