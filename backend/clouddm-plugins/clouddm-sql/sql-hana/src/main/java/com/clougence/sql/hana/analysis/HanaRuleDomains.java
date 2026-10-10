/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.Map;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;

final class HanaRuleDomains {
    private HanaRuleDomains() {}

    static RuleDomain objectDomain(TargetType type, Map<TargetType, String> resource) {
        String catalog = resource.get(TargetType.Catalog);
        String schema = resource.get(TargetType.Schema);
        String name = resource.get(type);
        switch (type) {
            case Schema: {
                RdbSchemaDomain domain = new RdbSchemaDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                return domain;
            }
            case Table: {
                RdbTableDomain domain = new RdbTableDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setTable(name);
                return domain;
            }
            case View: {
                RdbViewDomain domain = new RdbViewDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setView(name);
                return domain;
            }
            case Sequence: {
                RdbSequenceDomain domain = new RdbSequenceDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setName(name);
                return domain;
            }
            case Synonym: {
                RdbSynonymDomain domain = new RdbSynonymDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setName(name);
                return domain;
            }
            case Procedure: {
                RdbProcedureDomain domain = new RdbProcedureDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setName(name);
                return domain;
            }
            case Function: {
                RdbFunctionDomain domain = new RdbFunctionDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setName(name);
                return domain;
            }
            case Trigger: {
                RdbTriggerDomain domain = new RdbTriggerDomain();
                domain.setCatalog(catalog);
                domain.setSchema(schema);
                domain.setName(name);
                return domain;
            }
            default: throw new IllegalArgumentException("Unsupported HANA rule target: " + type);
        }
    }
}
