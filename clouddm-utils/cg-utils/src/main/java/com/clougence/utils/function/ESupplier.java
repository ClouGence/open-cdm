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
package com.clougence.utils.function;

import java.util.function.Supplier;

import com.clougence.utils.ExceptionUtils;

/**
 * Supplier 允许异常抛出。
 * @version 2021-01-23
 * @author 赵永春 (zyc@hasor.net)
 */
@FunctionalInterface
public interface ESupplier<T, E extends Throwable> extends Supplier<T> {

    @Override
    public default T get() {
        try {
            return this.eGet();
        } catch (Throwable e) {
            throw ExceptionUtils.toRuntime(e);
        }
    }

    T eGet() throws E;
}
