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

import java.util.List;

import com.clougence.utils.StringUtils;

public final class ScmRepoUtils {

    private ScmRepoUtils(){
    }

    /**
     * Finds one unambiguous repository using the strongest identity available in the selection.
     */
    public static ScmRepo findUnique(List<ScmRepo> repos, ScmRepo selection) {
        if (repos == null || selection == null) {
            return null;
        }
        ScmRepo matched = null;
        for (ScmRepo repo : repos) {
            if (!matches(repo, selection)) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = repo;
        }
        return matched;
    }

    private static boolean matches(ScmRepo repo, ScmRepo selection) {
        if (repo == null) {
            return false;
        }
        if (StringUtils.isNotBlank(selection.getRepoId()) && StringUtils.isNotBlank(repo.getRepoId())) {
            return StringUtils.equals(selection.getRepoId(), repo.getRepoId());
        }
        if (StringUtils.isNotBlank(selection.getRepoPath()) && StringUtils.isNotBlank(repo.getRepoPath())) {
            return StringUtils.equals(selection.getRepoPath(), repo.getRepoPath());
        }
        if (StringUtils.isNotBlank(selection.getRepoSpace()) && StringUtils.isNotBlank(repo.getRepoSpace())) {
            return StringUtils.equals(selection.getRepoSpace(), repo.getRepoSpace()) && StringUtils.equals(selection.getRepoName(), repo.getRepoName());
        }
        return StringUtils.equals(selection.getRepoName(), repo.getRepoName());
    }
}
