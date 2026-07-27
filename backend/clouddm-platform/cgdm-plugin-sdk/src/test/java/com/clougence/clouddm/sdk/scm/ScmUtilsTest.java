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

public class ScmUtilsTest {

    @Test
    public void shouldBuildRepositoryPath() {
        assertEquals("database", ScmUtils.buildRepoPath(null, "database"));
        assertEquals("database", ScmUtils.buildRepoPath(" ", "database"));
        assertEquals("group/sub/database", ScmUtils.buildRepoPath("group/sub", "database"));
    }

    @Test
    public void shouldNormalizeRepositoryRelativeDirectory() {
        assertEquals("", ScmUtils.normalizeDirectoryPath(null));
        assertEquals("scripts/mysql", ScmUtils.normalizeDirectoryPath(" /scripts//./mysql/ "));
        assertEquals("scripts/mysql", ScmUtils.normalizeDirectoryPath("scripts\\mysql"));
    }

    @Test
    public void shouldNormalizeGitlabWebUrl() {
        assertEquals("https://gitlab.example.com/group", ScmUtils.normalizeGitlabWebUrl(" https://gitlab.example.com/group/// "));
        assertInvalidUrl("https://gitlab.example.com/api/v4");
        assertInvalidUrl("https://user@gitlab.example.com");
    }

    @Test
    public void shouldRejectParentTraversalAndNullBytes() {
        assertInvalid("../scripts");
        assertInvalid("scripts/../secret");
        assertInvalid("scripts\u0000/secret");
    }

    private static void assertInvalid(String value) {
        try {
            ScmUtils.normalizeDirectoryPath(value);
            fail("path must be rejected: " + value);
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void assertInvalidUrl(String value) {
        try {
            ScmUtils.normalizeGitlabWebUrl(value);
            fail("URL must be rejected: " + value);
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
