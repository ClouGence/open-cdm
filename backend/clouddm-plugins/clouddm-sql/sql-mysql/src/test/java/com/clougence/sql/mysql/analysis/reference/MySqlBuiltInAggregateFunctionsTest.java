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
package com.clougence.sql.mysql.analysis.reference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MySqlBuiltInAggregateFunctionsTest {

    @Test
    public void shouldRespectVersionBoundaries() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();

        Assertions.assertTrue(registry.isBuiltInAggregateFunction("`count`", 50600));
        Assertions.assertFalse(registry.isBuiltInAggregateFunction("JSON_ARRAYAGG", 50721));
        Assertions.assertTrue(registry.isBuiltInAggregateFunction("json_arrayagg", 50722));
        Assertions.assertFalse(registry.isBuiltInAggregateFunction("ST_COLLECT", 80023));
        Assertions.assertTrue(registry.isBuiltInAggregateFunction("st_collect", 80024));
    }

    @Test
    public void everyRegisteredFunctionMustHaveOfficialEvidence() throws IOException {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();
        Set<String> registered = registry.registeredAggregateFunctions(99999);
        Set<String> evidenced = loadEvidenceNames();

        Assertions.assertEquals(evidenced, registered,
                "registry and official evidence ledger must contain exactly the same normalized names");
    }

    private static Set<String> loadEvidenceNames() throws IOException {
        String resource = "behavior/mysql/_evidence/built-in-aggregate-functions.csv";
        InputStream input = MySqlBuiltInAggregateFunctionsTest.class.getClassLoader().getResourceAsStream(resource);
        Assertions.assertNotNull(input, "missing evidence ledger: " + resource);

        Set<String> names = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (!line.isBlank()) {
                    String[] columns = line.split(",", -1);
                    Assertions.assertTrue(columns.length >= 6, "invalid evidence row: " + line);
                    Assertions.assertTrue(columns[3].startsWith("https://dev.mysql.com/")
                                    || columns[3].startsWith("https://downloads.mysql.com/"),
                            "evidence URL must point to the database vendor website: " + line);
                    Assertions.assertFalse(columns[4].isBlank(), "evidence document section is required: " + line);
                    Assertions.assertFalse(columns[5].isBlank(), "evidence verification date is required: " + line);
                    Assertions.assertTrue(names.add(columns[1]), "duplicate evidence name: " + columns[1]);
                }
            }
        }
        return names;
    }
}
