/*
 * Copyright 2008-2009 the original author or authors.
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
package com.clougence.dslpaser.ast.location;

import lombok.Getter;

/**
 * ast and line number location
 * @author zyc@hasor.net
 * @version : 2020-06-11
 */
@Getter
public final class CodeLocation {

    private final int lineNumber;
    private final int columnNumber;

    public CodeLocation(){
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    public CodeLocation(int lineNumber, int columnNumber){
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String toString() {
        if (lineNumber <= 0 && columnNumber < 0) {
            return "Unknown";
        }
        String lineNumStr = lineNumber >= 0 ? String.valueOf(lineNumber) : "Unknown";
        String columnNumStr = columnNumber >= 0 ? String.valueOf(columnNumber) : "Unknown";
        if ("Unknown".equalsIgnoreCase(columnNumStr)) {
            return lineNumStr;
        } else {
            return lineNumStr + ":" + columnNumStr;
        }
    }
}
