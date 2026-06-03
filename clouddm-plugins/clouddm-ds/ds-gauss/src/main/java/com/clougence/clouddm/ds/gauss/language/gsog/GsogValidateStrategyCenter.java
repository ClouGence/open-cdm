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
package com.clougence.clouddm.ds.gauss.language.gsog;

import java.util.List;

import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.clouddm.dsfamily.language.validate.SyntaxValidateStrategy;
import com.clougence.clouddm.dsfamily.language.validate.ValidateStrategy;
import com.clougence.clouddm.dsfamily.language.validate.ValidateStrategyCenter;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.dslpaser.antlr.DslProvider;

public class GsogValidateStrategyCenter extends ValidateStrategyCenter {

    @Override
    protected DslProvider dslProvider(ValidateRequest request) {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected void register(List<ValidateStrategy> strategies) {
        strategies.add(new SyntaxValidateStrategy());
    }
}
