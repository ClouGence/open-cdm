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
package com.clougence.clouddm.dsfamily.language.validate;

import java.util.List;

import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class SyntaxValidateStrategy implements ValidateStrategy {

    @Override
    public boolean match(ValidateContext context) {
        return context.hasSyntaxError();
    }

    @Override
    public List<Diagnostic> validate(ValidateContext context, MetaService metaService) {
        return List.of(ValidateDiagnostics.syntaxError(context.getSyntaxError(), context.getSqlText()));
    }

    @Override
    public boolean stopOnDiagnostics() {
        return true;
    }
}
