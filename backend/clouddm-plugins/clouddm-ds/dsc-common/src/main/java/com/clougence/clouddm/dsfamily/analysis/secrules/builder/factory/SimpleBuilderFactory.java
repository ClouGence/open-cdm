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
package com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class SimpleBuilderFactory {

    protected final Stack<DomainBuilder>    domainStack = new Stack<>();
    protected final List<RuleDomain>        ruleDomains = new ArrayList<>();
    protected Stack<List<WithSelectDomain>> selectStack = new Stack<>();
    protected MetaService                   metaService;

    public SimpleBuilderFactory(MetaService metaService){
        this.metaService = metaService;
    }

    protected DomainBuilder getCurrentBuilder() { return domainStack.peek(); }

    public void addDomain(RuleDomain ruleDomain) {
        this.ruleDomains.add(ruleDomain);
    }

    public List<RuleDomain> build() {
        List<RuleDomain> domains = new ArrayList<>();
        for (RuleDomain ruleDomain : this.ruleDomains) {
            parseDomain(domains, ruleDomain);
        }
        return domains;
    }

    protected void parseDomain(List<RuleDomain> domains, RuleDomain ruleDomain) {
        domains.add(ruleDomain);
    }
}
