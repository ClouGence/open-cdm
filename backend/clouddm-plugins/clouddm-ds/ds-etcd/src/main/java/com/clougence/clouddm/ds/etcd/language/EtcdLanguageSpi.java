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
package com.clougence.clouddm.ds.etcd.language;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.ds.etcd.command.EtcdCommandParser;
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
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;

public class EtcdLanguageSpi implements DsLanguageSpi {

    @Override
    public Set<DsLanguageSupport> supports() {
        return Set.of(DsLanguageSupport.SPLIT);
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        return init(request, new CompletionResult());
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        return init(request, new ValidateResult());
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = init(request, new SplitResult());
        List<SplitScript> scripts = EtcdCommandParser.split(new StringReader(request.getSqlText()), List.of(), request.getBasicCodeLine(), request.getBasicCodeColumn());
        for (SplitScript script : scripts) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(script.getScript());
            BlockLocation range = new BlockLocation();
            range.setStartPosition(new CodeLocation(script.getBodyStartCodeLine(), script.getBodyStartCodeColumn()));
            range.setEndPosition(new CodeLocation(script.getBodyEndCodeLine(), script.getBodyEndCodeColumn()));
            statement.setRange(range);
            result.getStatements().add(statement);
        }
        return result;
    }

    private static <T extends LanguageResult> T init(AbstractRequest request, T result) {
        result.setRequestId(request.getRequestId());
        result.setRequestVersion(request.getRequestVersion());
        return result;
    }
}
