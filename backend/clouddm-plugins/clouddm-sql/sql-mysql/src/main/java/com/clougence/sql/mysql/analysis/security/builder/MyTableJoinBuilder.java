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
package com.clougence.sql.mysql.analysis.security.builder;

import java.util.LinkedHashMap;

import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbJoinType;
import com.clougence.sql.common.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.sql.common.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.sql.mysql.analysis.security.MySecDomainOptionKeys;

public class MyTableJoinBuilder extends TableJoinBuilder {

    public MyTableJoinBuilder(String joinType){
        super(joinType);
        if ("naturaljoin".equalsIgnoreCase(joinType) || "naturalinnerjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalleftjoin".equalsIgnoreCase(joinType) || "naturalleftouterjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.LEFT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalrightjoin".equalsIgnoreCase(joinType) || "naturalrightouterjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.RIGHT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalcrossjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.CROSS_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        }
    }
}
