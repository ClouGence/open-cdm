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
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.utils.function.ESupplier;

public interface ScmProviderSpi extends Spi {

    String name();

    String getServiceUrl();

    String getHelpUrl();

    List<ScmEventType> devopsSupportEvents();

    List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter);

    ScmRepo fetchRepo(String serviceUrl, String accessToken, ScmRepo selection);

    List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, ScmRepo repo, String filter, boolean exactMatch);

    ScmPathValidation validateScriptPath(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath);

    String fetchServerVersion(String serviceUrl, String accessToken);

    ScmEvent readEvent(String serviceUrl, String accessToken, String repoId, String repoPath, String repoName, String password, String signingToken,
                       Map<String, List<String>> headers, String jsonBody);

    void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) throws Exception;
}
