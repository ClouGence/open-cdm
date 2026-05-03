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

    List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, String repoName, String filter, boolean exactMatch);

    ScmEvent readEvent(String serviceUrl, String accessToken, String repoPath, String repoName, String password, Map<String, List<String>> headers, String jsonBody);

    void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) throws Exception;
}
