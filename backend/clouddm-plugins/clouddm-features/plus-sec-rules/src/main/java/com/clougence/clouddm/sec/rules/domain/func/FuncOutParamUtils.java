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
package com.clougence.clouddm.sec.rules.domain.func;

import com.clougence.detectrule.lang.reflect.RuleFunction;

import java.util.HashMap;
import java.util.Map;

public class FuncOutParamUtils {

    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

    public static Map<String, String> getOutParams() { return threadLocal.get(); }

    public static void initOutParams() {
        threadLocal.set(new HashMap<>());
    }

    public static void clearOutParams() {
        threadLocal.remove();
    }

    @RuleFunction("putParam")
    public void putParam(String key, String value) {
        getOutParams().put(key, value);
    }
}
