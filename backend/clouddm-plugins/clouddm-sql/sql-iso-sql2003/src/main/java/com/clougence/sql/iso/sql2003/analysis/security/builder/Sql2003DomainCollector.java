/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003.analysis.security.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public class Sql2003DomainCollector {

    private final List<RuleDomain> domains = new ArrayList<>();

    public void add(RuleDomain domain) {
        domain.setOptions(Collections.emptyMap());
        domains.add(domain);
    }

    public List<RuleDomain> build() {
        return domains;
    }
}
