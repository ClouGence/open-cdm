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
package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class I18nKeyVal {

    private final String     i18n;
    private final String     name;
    private final boolean    choose;
    private boolean          hidden;
    private List<I18nKeyVal> children;

    public I18nKeyVal(String name, String i18n){
        this.name = name;
        this.i18n = i18n;
        this.choose = false;
        this.hidden = false;
    }

    public I18nKeyVal(String name, String i18n, boolean choose){
        this.name = name;
        this.i18n = i18n;
        this.choose = choose;
        this.hidden = false;
    }

    public I18nKeyVal(String name, String i18n, boolean choose, boolean hidden){
        this.name = name;
        this.i18n = i18n;
        this.choose = choose;
        this.hidden = hidden;
    }
}
