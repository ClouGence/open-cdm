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
package com.clougence.sql.db2.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.misc.Interval;

public class UpperCaseCharStream implements CharStream {

    private final CharStream delegate;

    public UpperCaseCharStream(CharStream delegate){
        this.delegate = delegate;
    }

    @Override
    public String getText(Interval interval) {
        return delegate.getText(interval);
    }

    @Override
    public void consume() {
        delegate.consume();
    }

    @Override
    public int LA(int i) {
        int result = delegate.LA(i);
        if (result <= 0) {
            return result;
        }
        return Character.toUpperCase(result);
    }

    @Override
    public int mark() {
        return delegate.mark();
    }

    @Override
    public void release(int marker) {
        delegate.release(marker);
    }

    @Override
    public int index() {
        return delegate.index();
    }

    @Override
    public void seek(int index) {
        delegate.seek(index);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public String getSourceName() { return delegate.getSourceName(); }
}
