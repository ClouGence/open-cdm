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
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.sql.postgres.parser.PostgresVersion;

class PgDropIndexTest {

    private List<RuleDomain> resolve(String sql) {
        PgSecDomainResolveSpi spi = new PgSecDomainResolveSpi(null, PostgresVersion.LATEST);
        try (StringReader reader = new StringReader(sql); Stream<RuleDomain> stream = spi.resolveDomainStream(DataSourceType.PostgreSQL, reader, 0, 0, null)) {
            return stream.toList();
        }
    }

    private RdbIndexDomain resolveIndex(String sql) {
        List<RuleDomain> domains = resolve(sql);
        Assertions.assertEquals(1, domains.size());
        return (RdbIndexDomain) domains.get(0);
    }

    @Test
    void dropIndexBasic() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX idx_assets_asset_serial_number;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals(SecQueryKind.DROP, domain.getAuditKind());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropIndexIfExists() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX IF EXISTS idx_assets_asset_serial_number;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropIndexConcurrently() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX CONCURRENTLY idx_assets_asset_serial_number;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropIndexConcurrentlyIfExists() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX CONCURRENTLY IF EXISTS idx_assets_asset_serial_number;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropIndexCascade() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX idx_assets_asset_serial_number CASCADE;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropIndexWithQualifiedName() {
        RdbIndexDomain domain = resolveIndex("DROP INDEX public.idx_assets_asset_serial_number;");

        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void dropMultipleIndexes() {
        List<RuleDomain> domains = resolve("DROP INDEX idx_a, idx_b;");

        Assertions.assertEquals(2, domains.size());
        Assertions.assertEquals("idx_a", ((RdbIndexDomain) domains.get(0)).getName());
        Assertions.assertEquals("idx_b", ((RdbIndexDomain) domains.get(1)).getName());
    }

    @Test
    void createUniqueIndexWithWhere() {
        RdbIndexDomain domain = resolveIndex("CREATE UNIQUE INDEX idx_assets_asset_uuid_active ON assets (asset_uuid) WHERE retired = false;");

        Assertions.assertEquals(RuleQueryType.ADD_INDEX, domain.getSqlType());
        Assertions.assertEquals(SecQueryKind.CREATE, domain.getAuditKind());
        Assertions.assertEquals("idx_assets_asset_uuid_active", domain.getName());
        Assertions.assertEquals("assets", domain.getTableName());
        Assertions.assertEquals("unique", domain.getType());
        Assertions.assertEquals(List.of("asset_uuid"), domain.getColumns());
    }

    @Test
    void createUniqueIndexIfNotExistsWithWhere() {
        RdbIndexDomain domain = resolveIndex("CREATE UNIQUE INDEX IF NOT EXISTS idx_assets_asset_uuid_active ON assets (asset_uuid) WHERE retired = false;");

        Assertions.assertEquals(RuleQueryType.ADD_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_uuid_active", domain.getName());
        Assertions.assertEquals("assets", domain.getTableName());
        Assertions.assertEquals("unique", domain.getType());
        Assertions.assertEquals(List.of("asset_uuid"), domain.getColumns());
    }

    @Test
    void createIndexConcurrently() {
        RdbIndexDomain domain = resolveIndex("CREATE INDEX CONCURRENTLY idx_assets_asset_serial_number ON assets (asset_serial_number);");

        Assertions.assertEquals(RuleQueryType.ADD_INDEX, domain.getSqlType());
        Assertions.assertEquals("idx_assets_asset_serial_number", domain.getName());
    }

    @Test
    void multiStatementDropAndCreate() {
        String sql = "DROP INDEX idx_assets_asset_serial_number;\n"
                     + "CREATE UNIQUE INDEX idx_assets_asset_uuid_active ON assets (asset_uuid) WHERE retired = false;";

        List<RuleDomain> domains = resolve(sql);

        Assertions.assertEquals(2, domains.size());
        Assertions.assertEquals(RuleQueryType.DROP_INDEX, domains.get(0).getSqlType());
        Assertions.assertEquals(RuleQueryType.ADD_INDEX, domains.get(1).getSqlType());
        Assertions.assertEquals("idx_assets_asset_uuid_active", ((RdbIndexDomain) domains.get(1)).getName());
    }
}
