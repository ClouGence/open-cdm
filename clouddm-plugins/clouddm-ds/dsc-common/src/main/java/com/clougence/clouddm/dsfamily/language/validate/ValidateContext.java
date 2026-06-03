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
package com.clougence.clouddm.dsfamily.language.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class ValidateContext {

    private final ValidateRequest              request;
    private final DslProvider                  dslProvider;
    private final String                       sqlText;
    private final List<ValidateStatementState> statementStates;
    private final AntlerSyntaxException        syntaxError;

    private ValidateContext(ValidateRequest request, DslProvider dslProvider, List<ValidateStatementState> statementStates, AntlerSyntaxException syntaxError){
        this.request = request;
        this.dslProvider = dslProvider;
        this.sqlText = StringUtils.toString(request.getSqlText());
        this.statementStates = List.copyOf(statementStates);
        this.syntaxError = syntaxError;
    }

    public static ValidateContext resolve(ValidateRequest request, DslProvider dslProvider) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(dslProvider, "dslProvider");

        String sqlText = StringUtils.toString(request.getSqlText());
        if (StringUtils.isBlank(sqlText)) {
            return new ValidateContext(request, dslProvider, List.of(), null);
        }

        try {
            List<ValidateStatementState> statementStates = new ArrayList<>();
            for (AstSplitScript splitScript : DslHelper.splitDsl(dslProvider, sqlText, new CodeLocation(1, 0))) {
                statementStates.add(new ValidateStatementState(splitScript));
            }
            return new ValidateContext(request, dslProvider, statementStates, null);
        } catch (AntlerSyntaxException e) {
            return new ValidateContext(request, dslProvider, List.of(), e);
        }
    }

    public boolean hasSyntaxError() {
        return this.syntaxError != null;
    }
}
