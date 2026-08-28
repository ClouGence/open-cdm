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

import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecRulesCheckResult {

    public static final SecRulesCheckResult EMPTY                       = new SecRulesCheckResult();
    private static final int                MAX_LOGGERS                 = 500;
    private static final int                MAX_CODE_LOCATIONS_PER_RULE = 20;

    private CodeLocation                    location;
    private String                          specName;
    private Map<String, RuleLevel>          checked                     = new LinkedHashMap<>();
    private Map<String, Long>               hitCountMap                 = new LinkedHashMap<>();
    private Map<String, String>             messageMap                  = new LinkedHashMap<>();
    private Map<String, List<String>>       loggerMap                   = new LinkedHashMap<>();
    private Map<String, Object>             result                      = new LinkedHashMap<>();
    private Map<String, Set<Integer>>       scriptMap                   = new LinkedHashMap<>();
    private long                            totalHitCount;
    private int                             loggerCount;
    private boolean                         loggerTruncated;

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
            info.setHitCount(this.hitCountMap.getOrDefault(name, 0L));
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
        this.hitCountMap.merge(name, 1L, Long::sum);
        this.totalHitCount++;
        this.messageMap.put(name, message);
        this.result.put(name, result);
        if (script != null) {
            this.addCodeLocation(name, script.getBodyStartCodeLine());
        }
    }

    public void addResult(String name, RuleLevel level, Object result, String message) {
        this.checked.put(name, level);
        this.hitCountMap.merge(name, 1L, Long::sum);
        this.totalHitCount++;
        this.messageMap.put(name, message);
        this.result.put(name, result);
    }

    public void addLogger(String name, List<String> logger) {
        if (CollectionUtils.isEmpty(logger)) {
            return;
        }
        int remaining = MAX_LOGGERS - this.loggerCount;
        if (remaining <= 0) {
            this.loggerTruncated = true;
            return;
        }
        int collectCount = Math.min(remaining, logger.size());
        this.loggerMap.computeIfAbsent(name, ignored -> new ArrayList<>()).addAll(logger.subList(0, collectCount));
        this.loggerCount += collectCount;
        if (collectCount < logger.size()) {
            this.loggerTruncated = true;
        }
    }

    public void merge(SecRulesCheckResult source) {
        if (source.specName != null) {
            this.specName = source.specName;
        }
        this.checked.putAll(source.checked);
        source.hitCountMap.forEach((name, count) -> this.hitCountMap.merge(name, count, Long::sum));
        this.totalHitCount += source.totalHitCount;
        this.messageMap.putAll(source.messageMap);
        this.result.putAll(source.result);

        source.loggerMap.forEach(this::addLogger);
        source.scriptMap.forEach((name, lines) -> {
            lines.forEach(line -> this.addCodeLocation(name, line));
        });
        this.loggerTruncated |= source.loggerTruncated;
    }

    private void addCodeLocation(String name, int line) {
        Set<Integer> lines = this.scriptMap.computeIfAbsent(name, ignored -> new HashSet<>());
        if (lines.contains(line)) {
            return;
        }
        if (lines.size() >= MAX_CODE_LOCATIONS_PER_RULE) {
            return;
        }
        lines.add(line);
    }
}
