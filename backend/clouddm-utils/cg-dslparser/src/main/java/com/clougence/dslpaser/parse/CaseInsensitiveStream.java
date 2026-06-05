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
package com.clougence.dslpaser.parse;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Interval;

public class CaseInsensitiveStream implements CharStream {

    private final CharStream charStream;

    public CaseInsensitiveStream(CharStream charStream){
        this.charStream = charStream;
    }

    @Override
    public String getText(Interval interval) {
        return this.charStream.getText(interval);

    }

    @Override
    public void consume() {
        this.charStream.consume();
    }

    @Override
    public int LA(int i) {
        int result = charStream.LA(i);

        switch (result) {
            case 0:
            case IntStream.EOF:
                return result;
            default:
                return Character.toUpperCase(result);
        }
    }

    @Override
    public int mark() {
        return this.charStream.mark();
    }

    @Override
    public void release(int i) {
        this.charStream.release(i);
    }

    @Override
    public int index() {
        return this.charStream.index();
    }

    @Override
    public void seek(int i) {
        this.charStream.seek(i);
    }

    @Override
    public int size() {
        return this.charStream.size();
    }

    @Override
    public String getSourceName() { return this.charStream.getSourceName(); }
}
