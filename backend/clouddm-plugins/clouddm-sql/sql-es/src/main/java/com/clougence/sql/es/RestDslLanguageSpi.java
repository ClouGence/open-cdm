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
package com.clougence.sql.es;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.language.format.FormatRequest;
import com.clougence.clouddm.sdk.language.format.FormatResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.DiagnosticSeverity;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateResult;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.i18n.I18nUtils;

public class RestDslLanguageSpi implements DsLanguageSpi {
    private final MetaService metaService;
    private final RestProduct product;
    private final I18nUtils   i18n = I18nUtils.initI18n(EsSqlI18nKeys.class);

    public RestDslLanguageSpi(MetaService metaService, RestProduct product){
        this.metaService = metaService;
        this.product = product;
    }

    @Override
    public Set<DsLanguageSupport> supports() {
        return EnumSet.allOf(DsLanguageSupport.class);
    }

    @Override
    public ValidateResult validate(ValidateRequest request) {
        ValidateResult result = init(request, new ValidateResult());
        String text = request.getSqlText();
        try {
            SqlParserParameters parameters = product.parameters(new SqlParserParameters(request.getSqlParameters()));
            var parsed = new EsRequestParser(parameters).parseScript(text);
            for (var statement : parsed.getStatements()) {
                EsRequestPolicy.analyze((EsRequest) statement, parameters);
            }
        } catch (EsParseException e) {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.ERROR);
            diagnostic.setMessage(i18n.getMessage(e.getMessageKey(), e.getMessageArgs()));
            diagnostic.setRange(range(text, text.offsetByCodePoints(0, e.getRange().start()), text.offsetByCodePoints(0, e.getRange().end()), 1, 0));
            result.getDiagnostics().add(diagnostic);
        }
        return result;
    }

    @Override
    public SplitResult split(SplitRequest request) {
        SplitResult result = init(request, new SplitResult());
        String text = request.getSqlText();
        try {
            for (EsRequest parsed : EsEditorParser.parse(text)) {
                int start = text.offsetByCodePoints(0, parsed.range().start());
                int end = text.offsetByCodePoints(0, parsed.range().end());
                SplitSqlStatement statement = new SplitSqlStatement();
                statement.setSql(text.substring(start, end));
                statement.setRange(range(text, start, end, request.getBasicCodeLine(), request.getBasicCodeColumn()));
                result.getStatements().add(statement);
            }
        } catch (EsParseException e) {
            // Incomplete documents have no executable selection until ANTLR can establish request boundaries.
        }
        return result;
    }

    @Override
    public FormatResult format(FormatRequest request) {
        FormatResult result = init(request, new FormatResult());
        try {
            result.setSqlText(EsEditorParser.format(request.getSqlText()));
        } catch (EsParseException e) {
            result.setMessage(i18n.getMessage(e.getMessageKey(), e.getMessageArgs()));
        }
        return result;
    }

    @Override
    public CompletionResult complete(CompletionRequest request) {
        CompletionResult result = init(request, new CompletionResult());
        String text = request.getSqlText();
        int offset = 0;
        int line = 1;
        while (line < request.getCursorLineNumber() && offset < text.length()) {
            if (text.charAt(offset++) == '\n') {
                line++;
            }
        }
        offset = Math.min(text.length(), offset + request.getCursorColNumber());
        EsCompletionContext context = EsCompletionContext.at(text, offset);
        RestApiProfile profile = RestApiProfile.from(product.parameters(new SqlParserParameters(request.getSqlParameters())));
        List<String> candidates = new ArrayList<>();
        switch (context.kind()) {
            case METHOD -> candidates.addAll(List.of("GET", "POST", "PUT", "DELETE", "HEAD"));
            case ENDPOINT -> addEndpointCandidates(candidates, context.parent(), profile);
            case PARAMETER -> {
                if (context.parent().endsWith("/_search")) {
                    candidates.addAll(List.of("size", "from", "q", "routing", "timeout", "track_total_hits", "_source", "pretty"));
                } else if (context.parent().endsWith("/_count")) {
                    candidates.addAll(List.of("q", "routing", "pretty"));
                } else {
                    candidates.add("pretty");
                }
            }
            case PROPERTY -> addPropertyCandidates(candidates, context.parent(), profile);
        }

        if (context.kind() == EsCompletionKind.PARAMETER
            && (context.parent().endsWith("/_mapping") || context.parent().endsWith("/_settings") || context.parent().matches("/[^/]+"))) {
            candidates.add(profile.preferredManagerTimeout());
        }
        for (String candidate : candidates) {
            add(request, result, context, candidate, CompletionItemKind.KEYWORD);
        }
        if (context.kind() == EsCompletionKind.INDEX || context.kind() == EsCompletionKind.FIELD) {
            completeMetadata(request, result, context);
        }
        return result;
    }

    private void addEndpointCandidates(List<String> candidates, String method, RestApiProfile profile) {
        switch (method) {
            case "GET" -> candidates.addAll(List.of("_search", "_count", "_doc", "_source", "_mapping", "_settings", "_alias"));
            case "POST" -> candidates.addAll(List.of("_search", "_count", "_doc", "_create", "_update", "_update_by_query", "_delete_by_query"));
            case "PUT" -> candidates.addAll(List.of("_doc", "_create", "_mapping", "_settings"));
            case "DELETE" -> candidates.add("_doc");
            case "HEAD" -> candidates.addAll(List.of("_doc", "_source"));
        }

        if ((method.equals("GET") || method.equals("POST")) && profile.capabilities() != null && profile.capabilities().legacyKnnSearch()) {
            candidates.add("_knn_search");
        }
    }

    private void addPropertyCandidates(List<String> candidates, String parent, RestApiProfile profile) {
        switch (parent) {
            case "" -> candidates.addAll(List.of("query", "size", "from", "sort", "_source", "fields", "aggs", "track_total_hits", "timeout"));
            case "query", "must", "filter", "should", "must_not" ->
                candidates.addAll(List.of("match_all", "match", "match_phrase", "term", "terms", "range", "bool", "exists", "prefix", "wildcard"));
            case "bool" -> candidates.addAll(List.of("must", "filter", "should", "must_not", "minimum_should_match"));
            case "exists" -> candidates.add("field");
            case "aggs", "aggregations" -> {
            }
            default -> candidates.addAll(List.of("field", "size", "order", "gte", "gt", "lte", "lt"));
        }

        if (parent.isEmpty()) {
            if (profile.capabilities() != null && profile.capabilities().searchKnn()) {
                candidates.add("knn");
            }

            candidates.removeAll(profile.excludedSearchFields());
        }
    }

    private void completeMetadata(CompletionRequest request, CompletionResult result, EsCompletionContext context) {
        List<MetaObj> indices = metaService
            .cachedObjectNames(request.getPrimaryUserId(), request.getCurrentUserId(), request.getDataSourceId(), request.getLevels(), request.getLevelsParam());
        for (MetaObj index : indices) {
            if (index.getType() != UmiTypes.Table) {
                continue;
            }
            if (context.kind() == EsCompletionKind.INDEX) {
                add(request, result, context, index.getName(), CompletionItemKind.TABLE);
            } else if (index.getName().equals(context.index())) {
                for (MetaCol column : metaService.fetchTableColumns(request.getCurrentUserId(), request.getDataSourceId(), request.getLevelsParam(), index.getName())) {
                    add(request, result, context, column.getColumn(), CompletionItemKind.COLUMN);
                }
                break;
            }
        }
    }

    private void add(CompletionRequest request, CompletionResult result, EsCompletionContext context, String candidate, CompletionItemKind kind) {
        String value = candidate;
        if (context.kind() == EsCompletionKind.INDEX) {
            new EsIndexReference(candidate, new EsSourceRange(0, candidate.length(), 1, 0, 1, candidate.length()));
            value = URLEncoder.encode(candidate, StandardCharsets.UTF_8).replace("+", "%20");
        }
        boolean json = context.kind() == EsCompletionKind.PROPERTY || context.kind() == EsCompletionKind.FIELD;
        if (json) {
            value = JsonUtils.toJson(candidate);
            if (context.quoted()) {
                value = value.substring(1, value.length() - 1);
            }
        }
        if (!value.startsWith(context.prefix()) && !candidate.startsWith(context.prefix())) {
            return;
        }
        CompletionItem item = new CompletionItem();
        item.setLabel(candidate);
        if (context.kind() == EsCompletionKind.INDEX && !context.parent().startsWith("/")) {
            value = "/" + value;
        }
        if (context.quoted() && (context.end() == request.getSqlText().length() || request.getSqlText().charAt(context.end()) != '"')) {
            value += '"';
        }
        item.setInsertText(value);
        item.setKind(kind);
        item.setWeight(800);
        item.setRange(range(request.getSqlText(), context.start(), context.end(), request.getBasicCodeLine(), request.getBasicCodeColumn()));
        result.getItems().add(item);
    }

    private static BlockLocation range(String text, int start, int end, int baseLine, int baseColumn) {
        BlockLocation range = new BlockLocation();
        int line = Math.max(1, baseLine);
        int column = baseColumn;
        for (int i = 0; i <= end; i++) {
            if (i == start) {
                range.setStartPosition(new CodeLocation(line, column));
            }
            if (i == end) {
                range.setEndPosition(new CodeLocation(line, column));
                break;
            }
            if (text.charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return range;
    }

    private static <T extends LanguageResult> T init(AbstractRequest request, T result) {
        result.setRequestId(request.getRequestId());
        result.setRequestVersion(request.getRequestVersion());
        return result;
    }
}
