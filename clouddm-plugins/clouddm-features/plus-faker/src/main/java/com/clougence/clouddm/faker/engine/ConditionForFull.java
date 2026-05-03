/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.clouddm.faker.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.generator.BoundQuery;
import com.clougence.clouddm.faker.generator.FakerRepository;

/**
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class ConditionForFull extends AbstractCondition {

    private final List<String>      tables     = new ArrayList<>();
    private final Map<String, Long> tableTotal = new HashMap<>();

    public ConditionForFull(FakerRepository repository){
        repository.getGeneratorTables().stream().filter(tab -> {
            return tab.getAcceptTotal().get(OpsType.Insert) > 0;
        }).forEach(tab -> {
            this.tables.add(tab.getTable());
            this.tableTotal.put(tab.getTable(), tab.getAcceptTotal().get(OpsType.Insert));
        });
    }

    @Override
    public boolean testContinue(FakerMonitor monitor) {
        Map<String, Map<OpsType, AtomicLong>> succeedCounter = monitor.getAffectRowsCounter();
        for (String tab : this.tables) {
            if (!succeedCounter.containsKey(tab)) {
                return true;
            }

            Map<OpsType, AtomicLong> opsMap = succeedCounter.get(tab);
            if (!opsMap.containsKey(OpsType.Insert)) {
                return true;
            }

            AtomicLong counter = opsMap.get(OpsType.Insert);
            if (counter.get() < this.tableTotal.get(tab)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean testIgnore(FakerMonitor monitor, BoundQuery event) {
        Map<String, Map<OpsType, AtomicLong>> succeedCounter = monitor.getAffectRowsCounter();
        String tab = event.getTableInfo().getTable();

        if (!succeedCounter.containsKey(tab)) {
            return false;
        }

        Map<OpsType, AtomicLong> opsMap = succeedCounter.get(tab);
        if (!opsMap.containsKey(OpsType.Insert)) {
            return false;
        }

        AtomicLong counter = opsMap.get(OpsType.Insert);
        if (counter.get() < this.tableTotal.get(tab)) {
            return false;
        }

        return true;
    }
}
