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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.KeyValueDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class OptionsBuilder extends AbstractDomainBuilder {

    Map<String, String> options = new HashMap<>();

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new OptionsDomain(options));
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.KEY_VALUE) {
            KeyValueDomain domain = (KeyValueDomain) list.get(0);
            String put = options.put(domain.getKey(), domain.getValue());
            if (put != null) {
                throw new UnsupportedOperationException("options contains same option: " + domain.getKey());
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
