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
package com.clougence.clouddm.ds.oceanbase.language.ob4ora;

import com.clougence.clouddm.ds.oceanbase.parser.ob4ora.ObOraDslProvider;
import com.clougence.clouddm.dsfamily.language.split.SplitStrategyCenter;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class ObOraLanguageSpi implements DsLanguageSpi {
    private final MetaService                   metaService;
    private final ObOraCompletionStrategyCenter completion = new ObOraCompletionStrategyCenter();
    private final ObOraValidateStrategyCenter   validate   = new ObOraValidateStrategyCenter();
    private final SplitStrategyCenter       split = new SplitStrategyCenter();

    public ObOraLanguageSpi(MetaService metaService){
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
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = initResult(request, new CompletionResult());
        result.getItems().addAll(this.completion.complete(request, this.metaService));
        return result;
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = initResult(request, new ValidateResult());
        result.getDiagnostics().addAll(this.validate.validate(request, this.metaService));
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        return this.split.split(request, ObOraDslProvider.INSTANCE);
    }

}
