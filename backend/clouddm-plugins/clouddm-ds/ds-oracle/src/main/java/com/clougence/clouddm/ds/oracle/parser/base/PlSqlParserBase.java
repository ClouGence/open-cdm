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
package com.clougence.clouddm.ds.oracle.parser.base;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public abstract class PlSqlParserBase extends Parser {

    private boolean        _isVersion12 = true;
    private boolean        _isVersion10 = true;
    public PlSqlParserBase self;

    public PlSqlParserBase(TokenStream input){
        super(input);
        self = this;
    }

    public boolean isVersion12() { return _isVersion12; }

    public void setVersion12(boolean value) { _isVersion12 = value; }

    public boolean isVersion10() { return _isVersion10; }

    public void setVersion10(boolean value) { _isVersion10 = value; }
}
