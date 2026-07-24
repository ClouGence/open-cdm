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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.scm.ScmPathUtils;
import com.clougence.clouddm.sdk.scm.ScmPathValidation;
import com.clougence.clouddm.sdk.scm.ScmProvider;
import com.clougence.clouddm.sdk.scm.ScmRepo;
import com.clougence.clouddm.sdk.scm.ScmSaveTo;
import com.clougence.clouddm.team.provider.gitlab.constants.GitlabI18nKeys;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;
import com.clougence.utils.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;

import okhttp3.HttpUrl;
import okhttp3.Response;

final class GitlabArchiveService {

    /**
     * Maximum compressed archive size accepted from GitLab, in bytes.
     */
    private static final long    MAX_ARCHIVE_BYTES   = 1024L * 1024 * 1024;
    /**
     * Maximum cumulative uncompressed archive size, in bytes.
     */
    private static final long    MAX_EXTRACTED_BYTES = 2L * 1024 * 1024 * 1024;
    /**
     * Maximum uncompressed size of one SQL file, in bytes.
     */
    private static final long    MAX_SQL_BYTES       = 50L * 1024 * 1024;
    /**
     * Maximum number of tree or archive entries inspected during one download.
     */
    private static final int     MAX_FILES           = 10_000;
    /**
     * Buffer size used while downloading and extracting archives.
     */
    private static final int     ARCHIVE_BUFFER_SIZE = 8192;
    /**
     * Accepted immutable Git object identifier format.
     */
    private static final Pattern COMMIT_SHA_PATTERN  = Pattern.compile("(?i)^[0-9a-f]{40,64}$");

    private final GitlabApiClient apiClient;

    GitlabArchiveService(GitlabApiClient apiClient){
        this.apiClient = apiClient;
    }

    ScmPathValidation validateScriptPath(String serviceUrl, String accessToken,
                                         ScmRepo repo, String scriptPath) {
        validateRepositoryCommit(repo);
        String normalizedPath = normalizeScriptPath(scriptPath);
        ScmProvider scm = buildScmProvider(serviceUrl, accessToken);
        ensureScriptPathIsNotSubmodule(scm, repo, normalizedPath);
        return apiClient.inspectScriptFiles(serviceUrl, accessToken, repo, normalizedPath);
    }

    void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo,
                         ESupplier<Boolean, Exception> watchdog) throws Exception {
        validateDownloadRequest(repo);
        String scriptPath = normalizeScriptPath(saveTo.getScriptPath());
        ensureScriptPathIsNotSubmodule(scm, repo, scriptPath);

        File tempDir = saveTo.getTempPath();
        File archive = new File(tempDir, "gitlab-repository.zip");
        try {
            Files.createDirectories(tempDir.toPath());
            downloadArchive(scm, repo, scriptPath, archive, watchdog);
            extractArchive(archive, saveTo.getSaveToLocal(), scriptPath, watchdog);
        } catch (ThirdPartyApiException e) {
            FileUtils.deleteQuietly(saveTo.getSaveToLocal());
            throw e;
        } catch (Exception e) {
            FileUtils.deleteQuietly(saveTo.getSaveToLocal());
            throw downloadError(e.getMessage(), e);
        } finally {
            FileUtils.deleteQuietly(archive);
            FileUtils.deleteQuietly(tempDir);
        }
    }

    private void validateRepositoryCommit(ScmRepo repo) {
        if (repo == null
            || StringUtils.isBlank(repo.getRepoId())
            || StringUtils.isBlank(repo.getCommitId())) {
            throw apiError("project identifier and commit SHA are required");
        }
    }

    private void validateDownloadRequest(ScmRepo repo) {
        if (repo == null || StringUtils.isBlank(repo.getRepoId())) {
            throw downloadError("missing project identifier", null);
        }
        if (StringUtils.isBlank(repo.getCommitId())
            || !COMMIT_SHA_PATTERN.matcher(repo.getCommitId()).matches()) {
            throw downloadError("missing or invalid immutable commit SHA", null);
        }
    }

    private ScmProvider buildScmProvider(String serviceUrl, String accessToken) {
        ScmProvider provider = new ScmProvider();
        provider.setServiceUrl(serviceUrl);
        provider.setAccessToken(accessToken);
        return provider;
    }

    private void ensureScriptPathIsNotSubmodule(ScmProvider scm, ScmRepo repo, String scriptPath) {
        if (StringUtils.isBlank(scriptPath)) {
            return;
        }
        String current = "";
        for (String segment : scriptPath.split("/")) {
            JsonNode entry = findTreeEntry(scm, repo, current, segment);
            if (entry == null) {
                throw downloadError("script directory does not exist at commit", null);
            }
            if ("commit".equals(entry.path("type").asText())) {
                throw downloadError("script directory is inside a Git submodule", null);
            }
            if (!"tree".equals(entry.path("type").asText())) {
                throw downloadError("script path is not a directory", null);
            }
            if (current.isEmpty()) {
                current = segment;
            } else {
                current = current + "/" + segment;
            }
        }
    }

    private JsonNode findTreeEntry(ScmProvider scm, ScmRepo repo, String path, String name) {
        int page = 1;
        while (!GitlabApiClient.pageLimitExceeded(page, MAX_FILES)) {
            HttpUrl url = buildTreeEntryUrl(scm, repo, path, page);
            try (Response response = apiClient.execute(url, scm.getAccessToken())) {
                JsonNode entries = JsonUtils.defaultObjectMapper().readTree(apiClient.requireSuccessful(response));
                if (!entries.isArray()) {
                    throw downloadError("unexpected repository tree response", null);
                }
                for (JsonNode entry : entries) {
                    if (name.equals(entry.path("name").asText())) {
                        return entry;
                    }
                }
                page = GitlabApiClient.nextPage(response);
                if (page == GitlabApiClient.NO_NEXT_PAGE) {
                    return null;
                }
            } catch (ThirdPartyApiException e) {
                throw e;
            } catch (Exception e) {
                throw downloadError(e.getMessage(), e);
            }
        }
        throw archiveLimit("more than 10000 entries in script path");
    }

    private HttpUrl buildTreeEntryUrl(ScmProvider scm, ScmRepo repo, String path, int page) {
        return GitlabApiClient
            .projectEndpoint(
                GitlabApiClient.apiRoot(scm.getServiceUrl()),
                repo.getRepoId(),
                "repository",
                "tree")
            .newBuilder()
            .addQueryParameter("ref", repo.getCommitId())
            .addQueryParameter("path", path)
            .addQueryParameter("per_page", Integer.toString(GitlabApiClient.API_PAGE_SIZE))
            .addQueryParameter("page", Integer.toString(page))
            .build();
    }

    private void downloadArchive(ScmProvider scm, ScmRepo repo, String scriptPath,
                                 File destination, ESupplier<Boolean, Exception> watchdog) throws Exception {
        HttpUrl url = buildArchiveUrl(scm.getServiceUrl(), repo, scriptPath);
        try (Response response = apiClient.execute(url, scm.getAccessToken())) {
            if (!response.isSuccessful()) {
                throw downloadError(response.code() + ":" + response.message(), null);
            }
            if (response.body().contentLength() > MAX_ARCHIVE_BYTES) {
                throw archiveLimit("compressed archive exceeds 1 GiB");
            }
            copyArchiveResponse(response, destination, watchdog);
        }
    }

    private HttpUrl buildArchiveUrl(String serviceUrl, ScmRepo repo, String scriptPath) {
        HttpUrl.Builder url = GitlabApiClient
            .projectEndpoint(
                GitlabApiClient.apiRoot(serviceUrl),
                repo.getRepoId(),
                "repository",
                "archive.zip")
            .newBuilder()
            .addQueryParameter("sha", repo.getCommitId())
            .addQueryParameter("include_lfs_blobs", "true");
        if (StringUtils.isNotBlank(scriptPath)) {
            url.addQueryParameter("path", scriptPath);
        }
        return url.build();
    }

    private void copyArchiveResponse(Response response, File destination,
                                     ESupplier<Boolean, Exception> watchdog) throws Exception {
        long total = 0;
        try (InputStream input = response.body().byteStream();
             FileOutputStream output = new FileOutputStream(destination)) {
            byte[] buffer = new byte[ARCHIVE_BUFFER_SIZE];
            int read;
            while ((read = input.read(buffer)) != -1) {
                if (!watchdog.eGet()) {
                    throw downloadError("download interrupted", null);
                }
                total += read;
                if (total > MAX_ARCHIVE_BYTES) {
                    throw archiveLimit("compressed archive exceeds 1 GiB");
                }
                output.write(buffer, 0, read);
            }
        }
    }

    private void extractArchive(File archive, File targetDir, String scriptPath,
                                ESupplier<Boolean, Exception> watchdog) throws Exception {
        Path root = targetDir.toPath().toAbsolutePath().normalize();
        Files.createDirectories(root);
        long extracted = 0;
        int files = 0;
        try (ZipFile zip = ZipFile.builder().setFile(archive).get()) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String relative = removeArchiveRoot(entry.getName());
                if (shouldSkipArchiveEntry(entry, relative, scriptPath)) {
                    continue;
                }
                files++;
                if (files > MAX_FILES) {
                    throw archiveLimit("more than 10000 files");
                }
                Path output = resolveArchiveOutput(root, relative);
                extracted += extractArchiveEntry(
                    zip,
                    entry,
                    output,
                    relative,
                    extracted,
                    watchdog);
            }
        }
    }

    private boolean shouldSkipArchiveEntry(ZipArchiveEntry entry, String relative,
                                           String scriptPath) {
        if (entry.isDirectory()
            || StringUtils.isBlank(relative)
            || !isInsidePath(relative, scriptPath)) {
            return true;
        }
        if (entry.isUnixSymlink()) {
            throw archiveLimit("symbolic links are not allowed: " + relative);
        }
        return false;
    }

    private Path resolveArchiveOutput(Path root, String relative) throws Exception {
        Path output = root.resolve(relative).normalize();
        if (!output.startsWith(root)) {
            throw archiveLimit("unsafe archive path");
        }
        Files.createDirectories(output.getParent());
        return output;
    }

    private long extractArchiveEntry(ZipFile zip, ZipArchiveEntry entry, Path output,
                                     String relative, long extracted,
                                     ESupplier<Boolean, Exception> watchdog) throws Exception {
        long fileBytes = 0;
        try (InputStream input = zip.getInputStream(entry);
             FileOutputStream stream = new FileOutputStream(output.toFile())) {
            byte[] buffer = new byte[ARCHIVE_BUFFER_SIZE];
            int read;
            while ((read = input.read(buffer)) != -1) {
                if (!watchdog.eGet()) {
                    throw downloadError("archive extraction interrupted", null);
                }
                fileBytes += read;
                validateExtractedSize(relative, extracted + fileBytes, fileBytes);
                stream.write(buffer, 0, read);
            }
        }
        return fileBytes;
    }

    private void validateExtractedSize(String relative, long extractedBytes, long fileBytes) {
        if (extractedBytes > MAX_EXTRACTED_BYTES) {
            throw archiveLimit("extracted archive exceeds 2 GiB");
        }
        if (relative.toLowerCase(Locale.ROOT).endsWith(".sql") && fileBytes > MAX_SQL_BYTES) {
            throw archiveLimit("SQL file exceeds 50 MiB: " + relative);
        }
    }

    private static String normalizeScriptPath(String value) {
        try {
            return ScmPathUtils.normalizeDirectoryPath(value);
        } catch (IllegalArgumentException e) {
            throw archiveLimit("invalid script path");
        }
    }

    private static String removeArchiveRoot(String name) {
        String normalized = name.replace('\\', '/');
        int slash = normalized.indexOf('/');
        return slash < 0 ? "" : normalized.substring(slash + 1);
    }

    private static boolean isInsidePath(String file, String keepPath) {
        return StringUtils.isBlank(keepPath)
               || file.equals(keepPath)
               || file.startsWith(keepPath + "/");
    }

    private static ThirdPartyApiException apiError(String message) {
        return ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_API_ERROR, message);
    }

    private static ThirdPartyApiException downloadError(String message, Throwable cause) {
        if (cause == null) {
            return ThirdPartyApiException.as().with(GitlabI18nKeys.GITLAB_SCM_DOWNLOAD_ERROR, message);
        }
        return ThirdPartyApiException.as().with(
            cause,
            GitlabI18nKeys.GITLAB_SCM_DOWNLOAD_ERROR,
            message);
    }

    private static ThirdPartyApiException archiveLimit(String message) {
        return ThirdPartyApiException.as().with(
            GitlabI18nKeys.GITLAB_SCM_ARCHIVE_LIMIT,
            message);
    }
}
