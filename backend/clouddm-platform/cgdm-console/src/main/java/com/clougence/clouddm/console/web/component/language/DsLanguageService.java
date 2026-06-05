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
package com.clougence.clouddm.console.web.component.language;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.CollectionUtils;

@Service
public class DsLanguageService {

    private DsLanguageSpi findSpi(AbstractRequest request) {
        DataSourceType dsType = DataSourceType.getTypeByName(request.getDsType());
        if (dsType == null) {
            return null;
        }

        DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
        if (dsPlugin == null) {
            return null;
        }

        List<DsLanguageSpi> languageSpis = dsPlugin.findSpi(DsLanguageSpi.class);
        if (CollectionUtils.isEmpty(languageSpis)) {
            return null;
        }

        return languageSpis.get(0);
    }

    private <T extends LanguageResult> T invoke(String ownerUid, AbstractRequest request, Supplier<T> emptyResult, Function<DsLanguageSpi, T> action) {
        T degraded = emptyResult.get();
        if (request != null) {
            degraded.setRequestId(request.getRequestId());
            degraded.setRequestVersion(request.getRequestVersion());
        }

        DsLanguageSpi spi = this.findSpi(request);
        if (spi == null) {
            return degraded;
        }

        T result = action.apply(spi);
        return result == null ? degraded : result;
    }

    //

    public CompletionResult complete(String ownerUid, CompletionRequest request) {
        return invoke(ownerUid, request, CompletionResult::new, spi -> {
            return spi.complete(request);
        });
    }

    public ValidateResult validate(String ownerUid, ValidateRequest request) {
        return invoke(ownerUid, request, ValidateResult::new, spi -> {
            return offsetValidateResult(request, spi.validate(request));
        });
    }

    public SplitResult split(String ownerUid, SplitRequest request) {
        return invoke(ownerUid, request, SplitResult::new, spi -> {
            return spi.split(request);
        });
    }

    //

    private static ValidateResult offsetValidateResult(AbstractRequest request, ValidateResult result) {
        for (Diagnostic diagnostic : result.getDiagnostics()) {
            if (diagnostic.getRange() == null) {
                continue;
            }
            BlockLocation range = diagnostic.getRange();
            BlockLocation offsetRange = new BlockLocation();
            offsetRange.setStartPosition(offsetPosition(request, range.getStartPosition()));
            offsetRange.setEndPosition(offsetPosition(request, range.getEndPosition()));
            diagnostic.setRange(offsetRange);
        }
        return result;
    }

    private static CodeLocation offsetPosition(AbstractRequest request, CodeLocation position) {
        int lineNumber = Math.max(1, position.getLineNumber());
        int columnNumber = Math.max(0, position.getColumnNumber());
        int startLineNumber = Math.max(1, request.getBasicCodeLine());
        int startColumnNumber = Math.max(0, request.getBasicCodeColumn());
        int offsetLineNumber = startLineNumber + lineNumber - 1;
        int offsetColumnNumber = lineNumber == 1 ? startColumnNumber + columnNumber : columnNumber;
        return new CodeLocation(offsetLineNumber, offsetColumnNumber);
    }
}
