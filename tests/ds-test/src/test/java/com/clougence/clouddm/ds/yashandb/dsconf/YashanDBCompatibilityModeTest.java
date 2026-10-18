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
package com.clougence.clouddm.ds.yashandb.dsconf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * Locks down the variant ↔ compatibility mode ↔ default port mapping.
 * <p>
 * YashanDB decides its compatibility mode at install time, and the two modes are reached over different ports with
 * different drivers, so the mapping has to stay 1:1 with {@link DataSourceType}.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBCompatibilityModeTest {

    @Test
    void fromDataSourceType_mapsEachVariant() {
        assertEquals(YashanDBCompatibilityMode.ORACLE, YashanDBCompatibilityMode.fromDataSourceType(DataSourceType.YashanDBOracle));
        assertEquals(YashanDBCompatibilityMode.MYSQL, YashanDBCompatibilityMode.fromDataSourceType(DataSourceType.YashanDBMySQL));
    }

    @Test
    void fromDataSourceType_rejectsForeignDataSource() {
        assertThrows(IllegalArgumentException.class, //
                () -> YashanDBCompatibilityMode.fromDataSourceType(DataSourceType.MySQL));
    }

    @Test
    void fromServerMode_isCaseInsensitiveAndTrimmed() {
        assertEquals(YashanDBCompatibilityMode.ORACLE, YashanDBCompatibilityMode.fromServerMode("oracle"));
        assertEquals(YashanDBCompatibilityMode.ORACLE, YashanDBCompatibilityMode.fromServerMode(" ORACLE "));
        assertEquals(YashanDBCompatibilityMode.MYSQL, YashanDBCompatibilityMode.fromServerMode("MySQL"));
    }

    @Test
    void fromServerMode_rejectsUnknownMode() {
        assertThrows(IllegalArgumentException.class, //
                () -> YashanDBCompatibilityMode.fromServerMode("postgresql"));
    }

    @Test
    void defaultPort_matchesEachMode() {
        assertEquals("1688", YashanDBCompatibilityMode.ORACLE.getDefaultPort());
        assertEquals("1690", YashanDBCompatibilityMode.MYSQL.getDefaultPort());
    }
}
