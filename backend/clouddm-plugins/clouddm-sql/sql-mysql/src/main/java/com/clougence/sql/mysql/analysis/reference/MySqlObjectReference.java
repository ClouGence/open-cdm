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

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

/**
 * Neutral object fact emitted directly from the MySQL parse tree.
 *
 * <p>Behavior analysis consumes this parser fact before console-side resource action conversion.
 * It carries no permission-check policy.</p>
 */
public record MySqlObjectReference(SplitQueryType sqlType, TargetType targetType, boolean require, int startLine, int startColumn, int endLine, int endColumn,
                                   List<String> nodes, BehaviorAction action, boolean explicitName) {

    public MySqlObjectReference{
        nodes = nodes == null ? List.of() : List.copyOf(nodes);
    }

    public MySqlObjectReference(SplitQueryType sqlType, TargetType targetType, boolean require, int startLine, int startColumn, int endLine, int endColumn,
                                List<String> nodes){
        this(sqlType, targetType, require, startLine, startColumn, endLine, endColumn, nodes, null, true);
    }

    public MySqlObjectReference(SplitQueryType sqlType, TargetType targetType, boolean require, int startLine, int startColumn, int endLine, int endColumn,
                                List<String> nodes, BehaviorAction action){
        this(sqlType, targetType, require, startLine, startColumn, endLine, endColumn, nodes, action, true);
    }
}
