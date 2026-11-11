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
package com.clougence.sql.postgres.analysis.security;

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
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PgIndexDdlSecurityTest {

    private static final String         CURRENT_CATALOG = "postgres";
    private static final String         CURRENT_SCHEMA  = "public";

    private final PgSecDomainResolveSpi resolveSpi      = new PgSecDomainResolveSpi(metaService(), PostgresVersion.LATEST);

    @Test
    public void dropIndexFormsResolveOwnerTable() {
        String[] statements = { "DROP INDEX accounts_email_idx;", "DROP INDEX IF EXISTS accounts_email_idx;", "DROP INDEX CONCURRENTLY accounts_email_idx;",
                                "DROP INDEX CONCURRENTLY IF EXISTS accounts_email_idx;" };
        for (String sql : statements) {
            List<RuleDomain> domains = resolve(sql);
            Assert.assertEquals(sql, 1, domains.size());

            RdbIndexDomain index = (RdbIndexDomain) domains.get(0);
            Assert.assertEquals(sql, RuleQueryType.DROP_INDEX, index.getSqlType());
            Assert.assertEquals(sql, "accounts_email_idx", index.getName());
            Assert.assertEquals(sql, CURRENT_CATALOG, index.getCatalog());
            Assert.assertEquals(sql, CURRENT_SCHEMA, index.getSchema());
            Assert.assertEquals(sql, CURRENT_SCHEMA, index.getTableSchema());
            Assert.assertEquals(sql, "accounts", index.getTableName());
        }
    }

    @Test
    public void schemaQualifiedIndexResolvesOwnerInItsSchema() {
        List<RuleDomain> domains = resolve("DROP INDEX sensitive.accounts_email_idx;");
        Assert.assertEquals(1, domains.size());

        RdbIndexDomain index = (RdbIndexDomain) domains.get(0);
        Assert.assertEquals("sensitive", index.getSchema());
        Assert.assertEquals("sensitive", index.getTableSchema());
        Assert.assertEquals("accounts", index.getTableName());
    }

    @Test
    public void multipleIndexesEmitOneDomainEach() {
        List<RuleDomain> domains = resolve("DROP INDEX idx_a, idx_b;");
        Assert.assertEquals(2, domains.size());
        Assert.assertEquals("table_a", ((RdbIndexDomain) domains.get(0)).getTableName());
        Assert.assertEquals("table_b", ((RdbIndexDomain) domains.get(1)).getTableName());
    }

    @Test
    public void quotedIndexNamesAreUnquoted() {
        List<RuleDomain> domains = resolve("DROP INDEX \"Sensitive\".\"AccountsIdx\";");
        Assert.assertEquals(1, domains.size());

        RdbIndexDomain index = (RdbIndexDomain) domains.get(0);
        Assert.assertEquals("AccountsIdx", index.getName());
        Assert.assertEquals("Sensitive", index.getSchema());
        Assert.assertEquals("Accounts", index.getTableName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unresolvedIndexIsRejected() {
        resolve("DROP INDEX missing_idx;");
    }

    @Test
    public void createIndexKeepsTableAndPredicateSupport() {
        List<RuleDomain> domains = resolve("CREATE UNIQUE INDEX idx ON sensitive.accounts (email) WHERE email IS NOT NULL;");
        Assert.assertEquals(1, domains.size());

        RdbIndexDomain index = (RdbIndexDomain) domains.get(0);
        Assert.assertEquals(RuleQueryType.ADD_INDEX, index.getSqlType());
        Assert.assertEquals("unique", index.getType());
        Assert.assertEquals("idx", index.getName());
        Assert.assertEquals("sensitive", index.getTableSchema());
        Assert.assertEquals("accounts", index.getTableName());
        Assert.assertEquals(List.of("email"), index.getColumns());
    }

    @Test
    public void createIndexIfNotExistsKeepsIndexName() {
        List<RuleDomain> domains = resolve("CREATE INDEX IF NOT EXISTS idx ON accounts (email);");
        Assert.assertEquals(1, domains.size());

        RdbIndexDomain index = (RdbIndexDomain) domains.get(0);
        Assert.assertEquals("idx", index.getName());
        Assert.assertEquals("accounts", index.getTableName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void nonIndexDropStaysUnsupported() {
        resolve("DROP SEQUENCE accounts_seq;");
    }

    private List<RuleDomain> resolve(String sql) {
        try (StringReader reader = new StringReader(sql); Stream<RuleDomain> stream = this.resolveSpi.resolveDomainStream(DataSourceType.PostgreSQL, reader, 1, 0, contextInfo())) {
            return stream.toList();
        }
    }

    private static ContextInfo contextInfo() {
        return ContextInfo.builder().cuid("tester").dsId(1).levelsParam(Map.of(UmiTypes.Catalog, CURRENT_CATALOG, UmiTypes.Schema, CURRENT_SCHEMA)).build();
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
        owners.put("public.idx_a", owner(CURRENT_SCHEMA, "table_a"));
        owners.put("public.idx_b", owner(CURRENT_SCHEMA, "table_b"));
        owners.put("Sensitive.AccountsIdx", owner("Sensitive", "Accounts"));
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
