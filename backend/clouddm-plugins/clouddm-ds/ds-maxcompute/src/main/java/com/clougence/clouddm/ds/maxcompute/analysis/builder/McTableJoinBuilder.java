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
package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.LinkedHashMap;

import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainOptionKeys;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;

public class McTableJoinBuilder extends TableJoinBuilder {

    public McTableJoinBuilder(String joinType){
        super(joinType);
        if ("naturaljoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(McSecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        }
    }
}
