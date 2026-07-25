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
package com.clougence.sql.mysql.analysis.reference;

import java.util.List;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

/**
 * Neutral object fact emitted directly from the MySQL parse tree.
 *
 * <p>Behavior analysis consumes this parser fact before console-side resource action conversion.
 * It carries no permission-check policy.</p>
 */
public record MySqlObjectReference(SecQueryType sqlType, TargetType targetType, boolean require, int startLine, int startColumn, int endLine, int endColumn, List<String> nodes) {

    public MySqlObjectReference{
        nodes = nodes == null ? List.of() : List.copyOf(nodes);
    }
}
