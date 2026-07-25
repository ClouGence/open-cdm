package com.clougence.clouddm.ds.secdomain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.sql.mysql.analysis.security.MySecDomainResolveSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

public final class MySql97SecDomainTextTest {

    private static final String RESOURCE_DIRECTORY = "secdomain/mysql/9.7";

    @TestFactory
    public Stream<DynamicTest> secDomainScripts() {
        MySecDomainResolveSpi spi = new MySecDomainResolveSpi(
                SqlTestSupport.metaService(), MySqlParserConfig.unknownSqlMode("9.7.1"));
        ContextInfo context = ContextInfo.builder().deepParser(false).build();
        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : SecDomainTextTest.listResourceFiles(RESOURCE_DIRECTORY)) {
            for (SecDomainTextTest.TestCase testCase : SecDomainTextTest.loadCases(resourcePath)) {
                tests.add(DynamicTest.dynamicTest(testCase.displayName("mysql/9.7"),
                    () -> SecDomainTextTest.assertCase(resourcePath, testCase, DataSourceType.MySQL, spi, context)));
            }
        }
        return tests.stream();
    }
}
