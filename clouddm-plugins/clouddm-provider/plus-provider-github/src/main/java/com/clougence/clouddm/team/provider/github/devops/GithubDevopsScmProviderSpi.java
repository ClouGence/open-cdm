package com.clougence.clouddm.team.provider.github.devops;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.function.ESupplier;

public class GithubDevopsScmProviderSpi implements ScmProviderSpi {

    @Override
    public String name() {
        return ScmProviderNames.Github.name();
    }

    @Override
    public String getServiceUrl() { return "https://github.com/"; }

    @Override
    public String getHelpUrl() { return "https://www.clougence.com/dm-doc/devops/provider/devops_cicd_github"; }

    @Override
    public List<ScmEventType> devopsSupportEvents() {
        return Arrays.asList(ScmEventType.Push);
    }

    @Override
    public List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter) {
        ScmRepo repo1 = new ScmRepo();
        repo1.setRepoName("cg-schema");
        repo1.setRepoUrl("https://github.com/clougence/clougence-schema");
        repo1.setBranchName("main");

        ScmRepo repo2 = new ScmRepo();
        repo2.setRepoName("cg-clouddm");
        repo2.setRepoUrl("https://github.com/clougence/clougence-clouddm");
        repo2.setBranchName("main");

        return Arrays.asList(repo1, repo2);
    }

    @Override
    public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, String repoName, String filter, boolean exactMatch) {
        return Collections.emptyList();
    }

    @Override
    public ScmEvent readEvent(String serviceUrl, String accessToken, String repoPath, String repoName, String password, Map<String, List<String>> headers, String jsonBody) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) {
        // TODO 从 SCM checkout 仓库到本地
        System.out.println("Github pullToLocal 从 SCM checkout 仓库到本地");
    }
}
