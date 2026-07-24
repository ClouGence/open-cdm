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
package com.clougence.clouddm.team.provider.gitee.devops;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.clouddm.team.provider.gitee.constants.GiteeI18nKeys;
import com.clougence.clouddm.team.provider.gitee.model.*;
import com.clougence.clouddm.team.provider.gitee.utils.ZipUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;
import com.clougence.utils.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//
// https://help.gitee.com/webhook/gitee-webhook-push-data-type#pull-request-hook-%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F
//
@Slf4j
public class GiteeDevopsScmProviderSpi implements ScmProviderSpi {

    /**
     * Maximum compressed repository archive size accepted from Gitee, in bytes, to bound temporary-disk usage.
     */
    private static final long  MAX_ARCHIVE_BYTES = 1024L * 1024 * 1024;
    /**
     * Maximum number of repository entries inspected before download, to reject unexpectedly large repositories.
     */
    private static final int   MAX_FILES         = 10_000;

    private final OkHttpClient httpClient;

    public GiteeDevopsScmProviderSpi(OkHttpClient httpClient){
        this.httpClient = httpClient;
    }

    @Override
    public String name() {
        return ScmProviderNames.Gitee.name();
    }

    @Override
    public String getServiceUrl() { return "https://gitee.com/"; }

    @Override
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/integrations/devops/devops_cicd_gitee"; }

    @Override
    public List<ScmEventType> devopsSupportEvents() {
        return Arrays.asList(ScmEventType.Push, ScmEventType.PullRequest);
    }

    @Override
    public String fetchServerVersion(String serviceUrl, String accessToken) {
        return null;
    }

    private List<DownloadInfo> fetchRepo(String accessToken, String filter) {
        List<GiteeApiRepos> repos = this.fetchOriginalRepos(accessToken, filter);

        // group by name
        List<DownloadInfo> infoList = new ArrayList<>();
        for (GiteeApiRepos repo : repos) {
            String spacePath = repo.getNamespace().getPath();
            String repoPath = repo.getPath();
            String repoName = repo.getName();
            String repoUrl = repo.getHtml_url();
            String repoHome = "https://gitee.com/" + repo.getFull_name();
            String repoBranch = repo.getDefault_branch();
            infoList.add(new DownloadInfo(spacePath, repoPath, repoName, repoUrl, repoHome, repoBranch));
        }
        return infoList;
    }

    private List<GiteeApiRepos> fetchOriginalRepos(String accessToken, String filter) {
        try {
            String q = "";
            if (StringUtils.isNotBlank(filter)) {
                q = "q=" + URLEncoder.encode(filter, "UTF-8") + "&";
            }

            String requestUrl = "https://gitee.com/api/v5/user/repos?sort=full_name&" + q + "per_page=100";
            Request request = authorizedRequest(requestUrl, accessToken);
            try (Response response = this.httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorMessage = response.code() + ":" + response.message();
                    throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_FETCH_REPOS_ERROR, errorMessage);
                }

                String jsonStr = response.body().string();
                return JsonUtils.toListUseType(jsonStr, GiteeApiRepos.class);
            }
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_FETCH_REPOS_ERROR, e.getMessage());
        }
    }

    private List<GiteeApiBranch> fetchOriginalBranch(String accessToken, ScmRepo repo, String filter, boolean exactMatch) {
        // fetch branch
        try {
            String fullName = StringUtils.isNotBlank(repo.getRepoId()) ? repo.getRepoId() : repo.getRepoSpace() + "/" + repo.getRepoName();
            String requestUrl = "https://gitee.com/api/v5/repos/" + fullName + "/branches?sort=name&direction=asc&per_page=100";
            Request request = authorizedRequest(requestUrl, accessToken);
            try (Response response = this.httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorMessage = response.code() + ":" + response.message();
                    throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_FETCH_BRANCH_ERROR, errorMessage);
                }

                String jsonStr = response.body().string();
                List<GiteeApiBranch> apiBranches = JsonUtils.toListUseType(jsonStr, GiteeApiBranch.class);

                return apiBranches.stream().filter(b -> {
                    if (StringUtils.isBlank(filter)) {
                        return true;
                    }

                    if (exactMatch) {
                        return StringUtils.equals(b.getName(), filter);
                    } else {
                        return StringUtils.startsWithIgnoreCase(b.getName(), filter);
                    }
                }).collect(Collectors.toList());
            }
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_FETCH_BRANCH_ERROR, e.getMessage());
        }
    }

    @Override
    public List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter) {
        List<DownloadInfo> repos = fetchRepo(accessToken, filter);

        return repos.stream().map(downloadInfo -> {
            ScmRepo repo = new ScmRepo();
            repo.setRepoId(downloadInfo.getSpacePath() + "/" + downloadInfo.getRepoPath());
            repo.setRepoPath(repo.getRepoId());
            repo.setRepoSpace(downloadInfo.getSpacePath());
            repo.setRepoName(downloadInfo.getRepoName());
            repo.setRepoUrl(downloadInfo.getRepoUrl());
            repo.setRepoHome(downloadInfo.getRepoHome());
            repo.setBranchName(downloadInfo.getRepoBranch());
            return repo;
        }).collect(Collectors.toList());
    }

    @Override
    public ScmRepo fetchRepo(String serviceUrl, String accessToken, ScmRepo selection) {
        if (selection == null) {
            return null;
        }
        List<ScmRepo> repos = fetchRepoList(serviceUrl, accessToken, selection.getRepoName());
        return ScmRepoUtils.findUnique(repos, selection);
    }

    @Override
    public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, ScmRepo repo, String filter, boolean exactMatch) {
        List<GiteeApiBranch> branches = this.fetchOriginalBranch(accessToken, repo, filter, exactMatch);

        return branches.stream().map(b -> {
            ScmBranch branch = new ScmBranch();
            branch.setBranchName(b.getName());
            branch.setCommitId(b.getCommit().getSha());
            return branch;
        }).collect(Collectors.toList());
    }

    @Override
    public ScmPathValidation validateScriptPath(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath) {
        if (repo == null || StringUtils.isBlank(repo.getCommitId())) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "missing immutable commit SHA");
        }

        String normalizedPath = normalizeScriptPath(scriptPath);
        HttpUrl treeUrl = buildRepositoryTreeUrl(repo);
        Request request = authorizedRequest(treeUrl.toString(), accessToken);
        try (Response response = this.httpClient.newCall(request).execute()) {
            JsonNode repositoryTree = readRepositoryTree(response);
            return inspectScriptPath(repositoryTree, normalizedPath);
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, e.getMessage());
        }
    }

    private static String normalizeScriptPath(String scriptPath) {
        try {
            return ScmPathUtils.normalizeDirectoryPath(scriptPath);
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "invalid script path");
        }
    }

    private static HttpUrl buildRepositoryTreeUrl(ScmRepo repo) {
        return Objects.requireNonNull(HttpUrl.parse("https://gitee.com/api/v5"))
            .newBuilder()
            .addPathSegment("repos")
            .addPathSegment(repo.getRepoSpace())
            .addPathSegment(repoName(repo))
            .addPathSegment("git")
            .addPathSegment("trees")
            .addPathSegment(repo.getCommitId())
            .addQueryParameter("recursive", "1")
            .build();
    }

    private static JsonNode readRepositoryTree(Response response) throws IOException {
        if (!response.isSuccessful()) {
            String errorMessage = response.code() + ":" + response.message();
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, errorMessage);
        }
        return JsonUtils.defaultObjectMapper().readTree(response.body().string());
    }

    private static ScmPathValidation inspectScriptPath(JsonNode repositoryTree, String normalizedPath) {
        JsonNode entries = repositoryTree.path("tree");
        if (!entries.isArray()) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "unexpected repository tree response");
        }

        boolean pathFound = StringUtils.isBlank(normalizedPath);
        ScmPathValidation validation = new ScmPathValidation();
        validation.setChecked(true);
        for (JsonNode entry : entries) {
            String entryPath = entry.path("path").asText();
            String entryType = entry.path("type").asText();

            boolean targetDirectory = StringUtils.isNotBlank(normalizedPath) && StringUtils.equals(entryPath, normalizedPath);
            if (targetDirectory) {
                validateScriptDirectoryEntry(entryType);
                pathFound = true;
            }
            if ("commit".equals(entryType) && normalizedPath.startsWith(entryPath + "/")) {
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "script directory is inside a Git submodule");
            }

            boolean insideScriptPath = StringUtils.isBlank(normalizedPath) || entryPath.startsWith(normalizedPath + "/");
            if (insideScriptPath && "blob".equals(entryType)) {
                countScriptFile(validation, entryPath);
            }
        }

        if (repositoryTree.path("truncated").asBoolean(false)) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "repository tree response was truncated");
        }
        if (!pathFound) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "script directory does not exist at commit");
        }
        return validation;
    }

    private static void validateScriptDirectoryEntry(String entryType) {
        if ("commit".equals(entryType)) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "script directory is a Git submodule");
        }
        if (!"tree".equals(entryType)) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "script path is not a directory");
        }
    }

    private static void countScriptFile(ScmPathValidation validation, String entryPath) {
        validation.setFileCount(validation.getFileCount() + 1);
        if (entryPath.toLowerCase(Locale.ROOT).endsWith(".sql")) {
            validation.setSqlFileCount(validation.getSqlFileCount() + 1);
        }
        if (validation.getFileCount() > MAX_FILES) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "more than 10000 files in script directory");
        }
    }

    private static String repoName(ScmRepo repo) {
        if (StringUtils.isNotBlank(repo.getRepoId())) {
            int slash = repo.getRepoId().lastIndexOf('/');
            if (slash >= 0 && slash + 1 < repo.getRepoId().length()) {
                return repo.getRepoId().substring(slash + 1);
            }
        }
        return repo.getRepoName();
    }

    @Override
    public ScmEvent readEvent(String serviceUrl, String accessToken, String repoId, String repoPath, String repoName, String password, String signingToken,
                              Map<String, List<String>> headers, String jsonBody) {
        if (StringUtils.isEmpty(jsonBody)) {
            return null;
        }
        //        List<String> strings = headers.get("X-Gitee-Event");
        //        if (strings != null && !strings.contains("Test Hook")) {
        //            return null; // is test hook.
        //        }

        try {
            GiteeWebHookEvent hookInfo = JsonUtils.toObjUseType(jsonBody, GiteeWebHookEvent.class);
            if (StringUtils.isNotBlank(password) && !StringUtils.equals(hookInfo.getPassword(), password)) {
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_EVENT_PASSWORD_ERROR);
            }

            ScmEventType eventType = null;
            if (StringUtils.equals(hookInfo.getHook_name(), "push_hooks")) {
                eventType = ScmEventType.Push;
            } else if (StringUtils.equalsIgnoreCase(hookInfo.getHook_name(), "tag_push_hooks")) {
                eventType = ScmEventType.Tag;
            } else if (StringUtils.equalsIgnoreCase(hookInfo.getHook_name(), "issue_hooks")) {
                eventType = ScmEventType.Issue;
            } else if (StringUtils.equalsIgnoreCase(hookInfo.getHook_name(), "merge_request_hooks")) {
                eventType = ScmEventType.PullRequest;
            } else if (StringUtils.equalsIgnoreCase(hookInfo.getHook_name(), "note_hooks")) {
                eventType = ScmEventType.Note;
            }

            switch (eventType) {
                case Push:
                case Tag: {
                    ScmEvent event = new ScmEvent();
                    event.setHookId(hookInfo.getHook_id());
                    event.setEventType(eventType);
                    event.setEventTime(new Date(Long.parseLong(hookInfo.getTimestamp())));
                    event.setEventId(hookInfo.getAfter());
                    event.setUserId(hookInfo.getPusher().getId());
                    event.setUserNick(hookInfo.getPusher().getName());
                    event.setUserName(hookInfo.getPusher().getUsername());
                    event.setUserEmail(hookInfo.getPusher().getEmail());
                    event.setTarRepoPath(hookInfo.getRepository().getNamespace());
                    event.setTarRepoId(hookInfo.getRepository().getNamespace() + "/" + hookInfo.getRepository().getPath());
                    event.setTarRepoName(hookInfo.getRepository().getPath());
                    String prefix = eventType == ScmEventType.Tag ? "refs/tags/" : "refs/heads/";
                    String branch = StringUtils.startsWith(hookInfo.getRef(), prefix) ? hookInfo.getRef().substring(prefix.length()) : hookInfo.getRef();
                    event.setTarRepoBranch(branch);

                    event.setTarget(ScmEventTarget.Repository);
                    if (Boolean.TRUE.equals(hookInfo.getCreated())) {
                        event.setStatus(ScmEventStatus.Create);
                    } else if (Boolean.TRUE.equals(hookInfo.getDeleted())) {
                        event.setStatus(ScmEventStatus.Delete);
                    } else {
                        event.setStatus(ScmEventStatus.Update);
                    }
                    return event;
                }
                case PullRequest: {
                    // The following information comes from the authoritative answers of Gitee officials.
                    //      'create_pr' => 'Create code review'
                    //      'merge_pr' => 'Merge code review'
                    //      'close_pr' => 'Close code review'
                    //      'assign_reviewer' => 'Assign reviewer'
                    //      'unassign_reviewer' => 'Unassign reviewer'
                    //      'review_pass' => 'Review passed'
                    //      'assign_tester' => 'Assign tester'
                    //      'unassign_tester' => 'Unassign tester'
                    //      'test_pass' => 'Test passed'
                    //      'update_issue' => 'Link/unlink work item'
                    //      'push_code' => 'Update source branch code'
                    //      'reopen' => 'Reopen'
                    //      'comment_pr' => 'Comment on code review'
                    //      'update_label' => 'Update label'
                    //      'set_draft' => 'Set draft'
                    //      'cancel_draft' => 'Cancel draft'
                    //
                    String status = hookInfo.getState();
                    // Open Status
                    // Closed
                    // merged: merged
                    String action = hookInfo.getAction();
                    // Test passed.
                    // Approved: Reviewed
                    // Close: close PR
                    // reopen: reopen PR
                    // Open: New PR
                    // Merge: Merge
                    // update: update
                    // Assign: Assign review, assign test

                    ScmEvent event = new ScmEvent();
                    event.setHookId(hookInfo.getHook_id());
                    event.setEventType(eventType);
                    event.setEventTime(new Date(Long.parseLong(hookInfo.getTimestamp())));
                    event.setEventId(hookInfo.getMerge_commit_sha());
                    event.setUserId(hookInfo.getAuthor().getId());
                    event.setUserNick(hookInfo.getAuthor().getName());
                    event.setUserName(hookInfo.getAuthor().getUsername());
                    event.setUserEmail(hookInfo.getAuthor().getEmail());
                    GiteeWebHookEventRepository targetRepo = hookInfo.getTarget_repo().getRepository();
                    event.setTarRepoPath(targetRepo.getNamespace());
                    event.setTarRepoId(targetRepo.getNamespace() + "/" + targetRepo.getPath());
                    event.setTarRepoName(targetRepo.getPath());
                    event.setTarRepoBranch(hookInfo.getTarget_branch());

                    event.setTarget(ScmEventTarget.PullRequest);
                    if (StringUtils.equals(status, "merged") && StringUtils.equals(action, "merge")) {
                        event.setStatus(ScmEventStatus.Merged);
                    } else if (StringUtils.equals(status, "open") && (StringUtils.equals(action, "reopen") || StringUtils.equals(action, "open"))) {
                        event.setStatus(ScmEventStatus.Create);
                    } else if (StringUtils.equals(status, "closed")) {
                        event.setStatus(ScmEventStatus.Closed);
                    } else {
                        event.setStatus(ScmEventStatus.Update);
                    }

                    GiteeWebHookEventRepository sourceRepo = hookInfo.getSource_repo().getRepository();
                    event.setSrcRepoPath(sourceRepo.getNamespace());
                    event.setSrcRepoName(sourceRepo.getPath());
                    event.setSrcRepoBranch(hookInfo.getSource_branch());
                    return event;
                }
                case Issue: {
                    ScmEvent event = new ScmEvent();
                    event.setHookId(hookInfo.getHook_id());
                    event.setEventType(eventType);
                    event.setEventTime(new Date(Long.parseLong(hookInfo.getTimestamp())));
                    event.setEventId(hookInfo.getIid());
                    event.setUserId(hookInfo.getUser().getId());
                    event.setUserNick(hookInfo.getUser().getName());
                    event.setUserName(hookInfo.getUser().getUsername());
                    event.setUserEmail(hookInfo.getUser().getEmail());
                    event.setTarRepoPath(hookInfo.getRepository().getNamespace());
                    event.setTarRepoName(hookInfo.getRepository().getPath());
                    event.setTarRepoBranch(null);

                    String status = hookInfo.getState();
                    event.setTarget(ScmEventTarget.Issue);
                    if (StringUtils.equals(status, "open")) {
                        event.setStatus(ScmEventStatus.Create);
                    } else if (StringUtils.equals(status, "closed") || StringUtils.equals(status, "rejected")) {
                        event.setStatus(ScmEventStatus.Closed);
                    } else {
                        event.setStatus(ScmEventStatus.Update);
                    }

                    event.setTitle(hookInfo.getTitle());
                    event.setBody(hookInfo.getDescription());
                    event.setTarget(null);
                    return event;
                }
                case Note: {
                    ScmEvent event = new ScmEvent();
                    event.setHookId(hookInfo.getHook_id());
                    event.setEventType(eventType);
                    if (StringUtils.equals(hookInfo.getNoteable_type(), "Issue")) {
                        event.setTarget(ScmEventTarget.Issue);
                    } else if (StringUtils.equals(hookInfo.getNoteable_type(), "PullRequest")) {
                        event.setTarget(ScmEventTarget.PullRequest);
                    } else {
                        event.setTarget(ScmEventTarget.Repository);
                    }
                    event.setEventTime(new Date(Long.parseLong(hookInfo.getTimestamp())));
                    event.setEventId(hookInfo.getNoteable_id());
                    event.setUserId(hookInfo.getAuthor().getId());
                    event.setUserNick(hookInfo.getAuthor().getName());
                    event.setUserName(hookInfo.getAuthor().getUsername());
                    event.setUserEmail(hookInfo.getAuthor().getEmail());
                    event.setTarRepoPath(hookInfo.getRepository().getNamespace());
                    event.setTarRepoName(hookInfo.getRepository().getPath());
                    event.setTarRepoBranch(hookInfo.getRepository().getDefault_branch());

                    String action = hookInfo.getAction();
                    if (StringUtils.equals(action, "comment")) {
                        event.setStatus(ScmEventStatus.Create);
                    } else if (StringUtils.equals(action, "edited")) {
                        event.setStatus(ScmEventStatus.Update);
                    } else {
                        event.setStatus(ScmEventStatus.Update);
                    }

                    event.setTitle(null);
                    event.setBody(hookInfo.getNote());
                    return event;
                }
                default:
                    return null;
            }
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_EVENT_DECODER_ERROR, e.getMessage());
        }
    }

    @Override
    public void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) throws Exception {
        String scmRepoName = repo.getRepoName();
        String scmCommitId = repo.getCommitId();
        if (StringUtils.isBlank(scmCommitId)) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "missing immutable commit SHA");
        }
        List<DownloadInfo> candidates = this.fetchRepo(scm.getAccessToken(), scmRepoName)
            .stream()
            .filter(d -> StringUtils.isBlank(repo.getRepoId()) ? StringUtils.equals(d.getRepoName(), scmRepoName) : StringUtils
                .equals(d.getSpacePath() + "/" + d.getRepoPath(), repo.getRepoId()))
            .collect(Collectors.toList());

        DownloadInfo info;
        if (candidates.isEmpty()) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_NOT_FOUND_REPO_ERROR);
        } else if (candidates.size() > 1) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_FETCH_BRANCH_MULTIPLE_REPOS_ERROR);
        }
        info = candidates.get(0);

        // download to local
        File tempPath = saveTo.getTempPath();
        File tempFile = new File(tempPath, "download.zip");
        File saveToLocal = saveTo.getSaveToLocal();
        try {
            tempPath.mkdirs();
            if (!downloadToLocal(scm, tempFile, watchdog, info, scmCommitId)) {
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "download interrupted");
            }

            // unzip
            saveToLocal.mkdirs();
            log.info("watchdog: begin unZip " + tempFile.getAbsoluteFile() + " to " + saveToLocal.getAbsoluteFile());
            new ZipUtils().unZip(tempFile, saveToLocal, saveTo.getScriptPath(), 1, watchdog);
        } catch (Exception e) {
            FileUtils.deleteQuietly(saveToLocal);
            throw e;
        } finally {
            FileUtils.deleteQuietly(tempFile);
            FileUtils.deleteQuietly(tempPath);
        }
    }

    private boolean downloadToLocal(ScmProvider scm, File saveTo, ESupplier<Boolean, Exception> watchdog, DownloadInfo info, String scmCommitId) throws Exception {
        if (!watchdog.eGet()) {
            log.info("watchdog: interrupt the " + info.getRepoUrl() + " download.");
            return false;
        }

        String requestUrl = "https://gitee.com/api/v5/repos/" + info.getSpacePath() + "/" + info.getRepoPath() + "/zipball?" +//
                            "ref=" + URLEncoder.encode(scmCommitId, "UTF-8");
        Request request = authorizedRequest(requestUrl, scm.getAccessToken());
        try (Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMessage = response.code() + ":" + response.message();
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, errorMessage);
            }
            if (response.body().contentLength() > MAX_ARCHIVE_BYTES) {
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "compressed archive exceeds 1 GiB");
            }

            long time = System.currentTimeMillis();
            long bytesReadTotal = 0;
            try (InputStream inputStream = response.body().byteStream(); FileOutputStream outputStream = new FileOutputStream(saveTo)) {
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesReadTotal = bytesReadTotal + bytesRead;
                    if (bytesReadTotal > MAX_ARCHIVE_BYTES) {
                        throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, "compressed archive exceeds 1 GiB");
                    }

                    if ((time + 2000) > System.currentTimeMillis()) {
                        continue;
                    }

                    // watchdog and print status
                    if (!watchdog.eGet()) {
                        log.info("watchdog: interrupt the " + info.getRepoUrl() + " download.");
                        FileUtils.deleteQuietly(saveTo);
                        return false;
                    } else {
                        time = System.currentTimeMillis();
                        log.info("watchdog: the " + FileUtils.readableFileSize(bytesReadTotal) + " data has been accepted.");
                    }
                }
                log.info("watchdog: the " + FileUtils.readableFileSize(bytesReadTotal) + " data has been accepted, transmission is complete.");
            }
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, e.getMessage());
        }

        return true;
    }

    private static Request authorizedRequest(String url, String accessToken) {
        return new Request.Builder().url(url).header("Authorization", "token " + accessToken).build();
    }
}
