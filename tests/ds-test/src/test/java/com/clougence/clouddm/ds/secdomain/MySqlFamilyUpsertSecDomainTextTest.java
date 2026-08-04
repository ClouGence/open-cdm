package com.clougence.clouddm.ds.secdomain;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;

public final class MySqlFamilyUpsertSecDomainTextTest {

    private static final String       RESOURCE    = "secdomain/mysql/upsert_classification.txt";
    private static final List<String> DATASOURCES = List.of("mysql", "mariadb", "adb", "ob4my", "por4my", "por4x", "tidb");

    @TestFactory
    public Stream<DynamicTest> upsertClassification() {
        return DATASOURCES.stream().flatMap(datasource -> {
            SecDomainResolveSpi spi = SqlTestSupport.sqlEngine(datasource).secDomainResolveSpi(SqlParserParameters.empty());
            ContextInfo context = SqlTestSupport.contextInfo(datasource);
            return SecDomainTextTest.loadCases(RESOURCE)
                .stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase
                    .displayName(datasource), () -> SecDomainTextTest.assertCase(RESOURCE, testCase, SqlTestSupport.dataSourceType(datasource), spi, context)));
        });
    }
}
