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
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.scm.*;
import com.clougence.clouddm.team.provider.gitee.constants.GiteeI18nKeys;
import com.clougence.clouddm.team.provider.gitee.model.*;
import com.clougence.clouddm.team.provider.gitee.utils.ZipUtils;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;
import com.clougence.utils.io.FileUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//
// https://help.gitee.com/webhook/gitee-webhook-push-data-type#pull-request-hook-%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F
//
@Slf4j
public class GiteeDevopsScmProviderSpi implements ScmProviderSpi {

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
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/devops/provider/devops_cicd_gitee#config"; }

    @Override
    public List<ScmEventType> devopsSupportEvents() {
        return Arrays.asList(ScmEventType.Push, ScmEventType.PullRequest);
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

            String requestUrl = "https://gitee.com/api/v5/user/repos?" +//
                                "access_token=" + accessToken + "&" +//
                                "sort=full_name&" + q + "per_page=100";
            Request request = new Request.Builder().url(requestUrl).build();
            Response response = this.httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                String errorMessage = response.code() + ":" + response.message();
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_FETCH_REPOS_ERROR, errorMessage);
            }

            String jsonStr = response.body().string();
            return JsonUtils.toListUseType(jsonStr, GiteeApiRepos.class);
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_SCM_FETCH_REPOS_ERROR, e.getMessage());
        }
    }

    private List<GiteeApiBranch> fetchOriginalBranch(String accessToken, String repoName, String filter, boolean exactMatch) {
        // fetch repo
        List<GiteeApiRepos> repoList = this.fetchOriginalRepos(accessToken, repoName)//
            .stream()
            .filter(repo -> repo.getName().equals(repoName))
            .collect(Collectors.toList());

        // check and find repo
        GiteeApiRepos repoObject = null;
        if (repoList.isEmpty()) {
            return Collections.emptyList();
        } else if (repoList.size() > 1) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_FETCH_BRANCH_MULTIPLE_REPOS_ERROR);
        } else {
            repoObject = repoList.get(0);
        }

        // fetch branch
        try {
            String requestUrl = "https://gitee.com/api/v5/repos/" + repoObject.getFull_name() + "/branches?" +//
                                "access_token=" + accessToken + "&" +//
                                "sort=name&direction=asc&per_page=100";
            Request request = new Request.Builder().url(requestUrl).build();
            Response response = this.httpClient.newCall(request).execute();
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
            repo.setRepoSpace(downloadInfo.getSpacePath());
            repo.setRepoName(downloadInfo.getRepoName());
            repo.setRepoUrl(downloadInfo.getRepoUrl());
            repo.setRepoHome(downloadInfo.getRepoHome());
            repo.setBranchName(downloadInfo.getRepoBranch());
            return repo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, String repoName, String filter, boolean exactMatch) {
        List<GiteeApiBranch> branches = this.fetchOriginalBranch(accessToken, repoName, filter, exactMatch);

        return branches.stream().map(b -> {
            ScmBranch branch = new ScmBranch();
            branch.setBranchName(b.getName());
            branch.setCommitId(b.getCommit().getSha());
            return branch;
        }).collect(Collectors.toList());
    }

    @Override
    public ScmEvent readEvent(String serviceUrl, String accessToken, String repoPath, String repoName, String password, Map<String, List<String>> headers, String jsonBody) {
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
                    event.setTarRepoName(hookInfo.getRepository().getPath());
                    event.setTarRepoBranch(hookInfo.getRef().substring("refs/heads/".length()));

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
                    // 以下信息来自 Gitee 官方人员权威解答
                    //      'create_pr' => '新建代码评审'
                    //      'merge_pr' => '合并代码评审'
                    //      'close_pr' => '关闭代码评审'
                    //      'assign_reviewer' => '指派审查者'
                    //      'unassign_reviewer' => '取消审查者'
                    //      'review_pass' => '审查通过'
                    //      'assign_tester' => '指派测试者'
                    //      'unassign_tester' => '取消测试者'
                    //      'test_pass' => '测试通过'
                    //      'update_issue' => '关联/取消关联 工作项'
                    //      'push_code' => '更新源分支代码'
                    //      'reopen' => '重新打开'
                    //      'comment_pr' => '评论代码评审'
                    //      'update_label' => '更新标签'
                    //      'set_draft' => '设置草稿'
                    //      'cancel_draft' => '取消草稿'
                    //
                    String status = hookInfo.getState();
                    // open 开启状态
                    // closed 已经关闭
                    // merged：已合并
                    String action = hookInfo.getAction();
                    // tested：测试通过
                    // approved：评审通过
                    // close：关闭 PR
                    // reopen：重新打开 PR
                    // open：新的 PR
                    // merge：合并
                    // update：更新信息
                    // assign：指派审查、指派测试

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
        String scmBranchName = repo.getBranchName();
        Map<String, DownloadInfo> groupByRepo = new HashMap<>();
        this.fetchRepo(scm.getAccessToken(), scmRepoName).forEach(d -> groupByRepo.put(d.getRepoName(), d));

        // check
        DownloadInfo info;
        if (!groupByRepo.containsKey(scmRepoName)) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_NOT_FOUND_REPO_ERROR);
        } else {
            info = groupByRepo.get(scmRepoName);
        }

        // download to local
        File tempPath = saveTo.getTempPath();
        tempPath.mkdirs();
        File tempFile = new File(tempPath, "download.zip");
        if (!downloadToLocal(scm, tempFile, watchdog, info, scmBranchName)) {
            return;
        }

        // unzip
        File saveToLocal = saveTo.getSaveToLocal();
        saveToLocal.mkdirs();
        log.info("watchdog: begin unZip " + tempFile.getAbsoluteFile() + " to " + saveToLocal.getAbsoluteFile());
        new ZipUtils().unZip(tempFile, saveToLocal, saveTo.getScriptPath(), 1, watchdog);
        tempFile.delete();
        tempFile.getParentFile().delete();
    }

    private boolean downloadToLocal(ScmProvider scm, File saveTo, ESupplier<Boolean, Exception> watchdog, DownloadInfo info, String scmBranchName) throws Exception {
        if (!watchdog.eGet()) {
            log.info("watchdog: interrupt the " + info.getRepoUrl() + " download.");
            return false;
        }

        String requestUrl = "https://gitee.com/api/v5/repos/" + info.getSpacePath() + "/" + info.getRepoPath() + "/zipball?" +//
                            "access_token=" + scm.getAccessToken() + "&" +//
                            "ref=" + URLEncoder.encode(scmBranchName, "UTF-8");
        Request request = new Request.Builder().url(requestUrl).build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMessage = response.code() + ":" + response.message();
                throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_SCM_DOWNLOAD_REPOS_ERROR, errorMessage);
            }

            long time = System.currentTimeMillis();
            long bytesReadTotal = 0;
            try (InputStream inputStream = response.body().byteStream(); FileOutputStream outputStream = new FileOutputStream(saveTo)) {
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesReadTotal = bytesReadTotal + bytesRead;

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
}
