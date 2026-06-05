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
package com.clougence.clouddm.console.web.util;

import java.util.Locale;

/**
 * @author bucketli 2021/11/16 17:08:09
 */
public class RdpLocal {

    private static final ThreadLocal<Locale> localLanguage = new ThreadLocal<>();

    public static Locale getLocal() { return localLanguage.get(); }

    public static void setLocal(Locale locale) {
        if (localLanguage.get() != null) {
            localLanguage.remove();
        }
        if (locale != null) {
            localLanguage.set(locale);
        }
    }
}
