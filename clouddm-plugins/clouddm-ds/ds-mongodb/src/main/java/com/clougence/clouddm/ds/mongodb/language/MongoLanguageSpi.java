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
package com.clougence.clouddm.ds.mongodb.language;

import java.util.List;
import java.util.Set;

import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
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
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class MongoLanguageSpi implements DsLanguageSpi {
    private final MetaService                 metaService;
    private final MongoValidateStrategyCenter validate = new MongoValidateStrategyCenter();

    public MongoLanguageSpi(MetaService metaService){
        this.metaService = metaService;
    }

    private static <T extends LanguageResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }
        return result;
    }

    @Override
    public Set<DsLanguageSupport> supports() {
        return Set.of(DsLanguageSupport.VALIDATE, DsLanguageSupport.SPLIT);
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        return initResult(request, new CompletionResult());
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = initResult(request, new ValidateResult());
        result.getDiagnostics().addAll(this.validate.validate(request, this.metaService));
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        String sqlText = request.getSqlText();
        if (StringUtils.isBlank(sqlText)) {
            return initResult(request, new SplitResult());
        }

        SplitResult result = initResult(request, new SplitResult());
        CodeLocation location = new CodeLocation(request.getBasicCodeLine(), request.getBasicCodeColumn());
        List<AstSplitScript> scripts = DslHelper.splitDsl(MongoDslProvider.INSTANCE, sqlText, location);
        for (AstSplitScript ss : scripts) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(ss.getScript());
            statement.setRange(ss.toLocation());
            result.getStatements().add(statement);
        }
        return result;
    }

}
