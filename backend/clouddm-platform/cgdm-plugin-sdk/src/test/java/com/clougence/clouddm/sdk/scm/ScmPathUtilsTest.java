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
package com.clougence.clouddm.sdk.scm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ScmPathUtilsTest {

    @Test
    public void shouldBuildRepositoryPath() {
        assertEquals("database", ScmPathUtils.buildRepoPath(null, "database"));
        assertEquals("database", ScmPathUtils.buildRepoPath(" ", "database"));
        assertEquals("group/sub/database", ScmPathUtils.buildRepoPath("group/sub", "database"));
    }

    @Test
    public void shouldNormalizeRepositoryRelativeDirectory() {
        assertEquals("", ScmPathUtils.normalizeDirectoryPath(null));
        assertEquals("scripts/mysql", ScmPathUtils.normalizeDirectoryPath(" /scripts//./mysql/ "));
        assertEquals("scripts/mysql", ScmPathUtils.normalizeDirectoryPath("scripts\\mysql"));
    }

    @Test
    public void shouldRejectParentTraversalAndNullBytes() {
        assertInvalid("../scripts");
        assertInvalid("scripts/../secret");
        assertInvalid("scripts\u0000/secret");
    }

    private static void assertInvalid(String value) {
        try {
            ScmPathUtils.normalizeDirectoryPath(value);
            fail("path must be rejected: " + value);
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
