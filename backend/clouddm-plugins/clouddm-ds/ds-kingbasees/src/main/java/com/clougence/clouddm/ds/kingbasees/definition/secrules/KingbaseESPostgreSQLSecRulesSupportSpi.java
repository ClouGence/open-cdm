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
package com.clougence.clouddm.ds.kingbasees.definition.secrules;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.SecRulesSupportSpi;

public class KingbaseESPostgreSQLSecRulesSupportSpi implements SecRulesSupportSpi {

    private static final List<TargetType> OBJECT_RANGES = Arrays.asList( //
            TargetType.Catalog,                                          //
            TargetType.Schema,                                           //
            TargetType.Table,                                            //
            TargetType.View,                                             //
            TargetType.Materialized                                      //
    );

    @Override
    public boolean isSupport() { return true; }

    @Override
    public List<TargetType> supportModel() {
        return Arrays.asList(            //
                TargetType.Catalog,      //
                TargetType.Schema,       //
                TargetType.Table,        //
                TargetType.View,         //
                TargetType.Column,       //
                TargetType.Index,        //
                TargetType.Constraint,   //
                TargetType.Query,        //
                TargetType.Update,       //
                TargetType.Delete,       //
                TargetType.Insert        //
        );
    }

    @Override
    public List<TargetType> exactRangeForQuery() {
        return OBJECT_RANGES;
    }

    @Override
    public List<TargetType> prefixRangeForQuery() {
        return OBJECT_RANGES;
    }

    @Override
    public List<TargetType> suffixRangeForQuery() {
        return OBJECT_RANGES;
    }

    @Override
    public List<TargetType> includeRangeForQuery() {
        return OBJECT_RANGES;
    }

    @Override
    public List<TargetType> exactRangeForSen() {
        return Arrays.asList(             //
                TargetType.Catalog,       //
                TargetType.Schema,        //
                TargetType.Table,         //
                TargetType.View,          //
                TargetType.Materialized,  //
                TargetType.Column        //
        );
    }

    @Override
    public List<TargetType> prefixRangeForSen() {
        return exactRangeForSen();
    }

    @Override
    public List<TargetType> suffixRangeForSen() {
        return exactRangeForSen();
    }

    @Override
    public List<TargetType> includeRangeForSen() {
        return exactRangeForSen();
    }
}
