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
package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class TableJoinBuilder implements DomainBuilder {

    protected JoinDomain joinDomain;

    public TableJoinBuilder(String text){
        RdbJoinType type = RdbJoinType.OTHER_JOIN;
        if ("join".equalsIgnoreCase(text)) {
            type = RdbJoinType.INNER_JOIN;
        } else if ("leftjoin".equalsIgnoreCase(text) || "leftouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.LEFT_JOIN;
        } else if ("rightjoin".equalsIgnoreCase(text) || "rightouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.RIGHT_JOIN;
        } else if ("innerjoin".equalsIgnoreCase(text) || "innertouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.INNER_JOIN;
        } else if ("crossjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.CROSS_JOIN;
        }
        this.joinDomain = new JoinDomain(type);
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(joinDomain);
    }
}
