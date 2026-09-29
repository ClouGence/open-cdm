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
package com.clougence.clouddm.ds.cockroachdb.sql.analysis;

import java.util.Set;

/** CockroachDB pg_catalog functions verified against the 24.3 function catalog. */
public final class CrdbSystemFunctions {
    private static final Set<String> FUNCTIONS = Set.of("unique_rowid", "unordered_unique_rowid", "cluster_logical_timestamp", "experimental_uuid_v4", "follower_read_timestamp");
    private CrdbSystemFunctions() {}
    public static boolean contains(String name) { return FUNCTIONS.contains(name); }
}
