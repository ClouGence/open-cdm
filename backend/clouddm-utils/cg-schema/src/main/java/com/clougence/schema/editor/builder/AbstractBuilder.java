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
package com.clougence.schema.editor.builder;

import com.clougence.schema.editor.TableEditor.Builder;

/**
 * @author mode 2021/5/21 19:56
 */
public abstract class AbstractBuilder implements Builder {

    private boolean finish;

    public AbstractBuilder(){
        this.finish = false;
    }

    @Override
    public boolean isFinish() { return this.finish; }

    @Override
    public void finish() {
        if (finish) {
            throw new IllegalStateException("already finish.");
        }
        this.finish = true;
    }

    protected abstract void doChanges();
}
