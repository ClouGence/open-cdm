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

import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.BlockStatementContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.CreateFunctionContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.CreateProcedureContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.DeclareVariableContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.FunctionParameterContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.ProcedureParameterContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.UidContext;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.VariableClauseContext;

/** Resolves routine parameters and locally declared variables. */
public final class TiRoutineAnalysis {

    private TiRoutineAnalysis(){
    }

    public static boolean isRoutineVariable(VariableClauseContext variable) {
        if (variable.uid() == null || variable.getChildCount() != 1) {
            return false;
        }
        String name = identifierName(variable.uid());
        for (ParseTree scope = variable.getParent(); scope != null; scope = scope.getParent()) {
            if (scope instanceof BlockStatementContext block) {
                for (DeclareVariableContext declaration : block.declareVariable()) {
                    for (UidContext local : declaration.uidList().uid()) {
                        if (name.equalsIgnoreCase(identifierName(local))) {
                            return true;
                        }
                    }
                }
            } else if (scope instanceof CreateProcedureContext procedure) {
                for (ProcedureParameterContext parameter : procedure.procedureParameter()) {
                    if (name.equalsIgnoreCase(identifierName(parameter.uid()))) {
                        return true;
                    }
                }
            } else if (scope instanceof CreateFunctionContext function) {
                for (FunctionParameterContext parameter : function.functionParameter()) {
                    if (name.equalsIgnoreCase(identifierName(parameter.uid()))) {
                        return true;
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
}
