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
package com.clougence.schema.umi.service;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbPartition;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTableUtilsInfo {

    private Map<String, List<RdbColumn>>            allColumns;
    private Map<String, Map<String, UmiConstraint>> pkUkMap;
    private Map<String, List<RdbForeignKey>>        fkMap;
    private Map<String, List<RdbIndex>>             indexMap;
    private Map<String, RdbPartition>               partitionMap;
    private Map<String, List<ConstraintObject>>     constraintMap;
}
