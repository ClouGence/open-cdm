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
package com.clougence.clouddm.ds.yashandb.execute.dsfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.clougence.drivers.DsConfigKeys;

/**
 * Locks down {@link YashanDBMySQLDsFactory#buildJdbcUrl(Properties)}.
 * <p>
 * The MySQL compatibility mode is reached over YashanDB's standard MySQL protocol port, so the URL is a plain MySQL
 * URL whose port falls back to {@code 1690} (measured on 23.4.1.109). These rules are pure string handling and are
 * covered here without a live instance.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLDsFactoryTest {

    private final YashanDBMySQLDsFactory factory = new YashanDBMySQLDsFactory();

    private static Properties config(String host, String schema, String database) {
        Properties props = new Properties();
        if (host != null) {
            props.setProperty(DsConfigKeys.HOST.getConfigKey(), host);
        }
        if (schema != null) {
            props.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), schema);
        }
        if (database != null) {
            props.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), database);
        }
        return props;
    }

    @Test
    void buildJdbcUrl_hostWithPort_keepsGivenPort() {
        String url = this.factory.buildJdbcUrl(config("10.9.18.77:1690", "test", null));
        assertEquals("jdbc:mysql://10.9.18.77:1690/test", url);
    }

    @Test
    void buildJdbcUrl_hostWithoutPort_fallsBackToDefaultPort() {
        String url = this.factory.buildJdbcUrl(config("10.9.18.77", "test", null));
        assertEquals("jdbc:mysql://10.9.18.77:1690/test", url);
    }

    @Test
    void buildJdbcUrl_customUrl_winsOverHostAndDatabase() {
        Properties props = config("10.9.18.77:1690", "test", null);
        props.setProperty(DsConfigKeys.CUSTOM_URL.getConfigKey(), "jdbc:mysql://127.0.0.1:3306/other");
        assertEquals("jdbc:mysql://127.0.0.1:3306/other", this.factory.buildJdbcUrl(props));
    }

    @Test
    void buildJdbcUrl_schemaWinsOverCatalog() {
        String url = this.factory.buildJdbcUrl(config("10.9.18.77:1690", "fromSchema", "fromCatalog"));
        assertEquals("jdbc:mysql://10.9.18.77:1690/fromSchema", url);
    }

    @Test
    void buildJdbcUrl_catalogUsedWhenSchemaBlank() {
        // the shared YashanDB config exposes both a Catalog and a Schema field; either one may carry the database name
        String url = this.factory.buildJdbcUrl(config("10.9.18.77:1690", null, "fromCatalog"));
        assertEquals("jdbc:mysql://10.9.18.77:1690/fromCatalog", url);
    }

    @Test
    void buildJdbcUrl_withoutDatabase_isStillConnectable() {
        // measured: YashanDB MySQL compatibility mode accepts a connection without a database in the path
        String url = this.factory.buildJdbcUrl(config("10.9.18.77:1690", null, null));
        assertEquals("jdbc:mysql://10.9.18.77:1690/", url);
    }

    @Test
    void buildJdbcUrl_unsupportedHostFormat_isRejected() {
        assertThrows(IllegalArgumentException.class, //
                () -> this.factory.buildJdbcUrl(config("a:b:c", "test", null)));
    }
}
