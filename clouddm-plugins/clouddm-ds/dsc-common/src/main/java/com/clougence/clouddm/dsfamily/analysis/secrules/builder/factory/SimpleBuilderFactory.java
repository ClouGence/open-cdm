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
