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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.clougence.clouddm.sdk.scm.*;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class GiteeDevopsScmProviderSpiTest {

    @Rule
    public TemporaryFolder            temporaryFolder = new TemporaryFolder();

    private MockWebServer             server;
    private GiteeDevopsScmProviderSpi provider;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        HttpUrl local = server.url("/");
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl rewritten = original.url().newBuilder().scheme(local.scheme()).host(local.host()).port(local.port()).build();
            return chain.proceed(original.newBuilder().url(rewritten).build());
        }).build();
        provider = new GiteeDevopsScmProviderSpi(client);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void shouldSendPatOnlyInAuthorizationHeaderAndUseStableRepoId() throws Exception {
        server.enqueue(json(repositoriesJson()));

        List<ScmRepo> repos = provider.fetchRepoList("https://gitee.com/", "pat-secret", "database");

        assertEquals(1, repos.size());
        assertEquals("team/database", repos.get(0).getRepoId());
        RecordedRequest request = server.takeRequest();
        assertEquals("token pat-secret", request.getHeader("Authorization"));
        assertFalse(request.getPath().contains("pat-secret"));
    }

    @Test
    public void shouldResolveBranchAndPreflightAtExactCommit() throws Exception {
        String commit = "a".repeat(40);
        server.enqueue(json("[{\"name\":\"main\",\"commit\":{\"sha\":\"" + commit + "\"}}]"));
        server.enqueue(json("{\"sha\":\"" + commit + "\",\"truncated\":false,\"tree\":[" + "{\"path\":\"scripts\",\"type\":\"tree\",\"sha\":\"tree1\"},"
                            + "{\"path\":\"scripts/001.sql\",\"type\":\"blob\",\"sha\":\"blob1\"},"
                            + "{\"path\":\"scripts/readme.md\",\"type\":\"blob\",\"sha\":\"blob2\"}]}"));
        ScmRepo repo = repo(commit);

        List<ScmBranch> branches = provider.fetchBranchList("https://gitee.com/", "pat-secret", repo, "main", true);
        ScmPathValidation validation = provider.validateScriptPath("https://gitee.com/", "pat-secret", repo, "/scripts/");

        assertEquals(commit, branches.get(0).getCommitId());
        assertTrue(validation.isChecked());
        assertEquals(2, validation.getFileCount());
        assertEquals(1, validation.getSqlFileCount());
        assertEquals("token pat-secret", server.takeRequest().getHeader("Authorization"));
        RecordedRequest treeRequest = server.takeRequest();
        assertEquals("/api/v5/repos/team/database/git/trees/" + commit, treeRequest.getRequestUrl().encodedPath());
        assertEquals("1", treeRequest.getRequestUrl().queryParameter("recursive"));
        assertFalse(treeRequest.getPath().contains("pat-secret"));
    }

    @Test
    public void shouldDownloadArchiveByExactCommitWithoutLeakingPat() throws Exception {
        String commit = "b".repeat(40);
        server.enqueue(json(repositoriesJson()));
        server.enqueue(new MockResponse().setResponseCode(200).setBody(new Buffer().write(zip("database-" + commit + "/scripts/001.sql", "select 1;"))));
        File target = temporaryFolder.newFolder("target");
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(target);
        saveTo.setTempPath(new File(temporaryFolder.getRoot(), "temp"));
        saveTo.setScriptPath("scripts");
        ScmProvider scm = new ScmProvider();
        scm.setServiceUrl("https://gitee.com/");
        scm.setAccessToken("pat-secret");

        provider.downloadToLocal(scm, repo(commit), saveTo, () -> true);

        assertEquals("select 1;", Files.readString(new File(target, "scripts/001.sql").toPath()));
        server.takeRequest();
        RecordedRequest archiveRequest = server.takeRequest();
        assertEquals(commit, archiveRequest.getRequestUrl().queryParameter("ref"));
        assertEquals("token pat-secret", archiveRequest.getHeader("Authorization"));
        assertFalse(archiveRequest.getPath().contains("pat-secret"));
    }

    @Test
    public void shouldCleanTemporaryAndTargetDirectoriesWhenDownloadFails() throws Exception {
        String commit = "c".repeat(40);
        server.enqueue(json(repositoriesJson()));
        server.enqueue(new MockResponse().setResponseCode(500).setBody("archive failed"));
        File target = new File(temporaryFolder.getRoot(), "failed-target");
        File temp = new File(temporaryFolder.getRoot(), "failed-temp");
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(target);
        saveTo.setTempPath(temp);
        ScmProvider scm = new ScmProvider();
        scm.setServiceUrl("https://gitee.com/");
        scm.setAccessToken("pat-secret");

        try {
            provider.downloadToLocal(scm, repo(commit), saveTo, () -> true);
            fail("failed archive download must be reported");
        } catch (RuntimeException expected) {
            assertFalse(temp.exists());
            assertFalse(target.exists());
        }
    }

    @Test
    public void shouldRejectSymbolicLinksAndCleanExtractedFiles() throws Exception {
        String commit = "d".repeat(40);
        server.enqueue(json(repositoriesJson()));
        server.enqueue(new MockResponse().setResponseCode(200)
            .setBody(new Buffer().write(symlinkZip("database-" + commit + "/scripts/link.sql", "../../escaped.sql"))));
        File target = new File(temporaryFolder.getRoot(), "symlink-target");
        File temp = new File(temporaryFolder.getRoot(), "symlink-temp");
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(target);
        saveTo.setTempPath(temp);
        saveTo.setScriptPath("scripts");
        ScmProvider scm = new ScmProvider();
        scm.setServiceUrl("https://gitee.com/");
        scm.setAccessToken("pat-secret");

        try {
            provider.downloadToLocal(scm, repo(commit), saveTo, () -> true);
            fail("symbolic links must be rejected");
        } catch (RuntimeException expected) {
            assertFalse(temp.exists());
            assertFalse(target.exists());
        }
    }

    @Test
    public void shouldCleanPartialExtractionWhenWatchdogCancels() throws Exception {
        String commit = "e".repeat(40);
        server.enqueue(json(repositoriesJson()));
        server.enqueue(new MockResponse().setResponseCode(200)
            .setBody(new Buffer().write(zip("database-" + commit + "/scripts/001.sql", "select 1;"))));
        File target = new File(temporaryFolder.getRoot(), "cancel-target");
        File temp = new File(temporaryFolder.getRoot(), "cancel-temp");
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(target);
        saveTo.setTempPath(temp);
        saveTo.setScriptPath("scripts");
        ScmProvider scm = new ScmProvider();
        scm.setServiceUrl("https://gitee.com/");
        scm.setAccessToken("pat-secret");
        AtomicInteger checks = new AtomicInteger();

        try {
            provider.downloadToLocal(scm, repo(commit), saveTo, () -> checks.incrementAndGet() == 1);
            fail("watchdog cancellation must interrupt extraction");
        } catch (RuntimeException expected) {
            assertFalse(temp.exists());
            assertFalse(target.exists());
        }
    }

    private static ScmRepo repo(String commit) {
        ScmRepo repo = new ScmRepo();
        repo.setRepoId("team/database");
        repo.setRepoSpace("team");
        repo.setRepoName("database");
        repo.setCommitId(commit);
        return repo;
    }

    private static MockResponse json(String body) {
        return new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(body);
    }

    private static String repositoriesJson() {
        return "[{\"id\":1,\"full_name\":\"team/database\",\"path\":\"database\",\"name\":\"Database\","
               + "\"html_url\":\"https://gitee.com/team/database\",\"default_branch\":\"main\"," + "\"namespace\":{\"path\":\"team\"}}]";
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
}
