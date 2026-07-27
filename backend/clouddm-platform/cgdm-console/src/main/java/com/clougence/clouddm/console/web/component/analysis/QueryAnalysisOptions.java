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
package com.clougence.clouddm.console.web.component.analysis;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;

@Getter
public final class QueryAnalysisOptions {

    private final Set<QueryAnalysisFeature> skippedFeatures;
    private final String                    currentUid;
    private final long                      dataSourceId;
    private final Map<UmiTypes, Object>     levels;
    private final boolean                   deepParser;

    private QueryAnalysisOptions(Builder builder){
        this.skippedFeatures = Set.copyOf(builder.skippedFeatures);
        this.currentUid = builder.currentUid;
        this.dataSourceId = builder.dataSourceId;
        this.levels = builder.levels;
        this.deepParser = builder.deepParser;
    }

    public static QueryAnalysisOptions defaults() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isEnabled(QueryAnalysisFeature feature) {
        return !this.skippedFeatures.contains(feature);
    }

    public static final class Builder {

        private final Set<QueryAnalysisFeature> skippedFeatures = EnumSet.noneOf(QueryAnalysisFeature.class);
        private String                          currentUid;
        private long                            dataSourceId;
        private Map<UmiTypes, Object>           levels;
        private boolean                         deepParser;

        private Builder(){
        }

        public Builder currentUid(String currentUid) {
            this.currentUid = currentUid;
            return this;
        }

        public Builder dataSourceId(long dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }

        public Builder levels(Map<UmiTypes, Object> levels) {
            this.levels = levels;
            return this;
        }

        public Builder deepParser(boolean deepParser) {
            this.deepParser = deepParser;
            return this;
        }

        public Builder skip(QueryAnalysisFeature first, QueryAnalysisFeature... others) {
            this.skippedFeatures.add(first);
            this.skippedFeatures.addAll(Arrays.asList(others));
            return this;
        }

        public QueryAnalysisOptions build() {
            return new QueryAnalysisOptions(this);
        }
    }
}
