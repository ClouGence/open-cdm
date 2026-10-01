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
package com.clougence.clouddm.ds.hana.language;

import java.io.StringReader;
import java.util.Set;
import java.util.stream.Stream;

import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.DiagnosticSeverity;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

public class HanaLanguageSpi implements DsLanguageSpi {

    private final MetaService                  metaService;
    private final HanaCompletionStrategyCenter completion = new HanaCompletionStrategyCenter();

    public HanaLanguageSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public Set<DsLanguageSupport> supports() {
        return Set.of(DsLanguageSupport.COMPLETE, DsLanguageSupport.VALIDATE, DsLanguageSupport.SPLIT);
    }

    private static <T extends LanguageResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }
        return result;
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = initResult(request, new CompletionResult());
        result.getItems().addAll(this.completion.complete(request, this.metaService));
        return result;
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = initResult(request, new ValidateResult());
        if (request == null || StringUtils.isBlank(request.getSqlText())) {
            return result;
        }
        Diagnostic diagnostic = new Diagnostic();
        int line = 1;
        int column = 0;
        try (StringReader reader = new StringReader(request.getSqlText());
             Stream<SplitScript> scripts = new HanaSplitAnalysisSpi().splitScriptStream(reader, null, 1, 0)) {
            if (scripts.count() == 0) {
                return result;
            }
            diagnostic.setSeverity(DiagnosticSeverity.INFO);
            diagnostic.setMessage("hana-sql-structural-validation");
        } catch (AntlerSyntaxException e) {
            line = e.getLine();
            String[] lines = request.getSqlText().split("\\n", -1);
            String lineText = lines[line - 1];
            column = lineText.offsetByCodePoints(0, Math.min(e.getColumn(), lineText.codePointCount(0, lineText.length())));
            diagnostic.setSeverity(DiagnosticSeverity.ERROR);
            diagnostic.setMessage(e.getMessage());
        }
        BlockLocation range = new BlockLocation();
        range.setStartPosition(new CodeLocation(line, column));
        range.setEndPosition(new CodeLocation(line, column + 1));
        diagnostic.setRange(range);
        result.getDiagnostics().add(diagnostic);
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = initResult(request, new SplitResult());
        if (request == null || StringUtils.isBlank(request.getSqlText())) {
            return result;
        }
        try (StringReader reader = new StringReader(request.getSqlText());
             Stream<SplitScript> scripts = new HanaSplitAnalysisSpi().splitScriptStream(reader, null, request.getBasicCodeLine(), request.getBasicCodeColumn())) {
            // Propagate syntax errors: an invalid block must not look like a successfully split empty script.
            for (SplitScript script : scripts.toList()) {
                BlockLocation range = new BlockLocation();
                range.setStartPosition(new CodeLocation(script.getBodyStartCodeLine(), script.getBodyStartCodeColumn()));
                range.setEndPosition(new CodeLocation(script.getBodyEndCodeLine(), script.getBodyEndCodeColumn()));
                SplitSqlStatement statement = new SplitSqlStatement();
                statement.setSql(script.getScript());
                statement.setRange(range);
                result.getStatements().add(statement);
            }
        }
        return result;
    }
}
