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
package com.clougence.clouddm.dsfamily.db2.language;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.sdk.language.DsLanguageSupport;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.db2.Db2SqlEngineSpi;

public class Db2LanguageSpiTest implements MetaService {
    @Test
    public void validatesCommonStatements() {
        Db2LanguageSpi language = new Db2LanguageSpi(null);
        assertTrue(language.supports().contains(DsLanguageSupport.VALIDATE));
        for (String sql : List
            .of("SELECT * FROM T1", "SELECT * FROM T1 FETCH FIRST 10 ROWS ONLY", "WITH C AS (SELECT ID FROM T1) SELECT ID FROM C", "UPDATE T1 SET ID = 2 WHERE ID = 1", "SELECT \"ID\" FROM \"SCHEMA\".\"T1\"")) {
            ValidateRequest request = new ValidateRequest();
            request.setSqlEngine(new Db2SqlEngineSpi());
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
        request.setSqlEngine(new Db2SqlEngineSpi());
        request.setSqlParameters(Map.of());
        request.setSqlText("SELECT * FROM");
        assertFalse(new Db2LanguageSpi(null).validate(request).getDiagnostics().isEmpty());
    }

    @Test
    public void completesColumns() {
        Db2LanguageSpi language = new Db2LanguageSpi(this);
        assertTrue(language.supports().contains(DsLanguageSupport.COMPLETE));
        for (String sql : List
            .of("SELECT * FROM \"SCHEMA\".\"T1\" WHERE ", "SELECT * FROM \"SCHEMA\".\"T1\" ORDER BY ", "SELECT I FROM \"SCHEMA\".\"T1\"", "SELECT t1. FROM \"SCHEMA\".\"T1\" t1")) {
            CompletionRequest request = new CompletionRequest();
            request.setSqlEngine(new Db2SqlEngineSpi());
            request.setSqlParameters(Map.of());
            request.setDataSourceId(1L);
            request.setSqlText(sql);
            request.setCursorLineNumber(1);
            int offset = sql.length();
            if (sql.startsWith("SELECT I ")) {
                offset = "SELECT I".length();
            } else if (sql.startsWith("SELECT t1.")) {
                offset = "SELECT t1.".length();
            }

            request.setCursorColNumber(offset);
            request.setRequestId("complete");
            request.setRequestVersion(3);
            var result = language.complete(request);
            assertTrue(sql, result.getItems().stream().anyMatch(item -> "ID".equals(item.getInsertText())));
            assertEquals("complete", result.getRequestId());
            assertEquals(3, result.getRequestVersion());
        }
    }

    @Test
    public void splitsStatements() {
        Db2LanguageSpi language = new Db2LanguageSpi(null);
        assertTrue(language.supports().contains(DsLanguageSupport.SPLIT));
        SplitRequest request = new SplitRequest();
        request.setSqlEngine(new Db2SqlEngineSpi());
        request.setSqlParameters(Map.of());
        request.setSqlText("SELECT ';' FROM T1; SELECT ID FROM T1;");
        request.setBasicCodeLine(1);
        request.setRequestId("split");
        request.setRequestVersion(4);
        var result = language.split(request);
        assertEquals(2, result.getStatements().size());
        assertTrue(result.getStatements().get(0).getSql().contains("';'"));
        assertEquals("split", result.getRequestId());
        assertEquals(4, result.getRequestVersion());
    }

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
        assertTrue(tableName, tableName.contains("T1"));
        MetaCol column = new MetaCol();
        column.setColumn("ID");
        return List.of(column);
    }

    @Override
    public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return List.of();
    }
}
