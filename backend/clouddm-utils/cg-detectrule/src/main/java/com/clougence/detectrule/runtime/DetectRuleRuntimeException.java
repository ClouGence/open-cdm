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
package com.clougence.detectrule.runtime;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.ast.location.Location;

import lombok.Getter;

@Getter
public class DetectRuleRuntimeException extends RuntimeException {

    private final LangObject throwData;

    public DetectRuleRuntimeException(Location location, LangObject s){
        super(toLineRange(location) + ": " + s);
        this.throwData = s;
    }

    private static String toLineRange(Location location) {
        CodeLocation startPosition = location.getStartPosition();
        CodeLocation endPosition = location.getEndPosition();

        String starStr = startPosition == null ? "Unknown" : startPosition.toString();
        String endStr = endPosition == null ? "Unknown" : endPosition.toString();
        if ("Unknown".equalsIgnoreCase(starStr) && "Unknown".equalsIgnoreCase(endStr)) {
            return "Unknown";
        }
        if ("Unknown".equalsIgnoreCase(endStr)) {
            return "line " + starStr;
        } else {
            return "line " + starStr + "~" + endStr;
        }
    }
}
