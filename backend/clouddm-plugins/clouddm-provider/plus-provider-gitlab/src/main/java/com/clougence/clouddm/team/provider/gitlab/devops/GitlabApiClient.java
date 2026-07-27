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
package com.clougence.clouddm.team.provider.gitlab.devops;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.clouddm.team.provider.gitlab.constants.GitlabI18nKeys;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final class GitlabApiClient {

    /**
     * Maximum number of projects returned by one repository search.
     */
    private static final int   MAX_PROJECTS               = 10_000;
    /**
     * Maximum number of branches returned for one project.
     */
    private static final int   MAX_BRANCHES               = 10_000;
    /**
     * Maximum number of repository tree entries inspected for one script directory.
     */
    private static final int   MAX_FILES                  = 10_000;
    /**
     * Number of GitLab API records requested per page.
     */
    static final int           API_PAGE_SIZE              = 100;
    /**
     * Sentinel returned when a response does not advertise another page.
     */
    static final int           NO_NEXT_PAGE               = -1;
    /**
     * Maximum number of same-origin redirects followed by one request.
     */
    private static final int   MAX_REDIRECTS              = 5;
    /**
     * Maximum retry attempts after the initial request.
     */
    private static final int   MAX_API_RETRIES            = 2;
    /**
     * Default bounded retry delay when GitLab omits a valid Retry-After header.
     */
    private static final long  DEFAULT_RETRY_DELAY_MILLIS = 250;
    /**
     * Maximum accepted retry delay, preventing one API call from blocking indefinitely.
     */
    private static final long  MAX_RETRY_DELAY_MILLIS     = 10_000;

    private final OkHttpClient httpClient;

    GitlabApiClient(OkHttpClient httpClient){
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient").newBuilder().followRedirects(false).followSslRedirects(false).build();
    }

    List<ScmRepo> fetchRepositories(String serviceUrl, String accessToken, String filter) {
        HttpUrl root = apiRoot(serviceUrl);
        try {
            return fetchRepositoryPages(root, accessToken, filter);
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw apiError(e.getMessage(), e);
        }
    }

    private List<ScmRepo> fetchRepositoryPages(HttpUrl root, String accessToken, String filter) throws Exception {
        List<ScmRepo> repositories = new ArrayList<>();
        int page = 1;
        while (page != NO_NEXT_PAGE) {
            try (Response response = execute(buildProjectListUrl(root, filter, page), accessToken)) {
                appendRepositories(readArrayResponse(response, "unexpected projects response"), filter, repositories);
                page = nextPage(response);
                if (pageLimitExceeded(page, MAX_PROJECTS)) {
                    throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_FETCH_REPOS_LIMIT);
                }
            }
        }
        return repositories;
    }

    private HttpUrl buildProjectListUrl(HttpUrl root, String filter, int page) {
        HttpUrl.Builder url = endpoint(root, "projects").newBuilder()
            .addQueryParameter("archived", "false")
            .addQueryParameter("order_by", "path")
            .addQueryParameter("sort", "asc")
            .addQueryParameter("per_page", Integer.toString(API_PAGE_SIZE))
            .addQueryParameter("page", Integer.toString(page));
        if (StringUtils.isNotBlank(filter)) {
            url.addQueryParameter("search", filter.trim());
        }
        return url.build();
    }

    private void appendRepositories(JsonNode projects, String filter, List<ScmRepo> repositories) {
        for (JsonNode project : projects) {
            if (!repositoryIsAvailable(project)) {
                continue;
            }
            ScmRepo repository = toRepo(project);
            if (matchesFilter(repository, filter)) {
                repositories.add(repository);
            }
            if (repositories.size() > MAX_PROJECTS) {
                throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_FETCH_REPOS_LIMIT);
            }
        }
    }

    String fetchServerVersion(String serviceUrl, String accessToken) {
        try (Response response = execute(endpoint(apiRoot(serviceUrl), "version"), accessToken)) {
            JsonNode metadata = JsonUtils.defaultObjectMapper().readTree(requireSuccessful(response));
            String version = metadata.path("version").asText();
            return StringUtils.isBlank(version) ? null : version;
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw apiError(e.getMessage(), e);
        }
    }

    ScmRepo fetchRepository(String serviceUrl, String accessToken, ScmRepo selection) {
        if (selection == null || StringUtils.isBlank(selection.getRepoId())) {
            return null;
        }
        try (Response response = execute(projectEndpoint(apiRoot(serviceUrl), selection.getRepoId()), accessToken)) {
            JsonNode project = JsonUtils.defaultObjectMapper().readTree(requireSuccessful(response));
            return repositoryIsAvailable(project) ? toRepo(project) : null;
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw apiError(e.getMessage(), e);
        }
    }

    List<ScmBranch> fetchBranches(String serviceUrl, String accessToken, ScmRepo repo, String filter, boolean exactMatch) {
        if (repo == null || StringUtils.isBlank(repo.getRepoId())) {
            throw apiError("missing project identifier");
        }
        HttpUrl root = apiRoot(serviceUrl);
        try {
            return fetchBranchPages(root, accessToken, repo.getRepoId(), filter, exactMatch);
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw apiError(e.getMessage(), e);
        }
    }

    private List<ScmBranch> fetchBranchPages(HttpUrl root, String accessToken, String repoId, String filter, boolean exactMatch) throws Exception {
        List<ScmBranch> branches = new ArrayList<>();
        int page = 1;
        while (page != NO_NEXT_PAGE) {
            try (Response response = execute(buildBranchListUrl(root, repoId, filter, page), accessToken)) {
                appendBranches(readArrayResponse(response, "unexpected branches response"), filter, exactMatch, branches);
                page = nextPage(response);
                if (pageLimitExceeded(page, MAX_BRANCHES)) {
                    throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_FETCH_BRANCH_LIMIT);
                }
            }
        }
        return branches;
    }

    private HttpUrl buildBranchListUrl(HttpUrl root, String repoId, String filter, int page) {
        HttpUrl.Builder url = projectEndpoint(root, repoId, "repository", "branches").newBuilder()
            .addQueryParameter("per_page", Integer.toString(API_PAGE_SIZE))
            .addQueryParameter("page", Integer.toString(page));
        if (StringUtils.isNotBlank(filter)) {
            url.addQueryParameter("search", filter);
        }
        return url.build();
    }

    private void appendBranches(JsonNode nodes, String filter, boolean exactMatch, List<ScmBranch> branches) {
        for (JsonNode node : nodes) {
            String branchName = node.path("name").asText();
            if (!branchMatchesFilter(branchName, filter, exactMatch)) {
                continue;
            }
            ScmBranch branch = new ScmBranch();
            branch.setBranchName(branchName);
            branch.setCommitId(node.path("commit").path("id").asText());
            branches.add(branch);
            if (branches.size() > MAX_BRANCHES) {
                throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_FETCH_BRANCH_LIMIT);
            }
        }
    }

    ScmPathValidation inspectScriptFiles(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath) {
        ScmPathValidation validation = new ScmPathValidation();
        validation.setChecked(true);
        try {
            int page = 1;
            while (page != NO_NEXT_PAGE) {
                try (Response response = execute(buildRepositoryTreeUrl(serviceUrl, repo, scriptPath, page), accessToken)) {
                    countScriptFiles(readArrayResponse(response, "unexpected repository tree response"), validation);
                    page = nextPage(response);
                    if (pageLimitExceeded(page, MAX_FILES)) {
                        throw archiveLimit("more than 10000 entries in script directory");
                    }
                }
            }
            return validation;
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw apiError(e.getMessage(), e);
        }
    }

    private HttpUrl buildRepositoryTreeUrl(String serviceUrl, ScmRepo repo, String scriptPath, int page) {
        HttpUrl.Builder url = projectEndpoint(apiRoot(serviceUrl), repo.getRepoId(), "repository", "tree").newBuilder()
            .addQueryParameter("ref", repo.getCommitId())
            .addQueryParameter("recursive", "true")
            .addQueryParameter("per_page", Integer.toString(API_PAGE_SIZE))
            .addQueryParameter("page", Integer.toString(page));
        if (StringUtils.isNotBlank(scriptPath)) {
            url.addQueryParameter("path", scriptPath);
        }
        return url.build();
    }

    private void countScriptFiles(JsonNode entries, ScmPathValidation validation) {
        for (JsonNode entry : entries) {
            if (!"blob".equals(entry.path("type").asText())) {
                continue;
            }
            validation.setFileCount(validation.getFileCount() + 1);
            if (entry.path("path").asText().toLowerCase(Locale.ROOT).endsWith(".sql")) {
                validation.setSqlFileCount(validation.getSqlFileCount() + 1);
            }
            if (validation.getFileCount() > MAX_FILES) {
                throw archiveLimit("more than 10000 files in script directory");
            }
        }
    }

    String fetchMergedCommitId(String serviceUrl, String accessToken, String projectId, String mergeRequestIid) {
        if (StringUtils.isBlank(projectId) || StringUtils.isBlank(mergeRequestIid)) {
            throw new ScmWebhookException(503, "unable to resolve merged commit SHA");
        }
        HttpUrl url = projectEndpoint(apiRoot(serviceUrl), projectId, "merge_requests", mergeRequestIid);
        try (Response response = execute(url, accessToken)) {
            JsonNode mergeRequest = JsonUtils.defaultObjectMapper().readTree(requireSuccessful(response));
            String commitId = firstNonBlank(mergeRequest.path("merge_commit_sha").asText(), mergeRequest.path("squash_commit_sha").asText());
            if (StringUtils.isBlank(commitId)) {
                throw new ScmWebhookException(503, "unable to resolve merged commit SHA");
            }
            return commitId;
        } catch (ScmWebhookException e) {
            throw e;
        } catch (Exception e) {
            throw new ScmWebhookException(503, "unable to resolve merged commit SHA");
        }
    }

    Response execute(HttpUrl url, String accessToken) throws Exception {
        HttpUrl requestUrl = url;
        int redirects = 0;
        int retries = 0;
        while (true) {
            Response response = httpClient.newCall(buildRequest(requestUrl, accessToken)).execute();
            if (isRedirect(response.code())) {
                redirects++;
                try {
                    requestUrl = resolveRedirect(url, requestUrl, response, redirects);
                } finally {
                    response.close();
                }
                continue;
            }
            if (isRetryable(response.code()) && retries < MAX_API_RETRIES) {
                long retryDelay = retryDelayMillis(response, retries);
                if (retryDelay >= 0) {
                    response.close();
                    retries++;
                    waitBeforeRetry(retryDelay);
                    continue;
                }
            }
            return response;
        }
    }

    private Request buildRequest(HttpUrl url, String accessToken) {
        return new Request.Builder().url(url).header("PRIVATE-TOKEN", Objects.requireNonNullElse(accessToken, "")).header("Accept", "application/json").build();
    }

    private HttpUrl resolveRedirect(HttpUrl origin, HttpUrl current, Response response, int redirectCount) {
        String location = response.header("Location");
        HttpUrl redirectUrl = location == null ? null : current.resolve(location);
        if (redirectUrl == null) {
            throw apiError("invalid GitLab redirect");
        }
        if (!isSameOrigin(origin, redirectUrl)) {
            throw apiError("cross-origin GitLab redirect is not allowed");
        }
        if (redirectCount > MAX_REDIRECTS) {
            throw apiError("too many GitLab redirects");
        }
        return redirectUrl;
    }

    private void waitBeforeRetry(long retryDelay) {
        try {
            Thread.sleep(retryDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw apiError("GitLab request retry interrupted", e);
        }
    }

    JsonNode readArrayResponse(Response response, String invalidResponseMessage) throws Exception {
        JsonNode result = JsonUtils.defaultObjectMapper().readTree(requireSuccessful(response));
        if (!result.isArray()) {
            throw apiError(invalidResponseMessage);
        }
        return result;
    }

    String requireSuccessful(Response response) throws Exception {
        if (response.isSuccessful()) {
            return response.body().string();
        }
        if (response.code() == 401 || response.code() == 403) {
            throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_AUTH_ERROR, response.code());
        }
        if (response.code() == 404) {
            throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_NOT_FOUND_ERROR);
        }
        if (response.code() == 429) {
            throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_RATE_LIMIT_ERROR);
        }
        throw apiError(response.code() + ":" + response.message());
    }

    static String normalizeServiceUrl(String serviceUrl) {
        try {
            return ScmUtils.normalizeGitlabWebUrl(serviceUrl);
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GitlabI18nKeys.GITLAB_SCM_INVALID_URL, e.getMessage());
        }
    }

    static HttpUrl apiRoot(String serviceUrl) {
        HttpUrl result = HttpUrl.parse(normalizeServiceUrl(serviceUrl) + "/api/v4");
        if (result == null) {
            throw ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_INVALID_URL, "unable to parse URL");
        }
        return result;
    }

    static HttpUrl endpoint(HttpUrl root, String... segments) {
        HttpUrl.Builder builder = root.newBuilder();
        for (String segment : segments) {
            builder.addPathSegment(segment);
        }
        return builder.build();
    }

    static HttpUrl projectEndpoint(HttpUrl root, String projectId, String... segments) {
        HttpUrl.Builder builder = root.newBuilder().addPathSegment("projects").addPathSegment(projectId);
        for (String segment : segments) {
            builder.addPathSegment(segment);
        }
        return builder.build();
    }

    static int nextPage(Response response) {
        String nextPage = response.header("X-Next-Page");
        return StringUtils.isBlank(nextPage) ? NO_NEXT_PAGE : Integer.parseInt(nextPage);
    }

    static boolean pageLimitExceeded(int page, int itemLimit) {
        return (long) (page - 1) * API_PAGE_SIZE >= itemLimit;
    }

    private boolean repositoryIsAvailable(JsonNode project) {
        boolean archived = project.path("archived").asBoolean(false);
        boolean repositoryDisabled = "disabled".equalsIgnoreCase(project.path("repository_access_level").asText());
        return !archived && !repositoryDisabled;
    }

    private boolean branchMatchesFilter(String branchName, String filter, boolean exactMatch) {
        if (StringUtils.isBlank(filter)) {
            return true;
        }
        if (exactMatch) {
            return filter.equals(branchName);
        }
        return branchName.toLowerCase(Locale.ROOT).startsWith(filter.toLowerCase(Locale.ROOT));
    }

    private ScmRepo toRepo(JsonNode project) {
        ScmRepo repo = new ScmRepo();
        String fullPath = project.path("path_with_namespace").asText();
        repo.setRepoId(project.path("id").asText());
        repo.setRepoPath(fullPath);
        repo.setRepoSpace(namespaceOf(fullPath));
        repo.setRepoName(project.path("path").asText());
        repo.setRepoUrl(project.path("web_url").asText());
        repo.setRepoHome(project.path("web_url").asText());
        repo.setBranchName(project.path("default_branch").asText());
        repo.setArchived(project.path("archived").asBoolean(false));
        repo.setEmpty(project.path("empty_repo").asBoolean(StringUtils.isBlank(repo.getBranchName())));
        return repo;
    }

    private static boolean matchesFilter(ScmRepo repo, String filter) {
        if (StringUtils.isBlank(filter)) {
            return true;
        }
        String needle = filter.toLowerCase(Locale.ROOT);
        return repo.getRepoName().toLowerCase(Locale.ROOT).contains(needle) || repo.getRepoPath().toLowerCase(Locale.ROOT).contains(needle);
    }

    private static String namespaceOf(String fullPath) {
        int index = fullPath == null ? -1 : fullPath.lastIndexOf('/');
        return index < 0 ? "" : fullPath.substring(0, index);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value) && !"null".equalsIgnoreCase(value)) {
                return value;
            }
        }
        return null;
    }

    private static boolean isRedirect(int statusCode) {
        return statusCode == 300 || statusCode == 301 || statusCode == 302 || statusCode == 303 || statusCode == 307 || statusCode == 308;
    }

    private static boolean isSameOrigin(HttpUrl source, HttpUrl target) {
        return source.scheme().equals(target.scheme()) && source.host().equalsIgnoreCase(target.host()) && source.port() == target.port();
    }

    private static boolean isRetryable(int statusCode) {
        return statusCode == 429 || statusCode == 502 || statusCode == 503 || statusCode == 504;
    }

    private static long retryDelayMillis(Response response, int retryCount) {
        String retryAfter = response.header("Retry-After");
        long delay = DEFAULT_RETRY_DELAY_MILLIS * (retryCount + 1L);
        if (StringUtils.isNotBlank(retryAfter)) {
            try {
                delay = Long.parseLong(retryAfter.trim()) * 1000;
            } catch (NumberFormatException e) {
                try {
                    Instant retryAt = ZonedDateTime.parse(retryAfter.trim(), DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
                    delay = Math.max(0, Duration.between(Instant.now(), retryAt).toMillis());
                } catch (Exception ignored) {
                    // Use the bounded default delay.
                }
            }
        }
        return delay < 0 || delay > MAX_RETRY_DELAY_MILLIS ? -1 : delay;
    }

    private static ThirdPartyApiException apiError(String message) {
        return ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_API_ERROR, message);
    }

    private static ThirdPartyApiException apiError(String message, Throwable cause) {
        return ThirdPartyApiException.as().with(cause, GitlabI18nKeys.GITLAB_SCM_API_ERROR, message);
    }

    private static ThirdPartyApiException archiveLimit(String message) {
        return ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_ARCHIVE_LIMIT, message);
    }
}
