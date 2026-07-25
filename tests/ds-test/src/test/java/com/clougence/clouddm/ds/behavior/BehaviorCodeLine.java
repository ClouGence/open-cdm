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
package com.clougence.clouddm.ds.behavior;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;

/** Fixture projection for a BehaviorObject end-exclusive source range. */
public final class BehaviorCodeLine {

    private static final Pattern FORMAT = Pattern.compile("([1-9][0-9]*):([0-9]+)~([1-9][0-9]*):([0-9]+)");

    private BehaviorCodeLine(){
    }

    public static String format(BehaviorObject object) {
        return new Range(object.getStartLine(), object.getStartColumn(), object.getEndLine(), object.getEndColumn()).format();
    }

    public static Range parse(String value) {
        Matcher matcher = FORMAT.matcher(value == null ? "" : value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("codeLine must match startLine:startColumn~endLine:endColumn: " + value);
        }
        try {
            return new Range(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("codeLine coordinate exceeds integer range: " + value, e);
        }
    }

    public record Range(int startLine, int startColumn, int endLine, int endColumn) {

        public Range {
            if (startLine < 1 || startColumn < 0 || endLine < 1 || endColumn < 0) {
                throw new IllegalArgumentException("codeLine coordinates must use 1-based lines and 0-based columns");
            }
            if (endLine < startLine || (endLine == startLine && endColumn <= startColumn)) {
                throw new IllegalArgumentException("codeLine end-exclusive position must be after its start");
            }
        }

        public String format() {
            return startLine + ":" + startColumn + "~" + endLine + ":" + endColumn;
        }
    }
}
