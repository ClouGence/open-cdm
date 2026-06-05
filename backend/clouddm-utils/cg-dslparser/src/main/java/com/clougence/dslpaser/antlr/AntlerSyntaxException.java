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
package com.clougence.dslpaser.antlr;

import com.clougence.dslpaser.ast.location.CodeLocation;

import lombok.Getter;

@Getter
public class AntlerSyntaxException extends RuntimeException {

    private final int line;
    private final int column;

    public AntlerSyntaxException(int line, int column, String errorMessage){
        super(errorMessage);
        this.line = Math.max(1, line);
        this.column = Math.max(0, column);
    }

    public CodeLocation offsetLocation(int offsetLine, int offsetColumn) {
        if (this.line == 1) {
            return new CodeLocation(this.line + Math.max(1, offsetLine) - 1, this.column + offsetColumn);
        } else {
            return new CodeLocation(this.line + Math.max(1, offsetLine) - 1, this.column);
        }
    }
}
