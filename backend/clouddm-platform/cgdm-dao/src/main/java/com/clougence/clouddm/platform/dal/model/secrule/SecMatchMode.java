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

import com.clougence.clouddm.sdk.service.secrules.MatchMode;

import lombok.Getter;

/**
 * @author mode 2021/1/7 19:16
 */
@Getter
public enum SecMatchMode {

    EXACT(MatchMode.EXACT, "SEC_SEN_MATCH_EXACT"),
    PREFIX(MatchMode.PREFIX, "SEC_SEN_MATCH_PREFIX"),
    SUFFIX(MatchMode.SUFFIX, "SEC_SEN_MATCH_SUFFIX"),
    INCLUDE(MatchMode.INCLUDE, "SEC_SEN_MATCH_INCLUDE");

    private final String    i18nKey;
    private final MatchMode matchMode;

    SecMatchMode(MatchMode matchMode, String i18nKey){
        this.matchMode = matchMode;
        this.i18nKey = i18nKey;
    }
}
