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
package com.clougence.clouddm.ds.doris.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.utils.StringUtils;

public class DrTableJoinBuilder extends TableJoinBuilder {

    public DrTableJoinBuilder(String text){
        super(text);
        if (StringUtils.isEmpty(text)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            return;
        }
        text = text.toLowerCase();
        if (text.equalsIgnoreCase("cross")) {
            this.joinDomain = new JoinDomain(RdbJoinType.CROSS_JOIN);
        } else if (text.startsWith("inner")) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
        } else if (text.startsWith("left")) {
            this.joinDomain = new JoinDomain(RdbJoinType.LEFT_JOIN);
        } else if (text.startsWith("right")) {
            this.joinDomain = new JoinDomain(RdbJoinType.RIGHT_JOIN);
        } else {
            this.joinDomain = new JoinDomain(RdbJoinType.OTHER_JOIN);
        }
    }
}
