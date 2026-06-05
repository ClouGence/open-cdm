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
package com.clougence.detectrule.parser.antlr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.detectrule.parser.DetectRulesFeature;

public abstract class DetectRulesBasicParser extends Parser {

    private final Set<DetectRulesFeature> feature = new HashSet<>();

    public DetectRulesBasicParser(TokenStream input){
        super(input);
        this.initFeatures(null);
    }

    public boolean support(DetectRulesFeature feature) {
        return this.feature.contains(feature);
    }

    protected void initFeatures(List<DetectRulesFeature> features) {
        if (features != null) {
            this.feature.addAll(features);
        }
    }
}
