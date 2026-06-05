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
package com.clougence.clouddm.console.web.component.detectrule;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecRulesCheckResult {

    public static final SecRulesCheckResult EMPTY      = new SecRulesCheckResult();

    private CodeLocation                    location;
    private String                          specName;
    private Map<String, RuleLevel>          checked    = new LinkedHashMap<>();
    private Map<String, String>             messageMap = new LinkedHashMap<>();
    private Map<String, List<String>>       loggerMap  = new LinkedHashMap<>();
    private Map<String, Object>             result     = new LinkedHashMap<>();
    private Map<String, Set<Integer>>       scriptMap  = new LinkedHashMap<>();

    public boolean isAllSuccess() { return this.checked == null || this.checked.isEmpty(); }

    public boolean hasAnyTarget(RuleLevel[] test) {
        if (this.checked == null || this.checked.isEmpty()) {
            return false;
        }

        for (RuleLevel level : this.checked.values()) {
            for (RuleLevel testTarget : test) {
                if (level == testTarget) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<SecHintInfo> toSecHintList() {
        if (this.checked == null || this.checked.isEmpty()) {
            return Collections.emptyList();
        }

        List<SecHintInfo> result = new ArrayList<>();
        this.checked.forEach((name, level) -> {
            SecHintInfo info = new SecHintInfo();

            Set<Integer> lines = this.scriptMap.get(name);
            if (lines != null) {
                info.setLines(lines.stream().sorted().collect(Collectors.toList()));
            }
            info.setSpecName(this.specName);
            info.setRuleName(name);
            info.setMessage(this.messageMap.get(name));
            info.setLevel(level);
            info.setResult(this.result.get(name));

            result.add(info);
        });

        return result;
    }

    public List<SecRulesLogger> toLoggerList() {
        List<SecRulesLogger> loggers = new ArrayList<>();
        loggerMap.forEach((ruleName, loggerInfo) -> {
            loggerInfo.forEach(log -> {
                loggers.add(new SecRulesLogger(ruleName, log));
            });
        });
        return loggers;
    }

    public void addResult(String name, RuleLevel level, Object result, String message, SplitScript script) {
        this.checked.put(name, level);
        this.messageMap.put(name, message);
        this.result.put(name, result);
        if (script != null) {
            this.scriptMap.computeIfAbsent(name, k -> new HashSet<>()).add(script.getBodyStartCodeLine());
        }
    }

    public void addResult(String name, RuleLevel level, Object result, String message) {
        this.checked.put(name, level);
        this.messageMap.put(name, message);
        this.result.put(name, result);
    }

    public void addLogger(String name, List<String> logger) {
        if (CollectionUtils.isNotEmpty(logger)) {
            this.loggerMap.computeIfAbsent(name, s -> new ArrayList<>()).addAll(logger);
        }
    }
}
