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
package com.clougence.clouddm.console.web.component.detectrule.impl;

import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckSession;

/**
 * Disabled rule-check session shared by callers when rule checking is unavailable.
 */
final class SecRulesCheckSession4Disabled implements SecRulesCheckSession {

    private static final SecRulesCheckSession4Disabled INSTANCE = new SecRulesCheckSession4Disabled();

    private SecRulesCheckSession4Disabled(){
    }

    public static SecRulesCheckSession4Disabled getInstance() { return INSTANCE; }

    @Override
    public boolean isEnabled() { return false; }

    @Override
    public SecRulesCheckResult applyCheck(String querySql, int baseCodeLine, int baseCodeColumn) {
        return new SecRulesCheckResult();
    }
}
