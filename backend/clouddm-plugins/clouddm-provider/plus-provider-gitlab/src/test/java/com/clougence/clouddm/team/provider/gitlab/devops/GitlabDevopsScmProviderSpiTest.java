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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.scm.*;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class GitlabDevopsScmProviderSpiTest {

    @Rule
    public TemporaryFolder             temporaryFolder = new TemporaryFolder();

    private MockWebServer              server;
    private GitlabDevopsScmProviderSpi provider;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        provider = new GitlabDevopsScmProviderSpi(new OkHttpClient());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void shouldNormalizeSelfManagedWebUrls() {
        assertEquals("http://gitlab.example:8088/gitlab", GitlabApiClient.normalizeServiceUrl(" http://gitlab.example:8088/gitlab/// "));
        assertInvalidUrl("https://gitlab.example/api/v4");
        assertInvalidUrl("https://user:secret@gitlab.example/gitlab");
        assertInvalidUrl("https://gitlab.example/gitlab?token=secret");
    }

    @Test
    public void shouldUseProjectIdsAndKeepPatOutOfUrls() throws Exception {
        server
            .enqueue(json("[{\"id\":101,\"path\":\"db\",\"path_with_namespace\":\"group/a/db\","
                          + "\"web_url\":\"https://gitlab.example/group/a/db\",\"default_branch\":\"main\","
                          + "\"archived\":false,\"empty_repo\":false,\"repository_access_level\":\"enabled\"},"
                          + "{\"id\":202,\"path\":\"db\",\"path_with_namespace\":\"group/b/db\","
                          + "\"web_url\":\"https://gitlab.example/group/b/db\",\"default_branch\":null,"
                          + "\"archived\":false,\"empty_repo\":true,\"repository_access_level\":\"enabled\"}]"));

        List<ScmRepo> repos = provider.fetchRepoList(serviceUrl(), "pat-secret", "db");

        assertEquals(2, repos.size());
        assertEquals("101", repos.get(0).getRepoId());
        assertEquals("group/a/db", repos.get(0).getRepoPath());
        assertEquals("group/a", repos.get(0).getRepoSpace());
        assertFalse(repos.get(0).isEmpty());
        assertEquals("202", repos.get(1).getRepoId());
        assertTrue(repos.get(1).isEmpty());
        RecordedRequest request = server.takeRequest();
        assertEquals("pat-secret", request.getHeader("PRIVATE-TOKEN"));
        assertTrue(request.getPath().startsWith("/gitlab/api/v4/projects?"));
        assertFalse(request.getPath().contains("pat-secret"));
        assertNull(request.getRequestUrl().queryParameter("membership"));
    }

    @Test
    public void shouldFetchServerVersionFromSelfManagedApi() throws Exception {
        server.enqueue(json("{\"version\":\"19.2.0-ee\",\"revision\":\"abc123\"}"));

        assertEquals("19.2.0-ee", provider.fetchServerVersion(serviceUrl(), "pat-secret"));

        RecordedRequest request = server.takeRequest();
        assertEquals("/gitlab/api/v4/version", request.getRequestUrl().encodedPath());
        assertEquals("pat-secret", request.getHeader("PRIVATE-TOKEN"));
    }

    @Test
    public void shouldFollowSameOriginRedirectsRetryAndRejectCrossOrigin() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(302).setHeader("Location", server.url("/gitlab/api/v4/projects?page=1")));
        server.enqueue(new MockResponse().setResponseCode(429).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(json("[]"));

        assertTrue(provider.fetchRepoList(serviceUrl(), "pat-secret", "").isEmpty());
        for (int i = 0; i < 4; i++) {
            assertEquals("pat-secret", server.takeRequest().getHeader("PRIVATE-TOKEN"));
        }

        MockWebServer redirectTarget = new MockWebServer();
        redirectTarget.start();
        try {
            server.enqueue(new MockResponse().setResponseCode(302).setHeader("Location", redirectTarget.url("/token-target")));
            try {
                provider.fetchRepoList(serviceUrl(), "pat-secret", "");
                fail("cross-origin redirect must be rejected");
            } catch (ThirdPartyApiException expected) {
                assertNull(redirectTarget.takeRequest(200, TimeUnit.MILLISECONDS));
            }
        } finally {
            redirectTarget.shutdown();
        }
    }

    @Test
    public void shouldFetchExactProjectAndBranchAtImmutableCommit() throws Exception {
        String commit = repeat('a', 40);
        server.enqueue(json("{\"id\":101,\"path\":\"renamed\",\"path_with_namespace\":\"new/group/renamed\","
                            + "\"web_url\":\"https://gitlab.example/new/group/renamed\",\"default_branch\":\"main\","
                            + "\"archived\":false,\"empty_repo\":false,\"repository_access_level\":\"enabled\"}"));
        server.enqueue(json("[{\"name\":\"main\",\"commit\":{\"id\":\"" + commit + "\"}}]"));
        ScmRepo selection = new ScmRepo();
        selection.setRepoId("101");

        ScmRepo repo = provider.fetchRepo(serviceUrl(), "pat-secret", selection);
        List<ScmBranch> branches = provider.fetchBranchList(serviceUrl(), "pat-secret", repo, "main", true);

        assertEquals("new/group/renamed", repo.getRepoPath());
        assertEquals(commit, branches.get(0).getCommitId());
        assertEquals("/gitlab/api/v4/projects/101", server.takeRequest().getRequestUrl().encodedPath());
        RecordedRequest branchRequest = server.takeRequest();
        assertEquals("/gitlab/api/v4/projects/101/repository/branches", branchRequest.getRequestUrl().encodedPath());
        assertEquals("main", branchRequest.getRequestUrl().queryParameter("search"));
    }

    @Test
    public void shouldValidateScriptDirectoryAndCountSqlFiles() throws Exception {
        String commit = repeat('b', 40);
        server.enqueue(json("[{\"id\":\"tree1\",\"name\":\"scripts\",\"path\":\"scripts\",\"type\":\"tree\"}]"));
        server.enqueue(json("[{\"id\":\"blob1\",\"name\":\"001.sql\",\"path\":\"scripts/001.sql\",\"type\":\"blob\"},"
                            + "{\"id\":\"blob2\",\"name\":\"readme.txt\",\"path\":\"scripts/readme.txt\",\"type\":\"blob\"}]"));
        ScmRepo repo = repo("101", commit);

        ScmPathValidation result = provider.validateScriptPath(serviceUrl(), "pat-secret", repo, "/scripts/");

        assertTrue(result.isChecked());
        assertEquals(2, result.getFileCount());
        assertEquals(1, result.getSqlFileCount());
        assertEquals("", server.takeRequest().getRequestUrl().queryParameter("path"));
        RecordedRequest recursive = server.takeRequest();
        assertEquals("scripts", recursive.getRequestUrl().queryParameter("path"));
        assertEquals(commit, recursive.getRequestUrl().queryParameter("ref"));
    }

    @Test
    public void shouldRejectMalformedRepositoryTreeResponses() {
        String commit = repeat('b', 40);
        server.enqueue(json("{}"));

        assertThrows(ThirdPartyApiException.class, () -> provider.validateScriptPath(serviceUrl(), "pat-secret", repo("101", commit), ""));
    }

    @Test
    public void shouldVerifySignedWebhookAndAllowLegacyFallbackWithoutSignature() throws Exception {
        byte[] key = "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8);
        String signingToken = "whsec_" + Base64.getEncoder().encodeToString(key);
        String body = "{\"object_kind\":\"push\"}";
        Instant now = Instant.ofEpochSecond(1_700_000_000L);
        String messageId = "delivery-1";
        String timestamp = Long.toString(now.getEpochSecond());
        String signature = sign(key, messageId + "." + timestamp + "." + body);
        Map<String, List<String>> headers = Map.of("webhook-id", List.of(messageId), "webhook-timestamp", List.of(timestamp), "webhook-signature", List
            .of("v1,invalid " + signature), "X-Gitlab-Token", List.of("legacy-secret"));

        GitlabWebhookParser.verifyWebhook(headers, body, "legacy-secret", signingToken, now);

        Map<String, List<String>> invalidSignature = Map
            .of("webhook-id", List.of(messageId),
                "webhook-timestamp", List.of(timestamp),
                "webhook-signature", List.of("v1,invalid"),
                "X-Gitlab-Token", List.of("legacy-secret"));
        assertWebhookStatus(401, () -> GitlabWebhookParser.verifyWebhook(invalidSignature, body, "legacy-secret", signingToken, now));
        assertWebhookStatus(401, () -> GitlabWebhookParser.verifyWebhook(headers, body, "legacy-secret", signingToken, now.plusSeconds(301)));
        GitlabWebhookParser.verifyWebhook(Map.of("X-Gitlab-Token", List.of("legacy-secret")), body, "legacy-secret", signingToken, now);
        GitlabWebhookParser.verifyWebhook(Map.of("X-Gitlab-Token", List.of("legacy-secret")), body, "legacy-secret", null, now);
    }

    @Test
    public void shouldParsePushAndMergedRequestUsingExactCommit() {
        String pushCommit = repeat('c', 40);
        String push = "{\"object_kind\":\"push\",\"event_created_at\":\"2026-07-21T12:00:00+00:00\","
                      + "\"checkout_sha\":\"" + pushCommit + "\",\"before\":\"" + repeat('b', 40)
                      + "\"," + "\"after\":\"" + pushCommit + "\",\"ref\":\"refs/heads/main\","
                      + "\"user_id\":7,\"user_name\":\"Alice\",\"user_username\":\"alice\",\"user_email\":\"a@example.com\","
                      + "\"project\":{\"id\":101,\"path\":\"db\",\"path_with_namespace\":\"group/sub/db\","
                      + "\"web_url\":\"https://gitlab.example/group/sub/db\"}}";
        ScmEvent pushEvent = provider
            .readEvent(serviceUrl(), "pat", "101", "group/sub", "db", "legacy", null,
                Map.of("X-Gitlab-Token", List.of("legacy"),
                    "webhook-id", List.of("push-delivery")),
                push);

        assertEquals(ScmEventType.Push, pushEvent.getEventType());
        assertEquals(ScmEventStatus.Update, pushEvent.getStatus());
        assertEquals(pushCommit, pushEvent.getEventId());
        assertEquals("101", pushEvent.getTarRepoId());
        assertEquals("group/sub", pushEvent.getTarRepoPath());
        assertEquals("main", pushEvent.getTarRepoBranch());
        assertEquals("push-delivery", pushEvent.getDeliveryId());

        String mergeCommit = repeat('d', 40);
        String mergeRequest = "{\"object_kind\":\"merge_request\",\"event_created_at\":\"2026-07-21T12:00:00Z\","
                              + "\"user\":{\"id\":8,\"name\":\"Bob\",\"username\":\"bob\",\"email\":\"b@example.com\"},"
                              + "\"project\":{\"id\":101,\"path\":\"db\",\"path_with_namespace\":\"group/sub/db\","
                              + "\"web_url\":\"https://gitlab.example/group/sub/db\"},"
                              + "\"object_attributes\":{\"iid\":12,\"action\":\"merge\",\"state\":\"merged\","
                              + "\"target_branch\":\"main\",\"source_branch\":\"feature\",\"source_project_id\":202,"
                              + "\"source\":{\"id\":202,\"path\":\"fork\",\"path_with_namespace\":\"forks/fork\"}," + "\"merge_commit_sha\":\"" + mergeCommit
                              + "\",\"title\":\"change\",\"description\":\"sql\"}}";
        ScmEvent mrEvent = provider.readEvent(serviceUrl(), "pat", "101", "group/sub", "db", "legacy", null,
            Map.of("X-Gitlab-Token", List.of("legacy"), "X-Gitlab-Webhook-UUID", List.of("hook-1"), "X-Gitlab-Event-UUID", List.of("event-1")),
            mergeRequest);

        assertEquals(ScmEventType.PullRequest, mrEvent.getEventType());
        assertEquals(ScmEventStatus.Merged, mrEvent.getStatus());
        assertEquals(mergeCommit, mrEvent.getEventId());
        assertEquals("202", mrEvent.getSrcRepoId());
        assertEquals("forks/fork", mrEvent.getSrcRepoPath());
        assertEquals("event-1", mrEvent.getDeliveryId());
        assertEquals("hook-1", mrEvent.getHookId());
    }

    @Test
    public void shouldResolveMergedCommitFromApiAndRetryWhenItIsNotReady() throws Exception {
        String mergeCommit = repeat('f', 40);
        String mergeRequest = "{\"object_kind\":\"merge_request\","
                              + "\"project\":{\"id\":101,\"path\":\"db\",\"path_with_namespace\":\"group/sub/db\"},"
                              + "\"object_attributes\":{\"iid\":12,\"action\":\"merge\",\"state\":\"merged\","
                              + "\"target_branch\":\"main\",\"source_branch\":\"feature\"}}";
        server.enqueue(json("{\"merge_commit_sha\":\"" + mergeCommit + "\",\"squash_commit_sha\":null}"));

        ScmEvent event = provider
            .readEvent(serviceUrl(), "pat-secret", "101", "group/sub", "db", "legacy", null, Map.of("X-Gitlab-Token", List.of("legacy")), mergeRequest);

        assertEquals(mergeCommit, event.getEventId());
        RecordedRequest request = server.takeRequest();
        assertEquals("/gitlab/api/v4/projects/101/merge_requests/12", request.getRequestUrl().encodedPath());
        assertEquals("pat-secret", request.getHeader("PRIVATE-TOKEN"));

        server.enqueue(json("{\"merge_commit_sha\":null,\"squash_commit_sha\":null}"));
        assertWebhookStatus(503,
            () -> provider.readEvent(serviceUrl(), "pat-secret", "101", "group/sub", "db", "legacy", null,
                Map.of("X-Gitlab-Token", List.of("legacy")), mergeRequest));
    }

    @Test
    public void shouldDownloadExactCommitAndRejectZipSlip() throws Exception {
        String commit = repeat('e', 40);
        server.enqueue(new MockResponse().setResponseCode(200).setBody(new Buffer().write(zip("project-" + commit + "/scripts/001.sql", "select 1;"))));
        File target = temporaryFolder.newFolder("valid-target");
        ScmProvider scm = scm();
        ScmSaveTo saveTo = saveTo(target, new File(temporaryFolder.getRoot(), "valid-temp"), "");

        provider.downloadToLocal(scm, repo("101", commit), saveTo, () -> true);

        assertEquals("select 1;", Files.readString(new File(target, "scripts/001.sql").toPath()));
        RecordedRequest archiveRequest = server.takeRequest();
        assertEquals(commit, archiveRequest.getRequestUrl().queryParameter("sha"));
        assertEquals("true", archiveRequest.getRequestUrl().queryParameter("include_lfs_blobs"));
        assertEquals("pat-secret", archiveRequest.getHeader("PRIVATE-TOKEN"));
        assertFalse(archiveRequest.getPath().contains("pat-secret"));

        server.enqueue(new MockResponse().setResponseCode(200).setBody(new Buffer().write(zip("project-" + commit + "/../../escaped.sql", "bad"))));
        File unsafeTarget = temporaryFolder.newFolder("unsafe-target");
        File escaped = new File(temporaryFolder.getRoot(), "escaped.sql");
        try {
            provider.downloadToLocal(scm, repo("101", commit), saveTo(unsafeTarget, new File(temporaryFolder.getRoot(), "unsafe-temp"), ""), () -> true);
            fail("zip slip must be rejected");
        } catch (ThirdPartyApiException expected) {
            assertFalse(escaped.exists());
        }

        server.enqueue(new MockResponse().setResponseCode(200)
            .setBody(new Buffer().write(symlinkZip("project-" + commit + "/scripts/link.sql", "../../escaped.sql"))));
        File symlinkTarget = temporaryFolder.newFolder("symlink-target");
        try {
            provider.downloadToLocal(scm, repo("101", commit), saveTo(symlinkTarget, new File(temporaryFolder.getRoot(), "symlink-temp"), ""), () -> true);
            fail("symbolic links must be rejected");
        } catch (ThirdPartyApiException expected) {
            assertFalse(symlinkTarget.exists());
        }
    }

    private String serviceUrl() {
        return server.url("/gitlab/").toString();
    }

    private ScmProvider scm() {
        ScmProvider scm = new ScmProvider();
        scm.setServiceUrl(serviceUrl());
        scm.setAccessToken("pat-secret");
        return scm;
    }

    private static ScmRepo repo(String id, String commit) {
        ScmRepo repo = new ScmRepo();
        repo.setRepoId(id);
        repo.setRepoName("db");
        repo.setRepoSpace("group/sub");
        repo.setCommitId(commit);
        return repo;
    }

    private static ScmSaveTo saveTo(File target, File temp, String path) {
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(target);
        saveTo.setTempPath(temp);
        saveTo.setScriptPath(path);
        return saveTo;
    }

    private static MockResponse json(String body) {
        return new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(body);
    }

    private static byte[] zip(String path, String contents) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
            zip.putNextEntry(new ZipEntry(path));
            zip.write(contents.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        return bytes.toByteArray();
    }

    private static byte[] symlinkZip(String path, String linkTarget) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream zip = new ZipArchiveOutputStream(bytes)) {
            ZipArchiveEntry entry = new ZipArchiveEntry(path);
            entry.setUnixMode(UnixStat.LINK_FLAG | 0777);
            zip.putArchiveEntry(entry);
            zip.write(linkTarget.getBytes(StandardCharsets.UTF_8));
            zip.closeArchiveEntry();
        }
        return bytes.toByteArray();
    }

    private static String sign(byte[] key, String message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return "v1," + Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }

    private static String repeat(char value, int count) {
        return String.valueOf(value).repeat(count);
    }

    private static void assertInvalidUrl(String url) {
        try {
            GitlabApiClient.normalizeServiceUrl(url);
            fail("URL must be rejected: " + url);
        } catch (ThirdPartyApiException expected) {
            // expected
        }
    }

    private static void assertWebhookStatus(int status, Runnable runnable) {
        try {
            runnable.run();
            fail("webhook must be rejected");
        } catch (ScmWebhookException expected) {
            assertEquals(status, expected.getStatusCode());
        }
    }
}
