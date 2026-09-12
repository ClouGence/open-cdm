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
package com.clougence.clouddm.ds.tidb.sql.parser;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiSysObjectRegistrySpi;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;

/** Classifies physical table references while respecting CTE visibility. */
public final class TiQueryAnalysis extends TiDBParserBaseVisitor<Boolean> {
    private static final TiSysObjectRegistrySpi SYSTEM_OBJECTS = new TiSysObjectRegistrySpi();

    private boolean metadataTableFound;

    private TiQueryAnalysis(){
    }

    public static boolean isMetadataOnly(ParseTree tree) {
        TiQueryAnalysis visitor = new TiQueryAnalysis();
        return visitor.visit(tree) && visitor.metadataTableFound;
    }

    @Override
    protected Boolean defaultResult() {
        return true;
    }

    @Override
    protected Boolean aggregateResult(Boolean aggregate, Boolean next) {
        return aggregate && next;
    }

    @Override
    public Boolean visitTableName(TableNameContext ctx) {
        if (isCte(ctx)) {
            return true;
        }
        boolean metadata = isMetadataTable(ctx);
        metadataTableFound |= metadata;
        return metadata;
    }

    public static boolean isCte(TableNameContext table) {
        if (table.fullId().uid().size() != 1) {
            return false;
        }
        String name = identifierName(table.fullId().uid(0));
        Set<ParseTree> ancestors = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            ancestors.add(parent);
        }
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            WithClauseContext clause = null;
            if (parent instanceof WithSelectStatementContext with) {
                clause = with.withClause();
            } else if (parent instanceof SingleUpdateStatementContext update) {
                clause = update.withClause();
            } else if (parent instanceof MultipleUpdateStatementContext update) {
                clause = update.withClause();
            } else if (parent instanceof SingleDeleteStatementContext delete) {
                clause = delete.withClause();
            } else if (parent instanceof MultipleDeleteStatementContext delete) {
                clause = delete.withClause();
            }
            if (clause != null) {
                for (WithSelectExprContext cte : clause.withSelectExpr()) {
                    boolean current = ancestors.contains(cte);
                    if (current && clause.RECURSIVE() == null) {
                        break;
                    }
                    if (name.equalsIgnoreCase(identifierName(cte.uid()))) {
                        return true;
                    }
                    if (current) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    private static String identifierName(UidContext context) {
        String name = context.getText();
        if (name.startsWith("`") && name.endsWith("`")) {
            return name.substring(1, name.length() - 1).replace("``", "`");
        }
        return name;
    }

    public static boolean isMetadataTable(TableNameContext table) {
        if (table.fullId().uid().size() != 2) {
            return false;
        }
        String schema = identifierName(table.fullId().uid(0));
        String name = identifierName(table.fullId().uid(1));
        return SYSTEM_OBJECTS.isMetadataTable(schema, name);
    }
}
