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
package com.clougence.clouddm.ds.valkey.language;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.dsfamily.language.split.SplitStrategyCenter;
import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class ValkeyLanguageSpi implements DsLanguageSpi {
    private final MetaService                 metaService;
    private final ValkeyValidateStrategyCenter validate = new ValkeyValidateStrategyCenter();
    private final SplitStrategyCenter         split    = new SplitStrategyCenter();

    public ValkeyLanguageSpi(MetaService metaService){
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
        return Set.of(DsLanguageSupport.COMPLETE, DsLanguageSupport.VALIDATE, DsLanguageSupport.SPLIT);
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = initResult(request, new CompletionResult());
        String prefix = keyPrefix(request);
        List<CompletionItem> items = new ArrayList<>();
        for (MetaObj metaObj : this.metaService
            .cachedObjectNames(request.getPrimaryUserId(), request.getCurrentUserId(), request.getDataSourceId(), List.of(UmiTypes.Key), request.getLevelsParam())) {
            if (metaObj == null || metaObj.getType() != UmiTypes.Key || StringUtils.isBlank(metaObj.getName()) || !metaObj.getName().startsWith(prefix)) {
                continue;
            }

            CompletionItem item = new CompletionItem();
            item.setLabel(metaObj.getName());
            item.setKind(CompletionItemKind.TEXT);
            item.setUmiType(UmiTypes.Key);
            item.setIcon("KEY");
            item.setInsertText(metaObj.getName());
            item.setWeight(800);
            items.add(item);
        }
        items.sort(Comparator.comparing(CompletionItem::getLabel));
        result.getItems().addAll(items);
        return result;
    }

    private static String keyPrefix(CompletionRequest request) {
        String before = beforeCursor(request);
        int start = before.length();
        while (start > 0 && !Character.isWhitespace(before.charAt(start - 1))) {
            start--;
        }
        return before.substring(start);
    }

    private static String beforeCursor(CompletionRequest request) {
        String sqlText = StringUtils.toString(request.getSqlText());
        int offset = 0;
        int line = 1;
        int column = 0;
        while (offset < sqlText.length()) {
            if (line == request.getCursorLineNumber() && column == request.getCursorColNumber()) {
                break;
            }
            char c = sqlText.charAt(offset++);
            if (c == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return sqlText.substring(0, offset);
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = initResult(request, new ValidateResult());
        result.getDiagnostics().addAll(this.validate.validate(request, this.metaService));
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        return this.split.split(request);
    }
}
