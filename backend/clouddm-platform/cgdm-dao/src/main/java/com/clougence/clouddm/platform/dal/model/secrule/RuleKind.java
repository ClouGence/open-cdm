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
package com.clougence.clouddm.platform.dal.model.secrule;

import lombok.Getter;

/**
 * @Author: Ekko
 * @Date: 2024-07-10 14:59
 */
@Getter
public enum RuleKind {

    QUERY("SEC_SEN_KIND_QUERY"),
    SENSITIVE("SEC_SEN_KIND_SENSITIVE");

    private final String i18nKey;

    RuleKind(String i18nKey){
        this.i18nKey = i18nKey;
    }
}
