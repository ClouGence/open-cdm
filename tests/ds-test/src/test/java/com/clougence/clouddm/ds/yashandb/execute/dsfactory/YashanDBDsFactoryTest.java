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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.clougence.drivers.DsConfigKeys;

/**
 * Locks down {@link YashanDBDsFactory#buildJdbcUrl(Properties)}.
 * <p>
 * YashanDB only exposes a database-name form of the URL ({@code jdbc:yasdb://host:port/database}); there is no
 * SID/service-name variant like Oracle, so the database name is mandatory and the port falls back to {@code 1688}.
 * These rules are pure string handling and are covered here without a live instance.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBDsFactoryTest {

    private final YashanDBDsFactory factory = new YashanDBDsFactory();

    private static Properties config(String host, String database) {
        Properties props = new Properties();
        if (host != null) {
            props.setProperty(DsConfigKeys.HOST.getConfigKey(), host);
        }
        if (database != null) {
            props.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), database);
        }
        return props;
    }

    @Test
    void buildJdbcUrl_hostWithPort_keepsGivenPort() {
        String url = this.factory.buildJdbcUrl(config("10.9.18.73:1688", "yashandb"));
        assertEquals("jdbc:yasdb://10.9.18.73:1688/yashandb", url);
    }

    @Test
    void buildJdbcUrl_hostWithoutPort_fallsBackToDefaultPort() {
        String url = this.factory.buildJdbcUrl(config("10.9.18.73", "yashandb"));
        assertEquals("jdbc:yasdb://10.9.18.73:1688/yashandb", url);
    }

    @Test
    void buildJdbcUrl_customUrl_winsOverHostAndDatabase() {
        Properties props = config("10.9.18.73:1688", "yashandb");
        props.setProperty(DsConfigKeys.CUSTOM_URL.getConfigKey(), "jdbc:yasdb://127.0.0.1:1689/other");
        assertEquals("jdbc:yasdb://127.0.0.1:1689/other", this.factory.buildJdbcUrl(props));
    }

    @Test
    void buildJdbcUrl_missingDatabase_isRejectedWithActionableMessage() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, //
                () -> this.factory.buildJdbcUrl(config("10.9.18.73:1688", null)));
        // the message must point the user at the form field they have to fill in
        assertTrue(error.getMessage().contains("database name is required"), error.getMessage());
        assertTrue(error.getMessage().contains("Catalog"), error.getMessage());
    }

    @Test
    void buildJdbcUrl_unsupportedHostFormat_isRejected() {
        assertThrows(IllegalArgumentException.class, //
                () -> this.factory.buildJdbcUrl(config("a:b:c", "yashandb")));
    }
}
