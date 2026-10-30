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
package com.clougence.clouddm.ds.secdomain;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;

public final class MySqlFamilyStorageSecDomainTextTest {

    private static final String       RESOURCE    = "secdomain/mysql/storage_ddl.txt";
    private static final List<String> DATASOURCES = List.of("mysql", "mariadb", "adb", "ob4my", "por4my", "por4x", "tidb");

    @TestFactory
    public Stream<DynamicTest> storageClassification() {
        return DATASOURCES.stream().flatMap(datasource -> {
            SecDomainResolveSpi spi = SqlTestSupport.sqlEngine(datasource).secDomainResolveSpi(SqlTestSupport.parserParameters(datasource));
            ContextInfo context = SqlTestSupport.contextInfo(datasource);
            return SecDomainTextTest.loadCases(RESOURCE)
                .stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase
                    .displayName(datasource), () -> SecDomainTextTest.assertCase(RESOURCE, testCase, SqlTestSupport.dataSourceType(datasource), spi, context)));
        });
    }
}
