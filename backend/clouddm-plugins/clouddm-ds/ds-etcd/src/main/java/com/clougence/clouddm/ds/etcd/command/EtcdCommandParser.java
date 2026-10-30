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
package com.clougence.clouddm.ds.etcd.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

public final class EtcdCommandParser {

    private EtcdCommandParser(){
    }

    public static List<SplitScript> split(Reader reader, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        List<SplitScript> scripts = new ArrayList<>();
        int lineNo = baseCodeLine <= 0 ? 1 : baseCodeLine;
        int firstLine = lineNo;
        long index = 0;
        try (BufferedReader lines = new BufferedReader(reader)) {
            String line;
            while ((line = lines.readLine()) != null) {
                String body = strip(line);
                if (!body.isEmpty()) {
                    int startColumn = line.indexOf(body);
                    if (lineNo == firstLine) {
                        startColumn = startColumn + Math.max(baseCodeColumn, 0);
                    }
                    SplitScript script = new SplitScript();
                    script.setIndex(index++);
                    script.setScript(body);
                    script.setScriptArgs(args);
                    script.setType(Set.of(queryType(body)));
                    script.setBodyStartCodeLine(lineNo);
                    script.setBodyStartCodeColumn(startColumn);
                    script.setBodyEndCodeLine(lineNo);
                    script.setBodyEndCodeColumn(startColumn + body.length());
                    scripts.add(script);
                }
                lineNo++;
            }
        } catch (IOException e) {
            throw ThirdPartyApiException.as().with(e);
        }
        return scripts;
    }

    public static EtcdCommand parse(String script) {
        String body = strip(script);
        if (body.isEmpty()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_COMMAND_UNSUPPORTED);
        }
        int space = body.indexOf(' ');
        String token = space < 0 ? body : body.substring(0, space);
        String rest = space < 0 ? "" : body.substring(space + 1).trim();
        EtcdCommandKind kind = EtcdCommandKind.find(token);
        if (kind == null) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_COMMAND_UNSUPPORTED);
        }
        if (rest.isEmpty()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_COMMAND_KEY_REQUIRED);
        }
        if (kind == EtcdCommandKind.PUT) {
            int[] cursor = new int[] { 0 };
            String key = readArgument(rest, cursor);
            if (key == null || key.isEmpty()) {
                throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_COMMAND_KEY_REQUIRED);
            }
            String value = "";
            int valueStart = cursor[0];
            while (valueStart < rest.length() && rest.charAt(valueStart) == ' ') {
                valueStart++;
            }
            if (valueStart < rest.length()) {
                value = unquote(rest.substring(valueStart).trim());
            }
            return new EtcdCommand(kind, key, value);
        }
        return new EtcdCommand(kind, unquote(rest), null);
    }

    private static String readArgument(String text, int[] cursor) {
        int start = cursor[0];
        while (start < text.length() && text.charAt(start) == ' ') {
            start++;
        }
        if (start >= text.length()) {
            cursor[0] = start;
            return null;
        }
        char quote = text.charAt(start);
        if (quote == '"' || quote == '\'') {
            int end = text.indexOf(quote, start + 1);
            if (end < 0) {
                throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_COMMAND_UNSUPPORTED);
            }
            cursor[0] = end + 1;
            return text.substring(start + 1, end);
        }
        int end = text.indexOf(' ', start);
        if (end < 0) {
            cursor[0] = text.length();
            return text.substring(start);
        }
        cursor[0] = end;
        return text.substring(start, end);
    }

    private static SplitQueryType queryType(String body) {
        int space = body.indexOf(' ');
        String token = space < 0 ? body : body.substring(0, space);
        EtcdCommandKind kind = EtcdCommandKind.find(token);
        if (kind == null) {
            return SplitQueryType.UNKNOWN;
        }
        return kind.queryType();
    }

    private static String strip(String line) {
        if (line == null) {
            return "";
        }
        String body = line.trim();
        if (body.startsWith("#")) {
            return "";
        }
        if (body.endsWith(";")) {
            body = body.substring(0, body.length() - 1).trim();
        }
        return body;
    }

    private static String unquote(String text) {
        if (text.length() >= 2) {
            char first = text.charAt(0);
            char last = text.charAt(text.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return text.substring(1, text.length() - 1);
            }
        }
        return text;
    }
}
