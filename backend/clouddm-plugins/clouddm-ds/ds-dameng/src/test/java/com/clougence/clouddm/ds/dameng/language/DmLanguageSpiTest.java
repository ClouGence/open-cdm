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
package com.clougence.clouddm.ds.dameng.language;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.dameng.sql.DmSqlEngineSpi;
import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;

public class DmLanguageSpiTest {
    @Test
    public void validatesCommonStatements() {
        DmLanguageSpi language = new DmLanguageSpi(null);
        assertTrue(language.supports().contains(DsLanguageSupport.VALIDATE));
        for (String sql : List
            .of("SELECT * FROM T1", "SELECT * FROM T1 FETCH FIRST 10 ROWS ONLY", "WITH C AS (SELECT ID FROM T1) SELECT ID FROM C", "UPDATE T1 SET ID = 2 WHERE ID = 1", "SELECT \"ID\" FROM \"SCHEMA\".\"T1\"")) {
            ValidateRequest request = new ValidateRequest();
            request.setSqlEngine(new DmSqlEngineSpi(null));
            request.setSqlParameters(Map.of());
            request.setSqlText(sql);
            request.setRequestId("validate");
            request.setRequestVersion(7);
            var result = language.validate(request);
            assertEquals(sql, List.of(), result.getDiagnostics());
            assertEquals("validate", result.getRequestId());
            assertEquals(7, result.getRequestVersion());
        }
    }

    @Test
    public void reportsInvalidSyntax() {
        ValidateRequest request = new ValidateRequest();
        request.setSqlEngine(new DmSqlEngineSpi(null));
        request.setSqlParameters(Map.of());
        request.setSqlText("SELECT * FROM");
        assertFalse(new DmLanguageSpi(null).validate(request).getDiagnostics().isEmpty());
    }
}
