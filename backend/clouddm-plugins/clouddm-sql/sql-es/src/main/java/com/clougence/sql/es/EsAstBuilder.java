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

import com.clougence.sql.es.antlr.EsParser;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

final class EsAstBuilder {
    private EsAstBuilder(){
    }

    static List<EsPathSegment> segments(EsParser.PathContext context) {
        List<EsPathSegment> result = new ArrayList<>();
        for (EsParser.SegmentContext segment : context.segment()) {
            EsSourceRange range = EsSourceRange.of(segment);
            String raw = segment.getText();
            result.add(new EsPathSegment(EsUrlCodec.decode(raw, false, range), raw, range));
        }
        return result;
    }

    static EsRequest from(EsParser.RequestContext context, String rawText) {
        EsParser.TargetContext target = context.header().target();
        List<EsQueryParameter> parameters = new ArrayList<>();
        for (EsParser.ParameterContext parameter : target.parameter()) {
            EsSourceRange range = EsSourceRange.of(parameter);
            String name = EsUrlCodec.decode(parameter.TEXT().getText(), true, range);
            String value = "";
            if (parameter.parameterValue() != null) {
                value = EsUrlCodec.decode(parameter.parameterValue().getText(), true, range);
            }
            parameters.add(new EsQueryParameter(name, value, parameter.EQUALS() != null, parameter.getText(), range));
        }
        EsJsonObject body = null;
        String rawBody = null;
        if (context.jsonObject() != null) {
            body = object(context.jsonObject());
            int offset = body.range().start() - context.getStart().getStartIndex();
            rawBody = rawText.substring(rawText.offsetByCodePoints(0, offset));
        }
        return new EsRequest(context.header().METHOD().getText(), target.path().getText(), segments(target.path()), parameters, body, rawBody, rawText, EsSourceRange.of(context));
    }

    private static EsJsonObject object(EsParser.JsonObjectContext context) {
        List<EsJsonMember> members = new ArrayList<>();
        for (EsParser.MemberContext member : context.member()) {
            members.add(new EsJsonMember(string(member.STRING().getSymbol()), value(member.value()), EsSourceRange.of(member)));
        }
        return new EsJsonObject(members, EsSourceRange.of(context));
    }

    private static EsJsonValue value(EsParser.ValueContext context) {
        if (context.jsonObject() != null) {
            return object(context.jsonObject());
        }
        if (context.jsonArray() != null) {
            EsParser.JsonArrayContext array = context.jsonArray();
            return new EsJsonArray(array.value().stream().map(EsAstBuilder::value).toList(), EsSourceRange.of(array));
        }
        if (context.STRING() != null) {
            return string(context.getStart());
        }
        if (context.NUMBER() != null) {
            return new EsJsonNumber(context.getText(), EsSourceRange.of(context));
        }
        return new EsJsonLiteral(context.getText(), EsSourceRange.of(context));
    }

    private static EsJsonString string(Token token) {
        String text = token.getText();
        StringBuilder decoded = new StringBuilder();
        for (int i = 1; i < text.length() - 1; i++) {
            char ch = text.charAt(i);
            if (ch != '\\') {
                decoded.append(ch);
                continue;
            }
            char escape = text.charAt(++i);
            switch (escape) {
                case 'b' -> decoded.append('\b');
                case 'f' -> decoded.append('\f');
                case 'n' -> decoded.append('\n');
                case 'r' -> decoded.append('\r');
                case 't' -> decoded.append('\t');
                case 'u' -> {
                    decoded.append((char) Integer.parseInt(text.substring(i + 1, i + 5), 16));
                    i += 4;
                }
                default -> decoded.append(escape);
            }
        }
        return new EsJsonString(decoded.toString(), text, EsSourceRange.of(token, token));
    }
}
