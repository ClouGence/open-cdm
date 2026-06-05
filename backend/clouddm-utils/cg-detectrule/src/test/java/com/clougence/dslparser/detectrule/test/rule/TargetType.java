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
package com.clougence.dslparser.detectrule.test.rule;

import lombok.Getter;

@Getter
public enum TargetType {

    // common
    Unknown(null),
    Environment(null),
    Machine(null),

    // rdb
    Catalog(UmiTypes.Catalog),
    Schema(UmiTypes.Schema),
    Table(UmiTypes.Table),
    View(UmiTypes.View),
    Column(UmiTypes.Column),
    Index(UmiTypes.Index),
    Constraint(null),
    Sequence(UmiTypes.Sequence),
    Function(UmiTypes.Function),
    Procedure(UmiTypes.Procedure),
    Trigger(UmiTypes.Trigger),
    Event(null),
    Synonym(UmiTypes.Synonym),

    // cache
    Key(UmiTypes.Key),

    ;

    private final UmiTypes umiType;

    TargetType(UmiTypes umiType){
        this.umiType = umiType;
    }
}
