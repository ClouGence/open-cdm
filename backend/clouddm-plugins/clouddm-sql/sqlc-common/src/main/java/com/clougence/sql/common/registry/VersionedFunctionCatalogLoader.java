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
package com.clougence.sql.common.registry;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/** Loads versioned built-in function names from a compact module-owned catalog. */
public final class VersionedFunctionCatalogLoader {

    private VersionedFunctionCatalogLoader(){
    }

    public static void load(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> registry) {
        InputStream input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing registered function catalog: " + resource);
        }
        Set<String> names = new HashSet<>();
        try (input; BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String value = line.trim();
                if (value.isEmpty() || value.startsWith("#")) {
                    continue;
                }
                register(resource, lineNumber, value, names, registry);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load registered function catalog: " + resource, e);
        }
    }

    private static void register(String resource, int lineNumber, String line, Set<String> names, VersionedResourceRegistry<Boolean> registry) {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 2 || fields[1].isBlank()) {
            throw invalid(resource, lineNumber, "expected <minimum>-<maximum>|<function>");
        }
        String[] versions = fields[0].split("-", -1);
        if (versions.length != 2) {
            throw invalid(resource, lineNumber, "expected a version range");
        }
        try {
            int minimumVersion = Integer.parseInt(versions[0]);
            int maximumVersion = Integer.parseInt(versions[1]);
            String name = fields[1].trim();
            if (!names.add(name)) {
                throw invalid(resource, lineNumber, "duplicate function: " + name);
            }
            registry.register(FUNCTION, minimumVersion, maximumVersion, true, name);
        } catch (NumberFormatException e) {
            throw invalid(resource, lineNumber, "invalid version range");
        }
    }

    private static IllegalStateException invalid(String resource, int lineNumber, String message) {
        return new IllegalStateException("Invalid registered function catalog " + resource + ":" + lineNumber + ": " + message);
    }
}
