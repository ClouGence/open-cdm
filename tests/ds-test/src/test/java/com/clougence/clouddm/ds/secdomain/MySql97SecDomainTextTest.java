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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.sql.mysql.analysis.security.MySecDomainResolveSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

public final class MySql97SecDomainTextTest {

    private static final String RESOURCE_DIRECTORY = "secdomain/mysql/9.7";

    @TestFactory
    public Stream<DynamicTest> secDomainScripts() {
        MySecDomainResolveSpi spi = new MySecDomainResolveSpi(SqlTestSupport.metaService(), MySqlParserConfig.unknownSqlMode("9.7.1"));
        ContextInfo context = ContextInfo.builder().build();
        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : SecDomainTextTest.listResourceFiles(RESOURCE_DIRECTORY)) {
            for (SecDomainTextTest.TestCase testCase : SecDomainTextTest.loadCases(resourcePath)) {
                tests.add(DynamicTest
                    .dynamicTest(testCase.displayName("mysql/9.7"), () -> SecDomainTextTest.assertCase(resourcePath, testCase, DataSourceType.MySQL, spi, context)));
            }
        }
        return tests.stream();
    }
}
