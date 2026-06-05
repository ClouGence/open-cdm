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
package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.Set;

import com.clougence.clouddm.sdk.language.DsLanguageSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsLanguage {

    private boolean supported;
    private boolean completion;
    private boolean validate;
    private boolean split;
    private Set<DsLanguageSupport> supports;
    private String  keywordResource;

    @Override
    public DsLanguage clone() {
        DsLanguage language = new DsLanguage();
        language.setSupported(this.supported);
        language.setCompletion(this.completion);
        language.setValidate(this.validate);
        language.setSplit(this.split);
        language.setSupports(this.supports == null ? null : Set.copyOf(this.supports));
        language.setKeywordResource(this.keywordResource);
        return language;
    }
}
