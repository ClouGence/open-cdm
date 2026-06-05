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
package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2024-11-20 11:00
 */
@Getter
@Setter
public class RdbCallDomain extends RuleDomain implements RdbConfigNames {

    private String       catalog;
    private String       schema;
    private String       callName;
    private boolean      func;

    private List<String> args;
    private boolean      emptyArg;

    public void addArgStr(String argStr) {
        if (argStr == null || argStr.isEmpty()) {
            return;
        }
        if (this.args == null) {
            this.args = new ArrayList<>();
        }
        if (!this.args.contains(argStr)) {
            this.args.add(argStr);
            this.emptyArg = false;
        }
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        if (isFunc()) {
            map.put(TargetType.Function, this.callName);
        } else {
            map.put(TargetType.Procedure, this.callName);
        }

        return Collections.singletonList(map);
    }

    @Override
    public void configName(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.callName = name;
    }
}
