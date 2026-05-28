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
package com.clougence.clouddm.console.web.global.i18n;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.i18n.I18nDictionary;

@Service
public class DmI18nDictionaryImpl implements I18nDictionary {

    @Override
    public Locale getLocale() { return DmI18nUtils.getLocale(); }

    @Override
    public String getMessage(String key) {
        return DmI18nUtils.getMessage(key);
    }

    @Override
    public String getMessage(String key, Object... args) {
        return DmI18nUtils.getMessage(key, args);
    }

    @Override
    public String getMessage(Enum<?> key, Object... args) {
        return DmI18nUtils.getMessage(key, args);
    }

    @Override
    public String getMessage(String key, Locale locale, Object... args) {
        return DmI18nUtils.getMessage(key, locale, args);
    }
}
