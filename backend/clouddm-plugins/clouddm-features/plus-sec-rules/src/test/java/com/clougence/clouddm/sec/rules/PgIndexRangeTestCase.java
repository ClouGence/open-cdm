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
package com.clougence.clouddm.sec.rules;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaIndexedObject;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.CheckerData;
import com.clougence.clouddm.sdk.service.secrules.CheckerRange;
import com.clougence.clouddm.sdk.service.secrules.MatchMode;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sec.rules.execute.Utils;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.analysis.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PgIndexRangeTestCase {

    private static final String         CURRENT_CATALOG = "postgres";
    private static final String         CURRENT_SCHEMA  = "public";

    private final PgSecDomainResolveSpi resolveSpi      = new PgSecDomainResolveSpi(metaService(), PostgresVersion.LATEST);

    @Test
    public void crossSchemaIndexMatchesSchemaAndTableRanges() {
        String sql = "DROP INDEX sensitive.accounts_email_idx;";
        RuleDomain domain = resolve(sql).get(0);

        Assert.assertTrue(Utils.checkRangeIncludeDomain(range(TargetType.Schema, "/1/2/" + CURRENT_CATALOG, "sensitive"), checkerData(sql, domain)));
        Assert.assertTrue(Utils.checkRangeIncludeDomain(range(TargetType.Table, "/1/2/" + CURRENT_CATALOG + "/sensitive", "accounts"), checkerData(sql, domain)));
        Assert.assertFalse(Utils.checkRangeIncludeDomain(range(TargetType.Schema, "/1/2/" + CURRENT_CATALOG, CURRENT_SCHEMA), checkerData(sql, domain)));
    }

    @Test
    public void unqualifiedIndexUsesCurrentSchema() {
        String sql = "DROP INDEX accounts_email_idx;";
        RuleDomain domain = resolve(sql).get(0);

        Assert.assertTrue(Utils.checkRangeIncludeDomain(range(TargetType.Schema, "/1/2/" + CURRENT_CATALOG, CURRENT_SCHEMA), checkerData(sql, domain)));
        Assert.assertTrue(Utils.checkRangeIncludeDomain(range(TargetType.Table, "/1/2/" + CURRENT_CATALOG + "/" + CURRENT_SCHEMA, "accounts"), checkerData(sql, domain)));
        Assert.assertFalse(Utils.checkRangeIncludeDomain(range(TargetType.Schema, "/1/2/" + CURRENT_CATALOG, "sensitive"), checkerData(sql, domain)));
    }

    @Test
    public void ifExistsMissingIndexStillMatchesSchemaRange() {
        String sql = "DROP INDEX CONCURRENTLY IF EXISTS missing_idx;";
        RuleDomain domain = resolve(sql).get(0);

        Assert.assertTrue(Utils.checkRangeIncludeDomain(range(TargetType.Schema, "/1/2/" + CURRENT_CATALOG, CURRENT_SCHEMA), checkerData(sql, domain)));
        Assert.assertFalse(Utils.checkRangeIncludeDomain(range(TargetType.Table, "/1/2/" + CURRENT_CATALOG + "/" + CURRENT_SCHEMA, "accounts"), checkerData(sql, domain)));
    }

    private List<RuleDomain> resolve(String sql) {
        try (StringReader reader = new StringReader(sql); Stream<RuleDomain> stream = this.resolveSpi.resolveDomainStream(DataSourceType.PostgreSQL, reader, 1, 0, contextInfo())) {
            return stream.toList();
        }
    }

    private static ContextInfo contextInfo() {
        return ContextInfo.builder().cuid("tester").dsId(1).levelsParam(Map.of(UmiTypes.Catalog, CURRENT_CATALOG, UmiTypes.Schema, CURRENT_SCHEMA)).build();
    }

    private static CheckerData checkerData(String sql, RuleDomain domain) {
        domain.setEnvId(1);
        domain.setDsId(2);

        CheckerData data = new CheckerData(sql, domain);
        data.setDsLevelsDef(List.of(UmiTypes.Catalog, UmiTypes.Schema));
        data.setCurrentCatalog(CURRENT_CATALOG);
        data.setCurrentSchema(CURRENT_SCHEMA);
        return data;
    }

    private static CheckerRange range(TargetType scope, String levelPrefix, String node) {
        CheckerRange range = new CheckerRange();
        range.setScope(scope);
        range.setMatchMode(MatchMode.EXACT);
        range.setLevelPrefix(levelPrefix);
        range.setLevelNodes(List.of(node));
        range.setChooseAll(false);
        return range;
    }

    private static MetaService metaService() {
        return new MetaService() {

            private final Map<String, MetaIndexedObject> owners = owners();

            @Override
            public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
                return List.of();
            }

            @Override
            public MetaIndexedObject fetchIndexedObject(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String indexName) {
                String schema = String.valueOf(levelsParam.get(UmiTypes.Schema));
                return this.owners.get(schema + "." + indexName);
            }

            @Override
            public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
                return List.of();
            }
        };
    }

    private static Map<String, MetaIndexedObject> owners() {
        Map<String, MetaIndexedObject> owners = new HashMap<>();
        owners.put("public.accounts_email_idx", owner(CURRENT_SCHEMA, "accounts"));
        owners.put("sensitive.accounts_email_idx", owner("sensitive", "accounts"));
        return owners;
    }

    private static MetaIndexedObject owner(String schema, String table) {
        MetaIndexedObject owner = new MetaIndexedObject();
        owner.setType(UmiTypes.Table);
        owner.setCatalog(CURRENT_CATALOG);
        owner.setSchema(schema);
        owner.setName(table);
        return owner;
    }
}
