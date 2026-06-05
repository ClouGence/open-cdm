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
package com.clougence.reactor.handlers.attributes;

import com.clougence.utils.StringUtils;

import lombok.Getter;

public enum TimeDefaultStrategyEnum {

    // do nothing to follow the original
    NOTHING("nothing"),
    // use 'zero', is kile "not null default '0000-00-00 00:00:00'"
    ZERO("zero"),
    // will be set empty or "null"
    IS_NULL("is_null"),
    // use 'current', is like "not null default current_timestamp"
    CURRENT("current"),
    // is '1970-01-01 00:00:00'
    UTC_ZERO("utc_zero"),
    // is '0000-01-01 00:00:00'
    ERA_ZERO("era_zero"),;

    @Getter
    private final String strategyType;

    TimeDefaultStrategyEnum(String strategyType){
        this.strategyType = strategyType;
    }

    public static TimeDefaultStrategyEnum valueOfCode(String strategyType) {
        for (TimeDefaultStrategyEnum strategyEnum : TimeDefaultStrategyEnum.values()) {
            if (StringUtils.equalsIgnoreCase(strategyEnum.strategyType, strategyType)) {
                return strategyEnum;
            }
        }
        return null;
    }
}
