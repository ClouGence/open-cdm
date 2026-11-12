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

package com.clougence.sql.greptimedb;

import static com.clougence.sql.greptimedb.antlr.GreptimeSqlParser.*;

import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlBaseVisitor;

final class GreptimeSplitVisitor extends GreptimeSqlBaseVisitor<SplitQueryType> {

    static final GreptimeSplitVisitor                 INSTANCE = new GreptimeSplitVisitor();

    private static final Map<Integer, SplitQueryType> TYPES    = Map
        .ofEntries(Map.entry(RULE_query, SplitQueryType.SELECT), Map.entry(RULE_insertStatement, SplitQueryType.INSERT), Map.entry(RULE_deleteStatement, SplitQueryType.DELETE), Map
            .entry(RULE_createDatabase, SplitQueryType.CREATE_SCHEMA), Map.entry(RULE_alterDatabase, SplitQueryType.ALTER_SCHEMA), Map
                .entry(RULE_dropDatabase, SplitQueryType.DROP_SCHEMA), Map.entry(RULE_createTable, SplitQueryType.CREATE_TABLE), Map
                    .entry(RULE_alterTable, SplitQueryType.ALTER_TABLE), Map.entry(RULE_dropTable, SplitQueryType.DROP_TABLE), Map
                        .entry(RULE_truncateTable, SplitQueryType.TRUNCATE_TABLE), Map.entry(RULE_createView, SplitQueryType.CREATE_VIEW), Map
                            .entry(RULE_dropView, SplitQueryType.DROP_VIEW), Map.entry(RULE_useDatabase, SplitQueryType.SWITCH_SCHEMA), Map
                                .entry(RULE_showStatement, SplitQueryType.METADATA), Map.entry(RULE_describeStatement, SplitQueryType.METADATA));

    private GreptimeSplitVisitor(){
    }

    @Override
    public SplitQueryType visitStatement(StatementContext statement) {
        return TYPES.getOrDefault(statement.getChild(ParserRuleContext.class, 0).getRuleIndex(), SplitQueryType.UNKNOWN);
    }
}
