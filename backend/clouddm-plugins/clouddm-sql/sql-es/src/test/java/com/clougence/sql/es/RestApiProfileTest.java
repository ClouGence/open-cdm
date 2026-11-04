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
package com.clougence.sql.es;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;

public class RestApiProfileTest {
    @Test
    public void pitAndStableOrderingHaveDifferentBoundaries() {
        assertFalse(profile("2.3.0").capabilities().pointInTime());
        assertTrue(profile("2.4.0").capabilities().pointInTime());
        for (String version : new String[] { "2.4.0", "2.19.0", "3.0.0", "3.2.0" }) {
            assertFalse(profile(version).capabilities().pitShardDoc());
            assertTrue(profile(version).excludedSorts().contains("_shard_doc"));
        }
        for (String version : new String[] { "3.3.0", "3.9.0", "3.9.0-SNAPSHOT", "3.9.0+build.1" }) {
            assertTrue(profile(version).capabilities().pitShardDoc());
            assertFalse(profile(version).capabilities().mappingTypes());
            assertNull(profile(version).capabilities().compatibleApiMajor());
            assertFalse(profile(version).capabilities().searchKnn());
        }
    }

    @Test
    public void missingVersionDoesNotEnableVersionSpecificOpenSearchFeatures() {
        var profile = RestApiProfile.from(RestProduct.OPENSEARCH.parameters(SqlParserParameters.empty()));
        assertFalse(profile.capabilities().pointInTime());
        assertFalse(profile.capabilities().pitShardDoc());
        assertFalse(profile.capabilities().mappingTypes());
        assertEquals("cluster_manager_timeout", profile.preferredManagerTimeout());
        assertNull(RestApiProfile.from(SqlParserParameters.empty()).capabilities());
    }

    @Test
    public void productAndVersionCannotBeConfused() {
        for (String number : new String[] { "1.3.0", "4.0.0", "8.19.0", "3.3", "3.3.0 garbage", "3.99999999999.0" }) {
            try {
                profile(number);
                fail(number);
            } catch (ThirdPartyApiException expected) {
                assertEquals(EsSqlI18nKeys.OPENSEARCH_SERVER_VERSION_INVALID, expected.getMessageKey());
            }
        }
        try {
            RestApiProfile.from(new SqlParserParameters(Map.of(RestProduct.PARAMETER, "unknown")));
            fail();
        } catch (ThirdPartyApiException expected) {
            assertEquals(EsSqlI18nKeys.REST_PRODUCT_INVALID, expected.getMessageKey());
        }
    }

    @Test
    public void productIdentityIsExplicitAndOverridesUntrustedParameters() {
        var parameters = RestProduct.OPENSEARCH.parameters(RestProduct.ELASTICSEARCH.parameters("3.9.0"));
        assertEquals("OPENSEARCH", parameters.get(RestProduct.PARAMETER));
        assertEquals("3.9.0", parameters.version());
        assertEquals(OpenSearchSqlEngineSpi.NAME, RestApiProfile.from(parameters).engineName());
    }

    private static RestApiProfile profile(String version) {
        return RestApiProfile.from(RestProduct.OPENSEARCH.parameters(version));
    }
}
