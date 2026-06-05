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
package com.clougence.utils.loader;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Loader 基类
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractResourceLoader implements ResourceLoader {

    @Override
    public CgClassLoader toClassLoader(ClassLoader parent) {
        return new CgClassLoader(parent, this);
    }

    protected <T> Predicate<T>[] buildPredicate(MatchType matchType, String[] scanPaths, Function<T, String> nameFunc) {
        if (matchType == null) {
            throw new IllegalStateException("missing matchType.");
        }
        Predicate<T>[] tests = new Predicate[scanPaths.length];
        for (int i = 0; i < scanPaths.length; i++) {
            final String matchExpr = scanPaths[i];
            switch (matchType) {
                case None: {
                    tests[i] = e -> true;
                    break;
                }
                case Match: {
                    tests[i] = e -> nameFunc.apply(e).equals(matchExpr);
                    break;
                }
                case Regex: {
                    tests[i] = e -> nameFunc.apply(e).matches(matchExpr);
                    break;
                }
                case ContainsAny: {
                    tests[i] = e -> nameFunc.apply(e).contains(matchExpr);
                    break;
                }
                case Prefix: {
                    tests[i] = e -> nameFunc.apply(e).startsWith(matchExpr);
                    break;
                }
                case Suffix: {
                    tests[i] = e -> nameFunc.apply(e).endsWith(matchExpr);
                    break;
                }
                default: {
                    tests[i] = e -> false;
                    break;
                }
            }
        }
        return tests;
    }
}
