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

import com.clougence.clouddm.sdk.service.secrules.RuleLevel;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum WarnLevel {

    PASS(4, RuleLevel.PASS),       // L4
    SUGGEST(3, RuleLevel.SUGGEST), // L3
    TICKET(2, RuleLevel.TICKET),   // L2
    FAILURE(1, RuleLevel.FAILURE)  // L1
    ;

    private final int       level;
    private final RuleLevel ruleLevel;

    WarnLevel(int level, RuleLevel ruleLevel){
        this.level = level;
        this.ruleLevel = ruleLevel;
    }

    public static WarnLevel valueOfCode(RuleLevel level) {
        if (level == null) {
            return null;
        }

        switch (level) {
            case PASS:
                return WarnLevel.PASS;
            case SUGGEST:
                return WarnLevel.SUGGEST;
            case TICKET:
                return WarnLevel.TICKET;
            case FAILURE:
                return WarnLevel.FAILURE;
            default:
                return null;
        }
    }
}
